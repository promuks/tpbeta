package net.atpco.fm.auth.model;

import java.util.Map;

public class UIResponse {

	public boolean canAccess = false;
	public String userName;
	public String userRole;
	public String accessTime;
	Map<String,String> appProperties;
	
	public Map<String, String> getAppProperties() {
		return appProperties;
	}

	public void setAppProperties(Map<String, String> appProperties) {
		this.appProperties = appProperties;
	}

	public String getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(final String accessTime) {
		this.accessTime = accessTime;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(final String userRole) {
		this.userRole = userRole;
	}

	public boolean isCanAccess() {
		return canAccess;
	}

	public void setCanAccess(final boolean canAccess) {
		this.canAccess = canAccess;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UIResponse [canAccess=" + canAccess + ", userName=" + userName
				+ ", userRole=" + userRole + ", accessTime=" + accessTime + "]";
	}

}
