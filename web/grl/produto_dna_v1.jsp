<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="com.tivic.manager.grl.EmpresaServices"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="com.tivic.manager.util.Recursos"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.alm.*"%>
<%
	try {
		int cdEmpresa            = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		Empresa empresa          = EmpresaDAO.get(cdEmpresa);
		int cdLocalArmazenamento = RequestUtilities.getParameterAsInteger(request, "cdLocalArmazenamento", 0);
		int tpProdutoServico     = RequestUtilities.getParameterAsInteger(request, "tpProdutoServico", 0);
		int cdGrupo              = RequestUtilities.getParameterAsInteger(request, "cdGrupo", 0);
		Grupo grupo              = cdGrupo==0 ? null : GrupoDAO.get(cdGrupo);
		int cdProdutoServico     = RequestUtilities.getParameterAsInteger(request, "cdProdutoServico", 0);
		int cdFormulario         = grupo==null ? 0 : grupo.getCdFormulario();
		int cdVinculoFornecedor  = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FORNECEDOR", 0);
		int hiddenClassificacaoFiscal = RequestUtilities.getParameterAsInteger(request, "hiddenClassificacaoFiscal", 0);
		// Parametros para mostrar o preço na consulta, importante para cliente
		int cdTipoOperacaoVarejo   = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, cdEmpresa);
		String nmTabelaPrecoVarejo = "";
		if(cdTipoOperacaoVarejo>0)	{
			TabelaPreco tabVarejo = TipoOperacaoServices.getTabelaPrecoOfOperacao(cdTipoOperacaoVarejo);
			if(tabVarejo!=null)
				nmTabelaPrecoVarejo = tabVarejo.getNmTabelaPreco();
		}
		int cdTipoOperacaoAtacado = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_ATACADO", 0, cdEmpresa);
		String nmTabelaPrecoAtacado = "";
		if(cdTipoOperacaoAtacado>0)	{
			TabelaPreco tabAtacado = TipoOperacaoServices.getTabelaPrecoOfOperacao(cdTipoOperacaoAtacado);
			if(tabAtacado!=null)
				nmTabelaPrecoAtacado = tabAtacado.getNmTabelaPreco();
		}
		int lgFotoProduto         = (tpProdutoServico <= 0 ? ParametroServices.getValorOfParametroAsInteger("LG_FOTO_PRODUTO", 0, cdEmpresa) : 0);
		int lgGrupoHierarquico    = ParametroServices.getValorOfParametroAsInteger("LG_GRUPO_HIERARQUICO", 0, cdEmpresa);
		String today           = sol.util.Util.formatDateTime(new java.util.GregorianCalendar(), "dd/MM/yyyy");
		String rodape             = RequestUtilities.getParameterAsString(request, "tpPessoa", ""); 
		int lgRelatorioEstilizado        = ParametroServices.getValorOfParametroAsInteger("LG_RELATORIO_ESTILIZADO", 0, 0);
		//------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------ Parametros ------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------
%>
<security:registerForm idForm="formProduto"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<loader:library libraries="shortcut, form, toolbar, grid2.0, treeview2.0, aba2.0, filter, calendario, floatmenu, stringTokenizer" compress="false"/>
<script language="javascript">
var disabledFormProdutoServico = false;
var disabledFormComponente     = false;
var columnsGrupo = [{label:'Nome grupo', reference: 'NM_GRUPO'}, 
					{label:'Principal', reference: 'LG_PRINCIPAL', type: GridOne._CHECKBOX, onCheck: onClickLgPrincipal}];
var columnsLocalArmazenamento = [{label: 'Nome do local de armazenamento', reference: 'NM_LOCAL_ARMAZENAMENTO'}, 
								 {label: 'Estoque', reference: 'QT_ESTOQUE', type: GridOne._FLOAT},
								 {label: 'Estoque consignado', reference: 'QT_ESTOQUE_CONSIGNADO', type: GridOne._FLOAT},
								 {label: 'Estoque total', reference: 'QT_ESTOQUE_TOTAL', type: GridOne._FLOAT}];
var columnsMovimento = [{label: 'Tipo', reference: 'DS_MOVIMENTO'}, 
						{label: 'Data', reference: 'DT_MOVIMENTO', type: GridOne._DATE}, 
						{label: 'Local Armazenamento', reference: 'NM_LOCAL_ARMAZENAMENTO'}, 
						{label: 'Quantidade', reference: 'QT_MOVIMENTO', type: GridOne._FLOAT}];
var isIdByGrupo = <%=ProdutoServicoEmpresaServices.isIdReduzidoByGrupo(cdEmpresa)%>;						
var gridGrupos;
var gridLocaisArmazenamento;
var gridEstoquesComponentes;
var gridMovimentos;
var tabProdutoServico;
var tabDadosComplementares;
var tabComposicao;
var tvComponentes;
var toolBar            = null;
var cdAtributoInEdicao = null;
var atributos          = [];
var cdGrupo                   = <%=cdGrupo%>;
var hiddenClassificacaoFiscal = <%=hiddenClassificacaoFiscal == 1%>;
var cdFormularioDefault       = <%=cdFormulario%>;
var cdVinculoFornecedor       = <%=cdVinculoFornecedor%>;
var recursosFloatMenu = null;
var rsmUnidadeMedida = <%=sol.util.Jso.getStream(com.tivic.manager.grl.UnidadeMedidaServices.getAll())%>; 
var tiposReabastecimento = <%=sol.util.Jso.getStream(ProdutoServicoEmpresaServices.tiposAbastecimento)%>; 
// var fotosAdicionaisFields;

function initProdutoServico()	{
    var cdEmpresa = parent.$('cdEmpresa')!=null ? parent.$('cdEmpresa').value : 0;
	$('cdEmpresa').value = cdEmpresa;
	$('cdEmpresa').setAttribute('defaultValue', cdEmpresa);
	if (cdFormularioDefault > 0) {
		$("txtEspecificacao").style.width            = (parseInt($("txtEspecificacao").style.width, 10) - 337) + "px";
		$("txtEspecificacao").parentNode.style.width = (parseInt($("txtEspecificacao").style.width, 10) + 3) + "px";
		$("txtDadoTecnico").style.width              = (parseInt($("txtDadoTecnico").style.width, 10) - 337) + "px";
		$("txtDadoTecnico").parentNode.style.width   = (parseInt($("txtDadoTecnico").style.width, 10) + 3) + "px";
		$("txtPrazoEntrega").style.width             = (parseInt($("txtPrazoEntrega").style.width, 10) - 337) + "px";
		$("txtPrazoEntrega").parentNode.style.width  = (parseInt($("txtPrazoEntrega").style.width, 10) + 3) + "px";
		$("txtPrazoEntrega").parentNode.parentNode.parentNode.style.width = (parseInt($("txtPrazoEntrega").style.width, 10)) + "px";		
		$("divAtributos").style.display              = '';
		loadAtributos(null, cdFormularioDefault);
	}	
	else {
		initProdutoServicoTemp();
	}
}

function initProdutoServicoTemp() {
	toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'horizontal',
								    		 buttons: [{id: 'btnNewProdutoServico', img: '/sol/imagens/form-btNovo24.gif', title: 'Novo... [Ctrl + N]', onClick: btnNewProdutoServicoOnClick},
								    		           {separator: 'horizontal'},
								    		           {id: 'btnAlterProdutoServico', img: '/sol/imagens/form-btAlterar24.gif', title: 'Alterar... [Ctrl + A]', onClick: btnAlterProdutoServicoOnClick},
								    	     		   {separator: 'horizontal'},
								    	     		   {id: 'btnSaveProdutoServico', img: '/sol/imagens/form-btSalvar24.gif', onClick: btnSaveProdutoServicoOnClick},
										    		   {separator: 'horizontal'},
										    		   {id: 'btnDeleteProdutoServico', img: '/sol/imagens/form-btExcluir24.gif', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteProdutoServicoOnClick},
										    		   {separator: 'horizontal'},
												       {id: 'btnAtualizarProdutoServico', img: '/sol/imagens/form-btAtualizar24.gif', title: 'Atualizar...', onClick: btnAtualizarProdutoServicoOnClick},
										    		   {separator: 'horizontal'},
												       {id: 'btnPesquisar', img: '/sol/imagens/form-btPesquisar24.gif', title: 'Pesquisar...', onClick: btnFindProdutoServicoOnClick},
													   {separator: 'horizontal'},
													   {id: 'btnRecursos', img: '../imagens/opcoes24.gif', title: 'Outras Opções', onClick: btnRecursosOnClick},
													   {type: 'space', width: 450},
													   {id: 'btnFirst', img: '../grl/imagens/first24.png', title: 'Primeiro', onClick: btnPrimeiroRegistro},
													   {id: 'btnPrevious', img: '../grl/imagens/previous24.png', title: 'Anterior', onClick: btnAnteriorRegistro},
													   {id: 'btnNext', img: '../grl/imagens/next24.png', title: 'Próximo', onClick: btnProximoRegistro},
													   {id: 'btnLast', img: '../grl/imagens/last24.png', title: 'Último', onClick: btnUltimoRegistro}
													   ]});

	var toolBarComponentes = ToolBar.create('toolBarComponentes', {plotPlace: 'toolBarComponentes', orientation: 'horizontal',
								    		 buttons: [{id: 'btnNewComponente', img: '/sol/imagens/form-btNovo24.gif', label: 'Novo', title: 'Novo componente ', onClick: btnNewComponenteOnClick},
								    	     		   {id: 'btnAlterComponente', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar', title: 'Alterar componente', onClick: btnAlterComponenteOnClick},
										    		   {id: 'btnSaveComponente', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar', onClick: btnSaveComponenteOnClick},
								    	     		   {id: 'btnDeleteComponente', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir', title: 'Excluir componente ', onClick: btnDeleteComponenteOnClick},
										    		   {separator: 'horizontal'},
												       {id: 'btnPesquisarComponente', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar', title: 'Pesquisar componente', onClick: btnFindComponenteOnClick},
												       {id: 'btnPrintProdutoServico', img: 'imagens/form-btRelatorio24.gif', label: 'Imprimir', title: 'Imprimir estrutura', onClick: btnPrintComponenteOnClick}]});

	var toolBarSubstituto = ToolBar.create('toolBarSubstituto', {plotPlace: 'toolBarSubstituto', orientation: 'horizontal',
											 buttons: [{id: 'btnNewSubstituto', img: '/sol/imagens/form-btNovo24.gif', label: 'Novo', title: 'Novo substituto ', onClick: btnNewSubstitutoOnClick},
										     		   {id: 'btnAlterSubstituto', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar', title: 'Alterar substituto', onClick: btnAlterSubstitutoOnClick},
										    		   {id: 'btnSaveSubstituto', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar', title: 'Gravar substituto', onClick: btnSaveSubstitutoOnClick},
										     		   {id: 'btnDeleteSubstituto', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir', title: 'Excluir substituto ', onClick: btnDeleteSubstitutoOnClick},
										     		   {id: 'btnGrade', img: '../alm/imagens/produto_similar24.gif', label: 'Grade', title: 'Incluir grade...', onClick: btnGradeOnClick}]});

	var toolBarRelacionado = ToolBar.create('toolBarRelacionado', {plotPlace: 'toolBarRelacionado', orientation: 'horizontal',
											 buttons: [{id: 'btnNewRelacionado', img: '/sol/imagens/form-btNovo24.gif', label: 'Novo', title: 'Novo relacionado ', onClick: btnNewRelacionadoOnClick},
										     		   {id: 'btnAlterRelacionado', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar', title: 'Alterar relacionado', onClick: btnAlterRelacionadoOnClick},
										    		   {id: 'btnSaveRelacionado', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar', title: 'Gravar relacionado', onClick: btnSaveRelacionadoOnClick},
										     		   {id: 'btnDeleteRelacionado', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir', title: 'Excluir relacionado ', onClick: btnDeleteRelacionadoOnClick}]});

	var toolBarReabastecimento = ToolBar.create('toolBarReabastecimento', {plotPlace: 'toolBarReabastecimento', orientation: 'horizontal',
											 buttons: [{id: 'btnNewReabastecimento', img: '/sol/imagens/form-btNovo24.gif', label: 'Novo', title: 'Novo reabastecimento ', onClick: btnNewReabastecimentoOnClick},
										     		   {id: 'btnAlterReabastecimento', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar', title: 'Alterar reabastecimento', onClick: btnAlterReabastecimentoOnClick},
										    		   {id: 'btnSaveReabastecimento', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar', title: 'Gravar reabastecimento', onClick: btnSaveReabastecimentoOnClick},
										     		   {id: 'btnDeleteReabastecimento', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir', title: 'Excluir reabastecimento ', onClick: btnDeleteReabastecimentoOnClick}]});

	var toolBarFotos = ToolBar.create('toolBarFotos', {plotPlace: 'toolBarFotos', orientation: 'horizontal',
											 buttons: [{id: 'btnNewFotos', img: '/sol/imagens/form-btNovo24.gif', label: 'Nova', title: 'Nova foto', onClick: btnNewFotosOnClick},
										     		   {id: 'btnAlterFotos', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar', title: 'Alterar foto', onClick: btnAlterFotosOnClick},
										    		   {id: 'btnSaveFotos', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar', title: 'Gravar foto', onClick: btnSaveFotosOnClick},
										     		   {id: 'btnDeleteFotos', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir', title: 'Excluir foto ', onClick: btnDeleteFotosOnClick},
										     		   {type: 'space', width: 150},
													   {id: 'btnFirst', img: 'imagens/first24.png', title: 'Primeiro', onClick: btnPrimeiraFotoOnClick},
													   {id: 'btnPrevious', img: 'imagens/previous24.png', title: 'Anterior', onClick: btnFotoAnteriorOnClick},
													   {id: 'btnNext', img: 'imagens/next24.png', title: 'Próximo', onClick: btnProximaFotoOnClick},
													   {id: 'btnLast', img: 'imagens/last24.png', title: 'Último', onClick: btnUltimaFotoOnClick}]});

	var toolBarCaracteristicas = ToolBar.create('toolBarCaracteristicas', {plotPlace: 'toolBarCaracteristicas', orientation: 'horizontal',
											 buttons: [{id: 'btnNewCaracteristicas', img: '/sol/imagens/form-btNovo24.gif', label: 'Nova', title: 'Nova característica', onClick: btnNewCaracteristicasOnClick},
										     		   {id: 'btnAlterCaracteristicas', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar', title: 'Alterar característica', onClick: btnAlterCaracteristicasOnClick},
										    		   {id: 'btnSaveCaracteristicas', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar', title: 'Gravar característica', onClick: btnSaveCaracteristicasOnClick},
										     		   {id: 'btnDeleteCaracteristicas', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir', title: 'Excluir característica ', onClick: btnDeleteCaracteristicasOnClick}]});

	var toolBarCodigoBarras = ToolBar.create('toolBarCodigoBarras', {plotPlace: 'toolBarCodigoBarras', orientation: 'horizontal',
											 buttons: [{id: 'btnNewCodigoBarras', img: '/sol/imagens/form-btNovo24.gif', label: 'Nova', title: 'Novo código de barras', onClick: btnNewCodigoBarrasOnClick},
										     		   {id: 'btnAlterCodigoBarras', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar', title: 'Alterar código de barras', onClick: btnAlterCodigoBarrasOnClick},
										    		   {id: 'btnSaveCodigoBarras', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar', title: 'Gravar código de barras', onClick: btnSaveCodigoBarrasOnClick},
										     		   {id: 'btnDeleteCodigoBarras', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir', title: 'Excluir código de barras', onClick: btnDeleteCodigoBarrasOnClick}]});

	var toolBarOutrosCodigos = ToolBar.create('toolBarOutrosCodigos', {plotPlace: 'toolBarOutrosCodigos', orientation: 'horizontal',
											 buttons: [{id: 'btnNewOutrosCodigos', img: '/sol/imagens/form-btNovo24.gif', label: 'Nova', title: 'Novo código', onClick: btnNewOutrosCodigosOnClick},
										     		   {id: 'btnAlterOutrosCodigos', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar', title: 'Alterar código', onClick: btnAlterOutrosCodigosOnClick},
										    		   {id: 'btnSaveOutrosCodigos', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar', title: 'Gravar código', onClick: btnSaveOutrosCodigosOnClick},
										     		   {id: 'btnDeleteOutrosCodigos', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir', title: 'Excluir código', onClick: btnDeleteOutrosCodigosOnClick}]});

	var toolBarGrade = ToolBar.create('toolBarGrade', {plotPlace: 'toolBarGrade', orientation: 'horizontal',
											 buttons: [{id: 'btnNewGrade', img: '/sol/imagens/form-btNovo24.gif', label: 'Nova', title: 'Novo código', onClick: btnNewGradeOnClick},
										     		   {id: 'btnAlterGrade', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar', title: 'Alterar código', onClick: btnAlterGradeOnClick},
										    		   {id: 'btnSaveGrade', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar', title: 'Gravar código', onClick: btnSaveGradeOnClick},
										     		   {id: 'btnDeleteGrade', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir', title: 'Excluir código', onClick: btnDeleteGradeOnClick}]});


	addShortcut('ctrl+0', function(){ tabProdutoServico.showTab(0)});
	addShortcut('ctrl+1', function(){ tabProdutoServico.showTab(1)});
	addShortcut('ctrl+2', function(){ tabProdutoServico.showTab(2)});
	addShortcut('ctrl+3', function(){ tabProdutoServico.showTab(3)});
	addShortcut('ctrl+n', function(){ if (!$('btnNewProdutoServico').disabled) btnNewProdutoServicoOnClick() });
	addShortcut('ctrl+a', function(){ if (!$('btnAlterProdutoServico').disabled) btnAlterProdutoServicoOnClick() });
	addShortcut('ctrl+p', function(){ if (!$('btnFindProdutoServico').disabled) btnFindProdutoServicoOnClick() });
	addShortcut('ctrl+e', function(){ if (!$('btnDeleteProdutoServico').disabled) btnDeleteProdutoServicoOnClick() });
	addShortcut('ctrl+x', function(){parent.closeWindow('jProdutoServico')});
	addShortcut('shift+m', function(){ if (!$('btnConsultarMovimento').disabled) { tabProdutoServico.showTab(3); btnConsultarMovimentoOnClick()} });
	$('nmProdutoServico').nextElement  = $('txtProdutoServico');
	$('txtProdutoServico').nextElement = $('sgProdutoServico');

	loadOptions($('tpControleEstoque'), <%=sol.util.Jso.getStream(ProdutoServicoEmpresaServices.tiposControleEstoque)%>);
	loadOptions($('tpAbastecimento'), <%=sol.util.Jso.getStream(ProdutoServicoEmpresaServices.tiposAbastecimento)%>);
	loadOptions($('tpReabastecimentoRelacionado'), <%=sol.util.Jso.getStream(ProdutoServicoEmpresaServices.tiposAbastecimento)%>);
	loadTabelasPrecos();

	loadOptionsFromRsm($('cdUnidadeMedida'),           rsmUnidadeMedida, {fieldValue: 'cd_unidade_medida', fieldText:'nm_unidade_medida'});
	loadOptionsFromRsm($('cdUnidadeMedidaForn'),       rsmUnidadeMedida, {fieldValue: 'cd_unidade_medida', fieldText:'nm_unidade_medida'});
	loadOptionsFromRsm($('cdUnidadeMedidaComponente'), rsmUnidadeMedida, {fieldValue: 'cd_unidade_medida', fieldText:'nm_unidade_medida'});
	loadOptionsFromRsm($('cdMoeda'),                   <%=sol.util.Jso.getStream(com.tivic.manager.grl.MoedaDAO.getAll())%>, {fieldValue: 'cd_moeda', fieldText:'nm_moeda'});
	loadOptionsFromRsm($('cdClassificacaoFiscal'), <%= sol.util.Jso.getStream(com.tivic.manager.adm.ClassificacaoFiscalDAO.getAll())%>, {fieldValue: 'cd_classificacao_fiscal', fieldText:'nm_classificacao_fiscal'});
	
	$('idReduzido').nextElement                      = $('qtDiasGarantia');
	$('qtDiasGarantia').nextElement                  = $('tpControleEstoque');
	$('tpControleEstoque').nextElement               = $('tpReabastecimento');
	$('tpReabastecimento').nextElement               = $('qtIdeal');
	$('qtDiasEstoqueLocalArmazenamento').nextElement = $('btnSaveLocalArmazenamento');
	$('txtPrazoEntrega').nextElement                 = $('vlLargura');
	$('vlPesoUnitarioEmbalagem').nextElement         = $('btnSaveProdutoServico');

	var dtDesativacaoMask = new Mask($("dtDesativacao").getAttribute("mask"), "date");
	dtDesativacaoMask.attach($("dtDesativacao"));

	$('idProdutoServico').onblur = function()	{ idProdutoServicoOnBlur(null, this.value); };
	
	enableTabEmulation();
    produtoServicoFields = [];
	localArmazenamentoFields = [];
	aliquotaIcmsFields = [];
	fotosAdicionaisFields = [];
	caracteristicasFields = [];
	outrosCodigosFields = [];
	loadFormFields(["produtoServico", "localArmazenamento", "aliquotaIcms", "preco", "fornecedor", "componente", "fotoProduto", "pesquisa", "grade", "relacionado", "substituto", "codigoBarras", "fotosAdicionais", "caracteristicas", "outrosCodigos", "reabastecimento"]);

    var dataMask    = new Mask("##/##/####");
	var maskInteger = new Mask("#,###", "number");
	var maskNumber  = new Mask("#,###.00", "number");

	maskNumber.attach($("qtIdeal"));
	maskNumber.attach($("vlUltimoPreco"));
	maskNumber.attach($("qtMinima"));
	maskNumber.attach($("qtMaxima"));
	maskNumber.attach($("qtIdealLocalArmazenamento"));
	maskNumber.attach($("qtMinimaLocalArmazenamento"));
	maskNumber.attach($("qtMinimaEcommerceLocalArmazenamento"));
	maskNumber.attach($("qtMaximaLocalArmazenamento"));
	maskNumber.attach($("vlPesoUnitario"));
	maskNumber.attach($("vlPesoUnitarioEmbalagem"));
	maskNumber.attach($("vlComprimento"));
	maskNumber.attach($("vlLargura"));
	maskNumber.attach($("vlAltura"));
	maskNumber.attach($("vlComprimentoEmbalagem"));
	maskNumber.attach($("vlLarguraEmbalagem"));
	maskNumber.attach($("vlAlturaEmbalagem"));
	maskNumber.attach($("qtProdutoServico"));
	maskNumber.attach($("prPerda"));
	maskNumber.attach($("vlPrecoVarejo"));
	maskNumber.attach($("vlPrecoAtacado"));
	maskNumber.attach($("qtTransferencia"));
	maskNumber.attach($("qtMinimaReabastecimento"));
	maskNumber.attach($("qtMaximaReabastecimento"));
	maskNumber.attach($("qtIdealReabastecimento"));
	changeLayout();
	changeLayoutDadosComplementares();
	changeLayoutSubstituto();
	changeLayoutRelacionado();
	changeLayoutReabastecimento();
	changeLayoutGrupos((gridGrupos != null ? gridGrupos.size() : 0));
// 	loadSimilares();
// 	loadReferenciados();
	loadPrecos();
	loadTributos();
	loadFornecedores();
	//
	loadPesquisa();
	loadEstoque();
	loadSubstituto();
 	loadRelacionado();
	loadReabastecimento();
	loadCaracteristicas();
	loadCodigoBarras();
	loadOutrosCodigos();
	loadTabelasPrecos();
	
	
	if (cdGrupo <= 0) { 
		loadGrupos();
	} 
	<% if (lgFotoProduto == 1) { %>
			loadFotoProduto();
	<% } %>

	initComponente();
	btnConsultarEstoqueOnClick(null, true);
	btnConsultarMovimentoOnClick(null, true);
	
	if ($('btnNewProdutoServico').disabled || $('cdProdutoServico').value != '0') {
		disabledFormProdutoServico=true;
		alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField2");
	}
	else {
		clearFormProdutoServico();
	    $('idProdutoServico').focus();
	}

	<% if (cdProdutoServico > 0) { %>
			loadProduto(null, <%=cdProdutoServico%>);
	<% } %>
}

function idProdutoServicoOnBlur(content, value)	{
	if (content == null) {
		if(value=='' || value==null)	{
			$('nmProdutoServico').focus();
			$('nmProdutoServico').select();
			return;
		}
		$('idProdutoServico').value = value.toUpperCase();
		var params = 'objects=crt=java.util.ArrayList();&execute=';
		params = createItemComparator(params, 'id_produto_servico', 'idProdutoServico', _EQUAL, _VARCHAR);
		params = createItemComparator(params, 'A.cd_empresa', '<%=cdEmpresa%>', _EQUAL, _INTEGER);
		params = createItemComparator(params, 'A.TP_PRODUTO_SERVICO', '0', _EQUAL, _INTEGER);
		params = createItemComparator(params, 'cd_local_armazenamento', '<%=cdLocalArmazenamento%>', _EQUAL, _INTEGER);
		
		getPage("GET", "idProdutoServicoOnBlur", '../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
		        '&' + params +
				'&method=findCompleto(*crt:java.util.ArrayList)', null, true, null, null);
	}
	else {
		var rsm = null;
		try { 
			rsm = eval("(" + content + ")"); 
		} catch(e) {}
		if (rsm!=null && rsm.lines.length != 0)
			btnFindProdutoServicoOnClick(rsm.lines);
	}
}

function btnRecursosOnClick() {
	if (recursosFloatMenu == null) {
		recursosFloatMenu = FloatMenuOne.create('printMenu', {width: 100, height: 100, plotPlace: $('produtoBody')});
		recursosFloatMenu.insertItem({label: 'Estoques', icon: 'imagens/estoques16.gif', onclick: btnDisponilidadeEstoqueOnClick});
		recursosFloatMenu.insertItem({label: 'Movimentação (Entradas e Saídas)', icon: 'imagens/movimentos16.gif', onclick: btnMovimentacaoProdutoOnClick});
		recursosFloatMenu.insertItem({label: 'Atualizar Custos e Preços', icon: '../adm/imagens/preco16.gif', onclick: btnUpdateCustosAndPrecosOnClick});
		recursosFloatMenu.insertItem({label: 'Identificar Regras de Preços', icon: '../adm/imagens/tabela_preco16.gif', onclick: btnViewRegrasPrecosOnClick});
		recursosFloatMenu.insertItem({label: 'Etiquetas', icon: '../adm/imagens/controle_boleto16.gif', onclick: function(){btnEtiquetaOnClick(0);}});
		recursosFloatMenu.insertItem({label: 'Fechar este menu', icon: '/sol/imagens/cancel_13.gif'});
	}
	recursosFloatMenu.show({element: $('btnRecursos')});
}

var columnsRegrasItens = [{label:'Prior.', reference:'NR_PRIORIDADE'},
						  {label:'Critérios para aplicação da Regra', reference:'clDS_CRITERIOS', style: 'white-space:normal;'},
						  {label:'Base Cálculo', reference:'clTP_VALOR_BASE', style: 'width:120px; white-space:normal;'},
						  {label:'% Lucro', reference:'PR_MARGEM_LUCRO', type:GridOne._CURRENCY, headerStyle: 'width:55px; white-space:normal; text-align: center;', style: 'text-align: center;'}];
var gridRegrasItens = null;
var rsmRegrasItens = null;
function btnViewRegrasPrecosOnClick(content) {
	if (content==null) {
		if ($('cdProdutoServico').value <= 0)
			showMsgbox('Manager', 300, 50, 'Nenhum Produto carregado...');
		else {
			createTempbox('jInfo', {message: 'Consultando Dados. Aguarde...', width: 160, height: 100});
			getPage('GET', 'btnViewRegrasPrecosOnClick', 
					'../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices'+
					'&method=getAllRegras(const ' +  $('cdProdutoServico').value + ':int, const ' + $('cdTabelaPrecoOfRegras').value + ':int)', null);
		}
	}
	else {
		closeWindow('jInfo');
		rsmRegrasItens = null;
		try {rsmRegrasItens = eval('(' + content + ')')} catch(e) {};
		gridRegrasItens = GridOne.create('gridRegrasItens', {columns: columnsRegrasItens, resultset :rsmRegrasItens, plotPlace : $('divGridRegrasItens'),
						                                     columnSeparator: false, noSelectorColumn: true, noSelectOnCreate: true,
															 onProcessRegister: function(reg) {
															 						var dsCriterios = '';
																					var nmGrupo           = reg['CD_GRUPO']==null || reg['CD_GRUPO']==0 ? null : reg['NM_GRUPO'];
																					if (reg['CD_GRUPO']!=null && reg['CD_GRUPO']!=0 && reg['NM_GRUPO']!='' && reg['CD_GRUPO_SUPERIOR']!=0 && reg['NM_GRUPO_SUPERIOR']!=null && reg['NM_GRUPO_SUPERIOR']!='')
																						nmGrupo = reg['NM_GRUPO_SUPERIOR'] + ' - ' + nmGrupo;
																					var nmProdutoServico  = reg['CD_PRODUTO_SERVICO_REGRA']==0 ? null : reg['NM_PRODUTO_SERVICO_REGRA'];
																					var nmFornecedor      = reg['CD_FORNECEDOR']           ==0 ? null : reg['NM_FORNECEDOR'];
																					var nmTabelaPrecoBase = reg['CD_TABELA_PRECO_BASE']    ==0 ? null : reg['NM_TABELA_PRECO_BASE'];
																					dsCriterios += (nmGrupo!=null           ? 'Grupo: ' + nmGrupo : '');
																					dsCriterios += (nmProdutoServico!=null  ? (dsCriterios!='' ? '<br/>' : '') + 'Produto/Serviço: ' + nmProdutoServico : '');
																					dsCriterios += (nmFornecedor!=null      ? (dsCriterios!='' ? '<br/>' : '') + 'Fornecedor: ' + nmFornecedor : '');
																					dsCriterios += (nmTabelaPrecoBase!=null ? (dsCriterios!='' ? '<br/>' : '') + 'Tabela de Preço Base: ' + nmTabelaPrecoBase : '');
																					reg['CLDS_CRITERIOS'] = dsCriterios;
																					reg['CLTP_VALOR_BASE'] = reg['TP_VALOR_BASE']==null ? '' : reg['TP_VALOR_BASE']==<%=TabelaPrecoRegraServices.TP_BASE_ULTIMO_CUSTO%> ? 'Último Custo (' + $('vlUltimoCusto').value + ')' :
																											   reg['TP_VALOR_BASE']==<%=TabelaPrecoRegraServices.TP_BASE_CUSTO_MEDIO%> ? 'Custo Médio (' + $('vlCustoMedio').value + ')' : 
																											   'Tabela Base (' + formatCurrency(reg['VL_PRECO_BASE']) + ')';
																			 }
		                                                       });
		createWindow('jReportRegrasItens', {caption: "Regras de Preços aplicáveis a este Produto", width: 604, height: 315, noDropContent: true, columnSeparator: false,
									        modal: true, noDrag: true, contentDiv: 'formReportRegrasItens'});
	}
}

function btnEtiquetaOnClick(qtEtiquetas)	{
	if(qtEtiquetas <= 0)	{
		if($('idReduzido').value=='') {
			alert('Você deve informar um ID Reduzido!');
			return;
		}
		FormFactory.createFormWindow('jEtiqueta', 
		        {caption: "Listando Etiquetas", width: 150, height: 90, noDrag: true, modal: true, id: 'etiqueta', unitSize: '%', cssVersion: '2',
				 lines: [[{type: 'text', label: 'Qtd. Etiquetas', id: 'qtEtiqueta', width: 100, value: $('qtEstoque').value}],
				         [{id:'btnPrint',  type:'button', label:'Imprimir', width:50, height: 20, onClick: function(){btnEtiquetaOnClick($('qtEtiqueta').value);}}, 
						  {id:'btnCancel', type:'button', label:'Cancelar', width:50, height: 20, onClick: function(){ closeWindow('jEtiqueta'); }}]],
				 focusField:'qtEtiqueta'});
	}
	else	{
		closeWindow('jEtiqueta');
		$('tableEtiqueta').style.display = 'table';
		var table   = $('tableEtiqueta');
		while (table.rows.length > 0)
			table.deleteRow(0);
		var qtTd = 0;
		var tr   = document.createElement('tr');
		var nmCompleto  = $('nmProdutoServico').value;
		// Cor
		if($('txtEspecificacao').value!=null && $('txtEspecificacao').value!='')
			nmCompleto += ' - '+$('txtEspecificacao').value.toUpperCase();
		// Tamanho
		if($('txtDadoTecnico').value!=null && $('txtDadoTecnico').value!='')
			nmCompleto += ' - '+$('txtDadoTecnico').value.toUpperCase();

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
		
		var nmEmpresa = '<%=empresa.getNmPessoa()%>';
		parent.createWindow('jRelatorioEtiqueta', {caption: 'ETIQUETAS', width: frameWidth-20, height: frameHeight-50,
                                                     contentUrl: '../alm/etiquetas.jsp?idReduzido=' +$('idReduzido').value+'&clNome=' + nmCompleto +
                                                    		     '&nrReferencia='+$('nrReferencia').value+
                                                    		     '&vlPreco=' + changeLocale($('vlPrecoVarejo')) + '&qtEtiquetas=' + 
                                                    		     qtEtiquetas + '&nmEmpresa=' + nmEmpresa});
		
// 		for(var l=0; l<qtEtiquetas; l++)	{
// 			qtTd++;
// 			var tdSpace = document.createElement('td');
// 			tdSpace.innerHTML = '<div style="height:25mm; width: 5mm;"/>';
// 			var td0 = document.createElement('td');
// 			td0.width             = '9%';
// 			td0.style.cssText    += 'border-top: 1px solid #000; border-bottom: 1px solid #000; border-left: 1px solid #000;';
// 			td0.innerHTML         = '&nbsp';
// 			var td1 = document.createElement('td');
// 			td1.style.cssText    += 'border-top: 1px solid #000; border-right: 1px dashed #000; border-bottom: 1px solid #000; ';
// 			td1.style.borderSize  = '1px';
// 			td1.style.fontSize    = '8px';
// 			td1.width             = '20%';
// 			td1.align             = 'center';
// 			td1.innerHTML =  reg['CL_NOME']+'<br/>'+
<%-- 				             '<img src="../barcode?cdBarcode='+reg['ID_REDUZIDO']+'&showDig=false&height=15&barcodeType=<%=com.tivic.manager.util.BarcodeGenerator._Code128Bean%>" style="height:8mm;"/>'+ --%>
// 			                 '<br/>'+reg['ID_REDUZIDO']+
<%-- 				             '<br/><%=empresa.getNmPessoa()%>'+ --%>
// 				             '<br/>Prazo para troca: 15 dias ';
				             
// 			var td2 = document.createElement('td');
// 			td2.width           = '5%';
// 			td2.style.cssText  += 'border-top: 1px solid #000; border-right: 1px solid #000; border-bottom: 1px solid #000; border-left: 0px';
// 			td2.innerHTML       = '<div style="height: 20px; width: 60px; font-size: 14px; font-weight: bold; -webkit-transform: rotate(-90deg); -moz-transform: rotate(-90deg);">'+
// 			                      'R$ '+formatCurrency(reg['VL_PRECO'])+'</div>';   
// 			//
// 			if(qtTd>1)
// 				tr.appendChild(tdSpace);
// 			tr.appendChild(td0);
// 			tr.appendChild(td1);
// 			tr.appendChild(td2);
// 			if(qtTd==3)	{
// 				qtTd = 0;
// 				table.appendChild(tr);
// 				tr        = document.createElement('tr');
// 				tr.height = '2.2mm';
// 				var tdSpace = document.createElement('td');
// 				tdSpace.innerHTML = '<div style="height:2.2mm;"/>';
// 				tr.appendChild(tdSpace);
// 				table.appendChild(tr);
// 				//
// 				tr = document.createElement('tr');
// 			}
// 		}
// 		if(qtTd>0)
// 			table.appendChild(tr);
// 		parent.ReportOne.create('jReportEtiquetas', {width: 702, height: 478, modal: true, caption: 'Etiquetas',
// 			                                         resultset: {lines:[{}]},
// 													 detailBand: {contentModel: $('tableEtiqueta')},
// 											 		 orientation: 'portraid', paperType: 'A4', tableLayout: 'fixed',
// 													 displayReferenceColumns: false});
	}
}

function btnUpdateCustosAndPrecosOnClick(content) {
	if (content == null) {
		var cdEmpresa = $("cdEmpresa").value;
		var cdProdutoServico = $("cdProdutoServico").value;
		createTempbox('jInfo', {message: 'Atualizando Dados. Aguarde...', width: 160, height: 100});
		getPage("GET", "btnUpdateCustosAndPrecosOnClick", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
										   '&method=updateCustosAndPrecosMedios(const ' + cdEmpresa + ':int, const ' + cdProdutoServico + ':int)',
										   null, null, null, null);
	}
	else {
		var hash = null;
		try { hash = eval("(" + content + ")"); } catch(e) {}
		closeWindow('jInfo');
		if (hash==null)
			showMsgbox('Manager', 300, 50, 'Erros reportados ao atualizar custos e preços.');
		else {
			$('vlUltimoCusto').value = hash!=null ? formatCurrency(parseFloat(hash['vlUltimoCusto'], 10)) : $('vlUltimoCusto').value;
			$('vlCustoMedio').value  = hash!=null ? formatCurrency(parseFloat(hash['vlCustoMedio'], 10)) : $('vlCustoMedio').value;
			$('vlPrecoMedio').value  = hash!=null ? formatCurrency(parseFloat(hash['vlPrecoMedio'], 10)) : $('vlPrecoMedio').value;
		}
	}
}

function loadProduto(content, cdProdutoServico){
	if (content == null) {
		clearFormProdutoServico();
		var cdEmpresa = $("cdEmpresa").value;
		getPage("GET", "loadProduto", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices'+
									  '&method=getAsResultSet(const ' + cdProdutoServico + ':int, const ' + cdEmpresa + ':int)',
									  null, null, null, null);
	}
	else {
		var rsmProdutos = null;
		try { rsmProdutos = eval("(" + content + ")"); } catch(e) {}
		if (rsmProdutos!=null && rsmProdutos.lines && rsmProdutos.lines.length > 0)
			btnFindProdutoServicoOnClick(rsmProdutos.lines);
	}
}

function changeLayoutGrupos(option) {
	if (option > 2) {
		$('divGridGruposSuperior').style.display = '';
		$('divGruposSuperior').style.display     = 'none';
	} 
	else {
		$('divGridGruposSuperior').style.display = 'none';
		$('divGruposSuperior').style.display     = '';
	}
    //Muda layout se foi informado algum grupo no chamado do formulário, ou seja, é o cadastro do produto de um grupo já especificado
    if (cdGrupo > 0) {
		$('cdGrupo').value = cdGrupo;
		$('divGridSimilaresSuperior1').appendChild($('divGridSimilaresSuperior2'));
		$('divGridSimilares').style.height     = '105px';
		$('divGridReferenciados').style.height = '105px';

		$('divGridGruposSuperior').style.display = 'none';
		$('divGruposSuperior').style.display     = 'none';

		$('txtProdutoServico').style.height      = '63px';
	}
}

function changeLayout() {
	tabProdutoServico = TabOne.create('tabProdutoServico', {width: 896, height: 295, plotPlace: 'divTabProdutoServico', tabPosition: ['top', 'left'], style: 'margin-top:280px;',
															tabs: [{caption: 'Pesquisa', reference:'divAbaPesquisa', active: true},
															       {caption: 'Principal', reference:'divAbaDadosBasicos'},
															       {caption: 'Estoque', reference:'divAbaEstoque'},
															       {caption: 'Composição', reference:'divAbaComposicao'},
															       {caption: 'Substituto', reference:'divAbaSubstituto'},
															       {caption: 'Relacionado', reference:'divAbaRelacionado'},
															       {caption: 'Reabastecimento', reference:'divAbaReabastecimento'},
															       {caption: 'Preço de Custo e Venda', reference:'divAbaConfigFinanceira'},
															       {caption: 'Dados Complementares', reference:'divAbaDadosComplementares'},
// 															       {caption: 'Loja Virtual', reference:'divAbaLoja'},
															       {caption: 'Observação', reference:'divAbaEspecificacoes'}
																	<% if (lgFotoProduto == 1) { %> 
																		,{caption: 'Fotos', 
																	 	  reference: 'divAbaFotoProduto'}
																	<% } %>
																  ]});

	if (cdGrupo > 0) {
		$('txtEspecificacao').style.height = '92px';
		$('txtDadoTecnico').style.height   = '92px';
		$('txtPrazoEntrega').style.height  = '90px';
	}
}

function changeLayoutSubstituto() {
	tabSubstituto = TabOne.create('tabSubstituto', {width: 880, height: 220, plotPlace: 'divAbaSubstitutoAba', tabPosition: ['top', 'left'], style: 'margin-top:200px;',
															tabs: [{caption: 'Pesquisa', reference:'divAbaSubstitutoPesquisa', active: true},
															       {caption: 'Dados', reference:'divAbaSubstitutoDados'}]});
}

function changeLayoutRelacionado() {
	tabRelacionado = TabOne.create('tabRelacionado', {width: 880, height: 220, plotPlace: 'divAbaRelacionadoAba', tabPosition: ['top', 'left'], style: 'margin-top:200px;',
															tabs: [{caption: 'Pesquisa', reference:'divAbaRelacionadoPesquisa', active: true},
															       {caption: 'Dados', reference:'divAbaRelacionadoDados'}]});
}

function changeLayoutReabastecimento() {
	tabReabastecimento = TabOne.create('tabReabastecimento', {width: 880, height: 220, plotPlace: 'divAbaReabastecimentoAba', tabPosition: ['top', 'left'], style: 'margin-top:200px;',
															tabs: [{caption: 'Pesquisa', reference:'divAbaReabastecimentoPesquisa', active: true},
															       {caption: 'Dados', reference:'divAbaReabastecimentoDados'}]});
}

function changeLayoutDadosComplementares() {
	tabDadosComplementares = TabOne.create('tabDadosComplementares', {width: 880, height: 265, plotPlace: 'divAbaDadosComplementares', tabPosition: ['top', 'left'], style: 'margin-top:200px;',
															tabs: [
 															       {caption: 'Parâmetros', reference:'divAbaParametros', active: true},
 															       {caption: 'Fotos Adicionais', reference:'divAbaFotosAdicionais'},
 															       {caption: 'Características', reference:'divAbaCaracteristicas'},
 															       {caption: 'Fornecedores', reference:'divAbaFornecedores'},
															       {caption: 'Código de Barras', reference:'divAbaCodigoBarras'},
 															       {caption: 'Outros Códigos', reference:'divAbaOutrosCodigos'},
//  															       {caption: 'Grade', reference:'divAbaGrade'}
																  ]});
}

function formValidationProdutoServico(){
	var campos = [];
    campos.push([$("nmProdutoServico"), 'Indique o nome do produto', VAL_CAMPO_NAO_PREENCHIDO]);
	campos.push([$("cdUnidadeMedida"), 'Indique a unidade', VAL_CAMPO_MAIOR_QUE, 0]);
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'nmProdutoServico');
}

function loadAtributos(content, cdFormulario) {
	if(content==null){
		getPage("POST", "loadAtributos", "../methodcaller?className=com.tivic.manager.grl.FormularioServices"+
										 "&method=getAllAtributos(const " + cdFormulario + ":int)");
    }
    else{
		var rsmAtributos = null;
		try { rsmAtributos = eval("(" + content + ")") } catch(e) {}
		for (var i=0; rsmAtributos!=null && i<rsmAtributos.lines.length; i++) {
			var cdFormularioAtributo = rsmAtributos.lines[i]['CD_FORMULARIO_ATRIBUTO'];
			var tpDado = rsmAtributos.lines[i]['TP_DADO'];
			var nrCasasDecimais = parseInt(rsmAtributos.lines[i]['NR_CASAS_DECIMAIS'], 10);
			var nmAtributo = rsmAtributos.lines[i]['NM_ATRIBUTO'];
			atributos.push({'cdFormularioAtributo' : cdFormularioAtributo,
							'tpDado' : tpDado,
							'nrCasasDecimais' : nrCasasDecimais,
							'nmAtributo' : nmAtributo});
			
			/* CAPTION */
			var caption = document.createElement("label");
			caption.className = "caption2";
			caption.style.width = "100px";
			caption.style.float = "left";
			caption.style.padding = "2px 0 0 0";
			caption.appendChild(document.createTextNode(nmAtributo));
			
			/* CONTROLE */
			var control = document.createElement(tpDado == <%=FormularioAtributoServices.TP_MEMO%> ? "textarea" : tpDado == <%=FormularioAtributoServices.TP_OPCOES%> ? "select" : "input");
			control.className = tpDado == <%=FormularioAtributoServices.TP_MEMO%> ? "textarea" : "field";
			if (tpDado != <%=FormularioAtributoServices.TP_BOOLEAN%> && tpDado != <%=FormularioAtributoServices.TP_PESSOA%>)
				control.style.width = tpDado == <%=FormularioAtributoServices.TP_DATA%> ? "190px" : "207px";
			if (tpDado == <%=FormularioAtributoServices.TP_MEMO%>)
				control.style.heitht = "50px";
			if (tpDado == <%=FormularioAtributoServices.TP_BOOLEAN%>)
				control.setAttribute('value', '1');
			control.setAttribute('idform', 'produtoServico');
			control.setAttribute('logmessage', (tpDado == <%=FormularioAtributoServices.TP_PESSOA%> ? 'Código ' : '') + nmAtributo);
			control.setAttribute('reference', "atributo_" + cdFormularioAtributo);
			control.setAttribute('name', "atributo_" + cdFormularioAtributo);
			control.setAttribute('id', "atributo_" + cdFormularioAtributo);
			control.setAttribute('type', tpDado == <%=FormularioAtributoServices.TP_BOOLEAN%> ? "checkbox" : tpDado == <%=FormularioAtributoServices.TP_PESSOA%> ? "hidden" : "text");

			/* CONTROLE VIEW (PARA CAMPOS HIDDEN) */
			var controlView = null;
			var controlClearView = null;
			var controlSearchView = null;
			if (tpDado == <%=FormularioAtributoServices.TP_PESSOA%>) {
				/* VIEW */
				controlView = document.createElement("input");
				controlView.className = "disabledField";
				controlView.style.width = "173px";
				controlView.setAttribute('idform', 'produtoServico');
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
				controlClearView.setAttribute('idform', 'produtoServico');
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
														       FilterOne.create("jFiltro", {caption:"Pesquisar " + nmAtributo, width: 550, height: 255, top:65, modal: true, noDrag: true,
																				   className: "com.tivic.manager.grl.PessoaDAO", method: "find", allowFindAll: true,
																				   filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
																				   gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}],
																							      strippedLines: true, columnSeparator: false, lineSeparator: false},
																				   callback: controlSearchViewReturnOnClick, autoExecuteOnEnter: true
																		});}
				controlSearchView.setAttribute('idform', 'produtoServico');
				controlSearchView.setAttribute('title', "Pesquisar Pessoa para este campo...");
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
				controlCalendar.setAttribute('idform', 'produtoServico');
				controlCalendar.setAttribute('reference', 'atributo_' + cdFormularioAtributo);
				controlCalendar.setAttribute('title', "Selecionar data...");
				var imgCalendar = document.createElement("IMG");
				imgCalendar.setAttribute('src', '/sol/imagens/date-button.gif');
				controlCalendar.appendChild(imgCalendar);
			}
			
			/* DIV */
			var divControl = document.createElement("div");
			divControl.className = "element";
			divControl.style.width = tpDado == <%=FormularioAtributoServices.TP_DATA%> ? "309px" : "309px";
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
		initProdutoServicoTemp();
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
	for (var i=0; optionsAtributo!=null && i<optionsAtributo.lines.length; i++) {
		var optionControl = document.createElement("OPTION");
		optionControl.setAttribute('value', optionsAtributo.lines[i]['CD_OPCAO']);
		optionControl.appendChild(document.createTextNode(optionsAtributo.lines[i]['TXT_OPCAO']));
		$("atributo_" + cdFormularioAtributo).appendChild(optionControl);
	}
}

function btnAtualizarProdutoServicoOnClick() {
	if ($('cdProdutoServico').value>0)
		loadProduto(null, $('cdProdutoServico').value);
}

function loadTabelasPrecos(content) {
	if (content==null && $('cdEmpresa').value != 0) {
		getPage("GET", "loadTabelasPrecos", 
				"../methodcaller?className=com.tivic.manager.adm.TabelaPrecoServices"+
				"&method=getAllOfEmpresa(const " + <%=cdEmpresa%> + ":int)");
	}
	else {
		var rsmTabelasPreco = null;
		try {rsmTabelasPreco = eval('(' + content + ')')} catch(e) {}
		loadOptionsFromRsm($('cdTabelaPrecoOfRegras'), rsmTabelasPreco, {setDefaultValueFirst:true, fieldValue: 'cd_tabela_preco', fieldText:'nm_tabela_preco'});
		loadOptionsFromRsm($('cdTabelaPreco'), rsmTabelasPreco, {setDefaultValueFirst:true, fieldValue: 'cd_tabela_preco', fieldText:'nm_tabela_preco'});
	}
}

function getNextIdReduzido(content){
    var cdEmpresa = parent.$('cdEmpresa')!=null ? parent.$('cdEmpresa').value : 0;
    if (cdEmpresa > 0) {
        if (content==null) {
            getPage("GET", "getNextIdReduzido", 
                    "../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices&method=getNextIdReduzido(const " + cdEmpresa + ":int, const "+$('cdGrupo').value+":int)");
        }
        else {
        	var id = eval("("+content+")");
        	$('idReduzido').value = id!='' ? id : $('idReduzido').value;
        }
    }
}

function clearFormProdutoServico(){
    $("dataOldProdutoServico").value = "";
    $('rodapeText').innerHTML = 'Produto';
    disabledFormProdutoServico = false;
    alterFieldsStatus(true, produtoServicoFields, "nmProdutoServico");
    tabProdutoServico.showTab(0);
    clearFields(produtoServicoFields);
    $('dtUltimaAlteracao').value = '';
    $('dtCadastro').value = '';
	getNextIdReduzido();
// 	loadSimilares();
// 	loadReferenciados();
	loadEstoque();
	loadSubstituto();
 	loadRelacionado();
 	loadComponentes();
 	loadReabastecimento();
 	loadCodigoBarras();
 	loadOutrosCodigos();
	loadPrecos();
	loadFornecedores();
	loadTributos();
	loadFotosAdicionais();
	loadCaracteristicas();
if (cdGrupo <= 0)  // Se nao for o cadastro de um produto de grupo específico 
		loadGrupos();
	loadUltimoFornecedor();

    alterFieldsStatus(true, produtoServicoFields, "nmProdutoServico");
	$('stProdutoEmpresa').value = 1;
	$('stProdutoEmpresa').checked = true;
	$('imageProduto').src = 'preview_imagem.jsp?lgDefaultInBlank=1';
	clearCaracteristicas();
	clearCodigoBarras();
	clearOutrosCodigos();
	clearReabastecimento();
	tabDadosComplementares.showTab(0);
}
function btnNewProdutoServicoOnClick(){
    clearFormProdutoServico();
	btnConsultarEstoqueOnClick(null, true);
}

function btnAlterProdutoServicoOnClick(){
    disabledFormProdutoServico = false;
    alterFieldsStatus(true, produtoServicoFields, "nmProdutoServico");
}

function btnSaveProdutoServicoOnClick(content){
    if(content==null){
    	if (disabledFormProdutoServico)	{
            createMsgbox("jMsg", {width: 250, height: 50,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.", msgboxType: "INFO"});
        }
        else if (formValidationProdutoServico()) {
        	$('txtEspecificacao').value = ($('txtEspecificacao').value+'').toUpperCase();
			$('txtDadoTecnico').value   = ($('txtDadoTecnico').value+'').toUpperCase();
			
        	var txtEspecificacao = $('txtEspecificacao').value;
			var txtDadoTecnico   = $('txtDadoTecnico').value;
			var txtPrazoEntrega  = $('txtPrazoEntrega').value;
            var executionDescription       = $("cdProdutoServico").value>0 ? formatDescriptionUpdate("ProdutoServico", $("cdProdutoServico").value, $("dataOldProdutoServico").value, produtoServicoFields) : formatDescriptionInsert("ProdutoServico", produtoServicoFields);
            var constructionProdutoEmpresa = 'const'+<%=cdEmpresa%>+': int, cdProdutoServico: int, cdUnidadeMedida: int, idReduzido: String, vlPrecoMedio: float, vlCustoMedio: float, vlUltimoCusto: float,const '+changeLocale('qtIdeal')+': float, qtMinima: float, qtMaxima: float, qtDiasEstoque: float, qtPrecisaoCusto: int, qtPrecisaoUnidade: int, qtDiasGarantia: int, tpAbastecimento: int, tpControleEstoque:int, tpTransporte:int, stProdutoEmpresa:int, dtDesativacao:GregorianCalendar, nrOrdem:String, lgEstoqueNegativo:int, tpOrigem:int, idFabrica:String, const ' + $('dtUltimaAlteracao').value + ':GregorianCalendar, const ' + $('dtCadastro').value + ':GregorianCalendar, nrSerie:String, *imgProduto: byte[]';
			var constructionProdutoServico = 'cdProdutoServico: int, cdCategoria: int, nmProdutoServico: String, txtProdutoServico: String, const ' + txtEspecificacao + ': String, const ' + txtDadoTecnico + ': String, const ' + txtPrazoEntrega + ': String, tpProdutoServico: int, idProdutoServico: String, sgProdutoServico: String, cdClassificacaoFiscal:int, cdFabricante: int, cdMarca: int, nmModelo: String, cdNcm: int, nrReferencia: String';
			var constructionProduto        = constructionProdutoServico + ', vlPesoUnitario:float, vlPesoUnitarioEmbalagem:float, vlComprimento:float, vlLargura:float, vlAltura:float, vlComprimentoEmbalagem:float, vlLarguraEmbalagem:float, vlAlturaEmbalagem:float, qtEmbalagem:int';
			var objectsAtributos           = 'atributos=java.util.ArrayList();';
			var commandsExecute            = '';
			
			objectsAtributos += 'imgProduto=byte[];';
			commandsExecute  += 'imgProduto=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const imgProduto_' + $('cdProdutoServico').value + ':String);';
			
			// ATRIBUTOS DINAMICO
			for (var i = 0; i < atributos.length; i++) {
				var atributo             = atributos[i];
				var cdFormularioAtributo = atributo['cdFormularioAtributo'];
				var tpDado               = atributo['tpDado'];
				var cdEmpresa            = 0;
				var cdOpcao              = tpDado==<%=FormularioAtributoServices.TP_OPCOES%> ? $('atributo_' + cdFormularioAtributo).value : 0;
				var cdDocumento          = 0;
				var cdPessoa             = tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? $('atributo_' + cdFormularioAtributo).value : 0;
				objectsAtributos += ';atributo' + i + '=com.tivic.manager.grl.FormularioAtributoValor(const ' + cdFormularioAtributo + ':int, const 0:int, cdProdutoServico:int, const ' + cdOpcao + ':int, const ' + cdEmpresa + ':int, const ' + cdDocumento + ':int, ' + (tpDado==<%=FormularioAtributoServices.TP_OPCOES%> || tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? 'const ' : ('atributo_' + cdFormularioAtributo)) + ':String, const 0:int, const ' + cdPessoa + ':int, const 0:int)';
				commandsExecute += 'atributos.add(*atributo' + i + ':java.lang.Object);';
			}
			
			//Parametros
			objectsAtributos += ';parametros=java.util.HashMap();';
			objectsAtributos += 'lgVerificarEstoqueNaVenda=java.lang.Integer(const ' +($('lgVerificarEstoqueNaVenda').checked ? "1" : "0") + ':int);';
			commandsExecute  += 'parametros.put(const lgVerificarEstoqueNaVenda:Object, *lgVerificarEstoqueNaVenda:Object);';	
			objectsAtributos += 'lgBloqueiaVenda=java.lang.Integer(const ' +($('lgBloqueiaVenda').checked ? "1" : "0") + ':int);';
			commandsExecute  += 'parametros.put(const lgBloqueiaVenda:Object, *lgBloqueiaVenda:Object);';	
			objectsAtributos += 'lgPermiteDesconto=java.lang.Integer(const ' +($('lgPermiteDesconto').checked ? "1" : "0") + ':int);';
			commandsExecute  += 'parametros.put(const lgPermiteDesconto:Object, *lgPermiteDesconto:Object);';	
			objectsAtributos += 'lgFazEntrega=java.lang.Integer(const ' +($('lgFazEntrega').checked ? "1" : "0") + ':int);';
			commandsExecute  += 'parametros.put(const lgFazEntrega:Object, *lgFazEntrega:Object);';	
			objectsAtributos += 'lgNaoControlaEstoque=java.lang.Integer(const ' +($('lgNaoControlaEstoque').checked ? "1" : "0") + ':int);';
			commandsExecute  += 'parametros.put(const lgNaoControlaEstoque:Object, *lgNaoControlaEstoque:Object);';	
			objectsAtributos += 'lgImprimeNaTabelaPreco=java.lang.Integer(const ' +($('lgImprimeNaTabelaPreco').checked ? "1" : "0") + ':int);';
			commandsExecute  += 'parametros.put(const lgImprimeNaTabelaPreco:Object, *lgImprimeNaTabelaPreco:Object);';	
			objectsAtributos += 'lgProdutoUsoConsumo=java.lang.Integer(const ' +($('lgProdutoUsoConsumo').checked ? "1" : "0") + ':int);';
			commandsExecute  += 'parametros.put(const lgProdutoUsoConsumo:Object, *lgProdutoUsoConsumo:Object);';	
			
			
			var className = "com.tivic.manager.grl.ProdutoServicoEmpresaServices";
			var method    = "new com.tivic.manager.grl.Produto(" + constructionProduto + "):com.tivic.manager.grl.ProdutoServico, " +
					    	"new com.tivic.manager.grl.ProdutoServicoEmpresa(" + constructionProdutoEmpresa + "):com.tivic.manager.grl.ProdutoServicoEmpresa, " +
					    	"const " +$("cdGrupo").value + ":int, const 0:int, *atributos:java.util.ArrayList,*parametros:java.util.HashMap";
				 
			if($("cdProdutoServico").value > 0)	{
				getPage("POST", "btnSaveProdutoServicoOnClick", "../methodcaller?className=" + className +
					"&execute=" + commandsExecute +
					"&objects=" + objectsAtributos +
                    "&method= updateDna(" + method + ", *imgProduto: byte[])", produtoServicoFields, null, null, executionDescription);
			}														  
            else	{
                getPage("POST", "btnSaveProdutoServicoOnClick", "../methodcaller?className="+className +
					"&execute=" + commandsExecute +
					"&objects=" + objectsAtributos +
                    "&method=insertDna(" + method + ")", produtoServicoFields, null, null, executionDescription);
        	}
		}
    }
    else{
    	var result = null;
    	try {result = eval("(" + content + ")"); } catch(e) {}
		$("cdProdutoServico").value = $("cdProdutoServico").value<=0 && result.code ? result.code : $("cdProdutoServico").value;
		if(result.code > 0){
            disabledFormProdutoServico=true;
            alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField2");
            createTempbox("jMsg", {width: 200, height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
            if(result.objects['dtCadastro'])
            	$("dtCadastro").value = result.objects['dtCadastro'];
            else
            	$("dtCadastro").value = '';
            if(result.objects['dtUltimaAlteracao'])
            	$("dtUltimaAlteracao").value = result.objects['dtUltimaAlteracao'];
            else
            	$("dtUltimaAlteracao").value = '';
            loadPesquisa();
        }
        else{
			var msg = "ERRO ao tentar gravar dados!";
			if (parseInt(result.code, 10) == <%=ProdutoServicoEmpresaServices.ERR_ID_REDUZIDO_EM_USO%>)
				msg += ". ID informado já se encontra em uso.";
			if (parseInt(result.code, 10) == <%=ProdutoServicoEmpresaServices.ERR_ID_CODIGO_BARRAS_EM_USO%>)
				msg += ". Código de Barras já existe!.";
            createTempbox("jMsg", {width: 200, height: 50, message: msg, tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnFindProdutoServicoOnClick(reg) {
    if(!reg){
    	var hiddenFields = [{reference:"A.TP_PRODUTO_SERVICO", value:0, comparator:_EQUAL, datatype:_INTEGER},
							{reference:"cd_empresa", value:$("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER},
							{reference:"cd_local_armazenamento", value:"0", comparator:_EQUAL, datatype:_INTEGER},
							{reference:"qtLimite", value:50, comparator:_EQUAL, datatype:_INTEGER}];
		
        var filterFieldsProdutoServico = [[{label:"Nome do produto", reference:"nm_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50, charcase:'uppercase'},
                                           {label:"Código de Barras", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:20, charcase:'uppercase'},
										   {label:"ID Reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:15, charcase:'uppercase'},
										   {label:"Referência", reference:"nr_referencia", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:15, charcase:'uppercase'}],
										  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:(cdGrupo > 0 ? 100 : 60), charcase:'uppercase'}]];
        var columnsFields = [{label:"Referência", reference:"NR_REFERENCIA"},
							 {label:"Nome do produto/serviço", reference:"CL_NOME"},
							 <%=cdTipoOperacaoVarejo>0 ?"{label:\"Preço Varejo\", reference:\"vl_preco_"+cdTipoOperacaoVarejo+"\", type: GridOne._CURRENCY},":""%>
							 <%=cdTipoOperacaoAtacado>0 && cdTipoOperacaoAtacado!=cdTipoOperacaoVarejo?
									                    "{label:\"Preço Atacado\", reference:\"vl_preco_"+cdTipoOperacaoAtacado+"\", type: GridOne._CURRENCY},":""%>		                   	 
							 {label:"Estoque", reference:"QT_ESTOQUE", style: 'text-align: right;'},
							 {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
							 {label:"ID/Cód. reduzido", reference:"id_reduzido"},
							 {label:"Fabricante", reference:"nm_fabricante"},
							 {label:"Grupo do produto", reference:"NM_GRUPO"}];
			
		if (cdGrupo > 0) {
			 hiddenFields.push({reference:"cd_grupo", value:cdGrupo, comparator:_EQUAL, datatype:_INTEGER});
		}
		else {
			// Incluído para apresentação de lançamento de grupo sem usar o grid
			hiddenFields.push({reference:"findGrupoPrincipal", value: 0, comparator:_EQUAL, datatype:_INTEGER});
			filterFieldsProdutoServico[1].push({label:"Grupo principal", reference:"nm_grupo", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:40, charcase:'uppercase'});
		}
		var className = "com.tivic.manager.alm.ProdutoEstoqueServices";
		
		FilterOne.create("jFiltro", {caption:"Pesquisar produtos", width: 640,height: 400, modal: true, noDrag: true, noTitle: true,
												   	className: className, method: "findCompleto",
												   	filterFields: filterFieldsProdutoServico,
												   	gridOptions: {columns: columnsFields, strippedLines: true, columnSeparator: false, lineSeparator: false,
												   		          onProcessRegister: function(reg)	{
																		reg['CL_NOME'] = reg['NM_PRODUTO_SERVICO'];
																		// Cor
																		if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
																			reg['CL_NOME'] += ' - '+reg['TXT_ESPECIFICACAO'].toUpperCase();
																		// Tamanho
																		if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
																			reg['CL_NOME'] += ' - '+reg['TXT_DADO_TECNICO'].toUpperCase();
												   		        	  	  
													   			  }},
												   	hiddenFields: hiddenFields,
												   	callback: btnFindProdutoServicoOnClick,
												   	autoExecuteOnEnter: true});
    }
    else {// retorno
//     	closeWindow("jFiltro");
//         disabledFormProdutoServico=true;
//         alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField2");
//         loadFormRegister(produtoServicoFields, reg[0]);
//         $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
// 		if ($('nmClassificacaoFiscalView') != null) {
// 			if (reg[0]['CD_CLASSIFICACAO_FISCAL']==null || reg[0]['CD_CLASSIFICACAO_FISCAL']==0)
// 				$('nmClassificacaoFiscalView').value = '';
// 			else 
// 				$('nmClassificacaoFiscalView').value = (trim(reg[0]['ID_CLASSIFICACAO_FISCAL'])!='' ? reg[0]['ID_CLASSIFICACAO_FISCAL'] + ' - ' : '') + reg[0]['NM_CLASSIFICACAO_FISCAL'];
// 		}
// 		$("cdEmpresa").value = $("cdEmpresa").getAttribute("defaultValue");
// 		/* CARREGUE OS GRIDS AQUI */
// 		setTimeout('loadAtributosProdutoServico()', 1);
// // 		setTimeout('loadSimilares(); loadReferenciados(); loadPrecos(); loadFornecedores();', 1);
// 		setTimeout('loadEstoque(); loadSubstituto(); loadReabastecimento(); loadRelacionado(); loadPrecos(); loadFornecedores(); loadCodigoBarras();', 1);
// 		setTimeout('loadTributos()', 1);
// // 		setTimeout('loadGrupos()', 1);
// 		setTimeout('loadNcm()', 1);
// 		setTimeout('loadGrupos()', 1);
// 		setTimeout('loadUltimoFornecedor()', 1);
// 		setTimeout('loadFotosAdicionais()', 1);
// 		setTimeout('loadCaracteristicas()', 1);
// 		setTimeout('loadOutrosCodigos()', 1);
// 		setTimeout('initComponente()', 1);
<%-- 		<% if (lgFotoProduto == 1) { %> --%>
// 			setTimeout('loadFotoProduto()', 10);
<%-- 		<% } %> --%>
// 		if(reg[0]['DT_CADASTRO'])
// 			$("dtCadastro").value = reg[0]['DT_CADASTRO'];
//         else
//         	$("dtCadastro").value = '';
// 		if(reg[0]['DT_ULTIMA_ALTERACAO'])
//         	$("dtUltimaAlteracao").value = reg[0]['DT_ULTIMA_ALTERACAO'];
//         else
//         	$("dtUltimaAlteracao").value = '';
		
//         $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
// 		if (!$("idProdutoServico").disabled)
// 	        $("idProdutoServico").focus();
<%-- 		$('imageProduto').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.ProdutoServicoEmpresaServices&method=getBytes(const '+<%=cdEmpresa%>+':int, const ' + reg[0]['CD_PRODUTO_SERVICO'] + ':int)&idSession=imgProduto_' + reg[0]['CD_PRODUTO_SERVICO']; --%>
// 		tabProdutoServico.showTab(1);
// 		tabDadosComplementares.showTab(0);
		closeWindow("jFiltro");
		disabledFormProdutoServico=true;
		alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField2");
		var reg = reg[0];
		$("dtCadastro").value = reg['DT_CADASTRO'];
		$('dtUltimaAlteracao').value = reg['DT_ULTIMA_ALTERACAO'];
		loadFormRegister(produtoServicoFields, reg);
		$("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
		if ($('nmClassificacaoFiscalView') != null) {
			if (reg[0]['CD_CLASSIFICACAO_FISCAL']==null || reg['CD_CLASSIFICACAO_FISCAL']==0)
				$('nmClassificacaoFiscalView').value = '';
			else 
				$('nmClassificacaoFiscalView').value = (trim(reg['ID_CLASSIFICACAO_FISCAL'])!='' ? reg['ID_CLASSIFICACAO_FISCAL'] + ' - ' : '') + reg['NM_CLASSIFICACAO_FISCAL'];
		}
		$("cdEmpresa").value = $("cdEmpresa").getAttribute("defaultValue");
		$('rodapeText').innerHTML = reg['ID_REDUZIDO'] + " - " + reg['NM_PRODUTO_SERVICO'];
		/* CARREGUE OS GRIDS AQUI */
		setTimeout('loadAtributosProdutoServico()', 1);
		//	setTimeout('loadSimilares(); loadReferenciados(); loadPrecos(); loadFornecedores();', 1);
		setTimeout('loadEstoque(); loadSubstituto(); loadReabastecimento(); loadRelacionado(); loadPrecos(); loadFornecedores();', 1);
		clearReabastecimento();
		setTimeout('loadTributos()', 1);
		setTimeout('loadGrupos()', 1);
		setTimeout('loadNcm()', 1);
		//	setTimeout('loadSubstituto()', 1);
		setTimeout('loadCodigoBarras()', 1);
		setTimeout('loadOutrosCodigos()', 1);
		//	setTimeout('loadRelacionado()', 1);
		setTimeout('loadUltimoFornecedor()', 1);
		setTimeout('loadFotosAdicionais()', 1);
		setTimeout('loadCaracteristicas()', 1);
		setTimeout('initComponente()', 1);
		loadParametros(reg);
		<% if (lgFotoProduto == 1) { %>
			setTimeout('loadFotoProduto()', 10);
		<% } %>
		
		$("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
		if (!$("idProdutoServico").disabled)
		    $("idProdutoServico").focus();
		
		$('imageProduto').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.ProdutoServicoEmpresaServices&method=getBytes(const '+<%=cdEmpresa%>+':int, const ' + reg['CD_PRODUTO_SERVICO'] + ':int)&idSession=imgProduto_' + reg['CD_PRODUTO_SERVICO'];
		tabProdutoServico.showTab(1);
		tabDadosComplementares.showTab(0);
    }
}

function btnDeleteProdutoServicoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("ProdutoServico", $("cdProdutoServico").value, $("dataOldProdutoServico").value);
    var className = "com.tivic.manager.grl.ProdutoServicoEmpresaServices";
	var method = "delete(const " + $("cdEmpresa").value + ":int, const " + $("cdProdutoServico").value + ":int):int";
	getPage("GET", "btnDeleteProdutoServicoOnClick", 
            "../methodcaller?className=" + className +
            "&method=" + method, null, null, null, executionDescription);
}
function btnDeleteProdutoServicoOnClick(content){
    if(content==null){
        if ($("cdProdutoServico").value == 0)
            createMsgbox("jMsg", {width: 300, caption: 'Manager', height: 60, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.", msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteProdutoServicoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content, 10)==1){
            createTempbox("jTemp", {width: 200, height: 75, message: "Produto excluído com sucesso!", time: 3000});
            clearFormProdutoServico();
        }
        else
            createTempbox("jTemp", {width: 300, height: 75, message: "Não foi possível excluir este produto!", time: 5000});
    }	
}

function btnFindFabricanteOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar fabricantes", width: 450, height: 325, modal: true, noDrag: true,
								     className: "com.tivic.manager.grl.PessoaDAO", method: "find", allowFindAll: true,
									 filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
									 gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}], strippedLines: true, columnSeparator: false, lineSeparator: false},
								     hiddenFields: [{reference:"gn_pessoa", value:0, comparator:_EQUAL, datatype:_INTEGER}],
									 callback: btnFindFabricanteOnClick, autoExecuteOnEnter: true});
    }
    else {// retorno
		closeWindow('jFiltro');
		$('cdFabricante').value     = reg[0]['CD_PESSOA'];
		$('cdFabricanteView').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearFabricanteOnClick(){
	$('cdFabricante').value = 0;
	$('cdFabricanteView').value = '';
}

function loadAtributosProdutoServico(content){
	if (content==null) {
		getPage("GET", "loadAtributosProdutoServico", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices" + 
				"&method=getAtributos(const " + $('cdProdutoServico').value + ":int, const " + $('cdEmpresa').value + ":int, const " + cdGrupo + ":int)");
	}
	else {
		var atributos = null;
		try {atributos = eval("(" + content + ")"); } catch(e) {}
		loadFormRegister(produtoServicoFields, atributos, false);
		$("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
	}
}

function onChangeStProdutoEmpresa(content, isChecked) {
	if (content==null) {
		if (isChecked)
			$('dtDesativacao').value = '';
		else
			getPage("GET", "onChangeStProdutoEmpresa", "../methodcaller?className=com.tivic.manager.util.Util&method=getDataAtual()");
	}
	else
		$('dtDesativacao').value = content.length >= 12 ? content.substring(1, 11) : '';
}
/*********************************************************************************************************************************
 ******************************************* REFERENCIADOS ***********************************************************************
 *********************************************************************************************************************************/
var gridReferenciados;
function loadReferenciados(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		getPage("GET", "loadReferenciados", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
				"&method=getAllSimilares(const " + $('cdProdutoServico').value + ":int, const 1: int)");
	}
	else {
		var rsmReferenciados = null;
		try {rsmReferenciados = eval('(' + content + ')')} catch(e) {}
		gridReferenciados = GridOne.create('gridReferenciados', {columns: [{label: 'Nome produto', reference: 'NM_PRODUTO_SERVICO'},
		         		                                                   {label: 'Tam', reference: 'TXT_DADO_TECNICO'},
		        		                                                   {label: 'Cor', reference: 'TXT_ESPECIFICACAO'},
		        		                                                   {label: 'Cód. Barras', reference: 'ID_PRODUTO_SERVICO'}],
					                                             resultset: rsmReferenciados, plotPlace: $('divGridReferenciados')});
	}
}

var produtoServicoReferenciadoTemp = null;
function btnNewProdutoReferenciadoOnClick(reg){
    if(!reg){
		if ($('cdProdutoServico').value == 0)
            createMsgbox("jMsg", {width: 300,
                                  height: 100,
                                  message: "Inclua ou localize um produto para especificar produtos referenciados.",
                                  msgboxType: "INFO"})
		else {
			var hiddenFieldsProdutoServico = [{reference:"A.tp_produto_servico", value:0, comparator:_EQUAL, datatype:_INTEGER},
											  {reference:"B.cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER},
											  {reference:"A.cd_produto_servico", value:$("cdProdutoServico").value, comparator:_DIFFERENT, datatype:_INTEGER}];
			
			if (cdGrupo > 0)
				hiddenFieldsProdutoServico.push({reference:"cd_grupo", value:cdGrupo, comparator:_EQUAL, datatype:_INTEGER});
			var filterFieldsProdutoServico = [[{label:"Nome", reference:"nm_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
											  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width: 60, charcase:'uppercase'},
											   {label:"ID", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:'uppercase'}]
											  ];
			filterFieldsProdutoServico[1].push({label:"ID reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'});
				
			filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar produtos", 
													    width: 550,
													    height: 300,
													    top: 50,
													    modal: true,
													    noDrag: true,
													    className: "com.tivic.manager.alm.ProdutoEstoqueServices",
													    method: "findCompleto",
													    allowFindAll: false,
													    filterFields: filterFieldsProdutoServico,
													    gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
														 					    {label:"ID", reference:"ID_PRODUTO_SERVICO"},
																			    {label:"ID reduzido", reference:"ID_REDUZIDO"},
																			    {label:"Fabricante", reference:"NM_FABRICANTE"}],
														strippedLines: true,
														columnSeparator: false,
														lineSeparator: false},
													    hiddenFields: hiddenFieldsProdutoServico,
													    callback: btnNewProdutoReferenciadoOnClick, 
														autoExecuteOnEnter: true});



		}
    }
    else {// retorno
        filterWindow.close();
		produtoServicoReferenciadoTemp = reg[0];
		setTimeout('btnNewProdutoReferenciadoAuxOnClick()', 1);
    }
}

function btnNewProdutoReferenciadoAuxOnClick(content){
    if(content==null){
		var cdReferenciado = produtoServicoReferenciadoTemp["CD_PRODUTO_SERVICO"];
		var nmReferenciado = produtoServicoReferenciadoTemp['NM_PRODUTO_SERVICO'];
		var cdProdutoServico = $('cdProdutoServico').value;
		var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		var executionDescription = "Inclusão de produto referenciado " + nmReferenciado + " (Cód. " + cdReferenciado + ") " + produtoServicoDescription;		
		getPage("GET", "btnNewProdutoReferenciadoAuxOnClick", "../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO" +
														      "&method=insert(new com.tivic.manager.grl.ProdutoSimilar(cdProdutoServico: int, cdReferenciado: int, const 1: int):com.tivic.manager.grl.ProdutoSimilar)" +
												  		 	  "&cdProdutoServico=" + cdProdutoServico +
												  		 	  "&cdReferenciado=" + cdReferenciado, null, null, null, executionDescription);
    }
    else{
        var ok = parseInt(content, 10) > 0;
		if (ok)
			gridReferenciados.addLine(0, produtoServicoReferenciadoTemp, null, true)	
		else
            createTempbox("jMsg", {width: 200, 
								   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
    }
}

function btnDeleteProdutoReferenciadoOnClick(content)	{
	if(content==null) {
		if (gridReferenciados.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o produto referenciado que deseja excluir.');
		else {
			var cdReferenciado = (gridReferenciados.getSelectedRow().register['CD_SIMILAR'] != null) ? gridReferenciados.getSelectedRow().register['CD_SIMILAR'] : gridReferenciados.getSelectedRow().register['CD_PRODUTO_SERVICO'];
			var nmReferenciado = gridReferenciados.getSelectedRow().register['NM_PRODUTO_SERVICO'];
			var cdProdutoServico = $('cdProdutoServico').value;
			var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		    var executionDescription = "Remoção do Referenciado " + nmReferenciado + " (Cód. " + cdReferenciado + ") da relação de similares do produto " + produtoServicoDescription;	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja excluir o produto referenciado selecionado?', 
							function() {
								getPage('GET', 'btnDeleteProdutoReferenciadoOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO'+
										'&method=delete(const ' + cdProdutoServico + ':int, const ' + cdReferenciado + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridReferenciados.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'ERRO ao tentar excluir produto referenciado.');
	}
}

/*********************************************************************************************************************************
 ******************************************* GRUPOS DE PRODUTOS  *****************************************************************
 *********************************************************************************************************************************/
var checkboxTemp = null;
function onClickLgPrincipal(content) {
	if(content==null){
		checkboxTemp = this;
		var cdGrupoPrincipal = this.parentNode.parentNode.register["CD_GRUPO"];
		var nmGrupoPrincipal = this.parentNode.parentNode.register["NM_GRUPO"];
		var cdProdutoServico = $('cdProdutoServico').value;
		var cdEmpresa = $('cdEmpresa').value;
		var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		if (this.checked) {
			var executionDescription = "Configuração do grupo " + nmGrupoPrincipal + " (Cód. " + cdGrupoPrincipal + ") como grupo principal do produto " + produtoServicoDescription;		
			getPage("GET", "onClickLgPrincipal", "../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
													  "&method=setGrupoPrincipal(cdProdutoServico: int, cdEmpresa: int, cdGrupo: int)" +
													  "&cdProdutoServico=" + cdProdutoServico +
													  "&cdEmpresa=" + cdEmpresa +
													  "&cdGrupo=" + cdGrupoPrincipal, null, null, null, executionDescription);
		}
		else {
			var executionDescription = "Retirando status de Grupo Principal do Grupo " + nmGrupoPrincipal + " (Cód. " + cdGrupoPrincipal + ") em relação ao produto " + produtoServicoDescription;		
			getPage("GET", "onClickLgPrincipal", "../methodcaller?className=com.tivic.manager.alm.ProdutoGrupoDAO"+
													  "&method=update(new com.tivic.manager.alm.ProdutoGrupo(cdProdutoServico: int, cdGrupo: int, cdEmpresa: int, lgPrincipal: int)" +
													  "&cdProdutoServico=" + cdProdutoServico + 
													  "&cdEmpresa=" + cdEmpresa +
													  "&lgPrincipal=0" + 
													  "&cdGrupo=" + cdGrupoPrincipal, null, null, null, executionDescription);
		}
    }
    else{
        var ok = parseInt(content, 10) > 0;
		if (ok) {
			if (checkboxTemp.checked) {
				var checkBoxes = gridGrupos.getElementsColumn(2);
				for (var i=0; checkBoxes!=null && i<checkBoxes.length; i++) {
					if (checkBoxes[i].id != checkboxTemp.id)
						checkBoxes[i].checked = false;
				}
			}
            createTempbox("jMsg", {width: 220, 
								   height: 50,
                                   message: "Dados atualizados com sucesso.",
                                   tempboxType: "MESSAGE", 
								   time: 3000});
		}
		else
            createTempbox("jMsg", {width: 200, 
								   height: 50,
                                   message: "ERRO ao atualizar dados!",
                                   tempboxType: "ERROR", 
								   time: 3000});
    }
}

// function loadGrupos(content) {
// 	if (content==null && $('cdProdutoServico').value != 0) {
// 		$('cdGrupo').value     = ''; 
// 		$('cdGrupoView').value = '';
// 		//
// 		$('cdGrupo2').value     = ''; 
// 		$('cdGrupoView2').value = '';
// 		//
<%-- 		var cdEmpresa = <%=cdEmpresa%>; --%>
// 		getPage("GET", "loadGrupos", 
// 				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices"+
// 				"&method=getAllGrupos(const " + $('cdProdutoServico').value + ":int, const " + cdEmpresa + ":int)");
// 	}
// 	else {
// 		var rsmGrupos = null;
// 		try {rsmGrupos = eval('(' + content + ')')} catch(e) {}
// 		gridGrupos = GridOne.create('gridGrupos', {columns: columnsGrupo, resultset :rsmGrupos, plotPlace : $('divGridGrupos')});
// 		// Atribuindo valores
// 		for(var i=0; rsmGrupos != null && i<rsmGrupos.lines.length; i++) 
// 			if(gridGrupos.size()==1 || gridGrupos.lines[i]['LG_PRINCIPAL']==1)	{
// 				$('cdGrupo').value     = gridGrupos.lines[i]['CD_GRUPO'];
// 				$('cdGrupoView').value = gridGrupos.lines[i]['NM_GRUPO'];
// 			}
// 			else if(gridGrupos.size()==2 && gridGrupos.lines[i]['LG_PRINCIPAL']!=1)	{
// 				$('cdGrupo2').value     = gridGrupos.lines[i]['CD_GRUPO'];
// 				$('cdGrupoView2').value = gridGrupos.lines[i]['NM_GRUPO'];
// 			}
// 	}
// 	changeLayoutGrupos(gridGrupos != null ? gridGrupos.size() : 0);
// }

function btnFindGrupoOnClick(reg, funcCallback, cdGrupoSuperior) {
    if(!reg) {
    	if(funcCallback==null)
    		funcCallback = btnFindGrupoOnClick;	
		var hiddenFields = [];
		if(cdGrupoSuperior > 0)
			hiddenFields.push({reference:"A.cd_grupo_superior", value:cdGrupoSuperior, comparator:_EQUAL, datatype:_INTEGER});
		//
		FilterOne.create("jFiltro", {caption:"Pesquisar grupos de produtos", width: 550, height: 375, modal: true, noDrag: true,
									 className: "com.tivic.manager.alm.GrupoServices", method: "find", allowFindAll: true, autoExecuteOnEnter: true,
									 hiddenFields: hiddenFields,
								     filterFields: [[{label:"Nome", reference:"A.NM_GRUPO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
								     gridOptions: {columns: [{label:"Nome", reference:"DS_GRUPO"}], strippedLines: true, columnSeparator: false, lineSeparator: false},
								     callback: funcCallback});
    }
    else {
        closeWindow("jFiltro");
		$('cdGrupo').value     = reg[0]['CD_GRUPO'];
		$('cdGrupoView').value = reg[0]['NM_GRUPO'];
		$('idGrupo').value 	   = reg[0]['ID_GRUPO'];
        if(isIdByGrupo) 
        	getNextIdReduzido(null);	
        loadGrupos();
        
    }
}

function btnClearGrupoOnClick(){
	$('cdGrupo').value     = 0;
	$('cdGrupoView').value = '';
	$('idGrupo').value 	   = '';
	loadGrupos();
}

function btnFindGrupo2Return(reg) {
    closeWindow("jFiltro");
    if($('cdGrupo').value <= 0 && <%=lgGrupoHierarquico==1%> && reg[0]['CD_GRUPO_SUPERIOR']>0) {
    	$('cdGrupo').value     = reg[0]['CD_GRUPO_SUPERIOR'];
    	$('cdGrupoView').value = reg[0]['NM_GRUPO_SUPERIOR'];
    	$('idGrupo').value     = reg[0]['ID_GRUPO_SUPERIOR'];
    	//
        if(isIdByGrupo) 
        	getNextIdReduzido(null);	
    }
}

function btnFindCdGrupoOnClick(codigo) {
    getPage("GET", "btnFindCdGrupoOnClickAux", 
		"../methodcaller?className=com.tivic.manager.alm.GrupoServices&method=findIdGrupo(const " + codigo + ":String)");
}

function btnFindCdGrupoOnClickAux(content) {
	var result = null;
	try {result = eval('(' + content + ')')} catch(e) {}
	if (result.code > 0) {
		$('cdGrupo').value = result.objects['CD_GRUPO'];
		$('cdGrupoView').value = result.objects['NM_GRUPO'];
		loadGrupos();
        if(isIdByGrupo) 
        	getNextIdReduzido(null);	
	}
	else {
		createMsgbox("jMsg", {width: 200, height: 20, message: "Grupo não cadastrado!", msgboxType: "ERROR"});
		$('cdGrupoView').value = '';
		$('cdGrupo').value     = '';
		$('idGrupo').value     = '';
		loadGrupos();
		$('cdGrupoView').focus();
        if(isIdByGrupo) 
        	$('idReduzido').value = '';	

	}
}

var produtoGrupoTemp = null;
function btnNewProdutoGrupoOnClick(reg) {
    if(!reg) {
		if ($('cdProdutoServico').value == 0) {
            createMsgbox("jMsg", {width: 250, height: 50, message: "Inclua ou localize um produto para adicionar grupos.", msgboxType: "INFO"})
		} 
		else if ($('cdGrupo').value <= 0) {
            createMsgbox("jMsg", {width: 350, height: 50, msgboxType: "INFO",
                                  message: "Você só precisará incluir um outro grupo após informar os dois grupos solicitados!"})
		} 
		else {
			FilterOne.create("jFiltro", {caption:"Pesquisar grupos de produtos", width: 450, height: 325, modal: true, noDrag: true,
									     className: "com.tivic.manager.alm.GrupoDAO", method: "find", allowFindAll: true,
									     filterFields: [[{label:"Nome", reference:"NM_GRUPO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
									     gridOptions: {columns: [{label:"Nome", reference:"NM_GRUPO"}], strippedLines: true, columnSeparator: false, lineSeparator: false},
									     callback: btnNewProdutoGrupoOnClick, 
										 autoExecuteOnEnter: true});
		}
    }
    else {// retorno
        filterWindow.close();
		produtoGrupoTemp = reg[0];
		setTimeout('btnNewProdutoGrupoAuxOnClick()', 1);
    }
}

function btnNewProdutoGrupoAuxOnClick(content){
    if(content==null){
		var cdGrupoNew = produtoGrupoTemp["CD_GRUPO"];
		var nmGrupo = produtoGrupoTemp['NM_GRUPO'];
		var cdProdutoServico = $('cdProdutoServico').value;
		var cdEmpresa = $('cdEmpresa').value;
		var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		var executionDescription = "Adição do grupo " + nmGrupo + " (Cód. " + cdGrupoNew + ") ao produto " + produtoServicoDescription;		
		getPage("GET", "btnNewProdutoGrupoAuxOnClick", "../methodcaller?className=com.tivic.manager.alm.ProdutoGrupoServices"+
												  "&method=insert(new com.tivic.manager.alm.ProdutoGrupo(cdProdutoServico: int, cdGrupo: int, cdEmpresa: int, lgPrincipal: int):com.tivic.manager.alm.ProdutoGrupo)" +
												  "&cdProdutoServico=" + cdProdutoServico +
												  "&cdGrupo=" + cdGrupoNew +
												  "&lgPrincipal=0" +
												  "&cdEmpresa=" + cdEmpresa, null, null, null, executionDescription);
    }
    else{
        var ok = parseInt(content, 10) > 0;
		if (ok) {
			produtoGrupoTemp['LG_PRINCIPAL'] = 0;
			loadGrupos();
			createMsgbox("jMsg", {width: 200,
								  height: 20,
								  message: "Grupo adicionado com sucesso!",
								  msgboxType: "INFO"});
		}
		else
            createTempbox("jMsg", {width: 200, 
								   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", 
								   time: 3000});
    }
	changeLayoutGrupos((gridGrupos != null ? gridGrupos.size() : 0));
}

function btnDeleteProdutoGrupoOnClick(content)	{
	if(content==null) {
		if (gridGrupos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o grupo que deseja excluir.');
		else {
			var cdGrupoDelete = gridGrupos.getSelectedRow().register['CD_GRUPO'];
			var nmGrupo = gridGrupos.getSelectedRow().register['NM_GRUPO'];
			var cdEmpresa = $('cdEmpresa').value;
			var cdProdutoServico = $('cdProdutoServico').value;
			var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		    var executionDescription = "Remoção do grupo " + nmGrupo + " (Cód. " + cdGrupoDelete + ") do produto " + produtoServicoDescription;	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o Grupo selecionado?', 
							function() {
								getPage('GET', 'btnDeleteProdutoGrupoOnClick', 
										'../methodcaller?className=com.tivic.manager.alm.ProdutoGrupoDAO'+
										'&method=delete(const ' + cdProdutoServico + ':int, const ' + cdGrupoDelete + ':int, const ' + cdEmpresa + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			createMsgbox("jMsg", {width: 200, height: 20, message: "Grupo excluído com sucesso!", msgboxType: "INFO"});
			loadGrupos();
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros ao excluir grupo deste produto.');
	}
}

/*********************************************************************************************************************************
 ******************************************* DISPONIBILIDADE EM ESTOQUE  *********************************************************
 *********************************************************************************************************************************/
function btnClearCdLocalArmazenamentoOnClick(target) {
	var idTarget = target==null ? 0 : parseInt(target, 10);
	switch(idTarget) {
		case 0:
			$('cdLocalArmazenamentoSearch').value = 0;
			$('nmLocalArmazenamentoSearch').value = '';
			break;
		case 1:
			$('cdLocalArmazenamentoMovimentoSearch').value = 0;
			$('nmLocalArmazenamentoMovimentoSearch').value = '';
			break;
	}
}

function btnFindCdLocalArmazenamentoOnClick(reg) {
	if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Locais de Armazenamento", 
												    width: 450,
												    height: 225,
												    top: 40,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.alm.LocalArmazenamentoDAO",
												    method: "find",
												    allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"nm_local_armazenamento", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"nm_local_armazenamento"}],
													   		      strippedLines: true,
															   	  columnSeparator: false,
															   	  lineSeparator: false},
												    hiddenFields: [{reference:"CD_EMPRESA", value:$("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER}],
												    callback: btnFindCdLocalArmazenamentoOnClick, 
													autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
        $('cdLocalArmazenamentoSearch').value = reg[0]['NM_LOCAL_ARMAZENAMENTO'];
		$('nmLocalArmazenamentoSearch').value = reg[0]['NM_LOCAL_ARMAZENAMENTO'];
    }
}

function btnConsultarEstoqueOnClick(content, clearGrid) {
	if (content==null && clearGrid==null) {
		var cdProdutoServico = $('cdProdutoServico').value;
		if (cdProdutoServico <= 0) {
			createMsgbox("jMsg", {width: 350, 
								  height: 30, 
								  message: "Inclua ou localize um produto antes de executar esta consulta.", 
								  msgboxType: "INFO"});
		}
		else {
			var cdEmpresa = $('cdEmpresa').value;
			var objects = '';
			var execute = '';
			if (trim($('nmLocalArmazenamentoSearch').value) != '') {
				objects += 'item1=sol.dao.ItemComparator(const A.nm_produto_servico:String, nmLocalArmazenamentoSearch:String, ' + _LIKE_BEGIN + ', ' + _VARCHAR + ')';
				execute += 'criterios.add(*item1:java.lang.Object);';
			}	
			setTimeout(function()	{
				getPage('GET', 'btnConsultarEstoqueOnClick', 
						'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
						(objects != '' ? '&objects=criterios=java.util.ArrayList();' + objects : '&objects=criterios=java.util.ArrayList();') +
						(execute != '' ? '&execute=' + execute : '') +
						'&method=findEstoqueProdutoByLocalArmazenamento(const ' + cdEmpresa + ':int, const ' + cdProdutoServico + ':int, *criterios:java.util.ArrayList):int')}, 100);
		}
	}
	else {
		clearFields(localArmazenamentoFields);
		var rsmLocaisArmazenamento = null;
		try {rsmLocaisArmazenamento = eval('(' + content + ')')} catch(e) {}
		for (var i = 0; rsmLocaisArmazenamento!=null && i<rsmLocaisArmazenamento.lines.length; i++) {
			if (rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE']==null)
				rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE'] = 0;
			if (rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE_CONSIGNADO']==null)
				rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE_CONSIGNADO'] = 0;
			rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE_TOTAL'] = rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE'] + rsmLocaisArmazenamento.lines[i]['QT_ESTOQUE_CONSIGNADO'];
		}
		gridLocaisArmazenamento = GridOne.create('gridLocaisArmazenamento', {columns: columnsLocalArmazenamento,
																			 resultset :rsmLocaisArmazenamento, 
																			 plotPlace : $('divGridLocaisArmazenamento'),
																			 onSelect : onClickLocalArmazenamento});
	}
}

function onClickLocalArmazenamento() {
	var register = this.register;
	for(i=0; i<localArmazenamentoFields.length; i++){
		var field = localArmazenamentoFields[i];
		if (field==null || field.tagName.toLowerCase()=='button')
			continue;
		if(field.getAttribute("reference")!=null && register[field.getAttribute("reference").toUpperCase()]!=null){
			var value = register[field.getAttribute("reference").toUpperCase()];
			if(field.getAttribute("mask")!=null){
				var mask = field.getAttribute("mask");
				var datatype = field.getAttribute("datatype");
				if(datatype == "DATE" || datatype == "DATETIME")
					value = (new Mask(field.getAttribute("mask"), "date")).format(value);
				else if(datatype == "FLOAT" || datatype == "INT")
					value = (new Mask(field.getAttribute("mask"), "number")).format(value);
				else 
					value = (new Mask(field.getAttribute("mask"))).format(value);
			}
			if (field.type == "checkbox")
				field.checked = field.value == value;
			else
				field.value = value;
		}
		else
			if (field.type == "checkbox")
				field.checked = false;
			else
				field.value = "";
	}
	$('dataOldLocalArmazenamento').value = captureValuesOfFields(localArmazenamentoFields);
}

function btnSaveLocalArmazenamentoOnClick(content) {
	if (content==null) {
		if (gridLocaisArmazenamento==null || gridLocaisArmazenamento.getSelectedRow()==null)
			createMsgbox("jMsg", {width: 300, 
								  height: 40, 
								  message: "Nenhum Local de Armazenamento selecionado.", 
								  msgboxType: "INFO"});
		else {
			var cdProdutoServico = $('cdProdutoServico').value;
			var cdEmpresa = $('cdEmpresa').value;
			var cdLocalArmazenamento = gridLocaisArmazenamento.getSelectedRow()['register']['CD_LOCAL_ARMAZENAMENTO'];
			var nmLocalArmazenamento = gridLocaisArmazenamento.getSelectedRow()['register']['NM_LOCAL_ARMAZENAMENTO'];
			var executionDescription = 'Configuração de parâmetros de armazenamento do produto no local de Armazenamento ' + nmLocalArmazenamento + "\n" + 
									   getSeparador() + "\nDados Anteriores:\n" + $("dataOldLocalArmazenamento").value +
									   getSeparador() + '\nDados atuais:\n' +
									   captureValuesOfFields(localArmazenamentoFields);
			getPage('POST', 'btnSaveLocalArmazenamentoOnClick', 
					'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
					'&method=update(new com.tivic.manager.alm.ProdutoEstoque(cdLocalArmazenamento:int, cdProdutoServico:int, cdEmpresa:int, qtEstoque:float, qtIdealLocalArmazenamento:float, qtMinimaLocalArmazenamento:float, qtMaximaLocalArmazenamento:float, qtDiasEstoqueLocalArmazenamento:int, qtMinimaEcommerceLocalArmazenamento:float, qtEstoqueConsignado:float):com.tivic.manager.alm.ProdutoEstoque)' +
					'&cdProdutoServico=' + cdProdutoServico +
					'&cdEmpresa=' + cdEmpresa +
					'&cdLocalArmazenamento=' + cdLocalArmazenamento, localArmazenamentoFields, null, null, executionDescription);
		}	
	}
	else {
		if(parseInt(content, 10)==1){
			var register = {};
			for (var i=0; i<localArmazenamentoFields.length; i++)
				if (localArmazenamentoFields[i].getAttribute("reference") != null)
					if (localArmazenamentoFields[i].tagName.toUpperCase()=='INPUT' && (localArmazenamentoFields[i].type.toUpperCase()=='CHECKBOX' || localArmazenamentoFields[i].type.toUpperCase()=='RADIOBUTTON'))
						register[localArmazenamentoFields[i].getAttribute("reference").toUpperCase()] = localArmazenamentoFields[i].checked ? 1 : 0;
					else {
						if (localArmazenamentoFields[i].getAttribute("mask")!=null && (localArmazenamentoFields[i].getAttribute("datatype")!='DATE' && localArmazenamentoFields[i].getAttribute("datatype")!='DATETIME'))
							register[localArmazenamentoFields[i].getAttribute("reference").toUpperCase()] = changeLocale(localArmazenamentoFields[i].id);
						else
							register[localArmazenamentoFields[i].getAttribute("reference").toUpperCase()] = localArmazenamentoFields[i].value
					}
			gridLocaisArmazenamento.getSelectedRow()['register'] = register;
            createTempbox("jTemp", {width: 250, 
									height: 75, 
									message: "Atualização realizada com sucesso!",  
									time: 1000});
        }
        else
            createTempbox("jTemp", {width: 300, 
			                        height: 75, 
									message: "Erros reportados ao executar atualização!", 
									time: 2000});
	}
}

/*********************************************************************************************************************************
 ****************************************************** TRIBUTOS  ****************************************************************
 *********************************************************************************************************************************/
var columnsTributo = [];
var tvTributos;
var stTributaria = <%=sol.util.Jso.getStream(TributoAliquotaServices.situacaoTributaria)%>;
var tpOperacao =  <%=sol.util.Jso.getStream(TributoAliquotaServices.tipoOperacao)%>;
var rsmTributos = null;

function loadTributos(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		var cdProdutoServico = $('cdProdutoServico').value;
		getPage("GET", "loadTributos", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
				"&method=getAllTributos(const " + cdProdutoServico + ":int)");
	}
	else {
		var rsmTributos = null;
		try {rsmTributos = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmTributos!=null && i<rsmTributos.lines.length; i++) {
			var tributo = rsmTributos.lines[i];
			tributo['NM_LABEL'] = tributo['NM_TRIBUTO'];
			for (var j=0; tributo['subResultSetMap']!=null && j<tributo['subResultSetMap'].lines.length; j++) {
				var aliquota = tributo['subResultSetMap'].lines[j];
				aliquota['NM_LABEL'] = tpOperacao[aliquota['TP_OPERACAO']]+': '+stTributaria[parseInt(aliquota['ST_TRIBUTARIA'], 10)];
				if (parseInt(aliquota['ST_TRIBUTARIA'], 10) == <%=TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL%> || parseInt(aliquota['ST_TRIBUTARIA'], 10) == <%=TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA%>)
					aliquota['NM_LABEL'] += ' - Alíquota: ' + formatCurrency(aliquota['PR_ALIQUOTA']) + '%'
				aliquota['_IMG'] = '../fsc/imagens/aliquota16.gif';
				for (var k=0; aliquota['subResultSetMap']!=null && k<aliquota['subResultSetMap'].lines.length; k++) {
					var configuracao = aliquota['subResultSetMap'].lines[k];
					var nmLogradouro = configuracao['CD_CIDADE']!=0 ? configuracao['NM_CIDADE'] : 
									        configuracao['CD_ESTADO']!=0 ? configuracao['NM_ESTADO'] : 
									        	configuracao['CD_PAIS']!=0 ? configuracao['NM_PAIS'] : null;
					var nmNaturezaOperacao = configuracao['CD_NATUREZA_OPERACAO']!=0 ? configuracao['NM_NATUREZA_OPERACAO'] : null;
					configuracao['_IMG'] = '../fsc/imagens/configuracao_aliquota16.gif';
					configuracao['TP_ESFERA'] = configuracao['CD_CIDADE'] != 0 ? <%=TributoServices._ESFERA_MUNICIPAL%> :
												   		configuracao['CD_ESTADO'] != 0 ? <%=TributoServices._ESFERA_ESTADUAL%> : <%=TributoServices._ESFERA_FEDERAL%>;
					configuracao['NM_LABEL'] = '';
					var origemDestino = aliquota['TP_OPERACAO']==1 ? 'Origem' : aliquota['TP_OPERACAO']==2 ? 'Destino' : 'Origem/Destino';
					if (nmNaturezaOperacao != null)
						configuracao['NM_LABEL'] += (configuracao['NM_LABEL']!='' ? ' / ' : '') + 'Nat. Operação (CFOP): ' + nmNaturezaOperacao;
					if (nmLogradouro != null)
						configuracao['NM_LABEL'] += ' - '+origemDestino+': '+nmLogradouro;
					if (configuracao['NM_LABEL'] == '')
						configuracao['NM_LABEL'] += 'Configuração padrão de alíquotas';
				}
			}
		}
		
		tvTributos = TreeOne.create('treeTributos', {resultset: rsmTributos,
										 columns: ['NM_LABEL'],
										 defaultImage: '../fsc/imagens/tributo16.gif',
										 plotPlace: $('divTreeTributos')});

	}
}

/*********************************************************************************************************************************
 ********************************************* Grupos *************************************************************************
 *********************************************************************************************************************************/
 function loadGrupos(content) {
	if (content==null && $('cdGrupo').value != 0) {
		getPage("GET", "loadGrupos", 
				"../methodcaller?className=com.tivic.manager.alm.GrupoServices"+
				"&method=getHierarquiaOfGrupo(const " + $('cdGrupo').value + ":int)");
	}
	else {
		var rsmGrupo = null;
 		try {rsmGrupo = eval('(' + content + ')')} catch(e) {}
 		gridGrupo = GridOne.create('gridGrupo', {columns: [{label: 'Grupo', reference: 'GRUPO'}],          
					                                     resultset: rsmGrupo, plotPlace: $('divGridGrupo')});
	}
}

/*********************************************************************************************************************************
 ****************************************************** MOVIMENTOS ***************************************************************
 *********************************************************************************************************************************/
function btnFindCdLocalArmazenamentoMovimentoOnClick(reg) {
	if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Locais de Armazenamento", 
												    width: 450,
												    height: 275,
												    top:40,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.alm.LocalArmazenamentoDAO",
												    method: "find",
												    allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"nm_local_armazenamento", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"nm_local_armazenamento"}],
													  		      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    hiddenFields: [{reference:"cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER}],
												    callback: btnFindCdLocalArmazenamentoMovimentoOnClick, 
													autoExecuteOnEnter: true
										           });
    }
    else {// retorno
        filterWindow.close();
        $('cdLocalArmazenamentoMovimentoSearch').value = reg[0]['NM_LOCAL_ARMAZENAMENTO'];
		$('nmLocalArmazenamentoMovimentoSearch').value = reg[0]['NM_LOCAL_ARMAZENAMENTO'];
		document.disabledTab = true;
		$('btnConsultarMovimento').focus();
    }
}

function btnConsultarMovimentoOnClick(content, clearGrid) {
	if (content==null && clearGrid==null) {
		var cdProdutoServico = $('cdProdutoServico').value;
		if (cdProdutoServico <= 0) {
			createMsgbox("jMsg", {width: 350, 
								  height: 30, 
								  message: "Inclua ou localize um produto antes de executar esta consulta.", 
								  msgboxType: "INFO"});
		}
		else {
			var cdEmpresa = $('cdEmpresa').value;
		}
	}
}

function onClickMovimento() {
}

/*********************************************************************************************************************************
 ****************************************************** COMPONENTES *************************************************************
 *********************************************************************************************************************************/
var rsmComponentes = null;
var levelComponente = null;
function initComponente() {
    clearFields(componenteFields);
	disabledFormComponente = true;
	alterFieldsStatus(false, componenteFields, "qtProdutoServico", "disabledField2");
	loadComponentes();
}

function clearFormComponente(){
    disabledFormComponente = false;
    clearFields(componenteFields);
    alterFieldsStatus(true, componenteFields, "qtProdutoServico");

	$('qtProdutoServico').value = 0;
	$('prPerda').value          = 0;
	$('lgCalculoCusto').value   = 1;
	$('lgCalculoCusto').checked = true;
}

function formValidationComponente(){
	var fields = [[$("qtProdutoServico"), 'Quantidade', VAL_CAMPO_MAIOR_QUE, 0]];
	if (!$("cdProdutoServicoComponente").value > 0)
	  fields.push([$("cdReferencia"), 'Produto/Serviço/Referência', VAL_CAMPO_MAIOR_QUE, 0])
	else
	  fields.push([$("cdProdutoServicoComponente"), 'Produto/Serviço/Referência', VAL_CAMPO_MAIOR_QUE, 0]);
	return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'cdUnidadeMedidaComponente');
}

function btnNewComponenteOnClick(){
	clearFormComponente();
	btnFindComponenteOnClick(null, true);
	createGridSubComponentes();
}

function btnAlterComponenteOnClick(){
    disabledFormComponente = false;
    alterFieldsStatus(true, componenteFields, "qtProdutoServico");
}

function btnSaveComponenteOnClick(content) {
	if (content==null) {
		if ($('cdProdutoServico').value=='' || $('cdProdutoServico').value=='0') {
			createTempbox("jMsg1", {width: 340,
								   height: 45,
								   message: "Grave ou pesquise um produto/serviço antes de continuar!",
								   tempboxType: "ALERT",
								   time: 2000});
		}
        if (disabledFormComponente) { 
            createMsgbox("jMsg2", {width: 250,
                                  height: 50,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
			return;
        }
		if (formValidationComponente()) {
			var construtorComposicao = "new com.tivic.manager.grl.ProdutoServicoComposicao(cdComposicao: int, const " + $('cdEmpresa').value + ": int, const " + $('cdProdutoServico').value + ": int, " +
			              			   "cdProdutoServicoComponente: int, cdReferencia: int, cdUnidadeMedidaComponente: int, qtProdutoServico: float, " + 
									   "prPerda: float, lgCalculoCusto: int)";
			var method = (($("cdComposicao").value > 0) ? "update" : "insert") + "(" + construtorComposicao + ":com.tivic.manager.grl.ProdutoServicoComposicao, const " + $('cdEmpresa').value + ":int)";
			getPage("POST", "btnSaveComponenteOnClick", "../methodcaller?className=com.tivic.manager.grl.ProdutoServicoComposicaoServices" + 
												         "&method=" + method, componenteFields);
			return;
		}
	}
	else {
	    var retorno = null;
		try {retorno = eval('(' + content + ')')} catch(e) {}
		if (retorno > 0) {
			createTempbox("jMsg5", {width: 200,
								   height: 50,
								   message: "Dados gravados com sucesso!",
								   tempboxType: "INFO",
								   time: 2000});
			disabledFormComponente = true;
			alterFieldsStatus(false, componenteFields, "qtProdutoServico", "disabledField2");
			$("dataOldComponente").value = captureValuesOfFields(componenteFields);

			levelComponente = tvComponentes.findLevel("CD_PRODUTO_SERVICO", $('cdProdutoServicoComponente').value, true);
			setTimeout('loadComponentes()', 10);
		}
		else {
		    if (retorno == -2) {
				createTempbox("jMsg3", {width: 330,
									   height: 55,
									   message: "Não existe regra de conversão entre a unidade selecionada e a unidade do componente!",
									   tempboxType: "ALERT",
									   time: 3000});
			}
			else if (retorno == -3) {
				createTempbox("jMsg4", {width: 330,
									   height: 55,
									   message: "Componente já cadastrado para este produto/serviço!",
									   tempboxType: "ALERT",
									   time: 2000});
			}
			else {
				createTempbox("jMsg6", {width: 200,
									   height: 50,
									   message: "ERRO ao tentar gravar dados!",
									   tempboxType: "ERROR",
									   time: 3000});
			}
		}
	}
}

function btnFindComponenteOnClick(reg, lookup) {
	if(!reg){
		if ($('cdProdutoServico').value == '' || $('cdProdutoServico').value == '0') {
			createTempbox("jMsg1", {width: 340,
								   height: 45,
								   message: "Grave ou pesquise um produto/serviço antes de continuar!",
								   tempboxType: "ALERT",
								   time: 2000});
		} 
		else if (!lookup && tvComponentes.lines <= 0) {
			createTempbox("jMsg2", {width: 390,
								   height: 45,
								   message: "Não existem componentes cadastrados para este produto/serviço!",
								   tempboxType: "ALERT",
								   time: 2000});
		} 
		else {
			var hiddenFields = [{reference:"B.CD_EMPRESA", value:$("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER}];	
			var filterFieldsProdutoServico = [[{label: "Produto/Serviço/Referência", reference: "NM_COMPONENTE", datatype: _VARCHAR, comparator: _LIKE_ANY, width: 100, charcase: 'uppercase'}],
											  [{label: "Fabricante", reference: "nm_pessoa", datatype: _VARCHAR, comparator: _LIKE_BEGIN, width: 60, charcase:'uppercase'},
											   {label: "ID", reference: "id_produto_servico", datatype: _VARCHAR, comparator: _LIKE_BEGIN, width: 40, charcase: 'uppercase'}]];
			filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar componentes", 
														width: 550,
														height: 300,
														top: 50,
														modal: true,
														noDrag: true,
														className: "com.tivic.manager.grl.ProdutoServicoComposicaoServices",
														method: "find",
														allowFindAll: false,
														filterFields: filterFieldsProdutoServico,
														gridOptions: {columns: [{label:"Produto/Serviço", reference:"NM_PRODUTO_SERVICO"},
																				{label:"ID", reference:"ID_PRODUTO_SERVICO"},
																				{label:"ID reduzido", reference:"id_reduzido"},
																				{label:"Fabricante", reference:"nm_fabricante"},
																				{label:"Grupo", reference:"nm_grupo"}],
																	  strippedLines: true,
																	  columnSeparator: false,
																	  lineSeparator: false},
														hiddenFields: hiddenFields,
														callback: btnFindComponenteOnClick,
														callbackOptions: lookup, 
														autoExecuteOnEnter: true
													   });
		}
	}
	else {// retorno
		filterWindow.close();
		if (!lookup) {
			levelComponente = tvComponentes.findLevel("CD_PRODUTO_SERVICO", reg[0]["CD_PRODUTO_SERVICO"], true);
			if (levelComponente != null) {
				tvComponentes.selectLevel(levelComponente);
			}
			else {
				createTempbox("jMsg", {width: 320,
									   height: 45,
									   message: "Componente não encontrado neste produto/serviço!",
									   tempboxType: "ALERT",
									   time: 2000});
			}
		}
		else {
			if (reg[0]['CD_PRODUTO_SERVICO'] == $('cdProdutoServico').value) {
				createTempbox("jMsg", {width: 470,
									   height: 45,
									   message: "Não é possível que um produto/serviço/referência seja componente dele mesmo!",
									   tempboxType: "ALERT",
									   time: 3500});
			}
			else {
				$('cdProdutoServicoComponente').value = reg[0]['CD_PRODUTO_SERVICO'];
				$('cdProdutoServicoComponenteView').value = reg[0]['NM_PRODUTO_SERVICO'];
				$('txtProdutoServicoComponente').value = reg[0]['TXT_PRODUTO_SERVICO'];
				$('cdUnidadeMedidaComponente').value = reg[0]['CD_UNIDADE_MEDIDA'];
				$('cdReferencia').value = reg[0]['CD_REFERENCIA'];
				$('qtProdutoServico').select();
				$('qtProdutoServico').focus();
			}
		}
	}
}

function loadFormComponente(register){
	disabledFormComponente = true;
	alterFieldsStatus(false, componenteFields, "qtProdutoServico", "disabledField2");
	
	loadFormRegister(componenteFields, register);
	
	$("dataOldComponente").value = captureValuesOfFields(componenteFields);
}

function btnDeleteComponenteOnClick(content){
    if(content==null){
        if(!tvComponentes.getSelectedLevel())
            createTempbox("jMsg1", {width: 230, height: 45, message: "Nenhum componente selecionado!", tempboxType: "ALERT", time: 2000});
        else if(tvComponentes.getSelectedLevelRegister()["CD_PRODUTO_SERVICO"]==$('cdProdutoServico').value)
	   	  createTempbox("jMsg2", {width: 250,
								 height: 45,
								 message: "Não é possível excluir o produto através dessa opção.",
								 tempboxType: "ALERT",
								 time: 2000});
	   else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {
												setTimeout(function(){
															    getPage("GET", "btnDeleteComponenteOnClick", 
																	  "../methodcaller?className=com.tivic.manager.grl.ProdutoServicoComposicaoDAO"+
																	  "&method=delete(const " + tvComponentes.getSelectedLevelRegister()['CD_COMPOSICAO'] + ":int):int");
															}, 10);
													}
									});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 200, 
                                  	height: 45, 
                                  	message: "Registro excluído com sucesso!",
						    		tempboxType: 'INFO',
                                  	time: 3000});
            clearFormComponente();
			setTimeout("loadComponentes()", 10);
        }
        else
            createTempbox("jTemp", {width: 230, 
                                  	height: 45, 
                                  	message: "Não foi possível excluir este registro!", 
						    		tempboxType: 'ERROR',
                                  	time: 5000});
    }	
}

function loadComponentes(content) {
	if (content==null && parseInt($('cdProdutoServico').value, 10) > 0) {
		getPage("GET", "loadComponentes", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoComposicaoServices" +
				"&method=getAllByEmpresaProdutoServico(const " + $('cdEmpresa').value + ":int, const " + $('cdProdutoServico').value + ":int)", null, true);
	}
	else {
		try {rsmComponentes = eval('(' + content + ')')} catch(e) {}
		for (var i = 0; rsmComponentes != null && i < rsmComponentes.lines.length; i++) {
			if (parseInt(rsmComponentes.lines[i]['CD_REFERENCIA'], 10) > 0) {
				rsmComponentes.lines[i]['NM_PRODUTO_SERVICO'] = rsmComponentes.lines[i]['NM_REFERENCIA'];
				rsmComponentes.lines[i]['NM_PRODUTO_SERVICO_COMPONENTE'] = rsmComponentes.lines[i]['NM_REFERENCIA'];
				rsmComponentes.lines[i]['ID_PRODUTO_SERVICO_COMPONENTE'] = rsmComponentes.lines[i]['ID_REFERENCIA'];
			}
		}
		gridComposicao = GridOne.create('divGridComposicao', {columns: [{label: 'Composicao', reference: 'NM_PRODUTO_SERVICO'}],
															  onDoubleClick:function() {
																  findComponente(null, gridComposicao.getSelectedRowRegister()['CD_PRODUTO_SERVICO_COMPONENTE']); 
														 	  },
            												  resultset: rsmComponentes, plotPlace: $('divGridComposicao')});
	}
}

function loadProdutoServico(content) {
	if (reg==null) {
		var reg = gridSubComponentes.getSelectedRowRegister();
		if (gridSubComponentes != null && reg != null) {
			setTimeout(function()	{
				   getPage('POST', 'fillFormProdutoServico', 
						   '../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices' +
						   '&objects=crt=java.util.ArrayList();' +
						   'item=sol.dao.ItemComparator(const A.cd_produto_servico:String, const ' + reg["CD_PRODUTO_SERVICO"] + ':String, const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);' +
						   '&execute=crt.add(*item:Object);' +
						   '&method=findCompleto(*crt:java.util.ArrayList)', null, true)}, 100);
		}
	}
}

function findComponente(content, cdProdutoServicoComponente) {
	if(content == null){
		var className = "com.tivic.manager.grl.ProdutoServicoComposicaoServices";
		createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
		getPage("POST", "findComponente", "../methodcaller?className="+ className + 
						  "&method=getComponente(const "+$('cdProdutoServico').value+":int, const "+cdProdutoServicoComponente+":int, const " + $('cdEmpresa').value + ":int)", null, null, null, null);
	}
    else {
    	closeWindow("jLoadMsg");
        alterFieldsStatus(false, componenteFields, "nmProdutoServico", "disabledField2");
        var rsmPesquisa = null;
        try {rsmPesquisa = eval('(' + content + ')')} catch(e) {}
    	var reg = rsmPesquisa.lines[0];
    	$('cdProdutoServicoComponente').value		= reg['CD_PRODUTO_SERVICO_COMPONENTE'];
    	$('cdProdutoServicoComponenteView').value	= reg['NM_PRODUTO_SERVICO'];
    	$('cdUnidadeMedidaComponente').value		= reg['CD_UNIDADE_MEDIDA_COMPONENTE'];
    	$('qtProdutoServico').value     			= reg['QT_PRODUTO_SERVICO'];
    	$('prPerda').value     						= reg['PR_PERDA'];
    	$('txtProdutoServicoComponente').value		= reg['TXT_PRODUTO_SERVICO_COMPONENTE'];
		
    	$("cdProdutoServicoComponenteView").focus();
    	tabSubstituto.showTab(1);
	}

}

function fillFormProdutoServico(content)	{
    var rsm = null;
	try {rsm = eval('(' + content + ')')} catch(e) {}
	btnFindProdutoServicoOnClick(rsm.lines);
	tabProdutoServico.showTab(0, false);
}

function btnPrintComponenteOnClick() {

}

/****** FOTOS ******/
var gridFotoProduto = null;
var columnsFotoProduto = [{label:'Nome', reference: 'NM_FOTO'}, 
					  	  {label:'Ordem', reference: 'NR_ORDEM'}, 
					  	  {label:'Dw', reference: 'DOWNLOAD_IMG', type: GridOne._IMAGE, onImgClick: onClickDownloadFotoProduto, imgWidth: 10, Hint: 'Efetuar download da foto...'},
					  	  {label:'Vs', reference: 'VISUALIZAR_IMG', type: GridOne._IMAGE, onImgClick: onClickVisualizarFotoProduto, imgWidth: 10, Hint: 'Visualizar a foto...'}
					 	 ];
function loadFotoProduto(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		var cdEmpresa = $("cdEmpresa").value;
		getPage("GET", "loadFotoProduto", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
				"&method=getAllFotosOfProduto(const " + $('cdProdutoServico').value + ":int, const " + cdEmpresa + ":int)", null, true);
	}
	else {
		var rsmFotoProduto = null;
		try {rsmFotoProduto = eval('(' + content + ')')} catch(e) {}
		for (var i=0; rsmFotoProduto!=null && i<rsmFotoProduto.lines.length; i++) {
			rsmFotoProduto.lines[i]['DOWNLOAD_IMG'] = 'imagens/download16.gif';
			rsmFotoProduto.lines[i]['VISUALIZAR_IMG'] = 'imagens/visualizar16.gif';
		}
		gridFotoProduto = GridOne.create('gridFotoProduto', {columns: columnsFotoProduto,
														     resultset: rsmFotoProduto, 
														     plotPlace: $('divGridFotoProduto'),
														     onSelect: onClickFotoProduto});
	}
}

function onClickFotoProduto() {
	if (this != null) {
		var arquivo = this.register;
		if (arquivo != null)	{
			loadFormRegister(fotoProdutoFields, arquivo);
			$("dataOldFotoProduto").value = captureValuesOfFields(fotoProdutoFields);		
		}
	}
}

function btnNewFotoProdutoOnClick(){
    if ($('cdProdutoServico').value <= 0) {
	    createTempbox("jMsg", {width: 300,
	                           height: 50,
	                           message: "Inclua ou localize um produto para cadastrar fotos.",
	                           tempboxType: "INFO",
	                           time: 2000});
	}	                     
	else {
		gridFotoProduto.unselectGrid();
		$("dataOldFotoProduto").value = "";
		clearFields(fotoProdutoFields);
		$('nrOrdemFoto').value = gridFotoProduto.size() + 1;
		$('nmFoto').value = 'FOTO Nº ' + $('nrOrdemFoto').value + ' (' + $('nmProdutoServico').value + ')'; 
		createWindow('jFotoProduto', {caption: "FotoProduto",
									  width: 510,
									  height: 120,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'fotoProdutoPanel'});
		$('nmFoto').focus();
	}
}

function btnAlterFotoProdutoOnClick(){
    if (gridFotoProduto.getSelectedRow()==null) {
	    createTempbox("jMsg", {width: 200,
	                           height: 50,
	                           message: "Selecione a foto que você deseja alterar.",
	                           tempboxType: "INFO",
	                           time: 2000});
	}	                          
	else {
		createWindow('jFotoProduto', {caption: "FotoProduto",
									  width: 510,
									  height: 120,
									  noDropContent: true,
									  modal: true,
									  contentDiv: 'fotoProdutoPanel'});
		$('nmFoto').focus();
	}
}

function formValidationFotoProduto() {
    if(!validarCampo($("nmFoto"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Descrição' deve ser preenchido.", true, null, null))
        return false;
    else if($("cdFoto").value <= 0 && !validarCampo($("imgFoto"), VAL_CAMPO_NAO_PREENCHIDO, true, "Indique o local onde se encontra o arquivo.", true, null, null))
        return false;
	else
		return true;
}

function btnSaveFotoProdutoOnClick(content){
    if(content==null){
        if (formValidationFotoProduto()) {
			var fotoProdutoDescription = "(Produto/Serviço: " + $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value + ")";
            var executionDescription = $("cdFoto").value > 0 ? formatDescriptionUpdate("Foto do produto " + fotoProdutoDescription, $("cdFoto").value, $("dataOldFotoProduto").value, fotoProdutoFields) : formatDescriptionInsert("Foto do produto " + fotoProdutoDescription, fotoProdutoFields);
            var parameterImgFoto = trim($("imgFoto").value) == '' ? 'imgFotoTemp' : '*imgFoto';
            var parameterUpdateFile = trim($("imgFoto").value) == '' ? 'true' : 'false';
			var constructorFotoProduto = "cdFoto: int, cdProdutoServico: int, cdEmpresa: int, nmFoto: String, nrOrdemFoto: int, " + parameterImgFoto + ": byte[]";
			var objects = trim($("imgFoto").value) == '' ? '' : 'imgFoto=byte[]';
			var commandExecute = trim($("imgFoto").value) == '' ? '' : 'imgFoto=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const imgFoto:String);';
			if($("cdFoto").value > 0)
                getPage("POST", "btnSaveFotoProdutoOnClick", "../methodcaller?className=com.tivic.manager.ecm.FotoProdutoEmpresaServices" +
														  	 "&execute=" + commandExecute +
														 	 "&objects=" + objects +
                                                          	 "&method=update(new com.tivic.manager.ecm.FotoProdutoEmpresa(" + constructorFotoProduto + "):com.tivic.manager.ecm.FotoProdutoEmpresa, const " + parameterUpdateFile + ":boolean)" +
														  	 "&cdProdutoServico=" + $("cdProdutoServico").value +
														     "&cdEmpresa=" + $("cdEmpresa").value, fotoProdutoFields, true, null, executionDescription);
            else
                getPage("POST", "btnSaveFotoProdutoOnClick", "../methodcaller?className=com.tivic.manager.ecm.FotoProdutoEmpresaServices"+
														 	 "&execute=" + commandExecute +
														 	 "&objects=" + objects +
                                                          	 "&method=insert(new com.tivic.manager.ecm.FotoProdutoEmpresa(" + constructorFotoProduto + "):com.tivic.manager.ecm.FotoProdutoEmpresa)" +
														  	 "&cdProdutoServico=" + $("cdProdutoServico").value +
														     "&cdEmpresa=" + $("cdEmpresa").value, fotoProdutoFields, true, null, executionDescription);
        }
    }
    else{
		closeWindow('jFotoProduto');
		var fotoProduto = null;
		try { fotoProduto = eval("(" + content + ")")} catch(e) {}
        var ok = fotoProduto != null;
		var isInsert = $("cdFoto").value <= 0;
		if (ok) {
			if (isInsert)
				$("cdFoto").value = fotoProduto['cdFoto'];	
			var fotoProdutoRegister = {};
			for (var i = 0; i < fotoProdutoFields.length; i++) {
				fotoProdutoRegister[fotoProdutoFields[i].getAttribute("reference").toUpperCase()] = fotoProdutoFields[i].getAttribute("lguppercase") == "true" ? fotoProdutoFields[i].value.toUpperCase() : fotoProdutoFields[i].value;
			}
			fotoProdutoRegister['DOWNLOAD_IMG'] = 'imagens/download16.gif';
			fotoProdutoRegister['VISUALIZAR_IMG'] = 'imagens/visualizar16.gif';
			if (isInsert)
				gridFotoProduto.addLine(0, fotoProdutoRegister, onClickFotoProduto, true)	
			else {
				gridFotoProduto.getSelectedRow().register = fotoProdutoRegister;
			}
			gridFotoProduto.selectLineByIndex(gridFotoProduto.size());
			changeLayoutAbaFotoProduto(1, fotoProdutoRegister);
			$("dataOldFotoProduto").value = captureValuesOfFields(fotoProdutoFields);
		}	
		if (!ok)
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
    }
}

function btnDeleteFotoProdutoOnClick(content)	{
	if(content==null) {
		if (gridFotoProduto.getSelectedRow() == null) {
		    createTempbox("jMsg", {width: 300,
		                           height: 50,
		                           message: "Selecione a foto que você deseja excluir.",
		                           tempboxType: "INFO",
		                           time: 2000});
		}		                          
		else {
			var cdFoto = gridFotoProduto.getSelectedRow().register['CD_FOTO'];
			var cdProdutoServico = $('cdProdutoServico').value;
			var cdEmpresa = $('cdEmpresa').value;
			var fotoProdutoDescription = "(Produto/Serviço: " + $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value + ")";
		    var executionDescription = formatDescriptionDelete("Foto do produto " + fotoProdutoDescription, $("cdFoto").value, $("dataOldFotoProduto").value);	
			showConfirmbox('Manager', 350, 80, 'Você tem certeza que deseja remover a foto selecionada?', 
							function() {
								getPage('GET', 'btnDeleteFotoProdutoOnClick', 
										'../methodcaller?className=com.tivic.manager.ecm.FotoProdutoEmpresaServices'+
										'&method=delete(const ' + cdFoto + ':int, const ' + cdProdutoServico + ':int, const ' + cdEmpresa + ':int):int', null, true, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridFotoProduto.removeSelectedRow();
		}
		else
		    createTempbox("jMsg", {width: 200,
		                           height: 50,
		                           message: "Erro ao excluir a foto.",
		                           tempboxType: "ERROR",
		                           time: 2000});
	}
}

function btnSaveFotoProdutoAuxOnClick() {
	if (formValidationFotoProduto()) {
		if (trim($("imgFoto").value) == '')
			btnSaveFotoProdutoOnClick();
		else {
			document.frameFotoProduto.submit();
		}
	}
}

function onClickDownloadFotoProduto(source) {
	changeLayoutAbaFotoProduto(0);
	var cdFoto = this.parentNode.parentNode.register['CD_FOTO'];
	var cdProdutoServico = $('cdProdutoServico').value;
	var cdEmpresa = $('cdEmpresa').value;
	$('frameHidden').src = 'download_arquivo.jsp?className=com.tivic.manager.ecm.FotoProdutoEmpresaServices&method=getBytesFotoProduto(const ' + cdFoto + ':int, const ' + cdProdutoServico + ':int, const ' + cdEmpresa + ':int)';
}

function onClickVisualizarFotoProduto(source) {
	changeLayoutAbaFotoProduto(1, this);
}

function changeLayoutAbaFotoProduto(tipoLayout, line) {
	$('previewFotoProduto').src = 'preview_imagem.jsp?lgDefaultInBlank=1';
	if ($('divGridFotoProduto') != null) {
		$('divGridFotoProduto').style.height = (tipoLayout == 1 ? '128px' : '329px');
	}
	if (gridFotoProduto != null) {
		gridFotoProduto.resize(parseInt($('divGridFotoProduto').style.width, 10), parseInt($('divGridFotoProduto').style.height, 10));
	}
	$('divPreviewFotoProduto').style.display = (tipoLayout == 1 ? '' : 'none');;
	if (tipoLayout == 1) {
		if (gridFotoProduto) {
			if (line['CD_FOTO'] <= 0 || line['CD_FOTO'] == undefined || line['CD_FOTO'] == null) {
				var cdFoto = line.parentNode.parentNode.register['CD_FOTO'];
			}
			else {
				var cdFoto = line['CD_FOTO'];
			}
			if (cdFoto > 0) {
				var cdProdutoServico = $('cdProdutoServico').value;
				var cdEmpresa = $('cdEmpresa').value;
				$('previewFotoProduto').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.ecm.FotoProdutoEmpresaServices&method=getBytesFotoProduto(const ' + cdFoto + ':int, const ' + cdProdutoServico + ':int, const ' + cdEmpresa + ':int)';
			}
		}
	}
}


//********************************************** NCM *************************************************/

function loadNcm(content){
	if(content == null){
		if ($('cdProdutoServico').value != 0 && $('cdNcm').value != 0) {
			getPage("GET", "loadNcm", 
					"../methodcaller?className=com.tivic.manager.grl.NcmDAO"+
					"&method=get(const " +$('cdNcm').value + ":int)", null, true);
		}
	}
	else {
		var rsmNcm = null;
		try {rsmNcm = eval('(' + content + ')')} catch(e) {}
		
		$('nmNcm').value = rsmNcm['nmNcm'];
		$('nrNcm').value = rsmNcm['nrNcm'];
		
	}
}

function btnFindNrNcmOnClick(numero, funcCallback) {
    if (parseInt(numero, 10) > 0) {
    	if(funcCallback==null)
    		funcCallback = "btnFindNrNcmOnClickAux";
    	getPage("GET", funcCallback, 
				"../methodcaller?className=com.tivic.manager.grl.NcmServices&method=findNrNcm(const " + numero + ":String,const <%=cdEmpresa%>:int)");
	}
}

var rsmNcm;
function btnFindNrNcmOnClickAux(content) {
	var nmNcm;
	var cdNcm;
	try { rsmNcm = eval("(" + content + ")"); } catch(e) {}
	if(rsmNcm.lines.length != 0){
		$('nmNcm').value = rsmNcm.lines[0]['NM_NCM'];
		$('cdNcm').value = rsmNcm.lines[0]['CD_NCM'];
		if(rsmNcm.lines[0]['CD_CLASSIFICACAO_FISCAL'] > 0) {
			$('cdClassificacaoFiscal').value = rsmNcm.lines[0]['CD_CLASSIFICACAO_FISCAL'];  
		}
	}
	else {
		createMsgbox("jMsg", {width: 200, height: 20, message: "NCM não cadastrado!", msgboxType: "ERROR"});
		$('cdNcm').value     = '';
		$('nmNcm').value     = '';
		$('nrNcm').value     = '';
		$('nrNcm').focus();
	}
}

function btnFindNcmOnClick(reg, funcCallback) {
    if(!reg) {
    	if(funcCallback==null)
    		funcCallback = btnFindNcmOnClick;	
		FilterOne.create("jFiltro", {caption:"Pesquisar NCM", width: 450, height: 325, top:50, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.NcmServices", method: "find", allowFindAll: true, autoExecuteOnEnter: true,
								     filterFields: [[{label:"Número", reference:"NR_NCM", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30},
								                     {label:"Nome", reference:"NM_NCM", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70}]],
								     gridOptions: {columns: [{label:"Número", reference:"NR_NCM"},{label:"Nome", reference:"NM_NCM"}], strippedLines: true, columnSeparator: false, lineSeparator: false},
								     callback: funcCallback});
    }
    else {
        closeWindow("jFiltro");
        $('cdNcm').value     = reg[0]['CD_NCM'];
		$('nmNcm').value     = reg[0]['NM_NCM'];
		$('nrNcm').value     = reg[0]['NR_NCM'];
		if(reg[0]['CD_CLASSIFICACAO_FISCAL'] > 0) {
			$('cdClassificacaoFiscal').value = reg['CD_CLASSIFICACAO_FISCAL'];  
		}
    }
}

//***************************************************** REABASTECIMENTO ********************************************************************************************

var columnsReabastecimento = [{label: 'Local de Armazenamento Destino', reference: 'NM_LOCAL_ARMAZENAMENTO'},
                              {label: 'Estoque Atual do Destino', reference: 'QT_ESTOQUE', type: GridOne._CURRENCY},
				              {label: 'Local de Armazenamento Origem', reference: 'NM_LOCAL_ARMAZENAMENTO_ORIGEM'},
				              {label: 'Quantidade a Transferir', reference: 'QT_TRANSFERENCIA', type: GridOne._CURRENCY},
				              {label: 'Nível de Estoque Mínimo', reference: 'QT_MINIMA', type: GridOne._CURRENCY},
				              {label: 'Nível de Estoque Máximo', reference: 'QT_MAXIMA', type: GridOne._CURRENCY},
				              {label: 'Nível de Estoque Ideal', reference: 'QT_IDEAL', type: GridOne._CURRENCY},
				              {label: 'Tipo de Reabastecimento', reference: 'CL_TP_REABASTECIMENTO'},
				              {label: 'Ativo', reference: 'CL_ST_ESTOQUE'}];

function loadReabastecimento(content) {
	if(content == null){
		var className = "com.tivic.manager.alm.ProdutoEstoqueServices";
		createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
		getPage("POST", "loadReabastecimento", "../methodcaller?className="+ className + 
						  "&method=getAllByProduto(const"+$('cdProdutoServico').value+":int, const "+<%=cdEmpresa%>+":int)", reabastecimentoFields, null, null, null);
	}
    else {// retorno
    	closeWindow('jLoadMsg');
    	var rsmReabastecimento = null;
    	try {rsmReabastecimento = eval('(' + content + ')')} catch(e) {}
    	gridReabastecimento = GridOne.create('gridReabastecimento', {columns: columnsReabastecimento, resultset: rsmReabastecimento, plotPlace : $('divGridReabastecimento'), 
			                                                   noHeader: false, noSelectorColumn: false, lineSeparator: false, columnSeparator: false,
															   onDoubleClick:function() {
																   findReabastecimento(null, gridReabastecimento.getSelectedRowRegister()['CD_LOCAL_ARMAZENAMENTO']); 
													 		   },
															   onProcessRegister: function(register) {
																	 			var reg = register;
																	 			
																			}});
    	
    }

}

function clearReabastecimento(){
	clearFields(reabastecimentoFields);
	alterFieldsStatus(true, reabastecimentoFields, "cdLocalArmazenamentoDestinoView");
}

function findReabastecimento(content, cdLocalArmazenamento) {
	if(content == null){
		var className = "com.tivic.manager.alm.ProdutoEstoqueServices";
		createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
		getPage("POST", "findReabastecimento", "../methodcaller?className="+ className + 
						  "&method=getProdutoEstoque(const "+$('cdProdutoServico').value+":int, const "+$('cdEmpresa').value+":int, const "+cdLocalArmazenamento+":int)", null, null, null, null);
	}
    else {// retorno
    	closeWindow("jLoadMsg");
        //disabledFormProdutoServico=true;
        alterFieldsStatus(false, reabastecimentoFields, "cdLocalArmazenamentoView", "disabledField2");
        var rsmReabastecimento = null;
        try {rsmReabastecimento = eval('(' + content + ')')} catch(e) {}
    	var reg = rsmReabastecimento.lines[0];
    	$('cdLocalArmazenamentoDestino').value     = reg['CD_LOCAL_ARMAZENAMENTO'];
    	$('cdLocalArmazenamentoDestinoView').value = reg['NM_LOCAL_ARMAZENAMENTO'];
    	$('cdLocalArmazenamentoOrigem').value  	   = reg['CD_LOCAL_ARMAZENAMENTO_ORIGEM'];
    	$('cdLocalArmazenamentoOrigemView').value  = reg['NM_LOCAL_ARMAZENAMENTO_ORIGEM'];
    	$('tpAbastecimento').value  	           = reg['TP_ABASTECIMENTO'];
    	$('qtTransferencia').value  	           = formatCurrency(reg['QT_TRANSFERENCIA']);
    	$('qtMinimaReabastecimento').value         = formatCurrency(reg['QT_MINIMA']);
    	$('qtMaximaReabastecimento').value  	   = formatCurrency(reg['QT_MAXIMA']);
    	$('qtIdealReabastecimento').value  	   	   = formatCurrency(reg['QT_IDEAL']);
    	$('stEstoqueReabastecimento').checked  	   = (reg['ST_ESTOQUE'] == 1 ? true : false);
    	alterFieldsStatus(false, reabastecimentoFields, "cdLocalArmazenamentoDestinoView");
    	tabReabastecimento.showTab(0);
    	isInsertReabastecimento = false;
    	$("cdLocalArmazenamentoDestinoView").focus();
    	tabReabastecimento.showTab(1);
	}

}

function btnNewReabastecimentoOnClick(content){
	$('cdLocalArmazenamentoDestino').value     = 0;
	$('cdLocalArmazenamentoDestinoView').value = '';
	$('cdLocalArmazenamentoOrigem').value  	   = 0;
	$('cdLocalArmazenamentoOrigemView').value  = '';
	$('tpAbastecimento').value  	           = 0;
	$('qtTransferencia').value  	           = "0,00";
	$('qtMinimaReabastecimento').value         = "0,00";
	$('qtMaximaReabastecimento').value  	   = "0,00";
	$('qtIdealReabastecimento').value  	   	   = "0,00";
	$('stEstoqueReabastecimento').checked  	   = false;
	alterFieldsStatus(true, reabastecimentoFields, "cdLocalArmazenamentoDestinoView");
	tabReabastecimento.showTab(1);
	isInsertReabastecimento = true;
	$('cdLocalArmazenamentoDestinoView').focus();
	clearFields(reabastecimentoFields);
}

function btnAlterReabastecimentoOnClick(content){
	alterFieldsStatus(true, reabastecimentoFields, "cdLocalArmazenamentoDestinoView");
	tabReabastecimento.showTab(1);
	isInsertReabastecimento = false;
}

var isInsertReabastecimento = true;

function btnSaveReabastecimentoOnClick(content){
	if(content==null)	{
		
		if(!$('cdLocalArmazenamentoDestino').value || $('cdLocalArmazenamentoDestino').value == 0){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Preencha o campo de Local de Armazenamento Destino!", msgboxType: "INFO"});
			return;
		}
		
		if(!$('cdLocalArmazenamentoOrigem').value || $('cdLocalArmazenamentoOrigem').value == 0){
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Preencha o campo de Local de Armazenamento Origem!", msgboxType: "INFO"});
			return;
		}
		
		var method = "save(new com.tivic.manager.alm.ProdutoEstoque(const " +  $('cdLocalArmazenamentoDestino').value + ":int, const "+$('cdProdutoServico').value+":int, " + 
					 "const "+$('cdEmpresa').value+":int, const 0:float, const "+changeLocale('qtIdealReabastecimento')+":float, const "+changeLocale('qtMinimaReabastecimento')+":float, const "+changeLocale('qtMaximaReabastecimento')+":float, const 0:int," + 
					 "const 0:float, const 0:float, const 0:int, const "+$('cdLocalArmazenamentoOrigem').value+":int, const "+$('tpAbastecimento').value+":int, const "+changeLocale('qtTransferencia')+":float, const "+($('stEstoqueReabastecimento').checked ? "1" : "0")+":int))";
		getPage('GET', 'btnSaveReabastecimentoOnClick', 
				'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
				'&method='+method, null);
	}
	else	{
		var result = null;
    	try {result = eval('(' + content + ')')} catch(e) {}
		if(result.code>0)	{
			loadReabastecimento(null);
			loadEstoque();
			showMsgbox('Manager', 300, 50, 'Reabastecimento cadastrado com sucesso!');
			alterFieldsStatus(false, reabastecimentoFields, "cdLocalArmazenamentoDestinoView");
		}
		else
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Erro ao salvar. Contate os desenvolvedores!", msgboxType: "INFO"});
			
	}
}

function btnDeleteReabastecimentoOnClick(content){
	if(content==null) {
		if (gridReabastecimento.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o reabastecimento que deseja excluir.');
		else {
			var cdLocalArmazenamento = gridReabastecimento.getSelectedRow().register['CD_LOCAL_ARMAZENAMENTO'];
			var cdProdutoServico = $('cdProdutoServico').value;
			var cdEmpresa = $('cdEmpresa').value;
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o reabastecimento selecionado?', 
							function() {
								getPage('GET', 'btnDeleteReabastecimentoOnClick', 
										'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
										'&method=remove(const ' + cdLocalArmazenamento + ':int, const ' + cdProdutoServico + ':int, const ' + cdEmpresa + ':int)');
							});
		}
	}
	else {
		var result = null;
    	try {result = eval('(' + content + ')')} catch(e) {}
		if(result.code>0)	{
			gridReabastecimento.removeSelectedRow();
			clearFields(reabastecimentoFields);
		}
		else
			showMsgbox('Manager', 300, 50, 'ERRO ao tentar excluir reabastecimento!');
	}
}

function btnFindCdLocalArmazenamentoDestinoOnClick(reg) {
	if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Locais de Armazenamento", 
												    width: 450,
												    height: 275,
												    top:40,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.alm.LocalArmazenamentoServices",
												    method: "find",
												    allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"nm_local_armazenamento", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"nm_local_armazenamento"}],
													  		      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    hiddenFields: [{reference:"A.cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER},
												                   {reference:"cdProdutoServico",value:$("cdProdutoServico").value, comparator:_EQUAL,datatype:_INTEGER}],
												    callback: btnFindCdLocalArmazenamentoDestinoOnClick, 
													autoExecuteOnEnter: true
										           });
    }
    else {// retorno
        filterWindow.close();
        $('cdLocalArmazenamentoDestino').value = reg[0]['CD_LOCAL_ARMAZENAMENTO'];
		$('cdLocalArmazenamentoDestinoView').value = reg[0]['NM_LOCAL_ARMAZENAMENTO'];
	}
}

function btnClearLocalArmazenamentoDestinoOnClick(){
	$('cdLocalArmazenamentoDestino').value = '';
	$('cdLocalArmazenamentoDestinoView').value = '';
}

function btnFindCdLocalArmazenamentoOrigemOnClick(reg) {
	if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Locais de Armazenamento", 
												    width: 450,
												    height: 275,
												    top:40,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.alm.LocalArmazenamentoServices",
												    method: "find",
												    allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"nm_local_armazenamento", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"nm_local_armazenamento"}],
													  		      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    hiddenFields: [{reference:"A.cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER},
												                   {reference:"cdProdutoServico",value:$("cdProdutoServico").value, comparator:_EQUAL,datatype:_INTEGER}],
												    callback: btnFindCdLocalArmazenamentoOrigemOnClick, 
													autoExecuteOnEnter: true
										           });
    }
    else {// retorno
        filterWindow.close();
        $('cdLocalArmazenamentoOrigem').value = reg[0]['CD_LOCAL_ARMAZENAMENTO'];
		$('cdLocalArmazenamentoOrigemView').value = reg[0]['NM_LOCAL_ARMAZENAMENTO'];
	}
}

function btnClearLocalArmazenamentoOrigemOnClick(){
	$('cdLocalArmazenamentoOrigem').value = '';
	$('cdLocalArmazenamentoOrigemView').value = '';
}

//*********************************************************************************************************************************************************************************

//***************************************************** ESTOQUE ********************************************************************************************

var columnsEstoque = [{label: 'Local de Estoque', reference: 'NM_LOCAL_ARMAZENAMENTO'},
                              {label: 'Estoque Atual', reference: 'QT_ESTOQUE', type: GridOne._CURRENCY},
				              {label: 'Estoque Mínimo', reference: 'QT_MINIMA', type: GridOne._CONTROL, controlWidth: '70px', style: 'width: 40px; text-align: right;', datatype: 'CURRENCY', onBlur: function(){onBlurQuantidadeMinima();}},
				              {label: 'Estoque Máximo', reference: 'QT_MAXIMA', type: GridOne._CONTROL, controlWidth: '70px', style: 'width: 40px; text-align: right;', datatype: 'CURRENCY', onBlur: function(){onBlurQuantidadeMaxima();}},
				              {label: 'Estoque Ideal', reference: 'QT_IDEAL', type: GridOne._CONTROL, controlWidth: '70px', style: 'width: 40px; text-align: right;', datatype: 'CURRENCY', onBlur: function(){onBlurQuantidadeIdeal();}}];

function loadEstoque(content) {
	if(content == null){
		var className = "com.tivic.manager.alm.ProdutoEstoqueServices";
		createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
		getPage("POST", "loadEstoque", "../methodcaller?className="+ className + 
						  "&method=getAllByProduto(const"+$('cdProdutoServico').value+":int, const "+<%=cdEmpresa%>+":int)");
	}
    else {// retorno
    	closeWindow('jLoadMsg');
    	var rsmEstoque = null;
    	try {rsmEstoque = eval('(' + content + ')')} catch(e) {}
    	gridEstoque = GridOne.create('gridEstoque', {columns: columnsEstoque, resultset: rsmEstoque, plotPlace : $('divGridEstoque'), 
													    	  noFocusOnSelect : true, 
															  noSelectOnCreate: false,
															  columnSeparator: true,
													          lineSeparator: false,
															  onDoubleClick:function() {
																
															   },
															   onProcessRegister: function(register) {
													 				var reg = register;
													 				if( <%=lgRelatorioEstilizado%> == 1 ){
																		reg['NM_LOCAL_ARMAZENAMENTO_cellStyle'] = "background-color:green;";
																		reg['QT_ESTOQUE_cellStyle'] = "background-color:green;";
																		reg['QT_MINIMA_cellStyle'] = "background-color:green;";
																		reg['QT_MAXIMA_cellStyle'] = "background-color:green;";
																		reg['QT_IDEAL_cellStyle'] = "background-color:green;";
																		if( reg['QT_ESTOQUE'] <= reg['QT_IDEAL'] ){
																			reg['NM_LOCAL_ARMAZENAMENTO_cellStyle'] = "background-color:#FF9700;";
																			reg['QT_ESTOQUE_cellStyle'] = "background-color:#FF9700;";
																			reg['QT_MINIMA_cellStyle'] = "background-color:#FF9700;";
																			reg['QT_MAXIMA_cellStyle'] = "background-color:#FF9700;";
																			reg['QT_IDEAL_cellStyle'] = "background-color:#FF9700;";
																		}
																		if( reg['QT_ESTOQUE'] == reg['QT_MINIMA']  ){
																			reg['NM_LOCAL_ARMAZENAMENTO_cellStyle'] = "background-color:#FF4500;";
																			reg['QT_ESTOQUE_cellStyle'] = "background-color:#FF4500;";
																			reg['QT_MINIMA_cellStyle'] = "background-color:#FF4500;";
																			reg['QT_MAXIMA_cellStyle'] = "background-color:#FF4500;";
																			reg['QT_IDEAL_cellStyle'] = "background-color:#FF4500;";
																		}
																		if( reg['QT_ESTOQUE'] < reg['QT_MINIMA']  ){
																			reg['NM_LOCAL_ARMAZENAMENTO_cellStyle'] = "background-color:red;";
																			reg['QT_ESTOQUE_cellStyle'] = "background-color:red;";
																			reg['QT_MINIMA_cellStyle'] = "background-color:red;";
																			reg['QT_MAXIMA_cellStyle'] = "background-color:red;";
																			reg['QT_IDEAL_cellStyle'] = "background-color:red;";
																		}
																			
																		
																	}		
																}});
    	
    }

}


function onBlurQuantidadeMinima(content){
	if(content == null){
		var objects="produtoEstoque=com.tivic.manager.alm.ProdutoEstoque();";
		var execute="produtoEstoque=com.tivic.manager.alm.ProdutoEstoqueDAO.get(const "+gridEstoque.getSelectedRowRegister().CD_LOCAL_ARMAZENAMENTO+" :int, const "+$('cdProdutoServico').value+" :int, const "+$('cdEmpresa').value+" :int);";
		execute+="produtoEstoque.setQtMinima(const "+gridEstoque.getSelectedRowRegister().QT_MINIMA+" :float);";
		getPage("GET", "onBlurQuantidadeMinima", 
				"../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueDAO"+
				 "&objects=" + objects +
				(execute!=""?"&execute=":"") + execute +
				"&method=update(*produtoEstoque:com.tivic.manager.alm.ProdutoEstoque)",null,true);
	}
	else{
		if(content < 1){
			createMsgbox("jMsg", {width: 300, height: 40, message: "Erro ao atualizar quantidade mínima.", 
				  caption: 'Manager', msgboxType: "INFO"});
		}
		else{
			loadEstoque();
		}
	}
}

function onBlurQuantidadeMaxima(content){
	if(content == null){
		var objects="produtoEstoque=com.tivic.manager.alm.ProdutoEstoque();";
		var execute="produtoEstoque=com.tivic.manager.alm.ProdutoEstoqueDAO.get(const "+gridEstoque.getSelectedRowRegister().CD_LOCAL_ARMAZENAMENTO+" :int, const "+$('cdProdutoServico').value+" :int, const "+$('cdEmpresa').value+" :int);";
		execute+="produtoEstoque.setQtMaxima(const "+gridEstoque.getSelectedRowRegister().QT_MAXIMA+" :float);";
		getPage("GET", "onBlurQuantidadeMaxima", 
				"../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueDAO"+
				 "&objects=" + objects +
				(execute!=""?"&execute=":"") + execute +
				"&method=update(*produtoEstoque:com.tivic.manager.alm.ProdutoEstoque)",null,true);
	}
	else{
		if(content < 1){
			createMsgbox("jMsg", {width: 300, height: 40, message: "Erro ao atualizar quantidade máxima.", 
				  caption: 'Manager', msgboxType: "INFO"});
		}
		else{
			loadEstoque();
		}
	}
}

function onBlurQuantidadeIdeal(content){
	if(content == null){
		var objects="produtoEstoque=com.tivic.manager.alm.ProdutoEstoque();";
		var execute="produtoEstoque=com.tivic.manager.alm.ProdutoEstoqueDAO.get(const "+gridEstoque.getSelectedRowRegister().CD_LOCAL_ARMAZENAMENTO+" :int, const "+$('cdProdutoServico').value+" :int, const "+$('cdEmpresa').value+" :int);";
		execute+="produtoEstoque.setQtIdeal(const "+gridEstoque.getSelectedRowRegister().QT_IDEAL+" :float);";
		getPage("GET", "onBlurQuantidadeIdeal", 
				"../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueDAO"+
				 "&objects=" + objects +
				(execute!=""?"&execute=":"") + execute +
				"&method=update(*produtoEstoque:com.tivic.manager.alm.ProdutoEstoque)",null,true);
	}
	else{
		if(content < 1){
			createMsgbox("jMsg", {width: 300, height: 40, message: "Erro ao atualizar quantidade ideal.", 
				  caption: 'Manager', msgboxType: "INFO"});
		}
		else{
			loadEstoque();
		}
	}
}

//*********************************************************************************************************************************************************************************


function btnNewOutrosCodigosOnClick(content){
	
}

function btnAlterOutrosCodigosOnClick(content){
	
}

function btnSaveOutrosCodigosOnClick(content){
	
}

function btnDeleteOutrosCodigosOnClick(content){
	
}

function btnFindTipoOnClick(reg, funcCallback, cdGrupoSuperior) {
//     if(!reg) {
//     	if(funcCallback==null)
//     		funcCallback = btnFindGrupoOnClick;	
// 		var hiddenFields = [];
// 		if(cdGrupoSuperior > 0)
// 			hiddenFields.push({reference:"A.cd_grupo_superior", value:cdGrupoSuperior, comparator:_EQUAL, datatype:_INTEGER});
// 		//
// 		FilterOne.create("jFiltro", {caption:"Pesquisar grupos de produtos", width: 550, height: 375, modal: true, noDrag: true,
// 									 className: "com.tivic.manager.alm.GrupoServices", method: "find", allowFindAll: true, autoExecuteOnEnter: true,
// 									 hiddenFields: hiddenFields,
// 								     filterFields: [[{label:"Nome", reference:"NM_GRUPO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
// 								     gridOptions: {columns: [{label:"Nome", reference:"DS_GRUPO"}], strippedLines: true, columnSeparator: false, lineSeparator: false},
// 								     callback: funcCallback});
//     }
//     else {
//         closeWindow("jFiltro");
// 		$('cdGrupo').value     = reg[0]['CD_GRUPO'];
// 		$('cdGrupoView').value = reg[0]['NM_GRUPO'];
//         if(isIdByGrupo) 
//         	getNextIdReduzido(null);	
        
//     }
}

/*********************************************************************************************************************************
 ********************************************* PESQUISA *************************************************************************
 *********************************************************************************************************************************/
var gridPesquisa;
var columnsPesquisa = [{label: 'Código', reference: 'ID_REDUZIDO'},
                      {label:  'Descrição', reference: 'NM_PRODUTO_SERVICO'},
                      {label:  'Estoque Atual', reference: 'QT_ESTOQUE'}];
                      
                      
function btnPrimeiroRegistro(){
	gridPesquisa.selectFirstLine();
	findProduto(null, gridPesquisa.getSelectedRowRegister()['CD_PRODUTO_SERVICO']);
}                      

function btnAnteriorRegistro(){
	gridPesquisa.selectPreviousLine();
	findProduto(null, gridPesquisa.getSelectedRowRegister()['CD_PRODUTO_SERVICO']);
}                      

function btnProximoRegistro(){
	gridPesquisa.selectNextLine();
	findProduto(null, gridPesquisa.getSelectedRowRegister()['CD_PRODUTO_SERVICO']);
}                      

function btnUltimoRegistro(){
	gridPesquisa.selectLastLine();
	findProduto(null, gridPesquisa.getSelectedRowRegister()['CD_PRODUTO_SERVICO']);
}                      
                      
                      
                      
function loadPesquisa(content) {
	if(content == null){
		var className = "com.tivic.manager.alm.ProdutoEstoqueServices";
		createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
		getPage("POST", "loadPesquisa", "../methodcaller?className="+ className + 
						  "&method=findProduto(const "+<%=cdEmpresa%>+":int, const 0:int, codigoInicial: int, codigoFinal: int, letraInicial: String, letraFinal: String, cdTipoPesquisa: int, quantidadePesquisa: int)", pesquisaFields, null, null, null);
	}
    else {// retorno
    	closeWindow('jLoadMsg');
    	var rsmPesquisa = null;
    	try {rsmPesquisa = eval('(' + content + ')')} catch(e) {}
		gridPesquisa = GridOne.create('gridPesquisa', {columns: columnsPesquisa, resultset: rsmPesquisa, plotPlace : $('divGridPesquisa'), 
			                                                   noHeader: false, noSelectorColumn: false, lineSeparator: false, columnSeparator: false,
															   onDoubleClick:function() {
																   findProduto(null, gridPesquisa.getSelectedRowRegister()['CD_PRODUTO_SERVICO']); 
													 		   },
															   onProcessRegister: function(register) {
																	 			var reg = register;
																	 		}});
		
		$('rodapeText').innerHTML = 'Produto';
		
	}

}

function findProduto(content, cdProdutoServico) {
	if(content == null){
		var className = "com.tivic.manager.alm.ProdutoEstoqueServices";
		createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
		getPage("POST", "findProduto", "../methodcaller?className="+ className + 
						  "&method=findProduto(const"+<%=cdEmpresa%>+":int, const 0:int, const"+cdProdutoServico+":int)", pesquisaFields, null, null, null);
	}
    else {// retorno
    	closeWindow("jLoadMsg");
        disabledFormProdutoServico=true;
        alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField2");
        var rsmPesquisa = null;
        try {rsmPesquisa = eval('(' + content + ')')} catch(e) {}
    	var reg = rsmPesquisa.lines[0];
    	$("dtCadastro").value = reg['DT_CADASTRO'];
        $('dtUltimaAlteracao').value = reg['DT_ULTIMA_ALTERACAO'];
        loadFormRegister(produtoServicoFields, reg);
        $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
		if ($('nmClassificacaoFiscalView') != null) {
			if (reg[0]['CD_CLASSIFICACAO_FISCAL']==null || reg['CD_CLASSIFICACAO_FISCAL']==0)
				$('nmClassificacaoFiscalView').value = '';
			else 
				$('nmClassificacaoFiscalView').value = (trim(reg['ID_CLASSIFICACAO_FISCAL'])!='' ? reg['ID_CLASSIFICACAO_FISCAL'] + ' - ' : '') + reg['NM_CLASSIFICACAO_FISCAL'];
		}
		$("cdEmpresa").value = $("cdEmpresa").getAttribute("defaultValue");
		$('rodapeText').innerHTML = reg['ID_REDUZIDO'] + " - " + reg['NM_PRODUTO_SERVICO'];
		/* CARREGUE OS GRIDS AQUI */
		setTimeout('loadAtributosProdutoServico()', 1);
// 		setTimeout('loadSimilares(); loadReferenciados(); loadPrecos(); loadFornecedores();', 1);
		setTimeout('loadEstoque(); loadSubstituto(); loadReabastecimento(); loadRelacionado(); loadPrecos(); loadFornecedores();', 1);
		clearReabastecimento();
		setTimeout('loadTributos()', 1);
		setTimeout('loadGrupos()', 1);
		setTimeout('loadNcm()', 1);
// 		setTimeout('loadSubstituto()', 1);
		setTimeout('loadCodigoBarras()', 1);
		setTimeout('loadOutrosCodigos()', 1);
// 		setTimeout('loadRelacionado()', 1);
		setTimeout('loadUltimoFornecedor()', 1);
		setTimeout('loadFotosAdicionais()', 1);
		setTimeout('loadCaracteristicas()', 1);
		setTimeout('initComponente()', 1);
		loadParametros(reg);
		<% if (lgFotoProduto == 1) { %>
			setTimeout('loadFotoProduto()', 10);
		<% } %>

        $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
		if (!$("idProdutoServico").disabled)
	        $("idProdutoServico").focus();
		
		$('imageProduto').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.ProdutoServicoEmpresaServices&method=getBytes(const '+<%=cdEmpresa%>+':int, const ' + reg['CD_PRODUTO_SERVICO'] + ':int)&idSession=imgProduto_' + reg['CD_PRODUTO_SERVICO'];
		tabProdutoServico.showTab(1);
		tabDadosComplementares.showTab(0);
	}

}

function loadParametros(reg){
	if(reg['LG_VERIFICAR_ESTOQUE_NA_VENDA']==1){
    	$('lgVerificarEstoqueNaVenda').checked = true;
    }
	else{
		$('lgVerificarEstoqueNaVenda').checked = false;
	}
	if(reg['LG_BLOQUEIA_VENDA']==1){
    	$('lgBloqueiaVenda').checked = true;
    }
	else{
		$('lgBloqueiaVenda').checked = false;
	}
	if(reg['LG_PERMITE_DESCONTO']==1){
    	$('lgPermiteDesconto').checked = true;
    }
	else{
		$('lgPermiteDesconto').checked = false;
	}
	if(reg['LG_FAZ_ENTREGA']==1){
    	$('lgFazEntrega').checked = true;
    }
	else{
		$('lgFazEntrega').checked = false;
	}
	if(reg['LG_NAO_CONTROLA_ESTOQUE']==1){
    	$('lgNaoControlaEstoque').checked = true;
    }
	else{
		$('lgNaoControlaEstoque').checked = false;
	}
	if(reg['LG_IMPRIME_NA_TABELA_PRECO']==1){
    	$('lgImprimeNaTabelaPreco').checked = true;
    }
	else{
		$('lgImprimeNaTabelaPreco').checked = false;
	}
	if(reg['LG_PRODUTO_USO_CONSUMO']==1){
    	$('lgProdutoUsoConsumo').checked = true;
    }
	else{
		$('lgProdutoUsoConsumo').checked = false;
	}
	
}

/*********************************************************************************************************************************
 ********************************************* Substituto *************************************************************************
 *********************************************************************************************************************************/
 function loadSubstituto(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		getPage("GET", "loadSubstituto", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
				"&method=getAllSimilares(const " + $('cdProdutoServico').value + ":int, const 0: int)");
	}
	else {
		var rsmSubstituto = null;
 		try {rsmSubstituto = eval('(' + content + ')')} catch(e) {}
 		gridSubstituto = GridOne.create('gridSubstituto', {columns: [{label: 'Nome', reference: 'NM_PRODUTO_SIMILAR'},
 		                                                             {label: 'Produto', reference: 'NM_PRODUTO_SERVICO'},
 			                                                         {label: 'Descrição', reference: 'TXT_DESCRICAO'},
 		                                                             {label: 'Código de Barras', reference: 'ID_PRODUTO_SERVICO'},
 		                                                             {label: 'Referência', reference: 'NR_REFERENCIA'},
 		                                                             {label: 'Tamanho', reference: 'TXT_DADO_TECNICO'},
 		                                                         	 {label: 'Cor', reference: 'TXT_ESPECIFICACAO'}],
                                                         onDoubleClick:function() {
                                                           	findProdutoSubstituto(null, gridSubstituto.getSelectedRowRegister()['CD_PRODUTO_SERVICO_SIMILAR']); 
											 		     },          
					                                     resultset: rsmSubstituto, plotPlace: $('divGridSubstituto')});
	}
}

function findProdutoSubstituto(content, cdProdutoServico) {
	if(content == null){
		var className = "com.tivic.manager.grl.ProdutoServicoServices";
		createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
		getPage("POST", "findProdutoSubstituto", "../methodcaller?className="+ className + 
						  "&method=getSubstituto(const "+$('cdProdutoServico').value+":int, const "+cdProdutoServico+":int)", null, null, null, null);
	}
    else {// retorno
    	closeWindow("jLoadMsg");
        //disabledFormProdutoServico=true;
        alterFieldsStatus(false, substitutoFields, "nmProdutoServicoSubstituto", "disabledField2");
        var rsmSubstituto = null;
        try {rsmSubstituto = eval('(' + content + ')')} catch(e) {}
    	var reg = rsmSubstituto.lines[0];
    	$('cdProdutoServicoSubstituto').value     = reg['CD_PRODUTO_SERVICO'];
    	$('cdProdutoServicoSubstitutoView').value = reg['NM_PRODUTO_SERVICO'];
    	$('nmProdutoSimilarSubstituto').value     = reg['NM_PRODUTO_SIMILAR'];
    	$('txtDescricaoSubstituto').value    	  = reg['TXT_DESCRICAO'];
    	$('txtEspecificacaoSubstituto').value     = reg['TXT_ESPECIFICACAO'];
		
    	$("nmProdutoSimilarSubstituto").focus();
    	tabSubstituto.showTab(1);
	}

}

function btnNewSubstitutoOnClick(content){
	$('cdProdutoServicoSubstituto').value  	   = '';
	$('cdProdutoServicoSubstitutoView').value  = '';
	$('nmProdutoSimilarSubstituto').value  	   = '';
	$('txtDescricaoSubstituto').value      	   = '';
	$('txtEspecificacaoSubstituto').value  	   = '';
	alterFieldsStatus(true, substitutoFields, "nmProdutoSimilarSubstituto");
	tabSubstituto.showTab(1);
	isInsertSubstituto = true;
	$('nmProdutoSimilarSubstituto').focus();
}

function btnAlterSubstitutoOnClick(content){
	alterFieldsStatus(true, substitutoFields, "nmProdutoSimilarSubstituto");
	tabSubstituto.showTab(1);
	isInsertSubstituto = false;
}

var isInsertSubstituto = true;

function btnSaveSubstitutoOnClick(content)	{
	if(content==null)	{
		var method = (isInsertSubstituto ? "insert" : "update") + "(new com.tivic.manager.grl.ProdutoSimilar(const " +  $('cdProdutoServico').value + ":int, const "+$('cdProdutoServicoSubstituto').value+":int, " + 
					"const 0:int, const "+$('nmProdutoSimilarSubstituto').value.toUpperCase()+":String, const "+$('txtDescricaoSubstituto').value.toUpperCase()+":String, const "+$('txtEspecificacaoSubstituto').value+":String))";
		getPage('GET', 'btnSaveSubstitutoOnClick', 
				'../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO'+
				'&method='+method, null);
	}
	else	{
		if(content>0)	{
			loadSubstituto(null);
			closeWindow('jSubstituto');
			showMsgbox('Manager', 300, 50, 'Substituto cadastrada com sucesso!');
			loadSubstituto(null);
			alterFieldsStatus(false, substitutoFields, "nmProdutoSimilarSubstituto");
		}
		else
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Erro ao salvar. Contate os desenvolvedores!", msgboxType: "INFO"});
			
	}
}

function btnDeleteSubstitutoOnClick(content){
	if(content==null) {
		if (gridSubstituto.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o produto substituto que deseja excluir.');
		else {
			var cdSimilar = (gridSubstituto.getSelectedRow().register['CD_SIMILAR'] != null) ? gridSubstituto.getSelectedRow().register['CD_PRODUTO_SERVICO_SIMILAR'] : gridSubstituto.getSelectedRow().register['CD_PRODUTO_SERVICO'];
			var nmSimilar = gridSubstituto.getSelectedRow().register['NM_PRODUTO_SERVICO'];
			var cdProdutoServico = $('cdProdutoServico').value;
			var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		    var executionDescription = "Exclusão do substituto " + nmSimilar + " (Cód. " + cdSimilar + ") da relação de substitutos do produto " + produtoServicoDescription;	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o produto substituto selecionado?', 
							function() {
								getPage('GET', 'btnDeleteSubstitutoOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO'+
										'&method=delete(const ' + cdProdutoServico + ':int, const ' + cdSimilar + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridSubstituto.removeSelectedRow();
			btnNewSubstitutoOnClick();
		}
		else
			showMsgbox('Manager', 300, 50, 'ERRO ao tentar excluir produto similar!');
	}
	
}

function btnFindSubstitutoOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro", {caption:"Pesquisar produtos e serviços", width: 800, height: 400, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices", method: "findProdutosOfEmpresa", allowFindAll: true,
					   filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
					   				  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:35, charcase:'uppercase'},
									   {label:"ID/código", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:25, charcase:'uppercase'},
									   {label:"ID Reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
									   {label:"Referência", reference:"nr_referencia", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'}]],
					   gridOptions: {columns: [{label:"Referência", reference:"NR_REFERENCIA"},
					                           {label:"Nome", reference:"CL_NOME"},
					   						   {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
											   <%=cdTipoOperacaoVarejo>0 ?"{label:\"Preço Varejo\", reference:\"vl_preco_"+cdTipoOperacaoVarejo+"\", type: GridOne._CURRENCY},":""%>
											   <%=cdTipoOperacaoAtacado>0?"{label:\"Preço Atacado\", reference:\"vl_preco_"+cdTipoOperacaoAtacado+"\", type: GridOne._CURRENCY},":""%>		                   	 
											   {label:"Fabricante", reference:"nm_fabricante"},
											   {label:"ID/cód. reduzido", reference:"id_reduzido"}],
						             onProcessRegister: function(reg) {
							 				// Fabricante
					 						reg['CL_FABRICANTE'] = reg['SG_FABRICANTE']!=null && reg['SG_FABRICANTE']!='' ? reg['SG_FABRICANTE'] : reg['NM_FABRICANTE'];
					 						if(reg['CL_FABRICANTE'] == reg['NM_FABRICANTE'] && reg['NM_FABRICANTE']!=null)	{
					 							if(reg['NM_FABRICANTE'].indexOf('-')>0)
					 								reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf('-')));
					 							else if(reg['NM_FABRICANTE'].indexOf(' ')>0)
					 								reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf(' ')));
					 						}  
											//	
							 				reg['CL_NOME'] = reg['NM_PRODUTO_SERVICO'];
											// Cor
											if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
												reg['CL_NOME'] += ' - '+reg['TXT_ESPECIFICACAO'].toUpperCase();
											// Tamanho
											if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
												reg['CL_NOME'] += ' - '+reg['TXT_DADO_TECNICO'].toUpperCase();
						           },												
								   lineAction: function() {
									   btnFindSubstitutoOnClick([this.register]);
								   },
								   strippedLines: true, columnSeparator: false, lineSeparator: false},
					   hiddenFields: [{reference:"TP_PRODUTO_SERVICO",value:0, comparator:_EQUAL,datatype:_INTEGER},
					   				  {reference:"A.cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER}],
					   callback: btnFindSubstitutoOnClick, 
					   autoExecuteOnEnter: true});
    }
    else {// retorno
        closeWindow("jFiltro");
        $('cdProdutoServicoSubstituto').value  	   = reg[0]['CD_PRODUTO_SERVICO'];
        $('cdProdutoServicoSubstitutoView').value  = reg[0]['CL_NOME'];
	}
}

function btnClearSubstitutoOnClick() {
	$('cdProdutoServicoSubstituto').value    = 0;
	$('cdProdutoServicoSubstitutoView').value    = '';
}

var tamanhos = new StringTokenizer("<%=ParametroServices.getValorOfParametro("DS_GRADE", "PP,P,M,G,XG,XXG", cdEmpresa, null)%>", ",").getTokens();
function btnGradeOnClick()	{
	if ($('cdProdutoServico').value <= 0)	{
		showMsgbox('Manager', 300, 50, 'Nenhum produto carregado...');
		return;
	}
	if ($('txtDadoTecnico').value == '')	{
		showMsgbox('Manager', 300, 50, 'O tamanho do produto atual não foi informado, ou seja, ele não faz parte de uma grade.');
		return;
	}
	if (gridSimilares!=null && gridSimilares.size()>0)	{
		showMsgbox('Manager', 300, 50, 'A grade desse produto já foi cadastrada!');
		return;
	}
	
	var linhas    = [];
	var isOnGrade = 0;
	for(var i=0; i<tamanhos.length; i++)	{
		isOnGrade = ($('txtDadoTecnico').value.toUpperCase()==tamanhos[i].toUpperCase()) || isOnGrade==1 ? 1 : 0;
		
		if(($('txtDadoTecnico').value+'').toUpperCase().trim()==(tamanhos[i]+'').toUpperCase().trim())
			continue;
			
		linhas.push([{id:'txtDadoTecnico_G'+i, type:'text', reference: '', label:'Tamanho', width:20, value: tamanhos[i]},
		             {id:'txtEspecificacao_G'+i, type:'text', reference: '', label:'Cor', width:20, value: ($('txtEspecificacao').value+'').toUpperCase()},
		             {id:'idProdutoServico_G'+i, type:'text', reference: '', label:'Código de Barras (GTIN/NGIC)', width:35},
			  		 {id:'nrReferencia_G'+i, type:'text', reference: '', label:'Referência', width:25}]);
	}
	linhas.push([{type: 'space', width: 60},
				 {id:'btnSaveGrade', type:'button', label:'Cadastrar', width: 20, height:19, image: '/sol/imagens/form-btSalvar16.gif', 
				   	onClick: function(){ btnSaveGradeSubstitutoOnClick(null); }
	             },
				 {id:'btnCancelarGrade', type:'button', label:'Cancelar', width:20, height:19, image: '/sol/imagens/cancel_13.gif', 
				  onClick: function(){ closeWindow('jGradeSubstituto'); }
				 }]);	
	FormFactory.createFormWindow('jGradeSubstituto', {caption: "Cadastrando grade do produto", width: 500, height: ((tamanhos.length-isOnGrade)*30)+54, unitSize: '%', modal: true,
						                      id: 'produtoSimilar', loadForm: true, noDrag: true,
						                      lines: linhas, focusField:'idProdutoServico0'});
}
var flag = 1; //trata da tentativa de cadastrar uma grade mais de uma vez
function btnSaveGradeSubstitutoOnClick(content)	{
	if(content==null && flag==1)	{
		var objects = "grade=java.util.ArrayList();";
		var execute = "";
		for(var i=0; i<tamanhos.length; i++)	{
			if(($('txtDadoTecnico').value+'').toUpperCase().trim()==(tamanhos[i]+'').toUpperCase().trim())
				continue;
			if(($("idProdutoServico_G"+i).value+'').trim()=='' && ($("nrReferencia_G"+i).value+'').trim()=='')
				continue;
			var grade = $("txtDadoTecnico_G"+i).value+" |"+$("txtEspecificacao_G"+i).value+" |"+$("idProdutoServico_G"+i).value+" |"+$("nrReferencia_G"+i).value+" |";
			execute += "grade.add(const "+grade+":Object);";
		}
		if(execute=='') {
			flag=1;
			showMsgbox('Manager', 300, 50, 'Nenhuma referência ou código de barras foi informado!');
			return;
		}
		getPage('GET', 'btnSaveGradeSubstitutoOnClick', 
				'../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices'+
				'&execute='+execute+
				'&objects='+objects+
				'&method=insertGrade(const ' +  $('cdProdutoServico').value + ':int, const <%=cdEmpresa%>:int,*grade:java.util.ArrayList)', null);
		flag=-1;
	}
	else	{
		var ret = processResult(content, '');
		if(ret!= null && ret != '' && ret.code>0)	{
			loadSimilares(null);
			closeWindow('jGradeSubstituto');
			showMsgbox('Manager', 300, 50, 'Grade cadastrada com sucesso!');
		}
		else
			flag=1;
		
			
	}
}

/*********************************************************************************************************************************
 ********************************************* Relacionado *************************************************************************
 *********************************************************************************************************************************/
function loadRelacionado(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		getPage("GET", "loadRelacionado", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
				"&method=getAllSimilares(const " + $('cdProdutoServico').value + ":int, const 1: int)");
	}
	else {
		var rsmRelacionado = null;
 		try {rsmRelacionado = eval('(' + content + ')')} catch(e) {}
 		gridRelacionado = GridOne.create('gridRelacionado', {columns: [{label: 'Nome', reference: 'NM_PRODUTO_SIMILAR'},
 		                                                             {label: 'Produto', reference: 'NM_PRODUTO_SERVICO'},
 			                                                         {label: 'Descrição', reference: 'TXT_DESCRICAO'}],
                                                         onDoubleClick:function() {
                                                           	findProdutoRelacionado(null, gridRelacionado.getSelectedRowRegister()['CD_PRODUTO_SERVICO_SIMILAR']); 
											 		     },          
					                                     resultset: rsmRelacionado, plotPlace: $('divGridRelacionado')});
	}
}

function findProdutoRelacionado(content, cdProdutoServico) {
	if(content == null){
		var className = "com.tivic.manager.grl.ProdutoServicoServices";
		createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
		getPage("POST", "findProdutoRelacionado", "../methodcaller?className="+ className + 
						  "&method=getRelacionado(const "+$('cdProdutoServico').value+":int, const "+cdProdutoServico+":int)", null, null, null, null);
	}
    else {// retorno
    	closeWindow("jLoadMsg");
        //disabledFormProdutoServico=true;
        alterFieldsStatus(false, relacionadoFields, "nmProdutoServicoRelacionado", "disabledField2");
        var rsmPesquisa = null;
        try {rsmPesquisa = eval('(' + content + ')')} catch(e) {}
    	var reg = rsmPesquisa.lines[0];
    	$('cdProdutoServicoRelacionado').value     = reg['CD_PRODUTO_SERVICO'];
    	$('cdProdutoServicoRelacionadoView').value = reg['NM_PRODUTO_SERVICO'];
    	$('nmProdutoSimilarRelacionado').value     = reg['NM_PRODUTO_SIMILAR'];
    	$('txtDescricaoRelacionado').value    	  = reg['TXT_DESCRICAO'];
    	$('txtEspecificacaoRelacionado').value     = reg['TXT_ESPECIFICACAO'];
		
    	$("nmProdutoSimilarRelacionado").focus();
    	tabRelacionado.showTab(1);
	}

}

function btnNewRelacionadoOnClick(content){
	$('cdProdutoServicoRelacionado').value  	   = '';
	$('cdProdutoServicoRelacionadoView').value  = '';
	$('nmProdutoSimilarRelacionado').value  	   = '';
	$('txtDescricaoRelacionado').value      	   = '';
	$('txtEspecificacaoRelacionado').value  	   = '';
	alterFieldsStatus(true, relacionadoFields, "nmProdutoSimilarRelacionado");
	tabRelacionado.showTab(1);
	isInsertRelacionado = true;
	$('nmProdutoSimilarRelacionado').focus();
}

function btnAlterRelacionadoOnClick(content){
	alterFieldsStatus(true, relacionadoFields, "nmProdutoSimilarRelacionado");
	tabRelacionado.showTab(1);
	isInsertRelacionado = false;
}

var isInsertRelacionado = true;

function btnSaveRelacionadoOnClick(content)	{
	if(content==null)	{
		var method = (isInsertRelacionado ? "insert" : "update") + "(new com.tivic.manager.grl.ProdutoSimilar(const " +  $('cdProdutoServico').value + ":int, const "+$('cdProdutoServicoRelacionado').value+":int, " + 
					"const 1:int, const "+$('nmProdutoSimilarRelacionado').value.toUpperCase()+":String, const "+$('txtDescricaoRelacionado').value.toUpperCase()+":String, const "+$('txtEspecificacaoRelacionado').value+":String))";
		getPage('GET', 'btnSaveRelacionadoOnClick', 
				'../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO'+
				'&method='+method, null);
	}
	else	{
		if(content>0)	{
			loadRelacionado(null);
			closeWindow('jRelacionado');
			showMsgbox('Manager', 300, 50, 'Relacionado cadastrada com sucesso!');
			alterFieldsStatus(false, relacionadoFields, "nmProdutoSimilarRelacionado");
		}
		else
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Erro ao salvar. Contate os desenvolvedores!", msgboxType: "INFO"});
			
	}
}

function btnDeleteRelacionadoOnClick(content){
	if(content==null) {
		if (gridRelacionado.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o produto relacionado que deseja excluir.');
		else {
			var cdSimilar = (gridRelacionado.getSelectedRow().register['CD_SIMILAR'] != null) ? gridRelacionado.getSelectedRow().register['CD_PRODUTO_SERVICO_SIMILAR'] : gridRelacionado.getSelectedRow().register['CD_PRODUTO_SERVICO'];
			var nmSimilar = gridRelacionado.getSelectedRow().register['NM_PRODUTO_SERVICO'];
			var cdProdutoServico = $('cdProdutoServico').value;
			var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		    var executionDescription = "Exclusão do Relacionado " + nmSimilar + " (Cód. " + cdSimilar + ") da relação de Relacionados do produto " + produtoServicoDescription;	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o produto relacionado selecionado?', 
							function() {
								getPage('GET', 'btnDeleteRelacionadoOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO'+
										'&method=delete(const ' + cdProdutoServico + ':int, const ' + cdSimilar + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridRelacionado.removeSelectedRow();
			btnNewRelacionadoOnClick();
		}
		else
			showMsgbox('Manager', 300, 50, 'ERRO ao tentar excluir produto similar!');
	}
	
}

function btnFindRelacionadoOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro", {caption:"Pesquisar produtos e serviços", width: 800, height: 400, modal: true, noDrag: true,
									 className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices", method: "findProdutosOfEmpresa", allowFindAll: true,
					   filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
					   				  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:35, charcase:'uppercase'},
									   {label:"ID/código", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:25, charcase:'uppercase'},
									   {label:"ID Reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
									   {label:"Referência", reference:"nr_referencia", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'}]],
					   gridOptions: {columns: [{label:"Referência", reference:"NR_REFERENCIA"},
					                           {label:"Nome", reference:"CL_NOME"},
					   						   {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
											   <%=cdTipoOperacaoVarejo>0 ?"{label:\"Preço Varejo\", reference:\"vl_preco_"+cdTipoOperacaoVarejo+"\", type: GridOne._CURRENCY},":""%>
											   <%=cdTipoOperacaoAtacado>0?"{label:\"Preço Atacado\", reference:\"vl_preco_"+cdTipoOperacaoAtacado+"\", type: GridOne._CURRENCY},":""%>		                   	 
											   {label:"Fabricante", reference:"nm_fabricante"},
											   {label:"ID/cód. reduzido", reference:"id_reduzido"}],
						             onProcessRegister: function(reg) {
							 				// Fabricante
					 						reg['CL_FABRICANTE'] = reg['SG_FABRICANTE']!=null && reg['SG_FABRICANTE']!='' ? reg['SG_FABRICANTE'] : reg['NM_FABRICANTE'];
					 						if(reg['CL_FABRICANTE'] == reg['NM_FABRICANTE'] && reg['NM_FABRICANTE']!=null)	{
					 							if(reg['NM_FABRICANTE'].indexOf('-')>0)
					 								reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf('-')));
					 							else if(reg['NM_FABRICANTE'].indexOf(' ')>0)
					 								reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf(' ')));
					 						}  
											//	
							 				reg['CL_NOME'] = reg['NM_PRODUTO_SERVICO'];
											// Cor
											if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
												reg['CL_NOME'] += ' - '+reg['TXT_ESPECIFICACAO'].toUpperCase();
											// Tamanho
											if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
												reg['CL_NOME'] += ' - '+reg['TXT_DADO_TECNICO'].toUpperCase();
						           },												
								   lineAction: function() {
									   btnFindRelacionadoOnClick([this.register]);
								   },
								   strippedLines: true, columnSeparator: false, lineSeparator: false},
					   hiddenFields: [{reference:"TP_PRODUTO_SERVICO",value:0, comparator:_EQUAL,datatype:_INTEGER},
					   				  {reference:"A.cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER}],
					   callback: btnFindRelacionadoOnClick, 
					   autoExecuteOnEnter: true});
    }
    else {// retorno
        closeWindow("jFiltro");
        $('cdProdutoServicoRelacionado').value  	   = reg[0]['CD_PRODUTO_SERVICO'];
        $('cdProdutoServicoRelacionadoView').value  = reg[0]['CL_NOME'];
	}
}

function btnClearRelacionadoOnClick() {
	$('cdProdutoServicoRelacionado').value    = 0;
	$('cdProdutoServicoRelacionadoView').value    = '';
}

/*********************************************************************************************************************************
 ******************************************************** PRECOS *****************************************************************
 *********************************************************************************************************************************/
function validatePreco(content) {
    if($("cdTabelaPreco").value == '0') {
		showMsgbox('Manager', 300, 50, 'Selecione a Tabela de Preço', function() {$("cdTabelaPreco").focus()});
        return false;
	}
    else
		return true;
}

function btnSavePrecoOnClick(content) {
	if (content==null) {
		if ($('cdProdutoServico').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize um produto para lançar preços.');
		else if (validatePreco()) {
			$('btnSavePreco').disabled = true;
			var produtoServicoDescription = ' (Produto ' + $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value + ")";
            var executionDescription = $('cdTabelaPrecoOld').value>0 ? formatDescriptionUpdate("COnfiguração de Preço", $("cdTabelaPrecoOld").value + " " + produtoServicoDescription, $("dataOldPreco").value, precoFields) : formatDescriptionInsert("Configuração de Preco " + produtoServicoDescription, precoFields);
            var construtor = 'cdTabelaPreco:int, cdProdutoServico:int, cdProdutoServicoPreco:int, dtTerminoValidade:GregorianCalendar,const '+changeLocale('vlPreco')+':float';
			var methodName = gridPrecos.getSelectedRow()==null ? 
							'insert(new com.tivic.manager.adm.ProdutoServicoPreco(' + construtor + '):com.tivic.manager.adm.ProdutoServicoPreco)' :
							'update(new com.tivic.manager.adm.ProdutoServicoPreco(' + construtor + '):com.tivic.manager.adm.ProdutoServicoPreco)';
			getPage('POST', 'btnSavePrecoOnClick', 
					'../methodcaller?className=com.tivic.manager.adm.ProdutoServicoPrecoServices'+
					'&method=' + methodName +
					'&cdProdutoServico=' + $('cdProdutoServico').value,
					precoFields, null, null, executionDescription);
		}
	}
	else {
		$('btnSavePreco').disabled = false;
		closeWindow('jPreco');
		if (isInteger(content) && parseInt(content, 10) > 0) {
			var precoRegister = loadRegisterFromForm(precoFields);
			precoRegister['NM_TABELA_PRECO'] = $('cdTabelaPreco').options[$('cdTabelaPreco').selectedIndex].text;
			$('cdTabelaPrecoOld').value = $('cdTabelaPreco').value;
			$("dataOldPreco").value = captureValuesOfFields(precoFields);
			loadPrecos(null);
		}
		else
			showMsgbox('Manager', 300, 100, 'Erros reportados ao configurar preço para o Produto. Certifique-se que o preço ainda não esteja configurado para a Tabela selecionada.');
	}
}

function onClickPreco() {
	if (this!=null) {
		var preco = this.register;
		loadFormRegister(preco);
		$("dataOldPreco").value = captureValuesOfFields(precoFields);
	}
}

var columnsPreco = [{label:'Tabela de Preço', reference:'NM_TABELA_PRECO'}, 
					{label:'Preço', reference:'CL_PRECO', style:'text-align:right;'}];

function loadPrecos(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		getPage("GET", "loadPrecos", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
				"&method=getAllPrecos(const " + $('cdProdutoServico').value + ":int, " +
				"const " + $('cdEmpresa').value + ":int)");
	}
	else {
		var rsmPrecos = null;
		try {rsmPrecos = eval('(' + content + ')')} catch(e) {}
		gridPrecos = GridOne.create('gridPrecos', {columns: columnsPreco,
					     resultset :rsmPrecos, 
					     plotPlace : $('divGridPrecos'),
					     onDoubleClick:function() {
						   	 btnFindProdutoServicoPrecoOnClick([gridPrecos.getSelectedRowRegister()]);
					     },
					     onProcessRegister: function(reg){
					    	 reg['CL_PRECO'] = formatCurrency(reg['VL_PRECO']);
					     },
					     onSelect : onClickPreco});
	}
}


function btnFindProdutoServicoPrecoOnClick(reg){
	if ($('cdProdutoServico').value==0){
        createMsgbox("jMsg", {width: 250,  height: 100,
                              message: "Carregue um produto para poder buscar uma tabela de preço.", msgboxType: "INFO"});
        return;
    }
	if(!reg){
		var filters =  [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]];
		var columnsGrid = [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
		                   {label:"Preço", reference:"VL_PRECO", type: GridOne._CURRENCY},
		                   {label:"Custo", reference:"VL_ULTIMO_CUSTO", type: GridOne._CURRENCY}];
		FilterOne.create("jFiltro", {caption:"Pesquisar produtos ", width: 700, height: 400, modal: true, noDrag: true,
									   className: "com.tivic.manager.adm.ProdutoServicoPrecoServices", 
									   method: "findCompleto",
									   filterFields: filters,
									   gridOptions: {columns: columnsGrid, strippedLines: true, columnSeparator: false, lineSeparator: false, 
										             onProcessRegister: function(reg) {
											 				
										             }},
									   hiddenFields: [{reference:"B.cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER},
									                  {reference:"A.cd_tabela_preco",value:$('cdTabelaPreco').value, comparator:_EQUAL,datatype:_INTEGER},
									   				  {reference:"A.dt_termino_validade",value:null, comparator:_ISNULL,datatype:_TIMESTAMP}],
									   callback: btnFindProdutoServicoPrecoOnClick, 
									   autoExecuteOnEnter: true});
    }
    else {// retorno
    	closeWindow("jFiltro");
    	loadFormRegister(precoFields, reg[0]);
    	$('vlCustoProduto').value = formatCurrency(reg[0]['VL_ULTIMO_CUSTO']);
    	if(changeLocale('vlCustoProduto') == 0){
    		$('prMargemPrecoCustoProduto').disabled = "disabled";
    		$('prMargemPrecoCustoProduto').static   = "true";
    		$('prMargemPrecoCustoProduto').className= "disabledField";
    		
    		$('vlMargemPrecoCustoProduto').disabled = "disabled";
    		$('vlMargemPrecoCustoProduto').static   = "true";
    		$('vlMargemPrecoCustoProduto').className= "disabledField";
    		
    		$('prMargemPrecoPadrao').disabled = "disabled";
    		$('prMargemPrecoPadrao').static   = "true";
    		$('prMargemPrecoPadrao').className= "disabledField";
    		
    		$('vlMargemPrecoPadrao').disabled = "disabled";
    		$('vlMargemPrecoPadrao').static   = "true";
    		$('vlMargemPrecoPadrao').className= "disabledField";
    		
    		$('prMargemPrecoMaximo').disabled = "disabled";
    		$('prMargemPrecoMaximo').static   = "true";
    		$('prMargemPrecoMaximo').className= "disabledField";
    		
    		$('vlMargemPrecoMaximo').disabled = "disabled";
    		$('vlMargemPrecoMaximo').static   = "true";
    		$('vlMargemPrecoMaximo').className= "disabledField";
    		
    		$('prMargemPrecoMinimo').disabled = "disabled";
    		$('prMargemPrecoMinimo').static   = "true";
    		$('prMargemPrecoMinimo').className= "disabledField";
    		
    		$('vlMargemPrecoMinimo').disabled = "disabled";
    		$('vlMargemPrecoMinimo').static   = "true";
    		$('vlMargemPrecoMinimo').className= "disabledField";
    		
    	}
    	else{
    		calcularMargem("CustoProduto");
    		calcularMargem("Padrao");
    		calcularMargem("Maximo");
    		calcularMargem("Minimo");
    	}
    	alterFieldsStatus(false, precoFields);
    }
}


function calcularMargem(tpPreco){
	if(changeLocale('vlPreco' + tpPreco) > 0){
		$('vlMargemPreco' + tpPreco).value = formatCurrency(changeLocale('vlPreco' + tpPreco) - changeLocale('vlCustoProduto'));
		$('prMargemPreco' + tpPreco).value = formatCurrency(changeLocale('vlMargemPreco' + tpPreco) / changeLocale('vlCustoProduto') * 100);
	}
	else{
		if($('vlMargemPreco' + tpPreco))
			$('vlMargemPreco' + tpPreco).value = "0,00";
		if($('prMargemPreco' + tpPreco))
			$('prMargemPreco' + tpPreco).value = "0,00";
	}
}

function calcularPrecoPelaPorcentagem(tpPreco){
	var vlCustoProduto = parseFloat(changeLocale('vlCustoProduto'));
	var prMargemPreco  = parseFloat(changeLocale('prMargemPreco' + tpPreco));
	if(vlCustoProduto > 0 && prMargemPreco > 0){
		$('vlPreco' + tpPreco).value = formatCurrency(vlCustoProduto + (vlCustoProduto * prMargemPreco / 100));
		var vlPreco        = parseFloat(changeLocale('vlPreco' + tpPreco));
		$('vlMargemPreco' + tpPreco).value = formatCurrency(vlPreco - vlCustoProduto);
	}
}

function calcularPrecoPeloValor(tpPreco){
	var vlCustoProduto = parseFloat(changeLocale('vlCustoProduto'));
	var vlMargemPreco  = parseFloat(changeLocale('vlMargemPreco' + tpPreco));
	if(vlCustoProduto > 0 && vlMargemPreco > 0){
		$('vlPreco' + tpPreco).value = formatCurrency(vlCustoProduto + vlMargemPreco);
		$('prMargemPreco' + tpPreco).value = formatCurrency(vlMargemPreco / vlCustoProduto * 100);
	}
}

var disabledFormProdutoPreco = false; 
//ProdutoPreco
function btnNewPrecoOnClick(){
	if ($('cdProdutoServico').value == '0'){
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma produto para lançar preços.');
		return;
	}
	disabledFormProdutoPreco = false;
	clearFields(precoFields);
  	$('vlCustoProduto').value = "";
  	alterFieldsStatus(true, precoFields, "vlPrecoPadrao");
  	$('cdTabelaPreco').className = 'select';
	$('cdTabelaPreco').disabled  = false;
  	$('cdTabelaPreco').focus();
}

function btnAlterPrecoOnClick(){
	if (gridPrecos.getSelectedRow()==null){
		showMsgbox('Manager', 300, 50, 'Selecione o Preço que você deseja alterar.');
		return;	
	}
	disabledFormProdutoPreco = false;
  	alterFieldsStatus(true, precoFields, "vlPrecoPadrao");
  	$('cdTabelaPreco').className = 'disabledSelect';
	$('cdTabelaPreco').disabled = true;
	loadFormRegister(precoFields, gridPrecos.getSelectedRow().register, true);
	$('vlPreco').value = (gridPrecos.getSelectedRow().register['VL_PRECO']+'').replace('.',',');
	$('vlPreco').focus();
}

function btnSalvarPrecoOnClick(content){
  if(content==null){
      if (disabledFormProdutoPreco){
          createMsgbox("jMsg", {width: 250,  height: 100,
                                message: "Para atualizar os dados, coloque o registro em modo de edição.", msgboxType: "INFO"});
          return;
      }
      if ($('cdTabelaPreco').value==0){
          createMsgbox("jMsg", {width: 250,  height: 100,
                                message: "Carregue uma tabela de preço para poder salvar um produto.", msgboxType: "INFO"});
          return;
      }
      
      var construtor = "const " + $('cdTabelaPreco').value + ": int, const " + $('cdProdutoServico').value + ":int, cdProdutoServicoPreco:int,  dtTerminoValidade:GregorianCalendar, vlPrecoPadrao:float, vlPrecoMinimo:float, vlPrecoMaximo:float, vlPrecoCustoProduto:float, vlDiferencaPrecoPadrao:float, vlDiferencaPrecoMinimo:float, vlDiferencaPrecoMaximo:float, cdTabelaPrecoBase:int";
	  var methodName = $("cdProdutoServicoPreco").value>0 ? 
						'insert(new com.tivic.manager.adm.ProdutoServicoPreco(' + construtor + '):com.tivic.manager.adm.ProdutoServicoPreco)' :
						'update(new com.tivic.manager.adm.ProdutoServicoPreco(' + construtor + '):com.tivic.manager.adm.ProdutoServicoPreco)';
		getPage('POST', 'btnSalvarPrecoOnClick', 
				'../methodcaller?className=com.tivic.manager.adm.ProdutoServicoPrecoServices'+
				'&method=' + methodName +
				'&cdProdutoServico=' + $('cdProdutoServico').value,
				precoFields, null, null, null);
      
  }
  else{
      var ok = parseInt(content, 10)>0;
		$("cdProdutoServicoPreco").value = $("cdProdutoServicoPreco").value<=0 && ok ? parseInt(content, 10) : $("cdProdutoServicoPreco").value;
      if(ok){
      	disabledFormProdutoPreco=true;
          alterFieldsStatus(false, precoFields, "vlPrecoPadrao", "disabledField");
          createTempbox("jMsg", {width: 200, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
          loadPrecos();
      }
      else{
          createTempbox("jMsg", {width: 220, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
      }
  }
}

function btnDeletePrecoOnClickAux(content){
  getPage("GET", "btnDeletePrecoOnClick", 
          "../methodcaller?className=com.tivic.manager.adm.ProdutoServicoPrecoDAO"+
          "&method=delete(const "+$("cdTabelaPreco").value+":int, const "+$("cdProdutoServico").value+":int, const "+$("cdProdutoServicoPreco").value+":int):int", null, null, null, null);
}

function btnDeletePrecoOnClick(content){
  if(content==null){
      if ($("cdProdutoServicoPreco").value == 0)
          createMsgbox("jMsg", {width: 320, 
                                height: 50, 
                                message: "Nenhum registro foi carregado para que seja excluído.",
                                msgboxType: "INFO"});
      else
          createConfirmbox("dialog", {caption: "Exclusão de registro",
                                      width: 300, 
                                      height: 75, 
                                      message: "Você tem certeza que deseja excluir este registro?",
                                      boxType: "QUESTION",
                                      positiveAction: function() {setTimeout("btnDeletePrecoOnClickAux()", 10)}});
  }
  else{
      if(parseInt(content)==1){
          createTempbox("jTemp", {width: 300, 
                                height: 75, 
                                message: "Registro excluído com sucesso!",
                                time: 3000});
          loadPrecos();
          btnNewProdutoPrecoOnClick();
      }
      else
          createTempbox("jTemp", {width: 300, 
                                height: 75, 
                                message: "Não foi possível excluir este registro!", 
                                time: 5000});
  }	
}



































/*********************************************************************************************************************************
 ********************************************* Fotos Adicionais *************************************************************************
 *********************************************************************************************************************************/
 var registroFotos = [];
 var indiceGeral = -1;
 function loadFotosAdicionais(content) {
	if (content==null && $('cdProdutoServico').value != 0 && $('cdEmpresa').value != 0) {
		getPage("GET", "loadFotosAdicionais", 
				"../methodcaller?className=com.tivic.manager.ecm.FotoProdutoEmpresaServices"+
				"&method=getAllByProdutoServicoEmpresa(const " + $('cdProdutoServico').value + ":int, const " + $('cdEmpresa').value + ":int)");
	}
	else {
		var rsmFotosAdicionais = null;
		try {rsmFotosAdicionais = eval('(' + content + ')')} catch(e) {}
		if(rsmFotosAdicionais != null && rsmFotosAdicionais.lines && rsmFotosAdicionais.lines.length > 0){
			registroFotos = rsmFotosAdicionais.lines;
			var reg = registroFotos[0];
			indiceGeral = 0;
			$('cdFotoAdicional').value = reg['CD_FOTO'];
			$('imageAdicional').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.ecm.FotoProdutoEmpresaServices&method=getBytesFotoProduto(const '+ reg['CD_FOTO'] +':int, const ' + reg['CD_PRODUTO_SERVICO'] + ':int, const '+ reg['CD_EMPRESA'] +':int)&idSession=imgFoto_' + reg['CD_PRODUTO_SERVICO']  + reg['CD_FOTO'];
			$('txtDescricao').value = reg['TXT_DESCRICAO'];
			
		}
		else{
			$('cdFotoAdicional').value = 0;
			$('imageAdicional').src = '';
			$('txtDescricao').value = '';
		}
	}
}

var isInsertFotosAdicionais = true

function btnNewFotosOnClick(content){
	$('txtDescricao').value			= '';
	$('imageAdicional').src = '';
	$('cdFotoAdicional').value = 0;
	isInsertFotosAdicionais			= true;
	$('txtDescricao').focus();
	btLoadImagemFotoOnClick();
	alterFieldsStatus(true, fotosAdicionaisFields, "txtDescricao");
}

function btnAlterFotosOnClick(content){
	alterFieldsStatus(true, fotosAdicionaisFields, "txtDescricao");
	isInsertFotosAdicionais = false;
}

function btnSaveFotosOnClick(content){
	if(content==null)	{
		var objects = 'imgProduto=byte[]';
		var commandExecute = 'imgProduto=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const imgProduto_' + $('cdProdutoServico').value + $('cdFoto').value + ':String);';
		
		var method = "save(new com.tivic.manager.ecm.FotoProdutoEmpresa(const " +  $('cdFotoAdicional').value + ":int, const "+$('cdProdutoServico').value+":int, " + 
					"const " + $('cdEmpresa').value + ":int, const "+$('nmFoto').value.toUpperCase()+":String, const 0:int, *imgProduto: byte[], *imgProduto: byte[], " + 
					"const "+$('txtDescricao').value+":String))";
		getPage('GET', 'btnSaveFotosOnClick', 
				'../methodcaller?className=com.tivic.manager.ecm.FotoProdutoEmpresaServices'+
				'&objects=' + objects +
				'&execute=' + commandExecute +
				'&method='+method, null);
	}
	else	{
		var ret = processResult(content, 'Dados salvos com sucesso!');
		if(ret.code>0)	{
			if(isInsertFotosAdicionais){
				registroFotos.push(ret.objects['FOTOPRODUTOEMPRESARSM'].lines[0]);
				indiceGeral = registroFotos.length-1;
			}
			else{
				registroFotos[indiceGeral] = ret.objects['FOTOPRODUTOEMPRESARSM'].lines[0];
			}
			showMsgbox('Manager', 300, 50, 'Foto cadastrada com sucesso!');
			alterFieldsStatus(false, fotosAdicionaisFields, "txtDescricao");
		}
		
			
	}
}

function btnDeleteFotosOnClick(content){
	if(content==null) {
		if(indiceGeral < 0){
			showMsgbox('Manager', 300, 50, 'Não há fotos para serem removidas');
		}
		var reg = registroFotos[indiceGeral];
		showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o produto substituto selecionado?', 
						function() {
							getPage('GET', 'btnDeleteFotosOnClick', 
									'../methodcaller?className=com.tivic.manager.ecm.FotoProdutoEmpresaServices'+
									'&method=remove(const '+reg['CD_FOTO']+':int, const ' + reg['CD_PRODUTO_SERVICO'] + ':int, const ' + reg['CD_EMPRESA'] + ':int)');
						});
		
	}
	else {
		var ret = processResult(content, 'Dados salvos com sucesso!');
		if (ret.code > 0) {
			var registroAux = [];
			registroFotos[indiceGeral] = null;
			for(var i = 0, j = 0; i < registroFotos.length; i++){
				if(registroFotos[i] != null){
					registroAux[j] = registroFotos[i];
					j++;
				}
			}
			registroFotos = registroAux;
			var reg = registroFotos[0];
			indiceGeral = 0;
			$('cdFotoAdicional').value = reg['CD_FOTO'];
			$('imageAdicional').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.ecm.FotoProdutoEmpresaServices&method=getBytesFotoProduto(const '+ reg['CD_FOTO'] +':int, const ' + reg['CD_PRODUTO_SERVICO'] + ':int, const '+ reg['CD_EMPRESA'] +':int)&idSession=imgFoto_' + reg['CD_PRODUTO_SERVICO']  + reg['CD_FOTO'];
			$('txtDescricao').value = reg['TXT_DESCRICAO'];
		}
	}
}

function btnPrimeiraFotoOnClick(content){
	var reg = registroFotos[0];
	indiceGeral = 0;
	$('cdFotoAdicional').value = reg['CD_FOTO'];
	$('imageAdicional').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.ecm.FotoProdutoEmpresaServices&method=getBytesFotoProduto(const '+ reg['CD_FOTO'] +':int, const ' + reg['CD_PRODUTO_SERVICO'] + ':int, const '+ reg['CD_EMPRESA'] +':int)&idSession=imgFoto_' + reg['CD_PRODUTO_SERVICO']  + reg['CD_FOTO'];
	$('txtDescricao').value = reg['TXT_DESCRICAO'];
}

function btnFotoAnteriorOnClick(content){
	indiceGeral--;
	if(indiceGeral < 0)
		indiceGeral = 0;
	var reg = registroFotos[indiceGeral];
	$('cdFotoAdicional').value = reg['CD_FOTO'];
	$('imageAdicional').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.ecm.FotoProdutoEmpresaServices&method=getBytesFotoProduto(const '+ reg['CD_FOTO'] +':int, const ' + reg['CD_PRODUTO_SERVICO'] + ':int, const '+ reg['CD_EMPRESA'] +':int)&idSession=imgFoto_' + reg['CD_PRODUTO_SERVICO']  + reg['CD_FOTO'];
	$('txtDescricao').value = reg['TXT_DESCRICAO'];	
}

function btnProximaFotoOnClick(content){
	indiceGeral++;
	if(indiceGeral > (registroFotos.length-1))
		indiceGeral = (registroFotos.length-1);
	var reg = registroFotos[indiceGeral];
	$('cdFotoAdicional').value = reg['CD_FOTO'];
	$('imageAdicional').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.ecm.FotoProdutoEmpresaServices&method=getBytesFotoProduto(const '+ reg['CD_FOTO'] +':int, const ' + reg['CD_PRODUTO_SERVICO'] + ':int, const '+ reg['CD_EMPRESA'] +':int)&idSession=imgFoto_' + reg['CD_PRODUTO_SERVICO']  + reg['CD_FOTO'];
	$('txtDescricao').value = reg['TXT_DESCRICAO'];	
}

function btnUltimaFotoOnClick(content){
	var reg = registroFotos[registroFotos.length-1];
	indiceGeral = registroFotos.length-1;
	$('cdFotoAdicional').value = reg['CD_FOTO'];
	$('imageAdicional').src = 'preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.ecm.FotoProdutoEmpresaServices&method=getBytesFotoProduto(const '+ reg['CD_FOTO'] +':int, const ' + reg['CD_PRODUTO_SERVICO'] + ':int, const '+ reg['CD_EMPRESA'] +':int)&idSession=imgFoto_' + reg['CD_PRODUTO_SERVICO']  + reg['CD_FOTO'];
	$('txtDescricao').value = reg['TXT_DESCRICAO'];
}

function btLoadImagemFotoOnClick() {
 	var idSession = 'imgProduto_' + $('cdProdutoServico').value + $('cdFoto').value;
 	createWindow("jLoadFile", {caption:"Carregar imagem",
 							   width: 410,
 							   height: 90,
 							   contentUrl: '../load_file.jsp?idSession=' + idSession + '&idContentLoad=imageAdicional',
 							   modal: true});
 }

/*********************************************************************************************************************************
 ********************************************* Caracteristicas *************************************************************************
 *********************************************************************************************************************************/
var gridCaracteristicas;
function loadCaracteristicas(content) {
	if (content==null) {
		getPage("GET", "loadCaracteristicas", 
				"../methodcaller?className=com.tivic.manager.ecm.ProdutoEmpresaComentarioServices"+
				"&method=getAllByProdutoServicoEmpresa(const " + $('cdProdutoServico').value + ":int, const " + $('cdEmpresa').value + ":int)");
		
	}
	else {
		var rsmCaracteristicas = null;
		try {rsmCaracteristicas = eval('(' + content + ')')} catch(e) {}
		gridCaracteristicas = GridOne.create('gridCaracteristicas', {columns: [{label: 'Nome', reference: 'NM_TITULO'},
		                                                             		   {label: 'Característica', reference: 'TXT_COMENTARIO_EMPRESA'}],
		                                                     onDoubleClick: function(){
		                                                    	$('cdComentario').value = gridCaracteristicas.getSelectedRowRegister()['CD_COMENTARIO'];
		                                                    	$('txtComentarioEmpresa').value = gridCaracteristicas.getSelectedRowRegister()['TXT_COMENTARIO_EMPRESA'];
		                                                    	$('nmTitulo').value = gridCaracteristicas.getSelectedRowRegister()['NM_TITULO'];
		                                                    	alterFieldsStatus(false, caracteristicasFields, "nmTitulo");
		                                                     },       		   
						                                     resultset: rsmCaracteristicas, plotPlace: $('divGridCaracteristicas')});
	}
}

function clearCaracteristicas(){
	$('cdComentario').value     = 0;
	$('nmTitulo').value     = '';
   	$('txtComentarioEmpresa').value    	   = '';
   	loadCaracteristicas();
}

function btnNewCaracteristicasOnClick(content){
	isInsertCaracteristicas = true;
	alterFieldsStatus(true, caracteristicasFields, "nmTitulo");
	clearCaracteristicas();
	$('nmTitulo').focus();
}

function btnAlterCaracteristicasOnClick(content){
	alterFieldsStatus(true, caracteristicasFields, "nmTitulo");
	isInsertCaracteristicas = false;
}

var isInsertCaracteristicas = true;

function btnSaveCaracteristicasOnClick(content)	{
	if(content==null)	{
		var method = "save(new com.tivic.manager.ecm.ProdutoEmpresaComentario(const " + $('cdComentario').value + ":int, " +
					 "const " + $('cdProdutoServico').value + ":int, const " + $('cdEmpresa').value + ":int, const " + $('cdEmpresa').value + ":int, const 0:int, " + 
					 "const " + $('nmTitulo').value + ":String, const null:String, const " + $('txtComentarioEmpresa').value + ":String," +
					 "const 1:int, const 1:int))";
		getPage('GET2POST', 'btnSaveCaracteristicasOnClick', 
				'../methodcaller?className=com.tivic.manager.ecm.ProdutoEmpresaComentarioServices'+
				'&method='+method, null);
	}
	else	{
		var ret = null;
		try {ret = eval('(' + content + ')')} catch(e) {}
		if(ret.code > 0)	{
			loadCaracteristicas(null);
			showMsgbox('Manager', 300, 50, 'Característica cadastrada com sucesso!');
			alterFieldsStatus(false, caracteristicasFields, "nmTitulo");
		}
		else
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Erro ao salvar. Contate os desenvolvedores!", msgboxType: "INFO"});
			
	}
}

function btnDeleteCaracteristicasOnClick(content){
	if(content==null) {
		showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover a característica selecionada?', 
						function() {
							getPage('GET', 'btnDeleteCaracteristicasOnClick', 
									'../methodcaller?className=com.tivic.manager.ecm.ProdutoEmpresaComentarioServices'+
									'&method=remove(const ' + $('cdComentario').value + ':int, const ' + $('cdProdutoServico').value + ':int, const ' + $('cdEmpresa').value + ':int)');
						});
		
	}
	else {
		var ret = null;
		try {ret = eval('(' + content + ')')} catch(e) {}
		if(ret.code > 0)	{
			alterFieldsStatus(true, caracteristicasFields, "nmTitulo");
			loadCaracteristicas();
			btnNewCaracteristicasOnClick();
		}
		else
			showMsgbox('Manager', 300, 50, 'ERRO ao tentar excluir característica!');
	}
	
}


/*********************************************************************************************************************************
 ****************************************************** FORNECEDORES *************************************************************
 *********************************************************************************************************************************/
var formFornecedor = null;
var columnsFornecedor = [{label:'Código Produto', reference:'ID_PRODUTO'},
						 {label:'Nome fornecedor', reference:'NM_FORNECEDOR'}, 
						 {label:'Nome representante', reference:'NM_REPRESENTANTE'}, 
						 {label:'Último preço', reference:'VL_ULTIMO_PRECO', type:GridOne._CURRENCY}, 
						 {label:'Dias entrega', reference:'qt_dias_entrega', style: 'text-align: right;'}];
var gridFornecedores = null;
function validateFornecedor(content) {
    if($("cdFornecedor").value == '0') {
		showMsgbox('Manager', 300, 50, 'Indique o fornecedor.', function() {});
        return false;
	}
    else
		return true;
}

function btnSaveFornecedorOnClick(content) {
	if (content==null) {
		if ($('cdProdutoServico').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize um produto para lançar os fornecedores.');
		else if (validateFornecedor()) {
			$('btnSaveFornecedor').disabled = true;
			var produtoServicoDescription = ' (Produto ' + $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value + ")";
            var executionDescription = $('cdFornecedorOld').value>0 ? formatDescriptionUpdate("Configuração de Fornecedor", $("cdFornecedorOld").value + " " + produtoServicoDescription, $("dataOldFornecedor").value, fornecedorFields) : formatDescriptionInsert("Configuração de Fornecedor " + produtoServicoDescription, fornecedorFields);
			var construtor = 'cdFornecedor: int, cdEmpresa: int, cdProdutoServico: int, cdRepresentante: int, vlUltimoPreco: float, qtDiasEntrega: int, qtDiasUltimaEntrega: int, idProduto: String, qtPedidoMinimo:float, cdMoeda:int, cdUnidadeMedida:int';
			var methodName = gridFornecedores.getSelectedRow()==null ? 
							'insert(new com.tivic.manager.adm.ProdutoFornecedor(' + construtor + '):com.tivic.manager.adm.ProdutoFornecedor)':
							'update(new com.tivic.manager.adm.ProdutoFornecedor(' + construtor + '):com.tivic.manager.adm.ProdutoFornecedor)';
			getPage('POST', 'btnSaveFornecedorOnClick', 
					'../methodcaller?className=com.tivic.manager.adm.ProdutoFornecedorServices' +
					'&method=' + methodName +
					'&cdProdutoServico=' + $('cdProdutoServico').value +
                    '&cdEmpresa=' + $('cdEmpresa').value,
					fornecedorFields, null, null, executionDescription);
		}
	}
	else {
		$('btnSaveFornecedor').disabled = false;
		closeWindow('jFornecedor');
		if (isInteger(content) && parseInt(content, 10) > 0) {
			if (gridFornecedores.getSelectedRow()==null) {
				gridFornecedores.addLine(0, loadRegisterFromForm(fornecedorFields), onClickFornecedor, true);
			}
			else {
				gridFornecedores.updateSelectedRowByFields(fornecedorFields);
			}
			$('cdFornecedorOld').value = $('cdFornecedor').value;
			$("dataOldFornecedor").value = captureValuesOfFields(fornecedorFields);
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
		}
		else
			showMsgbox('Manager', 300, 100, 'Erros reportados ao configurar fornecedor para o Produto. Certifique-se que o fornecedor ainda não esteja configurado para a Produto.');
	}
}

function onClickFornecedor() {
}

function loadFornecedores(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		getPage("GET", "loadFornecedores", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
				"&method=getAllFornecedores(const " + $('cdProdutoServico').value + ":int, " +
				"const " + $('cdEmpresa').value + ":int)");
	}
	else {

		var rsmFornecedores = null;
		try {rsmFornecedores = eval('(' + content + ')')} catch(e) {}
		gridFornecedores = GridOne.create('gridFornecedores', {columns: columnsFornecedor,
					     resultset :rsmFornecedores, 
					     plotPlace : $('divGridFornecedores'),
					     onSelect : onClickFornecedor});
	}
}

function btnDeleteFornecedorOnClick(content)	{
	if(content==null) {
		if (gridFornecedores.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o fornecedor que deseja excluir.');
		else {
			var produtoServicoDescription = ' (Produto ' + $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value + ")";
		    var executionDescription = formatDescriptionDelete("Remoção de fornecedor " + produtoServicoDescription, $("cdFornecedorOld").value, $("dataOldFornecedor").value);
			var cdFornecedor = gridFornecedores.getSelectedRow().register['CD_FORNECEDOR'];
			var cdProdutoServico = $('cdProdutoServico').value;
			var cdEmpresa = $('cdEmpresa').value;
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o fornecedor selecionado?', 
							function() {
								getPage('GET', 'btnDeleteFornecedorOnClick', 
										'../methodcaller?className=com.tivic.manager.adm.ProdutoFornecedorDAO'+
										'&method=delete(const ' + cdFornecedor + ':int, const ' + cdEmpresa + ':int, const ' + cdProdutoServico + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridFornecedores.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir Preço.');
	}
}

function btnAlterFornecedorOnClick() {
	if (gridFornecedores.getSelectedRow()==null)
		showMsgbox('Manager', 300, 50, 'Selecione o fornecedor que você deseja alterar.');
	else {
    	loadFormRegister(fornecedorFields, gridFornecedores.getSelectedRow().register, true);
		formFornecedor = createWindow('jFornecedor', {caption: "Fornecedor",
									 width: 460,
									 height: 178,
									 noDropContent: true,
									 modal: true,
									 contentDiv: 'panelFornecedor'});
		$('qtDiasEntrega').focus();		
	}
}

function btnNewFornecedorOnClick(){
    if ($('cdProdutoServico').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize um produto para lançar fornecedores.');
	else {

		gridFornecedores.unselectGrid();
		$("dataOldFornecedor").value = "";
		clearFields(fornecedorFields);
		formFornecedor = createWindow('jFornecedor', {caption: "Fornecedor",
									 width: 460,
									 height: 178,
									 noDropContent: true,
									 modal: true,
									 contentDiv: 'panelFornecedor'});
		btnFindCdFornecedorOnClick();
	}
}

function btnFindCdFornecedorOnClick(reg)	{
    if(!reg){
        var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:60, charcase:'uppercase'},
							 {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:20, type:GridOne._MASK, mask:'###.###.###-##'},
							 {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}],
						    [{label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:60, charcase:'uppercase'},
						     {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:20},
						     {label:"Inscrição Estadual", reference:"NR_INSCRICAO_ESTADUAL", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]];
        var columnsGrid = [{label:"Nome", reference:"NM_PESSOA"}, 
						   {label:"ID", reference:"ID_PESSOA"}, 
						   {label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'},
						   {label:"Razão Social", reference:"NM_RAZAO_SOCIAL"},
						   {label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ},
					       {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"},
						   {label:'CPF', reference:'NR_CPF', type:GridOne._CPF},
						   {label:"Identidade", reference:"NR_RG"},
						   {label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE}];
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar fornecedores", 
												    width: 600,
												    height: 340,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.grl.PessoaServices",
												    method: "find",
												    filterFields: filterFields,
												    gridOptions: {columns: columnsGrid,
													  		      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    hiddenFields: [{reference:"J.CD_EMPRESA", value:$('cdEmpresa').value, comparator:_EQUAL, datatype:_INTEGER},
																   {reference:"J.CD_VINCULO", value:cdVinculoFornecedor, comparator:_EQUAL, datatype:_INTEGER}],
												    callback: btnFindCdFornecedorOnClick, 
													autoExecuteOnEnter: true
										           });
    }
    else {// retorno
    	filterWindow.close();
		$('cdFornecedor').value = reg[0]['CD_PESSOA'];
		$('nmFornecedor').value = reg[0]['NM_PESSOA'];
    }
}

function btnClearCdFornecedorOnClick(reg){
	$('cdFornecedor').value = 0;
	$('nmFornecedor').value = '';
}

function btnFindCdRepresentanteOnClick(reg){
    if(!reg){
        var filterFields = [[{label:"Nome do representante", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:60, charcase:'uppercase'},
							 {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:20, type:GridOne._MASK, mask:'###.###.###-##'},
							 {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]];
        var columnsGrid = [{label:"Nome do representante", reference:"NM_PESSOA"}, 
						   {label:"ID", reference:"ID_PESSOA"}, 
						   {label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'},
						   {label:'CPF', reference:'NR_CPF', type:GridOne._CPF},
						   {label:"Identidade", reference:"NR_RG"},
						   {label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE}];
			
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar representantes", 
												    width: 500,
												    height: 310,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.grl.PessoaServices",
												    method: "find",
												    filterFields: filterFields,
												    gridOptions: {columns: columnsGrid,
															      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    hiddenFields: [{reference:"A.GN_PESSOA",value:<%=com.tivic.manager.grl.PessoaServices.TP_FISICA%>,comparator:_EQUAL,datatype:_INTEGER}],
												    callback: btnFindCdRepresentanteOnClick, 
													autoExecuteOnEnter: true
										           });
    }
    else {// retorno
        closeWindow('jFiltro');
		$('cdRepresentante').value = reg[0]['CD_PESSOA'];
		$('nmRepresentante').value = reg[0]['NM_PESSOA'];
    }
}

function btnClearCdRepresentanteOnClick(reg){
	$('cdRepresentante').value = 0;
	$('nmRepresentante').value = '';
}

function btnDisponilidadeEstoqueOnClick()	{
	if ($('cdProdutoServico').value == '0')	{
		showMsgbox('Manager', 400, 30, 'Inclua ou localize um produto antes de consultar os estoques disponíveis!');
		return;
	}
	createWindow('jDisponibilidadeEstoque', {caption: "Disponibilidade em Estoque",
								  			 width: 644, 
											 height: 345, 
											 top: 10,
								  			 noDropContent: true,
								  			 modal: true,
								  			 contentDiv: 'formDisponibilidade'});
}

function btnMovimentacaoProdutoOnClick() {
	var cdProdutoServico = $('cdProdutoServico').value;
	if (cdProdutoServico <= 0) {
		createMsgbox("jMsg", {width: 350, 
							  height: 30, 
							  message: "Inclua ou localize um produto antes de executar esta consulta.", 
							  msgboxType: "INFO"});
	}
	else {
		var cdEmpresa = $('cdEmpresa').value;
		createWindow('jReportMovimentoProduto', {caption: 'Movimentação de produtos', 
												 width: 590, 
												 height: 350, 
												 modal: true,
												 contentUrl: '../alm/relatorio_movimento_produto.jsp?cdEmpresa=' + cdEmpresa + '&cdProdutoServico= ' + cdProdutoServico});
	}
}

/*********************************************************************************************************************************
 ********************************************* Codigo de Barras *************************************************************************
 *********************************************************************************************************************************/
var gridCodigoBarras;
function loadCodigoBarras(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		getPage("GET", "loadCodigoBarras", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoCodigoServices"+
				"&method=getAll(const " + $('cdProdutoServico').value + ":int)");
	}
	else {
		var rsmCodigoBarras = null;
 		try {rsmCodigoBarras = eval('(' + content + ')')} catch(e) {}
 		gridCodigoBarras = GridOne.create('gridCodigoBarras', {columns: [{label: 'Código de Barras', reference: 'ID_PRODUTO_SERVICO'},
 			                                                             {label: 'Descrição', reference: 'TXT_DESCRICAO'},
 			                                                             {label: 'ID Reduzido', reference: 'ID_REDUZIDO'}],
                                                         onDoubleClick:function() {
                                                           	findProdutoCodigoBarras(gridCodigoBarras.getSelectedRowRegister()); 
											 		     },          
					                                     resultset: rsmCodigoBarras, plotPlace: $('divGridCodigoBarras')});
	}
}

function findProdutoCodigoBarras(reg) {
    alterFieldsStatus(false, codigoBarrasFields, "idProdutoServicoCodigoBarras", "disabledField2");
    $('cdProdutoCodigoCodigoBarras').value     = reg['CD_PRODUTO_CODIGO'];
   	$('idProdutoServicoCodigoBarras').value    = reg['ID_PRODUTO_SERVICO'];
   	$('idReduzidoCodigoBarras').value     	   = reg['ID_REDUZIDO'];
   	$('txtDescricaoCodigoBarras').value    	   = reg['TXT_DESCRICAO'];
   	
   	$("idProdutoServicoCodigoBarras").focus();
}

function clearCodigoBarras(){
	$('cdProdutoCodigoCodigoBarras').value     = '';
   	$('idProdutoServicoCodigoBarras').value    = '';
   	$('idReduzidoCodigoBarras').value     	   = '';
   	$('txtDescricaoCodigoBarras').value    	   = '';
}

function btnNewCodigoBarrasOnClick(content){
	clearCodigoBarras();
	alterFieldsStatus(true, codigoBarrasFields, "idProdutoServicoCodigoBarras");
	isInsertCodigoBarras = true;
	$('idProdutoServicoCodigoBarras').focus();
}

function btnAlterCodigoBarrasOnClick(content){
	alterFieldsStatus(true, codigoBarrasFields, "idProdutoServicoCodigoBarras");
	isInsertCodigoBarras = false;
}

var isInsertCodigoBarras = true;

function btnSaveCodigoBarrasOnClick(content)	{
	if(content==null)	{
		var method = "save(new com.tivic.manager.grl.ProdutoCodigo(const " +  $('cdProdutoCodigoCodigoBarras').value + ":int, const "+$('txtDescricaoCodigoBarras').value.toUpperCase()+":String, " + 
					"const "+$('idProdutoServicoCodigoBarras').value.toUpperCase()+":String, const "+$('idReduzidoCodigoBarras').value.toUpperCase()+":String, const "+$('cdProdutoServico').value+":int, const 1:int))";
		getPage('GET', 'btnSaveCodigoBarrasOnClick', 
				'../methodcaller?className=com.tivic.manager.grl.ProdutoCodigoServices'+
				'&method='+method, null);
	}
	else	{
		var ret = null;
		try {ret = eval('(' + content + ')')} catch(e) {}
		if(ret.code > 0)	{
			loadCodigoBarras(null);
			closeWindow('jCodigoBarras');
			showMsgbox('Manager', 300, 50, 'Codigo de Barras cadastrada com sucesso!');
			alterFieldsStatus(false, codigoBarrasFields, "idProdutoServicoCodigoBarras");
		}
		else
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: (ret.message ? ret.message : "Erro ao salvar. Contate os desenvolvedores!"), msgboxType: "INFO"});
			
	}
}

function btnDeleteCodigoBarrasOnClick(content){
	if(content==null) {
		if (gridCodigoBarras.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o código de barras que deseja excluir.');
		else {
			var cdProdutoServico = gridCodigoBarras.getSelectedRow().register['CD_PRODUTO_CODIGO'];
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o código de barras selecionado?', 
							function() {
								getPage('GET', 'btnDeleteCodigoBarrasOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.ProdutoCodigoServices'+
										'&method=remove(const ' + cdProdutoServico + ':int):int', null, null, null, null);
							});
		}
	}
	else {
		var ret = null;
		try {ret = eval('(' + content + ')')} catch(e) {}
		if(ret.code > 0)	{
			gridCodigoBarras.removeSelectedRow();
			btnNewCodigoBarrasOnClick();
		}
		else
			showMsgbox('Manager', 300, 50, 'ERRO ao tentar excluir produto similar!');
	}
	
}


/*********************************************************************************************************************************
 ********************************************* Outros Códigos *************************************************************************
 *********************************************************************************************************************************/
var gridOutrosCodigos;
function loadOutrosCodigos(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		getPage("GET", "loadOutrosCodigos", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoCodigoServices"+
				"&method=getAll(const " + $('cdProdutoServico').value + ":int, const 0:int)");
	}
	else {
		var rsmOutrosCodigos = null;
 		try {rsmOutrosCodigos = eval('(' + content + ')')} catch(e) {}
 		gridOutrosCodigos = GridOne.create('gridOutrosCodigos', {columns: [{label: 'Código', reference: 'ID_REDUZIDO'},
 			                                                             {label: 'Descrição', reference: 'TXT_DESCRICAO'}],
                                                         onDoubleClick:function() {
                                                           	findProdutoOutrosCodigos(gridOutrosCodigos.getSelectedRowRegister()); 
											 		     },          
					                                     resultset: rsmOutrosCodigos, plotPlace: $('divGridOutrosCodigos')});
	}
}

function findProdutoOutrosCodigos(reg) {
    alterFieldsStatus(false, outrosCodigosFields, "idProdutoServicoOutrosCodigos", "disabledField2");
    $('cdProdutoCodigoOutrosCodigos').value     = reg['CD_PRODUTO_CODIGO'];
   	$('idReduzidoOutrosCodigos').value     	    = reg['ID_REDUZIDO'];
   	$('txtDescricaoOutrosCodigos').value    	= reg['TXT_DESCRICAO'];
   	
   	$("idReduzidoOutrosCodigos").focus();
}

function clearOutrosCodigos(){
	$('cdProdutoCodigoOutrosCodigos').value     = '';
   	$('idReduzidoOutrosCodigos').value     	   = '';
   	$('txtDescricaoOutrosCodigos').value    	   = '';
}

function btnNewOutrosCodigosOnClick(content){
	clearOutrosCodigos();
	alterFieldsStatus(true, outrosCodigosFields, "idReduzidoOutrosCodigos");
	isInsertOutrosCodigos = true;
	$('idReduzidoOutrosCodigos').focus();
}

function btnAlterOutrosCodigosOnClick(content){
	alterFieldsStatus(true, outrosCodigosFields, "idReduzidoOutrosCodigos");
	isInsertOutrosCodigos = false;
}

var isInsertOutrosCodigos = true;

function btnSaveOutrosCodigosOnClick(content)	{
	if(content==null)	{
		var method = "save(new com.tivic.manager.grl.ProdutoCodigo(const " +  $('cdProdutoCodigoOutrosCodigos').value + ":int, const "+$('txtDescricaoOutrosCodigos').value.toUpperCase()+":String, " + 
					"const null:String, const "+$('idReduzidoOutrosCodigos').value.toUpperCase()+":String, const "+$('cdProdutoServico').value+":int, const 0:int))";
		getPage('GET', 'btnSaveOutrosCodigosOnClick', 
				'../methodcaller?className=com.tivic.manager.grl.ProdutoCodigoServices'+
				'&method='+method, null);
	}
	else	{
		var ret = null;
		try {ret = eval('(' + content + ')')} catch(e) {}
		if(ret.code > 0)	{
			loadOutrosCodigos(null);
			closeWindow('jOutrosCodigos');
			showMsgbox('Manager', 300, 50, 'Codigo cadastrado com sucesso!');
			alterFieldsStatus(false, outrosCodigosFields, "idReduzidoOutrosCodigos");
		}
		else
			createMsgbox("jMsg", {caption: 'Manager', width: 300, height: 40, message: "Erro ao salvar. Contate os desenvolvedores!", msgboxType: "INFO"});
			
	}
}

function btnDeleteOutrosCodigosOnClick(content){
	if(content==null) {
		if (gridOutrosCodigos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o código de barras que deseja excluir.');
		else {
			var cdProdutoServico = gridOutrosCodigos.getSelectedRow().register['CD_PRODUTO_CODIGO'];
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o código selecionado?', 
							function() {
								getPage('GET', 'btnDeleteOutrosCodigosOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.ProdutoCodigoServices'+
										'&method=remove(const ' + cdProdutoServico + ':int):int', null, null, null, null);
							});
		}
	}
	else {
		var ret = null;
		try {ret = eval('(' + content + ')')} catch(e) {}
		if(ret.code > 0)	{
			gridOutrosCodigos.removeSelectedRow();
			btnNewOutrosCodigosOnClick();
		}
		else
			showMsgbox('Manager', 300, 50, 'ERRO ao tentar excluir produto similar!');
	}
	
}
/*********************************************************************************************************************************
 ********************************************* Ultimo Fornecedor *************************************************************************
 *********************************************************************************************************************************/
function loadUltimoFornecedor(content) {
	if(content == null){
		var className = "com.tivic.manager.grl.PessoaServices";
		createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
		getPage("POST", "loadUltimoFornecedor", "../methodcaller?className="+ className + 
						  "&method=findUltimoFornecedor(const"+<%=cdEmpresa%>+":int, const"+cdVinculoFornecedor+":int, const "+$('cdProdutoServico').value+":int)", pesquisaFields, null, null, null);
	}
    else {// retorno
    	closeWindow('jLoadMsg');
    	var rsmForn = null;
    	try {rsmForn = eval('(' + content + ')')} catch(e) {}
    	if(rsmForn == null){
			$('cdUltimoFornecedor').value     = rsmForn.lines[0]['cdPessoa'];
			$('cdUltimoFornecedorView').value = rsmForn.lines[0]['nmPessoa'];
    	}
    	else{
    		$('cdUltimoFornecedor').value     = '';
			$('cdUltimoFornecedorView').value = '';
    	}
    	
	}
}
/*********************************************************************************************************************************
 ********************************************* GRADE *****************************************************************************
 *********************************************************************************************************************************/
var gridSimilares;
function loadSimilares(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		getPage("GET", "loadSimilares", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
				"&method=getAllSimilares(const " + $('cdProdutoServico').value + ":int, const 0: int)");
	}
	else {
		var rsmSimilares = null;
		try {rsmSimilares = eval('(' + content + ')')} catch(e) {}
		gridSimilares = GridOne.create('gridSimilares', {columns: [{label: 'Nome produto', reference: 'NM_PRODUTO_SERVICO'},
		                                                           {label: 'Tam', reference: 'TXT_DADO_TECNICO'},
		                                                           {label: 'Cor', reference: 'TXT_ESPECIFICACAO'},
		                                                           {label: 'Cód. Barras', reference: 'ID_PRODUTO_SERVICO'}],
                                                         onDoubleClick:function() {
														   	findProdutoGrade(null, gridSimilares.getSelectedRowRegister()['CD_PRODUTO_SERVICO_SIMILAR']); 
											 		     },
					                                     resultset: rsmSimilares, 
					                                     plotPlace: $('divGridGrade')});
	}
}

function findProdutoGrade(content, cdProdutoServico) {
	if(content == null){
		var className = "com.tivic.manager.grl.ProdutoServicoDAO";
		createTempbox("jLoadMsg", {caption: 'Manager', width: 170, height: 40, message: 'Processando...', tempboxType: "LOADING"});
		getPage("POST", "findProdutoGrade", "../methodcaller?className="+ className + 
						  "&method=get(const "+cdProdutoServico+":int)", null, null, null, null);
	}
    else {// retorno
    	closeWindow("jLoadMsg");
        //disabledFormProdutoServico=true;
        alterFieldsStatus(false, gradeFields, "nmProdutoServicoGrade", "disabledField2");
        var rsmPesquisa = null;
        try {rsmPesquisa = eval('(' + content + ')')} catch(e) {}
    	var reg = rsmPesquisa;
    	$('cdProdutoServicoGrade').value = reg['cdProdutoServico'];
    	$('nmProdutoServicoGrade').value = reg['nmProdutoServico'];
    	$('txtDadoTecnicoGrade').value   = reg['txtDadoTecnico'];
    	$('txtEspecificacaoGrade').value = reg['txtEspecificacao'];
    	$('idProdutoServicoGrade').value = reg['idProdutoServico'];
    	$('nrReferenciaGrade').value 	 = reg['nrReferencia'];
        //$("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
// 		if ($('nmClassificacaoFiscalView') != null) {
// 			if (reg[0]['CD_CLASSIFICACAO_FISCAL']==null || reg['CD_CLASSIFICACAO_FISCAL']==0)
// 				$('nmClassificacaoFiscalView').value = '';
// 			else 
// 				$('nmClassificacaoFiscalView').value = (trim(reg['ID_CLASSIFICACAO_FISCAL'])!='' ? reg['ID_CLASSIFICACAO_FISCAL'] + ' - ' : '') + reg['NM_CLASSIFICACAO_FISCAL'];
// 		}
// 		$("cdEmpresa").value = $("cdEmpresa").getAttribute("defaultValue");
		$("nmProdutoServicoGrade").focus();
	}

}

function btnNewGradeOnClick(content){
	$('cdProdutoServicoGrade').value = '';
	$('nmProdutoServicoGrade').value = '';
	$('txtDadoTecnicoGrade').value = '';
	$('txtEspecificacaoGrade').value = '';
	$('idProdutoServicoGrade').value = '';
	$('nrReferenciaGrade').value = '';
	alterFieldsStatus(true, gradeFields, "nmProdutoServicoGrade");
}

function btnAlterGradeOnClick(content){
	alterFieldsStatus(true, gradeFields, "nmProdutoServicoGrade");
}

function btnSaveGradeOnClick(content)	{
	if(content==null)	{
		var objects = "grade=java.util.ArrayList();";
		var execute = "";
		
		if(($("nrReferenciaGrade").value+'').trim()==''){
			showMsgbox('Manager', 300, 50, 'Produto não pode ser cadastrado sem referência');
			return;
		}
		
		var grade = $("txtDadoTecnicoGrade").value+" |"+$("txtEspecificacaoGrade").value+" |"+$("idProdutoServicoGrade").value+" |"+$("nrReferenciaGrade").value+" |"+$("nmProdutoServicoGrade").value+" |";
		execute += "grade.add(const "+grade+":Object);";
		
		var method = ($('cdProdutoServicoGrade').value == '') ? "insertGrade(const " +  $('cdProdutoServico').value + ":int": "updateGrade(const " +  $('cdProdutoServicoGrade').value + ":int";
		
		getPage('GET', 'btnSaveGradeOnClick', 
				'../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices'+
				'&execute='+execute+
				'&objects='+objects+
				'&method='+method+', const <%=cdEmpresa%>:int,*grade:java.util.ArrayList, const true:boolean)', null);
	}
	else	{
		var ret = processResult(content, '');
		if(ret.code>0)	{
			loadSimilares(null);
			closeWindow('jGrade');
			showMsgbox('Manager', 300, 50, 'Grade cadastrada com sucesso!');
			alterFieldsStatus(false, gradeFields, "nmProdutoServicoGrade");
		}
			
	}
}

function btnDeleteGradeOnClick(content){
	if(content==null) {
		if (gridSimilares.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o produto similar que deseja excluir.');
		else {
			var cdSimilar = (gridSimilares.getSelectedRow().register['CD_SIMILAR'] != null) ? gridSimilares.getSelectedRow().register['CD_SIMILAR'] : gridSimilares.getSelectedRow().register['CD_PRODUTO_SERVICO'];
			var nmSimilar = gridSimilares.getSelectedRow().register['NM_PRODUTO_SERVICO'];
			var cdProdutoServico = $('cdProdutoServico').value;
			var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		    var executionDescription = "Exclusão do similar " + nmSimilar + " (Cód. " + cdSimilar + ") da relação de similares do produto " + produtoServicoDescription;	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o produto similar selecionado?', 
							function() {
								getPage('GET', 'btnDeleteGradeOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO'+
										'&method=delete(const ' + cdProdutoServico + ':int, const ' + cdSimilar + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridSimilares.removeSelectedRow();
			btnNewGradeOnClick();
		}
		else
			showMsgbox('Manager', 300, 50, 'ERRO ao tentar excluir produto similar!');
	}
	
}



<%-- var tamanhos = new StringTokenizer("<%=ParametroServices.getValorOfParametro("DS_GRADE", "PP,P,M,G,XG,XXG", cdEmpresa, null)%>", ",").getTokens(); --%>
// function btnGradeOnClick()	{
// 	if ($('cdProdutoServico').value <= 0)	{
// 		showMsgbox('Manager', 300, 50, 'Nenhum produto carregado...');
// 		return;
// 	}
// 	if ($('txtDadoTecnico').value == '')	{
// 		showMsgbox('Manager', 300, 50, 'O tamanho do produto atual não foi informado, ou seja, ele não faz parte de uma grade.');
// 		return;
// 	}
// 	if (gridSimilares!=null && gridSimilares.size()>0)	{
// 		showMsgbox('Manager', 300, 50, 'A grade desse produto já foi cadastrada!');
// 		return;
// 	}
	
// 	var linhas    = [];
// 	var isOnGrade = 0;
// 	for(var i=0; i<tamanhos.length; i++)	{
// 		isOnGrade = ($('txtDadoTecnico').value.toUpperCase()==tamanhos[i].toUpperCase()) || isOnGrade==1 ? 1 : 0;
		
// 		if(($('txtDadoTecnico').value+'').toUpperCase().trim()==(tamanhos[i]+'').toUpperCase().trim())
// 			continue;
			
// 		linhas.push([{id:'txtDadoTecnico_G'+i, type:'text', reference: '', label:'Tamanho', width:20, value: tamanhos[i]},
// 		             {id:'txtEspecificacao_G'+i, type:'text', reference: '', label:'Cor', width:20, value: ($('txtEspecificacao').value+'').toUpperCase()},
// 		             {id:'idProdutoServico_G'+i, type:'text', reference: '', label:'Código de Barras (GTIN/NGIC)', width:35},
// 			  		 {id:'nrReferencia_G'+i, type:'text', reference: '', label:'Referência', width:25}]);
// 	}
// 	linhas.push([{type: 'space', width: 60},
// 				 {id:'btnSaveGrade', type:'button', label:'Cadastrar', width: 20, height:19, image: '/sol/imagens/form-btSalvar16.gif', 
// 				   	onClick: function(){ btnSaveGradeOnClick(null); }
// 	             },
// 				 {id:'btnCancelarGrade', type:'button', label:'Cancelar', width:20, height:19, image: '/sol/imagens/cancel_13.gif', 
// 				  onClick: function(){ closeWindow('jGrade'); }
// 				 }]);	
// 	FormFactory.createFormWindow('jGrade', {caption: "Cadastrando grade do produto", width: 500, height: ((tamanhos.length-isOnGrade)*30)+54, unitSize: '%', modal: true,
// 						                      id: 'produtoSimilar', loadForm: true, noDrag: true,
// 						                      lines: linhas, focusField:'idProdutoServico0'});
// }


// var produtoServicoSimilarTemp = null;
// function btnNewProdutoSimilarOnClick(reg)	{
//     if(!reg){
// 		if ($('cdProdutoServico').value == 0)
//             createMsgbox("jMsg", {width: 250, height: 100, msgboxType: "INFO", message: "Inclua ou localize um produto para especificar similares."})
// 		else {
// 			var hiddenFieldsProdutoServico = [{reference:"A.tp_produto_servico", value:0, comparator:_EQUAL, datatype:_INTEGER},
// 											  {reference:"B.cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER},
// 											  {reference:"A.cd_produto_servico", value:$("cdProdutoServico").value, comparator:_DIFFERENT, datatype:_INTEGER}];
			
// 			if (cdGrupo > 0)
// 				hiddenFieldsProdutoServico.push({reference:"cd_grupo", value:cdGrupo, comparator:_EQUAL, datatype:_INTEGER});
// 			var filterFieldsProdutoServico = [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
// 											  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width: 60, charcase:'uppercase'},
// 											   {label:"ID", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:'uppercase'}]
// 											  ];
// 			filterFieldsProdutoServico[1].push({label:"ID reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:10, charcase:'uppercase'});
				
// 			filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar produtos", width: 550, height: 300, top: 50, modal: true, noDrag: true,
// 													    className: "com.tivic.manager.alm.ProdutoEstoqueServices", method: "findCompleto", allowFindAll: false,
// 													    filterFields: filterFieldsProdutoServico,
// 													    gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
// 														 					    {label:"ID", reference:"ID_PRODUTO_SERVICO"},
// 																			    {label:"ID reduzido", reference:"ID_REDUZIDO"},
// 																			    {label:"Fabricante", reference:"NM_FABRICANTE"}],
// 														              strippedLines: true, columnSeparator: false, lineSeparator: false},
// 													    hiddenFields: hiddenFieldsProdutoServico,
// 													    callback: btnNewProdutoSimilarOnClick, 
// 														autoExecuteOnEnter: true});



// 		}
//     }
//     else {// retorno
//         filterWindow.close();
// 		produtoServicoSimilarTemp = reg[0];
// 		setTimeout('btnNewProdutoSimilarAuxOnClick()', 1);
//     }
// }

// function btnNewProdutoSimilarAuxOnClick(content){
//     if(content==null){
// 		var cdSimilar = produtoServicoSimilarTemp["CD_PRODUTO_SERVICO"];
// 		var nmSimilar = produtoServicoSimilarTemp['NM_PRODUTO_SERVICO'];
// 		var cdProdutoServico = $('cdProdutoServico').value;
// 		var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
// 		var executionDescription = "Inclusão de produto similar " + nmSimilar + " (Cód. " + cdSimilar + ") " + produtoServicoDescription;		
// 		getPage("GET", "btnNewProdutoSimilarAuxOnClick", "../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO" +
// 														 "&method=insert(new com.tivic.manager.grl.ProdutoSimilar(cdProdutoServico: int, cdSimilar: int, const 0: int):com.tivic.manager.grl.ProdutoSimilar)" +
// 												  		 "&cdProdutoServico=" + cdProdutoServico +
// 												  		 "&cdSimilar=" + cdSimilar, null, null, null, executionDescription);
//     }
//     else{
//         var ok = parseInt(content, 10) > 0;
// 		if (ok)
// 			gridSimilares.addLine(0, produtoServicoSimilarTemp, null, true)	
// 		else
//             createTempbox("jMsg", {width: 200, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
//     }
// }

// function btnDeleteProdutoSimilarOnClick(content)	{
// 	if(content==null) {
// 		if (gridSimilares.getSelectedRow()==null)
// 			showMsgbox('Manager', 300, 50, 'Selecione o produto similar que deseja excluir.');
// 		else {
// 			var cdSimilar = (gridSimilares.getSelectedRow().register['CD_SIMILAR'] != null) ? gridSimilares.getSelectedRow().register['CD_SIMILAR'] : gridSimilares.getSelectedRow().register['CD_PRODUTO_SERVICO'];
// 			var nmSimilar = gridSimilares.getSelectedRow().register['NM_PRODUTO_SERVICO'];
// 			var cdProdutoServico = $('cdProdutoServico').value;
// 			var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
// 		    var executionDescription = "Exclusão do similar " + nmSimilar + " (Cód. " + cdSimilar + ") da relação de similares do produto " + produtoServicoDescription;	
// 			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o produto similar selecionado?', 
// 							function() {
// 								getPage('GET', 'btnDeleteProdutoSimilarOnClick', 
// 										'../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO'+
// 										'&method=delete(const ' + cdProdutoServico + ':int, const ' + cdSimilar + ':int):int', null, null, null, executionDescription);
// 							});
// 		}
// 	}
// 	else {
// 		if (isInteger(content) && parseInt(content, 10) > 0) {
// 			gridSimilares.removeSelectedRow();
// 		}
// 		else
// 			showMsgbox('Manager', 300, 50, 'ERRO ao tentar excluir produto similar!');
// 	}
// }

function onChangeStProdutoEmpresa(component) {
	if (component.checked==false)
		component.value = 0;
	else
		component.value = 1;
}

function btLoadImagemOnClick() {
	var idSession = 'imgProduto_' + $('cdProdutoServico').value;
	createWindow("jLoadFile", {caption:"Carregar imagem",
							   width: 410,
							   height: 90,
							   contentUrl: '../load_file.jsp?idSession=' + idSession + '&idContentLoad=imageProduto',
							   modal: true});
}



</script>
<style type="text/css" media="all">
.checkPersonalizado {
	font-family: Geneva,Arial,Helvetica,sans-serif; 
	font-size: 11px;
	vertical-align: middle; 
}
</style>
</head>
<body class="body" onload="initProdutoServico();" id="produtoBody">
<iframe id="frameHidden" name="frameHidden" style="display:none"></iframe>
<div style="width: 893px;" id="produtoServico" class="d1-form">
    <div style="width: 893px; height: 480px;" class="d1-body">
      <input idform="" reference="" id="dataOldProdutoServico" name="dataOldProdutoServico" type="hidden"/>
      <input idform="" reference="" id="dataOldLocalArmazenamento" name="dataOldLocalArmazenamento" type="hidden"/>
      <input idform="" reference="" id="dataOldTributo" name="dataOldTributo" type="hidden"/>
      <input idform="" reference="" id="dataOldPreco" name="dataOldPreco" type="hidden"/>
      <input idform="" reference="" id="dataOldFornecedor" name="dataOldFornecedor" type="hidden"/>
      <input idform="" reference="" id="dataOldFotoProduto" name="dataOldFotoProduto" type="hidden"/>
      <input idform="produtoServico" reference="cd_produto_servico" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0"/>
      <!--<input idform="produtoServico" reference="cd_classificacao_fiscal" id="cdClassificacaoFiscal" name="cdClassificacaoFiscal" type="hidden" value="0" defaultValue="0"/>-->
      <input idform="produtoServico" logmessage="Código Categoria Econômica" reference="cd_categoria" id="cdCategoria" name="cdCategoria" type="hidden"/>
      <input idform="produtoServico" reference="tp_produto_servico" id="tpProdutoServico" name="tpProdutoServico" type="hidden" value="<%=tpProdutoServico==-1 ? ProdutoServicoServices.TP_PRODUTO : tpProdutoServico%>" defaultValue="<%=tpProdutoServico==-1 ? ProdutoServicoServices.TP_PRODUTO : tpProdutoServico%>"/>
      <input idform="produtoServico" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>"/>
      <input id="cdFormularioDefault" name="cdFormularioDefault" type="hidden" value="<%=cdFormulario%>" defaultValue="<%=cdFormulario%>"/>
      <input idform="produtoServico" reference="cd_formulario" id="cdFormulario" name="cdFormulario" type="hidden" value="0" defaultValue="0"/>
	  <input idform="produtoServico" reference="cd_marca" id="cdMarca" name="cdMarca" type="hidden" value="0" defaultValue="0"/>
	  <input idform="produtoServico" reference="nm_modelo" id="nmModelo" name="nmModelo" type="hidden" value="" defaultValue=""/>
      <input idform="localArmazenamento" reference="cd_local_armazenamento" id="cdLocalArmazenamento" name="cdLocalArmazenamento" type="hidden" value="0" defaultvalue="0"/>
      <input idform="tributo" reference="cd_produto_servico_tributo" id="cdProdutoServicoTributo" name="cdProdutoServicoTributo" type="hidden" value="0" defaultValue="0"/>
      <input idform="preco" reference="cd_tabela_preco" id="cdTabelaPrecoOld" name="cdTabelaPrecoOld" type="hidden" value="0" defaultValue="0"/>
      <input idform="preco" reference="cd_produto_servico_preco" id="cdProdutoServicoPreco" name="cdProdutoServicoPreco" type="hidden" value="0" defaultValue="0"/>
      <input idform="fornecedor" reference="cd_fornecedor" id="cdFornecedorOld" name="cdFornecedorOld" type="hidden" value="0" defaultValue="0"/>
      <input logmessage="Código Composição" idform="componente" reference="cd_composicao" datatype="STRING" id="cdComposicao" name="cdComposicao" type="hidden" value="0" defaultValue="0"/>
      <input idform="" reference="" id="dataOldComponente" name="dataOldComponente" type="hidden"/>
      <input idform="produtoServico" reference="img_produto" id="imgProduto" name="imgProduto" type="hidden"/>
      <input idform="produtoServico" reference="img_foto" id="imgFoto" name="imgFoto" type="hidden"/>
      <div id="toolBar" class="d1-toolBar" style="height:30px; width: 890px;"></div>
      <div style="float:right; width:100px; height:90px; margin:0px 36px 0px 0px; class="d1-line">
	  	<div style="width: 100px;" class="element">
			<iframe id="imageProduto" style="overflow: hidden;border:1px solid #000000; background-color:#FFF; width:120px; _width:118px" height="90px" src="about:blank" frameborder="0"></iframe>
			<button idform="produtoServico" onclick="btLoadImagemOnClick()" style="margin:0px; left:0px; bottom:-16px; width:122px; font-size:9px" title="Limpar este campo..." class="controlButton"><img style="margin:0px 0px 0px -15px;" alt="X" src="/sol/imagens/filter-button.gif">Imagem</button>
		</div>
	  </div>
      <div id="divPrincipal">
	      <div class="d1-line" id="line1">
	      	<div style="width: 109px;" class="element">
              <label class="caption">Cód. Barras</label>
              <input style="width:104px;" logmessage="Código de Barras" class="field2" idform="produtoServico" reference="id_produto_servico" datatype="STRING" id="idProdutoServico" name="idProdutoServico" type="text"/>
            </div>
	        <div id="divTpProduto" style="width: 190px;" class="element">
              <label class="caption" for="tpOrigem">Tipo de Produto</label>
              <select style="width: 185px;" logmessage="Tipo de Produto" class="disabledSelect2" disabled="disabled" idform="produtoServico" reference="tp_produto" datatype="INT" id="tpProduto" name="tpProduto" defaultValue="0">
              </select>	
            </div>
	        <div id="divIdReduzido" style="width: 109px;" class="element">
              <label class="caption">ID</label>
              <input style="text-transform: uppercase; width: 90px;" lguppercase="true" logmessage="ID reduzido" class="field2" idform="produtoServico" reference="id_reduzido" datatype="STRING" maxlength="50" id="idReduzido" name="idReduzido" type="text"/>
        	  <button style="height: 22px;" idform="produtoServico" onclick="getNextIdReduzido(null);" id="btnNextIdReduzido" title="Gerar Número de Reduzido" class="controlButton"><img alt="|30|" src="/sol/imagens/reload-button.gif" width="15" height="15"></button>
            </div>
		    <div style="width: 109px; margin-left: 5px;" class="element">
		      <label class="caption" for="">Série</label>
		      <input lguppercase="true" style="text-transform: uppercase; width: 104px;" logmessage="Tamanho" class="field2" idform="produtoServico" reference="nr_serie" datatype="STRING" id="nrSerie" name="nrSerie" type="text"/>
		    </div>
		    <div style="width: 109px; " class="element">
		      <label class="caption" for="">ID Fábrica</label>
		      <input lguppercase="true" style="text-transform: uppercase; width: 104px;" logmessage="Tamanho" class="field2" idform="produtoServico" reference="id_fabrica" datatype="STRING" id="idFabrica" name="idFabrica" type="text"/>
		    </div>
		    <div style="width: 109px;" class="element">
              <label class="caption">Referência</label>
              <input style="width:109px;" logmessage="Referência" class="field2" idform="produtoServico" reference="nr_referencia" datatype="STRING" id="nrReferencia" name="nrReferencia" type="text"/>
            </div>
          </div>
	      <div class="d1-line" id="line3">
	      	<div id="divNmProdutoServico" style="width: 577px;" class="element">
	              <label class="caption" for="nmProdutoServico">Descrição</label>
	              <input lguppercase="true" style="text-transform: uppercase; width: 572px;" logmessage="Nome Produto" class="field2" idform="produtoServico" reference="nm_produto_servico" datatype="STRING" maxlength="256" id="nmProdutoServico" name="nmProdutoServico" type="text" />
            </div>
	      	<div style="width:85px;" class="element">
	          <label for="dtCadastro" class="caption">Data Cadastro</label>
	          <input name="dtCadastro" type="text" class="disabledField2" idform="" disabled="disabled" id="dtCadastro" reference="dt_cadastro" mask="dd/mm/yyyy" maxlength="10" style="width:80px; "/>
	        </div>
	      	<div style="width:85px;" class="element">
	          <label for="dtUltimaAlteracao" class="caption">Última Alteração</label>
	          <input name="dtUltimaAlteracao" type="text" class="disabledField2" idform="" disabled="disabled" id="dtUltimaAlteracao" reference="dt_ultima_alteracao" mask="dd/mm/yyyy" maxlength="10" style="width:80px; " value=""/>
	        </div>
	      	<div style="width: 524px;" class="element">
		        <label class="caption" for="">Descrição Reduzida</label>
		        <input lguppercase="true" style="text-transform: uppercase; width: 519px;" logmessage="Tamanho" class="field2" idform="produtoServico" reference="txt_produto_servico" datatype="STRING" id="txtProdutoServico" name="txtProdutoServico" type="text"/>
		    </div>
		    <div style="width: 109px;" class="element">
	          <label class="caption">Estoque Atual</label>
	          <input style="width: 104px; text-align:right;" readonly="readonly" static="true" logmessage="Qtd. por embalagem" mask="#,####" defaultvalue="0" class="disabledField2" idform="produtoServico" reference="qt_estoque" datatype="INT" id="qtEstoque" name="qtEstoque" type="text"/>
	        </div>
            <div id="divStProdutoEmpresa" style="width: 20px;" class="element">
            	<label class="caption" for="stProdutoEmpresa">&nbsp;</label>
            	<input name="stProdutoEmpresa" type="checkbox" id="stProdutoEmpresa" onchange="onChangeStProdutoEmpresa(null, this.checked)" value="1" checked="checked" logmessage="Ativo?"  idform="produtoServico" reference="st_produto_empresa"/>
            </div>
            <div id="divStProdutoEmpresa2" style="width: 25px;" class="element">
            	<label class="caption">&nbsp;</label>
            	<label style="margin:2px 0px 0px 0px" class="caption">Ativo</label>
            </div>
	      </div>
	  </div>
      <div id="divTabProdutoServico"  style="margin-top:80px;">
      	<div id="divAbaPesquisa" style="display:none;">
      		 <fieldset style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; float:left; width: 130px; height: 45px;">
            	  <legend>Códigos</legend> 
	             <div style="width: 62px;" class="element">
			        <label class="caption" for="">De</label>
			        <input style="width: 60px;" class="field2" idform="pesquisa" reference="codigo_inicial" datatype="INTEGER" id="codigoInicial" name="codigoInicial" type="text"/>
			      </div>
			      <div style="width: 65px;" class="element">
			      	<label class="caption" for="">A</label>
			      	<input lguppercase="true" style="text-transform: uppercase; width: 60px;" logmessage="Tamanho" class="field2" idform="pesquisa" reference="codigo_final" datatype="STRING" id="codigoFinal" name="codigoFinal" type="text"/>
			      </div>
		      </fieldset>
		      <fieldset style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; float:left; width: 130px; height: 45px;">
            	  <legend>Iniciais</legend> 
			      <div style="width: 52px;" class="element">
			        <label class="caption" for="">De</label>
			        <input lguppercase="true" style="text-transform: uppercase; width: 50px;" logmessage="Tamanho" class="field2" idform="pesquisa" reference="letra_inicial" datatype="STRING" id="letraInicial" name="letraInicial" type="text"/>
			      </div>
			      <div style="width: 55px;" class="element">
			      <label class="caption" for="">A</label>
			      	<input lguppercase="true" style="text-transform: uppercase; width: 50px;" logmessage="Tamanho" class="field2" idform="pesquisa" reference="letra_final" datatype="STRING" id="letraFinal" name="letraFinal" type="text"/>
			      </div>
		      </fieldset>
	          <div style="width: 65px; margin-top:18px;" class="element">
		        <label class="caption" for="">Quantidade</label>
		        <input lguppercase="true" style="text-transform: uppercase; width: 60px;" logmessage="Tamanho" class="field2" idform="pesquisa" reference="quantidade_pesquisa" datatype="STRING" id="quantidadePesquisa" name="quantidadePesquisa" type="text" defaultValue="20"/>
		      </div>
		      <div style="width: 135px; margin-top:53px;" class="element">
		      	<button style="width: 130px; height: 22px; padding-bottom: 5px;" id="loadPesquisa" name="loadPesquisa" onclick="loadPesquisa()" idform="pesquisa" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16" style="margin-top:-3px; left:13px;" />Pesquisar</button>
	          </div>
	          <div style="width: 880px;" class="element">
               	<label class="caption">Produtos</label>
               	<div id="divGridPesquisa" style="width: 875px; background-color:#FFF; height:190px; border:1px solid #000000">&nbsp;</div>
              </div>
      	</div>
        <div id="divAbaDadosBasicos" style="display:none;">
          <div class="d1-line" id="line1">
            <div style="width: 50px;" class="element">
              <label class="caption">SubGrupo</label>
              <input logmessage="Id do grupo" idform="produtoServico" reference="id_grupo" class="field2" style="width: 47px;" id="idGrupo" name="idGrupo" type="text" onblur="btnFindCdGrupoOnClick(this.value);"/>
            </div>
            <div id="divCdGrupoView" style="width: 832px;" class="element">
              <input idform="produtoServico" reference="cd_grupo" id="cdGrupo" name="cdGrupo" type="hidden" value="0" defaultvalue="<%=cdGrupo%>"/>	
              <label class="caption" for="cdGrupoView">&nbsp;</label>
              <input logmessage="Nome grupo" idform="produtoServico" reference="nm_grupo" style="width: 797px;" static="true" disabled="disabled" class="disabledField2" name="cdGrupoView" id="cdGrupoView" type="text"/>
              <button style="height: 22px;" id="btnFindGrupo" name="btnFindGrupo" onclick="btnFindGrupoOnClick()" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
              <button style="height: 22px;" onclick="btnClearGrupoOnClick()" idform="grupo" title="Excluir este grupo para o produto atual..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
            </div>
		  </div>
		  <div class="d1-line" id="line3">
			  <div id="divCategoriasElement" style="width: 870px;" class="element">
	              <label class="caption">Grupos:</label>
	              <div id="divGridGrupo" style="width: 870px; height: 78px; background-color:#FFF; border:1px solid #000000"></div>
	          </div>
	      </div>
	      <div class="d1-line" id="line4">
	          <div id="divCdFabricante" style="width: 435px;" class="element">
	              <label class="caption" for="cdFabricante">Fabricante</label>
	              <input logmessage="Código Fabricante" idform="produtoServico" reference="cd_fabricante" datatype="STRING" id="cdFabricante" name="cdFabricante" type="hidden" value="0" defaultValue="0"/>
	              <input logmessage="Nome Fabricante" idform="produtoServico" reference="nm_fabricante" style="width: 400px;" static="true" disabled="disabled" class="disabledField2" name="cdFabricanteView" id="cdFabricanteView" type="text"/>
	              <button style="height: 22px;"onclick="btnFindFabricanteOnClick()" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
	              <button style="height: 22px;"onclick="btnClearFabricanteOnClick()" idform="produtoServico" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
	          </div>
	          <div id="divCdUltimoFornecedor" style="width: 430px; margin-left: 5px;" class="element">
	              <label class="caption" for="cdUltimoFornecedor">Último Fornecedor</label>
	              <input logmessage="Código Ultimo Fornecedor" idform="produtoServico" reference="cd_ultimo_fornecedor" datatype="STRING" id="cdUltimoFornecedor" name="cdUltimoFornecedor" type="hidden" value="0" defaultValue="0"/>
	              <input logmessage="Nome Ultimo Fornecedor" idform="produtoServico" reference="nm_ultimo_fornecedor" style="width: 430px;" static="true" disabled="disabled" class="disabledField2" name="cdUltimoFornecedorView" id="cdUltimoFornecedorView" type="text"/>
	          </div>
	      </div>
          <div class="d1-line" id="line5">
	          <div id="divTpOrigem" style="width: 321px;" class="element">
	              <label class="caption" for="tpOrigem">Origem</label>
	              <select style="width: 316px;" logmessage="Tipo de Origem" class="select2" idform="produtoServico" reference="tp_origem" datatype="INT" id="tpOrigem" name="tpOrigem" defaultValue="0">
	                <option value="0">Nacional</option>
	                <option value="1">Estrangeira - Importação Direta</option>
	                <option value="2">Estrangeira - Mercado interno</option>
	              </select>	
	          </div>
	          <div id="divCdClassificacaoFiscal" style="width: 552px;" class="element">
              <label class="caption" for="cdClassificacaoFiscal">Classificação Fiscal</label>
	              <select style="width: 552px;" logmessage="Situação Tributária" class="select2" idform="produtoServico" reference="cd_classificacao_fiscal" datatype="STRING" id="cdClassificacaoFiscal" name="cdClassificacaoFiscal" defaultValue="0">
	                <option value="0">Selecione...</option>
	              </select>	
              </div>
          </div>
          <div class="d1-line" id="line6">
            <div style="width: 90px;" class="element">
              <label class="caption">Nr. NCM</label>
              <input style="width: 85px;" logmessage="Numero NCM" class="field2" idform="produtoServico" reference="nr_ncm" datatype="INT" id="nrNcm" name="nrNcm" type="text" onblur="btnFindNrNcmOnClick(this.value);" />
            </div>
            <div style="width: 785px;" class="element">
               <label class="caption" for="cdNcm">NCM</label>
               <input reference="cd_ncm" idform="produtoServico" disabled="disabled" name="cdNcm" id="cdNcm" type="hidden"/>
               <input logmessage="Ncm" reference="nm_ncm" style="width: 750px;" disabled="disabled" class="disabledField2" name="nmNcm" id="nmNcm" idform="produtoServico" type="text"/>
               <button style="height: 22px;" id="btnFindNcm" name="btnFindNcm" onclick="btnFindNcmOnClick()" title="Pesquisar valor para este campo..."  idform="produtoServico" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
               <button style="height: 22px;" id="btnCleanCentroCusto" onclick="$('nrNcm').value = ''; $('cdNcm').value = ''; $('nmNcm').value = '';"  idform="produtoServico" title="Excluir esta ncm..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
            </div>
          </div>
          <div class="d1-line" id="line7">
          	<div style="width: 110px;" class="element">
              <label class="caption" for="vlUltimoCusto">Prazo de Validade</label>
              <input style="width: 105px; text-align:right;" static="static" disabled="disabled" mask="#,###.00" logmessage="Último custo" class="disabledField2" idform="produtoServico" reference="vl_ultimo_custo_2" datatype="FLOAT" maxlength="10" id="vlUltimoCusto2" name="vlUltimoCusto2" type="text"/>
            </div>
          	<div style="width: 110px;" class="element">
              <label class="caption" for="vlUltimoCusto">Último custo</label>
              <input style="width: 105px; text-align:right;" static="static" disabled="disabled" mask="#,###.00" logmessage="Último custo" class="disabledField2" idform="produtoServico" reference="vl_ultimo_custo" datatype="FLOAT" maxlength="10" id="vlUltimoCusto" name="vlUltimoCusto" type="text"/>
            </div>
            <div style="width: 105px;" class="element">
              <label class="caption" for="vlCustoMedio">Custo médio</label>
              <input style="width: 100px; text-align:right;" static="static" disabled="disabled" mask="#,###.00" logmessage="Custo médio" class="disabledField2" idform="produtoServico" reference="vl_custo_medio" datatype="FLOAT" maxlength="10" id="vlCustoMedio" name="vlCustoMedio" type="text"/>
            </div>
            <div style="width: 70px;" class="element">
              <label class="caption" for="qtPrecisaoUnidade">Precisão Un.</label>
              <input style="width: 67px;" mask="##" logmessage="Precisão setor" class="field2" idform="produtoServico" reference="qt_precisao_unidade" datatype="INT" id="qtPrecisaoUnidade" name="qtPrecisaoUnidade" defaultValue="2" value="2" type="text"/>
            </div>
            <div id="divCdUnidadeMedidaCompra" style="width: 296px; " class="element">
              <label class="caption" for="cdUnidadeMedidaCompra">Unidade de Medida</label>
              <select style="width: 291px;" logmessage="Unidade" class="select2" idform="produtoServico" reference="cd_unidade_medida" datatype="STRING" id="cdUnidadeMedida" name="cdUnidadeMedida" defaultValue="0">
                <option value="0">Selecione...</option>
              </select>
            </div>
            <div style="width: 94px; " class="element">
              <label class="caption" for="">Tamanho</label>
              <input lguppercase="true" style="text-transform: uppercase; width: 89px;" logmessage="Tamanho" class="field2" idform="produtoServico" reference="txt_dado_tecnico" datatype="STRING" id="txtDadoTecnico" name="txtDadoTecnico" type="text"/>
            </div>
            <div style="width: 90px; " class="element">
              <label class="caption" for="">Cor</label>
              <input lguppercase="true" style="text-transform: uppercase; width: 90px;" logmessage="Cor" class="field2" idform="produtoServico" reference="txt_especificacao" datatype="STRING" id="txtEspecificacao" name="txtEspecificacao" type="text"/>
            </div>
		  </div>
        </div>
        <div id="divAbaEstoque" style="display:none;">
        	<div class="element">
	       	    <div style="width: 860px;" class="element">
	               <label class="caption">Estoque</label>
	               <div id="divGridEstoque" style="width: 885px; background-color:#FFF; height:252px; border:1px solid #000000">&nbsp;</div>
	            </div>
		    </div>
        </div>
        <div id="divAbaComposicao" style="display:none;">
            <div class="d1-line" id="line1">
              <div style="width: 350px;" class="element">
                <label class="caption" for="cdProdutoServicoComponente">Produto / Serviço / Referência</label>
                <input logmessage="Código Componente" idform="componente" reference="cd_produto_servico_componente" datatype="STRING" id="cdProdutoServicoComponente" name="cdProdutoServicoComponente" type="hidden" value="0" defaultValue="0">
                <input logmessage="Nome Componente" idform="componente" reference="nm_produto_servico_componente" style="width: 315px;" static="true" disabled="disabled" class="disabledField2" name="cdProdutoServicoComponenteView" id="cdProdutoServicoComponenteView" type="text">
                <input logmessage="Código Referência" idform="componente" reference="cd_referencia" datatype="STRING" id="cdReferencia" name="cdReferencia" type="hidden" value="0" defaultValue="0">
                <button style="height: 22px;" idform="componente" onclick="btnFindComponenteOnClick(null, true)" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                <button style="height: 22px;" idform="componente" onclick="$('cdProdutoServicoComponente').value=0; $('cdProdutoServicoComponenteView').value='';" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
              </div>
              <div style="width: 80px;" class="element">
                <label class="caption" for="idComponente">ID</label>
                <input static="static" disabled="disabled" style="width: 75px;" logmessage="ID Componente" class="disabledField2" idform="componente" reference="id_produto_servico_componente" datatype="STRING" maxlength="10" id="idComponente" name="idComponente" type="text" />
              </div>
              <div style="width: 200px;" class="element">
                 <label class="caption" for="cdUnidadeMedida">Unidade</label>
                 <select style="width: 195px;" logmessage="Unidade" class="select2" idform="componente" reference="cd_unidade_medida_componente" datatype="STRING" id="cdUnidadeMedidaComponente" name="cdUnidadeMedidaComponente" defaultValue="0">
                   <option value="0">Selecione...</option>
                 </select>
               </div>
               <div style="width: 60px;" class="element">
                 <label class="caption" for="qtProdutoServico">Quantidade</label>
                 <input style="width: 55px; text-align:right;" mask="#,###.00" logmessage="Quantidade" class="field2" idform="componente" reference="qt_produto_servico" datatype="FLOAT" maxlength="10" id="qtProdutoServico" name="qtProdutoServico" type="text" value="0" defaultValue="0">
               </div>
               <div style="width: 47px;" class="element">
                 <label class="caption" for="prPerda">% Perda</label>
                 <input style="width: 42px; text-align:right;" mask="#,###.00" logmessage="% Perda" class="field2" idform="componente" reference="pr_perda" datatype="FLOAT" maxlength="10" id="prPerda" name="prPerda" type="text" value="0" defaultValue="0">
               </div>
               <div style="width: 20px;" class="element">
                 <label class="caption" for="lgCalculoCusto">&nbsp;</label>
                 <input logmessage="Entra no cálculo do custo?" idform="componente" reference="lg_calculo_custo" id="lgCalculoCusto" name="lgCalculoCusto" type="checkbox" checked="checked" value="1"/>
               </div>
               <div style="width: 70px;" class="element">
                 <label class="caption">&nbsp;</label>                  
                 <label style="margin:2px 0px 0px 0px" class="caption">Cálculo custo</label>
               </div>
            </div>
            <div class="d1-line" id="line2">
               <div style="width: 437px;" class="element">
                 <label class="caption" for="txtProdutoServicoComponente">Descri&ccedil;&atilde;o</label>
                 <textarea disabled="disabled" static="static" logmessage="Descrição Componente" style="width: 432px; height:36px" class="disabledField" idform="componente" reference="txt_produto_servico_componente" datatype="STRING" id="txtProdutoServicoComponente" name="txtProdutoServicoComponente"></textarea>
               </div>
               <div class="element">
           			<div class="d1-toolBar" id="toolBarComponentes" style="width:440px; height:36px; margin-top:14px;"></div>
           	   </div>
            </div>
            <div class="d1-line" id="line3">
             <div id="divGridComposicaoSuperior" style="width: 800px;" class="element">
               <label class="caption">Componentes:</label>
               <div id="divGridComposicao" style="width: 875px; height: 165px; background-color:#FFF; border:1px solid #000000"></div>
             </div>
            </div>
        </div>
        <div id="divAbaSubstituto" style="display:none;">
        	<div class="element">
       			<div class="d1-toolBar" id="toolBarSubstituto" style="width:880px; height:36px; margin-top:5px;"></div>
       	    </div>
       	    <div class="element">
	       	    <div id="divAbaSubstitutoAba">
		        	<div id="divAbaSubstitutoPesquisa" style="display:none;">
		        		<div style="width: 860px;" class="element">
			               <label class="caption">Substitutos</label>
			               <div id="divGridSubstituto" style="width: 860px; background-color:#FFF; height:175px; border:1px solid #000000">&nbsp;</div>
			             </div>
			        </div>
		       		<div id="divAbaSubstitutoDados" style="display:none;">
		       			<input idform="substituto" reference="cd_produto_servico_substituto" id="cdProdutoServicoSubstituto" name="cdProdutoServicoSubstituto" type="hidden" value="0" defaultValue="0"/>
		       			<div id="divNmProdutoServicoSubstituto" style="width: 452px;" class="element">
			              <label class="caption" for="nmProdutoSimilarSubstituto">Nome</label>
			              <input lguppercase="true" style="text-transform: uppercase; width: 447px;" logmessage="Nome Produto" class="field2" idform="substituto" reference="nm_produto_similar" datatype="STRING" maxlength="256" id="nmProdutoSimilarSubstituto" name="nmProdutoSimilarSubstituto" type="text" />
			            </div>
			            <div id="divCdProdutoServicoSubstituto" style="width: 420px;" class="element">
				            <label class="caption" for="cdProdutoServicoSubstitutoView">Substituto</label>
							<input logmessage="Código Produto Similar" idform="substituto" reference="cd_produto_servico_similar" datatype="STRING" id="cdProdutoServicoSubstituto" name="cdProdutoServicoSubstituto" type="hidden" value="0" defaultValue="0"/>
							<input style="width: 395px;" logmessage="Nome Produto Substituto" reference="nm_produto_servico" idform="substituto" static="true" disabled="disabled" class="disabledField2" name="cdProdutoServicoSubstitutoView" id="cdProdutoServicoSubstitutoView" type="text"/>
							<button onclick="btnFindSubstitutoOnClick()" idform="substituto" title="Pesquisar valor para este campo..." class="controlButton controlButton3" style="margin-top:10px; height:22px;"><img alt="L" src="/sol/imagens/filter-button.gif"  /></button>
							<button onclick="btnClearSubstitutoOnClick()" idform="substituto" title="Limpar este campo..." class="controlButton controlButton2" style="margin-top:10px; height:22px;"><img alt="X" src="/sol/imagens/clear-button.gif" /></button>
				   		</div>
			        	<div style="width: 870px; " class="element">
				        	<label class="caption" for="">Descrição</label>
				        	<input lguppercase="true" style="text-transform: uppercase; width: 870px;" logmessage="Tamanho" class="field2" idform="substituto" reference="txt_descricao" datatype="STRING" id="txtDescricaoSubstituto" name="txtDescricaoSubstituto" type="text"/>
				      	</div>
				      	<div id="divTxtEspecificacaoSubstituto" style="width: 870px;" class="element">
			              <label class="caption" for="txtEspecificacaoSubstituto">Observações</label>
			              <textarea style="width: 870px; height: 105px;" logmessage="Observação" class="textarea" idform="substituto" reference="txt_especificacao" datatype="STRING" id="txtEspecificacaoSubstituto" name="txtEspecificacaoSubstituto"></textarea>
			            </div>
		       		</div>
		       	</div>
       		</div>
        </div>
        <div id="divAbaRelacionado" style="display:none;">
        	<div class="element">
       			<div class="d1-toolBar" id="toolBarRelacionado" style="width:880px; height:36px; margin-top:5px;"></div>
       	    </div>
       	    <div class="element">
       	    	<div id="divAbaRelacionadoAba">
		        	<div id="divAbaRelacionadoPesquisa" style="display:none;">
		        		<div style="width: 860px;" class="element">
			               <label class="caption">Relacionados</label>
			               <div id="divGridRelacionado" style="width: 860px; background-color:#FFF; height:175px; border:1px solid #000000">&nbsp;</div>
			            </div>
			        </div>
		       		<div id="divAbaRelacionadoDados" style="display:none;">
		       			<input idform="Relacionado" reference="cd_produto_servico_relacionado" id="cdProdutoServicoRelacionado" name="cdProdutoServicoRelacionado" type="hidden" value="0" defaultValue="0"/>
		       			<div id="divNmProdutoServicoRelacionado" style="width: 452px;" class="element">
			              <label class="caption" for="nmProdutoSimilarRelacionado">Nome</label>
			              <input lguppercase="true" style="text-transform: uppercase; width: 447px;" logmessage="Nome Produto" class="field2" idform="relacionado" reference="nm_produto_similar" datatype="STRING" maxlength="256" id="nmProdutoSimilarRelacionado" name="nmProdutoSimilarRelacionado" type="text" />
			            </div>
			            <div id="divCdProdutoServicoRelacionado" style="width: 420px;" class="element">
				            <label class="caption" for="cdProdutoServicoRelacionadoView">Relacionado</label>
							<input logmessage="Código Produto Similar" idform="relacionado" reference="cd_produto_servico_similar" datatype="STRING" id="cdProdutoServicoRelacionado" name="cdProdutoServicoRelacionado" type="hidden" value="0" defaultValue="0"/>
							<input style="width: 395px;" logmessage="Nome Produto Relacionado" reference="nm_produto_servico" idform="relacionado" static="true" disabled="disabled" class="disabledField2" name="cdProdutoServicoRelacionadoView" id="cdProdutoServicoRelacionadoView" type="text"/>
							<button onclick="btnFindRelacionadoOnClick()" idform="relacionado" title="Pesquisar valor para este campo..." class="controlButton controlButton3" style="margin-top:10px; height:22px;"><img alt="L" src="/sol/imagens/filter-button.gif"  /></button>
							<button onclick="btnClearRelacionadoOnClick()" idform="relacionado" title="Limpar este campo..." class="controlButton controlButton2" style="margin-top:10px; height:22px;"><img alt="X" src="/sol/imagens/clear-button.gif" /></button>
				   		</div>
			        	<div style="width: 870px; " class="element">
				        	<label class="caption" for="">Descrição</label>
				        	<input lguppercase="true" style="text-transform: uppercase; width: 870px;" logmessage="Tamanho" class="field2" idform="relacionado" reference="txt_descricao" datatype="STRING" id="txtDescricaoRelacionado" name="txtDescricaoRelacionado" type="text"/>
				      	</div>
				      	<div id="divTxtEspecificacaoRelacionado" style="width: 870px;" class="element">
			              <label class="caption" for="txtEspecificacaoRelacionado">Observações</label>
			              <textarea style="width: 870px; height: 105px;" logmessage="Observação" class="textarea" idform="relacionado" reference="txt_especificacao" datatype="STRING" id="txtEspecificacaoRelacionado" name="txtEspecificacaoRelacionado"></textarea>
			            </div>
		       		</div>
		       	</div>
<!-- 	       	    <div id="divAbaRelacionadoAba"> -->
<!-- 		        	<div id="divAbaRelacionadoPesquisa" style="display:none;"> -->
<!-- 		        		 <div style="width: 870px;" class="element"> -->
<!-- 			               <label class="caption">Relacionados</label> -->
<!-- 			               <div id="divGridRelacionados" style="width: 870px; background-color:#FFF; height:205px; border:1px solid #000000">&nbsp;</div> -->
<!-- 			             </div> -->
<!-- 		       		</div> -->
<!-- 		       		<div id="divAbaRelacionadoDados" style="display:none;"> -->
<!-- 		       			<input idform="relacionado" reference="cd_produto_servico_relacionado" id="cdProdutoServicoRelacionado" name="cdProdutoServicoRelacionado" type="hidden" value="0" defaultValue="0"/> -->
<!-- 		       			<div id="divNmProdutoServicoRelacionado" style="width: 452px;" class="element"> -->
<!-- 			              <label class="caption" for="nmProdutoServicoRelacionado">Nome</label> -->
<!-- 			              <input lguppercase="true" style="text-transform: uppercase; width: 449px;" logmessage="Nome Produto" class="field2" idform="relacionado" reference="nm_produto_servico" datatype="STRING" maxlength="256" id="nmProdutoServicoRelacionado" name="nmProdutoServicoRelacionado" type="text" /> -->
<!-- 			            </div> -->
<!-- 			            <div id="divStProdutoEmpresaRelacionado" style="width: 20px; margin-left:10px;" class="element"> -->
<!-- 			            	<label class="caption" for="stProdutoEmpresaRelacionado">&nbsp;</label> -->
<!-- 			            	<input name="stProdutoEmpresaRelacionado" type="checkbox" id="stProdutoEmpresaRelacionado" onchange="onChangeStProdutoEmpresa(this)" value="1" checked="checked" logmessage="Ativo?"  idform="relacionado" reference="st_produto_empresa"/> -->
<!-- 			            </div> -->
<!-- 			            <div id="divStProdutoEmpresa2Relacionado" style="width: 50px;" class="element"> -->
<!-- 			            	<label class="caption">&nbsp;</label> -->
<!-- 			            	<label style="margin:2px 0px 0px 0px" class="caption">Ativo</label> -->
<!-- 			            </div> -->
<!-- 			            <div style="width: 340px;" class="element"> -->
<!-- 			              <label class="caption" for="tpReabastecimentoRelacionado">Tipo reabastecimento</label> -->
<!-- 			              <select style="width: 340px;" logmessage="Tipo de reabastecimento" class="select2" idform="relacionado" reference="tp_reabastecimento" datatype="STRING" id="tpReabastecimentoRelacionado" name="tpReabastecimentoRelacionado" defaultValue="0"> -->
<!-- 			              </select> -->
<!-- 			            </div> -->
<!-- 			        	<div style="width: 870px; " class="element"> -->
<!-- 				        	<label class="caption" for="">Descrição</label> -->
<!-- 				        	<input lguppercase="true" style="text-transform: uppercase; width: 870px;" logmessage="Tamanho" class="field2" idform="relacionado" reference="txt_produto_servico" datatype="STRING" id="txtProdutoServicoRelacionado" name="txtProdutoServicoRelacionado" type="text"/> -->
<!-- 				      	</div> -->
<!-- 				      	<div id="divTxtEspecificacaoRelacionado" style="width: 870px;" class="element"> -->
<!-- 			              <label class="caption" for="txtEspecificacaoRelacionado">Observações</label> -->
<!-- 			              <textarea style="width: 870px; height: 135px;" logmessage="Observações" class="textarea" idform="relacionado" reference="txt_especificacao" datatype="STRING" id="txtEspecificacaoRelacionado" name="txtEspecificacaoRelacionado"></textarea> -->
<!-- 		            	</div> -->
<!-- 		       		</div> -->
<!-- 	       		</div> -->
       		</div>
       	</div>
        <div id="divAbaReabastecimento" style="display:none;">
        	<div class="element">
       			<div class="d1-toolBar" id="toolBarReabastecimento" style="width:880px; height:36px; margin-top:5px;"></div>
       	    </div>
        	 <div class="element">
	       	    <div id="divAbaReabastecimentoAba">
		        	<div id="divAbaReabastecimentoPesquisa" style="display:none;">
		        		<div style="width: 860px;" class="element">
			               <label class="caption">Reabastecimentos</label>
			               <div id="divGridReabastecimento" style="width: 860px; background-color:#FFF; height:175px; border:1px solid #000000">&nbsp;</div>
			            </div>
		       		</div>
		       		<div id="divAbaReabastecimentoDados" style="display:none;">
		       			<div style="width: 435px;" class="element">
			              	<label class="caption" for="cdLocalArmazenamentoDestino">Local de Armazenamento Destino</label>
							<input logmessage="Código Local Armazenamento Destino" idform="reabastecimento" reference="cd_local_armazenamento" id="cdLocalArmazenamentoDestino" name="cdLocalArmazenamentoDestino" type="hidden" value="0" defaultValue="0"/>
							<input style="width: 400px;" logmessage="Nome Local de Armazenamento" reference="nm_local_armazenamento" idform="reabastecimento" static="true" disabled="disabled" class="disabledField" name="cdLocalArmazenamentoDestinoView" id="cdLocalArmazenamentoDestinoView" type="text"/>
							<button id="btnFindCdLocalArmazenamentoDestino" onclick="btnFindCdLocalArmazenamentoDestinoOnClick()" idform="reabastecimento" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
							<button onclick="btnClearLocalArmazenamentoDestinoOnClick()" idform="reabastecimento" title="Limpar este campo..." class="controlButton" onfocus="focusToElement('nmLocalArmazenamentoDestinoView')"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			            </div>
			            <div style="width: 435px;" class="element">
			              <label class="caption" for="cdLocalArmazenamentoOrigem">Local de Armazenamento Origem</label>
							<input logmessage="Código Local Armazenamento Origem" idform="reabastecimento" reference="cd_local_armazenamento_origem" id="cdLocalArmazenamentoOrigem" name="cdLocalArmazenamentoOrigem" type="hidden" value="0" defaultValue="0"/>
							<input style="width: 400px;" logmessage="Nome Local de Armazenamento" reference="nm_local_armazenamento_origem" idform="reabastecimento" static="true" disabled="disabled" class="disabledField" name="cdLocalArmazenamentoOrigemView" id="cdLocalArmazenamentoOrigemView" type="text"/>
							<button id="btnFindCdLocalArmazenamentoOrigem" onclick="btnFindCdLocalArmazenamentoOrigemOnClick()" idform="reabastecimento" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
							<button onclick="btnClearLocalArmazenamentoOrigemOnClick()" idform="reabastecimento" title="Limpar este campo..." class="controlButton" onfocus="focusToElement('nmLocalArmazenamentoOrigemView')"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			            </div>
			            <div style="width: 170px;" class="element">
			              <label class="caption" for="tpAbastecimento">Tipo Abastecimento</label>
			              <select style="width: 165px;" logmessage="Tipo de abastecimento" class="select2" idform="reabastecimento" reference="tp_abastecimento" datatype="STRING" id="tpAbastecimento" name="tpAbastecimento" defaultValue="0">
			              </select>
			            </div>
			            <div id="divQtTransferir" style="width: 130px;" class="element">
			              <label class="caption" for="qtTransferencia">Quantidade a Transferir</label>
			              <input lguppercase="true" style="text-transform: uppercase; width: 125px;" logmessage="Quantidade a Transferir" class="field2" idform="reabastecimento" reference="qt_transferencia" datatype="FLOAT" id="qtTransferencia" name="qtTransferencia" type="text" />
			            </div>
			            <div id="divNmNivelEstoqueMinimo" style="width: 190px;" class="element">
			              <label class="caption" for="qtMinimaReabastecimento">Nível de Estoque Mínimo</label>
			              <input lguppercase="true" style="text-transform: uppercase; width: 185px;" logmessage="Nível de Estoque Mínimo" class="field2" idform="reabastecimento" reference="qt_minima" datatype="FLOAT" id="qtMinimaReabastecimento" name="qtMinimaReabastecimento" type="text" />
			            </div>
			            <div id="divNmNivelEstoqueMaximo" style="width: 190px;" class="element">
			              <label class="caption" for="qtMaximaReabastecimento">Nível de Estoque Máximo</label>
			              <input lguppercase="true" style="text-transform: uppercase; width: 185px;" logmessage="Nível de Estoque Máximo" class="field2" idform="reabastecimento" reference="qt_maxima" datatype="FLOAT" id="qtMaximaReabastecimento" name="qtMaximaReabastecimento" type="text" />
		            	</div>
		            	<div id="divNmNivelEstoqueMaximo" style="width: 190px;" class="element">
			              <label class="caption" for="qtIdealReabastecimento">Nível de Estoque Ideal</label>
			              <input lguppercase="true" style="text-transform: uppercase; width: 185px;" logmessage="Nível de Estoque Ideal" class="field2" idform="reabastecimento" reference="qt_ideal" datatype="FLOAT" id="qtIdealReabastecimento" name="qtIdealReabastecimento" type="text" />
		            	</div>
		            	<div id="divStEstoque" style="width: 20px;" class="element">
			            	<label class="caption" for="stEstoqueReabastecimento">&nbsp;</label>
			            	<input name="stEstoqueReabastecimento" type="checkbox" id="stEstoqueReabastecimento" value="1" checked="checked" logmessage="Ativo?"  idform="reabastecimento" reference="st_estoque"/>
			            </div>
			            <div id="divStEstoque2" style="width: 50px;" class="element">
			            	<label class="caption">&nbsp;</label>
			            	<label style="margin:2px 0px 0px 0px" class="caption">Ativo</label>
			            </div>
		       		</div>
	       		</div>
       		</div>
        </div>
        <div id="divAbaConfigFinanceira">
          <input idform="preco" reference="cd_produto_servico_preco" id="cdProdutoServicoPreco" name="cdProdutoServicoPreco" type="hidden" value="0" defaultValue="0"/>	
          <div style="width: 850px;" class="element">
	          <label class="caption" for="cdTabelaPreco">Lista de Pre&ccedil;o</label>
	          <select style="width: 850px;" logmessage="Tabela de Preço" registerclearlog="0" defaultValue="0" class="select2" idform="preco" reference="cd_tabela_preco" maxlength="10" id="cdTabelaPreco" name="cdTabelaPreco">
	            <option value="0">Selecione...</option>
	          </select>
	      </div>	
          <div style="width: 105px;" class="element">
            <label class="caption" for="vlCustoProduto">Custo do Produto</label>
            <input reference="vl_custo_produto" style="width: 100px;" mask="#,####.00" logmessage="Custo do produto" class="disabledField" disabled="disabled" static="true" idform="preco" datatype="FLOAT" maxlength="10" id="vlCustoProduto" name="vlCustoProduto" type="text"/>
          </div>
          <div style="width: 105px;" class="element">
            <label class="caption" for="vlPrecoCustoProduto">Preço Base</label>
            <input reference="vl_preco_base" style="width: 100px;"  mask="#,###.00" logmessage="Preço base do produto" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="vlPrecoCustoProduto" name="vlPrecoCustoProduto" type="text"/>
          </div>
          <div style="width: 105px;" class="element">
            <label class="caption" for="prMargemPrecoCustoProduto">Margem %</label>
            <input reference="pr_margem_custo_produto" style="width: 100px;" onblur='calcularPrecoPelaPorcentagem("Base")' mask="#,###.00" logmessage="Margem de lucro em porcentagem" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="prMargemPrecoCustoProduto" name="prMargemPrecoCustoProduto" type="text"/>
          </div>
          <div style="width: 110px;" class="element">
	        <label class="caption" for="vlMargemPrecoCustoProduto">Margem R$</label>
	        <input reference="vl_margem_custo_produto" style="width: 100px;" onblur='calcularPrecoPeloValor("Base")' mask="#,###.00" logmessage="Margem de lucro em moeda" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="vlMargemPrecoCustoProduto" name="vlMargemPrecoCustoProduto" type="text"/>
          </div>
          <div style="width: 105px;" class="element">
		    <label class="caption" for="vlPrecoMinimo">Preço Mínimo</label>
		    <input reference="vl_preco_minimo" style="width: 100px;" mask="#,####.00" logmessage="Custo do produto" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="vlPrecoMinimo" name="vlPrecoMinimo" type="text"/>
		  </div>
		  <div style="width: 105px;" class="element">
		    <label class="caption" for="prMargemPrecoMinimo">Margem %</label>
		    <input reference="pr_margem_preco_minimo" style="width: 100px;" onblur='calcularPrecoPelaPorcentagem("Minimo")' mask="#,###.00" logmessage="Margem de lucro em porcentagem" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="prMargemPrecoMinimo" name="prMargemPrecoMinimo" type="text"/>
		  </div>
		  <div style="width: 105px;" class="element">
		    <label class="caption" for="vlMargemPrecoMinimo">Margem R$</label>
		    <input reference="vl_margem_preco_minimo" style="width: 100px;" onblur='calcularPrecoPeloValor("Minimo")' mask="#,###.00" logmessage="Margem de lucro em moeda" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="vlMargemPrecoMinimo" name="vlMargemPrecoMinimo" type="text"/>
		  </div>
		  <div style="width: 110px;" class="element">
		    <label class="caption" for="vlDiferencaPrecoMinimo">Desconto/Acrescimo</label>
		    <input reference="vl_diferenca_preco_minimo" style="width: 100px;" mask="#,###.00" logmessage="Desconto/Acrescimo para o preço do produto" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="vlDiferencaPrecoMinimo" name="vlDiferencaPrecoMinimo" type="text"/>
		  </div>
          <div style="width: 105px;" class="element">
		    <label class="caption" for="vlPrecoPadrao">Preço Padrão</label>
		    <input reference="vl_preco" style="width: 100px;" mask="#,####.00" logmessage="Custo do produto" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="vlPrecoPadrao" name="vlPrecoPadrao" type="text"/>
		  </div>
		  <div style="width: 105px;" class="element">
		    <label class="caption" for="prMargemPrecoPadrao">Margem %</label>
		    <input reference="pr_margem_preco_padrao" style="width: 100px;" onblur='calcularPrecoPelaPorcentagem("Padrao")' mask="#,###.00" logmessage="Margem de lucro em porcentagem" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="prMargemPrecoPadrao" name="prMargemPrecoPadrao" type="text"/>
		  </div>
		  <div style="width: 105px;" class="element">
		    <label class="caption" for="vlMargemPrecoPadrao">Margem R$</label>
		    <input reference="vl_margem_preco_padrao" style="width: 100px;" onblur='calcularPrecoPeloValor("Padrao")' mask="#,###.00" logmessage="Margem de lucro em moeda" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="vlMargemPrecoPadrao" name="vlMargemPrecoPadrao" type="text"/>
		  </div>
		  <div style="width: 110px;" class="element">
		    <label class="caption" for="vlDiferencaPrecoPadrao">Desconto/Acrescimo</label>
		    <input reference="vl_diferenca_preco_padrao" style="width: 100px;" mask="#,###.00" logmessage="Desconto/Acrescimo para o preço do produto" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="vlDiferencaPrecoPadrao" name="vlDiferencaPrecoPadrao" type="text"/>
		  </div>
          <div style="width: 105px;" class="element">
		    <label class="caption" for="vlPrecoMaximo">Preço Maximo</label>
		    <input reference="vl_preco_maximo" style="width: 100px;" mask="#,####.00" logmessage="Custo do produto" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="vlPrecoMaximo" name="vlPrecoMaximo" type="text"/>
		  </div>
		  <div style="width: 105px;" class="element">
		    <label class="caption" for="prMargemPrecoMaximo">Margem %</label>
		    <input reference="pr_margem_preco_maximo" style="width: 100px;" onblur='calcularPrecoPelaPorcentagem("Maximo")' mask="#,###.00" logmessage="Margem de lucro em porcentagem" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="prMargemPrecoMaximo" name="prMargemPrecoMaximo" type="text"/>
		  </div>
		  <div style="width: 105px;" class="element">
		    <label class="caption" for="vlMargemPrecoMaximo">Margem R$</label>
		    <input reference="vl_margem_preco_maximo" style="width: 100px;" onblur='calcularPrecoPeloValor("Maximo")' mask="#,###.00" logmessage="Margem de lucro em moeda" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="vlMargemPrecoMaximo" name="vlMargemPrecoMaximo" type="text"/>
		  </div>
		  <div style="width: 110px;" class="element">
		    <label class="caption" for="vlDiferencaPrecoMaximo">Desconto/Acrescimo</label>
		    <input reference="vl_diferenca_preco_maximo" style="width: 100px;" mask="#,###.00" logmessage="Desconto/Acrescimo para o preço do produto" class="field" idform="preco" datatype="FLOAT" maxlength="10" id="vlDiferencaPrecoMaximo" name="vlDiferencaPrecoMaximo" type="text"/>
		  </div>
          <div id="divAbaEspecificacoes2">
           <div class="d1-line">
             <div style="width: 850px;" class="element">
               <label class="caption">Pre&ccedil;os</label>
               <div id="divGridPrecos" style="width: 850px; background-color:#FFF; height:155px; border:1px solid #000000">&nbsp;</div>
             </div>
             <div style="width: 20px;" class="element">
               <label class="caption">&nbsp;</label>
                 <button title="Novo Preço" onclick="btnNewPrecoOnClick();" style="margin-bottom:2px; margin-left:3px;" id="btnNewPreco" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
                 <button title="Salvar Preço" onclick="btnSalvarPrecoOnClick();" style="margin-bottom:2px; margin-left:3px;" id="btnSalvarPreco" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/></button>
                 <button title="Alterar Preço" onclick="btnAlterPrecoOnClick();" style="margin-bottom:2px; margin-left:3px;" id="btnAlterPreco" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
                 <button title="Excluir Preço" onclick="btnDeletePrecoOnClick();" style="margin-left:3px;" id="btnDeletePreco" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
             </div>
           </div>
          </div>
        </div>
<!--         <div id="divAbaConfigFinanceira"> -->
<!--           <div id="divAbaEspecificacoes2"> -->
<!--            <div class="d1-line"> -->
<!--              <div style="width: 850px;" class="element"> -->
<!--                <label class="caption">Pre&ccedil;os</label> -->
<!--                <div id="divGridPrecos" style="width: 850px; background-color:#FFF; height:245px; border:1px solid #000000">&nbsp;</div> -->
<!--              </div> -->
<!--              <div style="width: 20px;" class="element"> -->
<!--                <label class="caption">&nbsp;</label> -->
<!--                  <button title="Novo Preço" onclick="btnNewPrecoOnClick();" style="margin-bottom:2px; margin-left:3px;" id="btnNewPreco" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button> -->
<!--                  <button title="Alterar Preço" onclick="btnAlterPrecoOnClick();" style="margin-bottom:2px; margin-left:3px;" id="btnAlterPreco" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button> -->
<!--                  <button title="Excluir Preço" onclick="btnDeletePrecoOnClick();" style="margin-left:3px;" id="btnDeletePreco" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button> -->
<!--              </div> -->
<!--            </div> -->
<!--           </div> -->
<!--         </div> -->
        <div id="divAbaDadosComplementares" style="display:none;">
        	<div id="divAbaParametros" style="display:none;">
        		<div class="d1-line">
<!--         			<div class="element" style="width: 370px;" > -->
<!-- 		       			<div class="d1-toolBar" id="toolBarParametros" style="width:350px; height:36px; margin-top:5px;"></div> -->
<!-- 		       	    </div> -->
		       	    <input idform="parametros" reference="cd_parametro" id="cdParametro" name="cdParametro" type="hidden" value="0" defaultValue="0"/>
	        		<div id="divStProdutoEmpresaSubstituto3" style="width: 870px;" class="element">
		            	<input type="checkbox" id="lgVerificarEstoqueNaVenda" reference="lg_verificar_estoque_na_venda" idForm="produtoServico" style="vertical-align: middle;"/><span class="checkPersonalizado">Verificar Estoque na Venda</span>
		            </div>
		            <div id="divStProdutoEmpresaSubstituto" style="width: 870px;" class="element">
		            	<input type="checkbox" id="lgBloqueiaVenda" reference="lg_bloqueia_venda" idForm="produtoServico" style="vertical-align: middle;"/><span class="checkPersonalizado">Bloqueia Venda</span> 
		            </div>
		            <div id="divStProdutoEmpresaSubstituto3" style="width: 870px;" class="element">
		            	<input type="checkbox" id="lgPermiteDesconto" reference="lg_permite_desconto" idForm="produtoServico" style="vertical-align: middle;"/><span class="checkPersonalizado">Permite Desconto</span> 
		            </div>
		            <div id="divStProdutoEmpresaSubstituto" style="width: 870px;" class="element">
		            	<input type="checkbox" id="lgFazEntrega" reference="lg_faz_entrega" idForm="produtoServico" style="vertical-align: middle;"/><span class="checkPersonalizado">Faz entrega</span> 
		            </div>
		            <div id="divStProdutoEmpresaSubstituto3" style="width: 870px;" class="element">
		            	<input type="checkbox" id="lgNaoControlaEstoque" reference="lg_nao_controla_estoque" idForm="produtoServico" style="vertical-align: middle;"/><span class="checkPersonalizado">Não controlar estoque</span> 
		            </div>
		            <div id="divStProdutoEmpresaSubstituto" style="width: 870px;" class="element">
		            	<input type="checkbox" id="lgImprimeNaTabelaPreco" reference="lg_imprime_na_tabela_preco" idForm="produtoServico" style="vertical-align: middle;"/><span class="checkPersonalizado">Imprime na tabela de preço</span> 
		            </div>
		            <div id="divStProdutoEmpresaSubstituto3" style="width: 870px;" class="element">
		            	<input type="checkbox" id="lgProdutoUsoConsumo" reference="lg_produto_uso_consumo" idForm="produtoServico" style="vertical-align: middle;"/><span class="checkPersonalizado">Produto de Uso/Consumo</span> 
		            </div>
		        </div>
       		</div>
       		<div id="divAbaFotosAdicionais" style="display:none;">
       			<input idform="fotosAdicionais" reference="cd_foto" id="cdFotoAdicional" name="cdFotoAdicional" type="hidden" value="0" defaultValue="0"/>
       			<div class="d1-line" id="line2">
			      <div style="float:left; width:100px; height:90px; margin:0px 6px 0px 0px; class="d1-line">
				  	<div style="width: 100px;" class="element">
					<iframe scrolling="auto" id="imageAdicional" style="border:1px solid #000000; background-color:#FFF; width:280px; _width:98px" height="216px" src="about:blank" frameborder="0"></iframe>
					<button idform="fotosAdicionais" onclick="btLoadImagemFotoOnClick()" style="margin:0px; left:0px; bottom:-16px; width:282px; font-size:9px" title="Limpar este campo..." class="controlButton"><img style="margin:0px 0px 0px -15px;" alt="X" src="/sol/imagens/filter-button.gif">Imagem</button>
					</div>
				  </div>
				  <div class="element" style="width: 570px;margin-left:190px;" >
	       			<div class="d1-toolBar" id="toolBarFotos" style="width:570px; height:36px; margin-top:5px;"></div>
	       	      </div>
				  <div id="divTxtDescricao" style="width: 400px;margin-left:190px;" class="element">
	              	<label class="caption" for="txtDescricao">Descrição</label>
	              	<textarea style="width: 570px; height: 175px;" logmessage="Descrição" class="textarea" idform="fotosAdicionais" reference="txt_descricao" datatype="STRING" id="txtDescricao" name="txtDescricao"></textarea>
            	  </div>
			    </div>
       		</div>
       		<div id="divAbaCaracteristicas" style="display:none;">
       		<input idform="caracteristicas" reference="cd_comentario" id="cdComentario" name="cdComentario" type="hidden" value="0" defaultValue="0"/>
       			<div class="element" style="width: 470px;" >
	       			<div class="d1-toolBar" id="toolBarCaracteristicas" style="width:450px; height:36px; margin-top:5px;"></div>
	       	    </div>
	       	    <div id="divNmAtributo" style="width: 400px;" class="element">
	              <label class="caption" for="nmAtributo">Nome</label>
	              <input lguppercase="true" style="text-transform: uppercase; width: 400px;" logmessage="Nome Característica" class="field2" idform="caracteristicas" reference="nm_titulo" datatype="STRING" maxlength="256" id="nmTitulo" name="nmTitulo" type="text" />
	            </div>
	       	    <div id="divTxtAtributoValor" style="width: 470px;" class="element">
	              	<label class="caption" for="txtAtributoValor">Características</label>
	              	<textarea style="width: 450px; height: 175px;" logmessage="Características" class="textarea" idform="caracteristicas" reference="txt_comentario_empresa" datatype="STRING" id="txtComentarioEmpresa" name="txtComentarioEmpresa"></textarea>
            	</div>
            	<div style="width: 375px;" class="element">
	               <label class="caption"></label>
	               <div id="divGridCaracteristicas" style="width: 375px; background-color:#FFF; height:175px; border:1px solid #000000">&nbsp;</div>
	             </div>
	        </div>
       		<div id="divAbaFornecedores" style="display:none;">
       			<div class="d1-line">
		             <div style="width: 840px;" class="element">
		               <label class="caption">Fornecedores</label>
		               <div id="divGridFornecedores" style="width: 840px; background-color:#FFF; height:219px; border:1px solid #000000">&nbsp;</div>
		             </div>
		             <div style="width: 20px;" class="element">
		               <label class="caption">&nbsp;</label>
		                 <button title="Novo Preço" onclick="btnNewFornecedorOnClick();" style="margin-bottom:2px;margin-left:3px;" id="btnNewFornecedor" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
		                 <button title="Alterar Preço" onclick="btnAlterFornecedorOnClick();" style="margin-bottom:2px;margin-left:3px;" id="btnAlterFornecedor" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
		                 <button title="Excluir Preço" onclick="btnDeleteFornecedorOnClick();" style="margin-left:3px;" id="btnDeleteFornecedor" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
		             </div>
           		</div>
       		</div>
       		<div id="divAbaCodigoBarras" style="display:none;">
       			<div class="d1-line">
	       			 <div class="element" style="width: 370px;" >
		       			<div class="d1-toolBar" id="toolBarCodigoBarras" style="width:350px; height:36px; margin-top:5px;"></div>
		       	     </div>	
		       	     <input idform="codigoBarras" reference="cd_produto_codigo" id="cdProdutoCodigoCodigoBarras" name="cdProdutoCodigoCodigoBarras" type="hidden" value="0" defaultValue="0"/>
	       			 <div id="divIdProdutoServicoSubstituto" style="width: 100px;" class="element">
		              	<label class="caption" for="idProdutoServicoCodigoBarras">Código de Barras</label>
		              	<input lguppercase="true" style="text-transform: uppercase; width: 95px;" logmessage="Codigo de Barras" class="field2" idform="codigoBarras" reference="id_produto_servico" datatype="STRING" maxlength="256" id="idProdutoServicoCodigoBarras" name="idProdutoServicoCodigoBarras" type="text" />
		             </div>
		             <div id="divTxtDescricaoCodigoBarras" style="width: 270px;" class="element">
		              	<label class="caption" for="txtDescricaoCodigoBarras">Descrição</label>
		              	<input lguppercase="true" style="text-transform: uppercase; width: 265px;" logmessage="Descrição" class="field2" idform="codigoBarras" reference="txt_descricao" datatype="STRING" maxlength="256" id="txtDescricaoCodigoBarras" name="txtDescricaoCodigoBarras" type="text" />
		             </div>
		             <div id="divIdReduzidoCodigoBarras" style="width: 100px;" class="element">
		              	<label class="caption" for="idReduzidoCodigoBarras">ID Reduzido</label>
		              	<input lguppercase="true" style="text-transform: uppercase; width: 95px;" logmessage="ID Reduzido" class="field2" idform="codigoBarras" reference="id_reduzido" datatype="STRING" maxlength="256" id="idReduzidoCodigoBarras" name="idReduzidoCodigoBarras" type="text" />
		             </div>
		             <div style="width: 840px;" class="element">
		               <label class="caption">Código de Barras</label>
		               <div id="divGridCodigoBarras" style="width: 840px; background-color:#FFF; height:175px; border:1px solid #000000">&nbsp;</div>
		             </div>
<!-- 		             <div style="width: 20px;" class="element"> -->
<!-- 		               <label class="caption">&nbsp;</label> -->
<!-- 		                 <button title="Novo Código de Barras" onclick="btnNewCodigoBarrasOnClick();" style="margin-bottom:2px;margin-left:3px;" id="btnNewCodigoBarras" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button> -->
<!-- 		                 <button title="Alterar Código de Barras" onclick="btnAlterCodigoBarrasOnClick();" style="margin-bottom:2px;margin-left:3px;" id="btnAlterCodigoBarras" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button> -->
<!-- 		                 <button title="Excluir Código de Barras" onclick="btnDeleteCodigoBarrasOnClick();" style="margin-left:3px;" id="btnDeleteCodigoBarras" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button> -->
<!-- 		             </div> -->
           		</div>
       		</div>
       		<div id="divAbaOutrosCodigos" style="display:none;">
       			<div class="d1-line">
       				 <input idform="outrosCodigo" reference="cd_produto_codigo" id="cdProdutoCodigoOutrosCodigos" name="cdProdutoCodigoOutrosCodigos" type="hidden" value="0" defaultValue="0"/>
       			  	 <div class="element" style="width: 470px;" >
		       			<div class="d1-toolBar" id="toolBarOutrosCodigos" style="width:450px; height:36px; margin-top:5px;"></div>
		       	     </div>	
	       			 <div id="divIdReduzidoOutrosCodigos" style="width: 100px;" class="element">
		              	<label class="caption" for="">Código</label>
		              	<input lguppercase="true" style="text-transform: uppercase; width: 95px;" logmessage="Código" class="field2" idform="outrosCodigos" reference="id_reduzido" datatype="STRING" maxlength="256" id="idReduzidoOutrosCodigos" name="idReduzidoOutrosCodigos" type="text" />
		             </div>
		             <div id="divTxtDescricaoOutrosCodigos" style="width: 300px;" class="element">
		              	<label class="caption" for="">Descrição</label>
		              	<input lguppercase="true" style="text-transform: uppercase; width: 300px;" logmessage="Descrição" class="field2" idform="outrosCodigos" reference="txt_descricao" datatype="STRING" maxlength="256" id="txtDescricaoOutrosCodigos" name="txtDescricaoOutrosCodigos" type="text" />
		             </div>
		             <div style="width: 840px;" class="element">
		               <label class="caption">Outros Códigos</label>
		               <div id="divGridOutrosCodigos" style="width: 840px; background-color:#FFF; height:175px; border:1px solid #000000">&nbsp;</div>
		             </div>
<!-- 		             <div style="width: 20px;" class="element"> -->
<!-- 		               <label class="caption">&nbsp;</label> -->
<!-- 		                 <button title="Novo Preço" onclick="btnNewFornecedorOnClick();" style="margin-bottom:2px;margin-left:3px;" id="btnNewFornecedor" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button> -->
<!-- 		                 <button title="Alterar Preço" onclick="btnAlterFornecedorOnClick();" style="margin-bottom:2px;margin-left:3px;" id="btnAlterFornecedor" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button> -->
<!-- 		                 <button title="Excluir Preço" onclick="btnDeleteFornecedorOnClick();" style="margin-left:3px;" id="btnDeleteFornecedor" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button> -->
<!-- 		             </div> -->
           		</div>
       		</div>
       		<div id="divAbaGrade" style="display:none;">
       			<div class="d1-line">
		             <div class="element" style="width: 470px;" >
		       			<div class="d1-toolBar" id="toolBarGrade" style="width:450px; height:36px; margin-top:5px;"></div>
		       	     </div>	
		       	     <input idform="grade" reference="cd_produto_servico_similar" id="cdProdutoServicoGrade" name="cdProdutoServicoGrade" type="hidden" value="0" defaultValue="0"/>
	       			 <div id="divNmProdutoServicoGrade" style="width: 400px;margin-top:10px;" class="element">
		              	<label class="caption" for="nmProdutoServicoGrade">Descrição</label>
		              	<input lguppercase="true" style="text-transform: uppercase; width: 400px;" logmessage="Nome do Produto" class="field2" idform="grade" reference="nm_produto_servico" datatype="STRING" maxlength="256" id="nmProdutoServicoGrade" name="nmProdutoServicoGrade" type="text" />
		             </div>	
		             <div id="divTxtDadoTecnicoGrade" style="width: 210px;margin-top:10px;" class="element">
		              	<label class="caption" for="txtDadoTecnicoGrade">Tamanho</label>
		              	<input lguppercase="true" style="text-transform: uppercase; width: 205px;" logmessage="Tamanho do Produto" class="field2" idform="grade" reference="txt_dado_tecnico" datatype="STRING" maxlength="256" id="txtDadoTecnicoGrade" name="txtDadoTecnicoGrade" type="text" />
		             </div>
		             <div id="divTxtEspecificacaoGrade" style="width: 220px;margin-top:10px;" class="element">
		              	<label class="caption" for="nmProdutoServicoSubstituto2">Cor</label>
		              	<input lguppercase="true" style="text-transform: uppercase; width: 215px;" logmessage="Nome Produto" class="field2" idform="grade" reference="txt_especificacao" datatype="STRING" maxlength="256" id="txtEspecificacaoGrade" name="txtEspecificacaoGrade" type="text" />
		             </div>
		             <div id="divIdProdutoServicoGrade" style="width: 220px;margin-top:10px;" class="element">
		              	<label class="caption" for="idProdutoServicoGrade">Código de Barras</label>
		              	<input lguppercase="true" style="text-transform: uppercase; width: 215px;" logmessage="Nome Produto" class="field2" idform="grade" reference="id_produto_servico" datatype="STRING" maxlength="256" id="idProdutoServicoGrade" name="idProdutoServicoGrade" type="text" />
		             </div>
		             <div id="divNrReferenciaGrade" style="width: 220px;margin-top:10px;" class="element">
		              	<label class="caption" for="nrReferenciaGrade">Referência</label>
		              	<input lguppercase="true" style="text-transform: uppercase; width: 220px;" logmessage="Nome Produto" class="field2" idform="grade" reference="nr_referencia" datatype="STRING" maxlength="256" id="nrReferenciaGrade" name="nrReferenciaGrade" type="text" />
		             </div>
		             <div style="width: 870px;" class="element">
		               <label class="caption">Grade</label>
		               <div id="divGridGrade" style="width: 870px; background-color:#FFF; height:129px; border:1px solid #000000">&nbsp;</div>
		             </div>
		        </div>
       		</div>
       	</div>
        <div id="divAbaLoja" style="display:none;">
			<div id="divnmProdutoServicoSubstituto2" style="width: 880px;" class="element">
              <label class="caption" for="nmProdutoServicoSubstituto2">Descrição do produto na Loja Virtual</label>
              <input lguppercase="true" style="text-transform: uppercase; width: 880px;" logmessage="Nome Produto" class="disabledField2" disabled="disabled" idform="produtoServico" reference="nm_produto_servico" datatype="STRING" maxlength="256" id="nmProdutoServicoSubstituto2" name="nmProdutoServicoSubstituto2" type="text" />
            </div>
			<div style="position:relative; border:1px solid #999; float:left; padding:2px; margin-top:7px; margin-right:3px">
               <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; width:110px;">Dimensões do produto</div>
               <div style="width: 200px; height:35px;" class="element">
                 <input reference="vl_largura" style="width: 45px;margin-top:5px;margin-left:5px;" mask="#,###.00" logmessage="Largura Produto" idform="produtoServico" class="field2" name="vlLargura" id="vlLargura" datatype="FLOAT" type="text" />
                 <label style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">x</label>
                 <input reference="vl_altura" style="width: 45px;" mask="#,###.00" logmessage="Altura Produto" idform="produtoServico" class="field2" name="vlAltura" id="vlAltura" datatype="FLOAT" type="text" />
                 <label style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">x</label>
                 <input reference="vl_comprimento" style="width: 45px;" mask="#,###.00" logmessage="Comprimento Produto" idform="produtoServico" class="field2" name="vlComprimento" id="vlComprimento" datatype="FLOAT" type="text" />
                 <label class="" style="padding:15px 0 0 0; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">cm</label>
               </div>	
             </div>
             <div style="position:relative; border:1px solid #999; float:left; padding:2px; margin-top:7px; margin-left: 10px;">
               <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; width:130px">Dimensões da embalagem</div>
               <div style="width: 200px; height:35px;" class="element">
                 <input reference="vl_largura_embalagem" style="width: 45px;margin-top:5px;margin-left:5px;" mask="#,###.00" logmessage="Largura Embalagem" idform="produtoServico" class="field2" name="vlLarguraEmbalagem" id="vlLarguraEmbalagem" datatype="FLOAT" type="text" />
                 <label style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">x</label>
                 <input reference="vl_altura_embalagem" style="width: 45px;" mask="#,###.00" logmessage="Altura Embalagem" idform="produtoServico" class="field2" name="vlAlturaEmbalagem" id="vlAlturaEmbalagem" datatype="FLOAT" type="text" />
                 <label style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">x</label>
                 <input reference="vl_comprimento_embalagem" style="width: 45px;" mask="#,###.00" logmessage="Comprimento Embalagem" idform="produtoServico" class="field2" name="vlComprimentoEmbalagem" id="vlComprimentoEmbalagem" datatype="FLOAT" type="text" />
                 <label class="" style="padding:15px 0 0 0; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">cm</label>
               </div>
             </div>
             <div style="position:relative; border:1px solid #999; float:left; padding:2px; margin-top:7px; margin-left: 10px;">
               <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; width:130px">Entrega/Parcelamento</div>
               <div style="width: 440px; height:35px;" class="element">
                 <label style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">Prazo de Entrega</label>
                 <input reference="vl_largura_embalagem" style="width: 50px;margin-top:5px;" mask="#,###.00" logmessage="Largura Embalagem" idform="produtoServico" class="field2" name="vlLarguraEmbalagem" id="vlLarguraEmbalagem" datatype="FLOAT" type="text" />
                 <input type="radio" name="group1" value="Milk" style="width: 20px;margin-top:5px; margin-left:30px;" > 
				 <label style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">Dias</label>
				 <input type="radio" name="group1" value="Butter" style="width: 20px;margin-top:5px;" > 
				 <label style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">Horas</label>
                 <label style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; margin-left:30px;"  for="">Nº Parcelas</label>
                 <input reference="vl_largura_embalagem" style="width: 50px;margin-top:5px;" mask="#,###.00" logmessage="Largura Embalagem" idform="produtoServico" class="field2" name="vlLarguraEmbalagem" id="vlLarguraEmbalagem" datatype="FLOAT" type="text" />
               </div>
             </div>
        </div>
        <div id="divAbaEspecificacoes" style="display:none;">
        	<div id="divTxtEspecificacaoSubstituto" style="width: 870px;" class="element">
        	  <label class="caption">Observações</label>
              <textarea style="width: 880px; height: 250px;" logmessage="Especificações" class="textarea" idform="produtoServico" reference="txt_especificacao" datatype="STRING" id="txtEspecificacao" name="txtEspecificacao"></textarea>
            </div>
        </div>
    
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        <div id="divTemp"  type="hidden" style="display: none;">
          <div class="d1-line" id="line0">
            <div id="divNmProdutoServico" style="width: 452px;" class="element">
              <label class="caption" for="nmProdutoServico">Nome</label>
              <input lguppercase="true" style="text-transform: uppercase; width: 449px;" logmessage="Nome Produto" class="field" idform="produtoServico" reference="nm_produto_servico" datatype="STRING" maxlength="256" id="nmProdutoServico" name="nmProdutoServico" type="text" />
            </div>
<!--             <div style="width: 94px; " class="element"> -->
<!--               <label class="caption" for="">Tamanho</label> -->
<!--               <input lguppercase="true" style="text-transform: uppercase; width: 89px;" logmessage="Tamanho" class="field" idform="produtoServico" reference="txt_dado_tecnico" datatype="STRING" id="txtDadoTecnico" name="txtDadoTecnico" type="text"/> -->
<!--             </div> -->
<!--             <div style="width: 90px; " class="element"> -->
<!--               <label class="caption" for="">Cor</label> -->
<!--               <input lguppercase="true" style="text-transform: uppercase; width: 87px;" logmessage="Cor" class="field" idform="produtoServico" reference="txt_especificacao" datatype="STRING" id="txtEspecificacao" name="txtEspecificacao" type="text"/> -->
<!--             </div> -->
          </div>
          <div class="d1-line" id="line2">
            <div id="divSgProdutoServico" style="width: 40px; display: none;" class="element">
              <label class="caption" for="sgProdutoServico">Sigla</label>
              <input lguppercase="true" style="text-transform: uppercase; width: 37px;" logmessage="Sigla" class="field" idform="produtoServico" reference="sg_produto_servico" datatype="STRING" maxlength="10" id="sgProdutoServico" name="sgProdutoServico" type="text"/>
            </div>
            
            <div style="width: 70px; display: none;" class="element">
              <label class="caption" for="qtPrecisaoUnidade">Precisão Un.</label>
              <input style="width: 67px;" mask="##" logmessage="Precisão setor" class="field" idform="produtoServico" reference="qt_precisao_unidade" datatype="INT" id="qtPrecisaoUnidade" name="qtPrecisaoUnidade" defaultValue="2" value="2" type="text"/>
            </div>
            <div id="divIdProdutoServico" style="width: 150px;" class="element">
              <label class="caption" for="idProdutoServico">Código de Barras (GTIN/NGIC)</label>
              <input lguppercase="true" style="text-transform: uppercase; width: 147px;" logmessage="ID" class="field" idform="produtoServico" reference="id_produto_servico" datatype="STRING" maxlength="20" id="idProdutoServico" name="idProdutoServico" type="text"/>
            </div>
<!--             <div id="divIdReduzido" style="width: 150px;" class="element"> -->
<!--               <label class="caption">ID Reduzido</label> -->
<!--               <input style="text-transform: uppercase; width: 147px;" lguppercase="true" logmessage="ID reduzido" class="field" idform="produtoServico" reference="id_reduzido" datatype="STRING" maxlength="50" id="idReduzido" name="idReduzido" type="text"/> -->
<!--         	  <button idform="produtoServico" onclick="getNextIdReduzido(null);" id="btnNextIdReduzido" title="Gerar Número de Reduzido" class="controlButton"><img alt="|30|" src="/sol/imagens/reload-button.gif" width="15" height="15"></button> -->
<!--             </div> -->
            <div style="width: 94px;" class="element">
              <label class="caption">Referência</label>
              <input style="width: 89px;" logmessage="Referência" class="field" idform="produtoServico" reference="nr_referencia" datatype="STRING" id="nrReferencia" name="nrReferencia" type="text"/>
            </div>
            <div style="width: 90px;" class="element">
              <label class="caption">Estoque Atual</label>
              <input style="width: 87px; text-align:right;" readonly="readonly" static="true" logmessage="Qtd. por embalagem" mask="#,####" defaultvalue="0" class="disabledField" idform="produtoServico" reference="qt_estoque" datatype="INT" id="qtEstoque" name="qtEstoque" type="text"/>
            </div>
            <div id="divNrOrdem" style="width: 53px; display: none;" class="element">
              <label class="caption" for="nrOrdem">N&deg; Ordem</label>
              <input style="text-transform: uppercase; width: 50px;" lguppercase="true" logmessage="Nr Ordem" class="field" idform="produtoServico" reference="nr_ordem" datatype="STRING" maxlength="10" id="nrOrdem" name="nrOrdem" type="text"/>
            </div>
          </div>
          
          <div class="d1-line" id="line4">
            <div style="width: 238px;" class="element">
              <label class="caption">Tabela de Preço 1</label>
              <input style="width: 235px;" static="true" disabled="disabled" class="disabledField" name="nmTabelaPrecoVarejo" id="nmTabelaPrecoVarejo" type="text" value="<%=nmTabelaPrecoVarejo%>"/>
            </div>
            <div style="width: 80px;" class="element">
              <label class="caption">Preço</label>
              <input style="width: 77px; text-align:right;" readonly="readonly" static="true" class="disabledField" idform="produtoServico" reference="vl_preco_<%=cdTipoOperacaoVarejo%>" datatype="FLOAT" id="vlPrecoVarejo" name="vlPrecoVarejo" type="text" mask="#,###.00"/>
            </div>
            <div style="width: 238px;" class="element">
              <label class="caption">Tabela de Preço 2</label>
              <input style="width: 235px;" static="true" readonly="readonly" class="disabledField" name="nmTabelaPrecoAtacado" id="nmTabelaPrecoAtacado" type="text" value="<%=nmTabelaPrecoAtacado%>"/>
            </div>
            <div style="width: 80px;" class="element">
              <label class="caption">Preço</label>
              <input style="width: 77px; text-align:right;" readonly="readonly" static="true" class="disabledField" class="field" idform="produtoServico" reference="vl_preco_<%=cdTipoOperacaoAtacado%>" datatype="FLOAT" id="vlPrecoAtacado" name="vlPrecoAtacado" type="text" mask="#,###.00"/>
            </div>
          </div>
          <div class="d1-line" id="line1">
            <div id="divTxtProdutoServico" style="width: 637px;" class="element">
              <label class="caption" for="txtProdutoServico">Descrição Detalhada</label>
              <textarea style="width: 634px; height:38px;" logmessage="Descrição Produto" class="textarea" idform="produtoServico" reference="txt_produto_servico" datatype="STRING" id="txtProdutoServico" name="txtProdutoServico"></textarea>
            </div>
          </div>
          <div class="d1-line" id="line4">
            
            
          </div>
          <div id="divProdutoServicoLinha1" style="">
              <div class="d1-line" id="line8">
                <div style="width: 70px;" class="element">
                  <label class="caption" for="qtDiasGarantia" title="Garantia em dias fornecida pelo Fabricante">Dias garantia</label>
                  <input style="width: 67px;" logmessage="Dias garantia" class="field" idform="produtoServico" reference="qt_dias_garantia" datatype="INT" id="qtDiasGarantia" name="qtDiasGarantia" type="text"/>
                </div>
                <div style="width: 130px;" class="element">
                  <label class="caption" for="tpControleEstoque">Controle estoque</label>
                  <select style="width: 127px;" logmessage="Tipo de Controle de Estoque" class="select" idform="produtoServico" reference="tp_controle_estoque" datatype="STRING" id="tpControleEstoque" name="tpControleEstoque" defaultValue="0">
                  </select>
                </div>
                <div style="width: 150px;" class="element">
                  <label class="caption" for="tpReabastecimento">Tipo reabastecimento</label>
                  <select style="width: 147px;" logmessage="Tipo de reabastecimento" class="select" idform="produtoServico" reference="tp_reabastecimento" datatype="STRING" id="tpReabastecimento" name="tpReabastecimento" defaultValue="0">
                  </select>
                </div>
                <div style="width: 100px;" class="element">
                  <label class="caption" for="vlPrecoMedio">Preço médio</label>
                  <input style="width: 97px; text-align:right;" static="static" disabled="disabled" mask="#,###.00" logmessage="Preço médio" class="disabledField" idform="produtoServico" reference="vl_preco_medio" datatype="FLOAT" maxlength="10" id="vlPrecoMedio" name="vlPrecoMedio" type="text"/>
                </div>
              </div>
          </div>
          <div id="divProdutoServicoLinha2" style="">
              <div class="d1-line" id="line9">
                <div style="width: 80px;" class="element">
                  <label class="caption" for="qtIdeal">Estoque ideal</label>
                  <input style="width: 77px; text-align:right;" mask="#,###.00" logmessage="Est. ideal" class="field" idform="produtoServico" reference="qt_ideal" datatype="FLOAT" maxlength="10" id="qtIdeal" name="qtIdeal" type="text"/>
                </div>
                <div style="width: 80px;" class="element">
                  <label class="caption" for="qtMinima">Estoque mínimo</label>
                  <input style="width: 77px; text-align:right;" mask="#,###.00" logmessage="Est. mínimo" class="field" idform="produtoServico" reference="qt_minima" datatype="FLOAT" id="qtMinima" name="qtMinima" type="text"/>
                </div>
                <div style="width: 80px;" class="element">
                  <label class="caption" for="qtMaxima">Estoque máximo</label>
                  <input style="width: 77px; text-align:right;" mask="#,###.00" logmessage="Est. máximo" class="field" idform="produtoServico" reference="qt_maxima" datatype="FLOAT" id="qtMaxima" name="qtMaxima" type="text"/>
                </div>
                <div style="width: 80px;" class="element">
                  <label class="caption" for="qtDiasEstoque" title="Período minímo em dias para o qual há garantia de disponibilidade do produto em estoque">Dias estoque</label>
                  <input style="width: 77px; text-align:right;" logmessage="Dias estoque" class="field" idform="produtoServico" reference="qt_dias_estoque" datatype="INT" id="qtDiasEstoque" name="qtDiasEstoque" type="text"/>
                </div>
                <div style="width: 80px;" class="element">
                  <label class="caption" for="qtPrecisaoCusto">Precisão custo</label>
                  <input style="width: 77px; text-align:right;" mask="##" logmessage="Precisão custo" class="field" idform="produtoServico" reference="qt_precisao_custo" datatype="INT" id="qtPrecisaoCusto" name="qtPrecisaoCusto" type="text" defaultvalue="2"/>
                </div>
	            <div id="divDtDesativacao" style="width: 69px; display: none;" class="element">
	              <label class="caption" for="dtDesativacao">Desativação</label>
	              <input style="width: 66px;" logmessage="Data Desativação" readonly="readonly" static="static" disabled="disabled" mask="dd/mm/yyyy" maxlength="10" class="disabledField" idform="produtoServico" reference="dt_desativacao" datatype="DATE" id="dtDesativacao" name="dtDesativacao" type="text"/>
	            </div>
	            <div style="width: 20px;" class="element">
                  <label class="caption" for="lgEstoqueNegativo">&nbsp;</label>
                  <input logmessage="Permite estoque negativo?" idform="produtoServico" reference="lg_estoque_negativo" id="lgEstoqueNegativo" name="lgEstoqueNegativo" type="checkbox" value="1"/>
                </div>
                <div style="width: 120px;" class="element">
                  <label class="caption">&nbsp;</label>
                  <label style="margin:2px 0px 0px 0px" class="caption">Permite estoque negativo</label>
                </div>
              </div>
          </div>
          <div class="d1-line" id="divGridGruposSuperior" style="display: none;">
            <div id="divGridGruposParent" style="width: 613px;" class="element">
              <label class="caption">Grupos</label>
              <div id="divGridGrupos" style="width: 610px; background-color:#FFF; height:75px; border:1px solid #000000">&nbsp;</div>
            </div>
            <div style="width: 20px;" class="element">
              <label class="caption">&nbsp;</label>
                <button title="Adicionar grupo para o produto" onclick="btnNewProdutoGrupoOnClick();" style="margin-bottom:2px" id="btnNewProdutoGrupo" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
                <button title="Remover grupo para o produto" onclick="btnDeleteProdutoGrupoOnClick();" id="btnDeleteProdutoGrupo" class="toolButton"><img src="/sol/imagens/btDelete16.gif" height="16" width="16"/></button>
            </div>
          </div>
          <div class="d1-line" id="divGruposSuperior" style="display:none;">
            <div style="position:relative; border:1px solid #999; float:left; padding:2px; margin-top:7px; margin-right:0px">
              <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:2px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; width:130px;">&nbsp&nbspClassificação do Produto </div>
              <div style="width: 606px; height:69px; padding:0 0 2px 0" class="element" id="divBodyGrupo">
                
              </div>
            </div>
            <div style="float:left; width:20px; margin-top: 7px;">
                <button title="Adicionar grupo para o produto" onclick="btnNewProdutoGrupoOnClick();" style="margin-bottom:2px" id="btnNewProdutoGrupo" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
            </div>
          </div>
          <div id="divGridSimilaresSuperior1"> </div>
          </div>
        <div id="divProdutoServicoLinha3">
            <div id="divAbaEspecificacoes3" style="display:none;">
              <div style="float:left">
                 <div class="d1-line">
		             <div style="width: 613px;" class="element">
		               <label class="caption">Tributos</label>
		               <div id="divTreeTributos" style="width: 635px; background-color:#FFF; height:99px; border:1px solid #000">&nbsp;</div>
		             </div>
		         </div>
                <div class="d1-line" id="line2" style="">
                  <div id="divTxtEspecificacao" style="width: 637px;" class="element">
                    <label class="caption" for="txtEspecificacao">Especificações</label>
                    <textarea style="width: 634px; height: 50px;" logmessage="Especificações" class="textarea" idform="produtoServico" reference="txt_especificacao" datatype="STRING" id="txtEspecificacao2" name="txtEspecificacao2"></textarea>
                  </div>
                </div>
                <div class="d1-line" id="line3">
                  <div id="divTxtDadoTecnico" style="width: 637px;" class="element">
                    <label class="caption" for="txtDadoTecnico">Dados técnicos</label>
                    <textarea style="width: 634px; height: 45px;" logmessage="Dados Técnicos" class="textarea" idform="produtoServico" reference="txt_dado_tecnico" datatype="STRING" id="txtDadoTecnico2" name="txtDadoTecnico2"></textarea>
                  </div>
                </div>
                <div class="d1-line" id="line4">
                  <div id="divTxtPrazoEntrega" style="width: 637px;" class="element">
                    <label class="caption" for="txtPrazoEntrega">Prazo de entrega</label>
                    <textarea style="width: 634px; height: 45px;" logmessage="Prazo de Entrega" class="textarea" idform="produtoServico" reference="txt_prazo_entrega" datatype="STRING" id="txtPrazoEntrega" name="txtPrazoEntrega"></textarea>
                  </div>
                </div>
              </div>
              <div style="width:335px; display:none; float:left; padding:0 0 0 4px;" id="divAtributos">
                <div class="d1-line" id="line2">
                  <div style="position:relative; border:1px solid #999; float:left; padding:2px; margin-top:7px; margin-right:0px">
                    <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:2px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; width:90px;">Dados específicos</div>
                    <div style="width: 329px; height:304px; overflow:scroll; padding:0 0 2px 0;" class="element" id="divBodyAtributos">
                      <div id="divElementAtributo" style="width: 309px; padding:0 0 2px 0; display:none;" class="element">
                        <label id="labelAtributo" class="caption" style="width:100px; float:left; padding:2px 0 0 0">ID Atributo </label>
                        <input style="width: 207px;" logmessage="Log Atributo" class="field" idform="produtoServico" reference="log_atributo" datatype="INT" id="idLogAtributo" name="idLogAtributo" type="text">
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="d1-line" id="line2">
                <div style="position:relative; border:1px solid #999; float:left; padding:2px; margin-top:7px; margin-right:3px">
                  <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; width:110px;">Dimensões do produto</div>
                  <div style="width: 192px;" class="element">
                    <input reference="vl_largura" style="width: 47px;" mask="#,###.00" logmessage="Largura Produto" idform="produtoServico" class="field" name="vlLargura" id="vlLargura" datatype="FLOAT" type="text" />
                    <label style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">x</label>
                    <input reference="vl_altura" style="width: 47px;" mask="#,###.00" logmessage="Altura Produto" idform="produtoServico" class="field" name="vlAltura" id="vlAltura" datatype="FLOAT" type="text" />
                    <label style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">x</label>
                    <input reference="vl_comprimento" style="width: 47px;" mask="#,###.00" logmessage="Comprimento Produto" idform="produtoServico" class="field" name="vlComprimento" id="vlComprimento" datatype="FLOAT" type="text" />
                    <label class="" style="padding:15px 0 0 0; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">cm</label>
                  </div>
                </div>
                <div style="position:relative; border:1px solid #999; float:left; padding:2px; margin-top:7px; margin-right:3px">
                  <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; width:130px">Dimensões da embalagem</div>
                  <div style="width: 200px;" class="element">
                    <input reference="vl_largura_embalagem" style="width: 50px;" mask="#,###.00" logmessage="Largura Embalagem" idform="produtoServico" class="field" name="vlLarguraEmbalagem" id="vlLarguraEmbalagem" datatype="FLOAT" type="text" />
                    <label style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">x</label>
                    <input reference="vl_altura_embalagem" style="width: 50px;" mask="#,###.00" logmessage="Altura Embalagem" idform="produtoServico" class="field" name="vlAlturaEmbalagem" id="vlAlturaEmbalagem" datatype="FLOAT" type="text" />
                    <label style="font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">x</label>
                    <input reference="vl_comprimento_embalagem" style="width: 50px;" mask="#,###.00" logmessage="Comprimento Embalagem" idform="produtoServico" class="field" name="vlComprimentoEmbalagem" id="vlComprimentoEmbalagem" datatype="FLOAT" type="text" />
                    <label class="" style="padding:15px 0 0 0; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px" for="">cm</label>
                  </div>
                </div>
                <div style="width: 103px;" class="element">
                  <label class="caption" for="vlPesoUnitario">Peso produto </label>
                  <input style="width: 100px;" mask="#,###.00" logmessage="Peso unitário" class="field" idform="produtoServico" reference="vl_peso_unitario" datatype="FLOAT" maxlength="10" id="vlPesoUnitario" name="vlPesoUnitario" type="text">
                </div>
                <div style="width: 15px;" class="element">
                  <label class="caption" style="padding:15px 0 0 0" for="">g</label>
                </div>
                <div style="width: 93px;" class="element">
                  <label class="caption" for="vlPesoUnitarioEmbalagem">Peso embalagem </label>
                  <input style="width: 90px;" mask="#,###.00" logmessage="Peso unitário (embalagem)" class="field" idform="produtoServico" reference="vl_peso_unitario_embalagem" datatype="FLOAT" maxlength="10" id="vlPesoUnitarioEmbalagem" name="vlPesoUnitarioEmbalagem" type="text">
                </div>
                <div style="width: 15px;" class="element">
                  <label class="caption" style="padding:15px 0 0 0;">g</label>
                </div>
              </div>
              <div id="divGridSimilaresSuperior2">
                <div class="d1-line">
                  <div style="width: 291px;" class="element">
                    <label class="caption">Referenciados</label>
                    <div id="divGridReferenciados" style="width: 288px; background-color:#FFF; height:119px; border:1px solid #000000">&nbsp;</div>
                  </div>
                  <div style="width: 20px;" class="element">
                    <label class="caption">&nbsp;</label>
                      <button title="Novo produto referenciado" onclick="btnNewProdutoReferenciadoOnClick();" style="margin-bottom:2px" id="btnNewProdutoReferenciado" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
                      <button title="Excluir produto referenciado" onclick="btnDeleteProdutoReferenciadoOnClick();" id="btnDeleteProdutoReferenciado" class="toolButton"><img src="/sol/imagens/btDelete16.gif" height="16" width="16"/></button>
                  </div>
                </div>
              </div>
            </div>
            <div id="divAbaFotoProduto" style="display:none;">
            	<div class="d1-line">
                	<div style="width: 613px;" class="element">
                  		<label class="caption">Fotos</label>
              	    	<div id="divGridFotoProduto" style="width: 610px; background-color:#FFF; height:337px; border:1px solid #000000">&nbsp;</div>
               	 	</div>
	                <div style="width: 20px;" class="element">
	                  	<label class="caption">&nbsp;</label>
	                    	<button title="Novo Preço" onclick="btnNewFotoProdutoOnClick();" style="margin-bottom:2px" id="btnNewFotoProduto" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
	                    	<button title="Alterar Preço" onclick="btnAlterFotoProdutoOnClick();" style="margin-bottom:2px" id="btnAlterFotoProduto" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
	                    	<button title="Excluir Preço" onclick="btnDeleteFotoProdutoOnClick();" id="btnDeleteFotoProduto" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
	                </div>
		            <div id="divPreviewFotoProduto" style="width: 610px; margin-top: 3px; float: left; display: none;" class="element">
		            	<iframe scrolling="auto" id="previewFotoProduto" style="border:1px solid #000000; background-color:#FFF; margin:0px 0px 0px 0px; width:610px; height:200px;" src="about:blank" frameborder="0">&nbsp;</iframe>
		            </div>
              	</div>
            </div>
        </div>
      </div>
      <div id="divRodape">
          <img alt="|30|" src="../grl/imagens/produto16.gif" width="15" height="15">
     	  <label id="rodapeText" name="rodapeText" class="caption" style="margin-left:10px;font-size: 12px;font-style:italic;vertical-align: text-top;"></label>
      </div>    
    </div>
</div>

<div id="precoPanel" class="d1-form" style="display:none; width:405px; height:32px">
    <div style="width: 405px;" class="d1-body">
      <div class="d1-line">
        <div style="width: 250px;" class="element">
          <label class="caption" for="cdTabelaPreco">Tabela de Pre&ccedil;o</label>
          <select style="width: 247px;" logmessage="Tabela de Preço" registerclearlog="0" defaultValue="0" class="select" idform="preco" reference="cd_tabela_preco" maxlength="10" id="cdTabelaPreco" name="cdTabelaPreco">
            <option value="0">Selecione...</option>
          </select>
        </div>
        <div style="width: 115px;" class="element">
          <label class="caption" for="vlPreco">Pre&ccedil;o</label>
          <input style="text-transform: uppercase; width: 113px;" lguppercase="true" logmessage="Preço" class="field" idform="preco" reference="vl_preco" maxlength="10" id="vlPreco" name="vlPreco" type="text"/>
        </div>
        <div style="width: 20px;" class="element">
          <label class="caption" style="height:10px">&nbsp;</label>
          <button title="Gravar preço" onclick="btnSavePrecoOnClick();" style="margin-bottom:2px" id="btnSavePreco" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/></button>
        </div>
      </div>
    </div>
</div>

<div id="formDisponibilidade" class="d1-form" style="display: none; width:644px; height:345px">
    <div style="width: 644px;" class="d1-body">
      <div class="d1-line" id="line3">
        <div style="position:relative; border:1px solid #999; float:left; padding:4px; margin-top:5px;">
          <div style="position:absolute; top:-6px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">Consulta</div>
          <div style="width: 506px;" class="element">
            <label class="caption" for="cdLocalArmazenamentoSearch">Local de Armazenamento </label>
            <input datatype="STRING" id="cdLocalArmazenamentoSearch" name="cdLocalArmazenamentoSearch" type="hidden" value="0" defaultValue="0">
            <input style="width: 499px;" static="true" class="field" name="nmLocalArmazenamentoSearch" id="nmLocalArmazenamentoSearch" type="text" />
            <button onclick="btnFindCdLocalArmazenamentoOnClick()" idform="produtoEstoque" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
            <button onclick="btnClearCdLocalArmazenamentoOnClick()" idform="produtoEstoque" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
          </div>
          <div style="width: 120px;" class="element">
            <div style="width:120px; padding:10px 0 0 2px" class="element">
              <button id="btnConsultarEstoque" title="Consultar estoques" onclick="btnConsultarEstoqueOnClick(null, null);" style="width:120px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btPesquisar16.gif" height="16" width="16"/>Consultar estoque</button>
            </div>
          </div>
        </div>
        <div class="d1-line">
          <div style="width: 637px; padding:2px 0 0 0" class="element">
            <div id="divGridLocaisArmazenamento" style="width: 634px; background-color:#FFF; height:234px; border:1px solid #000000"></div>
          </div>
        </div>
        <div class="d1-line" id="line3">
          <div style="width: 96.667px;" class="element">
            <label class="caption" for="qtIdeal">Est. ideal</label>
            <input style="width: 93.667px;" mask="#,###.00" logmessage="Est. ideal" class="field" idform="localArmazenamento" reference="qt_ideal" datatype="FLOAT" maxlength="10" id="qtIdealLocalArmazenamento" name="qtIdealLocalArmazenamento" type="text">
          </div>
          <div style="width: 96.667px;" class="element">
            <label class="caption" for="qtMinima">Est. mínimo</label>
            <input style="width: 93.667px;" mask="#,###.00" logmessage="Est. mínimo" class="field" idform="localArmazenamento" reference="qt_minima" datatype="FLOAT" id="qtMinimaLocalArmazenamento" name="qtMinimaLocalArmazenamento" type="text">
          </div>
          <div style="width: 136.667px;" class="element">
            <label class="caption" for="qtMinima">Est. mínimo (e-commerce)</label>
            <input style="width: 133.667px;" mask="#,###.00" logmessage="Est. mínimo E-Commerce" class="field" idform="localArmazenamento" reference="qt_minima_ecommerce" datatype="FLOAT" id="qtMinimaEcommerceLocalArmazenamento" name="qtMinimaEcommerceLocalArmazenamento" type="text">
          </div>
          <div style="width: 96.667px;" class="element">
            <label class="caption" for="qtMaxima">Est. máximo</label>
            <input style="width: 93.667px;" mask="#,###.00" logmessage="Est. máximo" class="field" idform="localArmazenamento" reference="qt_maxima" datatype="FLOAT" id="qtMaximaLocalArmazenamento" name="qtMaximaLocalArmazenamento" type="text">
          </div>
          <div style="width: 99.667px;" class="element">
            <label class="caption" for="qtDiasEstoque">Dias estoque</label>
            <input style="width: 96.667px;" logmessage="Dias estoque" class="field" idform="localArmazenamento" reference="qt_dias_estoque" datatype="INT" id="qtDiasEstoqueLocalArmazenamento" name="qtDiasEstoqueLocalArmazenamento" type="text">
          </div>
          <div style="width: 86.667px;" class="element">
            <div style="width:120px; padding:10px 0 0 0" class="element">
                <button id="btnSaveLocalArmazenamento" title="Gravar item" onclick="btnSaveLocalArmazenamentoOnClick();" style="width:109px; height:22px; border:1px solid #999999; font-weight:normal" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Gravar dados</button>
            </div>
          </div>
        </div>
      </div>
    </div>
</div>
  
<div id="panelFornecedor" class="d1-form" style="display:none; width:445px; height:32px">
    <div style="width: 460px;" class="d1-body">
      <div class="d1-line" id="line0">
        <div style="width: 450px;" class="element">
          <label class="caption" for="cdFornecedor">Fornecedor</label>
          <input logmessage="Código Fornecedor" idform="fornecedor" reference="cd_fornecedor" datatype="STRING" id="cdFornecedor" name="cdFornecedor" type="hidden" value="0" defaultValue="0">
          <input logmessage="Nome Fornecedor" idform="fornecedor" reference="nm_fornecedor" style="width: 447px;" static="true" disabled="disabled" class="disabledField" name="nmFornecedor" id="nmFornecedor" type="text">
          <button id="btnFindCdFornecedor" onclick="btnFindCdFornecedorOnClick()" idform="fornecedor" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
          <button onclick="btnClearCdFornecedorOnClick()" idform="fornecedor" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
        </div>
      </div>
      <div class="d1-line" id="line0">
        <div style="width: 450px;" class="element">
          <label class="caption" for="cdRepresentante">Representante</label>
          <input logmessage="Código Representante" idform="fornecedor" reference="cd_representante" datatype="STRING" id="cdRepresentante" name="cdRepresentante" type="hidden" value="0" defaultValue="0">
          <input logmessage="Nome Representante" idform="fornecedor" reference="nm_representante" style="width: 447px;" static="true" disabled="disabled" class="disabledField" name="nmRepresentante" id="nmRepresentante" type="text">
          <button id="btnFindCdRepresentante" onclick="btnFindCdRepresentanteOnClick()" idform="fornecedor" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
          <button onclick="btnClearCdRepresentanteOnClick()" idform="fornecedor" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
        </div>
      </div>
      <div class="d1-line">
        <div style="width: 125px;" class="element">
          <label class="caption" for="idProduto">Cód. Produto</label>
          <input style="text-transform: uppercase; width: 122px;" lguppercase="true" datatype="STRING" logmessage="Código produto no fornecedor" class="field" idform="fornecedor" reference="id_produto" maxlength="50" id="idProduto" name="idProduto" type="text"/>
        </div>
        <div style="width: 100px;" class="element">
          <label class="caption" for="qtPedidoMinimo">Qtd. M&iacute;nima</label>
          <input style="text-transform: uppercase; width: 97px;" lguppercase="true" datatype="STRING" logmessage="" class="field" idform="fornecedor" reference="qt_pedido_minimo" maxlength="50" id="qtPedidoMinimo" name="qtPedidoMinimo" type="text"/>
        </div>
        <div id="" style="width: 150px;" class="element">
          <label class="caption" for="cdUnidadeMedidaForn">Unidade</label>
          <select style="width: 147px;" logmessage="Unidade" class="select" idform="fornecedor" reference="cd_unidade_medida" datatype="INT" id="cdUnidadeMedidaForn" name="cdUnidadeMedidaForn" defaultValue="0">
            <option value="0">Selecione...</option>
          </select>
        </div>
        <div style="width: 75px;" class="element">
          <label class="caption" for="qtDiasEntrega">Dias entrega</label>
          <input style="text-transform: uppercase; width: 72px;" lguppercase="true" datatype="INT" logmessage="Dias para entrega" class="field" idform="fornecedor" reference="qt_dias_entrega" maxlength="10" id="qtDiasEntrega" name="qtDiasEntrega" type="text"/>
        </div>
      </div>
      <div class="d1-line">
        <div id="" style="width: 280px;" class="element">
          <label class="caption" for="cdMoeda">Moeda</label>
          <select style="width: 277px;" logmessage="Unidade" class="select" idform="fornecedor" reference="cd_moeda" datatype="INT" id="cdMoeda" name="cdMoeda" defaultValue="0">
            <option value="0">Selecione...</option>
          </select>
        </div>
        <div style="width: 100px;" class="element">
          <label class="caption" for="qtDiasUltimaEntrega">Dias &uacute;ltima entrega</label>
          <input style="text-transform: uppercase; width: 97px;" disabled="disabled" lguppercase="true" datatype="INT" logmessage="Dias última entrega" class="disabledField" idform="fornecedor" reference="qt_dias_ultimaEntrega" maxlength="10" id="qtDiasUltimaEntrega" name="qtDiasUltimaEntrega" type="text"/>
        </div>
        <div style="width: 70px;" class="element">
          <label class="caption" for="vlUltimoPreco">&Uacute;ltimo custo</label>
          <input style="text-transform: uppercase; width: 67px;" mask="#,###.00" disabled="disabled" lguppercase="true" datatype="FLOAT" logmessage="Último preço" class="disabledField" idform="fornecedor" reference="vl_ultimo_preco" maxlength="10" id="vlUltimoPreco" name="vlUltimoPreco" type="text"/>
        </div>
      </div>
      <div class="d1-line" id="" style="float:right; width:174px; margin:2px 0px 0px 0px;">
        <div style="width:82px;" class="element">
          <button id="btnSaveFornecedor" title="Gravar fornecedor" onclick="btnSaveFornecedorOnClick();" style="width:80px; height:25px; border:1px solid #999;" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>&nbsp;Gravar</button>
        </div>
        <div style="width:80px;" class="element">
          <button id="btnCancelar" title="Voltar para a janela anterior" onclick="formFornecedor.close();" style="width:80px; height:25px; border:1px solid #999;" class="toolButton"> <img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>&nbsp;Cancelar</button>
        </div>
      </div>
    </div>
</div>

<div id="formReportRegrasItens" class="d1-form" style="display:none; width:594px; height:285px;">
  <div style="width: 594px;" class="d1-body">
    <div class="d1-line">
      <div style="width: 591px; padding:4px 0 0 0" class="element">
        <label class="caption">Selecione a Tabela de Pre&ccedil;o desejada para visualizar as regras aplic&aacute;veis:</label>
        <select style="width: 591px;" class="select" id="cdTabelaPrecoOfRegras" name="cdTabelaPrecoOfRegras">
        </select>
      </div>
    </div>
    <div class="d1-line" style="float:none;">
      <div style="width: 590px; padding:2px 0 0 0" class="element">
        <div id="divGridRegrasItens" style="width: 590px; background-color:#FFF; height:222px; border:1px solid #000000">&nbsp;</div>
      </div>
    </div>
    <div class="d1-line">
      <div style="width: 592px; padding:2px 0 0 0" class="element">
        <button id="" title="Gravar Item" onclick="closeWindow('jReportRegrasItens');" style="float:right; width:60px; height:22px; border:1px solid #999999; font-weight:normal; float:right" class="toolButton">Retornar</button>
      </div>
    </div>
  </div>
</div>
<!--
 *********************************************************************************************************************************
 ******************************************************** Fotos ******************************************************************
 *********************************************************************************************************************************
  -->
<div id="fotoProdutoPanel" class="d1-form" style="display:none; width:501px; height:97px">
  <input idform="fotoProduto" reference="cd_foto" id="cdFoto" name="cdFoto" type="hidden" value="0" defaultValue="0">
  <div style="width: 501px; height: 97px;" class="d1-body">
    <form action="load_foto_produto_session.jsp" method="post" enctype="multipart/form-data" name="frameFotoProduto" target="frameHidden" id="frameFotoProduto">
      <div class="d1-line" id="">
        <div style="width: 465px;" class="element">
          <label class="caption" for="nmFoto">Descrição da foto</label>
          <input style="width: 462px; text-transform:uppercase" lguppercase="true" logmessage="Descrição foto" class="field" idform="fotoProduto" reference="nm_foto" datatype="STRING" maxlength="256" id="nmFoto" name="nmFoto" type="text">
        </div>
        <div style="width: 35px;" class="element">
          <label class="caption" for="nrOrdemFoto">Ordem</label>
          <input style="width: 32px;" logmessage="Nº Ordem" class="field" idform="fotoProduto" reference="nr_ordem" datatype="INT" mask="##" id="nrOrdemFoto" name="nrOrdemFoto" defaultValue="0" type="text">
        </div>
      </div>
      <div class="d1-line" id="">
        <div style="width: 500px;" class="element">
          <label class="caption" for="imgFoto">Foto (se voc&ecirc; n&atilde;o informar a localiza&ccedil;&atilde;o, a foto n&atilde;o ser&aacute; atualizada)</label>
          <input name="imgFoto" type="file" class="field" id="imgFoto" style="width:100%; height: 23px;" size="82" />
        </div>
      </div>
      <div class="d1-line" style="width:499px;">
        <div style="width: 100px; float:right; padding:4px 0px 0px 0px" class="element">
          <button id="btnSaveFotoProduto" title="Gravar foto" onclick="btnSaveFotoProdutoAuxOnClick();" style="margin-bottom:2px; width:100px; height:23px; border:1px solid #999999" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>&nbsp;Gravar foto </button>
        </div>
      </div>
    </form>
  </div>
</div>

<table width="100%" border="0" cellspacing="0" cellpadding="0" id="tableEtiqueta" name="tableEtiqueta" style="margin: 0;"> </table>

</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>
