package com.codegrade.restapi.service;

import com.codegrade.restapi.entity.Question;
import com.codegrade.restapi.repository.QuestionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getAllQuestions(){

        return questionRepository.findAll();
    }

    public Optional<Question> getQuestion(UUID questionId){

        return questionRepository.findById(questionId);
    }

    public Question addQuestion(Question newQuestion){

        return questionRepository.save(newQuestion);
    }

    public Question editQuestion(UUID questionId,Question editQuestion){

        var eQuestion= questionRepository.findById(questionId);
        return questionRepository.save(editQuestion);
    }

    public void deleteQuestion(UUID questionId){

        questionRepository.deleteById(questionId);
    }
}
