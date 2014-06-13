/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.metadata.resources.internal;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.IObjectProvider;

/**
 * @author lvgao
 *
 */
public class EMFURIObjectProvider  implements IObjectProvider{
	private Logger logger = Logger.getLogger(EMFURIObjectProvider.class);
	String uri;
	public EMFURIObjectProvider(String uri){
		this.uri = uri;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.reference.IObjectProvider#getObject(com.hundsun.ares.studio.core.IARESResource)
	 */
	@Override
	public Object getObject(IARESResource resource) {
		try {
			EObject eObj = resource.getInfo(EObject.class);
			return eObj.eResource().getEObject(uri);
		} catch (Exception e) {
			logger.error(String.format("������Ϣ��ȡ��Դ%s����info��ʱ�����...", resource.getElementName()), e);
		}
		return null;
	}

}
