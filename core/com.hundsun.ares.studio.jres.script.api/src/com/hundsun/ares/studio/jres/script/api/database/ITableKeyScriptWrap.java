package com.hundsun.ares.studio.jres.script.api.database;


public interface ITableKeyScriptWrap {
	
	/**����*/
	public String getName();
	
	/**�ֶ��б�*/
	public ITableColScriptWrap[] getColumns();
	
	/**���ͣ�ֵΪ������/Ψһ/���*/
	public String getType();
	
	/**����б�*/
	public ITableColForergnKeyScriptWrap[] getForeignKey(); 
	
	/**���*/
	public String getMark();
}
