<!DOCTYPE html>
<html lang="en">
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="content-type">
        <meta charset="utf-8">
        <title>e-Trânsito</title>
        <meta content="Bootply" name="generator">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1" name="viewport">
        <meta content="Fixed header with independent scrolling left nav and right content." name="description">
        <link href="https://use.fontawesome.com/releases/v5.9.0/css/all.css" rel="stylesheet">
        <link href="./css/bootstrap.min.css" rel="stylesheet">
        <link href="./css/portal.css" rel="stylesheet">
        <link href="./css/portal-fa.css" rel="stylesheet">
        <link href="./js/weather/css/weather-icons.min.css" rel="stylesheet">
        <link href="./js/weather/css/weather-icons-wind.min.css" rel="stylesheet">
        <!--[if lt IE 9]>
        <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
        <link rel="icon" href="./favicon.png" type="image/x-icon" />
    </head>
    <body>
        <div class="bg-opacity"></div>
        <div class="content">
            <div class="sidebar col-md-3 col-sm-3 col-lg-3 col-xs-4 hidden-xs">
                <div class="col-md-12">
                    <img class="logo img-responsive" src="./mob/imagens/logo_branca.png" data-url="./mv">
                </div>
                <div class="sidebar-nav col-md-12">
                    <ul class="nav nav-stacked">
                        <li class="nav-header">
                            <a href="#" data-toggle="collapse" data-target="#menu1" data-id="principal"><i class="fas fa-star"></i> Início </a>
                        </li>
                         <li class="nav-header">
                             <a href="#" data-toggle="collapse" data-url="./portal-cidadao"> <i class="fa fa-user"></i> &nbsp;Portal Cidadão </a>
                         </li>
                        <li class="nav-header">
                            <a href="#" data-toggle="collapse" data-target="#menu4" data-id="fale-conosco"> <i class="fa fa-envelope"></i> Contato </a>
                        </li>
                    </ul>
                </div>
                <div class="sidebar-bottom" style="z-index: 0 !important; position: absolute;  bottom: 0px; margin-left: 80px;">
                    <div class="col-md-8 col-xs-9">
                    	<img class="logo img-responsive img-footer" src="./imagens/logo-tivic-small_branco.png" data-url="http://tivic.com.br/" target="_blank">
                    </div>
                </div>
            </div>
            <div class="col-md-9 col-sm-9 col-lg-9 col-xs-12 content-page">
                <div id="principal" class="active">                    
                    <div class="row">
                        <div class="row cover">
                            <div class="cover-overlay"></div>
                        </div>
                    </div>
                    <div class="row">
                        <hr class="hidden-lg hidden-md hidden-sm" />  
                        <div class="col-xs-12 col-sm-6 col-md-7 col-lg-4 block">
                        <br>
                            <h3>
                                <small><i class="fa fa-bars text-muted"></i></small> &nbsp; Acesso Rápido
                            </h3>
                            
                            <br>
                            
                            <div class="card-container">
	                            <div class="card-el info-box effected" data-toggle="tooltip" title="Módulo de Processamento" data-url="./adm/#/processamento">
	                                <span class="bg-purple info-box-icon"><i class="fas fa-microchip"></i></span>
	                                <div class="info-box-content">
	                                    <span class="info-box-text">Processamento</span>
	                                    <p class="text-muted info-box-description">Módulo de Processamento de Ocorrências de Trânsito</p>
	                                </div>
	                                <!-- /.info-box-content -->
	                            </div>
	                            <!-- /.info-box -->
	                            <div class="card-el info-box effected" data-toggle="tooltip" title="Módulo de Atendimento" data-url="./adm/#/protocolos">
	                                <span class="bg-yellow info-box-icon"><i class="fa fa-user"></i></span>
	                                <div class="info-box-content">
	                                    <span class="info-box-text">Atendimento</span>
	                                    <p class="text-muted info-box-description">Módulo de Atendimento</p>
	                                </div>
	                                <!-- /.info-box-content -->
	                            </div>
	                            <!-- /.info-box -->
	                            <div class="card-el info-box effected" data-toggle="tooltip" title="Módulo de Apoio a JARI" data-url="./jari">
	                                <span class="bg-blue info-box-icon"><i class="fas fa-briefcase"></i></span>
	                                <div class="info-box-content">
	                                    <span class="info-box-text">JARI</span>
	                                    <p class="text-muted info-box-description">Módulo de Apoio a JARI</p>
	                                </div>
	                                <!-- /.info-box-content -->
	                            </div>
                            </div>
                            
                            <!-- /.info-box -->
                            <div class="card-container">
	                            <div class="card-el info-box effected" data-toggle="tooltip" title="Módulo de Gráfica" data-url="./adm/#/processamento/lotes/search">
	                                <span class="bg-red info-box-icon"><i class="fas fa-print"></i></span>
	                                <div class="info-box-content">
	                                    <span class="info-box-text">Gráfica</span>
	                                    <p class="text-muted info-box-description">Módulo de Gerenciamento de Impressões</p>
	                                </div>
	                               <!--  /.info-box-content -->
	                            </div>
	                          	<!--   /.info-box -->
	                          	<div class="card-el info-box effected" data-toggle="tooltip" title="Módulo de Monitoramento" data-url="./adm/#/monitoramento" >
	                                <span class="bg-navy info-box-icon"><i class="fa fa-desktop"></i></span>
	                                <div class="info-box-content">
	                                    <span class="info-box-text">Monitoramento</span>
	                                    <p class="text-muted info-box-description">Módulo de Vídeo Monitoramento</p>
	                                </div>
	                            </div>
	                            <div class="card-el info-box effected" data-toggle="tooltip" title="Módulo de BI" data-url="./bi">
	                                <span class="bg-green info-box-icon"><i class="fas fa-chess"></i></i></span>
	                                <div class="info-box-content">
	                                    <span class="info-box-text">BI</span>
	                                    <p class="text-muted info-box-description">Módulo de apoio a decisão e Bussiness Inteligence</p>
	                                </div>
	                               <!--  /.info-box-content -->
	                            </div>
	                            <!-- /.info-box -->
                            </div>
                        </div>
                        
                        <div class="col-xs-12 col-sm-12 col-md-7 col-lg-5">
                        <hr class="hidden-lg hidden-md hidden-sm" />  
<!--                         	<h3> -->
<!--                                 <small><i class="fas fa-angle-double-right"></i></small>   -->
<!--                             </h3> -->
                            <!-- /.info-box -->
							
                        </div>                        
                    </div>
                </div>
                
                <div id="orgao">
                    <div class="col-xs-12 col-sm-6 col-md-7 col-lg-4">
                        <h3>
                            <small><i class="fa fa-bars text-muted"></i></small> Para a SMED
                        </h3>
                        <div class="info-box effected" data-toggle="tooltip" title="Módulo Administrativo" data-module="01SEC">
                            <span class="bg-red info-box-icon"><i class="fa fa-briefcase"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">Administrativo</span>
                                <p class="text-muted info-box-description">Módulo Administrativo</p>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        <div class="info-box effected" data-toggle="tooltip" title="Recursos Humanos" data-module="09SRH">
                            <span class="bg-aqua info-box-icon"><i class="fa fa-code-fork"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">RH</span>
                                <p class="text-muted info-box-description">Módulo de Recursos Humanos</p>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        <!-- /.info-box -->
                        <div class="info-box effected" data-toggle="tooltip" title="Processo Administrativo Eletrônico" data-module="10PAE">
                            <span class="bg-teal info-box-icon"><i class="fa fa-refresh"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">PAE</span>
                                <p class="text-muted info-box-description">Módulo de Processo Administrativo Eletrônico</p>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        <!-- /.info-box -->
                        <div class="info-box effected" data-toggle="tooltip" title="Georeferenciamento" data-url="./edf/geo.jsp">
                            <span class="bg-yellow info-box-icon"><i class="fa fa-street-view"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">GEO</span>
                                <p class="text-muted info-box-description">Módulo de Georeferenciamento</p>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        <!-- /.info-box -->
                        <div class="info-box effected" data-toggle="tooltip" title="Alimentação Escolar" data-module="06CAE">
                            <span class="bg-green info-box-icon"><i class="fa fa-cutlery"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">CAE</span>
                                <p class="text-muted info-box-description">Módulo de Controle de Alimentação Escolar</p>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        <div class="info-box effected" data-toggle="tooltip" title="Pedagógico" data-module="09PED" style="cursor: pointer;">
                            <span class="bg-maroon info-box-icon"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">PED</span>
                                <p class="text-muted info-box-description">Módulo de Gerenciamento Pedagógico</p>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        <div class="info-box effected" data-toggle="tooltip" title="Transporte Escolar" data-module="12MTE">
                            <span class="bg-aqua info-box-icon"><i class="fa fa-bus"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">MTE</span>
                                <p class="text-muted info-box-description">Módulo de Transporte Escolar</p>
                            </div>
                            <!-- /.info-box-content -->
                        </div>
                        <div class="info-box effected" data-toggle="tooltip" title="Apoio à Decisão (BI)" data-module="13BI">
                            <span class="bg-blue info-box-icon"><i class="fa fa-pie-chart"></i></span>
                            <div class="info-box-content">
                                <span class="info-box-text">BI</span>
                                <p class="text-muted info-box-description">Módulo de Apoio à decisão e Estatística</p>
                            </div>
                            <!-- /.info-box-content -->
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
                                            <p class="text-muted">Suporte Técnico - e-Trânsito</p>
                                            <p><i class="fa fa-phone"></i> <a href="tel:557734298480">(77) 3429-8480</a></p>
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
<!--         <script src="./js/quark/quark.js"></script> -->
<!--         <script src="./js/weather/weather_api.js"></script> -->
<!--         <script src="./js/weather/weather_icon.js"></script> -->
        <script src="./js/jquery.xdomainajax.js"></script>
        <script src="./js/bootstrap-typeahead.min.js"></script>
<!--         <script type="text/javascript" src="/sol/cvjs/CidadeVirtual.js"></script> -->
        <script type="text/javascript" src="./js/shadowbox.js"></script>
        <script>
            $(window).load(function() {
            
                var map        = null;
                var protocol   = window.location.protocol;
                var host       = window.location.host;
                var pathname   = window.location.pathname.split('/');
                var hasFlash   = navigator.mimeTypes["application/x-shockwave-flash"];
                var cidades    = [];
                var nmRoot     = protocol + '//' + host + '/' + pathname[1] + '/';
                
//                 Quark.ajaxCaller('0017-0001', {
//                 	callback: function(response){
//                 		response = response == null ? "0.0.00" : response;
//                 		$("title").html("eTrânsito - v" + response);
//                 		$(".versao").append(response);
//                 	}
//                 });
            
                
                
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
            
                var weather = function(cidade) {
                    window.localStorage.setItem('weather_city', cidade);
            
                    $.weatherApi({
                        displayname: window.localStorage.getItem('weather_city'),
                        location: window.localStorage.getItem('weather_city'),
                        unit: 'c',
                        lang: 'pt-BR',
                        success: function(response) {
                            var weather = response.forecast;
                                        
                            $(".widget-weather-title .forecast").html(response.temp + "°");
                            $(".widget-weather-title .weather-units").html(response.unit);
                            $(".widget-weather-icon").html(setWeatherIcon(weather[0].code))
                            $(".widget-weather-desc").html(translate(weather[0].code));
                            $(".widget-weather-location").html(response.displayname);
                            
                            $(".weather-today-max").html(weather[0].high + '° ' + response.unit);
                            $(".weather-today-min").html(weather[0].low + '° ' + response.unit);
                            $(".weather-tomorrow-max").html(weather[1].high + '° ' + response.unit);
                            $(".weather-tomorrow-min").html(weather[1].low + '° ' + response.unit);
            
                            $(".widget-weather-header").css({
                                background: 'url( ' + response.image + ' ) center center',
                                'background-size': 'cover'
                            });
            
                            $("box-weather-widget").stop().animate({
                                'opacity': 1,
                            }, 'fast');
            
                        }
                    });
                }
            
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
            		
                    if (!hasFlash) {
//                         Quark.alert($('body'), {
//                             message: "Seu navegador não tem suporte a este módulo.",
//                             icon: 'fa fa-times-circle-o text-danger'
//                         });
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
            
                $('.widget-weather-config').popover({
                    html: true,
                    placement: 'bottom',
                    content: $('.weather-form').clone().fadeIn(),
                    title: 'body'
                }).on('click', function() {
                    $(".typeahead").val("");
                    $(".typeahead").focus();
                });
            
                if (window.localStorage.getItem('weather_city') === null) {
            
                    var success = function(position) {
                        var GEOCODING = 'https://maps.googleapis.com/maps/api/geocode/json?latlng=' + position.coords.latitude + '%2C' + position.coords.longitude + '&language=en';
                        $.getJSON(GEOCODING).done(function(location) {
                            weather(location['results'][0]['address_components'][3]['long_name']);
                        });
                    }
            
                    if (/chrome/.test(navigator.userAgent.toLowerCase())) {
                        jQuery.post("https://www.googleapis.com/geolocation/v1/geolocate?key=AIzaSyBKj6naZR3KAqzM09-6souhpv6xU-hsvA0", function(data) {
                            success({
                                coords: {
                                    latitude: data.location.lat,
                                    longitude: data.location.lng
                                }
                            });
                        });
                    } else {
                        navigator.geolocation.getCurrentPosition(success, null);
                    }
            
                } else {
                    weather(window.localStorage.getItem('weather_city'));
                }
            
                $('body').on('click', function(e) {
                    $('[data-toggle="popover"]').each(function() {
                        if (!$(this).is(e.target) && !$(this).is('.dropdown') && $(this).has(e.target).length === 0 && $('.popover').has(e.target).length === 0 && !$(".bg-opacity").is(":visible")) {
                            $(this).popover('hide');
                        }
                    });
                });
                
                $.get(nmRoot + '/js/weather/estados_cidades.json', function(response) {            
                    for (e in response) {
                        var sgEstado = response[e].sigla;
                        var i = 0;
                        for (c in response[e].cidades) {
                            cidades.push({
                                id: i,
                                name: response[e].cidades[c] + ', ' + sgEstado,
                                estado: sgEstado,
                                cidade: response[e].cidades[c]
                            });
                            i++;
                        }
                    }
            
                    return cidades;
                });
            
                $(document).on('keyup', '.typeahead', function() {
                    if (this.value.length > 3) {
                        $(".typeahead").typeahead({
                            source: cidades,
                            limit: 5,
                            onSelect: function(item) {
                                var cidade = item.text;
                                weather(cidade);
                                $('.widget-weather-config').popover('hide');
                                $(".typeahead").val("");
                                $(".bg-opacity").fadeOut(200);
            
                                $("box-weather-widget").stop().animate({
                                    'opacity': .5,
                                }, 'fast');
                            },
                        });
                    }
                });
                       
            });
        </script>
    </body>
</html>

