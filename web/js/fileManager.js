document.onpaste = function(event) {
	document.body.innerHTML = "";
	var height = window.screen.availHeight
	var width  = window.screen.availWidth
	var base64;
	var items = (event.clipboardData || event.originalEvent.clipboardData).items;
	var blob = items[0].getAsFile();
	var reader = new FileReader();
	reader.onload = function(event) {
		base64 = event.target.result;
		
		var oImg = document.createElement("img");
		oImg.setAttribute('src', base64);
		oImg.setAttribute('id', 'colagem');
		
		if(oImg.width > (width/2)){
			oImg.setAttribute('width', width/2+'px');
		}
		
		document.body.appendChild(oImg);
	}; // data url!
	
	reader.readAsDataURL(blob);
}


//function getColagemSrc(idDocument){	
//	for(i=0; i<document.getElementsByTagName("div").length;i++){
//		var iframe = document.getElementById("iframe_" + idDocument + i);
//		var innerDoc = iframe.contentDocument || iframe.contentWindow.document;
//		var img = innerDoc.getElementById("colagem");
//		var base64 = img.src;
//		return base64;
//	}
//}

function getColagemSrc(idDocument){	
	var elements = document.getElementsByTagName("div");
    for(i=0; i<elements.length;i++){
		var iframe = document.getElementById("iframe_" + idDocument + i);
		if(iframe){
			var innerDoc = iframe.contentDocument || iframe.contentWindow.document;
			var img = innerDoc.getElementById("colagem");
			var base64 = img.src;
		}
	 }
    
	return base64;
}
