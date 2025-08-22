package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class FiltroRelatorioDAO{

	public static int insert(FiltroRelatorio objeto) {
		return insert(objeto, null);
	}

	public static int insert(FiltroRelatorio objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_filtro_relatorio", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFiltroRelatorio(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_filtro_relatorio (cd_filtro_relatorio,"+
			                                  "cd_formulario,"+
			                                  "nm_relatorio,"+
			                                  "ds_relatorio,"+
			                                  "tp_relatorio,"+
			                                  "st_relatorio,"+
			                                  "cd_pessoa,"+
			                                  "cd_usuario,"+
			                                  "json_filtro_relatorio) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFormulario());
			pstmt.setString(3,objeto.getNmRelatorio());
			pstmt.setString(4,objeto.getDsRelatorio());
			pstmt.setInt(5,objeto.getTpRelatorio());
			pstmt.setInt(6,objeto.getStRelatorio());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdPessoa());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUsuario());
			pstmt.setString(9,objeto.getJsonFiltroRelatorio());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FiltroRelatorio objeto) {
		return update(objeto, 0, null);
	}

	public static int update(FiltroRelatorio objeto, int cdFiltroRelatorioOld) {
		return update(objeto, cdFiltroRelatorioOld, null);
	}

	public static int update(FiltroRelatorio objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(FiltroRelatorio objeto, int cdFiltroRelatorioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_filtro_relatorio SET cd_filtro_relatorio=?,"+
												      		   "cd_formulario=?,"+
												      		   "nm_relatorio=?,"+
												      		   "ds_relatorio=?,"+
												      		   "tp_relatorio=?,"+
												      		   "st_relatorio=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_usuario=?,"+
												      		   "json_filtro_relatorio=? WHERE cd_filtro_relatorio=?");
			pstmt.setInt(1,objeto.getCdFiltroRelatorio());
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFormulario());
			pstmt.setString(3,objeto.getNmRelatorio());
			pstmt.setString(4,objeto.getDsRelatorio());
			pstmt.setInt(5,objeto.getTpRelatorio());
			pstmt.setInt(6,objeto.getStRelatorio());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdPessoa());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUsuario());
			pstmt.setString(9,objeto.getJsonFiltroRelatorio());
			pstmt.setInt(10, cdFiltroRelatorioOld!=0 ? cdFiltroRelatorioOld : objeto.getCdFiltroRelatorio());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFiltroRelatorio) {
		return delete(cdFiltroRelatorio, null);
	}

	public static int delete(int cdFiltroRelatorio, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_filtro_relatorio WHERE cd_filtro_relatorio=?");
			pstmt.setInt(1, cdFiltroRelatorio);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FiltroRelatorio get(int cdFiltroRelatorio) {
		return get(cdFiltroRelatorio, null);
	}

	public static FiltroRelatorio get(int cdFiltroRelatorio, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_filtro_relatorio WHERE cd_filtro_relatorio=?");
			pstmt.setInt(1, cdFiltroRelatorio);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FiltroRelatorio(rs.getInt("cd_filtro_relatorio"),
						rs.getInt("cd_formulario"),
						rs.getString("nm_relatorio"),
						rs.getString("ds_relatorio"),
						rs.getInt("tp_relatorio"),
						rs.getInt("st_relatorio"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_usuario"),
						rs.getString("json_filtro_relatorio"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_filtro_relatorio");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<FiltroRelatorio> getList() {
		return getList(null);
	}

	public static ArrayList<FiltroRelatorio> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<FiltroRelatorio> list = new ArrayList<FiltroRelatorio>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				FiltroRelatorio obj = FiltroRelatorioDAO.get(rsm.getInt("cd_filtro_relatorio"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FiltroRelatorioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_filtro_relatorio", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}