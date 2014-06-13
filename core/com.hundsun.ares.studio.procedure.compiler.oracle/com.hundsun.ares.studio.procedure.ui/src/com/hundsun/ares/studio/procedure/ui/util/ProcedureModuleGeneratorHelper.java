/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.ui.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleGeneratorHelper;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procdure.constants.IProcedureResType;

/**
 * @author qinyuan
 *
 */
public class ProcedureModuleGeneratorHelper extends ModuleGeneratorHelper{
	/**
	 * ��ȡģ�������޶���¼
	 * @param module
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public static String getModuleLastVersion(IARESModule module) throws Exception{
		List<RevisionHistory> hiss = getProcedureModuleHistorys(module);
		if(hiss.size() > 0) {
			return hiss.get(0).getVersion();
		}
		return module.getARESProject().getProjectProperty().getVersion();
	}

	/**
	 * ��ȡģ���������޸ļ�¼
	 * ���������޸ļ�¼��ģ���޸ļ�¼
	 * @param module
	 * @return
	 * @throws Exception
	 */
	public static List<RevisionHistory> getProcedureModuleHistorys(IARESModule module) throws Exception{
		List<RevisionHistory> hiss = new ArrayList<RevisionHistory>();//ģ�������е��޸ļ�¼
		IARESResource[] procs = module.getARESResources(IProcedureResType.PROCEDURE);
		for (IARESResource proc : procs) {
			EList<RevisionHistory> his = proc.getInfo(Procedure.class).getHistories();//�����޸ļ�¼
			hiss.addAll(his);
		}
		
		hiss.addAll(getModuleHistorys(module));//ģ���޸ļ�¼
		
		sortHistoryByVersion(hiss);//����
		return hiss;
	}
}
