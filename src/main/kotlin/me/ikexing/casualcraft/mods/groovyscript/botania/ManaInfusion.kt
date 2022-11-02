package me.ikexing.casualcraft.mods.groovyscript.botania

import com.cleanroommc.groovyscript.api.GroovyBlacklist
import com.cleanroommc.groovyscript.api.GroovyLog
import com.cleanroommc.groovyscript.helper.recipe.AbstractRecipeBuilder
import com.cleanroommc.groovyscript.registry.VirtualizedRegistry
import me.ikexing.casualcraft.Main
import me.ikexing.casualcraft.recipes.botania.ManaInfusionRecipe
import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import vazkii.botania.api.recipe.RecipeManaInfusion

class ManaInfusion :
    VirtualizedRegistry<ManaInfusionRecipe>("ManaInfusion", listOf("manainfusion", "mana_infusion").toString()) {

    fun recipeBuilder() = RecipeBuilder()

    @GroovyBlacklist
    override fun onReload() {
        // TODO: hot reload code
    }

    fun add(
        output: ItemStack,
        input: Any,
        mana: Int,
        catalystBlock: IBlockState?
    ) = ManaInfusionRecipe.addRecipe(output, input, mana, catalystBlock)

    class RecipeBuilder : AbstractRecipeBuilder<RecipeManaInfusion>() {
        private var mana: Int = 0
        private var catalystBlock: IBlockState? = null

        fun mana(mana: Int): RecipeBuilder {
            this.mana = mana
            return this
        }

        fun catalystBlock(catalystBlock: IBlockState): RecipeBuilder {
            this.catalystBlock = catalystBlock
            return this
        }

        override fun validate(msg: GroovyLog.Msg?) {
            this.validateItems(msg, 1, 1, 1, 1)
            this.validateFluids(msg)
        }

        override fun register(): RecipeManaInfusion {
            return Main.BOTANIA.get().manaInfusion.add(this.output[0], this.input[0], this.mana, this.catalystBlock)
        }

        override fun getErrorMsg(): String {
            return "Error adding Botania Mana Infusion recipe"
        }
    }
}
