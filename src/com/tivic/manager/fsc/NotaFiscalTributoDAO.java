package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.*;
import java.util.ArrayList;

public class NotaFiscalTributoDAO{

	public static int insert(NotaFiscalTributo objeto) {
		return insert(objeto, null);
	}

	public static int insert(NotaFiscalTributo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_nota_fiscal_tributo (cd_nota_fiscal,"+
			                                  "cd_tributo,"+
			                                  "vl_base_calculo,"+
			                                  "vl_outras_despesas,"+
			                                  "vl_outros_impostos,"+
			                                  "vl_tributo,"+
			                                  "vl_base_retencao,"+
			                                  "vl_retido) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdNotaFiscal()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdNotaFiscal());
			if(objeto.getCdTributo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTributo());
			pstmt.setFloat(3,objeto.getVlBaseCalculo());
			pstmt.setFloat(4,objeto.getVlOutrasDespesas());
			pstmt.setFloat(5,objeto.getVlOutrosImpostos());
			pstmt.setFloat(6,objeto.getVlTributo());
			pstmt.setFloat(7,objeto.getVlBaseRetencao());
			pstmt.setFloat(8,objeto.getVlRetido());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalTributoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalTributoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NotaFiscalTributo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(NotaFiscalTributo objeto, int cdNotaFiscalOld, int cdTributoOld) {
		return update(objeto, cdNotaFiscalOld, cdTributoOld, null);
	}

	public static int update(NotaFiscalTributo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(NotaFiscalTributo objeto, int cdNotaFiscalOld, int cdTributoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_nota_fiscal_tributo SET cd_nota_fiscal=?,"+
												      		   "cd_tributo=?,"+
												      		   "vl_base_calculo=?,"+
												      		   "vl_outras_despesas=?,"+
												      		   "vl_outros_impostos=?,"+
												      		   "vl_tributo=?,"+
												      		   "vl_base_retencao=?,"+
												      		   "vl_retido=? WHERE cd_nota_fiscal=? AND cd_tributo=?");
			pstmt.setInt(1,objeto.getCdNotaFiscal());
			pstmt.setInt(2,objeto.getCdTributo());
			pstmt.setFloat(3,objeto.getVlBaseCalculo());
			pstmt.setFloat(4,objeto.getVlOutrasDespesas());
			pstmt.setFloat(5,objeto.getVlOutrosImpostos());
			pstmt.setFloat(6,objeto.getVlTributo());
			pstmt.setFloat(7,objeto.getVlBaseRetencao());
			pstmt.setFloat(8,objeto.getVlRetido());
			pstmt.setInt(9, cdNotaFiscalOld!=0 ? cdNotaFiscalOld : objeto.getCdNotaFiscal());
			pstmt.setInt(10, cdTributoOld!=0 ? cdTributoOld : objeto.getCdTributo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalTributoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalTributoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNotaFiscal, int cdTributo) {
		return delete(cdNotaFiscal, cdTributo, null);
	}

	public static int delete(int cdNotaFiscal, int cdTributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_nota_fiscal_tributo WHERE cd_nota_fiscal=? AND cd_tributo=?");
			pstmt.setInt(1, cdNotaFiscal);
			pstmt.setInt(2, cdTributo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalTributoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalTributoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NotaFiscalTributo get(int cdNotaFiscal, int cdTributo) {
		return get(cdNotaFiscal, cdTributo, null);
	}

	public static NotaFiscalTributo get(int cdNotaFiscal, int cdTributo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_tributo WHERE cd_nota_fiscal=? AND cd_tributo=?");
			pstmt.setInt(1, cdNotaFiscal);
			pstmt.setInt(2, cdTributo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NotaFiscalTributo(rs.getInt("cd_nota_fiscal"),
						rs.getInt("cd_tributo"),
						rs.getFloat("vl_base_calculo"),
						rs.getFloat("vl_outras_despesas"),
						rs.getFloat("vl_outros_impostos"),
						rs.getFloat("vl_tributo"),
						rs.getFloat("vl_base_retencao"),
						rs.getFloat("vl_retido"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalTributoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalTributoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal_tributo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalTributoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalTributoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fsc_nota_fiscal_tributo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
