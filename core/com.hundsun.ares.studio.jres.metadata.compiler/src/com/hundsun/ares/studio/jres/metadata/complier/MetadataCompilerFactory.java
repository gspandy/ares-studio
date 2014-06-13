/**
* <p>Copyright: Copyright (c) 2011</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.metadata.complier;

import org.eclipse.emf.ecore.EClass;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.compiler.IResourceCompiler;
import com.hundsun.ares.studio.jres.compiler.IResourceCompilerFactory;
import com.hundsun.ares.studio.jres.model.metadata.MetadataPackage;

/**
 * @author gongyf
 *
 */
public class MetadataCompilerFactory implements IResourceCompilerFactory {

	/**
	 * 
	 */
	public MetadataCompilerFactory() {
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.core.compiler.IResourceCompilerFactory#isSupport(com.hundsun.ares.studio.core.IARESProject)
	 */
	@Override
	public boolean isSupport(IARESProject project) {
		/*
		 * DESIGN#������#��Ҷ��#��ͨ#��Ҷ��#�����빤���Ƿ����
		 *
		 * Ŀǰ���빤��Ӧ�ö��ⲿû���κ�Ҫ�󣬵��ǽ����ܶ�jresdk�汾��Ҫ��
		 * ��Ҫ���п���
		 */
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.core.compiler.IResourceCompilerFactory#create(com.hundsun.ares.studio.core.IARESResource)
	 */
	@Override
	public IResourceCompiler create(Object resource) {
		/*
		 * DESIGN#��Դ����#��Ҷ��#����#��д��#������Դ����������
		 *
		 * ��Ҫ��Ӧ��Դ���������������������ս�ʹ��jet��ʽʵ�֣�����Ŀǰ��һ�׶�ֻ��Ԫ���ݲ���
		 * ���Ա�����Ӧ���Ƕ�ȡԪ������Դ�ڵĽű���Դ���б��룬���ܱ������ڽű�����������
		 * ʵ�ʵı��������ܲ����ر�������
		 */
		return new MetadataResComplier();
	}

}
