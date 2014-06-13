/**
 * Դ�������ƣ�ImportOperation.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.core
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�dollyn
 */
package com.hundsun.ares.studio.core.excel;

import java.io.File;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.dialogs.IOverwriteQuery;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.BasicResourceInfo;
import com.hundsun.ares.studio.core.model.Constants;
import com.hundsun.ares.studio.core.model.CoreFactory;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.core.model.ModuleProperty;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.util.log.Log;

/**
 * @author sundl
 *
 */
public abstract class ImportOperation implements IWorkspaceRunnable{
	
	protected static final Logger logger = Logger.getLogger(ImportOperation.class);
	
	protected File[] files;
	public  BizExcelLog log  = new BizExcelLog();
	protected Multimap<Module, Resource>  resources = ArrayListMultimap.create();
	protected IOverwriteQuery overwriteQuery;
	public IARESProject project;
	protected ExcelParser exlPaser;
	
	/** ������ɺ���Ҫ���еĲ���
	 *  Ŀǰ����������ƽ���XML��ǩ��������չ���ԵĴ���
	 */
	protected PostParseOperation postParseOperation = new PostParseOperation(log);
	
	/**
	 * ����Դ��ͻ������£�����ѯ���û���δ���
	 * @param files
	 * @param overwriteQuery
	 */
	public ImportOperation(File[] files, IOverwriteQuery overwriteQuery) {
		this.files = files;
		this.overwriteQuery = overwriteQuery;
	}

	protected abstract ExcelParser createParser(File file, Log log) ;
	protected abstract IARESModuleRoot getRoot(Resource res);

	protected  void checkRes(IProgressMonitor monitor) {
		monitor.beginTask("", resources.size() * 2);
		monitor.subTask("У����Դ��...");
		Multimap<String, Resource> hashSet = ArrayListMultimap.create();
		for (Resource res : resources.values()) {
			hashSet.put(res.getDescription(), res);
			monitor.worked(1);
		}
		
		// У����Դ�ظ�
		for (String key : hashSet.keySet()) {
			monitor.worked(1);
			// ��Դ��Ϊ�յľͲ���ʾ�ظ��ˣ���Ϊ����Դ��Ϊ�յĴ�����ʾ
			if (StringUtils.isEmpty(key)) 
				continue;
			
			if (hashSet.get(key).size() > 1) {
				for (Resource r : hashSet.get(key)) {
					log.error("��Դ�ظ���" + r.getDescription(), r.startLoc);
				}
			}
		}
		monitor.done();
	}
	
	protected void createResources(IProgressMonitor monitor) throws ARESModelException, ExcelHandlerException{
		// д��Դ
		//monitor.subTask("������Դ�ļ�");
		String queryResult = null;
		monitor.beginTask("", resources.size());

		// ��Ӣ��������ĸ�����  ����-->Ӣ�ģ�BiMapҲ���Է������
		MODULES_LOOP: for (Module module : resources.keySet()) {
			for (Resource res : resources.get(module)) {
				String resDescription = res.getDescription();
				monitor.subTask("������Դ�ļ�:" + resDescription);
				IARESModuleRoot root = getRoot(res);
				if (root == null) {
					String exception = String.format("δ�ҵ���Դ����%s��Ӧ��ģ��������鹤�������ԣ�", res.type);
					log.logResFailed(res, "�޷�ȷ����ԴӦ�÷����ĸ�ģ�����");
					throw new ExcelHandlerException(exception);
				}
				
				// �Ƿ��Ѿ�����
				boolean exists = module.exists(root);
				IARESModule aresModule = module.create(root, log);
				if (aresModule == null) {
					String exception = String.format("û��ģ����Ϣ�򴴽�ģ��ʧ��(ģ�飺%s)", module.getFullName());
					log.logResFailed(res, exception);
					throw new ExcelHandlerException(exception);
				} else {
					// ��һ�δ�����д���޸ļ�¼
					if (!exists) {
						IARESResource propertyRes = aresModule.getARESResource(IARESModule.MODULE_PROPERTY_FILE);
						if (propertyRes.exists()) {
							ModuleProperty mp = propertyRes.getWorkingCopy(ModuleProperty.class);
							if (mp != null) {
								JRESResourceInfo info = (JRESResourceInfo) mp.getMap().get(Constants.RevisionHistory.MODULE_REVISION_EXT_KEY);
								if (info == null) {
									info = CoreFactory.eINSTANCE.createJRESResourceInfo();
									mp.getMap().put(Constants.RevisionHistory.MODULE_REVISION_EXT_KEY, info);
								}
								Collection<RevisionHistory> histories = exlPaser.histories.get("ȫ��");
								for (RevisionHistory his : histories) {
									info.getHistories().add(EcoreUtil.copy(his));
								}
								propertyRes.save(mp, true, null);
							}
						}
					}
				}
				
				IARESResource existingRes = aresModule.getARESResource(res.name, res.type);
				if (existingRes != null && existingRes.exists()) {
					BasicResourceInfo basicInfo = existingRes.getInfo(BasicResourceInfo.class);
					Resource old = new Resource();
					old.name = basicInfo.getName();
					old.info = basicInfo;
					
					// �Ѿ����ڣ��Ƿ���Ҫѯ���û���
					if (StringUtils.equals(queryResult, IOverwriteQuery.ALL)) {
						// ���ǰ���Ѿ�ѯ�ʹ������ҽ���� ȫ��Ϊ��,ֱ�Ӹ���
						createRes(res, aresModule);
						log.logResOverwritten(res);
					} else if (StringUtils.equals(queryResult, IOverwriteQuery.NO_ALL)) {
						// ���ǰ���Ѿ�ѯ�ʹ������ҽ���� ȫ��Ϊ��ֱ������
						log.logResSkipped(res);
					} else if (StringUtils.equals(queryResult, IOverwriteQuery.CANCEL)) {
						break MODULES_LOOP;
					} else {
						// �粻�����������������ѯ���û��Ƿ񸲸�
						queryResult = this.overwriteQuery.queryOverwrite(resDescription);
						if (StringUtils.equals(queryResult, IOverwriteQuery.ALL) ||
								StringUtils.equals(queryResult, IOverwriteQuery.YES)) {
							createRes(res, aresModule);
							log.logResOverwritten(res);
						} else if (StringUtils.equals(queryResult, IOverwriteQuery.NO)) {
							log.logResSkipped(res);
						}
					}
				} else {
					// ��Դ�����ڣ�ֱ�Ӵ���
					createRes(res, aresModule);
					log.logResCreted(res);
				}
				monitor.worked(1);
			}
		}
	}
	
	protected void createRes(Resource res, IARESModule module) {
		try {
			res.create(module);
		} catch (ARESModelException e) {
			log.logResFailed(res, e.getMessage());
		}
	}
	
	protected void dispose() {
		files = null;
		log = null;
		resources.clear();
		resources=null;
	}
	
}
