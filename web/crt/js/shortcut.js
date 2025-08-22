/* keyCode - keyDown/keyUp */
_shift = 16;
_ctrl = 17;
_alt = 18;
/* demais simbolos = ASCII caixa baixa*/
var keyMap = new Array();

function ShortCut(k, a){
	this.keys = k;
	this.action = a;
}

function addShortcut(shortcut, action){
	keyMap.push(new ShortCut(shortcut, action));
}

function listKeys(){
	var tmp = '';
	for(var i=0; i<keyMap.length; i++){
		for(var j=0; j<keyMap[i].keys.length; j++){
			if(j!=0)
				tmp+='+';
			tmp+=keyMap[i].keys[j];
		}
		tmp+='='+keyMap[i].action+'\n';
	}
	alert(tmp);
}

function getAction(shortcut){
	for(var i=0; i<keyMap.length; i++){
		if(keyMap[i].keys.toLowerCase()==shortcut.toLowerCase())
			return keyMap[i].action;
	}
	return null;
}

function processKeys(evt) {
    evt = (evt) ? evt : ((event) ? event : null);
    if (evt) {
		if(evt.ctrlKey || evt.shiftKey){
			var shortcut = '';
			
			if(evt.ctrlKey)
				shortcut+='ctrl+';
			if(evt.shiftKey)
				shortcut+='shift+';
			shortcut += String.fromCharCode(evt.keyCode);
			
			var action = getAction(shortcut);
			if(action!=null)
				action();
		}
	}
}

function enableTabEmulation()	{
	for(var i=0; i< document.forms.length; i++)	{
		var nextElement = null, lastControl = false;
		for (var j=document.form.elements.length-1; j>-1; j--) {
			var element = document.forms[i].elements[j];
			if(element.tagName.toLowerCase()!='fieldset' && (!element.type || element.type!='hidden'))	{
					if(element.tagName.toLowerCase()!='button' && !lastControl && document.getElementById('btSalvar'))	{
						lastControl = true;
						nextElement = document.getElementById('btSalvar');
					}
					if(!element.nextElement)
						element.nextElement = nextElement;
					if(element.tagName.toLowerCase()!='button') {
							if (navigator.appName.toLowerCase().indexOf('internet explorer') != -1)
								element.onkeyup = function(){	tabIE(this);	};
							else
								element.setAttribute("onkeyup","tab(this, event)");
					}
					nextElement = element;
			}
		}
	}
}

function tabIE(field){
	var intKeyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
	if(intKeyCode!=13)
		return;
	if (document.selection)
		document.selection.empty();
	else if (window.getSelection)
		window.getSelection().removeAllRanges();
	if(document.disabledTab)	{
		document.disabledTab = false;
		try{
			field.focus();
		}catch(e)	{};
		if(field.type=='text')
			field.select();
		return;
	}
	if ('eventOnEnter' in field)
		eval('(' + field.eventOnEnter + ')');
	else if(field.nextElement)	{
		if(field.nextElement.disabled)	{
			tab(field.nextElement);
			return;	
		}
		try {
			field.nextElement.focus();
		}catch(e)	{};
		if(field.nextElement.type=='text')
			field.nextElement.select();
	}
}

function tab(field, event){
	var intKeyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
	if(intKeyCode!=13)
		return;
	if (document.selection)
		document.selection.empty();
	else if (window.getSelection)
		window.getSelection().removeAllRanges();
	if(document.disabledTab)	{
		document.disabledTab = false;
		try {
			field.focus();
		}catch(e)	{};
		if(field.type=='text')
			field.select();
		return;
	}
	if ('eventOnEnter' in field)
		eval('(' + field.eventOnEnter + ')');
	else if(field.nextElement)	{
		if(field.nextElement.disabled)	{
			tab(field.nextElement);
			return;	
		}
		try {
			field.nextElement.focus();
		}catch(e)	{};
		if(field.nextElement.type=='text')
			field.nextElement.select();
	}
}

document.onkeyup=processKeys;