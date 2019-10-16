package kr.or.ddit.member.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.utils.MarshallingUtils;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.PagingInfoVO;

@Controller
@RequestMapping(value="/member")
public class MemberRetrieveController{ //POJO (Plain Old Java Object) //특정 프레임워크나 서버에 종속된 클래스가 아님
	@Inject
	IMemberService service;
	//계층구조가 형성됐기때문에 /를 빼준다
	@RequestMapping("memberList.do")
	public String memberList(
			@RequestParam(required=false) String searchType,
			@RequestParam(required=false) String searchWord,
			@RequestParam(name="page", required=false, defaultValue="1") int currentPage,
			Model model
			) {
		Map<String, Object> searchMap = new HashMap<>();
		searchMap.put("searchType", searchType);
		searchMap.put("searchWord", searchWord);
		
		PagingInfoVO<MemberVO> pagingVO = new PagingInfoVO<MemberVO>(5,3);
		pagingVO.setSearchMap(searchMap);
		int totalRecord =service.retrieveMemberCount(pagingVO);
		pagingVO.setTotalRecord(totalRecord);
		pagingVO.setCurrentPage(currentPage);
		List<MemberVO> lmv = service.retrieveMemberList(pagingVO);
		pagingVO.setDataList(lmv);
		
		model.addAttribute("pagingVO",pagingVO);
		
		String viewName ="member/memberList";
		return viewName;
	}
	
	@ResponseBody
	@RequestMapping(value="memberList.do",produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	//마샬링의 대상이 되는것이 리턴 타입
	public PagingInfoVO<MemberVO> memberListAjax(
			@RequestParam(required=false) String searchType,
			@RequestParam(required=false) String searchWord,
			@RequestParam(name="page", required=false, defaultValue="1") int currentPage
			,Model model
			) throws ServletException, IOException{
		memberList(searchType, searchWord, currentPage, model);
		return (PagingInfoVO<MemberVO>) model.asMap().get("pagingVO");
		
	}
	
	@RequestMapping("memberView.do")
	public String memberView(@RequestParam(required=true) String who, Model model) {
	      MemberVO saved = service.retrieveMember(new MemberVO(who, null));
	      model.addAttribute("member",saved);
	      String viewName = "member/memberView";
	      return viewName;
	      
	   }
}
