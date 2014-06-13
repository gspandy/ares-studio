/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.logic.ui.assistant;

import com.hundsun.ares.studio.atom.constants.IAtomRefType;
import com.hundsun.ares.studio.atom.ui.assistant.InternalAssistantLoader;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.cres.text.assistant.ConstantAssistantLoader;
import com.hundsun.ares.studio.cres.text.assistant.IAssistantFilter;
import com.hundsun.ares.studio.cres.text.assistant.IAssistantLoader;
import com.hundsun.ares.studio.cres.text.assistant.KeywordsAssistantLoader;
import com.hundsun.ares.studio.cres.text.assistant.ModuleAssistantFilter;
import com.hundsun.ares.studio.cres.text.assistant.ObjectParamAssistantLoader;
import com.hundsun.ares.studio.cres.text.assistant.PublicFuncAssistantLoader;
import com.hundsun.ares.studio.cres.text.assistant.ResourceAssistantLoader;
import com.hundsun.ares.studio.cres.text.assistant.StdFieldAssistantLoader;
import com.hundsun.ares.studio.cres.text.assistant.StructAssistantLoader;
import com.hundsun.ares.studio.cres.text.assistant.TextAssistant;
import com.hundsun.ares.studio.cres.text.assistant.UserMacroAssistantFilter;
import com.hundsun.ares.studio.cres.text.assistant.UserMacroAssistantLoader;
import com.hundsun.ares.studio.logic.constants.ILogicRefType;

/**
 * @author wangxh
 * �߼�����/����α����������ʾ
 */
public class LogicTextAssistant extends TextAssistant {

	public LogicTextAssistant(IARESResource resource) {
		super(resource);
	}
	
	@Override
	protected void createAssistantLoader() {
		//���б�׼�ֶκͱ���Դ�ڲ������ķѱ�׼�ֶ�
		IAssistantLoader loader = new StdFieldAssistantLoader(getResource());
		getLoaders().add(loader);
		
		loader = new InternalAssistantLoader(getResource());
		getLoaders().add(loader);
		//AS
		loader = new ResourceAssistantLoader(getResource(), IAtomRefType.ATOM_SERVICE_CNAME);
		getLoaders().add(loader);
		//LF
		loader = new ResourceAssistantLoader(getResource(), ILogicRefType.LOGIC_FUNCTION_CNAME);
		//ģ����������
		IAssistantFilter filter = new ModuleAssistantFilter(getResource().getModule());
		loader.setFilter(filter);
		getLoaders().add(loader);
		
		loader = new UserMacroAssistantLoader(getResource());
		loader.setFilter(new UserMacroAssistantFilter(UserMacroAssistantFilter.LOGIC_TYPE));
		getLoaders().add(loader);
		
		//��������
		loader = new PublicFuncAssistantLoader(getResource());
		getLoaders().add(loader);
		//��������
		loader = new StructAssistantLoader(getResource());
		getLoaders().add(loader);
		
		//��������������
		loader = new ObjectParamAssistantLoader(getResource());
		getLoaders().add(loader);
		
		//�ؼ�����ʾ����lpResultSet��
		loader = new KeywordsAssistantLoader();
		getLoaders().add(loader);
		
		//������ʾ
		loader = new ConstantAssistantLoader(getResource());
		getLoaders().add(loader);
	}

}
