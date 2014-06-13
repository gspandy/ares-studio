package com.hundsun.ares.studio.jres.basicdata.logic.util;

import org.eclipse.core.databinding.UpdateValueStrategy;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESProjectProperty;
import com.hundsun.ares.studio.jres.basicdata.constant.IBasicDataEpacakgeConstant;
import com.hundsun.ares.studio.jres.basicdata.logic.epackage.extensionpoint.EpackageFactoryItem;
import com.hundsun.ares.studio.jres.basicdata.logic.epackage.extensionpoint.EpackageFactoryManager;

public class EPackageUtil {

	/**
	 * ͨ��չʾ��ȡID
	 * @param name
	 * @return
	 */
	public static String getEpackageFactoryItemID(String name) {
		for( EpackageFactoryItem  item: EpackageFactoryManager.getInstance().getFactoryList()){
			if(item.name.equals(name)){
				return item.id;
			}
		}
		return name;
	}
	
	/**
	 * ͨ��ID��ȡչʾ
	 * @param id
	 * @return
	 */
	public static String getEpackageFactoryItemName(String id) {
		for( EpackageFactoryItem  item: EpackageFactoryManager.getInstance().getFactoryList()){
			if(item.id.equals(id)){
				return item.name;
			}
		}
		return id;
	}
	
	/**
	 * ��ȡ���浽ģ�͸��²���
	 * @return
	 */
	public static UpdateValueStrategy getTypeTargetToModelStrategy(){
		return new UpdateValueStrategy(){
			@Override
			public Object convert(Object value) {
				if(value instanceof String){
					return getEpackageFactoryItemID((String) value);
				}
				return super.convert(value);
			}
		};
	}
	
	/**
	 * ��ȡģ�͵�����ĸ��²���
	 * @return
	 */
	public static UpdateValueStrategy getTypeModelToTargetStrategy(){
		return new UpdateValueStrategy(){
			@Override
			public Object convert(Object value) {
				if(value instanceof String){
					return getEpackageFactoryItemName((String) value);
				}
				return super.convert(value);
			}
		};
	}
	
	/**
	 * ��ȡ�������ݵ�ģ�Ͷ�������Դ������
	 * @param project
	 * @return
	 */
	public static String getBasicDataType(IARESProject project){
		try {
			IARESProjectProperty properties = project.getProjectProperty();
			String name = properties.getString(IBasicDataEpacakgeConstant.Property_Basic_Data_type_ID);
			if(name != null){
				return getEpackageFactoryItemID(name);
			}
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		return "";
	}
}
