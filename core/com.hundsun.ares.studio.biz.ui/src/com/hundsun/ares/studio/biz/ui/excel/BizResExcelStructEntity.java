/**
 * Դ�������ƣ�BizExcelStructEntity.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�yanwj06282
 */
package com.hundsun.ares.studio.biz.ui.excel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Excel��Ϣ��װģ��
 * 
 * @author yanwj06282
 *
 */
public class BizResExcelStructEntity {

	/**
	 * ÿ����Դ��key,���ڳ����Ӷ�λ,���Ա���Ψһ,����ʹ����Դ����+����
	 */
	private String hyperlinkKey ;
	
	/**
	 * �������Ե���󳤶ȣ�����������һ���������Ŷ��ٵ�Ԫ��
	 * �������ǻ���������key��value��ʽһһ��Ӧ�ģ�������������У�value�ĵ�Ԫ�񳬳���ֵ���������ֵ�value���ỻ��
	 */
	private int basicParmasMAXCellLength = 3;
	
	//��һ������:�������ԣ�������չ����
	private Map<ExcelBasicParamStuctEntity ,ExcelBasicParamStuctEntity> basicParams = new LinkedHashMap<ExcelBasicParamStuctEntity ,ExcelBasicParamStuctEntity>();
	
	//�ڶ�������:����
	private Map<String ,ParameterStructEntity> parameterMaps = new LinkedHashMap<String ,ParameterStructEntity>();
	
	//����������:�Զ��嵥Ԫ��
	private Map<ExcelBasicParamStuctEntity ,ExcelBasicParamStuctEntity> endAres = new LinkedHashMap<ExcelBasicParamStuctEntity ,ExcelBasicParamStuctEntity>();
	
	public void putBasicParams(ExcelBasicParamStuctEntity key ,ExcelBasicParamStuctEntity value){
		basicParams.put(key, value);
	}
	
	public void putParameterMaps(String key ,ParameterStructEntity value){
		parameterMaps.put(key, value);
	}

	public Map<ExcelBasicParamStuctEntity, ExcelBasicParamStuctEntity> getBasicParams() {
		return basicParams;
	}

	public Map<String, ParameterStructEntity> getParameterMaps() {
		return parameterMaps;
	}

	public int getBasicParmasMAXCellLength() {
		return basicParmasMAXCellLength;
	}

	public void setBasicParmasMAXCellLength(int basicParmasMAXCellLength) {
		this.basicParmasMAXCellLength = basicParmasMAXCellLength;
	}

	public String getHyperlinkKey() {
		return hyperlinkKey;
	}

	public void setHyperlinkKey(String hyperlinkKey) {
		this.hyperlinkKey = hyperlinkKey;
	}

	public Map<ExcelBasicParamStuctEntity, ExcelBasicParamStuctEntity> getEndAres() {
		return endAres;
	}

	public void putEndAres(ExcelBasicParamStuctEntity key , ExcelBasicParamStuctEntity value) {
		endAres.put(key, value);
	}

}
