package com.example.lab4

import android.content.pm.ActivityInfo
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {
    @get:Rule
    val activityRule2 = ActivityTestRule(Activity1::class.java)


    private fun first() {
        onView(withId(R.id.button1)).check(matches(isDisplayed()))
        onView(withId(R.id.button2)).check(doesNotExist())
        onView(withId(R.id.button3)).check(doesNotExist())
        onView(withId(R.id.button4)).check(doesNotExist())
        onView(withId(R.id.button5)).check(doesNotExist())
    }

    private fun second() {
        onView(withId(R.id.button1)).check(doesNotExist())
        onView(withId(R.id.button2)).check(matches(isDisplayed()))
        onView(withId(R.id.button3)).check(matches(isDisplayed()))
        onView(withId(R.id.button4)).check(doesNotExist())
        onView(withId(R.id.button5)).check(doesNotExist())
    }

    private fun third() {
        onView(withId(R.id.button1)).check(doesNotExist())
        onView(withId(R.id.button2)).check(doesNotExist())
        onView(withId(R.id.button3)).check(doesNotExist())
        onView(withId(R.id.button4)).check(matches(isDisplayed()))
        onView(withId(R.id.button5)).check(matches(isDisplayed()))
    }

    @Test
    fun defaultNavigationTest() {
        first()
        onView(withId(R.id.button1)).perform(click())
        second()
        onView(withId(R.id.button2)).perform(click())
        first()
        onView(withId(R.id.button1)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        third()
        onView(withId(R.id.button5)).perform(click())
        second()
        onView(withId(R.id.button2)).perform(click())
        first()
    }

    @Test
    fun destroyingTaskTest() {
        onView(withId(R.id.button1)).perform(click())
        onView(withId(R.id.button3)).perform(click())
        third()
        onView(withId(R.id.button4)).perform(click())
        onView(withId(R.id.button1)).perform(click())
        onView(withId(R.id.button2)).perform(click())
        first()
        try {
            pressBack()
        } catch (e: NoActivityResumedException) {
        }
        assertTrue(activityRule2.activity.isDestroyed)
    }

    @Test
    fun landscapeTest() {
        first()
        onView(withId(R.id.button1)).perform(click())
        activityRule2.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        second()
        onView(withId(R.id.button2)).perform(click())
        defaultNavigationTest()
        destroyingTaskTest()
    }
}