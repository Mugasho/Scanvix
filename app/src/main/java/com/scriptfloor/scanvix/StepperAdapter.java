package com.scriptfloor.scanvix;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.scriptfloor.scanvix.fragment.LastFragment;
import com.scriptfloor.scanvix.fragment.StartFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by LINCOLN on 3/3/2019.
 */

public class StepperAdapter extends AbstractFragmentStepAdapter {
    public StepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        Bundle b = new Bundle();
        switch (position) {
            case 0:
                final StartFragment step1 = new StartFragment();
                b.putInt("Child", position);
                step1.setArguments(b);
                return step1;
            case 1:
                final LastFragment step2 = new LastFragment();
                b.putInt("Child", position);
                step2.setArguments(b);
                return step2;
            default:
                return null;
        }
    }

    @Override
    public Step findStep(int position) {
        return super.findStep(position);
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(int position) {
        return super.getViewModel(position);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
