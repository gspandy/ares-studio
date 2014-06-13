package com.hundsun.ares.studio.procedure.ui.module.code;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.preferences.IPreferencesService;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.ARESProblem;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProblem;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ICommonModel;
import com.hundsun.ares.studio.cres.constant.ICresUIConstant;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.GenCresModuleCode;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.FileNameHelper;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleGeneratorHelper;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleResourceStatisticsInfo;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.RevisionHistoryGenUtil;
import com.hundsun.ares.studio.engin.logic.ResourceEngin;
import com.hundsun.ares.studio.engin.skeleton.DefaultSkeletonInput;
import com.hundsun.ares.studio.jres.database.utils.ProjectSettingUtil;
import com.hundsun.ares.studio.procdure.Procedure;
import com.hundsun.ares.studio.procdure.constants.IProcedureResType;
import com.hundsun.ares.studio.procdure.provider.ProcedureUI;
import com.hundsun.ares.studio.procedure.compiler.mysql.constant.IProcedureSkeletonFactoryConstantMySQL;
import com.hundsun.ares.studio.procedure.compiler.oracle.constant.IProcedureEngineContextConstantOracle;
import com.hundsun.ares.studio.procedure.compiler.oracle.constant.IProcedureSkeletonFactoryConstantOracle;
import com.hundsun.ares.studio.procedure.ui.util.ProcedureModuleGeneratorHelper;
import com.hundsun.ares.studio.ui.ARESUI;


public class GenProcedureModuleCode extends GenCresModuleCode {
	
	@Override
	public void genModuleCode(IARESModule module, Map<Object, Object> context,
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
						newPath += StringUtils.trim(FileNameHelper.legalFileOrDirName(moduleCName));//ģ�����������˵Ŀո�Ҫȥ��
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
					createModuleFile(monitor,module,newPath,charset,context);//���ɹ���
				}
			}
			
			//�ݹ�ģ��
			for (IARESModule subModule : subModules) {
				genModuleCode(subModule,context,isWithPath,isPathWithCname,new SubProgressMonitor(monitor, 10));
			}
			
			monitor.done();
		} catch (Exception e) {
			e.printStackTrace();
			monitor.setCanceled(true);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.cres.extend.ui.module.gencode.GenCresModuleCode#getContent(com.hundsun.ares.studio.core.IARESProject)
	 */
	@Override
	public Map<Object, Object> getContext(IARESProject project) {
		Map<Object, Object> context = new HashMap<Object, Object>();
		context.put(IProcedureEngineContextConstantOracle.auto_define_input_param, 
				ProcedureUI.getPlugin().getPreferenceStore().getBoolean(ProcedureUI.PER_AUTO_DEFINE_PROC_INPARAM));
		context.put(IProcedureEngineContextConstantOracle.not_define_connect_type, 
				ProcedureUI.getPlugin().getPreferenceStore().getBoolean(ProcedureUI.PER_NOT_DEFINE_CONNECT_TYPE));
		context.put(IProcedureEngineContextConstantOracle.return_error_info, 
				ProcedureUI.getPlugin().getPreferenceStore().getBoolean(ProcedureUI.PER_RETURN_ERROR_INFO));
		context.put(IProcedureEngineContextConstantOracle.gen_related_info, 
				ProcedureUI.getPlugin().getPreferenceStore().getBoolean(ProcedureUI.PER_GEN_RELATED_INFO));
		if(ProcedureUI.isStock2Procedure()) {
			context.put(IProcedureEngineContextConstantOracle.gen_begin_code, 
					isHeadCode());
			context.put(IProcedureEngineContextConstantOracle.gen_end_code, 
					isEndCode());
		}else {
			context.put(IProcedureEngineContextConstantOracle.gen_begin_code, false);
			context.put(IProcedureEngineContextConstantOracle.gen_end_code, false);
		}

		return context;
	}
	
	/**
	 * ���ɹ��̽ű��ļ�
	 * @param moduleRes 
	 * 			������Դ
	 * @param fileSuffixName
	 * 			�ļ���׺��
	 * @param monitor
	 * @param module
	 * @param path
	 * @param charset
	 * @throws Exception
	 */
	private void createProcedureFile(
			IARESResource[] moduleRes,String fileSuffixName,IProgressMonitor monitor,
			IARESModule module, String path,String charset,Map<Object, Object> context,boolean needSort) throws Exception{
		// ģ����û�й��̣�������sql�ļ�
		if(moduleRes == null || moduleRes.length < 1) {
			return;
		}
		
		List<IARESResource> modulesReslist = Arrays.asList(moduleRes);
		//����
		if(needSort){
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
		}
		
		//���ɴ���ʱ��������Ϣ�������Ϣ����
		Queue<IARESProblem> msgQueue = new ArrayDeque<IARESProblem>();
		
		//���ݿ���
		String dbName = "";
		
		String codeFileName = path + dbName + getModuleNameFromAutoCreateModuleName(module) + fileSuffixName;
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
			String id = procedure.getObjectId() + "&&" + procedure.getFullyQualifiedName();
			//���ɴ���ʱ��������Դ������Ϣ�������Ϣ����
			Queue<IARESProblem> msg = new ArrayDeque<IARESProblem>();
			if(!resCodeCache.containsKey(id)) {
				resCodeCache.put(id, genProcedureCode(iaresResource,procedure,msg,context));
			}
			msgQueue.addAll(msg);
			
			resCodeBuffer.append(resCodeCache.get(id));
			infos.add(new ModuleResourceStatisticsInfo(procedure.getObjectId(), procedure.getName(), procedure.getChineseName(), procedure.getDescription()));
			monitor.worked(1);
		}
		
		//����Ҫ�����ļ�ͷ�����Ϣ
		if(ProcedureUI.isStock2Procedure()) {
			codeBuffer.append("set define off;\r\n");
			codeBuffer.append("set feedback off;\r\n");
			codeBuffer.append("set serveroutput on;\r\n");
		}
		
		codeBuffer.append(ModuleGeneratorHelper.getResourceStatisticsInfo(infos));
		codeBuffer.append("\n");
		// �����޸ļ�¼
		codeBuffer.append(RevisionHistoryGenUtil.getAllRevisionHistoryInfo(ProcedureModuleGeneratorHelper.getProcedureModuleHistorys(module), "--"));
		codeBuffer.append("\n");
		
		
		codeBuffer.append(resCodeBuffer);
		codeBuffer.append("\n");

		//д���ļ�
		writeToFile(codeFileName, codeBuffer.toString(),charset);
		//================================== err
		String errLogFileName = "s_as_" + getModuleNameFromAutoCreateModuleName(module) + "proc.errorlog";
		createErrLog(msgQueue, errLogFileName,path,charset);
	}
	
	/**
	 * ���ɹ���squall�ļ� 
	 * @param monitor
	 * @param module
	 * @param path
	 * @throws Exception
	 */
	private void createModuleFile(IProgressMonitor monitor,
			IARESModule module, String path,String charset,Map<Object, Object> context) throws Exception{
		//��ģ����Դ
		IARESResource[] moduleRes = module.getARESResources(IProcedureResType.PROCEDURE);
		createProcedureFile(moduleRes, "_or.sql", monitor, module, path, charset,context,true);
		
		if(ProcedureUI.isStock2Procedure()) {//������ģ���������
			//��ģ��
			IARESModule[] subModules = module.getSubModules();
			List<IARESModule> subModuleLists = Arrays.asList(subModules);
			Collections.sort(subModuleLists,new Comparator<IARESModule>(){//����ģ������
				
				@Override
				public int compare(IARESModule o1, IARESModule o2) {
					
					return getModuleNameFromAutoCreateModuleName(o1).compareTo(getModuleNameFromAutoCreateModuleName(o2));
				}
				
			});
			List<IARESResource> allSubModules = new ArrayList<IARESResource>();
			for (IARESModule subModule : subModuleLists) {
				IARESResource[] ress = subModule.getARESResources(IProcedureResType.PROCEDURE);
				List<IARESResource> ressList = Arrays.asList(ress);
				//����
				Collections.sort(ressList,new Comparator<IARESResource>() {
					
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
				allSubModules.addAll(ressList);
			}
			if(allSubModules.size() > 0){
				//��ǰģ��û����ģ�飬����Ҫ����һ�������ļ�
				createProcedureFile(allSubModules.toArray(new IARESResource[0]), "_all_or.sql", monitor, module, path, charset,context,false);
			}
		}else {
			//��ģ��������Դ��Դ���ݹ���ģ��
			IARESResource[] moduleAllRes = module.getARESResources(IProcedureResType.PROCEDURE,true);
			if(moduleRes.length != moduleAllRes.length) {
				//��ǰģ��û����ģ�飬����Ҫ����һ�������ļ�
				createProcedureFile(moduleAllRes, "_all_or.sql", monitor, module, path, charset,context,true);
			}
		}
	}

	/**
	 * @param iaresResource
	 * @param msgQueue
	 * @return
	 */
	public StringBuffer genProcedureCode(IARESResource iaresResource,Procedure procedure,
			Queue<IARESProblem> msgQueue,Map<Object, Object> context) throws Exception{
		StringBuffer ret = new StringBuffer();
		///���Ϊ�������ţ��ԡ������ָ����Ҫ���ɶ��
		String objectID = procedure.getObjectId();
		if(StringUtils.isBlank(objectID)) {
			String message = String.format("�洢����[(%s)%s]�����Ϊ�ա�", procedure.getName(),procedure.getChineseName());
			IARESProblem problem = ARESProblem.createError();
			problem.setMessage(message);
			msgQueue.add(problem);
		}else {
			String databaseType = ProjectSettingUtil.getDatabaseType(iaresResource.getARESProject());
			if(objectID.contains(",")){
				String[] objIDs = objectID.split(",");
				for (String objID : objIDs) {
					procedure.setObjectId(objID.trim());
					context.put(IProcedureEngineContextConstantOracle.ResourceModel, procedure);
					if(databaseType.equalsIgnoreCase(ProjectSettingUtil.MYSQL)){
						ret.append(ResourceEngin.instance.generate(
								new DefaultSkeletonInput(
										IProcedureSkeletonFactoryConstantMySQL.SKELETONTYPE_CRES_PROCEDURE_MYSQL,
										iaresResource), context,msgQueue));
					}else{
						ret.append(ResourceEngin.instance.generate(
								new DefaultSkeletonInput(
										IProcedureSkeletonFactoryConstantOracle.SKELETONTYPE_CRES_PROCEDURE_ORACLE,
										iaresResource), context,msgQueue));
					}
					
					ret.append("\r\n");
				}
			}else{
				context.put(IProcedureEngineContextConstantOracle.ResourceModel, procedure);
				if(databaseType.equalsIgnoreCase(ProjectSettingUtil.MYSQL)){
					ret.append(ResourceEngin.instance.generate(
							new DefaultSkeletonInput(
									IProcedureSkeletonFactoryConstantMySQL.SKELETONTYPE_CRES_PROCEDURE_MYSQL,
									iaresResource), context,msgQueue));
				}else{
					ret.append(ResourceEngin.instance.generate(
							new DefaultSkeletonInput(
									IProcedureSkeletonFactoryConstantOracle.SKELETONTYPE_CRES_PROCEDURE_ORACLE,
									iaresResource), context,msgQueue));
				}
				ret.append("\r\n");
			}
		}

//		return CodeFormater.formatProc(ret);
		return ret;
	}

	@Override
	public boolean canGenCode(IARESModule module) {
		String rootName = module.getParent().getElementName();
		if(StringUtils.equals(rootName, ICresUIConstant.ATOM_ROOT_NAME) || StringUtils.equals(rootName, ICresUIConstant.PROCEDURE_ROOT_NAME)){
			return true;
		}
		
		return false;
	}

}
