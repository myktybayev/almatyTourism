package kz.informatics.okulik.nalog_app.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kz.informatics.okulik.MainActivity;
import kz.informatics.okulik.R;

/**
 * Create Account (Registration) screen.
 * Design: same header as login, form with Full Name, Phone (+7), Email, Password, Sign Up button.
 */
public class RegistrationActivity extends AppCompatActivity {

    private EditText editFullName;
    private EditText editPhone;
    private EditText editEmail;
    private EditText editPassword;
    private ImageButton buttonTogglePassword;
    private android.widget.Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editFullName = findViewById(R.id.editFullName);
        editPhone = findViewById(R.id.editPhone);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        buttonTogglePassword = findViewById(R.id.buttonTogglePassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
        buttonSignUp.setOnClickListener(v -> onSignUp());
    }

    private boolean passwordVisible = false;

    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        editPassword.setInputType(
                passwordVisible
                        ? android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        : android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        buttonTogglePassword.setImageResource(passwordVisible ? R.drawable.ic_visibility_24 : R.drawable.ic_visibility_off_24);
        editPassword.setSelection(editPassword.getText() != null ? editPassword.getText().length() : 0);
    }

    private void onSignUp() {
        String fullName = editFullName.getText() != null ? editFullName.getText().toString().trim() : "";
        String phone = editPhone.getText() != null ? editPhone.getText().toString().trim() : "";
        String email = editEmail.getText() != null ? editEmail.getText().toString().trim() : "";
        String password = editPassword.getText() != null ? editPassword.getText().toString() : "";

        if (TextUtils.isEmpty(fullName)) {
            editFullName.setError(getString(R.string.reg_full_name_hint));
            return;
        }
        if (phone.length() != 10) {
            editPhone.setError(getString(R.string.login_phone_hint));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editEmail.setError(getString(R.string.login_email_hint));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editPassword.setError(getString(R.string.reg_password_hint));
            return;
        }

        AuthRepository auth = new AuthRepository(this);
        if (!auth.register(email, password, fullName, phone)) {
            editEmail.setError(getString(R.string.auth_email_exists));
            return;
        }
        auth.saveSession(new User(email, "", fullName, phone));
        Toast.makeText(this, getString(R.string.reg_sign_up), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
