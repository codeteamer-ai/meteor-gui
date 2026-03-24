package com.meteorui.client.gui;

import com.meteorui.client.module.Module;
import com.meteorui.client.module.ModuleManager;
import com.meteorui.client.sound.ModSounds;
import com.meteorui.client.util.AnimationUtil;
import com.meteorui.client.util.ColorPalette;
import com.meteorui.client.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.List;

public class ClickGuiScreen extends Screen {
    private float openProgress = 0.0f;
    private float backgroundAlpha = 0.0f;
    private float titleAlpha = 0.0f;
    private long openTime;
    private boolean closing = false;
    private float closingProgress = 1.0f;
    private int selectedTab = -1;
    private float[] tabHoverAnimations;
    private float contentAlpha = 0.0f;
    private float[] moduleHoverAnimations;
    private float[] moduleToggleAnimations;
    private static final int MODULE_ROW_HEIGHT = 28;
    private static final int MODULE_AREA_TOP = 54;
    private static final int MODULE_AREA_LEFT = 20;
    private static final int MODULE_AREA_RIGHT_PAD = 20;

    public ClickGuiScreen() { super(Text.literal("MeteorGUI")); }

    @Override
    protected void init() {
        super.init();
        openTime = System.currentTimeMillis();
        openProgress = 0.0f;
        closing = false;
        closingProgress = 1.0f;
        contentAlpha = 0.0f;
        tabHoverAnimations = new float[Category.values().length + 1];
        List<Module> allModules = ModuleManager.getInstance().getModules();
        moduleHoverAnimations = new float[allModules.size()];
        moduleToggleAnimations = new float[allModules.size()];
        for (int i = 0; i < allModules.size(); i++)
            moduleToggleAnimations[i] = allModules.get(i).isEnabled() ? 1.0f : 0.0f;
        ModSounds.playOpen();
    }

    private List<Module> getVisibleModules() {
        if (selectedTab == -1) return ModuleManager.getInstance().getModules();
        return ModuleManager.getInstance().getModulesByCategory(Category.values()[selectedTab]);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (closing) {
            closingProgress = AnimationUtil.lerp(closingProgress, 0.0f, 0.15f, delta);
            if (closingProgress < 0.02f) { super.close(); return; }
        } else {
            openProgress = AnimationUtil.lerp(openProgress, 1.0f, 0.1f, delta);
        }
        float progress = closing ? closingProgress : openProgress;
        backgroundAlpha = AnimationUtil.lerp(backgroundAlpha, closing ? 0.0f : 0.65f, 0.1f, delta);
        context.fill(0, 0, width, height, RenderUtil.color(8, 8, 20, (int)(backgroundAlpha * 255)));

        float time = (System.currentTimeMillis() - openTime) / 1000.0f;
        float wave = (float) Math.sin(time * 0.5f) * 0.5f + 0.5f;
        int ga = (int)(20 * progress);
        context.fill(0, 0, width, height / 3, RenderUtil.lerpColor(
                ColorPalette.withAlpha(ColorPalette.ACCENT_PURPLE, ga),
                ColorPalette.withAlpha(ColorPalette.ACCENT_BLUE, ga), wave));

        renderTopBar(context, mouseX, mouseY, delta, progress);
        renderModuleList(context, mouseX, mouseY, delta, progress);

        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        float wp = AnimationUtil.pulse(time, 2.0f);
        String wm = "MeteorGUI \u2022 MC 1.21.11";
        context.drawText(tr, wm, width - tr.getWidth(wm) - 10, height - 14,
                RenderUtil.colorWithAlpha(ColorPalette.TEXT_MUTED, (80 + 30 * wp) * progress / 255.0f), false);
        context.drawText(tr, "RIGHT SHIFT to toggle", 10, height - 14,
                RenderUtil.colorWithAlpha(ColorPalette.TEXT_MUTED, 60 * progress / 255.0f), false);
        super.render(context, mouseX, mouseY, delta);
    }

    private void renderTopBar(DrawContext context, int mouseX, int mouseY, float delta, float progress) {
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        RenderUtil.drawRoundedRect(context, 0, 0, width, 44, 0, RenderUtil.colorWithAlpha(ColorPalette.BG_DARK, progress * 0.85f));
        context.fill(0, 43, width, 44, RenderUtil.colorWithAlpha(ColorPalette.ACCENT_PURPLE, progress * 0.4f));

        titleAlpha = AnimationUtil.lerp(titleAlpha, closing ? 0.0f : 1.0f, 0.08f, delta);
        context.drawText(tr, "METEOR", 12, 8, RenderUtil.colorWithAlpha(ColorPalette.ACCENT_PURPLE, titleAlpha), true);
        context.drawText(tr, "GUI", 12 + tr.getWidth("METEOR") + 4, 8, RenderUtil.colorWithAlpha(ColorPalette.TEXT_SECONDARY, titleAlpha), true);
        context.drawText(tr, "v1.0.0", 12, 22, RenderUtil.colorWithAlpha(ColorPalette.TEXT_MUTED, titleAlpha * 0.7f), false);

        Category[] categories = Category.values();
        int tabStartX = 100, tabWidth = 65, tabHeight = 24, tabY = 10;

        // "All" tab
        boolean hovered = mouseX >= tabStartX && mouseX <= tabStartX + tabWidth && mouseY >= tabY && mouseY <= tabY + tabHeight;
        tabHoverAnimations[0] = AnimationUtil.lerp(tabHoverAnimations[0], hovered ? 1.0f : 0.0f, 0.2f, delta);
        boolean active = selectedTab == -1;
        RenderUtil.drawRoundedRect(context, tabStartX, tabY, tabWidth, tabHeight, 4,
                active ? ColorPalette.withAlpha(ColorPalette.ACCENT_PURPLE, (int)(100 * progress))
                       : ColorPalette.withAlpha(ColorPalette.BG_HOVER, (int)(tabHoverAnimations[0] * 80 * progress)));
        int tw = tr.getWidth("All");
        context.drawText(tr, "All", tabStartX + (tabWidth - tw) / 2, tabY + 8,
                RenderUtil.colorWithAlpha(active ? ColorPalette.TEXT_PRIMARY : ColorPalette.TEXT_SECONDARY, progress), false);

        for (int i = 0; i < categories.length; i++) {
            int tx = tabStartX + (i + 1) * (tabWidth + 4);
            boolean h = mouseX >= tx && mouseX <= tx + tabWidth && mouseY >= tabY && mouseY <= tabY + tabHeight;
            tabHoverAnimations[i + 1] = AnimationUtil.lerp(tabHoverAnimations[i + 1], h ? 1.0f : 0.0f, 0.2f, delta);
            boolean a = selectedTab == i;
            int cc = categories[i].getColor();
            RenderUtil.drawRoundedRect(context, tx, tabY, tabWidth, tabHeight, 4,
                    a ? ColorPalette.withAlpha(cc, (int)(100 * progress))
                      : ColorPalette.withAlpha(ColorPalette.BG_HOVER, (int)(tabHoverAnimations[i + 1] * 80 * progress)));
            if (a) context.fill(tx + 4, tabY + tabHeight - 2, tx + tabWidth - 4, tabY + tabHeight, cc);
            String name = categories[i].getDisplayName();
            int ntw = tr.getWidth(name);
            context.drawText(tr, name, tx + (tabWidth - ntw) / 2, tabY + 8,
                    RenderUtil.colorWithAlpha(a ? ColorPalette.TEXT_PRIMARY : ColorPalette.TEXT_SECONDARY, progress), false);
        }
    }

    private void renderModuleList(DrawContext context, int mouseX, int mouseY, float delta, float progress) {
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        contentAlpha = AnimationUtil.lerp(contentAlpha, closing ? 0.0f : 1.0f, 0.06f, delta);
        float alpha = contentAlpha * progress;
        if (alpha < 0.01f) return;

        List<Module> modules = getVisibleModules();
        List<Module> allModules = ModuleManager.getInstance().getModules();

        if (modules.isEmpty()) {
            String label = selectedTab == -1 ? "All Modules" : Category.values()[selectedTab].getDisplayName();
            int cx = width / 2, cy = height / 2;
            int ac = selectedTab == -1 ? ColorPalette.ACCENT_PURPLE : Category.values()[selectedTab].getColor();
            context.drawText(tr, label, cx - tr.getWidth(label) / 2, cy - 16, RenderUtil.colorWithAlpha(ac, alpha), true);
            String e = "No modules in this category";
            context.drawText(tr, e, cx - tr.getWidth(e) / 2, cy + 2, RenderUtil.colorWithAlpha(ColorPalette.TEXT_MUTED, alpha * 0.6f), false);
            return;
        }

        int maw = width - MODULE_AREA_LEFT - MODULE_AREA_RIGHT_PAD;
        for (int i = 0; i < modules.size(); i++) {
            Module mod = modules.get(i);
            int gi = allModules.indexOf(mod);
            int ry = MODULE_AREA_TOP + i * MODULE_ROW_HEIGHT;
            if (ry + MODULE_ROW_HEIGHT < MODULE_AREA_TOP || ry > height - 20) continue;

            boolean hov = mouseX >= MODULE_AREA_LEFT && mouseX <= MODULE_AREA_LEFT + maw && mouseY >= ry && mouseY <= ry + MODULE_ROW_HEIGHT - 2;
            if (gi < moduleHoverAnimations.length)
                moduleHoverAnimations[gi] = AnimationUtil.lerp(moduleHoverAnimations[gi], hov ? 1.0f : 0.0f, 0.2f, delta);
            float ha = gi < moduleHoverAnimations.length ? moduleHoverAnimations[gi] : 0;
            if (gi < moduleToggleAnimations.length)
                moduleToggleAnimations[gi] = AnimationUtil.lerp(moduleToggleAnimations[gi], mod.isEnabled() ? 1.0f : 0.0f, 0.15f, delta);
            float ta = gi < moduleToggleAnimations.length ? moduleToggleAnimations[gi] : 0;

            int rowBg = mod.isEnabled()
                    ? ColorPalette.withAlpha(mod.getCategory().getColor(), (int)((30 + 20 * ha) * alpha))
                    : ColorPalette.withAlpha(ColorPalette.BG_HOVER, (int)(ha * 60 * alpha));
            RenderUtil.drawRoundedRect(context, MODULE_AREA_LEFT, ry, maw, MODULE_ROW_HEIGHT - 2, 4, rowBg);

            if (ta > 0.01f) {
                int bh = (int)((MODULE_ROW_HEIGHT - 6) * ta);
                int by = ry + (MODULE_ROW_HEIGHT - 2 - bh) / 2;
                RenderUtil.drawRoundedRect(context, MODULE_AREA_LEFT + 2, by, 3, bh, 1,
                        ColorPalette.withAlpha(mod.getCategory().getColor(), (int)(255 * ta * alpha)));
            }

            int nc = mod.isEnabled() ? RenderUtil.colorWithAlpha(ColorPalette.TEXT_PRIMARY, alpha)
                    : RenderUtil.lerpColor(RenderUtil.colorWithAlpha(ColorPalette.TEXT_SECONDARY, alpha),
                                           RenderUtil.colorWithAlpha(ColorPalette.TEXT_PRIMARY, alpha), ha);
            context.drawText(tr, mod.getName(), MODULE_AREA_LEFT + 12, ry + 5, nc, false);
            context.drawText(tr, mod.getDescription(), MODULE_AREA_LEFT + 12, ry + 16,
                    RenderUtil.colorWithAlpha(ColorPalette.TEXT_MUTED, alpha * (0.5f + 0.3f * ha)), false);

            int tgx = MODULE_AREA_LEFT + maw - 36, tgy = ry + (MODULE_ROW_HEIGHT - 14) / 2;
            int tgBg = RenderUtil.lerpColor(RenderUtil.colorWithAlpha(ColorPalette.BG_SECONDARY, alpha),
                    RenderUtil.colorWithAlpha(mod.getCategory().getColor(), alpha * 0.7f), ta);
            RenderUtil.drawRoundedRect(context, tgx, tgy, 24, 12, 6, tgBg);
            int kx = tgx + 2 + (int)((24 - 12) * ta);
            RenderUtil.drawRoundedRect(context, kx, tgy + 2, 8, 8, 4, RenderUtil.colorWithAlpha(ColorPalette.TEXT_PRIMARY, alpha));

            if (ha > 0.01f) RenderUtil.drawRoundedOutline(context, MODULE_AREA_LEFT, ry, maw, MODULE_ROW_HEIGHT - 2, 4,
                    ColorPalette.withAlpha(mod.isEnabled() ? mod.getCategory().getColor() : ColorPalette.BORDER_HOVER, (int)(40 * ha * alpha)));
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean down) {
        double mx = click.x(), my = click.y();
        if (my <= 44) {
            int tabStartX = 100, tabWidth = 65, tabY = 10, tabHeight = 24;
            if (mx >= tabStartX && mx <= tabStartX + tabWidth && my >= tabY && my <= tabY + tabHeight) {
                selectedTab = -1; ModSounds.playTabSwitch(); return true;
            }
            for (int i = 0; i < Category.values().length; i++) {
                int tx = tabStartX + (i + 1) * (tabWidth + 4);
                if (mx >= tx && mx <= tx + tabWidth && my >= tabY && my <= tabY + tabHeight) {
                    selectedTab = i; ModSounds.playTabSwitch(); return true;
                }
            }
        }
        if (my > MODULE_AREA_TOP) {
            List<Module> modules = getVisibleModules();
            int maw = width - MODULE_AREA_LEFT - MODULE_AREA_RIGHT_PAD;
            for (int i = 0; i < modules.size(); i++) {
                int ry = MODULE_AREA_TOP + i * MODULE_ROW_HEIGHT;
                if (mx >= MODULE_AREA_LEFT && mx <= MODULE_AREA_LEFT + maw && my >= ry && my <= ry + MODULE_ROW_HEIGHT - 2) {
                    Module mod = modules.get(i);
                    mod.toggle();
                    if (mod.isEnabled()) ModSounds.playToggleOn(); else ModSounds.playToggleOff();
                    return true;
                }
            }
        }
        return super.mouseClicked(click, down);
    }

    @Override
    public void close() { if (!closing) { closing = true; ModSounds.playClose(); } }

    @Override
    public boolean shouldPause() { return false; }
}
