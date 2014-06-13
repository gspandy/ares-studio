/**
 * Դ�������ƣ�FormWidgetUtils.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.ExpandableComposite;


/**
 * @author gongyf
 *
 */
public class FormWidgetUtils {
	
	/**
	 * Ĭ��Section����ʽ
	 * @return
	 */
	public static int getDefaultSectionStyles() {
		return ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE | ExpandableComposite.EXPANDED;
	}
	
	public static int getExpanedSectionStyles() {
		return ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED;
	}
	
	public static int getDefaultSingleLineTextStyles() {
		return SWT.BORDER | SWT.SINGLE;
	}
	
	public static int getDefaultMultiLinesTextStyles() {
		return SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL;
	}
	
	public static int getDefaultTreeStyles() {
		return SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI;
	}
	
	public static int getDefaultTableStyles() {
		return SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI;
	}
	
	public static int[] getDefaultTextDataBingingEvents() {
		return new int[]{ SWT.Modify };
	}
}
