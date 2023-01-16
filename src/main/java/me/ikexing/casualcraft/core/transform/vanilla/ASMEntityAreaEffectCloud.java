package me.ikexing.casualcraft.core.transform.vanilla;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMEntityAreaEffectCloud extends ClassVisitor implements Opcodes {

    private final String className;

    public ASMEntityAreaEffectCloud(int api, ClassWriter cv, String className) {
        super(api, cv);
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(className, name, desc);
        if (methodName.equals("onUpdate") || methodName.equals("func_70071_h_")) {
            return new ASMEntityAreaEffectCloudOnUpdateVisitor(ASM5, super.visitMethod(access, name, desc, signature, exceptions));
        }
        return super.visitMethod(access, name, desc, signature, exceptions);
    }
}

class ASMEntityAreaEffectCloudOnUpdateVisitor extends MethodVisitor implements Opcodes {

    private boolean triggered = false;

    public ASMEntityAreaEffectCloudOnUpdateVisitor(int api, MethodVisitor mv) {
        super(api, mv);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);
        if (name.equals("getEntitiesWithinAABB")) {
            triggered = true;
        }
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        if (opcode == ALOAD && var == 6 && triggered) {
            super.visitVarInsn(ALOAD, 0);
            super.visitVarInsn(ALOAD, 5);
            super.visitMethodInsn(INVOKESTATIC,
                    "me/ikexing/casualcraft/core/hook/vanilla/HookEntityAreaEffectCloud",
                    "handleOnUpdate",
                    "(Lnet/minecraft/entity/EntityAreaEffectCloud;Ljava/util/List;)V",
                    false);
        }
        super.visitVarInsn(opcode, var);
    }
}