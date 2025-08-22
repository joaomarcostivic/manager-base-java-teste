package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class EntradaTributoDAO{

	public static int insert(EntradaTributo objeto) {
		return insert(objeto, null);
	}

	public static int insert(EntradaTributo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull){
				connect = Conexao.conectar();
			}
			else{
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_entrada_tributo (cd_documento_entrada,"+
			                                  "cd_tributo,"+
			                                  "vl_base_calculo,"+
			                                  "pr_aliquota,"+
			                                  "vl_tributo," +
			                                  "vl_base_retencao," +
			                                  "vl_retido) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDocumentoEntrada());
			if(objeto.getCdTributo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTributo());
			pstmt.setFloat(3,objeto.getVlBaseCalculo());
			pstmt.setFloat(4,objeto.getPrAliquota());
			pstmt.setFloat(5,objeto.getVlTributo());
			pstmt.setFloat(6,objeto.getVlBaseRetencao());
			pstmt.setFloat(7,objeto.getVlRetido());
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

	public static int update(EntradaTributo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(EntradaTributo objeto, int cdDocumentoEntradaOld, int cdTributoOld) {
		return update(objeto, cdDocumentoEntradaOld, cdTributoOld, null);
	}

	public static int update(EntradaTributo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(EntradaTributo objeto, int cdDocumentoEntradaOld, int cdTributoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_entrada_tributo SET cd_documento_entrada=?,"+
												      		   "cd_tributo=?,"+
												      		   "vl_base_calculo=?,"+
												      		   "pr_aliquota=?,"+
												      		   "vl_tributo=?," +
												      		   "vl_base_retencao=?," +
												      		   "vl_retido=? WHERE cd_documento_entrada=? AND cd_tributo=?");
			pstmt.setInt(1,objeto.getCdDocumentoEntrada());
			pstmt.setInt(2,objeto.getCdTributo());
			pstmt.setFloat(3,objeto.getVlBaseCalculo());
			pstmt.setFloat(4,objeto.getPrAliquota());
			pstmt.setFloat(5,objeto.getVlTributo());
			pstmt.setInt(6, cdDocumentoEntradaOld!=0 ? cdDocumentoEntradaOld : objeto.getCdDocumentoEntrada());
			pstmt.setFloat(6,objeto.getVlBaseRetencao());
			pstmt.setFloat(7,objeto.getVlRetido());
			pstmt.setInt(8, cdTributoOld!=0 ? cdTributoOld : objeto.getCdTributo());
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

	public static int delete(int cdDocumentoEntrada, int cdTributo) {
		return delete(cdDocumentoEntrada, cdTributo, null);
	}

	public static int delete(int cdDocumentoEntrada, int cdTributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_entrada_tributo WHERE cd_documento_entrada=? AND cd_tributo=?");
			pstmt.setInt(1, cdDocumentoEntrada);
			pstmt.setInt(2, cdTributo);
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

	public static EntradaTributo get(int cdDocumentoEntrada, int cdTributo) {
		return get(cdDocumentoEntrada, cdTributo, null);
	}

	public static EntradaTributo get(int cdDocumentoEntrada, int cdTributo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_entrada_tributo WHERE cd_documento_entrada=? AND cd_tributo=?");
			pstmt.setInt(1, cdDocumentoEntrada);
			pstmt.setInt(2, cdTributo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EntradaTributo(rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_tributo"),
						rs.getFloat("vl_base_calculo"),
						rs.getFloat("pr_aliquota"),
						rs.getFloat("vl_tributo"),
						rs.getFloat("vl_base_retencao"),
						rs.getFloat("vl_retido"));
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_entrada_tributo");
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
		return Search.find("SELECT * FROM adm_entrada_tributo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
