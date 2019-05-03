package com.scriptfloor.scanvix.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.LineChartView;
import com.scriptfloor.scanvix.R;
import com.scriptfloor.scanvix.TestActivity;
import com.scriptfloor.scanvix.model.AnswerModel;
import com.scriptfloor.scanvix.utils.DBHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    public static final int REQUEST_CODE = 1;
    DBHandler db;
    ArrayList<AnswerModel> answers;
    TextView txt_reviews;
LineChartView lineChartView;
CardView card1;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        lineChartView=view.findViewById(R.id.linechart);
        txt_reviews=view.findViewById(R.id.txt_reviews);
        card1=view.findViewById(R.id.card1);
        db=new DBHandler(getActivity());
        answers=new ArrayList<>();
        answers=db.getAnswers();
        if(answers.size()!=0) {
            LineSet LineData = new LineSet();
            for (int i = 0; i < answers.size(); i++) {
                AnswerModel answer = answers.get(i);
                float[] arr = {(answer.getHighest() * 100) / 3, (answer.getHigher() * 100) / 8, (answer.getHigh() * 100) / 9, (answer.getLower() * 100) / 11, (answer.getLowest() * 100) / 1};
                LineData.addPoint(answers.get(i).getDate_added(), largest(arr));
            }
            LineData.setColor(Color.parseColor("#53c1bd"))
                    .setFill(Color.parseColor("#3d6c73"))
                    .setGradientFill(new int[]{Color.parseColor("#364d5a"), Color.parseColor("#3f7178")},
                            null);
            LineData.setFill(Color.parseColor("#488bfdff"));
            lineChartView.setAxisColor(Color.parseColor("#189891ff"));
            lineChartView.setYLabels(AxisRenderer.LabelPosition.NONE);
            lineChartView.setYAxis(false);
            lineChartView.addData(LineData);
            lineChartView.show(new Animation(1000));
        }
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), TestActivity.class);
                startActivityForResult(i,REQUEST_CODE);
            }
        });


        txt_reviews.setText(answers.size()+"");
        return view;
    }

    static float largest(float[]arr)
    {
        int i;

        // Initialize maximum element
        float max = arr[0];

        // Traverse array elements from second and
        // compare every element with current max
        for (i = 1; i < arr.length; i++)
            if (arr[i] > max)
                max = arr[i];

        return max;
    }
}
