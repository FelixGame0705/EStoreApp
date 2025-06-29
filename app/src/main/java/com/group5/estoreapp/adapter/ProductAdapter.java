package com.group5.estoreapp.adapter;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.group5.estoreapp.ProductDetailActivity;
import com.group5.estoreapp.model.Product;
import com.group5.estoreapp.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, imgAdd;
        TextView tvProductName, tvProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgAdd = itemView.findViewById(R.id.imgAdd);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        // Hiển thị tên sản phẩm
        holder.tvProductName.setText(product.getTitle());

        // Hiển thị giá
        holder.tvProductPrice.setText("$" + product.getPrice());

        // Load ảnh từ URL
        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .placeholder(R.drawable.placeholder_image) // ảnh khi loading
                .error(R.drawable.error_image) // ảnh khi lỗi
                .into(holder.imgProduct);

        // Click vào sản phẩm để chuyển qua ProductDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.getId());
            v.getContext().startActivity(intent);
        });

        // (Optional) Bạn có thể thêm sự kiện cho imgAdd nếu muốn xử lý thêm
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
