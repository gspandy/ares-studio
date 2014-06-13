/**
 * Դ�������ƣ�DefaultValidateControl.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hundsun.ares.studio.ui.validate.IProblemPool;
import com.hundsun.ares.studio.ui.validate.IValidateControl;
import com.hundsun.ares.studio.ui.validate.IValidateUnit;

public class DefaultValidateControl implements IValidateControl{
	
	ValidateJob job;
	Map<Object, Object> context = new HashMap<Object, Object>();
	IProblemPool problempool;
	
	public DefaultValidateControl(){
	}
	
	protected List<IValidateUnit> unitList = new ArrayList<IValidateUnit>();
	

	@Override
	public void addValidateUnit(IValidateUnit unit) {
		unitList.add(unit);
	}

	@Override
	public void removeValidateUnit(IValidateUnit unit) {
		unitList.remove(unit);
	}

	protected ValidateJob getJob() {
		if (job == null) {
			job = new ValidateJob("������");
			job.setSystem(true);
		}
		return job;
	}
	
	@Override
	public void refresh() {
		getJob().cancel();
		getJob().setProblempool(getProblemPool());
		// �����Ķ�����ڼ��������޸�
		getJob().setContext(new HashMap<Object, Object>(getContext()));
		getJob().setUnits(unitList.toArray(new IValidateUnit[0]));
		getJob().schedule(getCheckJobDelay());
	}
	
	protected long getCheckJobDelay() {
		return 200;
	}
	
	public IProblemPool getProblemPool() {
		return problempool;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IValidateControl#setProblemPool(com.hundsun.ares.studio.jres.ui.validate.IProblemPool)
	 */
	@Override
	public void setProblemPool(IProblemPool pool) {
		this.problempool = pool;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IValidateControl#setContext(java.util.Map)
	 */
	@Override
	public void setContext(Map<Object, Object> context) {
		this.context = context;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IValidateControl#getContext()
	 */
	@Override
	public Map<Object, Object> getContext() {
		return context;
	}

	@Override
	public void destroyAll() {
		unitList.clear();
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.validate.IValidateControl#refresh(com.hundsun.ares.studio.jres.ui.validate.IValidateUnit)
	 */
	@Override
	public void refresh(IValidateUnit validateUnit) {
		getJob().cancel();
		getJob().setProblempool(getProblemPool());
		getJob().setContext(new HashMap<Object, Object>(getContext()));
		getJob().setUnits(new IValidateUnit[]{validateUnit});
		getJob().schedule(getCheckJobDelay());
	}

}
