<%@page import="java.sql.Connection"%>
<%@page import="Conexao"%>
<%@page import="sol.util.Result"%>
<%@page import="com.tivic.manager.grl.PessoaServices"%>
<%@page import="com.tivic.manager.grl.PessoaEndereco"%>
<%@page import="com.tivic.manager.grl.PessoaFisica"%>
<%@page import="java.io.PrintWriter"%>
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
	    try {
			int cdResultCadastro = 0;
			String nmResultMessageCadastro;
			Result pessoaSave = new Result(0);
			
        	ResultSetMap rsmEstado = EstadoDAO.getAll();
        	
        	if(request.getParameter("cadastro") != null && request.getParameter("cadastro").equals("equipe")){
        		
        		Connection connect = Conexao.conectar();
        		connect.setAutoCommit(false);
        		        		
        		int cdVinculo = 17; 
        		String nr_rg         = request.getParameter("rg");
				String nr_cpf        = request.getParameter("cpf");
				String nascimento    = request.getParameter("nascimento");
				String estado        = request.getParameter("estado");
				String cidade        = request.getParameter("cidade");
				String logradouro    = request.getParameter("logradouro");
				String complemento   = request.getParameter("complemento");
				String telefone      = request.getParameter("telefone");
				String numero        = request.getParameter("numero");
				String bairro        = request.getParameter("bairro");
				String cep           = request.getParameter("cep");
				
        		String cpf = request.getParameter("cpf").replaceAll("-", "");
        		PessoaFisica pessoa = new PessoaFisica();
        		pessoa.setCdPessoa(0);
        		pessoa.setStCadastro(1);
        		pessoa.setNmPessoa(request.getParameter("nome"));
        		pessoa.setGnPessoa(1);
        		pessoa.setTpSexo(Integer.valueOf(request.getParameter("sexo")));
        		pessoa.setDtNascimento(Util.convStringCalendar(nascimento));
				if(telefone.length() < 14){
					pessoa.setNrTelefone1(telefone);
				} else {
					pessoa.setNrCelular(telefone);
				}
				pessoa.setStCadastro(1);
				pessoa.setNrCpf(nr_cpf.replaceAll("-", ""));
				pessoa.setNrRg(nr_rg);
				pessoa.setTpSexo(1);
        		
        		PessoaEndereco endereco = new PessoaEndereco();
        		endereco.setNmLogradouro(logradouro);
        		endereco.setNmBairro(bairro);
        		endereco.setCdCidade(Integer.valueOf(cidade));
        		endereco.setNrCep(cep.replaceAll("-", ""));
        		endereco.setNrEndereco(numero);
        		
        		pessoaSave = PessoaServices.save(pessoa, endereco, 2, cdVinculo, connect);
        		
        		System.out.println(pessoaSave);
        		        		
//         		connect.commit();
        		
        	}
  
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Jornada Pedagógica 2016 - Prefeitura Municipal de Vitória da Conquita</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="./public/css/bootstrap.min.css">
        <link rel="stylesheet" href="./public/css/global.css">
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
    					<img src="./public/images/logo.png" style="width: 150px;">
   					</a>
                    <div class="collapse navbar-collapse navbar-ex1-collapse pull-right">
                        <ul class="nav navbar-nav">
                            <li><a class="active" href="./">Início</a></li>
                            <li><a href="./#jornada">Jornada da Educação</a></li>
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
                    	<% if(pessoaSave != null && pessoaSave.getCode() > 0){ %>
                    		<div class="alert alert-success alert-white rounded">
	                        	<button type="button" data-dismiss="alert" aria-hidden="true" class="close">×</button>
	                        	<div class="icon">
	                        		<i class=""></i> 
	                        	</div>
	                        	<strong>Atenção:</strong> <%=pessoaSave.getMessage()%>
                        	</div>
                    	<% } else if(pessoaSave != null && pessoaSave.getCode() < 0) { %>
                    		<div class="alert alert-danger alert-white rounded">
	                        	<button type="button" data-dismiss="alert" aria-hidden="true" class="close">×</button>
	                        	<div class="icon">
	                        		<i class=""></i> 
	                        	</div>
	                        	<strong>Atenção:</strong> <%=pessoaSave.getMessage()%>
                        	</div>
                    	<% } %>
                        <div class="form">
                            <div class="col-md-12 form-input">
                                <div class="row">
                                   	<div class="return"></div>
                                    <form id="equipe" role="form" method="post" class="col-md-7" action="?cadastro=equipe">
                                        <div id="dadosPessoais">
	                                        <fieldset>
	    										<legend>Dados pessoais - <small>Confirme suas informações pessoais.</small></legend>
	    										<div class="row">
			                                        <div class="form-group col-md-4">
			                                            <label class="control-label">
				                                            <label>Nome Completo:</label>
				                                            <input type="text" name="nome" class="form-control input-lg" value="" >
			                                            </label>                                            
			                                        </div>
			                                        <div class="form-group col-md-4">
			                                            <label class="control-label">
				                                            <label>Sexo:</label>
				                                            <select name="sexo" class="form-control input-lg" required>
                                                				<option disabled selected>Selecione</option>
                                                				<option value="1">Feminino</option>
                                                				<option value="0">Masculino</option>
                                            				</select>
			                                            </label>                                            
			                                        </div>
			                                        <div class="form-group col-md-4">
			                                            <label class="control-label">
				                                            <label>Data de nascimento:</label>
				                                            <input type="text" name="nascimento" class="form-control input-lg" data-inputmask="'alias': 'date'" value="" >
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
	                                        </fieldset>
                                        </div>
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
                                        <input type="submit" value="Cadastrar" class="btn btn-success btn-lg btn-block disabled btn-enviar" disabled>
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
        <script src="./public/js/jquery.min.js"></script>
        <script src="./public/js/bootstrap.min.js"></script>
        <script src="./public/js/plugins/jquery.validate.min.js" type="text/javascript"></script>
        <script src="./public/js/plugins/latinize.js" type="text/javascript"></script>        
        <script src="./public/js/plugins/inputmask.js" type="text/javascript"></script>
        <script src="./public/js/global.js" type="text/javascript"></script>
    </body>
</html>
<%	} catch (Exception e) {
	System.err.print(e);
	}
%>