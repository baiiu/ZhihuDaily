package com.baiiu.module;

import com.baiiu.annotations.Module;
import java.util.ArrayList;
import java.util.List;
import org.checkerframework.checker.regex.RegexUtil;
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

    private String className;
    private String superName;
    private boolean isApplicationClass = false;

    private List<String> mApplicationAnnotation;
    private List<String> mDependenciesAnnotation;

    public ModuleVisitor(String className, ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor);
    }

    /**
     * 当ASM进入类时回调
     */
    @Override
    public void visit(int version, int access, String name, String signature, String superName,
            String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.superName = superName;
    }


    @Override public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        isApplicationClass = superName.contains("Application") &&
                Type.getType(Module.class)
                        .getDescriptor()
                        .equals(descriptor);

        if (isApplicationClass) {
            return new ModuleAnnotationVisitor();
        } else {
            return super.visitAnnotation(descriptor, visible);
        }

    }

    /**
     * ASM进入到类的方法时进行回调
     *
     * @param name 方法名
     */
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc,
            final String signature,
            String[] exceptions) {

        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);

        if (isApplicationClass) {
            return new ModuleAdviceAdapter(Opcodes.ASM6, methodVisitor, access, name, desc);
        }

        return methodVisitor;
    }

    private class ModuleAnnotationVisitor extends AnnotationVisitor {

        ModuleAnnotationVisitor() {
            super(Opcodes.ASM6);
        }

        @Override public AnnotationVisitor visitArray(String name) {
            AnnotationVisitor av = super.visitArray(name);
            return new ArrayAnnotationVisitor(av, name);
        }

        class ArrayAnnotationVisitor extends AnnotationVisitor {

            final String key;

            private ArrayAnnotationVisitor(AnnotationVisitor av, String name) {
                super(Opcodes.ASM5, av);
                this.key = name;
            }

            @Override
            public void visit(String name, Object value) {
                super.visit(name, value);
                //String propValue = String.valueOf(value);
                //String className = propValue.substring(0, propValue.lastIndexOf("."));
                //String methodName = propValue.substring(propValue.lastIndexOf(".") + 1, propValue.indexOf("("));
                //String methodDesc = propValue.substring(propValue.indexOf("("), propValue.length());

                if ("application".equals(key)) {
                    if (mApplicationAnnotation == null) {
                        mApplicationAnnotation = new ArrayList<>();
                    }

                    mApplicationAnnotation.add((String) value);
                } else if ("dependencies".equals(key)) {
                    if (mDependenciesAnnotation == null) {
                        mDependenciesAnnotation = new ArrayList<>();
                    }

                    mDependenciesAnnotation.add((String) value);
                }

                System.out.println("ArrayAnnotationVisitor: " + key + ", " + value);
            }
        }

    }

    private class ModuleAdviceAdapter extends AdviceAdapter {
        private String methodName;


        ModuleAdviceAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
            this.methodName = name;
        }


        @Override protected void onMethodEnter() {
            if ("onCreate".equals(methodName) && mApplicationAnnotation != null) {

                for (String s : mApplicationAnnotation) {

                    mv.visitLdcInsn(s);
                    mv.visitMethodInsn(INVOKESTATIC,
                                       "com/baiiu/componentservice/Router",
                                       "registerComponent", "(Ljava/lang/String;)V",
                                       false);
                }
            }
        }

        @Override protected void onMethodExit(int opcode) {
        }
    }

}
