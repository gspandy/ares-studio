package com.hundsun.ares.studio.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

/**
 * IParrent�ӿڵ�ʵ�ַ���
 * @author sundl
 *
 */
public class TestIParent extends AbstractAresCoreTester {

	/**
	 * ��Ŀ��getChildren()��������
	 */
	@Test
	public void testGetChildren_Project() {
		try {
			IARESElement[] children = project.getChildren();
			assertEquals(4, children.length);
			assertTrue(ArrayUtils.contains(children, root1));
			assertTrue(ArrayUtils.contains(children, lib));
			assertTrue(ArrayUtils.contains(children, externalLib));
			// ����һ������Ŀ����
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ģ�����getChilren()�����Ĳ���
	 */
	@Test
	public void testGetChildren_Module_Root() {
		try {
			IARESElement[] children = root1.getChildren();
			assertEquals(3, children.length);
			assertTrue(ArrayUtils.contains(children, moduleA));
			assertTrue(ArrayUtils.contains(children, moduleB));
			// default module?
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ģ���getChildren()�����Ĳ���
	 */
	@Test public void testGetChildren_Module() {
		try {
			IARESElement[] children = moduleB.getChildren();
			assertEquals(2, children.length);
			assertTrue(ArrayUtils.contains(children, resourceB));
			assertTrue(ArrayUtils.contains(children, resourceReadOnly));
			// default module?
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ���ð���getChildren�Ĳ��Է���
	 */
	@Test public void testGetChildren_Libs() {
		try {
			IARESElement[] children = lib.getChildren();
			assertEquals(1, children.length);
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��Ŀ��hasChildren()��������
	 * @throws ARESModelException 
	 */
	@Test
	public void testHasChildren_Project() throws ARESModelException {
		assertTrue(project.hasChildren());
	}

	/**
	 * ģ�����hasChilren()�����Ĳ���
	 * @throws ARESModelException 
	 */
	@Test
	public void testHasChildren_Module_Root() throws ARESModelException {
		assertTrue(root1.hasChildren());
	}
	
	/**
	 * ģ���hasChildren()�����Ĳ���
	 * @throws ARESModelException 
	 */
	@Test public void testHasChildren_Module() throws ARESModelException {
		assertTrue(moduleB.hasChildren());
		//assertFalse(moduleA.hasChildren());
	}
	
	/**
	 * ���ð���hasChildren�Ĳ��Է���
	 * @throws ARESModelException 
	 */
	@Test public void testHasChildren_Libs() throws ARESModelException {
		assertTrue(root1.hasChildren());
	}
}
