// JavaScript Document
function miAlterarSenhaOnClick()	{
	createWindow('jAlterarSenha', {caption: 'Alterando senha', 
									width: 290, 
									height: 115, 
									contentUrl: '../seg/alterar_senha.jsp',
									modal:true});
}

function miConfGrupoOnClick() {
	if ($('tpUsuario') != null && $('tpUsuario').value == 0)
		showWindow('jConfGrupos', 'Configuração de Permissões - Grupos', 815, 505, '../seg/configuracao_grupo.jsp', true); 
	else
		showMsgbox('myManager', 250, 60, 'Sua conta de Usu�rio n�o tem acesso a este recurso.');	
}

function miConfUsuarioOnClick() {
	if ($('tpUsuario')!=null && $('tpUsuario').value == 0)
		showWindow('jConfUsuarios', 'Configuração de Permissões - Usuários', 815, 505, '../seg/configuracao_usuario.jsp', true); 
	else
		showMsgbox('myManager', 250, 60, 'Sua conta de Usuário n�o tem acesso a este recurso.');	
}

function miLogOnClick() {
	 showWindow('jLogs', 'Consulta de Logs', 701, 471, '../seg/log.jsp', true); 
}

function miPainelParametrosOnClick() {
	 showWindow('jPainelParametros', 'Configura��o de Par�metros', 902, 478, '../seg/configuracao_parametros.jsp', true); 
}

function miParametroOnClick() {
	 showWindow('jParametro', 'Cadastro e Manutenção de Parametros', 585, 317, 'parametro.jsp', true); 
}

function miInitParametrosOnClick(content){
    if(content==null){
		createTempbox('jProcessando', {width: 130, height: 45, message: 'Processando...', boxType: 'LOADING', time:0, noTitle: true, modal: true});
        getPage("POST", "miInitParametrosOnClick", "../methodcaller?className=com.tivic.manager.util.Util" +
                                                   "&method=init()", null, null, true);
    }
    else	{
		closeWindow('jProcessando');
        var ok = parseInt(content, 10)>0;
		if(ok)
            createTempbox("jMsg", {width: 250, height: 50, message: "Par�metros inicializados com sucesso!", boxType: "INFO", time: 2000});
        else
            createTempbox("jMsg", {width: 250, height: 50, message: "ERRO ao tentar inicializar par�metros!", boxType: "ERROR", time: 3000});
    }
}


function miSistemaOnClick() {
	createWindow('jAtividades', {caption: 'Inventório de Permiss�es', 
							  	 width: 815,
							  	 height: 510,
								 contentUrl: '../seg/sistema.jsp'});
}

function miScannerOnClick() {
	createWindow('jScanner', {caption: 'Scanner de A��es e Objetos', 
							  	 width:686,
							  	 height: 422,
								 contentUrl: 'scanner.jsp'});
}

function miPerguntaSecretaOnClick() {
	createGenericForm('jPerguntaSecreta',{classDAO: 'com.tivic.manager.seg.PerguntaSecretaDAO',
								  title: 'Manutenção de Perguntas Secretas',
								  width: 350, height: 300,
								  keysFields: ['cd_pergunta_secreta'],
								  constructorFields: [{name: 'cd_pergunta_secreta', type: 'int'},
													  {name: 'nm_pergunta_secreta', type: 'java.lang.String'}],
								  gridFields: [{name: 'nm_pergunta_secreta', label: 'Pergunta Secreta'}],
								  editFields: [{name: 'nm_pergunta_secreta', label: 'Pergunta Secreta', width:100, maxLength:60, charCase: 'normal'}]});
}

/**
 * @author Edgard
 * @since Nov/2014
 */
function miLicencaOnClick() {
	createWindow('jLicenca', {caption: 'Licença do Sistema - Licenciados e Chaves', 
							  	 width: 660,
							  	 height: 450,
								 contentUrl: '../seg/licenca.jsp'});
}

/**
 * @author Sapucaia
 * @since Ago/2014
 */
function miReleasesOnClick() {
	FormFactory.createQuickForm('jReleases', {caption: 'Manutenção de Releases', width: 500, height: 450,
		  //quickForm
		  id: "seg_release",
		  classDAO: 'com.tivic.manager.seg.ReleaseDAO',
		  keysFields: ['cd_release'], unitSize: '%',
		  constructorFields: [{reference: 'cd_release', type: 'int'},
		                      {reference: 'nr_maior', type: 'int'},
		                      {reference: 'nr_menor', type: 'int'},
							  {reference: 'txt_descricao', type: 'java.lang.String'},
							  {reference: 'dt_release', type: 'java.util.GregorianCalendar'},
							  {reference: 'blb_release', type: 'byte[]'},
							  {reference: 'lg_executado', type: 'int'},
							  {reference: 'nr_build', type: 'int'},
							  {reference: 'cd_sistema', type: 'int'}],
		  onAfterSave: function() {
		  		this.getAll();
		  },
		  gridOptions: {columns: [{reference: 'nm_versao', label: 'Vers�o'},
		                          {reference: 'dt_release', label: 'Data', type: GridOne._DATE},
		                          {reference: 'nm_executado', label: 'Exec?'},
	   							  {reference: 'txt_descricao', label: 'O que h� de novo?'}],
					    strippedLines: true,
					    onProcessRegister: function(reg) {
					    	reg['NM_VERSAO'] = 'v' + reg['NR_MAIOR'] + '.' + reg['NR_MENOR'] + '.' + reg['NR_BUILD']; 
					    	reg['NM_EXECUTADO'] = reg['LG_EXECUTADO'] == 1 ? 'Sim' : 'N�o'; 
					    },
					    columnSeparator: false,
					    lineSeparator: false},
		  lines: [[{reference: 'nr_maior', label: 'N� Maior', width: 15},
        		   {reference: 'nr_menor', label: 'N� Menor', width: 15},
	  			   {reference: 'nr_build', label: 'N� Build', width: 15},
	  			   {reference: 'dt_release', label: 'Data', width: 30, maxLength:10, type: 'date', mask: '##/##/####', calendarPosition:'TL', datatype: 'date'}, 
				   {reference: 'lg_executado', label: 'Executado?', width:25, value: 1, type: 'checkbox', checked: true}],
	  			  [{reference: 'cd_sistema', label: 'Sistema', width:100, type:'select', 
					    classMethodLoad: 'com.tivic.manager.seg.SistemaServices', methodLoad: 'getAll()', 
						fieldValue: 'cd_sistema', fieldText: 'nm_sistema'}],
	  			  [{reference: 'txt_descricao', type:'textarea', label:'O que h� de novo?', width:100, height: 80}]
	  			 ]});
}

function miFonteDadosOnClick() {
	createWindow('jDatasource', {caption: 'Fonte de dados', 
							  	 width:800,
							  	 height: 430,
								 contentUrl: '../grl/fonte_dados.jsp'});
}

/**
 * @author Gabriel
 * @since Out/2014
 * @param content
 */
function miSincronizacaoOnClick(content){
	if(content==null){
		createTempbox('jProcessando', {width: 130, height: 45, message: 'Processando...', boxType: 'LOADING', time:0, noTitle: true, modal: true});
        getPage("POST", "miSincronizacaoOnClick", "../methodcaller?className=com.tivic.manager.sinc.LoteServices" +
                                                   "&method=chamadaSincronizacao(const 1:int)", null, null, true);
    }
    else	{
		closeWindow('jProcessando');
		var result = null;
		try { result = eval("(" + content + ")"); } catch(e) {}
        var ok = parseInt(result.code, 10)>0;
		if(ok)
            createTempbox("jMsg", {width: 250, height: 50, message: "Sincronizacao realizada com sucesso!", boxType: "INFO", time: 2000});
        else
        	createMsgbox("jMsg", {width: 250, height: 50, message: "ERRO ao sincronizar!", boxType: "ERROR", time: 3000});
    }
}

function miSincTabelaOnClick() {
	var optionsTipoSincronizacao = [{value: 0, text: 'Local -> Servidor'},
	     					   	   {value: 1, text: 'Servidor -> Local'},
	     					   	   {value: 2, text: 'Local <-> Servidor'}];
	
	var optionsSituacaoSincronizacao = [{value: 0, text: 'Desativado'},
	 	     					   	   {value: 1, text: 'Ativado'}];
	
	FormFactory.createQuickForm('jSincTabela', {caption: 'Manutenção de Tabelas de Sincronização', width: 500, height: 350,
												  //quickForm
												  id: "sinc_tabela",
												  classDAO: 'com.tivic.manager.sinc.TabelaDAO',
												  classMethodGetAll: 'com.tivic.manager.sinc.TabelaServices',
												  classMethodInsert: 'com.tivic.manager.sinc.TabelaServices',
												  classMethodUpdate: 'com.tivic.manager.sinc.TabelaServices',
												  classMethodDelete: 'com.tivic.manager.sinc.TabelaServices',
												  keysFields: ['cd_tabela'], unitSize: '%',
												  constructorFields: [{reference: 'cd_tabela', type: 'int'},
													  				  {reference: 'nm_tabela', type: 'java.lang.String'},
													  				  {reference: 'dt_inicio', type: 'java.util.GregorianCalendar'},
													  				  {reference: 'st_sincronizacao', type: 'int'},
													  				  {reference: 'nm_campo_unico', type: 'java.lang.String'},
													  				  {reference: 'nm_campo_chave', type: 'java.lang.String'},
													  				  {reference: 'nm_campo_nao_nulo', type: 'java.lang.String'},
													  				  {reference: 'tp_sincronizacao', type: 'int'}],
                                                  onAfterSave: function(obj){
							  					    this.getAll();  
							  				      },
											      gridOptions: {columns: [{reference: 'nm_tabela', label: 'Nome da Tabela'},
								  			   							  {reference: 'cl_dt_inicio', label: 'Data de Inicio'},
								  			   							  {reference: 'cl_st_sincronizacao', label: 'Situação'},
								  			   							  {reference: 'cl_tp_sincronizacao', label: 'Tipo de Sincronização'}],
															    strippedLines: true,
															    columnSeparator: false,
															    lineSeparator: false},
												  lines: [[{reference: 'nm_tabela', label: 'Nome', width:39},
												           {reference: 'dt_inicio', label: 'Data de Inicio', type: 'date', calendarPosition: 'Tr', width:16},
												           {reference: 'tp_sincronizacao', label: 'Tipo de Sincronização', width:25, type: 'select', options: optionsTipoSincronizacao},
												           {reference: 'st_sincronizacao', label: 'Situação', width:20, type: 'select', options: optionsSituacaoSincronizacao, disabled: true}],
												          [{reference: 'nm_campo_unico', label: 'Campos Únicos', width:100}],
												   		  [{reference: 'nm_campo_chave', label: 'Chave Primária', width:100}],
												   		  [{reference: 'nm_campo_nao_nulo', label: 'Campos não-nulos', width:100}]]});
}

var windowGrupo;
var gridTabelas;
var gridGrupos;
function miSincGrupoOnClick(){
	var optionsSituacao = [{value: 0, text: 'Inativo'},
					   	   {value: 1, text: 'Ativo'}];
	windowGrupo = FormFactory.createQuickForm('jSincGrupo', {caption: 'Manutenção de Grupos de Sincronização', width: 500, height: 400,
		  //quickForm
		  id: "sinc_grupo",
		  classDAO: 'com.tivic.manager.sinc.GrupoDAO',
		  classMethodGetAll: 'com.tivic.manager.sinc.GrupoServices',
		  classMethodInsert: 'com.tivic.manager.sinc.GrupoServices',
		  classMethodUpdate: 'com.tivic.manager.sinc.GrupoServices',
		  classMethodDelete: 'com.tivic.manager.sinc.GrupoServices',
		  keysFields: ['cd_grupo'], unitSize: '%',
		  constructorFields: [{reference: 'cd_grupo', type: 'int'},
			  				  {reference: 'nm_grupo', type: 'java.lang.String'},
			  				  {reference: 'st_grupo', type: 'int'}],
		  onAfterSave: function(obj){
			this.getAll();  
		  },	  				  
		  gridOptions: {columns: [{reference: 'nm_grupo', label: 'Nome do Grupo'},
	   							  {reference: 'cl_st_grupo', label: 'Situação'}],
					    strippedLines: true,
					    columnSeparator: false,
					    lineSeparator: false,
					    extraFunction: function(grid){
					    	loadTabelas(null, grid);
					    }},
		  additionalButtons:[{id:'btnNewTabelaOnClick', idobject: 'btnNewTabela', img: '/sol/imagens/form-btNovo16.gif', label: 'Nova Tabela', onClick: btnNewTabelaOnClick},
		                     {id:'btnDeleteTabelaOnClick', idobject: 'btnDeleteTabela', img: '/sol/imagens/form-btExcluir16.gif', label: 'Deletar Tabela', onClick: btnDeleteTabelaOnClick}], 		    
		  lines: [[{reference: 'nm_grupo', label: 'Nome', width:80},
		           {reference: 'st_grupo', label: 'Situação', width:20, type: 'select', options: optionsSituacao}],
		           [{label: 'Tabelas', id:'divGridTabelas', width:100, height: 210, type: 'grid'}]]});
	setTimeout('loadGrids()', 10);
}

function loadGrids(){
	gridGrupos  = windowGrupo.grid;
	gridTabelas = $('gridTabelasEnvelope');
}

function btnNewTabelaOnClick(){
	FormFactory.createFormWindow('jTabela', {caption: "Tabela de Sincroniza��o",
		  width: 330, height: 100,noDrag: true,modal: true,
		  id: 'tabelaSincronizacao', unitSize: '%',
		  lines: [[{id:'cdTabela', width:100, label: 'Tabela',
				    type: 'lookup', findAction: function(){btnFindTabelaOnClick(null);}}],
	  			  [{type: 'space', width: 50},
	  			   {id:'btnConfirmar', type:'button', image: '/sol/imagens/check_13.gif', label:'Confirmar', width:25, 
	  			   	onClick: function(){ btnSaveTabelaOnClick(null);}},
				   {id:'btnCancelar', type:'button', image: '/sol/imagens/cancel_13.gif', label:'Fechar', width:25, 
				    onClick: function()	{
						closeWindow('jTabela');
					}}]],
		  focusField:'cdNovaCategoria'});
}

function btnFindTabelaOnClick(reg)	{
    if(!reg)	{
		FilterOne.create("jFiltro", {caption:"Pesquisar Tabela de Sincroniza��o", width: 500, height: 300, modal: true, noDrag: true,
										// Parametros do filtro
										className: "com.tivic.manager.sinc.TabelaServices", method: "find",
										filterFields: [[{label:"Nome da Tabela",reference:"NM_TABELA",datatype:_VARCHAR,comparator:_LIKE_ANY,width:100, charcase: 'normal'}]],
										gridOptions:{columns:[{label:"Nome da Tabela",reference:"NM_TABELA"}]},
										callback: btnFindTabelaOnClick
									 });
    }
    else {// retorno
        closeWindow('jFiltro');
        $("cdTabela").value 	= reg[0]['CD_TABELA'];
        $("cdTabelaView").value = reg[0]['NM_TABELA'];
	}
}

function btnSaveTabelaOnClick(content){
	if(content==null)	{
		if($('cdTabela').value <= 0)	{
	        createMsgbox("jMsg", {width: 300, height: 50, caption: 'Aten��o',
	                              message: "Informe a Tabela a ser inclu�da!", msgboxType: "INFO"});
			return;
		}
		getPage("GET", "btnSaveTabelaOnClick", 
				"../methodcaller?className=com.tivic.manager.sinc.TabelaGrupoServices"+
				"&method=save(new com.tivic.manager.sinc.TabelaGrupo(const "+$('cdTabela').value+":int,"+
				"const "+gridGrupos.getSelectedRowRegister()['CD_GRUPO']+":int))", null, true);
	}
	else	{
		var result = null;
		try { result = eval("(" + content + ")");} catch(e) {}
		if(result.code==1)	{
            createTempbox("jMsg", {width: 300, height: 50, message: "Dados gravados com sucesso!", tempboxType: "INFO", time: 2000});
			closeWindow('jTabela');
			loadTabelas(null, gridGrupos);
		}
		else	{
	        createMsgbox("jMsg", {width: 300, height: 50, caption: 'Aten��o',
	                              message: "Erro ao tentar incluir Tabela!", msgboxType: "ERRO"});
		}
	}
}

function btnDeleteTabelaOnClickAux(content){
    getPage("GET", "btnDeleteTabelaOnClick", 
            "../methodcaller?className=com.tivic.manager.sinc.TabelaGrupoServices"+
            "&method=remove(const "+gridTabelas.getSelectedRowRegister()['CD_TABELA']+":int, const "+gridGrupos.getSelectedRowRegister()['CD_GRUPO']+":int):int", null, true, null);
}

function btnDeleteTabelaOnClick(content){
	if(content==null){
        if (!gridTabelas || !gridTabelas.getSelectedRow())
            createMsgbox("jMsg", {width: 300, caption: 'Aten��o',
                                  height: 80, 
                                  message: "Nenhuma registro foi carregado para que seja exclu�do.",
                                  msgboxType: "INFO"});
        else
            createConfirmbox("dialog", {caption: "Exclus�o de registro",
                                        width: 300, 
                                        height: 75, 
                                        message: "Voc� tem certeza que deseja excluir este registro?",
                                        boxType: "QUESTION",
                                        positiveAction: function() {setTimeout("btnDeleteTabelaOnClickAux()", 10);}});
    }
    else{
		var result = null;
		try { result = eval("(" + content + ")");} catch(e) {}
		if(result.code==1)	{
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "Registro exclu�do com sucesso!",
                                  time: 3000});
            loadTabelas(null, gridGrupos);
		}
        else
            createTempbox("jTemp", {width: 300, 
                                  height: 75, 
                                  message: "N�o foi poss�vel excluir este registro!", 
                                  time: 5000});
    }
}


var gridTabelas = null;
function loadTabelas(content, grid){
	if(content == null){
		if(grid && grid.getSelectedRowRegister()){
			getPage('POST', 'loadTabelas', 
				   '../methodcaller?className=com.tivic.manager.sinc.GrupoServices'+
				   '&method=getAllTabelas(const '+grid.getSelectedRowRegister()['CD_GRUPO']+':int)', null, null, null, null);
		}
		else{
			gridTabelas = GridOne.create('gridTabelas', {columns: [{label:'Nome da Tabela', reference: 'NM_TABELA'}],
				 resultset: {lines:{}},
				 plotPlace: $('divGridTabelas'),
				 onProcessRegister: function(reg)	{
				 
				 },
				 columnSeparator: false,
				 noSelectOnCreate: false});
		}
	}
	else{
		try { rsmTabelas = eval("("+content+")"); } catch(e) {};
		if(rsmTabelas == null){
			showMsgbox('Manager', 300, 80, 'Algum erro ocorreu ao tentar buscar as tabelas!');
			return;
		}
		if(rsmTabelas.lines.length > 0){
			gridTabelas = GridOne.create('gridTabelas', {columns: [{label:'Nome da Tabela', reference: 'NM_TABELA'}],
																 resultset: rsmTabelas,
																 plotPlace: $('divGridTabelas'),
																 onProcessRegister: function(reg)	{
																 
																 },
																 columnSeparator: false,
																 noSelectOnCreate: false});
		}
	}
}

function miSincLocalOnClick(){
	var optionsTipoLocal = [{value: 0, text: 'Servidor'},
 					   	    {value: 1, text: 'Local'}];
	FormFactory.createQuickForm('jSincLocal', {caption: 'Manutenção de Locais de Sincronização', width: 500, height: 350,
		  //quickForm
		  id: "sinc_local",
		  classDAO: 'com.tivic.manager.sinc.LocalDAO',
		  classMethodGetAll: 'com.tivic.manager.sinc.LocalServices',
		  keysFields: ['cd_local'], unitSize: '%',
		  constructorFields: [{reference: 'cd_local', type: 'int'},
			  				  {reference: 'id_local', type: 'java.lang.String'},
			  				  {reference: 'tp_local', type: 'int'},
			  				  {reference: 'nm_login_database', type: 'java.lang.String'},
			  				  {reference: 'nm_senha_database', type: 'java.lang.String'},
			  				  {reference: 'nm_url_database', type: 'java.lang.String'},],
		  onAfterSave: function(obj){
			this.getAll();  
		  },	  				  
		  gridOptions: {columns: [{reference: 'id_local', label: 'ID do Local'},
	   							  {reference: 'cl_tp_local', label: 'Tipo de Local'}],
					    strippedLines: true,
					    columnSeparator: false,
					    lineSeparator: false},
		  lines: [[{reference: 'id_local', label: 'ID', width:50},
		           {reference: 'tp_local', label: 'Tipo de Local', width:50, type: 'select', options: optionsTipoLocal}],
		          [{reference: 'nm_url_database', label: 'URL', width:100}], 
		          [{reference: 'nm_login_database', label: 'Login', width:50},
		           {reference: 'nm_senha_database', label: 'Senha', width:50}]]});
}