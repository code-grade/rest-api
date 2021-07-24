package com.codegrade.restapi.repository;

import com.codegrade.restapi.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserAccountRepositoryTest {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    public void addUser() {
        Student student = Student.builder()
                .username("thilinatlm")
                .password("12345")
                .email("thilina@gmail.com")
                .isVerified(true)
                .isEnabled(true)
                .firstName("Thilina")
                .lastName("Lakshan")
                .indexNo("180371K")
                .avatar("")
                .build();
        userAccountRepository.save(student);
    }
}