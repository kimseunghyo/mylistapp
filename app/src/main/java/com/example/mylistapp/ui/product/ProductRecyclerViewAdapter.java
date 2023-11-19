package com.example.mylistapp.ui.product;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mylistapp.R;
import com.example.mylistapp.databinding.FragmentProductBinding;
import com.example.mylistapp.dto.ProductDto;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {
    private ArrayList<ProductDto> prodData = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(int id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.onBind(prodData.get(position));
    }

    @Override
    public int getItemCount() {
        return prodData.size();
    }

    public void addProduct(ProductDto prodDto) {
        prodData.add(prodDto);
    }

    public void removeProducts() {
        prodData.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout prodLayout;
        public TextView nameTxt;
        public ImageView prodImg;
        public TextView priceTxt;
        public TextView etcTxt;
        public ViewHolder(FragmentProductBinding binding) {
            super(binding.getRoot());
            this.prodLayout = binding.prodLayout;
            this.nameTxt = binding.nameTxt;
            this.prodImg = binding.prodImg;
            this.priceTxt = binding.priceTxt;
            this.etcTxt = binding.etcTxt;

            prodLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getBindingAdapterPosition();
                    int id = prodData.get(pos).getId();

                    if (id != RecyclerView.NO_POSITION) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(id);
                        }
                    }
                }
            });
        }

        public void onBind(ProductDto prodDto) {
            int price = prodDto.getPrice();
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String result = decimalFormat.format(Double.valueOf(price));

            nameTxt.setText(prodDto.getName());
            priceTxt.setText(result + " " + prodDto.getPriceUnit());
            etcTxt.setText(prodDto.getEtc());

            Glide.with(prodImg.getContext())
                    .load(prodDto.getImage())
                    .error(R.drawable.baseline_broken_image_24)
                    .fallback(R.drawable.baseline_insert_photo_24)
                    .fitCenter()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("Glide", "Image load failed", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.i("Glide", "Image loaded successfully");
                            return false;
                        }
                    })
                    .into(prodImg);
            // 저장된 경로 자체는 문제가 없는거 같은데 그럼 imageView의 문제인가
        }
    }
}
