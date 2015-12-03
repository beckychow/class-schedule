package com.example.user.ucsdschedulinghelper;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
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
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isSelected;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.Espresso.pressBack;
import static org.hamcrest.CoreMatchers.not;


/**
 * Created by bharatsubramaniam on 12/2/15.
 */

/**
 *
 * As a user, I want to select my major as computer engineering and my college as Muir, add CSE 7 to my classes completed,
 * go back to the list and then select CSE 7 again so that I can see if the class stays added.
 *
 * Scenario - based Test #3
 * Given : User opens the app
 * When : User selects his major as Computer Engineering and college as Muir
 * Then : The user has the option to view the four year plan and the list of courses.
 * When : The user selects the list of courses button
 * Then : The full list of required CSE courses for his major appears.
 * When : The user finds CSE 7 in the list and clicks on it
 * Then : The course description for CSE 7 opens, showing him a brief description, prerequisites and number of units.
 * When : The user clicks on the add to completed button
 * Then : The course is added to completion, the button changes to "Remove from Completed" and the button turns green.
 * When : The user clicks on the back button
 * Then : The list of CSE courses appears again.
 * When : The user clicks on CSE 7 again
 * Then : The course description for CSE 7 reopens.
 * When : The user checks if the class is still added
 * Then : The button is still green and the class is still added.
 * When : The user clicks on the remove from completed button
 * Then : The course is removed from completion, and the button is reset.
 */


public class CourseAddedCheckTest extends ActivityInstrumentationTestCase2<MajorChoiceActivity> {

    private MajorChoiceActivity majorChoiceActivity;

    public CourseAddedCheckTest() {
        super(MajorChoiceActivity.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        majorChoiceActivity = getActivity();
    }


    public void test1() {

        /* Selecting major as Computer Engineering and college as Muir */
        onView(withId(R.id.comp_eng_button)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.college_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Muir"))).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.college_spinner)).check(matches(withSpinnerText(containsString("Muir"))));
        onView(withId(R.id.buttonSubmitInfo)).perform(click());


        /* Viewing the list of courses and selecting CSE 7 */
        onView(withId(R.id.buttonToCourseListView)).perform(click());
        onView(allOf(withId(R.id.list_item_text_title), withText("Introduction to Programming with Matlab"))).perform(click());
        onView(withId(R.id.course_detailed_text_description)).check(matches(isDisplayed()));

        /* Adding the class to completed */
        onView(withId(R.id.course_detailed_button_completion)).perform(click());

        /* Going back to the list of courses page */
        pressBack();

        /* Selecting CSE 7 again */
        onView(allOf(withId(R.id.list_item_text_title), withText("Introduction to Programming with Matlab"))).perform(click());


        /* Checking if the class is still added */
        onView(withId(R.id.course_detailed_button_completion)).check(matches(not(isSelected())));

        /* Resetting button */
        onView(withId(R.id.course_detailed_button_completion)).perform(click());
    }
}