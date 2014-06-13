/**
 * Դ�������ƣ�BasicDatabaseService.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.internal.service;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.service.FastFindArrayList;
import com.hundsun.ares.studio.core.service.IKeyProvider;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseRefType;
import com.hundsun.ares.studio.jres.database.service.IDatabaseService;
import com.hundsun.ares.studio.jres.database.service.ITable;
import com.hundsun.ares.studio.jres.database.service.IView;
import com.hundsun.ares.studio.jres.model.database.TableResourceData;
import com.hundsun.ares.studio.jres.model.database.ViewResourceData;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;

/**
 * @author gongyf
 *
 */
public class BasicDatabaseService implements IDatabaseService {

	private final IARESProject project;
	private FastFindArrayList<String, ITable> cachedTableList;
	private FastFindArrayList<String, IView> cachedViewList;
	
	
	/**
	 * @param project
	 */
	public BasicDatabaseService(IARESProject project) {
		super();
		this.project = project;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.core.service.IDataService#refresh()
	 */
	@Override
	public void refresh() {
		cachedTableList = null;
		cachedViewList = null;
	}

	/**
	 * @return the cachedTableList
	 */
	public FastFindArrayList<String, ITable> getCachedTableList() {
		if (cachedTableList == null) {
			cachedTableList = new FastFindArrayList<String, ITable>(new IKeyProvider<String, ITable>() {

				@Override
				public String getKey(ITable obj) {
					return obj.getName();
				}
			});
			
			// TODO ��Ӵ��ڵı�
			List<ReferenceInfo> refList = ReferenceManager.getInstance().getReferenceInfos(project, IDatabaseRefType.Table, true);
			for(ReferenceInfo ref : refList){
				// FIXME ����Ӧ�ÿ�����getResource.getName��ȡ���֣�ʡȥһ��getInfo�ĵ���
				TableResourceData tableResourceData = (TableResourceData)ref.getObject();
				String name = tableResourceData.getName();
				ITable table = new TableAdapter(tableResourceData,project);
				if (StringUtils.isBlank(name)) {
					continue;
				}
				else{
					cachedTableList.add(table);
				}
				
			}
		}

		return cachedTableList;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.IDatabaseService#getTableList()
	 */
	@Override
	public List<? extends ITable> getTableList() {
		return Collections.unmodifiableList(getCachedTableList());
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.IDatabaseService#getTable(java.lang.String)
	 */
	@Override
	public ITable getTable(String name) {
		return getCachedTableList().find(name);
	}
	
	/**
	 * @return the cachedViewList
	 */
	public FastFindArrayList<String, IView> getCachedViewList() {
		if (cachedViewList == null) {
			cachedViewList = new FastFindArrayList<String, IView>(new IKeyProvider<String, IView>() {

				@Override
				public String getKey(IView obj) {
					return obj.getName();
				}
			});
			
			// TODO ��Ӵ��ڵı�
			List<ReferenceInfo> refList = ReferenceManager.getInstance().getReferenceInfos(project, IDatabaseRefType.View, true);
			for(ReferenceInfo ref : refList){
				ViewResourceData viewResourceData = (ViewResourceData)ref.getObject();
				String name = viewResourceData.getName();
				IView view = new ViewAdapter(viewResourceData);
				if (StringUtils.isBlank(name)) {
					continue;
				}
				else{
					cachedViewList.add(view);
				}
				
			}
		}
		return cachedViewList;
	}


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.IDatabaseService#getViewList()
	 */
	@Override
	public List<? extends IView> getViewList() {
		return Collections.unmodifiableList(getCachedViewList());
	}


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.database.service.IDatabaseService#getView(java.lang.String)
	 */
	@Override
	public IView getView(String name) {
		return getCachedViewList().find(name);
	}

}
