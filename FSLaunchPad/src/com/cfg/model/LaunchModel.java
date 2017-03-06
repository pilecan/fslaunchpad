package com.cfg.model;
//http://docs.oracle.com/javase/tutorial/uiswing/components/button.html
public class LaunchModel {
	private String fileName;
	private int priority;
	private String path;
	private char checked;
	public char getChecked() {
		return checked;
	}
	public void setChecked(char checked) {
		this.checked = checked;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public LaunchModel(String fileName, String path,char checked) {
		super();
		this.fileName = fileName;
		this.path = path;
		this.checked = checked;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LaunchModel [fileName=" + fileName + ", path=" + path + "]";
	}
	

}
