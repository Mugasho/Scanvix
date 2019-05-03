package com.scriptfloor.scanvix.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.scriptfloor.scanvix.R;
import com.scriptfloor.scanvix.TestActivity;
import com.scriptfloor.scanvix.model.AnswerModel;
import com.scriptfloor.scanvix.utils.DBHandler;
import com.scriptfloor.scanvix.utils.ReviewAdapter;

import java.util.ArrayList;

public class ResultFragment extends Fragment {

    FloatingActionButton fab;
    ListView recyclerView;
    DBHandler db;
    ArrayList<AnswerModel> answers;
    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_result, container, false);
        recyclerView=v.findViewById(R.id.result_list);
        db=new DBHandler(getActivity());
        answers=new ArrayList<>();
        answers=db.getAnswers();
        if(answers.size()!=0) {
            recyclerView.setAdapter(new ReviewAdapter(getActivity(), answers));
        }
        fab= v.findViewById(R.id.fab_result);
        fab.setOnClickListener(onClickListener);
        return v;
    }

    View.OnClickListener onClickListener=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent i=new Intent(getActivity(), TestActivity.class);
            startActivity(i);
        }
    };
}
