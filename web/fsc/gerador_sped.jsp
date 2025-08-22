<%@page import="sol.util.Result"%>
<%@page contentType="text/html; charset=iso-8859-1"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types" %>
<%@page import="com.tivic.manager.util.Recursos" %>
<%@page import="com.tivic.manager.grl.FormularioAtributoServices" %>
<%@page import="sol.dao.ItemComparator" %>
<%@page import="sol.util.RequestUtilities" %>
<%@page import="sol.util.Jso" %>
<%
int cdEmpresa = RequestUtilities.getParameterAsInteger(request, "cdEmpresa", 0);
com.tivic.manager.grl.Empresa empresa = com.tivic.manager.grl.EmpresaDAO.get(cdEmpresa);
try	{
	out.clear();
	int cdContrato 				    = RequestUtilities.getParameterAsInteger(request, "cdContrato", 0);
	GregorianCalendar dtCompetencia = RequestUtilities.getParameterAsGregorianCalendar(request, "dtCompetencia", new GregorianCalendar());
	Result result = null; // com.tivic.manager.adm.SpedFiscalServices.gerarSped(cdEmpresa, new GregorianCalendar(), new GregorianCalendar());
	if(result.getCode() >= 0)	{
		response.setContentType("application/octet-stream"); 
		response.setHeader("Content-Disposition","attachment; filename=SPED_"+sol.util.Util.formatDateTime(dtCompetencia, "yyyyMM")); 
		out.write((String)result.getObjects().get("file"));
	}
	else	{
		%>
		<script>
			alert('<%=result.getMessage()%>');
		</script>
		<%
	}
}
catch(Exception e)	{
		e.printStackTrace(System.out);

}
%>