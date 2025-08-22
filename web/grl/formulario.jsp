<%@page import="com.tivic.manager.grl.FormularioServices"%>
<%@ taglib uri="../tlds/dotSecurityManager.tld" prefix="security" %>
<%@ taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt" %>
<%@ taglib uri="../tlds/loader.tld" prefix="loader" %>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.sql.Types"%>
<%@page import="com.tivic.manager.util.Recursos"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="sol.util.RequestUtilities"%>
<%@page import="sol.util.Jso"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="com.tivic.manager.grl.*"%>
<%@page import="com.tivic.manager.seg.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">

<%
	try {		
		int cdFormulario = sol.util.RequestUtilities.getAsInteger(request, "cdFormulario", 0);
		
		Usuario usuario   = (Usuario) session.getAttribute("usuario");
		int cdUsuario     = usuario != null ? usuario.getCdUsuario() : 0;
		String nmOperador = "";
		String nmLogin    = "";
		
		ResultSetMap rsmFormulariosAtivos = new ResultSetMap();
		
		if (session.getAttribute("usuario") != null) {
			if (usuario.getCdPessoa() > 0) {
				Pessoa pessoa = PessoaDAO.get(usuario.getCdPessoa());
				nmOperador = pessoa != null ? pessoa.getNmPessoa() : usuario.getNmLogin();
				nmLogin = pessoa != null ? usuario.getNmLogin() : pessoa.getNmPessoa();
			}
		}
		
		if(cdFormulario == 0){
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
// 			criterios.add(new ItemComparator( "CD_USUARIO", String.valueOf( cdUsuario ), ItemComparator.EQUAL, Types.INTEGER ));
			criterios.add(new ItemComparator( "ST_RESPOSTA", String.valueOf( 0 ), ItemComparator.EQUAL, Types.INTEGER ));
			rsmFormulariosAtivos = FormularioUsuarioServices.findFormularioByUsuario(criterios);
		}
		
		
		
%>
<loader:library libraries="toolbar, form, grid2.0, shortcut, filter, aba2.0" compress="false" />
    <head>
    	<title></title>
		<link href="../js/metro/css/metro-bootstrap.css" rel="stylesheet" />
		<link href="../js/metro/css/metro-bootstrap-responsive.css" rel="stylesheet" />
		<link href="../js/metro/css/docs.css" rel="stylesheet" />
		<link href="../js/metro/prettify/prettify.css" rel="stylesheet" />
		<script src="../js/metro/jquery/jquery.min.js"></script>
		<script src="../js/metro/metro.min.js"></script>
		<script>
			$(function(){
				$(".container").html('dsadsad');
			});
		</script>
        <style type="text/css" media="screen"> 
            html, body  { margin:0; height:100%; }
            .container {
            	margin: 0 auto;
            	width: 700px !important;
            	border: 1px solid #eee;
            }
        </style>
    </head>
    <body class="metro">
    	<div class="container">
    		<% if(cdFormulario == 0){ %>
    		<table class="table">
    			<thead>
    				<tr>
	    				<th class="text-left">Assunto</th>
	    				<th class="text-left">Data Final</th>
   					</tr>
    			</thead>
    			<tbody>
    			<%
    			for(int i=0; i<rsmFormulariosAtivos.getLines().size(); i++){
    				System.out.println(rsmFormulariosAtivos.getLines().get(i));
    			%>
    				<tr>
	    				<td><a href="<%=rsmFormulariosAtivos.getLines().get(i).get("NM_LINK_FORMULARIO") == null ? "#" : rsmFormulariosAtivos.getLines().get(i).get("NM_LINK_FORMULARIO")%>"><%=rsmFormulariosAtivos.getLines().get(i).get("NM_FORMULARIO")%></a></td>
	    				<td>
	    				<%=new SimpleDateFormat("dd/MM/yyyy").format(rsmFormulariosAtivos.getLines().get(i).get("DT_INICIO_FORMULARIO"))%>
	    				</td>
   					</tr>
  				<%
    			}
    			%>
    			</tbody>
    		</table>
    		<% } %>
    	</div>
    </body>
<% 
	} catch (Exception e) {
		e.printStackTrace(System.out);
	} 
%>
</html>