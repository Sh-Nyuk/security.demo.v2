package com.security.demo.controllers;

import com.security.demo.models.Person;
import com.security.demo.models.Role;
import com.security.demo.repositories.PersonRepository;
import com.security.demo.service.PersonService;
import com.security.demo.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String getUsers(Model model, Principal principal) {
        String username = principal.getName();
        Person person = personService.findByUsername(username);
        model.addAttribute("user", person);
        return "users-page";
    }
}
