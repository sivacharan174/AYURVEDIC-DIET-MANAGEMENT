package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    RecyclerView rvAppointments, rvDietCharts, rvFollowups;
    TextView tvTodayCount, tvActiveCount, tvPendingCount, tvGreeting, tvNoAppointments;
    TextView tvSeeAllAppointments, tvSeeAllDietCharts, tvSeeAllFollowups;

    List<DashboardItem> appointmentsList = new ArrayList<>();
    List<DashboardItem> dietChartsList = new ArrayList<>();
    List<DashboardItem> followupsList = new ArrayList<>();

    DashboardAdapter appointmentsAdapter, dietChartsAdapter, followupsAdapter;

    public DashboardFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Bind views
        rvAppointments = view.findViewById(R.id.rvAppointments);
        rvDietCharts = view.findViewById(R.id.rvDietCharts);
        rvFollowups = view.findViewById(R.id.rvFollowups);
        tvTodayCount = view.findViewById(R.id.tvTodayCount);
        tvActiveCount = view.findViewById(R.id.tvActiveCount);
        tvPendingCount = view.findViewById(R.id.tvPendingCount);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        tvNoAppointments = view.findViewById(R.id.tvNoAppointments);

        // Setup RecyclerViews
        rvAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvDietCharts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFollowups.setLayoutManager(new LinearLayoutManager(getContext()));

        appointmentsAdapter = new DashboardAdapter(appointmentsList);
        dietChartsAdapter = new DashboardAdapter(dietChartsList);
        followupsAdapter = new DashboardAdapter(followupsList);

        rvAppointments.setAdapter(appointmentsAdapter);
        rvDietCharts.setAdapter(dietChartsAdapter);
        rvFollowups.setAdapter(followupsAdapter);

        // Set greeting based on time
        setGreeting();

        // Load data from API
        loadDashboardData();

        // Setup "See All" click listeners
        setupSeeAllListeners(view);

        return view;
    }

    private void setupSeeAllListeners(View view) {
        // Add Appointment button
        View btnAddAppointment = view.findViewById(R.id.btnAddAppointment);
        if (btnAddAppointment != null) {
            btnAddAppointment.setOnClickListener(v -> {
                startActivity(new Intent(getContext(), AddAppointmentActivity.class));
            });
        }

        // Chatbot button
        View btnChatbot = view.findViewById(R.id.btnChatbot);
        if (btnChatbot != null) {
            btnChatbot.setOnClickListener(v -> {
                startActivity(new Intent(getContext(), ChatbotActivity.class));
            });
        }

        // See All Diet Charts button
        View btnSeeAllDietCharts = view.findViewById(R.id.btnSeeAllDietCharts);
        if (btnSeeAllDietCharts != null) {
            btnSeeAllDietCharts.setOnClickListener(v -> {
                startActivity(new Intent(getContext(), DietPlanHistoryActivity.class));
            });
        }

        // See All Follow-ups button
        View btnSeeAllFollowups = view.findViewById(R.id.btnSeeAllFollowups);
        if (btnSeeAllFollowups != null) {
            btnSeeAllFollowups.setOnClickListener(v -> {
                startActivity(new Intent(getContext(), TasksActivity.class));
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment is visible
        loadDashboardData();
    }

    private void setGreeting() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String greeting;

        if (hour >= 5 && hour < 12) {
            greeting = "Good Morning 🌿";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good Afternoon ☀️";
        } else if (hour >= 17 && hour < 21) {
            greeting = "Good Evening 🌅";
        } else {
            greeting = "Good Night 🌙";
        }

        if (tvGreeting != null) {
            tvGreeting.setText(greeting);
        }
    }

    private void loadDashboardData() {
        if (getContext() == null)
            return;

        // Load appointments
        loadAppointments();

        // Load patients for stats
        loadPatientStats();

        // Load diet charts
        loadDietCharts();

        // Load tasks as follow-ups
        loadTasks();
    }

    private void loadAppointments() {
        // Get today's date
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Calendar.getInstance().getTime());

        String url = VolleyHelper.GET_APPOINTMENTS_URL + "?date=" + today;

        VolleyHelper.getInstance(getContext()).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (!isAdded())
                    return;

                try {
                    appointmentsList.clear();
                    JSONArray arr = response.optJSONArray("data");

                    int todayCount = 0;

                    if (arr != null && arr.length() > 0) {
                        todayCount = arr.length();

                        for (int i = 0; i < Math.min(arr.length(), 5); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            String patientName = obj.optString("patient_name", "Unknown");
                            String time = obj.optString("appointment_time", "");
                            String purpose = obj.optString("purpose", "Consultation");
                            String status = obj.optString("status", "scheduled");

                            // Format time (HH:mm to h:mm a)
                            String formattedTime = formatTime(time);

                            appointmentsList.add(new DashboardItem(
                                    "Patient: " + patientName,
                                    formattedTime + " • " + purpose));
                        }
                    }

                    tvTodayCount.setText(String.valueOf(todayCount));
                    appointmentsAdapter.notifyDataSetChanged();

                    // Show/hide empty state
                    if (tvNoAppointments != null) {
                        if (appointmentsList.isEmpty()) {
                            tvNoAppointments.setVisibility(View.VISIBLE);
                            rvAppointments.setVisibility(View.GONE);
                        } else {
                            tvNoAppointments.setVisibility(View.GONE);
                            rvAppointments.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    loadPatientsAsAppointments();
                }
            }

            @Override
            public void onError(String error) {
                // Fallback to showing recent patients
                loadPatientsAsAppointments();
            }
        });
    }

    private void loadPatientsAsAppointments() {
        // Fallback: show recent patients if appointments API not available
        VolleyHelper.getInstance(getContext()).getPatients(new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (!isAdded())
                    return;

                try {
                    JSONArray arr = response.optJSONArray("data");
                    if (arr == null)
                        arr = response.optJSONArray("patients");

                    appointmentsList.clear();
                    if (arr != null) {
                        int count = Math.min(arr.length(), 3);
                        tvTodayCount.setText(String.valueOf(count));

                        for (int i = 0; i < count; i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            String name = obj.optString("first_name", "") + " " + obj.optString("last_name", "");
                            String gender = obj.optString("gender", "");
                            appointmentsList.add(new DashboardItem(
                                    "Patient: " + name.trim(),
                                    gender + " • Recent patient"));
                        }
                    }
                    appointmentsAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    tvTodayCount.setText("0");
                }
            }

            @Override
            public void onError(String error) {
                tvTodayCount.setText("0");
            }
        });
    }

    private void loadPatientStats() {
        VolleyHelper.getInstance(getContext()).getPatients(new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (!isAdded())
                    return;

                try {
                    JSONArray arr = response.optJSONArray("data");
                    if (arr == null)
                        arr = response.optJSONArray("patients");

                    if (arr != null) {
                        tvActiveCount.setText(String.valueOf(arr.length()));
                    }
                } catch (Exception e) {
                    tvActiveCount.setText("0");
                }
            }

            @Override
            public void onError(String error) {
                if (isAdded()) {
                    tvActiveCount.setText("0");
                }
            }
        });
    }

    private void loadDietCharts() {
        String url = VolleyHelper.GET_DIET_CHARTS_URL;

        VolleyHelper.getInstance(getContext()).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (!isAdded())
                    return;

                try {
                    dietChartsList.clear();
                    JSONArray arr = response.optJSONArray("data");

                    if (arr != null && arr.length() > 0) {
                        for (int i = 0; i < Math.min(arr.length(), 5); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            String status = obj.optString("status", "");

                            if (status.equalsIgnoreCase("active") || dietChartsList.size() < 3) {
                                String goal = obj.optString("goal", "Diet Plan");
                                String duration = obj.optString("duration", "");
                                String createdAt = obj.optString("created_at", "");

                                String dateStr = formatDate(createdAt);

                                dietChartsList.add(new DashboardItem(
                                        goal + " Diet",
                                        duration + " • Created: " + dateStr));
                            }
                        }

                        dietChartsAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                // Silent fail
            }
        });
    }

    private void loadTasks() {
        String url = VolleyHelper.GET_TASKS_URL;

        VolleyHelper.getInstance(getContext()).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (!isAdded())
                    return;

                try {
                    followupsList.clear();
                    JSONArray arr = response.optJSONArray("data");
                    if (arr == null)
                        arr = response.optJSONArray("tasks");

                    int pendingCount = 0;

                    if (arr != null) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            String status = obj.optString("status", "");

                            if (status.equalsIgnoreCase("pending") || status.equalsIgnoreCase("incomplete")) {
                                pendingCount++;

                                if (followupsList.size() < 5) {
                                    String title = obj.optString("title", obj.optString("task_name", "Task"));
                                    String dueDate = obj.optString("due_date", "");
                                    String priority = obj.optString("priority", "normal");

                                    String subtitle = priority.substring(0, 1).toUpperCase() + priority.substring(1)
                                            + " Priority";
                                    if (!dueDate.isEmpty()) {
                                        subtitle += " • Due: " + formatDate(dueDate);
                                    }

                                    followupsList.add(new DashboardItem(title, subtitle));
                                }
                            }
                        }
                    }

                    tvPendingCount.setText(String.valueOf(pendingCount));
                    followupsAdapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                    tvPendingCount.setText("0");
                }
            }

            @Override
            public void onError(String error) {
                if (isAdded()) {
                    tvPendingCount.setText("0");
                }
            }
        });
    }

    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "N/A";
        }
        try {
            String[] parts = dateStr.split(" ")[0].split("-");
            String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
            int month = Integer.parseInt(parts[1]) - 1;
            int day = Integer.parseInt(parts[2]);
            return months[month] + " " + day;
        } catch (Exception e) {
            return dateStr;
        }
    }

    private String formatTime(String time) {
        if (time == null || time.isEmpty())
            return "";
        try {
            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            String ampm = hour >= 12 ? "PM" : "AM";
            if (hour > 12)
                hour -= 12;
            if (hour == 0)
                hour = 12;
            return String.format(Locale.getDefault(), "%d:%02d %s", hour, minute, ampm);
        } catch (Exception e) {
            return time;
        }
    }
}
