package com.example.user.ucsdschedulinghelper;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.text.Spannable;
import android.text.SpannableString;

import com.example.ucsdschedulinghelper.MajorChoiceActivity;
import com.example.ucsdschedulinghelper.R;

import org.junit.Before;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.Espresso.pressBack;



/**
 * Created by bharatsubramaniam on 12/2/15.
 */

/**
 *
 * As a user, I want to select my major as computer science and my college as Warren, look at the list of courses
 * and view CSE 20, so that I can take a look at the CAPE evaluations for that class.
 *
 * Scenario - based Test #2
 * Given : User opens the app
 * When : User selects his major as Computer Science and college as Warren
 * Then : The user has the option to view the four year plan and the list of courses.
 * When : The user selects the list of courses button
 * Then : The full list of required CSE courses for his major appears.
 * When : The user finds CSE 20 in the list and clicks on it
 * Then : The course description for CSE 20 opens, showing him a brief description, prerequisites and number of units.
 * When : The user clicks on the add to completed button
 * Then : The course is added to completion, the button changes to "Remove from Completed" and the button turns green.
 * When : The user clicks on the remove from completed button
 * Then : The course is removed from completion, and the button is reset.
 * When : The user clicks on the Cape button
 * Then : The Cape evaluations for the class are visible to him.
 */


public class MajorChoiceListViewTest extends ActivityInstrumentationTestCase2<MajorChoiceActivity> {

    private MajorChoiceActivity majorChoiceActivity;

    public MajorChoiceListViewTest() {
        super(MajorChoiceActivity.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        majorChoiceActivity = getActivity();
    }


    public void test1() {

        /* Selecting major as Computer Science and college as Warren */
        onView(withId(R.id.cs_button)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.college_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Earl Warren"))).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.college_spinner)).check(matches(withSpinnerText(containsString("Earl Warren"))));
        onView(withId(R.id.buttonSubmitInfo)).perform(click());


        /* Viewing the list of courses and selecting CSE 20 */
        onView(withId(R.id.buttonToCourseListView)).perform(click());
        onView(allOf(withId(R.id.list_item_text_title), withText("Discrete Mathematics"))).perform(click());
        onView(withId(R.id.course_detailed_text_description)).check(matches(isDisplayed()));

        /* Adding and removing the class from completed */
        onView(withId(R.id.course_detailed_button_completion)).perform(click());
        onView(withId(R.id.course_detailed_button_completion)).perform(click());

        /* Checking the CAPE evaluations for the class */
        onView(withId(R.id.button_cape)).perform(click());

    }
}