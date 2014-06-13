/**
 * Դ�������ƣ�SqlFormater.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.core.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import SQLinForm_200.SQLForm;

/**
 * @author liaogc
 *
 */
public class SqlFormater {

	
	/**
	 * ��ʽ��sql
	 * @param sql
	 * @param ���ݿ�ȡֵΪ"Any SQL","SQL Server","DB2/UDB","MSAccess","Sybase","Informix","MYSQL","PostgreSQL","Oracle"
	 * @return
	 */
	public static String formatSql(String sql, String language) {
		StringBuffer newSb = new StringBuffer();
		String tempsql = getSqlForm(language).formatSQLAsString(sql);
		if (tempsql != null) {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(tempsql.getBytes());
			InputStreamReader reader = new InputStreamReader(inputStream);
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			try {
				while ((line = br.readLine()) != null) {
					if (newSb != null && "/".equals(line.trim())) {
						newSb.append(line.trim() + "\r\n");
					} else {
						newSb.append(line + "\r\n");
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

				try {
					reader.close();
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return newSb.toString();
	}
	
	private static SQLForm getSqlForm(String language) {
		SQLForm form = new SQLForm();
		form.setCase(false, false);
		form.setGraphLevel(false);
		form.setSuppressSpace(true);
		form.setQuoteCharacter("'");
		form.setSuppressEmptyLine(false);
		form.setFormatLanguage("SQL");
		if(StringUtils.isBlank(language)){
			form.setSourceSQLLanguage("Oracle");
		}else{
			form.setSourceSQLLanguage(language);
		}
		
//		form.setBracketSpaces("noSpacesAroundBracket");
//		form.setCommaSpaces("oneSpaceAfterComma");
//		form.setEqualSpaces("oneSpaceAroundEqual");
		form.setSmallSQLWidth(120);
//		form.setPageWidth(80);
		form.setAndOrIndention(true);
		form.setInitialIndentation(0);
		form.setIndention(2, true);
		form.setNumCommas(4);//ÿ4��һ��
		form.setLinebreakKeyword(false);//�ؼ��ֺ��治����
		return form;
	}
	
	/**
	 * ��ʽ��sql
	 * @param sql
	 * @param ���ݿ�ȡֵΪ"Any SQL","SQL Server","DB2/UDB","MSAccess","Sybase","Informix","MYSQL","PostgreSQL","Oracle"
	 * @return
	 */
	public static String formatSqlOfCreateStatement(String sql, String language) {
		
		SQLForm sqlForm = getSqlForm(language);
		sqlForm.setCase(false, false);
		sqlForm.setNumCommas(1);
		sqlForm.setSmallSQLWidth(80);
		StringBuffer formatedSql = new StringBuffer();
		if (sql != null) {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(sql.getBytes());
			InputStreamReader reader = new InputStreamReader(inputStream);
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			try {
				StringBuffer sqlsb = new StringBuffer();
				while ((line = br.readLine()) != null) {
					if (sqlsb != null && "/".equals(line.trim())) {
						formatedSql.append(sqlForm.formatSQLAsString(sqlsb.toString()));
						formatedSql.append("\r\n"+line.trim() + "\r\n");
						sqlsb.delete(0, sqlsb.length());
					} else {
						sqlsb.append(line + "\r\n");
					}

				}
				if(sqlsb.length()!=0 && !sqlsb.toString().equals("\r\n")){
					formatedSql.append(sqlForm.formatSQLAsString(sqlsb.toString()));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

				try {
					reader.close();
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return formatedSql.toString();
	}


}
