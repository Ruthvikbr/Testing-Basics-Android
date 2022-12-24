package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class StatisticsUtilsTest {

    @Test
    fun `Get only active task stats when there are no completed tasks`() {
        val tasks = listOf(
            Task("title", "desc", isCompleted = false)
        )

        val result = getActiveAndCompletedStats(tasks)

        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(100f))
    }

    @Test
    fun `Get active and completed task stats`() {
        val tasks = listOf(
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = true)
        )

        val result = getActiveAndCompletedStats(tasks)

        assertThat(result.completedTasksPercent, `is`(50f))
        assertThat(result.activeTasksPercent, `is`(50f))
    }

    @Test
    fun `Get task stats when there are no tasks`() {
        val result = getActiveAndCompletedStats(emptyList<Task>())

        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }

    @Test
    fun `Get task stats when there is a network error`() {
        val result = getActiveAndCompletedStats(null)

        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }
}
