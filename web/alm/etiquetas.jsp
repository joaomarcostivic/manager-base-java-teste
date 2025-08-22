<%@page import="com.tivic.manager.pcb.BicoServices"%>
<%@page import="com.tivic.manager.seg.Usuario"%>
<%@page import="com.tivic.manager.pcb.MedicaoFisicaServices"%>
<%@page import="com.tivic.manager.pcb.BicoEncerranteServices"%>
<%@page import="net.sf.jasperreports.engine.data.JRMapArrayDataSource"%>
<%@page import="net.sf.jasperreports.engine.data.JsonDataSource"%>
<%@page import="net.sf.jasperreports.engine.data.JRBeanCollectionDataSource"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.ctb.ContaDAO"%>
<%@page import="com.tivic.manager.ctb.Conta"%>
<%@page import="com.tivic.manager.util.*"%>
<%@page import="java.util.*"%>
<%@page import="java.lang.*"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.util.Result"%>
<%@page import="Conexao"%>
<%@page import="com.tivic.manager.adm.*"%>
<%@page import="com.tivic.manager.alm.*"%>
<%@page import="com.tivic.manager.pcb.TanqueDAO"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="net.sf.jasperreports.engine.*"%>
<%@page import="net.sf.jasperreports.engine.xml.JRXmlLoader"%>
<%@page import="net.sf.jasperreports.engine.design.JasperDesign"%>
 <%@page import="net.sf.jasperreports.engine.design.JRDesignBand"%>
<%

	
String clNome       	= RequestUtilities.getParameterAsString(request, "clNome", "");
String nmEmpresa       	= RequestUtilities.getParameterAsString(request, "nmEmpresa", "");
String idReduzido       = RequestUtilities.getParameterAsString(request, "idReduzido", "");
String nrReferencia     = RequestUtilities.getParameterAsString(request, "nrReferencia", "");
float vlPreco           = RequestUtilities.getParameterAsFloat(request, "vlPreco", 0);
int qtEtiquetas			= RequestUtilities.getParameterAsInteger(request, "qtEtiquetas", 0);
int cdDocumentoEntrada	= RequestUtilities.getParameterAsInteger(request, "cdDocumentoEntrada", 0);

float vlMargemEsquerda   = ParametroServices.getValorOfParametroAsFloat("ETQ_MARGEM_ESQUERDA",   (float)1.482); 
float vlMargemSuperior   = ParametroServices.getValorOfParametroAsFloat("ETQ_MARGEM_SUPERIOR",   (float)1.023);
float vlAltura           = ParametroServices.getValorOfParametroAsFloat("ETQ_ALTURA",            (float)1.693); 
float vlEspacoEntreHoriz = ParametroServices.getValorOfParametroAsFloat("ETQ_ESPACO_HORIZONTAL", (float)0.318);

//String path   = getServletContext().getRealPath("/alm/");
String pJrxml = getServletContext().getRealPath("/jrxml/alm/");
String output = "";

try {

		System.out.println("AAA");
		
		Connection connect = Conexao.conectar();
		// Definido
		response.reset();
		response.setContentType("application/pdf");
		HashMap<String,Object> parametros = new HashMap<String,Object>();
		ResultSetMap rsmEtiquetas = new ResultSetMap();
		if(cdDocumentoEntrada <= 0) {
			HashMap<String,Object> register;
			for(int i = 0; i < qtEtiquetas; i++)	{
				register = new HashMap<String,Object>();
				register.put("NM_PRODUTO_SERVICO", clNome);
				register.put("NM_EMPRESA", nmEmpresa);
				register.put("ID_REDUZIDO", idReduzido);
				register.put("NR_REFERENCIA", nrReferencia);
				register.put("VL_PRECO", Util.formatNumber(vlPreco));
				rsmEtiquetas.addRegister(register);
			}
		}
		else {
			ResultSetMap rsmItens = DocumentoEntradaServices.getAllItens(cdDocumentoEntrada);
			while(rsmItens.next()) {
				rsmItens.setValueToField("NM_EMPRESA", nmEmpresa);
				rsmItens.setValueToField("VL_PRECO", Util.formatNumber(rsmItens.getFloat("VL_PRECO")));
				//
				int qtEntrada = Math.round(rsmItens.getFloat("QT_ENTRADA"));
				for(int i=0; i<qtEntrada; i++)
					rsmEtiquetas.addRegister(rsmItens.getRegister());
			}
			rsmEtiquetas.beforeFirst();
		}
		// GERANDO RELATÓRIO
		JRBeanCollectionDataSource  jrRS = new JRBeanCollectionDataSource (rsmEtiquetas.getLines());
		
		// Pegando o jrxml e modificando
		JasperDesign jasper = JRXmlLoader.load(pJrxml+"/etiquetas.jrxml");
		((JRDesignBand)jasper.getDetailSection().getBands()[0]).setHeight((int)Math.round(vlAltura / 0.03528));
		// Espaçamento entre colunas
		jasper.setColumnSpacing((int)Math.round(vlEspacoEntreHoriz / 0.03528));
		jasper.setTopMargin((int)Math.round(vlMargemSuperior / 0.03528));
		jasper.setLeftMargin((int)Math.round(vlMargemEsquerda / 0.03528));
		//
		System.out.println("vlMargemEsquerda   = "+((int)Math.round(vlMargemEsquerda / 0.03528)));
		System.out.println("vlMargemSuperior   = "+((int)Math.round(vlMargemSuperior / 0.03528)));
		System.out.println("vlAltura           = "+((int)Math.round(vlAltura / 0.03528)));
		System.out.println("vlEspacoEntreHoriz = "+((int)Math.round(vlEspacoEntreHoriz / 0.03528)));
		
		// Compilando
		JasperReport report2 = JasperCompileManager.compileReport(jasper);
		JasperPrint jp       = JasperFillManager.fillReport(report2, parametros, jrRS);

		// Criando relatório			
		java.io.OutputStream outputStream = response.getOutputStream();
		
		JasperExportManager.exportReportToPdfStream(jp, outputStream);		

		//ServletOutputStream ouputStream = response.getOutputStream();  
        //ouputStream.write(bytes, 0, bytes.length);  
        outputStream.flush();  
        outputStream.close();
		
		System.out.println("BBB");
}
catch (Exception e){
	e.printStackTrace();
	System.out.println("CCC");
	output = "Erro ao processar Etiquetas";
	response.reset();
	response.setContentType("text/html");
}
%>
<html>
<head>
<title>Etiquetas</title>
</head>

<body bgcolor="#8C92C6">
<center>
	<font color="white"> <h2> <%=output%></h2></font>
</center>
</body>
</html>
