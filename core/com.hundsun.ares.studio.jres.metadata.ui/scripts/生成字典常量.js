importPackage(java.lang);
importPackage(java.io);
importPackage(java.util);
importPackage(org.eclipse.jface.dialogs);

//�ű����죬���û���������Java�ļ�����Ϊ�����ֵ�����Java
//2011-10-20 ����

var begin = "\r\n"+"public class DictConstant {\r\n";

var end = "}\r\n";

var path = "constant";
var project = res.getARESProject().getProject();
var resource = util.getSrcFolder(project).getFile("constant\\DictConstant.java");
var fileName = resource.getLocation().toOSString();

var metadataUtil = classLoader.loadClass("com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil").newInstance();
var stringUtil = classLoader.loadClass("org.apache.commons.lang.StringUtils").newInstance();
var sb = new StringBuffer();
var errorLogInfo = new StringBuffer();//������ʾ��Ϣ
var warninginfo = "false";//Դ�ļ�Ϊ��Ԥ���

sb.append("package "+path+";\r\n");
sb.append(begin);
sb.append("\r\n");
getAllConstants(sb);
sb.append(end);

var status = util.genFile(fileName, sb.toString(),  errorLogInfo.toString(), "utf-8", true);

if(warninginfo.equals("true")){//Ԥ���Դ�ļ������Ƿ�Ϊ��
	MessageDialog.openWarning(null,"����", "Դ�ļ�����Ϊ�գ�����д���ٽ��в�����");
}else{
	if (!status.isOK()) {
		MessageDialog.openError("����ʧ��", status.getMessage());
	} else {
		if(errorLogInfo.toString().equals("")){
			resource.refreshLocal(0, null);
			MessageDialog.openInformation(null,"���ɳɹ�", "�ļ�������ϣ�����·����" + fileName);
		}else{
			resource.refreshLocal(0, null);
			MessageDialog.openWarning(null,"���ɳɹ���Դ�ļ����ڴ���", 
				"�ļ�������ϣ�����·����" +fileName+
				"\r\n\r\n"+"�赼��JAVAԴ�ļ��д������´���\r\n" + errorLogInfo + "\r\n\r\n������Ϣ������������Ӧ�ļ�");
		}		
	}
}

function getAllConstants(sb){
	//��ȡ�����ֵ��б���Ϣ���ж���Դ�ļ��Ƿ�Ϊ��
	var dictTypeSize = info.getItems().size();
	if(0 == dictTypeSize){
		warninginfo = "true";
	}
	else{
		
		for(var i=0; i<dictTypeSize; i++){
			//��ȡ��Դ�ļ�����Ϣ
			var dictType = metadataUtil.decrypt(info.getItems().get(i), res);
			var refID = info.getItems().get(i).getRefId();
			if(stringUtil.isBlank(refID)){
				
				var dictName = dictType.getName();
				var dictCNName = dictType.getChineseName();
				
				var dictItemSize = dictType.getItems().size();
				
				if(0 == dictItemSize){
				}else{
					for(var j=0; j<dictItemSize; j++){
						var dictItem = dictType.getItems().get(j);
						var dictConstName = dictItem.getConstantName();
						var dictValue = dictItem.getValue();
						var dictInfo = dictItem.getChineseName();
						if(stringUtil.isBlank(dictConstName) || stringUtil.isBlank(dictValue)){
						}else{							
							if(0==j){
								sb.append("\r\n\t/** "+dictName +" "+dictCNName+" **/\r\n");
							}
							sb.append("\tpublic static final String ");
							sb.append(dictConstName + " = ");							
							sb.append("\"" + dictValue + "\"" + ";");
							sb.append("\t// " + dictInfo + "\r\n");
						}
					}
				}

			}else{
				try{
					var dictRefTypeResPair = metadataUtil.resolve(info.getItems().get(i), res);
					if(null != dictRefTypeResPair){
						
						var dictRefType = dictRefTypeResPair.first;
						var dictRefName = dictRefType.getName();
						var dictRefCNName = dictRefType.getChineseName();
						var dicRefItems = dictRefType.getItems();
						var dictRefItemSize = dicRefItems.size();
						
						if(0 == dictRefItemSize){
						}
						else if(1 == dictRefItemSize){
							var dictRefItem = dicRefItems.get(0);
							var dictRefConstName = dictRefItem.getConstantName();
							var dictRefValue = dictRefItem.getValue();
							var dictRefInfo = dictRefItem.getChineseName();
							if(stringUtil.isBlank(dictRefConstName) || stringUtil.isBlank(dictRefValue)){
							}else{
								sb.append("\r\n\t/** �ֵ���["+info.getItems().get(i).getName() +"]��������  "+refID+" **/\r\n");
								sb.append("\t//public static final String ");
								sb.append(dictRefConstName + " = ");							
								sb.append("\"" + dictRefValue + "\"" + ";");
								sb.append("\t// " + dictRefInfo + "\r\n");
							}
						}
						else{
							for(var k=0; k<dictItemSize; k++){
								var dictRefItem = dicRefItems.get(k);
								var dictRefConstName = dictRefItem.getConstantName();
								var dictRefValue = dictRefItem.getValue();
								var dictRefInfo = dictRefItem.getChineseName();
								if(stringUtil.isBlank(dictRefConstName) || stringUtil.isBlank(dictRefValue)){
								}else{							
									if(0==k){
										sb.append("\r\n\t/** �ֵ���["+info.getItems().get(i).getName() +"]��������  "+refID+" **/\r\n");
									}
									sb.append("\t//public static final String ");
									sb.append(dictRefConstName + " = ");							
									sb.append("\"" + dictRefValue + "\"" + ";");
									sb.append("\t// " + dictRefInfo + "\r\n");
								}
							}
						}
						dictRefItemSize = 0;
					}else{
						dictERName = info.getItems().get(i).getName();
						errorLogInfo.append("���������ֵ�IDΪ["+dictERName+"]���õ�["+refID+"]������\r\n");
						sb.append("\r\n");
					}
				}
				catch (e){
					dictERName = info.getItems().get(i).getName();
					errorLogInfo.append("���������ֵ�IDΪ["+dictERName+"]ѭ������\r\n");
					sb.append("\r\n");
				}
			}
		}
	}
}//�����ֵ�����Java