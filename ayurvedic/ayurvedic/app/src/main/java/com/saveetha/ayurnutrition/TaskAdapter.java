package com.saveetha.ayurnutrition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> tasks;
    private Context context;
    private OnTaskCompleteListener listener;

    public interface OnTaskCompleteListener {
        void onComplete(Task task);
    }

    public TaskAdapter(Context context, List<Task> tasks, OnTaskCompleteListener listener) {
        this.context = context;
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Task t = tasks.get(pos);

        h.tvTitle.setText(t.title);
        h.tvPatient.setText("Patient: " + t.patientName);
        h.tvDue.setText("Due: " + t.dueDate);
        h.cbTask.setChecked("completed".equalsIgnoreCase(t.status));

        h.cbTask.setOnCheckedChangeListener((button, isChecked) -> {
            if (isChecked && listener != null && !"completed".equalsIgnoreCase(t.status)) {
                listener.onComplete(t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateTasks(List<Task> newTasks) {
        this.tasks = newTasks;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbTask;
        TextView tvTitle, tvPatient, tvDue;

        ViewHolder(View v) {
            super(v);
            cbTask = v.findViewById(R.id.cbComplete);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvPatient = v.findViewById(R.id.tvPatient);
            tvDue = v.findViewById(R.id.tvDueDate);
        }
    }
}
