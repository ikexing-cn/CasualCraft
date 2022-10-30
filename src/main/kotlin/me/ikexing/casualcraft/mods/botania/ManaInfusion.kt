package me.ikexing.casualcraft.mods.botania

import crafttweaker.annotations.ModOnly
import crafttweaker.annotations.ZenRegister
import crafttweaker.api.block.IBlockState
import crafttweaker.api.item.IIngredient
import crafttweaker.api.item.IItemStack
import crafttweaker.api.minecraft.CraftTweakerMC
import me.ikexing.casualcraft.utils.*
import stanhebben.zenscript.annotations.Optional
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.recipe.RecipeManaInfusion

@ZenRegister
@ModOnly("botania")
@ZenClass("mods.casualcraft.botania.ManaInfusion")
object ManaInfusion {

    @JvmStatic
    @ZenMethod
    fun addInfusion(output: IItemStack, input: IIngredient, mana: Int, @Optional catalystState: IBlockState?) {
        val recipe = BotaniaAPI.registerManaInfusionRecipe(output.original(), input.toObject(), mana)
        catalystState?.let {
            recipe.catalyst = it.original()
        }
    }

    @JvmStatic
    @ZenMethod
    fun addInfusion(output: IItemStack, input: IIngredient, mana: Int, @Optional catalystState: IItemStack?) {
        val recipe = BotaniaAPI.registerManaInfusionRecipe(output.original(), input.toObject(), mana)
        catalystState?.let {
            recipe.catalyst = catalystState.toBlockState()
        }
    }

    @JvmStatic
    @ZenMethod
    fun addAlchemy(output: IItemStack, input: IIngredient, mana: Int) {
        this.addInfusion(output, input, mana, CraftTweakerMC.getBlockState(RecipeManaInfusion.alchemyState))
    }

    @JvmStatic
    @ZenMethod
    fun addConjuration(output: IItemStack, input: IIngredient, mana: Int) {
        this.addInfusion(output, input, mana, CraftTweakerMC.getBlockState(RecipeManaInfusion.conjurationState))
    }

}


