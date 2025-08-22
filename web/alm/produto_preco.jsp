<%@page contentType="text/html; charset=iso-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<%@page import="com.tivic.manager.grl.*"%>
<%
	int cdEmpresa 	= sol.util.RequestUtilities.getAsInteger(request, "cdEmpresa", 0);
	Empresa empresa = EmpresaDAO.get(cdEmpresa);
	int cdUsuario   = 0;
	int tpEntrada	= sol.util.RequestUtilities.getAsInteger(request, "tpEntrada", 0);
	if(session.getAttribute("usuario")!=null)
		cdUsuario = ((com.tivic.manager.seg.Usuario)session.getAttribute("usuario")).getCdUsuario();
	//
	int cdTipoOperacaoVarejo  = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, cdEmpresa);
	int cdTipoOperacaoAtacado = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_ATACADO", 0, cdEmpresa);
%>
<security:registerForm idForm="formProdutoPreco"/>
<security:autenticacao attributeName="usuario" action="findWindowTopStartJslogin"/>
<loader:library libraries="shortcut, grid2.0, toolbar, form, filter, calendario" compress="false"/>
<script language="javascript" src="../js/adm_report.js"></script>
<script language="javascript">
function init()	{
	if(<%=cdUsuario<=0%>)	{
		createMsgbox("jMsg", {width: 350, height: 45, msgboxType: "INFO", caption: 'ALERTA',
							  message: "Não foi encontrada a identificação do usuário, você deve sair do sistema e efetuar login novamente."});
	}
	FormFactory.createFormWindow('jProdutoPreco2', 
	                     {caption: "Pagamento Avulso", width: 902, height: 478, unitSize: '%', top: -1, left: -1, cssVersion: '2',
						  id: 'produtoPreco', loadForm: true, noTitle: true, noDrag: true,
						  hiddenFields: [{id:'cdEmpresa', reference: 'cd_empresa', value: <%=cdEmpresa%>},
				                         {id:'<%=tpEntrada==0?"qtEstoque":"vlPreco"%>', value: '0,00'}],
						  lines: [[{id:'nmEmpresa', reference: 'nm_empresa', disabled:true, type:'text', label:'Empresa:', width:55, value: '<%=empresa!=null ? empresa.getNmPessoa() : ""%>'},
				<%=tpEntrada==0?  "{id:'lgMultiplaSelecao', type:'checkbox', label:'Permite Seleção Multipla (Preço para mais de um produto)', width:45, value: ''}":
						          "{id:'lgContagemAutomatica', type:'checkbox', label:'Contagem Automática', width:22, value: ''},"+
							      "{id:'lgConfirmacaoAutomatica', type:'checkbox', label:'Confirmação Automática', width:23, value: ''}"%>],
						          [{id:'label3', type:'caption', text:'A', width:99.6, style: 'margin-top: 5px; height: 2px; border: 1px solid #FFF; text-align: center; background-color: #CCC; display: inline; float: left;'}],
						  		  [{id:'idProdutoServico', reference: 'idProdutoServico', type:'text', label:'Código de Barras', width:16, value: ''},
						  		   {id:'idReduzido', reference: 'id_reduzido', type:'text', label:'ID Reduzido', width:17, value: ''},
						  		   {id:'nrReferencia', reference: 'nr_referencia', type:'text', label:'Referência', width:17, value: ''},
						  		   {id:'txtDadoTecnico', reference: 'txt_dado_tecnico', type:'text', label:'Tamanho', width:7, value: '', disabled: true},
						  		   {id:'txtEspecificacao', reference: 'txt_especificacao', type:'text', label:'Cor', width:15, value: '', disabled: true},
						  		   {id:'nmFabricante', reference: 'nm_fabricante', viewReference: 'nm_fabricante', disabled:true, type:'text', label:'Fabricante', width:<%=tpEntrada==1?"18":"28"%>}
		  		 <%=tpEntrada==1?",{id:\'vlPreco"+cdTipoOperacaoVarejo+"\', reference: \'vl_preco_"+cdTipoOperacaoVarejo+"\', type:\'text\', label:\'Preço\', width:10, value: \'0,00\', disabled:true}":""%>],
						  		  [{id:'cdProdutoServico', reference: 'cd_produto_servico', viewReference: 'nm_produto_servico', disabled:true, type:'lookup', label:'Nome do Produto', 
						  		    width:85, findAction: function(){btnFindProdutoServicoOnClick();}},
						  		   {id:'qtAtual', type:'text', label:'Estoque (Sistema)', width:14.6, disabled: true, value: 0}],
						  		  [
			    <%=tpEntrada==0 ? "{id:\'cdTabelaPreco\', reference: \'cd_tabela_preco\', type:\'select\', label:\'Tabela de Preço\', width:30},"+
				                  "{id:\'vlPreco\', reference: \'vl_preco\', type:\'text\', label:\'Preço\', width:15, value: \'0,00\'}":""%>,
				<%=tpEntrada==1 ? "{id:\'cdBalanco\', reference: \'cd_balanco\', type:\'select\', label:\'Balanço\', width:63.6},"+
		  		          		  "{id:\'qtEstoqueBalanco\', type:\'text\', label:\'Qtd. no Balanço\', hint: 'Quantidade já informada no balanço', width:12, disabled: true, value: \'0\'},"+
				  		          "{id:\'qtContagem\', type:\'text\', label:\'Qtd. Contagem [+]\', width:12, value: \'0\'},"+
				  		          "{id:\'qtEstoque\', type:\'text\', label:\'Qtd. Final Balanço\', width:12, disabled: true, value: \'0\'}":""%>],
				  		          [{type: 'space', width: 59.7},
								   {id:'btnSavePagamento', type:'button', label:'Gravar', width: 20, height:19, image: '/sol/imagens/form-btSalvar16.gif', 
								   	onClick: function(){
										if(<%=cdUsuario<=0%>)	{
											createMsgbox("jMsg", {width: 350, height: 45, msgboxType: "INFO", caption: 'ALERTA',
																  message: "Não foi encontrada a identificação do usuário, você deve sair do sistema e efetuar login novamente."});
											return;																
										}
								   		if($('cdProdutoServico').value<=0)	{
								   			createMsgbox("jMsg", {width: 350, height: 45, msgboxType: "INFO", caption: 'ALERTA', message: "Selecione o produto!"});
								   			return;
								   		}
								   		// DEFINIÇÃO DE PREÇO
								   		<%if(tpEntrada==0)	{%>
								   		if(changeLocale('vlPreco')<=0)	{
								   			createMsgbox("jMsg", {width: 350, height: 45, msgboxType: "INFO", caption: 'ALERTA', message: "Informa um preço válido!"});
								   			$('vlPreco').focus();
								   			$('vlPreco').select();
								   			return;
								   		}
								   		if($('cdTabelaPreco') && $('cdTabelaPreco').value<=0)	{
								   			createMsgbox("jMsg", {width: 350, height: 45, msgboxType: "INFO", caption: 'ALERTA', message: "Selecione uma tabela de preço!"});
								   			$('cdTabelaPreco').focus();
								   			return;
								   		}
								   		// CONTAGEM DE ESTOQUE	
								   		<%}else{%>
								   		if(changeLocale('qtEstoque')<0)	{
								   			createMsgbox("jMsg", {width: 350, height: 45, msgboxType: "INFO", caption: 'ALERTA', message: "Informa a quantidade em estoque!"});
								   			$('qtContagem').focus();
								   			$('qtContagem').select();
								   			return;
								   		}
								   		<%}%>
								   		btnSaveProdutoPrecoOnClick(null);	
								   }},
								   {id:'btnCancelarPagamento', type:'button', label:'Fechar', width:20, height:19, image: '/sol/imagens/cancel_13.gif', 
								   	onClick: function(){
										parent.closeWindow('jProdutoPreco');
									}
								   }
								  ],
						          [{id:'label3', type:'caption', text:'<%=tpEntrada==0?"Produtos selecionados":"Histórico de produtos definidos"%>', width:99.6, style: 'margin-top: 3px; border: 1px solid #FFF; text-align: center; background-color: #CCC; display: inline; float: left;'}],
								  [{id:'divGrid', width:100, height: 250, type: 'grid'}]],
						  focusField:'idProdutoServico'});
	$('qtAtual').style.textAlign = 'right';
	if($('vlPreco'))						  
		$('vlPreco').style.textAlign   = 'right';
	if($('vlPreco<%=cdTipoOperacaoVarejo%>'))						  
		$('vlPreco<%=cdTipoOperacaoVarejo%>').style.textAlign   = 'right';
	if($('qtContagem'))	{
		$('qtEstoqueBalanco').style.textAlign = 'right';
		$('qtContagem').style.textAlign       = 'right';
		$('qtEstoque').style.textAlign        = 'right';
		loadBalancos(null);
		$('qtContagem').onblur = function()	{ $('qtEstoque').value = parseFloat($('qtEstoqueBalanco').value) + parseFloat($('qtContagem').value);  }	                        
		$('cdBalanco').onblur  = function()	{ getSaldoEstoqueOf(null); };
	}
	$('idProdutoServico').nextElement = null;
	//
	$('idReduzido').onblur       = function()	{ idReduzidoOnBlur(null, this.value);       };
	$('idProdutoServico').onblur = function()	{ idProdutoServicoOnBlur(null, this.value); };
	$('nrReferencia').onblur     = function()	{ nrReferenciaOnBlur(null, this.value);     };                         
	if($('cdTabelaPreco'))
		loadTabelaPreco(null);
	createGrid({lines:[]});	                         
	enableTabEmulation();
}

function loadTabelaPreco(content) {
	if (content==null) {
		$('cdTabelaPreco').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carrengando..."));
		$('cdTabelaPreco').appendChild(newOption);
		
		getPage("GET", "loadTabelaPreco", 
				"METHODCALLER_PATH?className=com.tivic.manager.adm.TabelaPrecoServices"+
				"&method=getAll(const <%=cdEmpresa%>:int)", null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		$('cdTabelaPreco').length = 0;
		loadOptionsFromRsm($('cdTabelaPreco'), rsm, {fieldValue: 'cd_tabela_preco', fieldText:'nm_tabela_preco'});
	}
}

function loadBalancos(content) {
	if (content==null) {
		$('cdBalanco').length = 0;
		var newOption = document.createElement("OPTION");
		newOption.setAttribute("value", "");
		newOption.appendChild(document.createTextNode("Carrengando..."));
		$('cdBalanco').appendChild(newOption);
		
		getPage("GET", "loadBalancos", 
				'../methodcaller?className=com.tivic.manager.alm.BalancoServices'+
				'&method=getBalancosEmAberto(const <%=cdEmpresa%>:int)', null, true);
	}
	else {
		var rsm = null;
		try {rsm = eval('(' + content + ')')} catch(e) {}
		
		$('cdBalanco').length = 0;
//        	alert('Nº: '+reg['NR_BALANCO'] + ' - Data: ' + reg['DT_BALANCO'].split(' ')[0]+' - Local: '+reg['NM_LOCAL_ARMAZENAMENTO'])
		loadOptionsFromRsm($('cdBalanco'), rsm, {fieldValue: 'cd_balanco', fieldText:'ds_balanco'});
// 		loadOptionsFromRsm($('cdBalanco'), rsm, {fieldValue: 'cd_balanco', fieldText:'ds_balanco', putRegister: true,
// 			                                     onProcessRegister: function(reg){ 
// 			                                        	 reg['DS_BALANCO'] = 'Nº: '+reg['NR_BALANCO'] + ' - Data: ' + reg['DT_BALANCO'].split(' ')[0]+' - Local: '+reg['NM_LOCAL_ARMAZENAMENTO']; 
// 			                                         }});
	}
}

function idReduzidoOnBlur(content, value)	{
	if (content == null) {
		if(value=='' || value==null || ($('idProdutoServico').value!='' && $('cdProdutoServico').value>0))	{
			return;
		}
		$('idReduzido').value = value.toUpperCase();
		if($('lgMultiplaSelecao') && $('lgMultiplaSelecao').checked)	{
			getPage("POST", "idReduzidoOnBlur", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
					'&method=findProdutosOfEmpresa(cdEmpresa:int,idReduzido:String,cdTabelaPreco:int)', 
					[$('cdEmpresa'),$('idReduzido'),$('cdTabelaPreco')], true, null, null);
			return;					
		}
		var params = 'objects=crt=java.util.ArrayList();&execute=';
		params = createItemComparator(params, 'id_reduzido', 'idReduzido', _EQUAL, _VARCHAR);
		params = createItemComparator(params, 'A.cd_empresa', '<%=cdEmpresa%>', _EQUAL, _INTEGER);
		if($('cdTabelaPreco'))
			params = createItemComparator(params, 'cd_tabela_preco', 'cdTabelaPreco', _EQUAL, _INTEGER);
		getPage("GET", "idReduzidoOnBlur", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
		        '&' + params +
				'&method=findProdutosOfEmpresa(*crt:java.util.ArrayList)', null, true, null, null);
	}
	else {
		var rsm = null;
		try { 
			rsm = eval("(" + content + ")"); 
		} catch(e) {}
		if (rsm==null || rsm.lines.length == 0)	{
			alert('Não foi possível localizar produto!');
		}
		else
			btnFindProdutoServicoOnClick(rsm.lines);
	}
}

function nrReferenciaOnBlur(content, value)	{
	if (content == null) {
		if(value=='' || value==null || ($('idProdutoServico').value!='' && $('cdProdutoServico').value>0))	{
			return;
		}
		$('nrReferencia').value = value.toUpperCase();
		if($('lgMultiplaSelecao') && $('lgMultiplaSelecao').checked)	{
			getPage("POST", "nrReferenciaOnBlur", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
					'&method=findProdutosOfEmpresa(cdEmpresa:int,nrReferencia:String,cdTabelaPreco:int, const :String)', 
					[$('cdEmpresa'),$('nrReferencia'),$('cdTabelaPreco')], true, null, null);
			return;					
		}
		var params = 'objects=crt=java.util.ArrayList();&execute=';
		params = createItemComparator(params, 'nr_referencia', 'nrReferencia', _EQUAL, _VARCHAR);
		params = createItemComparator(params, 'A.cd_empresa', '<%=cdEmpresa%>', _EQUAL, _INTEGER);
		if($('cdTabelaPreco'))
			params = createItemComparator(params, 'cd_tabela_preco', 'cdTabelaPreco', _EQUAL, _INTEGER);
		getPage("GET", "nrReferenciaOnBlur", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
		        '&' + params +
				'&method=findProdutosOfEmpresa(*crt:java.util.ArrayList)', null, true, null, null);
	}
	else {
		var rsm = null;
		try { 
			rsm = eval("(" + content + ")"); 
		} catch(e) {}
		if (rsm==null || rsm.lines.length == 0)	{
			alert('Não foi possível localizar produto!');
		}
		else
			btnFindProdutoServicoOnClick(rsm.lines);
	}
}

function idProdutoServicoOnBlur(content, value)	{
	if (content == null) {
		if(value=='' || value==null)	{
			$('idReduzido').focus();
			$('idReduzido').select();
			return;
		}
		$('idProdutoServico').value = value.toUpperCase();
		var params = 'objects=crt=java.util.ArrayList();&execute=';
		params = createItemComparator(params, 'id_produto_servico', 'idProdutoServico', _EQUAL, _VARCHAR);
		params = createItemComparator(params, 'A.cd_empresa', '<%=cdEmpresa%>', _EQUAL, _INTEGER);
		if($('cdTabelaPreco'))
			params = createItemComparator(params, 'cd_tabela_preco', 'cdTabelaPreco', _EQUAL, _INTEGER);
		getPage("GET", "idProdutoServicoOnBlur", '../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices'+
		        '&' + params +
				'&method=findProdutosOfEmpresa(*crt:java.util.ArrayList)', null, true, null, null);
	}
	else {
		var rsm = null;
		try { 
			rsm = eval("(" + content + ")"); 
		} catch(e) {}
		if (rsm==null || rsm.lines.length == 0)	{
			alert('Não foi possível localizar produto!');
		}
		else
			btnFindProdutoServicoOnClick(rsm.lines);
	}
}


var regProduto;
function btnFindProdutoServicoOnClick(reg)	{
    if(!reg)	{
		var filters =  [[{label:"Nome do produto", reference:"nm_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:30, charcase:'uppercase'},
						 {label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
						 {label:"Código de Barras", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
						 {label:"Referência", reference:"nr_referencia", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:15, charcase:'uppercase'},
						 {label:"ID_Reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:15, charcase:'uppercase'}]];
		var columnsGrid = [{label:"Nome", reference:"NM_PRODUTO_SERVICO"},
		                   {label:"Tamanho", reference:"TXT_DADO_TECNICO"},
						   {label:"Cor", reference:"TXT_ESPECIFICACAO"},
<%=cdTipoOperacaoVarejo>0?"{label:\"Preço Varejo\", reference:\"vl_preco_"+cdTipoOperacaoVarejo+"\", type: GridOne._CURRENCY},":""%>
						   {label:"Referência", reference:"nr_referencia"},
						   {label:"ID Reduzido", reference:"id_reduzido"},
						   {label:"Fabricante", reference:"nm_fabricante"}];
		FilterOne.create("jFiltro", {caption:"Pesquisando produtos", width: 690, height: 370, modal: true, noDrag: true, noTitle: true,
									   className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices", method: "findProdutosOfEmpresa(*crt:java.util.ArrayList)",
									   filterFields: filters,
									   gridOptions: {columns: columnsGrid, strippedLines: true, columnSeparator: false, lineSeparator: false},
									   hiddenFields: [{reference:"A.cd_empresa",value:<%=cdEmpresa%>, comparator:_EQUAL,datatype:_INTEGER}
									                  <%=tpEntrada==0?",{reference:\'cd_tabela_preco\',value:$(\'cdTabelaPreco\').value, comparator:_EQUAL,datatype:_INTEGER}":""%>],
									   callback: btnFindProdutoServicoOnClick,autoExecuteOnEnter: true});
    }
    else {// retorno
    	closeWindow('jFiltro');
        regProduto = reg[0];
        if(<%=tpEntrada==0%>)
        	createGrid({lines:reg});
        
        $("cdProdutoServico").value 	= reg[0]['CD_PRODUTO_SERVICO'];
        $("cdProdutoServicoView").value = reg[0]['NM_PRODUTO_SERVICO'];
        $("idProdutoServico").value 	= reg[0]['ID_PRODUTO_SERVICO'];
        $("idReduzido").value 			= reg[0]['ID_REDUZIDO'];
        $("nrReferencia").value 		= reg[0]['NR_REFERENCIA'];
        $("txtEspecificacao").value 	= reg[0]['TXT_ESPECIFICACAO']!=null ? reg[0]['TXT_ESPECIFICACAO'] : '';
        $("txtDadoTecnico").value 		= reg[0]['TXT_DADO_TECNICO']!=null  ? reg[0]['TXT_DADO_TECNICO']  : '';
        $("nmFabricante").value 		= reg[0]['NM_FABRICANTE']!=null     ? reg[0]['NM_FABRICANTE']     : '';
        $("vlPreco").value 				= formatCurrency(reg[0]['VL_PRECO']);
        if($("vlPreco") && $("vlPreco").value=='true')
        	$("vlPreco").value = '0,00';
        if($("vlPreco<%=cdTipoOperacaoVarejo%>"))
        	$("vlPreco<%=cdTipoOperacaoVarejo%>").value = formatCurrency(reg[0]['VL_PRECO_<%=cdTipoOperacaoVarejo%>']);
        if($("vlPreco<%=cdTipoOperacaoVarejo%>") && $("vlPreco<%=cdTipoOperacaoVarejo%>").value=='true')
        	$("vlPreco<%=cdTipoOperacaoVarejo%>").value = '0,00';
		$('qtAtual').value          = reg[0]['QT_ESTOQUE'];
		
<%if(tpEntrada==0)	{%>
       	$("vlPreco").select();
       	$("vlPreco").focus();
<%} else {%>
		getSaldoEstoqueOf(null);
  		$('qtContagem').value       = 0;
  		$('qtEstoque').value        = 0;
  		$('qtEstoqueBalanco').value = 0;
       	//	
        if($('lgContagemAutomatica') && $('lgContagemAutomatica').checked)	{
        	$('qtContagem').value = 1;
        	$('qtEstoque').value  = parseInt($('qtEstoqueBalanco').value, 10) + 1;
        }
        if($('lgConfirmacaoAutomatica') && $('lgConfirmacaoAutomatica').checked && $('lgContagemAutomatica').checked)
	        btnSaveProdutoPrecoOnClick(null);
	    else	{	
	       $("qtContagem").select();
	       $("qtContagem").focus();
	   }
<%}%>
    }
}

function getSaldoEstoqueOf(content)	{
	if(content==null)	{
		getPage("POST", "getSaldoEstoqueOf", "../methodcaller?className=com.tivic.manager.alm.BalancoServices"+
				"&method=getItem(cdBalanco:int,cdEmpresa:int,cdProdutoServico:int)",
				[$('cdBalanco'), $('cdEmpresa'), $("cdProdutoServico")], true);
    }
    else	{
       	$('qtAtual').value           = 0;
       	$('qtEstoque').value         = 0;
       	$('qtEstoqueBalanco').value  = 0;
    	var rsm = eval("("+content+")");
    	if(rsm.lines.length > 0) {
           	$('qtAtual').value          = rsm.lines[0]['QT_ESTOQUE'];
           	$('qtEstoqueBalanco').value = rsm.lines[0]['QT_ESTOQUE_BALANCO'];
           	$('qtEstoque').value        = parseFloat($('qtEstoqueBalanco').value) + parseFloat($('qtContagem').value);
    	}
    }
}

function btnSaveProdutoPrecoOnClick(content)	{
	if(content==null)	{
	<%if(tpEntrada==0)	{%>
		if($('lgMultiplaSelecao').checked)	{
			var objects = 'produtos=java.util.ArrayList();', execute = '';
			for(var i=0; i<gridProdutos.size(); i++)	{
				execute += 'produtos.add(const '+gridProdutos.getRegisterByIndex(i)['CD_PRODUTO_SERVICO']+':Object);';
			}
			setTimeout(function()	{
				getPage("POST", "btnSaveProdutoPrecoOnClick", "../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices"+
						"&method=setProdutoPreco(cdEmpresa:int,*produtos:ArrayList,cdTabelaPreco:int,const "+changeLocale("vlPreco")+":float)"+
						"&objects="+objects+"&execute="+execute, 
						[$('cdEmpresa'),$("cdProdutoServico"),$("cdTabelaPreco")], true, null, 'Definindo preço')}, 100);
			return;						
		}
		
		setTimeout(function()	{
			getPage("POST", "btnSaveProdutoPrecoOnClick", "../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices"+
					"&method=setProdutoPreco(cdEmpresa:int,cdProdutoServico:int,cdTabelaPreco:int,const "+changeLocale("vlPreco")+":float,idProdutoServico:String)", 
					[$('cdEmpresa'),$("cdProdutoServico"),$("idProdutoServico"), $("cdTabelaPreco")], true, null, 'Definindo preço')}, 100);
	<%} else {%>
		if($("cdBalanco").value <= 0) {
			alert('Balanço não informado!');
			return;
		}
		//
		setTimeout(function()	{
			getPage("POST", "btnSaveProdutoPrecoOnClick", "../methodcaller?className=com.tivic.manager.grl.ProdutoServicoEmpresaServices"+
					"&method=setProdutoEstoque(cdEmpresa:int,cdProdutoServico:int,cdBalanco:int,const "+changeLocale("qtEstoque")+":float,idProdutoServico:String)", 
					[$('cdEmpresa'),$("cdProdutoServico"),$("idProdutoServico"), $("cdBalanco")], true, null, 'Definindo estoque')}, 100);
	<%}%>
    }
    else	{
    	var ret = processResult(content, '', {});
        if(ret.code > 0)	{
          	regProduto['QT_ATUAL']           = changeLocale('qtAtual'); 
          	regProduto['QT_ESTOQUE_BALANCO'] = changeLocale('qtEstoque'); 
            $('cdProdutoServico').value      = 0;
        	$("cdProdutoServicoView").value  = '';
            $('idProdutoServico').value      = '';
            $('idReduzido').value            = '';
            $('nrReferencia').value          = '';
			$('txtDadoTecnico').value        = '';
			$('txtEspecificacao').value      = '';
			$('nmFabricante').value          = '';
            $('vlPreco').value               = '0,00';
            $('qtAtual').value               = 0;
            if(<%=tpEntrada==0%>)
            	createGrid({lines:[]});
            else	{
                $('qtEstoque').value        = 0;
	            $('qtContagem').value       = 0;
	            $('qtEstoqueBalanco').value = 0;
	            gridProdutos.addLine(gridProdutos.size(), regProduto, null, true);
            	gridProdutos.moveRowTo(1);
            }
            $('idProdutoServico').focus();
        }
    }
}

var gridProdutos;
function createGrid(rsm)	{
	var columns = [{label:"Referência", reference:"ID_REDUZIDO"},
                   {label:"Nome", reference:"NM_PRODUTO_SERVICO"},
                   {label:"Tamanho", reference:"TXT_DADO_TECNICO"},
                   {label:"Cor", reference:"TXT_ESPECIFICACAO"},
                   {label:"Referência", reference:"nr_referencia"},
                   {label:"ID Reduzido", reference:"id_reduzido"},
                   {label:"Fabricante", reference:"nm_fabricante"},
                   {label:"<%=tpEntrada==0?"Preço":"Estoque(Sistema)"%>", reference:"<%=tpEntrada==0?"QT_IDEAL":"QT_ATUAL"%>", type: GridOne._CURRENCY},
                   <%=tpEntrada==1?"{label:\'Balanço\', reference:\'QT_ESTOQUE_BALANCO\', type: GridOne._FLOAT, precision: 0},":""%>
                   <%=tpEntrada==1?"{label:\'Preço Varejo\', reference:\'VL_PRECO_"+cdTipoOperacaoVarejo+"\', type: GridOne._CURRENCY},":""%>
                   {label:"Código de Barras", reference:"ID_PRODUTO_SERVICO"}];
                    
  	gridProdutos = GridOne.create('gridProdutos', {columns: columns, resultset: rsm, strippedLines: true, columnSeparator: true,
						                           lineSeparator: false, plotPlace: $('divGrid'), noSelectOnCreate: false});
}
</script>
<body class="body" onload="init();">
</body>