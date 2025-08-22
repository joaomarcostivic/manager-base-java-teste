function miPtcProcessoOnClick(options) {
	createWindow('jProcesso', {caption: 'Manuais', width: 600, height: 355, contentUrl: '../ptc/processo.jsp', noDestroyWindow:true, noDrag:true});
	if(getFrameContentById('jProcesso') != null && getFrameContentById('jProcesso').clearFormProcesso)
		getFrameContentById('jProcesso').clearFormProcesso();
}

function miFaseOnClick() {
	FormFactory.createQuickForm('jFase', {caption: 'Manutenção de Fases do Documento', width: 550, height: 500,
								  //quickForm
								  id: "ptc_fase",
								  classDAO: 'com.tivic.manager.ptc.FaseDAO',
								  keysFields: ['cd_fase'], unitSize: '%',
								  constructorFields: [{reference: 'cd_fase', type: 'int'},
												      {reference: 'nm_fase', type: 'java.lang.String'},
												      {reference: 'id_fase', type: 'java.lang.String'}],
								  gridOptions: {columns: [{reference: 'nm_fase', label: 'Fase'}],
												strippedLines: true, columnSeparator: false, lineSeparator: false},
								  lines: [[{reference: 'nm_fase', label: 'Fase', width:100, maxLength:50}]]});

}

function miTipoPendenciaOnClick() {
	FormFactory.createQuickForm('jFase', {caption: 'Manutenção de Tipo de Pendência', width: 400, height: 350,
								  //quickForm
								  id: "ptc_tipo_pendencia",
								  classDAO: 'com.tivic.manager.ptc.TipoPendenciaDAO',
								  keysFields: ['cd_tipo_pendencia'], unitSize: '%',
								  constructorFields: [{reference: 'cd_tipo_pendencia', type: 'int'},
												      {reference: 'nm_tipo_pendencia', type: 'java.lang.String'},
												      {reference: 'id_tipo_pendencia', type: 'java.lang.String'},
												      {reference: 'st_tipo_pendencia', type: 'int'}],
								  gridOptions: {columns: [{reference: 'nm_tipo_pendencia', label: 'Tipo de Pendência'}],
												strippedLines: true, columnSeparator: false, lineSeparator: false},
								  lines: [[{reference: 'nm_tipo_pendencia', label: 'Tipo de Pendência', width:100, maxLength:50}]]});

}

function miSituacaoDocumentoOnClick() {
	FormFactory.createQuickForm('jSituacaoDocumento', {caption: 'Manutenção de Situações de Documento', width: 400, height: 350,
								  //quickForm
								  id: "ptc_situacao_documento",
								  classDAO: 'com.tivic.manager.ptc.SituacaoDocumentoDAO',
								  keysFields: ['cd_situacao_documento'], unitSize: '%',
								  constructorFields: [{reference: 'cd_situacao_documento', type: 'int'},
												      {reference: 'nm_situacao_documento', type: 'java.lang.String'}],
								  gridOptions: {columns: [{reference: 'nm_situacao_documento', label: 'Situação'}],
												strippedLines: true, columnSeparator: false, lineSeparator: false},
								  lines: [[{reference: 'nm_situacao_documento', label: 'Situação', width:100, maxLength:50}]]});

}

function miTipoJuntadaOnClick() {
	FormFactory.createQuickForm('jTipoJuntada', {id: 'ptc_tipo_juntada', classDAO: 'com.tivic.manager.ptc.TipoJuntadaDAO', caption: 'Tipo de Juntada', width: 500, height: 350,
								  keysFields: ['cd_tipo_juntada'], unitSize: '%',
								  constructorFields: [{reference: 'cd_tipo_juntada', type: 'int'},
													  {reference: 'nm_tipo_juntada', type: 'java.lang.String'},
													  {reference: 'id_tipo_juntada', type: 'java.lang.String'}],
								  gridOptions: {columns: [{reference: 'nm_tipo_juntada', label: 'Tipo de Juntada'}],
								                strippedLines: true, columnSeparator: false, lineSeparator: false},
								  lines: [[{reference: 'nm_tipo_juntada', label: 'Tipo de Juntada', width:80, maxLength:50},
										   {reference: 'id_tipo_juntada', label: 'Código', width:20, maxLength:10}]]});
}

function miTipoAnexoOnClick() {
	FormFactory.createQuickForm('jTipoAnexo', {id: 'ptc_tipo_anexo', classDAO: 'com.tivic.manager.ptc.TipoAnexoDAO', caption: 'Tipo de Anexo', width: 500, height: 350,
								  keysFields: ['cd_tipo_anexo'], unitSize: '%',
								  constructorFields: [{reference: 'cd_tipo_anexo', type: 'int'},
													  {reference: 'nm_tipo_anexo', type: 'java.lang.String'}],
								  gridOptions: {columns: [{reference: 'nm_tipo_anexo', label: 'Tipo de Anexo'}],
								                strippedLines: true, columnSeparator: false, lineSeparator: false},
								  lines: [[{reference: 'nm_tipo_anexo', label: 'Tipo de Anexo', width:100, maxLength:50}]]});
}

function miTipoDecisaoOnClick() {
	FormFactory.createQuickForm('jTipoDecisao', {id: 'ptc_tipo_decisao', classDAO: 'com.tivic.manager.ptc.TipoDecisaoDAO', caption: 'Tipo de Decisão', width: 500, height: 350,
								  keysFields: ['cd_tipo_decisao'], unitSize: '%',
								  constructorFields: [{reference: 'cd_tipo_decisao', type: 'int'},
													  {reference: 'nm_tipo_decisao', type: 'java.lang.String'},
													  {reference: 'id_tipo_decisao', type: 'java.lang.String'}],
								  gridOptions: {columns: [{reference: 'nm_tipo_decisao', label: 'Tipo de Decisão'}],
								                strippedLines: true, columnSeparator: false, lineSeparator: false},
								  lines: [[{reference: 'nm_tipo_decisao', label: 'Tipo de Decisão', width:80, maxLength:50},
										   {reference: 'id_tipo_decisao', label: 'Código', width:20, maxLength:10}]]});
}

function miTipoDocumentoProtocoloOnClick()	{
	createWindow('jTipoDocumentoOnClick2',{caption:'Manutenção de Tipos de Documento', width:700,height:420,contentUrl:'../ptc/tipo_documento.jsp'});
}

function miSolicitacaoOnClick(options)	{
	createWindow('jSolicitacaoOnClick',{caption:'Solicitação de Documentos', top: options==null ? 80 : options.top,
				 width:750, height:410,contentUrl:'../ptc/solicitacao.jsp'});
}

function miGestaoDocumentoOnClick(options)	{
	var cdEmpresa        = options==null || options.cdEmpresa==null ? 0 : options.cdEmpresa;
	var cdSetor          = options==null || options.cdSetor==null ? 0 : options.cdSetor;
	var tpOpcaoRelatorio = options==null || options.tpOpcaoRelatorio==null ? -1 : options.tpOpcaoRelatorio;
	createWindow('jGestaoDocumentoOnClick',{caption:'Gestão de Documentos', width: 902, height: 478, 
	                                        contentUrl:'../ptc/gestao_documento.jsp?cdEmpresa='+cdEmpresa+'&cdSetor='+cdSetor+'&tpOpcaoRelatorio='+tpOpcaoRelatorio});
}

function miDocumentoOnClick(options)	{
	var cdDocumento     = options==null || options.cdDocumento==null ? 0 : options.cdDocumento;
	var immediatePrint  = options==null || options.immediatePrint==null ? 0 : options.immediatePrint;
	var closeAfterPrint = options==null || options.closeAfterPrint==null ? 0 : options.closeAfterPrint;
	var cdEmpresa       = options==null || options.cdEmpresa==null ? 0 : options.cdEmpresa;
	var cdSetor         = options==null || options.cdSetor==null ? 0 : options.cdSetor;
	var cdContrato      = options==null || options.cdContrato==null ? 0 : options.cdContrato;
	var cdPessoa        = options==null || options.cdPessoa==null ? 0 : options.cdPessoa;
	var noDestroyWindow = options==null || options.noDestroyWindow==null ? false : options.noDestroyWindow;
	createWindow('jDocumento',{caption:'Manutenção de Documentos', top: options==null ? 80 : options.top,width:751, height:485,noDestroyWindow:noDestroyWindow,  
				 contentUrl:'../ptc/documento.jsp?immediatePrint='+immediatePrint + 
				 			'&cdDocumento='+cdDocumento +'&closeAfterPrint='+closeAfterPrint+
							'&cdEmpresa='+cdEmpresa+'&cdSetor='+cdSetor+
							'&cdPessoa='+cdPessoa+'&cdContrato='+cdContrato});
	
	if(getFrameContentById('jDocumento') != null && getFrameContentById('jDocumento').btnNewDocumentoOnClick)	{
		getFrameContentById('jDocumento').btnNewDocumentoOnClick();
		if(cdDocumento>0)
			getFrameContentById('jDocumento').loadDocumento(null, cdDocumento);
	}
}

function miComunicadoInternoOnClick() {
	createWindow('jComunicadoInterno', {caption: 'Lançamento de Comunicados Internos', width: 555, height: 320, contentUrl: '../ptc/comunicado_interno.jsp', noDestroyWindow:true, noDrag:true});
	if(getFrameContentById('jComunicadoInterno') != null && getFrameContentById('jComunicadoInterno').clearFormDocumento)
		getFrameContentById('jComunicadoInterno').clearFormDocumento();
}

function miViewComunicadosInternosOnClick() {
	createWindow('jViewComunicadoInterno', {caption: 'Meus Comunicados Internos', width: 560, height: 332, contentUrl: '../ptc/comunicado_interno_view.jsp', noDestroyWindow:true, noDrag:true});
}

function verifyCis(content, options) {
	if (content==null && $('cdUsuario').value > 0) {
		var searchFields = [];
		var objects = 'item1=sol.dao.ItemComparator(const C.cd_usuario:String, const ' + $('cdUsuario').value + ':String, const <%=sol.dao.ItemComparator.EQUAL%>:int, const <%=java.sql.Types.INTEGER%>:int);';
		var execute = 'criterios.add(*item1:java.lang.Object);';
		getPage('POST', 'verifyCis', 
				'../methodcaller?className=com.tivic.manager.ptc.DocumentoServices' +
				(objects!='' ? '&objects=criterios=java.util.ArrayList();'+objects:'&objects=criterios=java.util.ArrayList();')+
				(execute!='' ? '&execute='+execute:'')+
				'&method=findCountNewDocComunicadosInternos(*criterios:java.util.ArrayList)',
				searchFields, null, null, null);
	}
	else {
		var countCis = null;
		try {countCis = parseInt(content, 10); } catch(e) {}
        if (countCis!=null && countCis>0) {
			closeWindow('jViewComunicadoInterno');
        	miViewComunicadosInternosOnClick();
        }
 		if ($('cdUsuario').value > 0) {
			setTimeout(function() { verifyCis(); }, 1000 * 60 * 5, 10);
		}
	}
}

function miRelatorioPendenciaOnClick(options)	{
	var frameHeight = 0;
	if (self.innerWidth)
		frameHeight = self.innerHeight;
	else if (document.documentElement && document.documentElement.clientWidth)
		frameHeight = document.documentElement.clientHeight;
	else if (document.body)
		frameHeight = document.body.clientHeight;
	
	options = options==null ? {} : options;
	options.noDestroyWindow = options.noDestroyWindow!=null ? options.noDestroyWindow : false;
	options.top             = options.top ? options.top : (frameHeight - 495);
	options.cdEmpresa = options.cdEmpresa==null ? 0 : options.cdEmpresa;
	options.cdSetor   = options.cdSetor==null   ? 0 : options.cdSetor;
	createWindow('jRelatorioPendencia',{caption:'Relatório de Pendências', width:902, height:485, top: options.top, noDestroyWindow: options.noDestroyWindow, 
	                                    contentUrl:'../ptc/relatorio_pendencia.jsp?cdEmpresa='+options.cdEmpresa+'&cdSetor='+options.cdSetor});
}
