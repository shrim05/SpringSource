package kr.or.ddit.common.rest;

import java.util.List;

import kr.or.ddit.vo.BupjungdongVO;

public interface IApartmentDAO {
	
	public List<BupjungdongVO> selectCodeList(String keyword);
	
}
