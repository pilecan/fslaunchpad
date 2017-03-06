package com.cfg.model;

import java.io.Serializable;
public class LauncherInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private String keyId;

	private String computerName;

	private String createdDate;
	
	private String orderCode;
	
	private String lastDate;
	
	private String fsDate;

	private String ipAddress;

	private int code;
	
	private int cptUsage;
	
	private int maxUsage;
	
	private int cptDay;
	
	private String version;
	
	private String lang;

	private String sim;

	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}

	public LauncherInfo() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getKeyId() {
		return keyId;
	}


	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}


	public String getComputerName() {
		return computerName;
	}


	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}


	public String getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}



	public String getOrderCode() {
		return orderCode;
	}


	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}


	public String getLastDate() {
		return lastDate;
	}


	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}


	public String getIpAddress() {
		return ipAddress;
	}


	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}


	
	public LauncherInfo(String keyId, String computerName, String fsDate,
			String orderCode, String lastDate, String createdDate, String ipAddress, String version, String lang, String sim) {
		super();
		this.keyId = keyId;
		this.computerName = computerName;
		this.fsDate = fsDate;
		this.orderCode = orderCode;
		this.lastDate = lastDate;
		this.createdDate = createdDate;
		this.ipAddress = ipAddress;
		this.version = version;
		this.lang = lang;
		this.sim = sim;
	}


	public String getFsDate() {
		return fsDate;
	}


	public void setFsDate(String fsDate) {
		this.fsDate = fsDate;
	}


	public int getCptUsage() {
		return cptUsage;
	}


	public void setCptUsage(int cptUsage) {
		this.cptUsage = cptUsage;
	}


	public int getMaxUsage() {
		return maxUsage;
	}


	public void setMaxUsage(int maxUsage) {
		this.maxUsage = maxUsage;
	}


	public int getCptDay() {
		return cptDay;
	}


	public void setCptDay(int cptDay) {
		this.cptDay = cptDay;
	}


	public int getCode() {
		return code;
	}


	public void setCode(int code) {
		this.code = code;
	}




	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getLang() {
		return lang;
	}


	public void setLang(String lang) {
		this.lang = lang;
	}


	public String getSim() {
		return sim;
	}


	public void setSim(String sim) {
		this.sim = sim;
	}


	@Override
	public String toString() {
		return "LauncherInfo [id=" + id + ", keyId=" + keyId
				+ ", computerName=" + computerName + ", createdDate="
				+ createdDate + ", orderCode=" + orderCode + ", lastDate="
				+ lastDate + ", fsDate=" + fsDate + ", ipAddress=" + ipAddress
				+ ", code=" + code + ", cptUsage=" + cptUsage + ", maxUsage="
				+ maxUsage + ", cptDay=" + cptDay + ", version=" + version
				+ ", lang=" + lang + ", sim=" + sim + "]";
	}


	



	
}
