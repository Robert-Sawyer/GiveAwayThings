package com.github.robertsawyer.GiveBackThings.controller;

import com.github.robertsawyer.GiveBackThings.domain.model.User;
import com.github.robertsawyer.GiveBackThings.dtos.LoginFormDTO;
import com.github.robertsawyer.GiveBackThings.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    private UserService userService;


    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "home/login";
    }

    @PostMapping
    public String login(@Valid @ModelAttribute("user") LoginFormDTO user, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "home/login";
        }
        User existingUser = userService.findExistingUser(user);
        if (existingUser == null) {
            result.addError(new FieldError("user", "email", "Email or password is incorrect"));
            return "home/login";
        }

        session.setAttribute("userId", existingUser);

        if (userService.checkIfRoleIsAdmin(user)) {
            return "redirect:/adminDashboard";
        }
        return "redirect:/userDashboard";
    }


}
