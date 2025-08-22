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
		int cdEmpresa           = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		Empresa empresa         = EmpresaDAO.get(cdEmpresa);
		int tpProdutoServico    = RequestUtilities.getParameterAsInteger(request, "tpProdutoServico", 0);
		int cdGrupo             = RequestUtilities.getParameterAsInteger(request, "cdGrupo", 0);
		int cdProdutoServico    = RequestUtilities.getParameterAsInteger(request, "cdProdutoServico", 0);
		Grupo grupo             = cdGrupo==0 ? null : GrupoDAO.get(cdGrupo);
		int cdFormulario        = grupo==null ? 0 : grupo.getCdFormulario();
		int cdVinculoFornecedor = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FORNECEDOR", 0);
		int lgPatrimonial       = RequestUtilities.getParameterAsInteger(request, "lgPatrimonial", 0);
		int hiddenClassificacaoFiscal = RequestUtilities.getParameterAsInteger(request, "hiddenClassificacaoFiscal", 0);
		// Parametros para mostrar o preço na consulta, importante para cliente
		int cdTipoOperacaoVarejo  = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, cdEmpresa);
		int cdTipoOperacaoAtacado = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_ATACADO", 0, cdEmpresa);
		int lgFotoProduto         = (tpProdutoServico <= 0 ? ParametroServices.getValorOfParametroAsInteger("LG_FOTO_PRODUTO", 0, cdEmpresa) : 0);
%>
<security:registerForm idForm="formProduto"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<loader:library libraries="shortcut, form, toolbar, grid2.0, treeview2.0, aba2.0, filter, calendario, floatmenu" compress="false"/>
<script language="javascript">
var disabledFormProdutoServico = false;
var disabledFormComponente     = false;
var columnsGrupo = [{label:'Nome grupo', reference: 'NM_GRUPO'}, 
					{label:'Principal', reference: 'LG_PRINCIPAL', type: GridOne._CHECKBOX, onCheck: onClickLgPrincipal}];
var columnsLocalArmazenamento = [{label: 'Nome do local de armazenamento', reference: 'NM_LOCAL_ARMAZENAMENTO'}, 
								 {label: 'Estoque', reference: 'QT_ESTOQUE', type: GridOne._FLOAT},
								 {label: 'Estoque consignado', reference: 'QT_ESTOQUE_CONSIGNADO', type: GridOne._FLOAT},
								 {label: 'Estoque total', reference: 'QT_ESTOQUE_TOTAL', type: GridOne._FLOAT}];
var columnsSimilar = [{label: 'Nome produto', reference: 'NM_PRODUTO_SERVICO'},
					  {label: 'ID', reference: 'ID_PRODUTO_SERVICO'}];
var columnsMovimento = [{label: 'Tipo', reference: 'DS_MOVIMENTO'}, 
						{label: 'Data', reference: 'DT_MOVIMENTO', type: GridOne._DATE}, 
						{label: 'Local Armazenamento', reference: 'NM_LOCAL_ARMAZENAMENTO'}, 
						{label: 'Quantidade', reference: 'QT_MOVIMENTO', type: GridOne._FLOAT}];
var gridGrupos;
var gridLocaisArmazenamento;
var gridEstoquesComponentes;
var gridMovimentos;
var gridSimilares;
var gridReferenciados;
var tabProdutoServico;
var tabComposicao;
var tvComponentes;
var toolBar            = null;
var cdAtributoInEdicao = null;
var atributos          = [];
var cdGrupo                   = <%=cdGrupo%>;
var lgPatrimonial             = <%=lgPatrimonial == 1%>;
var hiddenClassificacaoFiscal = <%=hiddenClassificacaoFiscal == 1%>;
var cdFormularioDefault       = <%=cdFormulario%>;
var cdVinculoFornecedor       = <%=cdVinculoFornecedor%>;
var recursosFloatMenu = null;

function initProdutoServico()	{
    var cdEmpresa = parent.$('cdEmpresa')!=null ? parent.$('cdEmpresa').value : 0;
	$('cdEmpresa').value = cdEmpresa;
	$('cdEmpresa').setAttribute('defaultValue', cdEmpresa);
	if (cdFormularioDefault > 0) {
		$("txtEspecificacao").style.width = (parseInt($("txtEspecificacao").style.width, 10) - 337) + "px";
		$("txtEspecificacao").parentNode.style.width = (parseInt($("txtEspecificacao").style.width, 10) + 3) + "px";
		$("txtDadoTecnico").style.width = (parseInt($("txtDadoTecnico").style.width, 10) - 337) + "px";
		$("txtDadoTecnico").parentNode.style.width = (parseInt($("txtDadoTecnico").style.width, 10) + 3) + "px";
		$("txtPrazoEntrega").style.width = (parseInt($("txtPrazoEntrega").style.width, 10) - 337) + "px";
		$("txtPrazoEntrega").parentNode.style.width = (parseInt($("txtPrazoEntrega").style.width, 10) + 3) + "px";
		$("txtPrazoEntrega").parentNode.parentNode.parentNode.style.width = (parseInt($("txtPrazoEntrega").style.width, 10)) + "px";		
		$("divAtributos").style.display = '';
		loadAtributos(null, cdFormularioDefault);
	}	
	else {
		initProdutoServicoTemp();
	}
}

function initProdutoServicoTemp() {
	toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
								    	     orientation: 'horizontal',
								    		 buttons: [{id: 'btnNewProdutoServico', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo... [Ctrl + N]', onClick: btnNewProdutoServicoOnClick},
								    	     		   {id: 'btnAlterProdutoServico', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar... [Ctrl + A]', onClick: btnAlterProdutoServicoOnClick},
										    		   {id: 'btnSaveProdutoServico', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveProdutoServicoOnClick},
								    	     		   {id: 'btnDeleteProdutoServico', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir... [Ctrl + D]', onClick: btnDeleteProdutoServicoOnClick},
										    		   {separator: 'horizontal'},
												       {id: 'btnAtualizarProdutoServico', img: '/sol/imagens/form-btAtualizar16.gif', label: 'Atualizar', title: 'Atualizar...', onClick: btnAtualizarProdutoServicoOnClick},
										    		   {separator: 'horizontal'},
												       {id: 'btnPesquisar', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar...', onClick: btnFindProdutoServicoOnClick},
													   {separator: 'horizontal'},
													   {id: 'btnRecursos', img: '../imagens/opcoes16.gif', label: 'Outras Opções', title: 'Outras Opções', onClick: btnRecursosOnClick}]});

	var toolBarComponentes = ToolBar.create('toolBarComponentes', {plotPlace: 'toolBarComponentes',
								    	     orientation: 'horizontal',
								    		 buttons: [{id: 'btnNewComponente', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo componente ', onClick: btnNewComponenteOnClick},
								    	     		   {id: 'btnAlterComponente', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar componente', onClick: btnAlterComponenteOnClick},
										    		   {id: 'btnSaveComponente', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveComponenteOnClick},
								    	     		   {id: 'btnDeleteComponente', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir componente ', onClick: btnDeleteComponenteOnClick},
										    		   {separator: 'horizontal'},
												       {id: 'btnPesquisarComponente', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar componente', onClick: btnFindComponenteOnClick},
												       {id: 'btnPrintProdutoServico', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', title: 'Imprimir estrutura', onClick: btnPrintComponenteOnClick}]});

	addShortcut('ctrl+0', function(){ tabProdutoServico.showTab(0) });
	addShortcut('ctrl+1', function(){ tabProdutoServico.showTab(1) });
	addShortcut('ctrl+2', function(){ tabProdutoServico.showTab(2) });
	addShortcut('ctrl+3', function(){ tabProdutoServico.showTab(3) });
	addShortcut('ctrl+n', function(){ if (!$('btnNewProdutoServico').disabled) btnNewProdutoServicoOnClick() });
	addShortcut('ctrl+a', function(){ if (!$('btnAlterProdutoServico').disabled) btnAlterProdutoServicoOnClick() });
	addShortcut('ctrl+p', function(){ if (!$('btnFindProdutoServico').disabled) btnFindProdutoServicoOnClick() });
	addShortcut('ctrl+e', function(){ if (!$('btnDeleteProdutoServico').disabled) btnDeleteProdutoServicoOnClick() });
	addShortcut('ctrl+x', function(){parent.closeWindow('jProdutoServico')});
	addShortcut('shift+m', function(){ if (!$('btnConsultarMovimento').disabled) { tabProdutoServico.showTab(3); btnConsultarMovimentoOnClick()} });
	$('nmProdutoServico').nextElement  = $('txtProdutoServico');
	$('txtProdutoServico').nextElement = $('sgProdutoServico');

	loadOptions($('tpControleEstoque'), <%=sol.util.Jso.getStream(ProdutoServicoEmpresaServices.tiposControleEstoque)%>);
	loadOptions($('tpReabastecimento'), <%=sol.util.Jso.getStream(ProdutoServicoEmpresaServices.tiposAbastecimento)%>);
	loadTabelasPrecos();

	loadOptionsFromRsm($('cdUnidadeMedida'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.UnidadeMedidaServices.getAll())%>, {fieldValue: 'cd_unidade_medida', fieldText:'nm_unidade_medida'});
	loadOptionsFromRsm($('cdUnidadeMedidaForn'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.UnidadeMedidaServices.getAll())%>, {fieldValue: 'cd_unidade_medida', fieldText:'nm_unidade_medida'});
	loadOptionsFromRsm($('cdUnidadeMedidaComponente'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.UnidadeMedidaDAO.getAll())%>, {fieldValue: 'cd_unidade_medida', fieldText:'nm_unidade_medida'});
	loadOptionsFromRsm($('cdMoeda'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.MoedaDAO.getAll())%>, {fieldValue: 'cd_moeda', fieldText:'nm_moeda'});

	$('idReduzido').nextElement = $('qtDiasGarantia');
	$('qtDiasGarantia').nextElement = $('tpControleEstoque');
	$('tpControleEstoque').nextElement = $('tpReabastecimento');
	$('tpReabastecimento').nextElement = $('qtIdeal');
	$('qtDiasEstoqueLocalArmazenamento').nextElement = $('btnSaveLocalArmazenamento');
	$('txtPrazoEntrega').nextElement = $('vlLargura');
	$('vlPesoUnitarioEmbalagem').nextElement = $('btnSaveProdutoServico');

	var dtDesativacaoMask = new Mask($("dtDesativacao").getAttribute("mask"), "date");
	dtDesativacaoMask.attach($("dtDesativacao"));

	var prDepreciacaoMask = new Mask($("prDepreciacao").getAttribute("mask"), "number");
	prDepreciacaoMask.attach($("prDepreciacao"));

	enableTabEmulation();
    produtoServicoFields = [];
	localArmazenamentoFields = [];
	aliquotaIcmsFields = [];
	loadFormFields(["produtoServico", "localArmazenamento", "aliquotaIcms", "preco", "fornecedor", "componente", "fotoProduto"]);

    var dataMask = new Mask("##/##/####");
	var maskInteger = new Mask("#,###", "number");
	var maskNumber = new Mask("#,###.00", "number");

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
	maskNumber.attach($("vlPreco"));
	maskNumber.attach($("qtProdutoServico"));
	maskNumber.attach($("prPerda"));
	maskInteger.attach($("qtEmbalagem"));

	changeLayout();
	changeLayoutGrupos((gridGrupos != null ? gridGrupos.size() : 0));
	
	loadSimilares();
	loadReferenciados();
	loadPrecos();
	loadTributos();
	loadFornecedores();
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
		alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField");
	}
	else {
		clearFormProdutoServico();
	    $('nmProdutoServico').focus();
	}

	<% if (cdProdutoServico > 0) { %>
			loadProduto(null, <%=cdProdutoServico%>);
	<% } %>
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
		try {rsmRegrasItens = eval('(' + content + ')')} catch(e) {}
		gridRegrasItens = GridOne.create('gridRegrasItens', {columns: columnsRegrasItens,
					     resultset :rsmRegrasItens, 
					     plotPlace : $('divGridRegrasItens'),
						 columnSeparator: false,
						 noSelectorColumn: true,
						 noSelectOnCreate: true,
						 onProcessRegister: function(reg) {
						 						var dsCriterios = '';
												var nmGrupo = reg['CD_GRUPO']==null || reg['CD_GRUPO']==0 ? null : reg['NM_GRUPO'];
												if (reg['CD_GRUPO']!=null && reg['CD_GRUPO']!=0 && reg['NM_GRUPO']!='' && reg['CD_GRUPO_SUPERIOR']!=0 && reg['NM_GRUPO_SUPERIOR']!=null && reg['NM_GRUPO_SUPERIOR']!='')
													nmGrupo = reg['NM_GRUPO_SUPERIOR'] + ' - ' + nmGrupo;
												var nmProdutoServico = reg['CD_PRODUTO_SERVICO_REGRA']==0 ? null : reg['NM_PRODUTO_SERVICO_REGRA'];
												var nmFornecedor = reg['CD_FORNECEDOR']==0 ? null : reg['NM_FORNECEDOR'];
												var nmTabelaPrecoBase = reg['CD_TABELA_PRECO_BASE']==0 ? null : reg['NM_TABELA_PRECO_BASE'];
												dsCriterios += (nmGrupo!=null ? 'Grupo: ' + nmGrupo : '');
												dsCriterios += (nmProdutoServico!=null ? (dsCriterios!='' ? '<br />' : '') + 'Produto/Serviço: ' + nmProdutoServico : '');
												dsCriterios += (nmFornecedor!=null ? (dsCriterios!='' ? '<br />' : '') + 'Fornecedor: ' + nmFornecedor : '');
												dsCriterios += (nmTabelaPrecoBase!=null ? (dsCriterios!='' ? '<br />' : '') + 'Tabela de Preço Base: ' + nmTabelaPrecoBase : '');
												reg['CLDS_CRITERIOS'] = dsCriterios;
												reg['CLTP_VALOR_BASE'] = reg['TP_VALOR_BASE']==null ? '' : reg['TP_VALOR_BASE']==<%=TabelaPrecoRegraServices.TP_BASE_ULTIMO_CUSTO%> ? 'Último Custo (' + $('vlUltimoCusto').value + ')' :
																		   reg['TP_VALOR_BASE']==<%=TabelaPrecoRegraServices.TP_BASE_CUSTO_MEDIO%> ? 'Custo Médio (' + $('vlCustoMedio').value + ')' : 
																		   'Tabela Base (' + formatCurrency(reg['VL_PRECO_BASE']) + ')';
										 },
					     onSelect : null});
		createWindow('jReportRegrasItens', {caption: "Regras de Preços aplicáveis a este Produto",
									  width: 604, height: 315,
									  noDropContent: true, columnSeparator: false,
									  modal: true, noDrag: true,
									  contentDiv: 'formReportRegrasItens'});
	}
}

function btnEtiquetaOnClick(qtEtiquetas)	{
	if(qtEtiquetas <= 0){
		FormFactory.createFormWindow('jEtiqueta', 
		        {caption: "Listando Etiquetas", width: 150, height: 90, noDrag: true, modal: true, id: 'etiqueta', unitSize: '%', cssVersion: '2',
				 lines: [[{type: 'text', label: 'Qtd. Etiquetas', id: 'qtEtiqueta', width: 100}],
				         [{id:'btnPrint',  type:'button', label:'Imprimir', width:50, height: 20, onClick: function(){btnEtiquetaOnClick($('qtEtiqueta').value);}}, 
						  {id:'btnCancel', type:'button', label:'Cancelar', width:50, height: 20, onClick: function(){ closeWindow('jEtiqueta'); }}]],
				 focusField:'qtEtiqueta'});
	}
	else	{
		$('tableEtiqueta').style.display = 'table';
		var table   = $('tableEtiqueta');
		while (table.rows.length > 0)
			table.deleteRow(0);
		var qtTd = 0;
		var tr   = document.createElement('tr');
		var reg = {ID_REDUZIDO: $('idReduzido').value, CL_NOME: $('nmProdutoServico').value, CD_PRODUTO_SERVICO: $('cdProdutoServico').value};
		for(var l=0; l<qtEtiquetas; l++)	{
			qtTd++;
			var tdSpace = document.createElement('td');
			tdSpace.innerHTML = '<div style="height:25mm; width: 5mm;"/>';
			var td0 = document.createElement('td');
			td0.width             = '9%';
			td0.style.cssText    += 'border-top: 1px solid #000; border-bottom: 1px solid #000; border-left: 1px solid #000;';
			td0.innerHTML         = '&nbsp';
			var td1 = document.createElement('td');
			td1.style.cssText    += 'border-top: 1px solid #000; border-right: 1px dashed #000; border-bottom: 1px solid #000; ';
			td1.style.borderSize  = '1px';
			td1.style.fontSize    = '8px';
			td1.width             = '20%';
			td1.align             = 'center';
			td1.innerHTML =  reg['CL_NOME']+'<br/>'+
				             '<img src="../barcode?cdBarcode='+reg['ID_REDUZIDO']+'&showDig=false&height=15&barcodeType=<%=com.tivic.manager.util.BarcodeGenerator._Code128Bean%>" style="height:8mm;"/>'+
			                 '<br/>'+reg['ID_REDUZIDO']+
				             '<br/><%=empresa.getNmPessoa()%>'+
				             '<br/>Prazo para troca: 15 dias ';
				             
			var td2 = document.createElement('td');
			td2.width           = '5%';
			td2.style.cssText  += 'border-top: 1px solid #000; border-right: 1px solid #000; border-bottom: 1px solid #000; border-left: 0px';
			td2.innerHTML       = '<div style="height: 20px; width: 60px; font-weight: bold; -webkit-transform: rotate(-90deg); -moz-transform: rotate(-90deg);">'+
			                      'R$ '+formatCurrency(reg['VL_PRECO'])+'</div>';   
			//
			if(qtTd>1)
				tr.appendChild(tdSpace);
			tr.appendChild(td0);
			tr.appendChild(td1);
			tr.appendChild(td2);
			if(qtTd==3)	{
				qtTd = 0;
				table.appendChild(tr);
				tr        = document.createElement('tr');
				tr.height = '2.2mm';
				var tdSpace = document.createElement('td');
				tdSpace.innerHTML = '<div style="height:2.2mm;"/>';
				tr.appendChild(tdSpace);
				table.appendChild(tr);
				//
				tr = document.createElement('tr');
			}
		}
		if(qtTd>0)
			table.appendChild(tr);
		parent.ReportOne.create('jReportEtiquetas', {width: 702, height: 478, modal: true, caption: 'Etiquetas',
			                                         resultset: {lines:[{}]},
													 detailBand: {contentModel: $('tableEtiqueta')},
											 		 orientation: 'portraid', paperType: 'A4', tableLayout: 'fixed',
													 displayReferenceColumns: false});
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
			$('vlCustoMedio').value = hash!=null ? formatCurrency(parseFloat(hash['vlCustoMedio'], 10)) : $('vlCustoMedio').value;
			$('vlPrecoMedio').value = hash!=null ? formatCurrency(parseFloat(hash['vlPrecoMedio'], 10)) : $('vlPrecoMedio').value;
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
	if (option > 1) { 
		if (lgPatrimonial) {
			$('divGridGruposParent').style.width = '618px';
			$('divGridGrupos').style.width = '616px';
			$('divGridGrupos').style.height = '85px';
		}
		$('txtProdutoServico').style.height = (lgPatrimonial ? '40px': '65px');
		$('divGridGruposSuperior').style.display = '';
		$('divGruposSuperior').style.display = 'none';
	} else {
		if (lgPatrimonial) {
			$('divBodyEndereco').style.width = '612px';
			$('divCdGrupoView').style.width = '572px';
			$('cdGrupoView').style.width = '569px';
		}
		$('txtProdutoServico').style.height = (lgPatrimonial ? '97px': '139px');
		$('divGridGruposSuperior').style.display = 'none';
		$('divGruposSuperior').style.display = '';
	}

    //Muda layout se foi informado algum grupo no chamado do formulário
	if (cdGrupo > 0) {
		$('divGridSimilaresSuperior1').appendChild($('divGridSimilaresSuperior2'));
		$('divGridSimilares').style.height = '105px';
		$('divGridReferenciados').style.height = '105px';

		$('divGridGruposSuperior').style.display = 'none';
		$('divGruposSuperior').style.display = 'none';

		$('txtProdutoServico').style.height = '63px';
	}
	
	if (gridGrupos != null && gridGrupos.getSelectedRow()) {
		$('cdGrupo').value = gridGrupos.getSelectedRow().register['CD_GRUPO'];
		$('cdGrupoView').value = gridGrupos.getSelectedRow().register['NM_GRUPO'];
	}
}

function changeLayout() {
	if (lgPatrimonial) { 
		$('divNmProdutoServico').style.width = '643px';
		$('nmProdutoServico').style.width = '640px';

		$('divTxtProdutoServico').style.width = '643px';
		$('txtProdutoServico').style.width = '640px';

		$('divSgProdutoServico').style.width = '81px';
		$('sgProdutoServico').style.width = '78px';

		$('divIdProdutoServico').style.width = '135px';
		$('idProdutoServico').style.width = '132px';

		$('divCdClassificacaoFiscal').style.display = (hiddenClassificacaoFiscal ? 'none' : '');
		if (hiddenClassificacaoFiscal) {
			$('divCdFabricante').style.width = '643px';
			$('cdFabricanteView').style.width = '640px';
		}
		else {
			$('divCdFabricante').style.width = '322px';
			$('cdFabricanteView').style.width = '319px';

			$('divCdClassificacaoFiscal').style.width = '321px';
			$('nmClassificacaoFiscalView').style.width = '318px';
		}
		toolBar.hideButton('btnRecursos');
	}
	else {
		tabProdutoServico = TabOne.create('tabProdutoServico', {width: 644,
																height: 380,
																tabs: [{caption: 'Dados básicos', 
																		reference:'divAbaDadosBasicos',
																		active: true},
																	   {caption: 'Outras informações', 
																		reference:'divAbaEspecificacoes'},
																	   {caption: 'Preços / Fornecedores / Tributos', 
																		reference:'divAbaConfigFinanceira'},
																	   {caption: 'Composição',
																		reference:'divAbaComposicao'}
																		<% if (lgFotoProduto == 1) { %> 
																			,{caption: 'Fotos', 
																		 	  reference: 'divAbaFotoProduto'}
																		<% } %>
																	  ],
																plotPlace: 'divTabProdutoServico',
																tabPosition: ['top', 'left']});
	
		tabComposicao = TabOne.create('tabComposicao', {width: 441,
														height: 205,
														tabs: [{caption: 'Componentes', 
																reference:'divAbaSubComponentes',
																active: true,
																image: 'imagens/template16.gif'},
															   {caption: 'Estoques', 
																reference:'divAbaEstoquesComponentes',
																image: 'imagens/organizacao16.gif'},
															   {caption: 'Fornecedores',
																reference: 'divAbaFornecedoresComponentes',
																image: 'imagens/link16.gif'},
															   {caption: 'Similares / Referenciados',
																reference: 'divAbaSimilaresComponentes',
																image: 'imagens/conteudo16.gif'}],
														plotPlace: 'divTabComposicao',
														tabPosition: ['top', 'left']});
		
	}
	$('divCdUnidadeMedida').style.display = (lgPatrimonial ? 'none' : '');
	$('divQtEmbalagem').style.display = (lgPatrimonial ? 'none' : '');
	$('divIdReduzido').style.display = (lgPatrimonial ? 'none' : '');
	// $('divNrOrdem').style.display = (lgPatrimonial ? 'none' : '');
	// $('divDtDesativacao').style.display = (lgPatrimonial ? 'none' : '');
	$('divStProdutoEmpresa').style.display = (lgPatrimonial ? 'none' : '');
	$('divStProdutoEmpresa2').style.display = (lgPatrimonial ? 'none' : '');
	$('divProdutoServicoLinha1').style.display = (lgPatrimonial ? 'none' : '');
	$('divProdutoServicoLinha2').style.display = (lgPatrimonial ? 'none' : '');
	$('divGridSimilaresSuperior1').style.display = (lgPatrimonial ? 'none' : '');
	$('divProdutoServicoLinha3').style.display = (lgPatrimonial ? 'none' : '');

	$('divCdClassificacao').style.display = (lgPatrimonial ? '' : 'none');
	$('divPrDepreciacao').style.display = (lgPatrimonial ? '' : 'none');
	$('divProdutoServicoLinha4').style.display = (lgPatrimonial ? '' : 'none');
	
	if (cdGrupo > 0) {
		$('txtEspecificacao').style.height = '92px';
		$('txtDadoTecnico').style.height = '92px';
		$('txtPrazoEntrega').style.height = '90px';
	}
}
function formValidationProdutoServico(){
	var campos = [];
    campos.push([$("nmProdutoServico"), 'Indique o nome do produto', VAL_CAMPO_NAO_PREENCHIDO]);
	if (!lgPatrimonial) {
		campos.push([$("cdUnidadeMedida"), 'Indique a unidade', VAL_CAMPO_MAIOR_QUE, 0]);
	}
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
				"&method=getAllOfEmpresa(const " + $('cdEmpresa').value + ":int)");
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
    disabledFormProdutoServico = false;
    clearFields(produtoServicoFields);
	getNextIdReduzido();
	loadSimilares();
	loadReferenciados();
	loadPrecos();
	loadFornecedores();
	loadTributos();
	if (cdGrupo <= 0) { 
		loadGrupos();
	}

    alterFieldsStatus(true, produtoServicoFields, "nmProdutoServico");
	$('stProdutoEmpresa').value = 1;
	$('stProdutoEmpresa').checked = true;
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
			var txtEspecificacao = (lgPatrimonial ? $('txtEspecificacaoPatrimonial').value : $('txtEspecificacao').value);
			var txtDadoTecnico = (lgPatrimonial ? $('txtDadoTecnicoPatrimonial').value : $('txtDadoTecnico').value);
			var txtPrazoEntrega = (lgPatrimonial ? $('txtPrazoEntregaPatrimonial').value : $('txtPrazoEntrega').value);
            
			var executionDescription = $("cdProdutoServico").value>0 ? formatDescriptionUpdate("ProdutoServico", $("cdProdutoServico").value, $("dataOldProdutoServico").value, produtoServicoFields) : formatDescriptionInsert("ProdutoServico", produtoServicoFields);
            var constructionProdutoEmpresa = 'cdEmpresa: int, cdProdutoServico: int, cdUnidadeMedida: int, idReduzido: String, vlPrecoMedio: float, vlCustoMedio: float, vlUltimoCusto: float,const '+changeLocale('qtIdeal')+': float, qtMinima: float, qtMaxima: float, qtDiasEstoque: float, qtPrecisaoCusto: int, qtPrecisaoUnidade: int, qtDiasGarantia: int, tpReabastecimento: int, tpControleEstoque:int, tpTransporte:int, stProdutoEmpresa:int, dtDesativacao:GregorianCalendar, nrOrdem:String, lgEstoqueNegativo:int';
			var constructionProdutoServico = 'cdProdutoServico: int, cdCategoria: int, nmProdutoServico: String, txtProdutoServico: String, const ' + txtEspecificacao + ': String, const ' + txtDadoTecnico + ': String, const ' + txtPrazoEntrega + ': String, tpProdutoServico: int, idProdutoServico: String, sgProdutoServico: String, cdClassificacaoFiscal:int, cdFabricante: int, cdMarca: int, nmModelo: String, const 0:int, const  :String';
			var constructionProduto = constructionProdutoServico + ', vlPesoUnitario:float, vlPesoUnitarioEmbalagem:float, vlComprimento:float, vlLargura:float, vlAltura:float, vlComprimentoEmbalagem:float, vlLarguraEmbalagem:float, vlAlturaEmbalagem:float, qtEmbalagem:int';
			var constructionBem = constructionProdutoServico + ', cdClassificacao:int, prDepreciacao:float';
			var constructionProdutoGrupo = 'cdProdutoServico: int, cdGrupo: int, cdEmpresa: int, const 0: int';
			var objectsAtributos = 'atributos=java.util.ArrayList()';
			var commandsExecute = '';
			for (var i = 0; i < atributos.length; i++) {
				var atributo = atributos[i];
				var cdFormularioAtributo = atributo['cdFormularioAtributo'];
				var tpDado = atributo['tpDado'];
				var cdEmpresa = 0;
				var cdOpcao = tpDado==<%=FormularioAtributoServices.TP_OPCOES%> ? $('atributo_' + cdFormularioAtributo).value : 0;
				var cdDocumento = 0;
				var cdPessoa = tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? $('atributo_' + cdFormularioAtributo).value : 0;
				objectsAtributos += ';atributo' + i + '=com.tivic.manager.grl.FormularioAtributoValor(const ' + cdFormularioAtributo + ':int, const 0:int, cdProdutoServico:int, const ' + cdOpcao + ':int, const ' + cdEmpresa + ':int, const ' + cdDocumento + ':int, ' + (tpDado==<%=FormularioAtributoServices.TP_OPCOES%> || tpDado==<%=FormularioAtributoServices.TP_PESSOA%> ? 'const ' : ('atributo_' + cdFormularioAtributo)) + ':String, const 0:int, const ' + cdPessoa + ':int, const 0:int)';
				commandsExecute += 'atributos.add(*atributo' + i + ':java.lang.Object);';
			}
			var className = "";
			var method = "";
			className = "com.tivic.manager.grl.ProdutoServicoEmpresaServices";
			if (lgPatrimonial) {
				method = "new com.tivic.manager.grl.Produto(" + constructionProduto + "):com.tivic.manager.grl.ProdutoServico, " +
						 "new com.tivic.manager.grl.ProdutoServicoEmpresa(" + constructionProdutoEmpresa + "):com.tivic.manager.grl.ProdutoServicoEmpresa, " +
						 "const " + cdGrupo + ":int, " + 
						 ($('cdGrupo').value > 0 ? ", new com.tivic.manager.alm.ProdutoGrupo(" + constructionProdutoGrupo + "):com.tivic.manager.alm.ProdutoGrupo, " : ", const null, ") +
						 "new com.tivic.manager.bpm.Bem(" + constructionBem + "):com.tivic.manager.bpm.Bem";
			}
			else {
				method = "new com.tivic.manager.grl.Produto(" + constructionProduto + "):com.tivic.manager.grl.ProdutoServico, " +
						 "new com.tivic.manager.grl.ProdutoServicoEmpresa(" + constructionProdutoEmpresa + "):com.tivic.manager.grl.ProdutoServicoEmpresa, " +
						 "const " + cdGrupo + ":int, *atributos:java.util.ArrayList" +
						 ($('cdGrupo').value > 0 ? ", new com.tivic.manager.alm.ProdutoGrupo(" + constructionProdutoGrupo + "):com.tivic.manager.alm.ProdutoGrupo" : "");
			}
			if($("cdProdutoServico").value > 0)	{
                getPage("POST", "btnSaveProdutoServicoOnClick", "../methodcaller?className=" + className +
					"&execute=" + commandsExecute +
					"&objects=" + objectsAtributos +
                    "&method= update(" + method + ")", produtoServicoFields, null, null, executionDescription);
			}														  
            else	{
                getPage("POST", "btnSaveProdutoServicoOnClick", "../methodcaller?className="+className +
					"&execute=" + commandsExecute +
					"&objects=" + objectsAtributos +
                    "&method=insert(" + method + ")", produtoServicoFields, null, null, executionDescription);
        	}
		}
    }
    else{
        var ok = parseInt(content, 10) > 0;
		$("cdProdutoServico").value = $("cdProdutoServico").value<=0 && ok ? parseInt(content, 10) : $("cdProdutoServico").value;
        if(ok){
            disabledFormProdutoServico=true;
            alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField");
            createTempbox("jMsg", {width: 200, height: 50, message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO", time: 2000});
            $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
        }
        else{
			var msg = "ERRO ao tentar gravar dados!";
			if (parseInt(content, 10) == <%=ProdutoServicoEmpresaServices.ERR_ID_REDUZIDO_EM_USO%>)
				msg += ". ID informado já se encontra em uso.";
            createTempbox("jMsg", {width: 200, height: 50, message: msg, tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnFindProdutoServicoOnClick(reg) {
    if(!reg){
		var hiddenFields = [{reference:"A.TP_PRODUTO_SERVICO", value:0, comparator:_EQUAL, datatype:_INTEGER},
							{reference:"cd_empresa", value:$("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER}];
		
        var filterFieldsProdutoServico = [[{label:"Nome do produto", reference:"nm_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60, charcase:'uppercase'},
                                           {label:"Código de Barras", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
										   {label:"Referência", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:20, charcase:'uppercase'}],
										  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:(cdGrupo > 0 ? 100 : 60), charcase:'uppercase'}]];
        var columnsFields = [{label:"ID/Cód. reduzido", reference:"id_reduzido"},
							 {label:"Nome do produto/serviço", reference:"NM_PRODUTO_SERVICO"},
							 <%=cdTipoOperacaoVarejo>0 ?"{label:\"Preço Varejo\", reference:\"vl_preco_"+cdTipoOperacaoVarejo+"\", type: GridOne._CURRENCY},":""%>
							 <%=cdTipoOperacaoAtacado>0?"{label:\"Preço Atacado\", reference:\"vl_preco_"+cdTipoOperacaoAtacado+"\", type: GridOne._CURRENCY},":""%>		                   	 
							 {label:"Estoque", reference:"QT_EMBALAGEM"},
							 {label:"Tamanho", reference:"TXT_DADO_TECNICO", cssStyle: 'charcase:uppercase;'},
							 {label:"Cor", reference:"TXT_ESPECIFICACAO"},
							 {label:"Fabricante", reference:"nm_fabricante"},
							 {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
							 {label:"Grupo do produto", reference:"NM_GRUPO"}];
			
		if (cdGrupo > 0) {
			 hiddenFields.push({reference:"cd_grupo", value:cdGrupo, comparator:_EQUAL, datatype:_INTEGER});
		}
		else {
			// Incluído para apresentação de lançamento de grupo sem usar o grid
			hiddenFields.push({reference:"findGrupoPrincipal", value: 0, comparator:_EQUAL, datatype:_INTEGER});
			filterFieldsProdutoServico[1].push({label:"Grupo principal", reference:"nm_grupo", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:40, charcase:'uppercase'});
		}
		var className = "";
		if (lgPatrimonial) {
			className = "com.tivic.manager.bpm.BemServices";
			hiddenFields = [{reference:"B.cd_empresa", value:$("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER}];
		}
		else {
			className = "com.tivic.manager.alm.ProdutoEstoqueServices";
		}
		FilterOne.create("jFiltro", {caption:"Pesquisar produtos", width: 640,height: 400, modal: true, noDrag: true, noTitle: true,
												   	className: className, method: "findCompleto",
												   	filterFields: filterFieldsProdutoServico,
												   	gridOptions: {columns: columnsFields, strippedLines: true, columnSeparator: false, lineSeparator: false},
												   	hiddenFields: hiddenFields,
												   	callback: btnFindProdutoServicoOnClick, 
													autoExecuteOnEnter: true});
    }
    else {// retorno
        closeWindow("jFiltro");
        disabledFormProdutoServico=true;
        alterFieldsStatus(false, produtoServicoFields, "nmProdutoServico", "disabledField");
        loadFormRegister(produtoServicoFields, reg[0]);
        $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
		if ($('nmClassificacaoFiscalView') != null) {
			if (reg[0]['CD_CLASSIFICACAO_FISCAL']==null || reg[0]['CD_CLASSIFICACAO_FISCAL']==0)
				$('nmClassificacaoFiscalView').value = '';
			else 
				$('nmClassificacaoFiscalView').value = (trim(reg[0]['ID_CLASSIFICACAO_FISCAL'])!='' ? reg[0]['ID_CLASSIFICACAO_FISCAL'] + ' - ' : '') + reg[0]['NM_CLASSIFICACAO_FISCAL'];
		}
		$("cdEmpresa").value = $("cdEmpresa").getAttribute("defaultValue");
		if(reg[0]['VL_PRECO_<%=cdTipoOperacaoVarejo%>']>0)
			$('qtIdeal').value = formatCurrency(reg[0]['VL_PRECO_<%=cdTipoOperacaoVarejo%>']);
        /* CARREGUE OS GRIDS AQUI */
		setTimeout('loadAtributosProdutoServico()', 1);
		setTimeout('loadSimilares(); loadReferenciados(); loadPrecos(); loadFornecedores();', 1);
		setTimeout('loadTributos()', 1);
		setTimeout('loadGrupos()', 1);
		setTimeout('initComponente()', 1);
		<% if (lgFotoProduto == 1) { %>
			setTimeout('loadFotoProduto()', 10);
		<% } %>

        $("dataOldProdutoServico").value = captureValuesOfFields(produtoServicoFields);
		if (!$("nmProdutoServico").disabled)
	        $("nmProdutoServico").focus();
    }
}

function btnDeleteProdutoServicoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("ProdutoServico", $("cdProdutoServico").value, $("dataOldProdutoServico").value);
    var className = "";
	var method = "";
	if (lgPatrimonial) {
		className = "com.tivic.manager.bpm.BemDAO";
		method = "delete(const " + $("cdProdutoServico").value + ":int):int";
	}
	else {
		className = "com.tivic.manager.grl.ProdutoServicoEmpresaServices";
		method = "delete(const " + $("cdEmpresa").value + ":int, const " + $("cdProdutoServico").value + ":int):int";
	}
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

function btnFindCdClassificacaoOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar classificação patrimonial", 
												   width: 450,
												   height: 325,
												   top:65,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.bpm.ClassificacaoDAO",
												   method: "find",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome", reference:"nm_classificacao", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Nome", reference:"nm_classificacao"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: null,
												   callback: btnFindCdClassificacaoOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
		filterWindow.close();
		$('cdClassificacao').value = reg[0]['CD_CLASSIFICACAO'];
		$('nmClassificacaoView').value = (reg[0]['NR_CLASSIFICACAO']==null ? '' : (reg[0]['NR_CLASSIFICACAO'] + " - ")) + reg[0]['NM_CLASSIFICACAO'];
	}
}

function btnClearCdClassificacaoOnClick(){
	$('cdClassificacao').value = 0;
	$('nmClassificacaoView').value = '';
}

function btnFindCdClassificacaoFiscalOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Situação Tributária", width: 450, height: 325, modal: true, noDrag: true,
								     className: "com.tivic.manager.adm.ClassificacaoFiscalDAO", method: "find", allowFindAll: true,
									   filterFields: [[{label:"Nome", reference:"nm_classificacao_fiscal", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
									   gridOptions: {columns: [{label:"Nome", reference:"nm_classificacao_fiscal"}],
												   strippedLines: true, columnSeparator: false, lineSeparator: false},
									   callback: btnFindCdClassificacaoFiscalOnClick, 
									   autoExecuteOnEnter: true });
    }
    else {// retorno
		closeWindow('jFiltro');
		$('cdClassificacaoFiscal').value     = reg[0]['CD_CLASSIFICACAO_FISCAL'];
		$('nmClassificacaoFiscalView').value = (reg[0]['ID_CLASSIFICACAO_FISCAL']==null ? '' : (reg[0]['ID_CLASSIFICACAO_FISCAL'] + " - ")) + reg[0]['NM_CLASSIFICACAO_FISCAL'];
	}
}

function btnClearCdClassificacaoFiscalOnClick(){
	$('cdClassificacaoFiscal').value = 0;
	$('nmClassificacaoFiscalView').value = '';
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
 ********************************************* SIMILARES *************************************************************************
 *********************************************************************************************************************************/
function loadSimilares(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		getPage("GET", "loadSimilares", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
				"&method=getAllSimilares(const " + $('cdProdutoServico').value + ":int, const 0: int)");
	}
	else {
		var rsmSimilares = null;
		try {rsmSimilares = eval('(' + content + ')')} catch(e) {}
		gridSimilares = GridOne.create('gridSimilares', {columns: columnsSimilar,
					     resultset: rsmSimilares, 
					     plotPlace: $('divGridSimilares'),
					     onSelect: null});
	}
}

var produtoServicoSimilarTemp = null;
function btnNewProdutoSimilarOnClick(reg){
    if(!reg){
		if ($('cdProdutoServico').value == 0)
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Inclua ou localize um produto para especificar similares.",
                                  msgboxType: "INFO"})
		else {
			var hiddenFieldsProdutoServico = [{reference:"A.tp_produto_servico", value:0, comparator:_EQUAL, datatype:_INTEGER},
											  {reference:"B.cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL, datatype:_INTEGER},
											  {reference:"A.cd_produto_servico", value:$("cdProdutoServico").value, comparator:_DIFFERENT, datatype:_INTEGER}];
			
			if (cdGrupo > 0)
				hiddenFieldsProdutoServico.push({reference:"cd_grupo", value:cdGrupo, comparator:_EQUAL, datatype:_INTEGER});
			var filterFieldsProdutoServico = [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
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
													    callback: btnNewProdutoSimilarOnClick, 
														autoExecuteOnEnter: true});



		}
    }
    else {// retorno
        filterWindow.close();
		produtoServicoSimilarTemp = reg[0];
		setTimeout('btnNewProdutoSimilarAuxOnClick()', 1);
    }
}

function btnNewProdutoSimilarAuxOnClick(content){
    if(content==null){
		var cdSimilar = produtoServicoSimilarTemp["CD_PRODUTO_SERVICO"];
		var nmSimilar = produtoServicoSimilarTemp['NM_PRODUTO_SERVICO'];
		var cdProdutoServico = $('cdProdutoServico').value;
		var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		var executionDescription = "Inclusão de produto similar " + nmSimilar + " (Cód. " + cdSimilar + ") " + produtoServicoDescription;		
		getPage("GET", "btnNewProdutoSimilarAuxOnClick", "../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO" +
														 "&method=insert(new com.tivic.manager.grl.ProdutoSimilar(cdProdutoServico: int, cdSimilar: int, const 0: int):com.tivic.manager.grl.ProdutoSimilar)" +
												  		 "&cdProdutoServico=" + cdProdutoServico +
												  		 "&cdSimilar=" + cdSimilar, null, null, null, executionDescription);
    }
    else{
        var ok = parseInt(content, 10) > 0;
		if (ok)
			gridSimilares.addLine(0, produtoServicoSimilarTemp, null, true)	
		else
            createTempbox("jMsg", {width: 200, 
								   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR", time: 3000});
    }
}

function btnDeleteProdutoSimilarOnClick(content)	{
	if(content==null) {
		if (gridSimilares.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o produto similar que deseja excluir.');
		else {
			var cdSimilar = gridSimilares.getSelectedRow().register['CD_PRODUTO_SERVICO'];
			var nmSimilar = gridSimilares.getSelectedRow().register['NM_PRODUTO_SERVICO'];
			var cdProdutoServico = $('cdProdutoServico').value;
			var produtoServicoDescription = $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value;
		    var executionDescription = "Exclusão do similar " + nmSimilar + " (Cód. " + cdSimilar + ") da relação de similares do produto " + produtoServicoDescription;	
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o produto similar selecionado?', 
							function() {
								getPage('GET', 'btnDeleteProdutoSimilarOnClick', 
										'../methodcaller?className=com.tivic.manager.grl.ProdutoSimilarDAO'+
										'&method=delete(const ' + cdProdutoServico + ':int, const ' + cdSimilar + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridSimilares.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'ERRO ao tentar excluir produto similar!');
	}
}

/*********************************************************************************************************************************
 ******************************************* REFERENCIADOS ***********************************************************************
 *********************************************************************************************************************************/
function loadReferenciados(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		getPage("GET", "loadReferenciados", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
				"&method=getAllSimilares(const " + $('cdProdutoServico').value + ":int, const 1: int)");
	}
	else {
		var rsmReferenciados = null;
		try {rsmReferenciados = eval('(' + content + ')')} catch(e) {}
		gridReferenciados = GridOne.create('gridReferenciados', {columns: columnsSimilar,
					     resultset: rsmReferenciados, 
					     plotPlace: $('divGridReferenciados'),
					     onSelect: null});
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
			showMsgbox('Manager', 300, 50, 'Selecione o produto similar que deseja excluir.');
		else {
			var cdReferenciado = gridReferenciados.getSelectedRow().register['CD_PRODUTO_SERVICO'];
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

function loadGrupos(content) {
	if (content==null && $('cdProdutoServico').value != 0) {
		var cdEmpresa = $('cdEmpresa').value;
		getPage("GET", "loadGrupos", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices"+
				"&method=getAllGrupos(const " + $('cdProdutoServico').value + ":int, const " + cdEmpresa + ":int)");
	}
	else {
		var rsmGrupos = null;
		try {rsmGrupos = eval('(' + content + ')')} catch(e) {}
		gridGrupos = GridOne.create('gridGrupos', {columns: columnsGrupo,
					     resultset :rsmGrupos, 
					     plotPlace : $('divGridGrupos'),
					     onSelect : null});
	}
	changeLayoutGrupos((gridGrupos != null ? gridGrupos.size() : 0));
}

function btnFindGrupoOnClick(reg) {
    if(!reg) {
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar grupos de produtos", 
												    width: 350,
												    height: 225,
												    top:50,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.alm.GrupoDAO",
												    method: "find",
												    allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"NM_GRUPO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"NM_GRUPO"}],
													  		      strippedLines: true,
															   	  columnSeparator: false,
															   	  lineSeparator: false},
												    hiddenFields: null,
												    callback: btnFindGrupoOnClick, 
													autoExecuteOnEnter: true
											       });
    }
    else {
        filterWindow.close();
		$('cdGrupo').value = reg[0]['CD_GRUPO'];
		$('cdGrupoView').value = reg[0]['NM_GRUPO'];
    }
}

function btnFindCdGrupoOnClick(codigo) {
    if (parseInt(codigo, 10) > 0) {
		getPage("GET", "btnFindCdGrupoOnClickAux", 
				"../methodcaller?className=com.tivic.manager.alm.GrupoServices"+
				"&method=findCdGrupo(const " + codigo + ":int)");
	}
}

function btnFindCdGrupoOnClickAux(content) {
	try { 
		var nmGrupo = eval("(" + content + ")"); } 
	catch(e) {}
	if (nmGrupo != null) {
		$('cdGrupoView').value = nmGrupo;
	}
	else {
		createMsgbox("jMsg", {width: 200,
							  height: 20,
							  message: "Grupo não cadastrado!",
							  msgboxType: "ERROR"});
		$('cdGrupoView').value = '';
		$('cdGrupo').value = '';
		$('cdGrupo').focus();
	}
}

var produtoGrupoTemp = null;
function btnNewProdutoGrupoOnClick(reg) {
    if(!reg) {
		if ($('cdProdutoServico').value == 0) {
            createMsgbox("jMsg", {width: 250,
                                  height: 50,
                                  message: "Inclua ou localize um produto para adicionar grupos.",
                                  msgboxType: "INFO"})
		} 
		else if ($('cdGrupo').value <= 0) {
            createMsgbox("jMsg", {width: 350,
                                  height: 50,
                                  message: "Este é o primeiro grupo que você está incluindo para este produto. Utilize o botão de pesquisa ao lado do campo.",
                                  msgboxType: "INFO"})
		} 
		else {
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar grupos de produtos", 
												    width: 350,
												    height: 225,
												    top:50,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.alm.GrupoDAO",
												    method: "find",
												    allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"NM_GRUPO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Nome", reference:"NM_GRUPO"}],
													  		      strippedLines: true,
															      columnSeparator: false,
															   	  lineSeparator: false},
												    hiddenFields: null,
												    callback: btnNewProdutoGrupoOnClick, 
													autoExecuteOnEnter: true
										           });
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
			$('cdGrupo').value = ''; 
			$('cdGrupoView').value = ''; 
			gridGrupos.removeSelectedRow();
			createMsgbox("jMsg", {width: 200,
								  height: 20,
								  message: "Grupo excluído com sucesso!",
								  msgboxType: "INFO"});
			changeLayoutGrupos((gridGrupos != null ? gridGrupos.size() : 0));
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
				aliquota['_IMG'] = '../adm/imagens/aliquota16.gif';
				for (var k=0; aliquota['subResultSetMap']!=null && k<aliquota['subResultSetMap'].lines.length; k++) {
					var configuracao = aliquota['subResultSetMap'].lines[k];
					var nmLogradouro = configuracao['CD_CIDADE']!=0 ? configuracao['NM_CIDADE'] : 
									        configuracao['CD_ESTADO']!=0 ? configuracao['NM_ESTADO'] : 
									        	configuracao['CD_PAIS']!=0 ? configuracao['NM_PAIS'] : null;
					var nmNaturezaOperacao = configuracao['CD_NATUREZA_OPERACAO']!=0 ? configuracao['NM_NATUREZA_OPERACAO'] : null;
					configuracao['_IMG'] = '../adm/imagens/configuracao_aliquota16.gif';
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
 ******************************************************** PRECOS *****************************************************************
 *********************************************************************************************************************************/
var columnsPreco = [{label:'Tabela de Preço', reference:'NM_TABELA_PRECO'}, 
					{label:'Preço', reference:'VL_PRECO', type:GridOne._CURRENCY}];
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
			var construtor = 'cdTabelaPreco:int, cdProdutoServico:int, cdProdutoServicoPreco:int, dtTerminoValidade:GregorianCalendar, vlPreco:float';
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
			if (gridPrecos.getSelectedRow()==null) {
				gridPrecos.addLine(0, precoRegister, onClickPreco, true);
			}
			else {
				if (gridPrecos.getSelectedRow()) {
					gridPrecos.updateSelectedRowByFields(precoFields);
				}
			}
			$('cdTabelaPrecoOld').value = $('cdTabelaPreco').value;
			$("dataOldPreco").value = captureValuesOfFields(precoFields);
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
					     onSelect : onClickPreco});
	}
}

function btnDeletePrecoOnClick(content)	{
	if(content==null) {
		if (gridPrecos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o Preço que deseja excluir.');
		else {
			var produtoServicoDescription = ' (Produto ' + $('nmProdutoServico').value.toUpperCase() + ", Cód. " + $('cdProdutoServico').value + ")";
		    var executionDescription = formatDescriptionDelete("Configuração de Preço " + produtoServicoDescription, $("cdTabelaPrecoOld").value, $("dataOldPreco").value);
			var cdTabelaPreco = gridPrecos.getSelectedRow().register['CD_TABELA_PRECO'];
			var cdProdutoServico = $('cdProdutoServico').value;
			showConfirmbox('Manager', 300, 80, 'Você tem certeza que deseja remover o Preço selecionado?', 
							function() {
								getPage('GET', 'btnDeletePrecoOnClick', 
										'../methodcaller?className=com.tivic.manager.adm.ProdutoServicoPrecoServices'+
										'&method=delete(const ' + cdTabelaPreco + ':int, const ' + cdProdutoServico + ':int):int', null, null, null, executionDescription);
							});
		}
	}
	else {
		if (isInteger(content) && parseInt(content, 10) > 0) {
			gridPrecos.removeSelectedRow();
		}
		else
			showMsgbox('Manager', 300, 50, 'Erros reportados ao excluir Preço.');
	}
}

function btnAlterPrecoOnClick() {
	if (gridPrecos.getSelectedRow()==null)
			showMsgbox('Manager', 300, 50, 'Selecione o Preço que você deseja alterar.');
	else {
		createWindow('jPreco', {caption: "Configuração de Preço",
							    width: 399,
								height: 60,
								noDropContent: true,
								modal: true,
								contentDiv: 'precoPanel'});
		$('cdTabelaPreco').className = 'disabledSelect';
		$('cdTabelaPreco').disabled = true;
		loadFormRegister(precoFields, gridPrecos.getSelectedRow().register, true);
		$('vlPreco').focus();
	}
}

function btnNewPrecoOnClick(){
    if ($('cdProdutoServico').value == '0')
			showMsgbox('Manager', 300, 50, 'Inclua ou localize uma produto para lançar preços.');
	else {
		gridPrecos.unselectGrid();
		$("dataOldPreco").value = "";
		clearFields(precoFields);
		$('cdTabelaPreco').className = 'select';
		$('cdTabelaPreco').disabled = false;
		createWindow('jPreco', {caption: "Configuração de Preço",
								width: 399,
								height: 60,
								noDropContent: true,
								modal: true,
								contentDiv: 'precoPanel'});
		$('cdTabelaPreco').focus();
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

/****** COMPONENTES *******/
var rsmComponentes = null;
var levelComponente = null;
function initComponente() {
    clearFields(componenteFields);
	disabledFormComponente = true;
	alterFieldsStatus(false, componenteFields, "qtProdutoServico", "disabledField");
	loadComponentes();
	createGridSubComponentes();
	createGridEstoquesComponentes();
	createGridFornecedoresComponentes();
	createGridSimilaresComponentes();
}

function clearFormComponente(){
    disabledFormComponente = false;
    clearFields(componenteFields);
    alterFieldsStatus(true, componenteFields, "qtProdutoServico");

	$('qtProdutoServico').value = 0;
	$('prPerda').value = 0;
	$('lgCalculoCusto').value = 1;
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
			alterFieldsStatus(false, componenteFields, "qtProdutoServico", "disabledField");
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
		else if (tvComponentes.lines <= 0 && !lookup) {
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
	alterFieldsStatus(false, componenteFields, "qtProdutoServico", "disabledField");
	
	loadFormRegister(componenteFields, register);
	
	$("dataOldComponente").value = captureValuesOfFields(componenteFields);
	
	loadSubComponentes();
	loadEstoquesComponentes();
	loadFornecedoresComponentes();
	loadSimilaresComponentes();
}

function btnDeleteComponenteOnClick(content){
    if(content==null){
        if(!tvComponentes.getSelectedLevel())
            createTempbox("jMsg1", {width: 230,
								   height: 45,
								   message: "Nenhum componente selecionado!",
								   tempboxType: "ALERT",
								   time: 2000});
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
				"&method=getAllHierarquia(const " + $('cdEmpresa').value + ":int, const " + $('cdProdutoServico').value + ":int, const false:boolean)", null, true);
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
		createTreeviewComponente(rsmComponentes);
	}
}

function createTreeviewComponente(rsmComponentes) {
	tvComponentes = TreeOne.create('tvComponentes', {resultset: rsmComponentes,
													 columns: ['NM_PRODUTO_SERVICO'],
													 defaultImage: 'imagens/produto16.gif',
													 plotPlace: $('divTvComposicao'),
													 collapseOnCreate: true,
													 noSelectOnCreate: false,
													 onProcessRegister: function(register){
															register['_IMG'] = (register['TP_PRODUTO_SERVICO'] == <%=com.tivic.manager.grl.ProdutoServicoServices.TP_PRODUTO%>) ? 'imagens/produto16.gif' : 'imagens/servico16.gif';
													},
													onSelect: function(){
																loadFormComponente(this.register);
															  }
													});
	loadSubComponentes();
	loadEstoquesComponentes();
	loadFornecedoresComponentes();
	loadSimilaresComponentes();

	closeWindow('jProcessando');
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

function fillFormProdutoServico(content)	{
    var rsm = null;
	try {rsm = eval('(' + content + ')')} catch(e) {}
	btnFindProdutoServicoOnClick(rsm.lines);
	tabProdutoServico.showTab(0, false);
}

function btnPrintComponenteOnClick() {
}

/****** SUB-COMPONENTES *******/
function loadSubComponentes(content) {
    var registerComponente = tvComponentes.getSelectedLevelRegister();
	if (content==null && registerComponente != null) {
		getPage("GET", "loadSubComponentes", 
				"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoComposicaoServices" +
				"&method=getAllHierarquia(const " + registerComponente["CD_EMPRESA"] + ":int, const " + registerComponente["CD_PRODUTO_SERVICO_COMPONENTE"] + ":int, const true:boolean)", null, true);
	}
	else {
	    var rsmSubComponentes = null;
		try {rsmSubComponentes = eval('(' + content + ')')} catch(e) {}
		for (var i = 0; rsmSubComponentes != null && i < rsmSubComponentes.lines.length; i++) {
			if (parseInt(rsmSubComponentes.lines[i]['CD_REFERENCIA'], 10) > 0) {
				rsmSubComponentes.lines[i]['NM_PRODUTO_SERVICO'] = rsmSubComponentes.lines[i]['NM_REFERENCIA'];
				rsmSubComponentes.lines[i]['NM_PRODUTO_SERVICO_COMPONENTE'] = rsmSubComponentes.lines[i]['NM_REFERENCIA'];
				rsmSubComponentes.lines[i]['ID_PRODUTO_SERVICO_COMPONENTE'] = rsmSubComponentes.lines[i]['ID_REFERENCIA'];
			}
		}
		createGridSubComponentes(rsmSubComponentes);
	}
}

function createGridSubComponentes(rsm){
	gridSubComponentes = GridOne.create('gridSubComponentes', {columns: [{label: 'Produto/Serviço/Referência', reference: 'NM_PRODUTO_SERVICO_COMPONENTE'},
																	     {label: 'ID', reference: 'ID_PRODUTO_SERVICO_COMPONENTE'},
																	     {label: 'Unidade medida', reference: 'NM_UNIDADE_MEDIDA'},
																	     {label: 'Quantidade', reference: 'QT_PRODUTO_SERVICO', type: GridOne._FLOAT},
																	     {label: '% Perda', reference: 'PR_PERDA', type: GridOne._FLOAT},
																	     {labelImg: 'imagens/custo16.gif', labelImgHint: 'Faz parte do cálculo do custo total do produto?', reference: 'IMG_LG_CALCULO_CUSTO', type: GridOne._IMAGE, imgWith: 16}],
															   resultset: rsm,
															   onProcessRegister: function(register){
																  register['IMG_LG_CALCULO_CUSTO'] = (register['LG_CALCULO_CUSTO']==1)?'imagens/custo16.gif':'imagens/not_custo16.gif';
															   },
															   onDoubleClick: function(){
															   		createConfirmbox("dialog", {caption: "Pesquisa produto/serviço",
																								width: 300, 
																								height: 75, 
																								message: "Você tem certeza que deseja carregar este registro?",
																								boxType: "QUESTION",
																								positiveAction: function() {setTimeout("loadProdutoServico()", 10)}});
															   },
															   strippedLines: true,
															   columnSeparator: true,
															   lineSeparator: false,
															   noSelectOnCreate: true,
															   plotPlace: 'divGridSubComponentes'});
}

/****** ESTOQUES COMPONENTES *******/
function loadEstoquesComponentes(content) {
    var registerComponente = tvComponentes.getSelectedLevelRegister();
	if (content==null && registerComponente != null) {
		var cdProdutoServico = registerComponente["CD_PRODUTO_SERVICO_COMPONENTE"];
		var cdEmpresa = registerComponente["CD_EMPRESA"];
		setTimeout(function()	{
			getPage('GET', 'loadEstoquesComponentes', 
					'../methodcaller?className=com.tivic.manager.alm.ProdutoEstoqueServices'+
					'&objects=criterios=java.util.ArrayList();' +
					'&method=findEstoqueProdutoByLocalArmazenamento(const ' + cdEmpresa + ':int, const ' + cdProdutoServico + ':int, *criterios:java.util.ArrayList):int')}, 100);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		for (var i = 0; rsm != null && i < rsm.lines.length; i++) {
			if (rsm.lines[i]['QT_ESTOQUE']==null)
				rsm.lines[i]['QT_ESTOQUE'] = 0;
			if (rsm.lines[i]['QT_ESTOQUE_CONSIGNADO']==null)
				rsm.lines[i]['QT_ESTOQUE_CONSIGNADO'] = 0;
			rsm.lines[i]['QT_ESTOQUE_TOTAL'] = rsm.lines[i]['QT_ESTOQUE'] + rsm.lines[i]['QT_ESTOQUE_CONSIGNADO'];
		}
		createGridEstoquesComponentes(rsm);
	}
}

function createGridEstoquesComponentes(rsm) {
	gridEstoquesComponentes = GridOne.create('gridEstoquesComponentes', {columns: columnsLocalArmazenamento,
																		 resultset: rsm, 
																		 strippedLines: true,
																		 columnSeparator: true,
																		 lineSeparator: false,
																		 noSelectOnCreate: true,
																		 plotPlace : 'divGridEstoquesComponentes'});
}

/****** FORNECEDORES COMPONENTES *******/
function loadFornecedoresComponentes(content) {
    var registerComponente = tvComponentes.getSelectedLevelRegister();
	if (content==null && registerComponente != null) {
		var cdProdutoServico = registerComponente["CD_PRODUTO_SERVICO_COMPONENTE"];
		var cdEmpresa = registerComponente["CD_EMPRESA"];
		setTimeout(function()	{
			getPage("GET", "loadFornecedoresComponentes", 
					"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
					"&method=getAllFornecedores(const " + cdProdutoServico + ":int, " +
					"const " + cdEmpresa + ":int)")}, 100);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridFornecedoresComponentes(rsm);
	}
}

function createGridFornecedoresComponentes(rsm) {
	gridFornecedoresComponentes = GridOne.create('gridFornecedoresComponentes', {columns: columnsFornecedor,
																				 resultset: rsm, 
																				 strippedLines: true,
																				 columnSeparator: true,
																				 lineSeparator: false,
																				 noSelectOnCreate: true,
																				 plotPlace : $('divGridFornecedoresComponentes')});
}

/****** SIMILARES/REFERENCIADOS COMPONENTES *******/
function loadSimilaresComponentes(content) {
    var registerComponente = tvComponentes.getSelectedLevelRegister();
	if (content==null && registerComponente != null) {
		var cdProdutoServico = registerComponente["CD_PRODUTO_SERVICO_COMPONENTE"];
		var cdEmpresa = registerComponente["CD_EMPRESA"];
		setTimeout(function()	{
			getPage("GET", "loadSimilaresComponentes", 
					"../methodcaller?className=com.tivic.manager.grl.ProdutoServicoServices"+
					"&method=getAllSimilares(const " + cdProdutoServico + ":int, const -1: int)")}, 100);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		createGridSimilaresComponentes(rsm);
	}
}

function createGridSimilaresComponentes(rsm) {
	gridSimilaresComponentes = GridOne.create('gridSimilaresComponentes', {columns: [{label: 'Nome produto', reference: 'NM_PRODUTO_SERVICO'},
																					 {label: 'ID', reference: 'ID_PRODUTO_SERVICO'}, 
																					 {label: 'Tipo', reference: 'CALC_LG_REFERENCIADO'}],
																		   resultset: rsm, 
																		   onProcessRegister: function(register){
																			  register['CALC_LG_REFERENCIADO'] = (register['LG_REFERENCIADO'] == 1) ? 'Referenciado' : 'Similar';
																		   },
																		   strippedLines: true,
																		   columnSeparator: true,
																		   lineSeparator: false,
																		   noSelectOnCreate: true,
																		   plotPlace : $('divGridSimilaresComponentes')});
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
</script>

</head>
<body class="body" onload="initProdutoServico();" id="produtoBody">
<iframe id="frameHidden" name="frameHidden" style="display:none"></iframe>
<div style="width: 645px;" id="produtoServico" class="d1-form">
    <div style="width: 645px; height: 407px;" class="d1-body">
      <input idform="" reference="" id="dataOldProdutoServico" name="dataOldProdutoServico" type="hidden"/>
      <input idform="" reference="" id="dataOldLocalArmazenamento" name="dataOldLocalArmazenamento" type="hidden"/>
      <input idform="" reference="" id="dataOldTributo" name="dataOldTributo" type="hidden"/>
      <input idform="" reference="" id="dataOldPreco" name="dataOldPreco" type="hidden"/>
      <input idform="" reference="" id="dataOldFornecedor" name="dataOldFornecedor" type="hidden"/>
      <input idform="" reference="" id="dataOldFotoProduto" name="dataOldFotoProduto" type="hidden"/>
      <input idform="produtoServico" reference="cd_produto_servico" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0"/>
      <input idform="produtoServico" reference="cd_classificacao_fiscal" id="cdClassificacaoFiscal" name="cdClassificacaoFiscal" type="hidden" value="0" defaultValue="0"/>
      <input idform="produtoServico" logmessage="Código Categoria Econômica" reference="cd_categoria" id="cdCategoria" name="cdCategoria" type="hidden"/>
      <input idform="produtoServico" reference="tp_produto_servico" id="tpProdutoServico" name="tpProdutoServico" type="hidden" value="<%=tpProdutoServico==-1 ? ProdutoServicoServices.TP_PRODUTO : tpProdutoServico%>" defaultValue="<%=tpProdutoServico==-1 ? ProdutoServicoServices.TP_PRODUTO : tpProdutoServico%>"/>
      <input idform="produtoServico" id="cdEmpresa" name="cdEmpresa" type="hidden" value="0" defaultValue="0"/>
      <input id="cdFormularioDefault" name="cdFormularioDefault" type="hidden" value="<%=cdFormulario%>" defaultValue="<%=cdFormulario%>"/>
      <input idform="produtoServico" reference="cd_formulario" id="cdFormulario" name="cdFormulario" type="hidden" value="0" defaultValue="0"/>
	  <input idform="produtoServico" reference="cd_marca" id="cdMarca" name="cdMarca" type="hidden" value="0" defaultValue="0">
	  <input idform="produtoServico" reference="nm_modelo" id="nmModelo" name="nmModelo" type="hidden" value="" defaultValue="">
      <input idform="localArmazenamento" reference="cd_local_armazenamento" id="cdLocalArmazenamento" name="cdLocalArmazenamento" type="hidden" value="0" defaultvalue="0"/>
      <input idform="tributo" reference="cd_produto_servico_tributo" id="cdProdutoServicoTributo" name="cdProdutoServicoTributo" type="hidden" value="0" defaultValue="0"/>
      <input idform="preco" reference="cd_tabela_preco" id="cdTabelaPrecoOld" name="cdTabelaPrecoOld" type="hidden" value="0" defaultValue="0"/>
      <input idform="preco" reference="cd_produto_servico_preco" id="cdProdutoServicoPreco" name="cdProdutoServicoPreco" type="hidden" value="0" defaultValue="0"/>
      <input idform="fornecedor" reference="cd_fornecedor" id="cdFornecedorOld" name="cdFornecedorOld" type="hidden" value="0" defaultValue="0"/>
      <input logmessage="Código Composição" idform="componente" reference="cd_composicao" datatype="STRING" id="cdComposicao" name="cdComposicao" type="hidden" value="0" defaultValue="0"/>
      <input idform="" reference="" id="dataOldComponente" name="dataOldComponente" type="hidden"/>
      <div id="toolBar" class="d1-toolBar" style="height:24px; width: 643px;">
      </div>
      <div id="divTabProdutoServico">
        <div id="divAbaDadosBasicos" style="display:none;">
          <div class="d1-line" id="line0">
            <div id="divNmProdutoServico" style="width: 452px;" class="element">
              <label class="caption" for="nmProdutoServico">Nome</label>
              <input lguppercase="true" style="text-transform: uppercase; width: 449px;" logmessage="Nome Produto" class="field" idform="produtoServico" reference="nm_produto_servico" datatype="STRING" maxlength="256" id="nmProdutoServico" name="nmProdutoServico" type="text" />
            </div>
            <div style="width: 90px; " class="element">
              <label class="caption" for="">Tamanho</label>
              <input lguppercase="true" style="text-transform: uppercase; width: 87px;" logmessage="Tamanho" class="field" idform="produtoServico" reference="txt_dado_tecnico" datatype="STRING" id="txtDadoTecnico" name="txtDadoTecnico" type="text"/>
            </div>
            <div style="width: 90px; " class="element">
              <label class="caption" for="">Cor</label>
              <input lguppercase="true" style="text-transform: uppercase; width: 87px;" logmessage="Cor" class="field" idform="produtoServico" reference="txt_especificacao" datatype="STRING" id="txtEspecificacao" name="txtEspecificacao" type="text"/>
            </div>
          </div>
          <div class="d1-line" id="line2">
            <div id="divSgProdutoServico" style="width: 40px; display: none;" class="element">
              <label class="caption" for="sgProdutoServico">Sigla</label>
              <input lguppercase="true" style="text-transform: uppercase; width: 37px;" logmessage="Sigla" class="field" idform="produtoServico" reference="sg_produto_servico" datatype="STRING" maxlength="10" id="sgProdutoServico" name="sgProdutoServico" type="text">
            </div>
            <div id="divCdUnidadeMedida" style="width: 152px;" class="element">
              <label class="caption" for="cdUnidadeMedida">Unidade de Medida</label>
              <select style="width: 149px;" logmessage="Unidade" class="select" idform="produtoServico" reference="cd_unidade_medida" datatype="STRING" id="cdUnidadeMedida" name="cdUnidadeMedida" defaultValue="0">
                <option value="0">Selecione...</option>
              </select>
            </div>
            <div style="width: 70px; display: none;" class="element">
              <label class="caption" for="qtPrecisaoUnidade">Precisão Un.</label>
              <input style="width: 67px;" mask="##" logmessage="Precisão setor" class="field" idform="produtoServico" reference="qt_precisao_unidade" datatype="INT" id="qtPrecisaoUnidade" name="qtPrecisaoUnidade" defaultValue="2" value="2" type="text">
            </div>
            <div id="divIdProdutoServico" style="width: 150px;" class="element">
              <label class="caption" for="idProdutoServico">Código de Barras (GTIN/NGIC)</label>
              <input lguppercase="true" style="text-transform: uppercase; width: 147px;" logmessage="ID" class="field" idform="produtoServico" reference="id_produto_servico" datatype="STRING" maxlength="20" id="idProdutoServico" name="idProdutoServico" type="text">
            </div>
            <div id="divIdReduzido" style="width: 150px;" class="element">
              <label class="caption" for="idReduzido">Referência</label>
              <input style="text-transform: uppercase; width: 147px;" lguppercase="true" logmessage="ID reduzido" class="field" idform="produtoServico" reference="id_reduzido" datatype="STRING" maxlength="50" id="idReduzido" name="idReduzido" type="text">
        	  <button idform="produtoServico" onclick="getNextIdReduzido(null);" id="btnNextIdReduzido" title="Gerar Número de Reduzido" class="controlButton"><img alt="|30|" src="/sol/imagens/reload-button.gif" width="15" height="15"></button>
            </div>
            <div id="divQtEmbalagem" style="width: 100px;" class="element">
              <label class="caption" for="qtEmbalagem">Estoque Inicial</label>
              <input style="width: 97px; text-align:right;" disabled="disabled" static="true" logmessage="Qtd. por embalagem" mask="#,####" defaultvalue="0" class="field" idform="produtoServico" reference="qt_embalagem" datatype="INT" id="qtEmbalagem" name="qtEmbalagem" type="text">
            </div>
            <div id="divNrOrdem" style="width: 53px; display: none;" class="element">
              <label class="caption" for="nrOrdem">N&deg; Ordem</label>
              <input style="text-transform: uppercase; width: 50px;" lguppercase="true" logmessage="Nr Ordem" class="field" idform="produtoServico" reference="nr_ordem" datatype="STRING" maxlength="10" id="nrOrdem" name="nrOrdem" type="text">
            </div>
                <div style="width: 80px;" class="element">
                  <label class="caption" for="qtIdeal">Preço de Venda</label>
                  <input style="width: 77px; text-align:right;" disabled="disabled" static="true" logmessage="Precisão custo" class="field" idform="produtoServico" reference="qt_ideal" datatype="INT" id="qtIdeal" name="qtIdeal" type="text"/>
                </div>
          </div>
          <div class="d1-line" id="line1">
            <div id="divTxtProdutoServico" style="width: 637px;" class="element">
              <label class="caption" for="txtProdutoServico">Descrição Detalhada</label>
              <textarea style="width: 634px; height:65px;" logmessage="Descrição Produto" class="textarea" idform="produtoServico" reference="txt_produto_servico" datatype="STRING" id="txtProdutoServico" name="txtProdutoServico"></textarea>
            </div>
          </div>
          <div class="d1-line" id="line3">
            <div id="divCdClassificacao" style="width: 348px;" class="element">
              <input logmessage="Código Classificação Patrimonial" idform="produtoServico" reference="cd_classificacao" datatype="STRING" id="cdClassificacao" name="cdClassificacao" type="hidden" value="0" defaultvalue="0" />
              <label class="caption" for="cdClassificacao">Classifica&ccedil;&atilde;o patrimonial</label>
              <input logmessage="Nome Classificação Patrimonial" idform="produtoServico" reference="nm_classificacao" style="width: 345px;" static="true" disabled="disabled" class="disabledField" name="nmClassificacaoView" id="nmClassificacaoView" type="text">
              <button onclick="btnFindCdClassificacaoOnClick()" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
              <button onclick="btnClearCdClassificacaoOnClick()" idform="produtoServico" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
            </div>
            <div id="divPrDepreciacao" style="width: 79px;" class="element">
              <label class="caption" for="prDepreciacao">% Deprecia&ccedil;&atilde;o</label>
              <input style="width: 76px;" mask="#,###.00" logmessage="% Depreciação" class="field" idform="produtoServico" reference="pr_depreciacao" datatype="FLOAT" maxlength="10" id="prDepreciacao" name="prDepreciacao" type="text">
            </div>
          </div>
          <div class="d1-line" id="line4">
            <div id="divCdFabricante" style="width: 318px;" class="element">
              <label class="caption" for="cdFabricante">Fabricante</label>
              <input logmessage="Código Fabricante" idform="produtoServico" reference="cd_fabricante" datatype="STRING" id="cdFabricante" name="cdFabricante" type="hidden" value="0" defaultValue="0">
              <input logmessage="Nome Código Fabricante" idform="produtoServico" reference="nm_fabricante" style="width: 315px;" static="true" disabled="disabled" class="disabledField" name="cdFabricanteView" id="cdFabricanteView" type="text">
              <button onclick="btnFindFabricanteOnClick()" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
              <button onclick="btnClearFabricanteOnClick()" idform="produtoServico" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
            </div>
            <div id="divCdClassificacaoFiscal" style="width: 318px;" class="element">
              <label class="caption" for="cdClassificacaoFiscal">Situação Tributária</label>
              <input logmessage="Código Situação Tributária" idform="produtoServico" reference="cd_classificacao_fiscal" datatype="STRING" id="cdClassificacaoFiscal" name="cdClassificacaoFiscal" type="hidden" value="0" defaultValue="0">
              <input logmessage="Situação Tributária" idform="produtoServico" reference="nm_classificacao_fiscal" style="width: 315px;" static="true" disabled="disabled" class="disabledField" name="nmClassificacaoFiscalView" id="nmClassificacaoFiscalView" type="text">
              <button onclick="btnFindCdClassificacaoFiscalOnClick()" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
              <button onclick="btnClearCdClassificacaoFiscalOnClick()" idform="produtoServico" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
            </div>
          </div>
          <div id="divProdutoServicoLinha4" style="">
              <div class="d1-line" id="line5" style="">
                <div style="width: 643px;" class="element">
                  <label class="caption" for="txtEspecificacaoPatrimonial">Especificações</label>
                  <textarea style="width: 640px; height:30px" logmessage="Especificações" class="textarea" idform="produtoServico" reference="txt_especificacao" datatype="STRING" id="txtEspecificacaoPatrimonial" name="txtEspecificacaoPatrimonial"></textarea>
                </div>
              </div>
              <div class="d1-line" id="line6">
                <div style="width: 643px;" class="element">
                  <label class="caption" for="txtDadoTecnico">Dados técnicos</label>
                  <textarea style="width: 640px; height:30px" logmessage="Dados Técnicos" class="textarea" idform="produtoServico" reference="txt_dado_tecnico" datatype="STRING" id="txtDadoTecnicoPatrimonial" name="txtDadoTecnicoPatrimonial"></textarea>
                </div>
              </div>
              <div class="d1-line" id="line7">
                <div style="width: 643px;" class="element">
                  <label class="caption" for="txtPrazoEntrega">Prazo de entrega</label>
                  <textarea style="width: 640px; height:30px" logmessage="Prazo de Entrega" class="textarea" idform="produtoServico" reference="txt_prazo_entrega" datatype="STRING" id="txtPrazoEntregaPatrimonial" name="txtPrazoEntregaPatrimonial"></textarea>
                </div>
              </div>
          </div>
          <div id="divProdutoServicoLinha1" style="">
              <div class="d1-line" id="line8">
                <div style="width: 70px;" class="element">
                  <label class="caption" for="qtDiasGarantia" title="Garantia em dias fornecida pelo Fabricante">Dias garantia</label>
                  <input style="width: 67px;" logmessage="Dias garantia" class="field" idform="produtoServico" reference="qt_dias_garantia" datatype="INT" id="qtDiasGarantia" name="qtDiasGarantia" type="text">
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
                  <input style="width: 97px; text-align:right;" static="static" disabled="disabled" mask="#,###.00" logmessage="Preço médio" class="disabledField" idform="produtoServico" reference="vl_preco_medio" datatype="FLOAT" maxlength="10" id="vlPrecoMedio" name="vlPrecoMedio" type="text">
                </div>
                <div style="width: 93px;" class="element">
                  <label class="caption" for="vlUltimoCusto">Último custo</label>
                  <input style="width: 90px; text-align:right;" static="static" disabled="disabled" mask="#,###.00" logmessage="Último custo" class="disabledField" idform="produtoServico" reference="vl_ultimo_custo" datatype="FLOAT" maxlength="10" id="vlUltimoCusto" name="vlUltimoCusto" type="text">
                </div>
                <div style="width: 93px;" class="element">
                  <label class="caption" for="vlCustoMedio">Custo médio</label>
                  <input style="width: 90px; text-align:right;" static="static" disabled="disabled" mask="#,###.00" logmessage="Custo médio" class="disabledField" idform="produtoServico" reference="vl_custo_medio" datatype="FLOAT" maxlength="10" id="vlCustoMedio" name="vlCustoMedio" type="text">
                </div>
              </div>
          </div>
          <div id="divProdutoServicoLinha2" style="">
              <div class="d1-line" id="line9">
                <div style="width: 80px;" class="element">
                  <label class="caption" for="qtIdeal2">Estoque ideal</label>
                  <input style="width: 77px; text-align:right;" mask="#,###.00" logmessage="Est. ideal" class="field" idform="produtoServico" reference="qt_ideal" datatype="FLOAT" maxlength="10" id="qtIdeal2" name="qtIdeal2" type="text">
                </div>
                <div style="width: 80px;" class="element">
                  <label class="caption" for="qtMinima">Estoque mínimo</label>
                  <input style="width: 77px; text-align:right;" mask="#,###.00" logmessage="Est. mínimo" class="field" idform="produtoServico" reference="qt_minima" datatype="FLOAT" id="qtMinima" name="qtMinima" type="text">
                </div>
                <div style="width: 80px;" class="element">
                  <label class="caption" for="qtMaxima">Estoque máximo</label>
                  <input style="width: 77px; text-align:right;" mask="#,###.00" logmessage="Est. máximo" class="field" idform="produtoServico" reference="qt_maxima" datatype="FLOAT" id="qtMaxima" name="qtMaxima" type="text">
                </div>
                <div style="width: 80px;" class="element">
                  <label class="caption" for="qtDiasEstoque" title="Período minímo em dias para o qual há garantia de disponibilidade do produto em estoque">Dias estoque</label>
                  <input style="width: 77px; text-align:right;" logmessage="Dias estoque" class="field" idform="produtoServico" reference="qt_dias_estoque" datatype="INT" id="qtDiasEstoque" name="qtDiasEstoque" type="text">
                </div>
                <div style="width: 80px;" class="element">
                  <label class="caption" for="qtPrecisaoCusto">Precisão custo</label>
                  <input style="width: 77px; text-align:right;" mask="##" logmessage="Precisão custo" class="field" idform="produtoServico" reference="qt_precisao_custo" datatype="INT" id="qtPrecisaoCusto2" name="qtPrecisaoCusto2" type="text">
                </div>
	            <div id="divDtDesativacao" style="width: 69px; display: none;" class="element">
	              <label class="caption" for="dtDesativacao">Desativação</label>
	              <input style="width: 66px;" logmessage="Data Desativação" readonly="readonly" static="static" disabled="disabled" mask="dd/mm/yyyy" maxlength="10" class="disabledField" idform="produtoServico" reference="dt_desativacao" datatype="DATE" id="dtDesativacao" name="dtDesativacao" type="text">
	            </div>
	            <div id="divStProdutoEmpresa" style="width: 20px;" class="element">
	              <label class="caption" for="stProdutoEmpresa">&nbsp;</label>
	              <input name="stProdutoEmpresa" type="checkbox" id="stProdutoEmpresa" onchange="onChangeStProdutoEmpresa(null, this.checked)" value="1" checked="checked" logmessage="Ativo?"  idform="produtoServico" reference="st_produto_empresa">
	            </div>
	            <div id="divStProdutoEmpresa2" style="width: 25px;" class="element">
	              <label class="caption">&nbsp;</label>
	              <label style="margin:2px 0px 0px 0px" class="caption">Ativo</label>
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
              <div id="divGridGrupos" style="width: 610px; background-color:#FFF; height:103px; border:1px solid #000000">&nbsp;</div>
            </div>
            <div style="width: 20px;" class="element">
              <label class="caption">&nbsp;</label>
                <button title="Adicionar grupo para o produto" onclick="btnNewProdutoGrupoOnClick();" style="margin-bottom:2px" id="btnNewProdutoGrupo" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
                <button title="Remover grupo para o produto" onclick="btnDeleteProdutoGrupoOnClick();" id="btnDeleteProdutoGrupo" class="toolButton"><img src="/sol/imagens/btDelete16.gif" height="16" width="16"/></button>
            </div>
          </div>
          <div class="d1-line" id="divGruposSuperior" style="display:none;">
            <div style="position:relative; border:1px solid #999; float:left; padding:2px; margin-top:7px; margin-right:0px">
              <div style="position:absolute; top:-8px; background-color:#F4F4F4; left:2px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; width:30px;">Grupo</div>
              <div style="width: 606px; height:29px; padding:0 0 2px 0" class="element" id="divBodyEndereco">
                <div class="d1-line" id="line1">
                  <div style="width: 40px;" class="element">
                    <label class="caption" for="cdGrupo">Grupo</label>
                    <input logmessage="Código grupo" idform="produtoServico" reference="cd_grupo" mask="####" class="field" style="width: 37px;" datatype="INT" id="cdGrupo" name="cdGrupo" type="text" onblur="btnFindCdGrupoOnClick(this.value);"/>
                  </div>
                  <div id="divCdGrupoView" style="width: 566px;" class="element">
                    <label class="caption" for="cdGrupoView">&nbsp;</label>
                    <input logmessage="Nome grupo" idform="produtoServico" reference="nm_grupo" style="width: 563px;" static="true" disabled="disabled" class="disabledField" name="cdGrupoView" id="cdGrupoView" type="text"/>
                    <button id="btnFindGrupo" name="btnFindGrupo" onclick="btnFindGrupoOnClick()" idform="produtoServico" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                    <button onclick="btnDeleteProdutoGrupoOnClick();" idform="grupo" title="Excluir este grupo para o produto atual..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
                  </div>
                </div>
              </div>
            </div>
            <div style="float:left; width:20px; margin-top: 7px;">
                <button title="Adicionar grupo para o produto" onclick="btnNewProdutoGrupoOnClick();" style="margin-bottom:2px" id="btnNewProdutoGrupo" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
            </div>
          </div>
          <div id="divGridSimilaresSuperior1"> </div>
        </div>
        <div id="divProdutoServicoLinha3">
            <div id="divAbaEspecificacoes" style="display:none;">
              <div style="float:left">
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
                  <div style="width: 292px;" class="element">
                    <label class="caption">Similares</label>
                    <div id="divGridSimilares" style="width: 289px; background-color:#FFF; height:119px; border:1px solid #000000">&nbsp;</div>
                  </div>
                  <div style="width: 20px; margin-right:9px" class="element">
                    <label class="caption">&nbsp;</label>
                      <button title="Novo produto similar" onclick="btnNewProdutoSimilarOnClick();" style="margin-bottom:2px" id="btnNewProdutoSimilar" class="toolButton"><img src="/sol/imagens/btAdd16.gif" height="16" width="16"/></button>
                      <button title="Excluir produto similar" onclick="btnDeleteProdutoSimilarOnClick();" id="btnDeleteProdutoSimilar" class="toolButton"><img src="/sol/imagens/btDelete16.gif" height="16" width="16"/></button>
                  </div>
                </div>
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
            <div id="divAbaConfigFinanceira" style="display:none;">
              <div class="d1-line">
                <div style="width: 613px;" class="element">
                  <label class="caption">Pre&ccedil;os</label>
                  <div id="divGridPrecos" style="width: 610px; background-color:#FFF; height:95px; border:1px solid #000000">&nbsp;</div>
                </div>
                <div style="width: 20px;" class="element">
                  <label class="caption">&nbsp;</label>
                    <button title="Novo Preço" onclick="btnNewPrecoOnClick();" style="margin-bottom:2px" id="btnNewPreco" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
                    <button title="Alterar Preço" onclick="btnAlterPrecoOnClick();" style="margin-bottom:2px" id="btnAlterPreco" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
                    <button title="Excluir Preço" onclick="btnDeletePrecoOnClick();" id="btnDeletePreco" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
                </div>
              </div>
              <div class="d1-line">
                <div style="width: 613px;" class="element">
                  <label class="caption">Fornecedores</label>
                  <div id="divGridFornecedores" style="width: 610px; background-color:#FFF; height:113px; border:1px solid #000000">&nbsp;</div>
                </div>
                <div style="width: 20px;" class="element">
                  <label class="caption">&nbsp;</label>
                    <button title="Novo Preço" onclick="btnNewFornecedorOnClick();" style="margin-bottom:2px" id="btnNewFornecedor" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button>
                    <button title="Alterar Preço" onclick="btnAlterFornecedorOnClick();" style="margin-bottom:2px" id="btnAlterFornecedor" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button>
                    <button title="Excluir Preço" onclick="btnDeleteFornecedorOnClick();" id="btnDeleteFornecedor" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button>
                </div>
              </div>
              <div class="d1-line">
                <div style="width: 613px;" class="element">
                  <label class="caption">Tributos</label>
                  <div id="divTreeTributos" style="width: 635px; background-color:#FFF; height:99px; border:1px solid #000">&nbsp;</div>
                </div>
              </div>
            </div>
            <div id="divAbaComposicao" style="display:none;">
                <!-- GRID COMPOSIÇÃO DE PRODUTOS/SERVIÇOS -->
                <div id="divTvComposicaoSuperior" style="width: 195px;" class="element">
                  <label class="caption">Componentes:</label>
                  <div id="divTvComposicao" style="width: 190px; height: 337px; background-color:#FFF; border:1px solid #000000"></div>
                </div>
                <div class="d1-toolBar" id="toolBarComponentes" style="width:438px; height:24px; float:left"></div>
                <div id="divCamposAbas" style="width: 440px; float:left">
                    <div style="height:117px">
                       <div class="d1-line" id="line1">
                         <div style="width: 360px;" class="element">
                           <label class="caption" for="cdProdutoServicoComponente">Produto / Serviço / Referência</label>
                           <input logmessage="Código Componente" idform="componente" reference="cd_produto_servico_componente" datatype="STRING" id="cdProdutoServicoComponente" name="cdProdutoServicoComponente" type="hidden" value="0" defaultValue="0">
                           <input logmessage="Nome Componente" idform="componente" reference="nm_produto_servico_componente" style="width: 357px;" static="true" disabled="disabled" class="disabledField" name="cdProdutoServicoComponenteView" id="cdProdutoServicoComponenteView" type="text">
                           <input logmessage="Código Referência" idform="componente" reference="cd_referencia" datatype="STRING" id="cdReferencia" name="cdReferencia" type="hidden" value="0" defaultValue="0">
                           <button idform="componente" onclick="btnFindComponenteOnClick(null, true)" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
                           <button idform="componente" onclick="$('cdProdutoServicoComponente').value=0; $('cdProdutoServicoComponenteView').value='';" title="Limpar este campo..." class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
                         </div>
                         <div style="width: 80px;" class="element">
                           <label class="caption" for="idComponente">ID</label>
                           <input static="static" disabled="disabled" style="width: 77px;" logmessage="ID Componente" class="disabledField" idform="componente" reference="id_produto_servico_componente" datatype="STRING" maxlength="10" id="idComponente" name="idComponente" type="text" />
                         </div>
                       </div>
                       <div class="d1-line" id="line2">
                          <div style="width: 440px;" class="element">
                            <label class="caption" for="txtProdutoServicoComponente">Descri&ccedil;&atilde;o</label>
                            <textarea disabled="disabled" static="static" logmessage="Descrição Componente" style="width: 437px; height:36px" class="disabledField" idform="componente" reference="txt_produto_servico_componente" datatype="STRING" id="txtProdutoServicoComponente" name="txtProdutoServicoComponente"></textarea>
                          </div>
                       </div>
                       <div class="d1-line" id="line3">
                          <div style="width: 245px;" class="element">
                            <label class="caption" for="cdUnidadeMedida">Unidade</label>
                            <select style="width: 242px;" logmessage="Unidade" class="select" idform="componente" reference="cd_unidade_medida_componente" datatype="STRING" id="cdUnidadeMedidaComponente" name="cdUnidadeMedidaComponente" defaultValue="0">
                              <option value="0">Selecione...</option>
                            </select>
                          </div>
                          <div style="width: 60px;" class="element">
                            <label class="caption" for="qtProdutoServico">Quantidade</label>
                            <input style="width: 57px; text-align:right;" mask="#,###.00" logmessage="Quantidade" class="field" idform="componente" reference="qt_produto_servico" datatype="FLOAT" maxlength="10" id="qtProdutoServico" name="qtProdutoServico" type="text" value="0" defaultValue="0">
                          </div>
                          <div style="width: 47px;" class="element">
                            <label class="caption" for="prPerda">% Perda</label>
                            <input style="width: 44px; text-align:right;" mask="#,###.00" logmessage="% Perda" class="field" idform="componente" reference="pr_perda" datatype="FLOAT" maxlength="10" id="prPerda" name="prPerda" type="text" value="0" defaultValue="0">
                          </div>
                          <div style="width: 20px;" class="element">
                            <label class="caption" for="lgCalculoCusto">&nbsp;</label>
                            <input logmessage="Entra no cálculo do custo?" idform="componente" reference="lg_calculo_custo" id="lgCalculoCusto" name="lgCalculoCusto" type="checkbox" checked="checked" value="1"/>
                          </div>
                          <div style="width: 65px;" class="element">
                            <label class="caption">&nbsp;</label>                  
                            <label style="margin:2px 0px 0px 0px" class="caption">Cálculo custo</label>
                          </div>
                       </div>
                    </div>
                    <div id="divTabComposicao"></div>
                    <div id="divAbaSubComponentes">
                       <div id="divGridSubComponentes" style="width: 432px; height:176px; background-color:#FFFFFF; float:left; border:1px solid #000000">&nbsp;</div>
                    </div>
                    <div id="divAbaEstoquesComponentes">
                       <div id="divGridEstoquesComponentes" style="width: 432px; height:176px; background-color:#FFFFFF; float:left; border:1px solid #000000">&nbsp;</div>
                    </div>
                    <div id="divAbaSimilaresComponentes">
                       <div id="divGridSimilaresComponentes" style="width: 432px; height:176px; background-color:#FFFFFF; float:left; border:1px solid #000000">&nbsp;</div>
                    </div>
                    <div id="divAbaFornecedoresComponentes">
                       <div id="divGridFornecedoresComponentes" style="width: 432px; height:176px; background-color:#FFF; float:left; border:1px solid #000000">&nbsp;</div>
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
          <input style="text-transform: uppercase; width: 113px;" lguppercase="true" datatype="FLOAT" logmessage="Preço" class="field" idform="preco" reference="vl_preco" maxlength="10" id="vlPreco" name="vlPreco" type="text" mask="#,###.00"/>
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
