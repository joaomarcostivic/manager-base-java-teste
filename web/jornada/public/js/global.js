var nmCidadeWS = null;

var carregarCidades = function(element){
	$("#enderecamento input[type='text']").attr('disabled', true);
	$("#cidades").empty();
	$("#cidades").append($('<option>').text("Carregando...").attr('value', -1));
	$.get('/edf/request/jornada/?cdEstado='+element.value, function(response){
		var response = JSON.parse(response);
		$("#cidades").empty();
		$.each(response, function(index, value){
			$("#cidades").append($('<option>').text(value.nmCidade).attr('value', value.cdCidade));
		});
		
		if(nmCidadeWS != null) {
			var nmCidade = latinize(nmCidadeWS);
			$("#cidades option").filter(function() { return this.text == nmCidade.toUpperCase() }).attr('selected', true);
		}
		$("#enderecamento input[type='text']").attr('disabled', false);
		$('.register form').valid();
	});
}

var openAcordoModal = function () {
	$("#acordoInscricaoModal").modal('show');
	$("#acordoInscricaoModal").remove(".error");
	$("#acordoInscricaoModal .close").on('click', function (e) {
		$("#termos").prop("checked", false);
	});
	return false;
}

var emitirComprovante = function () {
	$("#comprovanteInscricaoModal").modal('show');
	return false;
}


var showMessage = function (element, message, type) {
	var icon;
	var title;
	
	if(type === "success"){
		icon = "fa fa-check";
		title = "Tudo certo!"; 
	} else if(type === "info"){
		icon = "fa fa-info-circle";
		title = "Atenção!"
	} else if(type === "warning"){
		icon = "fa fa-times-circle";
		title = "Aviso!"
	} else if(type === "danger"){
		icon = "fa fa-times-circle";
		title = "Aviso!"
	}
	
    var cbMessageText = "<div class=\"alert alert-"+type+" alert-white rounded\">" +
                        "	<button type=\"button\" data-dismiss=\"alert\" aria-hidden=\"true\" class=\"close\">×</button>" +
                        "   <div class=\"icon\">" +
                        "   	<i class=\""+icon+"\"></i>" +
                        "	</div>" +
                        "	<strong>"+title+"</strong>" +
                        "	 " + message +
                        "</div>";
    $(element).append(cbMessageText);        
}
    
$(function () {
	
	$(":input").inputmask();
    
    $("input[name='cep']").focusout(function(){
    	var $cep = $(this).val();
    	$.get('http://api.postmon.com.br/v1/cep/'+$cep, function(response){
    		window.nmCidadeWS = response.cidade;
			$("input[name='logradouro']").val(response.logradouro);
    		$("input[name='bairro']").val(response.bairro);
			$("#estados option").filter(function() { return this.text == response.estado.toUpperCase() }).attr('selected', true);
			$("#estados").trigger('change');
    	});
    });
    
    $("#acordoInscricaoModal .btn-success").on('click', function(){
		$("#termos").prop("checked", true);
		$("#acordoInscricaoModal").modal('hide');
	});
    
    $("#acordoInscricaoModal .btn-primary").on('click', function(){
		$("#termos").prop("checked", false);
		$("#acordoInscricaoModal").modal('hide');
	});
    
    $("input[name='lgMatricula']").click(function(){
    	if($(this).is(':checked')){
    		$("#matricula").removeClass('hidden');
    		$(".convidado").addClass('hidden');
    		$("input[name='matricula']").rules("add", {  // <- apply rule to new field
    	        required: true
    	    });
    		$("select[name='cargo_publico']").rules("add", {  // <- apply rule to new field
    			required: true
    		});
    	} else {
    		$("#matricula").addClass('hidden');
    		$(".convidado").removeClass('hidden');
    		$("select[name='cargo_publico'] option").eq(0).prop('selected', true);
    		$("input[name='matricula']").val("");
//    		$("input[name='matricula']").rules("add", {  // <- apply rule to new field
//    	        required: false
//    	    });
//    		$("select[name='cargo_publico']").rules("add", {  // <- apply rule to new field
//    			required: false
//    		});
    	}
    	if ($('.register form').valid()) {                   // checks form for validity
            $('.btn-enviar').prop('disabled', false).removeClass("disabled");        // enables button
        } else {
            $('.btn-enviar').prop('disabled', true).addClass("disabled");;   // disables button
        }
    });
    
    $("#primeiros-passos").validate({
    	messages: {
    		nome: "Informe seu nome completo.",
    		sexo: "Informe seu sexo.",
    		email: "Informe seu e-mail.",
    		dtnascimento: "Informe sua data de nascimento!",
    		termos: "Certifique-se de que tenha lido o edital da Jornada Pedagógica 2016."
    	},
    	errorPlacement: function(error, element) {
            if (element.is(':checkbox')){
            	error.insertBefore($(".register .btn-success"));
            } else {
            	error.insertAfter(element);
            }
    	},
    	submitHandler: function(form) {
    		$.post('../request/jornada/?a=passo1', $(form).serialize(), function(data){
    			var data = JSON.parse(data);
    			if(data.code == 1 && data.dispatcher) {
    				location.href = data.dispatcher;
    			} else {
    				return false;
    			}            	
    		});
    	}
    });
    
    $("#equipe").validate({
    	messages: {
    		nome: "Informe seu nome completo.",
    		sexo: "Informe seu sexo.",
    		rg: "Informe seu número de RG.",
    		cpf: {
    			required: "Informe o número do seu CPF.",
    			cpf: "O CPF informado é inválido."
    		},
    		numero: "Informe o número do local.",
    		email: "Informe seu e-mail.",
    		dtnascimento: "Informe sua data de nascimento!",
    		logradouro: "Informe o nome completo do seu endereço.",
    		bairro: "Informe nome do seu bairro.",
    		cidade: "Informe a cidade onde reside.",
    		estado: "Infome o estado onde reside.",
    	},
    	errorPlacement: function(error, element) {
            if (element.is(':checkbox')){
            	error.insertBefore($(".register .btn-success"));
            } else {
            	error.insertAfter(element);
            }
    	},
    	submitHandler: function(form) {
    		form.submit();
    	}
    });
    
    $('.register input').on('blur keyup click', function () { // fires on every keyup & blur
        if ($('.register form').valid()) {                   // checks form for validity
            $('.btn-enviar').prop('disabled', false).removeClass("disabled");        // enables button
        } else {
            $('.btn-enviar').prop('disabled', true).addClass("disabled");;   // disables button
        }
    });
    
    $("#segundo-passo").validate({
    	rules: {
    		cpf: {
    			cpf: true,
    			required: true
    		}
    	},
    	messages: {
    		rg: "Informe seu número de RG.",
    		cpf: {
    			required: "Informe o número do seu CPF.",
    			cpf: "O CPF informado é inválido."
    		},
    		matricula: "Informe seu número de matrícula da rede pública.",
    		numero: "Informe o número do local.",
    		logradouro: "Informe o nome completo do seu endereço.",
    		bairro: "Informe nome do seu bairro.",
    		cidade: "Informe a cidade onde reside.",
    		estado: "Infome o estado onde reside.",
    		cursos: "Selecione o curso no qual deseja participar.",
    		cargo_publico: "Selecione seu cargo na rede municipal.",
    		evento_quinta_tarde: "Você deve selecionar um evento para este turno.",
    		evento_sexta_manha: "Você deve selecionar um evento para este turno.",
    		evento_sexta_tarde: "Você deve selecionar um evento para este turno.",
    		evento_sabado_manha: "Você deve selecionar um evento para este turno.",
    		evento_sabado_tarde: "Você deve selecionar um evento para este turno."
    	},
    	errorPlacement: function(error, element) {
    		var el = {};
			el.type = element.prop('type');
			if(el.type === "radio"){
				error.insertBefore(element.parent().parent());
			} else {
				error.insertAfter(element);
			}
		},
    	submitHandler: function(form) {
    		$(".return").html("");
    		if($("input[name='lgMatricula']").length > 0 && $("input[name='lgMatricula']").is(':checked') === false){
    			goToByScroll($(".convidado"));
    			return false;
    		}
    		$.post('../../request/jornada/?a=passo2', $(form).serialize(), function(data){
    			var data = JSON.parse(data);
    			if(data.code && parseInt(data.code) < 1) {
    				showMessage($(".return"), 	unescape(data.message), "danger");
    				goToByScroll($(".return"));
    			} else {
    				$(".register").html("");
    				showMessage($(".register"), "Sua inscrição foi efetuada com sucesso! <small>Imprima seu comprovante ou salve como PDF.</small>", "success");
    				$('<iframe>', { src:'../confirmacao.jsp' , id:  'myFrame', frameborder: 0, scrolling: 'no', width: '100%', height: '842px'}).appendTo(".register");
    				goToByScroll($(".header"));
    			}
    		});
    	}
    });
        
    $(".register input[name='nome']").not("input[name='cep'], input[name='cpf']").keyup(function() {
    	$(this).capitalize();
    });
    
    $(".register input, .register select").focusin(function(){
    	$(".main-overlay-1").stop().animate({
    		opacity: 0.9
    	});
    }).hover(function(){
    	$(".main-overlay-1").stop().animate({
    		opacity: 0.9
    	});
    }, function(){
    	var lg = $("form").is(":hover");
    	if(!lg){
    		$(".main-overlay-1").stop().animate({
        		opacity: 0.7
        	});
    	}
    }).focusout(function(){
    	var lg = $("form").is(":hover");
    	if(!lg){
    		$(".main-overlay-1").stop().animate({
        		opacity: 0.7
        	});
    	}
	});
});

//add a function to jQuery so we can call it on our jQuery collections
$.fn.capitalize = function () {

    //iterate through each of the elements passed in, `$.each()` is faster than `.each()
    $.each(this, function () {

        //split the value of this input by the spaces
        var split = this.value.split(' ');

        //iterate through each of the "words" and capitalize them
        for (var i = 0, len = split.length; i < len; i++) {
            split[i] = split[i].charAt(0).toUpperCase() + split[i].slice(1);
        }

        //re-join the string and set the value of the element
        this.value = split.join(' ');
    });
    return this;
};

$.validator.addMethod("cpf", function(value, element) {
	   value = jQuery.trim(value);

	    value = value.replace(/[^0-9.]/g, "");
	    cpf = value;
	    console.log(cpf);
	    while(cpf.length < 11) cpf = "0"+ cpf;
	    var expReg = /^0+$|^1+$|^2+$|^3+$|^4+$|^5+$|^6+$|^7+$|^8+$|^9+$/;
	    var a = [];
	    var b = new Number;
	    var c = 11;
	    for (i=0; i<11; i++){
	        a[i] = cpf.charAt(i);
	        if (i < 9) b += (a[i] * --c);
	    }
	    if ((x = b % 11) < 2) { a[9] = 0 } else { a[9] = 11-x }
	    b = 0;
	    c = 11;
	    for (y=0; y<10; y++) b += (a[y] * c--);
	    if ((x = b % 11) < 2) { a[10] = 0; } else { a[10] = 11-x; }

	    var retorno = true;
	    if ((cpf.charAt(9) != a[9]) || (cpf.charAt(10) != a[10]) || cpf.match(expReg)) retorno = false;

	    return this.optional(element) || retorno;

	}, "Informe um CPF válido");