<!-- TOTAL DE PX PARA RELATÓRIO - RETRATO = 590) -->
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
<%@page import="sol.util.*" %>
<%@page import="com.tivic.manager.adm.*" %>
<%@page import="com.tivic.manager.grl.*" %>
<%@page import="com.tivic.manager.util.*" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<security:registerForm idForm="formRelatorioOcorrencias"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, grid2.0, toolbar, form, filter, calendario, janela2.0, report" compress="false"/>
<%
	GregorianCalendar hoje = new GregorianCalendar();
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	Empresa empresa = EmpresaDAO.get(cdEmpresa);
	boolean callFind = RequestUtilities.getParameterAsInteger(request, "callFind", 0)==1;
	Date date = new Date();
	String nowDate = new SimpleDateFormat("dd/MM/yyyy").format(date);
	System.out.println(nowDate);
%>
<script language="javascript">

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
	
var situacaoOcorrencia = <%=sol.util.Jso.getStream(Recursos.situacaoOcorrencia)%>;
var rsmOcorrencias = null;
var columnsOcorrencias = [{label:'Cliente', reference:'NM_PESSOA', columnWidth: 175}, 
							 {label:'Telefone', reference:'NR_TELEFONE1', type:GridOne._MASK, mask:'(##) ####-####', columnWidth: 60},
							 {label:'Telefone - 2', reference:'NR_TELEFONE2', type:GridOne._MASK, mask:'(##) ####-####', columnWidth: 60},
						  	 {label:'Tipo ocorrência', reference:'NM_TIPO_OCORRENCIA', columnWidth: 175}, 
						  	 {label:'Situação', reference:'CL_ST_OCORRENCIA', columnWidth: 80},
						  	 {label:'Data', reference:'DT_OCORRENCIA', type:GridOne._DATE, columnWidth: 50},
						  	 {label:'Usuário', reference:'NM_LOGIN', columnWidth: 100}];
var gridOcorrencias = null;

function initRelatorioOcorrencias()	{
	var toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBarGrid',
								    	     orientation: 'horizontal',
								    	     buttons: [{id: 'btnPesquisar', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar registros', onClick: btnFindOnClick},
												       {id: 'btnImprimir', img: '/sol/imagens/form-btRelatorio16.gif', label: 'Imprimir', title: 'Imprimir dados', onClick: btnImprimirOnClick},
										    		   {separator: 'horizontal'},
										 			   {id: 'btVisualizar', img: '../adm/imagens/visualizar16.gif', label: 'Visualizar', onClick: viewPessoa}]});
    
  	addShortcut('ctrl+p', btnFindOnClick);

    enableTabEmulation();
	
    var dtDataMask = new Mask(document.getElementById("dtInicialSearch").getAttribute("mask"), "date");
    dtDataMask.attach(document.getElementById("dtInicialSearch"));
    dtDataMask.attach(document.getElementById("dtFinalSearch"));

	loadOptions(document.getElementById('stOcorrencia'), <%=sol.util.Jso.getStream(Recursos.situacaoOcorrencia)%>);
	
	if(<%=callFind%>)
		btnFindOnClick(null);
}

var titleText = '';
function btnFindOnClick(content) {
	if(content==null)	{
		if(document.getElementById('dtInicialSearch').value == '' || document.getElementById('dtInicialSearch').value == '')	{
			alert('Informe a data inicial para geração do relatório!');
			document.getElementById('dtInicialSearch').select();
			document.getElementById('dtInicialSearch').focus();
			return;
		}
		var objetos = 'criterios=java.util.ArrayList();',
			execute = '';
    	titleText = '<%=empresa!=null ? empresa.getNmPessoa() : "dotManager"%><br>Relatório - Ocorrências<br>' + 
	                '<table border="0" cellspacing="2" cellpadding="0">' + 
  			    	' 	<tr style="font-size:11px; font-family:Geneva, Arial, Helvetica, sans-serif;">';
		// Empresa
		if(document.getElementById('cdEmpresa').value > 0)	{
			objetos += 'itemEmpresa=sol.dao.ItemComparator(const E.cd_empresa:String, const ' + document.getElementById('cdEmpresa').value + ':String, ' +
				       'const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
			execute += 'criterios.add(*itemEmpresa:Object);';
		}
		// Período
		// Inicial
		objetos += 'itemDtInicial=sol.dao.ItemComparator(const dt_ocorrencia:String, const ' + document.getElementById('dtInicialSearch').value + ':String, ' + 
				   'const ' + _GREATER_EQUAL + ':int, const ' + _TIMESTAMP + ':int);';
		execute += 'criterios.add(*itemDtInicial:Object);';
		var periodoText = 'de ' + document.getElementById('dtInicialSearch').value;
		// Final
		if(document.getElementById('dtFinalSearch').value != '')	{
			objetos += 'itemDtFinal=sol.dao.ItemComparator(const dt_ocorrencia:String, const ' + document.getElementById('dtFinalSearch').value + ':String, ' +
					   'const ' + _MINOR_EQUAL + ':int, const ' + _TIMESTAMP + ':int);';
			execute += 'criterios.add(*itemDtFinal:Object);';
			periodoText = periodoText + ' até ' + document.getElementById('dtFinalSearch').value;
		}
		titleText += '		<td style="text-align:left;" nowrap="nowrap"><strong>Per&iacute;odo:</strong></td> ' +
			    	 '		<td style="text-align:left;" nowrap="nowrap">' + periodoText + '</td>' + 
			    	 '		<td style="text-align:left;" nowrap="nowrap">&nbsp;</td>';
		// Pessoa
		if(document.getElementById('cdPessoa').value > 0)	{
			objetos += 'itemPessoa=sol.dao.ItemComparator(const A.cd_pessoa:String, const ' + document.getElementById('cdPessoa').value + ':String, const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
			execute += 'criterios.add(*itemPessoa:Object);';
			titleText += ' 	<tr style="font-size:11px; font-family:Geneva, Arial, Helvetica, sans-serif;">' +
						 '		<td style="text-align:left;" nowrap="nowrap"><strong>Pessoa:</strong></td>' + 
				    	 '		<td style="text-align:left;" nowrap="nowrap">' + document.getElementById('cdPessoaView').value + '</td>' +
						 '  </tr>';
		}
		// Tipo de Ocorrência
		if(document.getElementById('cdTipoOcorrencia').value > 0)	{
			objetos += 'itemTipoOcorrencia=sol.dao.ItemComparator(const A.cd_tipo_ocorrencia:String, const ' + document.getElementById('cdTipoOcorrencia').value + ':String, ' +
					   'const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
			execute += 'criterios.add(*itemTipoOcorrencia:Object);';
			titleText += ' 	<tr style="font-size:11px; font-family:Geneva, Arial, Helvetica, sans-serif;">' +
						 '		<td style="text-align:left;" nowrap="nowrap"><strong>Tipo:</strong></td>' + 
				    	 '		<td style="text-align:left;" nowrap="nowrap">' + document.getElementById('cdTipoOcorrenciaView').value + '</td>' +
						 '  </tr>';
		}
		// Situação
		if(document.getElementById('stOcorrencia').value >= 0)	{
			objetos += 'itemStOcorrencia=sol.dao.ItemComparator(const st_ocorrencia:String, const ' + document.getElementById('stOcorrencia').value + ':String, ' +
					   'const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
			execute += 'criterios.add(*itemStOcorrencia:Object);';
			titleText += ' 	<tr style="font-size:11px; font-family:Geneva, Arial, Helvetica, sans-serif;">' +
						 '		<td style="text-align:left;" nowrap="nowrap"><strong>Situação:</strong></td>' + 
				    	 '		<td style="text-align:left;" nowrap="nowrap">' + document.getElementById('stOcorrencia').text + '</td>' +
						 '  </tr>';
		}
		titleText += '   </tr>' + 
			    	 '</table>';
		formMensagem = createTempbox("jMsg", {width: 165,
							   height: 50,
							   message: "Aguarde... carregando dados!",
							   tempboxType: "LOADING",
							   time: 0});
		createGrid(null);
		// BUSCANDO
		setTimeout(function()	{
				   getPage('GET', 'btnFindOnClick', 
						   '../methodcaller?className=com.tivic.manager.grl.OcorrenciaServices' +
						   '&objects=' + objetos +
						   (execute!=''?'&execute=':'') + execute +
						   '&method=find(*criterios:java.util.ArrayList)')}, 10);
	}
	else {	// retorno
		formMensagem.close();
		rsmOcorrencias = null;
		var numeroLinhas = 0;
		try {
			rsmOcorrencias = eval('(' + content + ')')
			numeroLinhas = rsmOcorrencias.lines.length;
		} catch(e) {}
		createGrid(rsmOcorrencias);
		if(numeroLinhas > 0)	{
			var reg = gridOcorrencias.getSelectedRowRegister();
		}
		document.getElementById('labelResultado').innerHTML = (numeroLinhas==0)?'Nenhum registro encontrado':(numeroLinhas==1)?'1 registro encontrado':numeroLinhas + ' registros encontrados';
	}
}

function btnPesquisarPessoaOnClick(reg){
    if(!reg){
        var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:60, charcase:'uppercase'},
							 {label:"Apelido", reference:"NM_APELIDO", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:40, charcase:'uppercase'}],
							[{label:"Nome da mãe", reference:"NM_MAE", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:50, charcase:'uppercase'},
							 {label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'},
							 {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:25, charcase:'uppercase'}]];
		filterFields.push([{label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:45, charcase:'uppercase'},
						   {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
						   {label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
						   {label:"Cidade", reference:"U.nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:25, charcase:'uppercase'}]);
        var columnsGrid = [{label:"Nome", reference:"NM_PESSOA"},
						   {label:"ID", reference:"ID_PESSOA"},
						   {label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'},
						   {label:"CPF", reference:"NR_CPF"},
						   {label:"Cidade", reference:"NM_CIDADE"},
						   {label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE}];
        var hiddenFields = [{reference:"J.CD_EMPRESA", value:<%=cdEmpresa%>, comparator:_EQUAL, datatype:_INTEGER}];	
    	hiddenFields.push({reference:"findEnderecoPrincipal", value:0, comparator:_EQUAL, datatype:_INTEGER});
			
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Pessoas", 
												   width: 600,
												   height: 380,
												   modal: true,
												   noDrag: true,
												   className: "com.tivic.manager.grl.PessoaServices",
												   method: "find",
												   filterFields: filterFields,
												   gridOptions: {columns: columnsGrid,
															     strippedLines: true,
															     columnSeparator: false,
															     lineSeparator: false},
												   hiddenFields: hiddenFields,
												   callback: btnPesquisarPessoaOnClick
										});
    }
    else {// retorno
        closeWindow('jFiltro');
        $("cdPessoa").value = reg[0]['CD_PESSOA'];
        $("cdPessoaView").value = reg[0]['NM_PESSOA'];
    }
}

function btnPesquisarTipoOcorrenciaOnClick(reg){
    if(!reg)	{
		FilterOne.create("jFiltro", {caption:"Pesquisar tipos de ocorrência", 
									   width: 400, height: 300, top: 10,
									   modal: true, 
									   noDrag: true,
									   className: "com.tivic.manager.grl.TipoOcorrenciaDAO", method: "find",
									   allowFindAll: true,
									   filterFields: [[{label:"Nome", reference:"NM_TIPO_OCORRENCIA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
									   gridOptions: {columns: [{label:"Nome", reference:"NM_TIPO_OCORRENCIA"}],
												     strippedLines: true,
												     columnSeparator: false,
												     lineSeparator: false},
									   callback: btnPesquisarTipoOcorrenciaOnClick
									});        
    }
    else {// retorno
        closeWindow('jFiltro');
        $("cdTipoOcorrencia").value = reg[0]['CD_TIPO_OCORRENCIA'];
        $("cdTipoOcorrenciaView").value = reg[0]['NM_TIPO_OCORRENCIA'];
    }
}

function createGrid(rsm)	{
	gridOcorrencias = GridOne.create('_gridOcorrencias', {columns: columnsOcorrencias,
														 resultset: rsm,
														 plotPlace: document.getElementById('divgridOcorrencias'),
														 onProcessRegister: function(reg) {
																if(reg['ST_OCORRENCIA'] != null)	{
																	reg['CL_ST_OCORRENCIA'] = situacaoOcorrencia[reg['ST_OCORRENCIA']];
																}
														 },
														 onDoubleClick: function() {viewPessoa();},
														 noSelectOnCreate: false});
}

function viewPessoa()	{
	if(gridOcorrencias.getSelectedRowRegister())
		parent.	createWindow('jPessoa', {caption: 'Manutenção de Cadastro Geral', top: 80, width: 700, height: 430, contentUrl: '../grl/pessoa.jsp?cdEmpresa=' + getValue('cdEmpresa') + '&cdPessoa=' + gridOcorrencias.getSelectedRowRegister()['CD_PESSOA']});
}

// Exibe relatório
function btnImprimirOnClick(content) {
	var objetos = 'criterios=java.util.ArrayList();',
		execute = '';
	// Empresa
	if(document.getElementById('cdEmpresa').value > 0)	{
		objetos += 'itemEmpresa=sol.dao.ItemComparator(const E.cd_empresa:String, const ' + document.getElementById('cdEmpresa').value + ':String, ' +
			       'const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
		execute += 'criterios.add(*itemEmpresa:Object);';
	}
	// Período
	// Inicial
	objetos += 'itemDtInicial=sol.dao.ItemComparator(const dt_ocorrencia:String, const ' + document.getElementById('dtInicialSearch').value + ':String, ' + 
			   'const ' + _GREATER_EQUAL + ':int, const ' + _TIMESTAMP + ':int);';
	execute += 'criterios.add(*itemDtInicial:Object);';
	
	if(document.getElementById('dtFinalSearch').value != '')	{
		objetos += 'itemDtFinal=sol.dao.ItemComparator(const dt_ocorrencia:String, const ' + document.getElementById('dtFinalSearch').value + ':String, ' +
				   'const ' + _MINOR_EQUAL + ':int, const ' + _TIMESTAMP + ':int);';
		execute += 'criterios.add(*itemDtFinal:Object);';
	}	
	// Pessoa
	if(document.getElementById('cdPessoa').value > 0)	{
		objetos += 'itemPessoa=sol.dao.ItemComparator(const A.cd_pessoa:String, const ' + document.getElementById('cdPessoa').value + ':String, const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
		execute += 'criterios.add(*itemPessoa:Object);';
	}
	// Tipo de Ocorrência
	if(document.getElementById('cdTipoOcorrencia').value > 0)	{
		objetos += 'itemTipoOcorrencia=sol.dao.ItemComparator(const A.cd_tipo_ocorrencia:String, const ' + document.getElementById('cdTipoOcorrencia').value + ':String, ' +
				   'const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
	}
	// Situação
	if(document.getElementById('stOcorrencia').value >= 0)	{
		objetos += 'itemStOcorrencia=sol.dao.ItemComparator(const st_ocorrencia:String, const ' + document.getElementById('stOcorrencia').value + ':String, ' +
				   'const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
		execute += 'criterios.add(*itemStOcorrencia:Object);';
	}
		 
	 var caption    = "Relatório de Ocorrências";
	 var className  = "com.tivic.manager.grl.OcorrenciaServices";
	 var method     = "gerarRelatorioOcorrencia(*criterios:java.util.ArrayList)";

	 parent.createWindow('jRelatorioOcorrencias', {caption: caption, width: frameWidth-20, height: frameHeight-50,
		 contentUrl: "../ireport2.jsp?tpLocalizacao=1&className="+ className +
				 "&method=" + method +
				 "&objects=" + objetos + 
				 (execute != '' ? '&execute=' : '') + execute + 
				 "&nomeJasper=relatorio_de_ocorrencias" +
				 "&cdEmpresa=" + document.getElementById('cdEmpresa').value +
				 "&p=dtInicial|" + document.getElementById('dtInicialSearch').value + "-=-dtFinal|" + document.getElementById('dtFinalSearch').value + 
				 "&modulo=grl"
	 });
}
</script>
</head>
<body class="body" onload="initRelatorioOcorrencias();">
<div style="width: 700px;" id="RelatorioOcorrencias" class="d1-form">
   <div style="width: 700px; height: 480px;" class="d1-body">
		<div class="d1-line" id="line0">
            <input id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>">
            <div style="width:75px;" class="element">
                <label for="dtInicialSearch" class="caption">Período</label>
                <input name="dtInicialSearch" type="text" class="field" id="dtInicialSearch" mask="dd/mm/yyyy" maxlength="10" style="width:72px; " value="<%=nowDate%>"/>
                <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtInicialSearch" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="width:75px;" class="element">
                <label for="dtFinalSearch" class="caption">&nbsp;</label>
                <input name="dtFinalSearch" type="text" class="field" id="dtFinalSearch" mask="dd/mm/yyyy" maxlength="10" style="width:72px;" value="<%=nowDate%>"/>
                <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtFinalSearch" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
            </div>
            <div style="width: 539px;" class="element">
                <label class="caption" for="cdPessoa">Pessoa (Cliente/Fornecedor/Funcionário/etc.)</label>
                <input datatype="INT" id="cdPessoa" name="cdPessoa" type="hidden"/>
                <input style="width: 536px;" static="true" disabled="disabled" class="disabledField" name="cdPessoaView" id="cdPessoaView" type="text"/>
                <button id="btnClearPessoa" title="Limpar este campo..." onclick="document.getElementById('cdPessoa').value=0; document.getElementById('cdPessoaView').value='';" class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
                <button id="btnPesquisarPessoa" onclick="btnPesquisarPessoaOnClick()" title="Pesquisar valor para este campo..." idform="Bairro" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
            </div>
        </div>
		<div class="d1-line" id="line0">
            <div style="width: 453px;" class="element">
                <label class="caption" for="cdTipoOcorrencia">Tipo de ocorrência</label>
                <input datatype="INT" id="cdTipoOcorrencia" name="cdTipoOcorrencia" type="hidden"/>
                <input style="width: 450px;" static="true" disabled="disabled" class="disabledField" name="cdTipoOcorrenciaView" id="cdTipoOcorrenciaView" type="text"/>
                <button id="btnClearTipoOcorrencia" title="Limpar este campo..." onclick="document.getElementById('cdTipoOcorrencia').value=0; document.getElementById('cdTipoOcorrenciaView').value='';" class="controlButton"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
                <button id="btnPesquisarTipoOcorrencia" onclick="btnPesquisarTipoOcorrenciaOnClick()" title="Pesquisar valor para este campo..." idform="Bairro" class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
            </div>
            <div style="width: 109px;" class="element">
            	<label class="caption" for="stOcorrencia">Situação</label>
            	<select logmessage="Situação" style="width: 106px;" class="select" idform="ocorrencia" reference="st_ocorrencia" datatype="STRING" id="stOcorrencia" name="stOcorrencia" defaultValue="0">
	            	<option value="-1">Todas</option>
            	</select>
          	</div>
            <div style="width: 130px;" class="element">
                <label class="caption" for="tpAgrupamento">Agrupar por:</label>
                <select style="width: 127px;" class="select" datatype="INT" id="tpAgrupamento" name="tpAgrupamento" defaultValue="-1">
                    <option value="-1">Nenhum</option>
                    <option value="0">Pessoa</option>
                    <option value="1">Tipo de ocorrência</option>
                    <option value="2">Situação</option>
                </select>
            </div>
        </div>
     	<div class="d1-toolBar" id="toolBarGrid" style="width:689px; height:24px; float:left; margin-top:4px;"></div>
		<div class="d1-line" id="line2">
			<div style="width: 689px;" class="element">
                <label class="caption" id="labelResultado" style="font-weight:bold;">Resultado da Pesquisa</label>
                <div id="divgridOcorrencias" style="float:left; width: 689px; height:292px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
			</div>
		</div>
  </div>
</div>
<div id="groupHeaderBandOcorrencias" style="display:none">
	<table _wdth="_PAGE_WIDTH" border="0" cellspacing="2" cellpadding="0" style="border-bottom:1px solid #000000">
	  <tr style="font-size:11px; font-family:Geneva, Arial, Helvetica, sans-serif;">
		<td id="tdCampoAgrupamento" width="1%" style="text-align:right;" nowrap="nowrap"><strong>Cidade:</strong></td>
		<td id="tdValorAgrupamento" width="99%" style="text-align:left;" nowrap="nowrap"></td>
	  </tr>
	</table>
</div>
<script>
	document.getElementById('dtInicialSearch').focus();
	createGrid(null);
</script>
</body>
</html>