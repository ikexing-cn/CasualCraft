package me.ikexing.casualcraft

import net.minecraftforge.fml.common.Mod

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

}