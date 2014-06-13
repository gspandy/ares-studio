/**
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: �������ӹɷ����޹�˾</p>
 */
package com.hundsun.ares.studio.logic.compiler.util;

import org.eclipse.core.runtime.Assert;


/**
 * ��ʾʵ�ʴ�����һ��ֵ��������ֵ����ֵ
 * 
 * @author gongyf
 *
 */
public class Value {

	public static final String CLOB = "clob";
	
	/** ��������ʽ */
	private static final String FORMAT_DECLARE = "%s %s%s = %s; %s\n";
	
	/** �޳�ֵ��������ʽ */
	private static final String FORMAT_DECLARE_NO_INIT = "%s %s%s; %s\n";
	
	/**
	 * �Ƿ�����ֵ��������ֵʱsourceText��Ч�������� prefix��name��Ч
	 */
	public boolean isRightValue = true;
	
	/**
	 * ǰ׺
	 */
	public String prefix = "";
	
	/**
	 * ������
	 * 
	 */
	public String name = "";
	
	/**
	 * ��ȡ����ȫ��
	 * @return ������
	 */
	public String getVariableName() {
		return prefix + name;
	}
	
	/** 
	 * ֱ�ӿ���д�������ı����� <BR>
	 * lpResultSet->GetString("mmss"), "0"," ", 0 
	 * */
	public String sourceText = "";

	/**
	 * ��ʾRAW����ʹ�õĳ��ȴ����ı�
	 */
	public String sourceTextLength = "";
	
	/**
	 * ��ȡֵ�ڴ����еı����ı�
	 * @return ��ʵ����
	 */
	public String getValue() {
		if (isRightValue) {
			return sourceText;
		} else {
			return getVariableName();
		}
	}
	
	public String getRawLengthValue() {
		Assert.isTrue(getTypeCategory() == TC_RAW, "������RAW���Ͳ��ܵ���");
		if (!isRightValue) {
			return (prefix.equals("v_") ? "vi_" : "pi_")  + name;
		}
		return sourceTextLength;
	}
	
	/**
	 * �������ͣ���int char* IPacker*
	 */
	public String dataType = "";
	
	/**
	 * ���������ʱ��length >= 0
	 */
	public int arrayLength = -1;
	
	/**
	 * ��ʼֵ������������ʱ��
	 */
	public String initValue = "";
	
	/**
	 * ����һ��ֵ���г�ʼ��
	 * 
	 */
	public Value initValue2 = null;
	
	/**
	 * ����ʱ���ע��
	 */
	public String initComment = "";
	
	/**
	 * ����������Ҫ���⴦������
	 */
	public String extendCode = "";
	
	// ����ר��
	/** ��Чλ�� */
	public int p;
	/** С��λ��ȷ�� */
	public int s;
	
	public static final int TC_CHAR = 0;
	public static final int TC_INT = 1;
	public static final int TC_DOUBLE = 2;
	public static final int TC_STRING = 3;
	// 2008��12��15��16:28:30 ������ RAW
	public static final int TC_RAW = 4;
	
	public static final int TC_F2UNPACKER = 5;
	
	public static final int TC_F2PACKER = 6;
	
//	private Pattern P_F2UNPACKER_POINTER = Pattern.compile("if2unpacker\\s*\\*");
//	private Pattern P_F2PACKER_POINTER = Pattern.compile("if2packer\\s*\\*");
	
	/**
	 * ��ñ�ֵ������
	 * @return ����
	 */
	public int getTypeCategory() {
		if (dataType.matches("^char\\s+\\*$")) {
			return TC_STRING;
		} else if (dataType.equals("char")) {
			if (arrayLength < 0) {
				return TC_CHAR;
			} else {
				return TC_STRING;
			}
		} else if (dataType.equals("float") || dataType.equals("double")) {
			return TC_DOUBLE;
		} else if (dataType.equals(CLOB)) {
			return TC_RAW;
		} else if (dataType.toLowerCase().startsWith("if2unpacker")) { // FIXME ƥ����ܳ������⣬���û�δ����*�������ж��*
			return TC_F2UNPACKER;
		} else if (dataType.toLowerCase().startsWith("if2packer")) { // FIXME ƥ����ܳ������⣬���û�δ����*�������ж��*
			return TC_F2PACKER;
		}else {
			return TC_INT;
		}
	}
	
	/**
	 * �Ӹ�����Value�и���������Ϣ
	 * @param v
	 */
	public void setTypeInfoFrom(Value v) {
		this.dataType = v.dataType;
		this.arrayLength = v.arrayLength;
		this.p = v.p;
		this.s = v.s;
	}
	
	/**
	 * ��������
	 * @param v
	 */
	public void copyFrom(Value v) {
		this.arrayLength = v.arrayLength;
		this.dataType = v.dataType;
		this.extendCode = v.extendCode;
		this.initComment = v.initComment;
		this.initValue = v.initValue;
		this.initValue2 = v.initValue2;
		this.isRightValue = v.isRightValue;
		this.name = v.name;
		this.p = v.p;
		this.prefix = v.prefix;
		this.s = v.s;
		this.sourceText = v.sourceText;
		this.sourceTextLength = v.sourceTextLength;
	}
	
	/**
	 * ���һ����ֵ��䣬��b���Ƶ�a
	 * 
	 * @param sb
	 * @param a
	 * @param b
	 */
	static public void writeCopy(StringBuffer sb, Value a, Value b) {
		
		Assert.isTrue(a.isRightValue == false);
		
		if (a.getTypeCategory() == TC_STRING) {
			// �ַ�������
			// sprintf(v_error_info, "%s", lpResultSet1330060->getString("error_info"));
			// hs_strncpy(a, b);
			sb.append( String.format("hs_strcpy(%1$s, %2$s);\n", a.getValue(), b.getValue()));
//			sb.append("sprintf(");
//			sb.append(a.getValue());
//			sb.append(", \"%s\", ");
//			sb.append(b.getValue());
//			sb.append(");\n");
		} else if (a.getTypeCategory() == TC_RAW){
			// ��2������
			sb.append(a.getRawLengthValue());
			sb.append(" = ");
			String valueString = b.getRawLengthValue();
			// Ϊ�յ�ʱ�����0����
			if (valueString.trim().isEmpty()) {
				sb.append("0");
			} else {
				sb.append(b.getRawLengthValue());
			}

			sb.append(";\n");
			
			sb.append(a.getValue());
			sb.append(" = ");
			sb.append(b.getValue());
			sb.append(";\n");
		} else {
			sb.append(a.getValue());
			sb.append(" = ");
			sb.append(b.getValue()); // TODO ��ͬ���͵�����Ƿ���Ҫ����
			sb.append(";\n");
		}
	}
	
	/**
	 * д��һ���������
	 * @param sb
	 */
	public void writeDeclare(StringBuffer sb) {
		
		Assert.isTrue(isRightValue == false, "��ֵ���ɽ�����������");
		String array = "";
		if (arrayLength > -1) {
			array = "[" + String.valueOf(arrayLength + 1) + "]";
		}
		
		String comment = null;
		if (initComment == null || initComment.trim().isEmpty()) {
			comment = "";
		} else {
			comment = "// " + initComment;
		}
		
		String theType = dataType;
		// 2008��12��15��16:37:36
		// RAW ������ʵ��Ҫ2������Э��,��Ӽ�¼���ȵı���
		if (getTypeCategory() == TC_RAW) {
			sb.append( String.format(FORMAT_DECLARE, "int", getRawLengthValue(), "", "0", "") );
			theType = "void *";
		}
		
		
		if (initValue == null || initValue.trim().isEmpty()) {
			// "%s %s%s;\n";
			sb.append( String.format(FORMAT_DECLARE_NO_INIT, theType, getVariableName(), array, comment) );
		} else {
			// "%s %s%s = %s;\n";
			sb.append( String.format(FORMAT_DECLARE, theType, getVariableName(), array, initValue, comment) );
		}
		
		if (initValue2 != null) {
			writeCopy(sb, this, initValue2);
		}
		
		sb.append(extendCode);

	}
	
	public boolean isConstant = false;
}
