package com.cfg.model;

import com.cfg.file.ManageConfigFile;

public class Area extends ManageConfigFile {
/*	[Area.699]
	Title=DP_Center
	Local=Addon Scenery\Portugal\DP_Center
	Layer=699
	Active=TRUE
	Required=FALSE
*/
	private String num;
	private String title;
	private String local;
	private String localkey;
	private String layer;
	private String active;
	private String texture;
	private String required;
	private String cashSize;
	private String exclude;
	private String remote;
	
	
	private String description;
	private String cleanOnExit;
	
	
	public Area() {
	}
	
	
	public Area(String num) {
		this.num = num;
	}
	/**
	 * @return the num
	 */
	public String getNum() {
		return num;
	}
	/**
	 * @param num the num to set
	 */
	public void setNum(String num) {
		this.num = num;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the local
	 */
	public String getLocal() {
		return local;
	}
	/**
	 * @param local the local to set
	 */
	public void setLocal(String local) {
		try {
			if ("\\".equals(local.substring(local.length()-1))){
				local = local.substring(0,local.length()-1);
			}
			localkey = local.substring(local.lastIndexOf("\\")+1);
			this.local = local;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	/**
	 * @return the layer
	 */
	public String getLayer() {
		return layer;
	}
	/**
	 * @param layer the layer to set
	 */
	public void setLayer(String layer) {
		this.layer = layer;
	}
	/**
	 * @return the active
	 */
	public String getActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(String active) {
		this.active = active;
	}
	/**
	 * @return the required
	 */
	public String getRequired() {
		return required;
	}
	/**
	 * @param required the required to set
	 */
	public void setRequired(String required) {
		this.required = required;
	}
	/**
	 * @return the texture
	 */
	public String getTexture() {
		return texture;
	}
	/**
	 * @param texture the texture to set
	 */
	public void setTexture(String texture) {
		this.texture = texture;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Area [num=" + num + ", Title=" + title + ", local=" + local
				+ ", layer=" + layer+ ", numCopy=" +", active=" + active + ", texture="
				+ texture + ", required=" + required + "]";
	}
	
	public String buildArea(int i){
		String tempAeara;
		if (i < 10) {
			tempAeara = "00"+i;
		} else if (i < 100) {
			tempAeara = "0"+i;
		} else {
			tempAeara = ""+i;
		}
		return "[Area."+tempAeara+"]\n"+
				"Title="+title+"\n"+
				"Local="+local+"\n"+
				 (texture != null?"Texture_ID="+texture+"\n":"")+
				"Layer="+i+"\n"+
				"Active=TRUE\n"+
				"Required="+required+"\n"+
				(exclude != null?"Exlcude="+exclude+"\n":"")+
				(remote!= null?"Remote="+remote+"\n":"");
	}
	
	public String buildArea(){
		return "[Area."+num+"]\n"+
				"Title="+title+"\n"+
				"Local="+local+"\n"+
				 (texture != null?"Texture_ID="+texture+"\n":"")+
				"Layer="+num+"\n"+
				"Active="+active+"\n"+
				"Required="+required+"\n"+
				(exclude != null?"Exlcude="+exclude+"\n":"")+
				(remote!= null?"Remote="+remote+"\n":"");

		
	}
	
	public String buildGeneral(){
		return num+"\n"+
				"Title="+title+"\n"+
				"Description="+description+"\n"+
				(cashSize != null && !"".equals(cashSize)?"Cache_Size="+cashSize+"\n":"")+
				"Clean_on_Exit="+cleanOnExit+"\n";
		
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the cleanOnExit
	 */
	public String getCleanOnExit() {
		return cleanOnExit;
	}
	/**
	 * @param cleanOnExit the cleanOnExit to set
	 */
	public void setCleanOnExit(String cleanOnExit) {
		this.cleanOnExit = cleanOnExit;
	}
	/**
	 * @return the numCopy
	 */
	
	public void setNums(String num){
		this.num = num;
		this.layer = Integer.valueOf(num).toString();
	}
	
	public void setArea(Area area) {
		this.num = area.getNum();
		this.title = area.getTitle();
		this.local = area.getLocal();
		this.layer = area.getLayer();
		this.active = area.getActive();
		this.texture = area.getTexture();
		this.required = area.getRequired();
		this.description = area.getDescription();
		this.cleanOnExit = area.getCleanOnExit();
		localkey = getLocal().substring(getLocal().lastIndexOf("\\")+1);

	}

	public Area(Area area) {
		super();
		this.num = area.getNum();
		this.title = area.getTitle();
		this.local = area.getLocal();
		this.layer = area.getLayer();
		this.active = area.getActive();
		this.texture = area.getTexture();
		this.required = area.getRequired();
		this.description = area.getDescription();
		this.cleanOnExit = area.getCleanOnExit();
		localkey = getLocal().substring(getLocal().lastIndexOf("\\")+1);
	}
	
	public void setAreaModified(String title, String active, String required) {
		this.title = title;
		this.active = active;
		this.required = required;
	}
	public String getLocalkey() {
		return localkey;
	}
	public void setLocalkey(String localkey) {
		this.localkey = localkey;
	}
	public String getCashSize() {
		return cashSize;
	}
	public void setCashSize(String cashSize) {
		this.cashSize = cashSize;
	}

	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}
	

}
