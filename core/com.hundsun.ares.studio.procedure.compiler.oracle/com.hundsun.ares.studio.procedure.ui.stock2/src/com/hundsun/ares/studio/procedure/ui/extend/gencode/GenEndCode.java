/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.ui.extend.gencode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.preferences.IPreferencesService;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProblem;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ICommonModel;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.GenCresModuleCode;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleGeneratorHelper;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleResourceStatisticsInfo;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.RevisionHistoryGenUtil;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procdure.constants.IProcedureResType;
import com.hundsun.ares.studio.procdure.provider.ProcedureUI;
import com.hundsun.ares.studio.procedure.ui.util.ProcedureModuleGeneratorHelper;
import com.hundsun.ares.studio.ui.ARESUI;

/**
 * @author qinyuan
 *
 */
public class GenEndCode extends GenCresModuleCode{
	
	public void genModuleEndCode(IARESModule module,
			boolean isWithPath, boolean isPathWithCname, IProgressMonitor monitor) {
		if(monitor.isCanceled()){
			return;
		}
		String newPath = ModuleGeneratorHelper.getModuleGenCodePath(module.getARESProject());
		IPreferencesService service = Platform.getPreferencesService();
		String charset = service.getString(ARESUI.PLUGIN_ID, ARESUI.PRE_GENERATE_CHARSET, StringUtils.EMPTY, null);
		try {
			if(isWithPath) {//��Ŀ¼
				if(isPathWithCname){//ʹ����������Ϊ�ļ�Ŀ¼
					String moduleCName = ModuleGeneratorHelper.getModuleProperty(module).getString(ICommonModel.CNAME);
					if(moduleCName != null && moduleCName.trim().length() > 0){
						newPath += ModuleGeneratorHelper.getParentModuleCnamePath(module);//����ģ��·��
						newPath += moduleCName;
						newPath += "\\";
						createFilePath(newPath);//�����ļ�Ŀ¼
					}
				}else{
					String moduleName = module.getShortName();
					if(moduleName != null && moduleName.trim().length() > 0){
						newPath += ModuleGeneratorHelper.getParentModulePath(module);//����ģ��·��
						newPath += moduleName;
						newPath += "\\";
						createFilePath(newPath);//�����ļ�Ŀ¼
					}
				}
			}
			//��ģ��
			IARESModule[] subModules = module.getSubModules();
			//��ģ����Դ
			IARESResource[] mRess = module.getARESResources(true);
			monitor.beginTask(newPath, mRess.length + subModules.length*10 + 1);
			if(mRess.length > 0) {//����Դ������
				if(mRess.length == 1 && StringUtils.equals(mRess[0].getType(), "module.xml")) {//�п�����ģ������
					System.out.println(mRess[0].getType());
				}else {
					createModuleFile(monitor,module,newPath,charset);//���ɺ��ô���
				}
			}
			
			//�ݹ�ģ��
			for (IARESModule subModule : subModules) {
				genModuleEndCode(subModule,isWithPath,isPathWithCname,new SubProgressMonitor(monitor, 10));
			}
			
			monitor.done();
		} catch (Exception e) {
			e.printStackTrace();
			monitor.setCanceled(true);
		}
	}
	

	/**
	 * @param monitor
	 * @param module
	 * @param newPath
	 * @param charset
	 */
	private void createModuleFile(IProgressMonitor monitor, IARESModule module,
			String path, String charset)  throws Exception{
		//��ģ����Դ
		IARESResource[] moduleRes = module.getARESResources(IProcedureResType.PROCEDURE);
		createEndCodeFile(moduleRes, "_proc_help_after.sql", monitor, module, path, charset);
		
		//��ģ��������Դ��Դ���ݹ���ģ��
		IARESResource[] moduleAllRes = module.getARESResources(IProcedureResType.PROCEDURE,true);
		if(moduleRes.length != moduleAllRes.length) {
			//��ǰģ��û����ģ�飬����Ҫ����һ�������ļ�
			createEndCodeFile(moduleAllRes, "_all_proc_help_after.sql", monitor, module, path, charset);
		}
	}


	/**
	 * @param moduleAllRes
	 * @param string
	 * @param monitor
	 * @param module
	 * @param path
	 * @param charset
	 */
	private void createEndCodeFile(IARESResource[] moduleRes, String fileSuffixName,
			IProgressMonitor monitor, IARESModule module, String path,
			String charset) throws Exception{
		// ģ����û�й��̣�������sql�ļ�
		if(moduleRes == null || moduleRes.length < 1) {
			return;
		}
		
		List<IARESResource> modulesReslist = Arrays.asList(moduleRes);
		//����
		Collections.sort(modulesReslist,new Comparator<IARESResource>() {
			
			@Override
			public int compare(IARESResource o1, IARESResource o2) {
				try {
					//��ʱ��ԴΪ�洢����
					Procedure p1 = o1.getInfo(Procedure.class);
					Procedure p2 = o2.getInfo(Procedure.class);
					return Integer.valueOf(p1.getObjectId()) - Integer.valueOf(p2.getObjectId());
					
				} catch (ARESModelException e) {
					e.printStackTrace();
					return 0;
				}
			}
		});
		
		//���ɴ���ʱ��������Ϣ�������Ϣ����
		Queue<IARESProblem> msgQueue = new ArrayDeque<IARESProblem>();
		
		//���ݿ���
		String dbName = "";
		
		String codeFileName = path + dbName + module.getShortName() + fileSuffixName;
		StringBuffer codeBuffer = new StringBuffer();
		
		// д��ע��ͷ
		ModuleGeneratorHelper.writeCommentHeader(codeBuffer, 
				ModuleGeneratorHelper.getCresProjectExtendProperty(module).getHeadFile(), 
				codeFileName, module.getShortName(), new Date());
		codeBuffer.append("\n");
		
		//��Դͳ����Ϣ
		List<ModuleResourceStatisticsInfo> infos = new ArrayList<ModuleResourceStatisticsInfo>();
		StringBuffer resCodeBuffer = new StringBuffer();//��Դ����
		//�����߼�������������
		for (IARESResource iaresResource : modulesReslist) {
			if(monitor.isCanceled()){
				return;
			}
			monitor.setTaskName("�洢���̣�"+iaresResource.getFullyQualifiedName());
			Procedure procedure = iaresResource.getInfo(Procedure.class);
			if(StringUtils.isBlank(procedure.getEndCode())){
				continue;
			}
			resCodeBuffer.append("\n/******************************************************\n");
			resCodeBuffer.append("**���ô���\n");
			resCodeBuffer.append("**��Դ����" + procedure.getName() + "\n");
			resCodeBuffer.append("**����ţ�" + procedure.getObjectId() + "\n");
			resCodeBuffer.append("******************************************************/\n");
			
			//2013��6��19��9:15:37 ���ô���ǰ���Զ����plsql��������
			resCodeBuffer.append("\n");
			resCodeBuffer.append("set define off;\n");
			resCodeBuffer.append("set feedback off;\n");
			resCodeBuffer.append("\n");
			resCodeBuffer.append(procedure.getEndCode());//���ô���
			resCodeBuffer.append("\r\n");
			resCodeBuffer.append("set define on;\n");
			resCodeBuffer.append("set feedback on;\n");
			resCodeBuffer.append("\n");
			infos.add(new ModuleResourceStatisticsInfo(procedure.getObjectId(), procedure.getName(), procedure.getChineseName(), procedure.getDescription()));
			monitor.worked(1);
		}
		
		codeBuffer.append(ModuleGeneratorHelper.getResourceStatisticsInfo(infos));
		codeBuffer.append("\n");
		// �����޸ļ�¼
		codeBuffer.append(RevisionHistoryGenUtil.getAllRevisionHistoryInfo(ProcedureModuleGeneratorHelper.getProcedureModuleHistorys(module), "--"));
		codeBuffer.append("\n");
		
		//����Ҫ�����ļ�ͷ�����Ϣ
		if(ProcedureUI.isStock2Procedure()) {
			codeBuffer.append("set define off;\r\n");
			codeBuffer.append("set feedback off;\r\n");
			codeBuffer.append("set serveroutput on;\r\n");
		}
		
		codeBuffer.append(resCodeBuffer);
		codeBuffer.append("\n");

		//д���ļ�
		writeToFile(codeFileName, codeBuffer.toString(),charset);
		//================================== err
		String errLogFileName = "s_as_" + module.getElementName() + "_end_proc.errorlog";
		createErrLog(msgQueue, errLogFileName,path,charset);
	}


	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.cres.extend.ui.module.gencode.IGenCresModuleCode#genModuleCode(com.hundsun.ares.studio.core.IARESModule, java.util.Map, boolean, boolean, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void genModuleCode(IARESModule module, Map<Object, Object> context,
			boolean isWithPath, boolean isPathWithCname,
			IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.cres.extend.ui.module.gencode.IGenCresModuleCode#canGenCode(com.hundsun.ares.studio.core.IARESModule)
	 */
	@Override
	public boolean canGenCode(IARESModule module) {
		// TODO Auto-generated method stub
		return false;
	}

}
