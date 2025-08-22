<meta http-equiv="pragma" content="no-cache">
<%@page import="java.util.*" %>
<%@page import = "org.rawsocket.jspupload.*" %>
<%@page import="sol.util.RequestUtilities"%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="/sol/css/form.css" rel="stylesheet" type="text/css" />
<script>
function init()	{
	var file = document.getElementById('fileName');
	if(file.focus)
		file.focus();
}

function btCarregarOnClick(formatos) {
	if(formatos!='' && !document.getElementById('fileName').value.toUpperCase().match(new RegExp(formatos+'$'))){
		alert('Formato de arquivo inválido.\nFormatos válidos: '+formatos.replace(/\|/g, ', '));
		return;
	}
	document.getElementById('nmArquivo').value = document.getElementById('fileName').value;
	if(document.getElementById('fileName').value=='')	{
		alert('Informe o nome do arquivo!');
		return;
	}
	document.form.cdAcao.value = '1';
	document.form.submit();
}
</script>
</head>
<%
	try {
		FileUpload fileUpload = new FileUpload(request);
		fileUpload.upload();
		int cdAcao = RequestUtilities.getParameterAsInteger(request, "cdAcao", 0, fileUpload);
		String nmArquivo = RequestUtilities.getParameterAsString(request, "nmArquivo", "", fileUpload, true);
		String idSession = RequestUtilities.getParameterAsString(request, "idSession", "", fileUpload, true);
		String idContentLoad = RequestUtilities.getParameterAsString(request, "idContentLoad", "", fileUpload, true);
		String idField = RequestUtilities.getParameterAsString(request, "idField", "", fileUpload, true);
		String returnFunction = RequestUtilities.getParameterAsString(request, "returnFunction", "", fileUpload, true);
		String formatosValidos = RequestUtilities.getParameterAsString(request, "formatosValidos", "", fileUpload, true);
		if (cdAcao==1) {
			HashMap<?,?> mimeParts = fileUpload.getMimeParts();
			MimeBodyPart part = (MimeBodyPart)mimeParts.get("fileName");
			byte[] bytesFile = part==null ? null : part.getBytes();
			session.setAttribute(idSession, bytesFile);
			%><script>
				function afterLoad()	{
					<%if(idContentLoad!=null && !idContentLoad.equals(""))	{%> 
					var contentLoad = parent.document.getElementById('<%=idContentLoad%>');
					if (contentLoad != null) {
						if (contentLoad.src)
							contentLoad.src = '../preview_imagem.jsp?idSession=<%=idSession%>';
					}
					<%}%>
					if('<%=returnFunction%>'!='')	{
						eval('<%=returnFunction+"(null, \""+nmArquivo.replaceAll("\\\\", "/")+"\")"%>');
					}
					<%if(idField!=null && idField.equals(""))	{%> 
					var field = parent.document.getElementById('<%=idField%>');
					if (field != null) {
						field.value = '<%=bytesFile%>';
					}
					<%}%>
					parent.closeWindow('jLoadFile');
				}
			</script><%
		}%>
<body class="body" onLoad="<%=cdAcao==1 ? "afterLoad();" : "init();"%>">
<form action="load_file.jsp" method="post" enctype="multipart/form-data" name="form" target="_self">
<div class="d1-form">
  <input name="idSession" id="idSession" type="hidden" value="<%=idSession%>">
  <input name="cdAcao" id="cdAcao" type="hidden" value="0">
  <input name="idContentLoad" id="idContentLoad" type="hidden" value="<%=idContentLoad%>">
  <input name="idField" id="idField" type="hidden" value="<%=idField%>">
  <input name="nmArquivo" id="nmArquivo" type="hidden" value="">
  <input name="returnFunction" id="returnFunction" type="hidden" value="<%=returnFunction%>">
  <div style="height: 100px;" class="d1-body">
  	<div class="d1-line" id="line6">
		<div class="element">
    		<label class="caption">Informe o nome do arquivo:</label>
    		<input type="file" id="fileName" name="fileName" class="disabledField" size="62" style="width:400px; height:20px; border:1px solid #999; margin-bottom:3px;" />
		</div>
	</div>
  	<div class="d1-line" id="line6" style="text-align:right;">
		<button id="btnConciliar" type="button" title="Gravar informações da Carteira" onClick="btCarregarOnClick('<%=formatosValidos%>');" style="width:80px; border:1px solid #999999" class="toolButton">
					<img src="/sol/imagens/positive16.gif" height="16" width="16"/>&nbsp;Carregar
		</button><button id="btnCancelar" type="button" title="Não é o mesmo movimento" onClick="parent.closeWindow('jLoadFile');" style="width:80px; border:1px solid #999999" class="toolButton">
			<img src="/sol/imagens/negative16.gif" height="16" width="16"/>&nbsp;Cancelar
		</button>
  	</div>
  </div>
</div>
</form>
</body>
<%
	}
	catch(Exception e) {
		e.printStackTrace(System.out);
	}
%>
</html>