package com.codegrade.restapi.service;

import com.codegrade.restapi.entity.Question;
import com.codegrade.restapi.repository.QuestionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionRepo questionRepo;

    public List<Question> getAllQuestions(){

        return questionRepo.findAll();
    }

    public Optional<Question> getQuestion(UUID questionId){

        return questionRepo.findById(questionId);
    }

    public Question addQuestion(Question newQuestion){

        return questionRepo.save(newQuestion);
    }

    public Question editQuestion(Question editQuestion){

        return questionRepo.save(editQuestion);
    }

    public void deleteQuestion(UUID questionId){

        questionRepo.deleteById(questionId);
    }
}
