var carregarCidades = function(element){
	
	$("select[name='cidades']").empty();
	$("select[name='cidades']").append($('<option disabled selected>').text("Carregando...").attr('value', -1));
	
	$.get('../request/selecao/?cdEstado='+element.value, function(response){
		var response = JSON.parse(response);
		$("select[name='cidades']").empty();
		$.each(response, function(index, value){
			$("select[name='cidades']").append($('<option>').text(value.nmCidade).attr('value', value.cdCidade));
		});
		
		if(nmCidadeWS != null) {
			var nmCidade = latinize(nmCidadeWS);
			$("select[name='cidades'] option").filter(function() { return this.text == nmCidade.toUpperCase() }).attr('selected', true);
		}
	});
	
}