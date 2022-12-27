package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
class TaskDaoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            ToDoDatabase::class.java
        ).build()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTaskAndRetrieveTaskById() = runBlockingTest {
        val task = Task("title", "description")
        database.taskDao().insertTask(task)

        val loaded = database.taskDao().getTaskById(task.id)

        assertThat(loaded as Task, notNullValue())
        assertThat(loaded.id, `is`(task.id))
        assertThat(loaded.title, `is`(task.title))
        assertThat(loaded.description, `is`(task.description))
        assertThat(loaded.isCompleted, `is`(task.isCompleted))
    }

    @Test
    fun insertTaskAndUpdateTask() = runBlockingTest {
        val id = UUID.randomUUID().toString()

        val task = Task("title", "description", id = id)
        database.taskDao().insertTask(task)

        val loaded = database.taskDao().getTaskById(id)

        assertThat(loaded as Task, notNullValue())
        assertThat(loaded.title, `is`(task.title))

        val updateTask = Task("Updated Title", "description", id = id)
        database.taskDao().updateTask(updateTask)

        val loadedAfterUpdate = database.taskDao().getTaskById(id)

        assertThat(loadedAfterUpdate as Task, notNullValue())
        assertThat(loadedAfterUpdate.id, `is`(task.id))
        assertThat(loadedAfterUpdate.title, `is`(updateTask.title))
    }
}
