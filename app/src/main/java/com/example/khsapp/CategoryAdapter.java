package com.example.khsapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//This adpter where we fetch our online data

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryModel> categoryModelList;

    public CategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item,viewGroup,false);
        return new ViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder ViewHolder, int position) {
        //here we bind everyItem with his view group
        String icon=categoryModelList.get(position).getCategoryIconLink();
        String name=categoryModelList.get(position).getCategoryName();
        ViewHolder.setCategory(name);
    }

    @Override
    public int getItemCount() {
        //here return size of list
        return categoryModelList.size();
    }


    //<CategoryAdapter.ViewHolder> in this line, we getting error so we use below
    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView categoryIcon;
        private TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon=itemView.findViewById(R.id.category_icon);
            categoryName=itemView.findViewById(R.id.category_name);
        }

        private void setCategoryIcon(){
          //todo: set category Icon form Firebase database
        }

        private  void setCategory(final String name){
            //todo:set category Icon from Firebase databse
            categoryName.setText(name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent categoryIntent= new Intent(itemView.getContext(),CategoryActivity.class);
                    categoryIntent.putExtra("CategoryName",name);
                    itemView.getContext().startActivity(categoryIntent);
                }
            });
        }
    }

}
