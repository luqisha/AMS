create database dbproject;
use dbproject;
create table Courses
(
CourseID int identity(1001, 1) primary key,
CourseNo varchar(50) not null,
CourseTitle varchar(50) not null,
CourseSession varchar(50) not null --year and semester combination
);

INSERT INTO Courses(CourseNo,CourseTitle,CourseSession) VALUES('CSE3100','SD','Spring-2020');
INSERT INTO Courses(CourseNo,CourseTitle,CourseSession) VALUES('CSE3101','DB','Spring-2020');
SELECT * FROM Courses WHERE CourseNo='CSE3100' AND CourseSession = 'Spring-2020';

create table Students
(
StudentID varchar(50) primary key,
StudentName varchar(50) not null,
section varchar(10) not null,
semester varchar(10) not null

);

INSERT INTO Students VALUES('190104143','Ineffable','C','3.1');
INSERT INTO Students VALUES('190104140','Ashique','C','3.1');
INSERT INTO Students VALUES('190104137','Swarna','C','3.1');
INSERT INTO Students VALUES('190104116','Toufique','C','3.1');


create table QuizMark(
	CourseID int, 
	StudentID varchar(50),
	QuizNo int,
	QuizMarks float,
	constraint student_fk foreign key(StudentID) references Students(StudentID),
	constraint course_fk foreign key(CourseID) references Courses(CourseID),
	constraint quizmarkid primary key(StudentID,CourseID)
)

-- many to many join table
create table StudentCourseJoin(

	CourseID int, 
	StudentID varchar(50),
	constraint scj_student_fk foreign key(StudentID) references Students(StudentID),
	constraint scj_course_fk foreign key(CourseID) references Courses(CourseID)
);

INSERT INTO StudentCourseJoin VALUES(1001,'190104143');
INSERT INTO StudentCourseJoin VALUES(1001,'190104140');
INSERT INTO StudentCourseJoin VALUES(1002,'190104137');
INSERT INTO StudentCourseJoin VALUES(1002,'190104116');

TRUNCATE TABLE StudentCourseJoin;


select * from StudentCourseJoin;

create table Attends(
	AID int identity(1000,1),
	StudentID varchar(50),
	CourseID int,
	ClassNo int,
	ClassDate date default getDate(),
	constraint att_student_fk foreign key(StudentID) references Students(StudentID),
	constraint att_course_fk foreign key(CourseID) references Courses(CourseID)
)

INSERT INTO Attends(StudentID,CourseID,ClassNo,ClassDate) VALUES('190104143',1001,1,GETDATE());
INSERT INTO Attends(StudentID,CourseID,ClassNo,ClassDate) VALUES('190104143',1001,2,GETDATE());
INSERT INTO Attends(StudentID,CourseID,ClassNo,ClassDate) VALUES('190104140',1001,1,GETDATE());


create table Users
(
username varchar(50) primary key,
name varchar(50),
email varchar(50) not null,
password varchar(50)
);


insert into Users values('ineffable','swapnil','gg@ez.com','1234');

drop table Users

SELECT StudentCourseJoin.StudentID, Attends.ClassNo  
FROM StudentCourseJoin
LEFT JOIN ATTENDS
ON StudentCourseJoin.CourseID = ATTENDS.courseID 
AND Attends.StudentID = StudentCourseJoin.StudentID
WHERE StudentCourseJoin.CourseID=1001;


SELECT SelectedStudents.StudentID,
CASE WHEN SelectedAttends.ClassNo IS NULL THEN 0
ELSE 1
END AS Present
FROM (SELECT * FROM StudentCourseJoin WHERE CourseID = 1001) 
AS SelectedStudents
LEFT JOIN (SELECT * FROM Attends WHERE ClassNO = 2) AS SelectedAttends
ON SelectedAttends.StudentID = SelectedStudents.StudentID;

SELECT * from StudentCourseJoin;
