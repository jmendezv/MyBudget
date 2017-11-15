package cristina.asensio.mybudget.activity;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cristina.asensio.mybudget.R;
import cristina.asensio.mybudget.model.Expense;
import cristina.asensio.mybudget.model.UtilDAOImpl;

public class StadisticsActivity extends AppCompatActivity {

    private static final int HOLE_RADIUS = 10;
    private static final int ROTATION_ANGLE = 0;
    private static final int SLICE_SPACE = 3;
    private static final int SELECTION_SHIFT = 5;
    private static final float VALUE_TEXT_SIZE = 11f;

    private ConstraintLayout stadisticsLayout;
    private UtilDAOImpl utilDAO;
    private PieChart chart;
    private List<Expense> currentMonthExpenses;
    private String[] xData = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stadistics);

        init();
        addPieChartToLayout();
        configurePieChart();
        configureHole();
        enableChartRotation();
        addDataToChart();
        handleChartOnTouch();
    }

    private void handleChartOnTouch() {
        this.chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry entry, int dataSetIndex, Highlight hightlight) {
                if (entry == null) {
                    return;
                }
                Toast.makeText(
                        StadisticsActivity.this,
                        xData[entry.getXIndex()] + " " + entry.getVal(),
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void addDataToChart() {
        final ArrayList<Entry> yValues = new ArrayList<>();
        float[] yData = {(float) getTotalExpenses(), getTotalAvailable()};

        for (int i = 0; i < yData.length; i++) {
            yValues.add(new Entry(yData[i], i));
        }

        final ArrayList<String> xValues = new ArrayList<>();
        this.xData[0] = getResources().getString(R.string.spent);
        this.xData[1] = getResources().getString(R.string.available);

        for (int i = 0; i < xData.length; i++) {
            xValues.add(xData[i]);
        }

        final PieDataSet dataSet = getPieDataSet(yValues);
        final PieData data = getPieData(xValues, dataSet);

        this.chart.setData(data);
        this.chart.highlightValues(null);
        this.chart.invalidate();
    }

    private PieData getPieData(ArrayList<String> xValues, PieDataSet dataSet) {
        final PieData data = new PieData(xValues, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(VALUE_TEXT_SIZE);
        data.setValueTextColor(Color.DKGRAY);
        return data;
    }

    private PieDataSet getPieDataSet(ArrayList<Entry> yValues) {
        final PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(SLICE_SPACE);
        dataSet.setSelectionShift(SELECTION_SHIFT);

        final ArrayList<Integer> colors = getColors();
        dataSet.setColors(colors);

        return dataSet;
    }

    private ArrayList<Integer> getColors() {
        final ArrayList<Integer> colors = new ArrayList<>();

        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        colors.add(ColorTemplate.getHoloBlue());

        return colors;
    }

    private float getTotalAvailable() {
        final String totalAvailable = String.valueOf(MainActivity.tvTotalAvailable.getText()).replace(" €", "");
        return Float.parseFloat(totalAvailable);
    }

    private float getTotalExpenses() {
        float totalExpenses = 0;

        for (Expense expense : this.currentMonthExpenses) {
            totalExpenses += expense.getQuantity();
        }
        return totalExpenses;
    }

    private void enableChartRotation() {
        this.chart.setRotationAngle(ROTATION_ANGLE);
        this.chart.setRotationEnabled(true);
    }

    private void configureHole() {
        this.chart.setDrawHoleEnabled(true);
        this.chart.setHoleColorTransparent(true);
        this.chart.setHoleRadius(HOLE_RADIUS);
        this.chart.setTransparentCircleRadius(HOLE_RADIUS);
    }

    private void configurePieChart() {
        this.chart.setUsePercentValues(true);
        this.chart.setDescription(getResources().getString(R.string.pie_chart_description));
    }

    private void addPieChartToLayout() {
        this.stadisticsLayout.addView(this.chart);
        this.stadisticsLayout.setBackgroundColor(Color.WHITE);
    }

    private void init() {
        this.stadisticsLayout = (ConstraintLayout) findViewById(R.id.stadisticsLayout);
        this.chart = new PieChart(this);

        try {
            this.utilDAO = new UtilDAOImpl(this);
            this.currentMonthExpenses = getCurrentMonthExpenses();
        } catch (SQLException | ParseException e) {
            Log.e(StadisticsActivity.class.getName(), e.toString());
        }
    }

    private List<Expense> getCurrentMonthExpenses() throws SQLException, ParseException {
        return this.utilDAO.lookupExpensesCurrentMonth();
    }
}
