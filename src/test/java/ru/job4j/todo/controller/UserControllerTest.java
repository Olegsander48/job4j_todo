package ru.job4j.todo.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.user.UserService;

import javax.security.auth.login.FailedLoginException;
import java.util.Optional;

class UserControllerTest {
    private UserController userController;
    private UserService userService;
    private MockHttpServletRequest mockHttpServletRequest;

    @BeforeEach
    void initServices() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        mockHttpServletRequest = new MockHttpServletRequest();
    }

    /**
     * Test case: Return registration page when requested.
     */
    @Test
    void whenRequestUserRegistrationPageThenGetPage() {
        var view = userController.getRegistrationPage();

        assertThat(view).isEqualTo("users/register");
    }

    /**
     * Test case: Save a user to the database and verify that the same user is passed to the service and redirected correctly.
     */
    @Test
    void whenPostUserWithAllFieldsThenSameDataAndRedirectToVacanciesPage() {
        var user = new User(1, "example@test.com", "Nikolay", "qwerty123");
        var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        var optionalUser = Optional.of(user);
        when(userService.save(userArgumentCaptor.capture())).thenReturn(optionalUser);

        var model = new ConcurrentModel();
        var view = userController.register(model, user);
        var actualUser = userArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/tasks");
        assertThat(actualUser).isEqualTo(user);
    }

    /**
     * Test case: Save a user with an existing email and verify that an exception is shown on the error page.
     */
    @Test
    void whenPostExistingUserWithAllFieldsThenGetErrorPage() {
        var expectedException = new RuntimeException("Пользователь с такой почтой уже существует");
        when(userService.save(any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = userController.register(model, new User());
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("fragments/errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    /**
     * Test case: Return login page when requested.
     */
    @Test
    void whenRequestUserLoginPageThenGetPage() {
        var view = userController.getLoginPage();

        assertThat(view).isEqualTo("users/login");
    }

    /**
     * Test case: Log in with an existing user and verify redirection to sessions and user stored in session.
     */
    @Test
    void whenLoginExistingUserThenGetRedirectionAndSameUser() throws FailedLoginException {
        var user = new User(1, "Aleks prig", "example@test.com", "qwerty123");
        var expectedUser = Optional.of(user);
        when(userService.findByLoginAndPassword(user.getLogin(), user.getPassword())).thenReturn(expectedUser);

        var model = new ConcurrentModel();
        var view = userController.loginUser(user, model, mockHttpServletRequest);
        var actualUser = mockHttpServletRequest.getSession().getAttribute("user");

        assertThat(view).isEqualTo("redirect:/tasks");
        assertThat(actualUser).isEqualTo(user);
    }

    /**
     * Test case: Attempt login with invalid credentials and verify error message is shown on login page.
     */
    @Test
    void whenLoginNonExistingUserThenGetRedirectionAndErrorMessage() throws FailedLoginException {
        var expectedException = new FailedLoginException("Login or password is incorrect");
        when(userService.findByLoginAndPassword(any(), any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = userController.loginUser(new User(), model, mockHttpServletRequest);
        var actualExceptionMessage = model.getAttribute("error");

        assertThat(view).isEqualTo("users/login");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    /**
     * Test case: Log out the current user and verify redirection to login page and session invalidation.
     */
    @Test
    void whenLogoutFromAccountThenGetRedirectionToLoginPage() {
        var view = userController.logout(mockHttpServletRequest.getSession());

        assertThat(view).isEqualTo("redirect:/users/login");
        assertThat(mockHttpServletRequest.getSession(false)).isNull();
    }
}