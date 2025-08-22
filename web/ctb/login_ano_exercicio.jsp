<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.security.StatusPermissionActionUser"%>
<%@page import="sol.security.User"%>
<%@page import="java.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="shortcut, form" compress="false" />
<script>
var lockedEmpresa = true;
var lockedAnoExercicio = true;

function loadEmpresas(content){
	if (content==null) {
		lockedEmpresa = true;
		getPage("GET", "loadEmpresas", 
				"../methodcaller?className=com.tivic.manager.grl.EmpresaServices"+
				"&method=getAll()");
	}
	else {
		var empresas = null;
		try {empresas = eval("(" + content + ")")} catch(e) {}
		for (var i=0; empresas != null && i < empresas.lines.length; i++) {
			var option = document.createElement('OPTION');
			option.setAttribute('value', empresas.lines[i]['CD_EMPRESA']);
			option.appendChild(document.createTextNode(empresas.lines[i]['NM_FANTASIA']));
			$('cdEmpresa').appendChild(option);
		}
		lockedEmpresa = false;
		loadExercicios();
	}
}

function loadExercicios(content){
	if (content==null) {
		lockedAnoExercicio = true;
		var cdEmpresa = $('cdEmpresa').value;
		getPage("GET", "loadExercicios", 
				"../methodcaller?className=com.tivic.manager.ctb.EmpresaServices"+
				"&method=loadExercicios(const " + cdEmpresa + ":int)");
	}
	else {
	var rsmExercicios = null;
	try {rsmExercicios = eval("(" + content + ")")} catch(e) {}
	loadOptionsFromRsm($('nrAnoExercicio'), rsmExercicios, {fieldValue: 'nr_ano_exercicio', fieldText:'nr_ano_exercicio', setDefaultValueFirst: true});
/*		
		try {
			while ($('nrAnoExercicio').length > 0)
				$('nrAnoExercicio').remove(0);
		}
		catch(e) {}
		for (var i=0; rsmExercicios != null && i < rsmExercicios.lines.length; i++) {
			var option = document.createElement('OPTION');
			option.setAttribute('value', rsmExercicios.lines[i]['NR_ANO_EXERCICIO']);
			option.setAttribute('value', rsmExercicios.lines[i]['NR_ANO_EXERCICIO']);
			option.appendChild(document.createTextNode(rsmExercicios.lines[i]['NR_ANO_EXERCICIO']));
			$('nrAnoExercicio').appendChild(option);
		}
*/		
		lockedAnoExercicio = false;
	}
}

function btnOkOnClick() {
	if (!lockedEmpresa && !lockedAnoExercicio) {
		parent.$('cdEmpresa').value = $('cdEmpresa').value;
		parent.$('nrAnoExercicio').value = $('nrAnoExercicio').value;
		var optionAnoExercicio = getOptionSelect('nrAnoExercicio');
		parent.$('stExercicio').value = optionAnoExercicio == null || optionAnoExercicio.register == null ? -1 : optionAnoExercicio.register['ST_EXERCICIO'];
		parent.closeWindow('jLogin');
	}
}
</script>
</head>
<%
	try {
		String msg = RequestUtilities.getParameterAsString(request, "msg", "");
%>
<body class="body">
	<div style="width: 250px;" class="d1-form">
		<div style="width: 240px; height: 130px; margin-top:10px" class="d1-body">
			<img style="float:left; margin: 0px 10px 0px 0px" src="imagens/ano_exercicio48.gif" width="48" height="48">
			<div class="d1-line" id="line0" style="height:110px; width:1px; background-color:#666666; float:left; margin:0px 5px 0px 0px">
			</div>
			<div class="d1-line" id="line1">
				<div id="divContentLine1" style="width: 173px; height:20px;" class="element">
					<label id="textLabel" class="caption" style="white-space:normal; font-weight:bold; color:#333333"><%=msg%></label>
				</div>
			</div>
			<div class="d1-line" id="line2">
				<div style="width: 175px; height:35px" class="element">
					<label class="caption">Empresa	</label>
					<select style="width: 175px;" class="select" id="cdEmpresa" name="cdEmpresa" onChange="loadExercicios()">
					</select>
				</div>
			</div>
			<div class="d1-line" id="line3">
				<div style="width: 70px; height:40px" class="element">
					<label class="caption">Ano exercício </label>
					<select style="width: 70px;" class="select" id="nrAnoExercicio" name="nrAnoExercicio" onChange="">
					</select>
				</div>
			</div>
			<div class="d1-toolButtons" id="line4">
				<div style="width: 175px;" class="element">
					<button style="width:65px" onClick="if (btnOkOnClick) btnOkOnClick();" id="btnOk" class="toolButton">
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
	}
%>
<script >
	enableTabEmulation();
	$('nrAnoExercicio').nextElement = $('btnOk');
	loadEmpresas();
	$('cdEmpresa').focus();
</script>
</html>