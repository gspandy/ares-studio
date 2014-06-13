/**
 * Դ�������ƣ�BaseModelConverter.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.modelconvert
 * ����˵�����ļ���ȡ�ͷ������е�������չʵ��
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.modelconvert;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Assert;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.core.registry.ARESResRegistry;
import com.hundsun.ares.studio.core.registry.IResDescriptor;

/**
 * �ṩ�˶�д�����֧��
 * @author gongyf
 *
 */
public abstract class BaseModelConverter extends AbstractModelConverter {
	
	private Logger log = Logger.getLogger(getClass());
	
	private Map<String, ModelConverterHandle> chainHeaderMap = new HashMap<String, ModelConverterHandle>();
	protected ModelConverterHandle getModelConverterChain(String type) {
		ModelConverterHandle chainHeader = chainHeaderMap.get(type);
		if (chainHeader == null) {
			chainHeader = createModelConverterChain(type);
			chainHeaderMap.put(type, chainHeader);
		}
		return chainHeader;
	}
	
	/**
	 * ����ģ��ת��ְ����
	 * @param type
	 * @return
	 */
	protected ModelConverterHandle createModelConverterChain(String type) {
		List<ModelConverterHandle> handles = ModelConverterUtils.CONVERTER_HANDLE_MAP.get(type);
		ModelConverterHandle result = getDefaultModelConverterHandle(type);
		// �����һ���ڵ㷴�򴴽�ְ����
		for (ModelConverterHandle handle : handles) {
			handle.setNextHandle(result);
			result = handle;
		}
		return result;
	}
	
	/**
	 * ��ȡĬ�ϵĴ��������ᱻ����ְ���������
	 * @param type
	 * @return
	 */
	protected abstract ModelConverterHandle getDefaultModelConverterHandle(String type);
	
	/**
	 * ��ģ�ʹ��������ж�ȡ�����һЩ����
	 * @param info
	 * @param resource
	 */
	protected abstract void processInfoOnRead(Object info, IARESResource resource);
	
	/**
	 * ��ģ��Ҫд���ļ�ǰ����һЩ����
	 * @param info
	 * @param resource
	 */
	protected abstract void processInfoOnWrite(Object info, IARESResource resource);
	
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.converter.IModelConverter2#read(com.hundsun.ares.studio.core.IARESResource)
	 */
	@Override
	final public Object read(IARESResource resource) throws Exception {
		InputStream is = null;
		Object info = null;
		IResDescriptor resDesc = ARESResRegistry.getInstance().getResDescriptor(resource.getType());
		Class<?> infoClass = resDesc.createInfo().getClass();
		
		try {
			is = resource.openStream();
			byte[] contents = IOUtils.toByteArray(is);
			
			ModelConverterHandle handle = getModelConverterChain(resource.getType());
			
			Assert.isNotNull(handle);
			
			info = handle.handleRead(resource, contents, infoClass);
			
			processInfoOnRead(info, resource);
			
		} catch (Exception e) {
			log.error("��ȡinfo�������" + resource.getFullyQualifiedName(),e);
			throw e;
		}finally {
			IOUtils.closeQuietly(is);
			
			if (info == null) {
				log.error("��ȡinfo����Ϊnull��" + resource.getFullyQualifiedName());
			}
		}
		
		return info;
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.core.model.converter.IModelConverter2#write(com.hundsun.ares.studio.core.IARESResource, java.lang.Object)
	 */
	@Override
	final public byte[] write(IARESResource resource, Object info) throws Exception {
		
		processInfoOnWrite(info, resource);
		
		byte[] contents = null;
		
		ModelConverterHandle handle = getModelConverterChain(resource.getType());
		
		Assert.isNotNull(handle);
		
		contents = handle.handleWrite(resource, info);
		
		Assert.isNotNull(contents);
		
		return contents;
	}
}
