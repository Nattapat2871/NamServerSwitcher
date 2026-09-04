// SPDX-License-Identifier: MIT
// Author: nattapat2871 (https://nattapat2871.me)
package com.serverswitcher.screen;

/** Shared scroll geometry for rendering, wheel input, and scrollbar dragging. */
final class ServerListScroll {
    private double amount;
    private int maximum;
    private int trackTop;
    private int trackHeight;
    private int thumbHeight;
    private boolean dragging;
    private double grabOffset;

    void configure(int viewHeight, int contentHeight, int trackTop, int trackHeight) {
        this.maximum = Math.max(0, contentHeight - viewHeight);
        this.trackTop = trackTop;
        this.trackHeight = Math.max(0, trackHeight);
        this.thumbHeight = contentHeight <= 0 ? this.trackHeight
                : Math.min(this.trackHeight, Math.max(12,
                        (int) ((long) this.trackHeight * viewHeight / contentHeight)));
        setAmount(amount);
        endDrag();
    }

    double amount() {
        return amount;
    }

    int maximum() {
        return maximum;
    }

    int thumbHeight() {
        return thumbHeight;
    }

    int thumbTop() {
        return trackTop + (maximum == 0 ? 0
                : (int) Math.round((trackHeight - thumbHeight) * amount / maximum));
    }

    boolean dragging() {
        return dragging;
    }

    void scroll(double delta) {
        setAmount(amount + delta);
    }

    boolean beginDrag(double mouseY) {
        if (maximum == 0 || trackHeight <= thumbHeight) {
            return false;
        }
        int top = thumbTop();
        // Clicking the track centers the thumb; grabbing the thumb preserves its position.
        grabOffset = mouseY >= top && mouseY < top + thumbHeight
                ? mouseY - trackTop - (trackHeight - thumbHeight) * amount / maximum
                : thumbHeight / 2.0;
        dragging = true;
        drag(mouseY);
        return true;
    }

    boolean drag(double mouseY) {
        if (!dragging) {
            return false;
        }
        setAmount((mouseY - trackTop - grabOffset) * maximum / (trackHeight - thumbHeight));
        return true;
    }

    void endDrag() {
        dragging = false;
    }

    private void setAmount(double value) {
        amount = Math.clamp(value, 0, maximum);
    }
}
