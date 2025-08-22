package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class FormacaoCursoServices {

	/*Tipo de grau acadêmico*/
	public static final int TP_GRAU_BACHARELADO  = 0;
	public static final int TP_GRAU_LICENCIATURA = 1;
	public static final int TP_GRAU_TECNOLOGICO  = 2;
	
	public static Result save(FormacaoCurso formacaoCurso){
		return save(formacaoCurso, null);
	}

	public static Result save(FormacaoCurso formacaoCurso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(formacaoCurso==null)
				return new Result(-1, "Erro ao salvar. FormacaoCurso é nulo");

			int retorno;
			if(formacaoCurso.getCdFormacaoCurso()==0){
				retorno = FormacaoCursoDAO.insert(formacaoCurso, connect);
				formacaoCurso.setCdFormacaoCurso(retorno);
			}
			else {
				retorno = FormacaoCursoDAO.update(formacaoCurso, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FORMACAOCURSO", formacaoCurso);
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
	public static Result remove(int cdFormacaoCurso){
		return remove(cdFormacaoCurso, false, null);
	}
	public static Result remove(int cdFormacaoCurso, boolean cascade){
		return remove(cdFormacaoCurso, cascade, null);
	}
	public static Result remove(int cdFormacaoCurso, boolean cascade, Connection connect){
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
			retorno = FormacaoCursoDAO.delete(cdFormacaoCurso, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_formacao_curso");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllFormacaoCurso() {
		return getAllFormacaoCurso(null);
	}

	public static ResultSetMap getAllFormacaoCurso(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("lgPrincipais", "" + true, ItemComparator.EQUAL, Types.BOOLEAN));
			
			return find(criterios);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoServices.getAllFormacaoCurso: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllFormacaoOutrosCursos() {
		return getAllFormacaoOutrosCursos(null);
	}

	public static ResultSetMap getAllFormacaoOutrosCursos(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("lgPrincipais", "" + false, ItemComparator.EQUAL, Types.BOOLEAN));
			
			return find(criterios);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoServices.getAllFormacaoOutrosCursos: " + e);
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
		boolean isConnectionNull = connect==null;
		try{
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			//No momento, variavel utilizada para separar os cursos principais (de formação superior) dos cursos de formação continuada
			boolean lgPrincipais = true;
			for(int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("lgPrincipais")) {
					lgPrincipais = (criterios.get(i).getValue().trim().equals("true"));
					criterios.remove(i--);
				}
			}
			
			
			return Search.find("SELECT * FROM acd_formacao_curso WHERE 1=1  AND cd_formacao_area_conhecimento " + (lgPrincipais ? "  IS NOT NULL " : "  IS NULL " ), "", criterios,  connect!=null ? connect : Conexao.conectar(), connect==null);
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
	
	public static FormacaoCurso getByOcde(String idOcde) {
		return getByOcde(idOcde, null);
	}

	public static FormacaoCurso getByOcde(String idOcde, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_formacao_curso WHERE id_ocde=?");
			pstmt.setString(1, idOcde);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return FormacaoCursoDAO.get(rs.getInt("cd_formacao_curso"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormacaoCursoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
