package com.codegrade.restapi.service;

import com.codegrade.restapi.entity.Question;
import com.codegrade.restapi.entity.User;
import com.codegrade.restapi.exception.ApiException;
import com.codegrade.restapi.repository.QuestionRepo;
import com.codegrade.restapi.utils.RBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class QuestionService {

    private final QuestionRepo questionRepo;

    /**
     * Create new question
     * @param instructor - User (with role instructor)
     * @param question - question data
     * @return Question
     */
    public Question.JustInstructorId create(User instructor, Question question) {
        question.setInstructor(instructor);
        return Question.JustInstructorId.fromQuestion(questionRepo.save(question));
    }

    /**
     * Get question by question id
     * @param questionId - UUID
     * @return Question
     */
    public Question getQuestion(UUID questionId) {
        return questionRepo.findById(questionId).orElseThrow(
                () -> new ApiException(RBuilder.notFound("question id is not exist"))
        );
    };

    /**
     * Get question by id
     * @return - Full question
     */
    public List<Question> getAllQuestions(){
        return questionRepo.findAll();
    }


    public Question editQuestion(Question editQuestion){

        return questionRepo.save(editQuestion);
    }

    public void deleteQuestion(UUID questionId){

        questionRepo.deleteById(questionId);
    }
}
