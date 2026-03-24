package com.meteorui.client.util;

public class AnimationUtil {
    public static float lerp(float current, float target, float speed, float delta) {
        float diff = target - current;
        float step = diff * speed * Math.min(delta * 3.0f, 1.0f);
        if (Math.abs(diff) < 0.001f) return target;
        return current + step;
    }

    public static float pulse(float time, float speed) {
        return (float)(Math.sin(time * speed) * 0.5 + 0.5);
    }

    public static float easeOutCubic(float t) {
        return 1.0f - (1.0f - t) * (1.0f - t) * (1.0f - t);
    }
}
