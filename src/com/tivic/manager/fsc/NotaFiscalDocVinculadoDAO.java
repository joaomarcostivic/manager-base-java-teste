package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.*;
import java.util.HashMap;
import java.util.ArrayList;

public class NotaFiscalDocVinculadoDAO{

	public static int insert(NotaFiscalDocVinculado objeto) {
		return insert(objeto, null);
	}

	public static int insert(NotaFiscalDocVinculado objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			@SuppressWarnings("unchecked")
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_nota_fiscal");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdNotaFiscal()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_doc_vinculado");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("fsc_nota_fiscal_doc_vinculado", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDocVinculado(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_nota_fiscal_doc_vinculado (cd_nota_fiscal,"+
			                                  "cd_doc_vinculado,"+
			                                  "cd_nota_fiscal_vinculada,"+
			                                  "cd_documento_saida,"+
			                                  "cd_documento_entrada,"+
			                                  "tp_documento_vinculado) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdNotaFiscal()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdNotaFiscal());
			pstmt.setInt(2, code);
			if(objeto.getCdNotaFiscalVinculada()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdNotaFiscalVinculada());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdDocumentoSaida());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdDocumentoEntrada());
			pstmt.setInt(6,objeto.getTpDocumentoVinculado());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NotaFiscalDocVinculado objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(NotaFiscalDocVinculado objeto, int cdNotaFiscalOld, int cdDocVinculadoOld) {
		return update(objeto, cdNotaFiscalOld, cdDocVinculadoOld, null);
	}

	public static int update(NotaFiscalDocVinculado objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(NotaFiscalDocVinculado objeto, int cdNotaFiscalOld, int cdDocVinculadoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_nota_fiscal_doc_vinculado SET cd_nota_fiscal=?,"+
												      		   "cd_doc_vinculado=?,"+
												      		   "cd_nota_fiscal_vinculada=?,"+
												      		   "cd_documento_saida=?,"+
												      		   "cd_documento_entrada=?,"+
												      		   "tp_documento_vinculado=? WHERE cd_nota_fiscal=? AND cd_doc_vinculado=?");
			pstmt.setInt(1,objeto.getCdNotaFiscal());
			pstmt.setInt(2,objeto.getCdDocVinculado());
			if(objeto.getCdNotaFiscalVinculada()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdNotaFiscalVinculada());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdDocumentoSaida());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdDocumentoEntrada());
			pstmt.setInt(6,objeto.getTpDocumentoVinculado());
			pstmt.setInt(7, cdNotaFiscalOld!=0 ? cdNotaFiscalOld : objeto.getCdNotaFiscal());
			pstmt.setInt(8, cdDocVinculadoOld!=0 ? cdDocVinculadoOld : objeto.getCdDocVinculado());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNotaFiscal, int cdDocVinculado) {
		return delete(cdNotaFiscal, cdDocVinculado, null);
	}

	public static int delete(int cdNotaFiscal, int cdDocVinculado, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_nota_fiscal_doc_vinculado WHERE cd_nota_fiscal=? AND cd_doc_vinculado=?");
			pstmt.setInt(1, cdNotaFiscal);
			pstmt.setInt(2, cdDocVinculado);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NotaFiscalDocVinculado get(int cdNotaFiscal, int cdDocVinculado) {
		return get(cdNotaFiscal, cdDocVinculado, null);
	}

	public static NotaFiscalDocVinculado get(int cdNotaFiscal, int cdDocVinculado, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_doc_vinculado WHERE cd_nota_fiscal=? AND cd_doc_vinculado=?");
			pstmt.setInt(1, cdNotaFiscal);
			pstmt.setInt(2, cdDocVinculado);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NotaFiscalDocVinculado(rs.getInt("cd_nota_fiscal"),
						rs.getInt("cd_doc_vinculado"),
						rs.getInt("cd_nota_fiscal_vinculada"),
						rs.getInt("cd_documento_saida"),
						rs.getInt("cd_documento_entrada"),
						rs.getInt("tp_documento_vinculado"));
			}
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_doc_vinculado");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM fsc_nota_fiscal_doc_vinculado", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
