var newMessage = function (msg, type, element, prepend) {
	
    var el = $("<div/>").css({display: 'none'});
    var faIcon;
	
    if (type === "e") {
        el.addClass("notice notice-danger");
        faIcon = "fa fa-times";
		} else if (type === "i") {
        el.addClass("notice notice-info");
        faIcon = "fa fa-info";
		} else if (type === "w") {
        el.addClass("notice notice-warning");
        faIcon = "fa fa-exclamation-triangle";
		} else if (type === "s") {
        el.addClass("notice notice-success");
        faIcon = "fa fa-check";
	}
	
    el.append("<span><i class=\"" + faIcon + "\"></i> " + msg + "</span>");
    if(prepend === false){
    	element.append(el);
    } else {
    	element.prepend(el);
    }
    el.fadeIn('fast');
};

var printPopup = function (data) {
    var mywindow = window.open('print.html','PROCON/VC - Consulta Online','width=800,height=800'); 
    mywindow.document.write('<html><head><title>PROCON/VC - Consulta Online</title>');
    mywindow.document.write('<link rel="stylesheet" href="./public/css/global.css" type="text/css" />');
    mywindow.document.write('<link rel="stylesheet" href="./public/css/bootstrap.css" type="text/css" />');
    mywindow.document.write('</head><body onLoad="window.print();" style="background: #FFFFFF !important; padding: 20px;overflow: scroll;">');
    mywindow.document.write(data);
    mywindow.document.write('</body></html>');
    mywindow.window.location.reload();    // this is the secret ingredient
    mywindow.document.close();
    mywindow.focus(); 
    return true;
};

var calculo = function (vl1, vl2){
    var larger  = (parseFloat(vl1) > parseFloat(vl2) ? vl1 : vl2);
    var smaller = (parseFloat(vl1) < parseFloat(vl2)? vl1 : vl2);
    var result = (smaller/larger)*100;
    return Math.round(result);
}


$(function () {
	
    $("#form .input-search").focus(function () {
        $(".notice").remove();
	});
    
    $(document).on('click', '.btn-print', function(){ 
        var div = $("<div />").addClass("col-md-12").css({margin: '20px'});
        $(".fixed-table-loading").remove();
        div.append($(".modal-body").html());
        printPopup(div.html());
	});
	
    $("#form").submit(function () {
		
        $(".notice").remove();
        $(".response").html("");
		
        if ($(".input-search", this).val() === "") {
            $(".load-overlay").fadeOut('fast');
            newMessage("Você precisa informar o número de atendimento.", "w", $(".response"));
            return false;
		}
		
        var url = "../proconvc?cod="+$(".input-search").val();
		
		$(".load-overlay").fadeIn('fast');       
		
        /* BUSCANDO INFORMAÇÕES NO SERVIDOR */
        $.ajax({
            url: url,
            type: "GET",
            dataType: "text",
            xhrFields: {
                withCredentials: true
			},
			}).done(function (response) {
            $(".load-overlay").fadeOut('fast');
            
            if (response === "null") {
                newMessage("Este número de atendimento não existe.", "e", $(".response"));
                return false;
			}
            
            var JSONParse = JSON.parse(response);
            
            if (JSONParse.length <= 0) {
                newMessage("Este número de atendimento não existe.", "e", $(".response"));
                return false;
			}
            /* BUSCANDO MODAL EXTERNO */
            $(".response").load("pages/modal_content.html", function (data) {
                
                
                var DOCUMENTO   = JSONParse[0]['DOCUMENTO'][0] ? JSONParse[0]['DOCUMENTO'][0] : null;
                var OCORRENCIAS = JSONParse[1]['OCORRENCIAS']  ? JSONParse[1]['OCORRENCIAS']  : null;               
                var AGENDAS     = JSONParse[2]['AGENDAS']      ? JSONParse[2]['AGENDAS']      : null;
                var TRAMITES    = JSONParse[3]['TRAMITES']     ? JSONParse[3]['TRAMITES']     : null;
                var ANEXADOS    = JSONParse[4]['ANEXADOS']     ? JSONParse[4]['ANEXADOS']     : null;
                
                /* ENVIANDO INFORMAÇÕES PARA A ESTRUTURA DO MODAL EXTERNO */
                $('.NR_DOCUMENTO').html(DOCUMENTO.NR_DOCUMENTO);
                $('.DS_ASSUNTO').html(DOCUMENTO.DS_ASSUNTO ? DOCUMENTO.DS_ASSUNTO + "..." : "Não informado...");
				
				if(JSONParse[1]['OCORRENCIAS'].length > 0){
					$('.NM_ANDAMENTO_ATUAL').html(JSONParse[1]['OCORRENCIAS'][0].NM_TIPO_OCORRENCIA);
				}
				
                $('.NM_SOLICITANTE').html(DOCUMENTO.NM_SOLICITANTE);
                $('.DT_AUDIENCIA').html(JSONParse[2]['AGENDAS'][0] ? JSONParse[2]['AGENDAS'][0].DT_INICIAL : 'NÃO INFORMADO');
                
                $("#tableOcorrencias").bootstrapTable({
                    columns: [{
						field: 'NM_TIPO_OCORRENCIA',
						title: 'Ocorrência',
						sortable: true
                        }, {
						field: 'TXT_OCORRENCIA',
						title: 'Informação',
						sortable: true
                        }, {
						field: 'DT_OCORRENCIA',
						title: 'Data',
						sortable: true
					}]
				});
                
                $("#tableAgendas").bootstrapTable({
                    columns: [{
						field: 'NM_LOCAL',
						title: 'Local',
						width: '61%',
						sortable: true
                        }, {
						field: 'DT_INICIAL',
						title: 'Data e Hora',
						sortable: true
					}]
				});
                
                $("#tableDocumentacao").bootstrapTable({
                    columns: [{
						field: 'NM_TIPO_DOCUMENTACAO',
						title: 'Documento'
                        }, {
						field: 'DT_RECEPCAO',
						title: 'Data e Hora'
					}]
				});
                
                $("#tableTramites").bootstrapTable({
                    columns: [{
						field: 'NM_SETOR_ORIGEM',
						title: 'Setor Origem'
                        }, {
						field: 'NM_SETOR_DESTINO',
						title: 'Setor Destino'
                        }, {
						field: 'DT_TRAMITACAO',
						title: 'Data e Hora'
					}]
				});
                
                $("#tableOcorrencias").bootstrapTable('load', OCORRENCIAS);
                $("#tableAgendas").bootstrapTable('load', AGENDAS);
                $("#tableDocumentacao").bootstrapTable('load', ANEXADOS);
                $("#tableTramites").bootstrapTable('load', TRAMITES);
                $("#myModal").modal('show');
			});
			
			}).fail(function (jqXHR, textStatus) {
            console.log(jqXHR);
		});
		
        return false;
	});   
	
	$(".btn-rank").click(function () {
		$(".response").load("pages/modal_ranking.html", function (data) {
			var d = new Date();
			var i = 0;

			var currDate = d.getDate();
			var currMonth = d.getMonth();
			var currYear = d.getFullYear();
			
			var date = $('.input-group.date').datepicker({
					format: "MM/yyyy",    
					startDate: "01/" + currYear,
					endDate: "12/" + currYear,
					startView: 1,
					minViewMode: 1,
					todayBtn: "linked",
					language: "pt-BR", 
					autoclose: true,
					defaultDate: d
				}).on('changeDate', function (ev) {
					var periodo = $(".input-group.date").data('datepicker').getFormattedDate('dd/mm/yyyy');
					$.post("../proconvc", {ranking: 'reclamado', periodo: periodo}, function(data){
						$(".lst-ranking").html("");
						var i=1;
						$.each(data[0], function(key, value){
							var percent = calculo(value.QT_RECLAMANTES, value.QT_RECLAMACOES_PERIODO);
							var element = 
								"<strong>"+i+"-</strong> "+ value.RECLAMADO + "<span class=\"pull-right\">" + value.QT_RECLAMANTES + " <span class=\"hidden-xs\">reclamações</span></span>" +
								"<div class=\"progress\">" +
								"<div class=\"progress-bar progress-bar-danger\" role=\"progressbar\" aria-valuenow=\"60\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: " +percent+ "%;\"><span>" +percent+ "%</span></div>" +
								"</div>";
							$(".lst-ranking").append(element);	
							i++;
						});
					});
				});
				
				$("#myModal").modal('show');											
			});		
		});		
	});		