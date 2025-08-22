<!DOCTYPE html>
<html lang="en">
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="content-type">
        <meta charset="utf-8">
        <title>Mobilidade Urbana</title>
        <meta content="Bootply" name="generator">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1" name="viewport">
        <meta content="Fixed header with independent scrolling left nav and right content." name="description">
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
        <link href="./css/bootstrap.min.css" rel="stylesheet">
        <link href="./css/portal.css" rel="stylesheet">
        <link href="./js/weather/css/weather-icons.min.css" rel="stylesheet">
        <link href="./js/weather/css/weather-icons-wind.min.css" rel="stylesheet">
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
                    <img class="logo img-responsive" src="./mob/imagens/mob-logo.png">
                </div>
                <div class="sidebar-nav col-md-12">
                    <ul class="nav nav-stacked">
                        <li class="nav-header">
                            <a href="#" data-toggle="collapse" data-target="#menu1" data-id="principal"> <i class="fa fa-home"></i> Início </a>
                        </li>
                        <li class="nav-header">
                            <a href="#" data-toggle="collapse" data-target="#menu2" data-id="semob"> <i class="fa fa-university"></i> Para SEMOB </a>
                        </li>
                        <li class="nav-header">
                            <a href="#" data-toggle="collapse" data-target="#menu4" data-id="fale-conosco"> <i class="fa fa-envelope"></i> Contato </a>
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
                                <span id="NM_INSTITUICAO">SECRETARIA MUNICIPAL DE MOBILIDADE URBANA</span>
                                <br />
                                <small id="NM_LOCALIZACAO"></small>
                            </h1>                            
<!--                             <span id="NR_VERSAO" class="versao">ver: </span> -->
                            <div class="cover-overlay"></div>
                        </div>
                    </div>
                    <div class="gmaps map-hidden effected">
                        <div class="search-address-form col-lg-4 well well-sm">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-map-marker"></i></span>
                                <input class="form-control input-sm" placeholder="Informe seu endereço" type="text" name="endereco">
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
                                <small><i class="fa fa-bars text-muted"></i></small> Acesso Rápido
                            </h3>
<!--                             <div class="info-box effected" data-toggle="tooltip" title="Módulo de Transporte" data-module="MOB"> -->
<!--                                 <span class="bg-aqua info-box-icon"><i class="fa fa-bus"></i></span> -->
<!--                                 <div class="info-box-content"> -->
<!--                                     <span class="info-box-text">Módulo de Transporte</span> -->
<!--                                     <p class="text-muted info-box-description">Módulo de Gestão Interna de Transporte Público</p> -->
<!--                                 </div> -->
<!--                                 /.info-box-content -->
<!--                             </div> -->
<!--                             /.info-box -->
<!--                             <div class="info-box disabled" data-toggle="tooltip" title="Módulo de Trânsito" data-module="02PROF"> -->
<!--                                 <span class="bg-gray-active info-box-icon"><i class="fa fa-car"></i></span> -->
<!--                                 <div class="info-box-content"> -->
<!--                                     <span class="info-box-text">Módulo de Trânsito</span> -->
<!--                                     <p class="text-muted info-box-description">Módulo de Gestão Interna do SIMTRANS</p> -->
<!--                                 </div> -->
<!--                                 /.info-box-content -->
<!--                             </div> -->
                            <!-- /.info-box -->
                            <div class="info-box disabled" data-toggle="tooltip" title="Portal do Taxista" data-module="10PAE">
                                <span class="bg-gray-active info-box-icon"><i class="fa fa-taxi"></i></span> <!-- bg-teal -->
                                <div class="info-box-content">
                                    <span class="info-box-text">Portal do Taxista</span>
                                    <p class="text-muted info-box-description">Portal de Acesso Rápido para o Permissionário</p>
                                </div>
                                <!-- /.info-box-content -->
                            </div>
                            <!-- /.info-box -->
<!--                             <div class="info-box effected" data-toggle="tooltip" title="Módulo de BEM-e" data-module="PNE"> -->
<!--                                 <span class="bg-blue info-box-icon"><i class="fa fa-wheelchair"></i></span> -->
<!--                                 <div class="info-box-content"> -->
<!--                                     <span class="info-box-text">Módulo de BEM-e</span> -->
<!--                                     <p class="text-muted info-box-description">Módulo de Gestão Interna do BEM Especial</p> -->
<!--                                 </div> -->
<!--                                 /.info-box-content -->
<!--                             </div> -->
                            <!-- /.info-box -->
                            <div class="info-box disabled" data-toggle="tooltip" title="Portal do BEM-e" data-module="10PAE">
                                <span class="bg-gray-active info-box-icon"><i class="fa fa-wheelchair"></i></span> <!-- bg-teal -->
                                <div class="info-box-content">
                                    <span class="info-box-text">Portal do BEM-e</span>
                                    <p class="text-muted info-box-description">Portal de Acesso aos resultados do BEM-e</p>
                                </div>
                                <!-- /.info-box-content -->
                            </div>                            
                        </div>
                        
                        <div class="col-xs-12 col-sm-6 col-md-5 col-lg-3">         
                            <hr class="hidden-lg hidden-md hidden-sm" />  
                            <h3>
                                <small><i class="fa fa-cloud text-muted"></i></small> Clima
                            </h3>
                            <div class="box box-weather-widget widget-weather-2">
                                <div class="widget-weather-header bg-red">
                                    <div class="widget-weather-icon">
                                        <i class="fa fa-moon-o" aria-hidden="true"></i>
                                    </div>
                                    <h3 class="widget-weather-title"><span class="forecast">0°</span> <small class="weather-unit">C</small></h3>
                                    <h5 class="widget-weather-desc">Aguardando...</h5>
                                    <h5 class="widget-weather-location">Aguardando...</h5>
                                    <button class="widget-weather-config btn btn-link" data-toggle="popover" title="Mudar localização">
                                    <i class="fa fa-cog"></i>
                                    </button>
                                </div>
                                <div class="box-footer no-padding">
                                    <ul class="nav nav-stacked">
                                        <li>
                                            <a href="#">
                                            Hoje
                                            <span class="pull-right">
                                            máx <span class="badge bg-yellow weather-today-max">0</span> 
                                            min <span class="badge bg-aqua weather-today-min">0</span> 
                                            </span>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#">
                                            Amanhã
                                            <span class="pull-right">
                                            máx <span class="badge bg-yellow weather-tomorrow-max">0</span> 
                                            min <span class="badge bg-aqua weather-tomorrow-min">0</span> 
                                            </span>
                                            </a>
                                        </li>
                                        <li><small class="copy pull-right">Dados do <strong class="text-danger">Yahoo</strong> desenvolvido por <strong class="text-info">TIViC</strong></small>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                            
                            <hr class="hidden-lg hidden-md hidden-sm" />  
<!--                             <h3> -->
<!--                                 <small><i class="fa fa-map-marker text-muted"></i></small> Buscar Instituições -->
<!--                             </h3> -->
<!--                             <img src="./edf/imagens/ache.jpg" class="img-thumbnail img-responsive img-gmaps" /> -->
                        </div>
                        
                        <div class="col-xs-12 col-sm-12 col-md-7 col-lg-5">
                        <hr class="hidden-lg hidden-md hidden-sm" />  
                            <h3>
                                <small><i class="fa fa-newspaper-o text-muted"></i></small> Últimas Notícias
                            </h3>
                            <div class="well well-sm clearfix">
                                <div class="col-md-12 col-lg-12 col-xs-12 notice-list">
                                    <ul class="list-unstyled">
                                    </ul>
                                </div>
                                <a href="http://www.pmvc.ba.gov.br/categoria/noticias/" target="_blank" class="btn btn-sm btn-link pull-right">VER MAIS</a>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="semob">
                    <div class="col-xs-12 col-sm-6 col-md-7 col-lg-4">
                        <h3>
                            <small><i class="fa fa-bars text-muted"></i></small> Para a SEMOB
                        </h3>
                         <div class="info-box effected" data-toggle="tooltip" title="Módulo de Transporte" data-module="MOB">
                                <span class="bg-aqua info-box-icon"><i class="fa fa-bus"></i></span>
                                <div class="info-box-content">
                                    <span class="info-box-text">Módulo de Transporte</span>
                                    <p class="text-muted info-box-description">Módulo de Gestão Interna de Transporte Público</p>
                                </div>
                                <!-- /.info-box-content -->
                            </div>
                            <!-- /.info-box -->
                            <div class="info-box disabled" data-toggle="tooltip" title="Módulo de Trânsito" data-module="02PROF">
                                <span class="bg-green info-box-icon"><i class="fa fa-car"></i></span>
                                <div class="info-box-content">
                                    <span class="info-box-text">Módulo de Trânsito</span>
                                    <p class="text-muted info-box-description">Módulo de Gestão Interna do SIMTRANS</p>
                                </div>
                                <!-- /.info-box-content -->
                            </div>
                            <!-- /.info-box -->
                            <div class="info-box effected" data-toggle="tooltip" title="Módulo de BEM-e" data-module="PNE">
                                <span class="bg-blue info-box-icon"><i class="fa fa-wheelchair"></i></span>
                                <div class="info-box-content">
                                    <span class="info-box-text">Módulo de BEM-e</span>
                                    <p class="text-muted info-box-description">Módulo de Gestão Interna do BEM Especial</p>
                                </div>
                                <!-- /.info-box-content -->
                            </div>  
                            <div class="info-box effected" data-toggle="tooltip" title="Módulo de Atendimento" data-module="CAM">
                                <span class="bg-orange info-box-icon"><i class="fa fa-square"></i></span>
                                <div class="info-box-content">
                                    <span class="info-box-text">Módulo de Atendimento</span>
                                    <p class="text-muted info-box-description">Módulo de Atendimento ao Usuário</p>
                                </div>
<!--                                 /.info-box-content -->
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
                                            <p class="text-muted">Suporte Técnico - Mobilidade Urbana</p>
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
                        <div class="col-sm-6">
                            <div class="panel">
                                <div class="panel-body p-t-10">
                                    <div class="media-main">
                                        <a class="pull-left" href="#">
                                        <img class="thumb-lg img-circle bx-s" src="./mob/imagens/logos/brasao_vdc_256px.png" alt="">
                                        </a>
                                        <div class="info">
                                            <h4>SEMOB - Secretaria Municipal de Mobilidade Urbana</h4>
                                            <p class="text-muted">Informações - Mobilidade Urbana</p>
                                            <p><i class="fa fa-phone"></i> <a href="tel:773429-7352">(77) 3429-7352</a></p>
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
        <script src="./js/weather/weather_api.js"></script>
        <script src="./js/weather/weather_icon.js"></script>
        <script src="./js/jquery.xdomainajax.js"></script>
        <script src="./js/bootstrap-typeahead.min.js"></script>
        <script type="text/javascript" src="/sol/cvjs/CidadeVirtual.js"></script>
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
                
                Quark.ajaxCaller('0017-0001', {
                	callback: function(response){
                		response = response == null ? "0.0.00" : response;
                		$("title").html("Mobilidade Urbana - v" + response);
                		$(".versao").append(response);
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
            
                        Quark.ajaxCaller('0010-0008', {
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
                                            content: "<b>" + item.NM_PESSOA + "</b>"
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
                                        console.log(results[0]);
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
                        Quark.alert($('body'), {
                            message: "Seu navegador não tem suporte a este módulo.",
                            icon: 'fa fa-times-circle-o text-danger'
                        });
                        return false;
                    }
            
                    if (nmRoot && $module !== '') {
                        location.href = nmRoot + 'flex/' +  'Manager.jsp?m=' + $module + '&p=mob';
                    } else {
                        location.href = nmRoot + 'flex/' +  'Manager.jsp?m=' + $module+ '&p=mob';
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
                            
                
                /**
                 * O trecho de código abaixo busca notícias via feed no site da prefeitura
                 * Dado o resultado em xml é tratado e renderizado em tela.
                 */
            
                $.get('http://www.pmvc.ba.gov.br/feed/?' + new Date().getTime(), function(data) {
                    $xml = $(data.responseText).eq(13);
                    $xml.find("item").each(function(i, val) {
                        if (i < 5) {
            
                            var $this = $(this);
                            var $li = $("<li />").addClass("row post effected");
                            var $col1 = $("<div />").addClass("col-md-2 col-xs-4");
                            var $col2 = $("<div />").addClass("col-md-10 col-xs-8").css({
                                'padding-left': 0
                            });
                            var $line = $("<li />").addClass("divider");
                            var data = new Date($this.find("pubDate").text());
                            var img;
            
                            if ($(this).find('img')[0])
                                img = $(this).find('img')[0];
            
                            $col1.append(
                                $("<div />").css({
                                    'height': '50px',
                                    'background': 'url(' + (img ? img.src : nmRoot + '/mob/imagens/noticia.jpg')+ ')',
                                    'background-size': 'cover'
                                })
                            );
            
                            $col2.append(
                                $("<p />").css({
                                    margin: '0'
                                }).append(
                                    $("<a />", {
                                        href: $this.find('guid').text(),
                                        target: '_blank'
                                    }).append(
                                        $this.text().substring($this.text().indexOf('http'), $this.length)
                                    )
                                )
                            ).append(
                                $("<em />").addClass("small").append(
                                    data.toLocaleDateString() + " " + data.toLocaleTimeString()
                                )
                            );
            
                            $li.append($col1).append($col2);
                            $li.appendTo('.notice-list ul');
                            if (i < 4) {
                                $line.appendTo('.notice-list ul');
                            }
                        }
                    });
            
                });
            });
        </script>
    </body>
</html>

