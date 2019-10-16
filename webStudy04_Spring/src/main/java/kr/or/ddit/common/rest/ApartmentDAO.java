package kr.or.ddit.common.rest;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import kr.or.ddit.db.mybatis.CustomSqlSessionFactoryBuilder;
import kr.or.ddit.vo.BupjungdongVO;

@Repository
public class ApartmentDAO implements IApartmentDAO {
	SqlSessionFactory sqlSessionFactory =
			CustomSqlSessionFactoryBuilder.getSqlSessionFactory();

	@Override
	public List<BupjungdongVO> selectCodeList(String keyword) {
		try(
				SqlSession sqlSession = sqlSessionFactory.openSession();
						){
			IApartmentDAO mapper = sqlSession.getMapper(IApartmentDAO.class);
					return mapper.selectCodeList(keyword);
				}
	}
	
}
