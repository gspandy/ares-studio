/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.logic.ui.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import com.hundsun.ares.studio.atom.AtomFunction;
import com.hundsun.ares.studio.atom.AtomService;
import com.hundsun.ares.studio.atom.constants.IAtomResType;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleGeneratorHelper;
import com.hundsun.ares.studio.logic.LogicFunction;
import com.hundsun.ares.studio.logic.LogicService;
import com.hundsun.ares.studio.logic.constants.ILogicResType;

/**
 * @author qinyuan
 *
 */
public class LogicModuleGeneratorHelper extends ModuleGeneratorHelper{
	
	/**
	 * ��ȡ�߼�ģ�������޶���¼
	 * @param module
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public static String getLogicModuleLastVersion(IARESModule module) throws Exception{
		List<RevisionHistory> hiss = getLogicModuleHistorys(module);
		if(hiss.size() > 0) {
			return hiss.get(0).getVersion();
		}
		return module.getARESProject().getProjectProperty().getVersion();
	}

	/**
	 * ��ȡ�߼�ģ���������޸ļ�¼
	 * �����߼������޸ļ�¼���߼������޸ļ�¼��ģ���޸ļ�¼
	 * @param module
	 * @return
	 * @throws Exception
	 */
	public static List<RevisionHistory> getLogicModuleHistorys(IARESModule module) throws Exception{
		List<RevisionHistory> hiss = new ArrayList<RevisionHistory>();//ģ�������е��޸ļ�¼
		IARESResource[] lss = module.getARESResources(ILogicResType.LOGIC_SERVICE);
		for (IARESResource ls : lss) {
			EList<RevisionHistory> his = ls.getInfo(LogicService.class).getHistories();//�߼������޸ļ�¼
			hiss.addAll(his);
		}
		IARESResource[] lfs = module.getARESResources(ILogicResType.LOGIC_FUNCTION);
		for (IARESResource lf : lfs) {
			EList<RevisionHistory> his = lf.getInfo(LogicFunction.class).getHistories();//�߼������޸ļ�¼
			hiss.addAll(his);
		}
		
		hiss.addAll(getModuleHistorys(module));//ģ���޸ļ�¼
		
		sortHistoryByVersion(hiss);//����
		return hiss;
	}

}
