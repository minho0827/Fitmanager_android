package com.fitmanager.app.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fitmanager.app.R;

/**
 * Created by Home on 21/03/2019.
 */

public class MealInsertGalleryAdapter extends RecyclerView.Adapter<MealInsertGalleryAdapter.ViewHolder> {
    static int TYPE_HEADER = 0;
    static int TYPE_CELL = 1;




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_insert_header_item,parent,false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_insert_cell_item, parent,false);
        }
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
        }
    }
}
