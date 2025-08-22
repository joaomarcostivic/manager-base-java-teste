<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true" %>
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
                        <a class="navbar-brand" style="height: auto;padding:0;" rel="home" href="#" title="Buy Sell Rent Everyting">
                        <img src="./public/images/logo.png" style="width: 150px;">
                        </a>
                    </div>
                    <div class="collapse navbar-collapse navbar-ex1-collapse pull-right">
                        <ul class="nav navbar-nav">
                            <li><a class="active" href="./">Início</a></li>
                            <li><a href="#jornada">Jornada da Educação</a></li>
                            <li><a href="#">Contato</a></li>
                        </ul>
                    </div>
                </nav>
            </div>
        </div>
        <div class="col-md-12">
            <div class="row content">
                <!--                	<div class="main-overlay-1"></div> -->
                <div class="col-md-6 notice" style="height: 100%;">
                    <!--                     <h1>Jornada Pedagógica 2015</h1> -->
                    <!--                     <span>Com o tema “Avaliação: O Caminho para construção de si mesmo” a jornada pedagógica 2015, no município de Vitória da Conquista aborda temas como a Educação Contemporânea e o papel do educador.</span> -->
                    <img alt="balao" src="./public/images/balao.png" class="floating img-responsive center-block">
                </div>
                <div class="col-md-6 register">
                    <div class="row form well">
                        <h2 class="form-title">
                            <strong>Vamos começar sua inscrição?</strong> 
                            <p>Dados para iniciarmos sua inscrição</p>
                        </h2>
                        
                        <div class="col-md-12">
                            <div class="row">
                                <form id="primeiros-passos" role="form" method="post" onsubmit="return false;">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-12 col-md-12">
                                            <div class="form-group">
                                                <label class="input-label">Nome completo:</label>     
                                                <input type="text" name="nome" class="form-control input-lg" placeholder="Nome completo" required>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-md-5">
                                            <label class="input-label">Sexo:</label>
                                            <select name="sexo" class="form-control input-lg" required>
                                                <option disabled selected>Selecione</option>
                                                <option value="1">Feminino</option>
                                                <option value="0">Masculino</option>
                                            </select>
                                        </div>
                                        <div class="form-group col-md-7">
                                            <label class="input-label">Endereço de e-mail:</label>
                                            <input type="email" name="email" class="form-control input-lg" placeholder="Endereço de e-mail" required>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="input-label">Data de nascimento:</label>
                                        <input type="text" name="dtnascimento" class="form-control input-lg" data-inputmask="'alias': 'date'" placeholder="___/___/_____" required>
                                    </div>
                                    <% if(request.getParameter("smed") != null  && request.getParameter("smed").equals("convidados")){ %>
                                    <input type="hidden" name="smed" value="1" />
                                    <% } %>
                                    <div class="checkbox">
                                        <label><input type="checkbox" name="termos" id="termos" onClick="openAcordoModal();" required> Li e concordo com os <a href="#" onClick="openAcordoModal()">acordos de inscrição</a> da Jornada Pedagógica 2016</label>
                                    </div>
                                    <input type="submit" value="Próximo passo!" class="btn btn-success btn-lg btn-block">
                                    <% if(request.getParameter("smed") != null  && request.getParameter("smed").equals("convidados")){ %>
                                    <button OnClick="emitirComprovante(); return false;" class="btn btn-info btn-lg btn-block">Emitir Comprovante</button>
                                    <% } %>
                                    <hr>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <hr>
        <div class="col-md-12">
        	<hr>
	        <div class="container">
	            <div id="jornada" class="row">
	                <h1>Jornada da Educação <small> Educação e Sociedade: Relação Homem e Mundo</small></h1>
	                <hr>
	                <p style="text-align: justify;">É com muita satisfação que trazemos para a sociedade conquistense a Jornada da Educação de Vitória da Conquista, apresentando-a como fórum privilegiado para valorização do conhecimento e da cultura, proporcionando troca de experiências e reflexão.
	                    Com este evento, pretendemos reafirmar o compromisso do Município com a Educação, assumindo o nosso papel de polo educacional do Estado da Bahia. A Educação como o epicentro das diversas mudanças que almejamos e precisamos, sobretudo as que são inerentes à formação do homem na sua condição histórica de sujeito, é o que queremos realçar.
	                    A Jornada da Educação, desse modo, é um grande encontro, no qual, temas relevantes serão debatidos por profissionais de reconhecimento internacional, nacional e regional que, atentos às novas demandas para o discorrer da vida no século XXI, refletem sobre o homem, as sociedades e a humanidade de forma sistêmica e discursiva a partir do contexto atual, recorrendo a historicidade como recurso basilar.
	                    Com o tema Educação e sociedade: relação homem e mundo buscamos a ampliação do olhar frente a esta intrínseca relação, no entendimento de que , construídos pelo mundo, ao mesmo tempo, o construímos. A reflexão sobre as ações individuais e coletivas deverá nos remeter para o desvelamento de que o processo da hominização depende da progressiva ampliação da consciência, pela instalação de uma ética planetária, que tenha a liberdade e a felicidade da humanidade como horizonte de expectativa.
	                </h4>
	                </p>
	                <br />
	                <br />
	                <br />
	                <br />
	                <br />
	            </div>
	            <div class="row">
	                <h1>Colaboradores <small> Quem acredita na ideia!</small></h1>
	                <hr>
	                <div class="col-md-12">
	                    <a href="#" class="col-md-2 col-xs-6">
	                    <img alt="PMVC" src="./public/images/colaboradores/prefeitura.png" class="img-responsive">
	                    </a>
	                    <a href="#" class="col-md-2 col-xs-6">
	                    <img alt="TIViC" src="./public/images/colaboradores/tivic.png" class="img-responsive">
	                    </a>
	                    <a href="#" class="col-md-2 col-xs-6">
	                    <img alt="IFBA" src="./public/images/colaboradores/ifba.png" class="img-responsive">
	                    </a>
	                    <a href="#" class="col-md-2 col-xs-6">
	                    <img alt="UESB" src="./public/images/colaboradores/uesb.png" class="img-responsive">
	                    </a>
	                    <a href="#" class="col-md-2 col-xs-6">
	                    <img alt="UFBA" src="./public/images/colaboradores/ufba.png" width="50%" class="center-block img-responsive">
	                    </a>
	                    <a href="#" class="col-md-2 col-xs-6">
	                    <img alt="SESC" src="./public/images/colaboradores/sesc.png" class="img-responsive">
	                    </a>
	                </div>
	            </div>
	        </div>
	        <hr>
	        <div class="row">
	            <div id="footer" class="footer">
	                <div class="container text-center">
	                    <p>Todos os direitos reservados &copy; copyright - Jornada Pedagógica 2016</p>
	                </div>
	            </div>
	        </div>
	        <!-- Modal -->
			<div class="modal fade" id="acordoInscricaoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
			 	<div class="modal-dialog" role="document">
			    	<div class="modal-content">
			      		<div class="modal-header">
			        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			       			<h3 class="modal-title text-center" id="myModalLabel">Acordo de inscrição</h3>
			      		</div>
			      		<div class="modal-body">
							<strong class="text-center"><small>A inscrição só será validada pelo credenciamento e aceitação dos critérios abaixo</small></strong>
			        		<hr>
							<p>
							<strong>1</strong> – considerando a grande procura por vagas e a necessidade de atender
							prioritariamente aos professores efetivos e contratados e monitores de creches
							municipais, a inscrição para a Jornada da Educação 2016 só será validada com a
							apresentação do RG e do contra cheque no momento do credenciamento. Para os
							demais candidatos às vagas, é necessário, apenas, a apresentação do RG;
							<hr>
							<strong>2</strong>- o credenciamento será efetivado mediante doação de um livro de literatura, novo
							ou usado, em boas condições de uso, contendo uma dedicatória, devidamente
							datada e assinada, dirigida: À Criança, Ao Jovem ou Ao Amigo (a) Leitor (a),
							conforme a especificidade da obra doada. O livro não poderá ser do FNDE. As
							doações recebidas passarão a compor o acervo dos Espaços de Leitura das
							Unidades Municipais de Ensino;
							<hr>
							<strong>3</strong> – haverá no local do evento uma praça de alimentação com restaurante e fast
							food. A alimentação durante a Jornada será custeada pelo participante a depender
							da conveniência pessoal, com exceção do Buffet de confraternização, no último dia
							do evento;
							<hr>
							<strong>4</strong> – só será expedido o certificado ao cursista que obtiver frequência igual ou acima
							de 75% do total da carga horaria. Os casos especiais omissos neste termo,
							relacionados à frequência, serão tratados diretamente pela Comissão Central da
							Jornada.
							ATENÇÃO:
							O credenciamento será realizado na Associação Atlética Banco do Brasil – AABB,
							na Avenida Olívia Flores, no dia 10 de fevereiro de 2016, das 10 às 18 horas</p>
		      			</div>
			      		<div class="modal-footer">
		        			<button type="button" class="btn btn-success">Li e concordo</button>
		        			<button type="button" class="btn btn-primary" data-dismiss="modal">Li e não concordo</button>
		      			</div>
		    		</div>
 				</div>
			</div>
			<% if(request.getParameter("smed") != null  && request.getParameter("smed").equals("convidados")){ %>
            <!-- Modal -->
			<div class="modal fade" id="comprovanteInscricaoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" data-backdrop="static" data-keyboard="false">
			 	<div class="modal-dialog" role="document">
			    	<div class="modal-content">
			      		<div class="modal-header">
			        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			       			<h3 class="modal-title text-center" id="myModalLabel">Reimpressão de Comprovante</h3>
			      		</div>
						<form method="get" action="../request/jornada" target="_blank">
			      		<div class="modal-body">
								<div class="form-group">
                                	<label class="input-label">CPF:</label>
                              		<input type="text" name="comprovante" class="form-control input-lg" data-inputmask="'mask': '999-999-999-99'" placeholder="Informe seu CPF" maxlength="14">
                                </div>
		      			</div>
			      		<div class="modal-footer">
		        			<button type="submit" class="btn btn-success">Imprimir Comprovante</button>
		      			</div>
		      			
							</form>
		    		</div>
 				</div>
			</div>
            <% } %>
        </div>
        <!-- Latest compiled and minified JavaScript -->
        <script src="./public/js/jquery.min.js"></script>
        <script src="./public/js/bootstrap.min.js"></script>
        <script src="./public/js/plugins/jquery.validate.min.js" type="text/javascript"></script>
        <script src="./public/js/plugins/inputmask.js" type="text/javascript"></script>
        <script src="./public/js/global.js" type="text/javascript"></script>
    </body>
</html>
