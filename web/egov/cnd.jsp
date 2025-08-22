<%@ page import="java.util.*"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="sol.util.RequestUtilities"%>
<%@ page import="sol.util.Result"%>
<%@ page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@ page import="net.sf.jasperreports.engine.JasperFillManager"%>
<%@ page import="net.sf.jasperreports.engine.JasperExportManager"%>
<%@ page import="net.sf.jasperreports.engine.JRResultSetDataSource"%>
<%
	String path        = session.getServletContext().getRealPath("/egov/");
	String nrInscricao = RequestUtilities.getAsString(request, "nrInscricao", "");
	String nrCpfCnpj   = RequestUtilities.getAsString(request, "nrCpfCnpj", "");
	int tpFinalidade   = RequestUtilities.getAsInteger(request, "tpFinalidade", 0);
	int lgDebug        = RequestUtilities.getAsInteger(request, "lgDebug", 0);
	String output      = "";
	try {
		Result result = com.tivic.manager.egov.CndServices.gerarCND(nrCpfCnpj, tpFinalidade, nrInscricao);
		if(result.getCode()<0)	{
			output = "Não foi possível emitir a Certidão Negativa para o CPF/CNPJ informado. Dirija-se ao endereço informado abaixo para maiores esclarecimentos: <br/>"+
                     "Praça Joaquim Correia, n° 55, Centro - Vitória da Conquista/BA.";
			if(result.getCode() > -100 || lgDebug==1)
				output += "<br/>"+result.getMessage()+"["+result.getCode()+"]";
			else
				output += "<br/>[ "+result.getCode()+( result.getCode()==-140 ? " - "+result.getMessage() : "")+" ]";
			response.reset();
			response.setContentType("text/html");
			//
			System.out.println("FALHA CPF/CNPJ: "+nrCpfCnpj+", "+result.getMessage());	
		}
		else	{
			// Definido
			response.reset();
			response.setContentType("application/pdf");
			//
			ResultSet rs                      = (ResultSet)result.getObjects().get("resultset");
			HashMap<String,Object> parametros = (HashMap)result.getObjects().get("parametros");
			System.out.println("rs = "+rs);
			System.out.println("parametros = "+parametros);
			parametros.put("logoPath", path);
			
			JasperPrint jp                    = JasperFillManager.fillReport(path+"/cnd.jasper", parametros, new JRResultSetDataSource(rs));
			
			java.io.OutputStream outputStream = response.getOutputStream();
			JasperExportManager.exportReportToPdfStream(jp, outputStream);
		}		
	}
	catch (Exception e){
		e.printStackTrace(System.out);
		output = "Falha ao gerar CND";
		response.reset();
		response.setContentType("text/html");
	}
%>
<html>
<head>
<title>Certidão Negativa de Débito</title>
</head>

<body bgcolor="#8C92C6">

<center>
	<font color="white"> <h2> <%=output%></h2></font>
</center>

</body>
</html>