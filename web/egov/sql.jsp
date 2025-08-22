<%@ page contentType="text/html; charset=iso-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.egov.TransparenciaServices"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<title>Transparência - Despesas</title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<script language="javascript" src="/sol/js/scriptaculous/prototype.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/builder.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/effects.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/dragdrop.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/controls.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/slider.js"></script>
<script language="javascript" src="/sol/js/masks.js"></script>
<script language="javascript" src="/sol/js/masks2.0.js"></script>
<script language="javascript" src="/sol/js/util.js"></script>
<script language="javascript" src="/sol/js/ajax.js"></script>
<script language="javascript" src="/sol/js/shortcut.js"></script>
<script language="javascript" src="/sol/js/form.js"></script>
<script language="javascript" src="/sol/js/janela2.0.js"></script>
<script language="javascript" src="/sol/js/grid2.0.js"></script>
<link href="/sol/css/form.css" rel="stylesheet" type="text/css" />
<link href="/sol/css/grid2.0.css" rel="stylesheet" type="text/css" />
<link href="/sol/css/janela2.0.css" rel="stylesheet" type="text/css" />
<link href="/sol/css/report.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"> 
var rsm = null;
var grid = null;
var rsmColumns = [];
var groupOptions = [];

function login(msg)	{
	document.getElementById('divGrid').style.width  = (window.innerWidth-23)+'px';
	document.getElementById('divGrid').style.height = (window.innerHeight*0.61)+'px';
	document.getElementById('sql').style.width  = (window.innerWidth-23)+'px';
	document.getElementById('sql').style.height = (window.innerHeight*0.32)+'px';
	//createWindow('jLogin', {caption: 'Login', noMinimizeButton: true, noMaximizeButton: true, noCloseButton: true, width: 350, height: 190, modal:true,
	//						contentUrl: '../login.jsp'+(msg!=null? '&msg='+msg : '')});
}

function btPesquisarOnClick(content)	{
	if(content==null)	{
		// BUSCANDO
		getPage('POST', 'btPesquisarOnClick', 
				'../methodcaller?className=com.tivic.manager.egov.IptuServices&method=execSQL(sql:String,nameSpace:String)', [document.getElementById('sql'),document.getElementById('nameSpace')], true);
	}
	else {	// retorno
		try	{
			rsm = eval("("+content+")");
			rsmColumns = [];
			for(var i=0;i<rsm.columns.length; i++)	{
				if(rsm.columns[i].name)
					rsmColumns.push({label: rsm.columns[i].name, reference: rsm.columns[i].name});
				else
					rsmColumns.push({label: rsm.columns[i], reference: rsm.columns[i]});
			}
			if(rsm.columns.length==0)	{
				rsmColumns.push({label: 'Resultado', reference: 'RETORNO'});
			}
			groupOptions = rsmColumns;
			
			grid = GridOne.create('grid',{plotPlace:document.getElementById('divGrid'), resultset: rsm, columns: rsmColumns});
		}
		catch(e)	{
			alert('Erro ao tentar executar instrução! \n Erro: '+e.message+'\nConteudo: '+content);
		} 
	}
}

function btImprimirOnClick()	{
	var params = 'frameName=jDBMScontentIFrame'+
				 '&rsmName=rsm'+
				 '&rsmColumns=rsmColumns'+
				 '&groupOptions=groupOptions'+
				 '&groupColumns='+
				 '&sumGroupColumns='+
				 '&orderColumns='+
				 '&orderType=ASC'+
				 '&headerImage=../imagens/hipercred.gif'+
				 '&headerTitle=Cobresp - HiperCred'+
				 '&headerText=Resultado da Consulta'+
				 '&headerInfo=dd/MM/yyyy hh:mm|p/P'+
				 '&footerText='+
				 '&orientation=Landscape'+
				 '&footerInfo=dd/MM/yyyy hh:mm|p/P'; 
	top.showWindow('jReport', 'Relatório', 700, 430, '../dotReport/controle_impressao.jsp?'+params, false); 
}

function btnExportarOnClick() {
	if(grid)	{
		grid.exportToFile();
	}
}
</script>
<body class="body" onLoad="login();">
<div style="width: 99%;" id="Contrato" class="d1-form">
  <div style="width: 98%; height: 98%;" class="d1-body">
    <div class="d1-line" id="line0" style="height:34px; display:block">
	 <div  style="position:relative; float:left; margin-left:2px; margin-right:8px; margin-top:3px;" class="element">
	 	 <textarea id="sql" name="sql" style="position:relative; overflow:auto; float:left; margin-top:3px; border:1px solid #999999; height:200px; " class="field"></textarea>
	 </div>
	 <div  style="position:relative; float:left; margin-left:2px; margin-right:8px; margin-top:3px;" class="element">
		 <div class="element" style="position:relative; float:right; height:20px; width:150px;">
		 	<select id="nameSpace" name="nameSpace" class="select2" style="width: 150px;">
		 		<option value="IPT">IPT</option>
		 		<option value="DIV">DIV</option>
		 		<option value="ISS">ISS</option>
		 		<option value="OR8">OR8</option>
		 	</select>
		 </div>
		 <button type="button" style="position:relative; float:right; margin-right:2px; width:100px; height:22px; border:1px solid #666; cursor:default" onclick="btImprimirOnClick()">
				<table width="100%" height="12"  border="0" align="center" cellpadding="0" cellspacing="1">
				  <tr>
					<td width="16" height="16" align="center" valign="middle" class="rotuloCTEC"><img src="/sol/imagens/print16.gif" width="16" height="15"></td>
					<td align="center" valign="middle" class="rotulo"><strong>Imprimir</strong></td>
				  </tr>
				</table>
		 </button>
		 <button type="button" style="position:relative; float:right; margin-left:2px; margin-right:2px; width:100px; height:22px; border:1px solid #666; cursor:default" onclick="btnExportarOnClick()">
				<table width="100%" height="12"  border="0" align="center" cellpadding="0" cellspacing="1">
				  <tr>
					<td width="16" height="16" align="center" valign="middle" class="rotuloCTEC"><img src="/sol/imagens/export16.gif" width="16" height="15"></td>
					<td align="center" valign="middle" class="rotulo"><strong>Exportar</strong></td>
				  </tr>
				</table>
		 </button>
		 <button type="button" style="position:relative; float:right; height:22px; border:1px solid #666; width:100px; cursor:default" onclick="btPesquisarOnClick()">
				<table width="100%" height="12"  border="0" align="center" cellpadding="0" cellspacing="1">
				  <tr>
					<td width="16" height="16" align="center" valign="middle" class="rotuloCTEC"><img src="/sol/imagens/execute16.gif" width="16" height="15"></td>
					<td align="center" valign="middle" class="rotulo"><strong>Executar</strong></td>
				  </tr>
				</table>
		 </button>
	 </div>
	 <div id="divGrid" style="position:relative; overflow:auto; float:left; margin-left:2px; margin-top:3px; border:1px solid #999; background-color:FFF;" class="bodyGridResult">&nbsp;</div>
</body>
</html>
