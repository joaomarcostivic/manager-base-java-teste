package com.tivic.manager.crt;

import java.sql.*;
import sol.util.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class NotaFiscalDAO{

	public static int insert(NotaFiscal objeto) {
		return insert(objeto, null);
	}

	public static int insert(NotaFiscal objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_nota_fiscal "+
			                                 "WHERE nr_nota_fiscal = ? "+
			                                 "  AND cd_empresa     = ?");

			pstmt.setInt(1,objeto.getNrNotaFiscal());
			pstmt.setInt(2,objeto.getCdEmpresa());
			if(pstmt.executeQuery().next())
				return -10;
			int code = Conexao.getSequenceCode("ADM_NOTA_FISCAL");
			pstmt = connect.prepareStatement("INSERT INTO ADM_NOTA_FISCAL (CD_NOTA_FISCAL,"+
			                                  "NR_NOTA_FISCAL,"+
			                                  "DT_NOTA_FISCAL,"+
			                                  "DT_EMISSAO,"+
			                                  "ST_NOTA_FISCAL,"+
			                                  "CD_EMPRESA,"+
			                                  "CD_PESSOA,"+
			                                  "VL_TOTAL_NOTA,"+
			                                  "VL_NOTA_FISCAL,"+
			                                  "VL_ISS,"+
			                                  "QT_ITENS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getNrNotaFiscal());
			if(objeto.getDtNotaFiscal()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtNotaFiscal().getTimeInMillis()));
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStNotaFiscal());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdPessoa());
			pstmt.setFloat(8,objeto.getVlTotalNota());
			pstmt.setFloat(9,objeto.getVlNotaFiscal());
			pstmt.setFloat(10,objeto.getVlIss());
			pstmt.setInt(11,objeto.getQtItens());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NotaFiscal objeto) {
		return update(objeto, null);
	}

	public static int update(NotaFiscal objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE ADM_NOTA_FISCAL SET NR_NOTA_FISCAL=?,"+
			                                  "DT_NOTA_FISCAL=?,"+
			                                  "DT_EMISSAO=?,"+
			                                  "ST_NOTA_FISCAL=?,"+
			                                  "CD_EMPRESA=?,"+
			                                  "CD_PESSOA=?,"+
			                                  "VL_TOTAL_NOTA=?,"+
			                                  "VL_NOTA_FISCAL=?,"+
			                                  "VL_ISS=?,"+
			                                  "QT_ITENS=? WHERE CD_NOTA_FISCAL=?");
			pstmt.setInt(1,objeto.getNrNotaFiscal());
			if(objeto.getDtNotaFiscal()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtNotaFiscal().getTimeInMillis()));
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStNotaFiscal());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPessoa());
			pstmt.setFloat(7,objeto.getVlTotalNota());
			pstmt.setFloat(8,objeto.getVlNotaFiscal());
			pstmt.setFloat(9,objeto.getVlIss());
			pstmt.setInt(10,objeto.getQtItens());
			pstmt.setInt(11,objeto.getCdNotaFiscal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNotaFiscal) {
		return delete(cdNotaFiscal, null);
	}

	public static int delete(int cdNotaFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM ADM_NOTA_FISCAL WHERE CD_NOTA_FISCAL=?");
			pstmt.setInt(1, cdNotaFiscal);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NotaFiscal get(int cdNotaFiscal) {
		return get(cdNotaFiscal, null);
	}

	public static NotaFiscal get(int cdNotaFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ADM_NOTA_FISCAL WHERE CD_NOTA_FISCAL=?");
			pstmt.setInt(1, cdNotaFiscal);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NotaFiscal(rs.getInt("CD_NOTA_FISCAL"),
						rs.getInt("NR_NOTA_FISCAL"),
						(rs.getTimestamp("DT_NOTA_FISCAL")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_NOTA_FISCAL").getTime()),
						(rs.getTimestamp("DT_EMISSAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_EMISSAO").getTime()),
						rs.getInt("ST_NOTA_FISCAL"),
						rs.getInt("CD_EMPRESA"),
						rs.getInt("CD_PESSOA"),
						rs.getFloat("VL_TOTAL_NOTA"),
						rs.getFloat("VL_NOTA_FISCAL"),
						rs.getFloat("VL_ISS"),
						rs.getInt("QT_ITENS"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ADM_NOTA_FISCAL");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM ADM_NOTA_FISCAL", criterios, Conexao.conectar());
	}

}