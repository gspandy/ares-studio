/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.internal.useroption;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.hundsun.ares.studio.jres.script.internal.useroption.control.ControlManager;
import com.hundsun.ares.studio.jres.script.internal.useroption.control.IUserOptionControlProvider;

/**
 * @author lvgao
 *
 */
public class UserOptionConfigReader {

	private static Logger logger = Logger.getLogger(UserOptionConfigReader.class);
	////////////////////////////////////XML�������/////////////////////////////////////////////////////////
	public static final String ROOT = "useroption";
	public static final String ID = "id";
	public static final String TEXT = "text";
	public static final String VALUE = "value";
	public static final String MODULE_ROOT = "module_root";
	public static final String DEFAULT_VALUE = "default_value";
	public static final String TYPE = "type";
	public static final String GROUP = "group";
	public static final String ITEM = "item";
	public static final String MODULE = "module";

	public UserOptionRoot read(InputStream is)throws Exception{
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(is);
		} catch (DocumentException e) {
			logger.error(e);
			return null;
		}
		
		Element root =  document == null ? null : document.getRootElement();
		if(null == root || !StringUtils.equals(ROOT, root.getName())){
			throw new Exception(String.format("���ø��ڵ����ӦΪ����Ϊ[%s]�ĸ��ڵ㡣", ROOT));
		}
		
		UserOptionRoot option = new UserOptionRoot();
		
		for(Object item: root.elements()){
			Element itemElment = (Element)item;
			String name =  itemElment.getName();
			
			if(StringUtils.equals(name, MODULE)){
				readModuleControl(itemElment, option);
			}
			
			if(StringUtils.equals(name, ITEM)){
				readItemControl(itemElment, option);
			}
			
			if(StringUtils.equals(name, GROUP)){
				readGroupControl(itemElment, option);
			}
			
		}
		
		return option;
	}
	
	
	private void readGroupControl(Element itemElment,IControlContainer container){
		IControl contrl = createControl(IControl.TYPE_GROUP);
		if(null == contrl){
			return;
		}
		
		/**
		 * ������߼�����˳���
		 * ��Ϊ��radio�Ĺ�ϵ������group�����ȳ�ʼ������Ŀؼ�
		 * Ȼ��������Ĭ��ֵ��
		 * 
		 */
		for(Object item: itemElment.elements()){
			Element subElment = (Element)item;
			String name =  subElment.getName();
			
			if(StringUtils.equals(name, MODULE)){
				readModuleControl(subElment, (IControlContainer)contrl);
			}
			
			if(StringUtils.equals(name, ITEM)){
				readItemControl(subElment, (IControlContainer)contrl);
			}
			
			if(StringUtils.equals(name, GROUP)){
				readGroupControl(subElment, (IControlContainer)contrl);
			}
		}
		
		contrl.setType(IControl.TYPE_GROUP);
		contrl.setID(getAttributeValue(itemElment, ID, ""));
		contrl.setText(getAttributeValue(itemElment, TEXT, ""));
		contrl.setValue(getAttributeValue(itemElment, VALUE, ""));
		contrl.setDefaultValue(getAttributeValue(itemElment, DEFAULT_VALUE, ""));
		container.addChildren(contrl);
	}
	
	private void readModuleControl(Element itemElment,IControlContainer container){
		String itemType = getAttributeValue(itemElment, TYPE, "");
		//�ؼ����
		IControl contrl = createControl(IControl.TYPE_MODULE);
		if(null == contrl){
			return;
		}
		contrl.setType(IControl.TYPE_MODULE);
		contrl.setControlType(itemType);
		contrl.setID(getAttributeValue(itemElment, ID, ""));
		contrl.setText(getAttributeValue(itemElment, TEXT, ""));
		contrl.setValue(getAttributeValue(itemElment, VALUE, ""));
		contrl.setModuleRoot(getAttributeValue(itemElment, MODULE_ROOT, ""));
		contrl.setDefaultValue(getAttributeValue(itemElment, DEFAULT_VALUE, ""));
		container.addChildren(contrl);
	}
	
	private void readItemControl(Element itemElment,IControlContainer container){
		String type = getAttributeValue(itemElment, TYPE, "");
		//�ؼ����
		IControl contrl = createControl(type);
		if(null == contrl){
			return;
		}
		/***
		 * �������������˳���
		 * setValue������setDefaultValueǰ��
		 * ����combo��setValueʱ��ʼ��ѡ�����Ĭ��ֵʱҪУ���Ƿ���ѡ����
		 */
		contrl.setType(type);
		contrl.setID(getAttributeValue(itemElment, ID, ""));
		contrl.setText(getAttributeValue(itemElment, TEXT, ""));
		contrl.setValue(getAttributeValue(itemElment, VALUE, ""));
		contrl.setDefaultValue(getAttributeValue(itemElment, DEFAULT_VALUE, ""));
		container.addChildren(contrl);
	}
	
	/**
	 * ��ȡ���Ե�ֵ
	 * @param itemElment
	 * @param key
	 * @param defalutValue
	 * @return
	 */
	public String getAttributeValue(Element itemElment,String key,String defalutValue){
		 Object attr =  itemElment.attribute(key);
		 if(null != attr){
			 return ((Attribute)attr).getValue();
		 }
		return defalutValue;
	}
	
	
	/**
	 * ������Ӧ�Ŀؼ�
	 * @param type
	 * @return
	 */
	public static IControl createControl(String type){
		if (StringUtils.equals(IControl.TYPE_TEXT, type)) {
			return new UserOptionControlText();
		}else if (StringUtils.equals(IControl.TYPE_COMBO, type)) {
			return new UserOptionControlCombo();
		}else if (StringUtils.equals(IControl.TYPE_CHECK, type)) {
			return new UserOptionControlCheck();
		}else if (StringUtils.equals(IControl.TYPE_RADIO, type)) {
			return new UserOptionControl();
		}else if (StringUtils.equals(IControl.TYPE_GROUP, type)) {
			return new UserOptionControlGroup();
		}else if (StringUtils.equals(IControl.TYPE_MODULE, type)) {
			return new UserOptionControlTree();
		} else {
			// ���������̶�����֮�⣬����������Ϊ����ͨ����չ����չ��
			IUserOptionControlProvider provider = ControlManager.getInstance().getTypeProvider(type);
			if (provider != null) {
				return provider.createControl();
			}
		}
		
		return null;
	}
	
}
