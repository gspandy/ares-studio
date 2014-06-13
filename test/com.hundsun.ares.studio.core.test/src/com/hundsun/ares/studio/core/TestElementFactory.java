package com.hundsun.ares.studio.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.junit.Before;
import org.junit.Test;


/**
 * ����create()����
 * @author sundl
 *
 */
public class TestElementFactory extends AbstractAresCoreTester{

	private IWorkspaceRoot workspaceRoot;
	
	@Before
	public void init() {
		workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
	}
	
	/**
	 * �ļ����create
	 */
	@Test
	public void testIFilebased() {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path("/testproject1/testroot1/aaa.test"));
		IARESElement ce = ARESCore.create(file);
		assertNotNull(ce);
		assertEquals(IARESElement.ARES_RESOURCE, ce.getElementType());
		assertTrue(ce.exists());
		
		// ����δע�����Դ
		file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path("/testproject1/testroot1/aa/bb/ccc.test1"));
		IARESElement resourceNotRegistered = ARESCore.create(file);
		assertTrue(resourceNotRegistered == null);
	}
	
	/**
	 * ModuleRoot��Create����
	 */
	@Test
	public void testCreateModuleRoot() {
		IFolder root1 = workspaceRoot.getFolder(new Path("/testproject1/testroot1"));
		assertNotNull(root1);
		assertTrue(root1.exists());  
		
		IFolder root100 = workspaceRoot.getFolder(new Path("/testproject1/testroot100"));
		assertNotNull(root100);
		assertTrue(!root100.exists());
	}
	
	@Test
	public void testCreateModule() {
		IFolder module1 = workspaceRoot.getFolder(new Path("/testproject1/testroot1/aa"));
		assertNotNull(module1);
		assertTrue(module1.exists());
		
		IFolder module2 = workspaceRoot.getFolder(new Path("/testproject1/testroot1/aaaaaaaaaaaaaaaaa"));
		assertNotNull(module2);
		assertTrue(!module2.exists());
	}
	
	@Test
	public void testIProjectbased() {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(Helper.TEST_PROJECT_1);
		IARESProject cProject = ARESCore.create(project);
		assertNotNull(cProject);
		assertTrue(cProject.exists());
	}
	
	@Test
	public void testElementIdentifier() {
		try {
			// ��Ŀ�ڵ���Դ
			IARESResource res = project.findResource("aa.bb.bbb", "test");
			assertNotNull(res);
			assertTrue(res.exists());
			
			String id = res.getHandleIdentifier();
			IARESResource resFromId = (IARESResource) ARESCore.create(id);
			assertEquals(res, resFromId);
			
			// �������ڵ����ð�
			res = lib.findResource("aaa", "test");
			assertNotNull(res);
			assertTrue(res.exists());
			
			id = res.getHandleIdentifier();
			resFromId = (IARESResource) ARESCore.create(id);
			assertEquals(res, resFromId);
			assertTrue(resFromId.exists());
			
			// ������������ð�
			res = externalLib.findResource("aaa", "test");
			assertNotNull(res);
			assertTrue(res.exists());
			
			id = res.getHandleIdentifier();
			resFromId = (IARESResource) ARESCore.create(id);
			assertEquals(res, resFromId);
			assertTrue(resFromId.exists());
		} catch (ARESModelException e) {
			e.printStackTrace();
		}
	}
	
}
