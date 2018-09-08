package com.cardeanutrition.lishe360app.fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cardeanutrition.lishe360app.Feedlist.adapter.AdapterListFeed;
import com.cardeanutrition.lishe360app.Feedlist.model.Feed;
import com.cardeanutrition.lishe360app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements AdapterListFeed.OnClickItemFeed, View.OnClickListener {

    public static final String FEED_ROOT = "feed";
    public static final int GET_PHOTO = 11;

    private RecyclerView recyclerView;
    private ProgressBar mProgressBar;

    private List<Feed> mList;
    private SwipeRefreshLayout swipeContainer;
    //for testing views purposes only
    private Feed testFeed;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.main_menu, menu);
       // super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_blank, container, false);

        mProgressBar = (ProgressBar)v.findViewById(R.id.pb);
        swipeContainer = (SwipeRefreshLayout)v.findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView)v.findViewById(R.id.rv_list_feed);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //for testing purposes
        testFeed = new Feed();
        testFeed.setMaintitle("Children eating psychology");
        testFeed.setDescription("yes the third line's first parameter is for the default color and the second parameter will set the mentioned color to the selected tab. ie for the non-selected tab the color will be #727272 and for selected tab the color will be #ffffff ");
        testFeed.setTime("4 Sept 2018");
        testFeed.setUrl("https://stackoverflow.com/questions/37000088/how-to-change-selected-tab-text-color-using-tablayout-from-code-in-android");
        testFeed.setPhoto("https://static01.nyt.com/images/2018/05/30/upshot/30up-pewdivide-1-inyt/up-pewdivide-urban-facebookJumbo.jpg");




        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (hasInternetConnection()) {
                    retrieveData();
                    swipeContainer.setRefreshing(false);


                } else {

                    swipeContainer.setRefreshing(false);
                    showSnackBar(R.string.internet_connection_failed);

                }
            }

        });
        return v;
    }



    @Override
    public void onResume() {
        super.onResume();

        retrieveData();

    }

    /**
     * Retrieve Data from Firebase
     */
    private void retrieveData(){
        showProgress(true);
        mList = new ArrayList<>();
        DatabaseReference feedReference = FirebaseDatabase.getInstance().getReference().child(FEED_ROOT).orderByChild("time").getRef();
        feedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Feed feed = snapshot.getValue(Feed.class );
                    mList.add(feed);
                }
                mList.add(testFeed);
                initRecyclerView(mList);
                showProgress(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    /**
     * Init data in RecyclerView
     * @param list
     */
    private void initRecyclerView(List<Feed> list){
        recyclerView.setAdapter( new AdapterListFeed(list,this));
    }


    private void showProgress(boolean b){
        mProgressBar.setVisibility( b ? View.VISIBLE:View.GONE );
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onClickItemFeed(int position, View view) {

    }



    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }




    public void showSnackBar(int message) {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}

