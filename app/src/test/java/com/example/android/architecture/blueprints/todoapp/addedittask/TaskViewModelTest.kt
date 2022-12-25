package com.example.android.architecture.blueprints.todoapp.addedittask

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import com.example.android.architecture.blueprints.todoapp.tasks.TasksViewModel
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskViewModelTest {

    lateinit var taskViewModel: TasksViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        taskViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
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
}
