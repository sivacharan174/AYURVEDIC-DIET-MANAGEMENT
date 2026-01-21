package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnPending, btnCompleted;
    RecyclerView rvTasks;
    TaskAdapter adapter;
    List<Task> allTasks = new ArrayList<>();
    List<Task> filteredTasks = new ArrayList<>();
    String currentFilter = "pending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        btnBack = findViewById(R.id.btnBack);
        btnPending = findViewById(R.id.btnPending);
        btnCompleted = findViewById(R.id.btnCompleted);
        rvTasks = findViewById(R.id.rvTasks);

        rvTasks.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        btnPending.setOnClickListener(v -> {
            currentFilter = "pending";
            updateTabColors();
            filterTasks();
        });

        btnCompleted.setOnClickListener(v -> {
            currentFilter = "completed";
            updateTabColors();
            filterTasks();
        });

        loadTasks();
    }

    private void updateTabColors() {
        int activeColor = 0xFF4DC080;
        int inactiveColor = 0xFF2E5746;
        btnPending.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                currentFilter.equals("pending") ? activeColor : inactiveColor));
        btnCompleted.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                currentFilter.equals("completed") ? activeColor : inactiveColor));
    }

    private void filterTasks() {
        filteredTasks.clear();
        for (Task t : allTasks) {
            if (t.status != null && t.status.equalsIgnoreCase(currentFilter)) {
                filteredTasks.add(t);
            }
        }
        if (adapter != null) {
            adapter.updateTasks(filteredTasks);
        }
    }

    private void loadTasks() {
        VolleyHelper.getInstance(this).getRequest(
                VolleyHelper.GET_TASKS_URL,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            allTasks.clear();
                            JSONArray arr = response.optJSONArray("data");
                            if (arr != null) {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    Task t = new Task();
                                    t.id = obj.optInt("id");
                                    t.title = obj.optString("title");
                                    t.description = obj.optString("description");
                                    t.patientName = obj.optString("patient_name");
                                    t.patientId = obj.optInt("patient_id");
                                    t.dueDate = obj.optString("due_date");
                                    t.status = obj.optString("status", "pending");
                                    t.type = obj.optString("type");
                                    allTasks.add(t);
                                }
                            }

                            adapter = new TaskAdapter(TasksActivity.this, filteredTasks, TasksActivity.this::onTaskComplete);
                            rvTasks.setAdapter(adapter);
                            filterTasks();

                        } catch (Exception e) {
                            Toast.makeText(TasksActivity.this, "Error loading tasks", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(TasksActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onTaskComplete(Task task) {
        // Mark task as complete via API
        java.util.Map<String, String> params = new java.util.HashMap<>();
        params.put("task_id", String.valueOf(task.id));
        params.put("status", "completed");

        VolleyHelper.getInstance(this).postRequest(
                VolleyHelper.UPDATE_TASK_URL,
                params,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        task.status = "completed";
                        filterTasks();
                        Toast.makeText(TasksActivity.this, "Task completed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(TasksActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
