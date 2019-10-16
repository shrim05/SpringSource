package kr.or.ddit.member.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.enums.ServiceResult;
import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.vo.MemberVO;

@Controller
public class MemberDeleteController {
	@Resource(type=IMemberService.class,name="memberService")
	IMemberService service;
	
	@RequestMapping(value="/member/memberDelete.do", method=RequestMethod.POST)
	public String doPost(HttpSession session, 
			@RequestParam(required=true) String password,
			HttpServletResponse response) throws ServletException, IOException {
		MemberVO authMember = (MemberVO)session.getAttribute("authMember");
		if(session.isNew() || authMember==null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		String viewName=null;
		MemberVO member = new MemberVO(authMember.getMem_id(),password);
		viewName = "/login"; 
		String message = null;
		ServiceResult result = service.removeMember(member);
		switch(result) {
		case INVALIDPASSWORD:
			message ="비번오류";
			viewName = "/mypage";
			session.setAttribute("message", message);
			break;
		case FAILED:
			message = "서버오류";
			viewName = "/mypage";
			session.setAttribute("message", message);
			break;
		default:
			viewName = "/";
			session.invalidate();
			break;
		}
		String tmp = "redirect:";
		return tmp+viewName;
	}
}
