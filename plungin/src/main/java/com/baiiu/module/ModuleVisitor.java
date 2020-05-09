package com.baiiu.module;

import java.util.Set;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
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
                "isApplicationClass: "
                + superName
                + ", "
                + name
                + ", isApplicationClass:"
                + isApplicationClass);
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
            return new ModuleAdviceAdapter(Opcodes.ASM6, methodVisitor, access, name, desc);
        }

        return methodVisitor;
    }

    private class ModuleAdviceAdapter extends AdviceAdapter {
        private String methodName;


        ModuleAdviceAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
            super(api, mv, access, name, desc);
            this.methodName = name;
        }


        @Override protected void onMethodEnter() {
        }

        @Override protected void onMethodExit(int opcode) {
            if ("onCreate".equals(methodName)) {
                hasOnCreateMethod = true;

                Set<String> applications = mRunAlone.getApplication();
                if (applications == null || applications.size() == 0) {
                    return;
                }

                for (String s : applications) {
                    if (s == null || s.length() == 0) {
                        continue;
                    }

                    mv.visitLdcInsn(s);
                    mv.visitMethodInsn(INVOKESTATIC,
                                       "com/baiiu/componentservice/Router",
                                       "registerComponent", "(Ljava/lang/String;)V",
                                       false);
                }

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
