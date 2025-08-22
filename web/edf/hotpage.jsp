<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="sol.dao.ResultSetMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.tivic.manager.seg.*" %>
<% 	
    int year = Calendar.getInstance().get(Calendar.YEAR);
    ResultSetMap releases = ReleaseServices.getReleases(5);
    releases.beforeFirst();
    %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="content-type">
        <meta charset="utf-8">
        <title>TIViC - www.tivic.com.br</title>
        <meta content="Bootply" name="generator">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1" name="viewport">
        <meta content="Fixed header with independent scrolling left nav and right content." name="description">
        <link href="../css/bootstrap.min.css" rel="stylesheet">
        <link href="../css/hotpage.css" rel="stylesheet">
        <link href="../css/shadowbox.css" rel="stylesheet">
        <link href="../css/shadowbox-video.css" rel="stylesheet">
        <!--[if lt IE 9]>
        <script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
        <link rel="icon" href="../favicon.png" type="image/x-icon" />
    </head>
    <style></style>
    <body>
        <div class="row content">
            <div class="sidebar col-md-2 col-xs-4">
                <div class="col-md-12"><img class="logo img-responsive" src="http://www.tivic.com.br/public/images/logo-edf-branco.png">
                </div>
                <div class="sidebar-nav col-md-12">
                    <ul class="nav">
                        <li class="active">
                            <a href="#updatelist">O que há de novo?</a>
                        </li>
                        <li>
                            <a href="#videos">Instruções de uso</a>
                        </li>
                    </ul>
                </div>
                <div class="sidebar-bottom" style="z-index: 0 !important; position: absolute;  bottom: 2px;">
                    <div class="col-md-9 col-xs-9">
                        <a href="http://www.tivic.com.br">
                        <img src="http://www.tivic.com.br/public/images/logo-tivic-small.png" class="img-responsive"/>
                        </a>
                    </div>
                </div>
            </div>
            <div class="col-md-7 content-page">
                <div class="row">
                    <h1 id="updatelist" class="title"><span class="glyphicon glyphicon-th-list"></span> O que há de novo? <small>Detalhes das últimas atualizações</small> <a class="btn btn-sm btn-primary pull-right" onClick="reload()"> <span class="glyphicon glyphicon-refresh"></span> Atualizar lista</a></h1>
                    <div class="col-md-12" class="update-list">
                        <% if(releases.next()){ %>
                        <%
                            releases.beforeFirst();
                            while(releases.next()){ 
                            %>
                        <h5><span class="label text-sm label-success">Versão (<%=releases.getString("NR_FULL_VERSION")%>)</span> <span class="label text-sm label-primary">Atualizado em: <%=releases.getDateFormat("DT_RELEASE", "dd/MM/yyyy")%></span></h5>
                        <blockquote class="update-quote">
                            <%=releases.getString("TXT_DESCRICAO").replace("-", "<strong>&bull;</strong> ").replace("\n", "<br />")%>
                        </blockquote>
                        <hr>
                        <% } %>           
                        <% } else { %>
                        <div class="alert alert-info">
                            <strong>Oops!</strong> Ainda não há atualizações cadastradas para este sistema. :c
                        </div>
                        <% } %>
                    </div>
                </div>
                <div class="row">
                    
                    <div class="col-md-12 content-videos">
                        <ul class="list-unstyled video-list-thumbs row">
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-md-3 col-lg-3" style="padding-top:50px; padding-right:1px;height: 100%; overflow: auto; overflow-x: hidden;">
                <h1 id="videos" class="title"><span class="glyphicon glyphicon-triangle-right"></span> Vídeos <small>Instruções de uso</small></h1>
                <div class="video-list row">
                    <hr>
                </div>
            </div>
        </div>
        <script src="../js/jquery-1.11.3.min.js"></script>
        <script src="../public/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="../js/shadowbox.js"></script>
        <script>
            Shadowbox.init({
            	overlayColor: "#000",
            	overlayOpacity: '0.9'
            });
            
            function reload () {
            	location.reload();
            }
            
            $(function(){
            	var videos = [
  	              	{id: 'DGBJ09uGoNk', title: 'EDF - Movimentação Escolar', desc: "Vídeo aula para te instruir na movimentação escolar."},
	              	{id: 'pJGEhlqzIFE', title: 'EDF - Profissionais da Educação', desc: "Vídeo aula para te instruir a gerenciar os profissionais da educação."},
	              	{id: 'sNoCI1nR-NM', title: 'EDF - Profissionais da Educação Parte 2', desc: "Continuação da vídeo aula de gerenciar os profissionais da educação."},
	              	{id: 'u_MD4uJqHYM', title: 'EDF - Alteração de Modalidade', desc: "Vídeo aula para de instruir a altera a modalidade de um curso."},
	              	{id: 'NC6utlkX8ok', title: 'EDF - Matrícula de Atendimento Especial', desc: "Vídeo aula para de instruir como realizar uma matrícula de atendimento especializado."},
	              	{id: 'qsfGsoXx_1g', title: 'EDF - Relatório de validação dos dados do Educacenso', desc: "Vídeo aula explicativa para validar dados do censo."},
	              	{id: '94qfGCEtt8k', title: 'EDF - Como matricular alunos em turmas específicas do Mais Educação', desc: "Vídeo aula para de instruir como matricular alunos em turmas do Mais Educação."}
              	];
            	
            	
            	$.each(videos, function(index, video){
            		console.log(video);
            		var videoThumbnail = $(document.createElement('div')).addClass('row nopadding video-thumbnail').bind('click', function(){
            			Shadowbox.open({ 
            				content: 'https://www.youtube.com/v/' + video.id + '?autoplay=1',
    						player: 'swf',
    						height: $(window).height() / .5,
    						width: $(window).width() / .5
						});
            		});
            		var videoIcon       = $(document.createElement('div')).addClass('col-md-2 col-sm-1 col-xs-1').append(
            			$(document.createElement('i')).addClass('glyphicon glyphicon-play-circle').css({'font-size': '45px', color: '#262e52'})
    				);
            		
            		var videoCaption   = $(document.createElement('div')).addClass('col-md-10 col-sm-10 col-xs-10').append(
            				$(document.createElement('p')).addClass('video-caption').append("<strong>" + video.title + "</strong>")
    				);
//             		var videoCaption   = $(document.createElement('p')).addClass('video-caption').append("<strong>" + video.title + "</strong>");
					var videoDesc   = $(document.createElement('div')).addClass('col-md-10 col-sm-10 col-xs-10').append(
							$(document.createElement('p')).addClass('video-desc').append(video.desc)
    				);
//             		var videoDesc      = $(document.createElement('p')).addClass('video-caption').append(video.desc);

            		videoDesc.append($(document.createElement('hr')));
            		videoThumbnail.append(videoIcon);
            		videoThumbnail.append(videoCaption);    
            		videoThumbnail.append(videoDesc);  
            		
            		
            		$(".video-list").append(videoThumbnail);
            	});
            	
            });
        </script>
    </body>
</html>

