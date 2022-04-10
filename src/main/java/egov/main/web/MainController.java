package egov.main.web;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egov.main.service.MainService;

@Controller
public class MainController {
	
	
	@Resource(name="MainService") MainService mainService;

	@RequestMapping(value="/main.do")
	public String main(HttpServletRequest request, Model model){
		return "main";
	}
	
	
	@RequestMapping(value="/main2.do")
	public String main2(HttpServletRequest request, Model model){
		return "main/main2";
	}
		
	
	// form으로부터 입력받은 데이트를 가공 및 처리 1
	
	 /*
	  * id를 user1 입력시 : userNo+5 한 값과 입력한 id가 나오도록
	  * 이 외 : userNo+5 한 값과 "잘못된 입력"이란 글자가 나오도록
	  */
//	@RequestMapping(value="/main3.do")
//	public String main3(HttpServletRequest request, ModelMap model){
//		
//		int userNo = Integer.parseInt(request.getParameter("userNo").toString());
//		String id = request.getParameter("id").toString();
//		
//		if(id.equals("user1")){
//			model.addAttribute("userId", id);
//		}
//		else{
//			model.addAttribute("userId", "잘못된 입력");
//		}
//		
//		userNo = userNo + 5;
//		model.addAttribute("userNo", userNo);
//		
//		return "main/main3";
//	}

	
	// form으로부터 입력받은 데이트를 가공 및 처리 1
	// id와 pw를 input으로 제출 시, userNo와 pw 나오도록
	@RequestMapping(value="/main3.do")
	public String main3(@RequestParam("pw")String pw, HttpServletRequest request, ModelMap model){
		
		int userNo = Integer.parseInt(request.getParameter("userNo").toString());
		String id = request.getParameter("id").toString();
		
		if(id.equals("user1")){
			model.addAttribute("userId", pw);
		}
		else{
			model.addAttribute("userId", pw);
		}
		
		userNo = userNo + 5;
		model.addAttribute("userNo", userNo);
		
		return "main/main3";
	}
	
	
	
	// @PathVariable를 이용해 userNo를 path(Url 경로)의 일부로 파라미터 사용
	// http://localhost:8080/Egov_WEB/main4/23123123.do
	@RequestMapping(value="/main4/{userNo}.do")
	public String main4(@PathVariable String userNo, HttpServletRequest request, ModelMap model){
		
		model.addAttribute("userNo", userNo);
		
		return "main/main3";
	}
	
	
	@RequestMapping(value="/login.do")
	public String login(HttpServletRequest request, ModelMap model){
		return "login/login";
	}

	@RequestMapping(value = "/loginSubmission.do")
	public String main5(HttpServletRequest request, ModelMap model) throws Exception{
		String userId = request.getParameter("id").toString();
		
		// id를 10자 이상 입력할 시, 로그인페이지로 리다이렉트 
		if (userId.length() > 10){
			return "redirect:/login.do";
		}
		
		// VO
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		paramMap.put("userId", userId);
		
		resultMap = mainService.selectLogin(paramMap);
		
		// 일치하는 id가 미존재할 시
		if(null == resultMap){
			return "redirect:/login.do";
		}
		
		// 일치하는 id가 존재할 시
		request.getSession().setAttribute("USER_ID", userId);
		
		return "main/main4";
	}
	
	// 로그인 성공으로 loginSubmission.do로 이동 후, main4.do로 가도 동일한 화면이 나오돍
	// 로그인하지 않고 main4로 가면 화면이 안나옴
	@RequestMapping(value="/main4.do")
	public String main4(HttpServletRequest request, ModelMap model){
		return "main/main4";
	}
}

