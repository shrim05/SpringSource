package kr.or.ddit.member.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.or.ddit.enums.ServiceResult;
import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.vo.MemberVO;

@Controller
@RequestMapping("/member/memberInsert.do")
public class MemberInsertController extends HttpServlet{
   @Inject
   IMemberService service;
   
   @RequestMapping()
   public String insertGet()  {
      return "member/memberForm";
   }
   
   
   @RequestMapping(method=RequestMethod.POST)
   public String insertPost(
		   //객체 검증의 대상은 command object.
		   @Valid @ModelAttribute("member") MemberVO member,
		   Errors errors,
		   Model model
		  ) {
	   
	  boolean valid = !errors.hasErrors();
      String viewName = "member/memberForm";
      String message = null;
      boolean redirect = false;
      
      if(valid) {
         ServiceResult result = service.createMember(member);
         switch (result) {
         case PKDUPLICATED:
            message = "아이디 중복";
            viewName = "member/memberForm";
            break;
         case FAILED:
            message = "서버 오류";
            viewName = "member/memberForm";
            break;
         default:
            message = "회원가입 성공";
            redirect = true;
            viewName = "/";
            break;
         }
      }else {
         viewName = "member/memberForm";
      }
      
      model.addAttribute("message", message);
      
      if(redirect) {
    	  return "redirect:"+viewName;
      }else {
    	  return viewName;
      }
   }

}