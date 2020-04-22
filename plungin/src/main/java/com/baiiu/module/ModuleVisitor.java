package com.baiiu.module;

import com.baiiu.interfaces.annotation.RouterService;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * author: zhuzhe
 * time: 2020-01-09
 * description:
 */
public class ModuleVisitor extends ClassVisitor {

    private boolean isApplicationClass = false;
    private boolean hasOnCreateMethod = false;
    private boolean isDirectory;
    private RunAlone mRunAlone;


    public ModuleVisitor(ClassVisitor classVisitor, RunAlone runAlone, boolean isDirectory) {
        super(Opcodes.ASM6, classVisitor);
        this.mRunAlone = runAlone;
        this.isDirectory = isDirectory;
    }

    /**
     * 当ASM进入类时回调
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName,
            String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        isApplicationClass = superName.contains("Application") && isDirectory;

        System.out.println(
                "name: " + name
                + ", superName: " + superName + ", "
                + ", isDirectory: " + isDirectory
                + ", isApplicationClass: " + isApplicationClass);
    }

    @Override public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        System.out.println("visitAnnotation: " + descriptor + ", " + visible);

        if (Type.getType(RouterService.class)
                .getDescriptor()
                .equals(descriptor)) {

            return new ModuleAnnotationVisitor();
        }

        return super.visitAnnotation(descriptor, visible);
    }

    private class ModuleAnnotationVisitor extends AnnotationVisitor {

        ModuleAnnotationVisitor() {
            super(Opcodes.ASM6);
        }

        @Override public AnnotationVisitor visitArray(String name) {
            System.out.println("ModuleAnnotationVisitor#visitArray" + name);
            AnnotationVisitor av = super.visitArray(name);
            return new ArrayAnnotationVisitor(av, name);
        }

        @Override public void visit(String name, Object value) {
            System.out.println("ModuleAnnotationVisitor#visit: " + name + "," + value);
            super.visit(name, value);
        }
    }

    class ArrayAnnotationVisitor extends AnnotationVisitor {
        final String name;

        private ArrayAnnotationVisitor(AnnotationVisitor av, String name) {
            super(Opcodes.ASM6, av);
            this.name = name;
        }

        @Override public void visit(String name, Object value) {
            super.visit(name, value);
            System.out.println("ArrayAnnotationVisitor#visit: " + name + "," + value);
        }

    }

    /**
     * ASM进入到类的方法时进行回调
     *
     * @param name 方法名
     */
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc,
            final String signature, String[] exceptions) {

        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

        if (isApplicationClass && mRunAlone != null) {
            return new ModuleAdviceAdapter(methodVisitor, access, name, desc);
        }

        return methodVisitor;
    }

    private class ModuleAdviceAdapter extends AdviceAdapter {
        private String methodName;


        ModuleAdviceAdapter(MethodVisitor mv, int access, String name, String desc) {
            super(Opcodes.ASM6, mv, access, name, desc);
            this.methodName = name;
        }


        @Override protected void onMethodEnter() {
        }

        @Override protected void onMethodExit(int opcode) {
            if ("onCreate".equals(methodName)) {
                hasOnCreateMethod = true;

                //for (String s : mRunAlone.getApplication()) {
                //    mv.visitLdcInsn(s);
                //    mv.visitMethodInsn(INVOKESTATIC,
                //                       "com/baiiu/componentservice/Router",
                //                       "registerComponent", "(Ljava/lang/String;)V",
                //                       false);
                //}

            } else {
                hasOnCreateMethod = false;
            }

            System.out.println(
                    "ModuleAdviceAdapter#onMethodEnter: "
                    + methodName
                    + ", "
                    + "hasOncreateMethod:"
                    + hasOnCreateMethod);
        }
    }

    public boolean isAddRouter() {
        return isApplicationClass && !hasOnCreateMethod;
    }
}
