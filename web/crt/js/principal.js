function allToUpperCase(doc)	{
	for(var i=0; i< doc.forms.length; i++)	{
		for (var j=doc.form.elements.length-1; j>-1; j--) {
			var field = doc.forms[i].elements[j];
			if((field.tagName.toLowerCase()=='input' || field.tagName.toLowerCase()=='select' || 
				field.tagName.toLowerCase()=='textarea' || field.tagName.toLowerCase()=='radio') && 
				!(field.getAttribute('noUpperCase')))
			field.value = trim((field.value+'').toUpperCase());
		}
	}
	isCadastroDesabilitado = false;
}
function getFloatWellFormated(value)	{
	value = new String(value);
	if(trim(value)=='')	{
		return '0';
	}
	else	{
		while(value.indexOf('.')>=0)	{
			value = value.replace('.','');
		}
		value = value.replace(',','.');
	}
	return value;
}

function clearAllForm(doc)	{
	for(var i=0; i< doc.forms.length; i++)	{
		for (var j=doc.form.elements.length-1; j>-1; j--) {
			var element = doc.forms[i].elements[j];
			if(element.getAttribute('noClear') || element.getAttribute('noclear')  || !element.tagName)
				continue;
			if(element.tagName.toLowerCase()=='input' || element.tagName.toLowerCase()=='select' || 
			   element.tagName.toLowerCase()=='textarea' || element.tagName.toLowerCase()=='radio')
				if(element.getAttribute('valueToClear'))
					element.value=element.getAttribute('valueToClear');
				else if(element.getAttribute('valuetoclear'))
					element.value=element.getAttribute('valueToClear');
				else
					element.value='';
		}
	}
}

function setValueToFields(doc, reg)	{
	for(var i=0; i<doc.forms.length; i++)	{
		for (var j=doc.form.elements.length-1; j>-1; j--) {
			var field = doc.forms[i].elements[j];
			if(field.getAttribute('reference') && (field.getAttribute('reference') in reg))	{
				if(field.type && field.type=='checkbox')
					field.checked = reg[field.getAttribute('reference')]==field.value;
				else if(reg[field.getAttribute('reference')]!=null)
					field.value = (field.getAttribute('mascara')) ? txtFormat(reg[field.getAttribute('reference')], field.getAttribute('mascara')) : 
													  	 reg[field.getAttribute('reference')];
				else
					field.value = ''; 
			};
		}
	}
}

function openApplication(page, nameWindow) {
	var url = page;	
	var name = nameWindow;
	var width = 700;
	var height = 430; 
	var screenWidth = screen.width;
	var screenHeigth = screen.height;
	var newWindow = window.open(url, name, "width="+width+", height="+height+", status");
	newWindow.moveTo((screenWidth-width)/2,(screenHeigth-height)/2);
	newWindow.focus();	
}

function FormatNumber(num, nrCasas)	{
	return formatNumber(num, nrCasas);
}

function formatNumber(num, nrCasas, decimalSeparator)	{
	if(num==''||num=='null'||num==null)
		num='0';
	if(!decimalSeparator)
		decimalSeparator='.';
	if(decimalSeparator==',')
		num=num.replace('.','').replace(',','.');
	if(isNaN(num))
		num=0;
	if(nrCasas==null)
		nrCasas=2;
	var minus='';
	num = ''+num;
	if (num.lastIndexOf("-") >= 0) { 
		minus='-'; 
		num = num.replace('-','');
	}
	var decimal = parseFloat(num).toFixed(nrCasas)+'';
	decimal = decimal.substring(decimal.length-nrCasas);
	num = Math.floor(num);
	var samount = new String(num);
	for (var i = 0; i < Math.floor((samount.length-(1+i))/3); i++)          {
            samount = samount.substring(0, samount.length-(4*i+3)) + '.' + samount.substring(samount.length-(4*i+3));
    }
    return minus + samount + ',' + decimal;
}
function trim(str) {
	while (str.charAt(0) == " ")
		str = str.substr(1, str.length-1);
	while (str.charAt(str.length-1) == " ")
		str = str.substr(0,str.length-1);
	return str;
} 

function alterarStatusCampos(isAtivo, doc, nomeCompToFocus, classNameDisabled) {
	if (doc == null)
		return;
	for(var i=0; i< doc.forms.length; i++)	{
		for (var j=doc.form.elements.length-1; j>-1; j--) {
			var campo = doc.forms[i].elements[j];
			if ((campo.tagName.toLowerCase()=='input' || campo.tagName.toLowerCase()=='select' || 
			     campo.tagName.toLowerCase()=='textarea' || campo.tagName.toLowerCase()=='radio' ||
				 campo.tagName.toLowerCase()=='button') && !(campo.getAttribute('stateFix')!=null)) {
				if (campo.type=='textarea' || campo.type=='text' || campo.type=="password"){
					campo.setAttribute('classNameEnabled', campo.getAttribute('classNameEnabled')!=null ? campo.getAttribute('classNameEnabled') : campo.getAttribute('className'));
					
					if(!isAtivo && classNameDisabled!=null)
						campo.setAttribute('className', campo.getAttribute('className') + ' ' + classNameDisabled);
					else if(campo.getAttribute('classNameEnabled') != null)
						campo.setAttribute('className', campo.getAttribute('classNameEnabled'));
				}
				if(campo.getAttribute('noUpperCase')!=null)	{
					campo.style.textTransform='none';	
				}
				campo.setAttribute('oldStatus', campo.getAttribute('oldStatus')!=null ? campo.getAttribute('oldStatus') : campo.disabled);
				campo.disabled = isAtivo ? campo.getAttribute('oldStatus') : !isAtivo;
			}
		}
		if (nomeCompToFocus!=null) {
			var campo = doc.getElementById(nomeCompToFocus);
			if (campo != null && !campo.disabled)
				campo.focus();
		}
	}
}