package com.hundsun.ares.studio.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
/**
 * �����׼�
 * @author sundl
 *
 */
@RunWith(Suite.class)	// ˵�����Ǹ������׼�
@Suite.SuiteClasses(	// �����׼�������Щ�࣬���԰������������׼�
		{
		TestElementFactory.class,
		TestUtil_isValideResourceName.class,
		TestUtil_isValideModuleName.class,
		TestCache.class,
		TestARESElement.class,
		TestIParent.class,
		TestARESProject.class,
		TestARESBundle.class}
		)	
public class AllTests {
	
}
