package com.example.braincheck;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterNavigationIT {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        onView(withId(R.id.buttonGoToRegister)).perform(click());
        onView(withId(R.id.editRegisterUsername)).perform(typeText("username"));
        onView(withId(R.id.editRegisterPassword)).perform(typeText("password"));
    }

    @Test
    public void testRegisterSuccessNavigatesToExamsList() {
        onView(withId(R.id.buttonRegister)).perform(click());

        ViewInteraction text = onView(withText(
                "Você ainda não possui exames. Clique no botão para começar."
        ));
        text.check(matches(isDisplayed()));
    }





}