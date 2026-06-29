package dev.lightclient.mixin;

import dev.lightclient.LightClient;
import dev.lightclient.Reference;
import dev.lightclient.util.ColorUtil;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Adds Light Client branding to the vanilla title screen. */
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin() {
        super(null);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void lightclient$renderBranding(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        int accent = LightClient.getInstance() != null
                ? LightClient.getInstance().getThemeManager().accent()
                : ColorUtil.opaque(Reference.COLOR_PURPLE_BRIGHT);

        int boxWidth = 132;
        int boxHeight = 26;
        int x = 6;
        int y = this.height - boxHeight - 6;

        RenderUtil.glow(context, x, y, boxWidth, boxHeight, accent, 4);
        RenderUtil.roundedRect(context, x, y, boxWidth, boxHeight, 4, ColorUtil.withAlpha(Reference.COLOR_DARK_PRIMARY, 220));
        RenderUtil.rect(context, x, y, 3, boxHeight, accent);
        RenderUtil.text(context, "§lLight§r Client", x + 10, y + 5, 0xFFFFFFFF);
        RenderUtil.text(context, "§7v" + Reference.VERSION + " • MC " + Reference.GAME_VERSION, x + 10, y + 15, 0xFFB9B9C0);
    }
}
