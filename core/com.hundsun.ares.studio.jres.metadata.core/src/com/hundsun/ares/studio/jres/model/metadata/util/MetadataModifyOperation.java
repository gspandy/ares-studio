package com.hundsun.ares.studio.jres.model.metadata.util;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.util.log.Log;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataResourceData;

/**
 * �Դ�����Ԫ���ݲ����ķ�װ�����������������������ӣ����ͳһִ��һ�Σ��������ļ���
 * ʹ���������Լ��ٴ�����getWorkingCopy()��save()д�ļ���ʱ�䡣
 * @author sundl
 *
 */
public class MetadataModifyOperation<T extends MetadataItem> {
	
	private static Logger logger = Logger.getLogger(MetadataModifyOperation.class);
	
	private Multimap<String, IMetadataModifyCommand> cmdMap = ArrayListMultimap.create();
	
	private IARESProject project;
	private String resType;
	
	public MetadataModifyOperation(IARESProject project, String resType) {
		this.project = project;
		this.resType = resType;
	}
	
	public void addCommand(IMetadataModifyCommand command) {
		cmdMap.put(command.getId(), command);
	}
	
	public void run(Log log, IProgressMonitor monitor) {
		monitor.beginTask("", cmdMap.size() + 2);
		monitor.subTask("�޸ı�׼�ֶ�...");
		IARESResource res = null;
		MetadataResourceData<?> data = null;
		try {
			monitor.subTask("������������...");
			res = project.findResource(resType, resType);
			if (res != null)
				data = res.getWorkingCopy(MetadataResourceData.class);
		} catch (ARESModelException e1) {
			e1.printStackTrace();
			log.error("�ڲ�����, �쳣��Ϣ��" + e1.getMessage(), null);
		}
		
		if (data != null) {
			for (MetadataItem item : data.getItems()) {
				Collection<IMetadataModifyCommand> commands = cmdMap.get(item.getName());
				for (IMetadataModifyCommand cmd : commands) {
					try {
						monitor.subTask(cmd.getDescription());
						logger.info("�ֶΣ�" + item.getName() + cmd.getDescription());
						cmd.excute(item, log);
					} catch (Exception e) {
						e.printStackTrace();
						log.error("�ڲ�����, �쳣��Ϣ��" + e.getMessage(), null);
					}
					monitor.worked(1);
				}
				// �������ɾ��
				cmdMap.removeAll(item.getName());
			}
			
			// �Ƿ���ʣ��ģ�˵���д�
			if (!cmdMap.isEmpty()) {
				for (IMetadataModifyCommand cmd : cmdMap.values()) {
					log.error(String.format("û���ҵ���׼�ֶ�%s, �޷�ִ������%s", cmd.getId(), cmd.getDescription()), null);
				}
			}
			
			monitor.subTask("������Դ...");
			try {
				res.save(data, true, null);
			} catch (ARESModelException e) {
				log.error("�����ļ�ʧ��, �쳣��Ϣ��" + e.getMessage(), null);
				e.printStackTrace();
			}
		}
		
		monitor.done();
	}
}
