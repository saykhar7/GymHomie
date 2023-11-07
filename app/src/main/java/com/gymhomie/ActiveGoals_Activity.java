package com.gymhomie;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.gymhomie.tools.Goal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActiveGoals_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_goals);

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        TextView tv = findViewById(R.id.textView);

        FrameLayout chartContainer = findViewById(R.id.chartContainer);
        Chart chart;


        // Retrieve the parameters sent from Goal_Activity
        String title = getIntent().getStringExtra("title");
        Bundle extras = getIntent().getExtras();
        String formattedDate = getFormattedDate();
        String start_date = "";
        String end_date = "";
        String target = "";
        String type = "";
        if(title.equalsIgnoreCase("steps")){
            chart = new PieChart(this);
            ViewGroup.LayoutParams layoutParams = chart.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(600, 600); // Set the desired width and height
            } else {
                layoutParams.width = 600; // Set the desired width in pixels
                layoutParams.height = 600; // Set the desired height in pixels
            }
            chart.setLayoutParams(layoutParams);

            // Set the top margin programmatically
            int marginTopInPixels = (int) getResources().getDimension(R.dimen.margin_16dp); // Convert 16dp to pixels
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(layoutParams);
            marginLayoutParams.topMargin = marginTopInPixels;
            chart.setLayoutParams(marginLayoutParams);
            ArrayList<PieEntry> entries = new ArrayList<>();
            String targ = extras.getString("3");
            String curr = extras.getString("4");
            entries.add(new PieEntry(Float.parseFloat(targ), "Target"));
            entries.add(new PieEntry(Float.parseFloat(curr), "Current"));
            PieDataSet dataSet = new PieDataSet(entries, formattedDate);
            dataSet.setColors(new int[]{Color.BLUE, Color.RED}); // Set colors for each segment
            dataSet.setSliceSpace(3f); // Space between slices
            dataSet.setSelectionShift(5f); // Distance of a selected slice from the center

            PieData data = new PieData(dataSet);
            chart.setData(data);
            Legend legend = chart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setDrawInside(false);
            legend.setForm(Legend.LegendForm.SQUARE);
            legend.setFormSize(12f);
            legend.setXEntrySpace(7f);
            legend.setYEntrySpace(0f);
            legend.setYOffset(0f);

            // Set an offset for the chart's center text
            ((PieChart) chart).setCenterTextOffset(0, -20);
            chart.getDescription().setEnabled(false);
            MPPointF centerText = MPPointF.getInstance();
            centerText.x = 0;
            centerText.y = 0;
            ((PieChart) chart).setCenterText("Daily Step\nProgress\n"+formattedDate);
            ((PieChart) chart).setCenterTextSize(16f);
            ((PieChart) chart).setCenterTextOffset(0f, -20f);
            ((PieChart) chart).setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            // Customize the chart
            ((PieChart) chart).setHoleRadius(60f); // Set the size of the hole (0-100)
            ((PieChart) chart).setTransparentCircleRadius(65f);
            chartContainer.addView(chart);
            int inner_count = 0;
            tv.setText("Step Goals");

            // Create a header row
            TableRow headerRow = new TableRow(this);

            // Create TextViews for each column in the header row
            TextView typeHeader = createHeaderTextView("Type");
            TextView targetStepsHeader = createHeaderTextView("Target");
            TextView currentStepsHeader = createHeaderTextView("Current");
            TextView startDateHeader = createHeaderTextView("Start Date");
            TextView endDateHeader = createHeaderTextView("End Date");

            // Add the TextViews to the header row
            headerRow.addView(typeHeader);
            headerRow.addView(targetStepsHeader);
            headerRow.addView(currentStepsHeader);
            headerRow.addView(startDateHeader);
            headerRow.addView(endDateHeader);

            // Add the header row to the table
            tableLayout.addView(headerRow);
            for (String key : extras.keySet()) {
                if (key.equalsIgnoreCase("title")) {
                    // TODO: nothing to do here
                } else {
                    int count = Integer.parseInt(key);
                    if (count == 0) {
                        // TODO: first goal entry
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    } else if (count % 4 == 0) {
                        // TODO: new goal entry
                        inner_count = 0;
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    } else {
                        // TODO: goal entry
                        if (inner_count == 1) {
                            end_date = getIntent().getStringExtra(String.valueOf(count));
                        } else if (inner_count == 2) {
                            type = getIntent().getStringExtra(String.valueOf(count));
                        } else {
                            TableRow row = new TableRow(this);
                            target = getIntent().getStringExtra(String.valueOf(count));
                            curr = getIntent().getStringExtra(String.valueOf(count+1));
//                            text = "Goal Type: " + type + ", Target Steps: " + target + ", Start Date: " + start_date + ", End Date: " + end_date;
//                            textView = new TextView(this);
                            // Create TextViews for each column in the header row
                            TextView _type_ = createHeaderTextView(type);
                            TextView targetSteps = createHeaderTextView(target);
                            TextView currSteps = createHeaderTextView(curr);
                            TextView startDate = createHeaderTextView(start_date);
                            TextView endDate= createHeaderTextView(end_date);

                            // Add the TextViews to the header row
                            row.addView(_type_);
                            row.addView(targetSteps);
                            row.addView(currSteps);
                            row.addView(startDate);
                            row.addView(endDate);

                            // Add the header row to the table
                            tableLayout.addView(row);
                        }
                        inner_count++;
                    }
                }
            }
        } else if (title.equalsIgnoreCase("hydration")) {
            chart = new PieChart(this);
            // Set the layout width and height programmatically
            ViewGroup.LayoutParams layoutParams = chart.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(600, 600); // Set the desired width and height
            } else {
                layoutParams.width = 600; // Set the desired width in pixels
                layoutParams.height = 600; // Set the desired height in pixels
            }
            chart.setLayoutParams(layoutParams);

            // Set the top margin programmatically
            int marginTopInPixels = (int) getResources().getDimension(R.dimen.margin_16dp); // Convert 16dp to pixels
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(layoutParams);
            marginLayoutParams.topMargin = marginTopInPixels;
            chart.setLayoutParams(marginLayoutParams);

            ArrayList<PieEntry> entries = new ArrayList<>();
            String targ = extras.getString("3");
            String curr = extras.getString("4");
            entries.add(new PieEntry(Float.parseFloat(targ), "Target"));
            entries.add(new PieEntry(Float.parseFloat(curr), "Current"));
            PieDataSet dataSet = new PieDataSet(entries, formattedDate);
            dataSet.setColors(new int[]{Color.BLUE, Color.RED}); // Set colors for each segment
            dataSet.setSliceSpace(3f); // Space between slices
            dataSet.setSelectionShift(5f); // Distance of a selected slice from the center

            PieData data = new PieData(dataSet);
            chart.setData(data);
            Legend legend = chart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setDrawInside(false);
            legend.setForm(Legend.LegendForm.SQUARE);
            legend.setFormSize(12f);
            legend.setXEntrySpace(7f);
            legend.setYEntrySpace(0f);
            legend.setYOffset(0f);

            // Set an offset for the chart's center text
            ((PieChart) chart).setCenterTextOffset(0, -20);
            chart.getDescription().setEnabled(false);
            MPPointF centerText = MPPointF.getInstance();
            centerText.x = 0;
            centerText.y = 0;
            ((PieChart) chart).setCenterText("Daily Hydration\nProgress\n"+formattedDate);
            ((PieChart) chart).setCenterTextSize(16f);
            ((PieChart) chart).setCenterTextOffset(0f, -20f);
            ((PieChart) chart).setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            // Customize the chart
            ((PieChart) chart).setHoleRadius(60f); // Set the size of the hole (0-100)
            ((PieChart) chart).setTransparentCircleRadius(65f);
            // Add the chart to the FrameLayout
            chartContainer.addView(chart);
            int inner_count = 0;
            tv.setText("Hydration Goals");

            // Create a header row
            TableRow headerRow = new TableRow(this);

            // Create TextViews for each column in the header row
            TextView typeHeader = createHeaderTextView("Type");
            TextView targetOuncesHeader = createHeaderTextView("Target");
            TextView currentOuncesHeader = createHeaderTextView("Current");
            TextView startDateHeader = createHeaderTextView("Start Date");
            TextView endDateHeader = createHeaderTextView("End Date");

            // Add the TextViews to the header row
            headerRow.addView(typeHeader);
            headerRow.addView(targetOuncesHeader);
            headerRow.addView(currentOuncesHeader);
            headerRow.addView(startDateHeader);
            headerRow.addView(endDateHeader);

            // Add the header row to the table
            tableLayout.addView(headerRow);
            for (String key : extras.keySet()){
                if(key.equalsIgnoreCase("title")){
                    // TODO: nothing to do here
                } else {
                    int count = Integer.parseInt(key);
                    if(count == 0){
                        // TODO: first goal entry
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else if(count % 4 == 0){
                        // TODO: new goal entry
                        inner_count = 0;
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else{
                        // TODO: goal entry
                        if(inner_count == 1){
                            end_date = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else if(inner_count == 2){
                            type = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else {
                            target = getIntent().getStringExtra(String.valueOf(count));
                            curr = getIntent().getStringExtra(String.valueOf(count+1));
                            //text = "Goal Type: " + type + ", Target Ounces: " + target + ", Start Date: " + start_date + ", End Date: " + end_date;
                            TableRow row = new TableRow(this);
                            TextView _type_ = createHeaderTextView(type);
                            TextView targetOunces = createHeaderTextView(target);
                            TextView currentOunces = createHeaderTextView(curr);
                            TextView startDate = createHeaderTextView(start_date);
                            TextView endDate= createHeaderTextView(end_date);

                            // Add the TextViews to the header row
                            row.addView(_type_);
                            row.addView(targetOunces);
                            row.addView(currentOunces);
                            row.addView(startDate);
                            row.addView(endDate);

                            // Add the header row to the table
                            tableLayout.addView(row);
                        }
                        inner_count++;
                    }
                }
            }
        } else if (title.equalsIgnoreCase("weight")) {
            tv.setText("Weight Goals");
            chart = new HorizontalBarChart(this);
            String targ = extras.getString("3");
            String curr = extras.getString("2");

            // Customize the chart settings
            chart.getDescription().setEnabled(false);
            // Set target value (change as needed)
            float currentValue = Float.parseFloat(curr);

            ((HorizontalBarChart)chart).getAxisRight().setEnabled(true);

            // Calculate the Y-axis range to cover a range of plus/minus 20.0 of the current value
            float yAxisMin = currentValue - 5.0f;
            float yAxisMax = currentValue + 5.0f;

            // Set the Y-axis minimum and maximum values
            ((HorizontalBarChart)chart).getAxisRight().setAxisMinimum(yAxisMin);
            ((HorizontalBarChart)chart).getAxisRight().setAxisMaximum(yAxisMax);

            // Set the maximum Y-axis value to ensure consistency
            //((HorizontalBarChart)chart).getAxisRight().setAxisMaximum(targetValue);

            ((HorizontalBarChart)chart).getXAxis().setEnabled(false);
            ((HorizontalBarChart)chart).getLegend().setEnabled(false);

            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0, Float.parseFloat(curr))); // First entry

            // Create data for the chart
            BarDataSet dataSet = new BarDataSet(entries, "Progress");
            dataSet.setColors(Color.GREEN);

            // Create data for the chart
            BarData data = new BarData(dataSet);
            ((HorizontalBarChart) chart).setData(data);

            // Customize legend
            Legend legend = chart.getLegend();
            legend.setEnabled(false);
            chartContainer.addView(chart);
            // Create a header row
            TableRow headerRow = new TableRow(this);

            // Create TextViews for each column in the header row
            TextView targetWeightHeader = createHeaderTextView("Target");
            TextView currentWeightHeader = createHeaderTextView("Current");
            TextView startDateHeader = createHeaderTextView("Start Date");
            TextView endDateHeader = createHeaderTextView("End Date");

            // Add the TextViews to the header row
            headerRow.addView(targetWeightHeader);
            headerRow.addView(currentWeightHeader);
            headerRow.addView(startDateHeader);
            headerRow.addView(endDateHeader);

            // Add the header row to the table
            tableLayout.addView(headerRow);
            int inner_count = 0;
            for (String key : extras.keySet()){
                if(key.equalsIgnoreCase("title")){
                    // TODO: nothing to do here
                } else {
                    int count = Integer.parseInt(key);
                    if(count == 0){
                        // TODO: first goal entry
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else if(count % 3 == 0){
                        // TODO: new goal entry
                        inner_count = 0;
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else{
                        // TODO: goal entry
                        if(inner_count == 1){
                            end_date = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else {
                            target = getIntent().getStringExtra(String.valueOf(count+1));
                            //text = "Target Weight: " + target + ", Start Date: " + start_date + ", End Date: " + end_date;
                            TableRow row = new TableRow(this);
                            TextView targetWeight = createHeaderTextView(target);
                            TextView currentWeight = createHeaderTextView(curr);
                            TextView startDate = createHeaderTextView(start_date);
                            TextView endDate= createHeaderTextView(end_date);

                            // Add the TextViews to the header row
                            row.addView(targetWeight);
                            row.addView(currentWeight);
                            row.addView(startDate);
                            row.addView(endDate);

                            // Add the header row to the table
                            tableLayout.addView(row);
                        }
                        inner_count++;
                    }
                }
            }
        } else if (title.equalsIgnoreCase("exercise")) {
            String targ = extras.getString("3");
            String curr = extras.getString("4");
            //chartContainer.addView(chart);
            tv.setText("Exercise Goals");
            chart = new HorizontalBarChart(this);

            // Customize the chart settings
            chart.getDescription().setEnabled(false);
            // Set target value (change as needed)
            float currentValue = Float.parseFloat(curr);

            ((HorizontalBarChart)chart).getAxisRight().setEnabled(true);

            // Calculate the Y-axis range to cover a range of plus/minus 20.0 of the current value
            float yAxisMin = currentValue - 50.0f;
            float yAxisMax = currentValue + 50.0f;

            // Set the Y-axis minimum and maximum values
            ((HorizontalBarChart)chart).getAxisRight().setAxisMinimum(yAxisMin);
            ((HorizontalBarChart)chart).getAxisRight().setAxisMaximum(yAxisMax);

            // Set the maximum Y-axis value to ensure consistency
            //((HorizontalBarChart)chart).getAxisRight().setAxisMaximum(targetValue);

            ((HorizontalBarChart)chart).getXAxis().setEnabled(false);
            ((HorizontalBarChart)chart).getLegend().setEnabled(false);

            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.add(new BarEntry(0, Float.parseFloat(curr))); // First entry

            // Create data for the chart
            BarDataSet dataSet = new BarDataSet(entries, "Progress");
            dataSet.setColors(Color.GREEN);

            // Create data for the chart
            BarData data = new BarData(dataSet);
            ((HorizontalBarChart) chart).setData(data);

            // Customize legend
            Legend legend = chart.getLegend();
            legend.setEnabled(false);
            chartContainer.addView(chart);

            // Create a header row
            TableRow headerRow = new TableRow(this);

            // Create TextViews for each column in the header row
            TextView typeHeader = createHeaderTextView("Exercise");
            TextView targetMaxHeader = createHeaderTextView("Target");
            TextView currentMaxHeader = createHeaderTextView("Current");
            TextView startDateHeader = createHeaderTextView("Start Date");
            TextView endDateHeader = createHeaderTextView("End Date");

            // Add the TextViews to the header row
            headerRow.addView(typeHeader);
            headerRow.addView(targetMaxHeader);
            headerRow.addView(currentMaxHeader);
            headerRow.addView(startDateHeader);
            headerRow.addView(endDateHeader);

            // Add the header row to the table
            tableLayout.addView(headerRow);
            int inner_count = 0;
            for (String key : extras.keySet()){
                if(key.equalsIgnoreCase("title")){
                    // TODO: nothing to do here
                } else {
                    int count = Integer.parseInt(key);
                    if(count == 0){
                        // TODO: first goal entry
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else if(count % 4 == 0){
                        // TODO: new goal entry
                        inner_count = 0;
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else{
                        // TODO: goal entry
                        if(inner_count == 1){
                            end_date = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else if(inner_count == 2){
                            type = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else {
                            target = getIntent().getStringExtra(String.valueOf(count));
                            curr = getIntent().getStringExtra(String.valueOf(count+1));
                            //text = "Exercise Type: " + type + ", Target: " + target + ", Start Date: " + start_date + ", End Date: " + end_date;
                            TableRow row = new TableRow(this);
                            TextView _type_ = createHeaderTextView(type);
                            TextView max_target = createHeaderTextView(target);
                            TextView curr_target = createHeaderTextView(curr);
                            TextView startDate = createHeaderTextView(start_date);
                            TextView endDate= createHeaderTextView(end_date);

                            // Add the TextViews to the header row
                            row.addView(_type_);
                            row.addView(max_target);
                            row.addView(curr_target);
                            row.addView(startDate);
                            row.addView(endDate);

                            // Add the header row to the table
                            tableLayout.addView(row);
                        }
                        inner_count++;
                    }
                }
            }
        } else if (title.equalsIgnoreCase("workout")) {
            ArrayList<Boolean> curr = (ArrayList<Boolean>) extras.get("4");
            ArrayList<Integer> intList = new ArrayList<>();

            for (boolean boolValue : curr) {
                int intValue = boolValue ? 1 : 0;
                intList.add(intValue);
            }
            chart = new LineChart(this);
            tv.setText("Workout Goals");
            // Create a list of data points for the last seven days
            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry(6, intList.get(0)));
            entries.add(new Entry(5, 1));
            entries.add(new Entry(4, 1));
            entries.add(new Entry(3, 1));
            entries.add(new Entry(2, 0));
            entries.add(new Entry(1, 1));
            entries.add(new Entry(0, 1));

            // Create a list of dates for the X-axis labels
            ArrayList<String> labels = new ArrayList<String>();
            labels = getLastSevenDates();
            LineDataSet dataSet = new LineDataSet(entries, "Yes/No Data");
            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            // Customize the chart settings
            chart.getDescription().setEnabled(false);
            ((LineChart) chart).getAxisLeft().setEnabled(false);
            ArrayList<String> finalLabels = labels;
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    int index = (int) value;
                    if (index >= 0 && index < finalLabels.size()) {
                        return finalLabels.get(index);
                    }
                    return "";
                }
            });
            xAxis.setGranularity(1f);
            ((LineChart)chart).getAxisRight().setEnabled(false);

            ((LineChart)chart).getXAxis().setEnabled(true);
            ((LineChart)chart).getLegend().setEnabled(false);

            // Create data for the chart
            LineData data = new LineData(dataSet);
            ((LineChart) chart).setData(data);
            // Customize legend
            Legend legend = chart.getLegend();
            legend.setEnabled(false);
            chartContainer.addView(chart);
            // Create a header row
            TableRow headerRow = new TableRow(this);

            // Create TextViews for each column in the header row
            TextView typeHeader = createHeaderTextView("Type");
            TextView targetStepsHeader = createHeaderTextView("Target");
            TextView startDateHeader = createHeaderTextView("Start Date");
            TextView endDateHeader = createHeaderTextView("End Date");

            // Add the TextViews to the header row
            headerRow.addView(typeHeader);
            headerRow.addView(targetStepsHeader);
            headerRow.addView(startDateHeader);
            headerRow.addView(endDateHeader);

            // Add the header row to the table
            tableLayout.addView(headerRow);
            int inner_count = 0;
            for (String key : extras.keySet()){
                if(key.equalsIgnoreCase("title")){
                    // TODO: nothing to do here
                } else {
                    int count = Integer.parseInt(key);
                    if(count == 0){
                        // TODO: first goal entry
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else if(count % 5 == 0){
                        // TODO: new goal entry
                        inner_count = 0;
                        start_date = getIntent().getStringExtra(String.valueOf(count));
                        inner_count++;
                    }
                    else{
                        // TODO: goal entry
                        if(inner_count == 1){
                            end_date = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else if(inner_count == 2){
                            type = getIntent().getStringExtra(String.valueOf(count));
                        }
                        else {
                            target = getIntent().getStringExtra(String.valueOf(count));
                            if(type.equalsIgnoreCase("daily")) {
                                TableRow row = new TableRow(this);
                                TextView _type_ = createHeaderTextView(type);
                                TextView target_streak = createHeaderTextView(target + "d");
                                TextView startDate = createHeaderTextView(start_date);
                                TextView endDate= createHeaderTextView(end_date);

                                // Add the TextViews to the header row
                                row.addView(_type_);
                                row.addView(target_streak);
                                row.addView(startDate);
                                row.addView(endDate);

                                // Add the header row to the table
                                tableLayout.addView(row);
                            }
                            else{
                                TableRow row = new TableRow(this);
                                TextView _type_ = createHeaderTextView(type);
                                TextView target_streak = createHeaderTextView(target + "d");
                                TextView startDate = createHeaderTextView(start_date);
                                TextView endDate= createHeaderTextView(end_date);

                                // Add the TextViews to the header row
                                row.addView(_type_);
                                row.addView(target_streak);
                                row.addView(startDate);
                                row.addView(endDate);

                                // Add the header row to the table
                                tableLayout.addView(row);
                            }
                        }
                        inner_count++;
                    }
                }
            }
        }
    }
    public ArrayList<String> getLastSevenDates() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Create a SimpleDateFormat to format the dates
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMdd", Locale.US);

        // Create a list to store the dates
        ArrayList<String> dateList = new ArrayList<>();

        // Add the current date to the list
        dateList.add(dateFormat.format(currentDate));

        // Calculate the dates of the last seven days
        for (int i = 1; i < 7; i++) {
            calendar.add(Calendar.DATE, -1); // Subtract one day
            Date previousDate = calendar.getTime();
            dateList.add(dateFormat.format(previousDate));
        }
        return dateList;
    }
    public static String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
    // Helper method to create a header TextView
    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(16, 8, 16, 8); // Adjust padding as needed
        //textView.setBackgroundResource(R.color.header_background); // Add a background color
        // Customize other attributes as needed
        return textView;
    }

    // Helper method to create a data TextView
    private TextView createDataTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(16, 8, 16, 8); // Adjust padding as needed
        // Customize other attributes as needed
        return textView;
    }
}
