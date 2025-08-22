// JavaScript Document

var METHODCALLER_PATH = '../methodcaller';

/* ATENÇÃO! NÃO UTILIZAR OU MODIFICAR AINDA O BLOCO A SEGUIR (AjaxOne)
 * Esboço do objeto que substituirá o getPage
 * vai ter controle de conexao, permitindo:
 * 	  - enviar várias requisicoes numa mesma conexao
 *    - manter uma conexao aberta, aguardando novas requisicoes
 *    - analise do controle de trafego
 *    - transacoes sincronas (uma transacao com várias funcoes JS)
 * vai permitir callback direto à funcao que chamou a requisicao (sem eval)
 * outras melhorias no tratamento de metodos de requisicao (GET/POST)
 * transmissao (up/down) de arquivos via ajax (bytes) e remontagem via js (ex: imagens, pdf)
 * 
 * POR ENQUANTO FAZ SOMENTE TRADUÇÃO PARA O METODO getPage
 * 
 * @author Sapucaia 
 * @since Ago/2009
 * */
var AjaxOne = {
	GET: "GET",
	POST: "POST",
	METHODCALLER_PATH: '../methodcaller',
	request: function(method, url, options){
		if(!method)
			throw "Ao fazer uma requisicao é necessario indicar o parametro 'method'!\nValores: [AjaxOne.GET|AjaxOne.POST]";
		
		if(!url)
			throw "Ao fazer uma requisicao é necessario indicar o parametro 'url'!";
		if(options.params && options.params instanceof Array){
			options.fields = (options.fields)?options.fields:[];
			for(var i=0; i<options.params.length; i++){
				var f = document.createElement("input");
					f.setAttribute("type", "hidden");
					f.setAttribute("value", options.params[i].value);
					f.setAttribute("id", options.params[i].id);
					f.setAttribute("name", options.params[i].id);
				options.fields.push(f);
			}
		}
		getPage(method,
				options.returnFunction, //deve entrar em desuso nas proximas versoes, substituindo por callback, referencia direta à funcao (sem eval)
				url,
				options.fields,
				options.loading,
				options.aditionalParameters, //deve entrar em desuso nas proximas versoes, callback direto
				options.txtLog,
				options); 
	}
};
/*FIM AjaxOne*/

function getPage(method, returnFunction, url, form, displayLoading, aditionalParameters, descriptionExecutionMethod, options) {
	var asynchronous = options==null || options.asynchronous==null ? true : options.asynchronous;
    var req;
	// Mozilla/Safari
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
    }
    // IE
    else if (window.ActiveXObject) {
		try { 
			req = new ActiveXObject("Msxml2.XMLHTTP"); 
		} 
		catch (e1) { 
			try { 
				req = new ActiveXObject("Microsoft.XMLHTTP"); 
			}
			catch (e2) { 
				req = null; 
			} 
		} 
    }
	if (method=='GET' && descriptionExecutionMethod) {
		url += "&executionDescription=" + urlEncode(descriptionExecutionMethod);
	}
	
	if(url.indexOf('METHODCALLER_PATH')!=-1)
		url=url.replace(/^METHODCALLER_PATH/, METHODCALLER_PATH);
		
	if(url.indexOf('?')>=0)
		url = url + '&noproxy='+(new Date().getTime())
	else
		url = url + '?noproxy='+(new Date().getTime());
	var urlParts;
	if(method=='POST')	{
		req.open(method, url, asynchronous);
    	req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
	}
	else if(method=='GET'){
		try {
			req.open(method, url, asynchronous);
			req.setRequestHeader('Content-Type', 'text/html; charset=UTF-8');
		}
		catch(e) {}
	}
	else if(method=='GET2POST'){
		urlParts = url.split('?');
		req.open("POST", urlParts[0], asynchronous);
		req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
	}
    
    req.onreadystatechange = function() {
		switch(req.readyState){
			case 0:
				break;
			case 1:	
				if(displayLoading)	{
					var displayLoadingOptions = options!=null && options.displayLoading!=null ? options.displayLoading : null;
					createTempbox('msgbox',{width: displayLoadingOptions!=null && displayLoadingOptions.width!=null ? displayLoadingOptions.width : 130, 
										height: 45, 
										message: displayLoadingOptions!=null && displayLoadingOptions.message!=null ? displayLoadingOptions.message : 'Processando...',
										boxType: 'LOADING',
										time: 0, 
										modal: displayLoadingOptions!=null && displayLoadingOptions.modal!=null ? true : null});
				}
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				if(displayLoading)
					closeWindow('msgbox');
				var html = req.responseText;
				if(returnFunction!=null) {
					eval(returnFunction+'(html, aditionalParameters)');
				}
				
				//transicao para novo AjaxOne
				if(options && options.callback && asynchronous){
					var args = [html];
					if(options.callbackArgs){
						if(options.callbackArgs instanceof Array){
							for(var i=0; options.callbackArgs && i<options.callbackArgs.length; i++){
								args.push(options.callbackArgs[i]);
							}
						}
						else{
							args.push(options.callbackArgs);
						}
					}
					options.callback.apply(this, args);
				}					 
				break;
		}
    }
	
	if(method=='POST'){
		var pairs = form==null ? null : transForm(form);
		if (descriptionExecutionMethod != null) {
			if (pairs==null)
				pairs = "";
			if (pairs != "")
				pairs += "&";
			pairs += "executionDescription=" + encodeURIComponent(descriptionExecutionMethod);
		}
		req.send(pairs);
	}
	else if(method=='GET')
		req.send(null);
	else if(method=='GET2POST'){
		var query = (form==null) ? "" : transForm(form);
		var queryParams ='';
		for(var q=1; q<urlParts.length; q++)
			queryParams+=((q>1)?'?':'')+urlParts[q];
		
		if(queryParams!=''){
			var params = queryParams.split('&');
			for(var i=0; i<params.length; i++){
				var pair = 	params[i].split('=');
				var value = '';
				for(var j=1; j<pair.length; j++)
					value+=((j>1)?'=':'')+pair[j];
					
				query+=((query=='')?'':'&')+pair[0]+'='+encodeURIComponent(value);
			}
		}
		req.send((query!='')?query:null);
	}
	
	if(!asynchronous){
       	if(displayLoading)
			closeWindow('msgbox');
	    var html = req.responseText;
		if (options && options.callback && options.callback.object && options.callback.method) {
			options.callback.object[options.callback.method](html, aditionalParameters);
		}
		else if(returnFunction!=null) {
			eval(returnFunction+'(html, aditionalParameters)');
		}	
    }
}

function transForm(form){
	var elements = [];
	if(form instanceof Array){
		for(var i=0; i<form.length; i++) {
			if (form[i]!=null)
				elements.push(form[i]);
		}
	}
	else
		elements = form.elements;
		
    var pairs = new Array();

    for (var i = 0; i < elements.length; i++) {
		try {
			if ((elements[i].type=='checkbox' || elements[i].type=='radio') && !elements[i].checked)
				continue;
			var name = elements[i].name;
			var value = elements[i].value;
			if (elements[i].getAttribute && elements[i].getAttribute("mask")!=null) {
				var datatype = elements[i].getAttribute("datatype")==null ? "" : elements[i].getAttribute("datatype").toUpperCase();
				if (datatype!='DATE' && datatype!='DATETIME')
					value = value.replace(/\./g, '').replace(/,/, '.').replace(/\//, '').replace(/-/, '').replace(/\//, '').replace(/\(/, '').replace(/\)/, '').replace(/\s/, '');
			}
			if (name!=null && value!=null){
				if(elements[i].getAttribute && elements[i].getAttribute("lguppercase")=="true")
					value = value.toUpperCase();
				else if(elements[i].getAttribute && elements[i].getAttribute("lglowercase")=="true")
					value = value.toLowerCase();
				pairs.push(name + "=" + encodeURIComponent(value));
			}
		}
		catch(e) {
		}
    }
    return pairs.join("&");
}

function urlEncode(str) { 
	var hex_chars = "0123456789ABCDEF"; 
	var noEncode = /^([a-zA-Z0-9\_\-\.])$/; 
	var n, strCode, hex1, hex2, strEncode = ""; 

	for(n = 0; n < str.length; n++) { 
		if (noEncode.test(str.charAt(n))) { 
			strEncode += str.charAt(n); 
		} else { 
			strCode = str.charCodeAt(n); 
			hex1 = hex_chars.charAt(Math.floor(strCode / 16)); 
			hex2 = hex_chars.charAt(strCode % 16); 
			strEncode += "%" + (hex1 + hex2); 
		} 
	} 
	return strEncode; 
} 

// url_decode version 1.0 
function urlDecode(str) { 
	var n, strCode, strDecode = ""; 

	for (n = 0; n < str.length; n++) { 
		if (str.charAt(n) == "%") { 
			strCode = str.charAt(n + 1) + str.charAt(n + 2); 
			strDecode += String.fromCharCode(parseInt(strCode, 16)); 
			n += 2; 
		} else { 
			strDecode += str.charAt(n); 
		} 
	} 

	return strDecode; 
}  