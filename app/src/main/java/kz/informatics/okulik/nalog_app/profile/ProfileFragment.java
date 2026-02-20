package kz.informatics.okulik.nalog_app.profile;

import android.app.Dialog;
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

import kz.informatics.okulik.R;

public class ProfileFragment extends Fragment {

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
        setupListeners(view);
    }

    private void updateLanguageDisplay() {
        if (textCurrentLanguage != null) {
            textCurrentLanguage.setText(LocaleHelper.getLanguageDisplayName(localeHelper.getLanguage()));
        }
    }

    private void setupListeners(View root) {
        root.findViewById(R.id.buttonEditAvatar).setOnClickListener(v ->
                Toast.makeText(getContext(), "Edit avatar", Toast.LENGTH_SHORT).show());

        root.findViewById(R.id.rowEditProfile).setOnClickListener(v ->
                Toast.makeText(getContext(), getString(R.string.edit_profile), Toast.LENGTH_SHORT).show());

        root.findViewById(R.id.rowPaymentMethods).setOnClickListener(v ->
                Toast.makeText(getContext(), getString(R.string.payment_methods), Toast.LENGTH_SHORT).show());

        root.findViewById(R.id.rowLanguage).setOnClickListener(v -> showLanguageDialog());

        root.findViewById(R.id.rowHelpSupport).setOnClickListener(v ->
                Toast.makeText(getContext(), getString(R.string.help_support), Toast.LENGTH_SHORT).show());

        root.findViewById(R.id.rowLogOut).setOnClickListener(v ->
                Toast.makeText(getContext(), getString(R.string.log_out), Toast.LENGTH_SHORT).show());
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
}
