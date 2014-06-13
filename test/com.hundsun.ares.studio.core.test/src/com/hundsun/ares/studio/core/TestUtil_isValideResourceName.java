/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.hundsun.ares.studio.core.util.Util;

/**
 * ����Util������
 * @author sundl
 */
@RunWith(Parameterized.class)
public class TestUtil_isValideResourceName {
	
	private Boolean expected;
	private String target;
	
	/**
	 * ��������
	 * @return
	 */
	@Parameters
	public static Collection cases() {
		return Arrays.asList(new Object[][] {
				{"test.xxx", true},	// normal
				{"test.", false},
				{".xxx", false},
				{"xxx", false},
				{".", false}
		} );
	}
	
	/**
	 * ���������Ա���Ĺ��캯��
	 * @param expected
	 * @param target
	 */
	public TestUtil_isValideResourceName(String target, Boolean expected) {
		this.expected = expected;
		this.target = target;
	}
	
	@Test
	public void testIsValidResname() {
		assertEquals(expected, Util.isValiedCommonResourceName(target));
	}
	
}
