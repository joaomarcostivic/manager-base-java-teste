package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class CreditoSolicitadoDAO{

	public static int insert(CreditoSolicitado objeto) {
		return insert(objeto, null);
	}

	public static int insert(CreditoSolicitado objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mcr_credito_solicitado", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCredito(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mcr_credito_solicitado (cd_credito,"+
			                                  "cd_pessoa,"+
			                                  "txt_finalidade,"+
			                                  "nr_prazo,"+
			                                  "vl_solicitado,"+
			                                  "vl_maximo_parcela,"+
			                                  "nr_dia_vencto_sugerido,"+
			                                  "lg_garantia_aval,"+
			                                  "lg_garantia_bem,"+
			                                  "lg_garantia_grupo_solidario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getTxtFinalidade());
			pstmt.setInt(4,objeto.getNrPrazo());
			pstmt.setFloat(5,objeto.getVlSolicitado());
			pstmt.setFloat(6,objeto.getVlMaximoParcela());
			pstmt.setInt(7,objeto.getNrDiaVenctoSugerido());
			pstmt.setInt(8,objeto.getLgGarantiaAval());
			pstmt.setInt(9,objeto.getLgGarantiaBem());
			pstmt.setInt(10,objeto.getLgGarantiaGrupoSolidario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CreditoSolicitadoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CreditoSolicitadoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CreditoSolicitado objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CreditoSolicitado objeto, int cdCreditoOld) {
		return update(objeto, cdCreditoOld, null);
	}

	public static int update(CreditoSolicitado objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CreditoSolicitado objeto, int cdCreditoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mcr_credito_solicitado SET cd_credito=?,"+
												      		   "cd_pessoa=?,"+
												      		   "txt_finalidade=?,"+
												      		   "nr_prazo=?,"+
												      		   "vl_solicitado=?,"+
												      		   "vl_maximo_parcela=?,"+
												      		   "nr_dia_vencto_sugerido=?,"+
												      		   "lg_garantia_aval=?,"+
												      		   "lg_garantia_bem=?,"+
												      		   "lg_garantia_grupo_solidario=? WHERE cd_credito=?");
			pstmt.setInt(1,objeto.getCdCredito());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getTxtFinalidade());
			pstmt.setInt(4,objeto.getNrPrazo());
			pstmt.setFloat(5,objeto.getVlSolicitado());
			pstmt.setFloat(6,objeto.getVlMaximoParcela());
			pstmt.setInt(7,objeto.getNrDiaVenctoSugerido());
			pstmt.setInt(8,objeto.getLgGarantiaAval());
			pstmt.setInt(9,objeto.getLgGarantiaBem());
			pstmt.setInt(10,objeto.getLgGarantiaGrupoSolidario());
			pstmt.setInt(11, cdCreditoOld!=0 ? cdCreditoOld : objeto.getCdCredito());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CreditoSolicitadoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CreditoSolicitadoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCredito) {
		return delete(cdCredito, null);
	}

	public static int delete(int cdCredito, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mcr_credito_solicitado WHERE cd_credito=?");
			pstmt.setInt(1, cdCredito);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CreditoSolicitadoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CreditoSolicitadoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CreditoSolicitado get(int cdCredito) {
		return get(cdCredito, null);
	}

	public static CreditoSolicitado get(int cdCredito, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_credito_solicitado WHERE cd_credito=?");
			pstmt.setInt(1, cdCredito);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CreditoSolicitado(rs.getInt("cd_credito"),
						rs.getInt("cd_pessoa"),
						rs.getString("txt_finalidade"),
						rs.getInt("nr_prazo"),
						rs.getFloat("vl_solicitado"),
						rs.getFloat("vl_maximo_parcela"),
						rs.getInt("nr_dia_vencto_sugerido"),
						rs.getInt("lg_garantia_aval"),
						rs.getInt("lg_garantia_bem"),
						rs.getInt("lg_garantia_grupo_solidario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CreditoSolicitadoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CreditoSolicitadoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_credito_solicitado");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CreditoSolicitadoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CreditoSolicitadoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_credito_solicitado", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
