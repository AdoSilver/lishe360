package com.cardeanutrition.lishe360app.fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cardeanutrition.lishe360app.R;
import com.cardeanutrition.lishe360app.activities.MainActivity;

import java.util.List;

public class categoriesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<String> categoryOldList;
    //private List<String> selectedCategories;


    public categoriesRecyclerAdapter(Context context, List<String> categoryOldList){
        this.context = context;
        this.categoryOldList = categoryOldList;
        //this.selectedCategories = new ArrayList<>();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View createScheduleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout,parent,false);
        return new CategoriesAdapterViewHolder(createScheduleView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       final String categoryOld = categoryOldList.get(position);
       final CategoriesAdapterViewHolder Cholder = (CategoriesAdapterViewHolder) holder;
       Cholder.categoryTextView.setText("#"+ categoryOld.toString());

       Cholder.categoryTextView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try {
                   ((MainActivity)context).scheduleFragment.keyword = categoryOld.toString();
                   //You are required to fire up fragment methods which will update
                   //Schedule contents based on the keyword content variable
                   //In this case Keyword is checked = hence contains filtered data
               }catch (NumberFormatException ie){
                   ie.printStackTrace();
               }
               Cholder.categoryPressedTextView.setVisibility(View.VISIBLE);
               Cholder.categoryPressedTextView.setText("#"+ categoryOld.toString());
               Cholder.categoryTextView.setVisibility(View.GONE);
           }
       });


        Cholder.categoryPressedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ((MainActivity)context).scheduleFragment.keyword = null;
                     //You are required to fire up fragment methods which will update
                     //Schedule contents based on the keyword content variable
                     //In this case Keyword is unchecked = hence null
                }catch (NumberFormatException ie){
                    ie.printStackTrace();
                }
                Cholder.categoryPressedTextView.setVisibility(View.GONE);
                Cholder.categoryTextView.setVisibility(View.VISIBLE);
            }
        });

    }



    @Override
    public int getItemCount() {
        return categoryOldList.size();
    }



    public class CategoriesAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView categoryTextView,categoryPressedTextView;
        //private ItemClickListener itemClickListener;

            CategoriesAdapterViewHolder(View view){
                super(view);
                categoryTextView = (TextView) view.findViewById(R.id.cs_category_text);
                categoryPressedTextView = (TextView) view.findViewById(R.id.cs_category_text_pressed);
            }

    }





}
