/**
 * Դ�������ƣ�PMDConstant.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.pdm;

/**
 * @author liaogc
 *
 */
public interface PMDConstant {


	/**
	 * �����෵�ؽ���Ľṹ����Key
	 */
	public final static String GROUP_DOMAIN = "Domain";
	
	public final static String GROUP_PACKAGE = "package";
	
	public final static String GROUP_TABLE = "table";
	
	public final static String GROUP_VIEW = "view";
	
	/**
	 * PDM�ļ��ڵ㶨��
	 */
	public final static String NODE_NAME = "Name";
	public final static String NODE_CODE = "Code";
	public final static String NODE_PACKAGE = "//c:Packages/o:Package";
	public final static String NODE_DOMAINS = "//c:Domains/o:PhysicalDomain";
	public final static String NODE_PHYSICAL_DOMAIN = "PhysicalDomain";
	public final static String NODE_DOMAIN = "Domain";
	public final static String NODE_TABLE = "c:Tables/o:Table";
	public final static String NODE_NO_PACK_TABLE = "//o:Model/c:Tables/o:Table";
	public final static String NODE_VIEW = "//c:Views/o:View";
	public final static String NODE_COLUMN = "c:Columns/o:Column";
	public final static String NODE_COLUMN_DATA_TYPE ="DataType" ;
	public final static String NODE_IND_COLUMN = "c:Column/o:Column";
	public final static String NODE_PRIMARY_KEY = "c:Keys/o:Key/c:Key.Columns/o:Column";
	public final static String NODE_INDEX_COLUMN = "c:IndexColumns/o:IndexColumn";
	public final static String NODE_CLUSTER = "c:ClusterObject/o:Index";
	public final static String NODE_DATATYPE = "DataType";
	public final static String NODE_VIEWQUERY = "View.SQLQuery";
	public final static String NODE_COMMENT = "Comment";
	public final static String NODE_MANDATORY = "Mandatory";
	public final static String NODE_DEFAULTVALUE = "DefaultValue";
	public final static String NODE_INDEX = "c:Indexes/o:Index";
	public final static String NODE_UNIQUE = "Unique";
	public final static String NODE_VIEW_SQLQUERY = "View.SQLQuery";
	
	/**
	 * PDM���Զ���
	 */
	public final static String ATTRIBUTE_ID = "Id";
	public final static String ATTRIBUTE_REF = "Ref";
	
	
}
