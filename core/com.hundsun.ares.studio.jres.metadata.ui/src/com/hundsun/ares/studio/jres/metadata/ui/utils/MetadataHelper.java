/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.metadata.ui.utils;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataResType;

/**
 * Ԫ���ݰ�������
 * @author qinyuan
 *
 */
public class MetadataHelper {
	
	/**
	 * ������Դ���ͻ�ȡ��Դ����������
	 * @param type
	 * @return
	 */
	public static String getRefTypeByResource(String type){
		if(StringUtils.equals(type, IMetadataResType.BizType)){
			return IMetadataRefType.BizType;
		}else if(StringUtils.equals(type, IMetadataResType.Const)){
			return IMetadataRefType.Const;
		}else if(StringUtils.equals(type, IMetadataResType.DefValue)){
			return IMetadataRefType.DefValue;
		}else if(StringUtils.equals(type, IMetadataResType.Dict)){
			return IMetadataRefType.Dict;
		}else if(StringUtils.equals(type, IMetadataResType.ErrNo)){
			return IMetadataRefType.ErrNo;
		}else if(StringUtils.equals(type, IMetadataResType.Menu)){
			return IMetadataRefType.Menu;
		}else if(StringUtils.equals(type, IMetadataResType.StdField)){
			return IMetadataRefType.StdField;
		}else if(StringUtils.equals(type, IMetadataResType.StdType)){
			return IMetadataRefType.StdType;
		}
		
		return "";
	}

}
