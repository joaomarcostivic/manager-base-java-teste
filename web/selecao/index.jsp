<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="com.tivic.manager.evt.EventoServices"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true" %>
<% 
	boolean manutencao = true;

	if(manutencao && request.getServerName().indexOf("192.168.1") == -1)
		response.sendError(403);

    ResultSetMap rsmEventos = EventoServices.getAll(); 
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Sistema On-line de Inscrições</title>
		<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1.0" />
		<meta name="apple-mobile-web-app-capable" content="yes" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <link rel="stylesheet" href="./public/css/bootstrap.min.css" media="screen">
        <link rel="stylesheet" href="./public/assets/css/custom.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
        <link rel="icon" href="http://www.pmvc.ba.gov.br/wp-content/uploads/fav.png" sizes="32x32" />
		<link rel="icon" href="http://www.pmvc.ba.gov.br/wp-content/uploads/fav.png" sizes="192x192" />
        <!-- @Author - Edgard Hufelande -->
        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
        <script src="../public/js/html5shiv.js"></script>
        <script src="../public/js/respond.min.js"></script>
        <![endif]-->
        <script>
        </script>
    </head>
    <body>
    	<div class="loading">
            <i class="fa fa-cog fa-spin"></i>
        </div>
        <div class="navbar navbar-default">
            <div class="container">
                <div class="navbar-header">
                    <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar-main">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    </button>
                </div>
                <div class="navbar-collapse collapse" id="navbar-main">
                    <ul class="nav navbar-nav">
                        <li>
                            <a href="./">Inicio</a>
                        </li>
                        
                        <li>
                            <a href="http://www.pmvc.ba.gov.br/categoria/publicacoes/#banner" target="_blank">Publicações</a>
                      	</li>
                      	
                        <li>
                            <a href="./contato">Contato</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div id="wrapper" style="flex-direction: column; flex: 1; align-items: center; justify-content: center;flex-flow: column;overflow: auto;">        
        	<header class="header">
	            <div class="container">
	                <img src="./public/assets/images/pmvc-logo.png" alt="logo" class="img-responsive logo" />                
	            </div>
	        </header>
	        <div class="container" style="padding: 20px 0">
	            <div class="col-md-12 col-lg-12 col-xs-12">								
	                <form method="post" class="row selecao-form" onSubmit="return false;">
	                    <div class="col-md-8 col-md-offset-2">
	                        <div class="slSelecao">
	                            <label>Selecione a Inscrição:</label>
	                            <select class="form-control" name="selecao" required>
	                                <option disabled selected>Selecione uma seleção</option>
	                                <% 
	                                if(rsmEventos != null) {
		                                rsmEventos.beforeFirst();
			                            while(rsmEventos.next()){
			                            	
			                            	String nmUrlEdital = rsmEventos.getString("NM_URL_EDITAL");
			                            	String dtInicial = rsmEventos.getDateFormat("DT_INICIAL", "dd/MM/yyyy HH:mm");
			                            	String dtFinal = rsmEventos.getDateFormat("DT_FINAL", "dd/MM/yyyy HH:mm");
			                            	boolean inTime = new Date().getTime() >= rsmEventos.getGregorianCalendar("DT_INICIAL").getTime().getTime()
				                            				 && new Date().getTime() <= rsmEventos.getGregorianCalendar("DT_FINAL").getTime().getTime();
			                            	
		                            		if(rsmEventos.getInt("TP_EVENTO") == 1 && rsmEventos.getInt("ST_EVENTO") == 1) {
			                            %>
			                            
	                           		<option value="<%=rsmEventos.getInt("CD_EVENTO")%>" <%=nmUrlEdital != null ? "data-url-edital=\""+nmUrlEdital+"\"" : "" %>
		                            <%=dtInicial != null ? "data-dtinicial=\""+dtInicial+"\"" : "" %> <%=dtFinal != null ? "data-dtfinal=\""+dtFinal+"\"" : "" %>
		                            data-valid="<%=inTime%>"
		                            >
		                            	<%=rsmEventos.getString("ID_EVENTO")%> - <%=rsmEventos.getString("NM_EVENTO")%>
	                            	</option>
	                            	
	 	                            	<%  }
			                            	//break;
		                            	}
		                            }%>
	                            </select>
	                        </div><br/>	               			
	               			<p class="well well-sm periodo-inscricao btn-edital hidden" style="margin-bottom: 5px;">              
	               				<a href="" target="_blank" class="text-warning text-xs" style="font-size: 11px;">VEJA AQUI O EDITAL DA SELEÇÃO ACIMA</a>
	               			</p>
	                        <p class="well well-sm periodo-inscricao hidden" style="margin-bottom: 5px;">             
	               				<strong>Período de Inscrição desta seleção: </strong> <span class="dt-inicial"></span> até <span class="dt-final"></span>
<!-- 	               				<strong>Período de inscrições encerrada.</strong> -->
	               			</p>
	                        <div class="panel panel-default">
	                            <div class="panel-heading">
	                                <h4>Opções <small>Escolha a opção que deseja prosseguir.</small></h4>
	                            </div>
	                            <ul class="list-group action-list">	                                
	                                <li class="list-group-item">
	                                    <div class="checkbox">
	                                        <label for="rdBoletoComprovante">
	                                        <input type="radio" name="acao" id="rdBoletoComprovante" value="grBoletoComprovante" required/>
	                                        Emissão de boleto bancário e/ou comprovante de inscrição e termos de compromisso 
	                                        </label>
	                                    </div>
	                                </li>      
	                                <li class="list-group-item">
	                                    <div class="checkbox">
	                                        <label for="rdSituacaoInscricao">                                	
	                                        <input type="radio" name="acao" id="rdSituacaoInscricao" value="stInscricao" required/>
	                                        Verificar situação do pagamento da inscrição 
	                                        </label>
	                                    </div>
	                                </li>             
	                            </ul>
	                        </div>
	                        <hr>
	                        <div class="acordo_edital">
	                            <div class="checkbox">
	                                <label for="ckEdital">
	                                <input type="checkbox" name="edital" id="ckEdital" required />
	                                Li e concordo com o edital desta seleção.
	                                </label>
	                            </div>
	                        </div>
	                        <hr>
	                        <button class="btn btn-primary btn-block btn-submit">Avançar</button>
	                    </div>
	                </form>
	
	                <div class="row load-form hide">
	                    <div class="col-md-8 col-md-offset-2 content">
	                    </div>
	                </div>
	            </div>
	        </div>
        </div>
        <footer class="footer">
      		<div class="container">
       			<p class="text-muted">Sistema desenvolvido pela <a href="http://www.tivic.com.br/" target="_blank">TIViC</a> e licenciado para a <a href="http://www.pmvc.ba.gov.br/" target="_blank">Prefeitura Municipal de Vitória da Conquista</a></p>
      		</div>
   		</footer>
        <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script src="./public/js/bootstrap.min.js"></script>
    	<script src="./public/js/jquery.validate.min.js?v=2.1"></script>
        <script src="./public/js/latinize.js" type="text/javascript"></script>        
        <script src="./public/js/inputmask.js?v=4.0.0-beta.34" type="text/javascript"></script>
        <script src="./public/assets/js/jquery.selecao.min.js" type="text/javascript"></script>
        <script type="text/javascript"></script>
    </body>
</html>