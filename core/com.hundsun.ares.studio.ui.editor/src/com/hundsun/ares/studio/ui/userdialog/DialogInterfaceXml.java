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
 * �û�xml���öԻ���
 * 
 * @author maxh
 */
public class DialogInterfaceXml {
	
	/**
	 * �Ի������
	 */
	String title = "";
	
	String fileId = "";
	
	private List<DialogInterfaceGroup> lstMenuInterfaceGroup = new ArrayList<DialogInterfaceGroup>();

	/**
	 * @return the lstMenuInterfaceGroup
	 */
	public List<DialogInterfaceGroup> getLstMenuInterfaceGroup() {
		return lstMenuInterfaceGroup;
	}

	/**
	 * @param lstMenuInterfaceGroup the lstMenuInterfaceGroup to set
	 */
	public void setLstMenuInterfaceGroup(List<DialogInterfaceGroup> lstMenuInterfaceGroup) {
		this.lstMenuInterfaceGroup = lstMenuInterfaceGroup;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	
	
}
