package kz.informatics.okulik.nalog_app.trips;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.app.DatePickerDialog;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.home.module.DestinationsRepository;
import kz.informatics.okulik.nalog_app.home.module.PopularPlace;

/**
 * Self-planned trip detail: empty by default. Add Location opens dialog with
 * manual input,
 * search (live filter), and suggested places list.
 */
public class SelfPlannedTripActivity extends AppCompatActivity {

    public static final String EXTRA_TRIP_NAME = "trip_name";
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_START_DATE = "start_date";
    public static final String EXTRA_END_DATE = "end_date";
    public static final String EXTRA_EDIT_TRIP_ID = "edit_trip_id";

    private LinearLayout layoutItineraryStops;
    private LinearLayout layoutBudgetItems;
    private TextView textTotalEstimated;
    private TextView textTitle;
    private TextView textDateRange;
    private int itineraryStopCount = 0;
    private final List<BudgetExpense> budgetExpenses = new ArrayList<>();
    private String tripDescription = "";
    private String editingTripId = null;
    private Calendar startCal = Calendar.getInstance();
    private Calendar endCal = Calendar.getInstance();
    private static final int[] EXPENSE_ICONS = {
            R.drawable.ic_hotel_room, // 0 Accommodation
            R.drawable.ic_car, // 1 Transport
            R.drawable.ic_confirmation_number_24, // 2 Entry Tickets
            R.drawable.ic_dining // 3 Food (Custom)
    };

    public static void open(Context context, String tripName, String description, String startDate, String endDate) {
        Intent i = new Intent(context, SelfPlannedTripActivity.class);
        i.putExtra(EXTRA_TRIP_NAME, tripName);
        i.putExtra(EXTRA_DESCRIPTION, description);
        i.putExtra(EXTRA_START_DATE, startDate);
        i.putExtra(EXTRA_END_DATE, endDate);
        context.startActivity(i);
    }

    public static void openForEdit(Context context, SelfPlannedTrip trip) {
        if (trip == null) return;
        Intent i = new Intent(context, SelfPlannedTripActivity.class);
        i.putExtra(EXTRA_EDIT_TRIP_ID, trip.id);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_planned_trip);

        String name = getIntent().getStringExtra(EXTRA_TRIP_NAME);
        tripDescription = getIntent().getStringExtra(EXTRA_DESCRIPTION) != null ? getIntent().getStringExtra(EXTRA_DESCRIPTION) : "";
        String start = getIntent().getStringExtra(EXTRA_START_DATE);
        String end = getIntent().getStringExtra(EXTRA_END_DATE);

        textTitle = findViewById(R.id.textTitle);
        textDateRange = findViewById(R.id.textDateRange);
        textTitle.setText(name != null && !name.isEmpty() ? name : getString(R.string.create_trip_name_hint));

        initDatesFromIntent(start, end);
        textDateRange.setText(formatCalendarRange());

        View layoutDateRange = findViewById(R.id.layoutDateRange);
        if (layoutDateRange != null) {
            layoutDateRange.setOnClickListener(v -> showDateRangePicker());
        }

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        ImageButton buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> saveTrip());

        ImageButton buttonMenu = findViewById(R.id.buttonMenu);
        buttonMenu.setOnClickListener(
                v -> Toast.makeText(this, getString(R.string.trips_self_planned_menu), Toast.LENGTH_SHORT).show());

        layoutItineraryStops = findViewById(R.id.layoutItineraryStops);
        layoutBudgetItems = findViewById(R.id.layoutBudgetItems);
        textTotalEstimated = findViewById(R.id.textTotalEstimated);
        textTotalEstimated.setText("0 ₸");

        editingTripId = getIntent().getStringExtra(EXTRA_EDIT_TRIP_ID);
        if (editingTripId != null) {
            SelfPlannedTrip trip = SelfPlannedTripsRepository.getInstance(this).getById(editingTripId);
            if (trip != null) loadTripData(trip);
        }

        TextView buttonAddStop = findViewById(R.id.buttonAddStop);
        buttonAddStop.setOnClickListener(v -> showAddLocationDialog());

        TextView buttonAddExpense = findViewById(R.id.buttonAddExpense);
        buttonAddExpense.setOnClickListener(v -> showAddExpenseDialog());
    }

    private void loadTripData(SelfPlannedTrip trip) {
        textTitle.setText(trip.title);
        parseDatesFromDisplay(trip.dates);
        textDateRange.setText(formatCalendarRange());
        tripDescription = trip.locationTheme != null ? trip.locationTheme : "";

        layoutItineraryStops.removeAllViews();
        itineraryStopCount = 0;
        for (String stopName : trip.stopNames) {
            if (stopName == null || stopName.isEmpty()) continue;
            itineraryStopCount++;
            View stopView = LayoutInflater.from(this).inflate(R.layout.item_self_planned_stop, layoutItineraryStops, false);
            TextView textStopTitle = stopView.findViewById(R.id.textStopTitle);
            EditText editNote = stopView.findViewById(R.id.editNote);
            textStopTitle.setText(itineraryStopCount + ". " + stopName);
            editNote.setHint(R.string.self_planned_add_note_hint);
            stopView.findViewById(R.id.buttonReorder).setOnClickListener(v -> {});
            stopView.findViewById(R.id.buttonEditNote).setOnClickListener(v -> editNote.requestFocus());
            stopView.findViewById(R.id.buttonDelete).setOnClickListener(v -> {
                layoutItineraryStops.removeView(stopView);
                itineraryStopCount--;
            });
            layoutItineraryStops.addView(stopView);
        }

        budgetExpenses.clear();
        layoutBudgetItems.removeAllViews();
        for (SelfPlannedTrip.SavedExpense e : trip.expenses) {
            BudgetExpense entry = new BudgetExpense(e.category, e.amountKzt, e.note);
            budgetExpenses.add(entry);
            View row = LayoutInflater.from(this).inflate(R.layout.item_self_planned_expense, layoutBudgetItems, false);
            ImageView icon = row.findViewById(R.id.iconExpense);
            TextView label = row.findViewById(R.id.textExpenseLabel);
            TextView amountView = row.findViewById(R.id.textExpenseAmount);
            int iconIndex = getExpenseIconIndex(e.category);
            icon.setImageResource(EXPENSE_ICONS[iconIndex]);
            label.setText(e.category);
            amountView.setText(formatAmount(e.amountKzt));
            row.setTag(entry);
            row.setOnLongClickListener(v -> {
                budgetExpenses.remove(entry);
                layoutBudgetItems.removeView(row);
                updateTotalEstimated();
                return true;
            });
            layoutBudgetItems.addView(row);
        }
        updateTotalEstimated();
    }

    private void initDatesFromIntent(String start, String end) {
        if (start != null && !start.isEmpty() && end != null && !end.isEmpty()) {
            if (parseDateToCalendar(start, startCal) && parseDateToCalendar(end, endCal)) {
                if (endCal.before(startCal)) endCal.setTimeInMillis(startCal.getTimeInMillis());
                return;
            }
        }
        startCal = Calendar.getInstance();
        endCal = Calendar.getInstance();
        endCal.add(Calendar.DAY_OF_MONTH, 3);
    }

    private boolean parseDateToCalendar(String dateStr, Calendar out) {
        if (dateStr == null || dateStr.isEmpty()) return false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("d MMM", Locale.US);
            out.setTime(sdf.parse(dateStr.trim()));
            if (out.get(Calendar.YEAR) < 2000) out.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void parseDatesFromDisplay(String datesStr) {
        if (datesStr == null || datesStr.isEmpty()) return;
        String[] parts = datesStr.split("\\s*-\\s*", 2);
        if (parts.length >= 2) {
            parseDateToCalendar(parts[0].trim(), startCal);
            parseDateToCalendar(parts[1].trim(), endCal);
        }
    }

    private String formatCalendarRange() {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM", Locale.US);
        return sdf.format(startCal.getTime()) + " - " + sdf.format(endCal.getTime());
    }

    private void showDateRangePicker() {
        new DatePickerDialog(this, (v, year, month, dayOfMonth) -> {
            startCal.set(Calendar.YEAR, year);
            startCal.set(Calendar.MONTH, month);
            startCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            new DatePickerDialog(this, (v2, year2, month2, dayOfMonth2) -> {
                endCal.set(Calendar.YEAR, year2);
                endCal.set(Calendar.MONTH, month2);
                endCal.set(Calendar.DAY_OF_MONTH, dayOfMonth2);
                if (endCal.before(startCal)) endCal.setTimeInMillis(startCal.getTimeInMillis());
                textDateRange.setText(formatCalendarRange());
            }, endCal.get(Calendar.YEAR), endCal.get(Calendar.MONTH), endCal.get(Calendar.DAY_OF_MONTH)).show();
        }, startCal.get(Calendar.YEAR), startCal.get(Calendar.MONTH), startCal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String formatDateRange(String start, String end) {
        if (start == null && end == null)
            return "";
        if (start != null && end != null)
            return start + " - " + end;
        return start != null ? start : (end != null ? end : "");
    }

    private void saveTrip() {
        String title = textTitle.getText() != null ? textTitle.getText().toString().trim() : "";
        if (title.isEmpty()) {
            title = getString(R.string.create_trip_name_hint);
        }
        String dates = formatCalendarRange();
        int totalCost = 0;
        for (BudgetExpense e : budgetExpenses) {
            totalCost += (int) e.amountKzt;
        }

        List<String> stopNames = new ArrayList<>();
        for (int i = 0; i < layoutItineraryStops.getChildCount(); i++) {
            View child = layoutItineraryStops.getChildAt(i);
            TextView titleView = child.findViewById(R.id.textStopTitle);
            if (titleView != null && titleView.getText() != null) {
                String t = titleView.getText().toString().trim();
                if (t.isEmpty()) continue;
                String name = t.replaceFirst("^\\d+\\.\\s*", "").trim();
                if (!name.isEmpty()) stopNames.add(name);
            }
        }
        List<SelfPlannedTrip.SavedExpense> expenses = new ArrayList<>();
        for (BudgetExpense e : budgetExpenses) {
            expenses.add(new SelfPlannedTrip.SavedExpense(e.category, e.amountKzt, e.note));
        }

        String id = editingTripId != null ? editingTripId : "trip_" + System.currentTimeMillis();
        SelfPlannedTrip trip = new SelfPlannedTrip(
                id,
                title,
                tripDescription != null ? tripDescription : "",
                dates,
                null,
                totalCost,
                stopNames,
                expenses
        );
        SelfPlannedTripsRepository repo = SelfPlannedTripsRepository.getInstance(this);
        if (editingTripId != null) {
            repo.update(trip);
        } else {
            repo.add(trip);
        }
        Toast.makeText(this, getString(R.string.self_planned_trip_saved), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showAddLocationDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_location, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            bottomSheet.setBackgroundResource(android.R.color.transparent);
        }

        EditText editTypeLocationManually = view.findViewById(R.id.editTypeLocationManually);
        EditText editSearchPlaces = view.findViewById(R.id.editSearchPlaces);
        RecyclerView recyclerSuggestedPlaces = view.findViewById(R.id.recyclerSuggestedPlaces);

        recyclerSuggestedPlaces.setLayoutManager(new LinearLayoutManager(this));
        SuggestedPlaceAdapter adapter = new SuggestedPlaceAdapter();
        List<PopularPlace> allPlaces = DestinationsRepository.getInstance().getPlacesForExplore(this);
        adapter.setPlaces(allPlaces);
        adapter.setOnPlaceSelectedListener(place -> {
            addStopToItinerary(place.title);
            dialog.dismiss();
        });
        recyclerSuggestedPlaces.setAdapter(adapter);

        editSearchPlaces.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s != null ? s.toString() : "");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        view.findViewById(R.id.buttonCloseAddLocation).setOnClickListener(v -> dialog.dismiss());

        view.findViewById(R.id.buttonAddManualLocation).setOnClickListener(v -> {
            String manual = editTypeLocationManually.getText() != null
                    ? editTypeLocationManually.getText().toString().trim()
                    : "";
            if (!manual.isEmpty()) {
                addStopToItinerary(manual);
                dialog.dismiss();
            }
        });

        editTypeLocationManually.setOnEditorActionListener((v, actionId, event) -> {
            String manual = editTypeLocationManually.getText() != null
                    ? editTypeLocationManually.getText().toString().trim()
                    : "";
            if (!manual.isEmpty()) {
                addStopToItinerary(manual);
                dialog.dismiss();
            }
            return false;
        });

        dialog.show();
    }

    private void addStopToItinerary(String placeName) {
        if (placeName == null || placeName.isEmpty())
            return;
        itineraryStopCount++;
        View stopView = LayoutInflater.from(this).inflate(R.layout.item_self_planned_stop, layoutItineraryStops, false);
        TextView textStopTitle = stopView.findViewById(R.id.textStopTitle);
        EditText editNote = stopView.findViewById(R.id.editNote);
        textStopTitle.setText(itineraryStopCount + ". " + placeName);
        editNote.setHint(R.string.self_planned_add_note_hint);
        stopView.findViewById(R.id.buttonReorder).setOnClickListener(v -> {
        });
        stopView.findViewById(R.id.buttonEditNote).setOnClickListener(v -> editNote.requestFocus());
        stopView.findViewById(R.id.buttonDelete).setOnClickListener(v -> {
            layoutItineraryStops.removeView(stopView);
            itineraryStopCount--;
        });
        layoutItineraryStops.addView(stopView);
    }

    private void showAddExpenseDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_expense, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        View bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            bottomSheet.setBackgroundResource(android.R.color.transparent);
        }

        String[] categories = getResources().getStringArray(R.array.expense_categories);
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add(getString(R.string.add_expense_select_category));
        spinnerItems.addAll(Arrays.asList(categories));

        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);
        ImageView iconCategoryDropdown = view.findViewById(R.id.iconCategoryDropdown);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerCategory.setSelection(0);

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iconCategoryDropdown.setImageResource(position > 0 ? EXPENSE_ICONS[position - 1] : EXPENSE_ICONS[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        iconCategoryDropdown.setImageResource(EXPENSE_ICONS[0]);

        EditText editCategoryManual = view.findViewById(R.id.editCategoryManual);
        EditText editAmount = view.findViewById(R.id.editAmount);
        editAmount.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12), digitsOnlyFilter()});

        view.findViewById(R.id.buttonCloseAddExpense).setOnClickListener(v -> dialog.dismiss());
        view.findViewById(R.id.buttonCancelExpense).setOnClickListener(v -> dialog.dismiss());

        view.findViewById(R.id.buttonConfirmAddExpense).setOnClickListener(v -> {
            String manualCategory = editCategoryManual.getText() != null ? editCategoryManual.getText().toString().trim() : "";
            int pos = spinnerCategory.getSelectedItemPosition();
            String category = manualCategory.isEmpty()
                    ? (pos > 0 && pos <= categories.length ? categories[pos - 1] : "")
                    : manualCategory;
            String amountStr = editAmount.getText() != null ? editAmount.getText().toString().trim() : "";
            if (category.isEmpty()) {
                Toast.makeText(this, getString(R.string.add_expense_category), Toast.LENGTH_SHORT).show();
                return;
            }
            long amount = 0;
            try {
                amount = Long.parseLong(amountStr.replace(",", "").replace(" ", ""));
            } catch (NumberFormatException ignored) {
            }
            if (amount <= 0) {
                Toast.makeText(this, getString(R.string.add_expense_amount), Toast.LENGTH_SHORT).show();
                return;
            }
            addExpenseToBudget(category, amount, "");
            updateTotalEstimated();
            dialog.dismiss();
        });

        dialog.show();
    }

    private InputFilter digitsOnlyFilter() {
        return (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };
    }

    private void addExpenseToBudget(String category, long amountKzt, String note) {
        BudgetExpense entry = new BudgetExpense(category, amountKzt, note);
        budgetExpenses.add(entry);

        View row = LayoutInflater.from(this).inflate(R.layout.item_self_planned_expense, layoutBudgetItems, false);
        ImageView icon = row.findViewById(R.id.iconExpense);
        TextView label = row.findViewById(R.id.textExpenseLabel);
        TextView amountView = row.findViewById(R.id.textExpenseAmount);

        int iconIndex = getExpenseIconIndex(category);
        icon.setImageResource(EXPENSE_ICONS[iconIndex]);
        label.setText(category);
        amountView.setText(formatAmount(amountKzt));

        row.setTag(entry);
        row.setOnLongClickListener(v -> {
            budgetExpenses.remove(entry);
            layoutBudgetItems.removeView(row);
            updateTotalEstimated();
            return true;
        });
        layoutBudgetItems.addView(row);
    }

    private int getExpenseIconIndex(String categoryLabel) {
        String[] categories = getResources().getStringArray(R.array.expense_categories);
        for (int i = 0; i < categories.length; i++) {
            if (categoryLabel.equals(categories[i]))
                return i;
        }
        return EXPENSE_ICONS.length - 1;
    }

    private void updateTotalEstimated() {
        long total = 0;
        for (BudgetExpense e : budgetExpenses) {
            total += e.amountKzt;
        }
        textTotalEstimated.setText(formatAmount(total));
    }

    private static String formatAmount(long amount) {
        return String.format(Locale.US, "%,d ₸", amount);
    }

    private static class BudgetExpense {
        final String category;
        final long amountKzt;
        final String note;

        BudgetExpense(String category, long amountKzt, String note) {
            this.category = category;
            this.amountKzt = amountKzt;
            this.note = note != null ? note : "";
        }
    }
}
