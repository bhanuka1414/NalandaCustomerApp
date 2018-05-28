package com.bp.nalandacustomerapp.services;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bp.nalandacustomerapp.HomeActivity;
import com.bp.nalandacustomerapp.R;
import com.bp.nalandacustomerapp.services.models.CatModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomCatAdapter extends RecyclerView.Adapter<CustomCatAdapter.ViewHolder> {

    private List<CatModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    // data is passed into the constructor
    public CustomCatAdapter(Context context, List<CatModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_list_1, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CatModel catModel = mData.get(position);
        holder.idTxt.setText(catModel.getId());
        holder.nameTxt.setText(catModel.getName());
        Picasso.with(context).load(catModel.getImg()).into(holder.imageView);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView idTxt, nameTxt;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            idTxt = itemView.findViewById(R.id.cat_id);
            nameTxt = itemView.findViewById(R.id.cat_name);
            imageView = (ImageView) itemView.findViewById(R.id.cat_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    CatModel getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
