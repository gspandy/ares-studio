package com.hundsun.ares.studio.cres.text.assistant;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hundsun.ares.studio.core.IARESModule;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.cres.extend.cresextend.MoudleDepend;
import com.hundsun.ares.studio.cres.extend.ui.module.gencode.util.ModuleGeneratorHelper;

/**ģ�����Թ��ˣ�ֻ��ʾ����ģ��*/
public class ModuleAssistantFilter implements IAssistantFilter{
	
	IARESModule module;
	List<MoudleDepend> depends = new ArrayList<MoudleDepend>();
	
	public ModuleAssistantFilter(IARESModule module) {
		this.module = module;
	}
	
	/**
	 * ��ȡ������ģ��(��ģ��û������������һ��ģ���������)
	 */
	private void initDepends(IARESModule module) {
		try {
			depends.addAll(ModuleGeneratorHelper.getAllDepends(module));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(depends.isEmpty()){
			IARESModule parent = module.getParentModule();
			if(parent != null && parent.exists()){
				initDepends(parent);
			}
		}
	}

	@Override
	public boolean filter(Object obj) {
		if(obj instanceof IARESResource){
			IARESResource resource = (IARESResource)obj;
			//����ģ����Ҳ��Ӧ����ʾ��
			if(resource.getModule().equals(module)){
				return true;
			}
			//����ģ��
			for(MoudleDepend depend : depends){
				if(StringUtils.equals(depend.getModulePath(), resource.getModule().getElementName())){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void init() {
		depends.clear();
		initDepends(module);
	}
}
