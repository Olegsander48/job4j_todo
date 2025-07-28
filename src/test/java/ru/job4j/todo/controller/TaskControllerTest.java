package ru.job4j.todo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.task.TaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskControllerTest {
    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);
    }

    /**
     * Test case: When getting all tasks, then they are added to the model and the correct view is returned.
     */
    @Test
    void whenGetAllTasksThenTasksAddedToModelAndListViewReturned() {
        var tasks = List.of(new Task());
        when(taskService.findAll()).thenReturn(tasks);

        var model = new ConcurrentModel();
        var view = taskController.getAllTasks(model);
        var actualTasks = model.getAttribute("tasks");

        assertThat(view).isEqualTo("tasks/list");
        assertThat(actualTasks).isEqualTo(tasks);
    }

    /**
     * Test case: When getting the creation page, then the create view is returned.
     */
    @Test
    void whenGetCreationPageThenCreateViewReturned() {
        assertThat(taskController.getCreationPage()).isEqualTo("tasks/create");
    }

    /**
     * Test case: When creating a task successfully, then redirect to tasks list.
     */
    @Test
    void whenCreateTaskThenRedirectToTasks() {
        var task = new Task();
        when(taskService.save(task)).thenReturn(task);

        var model = new ConcurrentModel();
        var view = taskController.create(task, model);

        assertThat(view).isEqualTo("redirect:/tasks");
    }

    /**
     * Test case: When creating a task throws exception, then error view is returned.
     */
    @Test
    void whenCreateTaskThrowsExceptionThenErrorViewReturned() {
        var expectedException = new RuntimeException("Failed to save task");
        var task = new Task();
        when(taskService.save(task)).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = taskController.create(task, model);
        var errorMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/404");
        assertThat(errorMessage).isEqualTo(expectedException.getMessage());
    }

    /**
     * Test case: When getting a task by id, then it is added to the model and the task view is returned.
     */
    @Test
    void whenGetTaskByIdThenTaskAddedToModelAndTaskViewReturned() {
        var task = new Task();
        when(taskService.findById(1)).thenReturn(Optional.of(task));

        var model = new ConcurrentModel();
        var view = taskController.getTaskPage(1, model);
        var actualTask = model.getAttribute("task");

        assertThat(view).isEqualTo("tasks/task");
        assertThat(actualTask).isEqualTo(task);
    }

    /**
     * Test case: When getting a task by id that does not exist, then error view is returned.
     */
    @Test
    void whenGetTaskByIdNotFoundThenErrorViewReturned() {
        var expectedException = new NoSuchElementException("Task not found");
        when(taskService.findById(1)).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = taskController.getTaskPage(1, model);
        var errorMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/404");
        assertThat(errorMessage).isEqualTo(expectedException.getMessage());
    }

    /**
     * Test case: When deleting a task successfully, then redirect to tasks list.
     */
    @Test
    void whenDeleteTaskThenRedirectToTasks() {
        when(taskService.deleteById(1)).thenReturn(true);

        var model = new ConcurrentModel();
        var view = taskController.delete(model, 1);

        assertThat(view).isEqualTo("redirect:/tasks");
    }

    /**
     * Test case: When deleting a task throws exception, then error view is returned.
     */
    @Test
    void whenDeleteTaskThrowsExceptionThenErrorViewReturned() {
        var expectedException = new NoSuchElementException("Task not found");
        when(taskService.deleteById(1)).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = taskController.delete(model, 1);
        var errorMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/404");
        assertThat(errorMessage).isEqualTo(expectedException.getMessage());
    }

    /**
     * Test case: When updating status of a task successfully, then redirect to tasks list.
     */
    @Test
    void whenUpdateStatusThenRedirectToTasks() {
        var task = new Task();
        when(taskService.findById(1)).thenReturn(Optional.of(task));

        var model = new ConcurrentModel();
        var view = taskController.updateStatus(model, 1);

        assertThat(task.isDone()).isTrue();
        assertThat(view).isEqualTo("redirect:/tasks");
    }

    /**
     * Test case: When updating status of a task throws exception, then error view is returned.
     */
    @Test
    void whenUpdateStatusThrowsExceptionThenErrorViewReturned() {
        var expectedException = new NoSuchElementException("Task not found");
        when(taskService.findById(1)).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = taskController.updateStatus(model, 1);
        var errorMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/404");
        assertThat(errorMessage).isEqualTo(expectedException.getMessage());
    }

    /**
     * Test case: When getting edit page for a task, then task and formatted date are added to the model and the edit view is returned.
     */
    @Test
    void whenGetEditPageThenTaskAndFormattedDateAddedAndEditViewReturned() {
        var task = new Task();
        task.setCreated(LocalDateTime.of(2024, 1, 1, 12, 0));
        when(taskService.findById(1)).thenReturn(Optional.of(task));

        var model = new ConcurrentModel();
        var view = taskController.getEditPage(1, model);
        var actualTask = model.getAttribute("task");
        var actualFormattedDate = model.getAttribute("formattedDate");

        assertThat(view).isEqualTo("tasks/edit");
        assertThat(actualTask).isEqualTo(task);
        assertThat(actualFormattedDate).isEqualTo("2024-01-01T12:00");
    }

    /**
     * Test case: When getting edit page for a task that does not exist, then error view is returned.
     */
    @Test
    void whenGetEditPageNotFoundThenErrorViewReturned() {
        var expectedException = new NoSuchElementException("Task not found");
        when(taskService.findById(1)).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = taskController.getEditPage(1, model);
        var errorMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/404");
        assertThat(errorMessage).isEqualTo(expectedException.getMessage());
    }

    /**
     * Test case: When updating a task successfully, then redirect to tasks list.
     */
    @Test
    void whenUpdateTaskThenRedirectToTasks() {
        var task = new Task();
        var created = "2024-01-01T12:00";

        var model = new ConcurrentModel();
        var view = taskController.updateTask(task, created, model);

        assertThat(task.getCreated()).isEqualTo(LocalDateTime.parse(created, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        assertThat(view).isEqualTo("redirect:/tasks");
    }

    /**
     * Test case: When updating a task with invalid date, then error view is returned.
     */
    @Test
    void whenUpdateTaskWithInvalidDateThenErrorViewReturned() {
        var task = new Task();
        var created = "invalid-date";

        var model = new ConcurrentModel();
        var view = taskController.updateTask(task, created, model);
        var errorMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/404");
        assertThat(errorMessage).isEqualTo("Check if the date is valid");
    }
}