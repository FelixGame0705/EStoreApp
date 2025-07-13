package com.group5.estoreapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.estoreapp.R;
import com.group5.estoreapp.adapter.ChatAdapter;
import com.group5.estoreapp.model.ChatMessage;
import com.group5.estoreapp.services.ChatService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {
    private ChatAdapter chatAdapter;
    private EditText inputMessage;
    private ChatService chatService;
    private int userId ; // TODO: Thay bằng userID thực tế từ session hoặc sharedPreferences

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        chatService = new ChatService(); // ✅ khởi tạo đúng

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewChat);
        inputMessage = view.findViewById(R.id.editTextMessage);
        Button btnSend = view.findViewById(R.id.buttonSend);

        SharedPreferences pref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        userId = pref.getInt("userId", -1);

        chatAdapter = new ChatAdapter(new java.util.ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);

        btnSend.setOnClickListener(v -> sendMessage());

        loadChatHistory();

        return view;
    }

    private void loadChatHistory() {
        chatService.getChatHistory(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatAdapter.setMessages(response.body());
                } else {
                    Toast.makeText(getContext(), "Không thể tải tin nhắn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải lịch sử chat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String msg = inputMessage.getText().toString().trim();
        if (msg.isEmpty()) return;

        String time = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(new Date());
        ChatMessage newMessage = new ChatMessage(userId, msg, time);

        chatService.sendMessage(newMessage, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                inputMessage.setText("");
                loadChatHistory(); // reload messages
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Gửi tin nhắn thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
