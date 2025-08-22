<%@page import="sol.util.RequestUtilities"%>
<%    
    int cdEvento = RequestUtilities.getParameterAsInteger(request, "cdEvento", 0);        
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
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
        <form id="stInscricaoForm" method="post" class="col-md-8 col-md-offset-2" OnSubmit="return false;">
        	<h3>Verificar Situação da Inscrição</h3>
        	<hr>
        	<div class="notice notice-warning">
		        <strong>Atenção:</strong> Aos candidatos que realizaram o pagamento, informamos que a confirmação de sua inscrição será efetuada até 48 horas após o término do período de inscrições.
		    </div>
        	<hr>
       		<div>
               	<label>Informe seu CPF:</label>
       			<input type="text" class="form-control" name="nr_cpf" data-inputmask="'mask': '999.999.999-99'" required />
        	</div>
        	<hr>
       		<div align="center" class="g-recaptcha" id="recaptcha"></div>
            <hr>
            <input type="text" class="form-control hidden" name="cd_evento_principal" required value="<%=cdEvento%>" />
            <button class="btn btn-primary btn-send btn-block">Avançar</button>
            <button class="btn btn-default btn-back btn-block">Voltar para Página Incial</button>
        </form>
    </div>     
	<script src="https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit" async defer></script>
    <script src="./public/assets/js/jquery.selecao.min.js"></script>
    <script type="text/javascript">
	    function onloadCallback() {
	    	widgetId = grecaptcha.render('recaptcha', {
	          'sitekey' : '6Le-FxkTAAAAAOCiowH5cqfgLOyLV7cHYvBaDGbj'
	        });
	      };
		$(function(){    		
			$(":input").inputmask();
		});
    </script>
</body>

</html>