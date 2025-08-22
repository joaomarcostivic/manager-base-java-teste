package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.TipoTransporteServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Produto;
import com.tivic.manager.grl.ProdutoDAO;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class CursoServices {
	/* tipo de contratacao */
	public static final int TP_MENSALIDADE 	= 0;
	public static final int TP_CURSO 		= 1;
	public static final int TP_PERIODO 		= 2;
	public static final int TP_DISCIPLINA 	= 3;

	/* classificacaoes de erros retornados por rotinas da classe CursoServices */
	public static final int ERR_TAB_PRECO_USO = -2;

	
	public static final String[] modalidadeEnsino = {"Regular","Especial","EJA"};
	public static final String[] tipoContratacao = {"Mensalidade", "Curso", "Período/Módulo/Série/Etapa", "Disciplina"};
	
	
	/*Tipo de Presenca*/
	public static final int TP_PRESENCIAL = 0;
	public static final int TP_SEMIPRESENCIAL = 1;
	
	
	/*Tipo de Ordem*/
	public static final int TP_ORDEM_SEM_IDENTIFICACAO = -1;
	public static final int TP_ORDEM_1_ANO = 0;
	public static final int TP_ORDEM_2_ANO_1_SERIE = 1;
	public static final int TP_ORDEM_3_ANO_2_SERIE = 2;
	public static final int TP_ORDEM_4_ANO_3_SERIE = 3;
	public static final int TP_ORDEM_5_ANO_4_SERIE = 4;
	public static final int TP_ORDEM_6_ANO_5_SERIE = 5;
	public static final int TP_ORDEM_7_ANO_6_SERIE = 6;
	public static final int TP_ORDEM_8_ANO_7_SERIE = 7;
	public static final int TP_ORDEM_9_ANO_8_SERIE = 8;
	public static final int TP_ORDEM_1_ANO_MEDIO = 9;
	public static final int TP_ORDEM_2_ANO_MEDIO = 10;
	public static final int TP_ORDEM_3_ANO_MEDIO = 11;
	public static final int TP_ORDEM_4_ANO_MEDIO = 12;
	
	public static final String[] tipoOrdem = {"Sem identificação", 
													"1º ano", 
													"2º ano", 
													"3º ano",
													"4º ano",
													"5º ano",
													"6º ano",
													"7º ano",
													"8º ano",
													"9º ano"};
	
	
	public static final String TP_ETAPA_CURSO_CRECHE 											= "01";
	public static final String TP_ETAPA_CURSO_PRE_ESCOLA 										= "02";
	public static final String TP_ETAPA_CURSO_UNIFICADA 										= "03";
	public static final String TP_ETAPA_CURSO_8_ANOS_1 											= "04";
	public static final String TP_ETAPA_CURSO_8_ANOS_2 											= "05";
	public static final String TP_ETAPA_CURSO_8_ANOS_3 											= "06";
	public static final String TP_ETAPA_CURSO_8_ANOS_4 											= "07";
	public static final String TP_ETAPA_CURSO_8_ANOS_5 											= "08";
	public static final String TP_ETAPA_CURSO_8_ANOS_6 											= "09";
	public static final String TP_ETAPA_CURSO_8_ANOS_7 											= "10";
	public static final String TP_ETAPA_CURSO_8_ANOS_8 											= "11";
	public static final String TP_ETAPA_CURSO_8_ANOS_MULTI 										= "12";
	public static final String TP_ETAPA_CURSO_8_ANOS_CORRECAO_FLUXO 							= "13";
	public static final String TP_ETAPA_CURSO_9_ANOS_1 											= "14";
	public static final String TP_ETAPA_CURSO_9_ANOS_2 											= "15";
	public static final String TP_ETAPA_CURSO_9_ANOS_3 											= "16";
	public static final String TP_ETAPA_CURSO_9_ANOS_4 											= "17";
	public static final String TP_ETAPA_CURSO_9_ANOS_5 											= "18";
	public static final String TP_ETAPA_CURSO_9_ANOS_6 											= "19";
	public static final String TP_ETAPA_CURSO_9_ANOS_7 											= "20";
	public static final String TP_ETAPA_CURSO_9_ANOS_8 											= "21";
	public static final String TP_ETAPA_CURSO_9_ANOS_MULTI 										= "22";
	public static final String TP_ETAPA_CURSO_9_ANOS_CORRECAO_FLUXO 							= "23";
	public static final String TP_ETAPA_CURSO_8_9_ANOS_MULTI 									= "24";
	public static final String TP_ETAPA_CURSO_MEDIO_1 											= "25";
	public static final String TP_ETAPA_CURSO_MEDIO_2 											= "26";
	public static final String TP_ETAPA_CURSO_MEDIO_3 											= "27";
	public static final String TP_ETAPA_CURSO_MEDIO_4 											= "28";
	public static final String TP_ETAPA_CURSO_MEDIO_NAO_SERIADA 								= "29";
	public static final String TP_ETAPA_CURSO_MEDIO_INTEGRADO_1 								= "30";
	public static final String TP_ETAPA_CURSO_MEDIO_INTEGRADO_2 								= "31";
	public static final String TP_ETAPA_CURSO_MEDIO_INTEGRADO_3 								= "32";
	public static final String TP_ETAPA_CURSO_MEDIO_INTEGRADO_4 								= "33";
	public static final String TP_ETAPA_CURSO_MEDIO_INTEGRADO_NAO_SERIADA 						= "34";
	public static final String TP_ETAPA_CURSO_MEDIO_MAGISTERIO_1 								= "35";
	public static final String TP_ETAPA_CURSO_MEDIO_MAGISTERIO_2 								= "36";
	public static final String TP_ETAPA_CURSO_MEDIO_MAGISTERIO_3 								= "37";
	public static final String TP_ETAPA_CURSO_MEDIO_MAGISTERIO_4 								= "38";
	public static final String TP_ETAPA_CURSO_PROFISSIONAL_CONCOMITANTE 						= "39";
	public static final String TP_ETAPA_CURSO_PROFISSIONAL_SUBSEQUENTE 							= "40";
	public static final String TP_ETAPA_CURSO_9_ANOS_9 											= "41";
	public static final String TP_ETAPA_CURSO_9_ANOS_MULTI_ETAPA								= "56";
	public static final String TP_ETAPA_CURSO_EJA_INICIAIS 										= "69";
	public static final String TP_ETAPA_CURSO_EJA_FINAIS 										= "70";
	public static final String TP_ETAPA_CURSO_EJA_MEDIO 										= "71";
	public static final String TP_ETAPA_CURSO_EJA_INICIAIS_FINAIS 								= "72";
	public static final String TP_ETAPA_CURSO_INFANTIL_FUNDAMENTAL_8_9_MULTIETAPA 				= "56";
	public static final String TP_ETAPA_CURSO_EJA_PROFISSIONAL_FUNDAMENTAL_FIC 					= "73";
	public static final String TP_ETAPA_CURSO_EJA_PROFISSIONAL_MEDIO 							= "74";
	public static final String TP_ETAPA_CURSO_PROFISSIONAL_MISTA_CONCO_SUBSEQUENTE 				= "64";
	public static final String TP_ETAPA_CURSO_EJA_PRESENCIAL_FUNDAMENTAL_PROURBANO 				= "65";
	public static final String TP_ETAPA_CURSO_EJA_PROFISSIONAL_MEDIO_FIC	 					= "67";
	public static final String TP_ETAPA_CURSO_CONCOMITANTE_FIC				 					= "68";
	
	public static Result save(Curso curso) {
		return save(curso, 0, null);
	}

	public static Result save(Curso curso, Connection connect) {
		return save(curso, 0, connect);
	}
	
	public static Result save(Curso curso, int cdEtapa) {
		return save(curso, cdEtapa, null);
	}

	/**
	 * Insere ou atualiza um curso
	 * @param curso
	 * @param cdEtapa
	 * @param connect
	 * @return
	 */
	public static Result save(Curso curso, int cdEtapa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			Result result = new Result(1);
			
			int retorno;
			/*
			 * Atualiza ou inclui dados do curso
			 */
			if(curso.getCdCurso() > 0)
				retorno = CursoDAO.update(curso, connect);
			else {
				retorno = CursoDAO.insert(curso, connect);
				curso.setCdCurso(retorno);
				
				/*
				 * Modulo padrao
				 * 
				 * Insere o modulo default.
				 * 
				 */
				if(retorno > 0) {
					CursoModulo cursoModulo = new CursoModulo(0, 0, "MÓDULO ÚNICO - " + curso.getNmProdutoServico(), null, null, null, null, 
							0, curso.getCdCurso()+"001", null, 0, 0, 0, null, 0, null, 0, 0, curso.getCdCurso(), 1);
					retorno = CursoModuloServices.save(cursoModulo, connect).getCode();
					if(retorno<=0)	{
						Conexao.rollback(connect);
						return new Result(-2, "Erro ao criar módulo padrão do curso!");
					}
					
					result.addObject("CURSOMODULO", cursoModulo);
				}
				
				
				
				/*
				 * Unidades Padrao
				 * 
				 * Insere as unidades default.
				 * 
				 */
				if(retorno > 0) {
					for (int i=1; i<=4; i++) {
						retorno = CursoUnidadeServices.save(new CursoUnidade(0, curso.getCdCurso(), i+"ª Unidade", null, null, "", i), connect).getCode();						
						if(retorno<=0)	{
							if(isConnectionNull)
								Conexao.rollback(connect);
							return new Result(-3, "Erro ao criar unidades padrão do curso!");
						}
					}
					
				}
				
			}
			
			if(retorno<=0)	{
				Conexao.rollback(connect);
				return new Result(-1, "Erro ao salvar curso!");
			}
			
			if(curso.getLgMulti()==0){
				int ret = connect.prepareStatement("DELETE FROM acd_curso_multi WHERE cd_curso_multi = " + curso.getCdCurso()).executeUpdate();
				if(ret<0)	{
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro deletar todos os cursos ligados ao curso multi!");
				}
			}
			
			
			if(cdEtapa > 0){
				int ret = connect.prepareStatement("DELETE FROM acd_curso_etapa WHERE cd_curso = " + curso.getCdCurso() + " AND cd_etapa <> " + cdEtapa).executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro deletar todas as etapas ligadas a esse curso!");
				}
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_curso", "" + curso.getCdCurso(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("A.cd_etapa", "" + cdEtapa, ItemComparator.EQUAL, Types.INTEGER));
				if(!CursoEtapaServices.find(criterios, connect).next()){
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("A.cd_etapa", "" + cdEtapa, ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmBuscaEtapa = CursoEtapaServices.find(criterios, connect);
					String idCursoEtapa = "";
					if(rsmBuscaEtapa.next()){
						idCursoEtapa = rsmBuscaEtapa.getString("id_curso_etapa");
					}
					if(CursoEtapaServices.save(new CursoEtapa(0/*cdCursoEtapa*/, curso.getCdCurso(), 0/*cdCursoEtapaPosterior*/, idCursoEtapa, cdEtapa), connect).getCode() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro criar relação de curso e etapa!");
					}
				}
			}
			

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			result.setCode(retorno);
			result.setMessage((retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...");
			result.addObject("CURSO", curso);
			
			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new Result(-3, "Erro ao tentar salvar Curso!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static Result remove(int cdCurso){
		return remove(cdCurso, false, null);
	}
	
	public static Result remove(int cdCurso, boolean cascade){
		return remove(cdCurso, cascade, null);
	}
	
	public static Result remove(int cdCurso, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade){
			
			}
				
			if(!cascade || retorno>0)
				retorno = CursoDAO.delete(cdCurso, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este curso esta vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Curso excluido com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir curso!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int delete(int cdCurso) {
		return delete(cdCurso, null);
	}

	public static int delete(int cdCurso, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			if (CursoDAO.delete(cdCurso, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (ProdutoServicoDAO.delete(cdCurso, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}


	public static ResultSetMap getAllNucleos(int cdCurso) {
		return getAllNucleos(cdCurso, null);
	}

	/**
	 * Busca todos os nucleos de um curso
	 * @param cdCurso
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllNucleos(int cdCurso, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.* " +
					"FROM acd_curso_nucleo A " +
					"WHERE A.cd_curso = "+cdCurso);
			return new ResultSetMap(pstmt.executeQuery());
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

	public static ResultSetMap getAllTurmas(int cdCurso) {
		return getAllTurmas(cdCurso, null);
	}

	/**
	 * Busca todas as turmas que tenha determinado curso
	 * @param cdCurso
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllTurmas(int cdCurso, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_matriz, C.nm_periodo_letivo, D.nm_tipo_periodo " +
					"FROM acd_turma A " +
					"LEFT OUTER JOIN acd_curso_matriz        B ON (A.cd_matriz = B.cd_matriz " +
					"									       AND A.cd_curso  = B.cd_curso) " +
					"LEFT OUTER JOIN acd_instituicao_periodo C ON (A.cd_periodo_letivo = C.cd_periodo_letivo) " +
					"LEFT OUTER JOIN acd_tipo_periodo        D ON (C.cd_tipo_periodo = D.cd_tipo_periodo) " +
					"WHERE A.cd_curso = "+cdCurso);
			return new ResultSetMap(pstmt.executeQuery());
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

	public static ResultSetMap getAllAreasInteresses(int cdCurso) {
		return getAllAreasInteresses(cdCurso, null);
	}

	public static ResultSetMap getAllAreasInteresses(int cdCurso, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_area_interesse " +
					"FROM acd_curso_area_interesse A, acd_area_interesse B " +
					"WHERE A.cd_area_interesse = B.cd_area_interesse " +
					"  AND A.cd_curso = "+cdCurso);
			return new ResultSetMap(pstmt.executeQuery());
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

	public static ResultSetMap getAllMatrizes(int cdCurso, int cdInstituicao) {
		return getAllMatrizes(cdCurso, cdInstituicao, null);
	}

	public static ResultSetMap getAllMatrizes(int cdCurso, int cdInstituicao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmtMatrizes = connection.prepareStatement("SELECT A.*" +
																  		  "FROM acd_curso_matriz A " +
																  		  "WHERE A.cd_curso = " + cdCurso +
																  		  "ORDER BY A.dt_vigencia_inicial DESC");
			PreparedStatement pstmtAreas = connection.prepareStatement("SELECT A.*" +
																	   "FROM acd_area_conhecimento A " +
																	   "WHERE A.cd_area_conhecimento_superior is null " +
																	   "ORDER BY A.nm_area_conhecimento DESC");
			
			ResultSetMap rsmMatrizes = new ResultSetMap(pstmtMatrizes.executeQuery());
			while (rsmMatrizes.next()) {
				ResultSetMap rsmClassificacao = new ResultSetMap();
				for (int i = 0; i < 3; i++) {
					HashMap<String, Object> regClassificacao = new HashMap<String, Object>();
					ResultSetMap rsmAreas = new ResultSetMap(pstmtAreas.executeQuery());
					ResultSetMap rsmAreasAux = new ResultSetMap();
					while (rsmAreas.next()) {
						ResultSetMap rsmDisciplinas = CursoDisciplinaServices.getCursoDisciplinasByClassificacaoArea(cdCurso, i/*tp_classificacao*/, rsmAreas.getInt("cd_area_conhecimento"), rsmMatrizes.getInt("cd_matriz"), cdInstituicao, connection);
						rsmAreas.setValueToField("HIERARQUIA_MATRIZ", rsmDisciplinas.getLines());
						if(rsmDisciplinas.getLines().size()>0)
							rsmAreasAux.addRegister(rsmAreas.getRegister());
					}
					regClassificacao.put("HIERARQUIA_MATRIZ", rsmAreasAux.getLines());					
					regClassificacao.put("TP_CLASSIFICACAO", i);
					regClassificacao.put("NM_TP_CLASSIFICACAO", DisciplinaServices.tiposClassificacao[i]);
					
					rsmClassificacao.addRegister(regClassificacao);
					
				}
				rsmMatrizes.setValueToField("HIERARQUIA_MATRIZ", rsmClassificacao.getLines());
			}
			rsmMatrizes.beforeFirst();
			return rsmMatrizes;
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

	public static ResultSetMap getAllModulos(int cdCurso) {
		return getAllModulos(cdCurso, null);
	}

	public static ResultSetMap getAllModulos(int cdCurso, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.cd_produto_servico, B.nm_produto_servico, B.id_produto_servico " +
					"FROM acd_curso_modulo A, grl_produto_servico B " +
					"WHERE A.cd_curso_modulo = B.cd_produto_servico "+
					"  AND A.cd_curso = "+cdCurso+
					"ORDER BY nr_ordem");
			return new ResultSetMap(pstmt.executeQuery());
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

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connection) {
		return getAll(false, false, false, connection);
	}
	
	public static ResultSetMap getAll(boolean lgRegular, boolean lgEspecial, boolean lgEja) {
		return getAll(lgRegular, lgEspecial, lgEja, null);
	}

	public static ResultSetMap getAll(boolean lgRegular, boolean lgEspecial, boolean lgEja, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_produto_servico, (H.nm_etapa || ' - ' || B.nm_produto_servico) AS nm_curso, B.id_produto_servico AS id_curso, H.nm_etapa, H.sg_tipo_etapa, H.lg_regular, H.lg_especial, H.lg_eja " +
					" FROM acd_curso A"+ 
					" JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico) "+ 
					" LEFT OUTER JOIN acd_curso_etapa G ON (G.cd_curso = A.cd_curso) " +
					" LEFT OUTER JOIN acd_tipo_etapa H ON (H.cd_etapa = G.cd_etapa) " +
					((lgRegular || lgEspecial || lgEja) ? " WHERE H.lg_regular = "+(lgRegular ? "1" : "0") + " AND H.lg_especial = "+(lgEspecial ? "1" : "0") + " AND H.lg_eja = "+(lgEja ? "1" : "0") : "")+
					" ORDER BY H.nm_etapa, B.nm_produto_servico");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				if(rsm.getString("nm_curso") == null || rsm.getString("nm_curso").trim().equals("")){
					rsm.setValueToField("nm_curso", rsm.getString("nm_produto_servico"));
				}
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
	
	public static ResultSetMap getAllCursosOf(int cdInstituicao) {
		return getAllCursosOf(cdInstituicao, true, false, false, false, 0, null);
	}

	public static ResultSetMap getAllCursosOf(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllCursosOf(cdInstituicao, true, false, false, false, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap getAllCursosOf(int cdInstituicao, Connection connection) {
		return getAllCursosOf(cdInstituicao, true, false, false, false, 0, connection);
	}
	
	public static ResultSetMap getAllCursosOf(int cdInstituicao, boolean lgRegular, boolean lgEspecial, boolean lgEja) {
		return getAllCursosOf(cdInstituicao, true, lgRegular, lgEspecial, lgEja, 0, null);
	}

	public static ResultSetMap getAllCursosOf(int cdInstituicao, boolean lgRegular, boolean lgEspecial, boolean lgEja, Connection connection) {
		return getAllCursosOf(cdInstituicao, true, lgRegular, lgEspecial, lgEja, 0, connection);
	}

	public static ResultSetMap getAllCursosOf(int cdInstituicao, boolean lgRegular, boolean lgEspecial, boolean lgEja, int cdPeriodoLetivo) {
		return getAllCursosOf(cdInstituicao, true, lgRegular, lgEspecial, lgEja, cdPeriodoLetivo, null);
	}

	public static ResultSetMap getAllCursosOf(int cdInstituicao, boolean lgRegular, boolean lgEspecial, boolean lgEja, int cdPeriodoLetivo, Connection connection) {
		return getAllCursosOf(cdInstituicao, true, lgRegular, lgEspecial, lgEja, cdPeriodoLetivo, connection);
	}
	
	public static ResultSetMap getAllCursosOf(int cdInstituicao, boolean lgIncluirSemEtapa) {
		return getAllCursosOf(cdInstituicao, lgIncluirSemEtapa, false, false, false, 0, null);
	}
	
	public static ResultSetMap getAllCursosOf(int cdInstituicao, boolean lgIncluirSemEtapa, Connection connect) {
		return getAllCursosOf(cdInstituicao, lgIncluirSemEtapa, false, false, false, 0, connect);
	}

	/**
	 * Busca todos os cursos segundo alguns argumentos passados
	 * @param cdInstituicao
	 * @param lgIncluirSemEtapa
	 * @param lgRegular
	 * @param lgEspecial
	 * @param lgEja
	 * @param cdPeriodoLetivo
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllCursosOf(int cdInstituicao, boolean lgIncluirSemEtapa, boolean lgRegular, boolean lgEspecial, boolean lgEja, int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			if(cdPeriodoLetivo == 0){
				ResultSetMap rsmPeriodoRecente = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdInstituicao, connection);
				if(rsmPeriodoRecente.next()){
					cdPeriodoLetivo = rsmPeriodoRecente.getInt("cd_periodo_letivo");
				}
			}
			
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_produto_servico, (H.nm_etapa || ' - ' || B.nm_produto_servico) AS nm_curso, B.id_produto_servico AS id_curso, H.nm_etapa, H.sg_tipo_etapa, H.lg_regular, H.lg_especial, H.lg_eja " +
					(cdInstituicao > 0 ? ", C.cd_instituicao " : "") +
					" FROM acd_curso A"+ 
					" JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico) "+ 
					(cdInstituicao > 0 ? " JOIN acd_instituicao_curso C ON (A.cd_curso = C.cd_curso AND C.cd_periodo_letivo = "+cdPeriodoLetivo+") " : "") +
					(lgIncluirSemEtapa ? " LEFT OUTER JOIN " : " JOIN " ) + " acd_curso_etapa G ON (G.cd_curso = A.cd_curso) " +
					" LEFT OUTER JOIN acd_tipo_etapa H ON (H.cd_etapa = G.cd_etapa) " +
					(cdInstituicao > 0 ? " WHERE C.cd_instituicao = "+cdInstituicao : "")+
					(cdInstituicao > 0 && (lgRegular || lgEspecial || lgEja) ? " AND H.lg_regular = "+(lgRegular ? "1" : "0") + " AND H.lg_especial = "+(lgEspecial ? "1" : "0") + " AND H.lg_eja = "+(lgEja ? "1" : "0") : "")+
					" ORDER BY H.nm_etapa, B.nm_produto_servico");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
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
	
	public static ResultSetMap getAllCursosOfComTurmas(int cdInstituicao) {
		return getAllCursosOfComTurmas(cdInstituicao, null);
	}
	public static ResultSetMap getAllCursosOfComTurmas(int cdInstituicao, boolean incluirTurmas) {
		return getAllCursosOfComTurmas(cdInstituicao, 0, false, incluirTurmas, null);
	}
	
	public static ResultSetMap getAllCursosOfComTurmas(int cdInstituicao, Connection connection) {
		return getAllCursosOfComTurmas(cdInstituicao, 0, connection);
	}
	
	public static ResultSetMap getAllCursosOfComTurmas(int cdInstituicao, int cdPeriodoLetivo) {
		return getAllCursosOfComTurmas(cdInstituicao, cdPeriodoLetivo, null);
	}
	
	public static ResultSetMap getAllCursosOfComTurmas(int cdInstituicao, int cdPeriodoLetivo, boolean lgSituacaoAlunoCenso) {
		return getAllCursosOfComTurmas(cdInstituicao, cdPeriodoLetivo, lgSituacaoAlunoCenso, false, null);
	}
	
	public static ResultSetMap getAllCursosOfComTurmas(int cdInstituicao, int cdPeriodoLetivo, Connection connection) {
		return getAllCursosOfComTurmas(cdInstituicao, cdPeriodoLetivo, false, false, connection);
	}
	
	/**
	 * Busca apenas cursos que conhtenham turmas
	 * @param cdInstituicao
	 * @param cdPeriodoLetivo
	 * @param lgSituacaoAlunoCenso
	 * @param incluirTurmas
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllCursosOfComTurmas(int cdInstituicao, int cdPeriodoLetivo, boolean lgSituacaoAlunoCenso, boolean incluirTurmas, Connection connection) {
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
					"SELECT A.*, B.nm_produto_servico, (H.nm_etapa || ' - ' || B.nm_produto_servico) AS nm_curso, B.id_produto_servico AS id_curso, H.nm_etapa, H.sg_tipo_etapa, H.lg_regular, H.lg_especial, H.lg_eja " +
					" FROM acd_curso A"+ 
					" JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico) "+ 
					" JOIN acd_curso_etapa G ON (G.cd_curso = A.cd_curso) " +
					" LEFT OUTER JOIN acd_tipo_etapa H ON (H.cd_etapa = G.cd_etapa) " +
					" WHERE " + (cdInstituicao > 0 ? "EXISTS (SELECT IC.cd_curso " + 
					"											FROM acd_instituicao_curso IC " + 
					"										  WHERE IC.cd_instituicao = "+cdInstituicao+
					" 											AND IC.cd_curso = A.cd_curso " + 
					"											AND IC.cd_periodo_letivo = "+cdPeriodoRecente+") AND " : "") + 
				    "   EXISTS (SELECT T.cd_turma " + 
					"			  FROM acd_turma T, acd_instituicao_periodo IP " + 
				    "			WHERE T.cd_instituicao = "+cdInstituicao+
				    " 			  AND T.cd_curso = A.cd_curso " + 
				    "			  AND T.cd_periodo_letivo = IP.cd_periodo_letivo " + 
				    "			  AND (IP.cd_periodo_letivo = "+cdPeriodoRecente+
				    " 			  OR IP.cd_periodo_letivo_superior = "+cdPeriodoRecente+") " + 
				    "			  AND T.st_turma IN ("+TurmaServices.ST_ATIVO+", "+TurmaServices.ST_CONCLUIDO+") "+
				    "             AND T.cd_curso NOT IN ("+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+", "+ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0)+")) " +
					" ORDER BY H.nm_etapa, B.nm_produto_servico");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				if(lgSituacaoAlunoCenso){
					ResultSetMap rsmTurmas = new ResultSetMap(connection.prepareStatement("SELECT * FROM acd_turma WHERE cd_instituicao = " + cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo + " AND cd_curso = "+rsm.getInt("cd_curso")+" AND st_turma <> " + TurmaServices.ST_INATIVO).executeQuery()); 
					float qtTurmas = rsmTurmas.size();
					float qtTurmasFechadas = 0;
					while(rsmTurmas.next()){
						ResultSetMap rsmTurmaSituacaoAlunoCenso = new ResultSetMap(connection.prepareStatement("SELECT cd_tipo_ocorrencia FROM grl_ocorrencia A, acd_ocorrencia_turma B WHERE A.cd_ocorrencia = B.cd_ocorrencia AND A.cd_tipo_ocorrencia IN (" + InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO + ", " + InstituicaoEducacensoServices.TP_OCORRENCIA_LIBERACAO_TURMA_SITUACAO_ALUNO_CENSO + ") AND cd_turma = "+rsmTurmas.getInt("cd_turma")+" order by dt_ocorrencia DESC").executeQuery());
						if(rsmTurmaSituacaoAlunoCenso.next()){
							if(rsmTurmaSituacaoAlunoCenso.getInt("cd_tipo_ocorrencia") == Integer.parseInt(InstituicaoEducacensoServices.TP_OCORRENCIA_FECHAMENTO_TURMA_SITUACAO_ALUNO_CENSO)){
								qtTurmasFechadas++;
							}
						}
					}
					rsm.setValueToField("PR_TURMAS_FECHADAS", (qtTurmas > 0 ? (qtTurmasFechadas/qtTurmas * 100) : 0));
					rsm.setValueToField("NM_CURSO", rsm.getString("NM_CURSO") + " ("+(Util.formatNumber((qtTurmas > 0 ? (qtTurmasFechadas/qtTurmas * 100) : 0), 2)) + "% de turmas fechadas)");
					
				}
				if(incluirTurmas){
					rsm.setValueToField("RSM_TURMAS", TurmaServices.getAllByCurso(cdInstituicao, rsm.getInt("cd_curso")));
				}
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
	public static ResultSetMap getAllCursosOfMatriculas(int cdPeriodoLetivo) {
		return getAllCursosOfMatriculas(cdPeriodoLetivo, null);
	}
	
	/**
	 * Busca todos os cursos que tenham turmas com alunos matriculados
	 * @param cdPeriodoLetivo
	 * @param connection
	 * @return
	 */
	public static ResultSetMap getAllCursosOfMatriculas(int cdPeriodoLetivo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.nm_produto_servico, (H.nm_etapa || ' - ' || B.nm_produto_servico) AS nm_curso, B.id_produto_servico AS id_curso, H.nm_etapa, H.sg_tipo_etapa, H.lg_regular, H.lg_especial, H.lg_eja " +
					" FROM acd_curso A"+ 
					" JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico) "+ 
					" JOIN acd_curso_etapa G ON (G.cd_curso = A.cd_curso) " +
					" LEFT OUTER JOIN acd_tipo_etapa H ON (H.cd_etapa = G.cd_etapa) " +
					" WHERE EXISTS (SELECT * FROM acd_matricula M, acd_instituicao_periodo IP WHERE M.cd_curso = A.cd_curso AND M.cd_periodo_letivo = IP.cd_periodo_letivo AND (IP.cd_periodo_letivo = "+cdPeriodoLetivo+" OR IP.cd_periodo_letivo_superior = "+cdPeriodoLetivo+") AND M.st_matricula = "+MatriculaServices.ST_EM_TRANSFERENCIA+") " +
					" ORDER BY H.nm_etapa, B.nm_produto_servico");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
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
	
	/**
	 * Busca todos os cursos que possuem alunos em transferencia
	 * @param cdCurso
	 * @param cdPeriodoLetivo
	 * @return
	 */
	public static ResultSetMap getAllMatriculasEmTransferencia(int cdCurso, int cdPeriodoLetivo)	{
		Connection connect = Conexao.conectar();
		try	{
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_pessoa AS nm_aluno, B.cd_pessoa, AL.*, PF.dt_nascimento, PF.nm_mae, PSC.nm_produto_servico AS NM_CURSO_MATRICULA, (T_CUR.nm_produto_servico || ' - ' || T.nm_turma) AS NM_TURMA_COMPLETO, T_INS.nm_pessoa AS NM_INSTITUICAO " +
					 "FROM acd_matricula A " +
					 "JOIN acd_turma T ON (A.cd_turma = T.cd_turma) " +
					 "JOIN grl_produto_servico T_CUR ON (T.cd_curso = T_CUR.cd_produto_servico) " +
					 "JOIN grl_pessoa T_INS ON (T.cd_instituicao = T_INS.cd_pessoa) " +
                     "JOIN grl_pessoa B ON (A.cd_aluno = B.cd_pessoa) " +
                     "JOIN grl_pessoa_fisica PF ON (A.cd_aluno = PF.cd_pessoa) " +
                     "JOIN acd_aluno AL ON (A.cd_aluno = AL.cd_aluno) " +
                     "JOIN grl_produto_servico PSC ON (A.cd_curso = PSC.cd_produto_servico) " +
                     "JOIN acd_instituicao_periodo IP ON (A.cd_periodo_letivo = IP.cd_periodo_letivo) " +
                     "WHERE A.cd_curso = "+cdCurso+ " AND (IP.cd_periodo_letivo = "+cdPeriodoLetivo+" OR IP.cd_periodo_letivo_superior = "+cdPeriodoLetivo+")AND A.ST_MATRICULA = "+MatriculaServices.ST_EM_TRANSFERENCIA+" ORDER BY T_INS.nm_pessoa, T_CUR.nm_produto_servico, B.nm_pessoa").executeQuery());
			while(rsm.next()){
				rsm.setValueToField("NM_DT_NASCIMENTO", Util.convCalendarString3(rsm.getGregorianCalendar("dt_nascimento")));
				rsm.setValueToField("NM_DT_MATRICULA", Util.convCalendarString3(rsm.getGregorianCalendar("dt_matricula")));
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

	
	public static ResultSetMap findCursosOfertados(ArrayList<ItemComparator> criterios) {
		return findCursosOfertados(criterios, null);
	}
	
	/**
	 * Busca todos os cursos que são ofertados segundo alguns critérios
	 * @param criterios
	 * @param connect
	 * @return
	 */
	public static ResultSetMap findCursosOfertados(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNul = connect == null;
		try{
			if(isConnectionNul){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdPeriodoLetivo 	      = 0;
			int cdInstituicao         	  = 0;
			boolean lgApenasPossuiTurmas  = false;
			boolean semCursoAee  = false;
			boolean retirarCursosExtras  = false;
			boolean incluirTurmas        = false;
			
			for(int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("B.CD_INSTITUICAO")) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("B.CD_PERIODO_LETIVO")) {
					cdPeriodoLetivo = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("buscarPeriodo")) {
					ResultSetMap rsmPeriodoLetivoAtual = InstituicaoPeriodoServices.getPeriodoRecenteOfInstituicao(cdInstituicao, connect);
					if(rsmPeriodoLetivoAtual.next())
						cdPeriodoLetivo = rsmPeriodoLetivoAtual.getInt("cd_periodo_letivo");
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgApenasPossuiTurmas")) {
					lgApenasPossuiTurmas = true;
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("semCursoAee")) {
					semCursoAee = true;
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("retirarCursosExtras")) {
					retirarCursosExtras = true;
					criterios.remove(i--);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("incluirTurmas")) {
					incluirTurmas = true;
					criterios.remove(i--);
				}
			}
			
			if(cdPeriodoLetivo == 0 && cdInstituicao > 0){
				ResultSetMap rsmPeriodoLetivo = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
				if(rsmPeriodoLetivo.next()){
					cdPeriodoLetivo = rsmPeriodoLetivo.getInt("cd_periodo_letivo");
				}
			}
			
			ResultSetMap rsm = Search.find(
					"SELECT C.cd_curso, C2.NM_PRODUTO_SERVICO, C2.nm_produto_servico as nm_curso, C.lg_multi "+
					"FROM acd_curso                C "+
					"JOIN acd_instituicao_curso A1 ON (A1.cd_curso = C.cd_curso AND A1.cd_instituicao = "+cdInstituicao+" AND A1.cd_periodo_letivo = "+cdPeriodoLetivo+")  " +
//					"LEFT OUTER JOIN acd_turma                B   ON  ( C.cd_curso = B.cd_curso AND B.CD_PERIODO_LETIVO = "+cdPeriodoLetivo+" AND B.cd_instituicao = "+cdInstituicao+")  " + 
//					"LEFT OUTER JOIN acd_matricula            M   ON  ( C.cd_curso = M.cd_curso AND M.CD_PERIODO_LETIVO = "+cdPeriodoLetivo+")   " +
//					"JOIN acd_instituicao_periodo  B2  ON  ( B.cd_periodo_letivo = B2.cd_periodo_letivo)   " +
					"JOIN grl_produto_servico      C2  ON  ( C.cd_curso = C2.cd_produto_servico )  WHERE 1=1 "+
					(semCursoAee ? " AND C2.id_produto_servico <> 'AEE'" : "") +
					(retirarCursosExtras ? " AND (C.cd_curso <> " + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_ATENDIMENTO_ESPECIALIZADO", 0) + " AND C.cd_curso <> " + ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MAIS_EDUCACAO", 0)+")" : ""),
					" GROUP BY C.cd_curso,  C2.nm_produto_servico "+
					" ORDER BY C2.nm_produto_servico ",
					criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			int o = 0;
			while(rsm.next()){
				ResultSetMap rsmAux = new ResultSetMap(connect.prepareStatement("SELECT COUNT(cd_turma) AS NR_TURMAS, SUM(qt_vagas) AS NR_VAGAS FROM acd_turma WHERE cd_instituicao = " + cdInstituicao + " AND cd_periodo_letivo = " + cdPeriodoLetivo + " AND cd_curso = " + rsm.getInt("cd_curso") + " and nm_turma NOT LIKE 'TRANS%' AND st_turma = " + TurmaServices.ST_ATIVO).executeQuery());
				if(rsmAux.next()){
					rsm.setValueToField("NR_TURMAS", rsmAux.getInt("NR_TURMAS"));
					rsm.setValueToField("NR_VAGAS", rsmAux.getInt("NR_VAGAS"));
					
					if(lgApenasPossuiTurmas && rsm.getInt("NR_TURMAS") == 0){
						rsm.deleteRow();
						if(o == 0)
							rsm.beforeFirst();
						else
							rsm.previous();
						continue;
					}
					
				}
				
				if(isCreche(rsm.getInt("cd_curso"), connect)){
					rsm.setValueToField("LG_CRECHE", 1);
				}
				else{
					rsm.setValueToField("LG_CRECHE", 0);
				}
				
				if(isEja(rsm.getInt("cd_curso"), connect)){
					rsm.setValueToField("LG_EJA", 1);
				}
				else{
					rsm.setValueToField("LG_EJA", 0);
				}
				
				rsm.setValueToField("CD_PERIODO_LETIVO", cdPeriodoLetivo);
				
				ResultSetMap rsmPeriodo = InstituicaoPeriodoServices.getPeriodoOfInstituicao(cdInstituicao, cdPeriodoLetivo, connect);
				if(rsmPeriodo.next()){
					rsm.setValueToField("CD_PERIODO_LETIVO_BUSCADO", rsmPeriodo.getInt("cd_periodo_letivo"));
					rsm.setValueToField("CD_INSTITUICAO", rsmPeriodo.getInt("cd_instituicao"));
				}
				
				//Caso as turmas sejam incluidas, ele irá buscar, porém haverá perca de desempenho
				if(incluirTurmas){
					rsm.setValueToField("RSM_TURMAS", InstituicaoServices.getAllTurmasByInstituicaoPeriodo(cdInstituicao, cdPeriodoLetivo, true, false, false, rsm.getInt("cd_curso")));
				}
				
				o++;
			}
			
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException e){
			e.printStackTrace();
			if(isConnectionNul)
				Conexao.rollback(connect);
			return null;
		}
		catch(Exception e){
			e.printStackTrace();
			if(isConnectionNul)
				Conexao.rollback(connect);
			return null;
		}
		finally{
			if(isConnectionNul)
				Conexao.desconectar(connect);
		}
	}
	
//	public static ResultSetMap findCursosOfertados(ArrayList<ItemComparator> criterios) {
//		return findCursosOfertados(criterios, null);
//	}
//	
//	public static ResultSetMap findCursosOfertados(ArrayList<ItemComparator> criterios, Connection connect) {
//		return Search.find(
//				
//				"SELECT C.cd_curso, B.cd_periodo_letivo, C2.nm_produto_servico as nm_curso, C3.cd_matriz, C3.nm_matriz "+
//				"FROM acd_oferta               A "+
//				"JOIN acd_turma                B   ON  ( A.cd_turma = B.cd_turma ) "+
//				"JOIN acd_curso                C   ON  ( A.cd_curso = C.cd_curso ) "+
//				"JOIN grl_produto_servico      C2  ON  ( C.cd_curso = C2.cd_produto_servico ) "+
//				"JOIN acd_curso_matriz         C3  ON  ( C.cd_curso = C3.cd_curso) "+
//				"JOIN acd_curso_modulo         D   ON  ( D.cd_curso = C.cd_curso ) ",
//				
//				
//				
////				"SELECT B.cd_curso, C.nm_produto_servico AS nm_curso, D.cd_matriz, D.nm_matriz, " +
////				"		E.cd_turma, E.nm_turma, E.id_turma, E.tp_turno, F.cd_periodo_letivo, F.nm_periodo_letivo " +
////				"FROM acd_instituicao_curso 			 A " +
////				"LEFT OUTER JOIN acd_curso				 B ON (A.cd_curso = B.cd_curso) " +
////				"LEFT OUTER JOIN grl_produto_servico 	 C ON (A.cd_curso = C.cd_produto_servico) " +
////				"LEFT OUTER JOIN acd_curso_matriz		 D ON (A.cd_curso = D.cd_curso) " +
////				"JOIN acd_turma							 E ON (B.cd_curso = E.cd_curso) " +
////				"LEFT OUTER JOIN acd_instituicao_periodo F ON (E.cd_periodo_letivo = F.cd_periodo_letivo) " +
////				"LEFT OUTER JOIN acd_curso_modulo		 G ON( A.cd_curso = G.cd_curso ) " +
////				"LEFT OUTER JOIN grl_produto_servico	 H ON (G.cd_curso_modulo = H.cd_produto_servico)  " +
////				"WHERE  B.cd_curso in ( SELECT A1.cd_curso " +
////				"						FROM acd_oferta A1  " +
////				"                       WHERE A1.cd_curso = B.cd_curso ) ",
//				" GROUP BY B.cd_periodo_letivo, C.cd_curso,  C2.nm_produto_servico, "+
//				" C3.cd_matriz, C3.nm_matriz "+
//				" ORDER BY C2.nm_produto_servico ",
//				criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
//	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int cdPeriodoLetivoOrigem = 0;
			int cdInstituicao = 0;
			int cdPeriodoLetivoAtual = 0;
			for(int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("C.CD_INSTITUICAO")) {
					cdInstituicao = Integer.parseInt(criterios.get(i).getValue());
					ResultSetMap rsmPeriodoAtual = InstituicaoServices.getPeriodoLetivoVigente(cdInstituicao, connect);
					if(rsmPeriodoAtual.next()){
						cdPeriodoLetivoAtual = rsmPeriodoAtual.getInt("cd_periodo_letivo");
					}
					criterios.remove(i--);					
				}				
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdPeriodoLetivoOrigem")) {
					cdPeriodoLetivoOrigem = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i--);
				}
			}
			
			String limit = "";
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if(criterios.get(i).getColumn().equalsIgnoreCase("limit")) {
					limit += " LIMIT "+ criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}
			}
			
			ResultSetMap rsm = Search.find("SELECT A.*, B.cd_produto_servico, B.nm_produto_servico, B.nm_produto_servico as nm_curso, B.id_produto_servico, "
					+ "D.*, E.cd_curso_modulo, E.nr_ordem, F.nm_produto_servico AS nm_modulo, "
					+ "H.cd_etapa, H.nm_etapa, H.sg_tipo_etapa, H.lg_regular, H.lg_especial, H.lg_eja "+
					(cdInstituicao > 0 ? ", C.cd_instituicao, C2.nm_pessoa " : "")+
					"FROM acd_curso A "+
		            "JOIN grl_produto_servico B ON( A.cd_curso = B.cd_produto_servico ) "+
					(cdInstituicao > 0 ? "LEFT OUTER JOIN acd_instituicao_curso C ON( A.cd_curso = C.cd_curso " + 
									     "												AND C.cd_instituicao = "+cdInstituicao+
									     "												AND C.cd_periodo_letivo = "+cdPeriodoLetivoAtual+") "+
					        			 "JOIN grl_pessoa C2 ON( C.cd_instituicao = C2.cd_pessoa ) " : "") +
		            "LEFT OUTER JOIN acd_curso_matriz D ON( A.cd_curso = D.cd_curso ) " +
		            "LEFT OUTER JOIN acd_curso_modulo E ON( A.cd_curso = E.cd_curso ) " +
		            "LEFT OUTER JOIN grl_produto_servico F ON( F.cd_produto_servico = E.cd_curso_modulo ) " +
		            "LEFT OUTER JOIN acd_curso_etapa G ON( G.cd_curso = A.cd_curso ) " +
		            "LEFT OUTER JOIN acd_tipo_etapa H ON( H.cd_etapa = G.cd_etapa ) " +
		            "WHERE 1 = 1 ", "ORDER BY H.cd_etapa, B.id_produto_servico " +limit,
		            criterios, null, connect!=null ? connect : Conexao.conectar(), connect==null, false);
			while(rsm.next()){
				if(cdInstituicao > 0){
					ResultSetMap rsmPeriodo = InstituicaoPeriodoServices.getPeriodoOfInstituicao(cdInstituicao, cdPeriodoLetivoOrigem, connect);
					if(rsmPeriodo.next())
						rsm.setValueToField("CD_PERIODO_LETIVO_BUSCADO", rsmPeriodo.getInt("cd_periodo_letivo"));
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findSimplificado(ArrayList<ItemComparator> criterios) {
		return findSimplificado(criterios, null);
	}

	public static ResultSetMap findSimplificado(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsm = Search.find("SELECT A.cd_curso, B.cd_produto_servico, B.nm_produto_servico, B.nm_produto_servico AS nm_curso "+
					"FROM acd_curso A "+
		            "JOIN grl_produto_servico B ON( A.cd_curso = B.cd_produto_servico ) "+
					"WHERE 1 = 1 ", "ORDER BY B.nm_produto_servico ",
		            criterios, null, connect!=null ? connect : Conexao.conectar(), connect==null, false);
			return rsm;
		}
		catch(SQLException e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	@Deprecated
	public static int insert(ProdutoServico servico, Curso curso, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int cdCurso = ProdutoServicoDAO.insert(servico, connection);
			if (cdCurso <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			curso.setCdCurso(cdCurso);
			if (CursoDAO.insert(curso, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}
			if (isConnectionNull)
				connection.commit();

			return cdCurso;
		}
		catch(SQLException e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	@Deprecated
	public static int update(ProdutoServico produtoServico, Curso curso) {
		return update(produtoServico, curso, null);
	}
	@Deprecated
	public static int update(ProdutoServico produtoServico, Curso curso, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int cdRetorno = produtoServico instanceof Produto ? ProdutoDAO.update(((Produto)produtoServico), connection) :
				ProdutoServicoDAO.update(produtoServico, connection);
			if (cdRetorno <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (CursoDAO.get(curso.getCdCurso(), connection)==null) {
				if (CursoDAO.insert(curso, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}
			else
				if (CursoDAO.update(curso, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}

			if (isConnectionNull)
				connection.commit();
			return cdRetorno;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Curso getCodById(String idCurso) {
		return getById(idCurso, null);
	}
	public static Curso getById(String idCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.* FROM acd_curso A, grl_produto_servico B WHERE A.cd_curso = B.cd_produto_servico AND B.id_produto_servico=?");
			pstmt.setString(1, idCurso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return CursoDAO.get(rs.getInt("cd_curso"), connect);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean isCreche(int cdCurso){
		return isCreche(cdCurso, null);
	}
	
	/**
	 * Testa para saber se o curso é uma creche
	 * 2 Anos
	 * 3 Anos
	 * @param cdCurso
	 * @param connect
	 * @return
	 */
	public static boolean isCreche(int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt =connect.prepareStatement("SELECT * FROM acd_curso_multi WHERE cd_curso_multi = ? AND cd_curso = " + cdCurso);
			pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_CRECHE", 0));
			ResultSetMap rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
			return rsmCursoMatricula.next();
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.get: " + sqlExpt);
			return false;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoServices.isCreche: " + e);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean isEducacaoInfantil(int cdCurso){
		return isEducacaoInfantil(cdCurso, null);
	}
	 
	/**
	 * Testa para saber se o curso faz parte de Educação Infantil
	 * 4 Anos
	 * 5 Anos
	 * @param cdCurso
	 * @param connect
	 * @return
	 */
	public static boolean isEducacaoInfantil(int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt =connect.prepareStatement("SELECT * FROM acd_curso_multi WHERE cd_curso_multi = ? AND cd_curso = " + cdCurso);
			pstmt.setInt(1, ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_PRE_ESCOLA", 0));
			ResultSetMap rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
			return rsmCursoMatricula.next();
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.get: " + sqlExpt);
			return false;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoServices.isCreche: " + e);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean isFundamentalRegular(int cdCurso){
		return isFundamentalRegular(cdCurso, null);
	}
	
	/**
	 * Testa para saber se o curso faz parte do fundamental regular
	 * 
	 * Do 1º ao 9º ano
	 * 
	 * @param cdCurso
	 * @param connect
	 * @return
	 */
	public static boolean isFundamentalRegular(int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			return cdCurso == 98  ||
				cdCurso == 97 ||
				cdCurso == 96 ||
	  		    cdCurso == 95 ||
				cdCurso == 94 ||
				cdCurso == 93 ||
				cdCurso == 92 ||
				cdCurso == 91 ||
				cdCurso == 90 ||
				cdCurso == 1149 ||
				cdCurso == 1151 ||
				cdCurso == 1153 ||
				cdCurso == 1155 ||
				cdCurso == 1157 ||
				cdCurso == 1201 ||
				cdCurso == 1199 ||
				cdCurso == 1183 ||
				cdCurso == 1185 ||
				cdCurso == 1161 ||
				cdCurso == 1163 ||
				cdCurso == 1165 ||
				cdCurso == 1167 ||
				cdCurso == 1169 ||
				cdCurso == 1171 ||
				cdCurso == 1173 ||
				cdCurso == 1175 ||
				cdCurso == 1177;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoServices.isFundamentalRegular: " + e);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static boolean isEja(int cdCurso){
		return isEja(cdCurso, null);
	}
	
	/**
	 * Testa para saber se o curso faz parte da Educação de Jovens e Adultos
	 * @param cdCurso
	 * @param connect
	 * @return
	 */
	public static boolean isEja(int cdCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt =connect.prepareStatement("SELECT * FROM acd_curso_multi WHERE (cd_curso_multi = ? OR cd_curso_multi = ?) AND cd_curso = " + cdCurso);
			int cdAnosIniciais = ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_ANOS_INICIAIS", 0);
			int cdAnosFinais = ParametroServices.getValorOfParametroAsInteger("CD_CURSO_MULTI_ANOS_FINAIS", 0);
			pstmt.setInt(1, cdAnosIniciais);
			pstmt.setInt(2, cdAnosFinais);
			ResultSetMap rsmCursoMatricula = new ResultSetMap(pstmt.executeQuery());
			return rsmCursoMatricula.next() || cdCurso == cdAnosIniciais || cdCurso == cdAnosFinais;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.get: " + sqlExpt);
			return false;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoServices.isCreche: " + e);
			return false;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static int getCursoCompativel(Turma turmaNova, Matricula matriculaAntiga){
		return getCursoCompativel(turmaNova, matriculaAntiga, null);
	}
	
	/**
	 * Testa para saber se os cursos da turma e da matricula passada são compativeis
	 * @param turmaNova
	 * @param matriculaAntiga
	 * @param connect
	 * @return
	 */
	public static int getCursoCompativel(Turma turmaNova, Matricula matriculaAntiga, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			Curso cursoTurmaNova = CursoDAO.get(turmaNova.getCdCurso(), connect);
			Curso cursoMatriculaAntiga = CursoDAO.get(matriculaAntiga.getCdCurso(), connect);
			
			if(cursoTurmaNova.getLgMulti() == 0){
				return turmaNova.getCdCurso();
			}
			else{
				PreparedStatement pstmt = connect.prepareStatement("SELECT A.cd_curso FROM acd_instituicao_curso A, acd_curso B "
						+ "															WHERE A.cd_curso = B.cd_curso "
						+ "															  AND A.cd_instituicao = " + turmaNova.getCdInstituicao()
						+ "															  AND A.cd_periodo_letivo = " + turmaNova.getCdPeriodoLetivo()
						+ "															  AND (EXISTS (SELECT * FROM acd_curso_correspondencia CC WHERE (CC.cd_curso = A.cd_curso AND CC.cd_curso_correspondencia = "+cursoMatriculaAntiga.getCdCurso()+") OR (CC.cd_curso_correspondencia = A.cd_curso AND CC.cd_curso = "+cursoMatriculaAntiga.getCdCurso()+"))"
						+ " 														  		OR A.cd_curso = " + cursoMatriculaAntiga.getCdCurso() + ")"
						+ "															  AND EXISTS (SELECT * FROM acd_curso_multi CM WHERE CM.cd_curso_multi = "+cursoTurmaNova.getCdCurso()+" AND CM.cd_curso = A.cd_curso)");
				ResultSetMap rsmInstituicaoCurso = new ResultSetMap(pstmt.executeQuery());
				if(rsmInstituicaoCurso.next()){
					return rsmInstituicaoCurso.getInt("cd_curso");
				}
				else{
					return 0;
				}					
				
			}
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CursoDAO.get: " + sqlExpt);
			return -1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CursoServices.isCreche: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Verifica a correspondencia de dois cursos passados
	 * @param cdCursoMatricula1
	 * @param cdCursoMatricula2
	 * @param connect
	 * @return
	 */
	public static boolean verificarCorrespondenciMatricula(int cdCursoMatricula1, int cdCursoMatricula2, Connection connect){
		
		if(CursoCorrespondenciaDAO.get(cdCursoMatricula1, cdCursoMatricula2, connect) == null
		&& CursoCorrespondenciaDAO.get(cdCursoMatricula2, cdCursoMatricula1, connect) == null
		&& cdCursoMatricula1 != cdCursoMatricula2){
			return false;
		}
		
		return true;
	}
	
	public static ResultSetMap getAllByProfessor(int cdProfessor, int cdInstituicao) {
		return getAllByProfessor(cdProfessor, cdInstituicao, null);
	}

	public static ResultSetMap getAllByProfessor(int cdProfessor, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT C.*, B.* " +
					"FROM acd_oferta A " +
					"JOIN grl_produto_servico B ON (A.cd_curso = B.cd_produto_servico) " +
					"JOIN acd_curso C ON (A.cd_curso = C.cd_curso) " +
					"JOIN acd_turma            G ON (A.cd_turma = G.cd_turma) " +
					"WHERE 1=1 "+
					"  AND G.cd_instituicao = " + cdInstituicao +
					(cdProfessor > 0 ? " AND EXISTS(SELECT * FROM acd_pessoa_oferta PO WHERE PO.cd_oferta = A.cd_oferta AND PO.cd_pessoa = " + String.valueOf(cdProfessor) + ")" : "")).executeQuery());
			
			ResultSetMap rsmFinal = new ResultSetMap();
			ArrayList<Integer> codigos = new ArrayList<Integer>();
			while(rsm.next()){
				if(!codigos.contains(rsm.getInt("CD_CURSO"))){
					rsmFinal.addRegister(rsm.getRegister());
					codigos.add(rsm.getInt("CD_CURSO"));
				}
			}
			
			return rsmFinal;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}