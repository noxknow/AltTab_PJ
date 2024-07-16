# ERD 설계
```sql
### 유저
CREATE TABLE `users` (
	`user_id`	LONG	NULL	DEFAULT AUTO_INCREMENT,
	`user_name`	VARCHAR(50)	NOT NULL,
	`email`	VARCHAR(100)	NOT NULL,
	`password`	VARCHAR(255)	NOT NULL,
	`created_at`	TIMESTAMP	NULL
);

### 스터디 그룹
CREATE TABLE `study_groups` (
	`study_id`	LONG	NOT NULL	DEFAULT AUTO_INCREMENT,
	`study_name`	VARCHAR(100)	NOT NULL,
	`study_info`	TEXT	NULL,
	`created_at`	VARCHAR(255)	NOT NULL
);

### 스터디 문제
CREATE TABLE `study_group_problems` (
	`study_problem_id`	LONG	NOT NULL	DEFAULT AUTO_INCREMENT,
	`ploblem_id`	LONG	NOT NULL,
	`study_id`	LONG	NOT NULL,
	`solved_date`	DATE	NULL
);

### 풀이
CREATE TABLE `solutions` (
	`solution_id`	LONG	NOT NULL	DEFAULT AUTO_INCREMENT,
	`ploblem_id`	LONG	NOT NULL,
	`user_id`	LONG	NOT NULL,
	`solution`	TEXT	NULL,
	`submitted_at`	TIMESTAMP	NULL
);

### 문제
CREATE TABLE `problem` (
	`ploblem_id`	LONG	NOT NULL	DEFAULT AUTO_INCREMENT,
	`title`	VARCHAR(255)	NOT NULL,
	`description`	TEXT	NULL,
	`created_at`	TIMESTAMP	NOT NULL
);

### 유저 - 스터디 연관 테이블
CREATE TABLE `user_study_groups` (
	`user_study_id`	LONG	NOT NULL	DEFAULT AUTO_INCREMENT,
	`user_id`	LONG	NOT NULL,
	`study_id`	LONG	NOT NULL,
	`role`	ENUM('reader', 'member')	NOT NULL
);

### 출석부
CREATE TABLE `attendance` (
	`attendance_id`	LONG	NOT NULL	DEFAULT AUTO_INCREMENT,
	`study_id`	LONG	NOT NULL,
	`user_id`	LONG	NOT NULL,
	`date`	DATE	NOT NULL,
	`status`	ENUM('present', 'absent', 'excused')	NOT NULL,
	`created_at`	TIMESTAMP	NULL
);

### 코드 스니펫
CREATE TABLE `code_snippets` (
	`snippet_id`	LONG	NOT NULL	DEFAULT AUTO_INCREMENT,
	`ploblem_id`	LONG	NOT NULL,
	`code`	TEXT	NOT NULL,
	`language`	VARCHAR(50)	NOT NULL,
	`created_at`	TIMESTAMP	NULL,
	`updated_at`	TIMESTAMP	NULL	DEFAULT CURRENT_TIMESTAMP
);

---

### 제약설정
ALTER TABLE `users` ADD CONSTRAINT `PK_USERS` PRIMARY KEY (
	`user_id`
);

ALTER TABLE `study_groups` ADD CONSTRAINT `PK_STUDY_GROUPS` PRIMARY KEY (
	`study_id`
);

ALTER TABLE `study_group_problems` ADD CONSTRAINT `PK_STUDY_GROUP_PROBLEMS` PRIMARY KEY (
	`study_problem_id`,
	`ploblem_id`,
	`study_id`
);

ALTER TABLE `solutions` ADD CONSTRAINT `PK_SOLUTIONS` PRIMARY KEY (
	`solution_id`,
	`ploblem_id`,
	`user_id`
);

ALTER TABLE `problem` ADD CONSTRAINT `PK_PROBLEM` PRIMARY KEY (
	`ploblem_id`
);

ALTER TABLE `user_study_groups` ADD CONSTRAINT `PK_USER_STUDY_GROUPS` PRIMARY KEY (
	`user_study_id`,
	`user_id`,
	`study_id`
);

ALTER TABLE `attendance` ADD CONSTRAINT `PK_ATTENDANCE` PRIMARY KEY (
	`attendance_id`,
	`study_id`,
	`user_id`
);

ALTER TABLE `code_snippets` ADD CONSTRAINT `PK_CODE_SNIPPETS` PRIMARY KEY (
	`snippet_id`,
	`ploblem_id`
);

ALTER TABLE `study_group_problems` ADD CONSTRAINT `FK_problem_TO_study_group_problems_1` FOREIGN KEY (
	`ploblem_id`
)
REFERENCES `problem` (
	`ploblem_id`
);

ALTER TABLE `study_group_problems` ADD CONSTRAINT `FK_study_groups_TO_study_group_problems_1` FOREIGN KEY (
	`study_id`
)
REFERENCES `study_groups` (
	`study_id`
);

ALTER TABLE `solutions` ADD CONSTRAINT `FK_study_group_problems_TO_solutions_1` FOREIGN KEY (
	`ploblem_id`
)
REFERENCES `study_group_problems` (
	`ploblem_id`
);

ALTER TABLE `solutions` ADD CONSTRAINT `FK_users_TO_solutions_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `users` (
	`user_id`
);

ALTER TABLE `user_study_groups` ADD CONSTRAINT `FK_users_TO_user_study_groups_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `users` (
	`user_id`
);

ALTER TABLE `user_study_groups` ADD CONSTRAINT `FK_study_groups_TO_user_study_groups_1` FOREIGN KEY (
	`study_id`
)
REFERENCES `study_groups` (
	`study_id`
);

ALTER TABLE `attendance` ADD CONSTRAINT `FK_study_groups_TO_attendance_1` FOREIGN KEY (
	`study_id`
)
REFERENCES `study_groups` (
	`study_id`
);

ALTER TABLE `attendance` ADD CONSTRAINT `FK_users_TO_attendance_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `users` (
	`user_id`
);

ALTER TABLE `code_snippets` ADD CONSTRAINT `FK_study_group_problems_TO_code_snippets_1` FOREIGN KEY (
	`ploblem_id`
)
REFERENCES `study_group_problems` (
	`ploblem_id`
);

```