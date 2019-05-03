package com.scriptfloor.scanvix;


import android.content.Context;

import com.scriptfloor.scanvix.model.AbstractWizardModel;
import com.scriptfloor.scanvix.model.BranchPage;
import com.scriptfloor.scanvix.model.ContentPage;
import com.scriptfloor.scanvix.model.MultipleFixedChoicePage;
import com.scriptfloor.scanvix.model.PageList;
import com.scriptfloor.scanvix.model.SingleFixedChoicePage;

/**
 * Created by LINCOLN on 3/10/2019.
 */

public class SandwichWizardModel extends AbstractWizardModel {
    public SandwichWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList(
                new BranchPage(this, "Select your age range")
                        .addBranch("Less than 13")
                        .addBranch("13 and above",
                                new BranchPage(this, "Have you ever tested for hiv?")
                                        .addBranch("Yes",
                                                new BranchPage(this, "Are you vaccinated?")
                                                        .addBranch("Yes",
                                                                new SingleFixedChoicePage(this, "How many times?")
                                                                        .setChoices("Once", "Twice", "Thrice")
                                                                        .setRequired(true))
                                                        .addBranch("No"),
                                                new BranchPage(this, "Have you ever tested for HIV?")
                                                        .addBranch("Yes",
                                                                new SingleFixedChoicePage(this, "When was the last time?")
                                                                        .setChoices("Less than 3 months ago", "More than 3 months ago"),
                                                                new SingleFixedChoicePage(this, "What was the result?")
                                                                        .setChoices("Negative", "Positive")
                                                                        .setRequired(true))
                                                        .addBranch("No")
                                                        .setRequired(true))
                                        .addBranch("No")
                                        .setRequired(true),
                                new ContentPage(this, "Enter your height"),
                                new ContentPage(this, "Enter your weight"),
                                new SingleFixedChoicePage(this, "Do you smoke?")
                                        .setChoices("Yes", "No")
                                        .setRequired(true),
                                new BranchPage(this, "Do you use a condom as a family planning method?")
                                        .addBranch("Yes",
                                                new SingleFixedChoicePage(this, "How often?")
                                                        .setChoices("Always", "Sometimes")
                                                        .setRequired(true))
                                        .addBranch("No")
                                        .setRequired(true),
                                new BranchPage(this, "Do you use oral contraceptive pills as a family planning method?")
                                        .addBranch("Yes",
                                                new SingleFixedChoicePage(this, "For how long?")
                                                        .setChoices("For long more than 5 yrs", "for only a short time less than 5yrs",
                                                                "I  stopped less than 10yrs ago", " I  stopped more than 10yrs ago")
                                                        .setRequired(true))
                                        .addBranch("No")
                                        .setRequired(true),
                                new BranchPage(this, "Do you use intra-uterine contraceptive device/coil as a family planning method?")
                                        .addBranch("Yes",
                                                new SingleFixedChoicePage(this, "For how long?")
                                                        .setChoices("Less than a year ", "More than a year").setRequired(true))
                                        .addBranch("No")
                                        .setRequired(true),
                                new SingleFixedChoicePage(this, "Do you use natural methods e.g safe days and danger days, withdrawal method as a family planning method?")
                                        .setChoices("Yes", "No"),
                                new MultipleFixedChoicePage(this, "Have you been taking any of the following dugs ? glucocorticoid/ steroids")
                                        .setChoices("None,hydrocortisone", "betamethasone", "budesonide", "dexamethasone", "prednisolone", "methylprednisolone"),
                                new SingleFixedChoicePage(this, "For how long have you taken these drugs?")
                                        .setChoices("More than 3 months ", "Less than 3 months"),
                                new SingleFixedChoicePage(this, "have you been taking chemotherapy drugs like dolorubicin?")
                                        .setChoices("only 1", "More than 1"),
                                new SingleFixedChoicePage(this, "How many sexual partners  have you ever had?")
                                        .setChoices("only 1", "More than 1"),
                                new SingleFixedChoicePage(this, "Does anyone of your family members (sister or mother) have cervical cancer?")
                                        .setChoices("Yes", "No"),
                                new BranchPage(this, "How many children have you delivered?"),
                                new SingleFixedChoicePage(this, "How often  do you eat fruits and vegetables?")
                                        .setChoices("Daily", "More than 3", "Less than 3", "Never")
                                        .setRequired(true),
                                new MultipleFixedChoicePage(this, "Have you ever been infected with any of the following?")
                                        .setChoices("chlamydia infection", "herpes simplex virus type 2( human herpes virus 2)", "syphilis", "Gonorrhea"),
                                new MultipleFixedChoicePage(this, "Do you experience any of the following?")
                                        .setChoices(" bleeding in between your cycles", "bleeding after sexual intercourse",
                                                "post-menopausal bleeding", "watery, pink or foul-smelling discharge from the vagina",
                                                "pelvic pain during intercourse", "pelvic pain any other time of the day"),
                                new BranchPage(this, "Have you ever screened for cervical cancer?")
                                        .addBranch("Yes")
                                        .addBranch("No")
                                        .setRequired(true),
                                new SingleFixedChoicePage(this, "Have you got any organ transplant?")
                                        .setChoices("Yes", "No"),
                                new SingleFixedChoicePage(this, "How much do you use a day on average?")
                                        .setChoices("less than  2 dollars(@ 3700ugx", "More than 2 dollars(@3700ugx"))
                        .setRequired(true)
        );

    }
}
