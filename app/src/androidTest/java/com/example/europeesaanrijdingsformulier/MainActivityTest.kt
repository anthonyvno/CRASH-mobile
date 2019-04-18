package com.example.europeesaanrijdingsformulier

import android.app.DatePickerDialog
import android.app.Instrumentation
import android.content.Context
import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.v7.widget.RecyclerView
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.CoreMatchers.containsString
import android.preference.PreferenceManager
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.content.Intent
import android.support.test.espresso.contrib.PickerActions
import android.widget.DatePicker
import org.hamcrest.Matchers
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.widget.TimePicker
import kotlin.concurrent.thread


class MainActivityTest{
    @Rule @JvmField
    val mActivityRule = ActivityTestRule(MainActivity::class.java)

    private var firstName = "Sander"
    private var lastName = "Beazar"
    private var email = "sander.beazar@realdolmen.com"

    private var firstName2 = "Anthony"
    private var lastName2 = "Van Noppen"
    private var email2 = "anthony.vannoppen@realdolmen.com"

    private var licenseType = "B"
    private var licenseNumber = "826419573"
    private var licenseNumber2 = "147302847"
    // private var licenseExpiringDate = "24/03/2022"

    private var vehicleType = "Vrachtwagen"
    private var brand = "Mercedes"
    private var model = "Actros"
    private var licensePlate = "1-ABC-456"
    private var country = "Belgium"
    private var brand2 = "Honda"
    private var model2 = "Camino"
    private var licensePlate2 = "1-XYZ-987"

    private var greenCardNumber = "837483"
    private var insuranceNumber = "123456"
    private var insuranceName = "Ethias"
    private var agencyPhone = "0473897865"
    private var agencyEmail = "kristof@bombeke.be"
    private var greenCardNumber2 = "6234852"
    private var insuranceNumber2 = "6723224"
    private var agencyPhone2 = "053629842"
    private var agencyEmail2 = "info@peetersinsurance.be"

    private var street = "Langestraat"
    private var streetNumber = "5"
    private var postalCode = "1790"
    private var city = "Affligem"



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
    fun ztestProfileVehicle() {
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

        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_vehicle_insurance_greenCard)).perform(ViewActions.typeText(greenCardNumber))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_vehicle_insurance_insuranceNumber)).perform(ViewActions.typeText(insuranceNumber))
        Espresso.onView(ViewMatchers.withId(R.id.spinner_profile_vehicle_insurance_insurer)).perform(ViewActions.click())
        onData(anything()).atPosition(3).perform(ViewActions.click());
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.edit_profile_vehicle_insurance_expires)).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2020, 2, 20))
        Espresso.onView(withText("OK"))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_vehicle_insurance_email)).perform(ViewActions.typeText(agencyEmail))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_vehicle_insurance_phone)).perform(ViewActions.typeText(agencyPhone))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.button_profile_vehicle_insurance_confirm)).perform(ViewActions.click())
        Espresso.closeSoftKeyboard()
        // CHECK

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
        Espresso.onView(ViewMatchers.withId(R.id.button_vehicle_detail_confirm)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_vehicle_insurance_insuranceNumber)).check(matches(withText(insuranceNumber)))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_vehicle_insurance_greenCard)).check(matches(withText(greenCardNumber)))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_vehicle_insurance_email)).check(matches(withText(agencyEmail)))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_profile_vehicle_insurance_phone)).check(matches(withText(agencyPhone)))
        Espresso.onView(ViewMatchers.withId(R.id.spinner_profile_vehicle_insurance_insurer)).check(matches(withSpinnerText(
            containsString(insuranceName))))
    }


    @Test
    fun testReportHappyFlow(){
        Espresso.pressBack()
        Espresso.onView(ViewMatchers.withId(R.id.button_home_startReport)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_crash_information_street)).perform(ViewActions.typeText(street))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_crash_information_streetNumber)).perform(ViewActions.typeText(streetNumber))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_crash_information_postalCode)).perform(ViewActions.typeText(postalCode))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_crash_information_city)).perform(ViewActions.typeText(city))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.spinner_report_crash_information_country)).perform(ViewActions.click())
        onData(anything()).atPosition(20).perform(ViewActions.click());
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.edit_report_crash_information_date)).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2020, 2, 20))
        Espresso.onView(withText("OK"))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.edit_report_crash_information_time)).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(withClassName(Matchers.equalTo(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(12, 25))
        Espresso.onView(withText("OK"))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.button_report_crash_information_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.button_report_start_a_profile)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.textedit_algemeen_a_firstname)).check(matches(withText(firstName)))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_algemeen_a_lastname)).check(matches(withText(lastName)))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_algemeen_a_email)).check(matches(withText(email)))
        Espresso.onView(ViewMatchers.withId(R.id.button_algemeen_a_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.spinner_report_license_a_category)).perform(ViewActions.click())
        onData(anything()).atPosition(4).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.edit_report_license_a_expires)).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2040, 2,10))
        Espresso.onView(withText("OK"))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_license_a_licenseNumber)).perform(ViewActions.typeText(licenseNumber))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.button_report_license_a_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.spinner_report_vehicle_detail_a_type)).perform(ViewActions.click())
        onData(anything()).atPosition(2).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_detail_a_brand)).perform(ViewActions.typeText(brand))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_detail_a_model)).perform(ViewActions.typeText(model))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_detail_a_licensePlate)).perform(ViewActions.typeText(licensePlate))
        Espresso.onView(ViewMatchers.withId(R.id.spinner_report_vehicle_detail_a_country)).perform(ViewActions.click())
        onData(anything()).atPosition(20).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.button_report_vehicle_detail_a_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_insurance_a_greenCard)).perform(ViewActions.typeText(greenCardNumber))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_insurance_a_insuranceNumber)).perform(ViewActions.typeText(insuranceNumber))
        Espresso.onView(ViewMatchers.withId(R.id.spinner_report_vehicle_insurance_a_insurer)).perform(ViewActions.click())
        onData(anything()).atPosition(3).perform(ViewActions.click());
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.edit_report_vehicle_insurance_a_expires)).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2030, 2, 20))
        Espresso.onView(withText("OK"))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_insurance_a_email)).perform(ViewActions.typeText(agencyEmail))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_insurance_a_phone)).perform(ViewActions.typeText(agencyPhone))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.button_report_vehicle_insurance_a_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.button_report_start_b_manual)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.textedit_algemeen_b_firstname)).perform(ViewActions.typeText(firstName2))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_algemeen_b_lastname)).perform(ViewActions.typeText(lastName2))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_algemeen_b_email)).perform(ViewActions.typeText(email2))
        Espresso.onView(ViewMatchers.withId(R.id.button_algemeen_b_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.spinner_report_license_b_category)).perform(ViewActions.click())
        onData(anything()).atPosition(2).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.edit_report_license_b_expires)).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2030, 10,21))
        Espresso.onView(withText("OK"))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_license_b_licenseNumber)).perform(ViewActions.typeText(licenseNumber2))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.button_report_license_b_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.spinner_report_vehicle_detail_b_type)).perform(ViewActions.click())
        onData(anything()).atPosition(3).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_detail_b_brand)).perform(ViewActions.typeText(brand2))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_detail_b_model)).perform(ViewActions.typeText(model2))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_detail_b_licensePlate)).perform(ViewActions.typeText(licensePlate2))
        Espresso.onView(ViewMatchers.withId(R.id.spinner_report_vehicle_detail_b_country)).perform(ViewActions.click())
        onData(anything()).atPosition(20).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.button_report_vehicle_detail_b_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_insurance_b_greenCard)).perform(ViewActions.typeText(greenCardNumber2))
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_insurance_b_insuranceNumber)).perform(ViewActions.typeText(insuranceNumber2))
        Espresso.onView(ViewMatchers.withId(R.id.spinner_report_vehicle_insurance_b_insurer)).perform(ViewActions.click())
        onData(anything()).atPosition(1).perform(ViewActions.click());
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.edit_report_vehicle_insurance_b_expires)).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2090, 11, 15))
        Espresso.onView(withText("OK"))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_insurance_b_email)).perform(ViewActions.typeText(agencyEmail2))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.textedit_report_vehicle_insurance_b_phone)).perform(ViewActions.typeText(agencyPhone2))
        Espresso.closeSoftKeyboard()
        Espresso.onView(ViewMatchers.withId(R.id.button_report_vehicle_insurance_b_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.switch_circ_1a)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.switch_circ_3a)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.switch_circ_7a)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.switch_circ_4b)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.button_report_circ_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.switch_circ_10a)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.switch_circ_12b)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.switch_circ_15b)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.switch_circ_16b)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.button_report_circ_save)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.drawview_report_sketch)).perform(
            GeneralSwipeAction(
                Swipe.SLOW,
                GeneralLocation.CENTER ,
                GeneralLocation.CENTER_RIGHT,
                Press.FINGER
            )
        )


        Espresso.onView(ViewMatchers.withId(R.id.button_sketch_draw)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.drawview_report_sketch)).perform(ViewActions.swipeDown())
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.button_sketch_undo)).perform(ViewActions.click())
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.button_sketch_redo)).perform(ViewActions.click())
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.button_sketch_clear)).perform(ViewActions.click())
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.drawview_report_sketch)).perform(ViewActions.swipeDown())
        Thread.sleep(500)
        Espresso.onView(ViewMatchers.withId(R.id.button_report_sketch_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.button_report_image_camera)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
            ViewActions.click()))
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
            ViewActions.click()))
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(2,
            ViewActions.click()))
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(3,
            ViewActions.click()))
        Espresso.onView(ViewMatchers.withId(R.id.menu_done)).perform(ViewActions.click())
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.button_report_image_confirm)).perform(ViewActions.click())

        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.button_report_overview_confirm)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.signature_pad_a)).perform(ViewActions.swipeDown())
        Espresso.onView(ViewMatchers.withId(R.id.signature_pad_b)).perform(ViewActions.swipeLeft())
        Espresso.onView(ViewMatchers.withId(R.id.button_report_confirmation_confirm)).perform(ViewActions.click())
        Thread.sleep(10000)

        Thread.sleep(10000)


    }
}


