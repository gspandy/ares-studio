/**
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 */
package com.hundsun.ares.studio.jres.modelconvert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.IOWrappedException;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.xml.sax.SAXParseException;

import com.hundsun.ares.studio.core.ConsoleHelper;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.model.ExtensibleModel;

/**
 * @author gongyf
 *
 */
public class DefaultEMFModelConverterHandle extends ModelConverterHandle {
	static final Logger console = ConsoleHelper.getLogger();
	private static Logger logger = Logger.getLogger(DefaultEMFModelConverterHandle.class);
	protected Resource createEMFResource(URI uri) {
		return new XMLResourceImpl(uri);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.modelconvert.ModelConverterHandle#handleRead(com.hundsun.ares.studio.core.IARESResource, byte[], java.lang.Class)
	 */
	@Override
	public Object handleRead(IARESResource resource, byte[] contents,
			Class<?> clazz) throws Exception {
		Resource emfRes = null;
		IResource rawRes = resource.getResource();
		if (rawRes == null) {
			emfRes = createEMFResource(null);
		} else {
			emfRes = createEMFResource(URI.createPlatformResourceURI(rawRes.getFullPath().toString(), true));
		}
		try{
			emfRes.load(new ByteArrayInputStream(contents), ModelConverterUtils.EMF_LOAD_OPTIONS);
		} catch (IOWrappedException e) {
			Throwable cause = e.getCause();
			String msg = String.format("�����ļ�%sʧ�ܣ� ԭ��������ļ����Ǳ�׼XML��ʽ�������ļ���\n ������Ϣ��%s", resource.getPath(), e.getMessage());
			if (cause instanceof SAXParseException) {
				SAXParseException saxException = (SAXParseException) cause;
				int line = saxException.getLineNumber();
				int column = saxException.getColumnNumber();
				msg += String.format("\n����λ�ã�%s��%s��", line, column);
			}
			cause.getLocalizedMessage();
			console.info(msg);
			logger.error(msg, e);
		}
		catch(Exception e){
			console.info("����"+resource.getResource().getFullPath().toString()+"����\r\n"+"����ԭ��:�ļ������Ѳ��Ǳ�׼��xml��ʽ 2,�ļ���������������.��鿴�ļ�����\r\n"+e.getMessage());
			logger.error("����"+resource.getResource().getFullPath().toString()+"����\r\n"+"����ԭ��:�ļ������Ѳ��Ǳ�׼��xml��ʽ 2,�ļ���������������.��鿴�ļ�����\r\n"+e.getMessage(), e);
		}
	
		EObject info = emfRes.getContents().get(0);
		
		Set<ExtensibleModel> models = new HashSet<ExtensibleModel>();
		
		if (info instanceof ExtensibleModel) {
			models.add((ExtensibleModel) info);
		}
		
		for (Iterator<Object> iterator = EcoreUtil.getAllContents(info, true); iterator.hasNext();) {
			Object obj = iterator.next();
			if (obj instanceof ExtensibleModel) {
				models.add((ExtensibleModel) obj);
			}
		}
		
		for (ExtensibleModel extensibleModel : models) {
			for (Iterator<Entry<String, EObject>> iterator = extensibleModel.getData2().iterator(); iterator.hasNext();) {
				Entry<String, EObject> entry = iterator.next();
				if (entry.getValue() == null) {
					iterator.remove();
				}
			}
		}
		
		return info;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.modelconvert.ModelConverterHandle#handleWrite(com.hundsun.ares.studio.core.IARESResource, java.lang.Object)
	 */
	@Override
	public byte[] handleWrite(IARESResource resource, Object info)
			throws Exception {
		if (info instanceof EObject) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			EObject obj = (EObject) info;
			if (obj.eResource() == null) {
				Resource emfRes = new XMLResourceImpl();
				emfRes.getContents().add(obj);
			}
			
			obj.eResource().save(out, ModelConverterUtils.EMF_SAVE_OPTIONS);
			return out.toByteArray();
		} else {
			throw new UnsupportedOperationException("info������EObject����");
		}
	}

}
