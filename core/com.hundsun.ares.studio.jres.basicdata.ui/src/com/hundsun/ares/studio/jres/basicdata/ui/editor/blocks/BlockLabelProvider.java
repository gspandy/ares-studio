/**
* <p>Copyright: Copyright (c) 2012</p>
* <p>Company: �������ӹɷ����޹�˾</p>
*/
package com.hundsun.ares.studio.jres.basicdata.ui.editor.blocks;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hundsun.ares.studio.jres.basicdata.core.basicdata.MasterSlaveLinkTable;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.MasterSlaveTable;
import com.hundsun.ares.studio.jres.basicdata.core.basicdata.SingleTable;
import com.hundsun.ares.studio.ui.validate.IProblemPool;
import com.hundsun.ares.studio.ui.validate.KeyParameter;

/**
 * @author wangxh
 *
 */
public class BlockLabelProvider extends ColumnLabelProvider{
	IProblemPool pool;
	private static ImageDescriptor IMG_DEC_ERROR = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_DEC_FIELD_ERROR);
	private static ImageDescriptor IMG_DEC_WARNING = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_DEC_FIELD_WARNING);
	
	private static Image IMG_ERROR = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
	private static Image IMG_WARNING = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);

	public BlockLabelProvider(IProblemPool pool) {
		super();
		this.pool = pool;
	}
	@Override
	public String getText(Object element) {
		if(element instanceof SingleTable){
			return "��ά��" + "       " + ((SingleTable)element).getUrl();
		}else if(element instanceof MasterSlaveTable){
			return "���ӱ�" + "       " + ((MasterSlaveTable)element).getUrl();
		}else if(element instanceof MasterSlaveLinkTable){
			return "���ӹ�����" + "   " + ((MasterSlaveLinkTable)element).getUrl();
		}
		return super.getText(element);
	}
	@Override
	final public Image getImage(Object element) {
		Image image = super.getImage(element);
		// �����������ͼ��ģ�����Ҫ���ϱ��
		
		Diagnostic diagnostic = getDiagnostic(element);
		if (diagnostic != null) {
			if (image == null) {
				switch (diagnostic.getSeverity()) {
				case Diagnostic.ERROR:
					return IMG_ERROR;
				case Diagnostic.WARNING:
					return IMG_WARNING;
				}
			} else {
				DecorationOverlayIcon icon = null;
				switch (diagnostic.getSeverity()) {
				case Diagnostic.ERROR:
					icon = new DecorationOverlayIcon(image, IMG_DEC_ERROR, IDecoration.BOTTOM_LEFT);
					break;
				case Diagnostic.WARNING:
					icon = new DecorationOverlayIcon(image, IMG_DEC_WARNING, IDecoration.BOTTOM_LEFT);
					break;
				}
				if (icon != null) {
					return ExtendedImageRegistry.INSTANCE.getImage(icon);
				} else {
					return image;
				}
				
			}
		}

		return image;
	}
	
	private Diagnostic getDiagnostic(Object element) {
		if(pool != null){
			Object[] objs = pool.getProblem(new KeyParameter(element));
			if(objs != null && objs.length >0){
				return (Diagnostic) objs[0];
			}
		}
		return null;
	}
	
	@Override
	public String getToolTipText(Object element) {
		Diagnostic diagnostic = getDiagnostic(element);
		if(diagnostic != null){
			return diagnostic.getMessage();
		}
		return super.getToolTipText(element);
	}
	
}
