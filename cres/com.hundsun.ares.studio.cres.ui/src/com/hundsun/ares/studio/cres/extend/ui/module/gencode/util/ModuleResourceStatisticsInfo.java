/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.cres.extend.ui.module.gencode.util;

/**
 * ģ�����������Դͳ����Ϣ
 * @author qinyuan
 *
 */
public class ModuleResourceStatisticsInfo {
	
	public ModuleResourceStatisticsInfo(String objectID,String name,String cName,String desc) {
		this.objectID = objectID;
		this.name = name;
		this.cName = cName;
		this.desc = desc;
	}
	
	/**
	 * @return the objectID
	 */
	public String getObjectID() {
		return objectID;
	}
	/**
	 * @param objectID the objectID to set
	 */
	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the cName
	 */
	public String getcName() {
		return cName;
	}
	/**
	 * @param cName the cName to set
	 */
	public void setcName(String cName) {
		this.cName = cName;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * �����
	 */
	private String objectID;
	/**
	 * Ӣ����
	 */
	private String name;
	/**
	 * ������
	 */
	private String cName;
	/**
	 * ˵��
	 */
	private String desc;

}
