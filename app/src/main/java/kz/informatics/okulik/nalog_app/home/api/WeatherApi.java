package kz.informatics.okulik.nalog_app.home.api;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Open-Meteo API – Almaty қаласы үшін live погода.
 * API key қажет емес.
 */
public class WeatherApi {

    private static final String ALMATY_URL =
            "https://api.open-meteo.com/v1/forecast?latitude=43.2567&longitude=76.9286&current_weather=true";

    private static final long REFRESH_INTERVAL_MS = 30 * 60 * 1000; // 30 минут

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface Callback {
        void onSuccess(String temperatureC, String description);
        void onError(String message);
    }

    public void fetchAlmatyWeather(Callback callback) {
        executor.execute(() -> {
            try {
                URL url = new URL(ALMATY_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                int code = conn.getResponseCode();
                if (code != HttpURLConnection.HTTP_OK) {
                    postError(callback, "HTTP " + code);
                    return;
                }

                StringBuilder sb = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) sb.append(line);
                }
                conn.disconnect();

                JSONObject root = new JSONObject(sb.toString());
                JSONObject current = root.getJSONObject("current_weather");
                double temp = current.getDouble("temperature");
                int codeWmo = current.optInt("weathercode", 0);

                String desc = getWeatherDescription(codeWmo);
                int tempInt = (int) Math.round(temp);
                String tempStr = tempInt + "°C";

                postSuccess(callback, tempStr, desc);
            } catch (Exception e) {
                postError(callback, e.getMessage());
            }
        });
    }

    private static String getWeatherDescription(int wmoCode) {
        switch (wmoCode) {
            case 0:  return "Ашық";
            case 1:  return "Негізінен ашық";
            case 2:  return "Ауа райы бөлінген";
            case 3:  return "Бұлтты";
            case 45:
            case 48: return "Тұман";
            case 51:
            case 53:
            case 55: return "Шыңғыр";
            case 56:
            case 57: return "Мұзды шыңғыр";
            case 61:
            case 63:
            case 65: return "Жаңбыр";
            case 66:
            case 67: return "Мұзды жаңбыр";
            case 71:
            case 73:
            case 75: return "Қар";
            case 77: return "Қар бөлшектері";
            case 80:
            case 81:
            case 82: return "Жаңбыр душасы";
            case 85:
            case 86: return "Қар душасы";
            case 95: return "Найзағай";
            case 96:
            case 99: return "Найзағай + бұршақ";
            default: return "Ауа райы";
        }
    }

    private void postSuccess(Callback c, String temp, String desc) {
        mainHandler.post(() -> {
            if (c != null) c.onSuccess(temp, desc);
        });
    }

    private void postError(Callback c, String msg) {
        mainHandler.post(() -> {
            if (c != null) c.onError(msg != null ? msg : "Қате");
        });
    }

    /**
     * Live жаңарту үшін интервал (миллисекунд).
     */
    public long getRefreshIntervalMs() {
        return REFRESH_INTERVAL_MS;
    }

    public void shutdown() {
        executor.shutdown();
    }
}
