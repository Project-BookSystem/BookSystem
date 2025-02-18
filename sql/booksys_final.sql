create database book;
use book;
show tables;
#commit;
#drop database book;

create table book(
	book_id int unsigned auto_increment primary key,	# => join 시 활용
	title varchar(128) not null,						# 책 제목
	author varchar(128) not null,						# 작가
	isbn varchar(24) not null,							# isbn코드
	publisher varchar(32),								# 출판사
	issue_year char(10) not null,						# 출간년도
    total_qty int unsigned default 1, 					# 총 수량
	qty int unsigned default 0,							# 빌린 수량
	book_created datetime default current_timestamp,	# 등록날짜
	book_updated datetime default current_timestamp	on update current_timestamp	# 수정날짜
);
desc book;
select * from book;
drop table book;

create table member(
	name varchar(32) not null,							# 이름
	user_id varchar(32) not null primary key,			# 아이디
	password varchar(24) not null,						# 비밀번호
	birthday char(8) not null,							# 생년월일
	gender char(1),										# 성별 - M/F
	mobile varchar(16) not null,						# 전화번호
	overdue char(1),									# 연체 - 0/1
	member_created datetime default current_timestamp,	# 회원등록날짜
	member_updated datetime default current_timestamp on update current_timestamp	# 회원정보 수정날짜
);
desc member;
select * from member;
drop table member;

create table rent (
	rent_id int unsigned auto_increment primary key,
	book_id int unsigned not null, foreign key(book_id) references book(book_id),								# book 테이블의 id
	member_id varchar(32) not null, foreign key(member_id) references member(user_id),							# member 테이블의 user_id
	rent datetime DEFAULT CURRENT_TIMESTAMP,																	# 빌린날짜
	returned datetime,																							# 실제 반납 날짜
	expected datetime DEFAULT (CURRENT_TIMESTAMP + INTERVAL 2 WEEK),											# 반납 예정일		-> rent + 14days
	extension tinyint(1) default false,																			# 연장 - 0/1		-> expected + 7days
	extension_count int default 0,																				# 연장횟수	
    overdue boolean default false, 																				# 연체여부 - 0/1
    overdays int default 0																						# 연체일수
);				
select * from rent;
drop table rent;

-- 연장 2회 이상, 반납 선택, 연체 처리 포함
DELIMITER $$
CREATE TRIGGER before_return_update
BEFORE UPDATE ON rent
FOR EACH ROW
BEGIN
    IF OLD.extension_count >= 2 THEN
        -- 연장이 2번 이상일 때, 반납만 가능
            IF NEW.returned IS NOT NULL THEN
            SET NEW.returned = current_timestamp;
                -- 반납이 늦었을 경우 연체 처리
            IF NEW.expected < NEW.returned THEN
                SET NEW.overdue = TRUE;
                SET NEW.overdays = DATEDIFF(NOW(), NEW.expected);
            ELSE
                SET NEW.overdue = FALSE;
                SET NEW.overdays = 0;
			END IF;
            ELSE
				SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '연장이 2번 완료되어 더 이상 연장이 불가능합니다.';
            END IF;
		ELSE
		-- 연장이 2번 미만일 때 연장 가능
        IF NEW.extension = 1 THEN
            SET NEW.expected = DATE_ADD(OLD.expected, INTERVAL 1 WEEK);
            SET NEW.extension_count = OLD.extension_count + 1;
        END IF;
    END IF;
END $$
DELIMITER ;
