/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
/**
 * 
 */
package com.hundsun.ares.studio.ui.userdialog;

import java.util.ArrayList;
import java.util.List;

/**
 * �û�xml���öԻ����е�group
 * 
 * @author maxh
 */
public class DialogInterfaceGroup {
	/* ������ */
	private String groupName ="";
	/* �Ƿ�����*/
	private boolean isUse =true;
	
	private List<DialogInterfaceItem> lstMenuInterfaceItem = new ArrayList<DialogInterfaceItem>();
	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * @return the lstMenuInterfaceItem
	 */
	public List<DialogInterfaceItem> getLstMenuInterfaceItem() {
		return lstMenuInterfaceItem;
	}
	/**
	 * @param lstMenuInterfaceItem the lstMenuInterfaceItem to set
	 */
	public void setLstMenuInterfaceItem(List<DialogInterfaceItem> lstMenuInterfaceItem) {
		this.lstMenuInterfaceItem = lstMenuInterfaceItem;
	}
	/**
	 * @return the isUse
	 */
	public boolean isUse() {
		return isUse;
	}
	/**
	 * @param isUse the isUse to set
	 */
	public void setUse(boolean isUse) {
		this.isUse = isUse;
	}
}
