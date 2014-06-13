/**
 * Դ�������ƣ�TestARESBundle.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.core.test
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.core;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 * @author sundl
 */
public class TestARESBundle extends AbstractAresCoreTester{
	
	@Test
	public void test() {
		assertNotNull(project);
		if (project != null)
			assertTrue(project.exists());
		
		assertNotNull(lib);
		assertEquals(lib.getId(), "ares.unittest");
		
		assertNotNull(externalLib);
		assertEquals(externalLib.getId(), "ares.unittest");
		
		IARESResource resaa;
		try {
			resaa = lib.findResource("aaa", "test");
			assertNotNull(resaa);
			assertEquals(resaa.getInfo(PropertiesModel.class).properties.get("id"), "a");
			
			resaa = externalLib.findResource("aaa", "test");
			assertNotNull(resaa);
			assertEquals(resaa.getInfo(PropertiesModel.class).properties.get("id"), "a");
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testReuiredBundles() {
		IARESBundle[] bundles = project.getRequiredBundles();
		assertNotNull(bundles);
		assertEquals(bundles.length, 2);
		
		try {
			IARESResource resFromProject = project.findResource("abc", "test");
			assertTrue(resFromProject == null);
			
			assertNotNull(lib.findResource("aaa", "test"));
			assertNotNull(externalLib.findResource("aaa", "test"));
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
	}
	
}
