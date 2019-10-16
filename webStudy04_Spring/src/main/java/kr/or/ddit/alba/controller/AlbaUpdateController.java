package kr.or.ddit.alba.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import kr.or.ddit.alba.dao.ICodeDAO;
import kr.or.ddit.alba.service.IAlbaService;
import kr.or.ddit.enums.ServiceResult;
import kr.or.ddit.vo.AlbaVO;
import kr.or.ddit.vo.LicAlbaVO;

@Controller
@RequestMapping("/alba/albaUpdate.do")
public class AlbaUpdateController{
	@Inject
	ICodeDAO codeDAO;
	@Inject
	IAlbaService service;
	
	private void setCodeInScope(Model model){
		model.addAttribute("licenses", codeDAO.selectLicense());
		model.addAttribute("grades", codeDAO.selectGrades());
	}
	
	@RequestMapping
	public String doGet(Model model,
						@RequestParam(required=true) String who,
						HttpServletResponse resp) throws IOException {
		setCodeInScope(model);
		AlbaVO alba = service.retrieveAlba(who);
		if(alba == null) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		model.addAttribute("alba", alba);
		String view = "alba/albaForm";
		return view;
	}

	@RequestMapping(method=RequestMethod.POST)
	public String doPost(Model model, 
						AlbaVO alba) throws IOException  {
		setCodeInScope(model);
		model.addAttribute("alba", alba);
		
		Map<String, String> errors = new HashMap<String, String>();
		model.addAttribute("errors", errors);
		boolean valid = validate(alba, errors);
		String[] lic_codes = alba.getLic_codes();
		// 자격증 코드와 자격증 사본에 대한 처리
		if(model instanceof MultipartHttpServletRequest && lic_codes!=null && lic_codes.length > 0){
			MultipartHttpServletRequest wrapper = (MultipartHttpServletRequest)model;
			List<MultipartFile> images = wrapper.getFiles("lic_images");
			if(images==null || lic_codes.length!=images.size()){
				valid = false;
				errors.put("lic_codes", "자격증 이미지 누락");
			}else{
				List<LicAlbaVO> licenseList = new ArrayList<>();
				alba.setLicenseList(licenseList);
				for(int idx=0; idx<lic_codes.length; idx++){
					LicAlbaVO licAlba = new LicAlbaVO();
					licAlba.setLic_code(lic_codes[idx]);
					licAlba.setLic_image(images.get(idx).getBytes());
					licenseList.add(licAlba);
				}
			}
		}
		
		String view = null;

		if (valid) {
			ServiceResult result = service.modifyAlba(alba);
			if (ServiceResult.OK.equals(result)) {
				view = "redirect:/alba/albaView.do?who=" + alba.getAl_id();
			} else {
				model.addAttribute("message", "수정 실패, 다시 시도");
				view = "alba/albaForm";
			}
		} else {
			view = "alba/albaForm";
		}
		return view;
	}

	private boolean validate(AlbaVO alba, Map<String, String> errors) {
		boolean valid = true;
		// 검증
		if (StringUtils.isBlank(alba.getAl_id())) {
			valid = false;
			errors.put("al_id", "아이디 누락");
		}
		if (StringUtils.isBlank(alba.getAl_name())) {
			valid = false;
			errors.put("al_name", "이름 누락");
		}
		if (alba.getAl_age()==null) {
			valid = false;
			errors.put("al_age", "나이 누락");
		}
		if (StringUtils.isBlank(alba.getAl_address())) {
			valid = false;
			errors.put("al_address", "주소 누락");
		}
		if (StringUtils.isBlank(alba.getAl_hp())) {
			valid = false;
			errors.put("al_hp", "휴대폰 누락");
		}
		if (StringUtils.isBlank(alba.getGr_code())) {
			valid = false;
			errors.put("gr_code", "최종학력 누락");
		}
		if (StringUtils.isBlank(alba.getAl_mail())) {
			valid = false;
			errors.put("al_mail", "이메일 누락");
		}
		return valid;
	}
}
