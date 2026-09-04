// SPDX-License-Identifier: MIT
// Author: nattapat2871 (https://nattapat2871.me)
package com.serverswitcher.screen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerListScrollTest {
    private ServerListScroll list() {
        var scroll = new ServerListScroll();
        scroll.configure(120, 440, 10, 116);
        return scroll;
    }

    @Test
    void wheelClampsAtBothEndsAndKeepsFractionalInput() {
        var scroll = list();
        scroll.scroll(5.5);
        assertEquals(5.5, scroll.amount());
        scroll.scroll(1000);
        assertEquals(320, scroll.amount());
        scroll.scroll(-1000);
        assertEquals(0, scroll.amount());
    }

    @Test
    void grabbingThumbDoesNotJumpAndDraggingReachesBothEnds() {
        var scroll = list();
        assertTrue(scroll.beginDrag(17));
        assertEquals(0, scroll.amount());
        assertTrue(scroll.drag(126 - scroll.thumbHeight() + 7));
        assertEquals(320, scroll.amount());
        scroll.drag(-100);
        assertEquals(0, scroll.amount());
    }

    @Test
    void grabbingRoundedThumbKeepsTheExactContentPosition() {
        var scroll = new ServerListScroll();
        scroll.configure(120, 22000, 10, 116);
        scroll.scroll(10999.5);
        scroll.beginDrag(scroll.thumbTop() + 3);
        assertEquals(10999.5, scroll.amount(), 0.00001);
    }

    @Test
    void trackClickCentersThumbWithoutSelectingARow() {
        var scroll = list();
        assertTrue(scroll.beginDrag(80));
        assertEquals(80, scroll.thumbTop() + scroll.thumbHeight() / 2.0, 0.5);
        assertTrue(scroll.amount() > 0);
        assertTrue(scroll.amount() < scroll.maximum());
    }

    @Test
    void draggingBeyondTrackClampsAndReleaseStopsMovement() {
        var scroll = list();
        scroll.beginDrag(15);
        scroll.drag(2000);
        assertEquals(scroll.maximum(), scroll.amount());
        scroll.endDrag();
        assertFalse(scroll.drag(0));
        assertEquals(scroll.maximum(), scroll.amount());
    }

    @Test
    void emptyAndShortListsNeverScrollOrDrag() {
        var scroll = list();
        for (int contentHeight : new int[] {0, 22, 120}) {
            scroll.configure(120, contentHeight, 10, 116);
            scroll.scroll(500);
            assertEquals(0, scroll.amount());
            assertFalse(scroll.beginDrag(30));
            assertEquals(116, scroll.thumbHeight());
        }
    }

    @Test
    void resizedListPreservesPositionThenClampsAndCancelsDrag() {
        var scroll = list();
        scroll.scroll(220);
        scroll.configure(160, 440, 10, 156);
        assertEquals(220, scroll.amount());
        scroll.beginDrag(scroll.thumbTop() + 3);
        scroll.configure(400, 440, 10, 396);
        assertEquals(40, scroll.amount());
        assertFalse(scroll.dragging());
    }

    @Test
    void tinyTracksNeverDivideByZero() {
        var scroll = list();
        for (int trackHeight : new int[] {0, 5, 12}) {
            scroll.configure(16, 440, 10, trackHeight);
            assertFalse(scroll.beginDrag(12));
            assertFalse(scroll.drag(50));
            assertEquals(0, scroll.amount());
        }
    }

    @Test
    void manyServersKeepMinimumThumbSizeAndLastRowReachable() {
        var scroll = new ServerListScroll();
        scroll.configure(120, 22000, 10, 116);
        assertEquals(12, scroll.thumbHeight());
        scroll.beginDrag(15);
        scroll.drag(10000);
        assertEquals(21880, scroll.amount());
        assertEquals(126, scroll.thumbTop() + scroll.thumbHeight());
    }
}
