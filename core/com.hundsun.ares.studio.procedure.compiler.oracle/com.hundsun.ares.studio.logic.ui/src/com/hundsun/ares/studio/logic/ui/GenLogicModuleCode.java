package com.hundsun.ares.studio.logic.ui;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.emf.common.util.EList;

import com.hundsun.ares.studio.atom.AtomService;
import com.hundsun.ares.studio.atom.constants.IAtomResType;
import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.util.ParamGroupUtil;
import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.ARESProblem;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESProblem;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.BasicResourceInfo;
import com.hundsun.ares.studio.core.model.ICommonModel;
import com.hundsun.ares.studio.cres.constant.ICresUIConstant;
import com.hundsun.ares.studio.cres.extend.cresextend.MoudleDepend;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.GenCresModuleCode;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.MakeFileBuilder;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleGeneratorHelper;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.RevisionHistoryGenUtil;
import com.hundsun.ares.studio.engin.format.CodeFormater;
import com.hundsun.ares.studio.engin.logic.ResourceEngin;
import com.hundsun.ares.studio.engin.skeleton.DefaultSkeletonInput;
import com.hundsun.ares.studio.logic.LogicFunction;
import com.hundsun.ares.studio.logic.LogicService;
import com.hundsun.ares.studio.logic.compiler.constant.ILogicEngineContextConstant;
import com.hundsun.ares.studio.logic.compiler.constant.ILogicSkeletonFactoryConstant;
import com.hundsun.ares.studio.logic.constants.ILogicResType;
import com.hundsun.ares.studio.logic.ui.util.LogicModuleGeneratorHelper;
import com.hundsun.ares.studio.ui.ARESUI;

public class GenLogicModuleCode extends GenCresModuleCode {

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
			IARESResource[] mRess = module.getARESResources();
			monitor.beginTask(newPath, mRess.length + subModules.length*10 + 1);
			if(mRess.length > 0) {//����Դ������
				if(mRess.length == 1 && StringUtils.equals(mRess[0].getType(), "module.xml")) {//�п�����ģ������
					System.out.println(mRess[0].getType());
				}else {
					createLSFunctionFile(monitor,module,newPath,charset);//�߼�����
					createLSServiceFile(monitor,module,newPath,charset);//�߼�����
					createMakefile(module,newPath,isWithPath,charset);//�����ļ�
					monitor.worked(1);
					generateLogicServiceTest(module,newPath,charset);//�߼���������ļ�
//					createFuncPasFile(monitor,module,newPath);//
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

	/**
	 * @param module
	 * @param newPath
	 */
	private void createMakefile(IARESModule module, String newPath,boolean isWithPath,String charset) throws Exception{
		
		//=================gcc
		String gccName = "s_ls_" + module.getShortName() + "flow.gcc";
		createMakeOrderFile(gccName,newPath,isWithPath,charset);
		//��Դ���ɴ���
		String gccFileName = newPath + gccName;
		//ģ������
		HashSet<String> gccIncludes = new HashSet<String>();
		EList<MoudleDepend> depends = ModuleGeneratorHelper.getCresMoudleExtendProperty(module).getDepends();
		for (MoudleDepend moudleDepend : depends) {
			//����moudleDepend�ҵ���Ӧ��ģ��
			for (IARESModule m : module.getARESProject().getModules()) {
				if(StringUtils.equals(m.getElementName(), moudleDepend.getModulePath())){
					// ԭ�ӵĲ���Ҫ���,ֻ����߼�ģ��
					if(m.getParent().getElementName().equals(ICresUIConstant.LOGIC_ROOT_NAME)){
						gccIncludes.add("s_ls_" + moudleDepend.getName() + "flow");//�����lib�⣬���������ʱ��Ӧ��Ϊso������flow���˶Ժ�����������Ҳ����˹�����
					}
				}
			}
		}
		// �Լ���������
		gccIncludes.remove("s_ls_" + module.getShortName() + "flow");
		StringBuffer gccCodeBuffer = new StringBuffer();
		MakeFileBuilder.writeLSGccMakeFile(module.getARESProject(), gccCodeBuffer, 
				"s_ls_" + module.getShortName(), "s_ls_" + module.getShortName() + "flow", 
				"s_ls_" + module.getShortName() + "func", gccIncludes.toArray(new String[0]));
		//д���ļ�
		writeToFile(gccFileName, gccCodeBuffer.toString(),charset);
		
//		//=================mvc
//		//��Դ���ɴ���
//		String mvcFileName = newPath + "s_ls_" + module.getShortName() + "flow.mvc";
//		HashSet<String> mvcDepends = new HashSet<String>();
//		// mvc ȡ��������
//		List<MoudleDepend> mvcDps = ModuleGeneratorHelper.getAllDepends(module);
//		for (MoudleDepend md : mvcDps) {
//			//����moudleDepend�ҵ���Ӧ��ģ��
//			for (IARESModule m : module.getARESProject().getModules()) {
//				if(StringUtils.equals(m.getElementName(), md.getModulePath())){
//					// ԭ�ӵĲ���Ҫ���,ֻ����߼�ģ��
//					if(m.getParent().getElementName().equals(LOGIC_ROOT_NAME)){
//						mvcDepends.add("s_ls_" + md.getName() + "func");
//					}
//				}
//			}
//		}
//		// �Լ���������
//		mvcDepends.remove("s_ls_" + module.getShortName() + "func");
//		
//		StringBuffer mvc = new StringBuffer();
//		MakeFileBuilder.writeLSMvcMakeFile(module.getARESProject(), mvc, 
//				"s_ls_" + module.getShortName(), "s_ls_" + module.getShortName() + "flow", 
//				"s_ls_" + module.getShortName() + "func", depends.toArray(new String[0]));
//		
//		writeToFile(mvcFileName, mvc.toString());
		
	}

	/**
	 * @param monitor
	 * @param module
	 * @param newPath
	 */
	private void createLSServiceFile(IProgressMonitor monitor,
			IARESModule module, String newPath,String charset) throws Exception{
		IARESResource[] moduleRes = module.getARESResources(ILogicResType.LOGIC_SERVICE);//�߼�����
		List<IARESResource> modulesReslist = Arrays.asList(moduleRes);
		//����
		Collections.sort(modulesReslist,new Comparator<IARESResource>() {
			
			@Override
			public int compare(IARESResource o1, IARESResource o2) {
				try {
					//��ʱ��ԴΪ�߼�����
					LogicService ls1 = o1.getInfo(LogicService.class);
					LogicService ls2 = o2.getInfo(LogicService.class);
					if(ls1.getObjectId() != null && ls1.getObjectId() != "" && ls2.getObjectId() != null && ls2.getObjectId() != ""){
						return Integer.valueOf(ls1.getObjectId()) - Integer.valueOf(ls2.getObjectId());
					}
					return 0;
					
				} catch (ARESModelException e) {
					e.printStackTrace();
					return 0;
				}
			}
		});
		
		//���ɴ���ʱ��������Ϣ�������Ϣ����
		Queue<IARESProblem> msgQueue = new ArrayDeque<IARESProblem>();
		
		//��Դ���ɴ���
		String codeFileName = newPath + "s_ls_" + module.getShortName() + "flow.cpp";
		StringBuffer codeBuffer = new StringBuffer();
		
		// д��ע��ͷ
		ModuleGeneratorHelper.writeCommentHeader(codeBuffer, 
				ModuleGeneratorHelper.getCresProjectExtendProperty(module).getHeadFile(), 
				codeFileName, module.getShortName(), new Date());
		codeBuffer.append("\n");
		// �����޸ļ�¼
		codeBuffer.append(RevisionHistoryGenUtil.getAllRevisionHistoryInfo(LogicModuleGeneratorHelper.getLogicModuleHistorys(module), "//"));
		codeBuffer.append("\n");
		
		// �������ģ���Ӣ��������ģ��Ϊ�ض�����
		List<String> includes = new ArrayList<String>();
		includes.add("s_ls_" + module.getShortName() + "func.h");
		ModuleGeneratorHelper.writeIncludeSection(codeBuffer, includes);
		codeBuffer.append("\n");

		//�����߼�������������
		ArrayList<BasicResourceInfo> services = new ArrayList<BasicResourceInfo>();
		for (IARESResource iaresResource : modulesReslist) {
			LogicService logicService = iaresResource.getInfo(LogicService.class);
			services.add(logicService);
			if(monitor.isCanceled()){
				return;
			}
			monitor.setTaskName("�߼�����"+iaresResource.getFullyQualifiedName());
			codeBuffer.append(genLogicServiceCode(iaresResource,msgQueue));
			monitor.worked(1);
		}
		
		codeBuffer.append("\n");
		//����м������
		ModuleGeneratorHelper.writeMiddlewareEnumerateMethod2(codeBuffer, services);
		ModuleGeneratorHelper.writeStartupMethods2(codeBuffer, LogicModuleGeneratorHelper.getLogicModuleLastVersion(module));
		
		//д���ļ�
		writeToFile(codeFileName, codeBuffer.toString(),charset);
		//================================== err
		String errLogFileName = "s_ls_" + module.getElementName() + "flow.errorlog";
		createErrLog(msgQueue, errLogFileName,newPath,charset);
	}

	/**
	 * @param monitor
	 * @param module
	 * @param newPath
	 */
	private void createLSFunctionFile(IProgressMonitor monitor,
			IARESModule module, String newPath,String charset) throws Exception{
		IARESResource[] moduleRes = module.getARESResources(ILogicResType.LOGIC_FUNCTION);//�߼�����
		List<IARESResource> modulesReslist = Arrays.asList(moduleRes);
		//����
		Collections.sort(modulesReslist,new Comparator<IARESResource>() {
			
			@Override
			public int compare(IARESResource o1, IARESResource o2) {
				try {
					//��ʱ��ԴΪ�߼�����
					LogicFunction lf1 = o1.getInfo(LogicFunction.class);
					LogicFunction lf2 = o2.getInfo(LogicFunction.class);
					if(lf1.getObjectId() != null && lf1.getObjectId() != "" && lf2.getObjectId() != null && lf2.getObjectId() != ""){
						return Integer.valueOf(lf1.getObjectId()) - Integer.valueOf(lf2.getObjectId());
					}
					return 0;
					
				} catch (ARESModelException e) {
					e.printStackTrace();
					return 0;
				}
			}
		});
		
		//���ɴ���ʱ��������Ϣ�������Ϣ����
		Queue<IARESProblem> msgQueue = new ArrayDeque<IARESProblem>();
		
		//��Դ���ɴ���
		String codeFileName = newPath + "s_ls_" + module.getShortName() + "func.cpp";
		StringBuffer codeBuffer = new StringBuffer();
		// д��ע��ͷ
		ModuleGeneratorHelper.writeCommentHeader(codeBuffer, 
				ModuleGeneratorHelper.getCresProjectExtendProperty(module).getHeadFile(), 
				codeFileName, module.getShortName(), new Date());
		codeBuffer.append("\n");
		// �����޸ļ�¼
		codeBuffer.append(RevisionHistoryGenUtil.getAllRevisionHistoryInfo(LogicModuleGeneratorHelper.getLogicModuleHistorys(module), "//"));
		codeBuffer.append("\n");
		//���ݺ�����������cppͷ�ļ�
		StringBuffer cppHeadBuffer = new StringBuffer();
		// �������ģ���Ӣ��������ģ��Ϊ�ض�����
		List<String> includes = new ArrayList<String>();
		includes.add("s_ls_" + module.getShortName() + "func.h");
		ModuleGeneratorHelper.writeIncludeSection(cppHeadBuffer, includes);
		codeBuffer.append("\n");
		//�����߼�������������
		for (IARESResource iaresResource : modulesReslist) {
			if(monitor.isCanceled()){
				return;
			}
			monitor.setTaskName("�߼�������"+iaresResource.getFullyQualifiedName());
			cppHeadBuffer.append(genLogicFunctionCode(iaresResource,msgQueue));
			monitor.worked(1);
		}
		codeBuffer.append(cppHeadBuffer);
		//д���ļ�
		writeToFile(codeFileName, codeBuffer.toString(),charset);
		
		//================================ͷ�ļ�
		String hppFileName = newPath + "s_ls_" + module.getShortName() + "func.h";
		List<String> headIncludes = new ArrayList<String>();
		headIncludes.add("hshead.h");

		//ģ������
		EList<MoudleDepend> depends = ModuleGeneratorHelper.getCresMoudleExtendProperty(module).getDepends();
		for (MoudleDepend moudleDepend : depends) {
			
			//����moudleDepend�ҵ���Ӧ��ģ��
			for (IARESModule m : module.getARESProject().getModules()) {
				if(StringUtils.equals(m.getElementName(), moudleDepend.getModulePath())){
					// ԭ�ӵĲ���Ҫ���,ֻ����߼�ģ��
					if(m.getParent().getElementName().equals(ICresUIConstant.LOGIC_ROOT_NAME)){
						headIncludes.add("s_ls_" + moudleDepend.getName() + "func.h");
					}
				}
			}
		}
		StringBuffer hppFileBuffer = ModuleGeneratorHelper.generateHeaderFile(cppHeadBuffer, "s_ls_" + module.getShortName() + "func.h", headIncludes.toArray(new String[includes.size()]), null);
		//д���ļ�
		writeToFile(hppFileName, hppFileBuffer.toString(),charset);
		
		//================================== err
		String errLogFileName = "s_ls_" + module.getElementName() + "func.errorlog";
		createErrLog(msgQueue, errLogFileName,newPath,charset);
	}
	
	/**
	 * �����߼���������ļ�
	 * @param path 
	 * @param module 
	 * @param charSet
	 */
	private void generateLogicServiceTest(IARESModule module, String path,String charset) throws Exception{
		String codeFileNname = path + "s_ls_" + module.getShortName() + ".xml";
		StringBuffer codeBuffer = new StringBuffer();
		
		codeBuffer.append("<?xml  version=\"1.0\" encoding=\"GBK\"?>");
		codeBuffer.append("\r\n");
		codeBuffer.append("<TEST_PACK note=\"ct���ù��ܺ���������԰�\">");
		codeBuffer.append("\r\n");
		codeBuffer.append("  <Test>");
		codeBuffer.append("\r\n");
		
		//�߼�����
		IARESResource[] moduleRes = module.getARESResources(ILogicResType.LOGIC_SERVICE);//�߼�����
		List<IARESResource> modulesReslist = Arrays.asList(moduleRes);
		//����
		Collections.sort(modulesReslist,new Comparator<IARESResource>() {
			
			@Override
			public int compare(IARESResource o1, IARESResource o2) {
				try {
					//��ʱ��ԴΪ�߼�����
					LogicService as1 = o1.getInfo(LogicService.class);
					LogicService as2 = o2.getInfo(LogicService.class);
					return Integer.valueOf(as1.getObjectId()) - Integer.valueOf(as2.getObjectId());
					
				} catch (ARESModelException e) {
					e.printStackTrace();
					return 0;
				}
			}
		});
		String inparams = "        <in name=\"%s\" value=\"%s\"/>";
		for (IARESResource res : modulesReslist) {
			LogicService service = res.getInfo(LogicService.class);
			if(!ParamGroupUtil.isContainObjectParameter(service.getInputParameters(),res.getARESProject()) && !ParamGroupUtil.isContainObjectParameter(service.getOutputParameters(),res.getARESProject())){
				codeBuffer.append("    <sub id=\"" + service.getObjectId() + "\" block=\"1\"  livetime=\"5000\" pri=\"8\" pack_ver=\"32\" note=\"" + service.getChineseName() + "\">");
				codeBuffer.append("\r\n");
				codeBuffer.append("      <route system=\"\" sub_system=\"\" branch=\"\" esb_name=\"\" esb_no=\"0\" neighbor=\"\" plugin=\"\"/>");
				codeBuffer.append("\r\n");
				codeBuffer.append("      <inparams note=\"" + service.getChineseName() + "\">");
				codeBuffer.append("\r\n");
				List<Parameter> allInputParametersWithNoObjectParameter = new ArrayList<Parameter>();
				ParamGroupUtil.parserParametersWithNoObjectParameter(service.getInputParameters(), allInputParametersWithNoObjectParameter, res.getARESProject());
				for(Parameter inParameter:allInputParametersWithNoObjectParameter){
					codeBuffer.append(String.format(inparams, inParameter.getId(), "")).append("\r\n");
					}
				List<Parameter> allOutParametersWithNoObjectParameter = new ArrayList<Parameter>();
				ParamGroupUtil.parserParametersWithNoObjectParameter(service.getOutputParameters(), allInputParametersWithNoObjectParameter, res.getARESProject());
				for (Parameter outParameter : allOutParametersWithNoObjectParameter) {
					if (StringUtils.indexOf(outParameter.getId(), "IO") >= 0) {
						codeBuffer.append(String.format(inparams, outParameter.getId(), "")).append("\r\n");
					}
				}
				
				codeBuffer.append("      </inparams>");
				codeBuffer.append("\r\n");
				codeBuffer.append("    </sub>");
				codeBuffer.append("\r\n");
			}
			
		}
		codeBuffer.append("  </Test>");
		codeBuffer.append("\r\n");
		codeBuffer.append("</TEST_PACK>");
		codeBuffer.append("\r\n");
		
		writeToFile(codeFileNname, codeBuffer.toString(),charset);
	}
	
	/**
	 * �����߼������������
	 * @param iaresResource
	 * @param msgQueue
	 * @return
	 */
	private StringBuffer genLogicServiceCode(IARESResource iaresResource,
			Queue<IARESProblem> msgQueue)  throws Exception{
		StringBuffer ret = new StringBuffer();
		Map<Object, Object> context = new HashMap<Object, Object>();
		LogicService logicService = iaresResource.getInfo(LogicService.class);
		// ���ΪD�ķ������ɴ���
		if(StringUtils.contains(logicService.getInterfaceFlag(), "D")){
			return ret;
		}
		
		///���Ϊ�������ţ��ԡ������ָ����Ҫ���ɶ��
		String objectID = logicService.getObjectId();
		if(StringUtils.isBlank(objectID)) {
			String message = String.format("�߼�����[(%s)%s]���ܺ�Ϊ�ա�", logicService.getName(),logicService.getChineseName());
			IARESProblem problem = ARESProblem.createError();
			problem.setMessage(message);
			msgQueue.add(problem);
		}else {
			if(objectID.contains(",")){
				String[] objIDs = objectID.split(",");
				for (String objID : objIDs) {
					logicService.setObjectId(objID.trim());
					context.put(ILogicEngineContextConstant.ResourceModel, logicService);
					ret.append(ResourceEngin.instance.generate(
							new DefaultSkeletonInput(
									ILogicSkeletonFactoryConstant.SKELETONTYPE_LOGIC_SERVICE,
									iaresResource), context,msgQueue));
					ret.append("\r\n");
				}
			}else{
				context.put(ILogicEngineContextConstant.ResourceModel, logicService);
				ret.append(ResourceEngin.instance.generate(
						new DefaultSkeletonInput(
								ILogicSkeletonFactoryConstant.SKELETONTYPE_LOGIC_SERVICE,
								iaresResource), context,msgQueue));
				ret.append("\r\n");
			}
		}
		
		return CodeFormater.formatCByForce(ret);
	}

	/**
	 * �߼�����
	 * @param iaresResource
	 * @param msgQueue
	 * @return
	 */
	private StringBuffer genLogicFunctionCode(IARESResource iaresResource,
			Queue<IARESProblem> msgQueue)  throws Exception{
		StringBuffer ret = new StringBuffer();
		Map<Object, Object> context = new HashMap<Object, Object>();
		LogicFunction logicFunction = iaresResource.getInfo(LogicFunction.class);
		// ���ΪD�ķ������ɴ���
		if(StringUtils.contains(logicFunction.getInterfaceFlag(), "D")){
			return ret;
		}
		
		///���Ϊ�������ţ��ԡ������ָ����Ҫ���ɶ��
		String objectID = logicFunction.getObjectId();
		if(StringUtils.isNotBlank(objectID) && objectID.contains(",")){
			String[] objIDs = objectID.split(",");
			for (String objID : objIDs) {
				logicFunction.setObjectId(objID.trim());
				context.put(ILogicEngineContextConstant.ResourceModel, logicFunction);
				ret.append(ResourceEngin.instance.generate(
						new DefaultSkeletonInput(
								ILogicSkeletonFactoryConstant.SKELETONTYPE_LOGIC_FUNCTION,
								iaresResource), context,msgQueue));
				ret.append("\r\n");
			}
		}else{
			context.put(ILogicEngineContextConstant.ResourceModel, logicFunction);
			ret.append(ResourceEngin.instance.generate(
					new DefaultSkeletonInput(
							ILogicSkeletonFactoryConstant.SKELETONTYPE_LOGIC_FUNCTION,
							iaresResource), context,msgQueue));
			ret.append("\r\n");
		}
		return CodeFormater.formatCByForce(ret);
	}

	@Override
	public boolean canGenCode(IARESModule module) {
		String rootName = module.getParent().getElementName();
		if(StringUtils.equals(rootName, ICresUIConstant.LOGIC_ROOT_NAME)){
			return true;
		}
		return false;
	}

}
