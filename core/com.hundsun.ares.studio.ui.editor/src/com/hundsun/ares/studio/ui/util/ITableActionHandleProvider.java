/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.util;

/**
 * ����������ṩ����
 * 
 * @author mawb
 */
public interface ITableActionHandleProvider {
	
	/**
	 * �Ƿ񼤻��������
	 * 
	 * @return
	 */
	boolean isActivate();
	
	/**
	 * �����Ƿ񼤻��������
	 * 
	 * @param isActivate
	 * @return
	 */
	void setActivate(Boolean isActivate);
	
	/**
	 * �Ƿ��������
	 * 
	 * @return
	 */
	boolean canAdd();
	
	/**
	 * �Ƿ��ɾ����
	 * 
	 * @return
	 */
	boolean canDelete();
	
	/**
	 * �Ƿ�ɲ��롣
	 * 
	 * @return
	 */
	boolean canInsert();
	
	/**
	 * �Ƿ�ɸ��ơ�
	 * 
	 * @return
	 */
	boolean canCopy();
	
	/**
	 * �Ƿ�ɼ��С�
	 * 
	 * @return
	 */
	boolean canCut();
	
	
	/**
	 * �Ƿ��ճ����
	 * 
	 * @return
	 */
	boolean canPaste();
	
	/**
	 * ��ӡ�
	 */
	void add();
	
	/**
	 * ɾ����
	 */
	void delete();
	
	/**
	 * ���롣
	 */
	void insert();
	
	/**
	 * ���ơ�
	 */
	void copy();
	
	/**
	 * ���С�
	 */
	void cut();
	
	/**
	 * ճ����
	 */
	void paste();
	
}
