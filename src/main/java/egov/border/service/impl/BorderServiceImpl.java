package egov.border.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egov.border.dao.BorderDAO;
import egov.border.service.BorderService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("BorderService")
public class BorderServiceImpl extends EgovAbstractServiceImpl implements BorderService  {
	
	@Resource(name="BorderDAO") BorderDAO borderDAO;
	
	@Override
	public void insertBorder(HashMap<String, Object> paramMap) throws Exception {
		borderDAO.insertBorder(paramMap);
	}

	@Override
	public void selectBorder(HashMap<String, Object> paramMap) throws Exception {
		borderDAO.selectBorder(paramMap);
	}

	@Override
	public void selectView(HashMap<String, Object> paramMap) throws Exception {
		borderDAO.selectView(paramMap);
	}
}
