package ru.geekbrains.kotlin.base.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.base.data.entity.Note
import ru.geekbrains.kotlin.base.ui.note.NoteActivity
import ru.geekbrains.kotlin.base.ui.note.NoteViewModel
import ru.geekbrains.kotlin.base.ui.note.NoteViewState
import ru.geekbrains.kotlin.base.ui.splash.SplashActivity
import ru.geekbrains.kotlin.base.ui.splash.SplashViewModel

class MainActivityTest {

    @get:Rule
    val activityTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    private val viewModel: MainViewModel = mockk(relaxed = true)
    private val viewStateLiveData = MutableLiveData<MainViewState>()
    private val EXTRA_NOTE = "note"

    private val testNotes = listOf(
        Note("1", "title1", "text1"),
        Note("2", "title2", "text2"),
        Note("3", "title3", "text3"),
    )

    @Before
    fun setup() {
        loadKoinModules(
            listOf(
                module {
                    viewModel { viewModel }
                    viewModel { mockk<NoteViewModel>(relaxed = true) }
                    viewModel { mockk<SplashViewModel>(relaxed = true) }
                }
            )
        )

        every {
            viewModel.getViewState()
        } returns viewStateLiveData
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(MainViewState(notes = testNotes))
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun check_data_is_displayed() {
        onView(withId(R.id.rv_notes)).perform(
            RecyclerViewActions.scrollToPosition<NotesRVAdapter.ViewHolder>(
                1
            )
        )
        onView(withText(testNotes[1].text)).check(matches(isDisplayed()))
    }

    @Test
    fun check_main_title_text() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        onView(withText(R.string.app_name)).check(matches(withParent(withId(R.id.toolbar))));
    }

    @Test
    fun check_logout_alert_is_present() {
        openActionBarOverflowOrOptionsMenu(
            InstrumentationRegistry.getInstrumentation().getTargetContext()
        );
        onView(withId(R.id.title)).perform(click())
        onView(withText(R.string.logout_message)).check(matches(isDisplayed()));
    }

    @Test
    fun check_splash_activity_intent_sent() {
        openActionBarOverflowOrOptionsMenu(
            InstrumentationRegistry.getInstrumentation().getTargetContext()
        );
        onView(withId(R.id.title)).perform(click())
        onView(withText("ВЫЙТИ")).perform(click())
        intended(
            allOf(
                hasComponent(SplashActivity::class.java.name)
            )
        )
    }

    @Test
    fun check_note_activity_intent_sent() {
        onView(withId(R.id.rv_notes)).perform(actionOnItemAtPosition<NotesRVAdapter.ViewHolder>(1, click()))
        intended(
            allOf(
                hasComponent(NoteActivity::class.java.name),
                hasExtra(EXTRA_NOTE, testNotes[1].id)
            )
        )
        onView(withId(R.id.et_title)).check(matches(withText(testNotes[1].title)))
    }

    @Test
    fun check_new_note_activity_intent_sent() {
        onView(withId(R.id.rv_notes)).perform(actionOnItemAtPosition<NotesRVAdapter.ViewHolder>(1, click()))
        intended(
            allOf(
                hasComponent(NoteActivity::class.java.name)
            )
        )
        onView(withId(R.id.et_title)).check(matches(withText("")))
    }


}