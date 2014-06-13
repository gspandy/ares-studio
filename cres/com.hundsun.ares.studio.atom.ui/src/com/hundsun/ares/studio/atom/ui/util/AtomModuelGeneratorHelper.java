/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.atom.ui.util;

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
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procdure.constants.IProcedureResType;

/**
 * @author qinyuan
 *
 */
public class AtomModuelGeneratorHelper extends ModuleGeneratorHelper{
	
	/**
	 * ��ȡԭ��ģ�������޶���¼
	 * @param module
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public static String getAtomModuleLastVersion(IARESModule module) throws Exception{
		List<RevisionHistory> hiss = getAtomModuleHistorys(module);
		if(hiss.size() > 0) {
			return hiss.get(0).getVersion();
		}
		return module.getARESProject().getProjectProperty().getVersion();
	}

	/**
	 * ��ȡԭ��ģ���������޸ļ�¼
	 * ����ԭ�Ӻ����޸ļ�¼��ԭ�ӷ����޸ļ�¼��ģ���޸ļ�¼
	 * @param module
	 * @return
	 * @throws Exception
	 */
	public static List<RevisionHistory> getAtomModuleHistorys(IARESModule module) throws Exception{
		List<RevisionHistory> hiss = new ArrayList<RevisionHistory>();//ģ�������е��޸ļ�¼
		IARESResource[] ass = module.getARESResources(IAtomResType.ATOM_SERVICE);
		for (IARESResource as : ass) {
			EList<RevisionHistory> his = as.getInfo(AtomService.class).getHistories();//ԭ�ӷ����޸ļ�¼
			hiss.addAll(his);
		}
		IARESResource[] afs = module.getARESResources(IAtomResType.ATOM_FUNCTION);
		for (IARESResource af : afs) {
			EList<RevisionHistory> his = af.getInfo(AtomFunction.class).getHistories();//ԭ�Ӻ����޸ļ�¼
			hiss.addAll(his);
		}
		
		hiss.addAll(getModuleHistorys(module));//ģ���޸ļ�¼
		
		sortHistoryByVersion(hiss);//����
		return hiss;
	}
}
