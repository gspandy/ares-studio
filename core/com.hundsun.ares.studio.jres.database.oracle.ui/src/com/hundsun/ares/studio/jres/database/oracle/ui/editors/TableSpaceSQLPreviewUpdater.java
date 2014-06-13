/**
 * Դ�������ƣ�DBSQLPreviewUpdater.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.database.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.database.oracle.ui.editors;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;

import com.hundsun.ares.studio.core.ARESModelException;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.core.util.SqlFormater;
import com.hundsun.ares.studio.jres.compiler.CompilationResult;
import com.hundsun.ares.studio.jres.compiler.CompileManager;
import com.hundsun.ares.studio.jres.compiler.CompileUtils;
import com.hundsun.ares.studio.jres.compiler.ICompilationResultExtension;
import com.hundsun.ares.studio.jres.compiler.IResourceCompilerFactory;
import com.hundsun.ares.studio.jres.database.constant.IDBConstant;
import com.hundsun.ares.studio.jres.database.ui.editors.DBSQLPreviewPage;
import com.hundsun.ares.studio.jres.model.database.DBModuleCommonProperty;
import com.hundsun.ares.studio.jres.model.database.DatabaseFactory;
import com.hundsun.ares.studio.ui.editor.text.TextEditorInput;

/**
 * @author gongyf
 *
 */
public class TableSpaceSQLPreviewUpdater implements IPageChangedListener {

	public static final String TABLE_SPACE_SQL_FULL= "#database.space.gensql.full#";
	private static final String SEPARATOR = "-------------------------------------------------\n";
	private static final String NEWLINE = "\n";
	
	@Override
	public void pageChanged(PageChangedEvent event) {
		if (event.getSelectedPage() instanceof DBSQLPreviewPage) {
			DBSQLPreviewPage page = (DBSQLPreviewPage) event.getSelectedPage();
			EObject eObj = page.getEditor().getInfo();
			
			StringBuffer buffer = new StringBuffer();
			
			// ����������Ҫ����IARESResource����
			Map<Object, Object> context = new HashMap<Object, Object>();
			context.put(CompileUtils.ARES_RESOURCE, page.getEditor().getARESResource());
			
			IARESProject project = page.getEditor().getARESResource().getARESProject();
			DBModuleCommonProperty property;
			try {
				property = (DBModuleCommonProperty) project.getProjectProperty().getMap().get(IDBConstant.MODULE_DATABASE_EXTEND_PROPERTY_KEY);
				property = (DBModuleCommonProperty) ObjectUtils.defaultIfNull(
						property, 
						DatabaseFactory.eINSTANCE.createDBModuleCommonProperty());
				context.put(IDBConstant.DATABASE_TYPE, property.getDatabase());
			} catch (ARESModelException e) {
				e.printStackTrace();
			} 
			appendTextCompilationResult(buffer, TABLE_SPACE_SQL_FULL, eObj, context);
			
			page.setInput(new TextEditorInput(buffer.toString()));

		}
	}
	
	private void appendTextCompilationResult(StringBuffer buffer, String type, EObject eObj, Map<Object, Object> context) {
		IResourceCompilerFactory factory = CompileManager.getInstance().getFactoryByType(type);
		if (factory != null) {

			CompilationResult result = factory.create(eObj).compile(eObj, context);
			if (result instanceof ICompilationResultExtension) {
				buffer.append(((ICompilationResultExtension) result).getTextResult());
			} else {
				buffer.append("--�޷���ʾ�������������������ı�");
			}
		} else {
			buffer.append("--�޷���ʾ��������û�ж�Ӧ�ı�����֧��");
		}
	}

}
