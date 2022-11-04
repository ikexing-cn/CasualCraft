package me.ikexing.casualcraft.client

import crafttweaker.mc1120.commands.CTChatCommand
import me.ikexing.casualcraft.Main
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent
import net.minecraftforge.client.settings.KeyConflictContext
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard

object KeyBinding {

    private val KEY_CT_HAND: KeyBinding =
        KeyBinding("key.${Main.MOD_ID}.ct_hand", KeyConflictContext.IN_GAME, Keyboard.KEY_K, "key.category.${Main.MOD_ID}")

    fun registerKeyBinding() {
        if (Loader.isModLoaded("crafttweaker")) {
            ClientRegistry.registerKeyBinding(KEY_CT_HAND)
            MinecraftForge.EVENT_BUS.register(CompatCraftTweakerEvent())
        }
    }


    class CompatCraftTweakerEvent {

        @SubscribeEvent
        fun onKeyPressed(event: KeyboardInputEvent) {
            if (KEY_CT_HAND.isKeyDown) {
                print(123)
                val craftTweakerCommands = CTChatCommand.getCraftTweakerCommands()
                val ctHandCommand = craftTweakerCommands["hand"] ?: return
                ctHandCommand.executeCommand(Minecraft.getMinecraft().integratedServer, Minecraft.getMinecraft().player, arrayOf())
            }  else {
                print(456)
            }
        }

    }

}