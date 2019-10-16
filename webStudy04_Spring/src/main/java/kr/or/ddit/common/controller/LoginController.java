package kr.or.ddit.common.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.member.exception.NotAuthenticatedException;
import kr.or.ddit.member.exception.UserNotFoundException;
import kr.or.ddit.member.service.IAuthenticateService;
import kr.or.ddit.utils.CookieUtil;
import kr.or.ddit.vo.MemberVO;

@Controller
public class LoginController {
	@Inject
	IAuthenticateService service;
	
	@RequestMapping("/login")
	public String loginGet() {
//		String savedId = new CookieUtil(request).getCookieValue("idCookie");
//		request.setAttribute("savedId", savedId);
		String viewName = "login/loginForm";
		return viewName;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String loginPost(@RequestParam(required=true) String mem_id, 
			@RequestParam(required=true)String mem_pass,
			@RequestParam(name="idSave",required=false) String checkbox,
			HttpServletResponse response,
			HttpSession session
			) throws IOException{
		String viewName = null;
		try {
			
			
			//직접 생성할꺼면 처음부터 끝까지 직접생성, 주입받을꺼면 전부 주입으로 처리.
			MemberVO savedMember = service.authenticate(new MemberVO(mem_id,mem_pass));
			Cookie idCookie = CookieUtil.createCookie("idCookie", mem_id);
			int maxAge = 0;
			if("idSave".equals(checkbox)) {
				maxAge = 60 * 60 * 24 * 2;
			}
			idCookie.setMaxAge(maxAge);
			response.addCookie(idCookie);
			
			session.setAttribute("authMember", savedMember);
			//이동 방식 => 해당 리퀘스트 객체 활용 끝났으니 redirect
			viewName = "redirect:/";
		}catch(UserNotFoundException | NotAuthenticatedException e) {
			session.setAttribute("message", e.getMessage());
			//이동방식 (인증절차이므로 기존 request 삭제)
			viewName = "redirect:/login";
			//메세지 전달 방식
		}
		//아이디와 비번일 동일하면 성공
		//getSession(false) -> 세션이 있으면 session 리턴 없으면 null
		//getSession() -> 없으면 session 만들어서 반환
		//ui먼저 호출되기 때문에 정상적으로 로그인 절차를 거쳤다면 이미 session 이 만들어져있음. 
		if(session==null || session.isNew()) {
			response.sendError(400,"로그인 절차 문제");
			return null;
		}
		return viewName;
		
	}
}
