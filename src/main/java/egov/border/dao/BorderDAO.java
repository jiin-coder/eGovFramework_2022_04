package egov.border.dao;

import java.util.HashMap;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper(value="BorderDAO")
public interface BorderDAO {

	void insertBorder(HashMap<String, Object> paramMap)throws Exception;
	void selectBorder(HashMap<String, Object> paramMap)throws Exception;
}
