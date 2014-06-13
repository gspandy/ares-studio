/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.editor.extend.user;

import java.util.Map;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.ui.editor.extend.IExtensibleModelPropertyDescriptor;

/**
 * �û���չ����������չ��ӿ�
 * @author sundl
 */
public interface IUserExtendPropertyTypeProvider {

	/**
	 * ����һ��IExtensibleModelPropertyDescriptor����������ʾһ���û���չ������
	 * @param config �û�����xml�������map, ���� <Attribute id="customer" name="�ͻ����Ի�" type="string" />������ͻ����Щ���Էŵ����map��
	 * @return
	 */
	IExtensibleModelPropertyDescriptor createPropertyDescriptor(IARESProject project, Map<String, String> config);
}
