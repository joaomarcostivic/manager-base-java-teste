package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.geo.Ponto;
import com.tivic.manager.geo.PontoDAO;
import com.tivic.manager.grl.FormularioAtributoValor;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.srh.DadosFuncionais;
import com.tivic.manager.srh.DadosFuncionaisDAO;
import com.tivic.manager.srh.DadosFuncionaisServices;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.Validator;
import com.tivic.manager.util.ValidatorResult;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class InstituicaoEducacensoServices {
	
	//Recebe Escolariazação em outro espaço
	public static final int TP_ESCOLARIZACAO_NAO_RECEBE = 0;
	public static final int TP_ESCOLARIZACAO_HOSPITAL   = 1;
	public static final int TP_ESCOLARIZACAO_DOMICILIO  = 2;
	
	public static final String[] tipoEscolarizacaoOutroEspaco = {"Não recebe", "Hospital", "Domicílio"};
	
	/*Tipos de Dependencia Administrativa*/
	public static final String TP_DEPENDENCIA_ADMINISTRATIVA_FEDERAL   = "1";
	public static final String TP_DEPENDENCIA_ADMINISTRATIVA_ESTADUAL  = "2";
	public static final String TP_DEPENDENCIA_ADMINISTRATIVA_MUNICIPAL = "3";
	public static final String TP_DEPENDENCIA_ADMINISTRATIVA_PRIVADA   = "4";
	
	@Deprecated
	public static final int TP_ATENDIMENTO_ESPECIALIZADO_NAO_OFERECE 		= 0;
	@Deprecated
	public static final int TP_ATENDIMENTO_ESPECIALIZADO_EXCLUSIVAMENTE 	= 1;
	@Deprecated
	public static final int TP_ATENDIMENTO_ESPECIALIZADO_NAO_EXCLUSIVAMENTE = 2;
	
	@Deprecated
	public static final int TP_ATIVIDADE_COMPLEMENTAR_NAO_OFERECE 		 = 0;
	@Deprecated
	public static final int TP_ATIVIDADE_COMPLEMENTAR_EXCLUSIVAMENTE 	 = 1;
	@Deprecated
	public static final int TP_ATIVIDADE_COMPLEMENTAR_NAO_EXCLUSIVAMENTE = 2;
	
	//Tipo de Forma de Ocupação
	public static final int TP_OCUPACAO_PROPRIO = 1;
	public static final int TP_OCUPACAO_ALUGADO = 2;
	public static final int TP_OCUPACAO_CEDIDO  = 3;
		
	/*Localização Diferenciada*/
	public static final String TP_LOCALIZACAO_DIFERENCIADA_ASSENTAMENTO 					= "1";
	public static final String TP_LOCALIZACAO_DIFERENCIADA_TERRA_INDIGENA 					= "2";
	public static final String TP_LOCALIZACAO_DIFERENCIADA_QUILOMBOS 						= "3";
	public static final String TP_LOCALIZACAO_DIFERENCIADA_USO_SUSTENTAVEL 					= "4";
	public static final String TP_LOCALIZACAO_DIFERENCIADA_USO_SUSTENTAVEL_TERRA_INDIGENA 	= "5";
	public static final String TP_LOCALIZACAO_DIFERENCIADA_USO_SUSTENTAVEL_QUILOMBOS 		= "6";
	public static final String TP_LOCALIZACAO_DIFERENCIADA_NAO_APLICA 						= "7";
	
	
	/*Tipo de Mantenedora*/
	public static final String TP_MANTENEDORA_EMPRESA 				= "01";
	public static final String TP_MANTENEDORA_SINDICATO 			= "02";
	public static final String TP_MANTENEDORA_ONG 					= "03";
	public static final String TP_MANTENEDORA_SEM_FINS_LUCRATIVOS 	= "04";
	public static final String TP_MANTENEDORA_SISTEMA_S 			= "05";
	public static final String TP_MANTENEDORA_OSCIP 			    = "06";
	
	/*Situação das Instituições*/
	public static final int ST_EM_ATIVIDADE 				= 1;
	public static final int ST_PARALISADA  				    = 2;
	public static final int ST_EXTINTA 					    = 3;
	
	/*Tipo de Localização*/
	public static final int TP_LOCALIZACAO_URBANA 				= 1;
	public static final int TP_LOCALIZACAO_RURAL 				= 2;
	
	/*Situação de Regulamento*/
	public static final int ST_REGULAMENTACAO_NAO 				= 0;
	public static final int ST_REGULAMENTACAO_SIM 				= 1;
	public static final int ST_REGULAMENTACAO_TRAMITANDO		= 2;
	
	/*Materiais Especificos*/
	public static final int TP_MATERIAL_ESPECIFICO_QUILOMBOLAS	 = 0;
	public static final int TP_MATERIAL_ESPECIFICO_INDIGENA		 = 1;
	public static final int TP_MATERIAL_ESPECIFICO_NAO_SE_APLICA = 2;
	
	public static final String[] situacaoInstituicao           = {"Nenhum", "Em atividade","Paralisada","Extinta"};
	public static final String[] tipoDependenciaAdministrativa = {"Federal","Estadual","Municipal","Privada"};
	public static final String[] tipoLocalizacao               = {"Urbana","Rural"};
	public static final String[] tipoCategoriaPrivada          = {"Particular","Comunitária","Confessional","Filantrópica"};
	public static final String[] tipoConvenio                  = {"Estadual","Municipal","Estadual e Municipal"};
	public static final String[] situacaoRegulamentacao        = {"Não", "Sim", "Tramitando"};
	public static final String[] situacaoAguaConsumida         = {"Não-Filtrada","Filtrada"};
	public static final String[] tipoAbastecimentoAgua = {"Rede pública","Poço artesiano","Cacimba/Cisterna/Poço","Fonta/Rio/Igarapé/Riacho/Córrego","Inexistene"};
	public static final String[] tipoRedeEletrica = {"Rede pública","Gerador","Outros(alternativa)","Inexistene"};
	public static final String[] tipoEsgoto = {"Rede pública","Fossa","Inexistene"};
	public static final String[] tipoDestinoLixo = {"Coleta períodica","Queima","Joga em outra área","Recicla","Enterra","Outros"};
	public static final String[] tipoAtendimentoEspecializado = {"Não oferece","Exclusivamente","Não exclusivamente"};
	public static final String[] tipoAtividadeComplementar = {"Não oferece","Exclusivamente","Não exclusivamente"};
	public static final String[] modalidadeEnsino = {"Ensino regular","Educação especial","Educação de jovens/adultos"};
	public static final String[] tipoEducacaoInfantil = {"Nenhum","Creche","Pré-escola"};
	public static final String[] tipoEnsinoMedio = {"Médio","Normal/Magistério","Integrado","Ed. profissionalizante"};
	public static final String[] tipoEducacaoJovemAdulto = {"Ensino fundamental","Ensino Médio"};
	public static final String[] tipoLocalizacaoDiferenciada = {"Assentamento","Terra indígena","Rem. de Quilombo", "Unidade de Uso sustentável", "Unidade de uso sustentável em Terra indígena", "Unidade de uso sustentável em remanescentes de quilombos","Não se aplica"};
	public static final String[] tipoMaterialEspecifico = {"Quilombolas","Indígena","Não utiliza"};
	public static final String[] tipoLingua = {"Portuguesa","Indígena"};

	/*Cargo do responsável*/
	public static final String CARGO_DIRETOR = "1";
	public static final String CARGO_OUTRO   = "2";
	
	/*Tipo de Local de Funcionamento da Instituicao*/
	public static final String ID_LOCAL_FUNCIONAMENTO_PREDIO 			 = "01";
	public static final String ID_LOCAL_FUNCIONAMENTO_TEMPLO 			 = "02";
	public static final String ID_LOCAL_FUNCIONAMENTO_SALAS_EMPRESA  	 = "03";
	public static final String ID_LOCAL_FUNCIONAMENTO_CASA   			 = "04";
	public static final String ID_LOCAL_FUNCIONAMENTO_SALAS_OUTRA_ESCOLA = "05";
	public static final String ID_LOCAL_FUNCIONAMENTO_GALPAO 			 = "06";
	public static final String ID_LOCAL_FUNCIONAMENTO_SOCIOEDUCATIVO	 = "07";
	public static final String ID_LOCAL_FUNCIONAMENTO_PRISIONAL 		 = "08";
	public static final String ID_LOCAL_FUNCIONAMENTO_OUTROS 			 = "09";
	
	/*Tipo de Abastecimento de Agua*/
	public static final String TP_ABASTECIMENTO_AGUA_REDE_PUBLICA   	= "01";
	public static final String TP_ABASTECIMENTO_AGUA_POCO_ARTESIANO 	= "02";
	public static final String TP_ABASTECIMENTO_AGUA_CACIMBA			= "03";
	public static final String TP_ABASTECIMENTO_AGUA_FONTE          	= "04";
	public static final String TP_ABASTECIMENTO_AGUA_INEXISTENTE    	= "05";

	
	//Tipo de abastecimento de energia
	public static final String TP_ABASTECIMENTO_ENERGIA_REDE_PUBLICA   		= "01";
	public static final String TP_ABASTECIMENTO_ENERGIA_GERADOR        		= "02";
	public static final String TP_ABASTECIMENTO_ENERGIA_OUTRAS		   		= "03";
	public static final String TP_ABASTECIMENTO_ENERGIA_INEXISTENTE    		= "04";
	public static final String TP_ABASTECIMENTO_ENERGIA_COMBUSTIVEL_FOSSIL 	= "05";
		
	//Tipo de esgoto sanitário
	public static final String TP_ESGOTO_SANITARIO_REDE_PUBLICA    = "01";
	public static final String TP_ESGOTO_SANITARIO_FOSSA           = "02";
	public static final String TP_ESGOTO_SANITARIO_INEXISTENTE     = "03";
	public static final String TP_ESGOTO_SANITARIO_FOSSA_SEPTICA   = "04";
	public static final String TP_ESGOTO_SANITARIO_FOSSA_RUDIMENTAR= "05";
		
	//Tipo de destinação de lixo
	public static final String TP_DESTINACAO_LIXO_COLETA      = "01";
	public static final String TP_DESTINACAO_LIXO_QUEIMA      = "02";
	public static final String TP_DESTINACAO_LIXO_OUTRA_AREA  = "03";
	public static final String TP_DESTINACAO_LIXO_RECICLA     = "04";
	public static final String TP_DESTINACAO_LIXO_ENTERRA     = "05";
	public static final String TP_DESTINACAO_LIXO_OUTROS      = "06";
	public static final String TP_DESTINACAO_LIXO_LICENCIADA  = "07";
	public static final String TP_DESTINACAO_LIXO_SEPARACAO   = "08";
	public static final String TP_DESTINACAO_LIXO_REAPROVEITAMENTO = "09";
	public static final String TP_DESTINACAO_LIXO_NAO_FAZ_TRATAMENTO = "10";
	
	
	/*Tipos de Dependencia da Instituição*/
	public static final String TP_DEPENDENCIA_INSTITUICAO_SALA_DIRETORIA   					= "01";
	public static final String TP_DEPENDENCIA_INSTITUICAO_SALA_PROFESSORES   				= "02";
	public static final String TP_DEPENDENCIA_INSTITUICAO_SALA_SECRETARIA   				= "03";
	public static final String TP_DEPENDENCIA_INSTITUICAO_LABORATORIO_INFORMATICA   		= "04";
	public static final String TP_DEPENDENCIA_INSTITUICAO_LABORATORIO_CIENCIAS   			= "05";
	public static final String TP_DEPENDENCIA_INSTITUICAO_SALA_RECURSOS_MULTIFUNCIONAIS   	= "06";
	public static final String TP_DEPENDENCIA_INSTITUICAO_QUADRA_ESPORTES_COBERTA   		= "07";
	public static final String TP_DEPENDENCIA_INSTITUICAO_QUADRA_ESPORTES_DESCOBERTA   		= "08";
	public static final String TP_DEPENDENCIA_INSTITUICAO_COZINHA   						= "09";
	public static final String TP_DEPENDENCIA_INSTITUICAO_BIBLIOTECA   						= "10";
	public static final String TP_DEPENDENCIA_INSTITUICAO_SALA_LEITURA   					= "11";
	public static final String TP_DEPENDENCIA_INSTITUICAO_PARQUE_INFANTIL   				= "12";
	public static final String TP_DEPENDENCIA_INSTITUICAO_BERCARIO   						= "13";
	public static final String TP_DEPENDENCIA_INSTITUICAO_BANHEIRO_FORA_PREDIO   			= "14";
	public static final String TP_DEPENDENCIA_INSTITUICAO_BANHEIRO_DENTRO_PREDIO   			= "15";
	public static final String TP_DEPENDENCIA_INSTITUICAO_BANHEIRO_EDUCACAO_INFANTIL   		= "16";
	public static final String TP_DEPENDENCIA_INSTITUICAO_BANHEIRO_ALUNOS_DEFICIENCIA   	= "17";
	public static final String TP_DEPENDENCIA_INSTITUICAO_DEPENDENCIAS_ALUNOS_DEFICIENCIA   = "18";
	public static final String TP_DEPENDENCIA_INSTITUICAO_BANHEIRO_CHUVEIRO   				= "19";
	public static final String TP_DEPENDENCIA_INSTITUICAO_REFEITORIO   						= "20";
	public static final String TP_DEPENDENCIA_INSTITUICAO_DESPENSA   						= "21";
	public static final String TP_DEPENDENCIA_INSTITUICAO_ALMOXARIFADO   					= "22";
	public static final String TP_DEPENDENCIA_INSTITUICAO_AUDITORIO   						= "23";
	public static final String TP_DEPENDENCIA_INSTITUICAO_PATIO_COBERTO   					= "24";
	public static final String TP_DEPENDENCIA_INSTITUICAO_PATIO_DESCOBERTO   				= "25";
	public static final String TP_DEPENDENCIA_INSTITUICAO_ALOJAMENTO_ALUNO   				= "26";
	public static final String TP_DEPENDENCIA_INSTITUICAO_ALOJAMENTO_PROFESSOR   			= "27";
	public static final String TP_DEPENDENCIA_INSTITUICAO_AREA_VERDE   						= "28";
	public static final String TP_DEPENDENCIA_INSTITUICAO_LAVANDERIA    					= "29";
	public static final String TP_DEPENDENCIA_INSTITUICAO_NENHUMA_DEPENDENCIAS_RELACIONADAS = "30";
	public static final String TP_DEPENDENCIA_INSTITUICAO_SALA_AULA      					= "31";
	public static final String TP_DEPENDENCIA_INSTITUICAO_BANHEIRO      					= "32";
	public static final String TP_DEPENDENCIA_INSTITUICAO_BANHEIRO_FUNCIONARIOS				= "33";
	public static final String TP_DEPENDENCIA_INSTITUICAO_PISCINA      						= "34";
	public static final String TP_DEPENDENCIA_INSTITUICAO_SALA_REPOUSO_ALUNOS				= "35";
	public static final String TP_DEPENDENCIA_INSTITUICAO_SALA_ARTES      					= "36";
	public static final String TP_DEPENDENCIA_INSTITUICAO_SALA_MUSICA      					= "37";
	public static final String TP_DEPENDENCIA_INSTITUICAO_SALA_DANCA      					= "38";
	public static final String TP_DEPENDENCIA_INSTITUICAO_SALA_MULTIUSO      				= "39";
	public static final String TP_DEPENDENCIA_INSTITUICAO_TERREIRAO      					= "40";
	public static final String TP_DEPENDENCIA_INSTITUICAO_VIVEIRO	      					= "41";
	
	
	/*Tipos de Recursos Acessibilidade*/
	public static final String TP_RECURSO_ACESSIBILIDADE_CORRIMAO	   						= "01";
	public static final String TP_RECURSO_ACESSIBILIDADE_ELEVADOR	   						= "02";
	public static final String TP_RECURSO_ACESSIBILIDADE_PISOS_TATEIS  						= "03";
	public static final String TP_RECURSO_ACESSIBILIDADE_PORTAS80	   						= "04";
	public static final String TP_RECURSO_ACESSIBILIDADE_RAMPAS	   							= "05";
	public static final String TP_RECURSO_ACESSIBILIDADE_SINALIZACAO_SONORA	   				= "06";
	public static final String TP_RECURSO_ACESSIBILIDADE_SINALIZACAO_TATIL	   				= "07";
	public static final String TP_RECURSO_ACESSIBILIDADE_SINALIZACAO_VISUAL	   				= "08";
	public static final String TP_RECURSO_ACESSIBILIDADE_NENHUM		   						= "09";
	
	/*Tipos de Equipamento*/
	public static final String TP_EQUIPAMENTO_TELEVISAO 		= "01";
	public static final String TP_EQUIPAMENTO_VIDEOCASSETE 		= "02";
	public static final String TP_EQUIPAMENTO_APARELHO_DVD 		= "03";
	public static final String TP_EQUIPAMENTO_ANTENA_PARABOLICA = "04";
	public static final String TP_EQUIPAMENTO_COPIADORA 		= "05";
	public static final String TP_EQUIPAMENTO_RETROPROJETOR 	= "06";
	public static final String TP_EQUIPAMENTO_IMPRESSORA 		= "07";
	public static final String TP_EQUIPAMENTO_APARELHO_SOM 		= "08";
	public static final String TP_EQUIPAMENTO_DATA_SHOW 		= "09";
	public static final String TP_EQUIPAMENTO_FAX 				= "10";
	public static final String TP_EQUIPAMENTO_FILMADORA 		= "11";
	public static final String TP_EQUIPAMENTO_COMPUTADORES 		= "12";
	public static final String TP_EQUIPAMENTO_IMPRESSORA_MULTI	= "13";
	public static final String TP_EQUIPAMENTO_SCANNER			= "14";
	public static final String TP_EQUIPAMENTO_LOUSA_DIGITAL		= "15";
	public static final String TP_EQUIPAMENTO_COMPUTADOR_MESA	= "16";
	public static final String TP_EQUIPAMENTO_COMPUTADOR_PORTATIL = "17";
	public static final String TP_EQUIPAMENTO_TABLET			= "18";
	
	/*Tipos de Acesso a internet*/
	public static final String TP_ACESSO_INTERNET_ADMINISTRATIVO 	= "01";
	public static final String TP_ACESSO_INTERNET_APRENDIZAGEM 		= "02";
	public static final String TP_ACESSO_INTERNET_ALUNOS 			= "03";
	public static final String TP_ACESSO_INTERNET_COMUNIDADE 		= "04";
	public static final String TP_ACESSO_INTERNET_NAO_POSSUI		= "05";
	

	/*Tipos de Equipamentos de acesso a internet pelos alunos*/
	public static final String TP_EQUIPAMENTO_ACESSO_INTERNET_ALUNOS_COMPUTADORES_ESCOLA 	= "01";
	public static final String TP_EQUIPAMENTO_ACESSO_INTERNET_ALUNOS_DISPOSIVOS_PESSOAIS	= "02";
	
	/*Tipos de recurso de interligacao de computadores*/
	public static final String TP_RECURSO_INTERLIGACAO_COMPUTADORES_CABO 	    = "01";
	public static final String TP_RECURSO_INTERLIGACAO_COMPUTADORES_WIRELESS 	= "02";
	public static final String TP_RECURSO_INTERLIGACAO_COMPUTADORES_NENHUM  	= "03";
	
	boolean possuiProfissionaisEscolaresAuxiliarSecretaria = false;
	boolean possuiProfissionaisEscolaresAuxiliarServicosGerais = false;
	boolean possuiProfissionaisEscolaresBibliotecario = false;
	boolean possuiProfissionaisEscolaresBombeiro = false;
	boolean possuiProfissionaisEscolaresCoordenador = false;
	boolean possuiProfissionaisEscolaresFonoaudiologo = false;
	boolean possuiProfissionaisEscolaresNutricionista = false;
	boolean possuiProfissionaisEscolaresPsicologo = false;
	boolean possuiProfissionaisEscolaresProfissionalAlimentar = false;
	boolean possuiProfissionaisEscolaresProfissionalApoio = false;
	boolean possuiProfissionaisEscolaresSecretarioEscolar = false;
	boolean possuiProfissionaisEscolaresSeguranca = false;
	boolean possuiProfissionaisEscolaresTecnicos = false;
	
	/*Tipos de profissionais escolares*/
	public static final String TP_PROFISSIONAIS_ESCOLARES_AUXILIAR_SECRETARIA 	    		= "01";
	public static final String TP_PROFISSIONAIS_ESCOLARES_AUXILIAR_SERVICOS_GERAIS 	    	= "02";
	public static final String TP_PROFISSIONAIS_ESCOLARES_BIBLIOTECARIO 	    			= "03";
	public static final String TP_PROFISSIONAIS_ESCOLARES_BOMBEIRO 	    					= "04";
	public static final String TP_PROFISSIONAIS_ESCOLARES_COORDENADOR 	    				= "05";
	public static final String TP_PROFISSIONAIS_ESCOLARES_FONOAUDIOLOGO 	    			= "06";
	public static final String TP_PROFISSIONAIS_ESCOLARES_NUTRICIONISTA 	    			= "07";
	public static final String TP_PROFISSIONAIS_ESCOLARES_PSICOLOGO 	    				= "08";
	public static final String TP_PROFISSIONAIS_ESCOLARES_PROFISSIONAL_ALIMENTAR 			= "09";
	public static final String TP_PROFISSIONAIS_ESCOLARES_PROFISSIONAL_APOIO 	    		= "10";
	public static final String TP_PROFISSIONAIS_ESCOLARES_SECRETARIO_ESCOLAR 	    		= "11";
	public static final String TP_PROFISSIONAIS_ESCOLARES_SEGURANCA 	    				= "12";
	public static final String TP_PROFISSIONAIS_ESCOLARES_TECNICOS 	    					= "13";
	
	/*Tipos de instrumentos pedagogicos*/
	public static final String TP_INSTRUMENTOS_PEDAGOGICOS_ACERVO_MULTIMIDIA 	    		= "01";
	public static final String TP_INSTRUMENTOS_PEDAGOGICOS_BRINQUEDOS_INFANTIL 	    		= "02";
	public static final String TP_INSTRUMENTOS_PEDAGOGICOS_MATERIAIS_CIENTIFICOS 	    	= "03";
	public static final String TP_INSTRUMENTOS_PEDAGOGICOS_EQUIPAMENTO_SOM 	    			= "04";
	public static final String TP_INSTRUMENTOS_PEDAGOGICOS_INSTURMENTOS_MUSICAIS 	    	= "05";
	public static final String TP_INSTRUMENTOS_PEDAGOGICOS_JOGOS_EDUCATIVOS 	    		= "06";
	public static final String TP_INSTRUMENTOS_PEDAGOGICOS_MATERIAIS_ARTISTICOS 	    	= "07";
	public static final String TP_INSTRUMENTOS_PEDAGOGICOS_MATERIAIS_DESPORTIVOS 	    	= "08";
	public static final String TP_INSTRUMENTOS_PEDAGOGICOS_MATERIAIS_EDUCACAO_INDIGENA 		= "09";
	public static final String TP_INSTRUMENTOS_PEDAGOGICOS_MATERIAIS_EDUCACAO_RACIAL 	    = "10";
	public static final String TP_INSTRUMENTOS_PEDAGOGICOS_MATERIAIS_EDUCACAO_CAMPO 	 	= "11";
	
	
	/*Tipos de grupos escolares*/
	public static final String TP_GRUPOS_ESCOLARES_ASSOCIACAO_PAIS 	    	= "01";
	public static final String TP_GRUPOS_ESCOLARES_ASSOCIACAO_PAIS_MESTRES 	= "02";
	public static final String TP_GRUPOS_ESCOLARES_CONSELHO_ESCOLAR 	    = "03";
	public static final String TP_GRUPOS_ESCOLARES_GREMIO_ESTUDANTIL 	    = "04";
	public static final String TP_GRUPOS_ESCOLARES_OUTROS 	    			= "05";
	public static final String TP_GRUPOS_ESCOLARES_NENHUM 	    			= "06";
	
	/*Tipos de Etapa*/
	public static final String TP_ETAPA_EDUCACAO_INFANTIL_0_3 			   			= "1";
	public static final String TP_ETAPA_EDUCACAO_INFANTIL_4_5 			   			= "2";
	public static final String TP_ETAPA_EDUCACAO_INFANTIL_0_5 			   			= "3";
	public static final String TP_ETAPA_ENSINO_FUNDAMENTAL_8 			   			= "4";
	public static final String TP_ETAPA_ENSINO_FUNDAMENTAL_9 			   			= "5";
	public static final String TP_ETAPA_ENSINO_FUNDAMENTAL_8_9 			   			= "6";
	public static final String TP_ETAPA_ENSINO_MEDIO		 			   			= "7";
	public static final String TP_ETAPA_ENSINO_MEDIO_INTEGRADO 			   			= "8";
	public static final String TP_ETAPA_ENSINO_MEDIO_NORMAL 			   			= "9";
	public static final String TP_ETAPA_ENSINO_MEDIO_PROFISSIONAL 		   			= "10";
	public static final String TP_ETAPA_EDUCACAO_INFANTIL_ESPECIAL_03      			= "11";
	public static final String TP_ETAPA_EDUCACAO_INFANTIL_ESPECIAL_4_5 	   			= "12";
	public static final String TP_ETAPA_ENSINO_FUNDAMENTAL_ESPECIAL_8 	   			= "13";
	public static final String TP_ETAPA_ENSINO_FUNDAMENTAL_ESPECIAL_9 	   			= "14";
	public static final String TP_ETAPA_ENSINO_MEDIO_ESPECIAL              			= "15";
	public static final String TP_ETAPA_ENSINO_MEDIO_ESPECIAL_INTEGRADO    			= "16";
	public static final String TP_ETAPA_ENSINO_MEDIO_ESPECIAL_NORMAL 	   			= "17";
	public static final String TP_ETAPA_ENSINO_MEDIO_ESPECIAL_PROFISSIONAL 			= "18";
	public static final String TP_ETAPA_EDUCACAO_ESPECIAL_EJA_FUNDAMENTAL  			= "19";
	public static final String TP_ETAPA_EDUCACAO_ESPECIAL_EJA_MEDIO 	   			= "20";
	public static final String TP_ETAPA_EDUCACAO_EJA_FUNDAMENTAL 		   			= "21";
	public static final String TP_ETAPA_EDUCACAO_EJA_FUNDAMENTAL_PROJOVEM  			= "22";
	public static final String TP_ETAPA_EDUCACAO_EJA_MEDIO 				   			= "23";
	public static final String TP_ETAPA_EDUCACAO_EJA_PROFISSIONAL_MISTA    			= "24";
	public static final String TP_ETAPA_EDUCACAO_EJA_ESPECIAL_PROFISSIONAL_MISTA    = "25";
	public static final String TP_ETAPA_EDUCACAO_INFANTIL_FUNDAMENTAL_8_9  			= "26";
	public static final String TP_ETAPA_EDUCACAO_INFANTIL_FUNDAMENTAL_ESPECIAL_8_9  = "27";
	
	/*Tipo de Atendimento Especializado*/
	public static final String TP_AEE_BRAILLE 							 = "1";
	public static final String TP_AEE_RECURSOS_OPTICOS 				 	 = "3";
	public static final String TP_AEE_DESENVOLVIMENTO_PROCESSOS_MENTAIS  = "4";
	public static final String TP_AEE_ORIENTACAO_MOBILIDADE 			 = "5";
	public static final String TP_AEE_LIBRAS 							 = "6";
	public static final String TP_AEE_CAA 								 = "7";
	public static final String TP_AEE_ENRIQUECIMENTO_CURRICULAR 		 = "8";
	public static final String TP_AEE_SOROBAN 							 = "9";
	public static final String TP_AEE_INFORMATICA_ACESSIVEL 			 = "10";
	public static final String TP_AEE_PORTUGUES_ESCRITO 				 = "11";
	public static final String TP_AEE_AUTONOMIA_AMBIENTE_ESCOLAR 		 = "12";
	
	/*Disciplinas*/
	public static final String DISC_QUIMICA 			= "1";
	public static final String DISC_FISICA 				= "2";
	public static final String DISC_MATEMATICA 			= "3";
	public static final String DISC_BIOLOGIA 			= "4";
	public static final String DISC_CIENCIAS 			= "5";
	public static final String DISC_LING_PORTUGUES 		= "6";
	public static final String DISC_LING_INGLES 		= "7";
	public static final String DISC_LING_ESPANHOL 		= "8";
	public static final String DISC_LING_FRANCES 		= "30";
	public static final String DISC_LING_OUTRA 			= "9";
	public static final String DISC_ARTES 				= "10";
	public static final String DISC_EDUCACAO_FISICA 	= "11";
	public static final String DISC_HISTORIA 			= "12";
	public static final String DISC_GEOGRAFIA 			= "13";
	public static final String DISC_FILOSOFIA 			= "14";
	public static final String DISC_ESTUDOS_SOCIAIS 	= "28";
	public static final String DISC_SOCIOLOGIA 			= "29";
	public static final String DISC_INFORMATICA 		= "16";
	public static final String DISC_PROFISSIONALIZANTES = "17";
	public static final String DISC_NESC_ESPECIAIS 		= "20";
	public static final String DISC_DIVER_SOCIOCULTURAL = "21";
	public static final String DISC_LIBRAS 				= "23";
	public static final String DISC_PEDAGOGICAS 		= "25";
	public static final String DISC_ENSINO_RELIGIOSO 	= "26";
	public static final String DISC_LING_INDIGENA 		= "27";
	public static final String DISC_OUTRAS 				= "99";
	
	/*Outros cursos de formação do professor*/
	public static final String CURSO_CRECHE_0_3 					= "01";
	public static final String CURSO_PRE_4_5 						= "02";
	public static final String CURSO_ANOS_INICIAIS_FUNDAMENTAL 		= "03";
	public static final String CURSO_ANOS_FINAIS_FUNDAMENTAL 		= "04";
	public static final String CURSO_MEDIO 							= "05";
	public static final String CURSO_EJA 							= "06";
	public static final String CURSO_EDUCACAO_ESPECIAL				= "07";
	public static final String CURSO_EDUCACAO_INDIGENA 				= "08";
	public static final String CURSO_EDUCACAO_CAMPO 				= "09";
	public static final String CURSO_EDUCACAO_AMBIENTAL 			= "10";
	public static final String CURSO_DIREITOS_HUMANOS 				= "11";
	public static final String CURSO_GENERO_DIVERSIDADE_SEXUAL 	  	= "12";
	public static final String CURSO_DIREITOS_CRIANCA_ADOLESCENTE 	= "13";
	public static final String CURSO_EDUCACAO_AFRO 					= "14";
	public static final String CURSO_OUTROS 						= "15";
	
	/*Funções padrão do sistema EDUCACENSO*/
	public static final String FUNCAO_PROFESSOR 	  = "01";
	public static final String FUNCAO_AUXILIAR 		  = "02";
	public static final String FUNCAO_MONITOR 		  = "03";
	public static final String FUNCAO_TRADUTOR_LIBRAS = "04";
	
	/*Tipo de NecessidadeEspecial*/
	public static final String TP_DEFICIENCIA 				  			  = "00";
	public static final String TP_CEGUEIRA 				  				  = "01";
	public static final String TP_BAIXA_VISAO 			  				  = "02";
	public static final String TP_SURDEZ 				  				  = "03";
	public static final String TP_DEFICIENCIA_AUDITIVA 	  				  = "04";
	public static final String TP_SURDOCEGUEIRA 		  				  = "05";
	public static final String TP_DEFICIENCIA_FISICA 	  				  = "06";
	public static final String TP_DEFICIENCIA_INTELECTUAL 				  = "07";
	public static final String TP_DEFICIENCIA_MULTIPLA 	  				  = "08";
	public static final String TP_DEFICIENCIA_AUTISMO_INFANTIL			  = "09";
	public static final String TP_DEFICIENCIA_SINDROME_ASPERGER			  = "10";
	public static final String TP_DEFICIENCIA_SINDROME_RETT				  = "11";
	public static final String TP_DEFICIENCIA_TRANSTORNO_DESINTEGRATIVO	  = "12";
	public static final String TP_DEFICIENCIA_SUPERDOTACAO				  = "13";
	public static final String TP_DEFICIENCIA_TRANSTORNO_ESPECTRO_AUTISTA = "14";
	
	/*Tipos de Recurso padrão*/
	public static final String TP_RECURSO_AUXILIO_LEDOR 		= "01";
	public static final String TP_RECURSO_AUXILIO_TRANSCRICAO 	= "02";
	public static final String TP_RECURSO_GUIA_INTERPRETE 		= "03";
	public static final String TP_RECURSO_INTERPRETE_LIBRAS 	= "04";
	public static final String TP_RECURSO_LEITURA_LABIAL 		= "05";
	public static final String TP_RECURSO_PROVA_AMPLIADA_16 	= "06";
	public static final String TP_RECURSO_PROVA_AMPLIADA_20 	= "07";
	public static final String TP_RECURSO_PROVA_AMPLIADA_24 	= "08";
	public static final String TP_RECURSO_PROVA_BRAILLE 		= "09";
	public static final String TP_RECURSO_PROVA_AMPLIADA_18		= "10";
	public static final String TP_RECURSO_CD_AUDIO_DEFICIENTE_VISUAL 				= "11";
	public static final String TP_RECURSO_PROVA_LINGUA_PORTUGUESA_SEGUNDA_LINGUA 	= "12";
	public static final String TP_RECURSO_PROVA_VIDEO_LIBRAS				 		= "13";
	
	/*Tipo de Transporte padrão*/
	public static final String TP_TRANSPORTE_VAN 				= "01";
	public static final String TP_TRANSPORTE_MICRO 				= "02";
	public static final String TP_TRANSPORTE_ONIBUS 			= "03";
	public static final String TP_TRANSPORTE_BICICLETA 			= "04";
	public static final String TP_TRANSPORTE_TRACAO_ANIMAL 		= "05";
	public static final String TP_TRANSPORTE_OUTRO 				= "06";
	public static final String TP_TRANSPORTE_EMBARCACAO_5 		= "07";
	public static final String TP_TRANSPORTE_EMBARCACAO_5_15 	= "08";
	public static final String TP_TRANSPORTE_EMBARCACAO_15_35 	= "09";
	public static final String TP_TRANSPORTE_EMBARCACAO_35 		= "10";
	public static final String TP_TRANSPORTE_METRO			 	= "11";
	
	/*Tipos de ocorrencia padrão*/
	public static final String TP_OCORRENCIA_ADMISSAO 					= "01";
	public static final String TP_OCORRENCIA_LIGACAO 					= "02";
	public static final String TP_OCORRENCIA_EMAIL_ENVIADO 				= "03";
	public static final String TP_OCORRENCIA_TRANSFERENCIA 				= "04";
	public static final String TP_OCORRENCIA_ATRIBUICAO 				= "05";
	public static final String TP_OCORRENCIA_CONCLUSAO 					= "06";
	public static final String TP_OCORRENCIA_REABERTURA 				= "07";
	public static final String TP_OCORRENCIA_MUDANCA_FASE 				= "08";
	public static final String TP_OCORRENCIA_RELEVANCIA 			  	= "09";
	public static final String TP_OCORRENCIA_FALTA_DOCUMENTACAO_ALUNO 	= "10";
	public static final String TP_OCORRENCIA_FALTA_DOCUMENTACAO_ESCOLA 	= "11";
	public static final String TP_OCORRENCIA_SOLICITACAO_TRANSFERENCIA 	= "12";
	public static final String TP_OCORRENCIA_CONCLUSAO_TRANSFERENCIA 	= "13";
	public static final String TP_OCORRENCIA_SOLICITACAO_QUADRO_VAGAS 	= "14";
	public static final String TP_OCORRENCIA_CONFIRMACAO_MATRICULA   	= "15";
	public static final String TP_OCORRENCIA_REMANEJAMENTO_ALUNO    	= "16";
	public static final String TP_OCORRENCIA_CONSERVACAO       			= "17";
	public static final String TP_OCORRENCIA_EVADIDO   					= "18";
	public static final String TP_OCORRENCIA_DESISTENTE   				= "19";
	public static final String TP_OCORRENCIA_ALTERAR_NUMERO_MATRICULA   = "20";
	public static final String TP_OCORRENCIA_ATIVACAO_MATRICULA         = "21";
	public static final String TP_OCORRENCIA_CANCELAMENTO_MATRICULA     = "22";
	public static final String TP_OCORRENCIA_INATIVO                    = "23";
	
	public static final String TP_OCORRENCIA_CADASTRO_INSTITUICAO       = "42";
	public static final String TP_OCORRENCIA_CADASTRO_TURMA             = "43";
	public static final String TP_OCORRENCIA_CADASTRO_PROFESSOR         = "44";
	public static final String TP_OCORRENCIA_CADASTRO_ALUNO             = "45";
	public static final String TP_OCORRENCIA_CADASTRO_OFERTA            = "46";
	
	public static final String TP_OCORRENCIA_RECLASSIFICACAO            = "47";
	public static final String TP_OCORRENCIA_CADASTRO_PESSOA_OFERTA     = "48";
	public static final String TP_OCORRENCIA_CADASTRO_MATRICULA         = "49";
	public static final String TP_OCORRENCIA_ALTERACAO_TURMA            = "50";
	public static final String TP_OCORRENCIA_ALTERACAO_PROFESSOR        = "51";
	public static final String TP_OCORRENCIA_ALTERACAO_ALUNO            = "52";
	public static final String TP_OCORRENCIA_ALTERACAO_MATRICULA        = "53";
	
	public static final String TP_OCORRENCIA_PENDENTE                   = "55";
	
	public static final String TP_OCORRENCIA_REALOCACAO  	            = "56";
	public static final String TP_OCORRENCIA_FALECIDO  	            	= "58";
	
	public static final String TP_OCORRENCIA_TRANSFERIDO                = "59";
	
	
	public static final String TP_OCORRENCIA_ALTERACAO_INSTITUICAO        	 = "60";
	public static final String TP_OCORRENCIA_CADASTRO_PERIODO_LETIVO      	 = "61";
	public static final String TP_OCORRENCIA_ALTERACAO_PERIODO_LETIVO     	 = "62";
	public static final String TP_OCORRENCIA_CADASTRO_INSTITUICAO_CURSO   	 = "63";
	public static final String TP_OCORRENCIA_REMOCAO_INSTITUICAO_CURSO    	 = "64";
	public static final String TP_OCORRENCIA_CADASTRO_INSTITUICAO_HORARIO 	 = "65";
	public static final String TP_OCORRENCIA_REMOCAO_INSTITUICAO_HORARIO  	 = "66";
	public static final String TP_OCORRENCIA_ALTERACAO_INSTITUICAO_HORARIO   = "67";
	
	public static final String TP_OCORRENCIA_ALTERACAO_SITUACAO_CENSO     	 = "68";
	
	public static final String TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO = "69";
	public static final String TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO  = "70";
	public static final String TP_OCORRENCIA_GERACAO_ARQUIVO_SITUACAO_ALUNO_CENSO  = "71";
	
	public static final String TP_OCORRENCIA_MATRICULA_CONCLUIDA             = "72";
	
	public static final String TP_OCORRENCIA_FINALIZACAO_ESCOLA_SITUACAO_ALUNO_CENSO  = "73";
	public static final String TP_OCORRENCIA_LIBERACAO_ESCOLA_SITUACAO_ALUNO_CENSO    = "74";
	
	public static final String TP_OCORRENCIA_FINALIZACAO_ESCOLA_MATRICULA_INICIAL  = "75";
	public static final String TP_OCORRENCIA_LIBERACAO_ESCOLA_MATRICULA_INICIAL    = "76";
	public static final String TP_OCORRENCIA_GERACAO_ARQUIVO_MATRICULA_INICIAL     = "77";
	public static final String TP_OCORRENCIA_DECLARAR_PRONTA_MATRICULA_INICIAL     = "78";
	public static final String TP_OCORRENCIA_DECLARAR_PENDENTE_MATRICULA_INICIAL   = "79";
	
	public static final String TP_OCORRENCIA_SALVAR_SITUACAO_FINAL_CENSO     = "80";
	
	public static final String TP_OCORRENCIA_ALTERAR_PARAMETRO_CONTROLE_EDF     = "81";
	
	public static final String TP_OCORRENCIA_ADICIONAR_HORARIO_TURMA 	 = "82";
	public static final String TP_OCORRENCIA_REMOVER_HORARIO_TURMA 	 = "83";
	
	public static final String TP_OCORRENCIA_ALTERACAO_OFERTA_AVALIACAO 	 = "84";
	public static final String TP_OCORRENCIA_DISCIPLINA_AVALIACAO_ALUNO 	 = "85";
	
	public static final String TP_OCORRENCIA_FECHAMENTO_PERIODOS 	 = "86";
	
	public static final String TP_OCORRENCIA_MATRICULA_PERMISSAO_ESPECIAL 	 = "87";
	
	public static final String TP_OCORRENCIA_SUBSTITUICAO_VEICULO_VIAGEM 	 = "88";
	public static final String TP_OCORRENCIA_ADICIONAR_VIAGEM_PREVIA 	     = "89";
	public static final String TP_OCORRENCIA_ADICIONAR_VIAGEM		 	     = "90";
	public static final String TP_OCORRENCIA_ATUALIZAR_VIAGEM		 	     = "91";
	public static final String TP_OCORRENCIA_CANCELAR_VIAGEM		 	     = "92";
	public static final String TP_OCORRENCIA_ENCERRAR_VIAGEM		 	     = "93";
	public static final String TP_OCORRENCIA_REMOVER_VIAGEM_PREVIA			 = "94";
	public static final String TP_OCORRENCIA_COLOCAR_VEICULO_MANUTENCAO 	 = "95";
	public static final String TP_OCORRENCIA_RETIRAR_VEICULO_MANUTENCAO 	 = "96";
	public static final String TP_OCORRENCIA_INATIVAR_VIAGEM_PREVIA			 = "97";
	
	public static final String TP_OCORRENCIA_REAPROVEITAMENTO_PERIODO_LETIVO = "98";
	public static final String TP_OCORRENCIA_REMOVER_PERIODO_LETIVO 		 = "99";
	
	public static final String TP_OCORRENCIA_ADICIONAR_MOTORISTA_FOLGA 		 = "100";
	public static final String TP_OCORRENCIA_ATUALIZAR_MOTORISTA_FOLGA 		 = "101";
	public static final String TP_OCORRENCIA_ADICIONAR_MOTORISTA_FOLGA_AGENDAMENTO = "102";
	public static final String TP_OCORRENCIA_ATUALIZAR_MOTORISTA_FOLGA_AGENDAMENTO = "103";
	public static final String TP_OCORRENCIA_EFETIVAR_FOLGA_MOTORISTA        = "104";
	public static final String TP_OCORRENCIA_CRIADO_POR_VIAGEM_AGENDAMENTO   = "105";
	public static final String TP_OCORRENCIA_FECHADA                         = "106";
	public static final String TP_OCORRENCIA_FECHAMENTO_MATRICULAS   	     = "107";
	public static final String TP_OCORRENCIA_CANCELAMENTO_DESISTENCIA_EVASAO = "108";
	
	/*Tipos de periodo padrão*/
	public static final String TP_PERIODO_LETIVO   						= "01";
	public static final String TP_PERIODO_MATRICULA   		  			= "02";
	public static final String TP_PERIODO_EXAMES_FINAIS   				= "03";
	public static final String TP_PERIODO_FERIAS_COLETIVAS				= "04";
	public static final String TP_PERIODO_RECESSO 						= "05";
	
	/*Tipos de área de conhecimento padrão*/
	public static final String TP_AREA_CONHECIMENTO_LINGUAGENS			= "01";
	public static final String TP_AREA_CONHECIMENTO_MATEMATICA   		= "02";
	public static final String TP_AREA_CONHECIMENTO_CIENCIAS_NATUREZA   = "03";
	public static final String TP_AREA_CONHECIMENTO_CIENCIAS_HUMANAS	= "04";
	public static final String TP_AREA_CONHECIMENTO_OUTRAS          	= "05";
	
	/*Tipos de documentacao padrão*/
	public static final String TP_DOCUMENTACAO_NIS   		  = "01";
	public static final String TP_DOCUMENTACAO_RG   		  = "02";
	public static final String TP_DOCUMENTACAO_PASSAPORTE     = "03";
	public static final String TP_DOCUMENTACAO_REG_NASCIMENTO = "04";
	public static final String TP_DOCUMENTACAO_REG_CASAMENTO  = "05";
	public static final String TP_DOCUMENTACAO_CPF            = "06";
	public static final String TP_DOCUMENTACAO_SUS            = "07";
	
	/*Tipo de Categoria de Escola Privada*/
	public static final int TP_CATEGORIA_ESCOLA_PRIVADA_PARTICULAR   = 1;
	public static final int TP_CATEGORIA_ESCOLA_PRIVADA_COMUNITARIA  = 2;
	public static final int TP_CATEGORIA_ESCOLA_PRIVADA_CONFESSIONAL = 3;
	public static final int TP_CATEGORIA_ESCOLA_PRIVADA_FILANTROPICA = 4;
	
	/*Tipo de Conveniencia com o poder público*/
	public static final int TP_CONVENIADA_PODER_PUBLICO_ESTADUAL   			= 1;
	public static final int TP_CONVENIADA_PODER_PUBLICO_MUNICIPAL   		= 2;
	public static final int TP_CONVENIADA_PODER_PUBLICO_ESTADUAL_MUNICIPAL  = 3;
	
	//Grupos de Validação
	public static final int GRUPO_VALIDACAO_INSERT = 0;
	public static final int GRUPO_VALIDACAO_UPDATE = 1;
	public static final int GRUPO_VALIDACAO_EDUCACENSO = 2;
		
	//Validações
	public static final int VALIDATE_EDUCACENSO_COMPUTADORES = 0;
	public static final int VALIDATE_EDUCACENSO_SITUACAO = 1;
	public static final int VALIDATE_EDUCACENSO_SALA_DE_AULA_DEPENDENCIA = 2;
	public static final int VALIDATE_EDUCACENSO_EQUIPAMENTO_SEM_QUANTIDADE = 3;
	public static final int VALIDATE_EDUCACENSO_INEP = 4;
	public static final int VALIDATE_EDUCACENSO_NOME_ORGAO_REGIONAL = 5;
	public static final int VALIDATE_EDUCACENSO_DEPENDENCIA_ADMINISTRATIVA = 6;
	public static final int VALIDATE_EDUCACENSO_LOCALIZACAO = 7;
	public static final int VALIDATE_EDUCACENSO_FORMA_OCUPACAO = 8;
	public static final int VALIDATE_EDUCACENSO_PREDIO_COMPARTILHADO = 9;
	public static final int VALIDATE_EDUCACENSO_REGULAMENTACAO = 10;
	public static final int VALIDATE_EDUCACENSO_AGUA_CONSUMIDA = 11;
	public static final int VALIDATE_EDUCACENSO_ABASTECIMENTO_AGUA = 12;
	public static final int VALIDATE_EDUCACENSO_ABASTECIMENTO_ENERGIA = 13;
	public static final int VALIDATE_EDUCACENSO_ESGOTO_SANITARIO = 14;
	public static final int VALIDATE_EDUCACENSO_DESTINACAO_LIXO = 15;
	public static final int VALIDATE_EDUCACENSO_SALAS_AULA = 16;
	public static final int VALIDATE_EDUCACENSO_QUANTIDADE_FUNCIONARIOS = 17;
	public static final int VALIDATE_EDUCACENSO_INTERNET = 18;
	public static final int VALIDATE_EDUCACENSO_BANDA_LARGA = 19;
	public static final int VALIDATE_EDUCACENSO_INTERNET_BANDA_LARGA = 20;
	public static final int VALIDATE_EDUCACENSO_ALIMENTACAO_ESCOLAR = 21;
	public static final int VALIDATE_EDUCACENSO_LOCALIZACAO_DIFERENCIADA = 22;
	public static final int VALIDATE_EDUCACENSO_MATERIAL_ESPECIFICO = 23;
	public static final int VALIDATE_EDUCACENSO_BRASIL_ALFABETIZADO = 24;
	public static final int VALIDATE_EDUCACENSO_ESPACO_COMUNIDADE = 25;
	public static final int VALIDATE_EDUCACENSO_DEPENDENCIAS = 26;
	public static final int VALIDATE_EDUCACENSO_HORARIOS = 27;
	public static final int VALIDATE_EDUCACENSO_LOCAL_FUNCIONAMENTO = 28;
	public static final int VALIDATE_EDUCACENSO_CATEGORIA_PRIVADA = 29;
	public static final int VALIDATE_EDUCACENSO_CONVENIADA_PODER_PUBLICO = 30;
	public static final int VALIDATE_EDUCACENSO_MANTENEDORA = 31;
	public static final int VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_MESMO_INEP_PRINCIPAL = 32;
	public static final int VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO = 33;
	public static final int VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_INVALIDO = 34;
	public static final int VALIDATE_EDUCACENSO_RECURSO_ACESSIBILIDADE = 35;
	public static final int VALIDATE_EDUCACENSO_ACESSO_INTERNET = 36;
	public static final int VALIDATE_EDUCACENSO_EQUIPAMENTO_ACESSO_INTERNET_ALUNO = 37;
	public static final int VALIDATE_EDUCACENSO_RECURSO_INTERLIGACAO_COMPUTADORES = 38;
	public static final int VALIDATE_EDUCACENSO_INSTRUMENTOS_PEDAGOGICOS = 39;
	public static final int VALIDATE_EDUCACENSO_GRUPOS_ESCOLARES = 40;
	public static final int VALIDATE_EDUCACENSO_CRITERIO_ACESSO_GESTOR = 41;
	public static final int VALIDATE_EDUCACENSO_TIPO_ADMISSAO_GESTOR = 42;
	public static final int VALIDATE_EDUCACENSO_GESTOR_NAO_LOTADO = 43;
	
	
	public static Result save(InstituicaoEducacenso instituicaoEducacenso, Instituicao instituicao){
		return save(instituicaoEducacenso, instituicao, null, null, null, null, null, null, null, null, 0, null);
	}

	public static Result save(InstituicaoEducacenso instituicaoEducacenso, Instituicao instituicao, AuthData authData){
		return save(instituicaoEducacenso, instituicao, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, authData, null);
	}
	
	public static Result save(InstituicaoEducacenso instituicaoEducacenso, Instituicao instituicao, ArrayList<Integer> locaisFuncionamento, ArrayList<Integer> tiposMantenedora, ArrayList<Integer> abastecimentoAgua, ArrayList<Integer> abastecimentoEnergia, ArrayList<Integer> esgotoSanitario, ArrayList<Integer> destinacaoLixo, ArrayList<Integer> tiposEquipamento, ArrayList<Integer> tiposEtapa, ArrayList<Integer> recursosAcessibilidade, ArrayList<Integer> acessoInternet, ArrayList<Integer> equipamentoAcessoInternetAluno, ArrayList<Integer> recursoInterligacaoComputadores, ArrayList<Integer> instrumentosPedagogicos, ArrayList<Integer> gruposEscolares, int cdUsuario){
		return save(instituicaoEducacenso, instituicao, locaisFuncionamento, tiposMantenedora, abastecimentoAgua, abastecimentoEnergia, esgotoSanitario, destinacaoLixo, tiposEquipamento, tiposEtapa, recursosAcessibilidade, acessoInternet, equipamentoAcessoInternetAluno, recursoInterligacaoComputadores, instrumentosPedagogicos, gruposEscolares, cdUsuario, null, null);
	}

	public static Result save(InstituicaoEducacenso instituicaoEducacenso, Instituicao instituicao, ArrayList<Integer> locaisFuncionamento, ArrayList<Integer> tiposMantenedora, ArrayList<Integer> abastecimentoAgua, ArrayList<Integer> abastecimentoEnergia, ArrayList<Integer> esgotoSanitario, ArrayList<Integer> destinacaoLixo, ArrayList<Integer> tiposEquipamento, ArrayList<Integer> tiposEtapa, int cdUsuario, AuthData authData){
		return save(instituicaoEducacenso, instituicao, locaisFuncionamento, tiposMantenedora, abastecimentoAgua, abastecimentoEnergia, esgotoSanitario, destinacaoLixo, tiposEquipamento, tiposEtapa, null, null, null, null, null, null, cdUsuario, authData, null);
	}

	public static Result save(InstituicaoEducacenso instituicaoEducacenso, Instituicao instituicao, ArrayList<Integer> locaisFuncionamento, ArrayList<Integer> tiposMantenedora, ArrayList<Integer> abastecimentoAgua, ArrayList<Integer> abastecimentoEnergia, ArrayList<Integer> esgotoSanitario, ArrayList<Integer> destinacaoLixo, ArrayList<Integer> tiposEquipamento, ArrayList<Integer> tiposEtapa, ArrayList<Integer> recursosAcessibilidade, ArrayList<Integer> acessoInternet, ArrayList<Integer> equipamentoAcessoInternetAluno, ArrayList<Integer> recursoInterligacaoComputadores, ArrayList<Integer> instrumentosPedagogicos, ArrayList<Integer> gruposEscolares, int cdUsuario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoEducacenso==null)
				return new Result(-1, "Erro ao salvar. InstituicaoEducacenso é nulo");

			
//			if(locaisFuncionamento.size() == 0){
//				if(isConnectionNull)
//					Conexao.rollback(connect);
//				return new Result(-1, "Deve haver pelo menos um local de funcionamento. Por favor verifique!");
//			}
			

			int retorno;
			if(instituicaoEducacenso.getCdInstituicao()==0){
				ValidatorResult resultadoValidacao = validate(instituicaoEducacenso, locaisFuncionamento, tiposMantenedora, abastecimentoAgua, abastecimentoEnergia, esgotoSanitario, destinacaoLixo, tiposEquipamento, tiposEtapa, recursosAcessibilidade, acessoInternet, equipamentoAcessoInternetAluno, recursoInterligacaoComputadores, instrumentosPedagogicos, gruposEscolares, cdUsuario, GRUPO_VALIDACAO_INSERT, connect);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
				
				retorno = InstituicaoEducacensoDAO.insert(instituicaoEducacenso, connect);
				instituicaoEducacenso.setCdInstituicao(retorno);
			}
			else {
				ValidatorResult resultadoValidacao = validate(instituicaoEducacenso, locaisFuncionamento, tiposMantenedora, abastecimentoAgua, abastecimentoEnergia, esgotoSanitario, destinacaoLixo, tiposEquipamento, tiposEtapa, recursosAcessibilidade, acessoInternet, equipamentoAcessoInternetAluno, recursoInterligacaoComputadores, instrumentosPedagogicos, gruposEscolares, cdUsuario, GRUPO_VALIDACAO_UPDATE, connect);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
				
				retorno = InstituicaoEducacensoDAO.update(instituicaoEducacenso, connect);
			}

			if(retorno < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao cadastrar Educacenso");
			}
			
			instituicao.setCdInstituicao(instituicaoEducacenso.getCdInstituicao());
			instituicao.setCdEmpresa(instituicaoEducacenso.getCdInstituicao());
			instituicao.setCdPessoa(instituicaoEducacenso.getCdInstituicao());
			
			retorno = InstituicaoDAO.update(instituicao, connect);
			
			if(retorno < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao atualizar Instituicao");
			}
			
//			if(instituicaoEducacenso.getQtTotalFuncionarios() <= 0){
//				if(isConnectionNull)
//					Conexao.rollback(connect);
//				return new Result(-1, "Total de Funcionários deve ser maior do que zero. Por favor verifique!");
//			}
			

//			int qtSalasDependencia = 0;
//			ResultSetMap rsmDependencias = InstituicaoServices.getAllDependencias(instituicao.getCdInstituicao(), connect);
//			while(rsmDependencias.next()){
//				if(rsmDependencias.getInt("cd_tipo_dependencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_DEPENDENCIA_INSTITUICAO_SALA_AULA)){
//					qtSalasDependencia++;
//				}
//			}
//			rsmDependencias.beforeFirst();
			
//			if((instituicaoEducacenso.getQtSalaAula() + instituicaoEducacenso.getQtSalaAulaExterna()) != qtSalasDependencia){
//				if(isConnectionNull)
//					Conexao.rollback(connect);
//				return new Result(-1, "Número de salas de aula informadas não é condizente com a quantidade de dependências do tipo Sala de Aula. Por favor verifique!");
//			}
			

			/**
			 * TIPOS DE MANTENEDORA
			 */
			if(tiposMantenedora!=null && tiposMantenedora.size()>0) {
				retorno = InstituicaoServices.clearTipoMantenedora(instituicao.getCdInstituicao(), instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				for (Integer cd : tiposMantenedora) {
					retorno = InstituicaoServices.addTipoMantenedora(instituicao.getCdInstituicao(), cd, instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar tipos de mantenedora!");
				}
			}
			if(tiposMantenedora!=null && tiposMantenedora.size() == 0){
				int ret = InstituicaoServices.clearTipoMantenedora(instituicao.getCdInstituicao(), instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar tipos de mantenedora!");
				}
			}
			
			
			/**
			 * LOCAIS DE FUNCIONAMENTO
			 */
			if(locaisFuncionamento!=null && locaisFuncionamento.size()>0) {
				retorno = InstituicaoServices.clearLocalFuncionamento(instituicao.getCdInstituicao(), instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				
				for (Integer cd : locaisFuncionamento) {
					retorno = InstituicaoServices.addLocalFuncionamento(instituicao.getCdInstituicao(), cd, instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar locais de funcionamento!");
				}
			}
			if(locaisFuncionamento!=null && locaisFuncionamento.size()==0) {
				int ret = InstituicaoServices.clearLocalFuncionamento(instituicao.getCdInstituicao(), instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar locais de funcionamento!");
				}
			}
			
			/**
			 * ABASTECIMENTO AGUA
			 */
			if(abastecimentoAgua!=null && abastecimentoAgua.size()>0) {
				retorno = InstituicaoServices.clearAbastecimentoAgua(instituicao.getCdInstituicao(), instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				
				for (Integer cd : abastecimentoAgua) {
					retorno = InstituicaoServices.addAbastecimentoAgua(instituicao.getCdInstituicao(), cd, instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar abastecimento de agua!");
				}
			}
			if(abastecimentoAgua!=null && abastecimentoAgua.size()==0) {
				int ret = InstituicaoServices.clearAbastecimentoAgua(instituicao.getCdInstituicao(), instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar abastecimento de agua!");
				}
			}

			
			
			/**
			 * ABASTECIMENTO DE ENERGIA
			 */
			if(abastecimentoEnergia!=null && abastecimentoEnergia.size()>0) {
				retorno = InstituicaoServices.clearAbastecimentoEnergia(instituicao.getCdInstituicao(), instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				
				for (Integer cd : abastecimentoEnergia) {
					retorno = InstituicaoServices.addAbastecimentoEnergia(instituicao.getCdInstituicao(), cd, instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar abastecimento de energia!");
				}
			}
			if(abastecimentoEnergia!=null && abastecimentoEnergia.size()==0) {
				int ret = InstituicaoServices.clearAbastecimentoEnergia(instituicao.getCdInstituicao(), instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar abastecimento de energia!");
				}
			}

			
			/**
			 * ESGOTO SANITARIO
			 */
			if(esgotoSanitario!=null && esgotoSanitario.size()>0) {
				retorno = InstituicaoServices.clearEsgotoSanitario(instituicao.getCdInstituicao(), instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				
				for (Integer cd : esgotoSanitario) {
					retorno = InstituicaoServices.addEsgotoSanitario(instituicao.getCdInstituicao(), cd, instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar esgoto sanitario!");
				}
			}
			if(esgotoSanitario!=null && esgotoSanitario.size()==0) {
				int ret = InstituicaoServices.clearEsgotoSanitario(instituicao.getCdInstituicao(), instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar esgoto sanitario!");
				}
			}	
				

			
			/**
			 * DESTINACAO DE LIXO
			 */
			if(destinacaoLixo!=null && destinacaoLixo.size()>0) {
				retorno = InstituicaoServices.clearDestinacaoLixo(instituicao.getCdInstituicao(), instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				
				for (Integer cd : destinacaoLixo) {
					retorno = InstituicaoServices.addDestinacaoLixo(instituicao.getCdInstituicao(), cd, instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar destinacao de lixo!");
				}
			}
			if(destinacaoLixo!=null && destinacaoLixo.size()==0) {
				int ret = InstituicaoServices.clearDestinacaoLixo(instituicao.getCdInstituicao(), instituicaoEducacenso.getCdPeriodoLetivo(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar destinacao de lixo!");
				}
			}
				

			
			/**
			 * TIPO DE EQUIPAMENTO
			 */
			if(tiposEquipamento!=null && tiposEquipamento.size()>0) {
				retorno = InstituicaoServices.clearTipoEquipamento(instituicao.getCdInstituicao(), connect).getCode();

				for (Integer cd : tiposEquipamento) {
					retorno = InstituicaoServices.addTipoEquipamento(instituicao.getCdInstituicao(), cd, connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar tipos de equipamento!");
				}
			}
			if(tiposEquipamento!=null && tiposEquipamento.size()==0) {
				int ret = InstituicaoServices.clearTipoEquipamento(instituicao.getCdInstituicao(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar tipos de equipamento!");
				}
			}
				
			
			
			/**
			 * TIPO DE ETAPA
			 */
			if(tiposEtapa!=null && tiposEtapa.size()>0) {
				retorno = InstituicaoServices.clearTipoEtapa(instituicao.getCdInstituicao(), connect).getCode();
				
				for (Integer cd : tiposEtapa) {
					retorno = InstituicaoServices.addTipoEtapa(instituicao.getCdInstituicao(), cd, connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar tipos de etapa!");
				}
			}
			if(tiposEtapa!=null && tiposEtapa.size()==0) {
				int ret = InstituicaoServices.clearTipoEtapa(instituicao.getCdInstituicao(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar tipos de etapa!");
				}				
			}
			
			/**
			 * RECURSO ACESSIBILIDADE
			 */
			if(recursosAcessibilidade!=null && recursosAcessibilidade.size()>0) {
				retorno = InstituicaoServices.clearRecursoAcessibilidade(instituicao.getCdInstituicao(), connect).getCode();
				
				for (Integer cd : recursosAcessibilidade) {
					retorno = InstituicaoServices.addRecursoAcessibilidade(instituicao.getCdInstituicao(), cd, connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar recurso de acessibilidade!");
				}
			}
			if(recursosAcessibilidade!=null && recursosAcessibilidade.size()==0) {
				int ret = InstituicaoServices.clearRecursoAcessibilidade(instituicao.getCdInstituicao(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar recurso de acessibilidade!");
				}				
			}
				
			/**
			 * ACESSO INTERNET
			 */
			if(acessoInternet!=null && acessoInternet.size()>0) {
				retorno = InstituicaoServices.clearAcessoInternet(instituicao.getCdInstituicao(), connect).getCode();
				
				for (Integer cd : acessoInternet) {
					retorno = InstituicaoServices.addAcessoInternet(instituicao.getCdInstituicao(), cd, connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar acesso internet!");
				}
			}
			if(acessoInternet!=null && acessoInternet.size()==0) {
				int ret = InstituicaoServices.clearAcessoInternet(instituicao.getCdInstituicao(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar acesso internet");
				}				
			}
			
			/**
			 * EQUIPAMENTO ACESSO INTERNET ALUNO
			 */
			if(equipamentoAcessoInternetAluno!=null && equipamentoAcessoInternetAluno.size()>0) {
				retorno = InstituicaoServices.clearEquipamentoAcessoInternetAluno(instituicao.getCdInstituicao(), connect).getCode();
				
				for (Integer cd : equipamentoAcessoInternetAluno) {
					retorno = InstituicaoServices.addEquipamentoAcessoInternetAluno(instituicao.getCdInstituicao(), cd, connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar equipamento acesso internet luno!");
				}
			}
			if(equipamentoAcessoInternetAluno!=null && equipamentoAcessoInternetAluno.size()==0) {
				int ret = InstituicaoServices.clearEquipamentoAcessoInternetAluno(instituicao.getCdInstituicao(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar equipamento acesso internet luno");
				}				
			}
			
			/**
			 * RECURSO INTERLIGACAO COMPUTADORES
			 */
			if(recursoInterligacaoComputadores!=null && recursoInterligacaoComputadores.size()>0) {
				retorno = InstituicaoServices.clearRecursoInterligacaoComputadores(instituicao.getCdInstituicao(), connect).getCode();
				
				for (Integer cd : recursoInterligacaoComputadores) {
					retorno = InstituicaoServices.addRecursoInterligacaoComputadores(instituicao.getCdInstituicao(), cd, connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar equipamento acesso internet luno!");
				}
			}
			if(recursoInterligacaoComputadores!=null && recursoInterligacaoComputadores.size()==0) {
				int ret = InstituicaoServices.clearRecursoInterligacaoComputadores(instituicao.getCdInstituicao(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar equipamento acesso internet luno");
				}				
			}
				
			/**
			 * INSTRUMENTOS PEDAGOGICOS
			 */
			if(instrumentosPedagogicos!=null && instrumentosPedagogicos.size()>0) {
				retorno = InstituicaoServices.clearInstrumentosPedagogicos(instituicao.getCdInstituicao(), connect).getCode();
				
				for (Integer cd : instrumentosPedagogicos) {
					retorno = InstituicaoServices.addInstrumentosPedagogicos(instituicao.getCdInstituicao(), cd, connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar equipamento acesso instrumentos pedagogicos!");
				}
			}
			if(instrumentosPedagogicos!=null && instrumentosPedagogicos.size()==0) {
				int ret = InstituicaoServices.clearInstrumentosPedagogicos(instituicao.getCdInstituicao(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar equipamento acesso instrumentos pedagogicos");
				}				
			}
				
			/**
			 * GRUPOS ESCOLARES
			 */
			if(gruposEscolares!=null && gruposEscolares.size()>0) {
				retorno = InstituicaoServices.clearGruposEscolares(instituicao.getCdInstituicao(), connect).getCode();
				
				for (Integer cd : gruposEscolares) {
					retorno = InstituicaoServices.addGruposEscolares(instituicao.getCdInstituicao(), cd, connect).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar equipamento acesso grupos escolares!");
				}
			}
			if(gruposEscolares!=null && gruposEscolares.size()==0) {
				int ret = InstituicaoServices.clearGruposEscolares(instituicao.getCdInstituicao(), connect).getCode();
				if(ret<0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar equipamento acesso grupos escolares");
				}				
			}
				
				
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOEDUCACENSO", instituicaoEducacenso);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getResumoEducacensoByPeriodo(int cdPeriodoLetivoSuperior) {
		return getResumoEducacensoByPeriodo(cdPeriodoLetivoSuperior, null);
	}
	
	public static ResultSetMap getResumoEducacensoByPeriodo(int cdPeriodoLetivoSuperior, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			pstmt = connect.prepareStatement("SELECT A.*, B.*, B.nm_pessoa AS nm_instituicao, "
				    + "B.nm_pessoa AS nm_fantasia, B.nr_telefone1, B.nr_telefone2, B.nr_fax, " 
			        + "B.nm_email, B.nm_url, B.nm_pessoa as nm_pessoa, C.cd_periodo_letivo, C.nr_inep, C.st_instituicao_publica " 
			        + " FROM acd_instituicao A "
					+ " JOIN grl_pessoa B ON( A.cd_instituicao = B.cd_pessoa ) "
					+ " JOIN acd_instituicao_periodo IP ON( A.cd_instituicao = IP.cd_instituicao AND IP.cd_periodo_letivo_superior = "+cdPeriodoLetivoSuperior+" ) "
					+ " JOIN acd_instituicao_educacenso C ON( A.cd_instituicao = C.cd_instituicao AND C.cd_periodo_letivo = IP.cd_periodo_letivo ) "
					+ " WHERE 1=1 " 
					+ "   AND A.cd_instituicao <> " + ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0) 
					+ "   AND C.st_instituicao_publica IN (" + InstituicaoEducacensoServices.ST_EM_ATIVIDADE + ", " + InstituicaoEducacensoServices.ST_PARALISADA + ")"
					+ " ORDER BY B.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				
				if(rsm.getInt("st_instituicao_publica") == InstituicaoEducacensoServices.ST_PARALISADA){
					rsm.setValueToField("NM_INSTITUICAO", rsm.getString("NM_INSTITUICAO") + " (Paralisada)");
				}
				
				ResultSetMap rsmAlunos = InstituicaoServices.getAlunosOf(rsm.getInt("cd_instituicao"), rsm.getInt("cd_periodo_letivo"), true, connect);
				rsm.setValueToField("NR_ALUNOS", rsmAlunos.size());
				
				ResultSetMap rsmCirculo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao_circulo A, acd_circulo B WHERE A.cd_circulo = B.cd_circulo AND A.cd_instituicao = " + rsm.getInt("cd_instituicao") + " AND B.tp_circulo IN (0, 1)").executeQuery());
				if(rsmCirculo.next()){
					rsm.setValueToField("NM_CIRCULO", rsmCirculo.getString("nm_circulo"));
					rsm.setValueToField("NR_ORDEM", 2);
				}
				else{
					if(rsm.getInt("tp_instituicao") == InstituicaoServices.TP_INSTITUICAO_CRECHE){
						rsm.setValueToField("NM_CIRCULO", "CRECHE");
						rsm.setValueToField("NR_ORDEM", 0);
					}
					else{
						rsm.setValueToField("NM_CIRCULO", "URBANA");
						rsm.setValueToField("NR_ORDEM", 1);
					}
				}
				
				/*
				 * MATRICULA INICIAL
				 * */
				
				String nmEstadoFinal = "Matrícula Inicial - Não está pronta";
				
				ResultSetMap rsmDeclaracaoMatriculaInicial = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_ocorrencia A, acd_ocorrencia_instituicao B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_DECLARAR_PRONTA_MATRICULA_INICIAL + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_DECLARAR_PENDENTE_MATRICULA_INICIAL+ ") AND cd_instituicao = "+rsm.getInt("cd_instituicao")+" AND cd_periodo_letivo = "+rsm.getInt("cd_periodo_letivo")+" order by dt_ocorrencia DESC").executeQuery());
				if(rsmDeclaracaoMatriculaInicial.size() == 0){
					rsm.setValueToField("CL_DECLARACAO_MATRICULA_INICIAL", "Não está pronta");
					rsm.setValueToField("_FECHADO_CL_DECLARACAO_MATRICULA_INICIAL", 0);
				}
				else{
					if(rsmDeclaracaoMatriculaInicial.next()){
						if(rsmDeclaracaoMatriculaInicial.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_DECLARAR_PRONTA_MATRICULA_INICIAL)){
							rsm.setValueToField("CL_DECLARACAO_MATRICULA_INICIAL", "Pronta");
							rsm.setValueToField("_FECHADO_CL_DECLARACAO_MATRICULA_INICIAL", 1);
							nmEstadoFinal = "Matrícula Inicial - Pronta";
						}
						else{
							rsm.setValueToField("CL_DECLARACAO_MATRICULA_INICIAL", "Não está pronta");
							rsm.setValueToField("_FECHADO_CL_DECLARACAO_MATRICULA_INICIAL", 0);
						}
					}
				}
				
				
				ResultSetMap rsmDeclaracaoGeracaoArquivoMatriculaInicial = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_ocorrencia A, acd_ocorrencia_instituicao B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_GERACAO_ARQUIVO_MATRICULA_INICIAL + ") AND cd_instituicao = "+rsm.getInt("cd_instituicao")+" AND cd_periodo_letivo = "+rsm.getInt("cd_periodo_letivo")+" order by dt_ocorrencia DESC").executeQuery());
				if(rsmDeclaracaoGeracaoArquivoMatriculaInicial.size() == 0){
					rsm.setValueToField("CL_GERACAO_ARQUIVO_MATRICULA_INICIAL", "Arquivo não gerado");
					rsm.setValueToField("_FECHADO_CL_GERACAO_ARQUIVO_MATRICULA_INICIAL", 0);
				}
				else{
					rsm.setValueToField("CL_GERACAO_ARQUIVO_MATRICULA_INICIAL", "Arquivo gerado");
					rsm.setValueToField("_FECHADO_CL_GERACAO_ARQUIVO_MATRICULA_INICIAL", 1);
					nmEstadoFinal = "Matrícula Inicial - Arquivo Gerado";
				}
				
				
				ResultSetMap rsmFinalizacaoMatriculaInicial = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_ocorrencia A, acd_ocorrencia_instituicao B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_MATRICULA_INICIAL + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_ESCOLA_MATRICULA_INICIAL + ") AND cd_instituicao = "+rsm.getInt("cd_instituicao")+" AND cd_periodo_letivo = "+rsm.getInt("cd_periodo_letivo")+" order by dt_ocorrencia DESC").executeQuery());
				if(rsmFinalizacaoMatriculaInicial.size() == 0){
					rsm.setValueToField("CL_FINALIZACAO_MATRICULA_INICIAL", "Não finalizada");
					rsm.setValueToField("_FECHADO_CL_FINALIZACAO_MATRICULA_INICIAL", 0);
				}
				else{
					if(rsmFinalizacaoMatriculaInicial.next()){
						if(rsmFinalizacaoMatriculaInicial.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_MATRICULA_INICIAL)){
							rsm.setValueToField("CL_FINALIZACAO_MATRICULA_INICIAL", "Finalizada");
							rsm.setValueToField("_FECHADO_CL_FINALIZACAO_MATRICULA_INICIAL", 1);
							nmEstadoFinal = "Matrícula Inicial - Finalizada";
						}
						else{
							rsm.setValueToField("CL_FINALIZACAO_MATRICULA_INICIAL", "Não finalizada");
							rsm.setValueToField("_FECHADO_CL_FINALIZACAO_MATRICULA_INICIAL", 0);
						}
					}
				}
				
				
				/*
				 * SITUACAO DO ALUNO
				 * */
				
				ResultSetMap rsmTurmas = InstituicaoServices.getAllTurmasByInstituicaoPeriodo(rsm.getInt("cd_instituicao"), rsm.getInt("cd_periodo_letivo"), false, false, false, 0, -1, true);
				float qtTurmasFinalizadas    = 0;
				while(rsmTurmas.next()){
					ResultSetMap rsmTurmaSituacaoAlunoCenso = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_ocorrencia A, acd_ocorrencia_turma B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO + ") AND cd_turma = "+rsmTurmas.getInt("cd_turma")+" order by dt_ocorrencia DESC").executeQuery());
					if(rsmTurmaSituacaoAlunoCenso.next()){
						if(rsmTurmaSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO)){
							qtTurmasFinalizadas++;
						}
					}
				}
				rsmTurmas.beforeFirst();
				float qtTurmasTotal = (float)rsmTurmas.size();
				if(rsmTurmas.size() > 0 && rsmTurmas.size() == qtTurmasFinalizadas){
					rsm.setValueToField("CL_DECLARACAO_SITUACAO_ALUNO", Util.formatNumber((qtTurmasTotal > 0 ? (qtTurmasFinalizadas/qtTurmasTotal * 100) : 0), 2) + "% de turmas fechadas");
					rsm.setValueToField("_FECHADO_CL_DECLARACAO_SITUACAO_ALUNO", 1);
					nmEstadoFinal = "Situação do aluno - Pronta";
				}
				else{
					rsm.setValueToField("CL_DECLARACAO_SITUACAO_ALUNO", Util.formatNumber((qtTurmasTotal > 0 ? (qtTurmasFinalizadas/qtTurmasTotal * 100) : 0), 2) + "% de turmas fechadas");
					rsm.setValueToField("_FECHADO_CL_DECLARACAO_SITUACAO_ALUNO", ((qtTurmasTotal > 0 ? (qtTurmasFinalizadas/qtTurmasTotal * 100) : 0) == 0 ? 0 : 2));
				}
				
				
				ResultSetMap rsmDeclaracaoGeracaoArquivoSituacaoAluno = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_ocorrencia A, acd_ocorrencia_instituicao B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_GERACAO_ARQUIVO_SITUACAO_ALUNO_CENSO + ") AND cd_instituicao = "+rsm.getInt("cd_instituicao")+" AND cd_periodo_letivo = "+rsm.getInt("cd_periodo_letivo")+" order by dt_ocorrencia DESC").executeQuery());
				if(rsmDeclaracaoGeracaoArquivoSituacaoAluno.size() == 0){
					rsm.setValueToField("CL_GERACAO_ARQUIVO_SITUACAO_ALUNO", "Arquivo não gerado");
					rsm.setValueToField("_FECHADO_CL_GERACAO_ARQUIVO_SITUACAO_ALUNO", 0);
				}
				else{
					rsm.setValueToField("CL_GERACAO_ARQUIVO_SITUACAO_ALUNO", "Arquivo gerado");
					rsm.setValueToField("_FECHADO_CL_GERACAO_ARQUIVO_SITUACAO_ALUNO", 1);
					nmEstadoFinal = "Situação do aluno - Arquivo Gerado";
				}
				
				
				ResultSetMap rsmFinalizacaoSituacaoAluno = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_ocorrencia A, acd_ocorrencia_instituicao B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_ESCOLA_SITUACAO_ALUNO_CENSO + ") AND cd_instituicao = "+rsm.getInt("cd_instituicao")+" AND cd_periodo_letivo = "+rsm.getInt("cd_periodo_letivo")+" order by dt_ocorrencia DESC").executeQuery());
				if(rsmFinalizacaoSituacaoAluno.size() == 0){
					rsm.setValueToField("CL_FINALIZACAO_SITUACAO_ALUNO", "Não finalizada");
					rsm.setValueToField("_FECHADO_CL_FINALIZACAO_SITUACAO_ALUNO", 0);
				}
				else{
					if(rsmFinalizacaoSituacaoAluno.next()){
						if(rsmFinalizacaoSituacaoAluno.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_SITUACAO_ALUNO_CENSO)){
							rsm.setValueToField("CL_FINALIZACAO_SITUACAO_ALUNO", "Finalizada");
							rsm.setValueToField("_FECHADO_CL_FINALIZACAO_SITUACAO_ALUNO", 1);
							nmEstadoFinal = "Situação do aluno - Finalizada";
						}
						else{
							rsm.setValueToField("CL_FINALIZACAO_SITUACAO_ALUNO", "Não finalizada");
							rsm.setValueToField("_FECHADO_CL_FINALIZACAO_SITUACAO_ALUNO", 0);
						}
					}
				}
						
				rsm.setValueToField("NM_ESTADO", nmEstadoFinal);
				
			}
			rsm.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NR_ORDEM");
			fields.add("NM_CIRCULO");
			fields.add("NM_INSTITUICAO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int convertSituacaoAlunoCenso(int cdMatricula, int stAlunoCenso){
		return convertSituacaoAlunoCenso(cdMatricula, stAlunoCenso, null);
	}
	
	public static int convertSituacaoAlunoCenso(int cdMatricula, int stAlunoCenso, Connection connect){
		
		Matricula matricula = MatriculaDAO.get(cdMatricula, connect);
		
		Curso curso = CursoDAO.get(matricula.getCdCurso(), connect);
		TipoEtapa tipoEtapa = null;
		ArrayList<ItemComparator>criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_curso", "" + curso.getCdCurso(), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmCursoEtapa = CursoEtapaDAO.find(criterios, connect);
		if(rsmCursoEtapa.next()){
			tipoEtapa = TipoEtapaDAO.get(rsmCursoEtapa.getInt("cd_etapa"), connect);
		}
		
		HashMap<Integer, Integer> conversao = new HashMap<Integer, Integer>();
		conversao.put(MatriculaServices.ST_ALUNO_CENSO_TRANSFERIDO, MatriculaServices.ST_ALUNO_CENSO_TRANSFERIDO_CONVERSAO);
		conversao.put(MatriculaServices.ST_ALUNO_CENSO_EVADIDO, MatriculaServices.ST_ALUNO_CENSO_DEIXOU_FREQUENTAR_CONVERSAO);
		conversao.put(MatriculaServices.ST_ALUNO_CENSO_DESISTENTE, MatriculaServices.ST_ALUNO_CENSO_DEIXOU_FREQUENTAR_CONVERSAO);
		conversao.put(MatriculaServices.ST_ALUNO_CENSO_FALECIDO, MatriculaServices.ST_ALUNO_CENSO_FALECIDO_CONVERSAO);
		
		if(tipoEtapa.getIdEtapa().equals(InstituicaoEducacensoServices.TP_ETAPA_EDUCACAO_INFANTIL_0_3)
			|| tipoEtapa.getIdEtapa().equals(InstituicaoEducacensoServices.TP_ETAPA_EDUCACAO_INFANTIL_4_5)
			|| tipoEtapa.getIdEtapa().equals(InstituicaoEducacensoServices.TP_ETAPA_EDUCACAO_INFANTIL_0_5)){
			conversao.put(MatriculaServices.ST_ALUNO_CENSO_APROVADO, MatriculaServices.ST_ALUNO_CENSO_EM_ANDAMENTO_CONVERSAO);
			conversao.put(MatriculaServices.ST_ALUNO_CENSO_REPROVADO, MatriculaServices.ST_ALUNO_CENSO_EM_ANDAMENTO_CONVERSAO);
		}
		
		else if(curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_8_ANOS_8)
			|| curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_9_ANOS_9)
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_MEDIO_3)
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_MEDIO_4)
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_MEDIO_NAO_SERIADA)
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_MEDIO_INTEGRADO_3)
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_MEDIO_INTEGRADO_4)
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_MEDIO_INTEGRADO_NAO_SERIADA)
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_MEDIO_MAGISTERIO_3)
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_MEDIO_MAGISTERIO_4)
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_PROFISSIONAL_CONCOMITANTE)
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_PROFISSIONAL_SUBSEQUENTE)
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_EJA_PROFISSIONAL_MEDIO_FIC)
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_CONCOMITANTE_FIC)
		    || curso.getIdProdutoServico().equals(CursoServices.TP_ETAPA_CURSO_EJA_FINAIS + "8")
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_EJA_MEDIO)
		    || curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_EJA_PROFISSIONAL_FUNDAMENTAL_FIC)){
			
			conversao.put(MatriculaServices.ST_ALUNO_CENSO_APROVADO, MatriculaServices.ST_ALUNO_CENSO_APROVADO_CONCLUINTE_CONVERSAO);
			conversao.put(MatriculaServices.ST_ALUNO_CENSO_REPROVADO, MatriculaServices.ST_ALUNO_CENSO_REPROVADO_CONVERSAO);
		}
		
		else if(curso.getIdProdutoServico().equals(CursoServices.TP_ETAPA_CURSO_EJA_PRESENCIAL_FUNDAMENTAL_PROURBANO)){
			conversao.put(MatriculaServices.ST_ALUNO_CENSO_APROVADO, MatriculaServices.ST_ALUNO_CENSO_EM_ANDAMENTO_CONVERSAO);
			conversao.put(MatriculaServices.ST_ALUNO_CENSO_REPROVADO, MatriculaServices.ST_ALUNO_CENSO_EM_ANDAMENTO_CONVERSAO);
		}
		
		else{
			conversao.put(MatriculaServices.ST_ALUNO_CENSO_APROVADO, MatriculaServices.ST_ALUNO_CENSO_APROVADO_CONVERSAO);
			conversao.put(MatriculaServices.ST_ALUNO_CENSO_REPROVADO, MatriculaServices.ST_ALUNO_CENSO_REPROVADO_CONVERSAO);
		}
		
		return conversao.get(matricula.getStAlunoCenso());
	}
	
	public static ResultSetMap findRecente(ArrayList<ItemComparator> criterios) {
		
		ArrayList<ItemComparator> crt = (ArrayList<ItemComparator>)criterios.clone();
		
		crt.add(new ItemComparator("C.st_periodo_letivo", "" + InstituicaoPeriodoServices.ST_PENDENTE, ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsm = find(crt, null);
		
		if(rsm.size() == 0){
			crt = (ArrayList<ItemComparator>)criterios.clone();
			crt.add(new ItemComparator("C.st_periodo_letivo", "" + InstituicaoPeriodoServices.ST_ATUAL, ItemComparator.EQUAL, Types.INTEGER));
			
			return find(crt, null);
		}
		
		return rsm;
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsm = Search.find("SELECT A.*, B.*, C.*, D.nm_pessoa AS NM_INSTITUICAO, PJ.*, P.*, E.* FROM acd_instituicao_educacenso A "
					+ "			  JOIN acd_instituicao B ON (A.cd_instituicao = B.cd_instituicao) "
					+ "			  JOIN grl_pessoa_juridica PJ ON (A.cd_instituicao = PJ.cd_pessoa) "
					+ "			  JOIN grl_pessoa P ON (A.cd_instituicao = P.cd_pessoa) "
					+ "			  JOIN grl_empresa E ON (A.cd_instituicao = E.cd_empresa) "
					+ "			  JOIN acd_instituicao_periodo C ON (A.cd_instituicao = C.cd_instituicao"
					+ "													AND A.cd_periodo_letivo = C.cd_periodo_letivo) "
					+ "			  JOIN grl_pessoa D ON (A.cd_instituicao = D.cd_pessoa)", "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			while(rsm.next()){
				ResultSetMap rsmFinalizacaoMatriculaInicial = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_ocorrencia A, acd_ocorrencia_instituicao B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_MATRICULA_INICIAL + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_ESCOLA_MATRICULA_INICIAL + ") AND cd_instituicao = "+rsm.getInt("cd_instituicao")+" AND cd_periodo_letivo = "+rsm.getInt("cd_periodo_letivo")+" order by dt_ocorrencia DESC").executeQuery());
				if(rsmFinalizacaoMatriculaInicial.size() == 0){
					rsm.setValueToField("LG_FINALIZADA", 0);
				}
				else{
					if(rsmFinalizacaoMatriculaInicial.next()){
						if(rsmFinalizacaoMatriculaInicial.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FINALIZACAO_ESCOLA_MATRICULA_INICIAL)){
							rsm.setValueToField("LG_FINALIZADA", 1);
						}
						else{
							rsm.setValueToField("LG_FINALIZADA", 0);
						}
					}
				}
				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int ret = InstituicaoEducacensoDAO.delete(cdInstituicao, cdPeriodoLetivo, connect);
			if(ret<0){
				Conexao.rollback(connect);
				return new Result(ret, "Erro ao excluir instituição horário");
			}
			
			
			if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result validacaoEducacenso(InstituicaoEducacenso educacenso, int tpRelatorio, int cdUsuario){
		return validacaoEducacenso(educacenso, tpRelatorio, cdUsuario, null);
	}
	
	public static Result validacaoEducacenso(InstituicaoEducacenso educacenso, int tpRelatorio, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//Buscar Locais de Funcionamento em forma de array
			ArrayList<Integer> locaisFuncionamento = new ArrayList<Integer>();
			ResultSetMap rsmLocaisFuncionamento = InstituicaoServices.getLocalFuncionamento(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmLocaisFuncionamento.next()){
				locaisFuncionamento.add(rsmLocaisFuncionamento.getInt("cd_local_funcionamento"));
			}
			rsmLocaisFuncionamento.beforeFirst();
			
			//Buscar Tipos de Mantenedora em forma de array
			ArrayList<Integer> tiposMantenedora = new ArrayList<Integer>();
			ResultSetMap rsmTiposMantenedora = InstituicaoServices.getTipoMantenedora(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmTiposMantenedora.next()){
				tiposMantenedora.add(rsmTiposMantenedora.getInt("cd_tipo_mantenedora"));
			}
			rsmTiposMantenedora.beforeFirst();
			
			//Buscar Tipos de Abastecimento de Agua em forma de array
			ArrayList<Integer> abastecimentoAgua = new ArrayList<Integer>();
			ResultSetMap rsmAbastecimentoAgua = InstituicaoServices.getAbastecimentoAgua(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmAbastecimentoAgua.next()){
				abastecimentoAgua.add(rsmAbastecimentoAgua.getInt("cd_abastecimento_agua"));
			}
			rsmAbastecimentoAgua.beforeFirst();
			
			//Buscar Tipos de Abastecimento de Energia em forma de array
			ArrayList<Integer> abastecimentoEnergia = new ArrayList<Integer>();
			ResultSetMap rsmAbastecimentoEnergia = InstituicaoServices.getAbastecimentoEnergia(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmAbastecimentoEnergia.next()){
				abastecimentoEnergia.add(rsmAbastecimentoEnergia.getInt("cd_abastecimento_energia"));
			}
			rsmAbastecimentoEnergia.beforeFirst();
			
			//Buscar Tipos de Esgoto Sanitario em forma de array
			ArrayList<Integer> esgotoSanitario = new ArrayList<Integer>();
			ResultSetMap rsmEsgotoSanitario = InstituicaoServices.getEsgotoSanitario(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmEsgotoSanitario.next()){
				esgotoSanitario.add(rsmEsgotoSanitario.getInt("cd_esgoto_sanitario"));
			}
			rsmEsgotoSanitario.beforeFirst();
			
			//Buscar Tipos de Destinacao Lixo em forma de array
			ArrayList<Integer> destinacaoLixo = new ArrayList<Integer>();
			ResultSetMap rsmDestinacaoLixo = InstituicaoServices.getDestinacaoLixo(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmDestinacaoLixo.next()){
				destinacaoLixo.add(rsmDestinacaoLixo.getInt("cd_destinacao_lixo"));
			}
			rsmDestinacaoLixo.beforeFirst();
			
			//Buscar Tipos de Equimaneto em forma de array
			ArrayList<Integer> tiposEquipamento = new ArrayList<Integer>();
			ResultSetMap rsmTiposDeEquipamento = InstituicaoServices.getTipoEquipamento(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmTiposDeEquipamento.next()){
				tiposEquipamento.add(rsmTiposDeEquipamento.getInt("cd_tipo_equipamento"));
			}
			rsmTiposDeEquipamento.beforeFirst();
			
			//Buscar Tipos de Etapa em forma de array
			ArrayList<Integer> tiposEtapa = new ArrayList<Integer>();
			ResultSetMap rsmTipoEtapa = InstituicaoServices.getTipoEtapa(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmTipoEtapa.next()){
				tiposEtapa.add(rsmTipoEtapa.getInt("cd_tipo_etapa"));
			}
			rsmTipoEtapa.beforeFirst();
			
			ArrayList<Integer> recursosAcessibilidade = new ArrayList<Integer>();
			ResultSetMap rsmRecursosAcessibilidade = InstituicaoServices.getAllRecursosAcessibilidade(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmRecursosAcessibilidade.next()){
				recursosAcessibilidade.add(rsmRecursosAcessibilidade.getInt("cd_tipo_recurso_acessibilidade"));
			}
			rsmRecursosAcessibilidade.beforeFirst();
			
			ArrayList<Integer> acessoInternet = new ArrayList<Integer>();
			ResultSetMap rsmAcessoInternet = InstituicaoServices.getAllAcessoInternet(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmAcessoInternet.next()){
				acessoInternet.add(rsmAcessoInternet.getInt("cd_tipo_acesso_internet"));
			}
			rsmAcessoInternet.beforeFirst();
			
			ArrayList<Integer> equipamentoAcessoInternetAluno = new ArrayList<Integer>();
			ResultSetMap rsmEquipamentoAcessoInternetAluno = InstituicaoServices.getAllEquipamentoAcessoInternetAluno(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmEquipamentoAcessoInternetAluno.next()){
				equipamentoAcessoInternetAluno.add(rsmEquipamentoAcessoInternetAluno.getInt("cd_tipo_equipamento_acesso_internet_aluno"));
			}
			rsmEquipamentoAcessoInternetAluno.beforeFirst();

			ArrayList<Integer> recursoInterligacaoComputadores = new ArrayList<Integer>();
			ResultSetMap rsmRecursoInterligacaoComputadores = InstituicaoServices.getAllRecursoInterligacaoComputadores(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmRecursoInterligacaoComputadores.next()){
				recursoInterligacaoComputadores.add(rsmRecursoInterligacaoComputadores.getInt("cd_tipo_recurso_interligacao_computadores"));
			}
			rsmRecursoInterligacaoComputadores.beforeFirst();
			
			ArrayList<Integer> instrumentosPedagogicos = new ArrayList<Integer>();
			ResultSetMap rsmInstrumentosPedagogicos = InstituicaoServices.getAllInstrumentosPedagogicos(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmInstrumentosPedagogicos.next()){
				instrumentosPedagogicos.add(rsmInstrumentosPedagogicos.getInt("cd_tipo_instrumentos_pedagogicos"));
			}
			rsmInstrumentosPedagogicos.beforeFirst();
			
			ArrayList<Integer> gruposEscolares = new ArrayList<Integer>();
			ResultSetMap rsmGruposEscolares = InstituicaoServices.getAllGruposEscolares(educacenso.getCdInstituicao(), educacenso.getCdPeriodoLetivo());
			while(rsmGruposEscolares.next()){
				gruposEscolares.add(rsmGruposEscolares.getInt("cd_tipo_grupos_escolares"));
			}
			rsmGruposEscolares.beforeFirst();
			
			
			ValidatorResult resultadoValidacao = validate(educacenso, locaisFuncionamento, tiposMantenedora, abastecimentoAgua, abastecimentoEnergia, esgotoSanitario, destinacaoLixo, tiposEquipamento, tiposEtapa, recursosAcessibilidade, acessoInternet, equipamentoAcessoInternetAluno, recursoInterligacaoComputadores, instrumentosPedagogicos, gruposEscolares, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
			Result resultValidacoesPendencia = InstituicaoPendenciaServices.atualizarValidacaoPendencia(resultadoValidacao, educacenso.getCdInstituicao(), 0/*cdTurma*/, 0/*cdAluno*/, 0/*cdProfessor*/, InstituicaoPendenciaServices.TP_REGISTRO_EDUCACENSO_CENSO, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
			if(resultValidacoesPendencia.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return resultValidacoesPendencia;
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result =  new Result(1, "Atualização na validação do educacenso realizada com sucesso");
			return result;
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1);
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ValidatorResult validate(InstituicaoEducacenso educacenso, int idGrupo){
		return validate(educacenso, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, idGrupo, null);
	}
	
	public static ValidatorResult validate(InstituicaoEducacenso educacenso, int idGrupo, Connection connect){
		return validate(educacenso, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 0, idGrupo, connect);
	}
	
	public static ValidatorResult validate(InstituicaoEducacenso educacenso, 
			ArrayList<Integer> locaisFuncionamento, ArrayList<Integer> tiposMantenedora, 
			ArrayList<Integer> abastecimentoAgua, ArrayList<Integer> abastecimentoEnergia, ArrayList<Integer> esgotoSanitario, ArrayList<Integer> destinacaoLixo, ArrayList<Integer> tiposEquipamento, ArrayList<Integer> tiposEtapa, ArrayList<Integer> recursosAcessibilidade, ArrayList<Integer> acessoInternet, ArrayList<Integer> equipamentoAcessoInternetAluno, ArrayList<Integer> recursoInterligacaoComputadores, ArrayList<Integer> instrumentosPedagogicos, ArrayList<Integer> gruposEscolares, int cdUsuario, int idGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(educacenso == null){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new ValidatorResult(ValidatorResult.ERROR, "Educacenso não encontrado");
			}
			
			ValidatorResult result = new ValidatorResult(ValidatorResult.VALID, "Educacenso passou pela validação");
			HashMap<Integer, Validator> listValidator = getListValidation();
			
			//COMPUTADORES  - Saber se a quantidade de computadores informada no Educacenso é equivalente ao número de equipamentos do tipo computador
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_instituicao", "" + educacenso.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_periodo_letivo", "" + educacenso.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmEquipamentosInstituicao = InstituicaoTipoEquipamentoDAO.find(criterios, connect);
			boolean lgPossuiComputadores = false;
			while(rsmEquipamentosInstituicao.next()){
				
				if(rsmEquipamentosInstituicao.getInt("qt_equipamento") == 0){
					listValidator.get(VALIDATE_EDUCACENSO_EQUIPAMENTO_SEM_QUANTIDADE).add(ValidatorResult.ERROR, "Existe equipamentos informados com a quantidade zerada");
				}
				
				if(rsmEquipamentosInstituicao.getInt("cd_tipo_equipamento") == Integer.parseInt(InstituicaoEducacensoServices.TP_EQUIPAMENTO_COMPUTADORES)){
//					if(rsmEquipamentosInstituicao.getInt("qt_equipamento") != (educacenso.getQtComputadorAluno() + educacenso.getQtComputadorAdministrativo())){
//						listValidator.get(VALIDATE_EDUCACENSO_COMPUTADORES).add(ValidatorResult.ERROR, "A soma das quantidade de computador para os alunos e administrativo não condiz com o número de equipamentos do tipo 'Computador' informada", GRUPO_VALIDACAO_EDUCACENSO);
//					}
					lgPossuiComputadores = true;
					break;
				}
			}
			
			//SITUAÇÃO - Saber se o valor de situação do educacenso é válida
			if(educacenso.getStInstituicaoPublica() < InstituicaoEducacensoServices.ST_EM_ATIVIDADE || educacenso.getStInstituicaoPublica() > InstituicaoEducacensoServices.ST_EXTINTA){
				listValidator.get(VALIDATE_EDUCACENSO_SITUACAO).add(ValidatorResult.ERROR, "O valor de 'Situação' não é válido");
			}
			
			
			//DEPENDENCIAS - Verificar se existe ao menos uma dependencia cadastrada
			ResultSetMap rsmDependencias = InstituicaoServices.getAllDependencias(educacenso.getCdInstituicao(), connect);
			if(rsmDependencias == null || rsmDependencias.size() == 0){
				listValidator.get(VALIDATE_EDUCACENSO_DEPENDENCIAS).add(ValidatorResult.ERROR, "Nenhuma dependência foi registrada", GRUPO_VALIDACAO_EDUCACENSO);
			}
			else{
				//SALA DE AULA DEPENDENCIA - Verificar se a quantidade de salas de aula interna e externa corresponde a soma de dependencias do tipo Sala de Aula
				int qtSalasDependencia = 0;
				while(rsmDependencias.next()){
					if(rsmDependencias.getInt("cd_tipo_dependencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_DEPENDENCIA_INSTITUICAO_SALA_AULA)){
						qtSalasDependencia++;
					}
				}
				rsmDependencias.beforeFirst();
				if((educacenso.getQtSalaAula() + educacenso.getQtSalaAulaExterna()) != qtSalasDependencia){
					listValidator.get(VALIDATE_EDUCACENSO_SALA_DE_AULA_DEPENDENCIA).add(ValidatorResult.ALERT, "A soma das quantidades de salas de aula e salas de aula externas não corresponde ao número de dependências do tipo 'Sala de Aula' informada", GRUPO_VALIDACAO_UPDATE);
				}
				
			}
			
			//INEP - Verificar se existe número de INEP
			if(educacenso.getNrInep() == null || educacenso.getNrInep().trim().equals("") || educacenso.getNrInep().trim().length() != 8){
				listValidator.get(VALIDATE_EDUCACENSO_INEP).add(ValidatorResult.ERROR, "Não foi informado o número do INEP");
			}
						
			//ORGAO REGIONAL - Verificar se existe nome do orgão regional
			if(educacenso.getNmOrgaoRegional() == null || educacenso.getNmOrgaoRegional().trim().equals("")){
				listValidator.get(VALIDATE_EDUCACENSO_NOME_ORGAO_REGIONAL).add(ValidatorResult.ERROR, "Não foi informado o nome do orgão regional");
			}
						
			//DEPENDENCIA ADMINISTRATIVA - Verificar se o valor de dependencia administrativa é válido
			if(educacenso.getTpDependenciaAdministrativa() < Integer.parseInt(InstituicaoEducacensoServices.TP_DEPENDENCIA_ADMINISTRATIVA_FEDERAL) || educacenso.getTpDependenciaAdministrativa() > Integer.parseInt(InstituicaoEducacensoServices.TP_DEPENDENCIA_ADMINISTRATIVA_PRIVADA)){
				listValidator.get(VALIDATE_EDUCACENSO_DEPENDENCIA_ADMINISTRATIVA).add(ValidatorResult.ERROR, "O valor de 'Tipo de dependência administrativa' não é válido");
			}
						
			//LOCALIZAÇÃO - Verificar se o valor de localização é válido
			if(educacenso.getTpLocalizacao() < InstituicaoEducacensoServices.TP_LOCALIZACAO_URBANA || educacenso.getTpLocalizacao() > InstituicaoEducacensoServices.TP_LOCALIZACAO_RURAL){
				listValidator.get(VALIDATE_EDUCACENSO_LOCALIZACAO).add(ValidatorResult.ERROR, "O valor de 'Localização' não é válido");
			}
						
			//FORMA DE OCUPACAO - Verificar se o valor de forma de ocupaca é válido
			boolean achadoPredioEscolar = false;
			boolean achadoGalpao  = false;
			for(int index : locaisFuncionamento){
				LocalFuncionamento localFuncionamento = LocalFuncionamentoDAO.get(index, connect);
				if(localFuncionamento.getIdLocalFuncionamento().equals(ID_LOCAL_FUNCIONAMENTO_PREDIO)){
					achadoPredioEscolar = true;
				}
				else if(localFuncionamento.getIdLocalFuncionamento().equals(ID_LOCAL_FUNCIONAMENTO_GALPAO)){
					achadoGalpao = true;
				}
				
			}
			if((achadoPredioEscolar || achadoGalpao) && (educacenso.getTpFormaOcupacao() < InstituicaoEducacensoServices.TP_OCUPACAO_PROPRIO || educacenso.getTpFormaOcupacao() > InstituicaoEducacensoServices.TP_OCUPACAO_CEDIDO)){
				listValidator.get(VALIDATE_EDUCACENSO_FORMA_OCUPACAO).add(ValidatorResult.ERROR, "O valor de 'Forma de ocupação' não é válido");
			}
						
			//PREDIO COMPARTILHADO - Verificar se o valor de predio compartilhado é válido
			if(educacenso.getLgPredioCompartilhado() < 0 || educacenso.getLgPredioCompartilhado() > 1){
				listValidator.get(VALIDATE_EDUCACENSO_PREDIO_COMPARTILHADO).add(ValidatorResult.ERROR, "O valor de 'Prédio compartilhado' não é válido");
			}
						
			//REGULAMENTACAO - Verificar se o valor de regulamentacao é válido
			if(educacenso.getStRegulamentacao() < InstituicaoEducacensoServices.ST_REGULAMENTACAO_NAO || educacenso.getStRegulamentacao() > InstituicaoEducacensoServices.ST_REGULAMENTACAO_TRAMITANDO){
				listValidator.get(VALIDATE_EDUCACENSO_REGULAMENTACAO).add(ValidatorResult.ERROR, "O valor de 'Regulamentação' não é válido");
			}
						
			//AGUA CONSUMIDA - Verificar se o valor de agua consumida é válido
			if(educacenso.getStAguaConsumida() < 0 || educacenso.getStAguaConsumida() > 1){
				listValidator.get(VALIDATE_EDUCACENSO_AGUA_CONSUMIDA).add(ValidatorResult.ERROR, "O valor de 'Água consumida' não é válido");
			}
						
			//ABASTECIMENTO DE AGUA - Verificar se existe pelo menos um registro de abastecimento de agua
			if(abastecimentoAgua == null || abastecimentoAgua.size() == 0){
				listValidator.get(VALIDATE_EDUCACENSO_ABASTECIMENTO_AGUA).add(ValidatorResult.ERROR, "Não foi registrado nenhuma forma de abastecimento de água");
			}
						
			//ABASTECIMENTO DE ENERGIA - Verificar se existe pelo menos um registro de abastecimento de energia
			if(abastecimentoEnergia == null || abastecimentoEnergia.size() == 0){
				listValidator.get(VALIDATE_EDUCACENSO_ABASTECIMENTO_ENERGIA).add(ValidatorResult.ERROR, "Não foi registrado nenhuma forma de abastecimento de energia");
			}
						
			//ESGOTO SANITARIO - Verificar se existe pelo menos um registro de esgoto sanitario
			if(esgotoSanitario == null || esgotoSanitario.size() == 0){
				listValidator.get(VALIDATE_EDUCACENSO_ESGOTO_SANITARIO).add(ValidatorResult.ERROR, "Não foi registrado nenhuma forma de esgoto sanitário");
			}
						
			//DESTINACAO DE LIXO - Verificar se existe pelo menos um registro de destinacao de lixo
			if(destinacaoLixo == null || destinacaoLixo.size() == 0){
				listValidator.get(VALIDATE_EDUCACENSO_DESTINACAO_LIXO).add(ValidatorResult.ERROR, "Não foi registrado nenhuma forma de destinação de lixo");
			}
			
			//RECURSO ACESSIBILIDADE - Verificar se existe pelo menos um registro de recurso de acessibilidade
			if(recursosAcessibilidade == null || recursosAcessibilidade.size() == 0){
				listValidator.get(VALIDATE_EDUCACENSO_RECURSO_ACESSIBILIDADE).add(ValidatorResult.ERROR, "Não foi registrado nenhuma forma de recurso de acessibilidade");
			}
			
			//ACESSO INTERNET - Verificar se existe pelo menos um registro de acesso a internet
			if(acessoInternet == null || acessoInternet.size() == 0){
				listValidator.get(VALIDATE_EDUCACENSO_ACESSO_INTERNET).add(ValidatorResult.ERROR, "Não foi registrado nenhuma forma de acesso a internet");
			}
			
			boolean lgAcessoInternetAluno = false;
			for(Integer cdTipoAcessoInternet : acessoInternet){
				TipoAcessoInternet tipoAcessoInternet = TipoAcessoInternetDAO.get(cdTipoAcessoInternet, connect);
				if(tipoAcessoInternet.getIdTipoAcessoInternet().equals(InstituicaoEducacensoServices.TP_ACESSO_INTERNET_ALUNOS)){
					lgAcessoInternetAluno = true;
					break;
				}
			}
			
			//EQUIPAMENTO ACESSO INTERNET ALUNO - Verificar se existe pelo menos um registro de equipamento de acesso a internet do aluno
			if(lgAcessoInternetAluno && (equipamentoAcessoInternetAluno == null || equipamentoAcessoInternetAluno.size() == 0)){
				listValidator.get(VALIDATE_EDUCACENSO_EQUIPAMENTO_ACESSO_INTERNET_ALUNO).add(ValidatorResult.ERROR, "Foi registrado acesso a internet pelos alunos, porém não foi registrado nenhum equipamento de acesso a internet do aluno");
			}
			
			//RECURSO INTERLIGACAO COMPUTADORES - Verificar se existe pelo menos um registro de recurso de interligação de computadores
			if(recursoInterligacaoComputadores == null || recursoInterligacaoComputadores.size() == 0){
				listValidator.get(VALIDATE_EDUCACENSO_RECURSO_INTERLIGACAO_COMPUTADORES).add(ValidatorResult.ERROR, "Não foi registrado nenhum recurso de interligação de computadores");
			}
			
			//GRUPOS ESCOLARES - Verificar se existe pelo menos um registro de grupos escolares
			if(gruposEscolares == null || gruposEscolares.size() == 0){
				listValidator.get(VALIDATE_EDUCACENSO_GRUPOS_ESCOLARES).add(ValidatorResult.ERROR, "Não foi registrado nenhuma forma de grupos escolares");
			}
						
			//SALAS DE AULA - Verifica se existe pelo menos uma sala de aula na escola
			if((educacenso.getQtSalaAula() + educacenso.getQtSalaAulaExterna()) == 0){
				listValidator.get(VALIDATE_EDUCACENSO_SALAS_AULA).add(ValidatorResult.ERROR, "O número de salas de aula foi informada zerada");
			}
						
			//INTERNET - Verifica se o valor do Internet é válido
			if(educacenso.getLgInternet() < 0 || educacenso.getLgInternet() > 1){
				listValidator.get(VALIDATE_EDUCACENSO_INTERNET).add(ValidatorResult.ERROR, "O valor de 'Internet' não é válido");
			}
						
			//BANDA LARGA - Verifica se caso a Internet estiver ativa, se o valor de Banda Larga é válido
			if(educacenso.getLgInternet() == 1 && (educacenso.getLgBandaLarga() < 0 || educacenso.getLgBandaLarga() > 1)){
				listValidator.get(VALIDATE_EDUCACENSO_BANDA_LARGA).add(ValidatorResult.ERROR, "O valor de 'Banda Larga' não é válido");
			}
						
			//ALIMENTACAO ESCOLAR - Verificar se o valor de Alimentação Escolar é válido
			if(educacenso.getLgAlimentacaoEscolar() < 0 || educacenso.getLgAlimentacaoEscolar() > 1){
				listValidator.get(VALIDATE_EDUCACENSO_ALIMENTACAO_ESCOLAR).add(ValidatorResult.ERROR, "O valor de 'Alimentação Escolar' não é válido");
			}
						
			//LOCALIZACAO DIFERENCIADA - Verificar se o valor de Localização Diferenciada é válido
			if(educacenso.getTpLocalizacaoDiferenciada() < Integer.parseInt(InstituicaoEducacensoServices.TP_LOCALIZACAO_DIFERENCIADA_ASSENTAMENTO) || educacenso.getTpLocalizacaoDiferenciada() > Integer.parseInt(InstituicaoEducacensoServices.TP_LOCALIZACAO_DIFERENCIADA_NAO_APLICA)){
				listValidator.get(VALIDATE_EDUCACENSO_LOCALIZACAO_DIFERENCIADA).add(ValidatorResult.ERROR, "O valor de 'Localização Diferenciada' não é válido");
			}
						
			//MATERIAL ESPECIFICO - Verificar se o valor de Material Especifico é válido
			if(educacenso.getTpMaterialEspecifico() < InstituicaoEducacensoServices.TP_MATERIAL_ESPECIFICO_QUILOMBOLAS && educacenso.getTpMaterialEspecifico() > InstituicaoEducacensoServices.TP_MATERIAL_ESPECIFICO_NAO_SE_APLICA){
				listValidator.get(VALIDATE_EDUCACENSO_MATERIAL_ESPECIFICO).add(ValidatorResult.ERROR, "O valor de 'Material Específico' não é válido");
			}
						
			//BRASIL ALFBETIZADO - Verificar se o valor em Brasil ALfabetizado é válido
			if(educacenso.getLgBrasilAlfabetizado() < 0 && educacenso.getLgBrasilAlfabetizado() > 1){
				listValidator.get(VALIDATE_EDUCACENSO_BRASIL_ALFABETIZADO).add(ValidatorResult.ERROR, "O valor de 'Brasil Alfabetizado' não é válido");
			}
						
			//ESPACO PARA A COMUNIDADE - Verificar se o valor de Espaço para a comunidade é válido
			if(educacenso.getLgEspacoComunidade() <= -1 && educacenso.getLgEspacoComunidade() >= 2){
				listValidator.get(VALIDATE_EDUCACENSO_ESPACO_COMUNIDADE).add(ValidatorResult.ERROR, "O valor de 'Espaço para a comunidade' não é válido");
			}
						
			//LOCAIS DE FUNCIONAMENTO - Verificar se existe ao menos um local de funcionamento cadastrado
			if(locaisFuncionamento != null && locaisFuncionamento.size() == 0){
				listValidator.get(VALIDATE_EDUCACENSO_LOCAL_FUNCIONAMENTO).add(ValidatorResult.ERROR, "Nenhum local de funcionamento foi registrado");
			}
			
			//CATEGORIA DE ESCOLA PRIVADA - Verificar caso a escola seja privada, se a categoria da mesma está com o valor válido
			if(educacenso.getStInstituicaoPublica() == ST_EM_ATIVIDADE && educacenso.getTpDependenciaAdministrativa() == Integer.parseInt(TP_DEPENDENCIA_ADMINISTRATIVA_PRIVADA) && 
				(educacenso.getTpCategoriaPrivada() < TP_CATEGORIA_ESCOLA_PRIVADA_PARTICULAR || educacenso.getTpCategoriaPrivada() > TP_CATEGORIA_ESCOLA_PRIVADA_FILANTROPICA)){
				listValidator.get(VALIDATE_EDUCACENSO_CATEGORIA_PRIVADA).add(ValidatorResult.ERROR, "O valor de 'Categoria de escola' privada não é válido");
			}
			
			//CONVENIADO COM O PODER PUBLICO - Verificar caso a escola seja privada, se a conveniencia com o poder público está com o valor válido
//			NAO OBRIGATORIO
//			if(educacenso.getStInstituicaoPublica() == ST_EM_ATIVIDADE && educacenso.getTpDependenciaAdministrativa() == Integer.parseInt(TP_DEPENDENCIA_ADMINISTRATIVA_PRIVADA) && 
//				(educacenso.getTpConvenio() < TP_CONVENIADA_PODER_PUBLICO_ESTADUAL || educacenso.getTpConvenio() > TP_CONVENIADA_PODER_PUBLICO_ESTADUAL_MUNICIPAL)){
//				result.addResult(VALIDATE_EDUCACENSO_CONVENIADA_PODER_PUBLICO, ValidatorResult.ERROR);
//			}
			
			//TIPO DE MANTENEDORA - Verificar se caso a escola seja privada, se existe mantenedora cadastrada
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_instituicao", "" + educacenso.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_periodo_letivo", "" + educacenso.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmMantenedoraInstituicao = InstituicaoTipoMantenedoraDAO.find(criterios, connect);
			if(educacenso.getStInstituicaoPublica() == ST_EM_ATIVIDADE && educacenso.getTpDependenciaAdministrativa() == Integer.parseInt(TP_DEPENDENCIA_ADMINISTRATIVA_PRIVADA) && 
				rsmMantenedoraInstituicao.size() == 0){
				listValidator.get(VALIDATE_EDUCACENSO_MANTENEDORA).add(ValidatorResult.ERROR, "Faltando cadastrar a mantenedora da escola");
			}
			
			
			if(educacenso.getNrInep().equals(educacenso.getNrCodigoEscolaCompartilhada1())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_MESMO_INEP_PRINCIPAL).add(ValidatorResult.ERROR, "O código da escola 1 com prédio compartilhado é o mesmo da instituição principal");
			}
			
			if(educacenso.getNrInep().equals(educacenso.getNrCodigoEscolaCompartilhada2())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_MESMO_INEP_PRINCIPAL).add(ValidatorResult.ERROR, "O código da escola 2 com prédio compartilhado é o mesmo da instituição principal");
			}
			
			if(educacenso.getNrInep().equals(educacenso.getNrCodigoEscolaCompartilhada3())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_MESMO_INEP_PRINCIPAL).add(ValidatorResult.ERROR, "O código da escola 3 com prédio compartilhado é o mesmo da instituição principal");
			}
			
			if(educacenso.getNrInep().equals(educacenso.getNrCodigoEscolaCompartilhada4())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_MESMO_INEP_PRINCIPAL).add(ValidatorResult.ERROR, "O código da escola 4 com prédio compartilhado é o mesmo da instituição principal");
			}
			
			if(educacenso.getNrInep().equals(educacenso.getNrCodigoEscolaCompartilhada5())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_MESMO_INEP_PRINCIPAL).add(ValidatorResult.ERROR, "O código da escola 5 com prédio compartilhado é o mesmo da instituição principal");
			}
			
			if(educacenso.getNrInep().equals(educacenso.getNrCodigoEscolaCompartilhada6())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_MESMO_INEP_PRINCIPAL).add(ValidatorResult.ERROR, "O código da escola 6 com prédio compartilhado é o mesmo da instituição principal");
			}
			
			
			//Comparação do codigo 1
			if(educacenso.getNrCodigoEscolaCompartilhada1() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada1().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada1().equals(educacenso.getNrCodigoEscolaCompartilhada2())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 1 com prédio compartilhado é o mesmo código da escola 2");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada1() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada1().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada1().equals(educacenso.getNrCodigoEscolaCompartilhada3())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 1 com prédio compartilhado é o mesmo código da escola 3");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada1() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada1().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada1().equals(educacenso.getNrCodigoEscolaCompartilhada4())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 1 com prédio compartilhado é o mesmo código da escola 4");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada1() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada1().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada1().equals(educacenso.getNrCodigoEscolaCompartilhada5())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 1 com prédio compartilhado é o mesmo código da escola 5");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada1() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada1().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada1().equals(educacenso.getNrCodigoEscolaCompartilhada6())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 1 com prédio compartilhado é o mesmo código da escola 6");
			}
			
			//Comparacao do codigo 2
			if(educacenso.getNrCodigoEscolaCompartilhada2() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada2().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada2().equals(educacenso.getNrCodigoEscolaCompartilhada3())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 2 com prédio compartilhado é o mesmo código da escola 3");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada2() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada2().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada2().equals(educacenso.getNrCodigoEscolaCompartilhada4())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 2 com prédio compartilhado é o mesmo código da escola 4");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada2() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada2().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada2().equals(educacenso.getNrCodigoEscolaCompartilhada5())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 2 com prédio compartilhado é o mesmo código da escola 5");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada2() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada2().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada2().equals(educacenso.getNrCodigoEscolaCompartilhada6())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 2 com prédio compartilhado é o mesmo código da escola 6");
			}
			
			//Comparacao do codigo 3
			if(educacenso.getNrCodigoEscolaCompartilhada3() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada3().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada3().equals(educacenso.getNrCodigoEscolaCompartilhada4())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 3 com prédio compartilhado é o mesmo código da escola 4");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada3() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada3().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada3().equals(educacenso.getNrCodigoEscolaCompartilhada5())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 3 com prédio compartilhado é o mesmo código da escola 5");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada3() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada3().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada3().equals(educacenso.getNrCodigoEscolaCompartilhada6())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 3 com prédio compartilhado é o mesmo código da escola 6");
			}
			
			//Comparacao do codigo 4
			if(educacenso.getNrCodigoEscolaCompartilhada4() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada4().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada4().equals(educacenso.getNrCodigoEscolaCompartilhada5())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 4 com prédio compartilhado é o mesmo código da escola 5");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada4() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada4().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada4().equals(educacenso.getNrCodigoEscolaCompartilhada6())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 4 com prédio compartilhado é o mesmo código da escola 6");
			}
			
			//Comparacao do codigo 5
			if(educacenso.getNrCodigoEscolaCompartilhada5() != null 
				&& !educacenso.getNrCodigoEscolaCompartilhada5().equals("") 
				&& educacenso.getNrCodigoEscolaCompartilhada5().equals(educacenso.getNrCodigoEscolaCompartilhada6())){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO).add(ValidatorResult.ERROR, "O código da escola 5 com prédio compartilhado é o mesmo código da escola 6");
			}
			
			
			if(educacenso.getNrCodigoEscolaCompartilhada1() != null 
					&& !educacenso.getNrCodigoEscolaCompartilhada1().equals("") 
					&& educacenso.getNrCodigoEscolaCompartilhada1().length() != 8){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_INVALIDO).add(ValidatorResult.ERROR, "O código da escola 1 com prédio compartilhado é inválido");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada2() != null 
					&& !educacenso.getNrCodigoEscolaCompartilhada2().equals("") 
					&& educacenso.getNrCodigoEscolaCompartilhada2().length() != 8){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_INVALIDO).add(ValidatorResult.ERROR, "O código da escola 2 com prédio compartilhado é inválido");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada3() != null 
					&& !educacenso.getNrCodigoEscolaCompartilhada3().equals("") 
					&& educacenso.getNrCodigoEscolaCompartilhada3().length() != 8){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_INVALIDO).add(ValidatorResult.ERROR, "O código da escola 3 com prédio compartilhado é inválido");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada4() != null 
					&& !educacenso.getNrCodigoEscolaCompartilhada4().equals("") 
					&& educacenso.getNrCodigoEscolaCompartilhada4().length() != 8){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_INVALIDO).add(ValidatorResult.ERROR, "O código da escola 4 com prédio compartilhado é inválido");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada5() != null 
					&& !educacenso.getNrCodigoEscolaCompartilhada5().equals("") 
					&& educacenso.getNrCodigoEscolaCompartilhada5().length() != 8){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_INVALIDO).add(ValidatorResult.ERROR, "O código da escola 5 com prédio compartilhado é inválido");
			}
			
			if(educacenso.getNrCodigoEscolaCompartilhada6() != null 
					&& !educacenso.getNrCodigoEscolaCompartilhada6().equals("") 
					&& educacenso.getNrCodigoEscolaCompartilhada6().length() != 8){
				listValidator.get(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_INVALIDO).add(ValidatorResult.ERROR, "O código da escola 6 com prédio compartilhado é inválido");
			}
			Instituicao instituicao = InstituicaoDAO.get(educacenso.getCdInstituicao(), connect);
			PessoaFisica gestor = PessoaFisicaDAO.get(instituicao.getCdAdministrador(), connect);
			DadosFuncionais dadosFuncionais = null;
			ResultSetMap rsmGestor = DadosFuncionaisServices.getByProfessor(gestor.getCdPessoa(), instituicao.getCdInstituicao(), connect);
			if(rsmGestor.next()){
				dadosFuncionais = DadosFuncionaisDAO.get(rsmGestor.getInt("cd_matricula"), connect);
			}
			
			if(dadosFuncionais != null){
				
				if(dadosFuncionais.getCdTipoAdmissao() == 0){
					listValidator.get(VALIDATE_EDUCACENSO_TIPO_ADMISSAO_GESTOR).add(ValidatorResult.ERROR, "Não cadastrado o tipo de admissão do gestor", GRUPO_VALIDACAO_EDUCACENSO);
				}
				
				if(dadosFuncionais.getTpAcessoCargo() != 2 && dadosFuncionais.getTpAcessoCargo() != 5){
					listValidator.get(VALIDATE_EDUCACENSO_CRITERIO_ACESSO_GESTOR).add(ValidatorResult.ERROR, "Os únicos tipos de critério de acesso permitidos são: 'Exclusivamente por indicação/escolha da gestão' e 'Exclusivamente por processo eleitoral com a participação da comunidade escolar'", GRUPO_VALIDACAO_EDUCACENSO);
				}
				
			}
			else{
				listValidator.get(VALIDATE_EDUCACENSO_GESTOR_NAO_LOTADO).add(ValidatorResult.ERROR, "Gestor não lotado", GRUPO_VALIDACAO_EDUCACENSO);
			}
			
			//Faz a verificação das validações a partir do grupo que chamou
			result.addListValidator(listValidator);
			result.verify(idGrupo);
			
			//RETORNO
			return result;
		
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoEducacensoServices.validate: " + e);
			return new ValidatorResult(ValidatorResult.ERROR, "Erro em InstituicaoEducacensoServices.validate");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static HashMap<Integer, Validator> getListValidation(){
		HashMap<Integer, Validator> list = new HashMap<Integer, Validator>();
	
		list.put(VALIDATE_EDUCACENSO_COMPUTADORES, new Validator(VALIDATE_EDUCACENSO_COMPUTADORES, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_SITUACAO, new Validator(VALIDATE_EDUCACENSO_SITUACAO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_SALA_DE_AULA_DEPENDENCIA, new Validator(VALIDATE_EDUCACENSO_SALA_DE_AULA_DEPENDENCIA, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_EQUIPAMENTO_SEM_QUANTIDADE, new Validator(VALIDATE_EDUCACENSO_EQUIPAMENTO_SEM_QUANTIDADE, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_INEP, new Validator(VALIDATE_EDUCACENSO_INEP, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_NOME_ORGAO_REGIONAL, new Validator(VALIDATE_EDUCACENSO_NOME_ORGAO_REGIONAL, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_DEPENDENCIA_ADMINISTRATIVA, new Validator(VALIDATE_EDUCACENSO_DEPENDENCIA_ADMINISTRATIVA, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_LOCALIZACAO, new Validator(VALIDATE_EDUCACENSO_LOCALIZACAO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_FORMA_OCUPACAO, new Validator(VALIDATE_EDUCACENSO_FORMA_OCUPACAO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_PREDIO_COMPARTILHADO, new Validator(VALIDATE_EDUCACENSO_PREDIO_COMPARTILHADO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_REGULAMENTACAO, new Validator(VALIDATE_EDUCACENSO_REGULAMENTACAO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_AGUA_CONSUMIDA, new Validator(VALIDATE_EDUCACENSO_AGUA_CONSUMIDA, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_ABASTECIMENTO_AGUA, new Validator(VALIDATE_EDUCACENSO_ABASTECIMENTO_AGUA, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_ABASTECIMENTO_ENERGIA, new Validator(VALIDATE_EDUCACENSO_ABASTECIMENTO_ENERGIA, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_ESGOTO_SANITARIO, new Validator(VALIDATE_EDUCACENSO_ESGOTO_SANITARIO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_DESTINACAO_LIXO, new Validator(VALIDATE_EDUCACENSO_DESTINACAO_LIXO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_SALAS_AULA, new Validator(VALIDATE_EDUCACENSO_SALAS_AULA, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_QUANTIDADE_FUNCIONARIOS, new Validator(VALIDATE_EDUCACENSO_QUANTIDADE_FUNCIONARIOS, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_INTERNET, new Validator(VALIDATE_EDUCACENSO_INTERNET, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_BANDA_LARGA, new Validator(VALIDATE_EDUCACENSO_BANDA_LARGA, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_INTERNET_BANDA_LARGA, new Validator(VALIDATE_EDUCACENSO_INTERNET_BANDA_LARGA, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_ALIMENTACAO_ESCOLAR, new Validator(VALIDATE_EDUCACENSO_ALIMENTACAO_ESCOLAR, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_LOCALIZACAO_DIFERENCIADA, new Validator(VALIDATE_EDUCACENSO_LOCALIZACAO_DIFERENCIADA, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_MATERIAL_ESPECIFICO, new Validator(VALIDATE_EDUCACENSO_MATERIAL_ESPECIFICO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_BRASIL_ALFABETIZADO, new Validator(VALIDATE_EDUCACENSO_BRASIL_ALFABETIZADO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_ESPACO_COMUNIDADE, new Validator(VALIDATE_EDUCACENSO_ESPACO_COMUNIDADE, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_DEPENDENCIAS, new Validator(VALIDATE_EDUCACENSO_DEPENDENCIAS, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_LOCAL_FUNCIONAMENTO, new Validator(VALIDATE_EDUCACENSO_LOCAL_FUNCIONAMENTO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_CATEGORIA_PRIVADA, new Validator(VALIDATE_EDUCACENSO_CATEGORIA_PRIVADA, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_CONVENIADA_PODER_PUBLICO, new Validator(VALIDATE_EDUCACENSO_CONVENIADA_PODER_PUBLICO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_MANTENEDORA, new Validator(VALIDATE_EDUCACENSO_MANTENEDORA, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_MESMO_INEP_PRINCIPAL, new Validator(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_MESMO_INEP_PRINCIPAL, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO, new Validator(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_REPETIDO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_INVALIDO, new Validator(VALIDATE_EDUCACENSO_ESCOLA_COMPARTILHADA_INEP_INVALIDO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_RECURSO_ACESSIBILIDADE, new Validator(VALIDATE_EDUCACENSO_RECURSO_ACESSIBILIDADE, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_ACESSO_INTERNET, new Validator(VALIDATE_EDUCACENSO_ACESSO_INTERNET, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_EQUIPAMENTO_ACESSO_INTERNET_ALUNO, new Validator(VALIDATE_EDUCACENSO_EQUIPAMENTO_ACESSO_INTERNET_ALUNO, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_RECURSO_INTERLIGACAO_COMPUTADORES, new Validator(VALIDATE_EDUCACENSO_RECURSO_INTERLIGACAO_COMPUTADORES, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_INSTRUMENTOS_PEDAGOGICOS, new Validator(VALIDATE_EDUCACENSO_INSTRUMENTOS_PEDAGOGICOS, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_GRUPOS_ESCOLARES, new Validator(VALIDATE_EDUCACENSO_GRUPOS_ESCOLARES, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_CRITERIO_ACESSO_GESTOR, new Validator(VALIDATE_EDUCACENSO_CRITERIO_ACESSO_GESTOR, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_TIPO_ADMISSAO_GESTOR, new Validator(VALIDATE_EDUCACENSO_TIPO_ADMISSAO_GESTOR, ValidatorResult.VALID));
		list.put(VALIDATE_EDUCACENSO_GESTOR_NAO_LOTADO, new Validator(VALIDATE_EDUCACENSO_GESTOR_NAO_LOTADO, ValidatorResult.VALID));
		
		
		return list;
	}
	
	@Deprecated //usar metodos em InstituicaoServices
	public static ResultSetMap getTipoMantenedoraOf(int cdInstituicao)	{
		Connection connect = Conexao.conectar();
		try	{
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_tipo_mantenedora A " +
					                                         "LEFT OUTER JOIN acd_instituicao_tipo_mantenedora B ON (B.cd_tipo_mantenedora = A.cd_tipo_mantenedora " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = "+cdPeriodoLetivoAtual+") " +
					                                         "ORDER BY cd_instituicao ASC, nm_tipo_mantenedora ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	@Deprecated
	public static Result addTipoMantenedora(int cdInstituicao, int cdTipoMantenedora)	{
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			

			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
						
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_tipo_mantenedora " +
					                              	"WHERE cd_instituicao      = "+cdInstituicao+
					                              	"  AND cd_tipo_mantenedora = "+cdTipoMantenedora+
					                              	"  AND cd_periodo_letivo = "+cdPeriodoLetivoAtual).executeQuery();
			if(rs.next())
				return new Result(-1, "ERRO: Esta atividade complementar já foi informado para essa instituicao!");
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_tipo_mantenedora (cd_instituicao,"+
			                                  "cd_tipo_mantenedora, cd_periodo_letivo) VALUES (?, ?, ?)");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdTipoMantenedora);
			pstmt.setInt(3, cdPeriodoLetivoAtual);
			return new Result(pstmt.executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir tipo de entidade mantenedora na instituicao!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	@Deprecated
	public static Result deleteTipoMantenedora(int cdInstituicao, int cdTipoMantenedora){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			

			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_tipo_mantenedora " +
					                              	   "WHERE cd_instituicao      = "+cdInstituicao+
					                              	   "  AND cd_tipo_mantenedora = "+cdTipoMantenedora+
					                              	   "  AND cd_periodo_letivo = " + cdPeriodoLetivoAtual).executeUpdate(), "Tipo de mantenedora excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir tipo de mantenedora da instituicao!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	@Deprecated
	public static ResultSetMap getLocalFuncionamentoOf(int cdInstituicao)	{
		Connection connect = Conexao.conectar();
		try	{
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_local_funcionamento A " +
					                                         "LEFT OUTER JOIN acd_instituicao_local_funcionamento B ON (B.cd_local_funcionamento = A.cd_local_funcionamento " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = "+cdPeriodoLetivoAtual+") " +
					                                         "ORDER BY cd_instituicao ASC, nm_local_funcionamento ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	@Deprecated
	public static Result addLocalFuncionamento(int cdInstituicao, int cdLocalFuncionamento)	{
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_local_funcionamento " +
					                              	"WHERE cd_instituicao                  = "+cdInstituicao+
					                              	"  AND cd_local_funcionamento = "+cdLocalFuncionamento+
					                              	"  AND cd_periodo_letivo = " + cdPeriodoLetivoAtual).executeQuery();
			if(rs.next())
				return new Result(-1, "ERRO: Esta atendimento especializado já foi informado para essa instituicao!");
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_local_funcionamento (cd_instituicao,"+
			                                  "cd_local_funcionamento, cd_periodo_letivo) VALUES (?, ?, ?)");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdLocalFuncionamento);
			pstmt.setInt(3, cdPeriodoLetivoAtual);
			return new Result(pstmt.executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir local de funcionamento na instituicao!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	@Deprecated
	public static Result deleteLocalFuncionamento(int cdInstituicao, int cdLocalFuncionamento){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_local_funcionamento " +
					                              	   "WHERE cd_instituicao = "+cdInstituicao+
					                              	   "  AND cd_local_funcionamento = "+cdLocalFuncionamento+
					                              	   "  AND cd_periodo_letivo = " + cdPeriodoLetivoAtual).executeUpdate(), "Local de funcionamento excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir o local de funcionamento da instituicao!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	@Deprecated
	public static ResultSetMap getTipoDependenciaOf(int cdInstituicao)	{
		Connection connect = Conexao.conectar();
		try	{
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_tipo_dependencia A " +
					                                         "LEFT OUTER JOIN acd_instituicao_dependencia B ON (B.cd_tipo_dependencia = A.cd_tipo_dependencia " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+
					                                         "													AND B.cd_periodo_letivo = "+cdPeriodoLetivoAtual+") " +
					                                         "ORDER BY cd_instituicao ASC, nm_tipo_dependencia ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	@Deprecated
	public static Result addTipoDependencia(int cdInstituicao, int cdTipoDependencia)	{
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_dependencia " +
					                              	"WHERE cd_instituicao      = "+cdInstituicao+
					                              	"  AND cd_tipo_dependencia = "+cdTipoDependencia+
					                              	"  AND cd_periodo_letivo = " + cdPeriodoLetivoAtual).executeQuery();
			if(rs.next())
				return new Result(-1, "ERRO: Este tipo de dependência já foi informada para essa instituicao!");
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_dependencia (cd_instituicao,"+
			                                  "cd_tipo_dependencia, cd_periodo_letivo) VALUES (?, ?, ?)");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdTipoDependencia);
			pstmt.setInt(3, cdPeriodoLetivoAtual);
			return new Result(pstmt.executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir tipo de dependência na instituicao!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	@Deprecated
	public static Result deleteTipoDependencia(int cdInstituicao, int cdTipoDependencia){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
			if(rsmPeriodoAtual.next()){
				cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
			}
			
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_dependencia " +
					                              	   "WHERE cd_instituicao      = "+cdInstituicao+
					                              	   "  AND cd_tipo_dependencia = "+cdTipoDependencia+
					                              	   "  AND cd_periodo_letivo = " + cdPeriodoLetivoAtual).executeUpdate(), "Tipo de dependência excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir tipo de dependência da instituicao!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	@Deprecated
	public static ResultSetMap getTipoEquipamentoOf(int cdInstituicao)	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_instituicao FROM acd_tipo_equipamento A " +
					                                         "LEFT OUTER JOIN acd_instituicao_tipo_equipament B ON (B.cd_tipo_equipamento = A.cd_tipo_equipamento " +
					                                         "                                                  AND B.cd_instituicao = "+cdInstituicao+") " +
					                                         "ORDER BY cd_instituicao ASC, nm_tipo_equipamento ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	@Deprecated
	public static Result addTipoEquipamento(int cdInstituicao, int cdTipoEquipamento)	{
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_instituicao_tipo_equipament " +
					                              	"WHERE cd_instituicao      = "+cdInstituicao+
					                              	"  AND cd_tipo_equipamento = "+cdTipoEquipamento).executeQuery();
			if(rs.next())
				return new Result(-1, "ERRO: Esta atendimento especializado já foi informado para essa instituicao!");
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_tipo_equipament (cd_instituicao,"+
			                                  "cd_tipo_equipamento) VALUES (?, ?)");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdTipoEquipamento);
			return new Result(pstmt.executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir tipo de equipamento na instituicao!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	@Deprecated
	public static Result deleteTipoEquipamento(int cdInstituicao, int cdTipoEquipamento){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return new Result(connect.prepareStatement("DELETE FROM acd_instituicao_tipo_equipament " +
					                              	   "WHERE cd_instituicao      = "+cdInstituicao+
					                              	   "  AND cd_tipo_equipamento = "+cdTipoEquipamento).executeUpdate(), "Tipo de equipamento excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir o tipo de equipamento da instituicao!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
