<%@ page contentType="text/html; charset=iso-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<title>Transparência - Receitas</title>
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
<%
	if(sol.util.RequestUtilities.getAsInteger(request, "arquivo", 0)==1)
		response.sendRedirect("admin.jsp");
	com.tivic.manager.egov.TransparenciaServices.registraAcesso(request);
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
var rsmReceita = null;
var gridReceita;
var columnsReceita = [{label:'Data', reference: 'DT_ETAPA', type: GridOne._DATE},
				      {label:'Modalidade', reference: 'CL_MODALIDADE'}, 
					  {label:'Tipo', reference: 'CL_TIPO'},
					  {label:'Unidade Gestora', reference: 'NM_UNIDADE_GESTORA'},							   
					  {label:'Valor', reference: 'VL_RECEITA',  type: GridOne._CURRENCY},
    				  {label:'Natureza da Receita', reference: 'NM_NATUREZA_RECEITA'}];

function init()	{
	try	{
		$('divReceita').style.marginLeft = Math.round((screen.availWidth-780) / 2)+'px';
	}catch(e){};
	addShortcut('ctrl+p', btPesquisarOnClick);
	enableTabEmulation();
	
	loadOptionsFromRsm($('dtEtapa'), <%=sol.util.Jso.getStream(com.tivic.manager.egov.TransparenciaServices.getDates(0))%>, 
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
		// Tipo de Receita
		if($('tpReceita').value>=0)	{
			objetos += 'tpReceita=sol.dao.ItemComparator(const A.tp_receita:String,tpReceita:String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*tpReceita:Object);';
		}
		// Modalidade
		if($('tpModalidade').value != -1)	{
			objetos += 'itemModalidade=sol.dao.ItemComparator(const A.tp_modalidade:String,tpModalidade:String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*itemModalidade:Object);';
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
		var fields = [$('tpModalidade'),$('tpReceita'),$('dtEtapa')];
		// BUSCANDO
		setTimeout(function()	{
				   getPage('POST', 'btPesquisarOnClick', 
						   '../methodcaller?className=com.tivic.manager.egov.TransparenciaServices'+
						   '&objects='+objetos+
						   (execute!=''?'&execute=':'')+execute+
						   '&method=findReceita(*crt:java.util.ArrayList)', fields)}, 10);
	}
	else {	// retorno
		formMensagem.close();
		rsmReceita = eval("("+content+")");
		var qt = rsmReceita.lines.length;
		$('labelResultado').innerHTML = (qt==0)?'Nenhuma receita encontrada':(qt==1)?'1 receita encontrada':qt+' receitas encontradas';
		createGrid(rsmReceita);
	}
}

function createGrid(rsm)	{
	var modalidade = ['Lançamento','Recebimento'];
	var tipo = ['Orçamentária','Não Orçamentária'];
	gridReceita = GridOne.create('gridReceita', {columns: columnsReceita, resultset: rsm, plotPlace: $('divGridReceita'),
											     onDoubleClick: viewReceita,
												 columnSeparator: true, lineSeparator: false, strippedLines: true, noSelectOnCreate: false,
												 onProcessRegister: function(reg){
														reg['CL_MODALIDADE'] = modalidade[reg['TP_MODALIDADE']];
														reg['CL_TIPO']       = tipo[reg['TP_RECEITA']];
														reg['CL_IMG']   = 'imagens/detalhe16.gif';
												 }});

}

function viewReceita()	{
	FormFactory.createFormWindow('jDetalhe', 
	                     {caption: "Detalhe da Receita", width: 500, height: 257, unitSize: '%',
						  id: 'detalheReceita', loadForm: true, noDrag: true, modal: true,
						  lines: [[{id:'nmUnidadeGestora', reference: 'nm_unidade_gestora', readonly:true, type:'text', label:'Unidade Gestora', width:100}],
						  		  [{id:'dtPublicacao', reference: 'dt_publicacao', readonly:true, type:'text', label:'Data de Publicação', width:20},
						  		   {id:'dsModalidade', reference: 'cl_modalidade', readonly:true, type:'text', label:'Modalidade', width:30},
						  		   {id:'dsTipo', reference: 'cl_tipo', readonly:true, type:'text', label:'Tipo', width:30},
						  		   {id:'dtEtapaForm', reference: 'dt_etapa', readonly:true, type:'text', label:'Data', width:20}],
						  		  [{id:'txtDescricao', reference: 'txt_descricao', disabled:true, type:'textarea', height: 70, label:'Descrição da Receita', width:100}],
						  		  [{id:'nmNaturezaReceita', reference: 'nm_natureza_receita', readonly:true, type:'text', label:'Natureza da Receita', width:100}],
						  		  [{id:'vlReceita', reference: 'vl_receita', readonly:true, type:'text', label:'Valor', width:20, value: '0,00'},
						  		   {id:'dsDestinacao', reference: 'ds_destinacao', readonly:true, type:'text', label:'Destinação', width:80}],
								  [{type: 'space', width: 80},
								   {id:'btnFechar', type:'button', label:'Fechar', width:20, height:19, image: '/sol/imagens/cancel_13.gif', 
								   	onClick: function(){
								   		closeWindow('jDetalhe'); 
								   	}
								   }
								  ]],
						  focusField:'cdConta'});
	$('vlReceita').style.textAlign = 'right';
	var reg = gridReceita.getSelectedRowRegister();
	$('nmUnidadeGestora').value  = reg['NM_UNIDADE_GESTORA'];
	$('dtPublicacao').value 	 = reg['DT_PUBLICACAO'].split(' ')[0];
	$('dsModalidade').value      = reg['CL_MODALIDADE'];
	$('dsTipo').value            = reg['CL_TIPO'];						  
	$('dtEtapaForm').value       = reg['DT_ETAPA'].split(' ')[0];						  
	$('txtDescricao').value      = reg['TXT_DESCRICAO'];
	$('dsDestinacao').value      = reg['DS_DESTINACAO'];
	$('vlReceita').value 		 = formatCurrency(reg['VL_RECEITA']);
	$('nmNaturezaReceita').value = reg['NM_NATUREZA_RECEITA'];
}

</script>
</head>
<body class="body" onload="init()" margin="0" topmargin="0" marginheight="0" rightmargin="0" bgcolor="#F1F1F3">
<div style="width: 777px;" id="divReceita" class="d1-form">
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
			<label for="tpEtapa" class="caption" style="font-size: 18px; height: 30px; color:#364C87; font-weight: bold;">Receitas</label>
		</div>
	</div>
	<div class="d1-line" id="line0">
		<div class="element" style="width:230px; margin-left: 5px;">
			<label for="tpEtapa" class="caption" title="Tipo de Receita" style="color:#444;">Tipo de Receita</label>
			<select style="width:227px" type="text" name="tpReceita" id="tpReceita" class="select1">
				<option value="-1">Todos</option>
				<option value="0">Orçamentária</option>
				<option value="1">Não Orçamentária</option>
			</select>
		</div>
		<div class="element" style="width:230px;">
			<label class="caption" style="color:#444;">Modalidade</label>
			<select style="width:227px" type="text" name="" id="tpModalidade" class="select1" >
				<option value="-1">Todas</option>
				<option value="0">Lançamento</option>
				<option value="1">Recebimento</option>
			</select>
		</div>
		<div class="element" style="width:311px;">
			<label for="stConta" class="caption" style="color:#444;">Data da Etapa (Empenho/Liquidação/Pagamento)</label>
			<select style="width:306px" name="dtEtapa" id="dtEtapa" class="select1">
			</select>
		</div>
	 </div>
    <div class="d1-toolBar" id="toolBarReceita" style="width:765px; height:24px; margin-top:4px; float:left; margin-left: 5px;">
    	<button title="Buscar informações..." style="width: 80px;" onclick="btPesquisarOnClick()" id="btPesquisar" class="toolButton"><img src="imagens/form-btPesquisar16.gif" height="16" width="16">Pesquisar</button>
    	<button title="Visualizar detalhe do registro selecionado" style="width: 120px;" onclick="viewReceita();" id="btVisualizar" class="toolButton"><img src="imagens/visualizar16.gif" height="16" width="16">Visualizar detalhe</button>
    </div>
	<div class="d1-line" id="line4">
		<div style="width: 764px; margin-left: 5px;" class="element">
			<label class="caption" id="labelResultado" style="font-weight:bold; color: #444;">Resultado da Pesquisa</label>
			<div id="divGridReceita" style="float:left; width: 764px; height:305px; border:1px solid #000000; background-color:#FFF;">&nbsp;</div>
		</div>
	</div>
	<div id="rodape" class="rodape" style="margin-top: 5px; float: left;"></div>
  </div>
</div>
</body>
</html>