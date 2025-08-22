package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoComponenteDAO{

	public static int insert(TipoComponente objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoComponente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_tipo_componente", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoComponente(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_tipo_componente (cd_tipo_componente,"+
			                                  "nm_tipo_componente,"+
			                                  "txt_tipo_componente,"+
			                                  "qt_hodometro_validade,"+
			                                  "qt_hodometro_manutencao,"+
			                                  "tp_recorrencia_manutencao,"+
			                                  "qt_intervalo_recorrencia) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoComponente());
			pstmt.setString(3,objeto.getTxtTipoComponente());
			pstmt.setFloat(4,objeto.getQtHodometroValidade());
			pstmt.setFloat(5,objeto.getQtHodometroManutencao());
			pstmt.setInt(6,objeto.getTpRecorrenciaManutencao());
			pstmt.setInt(7,objeto.getQtIntervaloRecorrencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoComponenteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoComponenteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoComponente objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoComponente objeto, int cdTipoComponenteOld) {
		return update(objeto, cdTipoComponenteOld, null);
	}

	public static int update(TipoComponente objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoComponente objeto, int cdTipoComponenteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_tipo_componente SET cd_tipo_componente=?,"+
												      		   "nm_tipo_componente=?,"+
												      		   "txt_tipo_componente=?,"+
												      		   "qt_hodometro_validade=?,"+
												      		   "qt_hodometro_manutencao=?,"+
												      		   "tp_recorrencia_manutencao=?,"+
												      		   "qt_intervalo_recorrencia=? WHERE cd_tipo_componente=?");
			pstmt.setInt(1,objeto.getCdTipoComponente());
			pstmt.setString(2,objeto.getNmTipoComponente());
			pstmt.setString(3,objeto.getTxtTipoComponente());
			pstmt.setFloat(4,objeto.getQtHodometroValidade());
			pstmt.setFloat(5,objeto.getQtHodometroManutencao());
			pstmt.setInt(6,objeto.getTpRecorrenciaManutencao());
			pstmt.setInt(7,objeto.getQtIntervaloRecorrencia());
			pstmt.setInt(8, cdTipoComponenteOld!=0 ? cdTipoComponenteOld : objeto.getCdTipoComponente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoComponenteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoComponenteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoComponente) {
		return delete(cdTipoComponente, null);
	}

	public static int delete(int cdTipoComponente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_tipo_componente WHERE cd_tipo_componente=?");
			pstmt.setInt(1, cdTipoComponente);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoComponenteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoComponenteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoComponente get(int cdTipoComponente) {
		return get(cdTipoComponente, null);
	}

	public static TipoComponente get(int cdTipoComponente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_componente WHERE cd_tipo_componente=?");
			pstmt.setInt(1, cdTipoComponente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoComponente(rs.getInt("cd_tipo_componente"),
						rs.getString("nm_tipo_componente"),
						rs.getString("txt_tipo_componente"),
						rs.getFloat("qt_hodometro_validade"),
						rs.getFloat("qt_hodometro_manutencao"),
						rs.getInt("tp_recorrencia_manutencao"),
						rs.getInt("qt_intervalo_recorrencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoComponenteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoComponenteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_tipo_componente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoComponenteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoComponenteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_tipo_componente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
