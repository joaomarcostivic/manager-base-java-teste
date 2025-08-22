<!DOCTYPE html>
<html lang="en">
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="content-type">
        <meta charset="utf-8">
        <title>Escola do Futuro</title>
        <meta content="Bootply" name="generator">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1" name="viewport">
        <meta content="Fixed header with independent scrolling left nav and right content." name="description">
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
        <link href="./css/bootstrap.min.css" rel="stylesheet">
        <link href="./css/animate.css" rel="stylesheet">
        <link href="./css/portal.css" rel="stylesheet">
<!--         <link href="./js/weather/css/weather-icons.min.css" rel="stylesheet"> -->
<!--         <link href="./js/weather/css/weather-icons-wind.min.css" rel="stylesheet"> -->
        <link href="/sol/cvjs/css/style.css" rel="stylesheet">
        <!--[if lt IE 9]>
        <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
        <link rel="icon" href="./favicon.png" type="image/x-icon" />
    </head>
    <body>
        <div class="bg-opacity"></div>
        <div class="content">
            <div class="sidebar col-md-3 col-sm-3 col-lg-2 col-xs-4 hidden-xs">
                <div class="col-md-12">
                    <img class="logo img-responsive" src="./edf/imagens/logo-edf-branco.png">
                </div>
                <div class="sidebar-nav col-md-12">
                    <ul class="nav nav-stacked">
                        <li class="nav-header">
                            <a href="#" data-toggle="collapse" data-target="#menu1" data-id="principal"> <i class="fa fa-home"></i> Início </a>
                        </li>
                        <li class="nav-header">
                            <a href="#" data-toggle="collapse" data-target="#menu2" data-id="smed"> <i class="fa fa-university"></i> Para a SMED </a>
                        </li>
                        <li class="nav-header">
                            <a href="#" data-toggle="collapse" data-target="#menu3" data-id="sobre"> <i class="fa fa-info-circle"></i> Sobre </a>
                        </li>
                        <li class="nav-header">
                            <a href="#" data-toggle="collapse" data-target="#menu5" data-id="fale-conosco"> <i class="fa fa-envelope"></i> Contato </a>
                        </li>
                    </ul>
                </div>
                <div class="sidebar-bottom" style="z-index: 0 !important; position: absolute;  bottom: 2px;">
                    <div class="col-md-8 col-xs-9">
                        <a href="http://www.tivic.com.br">
                        </a>
                    </div>
                </div>
            </div>
            <div class="col-md-9 col-sm-9 col-lg-10 col-xs-12 content-page">
                <div id="principal" class="active">
                    <div class="row">
                        <div class="row cover">
                            <h1>
                                <span id="NM_INSTITUICAO">SECRETARIA MUNICIPAL</span>
                                <br />
                                <small id="NM_LOCALIZACAO"></small>
                            </h1>
                            <span id="NR_VERSAO" class="versao">ver: </span>
                            <div class="cover-overlay"></div>
                        </div>
                    </div>
                    <div class="gmaps map-hidden effected">
                        <div class="search-address-form col-lg-4 well well-sm">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-map-marker"></i></span>
                                <input class="form-control input-sm" placeholder="Informe seu endereï¿½o" type="text" name="endereco">
                                <a class="info input-group-btn">
                                <button class="btn btn-info btn-sm" type="button" onClick="google.maps.event.trigger(new google.maps.places.Autocomplete(document.getElementsByName('endereco')[0]), 'place_changed');">
                                <i class="fa fa-search"></i> Buscar
                                </button>
                                <button class="btn btn-danger btn-sm btn-sair-mapa" type="button">
                                <i class="fa fa-sign-out"></i> Sair
                                </button>
                                </a>
                            </div>
                        </div>
                        <div id="mapViewport">
                        </div>
                    </div>
                    <div class="row">
                        <hr class="hidden-lg hidden-md hidden-sm" />
                        <div class="col-xs-12 col-sm-6 col-md-7 col-lg-4">
                            <h3>
                                <small><i class="fa fa-bars text-muted"></i></small> Acesso Rï¿½pido
                            </h3>
                            <div class="info-box effected" data-toggle="tooltip" title="Módulo da Secretaria" data-module="03ESC">
                                <span class="bg-aqua info-box-icon"><i class="fa fa-home"></i></span>
                                <div class="info-box-content">
                                    <span class="info-box-text">Módulo da Escola</span>
                                    <p class="text-muted info-box-description">Módulo de Gestão Interna de uma instituição</p>
                                </div>
                                <!-- /.info-box-content -->
                            </div>
                            <div class="info-box effected" data-toggle="tooltip" title="Sistema EDF Angular" data-url="./v2/" >
	                            <span class="bg-orange info-box-icon"><i class="fa fa-html5"></i></span>
	                            <div class="info-box-content">
	                                <span class="info-box-text">EDF v2</span>
	                                <p class="text-muted info-box-description">Módulo do Sistema EDF com novo layout</p>
	                            </div>
	                            <!-- /.info-box-content -->
	                        </div>
                            <!-- /.info-box -->
                            <div class="info-box effected" data-toggle="tooltip" title="Módulo do Professor" data-url="./portal_professor">
                                <span class="bg-green info-box-icon"><i class="fa fa-leanpub"></i></span>
                                <div class="info-box-content">
                                    <span class="info-box-text">Módulo do Professor</span>
                                    <p class="text-muted info-box-description">Módulo de Gestão Escolar para o professor</p>
                                </div>
                                <!-- /.info-box-content -->
                            </div>
                            <!-- /.info-box -->
                            <div class="info-box effected" data-toggle="tooltip" title="Módulo do Aluno" data-url="./portal_aluno">
                                <span class="bg-yellow info-box-icon"><i class="fa fa-graduation-cap"></i></span> <!-- bg-yellow -->
                                <div class="info-box-content">
                                    <span class="info-box-text">Módulo do Aluno</span>
                                    <p class="text-muted info-box-description">Módulo para o discente</p>
                                </div>
                                <!-- /.info-box-content -->
                            </div>
                            <!-- /.info-box -->
                            <div class="info-box effected" data-toggle="tooltip" title="Módulo do Responsável" data-module="">
                                <span class="bg-gray-active info-box-icon"><i class="fa fa-male"></i></span> <!-- bg-teal -->
                                <div class="info-box-content">
                                    <span class="info-box-text">Módulo do Responsável</span>
                                    <p class="text-muted info-box-description">Módulo para o pai/responsável do discente</p>
                                </div>
                                <!-- /.info-box-content -->
                            </div>
                            <div class="info-box effected" data-toggle="tooltip" title="Pré-matrícula" data-url="./pre_matricula">
                                <span class="bg-red info-box-icon"><i class="fa fa-user"></i></span> <!-- bg-teal -->
                                <div class="info-box-content">
                                    <span class="info-box-text">Pré-matrícula</span>
                                    <p class="text-muted info-box-description">Módulo para a realização de uma pré-matrícula</p>
                                </div>
                                <!-- /.info-box-content -->
                            </div>
                        </div>
                        <div class="col-xs-12 col-sm-6 col-md-5 col-lg-3">
                            <hr class="hidden-lg hidden-md hidden-sm" />
<!--                             <h3> -->
<!--                                 <small><i class="fa fa-cloud text-muted"></i></small> Clima -->
<!--                             </h3> -->
<!--                             <div class="box box-weather-widget widget-weather-2"> -->
<!--                                 <div class="widget-weather-header bg-red"> -->
<!--                                     <div class="widget-weather-icon"> -->
<!--                                         <i class="fa fa-moon-o" aria-hidden="true"></i> -->
<!--                                     </div> -->
<!--                                     <h3 class="widget-weather-title"><span class="forecast">0°</span> <small class="weather-unit">C</small></h3> -->
<!--                                     <h5 class="widget-weather-desc">Aguardando...</h5> -->
<!--                                     <h5 class="widget-weather-location">Aguardando...</h5> -->
<!--                                     <button class="widget-weather-config btn btn-link" data-toggle="popover" title="Mudar localização"> -->
<!--                                     <i class="fa fa-cog"></i> -->
<!--                                     </button> -->
<!--                                 </div> -->
<!--                                 <div class="box-footer no-padding"> -->
<!--                                     <ul class="nav nav-stacked"> -->
<!--                                         <li> -->
<!--                                             <a href="#"> -->
<!--                                             Hoje -->
<!--                                             <span class="pull-right"> -->
<!--                                             máx <span class="badge bg-yellow weather-today-max">0</span>  -->
<!--                                             min <span class="badge bg-aqua weather-today-min">0</span>  -->
<!--                                             </span> -->
<!--                                             </a> -->
<!--                                         </li> -->
<!--                                         <li> -->
<!--                                             <a href="#"> -->
<!--                                             Amanhã -->
<!--                                             <span class="pull-right"> -->
<!--                                             máx <span class="badge bg-yellow weather-tomorrow-max">0</span>  -->
<!--                                             min <span class="badge bg-aqua weather-tomorrow-min">0</span>  -->
<!--                                             </span> -->
<!--                                             </a> -->
<!--                                         </li> -->
<!--                                         <li><small class="copy pull-right">Dados do <strong class="text-danger">Yahoo</strong> desenvolvido por <strong class="text-info">TIViC</strong></small> -->
<!--                                         </li> -->
<!--                                     </ul> -->
<!--                                 </div> -->
<!--                             </div> -->
<!--                             <hr class="hidden-lg hidden-md hidden-sm" /> -->
                            <h3>
                                <small><i class="fa fa-map-marker text-muted"></i></small> Buscar Instituições
                            </h3>
                            <img src="./edf/imagens/ache.jpg" class="img-thumbnail img-responsive img-gmaps" />
                        </div>
<!--                         <div class="col-xs-12 col-sm-12 col-md-7 col-lg-5"> -->
<!--                             <hr class="hidden-lg hidden-md hidden-sm" /> -->
<!--                             <h3> -->
<!--                                 <small><i class="fa fa-newspaper-o text-muted"></i></small> Últimas Notícias -->
<!--                             </h3> -->
<!--                             <div class="well well-sm clearfix"> -->
<!--                                 <div id="notice-list" class="col-md-12 col-lg-12 col-xs-12 notice-list"> -->
<!--                                     <ul class="list-unstyled"> -->
<!--                                     </ul> -->
<!--                                 </div> -->
<!--                                 <a href="http://www.pmvc.ba.gov.br/categoria/noticias/" target="_blank" class="btn btn-sm btn-link pull-right">VER MAIS</a> -->
<!--                             </div> -->
<!--                         </div> -->
                        <div class="col-xs-12 col-sm-12 col-md-7 col-lg-5">
                            <hr class="hidden-lg hidden-md hidden-sm" />
                            <h3>
                                <small><i class="fa fa-newspaper-o text-muted"></i></small> Documentos oficiais
                            </h3>
                            <div class="well well-sm clearfix">
                                <div class="col-md-12 col-lg-12 col-xs-12 document-list">
                                    <ul class="list-unstyled">
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="smed">
                    <div class="col-xs-12 col-sm-6 col-md-5 col-lg-4">
                        <h3>
                            <small><i class="fa fa-bars text-muted"></i></small> Para a SMED
                        </h3>
                        <div class="info-box effected" data-toggle="tooltip" title="Mï¿½dulo Administrativo" data-module="01SEC">
                            <span class="bg-red info-box-icon"><i class="fa fa-briefcase"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">Administrativo</span>
                                <p class="text-muted info-box-description">Módulo Administrativo</p>
                                <span class="text hidden"> O módulo de administrativo é o responsável por cuidar das funções voltadas a organização das escolas, desde a infraestrutura, organização de cursos, disciplinas e matrizes curriculares, até o cadastro e organização das turmas, a matrícula de alunos e enturmação de professores. </span>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        <div class="info-box effected" data-toggle="tooltip" title="Recursos Humanos" data-module="09SRH">
                            <span class="bg-aqua info-box-icon"><i class="fa fa-code-fork"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">RH</span>
                                <p class="text-muted info-box-description">Módulo de Recursos Humanos</p>
                                <span class="text hidden"> O módulo de Recursos Humanos é responsável por gerenciar as informações gerais dos funcionários da Secretária Municipal de Educação. A lotação dos mesmos, folha de pagamento, férias, desligamento, entre outros. O módulo também controla bens patrimoniais e emite relatórios que auxiliam o gestor do setor na tomada de decisão. </span>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        <!-- /.info-box -->
                        <div class="info-box effected" data-toggle="tooltip" title="Processo Administrativo Eletrï¿½nico" data-module="10PAE">
                            <span class="bg-teal info-box-icon"><i class="fa fa-refresh"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">PAE</span>
                                <p class="text-muted info-box-description">Módulo de Processo Administrativo Eletrônico</p>
                                <span class="text hidden"> O módulo PAE (Processo Administrativo Eletrônico) gerencia a emissão e movimentação de documentos oficiais e processos administrativos que tramitam entre os setores da secretaria de educação e as escolas. </span>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        <!-- /.info-box -->
<!--                         <div class="info-box effected" data-toggle="tooltip" title="Georeferenciamento" data-url="./edf/geo.jsp"> -->
<!--                             <span class="bg-yellow info-box-icon"><i class="fa fa-street-view"></i></span> -->
<!--                             <div class="info-box-content"> -->
<!--                                 <span class="info-box-text">GEO</span> -->
<!--                                 <p class="text-muted info-box-description">Módulo de Georeferenciamento</p> -->
<!--                                 <span class="text hidden">Módulo que mostra a informação geográfica e a localização de cada instituição de ensino municipal através do mapa.</span> -->
<!--                             </div> -->
<!--                             /.info-box-content -->
<!--                         </div> -->
                        <!-- /.info-box -->
                        <div class="info-box effected" data-toggle="tooltip" title="Alimentaï¿½ï¿½o Escolar" data-module="06CAE">
                            <span class="bg-green info-box-icon"><i class="fa fa-cutlery"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">CAE</span>
                                <p class="text-muted info-box-description">Módulo de Controle de Alimentação Escolar</p>
                                <span class="text hidden"> O módulo de alimentação escolar é responsável pelo controle de todos os alimentos movimentados pela rede municipal, desde os produtos armazenados no almoxarifado central, no estoque individual das escolas e até a entrega dos fornecedores direto a escola. Todos os controles de pedido e envio de alimentos tem por base os cardápios elaborados pelos nutricionistas que seguem as recomendações do programa PNAE - FNDE. </span>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        <div class="info-box effected" data-toggle="tooltip" title="Pedagógico" data-url="./portal_pedagogico" style="cursor: pointer;">
                            <span class="bg-maroon info-box-icon"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">PED</span>
                                <p class="text-muted info-box-description">Módulo de Gerenciamento Pedagógico</p>
                                <span class="text hidden">O módulo pedagógico tem a função de auxiliar a coordenação pedagógica na articulação entre escola, professor e as práticas pedagógicas, melhorando o aprendizado do aluno. Através desse módulo é possível acompanhar as atividades diárias dos professores e fornecer ferramentas para que estes trabalhem as propostas curriculares do município, de acordo a realidade de cada escola da rede.</span>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        <div class="info-box effected" data-toggle="tooltip" title="Transporte Escolar" data-url="./portal_transporte">
                            <span class="bg-aqua info-box-icon"><i class="fa fa-bus"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">MTE</span>
                                <p class="text-muted info-box-description">Módulo de Transporte Escolar</p>
                                <span class="text hidden"> O Módulo de Transporte Escolar visa gerenciar informações sobre a frota de veículos, os contratos, as vistorias, as rotas e os motoristas. Podendo ser observada a frequência e a folha de ponto dos motoristas em tempo real através da integração do módulo com o módulo da escola. </span>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        <div class="info-box effected" data-toggle="tooltip" title="Apoio ï¿½ Decisï¿½o (BI)" data-module="13BI">
                            <span class="bg-blue info-box-icon"><i class="fa fa-pie-chart"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">BI</span>
                                <p class="text-muted info-box-description">Módulo de Apoio à decisão e Estatística</p>
                                <span class="text hidden">O Módulo de Apoio a Tomada de Decisão (BI- Business Intelligence) é uma ferramenta que auxilia o gestor através da analise de dados do sistema como um todo. Com a geração automática de gráficos, o BI consegue transmitir de forma rápida e ilustrativa a realidade da instituição e da rede a partir de dados coletados em todos os outros módulos.</span>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                    </div>
                    
                    <div class="hidden-sm hidden-xs col-md-6 col-lg-8 module-description hide">
       					<div class="affix">
	                    	<div class="module-icon text-center">
	                    		<i class="fa fa-pie-chart fw" style="font-size: 256px;"></i></span>
	                    		<h1>Nome módulo</h1>
	                    		<p class="text col-lg-10 col-lg-offset-1"></p>
	                    	</div>
                    	</div>
                    </div>
                </div>
                <div id="sobre">
                	<div class="col-xs-12 col-sm-6 col-md-5 col-lg-4">
                        <h3>
                            <small><i class="fa fa-bars text-muted"></i></small> Sobre a secretaria
                        </h3>
                        <div class="info-box effected" data-toggle="tooltip" title="Aspirações">
                            <span class="bg-red info-box-icon"><i class="fa fa-puzzle-piece"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text-sobre">Aspirações</span>
                                <span class="text hidden"> A aspiração a aprendizagem consiste na valorização do sucesso a médio ou longo prazo, o que exige determinadas competências. Trata-se de uma disposição geral para aprendizagens complexas e prolongadas, que pressupõem um alto nível de auto - confiança e de motivação para a realização. O nível de aspiração e por isso mais uma variável motivacional indispensável para uma boa aprendizagem. </span>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        
                        <div class="info-box effected" data-toggle="tooltip" title="Aspirações">
                            <span class="bg-yellow info-box-icon"><i class="fa fa-arrows-alt"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text-sobre">Diretrizes pedagógicas</span>
                                <span class="text hidden"> O que são diretrizes pedagógicas? Diretrizes pedagógicas é nada mais do que o currículo da escola, a forma como a instituição trabalha, qual didática é usada, materiais didáticos, metas de aprendizagem. Por que é importante definir as diretrizes pedagógicas da escola? Porque é a partir dessas diretrizes que os professores terão uma base para elaborar seus projetos e planos de aulas, seguindo o currículo da escola para alcançar as metas de aprendizagem.. </span>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        
                        <div class="info-box effected" data-toggle="tooltip" title="Aspirações">
                            <span class="bg-green info-box-icon"><i class="fa fa-users"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text-sobre">Gestores e equipe de apoio</span>
                                <span class="text hidden">Um bom gestor sabe que oferecer maior autonomia ao seu time ajuda a fazer com que os profissionais trabalhem com mais liberdade e, consequentemente, mais engajamento. Ao mesmo tempo, ele sabe delegar tarefas de forma a otimizar os processos, distribuindo as responsabilidades de acordo com a compatibilidade que as tarefas têm com o perfil de cada um.</span>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        
                        <div class="info-box effected" data-toggle="tooltip" title="Aspirações">
                            <span class="bg-purple info-box-icon"><i class="fa fa-sitemap"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text-sobre">Estrutura organizacional</span>
                                <span class="text hidden"> Estrutura organizacional é um conceito da área de administração e gestão de empresas. Trata da forma como a empresa é organizada em torno da divisão de atividades e recursos com fins de cumprir os objetivos da companhia.. </span>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                    </div>
                    
                    <div class="hidden-sm hidden-xs col-md-6 col-lg-8 module-description hide">
       					<div class="affix">
	                    	<div class="module-icon text-center">
	                    		<i class="fa fa-pie-chart fw" style="font-size: 256px;"></i></span>
	                    		<h1>Nome módulo</h1>
	                    		<p class="text col-lg-10 col-lg-offset-1"></p>
	                    	</div>
                    	</div>
                    </div>
                </div>
                <div id="fale-conosco">
                    <div class="row">
                        <hr />
                        <div class="col-sm-6">
                            <div class="panel">
                                <div class="panel-body p-t-10">
                                    <div class="media-main">
                                        <a class="pull-left" href="#">
                                        <img class="thumb-lg img-circle bx-s" src="http://graph.facebook.com/TivicOficial/picture?type=large" alt="">
                                        </a>
                                        <div class="info">
                                            <h4>TIViC - Tecnologia, Informação e Inovação</h4>
                                            <p class="text-muted">Suporte Técnico - Escola do Futuro</p>
                                            <p><i class="fa fa-phone"></i> <a href="tel:557734298460">(77) 3429-8460</a></p>
                                        </div>
                                    </div>
                                    <div class="clearfix"></div>
                                    <hr>
                                    <ul class="social-links list-inline p-b-10">
                                        <li>
                                            <a title="" data-placement="top" data-toggle="tooltip" class="tooltips" href="#" data-original-title="Facebook"><i class="fa fa-facebook"></i></a>
                                        </li>
                                        <li>
                                            <a title="" data-placement="top" data-toggle="tooltip" class="tooltips" href="#" data-original-title="Skype"><i class="fa fa-skype"></i></a>
                                        </li>
                                        <li>
                                            <a title="" data-placement="top" data-toggle="tooltip" class="tooltips" href="#" data-original-title="Message"><i class="fa fa-envelope-o"></i></a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="panel">
                                <div class="panel-body p-t-10">
                                    <div class="media-main">
                                        <a class="pull-left" href="#">
                                        <img class="thumb-lg img-circle bx-s" src="./imagens/brasao_pmvc.jpg" alt="">
                                        </a>
                                        <div class="info">
                                            <h4>SMED - Secretaria Municipal de Educação</h4>
                                            <p class="text-muted">Informações - Escola do Futuro</p>
                                            <p><i class="fa fa-phone"></i> <a href="tel:7734297750">(77) 3429-7750</a></p>
                                        </div>
                                    </div>
                                    <div class="clearfix"></div>
                                    <hr>
                                    <ul class="social-links list-inline p-b-10">
                                        <li>
                                            <a title="" data-placement="top" data-toggle="tooltip" class="tooltips" href="#" data-original-title="Facebook"><i class="fa fa-facebook"></i></a>
                                        </li>
                                        <li>
                                            <a title="" data-placement="top" data-toggle="tooltip" class="tooltips" href="#" data-original-title="Skype"><i class="fa fa-skype"></i></a>
                                        </li>
                                        <li>
                                            <a title="" data-placement="top" data-toggle="tooltip" class="tooltips" href="#" data-original-title="Message"><i class="fa fa-envelope-o"></i></a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="weather-form" style="display: none;">
            <div class="input-group">
                <input type="text" class="form-control input-cidades typeahead" placeholder="Infome sua cidade" />
                <a class="info input-group-btn">
                <button class="btn btn-info" type="button">
                <i class="fa fa-check"></i>
                </button>
                </a>
            </div>
        </div>
        <script src="./js/jquery-1.11.3.min.js"></script>
        <script src="./js/bootstrap.min.js"></script>
        <script src="./js/quark/quark.js"></script>
<!--         <script src="./js/weather/lang/pt-br.js"></script> -->
<!--         <script src="./js/weather/weather_api.js"></script> -->
<!--         <script src="./js/weather/weather_icon.js"></script> -->
        <script src="./js/jquery.xdomainajax.js"></script>
        <script src="./js/bootstrap-typeahead.min.js"></script>
        <script type="text/javascript" src="/sol/cvjs/CidadeVirtual.js"></script>
        <script type="text/javascript" src="./js/shadowbox.js"></script>
        <script type="text/javascript" src="https://rss2json.com/gfapi.js"></script>
        <script>
        
        	google.load("feeds", "1");
        
            $(window).load(function() {
            
                var map        = null;
                var protocol   = window.location.protocol;
                var host       = window.location.host;
                var pathname   = window.location.pathname.split('/');
                var hasFlash   = navigator.mimeTypes["application/x-shockwave-flash"];
                var cidades    = [];
                var nmRoot     = protocol + '//' + host + '/' + pathname[1] + '/';
                
                var backgrounds = [
                    "https://i.imgur.com/reYiDrK.jpg",
                    "https://i.imgur.com/LEAlzQK.jpg",
                    "https://i.imgur.com/Dg6xG2x.jpg",
                    "https://i.imgur.com/FYbdFQl.jpg",
                    "https://i.imgur.com/fgjYvfK.jpg",
                    "https://i.imgur.com/LeKPJyp.jpg"
	            ];
	
	            var rand = Math.floor(Math.random()*5)+1;
	
	            $("#principal .cover").css('background-image', 'url('+backgrounds[rand]+')');
	            $("#principal .cover").css('background-size', 'cover');

                Quark.ajaxCaller('SEG0002-0001', {
                	callback: function(response){
                		response = response == null ? "0.0.00" : response;
                		$("title").html("Escola do Futuro - v" + response);
                		$(".versao").append(response);
                	}
                });
                                
                Quark.ajaxCaller('GRL0001-0001', {
                    args: [{
                        value: 'CD_INSTITUICAO_SECRETARIA_MUNICIPAL',
                        type: 'String'
                    },
                    {
                    	value: 0,
                        type: 'int'
                    }],
                    callback: function(response) {
                    	Quark.ajaxCaller('ACD0004-0009', {
                            args: [{
                            	value: response,
                                type: 'int'
                            }],
                            callback: function(response) {
								if(response){
									var empresa = response.lines[0];
									$("#NM_INSTITUICAO").text(empresa.NM_EMPRESA);
									$("#NM_LOCALIZACAO").text(empresa.NM_CIDADE + ' - ' + empresa.NM_ESTADO);
								}
                    		}
							
                        });
            		}
                });
            
                CidadeVirtual.init('AIzaSyDNLrUr1SpDDRZBfHOhR4m9ELvVWa27ly8', false, ['visualization', 'weather', 'places', 'geometry']);
            
                map = CidadeVirtual.createMap({
                    viewport: 'mapViewport',
                    lat: -14.851,
                    lng: -40.8485,
                    zoom: 14,
                    onCreate: function(map) {
                        var autocomplete = new google.maps.places.Autocomplete(document.getElementsByName('endereco')[0], {
                            types: ["geocode"]
                        });
            
                        Quark.ajaxCaller('ACD0004-0008', {
                            args: [{
                                value: 1,
                                type: 'int'
                            }],
                            callback: function(response) {
                                if (response && response.lines) {
            
                                    var layer = map.addLayer({
                                        id: 'TP_LOCALIZACAO_POSICAO',
                                        label: 'Localização'
                                    });
            
                                    $.each(response.lines, function(index, item) {
                                    	
                                        layer.addRef({
                                            title: item.NM_PESSOA,
                                            label: item.NM_PESSOA,
                                            type: CidadeVirtual.REF_POINT,
                                            lat: item.VL_LATITUDE,
                                            lng: item.VL_LONGITUDE,
                                            pin: CidadeVirtual.Pins.SQUARE_PIN,
                                            pinColor: '#da3c78',
                                            scale: 1,
                                            content: "Instituição: <b>" + item.NM_PESSOA + "</b><br />" + 
                                            		 "Gestor(a): <b>" + item.NM_GESTOR + "</b><br />" +
                                            		 "Vice-Diretor(a): <b>" + item.NM_VICE_DIRETOR + "</b><br />" +
                                            		 "Secretário(a): <b>" + item.NM_SECRETARIO + "</b><br />" +
                                            		 "Localização: <b>" + item.NM_TP_LOCALIZACAO + "</b><br />" +
                                            		 "INEP: <b>" + item.NR_INEP + "</b><br />"+
                                            		 "Número de salas: <b>" + item.NR_SALAS + "</b><br />"+
                                   					 "Número de alunos: <b>" + item.NR_ALUNOS + "</b><br />" +
                                  					 "Etapas oferecidas: <b>" + item.CONJUNTO_ETAPAS + "</b><br />"
                                   
                                   
                                   
                                        });
                                    });
                                }
                            }
                        });
            
                        autocomplete.bindTo('bounds', map._gmap);
                        
            
                        google.maps.event.addListenerOnce(map._gmap, 'idle', function() {
                            google.maps.event.trigger(map._gmap, 'resize');
                        });
            
                        google.maps.event.addListener(autocomplete, 'place_changed', function() {
                            var place = autocomplete.getPlace();
            
                            if (place.geometry) {
                                if (place.geometry.viewport) {
                                    map._gmap.fitBounds(place.geometry.viewport);
                                } else {
                                    map._gmap.setCenter(place.geometry.location);
                                    map._gmap.setZoom(16);
                                }
                            }
                        });
            
                        $("input").on('focusin', function(ev) {
                            var $element = $(ev.target);
                            $(document).on('keypress', function(e) {
                                if (e.which == 13) {
                                    var firstResult = $(".pac-container .pac-item:first").text();
                                    var geocoder = new google.maps.Geocoder();
            
                                    geocoder.geocode({
                                        "address": firstResult
                                    }, function(results, status) {
                                        if (status == google.maps.GeocoderStatus.OK) {
                                            if (results[0].geometry.viewport) {
                                                map._gmap.fitBounds(results[0].geometry.viewport);
                                            } else {
                                                map._gmap.setCenter(results[0].geometry.location);
                                                map._gmap.setZoom(16);
                                            }
            
                                        }
                                        
                                        $element.val(results[0].formatted_address);
                                    });
                                }
                            });
                        });
                    }
                });
            
                $(".btn-sair-mapa, .img-gmaps").bind('click', function() {
                    $(".gmaps").toggleClass('map-hidden');
                });
                
                var idHash = $(location).attr('hash');
                if($(idHash)){
                	if($(idHash).length > 0){
                        $(".content-page > div").fadeOut('fast').removeClass("active").end().find(idHash).addClass("active").delay(200).fadeIn(200);
                	}
                }
                
                $(".content-page > div.active").fadeIn('fast');
                $(".nav-stacked li a").on('click', function() {
                	var id = $(this).data('id');
            
                	if($(".gmaps").is(':visible')){
                		$(".gmaps").addClass('map-hidden');
                	}
                	if($("#" + id).length > 0){
                        $(".content-page > div").fadeOut('fast').removeClass("active").end().find("#" + id).addClass("active").delay(200).fadeIn(200, function(){
                        	window.location.hash = id
                        });
                	}
                });
            
//                 var weather = function(cidade) {
//                     window.localStorage.setItem('weather_city', cidade);
            
//                     $.weatherApi({
//                         displayname: window.localStorage.getItem('weather_city'),
//                         location: window.localStorage.getItem('weather_city'),
//                         unit: 'c',
//                         lang: 'pt-BR',
//                         success: function(response) {
                        	
//                         	console.log('response = ', response);
                        	
//                             var weather = response.forecast;
                                        
//                             $(".widget-weather-title .forecast").html(response.temp + "°");
//                             $(".widget-weather-title .weather-units").html(response.unit);
//                             $(".widget-weather-icon").html(setWeatherIcon(weather[0].code))
//                             $(".widget-weather-desc").html(translate(weather[0].code));
//                             $(".widget-weather-location").html(response.displayname);
                            
//                             $(".weather-today-max").html(weather[0].high + '° ' + response.unit);
//                             $(".weather-today-min").html(weather[0].low + '° ' + response.unit);
//                             $(".weather-tomorrow-max").html(weather[1].high + '° ' + response.unit);
//                             $(".weather-tomorrow-min").html(weather[1].low + '° ' + response.unit);
            
//                             $(".widget-weather-header").css({
//                                 background: 'url( ' + response.image + ' ) center center',
//                                 'background-size': 'cover'
//                             });
            
//                             $("box-weather-widget").stop().animate({
//                                 'opacity': 1,
//                             }, 'fast');
            
//                         }
//                     });
//                 }
            
                $('body').on('click', ".nav-header a", function() {
                    var $el = $(this);
            
                    if ($el.hasClass('collapsed')) {
                        $('i', $el).eq(1).removeClass("glyphicon-chevron-right").addClass("glyphicon-chevron-down");
                    } else {
                        $('i', $el).eq(1).removeClass("glyphicon-chevron-down").addClass("glyphicon-chevron-right");
                    }
                });
            
                $("[data-module]").css({
                    cursor: 'pointer'
                }).bind('click', function() {
                    $module = $(this).data('module');
            		
                    if (!hasFlash && (/chrome/i.test( navigator.userAgent ) == false)) {
                        Quark.alert($('body'), {
                            message: "Seu navegador nï¿½o tem suporte a este mï¿½dulo.",
                            icon: 'fa fa-times-circle-o text-danger'
                        });
                        return false;
                    }
            
                    if (nmRoot && $module !== '') {
                        location.href = nmRoot + 'flex/' + (pathname[0].indexOf('edf') ? 'EDF.jsp?m=' : 'Manager.jsp=m=') + $module;
                    }
                });
                
                $("[data-url]").css({
                    cursor: 'pointer'
                }).bind('click', function() {
                    $url = $(this).data('url');
            		
                    if (nmRoot && $url !== '') {
                    	location.href = $url;
                    }
                });
            
                $("[data-tooltip='true']").tooltip({
                    placement: 'left',
                    container: 'body'
                });
            
//                 $('.widget-weather-config').popover({
//                     html: true,
//                     placement: 'bottom',
//                     content: $('.weather-form').clone().fadeIn(),
//                     title: 'body'
//                 }).on('click', function() {
//                     $(".typeahead").val("");
//                     $(".typeahead").focus();
//                 });
            
//                 if (window.localStorage.getItem('weather_city') === null) {
            
//                     var success = function(position) {
//                         var GEOCODING = 'https://maps.googleapis.com/maps/api/geocode/json?latlng=' + position.coords.latitude + '%2C' + position.coords.longitude + '&language=en';
//                         $.getJSON(GEOCODING).done(function(location) {
// 							if(weather(location['results'][0]))
// 								weather(location['results'][0]['address_components'][3]['long_name']);
//                         });
//                     }
            
//                     if (/chrome/.test(navigator.userAgent.toLowerCase())) {
//                         jQuery.post("https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyBKj6naZR3KAqzM09-6souhpv6xU-hsvA0", function(data) {
//                             success({
//                                 coords: {
//                                     latitude: data.location.lat,
//                                     longitude: data.location.lng
//                                 }
//                             });
//                         });
//                     } else {
//                         navigator.geolocation.getCurrentPosition(success, null);
//                     }
            
//                 } else {
//                     weather(window.localStorage.getItem('weather_city'));
//                 }
            
                $('body').on('click', function(e) {
                    $('[data-toggle="popover"]').each(function() {
                        if (!$(this).is(e.target) && !$(this).is('.dropdown') && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0 && !$(".bg-opacity").is(":visible")) {
                            $(this).popover('hide');
                        }
                    });
                });
                
//                 $.get(nmRoot + '/js/weather/estados_cidades.json', function(response) {            
//                     for (e in response) {
//                         var sgEstado = response[e].sigla;
//                         var i = 0;
//                         for (c in response[e].cidades) {
//                             cidades.push({
//                                 id: i,
//                                 name: response[e].cidades[c] + ', ' + sgEstado,
//                                 estado: sgEstado,
//                                 cidade: response[e].cidades[c]
//                             });
//                             i++;
//                         }
//                     }
            
//                     return cidades;
//                 });
            
//                 $(document).on('keyup', '.typeahead', function() {
//                     if (this.value.length > 3) {
//                         $(".typeahead").typeahead({
//                             source: cidades,
//                             limit: 5,
//                             onSelect: function(item) {
//                                 var cidade = item.text;
//                                 weather(cidade);
//                                 $('.widget-weather-config').popover('hide');
//                                 $(".typeahead").val("");
//                                 $(".bg-opacity").fadeOut(200);
            
//                                 $("box-weather-widget").stop().animate({
//                                     'opacity': .5,
//                                 }, 'fast');
//                             },
//                         });
//                     }
//                 });
                            
                
//                 /**
//                  * O trecho de código abaixo busca notícias via feed no site da prefeitura
//                  * Dado o resultado em xml é tratado e renderizado em tela.
//                  */
                 
//                  var feed = new google.feeds.Feed("https://g1.globo.com/rss/g1/educacao");
//                  feed.load(function(result) {
//                    if (!result.error) {
//                      var container = document.getElementById("notice-list");
//                      for (var i = 0; i < result.feed.entries.length; i++) {
//                        var entry = result.feed.entries[i];
//                        var div = document.createElement("div");
//                        div.appendChild(document.createTextNode(entry.title));
//                        container.appendChild(div);
//                      }
//                    }
//                  });
                 
//                 $.ajax({
//                 	url: 'https://www.google.com/alerts/feeds/10345859438281277149/15543187264897995665',
//             		type: 'GET',
//             		dataType: 'xml',
//             		success: function(data){
            			
//             			$xml = $(data.responseText);
                        
//                         $xml.find("item").each(function(i, val) {
//                             if (i < 5) {
                
//                                 var $this = $(this);
//                                 var $li = $("<li />").addClass("row post effected");
//                                 var $col1 = $("<div />").addClass("col-md-2 col-xs-4");
//                                 var $col2 = $("<div />").addClass("col-md-10 col-xs-8").css({
//                                     'padding-left': 0
//                                 });
//                                 var $line = $("<li />").addClass("divider");
//                                 var data = new Date($this.find("pubDate").text());
//                                 var img;
                
//                                 if ($(this).find('img')[0])
//                                     img = $(this).find('img')[0];
                            
//                             $col1.append(
//                                 $("<div />").css({
//                                     'height': '50px',
//                                     'background': 'url(' + (img ? img.src : 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQCAIAAAABc2X6AAAQR0lEQVR4Ae3beZSddXkH8Pb0nP7R9tRilUWlCERsAasiUlGstKWiILaAIlStolRorYtUlhCGJISQsCQhAQJJyBJ2EiAQsmf2fd+Xycwks+9LZt9n0k/mPb29zCjxn4EbmXvec887v/suv+/vWZ7v8zy/+YMHEj/xvjrmAM8BngM8B3gO8BzgOcDvN8BLEs5dknDO+wXwovi/ejjlMyvTLnHy+wZ4SeK5hPlA4ryFcR/1vSRx3oLYD2/JvyGxdk1c9YpXS366IPbUKVGf5wInLnD9yQf4/oSzHaYOwNLE85y/XPwfSxM/+Vj6l/KaX9ldEfVIykUPpXx6e8l/5be8uinvWwvjPvZK8U8eSv5bVy5N+qRv4BfHnxXpgINZwrk648uPpF70aOrFjb0FZe37Xij84cBo54rUz69K+8LYxMjOQ3ftrVyU3rDx2YLvjk+MvFD0w4XxZ3YPNeyuWJBc92Rrf/mmvOs9ZG3WP1uvxVNrF3GAQTVFQns88/IFMR8+ULW0ric7tX79sf/7TB6bfDLzigeT/rq0fW9c9aqKztiKjpiEmjUlbbs25HzTE/pG2kIXl7TtLmp9o7h1572xp1ojK+XhgWlECOB5y5LOvyv6z1Pr1ncOVnNFT2T+w8TkxLG3f3ZV3LMm4yvlHQfvi/vIU9lXEu9d0R/Ia9n+fOG/P5N77djE8LTrXym+zTPL2vfWdKffE/OXVJ2pv/eAaeOm3Os7Bo/EHHkYWhPdWnDT3dF/UX00dRqAvpHWrMbn2gcqDx5+kHgPdUTHVj96uCupsHVHfU/etIt7hpuowyOpnxudGBwZH4g+8lBzX8mOsl9ZrPcY8OKEj69Ivbh/tCM018rO+IeSP1XXnRWAfL3sl7HVK0ICN/sdZbfHHHkkp+ml5wq+B1joxtzmbS8W/ailv8x5/0i7uJVavyF8FZ7O/jr1ee9V+t7Y0/dULgyf2ZGjKT3DzU6ym1644+CfEktTX3Ho17T6Z5Yln88n7zx0Z2hweKyXk7t9/x/vqYzy5+j4AOUPf6ZHRcWeERE2zGMtT77whcIf5DVvS65byyGHZll9NE3I4Xv8NAVjsHektWPg8P6qJa+V/qyxt5CEh8f7/ESw9yecc0/MhyxH6PaJyfGMhs10njI/mvp52hQ5XvpcYlyZ9ne8S7jcjg41rMn4+0dSPrsx73orsjnvW6LuswX/tq3kVkH4+cLvb877tiD0XOH3nbtdMDvSlRy6PbNx693Rp6xKu5SnEJ8iwmmFkyphyaQZMH3mtN4o/5+ns7/2bMFNLFakIUOmPjTWPTh2dHD0+OHEnwOjXe0DVXxYUu2TLxX9yC1IiOsHx7rXZn2V/xeWCD8CiQeCde4zuf+yNuuKp7O+tjn/2yl1T/Pe5t3aX4ZR7a6MEoE25l67Lufqp7K+6liXfdUzuf8qPqEiWY3PNvTmMwdKntv8MoFzUY+lf9H30qTzIpdpPZH5j1C19JWyzEMdMdtK/nN1+pdho8AEiHU19OSx4aND9ahV5+CRpt5CIQrrog7rc65Znf4lrju/+dXB0S7XvF72C0JGToX6yJTw2fwKISfWPkEyAEgSiPfY7/ph83WZjc9uzb/pycx/ij68HAP3QI+NRMDksDz5Uw8m/Q09pLfpDZua+gpRkbaBSpyhqa/ohEdzX3Fb/yG3oNNM4PnCHzyecTkb5hTQrMgCvDj+46szLsOiiKhrqK53uKVnuNFJ52ANBQ4dwHQN1p5wxI3cuwDmacHBjcsfIwrwWY/jz8fGw/VzbGIIrwofGZ8cRTCchCcVw+NG3jbGz03n1SW3CnuRBvhyvDcEY2JyTBTNaNgiK5icnDAGbWnbnoOHl8kQ/RrgP9yViISPv20kaU/FfaGR4MOYIxkwwtwffWQ5+sWq9x9+wJ9mn1z31Io0id48Qah/tN0qsHNxW9K7o+yXWPfksQm8igvgpV4r+3n3cONJA9iHBwqKHkJUMIJgyAcWxJwqJQ5GcA+kQv4s9Q1GUGimMT/mg1ILf0Y64LHJ/89puR/qKtGv7ckKRrqGarHimqPp8ttgREpkRKZhCUIj+S2vNfUWuSbSAUv68SSCxQplNrKF2u7MobGemcEWx8RA1DpcMDoxNO1Xqu4CC0G9PQRF9VhkM+IAr8+5atehexbGfywq9nRaqhx5X9xHJQOI14HDS9EsZcrXSn+OUdDzqLjTXeaulWlfcMHuinul+Puq7n+l+FYLxxai4s5wQfCtJAawJ0cW4A0533ip6MfzY05ZnnwBywTVONh3R39AAWR+9CnyHucukPqoZjgWxZ8pH2SxciyHE1f6tlIe6FdSdQvGRqUjDrAiY07Ti/S5a6hGDsQauavc5ld44PU53yC3p7KulCHG1zwm8LQNVGBgVV2JnPmmvOtQ6BVpl/ACLkisfVzmRNvZNr61r3LRvqrFljLiAK/J/Mo7kGSp/Dv8Km5Zo5n2HOa0bolEp9U70oxUiqjhh6QHSZQJk7larJHekRYn77wEPpHupddlf51Pkjyob4QOxGNj7nVKFg+nfDo0qJq1Kv2L8t5pqBBvKbSc8WQBfNXLRbfwq3xscETFfgS83uFm1Gp+9AfvP57onWNc5XFZ8oWI1DRSnd/y+q/2/ZHsH+U6OQALHhwsPFCZH/GqUasExFev5LdlVByPn5T1Hkr5zDTA2hTLki+wIsuSLkypW3fSSFjuSod1QzMat9R2ZyFbKpWqHFxuVWf8c4Xf00bjtJVppwF2/aOpn7McvtMbNkcWYFUIYvQdqK6TqNjT1HFMC3KxRE6rMyYUC7Y9I02CjWssRGVnHEPdnHeDasE0wD4l7XvE4aLWN2fasAK9+Bz2xrODQP1uAPY+Cc2GnGt8kwYrlfFA+1zBd18sulnqs7dyYWLNGkkf9QaMi36p+Md3Hvwzxbr0+o1Yl8KdhZgJWAyfAvyG82nmrS6tNuiNgdvTYfNGtSSkbXYBH39BwrypBCCjsTc/iDRBH6x9oEKpMWjqopZvHZqPCa9MvURYUoh+tfRn+mb8s+6BRrGC7kzA+Ib2mkRiZgxv668IaHZAaWQmhzoODo31sg66NouAORVS1fVt6M1rHzisWCEHKGx9AzEyIaVJlkbIrsSBXyy8mXqLuhWdMfE1qyzW4oTjy0EhZwKWPHLmUyt1Nx4yTcZBm8pa0AJOQbRX91JRCZKKWVVpYM7bkn8jjfVKZWca6/W9w63qGFN8KFR5QqRPU1U3V2hx5lAx3ck0wHqiGhT3xZ3hV0/AUnm7MLgTSkVyRgUTLsAItUqqfcICcYGeNutOS7D1LWvjnHRAdYPkuvCz0nDGK/yYU1VXgmxe8ZGEwwGTfEA20BVXhmumP2mBFIr2BipNoeizoq+OVEVnnMG1mVdISGbdhoODFnGbVh2Y+p4ceMYnRqWvbFUy5NfgYK5BQYMVAHlv7GlGguTxweTzaUdczapF8WfxZ34yHn5Is+44+CeWSRnIi5Jr11LgwpYdmQ1bAKbVcdUr3fUuhSVvym56ng5LfThkjX9z4qVL2/flt2xXsvDNY+FMGiiutBYKeg4nAAi5Snn0HJGERBMY65h5GFfQT6hZLa+SM6vdKgNzEy8X/0SzQpFAlOIvZh0wWcl4WVHN0TTTUkCvPe6xC+hkUL464UelNiAnv+P1VhNps16MqLR9j2Cum0HP7Zvw0lkHrCC+Nf9G87BPoaDldZ1e+S0F+8W+P8xs2HrC2Zv0uuyr9dAZKsx6Sye6w4tybfawu8u5LrnqZ0BO+HPKP+uAzRV5CKu2Dys1Cj86nVvyv/OOM+dd2+1f4bEDwsQ0poLcCTAfqHpgQ+41vKPF0osJBS2GwynMOmDeqLjtrXCJcT9BzdXsUYLfNm/dYJqs0KMQu7XgRh5O0JrCfLHm02+7i69CsFCdoNUWvrsJG8FhOOrZAxxsJZwX2J6GSBCHnbNkpXYtzz2V9/3GeStfavYrXKkTlLXvp5+4YVHrTrvSFsR8CDnlmX7jjXSHCbBYvYip1sSYfDMIV3y1tZCEzS7TksqDx267hxpFo4AS2okUbHA47or6S2eipe2EaXIai6+W/jc+rFTwQuHNumSBX1AkEMlnln6Uu5AQLNJeCU0ML7Je6ifl7fs7BqrU92adWmqgVB9NYUvBPh0FZDE5v3m7IOTP/VUP4PThk1ap0uO2ryXomOuAqtQuT7pgSeIn6LYGukQisBStc0o7bS8bzXci1LNhRFI1r64nB83k7Vr7DwUbmWaXWuJSRBqUHS08n1nQ8lpDT64Ya+1NziaVPRVRoQgkowj5UvZmfynA0mYUFdo1GZcFgANSiWDa7xHcaxsqVecacBv6j1fqyAjgQVsDt3OlbIQMZt1Lq5siet5Ht4PJpddvQiESah7Dveibmqt9lJOT4wgDnxxWJzibElJOZQ1/2mW5Of+G0N4cJ7zu6vTLgCzvOKAJbteHZAt1FQs0IgU/zWSC1d+gONRqtr10yIw/S5jyXpVk2qWBJGGC/83yO2WFpsLCpaw7ym9/s/wOKbGQG+qb29Ei6VmXc9Wq9EupBnsO6aTU0rmk2oI+ln4pgsFR4ZJItd6NcIBR85Sc30tFt3jpu0Q8Aswmp4GAFdu2QsI0HDY0CBkwYq72rAhChKmwviXvBlGXxiKD7rWXwS4Ojlp2xSMYtCLKF7bbSsIYKsUmQ54ZP/VYe1xokA2LXsSCvJRZWTs3Uop3qaYVIrEyJ/SY4iG6YpLGr0ZZQu2aKW2ftMtSHLLd0gZpC/Fk1hWcM+RyBkvGUXEKdFj36K2K+TvL77Rnx742LJ0kB0e7Waw00JJJG7A6jTVB0ZIFE3hvinheb7HxaoxXKwwNpIHbim/jugM6IVu2EPzTxrzrVGpspdxVsUDaCBiQ3Nsb5b8mcBdsL/lp81SEV83gn6UNI+N9WB2oCgwoh+w3IjaXEhfNZGBiI0myQJ7J1uCMhk0ye11yyskCzRs2Si4UO1S2fHPp20puE3LQiYCo5ja9bKuqPEx2LdRZGurDU/DtVIMOR0KZ9jwWKIWyldSkKaGc3ixJkngFYWUg46gYLQhKU4rVXHFoU/jxLT+DNexT0iczKWs/oLTAKbJhBsIdit70P7L2WpKzMEs+AEAucypu20XO/LD80SCh4SSqfwUtO1qmtL17uIm2Ey/JW52Nede+Wf5rar+rYj6Coeg7RVoLUfRAtpG4E4+/5aKE4u7hBuGHKdpBizPSahJj1bAp06gBAGPTHcYiwNZ0Z8rvkSprxC2LeTJeVsCAg/9zCtBGHODAeeJVEqCsxucVIqEiQFmhzGZv1aKqzgRRlE9Wpg/2fFNXaszytVfiq1cJ4IEl26f5dPaV6DeneBL835IIKVwhBmn1G1hjkDzi/Rwvsk2wgqq8z087y+9AWup6shQ0glpsXvP2rfnfWRh3pjh/kv1nGlUkbS0yBDOtfqP/ApA2BfvfQ0m8ER6LnBUxWPujqRdZKXYhPp+s/3soQ+JyIA8aZetzrhZ10Sn0UODZkPtNiSFDgJMfds3vzz9bQhU0hwnQEoAnDRbP6L+6wtz/D88BngM8B3gO8BzgOcDvg+N/AXWzpa8ii2eXAAAAAElFTkSuQmCC')+ ')',
//                                     'background-size': 'cover'
//                                 })
//                             );
                
//                            $col2.append(
//                                $("<p />").css({
//                                    margin: '0'
//                                }).append(
//                                    $("<a />", {
//                                        href: $this.find('guid').text(),
//                                        target: '_blank'
//                                    }).append(
//                                        $this.text().substring($this.text().indexOf('http'), $this.length)
//                                    )
//                                )
//                            ).append(
//                                $("<em />").addClass("small").append(
//                                    data.toLocaleDateString() + " " + data.toLocaleTimeString()
//                                )
//                            );
            
//                            $li.append($col1).append($col2);
//                            $li.appendTo('.notice-list ul');
//                            if (i < 4) {
//                                $line.appendTo('.notice-list ul');
//                            }
//                             }
//                         });
//             		},
//                     error: function(jxhr,e) {
//                         console.log("jxhr = ", jxhr);
//                         console.log("e = ", e);
//                     }
//                 });
               	
               	
               	
               	//Lista de documentos
               	 var $this = $(this);
                 var $li = $("<li />").addClass("row post effected");
                 var $col1 = $("<div />").addClass("col-md-2 col-xs-4");
                 var $col2 = $("<div />").addClass("col-md-10 col-xs-8").css({
                     'padding-left': 0
                 });
                 var $line = $("<li />").addClass("divider");
                      
                 $col1.append(
                     $("<div />").css({
                         'height': '50px',
                         'background': 'url(data:image/png;base64,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',
                         'background-size': 'cover'
                     })
                 );
                
                 $col2.append(
                     $("<p />").css({
                         margin: '0'
                     }).append(
                         $("<a />", {
                             href: "documentos/resolucao_01.pdf",
                             target: '_blank'
                         }).append(
                             "Resolução 001/2019"
                         )
                     )
                 );
            
                 $li.append($col1).append($col2);
                 $li.appendTo('.document-list ul');
                 
                 $line.appendTo('.document-list ul');
                 
               	
                 var $this = $(this);
                 var $li = $("<li />").addClass("row post effected");
                 var $col1 = $("<div />").addClass("col-md-2 col-xs-4");
                 var $col2 = $("<div />").addClass("col-md-10 col-xs-8").css({
                     'padding-left': 0
                 });
                 var $line = $("<li />").addClass("divider");
                      
                 $col1.append(
                     $("<div />").css({
                         'height': '50px',
                         'background': 'url(data:image/png;base64,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',
                         'background-size': 'cover'
                     })
                 );
                
                 $col2.append(
                     $("<p />").css({
                         margin: '0'
                     }).append(
                         $("<a />", {
                             href: "documentos/resolucao_01.pdf",
                             target: '_blank'
                         }).append(
                             "Resolução 002/2019"
                         )
                     )
                 );
            
                 $li.append($col1).append($col2);
                 $li.appendTo('.document-list ul');
                 
                 $line.appendTo('.document-list ul');
               	
                 var $this = $(this);
                 var $li = $("<li />").addClass("row post effected");
                 var $col1 = $("<div />").addClass("col-md-2 col-xs-4");
                 var $col2 = $("<div />").addClass("col-md-10 col-xs-8").css({
                     'padding-left': 0
                 });
                 var $line = $("<li />").addClass("divider");
                      
                 $col1.append(
                     $("<div />").css({
                         'height': '50px',
                         'background': 'url(data:image/png;base64,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',
                         'background-size': 'cover'
                     })
                 );
                
                 $col2.append(
                     $("<p />").css({
                         margin: '0'
                     }).append(
                         $("<a />", {
                             href: "documentos/resolucao_01.pdf",
                             target: '_blank'
                         }).append(
                             "Resolução 003/2019"
                         )
                     )
                 );
            
                 $li.append($col1).append($col2);
                 $li.appendTo('.document-list ul');
                 
                 $line.appendTo('.document-list ul');
                 
                 var $this = $(this);
                 var $li = $("<li />").addClass("row post effected");
                 var $col1 = $("<div />").addClass("col-md-2 col-xs-4");
                 var $col2 = $("<div />").addClass("col-md-10 col-xs-8").css({
                     'padding-left': 0
                 });
                 var $line = $("<li />").addClass("divider");
                      
                 $col1.append(
                     $("<div />").css({
                         'height': '50px',
                         'background': 'url(data:image/png;base64,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',
                         'background-size': 'cover'
                     })
                 );
                
                 $col2.append(
                     $("<p />").css({
                         margin: '0'
                     }).append(
                         $("<a />", {
                             href: "documentos/resolucao_01.pdf",
                             target: '_blank'
                         }).append(
                             "Resolução 004/2019"
                         )
                     )
                 );
            
                 $li.append($col1).append($col2);
                 $li.appendTo('.document-list ul');
                 
                 $line.appendTo('.document-list ul');
               	
               	
               	
                
                
                var timeOut;
                $(".info-box").hover(function(e){
                	$this = this;
                	var css 		= {'-vendor-animation-duration': '1s', '-vendor-animation-delay': '0.5s', '-vendor-animation-iteration-count': 'infinite'}
                	var nmModule 	= $(".info-box-description", $this).text();
                	var textModule 	= $(".text", $this).text();
                	var iconModule 	= $(".fa", $this)[0].outerHTML;
                	
                    if(textModule){
	                	timeOut = setTimeout(function() {	                	
		                	$(".module-icon").addClass('animated fadeInRight').bind("animationend webkitAnimationEnd oAnimationEnd MSAnimationEnd",function(){
		                	    $(this).removeClass('animated fadeInRight');
		                	});
		                	
		                	$(".module-description i").attr('class', '').addClass($(iconModule).attr('class'));
		                	
		                	
		                	$(".module-description h1").css(css).text(nmModule).addClass('animated fadeInRight').bind("animationend webkitAnimationEnd oAnimationEnd MSAnimationEnd",function(){
		                	    $(this).removeClass('animated fadeInRight');
		                	});
		                	
		                	$(".module-description .text").text(textModule);
		                	$(".module-description").stop().delay(200).removeClass('hide').fadeIn('fast');
	                    }, 200);
                    }
                }, function(){    
                	$(".module-description *").off();
                	$(".module-description").stop().fadeOut(200);
                	clearTimeout(timeOut);
                });
            });  
                    
        </script>
    </body>
</html>

