<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.security.StatusPermissionActionUser"%>
<%@page import="sol.security.User"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>

<script language="javascript" src="/sol/js/sol.js"></script>
<script language="javascript">
<%
	try {
		com.tivic.manager.seg.Modulo modulo = com.tivic.manager.seg.ModuloServices.getModuloById(RequestUtilities.getParameterAsString(request, "idModulo", ""));
		//
		String msg            = RequestUtilities.getParameterAsString (request, "msg", "");
		boolean parentUser    = RequestUtilities.getParameterAsInteger(request, "parentUser", 0)==1;
		int lgEstoque         = RequestUtilities.getParameterAsInteger(request, "lgEstoque", 0);
		int lgAgenda          = RequestUtilities.getParameterAsInteger(request, "lgAgenda", 0);
		int lgEscolherEmpresa = RequestUtilities.getParameterAsInteger(request, "lgEscolherEmpresa", 0);
		int lgContabil        = RequestUtilities.getParameterAsInteger(request, "lgContabil", 0);
		boolean lgSupervisor  = RequestUtilities.getParameterAsInteger(request, "lgSupervisor", 0)==1;
		sol.dao.ResultSetMap rsmEmpresas = (sol.dao.ResultSetMap)session.getAttribute("rsmEmpresas");
		String callback 				 = RequestUtilities.getParameterAsString(request, "callback", "");
		int cdEmpresa         = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
%>

var callback =  <%=(!callback.equals(""))?callback:"null"%>;

function init()	{
	enableTabEmulation();
	document.getElementById('nmSenha').nextElement = document.getElementById('btnLogin');
	document.getElementById('nmLogin').focus();
	if (<%=parentUser%>) {
		btnLoginOnClick('{code: 1, objects: {usuario: {cdUsuario: '+parent.document.getElementById("cdUsuario").value+', tpUsuario: '+parent.document.getElementById("tpUsuario").value+', nmLogin: \"'+parent.document.getElementById("nmLogin").value+'\"},'+ 
	                    'nmUsuario: \''+parent.document.getElementById("nmUsuario").value+'\'}}');
	}
}

function validateUser() {
	var fields = [[document.getElementById("nmLogin"), 'Login', VAL_CAMPO_NAO_PREENCHIDO],
				  [document.getElementById("nmSenha"), 'Senha', VAL_CAMPO_NAO_PREENCHIDO]];
    return validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmLogin', 210, 50);
}

var user = { };
function onFinishLogin(notCloseWindow) {
	if(callback){
		callback(user);
	}
	if(parent.document.getElementById('userPanel')){
		parent.document.getElementById('userPanel').style.display = '';
		if (parent.document.getElementById('NM_OPERADOR')!=null)	{
			if(user['nmUsuario']!='')
				parent.document.getElementById('NM_OPERADOR').innerHTML = user['nmUsuario'];
			else
				parent.document.getElementById('NM_OPERADOR').innerHTML = document.getElementById('nmLogin').value;
		}
		if (parent.document.getElementById('NM_EMPRESA')!=null)	{
			parent.document.getElementById('NM_EMPRESA').innerHTML = getTextSelect('cdEmpresa');
		}
	}
	if (!notCloseWindow && parent!=null && parent.closeWindow!=null) { 
		parent.closeWindow('jLogin');
	}
}

function btnLoginEmpresaOnClick()	{
	var cdEmpresa = document.getElementById('cdEmpresa').value;
	var nmEmpresa = '';
	if(document.getElementById('cdEmpresa').selectedIndex<0)	{
		createTempbox("jMsg", {width: 310, height: 50, message: 'Você deve selecionar uma empresa!', tempboxType: "ERROR", time: 2000});
		return;		
	}
	if(parent.document.getElementById('cdEmpresa'))
		parent.document.getElementById('cdEmpresa').value = cdEmpresa;
	
	if(parent.document.getElementById('nmEmpresa'))
		parent.document.getElementById('nmEmpresa').value = nmEmpresa;
	
	
	user['cdEmpresa'] = cdEmpresa;
	user['nmEmpresa'] = nmEmpresa;
	
	if (parent.loadProfileUsuario)	
		parent.loadProfileUsuario();
	
	onFinishLogin();
}


var jLoginMsg;
function btnLoginOnClick(content) {
	if (content==null) {
		var cdEmpresa = <%=cdEmpresa%>;
		var method = "loginAndUpdateSession(" + (<%=lgSupervisor%> ? "nmLogin:String, nmSenha:String, const <%=modulo!=null?modulo.getCdModulo():0%>:int, " + 
				      "const true:boolean, const "+cdEmpresa+":int" : "session:javax.servlet.http.HttpSession, nmLogin:String, nmSenha:String, const true:boolean, " + 
				   	  "const " + (document.getElementById('lgAgenda').value == 1 ? "true" : "false") + ":boolean, "  +
				   	  "const <%=modulo!=null?modulo.getCdModulo():0%>:int") + ")";
		if (validateUser()) {
			var fields = [document.getElementById('nmLogin'), document.getElementById('nmSenha')];
			getPage("POST", "btnLoginOnClick", "methodcaller?className=com.tivic.manager.seg.UsuarioServices"+
					"&method=" + method +
					"&noRegisterLog=1", fields, null, null);
			document.getElementById('btnLogin').disabled = true;
			createTempbox("jLoginMsg", {width: 220, height: 50, message: "Autenticando usuário. Aguarde...", tempboxType: "LOADING", time: 0});
		}
	}
	else {
		document.getElementById('btnLogin').disabled = false;
		closeWindow("jLoginMsg");
		var result = eval("("+content+")");
		var info   = result.objects;
		//
		if (info != null && info['usuario'] != null && info['usuario']['cdUsuario'] > 0 && <%=!lgSupervisor%>) {
			user['cdUsuario'] = info['usuario']['cdUsuario'];
			user['tpUsuario'] = info['usuario']['tpUsuario'];
			user['nmUsuario'] = info['nmUsuario'];
			user['nmLogin']   = info['usuario']['nmLogin'];
			
			if(parent.document.getElementById('cdUsuario') != null)	
				parent.document.getElementById('cdUsuario').value = info['usuario']['cdUsuario'];
			
			if(parent.document.getElementById('cdPessoa') != null)	
				parent.document.getElementById('cdPessoa').value = info['usuario']['cdPessoa'];
			
			if(parent.document.getElementById('tpUsuario') != null)	
				parent.document.getElementById('tpUsuario').value = info['usuario']['tpUsuario'];
			
			if(parent.document.getElementById('nmUsuario') != null)	
				parent.document.getElementById('nmUsuario').value = info['nmUsuario']!='' ? info['nmUsuario'] : info['usuario']['nmLogin'];
			
			if (parent.document.getElementById('lockScreen') != null)	
				parent.document.getElementById('lockScreen').style.display='none';
			// Exibe janela de escolha do local de armazenamento
			if (document.getElementById('lgEstoque').value == 1) {
				onFinishLogin(true);
				if(result.objects['rsmEmpresas']!=null)
					parent.rsmEmpresas = result.objects['rsmEmpresas']; 
				document.location = 'alm/login_local_armazenamento.jsp'
				if (parent.janelaLogin != null) {
					var newLeft = parent.janelaLogin.options.left + ((parent.janelaLogin.options.left - 350) / 2) - 20;
					parent.janelaLogin.move(newLeft, parent.janelaLogin.options.top);
					parent.janelaLogin.resize(350, 190);
				}
			}
			else if (document.getElementById('lgAgenda').value == 1) {
				parent.loadProfileUsuario(info);
				onFinishLogin();
			}
			// Exibe janela de escolha do ano de exercício (contábil)
			else if (document.getElementById('lgContabil').value == 1) {
				document.location = 'ctb/login_ano_exercicio.jsp';
				if (parent.janelaLogin != null) {
					var newLeft = parent.janelaLogin.options.left + ((parent.janelaLogin.options.left - 250) / 2) - 20;
					parent.janelaLogin.move(newLeft, parent.janelaLogin.options.top);
					parent.janelaLogin.resize(250, 155);
				}
			}
			// Exibe empresa para escolher sem precisar chamar outra janela
			else if (parseInt(getValue('lgEscolherEmpresa'), 10) == 1) {
				if(result.objects['rsmEmpresas']!=null)
					loadOptionsFromRsm(document.getElementById('cdEmpresa'), result.objects['rsmEmpresas'], {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
				else	{
					<%if(rsmEmpresas!=null)	{%>
						// From Session
						loadOptionsFromRsm(document.getElementById('cdEmpresa'), <%=sol.util.Jso.getStream(rsmEmpresas)%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
					<%}
					else if(lgEscolherEmpresa==1){%>
						// getAll
						loadOptionsFromRsm(document.getElementById('cdEmpresa'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.EmpresaServices.getAllAtivos())%>, {fieldValue: 'cd_empresa', fieldText:'nm_fantasia'});
					<%}%>
				}
				document.getElementById('nmLogin').value         = info['nmUsuario']!='' ? info['nmUsuario'] : info['usuario']['nmLogin'];
				document.getElementById('nmLogin').disabled      = true;
				document.getElementById('nmLogin').className     = 'disabledField';
				document.getElementById('line4').style.display   = '';
				document.getElementById('line3').style.display   = 'none';
				document.getElementById('btnLogin').onclick 	   = btnLoginEmpresaOnClick;
				document.getElementById('cdEmpresa').nextElement = document.getElementById('btnLogin');
				document.getElementById('cdEmpresa').focus();
				if(document.getElementById('cdEmpresa').options.length==1)
					btnLoginEmpresaOnClick();
			}
			else 
				onFinishLogin();
		}
		else if(result.code > 0 && <%=lgSupervisor%>){
			parent.validarSupervisor();
			onFinishLogin();
		}
		else {
			document.getElementById('nmLogin').value = '';
			document.getElementById('nmSenha').value = '';
			createTempbox("jMsg", {width: 210, height: 50, message: result.message, tempboxType: "ERROR", time: 2000});
			document.getElementById('nmLogin').focus();
		}
	}
}
</script>
</head>
<body class="body" onload="init();">
<div id="textModulo" class="caption" style="white-space:normal; font-weight:bold; text-align:center; background-color: #DDD; width: 100%; margin-left: -1px; font-family: sans-serif; font-size: 11px; padding-top: 5px; padding-bottom: 5px; text-transform: uppercase;"><%=modulo!=null?modulo.getNmModulo():""%></div>
<div class="d1-form" style="width:340px; background-color: #FFFFFF !important; ">
  <input id="nmEmpresa" name="nmEmpresa" type="hidden"/>
  <input name="lgEstoque"  type="hidden" id="lgEstoque"  value="<%=lgEstoque%>"/>
  <input name="lgAgenda"   type="hidden" id="lgAgenda"   value="<%=lgAgenda%>"/>
  <input name="lgContabil" type="hidden" id="lgContabil" value="<%=lgContabil%>"/>
  <input name="lgEscolherEmpresa" type="hidden" id="lgEscolherEmpresa" value="<%=lgEscolherEmpresa%>"/>
  <div style="height: 126px; margin-top:5px" class="d1-body">
    <div style="width:55px; height:115px; float:left; border-right:1px solid #CCCCCC; margin-right:5px;">
	    <img style=" margin: 0px 10px 0px 0px" src="imagens/login_key.gif" width="48" height="48"/>
	</div>
	<div style="float:left; margin:0px; overflow:visible; width:270px">
		<div class="d1-line" id="line1">
		 <div id="divContentLine1" style="width: 100%; height:20px;" class="element">
		   <label id="textLabel" class="caption" style="white-space:normal; font-weight:bold; color:#333333"><%=msg%></label>
		 </div>
	    </div>
		<div class="d1-line" id="line2">
		 <div style="width: 100%; height:38px" class="element">
		   <label class="caption">Login	</label>
		   <input style="width: 100%;" class="field2" idform="empresa" datatype="STRING" id="nmLogin" name="nmLogin" type="text" tabindex="0"/>
		 </div>
	    </div>
	    <div class="d1-line" style="position:relative;" id="line3">
		 <div style="width: 100%; height:38px" class="element">
		   <label class="caption">Senha</label>
		   <input style="width: 100%;" class="field2" idform="empresa"  datatype="STRING" id="nmSenha" name="nmSenha" type="password"  tabindex="1"/>
		 </div>
		</div>
		<div class="d1-line" id="line4" style="display: none">
		 <div style="width: 100%; margin-bottom:9px;" class="element">
		   <label class="caption">Empresa</label>
		   <select style="width: 100%;" class="select2" idform="empresa"  id="cdEmpresa" name="cdEmpresa" tabindex="2">
		   </select>
		 </div>
	    </div>
	    <div class="d1-toolButtons" id="line5">
		 <div style="width: <%=lgSupervisor ? 80 : 100%>%;" class="element">
			<button style="width:60px;" onClick="btnLoginOnClick();" id="btnLogin" class="toolButton" tabindex="3">Login</button>  
		  </div>
		</div>
		<% if(lgSupervisor){ %>
		 <div style="width: 20%;" class="element">
			<button style="width:60px;" onClick="onFinishLogin();" id="btnFinish" class="toolButton" tabindex="3">Cancelar</button>  
		  </div>
		</div>
		<%} %>
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