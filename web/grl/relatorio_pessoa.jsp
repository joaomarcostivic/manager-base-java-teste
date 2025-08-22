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
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="aba2.0, shortcut, grid2.0, form, filter, calendario, report, flatbutton, toolbar, chart" compress="false"/>
<script language="javascript">
var rsmPessoas;
var tipoRegiao = <%=sol.util.Jso.getStream(com.tivic.manager.grl.RegiaoServices.tipoRegiao)%>;
var gridPessoa = null;
var gridColumnReport;
var registro;
var tab;
function init()	{
	var divAbaGrafico = ChartFactory.createChartForm({width: 685, height: 350, id: 'divAbaGrafico', gridName: 'gridPessoa'});
	var divAbaDados   = ReportOne.createFindResultForm({width: 685, height: 360, id: 'divAbaDados', columnsGridPlace: 'divGridColumnReport', resultGridPlace: 'divGridPessoa', reportTitle: 'Relatório de Pessoas'});
	$('RelatorioPessoa').appendChild(divAbaDados);
	$('RelatorioPessoa').appendChild(divAbaGrafico);
    
	enableTabEmulation();
	// TOOLBUTTON
	tab = TabOne.create('tabRelatorioPessoa',  {width: 690,height: 365, plotPlace: 'divTabRelatorio', tabPosition: ['top', 'left'],
											tabs: [{caption: 'Opções de Filtros e Configurações', reference:'divAbaFiltro', image: '/sol/imagens/filtro16.gif', active: true},
												   {caption: 'Resultado da Pesquisa', reference:'divAbaDados',image: '/sol/imagens/dados16.gif'},
												   {caption: 'Gráficos', reference:'divAbaGrafico', image: '/sol/imagens/grafico16.gif'}]});

	ToolBar.create('toolBar', {plotPlace: 'toolBarRelatorio', orientation: 'horizontal',
							   buttons: [{id: 'btnPesquisar', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar registros', onClick: btnPesquisarOnClick},
										 {id: 'btnImprimir', img: '/sol/imagens/print16.gif', label: 'Imprimir', title: 'Imprimir dados', onClick: btnImprimirOnClick},
									     {id: 'btExportar', img: '/sol/imagens/export16.gif', label: 'Exportar', onClick: btExportarOnClick},
										 {id: 'btVisualizar', img: '../adm/imagens/visualizar16.gif', label: 'Visualizar Cadastro', onClick: viewPessoa}]});

    var dtDataMask = new Mask($("dtCadastroInicial").getAttribute("mask"));
    dtDataMask.attach($("dtCadastroInicial"));
    dtDataMask.attach($("dtCadastroFinal"));

  	addShortcut('ctrl+d', function(){ if (!document.getElementById('btnDeletePessoaOnClick').disabled) btnDeletePessoaOnClick() });
  	addShortcut('ctrl+b', function(){ if (!document.getElementById('btnBaixaPessoaOnClick').disabled) btnBaixaPessoaOnClick() });
  	addShortcut('ctrl+k', function(){ if (!document.getElementById('btnCancelarPessoaOnClick').disabled) btnCancelarPessoaOnClick() });
	
	var rsmRegioes = <%=sol.util.Jso.getStream(com.tivic.manager.grl.RegiaoDAO.getAll())%>;
	$('cdRegiao').options.length = 1;
	for(var i=0; i<rsmRegioes.lines.length; i++)	{	
		switch(rsmRegioes.lines[i]['TP_REGIAO'])	{
			case 0:
				rsmRegioes.lines[i]['CL_REGIAO'] = 'Países do '+rsmRegioes.lines[i]['NM_REGIAO'];
				break;
			case 1:
				rsmRegioes.lines[i]['CL_REGIAO'] = 'Estados do '+rsmRegioes.lines[i]['NM_REGIAO'];
				break;
			case 2:
				rsmRegioes.lines[i]['CL_REGIAO'] = 'Cidades do(a) '+rsmRegioes.lines[i]['NM_REGIAO'];
				break;
			case 3:
				rsmRegioes.lines[i]['CL_REGIAO'] = 'Logradouros do(a) '+rsmRegioes.lines[i]['NM_REGIAO'];
				break;
		}
		var opt = new Option(rsmRegioes.lines[i]['CL_REGIAO'], rsmRegioes.lines[i]['CD_REGIAO'], false, false);
		opt.tpRegiao = rsmRegioes.lines[i]['TP_REGIAO'];
		$('cdRegiao').options[i+1] = opt;
	}
	loadOptionsFromRsm($('cdEstado'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EstadoServices.find(new java.util.ArrayList<sol.dao.ItemComparator>()))%>, {fieldValue: 'CD_ESTADO', fieldText:'SG_ESTADO'});
    loadOptionsFromRsm($('cdEmpresa'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
    loadOptionsFromRsm($('cdVinculo'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.VinculoDAO.getAll())%>, {fieldValue: 'cd_vinculo', fieldText:'nm_vinculo'});
	loadOptionsFromRsm($('cdFormaDivulgacao'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.FormaDivulgacaoDAO.getAll())%>, {fieldValue: 'CD_FORMA_DIVULGACAO', fieldText:'NM_FORMA_DIVULGACAO'});
	
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
	rsmColumns.lines.push({LABEL:'Empresa', GROUPBY:'N1.NM_PESSOA', DATATYPE: _VARCHAR, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Vínculo', GROUPBY:'NM_VINCULO', DATATYPE: _VARCHAR, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Nome da Pessoa (Física/Jurídica)', GROUPBY:'A.NM_PESSOA', DATATYPE: _VARCHAR, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Como nos conheceu', GROUPBY:'NM_FORMA_DIVULGACAO', DATATYPE: _VARCHAR, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Endereço: Região do País', GROUPBY:'O.cd_regiao, O1.nm_regiao', DATATYPE: _INTEGER, SELECAO: 0, ALIAS: 'nm_regiao_pais'});
	rsmColumns.lines.push({LABEL:'Endereço: Região do Estado', GROUPBY:'M.cd_regiao, O2.nm_regiao', DATATYPE: _INTEGER, SELECAO: 0, ALIAS: 'nm_regiao_estado'});
	rsmColumns.lines.push({LABEL:'Endereço: Região da Cidade', GROUPBY:'G.cd_regiao, O3.nm_regiao', DATATYPE: _INTEGER, SELECAO: 0, ALIAS: 'nm_regiao_cidade'});
	rsmColumns.lines.push({LABEL:'Endereço: Estado', GROUPBY:'NM_ESTADO', DATATYPE: _VARCHAR, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Endereço: Cidade', GROUPBY:'NM_CIDADE', DATATYPE: _VARCHAR, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Endereço: Bairro', GROUPBY:'H.NM_BAIRRO', DATATYPE: _VARCHAR, SELECAO: 0});
	rsmColumns.lines.push({LABEL:'Endereço: Logradouro', GROUPBY:'I.NM_LOGRADOURO', DATATYPE: _VARCHAR, SELECAO: 0});
	
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
		$('labelResultado').innerHTML = 'Resultados da Pesquisa';
		var objects = 'crt=java.util.ArrayList();groupBy=java.util.ArrayList();';
		var execute = '';
		columnsGrid = [];
		var fieldsPost = [];
		for(var i=0; i<gridOpcaoAgrupamento.size(); i++)	{
			var reg = gridOpcaoAgrupamento.getRegisterByIndex(i);
			if(reg['SELECAO']==1)	{
				var colName = reg['REFERENCE'] ? reg['REFERENCE'] : (reg['ALIAS'] ? reg['ALIAS'] : reg['GROUPBY']);
				if(colName.indexOf('.')>=0)	{
					colName = colName.substring(colName.indexOf('.')+1);
				}
				columnsGrid.push({reference: colName, label: reg['LABEL']});
				var column = reg['ALIAS'] ? reg['GROUPBY']+' AS '+reg['ALIAS'] : reg['GROUPBY'];
				//objects += 'fields'+i+'=const ' + column + ':String;';
				objects += 'cfields'+i+'=fields' + i + ':String;';
				execute += 'groupBy.add(*cfields' + i + ':Object);';
				fieldsPost.push(createInputElement('hidden', 'fields'+i, column));
			}
		}
		if(columnsGrid.length == 0)	{
			columnsGrid = columnsPessoa;
		}
		else	{
			columnsGrid.push({label: 'Quantidade', reference:'QT_PESSOA'});
		}
		var params = objects + '&' + execute;
		// Empresa
		params = $('cdEmpresa').value > 0 ? createItemComparator(params, 'D.cd_empresa', 'cdEmpresa') : params;
		// Vínculo
		params = $('cdVinculo').value > 0 ? createItemComparator(params, 'D.cd_vinculo', 'cdVinculo') : params;
		// Período de Cadastro
		params = $('dtCadastroInicial').value != '' ? createItemComparator(params, 'A.dt_cadastro', 'dtCadastroInicial', _GREATER_EQUAL, _TIMESTAMP) : params;
		params = $('dtCadastroFinal').value != '' ? createItemComparator(params, 'A.dt_cadastro', 'dtCadastroFinal', _MINOR_EQUAL, _TIMESTAMP) : params;	
		// Situação
		params = $('stPessoa').value >= 0 ? createItemComparator(params, 'st_vinculo', 'stPessoa') : params;
		// Tipo (Jurídica / Física)
		params = $('gnPessoa').value >= 0 ? createItemComparator(params, 'A.gn_pessoa', 'gnPessoa') : params;
		// LINHA 2
		// Porte da Empresa
		params = $('lgEnderecoPrincipal').checked ? createItemComparator(params, 'lgEnderecoPrincipal', 'lgEnderecoPrincipal') : params;
		params = $('tpEmpresa').value >= 0 ? createItemComparator(params, 'B.tp_empresa', 'tpEmpresa') : params;
		// Como nos conheceu
		params = $('cdFormaDivulgacao').value > 0 ? createItemComparator(params, 'A.cd_forma_divulgacao', 'cdFormaDivulgacao') : params;
		// Aniversário
		params = $('nrDiaAniversario').value > 0 ? createItemComparator(params, 'EXTRACT(DAY FROM dt_nascimento)', 'nrDiaAniversario', _EQUAL, _INTEGER) : params;
		params = params.replace('const EXTRACT(DAY FROM dt_nascimento)', 'fieldDiaAniversario');
		params = $('nrMesAniversario').value > 0 ? createItemComparator(params, 'EXTRACT(MONTH FROM dt_nascimento)', 'nrMesAniversario', _EQUAL, _INTEGER) : params;
		params = params.replace('const EXTRACT(MONTH FROM dt_nascimento)', 'fieldMesAniversario');
		// Região
		var fields = ['O.cd_regiao', 'M.cd_regiao', 'G.cd_regiao', 'I.cd_regiao'];
		params = $('cdRegiao').value > 0 ? createItemComparator(params, fields[$('cdRegiao').options[$('cdRegiao').selectedIndex].tpRegiao], 'cdRegiao') : params;
		// Estado
		params = $('cdEstado').value > 0 ? createItemComparator(params, 'G.cd_estado', 'cdEstado') : params;
		// Cidade
		params = $('cdCidade').value > 0 ? createItemComparator(params, 'F.cd_cidade', 'cdCidade') : params;
		// Distrito
		params = $('cdDistrito').value > 0 ? createItemComparator(params, 'I.cd_distrito', 'cdDistrito') : params;
		// Bairro
		params = $('cdBairro').value > 0 ? createItemComparator(params, 'F.cd_bairro', 'cdBairro') : params;
		// Logradouro
		params = $('cdLogradouro').value > 0 ? createItemComparator(params, 'F.cd_logradouro', 'cdLogradouro') : params;
		// LINHA 3
		createTempbox("jMsg", {width: 165, height: 50, tempboxType: "LOADING", time: 0, message: "Aguarde... pesquisando!"});
		createGrid(null);
		// BUSCANDO
		var parts = params.split('&');
		fieldsPost.push(createInputElement('hidden', 'objects', parts[0]));
		fieldsPost.push(createInputElement('hidden', 'execute', parts[1]));
		setTimeout(function() { getPage('POST', 'btnPesquisarOnClick', 
				'../methodcaller?className=com.tivic.manager.grl.PessoaServices' +
				'&method=findPessoaEmpresaAndEndereco(*crt:java.util.ArrayList,*groupBy:java.util.ArrayList)'+
				'&fieldDiaAniversario=EXTRACT(DAY FROM dt_nascimento)'+
				'&fieldMesAniversario=EXTRACT(MONTH FROM dt_nascimento)', fieldsPost) }, 10);
	}
	else {	// retorno
		closeWindow('jMsg');
		rsmPessoas = null;
		try { rsmPessoas = eval("(" + content + ")"); } catch(e) {}
		rsmPessoas = rsmPessoas!=null ? rsmPessoas : {lines: []};
		var qt = rsmPessoas.lines.length;
		$('labelResultado').innerHTML = (qt==0)?'Nenhuma pessoa encontrada':(qt==1)?'1 pessoa encontrada':qt+' pessoas encontradas';
		gridColumnReport = ReportOne.createGridColumnReport({id: 'gridColumnReport', plotPlace: $('divGridColumnReport'), columnsGrid: columnsGrid, callLoadColumnsOptions: true});
		tab.showTab(1);
		createGrid(rsmPessoas);
	}
}

var columnsPessoa = [{label:'Nome', reference: 'NM_PESSOA'},
					 {label:'Razão Social', reference: 'nm_razao_social'},
					 {label:'CNPJ/CPF', reference: 'CL_NR_CNPJ_CPF'},	
					 {label:'Nº Inscrição Estadual', reference: 'nr_inscricao_estadual'},
					 {label:'Aniversário', reference: 'DT_NASCIMENTO', type: GridOne._DATE},							   
					 {label:'Endereço', reference: 'CL_ENDERECO'},
					 {label:'CEP', reference: 'NR_CEP'},
					 {label:'Cidade', reference: 'NM_CIDADE'},
					 {label:'País de origem', reference: 'nm_pais'},
					 {label:'Telefones', reference: 'CL_TELEFONE'},
					 {label:'Celular', reference: 'NR_CELULAR'},
					 {label:'Vinculo', reference: 'NM_VINCULO'},
					 {label:'Observação', reference: 'TXT_OBSERVACAO'}];

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
											   		reg['CL_PESSOA'] = reg['GN_PESSOA']==0 ? 'J' : 'F';
											   		var nmCidade = reg['NM_CIDADE'];
											   		var sgEstado = reg['SG_ESTADO'];
											   		reg['NM_CIDADE'] = null;
											   		reg['SG_ESTADO'] = null; 
													reg['CL_NR_CNPJ_CPF'] = reg['GN_PESSOA']===0 ? reg['NR_CNPJ'] : reg['NR_CPF'];
											   		reg['CL_ENDERECO'] = reg['CL_ENDERECO'] ? reg['CL_ENDERECO'] : getFormatedAddress(reg);
													reg['CL_TELEFONE'] = '';
													if (reg['NR_TELEFONE']!=null && trim(reg['NR_TELEFONE'])!='')
														reg['CL_TELEFONE'] = reg['NR_TELEFONE'];
													else {
														reg['CL_TELEFONE'] += reg['NR_TELEFONE1']!=null && trim(reg['NR_TELEFONE1'])!='' ? (reg['CL_TELEFONE']=='' ? '' : '; ') + trim(reg['NR_TELEFONE1']) : '';
														reg['CL_TELEFONE'] += reg['NR_TELEFONE2']!=null && trim(reg['NR_TELEFONE2'])!='' ? (reg['CL_TELEFONE']=='' ? '' : '; ') + trim(reg['NR_TELEFONE2']) : '';
													}
											   		reg['NM_CIDADE'] = nmCidade+(sgEstado!=null ? '-'+sgEstado : '');
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

	var objects    = "crt=java.util.ArrayList();groups=java.util.ArrayList();order=java.util.ArrayList();";
	var execute    = "";
	
	/* CONDIÇÃO DE GERAR O RELATÓRIO
	 * Verifica de há uma pessoa selecionada, caso houver é adicionado um critério no relatório para que seja gerada as informações apenas
	 * dessa pessoa, caso o contrário, gera de todas as pessoas apresentadas no Grid.
	*/
	
	var question = confirm("Deseja imprimir apenas o resultado selecionado?");
	    var cdPessoa;
	if (question === true) {
		cdPessoa = gridPessoa.getSelectedRowRegister()['CD_PESSOA'];
	} else {
		cdPessoa = null;
	}
	
	if (cdPessoa != null) {
		objects   += 'cdPessoa=sol.dao.ItemComparator(const A.cd_pessoa:String,const '+ cdPessoa +':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*cdPessoa:Object);';
	}
	
	caption    = "Relatório de Clientes";
	className  = "com.tivic.manager.grl.PessoaServices";
	method     = "gerarRelatorioCliente(*crt:java.util.ArrayList)";
	nomeJasper = "relatorio_cliente_detalhado";	
        
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
					 "&objects=" + objects +
					 "&execute=" + execute +
					 "&cdEmpresa=" 	+ $('cdEmpresa').value + 
// 					 "&p=dtSaidaInicial|" + $('dtSaidaInicial').value + "-=-dtSaidaFinal|" + $('dtSaidaFinal').value + "-=-nmTurnos|" + nmTurnos + "-=-nmCargo|" + nmCargo + "-=-nmPessoa|" + nmPessoa +
					 "&modulo=pcb"});
	
	
	
// 	if(!gridPessoa || !gridPessoa.options.resultset || gridPessoa.options.resultset.lines.length==0)	{
// 		alert('Não existe nenhum registro selecionado! Pesquise antes os registros que deseja imprimir.');
// 		return;
// 	}

<%-- 	var urlLogo = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const <%=cdEmpresa%>:int)'; --%>

// 	var reportColumns = [];
// 	var reportGroups  = [];
// 	for(var i=0; i<gridColumnReport.size(); i++)	{
// 		var reg = gridColumnReport.getRegisterByIndex(i);
// 		if(reg['SHOW']==1)	{
// 			reportColumns.push({label: reg['LABEL'], reference: reg['REFERENCE'], type: reg['TYPE']});
// 		}
// 		if(reg['GROUP']==1)	{
// 			reportGroups.push({reference: reg['REFERENCE'], headerBand: {defaultText: reg['LABEL']+': #'+reg['REFERENCE']},
// 						       pageBreak: (reg['PAGEBRAKE']==1)});
// 		}
// 	}
	
// 	parent.ReportOne.create('jReportPessoa', {width: 700,
// 									 height: 430,
// 									 caption: 'Relação do Cadastro Geral',
// 									 resultset: gridPessoa.options.resultset,
// 									 /*titleBand: {defaultImage: urlLogo,
// 												   defaultTitle: 'TitleBand',
// 											       defaultInfo: 'Pág. #p de #P<br/>#d/#M/#y #h:#m:#s'},*/
// 									 pageHeaderBand: {defaultImage: urlLogo,
// 													  defaultTitle: 'Relação do Cadastro Geral',
// 													  defaultInfo: 'Pág. #p de #P'},
// 									 detailBand: {columns: reportColumns,
// 												  displayColumnName: true,
// 												  displaySummary: true},
// 									 //groups: [{reference: 'DT_VENCIMENTO',
// 									 //		 headerBand: {defaultText: 'Data de vencimento: #CL_VENCIMENTO'},
// 									 //		 pageBreak: false}/*,
// 									 //		{reference: 'DS_TP_COMBUSTIVEL',
// 									 //		 headerBand: {defaultText: '&nbsp;&nbsp;#DS_TP_COMBUSTIVEL - GroupHeaderBand2'},
// 									 //		 //footerBand: {defaultText: '&nbsp;&nbsp;#DS_TP_COMBUSTIVEL - GroupFooterBand2'},
// 									 //		 pageBreak: false}*/],
// 									 pageFooterBand: {defaultText: 'Manager',
// 													  defaultInfo: 'Pág. #p de #P'},
// 									 groups: reportGroups,
// 									 orientation: $('orientacao').value,
// 									 paperType: $('papel').value,
// 									 /*tableLayout: 'fixed',*/
// 									 orderBy: [{reference:'NM_PESSOA', type:'ASC || DESC'}],
// 									 displayReferenceColumns: true});
// 	*/
}
</script>
</head>
<body class="body" onload="init();">
	<div style="width: 690px;" id="RelatorioPessoa" class="d1-form">
   		<div style="width: 680px; height: 402px;" class="d1-body">
			<div class="d1-toolBar" id="toolBarRelatorio" style="width:688px; height:24px; float:left; margin-top:4px;"></div>
        	<div class="element" id="divTabRelatorio" style="margin-top:3px;"></div>
			<input idform="" reference="cd_conta" id="cdConta" name="cdConta" type="hidden" value="0" defaultValue="0">
			<input idform="" reference="cd_movimento_conta" id="cdMovimentoConta" name="cdMovimentoConta" type="hidden" value="0" defaultValue="0">
          <div style="display:<%=1==1?"none":""%>;" id="divAbaFiltro">
	 		<div class="d1-line" id="line0">
				<div style="width: 220px;" class="element">
					<label class="caption" for="cdEmpresa">Vínculo com a Empresa:</label>
					<select style="width: 217px;" class="select" idform="ContaPagar" reference="cd_empresa" datatype="INT" id="cdEmpresa" name="cdEmpresa">
					<option value="0">Todas</option>
					</select>
				</div>
                <div class="element" style="width:170px;">
                    <label for="cdVinculo" class="caption">Vínculo</label>
                    <select style="width: 167px" type="text" name="cdVinculo" id="cdVinculo" class="select" >
                        <option value="0">Todos</option>
                    </select>
                </div>
                <div style="width:80px;" class="element">
                    <label for="dtCadastroInicial" class="caption" style="overflow:visible;">Período de Cadastro</label>
                    <input name="dtCadastroInicial" type="text" class="field" id="dtCadastroInicial" mask="##/##/####" maxlength="10" style="width:76px; " value=""/>
                    <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtCadastroInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
                </div>
                <div style="width:80px;" class="element">
                    <label for="dtCadastroFinal" class="caption">&nbsp;</label>
                    <input name="dtCadastroFinal" type="text" class="field" id="dtCadastroFinal" mask="##/##/####" maxlength="10" style="width:76px;" value=""/>
                    <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtCadastroFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
                </div>
                <div class="element" style="width: 60px;">
                    <label for="stPessoa" class="caption">Situação</label>
                    <select style="width: 57px" type="text" name="stPessoa" id="stPessoa" class="select">
                        <option value="-1">Todas</option>
                        <option value="0">Inativas</option>
                        <option value="1" selected="selected">Ativas</option>
                    </select>
                </div>
                <div class="element" style="width: 70px;">
                    <label for="stPessoa" class="caption">Física/Jurídica</label>
                    <select style="width: 68px" type="text" name="gnPessoa" id="gnPessoa" class="select" >
                        <option value="-1" selected="selected">Todas</option>
                        <option value="0">Jurídicas</option>
                        <option value="1">Física</option>
                    </select>
                </div>
	 		</div>
            <div class="d1-line" id="line1">  
                <div style="width: 110px;" class="element">
                    <label class="caption" for="tpEmpresa">Porte da Empresa</label>
                    <select style="width: 107px;" class="select" datatype="INT" id="tpEmpresa" name="tpEmpresa" defaultValue="-1">
                        <option value="-1">Selecione...</option>
                        <option value="0">Micro Empresa</option>
                        <option value="1">Pequeno Porte</option>
                        <option value="2">Médio ou Grande</option>
                    </select>
                </div>
                <div style="width: 110px;" class="element">
                    <label class="caption" for="cdFormaDivulgacao">Como nos conheceu</label>
                    <select style="width: 107px;" class="select" datatype="INT" id="cdFormaDivulgacao" name="cdFormaDivulgacao" defaultValue="0">
                        <option value="0">Selecione...</option>
                    </select>
                </div>
                <div class="element" style="width:30px;">
                    <label for="nrDiaAniversario" class="caption" style="overflow:visible;">Aniversário: Dia / Mês</label>
                    <input style="width:27px; " type="text" class="field" name="nrDiaAniversario" id="nrDiaAniversario" value=""/>
                </div>
                <div style="width:85px;" class="element">
                    <label for="nrMesAniversario" class="caption">&nbsp;</label>
                    <select style="width: 82px;" class="select" datatype="INT" id="nrMesAniversario" name="nrMesAniversario" defaultValue="0">
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
                <div class="element" style="width: 185px;">
                    <label for="cdRegiao" class="caption">Região (do país, do estado, etc...)</label>
                    <select style="width:182px" type="text" name="cdRegiao" id="cdRegiao" class="select" defaultValue="0">
                        <option value="0">Selecione ...</option>
                    </select>
                </div>
                <div style="width: 45px;" class="element">
                    <label class="caption" for="cdEstado">Estado</label>
                    <select style="width: 43px" type="text" name="cdEstado" id="cdEstado" class="select" defaultValue="BA">
                        <option value="0">...</option>
                    </select>
                </div>
                <div id="" style="width: 20px;" class="element">
                  <label class="caption" for="lgEnderecoPrincipal">&nbsp;</label>
                  <input name="lgEnderecoPrincipal" type="checkbox" id="lgEnderecoPrincipal" value="1" checked="checked">
                </div>
              <div id="" style="width: 95px;" class="element">
                <label class="caption">&nbsp;</label>
                <label style="margin:2px 0px 0px 0px" class="caption">Endere&ccedil;o principal</label>
              </div>
            </div>
			<div class="d1-line" id="line1">
                <div style="width: 195px;" class="element">
                    <label class="caption" for="cdCidade">Cidade</label>
                    <input datatype="INT" id="cdCidade" name="cdCidade" type="hidden"/>
                    <input style="width: 192px;" static="true" disabled="disabled" class="disabledField" name="nmCidade" id="nmCidade" type="text"/>
                    <button id="btnClearCidade" title="Limpar este campo..." onclick="document.getElementById('cdCidade').value=0; document.getElementById('nmCidade').value=''; $('cdDistrito').options.length = 1;" class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
                    <button id="btnFindCidade" onclick="btnFindCidadeOnClick()" title="Pesquisar valor para este campo..." idform="Bairro" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                </div>
                <div style="width: 130px;" class="element">
                    <label class="caption" for="cdDistrito">Distrito</label>
                    <select style="width: 127px" type="text" name="cdDistrito" id="cdDistrito" class="select" idform="Cidade" reference="cd_estado" defaultValue="BA">
                        <option value="0">Selecione ...</option>
                    </select>
                </div>
                <div style="width: 160px;" class="element">
                    <label class="caption" for="cdBairro">Bairro</label>
                    <input datatype="INT" id="cdBairro" name="cdBairro" type="hidden"/>
                    <input style="width: 157px;" static="true" disabled="disabled" class="disabledField" name="nmBairro" id="nmBairro" type="text"/>
                    <button id="btnCleaBairro" title="Limpar este campo..." onclick="document.getElementById('cdBairro').value=0; document.getElementById('nmBairro').value='';" class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
                    <button id="btnFindBairro" onclick="btnFindBairroOnClick()" title="Pesquisar valor para este campo..." idform="Bairro" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                </div>
                <div style="width: 195px;" class="element">
                    <label class="caption" for="cdLogradouro">Logradouro</label>
                    <input datatype="INT" id="cdLogradouro" name="cdLogradouro" type="hidden"/>
                    <input style="width: 192px;" static="true" disabled="disabled" class="disabledField" name="nmLogradouro" id="nmLogradouro" type="text"/>
                    <button id="btnClearCidade" title="Limpar este campo..." onclick="document.getElementById('cdLogradouro').value=0; document.getElementById('nmLogradouro').value='';" class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
                    <button id="btnFindCidade" onclick="btnFindLogradouroOnClick()" title="Pesquisar valor para este campo..." idform="Bairro" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
                </div>
            </div>
             <div class="d1-line" id="line4" style="width:700px;">
                <div style="" class="element">
                  <label class="caption" for="divGridOpcaoAgrupamento">Op&ccedil;&otilde;es de agrupamento</label>
                </div>
             </div>
             <div class="d1-line" id="line4" style="width:700px;">
             	  <div id="divGridOpcaoAgrupamento" style="width:640px; height:230px; border:1px solid #CCC; background-color:#FFF;" class="element"></div>
                  <div class="d1-toolBar" id="toolBarColunas" style="width:35px; _width:30px; height:230px; float:left; margin:2px 0 0 2px;"></div>
             </div>
        </div>
      </div> <!-- FINAL DA ABA FILTRO-->
	</div>
    <div id="sumaryPessoa" style="display:<%=1==1?"none":""%>; width:_PAGE_WIDTH">
        <table width="_PAGE_WIDTH" border="0" cellspacing="2" cellpadding="2" style="border-top:2px solid #000000">
          <tr style="font-size:11px; font-family:Geneva, Arial, Helvetica, sans-serif;">
            <td width="25%" nowrap="nowrap" ><strong>Total a receber:</strong></td>
            <td width="25%" align="right" nowrap="nowrap" style="font-size:11px">#VL_CONTA</td>
            <td width="25%" nowrap="nowrap"><strong>Total recebido:</strong></td>
            <td width="25%" align="right" style="font-size:11px" nowrap="nowrap">#VL_RECEBIDO</td>
          </tr>
          <tr style="font-size:11px; font-family:Geneva, Arial, Helvetica, sans-serif;">
            <td width="25%" nowrap="nowrap" ><strong>Total acréscimos:</strong></td>
            <td width="25%" align="right" nowrap="nowrap" style="font-size:11px">#VL_ACRESCIMO</td>
            <td width="25%" nowrap="nowrap"><strong>Total descontos:</strong></td>
            <td width="25%" align="right" style="font-size:11px" nowrap="nowrap">#VL_ABATIMENTO</td>
          </tr>
        </table>
    </div>
</body>
</html>