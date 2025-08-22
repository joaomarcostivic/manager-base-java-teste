<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@page import="com.tivic.manager.util.*"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.seg.*"%>
<%@page import="java.util.*"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%
	//Acrescentados
	int lgDocObrigatorio      = RequestUtilities.getParameterAsInteger(request, "lgDocObrigatorio", 0);
	int cdEmpresa             = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	int cdVinculo             = RequestUtilities.getParameterAsInteger(request, "cdVinculo", 0);	
 	int cdPessoa           	  = RequestUtilities.getParameterAsInteger(request, "cdPessoa", 0);
	
  	int tpUsuario         = session.getAttribute("usuario")!=null ? ((Usuario)session.getAttribute("usuario")).getTpUsuario() : -1;
	String nrCpf          = RequestUtilities.getParameterAsString(request, "nrCpf", "");
	String nrCnpj         = RequestUtilities.getParameterAsString(request, "nrCnpj", "");
	String nmCliente      = RequestUtilities.getParameterAsString(request, "nmCliente", "");
  	String functionReturn = RequestUtilities.getParameterAsString(request, "functionReturn", "");
	boolean findByCpf     = RequestUtilities.getParameterAsString(request, "findByCpf", "false").equals("true");
	boolean findByCnpj     = RequestUtilities.getParameterAsString(request, "findByCnpj", "false").equals("true");
%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<loader:library libraries="shortcut, form, toolbar, grid2.0, aba2.0, calendario, filter, validacao" compress="false" />
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<security:registerForm idForm="formPessoa"/>
<script language="javascript">
var isCadastroDesabilitado = false;
var functionReturn         = null;
var disabledFormPessoa     = false;
var cdVinculo              = <%=cdVinculo%>;
function init()	{


	loadOptionsFromRsm($('cdTipoEndereco'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoEnderecoDAO.getAll())%>, {fieldValue: 'cd_tipo_endereco', fieldText:'nm_tipo_endereco'});
	if($('cdVinculo')) loadOptionsFromRsm($('cdVinculo'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.VinculoDAO.getAll())%>, {fieldValue: 'cd_vinculo', fieldText:'nm_vinculo'<%if (cdVinculo>0) { %>, defaultValue:<%=cdVinculo%><% }%><%if (cdVinculo<=0) { %>, setDefaultValueFirst:true<% }%>});
	loadOptionsFromRsm($('cdTipoLogradouro'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoLogradouroDAO.getAll())%>, {fieldValue: 'cd_tipo_logradouro', fieldText:'nm_tipo_logradouro'});
	loadOptionsFromRsm($('cdTipoDocumento'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.TipoDocumentoDAO.getAll())%>, {fieldValue: 'cd_tipo_documento', fieldText:'nm_tipo_documento'});
	
	ToolBar.create('toolBar',{plotPlace: 'toolBar', orientation: 'horizontal',
		                      buttons: [{id: 'btNovo', img: '/sol/imagens/form-btNovo24.gif', label: 'Novo', title: 'Cadastrar novo cliente', onClick: btnNewPessoaOnClick, imagePosition: 'top', width: 106}, {separator: 'horizontal'},
			            				{id: 'btAlterar', img: '/sol/imagens/form-btAlterar24.gif', label: 'Alterar', title: 'Alterar dados do cliente', onClick: btnAlterPessoaOnClick, imagePosition: 'top', width: 106}, {separator: 'horizontal'},
			            				{id: 'btSalvar', img: '/sol/imagens/form-btSalvar24.gif', label: 'Gravar', title: 'Salvar dados do cliente', onClick: btnSavePessoaOnClick, imagePosition: 'top', width: 106}, {separator: 'horizontal'},
			            				{id: 'btExcluir', img: '/sol/imagens/form-btExcluir24.gif', label: 'Excluir', title: 'Excluir cliente', onClick: btnDeletePessoaOnClick, imagePosition: 'top', width: 106}, {separator: 'horizontal'},
			            				{id: 'btPesquisar', img: '/sol/imagens/form-btPesquisar24.gif', label: 'Pesquisar', title: 'Pesquisar um cliente', onClick: btnFindPessoaOnClick, imagePosition: 'top', width: 106}, {separator: 'horizontal'}
		            
		           ]});
	
	loadFormFields(["pessoa"]);
	clearFormPessoa();
	
	var dataMask = new Mask('##/##/####');

	dataMask.attach($("dtNascimento"));
	
	var nrTelefoneMask = new Mask("(##)####-####");
	nrTelefoneMask.attach($("nrTelefone1"));
	nrTelefoneMask.attach($("nrCelular"));

	if ($("nrCep") != null) {
		var nrCepMask = new Mask($("nrCep").getAttribute("mask"));
		nrCepMask.attach($("nrCep"));
	}
	
	if ($("nrCpf") != null) {
		var nrCpfMask = new Mask($("nrCpf").getAttribute("mask"));
		nrCpfMask.attach($("nrCpf"));
	}
	
	if ($("nrCnpj") != null) {
		var nrCnpjMask = new Mask($("nrCnpj").getAttribute("mask"));
		nrCnpjMask.attach($("nrCnpj"));
	}
	
	$('nmPessoa').focus();
	if($('divGridContrato')!=null)
		findContrato('{lines:[]}');
	enableTabEmulation();
	gnPessoaOnChange($('gnPessoa'));
	functionReturn = '<%=functionReturn%>';
	<%
	if(!nrCpf.equals(""))	{
		%>
		document.form.nrCpf.value = <%=nrCpf.equals("")?"\'\'":"txtFormat(\'"+nrCpf+"\',document.form.nrCpf.mascara)"%>;
		<%
		if(findByCpf)	{
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.nr_cpf_cnpj", nrCpf, ItemComparator.EQUAL, java.sql.Types.VARCHAR));
			%>
				var rsm = <%=sol.util.Jso.getStream(PessoaServices.find(criterios))%>;
				btPesquisarOnClick(rsm.lines);
				if($('btAlterar'))
					$('btAlterar').focus();
			<%
		}
	}
	
	if(!nrCnpj.equals(""))	{
		%>
		document.form.nrCnpj.value = <%=nrCnpj.equals("")?"\'\'":"txtFormat(\'"+nrCnpj+"\',document.form.nrCnpj.mascara)"%>;
		<%
		if(findByCnpj)	{
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.nr_cpf_cnpj", nrCpf, ItemComparator.EQUAL, java.sql.Types.VARCHAR));
			%>
				var rsm = <%=sol.util.Jso.getStream(PessoaServices.find(criterios))%>;
				btPesquisarOnClick(rsm.lines);
				if($('btAlterar'))
					$('btAlterar').focus();
			<%
		}
	}
	
	if(cdPessoa>0)	{
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_pessoa", String.valueOf(cdPessoa), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		%>
			var rsm = <%=sol.util.Jso.getStream(PessoaServices.find(criterios))%>;
			btPesquisarOnClick(rsm.lines);
			if($('btAlterar'))
				$('btAlterar').focus();
		<%
	}
%>
}

function formValidationPessoa(){
	var campos = [];
    campos.push([$("nmPessoa"), 'Nome', VAL_CAMPO_NAO_PREENCHIDO]);
	if ($('gnPessoa').value == 0)	{
	    campos.push([$("nrCpf"), 'CPF', VAL_CAMPO_CPF]);
	    <%=lgDocObrigatorio==1 ? "campos.push([$(\"nrCpf\"), \'CPF\', VAL_CAMPO_NAO_PREENCHIDO]);" : ""%>
	}
	else	{
		<%=lgDocObrigatorio==1 ? "campos.push([$(\"nrCnpj\"), \'CNPJ\', VAL_CAMPO_NAO_PREENCHIDO]);" : ""%>
	    campos.push([$("nrCnpj"), 'CNPJ', VAL_CAMPO_CNPJ]);
	}
    return validateFields(campos, true, 'Os campos marcados devem ser preenchidos!', 'nmPessoa');
}

function btnNewPessoaOnClick(){
    clearFormPessoa();
    $('lgAtivo').value = 1;		
}

function btnSavePessoaOnClick(content){
	if(content==null){
		if (disabledFormPessoa){
			createMsgbox("jMsg", {width: 250, height: 100, message: "Para atualizar os dados, coloque o registro em modo de edição.", msgboxType: "INFO"});
			return;
		}
		if (formValidationPessoa()) {
			var gnPessoa                  = parseInt($('gnPessoa').value, 10);
			var executionDescription      = $("cdPessoa").value > 0 ? formatDescriptionUpdate("Pessoa", $("cdPessoa").value, $("dataOldPessoa").value, pessoaFields) : formatDescriptionInsert("Pessoa", pessoaFields);
			var constructorPessoa         = "cdPessoa: int, cdPessoaSuperior: int, cdPais: int, nmPessoa: String, nrTelefone1: String, nrTelefone2: String, nrCelular: String, nrFax: String, nmEmail: String, dtCadastro: GregorianCalendar, gnPessoa: int, *imgFoto: byte[], stCadastro: int, nmUrl: String, nmApelido: String, txtObservacao: String, lgNotificacao: int, idPessoa: String, cdClassificacao: int, cdFormaDivulgacao:int, dtChegadaPais: GregorianCalendar";
			var constructorPessoaJuridica = "nrCnpj: String, nmRazaoSocial: String, nrInscricaoEstadual: String, nrInscricaoMunicipal: String, nrFuncionarios: int, dtInicioAtividade: GregorianCalendar, cdNaturezaJuridica:int, tpEmpresa : int, dtTerminoAtividade: GregorianCalendar";
			var constructorPessoaFisica   = "cdNaturalidade: int, cdEscolaridade: int, dtNascimento: GregorianCalendar, nrCpf: String, sgOrgaoRg: String, nmMae: String, nmPai: String, tpSexo: int, stEstadoCivil: int, nrRg: String, nrCnh: String, dtValidadeCnh: GregorianCalendar, dtPrimeiraHabilitacao: GregorianCalendar, tpCategoriaHabilitacao: int, tpRaca:int, lgDeficienteFisico : int, nmFormaTratamento : String, cdEstadoRg: int, dtEmissaoRg: GregorianCalendar, *blbFingerprint: byte[]";
			var constructorEndereco       = 'cdEndereco: int, cdPessoa: int, dsEndereco: String, cdTipoLogradouro: int, cdTipoEndereco: int, cdLogradouro: int, cdBairro: int, cdCidade: int, nmLogradouro: String, nmBairro: String, nrCep: String, nrEndereco: String, nmComplemento: String, nrTelefone1: String, nmPontoReferencia: String, lgCobranca: int, lgPrincipal: int'; 
			if ($('lgPrincipal') != null)
				$('lgPrincipal').value = 1;
			var objects = 'imgFoto=byte[]; '+
						  'blbFingerprint=byte[];';
			var commandExecute = 'imgFoto=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const imgFoto_' + $('cdPessoa').value + ':String);'+
								 'blbFingerprint=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const blbFingerprint_' + $('cdPessoa').value + ':String);';
			if($('imgFoto').value == '') 
				var commandExecute = 'imgFoto=null;';
			else 
				var commandExecute = 'imgFoto=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const imgFoto_' + $('cdPessoa').value + ':String);';
			
			if($('dtCadastro').value=='')
				$('dtCadastro').value = formatDateTime(new Date());
			var methodName = ((parseInt($("cdPessoa").value, 10) > 0) ? "update" : "insert")+"(new com.tivic.manager.grl.Pessoa";
			
			methodName += (gnPessoa == 0 ? "Juridica" :  "Fisica");
			methodName += "(" + constructorPessoa + ", ";
			methodName += (gnPessoa == 0 ? constructorPessoaJuridica : constructorPessoaFisica)+"):com.tivic.manager.grl.Pessoa";
			methodName += ", new com.tivic.manager.grl.PessoaEndereco(" + constructorEndereco + "):com.tivic.manager.grl.PessoaEndereco";
			methodName += ", cdEmpresa:int, cdVinculo:int"+ ($('cdPessoa').value>0?',cdVinculoOld:int':'');
			methodName += ", cdTipoDocumento:int, nrDocumento:String)";
			
			getPage("POST", "btnSavePessoaOnClick", "../methodcaller?className=com.tivic.manager.grl.PessoaServices" +
													"&execute=" + commandExecute +
													"&objects=" + objects +
													"&method=" + methodName, pessoaFields, true, null, executionDescription);
		}
	}
	else {
		if ($("cdVinculo")!=null)
			$("cdVinculoOld").value = $("cdVinculo").value;
		var result = processResult(content, 'Dados salvos com sucesso!');
		var ok = false;
		var pessoa      = result.objects['pessoa'];
		var endereco    = result.objects['endereco'];
		var cdPessoa    = $('cdPessoa').value>0 ? $('cdPessoa').value : pessoa['cdPessoa'];
		var cdEndereco  = endereco==null ? 0 : endereco['cdEndereco'];
		if (endereco != null && cdEndereco > 0 && $('cdEndereco')!=null)
			$('cdEndereco').value = cdEndereco;
		var isInsertPessoa = $("cdPessoa").value <= 0 ? true : false;
		$("cdPessoa").value = $("cdPessoa").value <= 0 ? cdPessoa : $("cdPessoa").value;

		if (result.code > 0) {
			disabledFormPessoa=true;
			alterFieldsStatus(false, pessoaFields, "gnPessoa", "disabledField2");
			createTempbox("jMsg", {width: 200, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
			$("dataOldPessoa").value = captureValuesOfFields(pessoaFields);
		}
	}
}

function btnFindCidadeOnClick(registros) {
	if(registros==null)	{
		FilterOne.create("jFiltro", {caption:'Pesquisar Cidade', 
											   width: 450, height: <%=functionReturn.equals("")?"220":"194, top: 1"%>, modal: true,
											   className: "com.tivic.manager.grl.CidadeDAO", method: "find",
											   filterFields: [[{label:"Nome da Cidade",reference:"NM_CIDADE",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80},
															   {label:"Estado", reference:"SG_ESTADO",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}]],
											   gridOptions: {columns: [{label:"Cidade", reference:"NM_CIDADE"},{label:"Estado", reference:"SG_ESTADO"}]},
											   callback: btnFindCidadeOnClick
									});
	}
	else	{
		closeWindow("jFiltro");
		$('cdCidade').value = registros[0].CD_CIDADE;
		$('cdCidadeView').value = registros[0].NM_CIDADE;
		$('cdCidadeView2').value = registros[0].SG_ESTADO;
		
		$('nmEmail').select();
		$('nmEmail').focus();
	}
}


function btnClearCidadeEnderecoOnClick(registros) {
	$('cdCidade').value = "";
	$('cdCidadeView').value = "";
	$('cdCidadeView2').value = "";
	
}

function btnAlterPessoaOnClick()	{
    disabledFormPessoa = false;
    alterFieldsStatus(true, pessoaFields, "gnPessoa");
    $('gnPessoa').disabled = $('cdPessoa').value > 0;
	if($('cdEmpresa').value <= 0)	{
		$('cdEmpresa').value = <%=cdEmpresa%>;
		
	}
	
}


function btPesquisarNaturalidadeOnClick(registros) {
	if(registros==null)	{
		FilterOne.create("jFiltro", {caption:'Pesquisar Naturalidade', 
											   width: 450, height: <%=functionReturn.equals("")?"220":"194, top: 1"%>, modal: true,
											   className: "com.tivic.manager.grl.CidadeDAO", method: "find",
											   filterFields: [[{label:"Nome da Cidade",reference:"NM_CIDADE",datatype:_VARCHAR,comparator:_LIKE_ANY,width:80},
															   {label:"Estado", reference:"SG_ESTADO",datatype:_VARCHAR,comparator:_LIKE_ANY,width:20}]],
											   gridOptions: {columns: [{label:"Cidade", reference:"NM_CIDADE"},{label:"Estado", reference:"SG_ESTADO"}]},
											   callback: btPesquisarNaturalidadeOnClick
									});
	}
	else{
		
		
		closeWindow("jFiltro");
		if(document.focus)
			document.focus();
		$('cdNaturalidade').value = registros[0].CD_CIDADE;
		
		$('cdNaturalidadeView').value = registros[0].NM_CIDADE+' - '+registros[0].SG_ESTADO;
		
		$('nrRg').focus();
	}
}

function btnClearNaturalidadeOnClick(){
	$('cdNaturalidade').value = "";
	$('cdNaturalidadeView').value = "";	
}

function clearFormPessoa(){
    $("dataOldPessoa").value = "";
    disabledFormPessoa = false;
    clearFields(pessoaFields, true);
	
    gnPessoaOnChange($('gnPessoa'));
    
 	alterFieldsStatus(true, pessoaFields, "gnPessoa");
	
 	if ($('imagePessoa') != null) {
		$('imagePessoa').src = 'preview_imagem.jsp?lgDefaultInBlank=1';
	}
		
	if ($('gnPessoa') != null && !$('gnPessoa').disabled)
		$('gnPessoa').focus();
	else if ($('nmPessoa') != null && !$('nmPessoa').disabled)
		$('nmPessoa').focus();
	

}

function loadPessoaSuperior(content, cdPessoa){
	if (content == null) {
		getPage("GET", "loadPessoaSuperior", '../methodcaller?className=com.tivic.manager.grl.PessoaServices'+
										   '&method=getAsResultSet(const ' + cdPessoa + ':int)',
										   null, true, null, null);
	}
	else {
		var rsmPessoas = null;
		try { rsmPessoas = eval("(" + content + ")"); } catch(e) {}
		
		$('cdPessoaSuperior').value = rsmPessoas.lines[0]['CD_PESSOA'];
		$('nmSuperior').value       = rsmPessoas.lines[0]['NM_PESSOA'];

	}
}

function btnFindPessoaOnClick(reg, options){
    if(!reg){
        var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60, charcase:'uppercase'},
							 {label:"Apelido", reference:"NM_APELIDO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40, charcase:'uppercase'}],
							[{label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:33, charcase:'uppercase'},
							 {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:34, charcase:'uppercase'},
							 {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:33, charcase:'uppercase'}]];
        var columnsGrid = [];
		columnsGrid.push({label:"Nome", reference:"NM_PESSOA"});
		columnsGrid.push({label:"ID", reference:"ID_PESSOA"});
    	columnsGrid.push({label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'});
		columnsGrid.push({label:"Cidade", reference:"NM_CIDADE"});
		columnsGrid.push({label:"Razão Social", reference:"NM_RAZAO_SOCIAL"});
		columnsGrid.push({label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ});
		columnsGrid.push({label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"});
		columnsGrid.push({label:'CPF', reference:'NR_CPF', type:GridOne._CPF});
		columnsGrid.push({label:"Identidade", reference:"NR_RG"});
		columnsGrid.push({label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE});
		var hiddenFields = [{reference:"qtLimite", value:50, comparator:_EQUAL, datatype:_INTEGER}];
		<% if (cdVinculo > 0) { %>
    	    hiddenFields = [{reference:"J.CD_EMPRESA", value:<%=cdEmpresa%>, comparator:_EQUAL, datatype:_INTEGER},
							{reference:"J.CD_VINCULO", value:<%=cdVinculo%>, comparator:_EQUAL, datatype:_INTEGER}];	
		<% } %>
		
    	hiddenFields.push({reference:"findEnderecoPrincipal", value:0, comparator:_EQUAL, datatype:_INTEGER});
			
		var callback = options!=null && options.callback ? options.callback : btnFindPessoaOnClick;
		var caption = options!=null && options.caption ? options.caption : 'Pesquisar Pessoas';
		FilterOne.create("jFiltro", {caption:caption, width: 650, height: 320, modal: true, noDrag: true,
									 className: 'com.tivic.manager.grl.PessoaServices', method: "find",
									   filterFields: filterFields,
									   gridOptions: {columns: columnsGrid, strippedLines: true, columnSeparator: false, lineSeparator: false},
									   hiddenFields: hiddenFields,
									   callback: callback, autoExecuteOnEnter: true});
    }
    else {// retorno
    	clearFields(pessoaFields);
		closeWindow("jFiltro");
        disabledFormPessoa=true;
        var string = '';
        loadFormRegister(pessoaFields, reg[0]);
        loadDocumentos();
        loadPessoaSuperior(null, reg[0]['CD_PESSOA_SUPERIOR']);
        alterFieldsStatus(false, pessoaFields, "gnPessoa", "disabledField2");
        $("dataOldPessoa").value = captureValuesOfFields(pessoaFields);
		
        gnPessoaOnChange($('gnPessoa'));	
		
		$("cdEmpresa").value =  $("cdEmpresa").getAttribute("defaultValue");
// 		if(reg[0]['CD_NATUREZA_JURIDICA'] > 0)
// 			$('nmNaturezaJuridica').value = reg[0]['ID_NATUREZA_JURIDICA']+' - '+reg[0]['NM_NATUREZA_JURIDICA'];
		
//       Colocar quando arrumar vinculo de pessoa

//          setTimeout('loadVinculoOfPessoa()', 10);
<%-- 		<% if (cdVinculo <= 0) { %> --%>
// 			setTimeout('loadVinculos()', 10);
<%-- 		<% } %> --%>
<%-- 		<% if (showArquivos) { %> --%>
// 			setTimeout('loadArquivos()', 10);
<%-- 		<% } %> --%>
		
    }
}

function btnDeletePessoaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Pessoa", $("cdPessoa").value, $("dataOldPessoa").value);
    getPage("GET", "btnDeletePessoaOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
            "&method=delete(const "+$("cdPessoa").value+":int):int", null, true, null, executionDescription);
}

function btnDeletePessoaOnClick(content){
    if(content==null){
        if ($("cdPessoa").value == 0)
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
                                        positiveAction: function() {setTimeout("btnDeletePessoaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content, 10)==1){
            createTempbox("jTemp", {width: 200, 
									height: 50, 
									message: "Registro excluído com sucesso!", 
									time: 3000});
            clearFormPessoa();
        }
        else
            createTempbox("jTemp", {width: 270, height: 80, message: "A exclusão deste registro está impossibilitado pelo fato de ele está sendo usado no lançamento de outras informações no Sistema, como Contas a Receber e a Pagar, Contratos, Usuários, entre outras.", time: 5000});
    }	
}

function loadDocumentos(content) {
	if (content == null && $('cdPessoa').value != 0) {
		getPage("GET", "loadDocumentos", 
				"../methodcaller?className=com.tivic.manager.grl.PessoaServices"+
				"&method=getAllDocumentosOfPessoa(const " + $('cdPessoa').value + ":int)", null, true);
	}
	else {
		var rsmDocumentos = null;
		try {rsmDocumentos = eval('(' + content + ')')} catch(e) {}

			$('cdTipoDocumento').value = rsmDocumentos.lines[0]['CD_TIPO_DOCUMENTO'];
			$('nrDocumento').value     = rsmDocumentos.lines[0]['NR_DOCUMENTO'];
		
	}
}

function gnPessoaOnChange(element)	{
	
	$('labelNmPessoa').innerHTML             = (element.value==1) ? 'Nome da Pessoa' : 'Nome Fantasia';
	$('elementPessoa').style.width           = (element.value==1) ? '320px' : '450px'; 
	$('nmPessoa').style.width                = (element.value==1) ? '315px' : '445px'; 
	$('labelNaturalidade').innerHTML         = (element.value==1) ? 'Naturalidade' : 'Razão Social';
	$('cdNaturalidadeView').style.display    = (element.value==1) ? '' : 'none';
	$('nmRazaoSocial').style.display         = (element.value==1) ? 'none' : '';
	$('elementBtNaturalidade').style.display = (element.value==1) ? '' : 'none';
	$('elementResp').style.display           = (element.value==1) ? 'none' : '';
	$('elementBtResp').style.display           = (element.value==1) ? 'none' : '';	
	$('elementNascimento').style.display  	 = (element.value==1) ? '' : 'none';
	$('elementSexo').style.display 			 = (element.value==1) ? '' : 'none';
	$('elementRg').style.display 			 = (element.value==1) ? '' : 'none';
	$('elementOrgaoExpedidor').style.display = (element.value==1) ? '' : 'none';
	$('elementUfRg').style.display           = (element.value==1) ? '' : 'none';
	$('elementEstadoCivil').style.display    = (element.value==1) ? '' : 'none';
	// CPF / CNPJ
	$('elementCpfCnpj').style.width          = (element.value==1) ? '115px' : '135px';
	$('labelNrCpfCnpj').innerHTML  			 = (element.value==1) ? 'CPF:' : 'CNPJ:';
	$('nrCpf').style.display 				 = (element.value==1) ? '' : 'none';
	$('nrCnpj').style.display 				 = (element.value==1) ? 'none' : '';
	$('nrCpf').value  						 = (element.value==1) ? $('nrCpf').value : '';
	$('nrCnpj').value 						 = (element.value==1) ? '' : $('nrCnpj').value;
}


function btnFindResponsavelOnClick(reg, options){
    if(!reg){
        var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_ANY, width:60, charcase:'uppercase'},
							 {label:"Apelido", reference:"NM_APELIDO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:40, charcase:'uppercase'}],
							[{label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:33, charcase:'uppercase'},
							 {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:34, charcase:'uppercase'},
							 {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:33, charcase:'uppercase'}]];
        var columnsGrid = [];
		columnsGrid.push({label:"Nome", reference:"NM_PESSOA"});
		columnsGrid.push({label:"ID", reference:"ID_PESSOA"});
    	columnsGrid.push({label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'});
		columnsGrid.push({label:"Cidade", reference:"NM_CIDADE"});
		columnsGrid.push({label:"Razão Social", reference:"NM_RAZAO_SOCIAL"});
		columnsGrid.push({label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ});
		columnsGrid.push({label:"Insc. Estadual", reference:"NR_INSCRICAO_ESTADUAL"});
		columnsGrid.push({label:'CPF', reference:'NR_CPF', type:GridOne._CPF});
		columnsGrid.push({label:"Identidade", reference:"NR_RG"});
		columnsGrid.push({label:"Data de cadastro", reference:"DT_CADASTRO", type: GridOne._DATE});
		var hiddenFields = [{reference:"qtLimite", value:50, comparator:_EQUAL, datatype:_INTEGER}];
		<% if (cdVinculo > 0) { %>
    	    hiddenFields = [{reference:"J.CD_EMPRESA", value:<%=cdEmpresa%>, comparator:_EQUAL, datatype:_INTEGER},
							{reference:"J.CD_VINCULO", value:<%=cdVinculo%>, comparator:_EQUAL, datatype:_INTEGER}];	
		<% } %>
		
    	hiddenFields.push({reference:"findEnderecoPrincipal", value:0, comparator:_EQUAL, datatype:_INTEGER});
			
		var callback = options!=null && options.callback ? options.callback : btnFindResponsavelOnClick;
		var caption = options!=null && options.caption ? options.caption : 'Pesquisar Pessoas';
		FilterOne.create("jFiltro2", {caption:caption, width: 600, height: 340, modal: true, noDrag: true,
									 className: 'com.tivic.manager.grl.PessoaServices', method: "find",
									   filterFields: filterFields,
									   gridOptions: {columns: columnsGrid, strippedLines: true, columnSeparator: false, lineSeparator: false},
									   hiddenFields: hiddenFields,
									   callback: callback, autoExecuteOnEnter: true});
    }
    else {// retorno
    	closeWindow("jFiltro2");
        
    	$('cdPessoaSuperior').value = reg[0]['CD_PESSOA'];
    	$('nmSuperior').value       = reg[0]['NM_PESSOA'];
    
    	$('nrCnpj').focus();
    }
}

function btnClearResponsavelOnClick(){
	$('cdPessoaSuperior').value = "";
	$('nmSuperior').value = "";	
}

</script>
</head>
<body class="body" onLoad="init();" id="pessoa2Body">
	<div style="width: 820px;" id="Pessoa" class="d1-form">
		<div style="width: 820px; height:315px;" class="d1-body"> 
			<!-- 	PESSOA -->
				<input idform="pessoa" reference="CD_PESSOA" name="cdPessoa" type="hidden" id="cdPessoa" />
				<input idform="pessoa" reference="cd_pais" id="cdPais" name="cdPais" type="hidden" value="0" defaultValue="0" />
			    <input idform="pessoa" reference="nr_telefone2" id="nrTelefone2" name="nrTelefone2" type="hidden" value="" defaultValue="" />
			    <input idform="pessoa" reference="nr_fax" id="nrFax" name="nrFax" type="hidden" value="" defaultValue="" />
			    <input idform="pessoa" reference="dt_cadastro" id="dtCadastro" name="dtCadastro" type="hidden" value="%DATE" defaultValue="%DATE" />
			    <input idform="pessoa" reference="img_foto" id="imgFoto" name="imgFoto" type="hidden" value="" defaultValue="" />
				<input idform="pessoa" reference="st_cadastro" id="stCadastro" name="stCadastro" type="hidden" value="1" defaultValue="1" />
				<input idform="pessoa" reference="nm_url" id="nmUrl" name="nmUrl" type="hidden" value="" defaultValue="" />
				<input idform="pessoa" reference="nm_apelido" id="nmApelido" name="nmApelido" type="hidden" value="" defaultValue="" />
				<input idform="pessoa" reference="txt_obervacao" id="txtObervacao" name="txtObervacao" type="hidden" value="" defaultValue="" />
				<input idform="pessoa" reference="lg_notificacao" id="lgNotificacao" name="lgNotificacao" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="id_pessoa" id="idPessoa" name="idPessoa" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="cd_classificacao" id="cdClassificacao" name="cdClassificacao" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="cd_forma_divulgacao" id="cdFormaDivulgacao" name="cdFormaDivulgacao" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="dt_chegada_pais" id="dtChegadaPais" name="dtChegadaPais" type="hidden" value="" defaultValue="" />
				
			<!-- 	PESSOA JURIDICA -->
				<input idform="pessoa" reference="nr_inscricao_estadual" id="nrInscricaoEstadual" name="nrInscricaoEstadual" type="hidden" value="" defaultValue="" />
				<input idform="pessoa" reference="nr_inscricao_municipal" id="nrInscricaoMunicipal" name="nrInscricaoMunicipal" type="hidden" value="" defaultValue="" />
				<input idform="pessoa" reference="nr_funcionarios" id="nrFuncionarios" name="nrFuncionarios" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="dt_inicio_atividade" id="dtInicioAtividade" name="dtInicioAtividade" type="hidden" value="" defaultValue="" />
				<input idform="pessoa" reference="cd_natureza_juridica" id="cdNaturezaJuridica" name="cdNaturezaJuridica" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="tp_empresa" id="tpEmpresa" name="tpEmpresa" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="dt_termino_atividade" id="dtTerminoAtividade" name="dtTerminoAtividade" type="hidden" value="" defaultValue="" />
			
			<!-- 	PESSOA FISICA -->
				<input idform="pessoa" reference="cd_escolaridade" id="cdEscolaridade" name="cdEscolaridade" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="nm_mae" id="nmMae" name="nmMae" type="hidden" value="" defaultValue="" />
			    <input idform="pessoa" reference="nm_pai" id="nmPai" name="nmPai" type="hidden" value="" defaultValue="" />
			    <input idform="pessoa" reference="dt_validade_cnh" id="dtValidadeCnh" name="dtValidadeCnh" type="hidden" value="" defaultValue="" />
			    <input idform="pessoa" reference="dt_primeira_habilitacao" id="dtPrimeiraHabilitacao" name="dtPrimeiraHabilitacao" type="hidden" value="" defaultValue="" />
				<input idform="pessoa" reference="tp_categoria_habilitacao" id="tpCategoriaHabilitacao" name="tpCategoriaHabilitacao" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="tp_raca" id="tpRaca" name="tpRaca" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="lg_deficiente_fisico" id="lgDeficienteFisico" name="lgDeficienteFisico" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="nm_forma_tratamento" id="nmFormaTratamento" name="nmFormaTratamento" type="hidden" value="" defaultValue="" />
			    <input idform="pessoa" reference="dt_emissao_rg" id="dtEmissaoRg" name="dtEmissaoRg" type="hidden" value="" defaultValue="" />
				<input idform="pessoa" reference="blb_finger_print" id="blbFingerprint" name="blbFingerprint" type="hidden" value="" defaultValue="" />
				
			<!-- 	VINCULO -->
				<input idform="pessoa" id="cdEmpresa" name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>" defaultValue="<%=cdEmpresa%>" />
				<input idform="pessoa" reference="cd_vinculo" id="cdVinculoOld" name="cdVinculoOld" type="hidden" value="0" defaultValue="0" />
			    <input idform="pessoa" reference="data_old_pessoa" id="dataOldPessoa" name="dataOldPessoa" type="hidden" />
			
			<!-- 	ENDERECO -->
				<input idform="pessoa" reference="cd_endereco" id="cdEndereco" name="cdEndereco" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="ds_endereco" id="dsEndereco" name="dsEndereco" type="hidden" />
				<input idform="pessoa" reference="cd_logradouro" id="cdLogradouro" name="cdLogradouro" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="cd_bairro" id="cdBairro" name="cdBairro" type="hidden" value="0" defaultValue="0" />
				<input idform="pessoa" reference="lg_cobranca" id="lgCobranca" name="lgCobranca" type="hidden" value="0" defaultValue="1" />
				<input idform="pessoa" reference="lg_principal" id="lgPrincipal" name="lgPrincipal" type="hidden" value="" defaultValue="1" />
				<div id="toolBar" class="d1-toolBar" style="height:50px; width: 820px;"></div>
				<div id="line0" class="d1-line">
					<div style="width:53px;" class="element">
				    	<label for="gnPessoa" class="caption">Pessoa</label>
						<select style="width:50px;" reference="GN_PESSOA" idform="pessoa" name="gnPessoa" id="gnPessoa" value="1" class="select2" defaultvalue="1" onchange="gnPessoaOnChange(this)">
							<option value="0">Jur</option>
							<option value="1">Fis</option>
						</select>    
				    </div>
					<div style="width:320px;" class="element" id="elementPessoa">
						<label for="nmPessoa" id="labelNmPessoa" class="caption">Nome da Pessoa</label>
						<input style="width:315px; text-transform: uppercase;" idform="pessoa" lguppercase="true" reference="NM_PESSOA" name="nmPessoa" type="text" class="field2" id="nmPessoa" maxlength="50" value="<%=nmCliente%>"/>
			        </div>
			        <div style="width:116px;" class="element" id="elementNascimento">
				    	<label class="caption" id = "labelNascimento" for="dtNascimento">Data nascimento</label>
			            <input style="width: 111px;" mask="##/##/####" maxlength="10" logmessage="Data Nascimento" class="field2" idform="pessoa" reference="dt_nascimento" datatype="DATETIME" id="dtNascimento" name="dtNascimento" type="text"/>
			            <button idform="pessoa" onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')" title="Selecionar data..." reference="dtNascimento" class="controlButton" style="height: 22px;"><img alt="|30|" src="/sol/imagens/date-button.gif" /></button>
				    </div>
			        <div style="width:65px;" class="element"  id="elementSexo">
						<label for="nmPessoa" class="caption">Sexo</label>
			            <select style="width:60px;" idform="pessoa" class="select2" datatype="STRING" name="tpSexo" id="tpSexo" reference="TP_SEXO" valueToClear="0">
			                <option value="">..</option>
			                <option value="0">M</option>
			                <option value="1">F</option>
			            </select>
			        </div>
					<div style="width:263px;" class="element">
						<label for="nmCidadeNaturalidade" id = "labelNaturalidade" class="caption">Naturalidade</label>
			    		<input logmessage="Código Naturalidade" idform="pessoa" reference="CD_NATURALIDADE" id="cdNaturalidade" name="cdNaturalidade" type="hidden" value="0" defaultValue="0"/>
						<input logmessage="Nome Naturalidade" idform="pessoa" reference="nm_naturalidade" style="width: 232px;" static="true" disabled="disabled" class="disabledField2" name="cdNaturalidadeView" id="cdNaturalidadeView" type="text"/>
						<input style="width:310px; display: none; text-transform: uppercase;" idform="pessoa" lguppercase="true" reference="NM_RAZAO_SOCIAL" name="nmRazaoSocial" type="text" class="field2" id="nmRazaoSocial" value=""/>	
			    	</div>
			    	<div class="element" id="elementBtNaturalidade" style="margin-top:22px;">
				    	<label class="caption">&nbsp;</label>
						<button type="button" id="btPesquisarNaturalidade" onclick="btPesquisarNaturalidadeOnClick(null)" idform="pessoa" title="Pesquisar valor para este campo..." class="controlButton controlButton2" style="height: 22px;"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
						<button onclick="btnClearNaturalidadeOnClick()" idform="pessoa" title="Limpar este campo..." class="controlButton" style="height: 22px;"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			    	</div>
			    </div>
			    <div id="line2" class="d1-line" style="margin-top:30px;">
					<div style="width:365px;" class="element" id="elementResp">
			            <label for="nmPessoa" class="caption">Colaborador Respons&aacute;vel:</label>
			            <input logmessage="Código Da Pessoa responsável pela Empresa" idform="pessoa" reference="CD_PESSOA_SUPERIOR" id="cdPessoaSuperior" name="cdPessoaSuperior" type="hidden" value="0" defaultValue="0"/>
						<input style="width:360px;" idform="pessoa" reference="NM_SUPERIOR" name="nmSuperior" type="text" class="disabledField2" id="nmSuperior" disabled="disabled" value=""/>
			        </div>
			        <div class="element" id="elementBtResp" style="margin-top:22px;">
				    	<label class="caption">&nbsp;</label>
						<button type="button" id="btnFindResponsavelOnClick" onclick="btnFindResponsavelOnClick(null)" idform="pessoa" title="Pesquisar valor para este campo..." class="controlButton controlButton2" style="height: 22px;"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
						<button onclick="btnClearResponsavelOnClick()" idform="pessoa" title="Limpar este campo..." class="controlButton" style="height: 22px;"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
			        </div>
					<div style="width: 115px;" class="element" id="elementRg">
			            <label class="caption" for="nrRg">R.G.</label>
			            <input style="text-transform: uppercase; width: 110px;" logmessage="Identidade" lguppercase="true" nologcasevalue="Identidade" class="field2" idform="pessoa" reference="nr_rg" datatype="STRING" maxlength="15" id="nrRg" name="nrRg" type="text"/>
			        </div>
			    	<div style="width:70px;" class="element"  id="elementOrgaoExpedidor">
						<label for="sgOrgaoRg" class="caption">Órg. Exp.</label>
						<select style="width:65px;" idform="pessoa" reference="SG_ORGAO_RG" name="sgOrgaoRg" class="select2" id="sgOrgaoRg">
			                <option value="">...</option>
						<%for(int i=0; i<PessoaFisicaServices.siglaOrgaoExpedidor.length; i++){%>
			                <option value="<%=PessoaFisicaServices.siglaOrgaoExpedidor[i][0]%>"><%=PessoaFisicaServices.siglaOrgaoExpedidor[i][0]%></option>
			            <%}%>
			           	</select>
			        </div>
			        
					<div style="width:65px;" class="element" id="elementUfRg">
						<label for="nmPessoa" class="caption">Uf</label>
						<select style="width:60px;" idform="pessoa" reference="CD_ESTADO_RG" name="cdEstadoRg" class="select2" id="cdEstadoRg">
			                <option value="">...</option>
						<%	for(int i=0; i<Recursos.siglasEstados.length; i++){%>
			                	<option value="<%=/*Recursos.siglasEstados[i]*/i%>"><%=Recursos.siglasEstados[i]%></option>
			            <%	}%>
			           	</select>
			        </div>
			        
			        <div style="width: 115px;" class="element" id="elementCpfCnpj">
			            <label class="caption" for="nrCpf" id="labelNrCpfCnpj">CPF</label>
			            <input style="text-transform: uppercase; width: 110px;" lguppercase="true" mask="###.###.###-##" logmessage="C.P.F." class="field2" idform="pessoa" reference="nr_cpf" datatype="STRING" maxlength="14" id="nrCpf" name="nrCpf" type="text" onblur="if (!isCPF_Valido(this.value)) {alert('CPF Inválido!'); this.select(); this.focus(); return false} else if(this.value!=this.oldValue) findByCpfCnpj(null);"/>
			        	<input style="text-transform: uppercase; width:130px;" lguppercase="true" logmessage="C.N.P.J." reference="NR_CNPJ" idform="pessoa" class="field2" name="nrCnpj" type="text" class="field2" datatype="STRING" id="nrCnpj" value="" mask="##.###.###/####-##" maxlength="18" onblur="if (!isCNPJ_Valido(this.value)) {alert('CNPJ Inválido!'); this.select(); this.focus(); return false} else if(this.value!=this.oldValue) findByCpfCnpj(null);" />
			        </div>
			
					<div style="width:140px;" class="element" id="elementEstadoCivil">
						<label for="nmPessoa" class="caption">Estado Civil</label>
						<select style="width:135px;" idform="pessoa" reference="ST_ESTADO_CIVIL" name="stEstadoCivil" class="select2" id="stEstadoCivil" valueToClear="-1">
			                	<option value="-1">Selecione...</option>
							<%for(int i=0; i<Recursos.estadoCivil.length; i++){%>
			                	<option value="<%=i%>"><%=Recursos.estadoCivil[i]%></option>
							<%}%>
			            </select>
			        </div>
				
					<div style="width: 180px;" class="element">
				        <label class="caption" for="cdTipoDocumento">Tipo de Documento</label>
				        <select style="width: 175px;" idform="pessoa" logmessage="Tipo Documento" registerclearlog="0" value="0" defaultValue="0" class="select2" idform="pessoal" reference="cd_tipo_documento" maxlength="10" id="cdTipoDocumento" name="cdTipoDocumento" type="text">
				          <option value="0">Selecione...</option>
				        </select>
				    </div>
				    
				    <div style="width: 135px;" class="element">
				        <label class="caption" for="nrDocumento">N&deg; Documento </label>
				        <input style="text-transform: uppercase; width: 130px;" lguppercase="true" logmessage="Nº Documento" class="field2" idform="pessoa" reference="nr_documento" maxlength="20" id="nrDocumento" name="nrDocumento" type="text"/>
				    </div>
				    
			    </div>
			    
				<div id="line2" class="d1-line" style="margin-top:30px;">
					
					<div style="width: 185px;" class="element">
			            <label class="caption" for="nmPessoa">Telefone</label>
			            <input style="width: 180px;" lguppercase="true" mask="(##)####-####" logmessage="Nº Telefone" class="field2" idform="pessoa" reference="nr_telefone1" datatype="STRING" maxlength="15" id="nrTelefone1" name="nrTelefone1" type="text"/>
			        </div>
						
					<div style="width: 185px;" class="element">
			            <label class="caption" for="nrCelular">Celular</label>
			            <input style="width: 180px;" lguppercase="true" mask="(##)####-####" logmessage="Nº Celular" class="field2" idform="pessoa" reference="nr_celular" datatype="STRING" maxlength="15" id="nrCelular" name="nrCelular" type="text"/>
			        </div>
					
			        <div style="width: 256px;" class="element">
			            <label class="caption" for="nmEmail">E-mail</label>
			            <input style="text-transform: lowercase; width: 251px;" logmessage="E-mail" class="field2" idform="pessoa" reference="nm_email" datatype="STRING" maxlength="256" id="nmEmail" name="nmEmail" type="text"/>
			        </div>
					
			        <div style="width: 190px;" class="element">
			                <label class="caption" for="cdVinculo">Vínculo</label>
			                <select static="static" disabled="disabled" style="width: 190px;" class="disabledSelect2" idform="pessoa" logmessage="Vínculo" datatype="STRING" id="cdVinculo" name="cdVinculo" reference = "cd_vinculo">
			                  <option value="0">Selecione...</option>
			                </select>
			        </div>
			    </div>
			    
				<div id="line2" class="d1-line" style="margin-top:30px;">
					
					<div style="width: 180px;" class="element">
			            <label class="caption" for="cdTipoEndereco">Tipo endereço</label>
			            <select logmessage="Tipo Endereço" registerclearlog="0" style="width: 175px;" class="select2" idform="pessoa" reference="cd_tipo_endereco" datatype="STRING" id="cdTipoEndereco" name="cdTipoEndereco" defaultValue="0">
			            	<option value="0">Selecione...</option>
			        	</select>
			        </div>
						
					<div style="width: 180px;" class="element">
			            <label class="caption" for="cdTipoLogradouro">Tipo logradouro</label>
			            <select logmessage="Tipo Logradouro" registerclearlog="0" style="width: 175px;" class="select2" idform="pessoa" reference="cd_tipo_logradouro" datatype="STRING" id="cdTipoLogradouro" name="cdTipoLogradouro" defaultValue="0">
			                <option value="0">Selecione...</option>
			            </select>
			        </div>	
			        
					<div style="width:395px;" class="element">
						<label for="nmLogradouro" class="caption">Endereço (Logradouro)</label>
			            <input style="width:390px;text-transform: uppercase;" reference="NM_LOGRADOURO" idform="pessoa" name="nmLogradouro" type="text" class="field2" id="nmLogradouro" value="" maxlength="50"/>
			    	</div>
			    	
					<div style="width:65px;" class="element">
						<label for="nrEndereco" class="caption">Número</label>
			            <input style="width:60px;" reference="nr_endereco" idform="pessoa" name="nrEndereco" type="text" class="field2" id="nrEndereco" value=""/>
			        </div>
					        
			    </div>
			    
			    <div id="line2" class="d1-line" style="margin-top:30px;">
			    
			    	<div style="width:410px;" class="element">
						<label for="nmComplemento" class="caption">Complemento</label>
			            <input style="width:405px;text-transform: uppercase;" reference="NM_COMPLEMENTO" idform="pessoa"  name="nmComplemento" type="text" class="field2" id="nmComplemento" value="" maxlength="50"/>
			        </div>
					
					<div style="width:410px;" class="element">
						<label for="nmBairro" class="caption">Bairro</label>
			            <input style="width:405px;text-transform: uppercase;" reference="NM_BAIRRO" idform="pessoa"  name="nmBairro" type="text" class="field2" id="nmBairro" value="" maxlength="50"/>
			        </div>
					
			   </div>
			   
			   <div id="line2" class="d1-line" style="margin-top:30px;">
			   
			       	<div style="width: 290px;" class="element">
			            <label class="caption" for="nmPontoReferencia">Ponto referência</label>
			            <input style="text-transform: uppercase; width: 285px;" lguppercase="true" logmessage="Ponto referência" class="field2" idform="pessoa" reference="nm_ponto_referencia" datatype="STRING" maxlength="256" id="nmPontoReferencia" name="nmPontoReferencia" type="text"/>
			        </div>
			    
			    	<div style="width:130px;" class="element">
						<label for="nrCep" class="caption">CEP</label>
			            <input style="width:125px;" reference="NR_CEP" name="nrCep" idform="pessoa"  type="text" class="field2" id="nrCep" value="" onKeyPress="return txtBoxFormat(this.form, this.name, this.getAttribute('mascara'), event);" maxlength="10" mascara="99.999-999" />
			        </div>
			    
			    	<input logmessage="Código Cidade Endereço" idform="pessoa" reference="CD_CIDADE" datatype="STRING" id="cdCidade" name="cdCidade" type="hidden" value="0" defaultValue="0"/>
					
					<div style="width:324px;" class="element">
						<label for="nmCidadeEndereco" class="caption">Cidade</label>
						<input logmessage="Nome Cidade" idform="pessoa" reference="NM_CIDADE" style="width: 319px;" static="true" disabled="disabled" class="disabledField2" name="cdCidadeView" id="cdCidadeView" type="text"/>
					</div>
					<div style="width:74px;" class="element">
						<label for="sgCidadeEndereco" class="caption">Estado</label>
				        <input logmessage="Nome Estado" idform="pessoa" reference="SG_ESTADO" style="width: 71px;" static="true" disabled="disabled" class="disabledField2" name="cdCidadeView2" id="cdCidadeView2" type="text"/>
						<button type="button" id="btPesquisarCidade" onclick="btnFindCidadeOnClick(null)" idform="pessoa" title="Pesquisar valor para este campo..." class="controlButton controlButton2" style="height: 22px;"><img alt="L" src="/sol/imagens/filter-button.gif"></button>
						<button onclick="btnClearCidadeEnderecoOnClick()" idform="pessoa" title="Limpar este campo..." class="controlButton" style="height: 22px;"><img alt="X" src="/sol/imagens/clear-button.gif"></button>
					</div>
			     </div>
			</div>
		</div>
	</body>
</html>
