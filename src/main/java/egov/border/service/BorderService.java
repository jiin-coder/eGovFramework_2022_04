package egov.border.service;

import java.util.HashMap;

import javax.annotation.Resource;

import egov.border.dao.BorderDAO;

public interface BorderService {

	void insertBorder(HashMap<String, Object> paramMap) throws Exception;
	void selectBorder(HashMap<String, Object> paramMap) throws Exception;
	void selectBorderView(HashMap<String, Object> paramMap) throws Exception;
	void insertBorderReply(HashMap<String, Object> paramMap) throws Exception;
	void updateBorderEdit(HashMap<String, Object> paramMap) throws Exception;
	void updateBorderRemove(HashMap<String, Object> paramMap) throws Exception;
}
