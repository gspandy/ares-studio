/**
 * Դ�������ƣ�StdFieldDictDescColumnLabelProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.viewer;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeDictionaryItem;
import com.hundsun.ares.studio.jres.model.metadata.decrypt.DeDictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil;

/**
 * @author gongyf
 *
 *�ֵ���Ŀ˵����labelprovider���ֵ���Ŀ˵�����ɱ༭����ȡ���յ����õ��ֵ���Ŀ���������� a b c d4���ֵ������ʾ˵�������� 
 * a.�ֵ���(ð��)a.�ֵ���˵��(�ո�)b.�ֵ���(ð��)b.�ֵ���˵��(�ո�)c.�ֵ���(ð��)c.�ֵ���˵��(�ո�)d.�ֵ���(ð��)d.�ֵ���˵��
 * 
 */

public class StdFieldDictDescColumnLabelProvider extends ColumnLabelProvider {
	
	private IARESResource resource;
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
	 */
	/**
	 * @param resource
	 */
	public StdFieldDictDescColumnLabelProvider(IARESResource resource) {
		super();
		this.resource = resource;
	}


	@Override
	public Color getBackground(Object element) {
		return Display.getDefault().getSystemColor(SWT.COLOR_GRAY);
	}
	
	@Override
	public String getText(Object element) {
		StringBuffer text = new StringBuffer();
		if(element instanceof StandardField){
			StandardField std = (StandardField) element;
			if(StringUtils.isNotBlank(std.getDictionaryType())){
				DeDictionaryType dictionary = MetadataUtil.decrypt(std,resource).getDictionaryType();
				EList<DeDictionaryItem>items = dictionary.getItems();
				
				for(DeDictionaryItem item : items){
					String value = StringUtils.defaultString(item.getValue());
					String chineseName = StringUtils.defaultString(item.getChineseName());
					text.append(value);
					text.append(":");
					text.append(chineseName);
					text.append(" ");
				}
			}
			
		}
		return text.toString();
	}
}
