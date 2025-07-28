package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.task.TaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, Model model) {
        try {
            taskService.save(task);
            return "redirect:/tasks";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "fragments/errors/404";
        }
    }

    @GetMapping("/{id}")
    public String getTaskPage(@PathVariable int id, Model model) {
        try {
            var task = taskService.findById(id);
            model.addAttribute("task", task.get());
            return "tasks/task";
        } catch (NoSuchElementException exception) {
            model.addAttribute("message", exception.getMessage());
            return "fragments/errors/404";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        try {
            taskService.deleteById(id);
        } catch (NoSuchElementException exception) {
            model.addAttribute("message", exception.getMessage());
            return "fragments/errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/update/{id}")
    public String updateStatus(Model model, @PathVariable int id) {
        try {
            Task task = taskService.findById(id).get();
            task.setDone(true);
            taskService.update(task);
        } catch (NoSuchElementException exception) {
            model.addAttribute("message", exception.getMessage());
            return "fragments/errors/404";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/edit/{id}")
    public String getEditPage(@PathVariable int id, Model model) {
        try {
            var task = taskService.findById(id).get();
            model.addAttribute("task", task);
            model.addAttribute("formattedDate",
                    task.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            return "tasks/edit";
        } catch (NoSuchElementException exception) {
            model.addAttribute("message", exception.getMessage());
            return "fragments/errors/404";
        }
    }

    @PostMapping("/update")
    public String updateTask(@ModelAttribute Task task, @RequestParam("created") String created, Model model) {
        try {
            task.setCreated(LocalDateTime.parse(created, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            taskService.update(task);
        } catch (DateTimeParseException exception) {
            model.addAttribute("message", "Check if the date is valid");
            return "fragments/errors/404";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "fragments/errors/404";
        }
        return "redirect:/tasks";
    }
}
