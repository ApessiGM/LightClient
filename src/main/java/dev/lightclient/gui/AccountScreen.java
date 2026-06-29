package dev.lightclient.gui;

import dev.lightclient.LightClient;
import dev.lightclient.Reference;
import dev.lightclient.account.OfflineAccount;
import dev.lightclient.manager.AccountManager;
import dev.lightclient.util.ColorUtil;
import dev.lightclient.util.animation.Animation;
import dev.lightclient.util.animation.Easing;
import dev.lightclient.util.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.List;

/** In-game offline account manager: add, switch and delete accounts live. */
public final class AccountScreen extends Screen {
    private static final int PANEL_WIDTH = 300;
    private static final int ROW_HEIGHT = 24;

    private final Animation fade = new Animation(260, Easing.EASE_OUT_EXPO);
    private TextFieldWidget nameField;
    private double scroll;

    private int panelX;
    private int panelY;

    public AccountScreen() {
        super(Text.literal("Accounts"));
    }

    @Override
    protected void init() {
        fade.animateTo(1.0);
        panelX = (width - PANEL_WIDTH) / 2;
        panelY = 44;
        nameField = new TextFieldWidget(textRenderer, panelX + 14, panelY + 44, PANEL_WIDTH - 90, 18,
                Text.literal("Username"));
        nameField.setMaxLength(16);
        nameField.setPlaceholder(Text.literal("Enter username..."));
        addSelectableChild(nameField);
        setInitialFocus(nameField);
    }

    private AccountManager accounts() {
        return LightClient.getInstance().getAccountManager();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        float alpha = fade.getFloat();
        context.fill(0, 0, width, height, ColorUtil.withAlpha(Reference.COLOR_DARK_SECONDARY, (int) (150 * alpha)));

        int accent = LightClient.getInstance().getThemeManager().accent();
        int accentEnd = LightClient.getInstance().getThemeManager().accentEnd();
        int listTop = panelY + 74;
        int listBottom = height - 30;
        int panelHeight = listBottom - panelY + 14;

        RenderUtil.glow(context, panelX, panelY, PANEL_WIDTH, panelHeight, accent, 6);
        RenderUtil.roundedRect(context, panelX, panelY, PANEL_WIDTH, panelHeight, 6,
                ColorUtil.withAlpha(Reference.COLOR_DARK_PRIMARY, 235));
        RenderUtil.gradientH(context, panelX, panelY, PANEL_WIDTH, 26, accent, accentEnd);
        RenderUtil.text(context, "§lAccount§r Switcher", panelX + 12, panelY + 9, 0xFF121212);
        RenderUtil.text(context, "§7Current: §f" + accounts().currentUsername(), panelX + 12, panelY + 30, 0xFFB9B9C0);

        // Add button.
        int addX = panelX + PANEL_WIDTH - 70;
        int addY = panelY + 44;
        boolean addHover = hovered(mouseX, mouseY, addX, addY, 56, 18);
        RenderUtil.roundedRect(context, addX, addY, 56, 18, 3, addHover ? accent : ColorUtil.withAlpha(accent, 150));
        RenderUtil.textCentered(context, "Add", addX + 28, addY + 5, 0xFF121212);

        nameField.render(context, mouseX, mouseY, delta);

        // Account list.
        List<OfflineAccount> list = accounts().getAccounts();
        context.enableScissor(panelX, listTop, panelX + PANEL_WIDTH, listBottom);
        int y = (int) (listTop - scroll);
        String current = accounts().currentUsername();
        for (OfflineAccount account : list) {
            renderRow(context, account, mouseX, mouseY, y, current, accent);
            y += ROW_HEIGHT + 4;
        }
        if (list.isEmpty()) {
            RenderUtil.text(context, "§7No accounts yet. Add one above.", panelX + 14, listTop + 6, 0xFF8A8A92);
        }
        context.disableScissor();

        RenderUtil.text(context, "§7Esc to close", panelX + 12, height - 24, 0xFFB9B9C0);
    }

    private void renderRow(DrawContext context, OfflineAccount account, int mouseX, int mouseY, int y,
                           String current, int accent) {
        int rowX = panelX + 12;
        int rowW = PANEL_WIDTH - 24;
        boolean active = account.getUsername().equalsIgnoreCase(current);
        RenderUtil.roundedRect(context, rowX, y, rowW, ROW_HEIGHT, 3, ColorUtil.withAlpha(0x1A1A22, 235));
        if (active) {
            RenderUtil.rect(context, rowX, y, 3, ROW_HEIGHT, accent);
        }
        RenderUtil.text(context, account.getUsername(), rowX + 10, y + 5, active ? accent : 0xFFFFFFFF);
        RenderUtil.text(context, active ? "§aactive" : "§7offline", rowX + 10, y + 14, 0xFF8A8A92);

        int loginX = rowX + rowW - 96;
        int delX = rowX + rowW - 40;
        boolean loginHover = hovered(mouseX, mouseY, loginX, y + 4, 50, 16);
        boolean delHover = hovered(mouseX, mouseY, delX, y + 4, 34, 16);
        RenderUtil.roundedRect(context, loginX, y + 4, 50, 16, 3, loginHover ? accent : ColorUtil.withAlpha(accent, 140));
        RenderUtil.textCentered(context, "Login", loginX + 25, y + 8, 0xFF121212);
        RenderUtil.roundedRect(context, delX, y + 4, 34, 16, 3, delHover ? 0xFFFF453A : 0xFF552028);
        RenderUtil.textCentered(context, "Del", delX + 17, y + 8, 0xFFFFFFFF);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        double mx = click.x();
        double my = click.y();

        int addX = panelX + PANEL_WIDTH - 70;
        int addY = panelY + 44;
        if (hovered(mx, my, addX, addY, 56, 18)) {
            tryAdd();
            return true;
        }

        int listTop = panelY + 74;
        int y = (int) (listTop - scroll);
        int rowX = panelX + 12;
        int rowW = PANEL_WIDTH - 24;
        for (OfflineAccount account : List.copyOf(accounts().getAccounts())) {
            int loginX = rowX + rowW - 96;
            int delX = rowX + rowW - 40;
            if (hovered(mx, my, loginX, y + 4, 50, 16)) {
                accounts().login(account);
                return true;
            }
            if (hovered(mx, my, delX, y + 4, 34, 16)) {
                accounts().remove(account);
                return true;
            }
            y += ROW_HEIGHT + 4;
        }
        return super.mouseClicked(click, doubled);
    }

    private void tryAdd() {
        if (accounts().add(nameField.getText())) {
            nameField.setText("");
        } else {
            LightClient.getInstance().getNotificationManager()
                    .error("Account", "Invalid or duplicate username");
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int contentHeight = accounts().getAccounts().size() * (ROW_HEIGHT + 4);
        int viewHeight = (height - 30) - (panelY + 74);
        double max = Math.max(0, contentHeight - viewHeight);
        scroll = Math.max(0, Math.min(max, scroll - verticalAmount * 16));
        return true;
    }

    private boolean hovered(double mx, double my, double x, double y, double w, double h) {
        return mx >= x && mx <= x + w && my >= y && my <= y + h;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
