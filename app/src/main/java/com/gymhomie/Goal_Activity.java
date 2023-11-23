package com.gymhomie;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.gymhomie.tools.Goal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;


public class Goal_Activity extends AppCompatActivity {

    private ImageView artImageView;
    private TextView title;
    private int currentImageIndex = 0;
    DatePicker startDatePicker;
    DatePicker endDatePicker;
    EditText target;
    Spinner type;
    private final int[] imageResources = {
            R.drawable.jogging,
            R.drawable.water_bottle,
            R.drawable.body_scan,
            R.drawable.barbell,
            R.drawable.health
    };
    private final String[] titleResources = {
            "Step Goals",
            "Hydration Goals",
            "Weight Goals",
            "Exercise Goals",
            "Workout Goals"
    };
    Goal goal = new Goal();

    private final boolean[] hasGoals = new boolean[imageResources.length];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        artImageView = findViewById(R.id.artImageView);
        title = findViewById(R.id.titleAboveImageView);
        //summary = findViewById(R.id.summaryTextView);

        updateArtImageView();
        updateTitle();
        //updateSummary();
        Button nextButton = findViewById(R.id.nextButton);
        Button previousButton = findViewById(R.id.previousButton);
        Button activeGoalButton = findViewById(R.id.activeGoalsButton);
        Button goalHistoryButton = findViewById(R.id.goalHistoryButton);

        activeGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Goal_Activity.this, ActiveGoals_Activity.class);
                if(currentImageIndex == 0) {
                    String title = "steps";
                    Stack<String> start_dates = new Stack<>();
                    Stack<String> end_dates = new Stack<>();
                    Stack<String> types = new Stack<>();
                    Stack<String> targets = new Stack<>();
                    Stack<String> currents = new Stack<>();
                    if(goal.getGoalCollection().get(0)){
                        // start active goals intent
                        setupInfo(start_dates, end_dates, types, targets, currents, title);
                    } else if (!goal.getGoalCollection().get(0)) {
                        // TODO: Setup no goals screen
                    }
                    setContentView(R.layout.activity_active_goals);
                    implementUI_DonutChart(start_dates, end_dates, types, targets, currents, title);
                    Button addGoalButton = findViewById(R.id.addGoal);
                    addGoalButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Create a ScrollView to make the layout scrollable
                            ScrollView scrollView = new ScrollView(Goal_Activity.this);
                            scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));

                            // Create a vertical LinearLayout with light gray background
                            LinearLayout linearLayout = new LinearLayout(Goal_Activity.this);
                            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT));
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setGravity(Gravity.CENTER);
                            linearLayout.setBackgroundColor(Color.parseColor("#C0C0C0")); // Light gray background
                            createAddGoalLayout(scrollView, linearLayout, currentImageIndex);
                            // Create a Button with a larger font
                            Button button = new Button(Goal_Activity.this);
                            button.setText("Submit");
                            button.setTextSize(16); // Larger font size for the button
                            linearLayout.addView(button);
                            // Set a click listener for the button
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                    FirebaseAuth auth = FirebaseAuth.getInstance();
                                    String user_id = auth.getUid();
                                    CollectionReference goalCollection = FirebaseFirestore.getInstance().collection("users/" + user_id + "/Goal");
                                    // Create a Map to store the data
                                    Map<String, Object> goalData = setupGoalData(currentImageIndex);
                                    // Add the new document to the Firestore collection
                                    goalCollection.add(goalData)
                                            .addOnSuccessListener(documentReference -> {
                                                // Document added successfully
                                                Toast.makeText(Goal_Activity.this, "Goal added successfully", Toast.LENGTH_SHORT).show();
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle errors
                                                Toast.makeText(Goal_Activity.this, "Error adding goal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                }
                            });

                            // Set the LinearLayout as the content view of the activity
                            scrollView.addView(linearLayout);
                            setContentView(scrollView);
                        }
                    });
                }else if(currentImageIndex == 1){
                    if(goal.getGoalCollection().get(1)){
                        // start active goals intent
                        String title = "hydration";
                        Stack<String> start_dates = new Stack<>();
                        Stack<String> end_dates = new Stack<>();
                        Stack<String> types = new Stack<>();
                        Stack<String> targets = new Stack<>();
                        Stack<String> currents = new Stack<>();
                        if(goal.getGoalCollection().get(1)){
                            // start active goals intent
                            setupInfo(start_dates, end_dates, types, targets, currents, title);
                        } else if (!goal.getGoalCollection().get(1)) {
                            // TODO: Setup no goals screen
                        }
                        setContentView(R.layout.activity_active_goals);
                        implementUI_DonutChart(start_dates, end_dates, types, targets, currents, title);
                        Button addGoalButton = findViewById(R.id.addGoal);
                        addGoalButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Create a ScrollView to make the layout scrollable
                                ScrollView scrollView = new ScrollView(Goal_Activity.this);
                                scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));

                                // Create a vertical LinearLayout with light gray background
                                LinearLayout linearLayout = new LinearLayout(Goal_Activity.this);
                                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                linearLayout.setGravity(Gravity.CENTER);
                                linearLayout.setBackgroundColor(Color.parseColor("#C0C0C0")); // Light gray background
                                final DatePicker startDatePicker = null;
                                final DatePicker endDatePicker = null;
                                final EditText target = null;
                                final Spinner type = null;
                                createAddGoalLayout(scrollView, linearLayout, currentImageIndex);
                                // Create a Button with a larger font
                                Button button = new Button(Goal_Activity.this);
                                button.setText("Submit");
                                button.setTextSize(16); // Larger font size for the button
                                linearLayout.addView(button);

                                // Set a click listener for the button
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        String user_id = auth.getUid();
                                        CollectionReference goalCollection = FirebaseFirestore.getInstance().collection("users/" + user_id + "/Goal");
                                        // Create a Map to store the data
                                        Map<String, Object> goalData = setupGoalData(currentImageIndex);

                                        // Add the new document to the Firestore collection
                                        goalCollection.add(goalData)
                                                .addOnSuccessListener(documentReference -> {
                                                    // Document added successfully
                                                    Toast.makeText(Goal_Activity.this, "Goal added successfully", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle errors
                                                    Toast.makeText(Goal_Activity.this, "Error adding goal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });

                                // Set the LinearLayout as the content view of the activity
                                scrollView.addView(linearLayout);
                                setContentView(scrollView);
                            }
                        });
                    } else if (!goal.getGoalCollection().get(1)) {
                        // TODO: Setup no goals screen
                    }
                } else if(currentImageIndex == 2){
                    if(goal.getGoalCollection().get(2)){
                        // start active goals intent
                        String title = "weight";
                        Stack<String> start_dates = new Stack<>();
                        Stack<String> end_dates = new Stack<>();
                        Stack<String> types = new Stack<>();
                        Stack<String> targets = new Stack<>();
                        Stack<String> currents = new Stack<>();
                        if(goal.getGoalCollection().get(2)){
                            // start active goals intent
                            setupInfo(start_dates, end_dates, types, targets, currents, title);
                        } else if (!goal.getGoalCollection().get(2)) {
                            // TODO: Setup no goals screen
                        }
                        setContentView(R.layout.activity_active_goals);
                        implementUI_HBarChart(start_dates, end_dates, types, targets, currents, title);
                        Button addGoalButton = findViewById(R.id.addGoal);
                        addGoalButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Create a ScrollView to make the layout scrollable
                                ScrollView scrollView = new ScrollView(Goal_Activity.this);
                                scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));

                                // Create a vertical LinearLayout with light gray background
                                LinearLayout linearLayout = new LinearLayout(Goal_Activity.this);
                                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                linearLayout.setGravity(Gravity.CENTER);
                                linearLayout.setBackgroundColor(Color.parseColor("#C0C0C0")); // Light gray background
                                final DatePicker startDatePicker = null;
                                final DatePicker endDatePicker = null;
                                final EditText target = null;
                                final Spinner type = null;
                                createAddGoalLayout(scrollView, linearLayout, currentImageIndex);
                                // Create a Button with a larger font
                                Button button = new Button(Goal_Activity.this);
                                button.setText("Submit");
                                button.setTextSize(16); // Larger font size for the button
                                linearLayout.addView(button);

                                // Set a click listener for the button
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        String user_id = auth.getUid();
                                        CollectionReference goalCollection = FirebaseFirestore.getInstance().collection("users/" + user_id + "/Goal");
                                        // Create a Map to store the data
                                        Map<String, Object> goalData = setupGoalData(currentImageIndex);

                                        // Add the new document to the Firestore collection
                                        goalCollection.add(goalData)
                                                .addOnSuccessListener(documentReference -> {
                                                    // Document added successfully
                                                    Toast.makeText(Goal_Activity.this, "Goal added successfully", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle errors
                                                    Toast.makeText(Goal_Activity.this, "Error adding goal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });

                                // Set the LinearLayout as the content view of the activity
                                scrollView.addView(linearLayout);
                                setContentView(scrollView);
                            }
                        });
                    } else if (!goal.getGoalCollection().get(2)) {
                        // TODO: Setup no goals screen
                    }
                } else if(currentImageIndex == 3){
                    if(goal.getGoalCollection().get(3)){
                        // start active goals intent
                        String title = "exercise";
                        Stack<String> start_dates = new Stack<>();
                        Stack<String> end_dates = new Stack<>();
                        Stack<String> types = new Stack<>();
                        Stack<String> targets = new Stack<>();
                        Stack<String> currents = new Stack<>();
                        if(goal.getGoalCollection().get(3)){
                            // start active goals intent
                            setupInfo(start_dates, end_dates, types, targets, currents, title);
                        } else if (!goal.getGoalCollection().get(3)) {
                            // TODO: Setup no goals screen
                        }
                        setContentView(R.layout.activity_active_goals);
                        implementUI_HBarChart(start_dates, end_dates, types, targets, currents, title);
                        Button addGoalButton = findViewById(R.id.addGoal);
                        addGoalButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Create a ScrollView to make the layout scrollable
                                ScrollView scrollView = new ScrollView(Goal_Activity.this);
                                scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));

                                // Create a vertical LinearLayout with light gray background
                                LinearLayout linearLayout = new LinearLayout(Goal_Activity.this);
                                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                linearLayout.setGravity(Gravity.CENTER);
                                linearLayout.setBackgroundColor(Color.parseColor("#C0C0C0")); // Light gray background
                                final DatePicker startDatePicker = null;
                                final DatePicker endDatePicker = null;
                                final EditText target = null;
                                final Spinner type = null;
                                createAddGoalLayout(scrollView, linearLayout, currentImageIndex);
                                // Create a Button with a larger font
                                Button button = new Button(Goal_Activity.this);
                                button.setText("Submit");
                                button.setTextSize(16); // Larger font size for the button
                                linearLayout.addView(button);

                                // Set a click listener for the button
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        String user_id = auth.getUid();
                                        CollectionReference goalCollection = FirebaseFirestore.getInstance().collection("users/" + user_id + "/Goal");
                                        // Create a Map to store the data
                                        Map<String, Object> goalData = setupGoalData(currentImageIndex);

                                        // Add the new document to the Firestore collection
                                        goalCollection.add(goalData)
                                                .addOnSuccessListener(documentReference -> {
                                                    // Document added successfully
                                                    Toast.makeText(Goal_Activity.this, "Goal added successfully", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle errors
                                                    Toast.makeText(Goal_Activity.this, "Error adding goal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });

                                // Set the LinearLayout as the content view of the activity
                                scrollView.addView(linearLayout);
                                setContentView(scrollView);
                            }
                        });
                    } else if (!goal.getGoalCollection().get(3)) {
                        // TODO: Setup no goals screen
                    }
                } else if(currentImageIndex == 4){
                    if(goal.getGoalCollection().get(4)){
                        // start active goals intent
                        String title = "workout";
                        Stack<String> start_dates = new Stack<>();
                        Stack<String> end_dates = new Stack<>();
                        Stack<String> types = new Stack<>();
                        Stack<String> targets = new Stack<>();
                        Stack<String> current = new Stack<>();
                        Stack<ArrayList<Boolean>> currents = new Stack<>();
                        if(goal.getGoalCollection().get(4)){
                            // start active goals intent
                            setupInfo_Workout(start_dates, end_dates, types, targets, currents, title);
                        } else if (!goal.getGoalCollection().get(4)) {
                            // TODO: Setup no goals screen
                        }
                        setContentView(R.layout.activity_active_goals);
                        implementUI_LineChart(start_dates, end_dates, types, targets, current);
                        Button addGoalButton = findViewById(R.id.addGoal);
                        addGoalButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Create a ScrollView to make the layout scrollable
                                ScrollView scrollView = new ScrollView(Goal_Activity.this);
                                scrollView.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));

                                // Create a vertical LinearLayout with light gray background
                                LinearLayout linearLayout = new LinearLayout(Goal_Activity.this);
                                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));
                                linearLayout.setOrientation(LinearLayout.VERTICAL);
                                linearLayout.setGravity(Gravity.CENTER);
                                linearLayout.setBackgroundColor(Color.parseColor("#C0C0C0")); // Light gray background

                                createAddGoalLayout(scrollView, linearLayout, currentImageIndex);
                                // Create a Button with a larger font
                                Button button = new Button(Goal_Activity.this);
                                button.setText("Submit");
                                button.setTextSize(16); // Larger font size for the button
                                linearLayout.addView(button);

                                // Set a click listener for the button
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        String user_id = auth.getUid();
                                        CollectionReference goalCollection = FirebaseFirestore.getInstance().collection("users/" + user_id + "/Goal");
                                        // Create a Map to store the data
                                        Map<String, Object> goalData = setupGoalData(currentImageIndex);

                                        // Add the new document to the Firestore collection
                                        goalCollection.add(goalData)
                                                .addOnSuccessListener(documentReference -> {
                                                    // Document added successfully
                                                    Toast.makeText(Goal_Activity.this, "Goal added successfully", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Handle errors
                                                    Toast.makeText(Goal_Activity.this, "Error adding goal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });

                                // Set the LinearLayout as the content view of the activity
                                scrollView.addView(linearLayout);
                                setContentView(scrollView);
                            }
                        });
                    } else if (!goal.getGoalCollection().get(4)) {
                        // TODO: Setup no goals screen
                    }
                }
                //startActivity(intent);
            }
        });

        goalHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentImageIndex == 0) {
                    if(goal.getGoalCollection().get(0)){
                        // start active goals intent
                        String title = "steps";
                        Stack<String> start_dates = new Stack<>();
                        Stack<String> end_dates = new Stack<>();
                        Stack<String> types = new Stack<>();
                        Stack<String> targets = new Stack<>();
                        Stack<String> currents = new Stack<>();
                        if(goal.getGoalCollection().get(0)){
                            // start active goals intent
                            setupInfoHistory(start_dates, end_dates, types, targets, currents, title);
                        } else if (!goal.getGoalCollection().get(0)) {
                            // TODO: Setup no goals screen
                        }
                        setContentView(R.layout.activity_goal_history);
                        setupGoalHistoryUI(start_dates, end_dates, types, targets, currents, title);
                    } else if (!goal.getGoalCollection().get(0)) {
                        // TODO: Setup no goals screen

                    }
                }else if(currentImageIndex == 1){
                    // start active goals intent
                    String title = "hydration";
                    Stack<String> start_dates = new Stack<>();
                    Stack<String> end_dates = new Stack<>();
                    Stack<String> types = new Stack<>();
                    Stack<String> targets = new Stack<>();
                    Stack<String> currents = new Stack<>();
                    if(goal.getGoalCollection().get(0)){
                        // start active goals intent
                        setupInfoHistory(start_dates, end_dates, types, targets, currents, title);
                    } else if (!goal.getGoalCollection().get(0)) {
                        // TODO: Setup no goals screen
                    }
                    setContentView(R.layout.activity_goal_history);
                    setupGoalHistoryUI(start_dates, end_dates, types, targets, currents, title);
                } else if(currentImageIndex == 2){
                    if(goal.getGoalCollection().get(2)){
                        // start active goals intent
                        String title = "weight";
                        Stack<String> start_dates = new Stack<>();
                        Stack<String> end_dates = new Stack<>();
                        Stack<String> types = new Stack<>();
                        Stack<String> targets = new Stack<>();
                        Stack<String> currents = new Stack<>();
                        if(goal.getGoalCollection().get(2)){
                            // start active goals intent
                            setupInfoHistory(start_dates, end_dates, types, targets, currents, title);
                        } else if (!goal.getGoalCollection().get(2)) {
                            // TODO: Setup no goals screen
                        }
                        setContentView(R.layout.activity_goal_history);
                        setupGoalHistoryUI(start_dates, end_dates, types, targets, currents, title);
                }} else if(currentImageIndex == 3){
                    if(goal.getGoalCollection().get(3)) {
                        // start active goals intent
                        String title = "hydration";
                        Stack<String> start_dates = new Stack<>();
                        Stack<String> end_dates = new Stack<>();
                        Stack<String> types = new Stack<>();
                        Stack<String> targets = new Stack<>();
                        Stack<String> currents = new Stack<>();
                        if (goal.getGoalCollection().get(0)) {
                            // start active goals intent
                            setupInfoHistory(start_dates, end_dates, types, targets, currents, title);
                        } else if (!goal.getGoalCollection().get(0)) {
                            // TODO: Setup no goals screen
                        }
                        setContentView(R.layout.activity_goal_history);
                        setupGoalHistoryUI(start_dates, end_dates, types, targets, currents, title);
                    }                } else if(currentImageIndex == 4){
                    if(goal.getGoalCollection().get(4)) {
                        // start active goals intent
                        String title = "workout";
                        Stack<String> start_dates = new Stack<>();
                        Stack<String> end_dates = new Stack<>();
                        Stack<String> types = new Stack<>();
                        Stack<String> targets = new Stack<>();
                        Stack<String> currents = new Stack<>();
                        if (goal.getGoalCollection().get(4)) {
                            // start active goals intent
                            setupInfoHistory(start_dates, end_dates, types, targets, currents, title);
                        } else if (!goal.getGoalCollection().get(4)) {
                            // TODO: Setup no goals screen
                        }
                        setContentView(R.layout.activity_goal_history);
                        setupGoalHistoryUI(start_dates, end_dates, types, targets, currents, title);
                    }                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex > 0) {
                    // index 0 is steps: target steps, goal type
                    // index 1 is hydration: target ounces, goal type
                    // index 2 is weight: target weight
                    // index 3 is exercise: target weight
                    // index 4 is workouts: goal type, daily/weekly
                    currentImageIndex--;
                    updateArtImageView();
                    updateTitle();
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex < imageResources.length - 1) {
                    // index 0 is steps: target steps, goal type
                    // index 1 is hydration: target ounces, goal type
                    // index 2 is weight: target weight
                    // index 3 is exercise: target weight
                    // index 4 is workouts: goal type, daily/weekly
                    currentImageIndex++;
                    updateArtImageView();
                    updateTitle();
                }
            }
        });
    }
    public String formatTitle(String title){
        if (title.equalsIgnoreCase("steps")){
            title = "Steps";
        }
        else if (title.equalsIgnoreCase("hydration")) {
            title = "Hydration";
        }
        else if (title.equalsIgnoreCase("weight")) {
            title = "Weight";
        }
        else if (title.equalsIgnoreCase("exercise")) {
            title = "Exercise";
        }
        else if (title.equalsIgnoreCase("workout")) {
            title = "Workout";
        }
        return title;
    }
    public void implementUI_DonutChart(Stack<String> startDates, Stack<String> endDates, Stack<String> types, Stack<String> targets, Stack<String> currents, String title) {
        title = formatTitle(title);
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        TextView tv = findViewById(R.id.textView);

        FrameLayout chartContainer = findViewById(R.id.chartContainer);
        if(title.equalsIgnoreCase("Steps") || title.equalsIgnoreCase("Hydration")) {
            Chart chart = new PieChart(Goal_Activity.this);
            ViewGroup.LayoutParams layoutParams = chart.getLayoutParams();
            if (title.equalsIgnoreCase("Steps")) {
                if (layoutParams == null) {
                    layoutParams = new ViewGroup.LayoutParams(900, 900); // Set the desired width and height
                } else {
                    layoutParams.width = 900; // Set the desired width in pixels
                    layoutParams.height = 900; // Set the desired height in pixels
                }
            } else if (title.equalsIgnoreCase("Hydration")) {
                if (layoutParams == null) {
                    layoutParams = new ViewGroup.LayoutParams(950, 950); // Set the desired width and height
                } else {
                    layoutParams.width = 950; // Set the desired width in pixels
                    layoutParams.height = 950; // Set the desired height in pixels
                }
            }
            chart.setLayoutParams(layoutParams);

            // Set the top margin programmatically
            int marginTopInPixels = (int) getResources().getDimension(R.dimen.margin_16dp); // Convert 16dp to pixels
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(layoutParams);
            marginLayoutParams.topMargin = marginTopInPixels;
            chart.setLayoutParams(marginLayoutParams);
            ArrayList<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(Float.parseFloat(targets.get(0)), "Target"));
            entries.add(new PieEntry(Float.parseFloat(currents.get(0)), "Current"));
            String formattedDate = getFormattedDate();
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
            ((PieChart) chart).setCenterText("Daily " + title + "\nProgress\n" + formattedDate);
            ((PieChart) chart).setCenterTextSize(16f);
            ((PieChart) chart).setCenterTextOffset(0f, -20f);
            ((PieChart) chart).setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            // Customize the chart
            ((PieChart) chart).setHoleRadius(60f); // Set the size of the hole (0-100)
            ((PieChart) chart).setTransparentCircleRadius(65f);
            chartContainer.addView(chart);
        }
        int inner_count = 0;
        tv.setText(title + " Goals");

        // Create a header row
        TableRow headerRow = new TableRow(Goal_Activity.this);

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

        while (!types.isEmpty()) {
            TableRow dataRow = new TableRow(Goal_Activity.this);
            String type = types.pop();
            String target = targets.pop();
            String start = startDates.pop();
            String end = endDates.pop();
            // Create TextViews for each column in the data row
            TextView typeTextView = createHeaderTextView(type);
            TextView targetStepsTextView = createHeaderTextView(target);
            TextView currentStepsTextView = createHeaderTextView(currents.pop());
            TextView startDateTextView = createHeaderTextView(start);
            TextView endDateTextView = createHeaderTextView(end);

            // Add the TextViews to the data row
            dataRow.addView(typeTextView);
            dataRow.addView(targetStepsTextView);
            dataRow.addView(currentStepsTextView);
            dataRow.addView(startDateTextView);
            dataRow.addView(endDateTextView);


            dataRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int start_day = getDay(start);
                    int start_month = getMonth(start);
                    int start_year = getYear(start);
                    int end_day = getDay(end);
                    int end_month = getMonth(end);
                    int end_year = getYear(end);
                    showDeleteConfirmationDialog(type, Integer.parseInt(target), start_day, start_month, start_year, end_day, end_month, end_year);
                }
            });

            // Add the data row to the table
            tableLayout.addView(dataRow);
        }
    }
    public void deleteEntryFromFirestore(String type, int target, int start_day, int start_month, int start_year, int end_day, int end_month, int end_year) {
        // Delete the document from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String user_id = auth.getUid();
        // Specify the collection and build the query
        CollectionReference collectionRef = db.collection("users/" + user_id + "/Goal");

        // Query to find the document with specified field values

        Query query = collectionRef
                .whereEqualTo("type", type)
                .whereEqualTo("target", target)
                .whereEqualTo("start_day", start_day)
                .whereEqualTo("start_month", start_month)
                .whereEqualTo("start_year", start_year)
                .whereEqualTo("end_day", end_day)
                .whereEqualTo("end_month", end_month)
                .whereEqualTo("end_year", end_year);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    // Delete the document
                    document.getReference().delete();
                }
                }
            })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                        Toast.makeText(Goal_Activity.this, "Error deleting entry: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void showDeleteConfirmationDialog(String type, int target, int start_day, int start_month, int start_year, int end_day, int end_month, int end_year) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteEntryFromFirestore(type, target, start_day, start_month, start_year, end_day, end_month, end_year);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked No button
                        dialog.dismiss();
                    }
                });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Show the AlertDialog
        dialog.show();
    }
    public void implementUI_LineChart(Stack<String> startDates, Stack<String> endDates, Stack<String> types, Stack<String> targets, Stack<String> currents){
        String title = "Workout";
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        TextView tv = findViewById(R.id.textView);

        FrameLayout chartContainer = findViewById(R.id.chartContainer);
            Chart chart = new LineChart(Goal_Activity.this);
            tv.setText("Workout Goals");
            // Create a list of data points for the last seven days
            ArrayList<Entry> entries = new ArrayList<>();
            entries.add(new Entry(0, 0));
            entries.add(new Entry(1, 1));
            entries.add(new Entry(2, 1));
            entries.add(new Entry(3, 1));
            entries.add(new Entry(4, 0));
            entries.add(new Entry(5, 1));
            entries.add(new Entry(6, 1));

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

        int inner_count = 0;
        tv.setText(title + " Goals");

        // Create a header row
        TableRow headerRow = new TableRow(Goal_Activity.this);

        // Create TextViews for each column in the header row
        TextView typeHeader = createHeaderTextView("Type");
        TextView targetStepsHeader = createHeaderTextView("Target");
        //TextView currentStepsHeader = createHeaderTextView("Current");
        TextView startDateHeader = createHeaderTextView("Start Date");
        TextView endDateHeader = createHeaderTextView("End Date");

        // Add the TextViews to the header row
        headerRow.addView(typeHeader);
        headerRow.addView(targetStepsHeader);
        //headerRow.addView(currentStepsHeader);
        headerRow.addView(startDateHeader);
        headerRow.addView(endDateHeader);

        // Add the header row to the table
        tableLayout.addView(headerRow);

        while (!types.isEmpty()) {
            TableRow dataRow = new TableRow(Goal_Activity.this);

            // Create TextViews for each column in the data row
            TextView typeTextView = createHeaderTextView(types.pop());
            TextView targetStepsTextView = createHeaderTextView(targets.pop());
            //TextView currentStepsTextView = createHeaderTextView(currents.pop());
            TextView startDateTextView = createHeaderTextView(startDates.pop());
            TextView endDateTextView = createHeaderTextView(endDates.pop());

            // Add the TextViews to the data row
            dataRow.addView(typeTextView);
            dataRow.addView(targetStepsTextView);
           // dataRow.addView(currentStepsTextView);
            dataRow.addView(startDateTextView);
            dataRow.addView(endDateTextView);

            // Add the data row to the table
            tableLayout.addView(dataRow);
        }
    }
    public void implementUI_HBarChart(Stack<String> startDates, Stack<String> endDates, Stack<String> types, Stack<String> targets, Stack<String> currents, String title){
        title = formatTitle(title);
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        TextView tv = findViewById(R.id.textView);

        FrameLayout chartContainer = findViewById(R.id.chartContainer);
            Chart chart = new HorizontalBarChart(this);

            // Customize the chart settings
            chart.getDescription().setEnabled(false);
            // Set target value (change as needed)
            float currentValue = Float.parseFloat(currents.get(0));

            ((HorizontalBarChart)chart).getAxisRight().setEnabled(false);

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
            entries.add(new BarEntry(0, Float.parseFloat(currents.get(0)))); // First entry

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
        int inner_count = 0;
        tv.setText(title + " Goals");

        // Create a header row
        TableRow headerRow = new TableRow(Goal_Activity.this);

        // Create TextViews for each column in the header row
        if(title.equalsIgnoreCase("Exercise")) {
            TextView typeHeader = createHeaderTextView("Type");
            headerRow.addView(typeHeader);
        }
        TextView targetStepsHeader = createHeaderTextView("Target");
        TextView currentStepsHeader = createHeaderTextView("Current");
        TextView startDateHeader = createHeaderTextView("Start Date");
        TextView endDateHeader = createHeaderTextView("End Date");

        // Add the TextViews to the header row
        headerRow.addView(targetStepsHeader);
        headerRow.addView(currentStepsHeader);
        headerRow.addView(startDateHeader);
        headerRow.addView(endDateHeader);

        // Add the header row to the table
        tableLayout.addView(headerRow);

        while (!targets.isEmpty()) {
            TableRow dataRow = new TableRow(Goal_Activity.this);

            // Create TextViews for each column in the data row
            if(title.equalsIgnoreCase("Exercise")) {
                TextView typeTextView = createHeaderTextView(types.pop());
                dataRow.addView(typeTextView);
            }
            TextView targetStepsTextView = createHeaderTextView(targets.pop());
            TextView currentStepsTextView = createHeaderTextView(currents.pop());
            TextView startDateTextView = createHeaderTextView(startDates.pop());
            TextView endDateTextView = createHeaderTextView(endDates.pop());

            // Add the TextViews to the data row
            dataRow.addView(targetStepsTextView);
            dataRow.addView(currentStepsTextView);
            dataRow.addView(startDateTextView);
            dataRow.addView(endDateTextView);

            // Add the data row to the table
            tableLayout.addView(dataRow);
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
    public void setupGoalHistoryUI(Stack<String> startDates, Stack<String> endDates, Stack<String> types, Stack<String> targets, Stack<String> currents, String title){
        title = formatTitle(title);
        TableLayout tableLayout = findViewById(R.id.tableLayout);

        TextView tv = findViewById(R.id.textView);
        tv.setText(title + " Goals");

        // Create a header row
        TableRow headerRow = new TableRow(Goal_Activity.this);

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

        while (!types.isEmpty()) {
            TableRow dataRow = new TableRow(Goal_Activity.this);

            // Create TextViews for each column in the data row
            TextView typeTextView = createHeaderTextView(types.pop());
            TextView targetStepsTextView = createHeaderTextView(targets.pop());
            TextView startDateTextView = createHeaderTextView(startDates.pop());
            TextView endDateTextView = createHeaderTextView(endDates.pop());

            // Add the TextViews to the data row
            dataRow.addView(typeTextView);
            dataRow.addView(targetStepsTextView);
            dataRow.addView(startDateTextView);
            dataRow.addView(endDateTextView);

            // Add the data row to the table
            tableLayout.addView(dataRow);
        }
    }
    public void setupInfoHistory(Stack<String> startDates, Stack<String> endDates, Stack<String> types, Stack<String> targets, Stack<String> currents, String title) {
        String start_date = "";
        String end_date = "";
        String target = "";
        //String current = "";
        String type = "";
        if(title.equalsIgnoreCase("steps")) {
            for (Map<String, Object> map : goal.getCompletedStepGoals()) {
                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                type = map.get("type").toString();
                target = map.get("target").toString();
                //current = String.valueOf(goal.getCurrent_steps());
                startDates.push(start_date);
                endDates.push(end_date);
                types.push(type);
                targets.push(target);
                //currents.push(current);
            }
        }
        else if(title.equalsIgnoreCase("hydration")){
            for (Map<String, Object> map : goal.getCompletedHydrationGoals()) {
                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                type = map.get("type").toString();
                target = map.get("target").toString();
                //current = String.valueOf(goal.getCurrent_ounces());
                startDates.push(start_date);
                endDates.push(end_date);
                types.push(type);
                targets.push(target);
                //currents.push(current);
            }
        }
        else if(title.equalsIgnoreCase("weight")){
            for (Map<String, Object> map : goal.getCompletedWeightGoals()) {
                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                //type = map.get("type").toString();
                target = map.get("target").toString();
                //current = String.valueOf(goal.getCurrent_weight());
                startDates.push(start_date);
                endDates.push(end_date);
                types.push(type);
                targets.push(target);
                //currents.push(current);
            }
        }
        else if(title.equalsIgnoreCase("exercise")){
            for (Map<String, Object> map : goal.getCompletedExerciseGoals()) {
                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                type = map.get("type").toString();
                target = map.get("target").toString();
                //current = String.valueOf(goal.getCurrent_max());
                startDates.push(start_date);
                endDates.push(end_date);
                types.push(type);
                targets.push(target);
                //currents.push(current);
            }
        }
        else if(title.equalsIgnoreCase("workout")){
            for (Map<String, Object> map : goal.getCompletedWorkoutGoals()) {
                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                type = map.get("type").toString();
                target = map.get("target").toString();
                //current = goal.getCurrent_streak();
                startDates.push(start_date);
                endDates.push(end_date);
                types.push(type);
                targets.push(target);
                //currents.push(current);
            }
        }
    }
    public void setupInfo(Stack<String> startDates, Stack<String> endDates, Stack<String> types, Stack<String> targets, Stack<String> currents, String title) {
        String start_date = "";
        String end_date = "";
        String target = "";
        String current = "";
        String type = "";
        if(title.equalsIgnoreCase("steps")) {
            for (Map<String, Object> map : goal.getStepGoals()) {
                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                type = map.get("type").toString();
                target = map.get("target").toString();
                current = String.valueOf(goal.getCurrent_steps());
                startDates.push(start_date);
                endDates.push(end_date);
                types.push(type);
                targets.push(target);
                currents.push(current);
            }
        }
        else if(title.equalsIgnoreCase("hydration")){
            for (Map<String, Object> map : goal.getHydrationGoals()) {
                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                type = map.get("type").toString();
                target = map.get("target").toString();
                current = String.valueOf(goal.getCurrent_ounces());
                startDates.push(start_date);
                endDates.push(end_date);
                types.push(type);
                targets.push(target);
                currents.push(current);
            }
        }
        else if(title.equalsIgnoreCase("weight")){
            for (Map<String, Object> map : goal.getWeightGoals()) {
                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                //type = map.get("type").toString();
                target = map.get("target").toString();
                current = String.valueOf(goal.getCurrent_weight());
                startDates.push(start_date);
                endDates.push(end_date);
                types.push(type);
                targets.push(target);
                currents.push(current);
            }
        }
        else if(title.equalsIgnoreCase("exercise")){
            for (Map<String, Object> map : goal.getExerciseGoals()) {
                start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
                end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
                type = map.get("type").toString();
                target = map.get("target").toString();
                current = String.valueOf(goal.getCurrent_max());
                startDates.push(start_date);
                endDates.push(end_date);
                types.push(type);
                targets.push(target);
                currents.push(current);
            }
        }
    }
    public void setupInfo_Workout(Stack<String> startDates, Stack<String> endDates, Stack<String> types, Stack<String> targets, Stack<ArrayList<Boolean>> currents, String title) {
        String start_date = "";
        String end_date = "";
        String target = "";
        ArrayList<Boolean> current = new ArrayList<Boolean>();
        String type = "";
        for (Map<String, Object> map : goal.getWorkoutGoals()) {
            start_date = map.get("start_month").toString() + "/" + map.get("start_day").toString() + "/" + map.get("start_year").toString();
            end_date = map.get("end_month").toString() + "/" + map.get("end_day").toString() + "/" + map.get("end_year").toString();
            type = map.get("type").toString();
            target = map.get("target").toString();
            current = goal.getCurrent_streak();
            startDates.push(start_date);
            endDates.push(end_date);
            types.push(type);
            targets.push(target);
            currents.push(current);
        }
    }
    public TextView createDataTextView(String data) {
        TextView textView = new TextView(this);
        textView.setText(data);
        return textView;
    }

    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(16, 8, 16, 8); // Adjust padding as needed
        //textView.setBackgroundResource(R.color.header_background); // Add a background color
        // Customize other attributes as needed
        return textView;
    }

    private String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
    private static int getDay(String formattedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        try {
            Date date = dateFormat.parse(formattedDate);
            SimpleDateFormat dayFormat = new SimpleDateFormat("d", Locale.getDefault());
            return Integer.parseInt(dayFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Handle the error appropriately
        }
    }

    private static int getMonth(String formattedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        try {
            Date date = dateFormat.parse(formattedDate);
            SimpleDateFormat monthFormat = new SimpleDateFormat("M", Locale.getDefault());
            return Integer.parseInt(monthFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Handle the error appropriately
        }
    }
    public void createAddGoalLayout(ScrollView scrollView, LinearLayout linearLayout, int index){
        // Add a title TextView
        TextView titleTextView = new TextView(Goal_Activity.this);
        titleTextView.setText("Add Goal");
        titleTextView.setTextSize(24); // Larger font size for the title
        titleTextView.setTextColor(Color.BLACK); // Black text color
        titleTextView.setTypeface(titleTextView.getTypeface(), android.graphics.Typeface.BOLD); // Bold text
        titleTextView.setGravity(Gravity.CENTER); // Center align the title
        titleTextView.setPadding(0, 20, 0, 20); // Add some padding to the title
        linearLayout.addView(titleTextView);
        // Create labeled TextViews
        linearLayout.addView(createTextView("Start Date:"));
        // Create a LinearLayout to center-align the DatePicker
        LinearLayout startDateLayout = new LinearLayout(Goal_Activity.this);
        startDateLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        startDateLayout.setGravity(Gravity.CENTER);
        startDatePicker = new DatePicker(Goal_Activity.this);
        startDateLayout.addView(startDatePicker);
        linearLayout.addView(startDateLayout);

        linearLayout.addView(createTextView("End Date:"));
        LinearLayout endDateLayout = new LinearLayout(Goal_Activity.this);
        endDateLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        endDateLayout.setGravity(Gravity.CENTER);
        endDatePicker = new DatePicker(Goal_Activity.this);
        endDateLayout.addView(endDatePicker);
        linearLayout.addView(endDateLayout);
        if(index == 0) {
            linearLayout.addView(createTextView("Target Steps:"));
            target = new EditText(Goal_Activity.this);
            target.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            linearLayout.addView(target);

            linearLayout.addView(createTextView("Type:"));
            type = new Spinner(Goal_Activity.this);
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(Goal_Activity.this, android.R.layout.simple_spinner_item, new String[]{"Daily", "Weekly"});
            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type.setAdapter(typeAdapter);
            linearLayout.addView(type);
        }
        if(index == 1) {
            linearLayout.addView(createTextView("Target Ounces:"));
            target = new EditText(Goal_Activity.this);
            target.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            linearLayout.addView(target);

            linearLayout.addView(createTextView("Type:"));
            type = new Spinner(Goal_Activity.this);
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(Goal_Activity.this, android.R.layout.simple_spinner_item, new String[]{"Daily", "Weekly"});
            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type.setAdapter(typeAdapter);
            linearLayout.addView(type);
        }
        if(index == 2) {
            linearLayout.addView(createTextView("Target Weight:"));
            target = new EditText(Goal_Activity.this);
            target.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            linearLayout.addView(target);
        }
        if(index == 3) {
            linearLayout.addView(createTextView("Target Weight:"));
            target = new EditText(Goal_Activity.this);
            target.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            linearLayout.addView(target);

            linearLayout.addView(createTextView("Exercise Name:"));
            type = new Spinner(Goal_Activity.this);
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(Goal_Activity.this, android.R.layout.simple_spinner_item, new String[] { "Bench Press", "Squats", "Deadlifts", "Overhead Press", "Bent Over Rows", "Pull-ups", "Push-ups", "Dumbbell Curls", "Tricep Dips", "Lunges", "Leg Press", "Lat Pulldowns", "Planks", "Russian Twists", "Mountain Climbers", "Burpees", "Kettlebell Swings", "Box Jumps", "Farmers Walk", "Hammer Curls", "Leg Extensions", "Calf Raises", "Hanging Leg Raises", "Front Squats", "Lateral Raises", "Hyperextensions", "Incline Bench Press", "Dumbbell Flyes", "Cable Rows", "Step-ups", "Reverse Crunches", "Medicine Ball Slams", "Seated Shoulder Press", "Cable Crunches", "Renegade Rows", "Romanian Deadlifts", "Box Squats", "Preacher Curls", "Face Pulls", "Side Planks", "Hamstring Curls" });
            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type.setAdapter(typeAdapter);
            linearLayout.addView(type);
        }
        if(index == 4) {
            linearLayout.addView(createTextView("Type:"));
            type = new Spinner(Goal_Activity.this);
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(Goal_Activity.this, android.R.layout.simple_spinner_item, new String[]{"Daily", "Weekly"});
            typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type.setAdapter(typeAdapter);
            linearLayout.addView(type);

            linearLayout.addView(createTextView("Target Streak:"));
            target = new EditText(Goal_Activity.this);
            target.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            linearLayout.addView(target);
        }
    }

    private static int getYear(String formattedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        try {
            Date date = dateFormat.parse(formattedDate);
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
            return Integer.parseInt(yearFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return 0; // Handle the error appropriately
        }
    }

    private void updateArtImageView() {
        if (currentImageIndex >= 0 && currentImageIndex < imageResources.length) {
            artImageView.setImageResource(imageResources[currentImageIndex]);
        }
    }
    private void updateTitle() {
        if (currentImageIndex >= 0 && currentImageIndex < imageResources.length) {
            title.setText(titleResources[currentImageIndex]);
        }
    }
    public Map<String, Object> setupGoalData(int index){
        // Retrieve values from the UI components
        int startDay = startDatePicker.getDayOfMonth();
        int startMonth = startDatePicker.getMonth();
        int startYear = startDatePicker.getYear();

        int endDay = endDatePicker.getDayOfMonth();
        int endMonth = endDatePicker.getMonth();
        int endYear = endDatePicker.getYear();
        int target_ = Integer.parseInt(target.getText().toString());
        String type_ = "";
        if(type != null){
            type_ = type.getSelectedItem().toString();
        }
        Map<String, String> completed = new HashMap<>();
        boolean comp = false;
        Map<String, Object> goalData = new HashMap<>();
        if(index == 0) {
            goalData.put("start_day", startDay);
            goalData.put("start_month", startMonth+1);
            goalData.put("start_year", startYear);
            goalData.put("end_day", endDay);
            goalData.put("end_month", endMonth+1);
            goalData.put("end_year", endYear);
            goalData.put("type", type_);
            goalData.put("target", target_);
            goalData.put("title", "steps");
            goalData.put("completed", completed);
        }
        if(index == 1) {
            goalData.put("start_day", startDay);
            goalData.put("start_month", startMonth+1);
            goalData.put("start_year", startYear);
            goalData.put("end_day", endDay);
            goalData.put("end_month", endMonth+1);
            goalData.put("end_year", endYear);
            goalData.put("type", type_);
            goalData.put("target", target_);
            goalData.put("title", "hydration");
            goalData.put("completed", completed);
        }
        if(index == 2) {
            goalData.put("start_day", startDay);
            goalData.put("start_month", startMonth+1);
            goalData.put("start_year", startYear);
            goalData.put("end_day", endDay);
            goalData.put("end_month", endMonth+1);
            goalData.put("end_year", endYear);
            goalData.put("target", target_);
            goalData.put("title", "weight");
            goalData.put("completed", comp);
        }
        if(index == 3) {
            goalData.put("start_day", startDay);
            goalData.put("start_month", startMonth+1);
            goalData.put("start_year", startYear);
            goalData.put("end_day", endDay);
            goalData.put("end_month", endMonth+1);
            goalData.put("end_year", endYear);
            goalData.put("type", type_);
            goalData.put("target", target_);
            goalData.put("title", "exercise");
            goalData.put("completed", comp);
        }
        if(index == 4) {
            goalData.put("start_day", startDay);
            goalData.put("start_month", startMonth+1);
            goalData.put("start_year", startYear);
            goalData.put("end_day", endDay);
            goalData.put("end_month", endMonth+1);
            goalData.put("end_year", endYear);
            goalData.put("type", type_);
            goalData.put("target", target_);
            goalData.put("title", "workout");
            goalData.put("completed", completed);
        }
        return goalData;
    }
    // Helper method to create TextViews with larger, bold fonts
    public TextView createTextView(String text) {
        TextView textView = new TextView(Goal_Activity.this);
        textView.setText(text);
        textView.setTextSize(18); // Larger font size
        textView.setTextColor(Color.BLACK); // Black text color
        textView.setTypeface(textView.getTypeface(), android.graphics.Typeface.BOLD); // Bold text
        return textView;
    }

}
