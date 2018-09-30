package com.cardeanutrition.lishe360app.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cardeanutrition.lishe360app.Feedlist.adapter.TopiscAdapter;
import com.cardeanutrition.lishe360app.Feedlist.model.Topics;
import com.cardeanutrition.lishe360app.R;
import com.cardeanutrition.lishe360app.activities.DetailsActivity;
import com.cardeanutrition.lishe360app.activities.LoginActivity;
import com.cardeanutrition.lishe360app.enums.ProfileStatus;
import com.cardeanutrition.lishe360app.managers.ProfileManager;
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
public class ScheduleFragment extends Fragment implements TopiscAdapter.OnItemClickListener {
    private static final String TAG = ScheduleFragment.class.getSimpleName();
    public static final String FEED_ROOT = "Topics";
    public static final int GET_PHOTO = 11;

    private RecyclerView recyclerView;
    private ProgressBar mProgressBar;
    private List<Topics> mList;
    private SwipeRefreshLayout swipeContainer;
    private ProfileManager profileManager;
    List<String> selected_categories;
    RecyclerView categoriesRecyclerView;
    RecyclerView.LayoutManager categoriesLayoutManager;
    categoriesRecyclerAdapter categoriesRecyclerAdapter;

    public String keyword = null;


    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.masomo, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.createSomo:
               // showChangeLangDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);


        mProgressBar = (ProgressBar) view.findViewById(R.id.pb);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        recyclerView = (RecyclerView) view.findViewById(R.id.topicsrecycleview);
        categoriesRecyclerView = (RecyclerView) view.findViewById(R.id.cs_category_recycler_view);
        categoriesRecyclerView.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        profileManager = ProfileManager.getInstance(getActivity());

        selected_categories = new ArrayList<>();

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        categoriesLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        categoriesRecyclerView.setLayoutManager(categoriesLayoutManager);
        initializingCategories();

        categoriesRecyclerAdapter = new categoriesRecyclerAdapter(getActivity(),selected_categories);
        categoriesRecyclerView.setAdapter(categoriesRecyclerAdapter);
        categoriesRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //nameEdit.clearFocus();
                return false;
            }
        });

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

        return view;
    }




    @Override
    public void onResume() {
        super.onResume();

        retrieveData();

    }

    /**
     * Retrieve Data from Firebase
     */
    private void retrieveData() {
        showProgress(true);
        mList = new ArrayList<>();
        DatabaseReference feedReference = FirebaseDatabase.getInstance().getReference().child(FEED_ROOT).orderByChild("time").getRef();
        feedReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mList.clear();
                Log.d("kisanga",dataSnapshot.toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Topics topics = snapshot.getValue(Topics.class);
                    mList.add(topics);
                }
                setTopiscAdapter(recyclerView);
                showProgress(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    private void setTopiscAdapter(RecyclerView recyclerView) {
        TopiscAdapter adapter = new TopiscAdapter(mList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }


    private void showProgress(boolean b) {
        mProgressBar.setVisibility(b ? View.VISIBLE : View.GONE);
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

    @Override
    public void onItemClick(View view, Topics viewModel) {

        ProfileStatus profileStatus = profileManager.checkProfile();

        if (profileStatus.equals(ProfileStatus.PROFILE_CREATED)) {
            DetailsActivity.navigate(getActivity(), viewModel);
        } else {
            doAuthorization(profileStatus);
        }

    }

    public void doAuthorization(ProfileStatus status) {
        if (status.equals(ProfileStatus.NOT_AUTHORIZED) || status.equals(ProfileStatus.NO_PROFILE)) {
            startLoginActivity();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton("Endelea", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();

        b.show();

        //for negative side button
        b.getButton(b.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.primary));
//for positive side button
        b.getButton(b.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.primary));

    }


    private void initializingCategories(){
        String[] categoriesNames = {"chakula","usafi","cliniki", "afya", "chanjo","madawa"};
        for(int i=1; i < categoriesNames.length+1; i++){
            selected_categories.add(categoriesNames[i-1]);
        }
    }


}
