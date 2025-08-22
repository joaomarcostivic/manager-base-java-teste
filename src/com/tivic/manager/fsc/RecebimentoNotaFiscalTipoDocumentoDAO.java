package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class RecebimentoNotaFiscalTipoDocumentoDAO{

	public static int insert(RecebimentoNotaFiscalTipoDocumento objeto) {
		return insert(objeto, null);
	}

	public static int insert(RecebimentoNotaFiscalTipoDocumento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_recebimento_nota_fiscal_tipo_documento (cd_recebimento_nota_fiscal,"+
			                                  "cd_tipo_documento,"+
											  "lg_presente) VALUES (?, ?, ?)");
			if(objeto.getCdRecebimentoNotaFiscal()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdRecebimentoNotaFiscal());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoDocumento());
			pstmt.setInt(3, objeto.getLgPresente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalTipoDocumentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalTipoDocumentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RecebimentoNotaFiscalTipoDocumento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(RecebimentoNotaFiscalTipoDocumento objeto, int cdRecebimentoNotaFiscalOld, int cdTipoDocumentoOld) {
		return update(objeto, cdRecebimentoNotaFiscalOld, cdTipoDocumentoOld, null);
	}

	public static int update(RecebimentoNotaFiscalTipoDocumento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(RecebimentoNotaFiscalTipoDocumento objeto, int cdRecebimentoNotaFiscalOld, int cdTipoDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_recebimento_nota_fiscal_tipo_documento SET cd_recebimento_nota_fiscal=?,"+
												      		   "cd_tipo_documento=?, lg_presente=? WHERE cd_recebimento_nota_fiscal=? AND cd_tipo_documento=?");
			pstmt.setInt(1,objeto.getCdRecebimentoNotaFiscal());
			pstmt.setInt(2,objeto.getCdTipoDocumento());
			pstmt.setInt(3, objeto.getLgPresente());
			pstmt.setInt(4, cdRecebimentoNotaFiscalOld!=0 ? cdRecebimentoNotaFiscalOld : objeto.getCdRecebimentoNotaFiscal());
			pstmt.setInt(5, cdTipoDocumentoOld!=0 ? cdTipoDocumentoOld : objeto.getCdTipoDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalTipoDocumentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalTipoDocumentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRecebimentoNotaFiscal, int cdTipoDocumento) {
		return delete(cdRecebimentoNotaFiscal, cdTipoDocumento, null);
	}

	public static int delete(int cdRecebimentoNotaFiscal, int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_recebimento_nota_fiscal_tipo_documento WHERE cd_recebimento_nota_fiscal=? AND cd_tipo_documento=?");
			pstmt.setInt(1, cdRecebimentoNotaFiscal);
			pstmt.setInt(2, cdTipoDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalTipoDocumentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalTipoDocumentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RecebimentoNotaFiscalTipoDocumento get(int cdRecebimentoNotaFiscal, int cdTipoDocumento) {
		return get(cdRecebimentoNotaFiscal, cdTipoDocumento, null);
	}

	public static RecebimentoNotaFiscalTipoDocumento get(int cdRecebimentoNotaFiscal, int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_recebimento_nota_fiscal_tipo_documento WHERE cd_recebimento_nota_fiscal=? AND cd_tipo_documento=?");
			pstmt.setInt(1, cdRecebimentoNotaFiscal);
			pstmt.setInt(2, cdTipoDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RecebimentoNotaFiscalTipoDocumento(rs.getInt("cd_recebimento_nota_fiscal"),
						rs.getInt("cd_tipo_documento"),
						rs.getInt("lg_presente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalTipoDocumentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalTipoDocumentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_recebimento_nota_fiscal_tipo_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalTipoDocumentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalTipoDocumentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RecebimentoNotaFiscalTipoDocumento> getList() {
		return getList(null);
	}

	public static ArrayList<RecebimentoNotaFiscalTipoDocumento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RecebimentoNotaFiscalTipoDocumento> list = new ArrayList<RecebimentoNotaFiscalTipoDocumento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RecebimentoNotaFiscalTipoDocumento obj = RecebimentoNotaFiscalTipoDocumentoDAO.get(rsm.getInt("cd_recebimento_nota_fiscal"), rsm.getInt("cd_tipo_documento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalTipoDocumentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM fsc_recebimento_nota_fiscal_tipo_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}