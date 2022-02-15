use dbproject;
create table Courses
(
CourseID int identity(1001, 1) primary key,
CourseNo varchar(50) not null,
CourseTitle varchar(50) not null,
CourseSession varchar(50) not null --year and semester combination
);


create table Students
(
StudentID varchar(50) primary key,
StudentName varchar(50) not null,
section varchar(10) not null,
semester varchar(10) not null

);


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
	constraint student_fk foreign key(StudentID) references Students(StudentID),
	constraint course_fk foreign key(CourseID) references Courses(CourseID)
	
)

create table Attends(
	AID int identity(1000,1),
	StudentID varchar(50),
	CourseID varchar(50),
	ClassNo int,
	ClassDate date default getDate(),
	constraint student_fk foreign key(StudentID) references Students(StudentID),
	constraint course_fk foreign key(CourseID) references Courses(CourseID)
)


create table Users
(
username varchar(50) primary key,
name varchar(50),
email varchar(50) not null,
password varchar(50)
);


insert into Users values('ineffable','swapnil','gg@ez.com','1234');

drop table Users
