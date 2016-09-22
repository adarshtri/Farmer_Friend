package com.thenewboston.kirusa_test.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.thenewboston.kirusa_test.Adapters.office_adapter;
import com.thenewboston.kirusa_test.ApplicationContext.MyApplication;
import com.thenewboston.kirusa_test.PojoClasses.officers_group;
import com.thenewboston.kirusa_test.R;
import com.thenewboston.kirusa_test.Sqlite.Officer_Group_DbOperation;

import java.util.ArrayList;

public class Officer extends Fragment {

    private RecyclerView recyclerView;
    private office_adapter office_adapter;
    private ArrayList<officers_group> officers_groups = new ArrayList<>();

    private Officer_Group_DbOperation officer_group_dbOperation;
    MyApplication myApplication;
    private Button button ;

    public Officer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_individual, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.groups_officer);
        myApplication = MyApplication.getInstance();
        officer_group_dbOperation = new Officer_Group_DbOperation(myApplication.getApplicationContext());
        button = (Button) view.findViewById(R.id.referesh);
        officers_groups = officer_group_dbOperation.read_group(officer_group_dbOperation);
        office_adapter = new office_adapter(getActivity().getBaseContext(),officers_groups);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(office_adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<officers_group> officers_groups = officer_group_dbOperation.read_group(officer_group_dbOperation);
                office_adapter.setFeed(officers_groups);
            }
        });
        return  view;
    }

    public interface OnFragmentInteractionListener {
    }

}
