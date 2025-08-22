<%@page import="java.util.HashMap"%>
<%@page import="sol.dao.Util"%>
<%@page import="com.tivic.manager.evt.EventoServices"%>
<%@page import="java.sql.Types"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@page import="com.tivic.manager.grl.EstadoDAO"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%	
	if(session.getAttribute("nome") == null){
		response.sendRedirect("../../jornada/");
	} else {
	    
	    try {
	    	
	    	GregorianCalendar date = (GregorianCalendar)session.getAttribute("dataNascimento");
		    Date dataNascimento = date.getTime();
		    int i = 0;
  
%>
<!DOCTYPE html>
<html>

<head>
    <title>Jornada Pedagógica 2015 - Prefeitura Municipal de Vitória da Conquita</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="./public/css/bootstrap.min.css">
    <link rel="stylesheet" href="./public/css/global.css">
    <script>
    	var printComprovante = function(){
    		var w = window.open('../report?filename=comprovante.pdf&download=1', '_blank');
    		return false;
    	}
    </script>
</head>
	<body>
		<hr>
	    <div class="form-group">
	        <div class="row">
	            <div class="col-md-4">
	                <label>Nome Completo:</label>
	                <input type="text" name="nome" class="form-control input-lg" value="<%=session.getAttribute("nome")%>" readonly>
	            </div>
	            <div class="col-md-4">
	                <label>Sexo:</label>
	                <input type="text" name="sexo" class="form-control input-lg" value="<%=Integer.parseInt((String)session.getAttribute("sexo")) == 0 ? "Masculino " : "Feminino "%>" readonly>
	            </div>
	            <div class="col-md-4">
	                <label>Data de nascimento:</label>
	                <input type="text" name="nome" class="form-control input-lg" value="<%=new SimpleDateFormat("dd/MM/yyyy").format(dataNascimento)%>" readonly>
	            </div>
	        </div>
	    </div>
	    <div class="form-group">
	        <div class="row">
	            <div class="col-md-4">
	                <label>Informe seu RG:</label>
	                <input type="text" name="rg" class="form-control input-lg" value="<%=session.getAttribute("nr_rg")%>" readonly>
	            </div>
	            <div class="col-md-4">
	                <label>Informe seu CPF:</label>
	                <input type="text" name="cpf" class="form-control input-lg" value="<%=session.getAttribute("nr_cpf")%>" readonly>
	            </div>
	        </div>
	    </div>
	    <hr>
	    <a href="../report?type=0" target="_blank" class="btn btn-lg btn-info">Imprimir Comprovante</a>
	    <a href="about:blank" onClick="printComprovante()" target="_blank" class="btn btn-lg btn-success">Baixar Comprovante</a>
	</body>
</html>
<%	} catch (Exception e) {
	System.err.print(e);
	}
}%>