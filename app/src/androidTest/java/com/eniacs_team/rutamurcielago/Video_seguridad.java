package com.eniacs_team.rutamurcielago;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Video_seguridad {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void video_seguridad() {
        ViewInteraction viewPager = onView(
                allOf(withId(R.id.view_pager), isDisplayed()));
        viewPager.perform(swipeLeft());

        ViewInteraction viewPager2 = onView(
                allOf(withId(R.id.view_pager), isDisplayed()));
        viewPager2.perform(swipeLeft());

        ViewInteraction viewPager3 = onView(
                allOf(withId(R.id.view_pager), isDisplayed()));
        viewPager3.perform(swipeLeft());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.btn_next), withText("Listo"), isDisplayed()));
        appCompatButton.perform(click());

        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.title), withText("Seguridad"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction viewPager4 = onView(
                allOf(withId(R.id.view_pager), isDisplayed()));
        viewPager4.perform(swipeLeft());

        ViewInteraction viewPager5 = onView(
                allOf(withId(R.id.view_pager), isDisplayed()));
        viewPager5.perform(swipeLeft());

        ViewInteraction viewPager6 = onView(
                allOf(withId(R.id.view_pager), isDisplayed()));
        viewPager6.perform(swipeLeft());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.btn_next), withText("Listo"), isDisplayed()));
        appCompatButton2.perform(click());

    }

}
