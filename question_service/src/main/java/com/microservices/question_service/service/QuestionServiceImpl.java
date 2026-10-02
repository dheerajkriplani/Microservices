package com.microservices.question_service.service;

import com.microservices.question_service.entity.Question;
import com.microservices.question_service.repo.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService{

    @Autowired
    private  QuestionRepository questionRepository;


    @Override
    public Question add(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public List<Question> get() {
        return questionRepository.findAll();
    }

    @Override
    public Question get(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Question> getByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }
}
