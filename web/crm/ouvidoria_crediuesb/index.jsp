<%@page import="com.tivic.manager.grl.ParametroServices" %>
<%@page import="com.tivic.manager.crm.CentralAtendimento" %>
<%@page import="com.tivic.manager.crm.CentralAtendimentoDAO" %>
<%@taglib uri="../../tlds/loader.tld" prefix="loader" %>
<loader:library libraries="form, toolbar, calendario, aba2.0, filter, grid2.0, shortcut" compress="false" />
<%
	int cdCentral = ParametroServices.getValorOfParametroAsInteger("CD_CENTRAL_ATENDIMENTO_PADRAO", 0); //configurar para a central
	CentralAtendimento central = (cdCentral==0)?null:CentralAtendimentoDAO.get(cdCentral);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>CREDIUESB :: Ouvidoria Online</title>
<script language="javascript">
	function miAtendimentoOnClick() {
		createWindow('jAtendimento', {caption: 'Atendimento', width: 700, height: 335, top: 10, left: 10,  
									  contentUrl: '../atendimento_externo.jsp?cdCentral=<%=cdCentral%>'});
	}
	
	function miAcompanhamentoOnClick() {
		createWindow('jAcompanhamento', {caption: 'Acompanhamento', width: 700, height: 335, top: 10, left: 10,  
									  contentUrl: '../acompanhamento_externo.jsp?cdCentral=<%=cdCentral%>'});
	}
	
	function miLoginOnClick(msg) {
		createWindow('jLogin', {caption: 'Login', 
							noMinimizeButton: true,
							noMaximizeButton: true,
							width: 350, 
							height: 180, 
							contentUrl: '../../login.jsp?callback=parent.onLogin&lgEscolherEmpresa=1',
							modal:true});
	}
	
	function onLogin(user){
		window.resizeTo(830, 560);
		document.location = 'crm/index.jsp?cdEmpresa='+user.cdEmpresa+'&cdUsuario='+user.cdUsuario;
	}

</script>
</head>

<body style="margin:0px;">
    <div style="width:701px; height:335px; background-color:#99CC66; padding:10px;">
    	<div style="height:335px; background-color:#F6F6F6; background-image:url(imagens/fundo_portal.jpg); background-position:bottom left; background-repeat:no-repeat; position:relative">
        	<img src="imagens/logo.gif" style="position:absolute; top:10px; left:10px;"/>
            <div style="position:absolute; top:6px; right:10px; height:30px; margin:10px; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:36px; color:#669933; white-space:nowrap; text-transform:capitalize;"><%=(central!=null)?central.getNmCentral().toLowerCase():"Nenhuma central configurada"%></div>
            <%
				if(central!=null){
				%>
				<div style="position:absolute; bottom:30px; right:30px;">
					<div onclick="miAtendimentoOnClick()" style="background-color:#FFFFFF; border:1px solid #CCCCCC; width:80px; height:85px; float:left; margin:5px; text-align:center; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; cursor:pointer;">
						<img src="imagens/message.png" style="margin-top:2px;" />
						<br />Abra seu chamado
					</div>
					<div onclick="miAcompanhamentoOnClick()" style="background-color:#FFFFFF; border:1px solid #CCCCCC; width:80px; height:85px; float:left; margin:5px; text-align:center; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; cursor:pointer;">
						<img src="imagens/find.png" style="margin-top:2px;" />
						Acompanhar chamado
					</div>
					<div onclick="miLoginOnClick()" style="background-color:#FFFFFF; border:1px solid #CCCCCC; width:80px; height:85px; float:left; margin:5px; text-align:center; font-family:Geneva, Arial, Helvetica, sans-serif; font-size:12px; cursor:pointer;">
						<img src="imagens/manager.png" style="margin-top:2px;" />
						Opções <br />do Atendente
					</div>
				</div>
				<%
				}
			%>
        </div>
    </div>
</body>
</html>
