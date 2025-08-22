package com.tivic.manager.srh;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class SindicatoDAO{

	public static int insert(Sindicato objeto) {
		return insert(objeto, null);
	}

	public static int insert(Sindicato objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("srh_sindicato", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdSindicato(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_sindicato (cd_sindicato,"+
			                                  "nm_sindicato,"+
			                                  "nr_mes_recolhimento,"+
			                                  "vl_aplicacao,"+
			                                  "cd_formulario,"+
			                                  "tp_cobranca,"+
			                                  "cd_evento_financeiro,"+
			                                  "id_sindicato) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmSindicato());
			pstmt.setInt(3,objeto.getNrMesRecolhimento());
			pstmt.setFloat(4,objeto.getVlAplicacao());
			pstmt.setInt(5,objeto.getCdFormulario());
			pstmt.setInt(6,objeto.getTpCobranca());
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEventoFinanceiro());
			pstmt.setString(8,objeto.getIdSindicato());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SindicatoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SindicatoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Sindicato objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Sindicato objeto, int cdSindicatoOld) {
		return update(objeto, cdSindicatoOld, null);
	}

	public static int update(Sindicato objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Sindicato objeto, int cdSindicatoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_sindicato SET cd_sindicato=?,"+
												      		   "nm_sindicato=?,"+
												      		   "nr_mes_recolhimento=?,"+
												      		   "vl_aplicacao=?,"+
												      		   "cd_formulario=?,"+
												      		   "tp_cobranca=?,"+
												      		   "cd_evento_financeiro=?,"+
												      		   "id_sindicato=? WHERE cd_sindicato=?");
			pstmt.setInt(1,objeto.getCdSindicato());
			pstmt.setString(2,objeto.getNmSindicato());
			pstmt.setInt(3,objeto.getNrMesRecolhimento());
			pstmt.setFloat(4,objeto.getVlAplicacao());
			pstmt.setInt(5,objeto.getCdFormulario());
			pstmt.setInt(6,objeto.getTpCobranca());
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEventoFinanceiro());
			pstmt.setString(8,objeto.getIdSindicato());
			pstmt.setInt(9, cdSindicatoOld!=0 ? cdSindicatoOld : objeto.getCdSindicato());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SindicatoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SindicatoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdSindicato) {
		return delete(cdSindicato, null);
	}

	public static int delete(int cdSindicato, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_sindicato WHERE cd_sindicato=?");
			pstmt.setInt(1, cdSindicato);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SindicatoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SindicatoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Sindicato get(int cdSindicato) {
		return get(cdSindicato, null);
	}

	public static Sindicato get(int cdSindicato, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_sindicato WHERE cd_sindicato=?");
			pstmt.setInt(1, cdSindicato);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Sindicato(rs.getInt("cd_sindicato"),
						rs.getString("nm_sindicato"),
						rs.getInt("nr_mes_recolhimento"),
						rs.getFloat("vl_aplicacao"),
						rs.getInt("cd_formulario"),
						rs.getInt("tp_cobranca"),
						rs.getInt("cd_evento_financeiro"),
						rs.getString("id_sindicato"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SindicatoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SindicatoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_sindicato");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SindicatoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SindicatoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_sindicato", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}