<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>IPTU - DAM WEB</title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<link href="css/form.css" rel="stylesheet" type="text/css"/>
<link href="css/janela.css" rel="stylesheet" type="text/css"/>
<%@taglib uri="../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="toolbar, shortcut, form, filter, calendario" compress="false"/>
<%
	String nrInscricao  = sol.util.RequestUtilities.getAsString(request, "nrInscricao", "");
	int lgParametros    = sol.util.RequestUtilities.getAsInteger(request, "lgParametros", 0);
	if(lgParametros==2)	{
		out.clear();
		sol.util.Result result = com.tivic.manager.egov.IptuServices.getLista();
		if(result.getCode() >= 0)	{
	response.setContentType("application/octet-stream"); 
	response.setHeader("Content-Disposition","attachment; filename=WEBDAM_Lista.txt"); 
	out.write((String)result.getObjects().get("file"));
		}
		else	{
%>
		<script language="javascript">
			alert('<%=result.getMessage()%>');
		</script>
	<%}
	}
	else	{
		///////////////////////////////////////////
		// PARAMETROS
		///////////////////////////////////////////
		// Ano Base
		int nrAnoBase   = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("NR_ANO_BASE", 2011);
		// Data Limite Desconto
		String dtLimiteDesconto = com.tivic.manager.grl.ParametroServices.getValorOfParametro("DT_LIMITE_DESCONTO");
		dtLimiteDesconto = dtLimiteDesconto==null ? "" : dtLimiteDesconto;
		// Multa
		float prMulta = 0; 
		try	{
			prMulta = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("PR_MULTA_DAM_IPTU"));
		}catch(Exception e){};
		// Juros
		float prJuros = 0;
		try	{
			prJuros = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("PR_JUROS_DAM_IPTU"));
		}catch(Exception e){};
		// % Desconto da Cota Única
		float prDescontoCotaUnica = 0;
		try	{
			prDescontoCotaUnica = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("PR_DESCONTO_COTA_UNICA"));
		}catch(Exception e){};
		// Taxa de Desconto
		float vlTaxaExpediente = 0; 
		try	{
			vlTaxaExpediente = Float.parseFloat(com.tivic.manager.grl.ParametroServices.getValorOfParametro("VL_TAXA_EXPEDIENTE"));
		}catch(Exception e){};
		// Parametros do Banco de Dados
		String urlDB    = com.tivic.manager.grl.ParametroServices.getValorOfParametro("URL_BD_NFE"); 
		String driverDB = com.tivic.manager.grl.ParametroServices.getValorOfParametro("DRIVER_BD_NFE"); 
		String userDB   = com.tivic.manager.grl.ParametroServices.getValorOfParametro("USER_BD_NFE");
		String passDB   = com.tivic.manager.grl.ParametroServices.getValorOfParametro("PASS_BD_NFE");
		// Sem Uso
		int lgPermiteDamSemCpf    = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("LG_PERMITE_DAM_SEM_CPF", 0); 
		int lgSomenteParcelaUnica = com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("LG_SOMENTE_PARCELA_UNICA", 0);
%>
<script language="javascript">
var zIndexWindow = 100;
var METHODCALLER_PATH = '../methodcaller';

function init()	{
	createWindow('jIPTU', {caption: ' ', width: 658, height: 450, noTitle: true, noDrag: true, modal: true, contentDiv: 'iptu'});
<%	if(lgParametros!=1) {%>  
	document.getElementById('nrInscricao').onblur = nrInscricaoOnBlur;
	var nrInscricaoMask = new Mask(document.getElementById("nrInscricao").getAttribute("mask"));
	nrInscricaoMask.attach(document.getElementById("nrInscricao"));
	document.getElementById('nrInscricao').value = '<%=nrInscricao%>';
	document.getElementById('nrInscricao').focus();
<%	}
	else	{%>
	document.getElementById('prMulta').value             = <%=prMulta%>; // formatCurrency(<%=prMulta%>);
	document.getElementById('prJuros').value             = <%=prJuros%>; // formatCurrency(<%=prJuros%>);
	document.getElementById('vlTaxaExpediente').value    = <%=vlTaxaExpediente%>;// formatCurrency(<%=vlTaxaExpediente%>);
	document.getElementById('prDescontoCotaUnica').value = <%=prDescontoCotaUnica%>;// formatCurrency(<%=prDescontoCotaUnica%>);
	document.getElementById('driverDB').value            = '<%=driverDB%>';
<%	}%>		
	 enableTabEmulation();
}

function nrInscricaoOnBlur(){
	if(document.getElementById('nrInscricao').value!='')	{
		loadPessoaByCpf();
	}
}

function loadPessoaByCpf(content) {
	clearAll();
	if(!document.getElementById('nrInscricao').value){
		return;
	}
	if (content==null) {
		createTempbox("jMsg", {width: 250, height: 45, message: "Pesquisando imóvel ...", boxType: "LOADING", time: 2000});
		
        getPage("POST", "loadPessoaByCpf", 
				"../methodcaller?className=com.tivic.manager.egov.IptuServices"+
				"&method=getDadosOfInscricao(nrInscricao:String,const 00169868575:String,const <%=nrAnoBase%>:int)",[document.getElementById('nrInscricao')]);
	}
	else {
		//closeWindow("jMsg");
		var rsm = eval('(' + content + ')');
		if(rsm && rsm.lines.length>0)	{
			document.getElementById('nmPessoa').value       = rsm.lines[0]['NMCONTRIBUINTE'];  
			document.getElementById('vlAreaTerreno').value  = formatCurrency(rsm.lines[0]['VLAREATERRENO']);  
			document.getElementById('vlM2Terreno').value    = formatCurrency(rsm.lines[0]['VLM2TERRENO']);  
			document.getElementById('vlTerreno').value      = formatCurrency(rsm.lines[0]['VLTERRENO']);  
			document.getElementById('vlM2Construcao').value = formatCurrency(rsm.lines[0]['VLM2CONSTRUCAO']);  
			document.getElementById('vlConstrucao').value   = formatCurrency(rsm.lines[0]['VLCONSTRUCAO']);  
			document.getElementById('vlVenal').value        = formatCurrency(rsm.lines[0]['VLVENAL']);
			document.getElementById('vlFatorCorrTerreno').value    = formatCurrency(rsm.lines[0]['VLFATORCORRTERRENO']);  
			document.getElementById('vlFatorCorrConstrucao').value = formatCurrency(rsm.lines[0]['VLFATORCORRCONSTRUCAO']);  
			document.getElementById('tpCategoria').value    = rsm.lines[0]['TPCATEGORIA'];  
			document.getElementById('nrLoteamento').value   = rsm.lines[0]['NRLOTEAMENTO'];  
			document.getElementById('nrQuadra').value       = rsm.lines[0]['NRQUADRA'];  
			document.getElementById('nrLote').value         = rsm.lines[0]['NRLOTE'];  
		}
		else	{
			alert('Número de inscrição não localizado!');
			clearAll();
			return;
		}
		document.getElementById('nrParcela').options.length = 1;
		loadOptionsFromRsm(document.getElementById('nrParcela'), rsm, {fieldValue: 'INDICEPARCELA', fieldText: "CL_PARCELA", 
		                                         onProcessRegister: function(reg)	{
											  		reg['DTVENCIMENTO'] = reg['DTVENCIMENTO'].substring(0,2)+'/'+reg['DTVENCIMENTO'].substring(2,4)+
											  		                      '/'+reg['DTVENCIMENTO'].substring(4,8);  
							                    	reg['CL_PARCELA'] = (reg['INDICEPARCELA']==0?'Parcela Única': 'Parcela Nº '+reg['INDICEPARCELA'])+
							                    	                    ' - '+reg['DTVENCIMENTO']+' - '+formatCurrency(reg['VLPARCELA']/100);
		                     					 }
		                    					});
		document.getElementById('nrParcela').value = 0;
		if(rsm && rsm.lines.length>0)
			btnDamOnClick();
		gridParcelas = GridOne.create('gridParcelas',{plotPlace: document.getElementById('divGridParcelas'), 
													  resultset: rsm,
													  onProcessRegister: function(reg)	{
													  		reg['VLPARCELA'] = reg['VLPARCELA'] / 100;
													  		reg['DTVENCIMENTO'] = reg['DTVENCIMENTO'].substring(0,2)+'/'+reg['DTVENCIMENTO'].substring(2,4)+
													  		                      '/'+reg['DTVENCIMENTO'].substring(4,8);  
													  },
                                                      columns: [{label: 'Nº Parcela', reference: 'INDICEPARCELA'},
							            						{label: 'Vencimento', reference: 'DTVENCIMENTO'},
							            						{label: 'Valor', reference: 'VLPARCELA', type: GridOne._CURRENCY}]});
	}
}

function clearAll()	{
	document.getElementById('nmPessoa').value       = '';  
	document.getElementById('vlAreaTerreno').value  = '';  
	document.getElementById('vlM2Terreno').value    = '';  
	document.getElementById('vlTerreno').value      = '';  
	document.getElementById('vlM2Construcao').value = '';  
	document.getElementById('vlConstrucao').value   = '';  
	document.getElementById('vlVenal').value        = '';
	document.getElementById('vlFatorCorrTerreno').value    = '';  
	document.getElementById('vlFatorCorrConstrucao').value = '';  
	document.getElementById('tpCategoria').value    = '';  
	document.getElementById('nrLoteamento').value   = '';  
	document.getElementById('nrQuadra').value       = '';  
	document.getElementById('nrLote').value         = '';  
}

function btnDamOnClick() {
	if(document.getElementById('nrParcela').value<0)	{
		// alert('Informe o número da parcela que deseja imprimir!');
		// return;
	}
	parent.createWindow('jDAM', {caption: ' ', width: Math.round(window.innerWidth-5), height: window.innerHeight, noDrag: true, modal: true,
	                             top: 0, left: 0, 
	                             contentUrl: '../report/preview.jsp?nrInscricao='+document.getElementById('nrInscricao').value+
	                                            '&nrParcela=0'});
}

function btnSaveParametroOnClick(content) {
	if (content==null) {
        getPage("POST", "btnSaveParametroOnClick", 
				"../methodcaller?className=com.tivic.manager.egov.IptuServices"+
				"&method=saveParametros(const "+changeLocale('prMulta')+":float,const "+changeLocale('prJuros')+":float,"+
				"const "+changeLocale('vlTaxaExpediente')+":float,const "+changeLocale('prDescontoCotaUnica')+":float,"+
				"lgPermiteDamSemCpf:int,lgSomenteParcelaUnica:int,nrAnoBase:int,dtLimiteDesconto:String,urlDB:String,driverDB:String,userDB:String,passDB:String)", 
			    [document.getElementById('lgPermiteDamSemCpf'),document.getElementById('lgSomenteParcelaUnica'),document.getElementById('urlDB'),
			     document.getElementById('nrAnoBase'),document.getElementById('dtLimiteDesconto'),
			     document.getElementById('driverDB'),document.getElementById('userDB'),document.getElementById('passDB')]);
	}
	else {
		var ret = processResult(content, 'Salvo com sucesso');
		if(ret.code > 0)
			alert('Salvo com sucesso!');
	}
}
</script>
</head>
<iframe id='frameIPTU' name='frameIPTU' src='' style='display: none; width: 300px; height: 100px; border: 1px solid #999; z-index: 100;'></iframe>
<body class="body" onload="init();">
<a href="../index.jsp" class="back" style="bottom: 20px; left: 20px; position: fixed; text-decoration: none; color: #000;" title="Voltar ao Portal"> <img src="../imagens/arrow-left.png" style="display: block; margin: 0 auto;"></img>
	<p style="text-align: center; font-size: 11px; font-family: tahoma; margin: 5px;">Voltar ao Portal</p>
</a>
<div style="width: 650px;" id="iptu" class="d1-form">
  <div style="width: 650px; height: 440px;" class="d1-body">
<%if(lgParametros!=1) {%>  
  	<div style="width: 550px; height: 130px; float: left;">
	    <div class="d1-line">
	        <div style="width: 550px; height: 40px; text-align: center; margin-bottom: 5px;" class="element">
	            <label class="caption" style="font-size: 16px; height: 19px; font-weight: bold; color: blue;">Documento de Arrecadação Municipal - DAM</label>
	            <label class="caption" style="font-size: 16px; height: 19px; font-weight: bold; color: blue;">Imposto Territorial Urbano - IPTU</label>
	        </div>
        </div>
	    <div class="d1-line">
	        <div style="width: 550px; height: 40px; margin-bottom: 5px;" class="element">
	            <label class="caption" style="font-size: 11px; height: 39px; font-weight: bold; color: blue; white-space: normal;">
	            			Digite no campo abaixo o número de inscrição do seu imóvel, aguarde enquanto o sistema busca suas informações, em seguida selecione a parcela que deseja imprimir e clique no botão "Emitir DAM".<br/>
	        </div>
        </div>
	    <div class="d1-line">
	        <div style="width: 160px;" class="element">
	            <label class="caption">Nº Inscrição</label>
	            <input style="width: 155px;" class="field2" id="nrInscricao" name="nrInscricao" type="text" maxlength="18" mask="##.##.###.####.###"/>
	        </div>
	        <div style="width: 390px;" class="element">
	            <label class="caption" ondblclick="remontarLinhaDigital(null);">Nome do Contribuinte</label>
	            <input style="width: 385px;" maxlength="60" class="disabledField2" readonly="readonly" id="nmPessoa" name="nmPessoa" type="text"/>
	        </div>
	    </div>
	    <div class="d1-line">
	        <div style="width: 550px;" class="element">
	            <label class="caption">Localização do Imóvel</label>
	            <input style="width: 545px;" class="disabledField" readonly="readonly" id="nmLocalizacaoImovel" name="nmLocalizacaoImovel" type="text"/>
	        </div>
	    </div>
	    <div class="d1-line">
	        <div style="width: 550px;" class="element">
	            <label class="caption">Endereço para Correspondência</label>
	            <input style="width: 545px;" class="disabledField" readonly="readonly" id="nmEnderecoCorrespondencia" name="nmEnderecoCorrespondencia" type="text"/>
	        </div>
	    </div>
	    <div class="d1-line">
	        <div style="width: 137px;" class="element">
	            <label class="caption">Área do Terreno</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="vlAreaTerreno" name="vlAreaTerreno" type="text"/>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Fração Ideal</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="vlFracaoIdeal" name="vlFracaoIdeal" type="text"/>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Valor M2 Terreno</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="vlM2Terreno" name="vlM2Terreno" type="text"/>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Fator Correção Terreno</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="vlFatorCorrTerreno" name="vlFatorCorrTerreno" type="text"/>
	        </div>
	    </div>
	    <div class="d1-line">
	        <div style="width: 137px;" class="element">
	            <label class="caption">Área Construção</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="vlAreaConstrucao" name="vlAreaConstrucao" type="text"/>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Categoria</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="tpCategoria" name="tpCategoria" type="text"/>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Valor M2 Construção</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="vlM2Construcao" name="vlM2Construcao" type="text"/>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Fator Corr. Construção</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="vlFatorCorrConstrucao" name="vlFatorCorrConstrucao" type="text"/>
	        </div>
	    </div>
	    <div class="d1-line">
	        <div style="width: 137px;" class="element">
	            <label class="caption">Valor Terreno</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="vlTerreno" name="vlTerreno" type="text"/>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Valor Construção</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="vlConstrucao" name="vlConstrucao" type="text"/>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Valor Venal Imóvel</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="vlVenal" name="vlVenal" type="text"/>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Alíquota</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="vlAliquota" name="vlAliquota" type="text"/>
	        </div>
	    </div>
	    <div class="d1-line">
	        <div style="width: 137px;" class="element">
	            <label class="caption">Nº Loteamento</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="nrLoteamento" name="nrLoteamento" type="text"/>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Nº Quadra Lot.</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="nrQuadra" name="nrQuadra" type="text"/>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Nº Lote Loteamento</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="nrLote" name="nrLote" type="text"/>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Base de Cálculo p/ Taxas</label>
	            <input style="width: 134px; text-align: right;" class="disabledField" readonly="readonly" id="vlBaseCalculoTaxa" name="vlBaseCalculoTaxa" type="text"/>
	        </div>
	    </div>
	    <div class="d1-line">
	        <div style="width: 445px;" class="element">
	            <label class="caption">Parcelas</label>
	            <select style="width: 437px; height: 25px;" class="select2" id="nrParcela">
           			<option value="-1">Selecione a parcela</option>
	            </select>
	        </div>
	        <button id="btnDam" onclick="btnDamOnClick(null);" title="Clique para visualizar o DAM" class="controlButton" style="width: 100px; margin-top: 15px;"><img src="imagens/forma_contato16.gif">&nbsp Emitir DAM</button>
	    </div>
    </div>
<%	}
	else	{%>  
	    <div class="d1-line">
	        <div style="width: 650px; height: 40px; text-align: center; margin-bottom: 5px;" class="element">
	            <label class="caption" style="font-size: 16px; height: 19px; font-weight: bold; color: blue;">Documento de Arrecadação Municipal - DAM</label>
	            <label class="caption" style="font-size: 16px; height: 19px; font-weight: bold; color: blue;">Imposto Territorial Urbano - IPTU</label>
	        </div>
        </div>
	    <div class="d1-line">
	        <div style="width: 650px; height: 20px; margin-bottom: 5px;" class="element">
	            <label class="caption" style="font-size: 11px; height: 19px; font-weight: bold; color: blue; white-space: normal;">
	            			Definição de parametros:<br/>
	        </div>
        </div>
	    <div class="d1-line">
	        <div style="width: 100px;" class="element">
	            <label class="caption">Código FEBRABAN:</label>
	            <input style="width: 95px;" class="field2" id="nrFebraban" name="nrFebraban" type="text" maxlength="4" value="4785"/>
	        </div>
	        <div style="width: 70px;" class="element">
	            <label class="caption">Nº Ano Base:</label>
	            <input style="width: 65px; text-align: right;" class="field2" id="nrAnoBase" name="nrAnoBase" type="text" maxlength="4" value="<%=nrAnoBase%>" datatype="INT"/>
	        </div>
	        <div style="width: 95px;" class="element">
	            <label class="caption">Taxa Expediente:</label>
	            <input style="width: 90px; text-align: right;" class="field2" id="vlTaxaExpediente" name="vlTaxaExpediente" type="text" maxlength="4" value="0,00" datatype="FLOAT"/>
	        </div>
	        <div style="width: 110px;" class="element">
	            <label class="caption">% Desc. Cota Única:</label>
	            <input style="width: 105px; text-align: right;" class="field2" id="prDescontoCotaUnica" name="prDescontoCotaUnica" type="text" maxlength="4" value="0,00" datatype="FLOAT"/>
	        </div>
	        <div style="width: 110px;" class="element">
	            <label class="caption">Prorrogar desc. até:</label>
	            <input style="width: 105px;" class="field2" id="dtLimiteDesconto" name="dtLimiteDesconto" type="text" maxlength="10" value="<%=dtLimiteDesconto%>"/>
	        </div>
	        <div style="width: 80px;" class="element">
	            <label class="caption">% Multa/Mora:</label>
	            <input style="width: 75px; text-align: right;" class="field2" id="prMulta" name="prMulta" type="text" maxlength="4" value="5,00" datatype="FLOAT"/>
	        </div>
	        <div style="width: 80px;" class="element">
	            <label class="caption">% Juros/Mora:</label>
	            <input style="width: 78px; text-align: right;" class="field2" id="prJuros" name="prJuros" type="text" maxlength="4" value="10,00" datatype="FLOAT"/>
	        </div>
	    </div>
	    <div class="d1-line">
	        <div style="width: 648px;" class="element">
	            <label class="caption">URL para acesso ao banco de dados do IPTU</label>
	            <input style="width: 643px;" class="field2" id="urlDB" name="urlDB" type="text" value="<%=urlDB%>"/>
	        </div>
	    </div>
	    <div class="d1-line">
	        <div style="width: 374px;" class="element">
	            <label class="caption">Driver para acesso ao BD</label>
	            <select style="width: 369px; height: 22px;" class="select2" id="driverDB" name="driverDB">
           			<option value="">Selecione o Driver</option>
           			<option value="com.intersys.jdbc.CacheDriver">CACHÉ</option>
           			<option value="org.postgresql.Driver">PostgreSQL</option>
           			<option value="org.firebirdsql.jdbc.FBDriver">Firebird</option>
           			<option value="net.sourceforge.jtds.jdbc.Driver">MS-SQL Server</option>
	            </select>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Usuário:</label>
	            <input style="width: 132px;" class="field2" id="userDB" name="userDB" type="text" value="<%=userDB%>"/>
	        </div>
	        <div style="width: 137px;" class="element">
	            <label class="caption">Senha</label>
	            <input style="width: 132px;" class="field2" id="passDB" name="passDB" type="password" value="<%=passDB%>"/>
	        </div>
	    </div>
	    <div class="d1-line">
	        <div style="width: 445px;" class="element">
	            <label class="caption">&nbsp</label>
	        </div>
	        <button id="btnSaveParametro" onclick="btnSaveParametroOnClick(null);" class="controlButton" style="width: 100px; margin-top: 15px;"><img src="imagens/forma_contato16.gif">&nbsp Salvar Paramêtros</button>
	    </div>
  </div>
<%	}%>  
  </div>
</div>
</body>
</html>
	        <!--  div style="width: 545px; display: none;" class="element">
	            <label class="caption">Parcelas</label>
	            <div style="width: 545px; height: 118px; border: 1px solid #999;" class="element" readonly="readonly" id="divGridParcelas"/>
	        </div> -->
<%	}%>  
	        