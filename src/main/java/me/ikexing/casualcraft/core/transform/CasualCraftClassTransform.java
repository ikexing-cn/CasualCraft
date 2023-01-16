package me.ikexing.casualcraft.core.transform;

import me.ikexing.casualcraft.core.transform.vanilla.ASMEntityAreaEffectCloud;
import me.ikexing.casualcraft.core.transform.vanilla.AsmEntityItem;
import me.ikexing.casualcraft.core.utils.CoreUtils;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.IOException;

public class CasualCraftClassTransform implements IClassTransformer, Opcodes {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("net.minecraft.entity.EntityAreaEffectCloud")) {
            ClassWriter classWriter = getClassWriter();
            ASMEntityAreaEffectCloud asm = new ASMEntityAreaEffectCloud(ASM5, classWriter, transformedName);
            return handleVanillaAsm(asm, classWriter, basicClass, transformedName);
        }
        if (transformedName.equals("net.minecraft.entity.item.EntityItem")) {
            ClassWriter classWriter = getClassWriter();
            AsmEntityItem asm = new AsmEntityItem(ASM5, classWriter, transformedName);
            return handleVanillaAsm(asm, classWriter, basicClass, transformedName);
        }
        return basicClass;
    }

    public ClassWriter getClassWriter() {
        return new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    }

    public byte[] handleVanillaAsm(ClassVisitor asm, ClassWriter classWriter, byte[] basicClass, String transformedName) {
        CoreUtils.getLogger().info("[CasualCraft]: transforming class {}", transformedName);
        try {
            CoreUtils.getLogger().info("on vanilla {} class visitor", transformedName);
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(asm, 0);

            byte[] result = classWriter.toByteArray();
            String[] splitNames = transformedName.split("\\.");
            FileUtils.writeByteArrayToFile(new File(".asm_out_classes" + File.separator + splitNames[splitNames.length - 1] + ".class"), result);
            return result;
        } catch (IOException e) {
            CoreUtils.getLogger().error("Error loading vanilla " + transformedName + " ASM", e);
        }
        return basicClass;
    }

}
