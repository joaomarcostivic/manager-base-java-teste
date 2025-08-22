<%@page import="com.tivic.manager.grl.PessoaDoencaServices"%>
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
		
		String nrCpf = RequestUtilities.getParameterAsString(request, "nr_cpf", "");
		int cdEventoPrincipal = RequestUtilities.getParameterAsInteger(request, "cd_evento_principal", 0);
		
		ResultSetMap rsmPessoa = new ResultSetMap();
		ResultSetMap rsmDeficiencias = new ResultSetMap();
		if(!nrCpf.equals("")){
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("D.NR_CPF", nrCpf.replaceAll("[\\.-]", ""), ItemComparator.EQUAL, Types.VARCHAR));
            //criterios.add(new ItemComparator("A.CD_EVENTO", cdEventoPrincipal+"", ItemComparator.EQUAL, Types.VARCHAR));
			
			rsmPessoa = EventoPessoaServices.getAllPessoas(criterios);     

            ArrayList<String> order = new ArrayList<String>();
            order.add("DT_INSCRICAO DESC");
            rsmPessoa.orderBy(order);
						
			if(rsmPessoa.size() > 0){
				int cdPessoa = Integer.parseInt(String.valueOf(rsmPessoa.getLines().get(0).get("CD_PESSOA")));
				rsmDeficiencias	= PessoaDoencaServices.getDoencaByPessoa(cdPessoa);
			}
		}
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
        <div class="row selecao-form">
            <form id="" method="post" class="col-md-8 col-md-offset-2" OnSubmit="return false;">
                <h3>Situação da inscrição</h3>
                <hr>
                <div class="row">
	                <div class="col-lg-12">
			        <% if(rsmPessoa.next()) { 
			        	rsmPessoa.beforeFirst();
			        	while (rsmPessoa.next()){
			       	%>

                        <div class="panel-group">
                            <div class="panel panel-default">
                                <div class="panel-heading clearfix">
                                    <h4 class="panel-title pull-left" style="padding-top: 7.5px;">
                                        Inscrição de n° <strong><%=rsmPessoa.getString("ID_CADASTRO")%></strong>
                                        <small>Efetuada em <%=rsmPessoa.getDateFormat("DT_INSCRICAO", "dd/MM/yyyy")%></small>
                                    </h4>
                                    <a class="btn btn-info btn-sm pull-right" data-toggle="collapse" href="#collapse<%=rsmPessoa.getPosition()%>">Visualizar</a>
                                </div>
                                <div id="collapse<%=rsmPessoa.getPosition()%>" class="panel-collapse collapse">
                                    <div class="panel-body">
                                        <div class="well bs-component comprovante">
                                            <fieldset>                              
                                                <% if(rsmPessoa.getInt("ST_CONTA") == 1) { %>               
                                                <legend class="text-center">
                                                    <strong>Comprovante de inscrição</strong>
                                                </legend>
                                                <% } %>
                                                <div class="form-group row">
                                                    <label for="inputEmail" class="col-lg-4 control-label text-right">Número de Inscrição: </label>
                                                    <div class="col-lg-8">
                                                        <%=rsmPessoa.getString("ID_CADASTRO")%>
                                                    </div>
                                                </div>
                                                <div class="form-group row">
                                                    <label for="inputEmail" class="col-lg-4 control-label text-right">Nome Completo: </label>
                                                    <div class="col-lg-8">
                                                        <%=rsmPessoa.getString("NM_PESSOA")%>
                                                    </div>
                                                </div>
                                                <div class="form-group row">
                                                    <label for="inputEmail" class="col-lg-4 control-label text-right">Documento de Indentidade: </label>
                                                    <div class="col-lg-8">
                                                        <%=rsmPessoa.getString("NR_RG")%>
                                                    </div>
                                                </div>
                                                <div class="form-group row">
                                                    <label for="inputEmail" class="col-lg-4 control-label text-right">CPF: </label>
                                                    <div class="col-lg-8">
                                                        <%=Util.formatCpf(rsmPessoa.getString("NR_CPF"))%>
                                                    </div>
                                                </div>
												<% if(rsmDeficiencias.size() > 0) {%>
													<div class="form-group row">
														<label for="inputEmail" class="col-lg-4 control-label text-right">Deficiência(s): </label>
														<div class="col-lg-8">
															<%while(rsmDeficiencias.next()) {%>
																<%=rsmDeficiencias.getString("NM_DOENCA")%><br />
															<%}%>
														</div>
                                                </div>
												<% } %>												
                                                <div class="form-group row">
                                                    <label for="inputEmail" class="col-lg-4 control-label text-right">Endereço: </label>
                                                    <div class="col-lg-8">
                                                        <%=rsmPessoa.getString("NM_LOGRADOURO")%>, <%=rsmPessoa.getString("NR_ENDERECO")%> - <%=rsmPessoa.getString("NM_BAIRRO")%>
                                                    </div>
                                                </div>                                  
                                                <div class="form-group row">
                                                    <label for="inputEmail" class="col-lg-4 control-label text-right">Cidade/Estado: </label>
                                                    <div class="col-lg-8">
                                                        <%=rsmPessoa.getString("NM_CIDADE")%>/<%=rsmPessoa.getString("SG_ESTADO")%>
                                                    </div>
                                                </div>
                                                <div class="form-group row">
                                                    <label for="inputEmail" class="col-lg-4 control-label text-right">Opção de Vaga: </label>
                                                    <div class="col-lg-8">
                                                         <%=rsmPessoa.getString("NM_OPCAO_SELECAO")%> 
                                                    </div>
                                                </div>				
												<% if(rsmPessoa.getString("NM_LOCAL") != null && !rsmPessoa.getString("NM_LOCAL").equals("")) { %>
                                                <div class="form-group row">
                                                    <label for="inputEmail" class="col-lg-4 control-label text-right">Localidade de trabalho: </label>
                                                    <div class="col-lg-8">
                                                         <%=rsmPessoa.getString("NM_LOCAL")%> 
                                                    </div>
                                                </div>
												<% } %>
                                                <% if(rsmPessoa.getInt("CD_CONTA_RECEBER") > 0 && rsmPessoa.getDouble("ST_CONTA") != 3) { %>
                                                <div class="form-group row">
                                                    <label for="inputEmail" class="col-lg-4 control-label text-right">Valor da Inscrição: </label>
                                                    <div class="col-lg-8">
                                                        R$ <%=Util.formatNumber(rsmPessoa.getDouble("VL_INSCRICAO"))%>
                                                    </div>
                                                </div>                        
                                                <div class="form-group row">
                                                    <label for="inputEmail" class="col-lg-4 control-label text-right">Situação: </label>
                                                    <div class="col-lg-8">
                                                       <strong>
                                                       <%if(rsmPessoa.getInt("ST_CONTA") == 1) { %>
                                                    		<span class="text-success">Pagamento de inscrição confirmado.</span>
                                                        <% } else { %>
                                                    		<span class="text-danger">Pagamento aguardando confirmação.</span>
                                                   		<% } %>
                                                   		</strong>
													   <br />
													    <% if(!rsmPessoa.getString("NR_MATRICULA").equals("")) { %>
	 														<small>(Caso tenha solicitado a isenção, favor aguarde o resultado da homologação)</small>
														<% } %>
                                                    </div>
                                                </div>     
                                                <% } else { %>       
                                                <div class="form-group row">
                                                    <label for="inputEmail" class="col-lg-4 control-label text-right">Situação: </label>
                                                    <div class="col-lg-8">
                                                       <% if(!rsmPessoa.getString("NR_MATRICULA").equals("") && rsmPessoa.getString("NR_MATRICULA").length() == 11) { %>
                                                       <strong><span class="text-success">Inscrição confirmada (NIS: <%=rsmPessoa.getString("NR_MATRICULA") %>).</span></strong>
                                                       <% } else if(rsmPessoa.getString("NR_MATRICULA").equals("")) { %>
	                                                       	<strong><span class="text-success">Inscrição confirmada.</span></strong>
                                                       <% } %>
                                                    </div>
                                                </div> 
                                                <% } %>
                                                <div class="form-group row">
                                                    <label for="inputEmail" class="col-lg-4 control-label text-right">Data e Hora da Inscrição: </label>
                                                    <div class="col-lg-8">
                                                        <%=rsmPessoa.getDateFormat("DT_INSCRICAO", "dd/MM/yyyy")%> ás <%=rsmPessoa.getDateFormat("DT_INSCRICAO", "HH:mm:ss")%>
                                                    </div>
                                                </div>      
                                                <% if(rsmPessoa.getString("NR_MATRICULA").equals("") && rsmPessoa.getInt("ST_CONTA") != 1) { %>
                                                <hr>
                                                <span class="text-warning">Caso seu pagamento não seja confirmado até o dia da realização da prova, favor levar o comprovante de pagamento.</span>
                                                <% } %>
                                            </fieldset>
                                            <hr>
                                            <button class="btn btn-primary btn-comprovante btn-block" data-nm-selecao="<%=rsmPessoa.getString("NM_SELECAO")%> ">Emitir comprovante de inscrição</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
        				<% } %>
                        <hr />
						<button class="btn btn-default btn-back btn-block" onClick="return false;">Realizar uma nova consulta</button>
	                </div>
                </div>
                <hr>
            </form>
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
        <script src="https://raw.githubusercontent.com/erikzaadi/jQueryPlugins/master/jQuery.printElement/jquery.printElement.min.js"></script>	
        <script src="./public/assets/js/jquery.selecao.min.js"></script>
        <script type="text/javascript">
	        var printPopup = function (data, nmSelecao) {
	        	data = data.replace("<button class=\"btn btn-primary btn-comprovante btn-block\">Emitir comprovante de inscrição</button>", "");
	            var mywindow = window.open('print.html', nmSelecao + ' - PMVC','width=800,height=800'); 
	            mywindow.document.write('<html><head><title>Concurso - PMVC</title>');
	            mywindow.document.write('<link rel="stylesheet" href="./public/css/bootstrap.min.css" type="text/css" />');
	            mywindow.document.write('<link rel="stylesheet" href="./public/assets/css/custom.min.css" type="text/css" />');
	            mywindow.document.write('</head><body onLoad="window.print();" style="color: #000 !important; background: #FFFFFF !important; padding: 20px;overflow: scroll;"><div class="row"><div class="col-md-7 col-sm-7 clearfix" style="width: 70%;"><h3>Comprovante de Inscrição <br /> <small>' + nmSelecao + '</small></h3></div><div style="position:absolute; top: 20px; right: 20px;"><img src=\"./public/assets/images/pmvc-logo.png" class="pull-right" width="200"/></div></div><hr />');
				mywindow.document.write("<div class=\"row\">");
	            mywindow.document.write(data);
				mywindow.document.write("</div>");
	            mywindow.document.write('</body></html>');
	            mywindow.window.location.reload();    // this is the secret ingredient
	            mywindow.document.close();
	            mywindow.focus(); 
	            mywindow.print();
	            return true;
	        };
            $(function(){            	
            	$(":input").keyup(function(e){
            		e.preventDefault();
            	}).inputmask();
            	
            	$(".btn-comprovante").click(function(){
            		var content   = $(this).closest('.comprovante').html();
					var nmSelecao = $(this).data('nmSelecao');
            		printPopup($(content).css({width: '800px', margin: '0 auto'}).html(), nmSelecao);
            	});
            });
        </script>
    </body>
</html>
<% } catch (Exception e) {
	e.printStackTrace();
	System.out.println(e);
 } %>
