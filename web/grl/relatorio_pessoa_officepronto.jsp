<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
<%@page import="java.sql.Types" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.util.*" %>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.seg.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	com.tivic.manager.grl.Empresa empresa = com.tivic.manager.grl.EmpresaDAO.get(cdEmpresa);
	Usuario usuario = (Usuario)session.getAttribute("usuario");
%>
<script language="javascript">

var rsmPessoa = null;
var tipoRegiao = <%=sol.util.Jso.getStream(com.tivic.manager.grl.RegiaoServices.tipoRegiao)%>;
var gridPessoa;
var registro = null;

function init()	{
    enableTabEmulation();
	
	var toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBarGrid',
								    	     orientation: 'horizontal',
								    	     buttons: [{id: 'btnPesquisar', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar registros', onClick: btnPesquisarOnClick},
												       {id: 'btnImprimir', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', title: 'Imprimir dados', onClick: btnImprimirOnClick},
										    		   {separator: 'horizontal'},
										 			   {id: 'btVisualizar', img: '../adm/imagens/visualizar16.gif', label: 'Visualizar', onClick: viewPessoa}]});

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
	
	$('cdEmpresa').value = <%=cdEmpresa%>;
	createGrid(null);
}

function formValidation(){
	var fields = [];
	if(parseInt($('tpData').value, 10) >= 0) {
		fields.push([$("dtInicial"), 'Informe', VAL_CAMPO_DATA_OBRIGATORIO]);
		fields.push([$("dtFinal"), 'Informe', VAL_CAMPO_DATA_OBRIGATORIO]);
	}
	return validateFields(fields, true, 'Campos obrigatórios ou com conteúdo inválido.', '');
}

function btnPesquisarOnClick(content) {
	if(content==null)	{
		$('labelResultado').innerHTML = 'Resultado da Pesquisa';
		var objetos = 'crt=java.util.ArrayList();',
			execute = '';
		// LINHA 1
		// Empresa
		if($('cdEmpresa').value > 0)	{
			objetos += 'itemEmp=sol.dao.ItemComparator(const D.cd_empresa:String,const '+$('cdEmpresa').value+':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemEmp:Object);';
		}
		// Vínculo
		if($('cdVinculo').value > 0)	{
			objetos += 'itemVinc=sol.dao.ItemComparator(const D.cd_vinculo:String,const '+$('cdVinculo').value+':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemVinc:Object);';
		}
		// Data de Cadastro
		if ($('dtCadastroInicial').value != '')	{ // Inicial
			objetos += 'itemDtInicial=sol.dao.ItemComparator(const A.dt_cadastro:String, const ' + $('dtCadastroInicial').value +
			           ':String, const ' + _GREATER_EQUAL + ':int, const ' + _TIMESTAMP + ':int);';
			execute += 'crt.add(*itemDtInicial:Object);';
		}
		if ($('dtCadastroFinal').value != '')	{ // Final
			objetos += 'itemDtFinal=sol.dao.ItemComparator(const A.dt_cadastro:String, const ' + $('dtCadastroFinal').value +
			            ':String, const ' + _MINOR_EQUAL + ':int,const ' + _TIMESTAMP + ':int);';
			execute += 'crt.add(*itemDtFinal:Object);';
		}
		// Situação
		if($('stPessoa').value>=0)	{
			//objetos += 'itemSit=sol.dao.ItemComparator(const st_vinculo:String,const '+$('stPessoa').value+':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			//execute += 'crt.add(*itemSit:Object);';
		}
		// Tipo (Jurídica / Física)
		if($('gnPessoa').value>=0)	{
			objetos += 'itemGn=sol.dao.ItemComparator(const A.gn_pessoa:String,const '+$('gnPessoa').value+':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemGn:Object);';
		}
		// LINHA 2
		// Porte da Empresa
		if($('tpEmpresa').value>=0)	{
			objetos += 'itemPorte=sol.dao.ItemComparator(const B.tp_empresa:String,const '+$('tpEmpresa').value+':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemPorte:Object);';
		}
		// Como nos conheceu
		if($('cdFormaDivulgacao').value > 0)	{
			objetos += 'itemFD=sol.dao.ItemComparator(const A.cd_forma_divulgacao:String,const '+$('cdFormaDivulgacao').value+':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemFD:Object);';
		}
		// Aniversário
		// FALTANDO
		// Região
		if($('cdRegiao').value > 0)	{
			var fields = ['grl_pais.cd_regiao', 'M.cd_regiao', 'G.cd_regiao', 'I.cd_regiao'];
			
			objetos += 'itemReg=sol.dao.ItemComparator(const '+fields[$('cdRegiao').options[$('cdRegiao').selectedIndex].tpRegiao]+
			           ':String,const '+$('cdFormaDivulgacao').value+':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemReg:Object);';
		}
		// Estado
		if($('cdEstado').value > 0)	{
			objetos += 'itemUF=sol.dao.ItemComparator(const G.cd_estado:String,const '+$('cdEstado').value+':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemUF:Object);';
		}
		// Cidade
		if($('cdCidade').value > 0)	{
			objetos += 'itemUF=sol.dao.ItemComparator(const F.cd_cidade:String,const '+$('cdCidade').value+':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemUF:Object);';
		}
		// Distrito
		if($('cdDistrito').value > 0)	{
			objetos += 'itemDistrito=sol.dao.ItemComparator(const I.cd_distrito:String,const '+$('cdDistrito').value+':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemDistrito:Object);';
		}
		// Bairro
		if($('cdBairro').value > 0)	{
			objetos += 'itemBairro=sol.dao.ItemComparator(const F.cd_bairro:String,const '+$('cdBairro').value+':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemBairro:Object);';
		}
		// Logradouro
		if($('cdLogradouro').value > 0)	{
			objetos += 'itemLog=sol.dao.ItemComparator(const F.cd_logradouro:String,const '+$('cdLogradouro').value+':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemLog:Object);';
		}
		// LINHA 3
		createTempbox("jMsg", {width: 165, height: 50, tempboxType: "LOADING", time: 0, message: "Aguarde... pesquisando!"});
		createGrid(null);
		// BUSCANDO
		setTimeout(function()	{
									getPage('GET', 'btnPesquisarOnClick', 
											'../methodcaller?className=com.tivic.manager.grl.PessoaServices' +
											'&objects=' + objetos +
											((execute != '')?'&execute=':'') + execute +
											'&method=findPessoaEmpresaAndEndereco(*crt:java.util.ArrayList)')
								}, 10);
	}
	else {	// retorno
		closeWindow('jMsg');
		rsmPessoa = eval("(" + content + ")");
		var qt = rsmPessoa.lines.length;
		$('labelResultado').innerHTML = (qt==0)?'Nenhuma pessoa encontrada':(qt==1)?'1 pessoa encontrada':qt+' pessoas encontradas';
		createGrid(rsmPessoa);
	}
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

var columnsPessoa = [{label:'Nome da Pessoa', reference: 'NM_PESSOA'},
					 {label:'P/J', reference: 'CL_PESSOA'},							   
					 {label:'Cadastro', reference: 'DT_CADASTRO', type: GridOne._DATE},							   
					 {label:'Endereço', reference: 'CL_ENDERECO'},
					 {label:'Telefone', reference: 'NR_TELEFONE'},
					 {label:'Celular', reference: 'NR_CELULAR'},
					 {label:'Vínculo', reference: 'NM_VINCULO'}];

function createGrid(rsm)	{
	gridPessoa = GridOne.create('gridPessoa', {columns: columnsPessoa,
											   resultset: rsm,
											   plotPlace: $('divGridPessoa'),
											   onDoubleClick: function() {viewPessoa(); },
											   columnSeparator: true,
											   lineSeparator: false,
											   strippedLines: true,
											   onProcessRegister: function(reg){
											   		reg['CL_PESSOA'] = reg['GN_PESSOA']==0 ? 'J' : 'F';
													reg['CL_ENDERECO'] = reg['CL_ENDERECO'] ? reg['CL_ENDERECO'] : getFormatedAddress(reg);
											   },		
											   noSelectOnCreate: false});
}

function viewPessoa()	{
	if(gridPessoa.getSelectedRowRegister())	{
		parent.createWindow('jPessoa', {caption: 'Manutenção de Cadastro Geral', width: 700, height: 430, contentUrl: '../grl/pessoa.jsp?cdPessoa=' + gridPessoa.getSelectedRowRegister()['CD_PESSOA']});
	}
}

function btnImprimirOnClick(content) {
	var urlLogo = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const <%=cdEmpresa%>:int)';

	parent.ReportOne.create('jReportPessoa', {width: 700,
									 height: 430,
									 caption: 'Relação do Cadastro Geral',
									 resultset: gridPessoa.options.resultset,
									 /*titleBand: {defaultImage: urlLogo,
												 defaultTitle: 'TitleBand',
											   defaultInfo: 'Pág. #p de #P<br/>#d/#M/#y #h:#m:#s'},*/
									 pageHeaderBand: {defaultImage: urlLogo,
													  defaultTitle: 'Relação do Cadastro Geral',
													  defaultInfo: 'Pág. #p de #P'},
									 detailBand: {columns: [{label:'Nome da Pessoa', reference: 'NM_PESSOA'},
														    {label:'P/J', reference: 'CL_PESSOA'},							   
														    {label:'Cadastro', reference: 'DT_CADASTRO', type: GridOne._DATE},							   
														    {label:'Endereço', reference: 'CL_ENDERECO'},
														    {label:'Telefone', reference: 'NR_TELEFONE'},
														    {label:'Celular', reference: 'NR_CELULAR'},
														    {label:'Vínculo', reference: 'NM_VINCULO'}],
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
									 //summaryBand: {contentModel: $('sumaryPessoa')},
									 orientation: 'landscape',
									 paperType: 'A4',
									 tableLayout: 'fixed',
									 displayReferenceColumns: true});
}
</script>
</head>
<body class="body" onload="init();">
	<div style="width: 710px;" id="ManutencaoPessoa" class="d1-form">
   		<div style="width: 710px; height: 480px;" class="d1-body">
			<input idform="" reference="cd_conta" id="cdConta" name="cdConta" type="hidden" value="0" defaultValue="0">
			<input idform="" reference="cd_movimento_conta" id="cdMovimentoConta" name="cdMovimentoConta" type="hidden" value="0" defaultValue="0">
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
                <div class="element" style="width: 70px;">
                    <label for="stPessoa" class="caption">Situação</label>
                    <select style="width: 67px" type="text" name="stPessoa" id="stPessoa" class="select">
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
                <div class="element" style="width: 285px;">
                    <label for="cdRegiao" class="caption">Região (do país, do estado, etc...)</label>
                    <select style="width:282px" type="text" name="cdRegiao" id="cdRegiao" class="select" defaultValue="0">
                        <option value="0">Selecione ...</option>
                    </select>
                </div>
                <div style="width: 70px;" class="element">
                    <label class="caption" for="cdEstado">Estado</label>
                    <select style="width: 68px" type="text" name="cdEstado" id="cdEstado" class="select" defaultValue="BA">
                        <option value="0">...</option>
                    </select>
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
                <div style="width: 140px;" class="element">
                    <label class="caption" for="cdDistrito">Distrito</label>
                    <select style="width: 137px" type="text" name="cdDistrito" id="cdDistrito" class="select" idform="Cidade" reference="cd_estado" defaultValue="BA">
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
			<div class="d1-toolBar" id="toolBarGrid" style="width:688px; height:24px; float:left; margin-top:4px;"></div>
            <div class="d1-line" id="line3">
                <div style="width: 688px;" class="element">
					<label class="caption" id="labelResultado" style="font-weight:bold;">Resultado da Pesquisa</label>
                    <div id="divGridPessoa" style="float:left; width: 688px; height:262px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
                </div>
            </div>
        </div>
	</div>
    <div id="sumaryPessoa" style="display:none; width:_PAGE_WIDTH">
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