package com.cts.training.dao;

import java.util.List;

import com.cts.training.bean.VisitorRegBean;
import com.cts.training.Entity.VisitorEntity;

public interface VisitorDaoImpl {
	// void update(Object object);
		void saveVisitor(VisitorRegBean visitorBean);

		String getPassword(String userName);
		
		List<VisitorEntity> getVisitor(String userName);
		VisitorEntity updateVisitorObject(VisitorRegBean visitorBean);
		boolean changePassword(String userName,String passWord);
		VisitorEntity getVisitor(int visitorId);
		int getId(String userName);

}
