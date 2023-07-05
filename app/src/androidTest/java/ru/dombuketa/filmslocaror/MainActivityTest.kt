package ru.dombuketa.filmslocaror

import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import ru.dombuketa.filmslocaror.view.MainActivity

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class MainActivityTest {
    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test0backPressedSingleClickNotExitApp() {
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressBack()
        onView(withId(R.id.main_recycler)).check(matches(isDisplayed()))

    }

    @Test
    fun test1recyclerViewShouldBeAttached(){
        onView(withId(R.id.main_recycler)).check(matches(isDisplayed()))
    }

    @Test
    fun test2allMenuDestinationsShouldWork(){
        onView(withId(R.id.favorites)).perform(click())
        onView(withId(R.id.favorite_fragment_root)).check(matches(isDisplayed()))
        onView(withId(R.id.watch_later)).perform(click())
        onView(withId(R.id.seelater_fragment_root)).check(matches(isDisplayed()))
        onView(withId(R.id.casts)).perform(click())
        onView(withId(R.id.casts_fragment_root)).check(matches(isDisplayed()))
        onView(withId(R.id.home)).perform(click())
        onView(withId(R.id.home_fragment_root)).check(matches(isDisplayed()))
    }

    @Test
    fun test3searchViewShouldBeAbleToInputText() {
        val testString = "111111"
        onView(withId(R.id.search_view)).check(matches(isDisplayed()))
        onView(withId(R.id.search_view)).perform(typeSearchViewText(testString))
        onView(withId(R.id.search_view)).perform(typeSearchViewText(""))
    }



    private fun typeSearchViewText(text: String?): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
            }

            override fun getDescription(): String {
                return "Change view text"
            }

            override fun perform(uiController: UiController?, view: View) {
                (view as SearchView).setQuery(text, false)
            }
        }
    }
}