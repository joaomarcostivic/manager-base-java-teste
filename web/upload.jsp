<%@page import="sol.util.RequestUtilities"%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta http-equiv="pragma" content="no-cache">
<% 
	try {
		session.removeAttribute("FILE_UPLOAD_STATS"); 
		
		String classname = RequestUtilities.getParameterAsString(request, "classname", "");		
		String method = RequestUtilities.getParameterAsString(request, "method", "");
		String args = RequestUtilities.getParameterAsString(request, "args", "");
		String addNameToArgs = RequestUtilities.getParameterAsString(request, "addNameToArgs", "false");
		
		session.setAttribute("FILE_UPLOAD_CLASSNAME", classname); 
		session.setAttribute("FILE_UPLOAD_METHOD", method);
		session.setAttribute("FILE_UPLOAD_ARGS", args);
		session.setAttribute("ADD_NAME_TO_ARGS", addNameToArgs);
		
		String extensions = RequestUtilities.getParameterAsString(request, "extensions", "");
		String callback = RequestUtilities.getParameterAsString(request, "callback", "");
%>
<style>
	.info {
		font-family:Geneva, Arial, Helvetica, sans-serif;
		font-size:12px;
		font-weight:bold;
		overflow:hidden;
		height:15px;
	}
</style>
<%@taglib uri="tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, progressbar" compress="false" />
<script>
var callback =  <%=(!callback.equals(""))?callback:"null"%>;
var progressBar;
function init()	{
	if($('datafile').focus)
		$('datafile').focus();
}
function btCarregarOnClick() {
	if($('datafile').value=='')	{
		createTempbox("jMsg", {width: 200, height: 50, message: "Informe o nome do arquivo", boxType: "ALERT", time: 2000});
		return;
	}
	var extensions = '<%=extensions%>';
	if(extensions && $('datafile').value.toUpperCase().search(/<%=extensions%>$/)==-1){
		createTempbox("jMsg", {width: 200, height: 50,
							   message: 'Formato de arquivo inválido.\nFormatos válidos: '+extensions.replace(/\|/g, ', '), boxType: "ALERT", time: 2000});
		return;	
	}
	document.form.submit();
	startStatusCheck();	
}

var process;
function killStatusCheck(content){
	if(content==null){
		getPage("GET", "killStatusCheck", "UploadStatus?status=end");
	}
	else{
		createTempbox("jMsg", {width: 100, height: 50, message: "Finalizado", boxType: "INFO", time: 1000});
		if(callback){
			setTimeout(callback, 1000);
		}
	}
}
function startStatusCheck() {
	process = setInterval(function(){
			getPage("GET", "displayStatusUpload", "UploadStatus");
		}, 4000);
	$('nmArquivo').innerHTML = $('datafile').value;
	createWindow('jStatus', {top:0, width: 400, height: 100, noDropContent: true, contentDiv: 'statusForm', noDrag: true, modal: true, noTitle: true});
	progressBar = ProgressBar.create('progressBar', {width: 390,
							    height: 16,
							    plotPlace: 'progressBar',
							    backColor: '#FFFFFF',
							    foreColor: '#000000',
							    initialProgress: 0,
							    minProgress: 0,
							    maxProgress: 100});
	$('dsTempoEstimado').innerHTML = '-';
	$('dsVelocidade').innerHTML = '-';
	$('dsDadosEnviados').innerHTML = '-';

}

function displayStatusUpload(content){
	var status;
	try {
		status = eval('(' + content + ')');
	} catch(e) {
		
	}
	
	if(status){
		switch(status.status){
			case 0:
			case 1:
			case 2:
				progressBar.setProgress(status.percentComplete);
				$('dsTempoEstimado').innerHTML = status.timeLeft;
				$('dsVelocidade').innerHTML = status.transferVelocity;
				$('dsDadosEnviados').innerHTML = status.bytesProcessed + status.unitBytesProcessed + ' / ' + status.sizeTotal + status.unitSizeTotal;
				break;
			case 3:
				clearInterval(process);
				createTempbox("jMsg", {width: 300,
								height: 50,
								message: "ERRO ao enviar arquivo",
								tempboxType: "ERROR",
								time: 3000});
				killStatusCheck();
				break;
			case 4:
				progressBar.setProgress(100);
				$('dsTempoEstimado').innerHTML = '-';
				$('dsVelocidade').innerHTML = '-';
				$('dsDadosEnviados').innerHTML = status.sizeTotal + status.unitSizeTotal;
				clearInterval(process);
				killStatusCheck();
				break;
		}
	}
	else{
		clearInterval(process);
		createTempbox("jMsg", {width: 300,
						height: 50,
						message: "ERRO ao enviar arquivo",
						tempboxType: "ERROR",
						time: 3000});
		killStatusCheck();
	}
}
</script>
</head>
<body class="body" onLoad="init();">
<iframe id='target_upload' name='target_upload' src='' style='display: none'></iframe>
<form action="FileUpload" method="post" enctype="multipart/form-data" name="form" target="target_upload">
<!--<input name="classname" type="hidden" value="<%=classname%>">
<input name="method" type="hidden" value="<%=method%>">
<input name="args" type="hidden" value="<%=args%>">
<input name="addNameToArgs" type="hidden" value="<%=addNameToArgs%>">-->
<div class="d1-form">
 <div style="height: 100px;" class="d1-body">
  	<div class="d1-line" style="margin:5px 0 5px 0">
		<div class="element" style="width:387px; ">
			<label class="caption">Selecione o arquivo:</label>
			<input type="file" id="datafile" name="datafile" class="disabledField" size="60" style="width:387px; height:20px; border:1px solid #999; margin-bottom:3px;" />
		</div>
	</div>
  	<div class="d1-line" style="text-align:right;">
		<button type="button" onClick="btCarregarOnClick();" style="width:80px; border:1px solid #999999" class="toolButton">
			<img src="/sol/imagens/positive16.gif" height="16" width="16" style="margin-bottom:-3px"/>&nbsp;Enviar
		</button><button id="btnCancelar" type="button" onClick="parent.closeWindow('jUpload');" style="width:80px; border:1px solid #999999" class="toolButton">
			<img src="/sol/imagens/negative16.gif" height="16" width="16" style="margin-bottom:-3px"/>&nbsp;Cancelar
		</button>
  	</div>
  </div>
</div>
</form>
<!--FORM STATUS -->
<div id="statusForm" style="display:none">
  <div class="d1-form" style="height:80px;">
	  <div class="d1-body">
		<div class="d1-line">
			<div style="width: 390px;" class="element">
				<label class="caption">Enviando</label>
				<div class="info" id="nmArquivo"></div>
			</div>
		</div>
		<div id="progressBar" style="margin:5px 0 5px 0"></div>
		<div class="d1-line">
			<div style="width: 100px;" class="element">
				<label class="caption">Tempo estimado</label>
				<div class="info" id="dsTempoEstimado">-</div>
			</div>
			<div style="width: 100px;" class="element">
				<label class="caption">Velocidade</label>
				<div class="info" id="dsVelocidade">-</div>
			</div>
			<div style="width: 130px;" class="element">
				<label class="caption">Enviado</label>
				<div class="info" id="dsDadosEnviados">-</div>
			</div>
		</div>
	  </div>
   </div>
</div>

</body>
<%
	}
	catch(Exception e) {
		e.printStackTrace(System.out);
	}
%>
</html>