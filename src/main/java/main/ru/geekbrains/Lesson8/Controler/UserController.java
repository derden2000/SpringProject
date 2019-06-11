package main.ru.geekbrains.Lesson8.Controler;

import main.ru.geekbrains.Lesson8.Entity.User;
import main.ru.geekbrains.Lesson8.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger.info("User count in repository {}", userRepository.count());
        userRepository.save(new User("ivan", "123", "ivan@mail.ru"));
        userRepository.save(new User("petr", "345", "petr@mail.ru"));
        userRepository.save(new User("julia", "789", "julia@mail.ru"));
    }

    @GetMapping("/")
    public String userList(Model model) {
        model.addAttribute("users",userRepository.findAll());
        return "index";
    }

    @GetMapping("/adduser")
    public String addNewUser(User user) {
        return "add-user";
    }

    @PostMapping("/adduser")
    public String addNewUser(User user, Model model) {
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("user/{id}/edit")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("user/{id}/update")
    public String updateUser(@PathVariable("id") Long id, @Valid User user, Model model) {

        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }

    @GetMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        return "index";
    }
}
