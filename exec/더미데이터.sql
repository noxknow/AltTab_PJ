CREATE TABLE Study (
    study_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    study_name VARCHAR(100) NOT NULL,
    study_description VARCHAR(100) NOT NULL,
    view_count BIGINT DEFAULT 0,
    like_count BIGINT DEFAULT 0,
    study_point BIGINT DEFAULT 0,
    solve_count BIGINT DEFAULT 0
);

CREATE TABLE StudySchedule (
    study_schedule_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    study_id BIGINT,
    deadline DATE,
    FOREIGN KEY (study_id) REFERENCES Study(study_id)
);

CREATE TABLE Problem (
    problem_id BIGINT PRIMARY KEY,
    title VARCHAR(255),
    tag VARCHAR(255),
    level BIGINT,
    representative VARCHAR(255)
);

CREATE TABLE Status (
    status_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT,
    study_id BIGINT,
    problem_id BIGINT
    -- 외래 키 관계가 정의되어 있지 않으므로 필요에 따라 추가하세요
);

CREATE TABLE Notification (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    study_id BIGINT,
    study_name VARCHAR(255),
    member_id BIGINT,
    FOREIGN KEY (member_id) REFERENCES Member(member_id)
);

CREATE TABLE Member (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    avatar_url VARCHAR(255),
    role ENUM('LEADER', 'FOLLOWER') NOT NULL
);

CREATE TABLE CodeSnippet (
    code_snippet_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    language VARCHAR(255),
    code TEXT,
    execution_status ENUM('PENDING', 'RUNNING', 'COMPLETED', 'FAILED'),
    study_id BIGINT,
    problem_id BIGINT,
    member_id BIGINT
    -- 외래 키 관계가 정의되어 있지 않으므로 필요에 따라 추가하세요
);

CREATE TABLE StudyProblem (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    study_id BIGINT,
    problem_id BIGINT,
    deadline DATE,
    presenter VARCHAR(255),
    level BIGINT,
    tag VARCHAR(255),
    problem_status ENUM('PENDING', 'IN_PROGRESS', 'DONE'),
    member_count INT,
    solve_count INT DEFAULT 0,
    FOREIGN KEY (study_id) REFERENCES Study(study_id),
    FOREIGN KEY (problem_id) REFERENCES Problem(problem_id)
);

CREATE TABLE ScheduleProblem (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    study_schedule_id BIGINT,
    problem_id BIGINT,
    presenter VARCHAR(255),
    FOREIGN KEY (study_schedule_id) REFERENCES StudySchedule(study_schedule_id),
    FOREIGN KEY (problem_id) REFERENCES Problem(problem_id)
);

-- drop  cursor.execute('DROP TABLE IF EXISTS study_problem')
--     cursor.execute('DROP TABLE IF EXISTS problem')
--     cursor.execute('DROP TABLE IF EXISTS study')
    
    DROP TABLE IF EXISTS problem;
    DROP TABLE IF EXISTS study_problem
    ;

CREATE TABLE MemberStudy (
    member_study_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT,
    study_id BIGINT,
    role ENUM('LEADER', 'FOLLOWER') NOT NULL,
    member_point BIGINT DEFAULT 0,
    FOREIGN KEY (member_id) REFERENCES Member(member_id),
    FOREIGN KEY (study_id) REFERENCES Study(study_id)
);


select * from problem;
select * from study;
select * from study_problem;
SHOW COLUMNS FROM StudyProblem LIKE 'problem_status';

