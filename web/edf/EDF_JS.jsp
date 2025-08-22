<!DOCTYPE html>
<%@page import="com.tivic.manager.seg.ReleaseServices"%>
<html>
<%@page import="com.tivic.manager.sis.*" %>
<%@page import="sol.util.RequestUtilities"%>
 <%
   try{
	
	String versao = ReleaseServices.getLastRelease();
	String module = RequestUtilities.getParameterAsString(request, "m", "");

	session.removeAttribute("usuario");
	session.removeAttribute("user");			
	session.removeAttribute("pessoa");
  %>
<head>
<title>Escola do Futuro <%=versao%> </title>

<meta name="google" value="notranslate" /> 
<meta charset="utf-8">
<link href="../js/metro/css/metro-bootstrap.css" rel="stylesheet">
<link href="../js/metro/css/metro-bootstrap-responsive.css" rel="stylesheet">
<link href="../js/metro/css/docs.css" rel="stylesheet">
<link href="../js/metro/prettify/prettify.css" rel="stylesheet">

<script src="../js/metro/jquery/jquery.min.js"></script>
<script src="../js/metro/jquery/jquery.widget.min.js"></script>
<script src="../js/metro/jquery/jquery.mousewheel.js"></script>
<script src="../js/metro/prettify/prettify.js"></script>

<script src="../js/metro/metro.min.js"></script>
<script src="../js/metro/docs.js"></script>

</head>

<script language="javascript">

function init() {
	
	login('');
	
	loadModule();
	
}

function loadModule() {
	
	var url = '<%=module%>.xml';
	var xmlhttp;
	
	if (window.XMLHttpRequest)  {
        xmlhttp = new XMLHttpRequest();
    }
    else {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

	
    xmlhttp.onload = function() {
        var xmlDoc = new DOMParser().parseFromString(xmlhttp.responseText, 'text/xml');
        //console.log(xmlDoc);
        
        var tabs = xmlDoc.getElementsByTagName("tab");
        var htmlTabs = '';
        var htmlTabsContent = '';
        for(i=0; i<tabs.length; i++) { 
        	htmlTabs+='<li '+(i==0 ? 'class="active" ' : '')+'><a href="#'+tabs[i].getAttribute('id')+'">'+tabs[i].getAttribute('label')+'</a></li>';
        	htmlTabsContent+='<div class="tab-panel" id="'+tabs[i].getAttribute('id')+'">'+loadTabContent(tabs[i])+'</div>';
        }
        $("#tabs").html(htmlTabs);
        $("#tabsContent").html(htmlTabsContent);
        
    }
	
	xmlhttp.open("GET", url, false);
	xmlhttp.send();
	
}

function loadTabContent(tab) {
	var groups = tab.getElementsByTagName("group");
	var content = '';
	for(j=0; j<groups.length; j++) { 
		if(groups[j].getAttribute('label')==null)
			continue;
		
		content += '<div class="tab-panel-group"><div class="tab-group-content">';
		
		
		content += '</div><div class="tab-group-caption">'+groups[j].getAttribute('label')+'</div></div>';
	}
	return content;
}

function login(msg) {
	$.Dialog({
        overlay: true,
        overlayClickClose: false,
        shadow: true,
        flat: false,
        //icon: '<img src="images/excel2013icon.png">',
        title: 'Login',
        sysButtons: false,
        content: '',
        onShow: function(_dialog){
            var html = [
                '<iframe width="380" height="160" src="../login.jsp?callback=parent.loginCallback&lgEscolherEmpresa=0&idModulo=acd'+
                		(msg!=null? '&msg='+msg : '')+'" frameborder="0" allowfullscreen="true"></iframe>'
            ].join("");

            $.Dialog.content(html);
        }
    });

	
// 	createWindow('jLogin', {caption: 'Login', noMinimizeButton: true, noMaximizeButton: true, noCloseButton: true, width: 350, height: 180, modal: true, 
// 							contentUrl: '../login.jsp?callback=parent.loginCallback&lgEscolherEmpresa=0&idModulo=acd'+(msg!=null? '&msg='+msg : '')});
}

function loginCallback(user)	{
	$.Dialog.close();
}
</script>
<body class="metro" onload="init()" style="background: url(imagens/logo_bg.png); background-repeat: no-repeat; background-position: -220px -120px; background-attachment: fixed;">

<img src="imagens/logo.png" style="position:absolute; left:10px; top:30px;"/>

<div id="fluentMenu" class="fluent-menu" data-role="fluentmenu" style="position:absolute; top:5px; left:200px; right:10px;">
    <ul id="tabs" class="tabs-holder"></ul>

    <div id="tabsContent" class="tabs-content"></div>
</div>

<!-- <div class="fluent-menu" data-role="fluentmenu" style="position:absolute; top:5px; left:200px; right:10px;"> -->
<!--     <ul class="tabs-holder"> -->
<!--         <li class="active"><a href="#tab_principal">Principal</a></li> -->
<!--         <li><a href="#tab_relatorios">Relatórios</a></li> -->
<!--     </ul> -->

<!--     <div class="tabs-content"> -->
<!--         <div class="tab-panel" id="tab_principal"> -->
<!--             <div class="tab-panel-group"> -->
<!--                 <div class="tab-group-content"> -->
<!--                     <button class="fluent-big-button"><span class="icon-mail"></span>Create<br />message</button> -->
<!--                     <div class="tab-content-segment"> -->
<!--                         <button class="fluent-big-button dropdown-toggle"> -->
<!--                             <span class="icon-pictures"></span> -->
<!--                             <span class="button-label">Create<br />element</span> -->
<!--                         </button> -->
<!--                         <ul class="dropdown-menu" data-role="dropdown"> -->
<!--                             <li><a href="#">Message</a></li> -->
<!--                             <li><a href="#">Event</a></li> -->
<!--                             <li><a href="#">Meeting</a></li> -->
<!--                             <li><a href="#">Contact</a></li> -->
<!--                         </ul> -->
<!--                     </div> -->
<!--                     <div class="tab-content-segment"> -->
<!--                         <button class="fluent-big-button"> -->
<!--                             <span class="icon-cancel"></span> -->
<!--                             <span class="button-label">Delete</span> -->
<!--                         </button> -->
<!--                     </div> -->
<!--                 </div> -->
<!--                 <div class="tab-group-caption">Clipboard</div> -->
<!--             </div> -->
<!--             <div class="tab-panel-group"> -->
<!--                 <div class="tab-group-content"> -->
<!--                     <div class="tab-content-segment"> -->
<!--                         <button class="fluent-button"><span class="icon-reply on-left"></span>Replay</button> -->
<!--                         <button class="fluent-button"><span class="icon-reply-2 on-left"></span>Replay all</button> -->
<!--                         <button class="fluent-button"><span class="icon-cycle on-left"></span>Forward</button> -->
<!--                     </div> -->
<!--                     <div class="tab-content-segment"> -->
<!--                         <button class="fluent-tool-button"><img src="images/Notebook-Save.png"></button> -->
<!--                         <button class="fluent-tool-button"><img src="images/Folder-Rename.png"></button> -->
<!--                         <button class="fluent-tool-button"><img src="images/Calendar-Next.png"></button> -->
<!--                     </div> -->
<!--                 </div> -->
<!--                 <div class="tab-group-caption">Reply</div> -->
<!--             </div> -->
<!--             <div class="tab-panel-group"> -->
<!--                 <div class="tab-group-content"> -->
<!--                     <div class="input-control text"> -->
<!--                         <input type="text"> -->
<!--                         <button class="btn-search"></button> -->
<!--                     </div> -->
<!--                     <button class="fluent-button"><span class="icon-book on-left"></span>Address Book</button> -->
<!--                     <div class="tab-content-segment"> -->
<!--                         <button class="fluent-button dropdown-toggle"> -->
<!--                             <span class="icon-filter on-left"></span> -->
<!--                             <span class="button-label">Mail Filters</span> -->
<!--                         </button> -->
<!--                         <ul class="dropdown-menu" data-role="dropdown"> -->
<!--                             <li><a href="#">Unread messages</a></li> -->
<!--                             <li><a href="#">Has attachments</a></li> -->
<!--                             <li class="divider"></li> -->
<!--                             <li><a href="#">Important</a></li> -->
<!--                             <li><a href="#">Broken</a></li> -->
<!--                         </ul> -->
<!--                     </div> -->
<!--                 </div> -->
<!--                 <div class="tab-group-caption">Search</div> -->
<!--             </div> -->
<!--         </div> -->

<!--         <div class="tab-panel" id="tab_relatorios"> -->
<!--         </div> -->
<!--     </div> -->
<!-- </div> -->


</body>
<%
	}
	catch(Exception e) {
	}
%>
</html>