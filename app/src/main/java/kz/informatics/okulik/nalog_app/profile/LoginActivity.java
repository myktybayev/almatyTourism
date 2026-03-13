package kz.informatics.okulik.nalog_app.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kz.informatics.okulik.MainActivity;
import kz.informatics.okulik.R;

/**
 * Login / Registration entry screen.
 * Design: header image with "Almaty" branding, white form with rounded top.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private ImageButton buttonTogglePassword;
    private View buttonLogIn;
    private View buttonCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        buttonTogglePassword = findViewById(R.id.buttonTogglePassword);
        buttonLogIn = findViewById(R.id.buttonLogIn);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);

        buttonTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
        buttonLogIn.setOnClickListener(v -> onLogIn());
        buttonCreateAccount.setOnClickListener(v -> onCreateAccount());
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

    private void onLogIn() {
        String email = editEmail.getText() != null ? editEmail.getText().toString().trim() : "";
        String password = editPassword.getText() != null ? editPassword.getText().toString() : "";
        if (TextUtils.isEmpty(email)) {
            editEmail.setError(getString(R.string.login_email_hint));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editPassword.setError(getString(R.string.login_password_hint));
            return;
        }
        AuthRepository auth = new AuthRepository(this);
        User user = auth.login(email, password);
        if (user == null) {
            Toast.makeText(this, getString(R.string.auth_invalid_credentials), Toast.LENGTH_SHORT).show();
            return;
        }
        auth.saveSession(user);
        Toast.makeText(this, getString(R.string.login_welcome_back), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void onCreateAccount() {
        startActivity(new Intent(this, RegistrationActivity.class));
    }
}
