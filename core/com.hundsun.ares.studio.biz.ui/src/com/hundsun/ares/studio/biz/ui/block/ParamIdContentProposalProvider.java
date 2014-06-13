/**
 * Դ�������ƣ�ParamIdContentProposalProvider.java
 * �������Ȩ���������ӹɷ����޹�˾ ��Ȩ����
 * ϵͳ���ƣ�JRES Studio
 * ģ�����ƣ�com.hundsun.ares.studio.biz.ui
 * ����˵����$desc
 * ����ĵ���
 * ���ߣ�sundl
 */
package com.hundsun.ares.studio.biz.ui.block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hundsun.ares.studio.biz.Parameter;
import com.hundsun.ares.studio.biz.constants.IBizRefType;
import com.hundsun.ares.studio.biz.core.BizUtil;
import com.hundsun.ares.studio.core.IARESProject;
import com.hundsun.ares.studio.jres.metadata.constant.IMetadataRefType;
import com.hundsun.ares.studio.jres.model.metadata.MetadataItem;
import com.hundsun.ares.studio.model.reference.ReferenceInfo;
import com.hundsun.ares.studio.reference.ReferenceManager;
import com.hundsun.ares.studio.ui.cellEditor.AresContentProposalProvider;
import com.hundsun.ares.studio.ui.cellEditor.IContentProposalProviderHelper;

/**
 * @author sundl
 *
 */
public class ParamIdContentProposalProvider extends AresContentProposalProvider{

	private IARESProject project;
	
    public ParamIdContentProposalProvider(IContentProposalProviderHelper helper, IARESProject project) {
    	super(helper);
    	this.project = project;
    }

	/* (non-Javadoc)
	 * @see com.hundsun.ares.studio.ui.cellEditor.AresContentProposalProvider#updateContent(java.lang.Object)
	 */
	@Override
	public void updateContent(Object element) {
		if (element instanceof Parameter) {
			Parameter p = (Parameter) element;
			switch(p.getParamType()) {
    			case STD_FIELD:
    				// ��׼�ֶ����͵Ĳ�������ʾ��׼�ֶ�
    				setupContents(IMetadataRefType.StdField);
    				break;
    			case OBJECT:
    				// �������͵Ĳ���������ж����׼�ֶΣ�����ʾ�����׼�ֶ�
    				if (BizUtil.hasStdObjList(project)) {
    					setupContents(IBizRefType.Std_Obj);
    				} 
    				break;
    			case PARAM_GROUP:
    				//setUpObjects();
    				break;
    			default:
    				setInput(new Object[0]);
    				break;
			}
			
		}

	}
	
	private void setupContents(String refType) {
		List<ReferenceInfo> referenceInfoList =ReferenceManager.getInstance().getReferenceInfos(project, refType ,true);
		if (referenceInfoList==null || referenceInfoList.size()==0) {
			setInput(new Object[0]);
		} else {
			// ���˵��ظ��ı�׼�ֶζ���
			Set<String> errorSet = new HashSet<String>();
			Set<String> processedSet = new HashSet<String>();
			Map<String, Object> processedObjects = new HashMap<String, Object>();
			for (ReferenceInfo refInfo : referenceInfoList) {
				Object object =refInfo.getObject();
				if (object instanceof MetadataItem) {
					MetadataItem item = (MetadataItem) object;
					if (!errorSet.contains(item.getName())) {
						if (processedSet.contains(item.getName())) {
							processedSet.remove(item.getName());
							errorSet.add(item.getName());
							processedObjects.remove(item.getName());
						} else {
							processedSet.add(item.getName());
							processedObjects.put(item.getName(), refInfo);
						}
					}
				}
			}
			setInput(processedObjects.values().toArray());
		}			
	}

//	private void setUpObjects() {
//		List<ReferenceInfo> referenceInfoList =ReferenceManager.getInstance().getReferenceInfos(project, IBizRefType.Object ,true);
//		if (referenceInfoList==null || referenceInfoList.size()==0) {
//			setInput(new Object[0]);
//		} else {
//			List<Object> input = new ArrayList<Object>();
//			for (ReferenceInfo refInfo : referenceInfoList) {
//				input.add(refInfo.getObject());
//			}
//			setInput(input.toArray());
//		}
//	}
}
