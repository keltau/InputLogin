package com.inputstick.apps.inputlogin;

import android.content.Context;

import com.inputstick.api.broadcast.InputStickBroadcast;
import com.inputstick.api.hid.HIDKeycodes;

final class InputStickBridge {

    private static final String GERMAN_LAYOUT = "de-DE";

    private final Context context;

    InputStickBridge(Context context) {
        this.context = context.getApplicationContext();
    }

    void connect() {
        InputStickBroadcast.requestConnection(context);
    }

    void disconnect() {
        InputStickBroadcast.releaseConnection(context);
    }

    void sendText(String text, boolean handleGermanDeadKeys) {
        String textToSend = handleGermanDeadKeys ? addGermanDeadKeyTerminators(text) : text;
        InputStickBroadcast.type(context, textToSend, GERMAN_LAYOUT);
    }

    void pressTab() {
        InputStickBroadcast.pressAndRelease(context, HIDKeycodes.NONE, HIDKeycodes.KEY_TAB);
    }

    void pressEnter() {
        InputStickBroadcast.pressAndRelease(context, HIDKeycodes.NONE, HIDKeycodes.KEY_ENTER);
    }

    /**
     * Adds a terminating space after German dead keys so the USB host emits them literally.
     *
     * @param text text that will be typed by InputStick
     * @return text with German dead-key terminators
     */
    static String addGermanDeadKeyTerminators(String text) {
        StringBuilder result = new StringBuilder(text.length());
        for (int index = 0; index < text.length(); index++) {
            char character = text.charAt(index);
            result.append(character);
            if (character == '^' || character == '`' || character == '´') {
                result.append(' ');
            }
        }
        return result.toString();
    }
}
