/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;

import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;

/**
 * ���ݿ���Դ����
 * 
 * @author yanwj06282
 *
 */
public interface IDatabaseResScriptWrap extends IScriptModelWrap{

	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	public String getChineseName();
	
	/**
	 * ��ȡ�����
	 * 
	 * @return
	 */
	public String getObjectId();
	
	/**
	 * ������֣�prefix��ǰ׺��־������ʶ��ǰ����ʷ��
	 * 
	 * @param prefix
	 * @return
	 */
	public String getName(String prefix);
	
	/**
	 * ��ȡ���ݿ��û�
	 * 
	 * @param prefix
	 * @return
	 */
	public String getDbuser(String prefix);
	
	/**
	 * ��ȡ��ռ��Ӧ���߼��������߼����洢SQL
	 * @param prefix
	 * @return
	 */
	public String getTableSpaceLogicName(String prefix);
	
	/**
	 * 	��ñ�ռ�
	 * @param prefix
	 * @return
	 */
	public String getTableSpace(String  prefix);
	
	/**
	 * ��ȡ�û��ļ���prefix��ǰ׺��־������ʶ��ǰ����ʷ��
	 * 
	 * @param prefix
	 * @return
	 */
	public String getDbuserFileName(String prefix);
	
	/**
	 * ��ȡ�޶���¼
	 * 
	 * @return
	 */
	public IRevHistoryScriptWrap[] getHistories();
	
	/**
	 * ���ö����
	 * 
	 * @param objectId
	 */
	public void setObjectId(String objectId);
	
	/**
	 * ��ȡ˵����Ϣ
	 * @return
	 */
	public String getDescription();
	
	/**
	 * ����������
	 * @param cname
	 */
	public void setChineseName(String cname);
	
	/**
	 * ��ȡ�汾�ţ�����Դ�м�����򱣳�һ�¡�
	 * @return
	 */
	public String getVesion();
	
}
