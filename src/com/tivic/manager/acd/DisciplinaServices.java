package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresaServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class DisciplinaServices {
	
	public static final String TP_DISCIPLINA_QUIMICA									= "1";
	public static final String TP_DISCIPLINA_FISICA										= "2";
	public static final String TP_DISCIPLINA_MATEMATICA									= "3";
	public static final String TP_DISCIPLINA_BIOLOGIA									= "4";
	public static final String TP_DISCIPLINA_CIENCIAS									= "5";
	public static final String TP_DISCIPLINA_LINGUA_PORTUGUESA							= "6";
	public static final String TP_DISCIPLINA_LINGUA_INGLESA								= "7";
	public static final String TP_DISCIPLINA_LINGUA_ESPANHOLA							= "8";
	public static final String TP_DISCIPLINA_LINGUA_OUTRAS								= "9";
	public static final String TP_DISCIPLINA_ARTES										= "10";
	public static final String TP_DISCIPLINA_ED_FISICA									= "11";
	public static final String TP_DISCIPLINA_HISTORIA									= "12";
	public static final String TP_DISCIPLINA_GEOGRAFIA									= "13";
	public static final String TP_DISCIPLINA_FILOSOFIA									= "14";
	public static final String TP_DISCIPLINA_INFORMATICA								= "16";
	public static final String TP_DISCIPLINA_DISC_PROFISSIONALIZANTES					= "17";
	public static final String TP_DISCIPLINA_DISC_ATENDIMENTO_NECESSIDADES_ESPECIAIS	= "20";
	public static final String TP_DISCIPLINA_DISC_DIVERSIDADE_SOCIO_CULTURAL			= "21";
	public static final String TP_DISCIPLINA_LIBRAS										= "23";
	public static final String TP_DISCIPLINA_DISC_PEDAGOGICAS							= "25";
	public static final String TP_DISCIPLINA_RELIGIAO									= "26";
	public static final String TP_DISCIPLINA_LINGUA_INDIGENA							= "27";
	public static final String TP_DISCIPLINA_ESTUDOS_SOCIAIS							= "28";
	public static final String TP_DISCIPLINA_SOCIOLOGIA									= "29";
	public static final String TP_DISCIPLINA_LINGUA_FRANCESA							= "30";
	public static final String TP_DISCIPLINA_LINGUA_PORTUGUESA_SEGUNDA_LINGUA			= "31";
	public static final String TP_DISCIPLINA_ESTAGIO_CURRICULAR_SUPERVISIONADO			= "32";
	public static final String TP_DISCIPLINA_OUTROS										= "99";
	
	public static final String[] tiposClassificacao = {"Base Nacional Comum", "Parte Diversificada", "Parte Extracurricular"};
	
	public static Result save( Disciplina disciplina ){
		return save( disciplina, null );
	}
	
	public static Result save(Disciplina disciplina, Connection connection){
		boolean isConnectionNull = connection==null;
		int retorno;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			if(disciplina==null)
				return new Result(-1, "Erro ao salvar. Disciplina é nula");
			if( disciplina.getCdDisciplina()==0 ){
				retorno = DisciplinaDAO.insert(disciplina, connection);
				disciplina.setCdDisciplina(retorno);
			}else{
				retorno = DisciplinaDAO.update(disciplina, connection);
			}
			 
			int cdSecretaria = ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0);
			if(cdSecretaria > 0){
				ProdutoServicoEmpresa produtoServicoEmpresa = ProdutoServicoEmpresaDAO.get(cdSecretaria, disciplina.getCdDisciplina(), connection);
				if(produtoServicoEmpresa == null){
					produtoServicoEmpresa = new ProdutoServicoEmpresa();
					produtoServicoEmpresa.setCdEmpresa(cdSecretaria);
					produtoServicoEmpresa.setCdProdutoServico(disciplina.getCdDisciplina());
					produtoServicoEmpresa.setStProdutoEmpresa(ProdutoServicoEmpresaServices.ST_ATIVO);
					if(ProdutoServicoEmpresaDAO.insert(produtoServicoEmpresa, connection) < 0){
						if(isConnectionNull)
							Conexao.rollback(connection);
						return new Result(-1, "Erro ao inserir disciplina secretaria");
					}
				}
			}
			
			if(retorno<=0)
				Conexao.rollback(connection);
			else if (isConnectionNull)
				connection.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "DISCIPLINA", disciplina);
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
	
	public static Result remove(int cdDisciplina){
		return remove(cdDisciplina, false, null);
	}
	
	public static Result remove(int cdDisciplina, boolean cascade){
		return remove(cdDisciplina, cascade, null);
	}
	
	public static Result remove(int cdDisciplina, boolean cascade, Connection connect){
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
				retorno = DisciplinaDAO.delete(cdDisciplina, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta disciplina está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Disciplina excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir disciplina!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.*, B.nm_produto_servico AS nm_disciplina, C.nm_area_conhecimento " +
																  "FROM acd_disciplina 						A " +
																  "JOIN grl_produto_servico 				B ON (A.cd_disciplina = B.cd_produto_servico) " +
																  "LEFT OUTER JOIN acd_area_conhecimento	C ON (A.cd_area_conhecimento = C.cd_area_conhecimento)");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("NM_DISCIPLINA_COMBO", rsm.getString("nm_area_conhecimento") + " - " + rsm.getString("nm_disciplina"));
			}
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("nm_area_conhecimento");
			fields.add("nm_produto_servico");
			rsm.orderBy(fields);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getAllAtivas() {
		return getAllAtivas(null);
	}

	public static ResultSetMap getAllAtivas(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, B.*, B.nm_produto_servico AS nm_disciplina, C.nm_area_conhecimento " +
																  "FROM acd_disciplina 						A " +
																  "JOIN grl_produto_servico 				B ON (A.cd_disciplina = B.cd_produto_servico) " +
																  "JOIN grl_produto_servico_empresa			D ON (A.cd_disciplina = D.cd_produto_servico) " +
																  "LEFT OUTER JOIN acd_area_conhecimento	C ON (A.cd_area_conhecimento = C.cd_area_conhecimento) WHERE D.st_produto_empresa = " + ProdutoServicoEmpresaServices.ST_ATIVO);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("NM_DISCIPLINA_COMBO", rsm.getString("nm_area_conhecimento") + " - " + rsm.getString("nm_disciplina"));
			}
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("nm_area_conhecimento");
			fields.add("nm_produto_servico");
			rsm.orderBy(fields);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	/**
	 * Pesquisa simples de disciplinas
	 * 
	 * @param criterios
	 * @return ResultSetMap
	 */
	public static ResultSetMap find2(ArrayList<ItemComparator> criterios) {
		return find2(criterios, null);
	}

	public static ResultSetMap find2(ArrayList<ItemComparator> criterios, Connection connect) {
		
		int cdCurso = 0;
		
		for(int i=0; criterios!=null && i<criterios.size(); i++){
			if (criterios.get(i).getColumn().equalsIgnoreCase("A.CD_INSTITUICAO") && Integer.parseInt(criterios.get(i).getValue()) == ParametroServices.getValorOfParametroAsInteger("CD_INSTITUICAO_SECRETARIA_MUNICIPAL", 0)) {
				criterios.remove(i);
			}
			
			if (criterios.get(i).getColumn().equalsIgnoreCase("cdCurso")) {
				cdCurso = Integer.parseInt(criterios.get(i).getValue()); 
				criterios.remove(i);
			}
			
		}
		
		ResultSetMap rsm = Search.find("SELECT A.*, B.id_produto_servico, B.nm_produto_servico, (B.nm_produto_servico) AS nm_disciplina, D.st_produto_empresa "+
				"FROM acd_disciplina A "+
	            "LEFT OUTER JOIN grl_produto_servico B ON ( A.cd_disciplina = B.cd_produto_servico ) " +
	            "JOIN grl_produto_servico_empresa D ON ( A.cd_disciplina = D.cd_produto_servico ) " +
	            "LEFT OUTER JOIN acd_area_conhecimento C ON ( A.cd_area_conhecimento = C.cd_area_conhecimento ) ",
	            " WHERE D.st_produto_empresa = "+ProdutoServicoEmpresaServices.ST_ATIVO+ (cdCurso <= 0 ? " " : " AND EXISTS (SELECT * FROM acd_curso_disciplina C "
	           + "				WHERE C.cd_disciplina = A.cd_disciplina"
	           + (cdCurso <= 0 ? "" : "				  AND C.cd_curso = "+cdCurso) +") "), criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		ArrayList<String> fields = new ArrayList<String>();
		fields.add("cd_disciplina");
		rsm.orderBy(fields);
		rsm.beforeFirst();
		
		while(rsm.next()){
			rsm.setValueToField("CL_ST_PRODUTO_EMPRESA", ProdutoServicoEmpresaServices.situacoesProduto[rsm.getInt("st_produto_empresa")]);
		}
		rsm.beforeFirst();
		return rsm;
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.id_produto_servico, B.nm_produto_servico, B.nm_produto_servico AS nm_disciplina, "+
							"B2.nm_produto_servico as nm_curso, C.cd_curso, C.cd_curso_modulo, C2.cd_instituicao, C3.nm_pessoa as nm_instituicao, D.cd_curso, D.cd_matriz, D.nm_matriz "+
							"FROM acd_disciplina A "+
				            "LEFT OUTER JOIN grl_produto_servico B ON ( A.cd_disciplina = B.cd_produto_servico ) "+ 
				            "LEFT OUTER JOIN acd_curso_disciplina C ON ( A.cd_disciplina = C.cd_disciplina ) "+
				            "LEFT OUTER JOIN grl_produto_servico B2 ON ( C.cd_curso = B2.cd_produto_servico ) "+ 
				            "LEFT OUTER JOIN acd_instituicao_curso C2 ON ( C.cd_curso = C2.cd_curso ) "+
				            "LEFT OUTER JOIN grl_pessoa C3 ON ( C2.cd_instituicao = C3.cd_pessoa ) "+
				            "LEFT OUTER JOIN acd_instituicao_periodo C4 ON ( C2.cd_instituicao = C4.cd_instituicao"+ 
				            " 											 AND C2.cd_periodo_letivo = C4.cd_periodo_letivo "+ 
				            "											 AND C4.st_periodo_letivo = "+InstituicaoPeriodoServices.ST_ATUAL+" ) "+
				            "LEFT OUTER JOIN acd_curso_matriz D ON ( C.cd_curso = D.cd_curso )",
				            "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static Disciplina getById(String idDisciplina) {
		return getById(idDisciplina, null);
	}

	public static Disciplina getById(String idDisciplina, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_disciplina WHERE id_disciplina=?");
			pstmt.setString(1, idDisciplina);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return DisciplinaDAO.get(rs.getInt("cd_disciplina"), connect);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DisciplinaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findAllByProfessor(ArrayList<ItemComparator> criterios) {
		return findAllByProfessor(criterios, null);
	}
	
	public static ResultSetMap findAllByProfessor(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try{
			
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdProfessor = 0;
			for(int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("cdProfessor")) {
					cdProfessor = Integer.parseInt(criterios.get(i).getValue());
				}
			}
			
			ResultSetMap rsm = getAllByProfessor(cdProfessor, connect);
			
			while(rsm.next()){
				
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
	
	public static ResultSetMap getAllByProfessor(int cdProfessor) {
		return getAllByProfessor(cdProfessor, null);
	}

	public static ResultSetMap getAllByProfessor(int cdProfessor, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, C.nm_produto_servico AS nm_disciplina FROM acd_disciplina A, acd_professor_disciplina B, grl_produto_servico C where A.cd_disciplina = B.cd_disciplina AND A.cd_disciplina = C.cd_produto_servico AND B.cd_professor = "+cdProfessor+" ORDER BY C.nm_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoHorarioDAO.getAllByInstituicao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}