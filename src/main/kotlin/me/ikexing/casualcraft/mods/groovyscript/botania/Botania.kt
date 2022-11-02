package me.ikexing.casualcraft.mods.groovyscript.botania

import com.cleanroommc.groovyscript.compat.mods.ModPropertyContainer

class Botania : ModPropertyContainer() {
    val manaInfusion = ManaInfusion()

    init {
        this.addRegistry(this.manaInfusion)
    }
}