<%@page import="sol.util.RequestUtilities"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="shortcut, form" compress="false" />
<script>
<%
try {
	String msg = RequestUtilities.getParameterAsString(request, "msg", "");
%>

var lockedEmpresa            = true;
var lockedLocalArmazenamento = true;

function init()	{
	enableTabEmulation();
	$('cdLocalArmazenamento').nextElement = $('btnOk');
	loadEmpresas();
	$('cdEmpresa').focus();
}

function loadEmpresas(content){
	if (content==null) {
		if(parent.rsmEmpresas!=null)	{
			loadEmpresas('{lines:[]}');
			return;
		}
		lockedEmpresa = true;
		getPage("GET", "loadEmpresas", 
				"../methodcaller?className=com.tivic.manager.grl.EmpresaServices"+
				"&method=getAllAtivos()");
	}
	else {
		var empresas = parent.rsmEmpresas!=null ? parent.rsmEmpresas : null;
		if (empresas==null)
			try {empresas = eval("(" + content + ")")} catch(e) {};
		loadOptionsFromRsm($('cdEmpresa'), empresas, {fieldValue: 'CD_EMPRESA', fieldText:'NM_FANTASIA'});
		lockedEmpresa = false;
		loadLocaisArmazenamento();
	}
}

function loadLocaisArmazenamento(content)	{
	if (content==null) {
		lockedLocalArmazenamento = true;
		var cdEmpresa = $('cdEmpresa').value;
		getPage("GET", "loadLocaisArmazenamento", 
				"../methodcaller?className=com.tivic.manager.alm.LocalArmazenamentoServices"+
				"&method=findLocaisArmazenamentoEmpresa(const " + cdEmpresa + ":int)");
	}
	else {
		try {
			while ($('cdLocalArmazenamento').length > 0)
				$('cdLocalArmazenamento').remove(0);
		}
		catch(e) {}
		var locais = null;
		try {locais = eval("(" + content + ")")} catch(e) {}
		for (var i=0; locais!=null && i<locais.lines.length; i++) {
			var option = document.createElement('OPTION');
			option.setAttribute('value', locais.lines[i]['CD_LOCAL_ARMAZENAMENTO']);
			option.appendChild(document.createTextNode(locais.lines[i]['NM_LOCAL_ARMAZENAMENTO']));
			$('cdLocalArmazenamento').appendChild(option);
		}
		lockedLocalArmazenamento = false;
		if($('cdEmpresa').options.length==1 && $('cdLocalArmazenamento').options.length==1)
			btnOkOnClick();
	}
}

var user = {};
function btnOkOnClick() {
	if (!lockedEmpresa && !lockedLocalArmazenamento) {
		if (parent.$('NM_EMPRESA')!=null)	{
			parent.document.getElementById('NM_EMPRESA').innerHTML = getTextSelect('cdEmpresa');
		}
		parent.document.getElementById('cdEmpresa').value = $('cdEmpresa').value;
		parent.document.getElementById('cdLocalArmazenamento').value = $('cdLocalArmazenamento').value;
		parent.closeWindow('jLogin');
	}
}
</script>
</head>
<body class="body" onload="init()">
<div style="width: 350px; background-color: #FFFFFF !important;" class="d1-form">
  <div style="width: 350px; height: 200px; margin-top:10px" class="d1-body">
    <img style="float:left; margin: 0px 10px 0px 0px" src="imagens/local_armazenamento48.gif" width="48" height="48">
	<div class="d1-line" id="line0" style="height:125px; width:1px; background-color:#666666; float:left; margin:0px 5px 0px 0px">
    </div>
	<div class="d1-line" id="line1">
      <div id="divContentLine1" style="width: 140px; height:20px;" class="element">
        <label id="textLabel" class="caption" style="white-space:normal; font-weight:bold; color:#333333"><%=msg%></label>
      </div>
    </div>
	<div class="d1-line" id="line2">
      <div style="width: 275px; height:40px" class="element">
        <label class="caption">Empresa</label>
        <select style="width: 275px;:" class="select" id="cdEmpresa" name="cdEmpresa" onChange="loadLocaisArmazenamento()">
        </select>
    </div>
    <div class="d1-line" id="line3">
      <div style="width: 275px; height:40px" class="element">
        <label class="caption">Local de Armazenamento</label>
        <select style="width: 275px;" class="select" id="cdLocalArmazenamento" name="cdLocalArmazenamento" onChange="">
        </select>
	</div>
    <div class="d1-toolButtons" id="line4">
      <div style="width: 275px;" class="element">
		<button style="width:60px" onClick="if (btnOkOnClick) btnOkOnClick();" id="btnOk" class="toolButton">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td align="center" class="caption">OK</td>
          </tr>
        </table>
		</button>  
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