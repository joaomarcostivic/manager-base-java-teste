<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.util.*" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<script language="javascript">
var rsmPessoa;
var stFuncional 	= <%=sol.util.Jso.getStream(com.tivic.manager.srh.DadosFuncionaisServices.situacaoFuncional)%>;
var stEstadoCivil 	= <%=sol.util.Jso.getStream(com.tivic.manager.grl.PessoaFisicaServices.situacaoEstadoCivil)%>;
var tpSexo 			= ['M','F'];
var gridPessoa = null;
var gridColumnReport;
var registro;
var tab;
function init()	{
	var divAbaGrafico = ChartFactory.createChartForm({width: 685, height: 350, id: 'divAbaGrafico', gridName: 'gridPessoa'});
	var divAbaDados = ReportOne.createFindResultForm({width: 685, height: 360, id: 'divAbaDados', columnsGridPlace: 'divGridColumnReport', resultGridPlace: 'divGridPessoa', reportTitle: 'Relatório de Pessoas'});
	$('RelatorioPessoa').appendChild(divAbaDados);
	$('RelatorioPessoa').appendChild(divAbaGrafico);
	enableTabEmulation();
	// TOOLBUTTON
	tab = TabOne.create('tabRelatorioPessoa',  {width: 690,height: 365,
											tabs: [{caption: 'Opções de Filtros e Configurações', 
													 reference:'divAbaFiltro', 
													 image: '/sol/imagens/filtro16.gif',
													 active: true},
												   {caption: 'Resultado da Pesquisa', 
													 reference:'divAbaDados', 
													 image: '/sol/imagens/dados16.gif'},
												   {caption: 'Gráficos', 
													reference:'divAbaGrafico',
													image: '/sol/imagens/grafico16.gif'}],
											plotPlace: 'divTabRelatorio',
											tabPosition: ['top', 'left']});

    
	ToolBar.create('toolBar', {plotPlace: 'toolBarRelatorio', orientation: 'horizontal',
							   buttons: [/*{id: 'btnCarregar', img: '../imagens/load_report16.gif', label: 'Carregar Configuração', title: 'Pesquisar registros', onClick: btnPesquisarOnClick},
							   			 {id: 'btnSalvar', img: '../imagens/save_report16.gif', label: 'Gravar configuração', title: 'Gravar os filtros e configurações para serem usadas posteriormente', onClick: btnPesquisarOnClick},
										 {separator: 'horizontal'},*/
										 {id: 'btnPesquisar', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar registros', onClick: btnPesquisarOnClick},
										 {id: 'btnImprimir', img: '/sol/imagens/print16.gif', label: 'Imprimir', title: 'Imprimir dados', onClick: btnImprimirOnClick},
									     {id: 'btExportar', img: '/sol/imagens/export16.gif', label: 'Exportar', onClick: btExportarOnClick},
										 {id: 'btVisualizar', img: '../adm/imagens/visualizar16.gif', label: 'Visualizar Cadastro', onClick: viewPessoa}
										 ]});

    
    var dtDataMask = new Mask($("dtMatriculaInicial").getAttribute("mask"));
    dtDataMask.attach($("dtMatriculaInicial"));
    dtDataMask.attach($("dtMatriculaFinal"));
    dtDataMask.attach($("dtDesligamentoInicial"));
    dtDataMask.attach($("dtDesligamentoFinal"));

  	addShortcut('ctrl+d', function(){ if (!document.getElementById('btnDeletePessoaOnClick').disabled) btnDeletePessoaOnClick() });
  	addShortcut('ctrl+b', function(){ if (!document.getElementById('btnBaixaPessoaOnClick').disabled) btnBaixaPessoaOnClick() });
  	addShortcut('ctrl+k', function(){ if (!document.getElementById('btnCancelarPessoaOnClick').disabled) btnCancelarPessoaOnClick() });
	
	loadOptions($('stFuncional'), stFuncional);
	loadOptionsFromRsm($('cdTipoAdmissao'), <%=sol.util.Jso.getStream(com.tivic.manager.srh.TipoAdmissaoServices.getAll())%>, {fieldValue: 'cd_tipo_admissao', fieldText:'nm_tipo_admissao'});
	loadOptionsFromRsm($('cdVinculoEmpregaticio'), <%=sol.util.Jso.getStream(com.tivic.manager.srh.VinculoEmpregaticioServices.getAll())%>, {fieldValue: 'cd_vinculo_empregaticio', fieldText:'nm_vinculo_empregaticio'});
	loadOptionsFromRsm($('cdTurma'), <%=sol.util.Jso.getStream(com.tivic.manager.srh.TurmaServices.getAll())%>, {fieldValue: 'cd_turma', fieldText:'nm_turma'});
	loadOptionsFromRsm($('cdFuncao'), <%=sol.util.Jso.getStream(com.tivic.manager.srh.FuncaoServices.getAll(cdEmpresa))%>, {fieldValue: 'cd_funcao', fieldText:'nm_funcao'});
	loadOptionsFromRsm($('cdAgenteNocivo'), <%=sol.util.Jso.getStream(com.tivic.manager.srh.AgenteNocivoServices.getAll())%>, {fieldValue: 'cd_agente_nocivo', fieldText:'nm_agente_nocivo'});
	loadOptionsFromRsm($('cdGrupoPagamento'), <%=sol.util.Jso.getStream(com.tivic.manager.flp.GrupoPagamentoServices.getAll())%>, {fieldValue: 'cd_grupo_pagamento', fieldText:'nm_grupo_pagamento'});
	loadOptionsFromRsm($('cdTabelaSalario'), <%=sol.util.Jso.getStream(com.tivic.manager.flp.TabelaSalarioServices.getAll())%>, {fieldValue: 'cd_tabela_salario', fieldText:'nm_tabela_salario'});
	loadOptionsFromRsm($('cdCategoriaFgts'), <%=sol.util.Jso.getStream(com.tivic.manager.srh.CategoriaFgtsServices.getAll())%>, {fieldValue: 'cd_categoria_fgts', fieldText:'nm_categoria_fgts'});
	loadOptionsFromRsm($('cdBancoFgts'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.BancoServices.getAll())%>, {fieldValue: 'cd_banco', fieldText:'nm_banco'});
	loadOptionsFromRsm($('cdEscolaridade'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EscolaridadeDAO.getAll())%>, {fieldValue: 'cd_escolaridade', fieldText:'nm_escolaridade'});
	loadOptions($('tpSexo'), tpSexo);
	loadOptions($('stEstadoCivil'), stEstadoCivil);
	loadOptions($('lgValeTransporte'), <%=sol.util.Jso.getStream(com.tivic.manager.srh.DadosFuncionaisServices.valeTransporte)%>);
	loadOptions($('tpProventoPrincipal'), <%=sol.util.Jso.getStream(com.tivic.manager.srh.DadosFuncionaisServices.tipoProventoPrincipal)%>);
	loadOptions($('tpPagamento'), <%=sol.util.Jso.getStream(com.tivic.manager.srh.DadosFuncionaisServices.tipoPagamento)%>);
	var rsm = <%=sol.util.Jso.getStream(com.tivic.manager.srh.TabelaHorarioDAO.getAll())%>;
	for(var i=0; i<rsm.lines.length; i++)	{
		$('cdTabelaHorario').options[i+1] = new Option(rsm.lines[i]['NM_TABELA_HORARIO'], rsm.lines[i]['CD_TABELA_HORARIO'], false, false);
		$('cdTabelaHorario').options[i+1].setAttribute("qtHorasMes", rsm.lines[i]['QT_HORAS_MES']);
	}
	var rsmRegioes = <%=sol.util.Jso.getStream(com.tivic.manager.grl.RegiaoDAO.getAll())%>;
	loadOptionsFromRsm($('cdEstado'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EstadoServices.find(new java.util.ArrayList<sol.dao.ItemComparator>()))%>, {fieldValue: 'CD_ESTADO', fieldText:'SG_ESTADO'});
    loadOptionsFromRsm($('cdEmpresa'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
    loadOptionsFromRsm($('cdVinculo'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.VinculoDAO.getAll())%>, {fieldValue: 'cd_vinculo', fieldText:'nm_vinculo'});
	// Coluna para agrupar
	$('cdEmpresa').value = <%=cdEmpresa%>;
	btnPesquisarOnClick('{lines:[]}');
	tab.showTab(0);
	// Coluna para agrupar
	createGroupByOptions();
}

function btExportarOnClick() {
	if (gridPessoa != null) {
		gridPessoa.exportToFile();
	}
}

function createGroupByOptions()	{
	var rsmColumns = {lines: []};
	rsmColumns.lines.push({LABEL:'Vínculo Empregatício', GROUPBY:'NM_VINCULO_EMPREGATICIO', DATATYPE: _VARCHAR, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Ano de Admissão', GROUPBY:'EXTRACT(YEAR FROM DT_MATRICULA)', ALIAS: 'NR_ANO_MATRICULA', DATATYPE: _TIMESTAMP, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Situação Funcional', GROUPBY:'ST_FUNCIONAL', DATATYPE: _INTEGER, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Tipo de Admissão', GROUPBY:'NM_TIPO_ADMISSAO', DATATYPE: _VARCHAR, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Sexo', GROUPBY:'TP_SEXO', DATATYPE: _INTEGER, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Escolaridade', GROUPBY:'NM_ESCOLARIDADE', DATATYPE: _VARCHAR, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Função', GROUPBY:'NM_FUNCAO', DATATYPE: _VARCHAR, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Setor/Departamento', GROUPBY:'NM_SETOR', DATATYPE: _VARCHAR, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Tabela de Horário', GROUPBY:'NM_TABELA_HORARIO', DATATYPE: _VARCHAR, SELECAO: 0});
	
	gridOpcaoAgrupamento = GridOne.create('gridOpcaoAgrupamento', {plotPlace: $('divGridOpcaoAgrupamento'), 
	                                                               resultset: rsmColumns, 
																   noSelectorColumn: true, 
																   columns: [{label:'Coluna', reference:'label'},
	 																		 {label: 'Agrupar', reference: 'SELECAO', type: GridOne._CHECKBOX, 
																			  onCheck: function(){
																					this.parentNode.parentNode.register['SELECAO'] = this.checked ? 1 : 0;
																			  }}]}); 

	ToolBar.create('toolBar', {plotPlace: 'toolBarColunas',orientation: 'vertical',
							   buttons: [{id: 'btnColumnUp', img: '../imagens/columnUp.gif', width: 21, height: 25, onClick: function() {gridOpcaoAgrupamento.moveUpSelectedRow()}},
										 {id: 'btnColumnDown', img: '../imagens/columnDown.gif', width: 21, height: 25, onClick: function() {gridOpcaoAgrupamento.moveDownSelectedRow()}}]});
}

function btnPesquisarOnClick(content) {
	if(content==null)	{
		$('labelResultado').innerHTML = 'Resultado da Pesquisa';
		var params = 'objects=crt=java.util.ArrayList();groupBy=java.util.ArrayList();&execute=';
		columnsGrid = [];
		for(var i=0; i<gridOpcaoAgrupamento.size(); i++)	{
			var reg = gridOpcaoAgrupamento.getRegisterByIndex(i);
			if(reg['SELECAO']==1)	{
				var colName = reg['REFERENCE'] ? reg['REFERENCE'] : (reg['ALIAS'] ? reg['ALIAS'] : reg['GROUPBY']);
				if(colName.indexOf('.')>=0)	{
					colName = colName.substring(colName.indexOf('.')+1);
				}
				if(colName=='TP_SEXO')
					columnsGrid.push({reference: 'CL_SEXO', label: reg['LABEL']});
				else if(colName=='ST_FUNCIONAL')
					columnsGrid.push({reference: 'CL_SITUACAO_FUNCIONAL', label: reg['LABEL']});
				else
					columnsGrid.push({reference: colName, label: reg['LABEL']});
				var column = reg['ALIAS'] ? reg['GROUPBY']+' AS '+reg['ALIAS'] : reg['GROUPBY'];
				params += 'groupBy.add(const '+column+':Object);';
			}
		}
		if(columnsGrid.length == 0)	{
			columnsGrid = columnsPessoa;
		}
		else	{
			columnsGrid.push({label: 'Quantidade', reference:'QT_COLABORADOR'});
		}
		// LINHA 1
		// Empresa
		params = $('cdEmpresa').value > 0 ? createItemComparator(params, 'F.cd_empresa', 'cdEmpresa') : params;
		// Porte da Empresa
		params = $('stFuncional').value >= 0 ? createItemComparator(params, 'st_funcional', 'stFuncional') : params;
		// Período de Matrícula
		params = $('dtMatriculaInicial').value != '' ? createItemComparator(params, 'dt_matricula', 'dtMatriculaInicial', _GREATER_EQUAL, _TIMESTAMP) : params;
		params = $('dtMatriculaFinal').value != '' ? createItemComparator(params, 'dt_matricula', 'dtMatriculaFinal', _MINOR_EQUAL, _TIMESTAMP) : params;	
		// Sexo
		params = $('tpSexo').value >= 0 ? createItemComparator(params, 'E.tp_sexo', 'tpSexo') : params;
		// Estado Civil
		params = $('stEstadoCivil').value >= 0 ? createItemComparator(params, 'E.st_estado_civil', 'stEstadoCivil') : params;
		// Escolaridade
		params = $('cdEscolaridade').value > 0 ? createItemComparator(params, 'F.cd_escolaridade', 'cdEscolaridade') : params;
		// Tipo de Admissão
		params = $('cdTipoAdmissao').value > 0 ? createItemComparator(params, 'F.cd_tipo_admissao', 'cdTipoAdmissao') : params;
		// Vinculo Empregatício
		params = $('cdVinculoEmpregaticio').value > 0 ? createItemComparator(params, 'F.cd_vinculo_empregaticio', 'cdVinculoEmpregaticio') : params;
		// Função
		params = $('cdFuncao').value > 0 ? createItemComparator(params, 'F.cd_funcao', 'cdFuncao') : params;
		// Aniversário
		params = $('nrDiaAniversario').value > 0 ? createItemComparator(params, 'EXTRACT(DAY FROM dt_nascimento)', 'nrDiaAniversario', _EQUAL, _INTEGER) : params;
		params = params.replace('const EXTRACT(DAY FROM dt_nascimento)', 'fieldDiaAniversario');
		params = $('nrMesAniversario').value > 0 ? createItemComparator(params, 'EXTRACT(MONTH FROM dt_nascimento)', 'nrMesAniversario', _EQUAL, _INTEGER) : params;
		params = params.replace('const EXTRACT(MONTH FROM dt_nascimento)', 'fieldMesAniversario');
		// Setor
		params = $('cdSetor').value > 0 ? createItemComparator(params, 'F.cd_setor', 'cdSetor') : params;
		// Tabela de Horário
		params = $('cdTabelaHorario').value > 0 ? createItemComparator(params, 'F.cd_tabela_horario', 'cdTabelaHorario') : params;
		// Turma
		params = $('cdTurma').value > 0 ? createItemComparator(params, 'F.cd_turma', 'cdTurma') : params;
		// Tipo de Informação de Salário
		params = $('tpProventoPrincipal').value >= 0 ? createItemComparator(params, 'F.tp_provento_principal', 'tpProventoPrincipal') : params;
		// Tipo de Salário
		params = $('tpSalario').value >= 0 ? createItemComparator(params, 'F.tp_salario', 'tpSalario') : params;
		// Tabela de Salário
		params = $('cdTabelaSalario').value > 0 ? createItemComparator(params, 'F.cd_tabela_salario', 'cdTabelaSalario') : params;
		// Grupo de Pagamento
		params = $('cdGrupoPagamento').value > 0 ? createItemComparator(params, 'F.cd_grupo_pagamento', 'cdGrupoPagamento') : params;
		// Tipo de Pagamento
		params = $('tpPagamento').value >= 0 ? createItemComparator(params, 'F.tp_pagamento', 'tpPagamento') : params;
		// Vale Transporte
		params = $('lgValeTransporte').value >= 0 ? createItemComparator(params, 'F.lg_vale_transporte', 'lgValeTransporte') : params;
		// Agente Nocivo
		params = $('cdAgenteNocivo').value > 0 ? createItemComparator(params, 'F.cd_agente_nocivo', 'cdAgenteNocivo') : params;
		// Convênio
		params = $('cdConvenio').value > 0 ? createItemComparator(params, 'F.cd_convenio', 'cdConvenio') : params;
		// Período de Desligamento
		params = $('dtDesligamentoInicial').value != '' ? createItemComparator(params, 'dt_desligamento', 'dtDesligamentoInicial', _GREATER_EQUAL, _TIMESTAMP) : params;
		params = $('dtDesligamentoFinal').value != '' ? createItemComparator(params, 'dt_desligamento', 'dtDesligamentoFinal', _MINOR_EQUAL, _TIMESTAMP) : params;	
		//
		createTempbox("jMsg", {width: 165, height: 50, tempboxType: "LOADING", time: 0, message: "Aguarde... pesquisando!"});
		createGrid(null);
		// BUSCANDO
		setTimeout(function()	{
									getPage('POST', 'btnPesquisarOnClick', 
											'../methodcaller?className=com.tivic.manager.srh.DadosFuncionaisServices' +
											'&' + params +
											'&method=find(*crt:java.util.ArrayList,*groupBy:java.util.ArrayList)'+
											'&fieldDiaAniversario=EXTRACT(DAY FROM dt_nascimento)'+
											'&fieldMesAniversario=EXTRACT(MONTH FROM dt_nascimento)')
								}, 10);
	}
	else {	// retorno
		closeWindow('jMsg');
		rsmPessoa = eval("(" + content + ")");
		var qt = rsmPessoa.lines.length;
		$('labelResultado').innerHTML = (qt==0)?'Nenhuma colaborador encontrado':(qt==1)?'1 colaborador encontrado':qt+' colaboradores encontrados';
		gridColumnReport = ReportOne.createGridColumnReport({id: 'gridColumnReport', plotPlace: $('divGridColumnReport'), columnsGrid: columnsGrid, callLoadColumnsOptions: true});
		tab.showTab(1);
		createGrid(rsmPessoa);
	}
}

var columnsPessoa = [{label:'Nº Matrícula', reference: 'NR_MATRICULA'},
                     {label:'Nome do Colaborador', reference: 'NM_PESSOA'},
					 {label:'Sit.Func.', reference: 'CL_SITUACAO_FUNCIONAL'},
					 {label:'Admissão', reference: 'DT_MATRICULA', type: GridOne._DATE},							   
					 {label:'Setor', reference: 'NM_SETOR'},
					 {label:'Função', reference: 'NM_FUNCAO'},
					 {label:'Vínculo', reference: 'NM_VINCULO_EMPREGATICIO'},
					 {label:'Sexo', reference: 'CL_SEXO'},
					 {label:'Est.Civil', reference: 'CL_ESTADO_CIVIL'},
					 {label:'Nasc.', reference: 'DT_NASCIMENTO', type: GridOne._DATE},							   
					 {label:'RG', reference: 'CL_RG'},
					 {label:'CPF', reference: 'NR_CPF', type: GridOne._CPF},
					 {label:'Naturalidade', reference: 'NM_NATURALIDADE'}];

var columnsGrid = columnsPessoa;
function createGrid(rsm)	{
	ChartFactory.loadColumnsOptions(columnsGrid);
	
	gridPessoa = GridOne.create('gridPessoa', {columns: columnsGrid,
											   resultset: rsm,
											   plotPlace: $('divGridPessoa'),
											   onDoubleClick: function() {viewPessoa(); },
											   columnSeparator: true,
											   lineSeparator: false,
											   strippedLines: true,
											   onProcessRegister: function(reg){
											   		reg['CL_SITUACAO_FUNCIONAL'] = stFuncional[reg['ST_FUNCIONAL']];
											   		reg['CL_SEXO'] 				 = tpSexo[reg['TP_SEXO']];
											   		reg['CL_ESTADO_CIVIL'] 		 = stEstadoCivil[reg['ST_ESTADO_CIVIL']];
											   		reg['CL_RG'] = reg['NR_RG']+(reg['SG_ORGAO_RG']!=null?' '+reg['SG_ORGAO_RG']:'')+
											   		                (reg['SG_ESTADO_RG']!=null?'/'+reg['SG_ESTADO_RG']:'');
											   },		
											   noSelectOnCreate: false});
}

function btnFindCidadeOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Cidades", 
									   width: 600, height: 350, top: 10,
									   modal: true, noDrag: true,
									   className: "com.tivic.manager.grl.CidadeServices", method: "find",
									   allowFindAll: true,
									   filterFields: [[{label:"Cidade", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase'},
													   {label:"UF", reference:"SG_ESTADO", datatype:_VARCHAR, comparator:_EQUAL, width:10, charcase:'uppercase'},
													   {label:"ID", reference:"ID_CIDADE", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]],
									   gridOptions: {columns: [{label:"Cidade", reference:"NM_CIDADE"},{label:"UF", reference:"SG_ESTADO"}],
												     strippedLines: true,
												     columnSeparator: false,
												     lineSeparator: false},
									   callback: btnFindCidadeOnClick
									});        
    }
    else {// retorno
        closeWindow('jFiltro');
		$('cdCidade').value = reg[0]['CD_CIDADE'];
		$('nmCidade').value = reg[0]['NM_CIDADE']+' - '+reg[0]['SG_ESTADO'];
		getAllDistritosOfCidade(null);
    }
}

function btnFindBairroOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro", {caption:"Pesquisar Registros", 
									   width: 600, height: 350, top: 10,
									   modal: true, noDrag: true,
									   className: "com.tivic.manager.grl.BairroServices", method: "find",
									   filterFields: [[{label:"Nome Bairro", reference:"NM_BAIRRO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50},
									                   {label:"Cidade", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50}]],
									   gridOptions: {columns: [{label:"Nome", reference:"NM_BAIRRO"},{label:"Distrito", reference:"NM_DISTRITO"},
									                           {label:"Nome", reference:"NM_CIDADE"},{label:"UF", reference:"SG_ESTADO"}],
												   strippedLines: true,
												   columnSeparator: false,
												   lineSeparator: false},
									   callback: btnFindBairroOnClick});
    }
    else {// retorno
        closeWindow('jFiltro');
		$('cdBairro').value = reg[0]['CD_BAIRRO'];
		$('nmBairro').value = reg[0]['NM_BAIRRO'];
		$('cdCidade').value = reg[0]['CD_CIDADE'];
		$('nmCidade').value = reg[0]['NM_CIDADE']+' - '+reg[0]['SG_ESTADO'];
		getAllDistritosOfCidade(null);
    }
}

function btnFindLogradouroOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro", {caption:"Pesquisar Logradouros", 
									   width: 600, height: 350, top: 10,
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
										callback: btnFindLogradouroOnClick
										});
    }
    else {// retorno
        closeWindow('jFiltro');
		$('cdLogradouro').value = reg[0]['CD_LOGRADOURO'];
		$('nmLogradouro').value = reg[0]['SG_TIPO_LOGRADOURO']+ ' ' +reg[0]['NM_LOGRADOURO'];
		$('cdCidade').value = reg[0]['CD_CIDADE'];
		$('nmCidade').value = reg[0]['NM_CIDADE']+' - '+reg[0]['SG_ESTADO'];
		getAllDistritosOfCidade(null);
    }
}

function getAllDistritosOfCidade(content)	{
	if(content==null)	{
		$('cdDistrito').options.length = 1;
		if(document.getElementById('cdCidade').value>0)														
			setTimeout(function() {
						getPage("GET", "getAllDistritosOfCidade", 
								"../methodcaller?className=com.tivic.manager.grl.CidadeServices" +
								"&method=getAllDistritosOfCidade(const " + getValue('cdCidade') + ":int)", null, null, null, null)}, 10);
	}
	else	{
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		loadOptionsFromRsm($('cdDistrito'), rsm, {fieldValue: 'CD_DISTRITO', fieldText:'NM_DISTRITO'});
	}
}

function viewPessoa()	{
	if(gridPessoa.getSelectedRowRegister())	{
		parent.createWindow('jPessoa', {caption: 'Manutenção de Cadastro Geral', width: 700, height: 430, contentUrl: '../grl/pessoa.jsp?cdPessoa=' + gridPessoa.getSelectedRowRegister()['CD_PESSOA']});
	}
}

function btnImprimirOnClick(content) {
	if(!gridPessoa || !gridPessoa.options.resultset || gridPessoa.options.resultset.lines.length==0)	{
		alert('Não existe nenhum registro selecionado! Pesquise antes os registros que deseja imprimir.');
		return;
	}

	var urlLogo = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const <%=cdEmpresa%>:int)';

	var reportColumns = [];
	var reportGroups  = [];
	for(var i=0; i<gridColumnReport.size(); i++)	{
		var reg = gridColumnReport.getRegisterByIndex(i);
		if(reg['SHOW']==1)	{
			reportColumns.push({label: reg['LABEL'], reference: reg['REFERENCE'], type: reg['TYPE']});
		}
		if(reg['GROUP']==1)	{
			reportGroups.push({reference: reg['REFERENCE'], headerBand: {defaultText: reg['LABEL']+': #'+reg['REFERENCE']},
						       pageBreak: (reg['PAGEBRAKE']==1)});
		}
	}
		var band = $('titleBand').cloneNode(true);
		var register = {};
		register['CL_EMPRESA'] 	= getTextSelect('cdEmpresa', '', true);
		register['URL_LOGO'] 	= '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const ' + $('cdEmpresa').value + ':int)';
		var fields = ['cl_empresa','url_logo'];
		for (var i=0; fields!=null && i<fields.length; i++) {
			regExp = new RegExp('#'+fields[i].toUpperCase(), 'g');
			band.innerHTML = band.innerHTML.replace(regExp, register[fields[i].toUpperCase()]==null ? '' : register[fields[i].toUpperCase()]);
		}
	
	parent.ReportOne.create('jReportPessoa', {width: 700, height: 430, caption: 'Relação do Cadastro Geral',
									 resultset: gridPessoa.options.resultset,
									 pageHeaderBand: {contentModel: band},
									 detailBand: {columns: reportColumns,
												  displayColumnName: true,
												  displaySummary: true},
									 //groups: [{reference: 'DT_VENCIMENTO',
									 //		 headerBand: {defaultText: 'Data de vencimento: #CL_VENCIMENTO'},
									 //		 pageBreak: false}/*,
									 //		{reference: 'DS_TP_COMBUSTIVEL',
									 //		 headerBand: {defaultText: '&nbsp;&nbsp;#DS_TP_COMBUSTIVEL - GroupHeaderBand2'},
									 //		 //footerBand: {defaultText: '&nbsp;&nbsp;#DS_TP_COMBUSTIVEL - GroupFooterBand2'},
									 //		 pageBreak: false}*/],
									 pageFooterBand: {defaultText: 'Manager',
													  defaultInfo: 'Pág. #p de #P'},
									 groups: reportGroups,
									 orientation: $('orientacao').value,
									 paperType: $('papel').value,
									 /*tableLayout: 'fixed',*/
									 orderBy: [{reference:'NM_PESSOA', type:'ASC || DESC'}],
									 displayReferenceColumns: true});
}
</script>
</head>
<body class="body" onload="init();">
	<div style="width: 690px;" id="RelatorioPessoa" class="d1-form">
   		<div style="width: 680px; height: 402px;" class="d1-body">
			<div class="d1-toolBar" id="toolBarRelatorio" style="width:688px; height:24px; float:left; margin-top:4px;"></div>
        	<div class="element" id="divTabRelatorio" style="margin-top:3px;"></div>
			<input id="cdVinculo" name="cdVinculo" type="hidden" value="0" defaultValue="0">
          <div style="display:none;" id="divAbaFiltro">
	 		<div class="d1-line" id="line0">
				<div style="width: 350px;" class="element">
					<label class="caption" for="cdEmpresa">Vínculo com a Empresa:</label>
					<select style="width: 347px;" class="select" datatype="INT" id="cdEmpresa" name="cdEmpresa">
					<option value="0">Todas</option>
					</select>
				</div>
                <div class="element" style="width: 168px;">
                    <label for="stFuncional" class="caption">Situação Funcional</label>
                    <select style="width: 165px" type="text" name="stFuncional" id="stFuncional" class="select">
                        <option value="-1">Todas</option>
                    </select>
                </div>
                <div style="width:80px;" class="element">
                    <label for="dtMatriculaInicial" class="caption" style="overflow:visible;">Admitido entre:</label>
                    <input name="dtMatriculaInicial" type="text" class="field" id="dtMatriculaInicial" mask="##/##/####" maxlength="10" style="width:76px; " value=""/>
                    <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtMatriculaInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
                </div>
                <div style="width:80px;" class="element">
                    <label for="dtMatriculaFinal" class="caption">&nbsp;</label>
                    <input name="dtMatriculaFinal" type="text" class="field" id="dtMatriculaFinal" mask="##/##/####" maxlength="10" style="width:76px;" value=""/>
                    <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtMatriculaFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
                </div>
	 		</div>
			<div class="d1-line" id="line1">
		        <div style="width: 100px;" class="element">
		            <label class="caption" for="tpSexo">Sexo</label>
		            <select style="width: 97px;" class="select" datatype="STRING" id="tpSexo" name="tpSexo">
		              <option value="-1">Todos...</option>
		            </select>
		        </div>
		        <div style="width: 180px;" class="element">
		            <label class="caption" for="stEstadoCivil">Estado civil</label>
		            <select style="width: 177px;" class="select" datatype="STRING" id="stEstadoCivil" name="stEstadoCivil" defaultValue="0">
		              <option value="-1">Todos...</option>
		            </select>
		        </div>
		        <div style="width: 260px; height: 30px;" class="element">
		            <label class="caption" for="cdEscolaridade">Escolaridade</label>
		            <select style="width: 257px;" class="select" name="cdEscolaridade" id="cdEscolaridade">
		              <option value="0">Todas...</option>
		            </select>
		        </div>
                <div class="element" style="width:40px;">
                    <label for="nrDiaAniversario" class="caption" style="overflow:visible;">Aniversário: Dia / Mês</label>
                    <input style="width:37px; " type="text" class="field" name="nrDiaAniversario" id="nrDiaAniversario" value=""/>
                </div>
                <div style="width:100px;" class="element">
                    <label for="nrMesAniversario" class="caption">&nbsp;</label>
                    <select style="width: 97px;" class="select" datatype="INT" id="nrMesAniversario" name="nrMesAniversario" defaultValue="0">
                        <option value="-1">Selecione...</option>
                        <option value="1">Janeiro</option>
                        <option value="2">Fevereiro</option>
                        <option value="3">Março</option>
                        <option value="4">Abril</option>
                        <option value="5">Maio</option>
                        <option value="6">Junho</option>
                        <option value="7">Julho</option>
                        <option value="8">Agosto</option>
                        <option value="9">Setembro</option>
                        <option value="10">Outubro</option>
                        <option value="11">Novembro</option>
                        <option value="12">Dezembro</option>
                    </select>
                </div>
            </div>
            <div class="d1-line" id="line1">  
                <div style="width: 185px;" class="element">
	                <label class="caption" for="cdTipoAdmissao">Tipo de admissão</label>
	                <select style="width: 182px;" class="select" datatype="INT" id="cdTipoAdmissao" name="cdTipoAdmissao" defaultvalue="0">
	                  <option value="0">Todos...</option>
	                </select>
                </div>
                <div class="element" style="width:265px;">
	                <label class="caption" for="cdVinculoEmpregaticio">Vínculo empregatício</label>
	                <select style="width: 262px;" class="select" datatype="INT" id="cdVinculoEmpregaticio" name="cdVinculoEmpregaticio" defaultvalue="0">
	                  <option value="0">Todos...</option>
	                </select>
                </div>
              	<div style="width: 230px;" class="element">
                	<label class="caption" for="cdFuncao">Função</label>
                	<select style="width: 227px;" class="select" datatype="INT" id="cdFuncao" name="cdFuncao" defaultvalue="0">
                  		<option value="0">Todas...</option>
                	</select>
                </div>
            </div>
			<div class="d1-line" id="line1">
              	<div style="width: 340px;" class="element">
	                <label class="caption" for="cdSetor">Setor/Departamento (Local de trabalho)</label>
	                <input datatype="INT" id="cdSetor" name="cdSetor" type="hidden">
	                <input style="width: 337px;" static="true" disabled="disabled" class="disabledField" name="nmSetor" id="nmSetor" type="text">
	                <button title="Pesquisar valor para este campo..." class="controlButton controlButton2" onclick="btnFindSetorOnClick(null);"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
	                <button title="Limpar este campo..." class="controlButton" onclick="$('cdSetor').value=0; $('nmSetor').value='';"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
              	</div>
	            <div style="width: 170px;" class="element">
	                <label class="caption" for="cdTabelaHorario">Tabela de horário</label>
	                <select style="width: 167px;" class="select" datatype="INT" id="cdTabelaHorario" name="cdTabelaHorario" defaultvalue="0" onchange="if (!disabledFormPessoa) {$('nrHorasMes').value = this.options[this.selectedIndex].getAttribute('qtHorasMes');}">
	                  <option value="0" qtHorasMes="0">Todas...</option>
	                </select>
	            </div>
	            <div style="width: 170px;" class="element">
	                <label class="caption" for="cdTurma">Turma</label>
	                <select style="width: 167px;" class="select" datatype="INT" id="cdTurma" name="cdTurma" defaultvalue="0">
	                  <option value="0">Todas...</option>
	                </select>
	            </div>
            </div>
			<div class="d1-line" id="line1">
              <div style="width: 123px;" class="element">
                <label class="caption" for="tpProventoPrincipal">Informação salarial</label>
                <select style="width: 120px;" class="select" datatype="STRING" id="tpProventoPrincipal" name="tpProventoPrincipal" onblur="" defaultvalue="0">
                  <option value="-1">Todos...</option>
                </select>
              </div>
              <div style="width: 95px;" class="element">
                <label class="caption" for="tpSalario">Tipo de salário</label>
                <select style="width: 92px;" class="select" datatype="INT" id="tpSalario" name="tpSalario" defaultvalue="0">
                  <option value="-1">Todos...</option>
                </select>
              </div>
              <div style="width: 180px;" class="element">
                <label class="caption" for="cdTabelaSalario">Tabela de salário</label>
                <select style="width: 177px;" class="select" datatype="INT" id="cdTabelaSalario" name="cdTabelaSalario" defaultvalue="0">
                  <option value="0">Todas...</option>
                </select>
              </div>
              <div style="width: 158px;" class="element">
                <label class="caption" for="cdGrupoPagamento">Grupo de pagamento/lote</label>
                <select style="width: 155px;" class="select" datatype="INT" id="cdGrupoPagamento" name="cdGrupoPagamento" defaultvalue="0">
                  <option value="0">Todos...</option>
                </select>
              </div>
              <div style="width: 120px;" class="element">
                <label class="caption" for="tpPagamento">Forma de pagamento</label>
                <select style="width: 120px;" class="select" datatype="INT" id="tpPagamento" name="tpPagamento" defaultvalue="0">
                  <option value="-1">Todas...</option>
                </select>
              </div>
            </div>
            <div class="d1-line" id="line4" style="width:700px;">
	              <div style="width: 95px;" class="element">
		                <label class="caption" for="lgValeTransporte">Vale transporte</label>
		                <select style="width: 92px;" class="select" datatype="INT" id="lgValeTransporte" name="lgValeTransporte" defaultvalue="0">
			              	<option value="-1">Todos...</option>
		                </select>
	              </div>
	              <div style="width: 253px;" class="element">
		                <label class="caption" for="cdAgenteNocivo">Exposição a agente nocivo</label>
		                <select style="width: 250px;" class="select" datatype="FLOAT" id="cdAgenteNocivo" name="cdAgenteNocivo" defaultvalue="0">
		                  	<option value="0">Todas...</option>
		                </select>
	              </div>
	            <div style="width: 170px;" class="element">
	                <label class="caption" for="cdConvenio">Convênio/Fonte de recurso</label>
	                <select style="width: 167px;" class="select" datatype="INT" id="cdConvenio" name="cdConvenio" defaultvalue="0">
	                  	<option value="0">Todos...</option>
	                </select>
	            </div>
                <div style="width:80px;" class="element">
                    <label for="dtDesligamentoInicial" class="caption" style="overflow:visible;">Desligado entre:</label>
                    <input name="dtDesligamentoInicial" type="text" class="field" id="dtDesligamentoInicial" mask="##/##/####" maxlength="10" style="width:76px; " value=""/>
                    <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtDesligamentoInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
                </div>
                <div style="width:80px;" class="element">
                    <label for="dtDesligamentoFinal" class="caption">&nbsp;</label>
                    <input name="dtDesligamentoFinal" type="text" class="field" id="dtDesligamentoFinal" mask="##/##/####" maxlength="10" style="width:76px;" value=""/>
                    <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtDesligamentoFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
                </div>
            </div>
             <div class="d1-line" id="line4" style="width:700px;">
             	  <div id="divGridOpcaoRelatorio" style="width:295px; height:143px; border:1px solid #CCC; margin-top:2px; background-color:#FFF;" class="element"></div>
             	  <div id="divGridOpcaoAgrupamento" style="width:338px; height:143px; border:1px solid #CCC; margin:2px 0 0 3px; background-color:#FFF;" class="element"></div>
                  <div class="d1-toolBar" id="toolBarColunas" style="width:35px; _width:30px; height:144px; float:left; margin:2px 0 0 2px;"></div>
             </div>
        </div>
      </div> <!-- FINAL DA ABA FILTRO-->
	</div>


	<div id="titleBand" style="width:100%; display: none;">
	  <div style="width:100%; float:left; border-bottom:1px solid #000; border-top:1px solid #000;">
	    <div style="height:48px; border-bottom:1px solid #000000;">
	      <img id="imgLogo" style="height:38px; margin:5px; float:left" src="#URL_LOGO"/>
	        <div style="height:48px; border-left:1px solid #000000; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold">
	            &nbsp;Manager - M&oacute;dulo de Recursos Humanos<br/>
	            &nbsp;#CL_EMPRESA<br/>
	       		&nbsp;Relatório de Colaboradores<br/>
	            &nbsp;			
	        </div>
	    </div>
	</div>
</body>
</html>