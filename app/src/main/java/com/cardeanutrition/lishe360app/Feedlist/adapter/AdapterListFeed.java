package com.cardeanutrition.lishe360app.Feedlist.adapter;


import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cardeanutrition.lishe360app.Application;
import com.cardeanutrition.lishe360app.Feedlist.model.Feed;
import com.cardeanutrition.lishe360app.Feedlist.view.widget.CustomImageView;
import com.cardeanutrition.lishe360app.R;

import java.util.List;


public class AdapterListFeed extends RecyclerView.Adapter<AdapterListFeed.MyViewHolder>{

    private List<Feed> mList;
    private OnClickItemFeed onClickItemFeed;


    public AdapterListFeed(List<Feed> mList, OnClickItemFeed onClickItemFeed) {
        this.mList = mList;
        this.onClickItemFeed = onClickItemFeed;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_feed,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Feed feed = mList.get(position);

        holder.setMaintitle(feed.getMaintitle());
        holder.setDescription( feed.getDescription() );
        holder.setTime(feed.getTime());
        holder.setUrl(feed.getUrl());
        holder.setPhoto(feed.getPhoto());

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView profile,ivLike;
        private CustomImageView ivContent;
        private TextView Maintitle,time,description,url;
        private ImageView feedImageView;
        ImageLoader imageLoader = Application.getInstance().getImageLoader();

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            //profile = (ImageView)itemView.findViewById( R.id.profile );
            Maintitle = (TextView)itemView.findViewById( R.id.Maintitle );
            time = (TextView)itemView.findViewById( R.id.time );
            description = (TextView)itemView.findViewById( R.id.description);
            url=(TextView) itemView.findViewById(R.id.txtUrl);
            feedImageView =  itemView
                    .findViewById(R.id.feedImage1);



        }

        public void setProfile(String url){
            if (profile == null)return;
            if (url.equals("default_uri")){
                Glide.with(profile.getContext())
                        .load(R.mipmap.home)
                        .centerCrop()
                        .transform(new CircleTransform(profile.getContext()))
                        .override(50,50)
                        .into( profile );
            }else{
                Glide.with(profile.getContext())
                        .load( url )
                        .centerCrop()
                        .transform(new CircleTransform(profile.getContext()))
                        .override(50,50)
                        .into(profile);
            }
        }


        public void setMaintitle(String text){
            if (Maintitle == null)return;
            Maintitle.setText( text );
        }

        public void setTime(String text){
            if (time == null)return;
            time.setText(text);
        }

        public void setDescription(String text){
            if (description == null)return;
            description.setText( text );
        }


        public void setUrl(String text) {
            // Checking for null feed url
            if (text != null) {
                url.setText(Html.fromHtml("<a href=\"" + text+ "\">"
                        + text + "</a> "));

                // Making url clickable
                url.setMovementMethod(LinkMovementMethod.getInstance());
                url.setVisibility(View.VISIBLE);
            } else {
                // url is null, remove from the view
                url.setVisibility(View.GONE);
            }
        }
         public void setPhoto(String urls) {
            if (urls==null)return;
            Glide.with(feedImageView.getContext())
                    .load(urls)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(feedImageView);
         }


        @Override
        public void onClick(View view) {
            if (onClickItemFeed != null){
                onClickItemFeed.onClickItemFeed(getAdapterPosition(),view);
            }
        }
    }

    /**
     * Click item list
     */
    public interface OnClickItemFeed{
        void onClickItemFeed(int position, View view);
    }

}
