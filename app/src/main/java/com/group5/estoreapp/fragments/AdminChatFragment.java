package com.group5.estoreapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.estoreapp.R;
import com.group5.estoreapp.adapter.ChatRoomAdapter;
import com.group5.estoreapp.model.ChatHub;
import com.group5.estoreapp.services.ChatService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.*;

public class AdminChatFragment extends Fragment {

    private ChatService chatService;
    private RecyclerView recyclerView;
    private ChatRoomAdapter adapter;
    private List<ChatHub> chatHubList = new ArrayList<>();
    private static final String TAG = "AdminChatFragment";
    private int adminId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_chat, container, false);
        chatService = new ChatService(requireContext());

        SharedPreferences pref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        adminId = pref.getInt("userId", -1); // giả sử đã login với admin ID

        recyclerView = view.findViewById(R.id.recyclerViewChatRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ChatRoomAdapter(chatHubList, this::openChatFragment);
        recyclerView.setAdapter(adapter);

        loadChatHubs();

        return view;
    }

    private void loadChatHubs() {
        chatService.getChatHubsByUser(adminId, new Callback<List<ChatHub>>() {
            @Override
            public void onResponse(Call<List<ChatHub>> call, Response<List<ChatHub>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatHubList.clear();
                    chatHubList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Lỗi tải phòng chat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatHub>> call, Throwable t) {
                Log.e(TAG, "Lỗi kết nối khi tải phòng chat: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openChatFragment(ChatHub hub) {
        // Truyền chatHubId sang ChatFragment
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("chatHubId", hub.getId());
        fragment.setArguments(bundle);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
