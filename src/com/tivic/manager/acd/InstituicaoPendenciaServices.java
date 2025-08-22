package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.htmlparser.filters.HasAttributeFilter;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ArquivoDAO;
import com.tivic.manager.grl.OcorrenciaArquivoDAO;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.Validator;
import com.tivic.manager.util.ValidatorItem;
import com.tivic.manager.util.ValidatorResult;

public class InstituicaoPendenciaServices {

	
	/* Tipo de Registro */
	public static final int TP_REGISTRO_ESCOLA_CENSO     		 = 0;
	public static final int TP_REGISTRO_TURMA_CENSO     		 = 1;
	public static final int TP_REGISTRO_ALUNO_CENSO     		 = 2;
	public static final int TP_REGISTRO_PROFESSOR_CENSO  		 = 3;
	public static final int TP_REGISTRO_EDUCACENSO_CENSO     	 = 4;
	public static final int TP_REGISTRO_PERIODO_LETIVO_CENSO     = 5;
	public static final int TP_REGISTRO_MATRICULA_CENSO     	 = 6;
	public static final int TP_REGISTRO_DOCUMENTACAO_CENSO     	 = 7;
	
	/* Tipo de Pendencia */
	public static final int TP_PENDENCIA_FALTANDO_NOME_ESCOLA	 	 	 						= 0;
	public static final int TP_PENDENCIA_FALTANDO_SITUACAO_ESCOLA	 	 						= 1;
	public static final int TP_PENDENCIA_FALTANDO_ENDERECO_ESCOLA	 	 						= 2;
	public static final int TP_PENDENCIA_FALTANDO_CEP_ENDERECO_ESCOLA	 						= 3;
	public static final int TP_PENDENCIA_INVALIDO_CEP_ENDERECO_ESCOLA	 						= 4;
	public static final int TP_PENDENCIA_FALTANDO_LOGRADOURO_ENDERECO_ESCOLA					= 5;
	public static final int TP_PENDENCIA_FALTANDO_NUMERO_ENDERECO_ESCOLA						= 6;
	public static final int TP_PENDENCIA_FALTANDO_BAIRRO_ENDERECO_ESCOLA						= 7;
	public static final int TP_PENDENCIA_FALTANDO_CIDADE_ENDERECO_ESCOLA						= 8;
	public static final int TP_PENDENCIA_FALTANDO_GESTOR_ESCOLA									= 9;
	public static final int TP_PENDENCIA_FALTANDO_NOME_GESTOR_ESCOLA							= 10;
	public static final int TP_PENDENCIA_FALTANDO_CPF_GESTOR_ESCOLA								= 11;
	public static final int TP_PENDENCIA_FALTANDO_EMAIL_GESTOR_ESCOLA							= 12;
	public static final int TP_PENDENCIA_INVALIDO_EMAIL_GESTOR_ESCOLA							= 13;
	public static final int TP_PENDENCIA_INVALIDO_EMAIL_ESCOLA									= 14;
	public static final int TP_PENDENCIA_FALTANDO_TELEFONE_ESCOLA								= 15;
	public static final int TP_PENDENCIA_INVALIDO_TELEFONE_1_ESCOLA								= 16;
	public static final int TP_PENDENCIA_INVALIDO_TELEFONE_2_ESCOLA								= 17;
	public static final int TP_PENDENCIA_INVALIDO_CELULAR_ESCOLA								= 18;
	public static final int TP_PENDENCIA_INVALIDO_FAX_ESCOLA									= 19;
	public static final int TP_PENDENCIA_INVALIDO_QUANT_SALAS_AULA_ESCOLA						= 20;
	public static final int TP_PENDENCIA_INVALIDO_QUANT_EQUIPAMENTOS_ESCOLA						= 21;
	public static final int TP_PENDENCIA_INVALIDO_QUANT_EQUIPAMENTOS_COMPUTADORES_ESCOLA		= 22;
	public static final int TP_PENDENCIA_FALTANDO_INEP_ESCOLA									= 23;
	public static final int TP_PENDENCIA_FALTANDO_NOME_ORGAO_ESCOLA								= 24;
	public static final int TP_PENDENCIA_FALTANDO_DEPENDENCIA_ADMINISTRATIVA_ESCOLA				= 25;
	public static final int TP_PENDENCIA_FALTANDO_LOCALIZACAO_ESCOLA							= 26;
	public static final int TP_PENDENCIA_FALTANDO_FORMA_OCUPACAO_ESCOLA							= 27;
	public static final int TP_PENDENCIA_FALTANDO_PREDIO_COMPARTILHADO_ESCOLA					= 28;
	public static final int TP_PENDENCIA_FALTANDO_REGULAMENTACAO_ESCOLA							= 29;
	public static final int TP_PENDENCIA_FALTANDO_LOCAL_FUNCIONAMENTO_ESCOLA					= 30;
	public static final int TP_PENDENCIA_FALTANDO_AGUA_CONSUMIDA_ESCOLA							= 31;
	public static final int TP_PENDENCIA_FALTANDO_ABASTECIMENTO_AGUA_ESCOLA						= 32;
	public static final int TP_PENDENCIA_FALTANDO_ABASTECIMENTO_ENERGIA_ESCOLA					= 33;
	public static final int TP_PENDENCIA_FALTANDO_ESGOTO_SANITARIO_ESCOLA						= 34;
	public static final int TP_PENDENCIA_FALTANDO_DESTINACAO_LIXO_ESCOLA						= 35;
	public static final int TP_PENDENCIA_FALTANDO_QUANTIDADE_SALAS_ESCOLA						= 36;
	public static final int TP_PENDENCIA_FALTANDO_TOTAL_FUNCIONARIOS_ESCOLA						= 37;
	public static final int TP_PENDENCIA_FALTANDO_CAMPO_INTERNET_ESCOLA							= 38;
	public static final int TP_PENDENCIA_FALTANDO_CAMPO_BANDA_LARGA_ESCOLA						= 39;
	public static final int TP_PENDENCIA_FALTANDO_ALIMENTACAO_ESCOLAR_ESCOLA					= 40;
	public static final int TP_PENDENCIA_FALTANDO_LOCALIZACAO_DIFERENCIADA_ESCOLA				= 41;
	public static final int TP_PENDENCIA_FALTANDO_MATERIAIS_ESPECIFICOS_ESCOLA					= 42;
	public static final int TP_PENDENCIA_FALTANDO_BRASIL_ALFABETIZADO_ESCOLA					= 43;
	public static final int TP_PENDENCIA_FALTANDO_ABRE_FINAIS_DE_SEMANA_ESCOLA					= 44;
	public static final int TP_PENDENCIA_FALTANDO_DEPENDENCIAS_ESCOLA							= 45;
	public static final int TP_PENDENCIA_FALTANDO_DEPENDENCIA_SALA_AULA_ESCOLA					= 46;
	public static final int TP_PENDENCIA_FALTANDO_PERIODO_LETIVO_ESCOLA							= 47;
	public static final int TP_PENDENCIA_FALTANDO_HORARIOS_ESCOLA								= 48;
	
	public static final int TP_PENDENCIA_FALTANDO_NOME_TURMA	 	 	 						= 49;
	public static final int TP_PENDENCIA_INVALIDO_NOME_TURMA	 	 	 						= 50;
	public static final int TP_PENDENCIA_FALTANDO_PERIODO_LETIVO_TURMA 	 						= 51;
	public static final int TP_PENDENCIA_FALTANDO_TURNO_TURMA 	 								= 53;
	public static final int TP_PENDENCIA_FALTANDO_CURSO_TURMA 	 								= 54;
	public static final int TP_PENDENCIA_FALTANDO_QUANTIDADE_VAGAS_TURMA 	 					= 55;
	public static final int TP_PENDENCIA_FALTANDO_ALUNO_CADASTRADO_TURMA 	 					= 56;
	public static final int TP_PENDENCIA_FALTANDO_TIPO_ATENDIMENTO_TURMA 	 					= 57;
	public static final int TP_PENDENCIA_FALTANDO_MODALIDADE_ENSINO_TURMA 	 					= 58;
	public static final int TP_PENDENCIA_FALTANDO_ATIVIDADES_COMPLEMENTARES_TURMA				= 59;
	public static final int TP_PENDENCIA_INVALIDO_ATIVIDADES_COMPLEMENTARES_TURMA				= 60;
	public static final int TP_PENDENCIA_FALTANDO_ATENDIMENTO_ESPECIALIZADO_TURMA				= 61;
	public static final int TP_PENDENCIA_INVALIDO_ATENDIMENTO_ESPECIALIZADO_TURMA				= 62;
	public static final int TP_PENDENCIA_FALTANDO_DISCIPLINAS_TURMA								= 63;
	public static final int TP_PENDENCIA_FALTANDO_HORARIOS_TURMA								= 64;
	public static final int TP_PENDENCIA_FALTANDO_PROFESSOR_MONITOR_TURMA						= 65;
	public static final int TP_PENDENCIA_INVALIDO_DISCIPLINAS_INFANTIL_TURMA					= 66;
	public static final int TP_PENDENCIA_INVALIDO_FUND_1_DISCIPLINAS_FORA_BASE_COMUM_TURMA		= 67;
	public static final int TP_PENDENCIA_FALTANDO_FUND_1_DISCIPLINAS_BASE_COMUM_TURMA			= 68;
	public static final int TP_PENDENCIA_INVALIDO_FUND_2_DISCIPLINAS_FORA_BASE_COMUM_TURMA		= 67;
	public static final int TP_PENDENCIA_FALTANDO_FUND_2_DISCIPLINAS_BASE_COMUM_TURMA			= 68;
	
	public static final int TP_PENDENCIA_FALTANDO_NOME_ALUNO	 	 	 						= 69;
	public static final int TP_PENDENCIA_INVALIDO_NOME_PAI_ALUNO 	 							= 70;
	public static final int TP_PENDENCIA_INVALIDO_NOME_ALUNO	 	 	 						= 71;
	public static final int TP_PENDENCIA_INVALIDO_NOME_MAE_ALUNO	 	 	 					= 72;
	public static final int TP_PENDENCIA_FALTANDO_SEXO_ALUNO	 	 	 						= 73;
	public static final int TP_PENDENCIA_FALTANDO_DATA_NASCIMENTO_ALUNO	 						= 74;
	public static final int TP_PENDENCIA_INVALIDO_DATA_NASCIMENTO_ALUNO	 						= 75;
	public static final int TP_PENDENCIA_INVALIDO_CADASTRO_DUPLICADO_ALUNO 						= 76;
	public static final int TP_PENDENCIA_FALTANDO_NACIONALIDADE_ALUNO 							= 77;
	public static final int TP_PENDENCIA_FALTANDO_NATURALIDADE_ALUNO 							= 78;
	public static final int TP_PENDENCIA_FALTANDO_RACA_ALUNO 									= 79;
	public static final int TP_PENDENCIA_FALTANDO_NOME_MAE_ALUNO	 	 	 					= 80;
	public static final int TP_PENDENCIA_FALTANDO_NOME_PAI_ALUNO	 	 	 					= 81;
	public static final int TP_PENDENCIA_FALTANDO_ENDERECO_ALUNO	 	 	 					= 82;
	public static final int TP_PENDENCIA_FALTANDO_CEP_ENDERECO_ALUNO	 	 	 				= 83;
	public static final int TP_PENDENCIA_INVALIDO_CEP_ENDERECO_ALUNO	 	 	 				= 84;
	public static final int TP_PENDENCIA_FALTANDO_LOGRADOURO_ENDERECO_ALUNO	 	 				= 85;
	public static final int TP_PENDENCIA_FALTANDO_NUMERO_ENDERECO_ALUNO	 	 					= 86;
	public static final int TP_PENDENCIA_FALTANDO_BAIRRO_ENDERECO_ALUNO	 	 					= 87;
	public static final int TP_PENDENCIA_FALTANDO_CIDADE_ENDERECO_ALUNO	 	 					= 88;
	public static final int TP_PENDENCIA_FALTANDO_ZONA_ENDERECO_ALUNO	 	 					= 89;
	public static final int TP_PENDENCIA_INVALIDO_PAIS_ORIGEM_ALUNO	 	 						= 90;
	public static final int TP_PENDENCIA_FALTANDO_MODELO_CERTIDAO_ALUNO	 						= 91;
	public static final int TP_PENDENCIA_FALTANDO_TIPO_REGISTRO_CERTIDAO_ALUNO	 				= 92;
	
	/* Situação da Pendencia */
	public static final int ST_PENDENTE	 = 0;
	public static final int ST_RESOLVIDO = 1;
	
	public static Result save(InstituicaoPendencia instituicaoPendencia){
		return save(instituicaoPendencia, null, null);
	}

	public static Result save(InstituicaoPendencia instituicaoPendencia, AuthData authData){
		return save(instituicaoPendencia, authData, null);
	}

	public static Result save(InstituicaoPendencia instituicaoPendencia, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(instituicaoPendencia==null)
				return new Result(-1, "Erro ao salvar. InstituicaoPendencia é nulo");

			int retorno;
			if(instituicaoPendencia.getCdInstituicaoPendencia()==0){
				retorno = InstituicaoPendenciaDAO.insert(instituicaoPendencia, connect);
				instituicaoPendencia.setCdInstituicaoPendencia(retorno);
			}
			else {
				retorno = InstituicaoPendenciaDAO.update(instituicaoPendencia, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOPENDENCIA", instituicaoPendencia);
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
	public static Result remove(int cdInstituicaoPendencia, int cdInstituicao){
		return remove(cdInstituicaoPendencia, cdInstituicao, false, null, null);
	}
	public static Result remove(int cdInstituicaoPendencia, int cdInstituicao, boolean cascade){
		return remove(cdInstituicaoPendencia, cdInstituicao, cascade, null, null);
	}
	public static Result remove(int cdInstituicaoPendencia, int cdInstituicao, boolean cascade, AuthData authData){
		return remove(cdInstituicaoPendencia, cdInstituicao, cascade, authData, null);
	}
	public static Result remove(int cdInstituicaoPendencia, int cdInstituicao, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = InstituicaoPendenciaDAO.delete(cdInstituicaoPendencia, cdInstituicao, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
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

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_pendencia");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllByAluno(int cdAluno) {
		return getAllByAluno(cdAluno, null);
	}

	public static ResultSetMap getAllByAluno(int cdAluno, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_pendencia WHERE cd_aluno = " + cdAluno);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaServices.getAllByAluno: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaServices.getAllByAluno: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllValidacoes(int cdInstituicao) {
		return getAllValidacoes(cdInstituicao, null);
	}
	
	public static ResultSetMap getAllValidacoes(int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(
					   "SELECT A.*, B.nm_pessoa AS nm_instituicao " +
				       "FROM acd_instituicao_pendencia A " +
				       "JOIN grl_pessoa                B ON (B.cd_pessoa = A.cd_instituicao) " +
				       "WHERE 1=1 " + (cdInstituicao != ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0) ? " AND A.cd_instituicao = " + cdInstituicao : "") + 
				       " AND A.st_instituicao_pendencia = " + ST_PENDENTE).executeQuery());
			 
			
			while(rsm.next()){
				
				rsm.setValueToField("DT_GERACAO", Util.convCalendarStringCompleto(rsm.getGregorianCalendar("dt_atualizacao")));
				
				switch(rsm.getInt("TP_REGISTRO")){
					case TP_REGISTRO_ESCOLA_CENSO:
					case TP_REGISTRO_EDUCACENSO_CENSO:
					case TP_REGISTRO_PERIODO_LETIVO_CENSO:
						rsm.setValueToField("NM_TIPO", "ESCOLA");
						rsm.setValueToField("CD_TIPO", "1");
						rsm.setValueToField("NM_REGISTRO", PessoaDAO.get(rsm.getInt("cd_instituicao"), connect).getNmPessoa());
						break;
						
					case TP_REGISTRO_TURMA_CENSO:
						rsm.setValueToField("NM_TIPO", "TURMA");
						rsm.setValueToField("CD_TIPO", "2");
						Turma turma = TurmaDAO.get(rsm.getInt("cd_turma"), connect);
						Curso curso = CursoDAO.get(turma.getCdCurso(), connect);
						rsm.setValueToField("NM_REGISTRO", curso.getNmProdutoServico() + " - " + turma.getNmTurma() + "("+TurmaServices.tiposTurno[turma.getTpTurno()]+")");
						break;
					
					case TP_REGISTRO_ALUNO_CENSO:
					case TP_REGISTRO_MATRICULA_CENSO:
						rsm.setValueToField("NM_TIPO", "ALUNO");
						rsm.setValueToField("CD_TIPO", "3");
						rsm.setValueToField("NM_REGISTRO", PessoaDAO.get(rsm.getInt("cd_aluno"), connect).getNmPessoa());
						break;

					case TP_REGISTRO_PROFESSOR_CENSO:
						rsm.setValueToField("NM_TIPO", "PROFESSOR");
						rsm.setValueToField("CD_TIPO", "4");
						rsm.setValueToField("NM_REGISTRO", PessoaDAO.get(rsm.getInt("cd_professor"), connect).getNmPessoa());
						break;
						
					case TP_REGISTRO_DOCUMENTACAO_CENSO:
						if(rsm.getInt("cd_aluno") > 0){
							rsm.setValueToField("NM_TIPO", "ALUNO");
							rsm.setValueToField("CD_TIPO", "3");
							rsm.setValueToField("NM_REGISTRO", PessoaDAO.get(rsm.getInt("cd_aluno"), connect).getNmPessoa());
						}
						else if(rsm.getInt("cd_professor") > 0){
							rsm.setValueToField("NM_TIPO", "PROFESSOR");
							rsm.setValueToField("CD_TIPO", "4");
							rsm.setValueToField("NM_REGISTRO", PessoaDAO.get(rsm.getInt("cd_professor"), connect).getNmPessoa());
						}
						break;
				
				}
				
			}
			
			rsm.beforeFirst();
			
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaServices.getAllByAluno: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoPendenciaServices.getAllByAluno: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM acd_instituicao_pendencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap gerarRelatorioPendencias(int cdInstituicao, int cdUsuario) {
		return gerarRelatorioPendencias(cdInstituicao, cdUsuario, null);
	}

	public static ResultSetMap gerarRelatorioPendencias(int cdInstituicao, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(
					   "SELECT A.*, B.nm_pessoa AS nm_instituicao " +
				       "FROM acd_instituicao_pendencia A " +
				       "JOIN grl_pessoa                B ON (B.cd_pessoa = A.cd_instituicao) " +
				       "WHERE 1=1 " + (cdInstituicao != ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0) ? " AND A.cd_instituicao = " + cdInstituicao : "") + 
				       " AND A.st_instituicao_pendencia = " + ST_PENDENTE).executeQuery());
			 
			
			while(rsm.next()){
				
				rsm.setValueToField("DT_GERACAO", Util.convCalendarStringCompleto(rsm.getGregorianCalendar("dt_atualizacao")));
				
				switch(rsm.getInt("TP_REGISTRO")){
					case TP_REGISTRO_ESCOLA_CENSO:
					case TP_REGISTRO_EDUCACENSO_CENSO:
					case TP_REGISTRO_PERIODO_LETIVO_CENSO:
						rsm.setValueToField("NM_TIPO", "ESCOLA");
						rsm.setValueToField("CD_TIPO", "1");
						rsm.setValueToField("NM_REGISTRO", PessoaDAO.get(rsm.getInt("cd_instituicao"), connect).getNmPessoa());
						break;
						
					case TP_REGISTRO_TURMA_CENSO:
						rsm.setValueToField("NM_TIPO", "TURMA");
						rsm.setValueToField("CD_TIPO", "2");
						Turma turma = TurmaDAO.get(rsm.getInt("cd_turma"), connect);
						Curso curso = CursoDAO.get(turma.getCdCurso(), connect);
						rsm.setValueToField("NM_REGISTRO", curso.getNmProdutoServico() + " - " + turma.getNmTurma() + "("+TurmaServices.tiposTurno[turma.getTpTurno()]+")");
						break;
					
					case TP_REGISTRO_ALUNO_CENSO:
					case TP_REGISTRO_MATRICULA_CENSO:
						rsm.setValueToField("NM_TIPO", "ALUNO");
						rsm.setValueToField("CD_TIPO", "3");
						rsm.setValueToField("NM_REGISTRO", PessoaDAO.get(rsm.getInt("cd_aluno"), connect).getNmPessoa());
						break;

					case TP_REGISTRO_PROFESSOR_CENSO:
						rsm.setValueToField("NM_TIPO", "PROFESSOR");
						rsm.setValueToField("CD_TIPO", "4");
						rsm.setValueToField("NM_REGISTRO", PessoaDAO.get(rsm.getInt("cd_professor"), connect).getNmPessoa());
						break;
						
					case TP_REGISTRO_DOCUMENTACAO_CENSO:
						if(rsm.getInt("cd_aluno") > 0){
							rsm.setValueToField("NM_TIPO", "ALUNO");
							rsm.setValueToField("CD_TIPO", "3");
							rsm.setValueToField("NM_REGISTRO", PessoaDAO.get(rsm.getInt("cd_aluno"), connect).getNmPessoa());
						}
						else if(rsm.getInt("cd_professor") > 0){
							rsm.setValueToField("NM_TIPO", "PROFESSOR");
							rsm.setValueToField("CD_TIPO", "4");
							rsm.setValueToField("NM_REGISTRO", PessoaDAO.get(rsm.getInt("cd_professor"), connect).getNmPessoa());
						}
						break;
				
				}
				
			}
			
			rsm.beforeFirst();
			
			
			int cdTipoOcorrenciaInstituicao = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_DECLARAR_PRONTA_MATRICULA_INICIAL, connect).getCdTipoOcorrencia();
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connect);
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
			InstituicaoPeriodo periodoAtual = null;
			if(rsmPeriodoAtual.next()){
				periodoAtual = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
			}
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_instituicao", "" + instituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_periodo_letivo", "" + periodoAtual.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + cdTipoOcorrenciaInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmOcorrencia = OcorrenciaInstituicaoServices.find(criterios, connect);
			
			if(rsm.size() == 0){
				GregorianCalendar dtGeracao = null;
				if(rsmOcorrencia.next()){
					dtGeracao = rsmOcorrencia.getGregorianCalendar("dt_ocorrencia");
				}
				else{
					dtGeracao = Util.getDataAtual();
					OcorrenciaInstituicao ocorrencia = new OcorrenciaInstituicao(0, cdInstituicao, "Validação do Educacenso completa para a instituicao " + instituicao.getNmPessoa() + " na etapa de Matrícula Inicial no periodo de " + periodoAtual.getNmPeriodoLetivo(), 
																				Util.getDataAtual(), cdTipoOcorrenciaInstituicao, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, instituicao.getCdInstituicao(), 
																				Util.getDataAtual(), cdUsuario, periodoAtual.getCdPeriodoLetivo());
					Result ret = OcorrenciaInstituicaoServices.save(ocorrencia, connect);
					if(ret.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return null;
					}
				}
				
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				register.put("TXT_INSTITUICAO_PENDENCIA", "Validação Completa");
				register.put("NM_INSTITUICAO", instituicao.getNmPessoa());
				register.put("CD_INSTITUICAO_PENDENCIA", 0);
				register.put("CD_TIPO", "99");
				register.put("DT_GERACAO", Util.convCalendarStringCompleto(dtGeracao));
				rsm.addRegister(register);
			}
			else{
				if(rsmOcorrencia.next()){
					Result ret = OcorrenciaInstituicaoServices.remove(rsmOcorrencia.getInt("cd_ocorrencia"), false, connect);
					if(ret.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return null;
					}
					
					int retorno = OcorrenciaServices.delete(rsmOcorrencia.getInt("cd_ocorrencia"), connect);
					if(retorno < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return null;
					}
				}
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_instituicao", "" + instituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_periodo_letivo", "" + periodoAtual.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_GERACAO_ARQUIVO_MATRICULA_INICIAL, connect).getCdTipoOcorrencia(), ItemComparator.EQUAL, Types.INTEGER));
				rsmOcorrencia = OcorrenciaInstituicaoServices.find(criterios, connect);
				if(rsmOcorrencia.next()){
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_ocorrencia", "" + rsmOcorrencia.getInt("cd_ocorrencia"), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmOcorrenciaArquivo = OcorrenciaArquivoDAO.find(criterios, connect);
					int cdArquivo = 0;
					if(rsmOcorrenciaArquivo.next()){
						cdArquivo = rsmOcorrenciaArquivo.getInt("cd_arquivo");
						int retorno = OcorrenciaArquivoDAO.delete(rsmOcorrenciaArquivo.getInt("cd_ocorrencia"), rsmOcorrenciaArquivo.getInt("cd_arquivo"), connect);
						if(retorno < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return null;
						}
					}
					
					Arquivo arquivo = ArquivoDAO.get(cdArquivo, connect);
					if(arquivo != null){
						int retorno = ArquivoDAO.delete(arquivo.getCdArquivo(), connect);
						if(retorno < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return null;
						}
					}
					
					
					Result ret = OcorrenciaInstituicaoServices.remove(rsmOcorrencia.getInt("cd_ocorrencia"), false, connect);
					if(ret.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return null;
					}
					
					int retorno = OcorrenciaServices.delete(rsmOcorrencia.getInt("cd_ocorrencia"), connect);
					if(retorno < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return null;
					}
				}
				
				
			}
			
			if(isConnectionNull){
				connect.commit();
			}
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_INSTITUICAO");
			fields.add("CD_TIPO");
			fields.add("NM_REGISTRO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			return rsm;
		
		}
		
		catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			if(isConnectionNull){
				Conexao.rollback(connect);
			}
			
			return null;
		}
		
		finally{
			if(isConnectionNull){
				Conexao.desconectar(connect);
			}
		}
	}
	
	public static Result atualizarValidacaoPendencia(ValidatorResult resultadoValidacao, int cdInstituicao, int cdTurma, int cdAluno, int cdProfessor, int tpRegistro, int cdUsuario, int idGrupoValidacao) {
		return atualizarValidacaoPendencia(resultadoValidacao, cdInstituicao, cdTurma, cdAluno, cdProfessor, tpRegistro, cdUsuario, idGrupoValidacao, null);
	}

	public static Result atualizarValidacaoPendencia(ValidatorResult resultadoValidacao, int cdInstituicao, int cdTurma, int cdAluno, int cdProfessor, int tpRegistro, int cdUsuario, int idGrupoValidacao, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			for(Validator validator : resultadoValidacao.getResults()){
				ValidatorItem validatorItem = validator.getByGroup(idGrupoValidacao);
				if(validatorItem != null){
					if(validatorItem.getTpValidator() != ValidatorResult.VALID){
						ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
						if(cdTurma > 0)
							criterios.add(new ItemComparator("cd_turma", "" + cdTurma, ItemComparator.EQUAL, Types.INTEGER));
						if(cdAluno > 0)
							criterios.add(new ItemComparator("cd_aluno", "" + cdAluno, ItemComparator.EQUAL, Types.INTEGER));
						if(cdProfessor > 0)
							criterios.add(new ItemComparator("cd_professor", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("tp_registro", "" + tpRegistro, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("st_instituicao_pendencia", "" + InstituicaoPendenciaServices.ST_PENDENTE, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("tp_pendencia", "" + validator.getIdValidator(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmPendencia = InstituicaoPendenciaDAO.find(criterios, connect);
						if(rsmPendencia.next()){
							InstituicaoPendencia instituicaoPendencia = InstituicaoPendenciaDAO.get(rsmPendencia.getInt("cd_instituicao_pendencia"), cdInstituicao, connect);
							instituicaoPendencia.setDtAtualizacao(Util.getDataAtual());
							instituicaoPendencia.setTxtInstituicaoPendencia(validator.getByGroup(idGrupoValidacao).getTxtReturn());
							if(InstituicaoPendenciaDAO.update(instituicaoPendencia, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao atualizar pendencia");
							}
						}
						else{
							InstituicaoPendencia instituicaoPendencia = new InstituicaoPendencia(0/*cdInstituicaoPendencia*/, cdInstituicao, cdTurma, cdAluno, cdProfessor, tpRegistro, validator.getIdValidator(), Util.getDataAtual(), Util.getDataAtual(), InstituicaoPendenciaServices.ST_PENDENTE, validator.getByGroup(idGrupoValidacao).getTxtReturn(), cdUsuario);
							if(InstituicaoPendenciaDAO.insert(instituicaoPendencia, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao cadastrar pendencia");
							}
						}
					}
					else{
						ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
						if(cdTurma > 0)
							criterios.add(new ItemComparator("cd_turma", "" + cdTurma, ItemComparator.EQUAL, Types.INTEGER));
						if(cdAluno > 0)
							criterios.add(new ItemComparator("cd_aluno", "" + cdAluno, ItemComparator.EQUAL, Types.INTEGER));
						if(cdProfessor > 0)
							criterios.add(new ItemComparator("cd_professor", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("tp_registro", "" + tpRegistro, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("st_instituicao_pendencia", "" + InstituicaoPendenciaServices.ST_PENDENTE, ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("tp_pendencia", "" + validator.getIdValidator(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmPendencia = InstituicaoPendenciaDAO.find(criterios, connect);
						if(rsmPendencia.next()){
							InstituicaoPendencia instituicaoPendencia = InstituicaoPendenciaDAO.get(rsmPendencia.getInt("cd_instituicao_pendencia"), cdInstituicao, connect);
							instituicaoPendencia.setDtAtualizacao(Util.getDataAtual());
							instituicaoPendencia.setStInstituicaoPendencia(InstituicaoPendenciaServices.ST_RESOLVIDO);
							instituicaoPendencia.setTxtInstituicaoPendencia(validator.getByGroup(idGrupoValidacao).getTxtReturn());
							if(InstituicaoPendenciaDAO.update(instituicaoPendencia, connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro ao atualizar pendencia");
							}
						}
					}
				}
				else{
					validatorItem = validator.getByGroup(ValidatorResult.STANDARD);
					if(validatorItem != null){
						if(validatorItem.getTpValidator() != ValidatorResult.VALID){
							ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
							if(cdTurma > 0)
								criterios.add(new ItemComparator("cd_turma", "" + cdTurma, ItemComparator.EQUAL, Types.INTEGER));
							if(cdAluno > 0)
								criterios.add(new ItemComparator("cd_aluno", "" + cdAluno, ItemComparator.EQUAL, Types.INTEGER));
							if(cdProfessor > 0)
								criterios.add(new ItemComparator("cd_professor", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("tp_registro", "" + tpRegistro, ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("st_instituicao_pendencia", "" + InstituicaoPendenciaServices.ST_PENDENTE, ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("tp_pendencia", "" + validator.getIdValidator(), ItemComparator.EQUAL, Types.INTEGER));
							ResultSetMap rsmPendencia = InstituicaoPendenciaDAO.find(criterios, connect);
							if(rsmPendencia.next()){
								InstituicaoPendencia instituicaoPendencia = InstituicaoPendenciaDAO.get(rsmPendencia.getInt("cd_instituicao_pendencia"), cdInstituicao, connect);
								instituicaoPendencia.setDtAtualizacao(Util.getDataAtual());
								instituicaoPendencia.setTxtInstituicaoPendencia(validator.getByGroup(ValidatorResult.STANDARD).getTxtReturn());
								if(InstituicaoPendenciaDAO.update(instituicaoPendencia, connect) < 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return new Result(-1, "Erro ao atualizar pendencia");
								}
							}
							else{
								InstituicaoPendencia instituicaoPendencia = new InstituicaoPendencia(0/*cdInstituicaoPendencia*/, cdInstituicao, cdTurma, cdAluno, cdProfessor, tpRegistro, validator.getIdValidator(), Util.getDataAtual(), Util.getDataAtual(), InstituicaoPendenciaServices.ST_PENDENTE, validator.getByGroup(ValidatorResult.STANDARD).getTxtReturn(), cdUsuario);
								int ret = InstituicaoPendenciaDAO.insert(instituicaoPendencia, connect);
								if(ret < 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return new Result(-1, "Erro ao cadastrar pendencia");
								}
							}
						}
						else{
							ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
							criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
							if(cdTurma > 0)
								criterios.add(new ItemComparator("cd_turma", "" + cdTurma, ItemComparator.EQUAL, Types.INTEGER));
							if(cdAluno > 0)
								criterios.add(new ItemComparator("cd_aluno", "" + cdAluno, ItemComparator.EQUAL, Types.INTEGER));
							if(cdProfessor > 0)
								criterios.add(new ItemComparator("cd_professor", "" + cdProfessor, ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("tp_registro", "" + tpRegistro, ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("st_instituicao_pendencia", "" + InstituicaoPendenciaServices.ST_PENDENTE, ItemComparator.EQUAL, Types.INTEGER));
							criterios.add(new ItemComparator("tp_pendencia", "" + validator.getIdValidator(), ItemComparator.EQUAL, Types.INTEGER));
							ResultSetMap rsmPendencia = InstituicaoPendenciaDAO.find(criterios, connect);
							if(rsmPendencia.next()){
								InstituicaoPendencia instituicaoPendencia = InstituicaoPendenciaDAO.get(rsmPendencia.getInt("cd_instituicao_pendencia"), cdInstituicao, connect);
								instituicaoPendencia.setDtAtualizacao(Util.getDataAtual());
								instituicaoPendencia.setStInstituicaoPendencia(InstituicaoPendenciaServices.ST_RESOLVIDO);
								instituicaoPendencia.setTxtInstituicaoPendencia(validator.getByGroup(ValidatorResult.STANDARD).getTxtReturn());
								if(InstituicaoPendenciaDAO.update(instituicaoPendencia, connect) < 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return new Result(-1, "Erro ao atualizar pendencia");
								}
							}
						}
					}
				}
			}
			if(isConnectionNull)
				connect.commit();
			
			
			return new Result(1, "Validações inseridas com sucesso");
		}
		
		catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			if(isConnectionNull){
				Conexao.rollback(connect);
			}
			
			return new Result(-1);
		}
		
		finally{
			if(isConnectionNull){
				Conexao.desconectar(connect);
			}
		}
	}	

}