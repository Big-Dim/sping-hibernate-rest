package com.dimpo.geofrm.controller;

import com.dimpo.geofrm.dto.GeoDto;
import com.dimpo.geofrm.dto.UserDto;
import com.dimpo.geofrm.entity.GeologicalClasses;
import com.dimpo.geofrm.entity.User;
import com.dimpo.geofrm.service.GeoService;
import com.dimpo.geofrm.service.UserService;
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class ViewController {

    private UserService userService;

    private GeoService geoService;


    @GetMapping("index")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "index";
    }

    // handler method to handle user registration request
    @GetMapping("register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle register user form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto user,
                               BindingResult result,
                               Model model){
        User existing = userService.findByEmail(user.getEmail());
        if (existing != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/users")
    public String listRegisteredUsers(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/geo")
    public String showGeoForm(Model model){
        List<GeologicalClasses> geolst = geoService.findAll();
        model.addAttribute("geolst", geolst);
        GeoDto geoDto = new GeoDto();
        model.addAttribute("geoClass", geoDto);
        return "geo";
    }

    @GetMapping("/geo/getall")
    public String showGeoAll(Model model){
        List<GeologicalClasses> geolst = geoService.findAll();
        model.addAttribute("geolst", geolst);
        GeoDto geoDto = new GeoDto();
        model.addAttribute("geoClass", geoDto);
        return "geo";
    }

    @PostMapping("/geo/addsec")
    public String addSection(@Valid @ModelAttribute("geoClass") GeoDto geoDto,
                             BindingResult result,  Model model){

        geoService.saveSection(geoDto);
        return "redirect:/geo";
    }

    @PostMapping("/geo/addgeo")
    public String addGeoClass(@Valid @ModelAttribute("geoClass") GeoDto geoDto,
                              BindingResult result,  Model model){

        geoService.saveGeo(geoDto);
        return "redirect:/geo";
    }


}
