package kz.informatics.okulik.nalog_app.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;
import kz.informatics.okulik.R;

public class ProfileFragment extends Fragment {

    private static final int REQUEST_EDIT_PROFILE = 2001;

    private TextView textCurrentLanguage;
    private LocaleHelper localeHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        localeHelper = new LocaleHelper(requireContext());
        textCurrentLanguage = view.findViewById(R.id.textCurrentLanguage);
        updateLanguageDisplay();
        bindLoggedInUser(view);
        setupListeners(view);
    }

    private void bindLoggedInUser(View root) {
        AuthRepository auth = new AuthRepository(requireContext());
        User user = auth.getCurrentUser();
        if (user == null) return;
        TextView name = root.findViewById(R.id.textUserName);
        TextView email = root.findViewById(R.id.textEmail);
        TextView phone = root.findViewById(R.id.textPhone);
        if (name != null) name.setText(user.fullName != null && !user.fullName.isEmpty() ? user.fullName : getString(R.string.profile_title));
        if (email != null) email.setText(user.email != null ? user.email : "");
        if (phone != null) {
            String p = user.phone != null ? user.phone.trim() : "";
            phone.setText(p.startsWith("+") ? p : (p.isEmpty() ? "" : "+7 " + p));
        }
        CircleImageView imageAvatar = root.findViewById(R.id.imageAvatar);
        if (imageAvatar != null) {
            String path = auth.getAvatarPath(user.email);
            if (path != null && new File(path).exists()) {
                try {
                    Bitmap bmp = BitmapFactory.decodeFile(path);
                    if (bmp != null) imageAvatar.setImageBitmap(bmp);
                } catch (Exception ignored) { }
            }
        }
    }

    private void updateLanguageDisplay() {
        if (textCurrentLanguage != null) {
            textCurrentLanguage.setText(LocaleHelper.getLanguageDisplayName(localeHelper.getLanguage()));
        }
    }

    private void setupListeners(View root) {
        root.findViewById(R.id.buttonEditAvatar).setOnClickListener(v -> openEditProfile());

        root.findViewById(R.id.rowEditProfile).setOnClickListener(v -> openEditProfile());

        /*
        root.findViewById(R.id.rowPaymentMethods).setOnClickListener(v ->
                Toast.makeText(getContext(), getString(R.string.payment_methods), Toast.LENGTH_SHORT).show());
        */
        root.findViewById(R.id.rowLanguage).setOnClickListener(v -> showLanguageDialog());

        root.findViewById(R.id.rowHelpSupport).setOnClickListener(v -> {
            sendWhatsAppMessage(getActivity(), "7714201162", "Помощь и поддержка");
        });

        root.findViewById(R.id.rowLogOut).setOnClickListener(v -> {
            new AuthRepository(requireContext()).clearSession();
            Intent i = new Intent(requireContext(), LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            if (getActivity() != null) getActivity().finish();
        });
    }
    public void sendWhatsAppMessage(Context context, String phoneNumber, String message) {
        try {
            String url = "https://api.whatsapp.com/send?phone="
                    + phoneNumber
                    + "&text="
                    + URLEncoder.encode(message, "UTF-8");

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setPackage("com.whatsapp");

            context.startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLanguageDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_language, null);
        dialog.setContentView(dialogView);

        dialogView.findViewById(R.id.optionKazakh).setOnClickListener(v -> selectLanguageAndClose(dialog, LocaleHelper.LANG_KK));
        dialogView.findViewById(R.id.optionRussian).setOnClickListener(v -> selectLanguageAndClose(dialog, LocaleHelper.LANG_RU));
        dialogView.findViewById(R.id.optionEnglish).setOnClickListener(v -> selectLanguageAndClose(dialog, LocaleHelper.LANG_EN));

        dialog.show();
    }

    private void selectLanguageAndClose(Dialog dialog, String langCode) {
        localeHelper.setLanguage(langCode);
        dialog.dismiss();
        if (getActivity() != null) {
            getActivity().recreate();
        }
    }

    private void openEditProfile() {
        startActivityForResult(new Intent(requireContext(), EditProfileActivity.class), REQUEST_EDIT_PROFILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == Activity.RESULT_OK && getView() != null) {
            bindLoggedInUser(getView());
        }
    }
}
