<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.tivic.manager.alm.GrupoDAO"%>
<%@page import="com.tivic.manager.alm.Grupo"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title></title>
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security"%>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<%@page import="com.tivic.manager.grl.EmpresaServices"%>
<%@page import="sol.util.Jso"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="java.sql.Types"%>
<%@page import="com.tivic.manager.grl.Empresa"%>
<%@page import="com.tivic.manager.grl.EmpresaDAO"%>
<%@page import="com.tivic.manager.alm.DocumentoSaidaServices"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.fsc.*"%>
<%@page import="com.tivic.manager.seg.*"%>
<%@taglib uri="../tlds/loader.tld" prefix="loader"%>
<%
	int cdEmpresa = sol.util.RequestUtilities.getAsInteger(request, "cdEmpresa", 0);
	Empresa empresa = EmpresaDAO.get(cdEmpresa);
	Usuario usuario        = (Usuario)session.getAttribute("usuario");
	Pessoa pessoaUsuario   = usuario!=null ? PessoaDAO.get(usuario.getCdPessoa()) : null;
	int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, empresa.getCdEmpresa());
	Grupo grupoCombustivel = GrupoDAO.get(cdGrupoCombustivel);
%>
<loader:library
	libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut, report, flatbutton, chart"
	compress="false" />
<script language="javascript" src="../js/jquery-1.6.1.min.js"></script>
<script language="javascript">

var $jQ = jQuery.noConflict();
var tiposSaida          = <%=Jso.getStream(DocumentoSaidaServices.tiposSaida)%>;
var situacaoDocumento   = <%=Jso.getStream(DocumentoSaidaServices.situacoes)%>;
var tiposDocumentoSaida = <%=Jso.getStream(DocumentoSaidaServices.tiposDocumentoSaida)%>;
var tiposMovimento      = <%=Jso.getStream(DocumentoSaidaServices.tiposMovimento)%>;

var rsmColumnsTurnos = {lines:[]};
var rsmColumnsFormaPagamento = {lines:[]};
var rsmColumnsCategoriaProdutos = {lines:[]};
var rsmColumnsSubCategoriaProdutos = {lines:[]};
var rsmColumnsPlanoPagamento = {lines:[]};
var rsmColumnsDocumentoSaida = {lines:[]};
var rsmColumnsTipoSaida = {lines:[]};

rsmGrupos     = <%=Jso.getStream(com.tivic.manager.alm.GrupoServices.getAllHierarquia() ) %>;
rsmTurnos     = <%=Jso.getStream(com.tivic.manager.adm.TurnoDAO.getAll())%>;
rsmPagamento  = <%=Jso.getStream(com.tivic.manager.adm.FormaPagamentoDAO.getAll())%>;
rsmPlanoPagto = <%=Jso.getStream(com.tivic.manager.adm.PlanoPagamentoDAO.getAll())%>;
rsmDocSaida   = <%=Jso.getStream(com.tivic.manager.alm.DocumentoSaidaServices.tiposDocumentoSaida)%>;
rsmTpSaida    = <%=Jso.getStream(com.tivic.manager.alm.DocumentoSaidaServices.tiposSaida)%>;

function onSubCatCkAllChange(){
	   for( var i=0; i<rsmColumnsCatProdutos.lines.length;i++ ){
		   if( rsmColumnsCatProdutos.lines[i].CHECKED == 1 ){
			   toggleSubCategorias( rsmColumnsCatProdutos.lines[i].CD_GRUPO , $('ckSubCatProdutos').checked);
		   }
		}
	   gridSubCatProdutos.updateRows();
}

function init()	{
	
	function toggleSubCategorias( cdGrupoSuperior, toggle ){
		 for( i=0;i<rsmColumnsSubCategoriaProdutos.lines.length;i++ ){
			 if( rsmColumnsSubCategoriaProdutos.lines[i].CD_GRUPO_SUPERIOR  == cdGrupoSuperior ){
				 rsmColumnsSubCategoriaProdutos.lines[i].CHECKED = toggle;
				 check = $('checkbox_gridSubCatProdutostable_tr_'+(i+1).toString()+'_td_0');
				 check.removeAttribute('disabled');
				 if(!toggle) 
				 	check.disabled = true;
			 }
			 
		 }
		 gridSubCatProdutos.updateRows();
	}

	
	/* Gerando Array do Grid Turnos. 
	 * Inserindo os dados no Grid com seus respectivos checkboxes e valores.
	 */
	
	for(i=0; i<rsmTurnos.lines.length;i++){
		newItem = {LABEL: rsmTurnos.lines[i].NM_TURNO, CD_TURNO:rsmTurnos.lines[i].CD_TURNO, TYPE: "checkbox", CHECKED: '0'};
		rsmColumnsTurnos.lines[i] = newItem;
	}
	
	gridTurnos = GridOne.create('gridTurnos', {plotPlace: $('divGridTurnos'),
		resultset: rsmColumnsTurnos,
		noSelectorColumn: true, /*noSelect: true,*/
		noFocusOnSelect: true,
		lineSeparator: true,
		columnSeparator: false,
		columns: [
		          {label:"", reference: 'CHECKED', type: GridOne._CHECKBOX, width: '20', onCheck: function(){
										this.parentNode.parentNode.register['CHECKED'] = this.checked ? 1 : 0;
										for(i=0;i<rsmColumnsTurnos.lines.length;i++){
											if( rsmColumnsTurnos.lines[i].CD_TURNO == this.CD_TURNO ){
												rsmColumnsTurnos.lines[i].disabled = this.checked ? "false" : "true";
											}
										}
							}
		          },
				  {label:'Turnos', reference:'LABEL', width: '800'}]
	});
	
	
	
	
	/* Gerando Array do Grid Forma de Pagamentos. 
	 * Inserindo os dados no Grid com seus respectivos checkboxes e valores.
	 */
	 
	for(i=0; i<rsmPagamento.lines.length;i++){
		newItem = {LABEL: rsmPagamento.lines[i].NM_FORMA_PAGAMENTO, CD_FORMA_PAGAMENTO: rsmPagamento.lines[i].CD_FORMA_PAGAMENTO, TYPE: "checkbox", CHECKED: '0'};
		rsmColumnsFormaPagamento.lines[i] = newItem;
	}
	
	gridTpPagamento = GridOne.create('gridFormaPagamento', {plotPlace: $('divGridFormaPagamento'),
		resultset: rsmColumnsFormaPagamento,
		noSelectorColumn: true, /*noSelect: true,*/
		noFocusOnSelect: true,
		lineSeparator: true,
		columnSeparator: false,
		columns: [
		          {label:"", reference: 'CHECKED', type: GridOne._CHECKBOX, width: '80', onCheck: function(){
										this.parentNode.parentNode.register['CHECKED'] = this.checked ? 1 : 0;
										for(i=0;i<rsmColumnsFormaPagamento.lines.length;i++){
											if( rsmColumnsFormaPagamento.lines[i].CD_FORMA_PAGAMENTO == this.CD_FORMA_PAGAMENTO ){
												rsmColumnsFormaPagamento.lines[i].disabled = this.checked ? "false" : "true";
											}
										}
							}
		          },
				  {label:'Forma de Pagamento', reference:'LABEL', width: '800'}]
	});
	
	
	
	
	/* Gerando Array do Grid de Categorias de Produtos.  
	 * Inserindo os dados no Grid com seus respectivos checkboxes e valores.
	 */
	 
	for (var i = 0; i < rsmGrupos.lines.length; i++) {
		if(rsmGrupos.lines[i].CD_GRUPO != <%= cdGrupoCombustivel %>){
			rsmColumnsCategoriaProdutos.lines.push({
				LABEL: rsmGrupos.lines[i].NM_GRUPO,
				TYPE: "checkbox",
				CD_GRUPO: rsmGrupos.lines[i].CD_GRUPO,
				CHECKED: '0'
			});
		}
	}
	
	gridCatProdutos = GridOne.create('gridCatProdutos', {plotPlace: $('divGridCatProdutos'),
		resultset: rsmColumnsCategoriaProdutos,
		noSelectorColumn: true, /*noSelect: true,*/
		noFocusOnSelect: true,
		lineSeparator: true,
		columnSeparator: false,
		columns: [
		          {label:"", reference: 'CHECKED', type: GridOne._CHECKBOX, width: '80', onCheck: function(){
										this.parentNode.parentNode.register['CHECKED'] = this.checked ? 1 : 0;
										toggleSubCategorias( this.parentNode.parentNode.register['CD_GRUPO'], this.checked );
										for(i=0;i<rsmColumnsCategoriaProdutos.lines.length;i++){
											if( rsmColumnsCategoriaProdutos.lines[i].CD_GRUPO_SUPERIOR != this.CD_GRUPO ){
												rsmColumnsCategoriaProdutos.lines[i].disabled = this.checked ? "false" : "true";
											}
										}
							}
		          },
				  {label:'Categoria de Produtos', reference:'LABEL', width: '800'}]
	});
	
	
	
	
	/* Gerando Array do Grid de Subcategorias de Produtos.  
	 * Inserindo os dados no Grid com seus respectivos checkboxes e valores.
	 */
	 
	for( i=0; i<rsmGrupos.lines.length;i++ ){
		rsmSubGrupos = rsmGrupos.lines[i].SUBRESULTSETMAP;
		for( j=0;j<rsmSubGrupos.lines.length;j++ ){
			if(rsmSubGrupos.lines[j].CD_GRUPO_SUPERIOR != <%= cdGrupoCombustivel %>){
				rsmColumnsSubCategoriaProdutos.lines.push({
					LABEL:rsmSubGrupos.lines[j].NM_GRUPO, TYPE: "checkbox",
					CD_GRUPO:rsmSubGrupos.lines[j].CD_GRUPO,
					CD_GRUPO_SUPERIOR: rsmSubGrupos.lines[j].CD_GRUPO_SUPERIOR,
					CHECKED: '0'
				});
			}
		}
	} 
	
	gridSubCatProdutos = GridOne.create('gridSubCatProdutos', {plotPlace: $('divGridSubCatProdutos'),
		resultset: rsmColumnsSubCategoriaProdutos,
		noSelectorColumn: true, /*noSelect: true,*/
		noFocusOnSelect: true,
		lineSeparator: true,
		columnSeparator: false,
		onCreate: function(){
  	 		//Mantem as subcategorias desabilitadas, até ser selecionada 
			check = this.getElementsColumn(0);
			for( var i=0; i<rsmColumnsSubCategoriaProdutos.lines.length;i++ ){
				check[i].disabled = 'disabled';
				}
		},
		columns: [
		          {label:"", reference: 'CHECKED', type: GridOne._CHECKBOX, width: '80', onCheck: function(){
		        	  this.parentNode.parentNode.register['CHECKED'] = this.checked ? 1 : 0;
		        	  if (this.checked == false){
		        		  this.disabled = true;
		        	  }
		        	      for(i=0;i<rsmColumnsSubCategoriaProdutos.lines.length;i++){
		        		      if( rsmColumnsSubCategoriaProdutos.lines[i].CD_GRUPO_SUPERIOR == this.CD_GRUPO ){
		        			      rsmColumnsSubCategoriaProdutos.lines[i].disabled = this.checked ? "false" : "true";
		        		      }
		        	      }
		              }
		          },
		          {label:'Categoria de Produtos', reference:'LABEL', width: '800'}]
	});
	
	
	
	
	/* Gerando Array do Grid de Planos de Pagamentos.  
	 * Inserindo os dados no Grid com seus respectivos checkboxes e valores.
	 */

	for(i=0; i<rsmPlanoPagto.lines.length;i++){
		newItem = {LABEL: rsmPlanoPagto.lines[i].NM_PLANO_PAGAMENTO, CD_PLANO_PAGAMENTO: rsmPlanoPagto.lines[i].CD_PLANO_PAGAMENTO, TYPE: "checkbox", CHECKED: '0'};
		rsmColumnsPlanoPagamento.lines[i] = newItem;
	}
	gridPlanoPagamento = GridOne.create('gridPlanoPagamento', {plotPlace: $('divGridPlanPagamento'),
		resultset: rsmColumnsPlanoPagamento,
		noSelectorColumn: true, /*noSelect: true,*/
		noFocusOnSelect: true,
		lineSeparator: true,
		columnSeparator: false,
		columns: [
		          {label:"", reference: 'CHECKED', type: GridOne._CHECKBOX, width: '80', onCheck: function(){
										this.parentNode.parentNode.register['CHECKED'] = this.checked ? 1 : 0;
										for(i=0;i<rsmColumnsFormaPagamento.lines.length;i++){
											if( rsmColumnsPlanoPagamento.lines[i].CD_PLANO_PAGAMENTO == this.CD_PLANO_PAGAMENTO ){
												rsmColumnsPlanoPagamento.lines[i].disabled = this.checked ? "false" : "true";
											}
										}
							}
		          },
				  {label:'Planos de Pagamento', reference:'LABEL', width: '800'}]
	});	
	
    /* Gerando Array do Grid de Documentos Saída.  
	 * Inserindo os dados no Grid com seus respectivos checkboxes e valores.
	 */
	for(i=0; i<rsmDocSaida.length;i++){
		newItem = {LABEL: rsmDocSaida[i], TP_DOCUMENTO_SAIDA: i, TYPE: "checkbox", CHECKED: '0'};
		rsmColumnsDocumentoSaida.lines[i] = newItem;
	}
    
	gridDocSaida = GridOne.create('gridDocumentoSaida', {plotPlace: $('divGridTpSaida'),
		resultset: rsmColumnsDocumentoSaida,
		noSelectorColumn: true, /*noSelect: true,*/
		noFocusOnSelect: true,
		lineSeparator: true,
		columnSeparator: false,
		columns: [
		          {label:"", reference: 'CHECKED', type: GridOne._CHECKBOX, width: '80', onCheck: function(){
										this.parentNode.parentNode.register['CHECKED'] = this.checked ? 1 : 0;
										for(i=0;i<rsmColumnsDocumentoSaida.lines.length;i++){
											if( rsmColumnsDocumentoSaida.lines[i] == this ){
												rsmColumnsDocumentoSaida.lines[i].disabled = this.checked ? "false" : "true";
											}
										}
							}
		          },
				  {label:'Tipo de Documento', reference:'LABEL', width: '800'}]
	});	
	
	
	
	
    /* Gerando Array do Grid de Documentos Saída.  
	 * Inserindo os dados no Grid com seus respectivos checkboxes e valores.
	 */
	for(i=0; i<rsmTpSaida.length;i++){
		newItem = {LABEL: rsmTpSaida[i], TP_SAIDA: i, TYPE: "checkbox", CHECKED: '0'};
		rsmColumnsTipoSaida.lines[i] = newItem;
	}
    
	gridDocSaida = GridOne.create('gridTiposSaida', {plotPlace: $('divGridTpDoc'),
		resultset: rsmColumnsTipoSaida,
		noSelectorColumn: true, /*noSelect: true,*/
		noFocusOnSelect: true,
		lineSeparator: true,
		columnSeparator: false,
		columns: [
		          {label:"", reference: 'CHECKED', type: GridOne._CHECKBOX, width: '80', onCheck: function(){
										this.parentNode.parentNode.register['CHECKED'] = this.checked ? 1 : 0;
										for(i=0;i<rsmColumnsTipoSaida.lines.length;i++){
											if( rsmColumnsTipoSaida.lines[i] == this ){
												rsmColumnsTipoSaida.lines[i].disabled = this.checked ? "false" : "true";
											}
										}
							}
		          },
				  {label:'Tipo Saída', reference:'LABEL', width: '800'}]
	});
	
    loadFormFields(["relSaida"]);
	clearFields(relSaidaFields);
	var dataMask = new Mask($("dtSaidaInicial").getAttribute("mask"));
    dataMask.attach($("dtSaidaInicial"));
    dataMask.attach($("dtSaidaFinal"));
    $('dtSaidaInicial').value 	= formatDateTime(new Date());
    $('dtSaidaFinal').value 	= formatDateTime(new Date());
    enableTabEmulation();

     toolbar = ToolBar.create('toolBar', {plotPlace: 'toolBarRelatorio', orientation: 'vertical',
		 buttons: [{id: 'btnImprimir', img: '/sol/imagens/print24.gif', label: 'Imprimir', title: 'Imprimir Dados', onClick: btImprimirOnClick, imagePosition: 'left', width: 100}, {separator: 'vertical'},
		           {id: 'btnFechar', img: '/sol/imagens/cancel_13.gif', label: 'Fechar Janela', title: 'Fechar Janela', onClick: btFecharOnClick, imagePosition: 'left', width: 100}, {separator: 'vertical'}]});


	
    loadOptions($('tpMovimentoEstoque'), <%=Jso.getStream(DocumentoSaidaServices.tiposMovimento)%>);
	loadOptions($('tpFrete'), <%=Jso.getStream(DocumentoSaidaServices.tiposFrete)%>);
	loadOptions($('stDocumentoSaida'), situacaoDocumento); $('stDocumentoSaida').value = 1;
	loadOptionsFromRsm($('cdTipoOperacao'), <%=Jso.getStream(TipoOperacaoServices.getAll(0))%>, {fieldValue: 'cd_tipo_operacao', fieldText:'nm_tipo_operacao'});
	
}

function btImprimirOnClick() {

	var objects    = "crt=java.util.ArrayList();groups=java.util.ArrayList();order=java.util.ArrayList();";
	var execute    = "";
	
	/* Definindo variáveis que armazenarão os checkboxes selecionados
	 * Para serem usados no ItemComparator.
	 */

	var arTurnos               =  [];
	var arFormaPagamento       =  [];
	var arCategoriaProdutos    =  [];
	var arSubCategoriaProdutos =  [];
	var arPlanoPagamento       =  [];
	var arDocumentoSaida       =  [];
	var arTipoSaida            =  [];
	var nmTurnos;
	
	/* Resgatando checkboxes marcados relacionados aos Turnos e os
	 * adicionando ao ItemComparator
  	 */
	for( i=0; i < rsmColumnsTurnos.lines.length;i++ ){
		if( rsmColumnsTurnos.lines[i].CHECKED == 1 ){
			arTurnos.push( rsmColumnsTurnos.lines[i].CD_TURNO );		
		}
	}
	

	if(arTurnos.length > 0) {
		nmTurnos     = arTurnos.join(", ");
		objects   += 'turnos=sol.dao.ItemComparator(const C.cd_turno:String,const ' + arTurnos.join('|') + ':String, const ' + _IN + ':int, const ' + _INTEGER + ':int);';
		execute   += 'crt.add(*turnos:Object);';
	} else if (arTurnos.length == rsmColumnsTurnos.lines.length) {
		nmTurnos = "Todos";
	} else {
		nmTurnos = "Todos";
	}
	
	
	/* Resgatando checkboxes marcados relacionados as Formas de Pagamentos e os
	 * adicionando ao ItemComparator
  	 */	
  	 
  	
	for( i=0; i < rsmColumnsFormaPagamento.lines.length;i++ ){
		if( rsmColumnsFormaPagamento.lines[i].CHECKED == 1 ){
			arFormaPagamento.push( rsmColumnsFormaPagamento.lines[i].CD_FORMA_PAGAMENTO );		
		}
	}
	
	
	if(arFormaPagamento.length > 0) {
		objects   += 'fpgntos=sol.dao.ItemComparator(const J.cd_forma_pagamento:String,const '+arFormaPagamento.join('|')+':String, const '+_IN+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*fpgntos:Object);';
	}
	
	
	/* Resgatando checkboxes marcados relacionados as Planos de Pagamentos e os
	 * adicionando ao ItemComparator
  	 */  	 
  	
	for( i=0; i < rsmColumnsPlanoPagamento.lines.length;i++ ){
		if( rsmColumnsPlanoPagamento.lines[i].CHECKED == 1 ){
			arPlanoPagamento.push( rsmColumnsPlanoPagamento.lines[i].CD_PLANO_PAGAMENTO );		
		}
	}
	
	
	if(arPlanoPagamento.length > 0) {
		objects   += 'plnpag=sol.dao.ItemComparator(const J.cd_plano_pagamento:String,const '+arPlanoPagamento.join('|')+':String, const '+_IN+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*plnpag:Object);';
	}
	
	
	/* Resgatando checkboxes marcados relacionados as Categoria de Produtos e os
	 * adicionando ao ItemComparator
  	 */	
	for( i=0; i < rsmColumnsCategoriaProdutos.lines.length;i++ ){
		if( rsmColumnsCategoriaProdutos.lines[i].CHECKED == 1 ){
			arCategoriaProdutos.push( rsmColumnsCategoriaProdutos.lines[i].CD_GRUPO );	
		}
	}
	
	for( i=0; i < rsmColumnsSubCategoriaProdutos.lines.length;i++ ){
		if( rsmColumnsSubCategoriaProdutos.lines[i].CHECKED == 1 ){
			arSubCategoriaProdutos.push( rsmColumnsSubCategoriaProdutos.lines[i].CD_GRUPO );		
		}
	}
	
// 	if(arSubCategoriaProdutos.length > 0) {
// 		objects   += 'subcatprod=sol.dao.ItemComparator(const H.cd_grupo:String,const '+arSubCategoriaProdutos.join('|')+':String, const '+_IN+':int, const '+_INTEGER+':int);';
// 		execute   += 'crt.add(*subcatprod:Object);';
// 	}
	
	if(arCategoriaProdutos.length > 0) {
		objects   += 'catprod=sol.dao.ItemComparator(const I.cd_grupo:String,const '+arCategoriaProdutos.join('|')+
			(arCategoriaProdutos.length>0 ? '|'+arSubCategoriaProdutos.join('|') : '')+':String, const '+_IN+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*catprod:Object);'		
	}	
	
	
	
	/* Resgatando checkboxes marcados relacionados as Tipos de Saídas e os
	 * adicionando ao ItemComparator
  	 */	
	for( i=0; i < rsmColumnsTipoSaida.lines.length;i++ ){
		if( rsmColumnsTipoSaida.lines[i].CHECKED == 1 ){
			arTipoSaida.push( rsmColumnsTipoSaida.lines[i].TP_SAIDA );		
		}
	}
	
	if(arTipoSaida.length > 0) {
		objects   += 'tpsaida=sol.dao.ItemComparator(const C.tp_saida:String,const '+arTipoSaida.join('|')+':String, const '+_IN+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*tpsaida:Object);'		
	}	
	
	/* Resgatando checkboxes marcados relacionados as Tipos de Saídas e os
	 * adicionando ao ItemComparator
  	 */	
	for( i=0; i < rsmColumnsDocumentoSaida.lines.length;i++ ){
		if( rsmColumnsDocumentoSaida.lines[i].CHECKED == 1 ){
			arDocumentoSaida.push( rsmColumnsDocumentoSaida.lines[i].TP_DOCUMENTO_SAIDA );		
		}
	}	
	
	if(arDocumentoSaida.length > 0) {
		objects   += 'docsaida=sol.dao.ItemComparator(const C.tp_documento_saida:String,const '+arDocumentoSaida.join('|')+':String, const '+_IN+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*docsaida:Object);'		
	}

	/* Resgatando checkboxes marcados relacionados as Subcategoria de Produtos e os
	 * adicionando ao ItemComparator
  	 */	
// 	for( i=0; i < rsmColumnsSubCategoriaProdutos.lines.length;i++ ){
// 		if( rsmColumnsSubCategoriaProdutos.lines[i].CHECKED == 1 ){
// 			arSubCategoriaProdutos.push( rsmColumnsSubCategoriaProdutos.lines[i].CD_GRUPO );		
// 		}
// 	}
	
// 	if(arSubCategoriaProdutos.length > 0) {
// 		objects   += 'subcatprod=sol.dao.ItemComparator(const H.cd_grupo:String,const '+arSubCategoriaProdutos.join('|')+':String, const '+_IN+':int, const '+_INTEGER+':int);';
// 		execute   += 'crt.add(*subcatprod:Object);';
// 	}
	
	
	
	/* Resgatando checkboxes marcados relacionados o Nome do Cliente e os
	 * adicionando ao ItemComparator
  	 */		
	if($('cdCliente').value > 0) {
		objects   += 'cdcliente=sol.dao.ItemComparator(const C.cd_cliente:String,const '+$('cdCliente').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*cdcliente:Object);';
	}
	
	/* Resgatando checkboxes marcados relacionados o Nome do Cliente e os
	 * adicionando ao ItemComparator
  	 */		
	if($('cdVendedor').value > 0) {
		objects   += 'cdvendedor=sol.dao.ItemComparator(const C.cd_vendedor:String,const '+$('cdVendedor').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*cdvendedor:Object);';
	}
	
	/* Resgatando checkboxes marcados relacionados o Nome do Cliente e os
	 * adicionando ao ItemComparator
  	 */		
	if($('cdTransportadora').value > 0) {
		objects   += 'cdtransportadora=sol.dao.ItemComparator(const C.cd_transportadora:String,const '+$('cdTransportadora').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*cdtransportadora:Object);';
	}
	
	/* Resgatando checkboxes marcados relacionados o Tipo de Movimento e os
	 * adicionando ao ItemComparator
  	 */		
	if($('stDocumentoSaida').value > 0) {
		objects   += 'stdocumentosaida=sol.dao.ItemComparator(const C.st_documento_saida:String,const '+$('stDocumentoSaida').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*stdocumentosaida:Object);';
	}
	
	/* Resgatando checkboxes marcados relacionados o Situação do Documento e os
	 * adicionando ao ItemComparator
  	 */		
	if($('tpMovimentoEstoque').value > 0) {
		objects   += 'tpmovimentoestoque=sol.dao.ItemComparator(const C.tp_movimento_estoque:String,const '+$('tpMovimentoEstoque').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*tpmovimentoestoque:Object);';
	}
	
	/* Resgatando checkboxes marcados relacionados ao Movimento Estoque e os
	 * adicionando ao ItemComparator
  	 */		
	if($('tpMovimentoEstoque').value > 0) {
		objects   += 'tpmovimentoestoque=sol.dao.ItemComparator(const C.tp_movimento_estoque:String,const '+$('tpMovimentoEstoque').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*tpmovimentoestoque:Object);';
	}
	
	/* Resgatando checkboxes marcados relacionados ao Tipo de Operação e os
	 * adicionando ao ItemComparator
  	 */		
	if($('cdTipoOperacao').value > 0) {
		objects   += 'tpoperacao=sol.dao.ItemComparator(const C.cd_tipo_operacao:String,const '+$('cdTipoOperacao').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*tpoperacao:Object);';
	}
	
	/* Resgatando checkboxes marcados relacionados ao Tipo de Frete e os
	 * adicionando ao ItemComparator
  	 */		
	if($('tpFrete').value > 0) {
		objects   += 'tpfrete=sol.dao.ItemComparator(const C.tp_frete:String,const '+$('tpFrete').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*tpfrete:Object);';
	}
	
	/* Resgatando checkboxes marcados relacionados ao Forma de Pagamento e os
	 * adicionando ao ItemComparator
  	 */		
	if($('tpFormaPagamento').value > 0) {
		objects   += 'tpfpgto=sol.dao.ItemComparator(const C.tp_forma_pagamento:String,const '+$('tpFormaPagamento').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*tpfpgto:Object);';
	}
	
	/* Resgatando checkboxes marcados relacionados ao Produto Serviço e os
	 * adicionando ao ItemComparator
  	 */		
	if($('cdProdutoServico').value > 0) {
		objects   += 'prodsrv=sol.dao.ItemComparator(const A.cd_produto_servico:String,const '+$('cdProdutoServico').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*prodsrv:Object);';
	}
	
	/* Resgatando checkboxes marcados relacionados ao Tipo de Pagamento e os
	 * adicionando ao ItemComparator
  	 */		
	if($('cdFornecedor').value > 0) {
		objects   += 'cdfrncd=sol.dao.ItemComparator(const cdFornecedor:String,const '+$('cdFornecedor').value+':String, const '+_EQUAL+':int, const '+_INTEGER+':int);';
		execute   += 'crt.add(*cdfrncd:Object);';
	}
	
	/* Gerando janela do relatório, conteúdo no formato PDF. */	
	
	var nmCargo  = $('tpRelatorio').options[$('tpRelatorio').selectedIndex].text;
	var nmPessoa = ($('cdVendedorView').value == null || $('cdVendedorView').value == "" ? "Não selecionado" : $('cdVendedorView').value);
	
	caption    = "Relatório de Vendas";
	className  = "com.tivic.manager.alm.DocumentoSaidaServices";
	method     = "gerarRelatorioVendaProdutoCompleto(const " + $('cdEmpresa').value + ":int,*crt:java.util.ArrayList, const " + $('dtSaidaInicial').value + ":GregorianCalendar, const " + $("dtSaidaFinal").value + ":GregorianCalendar)";
	nomeJasper = "relatorio_venda";	
        
	var frameHeight;
	if (top.innerHeight)
		frameHeight = top.innerHeight;
	else if (document.documentElement && document.documentElement.clientHeight)
		frameHeight = document.documentElement.clientHeight;
	else if (document.body)
		frameHeight = document.body.clientHeight;
		
	var frameWidth;
	if (top.innerWidth)
		frameWidth = top.innerWidth;
	else if (document.documentElement && document.documentElement.clientWidth)
		frameWidth = document.documentElement.clientWidth;
	else if (document.body)
		frameWidth = document.body.clientWidth;

	parent.createWindow('jRelatorioSaida', {caption: caption, width: frameWidth-20, height: frameHeight-50,
        contentUrl: "../ireport2.jsp?tpLocalizacao=1&className="+ className +
					 "&method="		+ method +
					 "&nomeJasper=" + nomeJasper +
					 "&objects=" + objects +
					 "&execute=" + execute +
					 "&cdEmpresa=" 	+ $('cdEmpresa').value + 
					 "&p=dtSaidaInicial|" + $('dtSaidaInicial').value + "-=-dtSaidaFinal|" + $('dtSaidaFinal').value + "-=-nmTurnos|" + nmTurnos + "-=-nmCargo|" + nmCargo + "-=-nmPessoa|" + nmPessoa +
					 "&modulo=pcb"});
	
}

function btFecharOnClick(content) {
	parent.closeWindow('jRelatorioVendasSupervisor');
}

function btnFindPessoaOnClick(reg, options){
    if(!reg){
		var gnPessoa = options==null || options['gnPessoa']==null ? -1 : options['gnPessoa'];
		var title = options==null || options['title']==null ? 'Localizar Cadastro Geral' : options['title'];
        var filterFields = [[{label:"Nome", reference:"NM_PESSOA", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:100, charcase:'uppercase'}]];
		if (gnPessoa==-1) {
			filterFields.push([{label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:18, charcase:'uppercase'},
							   {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:18, charcase:'uppercase'}, 
							   {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:18, charcase:'uppercase'}, 
							   {label:"Cidade", reference:"U.nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:46, charcase:'uppercase'}]);
		}
		else {
			var lineFields = [];
			if (gnPessoa == <%=PessoaServices.TP_FISICA%>) {
				lineFields = [{label:"CPF", reference:"NR_CPF", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'},
							  {label:"Identidade", reference:"NR_RG", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}];
			}
			else {
				lineFields = [{label:"Razão Social", reference:"NM_RAZAO_SOCIAL", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:30, charcase:'uppercase'},
                           	  {label:"CNPJ", reference:"NR_CNPJ", datatype:_VARCHAR, comparator:_EQUAL, width:20, charcase:'uppercase'}];
			}
			lineFields.push({label:"Cidade", reference:"U.nm_cidade", datatype:_VARCHAR, comparator:_LIKE_BEGIN, width:gnPessoa == <%=PessoaServices.TP_FISICA%> ? 60 : 50, charcase:'uppercase'});
			filterFields.push(lineFields);
		}
        var columnsGrid = [];
		columnsGrid.push({label:"Nome", reference:"NM_PESSOA"});
    	columnsGrid.push({label:"Telefone", reference:"NR_TELEFONE1", type:GridOne._MASK, mask:'(##)####-####'});
        columnsGrid.push({label:"Cidade", reference:"NM_CIDADE"});
		if (gnPessoa==-1 || gnPessoa == <%=PessoaServices.TP_JURIDICA%>) {
			columnsGrid.push({label:"Razão Social", reference:"NM_RAZAO_SOCIAL"});
			columnsGrid.push({label:'CNPJ', reference:'NR_CNPJ', type:GridOne._CNPJ});
		}
		if (gnPessoa==-1 || gnPessoa == <%=PessoaServices.TP_FISICA%>) {
			columnsGrid.push({label:'CPF', reference:'NR_CPF', type:GridOne._CPF});
			columnsGrid.push({label:"Identidade", reference:"NR_RG"});
		}
		var hiddenFields = [];
		if (gnPessoa!=-1){
	        hiddenFields.push({reference:"A.gn_pessoa", value:gnPessoa, comparator:_EQUAL, datatype:_INTEGER});
	        if ( $("tpRelatorio").value == 1 ){
	        	hiddenFields.push({reference:"findSupervisor", value: true, comparator:_EQUAL, datatype:_BOOLEAN});
	        }
		}
		filterWindow = FilterOne.create("jFiltro", {caption: title, width: 600, height: 340, modal: true, noDrag: true,
												   className: "com.tivic.manager.grl.PessoaServices", method: "find",
												   filterFields: filterFields,
												   gridOptions: {columns: columnsGrid, strippedLines: true, columnSeparator: false, lineSeparator: false},
												   hiddenFields: hiddenFields,
												   callback: btnFindPessoaOnClick, 
												   callbackOptions: options,
												   autoExecuteOnEnter: true });
    }
    else {
		closeWindow('jFiltro');
		var target = options==null || options['target']==null ? 0 : options['target'];
		switch(target) {
			case 0: 
				$('cdCliente').value = reg[0]['CD_PESSOA'];
				$('cdClienteView').value = reg[0]['NM_PESSOA'];
				break;
			case 1: 
				$('cdVendedor').value = reg[0]['CD_PESSOA'];
				$('cdVendedorView').value = reg[0]['NM_PESSOA'];
				break;
			case 2: 
				$('cdTransportadora').value = reg[0]['CD_PESSOA'];
				$('cdTransportadoraView').value = reg[0]['NM_PESSOA'];
				break;
			case 3: 
				$('cdFornecedor').value     = reg[0]['CD_PESSOA'];
				$('cdFornecedorView').value = reg[0]['NM_PESSOA'];
				break;
		}
    }
}

function btnClearPessoaOnClick(options){
	var target = options==null || options['target']==null ? 0 : options['target'];
	switch(target) {
		case 0: 
			$('cdCliente').value     = 0;
			$('cdClienteView').value = '';
			break;
		case 1: 
			$('cdVendedor').value     = 0;
			$('cdVendedorView').value = '';
			break;
		case 2: 
			$('cdTransportadora').value     = 0;
			$('cdTransportadoraView').value = '';
			break;
		case 3: 
			$('cdFornecedor').value     = 0;
			$('cdFornecedorView').value = '';
			break;
    }
}

function btnFindProdutoServicoOnClick(reg){
    if(!reg){
        FilterOne.create("jFiltro",{
        	caption:"Localizar Produtos", width: 650, height: 370, modal: true, noDrag: true,
        	className: "com.tivic.manager.grl.ProdutoServicoEmpresaServices", method: "findProdutosOfEmpresa", allowFindAll: true,
        	filterFields: [[{label:"Nome", reference:"NM_PRODUTO_SERVICO", datatype:_VARCHAR, comparator:_LIKE_ANY, width:100, charcase:'uppercase'}],
        	               [{label:"Fabricante", reference:"nm_pessoa", datatype:_VARCHAR, comparator:_LIKE_ANY, width:65, charcase:'uppercase'},
        	                {label:"ID/código", reference:"id_produto_servico", datatype:_VARCHAR, comparator:_LIKE_ANY, width:20, charcase:'uppercase'},
        	                {label:"ID/cód. reduzido", reference:"id_reduzido", datatype:_VARCHAR, comparator:_LIKE_ANY, width:15, charcase:'uppercase'}]],
        	                gridOptions: {columns: [{label:"ID/cód. reduzido", reference:"id_reduzido"},
        	                                        {label:"Nome", reference:"CL_NOME"},
        	                                        {label:"Fabricante", reference:"CL_FABRICANTE"},
        	                                        {label:"Unidade", reference:"SG_UNIDADE_MEDIDA"},
        	                                        {label:"ID/código", reference:"ID_PRODUTO_SERVICO"}],
        	                                        strippedLines: true, columnSeparator: false, lineSeparator: false,
        	                                        onProcessRegister: function(reg) {
        	                                        	// Fabricante
        	                                        	reg['CL_FABRICANTE'] = reg['SG_FABRICANTE']!=null && reg['SG_FABRICANTE']!='' ? reg['SG_FABRICANTE'] : reg['NM_FABRICANTE'];
        	                                        	if(reg['CL_FABRICANTE'] == reg['NM_FABRICANTE'] && reg['NM_FABRICANTE']!=null)	{
        	                                        		if(reg['NM_FABRICANTE'].indexOf('-')>0)
        	                                        			reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf('-')));
        	                                        		else if(reg['NM_FABRICANTE'].indexOf(' ')>0)
        	                                        			reg['CL_FABRICANTE'] = trim(reg['NM_FABRICANTE'].substring(0, reg['NM_FABRICANTE'].indexOf(' ')));
        	                                        		}
        	                                        	
        	                                        	reg['CL_NOME'] = reg['NM_PRODUTO_SERVICO'];
        	                                        	// Cor
        	                                        	if(reg['TXT_ESPECIFICACAO']!=null && reg['TXT_ESPECIFICACAO']!='')
        	                                        		reg['CL_NOME'] += ' - '+reg['TXT_ESPECIFICACAO'].toUpperCase();
        	                                        	// Tamanho
        	                                        	if(reg['TXT_DADO_TECNICO']!=null && reg['TXT_DADO_TECNICO']!='')
        	                                        		reg['CL_NOME'] += ' - '+reg['TXT_DADO_TECNICO'].toUpperCase();
        	                                        	}},
        	                                        	
        	                                        	hiddenFields: [{reference:"TP_PRODUTO_SERVICO",value:<%=ProdutoServicoServices.TP_PRODUTO%>, comparator:_EQUAL,datatype:_INTEGER}],
        	                                        	callback: btnFindProdutoServicoOnClick, autoExecuteOnEnter: true });
        } else {// retorno
        	closeWindow('jFiltro');
            $('cdProdutoServico').value = reg[0]['CD_PRODUTO_SERVICO'];
            $('nmProdutoServico').value = reg[0]['CL_NOME'];

        	
        	if($('nmProdutoServico').value != ""){
            	blockGrid('divGridCatProdutos', true);
            	blockGrid('divGridSubCatProdutos', true);
            }
        }
}

function btnClearProdutoServicoOnClick(){
	$('cdProdutoServico').value = 0;
	$('nmProdutoServico').value = '';
	blockGrid('divGridCatProdutos', false);
	blockGrid('divGridSubCatProdutos', false);
}

function blockGrid(id, act){
	if(act == true) {
		var checkboxes = document.querySelectorAll("#"+id+" input[type=checkbox]");
		var c;
		for(c=0;c<checkboxes.length;c++){
// 			checkboxes[c].checked = false;
			checkboxes[c].disabled = true;		
		}
	} else {
		var checkboxes = document.querySelectorAll("#"+id+" input[type=checkbox]");
		var c;
		for(c=0;c<checkboxes.length;c++){
			checkboxes[c].disabled = false;		
		}
	}	
}
</script>
</head>
<body class="body" onload="init();">
	<div style="width: 940px;" id="relatorioSaida" class="d1-form">
		<div style="width: 930px; height: 505px;" class="d1-body" id="divBody">
			<input idform="" reference="cd_nota_fiscal_grid_temp"
				id="cdNotaFiscalGridTemp" name="cdNotaFiscalGridTemp" type="hidden" />
			<input column="A.cd_empresa" relation="<%=ItemComparator.EQUAL%>"
				sqltype="<%=Types.INTEGER%>" idform="relSaida"
				reference="cd_empresa" datatype="INT" id="cdEmpresa"
				name="cdEmpresa" type="hidden" value="<%=cdEmpresa%>"
				defaultvalue="<%=cdEmpresa%>" />
			<div class="d1-toolBar" id="toolBarRelatorio"
				style="width: 113px; height: 125px; float: left;"></div>
			<!-- <div class="element" id="divTabRelatorio" style="margin-top:3px;"></div> -->
			<div id="divAbaFiltro"
				style="border: 1px solid #999; padding: 2px 2px 2px 4px; height: 120px; margin-bottom: 2px; width: 780px; margin-left: 5px; float: left;">
				<div class="d1-line" id="line0">
					<div style="width: 170px;" class="element">
						<label class="caption" for="tpRelatorio"> Relatório por</label> <select
							onchange="" style="width: 165px;" class="select" datatype="INT"
							id="tpRelatorio" name="tpRelatorio">
							<option value="0">Vendedor</option>
							<option value="1">Supervisor</option>
						</select>
					</div>
					<div style="width: 210px;" class="element">
						<label class="caption" for="cdTipoOperacao">Tipo
							Opera&ccedil;&atilde;o</label> <select nullvalue="0"
							column="A.cd_tipo_operacao" relation="<%=ItemComparator.EQUAL%>"
							sqltype="<%=Types.INTEGER%>" style="width: 205px;" class="select"
							titlefield="Empresa" idform="relSaida" reference="cd_empresa"
							datatype="INT" id="cdTipoOperacao" name="cdTipoOperacao">
							<option value="0">Todas</option>
						</select>
					</div>
					<div style="width: 85px;" class="element">
						<label for="dtSaidaInicial" class="caption"
							style="overflow: visible">Período Saida</label> <input
							column="A.dt_documento_saida"
							relation="<%=ItemComparator.GREATER_EQUAL%>"
							sqltype="<%=Types.TIMESTAMP%>" name="dtSaidaInicial" type="text"
							idform="relSaida" datatype="DATE" class="field"
							id="dtSaidaInicial" mask="##/##/####" maxlength="10"
							style="width: 77px;" value="" />
						<button
							onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')"
							title="Selecionar data..." reference="dtSaidaInicial"
							class="controlButton">
							<img alt="|30|" src="/sol/imagens/date-button.gif">
						</button>
					</div>
					<div style="width: 85px; margin-left: 3px" class="element">
						<label for="dtSaidaFinal" class="caption">&nbsp;</label> <input
							ignoretime="true" column="A.dt_documento_saida"
							relation="<%=ItemComparator.MINOR_EQUAL%>"
							sqltype="<%=Types.TIMESTAMP%>" name="dtSaidaFinal" type="text"
							idform="relSaida" class="field" datatype="DATE" id="dtSaidaFinal"
							mask="##/##/####" maxlength="10" style="width: 77px;" value="" />
						<button
							onclick="return showCalendar(this.getAttribute('reference'), '%d/%m/%Y', null , 'Bl')"
							title="Selecionar data..." reference="dtSaidaFinal"
							class="controlButton">
							<img alt="|30|" src="/sol/imagens/date-button.gif">
						</button>
					</div>
					<div style="width: 100px; margin-left: 3px" class="element">
						<label class="caption" for="tpMovimentoEstoque">Tipo
							Movimento</label> <select nullvalue="-1" column="A.tp_movimento_estoque"
							relation="<%=ItemComparator.EQUAL%>"
							sqltype="<%=Types.SMALLINT%>" style="width: 95px;" class="select"
							idform="relSaida" reference="cd_empresa" datatype="INT"
							id="tpMovimentoEstoque" name="tpMovimentoEstoque">
							<option value="-1">Todos</option>
						</select>
					</div>
				</div>
				<div class="d1-line" id="line2">
					<div style="width: 240px;" class="element">
						<label class="caption" for="cdCliente">Cliente</label> <input
							nullvalue="0" column="A.cd_cliente"
							relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>"
							logmessage="Código Cliente" idform="relSaida"
							reference="cd_cliente" datatype="INT" id="cdCliente"
							name="cdCliente" type="hidden" value="0" defaultvalue="0" /> <input
							style="width: 235px;" logmessage="Nome Cliente" idform="relSaida"
							reference="nm_cliente" static="true" disabled="disabled"
							class="disabledField" name="cdClienteView" id="cdClienteView"
							type="text" />
						<button id="btnFindCliente"
							onclick="btnFindPessoaOnClick(null, {target: 0, gnPessoa: -1, title: 'Localizar Cadastro Geral'})"
							idform="relSaida" title="Pesquisar valor para este campo..."
							class="controlButton controlButton2">
							<img alt="L" src="/sol/imagens/filter-button.gif">
						</button>
						<button onclick="btnClearPessoaOnClick({target: 0})"
							idform="relSaida" title="Limpar este campo..."
							class="controlButton" onfocus="">
							<img alt="X" src="/sol/imagens/clear-button.gif">
						</button>
					</div>
					<div style="width: 137px; margin-left: 3px" class="element">
						<label class="caption" for="stDocumentoSaida">Situa&ccedil;&atilde;o</label>
						<select nullvalue="-1" column="A.st_documento_saida"
							relation="<%=ItemComparator.EQUAL%>"
							sqltype="<%=Types.SMALLINT%>" style="width: 132px;"
							class="select" idform="relSaida" reference="cd_empresa"
							datatype="INT" id="stDocumentoSaida" name="stDocumentoSaida">
							<option value="-1">Todas</option>
						</select>
					</div>
				</div>
				<div class="d1-line" id="line0">
					<div style="width: 110px;" class="element">
						<label class="caption" for="tpFrete">Frete por conta</label> <select
							nullvalue="-1" column="A.tp_frete"
							relation="<%=ItemComparator.EQUAL%>"
							sqltype="<%=Types.SMALLINT%>" style="width: 105px;"
							class="select" idform="relSaida" reference="cd_empresa"
							datatype="INT" id="tpFrete" name="tpFrete">
							<option value="-1">Todos</option>
						</select>
					</div>
					<div style="width: 220px;" class="element">
						<label class="caption" for="cdVendedor">Vendedor /
							Supervisor</label> <input nullvalue="0" column="A.cd_vendedor"
							relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>"
							logmessage="Código Vendedor" idform="relSaida"
							reference="cd_cliente" datatype="INT" id="cdVendedor"
							name="cdVendedor" type="hidden" value="0" defaultvalue="0" /> <input
							style="width: 185px;" logmessage="Nome Vendedor/Supervisor"
							idform="relSaida" reference="nm_vendedor" static="true"
							disabled="disabled" class="disabledField" name="cdVendedorView"
							id="cdVendedorView" type="text" />
						<button id="btnFindVendedor"
							onclick="btnFindPessoaOnClick(null, {target: 1, gnPessoa: <%=PessoaServices.TP_FISICA%>, title: 'Localizar Vendedores'})"
							idform="relSaida" title="Pesquisar valor para este campo..."
							class="controlButton controlButton2">
							<img alt="L" src="/sol/imagens/filter-button.gif">
						</button>
						<button onclick="btnClearPessoaOnClick({target: 1})"
							idform="relSaida" title="Limpar este campo..."
							class="controlButton" onfocus="">
							<img alt="X" src="/sol/imagens/clear-button.gif">
						</button>
					</div>
					<div style="width: 220px;" class="element">
						<label class="caption" for="cdTransportadora">Transportadora</label>
						<input nullvalue="0" column="A.cd_transportadora"
							relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>"
							logmessage="Código Vendedor" idform="relSaida"
							reference="cd_cliente" datatype="INT" id="cdTransportadora"
							name="cdTransportadora" type="hidden" value="0" defaultvalue="0" />
						<input style="width: 185px;" logmessage="Nome Vendedor"
							idform="relSaida" reference="nm_cliente" static="true"
							disabled="disabled" class="disabledField"
							name="cdTransportadoraView" id="cdTransportadoraView" type="text" />
						<button id="btnFindVendedor"
							onclick="btnFindPessoaOnClick(null, {target: 2, gnPessoa: <%=PessoaServices.TP_JURIDICA%>, title: 'Localizar Fornecedores'})"
							idform="relSaida" title="Pesquisar valor para este campo..."
							class="controlButton controlButton2">
							<img alt="L" src="/sol/imagens/filter-button.gif">
						</button>
						<button onclick="btnClearPessoaOnClick({target: 2})"
							idform="relSaida" title="Limpar este campo..."
							class="controlButton" onfocus="">
							<img alt="X" src="/sol/imagens/clear-button.gif">
						</button>
					</div>
					<div style="width: 223px; margin-left: 3px" class="element">
						<label class="caption">Último Fornecedor</label> <input
							nullvalue="0" column="cdFornecedor"
							relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>"
							logmessage="Código Fornecedor" idform="relSaida"
							reference="cd_fornecedor" datatype="INT" id="cdFornecedor"
							name="cdFornecedor" type="hidden" value="0" defaultvalue="0" />
						<input style="width: 188px;" logmessage="Nome Fornecedor"
							reference="nm_fornecedor" static="true" disabled="disabled"
							class="disabledField" name="cdFornecedorView"
							id="cdFornecedorView" type="text" />
						<button id="btnFindFornecedor"
							onclick="btnFindPessoaOnClick(null, {target: 3, gnPessoa: <%=PessoaServices.TP_JURIDICA%>, title: 'Localizar Fornecedores'})"
							idform="relSaida" title="Pesquisar valor para este campo..."
							class="controlButton controlButton2">
							<img alt="L" src="/sol/imagens/filter-button.gif">
						</button>
						<button onclick="btnClearPessoaOnClick({target: 3})"
							idform="relSaida" title="Limpar este campo..."
							class="controlButton" onfocus="">
							<img alt="X" src="/sol/imagens/clear-button.gif">
						</button>
					</div>
				</div>
				<div class="d1-line" id="line2">
					<div style="width: 110px;" class="element">
						<label class="caption" for="cdFormaPagamento">Espécie</label> <select
							nullvalue="-1" column="O.tp_forma_pagamento"
							relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>"
							style="width: 105px;" class="select" idform="relSaida"
							reference="O.cd_forma_pagamento" datatype="INT"
							id="tpFormaPagamento" name="tpFormaPagamento">
							<option value="-1">Todas</option>
							<option value="0">Moeda Corrente</option>
							<option value="1">TEF (Cartões)</option>
							<option value="2">Título de Crédito</option>
						</select>
					</div>
					<div style="width: 250px;" class="element">
						<label class="caption" for="nmProdutoServico">Produto </label> <input
							nullvalue="0" column="B.cd_produto_servico"
							relation="<%=ItemComparator.EQUAL%>" sqltype="<%=Types.INTEGER%>"
							datatype="STRING" idform="relSaida" id="cdProdutoServico"
							name="cdProdutoServico" type="hidden" value="0" defaultValue="0">
							<input style="width: 225px;" static="true" idform="relSaida"
							disabled="disabled" readonly="readonly" class="disabledField"
							name="nmProdutoServico" id="nmProdutoServico" type="text"
							value="" defaultValue="" />
							<button onclick="btnFindProdutoServicoOnClick()"
								idform="relSaida" title="Pesquisar valor para este campo..."
								class="controlButton controlButton2">
								<img alt="L" src="/sol/imagens/filter-button.gif">
							</button>
							<button onclick="btnClearProdutoServicoOnClick()"
								idform="relSaida" title="Limpar este campo..."
								class="controlButton">
								<img alt="X" src="/sol/imagens/clear-button.gif">
							</button>
					</div>
				</div>
			</div>
			<div id="divGridArea"
				style="border: 0px solid #999; height: auto; margin-bottom: 2px; width: 220px; margin-left: 5px; float: left;">
				<div id="ckGridOptions" class="element"
					style="width: 185px; margin-top: 13px;">
					<input type="checkbox" name="ckAllOption" id="ckAllOption"
						datatype="STRING" idform="print"> <label class="caption" style="display: inline;">Todos</label>
				</div>
				<div id="divGridCatProdutos"
					style="width: 220px; height: 160px; border: 1px solid rgb(153, 153, 153);; background-color: #FFF;"
					class="element"></div>
			</div>
			<div id="divGridArea"
				style="border: 0px solid #999; height: auto; margin-bottom: 2px; width: 220px; margin-left: 5px; float: left;">
				<div id="ckGridOptions" class="element"
					style="width: 185px; margin-top: 13px;">
					<input type="checkbox" name="ckAllOption" id="ckAllOption"
						datatype="STRING" idform="print"> <label class="caption" style="display: inline;">Todos</label>
				</div>
				<div id="divGridSubCatProdutos"
					style="width: 220px; height: 160px; border: 1px solid rgb(153, 153, 153);; background-color: #FFF;"
					tabin class="element"></div>
			</div>
			<div id="divGridArea"
				style="border: 0px solid #999; height: auto; margin-bottom: 2px; width: 220px; margin-left: 5px; float: left;">
				<div id="ckGridOptions" class="element"
					style="width: 185px; margin-top: 13px;">
					<input type="checkbox" name="ckAllOption" id="ckAllOption"
						datatype="STRING" idform="print"> <label class="caption" style="display: inline;">Todos</label>
				</div>
				<div id="divGridFormaPagamento"
					style="width: 220px; height: 160px; border: 1px solid rgb(153, 153, 153);; background-color: #FFF;"
					tabin class="element"></div>
			</div>
			<div id="divGridArea"
				style="border: 0px solid #999; height: auto; margin-bottom: 2px; width: 220px; margin-left: 5px; float: left;">
				<div id="ckGridOptions" class="element"
					style="width: 185px; margin-top: 13px;">
					<input type="checkbox" name="ckAllOption" id="ckAllOption"
						datatype="STRING" idform="print"> <label class="caption" style="display: inline;">Todos</label>
				</div>
				<div id="divGridPlanPagamento"
					style="width: 220px; height: 160px; border: 1px solid rgb(153, 153, 153);; background-color: #FFF;"
					tabin class="element"></div>
			</div>
			<div id="divGridArea"
				style="border: 0px solid #999; height: auto; margin-bottom: 2px; width: 445px; margin-left: 5px; float: left;">
				<div id="ckGridOptions" class="element"
					style="width: 185px; margin-top: 13px;">
					<input type="checkbox" name="ckAllOption" id="ckAllOption"
						datatype="STRING" idform="print"> <label class="caption" style="display: inline;">Todos</label>
				</div>
				<div id="divGridTurnos"
					style="width: 445px; height: 160px; border: 1px solid rgb(153, 153, 153);; background-color: #FFF;"
					tabin class="element"></div>
			</div>
			<div id="divGridArea"
				style="border: 0px solid #999; height: auto; margin-bottom: 2px; width: 220px; margin-left: 5px; float: left;">
				<div id="ckGridOptions" class="element"
					style="width: 185px; margin-top: 13px;">
					<input type="checkbox" name="ckAllOption" id="ckAllOption"
						datatype="STRING" idform="print"> <label class="caption" style="display: inline;">Todos</label>
				</div>
				<div id="divGridTpSaida"
					style="width: 220px; height: 160px; border: 1px solid rgb(153, 153, 153);; background-color: #FFF;"
					tabin class="element"></div>
			</div>
			<div id="divGridArea"
				style="border: 0px solid #999; height: auto; margin-bottom: 2px; width: 220px; margin-left: 5px; float: left;">
				<div id="ckGridOptions" class="element"
					style="width: 185px; margin-top: 13px;">
					<input type="checkbox" name="ckAllOption" id="ckAllOption"
						datatype="STRING" idform="print"> <label class="caption" style="display: inline;">Todos</label>
				</div>
				<div id="divGridTpDoc"
					style="width: 220px; height: 160px; border: 1px solid rgb(153, 153, 153);; background-color: #FFF;"
					tabin class="element"></div>
			</div>
			<script>
			jQuery(function(){
				$jQ("#divGridArea > #ckGridOptions input[name=ckAllOption]").click(function(){
					var parentElement = $jQ(this).parent().parent();
					var nextElement   = $jQ(this).parent().next();
					var prevElement   = $jQ(this).parent().parent().prev();
					
// 					console.log('             START DEBUG             ');
// 					console.log('-------------------------------------');
// 					console.log(prevElement.attr('id'));
// 					console.log(parentElement.attr('id'));
// 					console.log('-------------------------------------');
// 					console.log('              END DEBUG              ');
					
					if($jQ(this).prop("checked")){
						$jQ('input', nextElement).each(function(){
							this.checked = true;
						});
						
						if(prevElement.attr('id') != parentElement.attr('id')){
							$jQ('#divGridArea > #divGridSubCatProdutos input').each(function(){
								this.checked = true;;
								$jQ("input[name=ckAllOption]:eq(1)").prop("checked", true);
							});
						}
						
					} else {
						$jQ('input', nextElement).each(function(){
							this.checked = false;
						});
						if(prevElement.attr('id') != parentElement.attr('id')){
							$jQ('#divGridArea > #divGridSubCatProdutos input').each(function(){
								this.checked = false;
								$jQ("input[name=ckAllOption]:eq(1)").prop("checked", false);
							});
						}
					}
				});
			});
			</script>
		</div>
	</div>
</body>
</html>