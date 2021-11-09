--DELETE FROM user_account;
--DELETE FROM email; 

INSERT INTO email (email,verified) VALUES
    ('admin1@code-grade.com', true),
    ('admin2@code-grade.com', true),
    ('instructor1@gmail.com', true),
    ('instructor2@gmail.com', true),
    ('instructor3@gmail.com', true),
    ('student1@gmail.com', true),
    ('student2@gmail.com', true),
    ('student3@gmail.com', true),
    ('student4@gmail.com', true),
    ('student5@gmail.com', true);

INSERT INTO user_account (user_id,first_name,last_name,email,username,"password",is_enabled,"role") VALUES
-- admins
   ('28650456-5b1b-4258-97cd-5dc2e9c744b1'::uuid,'Admin1','User1','admin1@code-grade.com','admin1','$2a$10$UfJY3oG87avyjNOtdgxnzuhLtlAG6ISmm6/m4RJNoOv9.DHA0DpUa',true,'ADMIN'),
   ('59b34535-40fd-482c-94bf-772e9fe28365'::uuid,'Admin2','User2','admin2@code-grade.com','admin2','$2a$10$UfJY3oG87avyjNOtdgxnzuhLtlAG6ISmm6/m4RJNoOv9.DHA0DpUa',true,'ADMIN'),
-- instructors
   ('71b329ea-c451-4140-bed0-ad38ed350a29'::uuid,'Instructor1','User1','instructor1@gmail.com','instructor1','$2a$10$UfJY3oG87avyjNOtdgxnzuhLtlAG6ISmm6/m4RJNoOv9.DHA0DpUa',true,'INSTRUCTOR'),
   ('e7d33c04-6312-435b-b9fe-0d778389d2af'::uuid,'Instructor2','User2','instructor2@gmail.com','instructor2','$2a$10$UfJY3oG87avyjNOtdgxnzuhLtlAG6ISmm6/m4RJNoOv9.DHA0DpUa',true,'INSTRUCTOR'),
   ('d0e92e60-8328-4749-b1b8-0f21d3d5c145'::uuid,'Instructor3','User3','instructor3@gmail.com','instructor3','$2a$10$UfJY3oG87avyjNOtdgxnzuhLtlAG6ISmm6/m4RJNoOv9.DHA0DpUa',true,'INSTRUCTOR'),
-- students
   ('b34ae9c7-5510-46eb-b9ef-8c3fa7a2f92e'::uuid,'Student1','User1','student1@gmail.com','student1','$2a$10$UfJY3oG87avyjNOtdgxnzuhLtlAG6ISmm6/m4RJNoOv9.DHA0DpUa',true,'STUDENT'),
   ('91d608a8-8709-46f2-873f-09b82ff3a135'::uuid,'Student2','User2','student2@gmail.com','student2','$2a$10$UfJY3oG87avyjNOtdgxnzuhLtlAG6ISmm6/m4RJNoOv9.DHA0DpUa',true,'STUDENT'),
   ('9c26027f-904c-402c-8b67-d1805ef5711d'::uuid,'Student3','User3','student3@gmail.com','student3','$2a$10$UfJY3oG87avyjNOtdgxnzuhLtlAG6ISmm6/m4RJNoOv9.DHA0DpUa',true,'STUDENT'),
   ('f047c6e1-80e1-4b15-ae98-24f787b3e039'::uuid,'Student4','User4','student4@gmail.com','student4','$2a$10$UfJY3oG87avyjNOtdgxnzuhLtlAG6ISmm6/m4RJNoOv9.DHA0DpUa',true,'STUDENT'),
   ('f6da539d-6e1c-402b-be59-c9b36d47264c'::uuid,'Student5','User5','student5@gmail.com','student5','$2a$10$UfJY3oG87avyjNOtdgxnzuhLtlAG6ISmm6/m4RJNoOv9.DHA0DpUa',true,'STUDENT');

-- delete assignment data
--DROP TABLE public."assignment" CASCADE;
--DROP TABLE public.assignment_questions CASCADE;
--DROP TABLE public.final_submission CASCADE;
--DROP TABLE public.participation CASCADE;
--DROP TABLE public.question CASCADE;
--DROP TABLE public.submission CASCADE;


