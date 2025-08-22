var jPlanoContas;
var contaDebito = 0;
var contaCredito = 1;
var contaRateio = 2;
var stAtivo = 1;

function miPlanoContasOnClick(noDestroyWindow, onCloseFunction) {
	jPlanoContas = createWindow('jPlanoContas', {caption: 'Manuten��o de plano de contas', 
								  width: 708, 
								  height: 462, 
								  top: 48,
								  noDestroyWindow: noDestroyWindow, 
								  onClose: onCloseFunction, 
	                              contentUrl: '../ctb/plano_contas.jsp?cdEmpresa=' + getValue('cdEmpresa')});
	if(getFrameContentById('jPlanoContas') != null && getFrameContentById('jPlanoContas').getAllPlanoContas){
		getFrameContentById('jPlanoContas').getAllPlanoContas(null);
	}
}

function miCentroCustoOnClick(noDestroyWindow, onCloseFunction) {
	jCentroCusto = createWindow('jCentroCusto', {caption: 'Manuten��o de centros de custos', 
								  width: 708, 
								  height: 313, 
								  top: 98,
								  noDestroyWindow: noDestroyWindow, 
								  onClose: onCloseFunction, 
	                              contentUrl: '../ctb/centro_custo.jsp?cdEmpresa=' + getValue('cdEmpresa')});
	if(getFrameContentById('jCentroCusto') != null && getFrameContentById('jCentroCusto').getAllCentroCusto){
		getFrameContentById('jCentroCusto').getAllCentroCusto(null);
	}
}

var jHistorico;
function miHistoricoOnClick() {
	jHistorico = FormFactory.createQuickForm('jHistorico', {caption: 'Hist�ricos', 
										  width: 480, 
										  height: 350, 
										  noDrag: true,
										  //quickForm
										  id: "ctb_historico",
										  classDAO: 'com.tivic.manager.ctb.HistoricoDAO',
										  keysFields: ['cd_historico'],
										  classMethodGetAll: 'com.tivic.manager.ctb.HistoricoDAO',
										  methodGetAll: 'getAll()',
										  hiddenFields: [{reference: 'cd_empresa', value: $('cdEmpresa').value}],
										  unitSize: '%',
										  constructorFields: [/* hist�rico */
										  					  {reference: 'cd_historico', type: 'int'},
										  					  {reference: 'cd_empresa', type: 'int'},
															  {reference: 'nm_historico', type: 'java.lang.String'},
															  {reference: 'id_historico', type: 'java.lang.String'},
															  {reference: 'lg_complemento', type: 'int'}],
										  gridOptions: {columns: [{label:'Descri��o', reference: 'nm_historico'},
										  						  {label:'Complemento?', reference: 'DS_LG_COMPLEMENTO'},
																  {label:'ID', reference: 'id_historico'}],
														onProcessRegister: function(register){
																register['DS_LG_COMPLEMENTO'] = (register['LG_COMPLEMENTO'] == 1 ? 'Sim' : 'N�o');
														},
													    strippedLines: true,
														columnSeparator: true,
														lineSeparator: false},
										  lines: [[{reference: 'nm_historico', label:'Descri��o', width:60, maxLength:256},
										  		   {reference: 'lg_complemento', label:'Complemento', width:20, type: 'checkbox', value: 1, defaultValue:0},
										  		   {reference: 'id_historico', label:'ID', width:20, maxLength:20}]],
										  focusField:'field_nm_historico'});
}

function miLoteOnClick(noDestroyWindow, onCloseFunction) {
	jLote = createWindow('jLote', {caption: 'Manuten��o de lotes cont�beis', 
								   width: 790, 
								   height: 431, 
								   top: 80,
								   noDestroyWindow: noDestroyWindow, 
								   onClose: onCloseFunction, 
	                               contentUrl: '../ctb/lote.jsp?cdEmpresa=' + getValue('cdEmpresa')});
	if(getFrameContentById('jLote') != null && getFrameContentById('jLote').getAllLote){
		getFrameContentById('jLote').getAllLote(null);
	}
}

function miLancamentoOnClick(noDestroyWindow, onCloseFunction) {
	jLote = createWindow('jLancamento', {caption: 'Manuten��o de lan�amentos avulsos', 
								   width: 730, 
								   height: 401, 
								   top: 90,
								   noDestroyWindow: noDestroyWindow, 
								   onClose: onCloseFunction, 
	                               contentUrl: '../ctb/lancamento.jsp?cdEmpresa=' + getValue('cdEmpresa')});
	if(getFrameContentById('jLancamento') != null && getFrameContentById('jLancamento').getAllLancamento){
		getFrameContentById('jLancamento').getAllLancamento(null);
	}
}

var jLancamentoAuto;
function miLancamentoAutoOnClick(situacaoLancamentoAuto) {
	var cdEmpresa = $('cdEmpresa').value;
	jLancamentoAuto = FormFactory.createQuickForm('jLancamentoAuto', {caption: 'Lan�amentos autom�ticos', 
										  width: 580, 
										  height: 350, 
										  top: 90,
										  noDrag: true,
										  //quickForm
										  id: "ctb_lancamento_auto",
										  classDAO: 'com.tivic.manager.ctb.LancamentoAutoDAO',
										  keysFields: ['cd_lancamento_auto'],
										  classMethodGetAll: 'com.tivic.manager.ctb.LancamentoAutoServices',
										  methodGetAll: 'findCompleto()',
										  hiddenFields: [{reference:'cd_empresa', value:cdEmpresa, defaultValue:cdEmpresa},
										  				 {reference:'lg_complemento'}],
										  unitSize: '%',
										  constructorFields: [/* lan�amento autom�tico */
										  					  {reference: 'cd_lancamento_auto', type: 'int'},
										  					  {reference: 'cd_conta_debito', type: 'int'},
										  					  {reference: 'cd_conta_credito', type: 'int'},
										  					  {reference: 'cd_historico', type: 'int'},
										  					  {reference: 'cd_empresa', type: 'int'},
															  {reference: 'nm_lancamento_auto', type: 'java.lang.String'},
															  {reference: 'txt_historico', type: 'java.lang.String'},
															  {reference: 'st_lancamento_auto', type: 'int'},
															  {reference: 'id_lancamento_auto', type: 'java.lang.String'},
															  {reference: 'cd_centro_custo_debito', type: 'int'},
															  {reference: 'cd_centro_custo_credito', type: 'int'}],
										  gridOptions: {columns: [{label:'Descri��o', reference: 'nm_lancamento_auto'},
																  {label:'Conta d�bito', reference: 'nm_conta_debito'},
																  {label:'Conta cr�dito', reference: 'nm_conta_credito'},
																  {label:'Centro de custo - d�bito', reference: 'ds_centro_custo_debito'},
																  {label:'Centro de custo - cr�dito', reference: 'ds_centro_custo_credito'},
																  {label:'Hist�rico', reference: 'nm_historico'},
										  						  {label:'Situa��o', reference: 'DS_ST_LANCAMENTO_AUTO'}],
														onProcessRegister: function(register){
															register['DS_ST_LANCAMENTO_AUTO'] = situacaoLancamentoAuto[register['ST_LANCAMENTO_AUTO']];
														},
													    onSelect: function() {
															$('field_txt_historico').className = $('field_lg_complemento').value == 1 ? 'field' : 'disabledField';
															$('field_txt_historico').disabled = $('field_lg_complemento').value != 1;
													    },
													    strippedLines: true,
														columnSeparator: true,
														lineSeparator: false},
										  onBeforeSave: function() {
												$('field_cd_empresa').value = $('cdEmpresa').value;
										  },
										  onAfterEdit: function() {
												$('field_txt_historico').className = $('field_lg_complemento').value == 1 ? 'field' : 'disabledField';
												$('field_txt_historico').disabled = $('field_lg_complemento').value != 1;
										  },
										  lines: [[{reference:'nm_lancamento_auto', label:'Descri��o', width:60, maxLength:256},
										  		   {reference:'id_lancamento_auto', label:'ID', width:20, maxLength:20},
										  		   {reference:'st_lancamento_auto', label:'Situa��o', width:20, type:'select', options:[], noClear:true}],
										  		  [{reference:'cd_conta_debito', label:'Conta a d�bito', width:50, type:'lookup', viewReference:'nm_conta_debito', findAction: function() { btnFindContaOnClick(null, contaDebito); }},
										  		   {reference:'cd_conta_credito', label:'Conta a cr�dito', width:50, type:'lookup', viewReference:'nm_conta_credito', findAction: function() { btnFindContaOnClick(null, contaCredito); }}],
										  		  [{reference:'cd_centro_custo_debito', label:'Centro de custo - d�bito', width:50, type:'lookup', viewReference:'ds_centro_custo_debito', findAction: function() { btnFindCentroCustoOnClick(null, contaDebito); }},
										  		   {reference:'cd_centro_custo_credito', label:'Centro de custo - cr�dito', width:50, type:'lookup', viewReference:'ds_centro_custo_credito', findAction: function() { btnFindCentroCustoOnClick(null, contaCredito); }}],
										  		  [{reference:'cd_historico', label:'Hist�rico', width:100, type:'lookup', viewReference:'nm_historico', findAction: function() { btnFindHistoricoOnClick(); }, clearAction: function () { btnClearCdHistorico();}}],
												  [{reference:'txt_historico', label:'Complemento do hist�rico', width:100, height:30, type:'textarea', disabled: true}]],
										  focusField:'field_nm_lancamento_auto'});
	loadOptions($('field_st_lancamento_auto'), situacaoLancamentoAuto); 
	$('field_st_lancamento_auto').value = stAtivo;
}

function miEncerramentoExercicioOnClick(noDestroyWindow)	{
	createWindow('jEncerramentoExercicio', {caption: 'Encerramento de exerc�cio', 
											width: 700, 
											height: 410, 
											top: 90,
											noDestroyWindow: noDestroyWindow,  
	              							contentUrl: '../ctb/encerramento_exercicio.jsp?cdEmpresa=' + $('cdEmpresa').value});
}

function btnFindHistoricoOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar hist�rico", 
												    width: 450,
												    height: 350,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.ctb.HistoricoDAO",
												    method: "find",
												    allowFindAll: true,
												    filterFields: [[{label:"Nome", reference:"nm_historico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}]],
												    gridOptions: {columns: [{label:"Descri��o", reference:"NM_HISTORICO"},
												   	 					    {label:"Complemento?", reference:"DS_LG_COMPLEMENTO"}],
															      onProcessRegister: function(register){
															   	  	  register['DS_LG_COMPLEMENTO'] =  register['LG_COMPLEMENTO'] == 0 ? 'N�o' : 'Sim';
																  },
													 		      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    hiddenFields: [{reference: 'CD_EMPRESA', value: $('cdEmpresa').value, datatype:_VARCHAR, comparator:_EQUAL}],
												    callback: btnFindHistoricoOnClick, 
												    autoExecuteOnEnter: false
										           });
    }
    else {// retorno
		filterWindow.close();
		if ($('cdHistorico')) 
			$('cdHistorico').value = reg[0]['CD_HISTORICO'];
		if ($('cdHistoricoView')) 
			$('cdHistoricoView').value = reg[0]['NM_HISTORICO'];
		if ($('txtHistorico')) { 
			$('txtHistorico').className = reg[0]['LG_COMPLEMENTO'] == 1 ? 'field' : 'disabledField';
			$('txtHistorico').disabled = reg[0]['LG_COMPLEMENTO'] != 1;
			$('txtHistorico').value = reg[0]['LG_COMPLEMENTO'] == 1 ? $('txtHistorico').value : '';
		}
		
		if ($('field_cd_historico')) 
			$('field_cd_historico').value = reg[0]['CD_HISTORICO'];
		if ($('field_cd_historicoView')) 
			$('field_cd_historicoView').value = reg[0]['NM_HISTORICO'];
		if ($('field_txt_historico')) { 
			$('field_txt_historico').className = reg[0]['LG_COMPLEMENTO'] == 1 ? 'field' : 'disabledField';
			$('field_txt_historico').disabled = reg[0]['LG_COMPLEMENTO'] != 1;
			$('field_txt_historico').value = reg[0]['LG_COMPLEMENTO'] == 1 ? $('field_txt_historico').value : '';
		}
	}
}

function btnClearCdHistorico() {
	if ($('cdHistorico')) 
		$('cdHistorico').value = '';
	if ($('cdHistoricoView')) 
		$('cdHistoricoView').value = '';
	if ($('txtHistorico')) { 
		$('txtHistorico').className = 'disabledField';
		$('txtHistorico').disabled = true;
		$('txtHistorico').value = '';
	}
}

function btnFindContaOnClick(reg, tipoLookup) {
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar conta", 
												    width: 550,
												    height: 350,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.ctb.ContaPlanoContasServices",
												    method: "findCompleto",
												    allowFindAll: true,
												    filterFields: [[{label:"N� Completo", reference:"A.nr_conta", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
												    				{label:"N� Conta", reference:"A.id_conta", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
												    				{label:"N� Reduzido", reference:"F.nr_conta_reduzida", datatype:_VARCHAR, comparator:_EQUAL, width:15, charcase:'uppercase'},
												    				{label:"Descri��o", reference:"F.nm_conta", datatype:_VARCHAR, comparator:_LIKE_ANY, width:50}]],
												    gridOptions: {columns: [{label:"N� Completo", reference:"nr_conta"},
												    						{label:"N� Conta", reference:"id_conta"},
												   	 					    {label:"N� Reduzido", reference:"nr_conta_reduzida"},
												   	 					    {label:"Descri��o", reference:"nm_conta"},
												   	 					    {label:"Tipo", reference:"DS_TP_CONTA"},
												   	 					    {label:"Natureza", reference:"DS_TP_NATUREZA"}],
																  onProcessRegister: function(register){
																	  register['DS_TP_CONTA'] = tipoConta[register['TP_CONTA']];
																	  register['DS_TP_NATUREZA'] = tipoNatureza[register['TP_NATUREZA']];
																  },
													 		      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    hiddenFields: [{reference:"E.CD_EMPRESA", value:$('cdEmpresa').value, comparator:_EQUAL, datatype:_INTEGER},
												    			   {reference:"E.NR_ANO_EXERCICIO", value:nrAno, comparator:_EQUAL, datatype:_VARCHAR},
												    			   {reference:"A.TP_CONTA", value:contaAnalitica, comparator:_EQUAL, datatype:_INTEGER}],
												    callback: btnFindContaOnClick,
												    callbackOptions: {tipo: tipoLookup}, 
												    autoExecuteOnEnter: false
										           });
    }
    else {// retorno
		filterWindow.close();
		if (tipoLookup['tipo'] == contaDebito) {
			if ($('field_cd_conta_debito') != null)
		        $('field_cd_conta_debito').value = reg[0]['CD_CONTA_PLANO_CONTAS'];
			if ($('field_cd_conta_debitoView') != null)
		        $('field_cd_conta_debitoView').value = reg[0]['NM_CONTA'];
		}
		else if (tipoLookup['tipo'] == contaCredito) {
			if ($('field_cd_conta_credito') != null)
		        $('field_cd_conta_credito').value = reg[0]['CD_CONTA_PLANO_CONTAS'];
			if ($('field_cd_conta_creditoView') != null)
		        $('field_cd_conta_creditoView').value = reg[0]['NM_CONTA'];
		}
		else {
			if ($('cdConta') != null)
				$('cdConta').value = reg[0]['CD_CONTA'];
			if ($('cdContaView') != null)
				$('cdContaView').value = reg[0]['DS_CONTA'];
			if ($('cdPlanoContas') != null)
				$('cdPlanoContas').value = reg[0]['CD_PLANO_CONTAS'];
			if ($('cdContaPlanoContas') != null) 
				$('cdContaPlanoContas').value = reg[0]['CD_CONTA_PLANO_CONTAS'];
			if ($('cdContaPlanoContasView') != null)
				$('cdContaPlanoContasView').value = reg[0]['DS_CONTA'];
		}
	}
}

function btnFindCentroCustoOnClick(reg, tipoLookup) {
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar centros de custos", 
												    width: 500,
												    height: 350,
												    modal: true,
												    noDrag: true,
												    className: "com.tivic.manager.ctb.CentroCustoServices",
												    method: "findCompleto",
												    allowFindAll: true,
												    filterFields: [[{label:"N� Conta", reference:"A.nr_centro_custo", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
												    			    {label:"Descri��o", reference:"A.nm_centro_custo", datatype:_VARCHAR, comparator:_LIKE_ANY, width:80}]],
												    gridOptions: {columns: [{label:"N� Conta", reference:"nr_centro_custo"},
												   	 					    {label:"Descri��o", reference:"nm_centro_custo"}],
													 		      strippedLines: true,
															      columnSeparator: false,
															      lineSeparator: false},
												    hiddenFields: [{reference:"D.CD_EMPRESA", value:$('cdEmpresa').value, comparator:_EQUAL, datatype:_INTEGER},
												    			   {reference:"D.NR_ANO_EXERCICIO", value:nrAno, comparator:_EQUAL, datatype:_VARCHAR}],
												    callback: btnFindCentroCustoOnClick, 
												    callbackOptions: {tipo: tipoLookup}, 
												    autoExecuteOnEnter: false
										           });
    }
    else {// retorno
		filterWindow.close();
		if (tipoLookup['tipo'] == contaDebito) {
			if ($('field_cd_centro_custo_debito') != null)
		        $('field_cd_centro_custo_debito').value = reg[0]['CD_CENTRO_CUSTO'];
			if ($('field_cd_centro_custo_debitoView') != null)
		        $('field_cd_centro_custo_debitoView').value = reg[0]['DS_CENTRO_CUSTO'];
		}
		else if (tipoLookup['tipo'] == contaCredito) {
			if ($('field_cd_centro_custo_credito') != null)
		        $('field_cd_centro_custo_credito').value = reg[0]['CD_CENTRO_CUSTO'];
			if ($('field_cd_centro_custo_creditoView') != null)
		        $('field_cd_centro_custo_creditoView').value = reg[0]['DS_CENTRO_CUSTO'];
		}
		else if (tipoLookup['tipo'] == contaRateio) {
			if ($('field_cd_centro_custo') != null)
		        $('field_cd_centro_custo').value = reg[0]['CD_CENTRO_CUSTO'];
			if ($('field_cd_centro_custoView') != null)
		        $('field_cd_centro_custoView').value = reg[0]['DS_CENTRO_CUSTO'];
		}
		else {
			if ($('cdCentroCusto') != null)
				$('cdCentroCusto').value = reg[0]['CD_CENTRO_CUSTO'];
			if ($('cdCentroCustoView') != null)
				$('cdCentroCustoView').value = reg[0]['DS_CENTRO_CUSTO'];
		}
	}
}

