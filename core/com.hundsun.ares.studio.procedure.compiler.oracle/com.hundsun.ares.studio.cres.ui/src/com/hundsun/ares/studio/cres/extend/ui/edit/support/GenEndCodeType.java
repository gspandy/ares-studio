/**
 * 
 */
package com.hundsun.ares.studio.cres.extend.ui.edit.support;

import org.apache.commons.lang.StringUtils;



/**
 * @author yanwj06282
 *
 */
public enum GenEndCodeType {

	CH_DIR_GENMODULE(1 ,"���ɺ��ô���(������Ŀ¼)"),
	
	EN_DIR_GENMODULE(2 ,"���ɺ��ô���(��Ӣ��Ŀ¼)"),
	
	NODIR_GENMODULE(3 ,"���ɺ��ô���(����Ŀ¼)");
	
	private static GenEndCodeType[] types = new GenEndCodeType[]{CH_DIR_GENMODULE ,EN_DIR_GENMODULE ,NODIR_GENMODULE};
	
	private int type;
	private String desc;
	
	private GenEndCodeType(int type , String desc){
		this.type = type;
		this.desc = desc;
	}
	
	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	public static GenEndCodeType[] getEnumTypes(){
		return types;
	}
	
	public static int getType(String desc){
		for (int i = 0; i < types.length; ++i) {
			GenEndCodeType result = types[i];
			if (result.getDesc().equals(desc)) {
				return result.getType();
			}
		}
		return 0;
	}
	
	public static String getDesc(int type){
		for (int i = 0; i < types.length; ++i) {
			GenEndCodeType result = types[i];
			if (result.getType() == type) {
				return result.getDesc();
			}
		}
		return StringUtils.EMPTY;
	}
	
}
