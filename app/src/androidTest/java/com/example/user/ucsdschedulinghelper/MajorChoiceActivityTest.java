package com.example.user.ucsdschedulinghelper;

import android.test.ActivityInstrumentationTestCase2;

import com.example.ucsdschedulinghelper.MajorChoiceActivity;
import com.example.ucsdschedulinghelper.R;
import android.support.test.InstrumentationRegistry;
import org.junit.Before;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by Huayin Zhou on 12/1/15.
 * Senario-based Test 1:
 * Given user is on major selection page,
 * when user selects major Computer Science with Bioinformatics, college Sixth, and click Submit,
 * then button for list of classes and four year plan will show
 * when user clicks button "four year plan",
 * then a list of quarters for four year plan will show;
 * when user clicks tab "Freshman Spring",
 * then a list of classes for Freshman Spring quarter will show;
 * when user clicks tab "Freshman Winter",
 * then a list of classes for Freshman Winter quarter will show;
 * when user clicks tab "Freshman Spring" again,
 * then the list of classes for Freshman Spring quarter will hide.
 *
 * Senario-based Test 2:
 * Given user is on major selection page,
 * when user selects major Computer Science,
 * then the checked box for Computer Science is checked;
 * when user clicks for selection of college,
 * then a list of colleges will show;
 * when user clicks to select Warren College,
 * then Warren College will show as college option;
 * when user clicks button "submit" and clicks button "Course list",
 * then a list of related courses will show;
 * when user clicks on a specific course CSE 12,
 * then a detailed description of CSE 12 will show;
 * when user clicks button cape result,
 * then the cape results of CSE 110 will show;
 * when user scrolls down,
 * then more cape evaluation from previous years will show;
 * when user scrolls right,
 * then more information of cape evaluation will show;
 * when user presses back button,
 * then the detailed description will show again.
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
        // select major, college
        onView(withId(R.id.cs_bio_button)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.college_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Sixth"))).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.college_spinner)).check(matches(withSpinnerText(containsString("Sixth"))));
        // click submit and check if buttons are unable before and enabled after
        onView(withId(R.id.buttonToCourseListView)).check(matches(not(isEnabled())));
        onView(withId(R.id.buttonToCourseListView)).check(matches(not(isEnabled())));
        onView(withId(R.id.buttonSubmitInfo)).perform(click());
        onView(withId(R.id.buttonToCourseListView)).check(matches(isEnabled()));
        onView(withId(R.id.buttonToCourseListView)).check(matches(isEnabled()));
        // goto four year plan
        onView(withId(R.id.buttonToFourYearPlan)).perform(click());
        onView(withId(R.id.expandableListView)).check(matches(isDisplayed()));
        // click Freshman Spring quarter and check if classes show
        onData(allOf(is(instanceOf(String.class)), is("Freshman Spring"))).inAdapterView(withId(R.id.expandableListView))
                .perform(click());
        onView(allOf(withId(R.id.child_txt), withText(startsWith("CSE")))).check(matches(isDisplayed()));
        // click Freshman Winter quarter and check if classes show
        onData(allOf(is(instanceOf(String.class)), is("Freshman Winter"))).inAdapterView(withId(R.id.expandableListView))
                .perform(click());
        onView(allOf(withId(R.id.child_txt), withText("BILD 1"))).check(matches(isDisplayed()));
        // click Freshman Spring quarter and check if classes hide
        onData(allOf(is(instanceOf(String.class)), is("Freshman Spring"))).inAdapterView(withId(R.id.expandableListView))
                .perform(click());
        onView(allOf(withId(R.id.child_txt), withText(startsWith("CSE")))).check(matches(not(withText("CSE 12"))));
    }

    public void test2() {
        // select major, college and submit
        onView(withId(R.id.cs_button)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.college_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Earl Warren"))).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.college_spinner)).check(matches(withSpinnerText(containsString("Earl Warren"))));
        onView(withId(R.id.buttonSubmitInfo)).perform(click());
        // click button course list view and check if course list view is shown
        onView(withId(R.id.buttonToCourseListView)).perform(click());
        onView(withId(R.id.courses_list)).check(matches(isDisplayed()));
        // click CSE 12 and check if detail description shows
        onView(allOf(withId(R.id.list_item_text_title), withText("Basic Data Structures and Object-Oriented Design"))).perform(click());
        onView(withId(R.id.course_detailed_text_description)).check(matches(isDisplayed()));
        // click cape for CSE 12 and check if cape shows
        onView(withId(R.id.button_cape)).perform(click());
        onView(withId(R.id.cape_table)).check(matches(isDisplayed()));
        // scroll down
        onView(withText("S211")).perform(scrollTo()).check(matches(isDisplayed()));
        // scroll right
        onView(withText("Avg Grade Received")).perform(scrollTo()).check(matches(isDisplayed()));
        // press back button
        pressBack();
        onView(withId(R.id.course_detailed_text_description)).check(matches(isDisplayed()));
    }


}
