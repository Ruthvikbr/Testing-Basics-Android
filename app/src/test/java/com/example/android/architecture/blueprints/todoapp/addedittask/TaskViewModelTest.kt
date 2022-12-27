package com.example.android.architecture.blueprints.todoapp.addedittask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import com.example.android.architecture.blueprints.todoapp.tasks.TasksFilterType
import com.example.android.architecture.blueprints.todoapp.tasks.TasksViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.* // ktlint-disable no-wildcard-imports
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TaskViewModelTest {

    private lateinit var taskViewModel: TasksViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var tasksRepository: FakeTestRepository

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        tasksRepository = FakeTestRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        tasksRepository.addTasks(task1, task2, task3)

        taskViewModel = TasksViewModel(tasksRepository)
    }

    @Test
    fun `Adding new tasks sets new task event`() {
        val observer = Observer<Event<Unit>> {}
        try {
            taskViewModel.newTaskEvent.observeForever(observer)
            taskViewModel.addNewTask()

            val value = taskViewModel.newTaskEvent.getOrAwaitValue()
            assertThat(value.getContentIfNotHandled(), not(nullValue()))
        } finally {
            taskViewModel.newTaskEvent.removeObserver(observer)
        }
    }

    @Test
    fun `Setting Filter to show all tasks displays the Add Task Button`() {
        taskViewModel.setFiltering(TasksFilterType.ALL_TASKS)
        assertThat(taskViewModel.tasksAddViewVisible.getOrAwaitValue(), `is`(true))
    }

    @Test
    fun `Completing tasks shows the correct message in the snackbar`() {
        val task = Task("Title", "Description")
        tasksRepository.addTasks(task)

        taskViewModel.completeTask(task, true)

        assertThat(tasksRepository.tasksServiceData[task.id]?.isCompleted, `is`(true))

        val snackbarText: Event<Int> = taskViewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbarText.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
    }
}
