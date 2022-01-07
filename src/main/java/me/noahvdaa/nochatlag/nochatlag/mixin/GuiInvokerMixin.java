package me.noahvdaa.nochatlag.nochatlag.mixin;

import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.UUID;

@Mixin(net.minecraft.client.gui.Gui.class)
public interface GuiInvokerMixin {

	@Invoker("guessChatUUID")
	UUID invokeGuessChatUUID(Component var1);

}
