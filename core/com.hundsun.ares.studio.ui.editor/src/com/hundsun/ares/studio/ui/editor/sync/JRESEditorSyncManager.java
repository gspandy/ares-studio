/**
 * Դ�������ƣ�JRESEditorSyncManager.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.sync;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * �༭��ͬ��������
 * @author lvgao
 *
 */
public class JRESEditorSyncManager implements IResourceChangeListener {
	private static final Logger logger = Logger.getLogger(JRESEditorSyncManager.class);

	public static JRESEditorSyncManager instance;
	
	private List<IFileSyncnizeUnit> syncList = new ArrayList<IFileSyncnizeUnit>();
	
	private JRESEditorSyncManager(){
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	public static JRESEditorSyncManager getInstance(){
		if(null == instance){
			instance = new JRESEditorSyncManager();
		}
		return instance;
	}

	/**
	 * ���ͬ����Ԫ
	 * @param unit
	 */
	public void addSyncUnit(IFileSyncnizeUnit unit){
		syncList.add(unit);
	}
	
	/**
	 * �Ƴ�ͬ����Ԫ
	 * @param unit
	 */
	public void removeSyncUnit(IFileSyncnizeUnit unit){
		syncList.remove(unit);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			IResourceDelta delta= event.getDelta();
			for(IFileSyncnizeUnit unit:syncList){
				unit.handleAction(delta);
			}
		} catch (Exception e) {
			logger.info("�༭�������������������ʧ�ܡ�");
		}
	}
	
}
