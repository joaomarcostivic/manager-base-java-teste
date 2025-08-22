package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class EstatisticaServices {

	public static final int TP_ITEM_MODALIDADE = 0;
	public static final int TP_ITEM_ZONA = 1;
	public static final int TP_ITEM_CURSO = 2;
	public static final int TP_ITEM_SEXO = 3;
	public static final int TP_ITEM_RACA = 4;
	public static final int TP_ITEM_NACIONALIDADE = 5;
	public static final int TP_ITEM_DOCUMENTACAO = 6;
	public static final int TP_ITEM_DEFICIENCIA = 7;
	public static final int TP_ITEM_TURMA = 8;
	public static final int TP_ITEM_MOVIMENTACAO = 9;
	public static final int TP_ITEM_DISTORCAO_IDADE_SERIE = 10;
	
	public static final int TP_QUALIFICACAO_MODALIDADE_CRECHE = 0;
	public static final int TP_QUALIFICACAO_MODALIDADE_ED_INFANTIL = 1;
	public static final int TP_QUALIFICACAO_MODALIDADE_ANOS_INICIAIS = 2;
	public static final int TP_QUALIFICACAO_MODALIDADE_ANOS_FINAIS = 3;
	public static final int TP_QUALIFICACAO_MODALIDADE_EJA = 4;
	
	public static final int TP_QUALIFICACAO_ZONA_URBANA = 0;
	public static final int TP_QUALIFICACAO_ZONA_RURAL  = 1;
	
	public static final int TP_QUALIFICACAO_SEXO_MASCULINO = 0;
	public static final int TP_QUALIFICACAO_SEXO_FEMININO  = 1;
	
	public static final int TP_QUALIFICACAO_RACA_NAO_DECLARADA = 0;
	public static final int TP_QUALIFICACAO_RACA_BRANCA 	   = 1;
	public static final int TP_QUALIFICACAO_RACA_PRETA 		   = 2;
	public static final int TP_QUALIFICACAO_RACA_PARDA 		   = 3;
	public static final int TP_QUALIFICACAO_RACA_AMARELA 	   = 4;
	public static final int TP_QUALIFICACAO_RACA_INDIGENA 	   = 5;
	
	public static final int TP_QUALIFICACAO_NACIONALIDADE_BRASILEIRA = 0;
	public static final int TP_QUALIFICACAO_NACIONALIDADE_BRASILEIRA_NASCIDO_EXTERIOR = 1;
	public static final int TP_QUALIFICACAO_NACIONALIDADE_ESTRANGEIRA = 2;
	
	public static final int TP_QUALIFICACAO_TURMA_REGULAR = 0;
	public static final int TP_QUALIFICACAO_TURMA_MAIS_EDUCACAO = 1;
	public static final int TP_QUALIFICACAO_TURMA_AEE = 2;
	
	public static final int TP_QUALIFICACAO_MOVIMENTACAO_CONFIRMACAO_MATRICULA = 0;
	public static final int TP_QUALIFICACAO_MOVIMENTACAO_CADASTRO_MATRICULA = 1;
	public static final int TP_QUALIFICACAO_MOVIMENTACAO_SOLICITACAO_TRANSFERENCIA = 2;
	public static final int TP_QUALIFICACAO_MOVIMENTACAO_CONCLUSAO_TRANSFERENCIA = 3;
	public static final int TP_QUALIFICACAO_MOVIMENTACAO_TRANSFERIDO = 4;
	public static final int TP_QUALIFICACAO_MOVIMENTACAO_EVADIDO = 5;
	public static final int TP_QUALIFICACAO_MOVIMENTACAO_DESISTENTE = 6;
	public static final int TP_QUALIFICACAO_MOVIMENTACAO_FALECIDO = 7;
	public static final int TP_QUALIFICACAO_MOVIMENTACAO_CANCELAMENTO_MATRICULA = 8;
	
	public static final int TP_QUALIFICACAO_DISTORCAO_IDADE_SERIE_CRECHE = 0;
	public static final int TP_QUALIFICACAO_DISTORCAO_IDADE_SERIE_ED_INFANTIL = 1;
	public static final int TP_QUALIFICACAO_DISTORCAO_IDADE_SERIE_ANOS_INICIAIS = 2;
	public static final int TP_QUALIFICACAO_DISTORCAO_IDADE_SERIE_ANOS_FINAIS = 3;
	public static final int TP_QUALIFICACAO_DISTORCAO_IDADE_SERIE_EJA = 4;
	
	public static final int TP_PERIODICIDADE_DIARIO  = 0;
	public static final int TP_PERIODICIDADE_SEMANAL = 1;
	public static final int TP_PERIODICIDADE_MENSAL  = 2;
	
	public static Result save(Estatistica estatistica){
		return save(estatistica, null, null);
	}

	public static Result save(Estatistica estatistica, AuthData authData){
		return save(estatistica, authData, null);
	}

	public static Result save(Estatistica estatistica, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(estatistica==null)
				return new Result(-1, "Erro ao salvar. Estatistica é nulo");

			int retorno;
			if(estatistica.getCdApuracao()==0){
				retorno = EstatisticaDAO.insert(estatistica, connect);
				estatistica.setCdApuracao(retorno);
			}
			else {
				retorno = EstatisticaDAO.update(estatistica, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ESTATISTICA", estatistica);
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
	public static Result remove(int cdApuracao, int cdPeriodoLetivo, int cdInstituicao){
		return remove(cdApuracao, cdPeriodoLetivo, cdInstituicao, false, null, null);
	}
	public static Result remove(int cdApuracao, int cdPeriodoLetivo, int cdInstituicao, boolean cascade){
		return remove(cdApuracao, cdPeriodoLetivo, cdInstituicao, cascade, null, null);
	}
	public static Result remove(int cdApuracao, int cdPeriodoLetivo, int cdInstituicao, boolean cascade, AuthData authData){
		return remove(cdApuracao, cdPeriodoLetivo, cdInstituicao, cascade, authData, null);
	}
	public static Result remove(int cdApuracao, int cdPeriodoLetivo, int cdInstituicao, boolean cascade, AuthData authData, Connection connect){
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
			retorno = EstatisticaDAO.delete(cdApuracao, cdPeriodoLetivo, cdInstituicao, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_estatistica");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EstatisticaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_estatistica", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	
	public static Result realizarApuracaoMovimentacao(int cdApuracao, int cdPeriodoLetivo) {
		return realizarApuracaoMovimentacao(cdApuracao, cdPeriodoLetivo, null);
	}

	public static Result realizarApuracaoMovimentacao(int cdApuracao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		PreparedStatement pstmt;
		try {

			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			GregorianCalendar dtInicial = Util.getDataAtual();
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			GregorianCalendar dtFinal = Util.getDataAtual();
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			//QUANTIDADE DE OCORRENCIAS DE MATRICULAS
			pstmt = connect.prepareStatement("SELECT * "+
					" FROM grl_ocorrencia A, acd_ocorrencia_matricula B, acd_matricula C, acd_instituicao_periodo D "+
					" WHERE A.cd_ocorrencia = B.cd_ocorrencia"+
					"   AND B.cd_matricula_origem = C.cd_matricula " +
					"   AND C.cd_periodo_letivo = D.cd_periodo_letivo " +
					"   AND (D.cd_periodo_letivo = " + cdPeriodoLetivo + " OR D.cd_periodo_letivo_superior = " + cdPeriodoLetivo + ")" +
					"   AND cd_tipo_ocorrencia = ? " +
					"   AND dt_ocorrencia >= ? " +
					"   AND dt_ocorrencia <= ? ");
			pstmt.setInt(1, Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_CONFIRMACAO_MATRICULA));
			pstmt.setTimestamp(2,new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(3,new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
			EstatisticaItem estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MOVIMENTACAO, TP_QUALIFICACAO_MOVIMENTACAO_CONFIRMACAO_MATRICULA, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, rsm.size());
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			
			pstmt.setInt(1, Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_MATRICULA));
			rsm = new ResultSetMap(pstmt.executeQuery());
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MOVIMENTACAO, TP_QUALIFICACAO_MOVIMENTACAO_CADASTRO_MATRICULA, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, rsm.size());
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			pstmt.setInt(1, Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_SOLICITACAO_TRANSFERENCIA));
			rsm = new ResultSetMap(pstmt.executeQuery());
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MOVIMENTACAO, TP_QUALIFICACAO_MOVIMENTACAO_SOLICITACAO_TRANSFERENCIA, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, rsm.size());
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			pstmt.setInt(1, Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_CONCLUSAO_TRANSFERENCIA));
			rsm = new ResultSetMap(pstmt.executeQuery());
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MOVIMENTACAO, TP_QUALIFICACAO_MOVIMENTACAO_CONCLUSAO_TRANSFERENCIA, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, rsm.size());
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			pstmt.setInt(1, Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_TRANSFERIDO));
			rsm = new ResultSetMap(pstmt.executeQuery());
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MOVIMENTACAO, TP_QUALIFICACAO_MOVIMENTACAO_TRANSFERIDO, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, rsm.size());
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			pstmt.setInt(1, Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_EVADIDO));
			rsm = new ResultSetMap(pstmt.executeQuery());
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MOVIMENTACAO, TP_QUALIFICACAO_MOVIMENTACAO_EVADIDO, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, rsm.size());
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			pstmt.setInt(1, Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_DESISTENTE));
			rsm = new ResultSetMap(pstmt.executeQuery());
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MOVIMENTACAO, TP_QUALIFICACAO_MOVIMENTACAO_DESISTENTE, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, rsm.size());
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			pstmt.setInt(1, Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FALECIDO));
			rsm = new ResultSetMap(pstmt.executeQuery());
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MOVIMENTACAO, TP_QUALIFICACAO_MOVIMENTACAO_FALECIDO, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, rsm.size());
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			pstmt.setInt(1, Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_CANCELAMENTO_MATRICULA));
			rsm = new ResultSetMap(pstmt.executeQuery());
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MOVIMENTACAO, TP_QUALIFICACAO_MOVIMENTACAO_CANCELAMENTO_MATRICULA, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, rsm.size());
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}

			if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Sucesso ao incluir registros");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioProcessos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result realizarApuracaoDistorcaoIdadeSerie(int cdApuracao, int cdPeriodoLetivo) {
		return realizarApuracaoDistorcaoIdadeSerie(cdApuracao, cdPeriodoLetivo, null);
	}

	public static Result realizarApuracaoDistorcaoIdadeSerie(int cdApuracao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			int cdCurso = 0;
			pstmt = connect.prepareStatement("SELECT A.cd_instituicao, CUR.nr_idade, PS.nm_produto_servico, M.cd_curso, CUR.nr_ordem, PF.dt_nascimento FROM acd_instituicao_educacenso A, acd_instituicao_periodo B, acd_matricula M, acd_curso_etapa CE, acd_tipo_etapa TE, grl_produto_servico PS, acd_curso CUR, grl_pessoa_fisica PF "
					+ "								WHERE A.cd_instituicao = B.cd_instituicao "
					+ "								  AND (B.cd_periodo_letivo = "+cdPeriodoLetivo+" OR B.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") "
					+ "								  AND A.cd_periodo_letivo = B.cd_periodo_letivo "
					+ "								  AND A.st_instituicao_publica = " + InstituicaoEducacensoServices.ST_EM_ATIVIDADE 
					+ " 							  AND A.cd_instituicao <> 2"
					+ "								  AND B.cd_periodo_letivo = M.cd_periodo_letivo"
					+ "								  AND M.st_matricula IN ("+MatriculaServices.ST_ATIVA+", "+MatriculaServices.ST_CONCLUIDA+")"
					+ "								  AND M.cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+")"
					+ "								  AND M.cd_curso = CE.cd_curso "
					+ "								  AND CE.cd_etapa = TE.cd_etapa "
					+ "								  AND M.cd_curso = PS.cd_produto_servico "
					+ "								  AND M.cd_curso = CUR.cd_curso"
					+ "								  AND M.cd_aluno = PF.cd_pessoa"
					+ " 							  AND CUR.lg_multi = 0"				
					+ "								ORDER BY 1, 2, 3, 4, 5");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(cdCurso > 0 && (rsm.getInt("cd_curso") != cdCurso)){
										
					Curso curso = CursoDAO.get(cdCurso, connect);
					if(CursoServices.isCreche(cdCurso, connect)){
						register.put("TP_MODALIDADE", TP_QUALIFICACAO_DISTORCAO_IDADE_SERIE_CRECHE);
					}
					else if(CursoServices.isEducacaoInfantil(cdCurso, connect)){
						register.put("TP_MODALIDADE", TP_QUALIFICACAO_DISTORCAO_IDADE_SERIE_ED_INFANTIL);
					}
					else if(CursoServices.isEja(cdCurso, connect)){
						register.put("TP_MODALIDADE", TP_QUALIFICACAO_DISTORCAO_IDADE_SERIE_EJA);
					}
					else{
						if(curso.getNrOrdem() >= 0 && curso.getNrOrdem() <= 4){
							register.put("TP_MODALIDADE", TP_QUALIFICACAO_DISTORCAO_IDADE_SERIE_ANOS_INICIAIS);
						}
						else if(curso.getNrOrdem() >= 5 && curso.getNrOrdem() <= 8){
							register.put("TP_MODALIDADE", TP_QUALIFICACAO_DISTORCAO_IDADE_SERIE_ANOS_FINAIS);
						}
						else{
							register.put("TP_MODALIDADE", -1);
						}
					}
					
					register.put("CD_CURSO", cdCurso);
					
					rsmFinal.addRegister(register);
					
					register = new HashMap<String, Object>();
					cdCurso = rsm.getInt("cd_curso");
					
					boolean distorcao = false;
					
					if(CursoServices.isCreche(cdCurso, connect) || CursoServices.isEducacaoInfantil(cdCurso, connect)){
						distorcao = Util.getIdade(rsm.getGregorianCalendar("dt_nascimento"), 31, 2, Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo())) != rsm.getInt("nr_idade");
					}
					else if(CursoServices.isEja(cdCurso, connect)){
						distorcao = Util.getIdade(rsm.getGregorianCalendar("dt_nascimento"), 31, 2, Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo())) < 15;
					}
					else
						distorcao = Util.getIdade(rsm.getGregorianCalendar("dt_nascimento"), 31, 2, Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo())) > (rsm.getInt("nr_idade") + 2) ||
								    Util.getIdade(rsm.getGregorianCalendar("dt_nascimento"), 31, 2, Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo())) < (rsm.getInt("nr_idade") - 2);
					
					register.put("QT_MATRICULADOS", (distorcao ? 1 : 0));
				}
				else{
					cdCurso = rsm.getInt("cd_curso");
					
					boolean distorcao = false;
					
					if(CursoServices.isCreche(cdCurso, connect) || CursoServices.isEducacaoInfantil(cdCurso, connect)){
						distorcao = Util.getIdade(rsm.getGregorianCalendar("dt_nascimento"), 31, 2, Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo())) != rsm.getInt("nr_idade");
					}
					else if(CursoServices.isEja(cdCurso, connect)){
						distorcao = Util.getIdade(rsm.getGregorianCalendar("dt_nascimento"), 31, 2, Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo())) < 15;
					}
					else
						distorcao = Util.getIdade(rsm.getGregorianCalendar("dt_nascimento"), 31, 2, Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo())) > (rsm.getInt("nr_idade") + 2) ||
								    Util.getIdade(rsm.getGregorianCalendar("dt_nascimento"), 31, 2, Integer.parseInt(InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect).getNmPeriodoLetivo())) < (rsm.getInt("nr_idade") - 2);
					
						
					if(register.get("QT_MATRICULADOS") == null)
						register.put("QT_MATRICULADOS", (distorcao ? 1 : 0));
					else
						register.put("QT_MATRICULADOS", Integer.parseInt(String.valueOf(register.get("QT_MATRICULADOS"))) + (distorcao ? 1 : 0));
				}
			}
			rsmFinal.beforeFirst();
			
			while(rsmFinal.next()){
				InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
				EstatisticaItem estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_DISTORCAO_IDADE_SERIE, rsmFinal.getInt("TP_MODALIDADE"), rsmFinal.getInt("cd_curso"), 0/*cdTipoNecessidadeEspecial*/, rsmFinal.getInt("qt_matriculados"));
				if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir item de estatistica");
				}
			}
			
			return new Result(1, "Sucesso ao apurar dados");
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getSumarioMatriculas(int cdPeriodoLetivo, int dtInicioMes, int dtInicioAno, int dtFinalMes, int dtFinalAno, int tpPeriodicidade) {
		return getSumarioMatriculas(cdPeriodoLetivo, dtInicioMes, dtInicioAno, dtFinalMes, dtFinalAno, tpPeriodicidade, null);
	}

	public static ResultSetMap getSumarioMatriculas(int cdPeriodoLetivo, int dtInicioMes, int dtInicioAno, int dtFinalMes, int dtFinalAno, int tpPeriodicidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			GregorianCalendar dtInicial = new GregorianCalendar(dtInicioAno, dtInicioMes, 1);
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			GregorianCalendar dtFinal = new GregorianCalendar(dtFinalAno, dtFinalMes, 1);
			dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			ResultSetMap rsm = new ResultSetMap();
			
			GregorianCalendar dtTemp = new GregorianCalendar(dtInicioAno, dtInicioMes, 1);
			dtTemp.set(Calendar.HOUR_OF_DAY, 0);
			dtTemp.set(Calendar.MINUTE, 0);
			dtTemp.set(Calendar.SECOND, 0);
			
			int qtSemana = 1;
			while(dtTemp.before(dtFinal)){
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				GregorianCalendar dt = (GregorianCalendar)dtTemp.clone();
				
				if(tpPeriodicidade == TP_PERIODICIDADE_MENSAL)
					dt.set(Calendar.DAY_OF_MONTH, dt.getActualMaximum(Calendar.DAY_OF_MONTH));
				else if(tpPeriodicidade == TP_PERIODICIDADE_SEMANAL){
					if((dt.get(Calendar.DAY_OF_MONTH) + 7) >= dt.getActualMaximum(Calendar.DAY_OF_MONTH)){
						dt.set(Calendar.DAY_OF_MONTH, dt.getActualMaximum(Calendar.DAY_OF_MONTH));
					}
					else{
						dt.set(Calendar.DAY_OF_MONTH, (dt.get(Calendar.DAY_OF_MONTH) + 7));
					}
				}
				dt.set(Calendar.HOUR_OF_DAY, 23);
				dt.set(Calendar.MINUTE, 59);
				dt.set(Calendar.SECOND, 59);
				
				register.put("DT_SUMARIO", dtTemp.clone());
				if(tpPeriodicidade == TP_PERIODICIDADE_SEMANAL){
					register.put("QT_SEMANA", (qtSemana++));
				}
				else{
					register.put("QT_SEMANA", 0);
				}
				
				switch(tpPeriodicidade){
					case TP_PERIODICIDADE_DIARIO:
						register.put("DT_SUMARIO_LABEL", dtTemp.get(Calendar.DAY_OF_MONTH) + " de " + Util.formatDate(dtTemp, "MMM")+"/"+
								dtTemp.get(Calendar.YEAR));
					break;
					case TP_PERIODICIDADE_SEMANAL:
						register.put("DT_SUMARIO_LABEL", dtTemp.get(Calendar.DAY_OF_WEEK_IN_MONTH) + "ª semana de " + Util.formatDate(dtTemp, "MMM")+"/"+
								dtTemp.get(Calendar.YEAR));
					break;
					case TP_PERIODICIDADE_MENSAL:
						register.put("DT_SUMARIO_LABEL", Util.formatDate(dtTemp, "MMM")+"/"+
								dtTemp.get(Calendar.YEAR));
					break;
				}
				
				
				//QUANTIDADE DE OCORRENCIAS DE MATRICULAS
				pstmt = connect.prepareStatement("SELECT count(*) as QT_TOTAL_OCORRENCIAS "+
						" FROM grl_ocorrencia A, acd_ocorrencia_matricula B, acd_matricula C, acd_instituicao_periodo D "+
						" WHERE A.cd_ocorrencia = B.cd_ocorrencia"+
						"   AND B.cd_matricula_origem = C.cd_matricula " +
						"   AND C.cd_periodo_letivo = D.cd_periodo_letivo " +
						"   AND (D.cd_periodo_letivo = " + cdPeriodoLetivo + " OR D.cd_periodo_letivo_superior = " + cdPeriodoLetivo + ")" +
						"   AND cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_CONFIRMACAO_MATRICULA + 
													", " + InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_MATRICULA + 
													", " + InstituicaoEducacensoServices.TP_OCORRENCIA_SOLICITACAO_TRANSFERENCIA + 
													", " + InstituicaoEducacensoServices.TP_OCORRENCIA_CONCLUSAO_TRANSFERENCIA + 
													", " + InstituicaoEducacensoServices.TP_OCORRENCIA_TRANSFERIDO + 
													", " + InstituicaoEducacensoServices.TP_OCORRENCIA_EVADIDO + 
													", " + InstituicaoEducacensoServices.TP_OCORRENCIA_DESISTENTE + 
													", " + InstituicaoEducacensoServices.TP_OCORRENCIA_FALECIDO + 
													", " + InstituicaoEducacensoServices.TP_OCORRENCIA_CANCELAMENTO_MATRICULA + ")" +
						"   AND dt_ocorrencia >= ? " +
						"   AND dt_ocorrencia <= ? ");
				pstmt.setTimestamp(1,new Timestamp(dtTemp.getTimeInMillis()));
				pstmt.setTimestamp(2,new Timestamp(dt.getTimeInMillis()));
				ResultSet rs = pstmt.executeQuery();
				
				register.put("QT_TOTAL_OCORRENCIAS", rs.next()?rs.getInt("QT_TOTAL_OCORRENCIAS"):0);
				
				rsm.addRegister(register);
				switch(tpPeriodicidade){
					case TP_PERIODICIDADE_DIARIO:
						dtTemp.add(Calendar.DAY_OF_MONTH, 1);
					break;
					case TP_PERIODICIDADE_SEMANAL:
						dtTemp.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
					break;
					case TP_PERIODICIDADE_MENSAL:
						dtTemp.add(Calendar.MONTH, 1);
					break;
				}
				
				rs.close();
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioProcessos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getSumarioSituacoesMatriculas(int cdPeriodoLetivo, int mes, int ano, int dia, int semana, int tpPeriodicidade) {
		return getSumarioSituacoesMatriculas(cdPeriodoLetivo, mes, ano, dia, semana, tpPeriodicidade, null);
	}

	public static ResultSetMap getSumarioSituacoesMatriculas(int cdPeriodoLetivo, int mes, int ano, int dia, int semana, int tpPeriodicidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			GregorianCalendar dtInicial = new GregorianCalendar(ano, mes, 1);
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			GregorianCalendar dtFinal = new GregorianCalendar(ano, mes, 1);
			dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			switch(tpPeriodicidade){
				case TP_PERIODICIDADE_DIARIO:
					dtInicial.set(Calendar.DAY_OF_MONTH, dia);
					dtFinal.set(Calendar.DAY_OF_MONTH, dia);
				break;
				case TP_PERIODICIDADE_SEMANAL:
					dtInicial.set(Calendar.DAY_OF_MONTH, Integer.parseInt(String.valueOf((7*(semana-1)+1))));
					int diaFinal = Integer.parseInt(String.valueOf((7*(semana-1)+1))) + 7;
					if(diaFinal > dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH)){
						diaFinal = dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH);
					}
					dtFinal.set(Calendar.DAY_OF_MONTH, diaFinal);
				break;
			}
		
			
			
			//total ocorrencias por situacao
			pstmt = connect.prepareStatement("SELECT C.nm_tipo_ocorrencia, C.cd_tipo_ocorrencia, count(*) as QT_OCORRENCIAS "+
					" FROM grl_ocorrencia A, acd_ocorrencia_matricula B, grl_tipo_ocorrencia C, acd_matricula D, acd_instituicao_periodo E "+
					" WHERE A.cd_ocorrencia = B.cd_ocorrencia"+
					"   AND A.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia" +
					"   AND B.cd_matricula_origem = D.cd_matricula" +
					"   AND D.cd_periodo_letivo = E.cd_periodo_letivo" +
					"   AND (E.cd_periodo_letivo = " + cdPeriodoLetivo + " OR E.cd_periodo_letivo_superior = " + cdPeriodoLetivo + ")" +
					"   AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_CONFIRMACAO_MATRICULA + 
												", " + InstituicaoEducacensoServices.TP_OCORRENCIA_CADASTRO_MATRICULA + 
												", " + InstituicaoEducacensoServices.TP_OCORRENCIA_SOLICITACAO_TRANSFERENCIA + 
												", " + InstituicaoEducacensoServices.TP_OCORRENCIA_CONCLUSAO_TRANSFERENCIA + 
												", " + InstituicaoEducacensoServices.TP_OCORRENCIA_TRANSFERIDO + 
												", " + InstituicaoEducacensoServices.TP_OCORRENCIA_EVADIDO + 
												", " + InstituicaoEducacensoServices.TP_OCORRENCIA_DESISTENTE + 
												", " + InstituicaoEducacensoServices.TP_OCORRENCIA_FALECIDO + 
												", " + InstituicaoEducacensoServices.TP_OCORRENCIA_CANCELAMENTO_MATRICULA + ")" +
					"   AND dt_ocorrencia >= ? " +
					"   AND dt_ocorrencia <= ? " + 
					" GROUP BY 1, 2 "+ 
					" ORDER BY C.nm_tipo_ocorrencia");
			pstmt.setTimestamp(1,new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2,new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("DT_SUMARIO", dtInicial.clone());
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getSumarioSituacaoMatriculaPorEscola(int cdPeriodoLetivo, int cdTipoOcorrencia, int mes, int ano, int dia, int semana, int tpPeriodicidade) {
		return getSumarioSituacaoMatriculaPorEscola(cdPeriodoLetivo, cdTipoOcorrencia, mes, ano, dia, semana, tpPeriodicidade, null);
	}

	public static ResultSetMap getSumarioSituacaoMatriculaPorEscola(int cdPeriodoLetivo, int cdTipoOcorrencia, int mes, int ano, int dia, int semana, int tpPeriodicidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			GregorianCalendar dtInicial = new GregorianCalendar(ano, mes, 1);
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			GregorianCalendar dtFinal = new GregorianCalendar(ano, mes, 1);
			dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			switch(tpPeriodicidade){
				case TP_PERIODICIDADE_DIARIO:
					dtInicial.set(Calendar.DAY_OF_MONTH, dia);
					dtFinal.set(Calendar.DAY_OF_MONTH, dia);
				break;
				case TP_PERIODICIDADE_SEMANAL:
					dtInicial.set(Calendar.DAY_OF_MONTH, Integer.parseInt(String.valueOf((7*(semana-1)+1))));
					int diaFinal = Integer.parseInt(String.valueOf((7*(semana-1)+1))) + 7;
					if(diaFinal > dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH)){
						diaFinal = dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH);
					}
					dtFinal.set(Calendar.DAY_OF_MONTH, diaFinal);
				break;
			}
	
			
			//total da ocorrencia por escola
			pstmt = connect.prepareStatement("SELECT G.nm_pessoa AS NM_INSTITUICAO, G.cd_pessoa AS CD_INSTITUICAO, count(*) as QT_OCORRENCIAS "+
					" FROM grl_ocorrencia A, acd_ocorrencia_matricula B, grl_tipo_ocorrencia C, acd_matricula D, acd_instituicao_periodo E, acd_turma F, grl_pessoa G "+
					" WHERE A.cd_ocorrencia = B.cd_ocorrencia"+
					"   AND A.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia" +
					"   AND B.cd_matricula_origem = D.cd_matricula" +
					"   AND D.cd_periodo_letivo = E.cd_periodo_letivo" +
					"   AND D.cd_turma = F.cd_turma" +
					"   AND F.cd_instituicao = G.cd_pessoa " +
					"   AND (E.cd_periodo_letivo = " + cdPeriodoLetivo + " OR E.cd_periodo_letivo_superior = " + cdPeriodoLetivo + ")" +
					(cdTipoOcorrencia > 0 ? " AND A.cd_tipo_ocorrencia IN (" + cdTipoOcorrencia + ")" : "") +
					"   AND dt_ocorrencia >= ? " +
					"   AND dt_ocorrencia <= ? " + 
					" GROUP BY 1, 2" +
					" ORDER BY G.nm_pessoa");
			pstmt.setTimestamp(1,new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2,new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getSumarioSituacaoMatriculaPorZona(int cdPeriodoLetivo, int cdTipoOcorrencia, int mes, int ano, int dia, int semana, int tpPeriodicidade) {
		return getSumarioSituacaoMatriculaPorZona(cdPeriodoLetivo, cdTipoOcorrencia, mes, ano, dia, semana, tpPeriodicidade, null);
	}

	public static ResultSetMap getSumarioSituacaoMatriculaPorZona(int cdPeriodoLetivo, int cdTipoOcorrencia, int mes, int ano, int dia, int semana, int tpPeriodicidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rsm = getSumarioSituacaoMatriculaPorEscola(cdPeriodoLetivo, cdTipoOcorrencia, mes, ano, dia, semana, tpPeriodicidade, connect);
			ResultSetMap rsmFinal = new ResultSetMap();
			int qtOcorrenciasCreche = 0;
			int qtOcorrenciasZonaUrbana = 0;
			int qtOcorrenciasZonaRural = 0;
			while(rsm.next()){
				int cdPeriodoLetivoInstituicao = 0;
				ResultSetMap rsmPeriodoInstituicao = new ResultSetMap(connect.prepareStatement("SELECT * FROM acd_instituicao_periodo WHERE cd_instituicao = " + rsm.getInt("cd_instituicao") + " AND cd_periodo_letivo_superior = " + cdPeriodoLetivo).executeQuery());
				if(rsmPeriodoInstituicao.next()){
					cdPeriodoLetivoInstituicao = rsmPeriodoInstituicao.getInt("cd_periodo_letivo");
				}
				InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(rsm.getInt("CD_INSTITUICAO"), cdPeriodoLetivoInstituicao, connect);
				if(rsm.getString("NM_INSTITUICAO").contains("CRECHE")){
					qtOcorrenciasCreche += rsm.getInt("QT_OCORRENCIAS");
				}
				else if(instituicaoEducacenso.getTpLocalizacao() == InstituicaoEducacensoServices.TP_LOCALIZACAO_RURAL){
					qtOcorrenciasZonaRural += rsm.getInt("QT_OCORRENCIAS");
				}
				else{
					qtOcorrenciasZonaUrbana += rsm.getInt("QT_OCORRENCIAS");
				}
			}
			rsm.beforeFirst();
			
			
			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("NM_ZONA", "Zona Urbana");
			register.put("QT_OCORRENCIAS", qtOcorrenciasZonaUrbana);
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NM_ZONA", "Zona Rural");
			register.put("QT_OCORRENCIAS", qtOcorrenciasZonaRural);
			rsmFinal.addRegister(register);
			
			register = new HashMap<String, Object>();
			register.put("NM_ZONA", "Creche");
			register.put("QT_OCORRENCIAS", qtOcorrenciasCreche);
			rsmFinal.addRegister(register);
			
			rsmFinal.beforeFirst();
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getSumarioSituacaoMatriculaPorModalidade(int cdPeriodoLetivo, int cdTipoOcorrencia, int mes, int ano, int dia, int semana, int tpPeriodicidade) {
		return getSumarioSituacaoMatriculaPorModalidade(cdPeriodoLetivo, cdTipoOcorrencia, mes, ano, dia, semana, tpPeriodicidade, null);
	}

	public static ResultSetMap getSumarioSituacaoMatriculaPorModalidade(int cdPeriodoLetivo, int cdTipoOcorrencia, int mes, int ano, int dia, int semana, int tpPeriodicidade, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			GregorianCalendar dtInicial = new GregorianCalendar(ano, mes, 1);
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			GregorianCalendar dtFinal = new GregorianCalendar(ano, mes, 1);
			dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			switch(tpPeriodicidade){
				case TP_PERIODICIDADE_DIARIO:
					dtInicial.set(Calendar.DAY_OF_MONTH, dia);
					dtFinal.set(Calendar.DAY_OF_MONTH, dia);
				break;
				case TP_PERIODICIDADE_SEMANAL:
					dtInicial.set(Calendar.DAY_OF_MONTH, Integer.parseInt(String.valueOf((7*(semana-1)+1))));
					int diaFinal = Integer.parseInt(String.valueOf((7*(semana-1)+1))) + 7;
					if(diaFinal > dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH)){
						diaFinal = dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH);
					}
					dtFinal.set(Calendar.DAY_OF_MONTH, diaFinal);
				break;
			}
			
			//total da ocorrencia por modalidade
			pstmt = connect.prepareStatement("SELECT F.nm_produto_servico AS NM_CURSO, count(*) as QT_OCORRENCIAS "+
					" FROM grl_ocorrencia A, acd_ocorrencia_matricula B, grl_tipo_ocorrencia C, acd_matricula D, acd_instituicao_periodo E, grl_produto_servico F "+
					" WHERE A.cd_ocorrencia = B.cd_ocorrencia"+
					"   AND A.cd_tipo_ocorrencia = C.cd_tipo_ocorrencia" +
					"   AND B.cd_matricula_origem = D.cd_matricula" +
					"   AND D.cd_periodo_letivo = E.cd_periodo_letivo" +
					"   AND D.cd_curso = F.cd_produto_servico " +
					"   AND (E.cd_periodo_letivo = " + cdPeriodoLetivo + " OR E.cd_periodo_letivo_superior = " + cdPeriodoLetivo + ")" +
					(cdTipoOcorrencia > 0 ? " AND A.cd_tipo_ocorrencia IN (" + cdTipoOcorrencia + ")" : "") +
					"   AND dt_ocorrencia >= ? " +
					"   AND dt_ocorrencia <= ? " + 
					" GROUP BY 1" +
					" ORDER BY F.nm_produto_servico DESC");
			pstmt.setTimestamp(1,new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2,new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static Result realizarApuracao(int cdPeriodoSecretaria) {
		return realizarApuracao(cdPeriodoSecretaria, null);
	}

	public static Result realizarApuracao(int cdPeriodoSecretaria, Connection connect) {
		boolean isConnectionNull = connect==null;
	
		try {
			System.out.println("INICIADO APURACAO...");
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			if(cdPeriodoSecretaria == 0){
				ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdSecretaria, connect); 
				if(rsmPeriodoAtual.next()){
					cdPeriodoSecretaria = rsmPeriodoAtual.getInt("cd_periodo_letivo");
				}
			}
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cdPeriodoLetivo", "" + cdPeriodoSecretaria, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = MatriculaServices.findSecretaria(criterios, connect);
			int qtTotalMatriculas = 0;
			int qtTotalVagas = 0;
			int qtTotalUrbana = 0;
			int qtTotalRural = 0;
			int qtTotalVagasUrbana = 0;
			int qtTotalVagasRural = 0;
			int qtTotalTurmas = 0;
			while(rsm.next()){
				qtTotalMatriculas += rsm.getInt("NR_ALUNOS");
				qtTotalTurmas += rsm.getInt("NR_TURMAS");
				qtTotalVagas += rsm.getInt("QT_VAGAS");
				
				int cdInstituicao = rsm.getInt("CD_INSTITUICAO");
				int cdPeriodoAtual = InstituicaoServices.getPeriodoLetivoCorrespondente(cdInstituicao, cdPeriodoSecretaria, connect); 
				
				InstituicaoEducacenso instituicaoEducacenso = InstituicaoEducacensoDAO.get(cdInstituicao, cdPeriodoAtual, connect);
				
				qtTotalRural += (instituicaoEducacenso.getTpLocalizacao() == InstituicaoEducacensoServices.TP_LOCALIZACAO_RURAL ? rsm.getInt("NR_ALUNOS") : 0);
				qtTotalUrbana  += (instituicaoEducacenso.getTpLocalizacao() == InstituicaoEducacensoServices.TP_LOCALIZACAO_URBANA ? rsm.getInt("NR_ALUNOS") : 0);
				
				qtTotalVagasRural += (instituicaoEducacenso.getTpLocalizacao() == InstituicaoEducacensoServices.TP_LOCALIZACAO_RURAL ? rsm.getInt("QT_VAGAS") : 0);
				qtTotalVagasUrbana  += (instituicaoEducacenso.getTpLocalizacao() == InstituicaoEducacensoServices.TP_LOCALIZACAO_URBANA ? rsm.getInt("QT_VAGAS") : 0);
				
				Estatistica estatistica = new Estatistica(0/*cdApuracao*/, cdPeriodoAtual, cdInstituicao, Util.getDataAtual(), rsm.getInt("NR_ALUNOS"), 
														  (instituicaoEducacenso.getTpLocalizacao() == InstituicaoEducacensoServices.TP_LOCALIZACAO_RURAL ? rsm.getInt("NR_ALUNOS") : 0), 
														  (instituicaoEducacenso.getTpLocalizacao() == InstituicaoEducacensoServices.TP_LOCALIZACAO_URBANA ? rsm.getInt("NR_ALUNOS") : 0),
														  rsm.getInt("QT_VAGAS"), 
														  (instituicaoEducacenso.getTpLocalizacao() == InstituicaoEducacensoServices.TP_LOCALIZACAO_RURAL ? rsm.getInt("QT_VAGAS") : 0), 
														  (instituicaoEducacenso.getTpLocalizacao() == InstituicaoEducacensoServices.TP_LOCALIZACAO_URBANA ? rsm.getInt("QT_VAGAS") : 0),
														  rsm.getInt("NR_TURMAS"));
				int ret = EstatisticaDAO.insert(estatistica, connect);
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir nova estatística");
				}
				estatistica.setCdApuracao(ret);
				
				Result result = realizarApuracaoItem(ret, cdPeriodoAtual, connect);
				if(result.getCode() < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return result;
				}
				
			}
			rsm.beforeFirst();
			
			Estatistica estatistica = new Estatistica(0/*cdApuracao*/, cdPeriodoSecretaria, cdSecretaria, Util.getDataAtual(), qtTotalMatriculas, qtTotalRural, qtTotalUrbana, qtTotalVagas, qtTotalVagasRural, qtTotalVagasUrbana, qtTotalTurmas);
			int ret = EstatisticaDAO.insert(estatistica, connect);
			if(ret < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir nova estatística");
			}
			estatistica.setCdApuracao(ret);
			
			Result result = realizarApuracaoItem(ret, cdPeriodoSecretaria, connect);
			if(result.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}
			
			System.out.println("FINALIZADA APURACAO.");
			
			if (isConnectionNull)
				connect.commit();
			
			result = new Result(1, "Sucesso ao pesquisar");
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static Result realizarApuracaoItem(int cdApuracao, int cdPeriodoLetivo) {
		return realizarApuracaoItem(cdApuracao, cdPeriodoLetivo, null);
	}

	public static Result realizarApuracaoItem(int cdApuracao, int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, C.tp_localizacao, E.nm_pessoa AS NM_INSTITUICAO  "+
							"FROM acd_matricula A  "+
							"LEFT OUTER JOIN acd_instituicao_periodo B ON (A.cd_periodo_letivo = B.cd_periodo_letivo)  "+
							"LEFT OUTER JOIN acd_instituicao_educacenso C ON (B.cd_instituicao = C.cd_instituicao AND B.cd_periodo_letivo = C.cd_periodo_letivo)  "+
							"LEFT OUTER JOIN acd_turma D ON (A.cd_turma = D.cd_turma)  "+
							"LEFT OUTER JOIN grl_pessoa E ON (B.cd_instituicao = E.cd_pessoa)  "+
							"WHERE A.cd_curso > 0 "+ 
							"  AND C.st_instituicao_publica = "+InstituicaoEducacensoServices.ST_EM_ATIVIDADE+
							"  AND D.nm_turma NOT LIKE 'TRANS%' AND D.tp_atendimento NOT IN ("+TurmaServices.TP_ATENDIMENTO_AEE+") " +
							"  AND D.ST_TURMA <> "+TurmaServices.ST_INATIVO+
							"  AND (B.cd_periodo_letivo = " + cdPeriodoLetivo + " OR B.cd_periodo_letivo_superior = " + cdPeriodoLetivo + ")" +
							"  AND A.st_matricula IN (" + MatriculaServices.ST_ATIVA + ", " + MatriculaServices.ST_CONCLUIDA + ")");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			int qtCreche = 0;
			int qtPreEscola = 0;
			int qtAnosIniciais = 0;
			int qtAnosFinais = 0;
			int qtEja = 0;
			
			int qtZonaUrbana = 0;
			int qtZonaRural  = 0;
			
			HashMap<Integer, Integer> registerQuantCurso = new HashMap<Integer, Integer>();
			
			while(rsm.next()){
				if(CursoServices.isCreche(rsm.getInt("cd_curso"), connect)){
					qtCreche++;
				}
				else if(CursoServices.isEducacaoInfantil(rsm.getInt("cd_curso"), connect)){
					qtPreEscola++;
				}
				else if(CursoServices.isFundamentalRegular(rsm.getInt("cd_curso"), connect)){
					Curso curso = CursoDAO.get(rsm.getInt("cd_curso"), connect);
					if(curso.getNrOrdem() < 5){
						qtAnosIniciais++;
					}
					else{
						qtAnosFinais++;
					}
				}
				else if(CursoServices.isEja(rsm.getInt("cd_curso"), connect)){
					qtEja++;
				}
				
				if(rsm.getInt("tp_localizacao") == InstituicaoEducacensoServices.TP_LOCALIZACAO_RURAL){
					qtZonaRural++;
				}
				else{
					qtZonaUrbana++;
				}
				
				if(registerQuantCurso.containsKey(rsm.getInt("cd_curso"))){
					registerQuantCurso.put(rsm.getInt("cd_curso"), (registerQuantCurso.get(rsm.getInt("cd_curso")) + 1));
				}
				else{
					registerQuantCurso.put(rsm.getInt("cd_curso"), 1);
				}
				
			}
			rsm.beforeFirst();
			
			InstituicaoPeriodo instituicaoPeriodo = InstituicaoPeriodoDAO.get(cdPeriodoLetivo, connect);
			EstatisticaItem estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MODALIDADE, TP_QUALIFICACAO_MODALIDADE_CRECHE, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, qtCreche);
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MODALIDADE, TP_QUALIFICACAO_MODALIDADE_ED_INFANTIL, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, qtPreEscola);
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MODALIDADE, TP_QUALIFICACAO_MODALIDADE_ANOS_INICIAIS, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, qtAnosIniciais);
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MODALIDADE, TP_QUALIFICACAO_MODALIDADE_ANOS_FINAIS, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, qtAnosFinais);
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_MODALIDADE, TP_QUALIFICACAO_MODALIDADE_EJA, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, qtEja);
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_ZONA, TP_QUALIFICACAO_ZONA_URBANA, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, qtZonaUrbana);
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_ZONA, TP_QUALIFICACAO_ZONA_RURAL, 0/*cdCurso*/, 0/*cdTipoNecessidadeEspecial*/, qtZonaRural);
			if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao inserir item de estatistica");
			}
			
			for(Integer key : registerQuantCurso.keySet()){
				estatisticaItem = new EstatisticaItem(0/*cdItem*/, cdApuracao, cdPeriodoLetivo, instituicaoPeriodo.getCdInstituicao(), TP_ITEM_CURSO, 0/*tpQualificacao*/, key, 0/*cdTipoNecessidadeEspecial*/, registerQuantCurso.get(key));
				if(EstatisticaItemDAO.insert(estatisticaItem, connect) < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir item de estatistica");
				}
			}
			
			Result result = new Result(1);
			
			result = realizarApuracaoMovimentacao(cdApuracao, cdPeriodoLetivo, connect);
			if(result.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}
			
			result = realizarApuracaoDistorcaoIdadeSerie(cdApuracao, cdPeriodoLetivo, connect);
			if(result.getCode() < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return result;
			}
			
			if (isConnectionNull)
				connect.commit();
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static Result getMatriculasAtuais() {
		return getMatriculasAtuais(-1, null);
	}
	
	public static Result getMatriculasAtuais(int cdPeriodoLetivo) {
		return getMatriculasAtuais(cdPeriodoLetivo, null);
	}

	public static Result getMatriculasAtuais(int cdPeriodoLetivo, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			
			if(cdPeriodoLetivo==-1) {
				ResultSetMap rsmP = InstituicaoServices.getPeriodoLetivoVigente(cdSecretaria, connect);
				if(rsmP.next())
					cdPeriodoLetivo = rsmP.getInt("cd_periodo_letivo");
			}
			
			ResultSetMap rsmPorModalidade = new ResultSetMap();
			ResultSetMap rsmPorZona = new ResultSetMap();
			ResultSetMap rsmPorCurso = new ResultSetMap();
			ResultSetMap rsmPorInstituicao = new ResultSetMap();
			Estatistica estatisticaSecretaria = null;
			
			ResultSetMap rsmApuracao = new ResultSetMap(connect.prepareStatement("SELECT MAX(cd_apuracao) AS cd_apuracao, cd_instituicao FROM acd_estatistica "
																		+ "WHERE cd_periodo_letivo = "+cdPeriodoLetivo+" "
																		+ "GROUP BY cd_instituicao "
																		+ "LIMIT 1").executeQuery());
			String txtInstituicoesMatricula = "";
			if(rsmApuracao.next()){
				Instituicao instituicao = InstituicaoDAO.get(rsmApuracao.getInt("cd_instituicao"), connect);
				Estatistica estatisticaInstituicao = EstatisticaDAO.get(rsmApuracao.getInt("cd_apuracao"), cdPeriodoLetivo, instituicao.getCdInstituicao(), connect);
								
				txtInstituicoesMatricula += "Matrículas Totais:  " + estatisticaInstituicao.getQtTotalMatriculas() + "\n";
				txtInstituicoesMatricula += "Matrículas Urbanas: " + estatisticaInstituicao.getQtMatriculasZonaUrbana() + "\n";
				txtInstituicoesMatricula += "Matrículas Rurais:  " + estatisticaInstituicao.getQtMatriculasZonaRural() + "\n\n";
				
				if(instituicao.getCdInstituicao() == cdSecretaria){
					
					estatisticaSecretaria = estatisticaInstituicao;
					
					ResultSetMap rsmInstituicoes = InstituicaoServices.getAllAtivas();
					while(rsmInstituicoes.next()){
						int cdPeriodoAtualInstituicao = InstituicaoServices.getPeriodoLetivoCorrespondente(rsmInstituicoes.getInt("cd_instituicao"), cdPeriodoLetivo, connect);
						ResultSetMap rsmApuracaoInstituicoesInternas = new ResultSetMap(connect.prepareStatement("SELECT MAX(cd_apuracao) AS cd_apuracao, cd_instituicao FROM acd_estatistica WHERE cd_periodo_letivo = "+cdPeriodoAtualInstituicao+" GROUP BY cd_instituicao LIMIT 1").executeQuery());
						while(rsmApuracaoInstituicoesInternas.next()){
							
							Instituicao instituicaoInterna = InstituicaoDAO.get(rsmApuracaoInstituicoesInternas.getInt("cd_instituicao"), connect);
							Estatistica estatisticaInstituicaoInterna = EstatisticaDAO.get(rsmApuracaoInstituicoesInternas.getInt("cd_apuracao"), cdPeriodoAtualInstituicao, rsmInstituicoes.getInt("cd_instituicao"), connect);
							InstituicaoEducacenso educacenso = InstituicaoEducacensoDAO.get(rsmInstituicoes.getInt("cd_instituicao"), cdPeriodoAtualInstituicao, connect);
							
							txtInstituicoesMatricula += instituicaoInterna.getNmPessoa() + " - Matrículas: " + estatisticaInstituicaoInterna.getQtTotalMatriculas() + "\n";
							
							int nrMatriculas = estatisticaInstituicaoInterna.getQtTotalMatriculas();
							double prMatriculas = (nrMatriculas*100)/(double)estatisticaSecretaria.getQtTotalMatriculas();
							
							
							HashMap<String, Object> register = new HashMap<String, Object>();
							register.put("NM_INSTITUICAO", instituicaoInterna.getNmPessoa()); 
							register.put("CD_INSTITUICAO", instituicaoInterna.getCdInstituicao());
							register.put("VL_LATITUDE", rsmInstituicoes.getDouble("vl_latitude"));
							register.put("VL_LONGITUDE", rsmInstituicoes.getDouble("vl_longitude"));
							register.put("TP_LOCALIZACAO", (educacenso == null) ? 1 : educacenso.getTpLocalizacao()); 
							register.put("NR_MATRICULAS", estatisticaInstituicaoInterna.getQtTotalMatriculas()); 
							register.put("PR_MATRICULAS", prMatriculas);
							rsmPorInstituicao.addRegister(register);
							
						}
						rsmApuracao.beforeFirst();
						
					}
					rsmInstituicoes.beforeFirst();
				}
				else{
					
				}
				
				
				/*
				 * MODALIDADE
				 * */
				HashMap<String, Object> register = new HashMap<String, Object>();
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_apuracao", "" + estatisticaInstituicao.getCdApuracao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_periodo_letivo", "" + estatisticaInstituicao.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_instituicao", "" + estatisticaInstituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("tp_item", "" + TP_ITEM_MODALIDADE, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("tp_qualificacao", "" + TP_QUALIFICACAO_MODALIDADE_CRECHE, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmEstatisticaItem = EstatisticaItemServices.find(criterios, connect);
				if(rsmEstatisticaItem.next()){
					register.put("NM_MODALIDADE", "Creche"); 
					register.put("NR_MATRICULAS", EstatisticaItemDAO.get(rsmEstatisticaItem.getInt("cd_item"), estatisticaInstituicao.getCdApuracao(), estatisticaInstituicao.getCdPeriodoLetivo(), estatisticaInstituicao.getCdInstituicao(), connect).getQtApuracao());
					rsmPorModalidade.addRegister(register);
				}
				
				register = new HashMap<String, Object>();
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_apuracao", "" + estatisticaInstituicao.getCdApuracao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_periodo_letivo", "" + estatisticaInstituicao.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_instituicao", "" + estatisticaInstituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("tp_item", "" + TP_ITEM_MODALIDADE, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("tp_qualificacao", "" + TP_QUALIFICACAO_MODALIDADE_ED_INFANTIL, ItemComparator.EQUAL, Types.INTEGER));
				rsmEstatisticaItem = EstatisticaItemServices.find(criterios, connect);
				if(rsmEstatisticaItem.next()){
					register.put("NM_MODALIDADE", "Educação Infantil"); 
					register.put("NR_MATRICULAS", EstatisticaItemDAO.get(rsmEstatisticaItem.getInt("cd_item"), estatisticaInstituicao.getCdApuracao(), estatisticaInstituicao.getCdPeriodoLetivo(), estatisticaInstituicao.getCdInstituicao(), connect).getQtApuracao());
					rsmPorModalidade.addRegister(register);
				}
				
				register = new HashMap<String, Object>();
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_apuracao", "" + estatisticaInstituicao.getCdApuracao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_periodo_letivo", "" + estatisticaInstituicao.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_instituicao", "" + estatisticaInstituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("tp_item", "" + TP_ITEM_MODALIDADE, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("tp_qualificacao", "" + TP_QUALIFICACAO_MODALIDADE_ANOS_INICIAIS, ItemComparator.EQUAL, Types.INTEGER));
				rsmEstatisticaItem = EstatisticaItemServices.find(criterios, connect);
				if(rsmEstatisticaItem.next()){
					register.put("NM_MODALIDADE", "Anos Iniciais"); 
					register.put("NR_MATRICULAS", EstatisticaItemDAO.get(rsmEstatisticaItem.getInt("cd_item"), estatisticaInstituicao.getCdApuracao(), estatisticaInstituicao.getCdPeriodoLetivo(), estatisticaInstituicao.getCdInstituicao(), connect).getQtApuracao());
					rsmPorModalidade.addRegister(register);
				}
				
				register = new HashMap<String, Object>();
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_apuracao", "" + estatisticaInstituicao.getCdApuracao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_periodo_letivo", "" + estatisticaInstituicao.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_instituicao", "" + estatisticaInstituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("tp_item", "" + TP_ITEM_MODALIDADE, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("tp_qualificacao", "" + TP_QUALIFICACAO_MODALIDADE_ANOS_FINAIS, ItemComparator.EQUAL, Types.INTEGER));
				rsmEstatisticaItem = EstatisticaItemServices.find(criterios, connect);
				if(rsmEstatisticaItem.next()){
					register.put("NM_MODALIDADE", "Anos Finais"); 
					register.put("NR_MATRICULAS", EstatisticaItemDAO.get(rsmEstatisticaItem.getInt("cd_item"), estatisticaInstituicao.getCdApuracao(), estatisticaInstituicao.getCdPeriodoLetivo(), estatisticaInstituicao.getCdInstituicao(), connect).getQtApuracao());
					rsmPorModalidade.addRegister(register);
				}
				
				register = new HashMap<String, Object>();
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_apuracao", "" + estatisticaInstituicao.getCdApuracao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_periodo_letivo", "" + estatisticaInstituicao.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_instituicao", "" + estatisticaInstituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("tp_item", "" + TP_ITEM_MODALIDADE, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("tp_qualificacao", "" + TP_QUALIFICACAO_MODALIDADE_EJA, ItemComparator.EQUAL, Types.INTEGER));
				rsmEstatisticaItem = EstatisticaItemServices.find(criterios, connect);
				if(rsmEstatisticaItem.next()){
					register.put("NM_MODALIDADE", "EJA"); 
					register.put("NR_MATRICULAS", EstatisticaItemDAO.get(rsmEstatisticaItem.getInt("cd_item"), estatisticaInstituicao.getCdApuracao(), estatisticaInstituicao.getCdPeriodoLetivo(), estatisticaInstituicao.getCdInstituicao(), connect).getQtApuracao());
					rsmPorModalidade.addRegister(register);
				}
				rsmPorModalidade.beforeFirst();
				
				/*
				 * ZONA
				 * */
				register = new HashMap<String, Object>();
				register.put("NM_ZONA", "Zona Urbana"); 
				register.put("NR_MATRICULAS", estatisticaInstituicao.getQtMatriculasZonaUrbana());
				rsmPorZona.addRegister(register);
				
				register = new HashMap<String, Object>();
				register.put("NM_ZONA", "Zona Rural"); 
				register.put("NR_MATRICULAS", estatisticaInstituicao.getQtMatriculasZonaRural());
				rsmPorZona.addRegister(register);
				
				
				/*
				 * CURSO
				 * */
				ResultSetMap rsmCursosInstituicao = new ResultSetMap();
				if(instituicao.getCdInstituicao() == cdSecretaria){
					rsmCursosInstituicao = CursoServices.getAll(connect);
					ArrayList<String> fields = new ArrayList<String>();
					fields.add("NM_CURSO DESC");
					rsmCursosInstituicao.orderBy(fields);
					rsmCursosInstituicao.beforeFirst();
				}
				else{
					rsmCursosInstituicao = CursoServices.getAllCursosOf(estatisticaInstituicao.getCdInstituicao(), false, false, false, false, estatisticaInstituicao.getCdPeriodoLetivo(), connect);
				}
				
				int nrTotal = 0;
				while(rsmCursosInstituicao.next()){
					register = new HashMap<String, Object>();
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_apuracao", "" + estatisticaInstituicao.getCdApuracao(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_periodo_letivo", "" + estatisticaInstituicao.getCdPeriodoLetivo(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_instituicao", "" + estatisticaInstituicao.getCdInstituicao(), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("tp_item", "" + TP_ITEM_CURSO, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_curso", "" + rsmCursosInstituicao.getInt("cd_curso"), ItemComparator.EQUAL, Types.INTEGER));
					rsmEstatisticaItem = EstatisticaItemServices.find(criterios, connect);
					if(rsmEstatisticaItem.next()){
						
						//TODO: REVER PARA UM MODO QUE NAO PRECISE IR NO BANCO TODA HORA BUSCAR A MODALIDADE DO CURSO
						int tpModalidade = -1;
						if(CursoServices.isCreche(rsmCursosInstituicao.getInt("cd_curso"), connect))
							tpModalidade = TP_QUALIFICACAO_MODALIDADE_CRECHE;
						else if(CursoServices.isEducacaoInfantil(rsmCursosInstituicao.getInt("cd_curso"), connect))
							tpModalidade = TP_QUALIFICACAO_MODALIDADE_ED_INFANTIL;
						else if(CursoServices.isFundamentalRegular(rsmCursosInstituicao.getInt("cd_curso"), connect)) {
							if(rsmCursosInstituicao.getInt("nr_ordem") < 5)
								tpModalidade = TP_QUALIFICACAO_MODALIDADE_ANOS_INICIAIS;
							else
								tpModalidade = TP_QUALIFICACAO_MODALIDADE_ANOS_FINAIS;
						}
						else if(CursoServices.isEja(rsmCursosInstituicao.getInt("cd_curso"), connect))
							tpModalidade = TP_QUALIFICACAO_MODALIDADE_EJA;
						
						int nrMatriculasCurso = EstatisticaItemDAO.get(rsmEstatisticaItem.getInt("cd_item"), estatisticaInstituicao.getCdApuracao(), estatisticaInstituicao.getCdPeriodoLetivo(), estatisticaInstituicao.getCdInstituicao(), connect).getQtApuracao();
						double prMatriculaCurso = (nrMatriculasCurso*100)/estatisticaSecretaria.getQtTotalMatriculas();
						
						nrTotal+=nrMatriculasCurso;
						
						
						register.put("NM_CURSO_COMPLETO", rsmCursosInstituicao.getString("NM_CURSO")); 
						register.put("NM_CURSO", rsmCursosInstituicao.getString("NM_PRODUTO_SERVICO"));
						register.put("TP_MODALIDADE", tpModalidade); 
						register.put("NR_MATRICULAS", nrMatriculasCurso);
						register.put("PR_MATRICULAS", prMatriculaCurso);
						rsmPorCurso.addRegister(register);
					}
				}
				
				System.out.println("Total: "+nrTotal);
			}
			
			Result result = new Result(1, txtInstituicoesMatricula);
						
			result.addObject("ESTATISTICA_SECRETARIA", estatisticaSecretaria);
			result.addObject("RSM_MATRICULAS_POR_INSTITUICAO", rsmPorInstituicao);
			result.addObject("RSM_MATRICULAS_POR_MODALIDADE", rsmPorModalidade);
			result.addObject("RSM_MATRICULAS_POR_ZONA", rsmPorZona);
			result.addObject("RSM_MATRICULAS_POR_CURSO", rsmPorCurso);
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}
	
	public static ResultSetMap getDistorcaoIdadeSerieEscolarByModalidade(int cdPeriodoLetivo, int tpZona, int lgUnirFundamental, int cdInstituicao) {
		return getDistorcaoIdadeSerieEscolarByModalidade(cdPeriodoLetivo, tpZona, lgUnirFundamental, cdInstituicao, null);
	}

	public static ResultSetMap getDistorcaoIdadeSerieEscolarByModalidade(int cdPeriodoLetivo, int tpZona, int lgUnirFundamental, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			//total da ocorrencia por modalidade
			ResultSetMap rsmFinal = new ResultSetMap();
			HashMap<String, Object> register = new HashMap<String, Object>();
			int cdCurso = 0;
			int tpQualificacao = -1;
			int nrOrdem = -1;
			pstmt = connect.prepareStatement("SELECT CUR.nr_idade, PS.nm_produto_servico, CUR.nr_ordem, EI.* FROM acd_estatistica_item EI, acd_instituicao_educacenso A, acd_instituicao_periodo B, grl_produto_servico PS, acd_curso CUR "
					+ "								WHERE EI.cd_periodo_letivo = A.cd_periodo_letivo "
					+ "								  AND EI.cd_instituicao = A.cd_instituicao "
					+ "								  AND A.cd_instituicao = B.cd_instituicao "
					+ "								  AND A.cd_periodo_letivo = B.cd_periodo_letivo "
					+ "								  AND (B.cd_periodo_letivo = "+cdPeriodoLetivo+" OR B.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") "
					+ "								  AND EI.cd_curso = PS.cd_produto_servico "
					+ "								  AND EI.cd_curso = CUR.cd_curso"
					+ "								  AND EI.tp_item = " + TP_ITEM_DISTORCAO_IDADE_SERIE
					+ (tpZona > 0 ? "				  AND A.tp_localizacao = " + tpZona : "")	
					+ (cdInstituicao > 0 ? "		  AND A.cd_instituicao = " + cdInstituicao : "")
					+ " 							  AND CUR.lg_multi = 0"				
					+ "								ORDER BY 1, 2, 3, 4, 5");

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(cdCurso > 0 && ((lgUnirFundamental <= 0 && rsm.getInt("cd_curso") != cdCurso) 
						        || (lgUnirFundamental > 0 && 
						        		((nrOrdem > -1 && nrOrdem < 9 && nrOrdem != rsm.getInt("nr_ordem"))
						        	   || ((nrOrdem <= -1 || nrOrdem >= 9) && rsm.getInt("cd_curso") != cdCurso))
						        	)
						          )
				  ){
					Curso curso = CursoDAO.get(cdCurso, connect);
					register.put("TP_MODALIDADE", tpQualificacao);
					register.put("CD_CURSO", cdCurso);
				
					if(lgUnirFundamental > 0 && nrOrdem > -1 && nrOrdem < 9 && !CursoServices.isEja(cdCurso, connect)){
						ResultSetMap rsmCursoEquivalente = new ResultSetMap(connect.prepareStatement("SELECT B.nm_produto_servico FROM acd_curso A, grl_produto_servico B WHERE A.cd_curso = B.cd_produto_servico AND A.lg_referencia = 1 AND A.nr_ordem = " + nrOrdem).executeQuery());
						String nmCurso = null;
						if(rsmCursoEquivalente.next()){
							nmCurso = rsmCursoEquivalente.getString("nm_produto_servico");
						}
						register.put("NM_CURSO", nmCurso);
					}
					else{
						register.put("NM_CURSO", curso.getNmProdutoServico());
					}
					rsmFinal.addRegister(register);
					
					register = new HashMap<String, Object>();
					cdCurso = rsm.getInt("cd_curso");
					nrOrdem = rsm.getInt("nr_ordem");
					tpQualificacao = rsm.getInt("tp_qualificacao");
					
					register.put("qtMatriculados", rsm.getInt("qt_apuracao"));
				}
				else{
					cdCurso = rsm.getInt("cd_curso");
					nrOrdem = rsm.getInt("nr_ordem");
					tpQualificacao = rsm.getInt("tp_qualificacao");
					
					register.put("qtMatriculados", Integer.parseInt(String.valueOf((register.get("qtMatriculados") != null ? register.get("qtMatriculados") : 0))) + rsm.getInt("qt_apuracao"));
				}
			}
			rsmFinal.beforeFirst();
			
			return rsmFinal;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoServices.getSumarioFase: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}

	
}