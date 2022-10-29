package me.ikexing.casualcraft.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.common.block.tile.mana.TilePool;

@Pseudo
@Mixin(value = TilePool.class, remap = false)
public class MixinTilePool {

    @Inject(method = "getMatchingRecipe", at = {@At("HEAD")})
    private static void getMatchingRecipe(ItemStack stack, IBlockState state, CallbackInfoReturnable<RecipeManaInfusion> cir) {

    }
}