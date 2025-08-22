<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.grl.ParametroServices"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="com.tivic.manager.grl.ProdutoServicoEmpresaServices"%>
<%@page import="com.tivic.manager.alm.LocalArmazenamentoServices"%>
<%@page import="sol.util.Jso"%>
<%@page import="java.sql.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<loader:library
	libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, report, shortcut, report, jquery, util"
	compress="false" />
<%
	try {
		String today           = sol.util.Util.formatDateTime(new java.util.GregorianCalendar(), "dd/MM/yyyy");
		int cdEmpresa          = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
		int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
		int cdVinculoFornecedor = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_FORNECEDOR", 0);
		
%>
<script language="javascript">
$.noConflict();
function init()	{
	ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'vertical',
										 buttons: [
												   {id: 'btnEmail', img: '../ptc/imagens/enviar_documento24.gif', label: 'Email', onClick: btnImprimirOnClick, imagePosition: 'left', width: 80},
												   {id: 'btnShow', img: '../ptc/imagens/documento24.gif', label: 'Visualizar', title: 'Abrir Documento... [Ctrl + A]', onClick: btnVisualizarOnClick, imagePosition: 'left', width: 80, height:50},
												   {id: 'btnPrint', img: '/sol/imagens/print24.gif', label: 'Imprimir', onClick: btnImprimirOnClick, imagePosition: 'left', width: 80},
										           {id: 'btnClose', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Fechar', title: 'Fechar', onClick: btnFecharOnClick, imagePosition: 'left', width: 80, heigth:50} 
												   ]});

	var dataMask = new Mask($("dtMovimentoInicio").getAttribute("mask"));
    dataMask.attach($("dtMovimentoInicio"));
	var dataMask2 = new Mask($("dtMovimentoFim").getAttribute("mask"));
    dataMask2.attach($("dtMovimentoFim"));
    createCategoriasOptions();
	createLocaisEstoqueOptions();
	loadOptions($('tpControleEstoque'), <%=Jso.getStream(ProdutoServicoEmpresaServices.tiposControleEstoque)%>);
	$('cdEmpresa').value = '<%=cdEmpresa%>';
	loadTabelasPrecosOnChange();
	
	jQuery('input[name=situacao]').first().attr('checked',true);
	jQuery('input[name=ordemImpressao]').first().attr('checked',true);
}

function loadTabelasPrecosOnChange(content) {
	if (content==null) {
		getPage('GET', 'loadTabelasPrecosOnChange', 
				'../methodcaller?className=com.tivic.manager.adm.TabelaPrecoServices'+
				'&method=getAll(cdEmpresa:int):sol.dao.ResultSetMap' +
				'&cdEmpresa=' + $('cdEmpresa').value);
	}
	else {
		var rsmTabelasPreco = null;
		try {rsmTabelasPreco = eval('(' + content + ')')} catch(e) {}
		loadOptionsFromRsm($('cdTabelaPreco'), rsmTabelasPreco, {fieldText: 'nm_tabela_preco', fieldValue:'cd_tabela_preco', beforeClear: true, 
																 optNotSelect: {value: -1, text: 'Qualquer'}});
	}
}

function changeTipoRelatorio(){
	var tpRelatorio =  $('tpRelatorio').value != null ?  $('tpRelatorio').value : 0; 
    /*
	* 0 - Consolidação de produto
	* 1 - Consolidação de combustivel
	*
	*/
    if(tpRelatorio == 0){
		jQuery("#divOrdemImpressao").show();
    } else{
		jQuery("#divOrdemImpressao").hide();
    }    	
	createCategoriasOptions();
	createLocaisEstoqueOptions();
}

function btnFecharOnClick(content) {
}

function btnPesquisarOnClick(content) {
}

function btnVisualizarOnClick()	{
	var caption;
	var className;
	var method; 
	var nomeJasper;	
	var execute;
	var objects;
	var params = '';
	var listaSubReports;
	execute = '';
	objects = 'crt=java.util.ArrayList(); orderBy=java.util.ArrayList();';
	caption    = "Consolidação de Estoque de Produtos";
	className  = "com.tivic.manager.alm.ProdutoEstoqueServices";
	listaSubReports = "";
    var tpRelatorio =  $('tpRelatorio').value != null ?  $('tpRelatorio').value : 0; 
    /*
	* 0 - Consolidação de produto
	* 1 - Consolidação de combustivel
	*
	*/
    if(tpRelatorio == 0){
		method     = "gerarRelatorioConsolicaoEstoqueProduto(const "+$('cdEmpresa').value+":int, *crt:java.util.ArrayList())";
	    nomeJasper = "consolidacao_estoque_produto";
    }else{
		method     = "gerarRelatorioConsolicaoEstoqueCombustivel(const "+$('cdEmpresa').value+":int, *crt:java.util.ArrayList())";
	    nomeJasper = "consolidacao_estoque_combustivel";
    }
    
	var arLocalArmazenamento = new Array();
	var arLocalArmazenamentoNome = new Array();
	var arCategoria = new Array();
	var arCategoriaNome = new Array();
	var arSubCategoria = new Array();
	var arSubCategoriaNome = new Array();
	for( var i=0; i < rsmOptionsLocaisEstoque.lines.length;i++ ){
		if( rsmOptionsLocaisEstoque.lines[i].CHECKED == 1 ){
			arLocalArmazenamento.push( rsmOptionsLocaisEstoque.lines[i].CD_GRUPO );		
			arLocalArmazenamentoNome.push( rsmOptionsLocaisEstoque.lines[i].LABEL );		
		}
	}
	for( var i=0; i < rsmOptionsCategoria.lines.length;i++ ){
		if( rsmOptionsCategoria.lines[i].CHECKED == 1 ){
			arCategoria.push( rsmOptionsCategoria.lines[i].CD_GRUPO );		
			arCategoriaNome.push( rsmOptionsCategoria.lines[i].LABEL );		
		}
	}
	for( var i=0; i < rsmColumnsSubCatProdutos.lines.length;i++ ){
		if( rsmColumnsSubCatProdutos.lines[i].CHECKED == 1 ){
			arSubCategoria.push( rsmColumnsSubCatProdutos.lines[i].CD_GRUPO );
			arSubCategoriaNome.push( rsmColumnsSubCatProdutos.lines[i].LABEL );		
		}
	}
	if( arLocalArmazenamento.length > 0 ){
		objects += 'localArmaz=sol.dao.ItemComparator(const cdLocalArmazenamento:String,const '+arLocalArmazenamento.join('|')+':String, const '+_IN+':int, const '+_INTEGER+':int);';
		execute += 'crt.add(*localArmaz:Object);';
	}
	if( arLocalArmazenamentoNome.length > 0 ){
		objects += 'localArmazNm=sol.dao.ItemComparator(const nmLocalArmazenamento:String,const '+arLocalArmazenamentoNome.join('|')+':String, const '+_IN+':int, const '+_INTEGER+':int);';
		execute += 'crt.add(*localArmazNm:Object);';
	}
	// Adiciona Categoria
	if( arCategoria.length > 0 ){
		objects += 'cat=sol.dao.ItemComparator(const cdCategoria:String,const '+arCategoria.join('|')+':String, const '+_IN+':int, const '+_INTEGER+':int);';
		execute += 'crt.add(*cat:Object);';
	}
	if( arCategoriaNome.length > 0 ){
		objects += 'catNm=sol.dao.ItemComparator(const nmCategoria:String,const '+arCategoriaNome.join('|')+':String, const '+_IN+':int, const '+_INTEGER+':int);';
		execute += 'crt.add(*catNm:Object);';
	}
	// Adiciona Subcategoria
	if( arSubCategoria.length > 0 ){
		objects += 'subCat=sol.dao.ItemComparator(const cdSubCategoria:String,const '+arSubCategoria.join('|')+':String, const '+_IN+':int, const '+_INTEGER+':int);';
		execute += 'crt.add(*subCat:Object);';
	}
	if( arSubCategoriaNome.length > 0 ){
		objects += 'subCatNm=sol.dao.ItemComparator(const nmSubCategoria:String,const '+arSubCategoriaNome.join('|')+':String, const '+_IN+':int, const '+_INTEGER+':int);';
		execute += 'crt.add(*subCatNm:Object);';
	}
	
	// data inicio
	objects+='dtMovimentoInicio=sol.dao.ItemComparator(const dtMovimentoInicio:String,const '+ $('dtMovimentoInicio').value+':String, const '+_EQUAL+':int, const '+_DATE+':int);';
	execute += 'crt.add(*dtMovimentoInicio:Object);';
	// data fim
	objects+='dtMovimentoFim=sol.dao.ItemComparator(const dtMovimentoFim:String,const '+ $('dtMovimentoFim').value+':String, const '+_EQUAL+':int, const '+_DATE+':int);';
	execute += 'crt.add(*dtMovimentoFim:Object);';
	
	objects+='cdFornec=sol.dao.ItemComparator(const cdFornecedor:String,const '+ $('cdFornecedorSearch').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
	execute += 'crt.add(*cdFornec:Object);';
	
	objects+='stPreco=sol.dao.ItemComparator(const stPreco:String,const '+ $('stPreco').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
	execute += 'crt.add(*stPreco:Object);';
	
	objects+='cdTabelaPreco=sol.dao.ItemComparator(const cdTabelaPreco:String,const '+ $('cdTabelaPreco').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
	execute += 'crt.add(*cdTabelaPreco:Object);';
	
	var cdProduto = $('cdProdutoSearch').value;
	if(cdProduto != null & cdProduto != undefined && cdProduto != 0){
		objects+='cdProd=sol.dao.ItemComparator(const cdProduto:String,const '+ cdProduto+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute += 'crt.add(*cdProd:Object);';
	}
	
	var calcCusto = jQuery('input[name="calculoCusto"]:checked').val();
	objects+='calcCusto=sol.dao.ItemComparator(const calculoCusto:String,const '+ (calcCusto != undefined ? parseInt(calcCusto) : -1) +':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
	execute += 'crt.add(*calcCusto:Object);';

	var sit = jQuery('input[name="situacao"]:checked').val();
	if(sit != null && sit != undefined ){
		objects+='sit=sol.dao.ItemComparator(const situacao:String,const '+ parseInt(sit) +':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute += 'crt.add(*sit:Object);';
	}
	
	var lgEstoq = jQuery('input[name="comSemEstoque"]:checked').val();
	objects+='lgEstoq=sol.dao.ItemComparator(const lgEstoque:String,const '+ (lgEstoq !=undefined ? parseInt(lgEstoq) : -1 )+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
	execute += 'crt.add(*lgEstoq:Object);';
	
	var ord = jQuery('input[name="ordemImpressao"]:checked').val();
	objects+='ordImp=sol.dao.ItemComparator(const ordemImpressao:String,const '+ (ord != undefined ? parseInt(ord) : -1) +':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
	execute += 'crt.add(*ordImp:Object);';
	
	sendToPost('../ireport2.jsp', '1', className, objects, execute, params, method, nomeJasper, cdEmpresa, 'alm', listaSubReports);
}

function btnImprimirOnClick(content) {
	printReport();
}

function printReport(){
	
}

function btnFindFornecedorOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Fornecedores", width: 350, height: 225,  modal: true,
									 className: "com.tivic.manager.grl.PessoaServices", method: "find", allowFindAll: true,
								     filterFields: [[{label:"Nome", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
								     gridOptions: {columns: [{label:"Nome", reference:"nm_pessoa"}],
											       strippedLines: true, columnSeparator: false, lineSeparator: false},
								     hiddenFields: [{reference:"gn_pessoa",value:0, comparator:_EQUAL,datatype:_INTEGER},
								                    {reference:"J.CD_EMPRESA", value:<%=cdEmpresa%>, comparator:_EQUAL, datatype:_INTEGER},
								                    {reference:"J.CD_VINCULO", value:<%=cdVinculoFornecedor%>, comparator:_EQUAL, datatype:_INTEGER}
								     				],
								     callback: btnFindFornecedorOnClick});
    }
    else {// retorno
		closeWindow('jFiltro');
		$('cdFornecedorSearch').value     = reg[0]['CD_PESSOA'];
		$('nmFornecedorViewSearch').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearFornecedorOnClick()	{
	$('cdFornecedorSearch').value = 0;
	$('nmFornecedorViewSearch').value = '';
}


function btnFindProdutoOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Produtos",
								   width: 550,
								   height: 280,
								   top:35,
								   modal: true,
								   noDrag: true,
								   className: "com.tivic.manager.grl.ProdutoServicoDAO",
								   method: "find",
								   allowFindAll: true,
								   filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
								   				  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80, charcase:'uppercase'},
												   {label:"ID", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'}]],
								   gridOptions: {columns: [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
								   						   {label:"ID", reference:"ID_PRODUTO_SERVICO"},
														   {label:"Fabricante", reference:"nm_fabricante"}],
											   strippedLines: true,
											   columnSeparator: false,
											   lineSeparator: false},
								   hiddenFields: [{reference:"TP_PRODUTO_SERVICO",value:0, comparator:_EQUAL,datatype:_INTEGER}],
							       callback: btnFindProdutoOnClick});
    }
    else {// retorno
		closeWindow('jFiltro');
		$('cdProdutoSearch').value     = reg[0]['CD_PRODUTO_SERVICO'];
		$('nmProdutoViewSearch').value = reg[0]['NM_PRODUTO_SERVICO'];
	}
}

function btnClearProdutoOnClick()	{
	$('cdProdutoSearch').value = 0;
	$('nmProdutoViewSearch').value = '';
}

var rsmOptionsCategoria = {lines:[]};
function createCategoriasOptions()	{
	rsmOptionsCategoria = {lines:[]};
	var tpRelatorio =  $('tpRelatorio').value != null ?  $('tpRelatorio').value : 0; 
	/*
	* 0 - Consolidação de produto
	* 1 - Consolidação de combustivel
	*
	*/
	if(tpRelatorio == 0){
		var rsmOptions = <%=Jso.getStream( com.tivic.manager.alm.GrupoServices.getAllHierarquia() ) %>;
		for ( var i = 0; i < rsmOptions.lines.length; i++) {
			if(rsmOptions.lines[i].CD_GRUPO != <%= cdGrupoCombustivel %>){
				rsmOptionsCategoria.lines.push( 
						{LABEL:rsmOptions.lines[i].NM_GRUPO,
						TYPE: "checkbox",
						CD_GRUPO:rsmOptions.lines[i].CD_GRUPO,
						CHECKED: '0'}
					); 
			}
		}
	} else{
		var rsmOptions = <%=Jso.getStream( com.tivic.manager.alm.GrupoServices.getAllHierarquia() ) %>;
		for ( var i = 0; i < rsmOptions.lines.length; i++) {
			if(rsmOptions.lines[i].CD_GRUPO == <%= cdGrupoCombustivel %>){
				rsmOptionsCategoria.lines.push( 
						{LABEL:rsmOptions.lines[i].NM_GRUPO,
						TYPE: "checkbox",
						CD_GRUPO:rsmOptions.lines[i].CD_GRUPO,
						CHECKED: '0'}
					); 
			}
		}
	}
	gridCategoria = GridOne.create('gridCategoria', {plotPlace: $('divGridCategoria'), 
	                                                                 resultset: rsmOptionsCategoria, 
																     noSelectorColumn: true,
																     columnSeparator: false,
																     lineSeparator: false,
																   columns: [
	 																		 {label:"Todos", reference: 'CHECKED', type: GridOne._CHECKBOX, width: '80', 
																			  onCheck: function(){
																							this.parentNode.parentNode.register['CHECKED'] = this.checked ? 1 : 0;
																							toggleSubCategorias( this.parentNode.parentNode.register['CD_GRUPO'], this.checked );
																						}
																			 },
																             {label:'',reference:'LABEL'}
																             ]});

	createSubCategoriasOptions(rsmOptions);
	
	
}
//Check/desabilitar automático de subcategorias ao selecionar categorias
function toggleSubCategorias( cdGrupoSuperior, toggle ){
	 for( var i=0;i<rsmColumnsSubCatProdutos.lines.length;i++ ){
		 if( rsmColumnsSubCatProdutos.lines[i].CD_GRUPO_SUPERIOR  == cdGrupoSuperior ){
			 rsmColumnsSubCatProdutos.lines[i].CHECKED = toggle;
			 check = $('checkbox_gridSubCategoriastable_tr_'+(i+1).toString()+'_td_0');
			 check.removeAttribute('disabled');
			 /*
			  * 0 - Consolidação de produto
			  * 1 - Consolidação de combustível
			  *
			 */
			 var tpRelatorio =  $('tpRelatorio').value != null ?  $('tpRelatorio').value : 0; 
			  if(tpRelatorio == 0){
			      if(!toggle) 
					check.disabled = 'disabled';
			  }
		}		
	 }
	 gridSubCategorias.updateRows();
}


var rsmColumnsSubCatProdutos = {lines:[]};
function createSubCategoriasOptions(rsmGrupos)	{
	rsmColumnsSubCatProdutos = {lines:[]};
	var tpRelatorio =  $('tpRelatorio').value != null ?  $('tpRelatorio').value : 0; 
	/*
	* 0 - Consolidação de produto
	* 1 - Consolidação de combustível
	*
	*/
	if(tpRelatorio == 0){
		for( i=0; i<rsmGrupos.lines.length;i++ ){
			rsmSubGrupos = rsmGrupos.lines[i].SUBRESULTSETMAP;
			for( j=0;j<rsmSubGrupos.lines.length;j++ ){
				if(rsmSubGrupos.lines[j].CD_GRUPO_SUPERIOR != <%= cdGrupoCombustivel %>){
					rsmColumnsSubCatProdutos.lines.push( {LABEL:rsmSubGrupos.lines[j].NM_GRUPO, TYPE: "checkbox",
														CD_GRUPO:rsmSubGrupos.lines[j].CD_GRUPO,
														CD_GRUPO_SUPERIOR: rsmSubGrupos.lines[j].CD_GRUPO_SUPERIOR,
		 												CHECKED: '0'} );
				}
			}
		}
	} else {
		for( i=0; i<rsmGrupos.lines.length;i++ ){
			rsmSubGrupos = rsmGrupos.lines[i].SUBRESULTSETMAP;
				for( j=0;j<rsmSubGrupos.lines.length;j++ ){
					if(rsmSubGrupos.lines[j].CD_GRUPO_SUPERIOR == <%= cdGrupoCombustivel %>){
						rsmColumnsSubCatProdutos.lines.push( {LABEL:rsmSubGrupos.lines[j].NM_GRUPO, TYPE: "checkbox",
															CD_GRUPO:rsmSubGrupos.lines[j].CD_GRUPO,
															CD_GRUPO_SUPERIOR: rsmSubGrupos.lines[j].CD_GRUPO_SUPERIOR,
			 												CHECKED: '0'} );
					}
				}
		}
	}
	gridSubCategorias = GridOne.create('gridSubCategorias', {plotPlace: $('divGridSubcategoria'), 
		   resultset: rsmColumnsSubCatProdutos, 
		   noSelectorColumn: true, /*noSelect: true,*/
		   noFocusOnSelect: true,
		   lineSeparator: true, 
		   columnSeparator: false,
		   onCreate: function(){
			 //Mantem as subcategorias desabilitadas, até ser selecionada 
				for( var i=0; i<rsmColumnsSubCatProdutos.lines.length;i++ ){
					check = this.getElementsColumn(0);
// 					check[i].disabled = 'disabled';
				}
		   },
		   columns: [{label:"", reference: 'CHECKED', type: GridOne._CHECKBOX, width: '80', 
				  onCheck: function(){
								this.parentNode.parentNode.register['CHECKED'] = this.checked ? 1 : 0;
						   }
				 },
				 {label:'SubCategorias', reference:'LABEL', width: '800'}
		]});
}

var rsmOptionsLocaisEstoque = {lines:[]};
function createLocaisEstoqueOptions()	{
	rsmOptionsLocaisEstoque = {lines:[]};
	
	var tpRelatorio =  $('tpRelatorio').value != null ?  $('tpRelatorio').value : 0; 
    /*
	* 0 - Consolidação de produto
	* 1 - Consolidação de combustível
	*
	*/
    if(tpRelatorio == 0){
		var rsmOptions = <%=Jso.getStream( com.tivic.manager.alm.LocalArmazenamentoServices.getNotTanques(cdEmpresa) ) %>;
    }else{
		var rsmOptions = <%=Jso.getStream( com.tivic.manager.alm.LocalArmazenamentoServices.getTanques(cdEmpresa) ) %>;
		}

		for (var i = 0; i < rsmOptions.lines.length; i++) {
			rsmOptionsLocaisEstoque.lines.push({
				LABEL : rsmOptions.lines[i].NM_LOCAL_ARMAZENAMENTO,
				TYPE : "checkbox",
				CD_GRUPO : rsmOptions.lines[i].CD_LOCAL_ARMAZENAMENTO,
				CHECKED : '0'
			});
		}
		gridLocaisEstoque = GridOne.create('gridLocaisEstoque',{
							plotPlace : $('divGridLocaisEstoque'),
							resultset : rsmOptionsLocaisEstoque,
							noSelectorColumn : true,
							columnSeparator : false,
							lineSeparator : false,
							columns : [
									{
										label : "Todos",
										reference : 'CHECKED',
										type : GridOne._CHECKBOX,
										width : '80',
										onCheck : function() {
											this.parentNode.parentNode.register['CHECKED'] = this.checked ? 1
													: 0;
										}
									}, {
										label : '',
										reference : 'LABEL'
									} ]
						});

	}
	
	function toggleCheck(element, el){
		var toggle = jQuery(el).is(':checked');
		var elements = element.getElementsByTagName('input');
		for(var i = 0; i < elements.length; i++){
			elements[i].checked = toggle;
		}
	}
</script>

<style type="text/css">
.d1-form .d1-body .d1-line {
	height: 35px;
}
</style>
</head>
<body class="body" onload="init()">
	<div style="width: 892px;" id="relatorioProduto" class="d1-form">
		<div style="width: 892px; height: 350px;" class="d1-body">
			<input datatype="INTEGER" id="cdEmpresa" name="cdEmpresa"
				type="hidden" />
			<div id="toolBar" class="d1-toolBar"
				style="height: 113px; width: 200px; float: left;"></div>
			<div
				style="border: 1px solid #999; padding: 2px 2px 2px 4px; height: 108px; margin-bottom: 2px; width: 650px; float: left; margin-left: 5px;">
				<div class="d1-line" id="line0">
					<div style="width: 195px;" class="element">
						<label class="caption" for="cdTabelaPreco">Tipo de
							Relatório</label> <select style="width: 190px;" nullvalue="-1"
							column="tpRelatorio" idform="relProduto" class="select2"
							id="tpRelatorio" name="tpRelatorio"
							onchange="changeTipoRelatorio();">
							<option value="0">Consolidação de Produto</option>
							<option value="1">Consolidação de Combustível</option>
						</select>
					</div>
					<div style="width: 100px;" class="element">
						<label class="caption">Data de Estoque </label> <input
							name="dtMovimentoInicio" type="text" class="field2"
							id="dtMovimentoInicio" style="width: 85px;" maxlength="19"
							mask="##/##/####" value="<%=today%>" />
						<button idform="relatorioEstoque"
							onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')"
							title="Selecionar data..." reference="dtMovimentoInicio"
							class="controlButton" style="height: 22px;">
							<img alt="|30|" src="/sol/imagens/date-button.gif">
						</button>
					</div>
					<div style="width: 100px;" class="element">
						<label class="caption"></label> <input name="dtMovimentoFim"
							type="text" class="field2" id="dtMovimentoFim"
							style="width: 85px;" maxlength="19" mask="##/##/####"
							value="<%=today%>" />
						<button idform="relatorioEstoque"
							onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')"
							title="Selecionar data..." reference="dtMovimentoFim"
							class="controlButton" style="height: 22px;">
							<img alt="|30|" src="/sol/imagens/date-button.gif">
						</button>
					</div>
					<div style="width: 125px;" class="element">
						<label class="caption" for="stPreco">Pre&ccedil;o de Venda</label>
						<select nullvalue="-1" column="stPreco"
							relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>"
							idform="relProduto" style="width: 120px;" class="select2"
							datatype="STRING" id="stPreco" name="stPreco">
							<option value="-1">Ignorar (todos)</option>
							<option value="0">Somente com pre&ccedil;o</option>
							<option value="1">Somento sem pre&ccedil;o</option>
						</select>
					</div>
					<div style="width: 125px;" class="element"margin-left: 5px;>
						<label class="caption" for="cdTabelaPreco">Tabela de
							Pre&ccedil;o de Venda</label> <select style="width: 120px;"
							nullvalue="-1" column="cdTabelaPreco"
							relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>"
							idform="relProduto" class="select2" datatype="STRING"
							id="cdTabelaPreco" name="cdTabelaPreco">
						</select>
					</div>
				</div>

				<div class="d1-line" id="line0">
					<div style="width: 320px;" class="element">
						<label class="caption" for="cdFornecedor">Fornecedor</label> <input
							idform="relProduto" nullvalue="0" column="cdFornecedor"
							relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>"
							datatype="STRING" id="cdFornecedorSearch"
							name="cdFornecedorSearch" type="hidden" value="0"
							defaultValue="0" /> <input style="width: 296px;" static="true"
							class="disabledField2" disabled="disabled"
							name="nmFornecedorViewSearch" id="nmFornecedorViewSearch"
							type="text" />
						<button onclick="btnFindFornecedorOnClick()" idform="relProduto"
							style="height: 22px;" title="Pesquisar valor para este campo..."
							class="controlButton controlButton2">
							<img alt="L" src="/sol/imagens/filter-button.gif" />
						</button>
						<button onclick="btnClearFornecedorOnClick()" idform="relProduto"
							style="height: 22px;" title="Limpar este campo..."
							class="controlButton">
							<img alt="X" src="/sol/imagens/clear-button.gif" />
						</button>
					</div>
					<div class="d1-line" id="line0">
						<div style="width: 320px;" class="element">
							<label class="caption" for="cdProduto">Produto</label> <input
								idform="relProduto" nullvalue="0" column="cdFornecedor"
								relation="<%=ItemComparator.EQUAL%>"
								sqltype="<%=Types.INTEGER%>" datatype="STRING"
								id="cdProdutoSearch" name="cdProdutoSearch" type="hidden"
								value="0" defaultValue="0" /> <input style="width: 296px;"
								static="true" class="disabledField2" disabled="disabled"
								name="nmProdutoViewSearch" id="nmProdutoViewSearch" type="text" />
							<button onclick="btnFindProdutoOnClick()" idform="relProduto"
								style="height: 22px;" title="Pesquisar valor para este campo..."
								class="controlButton controlButton2">
								<img alt="L" src="/sol/imagens/filter-button.gif" />
							</button>
							<button onclick="btnClearProdutoOnClick()" idform="relProduto"
								style="height: 22px;" title="Limpar este campo..."
								class="controlButton">
								<img alt="X" src="/sol/imagens/clear-button.gif" />
							</button>
						</div>
					</div>
				</div>
			</div>
			<div class="d1-line" id="line0" style="height: 100px; float: left;">
				<div style="width: 187px;" class="element">
					<label class="caption" for="divGridCategoria">Categoria</label>
					<div>
						<input type="checkbox" name="checkAllCategoria" onclick="toggleCheck(divGridCategoria, this)" />
						<label class="caption" style="display: inline;">Todos</label>
					</div>
					<div id="divGridCategoria"
						style="width: 180px; height: 155px; border: 1px solid #CCC; margin: 2px 1px 0 0; background-color: #FFF;"
						class="element">
					</div>
				</div>
				<div style="width: 187px;" class="element">
					<label class="caption" for="divGridSubcategoria">SubCategoria</label>
					<div>
						<input type="checkbox" name="checkAllSubCategoria" onclick="toggleCheck(divGridSubcategoria, this)" />
						<label class="caption" style="display: inline;">Todos</label>
					</div>
					<div id="divGridSubcategoria"
						style="width: 180px; height: 155px; border: 1px solid #CCC; margin: 2px 1px 0 0; background-color: #FFF;"
						class="element"></div>
				</div>
				<div style="width: 250px;" class="element">
					<label class="caption" for="situacao">Situação</label>
					<div id="situacao"
						style="width: 200px; height: 130; border: 1px solid #CCC; margin: 2px 1px 0 0; background-color: #FFF;"
						class="element">
						<input type="radio" name="situacao" value="-1" />Todos <input
							type="radio" name="situacao" value="0" /> Ativo <input
							type="radio" name="situacao" value="1" /> Inativo
					</div>
					<br />
					<div class="element" style="width: 250px;" id="divOrdemImpressao">
						<label class="caption" for="ordemImpressao" style="width: 100px;">Ordem
							Impressão</label>
						<div id="ordemImpressao"
							style="width: 100px; height: 130; border: 1px solid #CCC; margin: 2px 1px 0 0; background-color: #FFF;"
							class="element">
							<input type="radio" name="ordemImpressao" value="0">Código
								<br /> <input type="radio" name="ordemImpressao" value="1">Descrição
							
						</div>
					</div>
				</div>
				<div style="width: 250px;" class="element">
					<label class="caption" for="divGridLocaisEstoque">Locais de Estoque</label>
					<div>
						<input type="checkbox" name="checkAllLocaisEstoque" onclick="toggleCheck(divGridLocaisEstoque, this)" />
						<label class="caption" style="display: inline;">Todos</label>
					</div>
					<div id="divGridLocaisEstoque"
						style="width: 240px; height: 155px; border: 1px solid #CCC; margin: 2px 1px 0 0; background-color: #FFF;"
						class="element"></div>
				</div>
			</div>
</body>
<%
	} catch (Exception e) {
	}
%>
</html>