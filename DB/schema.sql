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


# 게시글 상세, 계층형 답글 구현
# Web_main 
create or replace PACKAGE WEB_MAIN AS
        PROCEDURE selectLogin
        (
            In_userId IN VARCHAR2,
            ref_cursor OUT SYS_REFCURSOR
        );
        
        PROCEDURE insertBorder
        (
            In_userId IN VARCHAR2,
            In_userIp IN VARCHAR2,
            In_title IN VARCHAR2,
            In_mytextarea IN VARCHAR2
        );
        
         PROCEDURE selectBorder
        (
            ref_cursor OUT SYS_REFCURSOR
        );
        
        PROCEDURE selectView
        (
            In_borderId IN VARCHAR2,
            ref_cursor OUT SYS_REFCURSOR
        );
        
        PROCEDURE insertBorderReply
        (
            In_borderId IN VARCHAR2,
            In_userId IN VARCHAR2,
            In_userIp IN VARCHAR2,
            In_title IN VARCHAR2,
            In_mytextarea IN VARCHAR2
        );

END web_main;

# Web_main_body
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


    PROCEDURE insertBorder
    (
        In_userId IN VARCHAR2,
        In_userIp IN VARCHAR2,
        In_title IN VARCHAR2,
        In_mytextarea IN VARCHAR2
    ) IS
    BEGIN
        
        INSERT INTO EGOV_BBS 
        (
        borderid,
        bordertype,
        group_num,
        parentid,
        group_order,
        group_tab,
        userid,
        nickname,
        writerip,
        editid,
        editip,
        title,
        bordertext,
        seecount,
        filename,
        filetype,
        fileurl,
        replycount
        )
        VALUES
        (
        borderid_seq.nextval,
        '0',
        borderid_seq.currval,
        0,
        0,
        0,
        In_userId,
        In_userId,
        In_userIp,
        '',
        '',
        In_title,
        In_mytextarea,
        0,
        '',
        '',
        '',
        0
        );
        
        COMMIT;
    
       EXCEPTION WHEN OTHERS THEN
         DBMS_OUTPUT.PUT_LINE('오류발생');
         DBMS_OUTPUT.PUT_LINE('에러내용:'||SQLERRM);
         DBMS_OUTPUT.PUT_LINE('발생위치:'||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
                
    END insertBorder;  
  
  
    PROCEDURE selectBorder
    (
      ref_cursor OUT SYS_REFCURSOR
    ) IS
    BEGIN
        
        OPEN ref_cursor FOR
        SELECT * 
        FROM EGOV_BBS 
        order by group_num desc,group_order asc;
    
       EXCEPTION WHEN OTHERS THEN
         DBMS_OUTPUT.PUT_LINE('오류발생');
         DBMS_OUTPUT.PUT_LINE('에러내용:'||SQLERRM);
         DBMS_OUTPUT.PUT_LINE('발생위치:'||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
                
    END selectBorder;
  
  
    PROCEDURE selectView
    (
        In_borderId IN VARCHAR2,
        ref_cursor OUT SYS_REFCURSOR
    ) IS
    BEGIN
        
        OPEN ref_cursor FOR
        SELECT BORDERID,TITLE,BORDERTEXT,NICKNAME 
        FROM EGOV_BBS 
        WHERE BORDERID = In_borderId;
    
       EXCEPTION WHEN OTHERS THEN
         DBMS_OUTPUT.PUT_LINE('오류발생');
         DBMS_OUTPUT.PUT_LINE('에러내용:'||SQLERRM);
         DBMS_OUTPUT.PUT_LINE('발생위치:'||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
                
    END selectView;
  
  
    PROCEDURE insertBorderReply
    (
        In_borderId IN VARCHAR2,
        In_userId IN VARCHAR2,
        In_userIp IN VARCHAR2,
        In_title IN VARCHAR2,
        In_mytextarea IN VARCHAR2
    ) IS    
    lo_group_num NUMBER(10,0);
    lo_group_order NUMBER(10,0);
    lo_group_tab NUMBER(10,0);
    BEGIN
        
        SELECT
        group_num,
        group_order,
        group_tab
        INTO
        lo_group_num,
        lo_group_order,
        lo_group_tab
        FROM EGOV_BBS
        WHERE BORDERID = In_borderId;
        
        /* 기존글 형식을 맞추기 위한 업데이트*/
        UPDATE EGOV_BBS
        SET group_order = lo_group_order+1
        WHERE group_num = lo_group_num
        AND lo_group_order < group_order;
        
        IF(lo_group_order = 0) 
        THEN
            SELECT
            MAX(group_order)
            INTO
            lo_group_order
            FROM EGOV_BBS
            WHERE group_num = lo_group_num;
        END IF;
        
        /*답글을 달고자하는 글의 데이터를 로컬변수에 저장*/
        INSERT INTO EGOV_BBS
        (
        borderid,
        bordertype,
        group_num,
        parentid,
        group_order,
        group_tab,
        userid,
        nickname,
        writerip,
        editid,
        editip,
        title,
        bordertext,
        seecount,
        filename,
        filetype,
        fileurl,
        replycount
        )
        VALUES
        (
        borderid_seq.nextval,
        '0',
        lo_group_num,
        In_borderId,
        lo_group_order+1,
        lo_group_tab+1,
        In_userId,
        In_userId,
        In_userIp,
        '',
        '',
        In_title,
        In_mytextarea,
        0,
        '',
        '',
        '',
        0
        );
        
        COMMIT;
    
       EXCEPTION WHEN OTHERS THEN
         DBMS_OUTPUT.PUT_LINE('오류발생');
         DBMS_OUTPUT.PUT_LINE('에러내용:'||SQLERRM);
         DBMS_OUTPUT.PUT_LINE('발생위치:'||DBMS_UTILITY.FORMAT_ERROR_BACKTRACE);
                
    END insertBorderReply; 
END WEB_MAIN;
