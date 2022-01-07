package me.noahvdaa.nochatlag.nochatlag.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.ChatListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.HttpUtil;
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

@Mixin(net.minecraft.client.gui.Gui.class)
public class GuiMixin {

	@Final
	@Shadow
	private Minecraft minecraft;

	@Final
	@Shadow
	private Map<ChatType, List<ChatListener>> chatListeners;

	@Inject(
			method = "handleChat(Lnet/minecraft/network/chat/ChatType;Lnet/minecraft/network/chat/Component;Ljava/util/UUID;)V",
			at = @At("HEAD"),
			cancellable = true
	)
	public void handleChat(ChatType chatType, Component chatComponent, UUID senderUUID, CallbackInfo ci) {
		HttpUtil.DOWNLOAD_EXECUTOR.submit(() -> {
			if (!minecraft.isBlocked(senderUUID)) {
				if (!minecraft.options.hideMatchedNames || !minecraft.isBlocked(((GuiInvokerMixin) this).invokeGuessChatUUID(chatComponent))) {
					Iterator chatListenerIterator = ((List) chatListeners.get(chatType)).iterator();

					while (chatListenerIterator.hasNext()) {
						ChatListener listener = (ChatListener) chatListenerIterator.next();
						listener.handle(chatType, chatComponent, senderUUID);
					}
				}
			}
		});
		ci.cancel();
	}

}
