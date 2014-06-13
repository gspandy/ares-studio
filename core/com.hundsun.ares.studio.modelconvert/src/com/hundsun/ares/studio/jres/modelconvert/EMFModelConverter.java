/**
 * Դ�������ƣ�EMFModelConverter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.modelconvert
 * ����˵�����ļ���ȡ�ͷ������е�������չʵ��
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.modelconvert;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.JRESResourceInfo;

/**
 * ������EMF��д��֧����
 * @author gongyf
 *
 */
public class EMFModelConverter extends BaseModelConverter {
	

	@Override
	protected void processInfoOnRead(Object info, IARESResource resource) {
		if (info instanceof JRESResourceInfo) {
			// �����ʱ��Ϣ
			((JRESResourceInfo) info).setFullyQualifiedName(resource.getFullyQualifiedName());
//			((JRESResourceInfo) info).setProject(new JRESProjectHandlerImpl(resource.getARESProject()));
			((JRESResourceInfo) info).setName(resource.getName());
		}
	}
	
	@Override
	protected void processInfoOnWrite(Object info, IARESResource resource) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.modelconvert.BaseModelConverter#getDefaultModelConverterHandle(java.lang.String)
	 */
	@Override
	protected ModelConverterHandle getDefaultModelConverterHandle(String type) {
		return new DefaultEMFModelConverterHandle();
	}
}
