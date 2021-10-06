CREATE TABLE IF NOT EXISTS assignment_state (
    state varchar(255) NOT NULL,
    CONSTRAINT assignment_state_pkey PRIMARY KEY (state)
);


CREATE TABLE IF NOT EXISTS assignment_type (
    "type" varchar(255) PRIMARY KEY
);


CREATE TABLE IF NOT EXISTS email (
    email    varchar(255) PRIMARY KEY,
    verified bool         NOT NULL
);


CREATE TABLE IF NOT EXISTS user_role (
    "role"      varchar(255) PRIMARY KEY,
    description varchar(255) NULL
);


CREATE TABLE IF NOT EXISTS user_account (
    user_id    uuid         PRIMARY KEY,
    first_name varchar(255) NOT NULL,
    last_name  varchar(255) NOT NULL,
    email      varchar(255) NULL,
    username   varchar(255) NOT NULL,
    "password" varchar(255) NOT NULL,
    is_enabled bool         NOT NULL,
    "role"     varchar(255) NULL,
    CONSTRAINT uk_username UNIQUE (username),
    CONSTRAINT fk_user_account_email FOREIGN KEY (email) REFERENCES email (email),
    CONSTRAINT fk_user_account_role FOREIGN KEY ("role") REFERENCES user_role ("role")
);


CREATE TABLE IF NOT EXISTS "assignment" (
    assignment_id   uuid         PRIMARY KEY,
    description     text         NULL,
    close_time      timestamp    NULL,
    is_scheduled    bool         NULL,
    open_time       timestamp    NULL,
    schedule_job_id int4         NULL,
    title           varchar(255) NULL,
    instructor_id   uuid         NULL,
    state           varchar(255) NULL,
    "type"          varchar(255) NULL,
    CONSTRAINT fk_assignment_instructor FOREIGN KEY (instructor_id) REFERENCES user_account (user_id),
    CONSTRAINT fk_assignment_state FOREIGN KEY (state) REFERENCES assignment_state (state),
    CONSTRAINT fk_assignment_type FOREIGN KEY ("type") REFERENCES assignment_type ("type")
);


CREATE TABLE IF NOT EXISTS participation (
    assignment_id   uuid         PRIMARY KEY,
    user_id         uuid         NOT NULL,
    enrollment_date timestamp    NULL,
    feedback        varchar(255) NULL,
    final_grade     float8       NULL,
    graded_time     timestamp    NULL,
    CONSTRAINT fk_participation_student FOREIGN KEY (user_id) REFERENCES user_account (user_id),
    CONSTRAINT fk_participation_assignment FOREIGN KEY (assignment_id) REFERENCES "assignment" (assignment_id)
);


CREATE TABLE IF NOT EXISTS question (
    question_id   uuid         PRIMARY KEY,
    description   text         NULL,
    difficulty    varchar(255) NULL,
    test_cases    jsonb        NULL,
    title         varchar(255) NULL,
    total_points  int4         NULL,
    instructor_id uuid         NULL,
    CONSTRAINT fk_question_instructor FOREIGN KEY (instructor_id) REFERENCES user_account (user_id)
);


CREATE TABLE IF NOT EXISTS submission (
    submission_id  uuid         PRIMARY KEY,
    evaluated      bool         NULL,
    evaluated_time timestamp    NULL,
    test_cases     jsonb        NULL,
    total_points   int4         NULL,
    "language"     varchar(255) NULL,
    "source"       varchar(255) NULL,
    submitted_time timestamp    NOT NULL,
    assignment_id  uuid         NULL,
    question_id    uuid         NULL,
    user_id        uuid         NULL,
    CONSTRAINT fk_submission_assignment FOREIGN KEY (assignment_id) REFERENCES "assignment" (assignment_id),
    CONSTRAINT fk_submission_question FOREIGN KEY (question_id) REFERENCES question (question_id),
    CONSTRAINT fk_submission_student FOREIGN KEY (user_id) REFERENCES user_account (user_id)
);


CREATE TABLE IF NOT EXISTS assignment_questions (
    assignment_id uuid NOT NULL,
    question_id   uuid NOT NULL,
    CONSTRAINT assignment_questions_pkey PRIMARY KEY (assignment_id, question_id),
    CONSTRAINT fk_aq_question FOREIGN KEY (question_id) REFERENCES question (question_id),
    CONSTRAINT fk_aq_assignment FOREIGN KEY (assignment_id) REFERENCES "assignment" (assignment_id)
);
