package kz.informatics.okulik.nalog_app.exchange;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Fetches real-time currency rates from ExchangeRate-API (free, no key).
 * Base: USD. Supports KZT, EUR, RUB, CNY and 160+ currencies.
 */
public class ExchangeRatesRepository {

    private static final String API_URL = "https://open.er-api.com/v6/latest/USD";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface Callback {
        void onSuccess(ExchangeRatesData data);
        void onError(String message);
    }

    public void fetchRates(Callback callback) {
        executor.execute(() -> {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                int code = conn.getResponseCode();
                if (code != 200) {
                    notifyError(callback, "HTTP " + code);
                    return;
                }
                BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) sb.append(line);
                r.close();

                JSONObject root = new JSONObject(sb.toString());
                if (!"success".equals(root.optString("result"))) {
                    notifyError(callback, "API error");
                    return;
                }

                long lastUpdateUnix = root.optLong("time_last_update_unix", 0) * 1000L;
                String lastUpdateUtc = root.optString("time_last_update_utc", "");
                JSONObject ratesObj = root.getJSONObject("rates");

                double usdKzt = ratesObj.optDouble("KZT", 450);
                double eurKzt = usdKzt / ratesObj.optDouble("EUR", 0.92);
                double rubKzt = usdKzt / ratesObj.optDouble("RUB", 90);
                double cnyKzt = usdKzt / ratesObj.optDouble("CNY", 7.2);

                List<CurrencyRate> list = new ArrayList<>();
                list.add(new CurrencyRate("USD", "$", usdKzt, 0.4));
                list.add(new CurrencyRate("EUR", "€", eurKzt, -0.1));
                list.add(new CurrencyRate("RUB", "₽", rubKzt, 1.2));
                list.add(new CurrencyRate("CNY", "¥", cnyKzt, 0.05));

                ExchangeRatesData data = new ExchangeRatesData(
                        usdKzt, eurKzt, rubKzt, cnyKzt,
                        list,
                        lastUpdateUnix > 0 ? new Date(lastUpdateUnix) : new Date(),
                        lastUpdateUtc,
                        ratesObj
                );
                notifySuccess(callback, data);
            } catch (Exception e) {
                notifyError(callback, e.getMessage() != null ? e.getMessage() : "Network error");
            }
        });
    }

    private void notifySuccess(Callback callback, ExchangeRatesData data) {
        mainHandler.post(() -> callback.onSuccess(data));
    }

    private void notifyError(Callback callback, String msg) {
        mainHandler.post(() -> callback.onError(msg));
    }

    public static class CurrencyRate {
        public final String code;
        public final String symbol;
        public final double rateToKzt;
        /** Percent change (demo values; API doesn't provide daily delta in free tier) */
        public final double percentChange;

        CurrencyRate(String code, String symbol, double rateToKzt, double percentChange) {
            this.code = code;
            this.symbol = symbol;
            this.rateToKzt = rateToKzt;
            this.percentChange = percentChange;
        }
    }

    public static class ExchangeRatesData {
        public final double usdToKzt;
        public final double eurToKzt;
        public final double rubToKzt;
        public final double cnyToKzt;
        public final List<CurrencyRate> rateCards;
        public final Date lastUpdate;
        public final String lastUpdateUtc;
        public final JSONObject allRates;

        ExchangeRatesData(double usd, double eur, double rub, double cny,
                          List<CurrencyRate> cards, Date lastUpdate, String lastUpdateUtc,
                          JSONObject allRates) {
            this.usdToKzt = usd;
            this.eurToKzt = eur;
            this.rubToKzt = rub;
            this.cnyToKzt = cny;
            this.rateCards = cards;
            this.lastUpdate = lastUpdate;
            this.lastUpdateUtc = lastUpdateUtc;
            this.allRates = allRates;
        }

        public double getRateFromUsd(String toCode) {
            if ("KZT".equals(toCode)) return usdToKzt;
            if ("USD".equals(toCode)) return 1.0;
            try {
                return allRates.optDouble(toCode, 1.0);
            } catch (Exception e) {
                return 1.0;
            }
        }

        public double getRateToKzt(String fromCode) {
            if ("KZT".equals(fromCode)) return 1.0;
            if ("USD".equals(fromCode)) return usdToKzt;
            double fromPerUsd = allRates.optDouble(fromCode, 1.0);
            if (fromPerUsd <= 0) return usdToKzt;
            return usdToKzt / fromPerUsd;
        }

        public double convert(String from, String to, double amount) {
            if (from.equals(to)) return amount;
            double amountKzt = amount * getRateToKzt(from);
            if ("KZT".equals(to)) return amountKzt;
            return amountKzt / getRateToKzt(to);
        }

        public double convertToKzt(String from, double amount) {
            return amount * getRateToKzt(from);
        }

        public double convertFromKzt(String to, double amount) {
            if ("KZT".equals(to)) return amount;
            return amount / getRateToKzt(to);
        }
    }
}
