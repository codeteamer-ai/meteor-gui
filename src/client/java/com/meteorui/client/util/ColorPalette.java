package com.meteorui.client.util;

public class ColorPalette {
    public static final int BG_DARK = 0xFF0D0D1A;
    public static final int BG_SECONDARY = 0xFF1A1A2E;
    public static final int BG_HOVER = 0xFF252540;
    public static final int BORDER_HOVER = 0xFF3A3A5C;
    public static final int ACCENT_PURPLE = 0xFF8B5CF6;
    public static final int ACCENT_BLUE = 0xFF6366F1;
    public static final int TEXT_PRIMARY = 0xFFFFFFFF;
    public static final int TEXT_SECONDARY = 0xFFB0B0C8;
    public static final int TEXT_MUTED = 0xFF6B6B8A;
    public static final int CAT_COMBAT = 0xFFEF4444;
    public static final int CAT_MOVEMENT = 0xFF3B82F6;
    public static final int CAT_RENDER = 0xFFA855F7;
    public static final int CAT_PLAYER = 0xFF22C55E;
    public static final int CAT_WORLD = 0xFFF59E0B;
    public static final int CAT_MISC = 0xFF6B7280;

    public static int withAlpha(int color, int alpha) {
        return (Math.max(0, Math.min(255, alpha)) << 24) | (color & 0x00FFFFFF);
    }
}
