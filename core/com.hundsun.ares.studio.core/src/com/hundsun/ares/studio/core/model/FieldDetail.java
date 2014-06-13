/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.core.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * ��ģ��������˽������Ϣ
 * �ƻ���ģ�͵Ĵ����� ����
 * ģ���ֶα�ע
 * @author maxh
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldDetail {
	/**
	 * ������չ�ֵ�����
	 * @return
	 */
	String showName() default "";
	/**
	 * ������չ�ֵ�ͼƬ ��Ҫ���ڴ��������
	 * @return
	 */
	String showPic() default "";
	/**
	 * ���ڻ��ģ��չ���ֶ�
	 * @return
	 */
	String mainGetMethod() default "";
//	/**
//	 * ����չ�ֵĿؼ�����
//	 * @return
//	 */
//	int showControlType() default TEXT_TYPE;
//	/**
//	 * combo��assittext��ѡ��ֵ
//	 * @return
//	 */
//	String[] value() default {};
	/**
	 * �Ƿ��ڴ����չ��
	 * @return
	 */
	boolean showInOutline() default true;
//	/**
//	 * �Ƿ�ģ�����ɽ���
//	 * @return
//	 */
//	boolean genUI() default false;
//	
//	
//	
//	static final public int TEXT_TYPE = 0;
//	static final public int COMBO_TYPE = 1;
//	static final public int DATE_TYPE = 2;
//	static final public int ASSIT_TEXT_TYPE = 3;
}
