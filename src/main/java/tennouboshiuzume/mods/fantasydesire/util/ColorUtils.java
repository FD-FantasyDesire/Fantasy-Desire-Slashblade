package tennouboshiuzume.mods.fantasydesire.util;

public class ColorUtils {
    public static int getSmoothTransitionColor(float step, int totalSteps) {
        // 将渐变过程分成三个阶段，计算每个阶段的长度
//        step += (int) (totalSteps/6);
        float phaseLength = (float) totalSteps / 3;

        // 如果 step 大于 totalSteps，将其限制在 totalSteps 范围内
        if (step > totalSteps) {
            step = step%totalSteps;
        }

        int r = 0, g = 0, b = 0;

        // 第一阶段：从红色到绿色
        if (step <= phaseLength) {
            float progress = (float) step / phaseLength;
            r = (int) (255 * (1 - progress)); // 红色从255渐变到0
            g = (int) (255 * progress);       // 绿色从0渐变到255
            b = 0;                            // 蓝色保持0
        }
        // 第二阶段：从绿色到蓝色
        else if (step <= 2 * phaseLength) {
            float progress = (float) (step - phaseLength) / phaseLength;
            r = 0;                            // 红色保持0
            g = (int) (255 * (1 - progress)); // 绿色从255渐变到0
            b = (int) (255 * progress);       // 蓝色从0渐变到255
        }
        // 第三阶段：从蓝色回到红色
        else {
            float progress = (float) (step - 2 * phaseLength) / phaseLength;
            r = (int) (255 * progress);       // 红色从0渐变到255
            g = 0;                            // 绿色保持0
            b = (int) (255 * (1 - progress)); // 蓝色从255渐变到0
        }

        // 限制 r, g, b 的值在 0 到 255 之间，避免溢出
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        // 将 RGB 值合并为一个整数
        return (r << 16) | (g << 8) | b;
    }

    public static int getSmoothTransitionColor(float step, int totalSteps,Boolean isHex) {
        // 将渐变过程分成六个阶段，计算每个阶段的长度
        int phaseLength = totalSteps / 6;

        // 如果 step 大于 totalSteps，将其限制在 totalSteps 范围内
        if (step > totalSteps) {
            step = step % totalSteps;
        }

        int r = 0, g = 0, b = 0;

        // 第一阶段：从红色到黄色 (0xFF0000 → 0xFFFF00)
        if (step <= phaseLength) {
            float progress = (float) step / phaseLength;
            r = 255;                         // 红色保持255
            g = (int) (255 * progress);      // 绿色从0渐变到255
            b = 0;                           // 蓝色保持0
        }
        // 第二阶段：从黄色到绿色 (0xFFFF00 → 0x00FF00)
        else if (step <= 2 * phaseLength) {
            float progress = (float) (step - phaseLength) / phaseLength;
            r = (int) (255 * (1 - progress)); // 红色从255渐变到0
            g = 255;                         // 绿色保持255
            b = 0;                           // 蓝色保持0
        }
        // 第三阶段：从绿色到青色 (0x00FF00 → 0x00FFFF)
        else if (step <= 3 * phaseLength) {
            float progress = (float) (step - 2 * phaseLength) / phaseLength;
            r = 0;                           // 红色保持0
            g = 255;                         // 绿色保持255
            b = (int) (255 * progress);      // 蓝色从0渐变到255
        }
        // 第四阶段：从青色到蓝色 (0x00FFFF → 0x0000FF)
        else if (step <= 4 * phaseLength) {
            float progress = (float) (step - 3 * phaseLength) / phaseLength;
            r = 0;                           // 红色保持0
            g = (int) (255 * (1 - progress)); // 绿色从255渐变到0
            b = 255;                         // 蓝色保持255
        }
        // 第五阶段：从蓝色到紫色 (0x0000FF → 0xFF00FF)
        else if (step <= 5 * phaseLength) {
            float progress = (float) (step - 4 * phaseLength) / phaseLength;
            r = (int) (255 * progress);      // 红色从0渐变到255
            g = 0;                           // 绿色保持0
            b = 255;                         // 蓝色保持255
        }
        // 第六阶段：从紫色到红色 (0xFF00FF → 0xFF0000)
        else {
            float progress = (float) (step - 5 * phaseLength) / phaseLength;
            r = 255;                         // 红色保持255
            g = 0;                           // 绿色保持0
            b = (int) (255 * (1 - progress)); // 蓝色从255渐变到0
        }

        // 限制 r, g, b 的值在 0 到 255 之间，避免溢出
        r = Math.max(0, Math.min(255, r));
        g = Math.max(0, Math.min(255, g));
        b = Math.max(0, Math.min(255, b));

        // 将 RGB 值合并为一个整数
        return (r << 16) | (g << 8) | b;
    }

}
