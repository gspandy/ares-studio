package com.hundsun.ares.studio.ui.editor.actions;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.model.util.RevisionHistoryUtil;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.ui.editor.ARESEditorPlugin;

/**
 * �Ӱ汾 ��ť�Ĺ��ܣ� ��ǰģ���е����汾��+1��Ȼ�����һ����¼�����Դ�Ϊ�¼�¼�İ汾��
 * 2012-08-16 sundl�� �������汾��ʱ��ͬʱ������Ŀ�����еİ汾��
 * @author sundl
 *
 */
public class IncreaseVersionAction extends AddRevisionHistoryAction {

	public static final String ID = "increase_version_id";
	
	private IARESResource aresResource;
	
	public IncreaseVersionAction(ColumnViewer viewer, EditingDomain editingDomain, EObject info, EReference eReference, IARESResource aresResource) {
		super(viewer, editingDomain, info, eReference);
		this.info = info;
		this.aresResource = aresResource;
		
		setId(ID);
		setText("�Ӱ汾");
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(ARESEditorPlugin.PLUGIN_ID, "icons/full/obj16/increaseVersion.png"));
	}

	@Override
	protected String getVersion() {
		// 2012-09-28 sundl �������汾��ʱ��ֻȡ��Դ���ڵĵ�ǰ���ģ��
		// 2012-11-21 sundl ���ݿ��µ���Դ������ģ����㣻 �����ط�����Դ��Ȼȡ��ǰģ��
		// 2012-12-28 sundl Ԫ�����µ� ��Դ������ģ�飬ֻ�ڱ���Դ����Ŀ������ȡ���ֵ
		// 2014��4��9��16:50:15 ��Ԫ UFT������Ҫȡ���ж�����Դ�İ汾
		
		// ģ�����������Դ�����汾
		String moduleRootVersion = null;
		IARESModule topModule = null; 
		if (aresResource == null) {
			topModule = null; 
		} else {
			String rootType = aresResource.getRoot().getType(); 
			if (ARESElementUtil.isDatabaseRoot(rootType)) {
				topModule = ARESElementUtil.getTopModule(aresResource);
			} else if (ARESElementUtil.isMetadataRoot(rootType)) {
				// topModuleΪnull��Ч�����ǲ�����ģ��
				topModule = null;
			} else if(ARESElementUtil.isUFTStructureRoot(rootType)) {
				topModule = null;
				moduleRootVersion = RevisionHistoryUtil.getMaxVersionByModuleRoot(aresResource.getRoot());
			}else {
				topModule = aresResource.getModule();
			}
		}
		
		// ��ǰ�Ѿ��������Դ�е����汾
		String currentVersion = RevisionHistoryUtil.getMaxVersion(topModule);
		// ��ǰ�༭���е����汾
		String maxInEditor = RevisionHistoryUtil.getMaxVersion((List<RevisionHistory>)info.eGet(eReference));
		// ��Ŀ����
		String projectVersion = RevisionHistoryUtil.getProjectPropertyVersion(aresResource.getARESProject());
		
		// ������3�����ֵ
		String versionStr = RevisionHistoryUtil.max(Arrays.asList(currentVersion, maxInEditor, projectVersion,moduleRootVersion));
		// ��һ���Ҳ����κμ�¼��ʱ��
		if (StringUtils.isEmpty(versionStr)) {
			versionStr = "1.0.0.0";
		} 
		
		// ���ֵ++
		return RevisionHistoryUtil.increase(versionStr);
	}
	
	// ��Ϊ������Action��ִ�н����������Ŀ���ԣ������޷�����������ÿ��ִ�ж����´�������֤ˢ�¡�
	@Override
	public Command getCommand() {
		return createCommand();
	}
	
}
