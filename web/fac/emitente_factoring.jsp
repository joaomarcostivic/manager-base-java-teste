<%@page import="com.tivic.manager.adm.ContaReceberServices"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@taglib uri='../tlds/dotVisualInterface.tld' prefix='cpnt' %>
<%@taglib uri='../tlds/dotSecurityManager.tld' prefix='sec' %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.adm.ContratoServices"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="com.tivic.manager.grl.VinculoServices"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="java.util.GregorianCalendar"%>
<%
	try {
		int cdPessoa      		 = RequestUtilities.getParameterAsInteger(request, "cdPessoa", 0);   
		int cdVinculoEmitente    = ParametroServices.getValorOfParametroAsInteger("CD_VINCULO_EMITENTE", 0);
		Empresa empresa 		 = EmpresaServices.getDefaultEmpresa();
		boolean isCadastroRapido = RequestUtilities.getParameterAsInteger(request, "isCadastroRapido", 0)==1;
		String nrCpfCnpj 		 = RequestUtilities.getParameterAsString(request, "nrCpfCnpj", "");
		GregorianCalendar hoje = new GregorianCalendar();
%>
<loader:library libraries="toolbar, form, aba2.0, grid2.0, shortcut, report, flatbutton, filter, permission, corners, janela2.0, calendario" compress="false" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="/sol/css/principal.css" rel="stylesheet" type="text/css"/>
<link href="/sol/css/modulos.css" rel="stylesheet" type="text/css"/>
<script language="javascript" src="../js/jur.js"></script>
<script language="javascript" src="../js/alm.js"></script>
<script language="javascript" src="../js/adm.js"></script>
<script language="javascript" src="../js/grl.js"></script>
<script language="javascript" src="../js/seg.js"></script>
<script language="javascript" src="../js/srh.js"></script>
<script language="javascript" src="../js/ctb.js"></script>
<script type="text/javascript">

/*************************************************** EMITENTE FACTORING ****************************************************/
var situacaoContaReceber = <%=sol.util.Jso.getStream(ContaReceberServices.situacaoContaReceber)%>;
function init(){
	var nrCpfCnpjStr = '<%=nrCpfCnpj%>';
	var optionsTipo = [{value: 0, text: 'Selecione...'}];
	FormFactory.createFormWindow('jEmitente', 
            {caption: "Emitente", width: <%=(isCadastroRapido? 640 : 600)%>, height: <%=(isCadastroRapido? 200 : 255)%>, unitSize: '%', modal: true,noTitle: true,
			  id: 'formEmitente', loadForm: true, noDrag: true, cssVersion: '2',
			  hiddenFields: [{id:'cdEmpresaGrid', idForm: 'pessoa', reference: 'cd_empresa', value: <%=empresa.getCdEmpresa()%>},
			                 {id:'cdPessoaGrid', idForm: 'pessoa', reference: 'cd_pessoa', value: 0},
			                 {id:'cdVinculoGrid', idForm: 'pessoa', reference: 'cd_vinculo', value: <%=cdVinculoEmitente%>},
			                 {id:'dtCadastroGrid',idForm: 'pessoa', reference: 'dt_cadastro'},
			                 {id:'cdContaBancariaGrid',idForm: 'pessoa', reference: 'cd_conta_bancaria'}],
			  lines:[[
			          <%if(!isCadastroRapido){%>
			          {id: 'toolBarEmitente', type: 'toolbar', width: 100,  
					   orientation: 'horizontal',
			    		 buttons: [{id: 'btnNewEmitenteOnClick', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', title: 'Novo...', onClick: btnNewEmitenteOnClick},
			    	     		   {id: 'btnAlterEmitenteOnClick', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', title: 'Alterar...', onClick: btnAlterEmitenteOnClick},
			    	     		   {id: 'btnPesquisarEmitenteOnClick', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', title: 'Pesquisar...', onClick: btnFindEmitenteOnClick},
					    		   {id: 'btnGravarEmitenteOnClick', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnGravarEmitenteOnClick},
			    	     		   {id: 'btnDeleteEmitenteOnClick', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', title: 'Excluir...', onClick: btnDeleteEmitenteOnClick}],
			  		   },
			  		   {id:'divTabNotaFiscal', type: 'panel', width: 100, cssStyle: 'margin-top: 4px;'},
			  		   {id:'divAba1', type: 'panel', width: 100,
				  			lines: [[
					   <%}else{%>
					   {type: 'space', width: 100},
					   <%}%>
					   {id: 'nmPessoaGrid', idForm: 'pessoa', reference: 'nm_pessoa', label:'Emitente', width:51, type: 'text', charcase: 'uppercase'},//Dados do Emitente
					   {id: 'nrCpfCnpjGrid', idForm: 'pessoa', reference: 'nr_cpf_cnpj', type:'text', label:'CPF/CNPJ', width:29, value: nrCpfCnpjStr, maxLength:14},
					   {id: 'stCadastroGrid', idForm: 'pessoa', reference: 'st_cadastro', label: 'Bloqueado', width:20, type: 'checkbox'},
		
					   {id:'cdBancoGrid', idForm: 'pessoa', reference: 'cd_banco', type:'select', label:'Banco', width:<%=(isCadastroRapido? 29 : 24)%>,
				            classMethodLoad: 'com.tivic.manager.grl.BancoDAO', methodLoad: 'getAll()', 
				  		    fieldValue: 'cd_banco', fieldText: 'nm_banco', options: optionsTipo},
				  	   {id:'dtAberturaGrid', idForm: 'pessoa', reference: 'dt_abertura', type:'date', label:'Data Abertura', datatype: 'DATE', width:16},
				  	   {id: 'nrAgenciaGrid', idForm: 'pessoa', reference: 'nr_agencia', label:'N. Agencia', width:10, type: 'text'},	    
				  	   {id:'tpOperacaoGrid', idForm: 'pessoa', reference: 'tp_operacao', type:'select', label:'Tipo Operação', width:25},
		               {id: 'nrContaGrid', idForm: 'pessoa', reference: 'nr_conta', label:'N. Conta', width:15, type: 'text'},
		               {id: 'nrDvGrid', idForm: 'pessoa', reference: 'nr_dv', label:'DV', width:5, type: 'text'},
				  	   
		               {id: 'stContaGrid', idForm: 'pessoa', reference: 'st_conta', type:'select', label:'Situação', width:25, options: [{value: 1, text: 'Selecione...'}], value: 1},
		               {id: 'nrCpfCnpjGridGrid', idForm: 'pessoa', reference: 'nr_cpf_cnpj', label:'CPF/CNPJ', width:<%=(isCadastroRapido? 30 : 25)%>, type: 'text'},
		               {id: 'nmTitularGrid', idForm: 'pessoa', reference: 'nm_titular', label:'Titular', width:45, type: 'text'}
		               <%if(!isCadastroRapido){%>
						,{id:'divGridContaBancaria', width:95, height: 70, type: 'grid'},
											   
											   {id: '', type: 'panel', width: 5, height: 30,
												    elements: [{type: 'toolbutton', id: 'btnNewContaBancariaOnClick', width: 100, height: 16, onClick: function() { btnNewContaBancariaOnClick()},
															    imgSrc: '/sol/imagens/form-btNovo16.gif', margin: '5px 0 2px 3px'}, 
															   {type: 'toolbutton', id: 'btnAlterContaBancariaOnClick', width: 100, height: 16, onClick: function() { btnAlterContaBancariaOnClick()},
															    imgSrc: '/sol/imagens/form-btAlterar16.gif', margin: '0 0 2px 3px'}, 
															   {type: 'toolbutton', id: 'btnDeleteContaBancariaOnClick', width: 100, height: 16, onClick: function() { btnDeleteContaBancariaOnClick()},
															    imgSrc: '/sol/imagens/form-btExcluir16.gif', margin: '0 0 2px 3px'}]}
		                ]]},
			          	{id:'divAba2', type: 'panel', width: 100,
				  			lines: [[
									{id:'divGridContaReceber', width:99, height: 160, type: 'grid'}
				  			         ]]}
			            <%}else{%>
		  				,{type: 'space', width: 60},
		  				{id: 'btnGravarEmitenteOnClick', type: 'button', width: 20, image: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: function(){btnGravarEmitenteOnClick(null);}},
		  				{id: 'btnFecharEmitenteOnClick', type: 'button', width: 20, image: '/sol/imagens/form-btExcluir16.gif', label: 'Cancelar', onClick: function(){parent.closeWindow('jEmitenteFactoring');}}
		  				<%}%>
		  				]]});
	<%if(!isCadastroRapido){%>
		$('divTabNotaFiscal').style.cssText += 'margin-top: 0px;';
		TabOne.create('tabDocumento', {width: 592, height: 195, plotPlace: 'divTabNotaFiscal', tabPosition: ['bottom', 'left'],
		    tabs: [{caption: 'Geral', reference:'divAba1', active: true},
		           {caption: 'Contas a Receber', reference:'divAba2', image: '../adm/imagens/conta_receber16.gif'}]});
		setTimeout('createGrid(null);', 100);
	<%}%>
	loadFormFields(["contaBancaria"]);
	loadOptionsFromRsm($('cdBanco'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.BancoDAO.getAll())%>, {fieldValue: 'cd_banco', fieldText:'nm_banco'});
	loadOptions($('stConta'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.PessoaContaBancariaServices.situacaoConta)%>);
	loadOptions($('tpOperacao'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.ContaFinanceiraServices.tipoOperacao)%>);
	setTimeout('loadContaBancaria(null);', 100);
	loadFormFields(["pessoa"]);
	<%if(cdPessoa > 0){%>
		loadEmitente(null, <%=cdPessoa%>);
	<%}%>	
	$('nmPessoaGrid').focus();
	loadOptions($('tpOperacaoGrid'), <%=sol.util.Jso.getStream(com.tivic.manager.adm.ContaFinanceiraServices.tipoOperacao)%>);
	loadOptions($('stContaGrid'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.PessoaContaBancariaServices.situacaoConta)%>);
	$('dtAberturaGrid').value = '<%=Util.convCalendarString(hoje)%>';
}

function loadEmitente(content, cdPessoa){
	if (content == null) {
		getPage("GET", "loadEmitente", '../methodcaller?className=com.tivic.manager.grl.PessoaServices'+
										   '&method=getAsResultSet(const ' + cdPessoa + ':int)',
										   null, true, null, null);
	}
	else {
		var rsmPessoas = null;
		try { rsmPessoas = eval("(" + content + ")"); } catch(e) {}
		if (rsmPessoas!=null && rsmPessoas.lines && rsmPessoas.lines.length > 0)
			btnFindEmitenteOnClick(rsmPessoas.lines);
		disabledFormPessoa = true;
		alterFieldsStatus(false, pessoaFields, "pessoa");
	    
	}
}


var disabledFormPessoa = false;
function btnFindEmitenteOnClick(reg)	{
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Emitente", width: 500, height: 200, modal: true, noDrag: true,
										// Parametros do filtro
										className: "com.tivic.manager.grl.PessoaServices", method: "find",
										hiddenFields: [{reference:"J.CD_EMPRESA",value:$('cdEmpresaGrid').value,comparator:_EQUAL,datatype:_INTEGER},
										               {reference:"J.CD_VINCULO", value:$('cdVinculoGrid').value, comparator:_EQUAL, datatype:_INTEGER}],
										filterFields: [[{label:"Emitente",reference:"NM_PESSOA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:60},
														{label:"CPF",reference:"NR_CPF",datatype:_VARCHAR,comparator:_EQUAL,width:20},
														{label:"CNPJ",reference:"NR_CNPJ",datatype:_VARCHAR,comparator:_EQUAL,width:20}]],
										gridOptions:{columns:[{label:"Nome do Sacado",reference:"NM_PESSOA"},
															  {label:"ID",reference:"ID_PESSOA"},
															  {label:"Data do Cadastro",reference:"DT_CADASTRO",type:GridOne._DATE}]},
										callback: btnFindEmitenteOnClick});
    }
    else {// retorno
        closeWindow('jFiltro');
        $("cdPessoaGrid").value = reg[0]['CD_PESSOA'];
        $("nmPessoaGrid").value = reg[0]['NM_PESSOA'];
        if(reg[0]['NR_CPF'] != null)
        	$('nrCpfCnpjGrid').value = reg[0]['NR_CPF'];
        else if(reg[0]['NR_CNPJ'] != null)
        	$('nrCpfCnpjGrid').value = reg[0]['NR_CNPJ'];
        if(reg[0]['ST_CADASTRO'] == 1)
        	$('stCadastroGrid').checked = true;
        else
        	$('stCadastroGrid').checked = false;	
        disabledFormPessoa = true;
    	alterFieldsStatus(false, pessoaFields, "pessoa");
        setTimeout('loadContaBancaria(null);createGrid(null);', 10);
    }
}

function btnNewEmitenteOnClick(){
	btnClearEmitenteOnClick(null);
}

function btnAlterEmitenteOnClick(){
	disabledFormPessoa = false;
	alterFieldsStatus(true, pessoaFields, "pessoa");
    
}

function formValidationPessoa(){
	var campos = [];
    campos.push([$("nmPessoaGrid"), 'Nome', VAL_CAMPO_NAO_PREENCHIDO]);
    if (($('nrCpfCnpjGrid').value+'').length < 14)
	    campos.push([$("nrCpfCnpjGrid"), 'CPF Inválido', VAL_CAMPO_CPF]);
	else
	    campos.push([$("nrCpfCnpjGrid"), 'CNPJ Inválido', VAL_CAMPO_CNPJ]);
	return validateFields(campos, true, 'Os campos marcados devem ser preenchidos!', 'nmPessoaGrid');
}


function btnGravarEmitenteOnClick(content){
	if(content==null){
		if (disabledFormPessoa){
			createMsgbox("jMsg", {width: 250,
								  height: 100,
								  message: "Para atualizar os dados, coloque o registro em modo de edição.",
								  msgboxType: "INFO"});
		}
		else if (formValidationPessoa()) {
			var className = "com.tivic.manager.grl.PessoaServices";
			var stCadastro = ($('stCadastroGrid').checked == true) ? 1 : 0;
			if($('dtCadastroGrid').value=='')
				$('dtCadastroGrid').value = formatDateTime(new Date());
			if($("cdPessoaGrid").value > 0)
				getPage("POST", "btnGravarEmitenteOnClick", "../methodcaller?className=" +className+
														"&method=updateEmitente(cdEmpresaGrid:int, cdPessoaGrid :int, nmPessoaGrid:String, const "+stCadastro+":int, nrCpfCnpjGrid:String)", pessoaFields, true, null, null);
			else
				getPage("POST", "btnGravarEmitenteOnClick", "../methodcaller?className=" +className+
														"&method=insertEmitente(cdEmpresaGrid:int, nmPessoaGrid:String, const "+stCadastro+":int, nrCpfCnpjGrid:String)", pessoaFields, true, null, null);
		}
	}
	else {
		var result = processResult(content, 'Dados salvos com sucesso!');
		var ok = false;
		if (result.code > 0) {
			disabledFormPessoa=true;
			alterFieldsStatus(false, pessoaFields, "pessoa", "disabledField");
			$("cdPessoaGrid").value = result.code;
			createTempbox("jMsg", {width: 200, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
			if($('cdBancoGrid').value > 0 || $('cdBanco').value > 0){
				btnSaveContaBancariaOnClick(null, true, result);
			}
		}
		
	}
	
}

function btnDeleteEmitenteOnClickAux(content){
    getPage("GET", "btnDeleteEmitenteOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
            "&method=delete(const "+$("cdPessoaGrid").value+":int):int", null, true, null, null);
}

function btnDeleteEmitenteOnClick(content){
    if(content==null){
    	if ($("cdPessoaGrid").value == 0)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 50, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteEmitenteOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content, 10)==1){
            createTempbox("jTemp", {width: 200, 
									height: 50, 
									message: "Registro excluído com sucesso!", 
									time: 3000});
            btnClearEmitenteOnClick();
        }
        else
            createTempbox("jTemp", {width: 270, height: 80, message: "A exclusão deste registro está impossibilitado pelo fato de ele está sendo usado no lançamento de outras informações no Sistema, como Contas a Receber e a Pagar, Contratos, Usuários, entre outras.", time: 5000});
    }	
}

function btnClearEmitenteOnClick(reg)	{
	disabledFormPessoa = false;
	alterFieldsStatus(true, pessoaFields, "pessoa");
    $("cdPessoaGrid").value = 0;
    $("nmPessoaGrid").value = "";
    $('nrCpfCnpjGrid').value = "";
    $('stCadastroGrid').checked = false;	
    setTimeout('loadContaBancaria(null);', 10);
}

/***************************                      Conta Banc?ria                       ********************************/
var columnsContaBancaria = [{label:'Situação', reference: 'CL_ST_CONTA'}, {label:'Banco', reference: 'NM_BANCO'}, 
                            {label:'Agência', reference: 'NR_AGENCIA'}, {label:'Nº da Conta', reference: 'CL_NUMERO'}, 
							{label:'Nº CPF/CNPJ', reference: 'NR_CPF_CNPJ'}, {label:'Titular da Conta', reference: 'NM_TITULAR'},
							{label:'Data de Abertura', reference: 'DT_ABERTURA', type: GridOne._DATE}];

function loadContaBancaria(content) {
	if (content==null && $('cdPessoaGrid').value != 0) {
		getPage("GET", "loadContaBancaria", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
				"&method=getAllContaBancaria(const " + $('cdPessoaGrid').value + ":int)", null, true);
	}
	else {
		var rsmContaBancaria = eval('(' + content + ')');
		
		if(rsmContaBancaria != null && rsmContaBancaria.lines.length > 0){
			$('cdContaBancariaGrid').value = rsmContaBancaria.lines[0]['CD_CONTA_BANCARIA'];
			$('cdBancoGrid').value = rsmContaBancaria.lines[0]['CD_BANCO']; 
			if(rsmContaBancaria.lines[0]['DT_ABERTURA'])
				$('dtAberturaGrid').value = rsmContaBancaria.lines[0]['DT_ABERTURA'].substring(0, 10);
			else
				$('dtAberturaGrid').value = "";
			$('nrAgenciaGrid').value = rsmContaBancaria.lines[0]['NR_AGENCIA'];
			$('tpOperacaoGrid').value = rsmContaBancaria.lines[0]['TP_OPERACAO'];
			$('nrContaGrid').value = rsmContaBancaria.lines[0]['NR_CONTA'];
			$('nrDvGrid').value = rsmContaBancaria.lines[0]['NR_DV'];
			$('stContaGrid').value = rsmContaBancaria.lines[0]['ST_CONTA'];
			$('nrCpfCnpjGridGrid').value = rsmContaBancaria.lines[0]['NR_CPF_CNPJ'];
			$('nmTitularGrid').value = rsmContaBancaria.lines[0]['NM_TITULAR'];
		}
		else{
			$('cdContaBancariaGrid').value = 0;
			$('cdBancoGrid').value = 0; 
			$('dtAberturaGrid').value = '<%=Util.convCalendarString(hoje)%>'; 
			$('nrAgenciaGrid').value = '';
			$('tpOperacaoGrid').value = 0;
			$('nrContaGrid').value = '';
			$('nrDvGrid').value = '';
			$('stContaGrid').value = 1;
			$('nrCpfCnpjGridGrid').value = '';
			$('nmTitularGrid').value = '';
		}
		
		gridContaBancaria = GridOne.create('gridContaBancaria', {columns: columnsContaBancaria,
												                 resultset: rsmContaBancaria, 
												                 plotPlace: $('divGridContaBancaria'),
												                 onProcessRegister: function(reg)	{
																						var situacaoConta = <%=sol.util.Jso.getStream(com.tivic.manager.grl.PessoaContaBancariaServices.situacaoConta)%>;
																						reg['CL_ST_CONTA'] = situacaoConta[reg['ST_CONTA']];
																						reg['CL_NUMERO'] = reg['NR_CONTA']+'-'+reg['NR_DV'];
																					}
																});
	}
}

var gridContaBancaria = null;
var formContaBancaria = null;
function formValidationContaBancaria(isPrincipal){
	var campos = [];
	if(!isPrincipal){
	    campos.push([$("cdBanco"), 'Banco', VAL_CAMPO_MAIOR_QUE, 0]);
	    campos.push([$("nrAgencia"), 'Nº Agência', VAL_CAMPO_NAO_PREENCHIDO]);
	    campos.push([$("nrConta"), 'Nº Conta', VAL_CAMPO_NAO_PREENCHIDO]);
	    campos.push([$("nrDv"), 'Dígito Verificador', VAL_CAMPO_NAO_PREENCHIDO]);
	    campos.push([$("tpOperacao"), 'N¦ Ag?ncia', VAL_CAMPO_MAIOR_QUE, -1]);
		if (($('nrCpfCnpj').value+'').length < 14)
		    campos.push([$("nrCpfCnpj"), 'CPF Inválido', VAL_CAMPO_CPF]);
		else
		    campos.push([$("nrCpfCnpj"), 'CNPJ Inválido', VAL_CAMPO_CNPJ]);
		if(($('nrCpfCnpj').value+'').length > 0) {
	    	campos.push([$("nmTitular"), 'Nome do titular da conta', VAL_CAMPO_NAO_PREENCHIDO]);
		}
		return validateFields(campos, true, 'Os campos marcados devem ser preenchidos!', 'cdBanco');
	}
	else{
		
		var camposInvalidos = "";
		if($("cdBancoGrid").value ==0)
			camposInvalidos += "Banco, ";
		if($("nrAgenciaGrid").value =='')
			camposInvalidos += "N. de Agência, ";
		if($("nrContaGrid").value =='')
			camposInvalidos += "N. da conta, ";
		if($("nrDvGrid").value =='')
			camposInvalidos += "N. de DV, ";
		if($("tpOperacaoGrid").value ==-1)
			camposInvalidos += "Tipo de Operação, ";
		if(camposInvalidos!=""){
			camposInvalidos = camposInvalidos.substring(0, camposInvalidos.length-2);
			return {RET: false, CAMPOS: camposInvalidos};
		}
		else
			return {RET: true};
	}
    
}
function clearFormContaBancaria(){
    $("dataOldContaBancaria").value = "";
    clearFields(contaBancariaFields);
}
function btnNewContaBancariaOnClick(){
    if ($('cdPessoaGrid').value == 0)	{
		showMsgbox('Manager', 300, 50, 'Inclua ou localize uma pessoa para cadastrar uma conta bancária.');
		return;
	}
    clearFormContaBancaria();
	formContaBancaria = createWindow('jContaBancaria', {caption: "Conta bancária",
								  width: 390,
								  height: 158,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'panelContaBancaria'});
    $('cdBanco').focus();
}

function btnAlterContaBancariaOnClick()	{
	if(gridContaBancaria.getSelectedRowRegister()==null)	{
		showMsgbox('Manager', 300, 50, 'Selecione a conta bancária que deseja alterar.');
		return;
	}
	loadFormRegister(contaBancariaFields, gridContaBancaria.getSelectedRowRegister());
	formContaBancaria = createWindow('jContaBancaria', {caption: "Conta bancária",
								  width: 390,
								  height: 158,
								  noDropContent: true,
								  modal: true,
								  contentDiv: 'panelContaBancaria'});
    $('cdBanco').focus();
}

function btnSaveContaBancariaOnClick(content, isPrincipal, result){
	if(content==null){
    	if(isPrincipal){
    		retorno = formValidationContaBancaria(isPrincipal);
    		if (retorno.RET) {
    			if($("cdContaBancariaGrid").value>0)
	                getPage("POST", "btnSaveContaBancariaOnClick", "../methodcaller?className=com.tivic.manager.grl.PessoaContaBancariaDAO"+
	                                                          "&method=update(new com.tivic.manager.grl.PessoaContaBancaria(cdContaBancariaGrid: int,const "+$('cdPessoaGrid').value+" : int, cdBancoGrid: int, nrContaGrid: String, nrDvGrid: String, nrAgenciaGrid: String, tpOperacaoGrid: int, nrCpfCnpjGridGrid: String, nmTitularGrid: String, stContaGrid: int, dtAberturaGrid: GregorianCalendar):com.tivic.manager.grl.PessoaContaBancaria)", pessoaFields, true, [isPrincipal, result], null);
	            else{
	            	getPage("POST", "btnSaveContaBancariaOnClick", "../methodcaller?className=com.tivic.manager.grl.PessoaContaBancariaDAO"+
	                                                          "&method=insert(new com.tivic.manager.grl.PessoaContaBancaria(cdContaBancariaGrid: int,const "+$('cdPessoaGrid').value+" : int, cdBancoGrid: int, nrContaGrid: String, nrDvGrid: String, nrAgenciaGrid: String, tpOperacaoGrid: int, nrCpfCnpjGridGrid: String, nmTitularGrid: String, stContaGrid: int, dtAberturaGrid: GregorianCalendar):com.tivic.manager.grl.PessoaContaBancaria)", pessoaFields, true, [isPrincipal, result], null);
	            }
    		}
    		else{
    			showMsgbox('Manager', 300, 50, 'Conta Bancária não criada, faltaram: '+retorno.CAMPOS+'. Tente novamente!');
    			loadContaBancaria(null);
    		}
        }
    	else if (formValidationContaBancaria(isPrincipal)) {
    		var executionDescription = $("cdContaBancaria").value>0 ? formatDescriptionUpdate("ContaBancaria", $("cdContaBancaria").value, $("dataOldContaBancaria").value, contaBancariaFields) : formatDescriptionInsert("ContaBancaria", contaBancariaFields);
            if($("cdContaBancaria").value>0)
                getPage("POST", "btnSaveContaBancariaOnClick", "../methodcaller?className=com.tivic.manager.grl.PessoaContaBancariaDAO"+
                                                          "&method=update(new com.tivic.manager.grl.PessoaContaBancaria(cdContaBancaria: int,const "+$('cdPessoaGrid').value+" : int, cdBanco: int, nrConta: String, nrDv: String, nrAgencia: String, tpOperacao: int, nrCpfCnpj: String, nmTitular: String, stConta: int, dtAbertura: GregorianCalendar):com.tivic.manager.grl.PessoaContaBancaria)", contaBancariaFields, true, null, executionDescription);
            else
                getPage("POST", "btnSaveContaBancariaOnClick", "../methodcaller?className=com.tivic.manager.grl.PessoaContaBancariaDAO"+
                                                          "&method=insert(new com.tivic.manager.grl.PessoaContaBancaria(cdContaBancaria: int,const "+$('cdPessoaGrid').value+" : int, cdBanco: int, nrConta: String, nrDv: String, nrAgencia: String, tpOperacao: int, nrCpfCnpj: String, nmTitular: String, stConta: int, dtAbertura: GregorianCalendar):com.tivic.manager.grl.PessoaContaBancaria)", contaBancariaFields, true, null, executionDescription);
            
        }
    	
    }
    else{
    	if(isPrincipal){
	    	result = isPrincipal[1];
	    	isPrincipal = isPrincipal[0];
    	}
    	else
    		isPrincipal = false;
    	var ok = false;
    	if($("cdContaBancaria").value<=0 && !isPrincipal)	{
        	$("cdContaBancaria").value = content;
        }
        else if($("cdContaBancariaGrid").value<=0 && isPrincipal)	{
        	$("cdContaBancariaGrid").value = content;
        }
        
        ok = (parseInt(content, 10) > 0);
        
        if(ok){
			loadContaBancaria(null);
			if(formContaBancaria)
				formContaBancaria.close();
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldContaBancaria").value = captureValuesOfFields(contaBancariaFields);
        }
        else{
            createTempbox("jMsg", {width: 200,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
        <%if(isCadastroRapido){%>
			parent.btnFindNrCpfCnpjOnClickAux("{lines:[{CD_PESSOA: "+result.code+", NM_PESSOA: '"+$("nmPessoaGrid").value.toUpperCase()+"', NR_CPF_CNPJ: '"+$("nrCpfCnpjGrid").value+"'}]}");
			parent.closeWindow('jEmitenteFactoring');
		<%}%>
    }
}

function btnDeleteContaBancariaOnClick(content){
    if(content==null){
        if (!gridContaBancaria.getSelectedRowRegister())
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhum registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteContaBancariaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 200, 
                                  height: 50, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            loadContaBancaria(null);
        }
        else
            createTempbox("jTemp", {width: 350, 
                                  height: 50, 
                                  message: "Não foi possível excluir este registro! Provavelmente existem dados relacionados.", 
                                  time: 5000});
    }	
}

function btnDeleteContaBancariaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("ContaBancaria", $("cdContaBancaria").value, $("dataOldContaBancaria").value);
    getPage("GET", "btnDeleteContaBancariaOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.PessoaContaBancariaDAO"+
            "&method=delete(const "+gridContaBancaria.getSelectedRowRegister()['CD_CONTA_BANCARIA']+":int,const "+$('cdPessoaGrid').value+" : int):int", null, true, null, executionDescription);
}

var columnsContaReceber = [{label:'Cliente', reference: 'NM_PESSOA'},
                           {label:'Nº Documento', reference: 'NR_DOCUMENTO'}, 
                           {label:'Emissão', reference: 'DT_EMISSAO', type: GridOne._DATE},							   
						   {label:'Vencimento', reference: 'DT_VENCIMENTO', type: GridOne._DATE},							   
						   {label:'Recebida em', reference: 'DT_RECEBIMENTO', type: GridOne._DATE},
  						   {label:'Valor conta', reference: 'VL_CONTA', type: GridOne._CURRENCY},
  						   {label:'Abatimento', reference: 'VL_ABATIMENTO', type: GridOne._CURRENCY},
						   {label:'Recebido', reference: 'VL_RECEBIDO', style: 'color:#0000FF; text-align:right;', type: GridOne._CURRENCY},
						   {label:'A Receber', reference: 'VL_ARECEBER', style: 'color:#FF0000; text-align:right;', type: GridOne._CURRENCY},
  					   	   {label:'Inst. Financeira', reference: 'CL_INST_FINANCEIRA'}, 
  					   	   {label:'Agência', reference: 'NR_AGENCIA'},
  					   	   {label:'Emissão', reference: 'CL_EMISSAO'},
  					   	   {label:'Circulação', reference: 'CL_CIRCULACAO'},
  					       {label:'Tipo do Emissor', reference: 'CL_TP_EMISSOR'},
  					       {label:'Quant. Dias', reference: 'QT_DIAS'},
  					   	   {label:'Juros (%)', reference: 'PR_JUROS'}];

function createGrid(content)	{
	if (content==null && $('cdPessoaGrid').value != 0) {
		var method = "getAllContasReceberOfCliente";
		getPage("GET", "createGrid", 
				"../methodcaller?className=com.tivic.manager.adm.ContaFactoringServices"+
				"&method="+method+"(const " + $('cdPessoaGrid').value + ":int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')}catch(e) {}
		gridContasReceber = GridOne.create('gridContaReceber', {columns: columnsContaReceber,
															   resultset: rsm,
															   plotPlace: $('divGridContaReceber'),
	 													       columnSeparator: true,
														       lineSeparator: false,
															   strippedLines: true,
															   onProcessRegister: function(reg){
															   		var dtVencimento = getAsDate(reg['DT_VENCIMENTO']);
																	var dataAtual = new Date();
																	dataAtual = dataAtual.setTime(<%=hoje.getTimeInMillis()%>);
																	var stConta = reg['ST_CONTA'];
																	if(stConta != null)	{
																		reg['CL_SITUACAO'] = situacaoContaReceber[stConta];
																		if (parseInt(stConta, 10) == <%=ContaReceberServices.ST_EM_ABERTO%>)
																			if (dtVencimento < dataAtual) {
																				reg['CL_SITUACAO'] = 'Vencida';
																				reg['ST_CONTA'] = 99;
																			}
																	}
																	reg['VL_ACRESCIMO']  = reg['VL_ACRESCIMO']==null ? 0 : reg['VL_ACRESCIMO'];
																	reg['VL_ABATIMENTO'] = reg['VL_ABATIMENTO']==null ? 0 : reg['VL_ABATIMENTO'];
																	reg['CL_VENCIMENTO'] = reg['DT_VENCIMENTO']==null ? null : reg['DT_VENCIMENTO'].split(' ')[0];
																    reg['VL_ARECEBER']   = reg['VL_CONTA'] + reg['VL_ACRESCIMO'] - reg['VL_ABATIMENTO'] - reg['VL_RECEBIDO'];
																    if(reg['VL_ARECEBER'] < 0)
																    	reg['VL_ARECEBER'] = 0;
																	switch(parseInt(reg['ST_CONTA'], 10)) {
																   		case <%=ContaReceberServices.ST_RECEBIDA%>  : reg['CL_SITUACAO_cellStyle'] = 'color:#00FF00;'; break;
																   		case <%=ContaReceberServices.ST_PRORROGADA%>: reg['CL_SITUACAO_cellStyle'] = 'color:#FF9900;'; break;
																   		case <%=ContaReceberServices.ST_CANCELADA%> : reg['CL_SITUACAO_cellStyle'] = 'color:#0000FF;'; break;
																   		case 99                                     : reg['CL_SITUACAO_cellStyle'] = 'color:#FF0000;'; break;
																    }
															   },		
															   noSelectOnCreate: true});//Mudei para tru
	}
}


</script>
</head>
<body style="margin:0px; background:#F5F5F5;" onLoad="init()">
<!--
 *********************************************************************************************************************************
 ******************************************************** Conta Bancária *********************************************************
 *********************************************************************************************************************************
  -->
<div style="width: 386px; display:none;" id="panelContaBancaria" class="d1-form">
  <div style="width: 386px; height: 405px;" class="d1-body">
    <input idform="" reference="" id="contentLogContaBancaria" name="contentLogContaBancaria" type="hidden">
    <input idform="" reference="" id="dataOldContaBancaria" name="dataOldContaBancaria" type="hidden">
    <input idform="contaBancaria" reference="cd_conta_bancaria" id="cdContaBancaria" name="cdContaBancaria" type="hidden">
    <div class="d1-line" id="line0">
      <div style="width: 215px;" class="element">
        <label class="caption" for="cdBanco">Banco</label>
        <select style="width: 210px;" class="select" idform="contaBancaria" reference="cd_banco" datatype="STRING" id="cdBanco" name="cdBanco">
          <option value="0">Selecione...</option>
        </select>
      </div>
      <div style="width: 80px;" class="element">
        <label class="caption" for="dtAbertura">Data de Abertura</label>
        <input style="width: 72px;" mask="##/##/####" maxlength="10" defaultvalue="%DATE" class="field" logmessage="Data abertura" idform="contaBancaria" reference="dt_abertura" datatype="DATE" id="dtAbertura" name="dtAbertura" type="text"/>
        <button onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" title="Selecionar data..." reference="dtAbertura" class="controlButton"><img alt="|30|" src="/sol/imagens/date-button.gif"></button>
      </div>
      <div style="width: 85px;" class="element">
        <label class="caption" for="nrAgencia">Nº Agência</label>
        <input style="width: 82px;" class="field" idform="contaBancaria" reference="nr_agencia" datatype="STRING" id="nrAgencia" name="nrAgencia" type="text">
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 170px;" class="element">
        <label class="caption" for="tpOperacao">Operação</label>
        <select style="width: 167px;" class="select" idform="contaBancaria" reference="tp_operacao" datatype="STRING" id="tpOperacao" name="tpOperacao">
        </select>
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="nrConta">Nº C/C</label>
        <input style="width: 107px;" class="field" idform="contaBancaria" reference="nr_conta" datatype="STRING" id="nrConta" name="nrConta" type="text"/>
      </div>
      <div style="width: 25px;" class="element">
        <label class="caption" for="nrDv">DV</label>
        <input style="width: 22px;" class="field" idform="contaBancaria" reference="nr_dv" datatype="STRING" id="nrDv" name="nrDv" type="text" maxlength="1"/>
      </div>
      <div style="width: 73px;" class="element">
        <label class="caption" for="stConta">Situação</label>
        <select style="width: 73px;" class="select" idform="contaBancaria" reference="st_conta" datatype="STRING" id="stConta" name="stConta" defaultValue = "1">
        </select>
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="position:relative; float:left; border:1px solid #999; padding:4px; margin-top:5px; _height:31px;">
        <div style="position:absolute; top:-6px; background-color:#F4F4F4; left:5px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">Quando o titular for diferente da pessoa da conta</div>
        <div style="width: 105px;" class="element">
          <label class="caption" for="nrCpfCnpj">Nº CPF/CNPJ</label>
          <input style="width: 102px;" class="field" idform="contaBancaria" reference="nr_cpf_cnpj" datatype="STRING" id="nrCpfCnpj" name="nrCpfCnpj" type="text" maxlength="18">
        </div>
        <div style="width: 263px;" class="element">
          <label class="caption" for="nmTitular">Titular da conta</label>
          <input style="width: 263px;" class="field" idform="contaBancaria" reference="nm_titular" datatype="STRING" id="nmTitular" name="nmTitular" type="text" maxlength="50">
        </div>
      </div>
    </div>
    <div class="d1-line" id="" style="float:right; width:171px; margin:2px 0px 0px 0px;">
      <div style="width:82px;" class="element">
        <button id="btnSaveContaCategoria" title="Gravar conta" onclick="btnSaveContaBancariaOnClick(null);" style="width:80px; height:25px; border:1px solid #999;" class="toolButton"> <img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>&nbsp;Gravar</button>
      </div>
      <div style="width:80px;" class="element">
        <button id="btnCancelar" title="Voltar para a janela anterior" onclick="formContaBancaria.close();" style="width:80px; height:25px; border:1px solid #999;" class="toolButton"> <img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>&nbsp;Cancelar</button>
      </div>
    </div>
  </div>
</div>
<%
	}
	catch(Exception e)	{
		e.printStackTrace(System.out);
	}
%>
</body>
</html>