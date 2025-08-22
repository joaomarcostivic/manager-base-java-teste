<%@page import="com.tivic.manager.adm.NaturezaOperacaoDAO"%>
<%@page import="com.tivic.manager.adm.NaturezaOperacao"%>
<%@page import="com.tivic.manager.print.ReportServlet"%>
<%@page import="com.tivic.sol.report.ReportServices"%>
<%@page import="com.tivic.manager.util.ContextManager"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="sol.util.ConfManager"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="Conexao"%>
<%@page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="javax.xml.parsers.DocumentBuilder"%>
<%@ page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@ page import="org.w3c.dom.Document"%>
<%@ page import="org.xml.sax.InputSource"%>
<%@ page import="sol.util.RequestUtilities"%>
<%@ page import="sol.util.Result"%>
<%@ page import="com.tivic.manager.fsc.*"%>
<%@ page import="com.tivic.manager.grl.*"%>
<%@ page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@ page import="net.sf.jasperreports.engine.JasperFillManager"%>
<%@ page import="net.sf.jasperreports.engine.JasperExportManager"%>
<%@ page import="net.sf.jasperreports.engine.JRResultSetDataSource"%>
<%@ page import="net.sf.jasperreports.engine.data.JRXmlDataSource"%>
<%
// 	String path      = session.getServletContext().getRealPath("/fsc/");
	String output    = "";
	int cdNotaFiscal = RequestUtilities.getAsInteger(request, "cdNotaFiscal", 0);
	int cdEmpresa    = RequestUtilities.getAsInteger(request, "cdEmpresa", 0);
	try {
		// Definido
		response.reset();
		response.setContentType("application/pdf");
		//
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
	    DocumentBuilder db         = dbf.newDocumentBuilder();  
	    // Carregando XML      
	    Connection conexao         = Conexao.conectar();
	    PreparedStatement pstmt    = conexao.prepareStatement("SELECT txt_xml FROM fsc_nota_fiscal_historico "+
	                                                          "WHERE cd_nota_fiscal = " + cdNotaFiscal + 
	                                                          "  AND tp_historico   = " + NotaFiscalHistoricoServices.AUTORIZACAO_NOTA);
	    ResultSetMap rsm           = new ResultSetMap(pstmt.executeQuery());
	    pstmt                      = conexao.prepareStatement("SELECT txt_xml FROM fsc_nota_fiscal_historico "+ 
	    	                                                  "WHERE cd_nota_fiscal = " + cdNotaFiscal + 
	    	                                                  "  AND tp_historico   = " + NotaFiscalHistoricoServices.PROC_DPEC);
	    ResultSetMap rsm2 = new ResultSetMap(pstmt.executeQuery());
	    
	    String xmlNFE = null;
	    
	    if(rsm.next() && rsm.getString("txt_xml") != null && !rsm.getString("txt_xml").equals("")){
	        //String xmlNFE        = (String) com.tivic.manager.fsc.NfeServices.getXmlComProtocolo(cdNotaFiscal, (NotaFiscalHistorico)null, cdEmpresa);
		    xmlNFE = rsm.getString("txt_xml");
		    
	    }
		else if (rsm2.next() && rsm2.getString("txt_xml") != null && !rsm2.getString("txt_xml").equals("")){
	    	//String xmlNFE        = (String) com.tivic.manager.fsc.NfeServices.getXmlComProtocolo(cdNotaFiscal, (NotaFiscalHistorico)null, cdEmpresa);
		    xmlNFE = rsm2.getString("txt_xml");
// 		    InputSource inStream = new InputSource();  
// 	        inStream.setCharacterStream(new StringReader(xmlNFE));  
// 		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
// 	        factory.setNamespaceAware(true);  
// 	        Document doc                  = db.parse(inStream);  
// 		    String recordPath             = "/nfeProc/NFe/infNFe/det";//estrutura do xml  
// 		    JRXmlDataSource xmlDataSource = new JRXmlDataSource(doc, recordPath);
		    
// 		    HashMap<String, Object> params = new HashMap<String, Object>();
// 		    Empresa empresa   = EmpresaDAO.get(cdEmpresa);
// 		    params.put("LOGO", empresa.getImgLogomarca());
// 		    params.put("TPEMISSAO", NotaFiscalServices.EMI_CONTIGENCIA_VIA_DPEC);
		    
// 		    JasperPrint jp = JasperFillManager.fillReport(path+"/danfe.jasper", params, xmlDataSource);
// 		    java.io.OutputStream outputStream = response.getOutputStream();
// 			JasperExportManager.exportReportToPdfStream(jp, outputStream);
	    }
	    
	    else{
	    	xmlNFE        = (String) com.tivic.manager.fsc.NfeServices.getXmlComProtocolo(cdNotaFiscal, (NotaFiscalHistorico)null, cdEmpresa);
// 	    	System.out.println("xmlNFE = " + xmlNFE);
// 		    InputSource inStream = new InputSource();  
// 	        inStream.setCharacterStream(new StringReader(xmlNFE));  
// 		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
// 	        factory.setNamespaceAware(true);  
// 	        Document doc                  = db.parse(inStream);  
// 		    String recordPath             = "/nfeProc/NFe/infNFe/det";//estrutura do xml  
// 		    JRXmlDataSource xmlDataSource = new JRXmlDataSource(doc, recordPath);
		    
// 		    HashMap<String, Object> params = new HashMap<String, Object>();
// 		    Empresa empresa   = EmpresaDAO.get(cdEmpresa);
// 		    params.put("LOGO", empresa.getImgLogomarca());
// 		    params.put("TPEMISSAO", NotaFiscalServices.EMI_NORMAL);
		    
// 		    JasperPrint jp = JasperFillManager.fillReport(path+"/danfe.jasper", params, xmlDataSource);
// 		    java.io.OutputStream outputStream = response.getOutputStream();
// 			JasperExportManager.exportReportToPdfStream(jp, outputStream);
// 	    	output = "DANFE não encontrado!";
// 	    	response.reset();
// 			response.setContentType("text/html");
	    }
	    
	    InputSource inStream = new InputSource();  
        inStream.setCharacterStream(new StringReader(xmlNFE));  
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        factory.setNamespaceAware(true);  
        Document doc                  = db.parse(inStream);  
	    String recordPath             = "/nfeProc/NFe/infNFe/det";//estrutura do xml  
	    JRXmlDataSource xmlDataSource = new JRXmlDataSource(doc, recordPath);
	    
	    HashMap<String, Object> params = new HashMap<String, Object>();
	    Empresa empresa   = EmpresaDAO.get(cdEmpresa);
	    params.put("LOGO", empresa.getImgLogomarca());
	    params.put("TPEMISSAO", NotaFiscalServices.EMI_NORMAL);
	    
	    NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
	    NaturezaOperacao natOp = NaturezaOperacaoDAO.get(nota.getCdNaturezaOperacao());
	    params.put("NMCFOP", natOp.getNmNaturezaOperacao());
	    boolean isCartao = (boolean) com.tivic.manager.fsc.NfeServices.isCartao(cdNotaFiscal);
	    boolean isPostoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa) != 0;
	    if(!isPostoCombustivel && isCartao){
	    	String fatura_duplicata = (String) com.tivic.manager.fsc.NfeServices.getDadosPagamentoFromNfe(cdNotaFiscal, null);
	    	params.put("FATURA_DUPLICATA", fatura_duplicata);
	    }else{
    		params.put("FATURA_DUPLICATA", "");
	    }
	    
		ConfManager conf = Util.getConfManager();
    	String reportPath = conf.getProps().getProperty("REPORT_PATH");
    	String path = ContextManager.getRealPath()+"/"+reportPath + "/fsc/danfe";
    	
    	GregorianCalendar dtMudanca = new GregorianCalendar();
    	dtMudanca.set(Calendar.DAY_OF_MONTH, 1);
    	dtMudanca.set(Calendar.MONTH, 3);
    	dtMudanca.set(Calendar.YEAR, 2015);
    	
    	if(nota.getDtEmissao().before(dtMudanca)){
    		path = ContextManager.getRealPath()+"/"+reportPath + "/fsc/danfe_defasado";
    	}
    	
    	JasperPrint jp = JasperFillManager.fillReport(ReportServices.getJasperReport(path), params, xmlDataSource);

	    ReportServices.setReportSession(session, jp, params);
	    ReportServlet report = new ReportServlet();
		report.doPost(request, response);
	}
	catch (Exception cnf){
		cnf.printStackTrace(System.out);
		output = "Erro ao processar DANFE";
		response.reset();
		response.setContentType("text/html");
	}
%>
<html>
<head>
<title>DANFE</title>
</head>

<body bgcolor="#8C92C6" style="text-align: center;">
	<font color="white"><%=output%></font>
</body>

</html>