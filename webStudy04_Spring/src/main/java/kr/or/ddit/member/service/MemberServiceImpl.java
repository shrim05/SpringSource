package kr.or.ddit.member.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import kr.or.ddit.enums.ServiceResult;
import kr.or.ddit.member.dao.IMemberDAO;
import kr.or.ddit.member.exception.NotAuthenticatedException;
import kr.or.ddit.member.exception.UserNotFoundException;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.PagingInfoVO;

@Service("memberService")
public class MemberServiceImpl implements IMemberService{
	
	private static MemberServiceImpl instance;
	//결합력 최상 -> HCLC 지향 -> FactoryObject pattern ,  Strategy pattern (DI)
	@Inject
	private IMemberDAO dao;
	
	@Inject
	private IAuthenticateService service;
	
   @Override
   public ServiceResult createMember(MemberVO member) {
      ServiceResult result = null;
      if(dao.selectMember(member)==null) {
         int cnt = dao.insertMember(member);
         if(cnt > 0) result = ServiceResult.OK;
         else result = ServiceResult.FAILED;
      }else {
         result = ServiceResult.PKDUPLICATED;
      }
      return result;
   }

   @Override
   public MemberVO retrieveMember(MemberVO member) {
      MemberVO saved = dao.selectMember(member);
      
      if(saved==null) {
         throw new UserNotFoundException(member.getMem_id()+"가 없음.");
      }
      
      return saved;
   }
   

	@Override
	public int retrieveMemberCount(PagingInfoVO<MemberVO> pagingVO) {
		return dao.selectMemberCount(pagingVO);
		
	}
   
	@Override
	public List<MemberVO> retrieveMemberList(PagingInfoVO pagingVO) {
		return dao.selectMemberList(pagingVO);
	}

	@Override
	public ServiceResult modifyMember(MemberVO mv) {
		ServiceResult sr = ServiceResult.FAILED;
		try {
			service.authenticate(mv);
			int result = 0;
			result = dao.updateMember(mv);
			if(result>0) {
				sr=ServiceResult.OK;
			}else if(result<=0) {
				sr=ServiceResult.FAILED;
			}
		}catch(NotAuthenticatedException e) {
			sr= ServiceResult.INVALIDPASSWORD;
		}
		return sr;
	}

	@Override
	public ServiceResult removeMember(MemberVO mv) {
		ServiceResult sr = ServiceResult.FAILED;
		try {
			service.authenticate(mv);
			int result = 0;
			result = dao.deleteMember(mv);
			if(result>0) {
				sr=ServiceResult.OK;
			}else if(result<=0) {
				sr=ServiceResult.FAILED;
			}
		}catch(NotAuthenticatedException e) {
			sr= ServiceResult.INVALIDPASSWORD;
		}
		return sr;
	}


}
