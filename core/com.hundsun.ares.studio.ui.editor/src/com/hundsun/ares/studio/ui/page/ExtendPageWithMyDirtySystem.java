/**
* <p>Copyright: Copyright   2010</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.page;

import org.eclipse.ui.forms.editor.FormEditor;

import com.hundsun.ares.studio.ui.extendpoint.manager.IExtendedPage;

/**
 * ����״̬����չ����
 * @author maxh
 *
 */
public abstract class ExtendPageWithMyDirtySystem<T> extends FromPageWithMyDirtySystem<T> implements IExtendItemLoader, IExtendedPage{

	public ExtendPageWithMyDirtySystem(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	public abstract boolean shouldLoad();
	
	/**
	 * ����һ�������Hook��ʹҳ������ڱ���֮ǰ��һЩ������
	 * <b>д�ļ���������չ��Ϣ����Ҫ������ʵ�֣����������Ϊ����һЩ��������������Acide�ﱣ���ʱ����Ҫͬ���������ļ���</b>
	 * @Deprecated ʵ��IExtendedPage#beforeSave()
	 */
	@Deprecated
	public boolean doSave() {return true;}

	@Override
	public void init(FormEditor editor) {
		initialize(editor);
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void beforeSave() {
	}

	@Override
	public void afterSave() {
	}
	
}
