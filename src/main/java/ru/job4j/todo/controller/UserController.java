package ru.job4j.todo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.user.UserService;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "users/register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute User user) {
        try {
            userService.save(user);
            return "redirect:/tasks";
        } catch (Exception ex) {
            model.addAttribute("message", "Пользователь с такой почтой уже существует");
            return "fragments/errors/404";
        }
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
        try {
            var userOptional = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
            var session = request.getSession();
            session.setAttribute("user", userOptional.get());
            return "redirect:/tasks";
        } catch (NoSuchElementException ex) {
            model.addAttribute("error", ex.getMessage());
            return "users/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }
}
