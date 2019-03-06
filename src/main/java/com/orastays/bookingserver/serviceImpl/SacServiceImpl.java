package com.orastays.bookingserver.serviceImpl;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.orastays.bookingserver.dao.SacCodeDAO;
import com.orastays.bookingserver.entity.SacCodeEntity;
import com.orastays.bookingserver.helper.Util;
import com.orastays.bookingserver.service.SacService;

@Service
@Transactional
public class SacServiceImpl implements SacService {
	private static final Logger logger = LogManager.getLogger(SacServiceImpl.class);

	@Autowired
	protected SacCodeDAO sacCodeDAO;

	@Value("${entitymanager.packagesToScan}")
	protected String entitymanagerPackagesToScan;

	@Override
	public SacCodeEntity getActiveSacCodeEntity() {

		logger.info("getActiveSacCodeEntity -- START");

		SacCodeEntity sacCodeEntity = null;
		try {
			Map<String, String> innerMap1 = new LinkedHashMap<>();
			innerMap1.put("status", String.valueOf(com.orastays.bookingserver.constants.Status.ACTIVE.ordinal()));
			Map<String, Map<String, String>> outerMap1 = new LinkedHashMap<>();
			outerMap1.put("eq", innerMap1);

			Map<String, Map<String, Map<String, String>>> alliasMap = new LinkedHashMap<>();
			alliasMap.put(entitymanagerPackagesToScan + ".SacCodeEntity", outerMap1);

			sacCodeEntity = sacCodeDAO.fetchObjectBySubCiteria(alliasMap);
		} catch (Exception e) {
			if (logger.isInfoEnabled()) {
				logger.info("Exception in getActiveSacCodeEntity -- " + Util.errorToString(e));
			}
		}

		logger.info("getActiveSacCodeEntity -- END");

		return sacCodeEntity;

	}
}
