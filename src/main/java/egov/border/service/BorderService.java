package egov.border.service;

import java.util.HashMap;

import javax.annotation.Resource;

import egov.border.dao.BorderDAO;

public interface BorderService {

	void insertBorder(HashMap<String, Object> paramMap) throws Exception;
	void selectBorder(HashMap<String, Object> paramMap) throws Exception;
}
