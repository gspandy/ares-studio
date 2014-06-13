/**
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.procedure.compiler.mysql.constant;

/**
 * @author lvgao
 *
 */
public interface IProcedureEngineContextConstantMySQL {
	
	//=============֤ȯ�������==begin==============
	public static final String procedure = "procedure";
	public static final String function = "function";
	public static final String return_type = "number";
	//public static final String define_type = "as";
	//public static final String auto_define_input_param = "autoDefineInputParam";
	//public static final String not_define_connect_type = "notDefineConnectType";
	//public static final String return_error_info = "returnErrorInfo";
	public static final String gen_related_info = "genRelatedInfo";
	//public static final String gen_begin_code = "genBeginCode";
	//public static final String gen_end_code = "genEndCode";
//	public static final String has_stock_two_ex = "hasStockTwoExPage";
	//=============֤ȯ�������==end================

//	public static final String Function_Macro_Service = "���������";
	
	public static final String Procedure_Macro_Service = "�洢���̺����";
	
//	public static final String Structure_Service = "�������";
	
	public static final String UserMacro_Service = "�û��Զ�������";
	
	/////////////////////////////�����ͷ���////////////////////////////
	
	/**
	 * �������д�ȡ��ǰ��Ŀ
	 */
	public static final String Aresproject = "��ǰ��Ŀ";
	
	/**
	 * �����������д�ȡ��Ӧ�ĺ��������
	 * �磺��Щ���Ƿ�����ڴ洢���̵�����
	 */
	public static final String MACRO_FILTER = "�������";
	
	
	public static final String InternalSTDHelper = "�Ƿ�����ʱ��׼�ֶΰ�����";
	
	/**
	 * �������������д����Դģ��
	 * ��Ҫ��Ϊ��Ԥ����ƣ�����Ԥ��ʱ�����ģ����δ�����
	 * ��Ԥ��ʱ���ô���ֵ
	 */
	public static final String ResourceModel = "��Դģ��";
	
	/**
	 * ��ǰΪ�˷���ʹ��jet�����õ��������з������
	 */
	public static final String Macro_args = "�����";
	
	public static final String ENGINE_EXCEPTION = "���ɴ���ʱ�����Ĵ���";
	
	public static final String ERROR_LOG_PATH = "���ɴ����ļ�ʱ���ɵ�log�ļ�·��";
	
	public static final String ERROR_LOG_FILENAME = "\\error.log";
	
	/**
	 * �������д�ȡα�����г��ֹ��ı�����Ҳ����@��ͷ�ı���
	 */
	public static final String PseudoCode_Para_LIST = "α����ʹ�ñ����б�";
	
	/////////////////////////////�����ͷ���ȫ������////////////////////////////
	/**
	 * Ϊ�˱�������ظ��������ƵĴ˲�����
	 * Ĭ��ʵ���м�¼�˲������ͺͲ������ƣ������ظ�����
	 */
	public static final String PARAM_DEFINE_HELPER = "�������������";
	
	/**
	 * ������ͼ����Ҫ���������ȫ�������ģ���Ȼ��Щ����Ҳ���Դ浽�������У���������
	 * �����ľ���Ը����ˣ�������Щ���Կ�����˳�������Ҫ������ר�������һ��ȫ�����Խӿ�
	 * ������ATTR_��ͷ�Ķ��Ǵ���"��ͼȫ�����԰�����"�е�
	 */
	public static final String SKELETON_ATTRIBUTE_HELPER = "��ͼȫ�����԰�����";
	
	/**
	 * 	����ʹ�ó��ù����ʼ���ı������Ƕ���Ϊ���ӱ���
	 *     ����������ɴ����
	 *     ��//��ʹ�õĸ��Ӳ�����ʼ����
	 *     ����
	 */
	public static final String ATTR_INITIAL_PARA = "α������ʹ�õĸ��ӱ�������";
	
	//��¼�ӿ�
//	public static final String ATTR_USED_RECORD_INTERFACE = "ʹ�õĶ���ӿ��б�";
	//��¼�ṹָ��
//	public static final String ATTR_USED_RECORD_OBJECT = "ʹ�õĶ���ṹ�б�";
	
	//�����Ķ���
//	public static final String ATTR_USED_RECORD_OBJECT_NEED_LOCK = "��Ҫ�����Ķ���";
	
	//���ڵ��ú���ʱĬ�ϴ�����
//	public static final String ATTR_USED_RECORD_TYPE = "ʹ�õĶ��������б�";
	
	/**
	 * �����ɹ�ʱҪ��ӵĴ��룬
	 * ���гɹ����붼��ӵ���Ӧ�̶�λ��
	 */
//	public static final String ATTR_FUNCTION_SUCCESS = "�����ɹ�";
	
	/**
	 * ����ʧ��ʱҪ��ӵĴ��룬
	 * ����ʧ�ܴ��붼��ӵ���Ӧ�̶�λ��
	 */
//	public static final String ATTR_FUNCTION_FAIL = "����ʧ��";
	
	/**
	 * ���ܺ����ɹ���ʧ�ܶ�Ҫִ�еĴ���
	 */
//	public static final String ATTR_FUNCTION_END = "��������-�����ɹ�ʧ��";
	
	public static final String ATTR_DATABASE_MACRO = "ʹ�õ����ݿ�������б�";
	
	public static final String ATTR_PROC_VARIABLE_LIST = "PRO*C�����б�";
	
//	public static final String ATTR_FUNC_CALL = "���������б�";
	
	public static final String ATTR_FUNC_RESULTSET = "������б�";
	
	public static final String ATTR_PROCEDURE_CALL = "���̵����б�";
	
	public static final String ATTR_PROC_MACRO = "ʹ�õ�PRO*C���б�";
	
	public static final String ATTR_IN_OUT_PARAM_LIST = "������������б�";
	
	public static final String ATTR_PACK_VALIABLESET = "pack�����б�";
	
	public static final String ATTR_CURSOR_LIST = "�α��б�";
	
	public static final String ATTR_GETLAST_RESULTSET = "ȡ�þͽ��Ľ����Id";//ȡ�þͽ��Ľ����Id�����ǿ�����lpResultSet->ȡ�ͽ�������ģ�����Ҫ�ӵ�����б���
	
	public static final String ATTR_RESULTSET_LIST = "������б�";//����Ľ��������ȫ���ͷ�

}
