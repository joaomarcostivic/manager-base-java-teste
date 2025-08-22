<%@page import="sol.util.RequestUtilities"%>
<%@page import="com.tivic.manager.evt.EventoPessoaServices"%>
<%@page import="java.sql.Types"%>
<%@page import="sol.dao.ItemComparator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="com.tivic.manager.evt.EventoServices"%>
<%@page import="com.tivic.manager.grl.PessoaServices"%>
<%@page import="com.tivic.manager.grl.EstadoDAO"%>
<%@page import="sol.dao.ResultSetMap"%>
<% 
	try {
		
		String nrCpf          = RequestUtilities.getParameterAsString(request, "nr_cpf", "");
		int cdEventoPrincipal = RequestUtilities.getParameterAsInteger(request, "cd_evento_principal", 0);
        int lgUltimaInscricao = RequestUtilities.getParameterAsInteger(request, "lg_ultima_inscricao", 0);
        
		ResultSetMap rsmPessoa = new ResultSetMap();
		if(!nrCpf.equals("")){
			ArrayList<ItemComparator> criterios = null;
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("D.NR_CPF", nrCpf.replaceAll("[\\.-]", ""), ItemComparator.EQUAL, Types.VARCHAR));
            criterios.add(new ItemComparator("A.CD_EVENTO", cdEventoPrincipal+"", ItemComparator.EQUAL, Types.VARCHAR));
			
			rsmPessoa = EventoPessoaServices.getAllPessoas(criterios);
			System.out.println(rsmPessoa.toString());
			
            
            ArrayList<String> order = new ArrayList<String>();
            order.add("DT_INSCRICAO DESC");
            rsmPessoa.orderBy(order);
			
		}
		System.out.println(rsmPessoa);
		rsmPessoa.beforeFirst();
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Concurso - PMVC</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <link rel="stylesheet" href="./public/css/bootstrap.min.css" media="screen">
        <link rel="stylesheet" href="./public/assets/css/custom.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
        <link rel="icon" href="./public/assets/favicon.png" type="image/x-icon" />
        <!-- @Author - Edgard Hufelande -->
        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
        <script src="../public/js/html5shiv.js"></script>
        <script src="../public/js/respond.min.js"></script>
        <![endif]-->
        <script>
            // TODO
        </script>
    </head>
    <body>
        <div class="row selecao-form" style="margin-bottom: 0;">
        	<div class="col-md-8 col-md-offset-2">        		
                <h3>Emissão de boleto</h3>
                <hr>          
        	</div>            
            <div class="col-md-8 col-md-offset-2">        		
                <button class="btn btn-default btn-back pull-right" onClick="return false;">Realizar uma nova consulta</button>
           	</div>
        	 <% if(rsmPessoa.size() > 0) { 
	        		rsmPessoa.beforeFirst();
	        		while (rsmPessoa.next()){
        	 %>
            <form id="" method="post" class="col-md-8 col-md-offset-2" OnSubmit="return false;">                      
                <div class="row">
	                <div class="col-lg-12">
	                	<p class="lead">Inscrição N°: <%=rsmPessoa.getString("ID_CADASTRO")%></p>
						<div class="well bs-component">
					        <fieldset>
					            <div class="form-group row">
					                <label for="inputEmail" class="col-lg-4 control-label text-right">Nome Completo: </label>
					                <div class="col-lg-8">
					                    <%=rsmPessoa.getString("NM_PESSOA")%>
					                </div>
					            </div>
					            <div class="form-group row">
					                <label for="inputEmail" class="col-lg-4 control-label text-right">CPF: </label>
					                <div class="col-lg-8">
					                    <%=Util.formatCpf(rsmPessoa.getString("NR_CPF"))%>
					                </div>
					            </div>
					            <div class="form-group row">
					                <label for="inputEmail" class="col-lg-4 control-label text-right">Opção de Vaga: </label>
					                <div class="col-lg-8">
					                     <%=rsmPessoa.getString("NM_OPCAO_SELECAO")%>
					                </div> 
					            </div>
					            <div class="form-group row">
					                <label for="inputEmail" class="col-lg-4 control-label text-right">Valor da Inscrição: </label>
					                <div class="col-lg-8">
					                    R$ <%=Util.formatNumber(rsmPessoa.getDouble("VL_INSCRICAO"))%>
					                </div>
					            </div>
                                <div class="form-group row">
                                    <label for="inputEmail" class="col-lg-4 control-label text-right">Data da Inscrição: </label>
                                    <div class="col-lg-8">
                                        <%=rsmPessoa.getDateFormat("DT_INSCRICAO", "dd/MM/yyyy")%>
                                    </div>
                                </div>
					        </fieldset>
						</div>
	                </div>
                </div>
            </form>
            <%	
				System.out.println(rsmPessoa.getInt("CD_CONTA_RECEBER") > 0);
				System.out.println(rsmPessoa.getInt("ST_CONTA") == 0);
				System.out.println(rsmPessoa.getGregorianCalendar("DT_PRORROGACAO") != null);
				System.out.println(rsmPessoa.getDouble("VL_INSCRICAO") > 0);
				//System.out.println((rsmPessoa.getInt("CD_CONTA_RECEBER") > 0 && rsmPessoa.getInt("ST_CONTA") == 0 && rsmPessoa.getGregorianCalendar("DT_PRORROGACAO") != null && rsmPessoa.getDouble("VL_INSCRICAO") > 0));
				
            	if((rsmPessoa.getInt("CD_CONTA_RECEBER") > 0 && rsmPessoa.getInt("ST_CONTA") == 0 && rsmPessoa.getString("NR_MATRICULA").trim().equals("") && rsmPessoa.getDouble("VL_INSCRICAO") > 0) ||
            	  (rsmPessoa.getInt("CD_CONTA_RECEBER") > 0 && rsmPessoa.getInt("ST_CONTA") == 0 && rsmPessoa.getGregorianCalendar("DT_PRORROGACAO") != null && rsmPessoa.getDouble("VL_INSCRICAO") > 0)){ %>
			<form action="../request/selecao/?acao=emitirBoleto" class="col-md-8 col-md-offset-2" method="post" target="_blank" accept-charset="ISO-8859-1">
				<input type="hidden" name="nrCpf" value="<%=rsmPessoa.getString("NR_CPF")%>">				
				<input type="hidden" name="cdSelecao" value="<%=rsmPessoa.getString("CD_EVENTO")%>">
				<input type="hidden" name="cdVaga" value="<%=rsmPessoa.getString("CD_SUBEVENTO")%>">

				<button type="submit" class="btn btn-primary btn-send btn-block">Gerar boleto bancário</button>
        		<hr>
			</form>		
            <hr />
            <%
             	}
                 if(rsmPessoa.getInt("CD_PESSOA") > 0 && lgUltimaInscricao != 0){
                     break;   
                 }
            } %>
        </div>
        <% if(rsmPessoa.getInt("ST_CONTA") == 0 && !rsmPessoa.getString("NR_MATRICULA").equals("") && rsmPessoa.getGregorianCalendar("DT_PRORROGACAO") == null){ %>
         <div class="col-md-8 col-md-offset-2" style="padding: 0 5px;">
       		<div class="alert alert-info" style="font-size: 13px; padding: 10px;">
 			  <strong class="text-sm">Aviso: </strong> Caso tenha solicitado a isenção, favor aguardar o resultado da homologação.
 			</div>
 		</div>
		<% } %>
        <div class="col-md-8 col-md-offset-2">        		
            <button class="btn btn-default btn-back pull-right" onClick="return false;">Realizar uma nova consulta</button>
       	</div>
     	<% } else { %>
     	<div class="col-md-8 col-md-offset-2">
     		<i class="fa fa-exclamation-circle text-muted  col-md-offset-4" style="font-size: 20em;"></i>
	     	<div class="notice notice-danger">
				<strong>Atenção:</strong> Não encontramos nenhum resultado com o <strong>CPF <%=Util.formatCpf(nrCpf)%></strong>, caso tenha digitado um cpf incorreto, favor fazer uma nova pesquisa.
			</div>
			<hr>
			<button class="btn btn-default btn-back btn-block">Realizar uma nova consulta</button>
     	</div>
     	<% } %>
        <script src="./public/assets/js/ajax.requests.js"></script>	
        <script src="./public/assets/js/jquery.selecao.min.js"></script>
    </body>
</html>
<% } catch (Exception e) {
	e.printStackTrace();
	System.out.println(e);
 } %>
