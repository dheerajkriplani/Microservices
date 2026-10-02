package com.microservices.question_service.controller;


import com.microservices.question_service.entity.Question;
import com.microservices.question_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping("/create")
    public Question createQuestion(@RequestBody Question question) {
        return questionService.add(question);
    }

    @GetMapping("/getAll")
    public List<Question> getAll() {
        return questionService.get();
    }

    @GetMapping("/get/{id}")
    public Question get(@PathVariable Long id) {
        return questionService.get(id);
    }

    @GetMapping("getByQuizId/{quizId}")
    public List<Question> getByQuizId(@PathVariable Long quizId) {
        return questionService.getByQuizId(quizId);
    }
}
