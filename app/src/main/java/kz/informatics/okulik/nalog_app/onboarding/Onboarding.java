package kz.informatics.okulik.nalog_app.onboarding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import kz.informatics.okulik.MainActivity;
import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.profile.LocaleHelper;

public class Onboarding extends AppCompatActivity {

    private final int[] images = new int[] {
            R.drawable.onboard1,
            R.drawable.onboard2,
            R.drawable.onboard3,
            R.drawable.onboard4
    };

    private final int[] titleTop = new int[] {
            R.string.onboarding1_title_top,
            R.string.onboarding2_title_top,
            R.string.onboarding3_title_top,
            R.string.onboarding4_title_top
    };

    private final int[] titleBottom = new int[] {
            R.string.onboarding1_title_bottom,
            R.string.onboarding2_title_bottom,
            R.string.onboarding3_title_bottom,
            R.string.onboarding4_title_bottom
    };

    private final int[] descriptions = new int[] {
            R.string.onboarding1_description,
            R.string.onboarding2_description,
            R.string.onboarding3_description,
            R.string.onboarding4_description
    };

    private int currentIndex = 0;

    private ImageView image;
    private LinearLayout dotsContainer;
    private TextView titleTopText;
    private TextView titleBottomText;
    private TextView descriptionText;
    private Button nextButton;
    private TextView skip;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.applyLocale(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.onboarding);

        image = findViewById(R.id.onboardingImage);
        dotsContainer = findViewById(R.id.dotsContainer);
        titleTopText = findViewById(R.id.titleTop);
        titleBottomText = findViewById(R.id.titleBottom);
        descriptionText = findViewById(R.id.description);
        nextButton = findViewById(R.id.nextButton);
        skip = findViewById(R.id.skip);

        createDots();
        renderPage();

        nextButton.setOnClickListener(v -> {
            if (currentIndex < images.length - 1) {
                currentIndex++;
                renderPage();
            } else {
                openMain();
            }
        });

        skip.setOnClickListener(v -> openMain());
    }

    private void openMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void createDots() {
        dotsContainer.removeAllViews();

        int dotSize = dp(8);
        int dotSelectedWidth = dp(22);
        int dotSpacing = dp(8);

        for (int i = 0; i < images.length; i++) {
            ImageView dot = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dotSize, dotSize);
            lp.setMargins(0, 0, dotSpacing, 0);
            dot.setLayoutParams(lp);
            dot.setImageResource(R.drawable.onboarding_dot_unselected);
            dotsContainer.addView(dot);
        }

        // pre-size the first dot as selected (renderPage will update)
        if (dotsContainer.getChildCount() > 0) {
            ImageView first = (ImageView) dotsContainer.getChildAt(0);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) first.getLayoutParams();
            lp.width = dotSelectedWidth;
            lp.height = dotSize;
            first.setLayoutParams(lp);
        }
    }

    private void renderPage() {
        image.setImageResource(images[currentIndex]);
        titleTopText.setText(getString(titleTop[currentIndex]));
        titleBottomText.setText(getString(titleBottom[currentIndex]));
        descriptionText.setText(getString(descriptions[currentIndex]));

        nextButton.setText(
                currentIndex == images.length - 1
                        ? getString(R.string.onboarding_get_started)
                        : getString(R.string.onboarding_next_arrow));
        skip.setVisibility(currentIndex == images.length - 1 ? TextView.INVISIBLE : TextView.VISIBLE);

        int dotSize = dp(8);
        int dotSelectedWidth = dp(22);
        int dotSpacing = dp(8);

        for (int i = 0; i < dotsContainer.getChildCount(); i++) {
            ImageView dot = (ImageView) dotsContainer.getChildAt(i);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) dot.getLayoutParams();
            lp.setMargins(0, 0, dotSpacing, 0);

            if (i == currentIndex) {
                dot.setImageResource(R.drawable.onboarding_dot_selected);
                lp.width = dotSelectedWidth;
                lp.height = dotSize;
            } else {
                dot.setImageResource(R.drawable.onboarding_dot_unselected);
                lp.width = dotSize;
                lp.height = dotSize;
            }
            dot.setLayoutParams(lp);
        }
    }

    private int dp(int value) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(value * density);
    }
}
