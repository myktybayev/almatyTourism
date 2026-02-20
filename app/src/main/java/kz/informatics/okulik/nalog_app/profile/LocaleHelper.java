package kz.informatics.okulik.nalog_app.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

/**
 * Тілді сақтау және қолдану.
 * Locale: kk (Қазақ), ru (Русский), en (English)
 */
public class LocaleHelper {

    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_LANGUAGE = "language";

    public static final String LANG_KK = "kk";
    public static final String LANG_RU = "ru";
    public static final String LANG_EN = "en";

    private final SharedPreferences prefs;

    public LocaleHelper(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setLanguage(String langCode) {
        prefs.edit().putString(KEY_LANGUAGE, langCode).apply();
    }

    public String getLanguage() {
        return prefs.getString(KEY_LANGUAGE, LANG_EN);
    }

    /**
     * Контекстке қолданылатын тілді қайтарады.
     */
    public static Context applyLocale(Context context) {
        String lang = new LocaleHelper(context).getLanguage();
        return updateResources(context, lang);
    }

    private static Context updateResources(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            config.setLocales(new LocaleList(locale));
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    /**
     * Тіл атын көрсету үшін (мысалы "Қазақ тілі", "English").
     */
    public static String getLanguageDisplayName(String langCode) {
        switch (langCode) {
            case LANG_KK: return "Қазақ тілі";
            case LANG_RU: return "Русский язык";
            case LANG_EN:
            default: return "English";
        }
    }
}
