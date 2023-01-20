package hexlet.code.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class WelcomeController {

    @GetMapping("")
    public final String sourceRoot() {
        return welcome();
    }

    @GetMapping("/welcome")
    public final String welcome() {
        return "Welcome to Spring";
    }
}
