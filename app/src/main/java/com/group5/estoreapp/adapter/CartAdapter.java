package com.group5.estoreapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group5.estoreapp.R;
import com.group5.estoreapp.activities.MainActivity;
import com.group5.estoreapp.model.CartItem;
import com.group5.estoreapp.services.CartService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final Context context;
    private final List<CartItem> cartItemList;

    public interface OnCartChangeListener {
        void onCartUpdated();
    }

    private OnCartChangeListener cartChangeListener;

    public void setOnCartChangeListener(OnCartChangeListener listener) {
        this.cartChangeListener = listener;
    }

    public CartAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);

        holder.tvName.setText(item.getProductName());
        holder.tvQuantity.setText("Số lượng: " + item.getQuantity());
        holder.tvPrice.setText(item.getPrice() + "₫");

        Glide.with(context).load(item.getProductImageURL()).into(holder.imgProduct);

        // Cộng số lượng
        holder.btnAdd.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            updateQuantity(item.getCartID(), item.getProductID(), newQuantity, () -> {
                item.setQuantity(newQuantity);
                notifyItemChanged(position);
            });
        });

        // Trừ số lượng
        holder.btnSubtract.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQuantity = item.getQuantity() - 1;
                updateQuantity(item.getCartID(), item.getProductID(), newQuantity, () -> {
                    item.setQuantity(newQuantity);
                    notifyItemChanged(position);
                });
            }
        });

        // Xóa sản phẩm khỏi giỏ hàng
        holder.btnDelete.setOnClickListener(v -> {
            int cartItemId = item.getCartItemID();

            CartService cartService = new CartService();
            cartService.deleteCartItem(cartItemId, new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        cartItemList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartItemList.size());

                        if (cartChangeListener != null) {
                            cartChangeListener.onCartUpdated();
                        }

                        Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                        if (cartChangeListener != null) {
                            cartChangeListener.onCartUpdated();
                        }
                    } else {
                        Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Lỗi khi xóa: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public ImageButton btnAdd;
        public ImageButton btnSubtract;
        public ImageButton btnDelete;
        ImageView imgProduct;
        TextView tvName, tvPrice, tvQuantity;

        public CartViewHolder(@NonNull View itemView) {
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

    private void updateQuantity(int cartId, int productId, int quantity, Runnable onSuccess) {
        CartService cartService = new CartService();
        cartService.addOrUpdateCartItem(cartId, productId, quantity, new CartService.AddToCartCallback() {
            @Override
            public void onSuccess() {
                onSuccess.run(); // cập nhật UI
                if (cartChangeListener != null) {
                    cartChangeListener.onCartUpdated(); // cập nhật badge
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(context, "Cập nhật số lượng thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
//    public List<CartItem> getCartItems() {
//        return cartItemList;
//    }
}
