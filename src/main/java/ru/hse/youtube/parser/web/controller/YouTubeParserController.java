package ru.hse.youtube.parser.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hse.youtube.parser.service.YouTubeParserService;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class YouTubeParserController {

    private final YouTubeParserService service;

    @GetMapping
    public String main(Model model) {
        model.addAttribute("url", "");
        model.addAttribute("commentCount", service.getTotalComments());

        return "main";
    }

    @GetMapping("/find")
    public String find(Model model) {
        model.addAttribute("commentCount", service.getTotalComments());
        model.addAttribute("comment", service.getRandomComment());

        return "main";
    }

    @PostMapping("/save")
    public String saveComments(@RequestParam(name="url") String url, Model model)
            throws IOException, GeneralSecurityException {
        service.saveComments(url);

        model.addAttribute("url", url);
        model.addAttribute("commentCount", service.getTotalComments());

        return "main";
    }

}
