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
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class BorderController {

	private static final Logger logger = LoggerFactory.getLogger(BorderController.class);

	@Resource(name = "BorderService")
	BorderService borderService;

	
	/* 게시글 등록 page */
	@RequestMapping(value = "/borderWrite.do")
	public String borderWrite(HttpServletRequest request, ModelMap model) {
		String userId = "";

		// 글쓰기권한 검사도 가능.
		if (request.getSession().getAttribute("USER_ID") == null) {
			request.getSession().invalidate();
			return "redirect:/login.do";
		} else {
			userId = request.getSession().getAttribute("USER_ID").toString();
		}

		model.addAttribute("userId", userId);
		return "border/borderwrite";
	}

	/* 게시글 등록 processing */
	@RequestMapping(value = "/borderInsert.do")
	public String borderInsert(HttpServletRequest request, ModelMap model) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();

		String title = request.getParameter("title").toString();
		String mytextarea = request.getParameter("mytextarea").toString();
		String userId = "";

		// javascript 유효성
		// 서버단에서 재검증.
		if (title.length() > 15) {
			return "redirect:/borderWrite.do";
		}
		// 2000자이상이면
		else if (mytextarea.length() > 2000) {
			return "redirect:/borderWrite.do";
		} else if (request.getSession().getAttribute("USER_ID") == null) {
			request.getSession().invalidate();
			return "redirect:/login.do";
		} else {
			userId = request.getSession().getAttribute("USER_ID").toString();
			paramMap.put("userId", userId);
			paramMap.put("userIp", request.getRemoteAddr());
			paramMap.put("title", title);
			paramMap.put("mytextarea", mytextarea);
		}

		borderService.insertBorder(paramMap);

		return "redirect:/borderList.do";
	}

	/* 게시글 리스팅 page */
	@RequestMapping(value = "/borderList.do")
	public String borderList(HttpServletRequest request, ModelMap model) throws Exception {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("ref_cursor", null);

		
		/* 페이징 처리 Start */
		/* 사용자의 요청 페이지 번호 */
		String pageNo = request.getParameter("pageNo");
		
		if (null == pageNo||pageNo.equals("")){
			pageNo = "1";
		}
		else {
			pageNo = request.getParameter("pageNo").toString();
		}
		
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(Integer.parseInt(pageNo)); // 현재 페이지 번호
		paginationInfo.setPageSize(10); // 한 페이지에 게시되는 게시물 건수
		paginationInfo.setRecordCountPerPage(10); // 페이징 리스트의 사이즈 입력 ex) 10 : <1> ~ <10>
		
		paramMap.put("currentPageNo", paginationInfo.getCurrentPageNo());
		paramMap.put("RecordCountPerPage", paginationInfo.getRecordCountPerPage());
		/* 페이징 처리 End */
		
		borderService.selectBorder(paramMap);
		
		int listcount = Integer.parseInt(paramMap.get("list_count").toString());
		paginationInfo.setTotalRecordCount(listcount);
		
		list = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
		
		model.addAttribute("borderlist", list);
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("pageNo", pageNo);

		return "border/borderlist";
	}

	/* 게시글 상세 page */
	@RequestMapping(value = "/borderView.do")
	public String borderView(HttpServletRequest request, ModelMap model) throws Exception {
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();

		String borderId = request.getParameter("no").toString();
		
		paramMap.put("borderId", borderId);
		paramMap.put("ref_cursor", null);

		borderService.selectBorderView(paramMap);
		
		resultList = (ArrayList<HashMap<String, Object>>) paramMap.get("ref_cursor");
		
		if (request.getSession().getAttribute("USER_ID") == null) 
		{
			model.addAttribute("userid","");
		} 
		else 
		{
			model.addAttribute("userId", request.getSession().getAttribute("USER_ID").toString());
			
		}
		model.addAttribute("resultList", resultList);

		
		return "border/borderview";
	}

	/* 답글 등록 processing */
	@RequestMapping(value = "/borderReply.do")
	public String borderReply(HttpServletRequest request, ModelMap model) throws Exception {
		String userId = "";
		String no = "";
		
		if (request.getSession().getAttribute("USER_ID") == null) {
			request.getSession().invalidate();
			return "redirect:/login.do";
		} else {
			userId = request.getSession().getAttribute("USER_ID").toString();
		}
		
		no = request.getParameter("no").toString();
		model.addAttribute("userId", userId);
		model.addAttribute("no", no);

		return "border/borderreply";
	}

	
	@RequestMapping(value = "/borderReplyReq.do")
	public String borderReplyReq(HttpServletRequest request, ModelMap model) throws Exception {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();

		String title = request.getParameter("title").toString();
		String mytextarea = request.getParameter("mytextarea").toString();
		String userId = "";
		String no = request.getParameter("no").toString();

		if (title.length() > 15) 
		{
			return "redirect:/borderList.do";
		} 
		else if (mytextarea.length() > 2000) 
		{
			return "redirect:/borderList.do";
		} 
		else if (request.getSession().getAttribute("USER_ID") == null) 
		{
			request.getSession().invalidate();
			return "redirect:/borderList.do";
		} 
		else 
		{
			userId = request.getSession().getAttribute("USER_ID").toString();
			paramMap.put("borderid", no);
			paramMap.put("userId", userId);
			paramMap.put("userIp", request.getRemoteAddr());
			paramMap.put("title", title);
			paramMap.put("mytextarea", mytextarea);
		}

		borderService.insertBorderReply(paramMap);

		return "redirect:/borderList.do";
	}
	
	/* 게시글 수정 */
	@RequestMapping(value = "/borderEdit.do")
	public String borderEdit(HttpServletRequest request, ModelMap model) throws Exception {
		String userId = "";
		String no = "";
		
		if (request.getSession().getAttribute("USER_ID") == null) 
		{
			request.getSession().invalidate();
			return "redirect:/login.do";
		} 
		else
		{
			userId = request.getSession().getAttribute("USER_ID").toString();
		}
		
		no = request.getParameter("no").toString();
		model.addAttribute("userId", userId);
		model.addAttribute("no", no);

		return "border/borderedit";
	}
	
	@RequestMapping(value = "/borderEditReq.do")
	public String borderEditReq(HttpServletRequest request, ModelMap model) throws Exception {
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();

		String title = request.getParameter("title").toString();
		String mytextarea = request.getParameter("mytextarea").toString();
		String userId = "";
		String no = request.getParameter("no").toString();

		if (title.length() > 15) 
		{
			return "redirect:/borderList.do";
		} 
		else if (mytextarea.length() > 2000) 
		{
			return "redirect:/borderList.do";
		} 
		else if (request.getSession().getAttribute("USER_ID") == null) 
		{
			request.getSession().invalidate();
			return "redirect:/borderList.do";
		} 
		else 
		{
			userId = request.getSession().getAttribute("USER_ID").toString();
			paramMap.put("borderid", no);
			paramMap.put("userId", userId);
			paramMap.put("userIp", request.getRemoteAddr());
			paramMap.put("title", title);
			paramMap.put("mytextarea", mytextarea);
		}
  
		borderService.updateBorderEdit(paramMap);

		return "redirect:/borderList.do";
	}
	
	/* 게시글 삭제 processing*/
	@RequestMapping(value = "/borderRemove.do")
	public String borderRemove(HttpServletRequest request, ModelMap model) throws Exception {
		
		HashMap<String, Object> paramMap = new HashMap<String, Object>();

		String userId = "";
		String no = request.getParameter("no").toString();

		if (request.getSession().getAttribute("USER_ID") == null) 
		{
			request.getSession().invalidate();
			return "redirect:/borderList.do";
		} 
		else 
		{
			userId = request.getSession().getAttribute("USER_ID").toString();
			paramMap.put("borderid", no);
			paramMap.put("userId", userId);
			paramMap.put("userIp", request.getRemoteAddr());
		}

		borderService.updateBorderRemove(paramMap);

		return "redirect:/borderList.do";
	}

}