package me.ikexing.casualcraft.core.transform.vanilla;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.*;

public class AsmEntityItem extends ClassVisitor implements Opcodes {

    private final String className;
    private boolean isFieldPotionsPresent;

    public AsmEntityItem(int api, ClassVisitor cv, String className) {
        super(api, cv);
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(className, name, desc);
        if (methodName.equals("writeEntityToNBT") || methodName.equals("func_70014_b"))
            return new AsmEntityItemWriteEntityToNBT(super.visitMethod(access, name, desc, signature, exceptions));
        if (methodName.equals("readEntityFromNBT") || methodName.equals("func_70037_a"))
            return new AsmEntityItemReadEntityFromNBT(super.visitMethod(access, name, desc, signature, exceptions));
        if (methodName.equals("<init>") && desc.equals("(Lnet/minecraft/world/World;DDD)V"))
            return new AsmEntityItemInitVisitor(super.visitMethod(access, name, desc, signature, exceptions));
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (!isFieldPotionsPresent && name.equals("potions"))
            isFieldPotionsPresent = true;
        return super.visitField(access, name, desc, signature, value);
    }


    @Override
    public void visitEnd() {
        if (!isFieldPotionsPresent) {
            FieldVisitor potions = super.visitField(ACC_PUBLIC,
                    "potions",
                    "Ljava/util/Map;",
                    "Ljava/util/Map<Ljava/lang/String;Lorg/apache/commons/lang3/tuple/Pair<Ljava/lang/Integer;Ljava/lang/Integer;>;>;",
                    null);
            if (potions != null) potions.visitEnd();
        }
        super.visitEnd();
    }
}

class AsmEntityItemWriteEntityToNBT extends MethodVisitor implements Opcodes {

    public AsmEntityItemWriteEntityToNBT(MethodVisitor mv) {
        super(ASM5, mv);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == RETURN) {
            this.insertPotions();
        }
        super.visitInsn(opcode);
    }

    private void insertPotions() {
        super.visitVarInsn(ALOAD, 1);
        super.visitLdcInsn("potions");
        super.visitVarInsn(ALOAD, 0);
        super.visitFieldInsn(
                GETFIELD,
                "net/minecraft/entity/item/EntityItem",
                "potions",
                "Ljava/util/Map;");
        super.visitMethodInsn(
                INVOKESTATIC,
                "me/ikexing/casualcraft/core/hook/vanilla/HookEntityItem",
                "handleWriteEntityToNBT",
                "(Ljava/util/Map;)Lnet/minecraft/nbt/NBTTagCompound;",
                false);
        super.visitMethodInsn(
                INVOKEVIRTUAL,
                "net/minecraft/nbt/NBTTagCompound",
                "setTag",
                "(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V",
                false);
    }

}

class AsmEntityItemReadEntityFromNBT extends MethodVisitor implements Opcodes {

    public AsmEntityItemReadEntityFromNBT(MethodVisitor mv) {
        super(ASM5, mv);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == RETURN) {
            this.updatePotions();
        }
        super.visitInsn(opcode);
    }


    private void updatePotions() {
        super.visitVarInsn(ALOAD, 1);
        super.visitLdcInsn("potions");
        super.visitMethodInsn(INVOKEVIRTUAL,
                "net/minecraft/nbt/NBTTagCompound",
                "hasKey",
                "(Ljava/lang/String;)Z",
                false);

        Label elseLabel = new Label();
        super.visitJumpInsn(IFEQ, elseLabel);
        super.visitVarInsn(ALOAD, 0);
        super.visitFieldInsn(GETFIELD,
                "net/minecraft/entity/item/EntityItem",
                "potions",
                "Ljava/util/Map;");
        super.visitVarInsn(ALOAD, 1);
        super.visitLdcInsn("potions");
        super.visitMethodInsn(INVOKEVIRTUAL,
                "net/minecraft/nbt/NBTTagCompound",
                "getCompoundTag",
                "(Ljava/lang/String;)Lnet/minecraft/nbt/NBTTagCompound;",
                false);
        super.visitMethodInsn(
                INVOKESTATIC,
                "me/ikexing/casualcraft/core/hook/vanilla/HookEntityItem",
                "handleReadEntityFromNBT",
                "(Ljava/util/Map;Lnet/minecraft/nbt/NBTTagCompound;)V",
                false);
        super.visitLabel(elseLabel);
    }

}

class AsmEntityItemInitVisitor extends MethodVisitor implements Opcodes {

    public AsmEntityItemInitVisitor(MethodVisitor mv) {
        super(ASM5, mv);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == RETURN) this.initMapPotions();
        super.visitInsn(opcode);
    }

    private void initMapPotions() {
        this.visitVarInsn(ALOAD, 0);
        this.visitTypeInsn(NEW, "java/util/HashMap");
        this.visitInsn(DUP);
        this.visitMethodInsn(INVOKESPECIAL, "java/util/HashMap", "<init>", "()V", false);
        this.visitFieldInsn(PUTFIELD, "net/minecraft/entity/item/EntityItem", "potions", "Ljava/util/Map;");
    }

}
