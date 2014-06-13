/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.ui.assistant;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.ui.extendpoint.manager.ExtendPageInfo;
import com.hundsun.ares.studio.ui.extendpoint.manager.ExtendPageManager;

/**
 * @author qinyuan
 *
 */
public class ProcedurePageHelper {
	
	//���̱༭��ע��id
	public static final String procedure_editor_id = "com.hundsun.ares.studio.procedure.ui.editor.procedure";
	
	//֤ȯ������չҳ��id
	public static final String stock_two_ex_page_id = "com.hundsun.ares.studio.procedure.ui.extend.procedureextendpage";

	/**
	 * �����Ƿ���֤ȯ������չҳ��
	 * @return
	 */
	public static boolean hasProcedureStock2ExtendPage() {
		for(ExtendPageInfo info:ExtendPageManager.getDefault().getPageInfo(procedure_editor_id)){
			if(StringUtils.equals(info.getPageId(), ProcedurePageHelper.stock_two_ex_page_id)){
				return true;
			}
		}
		return false;
	}

}
