package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class EntradaEventoFinanceiroDAO{

	public static int insert(EntradaEventoFinanceiro objeto) {
		return insert(objeto, null);
	}

	public static int insert(EntradaEventoFinanceiro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_entrada_evento_financeiro (cd_documento_entrada,"+
			                                  "cd_evento_financeiro,"+
			                                  "vl_evento_financeiro,"+
			                                  "lg_custo,"+
			                                  "lg_fiscal,"+
			                                  "lg_despesa_acessoria) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDocumentoEntrada());
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEventoFinanceiro());
			pstmt.setFloat(3,objeto.getVlEventoFinanceiro());
			pstmt.setFloat(4,objeto.getLgCusto());
			pstmt.setInt(5,objeto.getLgFiscal());
			pstmt.setInt(6,objeto.getLgDespesaAcessoria());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaEventoFinanceiroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaEventoFinanceiroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EntradaEventoFinanceiro objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(EntradaEventoFinanceiro objeto, int cdDocumentoEntradaOld, int cdEventoFinanceiroOld) {
		return update(objeto, cdDocumentoEntradaOld, cdEventoFinanceiroOld, null);
	}

	public static int update(EntradaEventoFinanceiro objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(EntradaEventoFinanceiro objeto, int cdDocumentoEntradaOld, int cdEventoFinanceiroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_entrada_evento_financeiro SET cd_documento_entrada=?,"+
												      		   "cd_evento_financeiro=?,"+
												      		   "vl_evento_financeiro=?,"+
												      		   "lg_custo=?,"+
												      		   "lg_fiscal=?,"+
												      		   "lg_despesa_acessoria=? WHERE cd_documento_entrada=? AND cd_evento_financeiro=?");
			pstmt.setInt(1,objeto.getCdDocumentoEntrada());
			pstmt.setInt(2,objeto.getCdEventoFinanceiro());
			pstmt.setFloat(3,objeto.getVlEventoFinanceiro());
			pstmt.setFloat(4,objeto.getLgCusto());
			pstmt.setInt(5,objeto.getLgFiscal());
			pstmt.setInt(6,objeto.getLgDespesaAcessoria());
			pstmt.setInt(7, cdDocumentoEntradaOld!=0 ? cdDocumentoEntradaOld : objeto.getCdDocumentoEntrada());
			pstmt.setInt(8, cdEventoFinanceiroOld!=0 ? cdEventoFinanceiroOld : objeto.getCdEventoFinanceiro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaEventoFinanceiroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaEventoFinanceiroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumentoEntrada, int cdEventoFinanceiro) {
		return delete(cdDocumentoEntrada, cdEventoFinanceiro, null);
	}

	public static int delete(int cdDocumentoEntrada, int cdEventoFinanceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_entrada_evento_financeiro WHERE cd_documento_entrada=? AND cd_evento_financeiro=?");
			pstmt.setInt(1, cdDocumentoEntrada);
			pstmt.setInt(2, cdEventoFinanceiro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaEventoFinanceiroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaEventoFinanceiroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EntradaEventoFinanceiro get(int cdDocumentoEntrada, int cdEventoFinanceiro) {
		return get(cdDocumentoEntrada, cdEventoFinanceiro, null);
	}

	public static EntradaEventoFinanceiro get(int cdDocumentoEntrada, int cdEventoFinanceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_entrada_evento_financeiro WHERE cd_documento_entrada=? AND cd_evento_financeiro=?");
			pstmt.setInt(1, cdDocumentoEntrada);
			pstmt.setInt(2, cdEventoFinanceiro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EntradaEventoFinanceiro(rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_evento_financeiro"),
						rs.getFloat("vl_evento_financeiro"),
						rs.getInt("lg_custo"),
						rs.getInt("lg_fiscal"),
						rs.getInt("lg_despesa_acessoria"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaEventoFinanceiroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaEventoFinanceiroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_entrada_evento_financeiro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EntradaEventoFinanceiroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaEventoFinanceiroDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EntradaEventoFinanceiro> getList() {
		return getList(null);
	}

	public static ArrayList<EntradaEventoFinanceiro> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EntradaEventoFinanceiro> list = new ArrayList<EntradaEventoFinanceiro>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EntradaEventoFinanceiro obj = EntradaEventoFinanceiroDAO.get(rsm.getInt("cd_documento_entrada"), rsm.getInt("cd_evento_financeiro"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaEventoFinanceiroDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_entrada_evento_financeiro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
