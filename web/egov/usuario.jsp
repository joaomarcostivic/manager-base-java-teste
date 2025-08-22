<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@page import="sol.util.RequestUtilities" %>
<script language="javascript" src="/sol/js/scriptaculous/prototype.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/builder.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/effects.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/dragdrop.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/controls.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/slider.js"></script>
<script language="javascript" src="/sol/js/masks.js"></script>
<script language="javascript" src="/sol/js/masks2.0.js"></script>
<script language="javascript" src="/sol/js/util.js"></script>
<script language="javascript" src="/sol/js/validacao.js"></script>
<script language="javascript" src="/sol/js/ajax.js"></script>
<script language="javascript" src="/sol/js/shortcut.js"></script>
<script language="javascript" src="/sol/js/form.js"></script>
<script language="javascript" src="/sol/js/janela2.0.js"></script>
<script language="javascript" src="/sol/js/grid2.0.js"></script>
<link href="/sol/css/form.css" rel="stylesheet" type="text/css" />
<link href="/sol/css/grid2.0.css" rel="stylesheet" type="text/css" />
<link href="/sol/css/janela2.0.css" rel="stylesheet" type="text/css" />
<%
	try {
		int cdUsuario = RequestUtilities.getParameterAsInteger(request, "cdUsuario", 0);
		com.tivic.manager.egov.TransparenciaServices.registraAcesso(request);
%>
<script type="text/javascript" src="colorDialog/color_conv.js"></script>
<script language="javascript">
var disabledFormUsuario = false;
var gridUsuarios = null;
var columnsUsuario = [{label:'Login', reference: 'nm_login'}];

function onClickUsuario() {
	if (this.register != null) {
		loadFormRegister(usuarioFields, this.register, true);
		$("dataOldUsuario").value = captureValuesOfFields(usuarioFields);
		disabledFormUsuario=true;
		alterFieldsStatus(false, usuarioFields, null, "disabledField");
	}
	else
		clearFormUsuario();
}

function loadUsuario(content, cdUsuario) {
	if (content == null) {
		getPage("GET", "loadUsuario", '../methodcaller?className=com.tivic.manager.agd.UsuarioServices'+
										   '&method=getAsResultSet(const ' + cdUsuario + ':int)',
										   null, null, null, null);
	}
	else {
		var rsmUsuarios = null;
		try { rsmUsuarios = eval("(" + content + ")"); } catch(e) { }
		if (rsmUsuarios != null && rsmUsuarios.lines != null && rsmUsuarios.lines.length>0) {
			loadFormRegister(usuarioFields, rsmUsuarios.lines[0], true);
			$("dataOldUsuario").value = captureValuesOfFields(usuarioFields);
			disabledFormUsuario=false;
			alterFieldsStatus(true, usuarioFields, null, "disabledField");
		}
	}
}

function formValidationUsuario(){
	if (!validarCampo($('nmLogin'), VAL_CAMPO_NAO_PREENCHIDO, true, 'Informe o login do usuário.', true))
		return false;
	else if (!validarCampo($('nmSenha'), VAL_CAMPO_NAO_PREENCHIDO, true, 'Informe a senha do usuário.', true))
		return false;
	else if ($('nmSenhaConfirmacao')!=null && $('nmSenha').value != $('nmSenhaConfirmacao').value){
		showMsgbox('Manager', 250, 50, 'Senha de confirmação não confere.', function() {$('nmSenhaConfirmacao').focus()});
		return false;
	}
	else
	    return true;
}

function initUsuario(){
	if ($('tpUsuario') != null && $('tpUsuario').tagName.toLowerCase() == 'select')
		loadOptions($('tpUsuario'), ["Administrador", "Operador"]);
    usuarioFields = [];
    loadFormFields(["usuario"]);
	if ($('nmUsuario') != null && !$('nmUsuario').disabled)
	    $('nmUsuario').focus();
	else if ($('nmSenha') != null && !$('nmSenha').disabled)
	    $('nmSenha').focus()
    enableTabEmulation();
	<% if (cdUsuario<=0) { %>
	loadUsuarios();
	<% } else { %>
	loadUsuario(null, <%=cdUsuario%>);
	<% } %>
}

function clearFormUsuario(){
    $("dataOldUsuario").value = "";
    disabledFormUsuario = false;
    clearFields(usuarioFields);
    alterFieldsStatus(true, usuarioFields, "nmUsuario");
}

function btnShowSenhaOnClick() {
	if ($('cdUsuario').value <= 0)
		showMsgbox('Manager', 250, 100, 'Selecione o Usuário do qual você deseja visualizar sua senha atual.');
	else
		createMsgbox("jMsg", {notForceHeight:true, caption: 'Manager', top:170, width: 250, height: 30, message: $('nmSenha').value, msgboxType: "INFO"});
}

function btnNewUsuarioOnClick(){
	gridUsuarios.unselectGrid();
    clearFormUsuario();
}

function btnAlterUsuarioOnClick(){
    disabledFormUsuario = false;
    alterFieldsStatus(true, usuarioFields, "nmUsuario");
}

function btnSaveUsuarioOnClick(content){
    if(content==null){
        if (disabledFormUsuario){
            createMsgbox("jMsg", {width: 250, height: 100, message: "Para atualizar os dados, coloque o registro em modo de edição.", msgboxType: "INFO"});
        }
        else if (formValidationUsuario()) {
			$('btnSaveUsuario').disabled = true;
			var executionDescription = '';
			if ($('btnSaveUsuario').firstChild)
				$('btnSaveUsuario').firstChild.src = '/sol/imagens/form-btSalvarDisabled16.gif';
			if($("cdUsuario").value>0)
                getPage("POST", "btnSaveUsuarioOnClick", "../methodcaller?className=com.tivic.manager.egov.TransparenciaServices" +
                                                          "&method=updateUsuario(cdUsuario: int, nmLogin: String, nmSenha: String, tpUsuario: int, stUsuario: int)", usuarioFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveUsuarioOnClick", "../methodcaller?className=com.tivic.manager.egov.TransparenciaServices"+
                                                          "&method=insertUsuario(nmLogin: String, nmSenha: String, tpUsuario: int, stUsuario: int)", usuarioFields, null, null, executionDescription);
        }
    }
    else{
        var ok = parseInt(content, 10) > 0;	
		var isInsert = $('cdUsuario').value<=0;
		$('cdUsuario').value = $('cdUsuario').value<=0 && ok ? parseInt(content, 10) : $('cdUsuario').value;
		$('btnSaveUsuario').disabled = false;
		if ($('btnSaveUsuario').firstChild)
			$('btnSaveUsuario').firstChild.src = '/sol/imagens/form-btSalvar16.gif';
		if (ok) {
			var usuarioRegister = loadRegisterFromForm(usuarioFields);
			if (isInsert)
				gridUsuarios.addLine(0, usuarioRegister, onClickUsuario, true);
			else {
				gridUsuarios.getSelectedRow().register = usuarioRegister;
				gridUsuarios.updateSelectedRow();
			}			
			$("dataOldUsuario").value = captureValuesOfFields(usuarioFields);
			disabledFormUsuario=true;
			alterFieldsStatus(false, usuarioFields, "Descrição", "disabledField");
			createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
			$("dataOldUsuario").value = captureValuesOfFields(usuarioFields);
		}
        else{
            createTempbox("jMsg", {width: 300, height: 50, message: "ERRO ao tentar gravar dados!", tempboxType: "ERROR", time: 3000});
        }
    }
}

function btnDeleteUsuarioOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Usuario", $("cdUsuario").value, $("dataOldUsuario").value);
    getPage("GET", "btnDeleteUsuarioOnClick", 
            "../methodcaller?className=com.tivic.manager.egov.TransparenciaServices"+
            "&method=deleteUsuario(const "+$("cdUsuario").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteUsuarioOnClick(content){
    if(content==null){
        if ($("cdUsuario").value == 0)
            createMsgbox("jMsg", {width: 300, height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.", msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de Usuário", width: 300, height: 75, 
                                        message: "Você tem certeza que deseja excluir este Usuário?", boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteUsuarioOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            clearFormUsuario();
            createTempbox("jTemp", {width: 300, height: 75, message: "Registro excluído com sucesso!", time: 3000});
			gridUsuarios.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, height: 75, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

function loadUsuarios(content) {
	if (content==null) {
		getPage("GET", "loadUsuarios", 
				"../methodcaller?className=com.tivic.manager.egov.TransparenciaServices"+
				"&method=getAllUsuario()");
	}
	else {
		var rsmUsuarios = null;
		try {rsmUsuarios = eval('(' + content + ')')} catch(e) {}
		gridUsuarios = GridOne.create('gridUsuarios', {columns: columnsUsuario,
					     resultset :rsmUsuarios, 
					     plotPlace : $('divGridUsuarios'),
					     onSelect : onClickUsuario,
						 onProcessRegister: function(reg) {
						 	reg['LG_AUDIO'] = reg['LG_AUDIO'] ? 1 : 0;
						 }});
	}
}

</script>
</head>
<body class="body" onload="initUsuario();">
<div style="width: 419px; height:300px" id="usuario" class="d1-form">
  <div class="d1-toolButtons" style="height:25px; margin:0 10px 0 0">
    <button title="Novo..." onclick="btnNewUsuarioOnClick();" id="btnNewUsuario" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"></button>
    <button title="Alterar..." onclick="btnAlterUsuarioOnClick();" id="btnAlterUsuario" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"></button>
    <button title="Salvar..." onclick="btnSaveUsuarioOnClick();" id="btnSaveUsuario" class="toolButton"><img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"></button>
    <button title="Excluir..." onclick="btnDeleteUsuarioOnClick();" id="btnDeleteUsuario" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"></button>
  </div>
  <div style="width: 419px; height: 300px;" class="d1-body">
    <input idform="" reference="" id="dataOldUsuario" name="dataOldUsuario" type="hidden"/>
    <input idform="usuario" reference="cd_usuario" id="cdUsuario" name="cdUsuario" type="hidden" value="0" defaultValue="0"/>
    <input idform="usuario" reference="cd_pessoa" id="cdPessoa" name="cdPessoa" type="hidden" value="0" defaultvalue="0" />
    <div class="d1-line">
      <div style="width: 407px;" class="element">
        <div id="divGridUsuarios" style="width: 407px; background-color:#FFF; height:140px; border:1px solid #000000"></div>
      </div>
    </div>
    <div class="d1-line" id="line0">
      <div style="width: 250px;" class="element">
        <label class="caption" for="">Nome:</label>
        <input lguppercase="true" style="text-transform: uppercase; width: 247px;" logmessage="Nome" class="field" idform="usuario" reference="nm_usuario" datatype="STRING" maxlength="50" id="nmUsuario" name="nmUsuario" type="text"/>
      </div>
      <div style="width: 110px;" class="element">
        <label class="caption" for="tpUsuario">Tipo:</label>
        <select style="width: 107px;" class="select" idform="usuario" reference="tp_usuario" datatype="INT" id="tpUsuario" name="tpUsuario" defaultValue="0">
        </select>
      </div>
      <div style="width: 20px;" class="element">
          <label class="caption" for="stUsuario">&nbsp;</label>
        <input name="stUsuario" type="checkbox" id="stUsuario" value="1" checked="checked" logmessage="Ativo"  idform="usuario" reference="st_usuario" defaultchecked="defaultchecked" />
      </div>
      <div style="width: 25px;" class="element">
            <label class="caption">&nbsp;</label>
          <label style="margin:3px 0px 0px 0px" class="caption">Ativo</label>
      </div>
    </div>
    <div class="d1-line" id="line0">
      <div style="width: 150px;" class="element">
        <label class="caption" for="">Login (at&eacute; 50 caracteres):</label>
        <input style="width: 147px;" logmessage="Login" class="field" idform="usuario" reference="nm_login" datatype="STRING" maxlength="50" id="nmLogin" name="nmLogin" type="text">
      </div>
      <div style="width: 130px;" class="element">
        <label class="caption" for="">Senha (at&eacute; 50 caracteres):</label>
        <input style="width: 127px;" logmessage="Senha" class="field" idform="usuario" reference="nm_senha" datatype="STRING" maxlength="50" id="nmSenha" name="nmSenha" type="password">
      </div>
      <div style="width: 130px;" class="element">
        <label class="caption" for="">Confirme a senha:</label>
        <input style="width: 127px;" logmessage="Confirmação de Senha" class="field" idform="usuario" reference="nm_senha" datatype="STRING" maxlength="50" id="nmSenhaConfirmacao" name="nmSenhaConfirmacao" type="password">
      </div>
      <div style="width: 40px;" class="element">
	        <label class="caption" for="">&nbsp;</label>
      </div>
    </div>
  </div>
</div>
</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>
