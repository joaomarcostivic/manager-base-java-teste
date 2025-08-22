<%@ page contentType="text/html; charset=iso-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.egov.TransparenciaServices"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<title>Transparência - Despesas</title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<script language="javascript" src="/sol/js/scriptaculous/prototype.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/builder.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/effects.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/dragdrop.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/controls.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/slider.js"></script>
<script language="javascript" src="/sol/js/masks.js"></script>
<script language="javascript" src="/sol/js/masks2.0.js"></script>
<script language="javascript" src="/sol/js/util.js"></script>
<script language="javascript" src="/sol/js/ajax.js"></script>
<script language="javascript" src="/sol/js/shortcut.js"></script>
<script language="javascript" src="/sol/js/form.js"></script>
<script language="javascript" src="/sol/js/janela2.0.js"></script>
<script language="javascript" src="/sol/js/grid2.0.js"></script>
<link href="/sol/css/form.css" rel="stylesheet" type="text/css" />
<link href="/sol/css/grid2.0.css" rel="stylesheet" type="text/css" />
<link href="/sol/css/janela2.0.css" rel="stylesheet" type="text/css" />
<link href="/sol/css/report.css" rel="stylesheet" type="text/css" />
<%
	if(sol.util.RequestUtilities.getAsInteger(request, "arquivo", 0)==1)
		response.sendRedirect("admin.jsp");
	TransparenciaServices.registraAcesso(request);
%>
<style type="text/css">
#rodape	{
	height:25px; 
	width:777px; 
	margin:0px; 
	clear:both; 
	background-image:url(imagens/margem_inf2.jpg); 
	border-bottom:solid 1px #CCC;	
	position:relative;
	background-repeat:repeat-x;
	border-top:solid 1px #CCC;
	border-collapse:collapse;
}
</style>
<script language="javascript">
var rsmDespesa = null;
var gridDespesa;
var columnsDespesa = [{label:'Tipo', reference: 'CL_ETAPA', columnWidth: '50px'}, 
					  {label:'Data', reference: 'DT_ETAPA', type: GridOne._DATE},
    				  {label:'Nº Processo', reference: 'NR_PROCESSO'}, 
					  {label:'Unidade Orçamentária', reference: 'NM_UNIDADE_ORCAMENTARIA'},							   
					  {label:'Favorecido (Credor)', reference: 'NM_CREDOR'},
    				  {label:'Nº Etapa', reference: 'NR_ETAPA'},
    				  {label:'Processo Licitatório', reference: 'DS_MODALIDADE'},
    				  {label:'Nº Proc. Licitatório', reference: 'NR_COMPRA'}];

function init()	{
	try	{
		$('divDespesa').style.marginLeft = Math.round((screen.availWidth-780) / 2)+'px';
	}catch(e){};
  	addShortcut('ctrl+p', btPesquisarOnClick);
	enableTabEmulation();
	
	loadOptionsFromRsm($('dtEtapa'), <%=sol.util.Jso.getStream(com.tivic.manager.egov.TransparenciaServices.getDates(1))%>, 
	                   {fieldValue: 'dt_etapa', fieldText:'cl_data', 
	                    onProcessRegister: function(reg)	{
	                    	var dtEtapa = getAsDate(reg['DT_ETAPA']);
	                    	reg['CL_DATA'] = _diaSemanaNames[dtEtapa.getDay()]+', '+dtEtapa.getDate()+' de '+_monthNames[dtEtapa.getMonth()]+' de '+dtEtapa.getFullYear();
	                    }});
    $('dtEtapa').focus();
    $('dtEtapa').selectedIndex = $('dtEtapa').options.length-1;
	createGrid(null);
}

function btPesquisarOnClick(content) {
	if(content==null)	{
		$('labelResultado').innerHTML = 'Resultado da Pesquisa';
		var objetos = 'crt=java.util.ArrayList();',
			execute = '';
		// Tipo de Etapa
		if($('tpEtapa').value>=0)	{
			objetos += 'tpEtapa=sol.dao.ItemComparator(const A.tp_etapa:String,tpEtapa:String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*tpEtapa:Object);';
		}
		// Nº do Processo
		if($('nrProcesso').value!='')	{
			objetos += 'itemProcesso=sol.dao.ItemComparator(const A.nr_processo:String,nrProcesso:String,const '+_EQUAL+':int,const '+_VARCHAR+':int);';
			execute += 'crt.add(*itemProcesso:Object);';
		}
		// Nº do processo licitatório
		if($('nrCompra').value!='')	{
			objetos += 'itemCompra=sol.dao.ItemComparator(const A.nr_compra:String,nrCompra:String,const '+_EQUAL+':int,const '+_VARCHAR+':int);';
			execute += 'crt.add(*itemCompra:Object);';
		}
		// Data
		if($('dtEtapa').value=='')	{
			alert('Selecione o dia do movimento que deseja visualizar!');
			return;
		}
		objetos += 'itemDtEtapa=sol.dao.ItemComparator(const A.dt_etapa:String,dtEtapa:String,const '+_EQUAL+':int,const '+_TIMESTAMP+':int);';
		execute += 'crt.add(*itemDtEtapa:Object);';
		// Situação do arquivo
		objetos += 'itemStArquivo=sol.dao.ItemComparator(const st_arquivo:String,const 1:String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
		execute += 'crt.add(*itemStArquivo:Object);';
		formMensagem = createTempbox("jMsg", {width: 165, height: 50, message: "Aguarde... pesquisando!", tempboxType: "LOADING", time: 0});
		createGrid(null);
		var fields = [$('cdUnidadeOrcamentaria'),$('dsModalidade'),$('tpEtapa'),$('dtEtapa'),
		              $('nrProcesso'),$('nrCompra'),$('nrEtapa')];
		// BUSCANDO
		setTimeout(function()	{
				   getPage('POST', 'btPesquisarOnClick', 
						   '../methodcaller?className=com.tivic.manager.egov.TransparenciaServices'+
						   '&objects='+objetos+
						   (execute!=''?'&execute=':'')+execute+
						   '&method=findDespesa(*crt:java.util.ArrayList)', fields)}, 10);
	}
	else {	// retorno
		formMensagem.close();
		rsmDespesa = eval("("+content+")");
		var qt = rsmDespesa.lines.length;
		$('labelResultado').innerHTML = (qt==0)?'Nenhuma despesa encontrada':(qt==1)?'1 despesa encontrada':qt+' despesas encontradas';
		createGrid(rsmDespesa);
	}
}

function createGrid(rsm)	{
	var etapa = ['Empenho','Liquidação','Pagamento'];
	gridDespesa = GridOne.create('gridDespesa', {columns: columnsDespesa, resultset: rsm, plotPlace: $('divGridDespesa'),
											     onDoubleClick: viewDespesa,
												 columnSeparator: true, lineSeparator: false, strippedLines: true, noSelectOnCreate: false,
												 onProcessRegister: function(reg){
														reg['CL_ETAPA'] = etapa[reg['TP_ETAPA']];
												 }});

}

function viewDespesa(el)	{
	FormFactory.createFormWindow('jDetalhe', 
	                     {caption: "Detalhe da Despesa", width: 500, height: 378, unitSize: '%',
						  id: 'detalheDespesa', loadForm: true, noDrag: true, modal: true,
						  lines: [[{id:'nmUnidadeOrcamentaria', reference: 'nm_unidade_orcamentaria', readonly:true, type:'text', label:'Unidade Orçamentária', width:100}],,
						          [{id:'dtPublicacao', reference: 'dt_publicacao', readonly:true, type:'text', label:'Data de Publicação', width:20},
						  		   {id:'dsEtapa', reference: 'cl_etapa', readonly:true, type:'text', label:'Etapa', width:30},
						  		   {id:'dtEtapaForm', reference: 'dt_etapa', readonly:true, type:'text', label:'Data', width:20},
						  		   {id:'nrProcessoForm', reference: 'nr_processo', readonly:true, type:'text', label:'Nº Processo Administrativo', width:30}],
						  		  [{id:'dsBemServico', reference: 'ds_bem_servico', disabled:true, type:'textarea', height: 70, label:'Bem ou serviço prestado', width:100}],
						  		  [{id:'nmCredorForm', reference: 'nm_credor', readonly:true, type:'text', label:'Credor', width:100}],
						  		  [{id:'vlDespesa', reference: 'vl_despesa', readonly:true, type:'text', label:'Valor', width:20, value: '0,00'},
						  		   {id:'nrEtapaForm', reference: 'nr_etapa', readonly:true, type:'text', label:'Nº Etapa', width:20},
						  		   {id:'dsModalidadeForm', reference: 'ds_modalidade', readonly:true, type:'text', label:'Processo Licitatório', width:40},
						  		   {id:'nrCompraForm', reference: 'nr_compra', readonly:true, type:'text', label:'Nº Proc. Licitatório', width:20}],
						  		  [{id:'nmFuncao', reference: 'nm_funcao', readonly:true, type:'text', label:'Função:', width:100}],
						  		  [{id:'nmSubFuncao', reference: 'nm_subfuncao', readonly:true, type:'text', label:'Sub-Função', width:100}],
						  		  [{id:'nmNaturezaDespesa', reference: 'nm_natureza_despesa', readonly:true, type:'text', label:'Natureza da Despesa', width:100}],
						  		  [{id:'nmFonteRecurso', reference: 'nm_fonte_recurso', readonly:true, type:'text', label:'Fonte do Recurso', width:100}],
								  [{type: 'space', width: 80},
								   {id:'btnFechar', type:'button', label:'Fechar', width:20, height:19, image: '/sol/imagens/cancel_13.gif', 
								   	onClick: function(){
								   		closeWindow('jDetalhe'); 
								   	}
								   }
								  ]]});
	$('vlDespesa').style.textAlign = 'right';
	var reg = gridDespesa.getSelectedRowRegister();
	$('nmUnidadeOrcamentaria').value  = reg['NM_UNIDADE_ORCAMENTARIA'];
	$('dtPublicacao').value 	 = reg['DT_PUBLICACAO'].split(' ')[0];
	$('dsEtapa').value           = reg['CL_ETAPA'];						  
	$('dtEtapaForm').value       = reg['DT_ETAPA'].split(' ')[0];						  
	$('nrProcessoForm').value    = reg['NR_PROCESSO'];
	$('dsBemServico').value      = reg['TXT_BEM_SERVICO'];
	$('nmCredorForm').value      = reg['NM_CREDOR'];
	$('vlDespesa').value 		 = formatCurrency(reg['VL_DESPESA']);
	$('nrEtapaForm').value       = reg['NR_ETAPA'];
	$('dsModalidadeForm').value  = reg['DS_MODALIDADE'];
	$('nrCompraForm').value      = reg['NR_COMPRA'];
	$('nmFuncao').value      	 = reg['NM_FUNCAO'];
	$('nmSubFuncao').value       = reg['NM_SUBFUNCAO'];
	$('nmNaturezaDespesa').value = reg['NM_NATUREZA_DESPESA'];
	$('nmFonteRecurso').value    = reg['NM_FONTE_RECURSO'];
}
</script>
</head>
<body class="body" onload="init()" margin="0" topmargin="0" marginheight="0" rightmargin="0" bgcolor="#F1F1F3">
<div style="width: 777px;" id="divDespesa" class="d1-form">
	<input id="cdUsuario" name="cdUsuario" value="0" type="hidden"/>	
   <div style="width: 777px; height: 555px; background-color: #FFF; border:1px solid #CCC;" class="d1-body">
	<div id="topo">
	  <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="777" height="130" title="imagem_flash">
	    <param name="movie" value="imagens/animacao.swf" />
	    <param name="quality" value="high" />
	    <embed src="imagens/animacao.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="777" height="130"></embed>
	  </object>
	</div>
	<div class="d1-line" id="line0">
		<div class="element" style="width:767px; border-bottom: 1px solid #555; margin-left: 5px; height: 20px;">
			<label for="tpEtapa" class="caption" style="font-size: 18px; height: 30px; color:#364C87; font-weight: bold;">Despesas</label>
		</div>
	</div>
	 <div class="d1-line" id="line0">
		<div style="width: 220px; display: none;" class="element">
			<label class="caption" for="cdUnidadeOrcamentaria" style="color: #444;">Unidade Orçamentária</label>
			<select style="width: 217px;" class="select1" idform="Despesa" reference="cd_empresa" datatype="INT" id="cdUnidadeOrcamentaria" name="cdUnidadeOrcamentaria">
				<option value="0">Todas</option>
			</select>
		</div>
		<div class="element" style="width:190px; display: none;">
			<label class="caption" style="color: #444;">Modalidade de Compra</label>
			<select style="width:187px" type="text" name="" id="dsModalidade" class="select1" >
				<option value="-1">Todas</option>
			</select>
		</div>
		<div class="element" style="width:170px; margin-left: 5px;">
			<label class="caption" title="Tipo de Etapa" style="color: #444;">Tipo de Etapa</label>
			<select style="width:167px" type="text" name="tpEtapa" id="tpEtapa" class="select1">
				<option value="-1">Todos</option>
				<option value="0">Empenho</option>
				<option value="1">Liquidação</option>
				<option value="2">Pagamento</option>
			</select>
		</div>
	  	<div style="width:130px;" class="element">
			<label class="caption" style="color: #444;">Nº Processo</label>
			<input type="text" name="nrProcesso" id="nrProcesso" class="field1" style="width:120px;" value=""/>
	  	</div>
	  	<div style="width:130px;" class="element">
			<label class="caption" style="color: #444;">Nº Proc. Licitatório</label>
			<input type="text" name="nrCompra" id="nrCompra" class="field1" style="width:120px;" value=""/>
	  	</div>
	  	<div style="width:100px; display: none;" class="element">
			<label class="caption" style="color: #444;">Nº Etapa</label>
			<input type="text" name="nrEtapa" id="nrEtapa" class="field1" style="width:90px;" value=""/>
	  	</div>
		<div class="element" style="width:335px;">
			<label class="caption" style="color: #444;">Data da Etapa (Empenho/Liquidação/Pagamento)</label>
			<select style="width:335px" name="dtEtapa" id="dtEtapa" class="select1">
			</select>
		</div>
	</div>
    <div class="d1-toolBar" id="toolBarDespesa" style="width:765px; height:24px; margin-top:4px; float:left; margin-left: 5px;">
    	<button title="Buscar informações..." style="width: 80px;" onclick="btPesquisarOnClick()" id="btPesquisar" class="toolButton"><img src="imagens/form-btPesquisar16.gif" height="16" width="16">Pesquisar</button>
    	<button title="Visualizar detalhe do registro selecionado" style="width: 120px;" onclick="viewDespesa();" id="btVisualizar" class="toolButton"><img src="imagens/visualizar16.gif" height="16" width="16">Visualizar detalhe</button>
    </div>
	<div class="d1-line" id="line4">
		<div style="width: 764px; margin-left: 5px;" class="element">
			<label class="caption" id="labelResultado" style="font-weight:bold; color: #444;">Resultado da Pesquisa</label>
			<div id="divGridDespesa" style="float:left; width: 764px; height:305px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
		</div>
	</div>
	<div id="rodape" class="rodape" style="margin-top: 5px; float: left;"></div>
  </div>
</div>

</body>
</html>