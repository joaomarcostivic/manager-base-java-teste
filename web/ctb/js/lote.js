function getDataAtual(content){
	if (content==null) {
		getPage("GET", "getDataAtual", "../methodcaller?className=com.tivic.manager.util.Util&method=getDataAtual()");
	}
	else {
		$('dtAbertura').value = content.substring(1, 11);
	}
}
