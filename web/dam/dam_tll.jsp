<%@ page import="java.util.*"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="sol.util.RequestUtilities"%>
<%@ page import="sol.util.Result"%>
<%@ page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@ page import="net.sf.jasperreports.engine.JasperFillManager"%>
<%@ page import="net.sf.jasperreports.engine.JasperExportManager"%>
<%@ page import="net.sf.jasperreports.engine.JRResultSetDataSource"%>
<%
	String path        = session.getServletContext().getRealPath("/dam/");
	String nrInscricaoMunicipal = RequestUtilities.getAsString(request, "nrInscricaoMunicipal", "");
	String output = "";
	int nrAnoBase = 2014;
	try {
		Result result = com.tivic.manager.egov.TLLServices.getDAM(nrInscricaoMunicipal);
		if(result.getCode()<0)	{
			output = result.getMessage();
			response.reset();
			response.setContentType("text/html");
		}
		else	{
			String dtVencimento = (String)result.getObjects().get("dtVencimento");
			float vlParcela     = (Float)result.getObjects().get("vlTotalGeral");
			nrAnoBase           = (Integer)result.getObjects().get("nrAnoBase");
			String nrDAM        = (String)result.getObjects().get("nrDAM");
			int nrParcela       = (Integer)result.getObjects().get("nrParcela");
			// Definido
			response.reset();
			response.setContentType("application/pdf");
			//
			JRResultSetDataSource jrRS = new JRResultSetDataSource((ResultSet)result.getObjects().get("rs"));
			String[] nrCodigoBarras    = com.tivic.manager.egov.TLLServices.getCodigoBarras(nrDAM, vlParcela, dtVencimento, nrParcela);
			//
			HashMap<String,Object> parametros = result.getObjects();
			try	{
				parametros.put("nrCodigoBarras", nrCodigoBarras[0]);
				parametros.put("nrCodigoBarras1", nrCodigoBarras[1]);
				parametros.put("nrCodigoBarras2", nrCodigoBarras[2]);
				parametros.put("nrCodigoBarras3", nrCodigoBarras[3]);
				parametros.put("nrCodigoBarras4", nrCodigoBarras[4]);
				parametros.put("logoPath", path);
			}
			catch(java.lang.Exception e)	{
				e.printStackTrace(System.out);
			};
		
			JasperPrint jp = JasperFillManager.fillReport(path+"/tll.jasper", parametros, jrRS);
			
			java.io.OutputStream outputStream = response.getOutputStream();
			
			JasperExportManager.exportReportToPdfStream(jp, outputStream);
		}		
	}
	catch (Exception cnf){
		cnf.printStackTrace(System.out);
		output = "Erro ao processar DAM/TLL";
		response.reset();
		response.setContentType("text/html");
	}
%>
<html>
<head>
<title>TLL <%=nrAnoBase%> - Documento de Arrecadação Municipal</title>
</head>

<body bgcolor="#8C92C6">

<center>
<font color="white"> <h2> <%=output%></h2></font>
</center>

</body>
</html>