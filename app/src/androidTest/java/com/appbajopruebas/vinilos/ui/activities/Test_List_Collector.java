package com.appbajopruebas.vinilos.ui.activities;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;

import com.appbajopruebas.vinilos.ColleccionistaActivity;
import com.appbajopruebas.vinilos.R;
import com.appbajopruebas.vinilos.VinilosActivity;

import org.junit.Before;
import org.junit.Test;

public class Test_List_Collector {

    @Before
    public void launchActivity() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ColleccionistaActivity.class);
        ActivityScenario.launch(intent);
    }



 @Test
     public void testFragmento() {

     try {
         Thread.sleep(2000);
     } catch (InterruptedException e) {
         e.printStackTrace();
     }
     // Verifica que el item está presente y visible
     onView(withId(R.id.collectorRv))
             .check(matches(isDisplayed()));

     // Hace scroll
     onView(withId(R.id.collectorRv))
             .perform(swipeUp());

    }

    @Test
    public void testBackButton() {
        // Presiona el botón de retroceso
        onView(withContentDescription("Navigate up")).perform(click());

        // Verifica que VinilosActivity está cerrada (no visible)
        Espresso.onView(ViewMatchers.withId(R.id.minicard1))
                .check(matches(isDisplayed()));
    }
 }
