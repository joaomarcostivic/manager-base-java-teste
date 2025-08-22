package com.tivic.manager.bpm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ComponenteReferenciaDAO{

	public static int insert(ComponenteReferencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(ComponenteReferencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("bpm_componente_referencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdComponente(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO bpm_componente_referencia (cd_componente,"+
			                                  "cd_referencia,"+
			                                  "nm_componente,"+
			                                  "dt_garantia,"+
			                                  "dt_validade,"+
			                                  "dt_aquisicao,"+
			                                  "dt_baixa,"+
			                                  "nr_serie,"+
			                                  "st_componente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdReferencia());
			pstmt.setString(3,objeto.getNmComponente());
			if(objeto.getDtGarantia()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtGarantia().getTimeInMillis()));
			if(objeto.getDtValidade()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			if(objeto.getDtAquisicao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtAquisicao().getTimeInMillis()));
			if(objeto.getDtBaixa()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtBaixa().getTimeInMillis()));
			pstmt.setString(8,objeto.getNrSerie());
			pstmt.setInt(9,objeto.getStComponente());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteReferenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteReferenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ComponenteReferencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ComponenteReferencia objeto, int cdComponenteOld) {
		return update(objeto, cdComponenteOld, null);
	}

	public static int update(ComponenteReferencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ComponenteReferencia objeto, int cdComponenteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE bpm_componente_referencia SET cd_componente=?,"+
												      		   "cd_referencia=?,"+
												      		   "nm_componente=?,"+
												      		   "dt_garantia=?,"+
												      		   "dt_validade=?,"+
												      		   "dt_aquisicao=?,"+
												      		   "dt_baixa=?,"+
												      		   "nr_serie=?,"+
												      		   "st_componente=? WHERE cd_componente=?");
			pstmt.setInt(1,objeto.getCdComponente());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdReferencia());
			pstmt.setString(3,objeto.getNmComponente());
			if(objeto.getDtGarantia()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtGarantia().getTimeInMillis()));
			if(objeto.getDtValidade()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			if(objeto.getDtAquisicao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtAquisicao().getTimeInMillis()));
			if(objeto.getDtBaixa()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtBaixa().getTimeInMillis()));
			pstmt.setString(8,objeto.getNrSerie());
			pstmt.setInt(9,objeto.getStComponente());
			pstmt.setInt(10, cdComponenteOld!=0 ? cdComponenteOld : objeto.getCdComponente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteReferenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteReferenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdComponente) {
		return delete(cdComponente, null);
	}

	public static int delete(int cdComponente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bpm_componente_referencia WHERE cd_componente=?");
			pstmt.setInt(1, cdComponente);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteReferenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteReferenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ComponenteReferencia get(int cdComponente) {
		return get(cdComponente, null);
	}

	public static ComponenteReferencia get(int cdComponente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bpm_componente_referencia WHERE cd_componente=?");
			pstmt.setInt(1, cdComponente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ComponenteReferencia(rs.getInt("cd_componente"),
						rs.getInt("cd_referencia"),
						rs.getString("nm_componente"),
						(rs.getTimestamp("dt_garantia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_garantia").getTime()),
						(rs.getTimestamp("dt_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade").getTime()),
						(rs.getTimestamp("dt_aquisicao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_aquisicao").getTime()),
						(rs.getTimestamp("dt_baixa")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_baixa").getTime()),
						rs.getString("nr_serie"),
						rs.getInt("st_componente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteReferenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteReferenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM bpm_componente_referencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteReferenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteReferenciaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM bpm_componente_referencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
