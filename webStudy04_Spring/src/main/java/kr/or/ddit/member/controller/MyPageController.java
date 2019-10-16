package kr.or.ddit.member.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.vo.MemberVO;


@Controller
//@SessionAttributes(names="authMember") 세션에 있는 속성을 가져올때 필요한 어노테이션
//파라미터도 modelattribute 세션에 있는 값을 어노테이션으로 가져와야함
public class MyPageController extends HttpServlet {
	@Inject
	IMemberService service;  
	
	@RequestMapping("/mypage")		
	public String mypage(
			HttpSession session, Model model) throws ServletException, IOException {
		String viewName=null;
		MemberVO authMember = (MemberVO) session.getAttribute("authMember");
		MemberVO savedMember = service.retrieveMember(authMember);
		model.addAttribute("savedMember",savedMember);
		viewName = "member/mypage";
		return viewName;
	}
}
