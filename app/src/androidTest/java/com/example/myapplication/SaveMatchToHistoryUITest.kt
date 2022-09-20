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
import java.io.File

@LargeTest
@RunWith(AndroidJUnit4::class)
class SaveMatchToHistoryUITest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(ActivityMainMenu::class.java)

    @Test
    fun saveMatchToHistoryUITest() {
        val fw = File("matches_history.txt")
        fw.delete()
        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.page_3), withContentDescription("History"),
                childAtPosition(
                    childAtPosition(
                        allOf(
                            withId(R.id.bottomNavigationView),
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
                                6
                            )
                        ),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.page_2), withContentDescription("Menu"),
                childAtPosition(
                    childAtPosition(
                        allOf(
                            withId(R.id.bottomNavigationView),
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
                                2
                            )
                        ),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())


        val appCompatImageView3 = onView(
            allOf(
                withId(R.id.imageView4),
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
        appCompatImageView3.perform(click())

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
        for(i in 1 .. 5){
            for(j in 1 .. 2){
                val appCompatBoard = onView(allOf(withId(R.id.gameBoardView), isDisplayed ()))
                appCompatBoard.perform(GameInfoUITest2.clickIn(j*150, i * 150))
                Thread.sleep(300)
            }



        }

        val bottomNavigationItemView5 = onView(
            allOf(
                withId(R.id.page_3), withContentDescription("History"),
                childAtPosition(
                    childAtPosition(
                        allOf(
                            withId(R.id.bottomNavigationView),
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
                            )
                        ),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView5.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.tv_winner_ad), withText("AI"),
                withParent(
                    allOf(
                        withId(R.id.match_list_item),
                        withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("AI")))

        val textView2 = onView(
            allOf(
                withId(R.id.tv_date_ad), withText("2020/11/14"),
                withParent(
                    allOf(
                        withId(R.id.match_list_item),
                        withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("2020/11/14")))

        val textView3 = onView(
            allOf(
                withId(R.id.tv_mode_ad), withText("EASY"),
                withParent(
                    allOf(
                        withId(R.id.match_list_item),
                        withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("EASY")))
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
