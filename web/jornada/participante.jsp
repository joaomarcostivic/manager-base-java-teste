<%@page import="com.tivic.manager.evt.EventoPessoaServices"%>
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
		    
		    ResultSetMap rsmEstado = EstadoDAO.getAll();
		    
		    String[] tpCargoPublico = EventoPessoaServices.tpCargoPublico;
		    
		    ArrayList<String> orderBy = new ArrayList<String>();
		    orderBy.add("CD_LOCAL");
		    
		    /**
		     * Busca todos os eventos simultaneos marcados para Quinta-Feira a tarde.
		     */
		    ArrayList<ItemComparator> criterios = null;
		    criterios = new ArrayList<ItemComparator>();
        	criterios.add(new ItemComparator("DT_EVENTO", "11/02/2016 14:00:00", ItemComparator.EQUAL, Types.TIMESTAMP));
        	ResultSetMap rsmEventosQuintaTarde = EventoServices.find(criterios);
        	rsmEventosQuintaTarde.orderBy(orderBy);
		    
        	/**
		     * Busca todos os eventos simultaneos marcados para Sexta-Feira pela manhã e a tarde.
		     */
		    criterios = new ArrayList<ItemComparator>();
        	criterios.add(new ItemComparator("DT_EVENTO", "12/02/2016 10:10:00", ItemComparator.EQUAL, Types.TIMESTAMP));
        	ResultSetMap rsmEventosSextaManha = EventoServices.find(criterios);
        	rsmEventosSextaManha.orderBy(orderBy);
		    
		    criterios = new ArrayList<ItemComparator>();
        	criterios.add(new ItemComparator("DT_EVENTO", "12/02/2016 14:00:00", ItemComparator.EQUAL, Types.TIMESTAMP));
        	ResultSetMap rsmEventosSextaTarde = EventoServices.find(criterios);
        	rsmEventosSextaTarde.orderBy(orderBy);

        	/**
		     * Busca todos os eventos simultaneos marcados para Sábado pela manhã e a tarde.
		     */
		    criterios = new ArrayList<ItemComparator>();
        	criterios.add(new ItemComparator("DT_EVENTO", "13/02/2016 10:10:00", ItemComparator.EQUAL, Types.TIMESTAMP));
        	ResultSetMap rsmEventosSabadoManha = EventoServices.find(criterios);
        	rsmEventosSabadoManha.orderBy(orderBy);
        	
        	criterios = new ArrayList<ItemComparator>();
        	criterios.add(new ItemComparator("DT_EVENTO", "13/02/2016 14:00:00", ItemComparator.EQUAL, Types.TIMESTAMP));
        	ResultSetMap rsmEventosSabadoTarde = EventoServices.find(criterios);
        	rsmEventosSabadoTarde.orderBy(orderBy);
  
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Jornada Pedagógica 2016 - Prefeitura Municipal de Vitória da Conquita</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="../public/css/bootstrap.min.css">
        <link rel="stylesheet" href="../public/css/global.css">
    </head>
    <body>
        <div class="col-md-12 header">
            <div class="container">
                <nav class="navbar" role="navigation">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                    </div>                    
                    <a class="navbar-brand" style="height: auto;padding:0;" rel="home">
    					<img src="../public/images/logo.png" style="width: 150px;">
   					</a>
                    <div class="collapse navbar-collapse navbar-ex1-collapse pull-right">
                        <ul class="nav navbar-nav">
                            <li><a class="active" href="../">Início</a></li>
                            <li><a href="../#jornada">Jornada da Educação</a></li>
                            <li><a href="#">Contato</a></li>
                        </ul>
                    </div>
                </nav>
            </div>
        </div>
        <div class="col-md-12">
            <div class="row content" style="background-position: 0px -50px;">
                <div class="container">
               		<div class="col-md-1 title">
                    </div>
                    <div class="col-md-11 title">
                        <h2>Jornada Pedagógica 2016</h2>
                    </div>
                </div>
            </div>
            <hr>
            <div class="row">
                <div class="container">
                    <div class="col-md-12 register">
                        <div class="form">
                            <div class="col-md-12 form-input">
                                <div class="row">
                                   	<div class="return"></div>
                                    <form id="segundo-passo" role="form" method="post" class="col-md-7">
                                        <div id="dadosPessoais">
	                                        <fieldset>
	    										<legend>Dados pessoais - <small>Confirme suas informações pessoais.</small></legend>
	    										<div class="row">
			                                        <div class="form-group col-md-4">
			                                            <label class="control-label">
				                                            <label>Nome Completo:</label>
				                                            <input type="text" name="nome" class="form-control input-lg" value="<%=session.getAttribute("nome")%>" disabled>
			                                            </label>                                            
			                                        </div>
			                                        <div class="form-group col-md-4">
			                                            <label class="control-label">
				                                            <label>Sexo:</label>
				                                            <input type="text" name="sexo" class="form-control input-lg" value="<%=Integer.parseInt((String)session.getAttribute("sexo")) == 0 ? "Masculino" : "Feminino"%>" disabled>
			                                            </label>                                            
			                                        </div>
			                                        <div class="form-group col-md-4">
			                                            <label class="control-label">
				                                            <label>Data de nascimento:</label>
				                                            <input type="text" name="nome" class="form-control input-lg" value="<%=new SimpleDateFormat("dd/MM/yyyy").format(dataNascimento)%>" disabled>
			                                            </label>                                            
			                                        </div>
		                                        </div>
		                                        <div class="form-group">
		                                        	<div class="row">             
			                                        	<div class="col-md-4">
			                                        		<label>Informe seu RG:</label>                       
					                                        <input type="text" name="rg" class="form-control input-lg" placeholder="Informe seu RG" maxlength="15" required>
				                                        </div>
				                                        <div class="col-md-4">
				                                        	<label>Informe seu CPF:</label>                                  
				                                        	<input type="text" name="cpf" class="form-control input-lg" data-inputmask="'mask': '999-999-999-99'" placeholder="Informe seu CPF" maxlength="14" required>
				                                        </div>				                                        
				                                        <div class="col-md-4">
			                                        		<label>Telefone/Celular:</label>                       
					                                        <input type="text" name="telefone" class="form-control input-lg" data-inputmask="'mask': '(99) 99999-9999'" placeholder="Telefone" maxlength="15">					                                        
			                                        		<label><small>(9º digito opcional)</small></label> 
				                                        </div>
			                                        </div>                                                                                        
		                                        </div>
	<!-- 		                                        <div class="form-group"> -->
	<!-- 		                                        	<div class="row">              -->
	<!-- 				                                        <div class="checkbox">                                        -->
	<!-- 					                                        <div class="col-md-12">                           -->
	<!-- 					                                        	<label><input type="checkbox" name="lgMatricula" checked>Eu sou professor da rede pública</label> -->
	<!-- 					                                        </div> -->
	<!-- 				                                        </div> -->
	<!-- 			                                        </div>                                                                                         -->
	<!-- 		                                        </div>      -->
	                                        </fieldset>
                                        </div>
										<% if(request.getParameter("smed") != null  && !request.getParameter("smed").equals("convidados")){ %>
                                        <div id="matricula" class="form-group">
                                        	<div class="row">      
		                                        <div class="col-md-6">
		                                        	<label>Matrícula Municipal:</label>                                  
		                                        	<input type="text" name="matricula" class="form-control input-lg"  data-inputmask="'mask': '9999999999'" placeholder="Informe seu número de matrícula" maxlength="10" required>
		                                        </div>
		                                        <div class="col-md-6">
		                                        	<label>Vínculo Municipal:</label>                                  
		                                        	<select class="form-control input-lg" name="cargo_publico" required>
			                                        	<option selected disabled>Selecione</option>
			                                        	<% for(int v=0; v<tpCargoPublico.length;v++){ %>
		                                        		<option value="<%=v%>"><%=tpCargoPublico[v]%></option>
			                                        	<% } %>
			                                        </select>
		                                        </div>
	                                        </div>                                                                                        
                                        </div>   
                                        <% } %>        
                                        <hr>
                                        <div id="enderecamento">
	                                        <fieldset>
	    										<legend>Endereçamento - <small>Preencha os dados do seu endereço.</small></legend>	    										                                        
		                                        <div class="form-group">
		                                        	<div class="row">          
				                                        <div class="col-md-5">
			                                        		<label>CEP:</label>                          
				                                        	<input type="text" name="cep" class="form-control input-lg" data-inputmask="'mask': '99999-999'" placeholder="     -   "  maxlength="10"> 
				                                        </div>   
				                                        <div class="col-md-6">   
			                                        		<label>Estado:</label>                             
					                                        <select id="estados" class="form-control input-lg" name="estado" onchange="carregarCidades(this);" required>
					                                        	<option selected disabled>Selecione</option>
					                                        	<% while(rsmEstado.next()){ %>
				                                        		<option value="<%=rsmEstado.getInt("CD_ESTADO")%>"><%=rsmEstado.getString("SG_ESTADO")%></option>
					                                        	<% } %>
					                                        </select>
				                                        </div>
			                                        </div>                                                                                        
		                                        </div>  
		                                        <div class="row">
                               		            	<div class="col-md-12">
				                               			<label class="text-warning"><small><strong>Atenção:</strong> Ao informar o CEP, seu endereço é preenchido automáticamente.</small></label>
				                                	</div>
		                                        </div>
		                                        <div class="form-group">   
		                                            <label class="control-label">
		                                            </label>
		                                        	<div class="row">             
			                                        	<div class="col-md-6">
			                                        		<label>Cidade:</label>     
					                                        <select id="cidades" name="cidade" class="form-control input-lg" required>
					                                        	<option selected disabled>Selecione seu estado</option>
					                                        </select>
				                                        </div>
			                                        	<div class="col-md-6">
			                                        		<label>Bairro:</label>                          
					                                        <input type="text" name="bairro" class="form-control input-lg" placeholder="Bairro" maxlength="20" required>
				                                        </div>
			                                        </div>
		                                        </div>		  
		                                        <div class="form-group">
		                                        	<div class="row">             
			                                        	<div class="col-md-9">
			                                        		<label>Endereço:</label> 
		                                          			<input type="text" name="logradouro" class="form-control input-lg" placeholder="Logradouro" maxlength="30" required>
		                                       			</div>      
		                                       			<div class="col-md-3">
			                                        		<label>Número:</label>
		                                       				<input type="text" name="numero" class="form-control input-lg" placeholder="Número" maxlength="30" required>
		                                       			</div>    
		                                   			</div>                                
		                                        </div>                                    
		                                        <div class="form-group">
	                                        		<label>Complemento:</label>
		                                            <input type="text" name="complemento" class="form-control input-lg" placeholder="Complemento"  maxlength="50">                                            
		                                        </div>
                                   		 	</fieldset>
                                        </div>
                                        <hr>	                                        
                                        <div id="cursos">
	                                        <fieldset>
    										<legend>Selecione seus eventos - <small>Selecione um evento por dia.</small></legend>
		                                        <div class="form-group">   
		                                            <label class="control-label">
		                                            </label>
		                                        	<div class="row">             
			                                        	<div class="col-md-12">
			                                        		<h4><strong>Quinta-feira 11/02 <span class="text-danger">(TURNO DA TARDE)</span>:</strong></h4>
			                                        		<% i = 0; %>
															<% while(rsmEventosQuintaTarde.next()){ %>
															<div class="radio">
																<label><input type="radio" name="evento_quinta_tarde" value="<%=rsmEventosQuintaTarde.getInt("CD_EVENTO")%>" <%=(i == 0 ? "required" : "")%>><%=rsmEventosQuintaTarde.getString("NM_EVENTO")%></label><br />																
																<label style="cursor: default; !important; font-size: 10px;"><strong><%=rsmEventosQuintaTarde.getString("NM_PESSOA")%></strong>: <span class="text-muted"><%=rsmEventosQuintaTarde.getString("TXT_OBSERVACAO")%></span></label>
															</div>
															<hr>
															<%	i++; %>
															<% } %>
				                                        </div>
			                                        </div>
		                                        </div>
		                                        <div class="form-group">   
		                                            <label class="control-label">
		                                            </label>
		                                        	<div class="row">             
			                                        	<div class="col-md-12">
			                                        		<h4><strong>Sexta-feira 12/02 <span class="text-danger">(TURNO DA MANHÃ)</span>:</strong></h4>   
			                                        		<% i = 0; %>
					                                        <% while(rsmEventosSextaManha.next()){ %>
															<div class="radio">
																<label><input type="radio" name="evento_sexta_manha" value="<%=rsmEventosSextaManha.getInt("CD_EVENTO")%>" <%=(i == 0 ? "required" : "")%>><%=rsmEventosSextaManha.getString("NM_EVENTO")%></label><br />
																<label style="cursor: default; !important; font-size: 10px;"><strong><%=rsmEventosSextaManha.getString("NM_PESSOA")%></strong>: <span class="text-muted"><%=rsmEventosSextaManha.getString("TXT_OBSERVACAO")%></span></label>
															</div>
															<hr>
															<%	i++; %>
															<% } %>
				                                        </div>
			                                        </div> 
		                                        </div>
		                                        <div class="form-group">   
		                                            <label class="control-label">
		                                            </label>
		                                        	<div class="row">             
			                                        	<div class="col-md-12">
			                                        		<h4><strong>Sexta-feira 12/02 <span class="text-danger">(TURNO DA TARDE)</span>:</strong></h4>   
			                                        		<% i = 0; %>
					                                        <% while(rsmEventosSextaTarde.next()){ %>
															<div class="radio">
																<label><input type="radio" name="evento_sexta_tarde" value="<%=rsmEventosSextaTarde.getInt("CD_EVENTO")%>" <%=(i == 0 ? "required" : "")%>><%=rsmEventosSextaTarde.getString("NM_EVENTO")%></label><br />
																<label style="cursor: default; !important; font-size: 10px;"><strong><%=rsmEventosSextaTarde.getString("NM_PESSOA")%></strong>: <span class="text-muted"><%=rsmEventosSextaTarde.getString("TXT_OBSERVACAO")%></span></label>
															</div>		
															<hr>													
															<%	i++; %>
															<% } %>
				                                        </div>
			                                        </div> 
		                                        </div>
		                                        <div class="form-group">   
		                                            <label class="control-label">
		                                            </label>
		                                        	<div class="row">  
			                                        	<div class="col-md-12">
			                                        		<h4><strong>Sábado 13/02 <span class="text-danger">(TURNO DA MANHÃ)</span>:</strong></h4>
			                                        		<% i = 0; %>   
					                                        <% while(rsmEventosSabadoManha.next()){ %>
															<div class="radio">
																<label><input type="radio" name="evento_sabado_manha" value="<%=rsmEventosSabadoManha.getInt("CD_EVENTO")%>" <%=(i == 0 ? "required" : "")%>><%=rsmEventosSabadoManha.getString("NM_EVENTO")%></label><br />
																<label style="cursor: default; !important; font-size: 10px;"><strong><%=rsmEventosSabadoManha.getString("NM_PESSOA")%></strong>: <span class="text-muted"><%=rsmEventosSabadoManha.getString("TXT_OBSERVACAO")%></span></label>
															</div>
															<hr>
															<%	i++; %>
															<% } %>
				                                        </div>
			                                        </div> 
		                                        </div>
		                                        <div class="form-group">   
		                                            <label class="control-label">
		                                            </label>
		                                        	<div class="row">  
			                                        	<div class="col-md-12">
			                                        		<h4><strong>Sábado 13/02 <span class="text-danger">(TURNO DA TARDE)</span>:</strong></h4> 
			                                        		<% i = 0; %>  
					                                        <% while(rsmEventosSabadoTarde.next()){ %>
															<div class="radio">
																<label><input type="radio" name="evento_sabado_tarde" value="<%=rsmEventosSabadoTarde.getInt("CD_EVENTO")%>" <%=(i == 0 ? "required" : "")%>><%=rsmEventosSabadoTarde.getString("NM_EVENTO")%> (<%=(rsmEventosSabadoTarde.getInt("LG_ATIVADO") == 0 ? "<span class=\"text-primary\">Sem vagas</span>" : "")%>)</label><br />
																<label style="cursor: default; !important; font-size: 10px;"><strong><%=rsmEventosSabadoTarde.getString("NM_PESSOA")%></strong>: <span class="text-muted"><%=rsmEventosSabadoTarde.getString("TXT_OBSERVACAO")%></span></label>
															</div>
															<hr>
															<%	i++; %>
															<% } %>
				                                        </div>
			                                        </div> 
		                                        </div>
                                   		 	</fieldset>
                                        </div>
                                        <hr>
                                        <input type="submit" value="Terceiro e último passo!" class="btn btn-danger btn-lg btn-block disabled btn-enviar" disabled>
                                        <hr>
                                    </form>
                                    <div class="col-md-4 pull-right">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Latest compiled and minified JavaScript -->
        <script src="../public/js/jquery.min.js"></script>
        <script src="../public/js/bootstrap.min.js"></script>
        <script src="../public/js/plugins/jquery.validate.min.js" type="text/javascript"></script>
        <script src="../public/js/plugins/latinize.js" type="text/javascript"></script>        
        <script src="../public/js/plugins/inputmask.js" type="text/javascript"></script>
        <script src="../public/js/global.js" type="text/javascript"></script>
    </body>
</html>
<%	} catch (Exception e) {
	System.err.print(e);
	}
}%>