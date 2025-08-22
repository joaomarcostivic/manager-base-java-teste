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
	String dtTroca 	 = RequestUtilities.getParameterAsString(request, "dtFinal", Util.formatDateTime(hoje, "dd/MM/yyyy"));
	boolean callFind = RequestUtilities.getParameterAsInteger(request, "callFind", 0)==1;
	int cdEmpresa 	 = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	int cdPessoa 	 = RequestUtilities.getParameterAsInteger(request, "cdPessoa", 0);
	com.tivic.manager.grl.Empresa empresa = com.tivic.manager.grl.EmpresaDAO.get(cdEmpresa);
	boolean isFactoring	  = com.tivic.manager.seg.ModuloServices.isActive("fac");
	int cdUsuario 		= session.getAttribute("usuario")!=null ? ((com.tivic.manager.seg.Usuario)session.getAttribute("usuario")).getCdUsuario() : 0;
%>
<script language="javascript" src="../js/adm_report.js"></script>
<script language="javascript">
var rsmContaReceber = null;
var situacaoContaReceber  = <%=sol.util.Jso.getStream(ContaReceberServices.situacaoContaReceber)%>;
var gridContaReceber;
var formBaixaContaReceber = null;
var registro = null;

function init()	{
    enableTabEmulation();
	
    ToolBar.create('toolBar', {plotPlace: 'toolBarGrid',
							   orientation: 'vertical',
				       	       buttons: [{id: 'btnPesquisar', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar', title: 'Pesquisar registros', onClick: btnPesquisarOnClick, width: 140},{separator: 'vertical'},
										 {id: 'btnImprimir', img: '/sol/imagens/print24.gif', label: 'Imprimir', title: 'Imprimir dados', onClick: btnImprimirOnClick, width: 140},{separator: 'vertical'},
										 {id: 'btnPreVisualizacao', img: '/sol/imagens/print24.gif', label: 'Pré Visualização', title: 'Pré Visualização do Relatório', onClick: btnPreVisualizacaoOnClick, width: 140},{separator: 'vertical'},
										 {id: 'btVisualizar', img: '../adm/imagens/visualizar24.gif', label: 'Visualizar', onClick: viewContaReceber, width: 140},{separator: 'vertical'}]});
    
    
    var dtDataMask = new Mask($("dtInicial").getAttribute("mask"));
    dtDataMask.attach($("dtInicial"));
    dtDataMask.attach($("dtFinal"));

  	addShortcut('ctrl+b', function() { if (!document.getElementById('btnBaixaContaReceberOnClick').disabled)    btnBaixaContaReceberOnClick(null, false,false)});
  	
    loadOptionsFromRsm($('cdEmpresa'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
    loadOptionsFromRsm($('cdFormaPagamento'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.FormaPagamentoEmpresaServices.getAll(cdEmpresa))%>, {fieldValue: 'cd_forma_pagamento', fieldText: 'nm_forma_pagamento'});
    
	$('cdEmpresa').value     = <%=cdEmpresa%>;
	$('cdEmpresa').disabled  = <%=cdEmpresa > 0%>;
	$('cdEmpresa').className = '<%=cdEmpresa > 0 ? "disabledSelect" : "select"%>';
	
	var vlContaMask = new Mask($("prTaxa").getAttribute("mask"), "number");
    try { vlContaMask.attach($("prTaxa")); } catch(e) {};
	
	if(<%=callFind%>)	{
		btnPesquisarOnClick(null);
	}
	createGrid(null);
	lgTodos = false;
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
function btnPesquisarOnClick(content) {
	if(content==null)	{
		$('labelResultado').innerHTML = 'Resultado da Pesquisa';
		if (formValidation()) {
			createGrid(null);
			var method     = "findCustodia(const " + $('cdEmpresa').value + ":int, " +
													"const " + $('tpData').value + ":int, " +
													"const " + $('dtInicial').value + ":GregorianCalendar, " +
													"const " + $('dtFinal').value + ":GregorianCalendar, " +
													"const " + $('cdCliente').value + ":int)";  

			
			formMensagem = createTempbox("jMsg", {width: 165, height: 50, message: "Aguarde... pesquisando!", tempboxType: "LOADING", time: 0});
			// BUSCANDO
			setTimeout(function()	{
				getPage('GET2POST', 'btnPesquisarOnClick', 
						'../methodcaller?className=com.tivic.manager.adm.ContaReceberServices' +
						'&method='+method)}, 10);
			try {
				$('dtInicial').value = $('dtInicial').value.split(' ')[0]; 
				$('dtFinal').value   = $('dtFinal').value.split(' ')[0]; 
			} catch(erro) {};
		}
	}
	else {	// retorno
		var vlTotal           = 0
		 	vlTotalAcrescimo  = 0, 
		    vlTotalAbatimento = 0; 
		formMensagem.close();
		var result = eval("("+content+")");
		rsmContaReceber = result.objects['rsm'];
		var qt = rsmContaReceber.lines.length;
		$('labelResultado').innerHTML = (qt==0)?'Nenhuma conta encontrada':(qt==1)?'1 conta encontrada':qt+' contas encontradas';
		createGrid(rsmContaReceber);
		lgTodos = false;
	}
}
var vlTotalSelecao = 0;
var vlAcrescimoSelecao = 0;
var vlAbatimentoSelecao = 0;
var vlTotalContaSelecao = 0;
var lgTodos = false;
var columnsContaReceber = [{label:"", reference:"_SELECTED", type: GridOne._CHECKBOX, width: 20,  
							labelImg: '../imagens/confirmar_all16.gif', labelImgHint: 'Clique na imagem para inverter a marcação',
						    labelImgOnClick: function()	{
						    		lgTodos = (lgTodos ? false : true);
						    		vlTotalSelecao = 0;
						    		vlAcrescimoSelecao = 0;
						    		vlAbatimentoSelecao = 0;
						    		vlTotalContaSelecao = 0;
								    for(var i=0; i<gridContaReceber.size(); i++)	{
							   			var reg = gridContaReceber.getRegisterByIndex(i);
							   			reg['_SELECTED'] = reg['_SELECTED']==1 ? 0 : 1;
							   			$('checkbox_gridContaRecebertable_tr_'+(i+1)+'_td_0').checked = reg['_SELECTED']>0;
							   			if(reg['_SELECTED']){
							   				vlTotalSelecao += parseFloat(reg['VL_ARECEBER'], 10);
								   			vlAcrescimoSelecao  += parseFloat(reg['VL_ACRESCIMO'], 10);
								   			vlAbatimentoSelecao += parseFloat(reg['VL_ABATIMENTO'], 10);
								   			vlTotalContaSelecao += parseFloat(reg['VL_CONTA'], 10);
							   			}
							   		}
								    $('vlTotalSelecao').innerHTML  = formatCurrency(vlTotalSelecao);
								    $('vlTotalAcrescimo').innerHTML  = formatCurrency(vlAcrescimoSelecao);
								    $('vlTotalAbatimento').innerHTML  = formatCurrency(vlAbatimentoSelecao);
								    $('vlTotalContaSelecao').innerHTML  = formatCurrency(vlTotalContaSelecao);
						    },
						    onCheck: function() {
						    	vlTotalSelecao = 0;
						    	vlAcrescimoSelecao = 0;
					    		vlAbatimentoSelecao = 0;
					    		vlTotalContaSelecao = 0;
						   		this.register['_SELECTED'] = (this.checked) ? 1 : 0;
						   		for(var i=0; i<gridContaReceber.size(); i++)	{
						   			var reg = gridContaReceber.getRegisterByIndex(i);
						   			if(reg['_SELECTED']){
						   				vlTotalSelecao += parseFloat(reg['VL_ARECEBER'], 10);
						   				vlAcrescimoSelecao  += parseFloat(reg['VL_ACRESCIMO'], 10);
							   			vlAbatimentoSelecao += parseFloat(reg['VL_ABATIMENTO'], 10);
							   			vlTotalContaSelecao += parseFloat(reg['VL_CONTA'], 10);
						   			}
						   		}
							    $('vlTotalSelecao').innerHTML  = formatCurrency(vlTotalSelecao);
							    $('vlTotalAcrescimo').innerHTML  = formatCurrency(vlAcrescimoSelecao);
							    $('vlTotalAbatimento').innerHTML  = formatCurrency(vlAbatimentoSelecao);
							    $('vlTotalContaSelecao').innerHTML  = formatCurrency(vlTotalContaSelecao);
					  	 	}
					  	   },
					  	   {label:'Situação', reference: 'CL_SITUACAO'},
						   {label:'Sacado', reference: 'NM_PESSOA'},
						   {label:'Emissor', reference: 'NM_EMISSOR'},
						   {label:'Emissão', reference: 'DT_EMISSAO', type: GridOne._DATE},							   
						   {label:'Vencimento', reference: 'DT_VENCIMENTO', type: GridOne._DATE},							   
  						   {label:'A Receber', reference: 'VL_ARECEBER', style: 'color:#FF0000; text-align:right;', type: GridOne._CURRENCY},
  						   {label:'Valor conta', reference: 'VL_CONTA', type: GridOne._CURRENCY},
						   {label:'Recebida em', reference: 'DT_RECEBIMENTO', type: GridOne._DATE},
  						   {label:'Recebido', reference: 'VL_RECEBIDO', style: 'color:#0000FF; text-align:right;', type: GridOne._CURRENCY},
  						   {label:'Desconto', reference: 'VL_ABATIMENTO', type: GridOne._CURRENCY},
  						   {label:'Acréscimo', reference: 'VL_ACRESCIMO', type: GridOne._CURRENCY},
    					   {label:'Tipo Doc', reference: 'SG_TIPO_DOCUMENTO'}, 
    					   {label:'Nº Documento', reference: 'NR_DOCUMENTO'}, 
    					   {label:'Referência', reference: 'NR_REFERENCIA'}, 
						   {label:'Histórico', reference: 'DS_HISTORICO'},
						   {label:'Nosso Número', reference: 'ID_CONTA_RECEBER'},							   
						   {label:'Código', reference: 'CD_CONTA_RECEBER'}];

function createGrid(rsm)	{
	
	var vlTotalConta = 0, 
	    vlTotalRecebido = 0, 
	    vlTotalAcrescimo = 0, 
	    vlTotalDesconto = 0,
	    vlTotalAReceber = 0;
	
	gridContaReceber = GridOne.create('gridContaReceber', {columns: columnsContaReceber,
														   resultset: rsm,
														   plotPlace: $('divGridContaReceber'),
														   onDoubleClick: function() {viewContaReceber(); },
 													       columnSeparator: true,
													       lineSeparator: false,
														   strippedLines: true,
														   onProcessRegister: function(reg){
														   		reg["_SELECTED"] = 0;
														   		vlTotalConta     += parseFloat(reg['VL_CONTA'], 10);
														   		vlTotalRecebido  += parseFloat(reg['VL_RECEBIDO'], 10);
														   		vlTotalAcrescimo += parseFloat(reg['VL_ACRESCIMO'], 10);
														   		vlTotalDesconto  += parseFloat(reg['VL_ABATIMENTO'], 10);
														   		var dtVencimento = getAsDate(reg['DT_VENCIMENTO']);
																var dataAtual = new Date();
																dataAtual = dataAtual.setTime(<%=hoje.getTimeInMillis()%>);
																reg['VL_ACRESCIMO']  = reg['VL_ACRESCIMO']==null ? 0 : reg['VL_ACRESCIMO'];
																reg['VL_ABATIMENTO'] = reg['VL_ABATIMENTO']==null ? 0 : reg['VL_ABATIMENTO'];
																reg['CL_VENCIMENTO'] = reg['DT_VENCIMENTO']==null ? null : reg['DT_VENCIMENTO'].split(' ')[0];
															    reg['VL_ARECEBER']   = reg['VL_CONTA'] + reg['VL_ACRESCIMO'] - reg['VL_ABATIMENTO'] - reg['VL_RECEBIDO'];
															    vlTotalAReceber  += parseFloat(reg['VL_ARECEBER'], 10);
															    if(reg['VL_ARECEBER'] < 0)
															    	reg['VL_ARECEBER'] = 0;
																switch(parseInt(reg['ST_CONTA'], 10)) {
															   		case <%=ContaReceberServices.ST_RECEBIDA%>  : reg['CL_SITUACAO_cellStyle'] = 'color:#00FF00;'; break;
															   		case <%=ContaReceberServices.ST_PRORROGADA%>: reg['CL_SITUACAO_cellStyle'] = 'color:#FF9900;'; break;
															   		case <%=ContaReceberServices.ST_CANCELADA%> : reg['CL_SITUACAO_cellStyle'] = 'color:#0000FF;'; break;
															   		case 99                                     : reg['CL_SITUACAO_cellStyle'] = 'color:#FF0000;'; break;
															    }
														   },		
														   noSelectOnCreate: true});//Mudei para true
	document.getElementById('VL_CONTA').innerHTML 	   = formatCurrency(vlTotalConta);															   
	document.getElementById('VL_RECEBIDO').innerHTML   = formatCurrency(vlTotalRecebido);															   
	document.getElementById('VL_ABATIMENTO').innerHTML = formatCurrency(vlTotalDesconto);															   
	document.getElementById('VL_ACRESCIMO').innerHTML  = formatCurrency(vlTotalAcrescimo);
	
	document.getElementById('vlTotal').innerHTML 	         = formatCurrency(vlTotalConta);
	document.getElementById('vlTotalAcrescimo').innerHTML    = formatCurrency(vlTotalAcrescimo);
	document.getElementById('vlTotalAbatimento').innerHTML   = formatCurrency(vlTotalDesconto);
	document.getElementById('vlTotalContaSelecao').innerHTML = formatCurrency(vlTotalRecebido);
	document.getElementById('vlTotalSelecao').innerHTML      = formatCurrency(vlTotalAReceber);
	
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
var registrosCdContaReceber = [];
var posicaoContaReceber = [];
var regPagamento = {};

function btnFindClienteOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Sacado", width: 600, height:420, modal: true, noDrag: true,
				// Parametros do filtro
				className: "com.tivic.manager.grl.PessoaServices", method: "find",
				filterFields: [[{label:"Sacado",reference:"NM_PESSOA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:60},
								{label:"Apelido",reference:"NM_APELIDO",datatype:_VARCHAR,comparator:_EQUAL,width:40}]],
				gridOptions:{columns:[{label:"Nome do Sacado",reference:"NM_PESSOA"},
									  {label:"ID",reference:"ID_PESSOA"},
									  {label:"Data do Cadastro",reference:"DT_CADASTRO",type:GridOne._DATE}]},
				callback: btnFindClienteOnClick});
    }
    else {// retorno
        closeWindow('jFiltro');
        $('cdCliente').value     = reg[0]['CD_PESSOA'];
        $('cdClienteView').value = reg[0]['NM_PESSOA'];
    }
}

function btnClearClienteOnClick(reg){
	$('cdCliente').value = 0;
	$('cdClienteView').value = '';
}


function btnFindDestinatarioOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Destinatário", width: 600, height:420, modal: true, noDrag: true,
				// Parametros do filtro
				className: "com.tivic.manager.grl.PessoaServices", method: "find",
				filterFields: [[{label:"Destinatário",reference:"NM_PESSOA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],
				gridOptions:{columns:[{label:"Nome do Destinatário",reference:"NM_PESSOA"},
									  {label:"ID",reference:"ID_PESSOA"},
									  {label:"Data do Cadastro",reference:"DT_CADASTRO",type:GridOne._DATE}]},
				callback: btnFindDestinatarioOnClick});
    }
    else {// retorno
        closeWindow('jFiltro');
        $('cdDestinatario').value     = reg[0]['CD_PESSOA'];
        $('cdDestinatarioView').value = reg[0]['NM_PESSOA'];
    }
}

function btnClearDestinatarioOnClick(reg){
	$('cdDestinatario').value = 0;
	$('cdDestinatarioView').value = '';
}


function btnImprimirOnClick(){
	createConfirmbox("dialog", {caption: "Exclusão de registro",
        width: 300, 
        height: 50, 
        message: "Deseja realmente dar baixa dos cheques e imprimir o relátorio?",
        boxType: "QUESTION",
        positiveAction: function() {setTimeout("printRelatorioReport(1)", 10)}});
	
}

function btnPreVisualizacaoOnClick(){
	printRelatorioReport(0);
}

function printRelatorioReport(valido){
	if($('dtTroca').value == null || $('dtTroca').value == '')	{
		createMsgbox("jMsg", {width: 300, height: 50, msgboxType: "INFO", 
			  message: "Informar data da troca!"});
		return;
	}
	else if($('cdDestinatario').value == null || $('cdDestinatario').value == '')	{
		createMsgbox("jMsg", {width: 300, height: 50, msgboxType: "INFO", 
			  message: "Informar destinatário!"});
		return;
	}
	
	else if($('prTaxa').value == null || $('prTaxa').value == '')	{
		createMsgbox("jMsg", {width: 300, height: 50, msgboxType: "INFO", 
			  message: "Informar taxa de desconto!"});
		return;
	}
	
	var execute = '';
	var objects = 'lista=java.util.ArrayList();';
	for(var i=0; i<gridContaReceber.size(); i++)	{
		var reg = gridContaReceber.getRegisterByIndex(i);
		if(!lgTodos && reg["_SELECTED"]==1)	{
			objects += 'cdContaReceber'+i+'=java.lang.Integer(const ' +reg['CD_CONTA_RECEBER'] + ':int);';
			execute += 'lista.add(*cdContaReceber'+i+':Object);';
		}
		else if(lgTodos && reg["_SELECTED"]==0)	{
			objects += 'cdContaReceber'+i+'=java.lang.Integer(const ' +reg['CD_CONTA_RECEBER'] + ':int);';
			execute += 'lista.add(*cdContaReceber'+i+':Object);';
		}
	}	
	var caption    = "Custódia";
	var className  = "com.tivic.manager.adm.ContaReceberServices";
	
	className += "&objects="+objects+"&execute="+execute;
			
	var method     = "gerarRelatorioCustodia(const " + $('cdEmpresa').value + ":int, " +
					 "const " + $('tpData').value + ":int, " +
					 "const " + $('dtInicial').value + ":GregorianCalendar, " +
					 "const " + $('dtFinal').value + ":GregorianCalendar, " +
					 "const " + $('dtTroca').value + ":GregorianCalendar, " +
					 "const " + $('cdDestinatario').value + ":int, " +
					 "const " + $('cdCliente').value + ":int, " +
					 "const " + parseFloat(changeLocale('prTaxa')) + ":float, " +
					 "const " + <%=cdUsuario%> + ":int, " +
					 "const " + valido + ":int, " +
					 "const " + lgTodos + ":boolean, " +
					 "*lista:java.util.ArrayList)";  
	var nomeJasper = "relatorio_factoring";		
	
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
	parent.createWindow('jCustodia', {caption: caption, width: frameWidth-20, height: frameHeight-50,
        contentUrl: "../ireport2.jsp?tpLocalizacao=1&className="+ className +
					 "&method="		+ method +
					 "&nomeJasper=" + nomeJasper +
					 "&cdEmpresa=" 	+ $('cdEmpresa').value + 
					 "&modulo=fac" + 
					 "&id=jCustodia"});
	
}

function getContasSelecionadas(){
	var execute = '';
	var objects = 'lista=java.util.ArrayList();';
	var vlConta = 0;
	var vlRecebido = 0;
	var vlAbatimento = 0;
	var vlAcrescimo = 0;
	for (var i = 0; i < gridContaReceber.getResultSet().lines.length; i++){
		if(gridContaReceber.getResultSet().lines[i]['_SELECTED'] == 1){
			objects += 'registro'+i+'=java.util.HashMap();';
			objects += 'cdContaReceber'+i+'=java.lang.Integer(const ' +gridContaReceber.getResultSet().lines[i]['CD_CONTA_RECEBER'] + ':int);';
			execute += 'registro'+i+'.put(const cdContaReceber:Object, *cdContaReceber'+i+':Object);';	
			execute += 'lista.add(*registro'+i+':Object);';	
			vlConta += gridContaReceber.getResultSet().lines[i]['VL_CONTA'];
			vlRecebido += gridContaReceber.getResultSet().lines[i]['VL_RECEBIDO'];
			vlAbatimento += gridContaReceber.getResultSet().lines[i]['VL_ABATIMENTO'];
			vlAcrescimo += gridContaReceber.getResultSet().lines[i]['VL_ACRESCIMO'];
			
		}
	}
	
	
	getFrameContentById('jContaReceber').$('objects').value = objects;
	getFrameContentById('jContaReceber').$('execute').value = execute;
	getFrameContentById('jContaReceber').$('vlConta').value = vlConta;
	getFrameContentById('jContaReceber').$('vlRecebido').value = vlRecebido;
	getFrameContentById('jContaReceber').$('vlAbatimento').value = vlAbatimento;
	getFrameContentById('jContaReceber').$('vlAcrescimo').value = vlAcrescimo;
	
	
}
var columnsResultados = [{label:'Descrição', reference:'ERRO'}];
function miReceberOnClick(content){
    if(content==null){
        createConfirmbox("dialog", {caption: "Recebimento de Cheques",
                                        width: 300, 
                                        height: 50, 
                                        message: "Você tem certeza que deseja receber os cheques de "+$('dtInicialGrid').value+" ate "+$('dtFinalGrid').value+"?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("miReceberOnClickAux()", 10)}});
    }
    else{
    	closeWindow('jProcessando');
    	var ret = processResult(content, '');
        if(ret.code > 0){
            createTempbox("jTemp", {width: 200, 
									height: 50, 
									message: ret.message, 
									time: 3000});
            closeWindow('jRecebimentoAutomatico');
        }
        else{
			gridErros = GridOne.create('gridErros', {columns: columnsResultados,
											        resultset: ret.objects.ERRO, plotPlace: $('gridResultados'),
													   noHeader: false, noSelectorColumn: true, strippedLines: false, lineSeparator: false, columnSeparator: false,
													   onProcessRegister: function(reg) { },
													   noSelectOnCreate: false});
		}
//         else
//             createTempbox("jTemp", {width: 270, height: 80, message: ret.message, time: 5000});
    }	
}

function miReceberOnClickAux(content){
	if (content == null) {
		createTempbox("jProcessando", {width: 200, height: 45, modal: true, message: "Processando, aguarde...", boxType: "LOADING", time: 0});
		getPage("GET", "miReceberOnClick", "../methodcaller?className=com.tivic.manager.adm.ContaReceberServices"+
										   "&method=gerarRecebimentos(const "+$('cdEmpresa').value+":int, const "+'<%=cdUsuario%>'+":int,const "+$('dtInicialGrid').value+":GregorianCalendar,const "+$('dtFinalGrid').value+":GregorianCalendar)",
										   null, null, null, null);
	}
	
}

function onChange(opcao) {
	$('tpData').disabled  = (opcao == 2);
	$('tpData').className = ($('tpData').disabled ? 'disabledSelect2' : 'select2');
	if(opcao == 2)
		$('tpData').value = 1;	
		
}
</script>
</head>
<body class="body" onload="init();">
	<div style="width: 1000px;" id="ManutencaoContaReceber" class="d1-form">
   		<div style="width: 1000px; height: 480px;" class="d1-body">
   			<div class="d1-toolBar" id="toolBarGrid" style="width:150px; height:160px; float:left; margin-right: 5px;"></div>
			<input idform="ContaReceber" reference="cd_empresa" datatype="INT" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>" />
			<input idform="ContaReceber" reference="cd_conta" id="cdConta" name="cdConta" type="hidden" value="0" defaultValue="0"/>
			<input idform="ContaReceber" reference="cd_movimento_conta" id="cdMovimentoConta" name="cdMovimentoConta" type="hidden" value="0" defaultValue="0" />
	 		<input idform="ContaReceber" reference="cd_pessoa" id="cdPessoa" name="cdPessoa" type="hidden" value="<%=cdPessoa%>" />
	 		<div style="border:1px solid #999; padding:2px 2px 2px 4px; height: 150px; margin-bottom: 2px; width:830px; float: left; margin-left: 5px;">
		 		<div class="d1-line" id="line0">
		 			<div style="width: 405px;" class="element">
		                  <label class="caption" id="">Sacado / Cliente</label>
		                  <input reference="cd_cliente" datatype="INT" id="cdCliente" name="cdCliente" type="hidden"/>
		                  <input tabindex="11" style="width: 370px;"  reference="nm_cliente" static="true" disabled="disabled" class="disabledField2" name="cdClienteView" id="cdClienteView" type="text"/>
		                  <button  tabindex="12" id="btnPesquisarPessoa" title="Pesquisar Sacado / Cliente..." class="controlButton controlButton2" onclick="btnFindClienteOnClick()" style="height:22px;"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
		                  <button  title="Limpar este campo..." class="controlButton" onclick="btnClearClienteOnClick()" style="height:22px;"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
	                </div>	
	                <div class="element" style="width:110px; margin-left: 3px;">
	                    <label for="tpData" class="caption">Data de</label>
	                    <select style="width:106px; font-size: 12px;" type="text" name="tpData" id="tpData" class="select2">
	                        <option value="-1">Ignorar</option>
	                        <option value="0" selected="selected">Vencimento</option>
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
	            </div>
			</div>	
	 		<div style="border:1px solid #999; padding:2px 2px 2px 4px; height: 46px; margin-bottom: 2px; width:995px; float: left;">
		 		<div class="d1-line" id="line0">
		 			<div style="width:95px;" class="element">
	                    <label class="caption">Data da Troca</label>
	                    <input name="dtTroca" type="text" class="field2" id="dtTroca" mask="##/##/####" maxlength="10" style="width:76px; " value="<%=dtTroca%>" datatype="DATE"/>
	                    <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtTroca" class="controlButton" style="height:22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
	                </div>
		 			<div style="width: 683px; margin-left: 3px;" class="element">
		                  <label class="caption" id="">Destinatário</label>
		                  <input reference="cd_destinatario" datatype="INT" id="cdDestinatario" name="cdDestinatario" type="hidden"/>
		                  <input tabindex="11" style="width: 648px;"  reference="nm_destinatario" static="true" disabled="disabled" class="disabledField2" name="cdDestinatarioView" id="cdDestinatarioView" type="text"/>
		                  <button  tabindex="12" id="btnPesquisarPessoa" title="Pesquisar Sacado / Destinatario..." class="controlButton controlButton2" onclick="btnFindDestinatarioOnClick()" style="height:22px;"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
		                  <button  title="Limpar este campo..." class="controlButton" onclick="btnClearDestinatarioOnClick()" style="height:22px;"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
	                </div>	
	                <div style="width: 93px; margin-left: 3px;" class="element">
						<label class="caption" for="prTaxa">Taxa</label>
						<input style="width: 87px; text-align:right;" mask="#,###.00" class="field2" datatype="FLOAT" id="prTaxa" name="prTaxa" reference="pr_taxa" type="text" defaultvalue="0,00"/>
				 	</div>
				</div>
			</div>	
			<div class="d1-line" id="line3" >
                <div style="width: 688px;" class="element">
					<label class="caption" id="labelResultado" style="font-weight:bold;">Resultado da Pesquisa</label>
                    <div id="divGridContaReceber" style="float:left; width: 997px; height:249px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
                </div>
                <div style="margin-left:10px;" class="element">
				  	<div class="element">
					  	<label class="caption">Valor Total:</label>
		          	</div>
				  	<div style="width: 70px; text-align: right; font-weight: bold;" class="element">
					  	<label class="caption" id="vlTotal">0,00</label>
		          	</div>
				  	<div class="element" style="margin-left: 15px;">
					  	<label class="caption">Acréscimo:</label>
		          	</div>
				  	<div style="width: 70px; text-align: right; font-weight: bold;" class="element">
					  	<label class="caption" id="vlTotalAcrescimo">0,00</label>
		          	</div>
				  	<div class="element" style="margin-left: 15px;">
					  	<label class="caption">Abatimento:</label>
		          	</div>
				  	<div style="width: 70px; text-align: right; font-weight: bold;" class="element">
					  	<label class="caption" id="vlTotalAbatimento">0,00</label>
		          	</div>
		          	<div class="element" style="margin-left: 15px;">
					  	<label class="caption">Total Recebido:</label>
		          	</div>
				  	<div style="width: 70px; text-align: right; font-weight: bold;" class="element">
					  	<label class="caption" id="vlTotalContaSelecao">0,00</label>
		          	</div>
		          	<div class="element" style="margin-left: 15px;">
					  	<label class="caption">Total A Receber:</label>
		          	</div>
				  	<div style="width: 70px; text-align: right; font-weight: bold;" class="element">
					  	<label class="caption" id="vlTotalSelecao">0,00</label>
		          	</div>
		        </div>
            </div>
        </div>
	</div>


    <div id="sumaryContaReceber" style="display:none; width:100%;">
        <table style="width:100%;" border="0" cellspacing="2" cellpadding="2" style="border-top:2px solid #000000">
          <tr style="font-size:11px; font-family:Geneva, Arial, Helvetica, sans-serif;">
            <td width="25%" nowrap="nowrap" ><strong>Total das Contas:</strong></td>
            <td width="25%" align="right" nowrap="nowrap" style="font-size:11px" id="VL_CONTA"></td>
            <td width="25%" nowrap="nowrap"><strong>Total recebido:</strong></td>
            <td width="25%" align="right" style="font-size:11px" nowrap="nowrap" id="VL_RECEBIDO"></td>
          </tr>
          <tr style="font-size:11px; font-family:Geneva, Arial, Helvetica, sans-serif;">
            <td width="25%" nowrap="nowrap" ><strong>Total acréscimos:</strong></td>
            <td width="25%" align="right" nowrap="nowrap" style="font-size:11px" id="VL_ACRESCIMO"></td>
            <td width="25%" nowrap="nowrap"><strong>Total descontos:</strong></td>
            <td width="25%" align="right" style="font-size:11px" nowrap="nowrap" id="VL_ABATIMENTO"></td>
          </tr>
        </table>
    </div>
    
    <div id="formEscolherFormaLancamento" class="d1-form" style="display:none; width:230px; height:125px">
      <div style="width: 230px; height: 125px;" class="d1-body">
 	    <div class="d1-line">
          <div style="width:400px;" class="element">
            <button id="btnLancarIndividualmente" title="Individualmente" onclick="btnBaixaContaReceberOnClick(null, false);" style="width:215px; height:25px; font-size:12px; margin-top: 5px;" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Confirmar uma por uma</button>
          </div>
          <div style="width:400px;"class="element">
            <button id="btnLancarAutomaticamente" title="Automaticamente" onclick="btnBaixaContaReceberOnClick(null, true, true);" style="width:215px; height:25px; font-size:12px; margin-top: 5px;" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Confirmar todos (com Juros)</button>
          </div>
          <div style="width:400px;"class="element">
            <button id="btnLancarAutomaticamente" title="Automaticamente" onclick="btnBaixaContaReceberOnClick(null, true, false);" style="width:215px; height:25px; font-size:12px; margin-top: 5px;" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Confirmar todos (sem Juros)</button>
          </div>
        </div>
      </div>  
    </div>
</body>
</html>