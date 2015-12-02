package com.example.user.ucsdschedulinghelper;

import android.test.ActivityInstrumentationTestCase2;

import com.example.ucsdschedulinghelper.MajorChoiceActivity;
import com.example.ucsdschedulinghelper.R;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by Huayin Zhou on 12/1/15.
 * Senario-Based Test 1:
 * Given user is on the major selection page,
 * when user selects major Computer Science with Bioinformatics,
 * then the checked box for this major is checkedl
 * when user clicks for selection of college,
 * then a list of colleges will show;
 * when user clicks to select Sixth college,
 * then Sixth college will show as the college option;
 * when user clicks button "submit" and clicks clicks "four year plan",
 * then a list of quarters for four year plan will show;
 * when user clicks tab "Freshman Spring",
 * then a list of classes for Freshman Spring quarter will show.
 */

public class MajorChoiceActivityTest extends ActivityInstrumentationTestCase2<MajorChoiceActivity>{

    private MajorChoiceActivity majorChoiceActivity;

    public MajorChoiceActivityTest() {
        super(MajorChoiceActivity.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        majorChoiceActivity = getActivity();
    }


    public void test1() {
        onView(withId(R.id.cs_bio_button)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.college_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Sixth"))).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.college_spinner)).check(matches(withSpinnerText(containsString("Sixth"))));
        onView(withId(R.id.buttonSubmitInfo)).perform(click());
        onView(withId(R.id.buttonToFourYearPlan)).perform(click());
        onView(withId(R.id.expandableListView)).check(matches(isDisplayed()));
        onData(allOf(is(instanceOf(String.class)), is("Freshman Spring"))).inAdapterView(withId(R.id.expandableListView))
                .perform(click()).check(matches(isDisplayed()));
    }


}
