package kr.or.ddit.alba.controller;


import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.alba.service.IAlbaService;
import kr.or.ddit.enums.ServiceResult;

@Controller
public class AlbaDeleteController{
	@Inject
	IAlbaService service;
	
	@RequestMapping("/alba/albaDelete.do")
	public String doGet(@RequestParam(required=true) String who,
						RedirectAttributes redirectAttributes)  {
		String view = "/alba/albaView.do";
		ServiceResult result = service.removeAlba(who);
		if(ServiceResult.OK.equals(result)){
			view = "redirect:/alba/albaList.do";
		}else{
			redirectAttributes.addFlashAttribute("message", "삭제 실패, 다시 하라.");
			view = "redirect:/alba/albaView.do?who="+who;
		}
		
		return view;
	}
}
