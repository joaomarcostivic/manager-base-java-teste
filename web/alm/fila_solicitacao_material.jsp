<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@page import="java.sql.Types" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.util.Jso" %>
<%@page import="com.tivic.manager.grl.ProdutoServicoServices" %>
<%@page import="com.tivic.manager.adm.SolicitacaoMaterialServices" %>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	try {
%>
<style>
	.stepbar {
		border-bottom:1px solid #CCCCCC; 
		height:31px; 
		font-family:Geneva, Arial, Helvetica, sans-serif; 
		text-align:center; 
		font-size:18px; 
		font-weight:bold; 
		color:#999999;
		cursor:default;
	}
	.stepbar .label{
		text-align:left; 
		float:left; 
		height:30px; 
		line-height:30px; 
		text-indent:5px;
	}
	.stepbar .disabledStep{
		width:20px; 
		height:20px; 
		border:1px solid #CCCCCC; 
		background-color:#F5F5F5; 
		float:right; 
		margin:10px 0 0 2px;
	}
	.stepbar .enabledStep{
		width:30px; 
		height:30px; 
		border:1px solid #CCCCCC; 
		background-color:#CCCCCC; 
		float:right; 
		font-size:26px; 
		color: #FFFFFF;
		margin:0 0 0 2px;
	}
	
	.protocolo {
		width:680px; 
		height:210px; 
		background-color:#FFFFFF; 
		float:left; 
		margin:10px 0 10px 0; 
		border:1px solid #CCCCCC; 
		font-family:Verdana, Arial, Helvetica, sans-serif; 
		font-size:12px; 
		overflow:auto;
	}
</style>
<script language="javascript" src="../js/adm.js"></script>
<script language="javascript">
var gridSolicitacoes = null;
var situacaoSolicitacao = <%=Jso.getStream(SolicitacaoMaterialServices.situacoes)%>;
var columnsSolicitacao = [{label: 'Tempo', reference: 'QT_DIAS'},
						  {label:"Data Solicitação", reference:"dt_solicitacao", type: GridOne._DATE},
						  {label:"N° Documento", reference:"id_solicitacao_material"}, 
						  {label:"Setor solicitante", reference:"nm_setor_solicitante"},
						  {label:"Solicitante", reference:"nm_solicitante"}, 
						  {label:'Qtd Itens', reference:'qt_itens'},
						  {label:'Situação', reference:'cl_st_solicitacao_material'}];
																		 
function init(){
	$('cdEmpresa').value = parent.$('cdEmpresa') ? parent.$('cdEmpresa').value : $('cdEmpresa').value;
	$('cdEmpresa').setAttribute("defaultValue", parent.$('cdEmpresa') ? parent.$('cdEmpresa').value : $('cdEmpresa').value);

    loadFormFields(["solicitacaoMaterial"]);
    clearFields(solicitacaoMaterialFields);
	loadSetores();
	loadOptions($('stSolicitacaoMaterial'), situacaoSolicitacao);

	ToolBar.create('toolBar', {plotPlace: 'toolBar',
					    orientation: 'horizontal',
					    buttons: [{id: 'btPesquisar', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: findSolicitacoes},
								  {separator: 'horizontal'},
							      {id: 'btDetalhes', img: '../alm/imagens/solicitacao_material16.gif', label: 'Detalhes', onClick: btDetalhesOnClick},
							      {id: 'btStartAtendimento', img: '/imagens/solicitacao_material_start16.gif', label: 'Iniciar Atendimento', onClick: btStartAtendimentoOnClick},
							      {separator: 'horizontal'},
							      {id: 'btPrint', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', onClick: btPrintOnClick}]});
	findSolicitacoes();
	enableTabEmulation();
}

function btPrintOnClick() {
	ReportOne.create('jReportFilaSolicitacaoMaterial', {width: 650,
						height: 350,
						caption: 'Fila de Solicitações de Materiais',
						resultset: gridSolicitacoes==null ? {lines: []} : gridSolicitacoes.getResultSet(),
						titleBand: {defaultImage: '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const '+$('cdEmpresa').value+':int)',
									defaultTitle: 'Solicitações de Materiais',
									defaultInfo: '#d/#M/#y #h:#m'},
						detailBand: {columns: columnsSolicitacao,
									 displayColumnName: true},
						orientation: 'landscape',
						paperType: 'A4',
						onProcessRegister: function(reg) {
												reg['CL_ST_SOLICITACAO_MATERIAL'] = situacaoSolicitacao[parseInt(reg['ST_SOLICITACAO_MATERIAL'], 10)];
												var qtDias = Math.ceil(daysToNow(reg['ST_SOLICITACAO']));
												reg['QT_DIAS'] = qtDias+' dias';
												if(qtDias>10)
													reg['QT_DIAS_cellStyle'] = 'color: #FF0000; font-weight: bold; font-size:11px;';
											}});
}

function btDetalhesOnClick() {
	if (gridSolicitacoes.getSelectedRowRegister()==null)
		createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Selecione uma Solicitação para visualizar os detalhes.",
							  msgboxType: "INFO"});
	else
		loadDetalhesSolicitacao(true);
}

function btStartAtendimentoOnClick() {
	if (gridSolicitacoes.getSelectedRowRegister()==null)
		createMsgbox("jMsg", {caption: 'Manager', width: 250, height: 50, message: "Selecione uma Solicitação para iniciar o Atendimento.",
							  msgboxType: "INFO"});
	else
		loadDetalhesSolicitacao(true, true);
}

function loadDetalhesSolicitacao(isAtendimento, startAtendimento) {
	var register = gridSolicitacoes.getSelectedRowRegister();
	startAtendimento = startAtendimento==null ? false : startAtendimento;
	var cdSolicitacaoMaterial = register==null ? 0 : register['CD_SOLICITACAO_MATERIAL'];
	if (cdSolicitacaoMaterial > 0)
		parent.miSolicitacaoMaterialOnClick({cdSolicitacaoMaterial: cdSolicitacaoMaterial, caption: 'Detalhamento de Solicitação de Material', 
											 isAtendimento: isAtendimento, startAtendimento: startAtendimento});
}

function findSolicitacoes(content) {
	if (content==null) {
		var execute = '';
		var objects = 'crt=java.util.ArrayList();';
		var fields = [];
		for (var i=0; solicitacaoMaterialFields!=null && i<solicitacaoMaterialFields.length; i++) {
			var field = solicitacaoMaterialFields[i];
			var column = field.getAttribute('column');
			var nullvalue = field.getAttribute('nullvalue');
			var value = field.value;
			if (column!=null && trim(value)!='' && (nullvalue==null || trim(value)!=nullvalue)) {
				var relation = field.getAttribute('relation');
				var sqltype = field.getAttribute('sqltype');
				objects+='i'+i+'=sol.dao.ItemComparator(const '+column+':String, ' + field.id + ':String,const '+relation+':int,const '+sqltype+':int);';
				execute+='crt.add(*i'+i+':java.lang.Object);';
				fields.push(field);
			}
		}
		fields.push(createInputElement('hidden', 'execute', execute));
		fields.push(createInputElement('hidden', 'objects', objects));
		getPage("POST", "findSolicitacoes", 
				"../methodcaller?className=com.tivic.manager.adm.SolicitacaoMaterialServices"+
				"&method=find(*crt:java.util.ArrayList)", fields);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		gridSolicitacoes = GridOne.create('gridSolicitacoes', {columns: columnsSolicitacao,
													 resultset :rsm, 
													 plotPlace : $('divGridSolicitacoes'),
													 noSelectorColumn: true,
													 columnSeparator: true,
													 lineSeparator: false,
													 onDoubleClick: function(){
													 		loadDetalhesSolicitacao(true);
														},
													 onProcessRegister: function(reg) {
													 		reg['CL_ST_SOLICITACAO_MATERIAL'] = situacaoSolicitacao[parseInt(reg['ST_SOLICITACAO_MATERIAL'], 10)];
															var qtDias = Math.ceil(daysToNow(reg['ST_SOLICITACAO']));
															reg['QT_DIAS'] = qtDias+' dias';
															if(qtDias>10)
																reg['QT_DIAS_cellStyle'] = 'color: #FF0000; font-weight: bold; font-size:11px;';
														}});	
	}
}

function btnFindSolicitanteOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Localizar Responsável pela Solicitação", 
												   width: 450,
												   height: 275,
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
												   callback: btnFindSolicitanteOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
		filterWindow.close();
		$('cdSolicitante').value = reg[0]['CD_PESSOA'];
		$('cdSolicitanteView').value = reg[0]['NM_PESSOA'];
	}
}

function btnClearSolicitanteOnClick(){
	$('cdSolicitante').value = 0;
	$('cdSolicitanteView').value = '';
}

function btnFindProdutoServicoOnClick(reg){
    if(!reg){
        filterWindow = FilterOne.create("jFiltro", {caption:"Localizar Material a ser solicitado", 
												   width: 550,
												   height: 280,
												   top:65,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices",
												   method: "findProdutosOfEmpresa",
												   allowFindAll: true,
												   filterFields: [[{label:"Nome Material", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
												   				  [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:65, charcase:'uppercase'},
																   {label:"ID/código", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
																   {label:"ID/cód. reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:15, charcase:'uppercase'}]],
												   gridOptions: {columns: [{label:"ID/cód. reduzido", reference:"id_reduzido"},
												   						   {label:"Nome Material", reference:"NM_PRODUTO_SERVICO"},
												   						   {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
																		   {label:"ID/código", reference:"ID_PRODUTO_SERVICO"},
																		   {label:"Fabricante", reference:"nm_fabricante"}],
															   strippedLines: true,
															   columnSeparator: false,
															   lineSeparator: false},
												   hiddenFields: [{reference:"TP_PRODUTO_SERVICO",value:<%=ProdutoServicoServices.TP_PRODUTO%>, comparator:_EQUAL,datatype:_INTEGER},
												   				  {reference:"A.cd_empresa",value:$("cdEmpresa").value, comparator:_EQUAL,datatype:_INTEGER}],
												   callback: btnFindProdutoServicoOnClick, 
												   autoExecuteOnEnter: true
										});
    }
    else {// retorno
        filterWindow.close();
        $('cdProdutoServico').value = reg[0]['CD_PRODUTO_SERVICO'];
		$('nmProdutoServico').value = reg[0]['NM_PRODUTO_SERVICO'];
    }
}

function btnClearProdutoServicoOnClick(){
	$('cdProdutoServico').value = 0;
	$('nmProdutoServico').value = '';
}

function loadSetores(content)	{
	if (content==null) {
		getPage("GET", "loadSetores", 
				"../methodcaller?className=com.tivic.manager.grl.SetorServices"+
				"&method=getAllHierarquia(const " + $('cdEmpresa').value + ": int)");
	}
	else {
		var rsmSetores = null;
		try {rsmSetores = eval("(" + content + ")")} catch(e) {};
		for (var i=0; rsmSetores!=null && i<rsmSetores.lines.length; i++)
			addSetor(rsmSetores.lines[i]);
	}
}

function addSetor(regSetor)	{
	var option = document.createElement('OPTION');
	option.setAttribute('value', regSetor['CD_SETOR']);
	option.appendChild(document.createTextNode(regSetor['NM_SETOR']));
	$("cdSetorSolicitante").appendChild(option);
	var rsmSubSetores = regSetor['subResultSetMap'];
	if(rsmSubSetores != null){
		for(var i=0;i<rsmSubSetores.lines.length; i++)
			addSetor(rsmSubSetores.lines[i]);
	}
}
</script>
</head>
<body class="body" onload="init();">
<div style="width: 743px;" class="d1-form">
	<div style="width: 743px; height: 385px;" class="d1-body">
    	<input id="cdEmpresa" name="cdEmpresa" type="hidden"/>
    	<div class="d1-line" style="height:32px;">
          <div style="width: 340px;" class="element">
            <label class="caption" for="cdSetorSolicitante">Setor solicitante</label>
            <select nullvalue="0" column="A.cd_setor_solicitante" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" style="width:337px" class="select" datatype="STRING" idform="solicitacaoMaterial" id="cdSetorSolicitante" name="cdSetorSolicitante">
              <option value="0">Todos</option>
            </select>		
          </div>
          <div style="width: 400px;" class="element">
            <label class="caption" for="cdSolicitante">Solicitante</label>
            <input nullvalue="0" column="A.cd_solicitante" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" value="0" logmessage="" idform="solicitacaoMaterial" datatype="STRING" id="cdSolicitante" name="cdSolicitante" type="hidden">
            <input idform="solicitacaoMaterial" style="width: 397px;" static="true" disabled="disabled" class="disabledField" name="cdSolicitanteView" id="cdSolicitanteView" type="text">
            <button id="btnFindSolicitante" onclick="btnFindSolicitanteOnClick()" idform="solicitacaoMaterial" title="" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
            <button id="btnClearSolicitante" onclick="btnClearSolicitanteOnClick()" idform="solicitacaoMaterial" title="" class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
          </div>
		</div>
        <div class="d1-line" style="height:32px;">
          <div style="width: 95px;" class="element">
            <label class="caption" for="idSolicitacaoMaterial">N&deg; Solicita&ccedil;&atilde;o</label>
            <input column="A.id_solicitacao_material" relation="<%=ItemComparator.LIKE_BEGIN%>" sqltype="<%=Types.VARCHAR%>" style="text-transform: uppercase; width: 92px;" lguppercase="true" class="field" idform="solicitacaoMaterial" datatype="STRING" maxlength="15" id="idSolicitacaoMaterial" name="idSolicitacaoMaterial" type="text">
          </div>
          <div style="width: 121px;" class="element">
            <label class="caption" for="stSolicitacaoMaterial">Situação</label>
            <select nullvalue="-1" column="A.st_solicitacao_material" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.SMALLINT%>" style="width: 118px;" class="select" idform="solicitacaoMaterial" datatype="STRING" id="stSolicitacaoMaterial" name="stSolicitacaoMaterial" value="0" defaultvalue="0">
              <option value="-1">Todos</option>
            </select>
          </div>
            <div style="width: 110px;" class="element">
                <label class="caption" for="dtSolicitacaoInicial">Solicitado a partir de</label>
                <input style="width: 107px;" class="field" idform="solicitacaoMaterial" column="A.dt_solicitacao" relation="<%=ItemComparator.GREATER_EQUAL%>" sqltype="<%=Types.TIMESTAMP%>" id="dtSolicitacaoInicial" name="dtSolicitacaoInicial" type="text">
                <button id="btnDtSolicitacaoInicial" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtSolicitacaoInicial" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
			</div>
			<div style="width: 100px;" class="element">
                <label class="caption" for="dtSolicitacaoFinal">Solicitado at&eacute;</label>
                <input style="width: 97px;" class="field" idform="solicitacaoMaterial" column="A.dt_solicitacao" relation="<%=ItemComparator.MINOR_EQUAL%>" sqltype="<%=Types.TIMESTAMP%>" id="dtSolicitacaoFinal" name="dtSolicitacaoFinal" type="text">
                <button id="btnDtSolicitacaoFinal" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtSolicitacaoFinal" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
          <div style="width: 314px;" class="element">
            <label class="caption" for="cdProdutoServico">Material solicitado</label>
            <input nullvalue="0" column="cd_produto_servico" relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>" idform="solicitacaoMaterial" reference="cd_produto_servico" datatype="STRING" id="cdProdutoServico" name="cdProdutoServico" type="hidden" value="0" defaultValue="0">
            <input name="nmProdutoServico" type="text" disabled="disabled" class="disabledField" id="nmProdutoServico" style="width: 311px;" size="256" maxlength="256" idform="solicitacaoMaterial" reference="nm_produto_servico" static="true">
            <button id="btnFindProdutoServico" onclick="btnFindProdutoServicoOnClick()" idform="solicitacaoMaterial" title="" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
            <button id="btnClearProdutoServico" onclick="btnClearProdutoServicoOnClick()" idform="solicitacaoMaterial" title="" class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
          </div>
        </div>
        <div id="toolBar" class="d1-toolBar" style="height:24px; width: 738px; margin:4px 0 4px 0"></div>
		<div id="divGridSolicitacoes" style="width:738px; height:283px; background-color:#FFF; border:1px solid #999;"></div>
	</div>
</div>

</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>
