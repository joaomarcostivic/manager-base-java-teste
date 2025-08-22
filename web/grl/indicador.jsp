<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<security:registerForm idForm="formIndicador"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, grid2.0, toolbar, form, filter" compress="false"/>
<script language="javascript">
var disabledFormIndicador = false, disabledFormVariacao = false;
var _MENSAL = 0, _ANUAL = 1, _PERIODO = 2, _PERIODO_FAIXA = 3;
function formValidationIndicador()	{
    if(!validarCampo($("nmIndicador"), VAL_CAMPO_NAO_PREENCHIDO, true, "Campo 'Nome do Indicador' deve ser preenchido.", true, null, null))
        return false;
    return true;
}

function initIndicador(){
	ToolBar.create('toolBar', {plotPlace: 'toolBar',
							orientation: 'horizontal',
							buttons: [{id: 'btnNewIndicador', img: '/sol/imagens/form-btNovo16.gif', label: 'Novo', onClick: btnNewIndicadorOnClick},
									{id: 'btnEditIndicador', img: '/sol/imagens/form-btAlterar16.gif', label: 'Alterar', onClick: btnAlterIndicadorOnClick},
									{id: 'btnSaveIndicador', img: '/sol/imagens/form-btSalvar16.gif', label: 'Gravar', onClick: btnSaveIndicadorOnClick},
									{id: 'btnDeleteIndicador', img: '/sol/imagens/form-btExcluir16.gif', label: 'Excluir', onClick: btnDeleteIndicadorOnClick},
									{separator: 'horizontal'},
									{id: 'btnFindIndicador', img: '/sol/imagens/form-btPesquisar16.gif', label: 'Pesquisar', onClick: btnFindIndicadorOnClick}]});
    indicadorFields = [];
    indicadorVariacaoFields = [];
    indicadorVariacaoFaixaFields = [];
    loadFormFields(["indicador", "indicadorVariacao", "indicadorVariacaoFaixa"]);
	$('nmIndicador').focus();
    enableTabEmulation();
	
    var prVariacaoMask = new Mask($("prVariacao").getAttribute("mask"), "number");
    prVariacaoMask.attach($("prVariacao"));
    prVariacaoMask.attach($("vlReferencia1"));
    prVariacaoMask.attach($("vlReferencia2"));
    prVariacaoMask.attach($("vlReferencia3"));
	
    var dtInicioMask = new Mask($("dtInicio").getAttribute("mask"), "date");
    dtInicioMask.attach($("dtInicio"));
	getAllVariacao(null);
	getAllVariacaoFaixa(null);
	loadOptions($('tpIndicador'), <%=sol.util.Jso.getStream(com.tivic.manager.grl.IndicadorServices.tiposVariacao)%>);
}

function clearFormIndicador(){
    $("dataOldIndicador").value = "";
    disabledFormIndicador = false;
    clearFields(indicadorFields);
	getAllVariacao(null);
	getAllVariacaoFaixa(null);
    alterFieldsStatus(true, indicadorFields, "nmIndicador");
}

function btnNewIndicadorOnClick(){
    clearFormIndicador();
}

function btnAlterIndicadorOnClick(){
    disabledFormIndicador = false;
    alterFieldsStatus(true, indicadorFields, "nmIndicador");
}

function btnSaveIndicadorOnClick(content){
    if(content==null){
        if (disabledFormIndicador){
            createMsgbox("jMsg", {width: 250,
                                  height: 100,
                                  message: "Para atualizar os dados, coloque o registro em modo de edição.",
                                  msgboxType: "INFO"});
        }
        else if (formValidationIndicador()) {
            var executionDescription = $("cdIndicador").value>0 ? formatDescriptionUpdate("Indicador", $("cdIndicador").value, $("dataOldIndicador").value, indicadorFields) : formatDescriptionInsert("Indicador", indicadorFields);
            if($("cdIndicador").value>0)
                getPage("POST", "btnSaveIndicadorOnClick", "../methodcaller?className=com.tivic.manager.grl.IndicadorDAO"+
                                                          "&method=update(new com.tivic.manager.grl.Indicador(cdIndicador: int, nmIndicador: String, tpIndicador: int, lgAcumulativo: int):com.tivic.manager.grl.Indicador)", indicadorFields, null, null, executionDescription);
            else
                getPage("POST", "btnSaveIndicadorOnClick", "../methodcaller?className=com.tivic.manager.grl.IndicadorDAO"+
                                                          "&method=insert(new com.tivic.manager.grl.Indicador(cdIndicador: int, nmIndicador: String, tpIndicador: int, lgAcumulativo: int):com.tivic.manager.grl.Indicador)", indicadorFields, null, null, executionDescription);
        }
    }
    else{
        var ok = false;
        if($("cdIndicador").value<=0)	{
            $("cdIndicador").value = content;
            ok = ($("cdIndicador").value > 0);
        }
        else
            ok = (parseInt(content, 10) > 0);
        if(ok){
            disabledFormIndicador=true;
            alterFieldsStatus(false, indicadorFields, "nmIndicador", "disabledField");
            createTempbox("jMsg", {width: 270,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldIndicador").value = captureValuesOfFields(indicadorFields);
        }
        else{
            createTempbox("jMsg", {width: 270,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

var filterWindow;
function btnFindIndicadorOnClick(reg){
    if(!reg){
		filterWindow = FilterOne.create("jFiltro", {caption:"Pesquisar Indicadores",
													width: 270, height: 250,
													modal: true, allowFindAll: true, noDrag: true,
													// Parametros do filtro
													className: "com.tivic.manager.grl.IndicadorDAO",
													method: "find",
													filterFields: [[{label:"Indicador",reference:"NM_INDICADOR",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100}]],
													gridOptions:{columns:[{label:"Indicador",reference:"NM_INDICADOR"}]},
													callback: btnFindIndicadorOnClick
									 });
    }
    else {// retorno
        filterWindow.close();
        disabledFormIndicador=true;
        alterFieldsStatus(false, indicadorFields, "nmIndicador", "disabledField");
		loadFormRegister(indicadorFields, reg[0]);
        $("dataOldIndicador").value = captureValuesOfFields(indicadorFields);
        /* CARREGUE OS GRIDS AQUI */
        getAllVariacao(null);
		tpIndicadorOnChange(getValue('tpIndicador'));
    }
}

function btnDeleteIndicadorOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Indicador", $("cdIndicador").value, $("dataOldIndicador").value);
    getPage("GET", "btnDeleteIndicadorOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.IndicadorServices"+
            "&method=delete(const "+$("cdIndicador").value+":int):int", null, null, null, executionDescription);
}

function btnDeleteIndicadorOnClick(content){
    if(content==null){
        if ($("cdIndicador").value == 0)
            createMsgbox("jMsg", {width: 270, 
                                  height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 270, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteIndicadorOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 270, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            clearFormIndicador();
        }
        else
            createTempbox("jTemp", {width: 270, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function tpIndicadorOnChange(value)	{
	$('contentGridReferencia').style.display = value>2 ? '' : 'none';
	gridVariacao.resize(303, value>2 ? 88 : 200);
}
/**********************************************************************************************************************************
 **********************************************************************************************************************************
 *                                                       FORM VARIAÇÃO															  *
 **********************************************************************************************************************************
 **********************************************************************************************************************************/
var gridVariacao = null;
var formVariacao = null, isUpdate = false;

function btnNewVariacaoOnClick()	{
	if($('cdIndicador').value<=0)	{
		alert('Você selecionar o indicador para o qual deseja lançar variações!');
		return;
	}
	isUpdate = false;
	clearFields(indicadorVariacaoFields);
	$('elementNrMes').style.display    = getValue('tpIndicador')==_MENSAL ? '' : 'none';
	$('elementNrAno').style.display    = getValue('tpIndicador')==_MENSAL||getValue('tpIndicador')==_ANUAL ? '' : 'none';
	$('elementDtInicio').style.display = getValue('tpIndicador')==_MENSAL||getValue('tpIndicador')==_ANUAL ? 'none' : '';
	formVariacao = createWindow("jVariacao", {caption:"Adicionando Variação",
								 width: 203,
								 height: 82,
								 contentDiv: "IndicadorVariacao",
								 noDropContent: true,
								 modal: true});
	if(getValue('tpIndicador')==_MENSAL)
		$('nrMes').focus();
	else if(getValue('tpIndicador')==_ANUAL)
		$('nrAno').focus();
	else
		$('dtInicio').focus();
}

function btnAlterVariacaoOnClick()	{
	isUpdate = true;
	var reg = gridVariacao.getSelectedRowRegister();
	if(gridVariacao==null || reg==null)	{
		alert('Você selecionar a variação que deseja alterar!');
		return;
	}
	$('dtInicio').value   = reg['DT_INICIO'].split(' ')[0];
	$('prVariacao').value = (new Mask('#,####.00', "number")).format(reg['PR_VARIACAO']);
	$('nrMes').value = getAsDate(reg['DT_INICIO'].split(' ')[0]).getMonth()+1;
	$('nrAno').value = getAsDate(reg['DT_INICIO'].split(' ')[0]).getFullYear();

	$('elementNrMes').style.display = getValue('tpIndicador')==_MENSAL ? '' : 'none';
	$('elementNrAno').style.display = getValue('tpIndicador')==_MENSAL || getValue('tpIndicador')==_ANUAL? '' : 'none';
	$('elementDtInicio').style.display = getValue('tpIndicador')==_MENSAL || getValue('tpIndicador')==_ANUAL ? 'none' : '';
	formVariacao = createWindow("jVariacao", {caption:"Alterando Variação",
								 width: 203,
								 height: 82,
								 contentDiv: "IndicadorVariacao",
								 noDropContent: true,
								 modal: true});
	if(getValue('tpIndicador')==_MENSAL)
		$('nrMes').focus();
	else if(getValue('tpIndicador')==_ANUAL)
		$('nrAno').focus();
	else
		$('dtInicio').focus();
}

function btnDeleteVariacaoOnClickAux(content)	{
	var reg = gridVariacao.getSelectedRowRegister();
    var executionDescription = formatDescriptionDelete("IndicadorVariacao", reg['DT_INICIO'], $("dataOldVariacao").value);
    getPage("GET", "btnDeleteVariacaoOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.IndicadorVariacaoServices"+
            "&method=delete(const "+$("cdIndicador").value+":int,const "+reg['DT_INICIO']+":GregorianCalendar)",
			 null, null, null, executionDescription);
}

function btnDeleteVariacaoOnClick(content){
    if(content==null){
		var reg = gridVariacao.getSelectedRowRegister();
        if (gridVariacao==null || reg==null)	{
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
		}
        else	{
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteVariacaoOnClickAux()", 10)}});
    	}
	}
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, height: 35, message: "Registro excluído com sucesso!", time: 3000});
            gridVariacao.removeSelectedRow();
			getAllVariacaoFaixa(null);
        }
        else	{
            createTempbox("jTemp", {width: 300, 
									  height: 35, 
									  message: "Não foi possível excluir este registro!", 
									  time: 5000});
		}
    }	
}

function btnSaveVariacaoOnClick(content){
	if(content==null){
		var executionDescription = isUpdate ? formatDescriptionUpdate("IndicadorVariacao", $("dtInicio").value, $("dataOldVariacao").value, indicadorVariacaoFields) : formatDescriptionInsert("IndicadorVariacao", indicadorVariacaoFields);
		var nrMes = $('nrMes').value; nrMes = nrMes < 10 ? '0'+nrMes : nrMes;
		if(getValue('tpIndicador')==_MENSAL)
			$('dtInicio').value = '01/'+nrMes+'/'+$('nrAno').value;
		else if(getValue('tpIndicador')==_ANUAL)
			$('dtInicio').value = '01/01/'+$('nrAno').value;
		
		var construtor = "const "+getValue('cdIndicador')+":int,"+
		                 "const "+getValue('dtInicio')+":GregorianCalendar,"+
						 "const "+changeLocale('prVariacao')+": float";
		setTimeout(function() {
					getPage("GET", "btnSaveVariacaoOnClick", 
							"../methodcaller?className=com.tivic.manager.grl.IndicadorVariacaoDAO"+
							"&method="+(isUpdate ? 'update' : 'insert')+"(new com.tivic.manager.grl.IndicadorVariacao("+construtor+"):com.tivic.manager.grl.IndicadorVariacao)", 
							indicadorVariacaoFields, null, null, executionDescription)}, 10);
    }
    else	{
		if(parseInt(content, 10) > 0){
            createTempbox("jMsg", {width: 270,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("dataOldVariacao").value = captureValuesOfFields(indicadorVariacaoFields);
			formVariacao.close();
			getAllVariacao(null);
        }
        else{
            createTempbox("jMsg", {width: 270,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

function getAllVariacao(content)	{
	if(content==null)	{
		gridVariacao = GridOne.create('gridVariacao', {columns: [{label:'Data/Período',reference:'DT_INICIO'},
															     {label:'Variação',reference:'PR_VARIACAO',type:GridOne._CURRENCY}],
													   resultset: null,
													   plotPlace: $('divGridVariacao')});
		if($('cdIndicador').value>0)														
			setTimeout(function() {
						getPage("GET", "getAllVariacao", 
								"../methodcaller?className=com.tivic.manager.grl.IndicadorVariacaoDAO"+
								"&objects=criterios=java.util.ArrayList();"+
								"item0=sol.dao.ItemComparator(const cd_indicador:String,const "+$('cdIndicador').value+":String,"+
								"const "+_EQUAL+":int,const "+_INTEGER+":int)"+
								"&execute=criterios.add(*item0:Object);"+
								"&method=find(*criterios:java.util.ArrayList)", null, null, null, null)}, 10);
	}
	else	{
		var rsm = eval("("+content+")");
		var columns = [{label: 'Data/Período', reference: 'DT_INICIO', type: GridOne._DATE}];
		if(getValue('tpIndicador')==_ANUAL)	{
			columns = [{label: 'Ano', reference: 'DS_PERIODO'}];
			for(var i=0; i<rsm.lines.length; i++)	{
				var data = getAsDate(rsm.lines[i]['DT_INICIO']);
				rsm.lines[i]['DS_PERIODO'] = _monthShortNames[data.getMonth()]+' / '+data.getFullYear();
				rsm.lines[i]['DS_PERIODO'] = data.getFullYear();
			}
		}
		else if(getValue('tpIndicador')==_MENSAL)	{
			columns = [{label: 'Mês', reference: 'DS_PERIODO'}];
			for(var i=0; i<rsm.lines.length; i++)	{
				var data = getAsDate(rsm.lines[i]['DT_INICIO']);
				rsm.lines[i]['DS_PERIODO'] = _monthShortNames[data.getMonth()]+' / '+data.getFullYear();
			}
		}
		if(getValue('tpIndicador') < _PERIODO_FAIXA)
			columns.push({label: 'Variação', reference: 'PR_VARIACAO', type: GridOne._CURRENCY});
		gridVariacao = GridOne.create('gridVariacao', {columns: columns,
													   resultset: rsm,
													   onSelect: function(){getAllVariacaoFaixa(null)},
													   plotPlace: $('divGridVariacao')});
		getAllVariacaoFaixa(null);
	}
}

/**********************************************************************************************************************************
 **********************************************************************************************************************************
 *                                                  FORM VARIAÇÃO FAIXA															  *
 **********************************************************************************************************************************
 **********************************************************************************************************************************/
var gridVariacaoFaixa = null;
var formVariacaoFaixa = null, isUpdate = false;
var vlAnterior = 0;

function btnNewVariacaoFaixaOnClick()	{
	var reg = gridVariacao.getSelectedRowRegister();
	if(gridVariacao==null || reg==null)	{
		alert('Você deve selecionar a variação para a qual deseja acrescentar faixas!');
		return;
	}
	isUpdate = false;
    clearFields(indicadorVariacaoFaixaFields);
	$('vlReferenciaAnterior').value = formatCurrency(vlAnterior);
	formVariacaoFaixa = createWindow("jVariacaoFaixa", {caption:"Adicionando Faixas",
								 width: 276,
								 height: 82,
								 contentDiv: "IndicadorVariacaoFaixa",
								 noDropContent: true,
								 modal: true});
	$('vlReferencia1').select();
	$('vlReferencia1').focus();
}

function btnAlterVariacaoFaixaOnClick()	{
	isUpdate = true;
	var reg = gridVariacaoFaixa.getSelectedRowRegister();
	if(gridVariacaoFaixa==null || reg==null)	{
		alert('Você selecionar a faixa que deseja alterar!');
		return;
	}
	$('cdFaixa').value = reg['CD_FAIXA'];
	$('vlReferenciaAnterior').value = formatCurrency(reg['VL_REFERENCIA_ANTERIOR']);
	$('vlReferencia1').value = formatCurrency(reg['VL_REFERENCIA_1']);
	$('vlReferencia2').value = formatCurrency(reg['VL_REFERENCIA_2']);
	$('vlReferencia3').value = formatCurrency(reg['VL_REFERENCIA_3']);

	formVariacaoFaixa = createWindow("jVariacaoFaixa", {caption:"Adicionando Faixas",
								 width: 276,
								 height: 82,
								 contentDiv: "IndicadorVariacaoFaixa",
								 noDropContent: true,
								 modal: true});
	$('vlReferencia1').select();
	$('vlReferencia1').focus();
}

function btnDeleteVariacaoFaixaOnClickAux(content)	{
    var executionDescription = formatDescriptionDelete("IndicadorVariacaoFaixa", $("cdFaixa").value, $("dataOldVariacaoFaixa").value);
	var reg = gridVariacaoFaixa.getSelectedRowRegister();
    getPage("GET", "btnDeleteVariacaoFaixaOnClick", 
            "../methodcaller?className=com.tivic.manager.grl.IndicadorVariacaoFaixaDAO"+
            "&method=delete(const "+reg['CD_FAIXA']+":int,const "+reg["CD_INDICADOR"]+":int,const "+reg['DT_INICIO']+":GregorianCalendar)",
			 null, null, null, executionDescription);
}

function btnDeleteVariacaoFaixaOnClick(content){
    if(content==null){
		var reg = gridVariacaoFaixa.getSelectedRowRegister();
        if (gridVariacaoFaixa==null || reg==null)
            createMsgbox("jMsg", {width: 300, 
                                  height: 120, 
                                  message: "Nenhuma registro foi carregado para que seja excluído.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclusão de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Você tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteVariacaoFaixaOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro excluído com sucesso!",
                                  time: 3000});
            gridVariacaoFaixa.removeSelectedRow();
        }
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Não foi possível excluir este registro!", 
                                  time: 5000});
    }	
}

function btnSaveVariacaoFaixaOnClick(content){
	if(content==null){
		var executionDescription = isUpdate ? formatDescriptionUpdate("IndicadorVariacao", $("dtInicio").value, $("dataOldVariacao").value, indicadorVariacaoFields) : formatDescriptionInsert("IndicadorVariacao", indicadorVariacaoFields);
		var reg = gridVariacao.getSelectedRowRegister();

		var construtor = "const "+getValue('cdFaixa')+":int, const "+getValue('cdIndicador')+":int,"+
		                 "const "+reg['DT_INICIO'].split(' ')[0]+":GregorianCalendar,"+
						 "const "+changeLocale('vlReferencia1')+": float,"+
						 "const "+changeLocale('vlReferencia2')+": float,"+
						 "const "+changeLocale('vlReferencia3')+": float";
		setTimeout(function() {
					getPage("GET", "btnSaveVariacaoFaixaOnClick", 
							"../methodcaller?className=com.tivic.manager.grl.IndicadorVariacaoFaixaDAO"+
							"&method="+(isUpdate ? 'update' : 'insert')+"(new com.tivic.manager.grl.IndicadorVariacaoFaixa("+construtor+"):com.tivic.manager.grl.IndicadorVariacaoFaixa)", 
							null, null, null, executionDescription)}, 10);
    }
    else	{
		if(parseInt(content, 10) > 0){
            createTempbox("jMsg", {width: 270,
                                   height: 50,
                                   message: "Dados gravados com sucesso!",
                                   tempboxType: "INFO",
                                   time: 2000});
            $("cdFaixa").value = content;
            $("dataOldVariacaoFaixa").value = captureValuesOfFields(indicadorVariacaoFaixaFields);
			formVariacaoFaixa.close();
			getAllVariacaoFaixa(null);
        }
        else{
            createTempbox("jMsg", {width: 270,
                                   height: 50,
                                   message: "ERRO ao tentar gravar dados!",
                                   tempboxType: "ERROR",
                                   time: 3000});
        }
    }
}

function getAllVariacaoFaixa(content)	{
	if(content==null)	{
		gridVariacaoFaixa = GridOne.create('gridVariacaoFaixa', {columns: [{label: 'De', reference: 'VL_REFERENCIA_ANTERIOR'},
																		   {label: 'Até', reference: 'VL_REFERENCIA_1', type: GridOne._CURRENCY},
																		   {label: '%/R$', reference: 'VL_REFERENCIA_2', type: GridOne._CURRENCY},
																		   {label: 'Dedução', reference: 'VL_REFERENCIA_3', type: GridOne._CURRENCY}],
																 resultset: null,
																 plotPlace: $('divGridVariacaoFaixa')});
		var reg = gridVariacao.getSelectedRowRegister();
		if(gridVariacao!=null && reg!=null && getValue('tpIndicador')==3)	{
			setTimeout(function() {
						getPage("GET", "getAllVariacaoFaixa", 
								"../methodcaller?className=com.tivic.manager.grl.IndicadorVariacaoFaixaDAO"+
								"&objects=criterios=java.util.ArrayList();"+
								"item0=sol.dao.ItemComparator(const cd_indicador:String,const "+$('cdIndicador').value+":String,"+
								"const "+_EQUAL+":int,const "+_INTEGER+":int);"+
								"item1=sol.dao.ItemComparator(const dt_inicio:String,const "+reg['DT_INICIO'].split(' ')[0]+":String,"+
								"const "+_EQUAL+":int,const "+_TIMESTAMP+":int)"+
								"&execute=criterios.add(*item0:Object); criterios.add(*item1:Object);"+
								"&method=find(*criterios:java.util.ArrayList)", null, null, null, null)}, 10);
			//alert();
		}
	}
	else	{
		var rsm = eval("("+content+")");
		vlAnterior = 0.00;
		for(var i=0; i<rsm.lines.length; i++)	{
			rsm.lines[i]['VL_REFERENCIA_ANTERIOR'] = vlAnterior;
			vlAnterior = rsm.lines[i]['VL_REFERENCIA_1']+0.01;
		}
		gridVariacaoFaixa = GridOne.create('gridVariacaoFaixa', {columns: [{label: 'De', reference: 'VL_REFERENCIA_ANTERIOR', type: GridOne._CURRENCY},
																		   {label: 'Até', reference: 'VL_REFERENCIA_1', type: GridOne._CURRENCY},
																		   {label: '%/R$', reference: 'VL_REFERENCIA_2', type: GridOne._CURRENCY},
																		   {label: 'Dedução', reference: 'VL_REFERENCIA_3', type: GridOne._CURRENCY}],
																 resultset: rsm,
																 plotPlace: $('divGridVariacaoFaixa')});
	}
}

</script>
</head>
<body class="body" onload="initIndicador();">
<div style="width: 330px;" id="Indicador" class="d1-form">
  <div style="width: 330px; height: 275px;" class="d1-body">
    <input idform="" reference="" id="contentLogIndicador" name="contentLogIndicador" type="hidden"/>
    <input idform="" reference="" id="dataOldIndicador" name="dataOldIndicador" type="hidden"/>
    <input idform="indicador" reference="cd_indicador" id="cdIndicador" name="cdIndicador" type="hidden"/>
    <input idform="indicador" reference="lg_acumulativo" id="lgAcumulativo" name="lgAcumulativo" type="hidden"/>
    <div id="toolBar" class="d1-toolBar" style="height:24px; width: 328px;"></div>
    <div class="d1-line" id="line0">
      <div style="width: 220px;" class="element">
        <label class="caption" for="nmIndicador">Nome do Indicador</label>
        <input style="text-transform: uppercase; width: 216px;" lguppercase="true" class="field" idform="indicador" reference="nm_indicador" datatype="STRING" id="nmIndicador" name="nmIndicador" type="text" maxlength="50"/>
      </div>
      <div style="width: 108px;" class="element">
        <label class="caption" for="tpIndicador">Periodicidade</label>
        <select style="width: 108px;" class="select" idform="indicador" reference="tp_indicador" datatype="INT" id="tpIndicador" name="tpIndicador" defaultValue="1" onchange="tpIndicadorOnChange(this.value);">
        </select>
      </div>
    </div>
	<div style="width: 331px;" class="element">
	  	<label class="caption">Variações</label>
	  	<div id="divGridVariacao" style="float:left; width: 303px; height:200px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
		<div style="width: 20px;" class="element">
		  <security:actionAccessByObject><button title="Nova Variação" onclick="btnNewVariacaoOnClick();" style="margin-bottom:2px" id="btnNewVariacao" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
		  <security:actionAccessByObject><button title="Alterar Variação" onclick="btnAlterVariacaoOnClick();" style="margin-bottom:2px" id="btnAlterVariacao" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
		  <security:actionAccessByObject><button title="Excluir Variação" onclick="btnDeleteVariacaoOnClick();" id="btnDeleteVariacao" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
		</div>
	</div>
	<div style="width: 331px; display:none;" class="element" id="contentGridReferencia">
	  	<label class="caption">Faixas</label>
	  	<div id="divGridVariacaoFaixa" style="float:left; width: 303px; height:95px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
		<div style="width: 20px;" class="element">
		  <security:actionAccessByObject><button title="Nova Faixa" onclick="btnNewVariacaoFaixaOnClick();" style="margin-bottom:2px" id="btnNewVariacaoFaixa" class="toolButton"><img src="/sol/imagens/form-btNovo16.gif" height="16" width="16"/></button></security:actionAccessByObject>
		  <security:actionAccessByObject><button title="Alterar Faixa" onclick="btnAlterVariacaoFaixaOnClick();" style="margin-bottom:2px" id="btnAlterVariacaoFaixa" class="toolButton"><img src="/sol/imagens/form-btAlterar16.gif" height="16" width="16"/></button></security:actionAccessByObject>
		  <security:actionAccessByObject><button title="Excluir Faixa" onclick="btnDeleteVariacaoFaixaOnClick();" id="btnDeleteVariacaoFaixa" class="toolButton"><img src="/sol/imagens/form-btExcluir16.gif" height="16" width="16"/></button></security:actionAccessByObject>
		</div>
	</div>
  </div>
</div>

<div style="width: 193px; display:none;" id="IndicadorVariacao" class="d1-form">
	<div style="width: 193px; height: 205px;" class="d1-body">
		<input idform="" reference="" id="contentLogVariacao" name="contentLogVariacao" type="hidden"/>
		<input idform="" reference="" id="dataOldVariacao" name="dataOldVariacao" type="hidden"/>
		<div class="d1-line" id="line0">
		  <div style="width: 108px; display:none;" class="element" id="elementDtInicio">
			<label class="caption" for="dtInicio">Início da Vigência</label>
			<input style="width: 104px;" mask="dd/mm/yyyy" class="field" idform="indicadorVariacao" datatype="DATE" id="dtInicio" name="dtInicio" type="text"/>
		  </div>
		  <div style="width: 78px;" class="element" id="elementNrMes">
			<label class="caption" for="nrMes">Mês</label>
			<select style="width: 75px;" class="select" idform="indicadorVariacao" datatype="STRING" id="nrMes" name="nrMes" defaultValue="1">
				<option value="1">Janeiro</option>
				<option value="2">Fevereiro</option>
				<option value="3">Março</option>
				<option value="4">Abril</option>
				<option value="5">Maio</option>
				<option value="6">Junho</option>
				<option value="7">Julho</option>
				<option value="8">Agosto</option>
				<option value="9">Setembro</option>
				<option value="10">Outubro</option>
				<option value="11">Novembro</option>
				<option value="12">Dezembro</option>
			</select>
		  </div>
		  <div style="width: 30px;" class="element" id="elementNrAno">
			<label class="caption" for="nrAno">Ano</label>
			<input style="width: 27px;" class="field" idform="indicadorVariacao" datatype="INT" id="nrAno" name="nrAno" type="text" defaultvalue="2006"/>
		  </div>
		  <div style="width: 80px;" class="element" id="elementPrVariacao">
			<label class="caption" for="prVariacao">Variação</label>
			<input style="width: 80px;" mask="#,###.00" class="field" idform="indicadorVariacao" datatype="FLOAT" id="prVariacao" name="prVariacao" type="text" defaultvalue="0,00"/>
		  </div>
		</div>
		<div class="d1-line" id="line6" style="float:right; width:165px; margin:2px 0px 0px 0px;">
			<div style="width:80px;" class="element">
				<button id="btnSaveVariacao" title="Gravar informações" onclick="btnSaveVariacaoOnClick(null);" style="width:80px; border:1px solid #999999" class="toolButton">
						<img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Confirmar
				</button>
			</div>
			<div style="width:80px;" class="element">
				<button id="btnCancelar" title="Voltar para a janela anterior" onclick="formVariacao.close();" style="margin-left:2px; width:80px; border:1px solid #999;" class="toolButton"><img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar</button>
			</div>
		</div>
	</div>
</div>

<div style="width: 267px; display:none;" id="IndicadorVariacaoFaixa" class="d1-form">
	<div style="width: 267px; height: 205px;" class="d1-body">
		<input idform="" reference="" id="contentLogVariacaoFaixa" name="contentLogVariacaoFaixa" type="hidden"/>
		<input idform="" reference="" id="dataOldVariacaoFaixa" name="dataOldVariacaoFaixa" type="hidden"/>
    	<input idform="indicadorVariacaoFaixa" reference="cd_faixa" id="cdFaixa" name="cdFaixa" type="hidden"/>
		<div class="d1-line" id="line0">
		  <div style="width: 70px;" class="element">
			<label class="caption" for="vlReferenciaAnterior">De</label>
			<input style="width: 67px;" mask="#,##0.00" class="disabledField" disabled="disabled" idform="indicadorVariacaoFaixa" datatype="FLOAT" id="vlReferenciaAnterior" name="vlReferenciaAnterior" type="text" defaultValue="0,00"/>
		  </div>
		  <div style="width: 70px;" class="element">
			<label class="caption" for="vlReferencia1">Até</label>
			<input style="width: 67px;" mask="#,##0.00" class="field" idform="indicadorVariacaoFaixa" datatype="FLOAT" id="vlReferencia1" name="vlReferencia1" type="text" defaultValue="0,00"/>
		  </div>
		  <div style="width: 50px;" class="element">
			<label class="caption" for="vlReferencia2">% / R$</label>
			<input style="width: 47px;" mask="#,##0.00" class="field" idform="indicadorVariacaoFaixa" datatype="FLOAT" id="vlReferencia2" name="vlReferencia2" type="text" defaultValue="0,00"/>
		  </div>
		  <div style="width: 72px;" class="element">
			<label class="caption" for="vlReferencia3">Parc. Dedução</label>
			<input style="width: 72px;" mask="#,##0.00" class="field" idform="indicadorVariacaoFaixa" datatype="FLOAT" id="vlReferencia3" name="vlReferencia3" type="text" defaultValue="0,00"/>
		  </div>
		</div>
		<div class="d1-line" id="line6" style="float:right; width:165px; margin:2px 0px 0px 0px;">
			<div style="width:80px;" class="element">
				<button id="btnSaveVariacao" title="Gravar informações" onclick="btnSaveVariacaoFaixaOnClick(null);" style="width:80px; border:1px solid #999999" class="toolButton">
						<img src="/sol/imagens/form-btSalvar16.gif" height="16" width="16"/>Confirmar
				</button>
			</div>
			<div style="width:80px;" class="element">
				<button id="btnCancelar" title="Voltar para a janela anterior" onclick="formVariacaoFaixa.close();" style="margin-left:2px; width:80px; border:1px solid #999999" class="toolButton">
						<img src="/sol/imagens/form-btCancelar16.gif" height="16" width="16"/>Cancelar
				</button>
			</div>
		</div>
	</div>
</div>
</body>
</html>
