/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.cres.text.assistant;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;

import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.cres.extend.cresextend.FileDefine;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleGeneratorHelper;
import com.hundsun.ares.studio.ui.console.ARESConsoleFactory;

/**
 * @author wangxh
 *
 */
public class CustomFunctionFactory {
	//����������keyΪ�ļ����·����listΪ�����б�
	private final static  Map<String, List<String>> funcMap = new HashMap<String, List<String>>();
	//struct��ʽ�ĺ�����keyΪ�ļ����·����value�е�keyΪstruct����ĵ�һ�䣨������struct IF2Packer : public IKnown����listΪ��Ӧstruct�µĺ����б�
	private final static Map<String, Map<String, List<String>>> structMap = new HashMap<String, Map<String, List<String>>>();
	/***
	 * ͷ�ļ������� һ����ʽ��ƥ��
	 * struct IF2Packer : public IKnown
	 * {
	 * .....
	 * } 
	 */
	private final static Pattern STRUCT_PATTERN = Pattern.compile(
			"^struct\\s+[a-zA-Z]+[a-zA-Z0-9_]*\\s*:\\s*public\\s+[a-zA-Z]+[a-zA-Z0-9_]*[\r\n]+\\{[\\w\\W]+\\};",Pattern.MULTILINE);
	
	private static Logger logger = ConsoleHelper.getLogger();
	
	public static CustomFunctionFactory eINSTANCE = new CustomFunctionFactory();
	
	private CustomFunctionFactory(){}
	
	//��ȡ���еĹ�������
	public List<String> getAllPublicFunctions(IARESProject project){
		List<String> funcs = new ArrayList<String>();
		try {
			EList<FileDefine> funcDefines = ModuleGeneratorHelper.getCresProjectExtendProperty(project).getFuncDefine();
			for(FileDefine define : funcDefines){
				if(define.isIsUsed()){
					funcs.addAll(getFunctions(define.getValue(), project));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return funcs;
		}
		return funcs;
	}
	
	//��ȡ����struct�µĺ�����ֻ����->����ʾ��
	public Map<String, List<String>> getAllStructMap(IARESProject project){
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		try {
			EList<FileDefine> funcDefines = ModuleGeneratorHelper.getCresProjectExtendProperty(project).getFuncDefine();
			for(FileDefine define : funcDefines){
				if(define.isIsUsed()){
					map.putAll(getStructMap(define.getValue(), project));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return map;
		}
		return map;
	}
	
	//��ȡָ���ļ��µ�struct�¶���ĺ���
	private Map<String,List<String>> getStructMap(String path, IARESProject project) {
		String key = getKey(path, project);
		if(!structMap.containsKey(key)){
			parseFile(path,project);
		}
		return structMap.get(key);
	}

	//��ȡָ���ļ��¶���Ĺ�������
	public List<String> getFunctions(String path,IARESProject project){
		String key = getKey(path, project);
		if(!funcMap.containsKey(key)){
			parseFile(path,project);
		}
		return funcMap.get(key);
	}

	/**
	 * @param path
	 * @param project 
	 * �����ļ�������Ĭ��ͷ�ļ�ֻ���Ƕ��幫����������struct�µĺ�����
	 */
	private void parseFile(String path, IARESProject project) {
		String text = getFileContent(path, project);
		
		List<String> structs = new ArrayList<String>();
		Matcher structMatcher = STRUCT_PATTERN.matcher(text);
		
		while(structMatcher.find()){
			structs.add(structMatcher.group());
		}
		
		String key = getKey(path, project);
		//��struct�򹫹������б�Ϊ�գ���֮��struct�µĺ����б�Ϊ��
		if(structs.size()>0){
			structMap.put(key,parseStruct(structs));
			funcMap.put(key, new ArrayList<String>());
		}else{
			structMap.put(key, new HashMap<String, List<String>>());
			funcMap.put(key,parseFunctions(text));
		}
		String message = String.format("ˢ��ͷ�ļ�%s�ﶨ��Ĺ��������ɹ���",path);
		logger.info(message);
	}
	
	//����struct�µ����к���
	private Map<String, List<String>> parseStruct(List<String>structs){
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		//��ȡ��һ�е�key
		Pattern pattern = Pattern.compile("^struct\\s+[a-zA-Z]+[a-zA-Z0-9_]*\\s*:\\s*public\\s+[a-zA-Z]+[a-zA-Z0-9_]*");
		for(String struct : structs){
			Matcher matcher = pattern.matcher(struct);
			if(matcher.find()){
				map.put(matcher.group(), parseFunctions(struct));
			}
		}
		return map;
	}
	
	
	private List<String> parseMacros(String text) {
		List<String> funcs = new ArrayList<String>();
		//�궨�� #define FEQ(xasx,csa) ssdsd����ʽ
		String regex = "^#define\\s+\\w+(\\(.*\\))?\\s+[\\(\\)a-zA-Z0-9_]+";

		Pattern pattern = Pattern.compile(regex,Pattern.MULTILINE);
		
		Matcher matcher = pattern.matcher(text);
		while(matcher.find()){
			String group = matcher.group();
			String regex1 = "^#define\\s+\\w+(\\(.*\\))?\\s+";
			Matcher matcher1 = Pattern.compile(regex1).matcher(group);
			if(matcher1.find()){
				funcs.add(StringUtils.replace(matcher1.group(), "#define", "").trim());
			}
		}
		return funcs;
	}

	private List<String> parseFunctions(String text){
		List<String> funcs = new ArrayList<String>();
		funcs.addAll(parseMacros(text));
		//���������򷵻����Ͷ��壬��Сд��ĸ��ͷ��������ԽӴ�Сд�����»���*�źͿո�
		String regex = "^\\s*[a-zA-Z]+[a-zA-Z0-9_\\*\\s]*";
		//���������壬��Сд��ͷ������ɽӴ�Сд�����»��ߣ��ٺ���ɽӿո�
		regex += "[a-zA-Z]+[a-zA-Z0-9_]*\\s*";
		//�������壬��С���ſ�ͷ����С���żӷֺŽ�β�������м�����Ƚϸ��ӣ��򵥴����ƥ������з���������ַ�
		regex+= "\\(.*\\)\\s*";
		Pattern pattern = Pattern.compile(regex,Pattern.MULTILINE);
		
		Matcher matcher = pattern.matcher(text);
		while(matcher.find()){
			funcs.add(getDispalyString(matcher.group()).trim());
		}
		
		return funcs;
	
	}
	/**
	 * @param line
	 * @return
	 */
	private String getDispalyString(String line) {
		//�������Ӳ���ƥ��
		Pattern pattern = Pattern.compile("\\b\\s*[a-zA-Z]+[a-zA-Z0-9_]*\\s*\\(.*\\)");
		Matcher matcher = pattern.matcher(line);
		if(matcher.find()){
			String pre = matcher.group();
			int preIndex = matcher.start();
			return pre + " : " + line.substring(0, preIndex).trim();
		}
		return line;
	}
	
	//��ȡ�ļ�����
	private String getFileContent(String path,IARESProject project){
		String text = "";
		FileInputStream in = null;
		try {
			IFile file = project.getProject().getFile(path);
			if(file.exists()){
				in = new FileInputStream(file.getLocation().toFile());
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String line = reader.readLine();
				while(line != null){
					text += line;
					text += "\r\n";
					line = reader.readLine();
				}
			}else{
				ARESConsoleFactory.openARESConsole();
				String message = String.format("��Ŀ�����ж���Ĺ�������ͷ�ļ�%s�����ڣ�",path);
				logger.info(message);
				throw new Exception(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(in);
		}
		return text;
	}

	private String getKey(String path,IARESProject project){
		return project.getElementName() + "/" + path;
	}
	
	public void removeFuncs(String path,IARESProject project){
		String key = getKey(path, project);
		funcMap.remove(key);
		structMap.remove(key);
	}
	
	public void clear(){
		funcMap.clear();
		structMap.clear();
	}
}
