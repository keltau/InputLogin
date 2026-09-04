package com.inputstick.apps.inputlogin;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InputStickBridgeTest {

    @Test
    public void addGermanDeadKeyTerminators_keepsRegularTextUnchanged() {
        assertEquals("user@example.com", InputStickBridge.addGermanDeadKeyTerminators("user@example.com"));
    }

    @Test
    public void addGermanDeadKeyTerminators_terminatesEachSupportedDeadKey() {
        assertEquals("^ value` test´ end",
                InputStickBridge.addGermanDeadKeyTerminators("^value`test´end"));
    }

    @Test
    public void addGermanDeadKeyTerminators_preservesExistingSpaces() {
        assertEquals("^  value", InputStickBridge.addGermanDeadKeyTerminators("^ value"));
    }
}
