package kr.or.ddit.prod.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.board.controller.ReplyController.UpdateHint;
import kr.or.ddit.enums.ServiceResult;
import kr.or.ddit.prod.service.IProdService;
import kr.or.ddit.vo.ProdVO;

@Controller
@RequestMapping("/prod/prodUpdate.do")
public class ProdUpdateController {
	@Inject
	IProdService service;
	@RequestMapping()
	public String updateForm(
			@RequestParam(required=true)String prod_id, Model model
			) {
		ProdVO pv = service.retrieveProd(prod_id);
		model.addAttribute("prod", pv);
		return "prod/prodForm";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String update(
			@Validated(UpdateHint.class) @ModelAttribute("prod") ProdVO pv,
			Errors errors,
			Model model 
			) {
		String viewName = "prod/prodForm";
		String message = null;
		Boolean valid = !errors.hasErrors();
		if(valid) {
			ServiceResult result = service.modifyProd(pv);
			switch(result) {
			case OK:
				message="수정성공";
				viewName="redirect:/prod/prodView.do?prod_id="+pv.getProd_id();
				break;
			case FAILED:
				message="서버오류";
				viewName="prod/prodForm";
				break;
			}
		}else {
			viewName = "prod/prodForm";
		}
		model.addAttribute("message", message);
		return viewName;
	}
	
	
}
