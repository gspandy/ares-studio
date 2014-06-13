/**
 * Դ�������ƣ�MakeFileGenerator.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�ARES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.cres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.cres.extend.ui.module.gencode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.emf.common.util.EList;

import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.cres.extend.cresextend.MoudleDepend;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleGeneratorHelper;
import com.hundsun.ares.studio.ui.ARESUI;

/**
 * @author sundl
 *
 */
public class MakeFileGenerator {
	
	private static Logger logger = Logger.getLogger(MakeFileGenerator.class);
	static final Logger console = ConsoleHelper.getLogger();

	private Map<String,String> cmdMap = new HashMap<String,String>();//������ע�����ǰ����ա�
	private Map<String,List<String>> callList = new HashMap<String,List<String>>();//������飬ע�����ǰ����ա�

	private Set<IARESModule> modules = new HashSet<IARESModule>();
	
	private String charset;
	
	public MakeFileGenerator(Set<IARESModule> modules) {
		this.modules = modules;
		IPreferencesService service = Platform.getPreferencesService();
		charset = service.getString(ARESUI.PLUGIN_ID, ARESUI.PRE_GENERATE_CHARSET, StringUtils.EMPTY, null);
	}
	
	public void createMakeFileWithoutFolder(String path){
		cmdMap.clear();//�������������գ��ڶ������ɣ���ȫ��Ϊ��
		callList.clear();//���ü�¼��ա�
		StringBuffer make_all_buffer = new StringBuffer();
		Iterator<IARESModule> ite = modules.iterator();
		while(ite.hasNext()){
			IARESModule module = ite.next();
			make_all_buffer.append(getModuleMakeFileCmdStr(module));
		}
		String fileName = path + "makeall";
		
		//д���ļ�
		writeToFile(fileName, make_all_buffer.toString(),charset);
		
		String fileName2 = path + "makeclean";
		
		String  make_all_cl = make_all_buffer.toString().replaceAll("flow.gcc ORA_VER=10", "flow.gcc ORA_VER=10 clean");
		//д���ļ�
		writeToFile(fileName2, make_all_cl,charset);
	}
	
	/**
	 * ���ַ�����д��ָ���ļ�
	 * @param fileName
	 * @param contant
	 */
	protected void writeToFile(String fileName,String contant,String charset) {
		charset = StringUtils.defaultString(charset, "GB2312");
		try {
			FileUtils.writeStringToFile(new File(fileName), contant, charset);
		} catch (IOException e) {
			logger.error(e);
		}
	}
	
	/**
	 * �ݹ����
	 * @param module
	 * @return
	 */
	private StringBuffer getModuleMakeFileCmdStr(IARESModule module){
		StringBuffer cmdStr = new StringBuffer();
		
		IARESModule[] submodules = null;
		try{
			submodules = module.getSubModules();//�����Ѿ��õ�������ģ�飬����Ƕ�ײ�νṹ�е���ģ��
			if(submodules != null && submodules.length > 0){
				for(int i = 0;i < submodules.length;i++){
					cmdStr.append(getDependencyMakeFileCmdStr(submodules[i]));//�����Ѿ���ȡ��ȫ����ģ�飬�����ﲻ���ٵݹ飬������ظ�
				}
			}
			cmdStr.append(getDependencyMakeFileCmdStr(module));//��ǰģ�鼰������ģ��ı�������
		}catch(Exception ex){
			
		}
		
		return cmdStr;
	}
	
	private StringBuffer getDependencyMakeFileCmdStr(IARESModule module) throws Exception{
		StringBuffer cmdStr = new StringBuffer();
		EList<MoudleDepend> depends = ModuleGeneratorHelper.getCresMoudleExtendProperty(module).getDepends();//ģ����չ����
		for (MoudleDepend moduleDepend : depends) {
			IARESModule dm = getModuleByPath(moduleDepend.getModulePath());//ֻ������ģ���ڵ�ǰ��Ҫ���ɵ�ģ���б��У�����Ҫ��ӱ�������
			if(dm != null){//���LSģ������ASģ����ͬ��������ֻ�ж����ƣ��ʻ�������
				if(!cmdMap.containsKey(dm.getElementName())){//Ϊ��ֹ��ѭ������Ѿ���ӣ���ֱ����������
					if(!callList.containsKey(dm.getElementName())){
						if(callList.get(dm.getElementName()) != null){
							callList.get(dm.getElementName()).add(module.getElementName());
						}else{
							List<String> callInfo = new ArrayList<String>();
							callInfo.add(module.getElementName());
							callList.put(dm.getElementName(),callInfo);//��ǰģ���Ѿ��ݹ���ã���¼���ж��Ƿ��ظ�����
						}
						StringBuffer cmd = getDependencyMakeFileCmdStr(dm);
						if((cmd.toString() != "") &&(!cmdMap.containsValue(cmd))){
							cmdStr.append(cmd);//Ҫ���������ģ��ı�������
							cmdMap.put(dm.getElementName(), cmd.toString());
						}else if((cmd.toString() != "") && cmdMap.containsValue(cmd)){//�Ѿ������ͬ�ı������ֻ���ڲ�ͬ·���£�����ͬ��ģ����ʱ���ſ��ܳ���
							
							console.warn(dm.getElementName() + "ģ�����ظ����޷����GCC���");
						}
					}else{
						List<String> callInfo = callList.get(dm.getElementName());
						StringBuffer callbuff = new StringBuffer();
						for(int i_call = 0;i_call < callInfo.size();i_call++){
							callbuff.append(callInfo.get(i_call) + "->" + dm.getElementName() + ";");
							List<String> becall = callList.get(callInfo.get(i_call));
							for(int i_becall = 0;i_becall < becall.size();i_becall++){
								callbuff.append(becall.get(i_becall) + "->" + callInfo.get(i_call) + ";");
							}
						}
						console.warn(dm.getElementName() + "ģ�����ѭ�����ã���������·����" + callbuff + "��");
					}
					
				}
			}
		}
		if(!cmdMap.containsKey(module.getElementName())){//���ظ����
			
			String cmd = getCmdStr(module);
			if((cmd != "") && (!cmdMap.containsValue(cmd))){
				cmdStr.append(cmd);//���������ģ��ı��������ӵ�ǰģ��ı�������
				cmdMap.put(module.getElementName(), cmd);
			}else if((cmd != "") && (cmdMap.containsValue(cmd))){//�Ѿ������ͬ�ı������ֻ���ڲ�ͬ·���£�����ͬ��ģ����ʱ���ſ��ܳ���
				String overlap_path = getOverLapKey(cmd);//�����ظ���ģ����
				console.warn(module.getElementName() + "ģ������" + overlap_path + "�ظ����޷����GCC���");
			}
		}
		return cmdStr;
	}
	
	private String getCmdStr(IARESModule module){
		//��ģ����Դ
		IARESResource[] mRess = module.getARESResources();
		if(mRess.length > 0) {//����Դ������
			if(mRess.length == 1 && StringUtils.equals(mRess[0].getType(), "module.xml")) {//�п�����ģ������
				//����Դ��������
			}else {
				String root_type = module.getRoot().getType();
				if(root_type.equalsIgnoreCase("com.hundsun.ares.studio.atom.resources.atomroot")){
					return "make -f s_as_" + module.getShortName() + "flow.gcc ORA_VER=10\n";//ע��������Linux�µĻ��з���һ��Ҫ��\n��������������makeall��������				
				}else if(root_type.equalsIgnoreCase("com.hundsun.ares.studio.logic.resources.logicroot")){
					return "make -f s_ls_" + module.getShortName() + "flow.gcc ORA_VER=10\n";//ע��������Linux�µĻ��з���һ��Ҫ��\n��������������makeall��������
				}else{
					//�����Ͳ��账��
				}
			}
		}
		return "";
	}
	
	private IARESModule getModuleByPath(String path){
		Iterator<IARESModule> ite = this.modules.iterator();
		while(ite.hasNext()){
			IARESModule module = ite.next();
			if(StringUtils.equals(module.getElementName(), path)){
				return module;//��ǰ�б��д��ڣ���ֱ�ӷ���
			}
			IARESModule[] submodules = null;
			try{
				submodules = module.getSubModules();//��ǰģ���б��в����ڣ�������ģ���в���,�ѻ�ȡ����Ƕ����ģ��
				if(submodules != null && submodules.length > 0){
					for(int j =0;j < submodules.length;j++){
						IARESModule submodule = submodules[j];
						if(StringUtils.equals(submodule.getElementName(), path)){
							return submodule;
						}
					}
				}
			}catch(Exception ex){
				
			}			
		}
		return null;//�Ҳ������ؿ�
	}
	
	private String getOverLapKey(String cmd){
		Set<String> keys = cmdMap.keySet();
		Iterator<String> ite = keys.iterator();
		while(ite.hasNext()){
			String key = ite.next();
			String overlap_cmd = cmdMap.get(key);
			if(StringUtils.equals(overlap_cmd, cmd)){
				return key;
			}
		}
		return "";
	}
	
}
