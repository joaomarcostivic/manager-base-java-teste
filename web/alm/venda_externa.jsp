<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.alm.DocumentoEntradaServices"%>
<%@page import="sol.util.Jso"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="com.tivic.manager.alm.DocumentoSaidaServices"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.util.RequestUtilities" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut, floatmenu" compress="false" />
<%
	int cdEmpresa 			  = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	int cdLocalArmazenamento  = RequestUtilities.getParameterAsInteger(request, "cdLocalArmazenamento", 0);
	int cdCidadeDefault       = ParametroServices.getValorOfParametroAsInteger("CD_CIDADE_END_DEFAULT", 0, cdEmpresa);
	Cidade cidade             = cdCidadeDefault<=0 ? null : CidadeDAO.get(cdCidadeDefault);
	String nmCidade           = cidade==null ? "" : cidade.getNmCidade();
	Usuario usuario           = (Usuario)session.getAttribute("usuario");
	int cdUsuario             = usuario==null ? 0 : usuario.getCdUsuario();
	int cdPessoaUsuario       = usuario==null ? 0 : usuario.getCdPessoa();
	
%>
<script language="javascript" src="../js/alm.js"></script>
<script language="javascript">
var disabledFormViagem = false;

var toolBar;

var tabRemessa;
var tabVenda;
var tabRetorno;

function init(){
    loadFormFields(["viagem"]);
    
    toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
								    orientation: 'horizontal',
								    buttons: [{id: 'btnNew', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova Viagem', onClick: btnNewViagemOnClick},
										    {id: 'btnEdit', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterViagemOnClick},
										    {id: 'btnSave', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveViagemOnClick},
										    {id: 'btnDelete', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteViagemOnClick},
										    {id: 'btnFind', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindViagemOnClick},
										    {separator: 'horizontal'},
										    {id: 'btnGerarRetorno', img: 'imagens/documento_entrada16.gif', label: 'Gerar Nota de Retorno', onClick: btnGerarRetornoOnClick},
										    {id: 'btnDeleteRetorno', img: '/sol/imagens/form-btExcluir16.gif', label: 'Deletar Nota de Retorno', onClick: btnDeleteRetornoOnClick},
										    {id: 'btnImprimirRelatorios', img: '/sol/imagens/print16.gif', label: 'Imprimir', onClick: btnImprimirRelatoriosOnClick}]});
	
    enableTabEmulation();
    
    tabRemessa = TabOne.create('tabRemessa', {width: 890,
											 height: 100,
											 tabs: [{caption: 'Dados', 
												    reference:'divTabRemessaDados',
												    active: true,
												    image: 'imagens/documento_saida16.gif'},
												   {caption: 'Nota Fiscal Eletrônica', 
												    reference:'divTabRemessaNfe',
												    image: '../fsc/imagens/nfe16.gif'}],
											plotPlace: 'divTabRemessa',
											tabPosition: ['top', 'left']});
    
    tabVenda = TabOne.create('tabVenda', {width: 890,
											 height: 140,
											 tabs: [{caption: 'Dados', 
												    reference:'divTabVendaDados',
												    active: true,
												    image: 'imagens/documento_saida16.gif'},
												   {caption: 'Nota Fiscal Eletrônica', 
												    reference:'divTabVendaNfe',
												    image: '../fsc/imagens/nfe16.gif'}],
											plotPlace: 'divTabVenda',
											tabPosition: ['top', 'left']});

    
    tabRetorno = TabOne.create('tabRetorno', {width: 890,
											 height: 100,
											 tabs: [{caption: 'Dados', 
												    reference:'divTabRetornoDados',
												    active: true,
												    image: 'imagens/documento_entrada16.gif'},
												   {caption: 'Nota Fiscal Eletrônica', 
												    reference:'divTabRetornoNfe',
												    image: '../fsc/imagens/nfe16.gif'}],
											plotPlace: 'divTabRetorno',
											tabPosition: ['top', 'left']});


    toolBarGridRemessa = ToolBar.create('toolBarGridRemessa', {plotPlace: 'toolBarGridRemessa',
											    orientation: 'vertical',
											    buttons: [{id: 'btnNovoRemessa', img: '/sol/imagens/form-btNovo16.gif', width: 18, onClick: btnNovoDocumentoSaidaRemessaOnClick},
											              {id: 'btnFindRemessa', img: '/sol/imagens/form-btPesquisar16.gif', width: 18, onClick: btnFindDocumentoSaidaRemessaOnClick},
													      {id: 'btnDelete', img: '/sol/imagens/form-btExcluir16.gif', width: 18, onClick: deleteViagemRemessa}]});
    
    toolBarGridVenda = ToolBar.create('toolBarGridVenda', {plotPlace: 'toolBarGridVenda',
											    orientation: 'vertical',
											    buttons: [{id: 'btnNovoVenda', img: '/sol/imagens/form-btNovo16.gif', width: 18, onClick: btnNovoDocumentoSaidaVendaOnClick},
											              {id: 'btnFindVenda', img: '/sol/imagens/form-btPesquisar16.gif', width: 18, onClick: btnFindDocumentoSaidaVendaOnClick},
													      {id: 'btnDelete', img: '/sol/imagens/form-btExcluir16.gif', width: 18, onClick: deleteViagemVenda}]});
    
	clearFormViagem();
}

function clearFormViagem(){
    $("dataOldViagem").value = "";
    disabledFormViagem = false;
    clearFields(viagemFields);
    alterFieldsStatus(true, viagemFields, "txtObservacao", "disabledField2");
    createGridRemessa();
	createGridVenda();
	createGridRetorno();
	createGridRemessaNfe();
	createGridVendaNfe();
	createGridRetornoNfe();
}

function btnNewViagemOnClick(){
    clearFormViagem();
}

function btnAlterViagemOnClick(){
    disabledFormViagem = false;
    alterFieldsStatus(true, viagemFields, "txtObservacao", "disabledField2");
}

function formValidationViagem(){
	var fields = [[$("txtObservacao"), 'Nome da Viagem', VAL_CAMPO_NAO_PREENCHIDO],
			      [$("cdCidadeOrigemView"), 'Cidade Origem', VAL_CAMPO_NAO_PREENCHIDO],
			      [$("cdCidadeDestinoView"), 'Cidade Destino', VAL_CAMPO_NAO_PREENCHIDO],
			      [$("cdResponsavelView"), 'Responsável', VAL_CAMPO_NAO_PREENCHIDO],
			      [$("cdVeiculoView"), 'Veículo', VAL_CAMPO_NAO_PREENCHIDO]];
     return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'txtObservacao');
}


function btnSaveViagemOnClick(content){
    if(content==null){
    	if(disabledFormViagem){
            createMsgbox("jMsg", {caption: 'Atenção',
		  				    width: 220,
                                  height: 50,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  boxType: "INFO"});
        }
        if(<%=cdPessoaUsuario%> == null || <%=cdPessoaUsuario%> <= 0){
        	createMsgbox("jMsg", {caption: 'Atenção',
				    width: 220,
                      height: 50,
                      message: "Usuário não encontrado. Por Favor, recarregue a página.",
                      boxType: "INFO"});
        	
        }
        else if (formValidationViagem()) {
        	$('btnSave').disabled = !disabledFormViagem;
        	var objects = 'crtViagem=java.util.ArrayList();';
        	objects += "itemCdCidadeOrigem=sol.dao.ItemComparator(const cdCidadeOrigem:String,const "+$('cdCidadeOrigem').value+
			":String,const "+_EQUAL+":int,const "+_INTEGER+":int);";
			objects += "itemCdCidadeDestino=sol.dao.ItemComparator(const cdCidadeDestino:String,const "+$('cdCidadeDestino').value+
			":String,const "+_EQUAL+":int,const "+_INTEGER+":int);";
			objects += "itemCdResponsavel=sol.dao.ItemComparator(const cdResponsavel:String,const "+$('cdResponsavel').value+
			":String,const "+_EQUAL+":int,const "+_INTEGER+":int);";
			objects += "itemCdDigitador=sol.dao.ItemComparator(const cdDigitador:String,const "+<%=cdPessoaUsuario%>+
			":String,const "+_EQUAL+":int,const "+_INTEGER+":int);";
        	
			var execute = 'crtViagem.add(*itemCdCidadeOrigem:Object);';
        	execute    += 'crtViagem.add(*itemCdCidadeDestino:Object);';
        	execute    += 'crtViagem.add(*itemCdResponsavel:Object);';
        	execute    += 'crtViagem.add(*itemCdDigitador:Object);';
        	        	
            var executionDescription = $("cdViagem").value>0 ? formatDescriptionUpdate("Viagem", $("cdViagem").value, $("dataOldViagem").value, viagemFields) : formatDescriptionInsert("Viagem", viagemFields);
            getPage("POST", "btnSaveViagemOnClick", "../methodcaller?className=com.tivic.manager.fta.ViagemServices"+
            										"&objects="+objects+
            										"&execute="+execute+
                                                    "&method=saveViagemVendaExterna(new com.tivic.manager.fta.Viagem(cdViagem: int, cdVeiculo: int, dtSaida: GregorianCalendar, dtChegada: GregorianCalendar, txtObservacao: String, const null: GregorianCalendar, stViagem: int):com.tivic.manager.fta.Viagem, *crtViagem: java.util.ArrayList)", viagemFields, null, null, executionDescription);
            
        }
    }
    else{
    	$('btnSave').disabled = false;
    	var retorno;
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if(retorno.code>0){
		  $('cdViagem').value = retorno.code; 
		  disabledFormViagem=true;
		  alterFieldsStatus(false, viagemFields, "txtObervacao", "disabledField2");
		  createTempbox("jMsg", {width: 300,
							height: 50,
							message: "Dados gravados com sucesso!",
							tempboxType: "INFO",
							time: 2000});
		  $("dataOldViagem").value = captureValuesOfFields(viagemFields);
		  
		}
		else{
		  createTempbox("jMsg", {width: 300,
							height: 50,
							message: "ERRO ao tentar gravar dados!",
							tempboxType: "ERROR",
							time: 3000});
		}
    }
}

var filterWindow;
function btnFindViagemOnClick(reg){
	if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:'Pesquisar Viagens', 
								   width: 580,
								   height: 290,
								   modal: true,
								   noDrag: true,
								   className: "com.tivic.manager.fta.ViagemServices",
								   method: "findCompleto",
								   allowFindAll: true,
								   filterFields: [[{label:"Nome", reference:"V.TXT_OBSERVACAO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'},
												   {label:"Dt. Saída", reference:"DT_SAIDA", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'},
												   {label:"Dt. Chegada", reference:"DT_CHEGADA", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'}]],
								   gridOptions: { columns: [{label:"Nome", reference:"TXT_OBSERVACAO"},
								                            {label:"Veículo", reference:"NR_PLACA"},
														    {label:"Dt. Saída", reference:"DT_SAIDA", type: GridOne._DATE},
														    {label:"Dt. Chegada", reference:"DT_CHEGADA", type: GridOne._DATE}],
											   strippedLines: true,
											   columnSeparator: false,
											   lineSeparator: false},
								   callback: btnFindViagemOnClick
						});
	}
	else {// retorno
		filterWindow.close();
		loadFormViagem(reg[0]);
	}
}

function loadFormViagem(register){
	disabledFormFta_rota=true;
     alterFieldsStatus(false, viagemFields, "btnFindMotorista", "disabledField2");
	loadFormRegister(viagemFields, register);

     $("dataOldViagem").value = captureValuesOfFields(viagemFields);
	loadRemessa();
	loadVenda();
	loadRetorno();
	loadRemessaNfe();
	loadVendaNfe();
	loadRetornoNfe();
}


function btnDeleteViagemOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Viagem", $("cdViagem").value, $("dataOldViagem").value);
    getPage("GET", "btnDeleteViagemOnClick", 
            "../methodcaller?className=com.tivic.manager.fta.ViagemServices"+
            "&method=deleteViagemVendaExterna(const "+$("cdViagem").value+":int):int", null, null, null, executionDescription);
}
function btnDeleteViagemOnClick(content){
	if(content==null){
		if ($("cdViagem").value == 0){
		  createMsgbox("jMsg", {width: 300, 
						    height: 120, 
						    message: "Nenhuma registro foi carregado para que seja excluído.",
						    msgboxType: "INFO"});
		}
		else{
		  createConfirmbox("dialog", {caption: "Exclusão de registro",
								width: 300, 
								height: 75, 
								message: "Você tem certeza que deseja excluir este registro?",
								boxType: "QUESTION",
								positiveAction: function() {setTimeout("btnDeleteViagemOnClickAux()", 10)}});
		}
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormViagem();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnPrintViagemOnClick(){;}


function btnFindVeiculoOnClick(reg){
    if(!reg){
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
	$('cdVeiculo').value = '';
	$('cdVeiculoView').value = '';
}


function btnClearCidadeOrigemOnClick(){
	$('cdCidadeOrigem').value = '';
	$('cdCidadeOrigemView').value = '';
	resetCidadeOrigemFlag();
}

function btnFindResponsavelOnClick(reg){
	if(!reg){
		var hiddenFields = [{reference:"findEnderecoPrincipal", value:"1", comparator:_EQUAL, datatype:_INTEGER}];
        var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60, charcase:'uppercase'},
							 {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
							 {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]];
		filterFields.push([{label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:45, charcase:'uppercase'},
						   {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
						   {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
						   {label:"Cidade", reference:"U.nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:25, charcase:'uppercase'}]);
		FilterOne.create("jFiltro", {caption:"Pesquisar Responsavel", width: 800, height: 400, modal: true, noDrag: true,
									   className: "com.tivic.manager.grl.PessoaServices", method: "find", allowFindAll: true,
									   filterFields: filterFields,
									   gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"},
									   						   {label:"Razão Social", reference:"NM_RAZAO_SOCIAL"},
															   {label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ},
															   {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"}, 
															   {label:'CPF', reference:'NR_CPF', type:GridOne._CPF},
															   {label:"Identidade", reference:"NR_RG"}, 
									   						   {label:"Cidade", reference:"NM_CIDADE"}],
													 strippedLines: true, columnSeparator: false, lineSeparator: false},
									   hiddenFields: hiddenFields,
									   callback: btnFindResponsavelOnClick, 
									   autoExecuteOnEnter: true});
    }
    else {// retorno
		closeWindow("jFiltro");
		$('cdResponsavel').value = reg[0]['CD_PESSOA'];
		$('cdResponsavelView').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearResponsavelOnClick(){
	$('cdResponsavel').value = 0;
	$('cdResponsavelView').value = '';
}

var columnsSaida = [{label: 'Data', reference: 'DT_DOCUMENTO_SAIDA', type: GridOne._DATE, referenceGroupBy: 'DT_DOCUMENTO_SAIDA', SHOW: 0},
					{label: 'Nº Documento', reference: 'NR_DOCUMENTO_SAIDA', referenceGroupBy: 'cd_documento_saida', SHOW: 0},
					{label: 'Acrésc.', reference: 'VL_ACRESCIMO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
					{label: 'Desc.', reference: 'VL_DESCONTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
					{label: 'Valor', reference: 'VL_TOTAL_DOCUMENTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
					{label: 'Situação', reference: 'CL_ST_DOCUMENTO_SAIDA', referenceGroupBy: 'st_documento_saida'}, 
					{label: 'Tipo de Operação', reference: 'NM_TIPO_OPERACAO', referenceGroupBy: 'cd_tipo_operacao', SHOW: 0},
					{label: 'Tipo Saída', reference: 'CL_TP_SAIDA', referenceGroupBy: 'tp_saida', SHOW: 0},
					{label: 'Tipo Movimento', reference: 'CL_TP_MOVIMENTO_ESTOQUE', referenceGroupBy: 'tp_movimento_estoque', SHOW: 0}, 
					{label: 'Tipo Documento', reference: 'CL_TP_DOCUMENTO_SAIDA', referenceGroupBy: 'tp_documento_saida', SHOW: 0}];
					
var columnsEntrada = [{label: 'Data', reference: 'DT_DOCUMENTO_ENTRADA', type: GridOne._DATE, referenceGroupBy: 'DT_DOCUMENTO_ENTRADA', SHOW: 0},
					{label: 'Nº Documento', reference: 'NR_DOCUMENTO_ENTRADA', referenceGroupBy: 'cd_documento_saida', SHOW: 0},
					{label: 'Acrésc.', reference: 'VL_ACRESCIMO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
					{label: 'Desc.', reference: 'VL_DESCONTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
					{label: 'Valor', reference: 'VL_TOTAL_DOCUMENTO', type: GridOne._CURRENCY, style: 'text-align:right;', summaryFunction: 'SUM', sum: 'SUM'}, 
					{label: 'Situação', reference: 'CL_ST_DOCUMENTO_ENTRADA', referenceGroupBy: 'st_documento_entrada'}, 
					{label: 'Tipo Entrada', reference: 'CL_TP_ENTRADA', referenceGroupBy: 'tp_entrada', SHOW: 0},
					{label: 'Tipo Movimento', reference: 'CL_TP_MOVIMENTO_ESTOQUE', referenceGroupBy: 'tp_movimento_estoque', SHOW: 0}, 
					{label: 'Tipo Documento', reference: 'CL_TP_DOCUMENTO_ENTRADA', referenceGroupBy: 'tp_documento_entrada', SHOW: 0}];

var columnsNfe = [{label:'Série', reference:'NR_SERIE'},
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


var tiposSaida          = <%=Jso.getStream(DocumentoSaidaServices.tiposSaida)%>;
var situacaoDocumento   = <%=Jso.getStream(DocumentoSaidaServices.situacoes)%>;
var tiposDocumentoSaida = <%=Jso.getStream(DocumentoSaidaServices.tiposDocumentoSaida)%>;
var tiposMovimento      = <%=Jso.getStream(DocumentoSaidaServices.tiposMovimento)%>;

var tiposEntrada          = <%=Jso.getStream(DocumentoEntradaServices.tiposEntrada)%>;
var situacaoDocumentoENT  = <%=Jso.getStream(DocumentoEntradaServices.situacoes)%>;
var tiposDocumentoEntrada = <%=Jso.getStream(DocumentoEntradaServices.tiposDocumentoEntrada)%>;
var tiposMovimentoENT     = <%=Jso.getStream(DocumentoEntradaServices.tiposMovimento)%>;

function loadRemessa(content) {
	if (content==null) {
		getPage("GET", "loadRemessa", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
				"&method=findCompletoByViagem(const "+$('cdViagem').value+": int, const true:boolean)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridRemessa(rsm);
	}
}

var gridRemessa;
function createGridRemessa(rsm){
	gridRemessa = GridOne.create('gridRemessa', {columns: columnsSaida,
										 resultset: rsm,
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 noFocusOnSelect : true, 
										 noSelectOnCreate: false, 
										 onProcessRegister: function(reg){
											reg['CL_TP_SAIDA']                = tiposSaida[parseInt(reg['TP_SAIDA'], 10)];
										 	reg['CL_ST_DOCUMENTO_SAIDA']      = situacaoDocumento[parseInt(reg['ST_DOCUMENTO_SAIDA'], 10)];
										 	reg['CL_TP_DOCUMENTO_SAIDA']      = tiposDocumentoSaida[parseInt(reg['TP_DOCUMENTO_SAIDA'], 10)];
										 	reg['CL_TP_MOVIMENTO_ESTOQUE']    = tiposMovimento[parseInt(reg['TP_MOVIMENTO_ESTOQUE'], 10)];
										 	if(parseFloat(reg['VL_TOTAL_DOCUMENTO'], 10) == 0){
										 		reg['VL_TOTAL_DOCUMENTO'] = reg['VL_TOTAL_LIQUIDO'];
										 	}
										 },
										 onDoubleClick: function(){
											 parent.miDocumentoSaidaOnClick({cdDocumentoSaida: gridRemessa.getSelectedRowRegister()['CD_DOCUMENTO_SAIDA'], cdEmpresa: <%=cdEmpresa%>, onClose: function(){loadRemessa();loadVenda();loadRemessaNfe();loadVendaNfe();}});
										 },
										 plotPlace: 'divGridRemessa'});
	
}

function loadRemessaNfe(content) {
	if (content==null) {
		getPage("GET", "loadRemessaNfe", 
				"../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices"+
				"&method=findCompletoByViagem(const "+$('cdViagem').value+": int, const true:boolean)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridRemessaNfe(rsm);
	}
}

var gridRemessaNfe;
function createGridRemessaNfe(rsm){
	gridRemessaNfe = GridOne.create('gridRemessaNfe', {columns: columnsNfe,
										 resultset: rsm,
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 noFocusOnSelect : true, 
										 noSelectOnCreate: false, 
										 onProcessRegister: function(reg){
					
										 },
										 onDoubleClick: function(){

										 },
										 plotPlace: 'divGridRemessaNfe'});
}

function loadVenda(content) {
	if (content==null) {
		getPage("GET", "loadVenda", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices"+
				"&method=findCompletoByViagem(const "+$('cdViagem').value+": int, const false:boolean)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridVenda(rsm);
	}
}

var gridVenda;
function createGridVenda(rsm){
	gridVenda = GridOne.create('gridVenda', {columns: columnsSaida,
										 resultset: rsm,
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 noFocusOnSelect : true, 
										 noSelectOnCreate: false, 
										 onProcessRegister: function(reg){
											 reg['CL_TP_SAIDA']                = tiposSaida[parseInt(reg['TP_SAIDA'], 10)];
										 	 reg['CL_ST_DOCUMENTO_SAIDA']      = situacaoDocumento[parseInt(reg['ST_DOCUMENTO_SAIDA'], 10)];
										 	 reg['CL_TP_DOCUMENTO_SAIDA']      = tiposDocumentoSaida[parseInt(reg['TP_DOCUMENTO_SAIDA'], 10)];
										 	 reg['CL_TP_MOVIMENTO_ESTOQUE']    = tiposMovimento[parseInt(reg['TP_MOVIMENTO_ESTOQUE'], 10)];
										 	 if(parseFloat(reg['VL_TOTAL_DOCUMENTO'], 10) == 0){
										 		reg['VL_TOTAL_DOCUMENTO'] = reg['VL_TOTAL_LIQUIDO'];
										 	 }
										 },
										 onDoubleClick: function(){
											 parent.miDocumentoSaidaOnClick({cdDocumentoSaida: gridVenda.getSelectedRowRegister()['CD_DOCUMENTO_SAIDA'], cdEmpresa: <%=cdEmpresa%>, onClose: function(){loadVenda();loadRemessa();loadRemessaNfe();loadVendaNfe();}});
										 },
										 plotPlace: 'divGridVenda'});
}

function loadVendaNfe(content) {
	if (content==null) {
		getPage("GET", "loadVendaNfe", 
				"../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices"+
				"&method=findCompletoByViagem(const "+$('cdViagem').value+": int, const false:boolean)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridVendaNfe(rsm);
	}
}

var gridVendaNfe;
function createGridVendaNfe(rsm){
	gridVendaNfe = GridOne.create('gridVendaNfe', {columns: columnsNfe,
										 resultset: rsm,
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 noFocusOnSelect : true, 
										 noSelectOnCreate: false, 
										 onProcessRegister: function(reg){
											 
										 },
										 onDoubleClick: function(){
											 
										 },
										 plotPlace: 'divGridVendaNfe'});
}


function loadRetorno(content) {
	if (content==null) {
		getPage("GET", "loadRetorno", 
				"../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices"+
				"&method=findCompletoByViagem(const "+$('cdViagem').value+": int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridRetorno(rsm);
	}
}

var gridRetorno;
function createGridRetorno(rsm){
	gridRetorno = GridOne.create('gridRetorno', {columns: columnsEntrada,
										 resultset: rsm,
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 noFocusOnSelect : true, 
										 noSelectOnCreate: false, 
										 onProcessRegister: function(reg){
											 reg['CL_TP_ENTRADA']   = tiposEntrada[parseInt(reg['TP_ENTRADA'], 10)];
										 	 reg['CL_ST_DOCUMENTO_ENTRADA'] = situacaoDocumentoENT[parseInt(reg['ST_DOCUMENTO_ENTRADA'], 10)];
										 	 reg['CL_TP_DOCUMENTO_ENTRADA'] = tiposDocumentoEntrada[parseInt(reg['TP_DOCUMENTO_ENTRADA'], 10)];
										 	 reg['CL_TP_MOVIMENTO_ESTOQUE'] = tiposMovimentoENT[parseInt(reg['TP_MOVIMENTO_ESTOQUE'], 10)];
										 },
										 onDoubleClick: function(){
											 parent.miDocumentoEntradaOnClick({cdDocumentoEntrada: gridRetorno.getSelectedRowRegister()['CD_DOCUMENTO_ENTRADA'], cdEmpresa: <%=cdEmpresa%>, lgVendaExterna: true, onClose: function(){loadRetorno();loadRetornoNfe();}});
										 },	
										 plotPlace: 'divGridRetorno'});
	
}

function loadRetornoNfe(content) {
	if (content==null) {
		getPage("GET", "loadRetornoNfe", 
				"../methodcaller?className=com.tivic.manager.fsc.NotaFiscalServices"+
				"&method=findCompletoByViagemRetorno(const "+$('cdViagem').value+": int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridRetornoNfe(rsm);
	}
}

var gridRetornoNfe;
function createGridRetornoNfe(rsm){
	gridRetornoNfe = GridOne.create('gridRetornoNfe', {columns: columnsNfe,
										 resultset: rsm,
										 strippedLines: true,
										 columnSeparator: false,
										 lineSeparator: false,
										 noFocusOnSelect : true, 
										 noSelectOnCreate: false, 
										 onProcessRegister: function(reg){
											 
										 },
										 onDoubleClick: function(){
											 
										 },	
										 plotPlace: 'divGridRetornoNfe'});
}


var situacaoDocumento = <%=Jso.getStream(DocumentoSaidaServices.situacoes)%>;
var tipoDocumento     = <%=Jso.getStream(DocumentoSaidaServices.tiposDocumentoSaida)%>;
var tipoSaida         = <%=Jso.getStream(DocumentoSaidaServices.tiposSaida)%>;

function btnNovoDocumentoSaidaRemessaOnClick()	{
	if ($('cdViagem').value == null || $('cdViagem').value == 0)	{
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma viagem.');
		return;
	}
	
	//Não permiti a alteração dos dados depois que o retorno é gerado
	if(gridRetorno.getResultSet().lines.length > 0){
		createTempbox("jTemp", {width: 300, 
            height: 75, 
            message: "Não é possível alterar as informações de notas depois que o retorno é gerado.",
            time: 3000});
		return;
	}
	parent.miDocumentoSaidaOnClick({cdEmpresa: <%=cdEmpresa%>, onClose: function(){loadVenda();loadRemessa();loadRemessaNfe();loadVendaNfe();calcStViagem();}, cdViagem: $('cdViagem').value, cdVendedor: $('cdResponsavel').value, tpDocumentoSaida: <%=DocumentoSaidaServices.TP_NOTA_REMESSA%>, noDestroyWindow: false});
}

function btnNovoDocumentoSaidaVendaOnClick()	{
	if ($('cdViagem').value == null || $('cdViagem').value == 0)	{
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma viagem.');
		return;
	}
	//Não permiti a alteração dos dados depois que o retorno é gerado
	if(gridRetorno.getResultSet().lines.length > 0){
		createTempbox("jTemp", {width: 300, 
            height: 75, 
            message: "Não é possível alterar as informações de notas depois que o retorno é gerado.",
            time: 3000});
		return;
	}
	parent.miDocumentoSaidaOnClick({cdEmpresa: <%=cdEmpresa%>, onClose: function(){loadVenda();loadRemessa();loadRemessaNfe();loadVendaNfe();calcStViagem();}, cdViagem: $('cdViagem').value, cdVendedor: $('cdResponsavel').value, noDestroyWindow: false});
}


function calcStViagem(){
	if(gridRetorno && gridRetorno.getResultSet().lines.length > 0){
		$('stViagem').value = 2;
	}
	else{
		if(gridRemessa && gridRemessa.getResultSet().lines.length > 0){
			$('stViagem').value = 1;
		}
		else{
			$('stViagem').value = 0;
		}
	}
	
}


function btnFindDocumentoSaidaRemessaOnClick()	{
	btnFindDocumentoSaidaOnClick(null, [true]);
}

function btnFindDocumentoSaidaVendaOnClick()	{
	btnFindDocumentoSaidaOnClick(null, [false]);
}

function btnFindDocumentoSaidaOnClick(reg, options)	{
    if(!reg){
    	if ($('cdViagem').value == null || $('cdViagem').value == 0)	{
    		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma viagem.');
    		return;
    	}
    	//Não permiti a alteração dos dados depois que o retorno é gerado
    	if(gridRetorno.getResultSet().lines.length > 0){
    		createTempbox("jTemp", {width: 300, 
                height: 75, 
                message: "Não é possível alterar as informações de notas depois que o retorno é gerado.",
                time: 3000});
    		return;
    	}
    	var sitOpcoes = [{value: '', text: 'Todas'}];
    	for(var i=0; i<situacaoDocumento.length; i++)
    		sitOpcoes.push({value: i, text: situacaoDocumento[i]});
		FilterOne.create("jFiltro", {caption:"Pesquisar Registros", width: 800, height: 400, modal: true, noDrag: true,
									   className: "com.tivic.manager.alm.DocumentoSaidaServices", method: "find", allowFindAll: false,
									   filterFields: [[{label:"Nº", reference:"nr_documento_saida", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
													   {label:"Data Saída", reference:"dt_documento_saida", datatype:_DATE, comparator:_EQUAL, width:15},
													   {label:"Cliente", reference:"B.nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40, charcase:'uppercase'},
													   {label:"Nº Ticket", reference:"nr_conhecimento", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'},
													   {label:"Situação", reference:"st_documento_saida", type:'select', width: 20, defaultValue: '', datatype:_INTEGER, 
														comparator:_EQUAL, options: sitOpcoes}]],
									   gridOptions: {columns: [{label:"Tipo Doc", reference:"CL_TIPO_DOCUMENTO"},
									                           {label:"Nº", reference:"nr_documento_saida"},
															   {label:"Data de Saída", reference:"dt_documento_saida", type: GridOne._DATE},
															   {label:"Cliente", reference:"nm_cliente"},
															   {label:"Valor", reference:"vl_total_documento",type: GridOne._CURRENCY},
															   {label:"Situação", reference:"cl_situacao"},
															   {label:"Nº Ticket", reference:"nr_conhecimento"},
															   {label:"Vendedor", reference:"nm_vendedor"},
															   {label:"Tipo de Saída", reference:"cl_tipo_saida"}],
													 onProcessRegister: function(reg)	{
														 	reg['CL_TIPO_DOCUMENTO'] = tipoDocumento[reg['TP_DOCUMENTO_SAIDA']];
														 	reg['CL_TIPO_SAIDA'] = tipoSaida[reg['TP_SAIDA']];
															reg['CL_SITUACAO'] = situacaoDocumento[reg['ST_DOCUMENTO_SAIDA']];
															reg['VL_LIQUIDO']  = reg['VL_TOTAL_DOCUMENTO'] + reg['VL_ACRESCIMO'] - reg['VL_DESCONTO'];
													 }, strippedLines: true, columnSeparator: false, lineSeparator: false},
									   hiddenFields: [{reference:"cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER},
									                  {reference:"tp_documento_saida",value:<%=DocumentoSaidaServices.TP_NOTA_REMESSA%>, comparator:(options[0] ? _EQUAL : _DIFFERENT),datatype:_INTEGER}],
									   callback: btnFindDocumentoSaidaOnClick, callbackOptions: options, autoExecuteOnEnter: true });
    }
    else {// retorno
    	closeWindow('jFiltro');
        disabledFormDocumentoSaida=true;
        insertViagem(null, reg[0]['CD_DOCUMENTO_SAIDA'], options[0]);
	}
}

function insertViagem(content, cdDocumentoSaida, isRemessa){
	if(content==null)	{
		var objetos = 'documentoSaida=com.tivic.manager.alm.DocumentoSaida();';
		var execute = 'documentoSaida=com.tivic.manager.alm.DocumentoSaidaDAO.get(const '+cdDocumentoSaida+':int);';
		execute    += 'documentoSaida.setCdViagem(const '+$('cdViagem').value+':int);';
		execute    += 'documentoSaida.setCdVendedor(const '+$('cdResponsavel').value+':int);';
		
		// Empresa
		if($('cdViagem').value>0)	{
			getPage('POST', 'insertViagem', 
							   '../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices'+
							   '&objects='+objetos+
							   '&execute='+execute+
							   '&method=update(*documentoSaida:com.tivic.manager.alm.DocumentoSaida)');
		}
	}
	else {	// retorno
		var rsm = null;
		rsm = eval("("+content+")");
		if(rsm==null || rsm.code <= 0){
			createTempbox("jTemp", {width: 300, 
                height: 75, 
                message: "Erro ao cadastrar: " + rsm.message,
                time: 3000});
		}
		else{
			setTimeout('loadRemessa(); loadVenda();loadRemessaNfe();loadVendaNfe();', 1);	
		}
		
	}
	
	
}

function deleteViagemRemessa(){
	deleteViagem(null, true);
}

function deleteViagemVenda(){
	deleteViagem(null, false);
}

function deleteViagem(content, isRemessa){
	if(content==null){
		if ($('cdViagem').value == null || $('cdViagem').value == 0)	{
    		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma viagem.');
    		return;
    	 }
		 //Não permiti a alteração dos dados depois que o retorno é gerado
		 if(gridRetorno.getResultSet().lines.length > 0){
			createTempbox("jTemp", {width: 300, 
	            height: 75, 
	            message: "Não é possível alterar as informações de notas depois que o retorno é gerado.",
	            time: 3000});
			return;
		 }
		 createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
	         message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
	         positiveAction: function() {setTimeout("deleteViagemAux(null, "+isRemessa+")", 10)}});
	}
	else{
		setTimeout('loadRemessa(); loadVenda();loadRemessaNfe();loadVendaNfe();', 1);
	}
}

function deleteViagemAux(content, isRemessa){
	if(content==null)	{
		var objetos = 'documentoSaida=com.tivic.manager.alm.DocumentoSaida();';
		var execute = 'documentoSaida=com.tivic.manager.alm.DocumentoSaidaDAO.get(const '+(isRemessa ? gridRemessa.getSelectedRowRegister()['CD_DOCUMENTO_SAIDA'] : gridVenda.getSelectedRowRegister()['CD_DOCUMENTO_SAIDA'])+':int);';
		execute    += 'documentoSaida.setCdViagem(const 0:int);';
		
		// Empresa
		if($('cdViagem').value>0)	{
			setTimeout(function()	{
					   getPage('POST', 'deleteViagem', 
							   '../methodcaller?className=com.tivic.manager.alm.DocumentoSaidaServices'+
							   '&objects='+objetos+
							   (execute!=''?'&execute=':'')+execute+
							   '&method=update(*documentoSaida:com.tivic.manager.alm.DocumentoSaida)')}, 10);
		}
	}
	else {	// retorno
		var rsm = null;
		rsm = eval("("+content+")");
		if(rsm==null || rsm.code <= 0){
			createTempbox("jTemp", {width: 300, 
                height: 75, 
                message: "Erro ao cadastrar: " + rsm.message,
                time: 3000});
		}
		
	}
}

function btnFindCidadeOrigemEnderecoOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Cidades", 
												   width: 350,
												   height: 225,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.CidadeServices",
												   method: "find(*crt:java.util.ArrayList, const 0:int)",
												   allowFindAll: true,
												   filterFields: [[{label:"Cidade", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'},
												   				   {label:"UF", reference:"SG_ESTADO", datatype:_VARCHAR, comparator:_EQUAL, width:10, charcase:'uppercase'},
																   {label:"ID", reference:"ID_CIDADE", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Cidade", reference:"NM_CIDADE"},
												   						   {label:"UF", reference:"SG_ESTADO"},
												   						   {label:"ID IBGE", reference:"ID_IBGE"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindCidadeOrigemEnderecoOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
		$('cdCidadeOrigem').value = reg[0]['CD_CIDADE'];
		$('cdCidadeOrigemView').value = reg[0]['NM_CIDADE'];
		
    }
}

function btnFindCidadeDestinoEnderecoOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Cidades", 
												   width: 350,
												   height: 225,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.CidadeServices",
												   method: "find(*crt:java.util.ArrayList, const 0:int)",
												   allowFindAll: true,
												   filterFields: [[{label:"Cidade", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'},
												   				   {label:"UF", reference:"SG_ESTADO", datatype:_VARCHAR, comparator:_EQUAL, width:10, charcase:'uppercase'},
																   {label:"ID", reference:"ID_CIDADE", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Cidade", reference:"NM_CIDADE"},
												   						   {label:"UF", reference:"SG_ESTADO"},
												   						   {label:"ID IBGE", reference:"ID_IBGE"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindCidadeDestinoEnderecoOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
		$('cdCidadeDestino').value = reg[0]['CD_CIDADE'];
		$('cdCidadeDestinoView').value = reg[0]['NM_CIDADE'];
		
    }
}

function btnGerarRetornoOnClick(content){
	if(content==null)	{
		var objects = 'codigosRemessa=java.util.ArrayList();codigosVenda=java.util.ArrayList();';
		var execute = '';
		
		//Busca todos os codigos das notas de remessa
		for (var i = 0; i < gridRemessa.getResultSet().lines.length; i++){
			objects += 'cdDocumentoRemessa'+i+'=java.lang.Integer(const ' +gridRemessa.getResultSet().lines[i]['CD_DOCUMENTO_SAIDA'] + ':int);';
			execute += 'codigosRemessa.add(*cdDocumentoRemessa'+i+':Object);';	
		}
		//Busca todos os codigos das notas de venda
		for (var i = 0; i < gridVenda.getResultSet().lines.length; i++){
			objects += 'cdDocumentoVenda'+i+'=java.lang.Integer(const ' +gridVenda.getResultSet().lines[i]['CD_DOCUMENTO_SAIDA'] + ':int);';
			execute += 'codigosVenda.add(*cdDocumentoVenda'+i+':Object);';	
		}
		
		//Não permiti gerar a nota de retorno sem pelo menos uma nota de remessa
		if(gridRemessa.getResultSet().lines.length == 0){
			createTempbox("jTemp", {width: 300, 
                height: 75, 
                message: "Deve haver pelo menos uma remessa para ser lançado o retorno.",
                time: 3000});
			return;
		}
		//Não permiti a criação de mais de um documento de retorno para a mesma viagem
		if(gridRetorno.getResultSet().lines.length > 0){
			createTempbox("jTemp", {width: 300, 
	            height: 75, 
	            message: "Já existe um retorno para essa viagem.",
	            time: 3000});
			return;
		}
		
		if($('cdViagem').value>0)	{
			setTimeout(function()	{
					   createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
					   getPage('POST', 'btnGerarRetornoOnClick', 
							   '../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices'+
							   '&objects=' + objects +
							   '&execute=' + execute +
							   '&method=entradaFromVendaExterna(*codigosRemessa:java.util.ArrayList, *codigosVenda:java.util.ArrayList, const '+<%=cdLocalArmazenamento%>+':int)')}, 10);
		}
	}
	else {	// retorno
		closeWindow('jLoadMsg');
		var rsm = null;
		rsm = eval("("+content+")");
		if(rsm!=null && rsm.code == <%=DocumentoEntradaServices.RET_SEM_RETORNO%>){
			createTempbox("jTemp", {width: 300, 
                height: 75, 
                message: rsm.message,
                time: 3000});
		}
		else if(rsm==null || rsm.code <= 0){
			createTempbox("jTemp", {width: 300, 
                height: 75, 
                message: "Erro ao cadastrar: " + rsm.message,
                time: 3000});
		}
		else{
			setTimeout('loadRetorno();loadRetornoNfe();', 1);	
			parent.miDocumentoEntradaOnClick({cdDocumentoEntrada: rsm.code, cdEmpresa: <%=cdEmpresa%>, lgVendaExterna: true, onClose: function(){loadRetorno();loadRetornoNfe();}});
		}
		
	}
}

function btnDeleteRetornoOnClick(content){
	if(content==null){
		if(gridRetorno.getResultSet().lines.length == 0){
			createTempbox("jTemp", {width: 300, 
	            height: 75, 
	            message: "Não existe retorno para ser apagado.",
	            time: 3000});
			return;
		}
		createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
	         message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
	         positiveAction: function() {setTimeout("btnDeleteRetornoOnClickAux(null)", 10);}});
	}
	
}

function btnDeleteRetornoOnClickAux(content){
	if(content==null)	{
		// Viagem
		if($('cdViagem').value>0)	{
			setTimeout(function()	{
					   createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
					   getPage('POST', 'btnDeleteRetornoOnClickAux', 
							   '../methodcaller?className=com.tivic.manager.alm.DocumentoEntradaServices'+
							   '&method=deleteByViagem(const '+$('cdViagem').value+':int)')}, 10);
		}
	}
	else {	// retorno
		closeWindow('jLoadMsg');
		var rsm = null;
		rsm = eval("("+content+")");
		if(rsm==null || rsm.code <= 0){
			createTempbox("jTemp", {width: 300, 
                height: 75, 
                message: "Erro ao deletar: " + rsm.message,
                time: 3000});
		}
		else{
			createTempbox("jTemp", {width: 300, 
                height: 75, 
                message: "Nota excluída com sucesso.",
                time: 3000});
			setTimeout('loadRetorno();loadRetornoNfe();', 1);
		}
		
	}
}

var recursosFloatMenuRelatorio = null;
function btnImprimirRelatoriosOnClick()	{
	    if (recursosFloatMenuRelatorio == null) {
			recursosFloatMenuRelatorio = FloatMenuOne.create('recursosFloatMenuRelatorio', {width: 100, height: 100, plotPlace: $('bodyPrincipal'), color:'CADDED'});
			recursosFloatMenuRelatorio.insertItem({label: 'Diferença', icon: 'imagens/cifrao.png', onclick: function(){ btnGerarRelatorioDiferenca();}});
			recursosFloatMenuRelatorio.insertItem({label: 'Empilhadeira', icon: 'imagens/produto_dinamico13.gif', onclick: function(){ btnGerarRelatorioEmpilhadeira();}});
			recursosFloatMenuRelatorio.insertItem({label: 'Fechar este menu', id:'close', icon: '/sol/imagens/cancel_13.gif'});
	  	}
	  	recursosFloatMenuRelatorio.show({element: $('btnImprimirRelatorios'),suby:63});
}

function btnGerarRelatorioDiferenca(){
	if ($('cdViagem').value == null || $('cdViagem').value == 0)	{
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma viagem.');
		return;
	}
	if(gridVenda.getResultSet().lines.length == 0){
		createTempbox("jTemp", {width: 300, 
            height: 75, 
            message: "Deve haver pelo menos uma venda para gerar esse relatório.",
            time: 3000});
		return;
	}
	var caption    = "Relatório de Vendas Externas";
	var className  = "com.tivic.manager.alm.DocumentoSaidaServices";
	var method     = "gerarRelatorioVendasExternas(const "+<%=cdEmpresa%>+":int, const "+$('cdViagem').value+":int)"; 
	var nomeJasper = "relatorio_vendas_externas";	
		
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
					 "&cdEmpresa=" 	+ <%=cdEmpresa%> + 
					 "&modulo=alm"});


}

function btnGerarRelatorioEmpilhadeira(){
	if ($('cdViagem').value == null || $('cdViagem').value == 0)	{
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma viagem.');
		return;
	}
	if(gridVenda.getResultSet().lines.length == 0){
		createTempbox("jTemp", {width: 300, 
            height: 75, 
            message: "Deve haver pelo menos uma venda para gerar esse relatório.",
            time: 3000});
		return;
	}
	var caption    = "Relatório de Empilhadeira";
	var className  = "com.tivic.manager.alm.DocumentoSaidaServices";
	var method     = "gerarRelatorioEmpilhadeira(const "+<%=cdEmpresa%>+":int, const "+$('cdViagem').value+":int)"; 
	var nomeJasper = "relatorio_empilhadeira";	
		
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
					 "&cdEmpresa=" 	+ <%=cdEmpresa%> + 
					 "&modulo=alm"});


}

</script>
<style>
.paradaLabel {
	width: 50px; 
	height:15px; 
	text-align:center;
}

.trechoLinha {
	position:absolute;
	left:20px;
	height:5px;
	top:-5px;
}

.trechoLinhaFundo {
	position:absolute;
	left:24px;
	height:14px;
	top:-14px;
	border-left:1px solid #FF0000;
}

.trecho {
	position:relative;
	height:24px;
	float:left;
}

.trecho-parada {
	background-color:#FFFF99; 
	border:1px solid #FFCC00; 
	width:16px; 
	height:16px; 
	text-align:center; 
	font-family:Geneva, Arial, Helvetica, sans-serif; 
	font-size:10px; 
	font-weight:bold; 
	margin:3px 0 0 0; 
	line-height:16px; 
	position:absolute; 
	right:-8px; 
	z-index:50;
	cursor:help;
}

</style>
</head>
<body class="body" onload="init();" id="bodyPrincipal">
<div style="width: 902px;" id="fta_rota" class="d1-form">
  <div style="width: 902px; height: 478px;" class="d1-body">
    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 892px;"></div>
    <input idform="" reference="" id="contentLogViagem" name="contentLogViagem" type="hidden">
    <input idform="" reference="" id="dataOldViagem" name="dataOldViagem" type="hidden">
    <input idform="viagem" reference="cd_viagem" id="cdViagem" name="cdViagem" type="hidden">
    <input idform="viagem" reference="" id="cdPessoaUsuario" name="cdPessoaUsuario" type="hidden" value="<%=cdPessoaUsuario%>" defaultvalue="<%=cdPessoaUsuario%>" />
    <div id="divTabViagem"></div>
    	<div class="d1-line" id="line1">
	    	<div style="width: 200px;" class="element">
				<label class="caption" for="dsLink">Nome</label>
				<input style="text-transform: uppercase; width: 195px;" class="field2" idform="viagem" reference="txt_observacao" datatype="STRING" id="txtObservacao" name="txtObservacao"></input>
			</div>
			<div style="width: 150px;" class="element">
                <label class="caption" for="tpViagem">Situação</label>
                <select nullvalue="-1" column="st_viagem" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width: 145px;" class="select2" idform="viagem" reference="st_viagem" datatype="INT" id="stViagem" name="stViagem">
               		<option value="0">Em Conferência</option>
                    <option value="1">Em Viagem</option>
                    <option value="2">Chegada</option>
                </select>
            </div>
			<div style="width: 140px;" class="element">
				<label class="caption" for="dtSaida">Dt. Sa&iacute;da</label>
				<input style="width: 137px;" mask="dd/mm/yyyy" maxlength="10" class="field2" idform="viagem" reference="dt_saida" datatype="DATETIME" id="dtSaida" name="dtSaida" type="text"/>
				<button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtSaida" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
			</div>
			<div style="width: 140px;" class="element">
				<label class="caption" for="dtChegada">Dt. Chegada</label>
				<input style="width: 137px;" mask="dd/mm/yyyy" maxlength="10" class="field2" idform="viagem" reference="dt_chegada" datatype="DATETIME" id="dtChegada" name="dtChegada" type="text"/>
				<button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtChegada" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
			</div>
			<div style="width: 260px; margin-left: 2px;" class="element">
		        <label class="caption" for="cdResponsavel">Responsavel</label>
		        <input logmessage="Código Responsavel" idform="viagem" reference="cd_responsavel" datatype="INT" id="cdResponsavel" name="cdResponsavel" type="hidden" value="0" defaultvalue="0">
		        <input style="width: 225px;" logmessage="Nome Responsavel" idform="viagem" reference="nm_responsavel" static="true" disabled="disabled" class="disabledField2" name="cdResponsavelView" id="cdResponsavelView" type="text">
		        <button id="btnFindResponsavel" onclick="btnFindResponsavelOnClick()" idform="viagem" title="Localizar responsavel" class="controlButton controlButton2" style="height: 22px;"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
		        <button onclick="btnClearResponsavelOnClick()" idform="viagem" title="Limpar este campo" class="controlButton" style="height: 22px;"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
		    </div>
    	</div>
    	<div class="d1-line" id="line1">
			<div style="width: 390px;" class="element">
				<label class="caption">Ve&iacute;culo</label>
				<input idform="viagem" reference="cd_veiculo" datatype="STRING" id="cdVeiculo" name="cdVeiculo" type="hidden" value="0" defaultValue="0">
				<input idform="viagem" reference="nm_veiculo" style="width: 385px;" static="true" disabled="disabled" class="disabledField2" name="cdVeiculoView" id="cdVeiculoView" type="text">
				<button onclick="btnFindVeiculoOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2" style="height: 22px;"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
				<button onclick="btnClearVeiculoOnClick()" title="Limpar este campo..." class="controlButton" style="height: 22px;"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			</div>
			<div style="width: 250px;" class="element">
	          <label class="caption" for="cdCidadeOrigem">Origem</label>
	          <input logmessage="Código da Cidade de Origem" idform="viagem" reference="cd_cidade_origem" datatype="STRING" id="cdCidadeOrigem" name="cdCidadeOrigem" value="<%=cdCidadeDefault%>" defaultValue="<%=cdCidadeDefault%>" type="hidden"/>
	          <input logmessage="Nome da Cidade de Origem" idform="viagem" reference="nm_cidade_origem" style="width: 215px;" static="true" disabled="disabled" value="<%=nmCidade%>" defaultValue="<%=nmCidade%>" class="disabledField2" name="cdCidadeOrigemView" id="cdCidadeOrigemView" type="text"/>
	          <button id="btnFindCidadeOrigemEndereco" onclick="btnFindCidadeOrigemEnderecoOnClick(null)" idform="viagem" title="Pesquisar valor para este campo..." class="controlButton controlButton2" style="height: 22px;"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
	          <button onclick="$('cdCidadeOrigem').value = 0; $('cdCidadeOrigemView').value = ''" idform="viagem" title="Limpar este campo..." class="controlButton" style="height: 22px;"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
	        </div>
	        <div style="width: 250px;" class="element">
	          <label class="caption" for="cdCidadeDestino">Destino</label>
	          <input logmessage="Código da Cidade de Destino" idform="viagem" reference="cd_cidade_destino" datatype="STRING" id="cdCidadeDestino" name="cdCidadeDestino" value="0" defaultValue="0" type="hidden"/>
	          <input logmessage="Nome da Cidade de Destino" idform="viagem" reference="nm_cidade_destino" style="width: 215px;" static="true" disabled="disabled" value="" defaultValue="" class="disabledField2" name="cdCidadeDestinoView" id="cdCidadeDestinoView" type="text"/>
	          <button id="btnFindCidadeDestinoEndereco" onclick="btnFindCidadeDestinoEnderecoOnClick(null)" idform="viagem" title="Pesquisar valor para este campo..." class="controlButton controlButton2" style="height: 22px;"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
	          <button onclick="$('cdCidadeDestino').value = 0; $('cdCidadeDestinoView').value = ''" idform="viagem" title="Limpar este campo..." class="controlButton" style="height: 22px;"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
	        </div>
        </div>
		<div id="divTabRemessa" style="margin-top: 45px;"></div>
		<div id="divTabRemessaDados">
			<div class="d1-line" id="line1">
				<label class="caption" style="float:left;">Notas de Remessa</label> 
				<div id="divGridRemessa" style="width:822px; height:50px; background-color:#FFF; border:1px solid #000000; float:left;">&nbsp;</div>
				<div id="toolBarGridRemessa" class="d1-toolBar" style="height:50px; width:56px; float:left;"></div>
			</div>
		</div>
		<div id="divTabRemessaNfe">
			<div class="d1-line" id="line1">
				<div id="divGridRemessaNfe" style="width:822px; height:70px; background-color:#FFF; border:1px solid #000000; float:left;">&nbsp;</div>
				<div id="toolBarGridRemessaNfe" class="d1-toolBar" style="height:70px; width:56px; float:left;"></div>
			</div>
		</div>
		<div id="divTabVenda"></div>
		<div id="divTabVendaDados">
			<div class="d1-line" id="line1">
				<label class="caption" style="float:left;">Notas de Venda</label> 
				<div id="divGridVenda" style="width:822px; height:90px; background-color:#FFF; border:1px solid #000000; float:left;">&nbsp;</div>
				<div id="toolBarGridVenda" class="d1-toolBar" style="height:90px; width:56px; float:left;"></div>
			</div>
		</div>
		<div id="divTabVendaNfe">
			<div class="d1-line" id="line1">
				<div id="divGridVendaNfe" style="width:822px; height:110px; background-color:#FFF; border:1px solid #000000; float:left;">&nbsp;</div>
				<div id="toolBarGridVendaNfe" class="d1-toolBar" style="height:110px; width:56px; float:left;"></div>
			</div>
		</div>
		<div id="divTabRetorno"></div>
		<div id="divTabRetornoDados">
			<div class="d1-line" id="line1">
				<label class="caption" style="float:left;">Nota de Retorno</label> 
				<div id="divGridRetorno" style="width:822px; height:50px; background-color:#FFF; border:1px solid #000000; float:left;">&nbsp;</div>
			</div>
		</div>
		<div id="divTabRetornoNfe">
			<div class="d1-line" id="line1">
				<div id="divGridRetornoNfe" style="width:822px; height:70px; background-color:#FFF; border:1px solid #000000; float:left;">&nbsp;</div>
			</div>
		</div>
  </div>
</div>

</body>
</html>
