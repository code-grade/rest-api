package com.codegrade.restapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlagiarismReport {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID reportId;

    private UUID assignmentId;
    private UUID question_id;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<Object> report;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date generated_time = new Date();

}
