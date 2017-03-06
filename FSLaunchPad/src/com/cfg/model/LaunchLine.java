package com.cfg.model;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class LaunchLine {
	private JCheckBox checkBox;
	private JButton buttonLaunch;
	private JButton buttonDel;
	private String fileName;
	private int priority;
	private String path;
	private JPanel panelLine;

	/**
	 * @return the checkBox
	 */
	public JCheckBox getCheckBox() {
		return checkBox;
	}
	/**
	 * @param checkBox the checkBox to set
	 */
	public void setCheckBox(JCheckBox checkBox) {
		this.checkBox = checkBox;
	}
	/**
	 * @return the buttonLaunch
	 */
	public JButton getButtonLaunch() {
		return buttonLaunch;
	}
	/**
	 * @param buttonLaunch the buttonLaunch to set
	 */
	public void setButtonLaunch(JButton button) {
		this.buttonLaunch = button;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	public LaunchLine(JCheckBox checkBox, JButton button, JButton buttonDel, JPanel panelLine, String fileName, String path) {
		super();
		this.checkBox = checkBox;
		this.buttonLaunch = button;
		this.fileName = fileName;
		this.path = path;
		this.buttonDel = buttonDel;
		this.panelLine = panelLine;
	}
	
	
	/**
	 * @return the buttonDel
	 */
	public JButton getButtonDel() {
		return buttonDel;
	}
	/**
	 * @param buttonDel the buttonDel to set
	 */
	public void setButtonDel(JButton buttonDel) {
		this.buttonDel = buttonDel;
	}
	public LaunchLine(JButton buttonDel) {
		super();
		this.buttonDel = buttonDel;
	}
	public JPanel getPanelLine() {
		return panelLine;
	}
	public void setPanelLine(JPanel panelLine) {
		this.panelLine = panelLine;
	}
	

}
