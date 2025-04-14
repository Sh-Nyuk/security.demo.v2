package com.security.demo.controllers;

import com.security.demo.models.Person;
import com.security.demo.models.Role;
import com.security.demo.service.PersonService;
import com.security.demo.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final PersonService personService;
    private final RoleService roleService;

    public AdminController(PersonService personService, RoleService roleService) {
        this.personService = personService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getUsers(Model model, @ModelAttribute("error") String error,
                           @ModelAttribute("message") String message) {
        List<Person> users = personService.getAllPersons();
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        if (error != null && !error.isEmpty()) {
            model.addAttribute("error", error);
        }
        if (message != null && !message.isEmpty()) {
            model.addAttribute("message", message);
        }
        return "admin-page";
    }

    @GetMapping("/edit")
    public String getEdit(@RequestParam("id") long id, Model model) {
        Person person = personService.findById(id);
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("user", person);
        model.addAttribute("roles", roles);
        return "admin-edit";
    }

    @PostMapping("/edit")
    public String editUser(
            @RequestParam("id") int id
            , @RequestParam("username") String username
            , @RequestParam("email") String email
            , @RequestParam("age") int age
            , @RequestParam(value = "rolesId", required = false) List<Long> rolesId
            , RedirectAttributes redirectAttributes) {
        Person person = personService.findById((long) id);

        person.setUsername(username);
        person.setEmail(email);
        person.setAge(age);

        try {
            personService.updatePerson(person, rolesId);
            redirectAttributes.addFlashAttribute("message", "User updated successfully");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/add")
    public String addUser(
            @RequestParam("username") String username
            , @RequestParam("email") String email
            , @RequestParam("age") int age
            , @RequestParam("password") String password
            , @RequestParam(value = "rolesId", required = false) List<Long> rolesId
            , RedirectAttributes redirectAttributes) {
        Person newPerson = new Person(username, password, email, age);

        try {
            personService.addPerson(newPerson, rolesId);
            redirectAttributes.addFlashAttribute("message", "User added successfully");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(
            @RequestParam("id") long id
            , RedirectAttributes redirectAttributes) {
        try {
            personService.deletePerson(id);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }
}
