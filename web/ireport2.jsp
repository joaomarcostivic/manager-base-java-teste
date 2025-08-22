<%@page import="net.sf.jasperreports.j2ee.servlets.BaseHttpServlet"%>
<%@page import="sun.org.mozilla.javascript.internal.ast.ForInLoop"%>
<%@page import="sol.util.ConfManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tivic.sol.report.ReportServices"%>
<%@page import="com.tivic.manager.print.ReportServlet"%>
<%@page contentType="text/html; charset=iso-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta http-equiv="pragma" content="no-cache"/>
<title>Manager - Enterprise Resource Planning</title>
<script language="javascript" src="/sol/js/scriptaculous/prototype.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/builder.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/effects.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/dragdrop.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/controls.js"></script>
<script language="javascript" src="/sol/js/scriptaculous/slider.js"></script>
<script language="javascript" src="/sol/js/util.js"></script>
<script language="javascript" src="/sol/js/validacao.js"></script>
<script language="javascript" src="/sol/js/masks.js"></script>
<script language="javascript" src="/sol/js/masks2.0.js"></script>
<script language="javascript" src="/sol/js/janela2.0.js"></script>
<script language="javascript" src="/sol/js/ajax.js"></script>
<script language="javascript" src="/sol/js/jason.js"></script>
<script language="javascript" src="/sol/js/form.js"></script>
<script language="javascript" src="/sol/js/toolbar.js"></script>
<script language="javascript" src="/sol/js/shortcut.js"></script>
<script language="javascript" src="/sol/js/report.js"></script>
<script language="javascript" src="/sol/js/flatbutton.js"></script>
<script language="javascript" src="/sol/js/corners.js"></script>
<%@page import="java.sql.ResultSet"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.util.Result"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="net.sf.jasperreports.engine.JasperPrint"%>
<%@page import="net.sf.jasperreports.engine.JasperFillManager"%>
<%@page import="net.sf.jasperreports.engine.JasperExportManager"%>
<%@page import="net.sf.jasperreports.engine.JRResultSetDataSource"%>
<%@page import="net.sf.jasperreports.engine.data.JRBeanCollectionDataSource"%>
<%@page import="sol.util.MethodTrigger"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.seg.*"%>
<%@page import="com.tivic.manager.util.*"%>
<%@page import="java.io.File"%>
<%@page import="net.sf.jasperreports.engine.type.OrientationEnum"%>
<%
	
	session.removeAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE);
	String reportFile = RequestUtilities.getAsString(request, "report", "");
	String modulo     = RequestUtilities.getAsString(request, "modulo", "");
	String path       = session.getServletContext().getRealPath("/"+modulo+"/");
	String nomeJasper = RequestUtilities.getAsString(request, "nomeJasper", "");
	int cdEmpresa     = RequestUtilities.getAsInteger(request, "cdEmpresa", 0);
	Empresa empresa   = EmpresaDAO.get(cdEmpresa);
	Usuario usuario   = (Usuario)session.getAttribute("usuario");
	Pessoa pessoaUsuario = usuario!=null ? PessoaDAO.get(usuario.getCdPessoa()) : null;
	String fileName   = session.getServletContext().getRealPath("/")+reportFile;
	String output     = "";
	String className  = RequestUtilities.getAsString(request, "className", "");
	String method     = RequestUtilities.getAsString(request, "method", "");
	String execute    = RequestUtilities.getAsString(request, "execute", "");
	String objects    = RequestUtilities.getAsString(request, "objects", "");
	String id	      = RequestUtilities.getAsString(request, "id", "");
	
	String listaSubReports = RequestUtilities.getAsString(request, "listaSubReports", "");
	String p = RequestUtilities.getAsString(request, "p", "");
	
	int tpExport      = RequestUtilities.getAsInteger(request, "tpExport", -1);
	Result result     = null;
	Pessoa pessoa 	  = null;
	File file 		  = null; 
	// Se o tpExport for menor do que zero é porque é a chamada da janela principal
	//if(tpExport >= 0)
	//	result = com.tivic.manager.util.Util.processReport(request, response, fileName, tpExport);
%>
<script language="javascript">
var _PDF  = 0;
// var _HTML = 1;
// var _RTF  = 2;
// var _XLS  = 3;
// var _PPT  = 4;
// var _XML  = 5;
function init()	{
	var frameHeight;
	if (self.innerHeight)
		frameHeight = self.innerHeight;
	else if (document.documentElement && document.documentElement.clientHeight)
		frameHeight = document.documentElement.clientHeight;
	else if (document.body)
		frameHeight = document.body.clientHeight;
		
	var frameWidth;
	if (self.innerWidth)
		frameWidth = self.innerWidth;
	else if (document.documentElement && document.documentElement.clientWidth)
		frameWidth = document.documentElement.clientWidth;
	else if (document.body)
		frameWidth = document.body.clientWidth;
	$('toolBar').style.height = frameHeight+'px';
	$('iFrame').style.height = (frameHeight-8)+'px';
	$('iFrame').style.width  = (frameWidth-90)+'px';
	
	//	
	toolBar = ToolBar.create('toolBar', {plotPlace: 'toolBar', orientation: 'vertical',
								         buttons: [{id: 'btPDF', img: 'imagens/pdf64.png', label: 'PDF', imagePosition: 'top', width: 65, height: 65, onClick: function() {btnGerarOnClick(_PDF);}},
								                   {id: 'btFechar', img: 'imagens/fechar64.png', label: 'Fechar', imagePosition: 'top', width: 65, height: 65, onClick: function() {parent.closeWindow('jReport')}}]});

<%-- 	if(<%=tpExport<0%>) --%>
// 		showBotoes();
	btnGerarOnClick(_PDF); 		
}

// function showBotoes() {
// 	// createWindow('jToolBar', {noTitle: true, noDrag: true, noBringUp: true, top: 0, left: 0, width: 720, height: 93, contentDiv: $('toolBar')});
// }

function btnGerarOnClick(tpExport) {
	$('tpExport').value = tpExport;
	$('form').submit();
	<%
	try {
		Object obj = new MethodTrigger(request, response, null, true).getObjectResultCall();
		if (obj != null && obj instanceof Result)
			result = (Result)obj;
		if(result == null || (result.getCode()<0 && result.getCode() != ReportServices.DESVIO_RELATORIO))	{
			output = (result == null ? "Erro " : result.getMessage());
			response.reset();
			response.setContentType("text/html");
		}
		else if(((ResultSetMap)result.getObjects().get("rsm")).getLines().size() == 0)	{
			output = "Não há registro";
			response.reset();
			response.setContentType("text/html");
		}
		else	{
			
			if(result.getCode() == ReportServices.DESVIO_RELATORIO){
				if(result.getObjects().get("novoJasper") != null)
					nomeJasper = (String) result.getObjects().get("novoJasper");
				if(result.getObjects().get("novoModulo") != null)
					modulo     = (String) result.getObjects().get("novoModulo");
				session.setAttribute("novoTitulo", "Erro nas Categorias - Verifique e corrija");
			}
			
			HashMap<String, Object> params = (HashMap)result.getObjects().get("params");
			String caminho = ParametroServices.getValorOfParametro("NM_ARQUIVO_DESENVOLVEDOR", cdEmpresa);
			if(caminho != null && !caminho.equals(""))	{
				try	{
					file = new File(caminho);
					if(file != null){
						byte[] data = Util.getBytesFromFile(file);
						params.put("LOGO_DEV", data);
					}
				}
				catch(Exception e) {
					e.printStackTrace(System.out);
				}
			}
			
		    int cdDesenvolvedor = ParametroServices.getValorOfParametroAsInteger("CD_DESENVOLVEDOR", 0);
		    params.put("nmDesenvolvedor", "");
		    params.put("nmEmail", "");
			if(cdDesenvolvedor > 0){
				pessoa = PessoaDAO.get(cdDesenvolvedor);
			    params.put("nmDesenvolvedor", (pessoa != null) ? pessoa.getNmPessoa() : "");
			    params.put("nmEmail", (pessoa != null) ? pessoa.getNmEmail() : "");
			}
			//
			params.put("nmFantasia",    empresa!=null ? empresa.getNmPessoa() : "");
			params.put("nmRazaoSocial", empresa!=null ? empresa.getNmRazaoSocial() : "");
			params.put("lgMatriz",      empresa!=null ? (empresa.getLgMatriz() == 1? "MATRIZ" : "FILIAL" ) : "");
			String nrCNPJ = empresa!=null&&empresa.getNrCnpj().length()>=14?(empresa.getNrCnpj().substring(0,2)+"." + empresa.getNrCnpj().substring(2,5) + "." + 
					                                                         empresa.getNrCnpj().substring(5,8) + "/" + empresa.getNrCnpj().substring(8,12) + "-" +empresa.getNrCnpj().substring(12,14)):"";
			params.put("nrCNPJEmpresa", empresa != null ? nrCNPJ : "");
			params.put("nmUsuario",     pessoaUsuario!=null ? pessoaUsuario.getNmPessoa() : "");
			params.put("path",          path);
			params.put("LOGO",          empresa!=null ? empresa.getImgLogomarca() : "");
			params.put("dtHoje",        Util.convCalendarString(Util.getDataAtual()));
			params.put("hrHoje",        Util.formatDateTime(new Date(), "HH:mm:ss"));
			
			String[] lista = listaSubReports.split(",");
			ArrayList<String> listaArray = new ArrayList<String>();
			if(!lista[0].equals(""))
				for(int i = 0; i < lista.length; i++)
					listaArray.add(lista[i]);
			if(!listaArray.isEmpty()){
				params.put("SUBREPORT_NAMES", listaArray);
				ConfManager conf = Util.getConfManager();
		    	String reportPath = conf.getProps().getProperty("REPORT_PATH");
				String pathSubReport = ContextManager.getRealPath()+"/"+reportPath + "/"+modulo+"/";
				params.put("SUBREPORT_DIR", pathSubReport);
			}
			
			if(!p.equals("")) {
				String[] ps = p.split("-=-");
				for(int i=0; i<ps.length; i++) {
					String[] pair = ps[i].split("\\|");
					if(pair.length==2)
						params.put(pair[0], pair[1]);
				}
			}
			// Definido
			response.reset();
			response.setContentType("application/pdf");
			request.setAttribute("type", ReportServlet.PDF);
			ReportServices.setReportSession(session, modulo + "/" + nomeJasper, params, (ResultSetMap)result.getObjects().get("rsm"));
			//new JRBeanCollectionDataSource(rsm.getLines());
// 			JRBeanCollectionDataSource jrRS = new JRBeanCollectionDataSource(((ResultSetMap)result.getObjects().get("rsm")).getLines());
// 			JasperPrint jp = JasperFillManager.fillReport(path+"//"+nomeJasper+".jasper", params, jrRS);
// 			java.io.OutputStream outputStream = response.getOutputStream();
// 			JasperExportManager.exportReportToPdfStream(jp, outputStream);
			ReportServlet report = new ReportServlet();
			report.doPost(request, response);
			
			
		}		
	}
	catch (Exception cnf){
		cnf.printStackTrace(System.out);
		output = "Erro ao processar ireport2";
		response.reset();
		response.setContentType("text/html");
	}
		
	%>
}
</script>
</head>
<body style="background:<%=(result!=null)?"#CCC":"#F5F5F5"%>; text-align: center;" onload="init()">
	<form name="form" id="form" method="post" target="iFrame">
		<input id="file" name="file" type="hidden" value="<%=reportFile%>"/>
		<input id="className" name="className" type="hidden" value="<%=className%>"/>
		<input id="method" name="method" type="hidden" value="<%=method%>"/>
		<input id="execute" name="execute" type="hidden" value="<%=execute%>"/>
		<input id="objects" name="objects" type="hidden" value="<%=objects%>"/>
		<input id="tpExport" name="tpExport" type="hidden" value=""/>
	</form>
	<div id="toolBar" style="height:70px; width:70px; border:0 solid; float: left;"></div>
	<font color="white" size="18"><%=output%></font>
</body>
</html>
