/**
 * 
 */
package com.hundsun.ares.studio.jres.script.api.wrap;





/**
 * Ԫ���ݣ���Ӧ���̽ṹ�е�Ԫ����ģ���������ֱ�ӻ��߼�ӵĻ�ȡԪ�����µ�������Ϣ
 * 
 * @author yanwj06282
 *
 */
public interface IMetadataScriptWrap{

	/**
	 * �������ͣ���ȡԪ���ݶ���
	 * 
	 * <li>
	 * ��ѡ����  :
	 * 	<i>defaultvalue</i>
	 * 	<i>stdfield</i>
	 * 	<i>datatype</i>
	 * 	<i>constant</i>
	 * 	<i>errorno</i>
	 * 	<i>dict</i>
	 * 	<i>menu</i>
	 * </li>
	 * 
	 * @param type
	 * @return
	 */
	public IMetadataResScriptWrap getMetadataInfoByType(String type);
	
	/**
	 * �������ͣ���ȡԪ���ݶ���
	 * 
	 * <li>
	 * ��ѡ����  :
	 * 	<i>defaultvalue</i>
	 * 	<i>stdfield</i>
	 * 	<i>datatype</i>
	 * 	<i>constant</i>
	 * 	<i>errorno</i>
	 * 	<i>dict</i>
	 * 	<i>menu</i>
	 * </li>
	 * 
	 * @param type
	 * @param includeRequridBundles  �Ƿ����������Ŀ�������
	 * @return
	 */
	public IMetadataResScriptWrap getMetadataInfoByType(String type, boolean includeRequridBundles);
	
}
