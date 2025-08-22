package com.tivic.manager.crt;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class DocumentoDAO{

	public static int insert(Documento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Documento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("PTC_DOCUMENTO", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDocumento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PTC_DOCUMENTO (cd_documento,"+
			                                  "dt_documento,"+
			                                  "txt_documento,"+
			                                  "nm_local_origem,"+
			                                  "nm_local_destino,"+
			                                  "lg_confidencial,"+
			                                  "id_documento,"+
			                                  "cd_pessoa_atual,"+
			                                  "cd_pessoa_emissor,"+
			                                  "cd_empresa_emissora,"+
			                                  "cd_empresa_atual,"+
			                                  "st_documento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtDocumento()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtDocumento().getTimeInMillis()));
			pstmt.setString(3,objeto.getTxtDocumento());
			pstmt.setString(4,objeto.getNmLocalOrigem());
			pstmt.setString(5,objeto.getNmLocalDestino());
			pstmt.setInt(6,objeto.getLgConfidencial());
			pstmt.setString(7,objeto.getIdDocumento());
			pstmt.setInt(8,objeto.getCdPessoaAtual());
			pstmt.setInt(9,objeto.getCdPessoaEmissor());
			pstmt.setInt(10,objeto.getCdEmpresaEmissora());
			pstmt.setInt(11,objeto.getCdEmpresaAtual());
			pstmt.setInt(12,objeto.getStDocumento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Documento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Documento objeto, int cdDocumentoOld) {
		return update(objeto, cdDocumentoOld, null);
	}

	public static int update(Documento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Documento objeto, int cdDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PTC_DOCUMENTO SET cd_documento=?,"+
												      		   "dt_documento=?,"+
												      		   "txt_documento=?,"+
												      		   "nm_local_origem=?,"+
												      		   "nm_local_destino=?,"+
												      		   "lg_confidencial=?,"+
												      		   "id_documento=?,"+
												      		   "cd_pessoa_atual=?,"+
												      		   "cd_pessoa_emissor=?,"+
												      		   "cd_empresa_emissora=?,"+
												      		   "cd_empresa_atual=?,"+
												      		   "st_documento=? WHERE cd_documento=?");
			pstmt.setInt(1,objeto.getCdDocumento());
			if(objeto.getDtDocumento()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtDocumento().getTimeInMillis()));
			pstmt.setString(3,objeto.getTxtDocumento());
			pstmt.setString(4,objeto.getNmLocalOrigem());
			pstmt.setString(5,objeto.getNmLocalDestino());
			pstmt.setInt(6,objeto.getLgConfidencial());
			pstmt.setString(7,objeto.getIdDocumento());
			pstmt.setInt(8,objeto.getCdPessoaAtual());
			pstmt.setInt(9,objeto.getCdPessoaEmissor());
			pstmt.setInt(10,objeto.getCdEmpresaEmissora());
			pstmt.setInt(11,objeto.getCdEmpresaAtual());
			pstmt.setInt(12,objeto.getStDocumento());
			pstmt.setInt(13, cdDocumentoOld!=0 ? cdDocumentoOld : objeto.getCdDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumento) {
		return delete(cdDocumento, null);
	}

	public static int delete(int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PTC_DOCUMENTO WHERE cd_documento=?");
			pstmt.setInt(1, cdDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Documento get(int cdDocumento) {
		return get(cdDocumento, null);
	}

	public static Documento get(int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PTC_DOCUMENTO WHERE cd_documento=?");
			pstmt.setInt(1, cdDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Documento(rs.getInt("cd_documento"),
						(rs.getTimestamp("dt_documento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_documento").getTime()),
						rs.getString("txt_documento"),
						rs.getString("nm_local_origem"),
						rs.getString("nm_local_destino"),
						rs.getInt("lg_confidencial"),
						rs.getString("id_documento"),
						rs.getInt("cd_pessoa_atual"),
						rs.getInt("cd_pessoa_emissor"),
						rs.getInt("cd_empresa_emissora"),
						rs.getInt("cd_empresa_atual"),
						rs.getInt("st_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PTC_DOCUMENTO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM PTC_DOCUMENTO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
