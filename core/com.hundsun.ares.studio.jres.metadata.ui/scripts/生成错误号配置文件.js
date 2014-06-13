importPackage(java.lang);
importPackage(java.io);
importPackage(org.eclipse.jface.dialogs);

var scriptUtils = classLoader.loadClass("com.hundsun.ares.studio.jres.core.util.ScriptUtils").newInstance();

var project = res.getARESProject();
var resource = util.getSrcFolder(project.getProject()).getFile("constant\\bizErrorMessageFormat.properties");			
var fileName = resource.getLocation().toOSString();	

var sb = new StringBuffer();
genAllErrorNoProperty(sb);

var status = scriptUtils.genFile(fileName, sb.toString(),"", "utf-8", true);
if (!status.isOK()) {
	MessageDialog.openError(null, "����ʧ��", status.getMessage());
} else {
	resource.refreshLocal(0, null);
	MessageDialog.openInformation(null, "���ɳɹ�", "�ļ�������ϣ�����·����" + fileName);
}

function genAllErrorNoProperty(sb) {
	var errorInfo = getErrorListInfo(project);
	if(errorInfo != null){
		for(var i = 0;i < errorInfo.getItems().size();i++){
			var sort = errorInfo.getItems().get(i);
			genErrorNoProperty(sb, sort);
		}
	}
}

function getErrorListInfo(project){
	var res = project.findResource("errorno", "errorno");
	if(res != null){
		return res.getInfo();
	}
	return null;
}

function genErrorNoProperty(sb,errorNo){
	var errorno = errorNo.getName();
	if (errorno != null && !errorno.equals("")) {
		sb.append(errorNo.getNo());
		sb.append("=");
		var errorinfo = errorNo.getMessage();
		sb.append(scriptUtils.toUnicode(errorinfo == null ? "" : errorinfo, false));
		sb.append("\r\n");
	}
}
