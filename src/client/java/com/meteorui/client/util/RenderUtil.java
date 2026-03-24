package com.meteorui.client.util;

import net.minecraft.client.gui.DrawContext;

public class RenderUtil {
    public static int color(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int colorWithAlpha(int baseColor, float alpha) {
        int a = Math.max(0, Math.min(255, (int)(alpha * 255)));
        return (a << 24) | (baseColor & 0x00FFFFFF);
    }

    public static int lerpColor(int c1, int c2, float t) {
        int a1 = (c1 >> 24) & 0xFF, r1 = (c1 >> 16) & 0xFF, g1 = (c1 >> 8) & 0xFF, b1 = c1 & 0xFF;
        int a2 = (c2 >> 24) & 0xFF, r2 = (c2 >> 16) & 0xFF, g2 = (c2 >> 8) & 0xFF, b2 = c2 & 0xFF;
        int a = (int)(a1 + (a2 - a1) * t);
        int r = (int)(r1 + (r2 - r1) * t);
        int g = (int)(g1 + (g2 - g1) * t);
        int b = (int)(b1 + (b2 - b1) * t);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static void drawRoundedRect(DrawContext ctx, int x, int y, int w, int h, int radius, int color) {
        int a = (color >> 24) & 0xFF;
        if (a < 2) return;
        ctx.fill(x, y, x + w, y + h, color);
    }

    public static void drawRoundedOutline(DrawContext ctx, int x, int y, int w, int h, int radius, int color) {
        int a = (color >> 24) & 0xFF;
        if (a < 2) return;
        ctx.fill(x, y, x + w, y + 1, color);
        ctx.fill(x, y + h - 1, x + w, y + h, color);
        ctx.fill(x, y, x + 1, y + h, color);
        ctx.fill(x + w - 1, y, x + w, y + h, color);
    }
}
