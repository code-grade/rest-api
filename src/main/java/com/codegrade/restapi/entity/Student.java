package com.codegrade.restapi.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;


@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@SuperBuilder
@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Student extends UserAccount {

    private String firstName;
    private String lastName;
    private String indexNo;
    private String avatar;

}
