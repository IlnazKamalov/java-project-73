package hexlet.code.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class WelcomeController {

    @GetMapping("")
    public String sourceRoot() {
        return welcome();
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Spring";
    }
}
