package com.scriptfloor.scanvix.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scriptfloor.scanvix.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import me.riddhimanadib.formmaster.FormBuilder;
import me.riddhimanadib.formmaster.model.BaseFormElement;
import me.riddhimanadib.formmaster.model.FormElementPickerDate;
import me.riddhimanadib.formmaster.model.FormElementPickerMulti;
import me.riddhimanadib.formmaster.model.FormElementPickerSingle;
import me.riddhimanadib.formmaster.model.FormElementPickerTime;
import me.riddhimanadib.formmaster.model.FormElementSwitch;
import me.riddhimanadib.formmaster.model.FormElementTextEmail;
import me.riddhimanadib.formmaster.model.FormElementTextMultiLine;
import me.riddhimanadib.formmaster.model.FormElementTextNumber;
import me.riddhimanadib.formmaster.model.FormElementTextPassword;
import me.riddhimanadib.formmaster.model.FormElementTextPhone;
import me.riddhimanadib.formmaster.model.FormElementTextSingleLine;
import me.riddhimanadib.formmaster.model.FormHeader;


public class StartFragment extends Fragment implements Step {
    RecyclerView mRecyclerView;
    FormBuilder mFormBuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start, container, false);
        // initialize variables
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mFormBuilder = new FormBuilder(getContext(), mRecyclerView);
        //initialize your UI
        // declare form elements
        FormHeader header1 = FormHeader.createInstance("Are you:");
        FormElementTextNumber element11 = FormElementTextNumber.createInstance().setTitle("Your Age?").setHint("13-80");
        FormElementSwitch element12 = FormElementSwitch.createInstance().setTitle("Aware of HPV vaccination").setSwitchTexts("Yes", "No");
        FormElementSwitch element13 = FormElementSwitch.createInstance().setTitle("Tested for HIV").setSwitchTexts("Yes", "No");


        FormHeader header2 = FormHeader.createInstance("Family Info");
        FormElementTextSingleLine element21 = FormElementTextSingleLine.createInstance().setTitle("Location").setValue("Dhaka");
        FormElementTextMultiLine element22 = FormElementTextMultiLine.createInstance().setTitle("Address");
        FormElementTextNumber element23 = FormElementTextNumber.createInstance().setTitle("Zip Code").setValue("1000");

        FormHeader header3 = FormHeader.createInstance("Schedule");
        FormElementPickerDate element31 = FormElementPickerDate.createInstance().setTitle("Date").setDateFormat("MMM dd, yyyy");
        FormElementPickerTime element32 = FormElementPickerTime.createInstance().setTitle("Time").setTimeFormat("KK hh");
        FormElementTextPassword element33 = FormElementTextPassword.createInstance().setTitle("Password").setValue("abcd1234");

        FormHeader header4 = FormHeader.createInstance("Preferred Items");
        List<String> fruits = new ArrayList<>();
        fruits.add("Banana");
        fruits.add("Orange");
        fruits.add("Mango");
        fruits.add("Guava");
        FormElementPickerSingle element41 = FormElementPickerSingle.createInstance().setTitle("Single Item").setOptions(fruits).setPickerTitle("Pick any item");
        FormElementPickerMulti element42 = FormElementPickerMulti.createInstance().setTitle("Multi Items").setOptions(fruits).setPickerTitle("Pick one or more").setNegativeText("reset");
        FormElementSwitch element43 = FormElementSwitch.createInstance().setTitle("Frozen?").setSwitchTexts("Yes", "No");

        List<BaseFormElement> formItems = new ArrayList<>();
        formItems.add(header1);
        formItems.add(element11);
        formItems.add(element12);
        formItems.add(element13);
        formItems.add(header2);
        formItems.add(element21);
        formItems.add(element22);
        formItems.add(element23);
        formItems.add(header3);
        formItems.add(element31);
        formItems.add(element32);
        formItems.add(element33);
        formItems.add(header4);
        formItems.add(element41);
        formItems.add(element42);
        formItems.add(element43);
        mFormBuilder.addFormElements(formItems);

        return v;
    }

    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }
}
