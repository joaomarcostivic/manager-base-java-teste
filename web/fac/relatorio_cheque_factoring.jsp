<%@ page contentType="text/html; charset=iso-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="java.util.*"%>
<%@page import="sol.util.*" %>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.seg.*"%>
<security:registerForm idForm="formRelatorioContaReceber"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, grid2.0, toolbar, form, filter, calendario, janela2.0, report" compress="false"/>
<%
	GregorianCalendar hoje = new GregorianCalendar();
	String dtInicial = RequestUtilities.getParameterAsString(request, "dtInicial", Util.formatDateTime(hoje, "dd/MM/yyyy"));
	String dtFinal 	 = RequestUtilities.getParameterAsString(request, "dtFinal", Util.formatDateTime(hoje, "dd/MM/yyyy"));
	boolean callFind = RequestUtilities.getParameterAsInteger(request, "callFind", 0)==1;
	int cdEmpresa 	 = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	int cdPessoa 	 = RequestUtilities.getParameterAsInteger(request, "cdPessoa", 0);
	com.tivic.manager.grl.Empresa empresa = com.tivic.manager.grl.EmpresaDAO.get(cdEmpresa);
%>
<script language="javascript" src="../js/adm_report.js"></script>
<script language="javascript">
var rsmContaReceber = null;
var situacaoTitulo  = <%=sol.util.Jso.getStream(TituloCreditoServices.situacao)%>;
var gridContaReceber;
var formBaixaContaReceber = null;
var registro = null;

function init()	{
    enableTabEmulation();
	
    ToolBar.create('toolBar', {plotPlace: 'toolBarGrid',
							   orientation: 'vertical',
				       	       buttons: [{id: 'btnPesquisar', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar', title: 'Pesquisar registros', onClick: btnPesquisarOnClick, width: 80},{separator: 'vertical'},
										 {id: 'btnImprimir', img: '/sol/imagens/print24.gif', label: 'Imprimir', title: 'Imprimir dados', onClick: btnImprimirOnClick, width: 80},{separator: 'vertical'}]});
    
    ToolBar.create('toolBar2', {plotPlace: 'toolBarGrid2',
		   orientation: 'horizontal',
	       buttons: [{id: 'btnBaixaContaReceber', img: '../adm/imagens/recebimento16.gif', label: 'Receber', title: 'Efetuar recebimento do cheque... [Ctrl + B]', onClick: btnLancarFaturamentoOnClick, width: 280},{separator: 'horizontal'},
	                 {id: 'btnResgate', img: '../fac/imagens/repassar16.gif', label: 'Resgatar', title: 'Resgatar cheque selecionado', onClick: btnResgateOnClick, width: 280},{separator: 'horizontal'},
	       			 {id: 'btnCompensar', img: '../fac/imagens/compensar16.png', label: 'Compensar', title: 'Compensar cheque selecionado', onClick: btnCompensarChequeOnClick, width: 280}]});
    ToolBar.create('toolBar3', {plotPlace: 'toolBarGrid3',
		   orientation: 'horizontal',
	       buttons: [{id: 'btnProrrogar', img: '../fac/imagens/prorrogacao16.gif', label: 'Prorrogar', title: 'Prorrogar o cheque selecionado', onClick: btnEscolherNovaDataOnClick, width: 280},{separator: 'horizontal'},
	       			 {id: 'btnRecuperacao', img: '../fac/imagens/recuperar16.png', label: 'Recuperar', title: 'Recuperar o cheque selecionado', onClick: btnRecuperarChequeOnClick, width: 280},{separator: 'horizontal'},
	       			 {id: 'btnDevolucao', img: '../fac/imagens/devolvido16.gif', label: 'Devolver', title: 'Devolver cheque selecionado', onClick: btnDevolverChequeOnClick, width: 280}]});
    ToolBar.create('toolBar4', {plotPlace: 'toolBarGrid4',
		   orientation: 'horizontal',
	       buttons: [{id: 'btnDeleteContaReceber', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir cheque [Ctrl + D]', onClick: btnDeleteContaReceberOnClick, width: 280},{separator: 'horizontal'},
	                 {id: 'btnCancelarContaReceber', img: '/sol/imagens/negative16.gif', label: 'Cancelar', title: 'Cancelar cheque [Ctrl + K]', onClick: btnCancelarContaReceberOnClick, width: 280},{separator: 'horizontal'},					 
	                 {id: 'btnPerda', img: '../fac/imagens/perdido16.png', label: 'Perda', title: 'Dar como perdido o cheque selecionado', onClick: btnPerderChequeOnClick, width: 280}]});

    var dtDataMask = new Mask($("dtInicial").getAttribute("mask"));
    dtDataMask.attach($("dtInicial"));
    dtDataMask.attach($("dtFinal"));

  	addShortcut('ctrl+d', function(){ if (!document.getElementById('btnDeleteContaReceberOnClick').disabled) btnDeleteContaReceberOnClick() });
  	addShortcut('ctrl+b', function(){ if (!document.getElementById('btnBaixaContaReceberOnClick').disabled) btnBaixaContaReceberOnClick(null, false,false)});
  	addShortcut('ctrl+k', function(){ if (!document.getElementById('btnCancelarContaReceberOnClick').disabled) btnCancelarContaReceberOnClick() });
	
    loadOptionsFromRsm($('cdEmpresa'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
	loadOptions($('stTitulo'), <%=Jso.getStream(TituloCreditoServices.situacao)%>);
    $('nmPessoa').focus();
	$('cdEmpresa').value     = <%=cdEmpresa%>;
	$('cdEmpresa').disabled  = <%=cdEmpresa > 0%>;
	$('cdEmpresa').className = '<%=cdEmpresa > 0 ? "disabledSelect" : "select"%>';
	
	if(<%=callFind%>)	{
		btnPesquisarOnClick(null);
	}
	$('nmPessoa').focus();
	createGrid(null);
}

function formValidation(){
	var fields = [];
	if(parseInt($('tpData').value, 10) >= 0) {
		fields.push([$("dtInicial"), 'Informe', VAL_CAMPO_DATA_OBRIGATORIO]);
		fields.push([$("dtFinal"), 'Informe', VAL_CAMPO_DATA_OBRIGATORIO]);
	}
	return validateFields(fields, true, 'Campos obrigatórios ou com conteúdo inválido.', '');
}

var index = 0, tipoRecibo = 0;
function loadEnderecos(content) {
	if (content==null && rsmContaReceber.lines[index]['CD_PESSOA']) {
		getPage("GET", "loadEnderecos", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
				"&method=getAllEnderecosOfPessoa(const "+rsmContaReceber.lines[index]['CD_PESSOA']+":int)", null, true);
	}
	else {
		var rsmEnderecos = null;
		try {
			rsmEnderecos = eval('(' + content + ')')
		} 
		catch(e) {}
		for (var i=0; rsmEnderecos!=null && i<rsmEnderecos.lines.length; i++) {
			var nmEnderecoFormatado = getFormatedAddress(rsmEnderecos.lines[i]);
			// Atribuindo o endereço a todos os boletos do mesmo cliente
			for(var l=index; l<rsmContaReceber.lines.length; l++)	{
				if(rsmContaReceber.lines[l]['CD_PESSOA']==rsmContaReceber.lines[index]['CD_PESSOA'])
					rsmContaReceber.lines[l]['DS_ENDERECO_SACADO'] = nmEnderecoFormatado;
			}		
		}
		if(rsmEnderecos==null || rsmEnderecos.lines.length==0 || rsmContaReceber.lines[index]['DS_ENDERECO_SACADO']=='')	{
			rsmContaReceber.lines[index]['DS_ENDERECO_SACADO'] = '-';
		}
		btnBoletoOnClick(tipoRecibo);
	}
}

function btnPesquisarOnClick(content) {
	if(content==null)	{
		$('labelResultado').innerHTML = 'Resultado da Pesquisa';
		var objetos = 'crt=java.util.ArrayList();',
			execute = '';
		// Empresa
		if($('cdEmpresa').value > 0)	{
			objetos += 'itemEmpresa=sol.dao.ItemComparator(const A.cd_empresa:String,const '+$('cdEmpresa').value+
			           ':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemEmpresa:Object);';
		}
		// cd Emitente
		if($('cdPessoa').value > 0)	{
			objetos += 'itemPessoa0=sol.dao.ItemComparator(const C.cd_pessoa:String,const '+$('cdPessoa').value+
			           ':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemPessoa0:Object);';
		}
		// nm Emitente
		if($('nmPessoa').value != '')	{
			objetos += 'itemPessoa1=sol.dao.ItemComparator(const C.nm_pessoa:String, const ' + $('nmPessoa').value.toUpperCase() +
			           ':String, const ' + _LIKE_ANY + ':int, const ' + _VARCHAR + ':int);';
			execute += 'crt.add(*itemPessoa1:Object);';
		}
		// nm Cliente
		if($('nmCliente').value != '')	{
			objetos += 'itemCliente=sol.dao.ItemComparator(const CPP.nm_pessoa:String, const ' + $('nmCliente').value.toUpperCase() +
			           ':String, const ' + _LIKE_ANY + ':int, const ' + _VARCHAR + ':int);';
			execute += 'crt.add(*itemCliente:Object);';
		}
		// Documento
		if($('nrDocumento').value != '')	{
			objetos += 'itemDocumento=sol.dao.ItemComparator(const A.nr_documento:String, const ' + $('nrDocumento').value +
			           ':String, const ' + _LIKE_ANY + ':int, const ' + _VARCHAR + ':int);';
			execute += 'crt.add(*itemDocumento:Object);';
		}
		// Situação
		if($('stTitulo').value >= 0)	{
			objetos += 'itemSituacao=sol.dao.ItemComparator(const I.st_titulo:String, const ' + $('stTitulo').value +
			           ':String,const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
			execute += 'crt.add(*itemSituacao:Object);';
		}
		objetos += 'isFactoring=sol.dao.ItemComparator(const isFactoring:String, const 1:String,const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
		execute += 'crt.add(*isFactoring:Object);';
		if ($('tpData').value >= 0)	{
			var fieldDate = '';
			switch(parseInt($('tpData').value, 10)) {
				case 0: fieldDate = 'A.dt_vencimento'; break;
				case 1: fieldDate = 'A.dt_emissao'; break;
				case 2: fieldDate = 'A.dt_recebimento'; break;
			}
			$('dtInicial').value = $('dtInicial').value + ' 00:00'; 
			$('dtFinal').value   = $('dtFinal').value + ' 23:59'; 
			// Inicial
			objetos += 'itemDtInicial=sol.dao.ItemComparator(const ' + fieldDate + ':String,dtInicial:String, const ' + _GREATER_EQUAL + ':int, const ' + _TIMESTAMP + ':int);';
			execute += 'crt.add(*itemDtInicial:Object);';
			// Final
			objetos += 'itemDtFinal=sol.dao.ItemComparator(const ' + fieldDate + ':String,dtFinal:String, const ' + _MINOR_EQUAL + ':int,const ' + _TIMESTAMP + ':int);';
			execute += 'crt.add(*itemDtFinal:Object);';
		}
		if (formValidation()) {
			createGrid(null);
			var method = $('tpRelatorio').value==0 ? 'findParcelas' : 'findByCliente';
			formMensagem = createTempbox("jMsg", {width: 165, height: 50, message: "Aguarde... pesquisando!", tempboxType: "LOADING", time: 0});
			// BUSCANDO
			setTimeout(function()	{
				getPage('GET2POST', 'btnPesquisarOnClick', 
						'../methodcaller?className=com.tivic.manager.adm.ContaReceberServices' +
						'&objects=' + objetos +
						((execute != '')?'&execute=':'') + execute +
						'&method='+method+'(*crt:java.util.ArrayList)', [$('dtInicial'),$('dtFinal')])}, 10);
		}
	}
	else {	// retorno
		try {
			$('dtInicial').value = $('dtInicial').value.split(' ')[0]; 
			$('dtFinal').value   = $('dtFinal').value.split(' ')[0]; 
		} catch(erro) {};
		var vlTotal          = 0
		    vlTotalAbatimento  = 0; 
		formMensagem.close();
		rsmContaReceber = eval("(" + content + ")");
		var qt = rsmContaReceber.lines.length;
		$('labelResultado').innerHTML = (qt==0)?'Nenhuma conta encontrada':(qt==1)?'1 conta encontrada':qt+' contas encontradas';
		createGrid(rsmContaReceber);
	}
}
var columnsContaReceber = [{label:'Situação', reference: 'CL_SITUACAO'},
					  	   {label:'Emitente', reference: 'NM_PESSOA'},
					  	   {label:'Cliente', reference: 'NM_CLIENTE'},
					  	   {label:'Nº Documento', reference: 'NR_DOCUMENTO'},
					  	   {label:'Emissão', reference: 'DT_EMISSAO', type: GridOne._DATE},							   
						   {label:'Vencimento', reference: 'DT_VENCIMENTO', type: GridOne._DATE},							   
						   {label:'Recebida em', reference: 'DT_RECEBIMENTO', type: GridOne._DATE},
  						   {label:'Valor conta', reference: 'VL_CONTA', type: GridOne._CURRENCY},
  						   {label:'Desconto', reference: 'VL_ABATIMENTO', type: GridOne._CURRENCY},
  						   {label:'Acréscimo', reference: 'VL_ACRESCIMO', type: GridOne._CURRENCY},
  						   {label:'Valor juros', reference: 'VL_JUROS', type: GridOne._CURRENCY},
						   {label:'Valor multa', reference: 'VL_MULTA', type: GridOne._CURRENCY},
						   {label:'Recebido', reference: 'VL_RECEBIDO', style: 'color:#0000FF; text-align:right;', type: GridOne._CURRENCY},
  						   {label:'A Receber', reference: 'VL_ARECEBER', style: 'color:#FF0000; text-align:right;', type: GridOne._CURRENCY}];

function createGrid(rsm){
	
	var columns = columnsContaReceber;
	
	if ($('tpRelatorio').value == 1)
		columns             = [{label: 'Emitente', reference: 'NM_PESSOA'},
				   			   {label: 'Qtd. Contas', reference: 'QT_CONTA', style: 'text-align:right;'},
				    		   {label: 'Total Conta', reference: 'VL_CONTA', type: GridOne._CURRENCY},
				   			   {label: 'Total Desconto', reference: 'VL_ABATIMENTO', type: GridOne._CURRENCY},
				   			   {label: 'Total Recebido', reference: 'VL_RECEBIDO', type: GridOne._CURRENCY},
				   			   {label: 'Total A Receber', reference: 'VL_ARECEBER', type: GridOne._CURRENCY}]; 
	
	gridContaReceber = GridOne.create('gridContaReceber', {columns: columns,
														   resultset: rsm,
														   plotPlace: $('divGridContaReceber'),
														   onDoubleClick: function() {viewContaReceber(); },
 													       columnSeparator: true,
													       lineSeparator: false,
														   strippedLines: true,
														   onProcessRegister: function(reg){
														   		var dtVencimento = getAsDate(reg['DT_VENCIMENTO']);
																var dataAtual = new Date();
																dataAtual = dataAtual.setTime(<%=hoje.getTimeInMillis()%>);
																var stTitulo = reg['ST_TITULO'];
																if(stTitulo != null)	{
																	reg['CL_SITUACAO'] = situacaoTitulo[stTitulo];
																	if (parseInt(stTitulo, 10) == <%=TituloCreditoServices.stEM_ABERTO%>)
																		if (dtVencimento < dataAtual) {
																			reg['CL_SITUACAO'] = 'Vencida';
																			reg['ST_TITULO'] = 99;
																		}
																}
																reg['VL_ABATIMENTO'] = reg['VL_ABATIMENTO']==null ? 0 : reg['VL_ABATIMENTO'];
																reg['CL_VENCIMENTO'] = reg['DT_VENCIMENTO']==null ? null : reg['DT_VENCIMENTO'].split(' ')[0];
															    reg['VL_ARECEBER']   = reg['VL_CONTA'] - reg['VL_ABATIMENTO'] - reg['VL_RECEBIDO'];
															    if(reg['VL_JUROS'] == null)
															    	reg['VL_JUROS'] = 0;
															    if(reg['VL_MULTA'] == null)
															    	reg['VL_MULTA'] = 0;
															    
															    if(reg['VL_ARECEBER'] < 0)
															    	reg['VL_ARECEBER'] = 0;
															    
															    if(reg['VL_ARECEBER'] == 0){
															    	reg['VL_JUROS'] = 0;
															    	reg['VL_MULTA'] = 0;
															    }
																switch(parseInt(reg['ST_TITULO'], 10)) {
															   		case <%=TituloCreditoServices.stLIQUIDADO%>  	: reg['CL_SITUACAO_cellStyle']  = 'color:#00FF00;'; break;
															   		case <%=TituloCreditoServices.stDESCONTADO%>  	: reg['CL_SITUACAO_cellStyle'] = 'color:#00FF00;'; break;
															   		case <%=TituloCreditoServices.stCANCELADO%>		: reg['CL_SITUACAO_cellStyle'] = 'color:#0000FF;'; break;
															   		case <%=TituloCreditoServices.stDEVOLVIDA_1X%> 	: reg['CL_SITUACAO_cellStyle'] = 'color:#808080;'; break;
															   		case <%=TituloCreditoServices.stDEVOLVIDA_2X%> 	: reg['CL_SITUACAO_cellStyle'] = 'color:#808080;'; break;
															   		case <%=TituloCreditoServices.stRESGATADO%> 	: reg['CL_SITUACAO_cellStyle'] = 'color:#808080;'; break;
															   		case <%=TituloCreditoServices.stPERDA%> 		: reg['CL_SITUACAO_cellStyle'] = 'color:#FF6432;'; break;
															   		case 99                                     	: reg['CL_SITUACAO_cellStyle'] = 'color:#FF0000;'; break;
															    }
														   },		
														   noSelectOnCreate: true});//Mudei para true
	
}

function viewContaReceber()	{
	if(gridContaReceber.getSelectedRowRegister())	{
		parent.miContaReceberOnClick(true, gridContaReceber.getSelectedRowRegister()['CD_CONTA_RECEBER']);
	}
	else	{
		createMsgbox("jMsg", {width: 300, height: 50, msgboxType: "INFO",
							  message: "Selecione a conta que deseja visualizar."});
	}											
}
function btnDeleteContaReceberOnClickAux(content){
	getPage("GET", "btnDeleteContaReceberOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.ContaReceberServices"+
            "&method=delete(const "+registro['CD_CONTA_RECEBER']+":int):int", null, null, null, null);
}

var registro;
function btnDeleteContaReceberOnClick(content){
    if(content==null){
    	registro = null;
    	registro = gridContaReceber.getSelectedRowRegister();
    	if (registro == null)	{
			createMsgbox("jMsg", {width: 250,
								  height: 50,
								  message: "Nenhuma conta selecionada.", 
								  msgboxType: "INFO"});
		}								  
        else 
            createConfirmbox("dialog", {caption: "Exclusão de registro(os)",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir esta(as) conta(as)?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteContaReceberOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 50, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
						
            btnPesquisarOnClick();
			
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 50, 
                                  message: "Não foi possível excluir o registro!", 
                                  time: 5000});
    }	
}

function btnCancelarContaReceberOnClickAux(content){
	var contaReceberDescription = "(Nr. Documento: " + registro["NR_DOCUMENTO"] + ", Cód.: " + registro["CD_CONTA_RECEBER"] + ")";
    var executionDescription = "Cancelamento " + contaReceberDescription;
	getPage("GET", "btnCancelarContaReceberOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.ContaReceberServices"+
            "&method=setCancelada(const " + registro["CD_CONTA_RECEBER"] + ":int)", null, null, null, executionDescription);
}

function btnCancelarContaReceberOnClick(content){
    if(content==null){
    	registro = gridContaReceber.getSelectedRowRegister();
		var situacaoTitulo = (registro != null)?parseInt(registro['ST_TITULO'], 10):'';
		if (registro == null)
			createMsgbox("jMsg", {width: 250, height: 100, message: "Nenhum registro foi carregado.", msgboxType: "INFO"});
        else if (situacaoTitulo == "<%=TituloCreditoServices.stLIQUIDADO%>" || situacaoTitulo == "<%=TituloCreditoServices.stDESCONTADO%>")
            createMsgbox("jMsg", {width: 300, height: 50, 
								message: "Este cheque já foi recebido. Cancelamento impossível.", 
								msgboxType: "INFO"});
        else if (situacaoTitulo == "<%=TituloCreditoServices.stCANCELADO%>")
            createMsgbox("jMsg", {width: 300, 
								height: 50, 
								message: "Este cheque já está cancelado.", 
								msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Cancelamento de cheque",
                                        width: 300, 
                                        height: 50, 
                                        message: "Confirma o cancelamento deste cheque?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnCancelarContaReceberOnClickAux(null)", 10)}});
    }
    
    
    
    else{
		var rsmRetorno = null;
		try {rsmRetorno = eval('(' + content + ')')} catch(e) {}
		if(rsmRetorno != null)	{
			if(rsmRetorno['ErrorCode']=='1'){
				createTempbox("jTemp", {width: 300, 
										height: 40, 
										message: "Cancelamento realizado com sucesso!", 
										time: 3000});
				registro["ST_TITULO"] = '<%=sol.util.Jso.getStream(TituloCreditoServices.stCANCELADO)%>';
				registro["CL_SITUACAO"] = 'Cancelada';
				registro['CL_SITUACAO_cellStyle'] = 'color:#0000FF;'; 
		    	gridContaReceber.updateSelectedRow(registro);
			}
			else
				createTempbox("jTemp", {width: 300, 
										height: 40, 
										message: rsmRetorno['ErrorMsg'], 
										time: 5000});
		}
    }	
}

function btnCompensarChequeOnClickAux(content){
	var contaReceberDescription = "(Nr. Documento: " + registro["NR_DOCUMENTO"] + ", Cód.: " + registro["CD_CONTA_RECEBER"] + ")";
    var executionDescription = "Compensacao " + contaReceberDescription;
	getPage("GET", "btnCompensarChequeOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.ContaReceberServices"+
            "&method=setCompensado(const " + registro["CD_CONTA_RECEBER"] + ":int)", null, null, null, executionDescription);
}

function btnCompensarChequeOnClick(content){
    if(content==null){
    	registro = gridContaReceber.getSelectedRowRegister();
    	var situacaoTitulo = (registro != null)?parseInt(registro['ST_TITULO'], 10):'';
    	if (registro == null){
			createMsgbox("jMsg", {width: 250, height: 100, message: "Nenhum registro foi carregado.", msgboxType: "INFO"});
    	}
        else if (situacaoTitulo != "<%=TituloCreditoServices.stLIQUIDADO%>")
            createMsgbox("jMsg", {width: 300, height: 50, 
								message: "Só é possível compensar um cheque que foi recebido", 
								msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Compensação de cheque",
                                        width: 300, 
                                        height: 50, 
                                        message: "Confirma a compensação deste cheque?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnCompensarChequeOnClickAux(null)", 10)}});
    }
    else{
		var rsmRetorno = null;
		try {rsmRetorno = eval('(' + content + ')')} catch(e) {}
		if(rsmRetorno != null)	{
			if(rsmRetorno['ErrorCode']=='1'){
				createTempbox("jTemp", {width: 300, 
										height: 40, 
										message: "Compensação realizada com sucesso!", 
										time: 3000});
				registro["ST_TITULO"] = '<%=sol.util.Jso.getStream(TituloCreditoServices.stDESCONTADO)%>';
				registro["CL_SITUACAO"] = "Compesado";
				registro['CL_SITUACAO_cellStyle'] = 'color:#00FF00;'; 
				gridContaReceber.updateSelectedRow(registro);
			}
			else
				createTempbox("jTemp", {width: 300, 
										height: 40, 
										message: rsmRetorno['ErrorMsg'], 
										time: 5000});
		}
    }	
}

function btnProrrogarChequeOnClickAux(content){
	var contaReceberDescription = "(Nr. Documento: " + registro["NR_DOCUMENTO"] + ", Cód.: " + registro["CD_CONTA_RECEBER"] + ")";
    var executionDescription = "Prorrogacao " + contaReceberDescription;
	getPage("GET", "btnProrrogarChequeOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.ContaReceberServices"+
            "&method=setProrrogado(const " + registro["CD_CONTA_RECEBER"] + ":int, const "+$('dtVencimentoNovoGrid').value+":GregorianCalendar)", null, null, null, executionDescription);
}


function btnEscolherNovaDataOnClick(content){
	registro = gridContaReceber.getSelectedRowRegister();
	var situacaoTitulo = (registro != null)?parseInt(registro['ST_TITULO'], 10):'';
	if (registro == null){
		createMsgbox("jMsg", {width: 250, height: 100, message: "Nenhum registro foi carregado.", msgboxType: "INFO"});
	}
    else if (situacaoTitulo != "<%=TituloCreditoServices.stEM_ABERTO%>" && situacaoTitulo != "99")
        createMsgbox("jMsg", {width: 300, height: 50, 
							message: "Só é possível prorrogar um cheque que está em aberto", 
							msgboxType: "INFO"});
    else
        FormFactory.createFormWindow('jEscolherData', 
            {caption: "Selecione a nova data de vencimento para este cheque", width: 390, height: 100, unitSize: '%', modal: true, 
			  id: 'formEscolherData', loadForm: true, noDrag: true, cssVersion: '2',
			  lines:[[{id:'dtVencimentoNovoGrid', idForm: 'pessoa', reference: 'dt_vencimento', type:'date', label:'Nova Data Vencimento', datatype: 'DATE', width:100},
			          {type: 'space', width: 50},
			          {id: 'btnGravarEmitenteOnClick', type: 'button', width: 25, image: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: function(){btnProrrogarChequeOnClick(null);}},
		  			  {id: 'btnFecharEmitenteOnClick', type: 'button', width: 25, image: '/sol/imagens/form-btExcluir16.gif', label: 'Cancelar', onClick: function(){closeWindow('jEscolherData');}}]]});
}

function btnProrrogarChequeOnClick(content){
    if(content==null){
    	createConfirmbox("dialog", {caption: "Prorrogação de cheque",
                                        width: 300, 
                                        height: 50, 
                                        message: "Confirma a prorrogação deste cheque?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnProrrogarChequeOnClickAux(null)", 10)}});
    }
    else{
		var rsmRetorno = null;
		try {rsmRetorno = eval('(' + content + ')')} catch(e) {}
		if(rsmRetorno != null)	{
			if(rsmRetorno['ErrorCode']=='1'){
				createTempbox("jTemp", {width: 300, 
										height: 40, 
										message: "Prorrogação realizada com sucesso!", 
										time: 3000});
				registro["ST_TITULO"] = '<%=sol.util.Jso.getStream(TituloCreditoServices.stEM_ABERTO)%>';
				registro["CL_SITUACAO"] = "Em aberto";
				registro['CL_SITUACAO_cellStyle'] = ''; 
				registro['DT_VENCIMENTO'] = rsmRetorno['dtVencimento'];
				registro['VL_ACRESCIMO'] = rsmRetorno['vlAcrescimo'];
				gridContaReceber.updateSelectedRow(registro);
				closeWindow('jEscolherData');
			}
			else
				createTempbox("jTemp", {width: 300, 
										height: 40, 
										message: rsmRetorno['ErrorMsg'], 
										time: 5000});
		}
    }	
}



function btnDevolverChequeOnClickAux(content){
	var contaReceberDescription = "(Nr. Documento: " + registro["NR_DOCUMENTO"] + ", Cód.: " + registro["CD_CONTA_RECEBER"] + ")";
    var executionDescription = "Devolução " + contaReceberDescription;
	getPage("GET", "btnDevolverChequeOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.ContaReceberServices"+
            "&method=setDevolvido(const " + registro["CD_CONTA_RECEBER"] + ":int)", null, null, null, executionDescription);
}

function btnDevolverChequeOnClick(content){
    if(content==null){
    	registro = gridContaReceber.getSelectedRowRegister();
    	var situacaoTitulo = (registro != null)?parseInt(registro['ST_TITULO'], 10):'';
    	if (registro == null){
			createMsgbox("jMsg", {width: 250, height: 100, message: "Nenhum registro foi carregado.", msgboxType: "INFO"});
    	}
        else if (situacaoTitulo != '<%=TituloCreditoServices.stEM_ABERTO%>' && situacaoTitulo != '<%=TituloCreditoServices.stDEVOLVIDA_1X%>' 
              && situacaoTitulo != "99")
            createMsgbox("jMsg", {width: 300, height: 50, 
								message: "Só é possível devolver um cheque que está em aberto, ou que já foi devolvido 1x", 
								msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Devolução de cheque",
                                        width: 300, 
                                        height: 50, 
                                        message: "Confirma a devolução deste cheque?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDevolverChequeOnClickAux(null)", 10)}});
    }
    else{
		var rsmRetorno = null;
		try {rsmRetorno = eval('(' + content + ')')} catch(e) {}
		if(rsmRetorno != null)	{
			if(rsmRetorno['ErrorCode']=='1'){
				createTempbox("jTemp", {width: 300, 
										height: 40, 
										message: "Devolução realizada com sucesso!", 
										time: 3000});
				registro["VL_MULTA"]	= rsmRetorno['vlMulta'];
				registro["ST_TITULO"] 	= rsmRetorno['vez'];
				registro["CL_SITUACAO"] = "Devolvido " + (rsmRetorno['vez'] == "<%=TituloCreditoServices.stDEVOLVIDA_1X%>" ? "1x" : "2x");
				registro['CL_SITUACAO_cellStyle'] = 'color:#808080;';
				gridContaReceber.updateSelectedRow(registro);
			}
			else
				createTempbox("jTemp", {width: 300, 
										height: 40, 
										message: rsmRetorno['ErrorMsg'], 
										time: 5000});
		}
    }	
}

function btnPerderChequeOnClickAux(content){
	var contaReceberDescription = "(Nr. Documento: " + registro["NR_DOCUMENTO"] + ", Cód.: " + registro["CD_CONTA_RECEBER"] + ")";
    var executionDescription = "Perda " + contaReceberDescription;
	getPage("GET", "btnPerderChequeOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.ContaReceberServices"+
            "&method=setPerda(const " + registro["CD_CONTA_RECEBER"] + ":int)", null, null, null, executionDescription);
}

function btnPerderChequeOnClick(content){
    if(content==null){
    	registro = gridContaReceber.getSelectedRowRegister();
    	var situacaoTitulo = (registro != null)?parseInt(registro['ST_TITULO'], 10):'';
    	if (registro == null){
			createMsgbox("jMsg", {width: 250, height: 100, message: "Nenhum registro foi carregado.", msgboxType: "INFO"});
    	}
        else if (situacaoTitulo != '<%=TituloCreditoServices.stEM_ABERTO%>' && situacaoTitulo != '<%=TituloCreditoServices.stDEVOLVIDA_1X%>'
        		&& situacaoTitulo != '<%=TituloCreditoServices.stDEVOLVIDA_2X%>' && situacaoTitulo != "99")
            createMsgbox("jMsg", {width: 300, height: 50, 
								message: "Só é possível dar perda de um cheque que está em aberto, ou que foi devolvido.", 
								msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Perda de cheque",
                                        width: 300, 
                                        height: 50, 
                                        message: "Confirma a perda deste cheque?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnPerderChequeOnClickAux(null)", 10)}});
    }
    else{
		var rsmRetorno = null;
		try {rsmRetorno = eval('(' + content + ')')} catch(e) {}
		if(rsmRetorno != null)	{
			if(rsmRetorno['ErrorCode']=='1'){
				createTempbox("jTemp", {width: 300, 
										height: 40, 
										message: "Perda realizada com sucesso!", 
										time: 3000});
				registro["ST_TITULO"] = "<%=TituloCreditoServices.stPERDA%>";
				registro["CL_SITUACAO"] = "Perda";
				registro['CL_SITUACAO_cellStyle'] = 'color:#FF6432;'; 
				gridContaReceber.updateSelectedRow(registro);
			}
			else
				createTempbox("jTemp", {width: 300, 
										height: 40, 
										message: rsmRetorno['ErrorMsg'], 
										time: 5000});
		}
    }	
}

function btnRecuperarChequeOnClickAux(content){
	var contaReceberDescription = "(Nr. Documento: " + registro["NR_DOCUMENTO"] + ", Cód.: " + registro["CD_CONTA_RECEBER"] + ")";
    var executionDescription = "Devolução " + contaReceberDescription;
	getPage("GET", "btnRecuperarChequeOnClick", 
            "../methodcaller?className=com.tivic.manager.adm.ContaReceberServices"+
            "&method=setRecuperar(const " + registro["CD_CONTA_RECEBER"] + ":int)", null, null, null, executionDescription);
}

function btnRecuperarChequeOnClick(content){
    if(content==null){
    	registro = gridContaReceber.getSelectedRowRegister();
    	var stTitulo = (registro != null)?parseInt(registro['ST_TITULO'], 10):'';
    	if (registro == null){
			createMsgbox("jMsg", {width: 250, height: 100, message: "Nenhum registro foi carregado.", msgboxType: "INFO"});
    	}
        else if (stTitulo != "<%=TituloCreditoServices.stPERDA%>")
            createMsgbox("jMsg", {width: 300, height: 50, 
								message: "Só é possível recuperar um cheque perdido.", 
								msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Recuperação de cheque",
                                        width: 300, 
                                        height: 50, 
                                        message: "Confirma a recuperação deste cheque?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnRecuperarChequeOnClickAux(null)", 10)}});
    }
    else{
		var rsmRetorno = null;
		try {rsmRetorno = eval('(' + content + ')')} catch(e) {}
		if(rsmRetorno != null)	{
			if(rsmRetorno['ErrorCode']=='1'){
				createTempbox("jTemp", {width: 300, 
										height: 40, 
										message: "Recuperação realizada com sucesso!", 
										time: 3000});
				var dtVencimento = getAsDate(registro['DT_VENCIMENTO']);
				var dataAtual = new Date();
				dataAtual = dataAtual.setTime(<%=hoje.getTimeInMillis()%>);
				registro["ST_TITULO"] = "<%=TituloCreditoServices.stEM_ABERTO%>";
				var stTitulo = registro['ST_TITULO'];
				if(stTitulo != null)	{
					registro['CL_SITUACAO'] = situacaoTitulo[stTitulo];
					if (parseInt(stTitulo, 10) == <%=TituloCreditoServices.stEM_ABERTO%>)
						if (dtVencimento < dataAtual) {
							registro['CL_SITUACAO'] = 'Vencida';
							registro['ST_TITULO'] = "99";
							registro['CL_SITUACAO_cellStyle'] = 'color:#FF0000;'; 
						}
						else{
							registro['CL_SITUACAO_cellStyle'] = ''; 
						}
					
				}
				gridContaReceber.updateSelectedRow(registro);
			}
			else
				createTempbox("jTemp", {width: 300, 
										height: 40, 
									message: rsmRetorno['ErrorMsg'], 
										time: 5000});
		}
    }	
}

var regPagamento = {};
function verificarStatusConta(registro)	{
	var situacaoTitulo = (registro != null)?parseInt(registro['ST_TITULO'], 10) : '';
	if (registro == null)	{
		createMsgbox("jMsg", {width: 250, height: 50, message: "Nenhum registro foi carregado.", msgboxType: "INFO"});
		return false;
	}
    else if (situacaoTitulo == "<%=TituloCreditoServices.stLIQUIDADO%>" || situacaoTitulo == "<%=TituloCreditoServices.stDESCONTADO%>")	{
        createMsgbox("jMsg", {width: 300,  height: 50, msgboxType: "INFO",
                              message: "Este cheque já foi pago. \n Emitente: "+registro['NM_PESSOA']+
                                       ", Valor do cheque: "+formatCurrency(registro['VL_CONTA'])});
		return false;
    }
    else if (situacaoTitulo == "<%=TituloCreditoServices.stCANCELADO%>")	{
        createMsgbox("jMsg", {width: 300, height: 50, msgboxType: "INFO", 
                              message: "Este cheque está cancelada. Pagamento não permitido. \n Emitente: "+registro['NM_PESSOA']+", Valor da Conta: "+formatCurrency(registro['VL_CONTA'])});
		return false;
	}
	else if (situacaoTitulo == "<%=TituloCreditoServices.stRESGATADO%>")	{
        createMsgbox("jMsg", {width: 300, height: 50, msgboxType: "INFO", 
                              message: "Este cheque está resgatado. Pagamento não permitido. \n Emitente: "+registro['NM_PESSOA']+", Valor da Conta: "+formatCurrency(registro['VL_CONTA'])});
		return false;
	}
    else if (situacaoTitulo == "<%=TituloCreditoServices.stPERDA%>")	{
        createMsgbox("jMsg", {width: 300, height: 30, msgboxType: "INFO", 
                              message: "Este cheque está perdido. Pagamento não permitido. \n Emitente: "+registro['NM_PESSOA']+", Valor da Conta: "+formatCurrency(registro['VL_CONTA'])});
		return false;
	}
	return true;
}

function btnEscolherFormaLancamentoOnClick(){
	createWindow('jEscolherForma', {caption: "Escolha a forma de Lançamento", width: 230, height: 125, noDropContent: true, columnSeparator: false,
									modal: true, noDrag: true, contentDiv: 'formEscolherFormaLancamento'});
	
}

function inArray(codigo, array){
	for(var i = 0; i < array.length; i++){
		if(codigo==array[i].value)
			return true;
	}
	return false;
}

function btnLancarFaturamentoOnClick(content){
	btnBaixaContaReceberOnClick(null, 0);
}

function btnResgateOnClick(content){
	registro = gridContaReceber.getSelectedRowRegister();
	if (registro['ST_TITULO'] != "99" && registro['ST_TITULO'] != "<%=TituloCreditoServices.stDEVOLVIDA_1X%>"  && registro['ST_TITULO'] != "<%=TituloCreditoServices.stDEVOLVIDA_2X%>" )	{
        createMsgbox("jMsg", {width: 300,  height: 50, msgboxType: "INFO",
                              message: "Somente cheques vencidos ou devolvidos podem ser resgatados"});
		return false;
    }
	btnBaixaContaReceberOnClick(null, 1);
}

function btnBaixaContaReceberOnClick(content, isResgate){
	
	closeWindow('jEscolherForma');
	var allowRecebimento = true;
	var reg = gridContaReceber.getSelectedRowRegister();
	var nmConta = reg["NM_CONTA"];
	var cdConta = reg["CD_CONTA"];
	if(!verificarStatusConta(gridContaReceber.getSelectedRowRegister()))	{
		createMsgbox("jMsg", {width: 300, height: 50, msgboxType: "INFO", 
			  message: "O cheque não pode ser dado baixa ou não há cheque selecionado!"});
		return;
	}
    else {
    	$('cdContaEscolhida').value = cdConta;
    	$('nmContaEscolhida').value = nmConta;
    	btnBaixaContaReceberAuxOnClick(content, isResgate);
    		
    }
	
}

var listaContasGlobl;
function btnBaixaContaReceberAuxOnClick(content, isResgate){
	if(content==null){
		createWindow("jMovimentoContaRecebimento", {caption: "Baixa de conta a receber",
													  width: 500, height: 396, modal: true,
													  contentUrl: "../adm/movimento_conta_receber.jsp?cdEmpresa=<%=cdEmpresa%>" +
																  "&cdContaReceber=" + gridContaReceber.getSelectedRowRegister()['CD_CONTA_RECEBER'] +
																  "&cdConta=" + gridContaReceber.getSelectedRowRegister()['CD_CONTA'] +
																  "&hasJuros=1" +
																  "&isValorTotal=1"+
																  "&nmConta=" + gridContaReceber.getSelectedRowRegister()['NM_CONTA'] +
																  "&isGravarDados=1" + 
																  "&isResumido=0" + 
																  "&isResgate="+isResgate});
	
	
	}
	else {
        var ok = parseInt(content, 10) > 0;
		if (ok) {
			closeWindow('jMovimentoContaRecebimento');
			closeWindow('jMovimentoConta');
			createTempbox("jMsg", {width: 300,
								   height: 50,
								   message: "Recebimento efetuado com sucesso!",
								   tempboxType: "INFO",
								   time: 3000});F
			registro['CD_CONTA']           = regPagamento['CD_CONTA'];
			registro['CD_MOVIMENTO_CONTA'] = regPagamento['CD_MOVIMENTO_CONTA'];
			registro['CD_CONTA_RECEBER']   = regPagamento['CD_CONTA_RECEBER'];
			registro['NM_PESSOA'] 	  	   = regPagamento['NM_PESSOA'];
			registro['DT_VENCIMENTO']      = regPagamento['DT_VENCIMENTO'];
			registro['DT_MOVIMENTO']   	   = regPagamento['DT_MOVIMENTO'];
			registro['NR_DOCUMENTO']   	   = regPagamento['NR_DOCUMENTO'];
			registro['TP_DOCUMENTO']   	   = regPagamento['TP_DOCUMENTO'];
			registro['TP_PAGAMENTO']   	   = regPagamento['TP_PAGAMENTO'];
			registro['VL_CONTA'] 	       = regPagamento['VL_CONTA'];
			registro['VL_MULTA'] 	       = regPagamento['VL_MULTA'];
			registro['VL_JUROS'] 	       = regPagamento['VL_JUROS'];
			registro['VL_DESCONTO']    	   = regPagamento['VL_DESCONTO'];
			registro['VL_RECEBIDO']    	   = regPagamento['VL_RECEBIDO'];
			registro["ST_CONTA"] 		   = <%=ContaReceberServices.ST_RECEBIDA%>;
			registro["ST_TITULO"] 		   = <%=TituloCreditoServices.stLIQUIDADO%>;
			registro["CL_SITUACAO"] 	   = 'Liquidado';
			registro['CL_SITUACAO_cellStyle']  = 'color:#00FF00;'; 
			gridContaReceber.updateSelectedRow(registro);
		}
		else	
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "ERRO ao tentar efetuar baixa da conta!",
                                   tempboxType: "ERROR",
                                   time: 3000});
	}
}

function btnImprimirOnClick(){
		printRelatorioReport();
}

function printRelatorioReport(){
	var caption    = "Relatório de Cheques a Receber";
	var className  = "com.tivic.manager.adm.ContaFactoringServices";
	var method; 
	var nomeJasper;
	if($('tpRelatorio').value == 0){	
		method = "gerarRelatorioChequesAReceber";
		nomeJasper = "relatorio_cheques_receber";
	}
	else if($('tpRelatorio').value == 1){	
		method = "gerarRelatorioChequesAReceberByEmitente";
		nomeJasper = "relatorio_cheques_receber_emitente";
		caption += " (Por Emitente)";
	}
	
	method += "(const "+$('cdEmpresa').value+":int,const "+$('tpData').value+":int, "+
			 "const "+$('dtInicial').value+":GregorianCalendar, const "+$('dtFinal').value+":GregorianCalendar, " +
			 "const "+$('stTitulo').value+":int,const "+$('nmPessoa').value+":String,const "+$('nmCliente').value+":String, " +
			 "const "+$('nrDocumento').value+":String)";
			
	
	var frameHeight;
	if (top.innerHeight)
		frameHeight = top.innerHeight;
	else if (document.documentElement && document.documentElement.clientHeight)
		frameHeight = document.documentElement.clientHeight;
	else if (document.body)
		frameHeight = document.body.clientHeight;
		
	var frameWidth;
	if (top.innerWidth)
		frameWidth = top.innerWidth;
	else if (document.documentElement && document.documentElement.clientWidth)
		frameWidth = document.documentElement.clientWidth;
	else if (document.body)
		frameWidth = document.body.clientWidth;
	parent.createWindow('jRelatorioSaida', {caption: caption, width: frameWidth-20, height: frameHeight-50,
        contentUrl: "../ireport2.jsp?tpLocalizacao=1&className="+ className +
					 "&method="		+ method +
					 "&nomeJasper=" + nomeJasper +
					 "&cdEmpresa=" 	+ $('cdEmpresa').value + 
					 "&modulo=fac"});
	
	
}

</script>
</head>
<body class="body" onload="init();">
	<div style="width: 891px;" id="ManutencaoContaReceber" class="d1-form">
   		<div style="width: 891px; height: 480px;" class="d1-body">
   			<div class="d1-toolBar" id="toolBarGrid" style="width:90px; height:83px; float:left; margin-right: 5px;"></div>
			<input idform="ContaReceber" reference="cd_empresa" datatype="INT" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>" />
			<input idform="ContaReceber" reference="cd_conta" id="cdConta" name="cdConta" type="hidden" value="0" defaultValue="0"/>
			<input idform="ContaReceber" reference="cd_movimento_conta" id="cdMovimentoConta" name="cdMovimentoConta" type="hidden" value="0" defaultValue="0" />
	 		<input idform="ContaReceber" reference="cd_pessoa" id="cdPessoa" name="cdPessoa" type="hidden" value="<%=cdPessoa%>" />
	 		<input idform="ContaReceber" reference="cd_conta_escolhida" id="cdContaEscolhida" name="cdContaEscolhida" type="hidden" value="0" defaultValue="0"/>
			<input idform="ContaReceber" reference="nm_conta_escolhida" id="nmContaEscolhida" name="nmContaEscolhida" type="hidden" />
			<div style="border:1px solid #999; padding:2px 2px 2px 4px; height: 83px; margin-bottom: 2px; width:777px; float: left; margin-left: 5px;">
		 		<div class="d1-line" id="line0">
					<div style="width: 180px;" class="element">
						<label class="caption" for="gnPessoa">Relatório de</label>
						<select style="width: 175px;" datatype="INT" id="tpRelatorio" name="tpRelatorio" class="select2">
						  <option value="0">Cheques a Receber</option>
						  <option value="1">Cheques a Receber (Agrupado por Emitente)</option>
						</select>
					</div>	
					<div class="element" style="width:120px;">
	                    <label for="tpData" class="caption">Data de</label>
	                    <select style="width:115px" type="text" name="tpData" id="tpData" class="select2">
	                        <option value="-1" selected="selected">Ignorar</option>
	                        <option value="0">Vencimento</option>
	                        <option value="1">Emissão</option>
	                        <option value="2">Recebimento</option>
	                    </select>
	                </div>
	                <div style="width:95px;" class="element">
	                    <label class="caption">Período</label>
	                    <input name="dtInicial" type="text" class="field2" id="dtInicial" mask="##/##/####" maxlength="10" style="width:76px; " value="<%=dtInicial%>" datatype="DATE"/>
	                    <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtInicial" class="controlButton" style="height:22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
	                </div>
	                <div style="width:95px;margin-left:3px;" class="element">
	                    <label for="dtFinal" class="caption">&nbsp;</label>
	                    <input name="dtFinal" type="text" class="field2" id="dtFinal" mask="##/##/####" maxlength="10" style="width:76px;" value="<%=dtFinal%>" datatype="DATE"/>
	                    <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtFinal" class="controlButton" style="height:22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
	                </div>
	                <div style="width:278px;margin-left:3px;" class="element">
	                    <label for="nmPessoa" class="caption">Emitente</label>
						<input style="width:278px; text-transform:uppercase;" type="text" name="nmPessoa" id="nmPessoa" class="field2" value=""/>
					</div>
					<div style="width:440px;" class="element">
	                    <label for="nmCliente" class="caption">Cliente</label>
						<input style="width:435px; text-transform:uppercase;" type="text" name="nmCliente" id="nmCliente" class="field2" value=""/>
					</div>
				</div>
				<div class="d1-line" id="line1">
					<div style="width:220px;" class="element">
	                    <label for="nrDocumento" class="caption">Nº Documento</label>
	                    <input type="text" name="nrDocumento" id="nrDocumento" class="field2" style="width:215px;" value=""/>
	                </div>
	                <div class="element" style="width:115px;">
	                    <label for="stTitulo" class="caption">Situação</label>
	                    <select style="width:115px" type="text" name="stTitulo" id="stTitulo" class="select2" >
	                        <option value="-1">Todas</option>
	                    </select>
	                </div>
	            </div>
			</div>	
			<div class="d1-toolBar" id="toolBarGrid2" style="width:885px; height:28px; float:left; margin-right: 5px;"></div>
			<div class="d1-toolBar" id="toolBarGrid3" style="width:885px; height:28px; float:left; margin-right: 5px;"></div>
			<div class="d1-toolBar" id="toolBarGrid4" style="width:885px; height:28px; float:left; margin-right: 5px;"></div>			
			<div class="d1-line" id="line3" >
                <div style="width: 688px;" class="element">
					<label class="caption" id="labelResultado" style="font-weight:bold;">Resultado da Pesquisa</label>
                    <div id="divGridContaReceber" style="float:left; width: 884px; height:240px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
                </div>
            </div>
        </div>
	</div>
    
</body>
</html>