package kr.or.ddit.prod.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.or.ddit.common.hint.InsertHint;
import kr.or.ddit.enums.ServiceResult;
import kr.or.ddit.prod.service.IProdService;
import kr.or.ddit.prod.service.ProdServiceImpl;
import kr.or.ddit.vo.ProdVO;

@Controller
@RequestMapping("/prod/prodInsert.do")
public class ProdInsertController {
	IProdService service = new ProdServiceImpl();
	@RequestMapping()
	public String insertForm() {
		return "prod/prodForm";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String insert(
			@Validated(InsertHint.class) @ModelAttribute(name="prod") ProdVO pv,
			BindingResult errors,
			Model model){
		Boolean valid = !errors.hasErrors();
		String message = null;
		String viewName ="/prod/prodForm";
		if(valid) {
			ServiceResult result = service.createProd(pv);
			switch (result) {
			case OK:
				message ="추가 성공";
				viewName ="redirect:/prod/prodView.do?prod_id="+pv.getProd_id();
				break;
			case FAILED:
				message="서버오류";
				viewName ="prod/prodForm";
				break;
			}
		}else {
			viewName="prod/prodForm";
		}
		model.addAttribute("message", message);
		return viewName;
		
	}
}
