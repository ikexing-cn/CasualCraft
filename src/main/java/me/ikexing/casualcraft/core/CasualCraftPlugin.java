package me.ikexing.casualcraft.core;

import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.*;

@Name("CasualCraftCore")
@MCVersion(ForgeVersion.mcVersion)
public class CasualCraftPlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"me.ikexing.casualcraft.core.transform.CasualCraftClassTransform"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
