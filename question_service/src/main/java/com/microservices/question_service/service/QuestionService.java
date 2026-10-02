package com.microservices.question_service.service;

import com.microservices.question_service.entity.Question;

import java.util.List;

public interface QuestionService {
        Question add(Question question);

        List<Question> get();

        Question get(Long id);

        List<Question> getByQuizId(Long quizId);
}
