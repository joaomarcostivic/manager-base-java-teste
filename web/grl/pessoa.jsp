<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="sol.dao.ResultSetMap" %>
<%@page import="com.tivic.manager.seg.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.util.*"%>
<%
	try {
		int cdEmpresa             = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		int cdVinculo             = RequestUtilities.getParameterAsInteger(request, "cdVinculo", 0);
		int lgDocObrigatorio      = ParametroServices.getValorOfParametroAsInteger("LG_DOCUMENTOS_OBRIGATORIOS", 0, 0);
		Vinculo vinculo           = cdVinculo==0 ? null : VinculoDAO.get(cdVinculo);
		int cdFormulario          = vinculo==null ? 0 : vinculo.getCdFormulario();
		ResultSetMap rmsAtributos = com.tivic.manager.grl.FormularioServices.getAllAtributos(cdFormulario);
		int cdPessoa           	  = RequestUtilities.getParameterAsInteger(request, "cdPessoa", 0);
		Usuario usuario           = (Usuario)session.getAttribute("usuario");
		int cdUsuario             = usuario==null ? 0 : usuario.getCdUsuario();
		int cdPessoaUsuario       = usuario==null ? 0 : usuario.getCdPessoa();
		Pessoa pessoa             = cdPessoaUsuario==0 ? null : PessoaDAO.get(cdPessoaUsuario);
		String nmPessoaUsuario    = pessoa==null ? null : pessoa.getNmPessoa();
		int lgPessoaArquivos      = ParametroServices.getValorOfParametroAsInteger("LG_PESSOA_ARQUIVOS", 0, cdEmpresa);
		boolean showFactoring	  = com.tivic.manager.seg.ModuloServices.isActive("fac");
		boolean showDependentes   = RequestUtilities.getParameterAsInteger(request, "showDependentes", 0)==1;
		boolean showArquivos 	  = lgPessoaArquivos==1;
		
		int tpPessoa              = RequestUtilities.getParameterAsInteger(request, "tpPessoa", 2); //2 = qualquer
		int tpEnderecamento       = ParametroServices.getValorOfParametroAsInteger("TP_ENDERECAMENTO", PessoaEnderecoServices.TP_DIGITAVEL);
		int cdCidadeDefault       = ParametroServices.getValorOfParametroAsInteger("CD_CIDADE_END_DEFAULT", 0, cdEmpresa);
		Cidade cidade             = cdCidadeDefault<=0 ? null : CidadeDAO.get(cdCidadeDefault);
		String nmCidade           = cidade==null ? "" : cidade.getNmCidade();
		
		Empresa empresa           = EmpresaDAO.get(cdEmpresa);
%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<loader:library libraries="shortcut, form, toolbar, grid2.0, aba2.0, calendario, filter" compress="false" />
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<security:registerForm idForm="formPessoa"/>
<script language="javascript">
var disabledFormPessoa  = false;
var linesPessoaFisica   = [$('line4'), $('line5'), $('line6'), $('line21')];
var linesPessoaJuridica = [$('line8'), $('line9'), $('line20')];
var gridDocumentos = null;
var gridEnderecos = null;
var gridOcorrencias = null;
var gridArquivos = null;
var tabPessoa = null;

var tabVinculos = null;
var gridDependentes = null;
var gridVinculos = null;

var cdVinculo = <%=cdVinculo%>;
var cdFormularioDefault = <%=cdFormulario%>;
var rsmAtributos = null;

var gridContasReceber = null;
function init()	{
    var cdEmpresa = parent.$('cdEmpresa')!=null ? parent.$('cdEmpresa').value : 0;
	$('cdEmpresa').value = cdEmpresa;
	$('cdEmpresa').setAttribute('defaultValue', cdEmpresa);
	if (cdFormularioDefault > 0) {
		$("divAbaAtributos").style.display = '';
		try { rsmAtributos = eval("(" + content + ")"); } catch(e) {}
		loadAtributos(rsmAtributos, cdFormularioDefault);
	}	
	else {
		initPessoa();
	}
}

function initPessoa(){
	configureTabFields(['cdVinculo', 'dtVinculo', 'stVinculo', 'btnSaveVinculo']);
	configureTabFields(['cdTipoDocumento', 'nrDocumento', 'btnSaveTipoDocumento']);

	var toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
								    	     orientation: 'horizontal',
								    		 buttons: [{id: 'btnNewPessoa', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo... [Ctrl + N]', onClick: btnNewPessoaOnClick},
								    	     		   {id: 'btnAlterPessoa', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterPessoaOnClick},
										    		   {id: 'btnSavePessoa', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSavePessoaOnClick},
								    	     		   {id: 'btnDeletePessoa', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeletePessoaOnClick},
										    		   {id: 'btnFindPessoa', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar...', onClick: btnFindPessoaOnClick},
												       {separator: 'horizontal'},
												       {id: 'btnFingerprint', img: 'imagens/fingerprint16.gif', label: 'Impressão Digital', title: 'Capturar impressão digital...', onClick: btnFingerprintOnClick},
												       {id: 'btnSubstituirPessoa', img: 'imagens/pessoa_substituir16.gif', label: 'Substituir', title: 'Substituir...', onClick: btnSubstituirPessoaOnClick},
													   {separator: 'horizontal'},
												       {id: 'btnPrintPessoa', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', title: 'Imprimir...', onClick: btnPrintPessoaOnClick}
													   ]});

	tabPessoa = TabOne.create('tabPessoa', {width: 690,
	                                        height: 376,
											tabs: [{caption: 'Dados básicos', 
													reference:'divAbaDadosBasicos',
													active: true},
												   {caption: 'Outras informações', 
													reference:'divAbaOutrosDados'}
												   <%=cdFormulario > 0 ? 
												    ", {caption: 'Atributos', " +
													"reference:'divAbaAtributos'}" : ""%>
												   <%=showArquivos ? 
												    ", {caption: 'Arquivos', " +
													"reference:'divAbaArquivos'}" : ""%>
												   <%=showDependentes ? 
												    ", {caption: 'Dependentes', " +
													"reference:'divAbaVinculosDependentes'}" : ""%>
												   <%=showFactoring ? 
												    ", {caption: 'Factoring', " +
													"reference:'divAbaFactoring'}" : ""%>],
													plotPlace: 'divTabPessoa',
													tabPosition: ['top', 'left'],
													borderColor: null});
	
	addShortcut('ctrl+n', function(){ if (!$('btnNewPessoa').disabled) btnNewPessoaOnClick() });
	addShortcut('ctrl+a', function(){ if (!$('btnAlterPessoa').disabled) btnAlterPessoaOnClick() });
	addShortcut('ctrl+p', function(){ if (!$('btnFindPessoa').disabled) btnFindPessoaOnClick() });
	addShortcut('ctrl+e', function(){ if (!$('btnDeletePessoa').disabled) btnDeletePessoaOnClick() });
	
	loadOptionsFromRsm($('cdFormaDivulgacao'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.FormaDivulgacaoDAO.getAll())%>, {fieldValue: 'CD_FORMA_DIVULGACAO', fieldText:'NM_FORMA_DIVULGACAO'});
	loadOptionsFromRsm($('cdEstadoRg'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EstadoServices.getAll())%>, {fieldValue: 'cd_estado', fieldText:'sg_estado'});
	loadOptionsFromRsm($('cdTipoDocumento'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoDocumentoDAO.getAll())%>, {fieldValue: 'cd_tipo_documento', fieldText:'nm_tipo_documento'});
	loadOptionsFromRsm($('cdFuncaoContato'), <%=sol.util.Jso.getStream(com.tivic.manager.srh.FuncaoServices.getAll(0))%>, {fieldValue: 'cd_funcao', fieldText:'nm_funcao'});
	loadOptions($('tpCategoriaHabilitacao'), <%=sol.util.Jso.getStream(PessoaFisicaServices.tipoCategoriaHabilitacao)%>);
	loadOptions($('stEstadoCivil'), <%=sol.util.Jso.getStream(PessoaFisicaServices.situacaoEstadoCivil)%>);
	loadOptions($('tpOperacao'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.ContaFinanceiraServices.tipoOperacao)%>);
	loadOptions($('stOcorrencia'), <%=sol.util.Jso.getStream(Recursos.situacaoOcorrencia)%>);
	loadOptionsFromRsm($('cdEscolaridade'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EscolaridadeDAO.getAll())%>, {fieldValue: 'cd_escolaridade', fieldText:'nm_escolaridade'});
	loadOptionsFromRsm($('cdTipoEndereco'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoEnderecoDAO.getAll())%>, {fieldValue: 'cd_tipo_endereco', fieldText:'nm_tipo_endereco'});
	loadOptionsFromRsm($('cdTipoEnderecoPrincipal'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoEnderecoDAO.getAll())%>, {fieldValue: 'cd_tipo_endereco', fieldText:'nm_tipo_endereco'});
	if ($('cdTipoLogradouroPessoa') != null) {
		loadOptionsFromRsm($('cdTipoLogradouroPessoa'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoLogradouroDAO.getAll())%>, {fieldValue: 'cd_tipo_logradouro', fieldText:'nm_tipo_logradouro'});
		configureTabFields(['idPessoa', 'cdTipoLogradouroPessoa']);
		configureTabFields(['nmBairroPessoa', 'nmPontoReferenciaPessoa']);
		configureTabFields(['nrTelefonePessoa', 'btnSavePessoa']);
	}
	loadOptionsFromRsm($('cdTipoLogradouro'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoLogradouroDAO.getAll())%>, {fieldValue: 'cd_tipo_logradouro', fieldText:'nm_tipo_logradouro'});
	loadOptionsFromRsm($('cdTipoArquivo'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoArquivoDAO.getAll())%>, {fieldValue: 'cd_tipo_arquivo', fieldText:'nm_tipo_arquivo'});
	loadOptionsFromRsm($('cdTipoOcorrencia'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoOcorrenciaDAO.getAll())%>, {fieldValue: 'cd_tipo_ocorrencia', fieldText:'nm_tipo_ocorrencia'});
	loadOptionsFromRsm($('cdNivelAcesso'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.NivelAcessoDAO.getAll())%>, {fieldValue: 'cd_nivel_acesso', fieldText:'nm_nivel_acesso'});
	loadOptionsFromRsm($('cdBanco'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.BancoDAO.getAll())%>, {fieldValue: 'cd_banco', fieldText:'nm_banco'});
	loadOptions($('sgOrgaoRg'), <%=sol.util.Jso.getStream(PessoaFisicaServices.siglaOrgaoExpedidor)%>);
	loadOptions($('nmFormaTratamento'), <%=sol.util.Jso.getStream(PessoaFisicaServices.formaTratamento)%>);
	loadOptions($('tpSexo'), <%=sol.util.Jso.getStream(PessoaFisicaServices.tipoSexo)%>);
	loadOptions($('tpEmpresa'), <%=sol.util.Jso.getStream(PessoaJuridicaServices.tipoEmpresa)%>);
	loadOptions($('stVinculo'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.PessoaEmpresaServices.situacaoVinculo)%>);
	loadOptions($('stConta'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.PessoaContaBancariaServices.situacaoConta)%>);
	if($('cdVinculo'))
		loadOptionsFromRsm($('cdVinculo'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.VinculoDAO.getAll())%>, {fieldValue: 'cd_vinculo', fieldText:'nm_vinculo'<%if (cdVinculo>0) { %>, defaultValue:<%=cdVinculo%><% }%><%if (cdVinculo<=0) { %>, setDefaultValueFirst:true<% }%>});
    if ($('btnSavePessoa').disabled && $('btnSavePessoa').firstChild)
		$('btnSavePessoa').firstChild.src = '/sol/imagens/form-btSalvarDisabled16.gif';
    if ($('btnAlterTipoDocumento')!=null && $('btnAlterTipoDocumento').disabled && $('btnNewTipoDocumento').firstChild)
		$('btnSaveTipoDocumento').firstChild.src = '/sol/imagens/form-btSalvarDisabled16.gif';
	var divFieldsNaoDigitavel = ['divFieldsEndereco1', 'divFieldsEndereco3', 'divFieldsEndereco5', 'divFieldsEndereco7',
								 'divFieldsPessoaEndereco1', 'divFieldsPessoaEndereco3', 'divFieldsPessoaEndereco5', 'divFieldsPessoaEndereco7'];
	var divFieldsDigitavel = ['divFieldsEndereco2', 'divFieldsEndereco4', 'divFieldsEndereco6', 'divFieldsEndereco8',
							  'divFieldsPessoaEndereco2', 'divFieldsPessoaEndereco4', 'divFieldsPessoaEndereco6', 'divFieldsPessoaEndereco8'];
	for (var i=0; i < divFieldsDigitavel.length; i++) {
		if ($(divFieldsDigitavel[i]) != null)
			$(divFieldsDigitavel[i]).style.display = $('tpEnderecamento').value == <%=PessoaEnderecoServices.TP_DIGITAVEL%> ? "" : "none";
		if ($(divFieldsNaoDigitavel[i]) != null)
			$(divFieldsNaoDigitavel[i]).style.display = $('tpEnderecamento').value == <%=PessoaEnderecoServices.TP_END_LOGRADOUROS_BAIRROS%> ? "" : "none";
	}
	if ($('tpEnderecamento').value == <%=PessoaEnderecoServices.TP_DIGITAVEL%>) {
		configureTabFields(['cdTipoEndereco', 'cdTipoLogradouro', 'nmLogradouro', 'nrEndereco2', 'nmComplemento2', 'nmBairro', 'nmPontoReferencia', 'nrCep',
							'btnFindCidadeEndereco', 'nrTelefone', 'dsEndereco', 'lgPrincipal', 'lgCobranca', 'btnSaveEndereco']);
	}
	else {
		configureTabFields(['cdTipoEndereco', 'btnFindLogradouro', 'nrEndereco1', 'nmComplemento1', 'btnFindBairro', 'nmPontoReferencia', 'nrCep',
							'btnFindCidadeEndereco', 'nrTelefone', 'dsEndereco', 'lgPrincipal', 'lgCobranca', 'btnSaveEndereco']);
	}

	enableTabEmulation();
	
	var dataMask = new Mask('##/##/####');
    dataMask.attach($("dtCadastro"));
    dataMask.attach($("dtInicioAtividade"));
    dataMask.attach($("dtNascimento"));
    dataMask.attach($("dtChegadaPais"));

	dataMask.attach($("dtEmissaoRg"));
	dataMask.attach($("dtPrimeiraHabilitacao"));
	dataMask.attach($("dtValidadeCnh"));
	dataMask.attach($("dtCadastroContato"));
	dataMask.attach($("dtVinculo"));
	var nrTelefoneMask = new Mask("(##)####-####");
	nrTelefoneMask.attach($("nrTelefone1"));
	nrTelefoneMask.attach($("nrTelefone2"));
	nrTelefoneMask.attach($("nrFax"));
	nrTelefoneMask.attach($("nrCelular"));
	nrTelefoneMask.attach($("nrTelefone"));
	nrTelefoneMask.attach($("nrTelefone2Contato"));
	nrTelefoneMask.attach($("nrCelularContato"));
    nrTelefoneMask.attach($("nrTelefone1Contato"));
	if ($("nrCpf") != null) {
		var nrCpfMask = new Mask($("nrCpf").getAttribute("mask"));
		nrCpfMask.attach($("nrCpf"));
	}
	
	if ($("nrCnpj") != null) {
		var nrCnpjMask = new Mask($("nrCnpj").getAttribute("mask"));
		nrCnpjMask.attach($("nrCnpj"));
	}
	
	if ($("nrCep") != null) {
		var nrCepMask = new Mask($("nrCep").getAttribute("mask"));
		nrCepMask.attach($("nrCep"));
	}
	if ($("nrCepAgd") != null) {
		var nrCepMask = new Mask($("nrCepAgd").getAttribute("mask"));
		nrCepMask.attach($("nrCepAgd"));
	}
	var nrTelefoneMask = new Mask("(##)####-####");
	if ($("nrCepPessoa") != null) {
		var nrCepMask = new Mask($("nrCepPessoa").getAttribute("mask"));
		nrCepMask.attach($("nrCepPessoa"));
	}
	
	<%if(showFactoring){%>
	var nrMoneyMask = new Mask($("vlLimiteFactoring").getAttribute("mask"), "number");
	nrMoneyMask.attach($("vlLimiteFactoring"));
	nrMoneyMask.attach($("vlLimiteFactoringEmissor"));
	nrMoneyMask.attach($("vlLimiteFactoringUnitario"));
	nrMoneyMask.attach($("vlGanhoMinimoFactoring"));
	nrMoneyMask.attach($("vlGanhoMinimoFactoring"));
	<%}%>
	
	
    loadFormFields(["pessoa", "documento", "endereco", 
	                "vinculo", "ocorrencia", "arquivo", 
					"contaBancaria", "contato","dependentes"]);
	/* VINCULOS / DADOS FUNCIONAIS*/
	clearFormPessoa();			
	if ($('btnNewPessoa').disabled || $('cdPessoa').value != '0') {
		disabledFormPessoa=true;
		alterFieldsStatus(false, pessoaFields, "nmPessoa", "disabledField");
	}
	else {
	   try {  
			if ($('cdGrupo')!=null)
				$('cdGrupo').focus();
			else if ($('gnPessoa') != null)
				$('gnPessoa').focus();
	   }
	   catch(e) {}
	}
	<% if (cdPessoa > 0) { %>
		loadPessoa(null, <%=cdPessoa%>);
	<% } %>
}



var columnsContaReceber = [{label:'Nº Documento', reference: 'NR_DOCUMENTO'}, 
                           {label:'Emissão', reference: 'DT_EMISSAO', type: GridOne._DATE},							   
						   {label:'Vencimento', reference: 'DT_VENCIMENTO', type: GridOne._DATE},							   
						   {label:'Recebida em', reference: 'DT_RECEBIMENTO', type: GridOne._DATE},
  						   {label:'Valor conta', reference: 'VL_CONTA', type: GridOne._CURRENCY},
  						   {label:'Abatimento', reference: 'VL_ABATIMENTO', type: GridOne._CURRENCY},
						   {label:'Recebido', reference: 'VL_RECEBIDO', style: 'color:#0000FF; text-align:right;', type: GridOne._CURRENCY},
						   {label:'A Receber', reference: 'VL_ARECEBER', style: 'color:#FF0000; text-align:right;', type: GridOne._CURRENCY},
  					   	   {label:'Inst. Financeira', reference: 'CL_INST_FINANCEIRA'}, 
  					   	   {label:'Agência', reference: 'NR_AGENCIA'},
  					   	   {label:'Emissão', reference: 'CL_EMISSAO'},
  					   	   {label:'Circulação', reference: 'CL_CIRCULACAO'},
  					       {label:'Tipo do Emissor', reference: 'CL_TP_EMISSOR'},
  					       {label:'Nº CPF', reference: 'NR_CPF'},
  					       {label:'Nome Emissor', reference: 'NM_EMISSOR'},
  					       {label:'Quant. Dias', reference: 'QT_DIAS'},
  					   	   {label:'Juros (%)', reference: 'PR_JUROS'}];

function createGrid(content)	{
	if (content==null && $('cdPessoa').value != 0) {
		getPage("GET", "createGrid", 
				"../methodcaller?className=com.tivic.manager.adm.ContaFactoringServices"+
				"&method=getAllContasReceberOfPessoa(const " + $('cdPessoa').value + ":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')}catch(e) {}
		gridContasReceber = GridOne.create('gridContaReceber', {columns: columnsContaReceber,
															   resultset: rsm,
															   plotPlace: $('divGridContaReceber'),
	 													       columnSeparator: true,
														       lineSeparator: false,
															   strippedLines: true,
															   onProcessRegister: function(reg){
// 															   		reg["_SELECTED"] = 0;
// 															   		var dtVencimento = getAsDate(reg['DT_VENCIMENTO']);
// 																	var dataAtual = new Date();
<%-- 																	dataAtual = dataAtual.setTime(<%=hoje.getTimeInMillis()%>); --%>
// 																	var stConta = reg['ST_CONTA'];
// 																	if(stConta != null)	{
// 																		reg['CL_SITUACAO'] = situacaoContaReceber[stConta];
<%-- 																		if (parseInt(stConta, 10) == <%=ContaReceberServices.ST_EM_ABERTO%>) --%>
// 																			if (dtVencimento < dataAtual) {
// 																				reg['CL_SITUACAO'] = 'Vencida';
// 																				reg['ST_CONTA'] = 99;
// 																			}
// 																	}
// 																	reg['VL_ACRESCIMO']  = reg['VL_ACRESCIMO']==null ? 0 : reg['VL_ACRESCIMO'];
// 																	reg['VL_ABATIMENTO'] = reg['VL_ABATIMENTO']==null ? 0 : reg['VL_ABATIMENTO'];
// 																	reg['CL_VENCIMENTO'] = reg['DT_VENCIMENTO']==null ? null : reg['DT_VENCIMENTO'].split(' ')[0];
// 																    reg['VL_ARECEBER']   = reg['VL_CONTA'] + reg['VL_ACRESCIMO'] - reg['VL_ABATIMENTO'] - reg['VL_RECEBIDO'];
// 																    if(reg['VL_ARECEBER'] < 0)
// 																    	reg['VL_ARECEBER'] = 0;
// 																	switch(parseInt(reg['ST_CONTA'], 10)) {
<%-- 																   		case <%=ContaReceberServices.ST_RECEBIDA%>  : reg['CL_SITUACAO_cellStyle'] = 'color:#00FF00;'; break; --%>
<%-- 																   		case <%=ContaReceberServices.ST_PRORROGADA%>: reg['CL_SITUACAO_cellStyle'] = 'color:#FF9900;'; break; --%>
<%-- 																   		case <%=ContaReceberServices.ST_CANCELADA%> : reg['CL_SITUACAO_cellStyle'] = 'color:#0000FF;'; break; --%>
// 																   		case 99                                     : reg['CL_SITUACAO_cellStyle'] = 'color:#FF0000;'; break;
// 																    }
															   },		
															   noSelectOnCreate: true});//Mudei para tru
	}
}


function changeLayoutEnderecos(option) {
	if (option > 1) { 
		if ($('divGridDocumentos') != null)
			$('divGridDocumentos').style.height = gnPessoa==<%=PessoaServices.TP_JURIDICA%> ? '110px' : '68px';
		if (gridDocumentos != null)
			gridDocumentos.resize(parseInt($('divGridDocumentos').style.width, 10), parseInt($('divGridDocumentos').style.height, 10));
		if ($('divGridVinculos') != null)
			$('divGridVinculos').style.height = gnPessoa==<%=PessoaServices.TP_JURIDICA%> ? '110px' : '68px';
		if (gridVinculos != null)
			gridVinculos.resize(parseInt($('divGridVinculos').style.width, 10), parseInt($('divGridVinculos').style.height, 10));
		if ($('txtObservacao') != null)
			$('txtObservacao').style.height = gnPessoa==<%=PessoaServices.TP_JURIDICA%> ? '110px' : '68px';
		$('divGridEnderecosSuperior').style.display = '';
		$('divEnderecosSuperior').style.display = 'none';
	} 
	else {
		if ($('divGridDocumentos') != null)
			$('divGridDocumentos').style.height = gnPessoa==<%=PessoaServices.TP_JURIDICA%> ? '110px' : '80px';
		if (gridDocumentos != null)
			gridDocumentos.resize(parseInt($('divGridDocumentos').style.width, 10), parseInt($('divGridDocumentos').style.height, 10));
		if ($('divGridVinculos') != null)
			$('divGridVinculos').style.height = gnPessoa==<%=PessoaServices.TP_JURIDICA%> ? '110px' : '80px';
		if (gridVinculos != null)
			gridVinculos.resize(parseInt($('divGridVinculos').style.width, 10), parseInt($('divGridVinculos').style.height, 10));
		if ($('txtObservacao') != null)
			$('txtObservacao').style.height = gnPessoa==<%=PessoaServices.TP_JURIDICA%> ? '110px' : '80px';
		$('divGridEnderecosSuperior').style.display = 'none';
		$('divEnderecosSuperior').style.display = '';
	}
}

function btnSubstituirPessoaOnClick(reg) {
	if (reg==null) {
		if ($("cdPessoa").value<=0)
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 60, message: "Localize o registro a ser substituído antes de proceder com esta operação.", msgboxType: "INFO"});
		else
			btnFindPessoaOnClick(null, {callback: btnSubstituirPessoaOnClick, caption: 'Localizar registro para substituir o registro atual'});
	}
	else {
		closeWindow('jFiltro');
		btnConfirmSubstituirPessoaOnClick(null, {cdPessoa: reg[0]['CD_PESSOA']});
	}
}

function btnConfirmSubstituirPessoaOnClick(content, options) {
	if (content==null) {
		var cdPessoa = options!=null && options.cdPessoa!=null ? options.cdPessoa : 0;
		getPage("GET", "btnConfirmSubstituirPessoaOnClick", "../methodcaller?className=com.tivic.manager.grl.PessoaServices" +
												"&method=substituirPessoa(const " + $('cdPessoa').value + ":int, const " + cdPessoa + ":int)", null, true, null, null);
	}
	else {
		var result = processResult(content, null, {caption: 'Substituição de registro', noDetailButton: true, width: 350, height: 100});
		if (result!=null && result.code>0)
            clearFormPessoa();
	}

}

/**
 * Valida se ao menos um telefone foi indicado, necessário para gerar NFe.
 */
function validateTelefones() {
	var campos = ['nrTelefone1','nrTelefone2','nrCelular','nrFax'];
	return validaCamposTelefone(campos);
}

/**
 * Pega o array de id's de campos e valida se algum está preenchido.
 */
function validaCamposTelefone(campos) {
	for(var i = 0; i < campos.length; i++){
		var val = document.getElementById(campos[i]).value;
		if(!isNullOrBlank(val)){
			return true;
		}
	}
	return false; 
}

/**
 * Testa se algum valor é null, branco ou vazio
 */
function isNullOrBlank(val){
	return val == null || val == '' || val == undefined;
} 

function formValidationPessoa(){
	if(!validateTelefones()){
		alert("Ao menos um número de telefone válido deve ser cadastrado.")
		return false;
	}
	var campos = [];
    campos.push([$("nmPessoa"), 'Nome', VAL_CAMPO_NAO_PREENCHIDO]);
   	if ($('gnPessoa').value == 1)	{
	    campos.push([$("nrCpf"), 'CPF', VAL_CAMPO_CPF]);
	    <%=lgDocObrigatorio==1 ? "campos.push([$(\"nrCpf\"), \'CPF\', VAL_CAMPO_NAO_PREENCHIDO]);" : ""%>
	}
	else	{
		<%=lgDocObrigatorio==1 ? "campos.push([$(\"nrCnpj\"), \'CNPJ\', VAL_CAMPO_NAO_PREENCHIDO]);" : ""%>
	    campos.push([$("nrCnpj"), 'CNPJ', VAL_CAMPO_CNPJ]);
	}
   return validateFields(campos, true, 'Os campos marcados devem ser preenchidos corretamente!', 'nmPessoa');
}

function btnNewPessoaOnClick(){
    clearFormPessoa();
}


function btLoadImagemOnClick() {
	var idSession = 'imgFoto_' + $('cdPessoa').value;
	createWindow("jLoadFile", {caption:"Carregando imagem",
							   width: 410,
							   height: 90,
							   contentUrl: '../load_file.jsp?idSession=' + idSession + '&idContentLoad=imagePessoa&idField=imgFoto',
							   modal: true});
}

function btClearImagemOnClick() {
	$('imgFoto').value = null;
	$('imagePessoa').src = 'preview_imagem.jsp?lgDefaultInBlank=1';
}

function loadGrupos() {
	if (parent.$('cdGrupo') != null) {
		var j = 0;
		for (var i=0; i<parent.$('cdGrupo').childNodes.length; i++) {
			var childNode = parent.$('cdGrupo').childNodes[i];
			if (childNode.getAttribute != null && childNode.getAttribute("value") != 0) {
				var newOption = document.createElement("OPTION");
				newOption.setAttribute("value", childNode.getAttribute("value"));
				newOption.appendChild(document.createTextNode(childNode.innerHTML));
				$('cdGrupo').appendChild(newOption);
				if (j == 0)
					$('cdGrupo').setAttribute("defaultValue", childNode.getAttribute("value"));
				j++;
			}
		}
	}
}

function loadPessoa(content, cdPessoa){
	if (content == null) {
		getPage("GET", "loadPessoa", '../methodcaller?className=com.tivic.manager.grl.PessoaServices'+
										   '&method=getAsResultSet(const ' + cdPessoa + ':int)',
										   null, true, null, null);
	}
	else {
		var rsmPessoas = null;
		try { rsmPessoas = eval("(" + content + ")"); } catch(e) {}
		if (rsmPessoas!=null && rsmPessoas.lines && rsmPessoas.lines.length > 0)
			btnFindPessoaOnClick(rsmPessoas.lines);
	}
}
	
var gnPessoa = 0;
function onChangeGnPessoa() {
	gnPessoa = parseInt($('gnPessoa').value, 10);
	linesPessoaFisica   = [$('line4'), $('line5'), $('line6'), $('line21')];
	linesPessoaJuridica = [$('line8'), $('line9'), $('line20')];	
	// Ocultando/Exibindo linha da Pessoa Jurídica
	for (var i=0; i<linesPessoaJuridica.length; i++) {
		if (linesPessoaJuridica[i] != null) {
			linesPessoaJuridica[i].style.display = (gnPessoa==0 ? '' : 'none'); 
		}
	}
	// Ocultando/Exibindo linhas da Pessoa Física
	for (var i=0; i<linesPessoaFisica.length; i++) {
		if (linesPessoaFisica[i] != null) {
			linesPessoaFisica[i].style.display = (gnPessoa==1 ? '' : 'none');
		}
	}
	if ($('nmFormaTratamentoElement') != null)
		$('nmFormaTratamentoElement').style.display = gnPessoa==1 ? '' : 'none';
	if ($('nmPessoaElement') != null)
		$('nmPessoaElement').style.width = gnPessoa==1 ? '240px' : '300px';
	if ($('nmPessoa') != null)
		$('nmPessoa').style.width = gnPessoa==1 ? '237px' : '297px';
	if ($('divGridContaBancaria') != null)
		$('divGridContaBancaria').style.height = gnPessoa==<%=com.tivic.manager.grl.PessoaServices.TP_FISICA%> ? '101px' : '71px';
	if ($('divGridOcorrencias') != null)
		$('divGridOcorrencias').style.height = gnPessoa==<%=com.tivic.manager.grl.PessoaServices.TP_FISICA%> ? '101px' : '73px';
	if ($('cdCboPessoa') != null)
		$('cdCboPessoa').parentNode.style.display = gnPessoa==<%=PessoaServices.TP_FISICA%> ? '' : 'none';
	if ($('cdCnaePessoa') != null)
		$('cdCnaePessoa').parentNode.style.display = gnPessoa==<%=PessoaServices.TP_JURIDICA%> ? '' : 'none';
	if(gnPessoa==0)	{ // Pessoa Jurídica
		$('gnPessoa').nextElement  = $('nmPessoa');
		if ($('nmApelido') != null)
			$('nmApelido').nextElement = $('nmRazaoSocial');
		$('labelNmPessoa').innerHTML = 'Nome Fantasia';
		if ($('labelImagem') != null)
			$('labelImagem').innerHTML = 'Logomarca';
		if ($('divGridEnderecos') != null) {
			$('divGridEnderecos').style.height = '98px';
			if (gridEnderecos != null)
				gridEnderecos.resize(parseInt($('divGridEnderecos').style.width, 10), parseInt($('divGridEnderecos').style.height, 10));
		}
	}
	else	{ // Pessoa Física
		if ($('nmApelido') != null)
			$('nmApelido').nextElement = $('tpSexo');
		$('gnPessoa').nextElement = $('nmFormaTratamento')!= null ? $('nmFormaTratamento') : $('nmPessoa');
		if ($('tpCategoriaHabilitacao') != null)
			$('tpCategoriaHabilitacao').nextElement = $('btnPesquisarPais');
		$('labelNmPessoa').innerHTML = 'Nome Completo';
		if ($('labelImagem') != null)
			$('labelImagem').innerHTML = 'Foto';
		if ($('divGridEnderecos') != null) {
			$('divGridEnderecos').style.height = '70px';
			if (gridEnderecos != null)
				gridEnderecos.resize(parseInt($('divGridEnderecos').style.width, 10), parseInt($('divGridEnderecos').style.height, 10));
		}
	}
	changeLayoutEnderecos((gridEnderecos != null ? gridEnderecos.size() : 0));
	//muda o tamanho do campo situação
	if(gnPessoa == 0){
		$("situacao").style.width = '70px';
		$("stCadastro").style.width = '68px';
	} else {
		$("situacao").style.width = '87px';
		$("stCadastro").style.width = '85px';
	}
}

function clearFormPessoa(){
    $("dataOldPessoa").value = "";
    disabledFormPessoa = false;
    clearFields(pessoaFields, true);
	onChangeGnPessoa();
	clearFields(enderecoFields);
	clearFields(documentoFields);
	clearFields(contatoFields);
    alterFieldsStatus(true, pessoaFields, "gnPessoa");
	loadDocumentos();
	loadEnderecos();
	clearFields(ocorrenciaFields);
	if ($('imagePessoa') != null) {
		$('imagePessoa').src = 'preview_imagem.jsp?lgDefaultInBlank=1';
	}
		
	<% if (cdVinculo <= 0) { %>
		loadVinculos();
	<% } %>
	<% if (showArquivos) { %>
		loadArquivos();
	<% } %>

	loadOcorrencias();
	loadCnaes();
	loadCbos();
	loadContatos();
	loadContaBancaria();
	if ($('gnPessoa') != null && !$('gnPessoa').disabled)
		$('gnPessoa').focus();
	else if ($('nmPessoa') != null && !$('nmPessoa').disabled)
		$('nmPessoa').focus();
	<% if (showFactoring) { %>
	createGrid();
	<% } %>
}

function btnNewPessoaOnClick(){
    clearFormPessoa();
}

function btnAlterPessoaOnClick()	{
    disabledFormPessoa = false;
    alterFieldsStatus(true, pessoaFields, "gnPessoa");
	//$('gnPessoa').disabled = $('cdPessoa').value > 0;
	if($('cdEmpresa').value <= 0)	{
		$('cdEmpresa').value = <%=cdEmpresa%>;
		if($('nmEmpresa'))
			$('nmEmpresa').value = '<%=empresa != null ? empresa.getNmPessoa() : ""%>';
	}
	if($('tpProventoPrincipal'))
		tpProventoPrincipalOnChange($('tpProventoPrincipal').value);
}

/*********************** SALVAR PESSOA ********************************/
function btnSavePessoaOnClick(content){
	if(content==null){
		if (disabledFormPessoa){
			createMsgbox("jMsg", {width: 250,
								  height: 100,
								  message: "Para atualizar os dados, coloque o registro em modo de edição.",
								  msgboxType: "INFO"});
		}
		else if (formValidationPessoa()) {
			var className = "com.tivic.manager.grl.PessoaServices";
			var gnPessoa = parseInt($('gnPessoa').value, 10);
			var executionDescription = $("cdPessoa").value > 0 ? formatDescriptionUpdate("Pessoa", $("cdPessoa").value, $("dataOldPessoa").value, pessoaFields) : formatDescriptionInsert("Pessoa", pessoaFields);
			var constructorPessoa         = "cdPessoa: int, cdPessoaSuperior: int, cdPais: int, nmPessoa: String, nrTelefone1: String, nrTelefone2: String, nrCelular: String, nrFax: String, nmEmail: String, dtCadastro: GregorianCalendar, gnPessoa: int, *imgFoto: byte[], stCadastro: int, nmUrl: String, nmApelido: String, txtObservacao: String, lgNotificacao: int, idPessoa: String, cdClassificacao: int, cdFormaDivulgacao:int, dtChegadaPais: GregorianCalendar";
			var constructorPessoaJuridica = "nrCnpj: String, nmRazaoSocial: String, nrInscricaoEstadual: String, nrInscricaoMunicipal: String, nrFuncionarios: int, dtInicioAtividade: GregorianCalendar, cdNaturezaJuridica:int, tpEmpresa : int, dtTerminoAtividade: GregorianCalendar";
			var constructorPessoaFisica   = "cdNaturalidade: int, cdEscolaridade: int, dtNascimento: GregorianCalendar, nrCpf: String, sgOrgaoRg: String, nmMae: String, nmPai: String, tpSexo: int, stEstadoCivil: int, nrRg: String, nrCnh: String, dtValidadeCnh: GregorianCalendar, dtPrimeiraHabilitacao: GregorianCalendar, tpCategoriaHabilitacao: int, tpRaca:int, lgDeficienteFisico : int, nmFormaTratamento : String, cdEstadoRg: int, dtEmissaoRg: GregorianCalendar, *blbFingerprint: byte[]";
			var constructorEndereco       = 'cdEnderecoPessoa: int, cdPessoa: int, dsEnderecoPrincipal: String, cdTipoLogradouroPessoa: int, cdTipoEnderecoPrincipal: int, cdLogradouroPessoa: int, cdBairroPessoa: int, cdCidadePessoa: int, nmLogradouroPessoa: String, nmBairroPessoa: String, nrCepPessoa: String, nrEnderecoPessoa' + ($('tpEnderecamento').value==<%=PessoaEnderecoServices.TP_DIGITAVEL%> ? 2 : 1) + ': String, nmComplementoPessoa' + ($('tpEnderecamento').value==<%=PessoaEnderecoServices.TP_DIGITAVEL%> ? 2 : 1) + ': String, nrTelefonePessoa: String, nmPontoReferenciaPessoa: String, lgCobrancaPessoa: int, lgEnderecoPrincipal: int';
			if ($('lgEnderecoPrincipal') != null)
				$('lgEnderecoPrincipal').value = 1;
			var objects = 'imgFoto=byte[]; '+
						  'blbFingerprint=byte[];';
			var commandExecute = 'imgFoto=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const imgFoto_' + $('cdPessoa').value + ':String);'+
								 'blbFingerprint=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const blbFingerprint_' + $('cdPessoa').value + ':String);';
			if($('imgFoto').value == '') {
				var commandExecute = 'imgFoto=null;';
			}
			else {
				var commandExecute = 'imgFoto=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const imgFoto_' + $('cdPessoa').value + ':String);';
			}
			objects += 'atributos=java.util.ArrayList()';
			for (var i = 0; i < atributos.length; i++) {
				var atributo = atributos[i];
				var cdFormularioAtributo = atributo['cdFormularioAtributo'];
				var tpDado = atributo['tpDado'];
				var cdEmpresa = 0;
				var cdOpcao = tpDado == <%=FormularioAtributoServices.TP_OPCOES%> ? $('atributo_' + cdFormularioAtributo).value : 0;
				var cdDocumento = 0;
				var cdPessoa = tpDado == <%=FormularioAtributoServices.TP_PESSOA%> ? $('atributo_' + cdFormularioAtributo).value : 0;
				objects += ';atributo' + i + '=com.tivic.manager.grl.FormularioAtributoValor(const ' + cdFormularioAtributo + ':int, const 0:int, cdProdutoServico:int, const ' + cdOpcao + ':int, const ' + cdEmpresa + ':int, const ' + cdDocumento + ':int, ' + (tpDado==<%=FormularioAtributoServices.TP_OPCOES%> || tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? 'const ' : ('atributo_' + cdFormularioAtributo)) + ':String, const ' + $('cdPessoa').value + ':int, const ' + cdPessoa + ':int, const 0:int)';
				commandExecute += 'atributos.add(*atributo' + i + ':java.lang.Object);';
			}

			if($('dtCadastro').value=='')
				$('dtCadastro').value = formatDateTime(new Date());
			var methodName = "(new com.tivic.manager.grl.Pessoa";
			methodName += (gnPessoa == 0 ? "Juridica" : "Fisica");
			methodName += "(" + constructorPessoa + ", ";
			methodName += (gnPessoa == 0 ? constructorPessoaJuridica : constructorPessoaFisica)+"):com.tivic.manager.grl.Pessoa";
			methodName += (gridEnderecos.size() <= 1 ? ", new com.tivic.manager.grl.PessoaEndereco(" + constructorEndereco + "):com.tivic.manager.grl.PessoaEndereco" : ", const null:com.tivic.manager.grl.PessoaEndereco");
			methodName += ", cdEmpresa:int, cdVinculo:int"+($('cdPessoa').value>0?',cdVinculoOld:int':'');
			methodName += (cdFormularioDefault > 0 ? ", *atributos:java.util.ArrayList" : "");
			
			methodName += ")";
			
			if($("cdPessoa").value > 0)
				getPage("POST", "btnSavePessoaOnClick", "../methodcaller?className=" +className+
														"&execute=" + commandExecute +
														"&objects=" + objects +
														"&method=update" + methodName, pessoaFields, true, null, executionDescription);
			else
				getPage("POST", "btnSavePessoaOnClick", "../methodcaller?className=" +className+
														"&execute=" + commandExecute +
														"&objects=" + objects +
														"&method=insert" + methodName, pessoaFields, true, null, executionDescription);
		}
	}
	else {
		if ($("cdVinculo")!=null)
			$("cdVinculoOld").value = $("cdVinculo").value;
		var result = processResult(content, 'Dados salvos com sucesso!');
		var ok = false;
		
		var pessoa      = result.objects['pessoa'];
		var endereco    = result.objects['endereco'];
		var cdPessoa    = $('cdPessoa').value>0 ? $('cdPessoa').value : pessoa['cdPessoa'];
		var cdEndereco  = endereco==null ? 0 : endereco['cdEndereco'];
		if (endereco != null && cdEndereco > 0 && $('cdEnderecoPessoa')!=null)
			$('cdEnderecoPessoa').value = cdEndereco;
		var isInsertPessoa = $("cdPessoa").value <= 0 ? true : false;
		$("cdPessoa").value = $("cdPessoa").value <= 0 ? cdPessoa : $("cdPessoa").value;

		if (result.code > 0) {
			disabledFormPessoa=true;
			alterFieldsStatus(false, pessoaFields, "gnPessoa", "disabledField");
			createTempbox("jMsg", {width: 200, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
			$("dataOldPessoa").value = captureValuesOfFields(pessoaFields);
		}
	}
}

var filterWindow;
function btnFindPessoaOnClick(reg, options){
    if(!reg){
        var filterFields = [[{label:"Nome", reference:"A.NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'},
                             {label:"Razão Social", reference:"F.NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'}],
							[{label:"Apelido", reference:"NM_APELIDO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:25, charcase:'uppercase'},
							 {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'},
							 {label:"CNPJ", reference:"F.NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'},
							 {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'}]];

        var columnsGrid = [];
		columnsGrid.push({label:"Nome", reference:"NM_PESSOA"});
		columnsGrid.push({label:"ID", reference:"ID_PESSOA"});
    	columnsGrid.push({label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'});
		columnsGrid.push({label:"Cidade", reference:"NM_CIDADE"});
		columnsGrid.push({label:"Razão Social", reference:"F.NM_RAZAO_SOCIAL"});
		columnsGrid.push({label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ});
		columnsGrid.push({label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"});
		columnsGrid.push({label:'CPF', reference:'NR_CPF', type:GridOne._CPF});
		columnsGrid.push({label:"Identidade", reference:"NR_RG"});
		columnsGrid.push({label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE});
		var hiddenFields = [{reference:"qtLimite", value:50, comparator:_EQUAL, datatype:_INTEGER}];
		hiddenFields.push({reference:"cd_empresa", value:<%=cdEmpresa%>, comparator:_EQUAL, datatype:_INTEGER});
		<% if (cdVinculo > 0) { %>
    	    hiddenFields.push({reference:"J.CD_EMPRESA", value:<%=cdEmpresa%>, comparator:_EQUAL, datatype:_INTEGER});
    	    hiddenFields.push({reference:"J.CD_VINCULO", value:<%=cdVinculo%>, comparator:_EQUAL, datatype:_INTEGER});	
		<% } %>
		
    	hiddenFields.push({reference:"findEnderecoPrincipal", value:0, comparator:_EQUAL, datatype:_INTEGER});
    	hiddenFields.push({reference:"P.LG_PRINCIPAL", value:1, comparator:_EQUAL, datatype:_INTEGER});
    	hiddenFields.push({reference:"findCliente", value:true, comparator:_EQUAL, datatype:_BOOLEAN});
		var callback = options!=null && options.callback ? options.callback : btnFindPessoaOnClick;
		var caption = options!=null && options.caption ? options.caption : 'Pesquisar Pessoas';
		FilterOne.create("jFiltro", {caption:caption, width: 600, height: 340, modal: true, noDrag: true,
									 className: 'com.tivic.manager.grl.PessoaServices', method: "find",
									   filterFields: filterFields,
									   gridOptions: {columns: columnsGrid, strippedLines: true, columnSeparator: false, lineSeparator: false,
										             onProcessRegister: function(reg){
										            	 reg['NM_CIDADE'] = reg['NM_CIDADE']+'-'+reg['SG_ESTADO'];
										             }},
									   hiddenFields: hiddenFields,
									   callback: callback, 
									   autoExecuteOnEnter: true});
    }
    else {// retorno
		clearFields(pessoaFields);
		clearFields(enderecoFields);
		clearFields(documentoFields);
		closeWindow("jFiltro");
        disabledFormPessoa=true;
		loadFormRegister(pessoaFields, reg[0]);
        alterFieldsStatus(false, pessoaFields, "gnPessoa", "disabledField");
        $("dataOldPessoa").value = captureValuesOfFields(pessoaFields);
		onChangeGnPessoa();
		setTimeout('loadDocumentos()', 10);
		if($('gnPessoa').value==0) // Pessoa Jurídica
			setTimeout('loadContatos()', 10);
		$('imagePessoa').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.PessoaServices&method=getBytesImage(const ' + reg[0]['CD_PESSOA'] + ':int)&idSession=imgFoto_' + reg[0]['CD_PESSOA'];
		$("cdEmpresa").value =  $("cdEmpresa").getAttribute("defaultValue");
		if(reg[0]['CD_NATUREZA_JURIDICA'] > 0)
			$('nmNaturezaJuridica').value = reg[0]['ID_NATUREZA_JURIDICA']+' - '+reg[0]['NM_NATUREZA_JURIDICA'];
		if($('nrContaPagamento') && reg[0]['NR_CONTA']!=null)
			$('nrContaPagamento').value = reg[0]['NR_CONTA']+'-'+reg[0]['NR_DV'];
		setTimeout('loadVinculoOfPessoa()', 10);
		<% if (cdVinculo <= 0) { %>
			setTimeout('loadVinculos()', 10);
		<% } %>
		<% if (showArquivos) { %>
			setTimeout('loadArquivos()', 10);
		<% } %>
		<% if (showFactoring) { %>
		$('vlLimiteFactoring').value = reg[0]['VL_LIMITE_FACTORING_TRABALHO'];
		$('vlLimiteFactoringEmissor').value = reg[0]['VL_LIMITE_FACTORING_EMISSOR_TRABALHO'];
		$('vlLimiteFactoringUnitario').value = reg[0]['VL_LIMITE_FACTORING_UNITARIO_TRABALHO'];
		$('qtPrazoMinimoFactoring').value = reg[0]['QT_PRAZO_MINIMO_FACTORING_TRABALHO'];
		$('qtPrazoMaximoFactoring').value = reg[0]['QT_PRAZO_MAXIMO_FACTORING_TRABALHO'];
		$('qtIdadeMinimaFactoring').value = reg[0]['QT_IDADE_MINIMA_FACTORING_TRABALHO'];
		$('vlGanhoMinimoFactoring').value = reg[0]['VL_GANHO_MINIMO_FACTORING_TRABALHO'];
		$('prTaxaMinimaFactoring').value = reg[0]['PR_TAXA_MINIMA_FACTORING_TRABALHO'];
		<% } %>
		setTimeout('loadAtributosPessoa()', 1);
		setTimeout('loadOcorrencias()', 10);
		setTimeout('loadCnaes()', 10);
		setTimeout('loadCbos()', 10);
		setTimeout('loadContaBancaria()', 10);
		setTimeout('loadEnderecos()', 10);
		/* VINCULOS */
		<%if(showDependentes)	{%>
			setTimeout('loadDependentes()', 10);
		<% }%>	
		<% if (showFactoring) { %>
		createGrid();
		<% } %>
    }
}

function btnDeletePessoaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Pessoa", $("cdPessoa").value, $("dataOldPessoa").value);
    getPage("GET", "btnDeletePessoaOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
            "&method=delete(const "+$("cdPessoa").value+":int):int", null, true, null, executionDescription);
}

function btnDeletePessoaOnClick(content){
    if(content==null){
        if ($("cdPessoa").value == 0)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 50, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeletePessoaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content, 10)==1){
            createTempbox("jTemp", {width: 200, 
									height: 50, 
									message: "Registro excluído com sucesso!", 
									time: 3000});
            clearFormPessoa();
        }
        else {
        	createConfirmbox("dialog", {caption: "Inativação de pessoa",
	            width: 300, 
	            height: 130, 
	            message: "A exclusão deste registro está impossibilitado pelo fato de ele está sendo usado no lançamento de outras informações no Sistema, como Contas a Receber e a Pagar, Contratos, Usuários, entre outras. <br />Você gostaria de inativar este registro?",
	            boxType: "QUESTION",
	            positiveAction: function() {setTimeout("btnInativarPessoaOnClick()", 10)}});
        }
    }	
}

function btnInativarPessoaOnClick(content) {
	if(content == null){
		getPage("POST", "btnInativarPessoaOnClick", "../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
    	        "&method=inativarPessoa(const " + $("cdPessoa").value + " :int):int", null, true, null);
	} else{
		var ret = processResult(content, '');
		if(ret.code>0)	{
			createTempbox("jTemp", {width: 270, height: 80, boxType: "INFO", message: ret.message, time: 5000});
		}
	}
}


function btnFindPaisOnClick(reg){
    if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Países", 
												   width: 450,
												   height: 225,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.PaisDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_pais", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_pais"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindPaisOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
		$('cdPais').value = reg[0]['CD_PAIS'];
		$('cdPaisView').value = reg[0]['NM_PAIS'];
    }
}

function btnClearPaisOnClick(content){
	$('cdPaisView').value = '';
	$('cdPais').value = '0';
}

function btnFindNaturezaJuridicaOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Naturezas Jurídicas", 
												   width: 450,
												   height: 225,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.NaturezaJuridicaDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Código", reference:"id_natureza_juridica", datatype:_VARCHAR, comparator:_LIKE_ANY, width:25, charcase:'uppercase'},
												   				   {label:"Natureza Jurídica", reference:"nm_natureza_juridica", datatype:_VARCHAR, comparator:_LIKE_ANY, width:75, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Código", reference:"id_natureza_juridica"},
												   						   {label:"Natureza Jurídica", reference:"nm_natureza_juridica"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindNaturezaJuridicaOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
		$('cdNaturezaJuridica').value = reg[0]['CD_NATUREZA_JURIDICA'];
		$('nmNaturezaJuridica').value = reg[0]['ID_NATUREZA_JURIDICA']+' - '+reg[0]['NM_NATUREZA_JURIDICA'];
    }
}

function btnFindContaBancariaOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Contas Bancárias",
												 	width: 450, 
													height: 280,
												 	modal: true, 
													allowFindAll: true, 
													noDrag: true,
												 	// Parametros do filtro
												 	className: "com.tivic.manager.grl.PessoaContaBancariaServices",
												 	method: "find",
												 	filterFields: [[{label:"Nº Conta (sem dígito)",reference:"NR_CONTA",datatype:_VARCHAR,comparator:_EQUAL,width:30},
																	{label:"Agência",reference:"NR_AGENCIA",datatype:_VARCHAR,comparator:_EQUAL,width:15},
																	{label:"Titular",reference:"NM_TITULAR",datatype:_VARCHAR,comparator:_LIKE_ANY,width:55}]],
												 	gridOptions:{columns:[{label:"Nº Conta",reference:"CL_CONTA"}, 
																	      {label:"Nº Agência",reference:"NR_AGENCIA"},
																		  {label:"Banco",reference:"NM_BANCO"},
																	   	  {label:"Titular",reference:"NM_TITULAR"}],
												    onProcessRegister: function(reg)	{
																reg['CL_CONTA'] = reg['NR_CONTA']+(reg['NR_DV']!=null?'-'+reg['NR_DV']:'');
													}},
												 	hiddenFields: [{reference:"A.cd_pessoa", value:$("cdPessoa").value, comparator:_EQUAL, datatype:_INTEGER}],
												 	callback: btnFindContaBancariaOnClick, 
													autoExecuteOnEnter: true
									  });
    }
    else {// retorno
        filterWindow.close();
		$('nmBancoPagamento').value   = reg[0]['NM_BANCO'];
		$('nrAgenciaPagamento').value = reg[0]['NR_AGENCIA'];
		$('nrContaPagamento').value = reg[0]['NR_CONTA']+'-'+reg[0]['NR_DV'];
		$('cdContaPagamento').value = reg[0]['CD_CONTA_BANCARIA'];
    }
}

function btnClearContaBancariaOnClick()	{
	$('cdContaPagamento').value=0; 
	$('nmBancoPagamento').value=''; 
	$('nrAgenciaPagamento').value='';	
	$('nrContaPagamento').value='';	
}

function btnFindSetorOnClick(reg){
    if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Setores e Departamentos", 
												   width: 450,
												   height: 225,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.SetorDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Setor", reference:"nm_setor", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60, charcase:'uppercase'},
												   				   {label:"Nº Setor", reference:"nr_setor", datatype:_VARCHAR, comparator:_EQUAL, width:40, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_setor"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference:"cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindSetorOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
		$('cdSetor').value = reg[0]['CD_SETOR'];
		$('nmSetor').value = reg[0]['NM_SETOR'];
    }
}

function loadVinculoOfPessoa(content){
	if (content==null) {
		if ($('cdEmpresa').value != 0)
			getPage("GET", "loadVinculoOfPessoa", 
					"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
					"&method=getVinculoOfPessoa(const " + $('cdPessoa').value + ":int, const " + $('cdEmpresa').value + ":int)", null, true);
	}
	else {
		var vinculo = null;
		try {vinculo = eval("(" + content + ")")} catch(e) {}
		if ($('cdVinculo') != null && vinculo!=null) {
			$('cdVinculoOld').value = vinculo['cdVinculo'];
			$('cdVinculo').value = vinculo['cdVinculo'];
		}
        $("dataOldPessoa").value = captureValuesOfFields(pessoaFields);
	}
}

function btnFindCdNaturalidadeOnClick(reg){
    if(!reg){
    	alert(translate("NM_CIDADE"));
        FilterOne.create("jFiltro", {caption:"Pesquisar Cidades", width: 350, height: 225, modal: true, noDrag: true,
												   className: "com.tivic.manager.grl.CidadeServices",
												   method: "find(*crt:java.util.ArrayList, const 0:int)",
												   allowFindAll: true,
												   filterFields: [[{label:"Cidade", reference: translate("NM_CIDADE"), datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'},
												   				   {label:"UF", reference:"SG_ESTADO", datatype:_VARCHAR, comparator:_EQUAL, width:10, charcase:'uppercase'},
																   {label:"ID", reference:"ID_CIDADE", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Cidade", reference:"NM_CIDADE"},
												                           {label:"UF", reference:"SG_ESTADO"},												                           
																		   {label:"Cód. IBGE", reference:"ID_IBGE"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindCdNaturalidadeOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        closeWindow('jFiltro');
		$('cdNaturalidade').value = reg[0]['CD_CIDADE'];
		$('cdNaturalidadeView').value = reg[0]['NM_CIDADE'];
    }
}

function btnClearCdNaturalidadeOnClick(content){
	$('cdNaturalidadeView').value = '';
	$('cdNaturalidade').value     = '0';
}

function btnPrintPessoaOnClick(){
	parent.miRelatorioPessoaOnClick();
}

function btnFingerprintOnClick(){
    if ($('cdPessoa').value == '0'){
		createTempbox('jMsg', {width: 250, height: 50, message: 'Inclua ou localize uma pessoa para capturar a impressão digital.', boxType: 'ALERT', time: 2000});
	}
	else {
		createWindow('jFingerprint', {caption: "Impressão Digital",
									  width: 306,
									  height: 398,
									  modal: true,
									  contentUrl: 'leitor_biometrico.jsp?cdPessoa='+$('cdPessoa').value});
	}
}

/*********************************               V-NCULOS              *****************************/
var columnsVinculo = [{label:'Nome', reference:'NM_VINCULO'}];
function loadVinculos(content) {
	if (content == null && $('cdPessoa').value != 0) {
		getPage("GET", "loadVinculos", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
				"&method=getAllVinculosOfPessoa(const " + $('cdPessoa').value + ":int, const " + $('cdEmpresa').value + ":int)", null, true);
	}
	else {
		var rsmVinculos = null;
		try {rsmVinculos = eval('(' + content + ')')} catch(e) {}
		gridVinculos = GridOne.create('gridVinculos', {columns: columnsVinculo,
					     resultset :rsmVinculos, 
					     plotPlace : $('divGridVinculos'),
					     onSelect : onClickVinculo});
	}
}

function validateVinculo(content) {
    if($("cdVinculo").value == '0') {
		showMsgbox('Manager', 300, 50, 'Indique o Vínculo.');
        return false;
	}
	else
		return true;
}

function btnSaveVinculoOnClick(content) {
	if (content==null) {
		if ($('cdPessoa').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma pessoa para informar vínculos.');
		else if (validateVinculo()) {
			var pessoaDescription = ' (Pessoa ' + $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value + ")";
            var executionDescription = $('cdVinculoOld').value>0 ? formatDescriptionUpdate("Vinculo", $("cdVinculoOld").value + " " + pessoaDescription, $("dataOldVinculo").value, vinculoFields) : formatDescriptionInsert("Vinculo " + pessoaDescription, vinculoFields);
			var construtor = 'cdEmpresa:int, cdPessoa:int, cdVinculo:int, dtVinculo:GregorianCalendar, stVinculo:int';
			var methodName = $('cdVinculoOld').value <= 0 ? 
							'insert(new com.tivic.manager.grl.PessoaEmpresa(' + construtor + '):com.tivic.manager.grl.PessoaEmpresa)' :
							'update(new com.tivic.manager.grl.PessoaEmpresa(' + construtor + '):com.tivic.manager.grl.PessoaEmpresa, cdEmpresa:int, cdPessoa:int, cdVinculoOld:int)';
			getPage('POST', 'btnSaveVinculoOnClick', 
					'../methodcaller?className=com.tivic.manager.grl.PessoaEmpresaDAO'+
					'&method=' + methodName +
					'&cdPessoa=' + $('cdPessoa').value +
					'&cdEmpresa=<%=cdEmpresa%>'+
					'&cdVinculoOld=' + $('cdVinculoOld').value,
					vinculoFields, true, null, executionDescription);
		}
	}
	else {
		closeWindow('jVinculo');
		if (isInteger(content) && parseInt(content, 10) > 0) {
			var vinculo = {'CD_VINCULO' : $('cdVinculo').value,
							 'CD_TABELA_COMISSAO' : $('cdTabelaComissao').value,
							 'ST_VINCULO' : $('stVinculo').value,
							 'DT_VINCULO' : $('dtVinculo').value.toUpperCase(),
							 'NM_VINCULO' : $('cdVinculo').options[$('cdVinculo').selectedIndex].text};
			if ($('cdVinculoOld').value == '0') {
				gridVinculos.addLine(0, vinculo, onClickVinculo, true);
			}
			else {
				if (gridVinculos.getSelectedRow()) {
					gridVinculos.getSelectedRow().register = vinculo;
					gridVinculos.changeCellValue(gridVinculos.getSelectedRow().rowIndex, 1, vinculo['NM_VINCULO']);
				}
			}
			$('cdVinculoOld').value = $('cdVinculo').value;
			$("dataOldVinculo").value = captureValuesOfFields(vinculoFields);
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao incluir ou alterar Vínculo.');
	}
}

function onClickVinculo() {
	if (this!=null) {
		var vinculo = this.register;
		$('cdVinculo').value = vinculo['CD_VINCULO'];
		$('cdVinculoOld').value = vinculo['CD_VINCULO'];
		$('cdTabelaComissao').value = vinculo['CD_TABELA_COMISSAO'];
		$('dtVinculo').value = vinculo['DT_VINCULO'];
		$('stVinculo').value = vinculo['ST_VINCULO'];
		$("dataOldVinculo").value = captureValuesOfFields(vinculoFields);
	}
}

function btnDeleteVinculoOnClick(content)	{
	if(content==null) {
		if (gridVinculos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o vínculo que deseja excluir.');
		else {
			var pessoaDescription = ' (Pessoa ' + $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value + ")";
		    var executionDescription = formatDescriptionDelete("Vinculo " + pessoaDescription, $("cdVinculoOld").value, $("dataOldVinculo").value);
			var cdVinculo = gridVinculos.getSelectedRow().register['CD_VINCULO'];
			var cdPessoa = $('cdPessoa').value;
			var cdEmpresa = $('cdEmpresa').value;
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o vínculo selecionado?', 
							function() {
								getPage('GET', 'btnDeleteVinculoOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.PessoaEmpresaDAO'+
										'&method=delete(const ' + cdEmpresa + ':int, const ' + cdPessoa + ':int, const ' + cdVinculo + ':int):int', null, true, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridVinculos.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir Vínculo.');
	}
}

function btnAlterVinculoOnClick() {
	if (gridVinculos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o vínculo que você deseja alterar.');
	else {
		createWindow('jVinculo', {caption: "Vínculo",
									  width: 399,
									  height: 60,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'vinculoPanel'});
		document.disabledTab = true;
		try{$('cdVinculo').focus();}catch(e){;}
	}
}

function btnNewVinculoOnClick(){
    if ($('cdPessoa').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma pessoa para informar os Vínculos.');
	else {
		gridVinculos.unselectGrid();
		$('cdVinculoOld').value = 0;
		$("dataOldVinculo").value = "";
		clearFields(vinculoFields);
		createWindow('jVinculo', {caption: "Vínculo",
									  width: 399,
									  height: 60,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'vinculoPanel'});
		document.disabledTab = true;
		$('cdVinculo').value = $('cdVinculo').getAttribute("defaultValue");
		try{$('cdVinculo').focus();}catch(e){;}
	}
}


/*********************************                    DOCUMENTOS                      ******************************/
var columnsDocumentos = [{label:'Documento', reference:'NM_TIPO_DOCUMENTO'}, {label:'N¦ Documento', reference:'NR_DOCUMENTO'}];
function validateTipoDocumento(content) {
    if($("cdTipoDocumento").value == '0') {
		showMsgbox('Manager', 300, 50, 'Informe o Tipo de Documento.', function() {$("cdTipoDocumento").focus()});
        return false;
	}
    else if(!validarCampo($("nrDocumento"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'N¦ Documento' deve ser preenchido.", true, null, null))
        return false;
    else
		return true;
}

function btnSaveTipoDocumentoOnClick(content) {
	if (content==null) {
		if ($('cdPessoa').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma pessoa para lançar documentos.');
		else if (validateTipoDocumento()) {
			var pessoaDescription = ' (Pessoa ' + $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value + ")";
            var executionDescription = $('cdTipoDocumentoOld').value>0 ? formatDescriptionUpdate("Documento", $("cdTipoDocumentoOld").value + " " + pessoaDescription, $("dataOldDocumento").value, documentoFields) : formatDescriptionInsert("Documento " + pessoaDescription, documentoFields);
			var construtor = 'cdTipoDocumento:int, cdPessoa:int, nrDocumento:String';
			var methodName = $('cdTipoDocumentoOld').value == '0' ? 
							'insert(new com.tivic.manager.grl.TipoDocumentoPessoa(' + construtor + '):com.tivic.manager.grl.TipoDocumentoPessoa)' :
							'update(new com.tivic.manager.grl.TipoDocumentoPessoa(' + construtor + '):com.tivic.manager.grl.TipoDocumentoPessoa, cdTipoDocumentoOld:int, cdPessoa:int)';
			getPage('POST', 'btnSaveTipoDocumentoOnClick', 
					'../methodcaller?className=com.tivic.manager.grl.TipoDocumentoPessoaServices'+
					'&method=' + methodName +
					'&cdPessoa=' + $('cdPessoa').value,
					documentoFields, true, null, executionDescription);
		}
	}
	else {
		closeWindow('jTipoDocumento');
		if (isInteger(content) && parseInt(content, 10) > 0) {
			var documento = {'CD_PESSOA' : $('cdPessoa').value,
							 'CD_TIPO_DOCUMENTO' : $('cdTipoDocumento').value,
							 'NR_DOCUMENTO' : $('nrDocumento').value.toUpperCase(),
							 'NM_TIPO_DOCUMENTO' : $('cdTipoDocumento').options[$('cdTipoDocumento').selectedIndex].text};
			if ($('cdTipoDocumentoOld').value == '0') {
				gridDocumentos.addLine(0, documento, onClickTipoDocumento, true);
			}
			else {
				if (gridDocumentos.getSelectedRow()) {
					gridDocumentos.getSelectedRow().register = documento;
					gridDocumentos.updateSelectedRow();
				}
			}
			$('cdTipoDocumentoOld').value = $('cdTipoDocumento').value;
			$("dataOldDocumento").value = captureValuesOfFields(documentoFields);
		}
		else {
			var dsErro = "Erros reportados ao incluir ou alterar Documento.";
			switch(parseInt(content, 10)) {
				case <%=TipoDocumentoPessoaServices.ERR_TIPO_DOC_CADASTRADO%>:
					dsErro = "O tipo de documento indicado já foi informado para este cadastro.";
					break;
			}
			showMsgbox('Manager', 300, 50, dsErro);
		}
	}
}

function onClickTipoDocumento() {
	if (this!=null) {
		var documento = this.register;
		$('cdTipoDocumento').value = documento['CD_TIPO_DOCUMENTO'];
		$('cdTipoDocumentoOld').value = documento['CD_TIPO_DOCUMENTO'];
		$('nrDocumento').value = documento['NR_DOCUMENTO'];
		$('cdTipoDocumentoOld').value = $('cdTipoDocumento').value;
		$("dataOldDocumento").value = captureValuesOfFields(documentoFields);
	}
}

function loadDocumentos(content) {
	if (content == null && $('cdPessoa').value != 0) {
		getPage("GET", "loadDocumentos", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
				"&method=getAllDocumentosOfPessoa(const " + $('cdPessoa').value + ":int)", null, true);
	}
	else {
		var rsmDocumentos = null;
		try {rsmDocumentos = eval('(' + content + ')')} catch(e) {}
		gridDocumentos = GridOne.create('gridDocumentos', {columns: columnsDocumentos,
					     resultset: rsmDocumentos, 
					     plotPlace: $('divGridDocumentos'),
					     onSelect: onClickTipoDocumento});
	}
}

function btnDeleteTipoDocumentoOnClick(content)	{
	if(content==null) {
		if (gridDocumentos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o documento que deseja excluir.');
		else {
			var pessoaDescription = ' (Pessoa ' + $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value + ")";
		    var executionDescription = formatDescriptionDelete("Documento " + pessoaDescription, $("cdTipoDocumentoOld").value, $("dataOldDocumento").value);
			var cdTipoDocumento = gridDocumentos.getSelectedRow().register['CD_TIPO_DOCUMENTO'];
			var cdPessoa = $('cdPessoa').value;
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o documento selecionado?', 
							function() {
								getPage('GET', 'btnDeleteTipoDocumentoOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.TipoDocumentoPessoaDAO'+
										'&method=delete(const ' + cdTipoDocumento + ':int, const ' + cdPessoa + ':int):int', null, true, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			clearFields(documentoFields);
			gridDocumentos.removeSelectedRow();
			if (gridDocumentos.size() == 0)
				alterFieldsStatus(true, documentoFields);
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir Documento.');
	}
}

function btnAlterTipoDocumentoOnClick() {
	if (gridDocumentos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o documento que você deseja alterar.');
	else {
		createWindow('jTipoDocumento', {caption: "Documento",
								  width: 399,
								  height: 60,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'tipoDocumentoPanel'});
		$('cdTipoDocumento').focus();
	}
}

function btnNewTipoDocumentoOnClick(){
    if ($('cdPessoa').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma pessoa para lançar documentos.');
	else {
		gridDocumentos.unselectGrid();
		$("dataOldDocumento").value = "";
		clearFields(documentoFields);
		alterFieldsStatus(true, documentoFields, null);
		createWindow('jTipoDocumento', {caption: "Documento",
									  width: 399,
									  height: 60,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'tipoDocumentoPanel'});
		$('cdTipoDocumento').focus();
	}
}

/************************                 ENDEREçOS               ****************************/
var columnsEndereco = [{label:'Tipo Endereço', reference: 'NM_TIPO_ENDERECO'}, 
                       {label:'Descrição', reference: 'NM_ENDERECO_FORMATADO'},
                       {label:'Principal', reference: 'LG_PRINCIPAL_CALC'}];
var formEndereco = null;
function loadEnderecos(content) {
	if ($('divGridEnderecos') != null) {
		if (content==null && $('cdPessoa').value != 0) {
			getPage("GET", "loadEnderecos", 
					"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
					"&method=getAllEnderecosOfPessoa(const " + $('cdPessoa').value + ":int)", null, true);
		}
		else {
			var rsmEnderecos = null;
			try {rsmEnderecos = eval('(' + content + ')')} catch(e) {}
			if(rsmEnderecos != null){
				$('cdTipoLogradouro').value        = rsmEnderecos.lines[0]['CD_TIPO_LOGRADOURO'];
				$('nmLogradouroPessoa').value      = rsmEnderecos.lines[0]['NM_LOGRADOURO'];
				$('nrEnderecoPessoa2').value       = rsmEnderecos.lines[0]['NR_ENDERECO'];
				$('nmComplementoPessoa2').value    = rsmEnderecos.lines[0]['NM_COMPLEMENTO'];
				$('nmBairroPessoa').value          = rsmEnderecos.lines[0]['NM_BAIRRO'];
				$('nmPontoReferenciaPessoa').value = rsmEnderecos.lines[0]['NM_PONTO_REFERENCIA'];
				$('nrCepPessoa').value             = rsmEnderecos.lines[0]['NR_CEP'];
				$('cdCidadePessoa').value          = rsmEnderecos.lines[0]['CD_CIDADE'];
				$('cdCidadePessoaView').value      = rsmEnderecos.lines[0]['NM_CIDADE']+(rsmEnderecos.lines[0]['SG_ESTADO']!=null?'-'+rsmEnderecos.lines[0]['SG_ESTADO']:'');
			}
			gridEnderecos = GridOne.create('gridEnderecos', {columns: columnsEndereco,
							 resultset: rsmEnderecos, 
							 plotPlace: $('divGridEnderecos'),
							 onProcessRegister: function(reg)	{
							 	reg['LG_PRINCIPAL_CALC'] = reg['LG_PRINCIPAL'] == 1 ? 'Sim' : 'Não';
							 	
								reg['TP_ENDERECAMENTO'] = $('tpEnderecamento').value;
								if (reg['NM_TIPO_LOGRADOURO_ENDERECAMENTO'] == null)
									reg['NM_TIPO_LOGRADOURO_ENDERECAMENTO'] = '';
								if (reg['NM_LOGRADOURO_ENDERECAMENTO'] == null)
									reg['NM_LOGRADOURO_ENDERECAMENTO'] = '';
								reg['NM_LOGRADOURO_FORMATADO'] = reg['NM_TIPO_LOGRADOURO_ENDERECAMENTO'] + ' ' + reg['NM_LOGRADOURO_ENDERECAMENTO'];
								reg['NM_LOGRADOURO_FORMATADO'] = trim(reg['NM_LOGRADOURO_FORMATADO']);
								reg['NM_ENDERECO_FORMATADO'] = getFormatedAddress(reg);
							 },
							 onSelect: onClickEndereco});
		}
	}
	changeLayoutEnderecos((gridEnderecos != null ? gridEnderecos.size() : 0));
}

function onClickEndereco() {
	if (this!=null) {
		var endereco = this.register;
		if (endereco != null) {	
			loadFormRegister(enderecoFields, endereco);
			$("dataOldEndereco").value = captureValuesOfFields(enderecoFields);		
		}
	}
}

function btnNewEnderecoOnClick(){
    if ($('cdPessoa').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma pessoa para lançar endereços.');
	else {
		gridEnderecos.unselectGrid();
		$("dataOldEndereco").value = "";
		clearFields(enderecoFields);
		formEndereco = createWindow('jEndereco', {caption: "endereço",
									  width: 600,
									  height: 173,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'enderecoPanel'});
		$('cdTipoEndereco').focus();
	}
}

function btnAlterEnderecoOnClick(){
    if (gridEnderecos.getSelectedRow() == null)
			showMsgbox('Manager', 300, 50, 'Selecione o endereço que você deseja alterar.');
	else {
		formEndereco = createWindow('jEndereco', {caption: "endereço",
									  width: 600,
									  height: 173,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'enderecoPanel'});
		$('cdTipoEndereco').focus();
	}
}

var prefixCidade = null;
function btnFindCidadeEnderecoOnClick(reg, prefix){
    if(!reg){
		prefixCidade = prefix==null ? '' : prefix;
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
												   callback: btnFindCidadeEnderecoOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
		if ($('sgEstado' + prefixCidade) != null)
			$('sgEstado' + prefixCidade).value = reg[0]['SG_ESTADO'];
		if ($('cdCidade' + prefixCidade) != null)
			$('cdCidade' + prefixCidade).value = reg[0]['CD_CIDADE'];
		if ($('cdCidade' + prefixCidade + 'View') != null)
			$('cdCidade' + prefixCidade + 'View').value = reg[0]['NM_CIDADE']+(reg[0]['SG_ESTADO']!=null?'-'+reg[0]['SG_ESTADO']:'');
		if ($('cdCidade' + prefixCidade + 'Agd') != null)
			$('cdCidade' + prefixCidade + 'Agd').value = reg[0]['CD_CIDADE'];
		if ($('cdCidade' + prefixCidade + 'AgdView') != null)
			$('cdCidade' + prefixCidade + 'AgdView').value = reg[0]['NM_CIDADE']+(reg[0]['SG_ESTADO']!=null?'-'+reg[0]['SG_ESTADO']:'');
    }
}

function btnClearCdCidadeEnderecoOnClick(reg){
	if ($('sgEstado') != null)
		$('sgEstado').value = '';
	if ($('cdCidade') != null)
		$('cdCidade').value = '0';
	if ($('cdCidadeView') != null)
		$('cdCidadeView').value = '';
	if ($('cdCidadeAgd') != null)
		$('cdCidadeAgd').value = 0;
	if ($('cdCidadeAgdView') != null)
		$('cdCidadeAgdView').value = '';
}

function btnFindLogradouroOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro", {caption:"Pesquisar Logradouros", 
										width: 470, 
										height: 275, 
										top:20,
										modal: true, noDrag: true,
										className: "com.tivic.manager.grl.LogradouroServices", method: "find",
										allowFindAll: true,
										filterFields: [[{label:"Tipo", reference:"NM_TIPO_LOGRADOURO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10},
														{label:"Nome do Logradouro", reference:"NM_LOGRADOURO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:90}],
													   [{label:"Cidade", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60},
														{label:"Distrito", reference:"NM_DISTRITO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40}]],
										gridOptions: {columns: [{label:"Tipo", reference:"SG_TIPO_LOGRADOURO"},
																{label:"Nome", reference:"NM_LOGRADOURO"},
																{label:"Cidade", reference:"NM_CIDADE"},
																{label:"UF", reference:"SG_ESTADO"},
																{label:"Distrito", reference:"NM_DISTRITO"}],
													  strippedLines: true,
													  columnSeparator: false,
													  lineSeparator: false},
										hiddenFields: null,
										callback: btnFindLogradouroOnClick, 
										autoExecuteOnEnter: true
										});
    }
    else {// retorno
        closeWindow('jFiltro');
		if ($('nmTipoLogradouroEnderecamento') != null)
			$('nmTipoLogradouroEnderecamento').value = reg[0]['NM_TIPO_LOGRADOURO'];
		if ($('sgTipoLogradouroEnderecamento') != null)
			$('sgTipoLogradouroEnderecamento').value = reg[0]['SG_TIPO_LOGRADOURO'];
		if ($('nmLogradouroEnderecamento') != null)
			$('nmLogradouroEnderecamento').value = reg[0]['NM_LOGRADOURO'];
		if ($('nmTipoLogradouroEnderecamentoAgd') != null)
			$('nmTipoLogradouroEnderecamentoAgd').value = reg[0]['NM_TIPO_LOGRADOURO'];
		if ($('sgTipoLogradouroEnderecamentoAgd') != null)
			$('sgTipoLogradouroEnderecamentoAgd').value = reg[0]['SG_TIPO_LOGRADOURO'];
		if ($('nmLogradouroEnderecamentoAgd') != null)
			$('nmLogradouroEnderecamentoAgd').value = reg[0]['NM_LOGRADOURO'];
		if ($('cdLogradouro') != null)
			$('cdLogradouro').value = reg[0]['CD_LOGRADOURO'];
		if ($('cdLogradouroView') != null)
			$('cdLogradouroView').value = reg[0]['NM_TIPO_LOGRADOURO'] + ' ' + reg[0]['NM_LOGRADOURO'];
		if ($('cdLogradouroAgd') != null)
			$('cdLogradouroAgd').value = reg[0]['CD_LOGRADOURO'];
		if ($('cdLogradouroAgdView') != null)
			$('cdLogradouroAgdView').value = reg[0]['NM_TIPO_LOGRADOURO'] + ' ' + reg[0]['NM_LOGRADOURO'];
    }
}

function btnClearCdLogradouroOnClick(reg){
	if ($('nmTipoLogradouroEnderecamento') != null)
		$('nmTipoLogradouroEnderecamento').value = '';
	if ($('sgTipoLogradouroEnderecamento') != null)
		$('sgTipoLogradouroEnderecamento').value = '';
	if ($('nmLogradouroEnderecamento') != null)
		$('nmLogradouroEnderecamento').value = '';
	if ($('nmTipoLogradouroEnderecamentoAgd') != null)
		$('nmTipoLogradouroEnderecamentoAgd').value = '';
	if ($('sgTipoLogradouroEnderecamentoAgd') != null)
		$('sgTipoLogradouroEnderecamentoAgd').value = '';
	if ($('nmLogradouroEnderecamentoAgd') != null)
		$('nmLogradouroEnderecamentoAgd').value = '';
	if ($('cdLogradouroAgdView') != null)
		$('cdLogradouro').value = '0';
	if ($('cdLogradouroView') != null)
		$('cdLogradouroAgdView').value = '';
	if ($('cdLogradouroAgd') != null)
		$('cdLogradouroAgd').value = '0';
	if ($('cdLogradouroAgdView') != null)
		$('cdLogradouroAgdView').value = '';
}

function btnFindBairroOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro", {caption:"Pesquisar Registros", 
									   width: 370, height: 250, top:25,
									   modal: true, noDrag: true,
									   className: "com.tivic.manager.grl.BairroServices", method: "find",
									   filterFields: [[{label:"Nome Bairro", reference:"NM_BAIRRO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50},
									                   {label:"Cidade", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50}]],
									   gridOptions: {columns: [{label:"Nome", reference:"NM_BAIRRO"},{label:"Distrito", reference:"NM_DISTRITO"},
									                           {label:"Nome", reference:"NM_CIDADE"},{label:"UF", reference:"SG_ESTADO"}],
												   strippedLines: true,
												   columnSeparator: false,
												   lineSeparator: false},
									   callback: btnFindBairroOnClick, 
									   autoExecuteOnEnter: true});
    }
    else {// retorno
        closeWindow('jFiltro');
		if ($('nmBairroEnderecamento') != null)
			$('nmBairroEnderecamento').value = reg[0]['NM_BAIRRO'];
		if ($('cdBairro') != null)
			$('cdBairro').value = reg[0]['CD_BAIRRO'];
		if ($('cdBairroView') != null)
			$('cdBairroView').value = reg[0]['NM_BAIRRO'];
		if ($('cdBairroAgd') != null)
			$('cdBairroAgd').value = reg[0]['CD_BAIRRO'];
		if ($('cdBairroAgdView') != null)
			$('cdBairroAgdView').value = reg[0]['NM_BAIRRO'];
    }
}

function btnClearCdBairroOnClick(reg){
	if ($('nmBairroEnderecamento') != null)
		$('nmBairroEnderecamento').value = '';
	if ($('cdBairro') != null)
		$('cdBairro').value = '0';
	if ($('cdBairroView') != null)
		$('cdBairroView').value = '';
	if ($('cdBairroAgd') != null)
		$('cdBairroAgd').value = '0';
	if ($('cdBairroAgdView') != null)
		$('cdBairroAgdView').value = '';
}

function formValidationEndereco() {
	var campos = [];
    campos.push([$("cdTipoEndereco"), 'Tipo endereço', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([$("cdTipoLogradouro"), 'Tipo logradouro', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([$("nmLogradouro"), 'Nome do logradouro', VAL_CAMPO_NAO_PREENCHIDO]);
    campos.push([$("cdCidade"), 'Cidade', VAL_CAMPO_MAIOR_QUE, 0]);
    return validateFields(campos, true, 'Os campos marcados devem ser preenchidos!', 'nmPessoa');
}

function btnSaveEnderecoOnClick(content){
    if(content==null){
        if (formValidationEndereco()) {
			var pessoaDescription = "(Pessoa: " + $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value + ")";
            var executionDescription = $("cdEndereco").value>0 ? formatDescriptionUpdate("endereço " + pessoaDescription, $("cdEndereco").value, $("dataOldEndereco").value, enderecoFields) : formatDescriptionInsert("Endereco " + pessoaDescription, enderecoFields);
            if (gridEnderecos != null && gridEnderecos.size() == 0) {
				$('lgPrincipal').checked = true;
			}
            getPage("POST", "btnSaveEnderecoOnClick", "../methodcaller?className=com.tivic.manager.grl.PessoaEnderecoServices"+
                                                      "&method=saveEndereco(new com.tivic.manager.grl.PessoaEndereco(cdEndereco: int, cdPessoa: int, dsEndereco: String, cdTipoLogradouro: int, cdTipoEndereco: int, cdLogradouro: int, cdBairro: int, cdCidade: int, nmLogradouro: String, nmBairro: String, nrCep: String, nrEndereco" + ($('tpEnderecamento').value==<%=PessoaEnderecoServices.TP_DIGITAVEL%> ? 2 : 1) + ": String, nmComplemento" + ($('tpEnderecamento').value==<%=PessoaEnderecoServices.TP_DIGITAVEL%> ? 2 : 1) + ": String, nrTelefone: String, nmPontoReferencia: String, lgCobranca: int, lgPrincipal:int):com.tivic.manager.grl.PessoaEndereco)" +
													  "&cdPessoa=" + $("cdPessoa").value, enderecoFields, true, null, executionDescription);
        }
    }
    else{
		closeWindow('jEndereco');
        var ok = parseInt(content, 10) > 0;
		var isInsert = $("cdEndereco").value <= 0;
		if (ok) {
			$("dataOldEndereco").value = captureValuesOfFields(enderecoFields);
			setTimeout('loadEnderecos()', 10);
		}	
		else {
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

function btnDeleteEnderecoOnClick(content)	{
	if(content == null) {
		if (gridEnderecos.getSelectedRow() == null)
			showMsgbox('Manager', 300, 50, 'Selecione o endereço que deseja excluir.');
		else {
			var cdEndereco = gridEnderecos.getSelectedRow().register['CD_ENDERECO'];
			var cdPessoa = $('cdPessoa').value;
			var pessoaDescription = "(Pessoa: " + $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value + ")";
		    var executionDescription = formatDescriptionDelete("endereço " + pessoaDescription, $("cdEndereco").value, $("dataOldEndereco").value);	
			showConfirmbox('Manager', 300, 80, 'você tem certeza que deseja remover o endereço selecionado?', 
							function() {
								getPage('GET', 'btnDeleteEnderecoOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.PessoaEnderecoServices'+
										'&method=delete(const ' + cdEndereco + ':int, const ' + cdPessoa + ':int):int', null, true, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridEnderecos.removeSelectedRow();
			if (parent.updatePanelContato) {
				var registerContato = loadRegisterFromForm(pessoaFields);
				registerContato['rsmFones'] = gridEnderecos.getResultSet();
				parent.updatePanelContato(registerContato);
			}
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir endereço.');
		changeLayoutEnderecos((gridEnderecos != null ? gridEnderecos.size() : 0));
	}
}

/************************                     OCORR-NCIA                       *******************/
var columnsOcorrencia = [{label:'Data', reference: 'DT_OCORRENCIA', type: GridOne._DATE}, 
						 {label:'Tipo', reference: 'NM_TIPO_OCORRENCIA'}, 
						 {label:'Respons?vel', reference: 'NM_RESPONSAVEL'}, 
						 {label:'Situação', reference: 'DS_ST_OCORRENCIA'}];
var formOcorrencia = null;

function loadOcorrencias(content) {
	if (content == null && $('cdPessoa').value != 0) {
		getPage("GET", "loadOcorrencias", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
				"&method=getAllOcorrenciasOfPessoa(const " + $('cdPessoa').value + ":int)", null, true);
	}
	else {
		var rsmOcorrencias = null;
		try {rsmOcorrencias = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmOcorrencias!=null && i<rsmOcorrencias.lines.length; i++)
			rsmOcorrencias.lines[i]['DS_ST_OCORRENCIA'] = parseInt(rsmOcorrencias.lines[i]['ST_OCORRENCIA'], 10)==0 ? 'Pendente' :
														  parseInt(rsmOcorrencias.lines[i]['ST_OCORRENCIA'], 10)==1 ? 'Conclu?do' : 'Cancelado';
		gridOcorrencias = GridOne.create('gridOcorrencias', {columns: columnsOcorrencia,
					     resultset: rsmOcorrencias, 
					     plotPlace: $('divGridOcorrencias'),
					     onSelect: onClickOcorrencia});
	}
}

function onClickOcorrencia() {
	if (this!=null) {
		var ocorrencia = this.register;
		if (ocorrencia != null) {	
			loadFormRegister(ocorrenciaFields, ocorrencia);
			$("dataOldOcorrencia").value = captureValuesOfFields(ocorrenciaFields);		
		}
	}
}

function btnNewOcorrenciaOnClick(){
    if ($('cdPessoa').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma pessoa para lan?ar ocorr?ncias.');
	else {
		gridOcorrencias.unselectGrid();
		$("dataOldOcorrencia").value = "";
		clearFields(ocorrenciaFields);
		$('cdUsuarioOcorrencia').value = $('cdUsuario').value;
		$('cdUsuarioOcorrenciaView').value = $('nmPessoaUsuario').value;
		formOcorrencia = createWindow('jOcorrencia', {caption: "Ocorr?ncia",
									  width: 510,
									  height: 195,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'ocorrenciaPanel'});
		$('cdTipoOcorrencia').focus();
	}
}

function btnAlterOcorrenciaOnClick(){
    if (gridOcorrencias.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione a ocorr?ncia que você deseja alterar.');
	else {
		loadFormRegister(ocorrenciaFields, gridOcorrencias.getSelectedRow().register);
		formOcorrencia = createWindow('jOcorrencia', {caption: "Ocorr?ncia",
									  width: 510,
									  height: 195,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'ocorrenciaPanel'});
		$('cdTipoOcorrencia').focus();
	}
}

function formValidationOcorrencia() {
    if(!validarCampo($("txtOcorrencia"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Descrição' deve ser preenchido.", true, null, null))
        return false;
	else
		return true;
}

function btnSaveOcorrenciaOnClick(content) {
    if(content==null) {
        if (formValidationOcorrencia()) {
			var pessoaDescription = "(Pessoa: " + $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value + ")";
            var executionDescription = $("cdOcorrencia").value>0 ? formatDescriptionUpdate("Ocorr?ncia " + pessoaDescription, $("cdOcorrencia").value, $("dataOldOcorrencia").value, ocorrenciaFields) : formatDescriptionInsert("Ocorrencia " + pessoaDescription, ocorrenciaFields);
            if($("cdOcorrencia").value>0)
                getPage("POST", "btnSaveOcorrenciaOnClick", "../methodcaller?className=com.tivic.manager.grl.OcorrenciaServices"+
                                                            "&method=update(new com.tivic.manager.grl.Ocorrencia(cdOcorrencia: int, cdPessoa: int, txtOcorrencia: String, dtOcorrencia: GregorianCalendar, cdTipoOcorrencia: int, stOcorrencia: int, cdSistema: int, cdUsuarioOcorrencia:int):com.tivic.manager.grl.Ocorrencia)" +
														    "&cdPessoa=" + $("cdPessoa").value, ocorrenciaFields, true, null, executionDescription);
            else
                getPage("POST", "btnSaveOcorrenciaOnClick", "../methodcaller?className=com.tivic.manager.grl.OcorrenciaServices"+
                                                            "&method=insert(new com.tivic.manager.grl.Ocorrencia(cdOcorrencia: int, cdPessoa: int, txtOcorrencia: String, dtOcorrencia: GregorianCalendar, cdTipoOcorrencia: int, stOcorrencia: int, cdSistema: int, cdUsuarioOcorrencia:int):com.tivic.manager.grl.Ocorrencia)" +
														    "&cdPessoa=" + $("cdPessoa").value, ocorrenciaFields, true, null, executionDescription);
        }
    }
    else {
		closeWindow('jOcorrencia');
		var ocorrencia = null;
		try { ocorrencia = eval("(" + content + ")")} catch(e) {}
		if (ocorrencia != null) {
			loadOcorrencias();
			if (gridOcorrencias.getSelectedRow() == null) {
				gridOcorrencias.addLine(0, loadRegisterFromForm(ocorrenciaFields), onClickOcorrencia, true);
			}
			else {
				gridOcorrencias.updateSelectedRow(loadRegisterFromForm(ocorrenciaFields));
			}
			$("dataOldOcorrencia").value = captureValuesOfFields(ocorrenciaFields);
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
		}	
		else {
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
		}
    }
}

function btnDeleteOcorrenciaOnClick(content)	{
	if(content==null) {
		if (gridOcorrencias.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione a ocorr?ncia que deseja excluir.');
		else {
			var cdOcorrencia = gridOcorrencias.getSelectedRow().register['CD_OCORRENCIA'];
			var cdPessoa = $('cdPessoa').value;
			var pessoaDescription = "(Pessoa: " + $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value + ")";
		    var executionDescription = formatDescriptionDelete("Ocorr?ncia " + pessoaDescription, $("cdOcorrencia").value, $("dataOldOcorrencia").value);	
			showConfirmbox('Manager', 300, 80, 'você tem certeza que deseja remover a ocorr?ncia selecionado?', 
							function() {
								getPage('GET', 'btnDeleteOcorrenciaOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.OcorrenciaDAO'+
										'&method=delete(const ' + cdOcorrencia + ':int):int', null, true, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridOcorrencias.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir Ocorr?ncia.');
	}
}

/**********************                      ARQUIVOS                 ***********************/

var columnsArquivo = [{label:'Nome', reference: 'NM_DOCUMENTO'}, 
					  {label:'Tipo', reference: 'NM_TIPO_ARQUIVO'}, 
					  {label:'Data', reference: 'DT_ARQUIVAMENTO'}, 
					  {label:'Dw', reference: 'DOWNLOAD_IMG', type: GridOne._IMAGE, onImgClick: onClickDownloadArquivo, imgWidth: 10, Hint: 'Efetuar download do arquivo...'},
					  {label:'Vs', reference: 'VISUALIZAR_IMG', type: GridOne._IMAGE, onImgClick: onClickVisualizarArquivo, imgWidth: 10, Hint: 'Visualizar o arquivo...'}
					 ];
function loadArquivos(content) {
	if (content==null && $('cdPessoa').value != 0) {
		getPage("GET", "loadArquivos", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
				"&method=getAllArquivosOfPessoa(const " + $('cdPessoa').value + ":int)", null, true);
	}
	else {
		var rsmArquivos = null;
		try {rsmArquivos = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmArquivos!=null && i<rsmArquivos.lines.length; i++) {
			rsmArquivos.lines[i]['DOWNLOAD_IMG'] = 'imagens/download16.gif';
			rsmArquivos.lines[i]['VISUALIZAR_IMG'] = 'imagens/visualizar16.gif';
		}
		gridArquivos = GridOne.create('gridArquivos', {columns: columnsArquivo,
						     resultset: rsmArquivos, 
						     plotPlace: $('divGridArquivos'),
						     onSelect: onClickArquivo});
	}
}

function onClickArquivo() {
	if (this!=null) {
		var arquivo = this.register;
		if (arquivo != null)	{
			loadFormRegister(arquivoFields, arquivo);
			$("dataOldArquivo").value = captureValuesOfFields(arquivoFields);		
		}
	}
}

function btnNewArquivoOnClick(){
    if ($('cdPessoa').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma pessoa para lan?ar arquivos.');
	else {
		gridArquivos.unselectGrid();
		$("dataOldArquivo").value = "";
		clearFields(arquivoFields);
		createWindow('jArquivo', {caption: "Arquivo",
									  width: 510,
									  height: 209,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'arquivoPanel'});
		$('nmDocumento').focus();
	}
}

function btnAlterArquivoOnClick(){
    if (gridArquivos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o arquivo que você deseja alterar.');
	else {
		createWindow('jArquivo', {caption: "Arquivo",
									  width: 510,
									  height: 205,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'arquivoPanel'});
		$('nmDocumento').focus();
	}
}

function formValidationArquivo() {
    if(!validarCampo($("nmDocumento"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome Documento' deve ser preenchido.", true, null, null))
        return false;
    else if($("cdArquivo").value<=0 && !validarCampo($("blbArquivo"), VAL_CAMPO_NAO_PREENCHIDO, true, "Indique o local onde se encontra o arquivo.", true, null, null))
        return false;
	else
		return true;
}

function btnSaveArquivoOnClick(content){
    if(content==null){
        if (formValidationArquivo()) {
			var pessoaDescription = "(Pessoa: " + $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value + ")";
            var executionDescription = $("cdArquivo").value>0 ? formatDescriptionUpdate("Arquivo " + pessoaDescription, $("cdArquivo").value, $("dataOldArquivo").value, arquivoFields) : formatDescriptionInsert("Arquivo " + pessoaDescription, arquivoFields);
            var parameterBlbArquivo = trim($("blbArquivo").value)=='' ? 'blbArquivoTemp' : '*blbArquivo';
            var parameterUpdateFile = trim($("blbArquivo").value)=='' ? 'true' : 'false';
			var constructorArquivo = "cdArquivo: int, nmArquivo: String, dtArquivamento: GregorianCalendar, nmDocumento: String, " + parameterBlbArquivo + ": byte[], cdTipoArquivo: int";
			var objects = trim($("blbArquivo").value)=='' ? '' : 'blbArquivo=byte[]';
			var commandExecute = trim($("blbArquivo").value)=='' ? '' : 'blbArquivo=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const blbArquivo:String);';
			if($("cdArquivo").value>0)
                getPage("POST", "btnSaveArquivoOnClick", "../methodcaller?className=com.tivic.manager.grl.PessoaArquivoServices"+
														 "&execute=" + commandExecute +
														 "&objects=" + objects +
                                                          "&method=update(new com.tivic.manager.grl.PessoaArquivo(cdArquivo: int, cdPessoa: int, txtObservacaoArquivo: String, cdNivelAcesso: int, cdSetorArquivo: int):com.tivic.manager.grl.PessoaArquivo, new com.tivic.manager.grl.Arquivo(" + constructorArquivo + "):com.tivic.manager.grl.Arquivo, const " + parameterUpdateFile + ":boolean)" +
														  "&cdPessoa=" + $("cdPessoa").value, arquivoFields, true, null, executionDescription);
            else
                getPage("POST", "btnSaveArquivoOnClick", "../methodcaller?className=com.tivic.manager.grl.PessoaArquivoServices"+
														 "&execute=" + commandExecute +
														 "&objects=" + objects +
                                                          "&method=insert(new com.tivic.manager.grl.PessoaArquivo(cdArquivo: int, cdPessoa: int, txtObservacaoArquivo: String, cdNivelAcesso: int, cdSetorArquivo: int):com.tivic.manager.grl.PessoaArquivo, new com.tivic.manager.grl.Arquivo(" + constructorArquivo + "):com.tivic.manager.grl.Arquivo)" +
														  "&cdPessoa=" + $("cdPessoa").value, arquivoFields, true, null, executionDescription);
        }
    }
    else{
		closeWindow('jArquivo');
		var arquivo = null;
		try { arquivo = eval("(" + content + ")")} catch(e) {}
        var ok = arquivo!=null;
		var isInsert = $("cdArquivo").value<=0;
		if (ok) {
			if (isInsert)
				$("cdArquivo").value = arquivo['cdArquivo'];	
			var arquivoRegister = {};
			for (var i=0; i<arquivoFields.length; i++)
				arquivoRegister[arquivoFields[i].getAttribute("reference").toUpperCase()] = arquivoFields[i].getAttribute("lguppercase")=="true" ? arquivoFields[i].value.toUpperCase() : arquivoFields[i].value;
			if (isInsert)
				arquivoRegister['DT_ARQUIVAMENTO'] = arquivo['dtArquivamento'];	
			arquivoRegister['NM_TIPO_ARQUIVO'] = $('cdTipoArquivo').value>0 && $('cdTipoArquivo').selectedIndex > -1 ? $('cdTipoArquivo').options[$('cdTipoArquivo').selectedIndex].text : '';
			arquivoRegister['NM_NIVEL_ACESSO'] = $('cdNivelAcesso').value>0 && $('cdNivelAcesso').selectedIndex > -1 ? $('cdNivelAcesso').options[$('cdNivelAcesso').selectedIndex].text : '';
			arquivoRegister['DOWNLOAD_IMG'] = 'imagens/download16.gif';
			arquivoRegister['VISUALIZAR_IMG'] = 'imagens/visualizar16.gif';
			if (isInsert)
				gridArquivos.addLine(0, arquivoRegister, onClickArquivo, true)	
			else {
				gridArquivos.getSelectedRow().register = arquivoRegister;
				gridArquivos.changeCellValue(gridArquivos.getSelectedRow().rowIndex, 2, arquivoRegister['NM_TIPO_ARQUIVO']);
				gridArquivos.changeCellValue(gridArquivos.getSelectedRow().rowIndex, 1, arquivoRegister['NM_DOCUMENTO']);
			}
			
			$("dataOldArquivo").value = captureValuesOfFields(arquivoFields);
		}	
		if (!ok)
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
    }
}

function btnDeleteArquivoOnClick(content)	{
	if(content==null) {
		if (gridArquivos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o arquivo que deseja excluir.');
		else {
			var cdArquivo = gridArquivos.getSelectedRow().register['CD_ARQUIVO'];
			var cdPessoa = $('cdPessoa').value;
			var pessoaDescription = "(Pessoa: " + $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value + ")";
		    var executionDescription = formatDescriptionDelete("Arquivo " + pessoaDescription, $("cdArquivo").value, $("dataOldArquivo").value);	
			showConfirmbox('Manager', 300, 80, 'você tem certeza que deseja remover o arquivo selecionado?', 
							function() {
								getPage('GET', 'btnDeleteArquivoOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.PessoaArquivoServices'+
										'&method=delete(const ' + cdArquivo + ':int, const ' + cdPessoa + ':int):int', null, true, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridArquivos.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir Arquivo.');
	}
}

function btnSaveArquivoAuxOnClick() {
	if (formValidationArquivo()) {
		if (trim($("blbArquivo").value) == '')
			btnSaveArquivoOnClick();
		else {
			document.frameArquivo.submit();
		}
	}
}

function onClickDownloadArquivo(source) {
	changeLayoutAbaArquivos(0);
	var cdArquivo = this.parentNode.parentNode.register['CD_ARQUIVO'];
	$('frameHidden').src = 'download_arquivo.jsp?className=com.tivic.manager.grl.ArquivoServices&method=getBytesArquivo(const ' + cdArquivo + ':int)';
}

function onClickVisualizarArquivo(source) {
	changeLayoutAbaArquivos(1, this);
}

function changeLayoutAbaArquivos(tipoLayout, line) {
	$('previewArquivo').src = 'preview_imagem.jsp?lgDefaultInBlank=1';
	if ($('divGridArquivos') != null) {
		$('divGridArquivos').style.height = (tipoLayout == 1 ? '128px' : '333px');
	}
	if (gridArquivos != null) {
		gridArquivos.resize(parseInt($('divGridArquivos').style.width, 10), parseInt($('divGridArquivos').style.height, 10));
	}
	$('divPreviewArquivo').style.display = (tipoLayout == 1 ? '' : 'none');;
	if (tipoLayout == 1) {
		if (gridArquivos) {
			var cdArquivo = line.parentNode.parentNode.register['CD_ARQUIVO'];
			if (cdArquivo > 0) {
				$('previewArquivo').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.ArquivoServices&method=getBytesArquivo(const ' + cdArquivo + ':int)';
			}
		}
	}
}
/***************************                      Conta Banc?ria                       ********************************/
var columnsContaBancaria = [{label:'Situação', reference: 'CL_ST_CONTA'}, {label:'Banco', reference: 'NM_BANCO'}, 
                            {label:'Ag?ncia', reference: 'NR_AGENCIA'}, {label:'N¦ da Conta', reference: 'CL_NUMERO'}, 
							{label:'N¦ CPF/CNPJ', reference: 'NR_CPF_CNPJ'}, {label:'Titular da Conta', reference: 'NM_TITULAR'}];
var gridContaBancaria = null;
var formContaBancaria = null;
function formValidationContaBancaria(){
	var campos = [];
    campos.push([$("cdBanco"), 'Banco', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([$("nrAgencia"), 'N¦ Ag?ncia', VAL_CAMPO_NAO_PREENCHIDO]);
    campos.push([$("nrConta"), 'N¦ Conta', VAL_CAMPO_NAO_PREENCHIDO]);
    campos.push([$("nrDv"), 'D?gito Verificador', VAL_CAMPO_NAO_PREENCHIDO]);
    campos.push([$("tpOperacao"), 'N¦ Ag?ncia', VAL_CAMPO_MAIOR_QUE, -1]);
	if (($('nrCpfCnpj').value+'').length < 14)
	    campos.push([$("nrCpfCnpj"), 'CPF Inv?lido', VAL_CAMPO_CPF]);
	else
	    campos.push([$("nrCpfCnpj"), 'CNPJ Inv?lido', VAL_CAMPO_CNPJ]);
	if(($('nrCpfCnpj').value+'').length > 0) {
    	campos.push([$("nmTitular"), 'Nome do titular da conta', VAL_CAMPO_NAO_PREENCHIDO]);
	}
    return validateFields(campos, true, 'Os campos marcados devem ser preenchidos!', 'cdBanco');
}
function clearFormContaBancaria(){
    $("dataOldContaBancaria").value = "";
    clearFields(contaBancariaFields);
}
function btnNewContaBancariaOnClick(){
    if ($('cdPessoa').value == 0)	{
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma pessoa para cadastrar uma conta banc?ria.');
		return;
	}
    clearFormContaBancaria();
	formContaBancaria = createWindow('jContaBancaria', {caption: "Conta banc?ria",
								  width: 390,
								  height: 158,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'panelContaBancaria'});
    $('cdBanco').focus();
}

function btnAlterContaBancariaOnClick()	{
	if(gridContaBancaria.getSelectedRowRegister()==null)	{
		showMsgbox('Manager', 300, 50, 'Selecione a conta banc?ria que deseja alterar.');
		return;
	}
	loadFormRegister(contaBancariaFields, gridContaBancaria.getSelectedRowRegister());
	formContaBancaria = createWindow('jContaBancaria', {caption: "Conta banc?ria",
								  width: 390,
								  height: 158,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'panelContaBancaria'});
    $('cdBanco').focus();
}

function btnSaveContaBancariaOnClick(content){
    if(content==null){
        if (formValidationContaBancaria()) {
            var executionDescription = $("cdContaBancaria").value>0 ? formatDescriptionUpdate("ContaBancaria", $("cdContaBancaria").value, $("dataOldContaBancaria").value, contaBancariaFields) : formatDescriptionInsert("ContaBancaria", contaBancariaFields);
            if($("cdContaBancaria").value>0)
                getPage("POST", "btnSaveContaBancariaOnClick", "../methodcaller?className=com.tivic.manager.grl.PessoaContaBancariaDAO"+
                                                          "&method=update(new com.tivic.manager.grl.PessoaContaBancaria(cdContaBancaria: int,const "+$('cdPessoa').value+" : int, cdBanco: int, nrConta: String, nrDv: String, nrAgencia: String, tpOperacao: int, nrCpfCnpj: String, nmTitular: String, stConta: int):com.tivic.manager.grl.PessoaContaBancaria)", contaBancariaFields, true, null, executionDescription);
            else
                getPage("POST", "btnSaveContaBancariaOnClick", "../methodcaller?className=com.tivic.manager.grl.PessoaContaBancariaDAO"+
                                                          "&method=insert(new com.tivic.manager.grl.PessoaContaBancaria(cdContaBancaria: int,const "+$('cdPessoa').value+" : int, cdBanco: int, nrConta: String, nrDv: String, nrAgencia: String, tpOperacao: int, nrCpfCnpj: String, nmTitular: String, stConta: int):com.tivic.manager.grl.PessoaContaBancaria)", contaBancariaFields, true, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if($("cdContaBancaria").value<=0)	{
            $("cdContaBancaria").value = content;
            ok = ($("cdContaBancaria").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
			loadContaBancaria(null);
			formContaBancaria.close();
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldContaBancaria").value = captureValuesOfFields(contaBancariaFields);
        }
        else{
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

function btnDeleteContaBancariaOnClick(content){
    if(content==null){
        if (!gridContaBancaria.getSelectedRowRegister())
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteContaBancariaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 200, 
                                  height: 50, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            loadContaBancaria(null);
        }
        else
            createTempbox("jTemp", {width: 350, 
                                  height: 50, 
                                  message: "Não foi possível excluir este registro! Provavelmente existem dados relacionados.", 
                                  time: 5000});
    }	
}

function btnDeleteContaBancariaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("ContaBancaria", $("cdContaBancaria").value, $("dataOldContaBancaria").value);
    getPage("GET", "btnDeleteContaBancariaOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.PessoaContaBancariaDAO"+
            "&method=delete(const "+gridContaBancaria.getSelectedRowRegister()['CD_CONTA_BANCARIA']+":int,const "+$('cdPessoa').value+" : int):int", null, true, null, executionDescription);
}

function loadContaBancaria(content) {
	if (content==null && $('cdPessoa').value != 0) {
		getPage("GET", "loadContaBancaria", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
				"&method=getAllContaBancaria(const " + $('cdPessoa').value + ":int)", null, true);
	}
	else {
		var rsmContaBancaria = eval('(' + content + ')');
		gridContaBancaria = GridOne.create('gridContaBancaria', {columns: columnsContaBancaria,
												                 resultset: rsmContaBancaria, 
												                 plotPlace: $('divGridContaBancaria'),
												                 onProcessRegister: function(reg)	{
																						var situacaoConta = <%=sol.util.Jso.getStream(com.tivic.manager.grl.PessoaContaBancariaServices.situacaoConta)%>;
																						reg['CL_ST_CONTA'] = situacaoConta[reg['ST_CONTA']];
																						reg['CL_NUMERO'] = reg['NR_CONTA']+'-'+reg['NR_DV'];
																					}
																});
	}
}

/*********** V-NCULOS/DADOS FUNCIONAIS ********************/
/*** DEPENDENTES ***/
var formDependente = null;
function formValidationDependentes(){
    if(!validarCampo($("nmDependente"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'nome do dependente' deve ser preenchido.", true, null, null))
        return false;
    return true;
}

function clearFormDependentes(){
    clearFields(dependentesFields);
}

function btnNewDependenteOnClick(){
    if ($('cdPessoa').value <= 0)	{
		createMsgbox("jMsg", {width: 300, 
                                  height: 75, 
                                  message: 'Inclua ou localize uma pessoa para cadastrar o dependente.',
                                  msgboxType: "INFO"});
		return;
	}
    clearFormDependentes();
	formDependente = createWindow('jDependente', {caption: "Dependente/Pensionista",
								  width: 590,
								  height: 146,
								  top: 100,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'panelDependente'});
    $('nmDependente').focus();
}

function btnAlterDependenteOnClick(){
	if(gridDependentes.getSelectedRowRegister()==null)	{
		createMsgbox("jMsg", {width: 300, 
						  height: 50, 
						  message: 'Selecione o dependente que deseja alterar.',
						  msgboxType: "INFO"});
		return;
	}
	loadFormRegister(dependentesFields, gridDependentes.getSelectedRowRegister());
	checkPensionista();
	formDependente = createWindow('jDependente', {caption: "Dependente/Pensionista",
								  width: 590,
								  height: 146,
								  top: 100,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'panelDependente'});
	checkPensionista();								  
    $('nmDependente').focus();
}

function btnSaveDependenteOnClick(content){
    if(content==null){
        if (formValidationDependentes()) {
            var executionDescription = $("cdDependente").value > 0 ? formatDescriptionUpdate("Dependente", $("cdDependente").value, $("dataOldDependente").value, dependentesFields) : formatDescriptionInsert("Dependentes", dependentesFields);
			
			var construtorPessoa = "new com.tivic.manager.grl.PessoaFisica(const " + $("cdDependente").value + ": int,"+
						  "cdPessoaSuperior: int,cdPais: int,nmDependente: String,nrTelefoneDependente: String, "+
						  "nrTelefone2: String,nrCelularDependente: String,nrFax: String,nmEmailDependente: String,"+
						  "dtCadastro: GregorianCalendar,const 1: int,imgFoto: byte[],stCadastroDependente: int,"+
						  "nmUrlDependente: String,nmApelidoDependente: String,txtObservacao: String,const 0: int,"+
						  "idPessoa: String,cdClassificacao: int,cdFormaDivulgacao: int,dtChegadaPais: GregorianCalendar,"+
						  "cdNaturalidade: int,cdEscolaridadeDependente: int,dtNascimentoDependente: GregorianCalendar,"+
						  "nrCpf: String,sgOrgaoRgDependente: String,nmMae: String,nmPai: String,tpSexoDependente: int,"+
						  "stEstadoCivilDependente: int,nrRgDependente: String,nrCnh: String,dtValidadeCnh: GregorianCalendar,"+
						  "dtPrimeiraHabilitacao: GregorianCalendar,tpCategoriaHabilitacao: int,tpRaca: int,lgDeficienteFisico: int," + 	
						  "nmFormaTratamento: String,cdEstadoRgDependente: int,dtEmissaoRgDependente: GregorianCalendar)";

			var construtorDependente = "new com.tivic.manager.srh.Dependente(const " + $('cdPessoa').value + ": int," + 
			           "const " + $("cdDependente").value + ": int,tpParentesco:int,lgDependenteIr: int," + 
					   "lgDependenteSalarioFamilia: int,lgDependentePensionista: int,tpPagamentoPensionista: int," + 
					   "tpCalculoPensaoDependente: int,vlAplicacaoPensionista: float,nrContaPensionista: String," + 
					   "tpOperacaoPensionista: int,nrAgenciaPensionista: String,cdBancoPensionista: int)";
			
			var method = (($("cdDependente").value > 0) ? "update" : "insert") + "(" + construtorPessoa + ":com.tivic.manager.grl.PessoaFisica," +
			             construtorDependente + ":com.tivic.manager.srh.Dependente)";
			getPage("POST", "btnSaveDependenteOnClick", 
					"../methodcaller?className=com.tivic.manager.srh.DependenteServices"+
                    "&method=" + method, dependentesFields, true, null, executionDescription);
        }
    }
    else{
        var ok = (parseInt(content, 10) > 0);
        if(ok){
            loadDependentes();
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldDependente").value = captureValuesOfFields(dependentesFields);
			formDependente.close();
        }
        else{
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

function btnDeleteDependenteOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Dependentes", gridDependentes.getSelectedRowRegister()['CD_DEPENDENTE'], $("dataOldDependente").value);
    getPage("GET", "btnDeleteDependenteOnClick", 
            "../methodcaller?className=com.tivic.manager.srh.DependenteServices"+
            "&method=delete(const " + $("cdPessoa").value + ":int,const " + gridDependentes.getSelectedRowRegister()['CD_DEPENDENTE'] + ":int):int", null, true, null, executionDescription);
}

function btnDeleteDependenteOnClick(content){
    if(content==null){
        if(gridDependentes.getSelectedRowRegister()==null)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteDependenteOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 200, 
                                  height: 50, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            loadDependentes();
        }
        else
            createTempbox("jTemp", {width: 350, 
                                  height: 50, 
                                  message: "Não foi possível excluir este registro! Provavelmente existem dados relacionados.", 
                                  time: 5000});
    }	
}

function processRegisterDependentes(reg){
    if (reg['DT_NASCIMENTO'] != null) 
		reg['VL_IDADE'] = getIdade(reg['DT_NASCIMENTO']); 
}

function loadDependentes(content) {
	if (content==null && $('cdPessoa').value != 0) {
		getPage("GET", "loadDependentes", 
				"../methodcaller?className=com.tivic.manager.srh.DependenteServices"+
				"&method=find(const " + $('cdPessoa').value + ":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		gridDependentes = GridOne.create('gridDependentes', {
					     columns: [{label:'Nome', reference:'NM_PESSOA'}, 
						           {label:'Data Nascimento', reference:'DT_NASCIMENTO', type: GridOne._DATE},
								   {label:'Idade', reference:'VL_IDADE'}, 
								   {label:'Identidade', reference:'NR_RG'}],
					     resultset: rsm, 
						 onProcessRegister: function(reg){
						 	processRegisterDependentes(reg);
						 },
					     plotPlace: $('divGridDependentes')});
	}
	if(gridDependentes.getSelectedRowRegister()) {	
		loadFormRegister(dependentesFields, gridDependentes.getSelectedRowRegister());
	}
	else {
		clearFormDependentes();
	}
	<%	if(showDependentes)	{%>		
		if (gridDependentes != null) {
			$('divGridDependentesPai').style.width = '654px';
		    $('divGridDependentes').style.width = '651px';
		    $('divGridDependentes').style.heigth = '333px';
			gridDependentes.resize(parseInt($('divGridDependentes').style.width, 10), parseInt($('divGridDependentes').style.heigth, 10));
		}
	<%	}%>
}

function btnFindCdBancoOnClick(reg) {
    if(!reg)	{
		FilterOne.create("jFiltro", {caption:"Pesquisar bancos",
									 width: 500, 
									 height: 300,
									 modal: true, 
									 allowFindAll: true, 
									 noDrag: true,
									 // Parametros do filtro
									 className: "com.tivic.manager.grl.BancoDAO",
									 method: "find",
									 filterFields: [{label:"Nome do banco", reference:"NM_BANCO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80},
									 			    {label:"Código", reference:"NR_BANCO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20}],
									 gridOptions: {columns:[{label:"Nome do banco", reference:"NM_BANCO"},
									                        {label:"Código", reference:"NR_BANCO"}]},
									 callback: btnFindCdBancoOnClick, 
									 autoExecuteOnEnter: true
									});
    }
    else {// retorno
        closeWindow('jFiltro');
        $("cdBancoPensionista").value     = reg[0]['CD_BANCO'];
        $("cdBancoPensionistaView").value = reg[0]['NM_BANCO'];
	}
}

function checkPensionista(){
	if (formDependente) {
		formDependente.resize(590, ($('lgDependentePensionista').checked) ? 223 : 146);
	}
	$('panelPensionista').style.display = ($('lgDependentePensionista').checked) ? 'block' : 'none';
}
/*------------------ CNAES ------------------------*/
var columnsCnae = [{label:'Nome CNAE', reference: 'NM_CNAE'}, 
				   {label:'Principal', reference: 'LG_PRINCIPAL', type:GridOne._CHECKBOX, onCheck: onClickLgPrincipalCnae}];
var gridCnaes = null;
var checkboxTemp = null;
function onClickLgPrincipalCnae(content) {
	if(content==null){
		checkboxTemp = this;
		var cdCnae = this.parentNode.parentNode.register["CD_CNAE"];
		var nmCnae = this.parentNode.parentNode.register["NM_CNAE"];
		var cdPessoa = $('cdPessoa').value;
		var pessoaDescription = $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value;
		if (this.checked) {
			var executionDescription = "Configuração do Cnae " + nmCnae + " (Cod. " + cdCnae + ") como Cnae Principal do Produto " + pessoaDescription;		
			getPage("GET", "onClickLgPrincipalCnae", "../methodcaller?className=com.tivic.manager.grl.CnaePessoaJuridicaServices"+
													  "&method=setCnaePrincipal(cdPessoa: int, cdCnae: int)" +
													  "&cdPessoa=" + cdPessoa +
													  "&cdCnae=" + cdCnae, null, true, null, executionDescription);
		}
		else {
			var executionDescription = "Retirando status de Cnae Principal do Cnae " + nmCnae + " (Cod. " + cdCnae + ") em relação ao Produto " + pessoaDescription;		
			getPage("GET", "onClickLgPrincipalCnae", "../methodcaller?className=com.tivic.manager.grl.CnaePessoaJuridicaDAO"+
													  "&method=update(new com.tivic.manager.grl.CnaePessoaJuridica(cdPessoa: int, cdCnae: int, lgPrincipal: int):new com.tivic.manager.grl.CnaePessoaJuridica)" +
													  "&cdPessoa=" + cdPessoa + 
													  "&lgPrincipal=0" + 
													  "&cdCnae=" + cdCnae, null, true, null, executionDescription);
		}
    }
    else{
        var ok = parseInt(content, 10) > 0;
		if (ok) {
			if (checkboxTemp.checked) {
				var checkBoxes = gridCnaes.getElementsColumn(2);
				for (var i=0; checkBoxes!=null && i<checkBoxes.length; i++) {
					if (checkBoxes[i].id != checkboxTemp.id)
						checkBoxes[i].checked = false;
				}
			}
            createTempbox("jMsg", {width: 300, height: 50,
                                   message: "Dados atualizados com sucesso.",
                                   tempboxType: "MESSAGE", time: 3000});
		}
		else
            createTempbox("jMsg", {width: 300, height: 50,
                                   message: "ERRO ao atualizar dados...",
                                   tempboxType: "ERROR", time: 3000});
    }
}

function loadCnaes(content) {
	if (content==null && $('cdPessoa').value != 0) {
		getPage("GET", "loadCnaes", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaJuridicaServices"+
				"&method=getAllCnaes(const " + $('cdPessoa').value + ":int)", null, true);
	}
	else {
		var rsmCnaes = null;
		try {rsmCnaes = eval('(' + content + ')')} catch(e) {}
		gridCnaes = GridOne.create('gridCnaes', {columns: columnsCnae,
					     resultset: rsmCnaes, 
					     plotPlace: $('divGridCnaes'),
					     onSelect: null});
	}
}

var cnaeTemp = null;
function btnNewCnaeOnClick(reg){
    if(!reg){
		if ($('cdPessoa').value == 0)
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Inclua ou localize um registro para adicionar CNAEs.",
                                  msgboxType: "INFO"})
		else {
			FilterOne.create("jFiltro", {caption:"Pesquisar CNAEs", 
										   width: 350,
										   height: 225,
										   top:50,
										   modal: true,
										   noDrag: true,
										   className: "com.tivic.manager.grl.CnaeDAO",
										   method: "find",
										   allowFindAll: true,
										   filterFields: [[{label:"Nome", reference:"NM_CNAE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
										   gridOptions: {columns: [{label:"Nome", reference:"NM_CNAE"}],
													   strippedLines: true,
													   columnSeparator: false,
													   lineSeparator: false},
										   hiddenFields: null,
										   callback: btnNewCnaeOnClick, 
										   autoExecuteOnEnter: true});
		}
    }
    else {// retorno
        closeWindow('jFiltro');
		cnaeTemp = reg[0];
		setTimeout('btnNewCnaeAuxOnClick()', 1);
    }
}

function btnNewCnaeAuxOnClick(content){
    if(content==null){
		var cdCnae = cnaeTemp["CD_CNAE"];
		var nmCnae = cnaeTemp['NM_CNAE'];
		var cdPessoa = $('cdPessoa').value;
		var pessoaDescription = $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value;
		var executionDescription = "Adição do Cnae " + nmCnae + " (Cod. " + cdCnae + ") ? relação de CNAEs da Pessoa Jurídica " + pessoaDescription;		
		getPage("GET", "btnNewCnaeAuxOnClick", "../methodcaller?className=com.tivic.manager.grl.CnaePessoaJuridicaDAO"+
												  "&method=insert(new com.tivic.manager.grl.CnaePessoaJuridica(cdPessoa: int, cdCnae: int, lgPrincipal: int):com.tivic.manager.grl.CnaePessoaJuridica)" +
												  "&cdPessoa=" + cdPessoa +
												  "&cdCnae=" + cdCnae +
												  "&lgPrincipal=0", null, true, null, executionDescription);
    }
    else{
        var ok = parseInt(content, 10) > 0;
		if (ok) {
			cnaeTemp['LG_PRINCIPAL'] = 0;
			gridCnaes.addLine(0, cnaeTemp, null, true)	
		}
		else
            createTempbox("jMsg", {width: 200, height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
    }
}

function btnDeleteCnaeOnClick(content)	{
	if(content==null) {
		if (gridCnaes.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o CNAE que deseja excluir.');
		else {
			var cdCnae = gridCnaes.getSelectedRow().register['CD_CNAE'];
			var nmCnae = gridCnaes.getSelectedRow().register['NM_CNAE'];
			var cdPessoa = $('cdPessoa').value;
			var pessoaDescription = $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value;
		    var executionDescription = "Remoção do Cnae " + nmCnae + " (Cod. " + cdCnae + ") da relação de grupos ao qual pertence o Produto " + pessoaDescription;	
			showConfirmbox('Manager', 300, 80, 'VocÊ tem certeza que deseja remover o CNAE selecionado?', 
							function() {
								getPage('GET', 'btnDeleteCnaeOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.CnaePessoaJuridicaDAO'+
										'&method=delete(const ' + cdPessoa + ':int, const ' + cdCnae + ':int):int', null, true, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridCnaes.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir CNAE.');
	}
}

/*------------------ CBOS ------------------------*/
var checkboxTemp = null;
var columnsCbo = [{label:'Nome CBO', reference: 'NM_CBO'}, {label:'Principal', reference: 'LG_PRINCIPAL', type:GridOne._CHECKBOX, onCheck: onClickLgPrincipalCbo}];
var gridCbos = null;

function onClickLgPrincipalCbo(content) {
	if(content==null){
		checkboxTemp = this;
		var cdCbo = this.parentNode.parentNode.register["CD_CBO"];
		var nmCbo = this.parentNode.parentNode.register["NM_CBO"];
		var cdPessoa = $('cdPessoa').value;
		var pessoaDescription = $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value;
		if (this.checked) {
			var executionDescription = "Configuração da Ocupação " + nmCbo + " (Cod. " + cdCbo + ") como Ocupação Principal da Pessoa " + pessoaDescription;		
			getPage("GET", "onClickLgPrincipalCbo", "../methodcaller?className=com.tivic.manager.grl.CboPessoaFisicaServices"+
													  "&method=setCboPrincipal(cdPessoa: int, cdCbo: int)" +
													  "&cdPessoa=" + cdPessoa +
													  "&cdCbo=" + cdCbo, null, true, null, executionDescription);
		}
		else {
			var executionDescription = "Retirando status de CBO Principal da Ocupação " + nmCbo + " (Cod. " + cdCbo + ") em relação à Pessoa " + pessoaDescription;		
			getPage("GET", "onClickLgPrincipalCbo", "../methodcaller?className=com.tivic.manager.grl.CboPessoaFisicaDAO"+
													  "&method=update(new com.tivic.manager.grl.CboPessoaFisica(cdPessoa: int, cdCbo: int, lgPrincipal: int):new com.tivic.manager.grl.CboPessoaFisica)" +
													  "&cdPessoa=" + cdPessoa + 
													  "&lgPrincipal=0" + 
													  "&cdCbo=" + cdCbo, null, true, null, executionDescription);
		}
    }
    else{
        var ok = parseInt(content, 10) > 0;
		if (ok) {
			if (checkboxTemp.checked) {
				var checkBoxes = gridCbos.getElementsColumn(2);
				for (var i=0; checkBoxes!=null && i<checkBoxes.length; i++) {
					if (checkBoxes[i].id != checkboxTemp.id)
						checkBoxes[i].checked = false;
				}
			}
            createTempbox("jMsg", {width: 300, height: 50,
                                   message: "Dados atualizados com sucesso.",
                                   tempboxType: "MESSAGE", time: 3000});
		}
		else
            createTempbox("jMsg", {width: 300, height: 50,
                                   message: "ERRO ao atualizar dados...",
                                   tempboxType: "ERROR", time: 3000});
    }
}

function loadCbos(content) {
	if (content==null && $('cdPessoa').value != 0) {
		getPage("GET", "loadCbos", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaFisicaServices"+
				"&method=getAllCbos(const " + $('cdPessoa').value + ":int)", null, true);
	}
	else {
		var rsmCbos = null;
		try {rsmCbos = eval('(' + content + ')')} catch(e) {}
		gridCbos = GridOne.create('gridCbos', {columns: columnsCbo,
					     resultset: rsmCbos, 
					     plotPlace: $('divGridCbos'),
					     onSelect: null});
	}
}

var cboTemp = null;
function btnNewCboOnClick(reg){
    if(!reg){
		if ($('cdPessoa').value == 0)
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Inclua ou localize um registro para adicionar CBOs.",
                                  msgboxType: "INFO"})
		else {
			filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar CBO", 
													   width: 550,
													   height: 345,
													   top:50,
													   modal: true,
													   noDrag: true,
													   className: "com.tivic.manager.grl.CboServices",
													   method: "findWithSinonimo",
													   allowFindAll: true,
													   filterFields: [[{label: "Nome da ocupação", reference: "A.NM_CBO", datatype: _VARCHAR, comparator: _LIKE_ANY, width: 85},
												 				       {label: "ID", reference: "A.ID_CBO", datatype: _VARCHAR, comparator: _EQUAL, width: 15}]],
													   gridOptions:{columns:[{label: "Sinônimo?", reference: "LG_SINONIMO_CALC"},
													   						 {label: "Nome da ocupação", reference: "NM_CBO"},
													                         {label: "ID", reference: "ID_CBO"}],
																    strippedLines: true,
																    columnSeparator: false,
																    lineSeparator: false,
																    onProcessRegister: function(reg) {
																		reg['LG_SINONIMO_CALC'] = reg['LG_SINONIMO'] == 0 ? 'Não' : 'Sim';
																	}},
													   hiddenFields: null,
													   callback: btnNewCboOnClick, 
													   autoExecuteOnEnter: true
											});
		}
    }
    else {// retorno
        filterWindow.close();
		cboTemp = reg[0];
		setTimeout('btnNewCboAuxOnClick()', 1);
    }
}

function btnNewCboAuxOnClick(content){
    if(content==null){
		var cdCbo = cboTemp["CD_CBO"];
		var nmCbo = cboTemp['NM_CBO'];
		var cdPessoa = $('cdPessoa').value;
		var pessoaDescription = $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value;
		var executionDescription = "Adição do Cbo " + nmCbo + " (Cod. " + cdCbo + ") ? relação de CBOs da Pessoa Física " + pessoaDescription;		
		getPage("GET", "btnNewCboAuxOnClick", "../methodcaller?className=com.tivic.manager.grl.CboPessoaFisicaDAO"+
												  "&method=insert(new com.tivic.manager.grl.CboPessoaFisica(cdPessoa: int, cdCbo: int, lgPrincipal: int):com.tivic.manager.grl.CboPessoaFisica)" +
												  "&cdPessoa=" + cdPessoa +
												  "&cdCbo=" + cdCbo +
												  "&lgPrincipal=0", null, true, null, executionDescription);
    }
    else{
        var ok = parseInt(content, 10) > 0;
		if (ok) {
			cboTemp['LG_PRINCIPAL'] = 0;
			gridCbos.addLine(0, cboTemp, null, true)	
		}
		else
            createTempbox("jMsg", {width: 200, height: 148,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
    }
}

function btnDeleteCboOnClick(content)	{
	if(content==null) {
		if (gridCbos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o CBO que deseja excluir.');
		else {
			var cdCbo = gridCbos.getSelectedRow().register['CD_CBO'];
			var nmCbo = gridCbos.getSelectedRow().register['NM_CBO'];
			var cdPessoa = $('cdPessoa').value;
			var pessoaDescription = $('nmPessoa').value.toUpperCase() + ", Cod. " + $('cdPessoa').value;
		    var executionDescription = "Remoção da ocupação " + nmCbo + " (Cod. " + cdCbo + ") da relação de ocupações da Pessoa " + pessoaDescription;	
			showConfirmbox('Manager', 300, 80, 'você tem certeza que deseja remover o CBO selecionado?', 
							function() {
								getPage('GET', 'btnDeleteCboOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.CboPessoaFisicaDAO'+
										'&method=delete(const ' + cdPessoa + ':int, const ' + cdCbo + ':int):int', null, true, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridCbos.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir Cbo.');
	}
}

/*****************************                   CONTATO                         *************************/
var columnsContatos = [{label:'Nome', reference: 'NM_PESSOA_CONTATO'}, {label:'Setor/Departamento', reference: 'NM_SETOR'}, 
                       {label:'Função/Cargo', reference: 'NM_FUNCAO'}, 
					   {label:'Telefone/Ramal', reference: 'NR_TELEFONE1', type: GridOne._MASK, mask: '(##)####-####'},
					   {label:'Celular', reference: 'NR_CELULAR', type: GridOne._MASK, mask: '(##)####-####'}];
var gridContatos = null;
var formContato  = null;

function formValidationContato(){
    if(!validarCampo($("nmContato"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome do Contato' deve ser preenchido.", true, null, null))
        return false;
    else
	    return true;
}

function clearFormContato(){
    $("dataOldContato").value = "";
    clearFields(contatoFields);
}

function btnNewContatoOnClick(){
    if ($('cdPessoa').value == 0)	{
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma empresa para cadastrar contatos.');
		return;
	}
    clearFormContato();
	gridContatos.unselectGrid();
	alterFieldsStatus(true, contatoFields);
	formContato = createWindow('jContato', {caption: "Contatos",
								  width: 496,
								  height: 250,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'contato'});
	$('nmContato').focus();
}

function btnShowContatosOnClick(){
	if ($('gnPessoa').value == <%=PessoaServices.TP_FISICA%>) {
		showMsgbox('Manager', 300, 50, 'O lançamento de contatos está disponível apenas para Pessoas Jurídicas.');
	}
	else if($('cdEndereco').value == 0)	{
		showMsgbox('Manager', 300, 50, 'Selecione um endereço para visualizar os contatos listados.');
	}
	else {
		formContato = createWindow('jContato', {caption: "Contatos", top: 50, width: 496, height: 200,
									  noDropContent: true,
									  modal: true,
									  noDrag: true,
									  contentDiv: 'contato'});
	}
}

function btnAlterContatoOnClick(){
	if(gridContatos.getSelectedRowRegister()==null)	{
		showMsgbox('Manager', 300, 50, 'Selecione o contato que deseja alterar.');
		return;
	}
	loadFormRegister(contatoFields, gridContatos.getSelectedRowRegister());
	formContato = createWindow('jContato', {caption: "Contatos",
								  width: 496,
								  height: 250,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'contato'});
    $('nmContato').focus();
}

function btnSaveContatoOnClick(content){
    if(content==null){
        if (formValidationContato()) {
            var executionDescription = $("cdContato").value>0 ? formatDescriptionUpdate("Contato", $("cdContato").value, $("dataOldContato").value, contatoFields) : formatDescriptionInsert("Contato", contatoFields);
            getPage("POST", "btnSaveContatoOnClick", "../methodcaller?className=com.tivic.manager.grl.PessoaContatoDAO"+
                    "&method="+($("cdContato").value<=0?"insert":"update")+"(new com.tivic.manager.grl.PessoaContato(cdContato: int, cdPessoaJuridica: int, nmContato: String, nmApelidoContato: String, nmSetorContato: String, nrTelefone1Contato: String, nrTelefone2Contato: String, nrCelularContato: String, nmEmailContato: String, nmUrlContato: String, dtCadastroContato: GregorianCalendar, txtObservacaoContato: String, cdFuncaoContato:int, cdEnderecoContato:int, cdUsuarioContato:int):com.tivic.manager.grl.PessoaContato)" +
					"&cdPessoaJuridica=" + $('cdPessoa').value, contatoFields, true, null, executionDescription);
        }
    }
    else{
        ok = parseInt(content, 10) > 0;
		$("cdContato").value = $("cdContato").value>0 ? $("cdContato").value<=0 : ok ? parseInt(content, 10) : $("cdContato").value;
        if(ok){
			var contatoRegister = {};
			for (var i=0; i<contatoFields.length; i++)
				if (contatoFields[i].name!=null && contatoFields[i].getAttribute("reference") != null)
					if (contatoFields[i].getAttribute("mask")!=null && (contatoFields[i].getAttribute("datatype")!='DATE' && contatoFields[i].getAttribute("datatype")!='DATETIME'))
						contatoRegister[contatoFields[i].getAttribute("reference").toUpperCase()] = changeLocale(contatoFields[i].id);
					else
						contatoRegister[contatoFields[i].getAttribute("reference").toUpperCase()] = contatoFields[i].getAttribute("lguppercase")!=null ? contatoFields[i].value.toUpperCase() : contatoFields[i].value;
			contatoRegister['NM_FUNCAO'] = getTextSelect('cdFuncaoContato', '');
			if (gridContatos.getSelectedRow()==null) {
				gridContatos.addLine(0, contatoRegister, null, true);
			}
			else if (gridContatos.getSelectedRow()) {
				gridContatos.updateSelectedRow(contatoRegister);
			}
            $("dataOldContato").value = captureValuesOfFields(contatoFields);
			formContato.close();
            createTempbox("jMsg", {width: 200, height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
        }
        else{
            createTempbox("jMsg", {width: 200, height: 50, message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteContatoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Contato", $("cdContato").value, $("dataOldContato").value);
    getPage("GET", "btnDeleteContatoOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.PessoaContatoDAO"+
            "&method=delete(const "+$("cdContato").value+":int,const "+$("cdPessoa").value+":int):int", null, true, null, executionDescription);
}

function btnDeleteContatoOnClick(content){
    if(content==null){
        if ($("cdContato").value == 0)
            createMsgbox("jMsg", {width: 300, height: 120, message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteContatoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 200, 
									height: 50, 
									message: "Registro excluído com sucesso!", 
									time: 3000});
            clearFormContato();
			gridContatos.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 350, height: 50, message: "Não foi possível excluir este registro! Provavelmente existem dados relacionados.", time: 5000});
    }	
}

function loadContatos(content) {
	if (content==null && $('cdPessoa').value != 0) {
		getPage("GET", "loadContatos", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaContatoServices"+
				"&objects=crtPessoa=java.util.ArrayList();"+
				"itemCodigo=sol.dao.ItemComparator(const cd_pessoa_juridica:String,const "+$('cdPessoa').value+
				":String,const "+_EQUAL+":int,const "+_INTEGER+":int);"+
				"&execute=crtPessoa.add(*itemCodigo:Object);"+
				"&method=find(*crtPessoa : java.util.ArrayList())", null, true);
	}
	else {
		var rsmContatos = null;
		try {rsmContatos = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmContatos!=null && i<rsmContatos.lines.length; i++)
			rsmContatos.lines[i]['CL_PRINCIPAL'] = rsmContatos.lines[i]['LG_PRINCIPAL']==1 ? 'Sim' : 'Não';
		gridContatos = GridOne.create('gridContatos', {columns: columnsContatos,
					     resultset: rsmContatos, 
					     plotPlace: $('divGridContatos')});
	}
}

/*
 ********************************************************************************************************************************
 ******************************************************** ATRIBUTOS *************************************************************
 ********************************************************************************************************************************
*/
var cdAtributoInEdicao = null;
var atributos = [];

function loadAtributos(content, cdFormulario) {
	if(content==null){
		getPage("POST", "loadAtributos", "../methodcaller?className=com.tivic.manager.grl.FormularioServices"+
										 "&method=getAllAtributos(const " + cdFormulario + ":int)");
    }
    else{
		var rsmAtributos = null;
		try { rsmAtributos = eval("(" + content + ")") } catch(e) {}
		for (var i = 0; rsmAtributos != null && i < rsmAtributos.lines.length; i++) {
			var cdFormularioAtributo = rsmAtributos.lines[i]['CD_FORMULARIO_ATRIBUTO'];
			var tpDado = rsmAtributos.lines[i]['TP_DADO'];
			var nrCasasDecimais = parseInt(rsmAtributos.lines[i]['NR_CASAS_DECIMAIS'], 10);
			var nmAtributo = rsmAtributos.lines[i]['NM_ATRIBUTO'];
			atributos.push({'cdFormularioAtributo': cdFormularioAtributo,
							'tpDado': tpDado,
							'nrCasasDecimais': nrCasasDecimais,
							'nmAtributo': nmAtributo});
			
			/* CAPTION */
			var caption = document.createElement("label");
			caption.className = "caption2";
			caption.style.width = "300px";
			caption.style.float = "left";
			caption.style.padding = "2px 0 0 0";
			caption.appendChild(document.createTextNode(nmAtributo));
			
			/* CONTROLE */
			var control = document.createElement(tpDado == <%=FormularioAtributoServices.TP_MEMO%> ? "textarea" : tpDado == <%=FormularioAtributoServices.TP_OPCOES%> ? "select" : "input");
			control.className = tpDado == <%=FormularioAtributoServices.TP_MEMO%> ? "textarea" : "field";
			if (tpDado != <%=FormularioAtributoServices.TP_BOOLEAN%> && tpDado != <%=FormularioAtributoServices.TP_PESSOA%>)
				control.style.width = tpDado == <%=FormularioAtributoServices.TP_DATA%> ? "330px" : "347px";
			if (tpDado == <%=FormularioAtributoServices.TP_MEMO%>)
				control.style.heitht = "50px";
			if (tpDado == <%=FormularioAtributoServices.TP_BOOLEAN%>)
				control.setAttribute('value', '1');
			control.setAttribute('idform', 'pessoa');
			control.setAttribute('logmessage', (tpDado == <%=FormularioAtributoServices.TP_PESSOA%> ? 'Código ' : '') + nmAtributo);
			control.setAttribute('reference', "atributo_" + cdFormularioAtributo);
			control.setAttribute('name', "atributo_" + cdFormularioAtributo);
			control.setAttribute('id', "atributo_" + cdFormularioAtributo);
			control.setAttribute('type', tpDado == <%=FormularioAtributoServices.TP_BOOLEAN%> ? "checkbox" : tpDado == <%=FormularioAtributoServices.TP_PESSOA%> ? 'hidden' : "text");

			/* CONTROLE VIEW (PARA CAMPOS HIDDEN) */
			var controlView = null;
			var controlClearView = null;
			var controlSearchView = null;
			if (tpDado == <%=FormularioAtributoServices.TP_PESSOA%>) {
				/* VIEW */
				controlView = document.createElement("input");
				controlView.className = "disabledField";
				controlView.style.width = "313px";
				controlView.setAttribute('idform', 'pessoa');
				controlView.setAttribute('disabled', 'disabled');
				controlView.setAttribute('logmessage', nmAtributo);
				controlView.setAttribute('reference', "atributo_pessoa_valor_" + cdFormularioAtributo);
				controlView.setAttribute('name', "atributo_pessoa_valor_" + cdFormularioAtributo);
				controlView.setAttribute('id', "atributo_pessoa_valor_" + cdFormularioAtributo);
				controlView.setAttribute('type', "text");
				
				/* CLEAR VIEW */
				controlClearView = document.createElement("button");
				controlClearView.className = "controlButton";
				controlClearView.cdFormularioAtributo = cdFormularioAtributo;
				controlClearView.onclick = function() {$("atributo_pessoa_valor_" + this.cdFormularioAtributo).value=''; $("atributo_" + this.cdFormularioAtributo).value=0 };
				controlClearView.setAttribute('idform', 'pessoa');
				controlClearView.setAttribute('title', "Limpar este campo...");
				controlClearView.style.padding = "0 -2px 0 0";
				controlClearView.style.right = "auto";
				var imgClearView = document.createElement("IMG");
				imgClearView.setAttribute('src', '/sol/imagens/clear-button.gif');
				controlClearView.appendChild(imgClearView);
				
				/* SEARCH VIEW */
				controlSearchView = document.createElement("button");
				controlSearchView.setAttribute('cdFormularioAtributo', cdFormularioAtributo);
				controlSearchView.className = "controlButton";
				controlSearchView.onclick = function() { cdAtributoInEdicao = this.getAttribute("cdFormularioAtributo");
														 filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar " + nmAtributo, 
																				   width: 550,
																				   height: 255,
																				   top:65,
																				   modal: true,
																				   noDrag: true,
																				   className: "com.tivic.manager.grl.PessoaDAO",
																				   method: "find",
																				   allowFindAll: true,
																				   filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
																				   gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}],
																							   strippedLines: true,
																							   columnSeparator: false,
																							   lineSeparator: false},
																				   hiddenFields: null,
																				   callback: controlSearchViewReturnOnClick, 
																				   autoExecuteOnEnter: true
																		});}
				controlSearchView.setAttribute('idform', 'pessoa');
				controlSearchView.setAttribute('title', "Pesquisar pessoa para este campo...");
				var imgSearchView = document.createElement("IMG");
				imgSearchView.setAttribute('src', '/sol/imagens/filter-button.gif');
				controlSearchView.appendChild(imgSearchView);
			}
			
			/* CALENDARIO */
			var controlCalendar = null;
			if (tpDado == <%=FormularioAtributoServices.TP_DATA%>) {
				controlCalendar = document.createElement("button");
				controlCalendar.className = "controlButton";
				controlCalendar.onclick = function() { return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br') };
				controlCalendar.setAttribute('idform', 'pessoa');
				controlCalendar.setAttribute('reference', 'atributo_' + cdFormularioAtributo);
				controlCalendar.setAttribute('title', "Selecionar data...");
				var imgCalendar = document.createElement("IMG");
				imgCalendar.setAttribute('src', '/sol/imagens/date-button.gif');
				controlCalendar.appendChild(imgCalendar);
			}
			
			/* DIV */
			var divControl = document.createElement("div");
			divControl.className = "element";
			divControl.style.width = tpDado == <%=FormularioAtributoServices.TP_DATA%> ? "649px" : "649px";
			divControl.style.padding = "2px 0 0 0";
			divControl.appendChild(caption);
			divControl.appendChild(control);
			if (controlCalendar != null)
				divControl.appendChild(controlCalendar);
			if (controlView != null)
				divControl.appendChild(controlView);
			if (controlClearView != null)
				divControl.appendChild(controlClearView);
			if (controlSearchView != null)
				divControl.appendChild(controlSearchView);
			
			$('divBodyAtributos').appendChild(divControl);
			if (tpDado == <%=FormularioAtributoServices.TP_OPCOES%>) {
				var optionControl = document.createElement("OPTION");
				optionControl.setAttribute('value', 0);
				optionControl.appendChild(document.createTextNode("Selecione..."));
				$("atributo_" + cdFormularioAtributo).appendChild(optionControl);
				getPage("GET", "loadOptionsAtributo", "../methodcaller?className=com.tivic.manager.grl.FormularioAtributoServices&method=getAllOpcoes(const " + cdFormularioAtributo + ":int)", null, null, cdFormularioAtributo);
			}

			if (tpDado == <%=FormularioAtributoServices.TP_FLOAT%> && nrCasasDecimais>0) {
				var maskNumber = "#,####."
				for (var j=0; j<nrCasasDecimais; j++)
					maskNumber += "0";
				var fieldMask = new Mask(maskNumber, "number");
   				fieldMask.attach(control);
			}
			else if (tpDado == <%=FormularioAtributoServices.TP_DATA%>) {
				var maskNumber = "##/##/####";
				var fieldMask = new Mask(maskNumber);
   				fieldMask.attach(control);
			}
		}
		initPessoa();
    }
}

function loadAtributosPessoa(content){
	if (content == null) {
		getPage("GET", "loadAtributosPessoa", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices" + 
				"&method=getAtributos(const " + $('cdPessoa').value + ":int, const " + $('cdEmpresa').value + ":int, const " + cdVinculo + ":int)");
	}
	else {
		var atributos = null;
		try {atributos = eval("(" + content + ")"); } catch(e) {}
		loadFormRegister(pessoaFields, atributos, false);
		$("dataOldPessoa").value = captureValuesOfFields(pessoaFields);
	}
}

function controlSearchViewReturnOnClick(reg) {
	closeWindow('jFiltro');
	$("atributo_" + cdAtributoInEdicao).value = reg[0]['CD_PESSOA'];
	$("atributo_pessoa_valor_" + cdAtributoInEdicao).value = reg[0]['NM_PESSOA'];
}

function loadOptionsAtributo(content, cdFormularioAtributo) {
	var optionsAtributo = null;
	try { optionsAtributo = eval("(" + content + ")"); } catch(e) {}
	for (var i = 0; optionsAtributo != null && i < optionsAtributo.lines.length; i++) {
		var optionControl = document.createElement("OPTION");
		optionControl.setAttribute('value', optionsAtributo.lines[i]['CD_OPCAO']);
		optionControl.appendChild(document.createTextNode(optionsAtributo.lines[i]['TXT_OPCAO']));
		$("atributo_" + cdFormularioAtributo).appendChild(optionControl);
	}
}
</script>
</head>
<body class="body" onload="init();">
<iframe id="frameHidden" name="frameHidden" style="display:none"></iframe>
<div style="width: 700px;" id="pessoa" class="d1-form">
  <div style="width: 700px; height: 405px;" class="d1-body">
    <input idform="" reference="" id="dataOldDocumento" name="dataOldDocumento" type="hidden">
    <input idform="" reference="" id="dataOldEndereco" name="dataOldEndereco" type="hidden">
    <input idform="" reference="" id="dataOldPessoa" name="dadaOldPessoa" type="hidden">
    <input idform="" reference="" id="dataOldOcorrencia" name="dataOldOcorrencia" type="hidden">
    <input idform="" reference="" id="dataOldVinculo" name="dataOldVinculo" type="hidden">
    <input idform="" reference="" id="dataOldArquivo" name="dataOldArquivo" type="hidden">
    <input idform="" reference="" id="dataOldDependente" name="dataOldDependente" type="hidden">
    <input idform="" reference="" id="cdUsuario" name="cdUsuario" type="hidden" value="<%=cdUsuario%>" defaultvalue="<%=cdUsuario%>" />
    <input idform="" reference="" id="tpEnderecamento" name="tpEnderecamento" type="hidden" value="<%=tpEnderecamento%>" defaultvalue="<%=tpEnderecamento%>" />
    <input idform="" reference="" id="cdVinculoDefault" name="cdVinculoDefault" type="hidden" value="<%=cdVinculo%>" defaultvalue="<%=cdVinculo%>" />
    <input idform="" reference="cd_vinculo" id="cdVinculoOld" name="cdVinculoOld" type="hidden" value="0" defaultValue="0">
    <input idform="" reference="" id="cdPessoaUsuario" name="cdPessoaUsuario" type="hidden" value="<%=cdPessoaUsuario%>" defaultvalue="<%=cdPessoaUsuario%>" />
    <input idform="" reference="" id="nmPessoaUsuario" name="nmPessoaUsuario" type="hidden" value="<%=nmPessoaUsuario%>" defaultvalue="<%=nmPessoaUsuario%>" />
    <input idform="" reference="cd_regiao" id="cdRegiaoOld" name="cdRegiaoOld" type="hidden" value="0" defaultValue="0">
    <input idform="documento" reference="cd_tipo_documento" id="cdTipoDocumentoOld" name="cdTipoDocumentoOld" type="hidden" value="0" defaultValue="0">
    <input idform="endereco" reference="cd_endereco" id="cdEndereco" name="cdEndereco" type="hidden" value="0" defaultValue="0">
    <input idform="endereco" reference="nm_ponto_referencia" id="nmPontoReferenciaAgd" name="nmPontoReferenciaAgd" type="hidden" value=""/>
    <input idform="endereco" reference="ds_endereco" id="dsEnderecoAgd" name="dsEnderecoAgd" type="hidden" value="">
    <input idform="endereco" reference="lg_cobranca" id="lgCobrancaAgd" name="lgCobrancaAgd" type="hidden" value="0" defaultValue="0">
    <input idform="endereco" reference="nm_tipo_logradouro_enderecamento" id="nmTipoLogradouroEnderecamentoAgd" name="nmTipoLogradouroEnderecamentoAgd" type="hidden">
    <input idform="endereco" reference="sg_tipo_logradouro_enderecamento" id="sgTipoLogradouroEnderecamentoAgd" name="sgTipoLogradouroEnderecamentoAgd" type="hidden">
    <input idform="endereco" reference="nm_logradouro_enderecamento" id="nmLogradouroEnderecamentoAgd" name="nmLogradouroEnderecamentoAgd" type="hidden">
    <input idform="endereco" reference="nm_tipo_logradouro_enderecamento" id="nmTipoLogradouroEnderecamento" name="nmTipoLogradouroEnderecamento" type="hidden">
    <input idform="endereco" reference="sg_tipo_logradouro_enderecamento" id="sgTipoLogradouroEnderecamento" name="sgTipoLogradouroEnderecamento" type="hidden">
    <input idform="endereco" reference="nm_bairro_enderecamento" id="nmBairroEnderecamento" name="nmBairroEnderecamento" type="hidden">
    <input idform="endereco" reference="nm_logradouro_enderecamento" id="nmLogradouroEnderecamento" name="nmLogradouroEnderecamento" type="hidden">
    <input idform="endereco" reference="sg_estado" id="sgEstado" name="sgEstado" type="hidden">
    <input idform="pessoa" reference="cd_pessoa" id="cdPessoa" name="cdPessoa" type="hidden" defaultValue="0">
    <input idform="pessoa" reference="cd_pessoa_superior" id="cdPessoaSuperior" name="cdPessoaSuperior" type="hidden" value="0" defaultValue="0">
    <input idform="pessoa" logmessage="Codigo Classificação Financeira" reference="cd_classificacao" id="cdClassificacao" name="cdClassificacao" type="hidden" value="0" defaultValue="0">
    <input idform="pessoa" reference="lg_notificacao" id="lgNotificacao" name="lgNotificacao" type="hidden" value="0" defaultValue="0">
    <input idform="pessoa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>">
    <input idform="pessoa" id="cdVinculoOld" name="cdVinculoOld" type="hidden" value="0" defaultValue="0">
    <input idform="pessoa" reference="cd_matricula" id="cdMatricula" name="cdMatricula" type="hidden">
    <input idform="pessoa" reference="cd_endereco" id="cdEnderecoPessoa" name="cdEnderecoPessoa" type="hidden" value="0" defaultValue="0">
    <input idform="pessoa" reference="lg_endereco" id="lgEnderecoPrincipal" name="lgEnderecoPrincipal" type="hidden" value="1" defaultValue="1">
    <input idform="pessoa" reference="ds_endereco" id="dsEnderecoPrincipal" name="dsEnderecoPrincipal" type="hidden">
    <input idform="pessoa" reference="lg_cobranca" id="lgCobrancaPessoa" name="lgCobrancaPessoa" type="hidden" value="0" defaultValue="0">
    <input idform="pessoa" reference="blb_biometria" id="blbBiometria" name="blbBiometria" type="hidden">
    <input idform="pessoa" reference="dt_termino_atividade" id="dtTerminoAtividade" name="dtTerminoAtividade" type="hidden">
    <input idform="ocorrencia" reference="cd_ocorrencia" id="cdOcorrencia" name="cdOcorrencia" type="hidden" value="0" defaultValue="0" />
    <input idform="ocorrencia" reference="cd_sistema" id="cdSistema" name="cdSistema" type="hidden" value="0" defaultValue="0">
    <input idform="ocorrencia" reference="cd_contrato" id="cdContrato" name="cdContrato" type="hidden" value="0" defaultValue="0" />
    <input idform="arquivo" reference="cd_arquivo" id="cdArquivo" name="cdArquivo" type="hidden" value="0" defaultValue="0">
    <input idform="arquivo" reference="cd_setor" id="cdSetorArquivo" name="cdSetorArquivo" type="hidden" value="0" defaultValue="0">
    <input idform="arquivo" reference="nm_arquivo" id="nmArquivo" name="nmArquivo" type="hidden">
    <input idform="dependentes" reference="cd_dependente" id="cdDependente" name="cdDependente" type="hidden" value="0" defaultValue="0">
    <input idform="dependentes" reference="st_cadastro" id="stCadastroDependente" name="stCadastroDependente" type="hidden" value="1" defaultValue="1">
    <input idform="movimentacao" reference="cd_movimentacao" id="cdMovimentacao" name="cdMovimentacao" type="hidden" value="0" defaultValue="0">
    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 690px;"></div>
    <div id="divTabPessoa">
      <div id="divAbaDadosBasicos" style="display:none;">
        <div style="width: 90px; float:right;" class="element">
          <label class="caption" id="labelImagem">Foto/Logomarca</label>
		  <input idform="pessoa" reference="img_foto" id="imgFoto" name="imgFoto" type="hidden">
          <iframe scrolling="auto" id="imagePessoa" style="border:1px solid #000000; background-color:#FFF; margin:0px 0px 0px 0px; width:87px; height:59px;" src="about:blank" frameborder="0">&nbsp;</iframe>
          <button idform="pessoa" onclick="btLoadImagemOnClick()" style="margin:0px; left:0px; bottom:-16px; width:69px; font-size:9px" title="Carregar imagem de arquivo..." class="controlButton"><img style="margin:0px 0px 0px -6px;" alt="X" src="/sol/imagens/filter-button.gif"/>&nbsp;&nbsp;&nbsp;&nbsp;Imagem...</button>
          <button idform="pessoa" onclick="btClearImagemOnClick()" style="bottom:-16px; width:21px;" title="Limpar este campo" class="controlButton"><img style="margin:-7px 0px 0px -7px;" alt="X" src="/sol/imagens/clear-button.gif"/></button>
        </div>
        <div class="d1-line" id="line0">
          <div style="width: 70px;" class="element">
            <label class="caption" for="gnPessoa">Tipo</label>
            <select style="width: 67px;" idform="pessoa" logmessage="Tipo de pessoa" reference="gn_pessoa" datatype="INT" id="gnPessoa" name="gnPessoa" onchange="onChangeGnPessoa(this.value);" <%=tpPessoa==2 ? "defaultValue=\"" + PessoaServices.TP_FISICA + "\"" : ""%> <%=tpPessoa!=2?"static=\'true\' defaultvalue=\'"+tpPessoa+"\' disabled=\'disabled\' class=\'disabledSelect\' ":"class=\'select\'"%>>
            	<option value="1" selected="selected">Física</option>
            	<option value="0">Jurídica</option>
            </select>
          </div>
          <div style="width: 60px;" class="element" id="nmFormaTratamentoElement">
            <label class="caption" for="nmFormaTratamento">Tratamento</label>
            <select style="width: 57px;" class="select" idform="pessoa" logmessage="Forma de tratamento" reference="nm_forma_tratamento" datatype="STRING" id="nmFormaTratamento" name="nmFormaTratamento" defaultvalue="Sr(a)">
            </select>
          </div>
          <div style="width: 295px;" class="element" id="nmPessoaElement">
            <label class="caption" for="nmPessoa" id="labelNmPessoa">Nome</label>
            <input style="text-transform: uppercase; width: 292px;" lguppercase="true" logmessage="Nome" class="field" idform="pessoa" reference="nm_pessoa" datatype="STRING" maxlength="50" id="nmPessoa" name="nmPessoa" type="text"/>
          </div>
          <div style="width: 151px;" class="element">
            <label class="caption" for="nmApelido">Apelido</label>
            <input style="text-transform: uppercase; width: 148px;" lguppercase="true" logmessage="Apelido" class="field" idform="pessoa" reference="nm_apelido" datatype="STRING" maxlength="30" id="nmApelido" name="nmApelido" type="text"/>
          </div>
          <div style="width: 65px;" class="element">
            <label class="caption" for="dtCadastro">Cadastro</label>
            <input style="width: 65px;" mask="##/##/####" static="true" logmessage="Data Cadastro" class="disabledField" disabled="disabled" idform="pessoa" reference="dt_cadastro" datatype="DATE" maxlength="10" id="dtCadastro" name="dtCadastro" type="text" defaultvalue="%DATE"/>
          </div>
        </div>
        <div class="d1-line" id="line4">
          <div style="width: 80px;" class="element">
            <label class="caption" for="tpSexo">Sexo</label>
            <select style="width: 77px;" class="select" logmessage="Sexo" idform="pessoa" reference="tp_sexo" datatype="STRING" id="tpSexo" name="tpSexo">
              <option value="-1">Selecione...</option>
            </select>
          </div>
          <div style="width: 90px;" class="element">
            <label class="caption" for="dtNascimento">Data nascimento</label>
            <input style="width: 87px;" mask="##/##/####" maxlength="10" logmessage="Data Nascimento" class="field" idform="pessoa" reference="dt_nascimento" datatype="DATETIME" id="dtNascimento" name="dtNascimento" type="text"/>
            <button idform="pessoa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtNascimento" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
          </div>
          <div style="width: 200px;" class="element">
            <label class="caption" for="cdNaturalidade">Naturalidade</label>
            <input logmessage="Código Cidade Naturalidade" idform="pessoa" reference="cd_naturalidade" datatype="STRING" id="cdNaturalidade" name="cdNaturalidade" type="hidden"/>
            <input logmessage="Nome Cidade Naturalidade" idform="pessoa" style="width: 195px;" static="static" reference="nm_naturalidade" disabled="disabled" class="disabledField" name="cdNaturalidadeView" id="cdNaturalidadeView" type="text"/>
            <button id="btnFindCdNaturalidade" idform="pessoa" onclick="btnFindCdNaturalidadeOnClick()" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif" /></button>
            <button idform="pessoa" onclick="btnClearCdNaturalidadeOnClick()" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
          </div>
          <div style="width: 132px;" class="element">
            <label class="caption" for="stEstadoCivil">Estado civil</label>
            <select style="width: 130px;" class="select" logmessage="Estado Civil" registerclearlog="-1" idform="pessoa" reference="st_estado_civil" datatype="STRING" id="stEstadoCivil" name="stEstadoCivil" defaultValue="0">
              <option value="-1">Selecione...</option>
            </select>
          </div>
        </div>
        <div style="width: 87px;" class="element" id="situacao">
	      <label class="caption" for="stCadastro">Situação</label>
	      <select style="width: 85px;" class="select" logmessage="Situação" idform="pessoa" reference="st_cadastro" datatype="SMALLINT" id="stCadastro" name="stCadastro" defaultValue="1">
	        <option value="-1">Selecione...</option>
	        <option value="0">Inativo</option>
	        <option value="1">Ativo</option>
	      </select>
        </div>
        <div class="d1-line" id="line5">
          <div style="width: 203px;" class="element">
            <label class="caption" for="nmPai">Nome pai</label>
            <input style="text-transform: uppercase; width: 200px;" lguppercase="true" logmessage="Nome Pai" class="field" idform="pessoa" reference="nm_pai" datatype="STRING" maxlength="50" id="nmPai" name="nmPai" type="text"/>
          </div>
          <div style="width: 203px;" class="element">
            <label class="caption" for="nmMae">Nome mãe</label>
            <input style="text-transform: uppercase; width: 200px;" lguppercase="true" logmessage="Nome M?e" class="field" idform="pessoa" reference="nm_mae" datatype="STRING" maxlength="50" id="nmMae" name="nmMae" type="text"/>
          </div>
          <div style="width: 182px; height: 30px;" class="element">
            <label class="caption" for="cdEscolaridade">Escolaridade</label>
            <select logmessage="Código Escolaridade" idform="pessoa" reference="cd_escolaridade" style="width: 182px;" class="select" name="cdEscolaridade" id="cdEscolaridade">
              <option value="0">Selecione...</option>
            </select>
          </div>
        </div>
        <div class="d1-line" id="line6">
          <div style="width: 96px;" class="element">
            <label class="caption" for="nrCpf">CPF</label>
            <input style="text-transform: uppercase; width: 93px;" lguppercase="true" mask="###.###.###-##" logmessage="C.P.F." class="field" idform="pessoa" reference="nr_cpf" datatype="STRING" maxlength="14" id="nrCpf" name="nrCpf" type="text" onblur=""/>
          </div>
          <div style="width: 101px;" class="element">
            <label class="caption" for="nrRg">Identidade</label>
            <input style="text-transform: uppercase; width: 98px;" logmessage="Identidade" lguppercase="true" nologcasevalue="Identidade" class="field" idform="pessoa" reference="nr_rg" datatype="STRING" maxlength="15" id="nrRg" name="nrRg" type="text"/>
          </div>
          <div style="width: 77px;" class="element">
            <label class="caption" for="dtEmissaoRg">Data emissão</label>
            <input style="width: 74px;" mask="##/##/####" maxlength="10" logmessage="Data Emiss?o Rg" class="field" idform="pessoa" reference="dt_emissao_rg" datatype="DATETIME" id="dtEmissaoRg" name="dtEmissaoRg" type="text"/>
            <button idform="pessoa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtEmissaoRg" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
          </div>
          <div style="width: 71px;" class="element">
            <label class="caption" for="sgOrgaoRg">Orgão exp.</label>
            <select style="width: 68px;" class="select" idform="pessoa" logmessage="Org?o Expedidor" reference="sg_orgao_rg" datatype="STRING" id="sgOrgaoRg" name="sgOrgaoRg" defaultvalue="">
            	<option value="" selected="selected">...</option>
            </select>
          </div>
          <div style="width: 43px;" class="element">
            <label class="caption" for="cdEstadoRg">UF RG</label>
            <select style="width: 40px;" class="select" idform="pessoa" logmessage="Estado Emissor RG" reference="cd_estado_rg" datatype="INT" id="cdEstadoRg" name="cdEstadoRg" defaultvalue="0">
            	<option value="0" selected="selected">...</option>
            </select>
          </div>
          <div style="width: 80px;" class="element">
            <label class="caption" for="nrCnh">N&deg; CNH</label>
            <input style="width: 77px;" lguppercase="true" logmessage="N¦ C.N.H." class="field" idform="pessoa" reference="nr_cnh" datatype="STRING" maxlength="10" id="nrCnh" name="nrCnh" type="text"/>
          </div>
          <div style="width: 80px;" class="element">
            <label class="caption" for="dtPrimeiraHabilitacao">1ª Habilitação</label>
            <input logmessage="Data 1ª Habilitação" style="width: 77px;" mask="##/##/####" maxlength="10" class="field" idform="pessoa" reference="dt_primeira_habilitacao" datatype="DATETIME" id="dtPrimeiraHabilitacao" name="dtPrimeiraHabilitacao" type="text"/>
            <button idform="pessoa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtPrimeiraHabilitacao" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"/></button>
          </div>
          <div style="width: 77px;" class="element">
            <label class="caption" for="dtValidadeCnh">Validade CNH</label>
            <input logmessage="Validade Habilitação" style="width: 74px;" mask="##/##/####" maxlength="10" class="field" idform="pessoa" reference="dt_validade_cnh" datatype="DATETIME" id="dtValidadeCnh" name="dtValidadeCnh" type="text"/>
            <button idform="pessoa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtValidadeCnh" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"/></button>
          </div>
          <div style="width: 57px;" class="element">
            <label class="caption" for="tpCategoriaHabilitacao">Categoria</label>
            <select logmessage="Categoria Habilitação" registerclearlog="-1" style="width: 57px;" class="select" idform="pessoa" reference="tp_categoria_habilitacao" datatype="STRING" id="tpCategoriaHabilitacao" name="tpCategoriaHabilitacao">
              <option value="-1" selected="selected">...</option>
            </select>
          </div>
        </div>
        <div class="d1-line" id="line8">
          <div style="width: 201px;" class="element">
            <label class="caption" for="nmRazaoSocial">Razão social</label>
            <input style="text-transform: uppercase; width: 198px;" lguppercase="true" logmessage="Raz?o Social" class="field" idform="pessoa" reference="nm_razao_social" datatype="STRING" maxlength="50" id="nmRazaoSocial" name="nmRazaoSocial" type="text"/>
          </div>
          <div style="width: 105px;" class="element">
            <label class="caption" for="nrCnpj">CNPJ/MF</label>
            <input style="text-transform: uppercase; width: 102px;" lguppercase="true" mask="##.###.###/####-##" logmessage="C.N.P.J." class="field" idform="pessoa" reference="nr_cnpj" datatype="STRING" maxlength="18" id="nrCnpj" name="nrCnpj" type="text" onblur=""/>
          </div>
          <div style="width: 106px;" class="element">
            <label class="caption" for="nrInscricaoMunicipal">Nº Inscrição Municipal</label>
            <input style="text-transform: uppercase; width: 102px;" lguppercase="true" logmessage="Nº Inscrição Municipal" class="field" idform="pessoa" reference="nr_inscricao_municipal" datatype="STRING" maxlength="15" id="nrInscricaoMunicipal" name="nrInscricaoMunicipal" type="text"/>
          </div>
          <div style="width: 105px;" class="element">
            <label class="caption" for="nrInscricaoEstadual">Nº Inscrição Estadual</label>
            <input style="text-transform: uppercase; width: 105px;" lguppercase="true" logmessage="Nº Inscrição Estadual" class="field" idform="pessoa" reference="nr_inscricao_estadual" datatype="STRING" maxlength="15" id="nrInscricaoEstadual" name="nrInscricaoEstadual" type="text"/>
          </div>
        </div>
        <div class="d1-line" id="line9">
          <div style="width: 90px;" class="element">
            <label class="caption" for="nrTempoAtividade">Início atividade</label>
            <input style="width: 87px;" lguppercase="true" logmessage="In?cio da Atividade" class="field" idform="pessoa" reference="dt_inicio_atividade" mask="##/##/####" datatype="DATE" id="dtInicioAtividade" name="dtInicioAtividade" type="text"/>
            <button idform="pessoa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'BR')" title="Selecionar data..." reference="dtInicioAtividade" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
          </div>
          <div style="width: 80px;" class="element">
            <label class="caption" for="nrFuncionarios">Nº Funcionários</label>
            <input style="text-transform: uppercase; width: 77px;" lguppercase="true" logmessage="N¦ Funcion?rios" class="field" idform="pessoa" reference="nr_funcionarios" datatype="STRING" id="nrFuncionarios" name="nrFuncionarios" type="text"/>
          </div>
          <div style="width: 110px;" class="element">
            <label class="caption" for="tpEmpresa">Porte da empresa</label>
            <select logmessage="Porte da Empresa" registerclearlog="0" style="width: 107px;" class="select" idform="pessoa" reference="tp_empresa" datatype="INT" id="tpEmpresa" name="tpEmpresa" defaultValue="-1">
              <option value="-1">Selecione...</option>
            </select>
          </div>
          <div style="width: 309px;" class="element">
            <label class="caption" for="cdNaturezaJuridica">Natureza jurídica</label>
            <input logmessage="Natureza Jurídica" idform="pessoa" reference="cd_natureza_juridica" datatype="STRING" id="cdNaturezaJuridica" name="cdNaturezaJuridica" type="hidden"/>
            <input logmessage="Natureza Jurídica" style="width: 305px;" idform="pessoa" reference="nm_natureza_juridica" static="true" disabled="disabled" class="disabledField" name="nmNaturezaJuridica" id="nmNaturezaJuridica" type="text"/>
            <button id="btnClearNaturezaJuridica" title="Limpar este campo..." onclick="$('cdNaturezaJuridica').value=0; $('nmNaturezaJuridica').value='';" class="controlButton" idform="pessoa"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
            <button id="btnFindNaturezaJuridica" onclick="btnFindNaturezaJuridicaOnClick()" title="Pesquisar valor para este campo..." idform="pessoa" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
          </div>
        </div>
        <div class="d1-line" id="line1">
          <div style="width: 231px;" class="element">
            <label class="caption" for="cdPais">País de origem</label>
            <input logmessage="Código Pa?s" idform="pessoa" reference="cd_pais" datatype="STRING" id="cdPais" name="cdPais" type="hidden"/>
            <input logmessage="Nome Pa?s" style="width: 228px;" static="true" disabled="disabled" class="disabledField" name="cdPaisView" reference="nm_pais" idform="pessoa" id="cdPaisView" type="text"/>
            <button id="btnPesquisarPais" onclick="btnFindPaisOnClick()" title="Pesquisar valor para este campo..." idform="pessoa" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
            <button id="btnClearPais" title="Limpar este campo..." onclick="btnClearPaisOnClick()" class="controlButton" idform="pessoa"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
          </div>
          <div style="width: 90px;" class="element">
            <label class="caption" for="dtChegadaPais">Data chegada</label>
            <input style="width: 87px;" mask="##/##/####" maxlength="10" logmessage="Data Chegada" class="field" idform="pessoa" reference="dt_chegada_pais" datatype="DATETIME" id="dtChegadaPais" name="dtChegadaPais" type="text"/>
            <button idform="pessoa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtChegadaPais" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
          </div>
          <div style="width: 90px;" class="element">
            <label class="caption" for="nrTelefone1">Telefones</label>
            <input style="width: 87px;" lguppercase="true" mask="(##)####-####" logmessage="N¦ Telefone 1" class="field" idform="pessoa" reference="nr_telefone1" datatype="STRING" maxlength="15" id="nrTelefone1" name="nrTelefone1" type="text"/>
          </div>
          <div style="width: 90px;" class="element">
            <label class="caption" for="nrTelefone2">&nbsp;</label>
            <input style="width: 87px;" lguppercase="true" mask="(##)####-####" logmessage="N¦ Telefone 2" class="field" idform="pessoa" reference="nr_telefone2" datatype="STRING" maxlength="15" id="nrTelefone2" name="nrTelefone2" type="text"/>
          </div>
          <div style="width: 90px;" class="element">
            <label class="caption" for="nrFax">Fax</label>
            <input style="width: 87px;" lguppercase="true" mask="(##)####-####" logmessage="N¦ Fax" class="field" idform="pessoa" reference="nr_fax" datatype="STRING" maxlength="15" id="nrFax" name="nrFax" type="text"/>
          </div>
          <div style="width: 90px;" class="element">
            <label class="caption" for="nrCelular">Celular</label>
            <input style="width: 87px;" lguppercase="true" mask="(##)####-####" logmessage="N¦ Celular" class="field" idform="pessoa" reference="nr_celular" datatype="STRING" maxlength="15" id="nrCelular" name="nrCelular" type="text"/>
          </div>
        </div>
        <div class="d1-line" id="line1">
          <div style="width: 173.5px;" class="element">
            <label class="caption" for="nmEmail">E-mail</label>
            <input style="text-transform: lowercase; width: 170.5px;" logmessage="E-mail" class="field" idform="pessoa" reference="nm_email" datatype="STRING" maxlength="256" id="nmEmail" name="nmEmail" type="text"/>
          </div>
          <div style="width: 175px;" class="element">
            <label class="caption" for="nmUrl">Homepage</label>
            <input style="text-transform: lowercase; width: 172px;" logmessage="Homepage" class="field" idform="pessoa" reference="nm_url" datatype="STRING" maxlength="256" id="nmUrl" name="nmUrl" type="text"/>
          </div>
          <% if (cdEmpresa!=0 && cdVinculo > 0) { %>
              <div style="width: 137px;" class="element">
                <label class="caption" for="cdVinculo">Vínculo</label>
                <select static="static" disabled="disabled" style="width: 134px;" class="disabledSelect" idform="pessoa" logmessage="Vínculo" datatype="STRING" id="cdVinculo" name="cdVinculo">
                  <option value="0">Selecione...</option>
                </select>
              </div>
          <% } %>
          <div style="width: <%=cdEmpresa != 0 && cdVinculo > 0 ? 115 : 252%>px;" class="element">
            <label class="caption" for="cdFormaDivulgacao">Como nos conheceu</label>
            <select logmessage="Forma de Divulgação" registerclearlog="0" style="width: <%=cdEmpresa != 0 && cdVinculo > 0 ? 112 : 249%>px;" class="select" idform="pessoa" reference="cd_forma_divulgacao" datatype="STRING" id="cdFormaDivulgacao" name="cdFormaDivulgacao" defaultValue="<%=cdVinculo%>">
              <option value="0">Selecione...</option>
            </select>
          </div>
          <div style="width: 80px;" class="element">
            <label class="caption" for="idPessoa">ID</label>
            <input style="width: 77px;" class="field" idform="pessoa" reference="id_pessoa" datatype="STRING" id="idPessoa" name="idPessoa" type="text" maxlength="20"/>
          </div>
        </div>
        <div class="d1-line" id="divGridEnderecosSuperior" style="display: none">
          <div style="width: 658px;" class="element">
            <label class="caption">Endere&ccedil;os</label>
            <div id="divGridEnderecos" style="width: 655px; background-color:#FFF; height:68px; border:1px solid #000000">&nbsp;</div>
          </div>
          <div style="width: 20px;" class="element">
            <label class="caption">&nbsp;</label>
              <button title="Novo endereço" onclick="btnNewEnderecoOnClick();" style="margin-bottom:2px" id="btnNewEndereco" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
              <button title="Alterar endereço" onclick="btnAlterEnderecoOnClick();" style="margin:0 0 2px 1px" id="btnAlterEndereco" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
              <button title="Excluir endereço" onclick="btnDeleteEnderecoOnClick();" style="margin:0 0 2px 1px" id="btnDeleteEndereco" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
          </div>
        </div>
        <div class="d1-line" id="divEnderecosSuperior" style="display: none">
          <div style="position:relative; border:1px solid #999; float:left; padding:2px; margin-top:7px; margin-right:0px">
            <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:2px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; width:50px;">Endere&ccedil;o</div>
            <div style="width: 651px; height:58px; padding:0 0 2px 0" class="element" id="divBodyEndereco">
              <div class="d1-line" id="line1">
                <div id="divFieldsPessoaEndereco1" style="display:none">
                  <div style="width: 354.5px;" class="element">
                    <label class="caption" for="cdLogradouroPessoa">Logradouro</label>
                    <input logmessage="Código Logradouro" idform="pessoa" reference="cd_logradouro" datatype="STRING" id="cdLogradouroPessoa" name="cdLogradouroPessoa" type="hidden"/>
                    <input logmessage="Nome Logradouro" idform="pessoa" reference="nm_logradouro_formatado" style="width: 351.5px;" static="true" disabled="disabled" class="disabledField" name="cdLogradouroPessoaView" id="cdLogradouroPessoaView" type="text"/>
                    <button id="btnFindLogradouro" name="btnFindLogradouro" onclick="btnFindLogradouroOnClick()" idform="pessoa" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                    <button onclick="$('cdLogradouroPessoa').value = 0; $('cdLogradouroPessoaView').value = ''" idform="pessoa" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
                  </div>
                  <div style="width: 96px;" class="element">
                    <label class="caption" for="nrEnderecoPessoa1">N&deg;</label>
                    <input style="width: 96px;" lguppercase="true" logmessage="N¦ Porta" class="field" idform="pessoa" reference="nr_endereco" datatype="STRING" maxlength="10" id="nrEnderecoPessoa1" name="nrEnderecoPessoa1" type="text"/>
                  </div>
                  <div style="width: 127px;" class="element">
                    <label class="caption" for="nmComplementoPessoa1">Complemento</label>
                    <input style="text-transform: uppercase; width: 127px;" lguppercase="true" logmessage="Complemento" class="field" idform="pessoa" reference="nm_complemento" datatype="STRING" maxlength="50" id="nmComplementoPessoa1" name="nmComplementoPessoa1" type="text"/>
                  </div>
                </div>
                <div id="divFieldsPessoaEndereco2" style="display:<%=1!=1 ? "none" : ""%>">
                  <div style="width: 90px;" class="element">
                    <label class="caption" for="cdTipoEnderecoPrincipal">Tipo endereço</label>
                    <select logmessage="Tipo endereço" registerclearlog="0" style="width: 87px;" class="select" idform="pessoa" reference="cd_tipo_endereco" datatype="STRING" id="cdTipoEnderecoPrincipal" name="cdTipoEnderecoPrincipal" defaultValue="0">
                      <option value="0">Selecione...</option>
                    </select>
                  </div>
                  <div style="width: 106px;" class="element">
                    <label class="caption" for="cdTipoLogradouroPessoa">Tipo logradouro</label>
                    <select logmessage="Tipo Logradouro" registerclearlog="0" style="width: 103px;" class="select" idform="pessoa" reference="cd_tipo_logradouro" datatype="STRING" id="cdTipoLogradouroPessoa" name="cdTipoLogradouroPessoa" defaultValue="0">
                      <option value="0">Selecione...</option>
                    </select>
                  </div>
                  <div style="width: 183px;" class="element">
                    <label class="caption" for="nmLogradouroPessoa">Logradouro</label>
                    <input style="text-transform: uppercase; width: 180px;" lguppercase="true" logmessage="Nome Logradouro (livre digitação)" class="field" idform="pessoa" reference="nm_logradouro" datatype="STRING" maxlength="100" id="nmLogradouroPessoa" name="nmLogradouroPessoa" type="text"/>
                  </div>
                  <div style="width: 57px;" class="element">
                    <label class="caption" for="nrEnderecoPessoa2">N&deg;</label>
                    <input style="width: 54px;" lguppercase="true" logmessage="N¦ Porta" class="field" idform="pessoa" reference="nr_endereco" datatype="STRING" maxlength="10" id="nrEnderecoPessoa2" name="nrEnderecoPessoa2" type="text"/>
                  </div>
                  <div style="width: 118px;" class="element">
                    <label class="caption" for="nmComplementoPessoa2">Complemento</label>
                    <input style="text-transform: uppercase; width: 115px;" lguppercase="true" logmessage="Complemento" class="field" idform="pessoa" reference="nm_complemento" datatype="STRING" maxlength="50" id="nmComplementoPessoa2" name="nmComplementoPessoa2" type="text"/>
                  </div>
                  <div style="width: 92px;" class="element">
                    <label class="caption" for="nmBairroPessoa">Bairro</label>
                    <input style="text-transform: uppercase; width: 92px;" lguppercase="true" logmessage="Bairro (livre digitação)" class="field" idform="pessoa" reference="nm_bairro" datatype="STRING" maxlength="50" id="nmBairroPessoa" name="nmBairroPessoa" type="text"/>
                  </div>
                </div>
              </div>
              <div class="d1-line" id="line1">
                <div class="d1-line" id="divFieldsPessoaEndereco3">
                  <div style="width: 382px;" class="element">
                    <label class="caption" for="cdBairroPessoa">Bairro</label>
                    <input logmessage="Código Bairro" idform="pessoa" reference="cd_bairro" datatype="STRING" id="cdBairroPessoa" name="cdBairroPessoa" type="hidden"/>
                    <input logmessage="Nome Bairro" idform="pessoa" reference="nm_bairro_enderecamento" style="width: 379px;" static="true" disabled="disabled" class="disabledField" name="cdBairroPessoaView" id="cdBairroPessoaView" type="text"/>
                    <button id="btnFindBairro" onclick="btnFindBairroOnClick(null, 'Pessoa')" idform="pessoa" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                    <button onclick="$('cdBairroPessoa').value = 0; $('cdBairroPessoaView').value = ''" idform="pessoa" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
                  </div>
                </div>
                <div style="width: 247px;" class="element">
                  <label class="caption" for="nmPontoReferenciaPessoa">Ponto referência</label>
                  <input style="text-transform: uppercase; width: 244px;" lguppercase="true" logmessage="Ponto refer?ncia" class="field" idform="pessoa" reference="nm_ponto_referencia" datatype="STRING" maxlength="256" id="nmPontoReferenciaPessoa" name="nmPontoReferenciaPessoa" type="text"/>
                </div>
                <div style="width: 65px;" class="element">
                  <label class="caption" for="nrCepPessoa">CEP</label>
                  <input style="text-transform: uppercase; width: 62px;" lguppercase="true" mask="#####-###" logmessage="CEP" class="field" idform="pessoa" reference="nr_cep" datatype="STRING" maxlength="9" id="nrCepPessoa" name="nrCepPessoa" type="text"/>
                </div>
                <div style="width: 337px;" class="element">
                  <label class="caption" for="cdCidadePessoa">Cidade</label>
                  <input logmessage="Código Cidade" idform="pessoa" reference="cd_cidade" datatype="STRING" id="cdCidadePessoa" name="cdCidadePessoa" value="<%=cdCidadeDefault%>" defaultValue="<%=cdCidadeDefault%>" type="hidden"/>
                  <input logmessage="Nome Cidade" idform="pessoa" reference="nm_cidade" style="width: 334px;" static="true" disabled="disabled" value="<%=nmCidade%>" defaultValue="<%=nmCidade%>" class="disabledField" name="cdCidadePessoaView" id="cdCidadePessoaView" type="text"/>
                  <button id="btnFindCidadePessoaEndereco" onclick="btnFindCidadeEnderecoOnClick(null, 'Pessoa')" idform="pessoa" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                  <button onclick="$('cdCidadePessoa').value = 0; $('cdCidadePessoaView').value = ''" idform="pessoa" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
                </div>
                <input style="text-transform: uppercase; width: 108px;" mask="(##)####-####" lguppercase="true" logmessage="Telefone" class="field" idform="pessoa" reference="nr_telefone" datatype="STRING" maxlength="15" id="nrTelefonePessoa" name="nrTelefonePessoa" type="hidden"/>
              </div>
            </div>
          </div>
          <div style="float:left; width:20px; margin-top: 7px;">
              <button title="Novo endereço" onclick="btnNewEnderecoOnClick();" style="margin-bottom:2px" id="btnAddEndereco" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
          </div>
        </div>
        <div class="d1-line">
          <div style="width: <%=cdVinculo > 0 ? 327 : 188%>px;" class="element">
            <label class="caption">Outros Documentos</label>
            <div id="divGridDocumentos" style="width: <%=cdVinculo > 0 ? 324 : 185%>px; background-color:#FFF; height:110px; border:1px solid #000000">&nbsp;</div>
          </div>
          <div style="width: 25px;" class="element">
            <label class="caption">&nbsp;</label>
              <button title="Novo Documento" onclick="btnNewTipoDocumentoOnClick();" style="margin-bottom:2px" id="btnNewTipoDocumento" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
              <button title="Alterar Documento" onclick="btnAlterTipoDocumentoOnClick();" style="margin-bottom:2px" id="btnAlterTipoDocumento" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
              <button title="Excluir Documento" onclick="btnDeleteTipoDocumentoOnClick();" id="btnDeleteTipoDocumento" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
          </div>
          <% if (cdVinculo <= 0) { %>
              <div style="width: 188px;" class="element">
                <label class="caption">V&iacute;nculos</label>
                <div id="divGridVinculos" style="width: 185px; background-color:#FFFFFF; height:110px; border:1px solid #000000">&nbsp;</div>
              </div>
              <div style="width: 25px;" class="element">
                <label class="caption">&nbsp;</label>
                  <button title="Novo Vínculo" onclick="btnNewVinculoOnClick();" style="margin-bottom:2px" id="btnNewPessoaEmpresa" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
                  <button title="Alterar Vínculo" onclick="btnAlterVinculoOnClick();" style="margin-bottom:2px" id="btnAlterPessoaEmpresa" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
                  <button title="Excluir Vínculo" onclick="btnDeleteVinculoOnClick();" id="btnDeletePessoaEmpresa" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
              </div>
          <% } %>
          <div style="width: <%=cdVinculo > 0 ? 327 : 253%>px;" class="element">
            <label class="caption" for="txtObservacao">Observações</label>
            <textarea style="width: <%=cdVinculo > 0 ? 327 : 253%>px; height:110px;" logmessage="Observações" class="field" idform="pessoa" reference="txt_observacao" datatype="STRING" id="txtObservacao" name="txtObservacao">&nbsp;</textarea>
          </div>
        </div>
      </div>
      <div id="divAbaOutrosDados" style="display:none;">
        <div style="width: 658px;" class="element">
          <label class="caption">Contas Bancárias</label>
          <div id="divGridContaBancaria" style="width: 655px; background-color:#FFF; height:101px; border:1px solid #000000">&nbsp;</div>
        </div>
        <div style="width: 20px;" class="element">
          <label class="caption">&nbsp;</label>
            <button title="Nova conta banc?ria" onclick="btnNewContaBancariaOnClick();" style="margin-bottom:2px" id="btnNewOcorrencia" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
            <button title="Alterar Ocorr?ncia" onclick="btnAlterContaBancariaOnClick();" style="margin-bottom:2px" id="btnAlterOcorrencia" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
            <button title="Excluir Ocorr?ncia" onclick="btnDeleteContaBancariaOnClick();" id="btnDeleteOcorrencia" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
        </div>
        <div id="line20" class="d1-line">
          <div style="width: 658px;" class="element">
            <label class="caption">Contatos</label>
            <div id="divGridContatos" style="width: 655px; background-color:#FFF; height:71px; border:1px solid #000000">&nbsp;</div>
          </div>
          <div style="width: 20px;" class="element">
            <label class="caption">&nbsp;</label>
              <button title="Novo Contato" onclick="btnNewContatoOnClick();" style="margin-bottom:2px" id="btnNewArquivo" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
              <button title="Alterar Contato" onclick="btnAlterContatoOnClick();" style="margin-bottom:2px" id="btnAlterArquivo" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
              <button title="Excluir Contato" onclick="btnDeleteContatoOnClick();" id="btnDeleteArquivo" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
          </div>
          <div style="width: 658px;" class="element">
            <label class="caption">Atividades Econômicas</label>
            <div id="divGridCnaes" style="width: 655px; background-color:#FFF; height:71px; border:1px solid #000000">&nbsp;</div>
          </div>
          <div style="width: 20px;" class="element">
            <label class="caption">&nbsp;</label>
              <button title="Adicionar atividade economica" onclick="btnNewCnaeOnClick();" style="margin-bottom:2px" id="btnNewCnaeOnClick" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
              <button title="Excluir atividade econ?mica" onclick="btnDeleteCnaeOnClick();" id="btnDeleteCnae" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
          </div>
        </div>
        <div id="line21" class="d1-line">
          <div style="width: 658px;" class="element">
            <label class="caption">Ocupa&ccedil;&otilde;es</label>
            <div id="divGridCbos" style="width: 655px; background-color:#FFF; height:101px; border:1px solid #000000">&nbsp;</div>
          </div>
          <div style="width: 20px;" class="element">
            <label class="caption">&nbsp;</label>
              <button title="Adicionar Ocupação" onclick="btnNewCboOnClick();" style="margin-bottom:2px" id="btnNewCboOnClick" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
              <button title="Excluir Ocupação" onclick="btnDeleteCboOnClick();" id="btnDeleteCbo" class="toolButton"><img src="/sol/imagens/btDelete16.gif" height="16" width="16"/></button>
          </div>
        </div>
        <div style="width: 658px;" class="element">
          <label class="caption">Ocorr&ecirc;ncias</label>
          <div id="divGridOcorrencias" style="width: 655px; background-color:#FFF; height: 101px; border:1px solid #000000">&nbsp;</div>
        </div>
        <div style="width: 20px;" class="element">
          <label class="caption">&nbsp;</label>
            <button title="Nova Ocorr?ncia" onclick="btnNewOcorrenciaOnClick();" style="margin-bottom:2px" id="btnNewOcorrencia" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
            <button title="Alterar Ocorr?ncia" onclick="btnAlterOcorrenciaOnClick();" style="margin-bottom:2px" id="btnAlterOcorrencia" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
            <button title="Excluir Ocorr?ncia" onclick="btnDeleteOcorrenciaOnClick();" id="btnDeleteOcorrencia" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
        </div>
      </div>
      <div id="divAbaAtributos" style="display: none;">
        <div style="width:655px; float:left; padding:0 0 0 4px;" id="divAtributos">
          <div class="d1-line" id="line2">
            <div style="position:relative; border:1px solid #999; float:left; padding:2px; margin-top:7px; margin-right:0px">
              <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:4px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; width:90px;">Dados espec?ficos</div>
              <div style="width: 670px; height:331px; overflow:scroll; padding:0 0 2px 0;" class="element" id="divBodyAtributos">
                <div id="divElementAtributo" style="width: 658px; padding:0 0 2px 0; display:<%=1==1 ? "none" : ""%>;" class="element">
                  <label id="labelAtributo" class="caption" style="width:229px; float:left; padding:2px 0 0 0">ID Atributo </label>
                  <input style="width: 427px;" logmessage="Log atributo" class="field" idform="produtoServico" reference="log_atributo" datatype="INT" id="idLogAtributo" name="idLogAtributo" type="text">
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <% if (showArquivos) { %>
          <div id="divAbaArquivos" style="display:none;">
            <div style="width: 658px;" class="element">
              <label class="caption">Arquivos</label>
              <div id="divGridArquivos" style="width: 655px; background-color:#FFF; height: 333px; border:1px solid #000000">&nbsp;</div>
            </div>
            <div style="width: 20px;" class="element">
              <label class="caption">&nbsp;</label>
                <button title="Novo Arquivo" onclick="btnNewArquivoOnClick();" style="margin-bottom:2px" id="btnNewArquivo" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
                <button title="Alterar Arquivo" onclick="btnAlterArquivoOnClick();" style="margin-bottom:2px" id="btnAlterArquivo" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
                <button title="Excluir Arquivo" onclick="btnDeleteArquivoOnClick();" id="btnDeleteArquivo" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
            </div>
            <div id="divPreviewArquivo" style="width: 655px; margin-top: 3px; float: left; display: none;" class="element">
              <iframe scrolling="auto" id="previewArquivo" style="border:1px solid #000000; background-color:#FFF; margin:0px 0px 0px 0px; width:655px; height:200px;" src="about:blank" frameborder="0">&nbsp;</iframe>
            </div>
          </div>
      <% } %>
      <!--**************************** V-NCULOS/DADOS FUNCIONAIS ****************************-->
      <%if (showDependentes)	{ %>
              <!-- ABA/GRID DEPENDENTES -->
              <div id="divAbaVinculosDependentes" style="display:none; margin-left:3px;">
                <div class="d1-line" id="line0">
                  <div id="divGridDependentesPai" style="width: 646px;" class="element">
                    <label class="caption">Dependentes</label>
                    <div id="divGridDependentes" style="width: 643px; height:303px; background-color:#FFF; border:1px solid #000000">&nbsp;</div>
                  </div>
                  <div style="width: 25px;" class="element">
                    <label class="caption">&nbsp;</label>
                      <button title="Incluir dependente" onclick="btnNewDependenteOnClick();" style="margin-bottom:2px" id="btnNewDependente" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
                      <button title="Alterar dependente" onclick="btnAlterDependenteOnClick();" style="margin-bottom:2px" id="btnAlterDependente" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
                      <button title="Excluir dependente" onclick="btnDeleteDependenteOnClick();" id="btnDeleteDependente" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
                  </div>
                </div>
              </div>
              <!--
               *********************************************************************************************************************
               *********************************************** Dependentes *********************************************************
               *********************************************************************************************************************
              -->
              <div style="width: 600px; display:none;" id="panelDependente" class="d1-form">
                  <div style="width: 600px; height: 200px;" class="d1-body">
                    <div class="d1-line" id="line1">
                      <div style="width: 350px;" class="element">
                        <label class="caption" for="nmDependente">Nome do dependente</label>
                        <input style="text-transform: uppercase; width: 347px;" lguppercase="true" class="field" idform="dependentes" reference="nm_pessoa" maxlength="50" datatype="STRING" id="nmDependente" name="nmDependente" type="text" >
                      </div>
                      <div style="width: 87px;" class="element">
                        <label class="caption" for="dtNascimentoDependente">Data nascimento</label>
                        <input style="width: 84px;" onblur="getIdade(this.value)" mask="##/##/####" maxlength="10" logmessage="Data Nascimento Dependente" class="field" idform="dependentes" reference="dt_nascimento" datatype="DATETIME" id="dtNascimentoDependente" name="dtNascimentoDependente" type="text"/>
                        <button idform="dependentes" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tr')" title="Selecionar data..." reference="dtNascimentoDependente" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
                      </div>
                      <div style="width: 58px;" class="element">
                        <label class="caption" for="nrIdadeDependente">Idade</label>
                        <input style="width: 55px;" disabled="true" class="disabledField" idform="dependentes" reference="vl_idade" datatype="STRING" id="nrIdadeDependente" name="nrIdadeDependente" type="text" >
                      </div>
                      <div style="width: 86px;" class="element">
                        <label class="caption" for="tpSexoDependente">Sexo</label>
                        <select style="width: 83px;" class="select" idform="dependentes" reference="tp_sexo" datatype="INT" id="tpSexoDependente" name="tpSexoDependente">
                          <option value="-1">Selecione...</option>
                        </select>
                      </div>
                    </div>
                    <div class="d1-line" id="line2">
                      <div style="width: 161px;" class="element">
                        <label class="caption" for="stEstadoCivilDependente">Estado civil</label>
                        <select style="width: 158px;" class="select" idform="dependentes" reference="st_estado_civil" datatype="INT" id="stEstadoCivilDependente" name="stEstadoCivilDependente">
                          <option value="-1">Selecione...</option>
                        </select>
                      </div>
                      <div style="width: 97px;" class="element">
                        <label class="caption" for="nrRgDependente">Identidade</label>
                        <input style="width: 94px;" class="field" idform="dependentes" reference="nr_rg" datatype="STRING" id="nrRgDependente" maxlength="15" name="nrRgDependente" type="text" >
                      </div>
                      <div style="width: 75px;" class="element">
                        <label class="caption" for="dtEmissaoRgDependente">Data emissão</label>
                        <input style="width: 72px;" mask="##/##/####" maxlength="10" logmessage="Data Emiss?o Rg Dependente" class="field" idform="dependentes" reference="dt_emissao_rg" datatype="DATETIME" id="dtEmissaoRgDependente" name="dtEmissaoRgDependente" type="text"/>
                        <button idform="dependentes" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Tl')" title="Selecionar data..." reference="dtEmissaoRgDependente" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
                      </div>
                      <div style="width: 60px;" class="element">
                        <label class="caption" for="sgOrgaoRgDependente">Org?o exp.</label>
                        <select style="width: 57px;" class="select" idform="dependentes" logmessage="Org?o Expedidor Dependente" reference="sg_orgao_rg" datatype="STRING" id="sgOrgaoRgDependente" name="sgOrgaoRgDependente" defaultvalue="">
                        	<option value="">...</option>
                        </select>
                      </div>
                      <div style="width: 43px;" class="element">
                        <label class="caption" for="cdEstadoRgDependente">UF RG</label>
                        <select style="width: 40px;" class="select" idform="dependentes" logmessage="Estado Emissor RG - Dependente" reference="cd_estado_rg" datatype="STRING" id="cdEstadoRgDependente" name="cdEstadoRgDependente" defaultvalue="0">
			            	<option value="0">...</option>
                        </select>
                      </div>
                      <div style="width: 145px;" class="element">
                        <label class="caption" for="tpParentesco">Parentesco</label>
                        <select logmessage="Tipo Parentesco Dependente" idform="dependentes" reference="tp_parentesco" style="width: 142px;" class="select" name="tpParentesco" id="tpParentesco">
                          <option value="-1">Selecione...</option>
                        </select>
                      </div>
                    </div>
                    <div class="d1-line" id="line3">
                      <div style="width: 291px;" class="element">
                        <label class="caption" for="cdEscolaridadeDependente">Escolaridade</label>
                        <select logmessage="Código Escolaridade Dependente" idform="dependentes" reference="cd_escolaridade" style="width: 288px;" class="select" name="cdEscolaridadeDependente" id="cdEscolaridadeDependente">
                          <option value="0">Selecione...</option>
                        </select>
                      </div>
                      <div style="width: 20px; margin: 13px 0px 0px 0px;" class="element">
                        <input logmessage="Imposto de renda?" idform="dependentes" reference="lg_ir" id="lgDependenteIr" name="lgDependenteIr" type="checkbox" value="1">
                      </div>
                      <div id="divLgDependenteIr" style="width: 90px;" class="element">
                        <label style="margin:15px 0px 0px 0px" class="caption">Imposto de renda</label>
                      </div>
                      <div style="width: 20px; margin: 13px 0px 0px 0px;" class="element">
                        <input logmessage="Sal?rio Fam?lia?" idform="dependentes" reference="lg_salario_familia" id="lgDependenteSalarioFamilia" name="lgDependenteSalarioFamilia" type="checkbox" value="1">
                      </div>
                      <div id="divLgDependenteSalarioFamilia" style="width: 75px;" class="element">
                        <label style="margin:15px 0px 0px 0px" class="caption">Salário família</label>
                      </div>
                      <div style="width: 20px; margin: 13px 0px 0px 0px;" class="element">
                        <input logmessage="Pensionista?" idform="dependentes" reference="lg_pensionista" id="lgDependentePensionista" name="lgDependentePensionista" type="checkbox" value="1" onclick="checkPensionista()"/>
                      </div>
                      <div id="divLgDependentePensionista" style="width: 65px;" class="element">
                        <label style="margin:15px 0px 0px 0px" class="caption">Pensionista</label>
                      </div>
                    </div>
                  </div>
              </div>
            </div>
          </div>
      <% } %>
      <!--**************************** V-NCULOS/DADOS FUNCIONAIS ****************************-->
      <%if (showFactoring)	{ %>
              <!-- ABA/GRID FACTORING -->
              <div id="divAbaFactoring" style="display:none; margin-left:3px;">
              	<fieldset style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; float:left; width: 368px; height: 45px;">
            	  <legend>Limite de crédito</legend>
		 			<div id="line8" class="d1-line" style="float: left;">
		 				<div style="width: 122px; margin-bottom:5px;" class="element">
							<label class="caption">Limite Geral</label>
							<input style="width: 117px;" id="vlLimiteFactoring" reference="vl_limite_factoring" onblur="addParametros(this)" idform="pessoa" datatype="float" mask="#,####.00" name="vlLimiteFactoring" class="field2"  type="text"/>
				 		</div>
						<div style="width: 122px; margin-bottom:5px;" class="element">
							<label class="caption">Limite Emissor</label>
							<input style="width: 117px;" id="vlLimiteFactoringEmissor" reference="vl_limite_factoring_emissor" onblur="addParametros(this)" idform="pessoa" datatype="float" mask="#,####.00" name="vlLimiteFactoringEmissor" class="field2" type="text"/>
				 		</div>
						<div style="width: 122px; margin-bottom:5px;" class="element">
							<label class="caption">Limite Unitario</label>
							<input style="width: 122px;" id="vlLimiteFactoringUnitario" reference="vl_limite_factoring_unitario" onblur="addParametros(this)" idform="pessoa" datatype="float" mask="#,####.00" name="vlLimiteFactoringUnitario" class="field2"  type="text"/>
				 		</div>
		 			</div>
				</fieldset>
				<fieldset style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; float:left; width: 265px; height: 45px;">
            	  <legend>Prazos</legend>
		 			<div id="line8" class="d1-line" style="float: left;">
		 				<div style="width: 130px; margin-bottom:5px;" class="element">
							<label class="caption">Minimo</label>
							<input style="width: 125px;" id="qtPrazoMinimoFactoring" reference="qt_prazo_minimo_factoring" onblur="addParametros(this)" idform="pessoa" datatype="float" mask="#,####.00" name="qtPrazoMinimoFactoring" class="field2" type="text"/>
				 		</div>
						<div style="width: 135px; margin-bottom:5px;" class="element">
							<label class="caption">Maximo</label>
							<input style="width: 135px;" id="qtPrazoMaximoFactoring" reference="qt_prazo_maximo_factoring" onblur="addParametros(this)" idform="pessoa" datatype="float" mask="#,####.00" name="qtPrazoMaximoFactoring" class="field2" type="text"/>
				 		</div>
					</div>
				</fieldset>
				<fieldset style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; float:left; width: 657px; height: 45px;">
            	  <legend>Prazos</legend>
					<div style="width: 219px; margin-bottom:5px;" class="element">
						<label class="caption">Idade Minima</label>
						<input style="width: 209px;" id="qtIdadeMinimaFactoring" reference="qt_idade_minima_factoring" onblur="addParametros(this)" idform="pessoa" datatype="float" mask="#,####.00" name="qtIdadeMinimaFactoring" class="field2" type="text"/>
			 		</div>
			 		<div style="width: 219px; margin-bottom:5px;" class="element">
						<label class="caption">Ganho Minimo</label>
						<input style="width: 209px;" id="vlGanhoMinimoFactoring" reference="vl_ganho_minimo_factoring" onblur="addParametros(this)" idform="pessoa" datatype="float" mask="#,####.00" name="vlGanhoMinimoFactoring" class="field2" type="text"/>
			 		</div>
			 		<div style="width: 219px; margin-bottom:5px;" class="element">
						<label class="caption">Taxa Minima</label>
						<input style="width: 219px;" id="prTaxaMinimaFactoring" reference="pr_taxa_minima_factoring" onblur="addParametros(this)" idform="pessoa" datatype="float" mask="#,####.00" name="prTaxaMinimaFactoring" class="field2" type="text"/>
			 		</div>
		 		</fieldset>
		 		<div style="width: 675px; margin-top:5px;" class="element">
		 			<label class="caption">Cheques em Aberto</label>
					<div id="divGridContaReceber" style="width: 675px; background-color:#FFF; height:207px; border:1px solid #000;">&nbsp;</div>
				</div>
			  </div>
            </div>
          </div>
      <% } %>
    </div>
  </div>
</div>
<!--
 *********************************************************************************************************************************
 ******************************************************** DOCUMENTOS **************************************************************
 *********************************************************************************************************************************
  -->
<div id="tipoDocumentoPanel" class="d1-form" style="display:none; width:405px; height:32px">
  <div style="width: 405px;" class="d1-body">
    <div class="d1-line">
      <div style="width: 230px;" class="element">
        <label class="caption" for="cdTipoDocumento">Tipo de Documento</label>
        <select style="width: 227px;" logmessage="Tipo Documento" registerclearlog="0" value="0" defaultValue="0" class="select" idform="documento" reference="cd_tipo_documento" maxlength="10" id="cdTipoDocumento" name="cdTipoDocumento" type="text">
          <option value="0">Selecione...</option>
        </select>
      </div>
      <div style="width: 135px;" class="element">
        <label class="caption" for="nrDocumento">N&deg; Documento </label>
        <input style="text-transform: uppercase; width: 132px;" lguppercase="true" logmessage="N¦ Documento" class="field" idform="documento" reference="nr_documento" maxlength="20" id="nrDocumento" name="nrDocumento" type="text"/>
      </div>
      <div style="width: 20px;" class="element">
        <label class="caption" style="height:10px">&nbsp;</label>
        <button title="Gravar" onclick="btnSaveTipoDocumentoOnClick();" style="margin-bottom:2px" id="btnSaveTipoDocumento" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/></button>
      </div>
    </div>
  </div>
</div>
<!--
 *********************************************************************************************************************************
 ******************************************************** endereços **************************************************************
 *********************************************************************************************************************************
  -->
<div id="enderecoPanel" class="d1-form" style="display:none; width:600px; height:150px">
  <div style="width: 600px;" class="d1-body">
    <div class="d1-line" id="line0">
      <div style="width: 136px;" class="element">
        <label class="caption" for="cdTipoEndereco">Tipo endereço</label>
        <select logmessage="Tipo endereço" registerclearlog="0" style="width: 133px;" class="select" idform="endereco" reference="cd_tipo_endereco" datatype="STRING" id="cdTipoEndereco" name="cdTipoEndereco" defaultValue="0">
          <option value="0">Selecione...</option>
        </select>
      </div>
      <div id="divFieldsEndereco1" style="display:none">
        <div style="width: 354.5px;" class="element">
          <label class="caption" for="cdLogradouro">Logradouro</label>
          <input logmessage="Código Logradouro" idform="endereco" reference="cd_logradouro" datatype="STRING" id="cdLogradouro" name="cdLogradouro" type="hidden"/>
          <input logmessage="Nome Logradouro" idform="endereco" reference="nm_logradouro_formatado" style="width: 351.5px;" static="true" disabled="disabled" class="disabledField" name="cdLogradouroView" id="cdLogradouroView" type="text"/>
          <button id="btnFindLogradouro" name="btnFindLogradouro" onclick="btnFindLogradouroOnClick()" static="static" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
          <button onclick="btnClearCdLogradouroOnClick()" static="static" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
        </div>
        <div style="width: 96px;" class="element">
          <label class="caption" for="nrEndereco1">N&deg; Endereço</label>
          <input style="width: 96px;" lguppercase="true" logmessage="N¦ End" class="field" idform="endereco" reference="nr_endereco" datatype="STRING" maxlength="10" id="nrEndereco1" name="nrEndereco1" type="text"/>
        </div>
      </div>
      <div id="divFieldsEndereco2" style="display:<%=1!=1 ? "none" : ""%>">
        <div style="width: 100px;" class="element">
          <label class="caption" for="cdTipoLogradouro">Tipo logradouro</label>
          <select logmessage="Tipo Logradouro" registerclearlog="0" style="width: 97px;" class="select" idform="endereco" reference="cd_tipo_logradouro" datatype="STRING" id="cdTipoLogradouro" name="cdTipoLogradouro" defaultValue="0">
            <option value="0">Selecione...</option>
          </select>
        </div>
        <div style="width: 276px;" class="element">
          <label class="caption" for="nmLogradouro">Logradouro</label>
          <input style="text-transform: uppercase; width: 273px;" lguppercase="true" logmessage="Nome Logradouro (livre digitação)" class="field" idform="endereco" reference="nm_logradouro" datatype="STRING" maxlength="100" id="nmLogradouro" name="nmLogradouro" type="text"/>
        </div>
        <div style="width: 78px;" class="element">
          <label class="caption" for="nrEndereco2">N&deg;</label>
          <input style="width: 75px;" lguppercase="true" logmessage="N¦ Porta" class="field" idform="endereco" reference="nr_endereco" datatype="STRING" maxlength="10" id="nrEndereco2" name="nrEndereco2" type="text"/>
        </div>
      </div>
    </div>
    <div class="d1-line" id="divFieldsEndereco3" style="display:<%=1!=1 ? "none" : ""%>;">
      <div style="width: 207px;" class="element">
        <label class="caption" for="nmComplemento1">Complemento</label>
        <input style="text-transform: uppercase; width: 204px;" lguppercase="true" logmessage="Complemento" class="field" idform="endereco" reference="nm_complemento" datatype="STRING" maxlength="50" id="nmComplemento1" name="nmComplemento1" type="text"/>
      </div>
      <div style="width: 382px;" class="element">
        <label class="caption" for="cdBairro">Bairro</label>
        <input logmessage="Código Bairro" idform="endereco" reference="cd_bairro" datatype="STRING" id="cdBairro" name="cdBairro" type="hidden"/>
        <input logmessage="Nome Bairro" idform="endereco" reference="nm_bairro_enderecamento" style="width: 379px;" static="true" disabled="disabled" class="disabledField" name="cdBairroView" id="cdBairroView" type="text"/>
        <button id="btnFindBairro" onclick="btnFindBairroOnClick()" static="static" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
        <button onclick="btnClearCdBairroOnClick()" static="static" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
      </div>
    </div>
    <div class="d1-line" id="divFieldsEndereco4">
      <div style="width: 255px;" class="element">
        <label class="caption" for="nmComplemento2">Complemento</label>
        <input style="text-transform: uppercase; width: 252px;" lguppercase="true" logmessage="Complemento" class="field" idform="endereco" reference="nm_complemento" datatype="STRING" maxlength="50" id="nmComplemento2" name="nmComplemento2" type="text"/>
      </div>
      <div style="width: 335px;" class="element">
        <label class="caption" for="nmBairro">Bairro</label>
        <input style="text-transform: uppercase; width: 332px;" lguppercase="true" logmessage="Bairro (livre digitação)" class="field" idform="endereco" reference="nm_bairro" datatype="STRING" maxlength="50" id="nmBairro" name="nmBairro" type="text"/>
      </div>
    </div>
    <div class="d1-line">
      <div style="width: 171px;" class="element">
        <label class="caption" for="nmPontoReferencia">Ponto referência</label>
        <input style="text-transform: uppercase; width: 168px;" lguppercase="true" logmessage="Ponto referência" class="field" idform="endereco" reference="nm_ponto_referencia" datatype="STRING" maxlength="256" id="nmPontoReferencia" name="nmPontoReferencia" type="text"/>
      </div>
      <div style="width: 65px;" class="element">
        <label class="caption" for="nrCep">CEP</label>
        <input style="text-transform: uppercase; width: 62px;" lguppercase="true" mask="#####-###" logmessage="CEP" class="field" idform="endereco" reference="nr_cep" datatype="STRING" maxlength="9" id="nrCep" name="nrCep" type="text"/>
      </div>
      <div style="width: 268px;" class="element">
        <label class="caption" for="cdCidade">Cidade</label>
        <input logmessage="Código Cidade" idform="endereco" reference="cd_cidade" datatype="STRING" id="cdCidade" name="cdCidade" type="hidden" value="<%=cdCidadeDefault%>" defaultValue="<%=cdCidadeDefault%>"/>
        <input logmessage="Nome Cidade" idform="endereco" reference="nm_cidade" style="width: 265px;" static="true" disabled="disabled" value="<%=nmCidade%>" defaultValue="<%=nmCidade%>" class="disabledField" name="cdCidadeView" id="cdCidadeView" type="text"/>
        <button id="btnFindCidadeEndereco" onclick="btnFindCidadeEnderecoOnClick()" static="static" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
        <button onclick="btnClearCdCidadeEnderecoOnClick()" static="static" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
      </div>
      <div style="width: 86.333px;" class="element">
        <label class="caption" for="nrTelefone">Telefone</label>
        <input style="text-transform: uppercase; width: 83.333px;" mask="(##)####-####" lguppercase="true" logmessage="N¦ Telefone" class="field" idform="endereco" reference="nr_telefone" datatype="STRING" maxlength="15" id="nrTelefone" name="nrTelefone" type="text"/>
      </div>
    </div>
    <div class="d1-line">
      <div style="width: 393px;" class="element">
        <label class="caption" for="dsEndereco">Descri&ccedil;&atilde;o endere&ccedil;o</label>
        <input style="text-transform: uppercase; width: 390px;" lguppercase="true" logmessage="Descrição Endereço" class="field" idform="endereco" reference="ds_endereco" datatype="STRING" maxlength="50" id="dsEndereco" name="dsEndereco" type="text"/>
      </div>
      <div style="width: 24px; height:20px" class="element">
        <label class="caption">&nbsp;</label>
        <input name="lgPrincipal" logemssage="endereço principal" idform="endereco" reference="lg_principal" type="checkbox" id="lgPrincipal" value="1" />
      </div>
      <div style="width: 96px;" class="element">
        <label class="caption">&nbsp;</label>
        <label style="margin:3px 0px 0px 0px" class="caption">Endereço principal</label>
      </div>
      <div style="width: 24px; height:20px" class="element">
        <label class="caption">&nbsp;</label>
        <input name="lgCobranca" logemssage="Endereço de Cobrança" idform="endereco" reference="lg_cobranca" type="checkbox" id="lgCobranca" value="1" />
      </div>
      <div style="width: 55px;" class="element">
        <label class="caption">&nbsp;</label>
        <label style="margin:3px 0px 0px 0px" class="caption">Cobran&ccedil;a</label>
      </div>
    </div>
    <div class="d1-line" style="float:right; width:175px; margin:4px 0px 0px 0px;">
      <div style="width:80px;" class="element">
        <button id="btnSaveEndereco" title="Gravar endereço" onclick="btnSaveEnderecoOnClick(null);" style="width:80px; border:1px solid #999; height:20px;" class="toolButton"> <img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>&nbsp;Gravar</button>
      </div>
      <div style="width:80px;" class="element">
        <button id="btnCancelar" title="Voltar para a janela anterior" onclick="formEndereco.close();" style="margin-left:2px; width:80px; border:1px solid #999; height:20px;" class="toolButton"><img src="/sol/imagens/negative16.gif" height="16" width="16"/>&nbsp;Cancelar</button>
      </div>
    </div>
  </div>
</div>
<!--
 *********************************************************************************************************************************
 ******************************************************** Ocorr?ncias ************************************************************
 *********************************************************************************************************************************
  -->
<div id="ocorrenciaPanel" class="d1-form" style="display:none; width:501px; height:405px">
  <div style="width: 501px; height: 405px;" class="d1-body">
    <div class="d1-line" id="">
      <div style="width: 80px;" class="element">
        <label class="caption" for="dtOcorrencia">Data</label>
        <input style="width: 72px;" mask="##/##/####" maxlength="10" defaultvalue="%DATE" class="field" logmessage="Data ocorr?ncia" idform="ocorrencia" reference="dt_ocorrencia" datatype="DATE" id="dtOcorrencia" name="dtOcorrencia" type="text"/>
        <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtOcorrencia" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
      <div style="width: 312px;" class="element">
        <label class="caption" for="cdUsuarioOcorrencia">Responsável</label>
        <input logmessage="Código Pessoa Respons?vel" idform="ocorrencia" reference="cd_usuario" datatype="STRING" id="cdUsuarioOcorrencia" name="cdUsuarioOcorrencia" type="hidden" value="0" defaultValue="0">
        <input logmessage="Nome Pessoa Respons?vel" idform="ocorrencia" style="width: 309px;" reference="nm_responsavel" static="true" disabled="disabled" class="disabledField" name="cdUsuarioOcorrenciaView" id="cdUsuarioOcorrenciaView" type="text">
      </div>
      <div style="width: 109px;" class="element">
        <label class="caption" for="stOcorrencia">Situação</label>
        <select logmessage="Situação" style="width: 106px;" class="select" idform="ocorrencia" reference="st_ocorrencia" datatype="STRING" id="stOcorrencia" name="stOcorrencia" defaultValue="0">
        </select>
      </div>
    </div>
    <div class="d1-line" id="">
      <div style="width: 501px;" class="element">
        <label class="caption" for="cdTipoOcorrencia">Tipo de ocorrência</label>
        <select registerclearlog="0" logmessage="Código Situação Ocorr?ncia" style="width: 498px;" class="select" idform="ocorrencia" reference="cd_tipo_ocorrencia" datatype="STRING" id="cdTipoOcorrencia" name="cdTipoOcorrencia" defaultValue="0">
          <option value="0">Selecione...</option>
        </select>
      </div>
    </div>
    <div class="d1-line" id="">
      <div style="width: 500px;" class="element">
        <label class="caption" for="txtOcorrencia">Descrição</label>
        <textarea logmessage="Descrição" style="width: 497px; height:68px" class="textarea" idform="ocorrencia" reference="txt_ocorrencia" datatype="STRING" id="txtOcorrencia" name="txtOcorrencia"></textarea>
      </div>
    </div>
    <div class="d1-line" style="float:right; right:0px; width:164px; margin:2px 0px 0px 0px;">
      <div style="width:80px;" class="element">
        <button id="btnSaveOcorrencia" title="Gravar informações" onclick="btnSaveOcorrenciaOnClick(null);" style="width:80px; border:1px solid #999; height:25px;" class="toolButton"> <img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>&nbsp;Gravar</button>
      </div>
      <div style="width:80px;" class="element">
        <button id="btnCancelar" title="Voltar para a janela anterior" onclick="formOcorrencia.close();" style="margin-left:2px; width:80px; border:1px solid #999; height:25px;" class="toolButton"><img src="/sol/imagens/negative16.gif" height="16" width="16"/>&nbsp;Cancelar</button>
      </div>
    </div>
  </div>
</div>
<!--
 *********************************************************************************************************************************
 ******************************************************** Arquivos ***************************************************************
 *********************************************************************************************************************************
  -->
<div id="arquivoPanel" class="d1-form" style="display:none; width:501px; height:220px">
  <div style="width: 501px; height: 220px;" class="d1-body">
    <form action="load_arquivo_session.jsp" method="post" enctype="multipart/form-data" name="frameArquivo" target="frameHidden" id="frameArquivo">
      <div class="d1-line" id="">
        <div style="width: 500px;" class="element">
          <label class="caption" for="nmDocumento">Descrição documento</label>
          <input style="width: 497px; text-transform:uppercase" lguppercase="true" logmessage="Nome Documento" class="field" idform="arquivo" reference="nm_documento" datatype="STRING" maxlength="100" id="nmDocumento" name="nmDocumento" type="text">
        </div>
      </div>
      <div class="d1-line" id="">
        <div style="width: 155px;" class="element">
          <label class="caption" for="cdTipoArquivo">Tipo de arquivo</label>
          <select registerclearlog="0" defaultValue="0" logmessage="Tipo de Arquivo" style="width: 152px;" class="select" idform="arquivo" reference="cd_tipo_arquivo" datatype="STRING" id="cdTipoArquivo" name="cdTipoArquivo">
            <option value="0">Selecione...</option>
          </select>
        </div>
        <div style="width: 200px;" class="element">
          <label class="caption" for="cdNivelAcesso">Nivel de acesso</label>
          <select registerclearlog="0" defaultValue="0" logmessage="N?vel de Acesso" style="width: 197px;" class="select" idform="arquivo" reference="cd_nivel_acesso" datatype="STRING" id="cdNivelAcesso" name="cdNivelAcesso">
            <option value="0">Selecione...</option>
          </select>
        </div>
        <div style="width: 145px;" class="element">
          <label class="caption" for="dtArquivamento">Data arquivamento</label>
          <input style="width: 142px;" logmessage="Data Arquivamento" class="disabledField" disabled="disabled" idform="arquivo" reference="dt_arquivamento" datatype="STRING" maxlength="16" id="dtArquivamento" name="dtArquivamento" type="text">
        </div>
      </div>
      <div class="d1-line" id="">
        <div style="width: 500px;" class="element">
          <label class="caption" for="txtObservacao">Obervações</label>
          <textarea logmessage="Observações" style="width: 497px;" class="textarea" idform="arquivo" reference="txt_observacao" datatype="STRING" id="txtObservacaoArquivo" name="txtObservacaoArquivo"></textarea>
        </div>
      </div>
      <div class="d1-line" id="">
        <div style="width: 500px;" class="element">
          <label class="caption" for="blbArquivo">Arquivo (se voc&ecirc; n&atilde;o informar a localiza&ccedil;&atilde;o, o arquivo n&atilde;o ser&aacute; atualizado) </label>
          <input name="blbArquivo" type="file" class="field" id="blbArquivo" style="width:100%; height: 23px;" size="82" />
        </div>
      </div>
      <div class="d1-line" style="width:499px;">
        <div style="width: 100px; float:right; padding:2px 0px 0px 0px" class="element">
          <button id="btnSaveArquivo" title="Gravar arquivo" onclick="btnSaveArquivoAuxOnClick();" style="margin-bottom:2px; width:100px; height:23px; border:1px solid #999999" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>&nbsp;Gravar Arquivo </button>
        </div>
      </div>
    </form>
  </div>
</div>
<!--
 *********************************************************************************************************************************
 ******************************************************** Conta Bancária *********************************************************
 *********************************************************************************************************************************
  -->
<div style="width: 386px; display:none;" id="panelContaBancaria" class="d1-form">
  <div style="width: 386px; height: 405px;" class="d1-body">
    <input idform="" reference="" id="contentLogContaBancaria" name="contentLogContaBancaria" type="hidden">
    <input idform="" reference="" id="dataOldContaBancaria" name="dataOldContaBancaria" type="hidden">
    <input idform="contaBancaria" reference="cd_conta_bancaria" id="cdContaBancaria" name="cdContaBancaria" type="hidden">
    <div class="d1-line" id="line0">
      <div style="width: 295px;" class="element">
        <label class="caption" for="cdBanco">Banco</label>
        <select style="width: 292px;" class="select" idform="contaBancaria" reference="cd_banco" datatype="STRING" id="cdBanco" name="cdBanco">
          <option value="0">Selecione...</option>
        </select>
      </div>
      <div style="width: 85px;" class="element">
        <label class="caption" for="nrAgencia">Nº Agência</label>
        <input style="width: 82px;" class="field" idform="contaBancaria" reference="nr_agencia" datatype="STRING" id="nrAgencia" name="nrAgencia" type="text">
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 170px;" class="element">
        <label class="caption" for="tpOperacao">Operação</label>
        <select style="width: 167px;" class="select" idform="contaBancaria" reference="tp_operacao" datatype="STRING" id="tpOperacao" name="tpOperacao">
        </select>
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="nrConta">Nº C/C</label>
        <input style="width: 107px;" class="field" idform="contaBancaria" reference="nr_conta" datatype="STRING" id="nrConta" name="nrConta" type="text"/>
      </div>
      <div style="width: 25px;" class="element">
        <label class="caption" for="nrDv">DV</label>
        <input style="width: 22px;" class="field" idform="contaBancaria" reference="nr_dv" datatype="STRING" id="nrDv" name="nrDv" type="text" maxlength="1"/>
      </div>
      <div style="width: 73px;" class="element">
        <label class="caption" for="stConta">Situação</label>
        <select style="width: 73px;" class="select" idform="contaBancaria" reference="st_conta" datatype="STRING" id="stConta" name="stConta" defaultValue = "1">
        </select>
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="position:relative; float:left; border:1px solid #999; padding:4px; margin-top:5px; _height:31px;">
        <div style="position:absolute; top:-6px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">Quando o titular for diferente da pessoa da conta</div>
        <div style="width: 105px;" class="element">
          <label class="caption" for="nrCpfCnpj">Nº CPF/CNPJ</label>
          <input style="width: 102px;" class="field" idform="contaBancaria" reference="nr_cpf_cnpj" datatype="STRING" id="nrCpfCnpj" name="nrCpfCnpj" type="text" maxlength="18">
        </div>
        <div style="width: 263px;" class="element">
          <label class="caption" for="nmTitular">Titular da conta</label>
          <input style="width: 263px;" class="field" idform="contaBancaria" reference="nm_titular" datatype="STRING" id="nmTitular" name="nmTitular" type="text" maxlength="50">
        </div>
      </div>
    </div>
    <div class="d1-line" id="" style="float:right; width:171px; margin:2px 0px 0px 0px;">
      <div style="width:82px;" class="element">
        <button id="btnSaveContaCategoria" title="Gravar conta" onclick="btnSaveContaBancariaOnClick(null);" style="width:80px; height:25px; border:1px solid #999;" class="toolButton"> <img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>&nbsp;Gravar</button>
      </div>
      <div style="width:80px;" class="element">
        <button id="btnCancelar" title="Voltar para a janela anterior" onclick="formContaBancaria.close();" style="width:80px; height:25px; border:1px solid #999;" class="toolButton"> <img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>&nbsp;Cancelar</button>
      </div>
    </div>
  </div>
</div>
<!--
 *********************************************************************************************************************************
 ******************************************************** Vínculos **************************************************************
 *********************************************************************************************************************************
  -->
<div id="vinculoPanel" class="d1-form" style="display:none; width:405px; height:32px">
  <div style="width: 405px;" class="d1-body">
    <div class="d1-line">
      <div style="width: 185px;" class="element">
        <label class="caption" for="cdVinculo">Tipo de vínculo</label>
        <input name="cdTabelaComissao" type="hidden" reference="cd_tabela_comissao" id="cdTabelaComissao" idform="vinculo" value="0" />
        <select style="width: 182px;" logmessage="Tipo Vínculo" registerclearlog="0" value="0" defaultValue="0" class="select" idform="vinculo" reference="cd_vinculo" maxlength="10" id="cdVinculo" name="cdVinculo" type="text">
        </select>
      </div>
      <div style="width: 80px;" class="element">
        <label class="caption" for="dtVinculo">Data v&iacute;nculo </label>
        <input style="text-transform: uppercase; width: 77px;" mask="##/##/####" datatype="DATETIME" lguppercase="true" logmessage="Data Vínculo" class="field" idform="vinculo" reference="dt_vinculo" maxlength="10" id="dtVinculo" name="dtVinculo" type="text" defaultvalue="%DATE"/>
        <button idform="vinculo" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtVinculo" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"/></button>
      </div>
      <div style="width: 100px;" class="element">
        <label class="caption" for="stVinculo">Situa&ccedil;&atilde;o</label>
        <select style="width: 97px;" logmessage="Situação Vínculo" registerclearlog="0" value="0" defaultValue="1" class="select" idform="vinculo" reference="st_vinculo" maxlength="10" id="stVinculo" name="stVinculo" type="text">
        </select>
      </div>
      <div style="width: 20px;" class="element">
        <label class="caption" style="height:10px">&nbsp;</label>
        <button title="Gravar" onclick="btnSaveVinculoOnClick();" style="margin-bottom:2px" id="btnSaveVinculo" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/></button>
      </div>
    </div>
  </div>
</div>
<!--
 *********************************************************************************************************************************
 ******************************************************** Contato ****************************************************************
 *********************************************************************************************************************************
  -->
<div style="width: 486px; display:none;" id="contato" class="d1-form">
  <div style="width: 486px; height: 230px;" class="d1-body">
    <input idform="" reference="" id="dataOldContato" name="dataOldContato" type="hidden"/>
    <input idform="contato" reference="cd_pessoa_contato" id="cdContato" name="cdContato" type="hidden"/>
    <input idform="contato" reference="cd_endereco" id="cdEnderecoContato" name="cdEnderecoContato" type="hidden" value="0" defaultValue="0"/>
    <input idform="contato" reference="cd_usuario" id="cdUsuarioContato" name="cdUsuarioContato" type="hidden" value="0" defaultValue="0"/>
    <div class="d1-line" id="line0">
      <div style="width: 235px;" class="element">
        <label class="caption" for="nmContato">Nome do contato</label>
        <input style="width: 232px; text-transform:uppercase" class="field" lguppercase="true" idform="contato" reference="nm_pessoa_contato" datatype="STRING" id="nmContato" name="nmContato" type="text">
      </div>
      <div style="width: 161.667px;" class="element">
        <label class="caption" for="nmApelido">Apelido</label>
        <input style="width: 158.667px; text-transform:uppercase" class="field" idform="contato" lguppercase="true" reference="nm_apelido" datatype="STRING" id="nmApelidoContato" name="nmApelidoContato" type="text">
      </div>
      <div style="width: 85px;" class="element">
        <label class="caption" for="dtCadastro">Data cadastro</label>
        <input style="width: 82px;" disabled="true" mask="##/##/####" lguppercase="true" defaultvalue="%DATE" class="disabledField" idform="contato" reference="dt_cadastro" datatype="DATE" maxlength="10" id="dtCadastroContato" name="dtCadastroContato" type="text">
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 242.5px;" class="element">
        <label class="caption" for="nmSetor">Setor/Departamento</label>
        <input style="width: 239.5px; text-transform:uppercase" class="field" idform="contato" lguppercase="true" reference="nm_setor" datatype="STRING" id="nmSetorContato" name="nmSetorContato" type="text">
      </div>
      <div style="width: 242.5px;" class="element">
        <label class="caption" for="nmFuncao">Função/Cargo</label>
        <select style="width: 239.5px;" class="select" idform="contato" reference="cd_funcao" datatype="INT" id="cdFuncaoContato" name="cdFuncaoContato" defaultvalue="0">
          <option value="0">Selecione...</option>
        </select>
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 161.667px;" class="element">
        <label class="caption" for="nrTelefone1">Telefone</label>
        <input style="width: 158.667px;" mask="(##)####-####" lguppercase="true" class="field" idform="contato" reference="nr_telefone1" datatype="STRING" id="nrTelefone1Contato" name="nrTelefone1Contato" type="text">
      </div>
      <div style="width: 161.667px;" class="element">
        <label class="caption" for="nrTelefone2">Fax</label>
        <input style="width: 158.667px;" mask="(##)####-####" class="field" idform="contato" reference="nr_telefone2" datatype="STRING" id="nrTelefone2Contato" name="nrTelefone2Contato" type="text">
      </div>
      <div style="width: 161.667px;" class="element">
        <label class="caption" for="nrCelular">Celular</label>
        <input style="width: 158.667px;" mask="(##)####-####" class="field" idform="contato" reference="nr_celular" datatype="STRING" id="nrCelularContato" name="nrCelularContato" type="text">
      </div>
    </div>
    <div class="d1-line" id="line3">
      <div style="width: 242.5px;" class="element">
        <label class="caption" for="nmEmail">E-mail</label>
        <input style="text-transform: lowercase; width: 239.5px;" class="field" idform="contato" reference="nm_email" datatype="STRING" id="nmEmailContato" name="nmEmailContato" type="text">
      </div>
      <div style="width: 242.5px;" class="element">
        <label class="caption" for="nmUrl">Homepage</label>
        <input style="text-transform: lowercase; width: 239.5px;" class="field" idform="contato" reference="nm_url" datatype="STRING" id="nmUrlContato" name="nmUrlContato" type="text">
      </div>
    </div>
    <div class="d1-line" id="">
      <div style="width: 485px;" class="element">
        <label class="caption" for="txtObservacao">Observação</label>
        <textarea style="width: 482px; height:60px;" class="textarea" idform="contato" reference="txt_observacao" datatype="STRING" id="txtObservacaoContato" name="txtObservacaoContato"></textarea>
      </div>
    </div>
    <div class="d1-line" id="" style="float:right; width:164px; margin:2px 0px 0px 0px;">
      <div style="width:80px;" class="element">
        <button id="btnSaveContato" title="Gravar informações" onclick="btnSaveContatoOnClick(null);" style="width:80px; height:20px; border:1px solid #999999" class="toolButton"> <img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Confirmar </button>
      </div>
      <div style="width:80px;" class="element">
        <button id="btnCancelarContato" title="Voltar para a janela anterior" onclick="formContato.close();" style="margin-left:2px; height:20px; width:80px; border:1px solid #999999" class="toolButton"> <img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar </button>
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
