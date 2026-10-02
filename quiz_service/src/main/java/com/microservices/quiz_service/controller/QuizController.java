package com.microservices.quiz_service.controller;


import com.microservices.quiz_service.entity.Quiz;
import com.microservices.quiz_service.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/create")
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        return quizService.add(quiz);
    }

    @GetMapping("/getAll")
    public List<Quiz> getAll() {
        return quizService.get();
    }

    @GetMapping("/get/{id}")
    public Quiz get(@PathVariable Long id) {
        return quizService.get(id);
    }
}
