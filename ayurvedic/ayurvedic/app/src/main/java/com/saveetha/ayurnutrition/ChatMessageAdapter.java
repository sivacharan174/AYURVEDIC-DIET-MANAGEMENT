package com.saveetha.ayurnutrition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    private List<ChatMessage> messages;
    private Context context;

    public ChatMessageAdapter(Context context, List<ChatMessage> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage msg = messages.get(position);

        if (msg.isUser()) {
            holder.cardUser.setVisibility(View.VISIBLE);
            holder.llAI.setVisibility(View.GONE);
            holder.tvUserMessage.setText(msg.message);
        } else {
            holder.cardUser.setVisibility(View.GONE);
            holder.llAI.setVisibility(View.VISIBLE);
            holder.tvAIMessage.setText(msg.message);
        }
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void clearMessages() {
        int size = messages.size();
        messages.clear();
        notifyItemRangeRemoved(0, size);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardUser;
        LinearLayout llAI;
        TextView tvUserMessage, tvAIMessage;

        ViewHolder(View v) {
            super(v);
            cardUser = v.findViewById(R.id.cardUser);
            llAI = v.findViewById(R.id.llAI);
            tvUserMessage = v.findViewById(R.id.tvUserMessage);
            tvAIMessage = v.findViewById(R.id.tvAIMessage);
        }
    }
}
