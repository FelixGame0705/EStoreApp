package com.group5.estoreapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group5.estoreapp.R;
import com.group5.estoreapp.activities.MainActivity;
import com.group5.estoreapp.activities.ProductDetailActivity;
import com.group5.estoreapp.helpers.SessionManager;
import com.group5.estoreapp.model.CartItem;
import com.group5.estoreapp.model.Product;
import com.group5.estoreapp.services.CartService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void setProductList(List<Product> newList) {
        this.productList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView imgProduct, imgAdd;
        ImageView backButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgAdd = itemView.findViewById(R.id.imgAdd);
// ⬅️ gắn icon giỏ hàng
        }

        public void bind(Product product) {
            tvName.setText(product.getProductName());
            tvPrice.setText(product.getPrice() + "₫");
            Glide.with(context).load(product.getImageURL()).into(imgProduct);

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("product", product); // truyền cả object
                context.startActivity(intent);
            });


            // Bắt sự kiện click "Add to Cart"
            imgAdd.setOnClickListener(v -> {
                int userId = SessionManager.getUserId(context);
                if (userId == -1) {
                    Toast.makeText(context, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                    return;
                }

                CartService cartService = new CartService();
                cartService.addProductToCart(userId, product.getProductID(), 1, new CartService.AddToCartCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            });
        }
    }
}
