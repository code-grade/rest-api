INSERT INTO user_role ("role", description)
    VALUES
        ('ADMIN', 'An user with administrator privileges'),
        ('INSTRUCTOR', 'An user who can coordinate students'),
        ('STUDENT', 'An user who can participate for assignment')
    ON CONFLICT DO NOTHING;


INSERT INTO assignment_state (state)
    VALUES
        ('DRAFT'),
        ('PUBLISHED'),
        ('OPEN'),
        ('AUTO_OPEN'),
        ('CLOSED'),
        ('FINALIZED')
    ON CONFLICT DO NOTHING;

INSERT INTO assignment_type (type)
    VALUES
        ('PUBLIC'),
        ('PRIVATE')
    ON CONFLICT DO NOTHING;
