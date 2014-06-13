package com.hundsun.ares.studio.cres.text.assistant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import com.hundsun.ares.studio.core.IARESResource;

public class StructAssistantLoader extends AbstractAssistantLoader {
	
	IARESResource resource;
	
	public StructAssistantLoader(IARESResource resource){
		this.resource = resource;
	}
	
	private final static String PREFIX = "->";
	
	@Override
	public List<String> loadAssitant(String text, IDocument doc, int offset) {
		Map<String, List<String>> map = CustomFunctionFactory.eINSTANCE.getAllStructMap(resource.getARESProject());
		List<String> allproposals = new ArrayList<String>();
		try {
			int preOffset = offset-text.length()-2;
			if(preOffset >= 0){
				String pre = doc.get(preOffset, 2);
				//�鿴ǰ�������ַ��Ƿ���->
				if(pre.equals(PREFIX)){
					for(Entry<String, List<String>> entry : map.entrySet()){
						allproposals.addAll(entry.getValue());
					}
				}
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return allproposals;
	}
	
	@Override
	public String getReplacement(String display) {
		//��Сд��ͷ�����ſ����д�Сд��ĸ�����֡��»��ߣ����ſ����ɿո���(��β
		Pattern pattern = Pattern.compile("\\b[a-zA-Z]+[a-zA-Z0-9_]*\\s*\\(");
		Matcher matcher = pattern.matcher(display);
		if(matcher.find()){
			String replace = matcher.group();
			List<String> params = getParams(display);
			for(int index=0; index<params.size(); index++){
				String param = params.get(index);
				if(index>0){
					replace += ",";
				}
				replace += param;
			}
			replace += ")";
			return replace;
		}
		return display;
	}

	/**
	 * @param display
	 * @return
	 */
	private List<String> getParams(String display) {
		List<String> list = new ArrayList<String>();
		//��Сд��ͷ���ɽӴ�Сд�����»��ߣ�֮������пո������ַ���������
		Pattern pattern = Pattern.compile("[a-zA-Z]+[a-zA-Z0-9_]*\\s*$");
		String paramDefine = display.substring(display.indexOf("(")+1, display.indexOf(")")).trim();
		if(paramDefine.equalsIgnoreCase("void")){
			//����Ϊvoid
			return list;
		}
		for(String tmp : paramDefine.split(",")){
			//�еȺ����ʾ��Ĭ��ֵ��ֱ�Ӻ��Ըò���
			if(!tmp.contains("=")){
				Matcher matcher = pattern.matcher(tmp);
				if(matcher.find()){
					list.add(matcher.group());
				}
			}
		}
		return list;
	}

}
