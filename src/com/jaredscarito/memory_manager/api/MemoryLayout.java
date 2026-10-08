package com.jaredscarito.memory_manager.api;

/**
 * Shared size of the memory map. Placement math and the drawing code both use this height,
 * so a block's pixel position stays in proportion to its kilobytes.
 */
public final class MemoryLayout {
    public static final double VIEW_HEIGHT = 540;
    public static final double MIN_BLOCK_PX = 40;
    public static final double BLOCK_WIDTH = 148;
    public static final double SCALE_WIDTH = 92;

    private MemoryLayout() {
    }

    public static int minimumVisibleKb(double totalKb) {
        if (totalKb <= 0) {
            return 1;
        }
        return (int) Math.ceil((MIN_BLOCK_PX * totalKb) / VIEW_HEIGHT);
    }

    public static double pixelsFor(double sizeKb, double totalKb) {
        if (totalKb <= 0) {
            return 0;
        }
        return (VIEW_HEIGHT / totalKb) * sizeKb;
    }

    public static double kbForPixels(double pixels, double totalKb) {
        if (VIEW_HEIGHT <= 0) {
            return 0;
        }
        return (pixels / VIEW_HEIGHT) * totalKb;
    }
}
