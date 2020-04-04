package com.cts.training.Service;

import java.util.List;

import com.cts.training.Entity.VisitorEntity;
import com.cts.training.bean.VisitorRegBean;

public interface VisitorServiceInterface {
	void saveVisitor(VisitorRegBean visitorBean);

	String validateVisitorLogin(String userName, String passWord);

	List<VisitorEntity> getVisitor(String userName);

	VisitorEntity getVisitorObject(String userName);
	VisitorEntity getVisitor(int visitorId);
	VisitorEntity updateVisitorObject(VisitorRegBean visitorBean);
	boolean changePassword(String userName,String passWord);
	int getId(String userName);

}
