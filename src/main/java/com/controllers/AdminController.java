package com.controllers;

import com.entity.Role;
import com.entity.User;
import com.repositories.UserRepository;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin/")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "users")
    public String Users(Model model, Principal principal) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", userRepository.findByUsername(principal.getName()));
        return "admin";
    }

    @PostMapping("new")
    public String newPerson(@RequestParam(value = "username") String username,
                            @RequestParam(value = "password") String password,
                            @RequestParam(value = "role") String[] s) {
        Set<Role> role = new HashSet<>();
        for (String roles : s) {
            role.add(userService.getRoleByName(roles));
        }
        userService.edit(new User(username, password, role));
        return "redirect:users";
    }

    @PostMapping("editSave")
    public String update(Model model,
                         @RequestParam("id") Long id,
                         @RequestParam("username") String username,
                         @RequestParam("password") String password,
                         @RequestParam("role") String[] s){
        Set<Role> role = new HashSet<>();
        for (String roles : s) {
            role.add(userService.getRoleByName(roles));
        }
        userService.edit(new User(id, username, password, role));
        return "redirect:users";
    }

    @GetMapping("delete")
    public String delete(@RequestParam(value = "id") String s) {
        Long userId = Long.parseLong(s);
        userService.delete(userId);
        return "redirect:users";
    }


}
