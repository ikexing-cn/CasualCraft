package me.ikexing.casualcraft

import crafttweaker.CraftTweakerAPI
import crafttweaker.IAction
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent

@Mod(
    modid = Main.MOD_ID,
    name = Main.MOD_NAME,
    version = Main.MOD_VERSION,
    modLanguageAdapter = Main.LANG_ADAPTER,
    dependencies = Main.DEPENDENCIES
)
object Main {
    const val MOD_ID = "casualcraft"
    const val MOD_NAME = "CasualCraft"
    const val MOD_VERSION = "1.0.0"

    const val LANG_ADAPTER = "net.shadowfacts.forgelin.KotlinAdapter"
    const val DEPENDENCIES = "after:crafttweaker;required-after:forgelin;"

    val LATE_ADD_ACTIONS = mutableListOf<IAction>()
    val LATE_REMOVE_ACTIONS = mutableListOf<IAction>()

    @EventHandler
    fun loadComplete(event: FMLPostInitializationEvent) {
        // remove should be first and then add
        LATE_REMOVE_ACTIONS.forEach(CraftTweakerAPI::apply)
        LATE_ADD_ACTIONS.forEach(CraftTweakerAPI::apply)
    }

}