function validarChave(ajax, noclose){
	if(ajax === false){
		$.Dialog.close();
		$.Dialog({
	        shadow: true,
	        overlay: true,
			overlayClickClose : false,
	        icon: '<span class="icon-key"></span>',
	        title: 'Inserir código de ativação',
	        width: 400,
	        height: 160,
	        padding: 10,
	        closeOnEscape: false,
			onShow: function(_dialog){
				if(noclose){
					_dialog.find(".btn-close").hide();
				}
	        },
	        beforeClose: function(){
	            return false;
	        },
	        content: " <label>Insira sua chave: </label> " +
	        	     " <div class=\"input-control text\" data-role=\"input-control\"> " +
	                 " <input placeholder=\"Ex: BCDHAAFEJABEHBFGBEBHCDAAAAAAADABBJBEJJBIBAJEC\" name=\"txtChave\" type=\"text\" style=\"font-size: 12px;\"> " +
	                 " <button type=\"button\" class=\"btn-clear\" tabindex=\"-1\"></button> " +
	                 " </div> " +
                     " <button class=\"info\" style=\"float: right\" OnClick=\"validarChave(true);\">Validar</button></p> "
	    });
	} else {
		if ($("input[name='txtChave']").val() == ""){
			$.Notify({style: {background: 'orange', color: 'white'}, content: "Insira uma chave de licença.", caption: "Houve um problema..."});
			return false;
		}

		if ($("input[name='txtChave']").val().length < 44){
			$.Notify({style: {background: 'orange', color: 'white'}, content: "Uma chave deve conter 45 caractéres.", caption: "Houve um problema..."});
			return false;
		}
		
		if (/^[a-zA-Z()]+$/.test($("input[name='txtChave']"))){
			$.Notify({style: {background: 'orange', color: 'white'}, content: "Uma chave deve conter apenas letras.", caption: "Houve um problema..."});
			return false;
		}
		
		$.Notify({style: {background: 'blue', color: 'white'}, content: "Aguarde enquanto a chave é verificada e validada.", caption: "Validando..."});
		
		var construct = "const " + $("input[name='txtChave']").val() + ":String";
		
		$.ajax({
			url: 'methodcaller?className=com.tivic.manager.seg.ChaveLicencaServices&method=validarChave(' + encodeURIComponent(construct) + ')',
			type: "POST",
			dataType: "html"
		}).done(function(response){
			var jsonResult = eval('(' + response + ')');
			if(jsonResult.code < 0){
				$.Notify({style: {background: 'red', color: 'white'}, content: jsonResult.message, caption: "Falha na ativação!"});
			} else {
				$.Notify({style: {background: 'green', color: 'white'}, content: jsonResult.message, caption: "Ativação executada com sucesso!"});
				setTimeout(function(){
					location.reload();
				}, 1000);
			}
		});
	}
}