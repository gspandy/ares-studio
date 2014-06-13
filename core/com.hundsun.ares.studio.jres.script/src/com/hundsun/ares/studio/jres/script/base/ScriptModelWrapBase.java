/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.script.base;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.Constants;
import com.hundsun.ares.studio.core.model.CoreFactory;
import com.hundsun.ares.studio.core.model.ExtensibleModel;
import com.hundsun.ares.studio.core.model.UserExtensibleProperty;
import com.hundsun.ares.studio.core.model.util.EMFJSONUtil;
import com.hundsun.ares.studio.jres.script.api.model.IScriptModelWrap;
import com.hundsun.ares.studio.jres.script.internal.util.IJSONUtil;
import com.hundsun.ares.studio.jres.script.util.IJRESScript;

/**
 * @author lvgao
 *
 */
public abstract class ScriptModelWrapBase<T> implements IScriptModelWrap,IJRESScript{

	protected IARESResource resource;
	
	public ScriptModelWrapBase(IARESResource resource){
		this.resource = resource;
	}

	public String toJSON() {
		try {
			Object obj = getOriginalInfo();
			if (obj instanceof EObject) {
				return EMFJSONUtil.write((EObject)obj);
			}
		} catch (Exception e) {
			console.error(String.format("����Դ[%s]ģ��ת��ΪJSONʧ��,��ϸ��Ϣ:%s" , resource.getPath().toOSString(),e.getMessage()));
		}
		return "";
	}

	public String getExtendsValue(String key){
		String value = IJSONUtil.instance.getStringFromJSON(toJSON(),key);//ȡϵͳ��չ����
		if(StringUtils.isBlank(value)){
			if(!StringUtils.startsWith(key, "user_")){
				key="user_"+key;
			}
			value = IJSONUtil.instance.getStringFromJSON(toJSON(),key);//ȡ�û���չ����
		}
		return value;
	}
	
	public void setExtendsValue(String key , String value){
		Object obj = getOriginalInfo();
		if (obj instanceof ExtensibleModel) {
			UserExtensibleProperty userProperty = (UserExtensibleProperty) ((ExtensibleModel)obj).getData2().get(Constants.USER_DATA2_KEY);
			if (userProperty != null){
				userProperty.getMap().put(key, value);
			}else {
				UserExtensibleProperty uep = CoreFactory.eINSTANCE.createUserExtensibleProperty();
				((ExtensibleModel)obj).getData2().put(Constants.USER_DATA2_KEY, uep);
				uep.getMap().put(key, value);
			}
		}
	}
	
	@Override
	public String getType() {
		return resource.getType();
	}

	@Override
	public String getName() {
		return resource.getName();
	}
	
	public IARESResource getResource() {
		return resource;
	}

	public abstract T getOriginalInfo();
	
}
