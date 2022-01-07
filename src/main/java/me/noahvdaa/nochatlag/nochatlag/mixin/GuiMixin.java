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
	public void handleChat(ChatType var1, Component var2, UUID var3, CallbackInfo ci) {
		HttpUtil.DOWNLOAD_EXECUTOR.submit(() -> {
			if (!minecraft.isBlocked(var3)) {
				if (!minecraft.options.hideMatchedNames || !minecraft.isBlocked(((GuiInvokerMixin) this).invokeGuessChatUUID(var2))) {
					Iterator var4 = ((List) chatListeners.get(var1)).iterator();

					while (var4.hasNext()) {
						ChatListener var5 = (ChatListener) var4.next();
						var5.handle(var1, var2, var3);
					}

				}
			}
		});
		ci.cancel();
	}

}
