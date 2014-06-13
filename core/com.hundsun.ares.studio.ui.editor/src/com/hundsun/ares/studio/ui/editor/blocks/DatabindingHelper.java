/**
 * Դ�������ƣ�DatabindingHelper.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.ui
 * ����˵����JRES Studio�Ľ���չ�ֻ�����ܺͱ༭��ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.ui.editor.blocks;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;

/**
 * @deprecated ���������д������ȷ��������
 *
 */
public class DatabindingHelper {

	/**
	 * @deprecated
	 * @param context
	 * @param control
	 * @param obj
	 * @param attr
	 * @param targetToModel
	 * @param modelToTarget
	 */
	public static void emfBind(DataBindingContext context, 
								Control control, 
								EObject obj, 
								EAttribute attr, 
								UpdateValueStrategy targetToModel,
								UpdateValueStrategy modelToTarget) {
		IObservableValue txtAuthorObserveWidget = SWTObservables.observeText(control, SWT.FocusOut);
		IObservableValue txtAuthorObserveValue = EMFObservables.observeValue(obj, attr);
		context.bindValue(txtAuthorObserveWidget, txtAuthorObserveValue, targetToModel, modelToTarget);
	}
	
	
	/**
	 * ��ö�ٵ�ֵ�󶨵�combo�������б���
	 * @param context
	 * @param control
	 * @param eenum
	 * @deprecated
	 */
	public static void emfComboBind(DataBindingContext context
			,Control control
			,EEnum eenum){
		context.bindList(SWTObservables.observeItems(control)
				, new WritableList(eenum.getELiterals(), null)
		        , new UpdateListStrategy(), 
				new UpdateListStrategy().setConverter(new EnumToStringConventor()));
	}
	
	/**
	 * ��EMF���԰󶨵�combo�ؼ�
	 * @param context
	 * @param control  �ؼ�
	 * @param obj      ģ��
	 * @param attr     ���� 
	 * @param targetToModel ���浽ģ�Ͳ���
	 * @param modelToTarget ģ�͵��������
	 * @deprecated
	 */
	public static void emfComboBind(DataBindingContext context, 
			Control control, 
			EObject obj, 
			EAttribute attr,
			UpdateValueStrategy targetToModel,
			UpdateValueStrategy modelToTarget) {
		
		IObservableValue comboObserveWidget = SWTObservables.observeText(control);
		IObservableValue comboObserveValue = EMFObservables.observeValue(obj, attr);
		context.bindValue(comboObserveWidget,comboObserveValue,targetToModel,modelToTarget);
	}
	
	/**
	 * @deprecated
	 * @param context
	 * @param control
	 * @param obj
	 * @param attr
	 * @param targetToModel
	 * @param modelToTarget
	 */
	public static void emfSpinnerBind(DataBindingContext context, 
			Control control, 
			EObject obj, 
			EAttribute attr,
			UpdateValueStrategy targetToModel,
			UpdateValueStrategy modelToTarget) {
		
		IObservableValue spinnerIPrecisionObserveWidget = SWTObservables.observeSelection(control);
		IObservableValue spinnerIPrecisionObserveValue = EMFObservables.observeValue(obj, attr);
		context.bindValue(spinnerIPrecisionObserveWidget,spinnerIPrecisionObserveValue, targetToModel,modelToTarget);
		
	}
	

	
}
