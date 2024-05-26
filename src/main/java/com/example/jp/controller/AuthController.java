package com.example.jp.controller;


import com.example.jp.model.User;
import com.example.jp.repositories.UserRepository;
import com.example.jp.services.UserService;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import java.util.Objects;

@Controller
@RequestMapping("")
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final SecurityExpressionHandler webSecurityExpressionHandler;

    public AuthController(UserRepository userRepository, UserService userService, SecurityExpressionHandler webSecurityExpressionHandler) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.webSecurityExpressionHandler = webSecurityExpressionHandler;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "/login";
    }

    @RequestMapping(value = "/change_password", method = RequestMethod.GET)
    public String changePasswordForm() {
        return "change_password";
    }

    @RequestMapping(value = "/change_password", method = RequestMethod.POST)
    public String changePassword(@RequestParam("oldPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findUserByEmail(userEmail);
        try {
            if (!userService.checkIfValidOldPassword(user, currentPassword)) {
                throw new IllegalArgumentException("Niepoprawne hasło");
            }
            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("Wpisane hasła nie są jednakowe");
            }
            userService.changeUserPassword(user, newPassword);
            model.addAttribute("status", "Zmieniono hasło");
            return "change_password";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "change_password";
        }
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
        return "redirect:/login";
    }
}

