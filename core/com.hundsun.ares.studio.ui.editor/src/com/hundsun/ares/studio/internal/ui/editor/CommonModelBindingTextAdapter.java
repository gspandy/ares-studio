/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.internal.ui.editor;

import com.hundsun.ares.studio.core.model.ICommonModel;
import com.hundsun.ares.studio.ui.control.TextAdaptor;
import com.hundsun.ares.studio.ui.util.ImporveControlWithDitryStateContext;

/**
 * �༭ĳ��ICommonModel�����Ե�Text���
 * @author sundl
 */
public class CommonModelBindingTextAdapter extends TextAdaptor {

	private String key;
	private ICommonModel model;
	
	public CommonModelBindingTextAdapter(String label, int controlStyle, ImporveControlWithDitryStateContext context, String key, ICommonModel model) {
		super(label, controlStyle, context);
		this.key = key;
		this.model = model;
	}

	@Override
	public void syncControl() {
		if (control != null && model != null)
			this.control.setText(model.getString(key));
	}

	@Override
	public void syncModel() {
		model.setValue(key, text.getText());
	}

}
