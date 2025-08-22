var weekDays = ['Domingo', 'Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado'];
var months = ['Janeiro',  'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];
var countDaysMonths = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];	

var _TP_COMPROMISSO = 0;
var _TP_TAREFA = 1;

var _ST_PENDENTE = 0;
var _ST_CONCLUIDO = 1;
var _ST_CANCELADO = 2;

function getNextDay(date) {
	var day = date.getDate();
	var month = date.getMonth();
	var year = date.getFullYear() - 2000;
	var countDaysMonth = countDaysMonths[month];
	if (month == 1 && year%4 == 0)
		countDaysMonth++;
		
	if (day + 1 <= countDaysMonth)
		day++;
	else {
		if (month == 11) {
			month = 0;
			year++;
		}
		else
			month++;
		day = 1;
	}
	return stringToDate(day + '/' + (month + 1) + '/' + year);
}

function getPreviousDay(date) {
	var day = date.getDate();
	var month = date.getMonth();
	var year = date.getFullYear() - 2000;
	if (day - 1 == 0) {
		if (month == 0) {
			month = 11;
			year--;
		}
		else
			month--;
		day = countDaysMonths[month];
		if (month == 1 && year%4 == 0)
			day++;
	}
	else
		day--;
	return stringToDate(day + '/' + (month + 1) + '/' + year);
}

function createPanelAgdDia(agendamento, plotPlace, onClickEvent) {
	if (agendamento != null) {
		var dtInicial = stringToDate(agendamento['DT_INICIAL']);
		var hours = dtInicial.getHours();
		var minutes = dtInicial.getMinutes();
		var panel = document.createElement("div");
		panel.className = 'panel';
		panel.cdAgendamento = agendamento['CD_AGENDAMENTO'];
		panel.onClickEvent = onClickEvent;
		panel.onclick = function() {
			if (this.onClickEvent!=null)
				this.onClickEvent();
			else if (parent.openAgendamento)
				parent.openAgendamento(this.cdAgendamento);
		}
		panel.id = 'panelAgdDay_' + agendamento['CD_AGENDAMENTO'];
		panel.type = 'panelAgd';
		panel.style.width = (parseInt(plotPlace.style.width, 10) - 56) + "px";
		panel.style.top = ((hours * 40) + ((minutes * 40) / 60)) + "px";

		var bar = document.createElement("div");
		bar.className = 'bar';
		panel.appendChild(bar);

		var captionTitle = document.createElement("div");
		captionTitle.className = 'title';
		captionTitle.innerHTML = agendamento['NM_AGENDAMENTO'] + " - ";
		panel.appendChild(captionTitle);

		var captionHour = document.createElement("div");
		captionHour.className = 'hour';
		captionHour.innerHTML = agendamento['DT_INICIAL'].substring(11, 16);
		panel.appendChild(captionHour);

		return panel;
	}
	else
		return null;
}

function createPanelAgdSemana(agendamento, plotPlace, onClickEvent) {
	if (agendamento != null) {
		var widthAreaUtil = parseInt(plotPlace.style.width, 10) - 16;
		var widthAreaUtilDia = (widthAreaUtil - 38) / 7;
		widthAreaUtilDia = parseInt(widthAreaUtilDia, 10) - 2;
	
		var dtInicial = stringToDate(agendamento['DT_INICIAL']);
		var hours = dtInicial.getHours();
		var minutes = dtInicial.getMinutes();
		var panel = document.createElement("div");
		panel.cdAgendamento = agendamento['CD_AGENDAMENTO'];
		panel.register = agendamento;
		panel.onClickEvent = onClickEvent;
		panel.onclick = function() {
			if (this.onClickEvent!=null)
				this.onClickEvent();
		}
		panel.onmouseover = function(evt) {
		};
		panel.onmouseout = function() {
		};
		panel.id = 'panelAgdWeek_' + agendamento['CD_AGENDAMENTO'];
		panel.className = 'panel';
		panel.type = 'panelAgd';
		panel.style.width = widthAreaUtilDia + "px";
		panel.style.left = (36 + (dtInicial.getDay() * (widthAreaUtilDia + 3))) + "px";
		panel.style.top = ((hours * 40) + ((minutes * 40) / 60)) + "px";

		var bar = document.createElement("div");
		bar.className = 'bar';
		panel.appendChild(bar);

		var captionHour = document.createElement("div");
		captionHour.className = 'hour';
		captionHour.innerHTML = agendamento['DT_INICIAL'].substring(11, 16);
		panel.appendChild(captionHour);

		return panel;
	}
	else
		return null;
}

function createPanelAgdSemanaWork(agendamento, plotPlace, onClickEvent) {
	if (agendamento != null) {
		var widthAreaUtil = parseInt(plotPlace.style.width, 10) - 16;
		var widthAreaUtilDia = (widthAreaUtil - 38) / 5;
		widthAreaUtilDia = parseInt(widthAreaUtilDia, 10) - 2;
	
		var dtInicial = stringToDate(agendamento['DT_INICIAL']);
		var hours = dtInicial.getHours();
		var minutes = dtInicial.getMinutes();
		var panel = document.createElement("div");
		panel.cdAgendamento = agendamento['CD_AGENDAMENTO'];
		panel.register = agendamento;
		panel.onClickEvent = onClickEvent;
		panel.onclick = function() {
			if (this.onClickEvent!=null)
				this.onClickEvent();
		}
		panel.onmouseover = function(evt) {
		};
		panel.onmouseout = function() {
		};
		panel.id = 'panelAgdWeekWork_' + agendamento['CD_AGENDAMENTO'];
		panel.className = 'panel';
		panel.type = 'panelAgd';
		panel.style.width = (widthAreaUtilDia-1) + "px";
		panel.style.left = (36 + ((dtInicial.getDay()-1) * (widthAreaUtilDia + 3))) + "px";
		panel.style.top = ((hours * 40) + ((minutes * 40) / 60)) + "px";

		var bar = document.createElement("div");
		bar.className = 'bar';
		panel.appendChild(bar);

		var captionHour = document.createElement("div");
		captionHour.className = 'hour';
		captionHour.innerHTML = agendamento['DT_INICIAL'].substring(11, 16);
		panel.appendChild(captionHour);

		return panel;
	}
	else
		return null;
}

function createPanelAgdMes(agendamento, onClickEvent) {
	if (agendamento != null) {
		var panel = document.createElement("div");
		panel.cdAgendamento = agendamento['CD_AGENDAMENTO'];
		panel.onClickEvent = onClickEvent;
		panel.onclick = function() {
			if (this.onClickEvent!=null)
				this.onClickEvent();
			else if (parent.openAgendamento)
				parent.openAgendamento(this.cdAgendamento);
		}
		panel.id = 'panelAgdMonth_' + agendamento['CD_AGENDAMENTO'];
		panel.className = 'panel';

		var panelIntern = document.createElement("div");
		panelIntern.className = 'intern';

		var label = document.createElement("div");
		label.className = 'label';

		var caption = document.createElement("div");
		caption.className = 'caption';
		caption.innerHTML = agendamento['NM_AGENDAMENTO'];
		label.appendChild(caption);
		panelIntern.appendChild(label);

		panel.appendChild(panelIntern);

		return panel;
	}
	else
		return null;
}

function createCellMonth(date, widthBase, heightBase, className) {
	var cellMesBase = document.createElement("div");
	cellMesBase.className = 'cellDay';

	cellMesBase.style.width = widthBase + "px";
	cellMesBase.style.height = heightBase + "px";
	
	var cellMes = document.createElement("div");
	cellMes.className = className;

	if (className=='variant1' || className=='variant3')
		widthBase--;
	if (className=='variant1' || className=='variant2')
		heightBase--;

	cellMes.style.width = widthBase + "px";
	cellMes.style.height = heightBase + "px";

	var headerMes = document.createElement("div");
	headerMes.className = 'header';
	headerMes.style.width = widthBase + "px";
	
	var headerMesLabel = document.createElement("label");
	headerMesLabel.className = 'caption';
	headerMesLabel.style.width = (widthBase) + "px";
	headerMesLabel.innerHTML = date.getDate();
	
	var contentMes = document.createElement("div");
	contentMes.id = 'day_' + (date.getDate()<10 ? '0' : '') + date.getDate() + '_' + (date.getMonth()+1 < 10 ? '0' : '') + (date.getMonth()+1) + '_' + (date.getFullYear() - 2000);
	contentMes.className = 'content';
	contentMes.style.width = widthBase + "px";
	contentMes.style.height = (heightBase - 16) + "px";
	
	headerMes.appendChild(headerMesLabel);
	
	cellMes.appendChild(headerMes);
	cellMes.appendChild(contentMes);

	cellMesBase.appendChild(cellMes);

	return cellMesBase;
}

function miAgendamentoOnClick(options) {
	var cdAgendamento = options==null || options.cdAgendamento==null ? 0 : options.cdAgendamento;
	createWindow('jAgendamento', {caption: 'Agendamento', width: 660, height: 400, top: 75, contentUrl: 'agendamento.jsp?cdAgendamento='+cdAgendamento});
}

function miTipoAgendamentoOnClick() {
	createWindow('jTipoAgendamento', {caption: 'Cadastro e Manutenção de Tipos de Agendamentos', width: 420, height: 350, contentUrl: 'tipo_agendamento.jsp'});
}

function miAgendaOnClick() {
	createWindow('jAgenda', {caption: 'Lançamento de Grupos de Reuniões Ordinárias', width: 670, height: 352, contentUrl: '../agd/agenda_pronto.jsp', noDestroyWindow:true, noDrag:true});
}

function miMailingContaConviteOnClick() {	
	createWindow('jMailingContaConvite', {caption: 'Agendamento de Reuniões', width: 700, height: 435, contentUrl: '../agd/MailingContaConvite_pronto.jsp', noDestroyWindow:true, noDrag: true});
}

function miAssuntoOnClick() {
	createWindow('jAssunto', {caption: 'Lançamento de Temas', width: 700, height: 435, contentUrl: '../agd/assunto_pronto.jsp', noDestroyWindow:true, noDrag: true});
}

function miViewMailingContaConviteOnClick() {
	createWindow('jViewMailingContaConvite', {caption: 'Acompanhamento de Reuniões', width: 690, height: 425, contentUrl: '../agd/view_MailingContaConvite_pronto.jsp', noDestroyWindow:true, noDrag:true});
}

function miReuniaoOnClick() {	
	createWindow('jReuniao', {caption: 'Agendamento de Reuniões', width: 700, height: 435, contentUrl: '../agd/reuniao_pronto.jsp', noDestroyWindow:true, noDrag: true});
}

function miViewReuniaoOnClick() {	
	createWindow('jViewReuniao', {caption: 'Seleção de Reunião', width: 700, height: 465, contentUrl: '../agd/view_reuniao_pronto.jsp', noDestroyWindow:true, noDrag: true});
}

function miMailingContaConvitesOnClick() {
	FormFactory.createFormWindow('jMailingContaConvite', {caption: "Configuração de conta de e-mail para envio de convites de reuniões",
										  width: 500,
										  height: 178,
										  noDrag: true,
										  modal: true,
										  id: 'mailingContaConvite',
										  unitSize: '%',
										  hiddenFields: [{id: 'cdConta', value: 0, reference: 'cdConta'}, 
														 {id: 'dsAssinatura', value: '', reference: 'dsAssinatura'}],
										  lines: [[{id:'nmConta', reference: 'nmConta', idForm: 'mailingContaConvite', label: 'Nome Conta', width: 100, style: '', datatype: 'STRING', maxLength: 100}],
												  [{id:'nmEmail', reference: 'nmEmail', idForm: 'mailingContaConvite', label: 'E-mail', width: 48, maxLength: 256,type: 'text', style: ''}, 
												   {id:'nmLogin', reference: 'nmLogin', idForm: 'mailingContaConvite', label: 'Login', width: 32, maxLength: 256,type: 'text', style: ''}, 
												   {id:'nmSenha', reference: 'nmSenha', idForm: 'mailingContaConvite', label: 'Senha', width: 20, maxLength: 20,type: 'text', style: ''}],
												  [{id:'nmServidorPop', reference: 'nmServidorPop', idForm: 'mailingContaConvite', label: 'Servidor POP', width: 48, maxLength: 256,type: 'text', style: ''}, 
												   {id:'nrPortaPop', reference: 'nrPortaPop', idForm: 'mailingContaConvite', label: 'Porta POP', width: 12, maxLength: 10, type: 'text', style: ''}, 
												   {id:'lgSslPop', value: 1, reference: 'lgSslPop', idForm: 'mailingContaConvite', label: 'SSL', width: 10, type: 'checkbox', style: ''}, 
												   {id:'lgAutenticacaoPop', value: 1, reference: 'lgAutenticacaoPop', idForm: 'mailingContaConvite', width: 30, label: 'Requer autententicação?', type: 'checkbox', style: ''}],
												  [{id:'nmServidorSmtp', reference: 'nmServidorSmtp', idForm: 'mailingContaConvite', label: 'Servidor SMTP', width: 48, maxLength: 256,type: 'text', style: ''}, 
												   {id:'nrPortaSmtp', reference: 'nrPortaSmtp', idForm: 'mailingContaConvite', label: 'Porta SMTP', width: 12, maxLength: 10, type: 'text', style: ''}, 
												   {id:'lgSslSmtp', value: 1, reference: 'lgSslSmtp', idForm: 'mailingContaConvite', label: 'SSL', width: 10, type: 'checkbox', style: ''}, 
												   {id:'lgAutenticacaoSmtp', value: 1, reference: 'lgAutenticacaoSmtp', idForm: 'mailingContaConvite', width: 30, label: 'Requer autententicação?', type: 'checkbox', style: ''}],
												  [{type: 'space', width: 60}, 
												   {id:'btnCancelMailingContaConvite', type:'button', label:'Retornar', width:15, onClick: function(){
																																closeWindow('jMailingContaConvite');
																															}}, 
												   {id:'btnSaveMailingContaConvite', type:'button', label:'Gravar Configuração', width:25, 
												    onClick: function(){
																var fields = [[$("nmConta"), 'Nome Conta', VAL_CAMPO_NAO_PREENCHIDO],
																			  [$("nmEmail"), 'E-mail', VAL_CAMPO_NAO_PREENCHIDO],
																			  [$("nmLogin"), 'Login', VAL_CAMPO_NAO_PREENCHIDO], 
																			  [$("nmSenha"), 'Senha', VAL_CAMPO_NAO_PREENCHIDO], 
																			  [$("nmServidorSmtp"), 'Servidor SMTP', VAL_CAMPO_NAO_PREENCHIDO], 
																			  [$("nrPortaSmtp"), 'Porta SMTP', VAL_CAMPO_NAO_PREENCHIDO]];	
																if (validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmConta')) {
																	var constructionMailingConta = 'cdConta:int, nmConta:String, nmEmail:String, nmServidorPop:String, nmServidorSmtp:String, dsAssinatura:String, nrPortaPop:int, nrPortaSmtp:int, lgAutenticacaoPop:int, lgAutenticacaoSmtp:int, nmLogin:String, nmSenha:String, lgSslPop:int, lgSslSmtp:int';
																	getPage("POST", "$('cdConta').processSaveConta", "../methodcaller?className=com.tivic.manager.agd.AgendaServices"+
																						  "&method=saveMailingContaForConvites(new com.tivic.manager.crm.MailingConta(" + constructionMailingConta + "):com.tivic.manager.crm.MailingConta)", mailingContaConviteFields);	
																}
															 }}]],
										  focusField:'nmConta'});
	enableTabEmulation($('btnSaveMailingContaConvite'), $('jMailingContaConvite'));
	$('cdConta').processSaveConta = function(content) {
		var ret = processResult(content, 'Conta de e-mail para envio de convites configurado com sucesso!', {noMessage: false, caption: 'officePronto', noDetailButton: true});
	};
	$('cdConta').processLoadConta = function(content) {
									var mailingConta = null;
									try { mailingConta = eval('(' + content + ')'); } catch(e) { }
									loadFormFields(['mailingContaConvite']);
									if (mailingConta!=null)
										loadFormRegister(mailingContaConviteFields, mailingConta, true, {object: true});
									else
										clearFields(mailingContaConviteFields);
								 };
	getPage("GET", "$('cdConta').processLoadConta", "../methodcaller?className=com.tivic.manager.agd.AgendaServices"+
						  "&method=getMailingContaForConvites()");	
}

function miMailingContaAtasReunioesOnClick() {
	FormFactory.createFormWindow('jMailingContaAta', {caption: "Configuração de conta de e-mail para envio de atas de reuniões",
										  width: 500,
										  height: 178,
										  noDrag: true,
										  modal: true,
										  id: 'mailingContaAta',
										  unitSize: '%',
										  hiddenFields: [{id: 'cdConta', value: 0, reference: 'cdConta'}, 
														 {id: 'dsAssinatura', value: '', reference: 'dsAssinatura'}],
										  lines: [[{id:'nmConta', reference: 'nmConta', idForm: 'mailingContaAta', label: 'Nome Conta', width: 100, style: '', datatype: 'STRING', maxLength: 100}],
												  [{id:'nmEmail', reference: 'nmEmail', idForm: 'mailingContaAta', label: 'E-mail', width: 48, maxLength: 256,type: 'text', style: ''}, 
												   {id:'nmLogin', reference: 'nmLogin', idForm: 'mailingContaAta', label: 'Login', width: 32, maxLength: 256,type: 'text', style: ''}, 
												   {id:'nmSenha', reference: 'nmSenha', idForm: 'mailingContaAta', label: 'Senha', width: 20, maxLength: 20,type: 'text', style: ''}],
												  [{id:'nmServidorPop', reference: 'nmServidorPop', idForm: 'mailingContaAta', label: 'Servidor POP', width: 48, maxLength: 256,type: 'text', style: ''}, 
												   {id:'nrPortaPop', reference: 'nrPortaPop', idForm: 'mailingContaAta', label: 'Porta POP', width: 12, maxLength: 10, type: 'text', style: ''}, 
												   {id:'lgSslPop', value: 1, reference: 'lgSslPop', idForm: 'mailingContaAta', label: 'SSL', width: 10, type: 'checkbox', style: ''}, 
												   {id:'lgAutenticacaoPop', value: 1, reference: 'lgAutenticacaoPop', idForm: 'mailingContaAta', width: 30, label: 'Requer autententicação?', type: 'checkbox', style: ''}],
												  [{id:'nmServidorSmtp', reference: 'nmServidorSmtp', idForm: 'mailingContaAta', label: 'Servidor SMTP', width: 48, maxLength: 256,type: 'text', style: ''}, 
												   {id:'nrPortaSmtp', reference: 'nrPortaSmtp', idForm: 'mailingContaAta', label: 'Porta SMTP', width: 12, maxLength: 10, type: 'text', style: ''}, 
												   {id:'lgSslSmtp', value: 1, reference: 'lgSslSmtp', idForm: 'mailingContaAta', label: 'SSL', width: 10, type: 'checkbox', style: ''}, 
												   {id:'lgAutenticacaoSmtp', value: 1, reference: 'lgAutenticacaoSmtp', idForm: 'mailingContaAta', width: 30, label: 'Requer autententicação?', type: 'checkbox', style: ''}],
												  [{type: 'space', width: 60}, 
												   {id:'btnCancelmailingContaAta', type:'button', label:'Retornar', width:15, onClick: function(){
																																closeWindow('jMailingContaAta');
																															}}, 
												   {id:'btnSavemailingContaAta', type:'button', label:'Gravar Configuração', width:25, 
												    onClick: function(){
																var fields = [[$("nmConta"), 'Nome Conta', VAL_CAMPO_NAO_PREENCHIDO],
																			  [$("nmEmail"), 'E-mail', VAL_CAMPO_NAO_PREENCHIDO],
																			  [$("nmLogin"), 'Login', VAL_CAMPO_NAO_PREENCHIDO], 
																			  [$("nmSenha"), 'Senha', VAL_CAMPO_NAO_PREENCHIDO], 
																			  [$("nmServidorSmtp"), 'Servidor SMTP', VAL_CAMPO_NAO_PREENCHIDO], 
																			  [$("nrPortaSmtp"), 'Porta SMTP', VAL_CAMPO_NAO_PREENCHIDO]];	
																if (validateFields(fields, true, 'Os campos marcados devem ser preenchidos!', 'nmConta')) {
																	var constructionMailingConta = 'cdConta:int, nmConta:String, nmEmail:String, nmServidorPop:String, nmServidorSmtp:String, dsAssinatura:String, nrPortaPop:int, nrPortaSmtp:int, lgAutenticacaoPop:int, lgAutenticacaoSmtp:int, nmLogin:String, nmSenha:String, lgSslPop:int, lgSslSmtp:int';
																	getPage("POST", "$('cdConta').processSaveConta", "../methodcaller?className=com.tivic.manager.agd.AgendaServices"+
																						  "&method=saveMailingContaForAtasReunioes(new com.tivic.manager.crm.MailingConta(" + constructionMailingConta + "):com.tivic.manager.crm.MailingConta)", mailingContaAtaFields);	
																}
															 }}]],
										  focusField:'nmConta'});
	enableTabEmulation($('btnSavemailingContaAta'), $('jMailingContaAta'));
	$('cdConta').processSaveConta = function(content) {
		var ret = processResult(content, 'Conta de e-mail para envio de atas de reuniões configurado com sucesso!', {caption: 'officePronto', noDetailButton: true});
	};
	$('cdConta').processLoadConta = function(content) {
									var mailingConta = null;
									try { mailingConta = eval('(' + content + ')'); } catch(e) { }
									loadFormFields(['mailingContaAta']);
									if (mailingConta!=null)
										loadFormRegister(mailingContaAtaFields, mailingConta, true, {object: true});
									else
										clearFields(mailingContaAtaFields);
								 };
	getPage("GET", "$('cdConta').processLoadConta", "../methodcaller?className=com.tivic.manager.agd.AgendaServices"+
						  "&method=getMailingContaForAtasReunioes()");	
}