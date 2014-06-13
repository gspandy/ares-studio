/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.ui.newwizard;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.hundsun.ares.studio.core.IARESElement;
import com.hundsun.ares.studio.ui.ARESResourceCategory;

/**
 * @author lvgao
 *
 */
public class ElementSelectionValidator extends BaseWizardPageValidator  implements IWizardPageValidator{
	public static final int CATEGORY = 8888888;
	public static final int UNKNOWN = Integer.MAX_VALUE;
	
	
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.wizards.pages.IWizardPageValidator#validate(java.lang.Object)
	 */
	@Override
	public IStatus validate(Map<Object, Object> context) {
		Object selection = context.get(CommonElementSelectionPageEX.CONTEXT_KEY_SELECTION);
		if (selection == null) {
			return getErrorStatus("ѡ����Ϊ��");
		} else {
			
			String[][] selectingTypes = getSelctingElementTypes(selection);
			if (selectingTypes == null) {
				return Status.OK_STATUS;
			}
			
			int curType = getCurrentSelectionType(selection);
			for (String[] type : selectingTypes) {
				if (type[0].equals(String.valueOf(curType))) {
					return Status.OK_STATUS;
				}
 			}
			
			StringBuffer errorMsg = new StringBuffer();
			errorMsg.append("����ѡ��һ���������͵Ľڵ㣺 ");
			for (String[] type : selectingTypes) {
				errorMsg.append(type[1]);
				errorMsg.append("��");
			}
			errorMsg.deleteCharAt(errorMsg.length() - 1);
			
			return getErrorStatus(errorMsg.toString());
		}
	}
	
	
	
	
	protected int getCurrentSelectionType(Object selection) {
		if (selection instanceof IARESElement) {
			return ((IARESElement)selection).getElementType();
		} else if (selection instanceof ARESResourceCategory) {
			return CATEGORY;
		} else 
			return UNKNOWN;
	}
	
	/**
	 * ����Ҫ����ѡ�����Դ����; ��λ���飬��һ�����͵ڶ������֡�<br>
	 * ������ص���null������Ϊ�κ�ѡ�����Ч/�Ϸ�
	 */
	protected String[][] getSelctingElementTypes(Object selection) {
		return null;
	}
	
	

}
