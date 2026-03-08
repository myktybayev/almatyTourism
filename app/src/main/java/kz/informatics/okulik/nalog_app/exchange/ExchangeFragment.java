package kz.informatics.okulik.nalog_app.exchange;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kz.informatics.okulik.R;

/**
 * Currency Exchange screen: converter (you pay / you get) and today's rates grid.
 * Uses ExchangeRate-API (free, no key) for real-time KZT, USD, EUR, RUB, CNY.
 */
public class ExchangeFragment extends Fragment {

    private static final String[] CURRENCIES = {"USD", "KZT", "EUR", "RUB", "CNY"};

    private EditText editYouPay;
    private TextView textYouGet;
    private TextView textPayCurrency;
    private TextView textGetCurrency;
    private TextView textRateInfo;
    private TextView textRatesTimestamp;
    private GridLayout gridRates;
    private ImageButton buttonSwap;

    private ExchangeRatesRepository.ExchangeRatesData ratesData;
    private String payCurrency = "USD";
    private String getCurrency = "KZT";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exchange, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editYouPay = view.findViewById(R.id.editYouPay);
        textYouGet = view.findViewById(R.id.textYouGet);
        textPayCurrency = view.findViewById(R.id.textPayCurrency);
        textGetCurrency = view.findViewById(R.id.textGetCurrency);
        textRateInfo = view.findViewById(R.id.textRateInfo);
        textRatesTimestamp = view.findViewById(R.id.textRatesTimestamp);
        gridRates = view.findViewById(R.id.gridRates);
        buttonSwap = view.findViewById(R.id.buttonSwap);

        editYouPay.setText("100");
        textYouGet.setText("—");

        editYouPay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                recalcConverted();
            }
        });

        buttonSwap.setOnClickListener(v -> {
            String t = payCurrency;
            payCurrency = getCurrency;
            getCurrency = t;
            textPayCurrency.setText(payCurrency);
            textGetCurrency.setText(getCurrency);
            recalcConverted();
        });

        textPayCurrency.setOnClickListener(v -> showCurrencyPicker(true));
        textGetCurrency.setOnClickListener(v -> showCurrencyPicker(false));

        loadRates();
    }

    private void loadRates() {
        new ExchangeRatesRepository().fetchRates(new ExchangeRatesRepository.Callback() {
            @Override
            public void onSuccess(ExchangeRatesRepository.ExchangeRatesData data) {
                ratesData = data;
                textRatesTimestamp.setText(formatTimestamp(data.lastUpdate));
                bindRateCards();
                recalcConverted();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(requireContext(), getString(R.string.exchange_error) + " " + message,
                        Toast.LENGTH_SHORT).show();
                textRatesTimestamp.setText(getString(R.string.exchange_rates_unavailable));
            }
        });
    }

    private String formatTimestamp(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, HH:mm", Locale.getDefault());
        return sdf.format(date);
    }

    private void recalcConverted() {
        if (ratesData == null) {
            textYouGet.setText("—");
            textRateInfo.setText("");
            return;
        }
        String s = editYouPay.getText() != null ? editYouPay.getText().toString().trim() : "";
        double amount = 0;
        try {
            if (!s.isEmpty()) amount = Double.parseDouble(s.replace(",", "."));
        } catch (NumberFormatException ignored) {}
        double result = ratesData.convert(payCurrency, getCurrency, amount);
        textYouGet.setText(formatAmount(result));
        double onePayToGet = ratesData.convert(payCurrency, getCurrency, 1.0);
        textRateInfo.setText(String.format(Locale.US, "1 %s = %s %s",
                payCurrency, formatRate(onePayToGet), getCurrency));
    }

    private String formatAmount(double v) {
        if (v >= 1000) {
            return String.format(Locale.US, "%,.0f", v);
        }
        return String.format(Locale.US, "%.2f", v);
    }

    private String formatRate(double v) {
        if (v >= 1000 || v < 0.01) return String.format(Locale.US, "%,.2f", v);
        return String.format(Locale.US, "%.2f", v);
    }

    private void bindRateCards() {
        gridRates.removeAllViews();
        if (ratesData == null) return;
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (int i = 0; i < ratesData.rateCards.size(); i++) {
            ExchangeRatesRepository.CurrencyRate r = ratesData.rateCards.get(i);
            View card = inflater.inflate(R.layout.item_exchange_rate_card, gridRates, false);
            TextView sym = card.findViewById(R.id.textCurrencySymbol);
            TextView code = card.findViewById(R.id.textCurrencyCode);
            TextView rate = card.findViewById(R.id.textRate);
            TextView change = card.findViewById(R.id.textPercentChange);
            sym.setText(r.symbol);
            code.setText(r.code);
            rate.setText(String.format(Locale.US, "%.2f 〒", r.rateToKzt));
            boolean positive = r.percentChange >= 0;
            change.setText(String.format(Locale.US, "%s%s%.2f%%",
                    positive ? "↑ " : "↓ ", positive ? "+" : "", r.percentChange));
            change.setTextColor(ContextCompat.getColor(requireContext(), positive ? R.color.exchange_positive : R.color.exchange_negative));
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0;
            lp.height = GridLayout.LayoutParams.WRAP_CONTENT;
            lp.columnSpec = GridLayout.spec(i % 2, 1f);
            lp.rowSpec = GridLayout.spec(i / 2, 1f);
            int dp6 = (int) (6 * getResources().getDisplayMetrics().density);
            lp.setMargins(dp6, dp6, dp6, dp6);
            gridRates.addView(card, lp);

//            int left, int top, int right, int bottom
        }
    }

    private void showCurrencyPicker(boolean isPay) {
        String[] opts = CURRENCIES;
        new android.app.AlertDialog.Builder(requireContext())
                .setItems(opts, (d, which) -> {
                    if (isPay) {
                        payCurrency = opts[which];
                        textPayCurrency.setText(payCurrency);
                    } else {
                        getCurrency = opts[which];
                        textGetCurrency.setText(getCurrency);
                    }
                    recalcConverted();
                })
                .show();
    }
}
