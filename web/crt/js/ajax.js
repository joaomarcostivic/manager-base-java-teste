// JavaScript Document
function getPage(method, returnFunction, url, form, displayLoading, displayLoadingMsg) {
    var req;
	// Mozilla/Safari
    if (window.XMLHttpRequest) {
        req = new XMLHttpRequest();
    }
    // IE
    else if (window.ActiveXObject) {
        req = new ActiveXObject("Microsoft.XMLHTTP");
    }
    req.open(method, url, true);
    if(method=='POST')
		req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
	else
		req.setRequestHeader('Content-Type', 'text/html; charset=UTF-8');
		
	displayLoading = (displayLoading==null)?true:displayLoading;
	displayLoadingMsg = (displayLoadingMsg==null)?'Aguarde...':displayLoadingMsg;
    req.onreadystatechange = function() {
        
		switch(req.readyState){
			case 0:
				break;
			case 1:	
				if(displayLoading)	{
					try	{
						createTempbox('msgbox',{width: 130, 
												height: 45, 
												message: 'Processando...',
												tempboxType: 'LOADING',
												time: 0,
												noTitle: true});
					}
					catch(e)	{
						var img = document.createElement("IMG");
						img.src = "/sol/imagens/loading.gif";
						
						var divJanela = document.createElement("div");
						divJanela.id = 'msgbox';
						divJanela.style.position = "absolute";
						divJanela.style.width = img.width;
						divJanela.style.height = img.height;
						divJanela.style.left = document.body.clientWidth-img.width;
						divJanela.style.top = document.body.clientHeight-img.height;
						var eBody = document.getElementsByTagName('body').item(0);
						
						divJanela.appendChild(img);
						eBody.appendChild(divJanela);
					}
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
				if(returnFunction!=null)
					eval(returnFunction+'(html)');
				break;
		}
    }
	
	if(method=='POST')
    	req.send(transForm(form));
	else
		req.send(null);
}

function transForm(form){
    var elements = form.elements;
    var pairs = new Array();

    for (var i = 0; i < elements.length; i++) {
        if (((name = elements[i].name) && (value = elements[i].value)))
            pairs.push(name + "=" + encodeURIComponent(value));
    }
   	return pairs.join("&");
}