package egov.main.dao;

import java.util.HashMap;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper(value="MainDAO")
public interface MainDAO {
	HashMap<String, Object> selectMain(HashMap<String, Object> paramMap) throws Exception;
}
