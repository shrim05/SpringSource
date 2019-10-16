package kr.or.ddit.prod.controller;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.prod.service.IProdService;
import kr.or.ddit.vo.PagingInfoVO;
import kr.or.ddit.vo.ProdVO;
//POJO
@Controller
public class ProdListController {
	private static Logger logger = LoggerFactory.getLogger(ProdListController.class); 
	@Inject
	IProdService service;
	@RequestMapping(value="/prod")
	public String prodList(
			@RequestParam(name="page", required=false, defaultValue="1") int currentPage,
			ProdVO searchVO, 
			Model model,
			HttpServletResponse resp
			) throws IOException {
		PagingInfoVO<ProdVO> pagingVO = new PagingInfoVO<>(5,3);
		pagingVO.setSearchVO(searchVO);
		int totalRecord = service.retrieveProdCount(pagingVO);
		pagingVO.setTotalRecord(totalRecord);
		pagingVO.setCurrentPage(currentPage);
		List<ProdVO> lpv = service.retrieveProdList(pagingVO);
		pagingVO.setDataList(lpv);
		if(lpv==null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		model.addAttribute("pagingVO", pagingVO);
		
		return "/prod/prodList";
	}
}
