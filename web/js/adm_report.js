function getRecibo()	{
	var div = document.createElement("DIV");
	div.innerHTML = 
		/* Cabelho */
		'<div id="reciboPagamento" style="padding:5px; border:0.4mm solid #000; background-color: #E9E9E9;"> '+
		'  <div style="width:624px; height:180px; border:1px solid #000; font-family:Arial, Helvetica, sans-serif; background-color: #FFF;"> '+
		'	<div style="height:40px; border-bottom:2px solid #000000; position:relative"> '+
		'		<div style="height:30px; width:205px; float:left; border-right:1px solid #000; font-size:16px; padding-top:10px;"> '+
		'			<span style="font-size:22px; margin-top:20px; font-weight:bold;">N�:</span><span style="margin-top:5px;">#NR_DOCUMENTO</span>'+
		'       </div> '+
		'		<div style="height:40px; width:205px; float:left; font-size:35px; text-align:center;"> '+
		'			RECIBO '+
		'       </div> '+
		'		<div style="height:30px; width:205px; float:left; border-left:1px solid #000; font-size:16px; font-weight:bold; text-align:right; padding-top:10px;"> '+
		'			<span style="font-size:22px; font-weight:bold;">&nbsp;Valor:</span> R$ #DS_VL_RECEBIDO '+
		'       </div> '+
		'	</div> '+
		'	<div style="height:50px; border-bottom:1px solid #000000; position:relative"> '+
		'		<div style="height:25px; font-size:11px;"> '+
		'			&nbsp;Recebi(emos) de(o)(a):<br/> '+
		'			&nbsp;&nbsp;#NM_PESSOA</div> '+
		'	</div> '+
		'	<div style="height:30px; border-bottom:1px solid #000000;"> '+
		'		<div style="height:30px; width:100px; float:left; font-size:11px;"> '+
		'			&nbsp;Recebido em<br/> '+
		'		&nbsp;#DS_DT_MOVIMENTO</div> '+
		'		<div style="border-left:1px solid #000000; height:30px; width:100px; float:left; font-size:11px;"> '+
		'			&nbsp;Multa (R$)<br/> '+
		'		&nbsp;#DS_VL_MULTA</div> '+
		'		<div style="border-left:1px solid #000000; height:30px; width:100px; float:left; font-size:11px;"> '+
		'			&nbsp;Juros (R$)<br/> '+
		'		&nbsp;#DS_VL_JUROS</div> '+
		'		<div style="border-left:1px solid #000000; height:30px; width:105px; float:left; font-size:11px;"> '+
		'			&nbsp;Outros (R$)<br/> '+
		'		&nbsp;#DS_VL_TARIFA_COBRANCA</div> '+
		'		<div style="border-left:1px solid #000000; height:30px; width:105px; float:left; font-size:11px;"> '+
		'			&nbsp;Desconto (R$)<br/> '+
		'		&nbsp;#DS_VL_DESCONTO</div> '+
		'		<div style="border-left:1px solid #000000; height:30px; float:left; font-size:11px;"> '+
		'			&nbsp;Recebido (R$)<br/> '+
		'		&nbsp;#DS_VL_RECEBIDO</div> '+
		'	</div> '+
		'	<div style="font-size:11px;"> '+
		'			&nbsp;Hist�rico (referente a)<br/> '+
		'			&nbsp;#DS_HISTORICO '+
		'	</div> '+
		'  </div> '+ 
		'  <div style="width:624px; height:45px; margin-top:4px; border:1px solid #000; font-family:Arial, Helvetica, sans-serif; background-color: #FFF;"> '+
		'		<div style="height:40px; width:300px; margin-top: 15px; font-weight:bold; font-size:11px; float:left;"> '+
		'			&nbsp;&nbsp;#NM_CIDADE, #DATA_EXTENSO<br/> '+
		'		</div> '+
		'		<div style="height:45px; margin-top: 15px; font-weight:bold; font-size:11px; float:left; text-align:center; width: 80px"> '+
		'			Assinatura: '+
		'		</div> '+
		'		<div style="width:244px; white-space:nowrap; height: 45px; margin-top: 15px; margin-bottom: 1px solid #000; float:left; font-size:11px; text-align: center;"> '+
		'			<div style="text-align: center;">________________________________________</div>'+
		'			<div style="text-align: center;">#NM_EMPRESA</div>'+
		'		</div> '+
		'  </div> '+
		'</div>';
	return div;
}

function getReciboMovimento()	{
	var div = document.createElement("DIV");
	div.innerHTML = 
		/* Cabelho */
		'<div id="reciboPagamento" style="padding:5px; border:0.4mm solid #000; background-color: #E9E9E9;"> '+
		'  <div style="width:624px; height:180px; border:1px solid #000; font-family:Arial, Helvetica, sans-serif; background-color: #FFF;"> '+
		'	<div style="height:40px; border-bottom:2px solid #000000; position:relative"> '+
		'		<div style="height:30px; width:205px; float:left; border-right:1px solid #000; font-size:16px; padding-top:10px;"> '+
		'			<span style="font-size:22px; margin-top:20px; font-weight:bold;">N�:</span><span style="margin-top:5px;">#NR_DOCUMENTO</span>'+
		'       </div> '+
		'		<div style="height:40px; width:205px; float:left; font-size:35px; text-align:center;"> '+
		'			RECIBO '+
		'       </div> '+
		'		<div style="height:30px; width:205px; float:left; border-left:1px solid #000; font-size:16px; font-weight:bold; text-align:right; padding-top:10px;"> '+
		'			<span style="font-size:22px; font-weight:bold;">&nbsp;Valor:</span> R$ #DS_VL_MOVIMENTO '+
		'       </div> '+
		'	</div> '+
		'	<div style="height:50px; border-bottom:1px solid #000000; position:relative"> '+
		'		<div style="height:25px; font-size:11px;"> '+
		'			&nbsp;Recebi(emos) de(o)(a):<br/> '+
		'			&nbsp;&nbsp;#NM_PESSOA</div> '+
		'	</div> '+
		'	<div style="height:30px; border-bottom:1px solid #000000;"> '+
		'		<div style="height:30px; width:100px; float:left; font-size:11px;"> '+
		'			&nbsp;Recebido em<br/> '+
		'		&nbsp;#DS_DT_MOVIMENTO</div> '+
		'		<div style="border-left:1px solid #000000; height:30px; width:405px; float:left; font-size:11px;"> '+
		'			&nbsp;Forma Pagamento<br/> '+
		'		&nbsp;#NM_FORMA_PAGAMENTO</div> '+
		'		<div style="border-left:1px solid #000000; height:30px; float:left; font-size:11px;"> '+
		'			&nbsp;Recebido (R$)<br/> '+
		'		&nbsp;#DS_VL_MOVIMENTO</div> '+
		'	</div> '+
		'	<div style="font-size:11px;"> '+
		'			&nbsp;Hist�rico (referente a)<br/> '+
		'			&nbsp;#DS_HISTORICO '+
		'	</div> '+
		'  </div> '+ 
		'  <div style="width:624px; height:45px; margin-top:4px; border:1px solid #000; font-family:Arial, Helvetica, sans-serif; background-color: #FFF;"> '+
		'		<div style="height:45px; width:300px; margin-top: 15px; font-weight:bold; font-size:11px; float:left;"> '+
		'			&nbsp;&nbsp;#NM_CIDADE, #DATA_EXTENSO<br/> '+
		'		</div> '+
		'		<div style="height:45px; margin-top: 15px; font-weight:bold; font-size:11px; float:left; text-align:center; width: 80px"> '+
		'			Assinatura: '+
		'		</div> '+
		'		<div style="width:244px; white-space:nowrap; height: 45px; margin-top: 15px; margin-bottom: 1px solid #000; float:left; font-size:11px; text-align: center;"> '+
		'			<div style="text-align: center;">________________________________________</div>'+
		'			<div style="text-align: center;">#NM_EMPRESA</div>'+
		'		</div> '+
		'  </div> '+
		'</div>';
	return div;
}

function processRegisterBloqueto(reg)	{
	var urlLogo = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.adm.ContaCarteiraServices&method=getBytesImage(const '+reg['CD_CONTA']+':int,const '+reg['CD_CONTA_CARTEIRA']+':int)&idSession=imgLogo_'+reg['CD_CONTA_CARTEIRA'];
	var urlLogoEmpresa = '../preview_imagem.jsp?tpLocalizacao=1&className=com.tivic.manager.grl.EmpresaServices&method=getBytesLogo(const '+reg['CD_EMPRESA']+':int)';
	reg['LOGO_CARTEIRA']	  = urlLogo;
	reg['LOGO_EMPRESA']	  	  = urlLogoEmpresa;
	reg['DT_PROCESSAMENTO']   = formatDateTime(new Date());
	reg['NM_AVALISTA'] 		  = reg['NM_AVALISTA'] ? reg['NM_AVALISTA'] : '';
	reg['NR_CPF_CNPJ_SACADO'] = reg['NR_CPF']!=null ? (new Mask('###.###.###-##')).format(reg['NR_CPF']) : (new Mask('##.###.###/####-##')).format(reg['NR_CNPJ']);
	reg['DS_ENDERECO_SACADO'] = reg['DS_ENDERECO_SACADO']==null ? '' : reg['DS_ENDERECO_SACADO'];
	reg['DT_VENCIMENTO'] 	  = reg['DT_VENCIMENTO']==null ? '' : reg['DT_VENCIMENTO'].split(' ')[0];
	reg['DT_EMISSAO']    	  = reg['DT_EMISSAO']==null ? '' : reg['DT_EMISSAO'].split(' ')[0];
	reg['CL_BANCO'] 		  = reg['NR_BANCO']!=null&&reg['NR_BANCO']!='' ? (reg['NR_BANCO']+(reg['NR_DV_BANCO']==null?'':'-'+reg['NR_DV_BANCO'])) : '';
	reg['NR_AGENCIA'] 		  = reg['NR_AGENCIA'] ? reg['NR_AGENCIA'] : '';
	reg['NR_LINHA_DIGITAVEL'] = reg['NR_LINHA_DIGITAVEL'] ? reg['NR_LINHA_DIGITAVEL'] : '';
	reg['NM_LOCAL_PAGAMENTO'] = reg['NM_LOCAL_PAGAMENTO']==null ? 'Pag�vel em todas as ag�ncias banc�rias' : reg['NM_LOCAL_PAGAMENTO'];
	reg['NM_ACEITE'] 		  = reg['NM_ACEITE']!=null ? reg['NM_ACEITE'] : 'N�o';
	reg['NM_MOEDA']  		  = reg['NM_MOEDA']==null ? 'R$' : reg['NM_MOEDA'];
	reg['SG_CARTEIRA']        = reg['SG_CARTEIRA']==null ? 'Uso do Banco' : reg['SG_CARTEIRA'];
	reg['DS_QUANTIDADE']      = '';
	reg['DS_VALOR']    		  =	'';
	reg['CL_VALOR']    		  =	formatCurrency(reg['VL_ARECEBER']);
	reg['NM_SACADO']		  = reg['NM_RAZAO_SOCIAL']!=null && trim(reg['NM_RAZAO_SOCIAL'])!='' ? reg['NM_RAZAO_SOCIAL'] : reg['NM_PESSOA'];
	reg['INF_SACADO']		  = reg['NM_SACADO']+' - '+reg['NR_CPF_CNPJ_SACADO']+'<br>'+reg['DS_ENDERECO_SACADO'];
	reg['NR_NOSSO_NUMERO']    = reg['NR_NOSSO_NUMERO'] ? reg['NR_NOSSO_NUMERO'] : '';
	reg['NR_DOCUMENTO']       = reg['NR_DOCUMENTO'] ? reg['NR_DOCUMENTO'] : '-';
	reg['SG_TIPO_DOCUMENTO']  = reg['SG_TIPO_DOCUMENTO'] ? reg['SG_TIPO_DOCUMENTO'] : '-';
	reg['TXT_MENSAGEM']       = reg['TXT_MENSAGEM'] ? reg['TXT_MENSAGEM'] : '';
	reg['CL_ENDERECO_LINHA1'] = 'Rua Sinhazinha Santos, 237, Sala 03, Ed. S�o Gabriel, Bairro Centro';
	reg['CL_ENDERECO_LINHA2'] = 'CEP: 45.010-160 - Vit�ria da Conquista - Bahia';
	reg['CL_ENDERECO_LINHA3'] = 'Fone/Fax: (77)3421-3917';
	reg['CL_ENDERECO_LINHA4'] = 'www.tivic.com.br';
																// Calculando 
	var prMulta = reg['PR_MULTA_CARTEIRA']>0 ? parseFloat(reg['PR_MULTA_CARTEIRA']) : 0;
	var prJuros = reg['PR_JUROS_CARTEIRA']>0 ? parseFloat(reg['PR_JUROS_CARTEIRA']) : 0;
	reg['DS_PR_MULTA'] 		  = formatCurrency(prMulta);
	reg['DS_PR_JUROS'] 		  = formatCurrency(prJuros);
	reg['DS_VL_JUROS'] 		  = formatCurrency(prJuros)+(prJuros>=1 ?'a.m.':'a.d.');
	reg['DS_VL_MULTA'] 		  = formatCurrency(prMulta * parseFloat(reg['VL_CONTA'], 10) / 100);
	var prJurosad = prJuros>=1 ? prJuros/30 : prJuros; 
	reg['DS_VL_JUROS'] 		  = formatCurrency(prJurosad * parseFloat(reg['VL_CONTA']) / 100)+'a.d.';
	reg['TXT_MENSAGEM'] = reg['TXT_MENSAGEM'].replace('[%Multa]', '#DS_PR_MULTA');
	reg['TXT_MENSAGEM'] = reg['TXT_MENSAGEM'].replace('[%Juros]', '#DS_PR_JUROS');
	reg['TXT_MENSAGEM'] = reg['TXT_MENSAGEM'].replace('[$Multa]', '#DS_VL_MULTA');
	reg['TXT_MENSAGEM'] = reg['TXT_MENSAGEM'].replace('[$Juros]', '#DS_VL_JUROS');
	reg['TXT_MENSAGEM'] = reg['TXT_MENSAGEM'].replace('[$JurosDia]', formatCurrency(prJurosad * parseFloat(reg['VL_CONTA'], 10) / 100));
}

function getBloqueto(tpReciboSacado)	{
	// RECIBO DO SACADO
	var divBoleto = document.createElement("DIV");
	divBoleto.id = 'divBloqueto';
	divBoleto.style.cssText = 'width:205mm; white-space:nowrap; font-family:Arial, Helvetica, sans-serif; padding:0;';
	var divReciboSacado = document.createElement("DIV");
	if(tpReciboSacado==0 || !tpReciboSacado)	{
		divReciboSacado.style.cssText = 'width:28mm; height:95mm; float:left; white-space:nowrap; overflow:hidden; border-bottom:0.3mm dashed #000;';
		// Logomarca do banco
		var div = document.createElement("DIV");
		div.style.cssText = 'height:7mm; solid #000; align:center;';
		var img = document.createElement("IMG");
		img.style.cssText = "height:6.2mm; width:28mm; border:0; margin:0; padding:0;";
		img.src = "#LOGO_CARTEIRA";
		div.appendChild(img);
		divReciboSacado.appendChild(div);
		// Texto: recibo do sacado
		div = document.createElement("DIV");
		var label = document.createElement("label");
		div.style.cssText 	= 'height:3mm; border-bottom:0.35mm solid #000;';
		label.style.cssText = "white-space:nowrap; font-size:10px; width:32mm; text-align:center;";
		label.innerHTML  	= "&nbsp;RECIBO DO SACADO";
		div.appendChild(label);
		divReciboSacado.appendChild(div);
		var labels = ['Cedente','N� Documento','Vencimento','Ag&ecirc;ncia/C�d.Cedente','Nosso N&uacute;mero',
		              '(=)Valor Documento','(-)Desconto/Abatimento','(-)Outras dedu&ccedil;&otilde;es','(+)Mora / Multa',
		              '(+)Outros Acr&eacute;scimos','(=)Valor Cobrado','Sacado'];
		var values = ['#NM_EMPRESA','#NR_DOCUMENTO','#DT_VENCIMENTO','#NR_AGENCIA/#NR_CEDENTE','#NR_NOSSO_NUMERO',
		              '#CL_VALOR','','','','','','#NM_SACADO'];
		for(var i=0; i<labels.length; i++)	{
			var div   = document.createElement("DIV");
			var label = document.createElement("label");
			label.style.cssText = "font-size:8px;";
			label.innerHTML  	= labels[i];
			div.appendChild(label);
			div.style.cssText 	= 'height:6mm; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;';
			if(values[i]!='')	{
				var divContent = document.createElement("div");
				// N� Doc, Venc, Ag�ncia/Cedente e Nosso n�mero
				if(i==1 || i==2 || i==3 || i==4)	{
					divContent.style.cssText = "font-size:10px; white-space:nowrap; text-align: center;";
				}
				// Nome do Sacado
				else if (i == labels.length-1)	{
					div.style.cssText 	= 'height:12.5mm; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;';
					divContent.style.cssText = "font-size:10px; white-space:normal;";
				}
				else 
					divContent.style.cssText = "font-size:10px; white-space:nowrap;";
				divContent.innerHTML 	 = values[i];
				div.appendChild(divContent);
			}
			divReciboSacado.appendChild(div);
		}
		divBoleto.appendChild(divReciboSacado);
	}
	// RECIBO DE BOLETO (FOLHA TODA)
	else	{
		// Cabe�alho: Logo
		var div = document.createElement('div');
		div.style.cssText 	= 'width:36mm; height:12mm; margin-bottom:1mm; font-size:10px; font-weight:bold; float: left;';
		div.innerHTML 		= '<img style="border:0; height: 20mm; margin-top:1mm; margin:0; padding:0; float:left;" src="#LOGO_EMPRESA"/>';
		divBoleto.appendChild(div);
		// Cabe�alho: Endere�o
		var divEndereco = document.createElement('div');
		divEndereco.style.cssText 	= 'width:97mm; height:12mm; margin-bottom:1mm; text-align:center; border:0; font-size:10px; font-weight:bold; float: left;';
		divEndereco.innerHTML 		= '#CL_ENDERECO_LINHA1<br/>#CL_ENDERECO_LINHA2<br/>#CL_ENDERECO_LINHA3<br/>#CL_ENDERECO_LINHA4';
		var div = document.createElement('div');
		div.style.cssText = 'width:97mm; height:0.7mm; margin-top:1mm; text-align:center; border:0; font-size:15px; font-weight:bold; float: left;';
		div.innerHTML     = '#NM_EMPRESA';
		div.appendChild(divEndereco);
		divBoleto.appendChild(div);
		// Cabe�alho: Fatura
		var div = document.createElement('div');
		div.style.cssText 	= 'width:51mm; height:12mm; margin-top:10mm; text-align:right; font-size:14px; font-weight:bold; float:left;';
		div.innerHTML 		= 'RECIBO DO SACADO';
		divBoleto.appendChild(div);
		// Texto: RECIBO DO SACADO
		var div = document.createElement('div');
		div.style.cssText 	= 'width:185mm; height:3mm; margin-bottom:1mm; text-align:right; border:0; font-size:10px; font-weight:bold; float: left;';
		div.innerHTML 		= 'RECIBO DO SACADO';
		// divBoleto.appendChild(div);
		// Box principal
		divReciboSacado.style.cssText = 'width:185mm; height:124.94mm; float:left; white-space:nowrap; overflow:hidden; border:0.35mm solid #000;';
		divBoleto.appendChild(divReciboSacado);
		var labels = ['&nbsp;Cedente','Ag&ecirc;ncia/C�d.Cedente','Data do Documento','Vencimento','&nbsp;Sacado',
		              'N�mero do Documento','Nosso N�mero','&nbsp;Instru��es de responsabilidade do cedente','&nbsp;Valor do Documento',
		              '&nbsp;(+)Outros Acr�scimos','&nbsp;(-) Descontos/Abatimentos','&nbsp;(-) Outras Dedu��es','&nbsp;(+) Mora/Multa',
		              '&nbsp;(=) Valor Cobrado'];
		var values = ['#NM_EMPRESA','#NR_AGENCIA/#NR_CEDENTE','#DT_PROCESSAMENTO','#DT_VENCIMENTO','#NM_SACADO',
		              '#NR_DOCUMENTO','#NR_NOSSO_NUMERO','#TXT_MENSAGEM','#CL_VALOR','','','','',''];
		var wids   = [86,45.93,26,26,86,54,44.3,134.5,50,50,50, 50, 50, 50];
		for(var i=0; i<=13; i++)	{
			var div   = document.createElement("DIV");
			var label = document.createElement("label");
			label.style.cssText = "font-size:10px; height:5mm; line-height:5mm;";
			label.innerHTML  	= labels[i];
			div.appendChild(label);
			div.style.cssText 	= 'height:11mm; overflow: hidden; float:left; border-bottom:0.35mm solid #000; width:'+wids[i]+'mm';
			if(i!=0 && i!=4 && i<7)	{
				div.style.borderLeft = '0.35mm solid #000;';
			}
			if(values[i]=='#TXT_MENSAGEM')	{
				div.style.height 	  = '120mm';
				div.style.borderRight = '0.35mm solid #000;';
			}
			if(values[i]!='')	{
				var divContent = document.createElement("div");
				divContent.style.cssText = "font-size:14px; white-space:normal; font-weight:bold;";
				divContent.innerHTML 	 = '&nbsp;'+values[i];
				if(i!=0 && i!=4 && i<7)	{
					divContent.style.textAlign  = 'center';
				}
				if(i==8)	{
					div.style.backgroundColor 	= '#F0F0F0';
					divContent.style.textAlign  = 'right';
				}
				div.appendChild(divContent);
			}
			divReciboSacado.appendChild(div);
		}
		// Box de autentica��o
		div = document.createElement('div');
		div.style.cssText = 'width:185mm; height:5.17mm; margin-top:2mm; float:left; white-space:nowrap; overflow:hidden; border:0; text-align:right; font-size:10px;';
		div.innerHTML = 'Autentica��o Mec�nica&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
		divBoleto.appendChild(div);
		// Serrilhado
		div = document.createElement('div');
		div.style.cssText = 'width:185mm; height:2mm; margin-top:10mm; float:left; white-space:nowrap; overflow:hidden; border-top:0.3mm dashed #000;';
		divBoleto.appendChild(div);
	}
	//
	var divFichaCompensacao = document.createElement("DIV");
	var width = tpReciboSacado!=1 ? 171 : 185;
	var r = tpReciboSacado!=1 ? 0.95 : 1.08;
	divFichaCompensacao.style.cssText = "height:95mm; float:left; margin-left:1mm; font-size:10px; white-space:nowrap;";
	if(tpReciboSacado==0 || !tpReciboSacado)	{
		divFichaCompensacao.style.borderBottom = '0.3mm dashed #000';
	}
	divFichaCompensacao.style.width   = width+"mm";
	divFichaCompensacao.innerHTML = 
		/* Ficha de compensa��o */
		'		<div class="d1-line" style="height:8mm; border-bottom:0.6mm solid #000; width:'+(width-0.5)+'mm; overflow:hidden;"> '+
		'			<img style="border:0; margin-top:1mm; margin:0; padding:0; width:'+(28*r)+'mm; float:left;" src="#LOGO_CARTEIRA"/> '+
		(tpReciboSacado==0 || !tpReciboSacado ?
		 		/* Bloqueto */
				'	<div class="element" style="width:'+(15*r)+'mm; height:8.9mm; float:left; border-left:0.35mm solid #000; border-right:0.35mm solid #000;"> '+
				'		<div style="font-size:20px; font-weight:bold; margin-left:0.8mm; margin-top:1mm;">#CL_BANCO</div> '+
				'	</div> '+
				'	<div style="font-size:14px; white-space:nowrap; font-weight:bold; overflow:hidden; margin-top:2.5mm;">&nbsp#NR_LINHA_DIGITAVEL</div> '
				:
		 		/* Boleto */
				'	<div class="element" style="width:'+(15*r)+'mm; height:8.9mm; float:left; border-left:0.35mm solid #000; border-right:0.35mm solid #000;"> '+
				'		<div style="font-size:20px; font-weight:bold; margin-left:1mm;">#CL_BANCO</div> '+
				'	</div> '+
				'<div style="font-size:17px; white-space:nowrap; font-weight:bold; overflow:hidden; margin-top:1mm; margin-left:5mm;">&nbsp#NR_LINHA_DIGITAVEL</div> ')+
		'		</div> '+
		'		<div class="d1-line" style="height:6.2mm;"> '+
		'			<div class="element" style="width:'+(130*r)+'mm; float:left; height:100%; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Local de Pagamento</label> '+
		'				<div style="text-align:left; font-weight:bold;">#NM_LOCAL_PAGAMENTO</div> '+
		'			</div> '+
		'			<div class="element" style="width:'+(40*r)+'mm; float:left; height:100%; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Vencimento</label> '+
		'				<div style="width:'+(38*r)+'mm; font-weight:bold; text-align:center;">#DT_VENCIMENTO</div> '+
		'			</div> '+
		'		</div> '+
		'		<div class="d1-line" style="height:6.2mm;"> '+
		'			<div class="element" style="width:'+(130*r)+'mm; height:100%; float:left; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Cedente</label> '+
		'				<div style="text-align:left; font-weight:bold;">#NM_EMPRESA</div> '+
		'			</div> '+
		'			<div class="element" style="width:'+(40*r)+'mm; height:100%; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Ag&ecirc;ncia/C&oacute;digo cedente</label> '+
		'				<div style="width:'+(30*r)+'mm; font-weight:bold; text-align:center;">#NR_AGENCIA/#NR_CEDENTE</div> '+
		'			</div> '+
		'		</div> '+
		'		<div class="d1-line" style="height:6.2mm;"> '+
		'			<div class="element" style="width:'+(24*r)+'mm; height:100%; float:left; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Data de Emiss�o</label> '+
		'				<div style="text-align:center; font-weight:bold; width:100%;">#DT_EMISSAO</div> '+
		'			</div> '+
		'			<div class="element" style="width:'+(41*r)+'mm; height:100%; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;N&ordm; Documento</label> '+
		'				<div style="text-align:center; width: 100%;">#NR_DOCUMENTO</div> '+
		'			</div> '+
		'			<div class="element" style="width:'+(23*r)+'mm; height:100%; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Esp&eacute;cie Documento</label> '+
		'				<div style="text-align:center; font-weight:bold; width: 100%;">#SG_TIPO_DOCUMENTO</div> '+
		'			</div> '+
		'			<div class="element" style="width:'+(12*r)+'mm; height:100%; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Aceite</label> '+
		'				<div style="text-align:center; font-weight:bold; width: 100%;">#NM_ACEITE</div> '+
		'			</div> '+
		'			<div class="element" style="width:'+(28.8*r)+'mm; height:100%; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Data de Processamento</label> '+
		'				<div style="text-align:center; font-weight:bold; width: 100%;">#DT_PROCESSAMENTO</div> '+
		'			</div> '+
		'			<div class="element" style="width:'+(40*r)+'mm; height:100%; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Nosso N�mero</label> '+
		'				<div style="width:'+(38*r)+'mm; font-weight:bold; text-align:center;">#NR_NOSSO_NUMERO</div> '+
		'			</div> '+
		'		</div> '+
		'		<div class="d1-line" style="height:6.2mm;"> '+
		'			<div class="element" style="width:'+(24*r)+'mm; height:100%; float:left; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Uso do Banco</label> '+
		'			</div> '+
		'			<div class="element" style="width:'+(20.5*r)+'mm; height:100%; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Carteira</label> '+
		'				<div style="text-align:center; font-weight:bold; width: 100%;">#SG_CARTEIRA</div> '+
		'			</div> '+
		'			<div class="element" style="width:'+(20.5*r)+'mm; height:100%; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Esp�cie</label> '+
		'				<div style="text-align:center; font-weight:bold; width: 100%;">#NM_MOEDA</div> '+
		'			</div> '+
		'			<div class="element" style="width:'+(35*r)+'mm; height:100%; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Quantidade</label> '+
		'				<div style="text-align:center; font-weight:bold; width: 100%;">#DS_QUANTIDADE</div> '+
		'			</div> '+
		'			<div class="element" style="width:'+(28.8*r)+'mm; height:100%; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Valor</label> '+
		'				<div style="text-align:center; width: 100%;">#CL_VALOR</div> '+
		'			</div> '+
		'			<div class="element" style="width:'+(40*r)+'mm; height:100%; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;(=) Valor do Documento </label> '+
		'				<div style="text-align:right; font-weight:bold; width:'+(38*r)+'mm;">#CL_VALOR</div> '+
		'			</div> '+
		'		</div> '+
		'		<div class="d1-line" style="height:33.5mm;"> '+
		'			<div class="element" style="width:'+(130*r)+'mm; height:31mm; float:left; nowrap: normal;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;Instru&ccedil;&otilde;es de responsabilidade do cedente</label> '+
		'				<div class="" style="text-align:left; height:31mm; white-space: normal; width:'+(120*r)+'mm; ">#TXT_MENSAGEM</div> '+
		'			</div> '+
		'			<div class="element" style="width:'+(40*r)+'mm; height:6.2mm; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;(-) Desconto / Abatimento </label> '+
		'			</div> '+
		'			<div class="element" style="width:'+(40*r)+'mm; height:6.2mm; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;(-) Outras Dedu&ccedil;&otilde;es </label> '+
		'			</div> '+
		'			<div class="element" style="width:'+(40*r)+'mm; height:6.2mm; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;(+) Multa / Mora </label> '+
		'			</div> '+
		'			<div class="element" style="width:'+(40*r)+'mm; height:6.2mm; float:left; border-left:0.35mm solid #000; border-bottom:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;(+) Outros Acr�scimos </label> '+
		'			</div> '+
		'			<div class="element" style="width:'+(40*r)+'mm; height:6.2mm; float:left; border-left:0.35mm solid #000;"> '+
		'				<label class="caption" style="font-size:8px; line-height:2mm; height:2.5mm;">&nbsp;(=) Valor Cobrado </label> '+
		'			</div> '+
		'		</div> '+
		'		<div style="height:12mm; border-bottom:0.6mm solid #000; border-top:0.6mm solid #000; width:'+(width-0.5)+'mm;"> '+
		'			<label class="caption" style="display:inline; width:'+(10*r)+'mm; float:left; font-size:8px; height:9mm;">&nbsp;Sacado:</label> '+
		'			<div style="float:left; height:9mm; width:'+(width-15)+'mm; text-align:left; display:block;">#INF_SACADO</div> '+
		'			<label style="font-size:8px; display:inline; float:left;">&nbsp;Avalista:</label> '+
		'			<div style="text-align:left;">#NM_AVALISTA</div> '+
		'		</div> '+
		'		<div style="height:13mm;"> '+
		'			<div style="width:'+(85*r)+'mm; float:left; margin:1mm 0 0 1mm"> '+
		'				<img src="../barcode?cdBarcode=#NR_CODIGO_BARRAS&showDig=false&height=15&barcodeType=<%=com.tivic.manager.util.BarcodeGenerator._Interleaved2Of5Bean%>" style="height:13mm;"/> '+
		'			</div> '+
		'			<div class="" style="width:'+(50*r)+'mm; height:10mm; float:left; font-size:10px; text-align:center;"> '+
		'				Autentica��o Mecanica/Ficha de Compensa&ccedil;&atilde;o '+
		'			</div> '+
		'		</div> ';
	divBoleto.appendChild(divFichaCompensacao);
	return divBoleto;
}

function getGuiaDeposito()	{
	var divGuiaDeposito = document.createElement("DIV");
	divGuiaDeposito.id  = 'divGuiaDeposito';
	divGuiaDeposito.style.cssText = 'width:190mm; white-space:nowrap; font-family:Arial, Helvetica, sans-serif; padding:0;';
	// Cabe�alho
	  // imagem
	var div = document.createElement("DIV");
	div.style.cssText = 'width:35mm; height:8mm; border:1px solid #000; float:left; white-space:nowrap;';
	divGuiaDeposito.appendChild(div);
	  // texto
	div = document.createElement("DIV");  
	div.style.cssText = 'width:150mm; height:15mm; text-align:center; float:left; white-space:nowrap;';
	div.innerHTML  	= "&nbsp;&nbsp;&nbsp;Caixa R�pido Empresarial - Remessa de Documentos";
	divGuiaDeposito.appendChild(div);
	// Nome da Empresa
	div = document.createElement("DIV");
	div.style.cssText = 'width:189.2mm; height:8mm; float:left; white-space:nowrap; border-left:0.3mm solid #000; border-bottom:0.3mm solid #000; border-right:0.3mm solid #000;';
	var label = document.createElement("label");
	label.style.cssText = "white-space:nowrap; font-size:10px; text-align:center;";
	label.innerHTML  	= "Empresa";
	div.appendChild(label);
	label = document.createElement("label");
	label.style.cssText = "white-space:nowrap; font-weight:bold; font-size:12px; display: block;";
	label.innerHTML  	= "&nbsp;#NM_EMPRESA";
	div.appendChild(label);
	divGuiaDeposito.appendChild(div);
	// N�mero do Lacre
	div = document.createElement("DIV");
	div.style.cssText = 'width:42mm; height:11mm; float:left; white-space:nowrap; border-left:0.3mm solid #000; border-bottom:0.3mm solid #000; border-right:0.3mm solid #000; margin-top:2mm;';
	label = document.createElement("label");
	label.style.cssText = "white-space:nowrap; font-size:10px; width:32mm; text-align:center;";
	label.innerHTML  	= "N� do Lacre";
	div.appendChild(label);
	label = document.createElement("label");
	label.style.cssText = "white-space:nowrap; font-weight:bold; font-size:12px; display:block; margin-top:3mm;";
	label.innerHTML  	= "&nbsp;#NR_LACRE";
	div.appendChild(label);
	divGuiaDeposito.appendChild(div);
	// N�mero da Conta
	div = document.createElement("DIV");
	div.style.cssText = 'width:69mm; height:11mm; float:left; white-space:nowrap; border-left:0.3mm solid #000; border-bottom:0.3mm solid #000; border-right:0.3mm solid #000; margin:2mm 0 0 2mm;';
	label = document.createElement("label");
	label.style.cssText = "white-space:nowrap; font-size:10px; width:32mm; text-align:center;";
	label.innerHTML  	= "Conta corrente";
	div.appendChild(label);
	label = document.createElement("label");
	label.style.cssText = "white-space:nowrap; font-weight:bold; font-size:12px; display: block;";
	label.innerHTML  	= "&nbsp;#TP_OPERACAO - #NR_CONTA";
	div.appendChild(label);
	divGuiaDeposito.appendChild(div);
	// Telefones
	div = document.createElement("DIV");
	div.style.cssText = 'width:73mm; height:11mm; float:left; white-space:nowrap; border-left:0.3mm solid #000; border-bottom:0.3mm solid #000; border-right:0.3mm solid #000; margin:2mm 0 0 2mm';
	label = document.createElement("label");
	label.style.cssText = "white-space:nowrap; font-size:10px; width:32mm; text-align:center;";
	label.innerHTML  	= "Telefones para contato";
	div.appendChild(label);
	label = document.createElement("label");
	label.style.cssText = "white-space:nowrap; font-weight:bold; font-size:12px; display: block; margin-top:3mm;";
	label.innerHTML  	= "&nbsp;#NR_TELEFONE";
	div.appendChild(label);
	divGuiaDeposito.appendChild(div);
	
	// DEP�SITOS
	div = document.createElement("DIV");  
	div.style.cssText = 'width:190mm; height:10mm; font-weight:bold; float:left; white-space:nowrap; margin-top: 3mm;';
	div.innerHTML  	= "1 - Dep�sitos";
	divGuiaDeposito.appendChild(div);
	// Quantidade
	div = document.createElement("DIV");
	label = document.createElement("label");
	div.style.cssText = 'width:22mm; height:8mm; float:left; white-space:nowrap; border-left:0.3mm solid #000; border-bottom:0.3mm solid #000; border-right:0.3mm solid #000;';
	label.style.cssText = "white-space:nowrap; font-size:10px; width:32mm; text-align:center;";
	label.innerHTML  	= "Quantidade";
	div.appendChild(label);
	var div2 = document.createElement("DIV");
	div2.style.cssText = "white-space:nowrap; font-weight:bold; font-size:12px; display: block; width:100%; text-align: center;";
	div2.innerHTML  	= "#QUANTIDADE";
	div.appendChild(div2);
	divGuiaDeposito.appendChild(div);
	// Valor em dinheiro - R$
	div = document.createElement("DIV");
	label = document.createElement("label");
	div.style.cssText = 'width:52mm; height:8mm; float:left; white-space:nowrap; border-left:0.3mm solid #000; border-bottom:0.3mm solid #000; border-right:0.3mm solid #000; margin-left:2mm';
	label.style.cssText = "white-space:nowrap; font-size:10px; width:32mm; text-align:center;";
	label.innerHTML  	= "Valor em dinheiro - R$";
	div.appendChild(label);
	div2 = document.createElement("DIV");
	div2.style.cssText = "white-space:nowrap; font-weight:bold; font-size:12px; display: block; width:100%; text-align: center;";
	div2.innerHTML  	= "#VL_DINHEIRO";
	div.appendChild(div2);
	divGuiaDeposito.appendChild(div);
	// Valor em cheque - R$
	div = document.createElement("DIV");
	label = document.createElement("label");
	div.style.cssText = 'width:53mm; height:8mm; float:left; white-space:nowrap; border-left:0.3mm solid #000; border-bottom:0.3mm solid #000; border-right:0.3mm solid #000; margin-left:2mm';
	label.style.cssText = "white-space:nowrap; font-size:10px; width:32mm; text-align:center;";
	label.innerHTML  	= "Valor em cheque - R$";
	div.appendChild(label);
	div2 = document.createElement("DIV");
	div2.style.cssText = "white-space:nowrap; font-weight:bold; font-size:12px; display: block; width:100%; text-align: center;";
	div2.innerHTML  	= "#VL_CHEQUE";
	div.appendChild(div2);
	divGuiaDeposito.appendChild(div);
	// Valor total - R$
	div = document.createElement("DIV");
	label = document.createElement("label");
	div.style.cssText = 'width:54mm; height:8mm; float:left; white-space:nowrap; border-left:0.3mm solid #000; border-bottom:0.3mm solid #000; border-right:0.3mm solid #000; margin-left:2mm';
	label.style.cssText = "white-space:nowrap; font-size:10px; width:32mm; text-align:center;";
	label.innerHTML  	= "Valor total - R$";
	div.appendChild(label);
	div2 = document.createElement("DIV");
	div2.style.cssText = "white-space:nowrap; font-weight:bold; font-size:12px; display: block; width:100%; text-align: center;";
	div2.innerHTML  	= "#VL_TOTAL";
	div.appendChild(div2);
	divGuiaDeposito.appendChild(div);
	// PAGAMENTOS
	div = document.createElement("DIV");  
	div.style.cssText = 'width:190mm; height:10mm; font-weight:bold; float:left; white-space:nowrap; margin-top: 3mm;';
	div.innerHTML  	= "2 - Pagamentos";
	divGuiaDeposito.appendChild(div);
	var labels = ['Tipo de Documento','Cheques pre-datados para Cust�dia Cau��o/Simples','DARF','Disquete referente � carteira distributiva/SAD',
				  'DOC - Documento de Cr�dito','FAC - Ficha de Atualiza��o Cadastral', 'FGTS','Guia de Contribui��o Sindical','INSS','SIVAT - Ordem de Pagamento',
				  'REDECARD / CREDICARD','T�tulo / Bloqueto da CAIXA', 'T�tulo / Bloqueto de outros bancos', 'T�tulos de cr�dito para inclus�o no SICOB',
				  'T�tulo referentes ao processo de protesto', 'Outros'];
	var values = [];
	for(var i=0; i<labels.length; i++)	{
		var cssTitle = (i==0?'font-size:11px; font-weight:bold;':'');
		// Coluna 1
		div = document.createElement("DIV");
		div.style.cssText 	= 'height:7mm; width:40mm; float:left; white-space: normal; margin-top:1mm;';
		label = document.createElement("label");
		label.style.cssText = "font-size:9px;";
		label.innerHTML  	= labels[i];
		div.appendChild(label);
		divGuiaDeposito.appendChild(div);
		// Coluna 2
		div = document.createElement("DIV");
		div.style.cssText 	= 'height:7mm; width:17mm; float:left; border-left:0.3mm solid #000; border-bottom:0.3mm solid #000; margin-top:1mm; '+cssTitle;
		label = document.createElement("label");
		label.innerHTML = (i==0) ? 'Quantidade' : '';
		div.appendChild(label);
		divGuiaDeposito.appendChild(div);
		// Coluna 3
		div = document.createElement("DIV");
		div.style.cssText 	= 'height:7mm; width:27mm; float:left; border-left:0.3mm solid #000; border-bottom:0.3mm solid #000; margin-top:1mm; '+cssTitle;
		label = document.createElement("label");
		label.innerHTML = (i==0) ? 'Valor em dinheiro' : '';
		div.appendChild(label);
		divGuiaDeposito.appendChild(div);
		// Coluna 4
		div = document.createElement("DIV");
		div.style.cssText 	= 'height:7mm; width:28mm; float:left; border-left:0.3mm solid #000; border-bottom:0.3mm solid #000; margin-top:1mm; '+cssTitle;
		label = document.createElement("label");
		label.innerHTML = (i==0) ? 'Valor em cheque' : '';
		div.appendChild(label);
		divGuiaDeposito.appendChild(div);
		// Coluna 5
		div = document.createElement("DIV");
		div.style.cssText 	= 'height:7mm; width:44mm; float:left; border-left:0.3mm solid #000; border-bottom:0.3mm solid #000; margin-top:1mm; '+cssTitle;
		label = document.createElement("label");
		label.innerHTML = (i==0) ? 'Cheque<br/>Banco - N� do cheque' : '';
		div.appendChild(label);
		divGuiaDeposito.appendChild(div);
		// Coluna 6
		div = document.createElement("DIV");
		div.style.cssText 	= 'height:7mm; width:31.8mm; float:left; border-left:0.3mm solid #000; border-bottom:0.3mm solid #000; border-right:0.3mm solid #000; margin-top:1mm; '+cssTitle;
		label = document.createElement("label");
		label.innerHTML = (i==0) ? 'Valor Total' : '';
		div.appendChild(label);
		divGuiaDeposito.appendChild(div);
	}
	return divGuiaDeposito;
}