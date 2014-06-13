/**
 * Դ�������ƣ�TableColumnChineseNameLabelProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.basicdata.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.basicdata.ui.editor.blocks;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.hundsun.ares.studio.core.IARESBundle;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.service.DataServiceManager;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.StandardFieldColumn;
import com.hundsun.ares.studio.jres.metadata.service.IDictionaryType;
import com.hundsun.ares.studio.jres.metadata.service.IMetadataService;
import com.hundsun.ares.studio.jres.metadata.service.IStandardField;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeDictionaryItem;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeDictionaryType;

/**
 * @author lvgao
 *
 */
public class TableColumnRefLabelProvider extends ColumnLabelProvider {
	
	public enum TYPE {
		ChineseName, Type, Desciption
	}
	
	private IARESBundle bundle;
	private TYPE type;
	
	/**
	 * 
	 * @param bundle 
	 * @param type ��ʾ��������Ϣ {@link TYPE}
	 */
	public TableColumnRefLabelProvider(IARESBundle bundle, TYPE type) {
		super();
		this.bundle = bundle;
		this.type = type;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		StandardFieldColumn column = (StandardFieldColumn) element;
		IARESProject project = com.hundsun.ares.studio.core.util.ResourcesUtil.getARESProject(bundle);
		IMetadataService service = DataServiceManager.getInstance().getService(project, IMetadataService.class);
		if (service != null) {
			IStandardField filed = service.getStandardField(column.getStandardField());
			if (filed != null) {
				switch (type) {
				case ChineseName:
					return StringUtils.defaultString(filed.getChineseName()) ;
				case Desciption:
					StringBuffer text = new StringBuffer();
					IDictionaryType dictType = filed.getDictionaryType();
					if (dictType instanceof DeDictionaryType) {
						for(DeDictionaryItem item : ((DeDictionaryType) dictType).getItems()){
							String value = StringUtils.defaultString(item.getValue());
							String chineseName = StringUtils.defaultString(item.getChineseName());
							text.append(value);
							text.append(":");
							text.append(chineseName);
							text.append(" ");
						}
					}
					return StringUtils.defaultString(StringUtils.defaultIfBlank(text.toString(), filed.getDescription())) ;
				case Type:
					return StringUtils.defaultString(filed.getDataTypeId()) ;
				}
				
			}
		}
		
		return StringUtils.EMPTY;
	}
}
