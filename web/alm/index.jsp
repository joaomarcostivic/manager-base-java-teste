<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.fsc.TipoCreditoServices"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri='../tlds/dotVisualInterface.tld' prefix='cpnt'%>
<%@ taglib uri='../tlds/dotSecurityManager.tld' prefix='sec'%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="java.util.*"%>
<%@page import="sol.util.*"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<%@page import="com.tivic.manager.grl.VinculoServices"%>
<%@page import="com.tivic.manager.alm.GrupoServices"%>
<%@page import="com.tivic.manager.seg.UsuarioServices"%>
<%@page import="com.tivic.manager.seg.*"%>
<%@page import="com.tivic.manager.grl.*"%>

<head>
<title>Manager :: Adm. de Materiais(Estoque) e Vendas</title>
<%
	try {
		int cdVinculoFornecedor = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FORNECEDOR", 0);
		int cdVinculoCliente 	= ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0);
		ResultSetMap rsmGrupos  = GrupoServices.getAllGruposCadastroIsolado();
		Empresa empresa         = EmpresaServices.getDefaultEmpresa();
		int cdEmpresa           = sol.util.RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0); 
		int cdGrupoCombustivel  = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
		String nmUsuario        = sol.util.RequestUtilities.getAsString(request, "nmUsuario", "");
		Usuario usuario         = (Usuario)session.getAttribute("usuario");
		int cdUsuario           = usuario!=null ? usuario.getCdUsuario() : 0;
		String nmOperador = "";
		if(usuario!=null)	{
			usuario = ((Usuario)session.getAttribute("usuario"));
			if(usuario.getCdPessoa()>0)	{
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa()); 
				nmOperador = pessoa!=null ? pessoa.getNmPessoa() : usuario.getNmLogin();
			}
		}
		Result result = CertificadoServices.validarCertificadoDigital(empresa != null ? empresa.getCdEmpresa() : cdEmpresa);
%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="toolbar, form, grid2.0, shortcut, report, flatbutton, filter, permission, corners" compress="false" />
<script language="javascript" src="../js/alm.js"></script>
<script language="javascript" src="../js/adm.js"></script>
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript" src="../js/seg.js"></script>
<script language="javascript" src="../js/srh.js"></script>
<script language="javascript" src="../js/fta.js"></script>
<script language="javascript" src="../js/fsc.js"></script>
<script language="javascript">
var janelaLogin = null;
var toolBar = null;
var cdGrupoCombustivel = <%=cdGrupoCombustivel%>;
function init() {
	toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'horizontal',
										 buttons: [{id: 'btProduto', img: '../grl/imagens/produto48.gif', label: 'Produtos', imagePosition: 'top', width: 80, onClick: function(){miProdutoOnClick(0,'Produtos',{cdLocalArmazenamento:$('cdLocalArmazenamento').value})}},
												   {separator: 'horizontal'},
										           {id: 'btCliente', img: '../adm/imagens/cliente48.gif', label: 'Clientes', imagePosition: 'top', width: 80, onClick: miPessoaOnClick, paramsOnClick: ['Clientes', <%=cdVinculoCliente%>]},
										           {id: 'btFornecedor', img: '../alm/imagens/fornecedor48.gif', label: 'Fornecedores', imagePosition: 'top', width: 80, onClick: miPessoaOnClick, paramsOnClick: ['Fornecedores', <%=cdVinculoFornecedor%>]},
												   {separator: 'horizontal'},
												   {id: 'btDocumentoEntrada', img: 'imagens/documento_entrada48.gif', label: 'Entradas', imagePosition: 'top', width: 80, onClick: miDocumentoEntradaOnClick},
												   {id: 'btDocumentoSaida', img: 'imagens/documento_saida48.gif', label: 'Saídas', imagePosition: 'top', width: 80, onClick: miDocumentoSaidaOnClick},
												   {separator: 'horizontal'},
												   {id: 'btDevolucao', img: 'imagens/devolucao48.gif', label: 'Devolução/Cliente', imagePosition: 'top', width: 100, onClick: function() {miDevolucaoClienteOnClick({})}},
												   {id: 'btDevolucaoForn', img: 'imagens/devolucao48.gif', label: 'Devolução/Fornecedor', imagePosition: 'top', width: 100, onClick: function() {miDevolucaoFornecedorOnClick({})}}]
										 });
	roundCorner($('userPanel'), 5, 5, 5, 5);
}

/**
 * Valida o certificado e exibe uma mensagem caso esteja com algum problema com a data
 */
function validaCertificado(){
	<%	
	if (result.getCode() == -2) {
		%>
		var message = '<%=result.getMessage()%>';
		 createConfirmbox("jMsg", {caption: "Aviso sobre certificado.", width: 300, height: 75, 
             message: message, boxType: "INFO",
             positiveAction: function() {setTimeout("", 10)}});
	<%
	}
	if (result.getCode() == -3) {
	%>
	var message = '<%=result.getMessage()%>';
	createConfirmbox("jMsg", {caption: "Aviso sobre certificado.", width: 300, height: 75, 
        message: message, boxType: "ERROR",
        positiveAction: function() {setTimeout("", 10)}});
	<%
	}
	%>
}

function miAlterarSenhaOnClick()	{
	createWindow('jAlterarSenha', {caption: 'Alterando senha', width: 290, height: 115, contentUrl: '../seg/alterar_senha.jsp', modal:true});
}

function loadEmpresas(content){
	if (content==null) {
		getPage("GET", "loadEmpresas", "../methodcaller?className=com.tivic.manager.grl.EmpresaDAO&method=getAll()");
	}
	else {
		var empresas = null;
		try {empresas = eval("(" + content + ")")} catch(e) {}
		for (var i=0; empresas!=null && i<empresas.lines.length; i++) {
			var option = document.createElement('OPTION');
			option.setAttribute('value', empresas.lines[i]['CD_EMPRESA']);
			option.appendChild(document.createTextNode(empresas.lines[i]['NM_EMPRESA']));
			$('cdEmpresa').appendChild(option);
		}
	}
}

function login(msg) {
	closeAllWindow();
	janelaLogin = createWindow('jLogin', {caption: 'Login', noMinimizeButton: true, noMaximizeButton: true, noCloseButton: true,
										  width: 350, height: 190, modal:true,
										  contentUrl: '../login.jsp?lgEscolherEmpresa=1&parentUser=1&lgEstoque=1&idModulo=alm'+(msg!=null? '&msg='+msg : '')});
}

function miRelatorioPessoaOnClick()	{
	createWindow('jRelatorioPessoa', {caption: 'Relatório de Pessoas', width: 700, height: 430, contentUrl: '../grl/relatorio_pessoa.jsp?cdEmpresa='+getValue('cdEmpresa')});
}

function miFormularioAtributoOpcaoOnClick(cdFormularioAtributo, nmAtributo) {
	createWindow('jFormularioAtributoOpcao_' + cdFormularioAtributo, {caption: 'Manutenção de ' + (nmAtributo==null ? 'Opções :: Atributo' : nmAtributo), width: 384, height: 264, contentUrl: 'formulario_atributo_opcao.jsp?cdEmpresa=' + $('cdEmpresa').value +
																											 '&cdFormularioAtributo=' + cdFormularioAtributo});
}

function miAcertoConsignacaoEntradaOnClick() {
	createWindow('jAcertoConsignacaoEntrada', {caption: 'Acerto de Consignação :: Fornecedores', width: 600, height: 350, contentUrl: 'acerto_consignacao_entrada.jsp?cdEmpresa=' + $('cdEmpresa').value + '&cdLocalArmazenamento=' + $('cdLocalArmazenamento').value});
}

function miAcertoConsignacaoSaidaOnClick() {
	createWindow('jAcertoConsignacaoSaida', {caption: 'Acerto de Consignação :: Clientes', width: 600, height: 350, contentUrl: 'acerto_consignacao_saida.jsp?cdEmpresa=' + $('cdEmpresa').value + '&cdLocalArmazenamento=' + $('cdLocalArmazenamento').value});
}

function miModeloDocumentoOnClick() {
	createWindow('jModeloDocumento', {caption: 'Manutenção de Modelos de Documentos', width: 800, height: 500, contentUrl: '../doc/editor.jsp'});
}

function miSaldoAcertosEntradaOnClick() {
	createWindow('jReportSaldoAcertosEntrada', {caption: 'Saldo de Acertos de Consignação a Realizar :: Fornecedores', width: 590, height: 350, contentUrl: 'relatorio_saldo_acerto_consignacao_entrada.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miSaldoAcertosSaidaOnClick() {
	createWindow('jReportSaldoAcertosSaida', {caption: 'Saldo de Acertos de Consignação a Realizar :: Clientes', width: 590, height: 350, contentUrl: 'relatorio_saldo_acerto_consignacao_saida.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miAcertosConsignacaoEntradaOnClick() {
	createWindow('jRelatorioAcertoConsignacaoEntrada', {caption: 'Acertos de Consignação - Fornecedores', width: 600, height: 350, contentUrl: 'acerto_consignacao_entrada.jsp?lgRelatorio=1&cdEmpresa=' + $('cdEmpresa').value});
}

function miAcertosConsignacaoSaidaOnClick() {
	createWindow('jRelatorioAcertoConsignacaoSaida', {caption: 'Acertos de Consignação - Clientes', width: 600, height: 350, contentUrl: 'acerto_consignacao_saida.jsp?lgRelatorio=1&cdEmpresa=' + $('cdEmpresa').value});
}

function miHistoricoPrecoProdutosOnClick() {
	createWindow('jRelatorioHistoricoPrecoProdutos', {caption: 'Evolução de Preços', width: 590, height: 350, contentUrl: '../adm/relatorio_historico_produto_preco.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miRecalculoEstoqueOnClick() {
	createWindow('jRecalculoEstoque', {caption: 'Recálculo de Estoque', width: 590, height: 350, contentUrl: 'recalculo_estoque.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miRecalculoCustoOnClick() {
	createWindow('jRecalculoPrecoCusto', {caption: 'Recálculo de Preço de Custo', width: 590, height: 350, contentUrl: 'recalculo_custo.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miRelatorioComissaoOnClick()	{
	createWindow('jRelatorioComissao', {caption: 'Relatório de Vendas por Vendedor', width: 500, height: 255, modal: true, contentUrl: 'relatorio_comissao.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miRelatorioNcmOnClick()	{
	createWindow('jRelatorioNcm', {caption: 'Relatório de Ncm', width: 500, height: 270, modal: true, contentUrl: 'relatorio_ncm.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function miConfGrupoOnClick() {
	if ($('tpUsuario')!=null && $('tpUsuario').value == <%=UsuarioServices.ADMINISTRADOR%>)
		showWindow('jConfGrupos', 'Configuração de Permissões - Grupos', 701, 365, '../seg/configuracao_grupo.jsp', true); 
	else
		showMsgbox('Manager', 250, 100, 'Sua conta de Usuário não tem acesso a este recurso.');	
}

function miConfUsuarioOnClick() {
	if ($('tpUsuario')!=null && $('tpUsuario').value == <%=UsuarioServices.ADMINISTRADOR%>)
		showWindow('jConfUsuarios', 'Configuração de Permissões - Usuários', 810, 500, '../seg/configuracao_usuario.jsp', true); 
	else
		showMsgbox('Manager', 250, 100, 'Sua conta de Usuário não tem acesso a este recurso.');	
}

function miTabelaEventoFinanceiroOnClick() {
	var optionsTipo = [{value: 0, text: 'Despesa'},
					   {value: 1, text: 'Receita'}];
	FormFactory.createQuickForm('jTabelaEvento', {caption: 'Manutenção de Tabelas de Eventos', width: 500, height: 350,
												  //quickForm
												  id: "adm_evento_financeiro",
												  classDAO: 'com.tivic.manager.adm.EventoFinanceiroDAO',
												  keysFields: ['cd_evento_financeiro'], unitSize: '%',
												  constructorFields: [{reference: 'cd_evento_financeiro', type: 'int'},
													  				  {reference: 'nm_evento_financeiro', type: 'java.lang.String'},
													  				  {reference: 'cd_categoria_economica', type: 'int'},
													  				  {reference: 'vl_evento_financeiro', type: 'float'},
													  				  {reference: 'id_evento_financeiro', type: 'java.lang.String'},
													  				  {reference: 'tp_natureza_dirf', type: 'int'},
													  				  {reference: 'tp_lancamento', type: 'int'},
													  				  {reference: 'cd_categoria_economica', type: 'int'}],
												  gridOptions: {columns: [{reference: 'id_evento_financeiro', label: 'Código'},
								  			   							  {reference: 'nm_evento_financeiro', label: 'Descrição'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'id_evento_financeiro', label: 'Código', width:10, maxLength:2},
											   			   {reference: 'nm_evento_financeiro', label: 'Descrição', width:70, maxLength:50},
												   		   {reference: 'tp_natureza_dirf', label: 'Tipo de Evento', width:20, type: 'select',
															options: optionsTipo}]]});
}

function miTabelaNcmsOnClick() {
	var optionsTipo = [{value: 0, text: 'Selecione...'}];
	FormFactory.createQuickForm('jTabelaNcm', {caption: 'Manutenção de Tabelas de NCM', width: 500, height: 350,
												  //quickForm
												  id: "grl_ncm",
												  classDAO: 'com.tivic.manager.grl.NcmDAO',
												  classMethodGetAll:  'com.tivic.manager.grl.NcmServices',
												  keysFields: ['cd_ncm'], unitSize: '%',
												  constructorFields: [{reference: 'cd_ncm', type: 'int'},
													  				  {reference: 'nm_ncm', type: 'java.lang.String'},
													  				  {reference: 'cd_unidade_medida', type: 'int'},
													  				  {reference: 'nr_ncm', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'nr_ncm', label: 'Numero'},
												                          {reference: 'nm_ncm', label: 'Nome'},
												                          {reference: 'nm_unidade_medida', label: 'Uni. Medida'}],
												                onProcessRegister: function(reg)	{
																				
																},          
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'nr_ncm', label: 'Numero', width:20, maxLength:50},
												           {reference: 'nm_ncm', label: 'Nome', width:60, maxLength:50},
												           {reference:'cd_unidade_medida', label:'Unidade Medida', type:'select', options:optionsTipo, width:20, 
													        classMethodLoad:'com.tivic.manager.grl.UnidadeMedidaDAO', methodLoad:'getAll()', fieldValue:'cd_unidade_medida', fieldText:'nm_unidade_medida'}]]});
}


/***********************************************************************************************************************************************************************************************/

function miModeloDocumentoOnClick() {
	createWindow('jModeloDocumento', {caption: 'Manutenção de Modelos de Documentos', width: 800, height: 500, contentUrl: '../doc/editor.jsp'});
}

function miPedidoVendaOnClick() {
	createWindow('jPedidoVenda', {caption: 'Pedidos de Venda e Orçamentos', width: 700, height: 440, contentUrl: 'pedido_venda.jsp?cdEmpresa='+$('cdEmpresa').value});
}

function miTransfereProdutoPrecoOnClick(){
	createWindow('jTransfereProdutoPreco', {caption: 'Transfere dados entre empresas parceiras', width: 505, height: 195, contentUrl: '../grl/produto_transferencia.jsp?cdEmpresa='+$('cdEmpresa').value});
}
/*************************************************************************************
 * CADASTROS DE VEÍCULOS
 */
var jModeloVeiculo;
function miModeloVeiculoOnClick(){
	jModeloVeiculo = FormFactory.createQuickForm('jModeloVeiculo', {caption: 'Modelos de veículos', width: 680, height: 400, noDrag: true,
										  //quickForm
										  id: "fta_modelo_veiculo", classDAO: 'com.tivic.manager.fta.ModeloVeiculoDAO',
										  keysFields: ['cd_modelo'],
										  classMethodGetAll: 'com.tivic.manager.fta.ModeloVeiculoServices',
										  methodGetAll: 'getAllModeloMarca()',
										  hiddenFields: [{reference: 'cd_categoria_economica'}, {reference: 'nm_produto_servico'},
														 {reference: 'txt_produto_servico'}, {reference: 'txt_especificacao'},
														 {reference: 'txt_dado_tecnico'}, {reference: 'txt_prazo_entrega'},
														 {reference: 'tp_produto_servico'}, {reference: 'id_produto_servico'},
														 {reference: 'sg_produto_servico'}, {reference: 'cd_classificacao_fiscal'},
														 {reference: 'cd_fabricante'}, {reference: 'cd_classificacao'},
														 {reference: 'pr_depreciacao'}],
										  unitSize: '%',
										  //Costrutor da classe ModeloVeiculo
										  constructorFields: [/* produto servico */
										  					  {reference: 'cd_produto_servico', type: 'int'},
															  {reference: 'cd_categoria_economica', type: 'int'},
															  {reference: 'nm_produto_servico', type: 'java.lang.String'},
															  {reference: 'txt_produto_servico', type: 'java.lang.String'},
															  {reference: 'txt_especificacao', type: 'java.lang.String'},
															  {reference: 'txt_dado_tecnico', type: 'java.lang.String'},
															  {reference: 'txt_prazo_entrega', type: 'java.lang.String'},
															  {reference: 'tp_produto_servico', type: 'int'},
															  {reference: 'id_produto_servico', type: 'java.lang.String'},
															  {reference: 'sg_produto_servico', type: 'java.lang.String'},
															  {reference: 'cd_classificacao_fiscal', type: 'int'},
															  {reference: 'cd_fabricante', type: 'int'},
															  {reference: 'cd_marca_produto_servico', type:'int'},
															  {reference: 'nm_modelo_produto_servico', type:'java.lang.String'},
															  {reference: 'cd_ncm', type:'int'},
															  {reference: 'nr_referencia', type:'java.lang.String'},
															  /* bem */
															  {reference: 'cd_classificacao', type: 'int'},
															  {reference: 'pr_depreciacao', type: 'float'},
															  /* modelo veiculo */
															  {reference: 'cd_modelo', type: 'int'},
															  {reference: 'cd_marca', type: 'int'},
															  {reference: 'nr_portas', type: 'int'},
															  {reference: 'tp_combustivel', type: 'int'},
															  {reference: 'nr_capacidade', type: 'java.lang.String'},
															  {reference: 'tp_reboque', type: 'int'},
															  {reference: 'tp_carga', type: 'int'},
															  {reference: 'nr_potencia', type: 'int'},
															  {reference: 'nr_cilindrada', type: 'int'},
															  {reference: 'qt_capacidade_tanque', type: 'int'},
															  {reference: 'qt_consumo_urbano', type: 'float'},
															  {reference: 'qt_consumo_rodoviario', type: 'float'},
															  {reference: 'tp_eixo_dianteiro', type: 'int'},
															  {reference: 'tp_eixo_traseiro', type: 'int'},
															  {reference: 'qt_eixos_dianteiros', type: 'int'},
															  {reference: 'qt_eixos_traseiros', type: 'int'},
															  {reference: 'nm_modelo', type: 'java.lang.String'}],
										  gridOptions: {columns: [{label:'Marca', reference: 'NM_MARCA'}, 
																  {label:'Modelo', reference: 'NM_MODELO'}],
													    strippedLines: true,
														columnSeparator: true,
														lineSeparator: false},
										  lines: [[{reference: 'cd_marca', label:'Marca', width:50, type: 'lookup', reference: 'cd_marca', viewReference: 'nm_marca', findAction: function() { btnFindMarcaOnClick(); }},
												   {reference: 'nm_modelo', label:'Nome do modelo', width:50, charcase: 'uppercase', maxLength:50}],
												  [{reference: 'nr_portas', label:'Nº portas', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'nr_capacidade', label:'Passageiros', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'nr_potencia', label:'Potência (HP)', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'nr_cilindrada', label:'Cilindradas', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'qt_eixos_dianteiros', label:'Eixos dianteiros', value:1, width:12, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'tp_eixo_dianteiro', label:'&nbsp;', width:18, type: 'select', options: []},
										  		   {reference: 'qt_eixos_traseiros', label:'Eixos traseiros', value:1, width:12, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'tp_eixo_traseiro', label:'&nbsp;', width:18, type: 'select', options: []}],
												  [{reference: 'qt_capacidade_tanque', label:'Tanque (Lt.)', width:10, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'tp_combustivel', label:'Combustível', width:20, type: 'select', options: []},
												   {reference: 'qt_consumo_urbano', label:'Consumo Urb.', width:15, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'qt_consumo_rodoviario', label:'Consumo Rod.', width:15, datatype: 'INT', mask: '#DIGITS'},
												   {reference: 'tp_reboque', label:'Tipo reboque', width:20, type: 'select', options: []},
												   {reference: 'tp_carga', label:'Tipo carga', width:20, type: 'select', options: []}]],
										  focusField:'field_nm_modelo'});
	loadOptions($('field_tp_eixo_dianteiro'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoEixo)%>); 
	loadOptions($('field_tp_eixo_traseiro'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoEixo)%>); 
	loadOptions($('field_tp_combustivel'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoCombustivel)%>); 
	loadOptions($('field_tp_reboque'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoReboque)%>); 
	loadOptions($('field_tp_carga'), <%=Jso.getStream(com.tivic.manager.fta.VeiculoServices.tipoCarga)%>); 
}

var jTipoComponente;
function miTipoComponenteOnClick() {
	jTipoComponente = FormFactory.createQuickForm('jTipoComponente', {caption: 'Tipos de equipamentos', width: 500, height: 400, noDrag: true,
										  //quickForm
										  id: "fta_tipo_componente",
										  classDAO: 'com.tivic.manager.fta.TipoComponenteDAO',
										  classMethodGetAll: 'com.tivic.manager.fta.TipoComponenteServices',
										  keysFields: ['cd_tipo_componente'],
										  unitSize: '%',
										  constructorFields: [{reference: 'cd_tipo_componente', type: 'int'},
															  {reference: 'nm_tipo_componente', type: 'java.lang.String'},
															  {reference: 'txt_tipo_componente', type: 'java.lang.String'},
															  {reference: 'qt_hodometro_validade', type: 'float'},
															  {reference: 'qt_hodometro_manutencao', type: 'float'},
															  {reference: 'tp_recorrencia_manutencao', type: 'int'},
															  {reference: 'qt_intervalo_recorrencia', type: 'int'}],
										  gridOptions: {columns: [{label:'Tipo', reference: 'nm_tipo_componente'},
										                          {label:'Recorrência', reference: 'ds_intervalo_recorrencia'},
										                          {label:'Validade', reference: 'qt_hodometro_validade', type: GridOne._FLOAT, mask: '#,###'},
										                          {label:'Manutenção', reference: 'qt_hodometro_manutencao', type: GridOne._FLOAT, mask: '#,###'}],
										                onProcessRegister: function(register){
										               	 		switch(register['TP_RECORRENCIA_MANUTENCAO']){
										               	 			case 0: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'nenhum'; break;
										               	 			case 1: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'por dia'; break;
										               	 			case 2: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'por semana'; break;
										               	 			case 3: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'por mês'; break;
										               	 			case 4: register['DS_TP_RECORRENCIA_MANUTENCAO'] = 'por ano'; break;
										               	 		}
										               	 		register['DS_INTERVALO_RECORRENCIA'] = register['TP_RECORRENCIA_MANUTENCAO']==0?'':
										               	 											   (register['QT_INTERVALO_RECORRENCIA'] + (register['QT_INTERVALO_RECORRENCIA']==1?' vez ':' vezes ') + register['DS_TP_RECORRENCIA_MANUTENCAO']); 
										               	 	},
													    strippedLines: true,
														columnSeparator: true,
														noSelector: true,
														lineSeparator: false},
										  lines: [[{reference: 'nm_tipo_componente', label:'Nome', width:100, charcase: 'uppercase', maxLength:50}],
										          [{id: 'gbManutencaoKilometragem', type: 'groupbox', label: 'Check-up por quilometragem', width: 50, height: 30, lines: 
										          		[[{reference: 'qt_hodometro_validade', label:'Km. Validade', width:50, datatype: 'FLOAT', mask: '#,###'},
										           		 {reference: 'qt_hodometro_manutencao', label:'Km. Manutenção', width:50, datatype: 'FLOAT', mask: '#,###'}]]},
										           {id: 'gbManutencaoRecorrencia', type: 'groupbox', label: 'Check-up por Recorrência', width: 50, height: 30, lines: 
										           		[[{reference: 'qt_intervalo_recorrencia', label:'Intervalo', value: 1, defaultValue: 1, width: 40, datatype: 'INT', mask: '#DIGITS'},
										           		  {reference: 'tp_recorrencia_manutencao', label:'Recorrência', width: 60, type: 'select', datatype: 'INT', options: <%=Jso.getStream(com.tivic.manager.fta.TipoComponenteServices.tipoRecorrencia)%>}]]}],
										          [{reference: 'txt_tipo_componente', label:'Observação', width:100, height: 100, type: 'textarea'}]],
										  focusField:'field_nm_tipo_componente'});
}

function btnFindMarcaOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Marcas", width: 500, height: 300, modal: true, noDrag: true,
												   className: "com.tivic.manager.bpm.MarcaServices",
												   method: "findGruposMarca",
												   allowFindAll: true,
												   filterFields: [[{label:"Marca", reference:"NM_MARCA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"Marca", reference:"NM_MARCA"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference: 'B.CD_GRUPO', value: '<%=ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_MARCA_VEICULO", 0)%>', datatype:_VARCHAR, comparator:_EQUAL}],
												   callback: btnFindMarcaOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
		if ($('field_cd_marca') != null){
			$('field_cd_marca').value = reg[0]['CD_MARCA'];
		}
		if ($('field_cd_marcaView') != null){
			$('field_cd_marcaView').value = reg[0]['NM_MARCA'];
		}
    }
}

function miPedidoCompraOnClick()	{
	createWindow('jPedidoCompra', {caption: 'Pedidos de Compra', width: 700, height: 478, contentUrl: '../adm/pedido_compra.jsp?cdEmpresa=' + getValue('cdEmpresa') + '&tpPedidoCompra=<%=com.tivic.manager.adm.PedidoCompraServices.PED_COMPRA%>'});
}

function miOrdemCompraOnClick() {
	createWindow('jOrdemCompra', {caption: 'Ordens de Compra', 
								  width: 700, 
								  height: 488, 
								  contentUrl: '../adm/ordem_compra.jsp?cdEmpresa=' + $('cdEmpresa').value});
}


function miMapaFiscalOnClick(){
	createWindow('jMapaFiscal', {caption: 'Mapa Fiscal', 
		  width: 700, 
		  height: 280, 
		  contentUrl: '../fsc/relatorio_mapa_fiscal.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

var meses = <%=Jso.getStream(Util.meses)%>;
function miSintegraOnClick(){
	FormFactory.createFormWindow('jSINTEGRA', 
            {caption: "Sintegra", width: 600, height: 195, unitSize: '%', modal: true,
			  id: 'sintegra', loadForm: true, noDrag: true, cssVersion: '2',
			  hiddenFields: [{id:'cdEmpresaGrid', reference: 'cd_empresa', value: $('cdEmpresa').value}],
			  lines: [[{id:'nrMes', type:'select', label:'Mês', width:20},
			           {id:'nrAno', type:'text', label:'Ano', width:10},
			           {type: 'space', width:70}],
			           [{id:'tpEstrutura', type:'select', label:'Estrutura do Arquivo', width:100}],
			           [{id:'tpNatureza', type:'select', label:'Natureza das Operações Informadas', width:100}],
			           [{id:'tpFinalidade', type:'select', label:'Finalidade de Apresentação do Arquivo', width:100}],
				  	  [{type: 'space', width:60},
					   {id:'btnGerarGrid', type:'button', label:'Gerar', width: 20, height:19, image: '/sol/imagens/form-btSalvar16.gif', onClick: function(){miGerarSintegraOnClick();}},
					   {id:'btnCancelarGrid', type:'button', label:'Retornar', width: 20, height:19, image: '/sol/imagens/form-btCancelar16.gif', 
						onClick: function(){	closeWindow('jSINTEGRA');	}}]]});
	
	
	loadOptions($('tpEstrutura'), <%=Jso.getStream(com.tivic.manager.fsc.SintegraServices.estrutura)%>);
	loadOptions($('tpNatureza'), <%=Jso.getStream(com.tivic.manager.fsc.SintegraServices.natureza)%>);
	loadOptions($('tpFinalidade'), <%=Jso.getStream(com.tivic.manager.fsc.SintegraServices.finalidade)%>);
	loadOptions($('nrMes'), meses);
	$('nrMes').value        = (new Date().getMonth() > 0) ? new Date().getMonth() - 1 : 11;  
	$('nrAno').value        = (new Date().getMonth() > 0) ? new Date().getFullYear() : new Date().getFullYear() - 1;  
	$('tpEstrutura').value  = 3;
	$('tpNatureza').value   = 3;
	$('tpFinalidade').value = 1;
	$('nrMes').focus();
}

function miGerarSintegraOnClick(content){
	var iframe = $('iframe_save');
	if(iframe==null)	{
		iframe = document.createElement("iframe");
		iframe.id = 'iframe_save';
		iframe.style.visibility = 'hidden';
		$('topPanel').appendChild(iframe);
	}
	iframe.src = "../save_file.jsp?tpLocalizacao=1&className=com.tivic.manager.fsc.SintegraServices"+
				 "&method=gerarSintegra(const "+$('cdEmpresa').value+":int,const "+(parseInt($('nrMes').value, 10) + 1)+":int,"+
				 "const "+$('nrAno').value+":int,const "+$('tpEstrutura').value+":int,const "+$('tpNatureza').value+":int,const "+$('tpFinalidade').value+":int)"+
				 "&filename=SINTEGRA_"+meses[$('nrMes').value]+'_'+$('nrAno').value+".txt";
	closeWindow('jSINTEGRA');
}


function miTipoCreditoOnClick() {
	var optionsTipo = [{value: <%=TipoCreditoServices.TP_CREDITO_TRIBUTADA_MERCADO_INTERNO%>, text: 'CÓDIGOS VINCULADOS À RECEITA TRIBUTADA NO MERCADO INTERNO'},
					   {value: <%=TipoCreditoServices.TP_CREDITO_NAO_TRIBUTADA_MERCADO_INTERNO%>, text: 'CÓDIGOS VINCULADOS À RECEITA NÃO TRIBUTADA NO MERCADO INTERNO'},
					   {value: <%=TipoCreditoServices.TP_CREDITO_EXPORTACAO%>, text: 'CÓDIGOS VINCULADOS À RECEITA DE EXPORTAÇÃO'}];
	FormFactory.createQuickForm('jTabelaEvento', {caption: 'Manutenção de Tipo de Crédito', width: 500, height: 350,
												  //quickForm
												  id: "fsc_tipo_credito",
												  classDAO: 'com.tivic.manager.fsc.TipoCreditoDAO',
												  classMethodGetAll: 'com.tivic.manager.fsc.TipoCreditoServices',
												  keysFields: ['cd_tipo_credito'], unitSize: '%',
												  constructorFields: [{reference: 'cd_tipo_credito', type: 'int'},
													  				  {reference: 'nm_tipo_credito', type: 'java.lang.String'},
													  				  {reference: 'nr_tipo_credito', type: 'java.lang.String'},
													  				  {reference: 'tp_grupo_tipo_credito', type: 'int'}],
												  gridOptions: {columns: [{reference: 'nr_tipo_credito', label: 'Número'},
								  			   							  {reference: 'nm_tipo_credito', label: 'Descrição'},
								  			   							  {reference: 'CL_TIPO_CREDITO_GRUPO', label: 'Grupo'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'nr_tipo_credito', label: 'Número', width:30, maxLength:2},
											   			   {reference: 'nm_tipo_credito', label: 'Descrição', width:70, maxLength:50}],
												   		   [{reference: 'tp_grupo_tipo_credito', label: 'Grupo', width:100, type: 'select',
															options: optionsTipo}]]});
}

function miTipoContribuicaoSocialOnClick() {
	FormFactory.createQuickForm('jTabelaContribuicaoSocial', {caption: 'Manutenção de Tipo de Contribuição Social', width: 500, height: 350,
												  //quickForm
												  id: "fsc_tipo_contribuicao_social",
												  classDAO: 'com.tivic.manager.fsc.TipoContribuicaoSocialDAO',
												  classMethodGetAll: 'com.tivic.manager.fsc.TipoContribuicaoSocialServices',
												  keysFields: ['cd_tipo_contribuicao_social'], unitSize: '%',
												  constructorFields: [{reference: 'cd_tipo_contribuicao_social', type: 'int'},
													  				  {reference: 'nm_tipo_contribuicao_social', type: 'java.lang.String'},
													  				  {reference: 'nr_tipo_contribuicao_social', type: 'java.lang.String'}],
												  gridOptions: {columns: [{reference: 'nr_tipo_contribuicao_social', label: 'Número'},
								  			   							  {reference: 'nm_tipo_contribuicao_social', label: 'Descrição'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'nr_tipo_contribuicao_social', label: 'Número', width:30, maxLength:2},
											   			   {reference: 'nm_tipo_contribuicao_social', label: 'Descrição', width:70, maxLength:50}]]});
}

</script>

</head>
<body style="margin:0px; background:#F5F5F5">
<a href="../index.jsp" class="back" style="bottom: 20px; left: 20px; position: fixed; text-decoration: none; color: #000;" title="Voltar ao Portal"> <img src="../imagens/arrow-left.png" style="display: block; margin: 0 auto;"></img>
	<p style="text-align: center; font-size: 11px; font-family: tahoma; margin: 5px;">Voltar ao Portal</p>
</a>
<input id="cdEmpresa" name="cdEmpresa" type="hidden"
		value="<%=empresa != null ? empresa.getCdPessoa() : 0%>" />
	<input id="cdUsuario" name="cdUsuario" type="hidden"
		value="<%=usuario != null ? usuario.getCdUsuario() : 0%>" />
	<input id="nmLogin" name="nmLogin" type="hidden"
		value="<%=usuario != null ? usuario.getNmLogin() : ""%>" />
	<input id="nmUsuario" name="nmUsuario" type="hidden"
		value="<%=nmUsuario%>" />
	<input id="tpUsuario" name="tpUsuario" type="hidden"
		value="<%=usuario != null ? usuario.getTpUsuario()
						: com.tivic.manager.seg.UsuarioServices.USUARIO_COMUM%>" />
<input name="cdLocalArmazenamento" type="hidden" id="cdLocalArmazenamento"/>
<div class="topPanel" style="height:100px" id="topPanel">
	<div id="userPanel" style="height:30px; width:280px; border:1px solid #CCCCCC; background-color:#FFFFFF;  float: right; margin: 35px 5px 0 0; display: none; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:11px;">
		<strong>&nbsp;&nbsp;Operador:&nbsp;</strong><span id="NM_OPERADOR"></span><br/>
		<strong>&nbsp;&nbsp;Empresa:&nbsp;</strong><span id="NM_EMPRESA"></span>
	</div>
	<cpnt:barraMenu id="barraMenu" style="office"> 
		  <barramenu>
			<menu id="nmTabelas" rotulo="Tabelas Básicas" imagem="" letra="" teclas="" comando="">
				<item id="miCadastrosEndereco" rotulo="Endereçamento" imagem="../grl/imagens/endereco16.gif" letra="" teclas="">
					<menu id="" rotulo="" imagem="" letra="" teclas="" comando="">
						<item id="miRegiao" rotulo="Regiões" imagem="../grl/imagens/regiao16.gif" letra="" teclas="" comando="miRegiaoOnClick()"/>
						<item id="miTipoEndereco" rotulo="Tipos de Endereço" imagem="../grl/imagens/tipo_endereco16.gif" letra="" teclas="" comando="miTipoEnderecoOnClick()"/>
						<item id="miSeparator2" rotulo="" imagem="" letra="" teclas="" comando=""/>
                        <item id="miPais" rotulo="Países" imagem="../grl/imagens/pais16.gif" letra="" teclas="" comando="miPaisOnClick(null)"/>				
						<item id="miEstado" rotulo="Estados" imagem="../grl/imagens/estado16.gif" letra="" teclas="" comando="miEstadoOnClick(null)"/>				
						<item id="miCidade" rotulo="Cidades" imagem="../grl/imagens/cidade16.gif" letra="" teclas="" comando="miCidadeOnClick()"/>
						<item id="miTipoLogradouro" rotulo="Tipos de Logradouro" imagem="../grl/imagens/grupo16.gif" letra="" teclas="" comando="miTipoLogradouroOnClick()"/>
						<item id="miLogradouro" rotulo="Logradouros" imagem="../grl/imagens/logradouro16.gif" letra="" teclas="" comando="miLogradouroOnClick()"/>				
					</menu>
				</item>				
				<item id="miModeloDocumento" rotulo="Modelos de Documentos" imagem="../grl/imagens/modelo_documento16.gif" letra="" teclas="" comando="miModeloDocumentoOnClick()"/>
				<item id="miVinculo" rotulo="Vínculos" imagem="../grl/imagens/grupo16.gif" letra="" teclas="" comando="miVinculoOnClick()"/>				
				<separador-h/>
				<item id="miNivelLocalOnClick" rotulo="Níveis de Armazenamento" imagem="imagens/nivel_local16.gif" letra="" teclas="" comando="miNivelLocalOnClick()"/>				
				<item id="miLocalArmazenamentoOnClick" rotulo="Locais de Armazenamento" imagem="imagens/local_armazenamento16.gif" letra="" teclas="" comando="miLocalArmazenamentoOnClick({cdEmpresa: $(\'cdEmpresa\').value, cdLocalArmazenamento: $(\'cdLocalArmazenamento\').value})"/>								
				<item id="miIndicadorOnClick" rotulo="Indexadores" imagem="../grl/imagens/indicador16.gif" letra="" teclas="" comando="miIndicadorOnClick()"/>				
				<item id="miMoedaOnClick" rotulo="Moedas" imagem="../grl/imagens/moeda16.gif" letra="" teclas="" comando="miMoedaOnClick()"/>				
				<separador-h/>
				<item id="miFormaPagamento" rotulo="Forma de Pagamento" imagem="../adm/imagens/forma_pagamento16.gif" letra="" teclas="" comando="miFormaPagamentoOnClick(true, null, 1)"/>
				<item id="miPlanoPagamento" rotulo="Planos de Pagamento" imagem="../adm/imagens/plano_pagamento16.gif" letra="" teclas="" comando="miPlanoPagamentoOnClick()"/>
				<item id="miTabelaPrecoOnClick" rotulo="Tabelas de Preços" imagem="../adm/imagens/tabela_preco16.gif" letra="" teclas="" comando="miTabelaPrecoOnClick(null, cdGrupoCombustivel)"/>				
				<item id="miTabelaComissao" rotulo="Tabelas de Comissão" imagem="../adm/imagens/tabela_comissao16.gif" letra="" teclas="" comando="miTabelaComissaoOnClick()"/>
				<item id="miTipoOperacao" rotulo="Tipos de Operação de Venda" imagem="../adm/imagens/tipo_operacao16.gif" letra="" teclas="" comando="miTipoOperacaoOnClick(null)"/>
				<item id="miUnidadeMedidaOnClick" rotulo="Unidades de Medida" imagem="../grl/imagens/unidade_medida16.gif" letra="" teclas="" comando="miUnidadeMedidaOnClick()"/>				
			</menu>			
			<menu id="nmCadastros" rotulo="Cadastros" imagem="" letra="" teclas="" comando="">
				<item id="miSetor" rotulo="Setores" imagem="../grl/imagens/setor16.gif" letra="" teclas="" comando="miSetorOnClick()"/>				
				<item id="miSeparator2" rotulo="" imagem="" letra="" teclas="" comando=""/>
                <item id="miPessoa" rotulo="Pessoas (Geral)" imagem="../grl/imagens/pessoa16.gif" letra="" teclas="" comando="miPessoaOnClick()"/>				
				<item id="miFornecedor" rotulo="Fornecedores" imagem="../alm/imagens/fornecedor24.gif" letra="" teclas="" comando="miPessoaOnClick()"/>
				<item id="miTransportadora" rotulo="Transportadoras" imagem="../alm/imagens/transportadora24.gif" letra="" teclas="" comando="miPessoaOnClick()"/>
				<item id="miEventoFinanceiro" rotulo="Eventos Financeiros" imagem="../alm/imagens/evento_financeiro24.gif" letra="" teclas="" comando="miTabelaEventoFinanceiroOnClick()"/>
				<item id="miNcms" rotulo="NCMs" letra="" teclas="" comando="miTabelaNcmsOnClick()"/>
				<item id="miSeparator2" rotulo="" imagem="" letra="" teclas="" comando=""/>
                <item id="miServico" rotulo="Serviços" imagem="../grl/imagens/servico16.gif" letra="" teclas="" comando="miServicoOnClick()"/>
				<item id="miGrupoOnClick" rotulo="Grupos de Produtos" imagem="imagens/grupo_produto16.gif" letra="" teclas="" comando="miGrupoOnClick()"/>				
                <item id="miProduto" rotulo="Produtos" imagem="../grl/imagens/produto16.gif" letra="" teclas="" comando="miProdutoOnClick(0,'Produtos',{cdLocalArmazenamento:$('cdLocalArmazenamento').value})"/>
                <% while (rsmGrupos != null && rsmGrupos.next()) {
                	if(rsmGrupos.getString("nm_grupo") == null || rsmGrupos.getString("nm_grupo").equals(""))
                		continue;
					String img     = "produto_dinamico16.gif";
					String nmGrupo = rsmGrupos.getString("nm_grupo") == null ? "" : rsmGrupos.getString("nm_grupo").toLowerCase();
					if(nmGrupo.indexOf("livro") >= 0)
						img = "livro16.gif";
					else if(nmGrupo.indexOf("medicamento") >= 0 || nmGrupo.indexOf("remédio")>=0 || nmGrupo.indexOf("remedio")>=0)
						img = "medicamento16.gif";
					%>
                	<item id="miProduto_<%=rsmGrupos.getInt("cd_grupo")%>" rotulo="<%=Util.capitular(nmGrupo)%>" imagem="../alm/imagens/<%=img%>" letra="" teclas="" comando="miProdutoOnClick(<%=rsmGrupos.getInt("cd_grupo")%>, \'<%=Util.capitular(rsmGrupos.getString("nm_grupo")==null ? "" : rsmGrupos.getString("nm_grupo").toLowerCase())%>\')"/>
                <% } %>
            </menu>
			<menu id="mnLancamentos" rotulo="Lançamentos" imagem="" letra="" teclas="" comando="">
				<item id="miPedidoCompra" rotulo="Pedido de Compra" imagem="../adm/imagens/pedido_compra16.gif" letra="" teclas="" comando="miPedidoCompraOnClick()"/>
				<item id="miOrdemCompra" rotulo="Ordem de Compra" imagem="../adm/imagens/ordem_compra16.gif" letra="" teclas="" comando="miOrdemCompraOnClick()"/>
				<separador-h/>
				<item id="miDocumentoEntrada" rotulo="Entradas" imagem="imagens/documento_entrada16.gif" letra="" teclas="" comando="miDocumentoEntradaOnClick()"/>				
				<item id="miDocumentoSaida" rotulo="Saídas" imagem="imagens/documento_saida16.gif" letra="" teclas="" comando="miDocumentoSaidaOnClick()"/>
				<item id="miVendasExternas" rotulo="Venda Externa" imagem="imagens/documento_saida16.gif" letra="" teclas="" comando="miVendasExternasOnClick()"/>				
				<separador-h/>
				<item id="miPedidoVenda" rotulo="Pedido do Cliente" imagem="imagens/venda16.gif" letra="" teclas="" comando="miPedidoVendaOnClick()"/>
				<item id="miTransferencia" rotulo="Transferências" imagem="imagens/transferencia16.gif" letra="" teclas="" comando="miTransferenciaOnClick()"/>				
				<item id="miFilaSolicitacaoMaterial" rotulo="Fila de Solicitações" imagem="../adm/imagens/solicitacao_material16.gif" letra="" teclas="" comando="miFilaSolicitacaoMaterialOnClick()"/>				
				<separador-h/>
                <item id="miProdutoEstoque" rotulo="Contagem de Estoque" imagem="../alm/imagens/local_armazenamento16.gif" letra="" teclas="" comando="miProdutoPrecoOnClick({tpEntrada:1})"/>
                <item id="miProdutoPreco" rotulo="Definição de Preços" imagem="../alm/imagens/tabela_preco16.gif" letra="" teclas="" comando="miProdutoPrecoOnClick({tpEntrada:0})"/>
                <item id="miTransfereProdutoPreco" rotulo="Transfere Doc. Saida, Produto e Preço" imagem="../alm/imagens/copy_documento16.gif" letra="" teclas="" comando="miTransfereProdutoPrecoOnClick({tpEntrada:0})"/>
				<separador-h/>
				<item id="miAcertoConsignacaoEntradaOnClick" rotulo="Entradas Consignadas (Fornecedores)" imagem="imagens/documento_entrada16.gif" letra="" teclas="" comando="miAcertoConsignacaoEntradaOnClick()"/>				
				<item id="miAcertoConsignacaoSaidaOnClick" rotulo="Saída Consignadas (Clientes)" imagem="imagens/documento_saida16.gif" letra="" teclas="" comando="miAcertoConsignacaoSaidaOnClick()"/>				
				<separador-h/>
				<item id="miNotaFiscalOnClick" rotulo="Nota Fiscal Eletrônica" imagem="../fsc/imagens/nfe16.gif" letra="" teclas="" comando="miNfeViewOnClick()"/>
				<item id="miInutilizacaoNotaFiscalOnClick" rotulo="Inutilização de Nota Fiscal Eletrônica" imagem="../fsc/imagens/nfe16.gif" letra="" teclas="" comando="miNfeInutilizacaoOnClick({cdEmpresa: document.getElementById(\'cdEmpresa\').value})"/>
				<separador-h/>
				<item id="miSincronizacaoOnClick" rotulo="Sincronização" imagem="../seg/imagens/sincronizacao16.png" letra="" teclas="" comando="miSincronizacaoOnClick()"/>	
			</menu>	
			<menu id="mnFiscal" rotulo="Fiscal" imagem="" letra="" teclas="" comando="">
				<item id="miClassificacaoFiscalOnClick" rotulo="Classificação Fiscal" imagem="../adm/imagens/classificacao_fiscal16.gif" letra="" teclas="" comando="miClassificacaoFiscalOnClick()"/>				
				<item id="miNaturezaOperacaoOnClick" rotulo="Natureza da Operação" imagem="imagens/natureza_operacao16.gif" letra="" teclas="" comando="miNaturezaOperacaoOnClick()"/>
				<item id="miTributoOnClick" rotulo="Tributos (Impostos, Taxas e Contribuições)" imagem="../fsc/imagens/tributo16.gif" letra="" teclas="" comando="miTributoOnClick()"/>
				<item id="miTipoCreditoOnClick" rotulo="Tipo de Crédito" imagem="" letra="" teclas="" comando="miTipoCreditoOnClick()"/>
				<item id="miTipoContribuicaoSocialOnClick" rotulo="Tipo de Contribuição Social" imagem="" letra="" teclas="" comando="miTipoContribuicaoSocialOnClick()"/>				
				<separador-h/>
				<item id="miGerarSintegraOnClick" rotulo="Sintegra" imagem="../fsc/imagens/sintegra16.gif" letra="" teclas="" comando="miSintegraOnClick()"/>			
				<item id="miGerarSpedOnClick" rotulo="SPED" imagem="../fsc/imagens/sped16.gif" letra="" teclas="" comando="miSpedOnClick()"/>				
			</menu>			
			<menu id="mnLogistica" rotulo="Logística" imagem="" letra="" teclas="" comando="">
				<item id="miCadastrosLogistica" rotulo="Tabelas" imagem="../fta/imagens/carro16.gif" letra="" teclas="">
					<menu id="" rotulo="" imagem="" letra="" teclas="" comando="">
						<item id="miMarcaVeiculo" rotulo="Marcas" imagem="../fta/imagens/marca16.gif" letra="" teclas="" comando="miMarcaOnClick()"/>								
						<item id="miModeloVeiculo" rotulo="Modelos de veículo" imagem="../fta/imagens/carro16.gif" letra="" teclas="" comando="miModeloVeiculoOnClick()"/>								
						<item id="miTipoVeiculo" rotulo="Tipos de veículo" imagem="../fta/imagens/carro16.gif" letra="" teclas="" comando="miTipoVeiculoOnClick()"/>
					</menu>
				</item>				
				<item id="miSeparator1" rotulo="" imagem="" letra="" teclas="" comando=""/>	
				<item id="miVeiculo" rotulo="Veículos" imagem="../fta/imagens/carro16.gif" letra="" teclas="" comando="miVeiculosOnClick()"/>				
				<item id="miMotorista" rotulo="Motoristas" imagem="../fta/imagens/driver16.gif" letra="" teclas="" comando="miFuncionarioOnClick(\'Motoristas\', null, 1, null, [])"/>
			</menu>			
			<menu id="mnFerramentas" rotulo="Ferramentas" imagem="" letra="" teclas="" comando="">
				<item id="miBalanco" rotulo="Balanços" imagem="imagens/recalculo_estoque16.gif" letra="" teclas="" comando="miBalancoOnClick()"/>				
				<item id="miRecalculoEstoque" rotulo="Recálculo de Estoque" imagem="imagens/recalculo_estoque16.gif" letra="" teclas="" comando="miRecalculoEstoqueOnClick()"/>				
				<item id="miRecalculoCusto" rotulo="Recálculo de Preço de Custo" imagem="imagens/recalculo_preco16.gif" letra="" teclas="" comando="miRecalculoCustoOnClick()"/>				
				<item id="miCopiaProduto" rotulo="Cópia de Produtos" imagem="imagens/recalculo_preco16.gif" letra="" teclas="" comando="miCopiaProdutoOnClick()"/>				
				<item id="miSelectEmpresaLocal" rotulo="Selecionar Empresa e Local de Armazenamento padrão" imagem="imagens/local_armazenamento_select16.gif" letra="" teclas="" comando="miSelectEmpresaLocalOnClick()"/>				
			</menu>			
			<menu id="mnRelatorios" rotulo="Relatórios" imagem="" letra="" teclas="" comando="">
				<item id="miRelatorioPessoa" rotulo="Cadastro Geral (Pessoas Físicas e Jurídicas)" imagem="../grl/imagens/pessoa16.gif" letra="" teclas="" comando="miRelatorioPessoaOnClick()"/>				
				<item id="miRelProduto" rotulo="Relação de Produtos (Inventário)" imagem="../grl/imagens/produto16.gif" letra="" teclas="" comando="miRelatorioProdutoOnClick()"/>
				<item id="miRelNcm" rotulo="Relatório de Ncm" imagem="" letra="" teclas="" comando="miRelatorioNcmOnClick()"/>				
				<item id="miRelMovimento" rotulo="Movimento de Produtos" imagem="../alm/imagens/movimentacao_produto16.gif" letra="" teclas="" comando="miRelatorioMovimentoProdutoOnClick()"/>				
				<item id="miRelEntrada" rotulo="Entradas (Compras)" imagem="../alm/imagens/documento_entrada16.gif" letra="" teclas="" comando="miRelatorioEntradaOnClick()"/>				
				<item id="miRelSaida" rotulo="Saídas (Vendas)" imagem="../alm/imagens/documento_saida32.gif" letra="" teclas="" comando="miRelatorioSaidaOnClick()"/>				
				<item id="miRelSaida" rotulo="Vendas por Vendedor (Comissão)" imagem="../alm/imagens/documento_saida32.gif" letra="" teclas="" comando="miRelatorioComissaoOnClick()"/>				
				<item id="miRelPontoPedido" rotulo="Ponto de Pedido" imagem="../alm/imagens/documento_saida32.gif" letra="" teclas="" comando="miRelatorioPontoPedidoOnClick()"/>				
				<item id="miRelCurvaAbc" rotulo="Curva ABC" imagem="../alm/imagens/documento_saida32.gif" letra="" teclas="" comando="miRelatorioCurvaAbcOnClick({cdEmpresa: document.getElementById(\'cdEmpresa\').value})"/>				
				<item id="miRelBalanco" rotulo="Balanço" imagem="../grl/imagens/produto16.gif" letra="" teclas="" comando="miRelatorioBalancoOnClick()"/>				
				<separador-h/>
				<item id="miRelSaldoAcertoEntrada" rotulo="Saldo de Acertos de Consignação a Realizar - Fornecedores" imagem="/sol/imagens/print16.gif" letra="" teclas="" comando="miSaldoAcertosEntradaOnClick()"/>				
				<item id="miRelAcertoEntrada" rotulo="Acertos de Consignação - Fornecedores" imagem="imagens/documento_entrada16.gif" letra="" teclas="" comando="miAcertosConsignacaoEntradaOnClick()"/>				
				<separador-h/>
				<item id="miRelSaldoAcertoEntrada" rotulo="Saldo de Acertos de Consignação a Realizar - Clientes" imagem="/sol/imagens/print16.gif" letra="" teclas="" comando="miSaldoAcertosSaidaOnClick()"/>				
				<item id="miRelAcertoEntrada" rotulo="Acertos de Consignação - Clientes" imagem="imagens/documento_saida16.gif" letra="" teclas="" comando="miAcertosConsignacaoSaidaOnClick()"/>				
				<separador-h/>
				<item id="miRelHistoricoPrecoProdutos" rotulo="Evolução de Preços" imagem="../adm/imagens/evolucao_preco16.gif" letra="" teclas="" comando="miHistoricoPrecoProdutosOnClick()"/>				
				<item id="miRelTabelaPrecosOnClick" rotulo="Tabelas de Preços" imagem="../adm/imagens/tabela_preco16.gif" letra="" teclas="" comando="miRelatorioTabPrecoOnClick()"/>
				<item id="miMapaFiscalOnClick" rotulo="Mapa Fiscal" imagem="../fsc/imagens/fiscal16.png" letra="" teclas="" comando="miMapaFiscalOnClick()"/>				
			</menu>			
			<menu id="mnSeguranca" rotulo="Segurança" imagem="" letra="" teclas="" comando="">
				<item id="miEmpresa" rotulo="Empresas" imagem="../grl/imagens/empresa16.gif" letra="" teclas="" comando="miEmpresaOnClick()"/>
				<item id="miSenha" rotulo="Alterar senha..." imagem="/sol/imagens/senha16.gif" letra="" teclas="" comando="miAlterarSenhaOnClick()"/>
				<separador-h/>
				<item id="miConfGrupo" rotulo="Grupos" imagem="../seg/imagens/grupo16.gif" letra="" teclas="" comando="miConfGrupoOnClick()"/>
				<item id="miConfUsuario" rotulo="Usuários" imagem="../seg/imagens/usuario16.gif" letra="" teclas="" comando="miConfUsuarioOnClick()"/>
				<separador-h/>
				<item id="miLogoff" rotulo="Logoff" imagem="/sol/imagens/logoff16.gif" letra="" teclas="" comando="login(\'Informe login e senha...\')"/>				
			</menu>			
		</barramenu>
	</cpnt:barraMenu>
	<div id="toolBar" style="height:68px; width:800px; border:0 solid; margin-top:5px;"></div>
</div>
<div align="center">
    <div class="mainPanel" style="height:460px; background-color:#FFFFFF; border-top: 1px solid #CCCCCC;">
      <div style="margin:130px 0 0 0; width:500px; height:180px; border:1px solid #E1E1E1; font-family:Verdana, Arial, Helvetica, sans-serif; font-size:10px">
        <div style="background-color:#F2F2F2; padding:10px; height:50px;"> <img src="../imagens/minimodulo_estoque.gif" style="float:left" />
            <div style="text-align:right; float:right"><br />
              v1.0.0<br />
              &copy;2005-2008 sol Solu&ccedil;&otilde;es<br />
              Todos os direitos reservados </div>
        </div>
        <div style="margin-top:15px;" align="center"> Este programa de computador &eacute; protegido por leis<br />
          de direitos autorais e tratados internacionais. <br />
          A reprodu&ccedil;&atilde;o ou distribui&ccedil;&atilde;o n&atilde;o autorizada deste <br />
          programa, ou de qualquer parte dele, resultar&aacute; em severas <br />
          puni&ccedil;&otilde;es civis e criminais, e os infratores ser&atilde;o punidos <br />
          sob a m&aacute;xima extens&atilde;o poss&iacute;vel dentro da lei. </div>
      </div>
    </div>
</div>
</body>
<script language="javascript">
	init();
	loadEmpresas();
	login();
	validaCertificado();
</script>
<%
	}
	catch(Exception e) {
	}
%>
</html>