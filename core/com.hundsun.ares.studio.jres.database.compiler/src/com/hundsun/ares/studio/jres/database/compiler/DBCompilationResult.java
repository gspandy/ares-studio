/**
 * Դ�������ƣ�DBCompilationResult.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.compiler
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ���Ҷ��
 */
package com.hundsun.ares.studio.jres.database.compiler;

import java.util.LinkedHashMap;
import java.util.Map;

import com.hundsun.ares.studio.jres.compiler.CompilationResult;
import com.hundsun.ares.studio.jres.compiler.ICompilationResultExtension;

/**
 * @author gongyf
 *
 */
public class DBCompilationResult extends CompilationResult implements
		ICompilationResultExtension {

	private String sql;
	
	private Map<String,StringBuffer> sqlByUser = new LinkedHashMap<String,StringBuffer>();
	
	public void setSql(String sql) {
		this.sql = sql;
	}
	
	@Override
	public String getTextResult() {
		return sql;
	}

	public Map<String,StringBuffer> getSqlByUser() {
		return sqlByUser;
	}
	
	public void setSqlByUser(Map<String, StringBuffer> sqlByUser) {
		this.sqlByUser = sqlByUser;
	}

}
