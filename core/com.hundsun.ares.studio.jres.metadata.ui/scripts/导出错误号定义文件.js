importPackage(java.lang);
importPackage(java.io);
importPackage(java.util);
importPackage(org.eclipse.jface.dialogs);

var project = res.getARESProject().getProject();
var resource = util.getSrcFolder(project).getFile("fragment.bizErrorMessage");

var metadataUtil = classLoader.loadClass("com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil").newInstance();
var stringUtil = classLoader.loadClass("org.apache.commons.lang.StringUtils").newInstance();

var sb = new StringBuffer();
var errorLogInfo = new StringBuffer();//������ʾ��Ϣ
var warninginfo = "false";

sb.append("#The encoding of this file is UTF-8\r\n")

genAllErrorNoProperty(sb);
var fileName = resource.getLocation().toOSString();
var status = util.genFile(fileName , sb.toString(), errorLogInfo.toString(), "utf-8", true);

//������Դ�ļ�����������Ӧ�ļ���
if(warninginfo.equals("true")){
	MessageDialog.openWarning(null,"����","Դ�ļ�����Ϊ�գ�����д���ٽ��в�����");
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
					"�ļ�������ϣ�����·����" + fileName +
					"\r\n\r\n"+"�赼������Ŷ����ļ���Դ�ļ��д������´���\r\n" + errorLogInfo+
					"\r\n\r\n������Ϣ������������Ӧ�ļ�");
		}
	}
}

function genAllErrorNoProperty(sb) {
	
	var errInfoItems = info.getItems().size();
	
	if(errInfoItems == 0){
		warninginfo = "true";
	}
	else{
		for(var i = 0 ; i < errInfoItems ; i++){
			
			var errItem = metadataUtil.decrypt(info.getItems().get(i), res);
			var refId = info.getItems().get(i).getRefId();//���������
			
			var errorNo = errItem.getNo();//����ű��
			var errorID = errItem.getName();//�����ID
			
			var j = i+1;
			
			if(stringUtil.isBlank(refId)){	//�жϴ���������Ƿ����
				
				

				var errorMes = errItem.getMessage();//��ȡ�������Ϣ
				
				if(stringUtil.isBlank(errorID)){
					sb.append("#���󣺵�ǰ��Դ��["+j+"]����¼IDΪ��\r\n");
					errorLogInfo.append("���󣺵�ǰ��Դ��["+j+"]����¼IDΪ��\r\n");
				}
				else if(stringUtil.isBlank(errorNo)){
					sb.append("#����IDΪ["+errorID+"]�Ĵ���ű��Ϊ��\r\n");
					errorLogInfo.append("����IDΪ["+errorID+"]�Ĵ���ű��Ϊ��\r\n");
				}else if(stringUtil.isBlank(errorMes)){
					sb.append("#���󣺴���ű��["+errorNo+"]�Ĵ�����ϢΪ��\r\n");
					errorLogInfo.append("���󣺴���ű��["+errorNo+"]�Ĵ�����ϢΪ��\r\n");
				}else{
					if(errorNo.search("^(\\-?)\\d*$")==0){
						//��װ��Դ�ļ�
						sb.append(errorNo+"=");
						sb.append(util.toUnicode(errorMes == null ? "" : errorMes, false));
						sb.append("\r\n");
					}else{
						sb.append("#����IDΪ["+errorID+"]�Ĵ���ű��["+errorNo+"]Ϊ�������������\r\n");
						errorLogInfo.append("����IDΪ["+errorID+"]�Ĵ���ű��["+errorNo+"]Ϊ�������������\r\n");
					}
				}
			}else{
				try {
					var errRefItem = metadataUtil.decrypt(info.getItems().get(i), res);
					
					if(null != errRefItem){
						
						var errorRefID = errRefItem.getName();//�����ID
						var errorRefNO = errRefItem.getNo();//����ű��

						sb.append("#������Դ����ǰIDΪ["+errorID+"]�Ĵ������Դ������Դ��Ӧ�Ĵ���ű��Ϊ["+errorRefNO+"]\r\n");
					}else{
						
						sb.append("#����IDΪ["+errorID+"]���õ���Դ["+refId+"]������\r\n");
						errorLogInfo.append("#����IDΪ["+errorID+"]���õ���Դ["+refId+"]������\r\n");
					}
				} catch (e) {
					exception = "true";
				}
			}
		}
	}
}
