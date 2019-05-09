/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wisekiddo.mvpupgrade.statistics

import android.content.Intent
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.wisekiddo.android.architecture.blueprints.mvpupgrade.R
import com.wisekiddo.mvpupgrade.data.FakeTasksRemoteDataSource
import com.wisekiddo.mvpupgrade.data.Task
import com.wisekiddo.mvpupgrade.data.source.TasksRepository
import com.wisekiddo.mvpupgrade.taskdetail.TaskDetailActivity
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for the statistics screen.
 */
@RunWith(AndroidJUnit4::class) @LargeTest class StatisticsScreenTest {

    /**
     * [ActivityTestRule] is a JUnit [@Rule][Rule] to launch your activity under test.

     *
     *
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule @JvmField var statisticsActivityTestRule = ActivityTestRule(
            StatisticsActivity::class.java, true, false)

    /**
     * Setup your test fixture with a fake task id. The [TaskDetailActivity] is started with
     * a particular task id, which is then loaded from the service API.

     *
     *
     * Note that this test runs hermetically and is fully isolated using a fake implementation of
     * the service API. This is a great way to make your tests more reliable and faster at the same
     * time, since they are isolated from any outside dependencies.
     */
    @Before fun intentWithStubbedTaskId() {
        // Given some tasks
        TasksRepository.destroyInstance()
        with(FakeTasksRemoteDataSource.getInstance()) {
            addTasks(Task("Title1").apply { isCompleted = false })
            addTasks(Task("Title2").apply { isCompleted = true })
        }

        // Lazily start the Activity from the ActivityTestRule
        statisticsActivityTestRule.launchActivity(Intent())
    }

    @Test fun Tasks_ShowsNonEmptyMessage() {
        // Check that the active and completed tasks text is displayed
        with(InstrumentationRegistry.getTargetContext()) {
            val expectedActiveTaskText = getString(R.string.statistics_active_tasks)
            onView(withText(containsString(expectedActiveTaskText)))
                    .check(matches(isDisplayed()))
            val expectedCompletedTaskText = getString(R.string.statistics_completed_tasks)
            onView(withText(containsString(expectedCompletedTaskText)))
                    .check(matches(isDisplayed()))
        }
    }
}
