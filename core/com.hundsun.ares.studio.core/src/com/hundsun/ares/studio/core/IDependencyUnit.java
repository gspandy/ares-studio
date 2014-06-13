/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import java.util.List;

/**
 * ���������һ�����ð�������һ��������Ŀ��
 * @author sundl
 */
public interface IDependencyUnit{

	// ������Դ���
	IARESModuleRoot[] getRoots() throws ARESModelException;
	
	/**
	 * �������������������� ���ﷵ�صĽ�����һ��������������
	 * ����ֻ������Ҫһ��ID��Versionȷ����������ھ���Ļ����п��Խ����ɲ�ͬ��IDependencyUnit
	 * @return
	 */
	public abstract List<IDependenceDescriptor> getDependencyDescriptors();
	
	/**
	 * ������IDependencyUnit����
	 * @return
	 */
	//public List<IDependencyUnit> getDependencies();

	// ������Ϣ
	public abstract String getPublishTime();

	public abstract String getNote();

	public abstract String getName();

	public abstract String getPublisher();

	public abstract String getContact();

	public abstract String getProvider();

	public abstract String getVersion();

	public abstract String getId();
	
	public abstract String getType();
	
	/**
	 * ������Ϣ������"������Դ��- lib.jar", "��Ŀ- project1"
	 * @return 
	 */
	public abstract String getDescriptionStr();
}
