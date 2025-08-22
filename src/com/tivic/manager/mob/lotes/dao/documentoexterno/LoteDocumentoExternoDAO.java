package com.tivic.manager.mob.lotes.dao.documentoexterno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.tivic.manager.mob.lotes.model.documentoexterno.LoteDocumentoExterno;
import com.tivic.sol.connection.Conexao;

public class LoteDocumentoExternoDAO{

	public static int insert(LoteDocumentoExterno objeto) {
		return insert(objeto, null);
	}

	public static int insert(LoteDocumentoExterno objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_lote_documento_externo (" +
											  "cd_lote_impressao, "+
			                                  "cd_documento, "+
			                                  "cd_documento_externo) VALUES (?, ?, ?)");
			if(objeto.getCdLoteImpressao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdLoteImpressao());
			
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDocumento());
			
			if(objeto.getCdDocumentoExterno()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDocumentoExterno());
			
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDocumentoExternoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDocumentoExternoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LoteDocumentoExterno objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(LoteDocumentoExterno objeto, int cdLoteImpressaoOld, int cdAitOld) {
		return update(objeto, cdLoteImpressaoOld, cdAitOld, null);
	}

	public static int update(LoteDocumentoExterno objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(LoteDocumentoExterno objeto, int cdLoteImpressaoOld, int cdAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_lote_documento_externo SET cd_lote_impressao=?,"+
												      		   "cd_documento=?,"+
												      		   "cd_documento_externo=? WHERE cd_lote_impressao=? AND cd_documento=?");
			pstmt.setInt(1,objeto.getCdLoteImpressao());
			pstmt.setInt(2,objeto.getCdDocumento());
			pstmt.setInt(3,objeto.getCdDocumentoExterno());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDocumentoExternoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDocumentoExternoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLoteImpressao, int cdAit) {
		return delete(cdLoteImpressao, cdAit, null);
	}

	public static int delete(int cdLoteImpressao, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_lote_documento_externo WHERE cd_lote_impressao=? AND cd_documento=?");
			pstmt.setInt(1, cdLoteImpressao);
			pstmt.setInt(2, cdDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDocumentoExternoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDocumentoExternoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LoteDocumentoExterno get(int cdLoteImpressao, int cdDocumento) {
		return get(cdLoteImpressao, cdDocumento, null);
	}

	public static LoteDocumentoExterno get(int cdLoteImpressao, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_documento_externo WHERE cd_lote_impressao=? AND cd_documento=?");
			pstmt.setInt(1, cdLoteImpressao);
			pstmt.setInt(2, cdDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LoteDocumentoExterno(
						rs.getInt("cd_lote_impressao"),
						rs.getInt("cd_documento"),
						rs.getInt("cd_documento_externo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDocumentoExternoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDocumentoExternoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
