package kr.or.ddit.common.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.vo.MemberVO;

@Controller
public class GetUserListController  {
	
	@RequestMapping(value="/getUserList.do", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Set<MemberVO> getuserList(
			Set<MemberVO> userList
		) throws IOException, ServletException{
		return userList;
	}
}
