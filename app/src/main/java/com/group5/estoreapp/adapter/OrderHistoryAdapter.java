package com.group5.estoreapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.estoreapp.R;
import com.group5.estoreapp.model.Order;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private OnOrderClickListener onOrderClickListener;

    // Interface để handle click events
    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.onOrderClickListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView textOrderId;
        private TextView textOrderDate;
        private TextView textOrderStatus;
        private TextView textOrderTotal;
        private TextView textPaymentMethod;
        private TextView textShippingAddress;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            textOrderId = itemView.findViewById(R.id.textOrderId);
            textOrderDate = itemView.findViewById(R.id.textOrderDate);
            textOrderStatus = itemView.findViewById(R.id.textOrderStatus);
            textOrderTotal = itemView.findViewById(R.id.textOrderTotal);
            textPaymentMethod = itemView.findViewById(R.id.textPaymentMethod);
            textShippingAddress = itemView.findViewById(R.id.textShippingAddress);

            // Set click listener
            itemView.setOnClickListener(v -> {
                if (onOrderClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onOrderClickListener.onOrderClick(orderList.get(position));
                    }
                }
            });
        }

        public void bind(Order order) {
            // Order ID
            textOrderId.setText("Đơn hàng #" + order.getOrderID());

            // Order Date
            if (order.getOrderDate() != null) {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
//                String formattedDate = dateFormat.format(order.getOrderDate());
                textOrderDate.setText(order.getOrderDate());
            } else {
                textOrderDate.setText("N/A");
            }

            // Order Status
            String status = order.getOrderStatus();
            textOrderStatus.setText(getStatusDisplayText(status));
            textOrderStatus.setTextColor(getStatusColor(status));

            // Order Total
            if (order.getTotalAmount() != 0) {
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String formattedPrice = formatter.format(order.getTotalAmount());
                textOrderTotal.setText(formattedPrice);
            } else {
                textOrderTotal.setText("0 VNĐ");
            }

            // Payment Method
            String paymentMethod = order.getPaymentMethod();
            if (paymentMethod != null && !paymentMethod.isEmpty()) {
                textPaymentMethod.setText(paymentMethod);
            } else {
                textPaymentMethod.setText("N/A");
            }

            // Shipping Address
            String address = order.getBillingAddress();
            if (address != null && !address.isEmpty()) {
                textShippingAddress.setText(address);
            } else {
                textShippingAddress.setText("N/A");
            }
        }

        private String getStatusDisplayText(String status) {
            if (status == null) return "N/A";

            switch (status.toLowerCase()) {
                case "pending":
                    return "Chờ xử lý";
                case "confirmed":
                    return "Đã xác nhận";
                case "processing":
                    return "Đang xử lý";
                case "shipping":
                    return "Đang giao hàng";
                case "delivered":
                    return "Đã giao hàng";
                case "cancelled":
                    return "Đã hủy";
                case "completed":
                    return "Hoàn thành";
                default:
                    return status;
            }
        }

        private int getStatusColor(String status) {
            if (status == null) return context.getResources().getColor(android.R.color.darker_gray);

            switch (status.toLowerCase()) {
                case "pending":
                    return context.getResources().getColor(android.R.color.holo_orange_dark);
                case "confirmed":
                case "processing":
                    return context.getResources().getColor(android.R.color.holo_blue_dark);
                case "shipping":
                    return context.getResources().getColor(android.R.color.holo_purple);
                case "delivered":
                case "completed":
                    return context.getResources().getColor(android.R.color.holo_green_dark);
                case "cancelled":
                    return context.getResources().getColor(android.R.color.holo_red_dark);
                default:
                    return context.getResources().getColor(android.R.color.darker_gray);
            }
        }
    }
}