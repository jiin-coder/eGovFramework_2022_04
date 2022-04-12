package egov.lib.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleAop {

	private static final Logger logger = LoggerFactory.getLogger(ExampleAop.class);

	public void beforeMethod(JoinPoint joinPoint) throws Exception {
		logger.info("사용자 요청" + joinPoint.getTarget());
		System.out.println("사용자 요청" + joinPoint.getTarget());
	}

	public void afterExceptionMethod(JoinPoint joinPoint, Exception exception) throws Exception {
		// 이외에도 예외처리에는 많은 방법이 존재합니다. ErrorController로 제어등..
		logger.error("ST에러발생=================");
		logger.error("에러위치" + exception.getClass());
		logger.error("에러내용" + exception.getMessage());
		logger.error("ED에러발생=================");
		throw exception;
	}

	public void afterMethod(JoinPoint joinPoint) throws Exception {
		logger.info("메소드 종료");
	}

	public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {

		// 시간 측정
		long st = System.currentTimeMillis();
		System.out.println("공통기능");

		// 핵심기능
		Object rtn = joinPoint.proceed();

		// 시간 측정
		long ed = System.currentTimeMillis();
		System.out.println("공통기능2");

		// 성능 측정 : 소요시간 
		System.out.println("걸린시간" + (ed - st));
		return rtn;
	}
}
