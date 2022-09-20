package com.example.myapplication


import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class GameInfoUITest2 {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ActivityMainMenu::class.java)

    @Test
    fun gameInfoUITest2() {

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.btn_start),
                childAtPosition(
                    childAtPosition(
                        allOf(
                            withId(android.R.id.content),
                            childAtPosition(
                                allOf(
                                    withId(R.id.action_bar_root),
                                    childAtPosition(
                                        childAtPosition(
                                            withClassName(`is`("android.widget.LinearLayout")),
                                            1
                                        ),
                                        0
                                    )
                                ),
                                1
                            )
                        ),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        Thread.sleep(1000)
        for(i in 0 .. 4){
            for(j in 0..1){
                val appCompatBoard = onView(allOf(withId(R.id.gameBoardView), isDisplayed ()))
                appCompatBoard.perform(clickIn(i * 150, j * 150))
                Thread.sleep(200)
            }

        }

        val textView = onView(
            allOf(
                withId(R.id.tv_lvl), withText("MODE: PvP"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("MODE: PvP")))

        val textView2 = onView(
            allOf(
                withId(R.id.tv_lvl), withText("MODE: PvP"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("MODE: PvP")))

        val textView3 = onView(
            allOf(
                withId(R.id.tv_moves), withText("MOVES: 9"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("MOVES: 9")))

        val textView4 = onView(
            allOf(
                withId(R.id.tv_wining), withText("P1: WON!\nP2: ..."),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("P1: WON!\nP2: ...")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
    companion object {
        fun clickIn(x: Int, y: Int): ViewAction {
            return GeneralClickAction(
                Tap.SINGLE,
                CoordinatesProvider { view ->
                    val screenPos = IntArray(2)
                    view?.getLocationOnScreen(screenPos)

                    val screenX = (screenPos[0] + x).toFloat()
                    val screenY = (screenPos[1] + y).toFloat()

                    floatArrayOf(screenX, screenY)
                },
                Press.FINGER,
                InputDevice.SOURCE_MOUSE,
                MotionEvent.BUTTON_PRIMARY)
        }
    }
}
