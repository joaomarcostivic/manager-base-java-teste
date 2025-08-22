<%@page contentType="text/html; charset=iso-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta http-equiv="pragma" content="no-cache"/>
<title>Manager - Enterprise Resource Planning</title>
<%@taglib uri="tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, shortcut, report, flatbutton, corners" compress="false"/>
<%@page import="sol.util.Result"%>
<%@page import="sol.util.RequestUtilities"%>
<%
	String reportFile = RequestUtilities.getAsString(request, "report", "");
	String fileName   = session.getServletContext().getRealPath("/")+reportFile;
	String output     = "";
	String className  = RequestUtilities.getAsString(request, "className", "");
	String method     = RequestUtilities.getAsString(request, "method", "");
	String execute    = RequestUtilities.getAsString(request, "execute", "");
	String objects    = RequestUtilities.getAsString(request, "objects", "");
	int tpExport      = RequestUtilities.getAsInteger(request, "tpExport", -1);
	Result result     = null;
	// Se o tpExport for menor do que zero é porque é a chamada da janela principal
	//if(tpExport >= 0)
	//	result = com.tivic.manager.util.Util.processReport(request, response, fileName, tpExport);
%>
<script language="javascript">
var _PDF  = 0;
var _HTML = 1;
var _RTF  = 2;
var _XLS  = 3;
var _PPT  = 4;
var _XML  = 5;
function init()	{
	var frameHeight;
	if (self.innerHeight)
		frameHeight = self.innerHeight;
	else if (document.documentElement && document.documentElement.clientHeight)
		frameHeight = document.documentElement.clientHeight;
	else if (document.body)
		frameHeight = document.body.clientHeight;
		
	var frameWidth;
	if (self.innerWidth)
		frameWidth = self.innerWidth;
	else if (document.documentElement && document.documentElement.clientWidth)
		frameWidth = document.documentElement.clientWidth;
	else if (document.body)
		frameWidth = document.body.clientWidth;
	$('toolBar').style.height = frameHeight+'px';
	$('iFrame').style.height = (frameHeight-8)+'px';
	$('iFrame').style.width  = (frameWidth-90)+'px';
	//	
	toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'vertical',
								         buttons: [{id: 'btPDF', img: 'imagens/pdf64.png', label: 'PDF', imagePosition: 'top', width: 65, height: 65, onClick: function() {btnGerarOnClick(_PDF);}},
										   		   {separator: 'vertical'},
										   		   {id: 'btHTML', img: 'imagens/html64.png', label: 'HTML', imagePosition: 'top', width: 65, height: 65, onClick: function() {btnGerarOnClick(_HTML);}},
										   		   {separator: 'vertical'},
										   		   {id: 'btRTF', img: 'imagens/word64.png', label: 'Documento', imagePosition: 'top', width: 65, height: 65, onClick: function() {btnGerarOnClick(_RTF);}},
										   		   {separator: 'vertical'},
								                   //{id: 'btXLS', img: 'imagens/excel64.png', label: 'Planilha', imagePosition: 'top', width: 65, height: 65, onClick: function() {btnGerarOnClick(_XLS);}},
								                   //{separator: 'vertical'},
								                   //{id: 'btPPT', img: 'imagens/ppt64.png', label: 'PowerPoint', imagePosition: 'top', width: 65, height: 65, onClick: function() {btnGerarOnClick(_PPT);}},
								                   //{separator: 'vertical'},
								                   //{id: 'btXML', img: 'imagens/xml64.png', label: 'XML', imagePosition: 'top', width: 65, height: 65, onClick: function() {btnGerarOnClick(_XML);}},
								                   //{separator: 'vertical'},
								                   {id: 'btFechar', img: 'imagens/fechar64.png', label: 'Fechar', imagePosition: 'top', width: 65, height: 65, onClick: function() {parent.closeWindow('jReport')}}]});

	if(<%=tpExport<0%>)
		showBotoes();
	btnGerarOnClick(_PDF); 		
}

function showBotoes() {
	// createWindow('jToolBar', {noTitle: true, noDrag: true, noBringUp: true, top: 0, left: 0, width: 720, height: 93, contentDiv: $('toolBar')});
}

function btnGerarOnClick(tpExport) {
	$('tpExport').value = tpExport;
	$('form').submit();
}
</script>
</head>
<body style="margin:0px; background:<%=(result!=null)?"#CCC":"#F5F5F5"%>;" onload="init()">
<%	if (result!=null) {
		if (result.getCode()<0) {
			output = result.getMessage();
			response.reset();
			response.setContentType("text/html");
			out.write(result.getMessage());
		}
	}
	else	{%>
	<form name="form" id="form" method="post" target="iFrame">
	<input id="file" name="file" type="hidden" value="<%=reportFile%>"/>
	<input id="className" name="className" type="hidden" value="<%=className%>"/>
	<input id="method" name="method" type="hidden" value="<%=method%>"/>
	<input id="execute" name="execute" type="hidden" value="<%=execute%>"/>
	<input id="objects" name="objects" type="hidden" value="<%=objects%>"/>
	<input id="tpExport" name="tpExport" type="hidden" value=""/>
	</form>
	<iframe id="iFrame" name="iFrame" style="margin-right: 4px; margin-top: 3px; height:70px; width:70px; border:1px solid #666; float: right; background-color: #CCC;"></iframe>
	<div id="toolBar" style="height:70px; width:70px; border:0 solid; float: left;"></div>
<%} %>
</body>
</html>
