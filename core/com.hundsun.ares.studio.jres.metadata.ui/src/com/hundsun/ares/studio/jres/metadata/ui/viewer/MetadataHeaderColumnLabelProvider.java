/**
 * Դ�������ƣ�MetadataHeaderColumnLabelProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.jres.metadata.ui
 * ����˵����Ԫ�����û��༭��UIչ����ع���
 * ����ĵ���
 * ���ߣ�
 */
package com.hundsun.ares.studio.jres.metadata.ui.viewer;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hundsun.ares.studio.core.IARESResource;
import com.hundsun.ares.studio.jres.metadata.ui.MetadataUI;
import com.hundsun.ares.studio.jres.model.metadata.BizPropertyConfig;
import com.hundsun.ares.studio.jres.model.metadata.BusinessDataType;
import com.hundsun.ares.studio.jres.model.metadata.ConstantItem;
import com.hundsun.ares.studio.jres.model.metadata.DictionaryType;
import com.hundsun.ares.studio.jres.model.metadata.ErrorNoItem;
import com.hundsun.ares.studio.jres.model.metadata.MetadataCategory;
import com.hundsun.ares.studio.jres.model.metadata.StandardDataType;
import com.hundsun.ares.studio.jres.model.metadata.StandardField;
import com.hundsun.ares.studio.jres.model.metadata.TypeDefaultValue;
import com.hundsun.ares.studio.ui.editor.viewers.EObjectColumnLabelProvider;
import com.hundsun.ares.studio.ui.editor.viewers.IEStructuralFeatureProvider;

/**
 * �ṩԪ������Ŀͼ��
 * @author gongyf
 *
 */
public class MetadataHeaderColumnLabelProvider extends EObjectColumnLabelProvider {

	private static Image IMG_FOLDER = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
	
	private static Image IMG_TYPE_DEFAULT = MetadataUI.getDefault().getImage("icons/full/obj16/defaultValueFile.png");
	
	private static Image IMG_BIZ_TYPE = MetadataUI.getDefault().getImage("icons/full/obj16/bizTypeFile.png");
	
	private static Image IMG_CONST = MetadataUI.getDefault().getImage("icons/full/obj16/cnstFile.png");
	
	private static Image IMG_DICT= MetadataUI.getDefault().getImage("icons/full/obj16/dictFile.png");
	
	private static Image IMG_ERRORNO = MetadataUI.getDefault().getImage("icons/full/obj16/errornoFile.png");
	
	private static Image IMG_STD_FIELD = MetadataUI.getDefault().getImage("icons/full/obj16/stdFieldFile.png");
	
	private static Image IMG_STD_TYPE = MetadataUI.getDefault().getImage("icons/full/obj16/stdTypeFile.png");
	
	private static Image IMG_BIZ_CONFIG = MetadataUI.getDefault().getImage("icons/full/obj16/bizconfig.png");
	
	private IARESResource resource;
	
	/**
	 * @param attribute
	 */
	public MetadataHeaderColumnLabelProvider(EAttribute attribute , IARESResource resource) {
		super(attribute);
		this.resource = resource;
	}

	/**
	 * @param attributeProvider
	 */
	public MetadataHeaderColumnLabelProvider(
			IEStructuralFeatureProvider attributeProvider) {
		super(attributeProvider);
	}

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.jres.ui.viewers.EObjectColumnLabelProvider#doGetImage(java.lang.Object)
	 */
	@Override
	protected Image doGetImage(Object element) {
		if (element instanceof MetadataCategory) {
			return IMG_FOLDER;
		} else if (element instanceof TypeDefaultValue) {
			return IMG_TYPE_DEFAULT;
		}
		else if (element instanceof BusinessDataType) {
			return IMG_BIZ_TYPE;
		}
		else if (element instanceof ConstantItem) {
			return IMG_CONST;
		}
		else if (element instanceof DictionaryType) {
			return IMG_DICT;
		}
		else if (element instanceof ErrorNoItem) {
			return IMG_ERRORNO;
		}
		else if (element instanceof StandardField) {
			return IMG_STD_FIELD;
		}
		else if (element instanceof StandardDataType) {
			return IMG_STD_TYPE;
		}
		else if(element instanceof BizPropertyConfig){
			return IMG_BIZ_CONFIG;
		}
		/*
		 * TODO#�����߼�#��Ҷ��#�� #����#����״̬ #���ʱ�� #������(�������հ��к�ע����) #��ʱ(��ȷ������) #��ʾID��ͼ��
		 *
		 * ����Ԫ���ݵ����ͷ��ض��ڵ�ͼ�꣬��7��Ԫ�������ͣ����ص�ͼ�������ǵ��ļ�����ͼ��
		 */
			
		return super.doGetImage(element);
	}
}
