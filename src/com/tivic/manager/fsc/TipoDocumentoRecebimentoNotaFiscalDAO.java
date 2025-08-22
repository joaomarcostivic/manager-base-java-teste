package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoDocumentoRecebimentoNotaFiscalDAO{

	public static int insert(TipoDocumentoRecebimentoNotaFiscal objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoDocumentoRecebimentoNotaFiscal objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fsc_tipo_documento_recebimento_nota_fiscal", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoDocumento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_tipo_documento_recebimento_nota_fiscal (cd_tipo_documento,"+
			                                  "nm_tipo_documento,"+
			                                  "st_tipo_documento,"+
			                                  "lg_obrigatorio) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTipoDocumento());
			pstmt.setInt(3,objeto.getStTipoDocumento());
			pstmt.setInt(4,objeto.getLgObrigatorio());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoRecebimentoNotaFiscalDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoRecebimentoNotaFiscalDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoDocumentoRecebimentoNotaFiscal objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoDocumentoRecebimentoNotaFiscal objeto, int cdTipoDocumentoOld) {
		return update(objeto, cdTipoDocumentoOld, null);
	}

	public static int update(TipoDocumentoRecebimentoNotaFiscal objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoDocumentoRecebimentoNotaFiscal objeto, int cdTipoDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_tipo_documento_recebimento_nota_fiscal SET cd_tipo_documento=?,"+
												      		   "nm_tipo_documento=?,"+
												      		   "st_tipo_documento=?,"+
												      		   "lg_obrigatorio=? WHERE cd_tipo_documento=?");
			pstmt.setInt(1,objeto.getCdTipoDocumento());
			pstmt.setString(2,objeto.getNmTipoDocumento());
			pstmt.setInt(3,objeto.getStTipoDocumento());
			pstmt.setInt(4,objeto.getLgObrigatorio());
			pstmt.setInt(5, cdTipoDocumentoOld!=0 ? cdTipoDocumentoOld : objeto.getCdTipoDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoRecebimentoNotaFiscalDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoRecebimentoNotaFiscalDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDocumento) {
		return delete(cdTipoDocumento, null);
	}

	public static int delete(int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_tipo_documento_recebimento_nota_fiscal WHERE cd_tipo_documento=?");
			pstmt.setInt(1, cdTipoDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoRecebimentoNotaFiscalDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoRecebimentoNotaFiscalDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoDocumentoRecebimentoNotaFiscal get(int cdTipoDocumento) {
		return get(cdTipoDocumento, null);
	}

	public static TipoDocumentoRecebimentoNotaFiscal get(int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_tipo_documento_recebimento_nota_fiscal WHERE cd_tipo_documento=?");
			pstmt.setInt(1, cdTipoDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoDocumentoRecebimentoNotaFiscal(rs.getInt("cd_tipo_documento"),
						rs.getString("nm_tipo_documento"),
						rs.getInt("st_tipo_documento"),
						rs.getInt("lg_obrigatorio"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoRecebimentoNotaFiscalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoRecebimentoNotaFiscalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_tipo_documento_recebimento_nota_fiscal");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoRecebimentoNotaFiscalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoRecebimentoNotaFiscalDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoDocumentoRecebimentoNotaFiscal> getList() {
		return getList(null);
	}

	public static ArrayList<TipoDocumentoRecebimentoNotaFiscal> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoDocumentoRecebimentoNotaFiscal> list = new ArrayList<TipoDocumentoRecebimentoNotaFiscal>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoDocumentoRecebimentoNotaFiscal obj = TipoDocumentoRecebimentoNotaFiscalDAO.get(rsm.getInt("cd_tipo_documento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentoRecebimentoNotaFiscalDAO.getList: " + e);
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
		return Search.find("SELECT * FROM fsc_tipo_documento_recebimento_nota_fiscal", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}