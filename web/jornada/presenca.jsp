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
	<style>
		.entrada, .saida {
			display: none;
		}
	</style>
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
		</div>
		<div class="col-md-12">
			<div class="container">
				<div class="row">
					<div class="col-md-12">
						<button class="btn btn-lg btn-block btn-success btn-entrada">REGISTRAR ENTRADA</button>
						<button class="btn btn-lg btn-block btn-primary btn-saida">REGISTRAR SAÍDA</button>
					</div>
				</div>
				<hr>
				<div class="row entrada text-center">
					<h1>REGISTRAR ENTRADA</h1>
					<hr>
					<input type="text" onchange="registerAction(0, this.value, this)" onKeyDown="bloquear_ctrl_j()"  placeholder="Aguardando código do leitor de código de barras..." class="form-control input-lg text-center input-entrada" />
					<hr>
				</div>
				<div class="row saida text-center">
					<h1>REGISTRAR SAÍDA</h1>
					<hr>
					<input type="text" onchange="registerAction(1, this.value, this)" onKeyDown="bloquear_ctrl_j()"  placeholder="Aguardando código do leitor de código de barras..." class="form-control input-lg text-center input-saida" />
					<hr>
				</div>				
				<div class="well result text-center">Aguardando resultado...</div>
			</div>
        </div>
        <!-- Latest compiled and minified JavaScript -->
        <script src="./public/js/jquery.min.js"></script>
        <script src="./public/js/bootstrap.min.js"></script>
        <script src="./public/js/plugins/jquery.validate.min.js" type="text/javascript"></script>
        <script src="./public/js/plugins/latinize.js" type="text/javascript"></script>        
        <script src="./public/js/plugins/inputmask.js" type="text/javascript"></script>
        <script src="./public/js/global.js" type="text/javascript"></script>
		<script>
			var ucwords = function (str) {
				return (str + '').replace(/^([a-z])|\s+([a-z])/g, function ($1) {
					return $1.toUpperCase();
				});
			}
		
			var registerAction = function(type, value, element) {
				value = value.replace(';', '/');
				typeName  = type === 0 ? 'entrada' : 'saida';
				$.get('/edf/request/jornada?registrar='+typeName+'&nrinscricao='+value, function(data){
					$(".input-entrada").val("");
					$(".input-saida").val("");
					var result = JSON.parse(data);
					if(result == true){
						$(".result").html("<span class=\"text-success\">"+ucwords(typeName)+" para "+value+" registrado com sucesso!</span>");
					} else if (data == false){
						$(".result").html("<span class=\"text-primary\">Houve um problema em registrar a "+typeName+" para "+value+"</span>");						
					} else {
						$(".result").html("<span class=\"text-primary\">Problema desconhecido, favor contatar ao desenvolvedor.</span>");	
					}
					if(type==0){						
						$(".input-entrada").focus();			
					} else {						
						$(".input-saida").focus();			
					}
				});
			};
			
			$(function(){
				
				$("input[type='text']").keydown(function(e){
					if(e.which==17 || e.which==74){
						e.preventDefault();
					}else{
						console.log(e.which);
					}
				});
				$(".btn-entrada").click(function(){
						$(".input-entrada").attr('disabled', false);	
					$(".entrada").show('fast', function(){
						$(".saida").hide();
						$(".input-saida").attr('disabled', 'disabled');		
						$(".input-entrada").focus();				
					});	
				});
				$(".btn-saida").click(function(){
						$(".input-saida").attr('disabled', false);	
					$(".saida").show('fast', function(){
						$(".entrada").hide();
						$(".input-entrada").attr('disabled', 'disabled');	
						$(".input-saida").focus();	
					});
				});
			});
		</script>
    </body>
</html>
<%	} catch (Exception e) {
	System.err.print(e);
	}
%>
