package com.example.user.ucsdschedulinghelper;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.DrawerMatchers;
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
 * Modified by SKE on 12/4/15.
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
 * When : The user clicks on the "Add to interested" button
 * Then : The course is added to interested, the button changes to "Remove from interested".
 * When : The user clicks on the back button
 * Then : The list of CSE courses appears again.
 * When : The user is at the course list page
 * Then : Navigation drawer is available.
 * When : The user clicks on CSE 7 again
 * Then : The course description for CSE 7 opens and the class is still added to interested.
 * When : The user clicks on the "Add to completed" button
 * Then : The course is added to completed, removed from interested, "Remove from interested" button is reset and disabled.
 * When : The user clicks on the "Remove from completed" button
 * Then : The button changes to "Add to completed", and "Add to interested" button is enabled.
 */


public class CourseActionsAndNavigationTest extends ActivityInstrumentationTestCase2<MajorChoiceActivity> {

    private MajorChoiceActivity majorChoiceActivity;

    public CourseActionsAndNavigationTest() {
        super(MajorChoiceActivity.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        majorChoiceActivity = getActivity();
    }


    public void test1() {
        String college = "John Muir";
        String courseTitle = "Introduction to Programming with Matlab";
        String addInt = "Add to interested";
        String remInt = "Remove from interested";
        String addCom = "Add to completed";
        String remCom = "Remove from completed";

        /* Selecting major as Computer Engineering and college as Muir */
        onView(withId(R.id.comp_eng_button)).perform(click()).check(matches(isChecked()));
        onView(withId(R.id.college_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(college))).perform(click()).check(matches(isDisplayed()));
        onView(withId(R.id.college_spinner)).check(matches(withSpinnerText(containsString(college))));
        onView(withId(R.id.buttonSubmitInfo)).perform(click());

        /* View the list of courses and selecting CSE 7 */
        onView(withId(R.id.buttonToCourseListView)).perform(click());
        onView(allOf(withId(R.id.list_item_text_title), withText(courseTitle))).perform(click());
        onView(withId(R.id.course_detailed_text_description)).check(matches(isDisplayed()));

        /* Add the class to interested */
        onView(withId(R.id.course_detailed_button_interested)).perform(click());
        onView(withId(R.id.course_detailed_button_interested)).check(matches(is(withText(remInt))));

        /* Go back to the list of courses page */
        pressBack();

        /* Check the availability of the navigation drawer */
        onView(withId(R.id.drawer_layout)).
                perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).
                check(matches(DrawerMatchers.isOpen()));
        onView(withId(R.id.drawer_layout)).
                perform(DrawerActions.close());
        onView(withId(R.id.drawer_layout)).
                check(matches(DrawerMatchers.isClosed()));

        /* Select CSE 7 again and check if it is still added to interested */
        onView(allOf(withId(R.id.list_item_text_title), withText(courseTitle))).perform(click());
        onView(withId(R.id.course_detailed_button_interested)).check(matches(is(withText(remInt))));

        /* Add to completed and check the associated button */
        onView(withId(R.id.course_detailed_button_completion)).perform(click());
        onView(withId(R.id.course_detailed_button_completion)).check(matches(is(withText(remCom))));

        /* Check if 'interested' button has annulled its state and is disabled */
        onView(withId(R.id.course_detailed_button_interested)).check(matches(is(withText(addInt))));
        onView(withId(R.id.course_detailed_button_interested)).check(matches(not(isEnabled())));

        /* Remove from completed */
        onView(withId(R.id.course_detailed_button_completion)).perform(click());

        /* Check if both buttons are in the default state and are enabled */
        onView(withId(R.id.course_detailed_button_completion)).check(matches(isEnabled()));
        onView(withId(R.id.course_detailed_button_interested)).check(matches(isEnabled()));
        onView(withId(R.id.course_detailed_button_completion)).check(matches(is(withText(addCom))));
        onView(withId(R.id.course_detailed_button_interested)).check(matches(is(withText(addInt))));
    }
}