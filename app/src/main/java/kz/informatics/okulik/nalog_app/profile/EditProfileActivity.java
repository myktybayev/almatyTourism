package kz.informatics.okulik.nalog_app.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import kz.informatics.okulik.R;

/**
 * Edit profile: full name, phone, email (read-only). Avatar can be set from gallery.
 */
public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_IMAGE = 1001;

    private AuthRepository auth;
    private User user;
    private CircleImageView imageAvatar;
    private EditText editFullName;
    private EditText editPhone;
    private TextView textEmail;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        auth = new AuthRepository(this);
        user = auth.getCurrentUser();
        if (user == null) {
            finish();
            return;
        }

        imageAvatar = findViewById(R.id.imageAvatar);
        editFullName = findViewById(R.id.editFullName);
        editPhone = findViewById(R.id.editPhone);
        textEmail = findViewById(R.id.textEmail);
        Button buttonSave = findViewById(R.id.buttonSave);
        ImageView buttonChangePhoto = findViewById(R.id.buttonChangePhoto);

        editFullName.setText(user.fullName != null ? user.fullName : "");
        String phone = user.phone != null ? user.phone.trim() : "";
        editPhone.setText(phone.startsWith("+") ? phone.replaceFirst("^\\+7\\s*", "") : phone);
        textEmail.setText(user.email != null ? user.email : "");

        loadAvatarIntoView();

        findViewById(R.id.buttonBack).setOnClickListener(v -> finish());

        if (buttonChangePhoto != null) {
            buttonChangePhoto.setOnClickListener(v -> openGallery());
        }
        imageAvatar.setOnClickListener(v -> openGallery());

        buttonSave.setOnClickListener(v -> saveProfile());
    }

    private void loadAvatarIntoView() {
        String path = auth.getAvatarPath(user.email);
        if (path != null && new File(path).exists()) {
            try {
                Bitmap bmp = BitmapFactory.decodeFile(path);
                if (bmp != null) {
                    imageAvatar.setImageBitmap(bmp);
                }
            } catch (Exception ignored) {
            }
        }
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, getString(R.string.edit_profile_change_photo)), REQUEST_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_PICK_IMAGE || resultCode != RESULT_OK || data == null || data.getData() == null) {
            return;
        }
        Uri uri = data.getData();
        String savedPath = copyImageToAppStorage(uri);
        if (savedPath != null) {
            auth.setAvatarPath(user.email, savedPath);
            loadAvatarIntoView();
            Toast.makeText(this, R.string.edit_profile_change_photo, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.edit_profile_photo_error, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Copy image from content URI to app internal storage (avatars folder). Returns absolute path or null.
     */
    @Nullable
    private String copyImageToAppStorage(Uri sourceUri) {
        File dir = new File(getFilesDir(), "avatars");
        if (!dir.exists() && !dir.mkdirs()) return null;
        String safeName = (user.email != null ? user.email : "user").replace("@", "_").replace(".", "_") + ".jpg";
        File dest = new File(dir, safeName);
        try (InputStream in = getContentResolver().openInputStream(sourceUri);
             FileOutputStream out = new FileOutputStream(dest)) {
            if (in == null) return null;
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) {
                out.write(buf, 0, n);
            }
            return dest.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }

    private void saveProfile() {
        String fullName = editFullName.getText() != null ? editFullName.getText().toString().trim() : "";
        String phoneRaw = editPhone.getText() != null ? editPhone.getText().toString().trim() : "";
        String phone = phoneRaw.startsWith("+") ? phoneRaw : ("+7 " + phoneRaw);

        if (auth.updateUser(user.email, fullName, phone)) {
            setResult(RESULT_OK);
            finish();
            Toast.makeText(this, R.string.edit_profile_saved, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.edit_profile_save_error, Toast.LENGTH_SHORT).show();
        }
    }
}
