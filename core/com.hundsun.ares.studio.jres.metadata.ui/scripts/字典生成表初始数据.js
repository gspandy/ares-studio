importPackage(java.lang);
importPackage(java.io);
importPackage(java.util);
importPackage(org.eclipse.jface.dialogs);

//�����ֵ����ɱ��ʼ����
/**
 * 1���ýű�����ԭϵͳ�������ֵ����ɱ��ʼ���ݸ��Ķ���
 * 2��δ�������õ������ȥ��ǰ��Դ���е����ݽ���SQL��������
 */
var metadataUtil = classLoader.loadClass("com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil").newInstance();
var stringUtil = classLoader.loadClass("org.apache.commons.lang.StringUtils").newInstance();

var project = res.getARESProject().getProject();
var file = "MetaData_Dict_DictInit_"+util.getCurrentDate()+".sql";

/**
 * ע�⣺·���ָ����ڲ�ͬϵͳ�²�ͬ����win��Ϊ��\������linux��Ϊ��/�����ڲ�ͬ�Ĳ���ϵͳ����ע���޸�
 */
var fileName = project.getLocation().toOSString() + "\\sql\\MetaData_Dict_DictInit_"+util.getCurrentDate()+".sql";

var sb = new StringBuffer();
var errorLogInfo = new StringBuffer();//������ʾ��Ϣ
var warninginfo = "false";//Դ�ļ�Ϊ��Ԥ���

getAllDictInfo(sb);

var status = util.genFile(fileName , sb.toString(), errorLogInfo.toString(), "utf-8", true);

//ִ�д����ļ��Ĳ���
if (!status.isOK()) {
	MessageDialog.openError("����ʧ��", status.getMessage());
} else {
	MessageDialog.openInformation(null,"���ɳɹ�", "�ļ�������ϣ�����·����" + fileName);
}

function getCentSet(rootItems,paths){
	var rootSize = rootItems.size();
	for(var i=0;i<rootSize; i++){
		
		var curpaths = new Array();
		var rootCNNameAr = new Array();
		var rootDescAr = new Array();
		
		pushInfo(paths,curpaths);
		
		var item = rootItems.get(i);
		
		curpaths.push(item.getName());
		rootCNNameAr.push(item.getChineseName());
		rootDescAr.push(item.getDescription());
		
		printCentSet(curpaths,rootCNNameAr,rootDescAr);
		getCentSet(item.getChildren(),curpaths);
	}
}//��ȡ������Ϣ

function printCentSet(paths,rootCNNameAr,rootDescAr){
	var size = paths.length;
	var j;
	var rootTree = new StringBuffer();
	for(var i =0;i<size ;i++){
		rootTree.append(paths[i]+"#");
		j=i;
	}
	var parCode = paths[size - 2];
	var treeIdx = new StringBuffer();
	treeIdx.append("#bizroot#0001#");
	treeIdx.append(rootTree);
	
	sb.append("INSERT INTO tsys_kind (kind_type,kind_code,kind_name,parent_code,tree_idx,mnemonic,remark) VALUES (");
	sb.append("'0',")
	sb.append("'"+paths[j]+"',");
	sb.append("'"+rootCNNameAr+"',");
	if("undefined".equals(parCode) || null == parCode){
		sb.append("'0001',");
	}else{
		sb.append("'"+parCode+"',");
	}
	sb.append("'"+treeIdx+"',");
	sb.append("'"+paths[j]+"',");
	sb.append("'"+rootDescAr+"');\r\n");
}//��װ����SQL���

function pushInfo(paths,curpaths){
	var size = paths.length;
	for(var i= 0;i<size;i++){
		curpaths.push(paths[i]);
	}
}//

function getAllDictInfo(sb){
	
	var rootSize = info.getRoot().getChildren().size();
	if(rootSize == 0){
		
	}else{
		var rootItems = info.getRoot().getChildren();
		var paths = new Array();
		getCentSet(rootItems,paths);
	}//������Ϣ
	
	var dictTypeSize = info.getItems().size();
	if(dictTypeSize ==0){
		//���ֵ���Ŀ��Ϣ
	}else{
		var dictType = info.getItems();
		for(var i=0; i<dictTypeSize; i++){
			var dictTypeName = dictType.get(i).getName();
			var dictTypeDesc = dictType.get(i).getDescription();
			var dictTypeCNName = dictType.get(i).getChineseName();
			var dictTypeParSize = dictType.get(i).getCategories().size();
			if(dictTypeParSize == 0){
				sb.append("INSERT INTO tsys_dict_entry (kind_code,dict_entry_code,dict_entry_name,ctrl_flag,remark) VALUES (");
				sb.append("'',");
				sb.append("'"+dictTypeName+"',");
				sb.append("'"+dictTypeCNName+"',");
				sb.append("'0',");
				sb.append("'"+dictTypeDesc+"');\r\n");
			}else{
				var dictTypePar = dictType.get(i).getCategories().get(0).getName();
				sb.append("INSERT INTO tsys_dict_entry (kind_code,dict_entry_code,dict_entry_name,ctrl_flag,remark) VALUES (");
				sb.append("'"+dictTypePar+"',");
				sb.append("'"+dictTypeName+"',");
				sb.append("'"+dictTypeCNName+"',");
				sb.append("'0',");
				sb.append("'"+dictTypeDesc+"');\r\n");
			}
		}//��Ŀ��Ϣ
		
		for(var i=0;i<dictTypeSize;i++){
			var dictItemSize = dictType.get(i).getItems().size();
			if(dictItemSize == 0){
				//�ֵ�������Ϣ
			}else{
				var dictItem = dictType.get(i).getItems();
				for(var j=0;j<dictItemSize;j++){
					var dictItemName = dictItem.get(j).getName();
					var dictItemCNName = dictItem.get(j).getChineseName();
					var dictItemPar = dictItem.get(j).getParent().getName();
					var dictItemDesc = dictItem.get(j).getDescription();
					var dictItemVal = dictItem.get(j).getValue();
					
					sb.append("INSERT INTO tsys_dict_item (dict_entry_code,dict_item_code,dict_item_name) VALUES (");
					sb.append("'"+dictItemPar+"',");
					sb.append("'"+dictItemVal+"',");
					sb.append("'"+dictItemCNName+"');\r\n");
				}
			}
		}//�ֵ�����Ϣ
	}
	
}