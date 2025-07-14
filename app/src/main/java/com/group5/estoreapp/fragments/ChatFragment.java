package com.group5.estoreapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
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
import com.group5.estoreapp.helpers.SignalRManager;
import com.group5.estoreapp.model.ApiResponse;
import com.group5.estoreapp.model.ChatHub;
import com.group5.estoreapp.model.ChatMessage;
import com.group5.estoreapp.services.ChatService;

import java.util.*;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

    private static final int ADMIN_ID = 2;
    private static final String TAG = "ChatFragment";

    private ChatAdapter chatAdapter;
    private EditText inputMessage;
    private ChatService chatService;
    private RecyclerView recyclerView;

    private int userId;
    private String role;
    private String chatHubId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        chatService = new ChatService(requireContext());

        // Lấy userId và role
        SharedPreferences pref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        userId = pref.getInt("userId", -1);
        role = pref.getString("role", "User");

        // View binding
        inputMessage = view.findViewById(R.id.editTextMessage);
        Button btnSend = view.findViewById(R.id.buttonSend);
        recyclerView = view.findViewById(R.id.recyclerViewChat);
        chatAdapter = new ChatAdapter(new ArrayList<>());
        chatAdapter.setCurrentUserId(userId);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chatAdapter);

        btnSend.setOnClickListener(v -> sendMessage());

        // Nếu là Admin thì nhận hubId từ Bundle
        if ("Admin".equalsIgnoreCase(role) && getArguments() != null) {
            chatHubId = getArguments().getString("chatHubId");
            if (chatHubId != null) {
                loadChatMessages(chatHubId);
            } else {
                Toast.makeText(getContext(), "Không tìm thấy phòng chat", Toast.LENGTH_SHORT).show();
            }
        } else {
            initChatHubForUser();
        }

        return view;
    }

    private void initChatHubForUser() {
        chatService.getChatHubsByUser(userId, new Callback<List<ChatHub>>() {
            @Override
            public void onResponse(Call<List<ChatHub>> call, Response<List<ChatHub>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChatHub> hubs = response.body();
                    ChatHub found = null;

                    for (ChatHub hub : hubs) {
                        if ((hub.getFUserId() == ADMIN_ID && hub.getSUserId() == userId) ||
                                (hub.getFUserId() == userId && hub.getSUserId() == ADMIN_ID)) {
                            found = hub;
                            break;
                        }
                    }

                    if (found != null) {
                        chatHubId = found.getId();
                        loadChatMessages(chatHubId);
                    } else {
                        createChatHubWithAdmin();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi lấy phòng chat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatHub>> call, Throwable t) {
                Log.e(TAG, "Lỗi API getChatHubsByUser: " + t.getMessage());
                Toast.makeText(getContext(), "Không kết nối được", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createChatHubWithAdmin() {
        chatService.createChatHub(ADMIN_ID, new Callback<ApiResponse<ChatHub>>() {
            @Override
            public void onResponse(Call<ApiResponse<ChatHub>> call, Response<ApiResponse<ChatHub>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    chatHubId = response.body().getResult().getId();
                    loadChatMessages(chatHubId);
                } else {
                    Toast.makeText(getContext(), "Không thể tạo phòng chat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ChatHub>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tạo phòng chat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadChatMessages(String hubId) {
        chatService.getChatHubById(hubId, new Callback<ChatHub>() {
            @Override
            public void onResponse(Call<ChatHub> call, Response<ChatHub> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ChatMessage> messages = response.body().getMessages();

                    if (messages != null) {
                        // Sắp xếp theo thời gian tăng dần
                        Collections.sort(messages, Comparator.comparing(ChatMessage::getSentAt));
                        chatAdapter.setMessages(messages);
                        recyclerView.scrollToPosition(messages.size() - 1);
                    }
                    connectToSignalR(hubId);
                } else {
                    Toast.makeText(getContext(), "Không thể tải tin nhắn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChatHub> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi tải tin nhắn", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String message = inputMessage.getText().toString().trim();
        if (message.isEmpty() || chatHubId == null) return;
        SignalRManager.getInstance().sendMessage(chatHubId, message, userId);

        chatService.sendMessage(chatHubId, message, 0, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    inputMessage.setText("");
                    loadChatMessages(chatHubId);
                } else {
                    Toast.makeText(getContext(), "Không gửi được tin nhắn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi gửi tin nhắn", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void connectToSignalR(String hubId) {
        SignalRManager.getInstance().startConnection(
                "https://prmbe.felixtien.dev/chatHub", // Địa chỉ hub
                userId,
                new SignalRManager.MessageListener() {
                    @Override
                    public void onNewMessage(int senderId, String message) {
                        requireActivity().runOnUiThread(() -> {
                            ChatMessage chatMessage = new ChatMessage();
                            chatMessage.setSenderId(senderId);
                            chatMessage.setContent(message);

                            chatAdapter.addMessage(chatMessage);
                            recyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
                        });
                    }

                    @Override
                    public void onSystemMessage(String message) {
                        Log.d(TAG, "System: " + message);
                    }
                },
                new SignalRManager.ConnectionListener() {
                    @Override
                    public void onConnected() {
                        SignalRManager.getInstance().joinGroup(hubId, role); // Admin/User
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "SignalR error: " + t.getMessage());
                    }
                }
        );
    }

}
