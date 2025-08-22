<%@ page contentType="text/html; charset=iso-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<title>Painel de Controle - Transparência Municipal</title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@page import="java.util.*"%>
<%
	com.tivic.manager.egov.TransparenciaServices.registraAcesso(request);
%>
<script language="javascript" src="/sol/js/scriptaculous/prototype.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/builder.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/effects.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/dragdrop.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/controls.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/slider.js"></script>
<script language="javascript" src="/sol/js/masks.js"></script>
<script language="javascript" src="/sol/js/masks2.0.js"></script>
<script language="javascript" src="/sol/js/util.js"></script>
<script language="javascript" src="/sol/js/validacao.js"></script>
<script language="javascript" src="/sol/js/ajax.js"></script>
<script language="javascript" src="/sol/js/shortcut.js"></script>
<script language="javascript" src="/sol/js/form.js"></script>
<script language="javascript" src="/sol/js/janela2.0.js"></script>
<script language="javascript" src="/sol/js/grid2.0.js"></script>
<script language="javascript" src="/sol/js/toolbar.js"></script>
<script language="javascript" src="/sol/js/flatbutton.js"></script>
<script language="javascript" src="/sol/js/report.js"></script>
<script language="javascript" src="/sol/js/calendario.js"></script>
<link href="/sol/css/form.css" rel="stylesheet" type="text/css" />
<link href="/sol/css/grid2.0.css" rel="stylesheet" type="text/css" />
<link href="/sol/css/janela2.0.css" rel="stylesheet" type="text/css" />
<link href="/sol/css/report.css" rel="stylesheet" type="text/css" />
<link href="/sol/css/toolbar.css" rel="stylesheet" type="text/css" />
<link href="/sol/css/calendario.css" rel="stylesheet" type="text/css" />
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
var gridArquivos = null;
var columnsArquivo = [{label:'Situação', reference: 'img_situacao', type: GridOne._IMAGE},	
                      {label:'Tipo de Arquivo', reference: 'nm_tipo_arquivo'},
					  {label:'Criado em', reference: 'dt_criacao', type: GridOne._DATE},
                      {label:'Movimento do dia', reference: 'dt_etapa', type: GridOne._DATE},
                      {label:'Enviado em', reference: 'dt_arquivamento', type: GridOne._DATETIME},
                      {label:'por', reference: 'nm_login'},
                      {label:'Publicado em', reference: 'dt_publicacao', type: GridOne._DATETIME},
                      {label:'Conteúdo', reference: 'ds_log'}];

function init()	{
	try	{
		$('divPainelControle').style.marginLeft = Math.round((screen.availWidth-780) / 2)+'px';
	}catch(e){};
	login('');
	loadOptionsFromRsm($('cdTipoArquivo'), <%=sol.util.Jso.getStream(com.tivic.manager.egov.TransparenciaServices.getAllTipoArquivo())%>, {fieldValue: 'cd_tipo_arquivo', fieldText:'nm_tipo_arquivo'});
	btnFindArquivoOnClick('{lines:[]}');
	ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'horizontal',
								 buttons: [{id: 'btnUsuario', img: 'imagens/usuario24.png', label: 'Usuários', imagePosition: 'top', width: 50, onClick: btnUsuarioOnClick},
										   {id: 'btnAcessos', img: 'imagens/acessos24.png', label: 'Acessos', imagePosition: 'top', width: 50, onClick: function() { btnAcessosOnClick(null); }},
										   {separator: 'horizontal'},
										   {id: 'btnSendReceita', img: 'imagens/receita24.png', label: 'Enviar Receita', imagePosition: 'top', width: 70, onClick: function() { btnSendReceitaOnClick(false); }},
										   {id: 'btnSendReceita', img: 'imagens/republicar_despesa24.png', label: 'Reenviar', imagePosition: 'top', width: 50, onClick: function() { btnSendReceitaOnClick(true)}},
										   {separator: 'horizontal'},
										   {id: 'btnSendDespesa', img: 'imagens/despesa24.png', label: 'Enviar Despesa', imagePosition: 'top', width: 80, onClick:  function(){btnSendDespesaOnClick(false)}},
										   {id: 'btnResendDespesa', img: 'imagens/republicar_despesa24.png', label: 'Reenviar', imagePosition: 'top', width: 50, onClick:  function(){btnSendDespesaOnClick(true)}},
										   {separator: 'horizontal'},
										   {id: 'btnPublicar', img: 'imagens/publicar24.png', label: 'Publicar', imagePosition: 'top', width: 50, onClick: btnPublicarArquivoOnClick},
										   {id: 'btnProtocolo', img: 'imagens/protocolo24.png', label: 'Protocolo', imagePosition: 'top', width: 50, onClick: btnProtocoloOnClick},
										   {id: 'btnDeleteArquivo', img: 'imagens/excluir24.png', label: 'Excluir', imagePosition: 'top', width: 50, onClick: btnDeleteArquivoOnClick},
										   {id: 'btnConteudoArquivo', img: 'imagens/registro24.png', label: 'Registros', imagePosition: 'top', width: 50, onClick: btnDetalheOnClick},
										   {separator: 'horizontal'},
										   {id: 'btnFindArquivo', img: 'imagens/pesquisar24.png', label: 'Listar Arquivos', imagePosition: 'top', width: 80, onClick: btnFindArquivoOnClick}]});
	$('dtInicial').value = formatDateTime(new Date());
	$('dtFinal').value   = formatDateTime(new Date());
}

function login(msg) {
	closeAllWindow();
	
	createWindow('jLogin', {caption: 'Login', noMinimizeButton: true, noMaximizeButton: true, noCloseButton: true, width: 250, height: 180, modal:true, 
							contentUrl: 'login.jsp'+(msg!=null? '?msg='+msg : '')});
}

function miAlterarSenhaOnClick()	{
	createWindow('jAlterarSenha', {caption: 'Alterando senha', width: 290, height: 115, modal:true, contentUrl: '../seg/alterar_senha.jsp'});
}

/*
 * Envia arquivo de despesa  
 */
var republicacao = false;
function btnSendDespesaOnClick(lgRepublicar)	{
	if($('cdUsuario').value <= 0)	{
		alert('A informação do usuário não foi localizada! Você entrar novamente nessa janela!');
		return;
	}
	republicacao = lgRepublicar;
	createWindow("jLoadFile", {caption:"Arquivo de Despesa", width: 410, height: 90, modal: true,
							   contentUrl: "../load_file.jsp?idSession=fileDespesa" + 
										   "&returnFunction=parent.btnSendDespesaAux"});
}
/*
 * Esta função é chamada pelo botão carregar da janela de upload, na primeira chamada o arquivo é carregado na sessão e
 * em seguida é chamada o método passando o arquivo  
 */
function btnSendDespesaAux(content)	{
	if(content==null)	{
		var objects = 'fileDespesa=byte[]';
		var execute = 'fileDespesa=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const fileDespesa:String);';
		setTimeout(function()	{
					getPage('POST', 'btnSendDespesaAux', 
						   '../methodcaller?className=com.tivic.manager.egov.TransparenciaServices'+
						   '&objects='+objects+
						   (execute!=''?'&execute=':'')+execute+
						   '&method=loadDespesa(*fileDespesa:byte[],cdUsuario:int,const '+republicacao+':boolean)', [$('cdUsuario')], true)}, 10);
	}
	else	{
		var ret = processResult(content, 'Processado com sucesso!');
		var register = {};
		register['NM_TIPO_ARQUIVO'] = 'DESPESA';
		register['DT_ARQUIVAMENTO'] = '<%=com.tivic.manager.util.Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy HH:mm")%>';
		register['URL_LOGO'] 	    = 'imagens/brasao.jpg';
		register['NM_LOGIN'] 	    = ret.objects['usuario'];
		register['CL_SITUACAO']     = ret.code > 0 ? 'Processado' : 'ERRO';
		rsmLog = {lines: [{DS_LOG: ret.message}]};
		if(ret.code>=1)	{
			register['DT_CRIACAO'] = ret.objects['DT_CRIACAO'];
			register['DT_ETAPA']   = ret.objects['DT_MOVIMENTO'].split(' ')[0];
			rsmLog = ret.objects['rsmLog'];
		}
		// Substituindo band		
		var band = $('titleBand').cloneNode(true);
		var fields = ['nm_login', 'url_logo', 'dt_criacao', 'dt_arquivamento', 'dt_etapa', 'cl_situacao', 'nm_tipo_arquivo'];
		for (var i=0; fields!=null && i<fields.length; i++) {
			regExp = new RegExp('#'+fields[i].toUpperCase(), 'g');
			band.innerHTML = band.innerHTML.replace(regExp, register[fields[i].toUpperCase()]==null ? '' : register[fields[i].toUpperCase()]);
		}
		//
		printProtocolo(band, rsmLog);
	}
}

/*
 * Esta função exibe a janela que fará o upload do arquivo de receita  
 */
function btnSendReceitaOnClick(lgRepublicar)	{
	if($('cdUsuario').value <= 0)	{
		alert('A informação do usuário não foi localizada! Você entrar novamente nessa janela!');
		return;
		
		
	}
	republicacao = lgRepublicar;
	createWindow("jLoadFile", {caption:"Arquivo de Receita", width: 410, height: 90, modal: true,
							   contentUrl: "../load_file.jsp?idSession=fileReceita" + 
										   "&returnFunction=parent.btnSendReceitaAux"});
}
/*
 * Esta função é chamada pelo botão carregar da janela de upload, na primeira chamada o arquivo é carregado na sessão e
 * em seguida é chamada o método passando o arquivo  
 */
function btnSendReceitaAux(content)	{
	if(content==null)	{
		var objects = 'fileReceita=byte[]';
		var execute = 'fileReceita=sol.util.RequestUtilities.getParameterFromSession(session:javax.servlet.http.HttpSession, const fileReceita:String);';
		setTimeout(function()	{
					getPage('POST', 'btnSendReceitaAux', 
						   '../methodcaller?className=com.tivic.manager.egov.TransparenciaServices'+
						   '&objects='+objects+
						   (execute!=''?'&execute=':'')+execute+
						   '&method=loadReceita(*fileReceita:byte[],cdUsuario:int,const '+republicacao+':boolean)', [$('cdUsuario')], true)}, 10);
	}
	else	{
		var ret = processResult(content, 'Processado com sucesso!');
		var register = {};
		register['NM_TIPO_ARQUIVO'] = 'RECEITA';
		register['DT_ARQUIVAMENTO'] = '<%=com.tivic.manager.util.Util.formatDateTime(new GregorianCalendar(), "dd/MM/yyyy HH:mm")%>';
		register['URL_LOGO'] 	    = 'imagens/brasao.jpg';
		register['NM_LOGIN'] 	    = ret.objects['usuario'];
		register['CL_SITUACAO']     = ret.code > 0 ? 'Processado com sucesso' : 'Erro no processamento';
		rsmLog = {lines: [{DS_LOG: ret.message}]};
		if(ret.code>=1)	{
			register['DT_CRIACAO'] = ret.objects['DT_CRIACAO'];
			register['DT_ETAPA']   = ret.objects['DT_MOVIMENTO'].split(' ')[0];
			rsmLog = ret.objects['rsmLog'];
		}
		// Substituindo band		
		var band = $('titleBand').cloneNode(true);
		var fields = ['nm_login', 'url_logo', 'dt_criacao', 'dt_arquivamento', 'dt_etapa', 'cl_situacao', 'nm_tipo_arquivo'];
		for (var i=0; fields!=null && i<fields.length; i++) {
			regExp = new RegExp('#'+fields[i].toUpperCase(), 'g');
			band.innerHTML = band.innerHTML.replace(regExp, register[fields[i].toUpperCase()]==null ? '' : register[fields[i].toUpperCase()]);
		}
		//
		printProtocolo(band, rsmLog);
	}
}

function btnProtocoloOnClick()	{
	if(!gridArquivos || !gridArquivos.getSelectedRowRegister())	{
		alert('Selecione o arquivo do qual deseja imprimir o protocolo!');
		return;
	}
	var band = $('titleBand').cloneNode(true);
	var register = gridArquivos.getSelectedRowRegister();
	register['CL_SITUACAO']   = register['ST_ARQUIVO']==0 ? 'Pendente' : 'Publicado';
	register['URL_LOGO'] 	  = 'imagens/brasao.jpg';
	var fields = ['nm_login', 'url_logo', 'dt_criacao', 'dt_arquivamento', 'dt_etapa', 'cl_situacao', 'nm_tipo_arquivo'];
	for (var i=0; fields!=null && i<fields.length; i++) {
		regExp = new RegExp('#'+fields[i].toUpperCase(), 'g');
		band.innerHTML = band.innerHTML.replace(regExp, register[fields[i].toUpperCase()]==null ? '' : register[fields[i].toUpperCase()]);
	}
	//
	printProtocolo(band, register['RSM_LOG']);
}

function printProtocolo(band, rsmLog)	{
	ReportOne.create('jProtocolo', {width: 700, height: 430, caption: 'Protocolo de envio de arquivo',
									resultset: rsmLog, pageHeaderBand: {contentModel: band},
									detailBand: {columns: [{reference:'ds_log', label: 'Retorno do Processamento'}], displayColumnName: true, displaySummary: true},
									pageFooterBand: {defaultText: 'TIVIC - Tecnologia e Informação', defaultInfo: 'Pág. #p de #P'},
									orientation: 'portraid', paperType: 'A4', tableLayout: 'fixed'});
}

function btnFindArquivoOnClick(content) {
	if(content==null)	{
		var objetos = 'crt=java.util.ArrayList();',
			execute = '';
		// Tipo de Arquivo
		if($('cdTipoArquivo').value>0)	{
			objetos += 'tipoArquivo=sol.dao.ItemComparator(const A.cd_tipo_arquivo:String,cdTipoArquivo:String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*tipoArquivo:Object);';
		}
		// Situação de Arquivo
		if($('stArquivo').value>=0)	{
			objetos += 'tpEtapa=sol.dao.ItemComparator(const A.st_arquivo:String,stArquivo:String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
			execute += 'crt.add(*tpEtapa:Object);';
		}
		// Inicial
		/*
		objetos += 'itemData=sol.dao.ItemComparator(const A.dt_etapa:String,dtEtapa:String,const '+_EQUAL+':int,const '+_TIMESTAMP+':int);';
		execute += 'crt.add(*itemData:Object);';
		*/
		createTempbox("jMsg", {width: 165, height: 50, message: "Aguarde... pesquisando!", tempboxType: "LOADING", time: 0});
		
		var fields = [$('cdTipoArquivo'),$('stArquivo'),$('tpData'),$('dtInicial'),$('dtFinal')];
		// BUSCANDO
		setTimeout(function()	{
				   getPage('POST', 'btnFindArquivoOnClick', 
						   '../methodcaller?className=com.tivic.manager.egov.TransparenciaServices'+
						   '&objects='+objetos+
						   (execute!=''?'&execute=':'')+execute+
						   '&method=findArquivo(cdTipoArquivo:int,stArquivo:int,tpData:int,dtInicial:GregorianCalendar,dtFinal:GregorianCalendar)', fields)}, 10);
	}
	else {	// retorno
		closeWindow('jMsg');
		var rsmArquivos = null;
		try {rsmArquivos = eval('(' + content + ')')} catch(e) {}
		gridArquivos = GridOne.create('gridArquivos', {columns: columnsArquivo,
					     resultset :rsmArquivos, 
					     onDoubleClick: function(){btnDetalheOnClick(null)},
					     plotPlace : $('divGridArquivos'),
						 onProcessRegister: function(reg) {
						 		reg['IMG_SITUACAO'] = reg['ST_ARQUIVO']==1 ? 'imagens/publicado16.png' : 'imagens/pendente16.png';
						 }});
	}
}

function btnUsuarioOnClick()	{
	createWindow('jUsuario', {caption: 'Usuário', width: 429, height: 260, modal:true, contentUrl: 'usuario.jsp'});
}

function btnDeleteArquivoOnClickAux(content){
    var executionDescription = formatDescriptionDelete("Arquivo", gridArquivos.getSelectedRowRegister()['CD_ARQUIVO'], '');
    getPage("GET", "btnDeleteArquivoOnClick", 
            "../methodcaller?className=com.tivic.manager.egov.TransparenciaServices"+
            "&method=deleteArquivo(const "+gridArquivos.getSelectedRowRegister()['CD_ARQUIVO']+":int):int", null, null, null, executionDescription);
}

function btnDeleteArquivoOnClick(content){
    if(content==null){
		if(!gridArquivos || !gridArquivos.getSelectedRowRegister())	{
			alert('Selecione o arquivo que deseja excluir!');
			return;
		}
		if(gridArquivos.getSelectedRowRegister()['ST_ARQUIVO']==1)	{
			alert('Arquivo já publicado! Impossível exclusão.');
			return;
		}
        createConfirmbox("dialog", {caption: "Exclusão de Arquivo", width: 300, height: 75, 
                                    message: "Você tem certeza que deseja excluir este arquivo?", boxType: "QUESTION",
                                    positiveAction: function() {setTimeout("btnDeleteArquivoOnClickAux()", 10)}});
    }
    else{
        if(parseInt(content)==1){
			gridArquivos.removeSelectedRow();
            createTempbox("jTemp", {width: 300, height: 75, message: "Registro excluído com sucesso!", time: 3000});
        }
        else
            createTempbox("jTemp", {width: 300, height: 75, message: "Não foi possível excluir este registro!", time: 5000});
    }	
}

function btnPublicarArquivoOnClick(content) {
	if (content == null) {
		if(!gridArquivos || !gridArquivos.getSelectedRowRegister())	{
			alert('Selecione o arquivo que deseja publicar!');
			return;
		}
		if(gridArquivos.getSelectedRowRegister()['ST_ARQUIVO']!=0)	{
			alert('Arquivo já publicado!');
			return;
		}
		getPage("GET", "btnPublicarArquivoOnClick", '../methodcaller?className=com.tivic.manager.egov.TransparenciaServices'+
				'&method=publicarArquivo(const ' + gridArquivos.getSelectedRowRegister()['CD_ARQUIVO'] + ':int)', null, true, null, null);
	}
	else {
		if(parseInt(content, 10) > 0)	{
			gridArquivos.getSelectedRow().register['ST_ARQUIVO'] = 1;
			gridArquivos.updateSelectedRow();
		}
	}
}

var columnsDespesa = [{label:'Tipo', reference: 'CL_ETAPA', columnWidth: '50px'}, 
    				  {label:'Nº Processo', reference: 'NR_PROCESSO'}, 
					  {label:'Data', reference: 'DT_ETAPA', type: GridOne._DATE},
					  {label:'Valor', reference: 'VL_DESPESA', type: GridOne._CURRENCY},
					  {label:'Unidade Orçamentária', reference: 'NM_UNIDADE_ORCAMENTARIA'},
					  {label:'Favorecido (Credor)', reference: 'NM_CREDOR'},							   
    				  {label:'Nº Etapa', reference: 'NR_ETAPA'},
    				  {label:'Processo Licitatório', reference: 'DS_MODALIDADE'},
    				  {label:'Nº Proc. Licitatório', reference: 'NR_COMPRA'}];
var columnsReceita = [{label:'Data', reference: 'DT_ETAPA', type: GridOne._DATE},
				      {label:'Modalidade', reference: 'CL_MODALIDADE'}, 
					  {label:'Tipo', reference: 'CL_TIPO'},
					  {label:'Unidade Gestora', reference: 'NM_UNIDADE_GESTORA'},							   
					  {label:'Valor', reference: 'VL_RECEITA',  type: GridOne._CURRENCY},
    				  {label:'Natureza da Receita', reference: 'NM_NATUREZA_RECEITA'}];

var gridDetalhe;
function btnDetalheOnClick(content) {
	if(content==null)	{
		if(!gridArquivos.getSelectedRowRegister())	{
			alert('Nenhum registro encontrado!');
			return;
		}
		var objetos = 'crt=java.util.ArrayList();',
			execute = '';
		objetos += 'itemArquivo=sol.dao.ItemComparator(const A.cd_arquivo:String,cdArquivo:String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
		execute += 'crt.add(*itemArquivo:Object);';
		var method = 'findDespesa';
		if(gridArquivos.getSelectedRowRegister()['NM_TIPO_ARQUIVO'].indexOf('RECEITA')>=0)
			method = 'findReceita';
		// BUSCANDO
		setTimeout(function()	{
				   getPage('POST', 'btnDetalheOnClick', 
						   '../methodcaller?className=com.tivic.manager.egov.TransparenciaServices'+
						   '&objects='+objetos+
						   (execute!=''?'&execute=':'')+execute+
						   '&cdArquivo='+gridArquivos.getSelectedRowRegister()['CD_ARQUIVO']+
						   '&method='+method+'(*crt:java.util.ArrayList)', null, true)}, 10);
	}
	else {	// retorno
		rsmRegistro = eval("("+content+")");
		
		FormFactory.createFormWindow('jDetalheCategoria', {caption: "Detalhando despesas/receitas", width: 770, height: 430, modal: true, top: 5, noDrag: true,
								  id: 'detalheCategoria', unitSize: '%',
								  lines: [[{id:'divGridDetalhe', width:100, height: 368, type: 'grid'}],
							  			  [{type: 'panel', width: 85, height: 22, label: 'Clique duas vezes sobre o registro pra visualizar detalhes'},
										    {id:'btnCancel', type:'button', height: 22, image: '/sol/imagens/cancel_13.gif', label:'Fechar', width:15, 
										     onClick: function(){
													closeWindow('jDetalheCategoria');
										     }
										    }]]});
		var columns = columnsDespesa;
		var fctDetalhe = viewDespesa; 
		if(gridArquivos.getSelectedRowRegister()['NM_TIPO_ARQUIVO'].indexOf('RECEITA')>=0)	{
			columns    = columnsReceita;
			fctDetalhe = viewReceita;
		}
		gridDetalhe = GridOne.create('gridDetalhe', {columns: columns, resultset: rsmRegistro, onDoubleClick: fctDetalhe,
		                               onProcessRegister: function(reg)	{
											reg['CL_MODALIDADE'] = ['Lançamento','Recebimento'][reg['TP_MODALIDADE']];
											reg['CL_TIPO']       = ['Orçamentária','Não Orçamentária'][reg['TP_RECEITA']];
											reg['CL_ETAPA'] 	 = ['Empenho','Liquidação','Pagamento'][reg['TP_ETAPA']];
		                               },  
								       plotPlace: document.getElementById('divGridDetalhe')});
	}
}

var gridAcessos;
function btnAcessosOnClick(content) {
	if(content==null)	{
		var objetos = 'crt=java.util.ArrayList();',
			execute = '';
		// objetos += 'itemArquivo=sol.dao.ItemComparator(const A.cd_arquivo:String,cdArquivo:String,const '+_EQUAL+':int,const '+_INTEGER+':int);';
		// execute += 'crt.add(*itemArquivo:Object);';
		// BUSCANDO
		setTimeout(function()	{
				   getPage('POST', 'btnAcessosOnClick', 
						   '../methodcaller?className=com.tivic.manager.egov.TransparenciaServices'+
						   '&objects='+objetos+
						   // (execute!=''?'&execute=':'')+execute+
						   '&method=getAcessos(*crt:java.util.ArrayList)', null, true)}, 10);
	}
	else {	// retorno
		rsmRegistro = eval("("+content+")");
		
		FormFactory.createFormWindow('jAcessos', {caption: "Relatório de Acessos", width: 770, height: 430, modal: true,
								  id: 'detalheCategoria', unitSize: '%',
								  lines: [[{id:'divGridAcessos', width:100, height: 368, type: 'grid'}],
							  			  [{type: 'panel', width: 85, height: 22, label: 'Clique duas vezes sobre o registro pra visualizar detalhes'},
										    {id:'btnCancel', type:'button', height: 22, image: '/sol/imagens/cancel_13.gif', label:'Fechar', width:15, 
										     onClick: function(){
													closeWindow('jAcessos');
										     }
										    }]]});
		var columns = [{label:'Data', reference: 'DT_ACESSO', type: GridOne._DATE}, 
					   {label:'Acessos únicos', reference: 'QT_ACESSO'}];
		gridAcessos = GridOne.create('divGridAcessos', {columns: columns, resultset: rsmRegistro, onDoubleClick: null,
								       plotPlace: document.getElementById('divGridAcessos')});
	}
}

function viewDespesa(el)	{
	FormFactory.createFormWindow('jDetalhe', 
	                     {caption: "Detalhe da Despesa", width: 500, height: 378, unitSize: '%',
						  id: 'detalheDespesa', loadForm: true, noDrag: true, modal: true,
						  lines: [[{id:'nmUnidadeOrcamentaria', readonly:true, type:'text', label:'Unidade Orçamentária', width:100}],
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
								  ]],
						  focusField:'cdConta'});
	$('vlDespesa').style.textAlign = 'right';
	var reg = gridDetalhe.getSelectedRowRegister();
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
	var reg = gridDetalhe.getSelectedRowRegister();
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
<div style="width: 777px;" id="divPainelControle" class="d1-form">
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
			<label class="caption" style="font-size: 18px; height: 30px; color:#364C87; font-weight: bold;">Painel de Controle</label>
		</div>
	</div>
	 <div class="d1-line" id="line0">
     		<div class="d1-toolBar" id="toolBar" style="width:765px; height:70px; margin-top:4px; float:left; margin-left: 5px;"></div>
     </div>
	 <div class="d1-line" id="line0">
		<div class="element" style="width:190px; margin-left: 5px;">
			<label class="caption" title="Tipo de Arquivo" style="color: #444;">Tipo de Arquivo</label>
			<select style="width:187px; height: 24px;" type="text" name="cdTipoArquivo" id="cdTipoArquivo" class="select1">
				<option value="-1">Todos</option>
			</select>
		</div>
		<div class="element" style="width:190px;">
			<label class="caption" title="Tipo de Arquivo" style="color: #444;">Situação do Arquivo</label>
			<select style="width:187px; height: 24px;" type="text" name="stArquivo" id="stArquivo" class="select1">
				<option value="-1">Todas</option>
				<option value="0">Pendente</option>
				<option value="1">Publicado</option>
			</select>
		</div>
		<div class="element" style="width:150px;">
			<label class="caption" style="color: #444;">Tipo de Data</label>
			<select style="width:147px; height: 24px;" name="tpData" id="tpData" class="select1">
				<option value="-1">Nenhuma</option>
				<option value="0">Data de Criação</option>
				<option selected="selected" value="1">Data de Envio</option>
				<option value="2">Data de Publicação</option>
				<option value="3">Data do Movimento</option>
			</select>
		</div>
	    <div style="width: 93px; padding:2px 0 0 0" class="element">
            <label class="caption" for="dtInicial">Data Inicial</label>
            <input style="width: 90px;" mask="##/##/####" maxlength="10" class="field1" datatype="DATE" id="dtInicial" name="dtInicial" type="text"/>
        </div>
	    <div style="width: 20px; padding:2px 0 0 0" class="element">
            <label class="caption" for="dtFinal">&nbsp</label>
	        <img onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" reference="dtInicial" src="imagens/calendar24.png" style="cursor: pointer;"/>
        </div>
	    <div style="width: 93px; padding:2px 0 0 0; margin-left: 5px;" class="element">
            <label class="caption" for="dtFinal">Data Final</label>
            <input style="width: 90px;" mask="##/##/####" maxlength="10" class="field1" datatype="DATE" id="dtFinal" name="dtFinal" type="text"/>
        </div>
	    <div style="width: 20px; padding:2px 0 0 0" class="element">
            <label class="caption" for="dtFinal">&nbsp</label>
	        <img onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Br')" reference="dtFinal" src="imagens/calendar24.png" style="cursor: pointer;"/>
        </div>
	</div>
	<div class="d1-line" id="line4">
		<div style="width: 764px; margin-left: 5px;" class="element">
			<label class="caption" id="labelResultado" style="font-weight:bold; color: #444;">Lista de Arquivos</label>
			<div id="divGridArquivos" style="float:left; width: 764px; height:245px; border:1px solid #000; background-color:#FFF;">&nbsp;</div>
		</div>
	</div>
	<div id="rodape" class="rodape" style="margin-top: 5px; float: left;"></div>
  </div>
</div>

	<div id="titleBand" style="width:100%; display: none;">
	  <div style="width:100%; float:left; border-bottom:1px solid #000; border-top:1px solid #000;">
		<div style="height:50px; border-bottom:1px solid #000;">
		    <img id="imgLogo" style="height:40px; margin:5px; float:left" src="#URL_LOGO"/>
			<div style="height:50px; border-left:1px solid #000000; float:left; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; font-weight:bold">
				&nbsp;Prefeitura Municipal de Vitória da Conquista<br/>
				&nbsp;Secretaria Municipal de Finanças e Execução Orçamentária<br/>
				&nbsp;Transparência Municipal (Lei Complementar 131/2009)<br/>
		  		&nbsp;			
		  	</div>
  		</div>
		<div style="height:25px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:10px;">
			<div style="width:90px; height:25px; float:left;">
			  	&nbsp;Tipo de Arquivo<br/>
		  		&nbsp;&nbsp;#NM_TIPO_ARQUIVO
		  	</div>
			<div style="width:110px; height:25px; float:left; border-left:1px solid #000;">
				&nbsp;Criado em<br/>
		  		&nbsp;&nbsp;#DT_CRIACAO
		  	</div>
			<div style="width:110px; height:25px; float:left; border-left:1px solid #000; overflow: hidden;">
				&nbsp;Enviado em<br/>
		  		&nbsp;&nbsp;#DT_ARQUIVAMENTO
		  	</div>
			<div style="width:95px; height:25px; float:left; border-left:1px solid #000; overflow: hidden;">
				&nbsp;Movimento do dia<br/>
		  		&nbsp;&nbsp;#DT_ETAPA
		  	</div>
			<div style="width:100px; height:25px; float:left; border-left:1px solid #000; overflow: hidden;">
				&nbsp;Usuário<br/>
    			&nbsp;&nbsp;#NM_LOGIN
    		</div>
			<div style="width:110px; height:25px; float:left; border-left:1px solid #000; overflow: hidden;">
			  	&nbsp;Situação<br/>
		  		&nbsp;&nbsp;#CL_SITUACAO
		  	</div>
  		</div>
	</div>
	
</body>
</html>