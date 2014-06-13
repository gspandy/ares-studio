/**
 * Դ�������ƣ�PDMStandardField.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����PDM����ʱ���ɵ��뱨���е�(��׼�ֶ���Ϣ)
 * ����ĵ���
 * ���ߣ�liaogc
 */
package com.hundsun.ares.studio.jres.database.pdm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liaogc
 *
 */
public class PDMStandardField {
	private String oldName;//ԭ�ֶ���(����֮ǰ���ֶ���)
	private String oldChineseName;//ԭ������
	private String oldBusType;//ԭҵ������(��ʵ����ԭ������ʵ����)
	//private String belongTable;//������
	private String genName;//����ʱ�������ֶ���(��׼�ֶ�����)
	private String newName;//����֮����ֶ���(�ֶθ���)
	private String newChineseName;//��������
	private String genBusType;//ҵ����������(�û�������ʱ�����޸�newBusType)
	
	private String newBusType;//���͸���(�û�������genBusTypeʱ����ͨ���޸�newBusType������)
	private String dictId;//�����ֵ�
	private String oldComment;//�ɱ�ע
	private String newComment;//�±�ע
	private String modefyDesc;//�޸�˵��
	private String importPath;//�����ļ���·��
	private List<String> belongTableList = new ArrayList<String>();
	private List<String> bolongSubSystemList =new ArrayList<String>();
	private boolean isChanged = false;//�Ƿ��Ѿ�����Ψһ����
	
	/**
	 * @return the isChanged
	 */
	public boolean isChanged() {
		return isChanged;
	}

	/**
	 * @param isChanged the isChanged to set
	 */
	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	/**
	 * @return the bolongSubSystemList
	 */
	public List<String> getBolongSubSystemList() {
		return bolongSubSystemList;
	}

	/**
	 * @return the belongTableList
	 */
	public List<String> getBelongTableList() {
		return belongTableList;
	}
	
	/**
	 * @return the oldName
	 */
	public String getOldName() {
		return oldName;
	}
	/**
	 * @param oldName the oldName to set
	 */
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	/**
	 * @return the genName
	 */
	public String getGenName() {
		return genName;
	}
	/**
	 * @param genName the genName to set
	 */
	public void setGenName(String genName) {
		this.genName = genName;
	}
	/**
	 * @return the newName
	 */
	public String getNewName() {
		return newName;
	}
	/**
	 * @param newName the newName to set
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}
	/**
	 * @return the oldChineseName
	 */
	public String getOldChineseName() {
		return oldChineseName;
	}
	/**
	 * @param oldChineseName the oldChineseName to set
	 */
	public void setOldChineseName(String oldChineseName) {
		this.oldChineseName = oldChineseName;
	}
	/**
	 * @return the newChineseName
	 */
	public String getNewChineseName() {
		return newChineseName;
	}
	/**
	 * @param newChineseName the newChineseName to set
	 */
	public void setNewChineseName(String newChineseName) {
		this.newChineseName = newChineseName;
	}
	/**
	 * @return the oldBusType
	 */
	public String getOldBusType() {
		return oldBusType;
	}
	/**
	 * @param oldBusType the oldBusType to set
	 */
	public void setOldBusType(String oldBusType) {
		this.oldBusType = oldBusType;
	}
	/**
	 * @return the newBusType
	 */
	public String getNewBusType() {
		return newBusType;
	}
	/**
	 * @param newBusType the newBusType to set
	 */
	public void setNewBusType(String newBusType) {
		this.newBusType = newBusType;
	}
	/**
	 * @return the dictId
	 */
	public String getDictId() {
		return dictId;
	}
	/**
	 * @param dictId the dictId to set
	 */
	public void setDictId(String dictId) {
		this.dictId = dictId;
	}
	/**
	 * @return the belongTable
	 *//*
	public String getBelongTable() {
		return belongTable;
	}
	*//**
	 * @param belongTable the belongTable to set
	 *//*
	public void setBelongTable(String belongTable) {
		this.belongTable = belongTable;
	}*/
	
	/**
	 * @return the modefyDesc
	 */
	public String getModefyDesc() {
		return modefyDesc;
	}
	/**
	 * @param modefyDesc the modefyDesc to set
	 */
	public void setModefyDesc(String modefyDesc) {
		this.modefyDesc = modefyDesc;
	}
	/**
	 * @return the importPath
	 */
	public String getImportPath() {
		return importPath;
	}
	/**
	 * @param importPath the importPath to set
	 */
	public void setImportPath(String importPath) {
		this.importPath = importPath;
	}
	/**
	 * @return the genBusType
	 */
	public String getGenBusType() {
		return genBusType;
	}
	/**
	 * @param genBusType the genBusType to set
	 */
	public void setGenBusType(String genBusType) {
		this.genBusType = genBusType;
	}
	/**
	 * @return the oldComment
	 */
	public String getOldComment() {
		return oldComment;
	}
	/**
	 * @param oldComment the oldComment to set
	 */
	public void setOldComment(String oldComment) {
		this.oldComment = oldComment;
	}
	/**
	 * @return the newComment
	 */
	public String getNewComment() {
		return newComment;
	}
	/**
	 * @param newComment the newComment to set
	 */
	public void setNewComment(String newComment) {
		this.newComment = newComment;
	}
	

}
