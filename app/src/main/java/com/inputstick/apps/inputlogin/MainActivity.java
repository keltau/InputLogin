package com.inputstick.apps.inputlogin;

import android.os.Bundle;
import android.text.Editable;
import android.view.WindowManager;
import android.view.autofill.AutofillManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.color.DynamicColors;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private static final String PREFERENCES_NAME = "input_login_preferences";
    private static final String GERMAN_DEAD_KEYS_PREFERENCE = "german_dead_keys";
    private static final String REQUEST_AUTOFILL_EXTRA = "request_autofill";

    private InputStickBridge inputStickBridge;
    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private SwitchMaterial germanDeadKeysSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivityIfAvailable(this); // Material Colors follow system color
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE); // Credential protection
        // Apply system-bar and keyboard insets explicitly:
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);

        inputStickBridge = new InputStickBridge(this);
        bindViews();
        restorePreferences();
        bindActions();

        // Keep IME inset padding so the HID action buttons remain above the keyboard in edge-to-edge mode:
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    Math.max(systemBars.bottom, ime.bottom));
            return insets;
        });

        if (getIntent().getBooleanExtra(REQUEST_AUTOFILL_EXTRA, false)) {
            getIntent().removeExtra(REQUEST_AUTOFILL_EXTRA);
            usernameInput.post(this::requestAutofill);
        }
    }

    private void bindViews() {
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        germanDeadKeysSwitch = findViewById(R.id.german_dead_keys_switch);
    }

    private void restorePreferences() {
        boolean germanDeadKeysEnabled = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
                .getBoolean(GERMAN_DEAD_KEYS_PREFERENCE, false);
        germanDeadKeysSwitch.setChecked(germanDeadKeysEnabled);
    }

    private void bindActions() {
        findViewById(R.id.connect_button).setOnClickListener(view -> inputStickBridge.connect());
        findViewById(R.id.disconnect_button).setOnClickListener(view -> inputStickBridge.disconnect());
        findViewById(R.id.send_username_button).setOnClickListener(view -> sendUsername());
        findViewById(R.id.tab_button).setOnClickListener(view -> inputStickBridge.pressTab());
        findViewById(R.id.enter_button).setOnClickListener(view -> inputStickBridge.pressEnter());
        findViewById(R.id.send_password_button).setOnClickListener(view -> sendPassword());
        findViewById(R.id.clear_and_reload_button).setOnClickListener(view -> clearAndReload());
        germanDeadKeysSwitch.setOnCheckedChangeListener((button, isChecked) ->
                getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
                        .edit()
                        .putBoolean(GERMAN_DEAD_KEYS_PREFERENCE, isChecked)
                        .apply());
    }

    private void sendUsername() {
        Editable username = usernameInput.getText();
        if (username == null || username.length() == 0) {
            usernameInput.setError(getString(R.string.username_required));
            return;
        }
        inputStickBridge.sendText(username.toString(), germanDeadKeysSwitch.isChecked());
    }

    private void sendPassword() {
        Editable password = passwordInput.getText();
        if (password == null || password.length() == 0) {
            passwordInput.setError(getString(R.string.password_required));
            return;
        }
        inputStickBridge.sendText(password.toString(), germanDeadKeysSwitch.isChecked());
        password.clear();
    }

    private void clearAndReload() {
        Editable username = usernameInput.getText();
        Editable password = passwordInput.getText();
        if (username != null) {
            username.clear();
        }
        if (password != null) {
            password.clear();
        }

        AutofillManager autofillManager = getSystemService(AutofillManager.class);
        if (autofillManager != null) {
            autofillManager.cancel();
        }
        getIntent().putExtra(REQUEST_AUTOFILL_EXTRA, true);
        recreate();
    }

    private void requestAutofill() {
        usernameInput.requestFocus();
        AutofillManager autofillManager = getSystemService(AutofillManager.class);
        if (autofillManager != null) {
            autofillManager.requestAutofill(usernameInput);
        }
    }

}
