<%@page import="com.tivic.manager.evt.Evento"%>
<%@page import="com.tivic.manager.evt.LocalServices"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.GregorianCalendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.tivic.manager.util.Util"%>
<%@page import="com.tivic.manager.evt.EventoServices"%>
<%@page import="com.tivic.manager.evt.EventoDAO"%>
<%@page import="com.tivic.manager.grl.PessoaServices"%>
<%@page import="com.tivic.manager.grl.DoencaServices"%>
<%@page import="com.tivic.manager.grl.EstadoDAO"%>
<%@page import="sol.dao.ResultSetMap"%>
<% 
    ResultSetMap rsmEstado = EstadoDAO.getAll(); 
    String[]     orgaosExp = {"CNT", "DIC", "IFP", "IPF", "MAE", "MEX", "MMA", "POF", "POM", "SES", "SSP"};

	GregorianCalendar dtLimiteIsencao = new GregorianCalendar();
	dtLimiteIsencao.set(new GregorianCalendar().get(Calendar.YEAR), GregorianCalendar.DECEMBER, 6, 23, 59, 59);
	
	
    int cdEvento = Integer.valueOf(request.getParameter("cdEvento"));
	Evento evento = EventoDAO.get(cdEvento);
    ResultSetMap rsmSubEventos = EventoServices.getAllSubEventosByEventoPrincipal(cdEvento);

    ResultSetMap rsmLocal = LocalServices.getAll();

    ArrayList<String> orderBy = new ArrayList<String>();
    orderBy.add("CD_EVENTO ASC");
    rsmSubEventos.orderBy(orderBy);

    rsmSubEventos.beforeFirst();
    System.out.println(evento);
    ResultSetMap rsmDoencas = DoencaServices.getAll();
	
	boolean inTime = new Date().getTime() >= evento.getDtInicial().getTime().getTime()
				  && new Date().getTime() <= evento.getDtFinal().getTime().getTime();
	
	
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
			<%
			if(inTime){	
			%>
			<div class="col-md-8 col-md-offset-2">
				<div class="notice notice-warning">
					<strong>Atenção:</strong> O período de inscrições para <strong><%= evento.getIdEvento() %> - <%= evento.getNmEvento() %></strong> está finalizado.
				</div>
				<hr style="margin:20px 0;"/>
				<button class="btn btn-default btn-back btn-block">Voltar para Página Incial</button>
			</div>
			<%				
			} else {
			%>
            <form id="nvInscricaoForm" method="post" class="col-md-8 col-md-offset-2" OnSubmit="return false;">
                <h3>Formulário de Inscrição</h3> <small><%= evento.getIdEvento() %> - <%= evento.getNmEvento() %></small>
                <hr>
                <div class="row" style="padding: 5px;">
                    <div class="form-group col-md-12">
                        <label>Nome do Candidato:</label>
                        <input type="text" class="form-control" name="nome" maxlength="50" onkeyup="this.value = this.value.toUpperCase();" onfocusout="this.value = this.value.toUpperCase();" required />
                    </div>
                    <div class="form-group col-md-4">
                        <label>Documento de Identidade:</label>
                        <input type="text" class="form-control" name="nr_rg" maxlength="20" required />
                    </div>
                    <div class="form-group col-md-4">
                        <label>Órgão Expedidor:</label>
                        <select class="form-control" name="orgao" required>
                            <option disabled selected>Selecione</option>
                            <% for(String o:orgaosExp){ %>
                            <option value="<%=o%>"><%=o%></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="form-group col-md-4">
                        <label>Unidade da Federação:</label>
                        <select class="form-control" name="unfederativa" required>
                            <option disabled selected>Selecione</option>
                            <% while(rsmEstado.next()){ %>
                            <option value="<%=rsmEstado.getString("CD_ESTADO")%>"><%=rsmEstado.getString("SG_ESTADO")%></option>
                            <% } %>
                        </select>
                    </div>
                    <div class="form-group col-md-4">
                        <label>N° do CPF:</label>	      			
                        <input type="text" class="form-control" name="nr_cpf" maxlength="14" data-inputmask="'mask': '999.999.999-99'" required />
                    </div>
                    <div class="form-group col-md-4">
                        <label>Sexo:</label>
                        <select class="form-control" name="sexo" required>
                            <option disabled selected>Selecione</option>
                            <option value="0">Masculino</option>
                            <option value="1">Feminino</option>
                        </select>
                    </div>
                    <div class="form-group col-md-4">
                        <label>Data de Nascimento:</label>	      			
                        <input type="text" class="form-control" maxlength="10" name="dt_nascimento" data-inputmask="'mask': '99/99/9999'" required />
                    </div>
                </div>
                <hr>
                <div class="row" style="padding: 5px;">
                	<div class="row">
                		<div class="col-md-12">
		                    <div class="form-group col-md-4">
		                        <label>CEP:</label>	      			
		                        <input type="text" class="form-control" name="nr_cep" data-inputmask="'mask': '99999-999'" required />
		                    </div>
		                    <div class="form-group col-md-6">
		                        <label>Endereço:</label>
		                        <input type="text" class="form-control" name="logradouro" maxlength="50" onkeyup="this.value = this.value.toUpperCase();" required />
		                    </div>
		                    <div class="form-group col-md-2">
		                        <label>N° Endereço:</label>	      			
		                        <input type="text" class="form-control" name="nr_endereco" maxlength="5" required />
		                    </div>
	                    </div>
                    </div>                    
                	<div class="row">
                		<div class="col-md-12">
		                    <div class="form-group col-md-4">
		                        <label>Bairro:</label>	      			
		                        <input type="text" class="form-control" name="bairro" onkeyup="this.value = this.value.toUpperCase();" maxlength="20" required />
		                    </div>
		                    <div class="form-group col-md-4">
		                        <label>Estado:</label>	      			
		                        <select class="form-control" name="estados" onChange="carregarCidades(this)" required>
		                            <option disabled selected>Selecione</option>
		                            <% 
		                             rsmEstado.beforeFirst();
		                             while(rsmEstado.next()){ 
		                            %>
		                            <option value="<%=rsmEstado.getInt("CD_ESTADO")%>"><%=rsmEstado.getString("SG_ESTADO")%></option>
		                            <% } %>
		                        </select>
		                    </div>
		                    <div class="form-group col-md-4">
		                        <label>Cidade:</label>	      			
		                        <select class="form-control" name="cidades" required>
		                            <option disabled selected>Selecione o estado</option>
		                        </select>
		                    </div>
	                    </div>
                    </div>                       
                	<div class="row">
                		<div class="col-md-12">
		                    <div class="form-group col-md-12">
		                        <label>Complemento:</label>	      			
		                        <input type="text" class="form-control" onkeyup="this.value = this.value.toUpperCase();" name="complemento" maxlength="50" />
		                    </div>
                    	</div>
                   	</div>
                </div>
                <hr>
                <div class="row" style="padding: 5px;">
                    <div class="form-group col-md-4">
                        <label>Telefone:</label>	      			
                        <input type="text" class="form-control" name="nr_telefone" maxlength="14" data-inputmask="'mask': '(99) 9999-9999'" required />
                    </div>
                    <div class="form-group col-md-4">
                        <label>Celular:</label>	      			
                        <input type="text" class="form-control" name="nr_celular" maxlength="15" data-inputmask="'mask': '(99) 99999-9999'" required />
                    </div>
                    <div class="form-group col-md-4">
                        <label>E-mail:</label>	      			
                        <input type="text" class="form-control" name="email" maxlength="50" onkeyup="this.value = this.value.toLowerCase()" required />
                    </div>
                </div>
                <hr>
                <div class="row" style="padding: 5px;">
                    <div class="form-group col-md-12">
                        <div class="row">
                            <div class="col-md-12">
                                <legend>Dados da seleção</legend>
                                <label>Selecione a opção de vaga:</label>	      	
                                <select class="form-control" name="vagas" required>
                                    <option disabled selected>Selecione</option>
                                    <% while(rsmSubEventos.next()){%>                                    
                            		<option data-localidade="<%=rsmSubEventos.getString("CD_LOCAL")%>" data-desc="<%=rsmSubEventos.getString("DS_EVENTO")%>"
                            		        value="<%=rsmSubEventos.getInt("CD_EVENTO")%>">
                            		        <%=rsmSubEventos.getString("NM_EVENTO")%> (<%=rsmSubEventos.getString("QT_VAGAS")%> Vagas) - [Valor da inscrição: R$ <%=Util.formatNumber(rsmSubEventos.getDouble("VL_INSCRICAO"))%>]
                            		        </option>
                            		<% } %>
                                </select>
                                <br />
                                <div class="alert alert-info hidden" style="font-size: 13px; padding: 10px;">
								  <strong class="text-sm">Descrição da função: </strong> 
								  <p class="desc-funcao">
								  	Selecione uma vaga para ver detalhes da função.
								  </p>
								</div>
                            </div>
<!--                             <div class="col-md-12 hidden"> -->
<!--                                 <legend>Localização de trabalho</legend> -->
<!--                                 <label>Selecione um local de trabalho: </label>	      	 -->
<!--                                 <select id="cbLocalidade" class="form-control" name="localidade" required> -->
<!--                                     <option disabled selected>Selecione</option> -->
<%--                                     <% while(rsmLocal.next()){%>           --%>
<%--                             		<option value="<%=rsmLocal.getInt("CD_LOCAL")%>"><%=rsmLocal.getString("NM_LOCAL")%></option> --%>
<%--                             		<% } %> --%>
<!--                                 </select> -->
<!--                             </div> -->
                        </div>
                        <hr>
                        <% if(dtLimiteIsencao.getTime().getTime() >= new Date().getTime()) { %>
                        <div class="row">
                            <div class="col-md-12">
                                <legend>Isenção - <small class="text-warning" style="font-size: 13px;">Prazo limite de solicitação até <%=new SimpleDateFormat("dd/MM/yyyy 'ás' HH:mm").format(dtLimiteIsencao.getTime())%></small></legend>
                                <div class="checkbox">
                                    <label for="isento">
                                    <input type="radio" name="isencao" id="isento" value="0" required checked />
                                    	Não solicitar isenção de Taxa de Inscrição
                                    </label>
                                </div>
                                <div class="checkbox">
                                    <label for="nIsento">
                                    <input type="radio" name="isencao" id="nIsento" value="1" required />
                                    	Solicitar isenção de Taxa de Inscrição <strong>(Obrigatório informar o NIS)</strong>
                                    </label>
                                </div>
                                <div class="col-md-12">
                                    <input type="text" name="nr_isencao" class="form-control input-sm hide" data-inputmask="'mask': '99999999999'" minlength="10" placeholder="N° de Inclusão Social" required/>
                                </div>
                            </div>
                        </div>
                        <hr>
                        <% } %>
                        <div class="row">
                            <div class="col-md-12">
                                <legend>Lactante</legend>
                                <div class="checkbox">
                                    <label for="ckLactante" style="padding-left: 20px;">
                                    <input type="checkbox" name="lactante" id="ckLactante"/>
                                    	Condição especial para lactante.
                                    </label>
                                </div>
                            </div>
                        </div>
                        <hr>
                        <div class="row">
                            <div class="col-md-12">
                                <legend>Deficiência</legend>
                                <% while(rsmDoencas.next()) { %>
                                <div class="checkbox">
                                    <label for="rdDeficiencia_<%=rsmDoencas.getInt("CD_DOENCA")%>" style="padding-left: 20px;">
	                                    <input type="checkbox" id="rdDeficiencia_<%=rsmDoencas.getInt("CD_DOENCA")%>" name="deficiencia" data-label="<%=rsmDoencas.getString("NM_DOENCA")%>" value="<%=rsmDoencas.getInt("CD_DOENCA")%>"/>
	                                   <%=rsmDoencas.getString("NM_DOENCA")%>
                                    </label>
                                </div>
                                <% } %>
                                <div class="row">
	                                <div class="form-group col-md-12 hidden">
	                        			<label>Observações:</label>	      			
			                        	<textarea class="form-control" placeholder="Informe-nos o que você precisa para que possa realizar a prova..." name="txtDeficienciaObservacao" rows="4" maxlength="200"></textarea>
			                        	 <small class="text-muted pull-right">Máximo de 200 caracteres</small>
			                    	</div>
                                </div>
                                
                            </div>
                        </div>                        
                    </div>
                </div>
                <hr>
                <div align="center" id="recaptcha"></div>
                <hr>
                <div style="padding: 5px;">
	                <input type="hidden" name="cdEventoPrincipal" value="<%=cdEvento%>"/>
	                <button class="btn btn-primary btn-send btn-block" data-target="#confirmacaoModal">Avançar</button>
	                <button class="btn btn-default btn-back btn-block">Voltar para Página Incial</button>
                </div>
            </form>
          	<div class="modal fade" id="confirmacaoModel" tabindex="-1" role="dialog" style="z-index: 99999;">
			    <div class="modal-dialog" role="document">
			        <div class="modal-content">
			            <div class="modal-header">
			                <h4 class="modal-title">Você confirma que os dados abaixo estão corretos?</h4>
			            </div>
			            <div class="modal-body">
			                <div class="row clearfix">
				                <div class="alert bg-warning col-md-12" style="font-size: 14px;color:#fff;padding:10px;">
									Ao confirmar os dados informados no formulário, o candidato confirma que é de sua total 
									responsabilidade a exatidão dos dados preenchidos, visto que algum erro poderá anular a inscrição.
								</div>
			                	<div class="clearfix">
			                		<hr />
			                	</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group row">
					                        <label class="col-md-12">Nome Completo:</label>	      			
					                        <p data-ref="nome" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">Documento de Identidade:</label>	      			
					                        <p data-ref="nr_rg" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">N° do CPF:</label>	      			
					                        <p data-ref="nr_cpf" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">Órgão Expedidor:</label>	      			
					                        <p data-ref="orgao" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">Unidade da Federação:</label>	      			
					                        <p data-ref="unfederativa" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">Sexo:</label>	      			
					                        <p data-ref="sexo" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">Data de Nascimento:</label>	      			
					                        <p data-ref="dt_nascimento" class="col-md-12"></p>
					                    </div>
									</div>
									<hr class="hidden-lg hidden-md hidden-sm" />
									<div class="col-md-6">
										<div class="form-group row">
					                        <label class="col-md-12">Endereço:</label>	      			
					                        <p data-ref="logradouro" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">N° Endereço:</label>	      			
					                        <p data-ref="nr_endereco" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">CEP:</label>	      			
					                        <p data-ref="nr_cep" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">Bairro:</label>	      			
					                        <p data-ref="bairro" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">Estado:</label>	      			
					                        <p data-ref="estados" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">Cidade:</label>	      			
					                        <p data-ref="cidades" class="col-md-12"></p>
					                    </div>
									</div>
								</div>			
								<hr class="hidden-xs" style="margin: 0px 0 10px !important;" />														
								<div class="row">
									<hr class="hidden-lg hidden-md hidden-sm" />
									<div class="col-md-6">
										<div class="form-group row">
					                        <label class="col-md-12">Telefone:</label>	      			
					                        <p data-ref="nr_telefone" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">Celular:</label>	      			
					                        <p data-ref="nr_celular" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">E-mail:</label>	      			
					                        <p data-ref="email" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">NIS:</label>	      			
					                        <p data-ref="nr_isencao" class="col-md-12"></p>
					                    </div>
				                    </div>
									<div class="col-md-6">
					                    <hr class="hidden-lg hidden-md hidden-sm" style="margin: 10px 0 !important" />
					                    <div class="form-group row">
					                        <label class="col-md-12">Vaga:</label>	      			
					                        <p data-ref="vagas" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">Lactante:</label>	      			
					                        <p data-ref="lactante" class="col-md-12"></p>
					                    </div>
					                    <div class="form-group row">
					                        <label class="col-md-12">Deficiências:</label>	      			
					                        <p data-ref="deficiencia" class="col-md-12"></p>
					                    </div>
									</div>
								</div>
			                </div>
			            </div>
			            <div class="modal-footer">
			            	<button type="button" class="btn btn-danger" data-dismiss="modal">Não, desejo voltar.</button>
			                <button type="button" class="btn btn-success btn-confirm">Confirmo, meus dados estão corretos.</button>
			            </div>
			        </div>
			        <!-- /.modal-content -->
			    </div>
			    <!-- /.modal-dialog -->
			</div>
			<!-- /.modal -->
			<% } %>
        </div>
        <script src="https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit" async defer></script>
        <script src="./public/assets/js/ajax.requests.js"></script>	
        <script src="./public/assets/js/jquery.selecao.min.js"></script>
        <script type="text/javascript">
	        function onloadCallback() {
		    	widgetId = grecaptcha.render('recaptcha', {
		          'sitekey' : '6Le-FxkTAAAAAOCiowH5cqfgLOyLV7cHYvBaDGbj'
		        });
		      };
        	
            $(function(){
            	
            	$(":input").keyup(function(e){
            		e.preventDefault();
            	}).inputmask();
            	
            });
        </script>
    </body>
</html>

