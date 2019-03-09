package exodia.web.controllers;

import exodia.domain.models.binding.UserLoginBindingModel;
import exodia.domain.models.binding.UserRegisterBindingModel;
import exodia.domain.models.service.UserServiceModel;
import exodia.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;


    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    public ModelAndView register(ModelAndView modelAndView, HttpSession session) {
        if (session.getAttribute("username") != null) {
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("register");
        }
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerConfirm(@ModelAttribute UserRegisterBindingModel model, ModelAndView modelAndView) {
        if (!model.getPassword().equals(model.getConfirmPassword())) {
            throw new IllegalArgumentException("Password don't match");
        }

        if (!this.userService.registerUser(this.modelMapper.map(model, UserServiceModel.class))) {
            throw new IllegalArgumentException("Password don't match");
        }

        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView login(ModelAndView modelAndView, HttpSession session) {
        if (session.getAttribute("username") != null) {
            modelAndView.setViewName("redirect:/home");
        } else {
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView loginConfirm(@ModelAttribute UserLoginBindingModel bindingModel,
                                     ModelAndView modelAndView,
                                     HttpSession session) {

        UserServiceModel userServiceModel = this.userService.logUser(this.modelMapper.map(bindingModel, UserServiceModel.class));

        if (userServiceModel == null) {
            throw new IllegalArgumentException("User login failed!");
        }

        session.setAttribute("userId", userServiceModel.getId());
        session.setAttribute("username", userServiceModel.getUsername());

        modelAndView.setViewName("redirect:/home");
        return modelAndView;
    }

    @GetMapping("/logout")
    public ModelAndView logout(ModelAndView modelAndView, HttpSession session) {
        if (session.getAttribute("username") == null) {
            modelAndView.setViewName("redirect:/login");
        } else {
            session.invalidate();
            modelAndView.setViewName("redirect:/");
        }

        return modelAndView;

    }

}
