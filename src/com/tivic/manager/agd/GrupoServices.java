package com.tivic.manager.agd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class GrupoServices {

	public static final int VIEW_PRIVADO = 0;
	public static final int VIEW_PUBLICO = 1;

	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;

	public static Result save(Grupo grupo){
		return save(grupo, null);
	}
	
	public static Result save(Grupo grupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(grupo==null)
				return new Result(-1, "Erro ao salvar. Grupo é nulo");
			
			int retorno;
			if(grupo.getCdGrupo()==0){
				retorno = GrupoDAO.insert(grupo, connect);
				grupo.setCdGrupo(retorno);
			}
			else {
				retorno = GrupoDAO.update(grupo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "GRUPO", grupo);
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
	
	public static Result remove(int cdGrupo){
		return remove(cdGrupo, false, null);
	}
	
	public static Result remove(int cdGrupo, boolean cascade){
		return remove(cdGrupo, cascade, null);
	}
	
	public static Result remove(int cdGrupo, boolean cascade, Connection connect){
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
				retorno = GrupoDAO.delete(cdGrupo, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este grupo está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Grupo excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir grupo!");
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
			pstmt = connect.prepareStatement("SELECT A.*, C.nm_pessoa, C.nm_pessoa as nm_usuario FROM agd_grupo A" +
					" LEFT OUTER JOIN seg_usuario B ON (B.cd_usuario = A.cd_proprietario)" +
					" LEFT OUTER JOIN grl_pessoa C ON (C.cd_pessoa = B.cd_pessoa)" +
					" ORDER BY A.nm_grupo");
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
	
	public static ResultSetMap getAtivos() {
		return getAtivos(null);
	}

	public static ResultSetMap getAtivos(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<>();
			criterios.add(new ItemComparator("ST_GRUPO", Integer.toString(ST_ATIVO), ItemComparator.EQUAL, Types.INTEGER));
			return find(criterios, connect);
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
	
	public static ResultSetMap getAll(int cdProprietario) {
		return getAll(cdProprietario, null);
	}

	public static ResultSetMap getAll(int cdProprietario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM agd_grupo A " +
					"WHERE A.cd_proprietario = ? " +
					"   OR A.tp_visibilidade = " + VIEW_PUBLICO);
			pstmt.setInt(1, cdProprietario);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> criterios = new ArrayList<String>();
			criterios.add("NM_GRUPO");
			rsm.orderBy(criterios);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllContatos(int cdProprietario, int cdGrupo) {
		return getAllContatos(cdProprietario, cdGrupo, null, null);
	}

	public static ResultSetMap getAllContatos(int cdProprietario, int cdGrupo, String patternNomeContato) {
		return getAllContatos(cdProprietario, cdGrupo, patternNomeContato, null);
	}

	public static ResultSetMap getAllContatos(int cdProprietario, int cdGrupo, String patternNomeContato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.cd_grupo, C.tp_visibilidade " +
					"FROM grl_pessoa A, agd_grupo_pessoa B, agd_grupo C " +
					"WHERE A.cd_pessoa = B.cd_pessoa " +
					"  AND B.cd_grupo = C.cd_grupo " +
					(cdGrupo>0 ? "  AND B.cd_grupo = ? " : " AND (C.cd_proprietario = ? OR C.tp_visibilidade = " + VIEW_PUBLICO + ")") +
					(patternNomeContato!=null && !patternNomeContato.trim().equals("") ? "  AND A.nm_pessoa LIKE ? " : ""));
			int i = 1;
			if (cdGrupo > 0)
				pstmt.setInt(i++, cdGrupo);
			else if (cdProprietario > 0)
				pstmt.setInt(i++, cdProprietario);
			if (patternNomeContato != null && !patternNomeContato.trim().equals(""))
				pstmt.setString(i++, patternNomeContato.toUpperCase() + "%");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm != null && rsm.next()) {
				HashMap<String,Object> register = rsm.getRegister();
				pstmt = connect.prepareStatement("SELECT A.cd_endereco, A.nr_telefone " +
						"FROM grl_pessoa_endereco A " +
						"WHERE A.cd_pessoa = ?");
				pstmt.setInt(1, rsm.getInt("cd_pessoa"));
				register.put("rsmFones", new ResultSetMap(pstmt.executeQuery()));
			}
			ArrayList<String> criterios = new ArrayList<String>();
			criterios.add("NM_PESSOA");
			rsm.orderBy(criterios);
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoServices.getAllContatos: " + e);
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
		return Search.find("SELECT * FROM agd_grupo ", " ORDER BY nm_grupo ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
