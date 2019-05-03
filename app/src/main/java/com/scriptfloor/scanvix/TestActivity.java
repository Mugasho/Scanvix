package com.scriptfloor.scanvix;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.scriptfloor.scanvix.model.QuestionItem;
import com.scriptfloor.scanvix.model.ReviewItem;
import com.scriptfloor.scanvix.utils.DBHandler;
import com.stepstone.stepper.StepperLayout;
import com.scriptfloor.scanvix.model.AbstractWizardModel;
import com.scriptfloor.scanvix.model.ModelCallbacks;
import com.scriptfloor.scanvix.model.Page;
import com.scriptfloor.scanvix.ui.PageFragmentCallbacks;
import com.scriptfloor.scanvix.ui.ReviewFragment;
import com.scriptfloor.scanvix.ui.StepPagerStrip;
import com.tapadoo.alerter.Alerter;

import junit.framework.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TestActivity extends AppCompatActivity implements PageFragmentCallbacks,
        ReviewFragment.Callbacks,
        ModelCallbacks {

    private StepperLayout mStepperLayout;

    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;

    private boolean mEditingAfterReview;
    private List<ReviewItem> mCurrentReviewItems;

    private AbstractWizardModel mWizardModel = new SandwichWizardModel(this);

    private boolean mConsumePageSelectedEvent;
    private Button mNextButton;
    private Button mPrevButton;

    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        /*mStepperLayout = findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this));*/

        if (savedInstanceState != null) {
            mWizardModel.load(savedInstanceState.getBundle("model"));
        }


        mWizardModel.registerListener(this);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.viewpager);
        mPager.setAdapter(mPagerAdapter);
        mStepPagerStrip = findViewById(R.id.strip);
        mStepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(mPagerAdapter.getCount() - 1, position);
                if (mPager.getCurrentItem() != position) {
                    mPager.setCurrentItem(position);
                }
            }
        });


        mNextButton = findViewById(R.id.next_button);
        mPrevButton = findViewById(R.id.prev_button);

        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mStepPagerStrip.setCurrentPage(position);

                if (mConsumePageSelectedEvent) {
                    mConsumePageSelectedEvent = false;
                    return;
                }

                mEditingAfterReview = false;
                updateBottomBar();
            }
        });

        mNextButton.setOnClickListener(new MyOnClickListener());

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });

        onPageTreeChanged();
        updateBottomBar();
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 = review step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private void updateBottomBar() {
        int position = mPager.getCurrentItem();
        if (position == mCurrentPageSequence.size()) {
            mNextButton.setText(R.string.finish);
            mNextButton.setBackgroundResource(R.drawable.finish_background);
            //mNextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
        } else {
            mNextButton.setText(mEditingAfterReview
                    ? R.string.review
                    : R.string.next);
            mNextButton.setBackgroundResource(R.drawable.selectable_item_background);
            TypedValue v = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
            //mNextButton.setTextAppearance(this, v.resourceId);
            mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());
        }

        mPrevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mWizardModel.save());
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(key)) {
                mConsumePageSelectedEvent = true;
                mEditingAfterReview = true;
                mPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                mPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= mCurrentPageSequence.size()) {
                return new ReviewFragment();
            }

            return mCurrentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            if (mCurrentPageSequence == null) {
                return 0;
            }
            return Math.min(mCutOffPage + 1, mCurrentPageSequence.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }
    }

    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }

    private void showAlert(String Title, String content, int colorInt) {
        Alerter.create(this)
                .setTitle(Title)
                .setText(content)
                .setBackgroundColorRes(colorInt) // or setBackgroundColorInt(Color.CYAN)
                .show();
    }



    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
                double bmi=0; double height=0; double weight=0;
                DBHandler db = new DBHandler(getApplicationContext());
                int highest = 0;int higher=0; int high=0; int lower=0;int lowest=0; int age;
                ArrayList<QuestionItem> questionItems = new ArrayList<>();
                questionItems = db.getQuestionItems();
                for (QuestionItem item : questionItems) {
                    String[] questions = item.getQuestion().split(":", 2);
                    if (questions.length > 1) {
                        String question1 = questions[0];
                        String question2 = questions[1];
                        Log.d("qn1", question1 + "");
                        Log.d("qn2", question2 + "");
                        Log.d("ans", item.getAnswer() + "");

                        String answer = item.getAnswer();

                        /*highest*/
                        if (question2.contains("Are you aware of HPV vaccination programme?") &&
                                answer.contains("Yes")) {
                            Log.d("qn2", item.getAnswer());
                            highest = highest + 1;
                        } else if (question2.contains("Do you use oral contraceptive pills as a family planning method?") &&
                                answer.contains("Yes")) {
                            Log.d("qn6", item.getAnswer());
                            highest = highest + 1;
                        } else if (question1.contains("oral contraceptive pills") &&
                                question2.contains("For how long?") &&
                                answer.contains("For long >5 yrs")) {
                            Log.d("qn6b", item.getAnswer());
                            highest++;
                        }
                        /*higher*/
                        if (question2.contains("How many sexual partners  have you ever had?") &&
                                answer.contains("More than 1")) {
                            higher++;
                        }else if (question1.contains("Yes")&&question2.contains("What was the result?")&&
                                answer.contains("Positive")){
                            higher++;
                        }else if (question2.contains("Which family planning method do you use?")&&
                                answer.contains("natural methods e.g safe days,withdrawal")){
                            higher++;
                        }else if (question2.contains("How many children have you delivered?")&&
                                answer.contains("More than 3")){
                            higher++;
                        }else if (question2.contains("Have you ever been infected with any of the following?")&& !answer.isEmpty()){
                            higher++;
                        }else if (question2.contains("Have you been taking any of the following dugs ? glucocorticoid/ steroids")
                                && !answer.isEmpty()){
                            higher++;
                        }else if (question2.contains("For how long have you taken these drugs?")&&
                                answer.contains("More than 3 months")){
                            higher++;
                        }else if (question2.contains("Does anyone of your family members sister or mother have cervical cancer?")&&
                                answer.contains("Yes")){
                            higher++;
                        }
                        //high

                        if (question2.contains("Do you smoke?") && answer.contains("Yes")) {
                            high++;
                        }else if (question2.contains("At what age were you at your first delivery?")&&
                                answer.contains("Less than 25Yrs")){
                            high++;
                        }else if (question2.contains("Enter your height")&&
                                !answer.isEmpty()){
                            height=Double.parseDouble(answer);
                            height++;
                        }else if (question2.contains("Enter your weight")&&
                                !answer.isEmpty()){
                            weight=Double.parseDouble(answer);
                            bmi=weight/(height*height);
                            high++;
                        }else if(bmi>25){
                            high++;
                        }else if (question2.contains("How often  do you eat fruits and vegetables?")){
                               if( answer.contains("Less than 3")||answer.contains("Never")) {
                                   high++;
                               }
                        }else if (question2.contains("Have you got any organ transplant?")&&
                                answer.contains("Yes")){
                            high++;
                        }else if (question2.contains("Which family planning method do you use?")&&
                                answer.contains("condom")){
                            high++;
                        }else if (question2.contains("How much do you use a day on average?")&&
                                answer.contains("< 2 dollars(@ 3700ugx")){
                            high++;
                        }
                        //lower
                        if (question2.contains("Are you aware of HPV vaccination programme?") && answer.contains("Yes")) {
                            lower++;
                        }else if (question2.contains("Do you smoke?")&& answer.contains("No")){
                            lower++;
                        }else if (question2.contains("Which family planning method do you use?")&& answer.contains("intra-uterine contraceptive device(coil)")){
                            lower++;
                        }else if (question2.contains("Which family planning method do you use?")&& answer.contains("condom")){
                            lower++;
                        }else if (question2.contains("At what age were you at your first delivery?")&&
                                answer.contains("Above 25Yrs")){
                            lower++;
                        }else if (question2.contains("Have you ever screened for cervical cancer?")&&
                                answer.contains("Yes")){
                            lower++;
                        }else if (question2.contains("How many children have you delivered?")&&
                                answer.contains("Less than 3")){
                            lower++;
                        }else if (question2.contains("Have you got any organ transplant?")&&
                                answer.contains("No")){
                            lower++;
                        }else if (question2.contains("Have you ever been infected with any of the following?")&&
                                answer.isEmpty()){
                            lower++;
                        }else if (question2.contains("What was the result?")&&
                                answer.contains("Negative")){
                            lower++;
                        }else if (question2.contains("How much do you use a day on average?")&&
                                answer.contains(">2 dollars(@3700ugx")){
                            lower++;
                        }

                        if (question1.contains("Select your age range") && answer.contains("Less than 13")) {
                            lowest++;
                        }
                    }
                }

                Log.d("Highest", highest + "");
                Log.d("Higher", higher + "");
                Log.d("High", high + "");
                Log.d("lower", lower+ "");
                Log.d("lowest", lowest+ "");
                db.addAnswer(highest,higher,high,lower,lowest);
                if (highest >2) {
                    showAlert("Highest Risk", "You are at highest risk from " + highest + " criteria", R.color.red);
                }else if (higher > 6){
                    showAlert("Higher Risk", "detected " + higher + " criteria for higher risk", R.color.colorAccent);
                }else if (high > 9){
                    showAlert("Higher Risk", "detected " + higher + " criteria for higher risk", R.color.colorAccent);
                }else if (lower > 10){
                    showAlert("Lower Risk", "detected " + lower + " criteria for higher risk", R.color.colorAccent);
                }else if (lowest==1){
                    showAlert("Lowest Risk", "detected " + lowest + " criteria for higher risk", R.color.colorPrimaryDark);
                }

                Intent intent = new Intent();
                intent.putExtra("result", "OK");
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                if (mEditingAfterReview) {
                    mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
                } else {
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                }
            }
        }
    }
}
