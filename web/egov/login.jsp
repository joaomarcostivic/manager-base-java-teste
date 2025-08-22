<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.security.StatusPermissionActionUser"%>
<%@page import="sol.security.User"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<script language="javascript" src="/sol/js/scriptaculous/prototype.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/builder.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/effects.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/dragdrop.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/controls.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/slider.js"></script>
<script language="javascript" src="/sol/js/masks.js"></script>
<script language="javascript" src="/sol/js/util.js"></script>
<script language="javascript" src="/sol/js/validacao.js"></script>
<script language="javascript" src="/sol/js/ajax.js"></script>
<script language="javascript" src="/sol/js/shortcut.js"></script>
<script language="javascript" src="/sol/js/form.js"></script>
<script language="javascript" src="/sol/js/janela2.0.js"></script>
<link href="/sol/css/form.css" rel="stylesheet" type="text/css" />
<script language="javascript">
<%
	try {
		String msg = RequestUtilities.getParameterAsString(request, "msg", "");
		boolean parentUser = RequestUtilities.getParameterAsInteger(request, "parentUser", 0)==1;
		String callback = RequestUtilities.getParameterAsString(request, "callback", "");
%>

var callback =  <%=(!callback.equals(""))?callback:"null"%>;

function init()	{
	enableTabEmulation();
	$('nmSenha').nextElement = $('btnLogin');
	$('nmLogin').focus();
	if ($('parentUser').value=='true') {
		$('nmLogin').value = parent.$('nmLogin').value;
		btnLoginOnClick('', {usuario: {cdUsuario: parent.$('cdUsuario').value, tpUsuario: parent.$('tpUsuario').value, nmLogin: parent.$('nmLogin').value}, 
						               nmUsuario: parent.$('nmUsuario').value});
	}
}

function validateUser() {
	var fields = [[$("nmLogin"), 'Login', VAL_CAMPO_NAO_PREENCHIDO],
				  [$("nmSenha"), 'Senha', VAL_CAMPO_NAO_PREENCHIDO]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmLogin', 210, 50);
}

var jLoginMsg;
function btnLoginOnClick(content, infoForce) {
	if (content==null) {
		if (validateUser()) {
			var fields = [$('nmLogin'), $('nmSenha')];
			getPage("POST", "btnLoginOnClick", "../methodcaller?className=com.tivic.manager.egov.TransparenciaServices"+
					"&method=loginAndUpdateSession(session:javax.servlet.http.HttpSession, nmLogin:String, nmSenha:String)" +
					"&noRegisterLog=1", fields, null, null);
			$('btnLogin').disabled = true;
			createTempbox("jLoginMsg", {width: 210, height: 50, message: "Autenticando usuário. Aguarde...", tempboxType: "LOADING", time: 0});
		}
	}
	else {
		$('btnLogin').disabled = false;
		closeWindow("jLoginMsg");
		if (parseInt(content, 10) > 0) {
			if(parent.$('cdUsuario'))
				parent.$('cdUsuario').value = parseInt(content, 10); 
					
			parent.closeWindow('jLogin');
		}
		else {
			$('nmLogin').value = '';
			$('nmSenha').value = '';
			
			jLoginMsg = createTempbox("jMsg", {width: 210, height: 50, message: "Usuário ou senha não conferem", tempboxType: "ERROR", time: 2000});

			$('nmLogin').focus();
		}
	}
}
</script>
</head>
<body class="body" onload="init();">
<div class="d1-form" style="width:240px">
  <input id="nmEmpresa" name="nmEmpresa" type="hidden"/>
  <div style="height: 152px; margin-top:10px" class="d1-body">
    <div style="width:55px; height:115px; float:left; border-right:1px solid #CCCCCC; margin-right:5px;">
	    <img style=" margin: 0px 10px 0px 0px" src="imagens/login_key.gif" width="48" height="48">
	</div>
	<div style="float:left; margin:0px; overflow:visible; width:170px">
		<div class="d1-line" id="line1">
		 <div id="divContentLine1" style="width: 100%; height:20px;" class="element">
		   <label id="textLabel" class="caption" style="white-space:normal; font-weight:bold; color:#333333"><%=msg%></label>
		   <input name="parentUser" type="hidden" id="parentUser" value="<%=parentUser%>">
		 </div>
	    </div>
		<div class="d1-line" id="line2">
		 <div style="width: 100%; height:40px" class="element">
		   <label class="caption">Login	</label>
		   <input style="width: 100%;" class="field1" idform="empresa" datatype="STRING" id="nmLogin" name="nmLogin" type="text" tabindex="0">
		 </div>
	    </div>
	    <div class="d1-line" style="position:relative;" id="line3">
		 <div style="width: 100%; height:40px" class="element">
		   <label class="caption">Senha</label>
		   <input style="width: 100%;" class="field1" idform="empresa"  datatype="STRING" id="nmSenha" name="nmSenha" type="password"  tabindex="1">
		 </div>
		</div>
	    <div class="d1-toolButtons" id="line5">
		 <div style="width: 100%;" class="element">
			<button style="width:60px" onClick="btnLoginOnClick();" id="btnLogin" class="toolButton" tabindex="3">Login</button>  
		  </div>
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