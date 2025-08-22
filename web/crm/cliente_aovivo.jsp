<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content ="no-cache">
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.dao.ResultSetMap" %>
<%@page import="com.tivic.manager.util.Recursos" %>
<%@page import="com.tivic.manager.util.Util" %>
<%@page import="com.tivic.manager.grl.ParametroServices" %>
<%@page import="com.tivic.manager.crm.CentralAtendimento" %>
<%@page import="com.tivic.manager.crm.CentralAtendimentoDAO" %>
<%@page import="com.tivic.manager.crm.CentralAtendimentoServices" %>
<%@page import="com.tivic.manager.crm.SessaoAtendimento" %>
<%@page import="com.tivic.manager.crm.Atendente" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	int cdCentral = RequestUtilities.getParameterAsInteger(request, "cdCentral", ParametroServices.getValorOfParametroAsInteger("CD_CENTRAL_ATENDIMENTO_PADRAO", 0));
	
	String nmServidorXMPP = CentralAtendimentoServices.getXMPPServer(request.getRemoteAddr());

	CentralAtendimento central = (cdCentral==0)?null:CentralAtendimentoDAO.get(cdCentral);
	
	if(central!=null) {
		
		HashMap retorno = CentralAtendimentoServices.startAtendimento(central.getCdCentral(), "Cliente");
		
		SessaoAtendimento sessao = (SessaoAtendimento)retorno.get("SESSAO_ATENDIMENTO");
		String idAtendimento = (String)retorno.get("ID_ATENDIMENTO");
		
		if(sessao!=null && idAtendimento!=null){
			Atendente atendente = sessao.getAtendente();
%>
<style>
	.context-menu {
		background-color:#EAEAEA; 
		border:1px solid #FFFFFF; 
		border-bottom:1px solid #999999; 
		border-right:1px solid #999999; 
		position:absolute;
		z-index:10000;
		display:none;
		padding:1px 2px 1px 2px;
	}
	
	
	.context-menu .item {
		height:16px; 
		cursor:pointer;
		-moz-outline: 0 solid;
		display:block;
		text-decoration:none;
	}
	
	.context-menu .item:hover{
		background-color:#666666;
		color:#FFFFFF; 
	}
	
	.context-menu .item img {
		float:left;
		border:0 solid;
		background-color:#EAEAEA; 
	}
	
	.context-menu .item div {
		float:left;
		height:16px; 
		line-height:16px; 
		font-family:Geneva, Arial, Helvetica, sans-serif; 
		font-size:10px; 
		font-weight:bold; 
		color:#999999;
		padding:0 2px 0 2px;
	}
	
.chat-message {
	display:inline;
	float:left;
	padding:5px 0 0 5px;
}

.chat-message .message{
	font-family:Geneva, Arial, Helvetica, sans-serif;
	font-size:12px;
	position:relative;
	float:left;
	color:#000000;
	width:370px;
}
.chat-message .message .time {
	font-size: 10px;
	color: #666666;
}

.chat-message .message .nick {
	font-size: 12px;
	color:#333399;
	font-weight:bold;
}

.chat-message .message .me {
	font-size: 12px;
	color:#FF9900;
	font-weight:bold;
}

.chat-message .message .baloon{
	position:absolute;
	top:10px;
	left:-10px;
}
.alert-message {
	font-family:Geneva, Arial, Helvetica, sans-serif;
	font-size:10px;
	color:#999999;
	font-weight:bold;
}
</style>
<script language="javascript" src="/sol/js/im/JSJaC/JSJaC.js"></script>
<script language="javascript" src="/sol/js/im/dottalk.js"></script>
<script language="javascript" src="/sol/js/im/roster.js"></script>
<script language="javascript">

	/******** CHAT ***********/
	var MSG_UNAVAILABLE = "Seu amigo está offline. Tente mais tarde.";
	var MSG_STALKER = "Aguardando aceitação...";
	var MSG_REQUEST_SUB = "<br />quer adicionar você na lista de amigos.<br />Clique Ok para permitir";
	var MSG_OFFLINE = "Você está offline no momento...";
	var MSG_REQUEST = "Enviado! Aguarde a resposta do amigo...";
	
	var MSG_401 = "401: Falha de autorização";
	var MSG_409 = "409: Falha no registro. Escolha outro username.";
	var MSG_503 = "Serviço indisponível no momento";
	var MSG_500 = "Erro interno do servidor. Clique \"ok\" para reconectar";
	
	var MSG_SERVER_DOWN = 'Aguardando aceitação...';

	function init(){
	
		DotTalk.init({server: '<%=nmServidorXMPP%>',
				    jid: 'atendimento@<%=nmServidorXMPP%>/dottalk',
				    pass: 'atendimento',
				    nick: 'Cliente',
				    anonymous: true,
				    onStartConnect: function(){
							createTempbox("jConectando", {width: 150,
											  height: 45,
											  message: 'Conectando dotTalk',
											  boxType: "LOADING",
											  modal: true,
											  time: 0});
				    		},
				    onFinishConnect: function(){
				    			closeWindow('jConectando');
							$('chat_messages').innerHTML += '<span class="alert-message">[Iniciando atendimento.]<br /><%=atendente.getNmApelidoIm()%> é o seu atendente nesta sessão.</span><br />';
							DotTalk.sendCommandMessage('addAtendimento("'+DotTalk.nick+'", "<%=idAtendimento%>", cutResource(message.getFrom()));', '<%=atendente.getNmLoginIm()%>');
						},
				    onMessage: function(message) {
				    			atendimentoOnMessage(message);
						},
				    onSendMessage: function(message) {
				    			atendimentoOnMessage(message);
						},
				    onError: function(){
				    			closeWindow('jConectando');
						},
				    onChangeStatus: function(){
							$('statusLed').src = DotTalk.statusOptions[DotTalk.onlstat].img;
							$('dsStatus').innerHTML = DotTalk.statusOptions[DotTalk.onlstat].message;
						},
				    onUpdateList: function(){
							//createGridContatos(DotTalk.roster.resultset);
						}});
			
		initForm();
	}

	function initForm(){
		createWindow('jInit', {caption: "Iniciando atendimento",
								  width: 200,
								  height: 85,
								  noDropContent: true,
								  contentDiv: 'initForm',
								  noDrag: true,
								  modal: true});
	}
	
	function login(){
		DotTalk.nick = $('nmAtendido').value;
		DotTalk.connect();
		closeWindow('jInit');
	}
	
	function logout(){
		finalizarAtendimento();
		DotTalk.sendCommandMessage('putAlert("'+DotTalk.nick+' ficou offline", cutResource(message.getFrom())+"_chat_messages");', '<%=atendente.getNmLoginIm()%>');
		DotTalk.sendCommandMessage('removeAtendimento(cutResource(message.getFrom()));', '<%=atendente.getNmLoginIm()%>');
		DotTalk.logout();
	}
	
	function finalizarAtendimento(content) {
		if (content==null) {
			getPage("GET", "finalizarAtendimento", 
					"../methodcaller?className=com.tivic.manager.crm.CentralAtendimentoServices"+
					"&method=finishAtendimento(const <%=idAtendimento%>: String)");
		}
	}
	
	function atendimentoOnMessage(message){
	
		var from = cutResource(message.getFrom());
		var sender = DotTalk.roster.getUserByJID(from);
		
		var body = '';
		var err = false;
		if (message.getType() == 'error') {
			var error = aJSJaCPacket.getNode().getElementsByTagName('error').item(0);
			if (error && error.getElementsByTagName('text').item(0))
				body = error.getElementsByTagName('text').item(0).firstChild.nodeValue;
			err = true;
		}
		else
			body = message.getBody();
		
		var now;
		if (message.jwcTimestamp)
			now = message.jwcTimestamp;
		else
			now = new Date();
		
		var mtime = (now.getHours()<10)? "0" + now.getHours() : now.getHours();
		    mtime += ":";
		    mtime += (now.getMinutes()<10)? "0" + now.getMinutes() : now.getMinutes();
		    mtime += ":";
		    mtime += (now.getSeconds()<10)? "0" + now.getSeconds() : now.getSeconds();
		
		var fromMe = (DotTalk.jid == message.getFrom());
		
		var nick;
		var nickcolor;
		var dir;
		if (fromMe){ // msg sent by me
			nick = DotTalk.nick;
			nickcolor = 'blue';
			dir = 'to'
		}
		else {
			nick = '<%=atendente.getNmApelidoIm()%>';
			nickcolor = 'red';
			dir = 'from'
		}
			
		var msgHTML = '';
		msgHTML += '<div class="chat-message">';
		msgHTML += '	<div class="message">';
		msgHTML += '		<span class="time">['+mtime+']</span>&nbsp;<span class="'+(fromMe ? 'me' : 'nick')+'">'+nick+'</span>&nbsp;diz: ';
		msgHTML +=          (err)?'<span style="color:red;">&nbsp;'+msgFormat(body)+'</span>':msgFormat(body);
		msgHTML += '	</div>';
		msgHTML += '</div>';
		
		$('chat_messages').innerHTML += msgHTML;	
		
	}
	
	
	function msgboxKeyDown(el, e) {
		var keycode;
		if (window.event) { 
			e  = window.event; 
			keycode = window.event.keyCode; 
		}
		else if(e)
			keycode = e.which;
		else 
			return true;
	
		switch (keycode) {
			case 38:				// shift+up
				if (e.ctrlKey) {
					el.value = DotTalk.getHistory('up', el.value);
					el.focus(); el.select();
				}
				break;
			case 40:				// shift+down 
				if (e.ctrlKey) {
					el.value = DotTalk.getHistory('down', el.value);
					el.focus(); 
					el.select();
				}
				break;
			case 76:
				if (e.ctrlKey) {   // ctrl+l
					$('chat_messages').innerHTML = '';
					return false;
				}
				break;
			case 13:
				if(!e.shiftKey){
					//DotTalk.sendCommandMessage('alert("'+DotTalk.nick+' : '+el.value+'");', '<%=atendente.getNmLoginIm()%>');
					DotTalk.sendMessage(el.value, '<%=atendente.getNmLoginIm().toLowerCase()%>');
					el.value='';
					el.focus();
					return false;
				}
				break;
		}
		return true;
	}

</script>
</head>
<body class="body" onload="init();" onunload="logout();">

<!--FORM PRINCIPAL -->
<div style="width: 400px; height: 400px;" class="d1-form">
	<div class="d1-body">
		<div id="statusBar" class="d1-toolBar" style="width:390px; height:23px; position:relative; float:left;">
			<img id="statusLed" src="/sol/js/im/imagens/contact_offline.gif" style="float:left; margin:2px 2px 0 2px" />
			<div id="dsStatus" style="height:24px; line-height:24px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; font-weight:bold; color:#999999; float:left">Offline</div>
		</div>
		<div class="d1-line" id="line0">
			<div class="element" id="chat_messages" style="width:390px; height:257px; background-color:#FFFFFF; border:1px solid #CCCCCC; overflow:auto; ">
			</div>
		</div>
		<div class="d1-line" id="line1">
			<div class="element" style="width:390px; height:80px; margin-top: 2px;">
				<textarea id="chat_message" class="textarea" style="width:390px; height:80px;" onKeyDown="return msgboxKeyDown(this, event);"></textarea>
			</div>
		</div>

	</div>
</div>

<!--FORM USUARIO -->
<div id="initForm" style="display:none">
  <div class="d1-form" style="height:60px;">
	  <div class="d1-body">
		<div class="d1-line" id="line0">
			<div style="width: 200px;" class="element">
				<label class="caption" for="nmAtendido">Seu nome</label>
				<input style="width: 187px;" class="field" name="nmAtendido" id="nmAtendido" type="text"/>
			</div>
		</div>
		<div class="d1-line" id="line42" style="text-align:right">
			<div style="width: 186px; margin:3px;" class="element">
				<button onclick="login()" title="" style="border:1px solid #999999; background-color:#CCCCCC; font-family:Verdana, Arial, Helvetica, sans-serif; font-size:10px">Prosseguir</button>
			</div>
		</div>
	  </div>
   </div>
</div>


<%
	}
	else{
		out.println("Nenhum atendente online");
	}
}
else{
	out.println("Central inexistente");
}
%>

</body>
</html>
