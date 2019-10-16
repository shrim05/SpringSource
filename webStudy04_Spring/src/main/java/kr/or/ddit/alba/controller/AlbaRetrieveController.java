package kr.or.ddit.alba.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.alba.service.AlbaServiceImpl;
import kr.or.ddit.alba.service.IAlbaService;
import kr.or.ddit.vo.AlbaVO;
import kr.or.ddit.vo.LicAlbaVO;
import kr.or.ddit.vo.PagingInfoVO;

@Controller
public class AlbaRetrieveController{
	@Inject
	IAlbaService service;
	
//	@RequestMapping(value="/alba/licenseImage.do", produces=MediaType.APPLICATION_JSON_VALUE)
//	public String licenseImage(@RequestParam(required=true) LicAlbaVO licAlba,
//								Model)  {
//		LicAlbaVO license = service.retrieveLicense(licAlba);
//		byte[] imageData = license.getLic_image();
//		if(imageData==null){
//			try(
//				FileInputStream fis = new FileInputStream(req.getServletContext().getRealPath("/images/noImage.png"));
//			){		
//				int size = fis.available();
//				imageData = new byte[size];
//				IOUtils.read(fis, imageData);
//			}
//		}
//		try(
//			OutputStream os = resp.getOutputStream();
//			ByteArrayInputStream is = new ByteArrayInputStream(imageData);
//		){
//			IOUtils.copy(is, os);
//		}
//		return null;
//	}
	
	@RequestMapping("/alba/albaList.do")
	public String list(@RequestParam(required=false, name="page", defaultValue="1") int currentPage,
					   @RequestParam(required=false) String searchType,
					   @RequestParam(required=false) String searchWord,
					   Model model) {
		
		PagingInfoVO<AlbaVO> pagingVO = new PagingInfoVO<AlbaVO>(7, 3);
		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put("searchType", searchType);
		searchMap.put("searchWord", searchWord);
		pagingVO.setSearchMap(searchMap);
		pagingVO.setCurrentPage(currentPage);
		int totalRecord = service.retrieveAlbaCount(pagingVO);
		pagingVO.setTotalRecord(totalRecord);
		
		List<AlbaVO> albaList = service.retrieveAlbaList(pagingVO);
		pagingVO.setDataList(albaList);
		
		model.addAttribute("pagingVO", pagingVO);
		
		String view = "alba/albaList";
		return view;
	}
	
	@RequestMapping("/alba/albaView.do")
	public String view(@RequestParam(required=true) String who,
						Model model,
						HttpServletResponse resp) throws IOException {
		int sc = 0;
		AlbaVO alba = service.retrieveAlba(who);
		model.addAttribute("alba", alba);
		if(alba==null) {
			sc = HttpServletResponse.SC_NOT_FOUND;
			return null;
		}
		String view = null;
		if(sc!=0) {
			resp.sendError(sc);
		}else {
			view = "alba/albaView";
		}
		return view;
	}
}
