package com.appbajopruebas.vinilos.ui.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.appbajopruebas.vinilos.MainActivity;
import com.appbajopruebas.vinilos.R;

import org.junit.Rule;
import org.junit.Test;

public class Test_Home {

 @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

 @Test
     public void testButtonClicks() {
     // Verifica que minicard1 está presente y visible
     onView(allOf(withId(R.id.minicard1), isDisplayed()))
             .check(matches(isDisplayed()));

     // Verifica que minicard2 está presente y visible
     onView(allOf(withId(R.id.minicard2), isDisplayed()))
             .check(matches(isDisplayed()));

     // Verifica que minicard3 está presente y visible
     onView(allOf(withId(R.id.minicard3), isDisplayed()))
             .check(matches(isDisplayed()));
     }




 }
