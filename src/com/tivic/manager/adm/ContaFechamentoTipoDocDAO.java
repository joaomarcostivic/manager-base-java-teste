package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaFechamentoTipoDocDAO{

	public static int insert(ContaFechamentoTipoDoc objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaFechamentoTipoDoc objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_fechamento_tipo_doc (cd_conta,"+
			                                  "cd_fechamento,"+
			                                  "cd_tipo_documento,"+
			                                  "vl_total_entradas,"+
			                                  "vl_total_saidas,"+
			                                  "vl_saldo_final,"+
			                                  "vl_saldo_anterior) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdConta()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdConta());
			if(objeto.getCdFechamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFechamento());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoDocumento());
			pstmt.setInt(4,objeto.getVlTotalEntradas());
			pstmt.setInt(5,objeto.getVlTotalSaidas());
			pstmt.setInt(6,objeto.getVlSaldoFinal());
			pstmt.setFloat(7,objeto.getVlSaldoAnterior());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTipoDocDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTipoDocDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaFechamentoTipoDoc objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ContaFechamentoTipoDoc objeto, int cdContaOld, int cdFechamentoOld, int cdTipoDocumentoOld) {
		return update(objeto, cdContaOld, cdFechamentoOld, cdTipoDocumentoOld, null);
	}

	public static int update(ContaFechamentoTipoDoc objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ContaFechamentoTipoDoc objeto, int cdContaOld, int cdFechamentoOld, int cdTipoDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_fechamento_tipo_doc SET cd_conta=?,"+
												      		   "cd_fechamento=?,"+
												      		   "cd_tipo_documento=?,"+
												      		   "vl_total_entradas=?,"+
												      		   "vl_total_saidas=?,"+
												      		   "vl_saldo_final=?,"+
												      		   "vl_saldo_anterior=? WHERE cd_conta=? AND cd_fechamento=? AND cd_tipo_documento=?");
			pstmt.setInt(1,objeto.getCdConta());
			pstmt.setInt(2,objeto.getCdFechamento());
			pstmt.setInt(3,objeto.getCdTipoDocumento());
			pstmt.setInt(4,objeto.getVlTotalEntradas());
			pstmt.setInt(5,objeto.getVlTotalSaidas());
			pstmt.setInt(6,objeto.getVlSaldoFinal());
			pstmt.setFloat(7,objeto.getVlSaldoAnterior());
			pstmt.setInt(8, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.setInt(9, cdFechamentoOld!=0 ? cdFechamentoOld : objeto.getCdFechamento());
			pstmt.setInt(10, cdTipoDocumentoOld!=0 ? cdTipoDocumentoOld : objeto.getCdTipoDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTipoDocDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTipoDocDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConta, int cdFechamento, int cdTipoDocumento) {
		return delete(cdConta, cdFechamento, cdTipoDocumento, null);
	}

	public static int delete(int cdConta, int cdFechamento, int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_fechamento_tipo_doc WHERE cd_conta=? AND cd_fechamento=? AND cd_tipo_documento=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdFechamento);
			pstmt.setInt(3, cdTipoDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTipoDocDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTipoDocDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaFechamentoTipoDoc get(int cdConta, int cdFechamento, int cdTipoDocumento) {
		return get(cdConta, cdFechamento, cdTipoDocumento, null);
	}

	public static ContaFechamentoTipoDoc get(int cdConta, int cdFechamento, int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_fechamento_tipo_doc WHERE cd_conta=? AND cd_fechamento=? AND cd_tipo_documento=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdFechamento);
			pstmt.setInt(3, cdTipoDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaFechamentoTipoDoc(rs.getInt("cd_conta"),
						rs.getInt("cd_fechamento"),
						rs.getInt("cd_tipo_documento"),
						rs.getInt("vl_total_entradas"),
						rs.getInt("vl_total_saidas"),
						rs.getInt("vl_saldo_final"),
						rs.getFloat("vl_saldo_anterior"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTipoDocDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTipoDocDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_fechamento_tipo_doc");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTipoDocDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTipoDocDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_fechamento_tipo_doc", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
