package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaFechamentoTitCredDAO{

	public static int insert(ContaFechamentoTitCred objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaFechamentoTitCred objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_fechamento_tit_cred (cd_conta,"+
			                                  "cd_fechamento,"+
			                                  "cd_tipo_documento,"+
			                                  "cd_titulo_credito) VALUES (?, ?, ?, ?)");
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
			if(objeto.getCdTituloCredito()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTituloCredito());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTitCredDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTitCredDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaFechamentoTitCred objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(ContaFechamentoTitCred objeto, int cdContaOld, int cdFechamentoOld, int cdTipoDocumentoOld, int cdTituloCreditoOld) {
		return update(objeto, cdContaOld, cdFechamentoOld, cdTipoDocumentoOld, cdTituloCreditoOld, null);
	}

	public static int update(ContaFechamentoTitCred objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(ContaFechamentoTitCred objeto, int cdContaOld, int cdFechamentoOld, int cdTipoDocumentoOld, int cdTituloCreditoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_fechamento_tit_cred SET cd_conta=?,"+
												      		   "cd_fechamento=?,"+
												      		   "cd_tipo_documento=?,"+
												      		   "cd_titulo_credito=? WHERE cd_conta=? AND cd_fechamento=? AND cd_tipo_documento=? AND cd_titulo_credito=?");
			pstmt.setInt(1,objeto.getCdConta());
			pstmt.setInt(2,objeto.getCdFechamento());
			pstmt.setInt(3,objeto.getCdTipoDocumento());
			pstmt.setInt(4,objeto.getCdTituloCredito());
			pstmt.setInt(5, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.setInt(6, cdFechamentoOld!=0 ? cdFechamentoOld : objeto.getCdFechamento());
			pstmt.setInt(7, cdTipoDocumentoOld!=0 ? cdTipoDocumentoOld : objeto.getCdTipoDocumento());
			pstmt.setInt(8, cdTituloCreditoOld!=0 ? cdTituloCreditoOld : objeto.getCdTituloCredito());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTitCredDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTitCredDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConta, int cdFechamento, int cdTipoDocumento, int cdTituloCredito) {
		return delete(cdConta, cdFechamento, cdTipoDocumento, cdTituloCredito, null);
	}

	public static int delete(int cdConta, int cdFechamento, int cdTipoDocumento, int cdTituloCredito, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_fechamento_tit_cred WHERE cd_conta=? AND cd_fechamento=? AND cd_tipo_documento=? AND cd_titulo_credito=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdFechamento);
			pstmt.setInt(3, cdTipoDocumento);
			pstmt.setInt(4, cdTituloCredito);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTitCredDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTitCredDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaFechamentoTitCred get(int cdConta, int cdFechamento, int cdTipoDocumento, int cdTituloCredito) {
		return get(cdConta, cdFechamento, cdTipoDocumento, cdTituloCredito, null);
	}

	public static ContaFechamentoTitCred get(int cdConta, int cdFechamento, int cdTipoDocumento, int cdTituloCredito, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_fechamento_tit_cred WHERE cd_conta=? AND cd_fechamento=? AND cd_tipo_documento=? AND cd_titulo_credito=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdFechamento);
			pstmt.setInt(3, cdTipoDocumento);
			pstmt.setInt(4, cdTituloCredito);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaFechamentoTitCred(rs.getInt("cd_conta"),
						rs.getInt("cd_fechamento"),
						rs.getInt("cd_tipo_documento"),
						rs.getInt("cd_titulo_credito"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTitCredDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTitCredDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_fechamento_tit_cred");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTitCredDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoTitCredDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_fechamento_tit_cred", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
