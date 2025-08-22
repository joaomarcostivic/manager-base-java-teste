function miNfeInutilizacaoOnClick(options){
	var origem = options==null ? false : options.origem;
	var cdEmpresa = options.cdEmpresa;
	FormFactory.createFormWindow('jInutilizacao', 
            {caption: "Inutilização", width: 400, height: 80, unitSize: '%', modal: true,
			  id: 'inutilizacao', loadForm: true, noDrag: true, cssVersion: '2',
			  hiddenFields: [{id:'cdEmpresa', reference: 'cd_empresa', value: cdEmpresa}],
			  lines: [
//			           {id:'nrInicio', type:'text', label:'De', width:50},
//			           {id:'nrFim', type:'text', label:'Até', width:50}
//			  		  ],
//			  		  [{id:'txtJustificativa', type:'textarea', label:'Justificativa', width:100, height:90}
//			  		   ],
				  	  [
//				  	   {type: 'space', width:30},
				  	   {id:'btnEnviar', type:'button', label:'Enviar', width: 50, height:19, image: '/sol/imagens/form-btSalvar16.gif', onClick: function(){miInutilizarNfeOnClick();}},
					   {id:'btnCancelar', type:'button', label:'Retornar', width: 50, height:19, image: '/sol/imagens/form-btCancelar16.gif',onClick: function(){	closeWindow('jInutilizacao')}}
				  	  ]
			  		]
            });
}

function miInutilizarNfeOnClick(content){
	if (content==null) {
		getPage("GET", "miInutilizarNfeOnClick", "../methodcaller?className=com.tivic.manager.fsc.NfeServices&method=inutilizacaoDeNumeracaoNFe(const " + $('cdEmpresa').value + ":int)");
	}
	else {
		alert(content)
	}
}

function miNfeViewOnClick(options)	{
	var origem = options==null ? false : options.origem;
	createWindow('jNfeView', {caption: 'Nota Fiscal Eletrônica', width: 1000, height: 460, contentUrl: (origem ? '' : '../' ) + 'fsc/nfe_view.jsp?cdEmpresa=' + $('cdEmpresa').value + '&cdUsuario=' + $('cdUsuario').value + '&cdLocalArmazenamento=' + $('cdLocalArmazenamento').value});
}

function miSpedOnClick(options)	{
	var origem = options==null ? false : options.origem;
	createWindow('jSped', {caption: 'SPED', width: 700, height: 400, contentUrl: (origem ? '' : '../' ) + 'fsc/sped.jsp?cdEmpresa=' + $('cdEmpresa').value});
}


