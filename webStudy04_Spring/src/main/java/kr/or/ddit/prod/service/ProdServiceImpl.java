package kr.or.ddit.prod.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import kr.or.ddit.enums.ServiceResult;
import kr.or.ddit.exception.CommonException;
import kr.or.ddit.prod.dao.IProdDAO;
import kr.or.ddit.vo.PagingInfoVO;
import kr.or.ddit.vo.ProdVO;

@Service
public class ProdServiceImpl implements IProdService {
	@Inject
	IProdDAO dao;
	@Inject
	WebApplicationContext container;
	ServletContext application;
	String saveFolderPath;
	String saveFolderUrl = "/prodImages";
	File saveFolder;
	@PostConstruct
	public void init() {
		application = container.getServletContext();
		saveFolderPath = application.getRealPath(saveFolderUrl);
		saveFolder = new File(saveFolderPath);
	}
	
	@Override
	public ServiceResult createProd(ProdVO pv) {
		ServiceResult result = null;
		int cnt = dao.insertProd(pv);
		if(cnt >0 ) {
			try {
				pv.transfer(saveFolder);
			} catch (IllegalStateException | IOException e) {
				throw new RuntimeException();
			}
			result = ServiceResult.OK;
		}else {
			result = ServiceResult.FAILED;
		}
		return result;
	}

	@Override
	public List<ProdVO> retrieveProdList(PagingInfoVO pagingVO) {
		return dao.selectProdList(pagingVO);
	}

	@Override
	public ProdVO retrieveProd(String prod_id) {
		ProdVO pv = dao.selectProd(prod_id);
		if(pv==null) {
			throw new CommonException(prod_id+"해당 품목 없음");
		}
		return pv;
	}

	@Override
	public ServiceResult modifyProd(ProdVO pv) {
		ServiceResult result = null;
		retrieveProd(pv.getProd_id());
		int cnt = dao.updateProd(pv);
		if(cnt>0) {
			try {
				pv.transfer(saveFolder);
			} catch (IllegalStateException | IOException e) {
				throw new RuntimeException();
			}
			result = ServiceResult.OK;
		}else {
			result = ServiceResult.FAILED;
		}
		return result;
	}

	@Override
	public int retrieveProdCount(PagingInfoVO pagingVO) {
		return dao.selectProdCount(pagingVO);
		
	}


}
