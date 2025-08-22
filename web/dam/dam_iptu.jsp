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
	String nrInscricao = RequestUtilities.getAsString(request, "nrInscricao", "");
	String nrCpf 	   = RequestUtilities.getAsString(request, "nrCpf", "");
	int nrAno          = RequestUtilities.getAsInteger(request, "nrAno", new GregorianCalendar().get(Calendar.YEAR));
	int nrParcela      = RequestUtilities.getAsInteger(request, "nrParcela", 0);
	String output = "";
	try {
		Result result = com.tivic.manager.egov.IptuServices.getDadosOfInscricao(nrInscricao, nrCpf, nrAno, nrParcela);
		if(result.getCode()<0)	{
			output = result.getMessage();
			response.reset();
			response.setContentType("text/html");
		}
		else	{
			// Definido
			response.reset();
			response.setContentType("application/pdf");
			//
			JRResultSetDataSource jrRS = new JRResultSetDataSource((ResultSet)result.getObjects().get("resultset"));
			String[] nrCodigoBarras    = com.tivic.manager.egov.IptuServices.getCodigoBarras(nrInscricao, nrAno, nrParcela);
			//
			HashMap<String,Object> parametros = new HashMap<String,Object>();
			try	{
				parametros.put("nrCodigoBarras", nrCodigoBarras[0]);
				parametros.put("nrCodigoBarras1", nrCodigoBarras[1]);
				parametros.put("nrCodigoBarras2", nrCodigoBarras[2]);
				parametros.put("nrCodigoBarras3", nrCodigoBarras[3]);
				parametros.put("nrCodigoBarras4", nrCodigoBarras[4]);
				parametros.put("logoPath", path);
			}
			catch(java.lang.Exception e){
				e.printStackTrace(System.out);
			};
			
			JasperPrint jp = JasperFillManager.fillReport(path+"/iptu.jasper", parametros, jrRS);
			
			java.io.OutputStream outputStream = response.getOutputStream();
			
			JasperExportManager.exportReportToPdfStream(jp, outputStream);
		}		
	}
	catch (Exception cnf){
		cnf.printStackTrace(System.out);
		output = "Erro ao processar DAM/IPTU";
		response.reset();
		response.setContentType("text/html");
	}
%>
<html>
<head>
<title>IPTU <%=nrAno%> - Documento de Arrecadação Municipal</title>
</head>

<body bgcolor="#8C92C6">

<center>
<font color="white"> <h2> <%=output%></h2></font>
</center>

</body>
</html>