// JavaScript Document
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=2100;

var VAL_CAMPO_NAO_PREENCHIDO = 0;
var VAL_CAMPO_INTEIRO = 1;
var VAL_CAMPO_INTEIRO_OBRIGATORIO = 2;
var VAL_CAMPO_PONTO_FLUTUANTE = 3;
var VAL_CAMPO_PONTO_FLUTUANTE_OBRIGATORIO = 4;
var VAL_CAMPO_DATA = 5;
var VAL_CAMPO_DATA_OBRIGATORIO = 6;
var VAL_CAMPO_HORARIO = 7;
var VAL_CAMPO_HORARIO_OBRIGATORIO = 8;
var VAL_CAMPO_VALOR_INVALIDO = 9;
var VAL_CAMPO_SENHA_NAO_PREENCHIDO = 10;
var VAL_CAMPO_MAIOR_QUE = 11;
var VAL_CAMPO_MENOR_QUE = 12;

function CalculaDigitoMod11(Dado, NumDig, LimMult)  {
  var Mult, Soma, i, n;
    
  for(n=1; n<=NumDig; n++)    {
    Soma = 0;
    Mult = 2;
    for(i=Dado.length-1; i>=0; i--)     {
      Soma += (Mult * parseInt(Dado.charAt(i)));
      if(++Mult > LimMult) 
	  	Mult = 2;
    }
    Dado += ((Soma * 10) % 11) % 10;
  }
  return Dado.substr(Dado.length-NumDig, NumDig);
}

function isCPF_Valido(nrCpf)	{
	nrCpf=nrCpf.replace('.', '');
	nrCpf=nrCpf.replace('.', '');
	nrCpf=trim(nrCpf.replace('-', ''));
	if(nrCpf.length == 0)
		return true;
	else if(nrCpf.length != 11)
		return false;
	return (CalculaDigitoMod11(nrCpf.substring(0,9), 2, 12) == nrCpf.substring(9,11));
}

function isPIS_Valido(nrPis)	{
	nrPis=nrPis.replace('.', '');
	nrPis=nrPis.replace('.', '');
	nrPis=trim(nrPis.replace('-', ''));
	if(nrPis.length == 0)
		return true;
	else if(nrPis.length != 11)
		return false;
	return (CalculaDigitoMod11(nrPis.substring(0,10), 1, 9) == parseInt(nrPis.substring(10,11)));
}

function isCNPJ_Valido(nrCnpj)	{
	nrCnpj=nrCnpj.replace('.', '');
	nrCnpj=nrCnpj.replace('.', '');
	nrCnpj=trim(nrCnpj.replace('/', ''));
	nrCnpj=trim(nrCnpj.replace('-', ''));
	if(nrCnpj.length == 0)
		return true;
	else if(nrCnpj.length != 14)
		return false;
	return (CalculaDigitoMod11(nrCnpj.substring(0,12), 2, 9) == parseInt(nrCnpj.substring(12,14)));
}

function validarCampos(campos, showMessagem, mensagem, focusToCampo) {
	var isValido = true;
	var msg = showMessagem ? '' : null;
	var msgCamposInvalidos = '';
	var campoToFocus = null;
	var i = 0;
	var dadosValidacaoCampo = null;
	var resValidacao = true;
	for (i=0; campos!=null && i<campos.length; i++) {
		dadosValidacaoCampo = campos[i];
		resValidacao = validarCampo(dadosValidacaoCampo[0], dadosValidacaoCampo[2], false, null, false, dadosValidacaoCampo[2]==VAL_CAMPO_VALOR_INVALIDO || dadosValidacaoCampo[2]==VAL_CAMPO_MAIOR_QUE || dadosValidacaoCampo[2]==VAL_CAMPO_MENOR_QUE? dadosValidacaoCampo[3] : null);
		if (!resValidacao) {
			msgCamposInvalidos += dadosValidacaoCampo[1] + '\n';
			isValido = false;
			if (focusToCampo && campoToFocus == null)
				campoToFocus = dadosValidacaoCampo[0];
		}
	}
	if (!isValido && msg != null)
		alert(mensagem + '\n' + msgCamposInvalidos);
	if (campoToFocus && (campoToFocus.type==null || campoToFocus.type!='hidden') && (campoToFocus.readOnly==null || !campoToFocus.readOnly) && !campoToFocus.disabled)
		campoToFocus.focus();
	return isValido;
}

function isCampoReadyOnly(campo, showMessagem, mensagem) {
	if (campo.readOnly && showMessagem)
		alert(mensagem);
	return campo.readOnly;
}

function isCampoDisabled(campo, showMessagem, mensagem) {
	if (campo.disabled && showMessagem)
		alert(mensagem);
	return campo.disabled;
}

function validarCampo(campo, tpValidacao, showMessagem, mensagem, focusToCampo, valorInvalido, functionBeforeFocus) {
	var resValidacao = true;
	if (campo != null) {
		campo.value = trim(campo.value);
		switch(tpValidacao) {
			case VAL_CAMPO_SENHA_NAO_PREENCHIDO: if (campo.value == '')
													resValidacao = false;
												 break;
			case VAL_CAMPO_NAO_PREENCHIDO: if (campo.value == '')
												resValidacao = false;
											break;
			case VAL_CAMPO_INTEIRO: if (campo.value!='' && !isInteger(campo.value))
										resValidacao = false;
									break;
			case VAL_CAMPO_INTEIRO_OBRIGATORIO: if (campo.value == '' || !isInteger(campo.value))
													resValidacao = false;
												break;
			case VAL_CAMPO_PONTO_FLUTUANTE: if (trim(campo.value) != '' && isNaN(trim(campo.value.replace(/\./g, '').replace(/,/, '.'))))
												resValidacao = false;
											break;
			case VAL_CAMPO_PONTO_FLUTUANTE_OBRIGATORIO: if (trim(campo.value)=='' || isNaN(trim(campo.value.replace(/\./g, '').replace(/,/, '.'))))
															resValidacao = false;
														break;
			case VAL_CAMPO_DATA: if (trim(campo.value) != '' && !validaData(trim(campo.value)))
									resValidacao = false;
								 break;
			case VAL_CAMPO_DATA_OBRIGATORIO: 
				if (trim(campo.value) == '' || !validaData(trim(campo.value)))
					resValidacao = false;
	 			break;
			case VAL_CAMPO_HORARIO: if (trim(campo.value) != '' && !validaHorario(trim(campo.value)))
										resValidacao = false;
								 	break;
			case VAL_CAMPO_HORARIO_OBRIGATORIO: if (trim(campo.value) == '' || !validaHorario(trim(campo.value)))
													resValidacao = false;
								 				 break;
			case VAL_CAMPO_VALOR_INVALIDO: if (valorInvalido!=null && trim(campo.value) == valorInvalido)
													resValidacao = false;
								 				 break;
			case VAL_CAMPO_MAIOR_QUE:
				if (!validaFloat(campo.value) || parseFloat(campo.value.replace('.', '').replace(',', '.')) <= valorInvalido)
					resValidacao = false;
				break;
			case VAL_CAMPO_MENOR_QUE: if (!validaFloat(campo.value) || parseFloat(campo.value.replace('.', '').replace(',', '.')) >= valorInvalido)
													resValidacao = false;
								 				 break;
		}
		if (!resValidacao) {
			if (showMessagem)
				showMsgbox('SICOE', 250, 50, mensagem, function() { 
						if (functionBeforeFocus!=null) 
							functionBeforeFocus.call(this); 
						setTimeout(function() {campo.focus();}, 300);});
		}
	}
	return resValidacao;
}

function trim(str) {
	while (str.charAt(0) == " ")
		str = str.substr(1,str.length -1);
	while (str.charAt(str.length-1) == " ")
		str = str.substr(0,str.length-1);
	return str;
} 

function validaFloat(s){
	s = new String(s);
	s = s.replace('.','').replace(',','.');
	return trim(s)!='' && (!isNaN(trim(s)));
}

function validaInteger(s){
	var i;
	if (s == '')
		return false;
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1)
			returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}

function isInteger(s){
	var i;
	if (s == '')
		return false;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31
		if (i==4 || i==6 || i==9 || i==11)
			this[i] = 30;
		if (i==2)
			this[i] = 29;
   } 
   return this;
}

function validaData(s){
	var dtStr = '';
	if (trim(s).indexOf(' ') != -1) {
		dtStr = trim(s).substring(0, trim(s).indexOf(' '));
		if (!validaHorario(trim(trim(s).substring(trim(s).indexOf(' ')))))
			return false;
	}
	else
		dtStr = trim(s);
	var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strDay=dtStr.substring(0,pos1)
	var strMonth=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) 
		strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1)
		strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++)
		if (strYr.charAt(0)=="0" && strYr.length>1)
			strYr=strYr.substring(1);
	month=parseInt(strMonth, 10)
	day=parseInt(strDay, 10)
	year=parseInt(strYr, 10)
	if (pos1==-1 || pos2==-1)
		return false;
	if (strMonth.length<1 || month<1 || month>12)
		return false;
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month])
		return false;
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear)
		return false;
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false)
		return false;
    return true;
}

function validaHorario(s) {
	if (s == '')
		return false;
	var parts = s.split(':');
	if (parts.length <= 1 || parts.length>3)
		return false;
	else if (!isInteger(parts[0]))
		return false;
	else if (parts.length==3) {
		if (!isInteger(parts[1]))
			return false;
		else if (!isInteger(parts[2]))	
			return false;
		else if ((parseInt(parts[0], 10) < 0) || (parseInt(parts[0], 10) > 23))
			return false;
		else if ((parseInt(parts[1], 10) < 0) || (parseInt(parts[1], 10) > 59))
			return false;
		else if ((parseInt(parts[2], 10) < 0) || (parseInt(parts[2], 10) > 59))
			return false;
		else
			return true;
	}
	else if (!isInteger(parts[1]))
		return false;
	else if ((parseInt(parts[0], 10) < 0) || (parseInt(parts[0], 10) > 23))
		return false;
	else if ((parseInt(parts[1], 10) < 0) || (parseInt(parts[1], 10) > 59))
		return false;
	else
		return true;
}

/* Classe HashMap */
function HashMap(){
	this.register=new Array();
}

function HashMap_put(key, value){
	this.register.push(new Array(key,value));
}

HashMap.prototype.put = HashMap_put;

function HashMap_getValue(key){
	var i=0;
	for(i=0; i<this.register.length; i++){
		if(this.register[i][0]==key){
			return this.register[i][1];
		}
	}
	return null;
}

HashMap.prototype.getValue = HashMap_getValue;

function HashMap_toString(register){
	str='';
	var i=0;
	for(i=0; i<this.register.length; i++){
		str+='['+this.register[i][0]+']='+this.register[i][1]+', ';
	}
	return str;
}

HashMap.prototype.toString = HashMap_toString;
/* fim classe HashMap*/
