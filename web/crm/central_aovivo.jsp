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
<%@page import="com.tivic.manager.grl.Empresa" %>
<%@page import="com.tivic.manager.grl.EmpresaDAO" %>
<%@page import="com.tivic.manager.crm.*" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
	int cdUsuario = RequestUtilities.getParameterAsInteger(request, "cdUsuario", 0);
	int cdCentral = RequestUtilities.getParameterAsInteger(request, "cdCentral", 0);

	String nmServidorXMPP = CentralAtendimentoServices.getXMPPServer(request.getRemoteAddr());
	
	Empresa empresa = (cdEmpresa==0)?null:EmpresaDAO.get(cdEmpresa);
	Atendente atendente = (cdUsuario==0 || cdCentral==0)?null:AtendenteDAO.get(cdCentral, cdUsuario);
	CentralAtendimento central = (cdCentral==0)?null:CentralAtendimentoDAO.get(cdCentral);
	
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
	width:650px;
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

	var toolBar;
	var filterWindow;
	
	var tab;
	
	var atendimentos = [];
	
	function init(){
	    toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar',
									    orientation: 'horizontal',
									    buttons: [{id: 'btStart', img: 'imagens/start16.gif', label: 'Iniciar', onClick: iniciar},
											    //{id: 'btOnline', img: 'imagens/start16.gif', label: 'Online', onClick: online},
											    {id: 'btStop', img: 'imagens/stop16.gif', label: 'Parar', onClick: parar}]});
	    toolBar.disableButton('btStop');
	    
	    tab = TabOne.create('tab', {width: 790,
						  height: 375,                
						  tabs: [{caption: 'Atendimentos', 
						  		reference: 'chatTab',
								image: 'imagens/space16.gif', 
								active: true}],
						  plotPlace: 'divTab',
						  tabPosition: ['top', 'left']});
		
		DotTalk.init({server: '<%=nmServidorXMPP%>',
				    jid: '<%=atendente.getNmLoginIm().toLowerCase()%>@<%=nmServidorXMPP%>/dottalk',
				    pass: '<%=atendente.getNmSenhaIm()%>',
				    nick: '<%=Util.capitular(atendente.getNmApelidoIm())%>',
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
							startSessaoAtendimento();
				    		},
				    onMessage: function(message) {
				    			atendimentoOnMessage(message);
						},
				    onCommandMessage: function(message) {
				    			eval(message.getBody());
						},
				    onPresence: function(user){
							if(getChatIdByJid(user.jid)!=null){
								putAlert(user.name+" ficou "+user.status, user.jid+'_chat_messages');
								$(user.jid + "_chat_messages").scrollTop = $(user.jid + "_chat_messages").scrollHeight - $(user.jid + "_chat_messages").clientHeight;
				    			}
						},
				    onUserOffline: function(user){
							if(getChatIdByJid(user.jid)!=null && user.status != 'unavailable'){
								putAlert(user.name+" ficou offline", user.jid+'_chat_messages');
								$(user.jid + "_chat_messages").scrollTop = $(user.jid + "_chat_messages").scrollHeight - $(user.jid + "_chat_messages").clientHeight;
				    			}
						},
				    onSendMessage: function(message) {
				    			atendimentoOnMessage(message);
						},
				    onError: function(error){
				    			closeWindow('jConectando');
							if(error=='401' || error=='503'){
								toolBar.enableButton('btStart');
								toolBar.disableButton('btStop');
							}
						},
				    onChangeStatus: function(){
				    			if(DotTalk.statusOptions[DotTalk.onlstat]){
								$('statusLed').src = DotTalk.statusOptions[DotTalk.onlstat].img;
								$('dsStatus').innerHTML = DotTalk.statusOptions[DotTalk.onlstat].message;
							}
						},
				    onCleanUp: function(){
				    			//createGridContatos();
							//fechando abas
							for(var i=0; i<chatsAbertos.length; i++){
								tab.closeTab(chatsAbertos[i][1]);
							}
							chatsAbertos = [];
				    		},
				    onUpdateList: function(){
							//createGridContatos(DotTalk.roster.resultset);
						}});
	}
	
	function iniciar(){
		DotTalk.connect();
		toolBar.enableButton('btStop');
		toolBar.disableButton('btStart');
	}
	
	function parar(){
		finishSessaoAtendimento();	
		DotTalk.logout();
		toolBar.enableButton('btStart');
		toolBar.disableButton('btStop');
	}
	
	function online(){
		DotTalk.changeStatus('available', DotTalk.nick, DotTalk.onlprio);
	}
		
	var gridContatos;
	function createGridContatos(rsm){
		gridContatos = GridOne.create('gridContatos', {columns: [{label: '', reference: 'IMG_USER', type: GridOne._IMAGE, imgWidth: 16, style: 'background-color: #FFFFFF'},
													  {label: 'Contato', reference: 'NICK'}],
								 resultset: rsm,
								 onProcessRegister: function(reg){
									;	
								  },
								 columnSeparator: false,
								 lineSeparator: false,
								 noSelectorColumn: true,
								 noHeader: true,
								 plotPlace: 'divGridContatos'});
	}
		
	function atendimentoOnMessage(message){
	
		var from = cutResource(message.getFrom());
		var sender = DotTalk.roster.getUserByJID(from);
		
		var atendimento = getAtendimento(from);
			
		var body = '';
		var err = false;
		var type = message.getType();
		
		if (type == 'error') {
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
			nick = (atendimento)?atendimento.nmAtendido:from;
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
		
		var alarmChat = true;
		for(var i=0; i<chatsAbertos.length; i++){
			if(chatsAbertos[i][0]==from){
				if(tab.focusedTabId == chatsAbertos[i][1]){
					alarmChat = false;
				}
			}
		}
		
		if(message.getFrom() != DotTalk.jid){			
			if (type != 'groupchat') {		
				if(alarmChat){//somente se já nao tiver vendo o chat
					if (!sender.lastsrc)
						sender.lastsrc = DotTalk.statusOptions[sender.status].img;
					
					//gridContatos.changeValueByColumnValue('JID', from, 'IMG_USER', DotTalk.images.message_recv);
				}
			}
			openChat(from, (atendimento)?atendimento.nmAtendido:from, (getChatIdByJid(from)==null));
		}	
		
		var chatPanel = $(((fromMe)?message.getTo():from)+'_chat_messages');
		
		var auto_scroll = false;
		if(chatPanel.scrollTop + chatPanel.clientHeight >= chatPanel.scrollHeight){
			auto_scroll = true;
		}
	
		chatPanel.innerHTML += msgHTML;
	
		if(auto_scroll){
			chatPanel.scrollTop = chatPanel.scrollHeight - chatPanel.clientHeight;
		}

	}
	
	function putAlert(msg, chatId){
		$(chatId).innerHTML += '<div class="alert-message">'+msg+'</div>';
	}

	var chatsAbertos = [];
	function openChat(jid, nick, focusOnChat){
		for(var i=0; i<chatsAbertos.length; i++){
			if(jid == chatsAbertos[i][0]){
				if(tab.focusedTabId != chatsAbertos[i][1]){
					$(chatsAbertos[i][1]+'img').src = DotTalk.images.message_recv;
				}
				if(focusOnChat){
					tab.showTabById(chatsAbertos[i][1]);
				}
				return;
			}
		}
		
		var chat = document.createElement('div');
		    chat.id = jid + "_chat";
		    chat.startDate = new Date();
		chat.innerHTML = $('chatWindowModel').innerHTML.replace(/#JID/g, jid);
		
		document.body.appendChild(chat);
		var id = tab.insertTab({caption: nick,
						    reference: chat.id,
						    close: true,
						    onclose: function() {
									if(DotTalk.STORE_MESSAGES)
										DotTalk.storeChat(DotTalk.jid, DotTalk.jid + "_chat_messages");
									for(var i=0; i<chatsAbertos.length; i++){
										if(chatsAbertos[i][0]==jid){
											chatsAbertos.splice(i, 1);
										}
									}
								},
						    onfocus: function() {
									$(this.id+'img').src = DotTalk.images.message;
									var field = this.reference+'_message';
									setTimeout(function(){
										$(field).select();
										}, 10);
									/*for(var i=0; i<chatsAbertos.length; i++){
										if(chatsAbertos[i][1]==this.id){
											var sender = DotTalk.roster.getUserByJID(cutResource(chatsAbertos[i][0]));
											if(sender.lastsrc){
												gridContatos.changeValueByColumnValue('JID', chatsAbertos[i][0], 'IMG_USER', sender.lastsrc);
											}
										}
									}*/
									$(jid + "_chat_messages").scrollTop = $(jid + "_chat_messages").scrollHeight - $(jid + "_chat_messages").clientHeight;
								},
						    image: DotTalk.images.message_recv}, false);
							  
		chatsAbertos.push([jid, id]);
		if(focusOnChat)
			tab.showTabById(id);
	}
	
	function getChatIdByJid(jid){
		for(var i=0; i<chatsAbertos.length; i++)
			if(chatsAbertos[i][0]==jid)
				return chatsAbertos[i][1];
		
		return null;
	}
	
	function msgboxKeyDown(el, e, jidTo) {
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
					el.value = getHistory('up', el.value);
					el.focus(); el.select();
				}
				break;
			case 40:				// shift+down 
				if (e.ctrlKey) {
					el.value = getHistory('down', el.value);
					el.focus(); 
					el.select();
				}
				break;
			case 76:
				if (e.ctrlKey) {   // ctrl+l
					$(jidTo+'_chat_messages').innerHTML = '';
					return false;
				}
				break;
			case 27:
				tab.removeTabByReference(jidTo+'_chat');
				break;
			case 13:
				if(!e.shiftKey){
					DotTalk.sendMessage(el.value, jidTo);
					el.value='';
					el.focus();
					return false;
				}
				break;
		}
		return true;
	}	
	
	function startSessaoAtendimento(content) {
		if (content==null) {
			var construtor = "new com.tivic.manager.crm.Atendente(const <%=cdCentral%>: int, const  <%=cdCentral%>:int, const "+ DotTalk.jid +": String, const "+ DotTalk.pass +":String, const "+ DotTalk.nick +" :String)";
			getPage("GET", "startSessaoAtendimento", 
					"../methodcaller?className=com.tivic.manager.crm.CentralAtendimentoServices"+
					"&method=addSessaoAtendimento("+construtor+":com.tivic.manager.crm.Atendente)");
		}
		else {
			try {retorno = eval('(' + content + ')')} catch(e) {}
			if(retorno>0){
				createTempbox("jStartSessao", {width: 240,
								   height: 50,
								   message: "Sessão de atendimento iniciada...",
								   boxType: "INFO",
								   time: 2000});
			}
			else{
				createTempbox("jStartSessao", {width: 240,
								   height: 50,
								   message: "Continuando sessão de atendimento...",
								   boxType: "INFO",
								   time: 2000});
			}
		}
	}
	
	function finishSessaoAtendimento(content) {
		if (content==null) {
			var construtor = "new com.tivic.manager.crm.Atendente(const <%=cdCentral%>: int, const  <%=cdCentral%>:int, const "+ DotTalk.jid +": String, const "+ DotTalk.pass +":String, const "+ DotTalk.nick +" :String)";
			getPage("GET", "finishSessaoAtendimento", 
					"../methodcaller?className=com.tivic.manager.crm.CentralAtendimentoServices"+
					"&method=removeSessaoAtendimento("+construtor+":com.tivic.manager.crm.Atendente)");
		}
		else {
			try {retorno = eval('(' + content + ')')} catch(e) {}
			if(retorno>0){
				createTempbox("jStartSessao", {width: 240,
								   height: 50,
								   message: "Sessão de atendimento finalizada...",
								   boxType: "INFO",
								   time: 2000});
			}
			else{
				createTempbox("jStartSessao", {width: 240,
								   height: 50,
								   message: "Sessão de atendimento já finalizada...",
								   boxType: "INFO",
								   time: 2000});
			}
		}
	}
	
	function addAtendimento(nmAtendido, idAtendimento, jid){
		atendimentos[atendimentos.length] = {nmAtendido: nmAtendido, 
									  idAtendimento: idAtendimento, 
									  jid: jid};
		openChat(jid, nmAtendido, true);
		$(jid + "_chat_messages").innerHTML += '<span class="alert-message">[Iniciando atendimento.]<br />'+nmAtendido+' é o cliente a ser atendido nesta sessão.</span><br />';
		DotTalk.sendMessage("<%=central.getTxtMensagemInicial()%>", jid);
	}
	
	function getAtendimento(jid){
		for(var i=0; i<atendimentos.length; i++){
			if(atendimentos[i].jid==jid){
				return atendimentos[i];
			}
		}
		return null;
	}
	
	function removeAtendimento(jid){
		for(var i=0; i<atendimentos.length; i++){
			if(atendimentos[i].jid==jid){
				atendimentos.splice(i, 1);
			}
		}
	}
	
	</script>
	</head>
	<body class="body" onload="init();">

<!--FORM PRINCIPAL -->
<div style="width: 800px; height: 410px;" class="d1-form">
	<div class="d1-body">
		<div id="toolBar" class="d1-toolBar" style="height:24px; width: 632px; float:left; margin-right:2px;"></div>
		<div id="statusBar" class="d1-toolBar" style="width:154px; height:23px; position:relative; float:left;">
			<img id="statusLed" src="/sol/js/im/imagens/contact_offline.gif" style="float:left; margin:2px 2px 0 2px" />
			<div id="dsStatus" style="height:24px; line-height:24px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px; font-weight:bold; color:#999999; float:left">Offline</div>
		</div>
		<div class="d1-line" id="line0">
			<!--<div style="width: 205px;" class="element">
				<label class="caption">Atendimentos (0):</label>
				<div id="divGridContatos" style="width: 200px; height:360px; background-color:#FFF; border:1px solid #000000"></div>
			</div>-->
			<div id="divTab" style="width:760px; height:375px; float:left;">
				<div id="chatTab"></div>
			</div>
		</div>
	</div>
</div>

<div id="chatWindowModel" style="display:none">
	<div class="d1-form">
		<div class="d1-body">
			<div class="d1-line" id="line0">
				<div class="element" id="#JID_chat_messages" style="width:700px; height:220px; background-color:#FFFFFF; border:1px solid #CCCCCC; overflow:auto; ">
				</div>
			</div>
			<div class="d1-line" id="line2">
				<div class="element" style="width:700px; height:80px; margin-top:2px;">
					<textarea id="#JID_chat_message" class="textarea" style="width:700px; height:100px;" onKeyDown="return msgboxKeyDown(this, event, '#JID');"></textarea>
				</div>
			</div>
		</div>
	</div>
</div>


</body>
</html>
