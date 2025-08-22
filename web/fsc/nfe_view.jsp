<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.util.Util"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.security.StatusPermissionActionUser" %>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="sol.util.Jso"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="com.tivic.manager.seg.AcaoServices"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.alm.*"%>
<%@page import="com.tivic.manager.fsc.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="aba2.0, shortcut, grid2.0, form, treeview2.0, toolbar, floatmenu, filter, calendario, report, flatbutton" compress="false"/>
<script language="javascript" src="/sol/js/im/JSJaC/json.js"></script>
<security:registerForm idForm="formDocumentoEntrada"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<%
	try {
		String today                = sol.util.Util.formatDateTime(new java.util.GregorianCalendar(), "dd/MM/yyyy");
		int cdEmpresa               = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		int cdUsuario               = RequestUtilities.getParameterAsInteger(request, "cdUsuario", 0);
		int cdLocalArmazenamento    = RequestUtilities.getParameterAsInteger(request, "cdLocalArmazenamento", 0);
		Empresa empresa             = EmpresaDAO.get(cdEmpresa);
%>
<script language="javascript">
const ENTRADA = 0;
const SAIDA = 1;
const NOT_IMPORT = 0;
const IMPORT = 1;
var tiposNotaFiscal     		= ["Qualquer", "Normal", "Contigência via FS", "Contigência com Scan", "Contigência via DPEC", "Contigência FSDA"];
var situacaoNotaFiscal  		= ["Qualquer", "Em Digitação", "Validada", "Transmitida", "Autorizada", "Cancelada", "Denegada"];
var tiposSaida         			= <%=Jso.getStream(DocumentoSaidaServices.tiposSaida)%>;
var tiposEntrada        		= <%=Jso.getStream(DocumentoEntradaServices.tiposEntrada)%>;
var situacaoDocumentoSaida     	= <%=Jso.getStream(DocumentoSaidaServices.situacoes)%>;
var situacaoDocumentoEntrada   	= <%=Jso.getStream(DocumentoEntradaServices.situacoes)%>;
var tiposDocumentoSaida 		= <%=Jso.getStream(DocumentoSaidaServices.tiposDocumentoSaida)%>;
var tiposDocumentoEntrada 		= <%=Jso.getStream(DocumentoEntradaServices.tiposDocumentoEntrada)%>;
var tiposMovimentoSaida    		= <%=Jso.getStream(DocumentoSaidaServices.tiposMovimento)%>;
var tiposMovimentoEntrada		= <%=Jso.getStream(DocumentoEntradaServices.tiposMovimento)%>;
var tiposFreteSaida    			= <%=Jso.getStream(DocumentoSaidaServices.tiposFrete)%>;
var tiposFreteEntrada  			= <%=Jso.getStream(DocumentoEntradaServices.tiposFrete)%>;

var gridNotasFiscais = null;
var rsmEstados      = <%=Jso.getStream(com.tivic.manager.grl.EstadoServices.getAll())%>;
var columnsNotasFiscais = [{label: '', reference:'LG_SELECIONADO', width: 30, type: GridOne._CHECKBOX, onCheck: function() {
								onSelecionadoChecked(this);
							}},
						   {label:'Série', reference:'NR_SERIE'},
						   {label:'Nº N.F.', reference:'NR_NOTA_FISCAL'},
						   {label:'Emissão', reference:'DT_EMISSAO', type:GridOne.DATA},
						   {label:'Dt. Aut.', reference:'DT_AUTORIZACAO', type:GridOne.DATA},
						   {label:'Situação', reference:'NM_ST_NOTA_FISCAL'},
						   {label:'Produtos', reference:'VL_TOTAL_PRODUTO', type: GridOne._CURRENCY},
						   {label:'Desc.', reference:'VL_DESCONTO', type: GridOne._CURRENCY},
						   {label:'Frete', reference:'VL_FRETE', type: GridOne._CURRENCY},
						   {label:'Seguro', reference:'VL_SEGURO', type: GridOne._CURRENCY},
						   {label:'Outr. Desp.', reference:'VL_OUTRAS_DESPESAS', type: GridOne._CURRENCY},
						   {label:'ICMS/ST', reference:'VL_ICMS_ST', type: GridOne._CURRENCY},
						   {label:'IPI', reference:'VL_IPI', type: GridOne._CURRENCY},
						   {label:'II', reference:'VL_II', type: GridOne._CURRENCY},
						   {label:'Valor Nota', reference:'VL_TOTAL_NOTA', type: GridOne._CURRENCY},
						   {label:'ICMS', reference:'VL_ICMS', type: GridOne._CURRENCY},
						   {label:'Destinatário: Nome [Fantasia]', reference:'NM_PESSOA'},
						   {label:'Razão Social', reference:'NM_RAZAO_SOCIAL'},
						   {label:'CPF/CNPJ Destinatário', reference:'NR_CPF_CNPJ'},
						   {label:'UF', reference:'SG_ESTADO'},
						   {label:'Tipo', reference:'NM_TP_NOTA_FISCAL'}];

var columnsResultados = [{label:'Número', reference:'NUMERO'},
						   {label:'Resultado', reference:'RESULTADO'}];						

function initNotaFiscal()	{
	ToolBar.create('toolBar',{plotPlace: 'toolBar', orientation: 'horizontal',
			  buttons: [{id: 'btnPesquisarNotaFiscal', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar', title: 'Pesquisar...', onClick: btnPesquisarNotaFiscalOnClick, imagePosition: 'top', width: 65}, {separator: 'horizontal'},
			            {id: 'btnPreviewNotaFiscal', img: '../fsc/imagens/preview24.png', label: 'Preview', title: 'Preview...', onClick: btnPreviewNotaFiscalOnClick, imagePosition: 'top', width: 65}, {separator: 'horizontal'},
			            {id: 'btnTarefasNotaFiscal', img: '../fsc/imagens/tarefas24.png', label: 'Nota Rápida', title: 'Realizar Validação, Transmissão e Consulta...', onClick: btnTarefasNotaFiscalOnClick, imagePosition: 'top', width: 65}, {separator: 'horizontal'},
					    {id: 'btnValidarNotaFiscal', img: '../fsc/imagens/validar24.jpg', label: 'Validar', title: 'Validar...', onClick: btnValidarNotaFiscalOnClick, imagePosition: 'top', width: 65}, {separator: 'horizontal'},
					    {id: 'btnTransmitirNotaFiscal', img: '../fsc/imagens/transmitir24.gif', label: 'Transmitir', title: 'Transmitir...', onClick: btnTransmitirNotaFiscalOnClick, imagePosition: 'top', width: 65}, {separator: 'horizontal'},
					    {id: 'btnAutorizarNotaFiscal', img: '../fsc/imagens/autorizar24.gif', label: 'Consultar', title: 'Consultar Autorização da SEFAZ...', onClick: btnAutorizarNotaFiscalOnClick, imagePosition: 'top', width: 65}, {separator: 'horizontal'},
					    {id: 'btnImprimirNotaFiscal', img: '/sol/imagens/print24.gif', label: 'DANFE', title: 'Imprimir DANFE...', onClick: btnImprimirNotaFiscalOnClick, imagePosition: 'top', width: 65}, {separator: 'horizontal'},
						{id: 'btnDanfeSimplificado', img: '/sol/imagens/print24.gif', label: 'DANFE Simplificado', title: 'DANFE Simplificado...', onClick: btnDanfeSimplificadoOnClick, imagePosition: 'top', width: 100},{separator: 'horizontal'},
					    {id: 'btnCancelarNotaFiscal', img: '../fsc/imagens/cancelar24.gif', label: 'Cancelar', title: 'Cancelar Nota Fiscal...', onClick: motivoCancelamento, imagePosition: 'top', width: 65}, {separator: 'horizontal'},
						{id: 'btnExcluirNotaFiscal', img: '../fsc/imagens/excluir24.gif', label: 'Excluir', title: 'Excluir Nota Fiscal...', onClick: btnExcluitNotaFiscalOnClick, imagePosition: 'top', width: 65}, {separator: 'horizontal'}, 
						{id: 'btnRelatorioNotaFiscal', img: '../fsc/imagens/relatorio24.gif', label: 'Relatório', title: 'Relatório de Notas Fiscais...', onClick: printReport, imagePosition: 'top', width: 65}, {separator: 'horizontal'}, 
						{id: 'btnDownload', img: '../fsc/imagens/xml24.png', label: 'Download', title: 'Download dos Xml\'s...', onClick: btnDownloadOnClick, imagePosition: 'top', width: 65}
						]});

	gridNotasFiscais = GridOne.create('gridNotasFiscais', {columns: columnsNotasFiscais,
								                           resultset: null, plotPlace: $('divGridNotasFiscais'),
														   noHeader: false, noSelectorColumn: true, strippedLines: false, lineSeparator: false, columnSeparator: false,
														   onProcessRegister: function(reg) { processNfe(reg); },
														   noSelectOnCreate: false});
	
	
	loadOptions($('tpNotaFiscal'), <%=Jso.getStream(NotaFiscalServices.tiposNotaFiscal)%>);
	loadOptions($('stNotaFiscal'), <%=Jso.getStream(NotaFiscalServices.situacaoNotaFiscal)%>);

	notaFiscalFields = [];
    loadFormFields(["notaFiscal"]);
}

function btnDanfeSimplificadoOnClick(content){
	if(content == null){
		getPage("POST", "btnDanfeSimplificadoOnClick", "../methodcaller?className=com.tivic.manager.fsc.NfeServices"
													+ "&method=imprimirDanfeSimplificado(const "+ gridNotasFiscais.getSelectedRowRegister()['CD_EMPRESA'] + ":int, const "+ gridNotasFiscais.getSelectedRowRegister()['CD_NOTA_FISCAL'] +":int )", null, true, null, null);	
	} else {// retorno
    	 var result = null;
         try {result = eval('(' + content + ')')} catch(e) {}
		if(result.code >= 1){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: result.message, msgboxType: "INFO"});			
   		}else{
   			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: result.message, msgboxType: "ERROR"});
   		}
    }
}

function btnDownloadOnClick (content) {
	if(content == null){
		var objects = "criterios=java.util.ArrayList();";
		var execute = "";
		var i = 0;
		objects += "item"+ i +"=sol.dao.ItemComparator(const cd_empresa:String,const "+$('cdEmpresa').value+":String,const "+_EQUAL+":int,const "+_INTEGER+":int);";
		execute += "criterios.add(*item" + i + ":Object);";
		i++;
		
		objects += "item"+ i +"=sol.dao.ItemComparator(const dt_inicio:String,const "+$('dtInicial').value+":String,const "+_EQUAL+":int,const "+_INTEGER+":int);";
		execute += "criterios.add(*item" + i + ":Object);";
		i++;
		
		objects += "item"+ i +"=sol.dao.ItemComparator(const dt_fim:String,const "+$('dtFinal').value+":String,const "+_EQUAL+":int,const "+_INTEGER+":int);";
		execute += "criterios.add(*item" + i + ":Object);";
		i++;
		
		objects += "item"+ i +"=sol.dao.ItemComparator(const tp_nota_fiscal:String,const "+$('tpNotaFiscal').value+":String,const "+_EQUAL+":int,const "+_INTEGER+":int);";
		execute += "criterios.add(*item" + i + ":Object);";
		i++;
		
		objects += "item"+ i +"=sol.dao.ItemComparator(const st_nota_fiscal:String,const "+$('stNotaFiscal').value+":String,const "+_EQUAL+":int,const "+_INTEGER+":int);";
		execute += "criterios.add(*item" + i + ":Object);";
		i++;
		
		objects += "item"+ i +"=sol.dao.ItemComparator(const nr_serie:String,const "+$('nrSerie').value+":String,const "+_EQUAL+":int,const "+_INTEGER+":int);";
		execute += "criterios.add(*item" + i + ":Object);";
		i++;
		
		objects += "item"+ i +"=sol.dao.ItemComparator(const nr_inicio:String,const "+$('nrInicial').value+":String,const "+_EQUAL+":int,const "+_INTEGER+":int);";
		execute += "criterios.add(*item" + i + ":Object);";
		i++;
		
		objects += "item"+ i +"=sol.dao.ItemComparator(const nr_fim:String,const "+$('nrFinal').value+":String,const "+_EQUAL+":int,const "+_INTEGER+":int);";
		execute += "criterios.add(*item" + i + ":Object);";
		i++;
		
		objects += "item"+ i +"=sol.dao.ItemComparator(const nr_chave_acesso:String,const "+$('nrChaveAcesso').value+":String,const "+_EQUAL+":int,const "+_INTEGER+":int);";
		execute += "criterios.add(*item" + i + ":Object);";
		i++;
		
		getPage("POST", "btnDownloadOnClick", "../methodcaller?className=com.tivic.manager.fsc.NfeServices"
												  	+ "&objects=" + objects
													+ "&execute=" + execute
													+ "&method=gerarXmlNFE(*criterios:java.util.ArrayList())", null, true, null, null);	
	}
    else {// retorno
    	 var result = null;
         try {result = eval('(' + content + ')')} catch(e) {}
		if(result.code >= 1){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: result.message, msgboxType: "INFO"});			
   		}else{
   			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: result.message, msgboxType: "ERROR"});
   		}
    }
}

function onSelecionadoChecked(checkbox){
	if(checkbox.checked){
		checkbox.parentNode.parentNode.register['LG_SELECIONADO'] = 1;
	}
	else{
		checkbox.parentNode.parentNode.register['LG_SELECIONADO'] = 0;
	}
}

function processNfe(reg){
	
}

function btnPesquisarNotaFiscalOnClick(content){
	if(content == null){
		createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
		getPage("POST", "btnPesquisarNotaFiscalOnClick", "../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices"+
												  "&method=findNotasFiscais(cdNotaFiscal: int, cdEmpresa: int, dtInicial: GregorianCalendar, dtFinal: GregorianCalendar, tpNotaFiscal: int, stNotaFiscal: int, nrSerie: String, nrInicial: String, nrFinal: String, nrCpfCnpj: String, nrChaveAcesso: String, nrDocumentoSaida: String)", notaFiscalFields, null, null, null);
	}
    else {// retorno
    	closeWindow('jLoadMsg');
    	var rsmNFe = null;
    	try {rsmNFe = eval('(' + content + ')')} catch(e) {}
		gridNotasFiscais = GridOne.create('gridNotasFiscais', {columns: columnsNotasFiscais, resultset: rsmNFe, plotPlace : $('divGridNotasFiscais'), 
			                                                   noHeader: false, noSelectorColumn: false, lineSeparator: false, columnSeparator: false,
															   onDoubleClick:function() {
																   btnShowNotaFiscalOnClick(gridNotasFiscais.getSelectedRowRegister()['CD_NOTA_FISCAL']); 
													 		   },
															   onProcessRegister: function(register) {
																	 			var reg = register;
																	 			
																	 			if(reg['NR_CPF'] != null && reg['NR_CNPJ'] == null)
																	 				reg['NR_CPF_CNPJ'] = reg['NR_CPF'];
																	 			
																	 			else if(reg['NR_CPF'] == null && reg['NR_CNPJ'] != null)
																	 				reg['NR_CPF_CNPJ'] = reg['NR_CNPJ'];
																	 			
																	 			if(reg['NM_TP_NOTA_FISCAL'] == null)
																	 				reg['NM_TP_NOTA_FISCAL'] = tiposNotaFiscal[reg['TP_EMISSAO']];
																	 			
																	 			if(reg['NM_ST_NOTA_FISCAL'] == null)
																	 				reg['NM_ST_NOTA_FISCAL'] = situacaoNotaFiscal[reg['ST_NOTA_FISCAL']];
																	 			if(reg['VL_DESCONTO'] <= 0)
																	 				reg['VL_DESCONTO'] = 0;
																	 			reg['DT_EMISSAO'] = reg['DT_EMISSAO'].substring(0, 10);
																	 			if(reg['DT_AUTORIZACAO'] != null)
																	 				reg['DT_AUTORIZACAO'] = reg['DT_AUTORIZACAO'].substring(0, 10);
																	 			reg['LG_SELECIONADO'] = 0;
																	 			if(reg['CD_PAIS_DEST'] == reg['CD_PAIS_EMI'])
																	 				reg['LG_IMPORT'] = 0;
																	 			else if(reg['CD_PAIS_DEST'] != reg['CD_PAIS_EMI'] && reg['TP_DOC_VINC'] == 0)
																	 				reg['LG_IMPORT'] = 1;
																	 			
																	 			if(reg['VL_ICMS_ST'] == '' || reg['VL_ICMS_ST'] == null){
																	 				reg['VL_ICMS_ST'] = "0,00";
																	 			}
																	 			if(reg['VL_II'] == '' || reg['VL_II'] == null){
																	 				reg['VL_II'] = "0,00";
																	 			}
																	 			if(reg['VL_IPI'] == '' || reg['VL_IPI'] == null){
																	 				reg['VL_IPI'] = "0,00";
																	 			}
																	 			if(reg['VL_ICMS'] == '' || reg['VL_ICMS'] == null){
																	 				reg['VL_ICMS'] = "0,00";
																	 			}
																	 			if(reg['VL_FRETE'] == '' || reg['VL_FRETE'] == null){
																	 				reg['VL_FRETE'] = "0,00";
																	 			}
																	 			if(reg['VL_SEGURO'] == '' || reg['VL_SEGURO'] == null){
																	 				reg['VL_SEGURO'] = "0,00";
																	 			}
																	 			if(reg['VL_OUTRAS_DESPESAS'] == '' || reg['VL_OUTRAS_DESPESAS'] == null){
																	 				reg['VL_OUTRAS_DESPESAS'] = "0,00";
																	 			}
																	 			if(reg['VL_DESCONTO'] == '' || reg['VL_DESCONTO'] == null){
																	 				reg['VL_DESCONTO'] = "0,00";
																	 			}
																	 			
																			}});
	}
}

function btnShowNotaFiscalOnClick(){
	
	var disabled    = gridNotasFiscais.getSelectedRowRegister()['ST_NOTA_FISCAL'] == 5;
	var autorizada    = gridNotasFiscais.getSelectedRowRegister()['ST_NOTA_FISCAL'] == 4;
	var linhaBotoes = [{type: 'space', width:40},
					   {id:'btnPreviewGrid', type:'button', label:'Preview da Nota', width: 20, height:19, image: '../fsc/imagens/preview16.png', onClick: function(){btnPreviewNotaFiscalOnClick();}},
					   {id:'btnSalvarGrid', type:'button', label:'Salvar e Sair', width: 20, height:19, image: '/sol/imagens/form-btSalvar16.gif', onClick: function(){btnSalvarESair();}},
					   {id:'btnRetornarGrid', type:'button', label:'Retornar', width: 20, height:19, image: '../fsc/imagens/retorno16.gif', onClick: function(){closeWindow('jNFE');}}];	
	if(disabled){
		linhaBotoes = [{type: 'space', width:40},
					   {id:'btnSalvarXml', type:'button', label:'Gerar Xml da Nota', width: 20, height:19, image: '../fsc/imagens/xml16.png', onClick: function(){gerarXmlOnClick();}},
					   {id:'btnPreviewGrid', type:'button', label:'DANFE', width: 20, height:19, image: '/sol/imagens/print24.gif', onClick: function(){btnImprimirNotaFiscalOnClick();}},
					   {id:'btnRetornarGrid', type:'button', label:'Retornar', width: 20, height:19, image: '../fsc/imagens/retorno16.gif', onClick: function(){closeWindow('jNFE');}}];
	}
	
	if(autorizada){
		linhaBotoes = [{type: 'space', width:20},
		               {id:'btnEnviarEmail', type:'button', label:'Enviar email', width: 20, height:19, image: '../fsc/imagens/email16.png', onClick: function(){btnEnviarEmailOnClick();}},
					   {id:'btnSalvarXml', type:'button', label:'Gerar Xml da Nota', width: 20, height:19, image: '../fsc/imagens/xml16.png', onClick: function(){gerarXmlOnClick();}},
					   {id:'btnPreviewGrid', type:'button', label:'DANFE', width: 20, height:19, image: '/sol/imagens/print24.gif', onClick: function(){btnImprimirNotaFiscalOnClick();}},
					   {id:'btnRetornarGrid', type:'button', label:'Retornar', width: 20, height:19, image: '../fsc/imagens/retorno16.gif', onClick: function(){closeWindow('jNFE');}}];
	}
	
	
	var aba1 = [{id:'divGridProdutosGrid', width:99, height: 213, type: 'grid'}];
	
	if(gridNotasFiscais.getSelectedRowRegister()['LG_IMPORT'] == 1)
		aba1 = [{id:'nrDeclaracaoImportacaoGrid', reference: 'nr_declaracao_importacao', type:'text', label:'Número da Declaração', width:20, disabled: (disabled || autorizada)},
				{id:'dtRegistroGrid', reference: 'dt_registro', type:'date', label:'Data do Registro', datatype: 'DATE', width:13, calendarPosition: 'Bl', disabled: (disabled || autorizada)},
	  			{id:'dsLocalDesembaracoGrid', reference: 'ds_local_desembaraco', type:'text', label:'Local do Desembaraço', width:53, disabled: (disabled || autorizada)},	
	  			{id:'dtDesembaracoGrid', reference: 'dt_desembaraco', type:'date', label:'Data do Desembaraço', datatype: 'DATE', width:13, calendarPosition: 'Br', disabled: (disabled || autorizada)},
	  			{id:'divGridProdutosGrid', width:99, height: 100, type: 'grid'},
	  			{id:'divGridAdicoesGrid', width:99, height: 100, type: 'grid'}];
	
	
	FormFactory.createFormWindow('jNFE', 
            {caption: "Nota Fiscal Eletrônica", width: 880, height: 450, unitSize: '%', modal: true, noTitle: true, 
			  id: 'formNotaFiscal', loadForm: true, noDrag: true, cssVersion: '2',
			  hiddenFields: [{id:'cdEmpresaGrid', reference: 'cd_empresa', value: <%=cdEmpresa%>},
			                 {id:'cdNotaFiscalGrid', reference: 'cd_nota_fiscal'},
			                 {id:'cdNaturezaOperacaoGrid', reference: 'cd_natureza_operacao'},
			                 {id:'stNotaFiscalGrid', reference: 'st_nota_fiscal'},
			                 {id:'cdDeclaracaoImportacaoGrid', reference: 'cd_declaracao_importacao'}],
			  lines: [[{id:'nrChaveAcessoGrid', reference: 'nr_chave_acesso', type:'text', label:'Chave de Acesso', width:55, disabled: true},
			           {id:'tpModeloGrid', reference: 'tp_modelo', type:'text', label:'Modelo', width:15, disabled: true},//Dados da Nota
			  		   {id:'nrSerieGrid', reference: 'nr_serie', type:'text', label:'Série', width:<%=(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 1, cdEmpresa) == 0) ? 15 : 30%>, disabled: true}
			  		 <%if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 1, cdEmpresa) == 0){%>
			  		   ,{id:'nrNotaFiscalGrid', reference: 'cd_tipo_documento', type:'text', label:'Número', width:15, disabled: (disabled || autorizada)}
					 <%}%>			  		   
			  		   ],
			  		  [{id:'dtEmissaoGrid', reference: 'dt_emissao', type:'date', label:'Data Emissão', datatype: 'DATE', width:11, calendarPosition: 'Bl', disabled: (disabled || autorizada)},
			  		   {id:'dtMovimentacaoGrid', reference: 'dt_movimentacao', type:'date', datatype: 'DATE', label:'Data Movimentação', width:11, calendarPosition: 'Bl', disabled: (disabled || autorizada)}],
			  		  [{id: 'cdDestinatarioGrid', reference: 'nm_destinatario', label:'Destinatario', width:58, type: 'lookup', disabled: true, 
							viewReference: 'cdDestinatarioGrid', findAction: function() { btnFindDestinatarioOnClick(null);}},//Dados do Destinatario
			  		   {id:'nrCpfCnpjGrid', reference: 'nr_cpf_cnpj', type:'text', label:'CPF / CNPJ', width:20, disabled: true},
			  		   {id:'nrInscricaoEstadualGrid', reference: 'nr_inscricao_estadual', type:'text', label:'Inscrição Estadual', width:20, disabled: true},
			  		   {id:'nmEmailGrid', reference: 'nm_email', type:'text', label:'Email', width:20, disabled: true},
			  		   {id:'nmLogradouroGrid', reference: 'nm_logradouro', type:'text', label:'Logradouro', width:30, disabled: true},
			  		   {id:'nrEnderecoGrid', reference: 'nr_endereco', type:'text', label:'Número', width:10, disabled: true},
			  		   {id:'nmBairroGrid', reference: 'nm_bairro', type:'text', label:'Bairro', width:20, disabled: true},
			  		   {id:'nmCidadeGrid', reference: 'nm_cidade', type:'text', label:'Cidade', width:25, disabled: true},
			  		   {id:'idIbgeGrid', reference: 'id_ibge', type:'text', label:'Cod. IBGE', width:10, disabled: true},
			  		   {id:'sgEstadoGrid', reference: 'sg_estado', type:'text', label:'UF', width:10, disabled: true},
			  		   {id:'nrCepGrid', reference: 'ne_cep', type:'text', label:'CEP', width:10, disabled: true},
			  		   {id:'nrTelefoneGrid', reference: 'nr_telefone', type:'text', label:'Telefone', width:20, disabled: true},
			  		   {id:'tpEmissaoGrid', type:'select', label:'Emissão', width: 25, options: [{value: 1, text: 'Normal'}, 
// 	  		 	                                                                                  {value: 2, text: 'Contingência via FS'},
// 	  		 	                                                                             	  {value: 3, text: 'Contingência com Scan'},
// 	  		 	                                                                          		  {value: 4, text: 'Contingência via DPEC'},
// 	  		 	                                                                          		  {value: 5, text: 'Contignência FSDA'},
// 	  		 	                                                                          	  	  {value: 6, text: 'Contingencia SVC-AN'},
	  		 	                                                                         		  {value: 7, text: 'Contingencia SVC-RS'}
// 	  		 	                                                                      			  ,{value: 9, text: 'Contingência off-line NFC-e'}
	  		 	                                                                         		  ]}],
			  		  [{id:'divTabNotaFiscal', type: 'panel', width: 100, cssStyle: 'margin-top: 4px;'},
			  		   {id:'divAba1', type: 'panel', width: 100,
			  			lines: [aba1]},
			  		   {id:'divAba2', type: 'panel', width: 100,
				  		lines: [[{id:'txtObservacao', referente: 'txt_observacao', label: 'Observação', width:99, height: 130, type: 'textarea', disabled:  (disabled || autorizada)}],
				  		        [{id: 'cdTransportadorGrid', reference: 'cd_transportador', label:'Transportadora', width:99, type: 'lookup', disabled: true, 
									viewReference: 'nm_transportador', findAction: function() { btnFindTransportadorOnClick(null);}, clearAction: function() {btnClearTransportadoraOnClick();}}],
					  		    [
					  		     {id: 'cdVeiculo', reference: 'cd_veiculo', label:'Veículo', width:54, type: 'lookup', disabled: true, 
									viewReference: 'ds_veiculo', findAction: function() {
// 										btnFindVeiculoOnClick();
									}, 
									clearAction: function() {
// 										btnClearVeiculoOnClick();
									}
								 },
// 								 {id: 'cdVeiculo', reference: 'cd_veiculo', label:'Veículo', width:54, type: 'select', options:[['', 'Selecione uma transportadora...']]},
								 {id:'nrPlaca', referente: 'nr_placa', label: 'Placa', width:15, type: 'text', disabled:  (disabled || autorizada), charcase: 'uppercase'},
					  		     {id:'sgUfVeiculo', referente: 'sg_uf_veiculo', label: 'UF/Veículo', width:15, type: 'select', options: [['','..']], disabled:  (disabled || autorizada)},
					  		     {id:'nrRntc', referente: 'nr_rntc', label: 'Nº RNTC', width:15, type: 'text', disabled:  (disabled || autorizada)}],
					  		    [{id:'qtVolume', referente: 'qt_volume', label: 'Quantidade', width:15, datatype:'INT', mask:'#', type: 'text', disabled:  (disabled || autorizada)},
				  		       	 {id:'dsEspecie', referente: 'ds_especie', label: 'Espécie', width:15, type: 'text', disabled:  (disabled || autorizada)},
				  		     	 {id:'dsMarca', referente: 'ds_marca', label: 'Marca', width:15, type: 'text', disabled:  (disabled || autorizada)},
				  		     	 {id:'dsNumeracao', referente: 'ds_numeracao', label: 'Numeração', width:24, type: 'text', disabled:  (disabled || autorizada)},
				  		     	 {id:'vlPesoBruto', referente: 'vl_peso_bruto', label: 'Peso Bruto', width:15, type: 'text', disabled:  (disabled || autorizada)},
				  		     	 {id:'vlPesoLiquido', referente: 'vl_peso_liquido', label: 'Peso Líquido', width:15, type: 'text', disabled:  (disabled || autorizada)}]]},
			  		   {id:'divAba3', type: 'panel', width: 100,
					  	lines: [[{id:'divGridDocumentosGrid', width:99, height: 235, type: 'grid'}]]},
				  	   {id:'divAba4', type: 'panel', width: 100,
						lines: [[{id:'divGridHistorico', width:99, height: 235, type: 'grid'}]]}], 
					  linhaBotoes]});
	$('divTabNotaFiscal').style.cssText += 'margin-top: 4px;';
	$('sgUfVeiculo').options.length = 1;
		
	var nrPlacaMask = new Mask("***####");
	nrPlacaMask.attach($("nrPlaca"));
	loadOptionsFromRsm($('sgUfVeiculo'), rsmEstados, {fieldValue: 'SG_ESTADO', fieldText: 'SG_ESTADO'});
	TabOne.create('tabDocumento', {width: 869, height: 270, plotPlace: 'divTabNotaFiscal', tabPosition: ['bottom', 'left'],
									    tabs: [{caption: 'Itens (Produtos)', reference:'divAba1', image: '../grl/imagens/produto16.gif', active: true},
									           {caption: 'Observação e Transporte', reference:'divAba2', image: '../alm/imagens/transportadora16.gif'},
									           {caption: 'Documentos Vinculados', reference:'divAba3', image: '../alm/imagens/documento_saida16.gif'},
									           {caption: 'Histórico de Eventos', reference:'divAba4', image: '../grl/imagens/substituir16.gif'}]});	
	setTimeout('loadNotaFiscal(null); loadProdutos(null); loadHistorico(null);', 10);
	if(gridNotasFiscais.getSelectedRowRegister()['LG_IMPORT'] == 1)
		setTimeout('loadAdicoes(null);', 10);
	if(gridNotasFiscais.getSelectedRowRegister()['TP_DOC_VINC'] == 0)
		setTimeout('loadDocumentosEntrada(null);', 10);
	if(gridNotasFiscais.getSelectedRowRegister()['TP_DOC_VINC'] == 1)
		setTimeout('loadDocumentosSaida(null);', 10);
}



/**
 * Máscaras nos campos javascript
 */
function mascara(o,f){
    v_obj=o
    v_fun=f
	execmascara();
}

function execmascara(){
    v_obj.value=v_fun(v_obj.value)
}

function telefone(v){
    v=v.replace(/\D/g,"")                 //Remove tudo o que não é dígito
    v=v.replace(/^(\d\d)(\d)/g,"($1) $2") //Coloca parênteses em volta dos dois primeiros dígitos
    v=v.replace(/(\d{4})(\d)/,"$1-$2")    //Coloca hífen entre o quarto e o quinto dígitos
    return v
}

function cpf(v){
    v=v.replace(/\D/g,"")                    //Remove tudo o que não é dígito
    v=v.replace(/(\d{3})(\d)/,"$1.$2")       //Coloca um ponto entre o terceiro e o quarto dígitos
    v=v.replace(/(\d{3})(\d)/,"$1.$2")       //Coloca um ponto entre o terceiro e o quarto dígitos
                                             //de novo (para o segundo bloco de números)
    v=v.replace(/(\d{3})(\d{1,2})$/,"$1-$2") //Coloca um hífen entre o terceiro e o quarto dígitos
    return v
}

function cep(v){
    v=v.replace(/D/g,"")                //Remove tudo o que não é dígito
    v=v.replace(/^(\d{5})(\d)/,"$1-$2") //Esse é tão fácil que não merece explicações
    return v
}

function cnpj(v){
    v=v.replace(/\D/g,"")                           //Remove tudo o que não é dígito
    v=v.replace(/^(\d{2})(\d)/,"$1.$2")             //Coloca ponto entre o segundo e o terceiro dígitos
    v=v.replace(/^(\d{2})\.(\d{3})(\d)/,"$1.$2.$3") //Coloca ponto entre o quinto e o sexto dígitos
    v=v.replace(/\.(\d{3})(\d)/,".$1/$2")           //Coloca uma barra entre o oitavo e o nono dígitos
    v=v.replace(/(\d{4})(\d)/,"$1-$2")              //Coloca um hífen depois do bloco de quatro dígitos
    return v
}
/**
 * End - Máscaras nos campos javascript
 */

function loadNotaFiscal(content){
	if(content == null){
		$('cdNotaFiscalGridTemp').value = gridNotasFiscais.getSelectedRowRegister()['CD_NOTA_FISCAL'];
		$('nrCfopGridTemp').value       = gridNotasFiscais.getSelectedRowRegister()['NR_CFOP'];
		getPage('POST', 'loadNotaFiscal', 
				   '../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices'+
				   '&method=getWithDest(const '+$('cdNotaFiscalGridTemp').value+':int)', null, null, null, null);	
	}
	
	else{
		
		try { rsmNotaFiscal = eval("("+content+")"); } catch(e) {};
        //
		$('cdEmpresaGrid').value 			= rsmNotaFiscal.lines[0]['CD_EMPRESA'];
		$('cdNotaFiscalGrid').value 		= rsmNotaFiscal.lines[0]['CD_NOTA_FISCAL'];
		$('cdNaturezaOperacaoGrid').value 	= rsmNotaFiscal.lines[0]['CD_NATUREZA_OPERACAO'];
		$('tpModeloGrid').value 			= rsmNotaFiscal.lines[0]['TP_MODELO'];
		$('nrSerieGrid').value 				= rsmNotaFiscal.lines[0]['NR_SERIE'];
		<%if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 1, cdEmpresa) == 0){%>
			$('nrNotaFiscalGrid').value 		= rsmNotaFiscal.lines[0]['NR_NOTA_FISCAL'];
		<%}%>
		$('dtEmissaoGrid').value 			= rsmNotaFiscal.lines[0]['DT_EMISSAO'];
		$('dtMovimentacaoGrid').value 		= rsmNotaFiscal.lines[0]['DT_MOVIMENTACAO'];
		if(rsmNotaFiscal.lines[0]['NR_CHAVE_ACESSO'])
			$('nrChaveAcessoGrid').value 		= rsmNotaFiscal.lines[0]['NR_CHAVE_ACESSO'].substring(3);
		$('cdDestinatarioGrid').value 		= rsmNotaFiscal.lines[0]['CD_DESTINATARIO'];
		$('cdDestinatarioGridView').value  	= rsmNotaFiscal.lines[0]['NM_DESTINATARIO'];
		$('nrCpfCnpjGrid').value           	= ((rsmNotaFiscal.lines[0]['NR_CPF'] != null) ? rsmNotaFiscal.lines[0]['NR_CPF'] : rsmNotaFiscal.lines[0]['NR_CNPJ']);
		$('nrInscricaoEstadualGrid').value 	= rsmNotaFiscal.lines[0]['NR_INSCRICAO_ESTADUAL'];
		$('nmEmailGrid').value 				= rsmNotaFiscal.lines[0]['NM_EMAIL'];
		$('nmLogradouroGrid').value 		= (rsmNotaFiscal.lines[0]['NM_TIPO_LOGRADOURO']!=null ? rsmNotaFiscal.lines[0]['NM_TIPO_LOGRADOURO']+' ' : '')+  
		                                      rsmNotaFiscal.lines[0]['NM_LOGRADOURO'];
		$('nrEnderecoGrid').value 			= rsmNotaFiscal.lines[0]['NR_ENDERECO'];
		$('nmBairroGrid').value 			= rsmNotaFiscal.lines[0]['NM_BAIRRO'];
		$('nmCidadeGrid').value 			= rsmNotaFiscal.lines[0]['NM_CIDADE'] + " - " + rsmNotaFiscal.lines[0]['SG_ESTADO'];
		$('idIbgeGrid').value 				= rsmNotaFiscal.lines[0]['ID_IBGE'];
		$('sgEstadoGrid').value 			= rsmNotaFiscal.lines[0]['SG_ESTADO'];
		$('nrCepGrid').value 				= rsmNotaFiscal.lines[0]['NR_CEP'];
		
		nrTelefone = rsmNotaFiscal.lines[0]['NR_TELEFONE1'];
		if(!nrTelefone) {
			nrTelefone = rsmNotaFiscal.lines[0]['NR_TELEFONE2'];
		}
		if(!nrTelefone) {
			nrTelefone = rsmNotaFiscal.lines[0]['NR_CELULAR'];
		}
		if(!nrTelefone) {
			nrTelefone = rsmNotaFiscal.lines[0]['NR_FAX'];
		}
		if(!nrTelefone){
			nrTelefone = rsmNotaFiscal.lines[0]["CONTATO_NR_TELEFONE1"];
		}
		if(!nrTelefone){
			nrTelefone = rsmNotaFiscal.lines[0]["CONTATO_NR_TELEFONE2"];
		}
		if(!nrTelefone){
			nrTelefone = rsmNotaFiscal.lines[0]["CONTATO_NR_CELULAR"];
		}
		if(!nrTelefone){
			nrTelefone = rsmNotaFiscal.lines[0]["CONTATO_NR_CELULAR2"];
		}
		if(!nrTelefone){
			nrTelefone = rsmNotaFiscal.lines[0]["CONTATO_NR_CELULAR3"];
		}
		if(!nrTelefone){
			nrTelefone = rsmNotaFiscal.lines[0]["CONTATO_NR_CELULAR4"];
		}
		$('nrTelefoneGrid').value = nrTelefone;
		
		$('tpEmissaoGrid').value 				= rsmNotaFiscal.lines[0]['TP_EMISSAO'];
		
		$('txtObservacao').value 			= rsmNotaFiscal.lines[0]['TXT_OBSERVACAO'];
		$('stNotaFiscalGrid').value 		= rsmNotaFiscal.lines[0]['ST_NOTA_FISCAL'];
		$('cdTransportadorGrid').value 		= rsmNotaFiscal.lines[0]['CD_TRANSPORTADOR'];
		$('cdTransportadorGridView').value  = rsmNotaFiscal.lines[0]['NM_TRANSPORTADOR'];
		$('qtVolume').value 		        = rsmNotaFiscal.lines[0]['QT_VOLUME'];
		$('dsEspecie').value 				= rsmNotaFiscal.lines[0]['DS_ESPECIE'];
		$('dsMarca').value 					= rsmNotaFiscal.lines[0]['DS_MARCA'];
		$('dsNumeracao').value 				= rsmNotaFiscal.lines[0]['DS_NUMERACAO'];
		$('vlPesoBruto').value 				= rsmNotaFiscal.lines[0]['VL_PESO_BRUTO'];
		$('vlPesoLiquido').value 			= rsmNotaFiscal.lines[0]['VL_PESO_LIQUIDO'];
		$('cdVeiculo').value 				= rsmNotaFiscal.lines[0]['CD_VEICULO'];
		$('nrPlaca').value 					= rsmNotaFiscal.lines[0]['NR_PLACA'];
		$('sgUfVeiculo').value 				= rsmNotaFiscal.lines[0]['SG_UF_VEICULO'];
		$('nrRntc').value 					= rsmNotaFiscal.lines[0]['NR_RNTC'];
		//Apenas se for importacao
			
		
		if(gridNotasFiscais.getSelectedRowRegister()['LG_IMPORT'] == 1){
			$('cdDeclaracaoImportacaoGrid').value 	= rsmNotaFiscal.lines[0]['CD_DECLARACAO_IMPORTACAO'];
			$('nrDeclaracaoImportacaoGrid').value	= rsmNotaFiscal.lines[0]['NR_DECLARACAO_IMPORTACAO'];
			$('dtRegistroGrid').value 				= (rsmNotaFiscal.lines[0]['DT_REGISTRO'] == null) ? "" : rsmNotaFiscal.lines[0]['DT_REGISTRO'].substring(0, 10);
			$('dsLocalDesembaracoGrid').value 		= rsmNotaFiscal.lines[0]['DS_LOCAL_DESEMBARACO'];
			$('dtDesembaracoGrid').value 			= (rsmNotaFiscal.lines[0]['DT_DESEMBARACO'] == null) ? "" : rsmNotaFiscal.lines[0]['DT_DESEMBARACO'].substring(0, 10);
		}
		
		
		mascara(document.getElementById('nrTelefoneGrid'), telefone);
		mascara(document.getElementById('nrCpfCnpjGrid'), ((rsmNotaFiscal.lines[0]['NR_CPF'] != null) ? cpf : cnpj));
		mascara(document.getElementById('nrCepGrid'), cep);
		
// 		if($('cdTransportadorGrid').value > 0)
// 			loadVeiculos(null, $('cdTransportadorGrid').value);
		
	}
}

function btnFindDestinatarioOnClick(reg, funcCallback) {
	if(gridNotasFiscais.getSelectedRowRegister()['ST_NOTA_FISCAL'] > 2){
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Notas autorizadas, transmitidas ou denegadas não podem ser alteradas!", msgboxType: "INFO"});
		return;
	}
	
    if(!reg) {
    	if(funcCallback==null)
    		funcCallback = btnFindDestinatarioOnClick;	
		FilterOne.create("jFiltro", {caption:"Pesquisar Destinatario", width: 450, height: 325, top:50, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.PessoaServices", method: "find", allowFindAll: true, autoExecuteOnEnter: true,
								     filterFields: [[{label:"Nome [Fantasia]", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}],
								                    [{label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],
								     gridOptions: {columns: [{label:"Nome [Fantasia]", reference:"NM_PESSOA"},
								                             {label:"Razão Social", reference:"NM_RAZAO_SOCIAL"},
								                             {label:"Telefone", reference:"NR_TELEFONE1"},
								                             {label:"Email", reference:"NM_EMAIL"}], strippedLines: true, columnSeparator: false, lineSeparator: false},
									 hiddenFields: [{reference:"findEnderecoPrincipal",value:true, comparator:_EQUAL,datatype:_INTEGER}],
								     callback: funcCallback});
    }
    else {
        closeWindow("jFiltro");
        
        $('cdDestinatarioGrid').value      = reg[0]['CD_PESSOA'];
		$('cdDestinatarioGridView').value  = reg[0]['NM_PESSOA'];
		$('nrCpfCnpjGrid').value           = ((reg[0]['NR_CPF'] != null) ? reg[0]['NR_CPF'] : reg[0]['NR_CNPJ']);
		$('nrInscricaoEstadualGrid').value = reg[0]['NR_INSCRICAO_ESTADUAL'];
		$('nmEmailGrid').value             = reg[0]['NM_EMAIL'];
		$('nmLogradouroGrid').value        = reg[0]['NM_LOGRADOURO'];
		$('nrEnderecoGrid').value          = reg[0]['NR_ENDERECO'];
		$('nmBairroGrid').value            = reg[0]['NM_BAIRRO'];
		$('nmCidadeGrid').value            = reg[0]['NM_CIDADE'] + " - " + reg[0]['SG_ESTADO'];
		$('idIbgeGrid').value              = reg[0]['ID_IBGE'];
		$('sgEstadoGrid').value 		   = reg[0]['SG_ESTADO'];
		$('nrCepGrid').value               = reg[0]['NR_CEP'];
		$('nrTelefoneGrid').value 		   = reg[0]['NR_TELEFONE1']; 		
    }
}

function btnFindTransportadorOnClick(reg, funcCallback) {
	if(gridNotasFiscais.getSelectedRowRegister()['ST_NOTA_FISCAL'] > 2){
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Notas autorizadas, canceladas, transmitidas ou denegadas não podem ser alteradas!", msgboxType: "INFO"});
		return;
	}
	
    if(!reg) {
    	if(funcCallback==null)
    		funcCallback = btnFindTransportadorOnClick;	
		FilterOne.create("jFiltro", {caption:"Pesquisar Transportador(a)", width: 450, height: 325, top:50, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.PessoaServices", method: "find", allowFindAll: true, autoExecuteOnEnter: true,
									 filterFields: [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100}]],
								     gridOptions: {columns: [{label:"Nome", reference:"NM_PESSOA"},
								                             {label:"Telefone", reference:"NR_TELEFONE1"},
								                             {label:"Email", reference:"NM_EMAIL"}], strippedLines: true, columnSeparator: false, lineSeparator: false},
								     hiddenFields: [{reference:"findEnderecoPrincipal",value:true, comparator:_EQUAL,datatype:_INTEGER}],                         
								     callback: funcCallback});
    }
    else {
        closeWindow("jFiltro");
        
        $('cdTransportadorGrid').value      = reg[0]['CD_PESSOA'];
		$('cdTransportadorGridView').value  = reg[0]['NM_PESSOA'];
    
    }
}

function btnClearTransportadoraOnClick(){
   if(gridNotasFiscais.getSelectedRowRegister()['ST_NOTA_FISCAL'] > 2){
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Notas autorizadas, transmitidas ou denegadas não podem ser alteradas!", msgboxType: "INFO"});
		return;
   }
   $('cdTransportadorGrid').value = '';
   $('cdTransportadorGridView').value = '';
}

function btnSalvarESair(content){
	if(content == null)	{
		if($('stNotaFiscalGrid').value!=<%=NotaFiscalServices.ST_EM_DIGITACAO%> && $('stNotaFiscalGrid').value!=<%=NotaFiscalServices.ST_VALIDADA%>) {
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Notas autorizadas, transmitidas ou denegadas não podem ser alteradas!", msgboxType: "INFO"});
			return;
		}
		var objects = 'lista=java.util.ArrayList();';
		var execute = '';
		// Objetos
		objects += 'nota=com.tivic.manager.fsc.NotaFiscal();';
		objects += 'dtEmissao=java.util.GregorianCalendar();';
		objects += 'dtMovimentacao=java.util.GregorianCalendar();';
		
		// Executes
		execute += 'dtEmissao=com.tivic.manager.util.Util.convStringToCalendar(const '+$('dtEmissaoGrid').value+':String);';
		execute += 'dtMovimentacao=com.tivic.manager.util.Util.convStringToCalendar(const '+$('dtMovimentacaoGrid').value+':String);';
		execute += 'nota=com.tivic.manager.fsc.NotaFiscalDAO.get(const ' + $('cdNotaFiscalGridTemp').value + ':int);';
		execute += 'nota.setCdDestinatario(const ' + $('cdDestinatarioGrid').value + ':int);';
		execute += 'nota.setDtEmissao(*dtEmissao:GregorianCalendar);';
		execute += 'nota.setDtMovimentacao(*dtMovimentacao:GregorianCalendar);';
		<%if(ParametroServices.getValorOfParametroAsInteger("LG_GERAR_NUMERO_NOTA_AUTORIZACAO", 1, cdEmpresa) == 0){%>
			execute += 'nota.setNrNotaFiscal(const '+$('nrNotaFiscalGrid').value+':String);';
		<%}%>
		execute += 'nota.setStNotaFiscal(const 1:int);';
		execute += 'nota.setTxtObservacao(txtObservacao:String);';
		execute += 'nota.setQtVolume(const '+$('qtVolume').value+':String);';
		execute += 'nota.setDsEspecie(dsEspecie:String);';
		execute += 'nota.setDsMarca(dsMarca:String);';
		execute += 'nota.setDsNumeracao(dsNumeracao:String);';
		execute += 'nota.setVlPesoBruto(const '+changeLocale('vlPesoBruto')+':float);';
		execute += 'nota.setVlPesoLiquido(const '+changeLocale('vlPesoLiquido')+':float);';
		execute += 'nota.setCdTransportador(cdTransportadorGrid:int);';
		execute += 'nota.setCdVeiculo(cdVeiculo:int);';
		execute += 'nota.setNrPlaca(nrPlaca:String);';
		execute += 'nota.setSgUfVeiculo(sgUfVeiculo:String);';
		execute += 'nota.setNrRntc(nrRntc:String);';
		execute += 'nota.setTpEmissao(const ' + $('tpEmissaoGrid').value + ':int);';
		if(gridNotasFiscais.getSelectedRowRegister()['LG_IMPORT'] == 1){
			objects += 'declaracao=com.tivic.manager.fsc.DeclaracaoImportacao();';
			objects += 'dtRegistro=java.util.GregorianCalendar();';
			objects += 'dtDesembaraco=java.util.GregorianCalendar();';
			execute += 'dtRegistro=com.tivic.manager.util.Util.convStringToCalendar(const '+$('dtRegistroGrid').value+':String);';
			execute += 'dtDesembaraco=com.tivic.manager.util.Util.convStringToCalendar(const '+$('dtDesembaracoGrid').value+':String);';
			execute += 'declaracao=com.tivic.manager.fsc.DeclaracaoImportacaoDAO.get(const ' + $('cdDeclaracaoImportacaoGrid').value + ':int);';
			execute += 'declaracao.setNrDeclaracaoImportacao(const '+$('nrDeclaracaoImportacaoGrid').value+':String);';
			execute += 'declaracao.setDtRegistro(*dtRegistro:GregorianCalendar);';
			execute += 'declaracao.setDsLocalDesembaraco(const '+$('dsLocalDesembaracoGrid').value+':String);';
			execute += 'declaracao.setDtDesembaraco(*dtDesembaraco:GregorianCalendar);';
			objects +=  'lista2=java.util.ArrayList();';
			//Adicionando alteracao das adicoes
			for(var i = 0; i < gridAdicoes.getResultSet().lines.length; i++){
				objects += 'adicao'+i+'=com.tivic.manager.fsc.Adicao();';
				execute += 'adicao'+i+'=com.tivic.manager.fsc.AdicaoDAO.get(const ' + gridAdicoes.getResultSet().lines[i]['CD_ADICAO'] + ':int);';
				execute += 'adicao'+i+'.setVlVmcv(const '+gridAdicoes.getResultSet().lines[i]['VL_VMCV']+':float);';
				execute += 'adicao'+i+'.setQtPesoLiquido(const '+gridAdicoes.getResultSet().lines[i]['QT_PESO_LIQUIDO']+':float);';
				execute += 'adicao'+i+'.setNrAdicao(const '+gridAdicoes.getResultSet().lines[i]['NR_ADICAO']+':int);';
				execute += 'adicao'+i+'.setVlDesconto(const '+gridAdicoes.getResultSet().lines[i]['VL_DESCONTO']+':float);';
				execute += 'adicao'+i+'.setNrPedidoCompra(const '+gridAdicoes.getResultSet().lines[i]['NR_PEDIDO_COMPRA']+':String);';
				execute += 'adicao'+i+'.setVlAduaneiro(const '+gridAdicoes.getResultSet().lines[i]['VL_ADUANEIRO']+':float);';
				execute += 'lista2.add(*adicao'+i+':Object);';	
			}
			
		}
		
		//Adicionando alteracao dos itens
		for(var i = 0; i < gridProdutos.getResultSet().lines.length; i++){
			objects += 'registro'+i+'=java.util.HashMap();';
			objects += 'cdNotaFiscal'+i+'=java.lang.Integer(const ' +gridProdutos.getResultSet().lines[i]['CD_NOTA_FISCAL'] + ':int);';
			objects += 'cdItem'+i+'=java.lang.Integer(const ' +gridProdutos.getResultSet().lines[i]['CD_ITEM'] +':int);';
			objects += 'nrCodigoFiscal'+i+'=java.lang.Integer(const ' +gridProdutos.getResultSet().lines[i]['NR_CODIGO_FISCAL'] +':int);';
			objects += 'txtInformacaoAdicional'+i+'=java.lang.String(const ' +gridProdutos.getResultSet().lines[i]['TXT_INFORMACAO_ADICIONAL'] +':String);';
			execute += 'registro'+i+'.put(const cdNotaFiscal:Object, *cdNotaFiscal'+i+':Object);';	
			execute += 'registro'+i+'.put(const cdItem:Object, *cdItem'+i+':Object);';	
			execute += 'registro'+i+'.put(const nrCodigoFiscal:Object, *nrCodigoFiscal'+i+':Object);';	
			execute += 'registro'+i+'.put(const txtInformacaoAdicional:Object, *txtInformacaoAdicional'+i+':Object);';	
			execute += 'lista.add(*registro'+i+':Object);';	
		}
		
		getPage('GET2POST', 'btnSalvarESair', 
				   '../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices'+
				   '&objects=' + objects + 
				   '&execute=' + execute +
				   '&method=updateItem(*nota:com.tivic.manager.fsc.NotaFiscal, *lista:java.util.ArrayList' + ((gridNotasFiscais.getSelectedRowRegister()['LG_IMPORT'] == 1) ? ', *declaracao:com.tivic.manager.fsc.DeclaracaoImportacao, *lista2:java.util.ArrayList' : '') + ')', 
				   [$('txtObservacao'),$('qtVolume'),$('dsEspecie'),$('dsMarca'),$('dsNumeracao'),$('cdTransportadorGrid'),
				    $('cdVeiculo'),$('nrPlaca'),$('sgUfVeiculo'),$('nrRntc')], null, null, null);
	}
	else	{
		try { result = eval("("+content+")"); } catch(e) {};
		
		if(result.code > 0){
			if(gridNotasFiscais.getSelectedRowRegister()['ST_NOTA_FISCAL'] <= 2){
				createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: result.message, msgboxType: "INFO"});
			}
			closeWindow('jNFE');
			btnPesquisarNotaFiscalOnClick(null);
		}
		else	{
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: result.message, msgboxType: "ALERT"});
		}
	}
}

var gridNotaFiscal = null;
var rsmNotaFiscal = null;
var gridDocumentos = null;
var rsmDoc = null;


var columnsSaida = [{label: 'Data', reference: 'DT_DOCUMENTO_SAIDA', type: GridOne._DATE, referenceGroupBy: 'DT_DOCUMENTO_SAIDA', SHOW: 0},
					{label: 'Turno', reference: 'nm_turno', referenceGroupBy: 'nm_turno', SHOW: 0},
					{label: 'Nº Documento', reference: 'NR_DOCUMENTO_SAIDA', referenceGroupBy: 'cd_documento_saida', SHOW: 0},
					{label: 'Acrésc.', reference: 'VL_ACRESCIMO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
					{label: 'Desc.', reference: 'VL_DESCONTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
					{label: 'Valor', reference: 'VL_TOTAL_DOCUMENTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
					{label: 'Situação', reference: 'cl_st_documento_saida', referenceGroupBy: 'st_documento_saida'}, 
					{label: 'Vendedor', reference: 'nm_vendedor', referenceGroupBy: 'cd_vendedor'}, 
					{label: 'Tipo de Operação', reference: 'nm_tipo_operacao', referenceGroupBy: 'cd_tipo_operacao', SHOW: 0},
					{label: 'Tipo Saída', reference: 'cl_tp_saida', referenceGroupBy: 'tp_saida', SHOW: 0},
					{label: 'Tipo Movimento', reference: 'cl_tp_movimento_estoque', referenceGroupBy: 'tp_movimento_estoque', SHOW: 0}, 
					{label: 'Tipo Documento', reference: 'cl_tp_documento_saida', referenceGroupBy: 'tp_documento_saida', SHOW: 0}];

function loadDocumentosSaida(content){
	if(content == null)	{
		getPage('POST', 'loadDocumentosSaida', 
				   '../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices'+
				   '&method=findCompletoByNfe(const '+$('cdNotaFiscalGridTemp').value+':int)', null, null, null, null);	
	}
	
	else{
		
		try { rsmDoc = eval("("+content+")"); } catch(e) {};
		gridDocumentos = GridOne.create('gridDocumentos', {columns: columnsSaida,
			 resultset: rsmDoc,
			 plotPlace: $('divGridDocumentosGrid'),
			 onProcessRegister: function(reg)	{
				reg['CL_VL_ITEM']                 = reg['VL_UNITARIO']==null ? 0 : reg['VL_UNITARIO'] * reg['QT_SAIDA'] + reg['VL_ACRESCIMO'] - reg['VL_DESCONTO'];
			 	reg['CL_TP_SAIDA']                = tiposSaida[parseInt(reg['TP_SAIDA'], 10)];
			 	reg['CL_ST_DOCUMENTO_SAIDA']      = situacaoDocumentoSaida[parseInt(reg['ST_DOCUMENTO_SAIDA'], 10)];
			 	reg['CL_TP_DOCUMENTO_SAIDA']      = tiposDocumentoSaida[parseInt(reg['TP_DOCUMENTO_SAIDA'], 10)];
			 	reg['CL_TP_FRETE']                = tiposFreteSaida[parseInt(reg['TP_FRETE'], 10)];
				reg['QT_SAIDA_LOCAL']             = reg['QT_SAIDA_LOCAL']==null ? 0 : reg['QT_SAIDA_LOCAL'];
			 	reg['CL_TP_MOVIMENTO_ESTOQUE']    = tiposMovimentoSaida[parseInt(reg['TP_MOVIMENTO_ESTOQUE'], 10)];
				reg['CL_QT_SAIDA_LOCAL_PENDENTE'] = parseFloat(reg['QT_SAIDA'], 10) - (reg['QT_SAIDA_LOCAL']==null ? 0 : parseFloat(reg['QT_SAIDA_LOCAL'], 10));
				reg['CL_TOTAL_CUSTO']             = parseFloat(reg['VL_ULTIMO_CUSTO'], 10) * parseFloat(reg['QT_SAIDAS'], 10); 
				// Cor
				if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
					reg['NM_PRODUTO_SERVICO'] += ' - '+reg['TXT_ESPECIFICACAO'].toUpperCase();
				// Tamanho
				if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
					reg['NM_PRODUTO_SERVICO'] += ' - '+reg['TXT_DADO_TECNICO'].toUpperCase();
			 },
			 columnSeparator: false,
			 noSelectOnCreate: false});
	}
}
var columnsEntrada = [{label: 'Data', reference: 'DT_DOCUMENTO_ENTRADA', type: GridOne._DATE, referenceGroupBy: 'DT_DOCUMENTO_ENTRADA', SHOW: 0},
  					{label: 'Nº Documento', reference: 'NR_DOCUMENTO_ENTRADA', referenceGroupBy: 'cd_documento_entrada', SHOW: 0},
  					{label: 'Acrésc.', reference: 'VL_ACRESCIMO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
  					{label: 'Desc.', reference: 'VL_DESCONTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
  					{label: 'Valor', reference: 'VL_TOTAL_DOCUMENTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
  					{label: 'Situação', reference: 'cl_st_documento_entrada', referenceGroupBy: 'st_documento_entrada'}, 
  					{label: 'Tipo Entrada', reference: 'cl_tp_entrada', referenceGroupBy: 'tp_saida', SHOW: 0},
  					{label: 'Tipo Documento', reference: 'cl_tp_documento_entrada', referenceGroupBy: 'tp_documento_entrada', SHOW: 0}];					

function loadDocumentosEntrada(content){
	if(content == null)	{
		getPage('POST', 'loadDocumentosEntrada', 
				   '../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices'+
				   '&method=findCompletoByNfe(const '+$('cdNotaFiscalGridTemp').value+':int)', null, null, null, null);	
	}
	
	else{
		
		try { rsmDoc = eval("("+content+")"); } catch(e) {};
		gridDocumentos = GridOne.create('gridDocumentos', {columns: columnsEntrada,
			 resultset: rsmDoc,
			 plotPlace: $('divGridDocumentosGrid'),
			 onProcessRegister: function(reg)	{
				reg['CL_VL_ITEM']                 = reg['VL_UNITARIO']==null ? 0 : reg['VL_UNITARIO'] * reg['QT_ENTRADA'] + reg['VL_ACRESCIMO'] - reg['VL_DESCONTO'];
			 	reg['CL_TP_ENTRADA']              = tiposEntrada[parseInt(reg['TP_ENTRADA'], 10)];
			 	reg['CL_ST_DOCUMENTO_ENTRADA']      = situacaoDocumentoEntrada[parseInt(reg['ST_DOCUMENTO_ENTRADA'], 10)];
			 	reg['CL_TP_DOCUMENTO_ENTRADA']      = tiposDocumentoEntrada[parseInt(reg['TP_DOCUMENTO_ENTRADA'], 10)];
			 	reg['CL_TP_FRETE']                = tiposFreteEntrada[parseInt(reg['TP_FRETE'], 10)];
// 				reg['QT_SAIDA_LOCAL']             = reg['QT_SAIDA_LOCAL']==null ? 0 : reg['QT_SAIDA_LOCAL'];
			 	reg['CL_TP_MOVIMENTO_ESTOQUE']    = tiposMovimentoEntrada[parseInt(reg['TP_MOVIMENTO_ESTOQUE'], 10)];
// 				reg['CL_QT_SAIDA_LOCAL_PENDENTE'] = parseFloat(reg['QT_SAIDA'], 10) - (reg['QT_SAIDA_LOCAL']==null ? 0 : parseFloat(reg['QT_SAIDA_LOCAL'], 10));
// 				reg['CL_TOTAL_CUSTO']             = parseFloat(reg['VL_ULTIMO_CUSTO']) * parseFloat(reg['QT_SAIDAS']); 
				// Cor
				if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
					reg['NM_PRODUTO_SERVICO'] += ' - '+reg['TXT_ESPECIFICACAO'].toUpperCase();
				// Tamanho
				if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
					reg['NM_PRODUTO_SERVICO'] += ' - '+reg['TXT_DADO_TECNICO'].toUpperCase();
			 },
			 columnSeparator: false,
			 noSelectOnCreate: false});
	}
}

var rsmProdutos = null;
var columnsProdutos = [{label: 'Nome', reference: 'NM_PRODUTO_SERVICO'},
                       {label: 'Cod. Barras', reference: 'ID_PRODUTO_SERVICO'},
                       {label: 'ID Reduzido', reference: 'ID_REDUZIDO'},
					   {label: 'NCM', reference: 'NR_NCM'},
					   {label: 'CFOP', reference: 'NR_CODIGO_FISCAL', type: GridOne._CONTROL, controlWidth: '35px', style: 'width: 40px; text-align: right;', datatype: 'INT'},
					   {label: 'Medida', reference: 'SG_UNIDADE_MEDIDA'},
					   {label: 'QT. Tributario', reference: 'CL_QT_TRIBUTARIO', style: 'text-align: right;'}, 
					   {label: 'Vl. Unitário', reference: 'CL_VL_UNITARIO', style: 'text-align: right;'}, 
					   {label: 'Vl. Total', reference: 'VL_TOTAL', type: GridOne._CURRENCY}];

var gridProdutos = null;
function loadProdutos(content){
	if(content == null){
		getPage('POST', 'loadProdutos', 
				   '../methodcaller?className=com.tivic.manager.fsc.NotaFiscalItemServices'+
				   '&method=getAllByCdNfe(const '+$('cdNotaFiscalGridTemp').value+':int, const '+$('cdEmpresa').value+':int)', null, null, null, null);	
	}
	else	{
		try { rsmProdutos = eval("("+content+")"); } catch(e) { };
		var isReadOnly    = gridNotasFiscais.getSelectedRowRegister()['ST_NOTA_FISCAL'] > 2;
		columnsProdutos[4].type = isReadOnly ?  null : GridOne._CONTROL;
		if(gridNotasFiscais.getSelectedRowRegister()['LG_IMPORT'] == 1 && columnsProdutos.length < 10 && rsmProdutos.lines.length == 1){
			columnsProdutos.push({label: 'Informação Adicional', reference: 'TXT_INFORMACAO_ADICIONAL', type: GridOne._CONTROL, control: GridOne._TEXTAREA, controlWidth: '148px', controlHeight: '50px', style: 'width: 150px; text-align: right;', datatype: 'STRING'});
			columnsProdutos[9].type = isReadOnly ?  null : GridOne._CONTROL;
		}
		else if(gridNotasFiscais.getSelectedRowRegister()['LG_IMPORT'] == 1 && columnsProdutos.length == 10 && rsmProdutos.lines.length == 1){
			columnsProdutos[9].type = isReadOnly ?  null : GridOne._CONTROL;
		}
		else if(gridNotasFiscais.getSelectedRowRegister()['LG_IMPORT'] == 0 && columnsProdutos.length > 9){
			columnsProdutos.pop();
		}
		else if(gridNotasFiscais.getSelectedRowRegister()['LG_IMPORT'] == 1 && columnsProdutos.length > 9 && rsmProdutos.lines.length > 1){
			columnsProdutos.pop();
		}
		gridProdutos = GridOne.create('gridProdutos', {columns: columnsProdutos, resultset: rsmProdutos, plotPlace: $('divGridProdutosGrid'),
										 onDoubleClick: function() {
											 if(gridNotasFiscais.getSelectedRowRegister()['ST_NOTA_FISCAL'] > 2)	{
												createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Notas autorizadas, transmitidas ou denegadas não podem ser alteradas!", msgboxType: "INFO"});
												return;
											 }
											 if(gridProdutos.getSelectedRowRegister()['CD_PRODUTO_SERVICO'] > 0)
												 parent.createWindow('jProdutos', {caption: 'Produtos', width: 653, height: 435,
											            contentUrl: '../grl/produto.jsp?cdEmpresa=' + gridProdutos.getSelectedRowRegister()['CD_EMPRESA'] + '&cdProdutoServico=' + gridProdutos.getSelectedRowRegister()['CD_PRODUTO_SERVICO'] + '&tpProdutoServico=0' + '&cdLocalArmazenamento=' + $('cdLocalArmazenamento').value, onClose: function(){loadProdutos();}});
										 },
										 onProcessRegister: function(reg) {
											 try {
												 	var qtTributario = reg['QT_TRIBUTARIO'];
											 		var vlUnitario   = reg['VL_UNITARIO'];
												 	
													var sMask = '#';
													var precisao = parseInt(reg['NR_PRECISAO_MEDIDA'], 10);
													precisao     = isNaN(precisao) || precisao<=0 ? 0 : precisao;
													var qtTributarioTemp = precisao<=0 ? parseInt(qtTributario, 10) : qtTributario;
													for (var i=0; precisao>0 && i<precisao; i++)
														sMask += (i==0 ? '.' : '') + '0';
													var mask = new Mask(sMask, 'number');
													reg['CL_QT_TRIBUTARIO'] = mask.format(qtTributarioTemp);
													if(reg['SG_UNIDADE_MEDIDA']!=null && reg['SG_UNIDADE_MEDIDA']!='')
														reg['CL_QT_TRIBUTARIO'] += ' '+reg['SG_UNIDADE_MEDIDA'];
													sMask 		 = '#';
													precisao 	 = parseInt(reg['QT_PRECISAO_CUSTO'], 10);
													precisao     = isNaN(precisao) || precisao<=0 ? 0 : precisao;
													vlUnitarioTemp = precisao<=0 ? parseInt(vlUnitario, 10) : vlUnitario;
													for (var i=0; precisao>0 && i<precisao; i++)
														sMask += (i==0 ? '.' : '') + '0';
													mask = new Mask(sMask, 'number');
													reg['CL_VL_UNITARIO'] = mask.format(vlUnitarioTemp);
													
												}
												catch(e) {alert(e);} 
										 },
										 noFocusOnSelect : true, columnSeparator: false, noSelectOnCreate: false});
	}
}




var rsmAdicoes = null;
var columnsAdicoes = [{label: 'NCM', reference: 'NR_NCM'},
					   {label: 'VMCV', reference: 'VL_VMCV', type: GridOne._CONTROL, controlWidth: '95px', style: 'width: 100px; text-align: right;', datatype: 'FLOAT'},
					   {label: 'Peso Líquido', reference: 'QT_PESO_LIQUIDO', type: GridOne._CONTROL, controlWidth: '95px', style: 'width: 100px; text-align: right;', datatype: 'FLOAT'},
					   {label: 'Numero da Adição', reference: 'NR_ADICAO', type: GridOne._CONTROL, controlWidth: '95px', style: 'width: 100px; text-align: right;', datatype: 'STRING'},
					   {label: 'Desconto', reference: 'VL_DESCONTO', type: GridOne._CONTROL, controlWidth: '95px', style: 'width: 100px; text-align: right;', datatype: 'FLOAT'},
					   {label: 'Número do Pedido da Compra', reference: 'NR_PEDIDO_COMPRA', type: GridOne._CONTROL, controlWidth: '135px', style: 'width: 100px; text-align: right;', datatype: 'STRING'},
					   {label: 'Valor Aduaneiro', reference: 'VL_ADUANEIRO', type: GridOne._CONTROL, controlWidth: '95px', style: 'width: 100px; text-align: right;', datatype: 'FLOAT'}];

var gridAdicoes = null;
function loadAdicoes(content){
	if(content == null){
		getPage('POST', 'loadAdicoes', 
				   '../methodcaller?className=com.tivic.manager.fsc.AdicaoServices'+
				   '&method=getAllByCdNfe(const '+$('cdNotaFiscalGridTemp').value+':int)', null, null, null, null);	
	}
	else	{
		try { rsmAdicoes = eval("("+content+")"); } catch(e) { };
		var isReadOnly    = gridNotasFiscais.getSelectedRowRegister()['ST_NOTA_FISCAL'] > 2;
		columnsAdicoes[1].type = isReadOnly ?  null : GridOne._CONTROL;
		columnsAdicoes[2].type = isReadOnly ?  null : GridOne._CONTROL;
		columnsAdicoes[3].type = isReadOnly ?  null : GridOne._CONTROL;
		columnsAdicoes[4].type = isReadOnly ?  null : GridOne._CONTROL;
		columnsAdicoes[5].type = isReadOnly ?  null : GridOne._CONTROL;
		columnsAdicoes[6].type = isReadOnly ?  null : GridOne._CONTROL;
		gridAdicoes = GridOne.create('gridAdicoes', {columns: columnsAdicoes, resultset: rsmAdicoes, plotPlace: $('divGridAdicoesGrid'),
										 			onProcessRegister: function(reg) {
										 				if(reg['NR_PEDIDO_COMPRA'] == null)
										 					reg['NR_PEDIDO_COMPRA'] = "";
										 			},	
										 			noFocusOnSelect : true, columnSeparator: false, noSelectOnCreate: false});
	}
}




var gridHistorico = null;
function loadHistorico(content){
	if(content == null){
		getPage('POST', 'loadHistorico', 
				   '../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices'+
				   '&method=getHistoricoOf(const '+$('cdNotaFiscalGridTemp').value+':int)', null, null, null, null);	
	}
	
	else{
		try { rsmHistorico = eval("("+content+")"); } catch(e) { };
		gridHistorico = GridOne.create('gridHistorico', {columns: [{label: 'Nº Protocolo', reference: 'NR_PROTOCOLO'},
		                                                           {label: 'Data Envio', reference: 'DT_ENVIO', type: GridOne._DATETIME},
		                                                           {label: 'Recebimento', reference: 'DT_ENVIO', type: GridOne._DATETIME},
		                                                           {label: 'Mensagem', reference: 'DS_MENSAGEM'},
		                                                           {label: 'Status', reference: 'TP_STATUS'}], 
			                                             resultset: rsmHistorico, plotPlace: $('divGridHistorico'), 
														 noFocusOnSelect : true, columnSeparator: false, noSelectOnCreate: false});
	}
}

function btnValidarNotaFiscalOnClick(content){
	if(content == null){
		var execute = '';
		var objects = 'lista=java.util.ArrayList();';
		for (var i = 0; i < gridNotasFiscais.getResultSet().lines.length; i++){//Mudar de testando todos os registros para saber se estão selecionados, para guardar em uma lista os cdNotaFiscais dos selecionados depois só conferindo aqui
			if(gridNotasFiscais.getResultSet().lines[i]['LG_SELECIONADO'] == 1 && gridNotasFiscais.getResultSet().lines[i]['ST_NOTA_FISCAL'] == 1){
				objects += 'registro'+i+       '=java.util.HashMap();';
				objects += 'cdNotaFiscal'+i+'=java.lang.Integer(const ' +gridNotasFiscais.getResultSet().lines[i]['CD_NOTA_FISCAL'] + ':int);';
				objects += 'cdEmpresa'+i+'=java.lang.Integer(const ' +gridNotasFiscais.getResultSet().lines[i]['CD_EMPRESA'] + ':int);';
				execute += 'registro'+i+'.put(const cdNotaFiscal:Object, *cdNotaFiscal'+i+':Object);';	
				execute += 'registro'+i+'.put(const cdEmpresa:Object, *cdEmpresa'+i+':Object);';	
				execute += 'lista.add(*registro'+i+':Object);';	
			}
		}
		
		getPage("GET2POST", "btnValidarNotaFiscalOnClick", "../methodcaller?className=com.tivic.manager.fsc.NfeServices"+
												  '&objects=' + objects + 
												  '&execute=' + execute +
												  "&method=validarNFe(*lista:java.util.ArrayList())", null, true, null);
	}
	else{
		var resultado = null;
		try {resultado = eval('(' + content + ')')} catch(e) {}
		if(resultado.code > 0){
			FormFactory.createFormWindow('jResultados', {caption: "Resultados da Validação", width: 800, height: 400, noDrag: true,modal: true,
	            id: 'detalheResultados', unitSize: '%',
			    lines: [[{id:'dsMensagem', width:100, height: 30, type: 'caption', text: resultado.message}, 
			             {id:'divGridResultados', width:100, height: 320, type: 'grid'}]]});
			GridOne.create('gridNotasFiscais', {columns: columnsResultados, resultset :resultado.objects.resultado, plotPlace : $('divGridResultados'),
				 noHeader: false, noSelectorColumn: false, lineSeparator: false, columnSeparator: false});
			btnPesquisarNotaFiscalOnClick(null);
		}
		else
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: resultado.message, msgboxType: "INFO"});
		
	}
}

function btnTransmitirNotaFiscalOnClick(content){
	if(content == null){
		var execute = '';
		var objects = 'lista=java.util.ArrayList();';
		for (var i = 0; i < gridNotasFiscais.getResultSet().lines.length; i++){//Mudar de testando todos os registros para saber se estão selecionados, para guardar em uma lista os cdNotaFiscais dos selecionados depois só conferindo aqui
// 			if(gridNotasFiscais.getResultSet().lines[i]['LG_SELECIONADO'] == 1 && gridNotasFiscais.getResultSet().lines[i]['ST_NOTA_FISCAL'] == 2){
			if(gridNotasFiscais.getResultSet().lines[i]['ST_NOTA_FISCAL'] == 2){
				objects += 'registro'+i+       '=java.util.HashMap();';
				objects += 'cdNotaFiscal'+i+'=java.lang.Integer(const ' +gridNotasFiscais.getResultSet().lines[i]['CD_NOTA_FISCAL'] + ':int);';
				objects += 'cdEmpresa'+i+'=java.lang.Integer(const ' +gridNotasFiscais.getResultSet().lines[i]['CD_EMPRESA'] + ':int);';
				execute += 'registro'+i+'.put(const cdNotaFiscal:Object, *cdNotaFiscal'+i+':Object);';	
				execute += 'registro'+i+'.put(const cdEmpresa:Object, *cdEmpresa'+i+':Object);';
				execute += 'lista.add(*registro'+i+':Object);';	
			}
		}
		
		getPage("GET2POST", "btnTransmitirNotaFiscalOnClick", "../methodcaller?className=com.tivic.manager.fsc.NfeServices"+
												  '&objects=' + objects + 
												  '&execute=' + execute +
												  "&method=transmitirNFe(*lista:java.util.ArrayList())", null, true, null);
	}
	else{
		var resultado = null;
		try {resultado = eval('(' + content + ')')} catch(e) {}
		if(resultado.code > 0){
			FormFactory.createFormWindow('jResultados', {caption: "Resultados da Transmissão", width: 800, height: 400, noDrag: true,modal: true,
	            id: 'detalheResultados', unitSize: '%',
			    lines: [[{id:'dsMensagem', width:100, height: 30, type: 'caption', text: resultado.message},
			             {id:'divGridResultados', width:100, height: 320, type: 'grid'}]]});
			GridOne.create('gridNotasFiscais', {columns: columnsResultados, resultset :resultado.objects.resultado, plotPlace : $('divGridResultados'),
				 noHeader: false, noSelectorColumn: false, lineSeparator: false, columnSeparator: false});
			btnPesquisarNotaFiscalOnClick(null);
		}
		else
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: resultado.message, msgboxType: "INFO"});
		
	}
}

function btnAutorizarNotaFiscalOnClick(content){
	if(content == null){
		var execute = '';
		var objects = 'lista=java.util.ArrayList();';
		for (var i = 0; i < gridNotasFiscais.getResultSet().lines.length; i++){//Mudar de testando todos os registros para saber se estão selecionados, para guardar em uma lista os cdNotaFiscais dos selecionados depois só conferindo aqui
			if(gridNotasFiscais.getResultSet().lines[i]['ST_NOTA_FISCAL'] == 3){
				objects += 'registro'+i+       '=java.util.HashMap();';
				objects += 'cdNotaFiscal'+i+'=java.lang.Integer(const ' +gridNotasFiscais.getResultSet().lines[i]['CD_NOTA_FISCAL'] + ':int);';
				objects += 'cdEmpresa'+i+'=java.lang.Integer(const ' +gridNotasFiscais.getResultSet().lines[i]['CD_EMPRESA'] + ':int);';
				execute += 'registro'+i+'.put(const cdNotaFiscal:Object, *cdNotaFiscal'+i+':Object);';	
				execute += 'registro'+i+'.put(const cdEmpresa:Object, *cdEmpresa'+i+':Object);';
				execute += 'lista.add(*registro'+i+':Object);';	
			}
		}
		
		getPage("GET2POST", "btnAutorizarNotaFiscalOnClick", "../methodcaller?className=com.tivic.manager.fsc.NfeServices"+
												  '&objects=' + objects + 
												  '&execute=' + execute +
												  "&method=retornoProcessamento(*lista:java.util.ArrayList())", null, true, null);
	}
	else{
		var resultado = null;
		try {resultado = eval('(' + content + ')')} catch(e) {}
		if(resultado.code > 0){
			FormFactory.createFormWindow('jResultados', {caption: "Resultados da Consulta da SEFAZ", width: 800, height: 400, noDrag: true,modal: true,
	            id: 'detalheResultados', unitSize: '%',
			    lines: [[{id:'dsMensagem', width:100, height: 30, type: 'caption', text: resultado.message},
			             {id:'divGridResultados', width:100, height: 320, type: 'grid'}]]});
			GridOne.create('gridNotasFiscais', {columns: columnsResultados, resultset :resultado.objects.resultado, plotPlace : $('divGridResultados'),
				 noHeader: false, noSelectorColumn: false, lineSeparator: false, columnSeparator: false});
			btnPesquisarNotaFiscalOnClick(null);
		}
		else
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: resultado.message, msgboxType: "INFO"});
		
	}
}

function btnPreviewNotaFiscalOnClick(){
	
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
	
	parent.createWindow('jRelatorioContaPagar', {caption: 'DANFE', width: frameWidth-20, height: frameHeight-50, old: true,
			oldContentUrl: 'fsc/danfe.jsp?cdEmpresa=' + gridNotasFiscais.getSelectedRowRegister()['CD_EMPRESA'] + '&cdNotaFiscal=' + gridNotasFiscais.getSelectedRowRegister()['CD_NOTA_FISCAL'],
            contentUrl: '../fsc/danfe.jsp?cdEmpresa=' + gridNotasFiscais.getSelectedRowRegister()['CD_EMPRESA'] + '&cdNotaFiscal=' + gridNotasFiscais.getSelectedRowRegister()['CD_NOTA_FISCAL']});
	
}

function btnImprimirNotaFiscalOnClick(){
	
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
	
	if(gridNotasFiscais.getSelectedRowRegister()['ST_NOTA_FISCAL'] == 4){
		parent.createWindow('jRelatorioContaPagar', {caption: 'DANFE', width: frameWidth-20, height: frameHeight-50, old: true,
            contentUrl: '../fsc/danfe.jsp?cdEmpresa=' + gridNotasFiscais.getSelectedRowRegister()['CD_EMPRESA'] + '&cdNotaFiscal=' + gridNotasFiscais.getSelectedRowRegister()['CD_NOTA_FISCAL'],
            oldContentUrl: 'fsc/danfe.jsp?cdEmpresa=' + gridNotasFiscais.getSelectedRowRegister()['CD_EMPRESA'] + '&cdNotaFiscal=' + gridNotasFiscais.getSelectedRowRegister()['CD_NOTA_FISCAL']
		});
	}
	else{
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Essa nota não foi autorizada.", msgboxType: "INFO"});
	}
}


function motivoCancelamento(){
	
	FormFactory.createFormWindow('jMotivoCancelamento', {caption: "Motivo do Cancelamento", width: 400, height: 100, noDrag: true,modal: true,
											        id: 'detalheResultados', unitSize: '%',
												    lines: [[{id:'dsMotivo', label:'Digite o motivo do cancelamento da nota:', width:100, height: 30, type: 'textarea'}],
												            [{type: 'space', width:40},
												             {id:'btnCancelarNotaFiscalOnClick', label:'Cancelar Nota', width:30, height: 19, type: 'button', image: '/sol/imagens/form-btCancelar16.gif', onClick: function(){btnCancelarNotaFiscalOnClick(null, $('dsMotivo').value);}},
												             {id:'btnVoltar', label:'Voltar', width:30, height: 19, type: 'button', image: '/sol/imagens/return16.gif', onClick: function(){closeWindow('jMotivoCancelamento');}}]]});
												
}

function btnCancelarNotaFiscalOnClick(content, txtMotivo){
	if(content == null){
		var execute = '';
		var objects = 'lista=java.util.ArrayList();';
// 		for (var i = 0; i < gridNotasFiscais.getResultSet().lines.length; i++){//Mudar de testando todos os registros para saber se estão selecionados, para guardar em uma lista os cdNotaFiscais dos selecionados depois só conferindo aqui
			if(gridNotasFiscais.getSelectedRowRegister()['ST_NOTA_FISCAL'] == 4){
				objects += 'registro0=java.util.HashMap();';
				objects += 'cdNotaFiscal0=java.lang.Integer(const ' +gridNotasFiscais.getSelectedRowRegister()['CD_NOTA_FISCAL'] + ':int);';
				objects += 'cdEmpresa0=java.lang.Integer(const ' +gridNotasFiscais.getSelectedRowRegister()['CD_EMPRESA'] + ':int);';
				objects += 'txtMotivo0=java.lang.String(const ' +txtMotivo+ ':String);';
				execute += 'registro0.put(const cdNotaFiscal:Object, *cdNotaFiscal0:Object);';	
				execute += 'registro0.put(const cdEmpresa:Object, *cdEmpresa0:Object);';
				execute += 'registro0.put(const txtMotivo:Object, *txtMotivo0:Object);';
				execute += 'lista.add(*registro0:Object);';	
			}
// 		}
		
		getPage("GET2POST", "btnCancelarNotaFiscalOnClick", "../methodcaller?className=com.tivic.manager.fsc.NfeServices"+
												  '&objects=' + objects + 
												  '&execute=' + execute +
												  "&method=cancelarNFe(*lista:java.util.ArrayList())", null, true, null);
	}
	else{
		var resultado = null;
		try {resultado = eval('(' + content + ')')} catch(e) {}
		if(resultado.code > 0){
			FormFactory.createFormWindow('jResultados', {caption: "Resultados dos Cancelamentos", width: 800, height: 400, noDrag: true,modal: true,
	            id: 'detalheResultados', unitSize: '%',
			    lines: [[{id:'dsMensagem', width:100, height: 30, type: 'caption', text: resultado.message},
			             {id:'divGridResultados', width:100, height: 320, type: 'grid'}]]});
			GridOne.create('gridNotasFiscais', {columns: columnsResultados, resultset :resultado.objects.resultado, plotPlace : $('divGridResultados'),
				 noHeader: false, noSelectorColumn: false, lineSeparator: false, columnSeparator: false});
			closeWindow('jMotivoCancelamento');
			btnPesquisarNotaFiscalOnClick(null);
		}
		else
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: resultado.message, msgboxType: "INFO"});
		
	}
}

function btnExcluitNotaFiscalOnClick(content){
	if(content == null){
		var execute = '';
		var objects = 'lista=java.util.ArrayList();';
		for (var i = 0; i < gridNotasFiscais.getResultSet().lines.length; i++){//Mudar de testando todos os registros para saber se estão selecionados, para guardar em uma lista os cdNotaFiscais dos selecionados depois só conferindo aqui
			if(gridNotasFiscais.getResultSet().lines[i]['LG_SELECIONADO'] == 1 && gridNotasFiscais.getResultSet().lines[i]['ST_NOTA_FISCAL'] != 4){
				objects += 'registro'+i+       '=java.util.HashMap();';
				objects += 'cdNotaFiscal'+i+'=java.lang.Integer(const ' +gridNotasFiscais.getResultSet().lines[i]['CD_NOTA_FISCAL'] + ':int);';
				execute += 'registro'+i+'.put(const cdNotaFiscal:Object, *cdNotaFiscal'+i+':Object);';	
				execute += 'lista.add(*registro'+i+':Object);';	
			}
		}
		
		getPage("GET2POST", "btnExcluitNotaFiscalOnClick", "../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices"+
												  '&objects=' + objects + 
												  '&execute=' + execute +
												  "&method=delete(*lista:java.util.ArrayList())", null, true, null);
	}
    else {// retorno
    	var resultado = null;
		try {resultado = eval('(' + content + ')')} catch(e) {}
		if(resultado.code > 0){
			FormFactory.createFormWindow('jResultados', {caption: "Resultados das Exclusões", width: 800, height: 400, noDrag: true,modal: true,
	            id: 'detalheResultados', unitSize: '%',
			    lines: [[{id:'dsMensagem', width:100, height: 30, type: 'caption', text: resultado.message},
			             {id:'divGridResultados', width:100, height: 320, type: 'grid'}]]});
			GridOne.create('gridNotasFiscais', {columns: columnsResultados, resultset :resultado.objects.resultado, plotPlace : $('divGridResultados'),
				 noHeader: false, noSelectorColumn: false, lineSeparator: false, columnSeparator: false});
			btnPesquisarNotaFiscalOnClick(null);
		}
		else
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: resultado.message, msgboxType: "INFO"});
		
	}
}

function printReport(){

	var caption    = "Nota Fiscal Eletrônica";
	var className  = "com.tivic.manager.fsc.NotaFiscalServices";
	var method     = "gerarRelatorioNotaFiscalEletronica(const " + $('cdNotaFiscal').value+": int, const " + $('cdEmpresa').value+": int, " +
					 "const " + $('dtInicial').value+": GregorianCalendar, const " + $('dtFinal').value+": GregorianCalendar,  " + 
					 "const " + $('tpNotaFiscal').value+": int,  const " + $('stNotaFiscal').value+": int,  const " + $('nrSerie').value+": String,  " + 
					 "const " + $('nrInicial').value+": String,  const " + $('nrFinal').value+": String,  const " + $('nrCpfCnpj').value+": String,  " + 
					 "const " + $('nrChaveAcesso').value+": String)";
	var nomeJasper = "nota_fiscal_eletronica";	
	
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
					 "&modulo=fsc"});

}

function gerarXmlOnClick(content){
	var iframe = $('iframe_save');
	if(iframe==null)	{
		iframe = document.createElement("iframe");
		iframe.id = 'iframe_save';
		iframe.style.visibility = 'hidden';
		$('notaFiscal').appendChild(iframe);
	}
	var cdNotaFiscal = $('cdNotaFiscalGrid').value;
	if(cdNotaFiscal == '' || cdNotaFiscal == null)
		return;
	iframe.src = "../save_file.jsp?tpLocalizacao=1&className=com.tivic.manager.fsc.NfeServices"+
				 "&method=getXmlComProtocolo(const "+cdNotaFiscal+":int,null:com.tivic.manager.fsc.NotaFiscalHistorico, const "+$('cdEmpresa').value+":int)"+
				 "&filename="+$('nrChaveAcessoGrid').value+".xml";
}

function btnTarefasNotaFiscalOnClick(content){
	if(content == null){
		var execute = '';
		var objects = 'lista=java.util.ArrayList();';
		for (var i = 0; i < gridNotasFiscais.getResultSet().lines.length; i++){
			objects += 'registro'+i+       '=java.util.HashMap();';
			objects += 'cdNotaFiscal'+i+'=java.lang.Integer(const ' +gridNotasFiscais.getResultSet().lines[i]['CD_NOTA_FISCAL'] + ':int);';
			objects += 'cdEmpresa'+i+'=java.lang.Integer(const ' +gridNotasFiscais.getResultSet().lines[i]['CD_EMPRESA'] + ':int);';
			objects += 'lgSelecionado'+i+'=java.lang.Integer(const ' +gridNotasFiscais.getResultSet().lines[i]['LG_SELECIONADO'] + ':int);';
			execute += 'registro'+i+'.put(const cdNotaFiscal:Object, *cdNotaFiscal'+i+':Object);';	
			execute += 'registro'+i+'.put(const cdEmpresa:Object, *cdEmpresa'+i+':Object);';
			execute += 'registro'+i+'.put(const lgSelecionado:Object, *lgSelecionado'+i+':Object);';
			execute += 'lista.add(*registro'+i+':Object);';	
		}
		
		getPage("GET2POST", "btnTarefasNotaFiscalOnClick", "../methodcaller?className=com.tivic.manager.fsc.NfeServices"+
												  '&objects=' + objects + 
												  '&execute=' + execute +
												  "&method=tarefasNFe(*lista:java.util.ArrayList())", null, true, null);
	}
	else{
		var resultado = null;
		try {resultado = eval('(' + content + ')')} catch(e) {}
		if(resultado.code > 0){
			FormFactory.createFormWindow('jResultados', {caption: "Resultados", width: 800, height: 400, noDrag: true,modal: true,
	            id: 'detalheResultados', unitSize: '%',
			    lines: [[{id:'dsMensagem', width:100, height: 30, type: 'caption', text: resultado.message}, 
			             {id:'divGridResultados', width:100, height: 320, type: 'grid'}]]});
			GridOne.create('gridNotasFiscais', {columns: columnsResultados, resultset :resultado.objects.resultado, plotPlace : $('divGridResultados'),
				 noHeader: false, noSelectorColumn: false, lineSeparator: false, columnSeparator: false});
			btnPesquisarNotaFiscalOnClick(null);
		}
		else
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: resultado.message, msgboxType: "INFO"});
		
	}
}


function btnEnviarEmailOnClick(){
	createConfirmbox("dialog", {caption: "Envio de Email", width: 300, height: 75, 
        message: "Você tem certeza que deseja enviar um email para o destinatário com o xml da nota e o DANFE?", boxType: "QUESTION",
        positiveAction: function() {setTimeout("btnEnviarEmail2OnClick()", 10)}});
}


function btnEnviarEmail2OnClick(content){
	if(content == null){
		getPage("GET2POST", "btnEnviarEmail2OnClick", "../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices"+
												  "&method=enviarEmail(const "+$('cdNotaFiscalGridTemp').value+":int)", null, true, null);
	}
	else{
		var resultado = null;
		try {resultado = eval('(' + content + ')')} catch(e) {}
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: resultado.message, msgboxType: "INFO"});
	}
}

function btnFindVeiculoOnClick(reg){
    if(!reg){
       if(gridNotasFiscais.getSelectedRowRegister()['ST_NOTA_FISCAL'] > 2){
    		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Notas autorizadas, transmitidas ou denegadas não podem ser alteradas!", msgboxType: "INFO"});
    		return;
       }	
       filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar Veículos', 
							   top: 10,
							   width: 580,
							   height: 290,
							   modal: true,
							   noDrag: true,
							   className: "com.tivic.manager.fta.VeiculoServices",
							   method: "findCompleto",
							   allowFindAll: true,
							   filterFields: [[{label:"Placa", reference:"NR_PLACA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
										    {label:"Chassi", reference:"NR_CHASSI", datatype:_VARCHAR, comparator:_EQUAL, width:40, charcase:'uppercase'},
										    {label:"Renavan", reference:"NR_RENAVAM", datatype:_VARCHAR, comparator:_EQUAL, width:40, charcase:'uppercase'}],
										   [{label:"Marca", reference:"NM_MARCA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50},
										    {label:"Modelo", reference:"NM_MODELO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50}]],
							   gridOptions: { columns: [{label:"Placa", reference:"NR_PLACA", type:GridOne._MASK, mask: "***-####"},
												   {label:"Marca", reference:"NM_MARCA"},
												   {label:"Modelo", reference:"NM_MODELO"},
												   {label:"Chassi", reference:"NR_CHASSI"},
												   {label:"Renavam", reference:"NR_RENAVAM"}],
										   strippedLines: true,
										   columnSeparator: false,
										   lineSeparator: false},
							   callback: btnFindVeiculoOnClick
					});
    }
    else {// retorno
		filterWindow.close();
		$('cdVeiculo').value = reg[0]['CD_VEICULO'];
		$('cdVeiculoView').value = reg[0]['NR_PLACA'] + ' / ' + reg[0]['NM_MARCA'] + ' / ' + reg[0]['NM_MODELO'];
	}
}

function btnClearVeiculoOnClick(){
   if(gridNotasFiscais.getSelectedRowRegister()['ST_NOTA_FISCAL'] > 2){
		createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Notas autorizadas, transmitidas ou denegadas não podem ser alteradas!", msgboxType: "INFO"});
		return;
   }

   $('cdVeiculo').value = '';
   $('cdVeiculoView').value = '';
}

function loadVeiculos(content, cdTransportadora){
	if(content == null){
		getPage('POST', 'loadVeiculos', 
				   '../methodcaller?className=com.tivic.manager.fta.VeiculoServices'+
				   '&method=getAllByProprietario(const '+cdTransportadora+':int)', null, null, null, null);	
	}
	
	else{
// 		var rsm = null;
// 		try { rsm = eval("("+content+")"); } catch(e) {};
//         //
// 		deleteAllOptions($('cdVeiculo'));
//         alert(content);
		
	}
}

</script>
</head>
<body class="body" onload="initNotaFiscal();" id="notaFiscalBody">
<div style="width: 1000px;" id="notaFiscal" class="d1-form">
  <div style="width: 1000px; height: 460px;" class="d1-body">
    <input idform="notaFiscal" reference="cd_nota_fiscal" id="cdNotaFiscal" name="cdNotaFiscal" type="hidden" value="0" defaultValue="0"/>
    <input idform="notaFiscal" reference="cd_nota_fiscal" id="cdNotaFiscalGridTemp" name="cdNotaFiscalGridTemp" type="hidden" value="0" defaultValue="0"/>
    <input idform="notaFiscal" reference="nr_cfop" id="nrCfopGridTemp" name="nrCfopGridTemp" type="hidden" value="0" defaultValue="0"/>
    <input idform="notaFiscal" reference="cd_empresa" logmessage="Empresa" datatype="STRING" id="cdEmpresa" name="cdEmpresa" defaultValue="<%=cdEmpresa%>" type="hidden"/>
    <input idform="notaFiscal" reference="cd_local_armazenamento" logmessage="Local Armazenamento" datatype="STRING" id="cdLocalArmazenamento" name="cdLocalArmazenamento" defaultValue="<%=cdLocalArmazenamento%>" type="hidden"/>
    <div id="toolBar" class="d1-toolBar" style="height:48px; width: 990px;"></div>
	<div class="d1-line" id="line0">
		<div style="width: 195px; margin-left: 2px;" class="element">
			<label class="caption" for="dtInicial">Data Inicial</label>
			<input name="dtInicial" type="text" class="disabledField2" id="dtInicial" style="width: 190px;" maxlength="10" disabled="disabled" readonly="readonly" static="static" logmessage="Data inicial" mask="##/##/#### ##:##:##" idform="notaFiscal" reference="dt_inicial" datatype="DATE" value="<%=today%>"/>
			<button idform="notaFiscal" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtInicial" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		</div>
		<div style="width: 195px; margin-left: 2px;" class="element">
			<label class="caption" for="dtFinal">Data Final</label>
			<input name="dtFinal" type="text" class="disabledField2" id="dtFinal" style="width: 190px;" maxlength="10" disabled="disabled" readonly="readonly" static="static" logmessage="Data final" mask="##/##/#### ##:##:##" value="<%=today%>" idform="notaFiscal" reference="dt_final" datatype="DATE"/>
			<button idform="notaFiscal" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtFinal" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
		</div>
		<div style="width: 295px;" class="element">
			<label class="caption" for="tpNotaFiscal">Tipo de NFE</label>
			<select logmessage="Tipo de Nota Fiscal Eletrônica" style="width: 290px;" class="select2" idform="notaFiscal" reference="tp_nota_fiscal" id="tpNotaFiscal" name="tpNotaFiscal" defaultValue="0">
			</select>
		</div>
		<div style="width: 290px;" class="element">
			<label class="caption" for="stNotaFiscal">Situação da NFE</label>
			<select logmessage="Situação da Nota Fiscal Eletrônica" style="width: 290px;" class="select2" idform="notaFiscal" reference="st_nota_fiscal" id="stNotaFiscal" name="stNotaFiscal" defaultValue="0">
			</select>
		</div>
	</div>
	<div class="d1-line" id="line2">
		<div style="width: 100px; padding:2px 0 0 0" class="element">
		   <label class="caption" for="nrSerie">Série</label>
		   <input name="nrSerie" type="text" class="field2" id="nrSerie" style="width: 95px; text-transform:uppercase" maxlength="50" logmessage="Série da NFe" idform="notaFiscal" reference="nr_serie"/>
	    </div>
	    <div style="width: 130px; padding:2px 0 0 0" class="element">
		   <label class="caption" for="nrDocumentoSaida">Nº Documento de Saída</label>
		   <input name="nrDocumentoSaida" type="text" class="field2" id="nrDocumentoSaida" style="width: 125px; text-transform:uppercase" maxlength="50" logmessage="Número do documento de saída" idform="notaFiscal" reference="nr_documento_saida"/>
	    </div>
	    <div style="width: 120px; padding:2px 0 0 0" class="element">
		   <label class="caption" for="nrInicial">Número de</label>
		   <input name="nrInicial" type="text" class="field2" id="nrInicial" style="width: 115px; text-transform:uppercase" maxlength="50" logmessage="Número Inicial" idform="notaFiscal" reference="nr_inicial"/>
	    </div>
		<div style="width: 120px; padding:2px 0 0 0" class="element">
		   <label class="caption" for="nrFinal">Até</label>
		   <input name="nrFinal" type="text" class="field2" id="nrFinal" style="width: 115px; text-transform:uppercase" maxlength="50" logmessage="Número Final" idform="notaFiscal" reference="nr_final"/>
	    </div>
<!-- 	    Arrumar para que receba somente números -->
	    <div style="width: 219px; padding:2px 0 0 0" class="element">
		   <label class="caption" for="nr_cpf_cnpj">CPF/CNPJ do destinatário</label>
		   <input name="nrCpfCnpj" type="text" class="field2" mask="##############" id="nrCpfCnpj" style="width: 212px; text-transform:uppercase" maxlength="50" logmessage="CPF/CNPJ do destinatário" idform="notaFiscal" reference="nr_cpf_cnpj"/>
	    </div>
	    <div style="width: 290px; padding:2px 0 0 0" class="element">
		   <label class="caption" for="nr_chave_acesso">Chave de Acesso</label>
		   <input name="nrChaveAcesso" type="text" class="field2" id="nrChaveAcesso" style="width: 288px; text-transform:uppercase" maxlength="50" logmessage="Chave de Acesso" idform="notaFiscal" reference="nr_chave_acesso"/>
	    </div>
	    
	</div>
	<div style="width: 977px; padding:4px 0 0 0" class="element">
		<div id="divGridNotasFiscais" style="width: 977px; background-color:#FFF; height:300px; border:1px solid #000;">&nbsp;</div>
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