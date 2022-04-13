# 유저 테이블 생성
CREATE TABLE EGOV_USER(
      USER_ID VARCHAR(255),
      USER_PW VARCHAR(255),
      CONSTRAINT PK_USER PRIMARY KEY (USER_ID)
);


# 패키지 생성
create or replace PACKAGE web_main AS
    PROCEDURE selectLogin
    (
        In_userId IN VARCHAR2,
        ref_cursor OUT SYS_REFCURSOR
    );
END web_main;




create or replace PACKAGE BODY WEB_MAIN AS

  PROCEDURE selectLogin
  (
      In_userId IN VARCHAR2,
      ref_cursor OUT SYS_REFCURSOR
  ) IS
  BEGIN
        
        OPEN ref_cursor FOR
           SELECT 
           USER_ID AS USER_ID
           FROM EGOV_USER
           where USER_ID = In_userId;
    
       EXCEPTION WHEN OTHERS THEN
         DBMS_OUTPUT.PUT_LINE('오류발생');
         DBMS_OUTPUT.PUT_LINE('에러내용:'||SQLERRM);
         DBMS_OUTPUT.PUT_LINE('발생위치:'||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
                
  END selectLogin;

END WEB_MAIN;


# 게시판 테이블 생성
CREATE TABLE EGOV_BBS (	
    borderId NUMBER(10,0),
    borderType VARCHAR2(1 BYTE),
    groupNum NUMBER(10,0) DEFAULT 0,
    parentId NUMBER(10,0) DEFAULT 0,
    groupOrder NUMBER(10,0) DEFAULT 0,
    groupTab NUMBER(10,0)  DEFAULT 0,
    userId VARCHAR2(255 BYTE),
    nickName VARCHAR2(255 BYTE),
    writeDay DATE DEFAULT sysdate,
    writerIp VARCHAR2(16),
    editId VARCHAR2(255 BYTE),
    editIp VARCHAR2(16),
    title VARCHAR2(30),
    borderText VARCHAR2(4000),
    seeCount NUMBER(10,0) DEFAULT 0,
    fileName VARCHAR(250),
    fileType VARCHAR(30),
    fileUrl VARCHAR(250),
    replyCount NUMBER(10,0) DEFAULT 0,
    CONSTRAINT PK_BBS PRIMARY KEY(borderid)
);

create sequence borderid_seq;

commit;
