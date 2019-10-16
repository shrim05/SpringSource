package kr.or.ddit.common.interceptors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import kr.or.ddit.member.service.IMemberService;

//어댑터 패턴, 인터페이스 impl시 필요없는 메서드 구현해야함. 이를 방지하기 위해 어댑터를 상속
public class AuthenticationInterceptor extends HandlerInterceptorAdapter{
	//필터는 컨터이너 밖에 있어서.(was관리) 스프링 기능 사용 x , 인터셉터는 스프링이 관리. inject등 사용이 가능
	@Inject
	IMemberService service;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		boolean pass = true;
		pass = !session.isNew();
		if(pass) {
			Object authMember = session.getAttribute("authMember");
			pass = authMember !=null;
		}
		if(!pass) {
			response.sendRedirect(request.getContextPath()+"/login");
		}
		return pass;
	}

}
