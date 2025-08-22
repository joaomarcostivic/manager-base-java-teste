package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.TipoTransporteServices;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.Setor;
import com.tivic.manager.grl.SetorDAO;
import com.tivic.manager.grl.TipoOcorrenciaServices;
import com.tivic.manager.srh.DadosFuncionais;
import com.tivic.manager.srh.DadosFuncionaisDAO;
import com.tivic.manager.srh.DadosFuncionaisServices;
import com.tivic.manager.srh.Funcao;
import com.tivic.manager.srh.FuncaoDAO;
import com.tivic.manager.srh.FuncaoServices;
import com.tivic.manager.srh.Lotacao;
import com.tivic.manager.srh.LotacaoDAO;
import com.tivic.manager.util.Util;
import com.tivic.manager.util.Validator;
import com.tivic.manager.util.ValidatorResult;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TurmaServices {
	/* Tipos de turnos */
	public static final int TP_MATUTINO   = 0;
	public static final int TP_VESPERTINO = 1;
	public static final int TP_NOTURNO    = 2;
	public static final int TP_DIURNO     = 3;
	public static final int TP_INTEGRAL   = 4;
	public static final int TP_FIM_SEMANA = 5;

	public static final String[] tiposTurno = {"Matutino",
		"Vespertino",
		"Noturno",
		"Diurno",
		"Integral",
		"Fim de Semana"};
	
	/*Tipos de Atendimento*/
	public static final int TP_ATENDIMENTO_NAO_APLICA             = 0;
	public static final int TP_ATENDIMENTO_HOSPITALAR  			  = 1;
	public static final int TP_ATENDIMENTO_INTERNACAO 			  = 2;
	public static final int TP_ATENDIMENTO_PRISIONAL	   		  = 3;
	public static final int TP_ATENDIMENTO_ATIVIDADE_COMPLEMENTAR = 4;
	public static final int TP_ATENDIMENTO_AEE	   				  = 5;
	
	
	public static final String[] tipoAtendimento = {"Não se aplica", "Classe hospitalar","Unidade de Internação","Unidade prisional",
        "Atividade complementar","Atend. Educ. Especializado"};

	/*Tipos de Local Diferenciado*/
	public static final int TP_LOCAL_DIFERENCIADO_NAO_APLICA             = 0;
	public static final int TP_LOCAL_DIFERENCIADO_SALA_ANEXA  			  = 1;
	public static final int TP_LOCAL_DIFERENCIADO_INTERNACAO 			  = 2;
	public static final int TP_LOCAL_DIFERENCIADO_PRISIONAL	   		  = 3;
	public static final int TP_LOCAL_DIFERENCIADO_ATIVIDADE_COMPLEMENTAR = 4;
	public static final int TP_LOCAL_DIFERENCIADO_AEE	   				  = 5;
	
	
	public static final String[] tiposLocalDiferenciado = {"Não se aplica", "Sala anexa","Unidade de atendimento socioeducativo","Unidade prisional"};

	/* Situação das turmas */
	public static final int ST_CONCLUIDO = 0;
	public static final int ST_ATIVO = 1;
	public static final int ST_PENDENTE = 2;
	public static final int ST_INATIVO = 3;

	public static final String[] situacoesTurma = {"Concluída",
		"Ativo",
		"Pendente",
		"Inativo"};

	/*Tipos de Modalidade*/
	public static final int TP_MODALIDADE_REGULAR  = 1;
	public static final int TP_MODALIDADE_ESPECIAL = 2;
	public static final int TP_MODALIDADE_EJA	   = 3;
	
	public static final String[] tipoModalidade = {"Ignorar", "Ensino Regular", "Educação Especial - Modalidade Substitutiva", "Educação de Jovens e Adultos (EJA)"};
	
	/*Atendimento Especializado*/
	public static final String TP_ATENDIMENTO_ESPECIALIZADO_SISTEMA_BRAILE             				= "1";
	public static final String TP_ATENDIMENTO_ESPECIALIZADO_ENSINO_OPTICO_NAO_OPTICO  			  	= "3";
	public static final String TP_ATENDIMENTO_ESPECIALIZADO_PROCESSOS_MENTAIS 			  			= "4";
	public static final String TP_ATENDIMENTO_ESPECIALIZADO_ORIENTACAO_MOBILIDADE	   		  		= "5";
	public static final String TP_ATENDIMENTO_ESPECIALIZADO_LINGUAGEM_SINAIS 						= "6";
	public static final String TP_ATENDIMENTO_ESPECIALIZADO_COMUNICACAO_ALTERNATIVA	   				= "7";
	public static final String TP_ATENDIMENTO_ESPECIALIZADO_ATIVIDADES_ENRIQUECIMENTO_CURRICULAR 	= "8";
	public static final String TP_ATENDIMENTO_ESPECIALIZADO_SOROBAN  			  					= "9";
	public static final String TP_ATENDIMENTO_ESPECIALIZADO_INFORMATICA_ACESSIVEL 			  		= "10";
	public static final String TP_ATENDIMENTO_ESPECIALIZADO_LINGUA_PORTUGUESA_ESCRITA	   		  	= "11";
	public static final String TP_ATENDIMENTO_ESPECIALIZADO_ESTRATEGIAS_AUTONOMIA	   				= "12";
	public static final String TP_ATENDIMENTO_ESPECIALIZADO_FUNCOES_COGNITIVAS		   				= "13";
	public static final String TP_ATENDIMENTO_ESPECIALIZADO_LINGUA_PORTUGUESA_SEGUNDA  				= "14";
	
	public static final int ST_TURMA_SITUACAO_ALUNO_CENSO_LIBERADA  = 0;
	public static final int ST_TURMA_SITUACAO_ALUNO_CENSO_FECHADA   = 1;
	
	//Grupos de Validação
	public static final int GRUPO_VALIDACAO_INSERT = 0;
	public static final int GRUPO_VALIDACAO_UPDATE = 1;
	public static final int GRUPO_VALIDACAO_EDUCACENSO = 2;
	//Validações
	public static final int VALIDATE_NOME_FALTANDO_INVALIDO = 0;
	public static final int VALIDATE_NOME_INATIVO = 1;
	public static final int VALIDATE_PERIODO_LETIVO = 2;
	public static final int VALIDATE_TURNO = 3;
	public static final int VALIDATE_CURSO = 4;
	public static final int VALIDATE_VAGAS = 5;
	public static final int VALIDATE_TIPO_ATENDIMENTO = 6;
	public static final int VALIDATE_TIPO_MODALIDADE_ENSINO = 7;
	public static final int VALIDATE_DISCIPLINAS = 8;
	public static final int VALIDATE_HORARIOS = 9;
	public static final int VALIDATE_PROFESSORES = 10;
	public static final int VALIDATE_DISCIPLINAS_INFANTIL = 11;
	public static final int VALIDATE_DISCIPLINAS_FUNDAMENTAL_1_EXCESSO = 12;
	public static final int VALIDATE_DISCIPLINAS_FUNDAMENTAL_1_FALTANTE = 13;
	public static final int VALIDATE_DISCIPLINAS_FUNDAMENTAL_2_FALTANTE = 14;
	public static final int VALIDATE_NOME_DUPLICADO = 15;
	public static final int VALIDATE_ATENDIMENTOS_ESPECIALIZADOS = 16;
	public static final int VALIDATE_TURMA_NAO_AEE = 17;
	public static final int VALIDATE_ATIVIDADE_COMPLEMENTAR = 18;
	public static final int VALIDATE_TURMA_NAO_AC = 19;
	public static final int VALIDATE_QUADRO_VAGAS_QUANTIDADE_TURMAS = 20;
	public static final int VALIDATE_QUADRO_VAGAS_QUANTIDADE_VAGAS = 21;
	public static final int VALIDATE_QUADRO_VAGAS_CURSO_TURNO = 22;
	public static final int VALIDATE_SEM_QUADRO_VAGAS = 23;
	public static final int VALIDATE_ALUNOS = 24;
	public static final int VALIDATE_CURSO_EJA_ATENDIMENTO = 25;
	public static final int VALIDATE_OFERTA_SEM_DOCENTE = 26;
	
	public static Result save( Turma turma, ArrayList<Integer> atividadesComplementares, ArrayList<Integer> atendimentosEspecializados ){
		return save( turma, atividadesComplementares, atendimentosEspecializados, false, null );
	}
	
	public static Result save(Turma turma, ArrayList<Integer> atividadesComplementares, ArrayList<Integer> atendimentosEspecializados, Connection connection){
		return save( turma, atividadesComplementares, atendimentosEspecializados, false, connection );
	}
	
	public static Result save( Turma turma, ArrayList<Integer> atividadesComplementares, ArrayList<Integer> atendimentosEspecializados, boolean verificarQuadroVagas ){
		return save( turma, atividadesComplementares, atendimentosEspecializados, verificarQuadroVagas, null );
	}
	
	public static Result save( Turma turma, ArrayList<Integer> atividadesComplementares, ArrayList<Integer> atendimentosEspecializados, boolean verificarQuadroVagas, int cdUsuario ){
		return save( turma, atividadesComplementares, atendimentosEspecializados, verificarQuadroVagas, cdUsuario, null );
	}
	
	public static Result save(Turma turma, ArrayList<Integer> atividadesComplementares, ArrayList<Integer> atendimentosEspecializados, boolean verificarQuadroVagas, Connection connection){
		return save( turma, atividadesComplementares, atendimentosEspecializados, verificarQuadroVagas, 0, null );
	}
	
	/**
	 * Insere ou atualiza os dados de uma turma
	 * @param turma
	 * @param atividadesComplementares
	 * @param atendimentosEspecializados
	 * @param verificarQuadroVagas
	 * @param cdUsuario
	 * @param connection
	 * @return
	 */
	public static Result save(Turma turma, ArrayList<Integer> atividadesComplementares, ArrayList<Integer> atendimentosEspecializados, boolean verificarQuadroVagas, int cdUsuario, Connection connection){
		
		boolean isConnectionNull = connection==null;
		boolean isInsert = turma!=null && turma.getCdTurma()==0;
		int retorno;
		ValidatorResult resultadoValidacao;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			if(turma==null)
				return new Result(-1, "Erro ao salvar. Turma é nula");
			
			ResultSetMap rsmTipoEtapa = new ResultSetMap(connection.prepareStatement("select lg_eja from acd_turma A, acd_curso_etapa B, acd_tipo_etapa C where A.cd_curso = B.cd_curso AND B.cd_etapa = C.cd_etapa AND A.cd_turma = " + turma.getCdTurma()).executeQuery());
			if(rsmTipoEtapa.next()){
				if(rsmTipoEtapa.getInt("lg_eja") == 1){
					turma.setTpModalidadeEnsino(TP_MODALIDADE_EJA);
				}
				else{
					turma.setTpModalidadeEnsino(TP_MODALIDADE_REGULAR);
				}
			}
			
			//Atualiza a matriz da turma baseado no curso
			ResultSetMap rsmCursoMatriz = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_curso_matriz WHERE cd_curso = " + turma.getCdCurso()).executeQuery());
			if(rsmCursoMatriz.next())
				turma.setCdMatriz(rsmCursoMatriz.getInt("cd_matriz"));
			
			//Atualiza o modulo da turma baseado no curso
			ResultSetMap rsmCursoModulo = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_curso_modulo WHERE cd_curso = " + turma.getCdCurso()).executeQuery());
			if(rsmCursoModulo.next())
				turma.setCdCursoModulo(rsmCursoModulo.getInt("cd_curso_modulo"));
			
			//Atualiza todas as ofertas baseado no turno da turma
			ResultSetMap rsmOfertas = OfertaServices.getAllByTurma(turma.getCdTurma(), connection);
			while(rsmOfertas.next()){
				Oferta oferta = OfertaDAO.get(rsmOfertas.getInt("cd_oferta"), connection);
				oferta.setTpTurno(turma.getTpTurno());
				if(OfertaDAO.update(oferta, connection) < 0){
					if(isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar oferta");
				}
			}
			
			
			//Retira todos os horários que não estiverem condizentes com o turno da turma
			Turma turmaAntiga = TurmaDAO.get(turma.getCdTurma(), connection);
			if(turmaAntiga != null && turmaAntiga.getTpTurno() != turma.getTpTurno()){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_turma", "" + turmaAntiga.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmHorariosTurma = TurmaHorarioDAO.find(criterios, connection);
				while(rsmHorariosTurma.next()){
					InstituicaoHorario instituicaoHorario = InstituicaoHorarioDAO.get(rsmHorariosTurma.getInt("cd_horario"), connection);
					if(turma.getTpTurno() != instituicaoHorario.getTpTurno()){
						if(connection.prepareStatement("DELETE FROM acd_turma_horario WHERE cd_horario = " + instituicaoHorario.getCdHorario() + " AND cd_turma = " + turmaAntiga.getCdTurma()).executeUpdate() < 0){
							if(isConnectionNull)
								Conexao.rollback(connection);
							return new Result(-1, "Erro ao atualizar horários da turma");
						}
					}
				}
			}
			
			//Insere registro novo
			if( turma.getCdTurma()==0 ){
				resultadoValidacao = validate(turma, atividadesComplementares, atendimentosEspecializados, verificarQuadroVagas, isInsert, cdUsuario, GRUPO_VALIDACAO_INSERT, connection);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
				
				retorno = TurmaDAO.insert(turma, connection);
				turma.setCdTurma(retorno);
				
				Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connection);
				InstituicaoPeriodo instituicaoPeriodo = null;
				ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(instituicao.getCdInstituicao(), connection);
				if(rsmPeriodoAtual.next()){
					instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connection);
				}
				Curso curso = CursoDAO.get(turma.getCdCurso(), connection);
				
				int cdTipoOcorrenciaCadastroTurma = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_TURMA, connection).getCdTipoOcorrencia();
				OcorrenciaTurma ocorrencia = new OcorrenciaTurma(0, 0, "Turma " + curso.getNmProdutoServico() + " - " + turma.getNmTurma() + " de " + instituicaoPeriodo.getNmPeriodoLetivo() +" da escola " + instituicao.getNmPessoa() + " cadastrada", Util.getDataAtual(), cdTipoOcorrenciaCadastroTurma, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, turma.getCdTurma(), Util.getDataAtual(), cdUsuario, turma.getStTurma(), turma.getStTurma());
				OcorrenciaTurmaServices.save(ocorrencia, connection);
				
			}
			//Atualiza o registro
			else{
				resultadoValidacao = validate(turma, atividadesComplementares, atendimentosEspecializados, verificarQuadroVagas, isInsert, cdUsuario, GRUPO_VALIDACAO_UPDATE, connection);
				if(!resultadoValidacao.hasPassed()){
					return resultadoValidacao;
				}
				
				retorno = TurmaDAO.update(turma, connection);
				
				int cdTipoOcorrenciaCadastroTurma = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_TURMA, connection).getCdTipoOcorrencia();
				OcorrenciaTurma ocorrencia = null;
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_turma", "" + turma.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + cdTipoOcorrenciaCadastroTurma, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmOcorrenciaTurma = OcorrenciaTurmaServices.find(criterios, connection);
				if(rsmOcorrenciaTurma.next()){
					ocorrencia = OcorrenciaTurmaDAO.get(rsmOcorrenciaTurma.getInt("cd_ocorrencia"), connection);
				}
				
				Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connection);
				InstituicaoPeriodo instituicaoPeriodo = null;
				ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(instituicao.getCdInstituicao(), connection);
				if(rsmPeriodoAtual.next()){
					instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connection);
				}
				Curso curso = CursoDAO.get(turma.getCdCurso(), connection);
				
				if(ocorrencia == null){
					ocorrencia = new OcorrenciaTurma(0, 0, "Turma " + curso.getNmProdutoServico() + " - " + turma.getNmTurma() + " de " + instituicaoPeriodo.getNmPeriodoLetivo() +" da escola " + instituicao.getNmPessoa() + " atualizada", Util.getDataAtual(), cdTipoOcorrenciaCadastroTurma, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, turma.getCdTurma(), Util.getDataAtual(), cdUsuario, turma.getStTurma(), turma.getStTurma());
					OcorrenciaTurmaServices.save(ocorrencia, connection);
				}
				else{
					ocorrencia.setDtUltimaModificacao(Util.getDataAtual());
					ocorrencia.setCdUsuarioModificador(cdUsuario);
					ocorrencia.setStTurmaPosterior(turma.getStTurma());
					OcorrenciaTurmaServices.save(ocorrencia, connection);
				}
				
			}
			
			//Atualiza as atividades complementares/Mais Educação
			clearAtividadesComplementar(turma.getCdTurma(), connection);
			if(atividadesComplementares!=null && atividadesComplementares.size()>0) {
				Result resultado = null;
				for (Integer cd : atividadesComplementares) {
					resultado = addAtividadesComplementar(turma.getCdTurma(), cd, connection);
					retorno = resultado.getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar tipos de atividades complementares!");
				}
			}
			
			//Atualiza os Atendimentos Educacionais especializados
			clearAtendimentoEspecializado(turma.getCdTurma(), connection);
			if(atendimentosEspecializados!=null && atendimentosEspecializados.size()>0) {
				for (Integer cd : atendimentosEspecializados) {
					retorno = addAtendimentoEspecializado(turma.getCdTurma(), cd, connection).getCode();
				}
				
				if(retorno<=0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return new Result(-1, "Erro ao atualizar tipos de atendimentos especializados!");
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			if(resultadoValidacao.getCode() == ValidatorResult.VALID){
				Result result = new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TURMA", turma);
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_turma", "" + turma.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("lgIncluirInativos", "1", ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsm = find(criterios);
				result.addObject("rsm", rsm);
				return result;
			}
			else{
				resultadoValidacao.getObjects().put("TURMA", turma);
				resultadoValidacao.setMessage("Salvo com sucesso...");
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_turma", "" + turma.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("lgIncluirInativos", "1", ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsm = find(criterios);
				resultadoValidacao.addObject("rsm", rsm);
				return resultadoValidacao;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	private static Boolean idTurmaExiste(Turma turma){
		Connection connection = Conexao.conectar();
		try {
			ResultSetMap rsm = Search.find( "SELECT * FROM acd_turma"+
					 " WHERE id_turma like '"+turma.getIdTurma()+
					 "' AND cd_instituicao = "+turma.getCdInstituicao()+
					 ( turma.getCdTurma()>0?" AND cd_turma <> "+turma.getCdTurma():"" )
					 , null, connection);
		
			return ( rsm.next() )?true:false;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return false;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Apaga todas as atividades complementares de determinada turma
	 * @param cdTurma
	 * @param connection
	 * @return
	 */
	private static Result clearAtividadesComplementar(int cdTurma, Connection connection){
		boolean isconnectionionNull = connection==null;
		try {
			if(isconnectionionNull)
				connection = Conexao.conectar();

			return new Result(connection.prepareStatement("DELETE FROM acd_turma_atividade_complementar WHERE cd_turma = "+cdTurma).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir os tipos de atividades complementares!", e);
		}
		finally{
			if (isconnectionionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Apaga todos os atendimentos especializados de determinada turma
	 * @param cdTurma
	 * @param connection
	 * @return
	 */
	private static Result clearAtendimentoEspecializado(int cdTurma, Connection connection){
		boolean isconnectionNull = connection==null;
		try {
			if(isconnectionNull)
				connection = Conexao.conectar();

			return new Result(connection.prepareStatement("DELETE FROM acd_turma_aee WHERE cd_turma = "+cdTurma).executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar excluir os tipos de atendimentos especializados!", e);
		}
		finally{
			if (isconnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Adiciona uma atividade complementar a uma turma
	 * @param cdTurma
	 * @param cdAtividade
	 * @param connection
	 * @return
	 */
	public static Result addAtividadesComplementar(int cdTurma, int cdAtividade, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if(isConnectionNull)
				connection = Conexao.conectar();

			/*
			 * Verifica se o tipo de atividade já foi adicionado à turma
			 */
			ResultSet rs = connection.prepareStatement("SELECT * FROM acd_turma_atividade_complementar " +
					                                "WHERE cd_turma = " + cdTurma + " AND cd_atividade_complementar = " + cdAtividade).executeQuery();
			
			if(rs.next())
				return new Result(-1, "Este tipo de atividade complementar já foi informado para essa turma!");
			else 
				return new Result(connection.prepareStatement("INSERT INTO acd_turma_atividade_complementar (cd_turma, cd_atividade_complementar) " +
                                                           "VALUES (" + cdTurma + " ," + cdAtividade + ")").executeUpdate());
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o tipo de atividade complementar na turma!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Adiciona um atendimento especializado em uma turma
	 * @param cdTurma
	 * @param cdAtendimento
	 * @param connection
	 * @return
	 */
	public static Result addAtendimentoEspecializado(int cdTurma, int cdAtendimento, Connection connection){
		boolean isconnectionionNull = connection==null;
		try {
			if(isconnectionionNull)
				connection = Conexao.conectar();

			/*
			 * Verifica se o tipo de atendimento já foi adicionado à turma
			 */
			ResultSet rs = connection.prepareStatement("SELECT * FROM acd_turma_aee " +
					                                "WHERE cd_turma = " + cdTurma + " AND cd_atendimento_especializado = " + cdAtendimento).executeQuery();
			
			if(rs.next())
				return new Result(-1, "Este tipo de atendimento especializado já foi informado para essa turma!");
			else 
				return new Result(connection.prepareStatement("INSERT INTO acd_turma_aee (cd_turma, cd_atendimento_especializado) " +
                                                           "VALUES (" + cdTurma + " ," + cdAtendimento + ")").executeUpdate());
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1,"Erro ao tentar incluir o tipo de atendimento especializado na turma!", e);
		}
		finally{
			if (isconnectionionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result remove(int cdTurma){
		return remove(cdTurma, false, null);
	}
	
	public static Result remove(int cdTurma, boolean cascade){
		return remove(cdTurma, cascade, null);
	}
	
	public static Result remove(int cdTurma, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 1;
			if(cascade){
				//ATIVIDADES E ATENDIMENTOS
				
				// Excluindo turma atendimento especializado relacionados a turma
				retorno = TurmaAeeServices.removeByTurma(cdTurma, connect).getCode();
				
				// Excluindo turma atividade complementar relacionadas a turma
				retorno = TurmaAtividadeComplementarServices.removeByTurma(cdTurma, connect).getCode();
				
				// Excluindo turma conteudo relacionados a turma
				retorno = TurmaConteudoServices.removeByTurma(cdTurma, connect).getCode();
				
			}
				
			if(!cascade || retorno>0)
				retorno = TurmaDAO.delete(cdTurma, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta Turma está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Turma excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir Turma!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeAllByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo){
		return removeAllByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, null);
	}

	/**
	 * Remove todas as turmas de uma instituição em um periodo letivo
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param connect
	 * @return
	 */
	public static Result removeAllByInstituicaoPeriodo(int cdInstituicao, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_ocorrencia_turma A, acd_turma B WHERE A.cd_turma = B.cd_turma AND B.cd_instituicao = " + cdInstituicao + " AND B.cd_periodo_letivo = " + cdPeriodoLetivo + " AND B.nm_turma NOT LIKE '%TRANSFERENCIA%'").executeQuery());
			while(rsm.next()){
				int retorno = connect.prepareStatement("DELETE FROM acd_ocorrencia_turma WHERE cd_ocorrencia = " + rsm.getInt("cd_ocorrencia")).executeUpdate();
				if(retorno<0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao excluir ocorrencias!");
				}
				
				retorno = connect.prepareStatement("DELETE FROM grl_ocorrencia WHERE cd_ocorrencia = " + rsm.getInt("cd_ocorrencia")).executeUpdate();
				if(retorno<0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-2, "Erro ao excluir ocorrencias!");
				}
			}
			
			int retorno = connect.prepareStatement("DELETE FROM acd_turma WHERE cd_instituicao = " + cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo + " AND nm_turma NOT LIKE '%TRANSFERENCIA%'").executeUpdate();
			
			if(retorno<0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-2, "Alguma das turmas está vinculada a outros registros e não pode ser excluída!");
			}
			
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Turmas excluídas com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir turmas!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result inativar(int cdTurma, int cdUsuario){
		return inativar(cdTurma, cdUsuario, null);
	}
	
	/**
	 * Inativa uma turma
	 * @param cdTurma
	 * @param cdUsuario
	 * @param connect
	 * @return
	 */
	public static Result inativar(int cdTurma, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Turma turma = TurmaDAO.get(cdTurma, connect);
			Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);			
			if(instituicao.getLgOffline() == 1){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Escolas que estao no modo offline nao podem inativar turmas");
			}
			

			ResultSetMap rsmAlunos = TurmaServices.getAlunos(cdTurma, connect);
			if(rsmAlunos.size() > 0) {
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Turma possui alunos e não pode ser inativada");
			}
			
			turma.setStTurma(ST_INATIVO);
			int retorno = TurmaDAO.update(turma, connect);
			
			int cdTipoOcorrenciaCadastroTurma = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_TURMA, connect).getCdTipoOcorrencia();
			OcorrenciaTurma ocorrencia = null;
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_turma", "" + turma.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmOcorrenciaTurma = OcorrenciaTurmaDAO.find(criterios, connect);
			if(rsmOcorrenciaTurma.next()){
				ocorrencia = OcorrenciaTurmaDAO.get(rsmOcorrenciaTurma.getInt("cd_ocorrencia"), connect);
			}
			
			InstituicaoPeriodo instituicaoPeriodo = null;
			ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(instituicao.getCdInstituicao(), connect);
			if(rsmPeriodoAtual.next()){
				instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
			}
			Curso curso = CursoDAO.get(turma.getCdCurso(), connect);
			
			if(ocorrencia == null){
				ocorrencia = new OcorrenciaTurma(0, 0, "Turma " + curso.getNmProdutoServico() + " - " + turma.getNmTurma() + " de " + instituicaoPeriodo.getNmPeriodoLetivo() +" da escola " + instituicao.getNmPessoa() + " cadastrada", Util.getDataAtual(), cdTipoOcorrenciaCadastroTurma, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, turma.getCdTurma(), Util.getDataAtual(), cdUsuario, turma.getStTurma(), turma.getStTurma());
				OcorrenciaTurmaServices.save(ocorrencia, connect);
			}
			else{
				ocorrencia.setDtUltimaModificacao(Util.getDataAtual());
				ocorrencia.setCdUsuarioModificador(cdUsuario);
				ocorrencia.setStTurmaPosterior(turma.getStTurma());
				OcorrenciaTurmaServices.save(ocorrencia, connect);
			}
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Erro ao inativar turma!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Turma inativada com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir Turma!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result removeByMatriz(int cdMatriz) {
		return removeByMatriz(cdMatriz,  null);
	}
	public static Result removeByMatriz(int cdMatriz, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			ResultSetMap rsm = getAllByMatriz(cdMatriz, connect);
			while (rsm.next()) {
				if (rsm.getInt("cd_matriz") == cdMatriz) {
					retorno = remove(rsm.getInt("cd_turma"), true, connect).getCode();
					if(retorno<=0){
						Conexao.rollback(connect);
						return new Result(-2, "Esta Turma está vinculada a outros registros e não pode ser excluída!");
					}
				}
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Turma excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir Turma!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll();
	}
	
	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.* FROM acd_turma");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByMatriz(int cdMatriz) {
		return getAllByMatriz(cdMatriz);
	}
	
	public static ResultSetMap getAllByMatriz(int cdMatriz, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_turma WHERE cd_matriz = " + cdMatriz);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Principal metodo de busca das turmas que são compativeis ao curso passado
	 * Possui regras relaxadas que consideram turmas multi e de mesma ordem
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap findAllPossiveis(ArrayList<ItemComparator> criterios) {
		int cdInstituicao = 0;
		int cdCursoTurma = 0;
		int cdPeriodoLetivo = 0;
		int cdTurma = 0;
		int cdMatricula = 0;
		//Utilizado inclusão de turmas de transferência quando necessário buscar na gerencia de matricula e é preciso conservar o aluno
		int incluirTransferencia = 0;
		//Necessário para buscar o periodo letivo correspondente da instituicao passada
		int cdPeriodoLetivoOrigem = 0;
		int lgEnturmacao = 0;
		for(int i=0; criterios!=null && i<criterios.size(); i++){
			if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO")) {
				cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i--);
			}
			
			else if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_CURSO")) {
				cdCursoTurma = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i--);
			}
			
			else if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_PERIODO_LETIVO")) {
				cdPeriodoLetivo = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i--);
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivoOrigem")) {
				cdPeriodoLetivoOrigem = Integer.parseInt(criterios.get(i).getValue());
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("incluirTransferencia")) {
				incluirTransferencia = Integer.parseInt(criterios.get(i).getValue());
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("lgEnturmacao")) {
				lgEnturmacao = Integer.parseInt(criterios.get(i).getValue());
			}
		}
		if(cdPeriodoLetivoOrigem > 0){
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivoOrigem);
			ArrayList<ItemComparator> criteriosInstituicaoPeriodo = new ArrayList<ItemComparator>();
			criteriosInstituicaoPeriodo.add(new ItemComparator("NM_PERIODO_LETIVO", instituicaoPeriodo.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
			criteriosInstituicaoPeriodo.add(new ItemComparator("CD_INSTITUICAO", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoDAO.find(criteriosInstituicaoPeriodo);
			if(rsmPeriodoLetivo.next()){
				cdPeriodoLetivo = rsmPeriodoLetivo.getInt("cd_periodo_letivo");
			}
		}
		return getAllPossiveis(cdInstituicao, cdCursoTurma, cdPeriodoLetivo, cdTurma, cdMatricula, incluirTransferencia, 1, lgEnturmacao);
	}
	
	/**
	 * Retorna uma lista de turmas, sem incluir a que vem por parâmetro
	 * 
	 * @param cdIstituicao - codigo da instituicao para criterio de busca
	 * @param cdCurso - codigo do curso para criterio de busca
	 * @param cdTurma - turma que será removida do resultado da busca
	 * 
	 */
	public static ResultSetMap getAllPossiveisRecente(int cdInstituicao, int cdCursoTurma) {
		
		int cdPeriodoLetivo = 0;
		ResultSetMap rsmPeriodoRecente = InstituicaoServices.getPeriodoLetivoRecente(cdInstituicao);
		if(rsmPeriodoRecente.next())
			cdPeriodoLetivo = rsmPeriodoRecente.getInt("cd_periodo_letivo");
		
		return getAllPossiveis(cdInstituicao, cdCursoTurma, cdPeriodoLetivo, 0, 0, 0, 1);
	}
	
public static ResultSetMap getAllPossiveisVigente(int cdInstituicao, int cdCursoTurma) {
		
		int cdPeriodoLetivo = 0;
		ResultSetMap rsmPeriodoRecente = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao);
		if(rsmPeriodoRecente.next())
			cdPeriodoLetivo = rsmPeriodoRecente.getInt("cd_periodo_letivo");
		
		return getAllPossiveis(cdInstituicao, cdCursoTurma, cdPeriodoLetivo, 0, 0, 0, 1);
	}
	
	public static ResultSetMap getAllPossiveis(int cdInstituicao, int cdPeriodoLetivo, ArrayList<Integer> matriculas, int lgProgressao) {
		return getAllPossiveis(cdInstituicao, 0, cdPeriodoLetivo, 0, 0, 0, 1, 0, matriculas, lgProgressao);
	}
	
	public static ResultSetMap getAllPossiveis(int cdInstituicao, int cdPeriodoLetivo, int incluirTransferencia, ArrayList<Integer> matriculas, int lgProgressao) {
		return getAllPossiveis(cdInstituicao, 0, cdPeriodoLetivo, 0, 0, incluirTransferencia, 1, 0, matriculas, lgProgressao);
	}
	
	public static ResultSetMap getAllPossiveis(int cdInstituicao, int cdCursoTurma, int cdPeriodoLetivo, int cdTurma, int cdMatricula) {
		return getAllPossiveis(cdInstituicao, cdCursoTurma, cdPeriodoLetivo, cdTurma, cdMatricula, 0);
	}
	
	public static ResultSetMap getAllPossiveisByMatriculas(int cdInstituicao, int cdCursoTurma, int cdPeriodoLetivo, int cdTurma, ArrayList<Integer> matriculas) {
		return getAllPossiveis(cdInstituicao, cdCursoTurma, cdPeriodoLetivo, cdTurma, 0, 0, 1, 0, matriculas, 0);
	}
	
	public static ResultSetMap getAllPossiveis(int cdInstituicao, int cdCursoTurma, int cdPeriodoLetivo, int cdTurma, int cdMatricula, int incluirTransferencia) {
		return getAllPossiveis(cdInstituicao, cdCursoTurma, cdPeriodoLetivo, cdTurma, cdMatricula, incluirTransferencia, 1);
	}
	
	public static ResultSetMap getAllPossiveis(int cdInstituicao, int cdCursoTurma, int cdPeriodoLetivo, int cdTurma, int cdMatricula, int incluirTransferencia, int apenasAtivos) {
		return getAllPossiveis(cdInstituicao, cdCursoTurma, cdPeriodoLetivo, cdTurma, cdMatricula, incluirTransferencia, apenasAtivos, 0);
	}
	
	public static ResultSetMap getAllPossiveis(int cdInstituicao, int cdCursoTurma, int cdPeriodoLetivo, int cdTurma, int cdMatricula, int incluirTransferencia, int apenasAtivos, int lgEnturmacao) {
		return getAllPossiveis(cdInstituicao, cdCursoTurma, cdPeriodoLetivo, cdTurma, cdMatricula, incluirTransferencia, apenasAtivos, lgEnturmacao, null, 0);
	}
	
	/**
	 * Busca todas as turmas que são compatíveis com um curso passado, em um determinado periodo letivo
	 * @param cdInstituicao
	 * @param cdCursoTurma
	 * @param cdPeriodoLetivo
	 * @param cdTurma
	 * @param cdMatricula
	 * @param incluirTransferencia
	 * @param apenasAtivos
	 * @param lgEnturmacao
	 * @param matriculas
	 * @param lgProgressao
	 * @return
	 */
	public static ResultSetMap getAllPossiveis(int cdInstituicao, int cdCursoTurma, int cdPeriodoLetivo, int cdTurma, int cdMatricula, int incluirTransferencia, int apenasAtivos, int lgEnturmacao, ArrayList<Integer> matriculas, int lgProgressao) {
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		Curso curso = CursoDAO.get(cdCursoTurma);
		Matricula matricula = null;
		if(cdMatricula > 0){
			matricula = MatriculaDAO.get(cdMatricula);
		}
		
		if(apenasAtivos==1)
			criterios.add(new ItemComparator("A.ST_TURMA", Integer.toString(ST_ATIVO) + ", " + Integer.toString(ST_CONCLUIDO), ItemComparator.IN, Types.INTEGER));
		criterios.add(new ItemComparator("A.CD_INSTITUICAO", Integer.toString(cdInstituicao), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.CD_PERIODO_LETIVO", Integer.toString(cdPeriodoLetivo), ItemComparator.EQUAL, Types.INTEGER));
		
		//Usado para fazer o relaxamento da busca, considerando o curso passado, cursos relacionados por multi e com mesmo numero de ordem
		if(cdCursoTurma > 0){
			criterios.add(new ItemComparator("sqlAdicional", " AND (A.CD_CURSO = " + cdCursoTurma
					+ "											 OR M.CD_CURSO_MULTI = " + cdCursoTurma
					+ (curso.getLgMulti()==0 && lgEnturmacao==1 ? "" : (curso != null && curso.getNrOrdem() >= 0 ? " OR B.NR_ORDEM = " + curso.getNrOrdem() : ""))
					+ (curso.getLgMulti()==0 && lgEnturmacao==1 ? "" : " OR (B.LG_MULTI = 1 AND EXISTS (SELECT * FROM acd_curso_multi ACM WHERE ACM.cd_curso_multi = B.cd_curso AND ACM.cd_curso = "+cdCursoTurma+")) ")
					+ (curso.getLgMulti()==0 && lgEnturmacao==1 ? "" : " OR (B.LG_MULTI = 0 AND EXISTS (SELECT * FROM acd_curso_correspondencia ACC WHERE (ACC.cd_curso = B.cd_curso OR ACC.cd_curso_correspondencia = B.cd_curso) AND (ACC.cd_curso = "+cdCursoTurma+" OR ACC.cd_curso_correspondencia = "+cdCursoTurma+"))) ")
					+ (curso.getLgMulti()==0 && lgEnturmacao==1 ? "" : " OR (B.LG_MULTI = 1 AND EXISTS (SELECT * FROM acd_curso_multi ACM WHERE ACM.cd_curso_multi = B.cd_curso AND EXISTS (SELECT * FROM acd_curso_correspondencia ACC WHERE (ACC.cd_curso = ACM.cd_curso OR ACC.cd_curso_correspondencia = ACM.cd_curso) AND (ACC.cd_curso = "+cdCursoTurma+" OR ACC.cd_curso_correspondencia = "+cdCursoTurma+")))) ")
					+ ") ", ItemComparator.EQUAL, Types.VARCHAR));
		}
		else if(matriculas != null){
			int cdCursoMatriculas = 0;
			cdCursoTurma = 0;
			for(int codigoMatricula: matriculas){
				Matricula matriculaCodigos = MatriculaDAO.get(codigoMatricula);
				cdCursoTurma = matriculaCodigos.getCdCurso();
				if(cdCursoMatriculas == 0){
					cdCursoMatriculas = matriculaCodigos.getCdCurso();
				}
				
				if(cdCursoMatriculas != matriculaCodigos.getCdCurso()){
					return null;
				}
			}
			
			curso = CursoDAO.get(cdCursoTurma);
			
			Curso cursoMatricula = null;
			
			if(lgProgressao==1)
				cursoMatricula = CursoEtapaServices.getProximoCurso(cdCursoMatriculas, cdInstituicao);
			else
				cursoMatricula = CursoDAO.get(cdCursoMatriculas);
			
			if(cursoMatricula != null){
				InstituicaoCurso instituicaoCurso = InstituicaoCursoDAO.get(cdInstituicao, cursoMatricula.getCdCurso(), cdPeriodoLetivo);
				if(instituicaoCurso != null){
					
					criterios.add(new ItemComparator("sqlAdicional", " AND (A.CD_CURSO = " + cursoMatricula.getCdCurso()
							+ "											 OR M.CD_CURSO_MULTI = " + cursoMatricula.getCdCurso()
							+ (curso.getLgMulti()==0 && lgEnturmacao==1 ? "" : (cursoMatricula != null && cursoMatricula.getNrOrdem() >= 0 ? " OR B.NR_ORDEM = " + cursoMatricula.getNrOrdem() : ""))
							+ (curso.getLgMulti()==0 && lgEnturmacao==1 ? "" : " OR (B.LG_MULTI = 1 AND EXISTS (SELECT * FROM acd_curso_multi ACM WHERE ACM.cd_curso_multi = B.cd_curso AND ACM.cd_curso = "+cursoMatricula.getCdCurso()+")) ")
							+ (curso.getLgMulti()==0 && lgEnturmacao==1 ? "" : " OR (B.LG_MULTI = 0 AND EXISTS (SELECT * FROM acd_curso_correspondencia ACC WHERE (ACC.cd_curso = B.cd_curso OR ACC.cd_curso_correspondencia = B.cd_curso) AND (ACC.cd_curso = "+cursoMatricula.getCdCurso()+" OR ACC.cd_curso_correspondencia = "+cursoMatricula.getCdCurso()+"))) ")
							+ (curso.getLgMulti()==0 && lgEnturmacao==1 ? "" : " OR (B.LG_MULTI = 1 AND EXISTS (SELECT * FROM acd_curso_multi ACM WHERE ACM.cd_curso_multi = B.cd_curso AND EXISTS (SELECT * FROM acd_curso_correspondencia ACC WHERE (ACC.cd_curso = ACM.cd_curso OR ACC.cd_curso_correspondencia = ACM.cd_curso) AND (ACC.cd_curso = "+cursoMatricula.getCdCurso()+" OR ACC.cd_curso_correspondencia = "+cursoMatricula.getCdCurso()+")))) ")
							+ ") ", ItemComparator.EQUAL, Types.VARCHAR));
					
				}
				else{
					return null;
				}
			}
			else{
				return null;
			}
		}
		criterios.add(new ItemComparator("incluirTransferencia", Integer.toString(incluirTransferencia), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmTurmasPossiveis = find(criterios);
		// Colocar a quantidade de vagas na turma
		while(rsmTurmasPossiveis.next()){
			rsmTurmasPossiveis.setValueToField("nm_turma_antigo", rsmTurmasPossiveis.getString("nm_turma"));
			String nome = rsmTurmasPossiveis.getString("nm_produto_servico") + " - " + rsmTurmasPossiveis.getString("nm_turma") + (rsmTurmasPossiveis.getInt("TP_TURNO") >= 0 ? " - " + tiposTurno[rsmTurmasPossiveis.getInt("tp_turno")] : "")+
												" (" + rsmTurmasPossiveis.getInt("NR_MATRICULADOS") + " matriculados / "  + 
												       rsmTurmasPossiveis.getInt("qt_vagas") + " vagas) ";
			rsmTurmasPossiveis.setValueToField("nm_turma", nome);
		}
		
		// Retirar a turma atual do aluno do rsm
		rsmTurmasPossiveis.beforeFirst();
		while(rsmTurmasPossiveis.next()){
			if (rsmTurmasPossiveis.getInt("cd_turma") == cdTurma) {
				rsmTurmasPossiveis.deleteRow();
				return rsmTurmasPossiveis;
			}
			
			rsmTurmasPossiveis.setValueToField("ID_TURMA_VAGAS", rsmTurmasPossiveis.getString("nm_produto_servico") + " - " + rsmTurmasPossiveis.getString("nm_turma_antigo") + " (" + rsmTurmasPossiveis.getInt("QT_VAGAS_DISPONIVEIS") + " vagas disponíveis) " + (rsmTurmasPossiveis.getInt("TP_TURNO") >= 0 ? " - " + tiposTurno[rsmTurmasPossiveis.getInt("tp_turno")] : ""));
		}
		rsmTurmasPossiveis.beforeFirst();
		
		//Inclui no RSM as turmas que estão dentro do multi, caso a original seja multi
		if(curso != null && curso.getLgMulti() == 1 && cdMatricula > 0){
			
			int cdCursoAnterior = 0;
			
			int cdCursoEtapa = 0;
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_CURSO", Integer.toString(matricula.getCdCurso()), ItemComparator.EQUAL, Types.INTEGER));		
			ResultSetMap rsmCursoEtapa = CursoEtapaDAO.find(criterios);
			if(rsmCursoEtapa.next())
				cdCursoEtapa = rsmCursoEtapa.getInt("cd_curso_etapa");
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_CURSO_ETAPA_POSTERIOR", Integer.toString(cdCursoEtapa), ItemComparator.EQUAL, Types.INTEGER));		
			ResultSetMap rsmCursoAnterior = CursoEtapaDAO.find(criterios);
			if(rsmCursoAnterior.next())
				cdCursoAnterior = rsmCursoAnterior.getInt("cd_curso");
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_CURSO", Integer.toString(cdCursoAnterior), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.CD_INSTITUICAO", Integer.toString(cdInstituicao), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.CD_PERIODO_LETIVO", Integer.toString(cdPeriodoLetivo), ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsmTurmasPossiveisMulti = find(criterios);
			while(rsmTurmasPossiveisMulti.next()){
				String nome = rsmTurmasPossiveisMulti.getString("nm_turma") + (rsmTurmasPossiveisMulti.getInt("TP_TURNO") >= 0 ? " - " + tiposTurno[rsmTurmasPossiveisMulti.getInt("tp_turno")] : "") +
										" (" + rsmTurmasPossiveisMulti.getInt("NR_MATRICULADOS") + " matriculados / "  + 
										rsmTurmasPossiveisMulti.getInt("qt_vagas") + " vagas) ";
				rsmTurmasPossiveisMulti.setValueToField("nm_turma", nome);
				rsmTurmasPossiveis.addRegister(rsmTurmasPossiveisMulti.getRegister());
			}
		}
		
		rsmTurmasPossiveis.beforeFirst();
		return rsmTurmasPossiveis;
	}
	
	public static ResultSetMap getTurmaByCd(int cdTurma){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_turma", "" + cdTurma, ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios);
	}
	
	public static ResultSetMap getAllByCurso(int cdInstituicao, int cdCurso) {
		return getAllTurmas(cdInstituicao, cdCurso, null);
	}
	
	public static ResultSetMap getAllByCurso(int cdInstituicao, int cdCurso, int cdPeriodoLetivo) {
		return getAllTurmas(cdInstituicao, cdCurso, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap getTurma(int cdTurma){
		return getTurma(cdTurma, null);
	}
	
	public static ResultSetMap getTurma(int cdTurma, Connection connection){
		boolean isConnectionNull = connection==null;
		PreparedStatement pstmt;
		ResultSet rs;
		
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();		
			pstmt = connection.prepareStatement(
					"SELECT A.*, (F.nm_etapa || ' - ' || D.nm_produto_servico) AS nm_curso, C.nm_pessoa AS nm_fantasia FROM acd_turma A " +
					"JOIN acd_instituicao                    B  ON (A.cd_instituicao = B.cd_instituicao) " +
					"JOIN grl_pessoa                         C  ON (B.cd_instituicao = C.cd_pessoa) " +
					"LEFT OUTER JOIN grl_produto_servico     D  ON (A.cd_curso = D.cd_produto_servico) " +
					"LEFT OUTER JOIN acd_curso_etapa         E  ON (A.cd_curso = E.cd_curso) " +
					"LEFT OUTER JOIN acd_tipo_etapa          F  ON (E.cd_etapa = F.cd_etapa) " +
					"WHERE cd_turma=? "					
			);
			
			pstmt.setInt(1, cdTurma);
			rs = pstmt.executeQuery();
			
			ResultSetMap rsm =  new ResultSetMap(rs);
			
			while(rsm.next()){
				rsm.setValueToField("NM_TURMA_NORMAL", rsm.getString("NM_TURMA"));
				rsm.setValueToField("NM_TURNO", tiposTurno[rsm.getInt("TP_TURNO")]);
				rsm.setValueToField("NM_TURMA", tiposTurno[rsm.getInt("TP_TURNO")] + " - Turma " + rsm.getString("NM_TURMA"));
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Turma get(int cdTurma, Connection connection){
		ResultSetMap rsm = getTurma(cdTurma, connection);
		
		if(rsm.next()){
			return new Turma(rsm.getInt("cd_turma"),
					rsm.getInt("cd_matriz"),
					rsm.getInt("cd_periodo_letivo"),
					rsm.getString("nm_turma"),
					(rsm.getTimestamp("dt_abertura")==null)?null:Util.longToCalendar(rsm.getTimestamp("dt_abertura").getTime()),
					(rsm.getTimestamp("dt_conclusao")==null)?null:Util.longToCalendar(rsm.getTimestamp("dt_conclusao").getTime()),
					rsm.getInt("tp_turno"),
					rsm.getInt("cd_categoria_mensalidade"),
					rsm.getInt("cd_categoria_matricula"),
					rsm.getInt("st_turma"),
					rsm.getInt("cd_tabela_preco"),
					rsm.getInt("cd_instituicao"),
					rsm.getInt("cd_curso"),
					rsm.getInt("qt_vagas"),
					rsm.getInt("cd_curso_modulo"),
					rsm.getString("nr_inep"),
					rsm.getInt("qt_dias_semana_atividade"),
					rsm.getInt("tp_atendimento"),
					rsm.getInt("tp_modalidade_ensino"),
					rsm.getString("id_turma"),
					rsm.getInt("tp_educacao_infantil"),
					rsm.getInt("lg_mais_educa"),
					rsm.getInt("cd_turma_anterior"),
					rsm.getInt("tp_turno_atividade_complementar"),
					rsm.getInt("tp_local_diferenciado"));
		}
		else{
			return null;
		}
	}

	public static ResultSetMap getAllTurmas(int cdInstituicao) {
		return getAllTurmas(cdInstituicao, false, 0, null);
	}
	
	public static ResultSetMap getAllTurmas(int cdInstituicao, Connection connection) {
		return getAllTurmas(cdInstituicao, false, 0, connection);
	}
	
	public static ResultSetMap getAllTurmas(int cdInstituicao, boolean semCursosExtras, Connection connection) {
		return getAllTurmas(cdInstituicao, semCursosExtras, 0, connection);
	}
	
	public static ResultSetMap getAllTurmas(int cdInstituicao, boolean semCursosExtras, boolean lancarCenso, Connection connection) {
		return getAllTurmas(cdInstituicao, semCursosExtras, lancarCenso, 0, connection);
	}
	
	public static ResultSetMap getAllTurmas(int cdInstituicao, int cdCurso, Connection connection) {
		return getAllTurmas(cdInstituicao, false, cdCurso, connection);
	}
	
	public static ResultSetMap getAllTurmas(int cdInstituicao, int cdCurso, int cdPeriodoLetivo, Connection connection) {
		return getAllTurmas(cdInstituicao, false, false, cdCurso, cdPeriodoLetivo, connection);
	}
	
	public static ResultSetMap getAllTurmas(int cdInstituicao, boolean semCursosExtras, int cdCurso, Connection connection) {
		return getAllTurmas(cdInstituicao, semCursosExtras, false, cdCurso, connection);
	}
	
	public static ResultSetMap getAllTurmas(int cdInstituicao, boolean semCursosExtras, boolean lancarCenso, int cdCurso, Connection connection) {
		return getAllTurmas(cdInstituicao, semCursosExtras, lancarCenso, cdCurso, 0, connection);
	}
	
	public static ResultSetMap getAllTurmas(int cdInstituicao, boolean semCursosExtras, boolean lancarCenso, int cdCurso, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			int cdPeriodoRecente = 0;
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connection);
				if(rsmPeriodoRecente.next())
					cdPeriodoRecente = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			}
			else{
				cdPeriodoRecente = cdPeriodoLetivo;
			}
			
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_matriz, C.nm_periodo_letivo, D.nm_tipo_periodo " +
					"FROM acd_turma A " +
					"LEFT OUTER JOIN acd_curso_matriz        B ON (A.cd_matriz = B.cd_matriz " +
					"									       AND A.cd_curso  = B.cd_curso) " +
					"LEFT OUTER JOIN acd_instituicao_periodo C ON (A.cd_periodo_letivo = C.cd_periodo_letivo) " +
					"LEFT OUTER JOIN acd_tipo_periodo        D ON (C.cd_tipo_periodo = D.cd_tipo_periodo) " +
					"WHERE A.nm_turma NOT LIKE '%TRANS%' "+ 
					(lancarCenso ? " AND A.cd_curso <> 1187 " : "") +
					"  AND " + (cdCurso > 0 ? "A.cd_curso = "+cdCurso+" AND " : "") + 
					(semCursosExtras ? " A.cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+") AND " : "") + 
					" A.cd_instituicao = " + cdInstituicao + " AND A.cd_periodo_letivo = " + cdPeriodoRecente + "  AND st_turma IN (" + ST_ATIVO + ", " + ST_CONCLUIDO + ")");
			
			String dtReferenciaCenso = ParametroServices.getValorOfParametro("DT_REFERENCIA_CENSO", connection);
			if(dtReferenciaCenso == null){
				return null;
			}
			
			dtReferenciaCenso = dtReferenciaCenso + "/" + (cdPeriodoLetivo > 0 ? Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connection).getNmPeriodoLetivo()) : Util.getDataAtual().get(Calendar.YEAR));
			GregorianCalendar dtReferenciaCensoCalendar = Util.convStringToCalendar(dtReferenciaCenso);
			
			int x = 0;
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("NM_TURMA_COM_CONCLUINTES", tiposTurno[rsm.getInt("TP_TURNO")] + " - Turma " + rsm.getString("NM_TURMA") + " ("+getAlunos(rsm.getInt("CD_TURMA"), false, false, true, connection).size()+" concluintes)");
				rsm.setValueToField("NM_TURMA_NORMAL", rsm.getString("NM_TURMA"));
				rsm.setValueToField("NM_TURNO", (rsm.getInt("TP_TURNO") >= 0 && rsm.getInt("TP_TURNO") <= 5 ? tiposTurno[rsm.getInt("TP_TURNO")] : ""));
				rsm.setValueToField("NM_TURMA",(rsm.getInt("TP_TURNO") >= 0 && rsm.getInt("TP_TURNO") <= 5 ? tiposTurno[rsm.getInt("TP_TURNO")] + " - " : "") + " Turma " + rsm.getString("NM_TURMA"));
				
				ResultSetMap rsmTurmaSituacaoAlunoCenso = new ResultSetMap(connection.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_turma B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO + ") AND cd_turma = "+rsm.getInt("cd_turma")+" order by dt_ocorrencia DESC").executeQuery());
				if(rsmTurmaSituacaoAlunoCenso.next()){
					if(rsmTurmaSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO)){
						rsm.setValueToField("NM_TURMA_COM_CONCLUINTES", rsm.getString("NM_TURMA_COM_CONCLUINTES") + " - TURMA FECHADA");
					}
				}
				
				
				ResultSetMap rsmPeriodoLetivoPosterior = new ResultSetMap(connection.prepareStatement("SELECT A.cd_periodo_letivo, A.nm_periodo_letivo FROM acd_instituicao_periodo A WHERE A.nm_periodo_letivo = '"+(Integer.parseInt(InstituicaoPeriodoDAO.get(rsm.getInt("cd_periodo_letivo"), connection).getNmPeriodoLetivo())+1)+"' AND A.cd_instituicao = " + rsm.getInt("cd_instituicao")).executeQuery());
				if(rsmPeriodoLetivoPosterior.next()){
					rsm.setValueToField("CD_PERIODO_LETIVO_POSTERIOR", rsmPeriodoLetivoPosterior.getInt("cd_periodo_letivo"));
					rsm.setValueToField("NM_PERIODO_LETIVO_POSTERIOR", rsmPeriodoLetivoPosterior.getString("nm_periodo_letivo"));
				}
				
				if(lancarCenso){
					
					boolean excluido = false;
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_turma", "" + rsm.getInt("cd_turma"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_TURMA, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmOcorrenciaTurma = OcorrenciaTurmaServices.find(criterios, connection);
					while(rsmOcorrenciaTurma.next()){
						
						if(rsmOcorrenciaTurma.getGregorianCalendar("dt_ocorrencia").after(dtReferenciaCensoCalendar)){
							rsm.deleteRow();
							if(x == 0)
								rsm.beforeFirst();
							else
								rsm.previous();
							excluido = true;
						}
					}
					rsmOcorrenciaTurma.beforeFirst();
					
					if(excluido)
						continue;
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_turma", "" + rsm.getInt("cd_turma"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_tipo_ocorrencia", "" + InstituicaoEducacensoServices.TP_OCORRENCIA_ALTERACAO_TURMA, ItemComparator.EQUAL, Types.INTEGER));
					rsmOcorrenciaTurma = OcorrenciaTurmaServices.find(criterios, connection);
					while(rsmOcorrenciaTurma.next()){
						if(rsmOcorrenciaTurma.getGregorianCalendar("dt_ocorrencia").after(dtReferenciaCensoCalendar)){
							rsm.setValueToField("ST_TURMA", rsmOcorrenciaTurma.getInt("st_turma_anterior"));
							break;
						}
					}
					rsmOcorrenciaTurma.beforeFirst();
					
					if(excluido)
						continue;
					
					if(rsm.getInt("ST_TURMA") != ST_ATIVO){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						excluido = true;
					}
					
					if(excluido)
						continue;
					
					x++;
				}
			}
			rsm.beforeFirst();
			
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_TURMA");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro na turma: " + e.getMessage());
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAllTurmasByPeriodo(int cdPeriodoLetivo, int cdCurso) {
		return getAllTurmasByPeriodo(cdPeriodoLetivo, cdCurso, -1, null);
	}
	
	public static ResultSetMap getAllTurmasByPeriodo(int cdPeriodoLetivo, int cdCurso, Connection connection) {
		return getAllTurmasByPeriodo(cdPeriodoLetivo, cdCurso, -1, connection);
	}
	
	public static ResultSetMap getAllTurmasByPeriodo(int cdPeriodoLetivo, int cdCurso, int tpTurno) {
		return getAllTurmasByPeriodo(cdPeriodoLetivo, cdCurso, tpTurno, null);
	}
	
	public static ResultSetMap getAllTurmasByPeriodo(int cdPeriodoLetivo, int cdCurso, int tpTurno, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_matriz, C.nm_periodo_letivo " +
					"FROM acd_turma A " +
					"LEFT OUTER JOIN acd_curso_matriz        B ON (A.cd_matriz = B.cd_matriz " +
					"									       AND A.cd_curso  = B.cd_curso) " +
					"LEFT OUTER JOIN acd_instituicao_periodo C ON (A.cd_periodo_letivo = C.cd_periodo_letivo) " +
					"WHERE A.cd_curso = "+cdCurso+
					"  AND A.cd_periodo_letivo = " + cdPeriodoLetivo + 
					"  AND A.nm_turma <> 'TRANSFERENCIA'" +
					"  AND st_turma = " + ST_ATIVO + 
					(tpTurno >= 0 ? " AND tp_turno = " + tpTurno : ""));
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_TURMA");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro na turma: " + e.getMessage());
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Busca mais simplificada das turmas, para melhorar o processamento
	 * @param criterios
	 * @return
	 */
	public static ResultSetMap findSimplificado(ArrayList<ItemComparator> criterios) {
		Connection connect = Conexao.conectar();
		try{
			int cdPeriodoLetivoOrigem = 0;
			int cdPeriodoLetivo       = 0;
			int cdInstituicao         = 0;
			int cdCurso         	  = 0;
			Curso curso 			  = null; 
			int nrOrdem               = 0;
			int lgMulti          	  = -1;
			for(int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_CURSO")) {
					cdCurso = Integer.parseInt(criterios.get(i).getValue());
					curso = CursoDAO.get(cdCurso, connect);
					nrOrdem = curso.getNrOrdem();
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO")) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
				}
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_PERIODO_LETIVO")) {
					cdPeriodoLetivo = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivoOrigem")) {
					cdPeriodoLetivoOrigem = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("BC.LG_MULTI")) {
					lgMulti = Integer.parseInt(criterios.get(i).getValue());
				}
			}
			
			if(cdPeriodoLetivoOrigem > 0){
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivoOrigem, connect);
				ArrayList<ItemComparator> criteriosInstituicaoPeriodo = new ArrayList<ItemComparator>();
				criteriosInstituicaoPeriodo.add(new ItemComparator("NM_PERIODO_LETIVO", instituicaoPeriodo.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
				criteriosInstituicaoPeriodo.add(new ItemComparator("CD_INSTITUICAO", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoDAO.find(criteriosInstituicaoPeriodo, connect);
				if(rsmPeriodoLetivo.next()){
					cdPeriodoLetivo = rsmPeriodoLetivo.getInt("cd_periodo_letivo");
				}
			}
			
			ResultSetMap rsmFinal = new ResultSetMap();
			ArrayList<Integer> codigosTurma = new ArrayList<Integer>();
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT A.*, C.nm_produto_servico, (J.nm_etapa || ' - ' || C.nm_produto_servico) AS nm_curso, I.cd_curso_etapa FROM acd_turma A"
					+ "			  											LEFT OUTER JOIN acd_curso_multi B ON (A.cd_curso = B.cd_curso_multi) "
					+ "			  											LEFT OUTER JOIN acd_curso       AC ON (B.cd_curso = AC.cd_curso) "
					+ "			  											LEFT OUTER JOIN acd_curso       BC ON (A.cd_curso = BC.cd_curso) "
					+ "														LEFT OUTER JOIN grl_produto_servico C ON (A.cd_curso = C.cd_produto_servico) "
					+ "														LEFT OUTER JOIN acd_curso_etapa        I ON (A.cd_curso = I.cd_curso) " 
					+ "														LEFT OUTER JOIN acd_tipo_etapa        J ON (I.cd_etapa = J.cd_etapa)"
					+ "														WHERE A.cd_instituicao = " + cdInstituicao
					+ "														  AND A.cd_periodo_letivo = " + cdPeriodoLetivo
					+ (lgMulti >= 0 ? "										  AND BC.lg_multi = " + lgMulti : "")
					+ "														  AND (A.st_turma = " + ST_ATIVO + " OR A.st_turma = " + ST_PENDENTE + " OR A.st_turma = " + ST_CONCLUIDO + ")"
					+ "										  				  AND (A.cd_curso = " + cdCurso + " OR B.cd_curso = " + cdCurso + (nrOrdem >= 0 ? " OR "+ (curso != null && curso.getLgMulti() > 0 ? "A" : "B") + "C.nr_ordem = " + nrOrdem : "") + ")").executeQuery());
			
			while(rsm.next()){
				
				int qtVagas = rsm.getInt("qt_vagas");
				int qtMatriculas = getAlunos(rsm.getInt("cd_turma")).getLines().size();
				int qtVagasDisponiveis = qtVagas - qtMatriculas;
				rsm.setValueToField("QT_VAGAS_DISPONIVEIS", qtVagasDisponiveis);
				rsm.setValueToField("CL_TP_TURNO", tiposTurno[rsm.getInt("TP_TURNO")]);
				rsm.setValueToField("ID_TURMA_VAGAS", rsm.getString("nm_produto_servico") + " - " + rsm.getString("nm_turma") + " (" + qtVagasDisponiveis + " vagas disponíveis) - " + rsm.getString("CL_TP_TURNO"));
				
				ResultSetMap rsmCursoAnterior = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND B.cd_curso_etapa_posterior = " + rsm.getInt("cd_curso_etapa")).executeQuery());
				if(rsmCursoAnterior.next()){
					rsm.setValueToField("CD_CURSO_ANTERIOR", rsmCursoAnterior.getInt("cd_curso"));
					rsm.setValueToField("NM_CURSO_ANTERIOR", rsmCursoAnterior.getString("nm_curso"));
				}
				
				ResultSetMap rsmCursoPosterior = new ResultSetMap(connect.prepareStatement("SELECT C.cd_curso, D.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, acd_curso_etapa C, grl_produto_servico D WHERE A.cd_curso = B.cd_curso AND B.cd_curso_etapa = " + rsm.getInt("cd_curso_etapa") + " AND B.cd_curso_etapa_posterior = C.cd_curso_etapa AND C.cd_curso = D.cd_produto_servico").executeQuery());
				if(rsmCursoPosterior.next()){
					rsm.setValueToField("CD_CURSO_POSTERIOR", rsmCursoPosterior.getInt("cd_curso"));
					rsm.setValueToField("NM_CURSO_POSTERIOR", rsmCursoPosterior.getString("nm_curso"));
				}
				ResultSetMap rsmPeriodoLetivoPosterior = new ResultSetMap(connect.prepareStatement("SELECT A.cd_periodo_letivo, A.nm_periodo_letivo FROM acd_instituicao_periodo A WHERE A.nm_periodo_letivo = '"+(Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo())+1)+"' AND A.cd_instituicao = " + cdInstituicao).executeQuery());
				if(rsmPeriodoLetivoPosterior.next()){
					rsm.setValueToField("CD_PERIODO_LETIVO_POSTERIOR", rsmPeriodoLetivoPosterior.getInt("cd_periodo_letivo"));
					rsm.setValueToField("NM_PERIODO_LETIVO_POSTERIOR", rsmPeriodoLetivoPosterior.getString("nm_periodo_letivo"));
				}
				
				if(!codigosTurma.contains(rsm.getInt("cd_turma"))){
					rsmFinal.addRegister(rsm.getRegister());
					codigosTurma.add(rsm.getInt("cd_turma"));
				}
				
			}
			rsmFinal.beforeFirst();
			
			return rsmFinal;
		}
		catch(SQLException e) {
			e.printStackTrace(System.out);
			System.out.println(e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			boolean lgIncluirInativos = false;
			int cdPeriodoLetivoOrigem = 0;
			int cdInstituicao         = 0;
			String sqlAdicional = null;
			int incluirTransferencia = 0;
			for(int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO") && Integer.parseInt(criterios.get(i).getValue()) != ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0)) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgIncluirInativos")) {
					lgIncluirInativos = true;
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivoOrigem")) {
					cdPeriodoLetivoOrigem = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("sqlAdicional")) {
					sqlAdicional = criterios.get(i).getValue();
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("incluirTransferencia")) {
					incluirTransferencia = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
			}
			if(cdPeriodoLetivoOrigem > 0){
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivoOrigem, connect);
				ArrayList<ItemComparator> criteriosInstituicaoPeriodo = new ArrayList<ItemComparator>();
				criteriosInstituicaoPeriodo.add(new ItemComparator("NM_PERIODO_LETIVO", instituicaoPeriodo.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
				criteriosInstituicaoPeriodo.add(new ItemComparator("CD_INSTITUICAO", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoDAO.find(criteriosInstituicaoPeriodo, connect);
				if(rsmPeriodoLetivo.next()){
					criterios.add(new ItemComparator("A.CD_PERIODO_LETIVO", rsmPeriodoLetivo.getString("cd_periodo_letivo"), ItemComparator.EQUAL, Types.INTEGER));
				}
			}
			
			ResultSetMap rsm = Search.find(
				    "SELECT A.*, B.*, C.id_produto_servico, C.nm_produto_servico, I.cd_curso_etapa, (J.nm_etapa || ' - ' || C.nm_produto_servico) AS nm_curso, D.nm_periodo_letivo, E.nm_matriz, H.nm_pessoa AS nm_instituicao "+
				    	    "FROM acd_turma A "+
				    	    "LEFT OUTER JOIN acd_oferta          A2 ON (A.cd_turma = A2.cd_turma) "+
				    	    "LEFT OUTER JOIN acd_curso           B ON (A.cd_curso = B.cd_curso) "+
				    	    "LEFT OUTER JOIN grl_produto_servico C ON (B.cd_curso = C.cd_produto_servico) "+
				    	    "LEFT OUTER JOIN acd_instituicao_periodo D ON (A.cd_periodo_letivo = D.cd_periodo_letivo) "+
				    	    "LEFT OUTER JOIN acd_curso_matriz    E ON (A.cd_curso  = E.cd_curso "+
				    	    "           AND A.cd_matriz = E.cd_matriz) "+
				    	    "LEFT OUTER JOIN grl_pessoa H ON( A.cd_instituicao = H.cd_pessoa ) " +
				    	    "LEFT OUTER JOIN acd_instituicao_educacenso ACD ON( A.cd_instituicao = ACD.cd_instituicao AND D.cd_periodo_letivo = ACD.cd_periodo_letivo) " +
				    	    "LEFT OUTER JOIN acd_curso_etapa        I ON (A.cd_curso = I.cd_curso) " +
				    	    "LEFT OUTER JOIN acd_tipo_etapa        J ON (I.cd_etapa = J.cd_etapa)"+
				    	    (incluirTransferencia == 1 ? "LEFT OUTER " : "") + " JOIN acd_instituicao_curso        L ON (A.cd_curso = L.cd_curso"+
				    	    "            AND A.cd_instituicao = L.cd_instituicao "+
				    	    "            AND A.cd_periodo_letivo = L.cd_periodo_letivo) "+
				    	    "LEFT OUTER JOIN acd_curso_multi     M ON (A.cd_curso = M.cd_curso)"+
				    	    "WHERE 1=1 " + (incluirTransferencia == 1 ? "" : " AND nm_turma NOT LIKE '%TRANSFERENCIA%' ") + 
				    	    (!lgIncluirInativos ? " AND st_turma <> " + ST_INATIVO : "") +
				    	    (sqlAdicional != null ? sqlAdicional : ""), 
				    	    " GROUP BY A.cd_turma, B.cd_curso, C.id_produto_servico, I.cd_curso_etapa, J.nm_etapa, C.nm_produto_servico, "+
				    	    " D.nm_periodo_letivo, E.nm_matriz, H.nm_pessoa, D.dt_inicial " +
				    	    " ORDER BY D.dt_inicial DESC, I.cd_curso_etapa, J.nm_etapa, C.nm_produto_servico ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			while(rsm.next()){
				
				int qtVagas = rsm.getInt("qt_vagas");
				int qtMatriculas = getAlunos(rsm.getInt("cd_turma"), false, false, true).getLines().size();
				int qtVagasDisponiveis = qtVagas - qtMatriculas;
				
				rsm.setValueToField("QT_VAGAS_DISPONIVEIS", qtVagasDisponiveis);
				rsm.setValueToField("CL_TURNO", (rsm.getInt("TP_TURNO") <= 5 && rsm.getInt("TP_TURNO") >= 0 ? tiposTurno[rsm.getInt("TP_TURNO")] : ""));
				rsm.setValueToField("ID_TURMA_VAGAS", rsm.getString("nm_turma") + " (" + qtVagasDisponiveis + " vagas disponíveis) - " + rsm.getString("CL_TP_TURNO"));
				rsm.setValueToField("NR_MATRICULADOS", qtMatriculas);
				rsm.setValueToField("CL_TP_MODALIDADE_ENSINO", tipoModalidade[rsm.getInt("TP_MODALIDADE_ENSINO")]);
				rsm.setValueToField("CL_ST_TURMA", situacoesTurma[rsm.getInt("ST_TURMA")]);
				
				ResultSetMap rsmCursoAnterior = new ResultSetMap(connect.prepareStatement("SELECT A.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND A.cd_curso = C.cd_produto_servico AND B.cd_curso_etapa_posterior = " + rsm.getInt("cd_curso_etapa")).executeQuery());
				if(rsmCursoAnterior.next()){
					rsm.setValueToField("CD_CURSO_ANTERIOR", rsmCursoAnterior.getInt("cd_curso"));
					rsm.setValueToField("NM_CURSO_ANTERIOR", rsmCursoAnterior.getString("nm_curso"));
				}
				
				ResultSetMap rsmCursoPosterior = new ResultSetMap(connect.prepareStatement("SELECT C.cd_curso, D.nm_produto_servico AS nm_curso FROM acd_curso A, acd_curso_etapa B, acd_curso_etapa C, grl_produto_servico D WHERE A.cd_curso = B.cd_curso AND B.cd_curso_etapa = " + rsm.getInt("cd_curso_etapa") + " AND B.cd_curso_etapa_posterior = C.cd_curso_etapa AND C.cd_curso = D.cd_produto_servico").executeQuery());
				if(rsmCursoPosterior.next()){
					rsm.setValueToField("CD_CURSO_POSTERIOR", rsmCursoPosterior.getInt("cd_curso"));
					rsm.setValueToField("NM_CURSO_POSTERIOR", rsmCursoPosterior.getString("nm_curso"));
				}
				
				InstituicaoPeriodo periodoLetivo = InstituicaoPeriodoDAO.get(rsm.getInt("cd_periodo_letivo"), connect);
				if(periodoLetivo != null){
					ResultSetMap rsmPeriodoLetivoPosterior = new ResultSetMap(connect.prepareStatement("SELECT A.cd_periodo_letivo, A.nm_periodo_letivo FROM acd_instituicao_periodo A WHERE A.nm_periodo_letivo = '"+(Integer.parseInt(periodoLetivo.getNmPeriodoLetivo())+1)+"' AND A.cd_instituicao = " + cdInstituicao).executeQuery());
					if(rsmPeriodoLetivoPosterior.next()){
						rsm.setValueToField("CD_PERIODO_LETIVO_POSTERIOR", rsmPeriodoLetivoPosterior.getInt("cd_periodo_letivo"));
						rsm.setValueToField("NM_PERIODO_LETIVO_POSTERIOR", rsmPeriodoLetivoPosterior.getString("nm_periodo_letivo"));
					}
				}
				
				
				rsm.setValueToField("LG_CRECHE", 0);
				rsm.setValueToField("LG_PRE_ESCOLA", 0);
				rsm.setValueToField("LG_ATENDIMENTO_ESPECIALIZADO", 0);
				rsm.setValueToField("LG_ATIVIDADE_COMPLEMENTAR", 0);
				rsm.setValueToField("LG_PROJOVEM", 0);
				
				Curso cursoTurma = CursoDAO.get(rsm.getInt("cd_curso"), connect);
				if(cursoTurma != null){
					PreparedStatement pstmt  =connect.prepareStatement("SELECT * FROM acd_curso_multi WHERE cd_curso_multi = ? AND (cd_curso = " + cursoTurma.getCdCurso() + " OR cd_curso_multi = " + cursoTurma.getCdCurso()+")");
					
					pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_CRECHE", 0));
					ResultSetMap rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
					if(rsmCursoMatricula.next()){
						rsm.setValueToField("LG_CRECHE", 1);
					}
					pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_PRE_ESCOLA", 0));
					rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
					if(rsmCursoMatricula.next()){
						rsm.setValueToField("LG_PRE_ESCOLA", 1);
					}
					if(cursoTurma.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)){
						rsm.setValueToField("LG_ATENDIMENTO_ESPECIALIZADO", 1);
					}
					if(cursoTurma.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)){
						rsm.setValueToField("LG_ATIVIDADE_COMPLEMENTAR", 1);
					}
					if(cursoTurma.getCdCurso() == ParametroServices.getValorOfParametroAsInteger("CD_CURSO_PROJOVEM", 0)){
						rsm.setValueToField("LG_PROJOVEM", 1);
					}
				}
				
				rsm.setValueToField("NM_TURMA_COMPLETO", rsm.getString("NM_CURSO") + " - " + rsm.getString("NM_TURMA"));
			}
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			rsm.orderBy(fields);
			
			rsm.beforeFirst();
			return rsm;
			
		}
		catch(SQLException e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result findRelatorioTurmasPorTurno(ArrayList<ItemComparator> criterios){
		return findRelatorioTurmasPorTurno(criterios, null);
	}
	
	public static Result findRelatorioTurmasPorTurno(ArrayList<ItemComparator> criterios, Connection connection){
		boolean isConnectionNull = connection == null;
		try {
			
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			
			Result result = findRelatorio(criterios, connection);
			ResultSetMap rsm = (ResultSetMap) result.getObjects().get("RSM");
			int qtTurmasIntegral = 0;
			int qtTurmasMatutino = 0;
			int qtTurmasVespertino = 0;
			int qtTurmasNoturno = 0;
			String nmInstituicao = null;
			String nmLocalizacao = "";
			int cdInstituicao = 0;
			ResultSetMap rsmFinal = new ResultSetMap();
			while(rsm.next()){
				
				if(nmInstituicao != null && !nmInstituicao.equals(rsm.getString("NM_INSTITUICAO"))){
					
					ResultSetMap rsmDependenciaSalas = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_dependencia WHERE cd_instituicao = " + cdInstituicao + " AND cd_tipo_dependencia = " + ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DEPENDENCIA_SALA", 0)).executeQuery());
					int vlCapacidade = 0;
					while(rsmDependenciaSalas.next()){
						vlCapacidade += rsmDependenciaSalas.getInt("VL_CAPACIDADE");
					}
					rsmDependenciaSalas.beforeFirst();
					
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("NM_LOCALIZACAO", nmLocalizacao);
					register.put("NM_INSTITUICAO", nmInstituicao + " (Salas de Aula: " + rsmDependenciaSalas.size() + ", Capacidade total: " + vlCapacidade + ")");
					register.put("NM_TURNO", "Integral");
					register.put("QT_TURMAS", qtTurmasIntegral);
					rsmFinal.addRegister(register);
					register = new HashMap<String, Object>();
					register.put("NM_LOCALIZACAO", nmLocalizacao);
					register.put("NM_INSTITUICAO", nmInstituicao + " (Salas de Aula: " + rsmDependenciaSalas.size() + ", Capacidade total: " + vlCapacidade + ")");
					register.put("NM_TURNO", "Matutino");
					register.put("QT_TURMAS", qtTurmasMatutino);
					rsmFinal.addRegister(register);
					register = new HashMap<String, Object>();
					register.put("NM_LOCALIZACAO", nmLocalizacao);
					register.put("NM_INSTITUICAO", nmInstituicao + " (Salas de Aula: " + rsmDependenciaSalas.size() + ", Capacidade total: " + vlCapacidade + ")");
					register.put("NM_TURNO", "Vespertino");
					register.put("QT_TURMAS", qtTurmasVespertino);
					rsmFinal.addRegister(register);
					register = new HashMap<String, Object>();
					register.put("NM_LOCALIZACAO", nmLocalizacao);
					register.put("NM_INSTITUICAO", nmInstituicao + " (Salas de Aula: " + rsmDependenciaSalas.size() + ", Capacidade total: " + vlCapacidade + ")");
					register.put("NM_TURNO", "Noturno");
					register.put("QT_TURMAS", qtTurmasNoturno);
					rsmFinal.addRegister(register);
					
					qtTurmasIntegral = 0;
					qtTurmasMatutino = 0;
					qtTurmasVespertino = 0;
					qtTurmasNoturno = 0;
				}
				
				cdInstituicao = rsm.getInt("CD_INSTITUICAO");
				nmInstituicao = rsm.getString("NM_INSTITUICAO");
				nmLocalizacao = rsm.getString("NM_LOCALIZACAO");
				
				if(rsm.getInt("TP_TURNO") == TP_INTEGRAL){
					qtTurmasIntegral++;
				}
				else if(rsm.getInt("TP_TURNO") == TP_MATUTINO){
					qtTurmasMatutino++;
				}
				else if(rsm.getInt("TP_TURNO") == TP_VESPERTINO){
					qtTurmasVespertino++;
				}
				else if(rsm.getInt("TP_TURNO") == TP_NOTURNO){
					qtTurmasNoturno++;
				}
				
			}
			rsm.beforeFirst();
			rsmFinal.beforeFirst();
			
			//ULTIMA INSTITUICAO
			ResultSetMap rsmDependenciaSalas = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_instituicao_dependencia WHERE cd_instituicao = " + cdInstituicao + " AND cd_tipo_dependencia = " + ParametroServices.getValorOfParametroAsInteger("CD_TIPO_DEPENDENCIA_SALA", 0)).executeQuery());
			int vlCapacidade = 0;
			while(rsmDependenciaSalas.next()){
				vlCapacidade += rsmDependenciaSalas.getInt("VL_CAPACIDADE");
			}
			rsmDependenciaSalas.beforeFirst();
			
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NM_LOCALIZACAO", nmLocalizacao);
			register.put("NM_INSTITUICAO", nmInstituicao + " (Salas de Aula: " + rsmDependenciaSalas.size() + ", Capacidade total: " + vlCapacidade + ")");
			register.put("NM_TURNO", "Integral");
			register.put("QT_TURMAS", qtTurmasIntegral);
			rsmFinal.addRegister(register);
			register = new HashMap<String, Object>();
			register.put("NM_LOCALIZACAO", nmLocalizacao);
			register.put("NM_INSTITUICAO", nmInstituicao + " (Salas de Aula: " + rsmDependenciaSalas.size() + ", Capacidade total: " + vlCapacidade + ")");
			register.put("NM_TURNO", "Matutino");
			register.put("QT_TURMAS", qtTurmasMatutino);
			rsmFinal.addRegister(register);
			register = new HashMap<String, Object>();
			register.put("NM_LOCALIZACAO", nmLocalizacao);
			register.put("NM_INSTITUICAO", nmInstituicao + " (Salas de Aula: " + rsmDependenciaSalas.size() + ", Capacidade total: " + vlCapacidade + ")");
			register.put("NM_TURNO", "Vespertino");
			register.put("QT_TURMAS", qtTurmasVespertino);
			rsmFinal.addRegister(register);
			register = new HashMap<String, Object>();
			register.put("NM_LOCALIZACAO", nmLocalizacao);
			register.put("NM_INSTITUICAO", nmInstituicao + " (Salas de Aula: " + rsmDependenciaSalas.size() + ", Capacidade total: " + vlCapacidade + ")");
			register.put("NM_TURNO", "Noturno");
			register.put("QT_TURMAS", qtTurmasNoturno);
			rsmFinal.addRegister(register);
			
			rsmFinal.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_LOCALIZACAO");
			fields.add("NM_INSTITUICAO");
			rsmFinal.orderBy(fields);
			rsmFinal.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			result = new Result(1, "Sucesso!");
			result.addObject("RSM", rsmFinal);
			if (isConnectionNull)
				connection.commit();
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result findRelatorioTurmasPorModalidade(ArrayList<ItemComparator> criterios){
		return findRelatorioTurmasPorModalidade(criterios, null);
	}
	
	public static Result findRelatorioTurmasPorModalidade(ArrayList<ItemComparator> criterios, Connection connection){
		boolean isConnectionNull = connection == null;
		try {
			
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			
			Result result = findRelatorio(criterios, connection);
			ResultSetMap rsm = (ResultSetMap) result.getObjects().get("RSM");
			int qtTurmas = 0;
			String nmInstituicao = null;
			String nmLocalizacao = "";
			String nmCurso = "";
			
			Map<String, Integer> registerQuantidadeTotal = new TreeMap<String,Integer>();
			ResultSetMap rsmFinal = new ResultSetMap();
			while(rsm.next()){
				
				if(nmInstituicao != null && (!nmInstituicao.equals(rsm.getString("NM_INSTITUICAO")) || !nmCurso.equals(rsm.getString("NM_CURSO")))){
					
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("NM_LOCALIZACAO", nmLocalizacao);
					register.put("NM_INSTITUICAO", nmInstituicao);
					register.put("NM_CURSO", nmCurso);
					register.put("QT_TURMAS", qtTurmas);
					rsmFinal.addRegister(register);
					
					if(!registerQuantidadeTotal.containsKey(nmCurso)){
						registerQuantidadeTotal.put(nmCurso, qtTurmas);
					}
					else{
						registerQuantidadeTotal.put(nmCurso, registerQuantidadeTotal.get(nmCurso) + qtTurmas);
					}
					
					qtTurmas = 0;
				}
				
				nmInstituicao = rsm.getString("NM_INSTITUICAO");
				nmLocalizacao = rsm.getString("NM_LOCALIZACAO");
				nmCurso = rsm.getString("NM_CURSO");
				qtTurmas++;
			}
			rsm.beforeFirst();
			rsmFinal.beforeFirst();
			
			
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NM_LOCALIZACAO", nmLocalizacao);
			register.put("NM_INSTITUICAO", nmInstituicao);
			register.put("NM_CURSO", nmCurso);
			register.put("QT_TURMAS", qtTurmas);
			rsmFinal.addRegister(register);
			
			if(!registerQuantidadeTotal.containsKey(nmCurso)){
				registerQuantidadeTotal.put(nmCurso, qtTurmas);
			}
			else{
				registerQuantidadeTotal.put(nmCurso, registerQuantidadeTotal.get(nmCurso) + qtTurmas);
			}
			
			
			String txtTotalTurmas = "Total de Turmas por modalidade (Geral):\n\n";
			for(Map.Entry<String, Integer> entry : registerQuantidadeTotal.entrySet()){
				txtTotalTurmas += entry.getKey() + ": " + entry.getValue() + "\n";
			}
			
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_LOCALIZACAO");
			fields.add("NM_INSTITUICAO");
			rsmFinal.orderBy(fields);
			rsmFinal.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			result = new Result(1, "Sucesso!");
			result.addObject("RSM", rsmFinal);
			result.addObject("TXT_TOTAL_QUANTIDADE", txtTotalTurmas);
			
			if (isConnectionNull)
				connection.commit();
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	public static Result findRelatorio(ArrayList<ItemComparator> criterios) {
		return findRelatorio(criterios, null);
	}

	public static Result findRelatorio(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			boolean lgIncluirInativos = false;
			int cdPeriodoLetivoOrigem = 0;
			int cdInstituicao         = 0;
			int cdPeriodoLetivo       = 0;
			String sqlAdicional = null;
			int incluirTransferencia = 0;
			int tpApenasSemAlunos = -1;
			boolean semDetalhamentoTurma = false;
			int tpModalidade   = -1;
			
			for(int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO") && Integer.parseInt(criterios.get(i).getValue()) != ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0)) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgIncluirInativos")) {
					lgIncluirInativos = true;
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivoOrigem")) {
					cdPeriodoLetivoOrigem = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("sqlAdicional")) {
					sqlAdicional = criterios.get(i).getValue();
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("incluirTransferencia")) {
					incluirTransferencia = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivo")) {
					cdPeriodoLetivo = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpApenasSemAlunos")) {
					tpApenasSemAlunos = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("semDetalhamentoTurma")) {
					semDetalhamentoTurma = true;
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpModalidade")) {
					tpModalidade = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
			}
			if(cdPeriodoLetivoOrigem > 0){
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivoOrigem, connect);
				ArrayList<ItemComparator> criteriosInstituicaoPeriodo = new ArrayList<ItemComparator>();
				criteriosInstituicaoPeriodo.add(new ItemComparator("NM_PERIODO_LETIVO", instituicaoPeriodo.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
				criteriosInstituicaoPeriodo.add(new ItemComparator("CD_INSTITUICAO", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoDAO.find(criteriosInstituicaoPeriodo, connect);
				if(rsmPeriodoLetivo.next()){
					criterios.add(new ItemComparator("A.CD_PERIODO_LETIVO", rsmPeriodoLetivo.getString("cd_periodo_letivo"), ItemComparator.EQUAL, Types.INTEGER));
				}
			}
			 
			ResultSetMap rsm = Search.find(
				    "SELECT A.*, B.*, C.id_produto_servico, C.nm_produto_servico, I.cd_curso_etapa, (C.nm_produto_servico) AS nm_curso, D.nm_periodo_letivo, E.nm_matriz, H.nm_pessoa AS nm_instituicao, J.nm_etapa "+
				    	    "FROM acd_turma A "+
				    	    "LEFT OUTER JOIN acd_curso           B ON (A.cd_curso = B.cd_curso) "+
				    	    "LEFT OUTER JOIN grl_produto_servico C ON (B.cd_curso = C.cd_produto_servico) "+
				    	    "LEFT OUTER JOIN acd_instituicao_periodo D ON (A.cd_periodo_letivo = D.cd_periodo_letivo) "+
				    	    "LEFT OUTER JOIN acd_curso_matriz    E ON (A.cd_curso  = E.cd_curso "+
				    	    "           AND A.cd_matriz = E.cd_matriz) "+
				    	    "LEFT OUTER JOIN grl_pessoa H ON( A.cd_instituicao = H.cd_pessoa ) " +
				    	    "LEFT OUTER JOIN acd_instituicao_educacenso ACD ON( A.cd_instituicao = ACD.cd_instituicao AND D.cd_periodo_letivo = ACD.cd_periodo_letivo ) " +
				    	    "LEFT OUTER JOIN acd_curso_etapa        I ON (A.cd_curso = I.cd_curso) " +
				    	    "LEFT OUTER JOIN acd_tipo_etapa        J ON (I.cd_etapa = J.cd_etapa)"+
				    	    (incluirTransferencia == 1 ? "LEFT OUTER " : "") + " JOIN acd_instituicao_curso        L ON (A.cd_curso = L.cd_curso"+
				    	    "            AND A.cd_instituicao = L.cd_instituicao "+
				    	    "            AND A.cd_periodo_letivo = L.cd_periodo_letivo) "+
				    	    "LEFT OUTER JOIN acd_curso_multi     M ON (A.cd_curso = M.cd_curso)"+
				    	    "WHERE 1=1 " + (!lgIncluirInativos ? " AND st_turma <> " + ST_INATIVO : "") +
				    	    (sqlAdicional != null ? sqlAdicional : "") + 
				    	    (tpApenasSemAlunos == -1 ? "" : " AND " + (tpApenasSemAlunos == 1 ? " NOT " : "") + " EXISTS (SELECT MAT.cd_matricula FROM acd_matricula MAT WHERE MAT.cd_turma = A.cd_turma) ")+
				    	    (cdPeriodoLetivo > 0 ? " AND (A.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") " : ""), 
				    	    " GROUP BY A.cd_turma, B.cd_curso, C.id_produto_servico, I.cd_curso_etapa, J.nm_etapa, C.nm_produto_servico, "+
				    	    " D.nm_periodo_letivo, E.nm_matriz, H.nm_pessoa, D.dt_inicial " +
				    	    " ORDER BY D.dt_inicial DESC, I.cd_curso_etapa, J.nm_etapa, C.nm_produto_servico ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			int qtTurmasZonaUrbana = 0;
			int qtTurmasZonaRural = 0;
			int qtTurmasCreche = 0;
			int qtTurmasMatriculadosZonaUrbana = 0;
			int qtTurmasMatriculadosZonaRural = 0;
			int qtTurmasMatriculadosCreche = 0;
			int x = 0;
			while(rsm.next()){
				
				if(rsm.getInt("SUB_TURMA") == 1){
					continue;
				}
				
				
				if(tpModalidade == 1){
					boolean achado = false;
					ResultSetMap rsmCursosEducacaoInfantil 	= ParametroServices.getValoresOfParametro("CD_CURSO_EDUCACAO_INFANTIL");
					while(rsmCursosEducacaoInfantil.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosEducacaoInfantil.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					rsmCursosEducacaoInfantil.beforeFirst();
					if(!achado){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				else if(tpModalidade == 2){
					boolean achado = false;
					ResultSetMap rsmCursosFundamental1 = ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_1");
					while(rsmCursosFundamental1.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental1.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					if(!achado){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				else if(tpModalidade == 3){
					boolean achado = false;
					ResultSetMap rsmCursosFundamental2 = ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_2");
					while(rsmCursosFundamental2.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental2.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					
					if(!achado){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				
				int qtVagas = rsm.getInt("qt_vagas");
				int qtMatriculas = getAlunos(rsm.getInt("cd_turma"), false, false, true).getLines().size();
				int qtMatriculasPendentes = getAlunos(rsm.getInt("cd_turma"), false, true).getLines().size();
				int qtVagasDisponiveis = qtVagas - qtMatriculas;
				
					
				
				rsm.setValueToField("QT_VAGAS_DISPONIVEIS", qtVagasDisponiveis);
				rsm.setValueToField("CL_TURNO", (rsm.getInt("TP_TURNO") <= 5 && rsm.getInt("TP_TURNO") >= 0 ? tiposTurno[rsm.getInt("TP_TURNO")] : ""));
				rsm.setValueToField("ID_TURMA_VAGAS", rsm.getString("nm_turma") + " (" + qtVagasDisponiveis + " vagas disponíveis) - " + rsm.getString("CL_TP_TURNO"));
				rsm.setValueToField("NR_MATRICULADOS", qtMatriculas);
				rsm.setValueToField("NR_MATRICULADOS_PENDENTES", qtMatriculasPendentes);
				rsm.setValueToField("CL_TP_MODALIDADE_ENSINO", tipoModalidade[rsm.getInt("TP_MODALIDADE_ENSINO")]);
				rsm.setValueToField("CL_ST_TURMA", situacoesTurma[rsm.getInt("ST_TURMA")]);
				
				rsm.setValueToField("NR_VAGAS", qtVagas);
				if(qtVagas > 0)
					rsm.setValueToField("PR_OCUPACAO", "" + ((qtMatriculas * 100) / qtVagas) + "%");
				else
					rsm.setValueToField("PR_OCUPACAO", "0%");
				
				Curso curso = CursoDAO.get(rsm.getInt("CD_CURSO"), connect);
				if(curso != null){
					rsm.setValueToField("CD_CURSO", curso.getCdCurso());
					rsm.setValueToField("NM_MODALIDADE_CURSO", curso.getNmProdutoServico());
				}
				
				Instituicao instituicao = InstituicaoDAO.get(rsm.getInt("CD_INSTITUICAO"), connect);
				if(instituicao != null){
					rsm.setValueToField("NM_INSTITUICAO", instituicao.getNmPessoa());
					
					InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(instituicao.getCdInstituicao(), rsm.getInt("cd_periodo_letivo"), connect);
					if(instituicaoEducacenso != null){
						rsm.setValueToField("TP_LOCALIZACAO", instituicaoEducacenso.getTpLocalizacao());
						rsm.setValueToField("NM_LOCALIZACAO", "Zona " + PessoaEnderecoServices.tiposZona[instituicaoEducacenso.getTpLocalizacao()]);
						
						if(instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_CRECHE){
							rsm.setValueToField("NM_LOCALIZACAO", "Creches");
							rsm.setValueToField("TP_LOCALIZACAO", 4);
						}
						
						if(rsm.getInt("TP_ZONA") == 0)
							rsm.setValueToField("TP_ZONA", 3);
						if(rsm.getInt("TP_LOCALIZACAO") == 0)
							rsm.setValueToField("TP_LOCALIZACAO", 3);
						
						if(instituicaoEducacenso.getTpLocalizacao() == PessoaEnderecoServices.TP_ZONA_RURAL && instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_ESCOLA){
							qtTurmasZonaRural += 1;
							qtTurmasMatriculadosZonaRural += qtMatriculas;
						}
						else if(instituicaoEducacenso.getTpLocalizacao() == PessoaEnderecoServices.TP_ZONA_URBANA && instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_ESCOLA){
							qtTurmasZonaUrbana += 1;
							qtTurmasMatriculadosZonaUrbana += qtMatriculas;
						}
						else if(instituicao.getTpInstituicao() == InstituicaoServices.TP_INSTITUICAO_CRECHE){
							qtTurmasCreche += 1;
							qtTurmasMatriculadosCreche += qtMatriculas;
						}
						
					}
				}
				
				rsm.setValueToField("NM_TURMA_ANTIGO", rsm.getString("NM_TURMA"));
				
				rsm.setValueToField("NM_TURMA",  rsm.getString("nm_etapa") + " - " + rsm.getString("NM_CURSO") + " - " + rsm.getString("CL_TURNO") + " - " + rsm.getString("nm_turma"));
				
				rsm.setValueToField("NM_ST_TURMA", situacoesTurma[rsm.getInt("ST_TURMA")]);
				
				rsm.setValueToField("SUB_TURMA", 0);
				
				if(rsm.getInt("LG_MULTI") == 1 && !semDetalhamentoTurma){
					
					int contagem = 0;
					int contagemPendentes = 0;
					
					ArrayList<Integer> codigosCurso = new ArrayList<Integer>();
					
					ResultSetMap rsmTurmasMulti = new ResultSetMap(connect.prepareStatement("SELECT COUNT(A.cd_matricula) AS nr_matriculados, B.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_matricula A, acd_curso B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND B.cd_curso = C.cd_produto_servico AND A.cd_turma = " + rsm.getInt("cd_turma") + " AND A.st_matricula IN ("+MatriculaServices.ST_ATIVA+", "+MatriculaServices.ST_CONCLUIDA+") GROUP BY B.cd_curso, C.nm_produto_servico").executeQuery());
					while(rsmTurmasMulti.next()){
						if(rsm.getInt("cd_curso") == rsmTurmasMulti.getInt("cd_curso")){
							continue;
						}
						
						codigosCurso.add(rsmTurmasMulti.getInt("cd_curso"));
						
						HashMap<String, Object> register = (HashMap<String, Object>)rsm.getRegister().clone();
						register.put("NM_TURMA", register.get("NM_TURMA") + " (" + rsmTurmasMulti.getString("nm_curso") + ")");
						register.put("NR_MATRICULADOS", rsmTurmasMulti.getInt("NR_MATRICULADOS"));
						contagem += rsmTurmasMulti.getInt("NR_MATRICULADOS");
						ResultSetMap rsmTurmasMultiPendentes = new ResultSetMap(connect.prepareStatement("SELECT COUNT(A.cd_matricula) AS nr_matriculados_pendentes FROM acd_matricula A, acd_curso B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND B.cd_curso = C.cd_produto_servico AND A.cd_turma = " + rsm.getInt("cd_turma") + " AND A.st_matricula = "+MatriculaServices.ST_PENDENTE+" AND A.cd_curso = " + rsmTurmasMulti.getInt("cd_curso")).executeQuery());
						if(rsmTurmasMultiPendentes.next()){
							register.put("NR_MATRICULADOS_PENDENTES", rsmTurmasMultiPendentes.getInt("NR_MATRICULADOS_PENDENTES"));
							contagemPendentes += rsmTurmasMultiPendentes.getInt("NR_MATRICULADOS_PENDENTES");
						}
						else{
							register.put("NR_MATRICULADOS_PENDENTES", 0);
						}
						
						qtMatriculas = rsmTurmasMulti.getInt("NR_MATRICULADOS");
						if(qtVagas > 0)
							register.put("PR_OCUPACAO", "" + ((qtMatriculas * 100) / qtVagas) + "%");
						else
							register.put("PR_OCUPACAO", "0%");
						
						register.put("SUB_TURMA", 1);
						rsm.addRegister(register);
					}
					
					rsmTurmasMulti = new ResultSetMap(connect.prepareStatement("SELECT COUNT(A.cd_matricula) AS nr_matriculados, B.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_matricula A, acd_curso B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND B.cd_curso = C.cd_produto_servico AND A.cd_turma = " + rsm.getInt("cd_turma") + " AND A.st_matricula = "+MatriculaServices.ST_PENDENTE+" GROUP BY B.cd_curso, C.nm_produto_servico").executeQuery());
					while(rsmTurmasMulti.next()){
						if(rsm.getInt("cd_curso") == rsmTurmasMulti.getInt("cd_curso") || codigosCurso.contains(rsmTurmasMulti.getInt("cd_curso"))){
							continue;
						}
						
						HashMap<String, Object> register = (HashMap<String, Object>)rsm.getRegister().clone();
						register.put("NM_TURMA", register.get("NM_TURMA") + " (" + rsmTurmasMulti.getString("nm_curso") + ")");
						register.put("NR_MATRICULADOS", 0);
						contagem += 0;
						ResultSetMap rsmTurmasMultiPendentes = new ResultSetMap(connect.prepareStatement("SELECT COUNT(A.cd_matricula) AS nr_matriculados_pendentes FROM acd_matricula A, acd_curso B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND B.cd_curso = C.cd_produto_servico AND A.cd_turma = " + rsm.getInt("cd_turma") + " AND A.st_matricula = "+MatriculaServices.ST_PENDENTE+" AND A.cd_curso = " + rsmTurmasMulti.getInt("cd_curso")).executeQuery());
						if(rsmTurmasMultiPendentes.next()){
							register.put("NR_MATRICULADOS_PENDENTES", rsmTurmasMultiPendentes.getInt("NR_MATRICULADOS_PENDENTES"));
							contagemPendentes += rsmTurmasMultiPendentes.getInt("NR_MATRICULADOS_PENDENTES");
						}
						else{
							register.put("NR_MATRICULADOS_PENDENTES", 0);
						}
						
						qtMatriculas = 0;
						if(qtVagas > 0)
							register.put("PR_OCUPACAO", "" + ((qtMatriculas * 100) / qtVagas) + "%");
						else
							register.put("PR_OCUPACAO", "0%");
						
						register.put("SUB_TURMA", 1);
						rsm.addRegister(register);
					}
					
					if(contagem < rsm.getInt("NR_MATRICULADOS")){
						HashMap<String, Object> register = (HashMap<String, Object>)rsm.getRegister().clone();
						register.put("NM_TURMA", register.get("NM_TURMA") + " (" + rsm.getString("NM_CURSO") + ")");
						register.put("NR_MATRICULADOS", (rsm.getInt("NR_MATRICULADOS") - contagem));
						
						if(contagemPendentes < rsm.getInt("NR_MATRICULADOS_PENDENTES")){
							register.put("NR_MATRICULADOS_PENDENTES", (rsm.getInt("NR_MATRICULADOS_PENDENTES") - contagemPendentes));
						}
						
						if(qtVagas > 0)
							register.put("PR_OCUPACAO", "" + (((rsm.getInt("NR_MATRICULADOS") - contagem) * 100) / qtVagas) + "%");
						else
							register.put("PR_OCUPACAO", "0%");
						
						register.put("SUB_TURMA", 1);
						rsm.addRegister(register);
					}
					
				}
				x++;
			}
			rsm.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			
			fields.add("TP_LOCALIZACAO");
			fields.add("NM_INSTITUICAO");
			fields.add("NM_MODALIDADE_CURSO");
			fields.add("CL_TURNO");
			fields.add("NM_TURMA");
			
			rsm.orderBy(fields);
			
			rsm.beforeFirst();
			Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsm);
			result.addObject("QT_TURMAS_ZONA_URBANA", qtTurmasZonaUrbana);
			result.addObject("QT_TURMAS_ZONA_RURAL", qtTurmasZonaRural);
			result.addObject("QT_TURMAS_CRECHE", qtTurmasCreche);
			result.addObject("QT_TURMAS_MATRICULADOS_ZONA_URBANA", qtTurmasMatriculadosZonaUrbana);
			result.addObject("QT_TURMAS_MATRICULADOS_ZONA_RURAL", qtTurmasMatriculadosZonaRural);
			result.addObject("QT_TURMAS_MATRICULADOS_CRECHE", qtTurmasMatriculadosCreche);
			return result;
			
		}
		catch(SQLException e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result findRelatorioVagas(ArrayList<ItemComparator> criterios) {
		return findRelatorioVagas(criterios, null);
	}

	public static Result findRelatorioVagas(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			boolean lgIncluirInativos = false;
			int cdPeriodoLetivoOrigem = 0;
			int cdInstituicao         = 0;
			int cdPeriodoLetivo       = 0;
			String sqlAdicional = null;
			int incluirTransferencia = 0;
			int tpApenasSemAlunos = -1;
			boolean semDetalhamentoTurma = true;
			int tpModalidade   = -1;
			
			for(int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO") && Integer.parseInt(criterios.get(i).getValue()) != ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0)) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgIncluirInativos")) {
					lgIncluirInativos = true;
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivoOrigem")) {
					cdPeriodoLetivoOrigem = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("sqlAdicional")) {
					sqlAdicional = criterios.get(i).getValue();
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("incluirTransferencia")) {
					incluirTransferencia = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivo")) {
					cdPeriodoLetivo = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpApenasSemAlunos")) {
					tpApenasSemAlunos = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("semDetalhamentoTurma")) {
					semDetalhamentoTurma = true;
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpModalidade")) {
					tpModalidade = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
			}
			if(cdPeriodoLetivoOrigem > 0){
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivoOrigem, connect);
				ArrayList<ItemComparator> criteriosInstituicaoPeriodo = new ArrayList<ItemComparator>();
				criteriosInstituicaoPeriodo.add(new ItemComparator("NM_PERIODO_LETIVO", instituicaoPeriodo.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
				criteriosInstituicaoPeriodo.add(new ItemComparator("CD_INSTITUICAO", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoDAO.find(criteriosInstituicaoPeriodo, connect);
				if(rsmPeriodoLetivo.next()){
					criterios.add(new ItemComparator("A.CD_PERIODO_LETIVO", rsmPeriodoLetivo.getString("cd_periodo_letivo"), ItemComparator.EQUAL, Types.INTEGER));
				}
			}
			 
			ResultSetMap rsm = Search.find(
				    "SELECT A.*, B.*, C.id_produto_servico, C.nm_produto_servico, I.cd_curso_etapa, (C.nm_produto_servico) AS nm_curso, D.nm_periodo_letivo, E.nm_matriz, H.nm_pessoa AS nm_instituicao, J.nm_etapa, ACD.tp_localizacao, INS.tp_instituicao "+
				    	    "FROM acd_turma A "+
				    	    "LEFT OUTER JOIN acd_curso           B ON (A.cd_curso = B.cd_curso) "+
				    	    "LEFT OUTER JOIN grl_produto_servico C ON (B.cd_curso = C.cd_produto_servico) "+
				    	    "LEFT OUTER JOIN acd_instituicao INS ON (A.cd_instituicao = INS.cd_instituicao) "+
				    	    "LEFT OUTER JOIN acd_instituicao_periodo D ON (A.cd_periodo_letivo = D.cd_periodo_letivo) "+
				    	    "LEFT OUTER JOIN acd_curso_matriz    E ON (A.cd_curso  = E.cd_curso "+
				    	    "           AND A.cd_matriz = E.cd_matriz) "+
				    	    "LEFT OUTER JOIN grl_pessoa H ON( A.cd_instituicao = H.cd_pessoa ) " +
				    	    "LEFT OUTER JOIN acd_instituicao_educacenso ACD ON( A.cd_instituicao = ACD.cd_instituicao AND D.cd_periodo_letivo = ACD.cd_periodo_letivo ) " +
				    	    "LEFT OUTER JOIN acd_curso_etapa        I ON (A.cd_curso = I.cd_curso) " +
				    	    "LEFT OUTER JOIN acd_tipo_etapa        J ON (I.cd_etapa = J.cd_etapa)"+
				    	    (incluirTransferencia == 1 ? "LEFT OUTER " : "") + " JOIN acd_instituicao_curso        L ON (A.cd_curso = L.cd_curso"+
				    	    "            AND A.cd_instituicao = L.cd_instituicao "+
				    	    "            AND A.cd_periodo_letivo = L.cd_periodo_letivo) "+
				    	    "LEFT OUTER JOIN acd_curso_multi     M ON (A.cd_curso = M.cd_curso) "+
				    	    "WHERE ACD.st_instituicao_publica = "+InstituicaoEducacensoServices.ST_EM_ATIVIDADE+" AND A.nm_turma NOT LIKE '%TRANS%' AND A.cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+")  " + (!lgIncluirInativos ? " AND st_turma <> " + ST_INATIVO : "") +
				    	    (sqlAdicional != null ? sqlAdicional : "") + 
				    	    (tpApenasSemAlunos == -1 ? "" : " AND " + (tpApenasSemAlunos == 1 ? " NOT " : "") + " EXISTS (SELECT MAT.cd_matricula FROM acd_matricula MAT WHERE MAT.cd_turma = A.cd_turma) ")+
				    	    (cdPeriodoLetivo > 0 ? " AND (A.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") " : ""), 
				    	    " GROUP BY A.cd_turma, B.cd_curso, C.id_produto_servico, I.cd_curso_etapa, J.nm_etapa, C.nm_produto_servico, "+
				    	    " D.nm_periodo_letivo, E.nm_matriz, H.nm_pessoa, D.dt_inicial, ACD.tp_localizacao, INS.tp_instituicao  " +
				    	    " ORDER BY D.dt_inicial DESC, I.cd_curso_etapa, J.nm_etapa, C.nm_produto_servico ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			int qtTurmasZonaUrbana = 0;
			int qtTurmasZonaRural = 0;
			int qtTurmasCreche = 0;
			int x = 0;
			while(rsm.next()){
				
				if(rsm.getInt("SUB_TURMA") == 1){
					continue;
				}
				
				
				if(tpModalidade == 1){
					boolean achado = false;
					ResultSetMap rsmCursosEducacaoInfantil 	= ParametroServices.getValoresOfParametro("CD_CURSO_EDUCACAO_INFANTIL");
					while(rsmCursosEducacaoInfantil.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosEducacaoInfantil.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					rsmCursosEducacaoInfantil.beforeFirst();
					if(!achado){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				else if(tpModalidade == 2){
					boolean achado = false;
					ResultSetMap rsmCursosFundamental1 = ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_1");
					while(rsmCursosFundamental1.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental1.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					if(!achado){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				else if(tpModalidade == 3){
					boolean achado = false;
					ResultSetMap rsmCursosFundamental2 = ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_2");
					while(rsmCursosFundamental2.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental2.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					
					if(!achado){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				
				int qtVagas = rsm.getInt("qt_vagas");

				if(rsm.getInt("tp_localizacao") > 0 && rsm.getInt("tp_localizacao") < 3)
					rsm.setValueToField("NM_LOCALIZACAO", InstituicaoEducacensoServices.tipoLocalizacao[(rsm.getInt("tp_localizacao")-1)]);
				
				if(rsm.getInt("tp_instituicao") == 1) {
					rsm.setValueToField("TP_LOCALIZACAO", 0);
					rsm.setValueToField("NM_LOCALIZACAO", "CRECHE");
				}
				
				rsm.setValueToField("CL_TURNO", (rsm.getInt("TP_TURNO") <= 5 && rsm.getInt("TP_TURNO") >= 0 ? tiposTurno[rsm.getInt("TP_TURNO")] : ""));
				rsm.setValueToField("CL_TP_MODALIDADE_ENSINO", tipoModalidade[rsm.getInt("TP_MODALIDADE_ENSINO")]);
				rsm.setValueToField("CL_ST_TURMA", situacoesTurma[rsm.getInt("ST_TURMA")]);
				
				rsm.setValueToField("NR_VAGAS", qtVagas);
				
				Curso curso = CursoDAO.get(rsm.getInt("CD_CURSO"), connect);
				if(curso != null){
					rsm.setValueToField("CD_CURSO", curso.getCdCurso());
					rsm.setValueToField("NM_MODALIDADE_CURSO", curso.getNmProdutoServico());
				}
				
				
				rsm.setValueToField("NM_TURMA_ANTIGO", rsm.getString("NM_TURMA"));
				
				rsm.setValueToField("NM_TURMA",  rsm.getString("nm_etapa") + " - " + rsm.getString("NM_CURSO") + " - " + rsm.getString("CL_TURNO") + " - " + rsm.getString("nm_turma"));
				
				rsm.setValueToField("NM_ST_TURMA", situacoesTurma[rsm.getInt("ST_TURMA")]);
				
				rsm.setValueToField("SUB_TURMA", 0);
				
				if(rsm.getInt("LG_MULTI") == 1 && !semDetalhamentoTurma){
					
					int contagem = 0;
					int contagemPendentes = 0;
					
					ArrayList<Integer> codigosCurso = new ArrayList<Integer>();
					
					ResultSetMap rsmTurmasMulti = new ResultSetMap(connect.prepareStatement("SELECT COUNT(A.cd_matricula) AS nr_matriculados, B.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_matricula A, acd_curso B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND B.cd_curso = C.cd_produto_servico AND A.cd_turma = " + rsm.getInt("cd_turma") + " AND A.st_matricula IN ("+MatriculaServices.ST_ATIVA+", "+MatriculaServices.ST_CONCLUIDA+") GROUP BY B.cd_curso, C.nm_produto_servico").executeQuery());
					while(rsmTurmasMulti.next()){
						if(rsm.getInt("cd_curso") == rsmTurmasMulti.getInt("cd_curso")){
							continue;
						}
						
						codigosCurso.add(rsmTurmasMulti.getInt("cd_curso"));
						
						HashMap<String, Object> register = (HashMap<String, Object>)rsm.getRegister().clone();
						register.put("NM_TURMA", register.get("NM_TURMA") + " (" + rsmTurmasMulti.getString("nm_curso") + ")");
						
						register.put("SUB_TURMA", 1);
						rsm.addRegister(register);
					}
					
					rsmTurmasMulti = new ResultSetMap(connect.prepareStatement("SELECT COUNT(A.cd_matricula) AS nr_matriculados, B.cd_curso, C.nm_produto_servico AS nm_curso FROM acd_matricula A, acd_curso B, grl_produto_servico C WHERE A.cd_curso = B.cd_curso AND B.cd_curso = C.cd_produto_servico AND A.cd_turma = " + rsm.getInt("cd_turma") + " AND A.st_matricula = "+MatriculaServices.ST_PENDENTE+" GROUP BY B.cd_curso, C.nm_produto_servico").executeQuery());
					while(rsmTurmasMulti.next()){
						if(rsm.getInt("cd_curso") == rsmTurmasMulti.getInt("cd_curso") || codigosCurso.contains(rsmTurmasMulti.getInt("cd_curso"))){
							continue;
						}
						
						HashMap<String, Object> register = (HashMap<String, Object>)rsm.getRegister().clone();
						register.put("NM_TURMA", register.get("NM_TURMA") + " (" + rsmTurmasMulti.getString("nm_curso") + ")");
						
						register.put("SUB_TURMA", 1);
						rsm.addRegister(register);
					}
				
					
				}
				x++;
			}
			rsm.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			
			fields.add("TP_LOCALIZACAO");
			fields.add("NM_INSTITUICAO");
			fields.add("NM_MODALIDADE_CURSO");
			fields.add("CL_TURNO");
			fields.add("NM_TURMA");
			
			rsm.orderBy(fields);
			
			rsm.beforeFirst();
			Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsm);
			result.addObject("QT_TURMAS_ZONA_URBANA", qtTurmasZonaUrbana);
			result.addObject("QT_TURMAS_ZONA_RURAL", qtTurmasZonaRural);
			result.addObject("QT_TURMAS_CRECHE", qtTurmasCreche);
			return result;
			
		}
		catch(SQLException e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static Result findRelatorioPorCirculo(ArrayList<ItemComparator> criterios) {
		return findRelatorioPorCirculo(criterios, null);
	}

	public static Result findRelatorioPorCirculo(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			
			boolean lgIncluirInativos = false;
			int cdPeriodoLetivoOrigem = 0;
			int cdInstituicao         = 0;
			int cdPeriodoLetivo       = 0;
			String sqlAdicional = null;
			int incluirTransferencia = 0;
			int tpApenasSemAlunos = -1;
			boolean semDetalhamentoTurma = false;
			int tpModalidade   = -1;
			
			for(int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO") && Integer.parseInt(criterios.get(i).getValue()) != ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0)) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgIncluirInativos")) {
					lgIncluirInativos = true;
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivoOrigem")) {
					cdPeriodoLetivoOrigem = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("sqlAdicional")) {
					sqlAdicional = criterios.get(i).getValue();
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("incluirTransferencia")) {
					incluirTransferencia = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivo")) {
					cdPeriodoLetivo = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpApenasSemAlunos")) {
					tpApenasSemAlunos = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("semDetalhamentoTurma")) {
					semDetalhamentoTurma = true;
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpModalidade")) {
					tpModalidade = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
			}
			if(cdPeriodoLetivoOrigem > 0){
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivoOrigem, connect);
				ArrayList<ItemComparator> criteriosInstituicaoPeriodo = new ArrayList<ItemComparator>();
				criteriosInstituicaoPeriodo.add(new ItemComparator("NM_PERIODO_LETIVO", instituicaoPeriodo.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
				criteriosInstituicaoPeriodo.add(new ItemComparator("CD_INSTITUICAO", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoDAO.find(criteriosInstituicaoPeriodo, connect);
				if(rsmPeriodoLetivo.next()){
					criterios.add(new ItemComparator("A.CD_PERIODO_LETIVO", rsmPeriodoLetivo.getString("cd_periodo_letivo"), ItemComparator.EQUAL, Types.INTEGER));
				}
			}
			 
			ResultSetMap rsm = Search.find(
				    "SELECT A.cd_turma, A.nm_turma, A.cd_instituicao, (C.nm_produto_servico) AS nm_curso, H.nm_pessoa AS nm_instituicao, PER.nm_periodo_letivo, INS.tp_instituicao, INS_EDU.tp_localizacao "+
				    	    "FROM acd_turma A "+
				    	    "LEFT OUTER JOIN grl_produto_servico C ON (A.cd_curso = C.cd_produto_servico) "+
				    	    "LEFT OUTER JOIN acd_instituicao_periodo D ON (A.cd_periodo_letivo = D.cd_periodo_letivo) "+
				    	    "LEFT OUTER JOIN grl_pessoa H ON( A.cd_instituicao = H.cd_pessoa ) " +
				    	    "LEFT OUTER JOIN acd_instituicao INS ON( A.cd_instituicao = INS.cd_instituicao ) " +
				    	    "LEFT OUTER JOIN acd_instituicao_periodo PER ON( A.cd_periodo_letivo = PER.cd_periodo_letivo ) " +
				    	    "LEFT OUTER JOIN acd_instituicao_educacenso INS_EDU ON( A.cd_instituicao = INS_EDU.cd_instituicao AND A.cd_periodo_letivo = INS_EDU.cd_periodo_letivo ) " +
				    	    "WHERE INS_EDU.st_instituicao_publica = "+InstituicaoEducacensoServices.ST_EM_ATIVIDADE+" AND A.nm_turma NOT LIKE '%TRANS%' AND A.cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+") " + (!lgIncluirInativos ? " AND st_turma <> " + ST_INATIVO : "") +
				    	    (sqlAdicional != null ? sqlAdicional : "") + 
				    	    (tpApenasSemAlunos == -1 ? "" : " AND " + (tpApenasSemAlunos == 1 ? " NOT " : "") + " EXISTS (SELECT MAT.cd_matricula FROM acd_matricula MAT WHERE MAT.cd_turma = A.cd_turma) ")+
				    	    (cdPeriodoLetivo > 0 ? " AND (A.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") " : ""), 
				    	    "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			ResultSetMap rsmFinal = new ResultSetMap();
			
			int totalUrbano = 0;
			int totalRural  = 0;
			int totalCreche = 0;
			
			int x = 0;
			while(rsm.next()){
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_instituicao", rsm.getString("cd_instituicao"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("tp_circulo", "0, 1", ItemComparator.IN, Types.INTEGER));
				ResultSetMap rsmInstituicaoCirculo = InstituicaoCirculoServices.find(criterios, connect);
				if(rsmInstituicaoCirculo.next()) {
					rsm.setValueToField("cd_circulo", rsmInstituicaoCirculo.getInt("cd_circulo"));
					rsm.setValueToField("nm_circulo", rsmInstituicaoCirculo.getString("nm_circulo"));
				}
				
				if(tpModalidade == 1){
					boolean achado = false;
					ResultSetMap rsmCursosEducacaoInfantil 	= ParametroServices.getValoresOfParametro("CD_CURSO_EDUCACAO_INFANTIL");
					while(rsmCursosEducacaoInfantil.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosEducacaoInfantil.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					rsmCursosEducacaoInfantil.beforeFirst();
					if(!achado){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				else if(tpModalidade == 2){
					boolean achado = false;
					ResultSetMap rsmCursosFundamental1 = ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_1");
					while(rsmCursosFundamental1.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental1.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					if(!achado){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				else if(tpModalidade == 3){
					boolean achado = false;
					ResultSetMap rsmCursosFundamental2 = ParametroServices.getValoresOfParametro("CD_CURSO_FUNDAMENTAL_2");
					while(rsmCursosFundamental2.next()){
						if(rsm.getInt("cd_curso") == Integer.parseInt(rsmCursosFundamental2.getString("VL_REAL"))){
							achado = true;
							break;
						}
								
					}
					
					if(!achado){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
				}
				
				int qtMatriculas = getAlunos(rsm.getInt("cd_turma"), false, false, true).getLines().size();
				
				
				if(rsm.getInt("tp_instituicao") == 1) {
					totalCreche+=qtMatriculas;					
					
					HashMap<String, Object> register = new HashMap<String, Object>();
					
					register.put("NR_ORDEM", 0);
					register.put("NM_LOCALIZACAO", "CRECHE");
					register.put("NM_PERIODO_LETIVO", rsm.getString("nm_periodo_letivo"));
					register.put("NM_INSTITUICAO", rsm.getString("NM_INSTITUICAO"));
					register.put("NM_CURSO", rsm.getString("NM_CURSO"));
					register.put("NM_TURMA", rsm.getString("NM_TURMA"));
					register.put("QT_MATRICULADOS", qtMatriculas);
					
					rsmFinal.addRegister(register);
				}
				else if(rsm.getInt("cd_circulo") == 0 && rsm.getInt("tp_localizacao") == InstituicaoEducacensoServices.TP_LOCALIZACAO_URBANA) {
					totalUrbano+=qtMatriculas;
					
					HashMap<String, Object> register = new HashMap<String, Object>();
					
					register.put("NR_ORDEM", 1);
					register.put("NM_LOCALIZACAO", "URBANA");
					register.put("NM_PERIODO_LETIVO", rsm.getString("nm_periodo_letivo"));
					register.put("NM_INSTITUICAO", rsm.getString("NM_INSTITUICAO"));
					register.put("NM_CURSO", rsm.getString("NM_CURSO"));
					register.put("NM_TURMA", rsm.getString("NM_TURMA"));
					register.put("QT_MATRICULADOS", qtMatriculas);
					
					rsmFinal.addRegister(register);
				}
				else {
					totalRural+=qtMatriculas;
					
					HashMap<String, Object> register = new HashMap<String, Object>();
					
					register.put("NR_ORDEM", 2);
					register.put("NM_LOCALIZACAO", (rsm.getString("NM_CIRCULO") != null ? rsm.getString("NM_CIRCULO") : "Sem círculo"));
					register.put("NM_PERIODO_LETIVO", rsm.getString("nm_periodo_letivo"));
					register.put("NM_INSTITUICAO", rsm.getString("NM_INSTITUICAO"));
					register.put("NM_CURSO", rsm.getString("NM_CURSO"));
					register.put("NM_TURMA", rsm.getString("NM_TURMA"));
					register.put("QT_MATRICULADOS", qtMatriculas);
					
					rsmFinal.addRegister(register);
				}
				
				
				x++;
			}
			rsmFinal.beforeFirst();
			
			ArrayList<String> fields = new ArrayList<String>();
			
			fields.add("NR_ORDEM");
			fields.add("NM_LOCALIZACAO");
			fields.add("NM_INSTITUICAO");
			fields.add("NM_CURSO");
			fields.add("NM_TURMA");
			
			rsmFinal.orderBy(fields);
			
			rsmFinal.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao pesquisar", "RSM", rsmFinal);
			result.addObject("QT_TURMAS_MATRICULADOS_ZONA_URBANA", totalUrbano);
			result.addObject("QT_TURMAS_MATRICULADOS_ZONA_RURAL", totalRural);
			result.addObject("QT_TURMAS_MATRICULADOS_CRECHE", totalCreche);
			return result;
			
		}
		catch(SQLException e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAtividadeComplementarOf(int cdTurma)	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_turma FROM acd_atividade_complementar A " +
					                                         " JOIN acd_turma_atividade_complementar B ON (B.cd_atividade_complementar = A.cd_atividade_complementar " +
					                                         "                                                  AND B.cd_turma = "+cdTurma+") " +
					                                         "ORDER BY cd_turma ASC, nm_atividade_complementar ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static Result addAtividadeComplementar(int cdTurma, int cdAtividadeComplementar)	{
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_turma_atividade_complementar " +
					                              	"WHERE cd_turma                  = "+cdTurma+
					                              	"  AND cd_atividade_complementar = "+cdAtividadeComplementar).executeQuery();
			if(rs.next())
				return new Result(-1, "ERRO: Esta atividade complementar já foi informado para essa turma!");
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_turma_atividade_complementar (cd_turma,"+
			                                  "cd_atividade_complementar) VALUES (?, ?)");
			pstmt.setInt(1, cdTurma);
			pstmt.setInt(2, cdAtividadeComplementar);
			return new Result(pstmt.executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaServices.addAtividadeComplementar: " +  e);
			return new Result(-1, "Erro ao tentar incluir atividade complementar na turma!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result deleteAtividadeComplementar(int cdTurma, int cdAtividadeComplementar){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return new Result(connect.prepareStatement("DELETE FROM acd_turma_atividade_complementar " +
					                              	   "WHERE cd_turma = "+cdTurma+
					                              	   "  AND cd_atividade_complementar = "+cdAtividadeComplementar).executeUpdate(), "Meio de comunicação excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir atividade complementar da turma!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAtendimentoEspecializadoOf(int cdTurma)	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.cd_turma FROM acd_atendimento_especializado A " +
					                                         " JOIN acd_turma_aee B ON (B.cd_atendimento_especializado = A.cd_atendimento_especializado " +
					                                         "                                                  AND B.cd_turma = "+cdTurma+") " +
					                                         "ORDER BY cd_turma ASC, nm_atendimento_especializado ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static Result addAtendimentoEspecializado(int cdTurma, int cdAtendimentoEspecializado)	{
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_turma_aee " +
					                              	"WHERE cd_turma                  = "+cdTurma+
					                              	"  AND cd_atendimento_especializado = "+cdAtendimentoEspecializado).executeQuery();
			if(rs.next())
				return new Result(-1, "ERRO: Esta atendimento especializado já foi informado para essa turma!");
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_turma_aee (cd_turma,"+
			                                  "cd_atendimento_especializado) VALUES (?, ?)");
			pstmt.setInt(1, cdTurma);
			pstmt.setInt(2, cdAtendimentoEspecializado);
			return new Result(pstmt.executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar incluir atendimento especializado na turma!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result deleteAtendimentoEspecializado(int cdTurma, int cdAtendimentoEspecializado){
		Connection connect = Conexao.conectar();
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return new Result(connect.prepareStatement("DELETE FROM acd_turma_aee " +
					                              	   "WHERE cd_turma = "+cdTurma+
					                              	   "  AND cd_atendimento_especializado = "+cdAtendimentoEspecializado).executeUpdate(), "Meio de comunicação excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-1, "Erro ao tentar excluir o atendimento especializado da turma!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	@Deprecated
	public static ResultSetMap getDisciplinasByTurma(int cdTurma)	{
		Connection connect = Conexao.conectar();
		try	{
			int cdCurso = 0;
			int cdCursoModulo = 0;
			int cdMatriz = 0;
			ResultSet rs = connect.prepareStatement("SELECT * FROM acd_turma where cd_turma = "+cdTurma).executeQuery();
			
			if(rs.next())	{
				cdCurso 	   = rs.getInt("cd_curso");
				cdCursoModulo = rs.getInt("cd_curso_modulo");
				cdMatriz       = rs.getInt("cd_matriz");
			}
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_produto_servico AS nm_disciplina, D.nm_pessoa AS nm_professor " +
					 "FROM acd_curso_disciplina A " +
                     "JOIN grl_produto_servico B ON (A.cd_disciplina = B.cd_produto_servico) " +
                     "LEFT OUTER JOIN acd_oferta C ON (A.cd_curso         = C.cd_curso" +
					 "                                        AND A.cd_matriz        = C.cd_matriz" +
					 "                                        AND A.cd_curso_modulo = C.cd_curso_modulo" +
					 "                                        AND A.cd_disciplina    = C.cd_disciplina) " +
					 "LEFT OUTER JOIN grl_pessoa D ON (C.cd_professor = D.cd_pessoa) " +
					 "WHERE A.cd_curso  = "+cdCurso+
					 "  AND C.cd_turma = "+cdTurma+
                     "  AND A.cd_matriz = "+cdMatriz+
                     (cdCursoModulo>0?" AND A.cd_curso_modulo = "+cdCursoModulo:"")).executeQuery());
			
			return rsm;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getOfertasByTurma(int cdTurma)	{
		Connection connect = Conexao.conectar();
		try	{
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT O.cd_oferta, O.cd_disciplina, B.nm_produto_servico AS nm_disciplina, C.nm_produto_servico AS nm_curso, D.nm_pessoa AS nm_professor " +
					 "FROM acd_oferta O " +
					 "LEFT OUTER JOIN grl_produto_servico B ON (O.cd_disciplina = B.cd_produto_servico) " +
                     "JOIN grl_produto_servico C ON (O.cd_curso = C.cd_produto_servico) " +
                     "LEFT OUTER JOIN grl_pessoa D ON (O.cd_professor = D.cd_pessoa) " +
                     "WHERE O.cd_turma = "+cdTurma+ " AND O.st_oferta = " + OfertaServices.ST_ATIVO).executeQuery());
			
			while(rsm.next()){
				ResultSetMap rsmProfessores = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_pessoa_oferta A, grl_pessoa B WHERE A.cd_pessoa = B.cd_pessoa AND cd_oferta = " + rsm.getInt("cd_oferta") + " AND st_pessoa_oferta = " + PessoaOfertaServices.ST_ATIVO).executeQuery());
				String nmProfessor = "";
				while(rsmProfessores.next()){
					nmProfessor += rsmProfessores.getString("nm_pessoa") + ", ";
				}
				
				if(nmProfessor.length() > 2)
					nmProfessor = nmProfessor.substring(0, nmProfessor.length()-2);
				
				rsm.setValueToField("NM_PROFESSOR", nmProfessor);
				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAlunosOf(int cdTurma)	{
		return getAlunosOf(cdTurma, false);
	}
	
	public static ResultSetMap getAlunosOf(int cdTurma, boolean lgSituacaoAluno)	{
		Connection connect = Conexao.conectar();
		try	{
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.nm_pessoa AS nm_aluno, PF.*, B.*, AL.*, C.*, CID.nm_cidade, PSC.nm_produto_servico AS nm_curso " +
					 "FROM acd_matricula A " +
                     "JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa) " +
                     "JOIN grl_pessoa_fisica PF ON (A.cd_aluno = PF.cd_pessoa) " +
                     "JOIN grl_pessoa_endereco C ON (A.cd_aluno = C.cd_pessoa AND C.lg_principal = 1) " +
                     "JOIN grl_cidade CID ON (C.cd_cidade = CID.cd_cidade) " +
                     "JOIN acd_aluno AL ON (A.cd_aluno = AL.cd_aluno) " +
                     "JOIN grl_produto_servico PSC ON (A.cd_curso = PSC.cd_produto_servico) " +
                     "WHERE A.cd_turma = "+cdTurma+ " AND A.ST_MATRICULA IN ("+MatriculaServices.ST_ATIVA+", "+MatriculaServices.ST_CONCLUIDA+ (lgSituacaoAluno ? ", "+MatriculaServices.ST_DESISTENTE+", "+MatriculaServices.ST_EVADIDO+", "+MatriculaServices.ST_TRANSFERIDO+", "+MatriculaServices.ST_FALECIDO : "") + ") ORDER BY B.nm_pessoa");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("TIPOSTRANSPORTE", TipoTransporteServices.getAll());
				rsm.setValueToField("CURSOS", CursoServices.getAllCursosOf(TurmaDAO.get(cdTurma, connect).getCdInstituicao(), connect));
				
				ResultSetMap rsmPessoaTipoTransporte = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula_tipo_transporte WHERE cd_matricula = " + rsm.getInt("cd_matricula")).executeQuery());
				if(rsmPessoaTipoTransporte.next()){
					rsm.setValueToField("CD_TIPO_TRANSPORTE_PRINCIPAL", rsmPessoaTipoTransporte.getInt("cd_tipo_transporte"));
				}
			
				rsm.setValueToField("TOOL_TIP_CEP", "Após corrigir o CEP, pressione a tecla TAB para salvá-lo");
				
				rsm.setValueToField("NM_DT_NASCIMENTO", Util.formatDate(rsm.getGregorianCalendar("dt_nascimento"), "dd/MM/yyyy"));
				rsm.setValueToField("NM_ZONA", PessoaEnderecoServices.tiposZona[rsm.getInt("TP_ZONA")]);
				
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsm.getInt("cd_periodo_letivo"), connect);
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("nm_periodo_letivo", "" + String.valueOf(Integer.parseInt(instituicaoPeriodo.getNmPeriodoLetivo()) + 1), ItemComparator.EQUAL, Types.VARCHAR));
				criterios.add(new ItemComparator("cd_instituicao", "" + ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmPeriodoPosterior = InstituicaoPeriodoDAO.find(criterios, connect);
				InstituicaoPeriodo instituicaoPeriodoSuperior = null;
				if(rsmPeriodoPosterior.next())
					instituicaoPeriodoSuperior = InstituicaoPeriodoDAO.get(rsmPeriodoPosterior.getInt("cd_periodo_letivo"), connect);
				
				if(instituicaoPeriodoSuperior != null){
					ResultSetMap rsmMatriculaPeriodoPosterior = new ResultSetMap(connect.prepareStatement("SELECT A.cd_matricula, C.nm_turma, D.nm_produto_servico AS nm_curso, B.nm_periodo_letivo FROM acd_matricula A "
							+ "																				 JOIN acd_instituicao_periodo B ON (A.cd_periodo_letivo = B.cd_periodo_letivo) "
							+ "																				 JOIN acd_turma C ON (A.cd_turma = C.cd_turma) "
							+ "																				 JOIN grl_produto_servico D ON (A.cd_curso = D.cd_produto_servico) "
							+ "																				WHERE A.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", "+ MatriculaServices.ST_PENDENTE + ", "
							+ 																										     MatriculaServices.ST_EM_TRANSFERENCIA + ", "+ MatriculaServices.ST_TRANSFERIDO + ", "
							+ 																										     MatriculaServices.ST_CONCLUIDA + ")"
							+ "																		  		  AND B.cd_periodo_letivo_superior = " + instituicaoPeriodoSuperior.getCdPeriodoLetivo() 
							+ "																		  		  AND A.cd_aluno = " + rsm.getInt("cd_aluno")).executeQuery());
					if(rsmMatriculaPeriodoPosterior.next()){
						rsm.setValueToField("LG_PROGREDIDO", "Sim (Matriculado na turma "+rsmMatriculaPeriodoPosterior.getString("nm_curso")+" - "+rsmMatriculaPeriodoPosterior.getString("nm_turma")+" no periodo letivo de: "+rsmMatriculaPeriodoPosterior.getString("nm_periodo_letivo")+")");
					}
					else{
						rsm.setValueToField("LG_PROGREDIDO", "Não");
					}
				}
				else{
					rsm.setValueToField("LG_PROGREDIDO", "Último período letivo");
				}
				
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}	
	
	/**
	 * Método que busca todos os alunos ativos da disciplina/turma
	 * @param cdTurma
	 * @param cdDisciplina
	 * @return rsmAlunos
	 */
	public static ResultSetMap getAlunosByTurmaDisciplina(int cdTurma, int cdDisciplina) {
		return getAlunosByTurmaDisciplina(cdTurma, cdDisciplina, null);
	}

	public static ResultSetMap getAlunosByTurmaDisciplina(int cdTurma, int cdDisciplina, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {

			pstmt = connect.prepareStatement("SELECT A.nr_matricula, B.nm_pessoa AS nm_aluno, C.cd_matricula_disciplina," +
									" E.cd_disciplina, E.cd_professor, E.cd_curso, C.lg_aprovado, BPF.dt_nascimento" +
									" FROM acd_matricula A" +
									" JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa)" +
									" JOIN grl_pessoa_fisica BPF ON (B.cd_pessoa = BPF.cd_pessoa)" +
									" JOIN acd_matricula_disciplina C ON (A.cd_matricula = C.cd_matricula AND C.cd_disciplina = "+ cdDisciplina +"), acd_oferta E" +
									" WHERE A.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + ")" +
									"   AND A.cd_turma = " + cdTurma +
									"   AND E.cd_turma = " + cdTurma +
									"   AND E.cd_disciplina = " + cdDisciplina +
									" ORDER BY B.nm_pessoa");
			
			ResultSetMap rsmAlunos = new ResultSetMap(pstmt.executeQuery());
			
			rsmAlunos.beforeFirst();
			return rsmAlunos;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaServices.getAlunosByTurmaDisciplina: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaServices.getAlunosByTurmaDisciplina: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Método que busca todos os alunos da disciplina/turma com suas presenças e observacoes
	 * @param cdTurma
	 * @param cdDisciplina
	 * @param cdAula
	 * @return rsmAlunos
	 */
	public static ResultSetMap getAlunosComQtdFaltas(int cdTurma, int cdDisciplina, int cdOferta) {
		return getAlunosComQtdFaltas(cdTurma, cdDisciplina, cdOferta, null);
	}

	public static ResultSetMap getAlunosComQtdFaltas(int cdTurma, int cdDisciplina, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//Busca dos alunos
			ResultSetMap rsmAlunos = TurmaServices.getAlunosByTurmaDisciplina(cdTurma, cdDisciplina, connect);
			
			//Busca da quantidade de faltas
			while(rsmAlunos.next()){

				pstmt = connect.prepareStatement("SELECT count(*) AS qtd_presenca FROM acd_aula_matricula" +
												" WHERE cd_matricula_disciplina = " + rsmAlunos.getInt("CD_MATRICULA_DISCIPLINA") +
												" AND lg_presenca = 1" +
												" AND cd_aula IN (SELECT cd_aula FROM acd_aula " +
																" WHERE cd_oferta = " + cdOferta +
																" AND st_aula = " + AulaServices.ST_FECHADA + ")");

				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				
				if (rsm.next()) {
					rsmAlunos.setValueToField("NR_FALTAS_ACUMULADAS", AulaServices.getQtdAulasRealizadasByOferta(cdOferta) - rsm.getInt("QTD_PRESENCA"));
					
					GregorianCalendar dtNascimento = rsmAlunos.getGregorianCalendar("DT_NASCIMENTO");
					if(dtNascimento != null)
						
						rsmAlunos.setValueToField("DT_NM_NASCIMENTO", (Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.DAY_OF_MONTH))) > 9 ? dtNascimento.get(Calendar.DAY_OF_MONTH) : "0" + dtNascimento.get(Calendar.DAY_OF_MONTH)) + "/" + 
																(Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1 > 9 ? 
																(Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1) : "0" + 
																(Integer.parseInt(String.valueOf(dtNascimento.get(Calendar.MONTH))) + 1)) + "/" + dtNascimento.get(Calendar.YEAR));
				}
					
			}
			
			rsmAlunos.beforeFirst();
			return rsmAlunos;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TurmaServices.getAlunosComQtdFaltas: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaServices.getAlunosComQtdFaltas: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAlunosAtivosOf(int cdTurma)	{
		return getAlunosAtivosOf(cdTurma, null);
	}
	
	public static ResultSetMap getAlunosAtivosOf(int cdTurma, Connection connect)	{
		boolean isConnectionNull = connect == null;
		try	{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_pessoa AS nm_aluno " +
															 " FROM acd_matricula A " +
					                                         " JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa) " +
					                                         " WHERE A.st_matricula = "+MatriculaServices.ST_ATIVA+
					                                         "   AND A.cd_turma = "+cdTurma+
					                                         " ORDER BY B.nm_pessoa").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTiposAtendimentos(int cdTurma)	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT A.* FROM acd_atendimento_especializado A " +
					                                         "JOIN acd_turma_aee B ON (B.cd_atendimento_especializado = A.cd_atendimento_especializado " +
					                                         "AND B.cd_turma = "+cdTurma+") " +
					                                         "ORDER BY cd_atendimento_especializado ASC, nm_atendimento_especializado ").executeQuery());
		}
		
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getTiposAtividades(int cdTurma)	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT A.* FROM acd_atividade_complementar A " +
					                                         "JOIN acd_turma_atividade_complementar B ON (B.cd_atividade_complementar = A.cd_atividade_complementar " +
					                                         "AND B.cd_turma = "+cdTurma+") " +
					                                         "ORDER BY cd_atividade_complementar ASC, nm_atividade_complementar ").executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDisciplinas(int cdTurma)	{
		return getDisciplinas(cdTurma, -1);
	}
	
	public static ResultSetMap getDisciplinas(int cdTurma, int stDisciplina)	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, D.nm_produto_servico AS nm_disciplina, D.id_produto_servico FROM acd_disciplina A " +
											                    "JOIN grl_produto_servico D ON (A.cd_disciplina = D.cd_produto_servico) " +
											                    "WHERE EXISTS (SELECT * FROM acd_oferta O WHERE O.cd_disciplina = A.cd_disciplina AND O.cd_turma = "+cdTurma+" AND O.st_oferta = "+OfertaServices.ST_ATIVO+")" +
											                    (stDisciplina > 0 ? " AND tp_produto_servico = " + stDisciplina : "")).executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getProfissionais(int cdTurma)	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.*, C.* FROM acd_pessoa_oferta A " +
											                    "JOIN grl_pessoa B ON (A.cd_pessoa = B.cd_pessoa ) " +
											                    "JOIN acd_professor C ON (A.cd_pessoa = C.cd_professor ) " +
											                    "JOIN acd_oferta D ON (A.cd_oferta = D.cd_oferta ) " +
											                    "JOIN acd_turma E ON (D.cd_turma = E.cd_turma) " +
											                    "WHERE E.cd_turma = "+cdTurma+
											                    "  AND A.st_pessoa_oferta = " + PessoaOfertaServices.ST_ATIVO + 
											                    "  AND D.st_oferta = " + OfertaServices.ST_ATIVO + 
											                    "  AND E.st_turma = " + TurmaServices.ST_ATIVO).executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getHorariosByTurma(int cdTurma)	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_turma_horario A " +
											                    "WHERE cd_turma = "+cdTurma).executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getHorarios(int cdTurma, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			return new ResultSetMap(connect.prepareStatement("SELECT A.* FROM acd_oferta_horario A " +
											                    "JOIN acd_oferta B ON (A.cd_oferta = B.cd_oferta ) " +
											                    "WHERE B.cd_turma = "+cdTurma +
											                    "  AND B.cd_periodo_letivo = " + cdPeriodoLetivo).executeQuery());
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getAlunos(int cdTurma)	{
		return getAlunos(cdTurma, false, null);
	}
	
	public static ResultSetMap getAlunos(int cdTurma, Connection connect)	{
		return getAlunos(cdTurma, false, connect);
	}
	public static ResultSetMap getAlunos(int cdTurma, boolean onlyAtivos)	{
		return getAlunos(cdTurma, onlyAtivos, null);
	}
	
	public static ResultSetMap getAlunos(int cdTurma, boolean onlyAtivos, Connection connect)	{
		return getAlunos(cdTurma, onlyAtivos, false, false, connect);
	}
	
	public static ResultSetMap getAlunos(int cdTurma, boolean onlyAtivos, boolean onlyPendentes)	{
		return getAlunos(cdTurma, onlyAtivos, onlyPendentes, false, null);
	}
	
	public static ResultSetMap getAlunos(int cdTurma, boolean onlyAtivos, boolean onlyPendentes, boolean onlyAtivosConcluidos)	{
		return getAlunos(cdTurma, onlyAtivos, onlyPendentes, onlyAtivosConcluidos, null);
	}
	
	public static ResultSetMap getAlunos(int cdTurma, boolean onlyAtivos, boolean onlyPendentes, Connection connect)	{
		return getAlunos(cdTurma, onlyAtivos, onlyPendentes, false, connect);
	}
	
	public static ResultSetMap getAlunos(int cdTurma, boolean onlyAtivos, boolean onlyPendentes, boolean onlyAtivosConcluidos, Connection connect)	{
		boolean isConnectionNull = connect == null;
		try	{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, A2.nm_pessoa, B.*, C.*, E.nm_produto_servico AS nm_curso from grl_pessoa_fisica A "+ 
					"JOIN grl_pessoa A2 ON ( A.cd_pessoa = A2.cd_pessoa ) "+
					"JOIN acd_aluno B ON ( A. cd_pessoa = B.cd_aluno ) "+
					"JOIN acd_matricula C ON ( B.cd_aluno = C.cd_aluno ) " +
					"JOIN acd_turma D ON ( D.cd_turma = C.cd_turma ) " +
					"JOIN grl_produto_servico E ON (C.cd_curso = E.cd_produto_servico) " +
                    "WHERE D.cd_turma = "+cdTurma+
                    (onlyAtivosConcluidos ? "  AND (C.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + ")) " :
                    (onlyAtivos ? "  AND (C.st_matricula = " + MatriculaServices.ST_ATIVA + ") " : 
                    (onlyPendentes ? "  AND (C.st_matricula = " + MatriculaServices.ST_PENDENTE + ") " : "  AND (C.st_matricula = " + MatriculaServices.ST_ATIVA + 
                    "  OR C.st_matricula = " + MatriculaServices.ST_CONCLUIDA + 
                    "  OR C.st_matricula = " + MatriculaServices.ST_PENDENTE + ")" )))+
                    " ORDER BY A2.nm_pessoa");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("NM_ST_MATRICULA", MatriculaServices.situacao[rsm.getInt("ST_MATRICULA")]);
				rsm.setValueToField("NM_DT_NASCIMENTO_COMPLETO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")) + " ("+Util.getIdade(rsm.getGregorianCalendar("dt_nascimento"), 31, 2, Integer.parseInt(InstituicaoPeriodoDAO.get(rsm.getInt("cd_periodo_letivo"), connect).getNmPeriodoLetivo()))+" anos)");
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e)	{
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getListaTurma(int cdTurma)	{
		return getListaTurma(cdTurma, true, false, false, null);
	}
	
	public static Result getListaTurma(int cdTurma, Connection connect)	{
		return getListaTurma(cdTurma, true, false, false, connect);
	}
		
	public static Result getListaTurma(int cdTurma, boolean onlyAtivos, boolean onlyPendentes, boolean onlyAtivosConcluidos)	{
		return getListaTurma(cdTurma, onlyAtivos, onlyPendentes, onlyAtivosConcluidos, null);
	}
	
	public static Result getListaTurma(int cdTurma, boolean onlyAtivos, boolean onlyPendentes, boolean onlyAtivosConcluidos, Connection connect)	{
		boolean isConnectionNull = connect == null;
		try	{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsm = getAlunos(cdTurma, onlyAtivos, onlyPendentes, onlyAtivosConcluidos, connect);
			
			Turma turma = TurmaDAO.get(cdTurma, connect);
			
			String txtOfertas = "Disciplinas/Professores:\n\n";
			if(CursoServices.isCreche(turma.getCdCurso(), connect) || CursoServices.isEducacaoInfantil(turma.getCdCurso(), connect)){
				txtOfertas = "Professores/Auxiliares:\n\n";
			}
			
			ResultSetMap rsmOfertas = OfertaServices.getAllByTurma(cdTurma, connect);
			while(rsmOfertas.next()){
				txtOfertas += (rsmOfertas.getString("NM_DISCIPLINA") != null && !rsmOfertas.getString("NM_DISCIPLINA").equals("") ? rsmOfertas.getString("NM_DISCIPLINA") + " - " : "") + rsmOfertas.getString("NM_PROFESSOR") + "\n";
			}
			rsmOfertas.beforeFirst();
			
			Result result = new Result(1, "Lista bscada com sucesso");
			result.addObject("rsm", rsm);
			result.addObject("txtOfertas", txtOfertas);
			
			return result;
		}
		catch(Exception e)	{
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAlunosSimplificado(int cdTurma)	{
		return getAlunosSimplificado(cdTurma, null);
	}
	
	public static ResultSetMap getAlunosSimplificado(int cdTurma, Connection connect)	{
		boolean isConnectionNull = connect == null;
		try	{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * from acd_matricula " +
					"WHERE cd_turma = "+cdTurma+
                    "  AND st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + ")");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e)	{
			if(isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String gerarIdTurma(int cdTurma, Connection connect){
		boolean isConnectionNull = connect == null;
		try	{
			
			String idTurma = "";
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Turma turma = TurmaDAO.get(cdTurma, connect);
			
			CursoMatriz cursoMatriz = CursoMatrizDAO.get(turma.getCdMatriz(), turma.getCdCurso(), connect);
			
			Curso curso = CursoDAO.get(turma.getCdCurso(), connect);
			
			if(cursoMatriz != null && cursoMatriz.getNmMatriz() != null)
				idTurma += cursoMatriz.getNmMatriz();
			if(turma.getTpTurno() >= 0)
				idTurma +=  tiposTurno[turma.getTpTurno()].substring(0,2).toUpperCase();
			if(curso != null && curso.getIdProdutoServico() != null)
				idTurma += curso.getIdProdutoServico();
			
			return idTurma;
			
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally	{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result gerarRelatorioTurmas(int cdEmpresa, int cdCurso, int cdPeriodoLetivo, int tpTurno, int cdDisciplina, int cdAluno, int tpModalidadeEnsino){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
//			criterios.add(new ItemComparator("M.cd_cidade", "" + cdCidade, ItemComparator.EQUAL, Types.INTEGER));
//			criterios.add(new ItemComparator("N.tp_localizacao", "" + tpLocalizacao, ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = find(criterios, connection);

			while(rsm.next()){
//				rsm.setValueToField("DS_ENDERECO", rsm.getString("NM_LOGRADOURO") + ", " + (rsm.getString("NR_ENDERECO") != null && !rsm.getString("NR_ENDERECO").trim().equals("") ? rsm.getString("NR_ENDERECO") + ", " : "") + (rsm.getString("NM_BAIRRO") != null && !rsm.getString("NM_BAIRRO").trim().equals("") ? rsm.getString("NM_BAIRRO") : ""));
//				rsm.setValueToField("CL_ST_INSTITUICAO", InstituicaoEducacensoServices.situacaoInstituicao[rsm.getInt("ST_INSTITUICAO_PUBLICA")]);
			}
			
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_INSTITUICAO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
//	public static ResultSetMap getTurmasByCurso(int cdCurso, int cdMatriz)	{
//		Connection connect = Conexao.conectar();
//		try	{
//			return new ResultSetMap(connect.prepareStatement("SELECT A.* FROM acd_turma A " +
//															 "WHERE A.cd_curso = " + cdCurso + " A.cdMatriz = " + cdMatriz ).executeQuery());
//		}
//		catch(Exception e)	{
//			e.printStackTrace(System.out);
//			return null;
//		}
//		finally	{
//			Conexao.desconectar(connect);
//		}
//	}
	
	/**
	 * Troca o aluno para outra turma da mesma instituição, no mesmo período letivo, e que seja de turma equivalente.
	 * 
	 * @param cdAluno código de aluno
	 * @param cdTurma código da turma de origem
	 * @param cdTurmaDestino código da turma de destino
	 * @param cdPeriodoLetivo código do período letivo
	 * @return
	 * 
	 * @author Maurício
	 * @since 23/10/2015
	 */
	public static Result remanejarAluno(int cdAluno, int cdTurma, int cdTurmaDestino, int cdPeriodoLetivo, int cdUsuario){
		return remanejarAluno(cdAluno, cdTurma, cdTurmaDestino, cdPeriodoLetivo, cdUsuario, null);
	}
	
	public static Result remanejarAluno(int cdAluno, int cdTurma, int cdTurmaDestino, int cdPeriodoLetivo, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			// MATRICULA
			Matricula matricula = MatriculaServices.getMatricula(cdAluno, cdTurma, cdPeriodoLetivo, connect);
			matricula.setCdTurma(cdTurmaDestino);
			Turma turmaDestino = TurmaDAO.get(cdTurmaDestino, connect);
			Curso cursoTurmaDestino = CursoDAO.get(turmaDestino.getCdCurso(), connect);
			matricula.setCdCurso(CursoServices.getCursoCompativel(turmaDestino, matricula, connect));
			Curso cursoMatriculaDestino = CursoDAO.get(matricula.getCdCurso(), connect);
			
			
			if(!verificarCompatibilidadeTurmaMatricula(cursoTurmaDestino, cursoMatriculaDestino, connect)){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Turma não compatível com a matrícula");
			}
			
			Result result = MatriculaServices.save(matricula, cdUsuario, true, true, connect);
			
			if(matricula.getStMatricula() != MatriculaServices.ST_ATIVA && matricula.getStMatricula() != MatriculaServices.ST_PENDENTE){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Remanejamentos não podem ser realizados para matrículas que não estejam Ativas");
			}
			
			//TODO: OFERTA
			
			if(result.getCode()<=0 && result.getCode() != MatriculaServices.ERR_ALUNO_FORA_IDADE) {
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return result;
			}
			
			Turma turma = TurmaDAO.get(cdTurma, connect);
			Curso cursoTurma = CursoDAO.get(turma.getCdCurso(), connect);
			
			// OCORRÊNCIA
			int cdTipoOcorrenciaRemanejamento = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REMANEJAMENTO_ALUNO, connect).getCdTipoOcorrencia();
			OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, cdAluno, 
					"Remanejamento do aluno da turma "+cursoTurma.getNmProdutoServico() + " - " + turma.getNmTurma()+" para a turma "+cursoTurmaDestino.getNmProdutoServico() + " - " + turmaDestino.getNmTurma(),//txtDescricao, 
					Util.getDataAtual(), cdTipoOcorrenciaRemanejamento, 
					OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matricula.getCdMatricula(), matricula.getCdMatricula(), cdTurma, cdTurmaDestino, matricula.getStMatricula(), cdUsuario);
			OcorrenciaMatriculaServices.save(ocorrencia, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Remanejamento realizado com sucesso.");
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao remanejar alunos.");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result trocarAluno(int cdAlunoTroca, int cdTurmaTroca, int cdAlunoTrocado, int cdTurmaTrocado, int cdUsuario){
		return trocarAluno(cdAlunoTroca, cdTurmaTroca, cdAlunoTrocado, cdTurmaTrocado, cdUsuario, null);
	}
	
	public static Result trocarAluno(int cdAlunoTroca, int cdTurmaTroca, int cdAlunoTrocado, int cdTurmaTrocado, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			// MATRICULA DE TROCA
			Turma turmaTroca = TurmaDAO.get(cdTurmaTroca, connect);
			Matricula matriculaTroca = MatriculaServices.getMatricula(cdAlunoTroca, cdTurmaTroca, turmaTroca.getCdPeriodoLetivo(), connect);
			matriculaTroca.setCdTurma(cdTurmaTrocado);
			
			// MATRICULA DE TROCADA
			Turma turmaTrocado = TurmaDAO.get(cdTurmaTrocado, connect);
			Matricula matriculaTrocado = MatriculaServices.getMatricula(cdAlunoTrocado, cdTurmaTrocado, turmaTrocado.getCdPeriodoLetivo(), connect);
			matriculaTrocado.setCdTurma(cdTurmaTroca);
			
			Curso cursoTurmaTroca = CursoDAO.get(turmaTroca.getCdCurso(), connect);
			Curso cursoTurmaTrocado = CursoDAO.get(turmaTrocado.getCdCurso(), connect);
			
			matriculaTroca.setCdCurso(CursoServices.getCursoCompativel(turmaTrocado, matriculaTroca, connect));
			matriculaTrocado.setCdCurso(CursoServices.getCursoCompativel(turmaTroca, matriculaTrocado, connect));
			
			Curso cursoMatriculaTroca = CursoDAO.get(matriculaTroca.getCdCurso(), connect);
			Curso cursoMatriculaTrocado = CursoDAO.get(matriculaTrocado.getCdCurso(), connect);
			
			
			if(!verificarCompatibilidadeTurmaMatricula(cursoTurmaTroca, cursoMatriculaTroca, connect)){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Turma de troca não compatível com a matrícula");
			}
			
			if(!verificarCompatibilidadeTurmaMatricula(cursoTurmaTrocado, cursoMatriculaTrocado, connect)){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Turma trocada não compatível com a matrícula");
			}
			
			//Salvamento da matricula de troca
			int result = MatriculaDAO.update(matriculaTroca, connect);
			
			if(matriculaTroca.getStMatricula() != MatriculaServices.ST_ATIVA && matriculaTroca.getStMatricula() != MatriculaServices.ST_PENDENTE){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Remanejamentos não podem ser realizados para matrículas que não estejam Ativas");
			}
			
			if(result<=0) {
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(result, "Erro ao tentar trocar aluno");
			}
			
			int cdTipoOcorrenciaRemanejamento = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REMANEJAMENTO_ALUNO, connect).getCdTipoOcorrencia();
			OcorrenciaMatricula ocorrencia = new OcorrenciaMatricula(0, cdAlunoTroca, 
					"Remanejamento do aluno da turma "+cursoTurmaTroca.getNmProdutoServico() + " - " + turmaTroca.getNmTurma()+" para a turma "+cursoTurmaTrocado.getNmProdutoServico() + " - " + turmaTrocado.getNmTurma(),//txtDescricao, 
					Util.getDataAtual(), cdTipoOcorrenciaRemanejamento, 
					OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matriculaTroca.getCdMatricula(), matriculaTroca.getCdMatricula(), cdTurmaTroca, cdTurmaTrocado, matriculaTroca.getStMatricula(), cdUsuario);
			OcorrenciaMatriculaServices.save(ocorrencia, connect);
			
			
			//Salvamento da matricula trocada
			result = MatriculaDAO.update(matriculaTrocado, connect);
			
			if(matriculaTrocado.getStMatricula() != MatriculaServices.ST_ATIVA && matriculaTrocado.getStMatricula() != MatriculaServices.ST_PENDENTE){
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(-1, "Remanejamentos não podem ser realizados para matrículas que não estejam Ativas");
			}
			
			if(result<0) {
				if(isConnectionNull){
					Conexao.rollback(connect);
				}
				return new Result(result, "Erro ao tentar trocar aluno");
			}
			
			cdTipoOcorrenciaRemanejamento = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_REMANEJAMENTO_ALUNO, connect).getCdTipoOcorrencia();
			ocorrencia = new OcorrenciaMatricula(0, cdAlunoTrocado, 
					"Remanejamento do aluno da turma "+cursoTurmaTrocado.getNmProdutoServico() + " - " + turmaTrocado.getNmTurma()+" para a turma "+cursoTurmaTroca.getNmProdutoServico() + " - " + turmaTroca.getNmTurma(),//txtDescricao, 
					Util.getDataAtual(), cdTipoOcorrenciaRemanejamento, 
					OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, matriculaTrocado.getCdMatricula(), matriculaTrocado.getCdMatricula(), cdTurmaTrocado, cdTurmaTroca, matriculaTrocado.getStMatricula(), cdUsuario);
			OcorrenciaMatriculaServices.save(ocorrencia, connect);
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Remanejamento realizado com sucesso.");
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao remanejar alunos.");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Remaneja uma lista de alunos para outra turma da mesma instituição, no mesmo período letivo.
	 * 
	 * @param alunos ArrayList<Integer> Códigos dos alunos
	 * @param cdTurma turma de origem
	 * @param cdTurmaDestino turma destino
	 * @param cdPeriodoLetivo período letivo
	 * @param cdUsuario usuário que está realizando o remanejamento
	 * @return
	 * 
	 * @author Maurício
	 * @since 23/10/2015
	 * 
	 */
	public static Result remanejarAlunos(ArrayList<Integer> matriculas, int cdTurma, int cdTurmaDestino, int cdPeriodoLetivo, int cdUsuario){
		return remanejarAlunos(matriculas, cdTurma, cdTurmaDestino, cdPeriodoLetivo, cdUsuario, null);
	}
	
	public static Result remanejarAlunos(ArrayList<Integer> matriculas, int cdTurma, int cdTurmaDestino, int cdPeriodoLetivo, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			// VERIFICAR SE EXISTEM VAGAS SUFICIENTES
//			int qtAlunos = alunos.size();
//			Turma turmaDestino = TurmaDAO.get(cdTurmaDestino, connect);
//			int qtLivres = turmaDestino.getQtVagas() - getAlunos(cdTurmaDestino).size();
//			if(qtLivres==0) {
//				return new Result(-2, "Não há vagas na turma selecionada.");
//			}
//			else if(qtAlunos>qtLivres) {
//				return new Result(-2, "Existe(m) apenas "+qtLivres+" vaga(s) na turma selecionada.");
//			}
			
			for (Integer cdMatricula : matriculas) {
				Matricula matricula = MatriculaDAO.get(cdMatricula, connect);
				Result result = remanejarAluno(matricula.getCdAluno(), cdTurma, cdTurmaDestino, cdPeriodoLetivo, cdUsuario, connect);
				
				if(result.getCode()<=0) {
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Remanejamento realizado com sucesso.");
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao remanejar alunos.");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result validarTurmasProjecaoVagas(int cdInstituicao, int cdPeriodoLetivo, int cdUsuario){
		return validarTurmasProjecaoVagas(cdInstituicao, cdPeriodoLetivo, cdUsuario, null);
	}
	
	public static Result validarTurmasProjecaoVagas(int cdInstituicao, int cdPeriodoLetivo, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(cdInstituicao == ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0)){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(1, "Escolha uma escola para poder validar");
			}
			
			String txtErro = "";
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM acd_quadro_vagas A " +
																"  JOIN acd_instituicao_periodo B ON (A.cd_periodo_letivo = B.cd_periodo_letivo) " + 
																"  WHERE A.cd_instituicao = "+cdInstituicao+" AND (B.cd_periodo_letivo = "+cdPeriodoLetivo+" OR B.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(rsm.next()){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_quadro_vagas", rsm.getString("cd_quadro_vagas"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmQuadroVagasCurso = QuadroVagasCursoDAO.find(criterios, connect);
				while(rsmQuadroVagasCurso.next()){
					
					Curso curso = CursoDAO.get(rsmQuadroVagasCurso.getInt("cd_curso"), connect);
					
					int qtTurmasProjecao = rsmQuadroVagasCurso.getInt("qt_turmas");
					int qtVagasProjecao  = rsmQuadroVagasCurso.getInt("qt_vagas");
					
					pstmt = connect.prepareStatement("SELECT * FROM acd_turma A " +
													"  JOIN acd_instituicao_periodo B ON (A.cd_periodo_letivo = B.cd_periodo_letivo) " + 
													"  WHERE A.cd_instituicao = "+cdInstituicao+" "
												  + "    AND A.cd_curso = "+rsmQuadroVagasCurso.getString("cd_curso")
												  + "    AND A.tp_turno = "+rsmQuadroVagasCurso.getString("tp_turno")
												  + "	 AND (B.cd_periodo_letivo = "+cdPeriodoLetivo+" OR B.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")"
												  + "    AND A.nm_turma <> 'TRANSFERENCIA'"
												  + "    AND A.st_turma <> " + ST_INATIVO);
					ResultSetMap rsmTurmasAvaliadas = new ResultSetMap(pstmt.executeQuery());
					int qtTurmasAvaliadas = rsmTurmasAvaliadas.size();
					int qtVagasAvaliadas  = 0;
					while(rsmTurmasAvaliadas.next()){
						qtVagasAvaliadas += rsmTurmasAvaliadas.getInt("qt_vagas");
					}
					rsmTurmasAvaliadas.beforeFirst();
					boolean ativar = true;
					if(qtTurmasProjecao < qtTurmasAvaliadas || qtVagasProjecao < qtVagasAvaliadas){
						txtErro += "Curso: "+curso.getNmProdutoServico()+" - Turno: " + tiposTurno[rsmQuadroVagasCurso.getInt("tp_turno")] +". Quantidade de Turmas: " + qtTurmasProjecao + " (projeção) - " + qtTurmasAvaliadas + " (cadastradas). Quantidade de Vagas: " + qtVagasProjecao + " (projeção) - " + qtVagasAvaliadas + " (cadastradas)\n\r";
						ativar = false;
					}
					while(rsmTurmasAvaliadas.next()){
						Turma turma = TurmaDAO.get(rsmTurmasAvaliadas.getInt("cd_turma"), connect);
						turma.setStTurma((ativar ? TurmaServices.ST_ATIVO: TurmaServices.ST_PENDENTE));
						int resultado = TurmaDAO.update(turma, connect);
						if(resultado <= 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro ao atualizar turma "+turma.getNmTurma());
						}
						
						
						
						int cdTipoOcorrenciaCadastroTurma = TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_TURMA, connect).getCdTipoOcorrencia();
						OcorrenciaTurma ocorrencia = null;
						
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_turma", "" + turma.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
						ResultSetMap rsmOcorrenciaTurma = OcorrenciaTurmaDAO.find(criterios, connect);
						if(rsmOcorrenciaTurma.next()){
							ocorrencia = OcorrenciaTurmaDAO.get(rsmOcorrenciaTurma.getInt("cd_ocorrencia"), connect);
						}
						
						Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);
						InstituicaoPeriodo instituicaoPeriodo = null;
						ResultSetMap rsmPeriodoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(instituicao.getCdInstituicao(), connect);
						if(rsmPeriodoAtual.next()){
							instituicaoPeriodo = InstituicaoPeriodoDAO.get(rsmPeriodoAtual.getInt("cd_periodo_letivo"), connect);
						}
						curso = CursoDAO.get(turma.getCdCurso(), connect);
						
						if(ocorrencia == null){
							ocorrencia = new OcorrenciaTurma(0, 0, "Turma " + curso.getNmProdutoServico() + " - " + turma.getNmTurma() + " de " + instituicaoPeriodo.getNmPeriodoLetivo() +" da escola " + instituicao.getNmPessoa() + " cadastrada", Util.getDataAtual(), cdTipoOcorrenciaCadastroTurma, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, turma.getCdTurma(), Util.getDataAtual(), cdUsuario, turma.getStTurma(), turma.getStTurma());
							OcorrenciaTurmaServices.save(ocorrencia, connect);
						}
						else{
							ocorrencia.setDtUltimaModificacao(Util.getDataAtual());
							ocorrencia.setCdUsuarioModificador(cdUsuario);
							ocorrencia.setStTurmaPosterior(turma.getStTurma());
							OcorrenciaTurmaServices.save(ocorrencia, connect);
						}
						
						
					}
					
				}
			}
			
			if(!txtErro.equals("")){
				int ret = connect.prepareStatement("UPDATE acd_turma SET st_turma = " + ST_PENDENTE + " WHERE cd_instituicao = " + cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo + " AND st_turma IN  ("+ST_ATIVO+", "+ST_PENDENTE+")").executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar turmas");
				}
				
				if(isConnectionNull)
					connect.commit();
				return new Result(-1, "Turmas avaliadas não condizem com a projeção submetida pela Secretaria de Educação, por favor verifique e corrija:\n\r"+txtErro);
			}
			
//			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
//			
//			if(instituicaoPeriodo.getCdInstituicao() != cdInstituicao){
//				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//				criterios.add(new ItemComparator("cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
//				criterios.add(new ItemComparator("nm_periodo_letivo", instituicaoPeriodo.getNmPeriodoLetivo(), ItemComparator.EQUAL, Types.VARCHAR));
//				ResultSetMap rsmInstituicaoPeriodoAtual = InstituicaoPeriodoDAO.find(criterios, connect);
//				if(rsmInstituicaoPeriodoAtual.next()){
//					cdPeriodoLetivo = rsmInstituicaoPeriodoAtual.getInt("cd_periodo_letivo");
//				}
//			}
//			
//			instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
//			instituicaoPeriodo.setStPeriodoLetivo(InstituicaoPeriodoServices.ST_ATUAL);
//			if(InstituicaoPeriodoServices.save(instituicaoPeriodo, connect).getCode() <= 0){
//				if(isConnectionNull)
//					Conexao.rollback(connect);
//				return new Result(-1, "Erro ao atualizar periodo letivo");
//			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Turmas validadas com sucesso. Periodo Letivo da instituição iniciado.");
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao remanejar alunos.");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result matriculaAlunoAee(int cdTurma, int cdAluno){
		return matriculaAlunoAee(cdTurma, cdAluno, null);
	}
	
	public static Result matriculaAlunoAee(int cdTurma, int cdAluno, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Turma turma = TurmaDAO.get(cdTurma, connect);
			Matricula matriculaAtual = null;
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(turma.getCdPeriodoLetivo(), connect);
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoSecretaria = 0;
			ResultSetMap rsmPeriodoSecretaria = InstituicaoServices.getPeriodoLetivoRecente(cdSecretaria);
			if(rsmPeriodoSecretaria.next())
				cdPeriodoSecretaria = rsmPeriodoSecretaria.getInt("cd_periodo_letivo");
			
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_aluno", "" + cdAluno, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("J.cd_periodo_letivo_superior", "" + instituicaoPeriodo.getCdPeriodoLetivoSuperior() + ", " + cdPeriodoSecretaria, ItemComparator.IN, Types.INTEGER));
			criterios.add(new ItemComparator("A.st_matricula", "" + MatriculaServices.ST_ATIVA, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmMatriculaAtual = MatriculaServices.find(criterios, connect);
			if(rsmMatriculaAtual.next()){
				matriculaAtual = MatriculaDAO.get(rsmMatriculaAtual.getInt("cd_matricula"), connect);
			}
			else{
				return new Result(-1, "Aluno não possui matrícula regular no periodo atual");
			}
			
			Matricula matricula = new Matricula();
			matricula.setCdAluno(cdAluno);
			matricula.setCdTurma(cdTurma);
			matricula.setCdMatriz(turma.getCdMatriz());
			matricula.setCdPeriodoLetivo(turma.getCdPeriodoLetivo());
			matricula.setDtMatricula(Util.getDataAtual());
			matricula.setStMatricula(MatriculaServices.ST_ATIVA);
			matricula.setNrMatricula(matriculaAtual.getNrMatricula());
			matricula.setCdCurso(turma.getCdCurso());
			
			ResultSetMap rsmHorariosTurma = TurmaServices.getHorariosByTurma(matricula.getCdTurma());
			ResultSetMap rsmMatriculaNoPeriodo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula A, acd_instituicao_periodo B WHERE A.cd_periodo_letivo = B.cd_periodo_letivo AND cd_matricula <> " + matricula.getCdMatricula() + " AND cd_aluno = " + matricula.getCdAluno() + " AND B.nm_periodo_letivo = '"+instituicaoPeriodo.getNmPeriodoLetivo()+"' AND A.st_matricula = " + MatriculaServices.ST_ATIVA).executeQuery());
			while(rsmMatriculaNoPeriodo.next()){
				
				Curso cursoMatriculado = CursoDAO.get(rsmMatriculaNoPeriodo.getInt("cd_curso"), connect);
				Turma turmaMatriculada = TurmaDAO.get(rsmMatriculaNoPeriodo.getInt("cd_turma"), connect);
				Instituicao instituicaoMatriculado = InstituicaoDAO.get(rsmMatriculaNoPeriodo.getInt("cd_instituicao"), connect);
				
				ResultSetMap rsmHorariosTurmaCadastrada = TurmaServices.getHorariosByTurma(rsmMatriculaNoPeriodo.getInt("cd_turma"));
				while(rsmHorariosTurmaCadastrada.next()){
					while(rsmHorariosTurma.next()){
						InstituicaoHorario instituicaoHorarioTurmaCadastrada = InstituicaoHorarioDAO.get(rsmHorariosTurmaCadastrada.getInt("cd_horario"), connect);
						InstituicaoHorario instituicaoHorarioTurma			 = InstituicaoHorarioDAO.get(rsmHorariosTurma.getInt("cd_horario"), connect);
						
						if(((instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) > instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) || 
							(instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) && 
							 instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.MINUTE) > instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE))) && 
							 instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) < instituicaoHorarioTurma.getHrTermino().get(Calendar.HOUR_OF_DAY) || 
							(instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurma.getHrTermino().get(Calendar.HOUR_OF_DAY) && 
							 instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.MINUTE) < instituicaoHorarioTurma.getHrTermino().get(Calendar.MINUTE))) ||
						   ((instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) > instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) || 
						    (instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) && 
						     instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE) > instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.MINUTE))) && 
						     instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) < instituicaoHorarioTurmaCadastrada.getHrTermino().get(Calendar.HOUR_OF_DAY) || 
						    (instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurmaCadastrada.getHrTermino().get(Calendar.HOUR_OF_DAY) && 
						     instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE) < instituicaoHorarioTurmaCadastrada.getHrTermino().get(Calendar.MINUTE))) &&
						     instituicaoHorarioTurmaCadastrada.getNrDiaSemana() == instituicaoHorarioTurma.getNrDiaSemana()){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Aluno já possui matricula nesse horário na turma "+cursoMatriculado.getNmProdutoServico()+" - "+turmaMatriculada.getNmTurma()+" na escola "+instituicaoMatriculado.getNmPessoa()+".");
						}
					}
					rsmHorariosTurma.beforeFirst();
				}
				rsmHorariosTurmaCadastrada.beforeFirst();
				
			}
			rsmMatriculaNoPeriodo.beforeFirst();
			
			
			if(MatriculaDAO.insert(matricula, connect) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao fazer matrícula");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Matricula em Aee realizada com sucesso.");
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao matricular aluno.");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result matriculaAlunoAtividadeComplementar(int cdTurma, ArrayList<Integer> codigosAlunos, int cdUsuario){
		return matriculaAlunoAtividadeComplementar(cdTurma, codigosAlunos, cdUsuario, null);
	}
	
	public static Result matriculaAlunoAtividadeComplementar(int cdTurma, ArrayList<Integer> codigosAlunos, int cdUsuario, Connection connect){
		
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsmAlunosMatriculados = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula WHERE cd_turma = " + cdTurma + " AND st_matricula = 0").executeQuery());
			while(rsmAlunosMatriculados.next()){
				if(!codigosAlunos.contains(rsmAlunosMatriculados.getInt("cd_aluno"))){
					Result result = MatriculaServices.saveInativacao(MatriculaDAO.get(rsmAlunosMatriculados.getInt("cd_matricula"), connect), cdUsuario, connect);
					if(result.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
				}
			}
			rsmAlunosMatriculados.beforeFirst();
			
			for(Integer cdAluno : codigosAlunos){
				
				boolean naoEncontrado = true;
				while(rsmAlunosMatriculados.next()){
					if(rsmAlunosMatriculados.getInt("cd_aluno") == cdAluno){
						naoEncontrado = false;
						break;
					}
				}
				rsmAlunosMatriculados.beforeFirst();
				
				if(naoEncontrado){
					Result result = matriculaAlunoAtividadeComplementar(cdTurma, cdAluno, connect);
					if(result.getCode() < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return result;
					}
				}
			}
		
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Matricula de Mais Educação realizada com sucesso.");
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao matricular aluno.");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result matriculaAlunoAtividadeComplementar(int cdTurma, int cdAluno){
		return matriculaAlunoAtividadeComplementar(cdTurma, cdAluno, null);
	}
	
	public static Result matriculaAlunoAtividadeComplementar(int cdTurma, int cdAluno, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Aluno aluno = AlunoDAO.get(cdAluno, connect);
			Turma turma = TurmaDAO.get(cdTurma, connect);
			Matricula matriculaAtual = null;
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(turma.getCdPeriodoLetivo(), connect);
			
			
			if(turma.getCdCurso() != ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "O curso da turma deve ser MAIS EDUCAÇÃO para que se possa adicionar alunos através desse recursos");
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_aluno", "" + cdAluno, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("J.cd_periodo_letivo_superior", "" + instituicaoPeriodo.getCdPeriodoLetivoSuperior(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.st_matricula", "" + MatriculaServices.ST_ATIVA, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmMatriculaAtual = MatriculaServices.find(criterios, connect);
			if(rsmMatriculaAtual.next()){
				matriculaAtual = MatriculaDAO.get(rsmMatriculaAtual.getInt("cd_matricula"), connect);
			}
			else{
				return new Result(-1, "Aluno(a) "+aluno.getNmPessoa()+" não possui matrícula regular no periodo atual");
			}
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_aluno", "" + cdAluno, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("st_matricula", "" + MatriculaServices.ST_ATIVA, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_curso", "" + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmMatriculaME = MatriculaDAO.find(criterios, connect);
			if(rsmMatriculaME.next()){
				return new Result(-1, "Aluno(a) "+aluno.getNmPessoa()+" já possui matrícula em turma MAIS EDUCAÇÃO");
			}
			
			Matricula matricula = new Matricula();
			matricula.setCdAluno(cdAluno);
			matricula.setCdTurma(cdTurma);
			matricula.setCdMatriz(turma.getCdMatriz());
			matricula.setCdPeriodoLetivo(turma.getCdPeriodoLetivo());
			matricula.setDtMatricula(Util.getDataAtual());
			matricula.setStMatricula(MatriculaServices.ST_ATIVA);
			matricula.setNrMatricula(matriculaAtual.getNrMatricula());
			matricula.setCdCurso(turma.getCdCurso());
			
			
			ResultSetMap rsmHorariosTurma = TurmaServices.getHorariosByTurma(matricula.getCdTurma());
			ResultSetMap rsmMatriculaNoPeriodo = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_matricula A, acd_instituicao_periodo B WHERE A.cd_periodo_letivo = B.cd_periodo_letivo AND cd_matricula <> " + matricula.getCdMatricula() + " AND cd_aluno = " + matricula.getCdAluno() + " AND B.nm_periodo_letivo = '"+instituicaoPeriodo.getNmPeriodoLetivo()+"' AND A.st_matricula = " + MatriculaServices.ST_ATIVA).executeQuery());
			while(rsmMatriculaNoPeriodo.next()){

				Curso cursoMatriculado = CursoDAO.get(rsmMatriculaNoPeriodo.getInt("cd_curso"), connect);
				Turma turmaMatriculada = TurmaDAO.get(rsmMatriculaNoPeriodo.getInt("cd_turma"), connect);
				Instituicao instituicaoMatriculado = InstituicaoDAO.get(rsmMatriculaNoPeriodo.getInt("cd_instituicao"), connect);
				
				ResultSetMap rsmHorariosTurmaCadastrada = TurmaServices.getHorariosByTurma(rsmMatriculaNoPeriodo.getInt("cd_turma"));
				while(rsmHorariosTurmaCadastrada.next()){
					while(rsmHorariosTurma.next()){
						InstituicaoHorario instituicaoHorarioTurmaCadastrada = InstituicaoHorarioDAO.get(rsmHorariosTurmaCadastrada.getInt("cd_horario"), connect);
						InstituicaoHorario instituicaoHorarioTurma			 = InstituicaoHorarioDAO.get(rsmHorariosTurma.getInt("cd_horario"), connect);
						
						if(((instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) > instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) || 
							(instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) && 
							 instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.MINUTE) > instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE))) && 
							 instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) < instituicaoHorarioTurma.getHrTermino().get(Calendar.HOUR_OF_DAY) || 
							(instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurma.getHrTermino().get(Calendar.HOUR_OF_DAY) && 
							 instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.MINUTE) < instituicaoHorarioTurma.getHrTermino().get(Calendar.MINUTE))) ||
						   ((instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) > instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) || 
						    (instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.HOUR_OF_DAY) && 
						     instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE) > instituicaoHorarioTurmaCadastrada.getHrInicio().get(Calendar.MINUTE))) && 
						     instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) < instituicaoHorarioTurmaCadastrada.getHrTermino().get(Calendar.HOUR_OF_DAY) || 
						    (instituicaoHorarioTurma.getHrInicio().get(Calendar.HOUR_OF_DAY) == instituicaoHorarioTurmaCadastrada.getHrTermino().get(Calendar.HOUR_OF_DAY) && 
						     instituicaoHorarioTurma.getHrInicio().get(Calendar.MINUTE) < instituicaoHorarioTurmaCadastrada.getHrTermino().get(Calendar.MINUTE))) &&
						     instituicaoHorarioTurmaCadastrada.getNrDiaSemana() == instituicaoHorarioTurma.getNrDiaSemana()){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Aluno(a) "+aluno.getNmPessoa()+" já possui matricula nesse horário na turma "+cursoMatriculado.getNmProdutoServico()+" - "+turmaMatriculada.getNmTurma()+" na escola "+instituicaoMatriculado.getNmPessoa()+".");
						}
					}
					rsmHorariosTurma.beforeFirst();
				}
				rsmHorariosTurmaCadastrada.beforeFirst();
				
			}
			rsmMatriculaNoPeriodo.beforeFirst();
			
			
			if(MatriculaDAO.insert(matricula, connect) <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao fazer matrícula");
			}
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1, "Matricula de Mais Educação realizada com sucesso.");
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao matricular aluno.");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getDadosFichaCensoByTurma(int cdTurma)	{
		return MatriculaServices.getDadosFichaCenso(0, cdTurma, null);
	}
	
	public static ResultSetMap getDadosFichaCensoByTurma(int cdTurma, Connection connect)	{
		return MatriculaServices.getDadosFichaCenso(0, cdTurma, connect);
	}
	
	
	public static Result validacaoTurmas(int cdInstituicao, int tpValidacao, int cdUsuario){
		return validacaoTurmas(cdInstituicao, tpValidacao, cdUsuario, null);
	}
	
	public static Result validacaoTurmas(int cdInstituicao, int tpValidacao/*0 - Validação Completa, 1 - Validação apenas de Turma*/, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			ResultSetMap rsmTurmas = TurmaServices.getAllTurmas(cdInstituicao, false, connect);
			int cont = 0;
			while(rsmTurmas.next()){
				Turma turma = TurmaDAO.get(rsmTurmas.getInt("cd_turma"), connect);
				Curso curso = CursoDAO.get(turma.getCdCurso(), connect);
				
				System.out.println((cont++) + " - Curso: " + curso.getNmProdutoServico() + ", Turma: " + turma.getNmTurma());
				
				Result resultado = validacaoTurma(rsmTurmas.getInt("cd_turma"), tpValidacao, cdUsuario, connect);
				if(resultado.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return resultado;
				}
			}
			rsmTurmas.beforeFirst();
			
			int cdPeriodoRecente = 0;
			ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
			if(rsmPeriodoRecente.next())
				cdPeriodoRecente = rsmPeriodoRecente.getInt("cd_periodo_letivo");
			
			rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_turma A, acd_instituicao_periodo B "
					+ "												WHERE A.cd_periodo_letivo = B.cd_periodo_letivo "
					+ "												  AND A.cd_instituicao = " + cdInstituicao
					+ " 											  AND B.cd_periodo_letivo = " + cdPeriodoRecente
					+ "												  AND A.st_turma NOT IN (" + ST_ATIVO + ", " + ST_CONCLUIDO + ")").executeQuery());
			while(rsmTurmas.next()){
				connect.prepareStatement("DELETE FROM acd_instituicao_pendencia "
						+ "												WHERE cd_instituicao = " + cdInstituicao
						+ " 											  AND cd_turma = " + rsmTurmas.getInt("cd_turma")).executeUpdate();
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Sucesso ao validar");
			return result;
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro em InstituicaoServices.validacaoTurmas");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result validacaoTurma(int cdTurma, int tpValidacao, int cdUsuario){
		return validacaoTurma(cdTurma, tpValidacao, cdUsuario, null);
	}
	
	public static Result validacaoTurma(int cdTurma, int tpValidacao/*0 - Validação Completa, 1 - Validação apenas de Turma*/, int cdUsuario, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Turma turma = TurmaDAO.get(cdTurma, connect);
			
			//Buscar Atividades complementar em forma de array
			ArrayList<Integer> atividadesComplementares = new ArrayList<Integer>();
			ResultSetMap rsmAtividadesComplementares = TurmaServices.getAtividadeComplementarOf(turma.getCdTurma());
			while(rsmAtividadesComplementares.next()){
				atividadesComplementares.add(rsmAtividadesComplementares.getInt("cd_atividade_complementar"));
			}
			rsmAtividadesComplementares.beforeFirst();
			
			//Buscar Atendimentos especializados em forma de array
			ArrayList<Integer> atendimentosEspecializados = new ArrayList<Integer>();
			ResultSetMap rsmAtendimentosEspecializados = TurmaServices.getAtendimentoEspecializadoOf(turma.getCdTurma());
			while(rsmAtendimentosEspecializados.next()){
				atendimentosEspecializados.add(rsmAtendimentosEspecializados.getInt("cd_atividade_complementar"));
			}
			rsmAtendimentosEspecializados.beforeFirst();
			
			ValidatorResult resultadoValidacao = validate(turma, atividadesComplementares, atendimentosEspecializados, true, false, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
			Result resultValidacoesPendencia = InstituicaoPendenciaServices.atualizarValidacaoPendencia(resultadoValidacao, turma.getCdInstituicao(), turma.getCdTurma(), 0/*cdAluno*/, 0/*cdProfessor*/, InstituicaoPendenciaServices.TP_REGISTRO_TURMA_CENSO, cdUsuario, GRUPO_VALIDACAO_EDUCACENSO, connect);
			if(resultValidacoesPendencia.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return resultValidacoesPendencia;
			}
			
			if(tpValidacao == 0){
				Result ret = AlunoServices.validacaoAlunos(turma.getCdTurma(), cdUsuario, connect);
				if(ret.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return ret;
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Sucesso ao validar");
			return result;
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro em InstituicaoServices.validacaoTurmas");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result correcaoDisciplinas(){
		return correcaoDisciplinas(null);
	}
	
	public static Result correcaoDisciplinas(Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ArrayList<Integer> codigosDisciplinaFund1 = new ArrayList<Integer>();
			codigosDisciplinaFund1.add(207);
			codigosDisciplinaFund1.add(209);
			codigosDisciplinaFund1.add(210);
			codigosDisciplinaFund1.add(215);
			codigosDisciplinaFund1.add(216);
			codigosDisciplinaFund1.add(217);
			codigosDisciplinaFund1.add(218);
			codigosDisciplinaFund1.add(228);
			
			ResultSetMap rsmTurmas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_turma WHERE cd_periodo_letivo > 835 AND st_turma = " + ST_ATIVO).executeQuery());
			while(rsmTurmas.next()){
				
				Turma turma = TurmaDAO.get(rsmTurmas.getInt("cd_turma"), connect);
				
				//INFANTIL
				if(rsmTurmas.getInt("cd_curso") == 1179 || rsmTurmas.getInt("cd_curso") == 1147 || rsmTurmas.getInt("cd_curso") == 1181 || rsmTurmas.getInt("cd_curso") == 1159 ||
				   rsmTurmas.getInt("cd_curso") == 111  || rsmTurmas.getInt("cd_curso") == 110){
					ResultSetMap rsmOfertas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_oferta WHERE cd_turma = " + rsmTurmas.getInt("cd_turma")).executeQuery());
					while(rsmOfertas.next()){
						Oferta oferta = OfertaDAO.get(rsmOfertas.getInt("cd_oferta"), connect);
						oferta.setCdDisciplina(0);
						if(OfertaDAO.update(oferta, connect) < 0){
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-1, "Erro na atualização de Oferta em Infantil");
						}
						
					}
				}
				
				//FUND 1
				if(rsmTurmas.getInt("cd_curso") == 1149 || rsmTurmas.getInt("cd_curso") == 1151 || rsmTurmas.getInt("cd_curso") == 1153 || rsmTurmas.getInt("cd_curso") == 1155  ||
				   rsmTurmas.getInt("cd_curso") == 1157  || rsmTurmas.getInt("cd_curso") == 1161 || rsmTurmas.getInt("cd_curso") == 1163 || rsmTurmas.getInt("cd_curso") == 1165 || 
				   rsmTurmas.getInt("cd_curso") == 1167 || rsmTurmas.getInt("cd_curso") == 1169){
					
					ArrayList<Integer> codigosDisciplinaFund1Comp = (ArrayList<Integer>)codigosDisciplinaFund1.clone();
					int cdProfessor = 0;
					ResultSetMap rsmOfertas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_oferta WHERE cd_turma = " + rsmTurmas.getInt("cd_turma")).executeQuery());
					while(rsmOfertas.next()){
						cdProfessor = rsmOfertas.getInt("cd_professor");
						if(cdProfessor == 0){
							ResultSetMap rsmPessoaOferta = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_pessoa_oferta WHERE cd_oferta = " + rsmOfertas.getInt("cd_oferta")).executeQuery());
							if(rsmPessoaOferta.next()){
								cdProfessor = rsmPessoaOferta.getInt("cd_pessoa");
							}
						}
						if(codigosDisciplinaFund1Comp.contains(rsmOfertas.getInt("cd_disciplina"))){
							codigosDisciplinaFund1Comp.remove(new Integer(rsmOfertas.getInt("cd_disciplina")));
						}
						else{
							connect.prepareStatement("DELETE FROM acd_matricula_disciplina WHERE cd_oferta = " + rsmOfertas.getInt("cd_oferta")).executeUpdate();
							connect.prepareStatement("DELETE FROM acd_pessoa_oferta WHERE cd_oferta = " + rsmOfertas.getInt("cd_oferta")).executeUpdate();
							
							Oferta oferta = OfertaDAO.get(rsmOfertas.getInt("cd_oferta"), connect);
							if(OfertaDAO.delete(oferta.getCdOferta(), connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na deleção de Oferta em Fund 1");
							}
						}
					}
					
					
					if(cdProfessor > 0){
						for(int cdDisciplina : codigosDisciplinaFund1Comp){
							Oferta oferta = new Oferta();
							oferta.setCdCurso(turma.getCdCurso());
							oferta.setCdDisciplina(cdDisciplina);
							oferta.setCdTurma(turma.getCdTurma());
							oferta.setCdPeriodoLetivo(turma.getCdPeriodoLetivo());
							oferta.setCdMatriz(turma.getCdMatriz());
							oferta.setCdCursoModulo(turma.getCdCursoModulo());
							oferta.setCdProfessor(cdProfessor);
							int cod = OfertaDAO.insert(oferta, connect);
							if(cod < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na inserção de Oferta em Fund 1");
							}
							
							if(PessoaOfertaDAO.insert(new PessoaOferta(cdProfessor, cod, ParametroServices.getValorOfParametroAsInteger("CD_FUNCAO_PROFESSOR", 0), PessoaOfertaServices.ST_ATIVO), connect) < 0){
								if(isConnectionNull)
									Conexao.rollback(connect);
								return new Result(-1, "Erro na relação de Professor e Oferta em Fund 1");
							}
							
							ResultSetMap rsmAlunos = getAlunos(turma.getCdTurma(), true, connect);
							while(rsmAlunos.next()){
								
								if(MatriculaPeriodoLetivoDAO.get(rsmAlunos.getInt("cd_matricula"), turma.getCdPeriodoLetivo(), connect) == null){
									if(MatriculaPeriodoLetivoDAO.insert(new MatriculaPeriodoLetivo(rsmAlunos.getInt("cd_matricula"), turma.getCdPeriodoLetivo(), 0, rsmAlunos.getGregorianCalendar("dt_matricula"), rsmAlunos.getInt("st_matricula"), 0, 0, 0), connect) < 0){
										if(isConnectionNull)
											Conexao.rollback(connect);
										return new Result(-1, "Erro na relação de Matricula e Periodo Letivo em Fund 1");
									}
								}
								
								MatriculaDisciplina matriculaDisciplina = new MatriculaDisciplina();
								matriculaDisciplina.setCdMatricula(rsmAlunos.getInt("cd_matricula"));
								matriculaDisciplina.setCdPeriodoLetivo(turma.getCdPeriodoLetivo());
								matriculaDisciplina.setCdProfessor(cdProfessor);
								matriculaDisciplina.setCdCurso(rsmAlunos.getInt("cd_curso"));
								matriculaDisciplina.setCdDisciplina(cdDisciplina);
								if(MatriculaDisciplinaDAO.insert(matriculaDisciplina, connect) < 0){
									if(isConnectionNull)
										Conexao.rollback(connect);
									return new Result(-1, "Erro na relação de Matricula e Disciplina em Fund 1");
								}
							}
						}
					}
				}
//				
//				
//				//FUND 2
//				if(rsmTurmas.getInt("cd_curso") == 1201 || rsmTurmas.getInt("cd_curso") == 1199 || rsmTurmas.getInt("cd_curso") == 1183 || rsmTurmas.getInt("cd_curso") == 1185  ||
//				   rsmTurmas.getInt("cd_curso") == 1171  || rsmTurmas.getInt("cd_curso") == 1173 || rsmTurmas.getInt("cd_curso") == 1175 || rsmTurmas.getInt("cd_curso") == 1177){
//					
//					ArrayList<Integer> codigosDisciplinaFund1Comp = (ArrayList<Integer>)codigosDisciplinaFund1.clone();
//					int cdProfessor = 0;
//					ResultSetMap rsmOfertas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_oferta WHERE cd_turma = " + rsmTurmas.getInt("cd_turma")).executeQuery());
//					while(rsmOfertas.next()){
//						cdProfessor = rsmOfertas.getInt("cd_professor");
//						if(cdProfessor == 0){
//							ResultSetMap rsmPessoaOferta = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_pessoa_oferta WHERE cd_oferta = " + rsmOfertas.getInt("cd_oferta")).executeQuery());
//							if(rsmPessoaOferta.next()){
//								cdProfessor = rsmPessoaOferta.getInt("cd_pessoa");
//							}
//						}
//						if(codigosDisciplinaFund1Comp.contains(rsmOfertas.getInt("cd_disciplina"))){
//							codigosDisciplinaFund1Comp.remove(rsmOfertas.getInt("cd_disciplina"));
//						}
//						else{
//							Oferta oferta = OfertaDAO.get(rsmOfertas.getInt("cd_oferta"), connect);
//							if(OfertaDAO.delete(oferta.getCdOferta(), connect) < 0){
//								if(isConnectionNull)
//									Conexao.rollback(connect);
//								return new Result(-1, "Erro na deleção de Oferta em Fund 1");
//							}
//						}
//					}
//					
//					
//					if(cdProfessor > 0){
//						for(int cdDisciplina : codigosDisciplinaFund1Comp){
//							Oferta oferta = new Oferta();
//							oferta.setCdCurso(turma.getCdCurso());
//							oferta.setCdDisciplina(cdDisciplina);
//							oferta.setCdTurma(turma.getCdTurma());
//							oferta.setCdPeriodoLetivo(turma.getCdPeriodoLetivo());
//							oferta.setCdMatriz(turma.getCdMatriz());
//							oferta.setCdCursoModulo(turma.getCdCursoModulo());
//							oferta.setCdProfessor(cdProfessor);
//							int cod = OfertaDAO.insert(oferta, connect);
//							if(cod < 0){
//								if(isConnectionNull)
//									Conexao.rollback(connect);
//								return new Result(-1, "Erro na inserção de Oferta em Fund 1");
//							}
//							
//							if(PessoaOfertaDAO.insert(new PessoaOferta(cdProfessor, cod, ParametroServices.getValorOfParametroAsInteger("CD_FUNCAO_PROFESSOR", 0)), connect) < 0){
//								if(isConnectionNull)
//									Conexao.rollback(connect);
//								return new Result(-1, "Erro na relação de Professor e Oferta em Fund 1");
//							}
//						}
//					}
//				}
				
				
			}
			rsmTurmas.beforeFirst();
			
			if(isConnectionNull)
				connect.commit();
			
			return new Result(1);
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro em InstituicaoServices.validacaoTurmas");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getTurmasBy(int cdProfessor, int cdInstituicao) {
		return getTurmasBy(cdProfessor, cdInstituicao, false, null);
	}

	public static ResultSetMap getTurmasBy(int cdProfessor, int cdInstituicao, Connection connect) {
		return getTurmasBy(cdProfessor, cdInstituicao, false, connect);
	}
	
	public static ResultSetMap getTurmasBy(int cdProfessor, int cdInstituicao, boolean lancarCenso) {
		return getTurmasBy(cdProfessor, cdInstituicao, lancarCenso, null);
	}

	public static ResultSetMap getTurmasBy(int cdProfessor, int cdInstituicao, boolean lancarCenso, Connection connect) {
		boolean isConnectionNull = connect == null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			// busca o periodo letivo vigente na instituicao
			ResultSetMap rsmPeriodoLetivoVigente = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao);
			
			int cdPeriodoLetivo = 0;
			if(rsmPeriodoLetivoVigente.next()) {
				cdPeriodoLetivo = rsmPeriodoLetivoVigente.getInt("CD_PERIODO_LETIVO");
			}
			
			pstmt = connect.prepareStatement("SELECT A.cd_turma, B.cd_disciplina " + 
											" FROM acd_turma A" +
											" JOIN acd_oferta B ON (A.cd_turma = B.cd_turma)" +
											" WHERE (EXISTS (SELECT * FROM acd_pessoa_oferta PO WHERE PO.cd_pessoa = "+cdProfessor+" AND PO.cd_oferta = B.cd_oferta AND PO.st_pessoa_oferta = "+PessoaOfertaServices.ST_ATIVO+") OR B.cd_professor = "+cdProfessor+")"+
											"   AND B.cd_periodo_letivo = " + cdPeriodoLetivo +
											"   AND A.cd_instituicao = " + cdInstituicao+
											"   AND A.st_turma = " + TurmaServices.ST_ATIVO+
											"   AND B.st_oferta = " + OfertaServices.ST_ATIVO);
			
			String dtReferenciaCenso = ParametroServices.getValorOfParametro("DT_REFERENCIA_CENSO", connect);
			dtReferenciaCenso += dtReferenciaCenso + "/" + Util.getDataAtual().get(Calendar.YEAR);
			GregorianCalendar dtReferenciaCensoCalendar = Util.convStringToCalendar(dtReferenciaCenso);
			
			int x = 0;
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ResultSetMap rsmFinal = new ResultSetMap();
			ArrayList<String> codigos = new ArrayList<String>();
			while(rsm.next()){
				
				if(lancarCenso){
					boolean excluido = false;
					ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_oferta", "" + rsm.getInt("cd_oferta"), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmAlunosOcorrenciaOferta = OcorrenciaOfertaServices.find(criterios, connect);
					while(rsmAlunosOcorrenciaOferta.next()){
						if(rsmAlunosOcorrenciaOferta.getGregorianCalendar("dt_ocorrencia").after(dtReferenciaCensoCalendar)){
							rsm.deleteRow();
							if(x == 0)
								rsm.beforeFirst();
							else
								rsm.previous();
							excluido = true;
							break;
						}
						else if (rsmAlunosOcorrenciaOferta.getGregorianCalendar("dt_ultima_modificacao").after(dtReferenciaCensoCalendar) && rsmAlunosOcorrenciaOferta.getInt("st_oferta_anterior") != rsmAlunosOcorrenciaOferta.getInt("st_oferta_posterior")){
							rsm.setValueToField("ST_OFERTA", rsmAlunosOcorrenciaOferta.getInt("st_oferta_anterior"));
						}
					}
					rsmAlunosOcorrenciaOferta.beforeFirst();
					
					if(rsm.getInt("ST_OFERTA") != OfertaServices.ST_ATIVO){
						rsm.deleteRow();
						if(x == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						excluido = true;
					}
					
					if(excluido)
						continue;
					
					x++;
				}
				
				if(!codigos.contains(rsm.getString("cd_turma"))){
					rsmFinal.addRegister(rsm.getRegister());
					codigos.add(rsm.getString("cd_turma"));
				}
			}
			
			rsmFinal.beforeFirst();
			
			return rsmFinal;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProfessorServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getTurmasSemAluno(int cdPeriodoLetivo) {
		return getTurmasSemAluno(cdPeriodoLetivo, 0, null);
	}
	
	public static Result getTurmasSemAluno(int cdPeriodoLetivo, int cdInstituicao) {
		return getTurmasSemAluno(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	public static Result getTurmasSemAluno(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT T.*, B.*, C.nm_pessoa AS NM_INSTITUICAO, F.nm_produto_servico AS NM_CURSO FROM acd_turma T, acd_instituicao B, grl_pessoa C, acd_instituicao_educacenso D, acd_instituicao_periodo E, grl_produto_servico F "
					+ "						   WHERE T.cd_instituicao = B.cd_instituicao "
					+ "							 AND B.cd_instituicao = C.cd_pessoa "
					+ "							 AND B.cd_instituicao = D.cd_instituicao"
					+ "							 AND T.cd_curso = F.cd_produto_servico"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							 AND T.cd_periodo_letivo = E.cd_periodo_letivo"
					+ "							 AND E.cd_periodo_letivo = D.cd_periodo_letivo"
					+ "							 AND B.cd_instituicao <> " + cdSecretaria
					+ (cdInstituicao > 0 ? " AND B.cd_instituicao = " + cdInstituicao : "")
					+ "							 AND (E.cd_periodo_letivo = "+cdPeriodoLetivo+" OR E.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")" 
					+ "							 AND T.nm_turma <> 'TRANSFERENCIA'"
					+ "						  ORDER BY C.nm_pessoa, F.nm_produto_servico, T.nm_turma");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ResultSetMap rsmTurmasSemAlunos = new ResultSetMap();
			while(rsm.next()){
				if(getAlunos(rsm.getInt("cd_turma"), true, connect).size() == 0){
					rsmTurmasSemAlunos.addRegister(rsm.getRegister());
				}
			}
			rsm.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsmTurmasSemAlunos);
			result.addObject("MENSAGEM_ESTATISTICA", "Número de Turmas sem alunos: " + rsmTurmasSemAlunos.size() + "\n"
					+ "Número de Turmas com alunos: " + (rsm.size() - rsmTurmasSemAlunos.size()) + "\n"
					+ "Número de Turmas total: " + (rsm.size()));
			return result;			
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getTurmasPorTurno(int cdPeriodoLetivo) {
		return getTurmasPorTurno(cdPeriodoLetivo, 0, null);
	}
	
	public static Result getTurmasPorTurno(int cdPeriodoLetivo, int cdInstituicao) {
		return getTurmasPorTurno(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	public static Result getTurmasPorTurno(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT T.tp_turno, count(T.cd_turma) AS QT_TURMAS FROM acd_turma T, acd_instituicao B, acd_instituicao_educacenso D, acd_instituicao_periodo E "
					+ "						   WHERE T.cd_instituicao = B.cd_instituicao "
					+ "							 AND B.cd_instituicao = D.cd_instituicao"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							 AND T.cd_periodo_letivo = E.cd_periodo_letivo"
					+ "							 AND E.cd_periodo_letivo = D.cd_periodo_letivo"
					+ "							 AND B.cd_instituicao <> " + cdSecretaria
					+ (cdInstituicao > 0 ? " AND B.cd_instituicao = " + cdInstituicao : "")
					+ "							 AND (E.cd_periodo_letivo = "+cdPeriodoLetivo+" OR E.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")" 
					+ "							 AND T.nm_turma <> 'TRANSFERENCIA'"
					+ "						  GROUP BY T.tp_turno");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("CL_TURNO", tiposTurno[rsm.getInt("TP_TURNO")]);
			}
			rsm.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "");
			return result;			
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getTurmasPorTurnoTurmas(int cdPeriodoLetivo, int tpTurno) {
		return getTurmasPorTurnoTurmas(cdPeriodoLetivo, tpTurno, 0, null);
	}
	
	public static Result getTurmasPorTurnoTurmas(int cdPeriodoLetivo, int tpTurno, int cdInstituicao) {
		return getTurmasPorTurnoTurmas(cdPeriodoLetivo, tpTurno, cdInstituicao, null);
	}
	
	public static Result getTurmasPorTurnoTurmas(int cdPeriodoLetivo, int tpTurno, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if(cdInstituicao > 0)
				criterios.add(new ItemComparator("A.cd_instituicao", "" + cdInstituicao, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("ACD.st_instituicao_publica", "" + InstituicaoEducacensoServices.ST_EM_ATIVIDADE, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.tp_turno", "" + tpTurno, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("sqlAdicional", " AND (D.cd_periodo_letivo = "+cdPeriodoLetivo+" OR D.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")", ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = TurmaServices.find(criterios, connect);
			while(rsm.next()){
				ResultSetMap rsmOferta = TurmaServices.getOfertasByTurma(rsm.getInt("cd_turma"));
				if(rsmOferta.next()){
					Oferta oferta = OfertaDAO.get(rsmOferta.getInt("cd_oferta"), connect);
					Professor professor = ProfessorDAO.get(oferta.getCdProfessor(), connect);
					if(professor != null){
						rsm.setValueToField("NM_PROFESSOR", professor.getNmPessoa());
					}
					else{
						String nmProfessores = "";
						ResultSetMap rsmPessoaOferta = PessoaOfertaServices.getAllByOferta(oferta.getCdOferta(), connect);
						while(rsmPessoaOferta.next()){
							professor = ProfessorDAO.get(rsmPessoaOferta.getInt("cd_pessoa"), connect);
							nmProfessores += professor.getNmPessoa() + ", ";
						}
						nmProfessores = nmProfessores.substring(0, nmProfessores.length()-2);

						rsm.setValueToField("NM_PROFESSOR", nmProfessores);
					}					
				}
			}
			rsm.beforeFirst();

			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "");
			return result;
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getTurmasPorModalidade(int cdPeriodoLetivo) {
		return getTurmasPorModalidade(cdPeriodoLetivo, 0, null);
	}
	
	public static Result getTurmasPorModalidade(int cdPeriodoLetivo, int cdInstituicao) {
		return getTurmasPorModalidade(cdPeriodoLetivo, cdInstituicao, null);
	}
	
	public static Result getTurmasPorModalidade(int cdPeriodoLetivo, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			pstmt = connect.prepareStatement("SELECT T.cd_curso, PS.nm_produto_servico AS nm_curso, count(T.cd_turma) AS QT_TURMAS FROM acd_turma T, grl_produto_servico PS, acd_instituicao B, acd_instituicao_educacenso D, acd_instituicao_periodo E "
					+ "						   WHERE T.cd_instituicao = B.cd_instituicao "
					+ "							 AND T.cd_curso = PS.cd_produto_servico"
					+ "							 AND B.cd_instituicao = D.cd_instituicao"
					+ "							 AND D.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE
					+ "							 AND T.cd_periodo_letivo = E.cd_periodo_letivo"
					+ "							 AND E.cd_periodo_letivo = D.cd_periodo_letivo"
					+ "							 AND B.cd_instituicao <> " + cdSecretaria
					+ (cdInstituicao > 0 ? " AND B.cd_instituicao = " + cdInstituicao : "")
					+ "							 AND (E.cd_periodo_letivo = "+cdPeriodoLetivo+" OR E.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")" 
					+ "							 AND T.nm_turma <> 'TRANSFERENCIA'"
					+ "						  GROUP BY T.cd_curso, PS.nm_produto_servico"
					+ "						  ORDER BY PS.nm_produto_servico");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int qtTurmas1Ano = 0;
			int qtTurmas2Ano = 0;
			int qtTurmas3Ano = 0;
			int qtTurmas4Ano = 0;
			int qtTurmas5Ano = 0;
			int qtTurmas6Ano = 0;
			int qtTurmas7Ano = 0;
			int qtTurmas8Ano = 0;
			int qtTurmas9Ano = 0;
			int qtTurmasRegular = 0;
			int qtTurmasEJA = 0;
			int qtTurmasMulti = 0;
			while(rsm.next()){
				Curso curso = CursoDAO.get(rsm.getInt("cd_curso"), connect);
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_curso", "" + curso.getCdCurso(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmCursoEtapa = CursoEtapaDAO.find(criterios, connect);
				CursoEtapa cursoEtapa = null;
				TipoEtapa tipoEtapa = null;
				if(rsmCursoEtapa.next()){
					cursoEtapa = CursoEtapaDAO.get(rsmCursoEtapa.getInt("cd_curso_etapa"), connect);
					tipoEtapa = TipoEtapaDAO.get(rsmCursoEtapa.getInt("cd_etapa"), connect);
				}
				
				if(tipoEtapa.getLgRegular() == 1 && curso.getNrOrdem() == 0){
					qtTurmas1Ano+=rsm.getInt("QT_TURMAS");
					qtTurmasRegular+=rsm.getInt("QT_TURMAS");
				}
				else if(tipoEtapa.getLgRegular() == 1 && curso.getNrOrdem() == 1){
					qtTurmas2Ano+=rsm.getInt("QT_TURMAS");
					qtTurmasRegular+=rsm.getInt("QT_TURMAS");
				}
				else if(tipoEtapa.getLgRegular() == 1 && curso.getNrOrdem() == 2){
					qtTurmas3Ano+=rsm.getInt("QT_TURMAS");
					qtTurmasRegular+=rsm.getInt("QT_TURMAS");
				}
				else if(tipoEtapa.getLgRegular() == 1 && curso.getNrOrdem() == 3){
					qtTurmas4Ano+=rsm.getInt("QT_TURMAS");
					qtTurmasRegular+=rsm.getInt("QT_TURMAS");
				}
				else if(tipoEtapa.getLgRegular() == 1 && curso.getNrOrdem() == 4){
					qtTurmas5Ano+=rsm.getInt("QT_TURMAS");
					qtTurmasRegular+=rsm.getInt("QT_TURMAS");
				}
				else if(tipoEtapa.getLgRegular() == 1 && curso.getNrOrdem() == 5){
					qtTurmas6Ano+=rsm.getInt("QT_TURMAS");
					qtTurmasRegular+=rsm.getInt("QT_TURMAS");
				}
				else if(tipoEtapa.getLgRegular() == 1 && curso.getNrOrdem() == 6){
					qtTurmas7Ano+=rsm.getInt("QT_TURMAS");
					qtTurmasRegular+=rsm.getInt("QT_TURMAS");
				}
				else if(tipoEtapa.getLgRegular() == 1 && curso.getNrOrdem() == 7){
					qtTurmas8Ano+=rsm.getInt("QT_TURMAS");
					qtTurmasRegular+=rsm.getInt("QT_TURMAS");
				}
				else if(tipoEtapa.getLgRegular() == 1 && curso.getNrOrdem() == 8){
					qtTurmas9Ano+=rsm.getInt("QT_TURMAS");
					qtTurmasRegular+=rsm.getInt("QT_TURMAS");
				}
				else if(tipoEtapa.getLgEja() == 1){
					qtTurmasEJA+=rsm.getInt("QT_TURMAS");
				}
				
				if(curso.getLgMulti()==1){
					qtTurmasMulti+=rsm.getInt("QT_TURMAS");
				}
				
			
			}
			rsm.beforeFirst();
			Result result = new Result(1, "Sucesso ao buscar", "RSM", rsm);
			result.addObject("MENSAGEM_ESTATISTICA", "Número de turmas de 1º ano regular: " + qtTurmas1Ano + "\n"
													+"Número de turmas de 2º ano: " + qtTurmas2Ano + "\n"
													+"Número de turmas de 3º ano: " + qtTurmas3Ano + "\n"
													+"Número de turmas de 4º ano: " + qtTurmas4Ano + "\n"
													+"Número de turmas de 5º ano: " + qtTurmas5Ano + "\n"
													+"Número de turmas de 6º ano: " + qtTurmas6Ano + "\n"
													+"Número de turmas de 7º ano: " + qtTurmas7Ano + "\n"
													+"Número de turmas de 8º ano: " + qtTurmas8Ano + "\n"
													+"Número de turmas de 9º ano: " + qtTurmas9Ano + "\n"
													+"Número de turmas de EJA: " + qtTurmasEJA + "\n"
													+"Número de turmas Multi: " + qtTurmasMulti + "\n");
			return result;			
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static Result fecharTurmaSituacaoCenso(int cdTurma, int cdUsuario) {
		return fecharTurmaSituacaoCenso(cdTurma, cdUsuario, null);
	}
	
	public static Result fecharTurmaSituacaoCenso(int cdTurma, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			int cdTipoOcorrenciaFechamentoTurmaSituacaoCenso = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaFechamentoTurmaSituacaoCenso > 0){
				Turma turma = TurmaDAO.get(cdTurma, connect);
				Curso curso = CursoDAO.get(turma.getCdCurso(), connect);
				Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(turma.getCdPeriodoLetivo(), connect);
				
				OcorrenciaTurma ocorrencia = new OcorrenciaTurma(0, 0, "Turma " + curso.getNmProdutoServico() + " - " + turma.getNmTurma() + " de " + instituicaoPeriodo.getNmPeriodoLetivo() +" da escola " + instituicao.getNmPessoa() + " fechada para a Situação do Censo", Util.getDataAtual(), cdTipoOcorrenciaFechamentoTurmaSituacaoCenso, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, turma.getCdTurma(), Util.getDataAtual(), cdUsuario, turma.getStTurma(), turma.getStTurma());
				Result result = OcorrenciaTurmaServices.save(ocorrencia, connect);
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Fechamento da turma feito com sucesso");
			return result;
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result liberarTurmaSituacaoCenso(int cdTurma, int cdUsuario) {
		return liberarTurmaSituacaoCenso(cdTurma, cdUsuario, null);
	}
	
	public static Result liberarTurmaSituacaoCenso(int cdTurma, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		
			int cdTipoOcorrenciaFechamentoTurmaSituacaoCenso = com.tivic.manager.grl.TipoOcorrenciaServices.getById(InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO, connect).getCdTipoOcorrencia();
			if(cdTipoOcorrenciaFechamentoTurmaSituacaoCenso > 0){
				Turma turma = TurmaDAO.get(cdTurma, connect);
				Curso curso = CursoDAO.get(turma.getCdCurso(), connect);
				Instituicao instituicao = InstituicaoDAO.get(turma.getCdInstituicao(), connect);
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(turma.getCdPeriodoLetivo(), connect);
				
				OcorrenciaTurma ocorrencia = new OcorrenciaTurma(0, 0, "Turma " + curso.getNmProdutoServico() + " - " + turma.getNmTurma() + " de " + instituicaoPeriodo.getNmPeriodoLetivo() +" da escola " + instituicao.getNmPessoa() + " liberada para a Situação do Censo", Util.getDataAtual(), cdTipoOcorrenciaFechamentoTurmaSituacaoCenso, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario, turma.getCdTurma(), Util.getDataAtual(), cdUsuario, turma.getStTurma(), turma.getStTurma());
				Result result = OcorrenciaTurmaServices.save(ocorrencia, connect);
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Liberação da turma feita com sucesso");
			return result;
		}
		
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo que busca a posição da turma em relação ao curso, periodo letivo e instituicao. Usado para saber se a turma é A, B, C, D... mesmo que os nomes tenham mudado
	 * @param cdTurma
	 * @param connection
	 * @return
	 */
	public static int getOrdemTurma(int cdTurma, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			Turma turma = TurmaDAO.get(cdTurma, connection);
			
			ResultSetMap rsmTurmas = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_periodo_letivo = " + turma.getCdPeriodoLetivo() + " AND cd_curso = " + turma.getCdCurso() + " ORDER BY cd_turma").executeQuery());
			int posicao = 0;
			while(rsmTurmas.next()){
				if(rsmTurmas.getInt("cd_turma") == turma.getCdTurma()){
					return posicao;
				}
				
				posicao++;
			}
			
			return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro na turma: " + e.getMessage());
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result getResumoRendimentoByTurmas(int cdCirculo, int cdInstituicao) {
		return getResumoRendimentoByTurmas(cdCirculo, cdInstituicao, null);
	}
	
	public static Result getResumoRendimentoByTurmas(int cdCirculo, int cdInstituicao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			Result result = new Result(1, "Sucesso ao pesquisar resumo");
			
			if(cdInstituicao > 0){
				result = calculoResumoRendimentoByTurmas(cdInstituicao, connection);
				result.addObject("QT_INSTITUICOES", 1);
			}
			else{
				
				ResultSetMap rsmTotal = new ResultSetMap();
				float qtTotalMatriculas = 0;
				float prTotalNotas = 0;
				float prTotalFrequencia = 0;
				
				ResultSetMap rsmInstituicoes = InstituicaoServices.getAllByCirculo(cdCirculo, connection);
				while(rsmInstituicoes.next()){
					Result resultCalculo = calculoResumoRendimentoByTurmas(rsmInstituicoes.getInt("cd_instituicao"), connection);
					
					ResultSetMap rsmCalculo = (ResultSetMap) resultCalculo.getObjects().get("rsm");
					rsmTotal.getLines().addAll(rsmCalculo.getLines());
					
					qtTotalMatriculas += Integer.parseInt(String.valueOf(resultCalculo.getObjects().get("QT_MATRICULAS")));
					prTotalNotas += Float.parseFloat(String.valueOf(resultCalculo.getObjects().get("PR_NOTAS")));
					prTotalFrequencia += Float.parseFloat(String.valueOf(resultCalculo.getObjects().get("PR_FREQUENCIA")));
				}
				rsmInstituicoes.beforeFirst();
				
				rsmTotal.beforeFirst();
				
				result.addObject("rsm", rsmTotal);
				
				result.addObject("QT_INSTITUICOES", rsmInstituicoes.size());
				
				result.addObject("QT_MATRICULAS", qtTotalMatriculas);
				
				result.addObject("PR_NOTAS", (prTotalNotas/rsmInstituicoes.size()));
				result.addObject("CL_NOTAS", Util.formatNumber((Float.parseFloat(String.valueOf(result.getObjects().get("PR_NOTAS")))*100), 2) + "%");
				
				result.addObject("PR_FREQUENCIA", (prTotalFrequencia/rsmInstituicoes.size()));
				result.addObject("CL_FREQUENCIA", Util.formatNumber((Float.parseFloat(String.valueOf(result.getObjects().get("PR_FREQUENCIA")))*100), 2) + "%");
				
				result.addObject("PR_RENDIMENTO", Float.parseFloat(String.valueOf(result.getObjects().get("PR_NOTAS"))) * Float.parseFloat(String.valueOf(result.getObjects().get("PR_FREQUENCIA"))));
				result.addObject("CL_RENDIMENTO", Util.formatNumber((Float.parseFloat(String.valueOf(result.getObjects().get("PR_RENDIMENTO")))*100), 2) + "%");
			}
			
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro na turma: " + e.getMessage());
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result calculoResumoRendimentoByTurmas(int cdInstituicao, Connection connection){
		boolean isConnectionNull = connection == null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoSecretaria = 0;
			ResultSetMap rsmPeriodoSecretaria = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connection);
			if(rsmPeriodoSecretaria.next())
				cdPeriodoSecretaria = rsmPeriodoSecretaria.getInt("cd_periodo_letivo");
			
			String sql = "SELECT T.cd_turma, INS_P.nm_pessoa AS NM_INSTITUICAO, (CUR_PROD.nm_produto_servico || ' - ' || T.nm_turma) AS NM_TURMA_COMPLETO FROM acd_turma T "
					+ "  JOIN acd_instituicao INS ON (T.cd_instituicao = INS.cd_instituicao)"
					+ "  JOIN acd_instituicao_periodo INS_PER ON (INS.cd_instituicao = INS_PER.cd_instituicao)"
					+ "  JOIN grl_pessoa INS_P ON (INS.cd_instituicao = INS_P.cd_pessoa)"
					+ "  JOIN grl_produto_servico CUR_PROD ON (T.cd_curso = CUR_PROD.cd_produto_servico)"
					+ "  WHERE INS_PER.cd_periodo_letivo_superior = " + cdPeriodoSecretaria
					+ "    AND T.cd_instituicao = " + cdInstituicao
					+ "    AND T.st_turma = " + ST_ATIVO
					+ "  ORDER BY INS_P.nm_pessoa, CUR_PROD.nm_produto_servico, T.nm_turma";
			
			int qtTotalMatriculas = 0;
			float prTotalNotas = 0;
			float prTotalFrequencia = 0;
			
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			while(rsm.next()){
				
				float prNotasTurma = 0;
				float prFrequenciaTurma = 0;
				
				ResultSetMap rsmMatriculas = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_matricula M "
						+ "																	WHERE M.cd_turma = " + rsm.getInt("cd_turma") 
						+ " 																  AND M.st_matricula = " + MatriculaServices.ST_ATIVA).executeQuery());
				while(rsmMatriculas.next()){
					float prNotasMatricula = DisciplinaAvaliacaoAlunoServices.getMediaNotaByTurma(rsmMatriculas.getInt("cd_matricula"), rsm.getInt("cd_turma"), connection);
					prNotasTurma += prNotasMatricula;
					
					float prFrequenciaMatricula = AulaMatriculaServices.getMediaFrequenciaByTurma(rsmMatriculas.getInt("cd_matricula"), rsm.getInt("cd_turma"), connection);
					prFrequenciaTurma += prFrequenciaMatricula;
				}
				rsmMatriculas.beforeFirst();
				
				rsm.setValueToField("QT_MATRICULAS", rsmMatriculas.size());
				qtTotalMatriculas += rsmMatriculas.size();
				
				rsm.setValueToField("PR_NOTAS", (rsmMatriculas.size() == 0 ? 0 : (prNotasTurma/rsmMatriculas.size())));
				prTotalNotas += rsm.getFloat("PR_NOTAS");
				rsm.setValueToField("CL_NOTAS", Util.formatNumber((rsm.getFloat("PR_NOTAS")*100), 2) + "%");
				
				rsm.setValueToField("PR_FREQUENCIA", (rsmMatriculas.size() == 0 ? 0 : (prFrequenciaTurma/rsmMatriculas.size())));
				prTotalFrequencia += rsm.getFloat("PR_FREQUENCIA");
				rsm.setValueToField("CL_FREQUENCIA", Util.formatNumber((rsm.getFloat("PR_FREQUENCIA")*100), 2) + "%");
				
				rsm.setValueToField("PR_RENDIMENTO", (rsm.getFloat("PR_NOTAS")*rsm.getFloat("PR_FREQUENCIA")));
				rsm.setValueToField("CL_RENDIMENTO", Util.formatNumber((rsm.getFloat("PR_RENDIMENTO")*100), 2) + "%");
				
			}
			rsm.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao calcular");
			result.addObject("rsm", rsm);
			
			result.addObject("QT_MATRICULAS", qtTotalMatriculas);
			
			result.addObject("PR_NOTAS", (rsm.size() == 0 ? 0 : (prTotalNotas/rsm.size())));
			result.addObject("CL_NOTAS", Util.formatNumber((Float.parseFloat(String.valueOf(result.getObjects().get("PR_NOTAS")))*100), 2) + "%");
			
			result.addObject("PR_FREQUENCIA", (rsm.size() == 0 ? 0 : (prTotalFrequencia/rsm.size())));
			result.addObject("CL_FREQUENCIA", Util.formatNumber((Float.parseFloat(String.valueOf(result.getObjects().get("PR_FREQUENCIA")))*100), 2) + "%");
			
			result.addObject("PR_RENDIMENTO", Float.parseFloat(String.valueOf(result.getObjects().get("PR_NOTAS"))) * Float.parseFloat(String.valueOf(result.getObjects().get("PR_FREQUENCIA"))));
			result.addObject("CL_RENDIMENTO", Util.formatNumber((Float.parseFloat(String.valueOf(result.getObjects().get("PR_RENDIMENTO")))*100), 2) + "%");
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro na turma: " + e.getMessage());
			return new Result(-1);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result getResumoRendimentoByDisciplinaCursos(int cdCirculo, int cdInstituicao) {
		return getResumoRendimentoByDisciplinaCursos(cdCirculo, cdInstituicao, null);
	}
	
	public static Result getResumoRendimentoByDisciplinaCursos(int cdCirculo, int cdInstituicao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			Result result = new Result(1, "Sucesso ao pesquisar resumo");
			
			if(cdInstituicao > 0){
				result = calculoResumoRendimentoByDisciplinaCursos(cdInstituicao, connection);
				result.addObject("QT_INSTITUICOES", 1);
			}
			else{
				
				ResultSetMap rsmTotal = new ResultSetMap();
				int qtTotalMatriculas = 0;
				float prTotalNotas = 0;
				float prTotalFrequencia = 0;
				
				ResultSetMap rsmInstituicoes = InstituicaoServices.getAllByCirculo(cdCirculo, connection);
				while(rsmInstituicoes.next()){
					Result resultCalculo = calculoResumoRendimentoByDisciplinaCursos(rsmInstituicoes.getInt("cd_instituicao"), connection);
					
					ResultSetMap rsmCalculo = (ResultSetMap) resultCalculo.getObjects().get("rsm");
					rsmTotal.getLines().addAll(rsmCalculo.getLines());
					
					qtTotalMatriculas += Integer.parseInt(String.valueOf(resultCalculo.getObjects().get("QT_MATRICULAS")));
					prTotalNotas += Float.parseFloat(String.valueOf(resultCalculo.getObjects().get("PR_NOTAS")));
					prTotalFrequencia += Float.parseFloat(String.valueOf(resultCalculo.getObjects().get("PR_FREQUENCIA")));
				}
				rsmInstituicoes.beforeFirst();
				
				rsmTotal.beforeFirst();
				
				result.addObject("rsm", rsmTotal);
				
				result.addObject("QT_INSTITUICOES", rsmInstituicoes.size());
				
				result.addObject("QT_MATRICULAS", qtTotalMatriculas);
				
				result.addObject("PR_NOTAS", (prTotalNotas/rsmInstituicoes.size()));
				result.addObject("CL_NOTAS", Util.formatNumber((Float.parseFloat(String.valueOf(result.getObjects().get("PR_NOTAS")))*100), 2) + "%");
				
				result.addObject("PR_FREQUENCIA", (prTotalFrequencia/rsmInstituicoes.size()));
				result.addObject("CL_FREQUENCIA", Util.formatNumber((Float.parseFloat(String.valueOf(result.getObjects().get("PR_FREQUENCIA")))*100), 2) + "%");
				
				result.addObject("PR_RENDIMENTO", Float.parseFloat(String.valueOf(result.getObjects().get("PR_NOTAS"))) * Float.parseFloat(String.valueOf(result.getObjects().get("PR_FREQUENCIA"))));
				result.addObject("CL_RENDIMENTO", Util.formatNumber((Float.parseFloat(String.valueOf(result.getObjects().get("PR_RENDIMENTO")))*100), 2) + "%");
			}
			
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro na turma: " + e.getMessage());
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result calculoResumoRendimentoByDisciplinaCursos(int cdInstituicao, Connection connection){
		boolean isConnectionNull = connection == null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			int cdPeriodoSecretaria = 0;
			ResultSetMap rsmPeriodoSecretaria = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdSecretaria, connection);
			if(rsmPeriodoSecretaria.next())
				cdPeriodoSecretaria = rsmPeriodoSecretaria.getInt("cd_periodo_letivo");
			
			String sql = "SELECT O.cd_disciplina, O.cd_curso FROM acd_oferta O "
					+ "  JOIN acd_turma T ON (O.cd_turma = T.cd_turma)"
					+ "  JOIN acd_disciplina DIS ON (O.cd_disciplina = DIS.cd_disciplina)"
					+ "  JOIN grl_produto_servico DIS_PROD ON (DIS.cd_disciplina = DIS_PROD.cd_produto_servico)"
					+ "  JOIN acd_instituicao INS ON (T.cd_instituicao = INS.cd_instituicao)"
					+ "  JOIN acd_instituicao_periodo INS_PER ON (INS.cd_instituicao = INS_PER.cd_instituicao)"
					+ "  JOIN grl_pessoa INS_P ON (INS.cd_instituicao = INS_P.cd_pessoa)"
					+ "  JOIN grl_produto_servico CUR_PROD ON (T.cd_curso = CUR_PROD.cd_produto_servico)"
					+ "  WHERE INS_PER.cd_periodo_letivo_superior = " + cdPeriodoSecretaria
					+ "    AND T.cd_instituicao = " + cdInstituicao
					+ "    AND T.st_turma = " + ST_ATIVO
					+ "    AND O.st_oferta = " + OfertaServices.ST_ATIVO
					+ "  GROUP BY O.cd_disciplina, O.cd_curso";
			
			Instituicao instituicao = InstituicaoDAO.get(cdInstituicao, connection);
			
			float prTotalNotas = 0;
			float prTotalFrequencia = 0;
			
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement(sql).executeQuery());
			while(rsm.next()){
				
				Curso curso = CursoDAO.get(rsm.getInt("cd_curso"), connection);
				Disciplina disciplina = DisciplinaDAO.get(rsm.getInt("cd_disciplina"), connection);
				
				float prNotas = DisciplinaAvaliacaoAlunoServices.getMediaNotaByDisciplinaCursos(rsm.getInt("cd_disciplina"), rsm.getInt("cd_curso"), cdInstituicao, connection);
				prTotalNotas += prNotas;
				
				float prFrequencia = AulaMatriculaServices.getMediaFrequenciaByDisciplinaCursos(rsm.getInt("cd_disciplina"), rsm.getInt("cd_curso"), cdInstituicao, connection);
				prTotalFrequencia += prFrequencia;
				
				rsm.setValueToField("PR_NOTAS", prNotas);
				rsm.setValueToField("CL_NOTAS", Util.formatNumber((rsm.getFloat("PR_NOTAS")*100), 2) + "%");
				
				rsm.setValueToField("PR_FREQUENCIA", prFrequencia);
				rsm.setValueToField("CL_FREQUENCIA", Util.formatNumber((rsm.getFloat("PR_FREQUENCIA")*100), 2) + "%");
				
				rsm.setValueToField("PR_RENDIMENTO", (rsm.getFloat("PR_NOTAS")*rsm.getFloat("PR_FREQUENCIA")));
				rsm.setValueToField("CL_RENDIMENTO", Util.formatNumber((rsm.getFloat("PR_RENDIMENTO")*100), 2) + "%");
				
				rsm.setValueToField("NM_INSTITUICAO", instituicao.getNmPessoa());
				rsm.setValueToField("NM_CURSO", curso.getNmProdutoServico());
				rsm.setValueToField("NM_DISCIPLINA", disciplina.getNmProdutoServico());
				
				
			}
			rsm.beforeFirst();
			
			Result result = new Result(1, "Sucesso ao calcular");
			result.addObject("rsm", rsm);
			
			ResultSetMap rsmMatriculasInstituicao = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_matricula M, acd_turma T WHERE M.cd_turma = T.cd_turma AND M.st_matricula = " + MatriculaServices.ST_ATIVA + " AND T.st_turma = " + TurmaServices.ST_ATIVO + " AND T.cd_instituicao = " + cdInstituicao).executeQuery());
			result.addObject("QT_MATRICULAS", rsmMatriculasInstituicao.size());
			
			result.addObject("PR_NOTAS", (rsm.size() == 0 ? 0 : (prTotalNotas/rsm.size())));
			result.addObject("CL_NOTAS", Util.formatNumber((Float.parseFloat(String.valueOf(result.getObjects().get("PR_NOTAS")))*100), 2) + "%");
			
			result.addObject("PR_FREQUENCIA", (rsm.size() == 0 ? 0 : (prTotalFrequencia/rsm.size())));
			result.addObject("CL_FREQUENCIA", Util.formatNumber((Float.parseFloat(String.valueOf(result.getObjects().get("PR_FREQUENCIA")))*100), 2) + "%");
			
			result.addObject("PR_RENDIMENTO", Float.parseFloat(String.valueOf(result.getObjects().get("PR_NOTAS"))) * Float.parseFloat(String.valueOf(result.getObjects().get("PR_FREQUENCIA"))));
			result.addObject("CL_RENDIMENTO", Util.formatNumber((Float.parseFloat(String.valueOf(result.getObjects().get("PR_RENDIMENTO")))*100), 2) + "%");
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.out.println("Erro na turma: " + e.getMessage());
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static boolean verificarCompatibilidadeTurmaMatricula(Curso cursoTurma, Curso cursoMatricula, Connection connect){
		if(cursoTurma.getLgMulti() == 1){
			CursoMulti cursoMulti = CursoMultiDAO.get(cursoTurma.getCdCurso(), cursoMatricula.getCdCurso(), connect);
			if(cursoMulti == null){
				return false;
			}
		}
		else{
			if(cursoMatricula.getCdCurso() != cursoTurma.getCdCurso()){
				return false;
			}
		}
		
		return true;
	}
	
	public static ValidatorResult validate(Turma turma, int idGrupo){
		return validate(turma, null, null, true, false, 0, idGrupo, null);
	}
	
	public static ValidatorResult validate(Turma turma, int idGrupo, Connection connect){
		return validate(turma, null, null, true, false, 0, idGrupo, connect);
	}
	
	public static ValidatorResult validate(Turma turma, ArrayList<Integer> atividadesComplementares, ArrayList<Integer> atendimentosEspecializados, boolean verificarQuadroVagas, boolean isInsert, int cdUsuario, int idGrupo){
		return validate(turma, atividadesComplementares, atendimentosEspecializados, verificarQuadroVagas, isInsert, cdUsuario, idGrupo, null);
	}
	
	public static ValidatorResult validate(Turma turma, ArrayList<Integer> atividadesComplementares, ArrayList<Integer> atendimentosEspecializados, boolean verificarQuadroVagas, boolean isInsert, int cdUsuario, int idGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		
		try {
		
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(turma == null){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new ValidatorResult(ValidatorResult.ERROR, "Turma não encontrada");
			}
			
			ValidatorResult result = new ValidatorResult(ValidatorResult.VALID, "Turma passou pela validação");
			HashMap<Integer, Validator> listValidator = getListValidation();
			
			ResultSetMap rsmDisciplinas = TurmaServices.getDisciplinas(turma.getCdTurma());
			ResultSetMap rsmHorarios = TurmaServices.getHorariosByTurma(turma.getCdTurma());
			ResultSetMap rsmProfessores = TurmaServices.getProfissionais(turma.getCdTurma());
			ResultSetMap rsmAlunos = TurmaServices.getAlunos(turma.getCdTurma(), true);
			
			Curso curso = CursoDAO.get(turma.getCdCurso(), connect);
			
			if(turma.getNmTurma() == null || turma.getNmTurma().trim().equals("") || !turma.getNmTurma().matches("[ A-Z0-9ªº-]+")){
				listValidator.get(VALIDATE_NOME_FALTANDO_INVALIDO).add(ValidatorResult.ERROR, "Faltando nome ou o mesmo é inválido");
			}
			
			//NOME - Verificar se o nome foi marcado como Inativo
			if(turma.getNmTurma().contains("INATIVA") || turma.getNmTurma().contains("Inativa") || turma.getNmTurma().contains("inativa") ||
					   turma.getNmTurma().contains("INATIVO") || turma.getNmTurma().contains("Inativo") || turma.getNmTurma().contains("inativo")){
				listValidator.get(VALIDATE_NOME_INATIVO).add(ValidatorResult.ERROR, "Nome da turma esta registrado como 'Inativo'");
			}
			
			//PERIODO LETIVO - Verificar se a turma possui periodo letivo
			if(turma.getCdPeriodoLetivo() == 0){
				listValidator.get(VALIDATE_PERIODO_LETIVO).add(ValidatorResult.ERROR, "Faltando período letivo");
			}
			
			//TURNO - Verifica se a turma possui um turno válido
			if(turma.getTpTurno() != TP_MATUTINO && turma.getTpTurno() != TP_VESPERTINO && turma.getTpTurno() != TP_NOTURNO && turma.getTpTurno() != TP_INTEGRAL){
				listValidator.get(VALIDATE_TURNO).add(ValidatorResult.ERROR, "Faltando turno");
			}
			
			//CURSO = Verifica se a turma possui curso
			if(turma.getCdCurso() == 0){
				listValidator.get(VALIDATE_CURSO).add(ValidatorResult.ERROR, "Faltando curso");
			}
			
			//QUANTIDADE DE VAGAS - Verifica se a quantidade de vagas é maior do que zero
			if(turma.getQtVagas() == 0){
				listValidator.get(VALIDATE_VAGAS).add(ValidatorResult.ERROR, "Quantidade de vagas está zerada");
			}
			
			//TIPO DE ATENDIMENTO - Verifica se o tipo de atendimento possui um valor válido
			if(turma.getTpAtendimento() < TP_ATENDIMENTO_NAO_APLICA || turma.getTpAtendimento() > TP_ATENDIMENTO_AEE){
				listValidator.get(VALIDATE_TIPO_ATENDIMENTO).add(ValidatorResult.ERROR, "Tipo de atendimento está com valor inválido");
			}
			
			//TIPO DE MODALIDADE DE ENSINO - Verifica se o tipo de modalidade de ensino possui um valor válido/
			if(turma.getTpModalidadeEnsino() < TP_MODALIDADE_REGULAR || turma.getTpModalidadeEnsino() > TP_MODALIDADE_EJA){
				listValidator.get(VALIDATE_TIPO_MODALIDADE_ENSINO).add(ValidatorResult.ERROR, "Tipo de modalidade de ensino está com valor inválido");
			}
			
			//DISCIPLINAS - Verifica se existe disciplinas cadastradas na turma
			if((rsmDisciplinas == null || rsmDisciplinas.size() == 0)
					&& !curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_CRECHE) 
					&& !curso.getIdProdutoServico().substring(0, 2).equals(CursoServices.TP_ETAPA_CURSO_PRE_ESCOLA) 
					&& curso.getCdCurso() != ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0) 
					&& curso.getCdCurso() != ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)
					&& curso.getCdCurso() != ParametroServices.getValorOfParametroAsInteger("CD_CURSO_PROJOVEM", 0)){
				listValidator.get(VALIDATE_DISCIPLINAS).add(ValidatorResult.ERROR, "Faltando todas as disciplinas necessárias", GRUPO_VALIDACAO_EDUCACENSO);
				listValidator.get(VALIDATE_DISCIPLINAS).add(ValidatorResult.ALERT, "Faltando todas as disciplinas necessárias", GRUPO_VALIDACAO_UPDATE);
			}
			
			String disciplinasSemDocente = "";
			
			while(rsmDisciplinas.next()){
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_turma", "" + turma.getCdTurma(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("A.cd_disciplina", rsmDisciplinas.getString("cd_disciplina"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("A.st_oferta", "" + OfertaServices.ST_ATIVO, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmOferta = OfertaServices.find(criterios);
				if(rsmOferta.next()){
					
					
					boolean possuiDocente = false;
					ResultSetMap rsmPessoaOferta = PessoaOfertaServices.getAllByOferta(rsmOferta.getInt("cd_oferta"), connect);
					while(rsmPessoaOferta.next()){
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_empresa", "" + turma.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("nm_setor", "CORPO DOCENTE", ItemComparator.EQUAL, Types.VARCHAR));
						ResultSetMap rsmSetor = SetorDAO.find(criterios, connect);
						Setor setor = null;
						if(rsmSetor.next()){
							setor = SetorDAO.get(rsmSetor.getInt("cd_setor"), connect);
						}
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_pessoa", "" + rsmPessoaOferta.getInt("cd_pessoa"), ItemComparator.EQUAL, Types.INTEGER));
						DadosFuncionais dadosFuncionais = null;
						ResultSetMap rsmDadosFuncionais = DadosFuncionaisDAO.find(criterios, connect);
						if(rsmDadosFuncionais.next()){
							dadosFuncionais = DadosFuncionaisDAO.get(rsmDadosFuncionais.getInt("cd_matricula"), connect);
						}
						criterios = new ArrayList<ItemComparator>();
						criterios.add(new ItemComparator("cd_setor", "" + setor.getCdSetor(), ItemComparator.EQUAL, Types.INTEGER));
						criterios.add(new ItemComparator("cd_matricula", "" + dadosFuncionais.getCdMatricula(), ItemComparator.EQUAL, Types.INTEGER));
						Lotacao lotacao = null;
						ResultSetMap rsmLotacao = LotacaoDAO.find(criterios, connect);
						if(rsmLotacao.next()){
							lotacao = LotacaoDAO.get(rsmLotacao.getInt("cd_lotacao"), rsmLotacao.getInt("cd_matricula"), connect);
						}
						
						if(lotacao != null){
							Funcao funcaoProfessor = FuncaoDAO.get(lotacao.getCdFuncao(), connect);
							
							if(funcaoProfessor.getIdFuncao().equals(FuncaoServices.ID_PROFESSOR)){
								possuiDocente = true;
								break;
							}
							
						}
					}
					
					if(!possuiDocente){
						disciplinasSemDocente += rsmDisciplinas.getString("nm_disciplina") + ", ";
					}
					
					
					
				}
				
			}
			
			if(disciplinasSemDocente.length() > 0){
				disciplinasSemDocente = disciplinasSemDocente.substring(0, disciplinasSemDocente.length()-2);
				listValidator.get(VALIDATE_OFERTA_SEM_DOCENTE).add(ValidatorResult.ALERT, "As disciplinas " + disciplinasSemDocente + " não possuem docente cadastrado para a turma " + curso.getNmProdutoServico() + " - " + turma.getNmTurma(), GRUPO_VALIDACAO_EDUCACENSO);
			}
			
			//HORARIOS - Verifica se a turma possui horários cadastrados
			if((rsmHorarios == null || rsmHorarios.size() == 0)){
				listValidator.get(VALIDATE_HORARIOS).add(ValidatorResult.ERROR, "Faltando horários de funcionamento", GRUPO_VALIDACAO_EDUCACENSO);
				listValidator.get(VALIDATE_HORARIOS).add(ValidatorResult.ALERT, "Faltando horários de funcionamento", GRUPO_VALIDACAO_UPDATE);
			}
			
			//PROFESSORES - Verifica se a turma possui professores cadastrados
			if((rsmProfessores == null || rsmProfessores.size() == 0)){
				listValidator.get(VALIDATE_PROFESSORES).add(ValidatorResult.ERROR, "Faltando enturmação de professores", GRUPO_VALIDACAO_EDUCACENSO);
				listValidator.get(VALIDATE_PROFESSORES).add(ValidatorResult.ALERT, "Faltando enturmação de professores", GRUPO_VALIDACAO_UPDATE);
			}
			
			//CRECHE E EDUCACAO INFANTIL - Verifica se existe disciplinas cadastradas para creche e educação infantil
			if(CursoServices.isCreche(turma.getCdCurso(), connect) || CursoServices.isEducacaoInfantil(turma.getCdCurso(), connect)){
				ResultSetMap rsmOfertas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_oferta WHERE cd_turma = " + turma.getCdTurma()).executeQuery());
				while(rsmOfertas.next()){
					if(rsmOfertas.getInt("cd_disciplina") > 0){
						listValidator.get(VALIDATE_DISCIPLINAS_INFANTIL).add(ValidatorResult.ERROR, "Turma de Creche/Educação Infantil com disciplinas cadastradas", GRUPO_VALIDACAO_EDUCACENSO);
						listValidator.get(VALIDATE_DISCIPLINAS_INFANTIL).add(ValidatorResult.ALERT, "Turma de Creche/Educação Infantil com disciplinas cadastradas", GRUPO_VALIDACAO_UPDATE);
						break;
					}
				}
			}
			
			//CURSO DO FUNDAMENTAL 1
			if((CursoServices.isFundamentalRegular(turma.getCdCurso()) && curso.getNrOrdem() < 5)){
				
				//Disciplinas da Base Comum:TODO
				ArrayList<Integer> codigosDisciplinaFund1 = new ArrayList<Integer>();
				codigosDisciplinaFund1.add(207);
				codigosDisciplinaFund1.add(209);
				codigosDisciplinaFund1.add(210);
				codigosDisciplinaFund1.add(215);
				codigosDisciplinaFund1.add(216);
				codigosDisciplinaFund1.add(217);
				codigosDisciplinaFund1.add(218);
				codigosDisciplinaFund1.add(228);
				
				ResultSetMap rsmOfertas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_oferta WHERE cd_turma = " + turma.getCdTurma() + " AND st_oferta = " + OfertaServices.ST_ATIVO).executeQuery());
				String nmDisciplinasExcesso = "";
				while(rsmOfertas.next()){
					Disciplina disciplina = DisciplinaDAO.get(rsmOfertas.getInt("cd_disciplina"), connect);
					if(codigosDisciplinaFund1.contains(rsmOfertas.getInt("cd_disciplina"))){
						codigosDisciplinaFund1.remove(new Integer(rsmOfertas.getInt("cd_disciplina")));
					}
					else{
						nmDisciplinasExcesso += disciplina.getNmProdutoServico() + ", ";
					}
						
				}
				if(!nmDisciplinasExcesso.equals("")){
					nmDisciplinasExcesso = nmDisciplinasExcesso.substring(0, nmDisciplinasExcesso.length()-2);
					listValidator.get(VALIDATE_DISCIPLINAS_FUNDAMENTAL_1_EXCESSO).add(ValidatorResult.ERROR, "Turma de Fundamental 1 com disciplinas que não fazem parte da Base Nacional - Disciplinas em excesso: " + nmDisciplinasExcesso, GRUPO_VALIDACAO_EDUCACENSO);
					listValidator.get(VALIDATE_DISCIPLINAS_FUNDAMENTAL_1_EXCESSO).add(ValidatorResult.ALERT, "Turma de Fundamental 1 com disciplinas que não fazem parte da Base Nacional", GRUPO_VALIDACAO_UPDATE);
					listValidator.get(VALIDATE_DISCIPLINAS_FUNDAMENTAL_1_EXCESSO).setTxtNotice("Disciplinas em excesso: " + nmDisciplinasExcesso);
				}	
				
				
				if(codigosDisciplinaFund1.size() > 0){
					nmDisciplinasExcesso = "";
					for(Integer codigo : codigosDisciplinaFund1){
						Disciplina disciplina = DisciplinaDAO.get(codigo, connect);
						nmDisciplinasExcesso += disciplina.getNmProdutoServico() + ", ";
					}
					nmDisciplinasExcesso = nmDisciplinasExcesso.substring(0, nmDisciplinasExcesso.length()-2);
					listValidator.get(VALIDATE_DISCIPLINAS_FUNDAMENTAL_1_FALTANTE).add(ValidatorResult.ERROR, "Turma de Fundamental 1 faltando disciplinas da Base Nacional - Disciplinas faltantes: " + nmDisciplinasExcesso, GRUPO_VALIDACAO_EDUCACENSO);
					listValidator.get(VALIDATE_DISCIPLINAS_FUNDAMENTAL_1_FALTANTE).add(ValidatorResult.ALERT, "Turma de Fundamental 1 faltando disciplinas da Base Nacional", GRUPO_VALIDACAO_UPDATE);
					listValidator.get(VALIDATE_DISCIPLINAS_FUNDAMENTAL_1_FALTANTE).setTxtNotice("Disciplinas faltantes: " + nmDisciplinasExcesso);
				}
			}
			
			//CURSO DO FUNDAMENTAL 2
			if((CursoServices.isFundamentalRegular(turma.getCdCurso()) && curso.getNrOrdem() >= 5)){
				
				//Disciplinas da Base Comum
				ArrayList<Integer> codigosDisciplinaFund2 = new ArrayList<Integer>();
				codigosDisciplinaFund2.add(207);
				codigosDisciplinaFund2.add(209);
				codigosDisciplinaFund2.add(210);
				codigosDisciplinaFund2.add(215);
				codigosDisciplinaFund2.add(216);
				codigosDisciplinaFund2.add(217);
				codigosDisciplinaFund2.add(218);
				codigosDisciplinaFund2.add(228);
				
				
				ResultSetMap rsmOfertas = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_oferta WHERE cd_turma = " + turma.getCdTurma()).executeQuery());
				while(rsmOfertas.next()){
					if(codigosDisciplinaFund2.contains(rsmOfertas.getInt("cd_disciplina"))){
						codigosDisciplinaFund2.remove(new Integer(rsmOfertas.getInt("cd_disciplina")));
					}
				}
				
				if(codigosDisciplinaFund2.size() > 0){
					String nmDisciplinasExcesso = "";
					for(Integer codigo : codigosDisciplinaFund2){
						Disciplina disciplina = DisciplinaDAO.get(codigo, connect);
						nmDisciplinasExcesso += disciplina.getNmProdutoServico() + ", ";
					}
					nmDisciplinasExcesso = nmDisciplinasExcesso.substring(0, nmDisciplinasExcesso.length()-2);
					listValidator.get(VALIDATE_DISCIPLINAS_FUNDAMENTAL_2_FALTANTE).add(ValidatorResult.ERROR, "Turma de Fundamental 2 faltando disciplinas da Base Nacional - Disciplinas faltantes: " + nmDisciplinasExcesso, GRUPO_VALIDACAO_EDUCACENSO);
					listValidator.get(VALIDATE_DISCIPLINAS_FUNDAMENTAL_2_FALTANTE).add(ValidatorResult.ALERT, "Turma de Fundamental 2 faltando disciplinas da Base Nacional", GRUPO_VALIDACAO_UPDATE);
					listValidator.get(VALIDATE_DISCIPLINAS_FUNDAMENTAL_2_FALTANTE).setTxtNotice("Disciplinas faltantes: " + nmDisciplinasExcesso);
				}
			}
			
			//NOME DUPLICADO - Verificar se já existe alguma turma da mesma etapa ja informada
//			ResultSetMap rsmTurmasInstituicao = InstituicaoServices.getAllTurmasByInstituicaoPeriodo(turma.getCdInstituicao(), turma.getCdPeriodoLetivo(), false, connect);
//			while(rsmTurmasInstituicao.next()){
//				Turma turmaInstituicao = TurmaDAO.get(rsmTurmasInstituicao.getInt("cd_turma"), connect);
//				if(turma.getCdTurma() != turmaInstituicao.getCdTurma()){
//					Curso cursoInstituicao = CursoDAO.get(turmaInstituicao.getCdCurso(), connect);
//					if(turma.getNmTurma().equals(turmaInstituicao.getNmTurma()) && curso.getIdProdutoServico().substring(0, 2).equals(cursoInstituicao.getIdProdutoServico().substring(0, 2))){
//						listValidator.get(VALIDATE_NOME_DUPLICADO).add(ValidatorResult.ERROR, "Turma de etapa equivalente com o mesmo nome já foi informada");
//						break;
//					}
//				}
//			}
//			rsmTurmasInstituicao.beforeFirst();
			
			
			//AEE - Verificar se foi informado para turma de Atendimento Especializado as atividades do atendimento 
			if(turma.getTpAtendimento() == TP_ATENDIMENTO_AEE){
				if(atendimentosEspecializados != null && atendimentosEspecializados.size() == 0){
					listValidator.get(VALIDATE_ATENDIMENTOS_ESPECIALIZADOS).add(ValidatorResult.ERROR, "Necessário informar qual tipo de atendimento especializado da turma");
				}
								
			}
			else if(atendimentosEspecializados != null && atendimentosEspecializados.size() > 0){
				listValidator.get(VALIDATE_TURMA_NAO_AEE).add(ValidatorResult.ERROR, "Atendimentos especializados só podem ser informados para turmas AEE");
			}
						
			//ATIVIDADE COMPLEMENTAR - Verificar se foi informado para turma de Atividade Complementar as atividades
			if(turma.getTpAtendimento() == TP_ATENDIMENTO_ATIVIDADE_COMPLEMENTAR){
				
				if(atividadesComplementares != null && atividadesComplementares.size() == 0){
					listValidator.get(VALIDATE_ATIVIDADE_COMPLEMENTAR).add(ValidatorResult.ERROR, "Necessário informar qual tipo de atividade complementar da turma");
				}
								
			}
			else if(atividadesComplementares != null && atividadesComplementares.size() > 0){
				listValidator.get(VALIDATE_TURMA_NAO_AC).add(ValidatorResult.ERROR, "Atividades complementares só podem ser informados para turmas de Atividade Complementar");
			}
						
			//QUADRO DE VAGAS - Verificar se o quadro de vagas esta sendo respeitado com qualquer alteração feita
			if(verificarQuadroVagas){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_periodo_letivo", "" + turma.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_instituicao", "" + turma.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmQuadroVagas = QuadroVagasDAO.find(criterios, connect);
				if(rsmQuadroVagas.next()){
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_quadro_vagas", rsmQuadroVagas.getString("cd_quadro_vagas"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_curso", "" + turma.getCdCurso(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("tp_turno", "" + turma.getTpTurno(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_instituicao", "" + turma.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmQuadroVagasCurso = QuadroVagasCursoDAO.find(criterios, connect);
					if(rsmQuadroVagasCurso.next()){
						ResultSetMap rsmTurmasCadastradas = InstituicaoServices.getAllTurmasByInstituicaoPeriodo(turma.getCdInstituicao(), turma.getCdPeriodoLetivo(), false, false, true, turma.getCdCurso(), turma.getTpTurno(), connect);
						boolean alteradoTurnoCurso = true;
						if(!isInsert)
							alteradoTurnoCurso = TurmaDAO.get(turma.getCdTurma()).getTpTurno() != turma.getTpTurno() || TurmaDAO.get(turma.getCdTurma()).getCdCurso() != turma.getCdCurso();
						if(rsmQuadroVagasCurso.getInt("qt_turmas") < (rsmTurmasCadastradas.size() + (alteradoTurnoCurso ? 1 : 0))){
							listValidator.get(VALIDATE_QUADRO_VAGAS_QUANTIDADE_TURMAS).add(ValidatorResult.ERROR, "Quantidade de TURMAS não ficará condizente com o Quadro de Vagas");
						}
												
						int qtVagas = 0;
						while(rsmTurmasCadastradas.next()){
							if(alteradoTurnoCurso || rsmTurmasCadastradas.getInt("cd_turma") != turma.getCdTurma())
								qtVagas += rsmTurmasCadastradas.getInt("qt_vagas");
						}
						if(rsmQuadroVagasCurso.getInt("qt_vagas") < (qtVagas + turma.getQtVagas())){
							listValidator.get(VALIDATE_QUADRO_VAGAS_QUANTIDADE_VAGAS).add(ValidatorResult.ERROR, "Quantidade de VAGAS não ficará condizente com o Quadro de Vagas");
						}
						
					}
					else{
						listValidator.get(VALIDATE_QUADRO_VAGAS_CURSO_TURNO).add(ValidatorResult.ERROR, "Curso/Turno não cadastrado no Quadro de Vagas");
					}
				}
				else{
					listValidator.get(VALIDATE_SEM_QUADRO_VAGAS).add(ValidatorResult.ERROR, "Instituição não possui Quadro de Vagas nesse período letívo");
				}
			}
			
			if(rsmAlunos == null || rsmAlunos.size() == 0){
				listValidator.get(VALIDATE_ALUNOS).add(ValidatorResult.ERROR, "Faltando alunos na turma", GRUPO_VALIDACAO_EDUCACENSO);
				listValidator.get(VALIDATE_ALUNOS).add(ValidatorResult.ALERT, "Faltando alunos na turma", GRUPO_VALIDACAO_UPDATE);
			}
			
			if(CursoServices.isEja(curso.getCdCurso(), connect) && turma.getTpModalidadeEnsino() != TP_MODALIDADE_EJA){
				listValidator.get(VALIDATE_CURSO_EJA_ATENDIMENTO).add(ValidatorResult.ALERT, "Turma EJA com modalidade de ensino regular ou especial");
			}
			
			//Cria a mensagem de erro em um objeto de retorno chamado 'RESULT'
			result.addListValidator(listValidator);
			result.verify(idGrupo);
			
			//RETORNO
			return result;
		
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TurmaServices.validate: " + e);
			return new ValidatorResult(ValidatorResult.ERROR, "Erro em TurmaServices.validate");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static HashMap<Integer, Validator> getListValidation(){
		HashMap<Integer, Validator> list = new HashMap<Integer, Validator>();
		
		list.put(VALIDATE_NOME_FALTANDO_INVALIDO, new Validator(VALIDATE_NOME_FALTANDO_INVALIDO, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_INATIVO, new Validator(VALIDATE_NOME_INATIVO, ValidatorResult.VALID));
		list.put(VALIDATE_PERIODO_LETIVO, new Validator(VALIDATE_PERIODO_LETIVO, ValidatorResult.VALID));
		list.put(VALIDATE_TURNO, new Validator(VALIDATE_TURNO, ValidatorResult.VALID));
		list.put(VALIDATE_CURSO, new Validator(VALIDATE_CURSO, ValidatorResult.VALID));
		list.put(VALIDATE_VAGAS, new Validator(VALIDATE_VAGAS, ValidatorResult.VALID));
		list.put(VALIDATE_TIPO_ATENDIMENTO, new Validator(VALIDATE_TIPO_ATENDIMENTO, ValidatorResult.VALID));
		list.put(VALIDATE_TIPO_MODALIDADE_ENSINO, new Validator(VALIDATE_TIPO_MODALIDADE_ENSINO, ValidatorResult.VALID));
		list.put(VALIDATE_DISCIPLINAS, new Validator(VALIDATE_DISCIPLINAS, ValidatorResult.VALID));
		list.put(VALIDATE_HORARIOS, new Validator(VALIDATE_HORARIOS, ValidatorResult.VALID));
		list.put(VALIDATE_PROFESSORES, new Validator(VALIDATE_PROFESSORES, ValidatorResult.VALID));
		list.put(VALIDATE_DISCIPLINAS_INFANTIL, new Validator(VALIDATE_DISCIPLINAS_INFANTIL, ValidatorResult.VALID));
		list.put(VALIDATE_DISCIPLINAS_FUNDAMENTAL_1_EXCESSO, new Validator(VALIDATE_DISCIPLINAS_FUNDAMENTAL_1_EXCESSO, ValidatorResult.VALID));
		list.put(VALIDATE_DISCIPLINAS_FUNDAMENTAL_1_FALTANTE, new Validator(VALIDATE_DISCIPLINAS_FUNDAMENTAL_1_FALTANTE, ValidatorResult.VALID));
		list.put(VALIDATE_DISCIPLINAS_FUNDAMENTAL_2_FALTANTE, new Validator(VALIDATE_DISCIPLINAS_FUNDAMENTAL_2_FALTANTE, ValidatorResult.VALID));
		list.put(VALIDATE_NOME_DUPLICADO, new Validator(VALIDATE_NOME_DUPLICADO, ValidatorResult.VALID));
		list.put(VALIDATE_ATENDIMENTOS_ESPECIALIZADOS, new Validator(VALIDATE_ATENDIMENTOS_ESPECIALIZADOS, ValidatorResult.VALID));
		list.put(VALIDATE_TURMA_NAO_AEE, new Validator(VALIDATE_TURMA_NAO_AEE, ValidatorResult.VALID));
		list.put(VALIDATE_ATIVIDADE_COMPLEMENTAR, new Validator(VALIDATE_ATIVIDADE_COMPLEMENTAR, ValidatorResult.VALID));
		list.put(VALIDATE_TURMA_NAO_AC, new Validator(VALIDATE_TURMA_NAO_AC, ValidatorResult.VALID));
		list.put(VALIDATE_QUADRO_VAGAS_QUANTIDADE_TURMAS, new Validator(VALIDATE_QUADRO_VAGAS_QUANTIDADE_TURMAS, ValidatorResult.VALID));
		list.put(VALIDATE_QUADRO_VAGAS_QUANTIDADE_VAGAS, new Validator(VALIDATE_QUADRO_VAGAS_QUANTIDADE_VAGAS, ValidatorResult.VALID));
		list.put(VALIDATE_QUADRO_VAGAS_CURSO_TURNO, new Validator(VALIDATE_QUADRO_VAGAS_CURSO_TURNO, ValidatorResult.VALID));
		list.put(VALIDATE_SEM_QUADRO_VAGAS, new Validator(VALIDATE_SEM_QUADRO_VAGAS, ValidatorResult.VALID));
		list.put(VALIDATE_ALUNOS, new Validator(VALIDATE_ALUNOS, ValidatorResult.VALID));
		list.put(VALIDATE_CURSO_EJA_ATENDIMENTO, new Validator(VALIDATE_CURSO_EJA_ATENDIMENTO, ValidatorResult.VALID));
		list.put(VALIDATE_OFERTA_SEM_DOCENTE, new Validator(VALIDATE_OFERTA_SEM_DOCENTE, ValidatorResult.VALID));
		
		
		
		return list;
	}
}
