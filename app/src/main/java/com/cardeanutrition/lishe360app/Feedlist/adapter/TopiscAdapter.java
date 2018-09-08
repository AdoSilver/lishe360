package com.cardeanutrition.lishe360app.Feedlist.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cardeanutrition.lishe360app.Feedlist.model.Topics;
import com.cardeanutrition.lishe360app.R;
import com.cardeanutrition.lishe360app.services.SquareImageViews;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopiscAdapter extends RecyclerView.Adapter<TopiscAdapter.ViewHolder> implements View.OnClickListener  {

    private List<Topics> items;
    private OnItemClickListener onItemClickListener;
    private Context mcontext;


    public TopiscAdapter(Context context, List<Topics> items) {
        this.items = items;
        this.mcontext = context;
    }

    public TopiscAdapter(List<Topics> items) {
        this.items = items;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_item_timetable,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Topics item = items.get(position);

        holder.description.setText(item.getDescption());
        holder.time.setText(item.getTime());
        holder.Maintitle.setText(item.getMaintitle());
        holder.imageView.setImageBitmap(null);
        Picasso.with(holder.imageView.getContext()) //
                .load(item.getPhoto()) //
                .fit().centerInside()
                .into(holder.imageView);
        holder.itemView.setTag(item);

    }

    @Override
    public int getItemCount() {

       return (null!=items ? items.size():0);
    }

    public void filter (final String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                items.clear();
                if (TextUtils.isEmpty(text)){
                    items.addAll(items);

                }else {
                    for (Topics item : items){
                        if (item.getMaintitle().toLowerCase().contains(text.toLowerCase())||
                                item.getDescption().toLowerCase().contains(text.toLowerCase())) {
                                items.add(item);
                        }
                    }
                }

                ((Activity)mcontext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }



    @Override
    public void onClick(final View v) {
        onItemClickListener.onItemClick(v, (Topics) v.getTag());
    }


    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Maintitle, time, description;
        private SquareImageViews imageView;


        public ViewHolder(View itemView) {
            super(itemView);

            Maintitle = (TextView) itemView.findViewById(R.id.Maintitle);
            time = (TextView) itemView.findViewById(R.id.timeposted);
            description = (TextView) itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.pictmasom);
        }

    }

    public interface OnItemClickListener {

        void onItemClick(View view, Topics viewModel);

    }

}
