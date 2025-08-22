<%@ page contentType="text/html; charset=iso-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="java.util.*"%>
<%@page import="sol.util.*" %>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.seg.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.fsc.*"%>
<%@page import="com.tivic.manager.adm.*"%>
<security:registerForm idForm="formRelatorioPessoa"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, grid2.0, toolbar, form, filter, calendario, janela2.0, report" compress="false"/>
<%
	GregorianCalendar hoje = new GregorianCalendar();
	String dtInicial = RequestUtilities.getParameterAsString(request, "dtInicial", Util.formatDateTime(hoje, "dd/MM/yyyy"));
	String dtFinal 	 = RequestUtilities.getParameterAsString(request, "dtFinal", Util.formatDateTime(hoje, "dd/MM/yyyy"));
	boolean callFind = RequestUtilities.getParameterAsInteger(request, "callFind", 0)==1;
	int cdEmpresa 	 = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	int cdPessoa 	 = RequestUtilities.getParameterAsInteger(request, "cdPessoa", 0);
	com.tivic.manager.grl.Empresa empresa = com.tivic.manager.grl.EmpresaDAO.get(cdEmpresa);
	int cdVinculoCliente      = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_CLIENTE", 0);
	int cdVinculoEmitente     = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_EMITENTE", 0);
	int cdCidadeDefault       = ParametroServices.getValorOfParametroAsInteger("CD_CIDADE_END_DEFAULT", 0, cdEmpresa);
	Cidade cidade             = cdCidadeDefault<=0 ? null : CidadeDAO.get(cdCidadeDefault);
	String nmCidade           = cidade==null ? "" : cidade.getNmCidade();
%>
<script language="javascript" src="../js/adm_report.js"></script>
<script language="javascript">
var rsmPessoa = null;
var gridPessoa;
var formBaixaPessoa = null;
var registro = null;

function init()	{
    enableTabEmulation();
	
    ToolBar.create('toolBar', {plotPlace: 'toolBarGrid',
							   orientation: 'vertical',
				       	       buttons: [{id: 'btnPesquisar', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar', title: 'Pesquisar registros', onClick: btnPesquisarOnClick, width: 80},{separator: 'vertical'},
										 {id: 'btnImprimir', img: '/sol/imagens/print24.gif', label: 'Imprimir', title: 'Imprimir dados', onClick: btnImprimirOnClick, width: 80},{separator: 'vertical'}]});
    
	var dtDataMask = new Mask($("dtInicial").getAttribute("mask"));
    dtDataMask.attach($("dtInicial"));
    dtDataMask.attach($("dtFinal"));

  	
    loadOptionsFromRsm($('cdEmpresa'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EmpresaServices.getAll())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
    loadOptions($('stTitulo'), <%=Jso.getStream(TituloCreditoServices.situacao)%>);
    $('nmPessoa').focus();
	$('cdEmpresa').value     = <%=cdEmpresa%>;
	$('cdEmpresa').disabled  = <%=cdEmpresa > 0%>;
	$('cdEmpresa').className = '<%=cdEmpresa > 0 ? "disabledSelect" : "select"%>';
	
	if(<%=callFind%>)	{
		btnPesquisarOnClick(null);
	}
	$('nmPessoa').focus();
	createGrid(null);
}

function formValidation(){
	var fields = [];
	fields.push([$("dtInicial"), 'Informe', VAL_CAMPO_DATA_OBRIGATORIO]);
	fields.push([$("dtFinal"), 'Informe', VAL_CAMPO_DATA_OBRIGATORIO]);
	return validateFields(fields, true, 'Campos obrigatórios ou com conteúdo inválido.', '');
}



function btnPesquisarOnClick(content) {
	if(content==null)	{
		$('labelResultado').innerHTML = 'Resultado da Pesquisa';
		var objetos = 'crt=java.util.ArrayList();',
			execute = '';
		// Empresa
		if($('cdEmpresa').value > 0)	{
			objetos += 'itemEmpresa=sol.dao.ItemComparator(const B.cd_empresa:String,const '+$('cdEmpresa').value+
			           ':String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemEmpresa:Object);';
		}
		// nm Pessoa
		if($('nmPessoa').value != '')	{
			objetos += 'itemPessoa1=sol.dao.ItemComparator(const A.nm_pessoa:String, const ' + $('nmPessoa').value.toUpperCase() +
			           ':String, const ' + _LIKE_ANY + ':int, const ' + _VARCHAR + ':int);';
			execute += 'crt.add(*itemPessoa1:Object);';
		}
		
		// Cidade
		if($('cdCidadePessoa').value != '' && $('cdCidadePessoa').value != '0' && $('cdCidadePessoa').value != 0)	{
			objetos += 'itemCidade=sol.dao.ItemComparator(const F.cd_cidade:String, const ' + $('cdCidadePessoa').value +
			           ':String, const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
			execute += 'crt.add(*itemCidade:Object);';
		}
		// Situação
		if($('stTitulo').value >= 0)	{
			objetos += 'itemSituacao=sol.dao.ItemComparator(const stTitulo:String, const ' + $('stTitulo').value +
			           ':String,const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
			execute += 'crt.add(*itemSituacao:Object);';
		}
		$('dtInicial').value = $('dtInicial').value + ' 00:00'; 
		$('dtFinal').value   = $('dtFinal').value + ' 23:59'; 
		
		if (formValidation()) {
			createGrid(null);
			if($('tpRelatorio').value==0 || $('tpRelatorio').value==2){
				var cdVinculo = '<%=cdVinculoCliente%>';
				objetos += 'itemVinculo=sol.dao.ItemComparator(const cdVinculo:String,const '+cdVinculo+':String, const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
			}
			else{ 
				objetos += 'itemVinculo=sol.dao.ItemComparator(const cdVinculo:String,const '+<%=cdVinculoEmitente%>+':String, const ' + _EQUAL + ':int, const ' + _INTEGER + ':int);';
			}
			execute += 'crt.add(*itemVinculo:Object);';
			
			if($('tpData').value != -1){
				var fieldDate = '';
				switch(parseInt($('tpData').value, 10)) {
					case 0: fieldDate = 'CR.dt_vencimento'; break;
					case 1: fieldDate = 'CR.dt_emissao'; break;
				}
				
				// Inicial
				objetos += 'itemDtInicial=sol.dao.ItemComparator(const dtInicial:String,dtInicial:String, const ' + _LIKE + ':int, const ' + _VARCHAR + ':int);';
				execute += 'crt.add(*itemDtInicial:Object);';
				// Final
				objetos += 'itemDtFinal=sol.dao.ItemComparator(const dtFinal:String,dtFinal:String, const ' + _LIKE + ':int,const ' + _VARCHAR + ':int);';
				execute += 'crt.add(*itemDtFinal:Object);';
				
				objetos += 'itemDsCampoData=sol.dao.ItemComparator(const dsCampoData:String,const '+fieldDate+':String, const ' + _LIKE + ':int,const ' + _VARCHAR + ':int);';
				execute += 'crt.add(*itemDsCampoData:Object);';
			}
			
			formMensagem = createTempbox("jMsg", {width: 165, height: 50, message: "Aguarde... pesquisando!", tempboxType: "LOADING", time: 0});
			// BUSCANDO
			setTimeout(function()	{
				getPage('GET2POST', 'btnPesquisarOnClick', 
						'../methodcaller?className=com.tivic.manager.grl.PessoaServices' +
						'&objects=' + objetos +
						((execute != '')?'&execute=':'') + execute +
						'&method=findPessoaFactoring(*crt:java.util.ArrayList)', [$('dtInicial'),$('dtFinal')])}, 10);
		}
	}
	else {	// retorno
		try {
			$('dtInicial').value = $('dtInicial').value.split(' ')[0]; 
			$('dtFinal').value   = $('dtFinal').value.split(' ')[0]; 
		} catch(erro) {};
		var vlTotal          = 0
		    vlTotalAbatimento  = 0; 
		formMensagem.close();
		rsmPessoa = eval("(" + content + ")");
		var qt = rsmPessoa.lines.length;
		$('labelResultado').innerHTML = (qt==0)?'Nenhuma pessoa encontrada':(qt==1)?'1 pessoa encontrada':qt+' pessoas encontradas';
		createGrid(rsmPessoa);
	}
}
var vlTotalSelecao = 0;
var vlAbatimentoSelecao = 0;
var vlTotalContaSelecao = 0;
var columnsPessoa = [{label:'Cliente', reference: 'NM_PESSOA'},
					  	   {label:'Quant. Cheques', reference: 'QT_CHEQUES'},
					  	   {label:'Total Bruto', reference: 'VL_TOTAL_BRUTO', type: GridOne._CURRENCY},
					  	   {label:'Total Liquido', reference: 'VL_TOTAL_LIQUIDO', type: GridOne._CURRENCY},
					  	   {label:'Média Bruto', reference: 'VL_MEDIA_BRUTO', type: GridOne._CURRENCY},							   
						   {label:'Média Liquido', reference: 'VL_MEDIA_LIQUIDO', type: GridOne._CURRENCY}];

function createGrid(rsm){
	
	var vlTotalBruto = 0, 
	    vlTotalLiquido = 0;
	    
	var count = 0;
	var columns = columnsPessoa;
	
	if ($('tpRelatorio').value == 1)
		columns = 			[{label:'Emitente', reference: 'NM_PESSOA'},
						  	   {label:'Quant. Cheques', reference: 'QT_CHEQUES'},
						  	   {label:'Total Bruto', reference: 'VL_TOTAL_BRUTO', type: GridOne._CURRENCY},
						  	   {label:'Total Liquido', reference: 'VL_TOTAL_LIQUIDO', type: GridOne._CURRENCY},
						  	   {label:'Média Bruto', reference: 'VL_MEDIA_BRUTO', type: GridOne._CURRENCY},							   
							   {label:'Média Liquido', reference: 'VL_MEDIA_LIQUIDO', type: GridOne._CURRENCY}];
	else if ($('tpRelatorio').value == 2)
		columns = 			[{label:'Cliente', reference: 'NM_PESSOA'},
						  	   {label:'CPF/CNPJ', reference: 'NR_CPF_CNPJ'},
						  	   {label:'Limite', reference: 'VL_LIMITE', type: GridOne._CURRENCY},
						  	   {label:'Limite Utilizado', reference: 'VL_LIMITE_UTILIZADO', type: GridOne._CURRENCY},
						  	   {label:'Limite Disponível', reference: 'VL_LIMITE_DISPONIVEL', type: GridOne._CURRENCY},							   
							   {label:'Endereço', reference: 'NM_ENDERECO'},
							   {label:'Bairro', reference: 'NM_BAIRRO'},
							   {label:'Cidade', reference: 'NM_CIDADE'},
							   {label:'UF', reference: 'SG_ESTADO'},
							   {label:'CEP', reference: 'NR_CEP', },
							   {label:'Fone', reference: 'NR_TELEFONE1'},
							   {label:'Email', reference: 'NM_EMAIL'}];
	
	gridPessoa = GridOne.create('gridPessoa', {columns: columns,
														   resultset: rsm,
														   plotPlace: $('divGridPessoa'),
														   onDoubleClick: function() {viewPessoa(); },
 													       columnSeparator: true,
													       lineSeparator: false,
														   strippedLines: true,
														   onProcessRegister: function(reg){
															   if(reg['NR_CPF'] != null && reg['NR_CPF'] != ''){
																   reg['NR_CPF_CNPJ'] = reg['NR_CPF']
															   }
															   else if(reg['NR_CNPJ'] != null && reg['NR_CNPJ'] != ''){
																   reg['NR_CPF_CNPJ'] = reg['NR_CNPJ']
															   }
															   vlTotalBruto    += parseFloat(reg['VL_TOTAL_BRUTO'], 10);
															   vlTotalLiquido  += parseFloat(reg['VL_TOTAL_LIQUIDO'], 10);
															   count = count + 1;
														   },		
														   noSelectOnCreate: true});//Mudei para true
	$('vlTotalBruto').innerHTML 	= formatCurrency(vlTotalBruto);
	$('vlTotalLiquido').innerHTML   = formatCurrency(vlTotalLiquido);
	$('vlMediaBruto').innerHTML 	= formatCurrency((vlTotalBruto / count));
	$('vlMediaLiquido').innerHTML   = formatCurrency((vlTotalLiquido / count));
	
}

function viewPessoa()	{
	if(gridPessoa.getSelectedRowRegister())	{
		if($('tpRelatorio').value == 1){
			parent.miEmitenteFactoringOnClick(gridPessoa.getSelectedRowRegister()['CD_PESSOA']);			
		}
		else{
			parent.miClienteFactoringOnClick('Cliente', <%=cdVinculoCliente%>, gridPessoa.getSelectedRowRegister()['CD_PESSOA'], null, null, null, <%=cdEmpresa%>);
		}
	}
}

function btnImprimirOnClick(){
		printRelatorioReport();
}


function printRelatorioReport(){
	
	var caption;
	var className;
	var method; 
	var nomeJasper;
	
	if($('tpRelatorio').value == 0){
		caption    = "Ranking de Clientes"; 
		className  = "com.tivic.manager.adm.ContaFactoringServices";
		method 	   = "gerarRelatorioPessoas";
		nomeJasper = "relatorio_ranking_pessoa";
	}
	else if($('tpRelatorio').value == 1){
		caption    = "Ranking de Emitentes";
		className  = "com.tivic.manager.adm.ContaFactoringServices";
		method 	   = "gerarRelatorioPessoas";
		nomeJasper = "relatorio_ranking_pessoa";
	}
	else if($('tpRelatorio').value == 2){
		caption    = "Relação de Clientes";
		className  = "com.tivic.manager.adm.ContaFactoringServices";
		method 	   = "gerarRelatorioPessoas";
		nomeJasper = "relatorio_relacao_pessoa";
	}
	
	method += "(const "+$('cdEmpresa').value+":int,const "+$('tpRelatorio').value+":int, "+
			 "const "+$('dtInicial').value+":GregorianCalendar, const "+$('dtFinal').value+":GregorianCalendar, " +
			 "const "+$('nmPessoa').value+":String,const "+$('tpData').value+":int,const "+$('cdCidadePessoa').value+":int, const "+$('stTitulo').value+":int)";
			
	
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
					 "&cdEmpresa=" 	+ $('cdEmpresa').value + 
					 "&modulo=fac"});
	
	
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
			$('cdCidade' + prefixCidade + 'View').value = reg[0]['NM_CIDADE'];
		if ($('cdCidade' + prefixCidade + 'Agd') != null)
			$('cdCidade' + prefixCidade + 'Agd').value = reg[0]['CD_CIDADE'];
		if ($('cdCidade' + prefixCidade + 'AgdView') != null)
			$('cdCidade' + prefixCidade + 'AgdView').value = reg[0]['NM_CIDADE'];
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

</script>
</head>
<body class="body" onload="init();">
	<div style="width: 891px;" id="ManutencaoPessoa" class="d1-form">
   		<div style="width: 891px; height: 480px;" class="d1-body">
   			<div class="d1-toolBar" id="toolBarGrid" style="width:90px; height:83px; float:left; margin-right: 5px;"></div>
			<input idform="Pessoa" reference="cd_empresa" datatype="INT" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>" />
			<input idform="endereco" reference="sg_estado" id="sgEstado" name="sgEstado" type="hidden">
			<div style="border:1px solid #999; padding:2px 2px 2px 4px; height: 83px; margin-bottom: 2px; width:777px; float: left; margin-left: 5px;">
		 		<div class="d1-line" id="line0">
					<div style="width: 180px;" class="element">
						<label class="caption" for="gnPessoa">Relatório de</label>
						<select style="width: 175px;" datatype="INT" id="tpRelatorio" name="tpRelatorio" class="select2">
						  <option value="0">Ranking - Cliente</option>
						  <option value="1">Ranking - Emitente</option>
						  <option value="2">Relação - Cliente</option>
						</select>
					</div>	
					<div class="element" style="width:120px;">
	                    <label for="tpData" class="caption">Tipo de Data</label>
	                    <select style="width:115px" type="text" name="tpData" id="tpData" class="select2">
	                    	<option value="-1" selected="selected">Nenhum</option>
	                        <option value="0">Vencimento</option>
	                        <option value="1">Troca</option>
	                    </select>
	                </div>
					<div style="width:95px;" class="element">
	                    <label class="caption">Período</label>
	                    <input name="dtInicial" type="text" class="field2" id="dtInicial" mask="##/##/####" maxlength="10" style="width:76px; " value="<%=dtInicial%>" datatype="DATE"/>
	                    <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtInicial" class="controlButton" style="height:22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
	                </div>
	                <div style="width:95px;margin-left:3px;" class="element">
	                    <label for="dtFinal" class="caption">&nbsp;</label>
	                    <input name="dtFinal" type="text" class="field2" id="dtFinal" mask="##/##/####" maxlength="10" style="width:76px;" value="<%=dtFinal%>" datatype="DATE"/>
	                    <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtFinal" class="controlButton" style="height:22px;"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
	                </div>
	                <div style="width: 275px;margin-left:3px;" class="element">
	                  <label class="caption" for="cdCidadePessoa">Cidade</label>
	                  <input logmessage="Código Cidade" idform="pessoa" reference="cd_cidade" datatype="STRING" id="cdCidadePessoa" name="cdCidadePessoa" type="hidden"/>
	                  <input logmessage="Nome Cidade" idform="pessoa" reference="nm_cidade" style="width: 240px;" static="true" disabled="disabled" class="disabledField2" name="cdCidadePessoaView" id="cdCidadePessoaView" type="text"/>
	                  <button id="btnFindCidadePessoaEndereco" onclick="btnFindCidadeEnderecoOnClick(null, 'Pessoa')" idform="pessoa" title="Pesquisar valor para este campo..." class="controlButton controlButton2" style="height:22px;"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
	                  <button onclick="$('cdCidadePessoa').value = 0; $('cdCidadePessoaView').value = ''" idform="pessoa" title="Limpar este campo..." class="controlButton" style="height:22px;"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
	                </div>
	                <div style="width:655px;" class="element">
	                    <label for="nmPessoa" class="caption">Pessoa</label>
						<input style="width:650px; text-transform:uppercase;" type="text" name="nmPessoa" id="nmPessoa" class="field2" value=""/>
					</div>
					<div class="element" style="width:115px;">
	                    <label for="stTitulo" class="caption">Situação dos Cheques</label>
	                    <select style="width:115px" type="text" name="stTitulo" id="stTitulo" class="select2" >
	                        <option value="-1">Todas</option>
	                    </select>
	                </div>
				</div>
			</div>	
			<div class="d1-line" id="line3" >
                <div style="width: 688px;" class="element">
					<label class="caption" id="labelResultado" style="font-weight:bold;">Resultado da Pesquisa</label>
                    <div id="divGridPessoa" style="float:left; width: 884px; height:320px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
                </div>
                <div style="margin-left:10px;" class="element">
				  	<div class="element">
					  	<label class="caption">Total Bruto:</label>
		          	</div>
				  	<div style="width: 70px; text-align: right; font-weight: bold;" class="element">
					  	<label class="caption" id="vlTotalBruto">0,00</label>
		          	</div>
				  	<div class="element" style="margin-left: 15px;">
					  	<label class="caption">Total Liquido:</label>
		          	</div>
				  	<div style="width: 70px; text-align: right; font-weight: bold;" class="element">
					  	<label class="caption" id="vlTotalLiquido">0,00</label>
		          	</div>
					<div class="element" style="margin-left: 15px;">
					  	<label class="caption">Média Bruto:</label>
		          	</div>
				  	<div style="width: 70px; text-align: right; font-weight: bold;" class="element">
					  	<label class="caption" id="vlMediaBruto">0,00</label>
		          	</div>  	
		          	<div class="element" style="margin-left: 15px;">
					  	<label class="caption">Média Liquido:</label>
		          	</div>
				  	<div style="width: 70px; text-align: right; font-weight: bold;" class="element">
					  	<label class="caption" id="vlMediaLiquido">0,00</label>
		          	</div>
		        </div>
            </div>
        </div>
	</div>
</body>
</html>