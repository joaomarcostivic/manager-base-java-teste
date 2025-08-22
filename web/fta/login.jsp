<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.security.StatusPermissionActionUser"%>
<%@page import="sol.security.User"%>
<%@page import="java.util.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="" compress="false" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<script >
function validateUser() {
    if(!validarCampo(document.getElementById("nmLogin"), VAL_CAMPO_NAO_PREENCHIDO, true, "Informe o Login", true, null, null))
        return false;
    else if(!validarCampo(document.getElementById("nmSenha"), VAL_CAMPO_NAO_PREENCHIDO, true, "Informe a senha", true, null, null))
        return false;
	return true;	
}

var jLoginMsg;
function btnLoginOnClick(content) {
	if (content==null) {
		if (validateUser()) {
			var fields = [document.getElementById('nmLogin'), document.getElementById('nmSenha')];
			getPage("POST", "btnLoginOnClick", "../methodcaller?className=com.tivic.manager.seg.UsuarioServices"+
													  "&method=loginAndUpdateSession(session:javax.servlet.http.HttpSession, nmLogin:String, nmSenha:String)", fields, null, null);
			jLoginMsg = createTempbox("jMsg", {width: 165,
                                   height: 50,
								   message: "Autenticando usuário!",
                                   tempboxType: "LOADING",
                                   time: 0});
		}
	}
	else {
		jLoginMsg.close();
		if (parseInt(content, 10) > 0) {
			if (parent.document.getElementById('lockScreen') != null)
				parent.document.getElementById('lockScreen').style.display='none';
			parent.closeWindow('jLogin');
		}
		else {
			document.getElementById('nmLogin').value = '';
			document.getElementById('nmSenha').value = '';
			
			jLoginMsg = createTempbox("jMsg", {width: 210,
					   height: 50,
					   message: "Usuário ou senha não conferem",
					   tempboxType: "ERROR",
					   time: 2000});

			//document.getElementById('divContentLine1').style.height = '40px';
			//document.getElementById('textLabel').innerHTML = 'Usuário não identificado. Informe login e senha novamente...';
			document.getElementById('nmLogin').focus();
		}
	}
}
</script>
</head>
<%
	try {
		String msg = RequestUtilities.getParameterAsString(request, "msg", "");
%>
<body class="body">
<div style="width: 210px;" class="d1-form">
  <div style="width: 210px; height: 200px; margin-top:10px" class="d1-body">
    <img style="float:left; margin: 0px 10px 0px 0px" src="../imagens/login_key.gif" width="48" height="48">
	<div class="d1-line" id="line0" style="height:125px; width:1px; background-color:#666666; float:left; margin:0px 5px 0px 0px">
    </div>
	<div class="d1-line" id="line1">
      <div id="divContentLine1" style="width: 140px; height:20px;" class="element">
        <label id="textLabel" class="caption" style="white-space:normal; font-weight:bold; color:#333333"><%=msg%></label>
      </div>
    </div>
	<div class="d1-line" id="line2">
      <div style="width: 140px; height:40px" class="element">
        <label class="caption">Login	</label>
        <input style="width: 137px;" class="field" idform="empresa" datatype="STRING" id="nmLogin" name="nmLogin" type="text">
      </div>
    </div>
    <div class="d1-line" id="line3">
      <div style="width: 140px; height:40px" class="element">
        <label class="caption">Senha</label>
        <input style="width: 137px;" class="field" datatype="STRING" id="nmSenha" name="nmSenha" type="password">
      </div>
	</div>
    <div class="d1-toolButtons" id="line4">
      <div style="width: 137px;" class="element">
		<button style="width:60px" onClick="btnLoginOnClick();" id="btnLogin" class="toolButton">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td align="center" class="caption">Login</td>
          </tr>
        </table>
		</button>  
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
<script>
	document.getElementById('nmSenha').nextElement = document.getElementById('btnLogin');
	document.getElementById('nmLogin').focus();
	enableTabEmulation();
</script>
</html>