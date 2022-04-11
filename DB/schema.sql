# 유저 테이블 생성
CREATE TABLE EGOV_USER(
      USER_ID VARCHAR(255),
      USER_PW VARCHAR(255),
      CONSTRAINT PK_USER PRIMARY KEY (USER_ID)
);



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