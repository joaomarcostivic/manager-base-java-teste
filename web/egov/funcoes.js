var addCodAtiv = 0;
var addDescAtiv = 0;
var addNome = 0;
var addCPF = 0;

function addAtSec(id){  

     var tbody = document.getElementById(id).getElementsByTagName("TBODY")[0];  
     var row = document.createElement("TR")  
     //cria a primeiro td  
     var td1 = document.createElement("TD")  
     td1.appendChild(document.createTextNode("Código "))  
     var currentElement = document.createElement("input");  
     currentElement.setAttribute("type", "text");  
	 currentElement.setAttribute("className", "text");
	 currentElement.setAttribute("class", "text");
     currentElement.setAttribute("name", "addCodAtiv" + addCodAtiv );  
	 currentElement.setAttribute("id", "addCodAtiv"+ addCodAtiv); 
	 currentElement.setAttribute("size", 12);
	 td1.appendChild(currentElement); 
       
     //cria o segundo td  
     var td2 = document.createElement("TD")  
   
  	 td2.appendChild(document.createTextNode("Descrição ")) 
     var currentElement = document.createElement("input");  
     currentElement.setAttribute("type", "text");  
     currentElement.setAttribute("name", "addDescAtiv" + addDescAtiv);  
	 currentElement.setAttribute("id", "addDescAtiv" + addDescAtiv); 
	 currentElement.setAttribute("class", "text");
	 currentElement.setAttribute("className", "text");
	 currentElement.setAttribute("size", 68);
   
     td2.appendChild(currentElement);  
   
     row.appendChild(td1);  
     row.appendChild(td2);  
     tbody.appendChild(row);  
	 
	 addCodAtiv++;
     addDescAtiv++;
 }  
 
function addSocio(id){  

     var tbody = document.getElementById(id).getElementsByTagName("TBODY")[0];  
     var row = document.createElement("TR")  
     //cria a primeiro td  
     var td1 = document.createElement("TD")  
     td1.appendChild(document.createTextNode("Nome ou Razão Social "))  
     var currentElement = document.createElement("input");  
     currentElement.setAttribute("type", "text");  
     currentElement.setAttribute("name", "NomeS" + addNome );  
	 currentElement.setAttribute("id", "NomeS"+ addNome); 
	 currentElement.setAttribute("class", "text");
	 currentElement.setAttribute("className", "text");
	 currentElement.setAttribute("size", 50);
	 td1.appendChild(currentElement); 
       
     //cria o segundo td  
     var td2 = document.createElement("TD")  
   
  	 td2.appendChild(document.createTextNode("CPF ou CNPJ ")) 
     var currentElement = document.createElement("input");  
     currentElement.setAttribute("type", "text");  
     currentElement.setAttribute("name", "CPFS" + addCPF);  
	 currentElement.setAttribute("id", "CPFS" + addCPF); 
	 currentElement.setAttribute("class", "text");
	 currentElement.setAttribute("className", "text");
	 currentElement.setAttribute("size", 12);
   
     td2.appendChild(currentElement);  
   
     row.appendChild(td1);  
     row.appendChild(td2);  
     tbody.appendChild(row);  
	 
	 addNome++;
     addCPF++;
 } 
 
 function numeros(ie, ff) {
    if (ie) {
        tecla = ie;
    } else {
        tecla = ff;
    }
 
    /**
    * 13 = [ENTER]
    * 8  = [BackSpace]
    * 9  = [TAB]
    * 46 = [Delete]
    * 48 a 57 = São os números
	* 44 = vírgula
    */
	if ((tecla >= 48 && tecla <= 57) || (tecla == 8) || (tecla == 13) || (tecla == 9) ||(tecla == 44))	 {
        return true;
    }
    else {
        return false;
    }
}

function MascaraMoeda(objTextBox, SeparadorMilesimo, SeparadorDecimal, e){
    var sep = 0;
    var key = '';
    var i = j = 0;
    var len = len2 = 0;
    var strCheck = '0123456789';
    var aux = aux2 = '';
    var whichCode = (window.Event) ? e.which : e.keyCode;
    if (whichCode == 13) return true;
    //acrescentar:
    var t = new String(objTextBox.value);
    if (whichCode == 8){
    objTextBox.value = t.substring(0, t.length-1);
	}
    key = String.fromCharCode(whichCode); // Valor para o cÃ³digo da Chave
    if (strCheck.indexOf(key) == -1) return false; // Chave invÃ¡lida
    len = objTextBox.value.length;
    for(i = 0; i < len; i++)
        if ((objTextBox.value.charAt(i) != '0') && (objTextBox.value.charAt(i) != SeparadorDecimal)) break;
    aux = '';
    for(; i < len; i++)
        if (strCheck.indexOf(objTextBox.value.charAt(i))!=-1) aux += objTextBox.value.charAt(i);
    aux += key;
    len = aux.length;
    if (len == 0) objTextBox.value = '';
    if (len == 1) objTextBox.value = '0'+ SeparadorDecimal + '0' + aux;
    if (len == 2) objTextBox.value = '0'+ SeparadorDecimal + aux;
    if (len > 2) {
        aux2 = '';
        for (j = 0, i = len - 3; i >= 0; i--) {
            if (j == 3) {
                aux2 += SeparadorMilesimo;
                j = 0;
            }
            aux2 += aux.charAt(i);
            j++;
        }
        objTextBox.value = '';
        len2 = aux2.length;
        for (i = len2 - 1; i > 0; i--)
        objTextBox.value += aux2.charAt(i);
        objTextBox.value += SeparadorDecimal + aux.substr(len - 2, len);
    }
    return false;
}
 
function Valida(content, form) {
//	if (content == null) {	
       var teste = form.VlrTransacao.value;
	   var teste1 = teste.replace (".", "");
	   var teste2 = teste1.replace (".","");
	   var teste3 = teste2.replace(".","");
	   var teste4 = teste3.replace(".","");
	   var teste5 = teste4.replace(",",".");
	   var normal = Number(teste5);
		if (form.TipoContrato.value == "") {
			alert ("Selecione o Tipo do Contrato.");
			form.TipoContrato.focus(); }
		else if ((form.EntFinanciadora.value != "") && (form.VlrFinanciado.value == "")) {
			alert ("É necessário preencher o Valor Financiado.");
			form.VlrFinanciado.focus(); }
		else if (form.RespEncaminhamento.value == "") {
			alert ("É necessário informar o Responsável pelo Encaminhamento.");
			form.RespEncaminhamento.focus(); }
		else if (form.NomeAd.value == "") {
			alert ("É necessário preencher o Nome do Adquirente.");
			form.NomeAd.focus(); }
		else if (form.Profissao.value == "") {
			alert ("É necessário preencher o campo Profissão.");
			form.Profissao.focus(); }
		else if (form.RG.value == "") {
			alert ("É necessário preencher o RG do Adquirente.");
			form.RG.focus(); }
		else if (form.CPFAd.value == "") {
			alert ("É necessário preencher o CNPJ/CPF do Adquirente.");
			form.CPFAd.focus(); }
		else if (form.EndAd.value == "") {
			alert ("É necessário preencher o Endereço do Adquirente.");
			form.EndAd.focus(); }
			else if (form.NomeTr.value == "") {
			alert ("É necessário preencher o Nome do Transmitente.");
			form.NomeTr.focus(); }
		else if (form.EndTr.value == "") {
			alert ("É necessário preencher o Endereço do Transmitente.");
			form.EndTr.focus(); }
		else if (form.CPFTr.value == "") {
			alert ("É necessário preencher o CNPJ/CPF do Transmitente.");
			form.CPFTr.focus(); }
		else if (form.CondLegais.value == "") {
			alert ("Selecione a Condição Legal do Terreno ou Imóvel Rural.");
			form.CondLegais.focus(); }
		else if (form.CondFisicas.value == "") {
			alert ("Selecione a Condição Física do Terreno ou Imóvel Rural.");
			form.CondFisicas.focus(); }
		else if ((form.Testada.value == "m") || (form.Testada.value == "")) {
			alert ("É necessário preencher o campo Testada.");
			form.Testada.focus(); }
		else if ((form.LadoDir.value == "m") || (form.LadoDir.value == "")) {
			alert ("É necessário preencher o campo Lado Direito.");
			form.LadoDir.focus(); }
		else if ((form.LadoEsq.value == "m") || (form.LadoEsq.value == "")) {
			alert ("É necessário preencher o campo Lado Esquerdo.");
			form.LadoEsq.focus(); }
		else if ((form.Fundo.value == "m") || (form.Fundo.value == "")) {
			alert ("É necessário preencher o campo Fundo.");
			form.Fundo.focus(); }
		else if ((form.AreaTerreno.value == "m²") || (form.AreaTerreno.value == "")) {
			alert ("É necessário preencher o campo Área do Terreno.");
			form.AreaTerreno.focus(); }
		else if ((form.AreaOcupada.value == "m²") || (form.AreaOcupada.value == "")) {
			alert ("É necessário preencher o campo Área Ocupada Edificada.");
			form.AreaOcupada.focus(); }
		else if ((form.AreaUtil.value == "m²") || (form.AreaUtil.value == "")) {
			alert ("É necessário preencher o campo Área Útil.");
			form.AreaUtil.focus(); }
		else if ((form.AreaTotal.value == "m²") || (form.AreaTotal.value == "")) {
			alert ("É necessário preencher o campo Área Total.");
			form.AreaTotal.focus(); }
		else if (form.Distrito.value == "") {
			alert ("É necessário preencher o campo Distrito.");
			form.Distrito.focus(); }
		else if (form.InscMunicipal.value == "") {
			alert ("É necessário preencher o campo Inscrição Municipal ou Rural.");
			form.InscMunicipal.focus(); }
		else if (form.TipoLogradouro.value == "") {
			alert ("Selecione Tipo de Logradouro do Terreno ou Imóvel Rural.");
			form.TipoLogradouro.focus(); }
		else if (form.EndImovel.value == "") {
			alert ("É necessário preencher o campo Endereço do Im&oacute;vel.");
			form.EndImovel.focus(); }
		else if (form.NroImovel.value == "") {
			alert ("É necessário preencher o campo N&ordm;.");
			form.NroImovel.focus(); }
		else if (form.BairroImovel.value == "") {
			alert ("É necessário preencher o campo Bairro do Im&oacute;vel.");
			form.BairroImovel.focus(); }
		else if (form.Especie.value == "") {
			alert ("É necessário preencher o campo Espécie.");
			form.Especie.focus(); }
		else {
			var janela = window.open("", "janela", "height=500, width=700, scrollbars=yes, left=150, top=50" ) ;
			form.submit();
//			var fieldsITBI = [form.InscMunicipal,form.CPFAd,form.NomeAd,
//			                  form.TipoLogradouro,form.EndAd,form.CompImovel,
//			                  form.NroImovel,form.BairroImovel,form.AreaTerreno,
//			                  form.AreaOcupada,form.AreaUtil,form.AreaTotal];
//			getPage("POST", "Valida", 
//	                "methodcaller?className=com.tivic.manager.egov.ITBIServices&method=gerarProtocoloITBI(InscMunicipal:String, CPFAd:String,"+
//	                " NomeAd:String, TipoLogradouro:String, EndAd:String, CompImovel:String, NroImovel:String, BairroImovel:String,"+
//	                " AreaUtil:String, AreaTotal:String, AreaTerreno:String, AreaOcupada:String, AreaUtil:String, "+
//	                " AreaTotal:String, const null:String, const null:String, const null:String, const null:String)", fieldsITBI, true);
			
		}
//	}
//	else {
//		var result = null;
//        try {result = eval('(' + content + ')');} catch(e) {}
//		window.open("protocoloITBI.jsp?nrInscricaoMobiliaria="+result.objects["nrInscricaoMobiliaria"]+
//				"&nrDocumento="+result.objects["nrDocumento"]+
//				"&idDocumento="+result.objects["idDocumento"]+
//				"&dataProtocolo="+result.objects["dataProtocolo"], 
//				"janela", "height=260, width=700, scrollbars=yes, left=150, top=50" ) ;
//	}
}

//funcao para buscar do banco as informações do imovel
//function DadosOfInscricao(content, form) {
//	if (content == null) {
//		var fields = [form.InscMunicipal];
//		getPage("POST", "DadosOfInscricao", 
//                "methodcaller?className=com.tivic.manager.egov.ITBIServices&method=getDadosOfInscricao(InscMunicipal:String)", fields, true);
//	}
//	else {
//		var result = null;
//		try {result = eval('(' + content + ')');} catch(e) {}
//		//dados do transmitente
//		document.getElementById('NomeTr').value = result.objects.rsm.lines[0]['NMCONTRIBUINTE'];
//		document.getElementById('EndTr').value = result.objects.rsm.lines[0]['NMTIPOLOGRADOURO']+ " " +
//												 result.objects.rsm.lines[0]['NMLOGRADOURO']+ ", " +
//												 result.objects.rsm.lines[0]['NRIMOVEL']+ ", " +
//												 result.objects.rsm.lines[0]['NMCIDADE']+ " - " +
//												 result.objects.rsm.lines[0]['SGESTADO'];
//		document.getElementById('CPFTr').value = result.objects.rsm.lines[0]['NRCPFCNPJ'];
//		//caracteristicas do terreno ou imovel rural
//		document.getElementById('Testada').value = result.objects.rsm.lines[0]['NMCAMPO13']+document.getElementById('Testada').value;
//		document.getElementById('Fundo').value = result.objects.rsm.lines[0]['NMCAMPO14']+document.getElementById('Fundo').value;
//		document.getElementById('LadoDir').value = result.objects.rsm.lines[0]['NMCAMPO6']+document.getElementById('LadoDir').value;
//		document.getElementById('LadoEsq').value = result.objects.rsm.lines[0]['NMCAMPO7']+document.getElementById('LadoEsq').value;
//		document.getElementById('AreaTerreno').value = result.objects.rsm.lines[0]['VLM2TERRENO']+document.getElementById('AreaTerreno').value;
//		document.getElementById('AreaOcupada').value = result.objects.rsm.lines[0]['VLM2CONSTRUCAO']+document.getElementById('AreaOcupada').value;
//		//caracteristicas da construca	
//		document.getElementById('AreaUtil').value = result.objects.rsm.lines[0]['VLAREATERRENO']+document.getElementById('AreaUtil').value;
//		document.getElementById('AreaTotal').value = result.objects.rsm.lines[0]['VLAREACONSTRUCAO']+document.getElementById('AreaTotal').value;
//		document.getElementById('FracaoIdeal').value = result.objects.rsm.lines[0]['VLAREATERRENO'];
//		document.getElementById('EndImovel').value = result.objects.rsm.lines[0]['NMLOGRADOUROIMOVEL'];
//		document.getElementById('BairroImovel').value = result.objects.rsm.lines[0]['NMBAIRROIMOVEL'];
//		document.getElementById('NroImovel').value = result.objects.rsm.lines[0]['NRENDERECO'];
//		document.getElementById('CompImovel').value = result.objects.rsm.lines[0]['NMCOMPLEMENTOIMOVEL'];
//	}
//}

//funcao criada para validar o formulario do arquivo novo_vistoria.jsp
function ValidaVistoria(quadro) {
	if (quadro.Nome.value == "") {
		alert ("É necessário informar o Nome ou Razão Social.");
		quadro.Nome.focus(); }	
	else if (quadro.End.value == "") {
		alert ("É necessário preencher o Endereço.");
		quadro.End.focus(); }
	else if (quadro.Bairro.value == "") {
		alert ("É necessário informar o nome do Bairro.");
		quadro.Bairro.focus(); }
	else if (quadro.AtivPrincipal.value == "") {
		alert ("É necessário informar a Atividade.");
		quadro.AtivPrincipal.focus(); }
	else if (quadro.Telefone.value == "") {
		alert ("É necessário informar um numero de telefone.");
		quadro.Telefone.focus(); }
	else if (quadro.CodAtividade.value == "") {
		alert ("É necessário informar o Codigo da Atividade.");
		quadro.CodAtividfade.focus(); }
	else if (quadro.Area.value == "") {
		alert ("É necessário informar a area do estabelecimento.");
		quadro.Area.focus(); }	
	else {
		var janela = window.open("", "janela", "height=500, width=700, scrollbars=yes, left=150, top=50" ) ;
		quadro.submit(); }
}

function ValidaReq(quadro) {
	if (quadro.Nome.value == "") {
		alert ("É necessário informar o Nome");
		quadro.Nome.focus(); }	
	else if (quadro.End.value == "") {
		alert ("É necessário preencher o Endereço.");
		quadro.End.focus(); }
	else if (quadro.RG.value == "") {
		alert ("É necessário informar o número do RG");
		quadro.RG.focus(); }
	else if (quadro.CPF.value == "") {
		alert ("É necessário informar o número do CPF");
		quadro.CPF.focus(); }
	else if (quadro.area.value == "") {
		alert ("É necessário informar a área do imóvel.");
		quadro.area.focus(); }
	else if (quadro.EndImovel.value == "") {
		alert ("É necessário informar o enderaço do imóvel");
		quadro.EndImovel.focus(); }
	else if (quadro.Zona.value == "") {
		alert ("Informe a Zona do imóvel");
		quadro.Zona.focus(); }	
	else if ((quadro.Zona.value == "urbana") && (quadro.Inscr.value == "")){
		alert("Informe o número da inscrição do imóvel");
		quadro.Inscr.focus();} 
	else {
		var janela = window.open("", "janela", "height=500, width=700, scrollbars=yes, left=150, top=50" ) ;
		quadro.submit(); }
}

//funcao criada para inserir somente numeros em um campo (inicialmente usada no arquivo novo_vistoria.jsp
function SomenteNumero(campo){
	var digits="0123456789";
	var campo_temp;
	for (var i=0;i<campo.value.length;i++){
    	campo_temp=campo.value.substring(i,i+1);	
    	if (digits.indexOf(campo_temp)==-1){
     		campo.value = campo.value.substring(0,i);
     		break;
	    }
	}
}

//function MascaraMonetario(campo){
//	var monetarioMask = new Mask('#,###.00', "number");
//	monetarioMask.attach($(campo));
//}
//
//function MascaraInscricao(inscMunicipal){
//	if(mascaraInteiro(inscMunicipal)==false){
//		event.returnValue = false;
//	}
//	return formataCampo(inscMunicipal, '00.00.000.0000.000', event);
//}
//
//function MascaraRG(rg){
//	if(mascaraInteiro(rg)==false){
//		event.returnValue = false;
//	}
//	return formataCampo(rg, '00000000-00', event);
//}
//
////função que insere máscara de CNPJ
//function MascaraCNPJ(cnpj){
//	if(mascaraInteiro(cnpj)==false){
//		event.returnValue = false;
//	}
//	return formataCampo(cnpj, cnpj.value.length > 13 ? '00.000.000/0000-00' : '000.000.000-00', event);
//}
//
//function mascaraInteiro(){
//	if (event.keyCode < 48 || event.keyCode > 57){
//		event.returnValue = false;
//		return false;
//	}
//	return true;
//}
//
//function formataCampo(campo, Mascara, evento) { 
//	var boleanoMascara; 
//	
//	var Digitato = evento.keyCode;
//	exp = /\-|\.|\/|\(|\)| /g
//	campoSoNumeros = campo.value.toString().replace( exp, "" ); 
//   
//	var posicaoCampo = 0;	 
//	var NovoValorCampo="";
//	var TamanhoMascara = campoSoNumeros.length;
//	
//	if (Digitato != 8) { // backspace 
//		for(i=0; i<= TamanhoMascara; i++) { 
//			boleanoMascara  = ((Mascara.charAt(i) == "-") || (Mascara.charAt(i) == ".")
//								|| (Mascara.charAt(i) == "/")) 
//			boleanoMascara  = boleanoMascara || ((Mascara.charAt(i) == "(") 
//								|| (Mascara.charAt(i) == ")") || (Mascara.charAt(i) == " ")) 
//			if (boleanoMascara) { 
//				NovoValorCampo += Mascara.charAt(i); 
//				  TamanhoMascara++;
//			}else { 
//				NovoValorCampo += campoSoNumeros.charAt(posicaoCampo); 
//				posicaoCampo++; 
//			  }	   	 
//		  }	 
//		campo.value = NovoValorCampo;
//		  return true; 
//	}else { 
//		return true; 
//	}
//}

function AbreCAE(quadro) {
	if (quadro.Nome.value == "") {
		alert ("É necessário preenche o Nome");
		quadro.Nome.focus(); }
	else if (quadro.NFantasia.value == "") {
		alert ("É necessário preenche o Nome Fantasia");
		quadro.NFantasia.focus(); }
	else if (quadro.Comando.value == "") {
		alert ("É necessário informar o Comando.");
		quadro.Comando.focus(); }
	else if (quadro.CPFCont.value == "") {
		alert ("É necessário preencher o CPF ou CNPJ do Contribuinte.");
		quadro.CPFCont.focus(); }
	else if (quadro.Incidencia.value == "") {
		alert ("É necessário informar a Incidência.");
		quadro.Incidencia.focus(); }
	else if (quadro.ContEstab.value == null) {
		alert ("É necessário marcar o campo Contribuinte Estabelecido.");}
	else if (quadro.InicAtiv.value == "") {
		alert ("É necessário preencher a data do Início da Atividade.");
		quadro.InicAtiv.focus(); }
	else if (quadro.Classif.value == "") {
		alert ("É necessário informar a Classificação da Atividade.");
		quadro.Classif.focus(); }
	else if (quadro.NatJuridica.value == "") {
		alert ("É necessário informar a Natureza Jurídica.");
		quadro.NatJuridica.focus(); }
	else if (quadro.TipoFisc.value == "") {
		alert ("É necessário preencher o Tipo do Endereço.");
		quadro.TipoFisc.focus(); }
	else if (quadro.LogradouroFisc.value == "") {
		alert ("É necessário preencher o Logradouro do Domicílio Fiscal.");
		quadro.LogradouroFisc.focus(); }
	else if (quadro.NumFisc.value == "") {
		alert ("É necessário preencher o Número.");
		quadro.NumFisc.focus(); }
	else if (quadro.BairroFisc.value == "") {
		alert ("É necessário preencher o Bairro ou Distrito do Domicílio Fiscal.");
		quadro.BairroFisc.focus(); }
	else if (quadro.TipoCont.value == "") {
		alert ("É necessário preencher o Tipo do Endereço.");
		quadro.TipoCont.focus(); }
	else if (quadro.LogradouroCont.value == "") {
		alert ("É necessário preencher o Logradouro do Contribuinte.");
		quadro.LogradouroCont.focus(); }
	else if (quadro.NumCont.value == "") {
		alert ("É necessário preencher o Número.");
		quadro.NumCont.focus(); }
	else if (quadro.BairroCont.value == "") {
		alert ("É necessário preencher o Bairro ou Distrito do Contribuinte.");
		quadro.BairroCont.focus(); }
	else if (quadro.CodPrinc.value == "") {
		alert ("É necessário preencher o Código da Atividade Principal.");
		quadro.CodPrinc.focus(); }
	else if (quadro.DescPrinc.value == "") {
		alert ("É necessário preencher a Descrição da Atividade Principal.");
		quadro.DescPrinc.focus(); }
	else if (quadro.NomeResp.value == "") {
		alert ("É necessário preencher o Nome do Responsável pelas Informações.");
		quadro.NomeResp.focus(); }
	else if (quadro.CPFResp.value == "") {
		alert ("É necessário preencher o CPF do Responsável pelas Informações.");
		quadro.CPFResp.focus(); }
	else if (quadro.EndResp.value == "") {
		alert ("É necessário preencher o Endereço do Responsável pelas Informações.");
		quadro.EndResp.focus(); }
	else if (quadro.RG.value == "") {
		alert ("É necessário preencher o RG.");
		quadro.RG.focus(); }
	else if (quadro.Expedicao.value == "") {
		alert ("É necessário preencher a Data de Expedição.");
		quadro.Expedicao.focus(); }
	else if (quadro.Orgao.value == "") {
		alert ("É necessário preencher o Órgão Emissor.");
		quadro.Orgao.focus(); }
	else {
		quadro.sessao.value = "sim";
		quadro.contAtivSec.value = addCodAtiv; 
		quadro.contSocio.value = addNome; 
		var janela = window.open("", "janela", "height=500, width=700, scrollbars=yes, left=150, top=50" ) ;
		quadro.submit(); }
}

function AbreIPTU(quadro) {

	var janela = window.open("", "janela", "height=500, width=700, scrollbars=yes, left=150, top=50" ) ;
	quadro.submit(); 
}

function consultaZona()
{var janela = window.open("zonas.html", "janela", "width=260, height=255, scrollbars=yes, left=500, top=310");} 

function testada()
{var janela = window.open("testada.html", "janela", "width=300, height=100, scrollbars=yes, left=300, top=210");} 

function fundo()
{var janela = window.open("fundo.html", "janela", "width=300, height=100, scrollbars=yes, left=300, top=210");}

function lados()
{var janela = window.open("lados.html", "janela", "width=300, height=100, scrollbars=yes, left=300, top=210");}

function AreaOcupada()
{var janela = window.open("AreaOcupada.html", "janela", "width=300, height=100, scrollbars=yes, left=300, top=210");}

function AreaTerreno()
{var janela = window.open("AreaTerreno.html", "janela", "width=300, height=100, scrollbars=yes, left=300, top=210");}

function fracaoIdeal()
{var janela = window.open("fracaoIdeal.html", "janela", "width=350, height=100, scrollbars=yes, left=300, top=210");}

function inscricao()
{var janela = window.open("inscricao.html", "janela", "width=350, height=100, scrollbars=yes, left=300, top=210");}

function encaminhamento()
{var janela = window.open("encaminhamento.html", "janela", "width=350, height=100, scrollbars=yes, left=300, top=210");}

function envia_dados(formulario)
{
	alert("oqueeeeeeeeeeeee");
	var janela = window.open("", "janela", "height=500, width=650");
	formulario.submit();
}

var tagAlvo = new Array('p'); 
 
var tamanhos = new Array( '10px', '11px','12px','13px','14px','15px', '16px' );
var tamanhoInicial = 2;
 
function mudaTamanho( idAlvo,acao ){
  if (!document.getElementById) return
  var selecionados = null,tamanho = tamanhoInicial,i,j,tagsAlvo;
  tamanho += acao;
  if ( tamanho < 0 ) tamanho = 0;
  if ( tamanho > 6 ) tamanho = 6;
  tamanhoInicial = tamanho;
  if ( !( selecionados = document.getElementById( idAlvo ) ) ) selecionados = document.getElementsByTagName( idAlvo )[ 0 ];
  
  selecionados.style.fontSize = tamanhos[ tamanho ];
  
  for ( i = 0; i < tagAlvo.length; i++ ){
    tagsAlvo = selecionados.getElementsByTagName( tagAlvo[ i ] );
    for ( j = 0; j < tagsAlvo.length; j++ ) tagsAlvo[ j ].style.fontSize = tamanhos[ tamanho ];
  }
}



function divSefin(id_opcao)
{
	var contexto = document.getElementById("contextoIndex");	
	var titulo = document.getElementById("titulo");
	
	contexto.innerHTML = "";
	titulo.innerHTML = "";

	if (id_opcao == 1){
		contexto.innerHTML = "<p>A Secretaria de Finan&ccedil;as da Prefeitura Municipal de Vit&oacute;ria da  Conquista &eacute; o &oacute;rg&atilde;o respons&aacute;vel pela centraliza&ccedil;&atilde;o, coordena&ccedil;&atilde;o e  controle do Sistema Municipal de Finan&ccedil;as e Tributa&ccedil;&atilde;o, tem por  finalidade:</p><ul><p>&bull; Planejar, coordenar, supervisionar, executar, controlar, e avaliar as pol&iacute;ticas financeiras e tribut&aacute;rias do Munic&iacute;pio;</p><p>&bull; Exercer a administra&ccedil;&atilde;o e a cobran&ccedil;a da d&iacute;vida ativa do Munic&iacute;pio; </p><p>&bull; Executar, controlar e avaliar as atividades de contabiliza&ccedil;&atilde;o dos  atos e fatos or&ccedil;ament&aacute;rios, patrimoniais e financeiros e de  processamento de dados do Munic&iacute;pio. </p></ul>";
		titulo.innerHTML = "A SEFIN&nbsp;";
	}
	else if (id_opcao == 2){
		contexto.innerHTML = "<p><a href='legislacoes/REFIS.pdf' target='_blank'>&bull; Lei 1.624-2009 - Institui o Programa de Recuperação Fiscal -  REFIS</a></p><p><a href='legislacoes/Lei 1387-2006 - Destinacao de Recursos a Entidades.pdf' target='_blank'>&bull; Lei 1387-2006 - Destina&ccedil;&atilde;o de Recursos a Entidades</a></p><p><a href='legislacoes/Lei 1369-2006 - Obrigatoriedade de instalacao de equipamento eliminador de ar em tubulacao.pdf' target='_blank'>&bull;   Lei 1369-2006 - Obrigatoriedade de instala&ccedil;&atilde;o de equipamento eliminador de ar em tubula&ccedil;&atilde;o</a></p><p><a href='legislacoes/Lei 1410-2007 - Codigo Meio Ambiente.pdf' target='_blank'>&bull;  Lei 1410-2007 - C&oacute;digo do Meio Ambiente</a></p><p><a href='legislacoes/Lei 1385-2006 -Plano diretor do municipio de Vitoria da Conquista.pdf' target='_blank'>&bull;  Lei 1385-2006 -Plano diretor do Munic&iacute;pio de Vit&oacute;ria da Conquista</a></p><p><a href='legislacoes/Codigo Tributario Municipal - LEI 1259 de 2004.pdf' target='_blank'>&bull;  C&oacute;digo Tribut&aacute;rio Municipal - Lei 1259 de 2004 - Lei 1306 de 2005 e Lei 1587 de 2008</a></p><p><a href='legislacoes/Lei 1481-2007 - Lei de Ordenamento do Solo e Codigo de Obras.pdf' target='_blank'>&bull;  Lei 1481-2007 - Lei de Ordenamento do Solo e C&oacute;digo de Obras</a></p><p><a href='legislacoes/Lei Organica do Municipio de Vitoria da Conquista.pdf' target='_blank'>&bull;  Lei Org&acirc;nica do Munic&iacute;pio de Vit&oacute;ria da Conquista</a></p><p><a href='legislacoes/Lei 1364-2006 - Taxa de Religacao.pdf' target='_blank'>&bull;  Lei 1364-2006 - Taxa de Religa&ccedil;&atilde;o</a></p><p><a href='legislacoes/Lei 1532-2008 - Altera Dispositivo da Lei Organica Municipal.pdf' target='_blank'>&bull;  Lei 1532-2008 - Altera Dispositivo da Lei Org&acirc;nica Municipal</a></p><p><a href='legislacoes/Lei   695-1993 - Codigo_Policia_Administrativa.pdf' target='_blank'>&bull;  Lei 695-1993 - C&oacute;digo de Pol&iacute;cia Administrativa</a></p><p><a href='legislacoes/Lei 1345-2006 - Atendimento ao Cliente nos Estabelecimentos Bancarios.pdf' target='_blank'>&bull;  Lei 1345-2006 - Atendimento ao Cliente nos Estabelecimentos Banc&aacute;rios</a></p><p><a href='legislacoes/Lei 1363-2006 - Politica ambiental.pdf' target='_blank'>&bull;  Lei 1363-2006 - Pol&iacute;tica Ambiental</a></p><p><a href='legislacoes/Lei 1292-2005 - Atendimento Bancario.pdf' target='_blank'>&bull;  Lei 1292-2005 - Atendimento Banc&aacute;rio</a></p><p><a href='legislacoes/Lei 1382-2006 - Estimula a arrecadacao do IPTU atraves de sorteio de premios.pdf' target='_blank'>&bull;  Lei 1382-2006 - Estimula a arrecada&ccedil;&atilde;o do IPTU atrav&eacute;s de sorteio de pr&ecirc;mios</a></p><p><a href='legislacoes/Lei 1361-2006 - Veda a noemacao de parentes.pdf' target='_blank'>&bull;  Lei 1361-2006 - Veda a noema&ccedil;&atilde;o de parentes</a></p><p><a href='legislacoes/Lei 1296-2005 - Disciplina Feiras Eventos Temporarios.pdf' target='_blank'>&bull;  Lei 1296-2005 - Disciplina Feiras e Eventos Tempor&aacute;rios</a></p><p><a href='legislacoes/Lei 1365-2006 - Incentivo a bicicleta.pdf' target='_blank'>&bull;  Lei 1365-2006 - Incentivo a bicicleta</a></p>";
		titulo.innerHTML = "Legisla&ccedil;&otilde;es&nbsp;";
	}
	else if (id_opcao == 3){
		contexto.innerHTML = "<p><a href= 'decretos/Decreto 15129-2013 Prorroga cota unica IPTU.pdf ' target='_blank'>&bull; Decreto 15129 - 2013 Prorroga prazo para pagamento da parcela &uacute;nica do IPTU 2013</a></p><p><a href= 'decretos/Decreto 14870 - 2012 - Atualiza tributos municipais.pdf ' target='_blank'>&bull; Decreto 14 870 - 2012 Atualiza os valores das tabelas do c&oacute;digo tribut&aacute;rio municipal e demais tributos municipais</a></p> <p><a href= 'decretos/Decreto  14198 - 2011 - Atualiza os tributos municipais.pdf' target='_blank'>&bull; Decreto 14 198 - 2011 Atualiza os tributos municipais</a></p> <p><a href= 'decretos/Decreto 13 758 IPTU Premiado.pdf' target='_blank'>&bull; Decreto 13 758 - 2011 Regulamenta a Campanha IPTU Premiado 2011</a></p><p><a href= 'decretos/Decreto 13 644 - 2010 - Atualiza valores.pdf' target='_blank'>&bull; Decreto 13 644 - 2010 Atualiza os valores integrantes das tabelas do código tribut&aacuterio municipal e demais tributos municipais</a></p><p><a href='decretos/Decreto 13 475 - 2010 - ISSQN.pdf' target='_blank'>&bull; Decreto 13 475 - 2010 -  Republica o decreto 12 300 - 2007 e estabelece obriga&ccedil;&otilde;es acess&oacute;rias relativas ao ISSQN</a></p><p><a href='decretos/Decreto de atualizacao dos tributos.pdf' target='_blank'>&bull; Decreto 12 516 - 2007 -  Atualiza os tributos municipais e os valores integrantes das tabelas do c&oacute;digo tribut&aacute;rio municipal</a></p><p><a href='decretos/Decreto 13217-2009.pdf' target='_blank'>&bull; Decreto 13 217 - 2009 - Altera pre&ccedil;o p&uacute;blico de boxes na CEASA</a></p><p><a href='decretos/Decreto 13 151.pdf' target='_blank'>&bull; Decreto 13 151 - 2009 - Fixa Preço P&uacute;blico Para Uso de Box</a></p><p><a href='decretos/Regulamento do Sorteio IPTU Premiado 2009.pdf' target='_blank'>&bull; Decreto 13 002 - 2009 - Regulamenta o sorteio do IPTU premiado ano 2009</a></p><p><a href='decretos/Decreto 13020-09-Alt. Turmas do Cons. do Contribuinte 13-04-2009.pdf' target='_blank'>&bull; Decreto 13 020-2009 - Altera a composi&ccedil;&atilde;o das turmas do conselho municipal de contribuinte</a></p><p><a href='decretos/Decreto 13038-2009 Prorroga IPTU 2009.pdf' target='_blank'>&bull; Decreto 13 038-2009 - Prorroga o prazo para pagamento da cota &uacute;nica do IPTU/2009 com 10% de desconto</a></p><p><a href='decretos/Decreto 12 377-2007 Regulamenta a produttividade fiscal.pdf' target='_blank'>&bull; Decreto 12 377-2007 - Regulamenta a produttividade fiscal</a></p><p><a href='decretos/Decreto  12 525 - Define criterios para concessao da bolsa de estudos social ampliada instituida pela Lei n 1 479 - 2007.pdf' target='_blank'>&bull; Decreto 12 525 - Define crit&eacute;rios para concess&atilde;o da bolsa de estudos social ampliada, instituida pela Lei n 1 479 - 2007</a></p><p><a href='decretos/Decreto    9 192 - 1998 - Dispoe Sobre a Exigencia de CND.pdf' target='_blank'>&bull; Decreto 9 192 - 1998 - Disp&otilde;e sobre a exig&ecirc;ncia de CND</a></p><p><a href='decretos/Decreto  12 462 - 2007 Dispoe Sobre os criterios para isencao de IPTU.pdf' target='_blank'>&bull; Decreto 12 462 - 2007 - Disp&otilde;e sobre os crit&eacute;rios para isen&ccedil;&atilde;o de IPTU</a></p></p><p><a href='decretos/Decreto  12 229 - 2006 - Planta de valores para IPTU.pdf' target='_blank'>&bull; Decreto 12 229 - 2006 - Planta de valores para IPTU</a></p></p><p><a href='decretos/Decreto  11 977 - 2005 - Atualizacao das Tabelas do  Codigo Tributario - Lei 1259 - 2004.pdf' target='_blank'>&bull; Decreto 11 977 - 2005 - Atualiza&ccedil;&atilde;o das Tabelas do C&oacute;digo Tribut&aacute;rio - Lei 1259 - 2004</a></p><p><a href='decretos/Decreto  12 300 - 2007 - Regulamenta o gerenciamento eletronico do ISSQN.pdf' target='_blank'>&bull; Decreto 12 300 - 2007 - Regulamenta o gerenciamento eletr&ocirc;nico do ISSQN</a></p><p><a href='decretos/Decreto  11 976 - 2005 - Planta de Valores para IPTU.pdf' target='_blank'>&bull; Decreto 11 976 - 2005 - Planta de Valores para IPTU</a></p><p><a href='decretos/Decreto  11 672 - 2004 - Regulamento do Codigo Tributario - Lei 1259 - 2004.pdf' target='_blank'>&bull; Decreto 11 672 - 2004 - Regulamento do C&oacute;digo Tribut&aacute;rio - Lei 1259 - 2004</a></p>";
		titulo.innerHTML = "Decretos&nbsp;";
	}
	else if (id_opcao == 4){
		contexto.innerHTML = "<p><a href='portarias/Portaria 01 - 2011 - Estabelece a tramitacao de processos para avaliacao de Impostos.pdf' target='_blank'>&bull; Portaria 01 - 2011 - Estabelece a tramita&ccedil;&atilde;o de processos para avalia&ccedil;&atilde;o de Impostos sobre a Transmiss&atilde;o Inter-Vivos</a></p><p><a href='portarias/Portaria 01- 2007 - Secretaria do Meio ambiente referente alvara ambiental.pdf' target='_blank'>&bull; Portaria 01- 2007 - Secretaria do Meio Ambiente referente a Alvara Ambiental</a></p><p><a href='portarias/Portaria 08 - 2006 - Documentos a serem anexados nos autos de infracao.pdf' target='_blank'>&bull; Portaria 08 - 2006 - Documentos a serem anexados nos autos de infra&ccedil;&atilde;o</a></p><p><a href='portarias/Portaria 02 -2007 - Prazo para comunicar o fisco o extavio de documentos fiscais.pdf' target='_blank'>&bull; Portaria 02 -2007 - Prazo para comunicar ao fisco o extavio de documentos fiscais</a></p><p><a href='portarias/Portaria 06 - 2006 - Necessidade de analise de capacidade de pagamento para parcelamento em mais de 24 parcelas.pdf' target='_blank'>&bull; Portaria 06 - 2006 - Necessidade de an&aacute;lise de capacidade de pagamento para parcelamento em mais de 24 parcelas</a></p><p><a href='portarias/Portaria 07 - 2006 - Valor maximo para pedido de isencao de IPTU 2007.pdf' target='_blank'>&bull; Portaria 07 - 2006 - Valor m&aacute;ximo para pedido de isen&ccedil;&atilde;o de IPTU 2007</a></p><p><a href='portarias/Portaria 01 - 2006 - Orienta emissao de Guias de ITBI.pdf' target='_blank'>&bull; Portaria 01 - 2006 - Orienta emiss&atilde;o de Guias de ITBI para transfer&ecirc;ncia de im&oacute;veis</a></p><p><a href='portarias/Portaria 06 - 2005 - Estabelece normas para fiscalizacao do ISS.pdf' target='_blank'>&bull; Portaria 06 - 2005 - Estabelece normas para fiscaliza&ccedil;&atilde;o do ISS das empresas enquadradas nos Itens 7.02 e 7.05 do Anexo I da Lei N&deg; 1259_2004</a></p><p><a href='portarias/Portaria 03 - 2006 - Prazo para solicitacao de isencao de IPTU 2006.pdf' target='_blank'>&bull; Portaria 03 - 2006 - Prazo para solicita&ccedil;&atilde;o de isen&ccedil;&atilde;o de IPTU 2006</a></p><p><a href='portarias/Portaria 05 - 2006 - Revalidacao de documentos fiscais.pdf' target='_blank'>&bull; Portaria 05 - 2006 - Revalida&ccedil;&atilde;o de documentos fiscais</a></p><p><a href='portarias/Portaria 01 - 2001 - Processos em tramitacao na IGR encaminhados por despachantes.pdf' target='_blank'>&bull; Portaria 01 - 2001 - Processos em tramita&ccedil;&atilde;o na IGR encaminhados por despachantes</a></p><p><a href='portarias/Portaria 01 - 2005 - Autoriza o fiscal plantonista a assinar solicitacao de taloes de notas fiscais.pdf' target='_blank'>&bull; Portaria 01 - 2005 - Autoriza o fiscal plantonista a assinar solicita&ccedil;&atilde;o de tal&otilde;es de notas fiscais</a></p><p><a href='portarias/Portaria 01 - 2003 - Informacoes nas guias de ITBI.pdf' target='_blank'>&bull; Portaria 01 - 2003 - Informa&ccedil;&otilde;es nas guias de ITBI</a></p><p><a href='portarias/Portaria 02-A - 2006 - Solicitacao de isencao de IPTU 2006.pdf' target='_blank'>&bull; Portaria 02-A - 2006 - Solicita&ccedil;&atilde;o de isen&ccedil;&atilde;o de IPTU 2006</a></p><p><a href='portarias/Portaria 01- 2004 - Autoriza a Gerencia de Fiscalizacao a realizar parcelamentos.pdf' target='_blank'>&bull; Portaria 01- 2004 - Autoriza a Ger&ecirc;ncia de Fiscaliza&ccedil;&atilde;o a realizar parcelamentos</a></p><p><a href='portarias/Portaria 02 - 2005 - Necessidade de numero de CPF ou CNPJ em processos destinados a IGR.pdf' target='_blank'>&bull; Portaria 02 - 2005 - Necessidade de n&uacute;mero de CPF ou CNPJ em processos destinados a IGR</a></p><p><a href='portarias/Portaria 01 - 2007 - Prazo para cadastramento, recadastramento e solicitacao de isencao de tributos.pdf' target='_blank'>&bull; Portaria 01 - 2007 - Prazo para cadastramento, recadastramento e solicita&ccedil;&atilde;o de isen&ccedil;&atilde;o de tributos</a></p>";
		titulo.innerHTML = "Portarias&nbsp;";
	}
	else if (id_opcao == 5){
		contexto.innerHTML = "<li><p><a href='resolucoes/Edital de lancamento do IPTU 2007.pdf' target='_blank'>&bull; Edital de lan&ccedil;amento do IPTU 2007</a></p></li>";
		titulo.innerHTML = "Resolu&ccedil;&otilde;es&nbsp;";
	}
	else if (id_opcao == 6){
		contexto.innerHTML = "<p><a href='publicacoes/Edital IPTU 2012.pdf' target='_blank'>&bull; Edital de lan&ccedil;amento do IPTU 2012</a></p><a href='publicacoes/Mapa Rodoviario da Bahia.pdf' target='_blank'>&bull; Mapa Rodovi&aacute;rio da Bahia</a></p><p><a href='publicacoes/Edital de lancamento do IPTU 2007.pdf' target='_blank'>&bull; Edital de lan&ccedil;amento do IPTU 2007</a></p><p><a href='publicacoes/Tributacao e politica fiscal em Vitoria da Conquista.pdf' target='_blank'>&bull; Tributa&ccedil;&atilde;o e pol&iacute;tica fiscal em Vit&oacute;ria da Conquista</a></p><p><a href='publicacoes/Extrato de Convenio Entre o Municipio e a Receita Federal.pdf' target='_blank'>&bull; Extrato de Conv&ecirc;nio entre o Munic&iacute;pio e a Receita Federal</a></p>";
		titulo.innerHTML = "Publica&ccedil;&otilde;es&nbsp;";
	}
	else if (id_opcao == 7){
		contexto.innerHTML = "<b>Localiza&ccedil;&atilde;o</b><br/>Pra&ccedil;a Joaquim Correia, n&deg; 55<br/>(77) 3424.8500<br/>Centro - Vit&oacute;ria da Conquista/BA<br/>CEP 45000-000<center style='margin-top:10px;'><iframe width='420' height='350' frameborder='0' scrolling='no' src='http://maps.google.com.br/maps?f=q&amp;source=s_q&amp;hl=pt-BR&amp;geocode=&amp;q=%22vit%C3%B3ria+da+conquista%22+prefeitura&amp;sll=-14.893861,-40.850601&amp;sspn=0.077141,0.11055&amp;ie=UTF8&amp;ll=-14.859204,-40.838796&amp;spn=0.023056,0.017662&amp;output=embed&amp;s=AARTsJphKy_BLIuLh_LDVbsvCey_abJqlw'></iframe><br /><small><a href='http://maps.google.com.br/maps?f=q&amp;source=embed&amp;hl=pt-BR&amp;geocode=&amp;q=%22vit%C3%B3ria+da+conquista%22+prefeitura&amp;sll=-14.893861,-40.850601&amp;sspn=0.077141,0.11055&amp;ie=UTF8&amp;ll=-14.859204,-40.838796&amp;spn=0.023056,0.017662' style='color:#0000FF;text-align:left' target='_blanck'>Exibir mapa ampliado</a></small></center>";
		titulo.innerHTML = "Atendimento ao P&uacute;blico&nbsp;";
	}

}

function divOrientacoes(id_opcao)
{
	var contexto = document.getElementById("contextoOrientacoes");	
	var titulo = document.getElementById("titulo");

	contexto.innerHTML = "";
	titulo.innerHTML = "";

	 if (id_opcao == 1){
		contexto.innerHTML = "<p>O IPTU tem como fato gerador a propriedade, o dom&iacute;nio &uacute;til ou a posse de bem im&oacute;vel, por natureza ou por acess&atilde;o f&iacute;sica, conforme definido na lei civil, localizado na zona urbana ou urbaniz&aacute;vel do Munic&iacute;pio, inclusive nas vilas e distritos da sua jurisdi&ccedil;&atilde;o administrativa.</p><p>Entende-se como zona urbana a definida em Lei Municipal, observado o requisito m&iacute;nimo da exist&ecirc;ncia de pelo menos dois dos seguintes melhoramentos, constru&iacute;dos ou mantidos pelo Poder P&uacute;blico:</p><ul>&bull; Meio fio ou cal&ccedil;amento;<br/>&bull; Abastecimento d&acute;&aacute;gua; <br/>&bull; Sistema de esgoto sanit&aacute;rio; <br/>&bull; Rede de ilumina&ccedil;&atilde;o p&uacute;blica, com ou sem posteamento para distribui&ccedil;&atilde;o domiciliar; <br/>&bull; Escola prim&aacute;ria ou posto de sa&uacute;de, a uma dist&acirc;ncia m&aacute;xima de 03 (tr&ecirc;s) quil&ocirc;metros do im&oacute;vel considerado.</ul><p>Consideram-se tamb&eacute;m urbanas as &aacute;reas urbaniz&aacute;veis, ou de expans&atilde;o urbana, constantes de loteamentos ou desmembramentos, aprovados ou n&atilde;o pela Prefeitura, destinados &agrave; habita&ccedil;&atilde;o, &agrave; ind&uacute;stria, ao com&eacute;rcio, recrea&ccedil;&atilde;o ou lazer.</p><p>O fato gerador do IPTU ocorre no primeiro dia de cada ano, ressalvados os pr&eacute;dios constru&iacute;dos durante o exerc&iacute;cio, cujo fato gerador ocorrer&aacute;, inicialmente, na data em que ficar constatada a efetiva constru&ccedil;&atilde;o.</p><p>A incid&ecirc;ncia do imposto alcan&ccedil;a:</p><ul>&bull; Quaisquer im&oacute;veis localizados na zona urbana do Munic&iacute;pio, independentemente de sua forma, estrutura, superf&iacute;cie, destina&ccedil;&atilde;o ou utiliza&ccedil;&atilde;o;<br/>&bull; As edifica&ccedil;&otilde;es cont&iacute;nuas das povoa&ccedil;&otilde;es e as suas &aacute;reas adjacentes, bem como os s&iacute;tios e ch&aacute;caras de recreio ou lazer, ainda que localizados fora da zona urbana, e nos quais a eventual produ&ccedil;&atilde;o n&atilde;o se destine ao com&eacute;rcio;<br/>&bull; Os terrenos arruados ou n&atilde;o, sem edifica&ccedil;&atilde;o ou que houver edifica&ccedil;&atilde;o interditada, paralisada, condenada, em ru&iacute;nas ou em demoli&ccedil;&atilde;o.</ul><p>A incid&ecirc;ncia do imposto independe:</p><ul>&bull; Da legalidade do t&iacute;tulo de aquisi&ccedil;&atilde;o ou de posse do bem im&oacute;vel; <br/>&bull; Do resultado econ&ocirc;mico da explora&ccedil;&atilde;o do bem im&oacute;vel; <br/>&bull; Do cumprimento de quaisquer exig&ecirc;ncias legais, regulamentares ou administrativas relativas ao bem im&oacute;vel; <br/>&bull; O imposto constitui gravame que acompanha o im&oacute;vel em todos os casos de transfer&ecirc;ncia de propriedade ou de direitos a ele relativos.</ul><p>O contribuinte do imposto &eacute; o propriet&aacute;rio do im&oacute;vel, o titular do seu dom&iacute;nio &uacute;til ou seu possuidor a qualquer t&iacute;tulo.</p><p>O imposto &eacute; calculado de acordo com as seguintes al&iacute;quotas:</p><ul>&bull; 1,5% (um e meio por cento) para os terrenos murados; <br/>&bull; 2% (dois por cento) para os terrenos n&atilde;o murados e sem utiliza&ccedil;&atilde;o racional, situados nos loteamentos considerados de classe C e D; <br/>&bull; 3% (tr&ecirc;s por cento) para os terrenos n&atilde;o murados e sem utiliza&ccedil;&atilde;o racional, situados nos loteamentos de classe A e B; <br/>&bull; 1% (um por cento) para os terrenos nos quais existam edifica&ccedil;&otilde;es (pr&eacute;dios de qualquer natureza).</ul><p><div id=topico><b>Do Pagamento</b></div></p><p>O pagamento do IPTU &eacute; anualmente, de uma s&oacute; vez, at&eacute; 31 de mar&ccedil;o de cada ano com redu&ccedil;&atilde;o de 10% (dez por cento) ou em at&eacute; 09 (nove) parcelas sem o desconto.</p><p>A falta de pagamento do imposto nas datas estabelecidas nas parcelas indicadas no documento de pagamento implica em acr&eacute;scimos de atualiza&ccedil;&atilde;o monet&aacute;ria, multa de 5% (Cinco por cento) e juros de 1% (Um por cento) ao m&ecirc;s.</p><p>O pagamento do imposto n&atilde;o confere a quem o fizer, presun&ccedil;&atilde;o de t&iacute;tulo leg&iacute;timo &agrave; propriedade, ao dom&iacute;nio ou &agrave; posse do im&oacute;vel.</p><br/><a href='iptu2010.jsp'><p><b>Emiss&atilde;o do DAM do IPTU</b></p></a>";
		titulo.innerHTML = "IPTU - Imposto Predial e Territorial Urbano&nbsp;";
	}
	else if (id_opcao == 2){
		contexto.innerHTML = "<p>O Imposto Sobre Servi&ccedil;os de Qualquer Natureza tem como fato gerador a presta&ccedil;&atilde;o dos servi&ccedil;os constantes na lista de servi&ccedil;os anexa a Lei Complementar 116 de 31 de Julho de 2003 e Lei Municipal 1.259 de 29 de Dezembro de 2004, ainda que n&atilde;o se constituam como atividade preponderante do prestador.</p><p>O imposto incide tamb&eacute;m sobre o servi&ccedil;o proveniente do exterior do Pa&iacute;s ou cuja presta&ccedil;&atilde;o se tenha iniciado no exterior do Pa&iacute;s.</p><p>O imposto incide ainda sobre os servi&ccedil;os prestados mediante a utiliza&ccedil;&atilde;o de bens e servi&ccedil;os p&uacute;blicos, explorados economicamente mediante autoriza&ccedil;&atilde;o, permiss&atilde;o ou concess&atilde;o, com o pagamento de tarifa, pre&ccedil;o ou ped&aacute;gio pelo usu&aacute;rio final do servi&ccedil;o.</p><p>A incid&ecirc;ncia do imposto n&atilde;o depende da denomina&ccedil;&atilde;o dada ao servi&ccedil;o prestado.</p><p><div id=topico><b>Sujeito Passivo</b></div></p><p>O contribuinte do imposto &eacute; o prestador do servi&ccedil;o.</p><p>S&atilde;o, tamb&eacute;m, respons&aacute;veis pelo pagamento do Imposto em rela&ccedil;&atilde;o aos servi&ccedil;os que lhes sejam prestados, devendo efetuar a reten&ccedil;&atilde;o na fonte:</p>&bull;	O tomador ou intermedi&aacute;rio de servi&ccedil;o proveniente do exterior do Pa&iacute;s ou cuja presta&ccedil;&atilde;o se tenha iniciado no exterior do Pa&iacute;s;<br/>&bull;	A pessoa jur&iacute;dica, ainda que imune ou isenta, tomadora ou intermedi&aacute;ria dos servi&ccedil;os descritos nos subitens 3.05, 7.02, 7.04, 7.05, 7.09, 7.10, 7.12,  7.16, 7.17, 7.19, 11.02, 17.05 e 17.10 da lista de servi&ccedil;os anexa a Lei Complementar 116-2003.<br/>&bull;	Os &Oacute;rg&atilde;os da administra&ccedil;&atilde;o direta da Uni&atilde;o, do Estado e do Munic&iacute;pio, bem como suas respectivas autarquias, empresas p&uacute;blicas, sociedade de economia mista sob seu controle, as funda&ccedil;&otilde;es institu&iacute;das pelo poder p&uacute;blico e as concession&aacute;rias de servi&ccedil;os p&uacute;blicos, estabelecidas ou sediadas no Munic&iacute;pio;<br/>&bull;	Os estabelecimentos banc&aacute;rios e demais entidades financeiras autorizadas a funcionar pelo Banco Central;<br/>&bull;	Incorporadoras, construtoras, empreiteiras e administradoras de obras de constru&ccedil;&atilde;o civil;<br/>&bull;	As empresas concession&aacute;rias, subconcession&aacute;rias e permission&aacute;rias de servi&ccedil;os p&uacute;blicos de energia el&eacute;trica, comunica&ccedil;&otilde;es, telecomunica&ccedil;&otilde;es, g&aacute;s, saneamento b&aacute;sico e distribui&ccedil;&atilde;o de &aacute;gua, pelo Imposto incidente sobre os servi&ccedil;os a elas prestados no territ&oacute;rio do Munic&iacute;pio;<br/>&bull;	As sociedades que explorem servi&ccedil;os de planos de sa&uacute;de ou de assist&ecirc;ncia m&eacute;dica, hospitalar e cong&ecirc;neres ou de seguros, atrav&eacute;s de plano de medicina de grupo e conv&ecirc;nios, pelo Imposto incidente sobre os servi&ccedil;os dos quais resultem remunera&ccedil;&otilde;es ou comiss&otilde;es, por elas pagas a seus agentes, corretores ou intermedi&aacute;rios estabelecidos no Munic&iacute;pio, pelos agenciamentos, corretagens ou intermedia&ccedil;&otilde;es de planos, seguros ou conv&ecirc;nios;<br/>&bull;	As empresas administradoras de aeroportos e de terminais rodovi&aacute;rios, pelo Imposto incidente sobre os servi&ccedil;os a elas prestados no territ&oacute;rio do Munic&iacute;pio.<br/>&bull;	Os hospitais e prontos-socorros, pelo Imposto incidente sobre os servi&ccedil;os a eles prestados no territ&oacute;rio do Munic&iacute;pio.<br/>&bull;	A Empresa Brasileira de Correios e Tel&eacute;grafos, pelo Imposto incidente sobre os servi&ccedil;os prestados por suas ag&ecirc;ncias franqueadas estabelecidas no Munic&iacute;pio e dos quais resultem remunera&ccedil;&otilde;es ou comiss&otilde;es por ela pagas.<br/>&bull;	Os agentes e promotores de eventos de divers&atilde;o, lazer, entretenimento e cong&ecirc;neres;(Lei Complementar n&deg; 1.306/2005).<br/>&bull;	Os tomadores de servi&ccedil;os, em rela&ccedil;&atilde;o aos servi&ccedil;os que lhes sejam prestados sem emiss&atilde;o de nota fiscal; (Lei Complementar n&deg; 1.306/2005).<br/>&bull;	Os tomadores de servi&ccedil;os, em rela&ccedil;&atilde;o aos servi&ccedil;os prestados no territ&oacute;rio do Munic&iacute;pio de Vit&oacute;ria da Conquista, por prestadores de servi&ccedil;os de outros munic&iacute;pios. (Lei Complementar n&deg; 1.306/2005).<br/><p><div id=topico><b>Da Al&iacute;quota e da Base de C&aacute;lculo</b></div></p><p>A base de c&aacute;lculo do imposto &eacute; o pre&ccedil;o do servi&ccedil;o.</p><p>A al&iacute;quota do Imposto Sobre Servi&ccedil;os de Qualquer Natureza &eacute; de 5% (cinco por cento), exceto para os seguinte servi&ccedil;os:</p>&bull;	Servi&ccedil;os de sa&uacute;de, assist&ecirc;ncia m&eacute;dica e cong&ecirc;neres, descritos no item 4 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 3% (tr&ecirc;s por cento);<br/>&bull;	Servi&ccedil;os previstos no item 7.02 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 3% (tr&ecirc;s por cento);<br/>&bull;	Servi&ccedil;os previstos no item 7.05 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 3% (tr&ecirc;s por cento);<br/>&bull;	Servi&ccedil;os previstos no item 8.01 da lista de servi&ccedil;os:a)	quando prestados por estabelecimentos de ensino regular pr&eacute;-escolar e fundamental at&eacute; a 4&ordf; s&eacute;rie, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>b)	quando prestados por estabelecimentos de ensino regular e fundamental da 5&ordf; s&eacute;rie at&eacute; o 3&deg; ano do ensino m&eacute;dio, bem como, de ensino superior, cuja al&iacute;quota &eacute; de 3% (tr&ecirc;s por cento);<br/>&bull;	Servi&ccedil;os previstos no item 8.02 do Anexo 01, cuja al&iacute;quota &eacute; de 3% (tr&ecirc;s por cento);<br/>&bull;	Servi&ccedil;os previstos no item 10.09 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os previstos no item 12.03 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os previstos no item 17.01 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os previstos no item 17.03 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os previstos no item 17.09 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os previstos no item 17.14 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os previstos no item 17.15 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os previstos no item 17.16 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os previstos no item 17.17 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os previstos no item 17.18 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os previstos no item 17.19 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os previstos no item 17.20 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os previstos no item 17.21 da lista de servi&ccedil;os, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os prestados por cooperativas, cuja al&iacute;quota &eacute; de 2% (dois por cento);<br/>&bull;	Servi&ccedil;os prestados por  empresa do ramo atacadista que atuam como Broker comercial, atrav&eacute;s de contrato de representa&ccedil;&atilde;o junto &agrave; industria, nas atividades de log&iacute;stica, distribui&ccedil;&atilde;o de bens, administra&ccedil;&atilde;o de estoques, comercializa&ccedil;&atilde;o de produtos e recebimento de valores para terceiros, cuja al&iacute;quota &eacute; de 2% (dois por cento). (Lei 1.587 - 2008).<p><div id=topico><b>Do Pagamento</b></div></p><p>O Imposto Sobre Servi&ccedil;os de Qualquer Natureza ser&aacute; pago at&eacute; o dia 10 (dez) do m&ecirc;s subseq&uuml;ente ao do servi&ccedil;o prestado.</p><p>Os contribuintes que exercem atividades sujeitas a valores fixos, conforme previsto no Anexo II Lei da Lei 1.259-2004, o pagamento do imposto ser&aacute; feito, integralmente, de uma s&oacute; vez, ou em at&eacute; 04 (quatro) parcelas mensais, no vencimento indicado no calend&aacute;rio fiscal, desde que o valor de cada parcela n&atilde;o seja inferior a R$ 25,00 (vinte e cinco reais).</p><p>O d&eacute;bito fiscal decorrente do n&atilde;o pagamento do imposto na data do vencimento, ter&aacute; seu valor atualizado monetariamente, com base no INPC, sem preju&iacute;zo de outro acr&eacute;scimos previstos em Lei.</p><p><div id=topico><b>Documentos Fiscais</b></div></p><p>Os contribuintes do imposto s&atilde;o obrigados a manter em uso escrita fiscal, destinada ao registro dos servi&ccedil;os prestados, ainda que n&atilde;o tributados.</p><p>Os livros fiscais s&atilde;o de exibi&ccedil;&atilde;o obrigat&oacute;ria ao agente fiscal, devendo ser conservados, por quem deles tiver feito uso, dentro do prazo de 05 (cinco) anos, contados do encerramento da atividade tribut&aacute;vel.</p><p>S&atilde;o documentos exigidos para fins fiscais:</p>&bull;	Nota fiscal de presta&ccedil;&atilde;o de servi&ccedil;os;<br/>&bull;	Nota fiscal - fatura de servi&ccedil;os;<br/>&bull;	Livro de Registro de ISSQN;<br/>&bull;	Declara&ccedil;&atilde;o mensal de servi&ccedil;os (DMS);<br/>&bull;	Declara&ccedil;&atilde;o mensal de reten&ccedil;&atilde;o da fonte (DMRF);<br/>&bull;	Ordem de Servi&ccedil;o;<br/>&bull;	Cupom Fiscal;<br/>&bull;	Carn&ecirc; de pagamento;<br/>&bull;	Cupom de estacionamento;<br/>&bull;	Rol de lavanderia;<br/>&bull;	Bilhete de passagem;<br/>&bull;	Ingresso para divers&otilde;es p&uacute;blicas.<p>Para ver o anexos da Lei n&deg; 1259 DE 2004, <a href='Anexos/Lei_1259.pdf' target='_blank'><b style='color:#364C87'>clique aqui</b></a></p>";
		titulo.innerHTML = "ISSQN - Imposto sobre Servi&ccedil;o de Qualquer Natureza&nbsp;";
	}
	
	else if (id_opcao == 3){
		contexto.innerHTML = "<p>O ITBI incide sobre:<ul></p>&bull; A transmiss&atilde;o, a qualquer t&iacute;tulo, de propriedade ou de dom&iacute;nio &uacute;til de bens im&oacute;veis por natureza ou acess&atilde;o f&iacute;sica, nos termos da Lei Civil;<br/>&bull; A transmiss&atilde;o, a qualquer t&iacute;tulo, de direitos reais sobre im&oacute;veis, exceto os direitos reais de garantia;<br/>&bull; A acess&atilde;o de direitos relativos &agrave;s transmiss&otilde;es referidas nos itens anteriores.</ul><p>Ocorre o fato gerador sempre que o im&oacute;vel objeto da transfer&ecirc;ncia da propriedade ou dos direitos a ele relativos se situe no Munic&iacute;pio de Vit&oacute;ria da Conquista, ainda que o respectivo contrato tenha sido realizado em outro.</p><p>Compreende-se na defini&ccedil;&atilde;o das hip&oacute;teses de incid&ecirc;ncia do imposto as seguintes muta&ccedil;&otilde;es patrimoniais, envolvendo bens im&oacute;veis ou direitos a ele relativos, decorrentes de qualquer fato ou ato &ldquo;inter-vivos&ldquo;.</p><ul>&bull; Compra e venda;<br/>&bull; Da&ccedil;&atilde;o em pagamento;<br/>&bull; Permuta;<br/>&bull; Aquisi&ccedil;&atilde;o por usucapi&atilde;o;<br/>&bull; Mandato em causa pr&oacute;pria ou com poderes equivalentes para transmiss&atilde;o de bem ou direito e seu substabelecimento;<br/>&bull; Institui&ccedil;&atilde;o de enfiteuse ou subenfiteuse e seu resgate;<br/>&bull; Institui&ccedil;&atilde;o de usufruto e habita&ccedil;&atilde;o;<br/>&bull; Institui&ccedil;&atilde;o e substitui&ccedil;&atilde;o de fideicomisso;<br/>&bull; De bem de direito em excesso partilhado ou adjudicado ao c&ocirc;njuge meeiro, em processo de separa&ccedil;&atilde;o ou dissolu&ccedil;&atilde;o de sociedade conjugal, mesmo a t&iacute;tulo de indeniza&ccedil;&atilde;o ou de pagamento de despesas;<br/>&bull; Arremata&ccedil;&atilde;o, adjudica&ccedil;&atilde;o de bens em leil&atilde;o, hasta p&uacute;blica ou pra&ccedil;a, bem como respectivas acess&otilde;es de direito;<br/>&bull; Compromissos ou promessa de compra e venda de im&oacute;veis, sem cl&aacute;usula de arrependimento, e cess&atilde;o de direitos deles decorrentes ou a cess&atilde;o de promessa de acess&atilde;o;<br/>&bull; Transfer&ecirc;ncia de bem ou direito do patrim&ocirc;nio de pessoa jur&iacute;dica para pagamento de capital na parte do valor do im&oacute;vel n&atilde;o utilizada na realiza&ccedil;&atilde;o do capital;<br/>&bull; Transfer&ecirc;ncia de bem ou direito do patrim&ocirc;nio de pessoa jur&iacute;dica para o de qualquer um de seus s&oacute;cios acionistas ou respectivos sucessores;<br/>&bull; Nas partilhas efetuadas em virtude de separa&ccedil;&atilde;o judicial ou div&oacute;rcio, quando o c&ocirc;njuge receber, dos im&oacute;veis situados no Munic&iacute;pio, quota-parte cujo valor seja maior do que o valor de sua mea&ccedil;&atilde;o, na totalidade desses im&oacute;veis;<br/>&bull; Nas divis&otilde;es, para extin&ccedil;&atilde;o de condom&iacute;nio de im&oacute;vel, quando for recebida por qualquer cond&ocirc;mino quota-parte material cujo valor seja maior do que a sua quota-parte ideal.<br/>&bull;Transfer&ecirc;ncia de direito sobre constru&ccedil;&atilde;o em terreno alheio, ainda que feita ao propriet &aacute;rio do solo;<br/>&bull; Cess&atilde;o dos direitos de op&ccedil;&atilde;o de venda, desde que o optante tenha direito &agrave; diferen&ccedil;a de pre&ccedil;o e n&atilde;o a mera comiss&atilde;o;<br/>&bull; Aquisi&ccedil;&atilde;o de terras devolutas;<br/>&bull; Incorpora&ccedil;&atilde;o de bens im&oacute;veis ou direitos reais ao patrim&ocirc;nio da sociedade, cuja atividade preponderante seja a venda ou loca&ccedil;&atilde;o de propriedade imobili &aacute;ria ou a cess&atilde;o de direitos relativos a sua aquisi&ccedil;&atilde;o;<br/>&bull; Quaisquer outros atos ou contratos translativos da propriedade do im&oacute;vel ou de direito a eles relativos situados no munic&iacute;pio, sujeitos a transforma&ccedil;&atilde;o na forma da lei.</ul><p><div id=topico><b>N&atilde;o Incid&ecirc;ncia</b></div></p><p>O imposto n&atilde;o incide sobre a transmiss&atilde;o de bens e direitos a ele relativos quando:</p><ul>&bull; Realizados para o patrim&ocirc;nio da Uni&atilde;o, dos Estados, do Distrito Federal e dos Munic&iacute;pios;<br/>&bull; Realizados para o patrim&ocirc;nio das autarquias e funda&ccedil;&otilde;es institu&iacute;das e mantidas pelo Poder P&uacute;blico e vinculadas a suas finalidades essenciais ou delas decorrentes;<br/>&bull; Realizados para o patrim&ocirc;nio dos partidos pol&iacute;ticos e suas funda&ccedil;&otilde;es, das entidades sindicais dos trabalhadores, das institui&ccedil;&otilde;es de educa&ccedil;&atilde;o e de assist&ecirc;ncia social, sem fins lucrativos, atendidos os requisitos previstos em lei;<br/>&bull; Realizados para o patrim&ocirc;nio das institui&ccedil;&otilde;es religiosas, relativamente ao local destinado ao seu templo;<br/>&bull; Realizados para incorpora&ccedil;&atilde;o ao patrim&ocirc;nio de pessoa jur&iacute;dica em pagamento de capital nela subscrito;<br/>&bull; Decorrente de fus&atilde;o, incorpora&ccedil;&atilde;o, cis&atilde;o ou extin&ccedil;&atilde;o de pessoa jur&iacute;dica.</ul><p>S&atilde;o contribuintes do imposto:</p><ul>&bull; Nas transmiss&otilde;es em geral, os adquirentes dos bens ou direitos transmitidos;<br/>&bull; Nas permutas, cada permutante em rela&ccedil;&atilde;o aos bens ou direitos adquiridos;<br/>&bull; No usufruto e no fideicomisso:<br/><ul>a)	o usufrutu&aacute;rio e o fiduci&aacute;rio, quando da institui&ccedil;&atilde;o;<br/>b)	o propriet&aacute;rio e o fideicomiss&aacute;rio, no momento da extin&ccedil;&atilde;o.</ul></ul><p>Nas transmiss&otilde;es que se efetuarem sem o pagamento do imposto devido, s&atilde;o solidariamente respons&aacute;veis, por esse pagamento, o adquirente e o transmitente, o cession&aacute;rio e o cedente, conforme o caso e, subsidiariamente, o oficial p&uacute;blico, o serventu&aacute;rio e o auxiliar da Justi&ccedil;a ou qualquer servidor p&uacute;blico cuja interfer&ecirc;ncia na forma&ccedil;&atilde;o do t&iacute;tulo de transmiss&atilde;o seja essencial para sua validade e efic&aacute;cia.</p><p><div id=topico><b>Das Al&iacute;quotas&nbsp;</b></div></p><p>Apurada a base de c&aacute;lculo, o imposto ser&aacute; calculado mediante a aplica&ccedil;&atilde;o das seguintes al&iacute;quotas:</p><ul>&bull; 1,5% (um e meio por cento) para as transmiss&otilde;es relativas ao Sistema Financeiro de Habita&ccedil;&atilde;o (SFH);<br/>&bull; 2,5% (dois e meio por cento) nas demais transmiss&otilde;es a t&iacute;tulo oneroso.</ul><p>Nas transmiss&otilde;es compreendidas no Sistema Financeiro de Habita&ccedil;&atilde;o (SFH), sobre o valor excedente n&atilde;o financiado, aplicar-se-&aacute; al&iacute;quota de 2,5% (dois e meio por cento).</p><p>A base de c&aacute;lculo do imposto &eacute; o valor dos bens ou direitos no momento da avalia&ccedil;&atilde;o. Em raz&atilde;o da procrastina&ccedil;&atilde;o do pagamento do imposto da valoriza&ccedil;&atilde;o ou desvaloriza&ccedil;&atilde;o superveniente proceder-se-&aacute; nova avalia&ccedil;&atilde;o.</p><p>A Secretaria de Finan&ccedil;as utilizar &aacute; tabelas de pre&ccedil;os de im&oacute;veis cujos valores servir&atilde;o de teto m&iacute;nimo, ressalvada a avalia&ccedil;&atilde;o contradit&oacute;ria.</p><p><div id=topico><b>Do Lan&ccedil;amento</b></div></p><p>Na Guia de Informa&ccedil;&atilde;o, ser&aacute; obrigat&oacute;ria a men&ccedil;&atilde;o dos seguintes elementos:</p><ul>&bull; Nome, n&uacute;mero do CPF e endere&ccedil;os dos outorgantes e outorgados;<br/>&bull; Natureza do contrato e pre&ccedil;o ou valor da transa&ccedil;&atilde;o;<br/>&bull; &Aacute;rea de terreno e da constru&ccedil;&atilde;o, quando houver, bem como os detalhes referentes a metragem de todas as faces daquele;<br/>&bull; Localiza&ccedil;&atilde;o do im&oacute;vel (rua, n&deg;, distrito, zona, etc) e suas confronta&ccedil;&otilde;es;<br/>&bull; Bases de avalia&ccedil;&atilde;o do im&oacute;vel (tabela, c&oacute;digo, item, al&iacute;quota, valor do imposto, etc);<br/>&bull; N&uacute;meros de inscri&ccedil;&atilde;o do im&oacute;vel no Cadastro Fiscal do Munic&iacute;pio. </ul><p>Sempre que o im&oacute;vel n&atilde;o tenha recebido numera&ccedil;&atilde;o oficial, far-se-&aacute; expressa men&ccedil;&atilde;o &agrave; dist&acirc;ncia em que se encontra o n&uacute;mero mais pr&oacute;ximo ou qualquer ponto facilmente identific&aacute;vel, bem como o nome das ruas entre as quais se localiza.</p><p>Tratando-se de im&oacute;vel constante de plantas de terrenos, arruados por particulares ou empresas imobili&aacute;rias, citar-se-&aacute; na guia o n&uacute;mero do lote, quadra correspondente e, se for o caso, o nome do loteamento.</p><p>Nas guias em que se objetive transmiss&atilde;o de im&oacute;veis ou direitos a eles relativos, pertencentes &agrave; zona rural, incluir-se-&aacute;, mais as seguintes caracter&iacute;sticas:</p><ul>&bull; N&uacute;mero do certificado do registro imobili&aacute;rio;<br/>&bull; Denomina&ccedil;&atilde;o pela qual &eacute; conhecido o im&oacute;vel e a sua &aacute;rea;<br/>&bull; Dist&acirc;ncia aproximada da sede do Munic&iacute;pio;<br/>&bull; Refer&ecirc;ncia &agrave;s culturas existentes e valor aproximado;<br/>&bull; Exist&ecirc;ncia de jazidas minerais, quedas d'&aacute;gua, fontes radioativas, t&eacute;rmicas, minerais e outras acess&otilde;es naturais, com indica&ccedil;&atilde;o de seus valores;<br/>&bull; Men&ccedil;&atilde;o da exist&ecirc;ncia ou n&atilde;o de edifica&ccedil;&otilde;es de terceiros.</ul><br/><a href='guiaITBI.jsp'><p><b>Emiss&atilde;o de Guia ITBI</b></p></a>";
		titulo.innerHTML = "ITBI - Imposto sobre Transmiss&atilde;o de Bens Im&oacute;veis&nbsp;";
	}			
		if (id_opcao == 5){
		contexto.innerHTML = "<span id='contextoDF' style='font-size:11px; color:#000' ><div id='pergunta'><b>&nbsp;O QUE &Eacute; CERTID&Atilde;O NEGATIVA?</b></div><p>Certid&atilde;o Negativa &eacute; um documento expedido regularmente ao contribuinte, pela Secretaria de Finan&ccedil;as do Munic&iacute;pio de Vit&oacute;ria da Conquista, dando prova de quita&ccedil;&atilde;o de d&iacute;vidas do usu&aacute;rio para com o Munic&iacute;pio.</p><div id='pergunta'><b>&nbsp;QUAL O PRAZO DE ENTREGA DE UMA CERTID&Atilde;O NEGATIVA?</b></div><p>Caso todos os d&eacute;bitos tribut&aacute;rios estejam pagos, as Certid&otilde;es Negativas de D&eacute;bitos ser&atilde;o fornecidas imediatamente, bastando para isso o requerente dirigir-se &agrave; Ger&ecirc;ncia de Cadastro e Lan&ccedil;amento e retirar diretamente com um dos atendentes.</p><div id='pergunta'><b>QUAL A DATA DE VALIDADE DA CERTID&Atilde;O NEGATIVA?</b></div><p>Validade: 30 dias.</p><div id='pergunta'><b>QUAIS OS PROCEDIMENTOS PARA SOLICITA&Ccedil;&Atilde;O DE CERTID&Atilde;O INFORMATIVA DE AVERBA&Ccedil;&Atilde;O DE &Aacute;REA CONSTRU&Iacute;DA?</b></div><p>Requerer junto &agrave; Gerencia de Cadastro e Lan&ccedil;amento, informar inscri&ccedil;&atilde;o imobili&aacute;ria do IPTU (Os d&eacute;bitos do im&oacute;vel dever&atilde;o estar quitados). Conferir junto &aacute; Ger&ecirc;ncia de Cadastro a data de cadastramento, a &aacute;rea territorial, a &aacute;rea edificada e a testada principal do im&oacute;vel.</p><div id='pergunta'><b>QUAL A DOCUMENTA&Ccedil;&Atilde;O NECESS&Aacute;RIA PARA AS CERTID&Otilde;ES DE TRIBUTOS MUNICIPAIS?</b></div><p>CPF ou CNPJ e RG.</p><div id='pergunta'><b>QUAL O ENDERE&Ccedil;O E HOR&Aacute;RIO DE EXPEDIENTE DO SETOR DE CERTID&Atilde;O NEGATIVA?</b></b></div><p>Hor&aacute;rio de Funcionamento: 10:00 hs &agrave;s 12:00 hs e 14:00 hs &agrave;s 18:00 hs  de segunda a sexta feira.<br/>Endere&ccedil;o: Pra&ccedil;a Tancredo Neves ao Lado da Catedral no pr&eacute;dio da Secretaria de Finan&ccedil;as</p><br/><center><p><a href='duvidasFrequentes.jsp'><img src='imagens/btIndice.jpg' border='0'/></a><a href='orientacoes.jsp?id=6'><img src='imagens/btProximo.jpg' border='0'  /></a></p></span>";
		titulo.innerHTML = "Certid&atilde;o Negativa&nbsp;";
	}
	else if (id_opcao == 6){
		contexto.innerHTML = "<span id='contextoDF' style='font-size:11px; color:#000'><div id='pergunta'><b>O QUE &Eacute; D&Iacute;VIDA ATIVA?</b></div><p>Em outras palavras, D&iacute;vida Ativa do Munic&iacute;pio &eacute; todo o n&atilde;o pagamento, por parte do contribuinte de Impostos, taxas, contribui&ccedil;&otilde;es de melhorias, inclusive multa de qualquer natureza, inscritas no setor competente da Secretaria de Finan&ccedil;as, depois de esgotado o prazo de seus pagamentos. </p><div id='pergunta'><b>OS PARCELAMENTOS DE D&Eacute;BITOS S&Atilde;O FEITOS TAMB&Eacute;M NO SETOR DA D&Iacute;VIDA ATIVA?</b></div><p>Sim. A D&iacute;vida Ativa &eacute; um &oacute;rg&atilde;o da Secretaria de Finan&ccedil;as do Munic&iacute;pio de Vit&oacute;ria da Conquista que cuida de todos os parcelamentos tribut&aacute;rios e n&atilde;o tribut&aacute;rios, emitindo guias de recolhimento dos mesmos e promovendo a execu&ccedil;&atilde;o fiscal.</p><div id='pergunta'><b>COMO &Eacute; FEITO O PARCELAMENTO DE D&Eacute;BITO DO ISS E IPTU?</b></div><p>O parcelamento dever&aacute; ser feito junto a D&iacute;vida Ativa, tendo que ser requerido pelo contribuinte emitindo termo de confiss&atilde;o, podendo o parcelamento ser autorizado em at&eacute; 24 meses ou em at&eacute; 60 meses desde que o valor mensal n&atilde;o seja inferior a R$ 100,00 (Cem Reais). </p><div id='pergunta'><b>O QUE &Eacute; CERTID&Atilde;O DA D&Iacute;VIDA ATIVA?</b></div><p>&Eacute; um documento que comprova que a d&iacute;vida foi inscrita e com ela pode a Procuradoria Fiscal do Munic&iacute;pio promover uma a&ccedil;&atilde;o, para receber em ju&iacute;zo o que n&atilde;o foi pago.</p><div id='pergunta'><b>SE A D&Iacute;VIDA ATIVA J&Aacute; ESTIVER SENDO EXECUTADA PODE SER PARCELADA?</b></div><p>Sim. Mesmo ajuizada ainda &eacute; poss&iacute;vel parcelar a d&iacute;vida junto &agrave; Procuradoria Fiscal, mediante acordo judicial. Para tanto, o contribuinte dever&aacute; comparecer &aacute; Ger&ecirc;ncia de Divida Ativa.</p><br/><center><p><a href='orientacoes.jsp?id=5'><img src='imagens/btAnterior.jpg' border='0'/></a><a href='duvidasFrequentes.jsp'><img src='imagens/btIndice.jpg' border='0'/></a><a href='orientacoes.jsp?id=7'><img src='imagens/btProximo.jpg' border='0'  /></a></p></span> ";
		titulo.innerHTML = "D&iacute;vida Ativa&nbsp;";
	}	
	else if (id_opcao == 7){
		contexto.innerHTML = "<span id='contextoDF' style='font-size:11px; color:#000'><div id='pergunta'><b>O QUE &Eacute; O IPTU?</b></div><p>&Eacute; o imposto recolhido anualmente, sobre o valor da propriedade predial e territorial urbana. O IPTU incide sobre todos os im&oacute;veis do Munic&iacute;pio de Vit&oacute;ria da Conquista, exceto os imunes e isentos.</p><div id='pergunta'><b>QUEM &Eacute; O CONTRIBUINTE DO IPTU?</b></div><p>O propriet&aacute;rio do im&oacute;vel, titular do seu dom&iacute;nio &uacute;til, ou o seu possuidor a qualquer t&iacute;tulo. Esse tributo acompanha o im&oacute;vel em todas as suas mudan&ccedil;as, ou seja, nos casos de venda, doa&ccedil;&atilde;o, desmembramento, etc.</p><div id='pergunta'><b>TODO IM&Oacute;VEL PRECISA TER UMA INSCRI&Ccedil;&Atilde;O NA PREFEITURA?</b></div><p>S&atilde;o obrigat&oacute;rias, as inscri&ccedil;&otilde;es no CADASTRO IMOBILI&Aacute;RIO da Secretaria de Finan&ccedil;as do Munic&iacute;pio, os im&oacute;veis urbanos existentes como unidades aut&ocirc;nomas em Vit&oacute;ria da Conquista e os que venham a surgir por desmembramento ou remembramento dos atuais, mesmo os beneficiados com isen&ccedil;&otilde;es e imunidades.</p><div id='pergunta'><b>COMO &Eacute; CALCULADO O VALOR DO IPTU?</b></div><p>O IPTU &eacute; calculado mediante a aplica&ccedil;&atilde;o de uma al&iacute;quota sobre o valor venal do im&oacute;vel. Este valor venal &eacute; calculado com base em diversos fatores, como: tamanho do terreno, do pr&eacute;dio, sua localiza&ccedil;&atilde;o, o tipo de acabamento, os tipos de equipamentos urbanos existentes no logradouro, entre outras.</p><p>O C&oacute;digo Tribut&aacute;rio Municipal estabelece o seguinte:</p><p>Art. 179 - Apurado o valor venal, pelos crit&eacute;rios indicados, o imposto ser&aacute; calculado de acordo com as seguintes al&iacute;quotas:</p><p>I.	1,5% (um e meio por cento) para os terrenos murados;</p><p>II.	2% (dois por cento) para os terrenos n&atilde;o murados e sem utiliza&ccedil;&atilde;o racional, situados nos loteamentos considerados de classe C e D;</p><p>III.	3% (tr&ecirc;s por cento) para os terrenos n&atilde;o murados e sem utiliza&ccedil;&atilde;o racional, situados nos loteamentos de classe A e B;</p><p>IV.	1% (um por cento) para os terrenos nos quais existam edifica&ccedil;&otilde;es (pr&eacute;dios de qualquer natureza).</p><div id='pergunta'><b>COMO &Eacute; CALCULADO O IPTU DE UM IM&Oacute;VEL DE USO MISTO, RESIDENCIAL E COMERCIAL?</b></div><p>O C&aacute;lculo do IPTU &eacute; feito proporcional a &aacute;rea utilizada pelo com&eacute;rcio e resid&ecirc;ncia.</p><div id='pergunta'><b>O QUE DEVE SER FEITO QUANDO O CONTRIBUINTE N&Atilde;O RECEBER O BOLETO DE PAGAMENTO DO IPTU?</b></div><p>Se n&atilde;o receber o seu boleto de pagamento dever&aacute;  procurar a Secretaria de Finan&ccedil;as para tirar a 2&ordf; via e regularizar sua informa&ccedil;&atilde;o cadastral.</p><p>O vencimento do seu IPTU &eacute; em 31 de mar&ccedil;o de cada ano.</p><div id='pergunta'><b>ONDE PAGAR O IPTU?</b></div><p>O IPTU pode ser pago nas Casas Lot&eacute;ricas, cooperativas vinculadas &agrave; rede SICOOB, no Banco do Brasil e na Caixa Econ&ocirc;mica Federal.</p><div id='pergunta'><b>O IPTU PODE SER PAGO EM OUTROS ESTADOS?</b></div><p>Sim. Os contribuintes do IPTU residentes em outros estados da federa&ccedil;&atilde;o poder&atilde;o emitir diretamente no site da Secretaria de Finan&ccedil;as e pagar no Banco do Brasil ou na Caixa Econ&ocirc;mica Federal, como tamb&eacute;m nas casas lot&eacute;ricas.</p><div id='pergunta'><b>AO RECEBER A GUIA DE RECOLHIMENTO O CONTRIBUINTE PODE PEDIR A REVIS&Atilde;O DO IPTU?</b></div><p>Sim. O documento de Arrecada&ccedil;&atilde;o Municipal - DAM &eacute; um documento importante. Por isso, se voc&ecirc; achar que nele alguma coisa n&atilde;o est&aacute; perfeitamente correta, entre em contato com a Secretaria de Finan&ccedil;as do Munic&iacute;pio. Verifique se &eacute; necess&aacute;rio fazer alguma altera&ccedil;&atilde;o cadastral do seu im&oacute;vel.</p><p>&Eacute; importante manter seus dados cadastrais de propriet&aacute;rio sempre atualizados, como: nome, endere&ccedil;o para correspond&ecirc;ncia, CPF, CNPJ e informa&ccedil;&otilde;es sobre altera&ccedil;&otilde;es no seu im&oacute;vel - reformas, obras suplementares ou quaisquer modifica&ccedil;&otilde;es. </p><p>Vale lembrar que o prazo para questionar o valor do seu IPTU anual, &eacute; de 30 (trinta) dias ap&oacute;s o primeiro vencimento da cota &uacute;nica.</p><br/><center><p><a href='orientacoes.jsp?id=6'><img src='imagens/btAnterior.jpg' border='0'/></a><a href='duvidasFrequentes.jsp'><img src='imagens/btIndice.jpg' border='0'/></a><a href='orientacoes.jsp?id=8'><img src='imagens/btProximo.jpg' border='0'  /></a></p></span>";
		titulo.innerHTML = "IPTU&nbsp;";
	}	
	
	if (id_opcao == 8){
		contexto.innerHTML = "<span id='contextoDF' style='font-size:11px; color:#000'><div id='pergunta'><b>O &Eacute; O ISS?</b></div><p>&Eacute; o Imposto Sobre Servi&ccedil;os de Qualquer Natureza, que tem como fato gerador a presta&ccedil;&atilde;o de servi&ccedil;os constantes da lista de servi&ccedil;os anexa a Lei Complementar Federal n&deg; 116 de 31 de Julho de 2003 e Lei Municipal n &deg; 1.259 de 22 de Dezembro de 2004.</p><div id='pergunta'><b>QUEM &Eacute; OBRIGADO A PAGAR O ISS?</b></div><p>A empresa, a sociedade civil e o profissional aut&ocirc;nomo que exerce atividade econ&ocirc;mica de presta&ccedil;&atilde;o de servi&ccedil;os, constante na lista de servi&ccedil;os.</p><div id='pergunta'><b>QUAL A DOCUMENTA&Ccedil;&Atilde;O NECESS&Aacute;RIA PARA SE INSCREVER NO CADASTRO FISCAL?</b></div><p>A inscri&ccedil;&atilde;o no Cadastro Fiscal, sua retifica&ccedil;&atilde;o, altera&ccedil;&otilde;es ou baixa ser&atilde;o efetivadas com bases em declara&ccedil;&otilde;es prestadas pelos contribuintes, ou em levantamentos efetuados pelo fisco municipal.</p><p>O Prazo para solicitar inscri&ccedil;&atilde;o e altera&ccedil;&otilde;es cadastrais &eacute; de 30 (trinta) dias, a contar do ato ou do fato que os motivarem</p><p>a)	Sociedades: Apresentar c&oacute;pia do contrato social e suas altera&ccedil;&otilde;es, caso o contrato n&atilde;o esteja consolidado;</p><p>b)	Firma Individual: Apresentar c&oacute;pia da Declara&ccedil;&atilde;o de firma individual; </p><p>c)	Associa&ccedil;&otilde;es, cooperativas, etc.: Apresentar estatuto, e ultima ata de elei&ccedil;&atilde;o e posse da diretoria;</p><p>d)	C&oacute;pia do CNPJ (Exceto aut&ocirc;nomos e feirantes);</p><p>e)	C&oacute;pia da identidade e CPF (No caso de firma individual, aut&ocirc;nomos e sociedade LTDA);</p><p>f)	Laudo de vistoria do corpo de bombeiros (Apresentar &agrave; Ger&ecirc;ncia de Posturas, se for o caso);</p><p>g)	Laudo de vistoria da Ger&ecirc;ncia de Posturas da Prefeitura Municipal:</p><p>h)	CAE - Cadastro de Atividades Econ&ocirc;micas, preenchido e assinado pelo representante legal da empresa;</p><p>i)	C&oacute;pia do DAM do IPTU do im&oacute;vel;</p><p>j)	Certid&atilde;o Negativa de d&eacute;bito municipal - CND;</p><p>k)	Empresas que necessitem de respons&aacute;vel t&eacute;cnico dever&atilde;o informar o respectivo profissional.</p><p>l)	Anexar Procura&ccedil;&atilde;o com c&oacute;pia do R. G. e CPF do outorgado caso n&atilde;o seja o pr&oacute;prio requerente a realizar a inscri&ccedil;&atilde;o.</p><p>m)	Im&oacute;vel alugado anexar c&oacute;pia do contrato de aluguel </p><p>n)	Sociedades de profissionais liberais: Apresentar c&oacute;pia das carteiras de inscri&ccedil;&atilde;o dos profissionais nos respectivos Conselhos e entidades reguladoras da profiss&atilde;o de todos os s&oacute;cios.</p><div id='pergunta'><b>DOCUMENTOS NECESS&Aacute;RIOS PARA INSCRI&Ccedil;&Atilde;O DE AUT&Ocirc;NOMOS E FEIRANTES:</b></div><p>a)	C&oacute;pia da identidade e CPF;</p><p>b)	Cadastro de Atividade Econ&ocirc;mica devidamente preenchido;</p><p>c)	C&oacute;pia do DAM referente ao IPTU do im&oacute;vel;</p><p>d)	Certid&atilde;o Negativa de d&eacute;bito municipal;</p><p>e)	C&oacute;pia do R.G. e CPF do outorgado caso n&atilde;o seja o pr&oacute;prio requerente  a solicitar a inscri&ccedil;&atilde;o.</p><p>f)	C&oacute;pia do contrato de aluguel quando se tratar de im&oacute;vel alugado;</p><p>g)	Aut&ocirc;nomo em logradouro p&uacute;blico: Apresentar autoriza&ccedil;&atilde;o da Gerencia de Posturas;</p><p>h)	Feirantes: Apresentar autoriza&ccedil;&atilde;o da Ger&ecirc;ncia de Servi&ccedil;os B&aacute;sicos </p><p>Para as atividades, abaixo relacionadas, necess&aacute;rio, ainda, anexar c&oacute;pia da carteira de habilita&ccedil;&atilde;o dos respectivos Conselhos e Entidades reguladoras da profiss&atilde;o:</p><p>a)	Advogados: OAB;</p><p>b)	Massoterapeutas: CRF;</p><p>c)	M&eacute;dicos: CRM;</p><p>d)	Dentistas: CRO;</p><p>e)	Veterin&aacute;rios: CRMV;</p><p>f)	Enfermeiros: COREN;</p><p>g)	Farmac&ecirc;uticos: CRF;</p><p>h)	Engenheiros: CREA;</p><p>i)	Arquitetos: CREA;</p><p>j)	Agr&ocirc;nomos: CREA;</p><p>k)	Psic&oacute;logos: CRP;</p><p>l)	Economistas: CORECON;</p><p>m)	Contadores: CRC;</p><p>n)	Administradores: CRA.</p><div id='pergunta'><b>DOCUMENTOS NECESS&Aacute;RIOS PARA INSCRI&Ccedil;&Atilde;O DE CIRCOS, PARQUES E CONG&Ecirc;NERES:</b></div><p>a)	CNPJ;</p><p>b)	Autoriza&ccedil;&atilde;o do propriet&aacute;rio do im&oacute;vel ou contrato de aluguel;</p><p>c)	Laudo de vistoria dos bombeiros;</p><p>d)	Laudo de vistoria da Ger&ecirc;ncia de Posturas da Prefeitura Municipal;</p><p>e)	&ldquo;ART&rdquo; (Anota&ccedil;&atilde;o do Respons&aacute;vel T&eacute;cnico) do CREA.</p><div id='pergunta'><b>REQUISITOS NECESS&Aacute;RIOS PARA INSCRI&Ccedil;&Atilde;O DE TRANSPORTE ESCOLAR, MOTORISTAS E TAXISTAS:</b></div><p>a)	Carteira Nacional de Habilita&ccedil;&atilde;o;</p><p>b)	Certid&atilde;o do SIMTRANS autorizando o cadastramento;</p><p>c)	Mesmos documentos exigidos para empresas ou aut&ocirc;nomos.</p><br/><center><p><a href='orientacoes.jsp?id=7'><img src='imagens/btAnterior.jpg' border='0'/></a><a href='duvidasFrequentes.jsp'><img src='imagens/btIndice.jpg' border='0'/></a><a href='orientacoes.jsp?id=9'><img src='imagens/btProximo.jpg' border='0'  /></a></p></span>";
		titulo.innerHTML = "ISS&nbsp;";
	}	
	
	else if (id_opcao == 9){
		contexto.innerHTML = "<span id='contextoDF' style='font-size:11px; color:#000'><div id='pergunta'><b>O QUE &Eacute; O ITBI?</b></div><p>&Eacute; o imposto sobre transmiss&atilde;o &ldquo;inter vivos&rdquo;, por ato oneroso, de bens im&oacute;veis ou direitos reais a eles relativos.</p><div id='pergunta'><b>QUEM S&Atilde;O OS CONTRIBUINTES DO ITBI?</b></div><p>O recolhimento do ITBI ao Munic&iacute;pio &eacute; de responsabilidade do adquirente ou cession&aacute;rio do bem ou direito, podendo ser efetuado tamb&eacute;m, pelo cedente ou transmitente como respons&aacute;vel. No caso especial de permuta (troca de im&oacute;veis) o ITBI passa a incidir sobre o valor de cada um dos bens permutados.</p><div id='pergunta'><b>QUANDO &Eacute; NECESS&Aacute;RIO PAGAR O ITBI?</b></div><p>O recolhimento do ITBI &eacute; obrigat&oacute;rio sempre que houver transmiss&atilde;o onerosa de bem im&oacute;vel, a qualquer t&iacute;tulo, seja essa transmiss&atilde;o da propriedade, de direitos reais ou de cess&atilde;o de direito.</p><div id='pergunta'><b>QUANTOS POR CENTO DO VALOR DO IM&Oacute;VEL O CONTRIBUINTE PAGA DE ITBI?</b></div><p>A al&iacute;quota do ITBI &eacute; calculada em 2,5% sobre o valor de mercado do im&oacute;vel.</p><div id='pergunta'><b>EXISTE ALGUM BENEF&Iacute;CIO PARA OS CONTRIBUINTES QUE PAGAM ITBI DE IM&Oacute;VEIS?</b></div><p>Sim.</p><p>No caso de financiamento pelo SFH, e somente por ele, o propriet&aacute;rio paga 1,5% sobre o valor financiado com recursos do SFH e 2,5% sobre o valor n&atilde;o financiado.</p><div id='pergunta'><b>QUAL A BASE DE C&Aacute;LCULO NA AVALIA&Ccedil;&Atilde;O PARA PAGAMENTO DO ITBI?</b></div><p>A base de c&aacute;lculo do ITBI &eacute; feita sobre o pre&ccedil;o de mercado do bem im&oacute;vel, determinado pela Administra&ccedil;&atilde;o Tribut&aacute;ria,  no m&ecirc;s de pagamento.</p><div id='pergunta'><b>O ITBI PODER&Aacute; SER RESTITU&Iacute;DO?</b></div><p>Sim.</p><p>O ITBI poder&aacute; ser restitu&iacute;do ao contribuinte, quando:</p><p>&bull;	N&atilde;o se completar o ato ou contrato que deu origem;</p><p>&bull;	For declarada, por decis&atilde;o judicial, a nulidade do ato ou contrato;</p><p>&bull;	For declarada a n&atilde;o incid&ecirc;ncia ou reconhecida a isen&ccedil;&atilde;o;</p><p>&bull;	Houver sido recolhido a maior.</p><div id='pergunta'><b>QUAL A DOCUMENTA&Ccedil;&Atilde;O NECESS&Aacute;RIA PARA PAGAR O ITBI?</b></div><p>&bull;	Dirigir-se a Secretaria de Finan&ccedil;as com as Guias de ITBI contendo as informa&ccedil;&otilde;es especificadas no CTM conforme a seguir:</p><p>Art. 245 - Nas Guias de Informa&ccedil;&atilde;o relativas a transmiss&atilde;o de im&oacute;veis ou direitos a eles relativos, pertencentes a zona urbana, ser&aacute; obrigat&oacute;ria a men&ccedil;&atilde;o dos seguintes elementos:</p><p>I.	nome, n&uacute;mero do CPF e endere&ccedil;os dos outorgantes e outorgados;</p><p>II.	natureza do contrato e pre&ccedil;o ou valor da transa&ccedil;&atilde;o;</p><p>III.	&aacute;rea de terreno e da constru&ccedil;&atilde;o, quando houver, bem como os detalhes referentes a metragem de todas as faces daquele;</p><p>IV.	localiza&ccedil;&atilde;o do im&oacute;vel (rua, n&deg;, distrito, zona, etc) e suas confronta&ccedil;&otilde;es;</p><p>V.	bases de avalia&ccedil;&atilde;o do im&oacute;vel (tabela, c&oacute;digo, item, al&iacute;quota, valor do imposto, etc);</p><p>VI.	n&uacute;meros de inscri&ccedil;&atilde;o do im&oacute;vel no Cadastro Fiscal do Munic&iacute;pio.</p><p>&sect; 1&deg; - Sempre que o im&oacute;vel n&atilde;o tenha recebido numera&ccedil;&atilde;o oficial, far-se-&aacute; expressa men&ccedil;&atilde;o &agrave; dist&acirc;ncia em que se encontra o n&uacute;mero mais pr&oacute;ximo ou qualquer ponto  facilmente identific&aacute;vel, bem como o nome das ruas entre as quais se localiza.</p><p>&sect; 2&deg; - Tratando-se de im&oacute;vel constante de plantas de terrenos, arruados por particulares ou empresas imobili&aacute;rias, citar-se-&aacute; na guia o n&uacute;mero do lote, quadra correspondente e, se for o caso, o nome do loteamento.</p><p>Art. 246 - Nas guias em que se objetive transmiss&atilde;o de im&oacute;veis ou direitos a eles relativos, pertencentes &agrave; zona rural, incluir-se-&atilde;o, obrigatoriamente, al&eacute;m do que se menciona nos incisos &ldquo;I&rdquo;, &ldquo;II&rdquo;, &ldquo;III&rdquo;, &ldquo;IV&rdquo; e &ldquo;V&rdquo; do artigo anterior, mais as seguintes caracter&iacute;sticas:</p><p>I.	n&uacute;mero do certificado do registro imobili&aacute;rio;</p><p>II.	denomina&ccedil;&atilde;o pela qual &eacute; conhecido o im&oacute;vel e a sua &aacute;rea;</p><p>III.	dist&acirc;ncia aproximada da sede do Munic&iacute;pio;</p><p>IV.	refer&ecirc;ncia &agrave;s culturas existentes e valor aproximado;</p><p>V.	exist&ecirc;ncia de jazidas minerais, quedas d'&aacute;gua, fontes radioativas, t&eacute;rmicas, minerais e outras acess&otilde;es naturais, com indica&ccedil;&atilde;o de seus valores;</p><p>VI.	men&ccedil;&atilde;o da exist&ecirc;ncia ou n&atilde;o de edifica&ccedil;&otilde;es de terceiros.</p><div id='pergunta'><b>QUAL O LOCAL DE ATENDIMENTO DO ITBI NA PREFEITURA?</b></div><p>Gerencia de Cadastro e Lan&ccedil;amento, setor de ITBI, na Secretaria de Finan&ccedil;as.</p><br/><center><p><a href='orientacoes.jsp?id=8'><img src='imagens/btAnterior.jpg' border='0'/></a><a href='duvidasFrequentes.jsp'><img src='imagens/btIndice.jpg' border='0'/></a><a href='orientacoes.jsp?id=10'><img src='imagens/btProximo.jpg' border='0'  /></a></p></span>";
		titulo.innerHTML = "ITBI&nbsp;";
	}
	
	else if (id_opcao == 10){
		contexto.innerHTML = "<span id='contextoDF' style='font-size:11px; color:#000'><br/><div id='pergunta'><b>ONDE O CONTRIBUINTE PODER&Aacute; CONSULTAR AS LEIS REFERENTES AOS TRIBUTOS MUNICIPAIS DE VIT&Oacute;RIA DA CONQUISTA?</b></div><br/><br/><p>No Site: <a href='http://www.pmvc.ba.gov.br' target='_blanck'><b style='color:#364C87'>www.pmvc.ba.gov.br</b></a> em servi&ccedil;os SEFIN</p><p>No Site: <a href='http://www.diariooficialdosmunicipios.org/prefeitura/vitoriadaconquista/' target='_blanck'><b style='color:#364C87'>www.diariooficialdosmunicipios.org/prefeitura/vitoriadaconquista/</b></a></p><p>No link : <a href='http://sefin.pmvc.ba.gov.br'><b style='color:#364C87'>http://sefin.pmvc.ba.gov.br</b></a></p><br/><center><a href='orientacoes.jsp?id=9'><img src='imagens/btAnterior.jpg' border='0'/></a><a href='duvidasFrequentes.jsp'><img src='imagens/btIndice.jpg' border='0'/></a><a href='orientacoes.jsp?id=11'><img src='imagens/btProximo.jpg' border='0'  /></a><br/></span>";
		titulo.innerHTML = "Legisla&ccedil;&atilde;o Tribut&aacute;ria&nbsp;";
	}
	
	else if (id_opcao == 11){
		contexto.innerHTML = "<span id='contextoDF' style='font-size:11px; color:#000'><div id='pergunta'><b>O QUE S&Atilde;O OBRIGA&Ccedil;&Otilde;ES ACESS&Oacute;RIAS DO ISS?</b></div><p>S&atilde;o as obriga&ccedil;&otilde;es legais a que est&atilde;o sujeitos todos os contribuintes do ISSQN, com exce&ccedil;&atilde;o do pagamento do imposto, que &eacute; a obriga&ccedil;&atilde;o principal.</p><div id='pergunta'><b>QUAL A FINALIDADE DAS OBRIGA&Ccedil;&Otilde;ES ACESS&Oacute;RIAS DO ISS?</b></div><p>Facilitar a administra&ccedil;&atilde;o tribut&aacute;ria, uma vez que envolve as declara&ccedil;&otilde;es fornecidas pelo contribuinte do imposto, o estabelecimento de prazos a serem cumpridos, a especifica&ccedil;&atilde;o de penalidades, etc.</p><br/><center><p><a href='orientacoes.jsp?id=10'><img src='imagens/btAnterior.jpg' border='0'/></a><a href='duvidasFrequentes.jsp'><img src='imagens/btIndice.jpg' border='0'/></a><a href='orientacoes.jsp?id=12'><img src='imagens/btProximo.jpg' border='0'  /></a></p></span>";
		titulo.innerHTML = "Obriga&ccedil;&otilde;es Acess&oacute;rias do ISS&nbsp;";
	}
	
	else if (id_opcao == 12){
		contexto.innerHTML = "<span id='contextoDF' style='font-size:11px; color:#000'><div id='pergunta'><b>O QUE &Eacute; A SUBSTITUI&Ccedil;&Atilde;O TRIBUT&Aacute;RIA?</b></div><p>A substitui&ccedil;&atilde;o tribut&aacute;ria &eacute; um instituto jur&iacute;dico inclu&iacute;do em nosso ordenamento legal pelo C&oacute;digo Tribut&aacute;rio Nacional (art.128), pela Emenda Constitucional 03/93, pela Lei Complementar 116, no caso do ISS.</p><p>Consiste em atribuir responsabilidade pelo pagamento do imposto a uma terceira pessoa que tenha rela&ccedil;&atilde;o com o fato gerador da obriga&ccedil;&atilde;o tribut&aacute;ria.</p><p>No munic&iacute;pio de Vit&oacute;ria da Conquista, foi institu&iacute;da no C&oacute;digo Tribut&aacute;rio Municipal Lei 1.259/2004.</p><div id='pergunta'><b>QUEM S&Atilde;O OS SUBSTITUTOS TRIBUT&Aacute;RIOS NO MUNIC&Iacute;PIO DE VIT&Oacute;RIA DA CONQUISTA?</b> (art. 206 do CTM)</div><p>Art. 206 - S&atilde;o, tamb&eacute;m, respons&aacute;veis pelo pagamento do Imposto em rela&ccedil;&atilde;o aos servi&ccedil;os que lhes sejam prestados, devendo efetuar a reten&ccedil;&atilde;o na fonte:</p><p>I - o tomador ou intermedi&aacute;rio de servi&ccedil;o proveniente do exterior do Pa&iacute;s ou cuja presta&ccedil;&atilde;o se tenha iniciado no exterior do Pa&iacute;s;</p><p>II - a pessoa jur&iacute;dica, ainda que imune ou isenta, tomadora ou intermedi&aacute;ria dos servi&ccedil;os descritos nos subitens 3.05, 7.02, 7.04, 7.05, 7.09, 7.10, 7.12,  7.16, 7.17, 7.19, 11.02, 17.05 e 17.10 do Anexo I desta esta lei.</p><p>III - Os &Oacute;rg&atilde;os da administra&ccedil;&atilde;o direta da Uni&atilde;o, do Estado e do Munic&iacute;pio, bem como suas respectivas autarquias, empresas p&uacute;blicas, sociedade de economia mista sob seu controle, as funda&ccedil;&otilde;es institu&iacute;das pelo poder p&uacute;blico e as concession&aacute;rias de servi&ccedil;os p&uacute;blicos, estabelecidas ou sediadas no Munic&iacute;pio, tomadores ou intermedi&aacute;ria dos servi&ccedil;os descritos no Anexo I desta lei.</p><p>IV - Os estabelecimentos banc&aacute;rios e demais entidades financeiras autorizadas a funcionar pelo Banco Central, tomadores ou intermedi&aacute;rios dos servi&ccedil;os descritos no Anexo I desta lei;</p><p>V - Incorporadoras, construtoras, empreiteiras e administradoras de obras de constru&ccedil;&atilde;o civil, tomadores ou intermedi&aacute;rios dos servi&ccedil;os descritos no Anexo I;</p><p>VI - as empresas concession&aacute;rias, subconcession&aacute;rias e permission&aacute;rias de servi&ccedil;os p&uacute;blicos de energia el&eacute;trica, comunica&ccedil;&otilde;es, telecomunica&ccedil;&otilde;es, g&aacute;s, saneamento b&aacute;sico e distribui&ccedil;&atilde;o de &aacute;gua, pelo Imposto incidente sobre os servi&ccedil;os a elas prestados no territ&oacute;rio do Munic&iacute;pio;</p><p>VII - as sociedades que explorem servi&ccedil;os de planos de sa&uacute;de ou de assist&ecirc;ncia m&eacute;dica, hospitalar e cong&ecirc;neres ou de seguros, atrav&eacute;s de plano de medicina de grupo e conv&ecirc;nios, pelo Imposto incidente sobre os servi&ccedil;os dos quais resultem remunera&ccedil;&otilde;es ou comiss&otilde;es, por elas pagas a seus agentes, corretores ou intermedi&aacute;rios estabelecidos no Munic&iacute;pio, pelos agenciamentos, corretagens ou intermedia&ccedil;&otilde;es de planos, seguros ou conv&ecirc;nios;</p><p>VIII - as empresas administradoras de aeroportos e de terminais rodovi&aacute;rios, pelo Imposto incidente sobre os servi&ccedil;os a elas prestados no territ&oacute;rio do Munic&iacute;pio.</p><p>IX - os hospitais e prontos-socorros, pelo Imposto incidente sobre os servi&ccedil;os a eles prestados no territ&oacute;rio do Munic&iacute;pio.</p><p>X - a Empresa Brasileira de Correios e Tel&eacute;grafos, pelo Imposto incidente sobre os servi&ccedil;os prestados por suas ag&ecirc;ncias franqueadas estabelecidas no Munic&iacute;pio e dos quais resultem remunera&ccedil;&otilde;es ou comiss&otilde;es por ela pagas.</p><p>XI. os agentes e promotores de eventos de divers&atilde;o, lazer, entretenimento e cong&ecirc;neres;(Lei Complementar n&deg; 1.306/2005).</p><p>XII. os tomadores de servi&ccedil;os, em rela&ccedil;&atilde;o aos servi&ccedil;os que lhes sejam prestados sem emiss&atilde;o de nota fiscal; (Lei Complementar n&deg; 1.306/2005).</p><p>XIII. os tomadores de servi&ccedil;os, em rela&ccedil;&atilde;o aos servi&ccedil;os prestados no territ&oacute;rio do Munic&iacute;pio de Vit&oacute;ria da Conquista, por prestadores de servi&ccedil;os de outros munic&iacute;pios. (Lei Complementar n&deg; 1.306/2005).</p><p>&sect; 1&deg; - Os respons&aacute;veis a que se refere este artigo est&atilde;o obrigados ao recolhimento integral do imposto devido, multa e acr&eacute;scimos legais, independentemente de ter sido efetuada sua reten&ccedil;&atilde;o na fonte.</p><div id='pergunta'><b>QUAIS OS DEVERES DOS CONTRIBUINTES SUBSTITUTOS?</b></div><p>&bull;	Inscrever-se no Cadastro da Secretaria de Finan&ccedil;as do Munic&iacute;pio de Vit&oacute;ria da Conquista; </p><p>&bull;	Reter o Imposto sobre Servi&ccedil;os do prestador de servi&ccedil;os;</p><p>&bull;	Emitir recibo de reten&ccedil;&atilde;o na fonte do Imposto sobre Servi&ccedil;os;</p><p>&bull;	Recolher o Imposto sobre Servi&ccedil;os, independente de ter sido efetuada a reten&ccedil;&atilde;o na fonte, at&eacute; o dia 10 do m&ecirc;s subseq&uuml;ente ao da presta&ccedil;&atilde;o do servi&ccedil;o ou da reten&ccedil;&atilde;o do imposto, conforme o caso;</p><p>&bull;	Entregar mensalmente a Declara&ccedil;&atilde;o de Servi&ccedil;os &agrave; Secretaria de Finan&ccedil;as do Munic&iacute;pio de Vit&oacute;ria da Conquista, atrav&eacute;s da &ldquo;Internet&ldquo;, at&eacute; o dia 10 (dez) do m&ecirc;s subseq&uuml;ente ao do servi&ccedil;o tomado;</p><p>&bull;	Manter controle em separado das reten&ccedil;&otilde;es efetuadas, para apresentar ao Fisco, quando solicitado.</p><div id='pergunta'><b>QUAIS OS CASOS DE DISPENSA DE RETEN&Ccedil;&Atilde;O NA FONTE POR PARTE DO CONTRIBUINTE SUBSTITUTO ?</b> (art. 10, &sect;3 ao 6&deg; do Regulamento do ISSQN)</div><p>&bull;	Profissionais aut&ocirc;nomos inscritos em qualquer munic&iacute;pio e em dia com o pagamento do imposto;</p><p>&bull;	Contribuintes enquadrados no regime de recolhimento do imposto por estimativa;</p><p>&bull;	Prestadores de servi&ccedil;os imunes ou isentos;</p><p>&bull;	Sociedade de profissionais submetidas a regime de pagamento do imposto por al&iacute;quota fixa mensal;</p><p>&bull;	Prestadores de servi&ccedil;os que possuam medida liminar ou tutelar antecipada, dispensando-os do pagamento do imposto ou autorizando o dep&oacute;sito judicial;</p><p>No primeiro caso, a dispensa de reten&ccedil;&atilde;o na fonte &eacute; condicionada &agrave; apresenta&ccedil;&atilde;o do Alvar&aacute; de Profissional Aut&ocirc;nomo emitido pelo munic&iacute;pio de Vit&oacute;ria da Conquista. Se o profissional for inscrito em outro munic&iacute;pio, exige-se o documento comprobat&oacute;rio de sua inscri&ccedil;&atilde;o municipal no outro munic&iacute;pio e prova de que est&aacute; em dia com o pagamento do imposto.</p><p>Nos demais casos, devem ser apresentados Certid&atilde;o de N&atilde;o Reten&ccedil;&atilde;o do ISSQN na Fonte emitida pelo fisco municipal da Prefeitura Municipal de Vit&oacute;ria da Conquista.</p><div id='pergunta'><b>EXISTE PENALIDADE PELO DESCUMPRIMENTO DAS OBRIGA&Ccedil;&Otilde;ES POR PARTE DOS CONTRIBUINTES SUBSTITUTOS?</b></div><p>Existe previs&atilde;o legal de multa punitiva, caso o substituto tribut&aacute;rio deixe de cumprir quaisquer das obriga&ccedil;&otilde;es tribut&aacute;rias previstas na Legisla&ccedil;&atilde;o Tribut&aacute;ria do Munic&iacute;pio de Vit&oacute;ria da Conquista, como, por exemplo: n&atilde;o realizar a sua inscri&ccedil;&atilde;o no Cadastro Fiscal; n&atilde;o entregar a Declara&ccedil;&atilde;o de Servi&ccedil;os; deixar de emitir comprovante de reten&ccedil;&atilde;o do ISSQN na fonte; n&atilde;o reter na fonte o ISSQN; n&atilde;o recolher o ISSQN retido. Veja a seguir conforme estabelece o CTM:</p><p>Art. 226 - S&atilde;o infra&ccedil;&otilde;es aquelas situa&ccedil;&otilde;es que firam a legisla&ccedil;&atilde;o sobre a presta&ccedil;&atilde;o de servi&ccedil;o de qualquer natureza, principalmente as especificadas neste artigo, com incid&ecirc;ncia das respectivas penalidades:</p><p>I - no valor de 10% (dez por cento) do total do tributo atualizado, para cada nota fiscal, ordem de servi&ccedil;o ou nota fiscal-fatura emitida sem identificar o tomador do servi&ccedil;o;</p><p>II - no valor de 50% (cinq&uuml;enta por cento) do tributo atualizado aos   contribuintes ou respons&aacute;veis que:</p><p>a)	deixarem de efetuar a reten&ccedil;&atilde;o na fonte, quando obrigat&oacute;ria;</p><p>b)	deixarem de recolher espontaneamente o imposto devido no prazo legal.</p><p>III - no valor de 100% (cem por cento) do tributo atualizado:</p><p>a)	pela falta de recolhimento &agrave; Fazenda Municipal do tributo retido na fonte;</p><p>b)	pela sonega&ccedil;&atilde;o verificada em face de documento, exame de inscrita mercantil e/ou fiscal ou elementos de qualquer natureza que resultarem de artif&iacute;cio doloso ou aparentarem intuito de fraude, e, a multa nunca ser&aacute; inferior a R$ 320,00 (trezentos e vinte reais).</p><p>IV - no valor de R$ 30,00 (trinta reais):</p><p>a)	pelo exerc&iacute;cio de atividade por contribuinte de reduzido movimento econ&ocirc;mico ou profissional aut&ocirc;nomo sem inscri&ccedil;&atilde;o no Cadastro Fiscal;</p><p>b)	ao contribuinte que encerrar as atividades e n&atilde;o solicitar a baixa no cadastro de atividades econ&ocirc;micas.</p><p>c)	Pela mudan&ccedil;a do endere&ccedil;o do estabelecimento, sem comunica&ccedil;&atilde;o ao Fisco.</p><p>V - no valor de R$ 32,00 (trinta e dois reais) para cada via de documento fiscal constante utilizada sem autoriza&ccedil;&atilde;o ou autentica&ccedil;&atilde;o da autoridade administrativa competente;</p><p>VI - no valor de R$ 70,00 (setenta reais) quando o contribuinte deixar de apresentar a Declara&ccedil;&atilde;o Mensal de Reten&ccedil;&atilde;o na Fonte e Declara&ccedil;&atilde;o Mensal de Servi&ccedil;os - DMS;</p><p>VII - no valor de R $ 140,00 (cento e quarenta reais), pela falta de escritura&ccedil;&atilde;o do Livro de Registro de ISSQN.</p><p>VIII - no valor de R$ 150,00 (cento e cinquenta) reais, pelo funcionamento de empresa de presta&ccedil;&atilde;o de servi&ccedil;o sem inscri&ccedil;&atilde;o no Cadastro Fiscal;</p><p>IX - no valor de R$ 200,00 (duzentos reais):</p><p>a)	pelo embara&ccedil;o &agrave; a&ccedil;&atilde;o fiscal;</p><p>b)	pelo n&atilde;o atendimento &agrave; intima&ccedil;&atilde;o do Fisco Municipal;</p><p>c)	pelo atraso na escritura&ccedil;&atilde;o dos livros fiscais;</p><p>d)	por n&atilde;o haver solicitado autoriza&ccedil;&atilde;o pr&eacute;via da reparti&ccedil;&atilde;o competente para confec&ccedil;&atilde;o e utiliza&ccedil;&atilde;o de documentos fiscais.</p><p>e)	aos que, n&atilde;o obrigados ao pagamento do imposto, deixarem de emitir nota fiscal ou fatura de servi&ccedil;o correspondente a opera&ccedil;&otilde;es isentas ou n&atilde;o tributadas, ou outros documentos de controle exigidos pela legisla&ccedil;&atilde;o municipal;</p><p>f)	aos que adotarem regime especial de documentos fiscais sem pr&eacute;via autoriza&ccedil;&atilde;o da reparti&ccedil;&atilde;o competente;</p><p>g)	aos que, indevidamente, emitirem documentos fiscais, em proveito pr&oacute;prio ou alheio;</p><p>X - No valor de R$ 300,00 (trezentos reais) pela transfer&ecirc;ncia fraudulenta da sede da empresa ou profissionais aut&ocirc;nomos para outros munic&iacute;pios com o intuito de burlar o Fisco Municipal no pagamento do ISS;</p><p>XI - No valor de R$ 640,00 (seiscentos e quarenta reais) por tal&atilde;o, para a gr&aacute;fica que imprimir documento fiscal sem pr&eacute;via autoriza&ccedil;&atilde;o da reparti&ccedil;&atilde;o competente;</p><p>&sect; 1&deg; - A multa de infra&ccedil;&atilde;o prevista no inciso II, letra &ldquo;b&rdquo; deste artigo, ser&aacute; dispensada quando o sujeito passivo efetuar o recolhimento espont&acirc;neo do tributo.</p><p>&sect; 2&deg; - Os contribuintes que procurarem o setor competente, antes de qualquer procedimento fiscal, para sanar irregularidades ao cumprimento das obriga&ccedil;&otilde;es acess&oacute;rias, ficar&atilde;o a salvo de penalidades. </p><p>&sect; 3&deg; - Havendo concurso de infra&ccedil;&otilde;es, as penalidades ser&atilde;o aplicadas, conjuntamente, uma para cada infra&ccedil;&atilde;o, ainda que capituladas no mesmo dispositivo legal. </p><div id='pergunta'><b>QUAL A VANTAGEM DA SUBSTITUI&Ccedil;&Atilde;O TRIBUT&Aacute;RIA?</b></div><p>Para o Fisco Municipal, permite o maior controle da arrecada&ccedil;&atilde;o e redu&ccedil;&atilde;o da sonega&ccedil;&atilde;o, haja vista, que &eacute; mais f&aacute;cil fiscalizar alguns tomadores de servi&ccedil;os, que uma vastid&atilde;o de contribuintes.</p><p>Para o Contribuinte Substituto, significa melhoria do seu fluxo de caixa e a certeza do pagamento do imposto. S&oacute; a t&iacute;tulo de exemplo, se um tomador de servi&ccedil;o pagar um servi&ccedil;o no dia primeiro de um dado m&ecirc;s, ele ficar&aacute; com o valor do imposto no seu caixa por um per&iacute;odo de 40 dias.</p><div id='pergunta'><b>QUAIS AS ATRIBUI&Ccedil;&Otilde;ES DOS CONTRIBUINTES SUBSTITUTOS?</b></div><p>&bull;	Requerer sua inscri&ccedil;&atilde;o no cadastro do Munic&iacute;pio, como respons&aacute;vel, se n&atilde;o for contribuinte do ISS.</p><p>&bull;	Efetuar o desconto do valor correspondente ao imposto, no momento do pagamento do servi&ccedil;o.</p><p>&bull;	Recolher o imposto retido no prazo estabelecido na legisla&ccedil;&atilde;o.</p><p>&bull;	Entregar a Declara&ccedil;&atilde;o de servi&ccedil;os no prazo legal.</p><p>&bull;	Emitir o Documento de Reten&ccedil;&atilde;o de ISSQN retido na fonte Fonte.</p><p>&bull;	Manter controle das reten&ccedil;&otilde;es feitas, para apresentar ao fisco, quando solicitado.</p><br/><center><p><a href='orientacoes.jsp?id=11'><img src='imagens/btAnterior.jpg' border='0'/></a><a href='duvidasFrequentes.jsp'><img src='imagens/btIndice.jpg' border='0'/></a><a href='orientacoes.jsp?id=13'><img src='imagens/btProximo.jpg' border='0'  /></a></p></span>";
		titulo.innerHTML = "Substitui&ccedil;&atilde;o Tribut&aacute;ria&nbsp;";
	}		
	
	else if (id_opcao == 13){
		contexto.innerHTML = "<span id='contextoDF' style='font-size:11px; color:#000'><br /><div id='pergunta'><b>O QUE &Eacute; TAXA E PORQUE &Eacute; COBRADA PELA PREFEITURA?</b></div><p>Taxa &eacute; a remunera&ccedil;&atilde;o devida &agrave; Administra&ccedil;&atilde;o P&uacute;blica Municipal pela utiliza&ccedil;&atilde;o, efetiva ou potencial, de servi&ccedil;o p&uacute;blico prestado ao contribuinte ou posto &agrave; sua disposi&ccedil;&atilde;o, bem como por melhorias realizadas dentro do munic&iacute;pio.</p><div id='pergunta'><b>QUAL O FATO GERADOR DA TAXA?</b></div><p>As taxas s&atilde;o geradas pelo exerc&iacute;cio regular do poder de pol&iacute;cia, ou pela utiliza&ccedil;&atilde;o, efetiva ou potencial, de servi&ccedil;o p&uacute;blico prestado ou colocado &agrave; disposi&ccedil;&atilde;o do contribuinte.</p><div id='pergunta'><b>O QUE E O EXERC&Iacute;CIO DO PODER DE POLICIA?</b></div><p>&Eacute; a atividade que, limitando ou disciplinando direito, interesse ou liberdade, regula a pr&aacute;tica de ato ou absten&ccedil;&atilde;o de fato, em raz&atilde;o de interesse p&uacute;blico referente &agrave; seguran&ccedil;a, higiene, ordem e costumes, disciplina da produ&ccedil;&atilde;o e do mercado, ao exerc&iacute;cio de atividades econ&ocirc;micas dependentes de concess&atilde;o ou autoriza&ccedil;&atilde;o do Poder P&uacute;blico, &agrave; tranq&uuml;ilidade p&uacute;blica ou ao respeito &agrave; propriedade e aos direitos individuais ou coletivos.</p><div id='pergunta'><b>QUAIS AS ESPECIES DE TAXAS COBRADAS PELA PREFEITURA DE VIT&Oacute;RIA DA CONQUISTA?</b></div><p>&bull;	Taxas De Licen&ccedil;a E De Localiza&ccedil;&atilde;o Para Funcionamento Dos Estabelecimentos Em Geral;</p><p>&bull;	Taxa De Licen&ccedil;a Para Explora&ccedil;&atilde;o De Atividades Em Logradouros P&uacute;blicos;</p><p>&bull;	Taxa De Licen&ccedil;a Para Explora&ccedil;&atilde;o Dos Meios De Publicidade;</p><p>&bull;	Taxa Licen&ccedil;a Para Explora&ccedil;&atilde;o Do Com&eacute;rcio Eventual Ou Ambulante;</p><p>&bull;	Taxa De Licen&ccedil;a E/Ou Alvar&aacute; Para Execu&ccedil;&atilde;o De Obras De &Aacute;reas Particulares;</p><p>&bull;	Taxa De Licen&ccedil;a Para Execu&ccedil;&atilde;o De Loteamentos, Arruamentos, Desmembramentos Ou Remembramentos;</p><p>&bull;	Taxa De Vigil&acirc;ncia Sanit&aacute;ria;</p><p>&bull;	Taxa De Licenciamento Ambiental;</p><p>&bull;	Taxas Pela Utiliza&ccedil;&atilde;o De Servi&ccedil;os P&uacute;blicos;</p><p>&bull;	Taxa De Expediente E Emolumentos;</p><p>&bull;	Taxa De Servi&ccedil;os Diversos;</p><p>&bull;	Taxa De Coleta Especial De Lixo S&eacute;ptico.</p><div id='pergunta'><b>QUAIS OS LOCAIS DA PREFEITURA ONDE O CONTRIBUINTE SABER&Aacute; DOS VALORES DAS TAXAS?</b></div><p>Os valores das taxas poder&atilde;o ser obtidos na Secretaria de Finan&ccedil;as junto ao plant&atilde;o fiscal.</p><br/><center><p><a href='orientacoes.jsp?id=12'><img src='imagens/btAnterior.jpg' border='0'/></a><a href='duvidasFrequentes.jsp'><img src='imagens/btIndice.jpg' border='0'/></a></p></span>";		
		titulo.innerHTML = "Taxas&nbsp;";
	}	
	
	else if (id_opcao == 14){
		contexto.innerHTML = "<p>Constitui d&iacute;vida ativa do Munic&iacute;pio a proveniente de tributos, multas de qualquer natureza, juros, atualiza&ccedil;&otilde;es monet&aacute;rias, foros, laud&ecirc;mios, alugu&eacute;is, alcances dos respons&aacute;veis, reposi&ccedil;&otilde;es oriundas de contratos administrativos, consistentes em quantias fixas e determinadas, depois de decorridos os prazos para pagamento, ou de decididos os processos fiscais administrativos ou judiciais.</p><p>A d&iacute;vida ativa &eacute; inscrita ap&oacute;s o vencimento do prazo do pagamento do cr&eacute;dito tribut&aacute;rio, extra&iacute;das as respectivas certid&otilde;es de d&eacute;bito, estas s&atilde;o relacionadas e remetidas ao &oacute;rg&atilde;o jur&iacute;dico para cobran&ccedil;a.</p><p><div id=topico><b>Da Cobran&ccedil;a</b></div></p><p>Compete &agrave; Procuradoria Geral do Munic&iacute;pio executar, superintender e fiscalizar a cobran&ccedil;a da d&iacute;vida ativa do Munic&iacute;pio.</p><p>A cobran&ccedil;a da d&iacute;vida ativa &eacute; feita, por via amig&aacute;vel ou judicial, atrav&eacute;s de a&ccedil;&atilde;o executiva fiscal.</p><p>A cobran&ccedil;a amig&aacute;vel &eacute; feita no prazo de 30 (trinta) dias a contar do recebimento das certid&otilde;es, podendo ser concedida prorroga&ccedil;&atilde;o de igual prazo, pela autoridade que dirige o &oacute;rg&atilde;o jur&iacute;dico.</p><p>A contar da data do recebimento da intima&ccedil;&atilde;o da cobran&ccedil;a amig&aacute;vel o contribuinte tem 15 (quinze) dias para quita&ccedil;&atilde;o do d&eacute;bito.</p><p>Decorrido o prazo de cobran&ccedil;a amig&aacute;vel, sem a quita&ccedil;&atilde;o do d&eacute;bito, &eacute; procedida a cobran&ccedil;a judicial, na forma da legisla&ccedil;&atilde;o federal em vigor.</p>";
		titulo.innerHTML = "D&iacute;vida Ativa Fiscal e Judicial&nbsp;";
	}
	else if (id_opcao == 15){
		contexto.innerHTML = "<p>Taxa para licen&ccedil;a p&uacute;blica municipal referente a publicidade nas formas de cartazes, out-door, letreiros, quadros, pain&eacute;is, faixas, an&uacute;ncios, mostru&aacute;rios e quaisquer outros instrumentos que tenham como finalidade a produ&ccedil;&atilde;o de mensagens de natureza comercial.</p><p>A licen&ccedil;a pr&eacute;via somente &eacute; concedida ap&oacute;s autoriza&ccedil;&atilde;o da Ger&ecirc;ncia de Posturas.</p><p>Quando o local em que se pretenda colocar an&uacute;ncio n&atilde;o for de propriedade do requerente, dever&aacute; este juntar ao requerimento autoriza&ccedil;&atilde;o do propriet&aacute;rio.</p><p>A taxa de licen&ccedil;a para publicidade em eventos diversos &eacute; cobrada de acordo com o per&iacute;odo fixado para a propaganda e de conformidade com a Tabela IV anexa a Lei 1.259-2004 e Lei Complementar n&deg; 1.306/2005.</p><p>A Taxa de Licen&ccedil;a de Publicidade n&atilde;o &eacute; cobrada:</p>&bull;	Quando em tabuletas indicativas se refiram a s&iacute;tios, granjas ou fazendas, rumo a dire&ccedil;&atilde;o de logradouros p&uacute;blicos, d&iacute;sticos ou denomina&ccedil;&atilde;o de estabelecimentos comerciais ou de presta&ccedil;&atilde;o de servi&ccedil;os, bem como os que sejam destinados a indica&ccedil;&atilde;o de endere&ccedil;os, telefones e atividades, desde que afixados no estabelecimento respectivo;<br/>&bull;	Placas, d&iacute;sticos de hospitais, entidades filantr&oacute;picas, beneficentes, culturais ou esportivas somente afixadas nos pr&eacute;dios em que funcionem;<br/>&bull;	Cartazes ou letreiros indicativos de tr&acirc;nsito, logradouros, tur&iacute;sticos, itiner&aacute;rios de viagem de transporte coletivo.<br/><p><a href='Anexos/Taxas_TLP.pdf' target='_blank'><b style='color:#364C87'>Veja as taxas de licen&ccedil;a para explora&ccedil;&atilde;o dos meios de publicidade.</b></a></p>";
		titulo.innerHTML = "TLP - Taxa de de licen&ccedil;a para explora&ccedil;&atilde;o dos meios de publicidade&nbsp;";
	}	
	else if (id_opcao == 16){
		contexto.innerHTML = "<p>A taxa de Licen&ccedil;a de Localiza&ccedil;&atilde;o e a taxa de Funcionamento de estabelecimentos comerciais, industriais, de presta&ccedil;&atilde;o de servi&ccedil;os, de cr&eacute;dito, seguro, capitaliza&ccedil;&atilde;o e empresas de qualquer natureza, fundadas no poder de pol&iacute;cia do Munic&iacute;pio, quanto ao saneamento da Cidade e ao ordenamento das atividades urbanas, t&ecirc;m como fato gerador o licenciamento obrigat&oacute;rio e a fiscaliza&ccedil;&atilde;o, respectivamente, quanto &agrave;s normas administrativas, constantes no C&oacute;digo de Posturas do Munic&iacute;pio, relativas &agrave; polui&ccedil;&atilde;o do meio ambiente, costumes, ordem, tranq&uuml;ilidade e seguran&ccedil;a p&uacute;blica.</p><p>Est&atilde;o inclu&iacute;dos tamb&eacute;m na incid&ecirc;ncia das taxas, o exerc&iacute;cio de atividades decorrentes de profiss&atilde;o, arte oficio ou fun&ccedil;&atilde;o.</p><p>A taxa &eacute; representada pela soma de duas parcelas:</p>&bull;	Uma parcela no ato da solicita&ccedil;&atilde;o da licen&ccedil;a, correspondente &agrave;s dilig&ecirc;ncia para verificar as condi&ccedil;&otilde;es de localiza&ccedil;&atilde;o do estabelecimento, quanto aos usos existentes no entorno e sua compatibilidade com o Plano Diretor do Munic&iacute;pio.<br/>&bull;	Outra parcela correspondente a taxa pelo funcionamento, a qual poder&aacute; perdurar por fra&ccedil;&atilde;o de meses ou um ano e ter&aacute; como finalidade a fiscaliza&ccedil;&atilde;o das normas constantes do C&oacute;digo de Pol&iacute;cia Administrativa do Munic&iacute;pio.<p>Anualmente, o contribuinte pagar&aacute; a renova&ccedil;&atilde;o da taxa de licen&ccedil;a para sua atividade a qual ser&aacute; constitu&iacute;da, unicamente, de uma parcela cuja base de c&aacute;lculo ser&aacute; efetuada de acordo com a Tabela de Receita n&deg; II anexa a Lei 1.259-2004.</p><p><a href='Anexos/TABELA II DA LEI 1259 DE 2004.pdf' target='_blank'><b style='color:#364C87'>Clique aqui</b></a> para ver a tabela com as taxas de funciomento.</p>";
		titulo.innerHTML = "TLF - Taxas de licen&ccedil;a e de localiza&ccedil;&atilde;o para funcionamento&nbsp;";
	}
	else if (id_opcao == 17){
		contexto.innerHTML = "<p>A taxa de licen&ccedil;a e execu&ccedil;&atilde;o de obras e urbaniza&ccedil;&atilde;o de &aacute;reas particulares, fundada no poder de pol&iacute;cia do Munic&iacute;pio, quanto ao estabelecimento de normas e edifica&ccedil;&atilde;o, de abertura e liga&ccedil;&atilde;o de novos logradouros ao sistema vi&aacute;rio urbano, tem como fato gerador o licenciamento obrigat&oacute;rio bem como a sua fiscaliza&ccedil;&atilde;o quanto &agrave;s normas administrativas do Servi&ccedil;o de Administra&ccedil;&atilde;o Tribut&aacute;ria, C&oacute;digo de Posturas e Obras, relativas &agrave; prote&ccedil;&atilde;o est&eacute;tica e ao aspecto paisag&iacute;stico e hist&oacute;rico da Cidade, bem assim, a higiene e a seguran&ccedil;a p&uacute;blica.</p><p>Os pedidos de licen&ccedil;a e de alvar&aacute; ser&atilde;o feitos atrav&eacute;s de peti&ccedil;&atilde;o, assinada pelo propriet&aacute;rio do im&oacute;vel ou interessado direto na execu&ccedil;&atilde;o da obra, dirigida &agrave; Secretaria Municipal de Transporte, Tr&acirc;nsito e Infra-estrutura Urbana, ficando o in&iacute;cio da obra ou a urbaniza&ccedil;&atilde;o a depender da prova de leg&iacute;timo interesse, pagamento da taxa e expedi&ccedil;&atilde;o do alvar&aacute; de licen&ccedil;a.</p><p>Antes da solicita&ccedil;&atilde;o do alvar&aacute; para realiza&ccedil;&atilde;o de obra ou abertura de liga&ccedil;&atilde;o de novos logradouros ao sistema vi&aacute;rio, dever&aacute; o interessado consultar, mediante peti&ccedil;&atilde;o dirigida &agrave; Secretaria Municipal de Transporte, Tr&acirc;nsito e Infra-estrutura Urbana, sobre a viabilidade do que se constitui o seu objetivo, pagando para isto taxa prevista na Tabela de Receita <b>n&deg; VI anexa </b>&agrave; Lei 1.259-2004.</p><p>Para o recebimento do alvar&aacute;, o interessado dever&aacute; fazer juntada do comprovante de quita&ccedil;&atilde;o da taxa, bem como da Certid&atilde;o Negativa de D&eacute;bito para com a Fazenda P&uacute;blica Municipal.</p><p>O alvar&aacute; de licen&ccedil;a caducar&aacute; em 12 meses, a contar da data da expedi&ccedil;&atilde;o.</p><p>Para o prosseguimento da obra, ser&aacute; necess&aacute;ria a renova&ccedil;&atilde;o do alvar&aacute;, dependendo esta da comprova&ccedil;&atilde;o do pagamento do d&eacute;bito referente ao alvar&aacute; anterior, atingido pela caducidade.</p><p>N&atilde;o se conceder&aacute; habite-se ou certificado de conclus&otilde;o da obra, antes do seu t&eacute;rmino, quando a constru&ccedil;&atilde;o tenha mais de tr&ecirc;s unidades imobili&aacute;rias.</p><p>Poder&aacute; ser concedida &ldquo;habite-se&rdquo; parcial, nas constru&ccedil;&otilde;es cujas unidades imobili&aacute;rias sejam independentes e obede&ccedil;am &agrave; l&oacute;gica de sua constru&ccedil;&atilde;o.</p><p><div id='topico'><b>Do Pagamento</b></div></p><p>O pagamento da taxa ser&aacute; efetuado em parcela &uacute;nica, podendo, a crit&eacute;rio da Administra&ccedil;&atilde;o, ser parcelado em at&eacute; 06 (seis) vezes.</p><p>Para efeito do pagamento da taxa, os c&aacute;lculos da &aacute;rea de constru&ccedil;&atilde;o, reforma e servi&ccedil;os outros que possam gerar a obrigatoriedade do seu pagamento, obedecer&atilde;o a Tabela de <b>Receita n&deg; VI</b>, anexa a Lei 1.259-2004.</p><p><a href='Anexos/Tabela_Alvara.pdf' target='_blank'><b style='color:#364C87'>Veja a tabela de licen&ccedil;a ou alvar&aacute; para execu&ccedil;&atilde;o de obras.</b></a></p>";
		titulo.innerHTML = "Alvar&aacute; para execu&ccedil;&atilde;o de obras de &aacute;reas particulares&nbsp;";
	}
}



