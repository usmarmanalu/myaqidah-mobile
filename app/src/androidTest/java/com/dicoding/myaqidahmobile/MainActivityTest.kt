package com.dicoding.myaqidahmobile

import androidx.navigation.*
import androidx.navigation.testing.*
import androidx.test.core.app.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.*
import org.hamcrest.Matchers.`is`
import org.junit.*
import org.junit.runner.*

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        // Launch MainActivity
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                // Initialize the TestNavHostController
                navController =
                    TestNavHostController(ApplicationProvider.getApplicationContext()).apply {
                        // Set the nav graph for testing
                        setGraph(R.navigation.mobile_navigation) // Replace with your actual nav graph
                    }
                // Set the NavController to the NavHostFragment
                Navigation.setViewNavController(
                    activity.findViewById(R.id.nav_host_fragment_activity_main),
                    navController
                )
            }
        }
    }

    @Test
    fun test_navigation_home() {
        // Verify that navigating to the Home fragment is successful
        onView(withId(R.id.navigation_home)).perform(click())
        // Assert that the current destination is the home fragment
        assertThat(navController.currentDestination?.id, `is`(R.id.navigation_home))
    }

    @Test
    fun test_navigation_pelayanan() {
        // Verify that navigating to the Pelayanan fragment is successful
        onView(withId(R.id.navigation_pelayanan)).perform(click())
        // Assert that the current destination is the pelayanan fragment
        assertThat(navController.currentDestination?.id, `is`(R.id.navigation_pelayanan))
    }

    @Test
    fun test_navigation_jadwal() {
        // Verify that navigating to the Jadwal fragment is successful
        onView(withId(R.id.navigation_jadwal)).perform(click())
        // Assert that the current destination is the jadwal fragment
        assertThat(navController.currentDestination?.id, `is`(R.id.navigation_jadwal))
    }

    @Test
    fun test_navigation_profile() {
        // Verify that navigating to the Profile fragment is successful
        onView(withId(R.id.navigation_profile)).perform(click())
        // Assert that the current destination is the profile fragment
        assertThat(navController.currentDestination?.id, `is`(R.id.navigation_profile))
    }
}
