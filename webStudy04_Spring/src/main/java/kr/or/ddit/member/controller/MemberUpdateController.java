package kr.or.ddit.member.controller;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.or.ddit.enums.ServiceResult;
import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.vo.MemberVO;

@Controller
public class MemberUpdateController extends HttpServlet {
	
	@Inject
	IMemberService service;
	
	@RequestMapping(value="/member/memberUpdate.do", method=RequestMethod.POST)
	public String updatePost(@Valid MemberVO mv, Errors errors,
			HttpSession session) throws ServletException, IOException {
		
		boolean valid = !errors.hasErrors();
		String viewName="/mypage";
		String message = null;
		if(valid) {
			ServiceResult result = service.modifyMember(mv);
			switch(result) {
			case INVALIDPASSWORD:
				message = "비번오류";
				break;
			case FAILED:
				message = "서버오류";
				break;
			default:
				message = "수정성공";
				break;
			}
		}else {
			
		}
		session.setAttribute("message", message);
		session.setAttribute("errors", errors);
		return "redirect:"+viewName;
//		if(result.equals(ServiceResult.OK)) {
//			session.setAttribute("message", "수정완료");
//		}else if(result.equals(ServiceResult.FAILED)) {
//			session.setAttribute("message", "수정실패, 재시도 또는 관리자 연락");
//		}else if(result.equals(ServiceResult.INVALIDPASSWORD)) {
//			session.setAttribute("message", "비밀번호 틀림");
//		}
//		String viewName=req.getContextPath()+"/mypage";
//		resp.sendRedirect(viewName);
		
	}
}
//1. 요청받고

//3-1. 통과
//1) 의존성
//2) 로직선택 (ServiceResult modifyMember(memberVO)
// session scope에 메세지 설정
//	-exception(런타임에러->was서버에서 처리가능. 내가 처리안해도 500에러 발생), 
//	-invalidpassword(:redirect ->mypage +msg) , ok(redirect->mypage +msg), failed (dispatch->mypage +msg)
//3-2. 검증 통과안될때