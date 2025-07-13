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
import com.group5.estoreapp.model.ApiResponse;
import com.group5.estoreapp.model.ChatHub;
import com.group5.estoreapp.model.ChatMessage;
import com.group5.estoreapp.services.ChatService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

    private ChatAdapter chatAdapter;
    private EditText inputMessage;
    private ChatService chatService;
    private int userId;
    private String role;
    private int chatHubId = -1;

    private RecyclerView recyclerView;
    private final int ADMIN_ID = 2; // 👈 Admin mặc định

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        chatService = new ChatService(requireContext());

        SharedPreferences pref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        userId = pref.getInt("userId", -1);
//        role = pref.getString("role", "User");
        role = "User";

        inputMessage = view.findViewById(R.id.editTextMessage);
        Button btnSend = view.findViewById(R.id.buttonSend);
        recyclerView = view.findViewById(R.id.recyclerViewChat);
        chatAdapter = new ChatAdapter(new ArrayList<>());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);

        btnSend.setOnClickListener(v -> sendMessage());

        initChatHub();

        return view;
    }

    private void initChatHub() {
        if (role.equalsIgnoreCase("Admin")) {
            // Admin lấy tất cả phòng của họ
            chatService.getChatHubsByUser(userId, new Callback<ApiResponse<List<ChatHub>>>() {
                @Override
                public void onResponse(Call<ApiResponse<List<ChatHub>>> call, Response<ApiResponse<List<ChatHub>>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        List<ChatHub> hubs = response.body().getResult();
                        if (!hubs.isEmpty()) {
                            chatHubId = hubs.get(0).getId(); // lấy phòng đầu tiên
                            loadChatMessages();
                        } else {
                            Toast.makeText(getContext(), "Không tìm thấy phòng chat", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<List<ChatHub>>> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi lấy danh sách phòng", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // User gửi tin đến Admin (ADMIN_ID)
            chatService.createChatHub(ADMIN_ID, new Callback<ApiResponse<ChatHub>>() {
                @Override
                public void onResponse(Call<ApiResponse<ChatHub>> call, Response<ApiResponse<ChatHub>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        chatHubId = response.body().getResult().getId();
                        loadChatMessages();
                    } else {
                        String errorBody = "";
                        try {
                            if (response.errorBody() != null) {
                                errorBody = response.errorBody().string();
                            }
                        } catch (Exception e) {
                            errorBody = "Lỗi đọc errorBody: " + e.getMessage();
                        }

                        Toast.makeText(getContext(), "Không thể tạo phòng chat:\n" + errorBody, Toast.LENGTH_LONG).show();
                        System.out.println("❌ Không tạo được chatHub. Status: " + response.code());
                        System.out.println("❌ Body: " + errorBody);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<ChatHub>> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối server: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    t.printStackTrace(); // log chi tiết lỗi ra Logcat
                }
            });

        }
    }

    private void loadChatMessages() {
        if (chatHubId == -1) return;

        chatService.getChatHubById(chatHubId, new Callback<ApiResponse<ChatHub>>() {
            @Override
            public void onResponse(Call<ApiResponse<ChatHub>> call, Response<ApiResponse<ChatHub>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ChatHub hub = response.body().getResult();
                    List<ChatMessage> messages = hub.getMessages();
                    chatAdapter.setMessages(messages);
                    recyclerView.scrollToPosition(messages.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ChatHub>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi tải tin nhắn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String msg = inputMessage.getText().toString().trim();
        if (msg.isEmpty() || chatHubId == -1) return;

        chatService.sendMessage(chatHubId, userId, msg, new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    inputMessage.setText("");
                    loadChatMessages();
                } else {
                    Toast.makeText(getContext(), "Gửi thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Toast.makeText(getContext(), "Gửi tin nhắn thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
