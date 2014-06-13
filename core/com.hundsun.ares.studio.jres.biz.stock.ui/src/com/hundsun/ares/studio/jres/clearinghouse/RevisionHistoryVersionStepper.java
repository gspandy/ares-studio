/**
 * 
 */
package com.hundsun.ares.studio.jres.clearinghouse;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.TriggerListener;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.CorePackage;
import com.hundsun.ares.studio.core.model.JRESResourceInfo;
import com.hundsun.ares.studio.core.model.RevisionHistory;
import com.hundsun.ares.studio.core.model.util.RevisionHistoryUtil;
import com.hundsun.ares.studio.core.util.ARESElementUtil;
import com.hundsun.ares.studio.jres.database.constant.IDatabaseResType;

/**
 * @author gongyf
 *
 */
public class RevisionHistoryVersionStepper extends TriggerListener implements
		ResourceSetListener {

	/* (non-Javadoc)
	 * @see org.eclipse.emf.transaction.TriggerListener#trigger(org.eclipse.emf.transaction.TransactionalEditingDomain, org.eclipse.emf.common.notify.Notification)
	 */
	@Override
	protected Command trigger(TransactionalEditingDomain domain,
			Notification notification) {
		if (CorePackage.Literals.JRES_RESOURCE_INFO__HISTORIES.equals(notification.getFeature()) 
				&& notification.getNewValue() instanceof RevisionHistory 
				/*&& notification.getNotifier() instanceof TableResourceData*/) {
			RevisionHistory rh = (RevisionHistory) notification.getNewValue();
			if (StringUtils.isBlank(rh.getVersion())) {
				// �������޶���ʷ�󣬰Ѱ汾����
				return new IncreaseVersionCommand(domain, (JRESResourceInfo) notification.getNotifier(), rh);
			}
			
			
		}
		return null;
	}
	
	static class IncreaseVersionCommand extends RecordingCommand {
		
		private JRESResourceInfo table;
		private RevisionHistory history;

		/**
		 * @param domain
		 * @param table
		 * @param history
		 */
		public IncreaseVersionCommand(TransactionalEditingDomain domain,
				JRESResourceInfo table, RevisionHistory history) {
			super(domain);
			this.table = table;
			this.history = history;
		}
		
		@Override
		protected void doExecute() {
			// ������Ŀ����
			IARESProject project = null;
			IFile file = null;
			try {
				String path = table.eResource().getURI().toPlatformString(true);
				
				file = (IFile) ResourcesPlugin.getWorkspace().getRoot().findMember(path);
				project = ARESCore.create(file.getProject());
			} catch (Exception e) {
			}
			
			history.setVersion(getMaxVersion(project, file));
		}
		
		private String getMaxVersion (IARESProject project ,IFile file ){
			String versionStr = "1.0.0.0";
			IARESModule topModule = null; 
			IARESResource resource = null;
			if (project == null) {
				return versionStr;
			} else {
				IPath path = file.getFullPath().makeRelativeTo(project.getPath());
				try {
					String fullName = StringUtils.substringAfter(path.toOSString(), File.separator);
					fullName = StringUtils.replace(fullName, File.separator, ".");
					fullName = StringUtils.substringBeforeLast(fullName, ".");
					resource = project.findResource(fullName, IDatabaseResType.Table);
				} catch (ARESModelException e) {
					e.printStackTrace();
				}
				if (resource == null) {
					return versionStr;
				}else {
					String rootType = resource.getRoot().getType(); 
					if (ARESElementUtil.isDatabaseRoot(rootType)) {
						topModule = ARESElementUtil.getTopModule(resource);
					} else if (ARESElementUtil.isMetadataRoot(rootType)) {
						// topModuleΪnull��Ч�����ǲ�����ģ��
						topModule = null;
					} else {
						topModule = resource.getModule();
					}
				}
			}
			
			// ��ǰ�Ѿ��������Դ�е����汾
			String currentVersion = RevisionHistoryUtil.getMaxVersion(topModule);
			// ��ǰ�༭���е����汾
			String maxInEditor = RevisionHistoryUtil.getMaxVersion((List<RevisionHistory>)table.eGet(CorePackage.Literals.JRES_RESOURCE_INFO__HISTORIES));

			// ��Ŀ����
			String projectVersion = RevisionHistoryUtil.getProjectPropertyVersion(resource.getARESProject());
			
			// ������3�����ֵ
			versionStr = RevisionHistoryUtil.max(Arrays.asList(currentVersion, maxInEditor, projectVersion));
			// ��һ���Ҳ����κμ�¼��ʱ��
			if (StringUtils.isEmpty(versionStr)) {
				versionStr = "1.0.0.0";
			}
			return versionStr;
		}
		
	}

	
}
