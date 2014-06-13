/**
* <p>Copyright: Copyright (c) 2014</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.cres.script.impl;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESModuleRoot;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.GenCresModuleCodeManager;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.IGenCresModuleCode;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.MakeFileGenerator;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleGeneratorHelper;
import com.hundsun.ares.studio.cres.ui.action.GenModuleCodeType;
import com.hundsun.ares.studio.jres.script.api.wrap.IGenCodeScriptWrap;

/**
 * ԭ����,�߼�,�������ɴ���ű�API��װ
 * @author liaogc
 *
 */
public class GenCodeScriptWrap implements IGenCodeScriptWrap{
	private int resType; /*����:1:ԭ��,2ҵ���߼�*,3����*/
	private int genType;/*genType:1:������Ŀ¼,2:��Ӣ��Ŀ¼,3:����Ŀ¼*/
	private IARESProject project;
	private Set<IARESModule> modules = new HashSet<IARESModule>();
	
	private static Logger logger = Logger.getLogger(GenCodeScriptWrap.class);
	static final Logger console = ConsoleHelper.getLogger();
	protected boolean isHeadCode = false;
	protected boolean isEndCode = false;

		
	public GenCodeScriptWrap( IARESProject project){
			this.project = project;
	}

	@Override
	public void genModuleCode(int resType, int genType) {
		this.resType = resType;
		this.genType = genType;
		try {
			init();
		} catch (ARESModelException e1) {
			console.error(e1.getMessage());
		}

		HashSet<IGenCresModuleCode> genModuleCodeSet = GenCresModuleCodeManager.getInstance().getSet();
		//�ڲ���Ŀ¼���ɵ�����£�����������ϵȷ������˳��
		if(!isGenCodeWithPath()){
			String newPath = ModuleGeneratorHelper.getModuleGenCodePath(project);
			MakeFileGenerator generator = new MakeFileGenerator(modules);
			generator.createMakeFileWithoutFolder(newPath);
		}
		console.info("ģ�����ɿ�ʼ");
		for (IARESModule module : modules) {
			long l1 = System.currentTimeMillis();
			StringBuffer errLog = new StringBuffer();
			GenCresModuleCodeManager.getInstance();
			for (IGenCresModuleCode genModuleCode : genModuleCodeSet) {
				if(genModuleCode.canGenCode(module)) {
					genModuleCode.setErrLog(errLog);
					genModuleCode.setIsHeadCode(isHeadCode);
					genModuleCode.setIsEndCode(isEndCode);
					genModuleCode.genModuleCode(module, genModuleCode.getContext(project), 
							isGenCodeWithPath(), isGenCodeWithCNamePath(), new NullProgressMonitor());
				}
			} 
			openErrorLog(errLog, true);
			long l2 = System.currentTimeMillis();
			console.info("ģ��[" + module.getElementName() +"]����������ʱ��" + (l2-l1) + " ms");
		}
		
		for (IGenCresModuleCode genModuleCode : genModuleCodeSet) {
			genModuleCode.clearCache();
		}
	
	}
	
	/**
	 * ���ݲ�ͬ����Դ���ͳ�ʼ����Ӧ���͵�ģ����Ϣ
	 * @throws ARESModelException
	 */
	private void init() throws ARESModelException {
		if (resType == 1) {//ԭ��
			IARESModuleRoot root = this.project.getModuleRoot("atom");
			if (root != null) {
				for (IARESModule module : root.getModules()) {
					if(StringUtils.isNotBlank(module.getElementName())){
						this.modules.add(module);
					}
					
				}
			}

		} else if (resType == 2) {//�߼�

			IARESModuleRoot root = this.project.getModuleRoot("logic");
			if (root != null) {
				for (IARESModule module : root.getModules()) {
					if(StringUtils.isNotBlank(module.getElementName())){
						this.modules.add(module);
					}
				}
			}

		} else if (resType == 3) {//����
			IARESModuleRoot root = this.project.getModuleRoot("procedure");
			if (root != null) {
				for (IARESModule module : root.getModules()) {
					this.modules.add(module);
				}
			}

		}
	}
	

	/**
	 * �򿪴�����־
	 * @param errLog
	 * 			������Ϣ
	 * @param openErrorLog
	 * 			�Ƿ�򿪴�����־�ļ�
	 */
	protected void openErrorLog(StringBuffer errLog,boolean openErrorLog) {
		
		// ������־Ϊ�յĻ�����д�ļ���
		if(!errLog.toString().trim().equals("")) {
			errLog.insert(0, "������־��\n");
			String fileName = "genCodeReport" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".txt";
			final IFile fReport = project.getProject().getFile(fileName);
			try {
				if (!fReport.exists()) {
					fReport.create(
						new ByteArrayInputStream(errLog.toString().getBytes(project.getProject().getDefaultCharset())), true,
						new NullProgressMonitor());
				} else {
					fReport.setContents(new ByteArrayInputStream(errLog.toString().getBytes(
						project.getProject().getDefaultCharset())), true, false, new NullProgressMonitor());
				}
				if(openErrorLog){
					//���г�����
				}
			} catch (CoreException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean isGenCodeWithPath() {
		if (GenModuleCodeType.NODIR_GENMODULE.getType() != genType) {
			return true;
		}
		return false;
	}

	private boolean isGenCodeWithCNamePath() {
		if (GenModuleCodeType.CH_DIR_GENMODULE.getType() == genType) {
			return true;
		}
		return false;
	}

}
