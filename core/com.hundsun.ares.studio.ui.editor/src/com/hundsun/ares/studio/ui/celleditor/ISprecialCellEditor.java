/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.celleditor;

import org.eclipse.swt.graphics.Image;

/**
 * һЩ�����cellEditor
 * ����Ҫչ��ͼƬ
 * ����չ�ֵ��ַ������ڴ�ģ�͵��ַ�����һ��
 * 
 * ���Ҫʵ����Щ�����Ǿ�Ҫ�øýӿ��еķ�����ת��
 * @author maxh
 */
public interface ISprecialCellEditor {

	public abstract Image getImage(Object value);

	public abstract String getText(Object value);
	
	public abstract Object getRealGetValue(Object value);

	public abstract Object getRealSetValue(Object value);
}
