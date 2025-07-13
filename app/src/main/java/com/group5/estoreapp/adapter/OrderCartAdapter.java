package com.group5.estoreapp.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group5.estoreapp.R;
import com.group5.estoreapp.model.CartItem;

import java.util.List;


public class OrderCartAdapter extends RecyclerView.Adapter<OrderCartAdapter.ViewHolder> {
    private final List<CartItem> cartItems;
    private final Context context;

    public OrderCartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.tvName.setText(item.getProductName());
        holder.tvQuantity.setText("Số lượng: " + item.getQuantity());
        holder.tvPrice.setText(item.getPrice() + "₫");
        Glide.with(context).load(item.getProductImageURL()).into(holder.imgProduct);

        // Ẩn các nút chỉnh sửa
        holder.btnAdd.setVisibility(View.GONE);
        holder.btnSubtract.setVisibility(View.GONE);
        holder.btnDelete.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvQuantity;
        ImageButton btnAdd, btnSubtract, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = itemView.findViewById(R.id.tvProductQuantity);
            btnAdd = itemView.findViewById(R.id.addQuantity);
            btnSubtract = itemView.findViewById(R.id.subtractQuantity);
            btnDelete = itemView.findViewById(R.id.deleteProduct);
        }
    }
}


