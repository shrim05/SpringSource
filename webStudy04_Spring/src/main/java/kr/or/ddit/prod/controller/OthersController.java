package kr.or.ddit.prod.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.prod.dao.IOtherDAO;
import kr.or.ddit.prod.dao.OthersDAOImpl;
import kr.or.ddit.vo.BuyerVO;

@RestController
public class OthersController {
	@Inject
	IOtherDAO othersDAO;
	@RequestMapping(value="/prod/getLprodList.do",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Map<String,Object>> getLprodListForAjax(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		List<Map<String,Object>> lprodList = othersDAO.selectLprodList();
		return lprodList;
	}
	
	@RequestMapping(value="/prod/getBuyerList.do", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<BuyerVO> getBuyerListForAjax(@RequestParam(required=false, name="prod_lgu")String lgu) throws IOException {
		List<BuyerVO> buyerList = othersDAO.selectBuyerList(lgu);
		return buyerList;
	}
}
