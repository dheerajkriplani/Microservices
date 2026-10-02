package com.microservices.quiz_service.service;

import com.microservices.quiz_service.entity.Quiz;

import java.util.List;

public interface QuizService {
    Quiz add(Quiz quiz);

    List<Quiz> get();

    Quiz get(Long id);
}
