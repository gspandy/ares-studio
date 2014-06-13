/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.model.extendable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Text;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultText;
import org.eclipse.core.runtime.IConfigurationElement;

import com.hundsun.ares.studio.core.ARESCore;
import com.hundsun.ares.studio.core.model.converter.IExtendModelConverter;
import com.hundsun.ares.studio.core.util.ExtendPointUtil;


/**
 * ��չ�ڵ�洢�ṹ����
 *<extendMap>
 *	<entity>
 *		<key>realkey</key>
 *		<value>valueElement</value>
 *	</entity>
 *	<entity>
 *		<key>realkey</key>
 *		<value>valueElement</value>
 *	</entity>
 *	......
 *</extendMap>
 * @author maxh
 * 
 * 
 * 
 * ��չģ�Ͷ�д���߷���
 */

public class ExtendModelConverterManager {
	
	//����
	Map<ExtendModelMapKey, IExtendModelConverter> map = new HashMap<ExtendModelMapKey, IExtendModelConverter>();
	
	//��չģ����XML�еĽڵ�ؼ���
	public static final String MAP_STRING = "extendMap";
	
	public static final String ENTITY_STRING = "entity";
	public static final String KEY_STRING = "key";
	public static final String VALUE_STRING = "value";
	//��չģ�Ͷ�д����չ��
	public static final String EXTEND_POINT_ID = ARESCore.PLUGIN_ID + ".ares_extend_model_serializer";
	
	//��չģ�Ͷ�д�������ֶ�
	public static final String POINT_TARGET_CLASS_STRING = "targetClassName";
	
	public static final String POINT_KEY_STRING = "key";
	
	//�û��Զ���ҳ��ı������
	public static final String USER_CONFIG_PAGE_STRING = "userConfigPageString";

	private ExtendModelConverterManager() {}
	
	static ExtendModelConverterManager extendModelConverterManager;
	
	public static ExtendModelConverterManager getDefault(){
		if(extendModelConverterManager == null){
			extendModelConverterManager = new ExtendModelConverterManager();
		}
		return extendModelConverterManager;
	}
	
	//����������key����չ�����ҵ���д�� �л���
	IExtendModelConverter getConverter(ExtendModelMapKey mapKey){
		IExtendModelConverter converter = null;
		if(map.containsKey(mapKey)){
			return map.get(mapKey);
		}
		for(IConfigurationElement ce:ExtendPointUtil.readAllConfiguredElements(EXTEND_POINT_ID)){
			if(mapKey.getKey().equals(ce.getAttribute(POINT_KEY_STRING)) && mapKey.getClassName().equals(ce.getAttribute(POINT_TARGET_CLASS_STRING))){
				try {
					converter = (IExtendModelConverter)ce.createExecutableExtension("class");
					break;
				} catch (Exception e) {
				}
			}
		}
		map.put(mapKey, converter);
		return converter;
	}
	
	//��ȡ��չMAP
	public void readExtendMap(IExtendAbleModel model,Element root){
		Element mapElement = root.element(MAP_STRING);
		if(mapElement == null && root.getName().equals(MAP_STRING)){
			mapElement = root;
		}
		String className = model.getClass().getName();
		if(mapElement != null){
			List list = mapElement.elements(ENTITY_STRING);
			if(list != null){
				for(Object o:list){
					if(o instanceof Element){
						Element entityElment = (Element)o;
						Element keyElement = entityElment.element(KEY_STRING);
						Element valueElement = entityElment.element(VALUE_STRING);
						if(keyElement != null && valueElement != null){
							String keyString = keyElement.getText();
							IExtendModelConverter converter = getConverter(new ExtendModelMapKey(keyString,className));
							//���û�ж�Ӧ����չ���д����Ϊ�˷�ֹ��Ϣ��ʧ��ģ����ֱ�ӻ���Element
							if(converter != null){
								model.getMap().put(keyString, converter.readExtendModel(valueElement));
							}else if("true".equalsIgnoreCase(keyElement.attributeValue(USER_CONFIG_PAGE_STRING))){
								model.getMap().put(keyString, readUserConfigMap(valueElement,keyString));
							}else{
								model.getMap().put(keyString,valueElement.clone());
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * @param valueElement
	 * @param keyString
	 * @return
	 */
	private UserConfigMap readUserConfigMap(Element valueElement, String keyString) {
		UserConfigMap map = new UserConfigMap();
		Element element = valueElement.element(keyString);
		if(element != null){
			for(Attribute attr:(List<Attribute>)element.attributes()){
				map.put(attr.getName(), attr.getValue());
			}
		}
		return map;
	}

	//д����չMAP
	public void writeExtendMap(IExtendAbleModel model,Element root){
		String className = model.getClass().getName();
		Element mapElment = root.addElement(MAP_STRING);
		if(model.getMap() != null){
			for(String keyString:model.getMap().keySet()){
				Element entityElment = mapElment.addElement(ENTITY_STRING);
				Element keyElment = entityElment.addElement(KEY_STRING);
				keyElment.setText(keyString);
				Object value = model.getMap().get(keyString);
				IExtendModelConverter converter = getConverter(new ExtendModelMapKey(keyString,className));
				if(converter != null){
					Element valueElment = entityElment.addElement(VALUE_STRING);
					converter.writeExtendModel(value,valueElment);
				}else if(value instanceof UserConfigMap){
					//�û��Զ���ҳ�� ������
					keyElment.addAttribute(USER_CONFIG_PAGE_STRING, "true");
					Element valueElment = entityElment.addElement(VALUE_STRING);
					writeUserConfigMap((UserConfigMap)value,valueElment,keyString);
				}else {
					((Element)value).setText(((Element)value).getTextTrim());
					entityElment.add(((Element)value));
				}
			}
		}
	}

	/**
	 * @param value
	 * @param valueElment
	 * @param keyString
	 */
	private void writeUserConfigMap(UserConfigMap value, Element valueElment, String keyString) {
		Element element = valueElment.addElement(keyString);
		for(String key:value.keySet()){
			element.addAttribute(key, value.get(key));
		}
	}
}




class ExtendModelMapKey{
	String key = "";
	String className = "";
	public ExtendModelMapKey(String key,String className) {
		this.key = key;
		this.className = className;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ExtendModelMapKey){
			return ((ExtendModelMapKey)obj).getKey().equals(key)
				&& ((ExtendModelMapKey)obj).getClassName().equals(className);
		}
		return super.equals(obj);
	}
	
	public String getClassName() {
		return className;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public String getKey() {
		return key;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return key.hashCode()*17+className.hashCode();
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
