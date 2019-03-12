package com.example.europeesaanrijdingsformulier

import android.app.DatePickerDialog
import android.app.Instrumentation
import android.content.Context
import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.withSpinnerText
import android.support.v7.widget.RecyclerView
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.CoreMatchers.containsString
import android.preference.PreferenceManager
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.content.Intent






class MainActivityTest{
    @Rule @JvmField
    val mActivityRule = ActivityTestRule(MainActivity::class.java)

    private var firstName = "Sander"
    private var lastName = "Beazar"
    private var email = "sander.beazar@gmail.com"

    private var licenseType = "B"
    private var licenseNumber = "826419573"
   // private var licenseExpiringDate = "24/03/2022"

    private var vehicleType = "Auto"
    private var brand = "Mercedes"
    private var model = "CLA"
    private var licensePlate = "1-ABC-456"
    private var country = "Belgium"

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getTargetContext()
        var sharedPreferences = context.getSharedPreferences(R.string.preferences_profile.toString(), Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().commit()

        Espresso.onView(ViewMatchers.withId(R.id.button_home_toProfile)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.cardview1_profile_summary)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_info_firstname)).perform(ViewActions.typeText(firstName))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_info_lastname)).perform(ViewActions.typeText(lastName))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_info_email)).perform(ViewActions.typeText(email))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.button_profile_info_confirm)).perform(ViewActions.click())
    }

    @Test
    fun testProfileInfo(){
        Espresso.onView(ViewMatchers.withId(R.id.cardview1_profile_summary)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_info_firstname)).check(matches(withText(firstName)))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_info_lastname)).check(matches(withText(lastName)))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_info_email)).check(matches(withText(email)))
    }

    @Test
    fun testProfileLicense(){
        Espresso.onView(ViewMatchers.withId(R.id.cardview2_profile_summary)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.spinner_profile_license_category)).perform(ViewActions.click())
        onData(anything()).atPosition(4).perform(ViewActions.click());
       // Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_license_expires)).perform(DatePicker.setDate(2020,3,23)))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_license_licenseNumber)).perform(ViewActions.typeText(licenseNumber))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.button_profile_license_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.cardview2_profile_summary)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.spinner_profile_license_category)).check(matches(withSpinnerText(
            containsString(licenseType))))
       // Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_license_expires)).check(matches(withText(licenseExpiringDate)))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_license_licenseNumber)).check(matches(withText(licenseNumber  )))
    }

    @Test
    fun testProfileVehicle() {
        Espresso.onView(ViewMatchers.withId(R.id.cardview3_profile_summary)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.button_vehicle_list_add)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.spinner_vehicle_detail_type)).perform(ViewActions.click())
        onData(anything()).atPosition(0).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.textedit_vehicle_detail_brand)).perform(ViewActions.typeText(brand))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_vehicle_detail_model)).perform(ViewActions.typeText(model))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_vehicle_detail_licensePlate)).perform(ViewActions.typeText(licensePlate))
        Espresso.onView(ViewMatchers.withId(R.id.spinner_vehicle_detail_country)).perform(ViewActions.click())
        onData(anything()).atPosition(20).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.button_vehicle_detail_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.cardview3_profile_summary)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.fragment_profile_vehicle_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click()));
        Espresso.onView(ViewMatchers.withId(R.id.spinner_vehicle_detail_type)).check(matches(withSpinnerText(
            containsString(vehicleType))))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_vehicle_detail_brand)).check(matches(withText(brand)))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_vehicle_detail_model)).check(matches(withText(model)))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_vehicle_detail_licensePlate)).check(matches(withText(licensePlate)))
        Espresso.onView(ViewMatchers.withId(R.id.spinner_vehicle_detail_country)).check(matches(withSpinnerText(
            containsString(country))))
    }


    @After
    fun tearDown() {
    }
}