package egov.border.web;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egov.border.service.BorderService;
import egov.main.service.MainService;

@Controller
public class BorderController {

	private static final Logger logger = LoggerFactory.getLogger(BorderController.class);
	
	@Resource(name="BorderService") BorderService borderService;
	
	@RequestMapping(value="/borderWrite.do")
	public String borderWrite(HttpServletRequest request,ModelMap model)
	{
		String userId="";
		
		//글쓰기권한 검사도 가능.
		if(request.getSession().getAttribute("USER_ID") == null)
		{
			request.getSession().invalidate();
			return "redirect:/login.do";
		}
		else
		{
			userId = request.getSession().getAttribute("USER_ID").toString();
		}
			
		model.addAttribute("userId",userId);
		return "border/borderwrite";
	}
	
	
	@RequestMapping(value="/borderInsert.do")
	public String borderInsert(HttpServletRequest request,ModelMap model)throws Exception
	{
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		
		String title = request.getParameter("title").toString();
		String mytextarea = request.getParameter("mytextarea").toString();
		String userId= "";
		
		//javascript 유효성
		//서버단에서 재검증.
		if(title.length()>15)
		{	
			return "redirect:/borderWrite.do";
		}
		//2000자이상이면
		else if(mytextarea.length()>2000)
		{
			return "redirect:/borderWrite.do";
		}
		else if(request.getSession().getAttribute("USER_ID") == null)
		{
			request.getSession().invalidate();
			return "redirect:/login.do";
		}
		else
		{
			userId = request.getSession().getAttribute("USER_ID").toString();
			paramMap.put("userId", userId);
			paramMap.put("userIp", request.getRemoteAddr());
			paramMap.put("title", title);
			paramMap.put("mytextarea", mytextarea);
		}
		
		borderService.insertBorder(paramMap);
		
		return "redirect:/borderList.do";
	}

	
	@RequestMapping(value="/borderList.do")
	public String borderList(HttpServletRequest request,ModelMap model) throws Exception
	{
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("ref_cursor",null);
		
		borderService.selectBorder(paramMap);
		
		list = (ArrayList<HashMap<String,Object>>)paramMap.get("ref_cursor");
		model.addAttribute("borderlist",list);
		
		return "border/borderlist";
	}
}