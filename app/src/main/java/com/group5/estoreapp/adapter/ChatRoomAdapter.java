package com.group5.estoreapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.estoreapp.R;
import com.group5.estoreapp.model.ChatHub;

import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder> {

    private final List<ChatHub> chatHubs;
    private final OnChatRoomClickListener listener;

    public interface OnChatRoomClickListener {
        void onChatRoomClick(ChatHub chatHub);
    }

    public ChatRoomAdapter(List<ChatHub> chatHubs, OnChatRoomClickListener listener) {
        this.chatHubs = chatHubs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room, parent, false);
        return new ChatRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        ChatHub hub = chatHubs.get(position);
        int userId = hub.getFUserId() != 2 ? hub.getFUserId() : hub.getSUserId(); // lấy user khác admin
        String userLabel = "User ID: " + userId;
        String lastMessage = hub.getMessages() != null && !hub.getMessages().isEmpty()
                ? hub.getMessages().get(hub.getMessages().size() - 1).getContent()
                : "Chưa có tin nhắn";

        holder.textViewRoomName.setText(userLabel);
        holder.textViewLastMessage.setText(lastMessage);
        holder.itemView.setOnClickListener(v -> listener.onChatRoomClick(hub));
    }

    @Override
    public int getItemCount() {
        return chatHubs.size();
    }

    public static class ChatRoomViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRoomName, textViewLastMessage;

        public ChatRoomViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRoomName = itemView.findViewById(R.id.textViewRoomName);
            textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);
        }
    }
}
