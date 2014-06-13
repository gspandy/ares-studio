importPackage(java.lang);
importPackage(java.io);
importPackage(java.util);
importPackage(org.eclipse.jface.dialogs);

var begin = 
"\r\n" +
"public class Constant {\r\n";

var end = 
"}\r\n";

var path = "constant";
var project = res.getARESProject().getProject();
var resource = util.getSrcFolder(project).getFile("constant\\Constant.java");
var fileName = resource.getLocation().toOSString();

var metadataUtil = classLoader.loadClass("com.hundsun.ares.studio.jres.model.metadata.util.MetadataUtil").newInstance();
var stringUtil = classLoader.loadClass("org.apache.commons.lang.StringUtils").newInstance();

var sb = new StringBuffer();
var errorLogInfo = new StringBuffer();//������ʾ��Ϣ
var warninginfo = "false";//Դ�ļ�Ϊ��Ԥ���

var exception = "false";//����ѭ�������쳣

sb.append("package "+path+";\r\n");
sb.append("import java.util.Date;\r\n");
sb.append(begin);
sb.append("\r\n");
getAllConstants(sb);
sb.append(end);

var status = util.genFile(fileName, sb.toString(),  errorLogInfo.toString(), "utf-8", true);

if(warninginfo.equals("true")){//Ԥ���Դ�ļ������Ƿ�Ϊ��
	MessageDialog.openWarning(null,"����", "Դ�ļ�����Ϊ�գ�����д���ٽ��в�����");
}else{
	
	if(exception.equals("true")){//����ѭ�����õ��쳣
		MessageDialog.openError(null,"������Ϣ��ʾ", "��Ҫ����Java�ļ���Դ�ļ��д���ѭ������");
		
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
						"\r\n\r\n"+"�赼��JAVA�����������Դ�ļ��д������´���\r\n" + 
						errorLogInfo + "\r\n\r\n������Ϣ������������Ӧ�ļ�");
			}
		}
	}
}

function getAllConstants(sb){
	
	var constantItem = info.getItems().size();
	
	if(constantItem == 0){
		warninginfo = "true";//Դ�ļ�Ϊ�ոı侯����Ϣֵ
	}else{
		for(var i = 0 ; i < constantItem ; i++){	
			
			var constItem = metadataUtil.decrypt(info.getItems().get(i), res);
			var constDesc = constItem.getDescription();//��ע��Ϣ
			var constName = constItem .getName();//����ID
			var refId = info.getItems().get(i).getRefId();
			
			var j = i+1;

			//1�����ж��û�����ID�Ƿ�Ϊ��
			//2��Ȼ���ж��Ƿ��������
			
			if(stringUtil.isBlank(constName)){
				sb.append("\t//���󣺵�ǰ��Դ��["+j+"]����¼IDΪ��\r\n");
				errorLogInfo.append("���󣺵�ǰ��Դ��["+j+"]����¼IDΪ��\r\n");
			}else {
				if(stringUtil.isBlank(refId)){
					
					
					var constValue = constItem .getValue();//����ֵ
					var constLen = constItem.getLength();//����
					var constpre = constItem.getPrecision();//����
					
					var constDataType = constItem.getDataType();//��׼��������
					var constDataTypeId = constItem.getDataTypeId();//��׼��������
					
					if(stringUtil.isBlank(constDataTypeId)){ //�ж����õı�׼���������Ƿ�Ϊ��
						
						sb.append("\t//�����û�����IDΪ["+constName+"]���õı�׼��������Ϊ��\r\n");
						errorLogInfo.append("�����û�����IDΪ["+constName+"]���õı�׼��������Ϊ��\r\n");
						
					}else{
						
						var constType = constDataType.getRealType("java", constLen, constpre);// ��ȡ��Ӧ�ı�׼�������͵���ʵ��Java��������
						
						if(stringUtil.isBlank(constValue) ){
							sb.append("\t//�����û�����IDΪ["+constName+"]�ĳ���ֵΪ��\r\n");
							errorLogInfo.append("�����û�����IDΪ["+constName+"]�ĳ���ֵΪ��\r\n");
						}else{
							if(stringUtil.isBlank(constType)){
								sb.append("\t//�����û�����IDΪ["+constName+"]�ı�׼���������޶�Ӧ��ʵJAVA��������\r\n");
								errorLogInfo.append("�����û�����IDΪ["+constName+"]�ı�׼���������޶�Ӧ��ʵJAVA��������\r\n");
							}else if(constType.equals("String")){
								sb.append("\tpublic static final ");
								sb.append(constType + "  ");
								sb.append(constName + " = ");
								sb.append("\"" + constValue + "\"" + ";");
								sb.append("\t// " + constDesc + "\r\n");
								
							}else if(constType.equals("Date")){
								sb.append("\tpublic static final ");
								sb.append(constType + "  ");
								sb.append(constName + " = ");
								sb.append("new Date(\"" + constValue + "\");");
								sb.append("\t// " + constDesc + "\r\n");
								
							}else{
								sb.append("\tpublic static final ");
								sb.append(constType + "  ");
								sb.append(constName + " = ");
								sb.append(constValue + ";");				
								sb.append("\t// " + constDesc + "\r\n");
							}			
						}			
					}
				}else{
					try{
						var constRefItem = metadataUtil.decrypt(info.getItems().get(i), res);
						
						if(constRefItem != null){
							
							var constName = constRefItem.first.getName();//����ID
							var constValue = constRefItem.first.getValue();//����ֵ
							var constLen = constRefItem.first.getLength();//����
							var constpre = constRefItem.first.getPrecision();//����
							var constDataType = constRefItem.first.getDataType();//��׼��������
							
							if(stringUtil.isBlank(constDataType)){
								sb.append("\t//�����û�����IDΪ["+constName+"]���õı�׼��������Ϊ��\r\n");
								errorLogInfo.append("�����û�����IDΪ["+constName+"]���õı�׼��������Ϊ��\r\n");
							}else{
								var constType = constDataType.getRealType("java", constLen, constpre);// ��ȡʵ�ʵ�Java��������
								
								if(stringUtil.isBlank(constValue) ){
									sb.append("\t//�����û�����IDΪ["+constName+"]�ĳ���ֵΪ��\r\n");
									errorLogInfo.append("�����û�����IDΪ["+constName+"]�ĳ���ֵΪ��\r\n")
								}	else{
									
									if(constType.equals("String")){
										sb.append("\tpublic static final ");
										sb.append(constType + "  ");
										sb.append(constName + " = ");
										sb.append("\"" + constValue + "\"" + ";");
										sb.append("\t// " + constDesc + "\r\n");
										
									}else if(constType.equals("Date")){
										sb.append("\tpublic static final ");
										sb.append(constType + "  ");
										sb.append(constName + " = ");
										sb.append("new Date(\"" + constValue + "\");");
										sb.append("\t// " + constDesc + "\r\n");
										
									}else{
										sb.append("\tpublic static final ");
										sb.append(constType + "  ");
										sb.append(constName + " = ");
										sb.append(constValue + ";");				
										sb.append("\t// " + constDesc + "\r\n");
										
									}
								}
							}
						}else{
							var constName = info.getItems().get(i).getName();//����ID
							sb.append("\t//���󣺳���IDΪ["+constName+"]���û���������["+refId+"]������\r\n");
							errorLogInfo.append("���󣺳���IDΪ["+constName+"]���û���������["+refId+"]������\r\n");
						}
					}catch(e){
						exception = "true";
					}
				}
			}
		}
	}
}
