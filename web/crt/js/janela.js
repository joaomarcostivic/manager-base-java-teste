/* para efeitos dos botoes*/

var baseopacity=60;

function slowhigh(which2){
imgobj=which2
browserdetect=which2.filters? "ie" : typeof which2.style.MozOpacity=="string"? "mozilla" : ""
instantset(baseopacity)
highlighting=setInterval("gradualfade(imgobj)",50)
}

function slowlow(which2){
cleartimer()
instantset(baseopacity)
}

function instantset(degree){
if (browserdetect=="mozilla")
imgobj.style.MozOpacity=degree/100
else if (browserdetect=="ie")
imgobj.filters.alpha.opacity=degree
}

function cleartimer(){
if (window.highlighting) clearInterval(highlighting)
}

function gradualfade(cur2){
if (browserdetect=="mozilla" && cur2.style.MozOpacity<1)
cur2.style.MozOpacity=Math.min(parseFloat(cur2.style.MozOpacity)+0.1, 0.99)
else if (browserdetect=="ie" && cur2.filters.alpha.opacity<100)
cur2.filters.alpha.opacity+=10
else if (window.highlighting)
clearInterval(highlighting)
}

/* fim efeito botoes */

/* para o drag da janela*/

var ie  = document.all
var ns6 = document.getElementById && !document.all

var dragapproved=false
var z,z2,x,y

function move(e){
	if (dragapproved){
		z.style.left=ns6? temp1+e.clientX-x: temp1+event.clientX-x
		z.style.top=ns6? temp2+e.clientY-y : temp2+event.clientY-y
		z2.style.left=ns6? temp1+e.clientX-x: temp1+event.clientX-x
		z2.style.top=ns6? temp2+e.clientY-y : temp2+event.clientY-y
		return false
	}
}

function drags(e){
	if (!ie&&!ns6)
		return
	var firedobj=ns6? e.target : event.srcElement
	var topelement=ns6? "HTML" : "BODY"
	
	while ('tagName' in firedobj && firedobj.tagName!=topelement && firedobj.className!="drag"){
		firedobj=ns6? firedobj.parentNode : firedobj.parentElement
	}

	if (firedobj.className=="drag"){
		dragapproved=true
		z=firedobj
		z2=document.getElementById('ifr_'+z.id);
		temp1=parseInt(z.style.left+0)
		temp2=parseInt(z.style.top+0)
		x=ns6? e.clientX: event.clientX
		y=ns6? e.clientY: event.clientY
		document.onmousemove=move
		return false
	}
}

document.onmousedown=drags
document.onmouseup=new Function("dragapproved=false")

/* fim drag */

/* controles da janela */

var qtJanelasMinimizadas = 0;
var zIndex=1;
var minWidth = 200;

function showPanel(parent, name, width, height, isDraggable, top, left){	
		if(document.getElementById(name)!=null){
			document.getElementById(name).style.zIndex=zIndex++;
			return;
		}
		
		if(top==null || left==null){
			left = (document.body.clientWidth - width) / 2;
			top = (document.body.clientHeight - height) / 2;
		}
		
		//criando janela
		divJanela = document.createElement("div");
		divJanela.id = name;
		divJanela.tLeft = 0;
		divJanela.tTop = 0;
		divJanela.tipo = "janela";
		if(isDraggable)
			divJanela.className = "drag";
		divJanela.style.position = "absolute";
		divJanela.style.width = width;
		divJanela.style.height = height;
		divJanela.style.zIndex = zIndex++;
		divJanela.style.left = left;
		divJanela.style.top = top;
		divJanela.style.border = "outset #FFFFFF 2px";
		
		parent.appendChild(divJanela);
}

/* JANELA */
function showWindow(name, title, width, height, url, isDraggable, top, left, actionOnClose){
		
		if(document.getElementById(name)!=null){
			document.getElementById(name).style.zIndex=zIndex++;
			return;
		}
		
		if(top==null || left==null){
			left = (document.body.clientWidth - width) / 2;
			top = (document.body.clientHeight - height) / 2;
		}
		
		ifrRef = document.createElement("IFRAME");
		ifrRef.id = 'ifr_'+name;
		ifrRef.style.position = "absolute";
		ifrRef.style.width = width;
		ifrRef.style.height = height;
		ifrRef.style.zIndex = zIndex++;
		ifrRef.style.left = left;
		ifrRef.style.top = top;
		ifrRef.style.display = "block";
		
		//criando janela
		divJanela = document.createElement("div");
		divJanela.id = name;
		divJanela.tLeft = 0;
		divJanela.tTop = 0;
		divJanela.tipo = "janela";
		if(isDraggable)
			divJanela.className = "drag";
		divJanela.style.position = "absolute";
		divJanela.style.width = width;
		divJanela.style.height = height;
		divJanela.style.zIndex = zIndex++;
		divJanela.style.left = left;
		divJanela.style.top = top;
		divJanela.style.border = "outset #FFFFFF 2px";
	
		tableObj = document.createElement("TABLE");
		tableObj.setAttribute("width","100%");
		tableObj.setAttribute("height","100%");
		tableObj.setAttribute("border", "0");
		tableObj.setAttribute("cellpadding","0");
		tableObj.cellSpacing = 0;
		tableObj.className = "systemColor3 janela";
		tableObj.onclick = function(){
			document.getElementById(name).style.zIndex = zIndex++;
		}

		tbodyObj = document.createElement("TBODY");
		tbodyObj.style.borderSpacing = 0;

		tr1Obj = document.createElement("TR");

		tr1td1Obj = document.createElement("TD");
		tr1td1Obj.style.width = '65%';
		tr1td1Obj.style.height = 20;
		tr1td1Obj.style.cursor = 'default';
		tr1td1Obj.setAttribute("nowrap","nowrap");
		tr1td1Obj.className = "systemColor1 titulo";
		tr1td1Obj.id = "titulo_"+name;
		
		var tituloTexto = document.createTextNode(title);

		tr1td2Obj = document.createElement("TD");
		tr1td2Obj.setAttribute("nowrap","nowrap");
		tr1td2Obj.setAttribute("align","left");
		tr1td2Obj.style.padding=0;

		tr1td2Image = document.createElement("IMG");
		tr1td2Image.src = "imagens/title_transicion.gif";
		tr1td2Image.width = 45;
		tr1td2Image.height = 21;
		
		tr1td3Obj = document.createElement("TD");
		tr1td3Obj.setAttribute("align","right");
		tr1td3Obj.setAttribute("nowrap","nowrap");
		
		
		btMinimizar = document.createElement("IMG");
		btMinimizar.tipo = 'btMinimizar';
		btMinimizar.src = "imagens/minimize.gif";
		btMinimizar.width = 14;
		btMinimizar.height = 14;
		btMinimizar.className = "botaoBarra";
		btMinimizar.id = "btMinimizar_"+name;
		btMinimizar.onmouseover = function(){
			slowhigh(this)
		}
		btMinimizar.onmouseout = function(){
			slowlow(this)
		}
		btMinimizar.onclick = function(){
			minimizeWindow(name, document.getElementById(name).tLeft, document.getElementById(name).tTop);
		}

		btMaximizar = document.createElement("IMG");
		btMaximizar.src = "imagens/maximize.gif";
		btMaximizar.width = 14;
		btMaximizar.height = 14;
		btMaximizar.className = "botaoBarra";
		btMaximizar.id = "btMaximizar_"+name;
		btMaximizar.style.display = 'none';
		btMaximizar.onmouseover = function(){
			slowhigh(this)
		}
		btMaximizar.onmouseout = function(){
			slowlow(this)
		}
		btMaximizar.onclick = function(){
			maximizeWindow(name, title, width, height, top, left);
		}
		tableObj.ondblclick = btMaximizar.onclick;
		
		btFechar = document.createElement("IMG");
		btFechar.src = "imagens/close.gif";
		btFechar.width = 14;
		btFechar.height = 14;
		btFechar.className = "botaoBarra";
		btFechar.id = "btFechar" + name;
		btFechar.name = "btFechar" + name;
		btFechar.onmouseover = function(){
			slowhigh(this)
		}
		btFechar.onmouseout = function(){
			slowlow(this)
		}

		btFechar.onclick = function(){
			if(actionOnClose!=null)
				actionOnClose();
			closeWindow(name);
		}
		
		
		tr2Obj = document.createElement("TR");

		tr2td1Obj = document.createElement("TD");
		tr2td1Obj.colSpan = 3;
		tr2td1Obj.setAttribute("nowrap","nowrap");
		
		var iframe = document.createElement("IFRAME");
		iframe.name = "content_"+name;
		iframe.id = "content_"+name;
		iframe.frameBorder = 0;
		iframe.scrolling = "no";
		iframe.src = url;
		iframe.style.width = '100%';
		iframe.style.height = '100%';

		divJanela.appendChild(tableObj);

		returnValue = tableObj.appendChild(tbodyObj);

		tbodyObj.appendChild(tr1Obj);
		tbodyObj.appendChild(tr2Obj);
	
		tr1Obj.appendChild(tr1td1Obj);
		tr1Obj.appendChild(tr1td2Obj);
		tr1Obj.appendChild(tr1td3Obj);
		tr2Obj.appendChild(tr2td1Obj);
	
		tr1td1Obj.appendChild(tituloTexto);
		
		tr1td2Obj.appendChild(tr1td2Image);
		
		tr1td3Obj.appendChild(btMinimizar);
		tr1td3Obj.appendChild(btMaximizar);
		tr1td3Obj.appendChild(btFechar);
		
		tr2td1Obj.appendChild(iframe);
		
		eBody = document.getElementsByTagName('body').item(0);
		eBody.appendChild(ifrRef);
		eBody.appendChild(divJanela);
}

/* MESSAGE BOX */
function showMsgbox(title, width, height, msg, action, top, left){
		var name = 'dialog';
		
		if(document.getElementById(name)!=null){
			document.getElementById(name).style.zIndex=zIndex++;
			return;
		}
		
		if(top==null || top==-1 || left==null || left==-1){
			left = (document.body.clientWidth - width) / 2;
			top = (document.body.clientHeight - height) / 2;
		}
		
		ifrRef = document.createElement("IFRAME");
		ifrRef.id = 'ifr_'+name;
		ifrRef.style.position = "absolute";
		ifrRef.style.width = width;
		ifrRef.style.height = height;
		ifrRef.style.zIndex = zIndex++;
		ifrRef.style.left = left;
		ifrRef.style.top = top;
		ifrRef.style.display = "block";
		
		//criando janela
		divJanela = document.createElement("div");
		divJanela.id = name;
		divJanela.tLeft = 0;
		divJanela.tTop = 0;
		divJanela.tipo = "janela";
		/*divJanela.className = "drag";*/
		divJanela.style.position = "absolute";
		divJanela.style.width = width;
		divJanela.style.height = height;
		divJanela.style.zIndex = zIndex++;
		divJanela.style.left = left;
		divJanela.style.top = top;
		divJanela.style.border = "outset #FFFFFF 2px";
	
		tableObj = document.createElement("TABLE");
		tableObj.setAttribute("width","100%");
		tableObj.setAttribute("height","100%");
		tableObj.setAttribute("border", "0");
		tableObj.setAttribute("cellpadding","0");
		tableObj.cellSpacing = 0;
		tableObj.className = "systemColor3 janela";
		tableObj.onclick = function(){
			document.getElementById(name).style.zIndex = zIndex++;
		}

		tbodyObj = document.createElement("TBODY");
		tbodyObj.style.borderSpacing = 0;

		tr1Obj = document.createElement("TR");

		tr1td1Obj = document.createElement("TD");
		tr1td1Obj.style.width = '65%';
		tr1td1Obj.style.height = 20;
		tr1td1Obj.style.cursor = 'default';
		tr1td1Obj.setAttribute("nowrap","nowrap");
		tr1td1Obj.className = "systemColor1 titulo";
		tr1td1Obj.id = "titulo_"+name;
		
		var tituloTexto = document.createTextNode(title);

		tr1td2Obj = document.createElement("TD");
		tr1td2Obj.setAttribute("nowrap","nowrap");
		tr1td2Obj.setAttribute("align","left");
		tr1td2Obj.style.padding=0;
		
		tr1td2Image = document.createElement("IMG");
		tr1td2Image.src = "imagens/title_transicion.gif";
		tr1td2Image.width = 45;
		tr1td2Image.height = 21;
		
		tr1td3Obj = document.createElement("TD");
		tr1td3Obj.setAttribute("align","right");
		tr1td3Obj.setAttribute("nowrap","nowrap");
		
		btFechar = document.createElement("IMG");
		btFechar.src = "imagens/close.gif";
		btFechar.width = 14;
		btFechar.height = 14;
		btFechar.className = "botaoBarra";
		btFechar.id = "btFechar_"+name;
		btFechar.onmouseover = function(){
			slowhigh(this)
		}
		btFechar.onmouseout = function(){
			slowlow(this)
		}
        btFechar.onclick = function(){
			closeWindow(name);
		}
		
		
		tr2Obj = document.createElement("TR");

		tr2td1Obj = document.createElement("TD");
		tr2td1Obj.colSpan = 3;
		tr2td1Obj.setAttribute("nowrap","nowrap");
		
		tableInfo = document.createElement("TABLE");
		tableInfo.setAttribute("width","100%");
		tableInfo.setAttribute("height","100%");
		tableInfo.setAttribute("border", "0");
		tableInfo.setAttribute("cellpadding","0");
		tableInfo.cellSpacing = 0;

		tbodyInfo = document.createElement("TBODY");
		tbodyInfo.style.borderSpacing = 0;

		trInfo = document.createElement("TR");
		
		trtd1Info = document.createElement("TD");
		trtd1Info.style.backgroundColor="#E5E5E5";
		trtd1Info.style.width = 40;
		trtd1Info.setAttribute("nowrap","nowrap");
		trtd1Info.setAttribute("align","right");
		
		trtd2Info = document.createElement("TD");
		trtd2Info.className="textoMsgbox";
		
		msgboxInfo = document.createElement("IMG");
		msgboxInfo.src = "imagens/msgbox_info.gif";
		msgboxInfo.width = 32;
		msgboxInfo.height = 32;

		var mensagem = document.createTextNode(msg);

		trInfo2 = document.createElement("TR");
		
		trtd1Info2 = document.createElement("TD");
		trtd1Info2.style.backgroundColor="#E5E5E5";
		trtd1Info2.style.padding=6;
		trtd1Info2.colSpan = 2;
		trtd1Info2.setAttribute("align","center");

		buttonInfo = document.createElement("BUTTON");
		buttonInfo.className = 'botao';
		buttonInfo.style.width = 100;
		buttonInfo.onclick = function(){
			if(action!=null)
				action();
			closeWindow(name);
		}
			btFechar.onclick;	
		buttonInfo.onblur = btFechar.onclick;	
		
		txtButtonInfo = document.createTextNode("Ok");

		tableInfo.appendChild(tbodyInfo);
		tbodyInfo.appendChild(trInfo);
		tbodyInfo.appendChild(trInfo2);
		trInfo.appendChild(trtd1Info);
		trInfo.appendChild(trtd2Info);
		trInfo2.appendChild(trtd1Info2);
		trtd1Info.appendChild(msgboxInfo);
		trtd2Info.appendChild(mensagem);
		trtd1Info2.appendChild(buttonInfo);
		buttonInfo.appendChild(txtButtonInfo);
		
		divJanela.appendChild(tableObj);

		returnValue = tableObj.appendChild(tbodyObj);

		tbodyObj.appendChild(tr1Obj);
		tbodyObj.appendChild(tr2Obj);
	
		tr1Obj.appendChild(tr1td1Obj);
		tr1Obj.appendChild(tr1td2Obj);
		tr1Obj.appendChild(tr1td3Obj);
		tr2Obj.appendChild(tr2td1Obj);
	
		tr1td1Obj.appendChild(tituloTexto);
		
		tr1td2Obj.appendChild(tr1td2Image);
		
		tr1td3Obj.appendChild(btFechar);

		tr2td1Obj.appendChild(tableInfo);
		
		eBody = document.getElementsByTagName('body').item(0);
		eBody.appendChild(ifrRef);
		eBody.appendChild(divJanela);
}

/* PROMPT BOX */
function showPromptbox(title, width, height, msg, fields, action, top, left){
		var name = 'dialog';
		
		if(document.getElementById(name)!=null){
			document.getElementById(name).style.zIndex=zIndex++;
			return;
		}
		
		if(top==null || top==-1 || left==null || left==-1){
			left = (document.body.clientWidth - width) / 2;
			top = (document.body.clientHeight - height) / 2;
		}
		
		ifrRef = document.createElement("IFRAME");
		ifrRef.id = 'ifr_'+name;
		ifrRef.style.position = "absolute";
		ifrRef.style.width = width;
		ifrRef.style.height = height;
		ifrRef.style.zIndex = zIndex++;
		ifrRef.style.left = left;
		ifrRef.style.top = top;
		ifrRef.style.display = "block";
		
		//criando janela
		divJanela = document.createElement("div");
		divJanela.id = name;
		divJanela.tLeft = 0;
		divJanela.tTop = 0;
		divJanela.tipo = "janela";
		/*divJanela.className = "drag";*/
		divJanela.style.position = "absolute";
		divJanela.style.width = width;
		divJanela.style.height = height;
		divJanela.style.zIndex = zIndex++;
		divJanela.style.left = left;
		divJanela.style.top = top;
		divJanela.style.border = "outset #FFFFFF 2px";
	
		tableObj = document.createElement("TABLE");
		tableObj.setAttribute("width","100%");
		tableObj.setAttribute("height","100%");
		tableObj.setAttribute("border", "0");
		tableObj.setAttribute("cellpadding","0");
		tableObj.cellSpacing = 0;
		tableObj.className = "systemColor3 janela";
		tableObj.onclick = function(){
			document.getElementById(name).style.zIndex = zIndex++;
		}

		tbodyObj = document.createElement("TBODY");
		tbodyObj.style.borderSpacing = 0;

		tr1Obj = document.createElement("TR");

		tr1td1Obj = document.createElement("TD");
		tr1td1Obj.style.width = '65%';
		tr1td1Obj.style.height = 20;
		tr1td1Obj.style.cursor = 'default';
		tr1td1Obj.setAttribute("nowrap","nowrap");
		tr1td1Obj.className = "systemColor1 titulo";
		tr1td1Obj.id = "titulo_"+name;
		
		var tituloTexto = document.createTextNode(title);

		tr1td2Obj = document.createElement("TD");
		tr1td2Obj.setAttribute("nowrap","nowrap");
		tr1td2Obj.setAttribute("align","left");
		tr1td2Obj.style.padding=0;
		
		tr1td2Image = document.createElement("IMG");
		tr1td2Image.src = "imagens/title_transicion.gif";
		tr1td2Image.width = 45;
		tr1td2Image.height = 21;
		
		tr1td3Obj = document.createElement("TD");
		tr1td3Obj.setAttribute("align","right");
		tr1td3Obj.setAttribute("nowrap","nowrap");
		
		btFechar = document.createElement("IMG");
		btFechar.src = "imagens/close.gif";
		btFechar.width = 14;
		btFechar.height = 14;
		btFechar.className = "botaoBarra";
		btFechar.id = "btFechar_"+name;
		btFechar.onmouseover = function(){
			slowhigh(this)
		}
		btFechar.onmouseout = function(){
			slowlow(this)
		}
        btFechar.onclick = function(){
			closeWindow(name);
		}
		
		
		tr2Obj = document.createElement("TR");

		tr2td1Obj = document.createElement("TD");
		tr2td1Obj.colSpan = 3;
		tr2td1Obj.setAttribute("nowrap","nowrap");
		
		tableInfo = document.createElement("TABLE");
		tableInfo.setAttribute("width","100%");
		tableInfo.setAttribute("height","100%");
		tableInfo.setAttribute("border", "0");
		tableInfo.setAttribute("cellpadding","0");
		tableInfo.cellSpacing = 0;

		tbodyInfo = document.createElement("TBODY");
		tbodyInfo.style.borderSpacing = 0;

		trInfo = document.createElement("TR");
		
		trtd1Info = document.createElement("TD");
		trtd1Info.style.backgroundColor="#E5E5E5";
		trtd1Info.style.width = 40;
		trtd1Info.setAttribute("nowrap","nowrap");
		trtd1Info.setAttribute("align","right");
		
		trtd2Info = document.createElement("TD");
		trtd2Info.className="textoMsgbox";
		
		msgboxInfo = document.createElement("IMG");
		msgboxInfo.src = "imagens/msgbox_info.gif";
		msgboxInfo.width = 32;
		msgboxInfo.height = 32;

		var mensagem = document.createTextNode(msg);
		
		tableInfo.appendChild(tbodyInfo);
		tbodyInfo.appendChild(trInfo);
		trInfo.appendChild(trtd1Info);
		trInfo.appendChild(trtd2Info);
		trtd1Info.appendChild(msgboxInfo);
		trtd2Info.appendChild(mensagem);


		/*FIELDS*/
		for(var i=0; i<fields.length; i++){
			var trFields = document.createElement("TR");
			
			var trtd1Fields = document.createElement("TD");
			trtd1Fields.style.backgroundColor="#E5E5E5";
			trtd1Fields.style.padding=6;
			trtd1Fields.className='rotulo';
			trtd1Fields.colSpan = 2;
			
			var inputLabel = document.createTextNode(fields[i][0]);
			
			var input = document.createElement("INPUT");
			input.type = 'TEXT';
			input.className='edit100';
			input.name = 'field'+i;
			input.id = 'field'+i;
			input.retorno = fields[i][1];
			
			tbodyInfo.appendChild(trFields);
			trFields.appendChild(trtd1Fields);
			trtd1Fields.appendChild(inputLabel);
			trtd1Fields.appendChild(input);
		}

		
		/*BOTOES*/		
		trInfo2 = document.createElement("TR");
		
		trtd1Info2 = document.createElement("TD");
		trtd1Info2.style.backgroundColor="#E5E5E5";
		trtd1Info2.style.padding=6;
		trtd1Info2.colSpan = 2;
		trtd1Info2.setAttribute("align","center");

		buttonInfo = document.createElement("BUTTON");
		buttonInfo.className = 'botao';
		buttonInfo.style.width = 100;
		buttonInfo.onclick = function(){
			for(var i=0; i<fields.length; i++){
				if(fields[i][2]=='VAR')
					fields[i][1] = document.getElementById('field'+i).value;
				else if(fields[i][2]=='EDIT')
					fields[i][1].value = document.getElementById('field'+i).value;
			}
			if(action!=null)
				action();
			closeWindow(name);
		}
		
		txtButtonInfo = document.createTextNode("Ok");

		tbodyInfo.appendChild(trInfo2);
		trInfo2.appendChild(trtd1Info2);
		trtd1Info2.appendChild(buttonInfo);
		buttonInfo.appendChild(txtButtonInfo);

		/*MONTAGEM JANELA*/
	
		divJanela.appendChild(tableObj);

		returnValue = tableObj.appendChild(tbodyObj);

		tbodyObj.appendChild(tr1Obj);
		tbodyObj.appendChild(tr2Obj);
	
		tr1Obj.appendChild(tr1td1Obj);
		tr1Obj.appendChild(tr1td2Obj);
		tr1Obj.appendChild(tr1td3Obj);
		tr2Obj.appendChild(tr2td1Obj);
	
		tr1td1Obj.appendChild(tituloTexto);
		
		tr1td2Obj.appendChild(tr1td2Image);
		
		tr1td3Obj.appendChild(btFechar);

		tr2td1Obj.appendChild(tableInfo);
		
		eBody = document.getElementsByTagName('body').item(0);
		eBody.appendChild(ifrRef);
		eBody.appendChild(divJanela);
}

/* TEMPORARY BOX */
function showTempBox(title, width, height, msg, time, isTitled, top, left, nameOfDialogo){
		var name = nameOfDialogo==null ? 'dialog' : nameOfDialogo;
		
		if(document.getElementById(name)!=null){
			document.getElementById(name).style.zIndex=zIndex++;
			return;
		}
			
		if(top==null || left==null){
			left = (document.body.clientWidth - width) / 2;
			top = (document.body.clientHeight - height) / 2;
		}
		
		ifrRef = document.createElement("IFRAME");
		ifrRef.id = 'ifr_'+name;
		ifrRef.style.position = "absolute";
		ifrRef.style.width = width;
		ifrRef.style.height = height;
		ifrRef.style.zIndex = zIndex++;
		ifrRef.style.left = left;
		ifrRef.style.top = top;
		ifrRef.style.display = "block";
		
		//criando janela
		divJanela = document.createElement("div");
		divJanela.id = name;
		divJanela.tLeft = 0;
		divJanela.tTop = 0;
		divJanela.tipo = "janela";
		/*divJanela.className = "drag";*/
		divJanela.style.position = "absolute";
		divJanela.style.width = width;
		divJanela.style.height = height;
		divJanela.style.zIndex = zIndex++;
		divJanela.style.left = left;
		divJanela.style.top = top;
		divJanela.style.border = "outset #FFFFFF 2px";
	
		tableObj = document.createElement("TABLE");
		tableObj.setAttribute("width","100%");
		tableObj.setAttribute("height","100%");
		tableObj.setAttribute("border", "0");
		tableObj.setAttribute("cellpadding","0");
		tableObj.cellSpacing = 0;
		tableObj.className = "systemColor3 janela";
		tableObj.onclick = function(){
			document.getElementById(name).style.zIndex = zIndex++;
		}

		tbodyObj = document.createElement("TBODY");
		tbodyObj.style.borderSpacing = 0;

		if(isTitled || time==0){
			tr1Obj = document.createElement("TR");
	
			tr1td1Obj = document.createElement("TD");
			tr1td1Obj.style.width = '65%';
			tr1td1Obj.style.height = 20;
			tr1td1Obj.style.cursor = 'default';
			tr1td1Obj.setAttribute("nowrap","nowrap");
			tr1td1Obj.className = "systemColor1 titulo";
			tr1td1Obj.id = "titulo_"+name;
			
			var tituloTexto = document.createTextNode((title!=null && title!='') ? title : 'Atenção');
	
			tr1td2Obj = document.createElement("TD");
			tr1td2Obj.setAttribute("nowrap","nowrap");
			tr1td2Obj.setAttribute("align","left");
			tr1td2Obj.style.padding=0;
			
			tr1td2Image = document.createElement("IMG");
			tr1td2Image.src = "imagens/title_transicion.gif";
			tr1td2Image.width = 45;
			tr1td2Image.height = 21;
			
			tr1td3Obj = document.createElement("TD");
			tr1td3Obj.setAttribute("align","right");
			tr1td3Obj.setAttribute("nowrap","nowrap");
			
			btFechar = document.createElement("IMG");
			btFechar.src = "imagens/close.gif";
			btFechar.width = 14;
			btFechar.height = 14;
			btFechar.className = "botaoBarra";
			btFechar.id = "btFechar_"+name;
			btFechar.onmouseover = function(){
				slowhigh(this)
			}
			btFechar.onmouseout = function(){
				slowlow(this)
			}
			btFechar.onclick = function(){
				closeWindow(name);
			}
		}
		
		tr2Obj = document.createElement("TR");

		tr2td1Obj = document.createElement("TD");
		tr2td1Obj.colSpan = 3;
		tr2td1Obj.setAttribute("nowrap","nowrap");
		
		tableInfo = document.createElement("TABLE");
		tableInfo.setAttribute("width","100%");
		tableInfo.setAttribute("height","100%");
		tableInfo.setAttribute("border", "0");
		tableInfo.setAttribute("cellpadding","0");
		tableInfo.cellSpacing = 0;

		tbodyInfo = document.createElement("TBODY");
		tbodyInfo.style.borderSpacing = 0;

		trInfo = document.createElement("TR");
		
		trtd1Info = document.createElement("TD");
		trtd1Info.style.backgroundColor="#E5E5E5";
		trtd1Info.style.width = 40;
		trtd1Info.setAttribute("nowrap","nowrap");
		trtd1Info.setAttribute("align","right");
		
		trtd2Info = document.createElement("TD");
		trtd2Info.className="textoMsgbox";
		
		msgboxInfo = document.createElement("IMG");
		msgboxInfo.src = "imagens/msgbox_info.gif";
		msgboxInfo.width = 32;
		msgboxInfo.height = 32;

		var mensagem = document.createTextNode(msg);


		tableInfo.appendChild(tbodyInfo);
		tbodyInfo.appendChild(trInfo);
		trInfo.appendChild(trtd1Info);
		trInfo.appendChild(trtd2Info);
		trtd1Info.appendChild(msgboxInfo);
		trtd2Info.appendChild(mensagem);
		
		divJanela.appendChild(tableObj);

		returnValue = tableObj.appendChild(tbodyObj);

		if(isTitled || time==0){
			tbodyObj.appendChild(tr1Obj);
			tr1Obj.appendChild(tr1td1Obj);
			tr1Obj.appendChild(tr1td2Obj);
			tr1Obj.appendChild(tr1td3Obj);
			tr1td1Obj.appendChild(tituloTexto);
			tr1td2Obj.appendChild(tr1td2Image);
			tr1td3Obj.appendChild(btFechar);
		}
		
		tbodyObj.appendChild(tr2Obj);
		tr2Obj.appendChild(tr2td1Obj);
		tr2td1Obj.appendChild(tableInfo);
		
		eBody = document.getElementsByTagName('body').item(0);
		eBody.appendChild(ifrRef);
		eBody.appendChild(divJanela);
		
		if(time!=0)
			setTimeout("closeWindow('"+name+"')", time);
}

function showConfirmbox(title, width, height, msg, action, top, left){
		var name = 'dialog';
		
		if(document.getElementById(name)!=null){
			document.getElementById(name).style.zIndex=zIndex++;
			return;
		}
		
		if(top==null || left==null){
			left = (document.body.clientWidth - width) / 2;
			top = (document.body.clientHeight - height) / 2;
		}
		
		ifrRef = document.createElement("IFRAME");
		ifrRef.id = 'ifr_'+name;
		ifrRef.style.position = "absolute";
		ifrRef.style.width = width;
		ifrRef.style.height = height;
		ifrRef.style.zIndex = zIndex++;
		ifrRef.style.left = left;
		ifrRef.style.top = top;
		ifrRef.style.display = "block";
		
		//criando janela
		divJanela = document.createElement("div");
		divJanela.id = name;
		divJanela.tLeft = 0;
		divJanela.tTop = 0;
		divJanela.tipo = "janela";
		/*divJanela.className = "drag";*/
		divJanela.style.position = "absolute";
		divJanela.style.width = width;
		divJanela.style.height = height;
		divJanela.style.zIndex = zIndex++;
		divJanela.style.left = left;
		divJanela.style.top = top;
		divJanela.style.display = "block";
		divJanela.style.border = "outset #FFFFFF 2px";
		
		tableObj = document.createElement("TABLE");
		tableObj.setAttribute("width","100%");
		tableObj.setAttribute("height","100%");
		tableObj.setAttribute("border", "0");
		tableObj.setAttribute("cellpadding","0");
		tableObj.cellSpacing = 0;
		tableObj.className = "systemColor3 janela";
		tableObj.onclick = function(){
			document.getElementById(name).style.zIndex = zIndex++;
		}

		tbodyObj = document.createElement("TBODY");
		tbodyObj.style.borderSpacing = 0;

		tr1Obj = document.createElement("TR");

		tr1td1Obj = document.createElement("TD");
		tr1td1Obj.style.width = '65%';
		tr1td1Obj.style.height = 20;
		tr1td1Obj.style.cursor = 'default';
		tr1td1Obj.setAttribute("nowrap","nowrap");
		tr1td1Obj.className = "systemColor1 titulo";
		tr1td1Obj.id = "titulo_"+name;
		
		var tituloTexto = document.createTextNode(title);

		tr1td2Obj = document.createElement("TD");
		tr1td2Obj.setAttribute("nowrap","nowrap");
		tr1td2Obj.setAttribute("align","left");
		tr1td2Obj.style.padding=0;
		
		tr1td2Image = document.createElement("IMG");
		tr1td2Image.src = "imagens/title_transicion.gif";
		tr1td2Image.width = 45;
		tr1td2Image.height = 21;
		
		tr1td3Obj = document.createElement("TD");
		tr1td3Obj.setAttribute("align","right");
		tr1td3Obj.setAttribute("nowrap","nowrap");
		
		btFechar = document.createElement("IMG");
		btFechar.src = "imagens/close.gif";
		btFechar.width = 14;
		btFechar.height = 14;
		btFechar.className = "botaoBarra";
		btFechar.id = "btFechar_"+name;
		btFechar.onmouseover = function(){
			slowhigh(this)
		}
		btFechar.onmouseout = function(){
			slowlow(this)
		}
		btFechar.onclick = function(){
			closeWindow(name);
		}
		
		tr2Obj = document.createElement("TR");

		tr2td1Obj = document.createElement("TD");
		tr2td1Obj.colSpan = 3;
		tr2td1Obj.setAttribute("nowrap","nowrap");
		
		tableInfo = document.createElement("TABLE");
		tableInfo.setAttribute("width","100%");
		tableInfo.setAttribute("height","100%");
		tableInfo.setAttribute("border", "0");
		tableInfo.setAttribute("cellpadding","0");
		tableInfo.cellSpacing = 0;

		tbodyInfo = document.createElement("TBODY");
		tbodyInfo.style.borderSpacing = 0;

		trInfo = document.createElement("TR");
		
		trtd1Info = document.createElement("TD");
		trtd1Info.style.backgroundColor="#E5E5E5";
		trtd1Info.style.width = 40;
		trtd1Info.setAttribute("nowrap","nowrap");
		trtd1Info.setAttribute("align","right");
		
		trtd2Info = document.createElement("TD");
		trtd2Info.className="textoMsgbox";
		
		msgboxInfo = document.createElement("IMG");
		msgboxInfo.src = "imagens/msgbox_info.gif";
		msgboxInfo.width = 32;
		msgboxInfo.height = 32;

		var mensagem = document.createTextNode(msg);

		trInfo2 = document.createElement("TR");
		
		trtd1Info2 = document.createElement("TD");
		trtd1Info2.style.backgroundColor="#E5E5E5";
		trtd1Info2.style.padding=6;
		trtd1Info2.colSpan = 2;
		trtd1Info2.setAttribute("align","center");

		buttonOK = document.createElement("BUTTON");
		buttonOK.className = 'botao';
		buttonOK.style.width = 70;
		buttonOK.onclick = function(){
			if(action!=null)
				action();
			closeWindow(name);
		}
		
		txtButtonOK = document.createTextNode("Ok");

		buttonCANCEL = document.createElement("BUTTON");
		buttonCANCEL.className = 'botao';
		buttonCANCEL.style.width = 70;
		buttonCANCEL.style.marginLeft = 3;
		buttonCANCEL.onclick = btFechar.onclick;
		
		txtButtonCANCEL = document.createTextNode("Cancelar");


		tableInfo.appendChild(tbodyInfo);
		tbodyInfo.appendChild(trInfo);
		tbodyInfo.appendChild(trInfo2);
		trInfo.appendChild(trtd1Info);
		trInfo.appendChild(trtd2Info);
		trInfo2.appendChild(trtd1Info2);
		trtd1Info.appendChild(msgboxInfo);
		trtd2Info.appendChild(mensagem);
		
		trtd1Info2.appendChild(buttonOK);
		buttonOK.appendChild(txtButtonOK);
		
		trtd1Info2.appendChild(buttonCANCEL);
		buttonCANCEL.appendChild(txtButtonCANCEL);
		
		divJanela.appendChild(tableObj);

		returnValue = tableObj.appendChild(tbodyObj);

		tbodyObj.appendChild(tr1Obj);
		tbodyObj.appendChild(tr2Obj);
	
		tr1Obj.appendChild(tr1td1Obj);
		tr1Obj.appendChild(tr1td2Obj);
		tr1Obj.appendChild(tr1td3Obj);
		tr2Obj.appendChild(tr2td1Obj);
		
		tr1td1Obj.appendChild(tituloTexto);
		
		tr1td2Obj.appendChild(tr1td2Image);
		
		tr1td3Obj.appendChild(btFechar);

		tr2td1Obj.appendChild(tableInfo);
		
		eBody = document.getElementsByTagName('body').item(0);
		eBody.appendChild(ifrRef);
		eBody.appendChild(divJanela);
}

function getWindowById(idJanela){
	var janelas = document.getElementsByTagName('DIV');
	var janelaSelecionada = null;
	for(var i=0; i<janelas.length; i++)
		if(janelas[i].tipo!=null && janelas[i].tipo=='janela' && janelas[i].id==idJanela) {
			janelaSelecionada = document.getElementById('content_' + idJanela);
			break;
		}
	return janelaSelecionada;
}

function miniminizeAllWindow(){
	var janelas = document.getElementsByTagName('DIV');
	for(var i=0; i<janelas.length; i++){
		if(janelas[i].tipo!=null && janelas[i].tipo=='janela')
			minimizeWindow(janelas[i].id,janelas[i].tLeft,janelas[i].tTop);
	}
}

function closeAllWindow(){
	var janelas = document.getElementsByTagName('DIV');
	for(var i=0; i<janelas.length; i++){
		if(janelas[i].tipo!=null && janelas[i].tipo=='janela')
			closeWindow(janelas[i].id);
	}
}

function minimizeWindow(windowName, left, top){
	document.getElementById('ifr_'+windowName).style.display = 'none';
	document.getElementById('content_'+windowName).style.display = 'none';
	document.getElementById('titulo_'+windowName).firstChild.nodeValue = resumir(document.getElementById('titulo_'+windowName).firstChild.nodeValue, 10)+'...';
	document.getElementById('btMinimizar_'+windowName).style.display='none';
	document.getElementById('btMaximizar_'+windowName).style.display=''; 
	var janela = document.getElementById(windowName);
	janela.style.height = 20;
	janela.style.width = minWidth;
	janela.style.left = (left!=null && left!=0) ? left : qtJanelasMinimizadas*10;
	janela.style.top = (top!=null && top!=0) ? top : (document.body.clientHeight-25)-qtJanelasMinimizadas*10;
	qtJanelasMinimizadas++;
	janela.className = "drag";
}

function maximizeWindow(windowName, title, width, height, top, left){
	document.getElementById('ifr_'+windowName).style.display = '';
	document.getElementById('content_'+windowName).style.display = '';
	var janela = document.getElementById(windowName);
	janela.tLeft = janela.style.left;
	janela.tTop = janela.style.top;
	janela.style.height = height;
	janela.style.width = width;
	janela.style.left = left;
	janela.style.top = top;
	document.getElementById('btMaximizar_'+windowName).style.display='none';
	document.getElementById('btMinimizar_'+windowName).style.display=''; 
	document.getElementById('titulo_'+windowName).firstChild.nodeValue = title;
	document.getElementById(windowName).className = "";
}

function closeWindow(windowName){
	if (document.getElementById(windowName) != null) {
		document.body.removeChild(document.getElementById(windowName));
		if (document.getElementById('ifr_'+windowName) != null)
			document.body.removeChild(document.getElementById('ifr_'+windowName));
	}
}

function resumir(texto, tamanho){
	return (texto.length > tamanho) ? texto.substring(0, tamanho) : texto;
}

		