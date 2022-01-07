package me.noahvdaa.nochatlag.nochatlag.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.ClientChatListener;
import net.minecraft.client.util.NetworkUtils;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(net.minecraft.client.gui.hud.InGameHud.class)
public abstract class GuiMixin {

	@Final
	@Shadow
	private MinecraftClient client;

	@Final
	@Shadow
	private Map<MessageType, List<ClientChatListener>> listeners;

	@Shadow
	public abstract UUID extractSender(Text message);

	@Inject(
			method = "addChatMessage(Lnet/minecraft/network/MessageType;Lnet/minecraft/text/Text;Ljava/util/UUID;)V",
			at = @At("HEAD"),
			cancellable = true
	)
	public void handleChat(MessageType chatType, Text chatComponent, UUID senderUUID, CallbackInfo ci) {
		NetworkUtils.EXECUTOR.submit(() -> {
			if (this.client.shouldBlockMessages(senderUUID) || (this.client.options.hideMatchedNames && this.client.shouldBlockMessages(this.extractSender(chatComponent)))) {
				return;
			}
			for (ClientChatListener chatListener : this.listeners.get(chatType)) {
				chatListener.onChatMessage(chatType, chatComponent, senderUUID);
			}
		});
		ci.cancel();
	}

}
