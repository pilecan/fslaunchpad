package com.cfg.model;

public class Status {
	private int code;
	private String key;
	private String message;
	private String lastDate;
	private int cptUsage;
	private int maxUsage;

	public Status() {
	}

	public Status(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Status(int code, String message, int cptUsage, int maxUsage) {
		super();
		this.code = code;
		this.message = message;
		this.cptUsage = cptUsage;
		this.maxUsage = maxUsage;
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


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	@Override
	public String toString() {
		return "Status [code=" + code + ", key=" + key + ", message=" + message
				+ ", lastDate=" + lastDate + ", cptUsage=" + cptUsage
				+ ", maxUsage=" + maxUsage + "]";
	}

	public Status(int code, String key, String message) {
		super();
		this.code = code;
		this.key = key;
		this.message = message;
	}

}
