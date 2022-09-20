package com.example.myapplication


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
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
class GameUndoMoveUITest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ActivityMainMenu::class.java)

    @Test
    fun gameUndoMoveUITest() {
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
        Thread.sleep(500)
        for(i in 0 .. 2){
            for(j in 0..1){
                val appCompatBoard = onView(allOf(withId(R.id.gameBoardView), isDisplayed ()))
                appCompatBoard.perform(GameInfoUITest2.clickIn(i * 150, j * 150))
                Thread.sleep(200)
            }

        }

        val appCompatButton = onView(
            allOf(
                withId(R.id.btn_undo_move),
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
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val appCompatButton2 = onView(
            allOf(
                withId(R.id.btn_undo_move),
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
                    3
                ),
                isDisplayed()
            )
        )
        appCompatButton2.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.tv_moves), withText("MOVES: 4"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("MOVES: 4")))
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
}
