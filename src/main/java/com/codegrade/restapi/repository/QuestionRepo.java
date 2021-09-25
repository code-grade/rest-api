package com.codegrade.restapi.repository;

import com.codegrade.restapi.entity.Question;
import com.codegrade.restapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepo extends JpaRepository<Question, UUID> {

    List<Question> findQuestionByInstructor(User instructor);

}
