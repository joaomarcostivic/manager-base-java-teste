<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<security:registerForm idForm="formAgencia"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, toolbar, form, filter, grid2.0" compress="false"/>
<script language="javascript">
var disabledFormAgencia = false;
function formValidationAgencia(){
	var campos = [];
    campos.push([$("cdBanco"), 'Banco', VAL_CAMPO_MAIOR_QUE, 0]);
    campos.push([$("nrAgencia"), 'Nº Agência', VAL_CAMPO_NAO_PREENCHIDO]);
    campos.push([$("cdCidade"), 'Cidade', VAL_CAMPO_MAIOR_QUE, 0]);
    return validateFields(campos, true, 'Campos obrigatórios ou com conteúdo inválido: ', 'cdBanco');
}

function initAgencia(){
	ToolBar.create('toolBar', {plotPlace: 'toolBar',
								orientation: 'horizontal',
								buttons: [{id: 'btnNewAgencia', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', onClick: btnNewAgenciaOnClick},
										{id: 'btnEditAgencia', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterAgenciaOnClick},
										{id: 'btnSaveAgencia', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveAgenciaOnClick},
										{id: 'btnDeleteAgencia', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteAgenciaOnClick},
										{separator: 'horizontal'},
										{id: 'btnFindAgencia', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindAgenciaOnClick}]});

    var nrTelefoneMask = new Mask("(##)####-####");
    nrTelefoneMask.attach($("nrTelefone1"));
    nrTelefoneMask.attach($("nrTelefone2"));
    nrTelefoneMask.attach($("nrFax"));

	var nrCepMask = new Mask("#####-###");
	nrCepMask.attach($("nrCep"));

    AgenciaFields = [];
    loadFormFields(["Agencia"]);
    $('cdBanco').focus()
    enableTabEmulation()
	getAllBanco(null);
}

function clearFormAgencia(){
    $("dataOldAgencia").value = "";
    disabledFormAgencia = false;
    clearFields(AgenciaFields);
    alterFieldsStatus(true, AgenciaFields, "cdBanco");
}

function btnNewAgenciaOnClick(){
    clearFormAgencia();
}

function btnAlterAgenciaOnClick(){
    disabledFormAgencia = false;
    alterFieldsStatus(true, AgenciaFields, "cdBanco");
}

function btnSaveAgenciaOnClick(content){
    if(content==null){
        if (disabledFormAgencia){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationAgencia()) {
			if($('nmPessoa').value=='')
				$('nmPessoa').value = $('cdBanco').options[$('cdBanco').selectedIndex].text+' - AG. '+$('nrAgencia').value;
			if($('nmRazaoSocial').value=='')
				$('nmRazaoSocial').value = $('cdBanco').options[$('cdBanco').selectedIndex].text;
			$('stCadastro').value = 1;
			$('stEndereco').value = 1;
            var executionDescription = $("cdAgencia").value>0 ? formatDescriptionUpdate("Agencia", $("cdAgencia").value, $("dataOldAgencia").value, AgenciaFields) : formatDescriptionInsert("Agencia", AgenciaFields);
			var construtor = "new com.tivic.manager.grl.Agencia(cdAgencia:int,cdPessoaSuperior:int,cdPais:int,nmPessoa:String,nrTelefone1:String,"+
							  "nrTelefone2:String,nrCelular:String,nrFax:String,nmEmail:String,dtCadastro:GregorianCalendar,"+
							  "gnPessoa:int,imgFoto:byte[],stCadastro:int,nmUrl:String,nmApelido:String,txtObservacao:String,"+
							  "lgNotificacao:int,idPessoa:String,cdClassificacao:int,cdFormaDivulgacao:int,dtChegadaPais:GregorianCalendar,"+
							  "nrCnpj:String,nmRazaoSocial:String,nrInscricaoEstadual:String,nrInscricaoMunicipal:String,nrFuncionarios:int,"+
							  "dtInicioAtividade:GregorianCalendar,cdNaturezaJuridica:int,tpEmpresa:int,dtTerminoAtividade:GregorianCalendar,cdBanco:int,nmContato:String,"+
							  "nrAgencia:String)";
			var endereco = "new com.tivic.manager.grl.PessoaEndereco(cdEndereco:int,cdAgencia:int,dsEndereco:String,cdTipoLogradouro:int,"+
			               "cdTipoEndereco:int,cdLogradouro:int,cdBairro:int,cdCidade:int,nmLogradouro:String,nmBairro:String,"+
						   "nrCep:String,nrEndereco:String,nmComplemento:String,nrTelefone:String,nmPontoReferencia:String,"+
						   "lgCobranca:int,const 1: int)";
			getPage("POST", "btnSaveAgenciaOnClick", "../methodcaller?className=com.tivic.manager.grl.AgenciaServices"+
					"&method="+($("cdAgencia").value>0?'update':'insert')+"("+construtor+":com.tivic.manager.grl.Agencia,"+
					endereco+":com.tivic.manager.grl.PessoaEndereco)", AgenciaFields, true, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if($("cdAgencia").value<=0)	{
            $("cdAgencia").value = content;
            ok = ($("cdAgencia").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormAgencia=true;
            alterFieldsStatus(false, AgenciaFields, "cdBanco", "disabledField");
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldAgencia").value = captureValuesOfFields(AgenciaFields);
        }
        else{
            createTempbox("jMsg", {width: 300,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

function btnFindAgenciaOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar de Agências",
									 width: 350, height: 225,
									 modal: true, allowFindAll: true, noDrag: true,
									 // Parametros do filtro
									 className: "com.tivic.manager.grl.AgenciaServices",
									 method: "find",
									 filterFields: [[{label:"Nome do Banco",reference:"NM_BANCO",datatype:_VARCHAR,comparator:_LIKE_ANY,width:70},
								 				    {label:"Agência",reference:"NR_AGENCIA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:30}]],
									 gridOptions:{columns:[{label:"Nome do Banco",reference:"NM_BANCO"},{label:"Nº Agência",reference:"NR_AGENCIA"},
														   {label:"Cidade",reference:"NM_CIDADE"}]},
									 callback: btnFindAgenciaOnClick
									});
    }
    else {// retorno
		closeWindow('jFiltro');
        disabledFormAgencia=true;
        alterFieldsStatus(false, AgenciaFields, "cdBanco", "disabledField");
		loadFormRegister(AgenciaFields, reg[0]);
        $("dataOldAgencia").value = captureValuesOfFields(AgenciaFields);
        /* CARREGUE OS GRIDS AQUI */
        $("cdBanco").focus();
    }
}

function btnDeleteAgenciaOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Agencia", $("cdAgencia").value, $("dataOldAgencia").value);
    getPage("GET", "btnDeleteAgenciaOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.AgenciaServices"+
            "&method=delete(const "+$("cdAgencia").value+":int):int", null, true, null, executionDescription);
}
function btnDeleteAgenciaOnClick(content){
    if(content==null){
        if ($("cdAgencia").value == 0)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteAgenciaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormAgencia();
        }
        else {
            createTempbox("jTemp", {width: 300, height: 75, 
                                    message: "Não foi possível excluir este registro!", time: 5000});
    	}	
	}
}

function btnFindCidadeOnClick(reg){
    if(!reg){
		FilterOne.create("jFiltro", {caption:"Pesquisar Cidades", 
									   width: 380, height: 240, top: 5, modal: true, noDrag: true,
									   className: "com.tivic.manager.grl.CidadeServices", method: "find",
									   filterFields: [[{label:"Cidade", reference:"NM_CIDADE", datatype:_VARCHAR, comparator:_LIKE_ANY, width:70, charcase:'uppercase', minlength: 3},
													   {label:"UF", reference:"SG_ESTADO", datatype:_VARCHAR, comparator:_EQUAL, width:10, charcase:'uppercase'},
													   {label:"ID", reference:"ID_CIDADE", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}]],
									   gridOptions: {columns: [{label:"UF", reference:"SG_ESTADO"},{label:"Cidade", reference:"NM_CIDADE"}],
												     strippedLines: true,
												     columnSeparator: false,
												     lineSeparator: false},
									 callback: btnFindCidadeOnClick
									});
    }
    else {// retorno
        closeWindow('jFiltro');
		document.getElementById('cdCidade').value 	  = reg[0]['CD_CIDADE'];
		document.getElementById('cdCidadeView').value = reg[0]['NM_CIDADE'];
    }
}

function getAllBanco(content)	{
    if(content==null){
		$('cdBanco').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carrengando..."));
		$('cdBanco').appendChild(newOption);
		
		getPage("GET", "getAllBanco", 
				"../methodcaller?className=com.tivic.manager.grl.BancoServices"+
				"&method=getAll()", null, true, null, null);
    }
    else {// retorno
		var rsm = eval("("+content+")");
		$("cdBanco").options[0] = new Option('Selecione o Banco', 0);
		for(i=0; i<rsm.lines.length; i++)	{
			$("cdBanco").options[i+1] = new Option(rsm.lines[i]['NR_BANCO']+'-'+rsm.lines[i]['NM_BANCO'], rsm.lines[i]['CD_BANCO']);
		}
    }
}
</script>
</head>
<body class="body" onload="initAgencia();">
<div style="width: 391px;" id="Agencia" class="d1-form">
  <div style="width: 391px; height: 242px;" class="d1-body">
    <input idform="" reference="" id="contentLogAgencia" name="contentLogAgencia" type="hidden"/>
    <input idform="" reference="" id="dataOldAgencia" name="dataOldAgencia" type="hidden"/>
    <input idform="Agencia" reference="cd_agencia" id="cdAgencia" name="cdAgencia" type="hidden"/>
    <input idform="Agencia" reference="nm_pessoa" id="nmPessoa" name="nmPessoa" type="hidden"/>
    <input idform="Agencia" reference="gn_pessoa" id="gnPessoa" name="gnPessoa" type="hidden" value="0" defaultValue="0"/>
    <input idform="Agencia" reference="dt_cadastro" name="dtCadastro" type="hidden" value="0" defaultValue="%DATE"/>
    <input idform="Agencia" reference="st_cadastro" id="stCadastro" name="stCadastro" type="hidden" value="1" defaultvalue="1"/>
    <input idform="Agencia" reference="nm_razao_social" id="nmRazaoSocial" name="nmRazaoSocial" type="hidden"/>
    <input idform="Agencia" reference="cd_endereco" id="cdEndereco" name="cdEndereco" type="hidden"/>
    <input idform="Agencia" reference="st_endereco" id="stEndereco" name="stEndereco" type="hidden" value="1" defaultvalue="1"/>
    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 388px;"></div>
    <div class="d1-line" id="line0">
      <div style="width: 250px;" class="element">
        <label class="caption" for="cdBanco">Banco</label>
        <select style="width: 232px;" class="select" idform="Agencia" reference="cd_banco" datatype="STRING" id="cdBanco" name="cdBanco">
        </select>
        <button idform="Agencia" onclick="parent.miBancoOnClick(getAllBanco);" title="Novo banco..." class="controlButton"><img alt="L" src="/sol/imagens/new-button.gif"/></button>
      </div>
      <div style="width: 80px;" class="element">
        <label class="caption" for="nrAgencia">Nº Agência</label>
        <input style="width: 77px;" class="field" idform="Agencia" reference="nr_agencia" datatype="STRING" id="nrAgencia" name="nrAgencia" maxlength="10" type="text"/>
      </div>
      <div style="width: 60px;" class="element">
        <label class="caption" for="idPessoa">ID Agência</label>
        <input style="width: 57px;" class="field" idform="Agencia" reference="id_pessoa" datatype="STRING" id="idPessoa" name="idPessoa" maxlength="20" type="text"/>
      </div>
    </div>
    <div class="d1-line" id="line1">
      <div style="width: 275px;" class="element">
        <label class="caption" for="nmContato">Contato</label>
        <input style="text-transform: uppercase; width: 272px;" lguppercase="true" class="field" idform="Agencia" reference="nm_contato" datatype="STRING" id="nmContato" name="nmContato" maxlength="50" type="text"/>
      </div>
      <div style="width: 115px;" class="element">
        <label class="caption" for="nrTelefone2">Telefone (contato)</label>
        <input style="width: 112px;" mask="(##)####-####" class="field" idform="Agencia" reference="nr_telefone2" datatype="STRING" maxlength="13" id="nrTelefone2" name="nrTelefone2" type="text"/>
      </div>
    </div>
    <div class="d1-line" id="line2">
      <div style="width: 310px;" class="element">
        <label class="caption" for="nmLogradouro">Logradouro</label>
        <input style="text-transform: uppercase; width: 307px;" lguppercase="true" class="field" idform="Agencia" reference="nm_logradouro" datatype="STRING" id="nmLogradouro" name="nmLogradouro" maxlength="100" type="text"/>
      </div>
      <div style="width: 80px;" class="element">
        <label class="caption" for="nrEndereco">Número</label>
        <input style="text-transform: uppercase; width: 77px;" lguppercase="true" class="field" idform="Agencia" reference="nr_endereco" datatype="STRING" id="nrEndereco" name="nrEndereco" maxlength="10" type="text"/>
      </div>
    </div>
    <div class="d1-line" id="line3">
      <div style="width: 140px;" class="element">
        <label class="caption" for="nmComplemento">Complemento</label>
        <input style="text-transform: uppercase; width: 137px;" lguppercase="true" class="field" idform="Agencia" reference="nm_complemento" datatype="STRING" id="nmComplemento" name="nmComplemento" maxlength="50" type="text"/>
      </div>
      <div style="width: 250px;" class="element">
        <label class="caption" for="nmBairro">Bairro</label>
        <input style="text-transform: uppercase; width: 247px;" lguppercase="true" class="field" idform="Agencia" reference="nm_bairro" datatype="STRING" id="nmBairro" name="nmBairro" maxlength="50" type="text"/>
      </div>
    </div>
    <div class="d1-line" id="line4">
	  <div style="width: 320px;" class="element">
		<label class="caption" for="cdCidadeView">Cidade</label>
		<input logmessage="Código Cidade" idform="Agencia" reference="cd_cidade" datatype="INT" id="cdCidade" name="cdCidade" type="hidden"/>
		<input logmessage="Nome Cidade" idform="Agencia" reference="nm_cidade" style="width: 317px;" static="true" disabled="disabled" class="disabledField" name="cdCidadeView" id="cdCidadeView" type="text"/>
		<button id="btnFindCidade" onclick="btnFindCidadeOnClick()" static="static" title="Pesquisar valor para este campo..." class="controlButton controlButton2"><img alt="L" src="/sol/imagens/filter-button.gif"/></button>
		<button id="btnClearCidade" static="static" title="Limpar este campo..." class="controlButton" onclick="$('cdCidadeView').value = ''; $('cdCidade').value = 0;"><img alt="X" src="/sol/imagens/clear-button.gif"/></button>
	  </div>
      <div style="width: 70px;" class="element">
        <label class="caption" for="nrCepPessoa">CEP</label>
        <input style="text-transform: uppercase; width: 67px;" lguppercase="true" mask="#####-###" logmessage="CEP - Endereço Agência" class="field" idform="Agencia" reference="nr_cep" datatype="STRING" maxlength="9" id="nrCep" name="nrCep" type="text"/>
      </div>
    </div>
    <div class="d1-line" id="line4">
      <div style="width: 110px;" class="element">
        <label class="caption" for="nrTelefone1">Telefone</label>
        <input style="width: 107px;" mask="(##)####-####" class="field" idform="Agencia" reference="nr_telefone1" datatype="STRING" maxlength="13" id="nrTelefone1" name="nrTelefone1" type="text"/>
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="nrFax">Fax</label>
        <input style="width: 107px;" mask="(##)####-####" class="field" idform="Agencia" reference="nr_fax" datatype="STRING" maxlength="13" id="nrFax" name="nrFax" type="text"/>
      </div>
      <div style="width: 170px;" class="element">
        <label class="caption" for="nmEmail">E-Mail</label>
        <input style="width: 167px;" class="field" idform="Agencia" reference="nm_email" datatype="STRING" id="nmEmail" name="nmEmail" type="text"/>
      </div>
    </div>
    <div class="d1-line" id="line5">
      <div style="width: 390px;" class="element">
        <label class="caption" for="nmUrl">Site</label>
        <input style="width: 387px;" class="field" idform="Agencia" reference="nm_url" datatype="STRING" id="nmUrl" name="nmUrl" type="text"/>
      </div>
    </div>
  </div>
</div>
</body>
</html>
