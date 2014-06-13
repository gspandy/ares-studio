/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.ui.control;

import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import com.hundsun.ares.studio.ui.assist.IAssistantProvider;
import com.hundsun.ares.studio.ui.assist.TextContentAssistEx;
import com.hundsun.ares.studio.ui.util.ImporveControlWithDitryStateContext;

/**
 * ������ʾ��չ�ı���
 * 
 * @author mawb
 */
public class TextAssistAdaptorEx extends TextAdaptor {
	
	/**
	 * @param label ��ʾ�ֶ�
	 * @param controlStyle �ؼ���ʽ
	 * @param context ������
	 * @param provider ������ʾ�ṩ��
	 */
	public TextAssistAdaptorEx(String label, int controlStyle, ImporveControlWithDitryStateContext context, IAssistantProvider provider) {
		super(label, controlStyle, context);
		((TextContentAssistEx)getControl()).setContentProvider(provider);
	}
	
	/**
	 * @param label ��ʾ�ֶ�
	 * @param controlStyle �ؼ���ʽ
	 * @param context ������
	 * @param separator �ı��ָ������ı������ж����ʾ��ʱ�� 
	 * @param provider ������ʾ�ṩ��
	 */
	public TextAssistAdaptorEx(String label, int controlStyle, ImporveControlWithDitryStateContext context, String separator, IAssistantProvider provider) {
		this(label, controlStyle, context, provider);
		((TextContentAssistEx)getControl()).setContentSeparator(separator);
	}
	
	/**
	 * @param ��ʾ�ֶ�
	 * @param �ؼ���ʽ
	 * @param ������
	 * @param ������ʾ�ṩ��
	 * @param ���ֶ�����
	 */
	public TextAssistAdaptorEx(String label, int controlStyle, ImporveControlWithDitryStateContext context, IAssistantProvider provider, String beanFieldName) {
		super(label,controlStyle,context,beanFieldName);
		((TextContentAssistEx)getControl()).setContentProvider(provider);
	}
	
	/**
	 * @param label ��ʾ�ֶ�
	 * @param controlStyle �ؼ���ʽ
	 * @param context ������
	 * @param separator �ı��ָ������ı������ж����ʾ��ʱ�� 
	 * @param provider ������ʾ�ṩ��
	 * @param beanFieldName ���ֶ�����
	 */
	public TextAssistAdaptorEx(String label, int controlStyle, ImporveControlWithDitryStateContext context, String separator, IAssistantProvider provider, String beanFieldName) {
		this(label, controlStyle, context, provider, beanFieldName);
		((TextContentAssistEx)getControl()).setContentSeparator(separator);
	}
	
	/**
	 * @param ��ʾ�ֶ�
	 * @param �ؼ���ʽ
	 * @param ������
	 * @param ������ʾ�ṩ��
	 * @param ��ģ��
	 * @param ���ֶ�����
	 */
	public TextAssistAdaptorEx(String label, int controlStyle, ImporveControlWithDitryStateContext context, IAssistantProvider provider, Object model, String beanFieldName) {
		super(label,controlStyle,context,model,beanFieldName);
		((TextContentAssistEx)getControl()).setContentProvider(provider);
	}
	
	/**
	 * @param label ��ʾ�ֶ�
	 * @param controlStyle �ؼ���ʽ
	 * @param context ������
	 * @param separator �ı��ָ������ı������ж����ʾ��ʱ��
	 * @param provider ������ʾ�ṩ��
	 * @param model ��ģ��
	 * @param beanFieldName ���ֶ�����
	 */
	public TextAssistAdaptorEx(String label, int controlStyle, ImporveControlWithDitryStateContext context, String separator, IAssistantProvider provider, Object model, String beanFieldName) {
		this(label, controlStyle, context, provider, model, beanFieldName);
		((TextContentAssistEx)getControl()).setContentSeparator(separator);
	}
	
	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.control.TextAdaptor#initControl()
	 */
	@Override
	protected Text initControl() {
		text = new TextContentAssistEx(parent, controlStyle|SWT.BORDER);
		format = new FormattedText(text);
		return text;
	}

}
