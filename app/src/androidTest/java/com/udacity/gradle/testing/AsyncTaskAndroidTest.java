package com.udacity.gradle.testing;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.udacity.gradle.builditbigger.MainActivity;
import com.udacity.gradle.builditbigger.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class AsyncTaskAndroidTest{

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule1=new ActivityTestRule<MainActivity>(MainActivity.class);
    @Test
    public void testSomeAsynTask () throws Throwable {
        final CountDownLatch signal = new CountDownLatch(1);
        onView(withId(R.id.tellJokeButton)).perform(scrollTo(),click());
        signal.await(30, TimeUnit.SECONDS);
        onView(withId(com.udacity.jokeviewer.R.id.jokeContentView)).check(matches(not(withText(""))));
    }
}
