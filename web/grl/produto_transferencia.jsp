<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="org.apache.poi.hssf.record.PageBreakRecord.Break"%>
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@page import="sol.security.StatusPermissionActionUser" %>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="sol.util.Jso"%>
<%@page import="java.sql.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, report, shortcut, report" compress="false"/>
<%
	try {
		int cdEmpresa          = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);		
		Empresa empresa        = EmpresaDAO.get(cdEmpresa);
%>
<script language="javascript">
//
function init()	{		
	if ($('cdEmpresa') != null && $('cdEmpresa').tagName.toLowerCase()=='select')
		loadOptionsFromRsm($('cdEmpresa'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
	$('cdEmpresa').value = '<%=cdEmpresa%>';
	//
	if ($('cdEmpresaCliente') != null && $('cdEmpresaCliente').tagName.toLowerCase()=='select'){
		loadOptionsFromRsm($('cdEmpresaCliente'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia', executeAfterLoad: tpEmpresaCliente()});						
			tpEmpresaCliente();	
	}
	//
	if($('cdTabelaPreco'))		
		loadTabelaPreco(null);
	//
	$('lgTransfereProduto').checked = true;
	enableTabEmulation();
}
 
function loadTabelaPreco(content) {
	if (content==null) {
		$('cdTabelaPreco').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carrengando..."));
		$('cdTabelaPreco').appendChild(newOption);

		getPage("GET", "loadTabelaPreco", 
				"METHODCALLER_PATH?className=com.tivic.manager.adm.TabelaPrecoServices"+
				"&method=getAll(const <%=cdEmpresa%>:int)", null, true);			
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		$('cdTabelaPreco').length = 0;
		loadOptionsFromRsm($('cdTabelaPreco'), rsm, {fieldValue: 'cd_tabela_preco', fieldText:'nm_tabela_preco'});
	}	
}

function loadTabelaPrecoCliente(content) {
	if (content==null) {				
		$('cdTabelaPrecoCliente').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carrengando..."));
		$('cdTabelaPrecoCliente').appendChild(newOption);
		
		getPage("GET", "loadTabelaPrecoCliente", 
				"METHODCALLER_PATH?className=com.tivic.manager.adm.TabelaPrecoServices"+
				"&method=getAll(const "+$('cdEmpresaCliente').value+":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		$('cdTabelaPrecoCliente').length = 0;
		loadOptionsFromRsm($('cdTabelaPrecoCliente'), rsm, {fieldValue: 'cd_tabela_preco', fieldText:'nm_tabela_preco'});
	}
}

function tpEmpresaCliente() {
	if($('cdTabelaPrecoCliente'))
		loadTabelaPrecoCliente(null);
}

function btnTransferirOnClick(){	
	if($('cdEmpresa').value == $('cdEmpresaCliente').value){
		showMsgbox('Manager', 250, 20, 'A Empresa Cliente não pode ser igual à Empresa Padrão.');
		$('cdEmpresaCliente').value = 0;
		tpEmpresaCliente();
	}
	//
	if($('cdEmpresaCliente').value == 0)
		showMsgbox('Manager', 350, 20, 'Selecione uma Empresa Cliente.');
	//
	if($('lgTransfereProduto').checked == true )
		btnTransferirProdutosOnClick(null)
	else		
		if($('cdTabelaPrecoCliente').value > 0)
			btnTransferirTabelasOnClick(null);
}

function btnTransferirProdutosOnClick(content){
	if(content==null){
		createConfirmbox("dialog", {caption: "Transferir Produtos", width: 300, height: 100, 
			message: "Tem certeza que deseja transferir os produtos para a Empresa Cliente?",
			boxType: "QUESTION",
			positiveAction: function() {
				getPage("POST", "btnTransferirProdutosOnClick", 
			            "../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices"+
			            "&method=transfereProduto(const "+$('cdEmpresa').value+":int, const "+$('cdEmpresaCliente').value+":int)");}});		
	}
    else{    	    	
    	var result = processResult(content, 'Produtos enviados com sucesso!');
        if(result.code > 0)	{
        	$('lgTransfereProduto').checked = false;        
        	showMsgbox('Manager', 250, 30, 'Produtos enviados com sucesso!');        	
        }
    }
}

function btnTransferirTabelasOnClick(content){
	if(content==null){
		createConfirmbox("dialog", {caption: "Transferir Tabela", width: 300, height: 100, 
			message: "Verifique se as tabelas selecionadas são correspondentes. Caso estejam corretas, pode continuar.",
			boxType: "QUESTION",
			positiveAction: function() {				
				getPage("POST", "btnTransferirTabelasOnClick", 
			            "../methodcaller?className=com.tivic.manager.adm.ProdutoServicoPrecoServices"+
			            "&method=transfereTabela(const "+$('cdTabelaPreco').value+":int, const "+$('cdTabelaPrecoCliente').value+":int)");}});
	}else{		
		var result = processResult(content, 'Preços transferidos com sucesso!');
        if(result.code > 0)	{
        	showMsgbox('Manager', 250, 30, 'Preços transferidos com sucesso!');
        	tpEmpresaCliente();	
        }
	}
}

function habilitaTabelasOnChange(){
	if($('lgTransfereProduto').checked == true){		
		$('cdTabelaPreco').disabled 	   = true;
		$('cdTabelaPrecoCliente').disabled = true;
	}else{			
		$('cdTabelaPreco').disabled 	   = false;
		$('cdTabelaPrecoCliente').disabled = false;
	}
}

function btnValidaNotaOnClick(){	
	if($('cdEmpresa').value == $('cdEmpresaCliente').value){
		showMsgbox('Manager', 250, 20, 'A Empresa Cliente não pode ser igual a Empresa Padrão.');
		$('cdEmpresaCliente').value = 0;
		tpEmpresaCliente();
	}
	//
	if($('cdEmpresaCliente').value == 0)
		showMsgbox('Manager', 350, 20, 'Selecione uma Empresa Cliente.');
	//		
	if(trim($('nrDocumentoSaida').value) == "")
		showMsgbox('Manager', 350, 20, 'Digite o Número do documento de Saída');
	else
		btnTransferirNotaOnClick(null);	
}	

function btnTransferirNotaOnClick(content){
	if(content==null){	
		createConfirmbox("dialog", {caption: "Transferir Notas entre Empresas Parceiras", width: 300, height: 100, 
			message: "Tem certeza que deseja transferir a Nota Nr. "+ $('nrDocumentoSaida').value + " para a Empresa Cliente?",
			boxType: "QUESTION",
			positiveAction: function() {
				getPage("POST", "btnTransferirNotaOnClick", 
			            "../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices"+
			            "&method=transfereNota(const "+$('cdEmpresaCliente').value+":int, const "+$('nrDocumentoSaida').value+":String)");}});		
	}
    else{
    	var result = processResult(content, 'Nota Transferida com sucesso!');    	
        if(result.code > 0){        	
        	showMsgbox('Manager', 250, 30, 'Nota Transferida com sucesso!');
        }    	
    }    
}
</script>
</head>
<body class="body" onload="init();">
<div style="width: 500px;" id="produtEmpresa" class="d1-form">
	<div style="width: 495px; height: 170px;" class="d1-body">
		<div style="border:1px solid #999; padding:2px 2px 2px 2px; height: 163px; margin-bottom: 2px; width:490px; float: left;">
			<div class="d1-line" id="line0">		 
				<div style="width: 245px;" class="element">
                	<label class="caption" for="cdEmpresa">Empresa Padrão</label>
                	<select style="width:240px" disabled="disabled" class="select" id="cdEmpresa" name="cdEmpresa">
                	</select>
            	</div>
            	<div style="width: 245px;" class="element">
                	<label class="caption" for="cdEmpresaCliente">Empresa Cliente</label>
                	<select onchange="tpEmpresaCliente()" style="width:243px;" class="select" id="cdEmpresaCliente" name="cdEmpresaCliente">
                		<option value="0">Selecione...</option>                        
                	</select>
            	</div>      				 
			</div>					
			<div style="border:1px solid #999; padding:2px 2px 2px 2px; height: 80px; margin-bottom: 2px; width:482px; float: left; margin-top: 3px">			
				<div style="padding: 2px;" class="d1-line" id="line1">		 
					<div style="width: 10px;" class="element"> 
						<input onchange="habilitaTabelasOnChange();" type="checkbox" id="lgTransfereProduto" name="lgTransfereProduto" class="caption"/>
            		</div>            		
            		<div style="width: 400px; margin-left: 10px" class="element"  > 						
						<label class="caption">Transferir Produtos para Empresa Cliente</label> 
            		</div>
				</div>
				<div style="margin-top: 3px;" class="d1-line" id="line2">		 
					<div style="width: 240px;" class="element"> 
                		<label class="caption" for="cdTabelaPreco">Tabela Padrão</label>
                		<select style="width:236px" class="select" id="cdTabelaPreco" name="cdTabelaPreco">
                		</select>
            		</div>
            		<div style="width: 240px;" class="element">
                		<label class="caption" for="cdTabelaPrecoCliente">Tabela Cliente</label>
                		<select style="width:239px;" class="select" id="cdTabelaPrecoCliente" name="cdTabelaPrecoCliente">                		                	          
                		</select>
            			<div style="width:110px; padding:5px 0 0 0; float:right" class="element">
            				<button id="btnTransferir" title="Confirmar Transferência" onclick="btnTransferirOnClick();" style="margin-bottom:2px; width:107px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="../adm/imagens/tabela_preco_add16.gif" height="16" width="16"/>Confirmar</button>
          				</div>      				 
            		</div>
            	</div>			
			</div>
			<div style="border:1px solid #999; padding:2px 2px 2px 2px; height: 35px; margin-bottom: 2px; width:200px; float: left; margin-top: 1px">
				<div style="width:90px; padding: 0px 0 0 0; float:left" class="element">
					<label class="caption" for="DocumentoSaida">N&deg; Documento</label>
	        		<input style="text-transform: uppercase; width: 80px;" lguppercase="true" logmessage="Nº Documento" class="field2" idform="nrDocumentoSaida" reference="nr_documento_saida" datatype="STRING" maxlength="15" id="nrDocumentoSaida" name="nrDocumentoSaida" type="text">
				</div>
				<div style="width:110px; padding:13px 0 0 0; float:right" class="element">
					<button id="btnTransferirNota" title="Confirmar Transferência de Nota" onclick="btnValidaNotaOnClick();" style="margin-bottom:2px; width:107px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="../adm/imagens/confirmar16.gif" height="16" width="16"/>Transferir</button>
        		</div>	
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
</html>