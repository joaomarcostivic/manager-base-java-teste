package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class GrupoDAO{

	public static int insert(Grupo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Grupo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("alm_grupo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdGrupo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_grupo (cd_grupo,"+
			                                  "cd_grupo_superior,"+
			                                  "cd_categoria_receita,"+
			                                  "cd_categoria_despesa,"+
			                                  "nm_grupo,"+
			                                  "cd_formulario,"+
			                                  "cd_evento_adesao_contrato,"+
			                                  "cd_evento_contratacao," +
			                                  "st_grupo," +
			                                  "id_grupo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdGrupoSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrupoSuperior());
			if(objeto.getCdCategoriaReceita()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCategoriaReceita());
			if(objeto.getCdCategoriaDespesa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCategoriaDespesa());
			pstmt.setString(5,objeto.getNmGrupo());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdFormulario());
			if(objeto.getCdEventoAdesaoContrato()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEventoAdesaoContrato());
			if(objeto.getCdEventoContratacao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdEventoContratacao());
			pstmt.setInt(9, objeto.getStGrupo());
			pstmt.setString(10, objeto.getIdGrupo());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Grupo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Grupo objeto, int cdGrupoOld) {
		return update(objeto, cdGrupoOld, null);
	}

	public static int update(Grupo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Grupo objeto, int cdGrupoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_grupo SET cd_grupo=?,"+
												      		   "cd_grupo_superior=?,"+
												      		   "cd_categoria_receita=?,"+
												      		   "cd_categoria_despesa=?,"+
												      		   "nm_grupo=?,"+
												      		   "cd_formulario=?,"+
												      		   "cd_evento_adesao_contrato=?,"+
												      		   "cd_evento_contratacao=?," +
												      		   "st_grupo=?," +
												      		   "id_grupo=? WHERE cd_grupo=?");
			pstmt.setInt(1,objeto.getCdGrupo());
			if(objeto.getCdGrupoSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrupoSuperior());
			if(objeto.getCdCategoriaReceita()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCategoriaReceita());
			if(objeto.getCdCategoriaDespesa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCategoriaDespesa());
			pstmt.setString(5,objeto.getNmGrupo());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdFormulario());
			if(objeto.getCdEventoAdesaoContrato()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEventoAdesaoContrato());
			if(objeto.getCdEventoContratacao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdEventoContratacao());
			pstmt.setInt(9,objeto.getStGrupo());
			pstmt.setString(10,objeto.getIdGrupo());
			pstmt.setInt(11, cdGrupoOld!=0 ? cdGrupoOld : objeto.getCdGrupo());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupo) {
		return delete(cdGrupo, null);
	}

	public static int delete(int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_grupo WHERE cd_grupo=?");
			pstmt.setInt(1, cdGrupo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Grupo get(int cdGrupo) {
		return get(cdGrupo, null);
	}

	public static Grupo get(int cdGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_grupo WHERE cd_grupo=?");
			pstmt.setInt(1, cdGrupo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Grupo(rs.getInt("cd_grupo"),
						rs.getInt("cd_grupo_superior"),
						rs.getInt("cd_categoria_receita"),
						rs.getInt("cd_categoria_despesa"),
						rs.getString("nm_grupo"),
						rs.getInt("cd_formulario"),
						rs.getInt("cd_evento_adesao_contrato"),
						rs.getInt("cd_evento_contratacao"),
						rs.getInt("st_grupo"),
						rs.getString("id_grupo"));
			}
			else{
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_grupo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM alm_grupo A ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
