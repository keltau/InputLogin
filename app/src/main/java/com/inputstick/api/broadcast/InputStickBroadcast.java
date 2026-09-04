package com.inputstick.api.broadcast;

import android.content.Context;
import android.content.Intent;

public final class InputStickBroadcast {

    public static final String PARAM_REQUEST = "REQUEST";
    public static final String PARAM_RELEASE = "RELEASE";
    public static final String PARAM_TEXT = "TEXT";
    public static final String PARAM_LAYOUT = "LAYOUT";
    public static final String PARAM_KEY = "KEY";
    public static final String PARAM_MODIFIER = "MODIFIER";

    private static final String ACTION_HID = "com.inputstick.apps.inputstickutility.HID";
    private static final String INPUTSTICK_UTILITY_PACKAGE = "com.inputstick.apps.inputstickutility";
    private static final String HID_RECEIVER =
            "com.inputstick.apps.inputstickutility.service.HIDReceiver";

    private InputStickBroadcast() {
    }

    public static void requestConnection(Context context) {
        Intent intent = new Intent();
        intent.putExtra(PARAM_REQUEST, true);
        send(context, intent);
    }

    public static void releaseConnection(Context context) {
        Intent intent = new Intent();
        intent.putExtra(PARAM_RELEASE, true);
        send(context, intent);
    }

    public static void type(Context context, String text, String layoutCode) {
        Intent intent = new Intent();
        intent.putExtra(PARAM_TEXT, text);
        if (layoutCode != null) {
            intent.putExtra(PARAM_LAYOUT, layoutCode);
        }
        send(context, intent);
    }

    public static void pressAndRelease(Context context, byte modifiers, byte key) {
        Intent intent = new Intent();
        intent.putExtra(PARAM_MODIFIER, modifiers);
        intent.putExtra(PARAM_KEY, key);
        send(context, intent);
    }

    private static void send(Context context, Intent intent) {
        intent.setAction(ACTION_HID);
        intent.setClassName(INPUTSTICK_UTILITY_PACKAGE, HID_RECEIVER);
        context.sendBroadcast(intent);
    }
}
