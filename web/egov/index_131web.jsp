<%@ page contentType="text/html; charset=iso-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>
<title>Transparência Municipal (Lei Complementar 131/2009)</title>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content ="no-cache"/>
<%@taglib uri="../tlds/dotVisualInterface.tld" prefix="cpnt"%>
<style type="text/css">
#rodape	{
	height:25px; 
	width:777px; 
	margin:0px; 
	clear:both; 
	background-image:url(imagens/margem_inf2.jpg); 
	border-bottom:solid 1px #CCC;	
	position:relative;
	background-repeat:repeat-x;
	border-top:solid 1px #CCC;
	border-collapse:collapse;
}
</style>
<script language="javascript">

function init()	{
	try	{
		document.getElementById('divTransparencia').style.marginLeft = Math.round((screen.availWidth-780) / 2)+'px';
	}
	catch(e){alert(e.message);}
}

function btnReceitaOnClick()	{
	window.location = 'receita.jsp';
}

function btnDespesaOnClick()	{
	window.location = 'despesa.jsp';
}
</script>
</head>
<body class="body" onload="init()" margin="0" topmargin="0" marginheight="0" rightmargin="0" bgcolor="#F1F1F3">
<div style="width: 777px; " id="divTransparencia" class="d1-form">
   <div style="width: 777px; height: 555px; background-color: #FFF; border:1px solid #CCC;" class="d1-body">
	<div id="topo">
	  <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="777" height="130" title="imagem_flash">
	    <param name="movie" value="imagens/animacao.swf" />
	    <param name="quality" value="high" />
	    <embed src="imagens/animacao.swf" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="777" height="130"></embed>
	  </object>
	</div>
	<div class="d1-line" id="line0">
		<div class="element" style="width:767px; border-bottom: 2px solid #364C87; margin-left: 5px; height: 24px; ">
			<label for="tpEtapa" class="caption" style="width:767px; font-size: 22px; text-align:center; height: 30px; color:#364C87; font-weight: bold;">Transparência Municipal (Lei Complementar 131/2009)</label>
		</div>
	</div>
	<div class="d1-line" id="line0">
	<div class="d1-line" id="line4">
		<div style="width: 764px; margin-left: 5px; height: 318px; margin-top: 50px;" class="element">
			<img src="imagens/receita.jpg" style="float: left; margin: 0 50px 0 180px; border: 0px; cursor: pointer;" onclick="btnReceitaOnClick();"/>
			<img src="imagens/despesa.jpg" style="cursor: pointer; border: 0px;" onclick="btnDespesaOnClick();"/>
		</div>
	</div>
	<div id="rodape" class="rodape" style="margin-top: 5px; float: left;"></div>
  </div>
</div>

</body>
</html>