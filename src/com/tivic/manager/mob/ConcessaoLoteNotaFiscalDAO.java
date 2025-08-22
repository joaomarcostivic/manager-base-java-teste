package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ConcessaoLoteNotaFiscalDAO{

	public static int insert(ConcessaoLoteNotaFiscal objeto) {
		return insert(objeto, null);
	}

	public static int insert(ConcessaoLoteNotaFiscal objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_concessao_lote_nota_fiscal (cd_concessao_lote,"+
			                                  "cd_concessao,"+
			                                  "cd_nota_fiscal,"+
			                                  "nr_dias_frequencia,"+
			                                  "vl_mensal_lote) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdConcessaoLote()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdConcessaoLote());
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConcessao());
			if(objeto.getCdNotaFiscal()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdNotaFiscal());
			pstmt.setInt(4,objeto.getNrDiasFrequencia());
			pstmt.setFloat(5,objeto.getVlMensalLote());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ConcessaoLoteNotaFiscal objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ConcessaoLoteNotaFiscal objeto, int cdConcessaoLoteOld, int cdConcessaoOld, int cdNotaFiscalOld) {
		return update(objeto, cdConcessaoLoteOld, cdConcessaoOld, cdNotaFiscalOld, null);
	}

	public static int update(ConcessaoLoteNotaFiscal objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ConcessaoLoteNotaFiscal objeto, int cdConcessaoLoteOld, int cdConcessaoOld, int cdNotaFiscalOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_concessao_lote_nota_fiscal SET cd_concessao_lote=?,"+
												      		   "cd_concessao=?,"+
												      		   "cd_nota_fiscal=?,"+
												      		   "nr_dias_frequencia=?,"+
												      		   "vl_mensal_lote=? WHERE cd_concessao_lote=? AND cd_concessao=? AND cd_nota_fiscal=?");
			pstmt.setInt(1,objeto.getCdConcessaoLote());
			pstmt.setInt(2,objeto.getCdConcessao());
			pstmt.setInt(3,objeto.getCdNotaFiscal());
			pstmt.setInt(4,objeto.getNrDiasFrequencia());
			pstmt.setFloat(5,objeto.getVlMensalLote());
			pstmt.setInt(6, cdConcessaoLoteOld!=0 ? cdConcessaoLoteOld : objeto.getCdConcessaoLote());
			pstmt.setInt(7, cdConcessaoOld!=0 ? cdConcessaoOld : objeto.getCdConcessao());
			pstmt.setInt(8, cdNotaFiscalOld!=0 ? cdNotaFiscalOld : objeto.getCdNotaFiscal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConcessaoLote, int cdConcessao, int cdNotaFiscal) {
		return delete(cdConcessaoLote, cdConcessao, cdNotaFiscal, null);
	}

	public static int delete(int cdConcessaoLote, int cdConcessao, int cdNotaFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_concessao_lote_nota_fiscal WHERE cd_concessao_lote=? AND cd_concessao=? AND cd_nota_fiscal=?");
			pstmt.setInt(1, cdConcessaoLote);
			pstmt.setInt(2, cdConcessao);
			pstmt.setInt(3, cdNotaFiscal);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ConcessaoLoteNotaFiscal get(int cdConcessaoLote, int cdConcessao, int cdNotaFiscal) {
		return get(cdConcessaoLote, cdConcessao, cdNotaFiscal, null);
	}

	public static ConcessaoLoteNotaFiscal get(int cdConcessaoLote, int cdConcessao, int cdNotaFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_lote_nota_fiscal WHERE cd_concessao_lote=? AND cd_concessao=? AND cd_nota_fiscal=?");
			pstmt.setInt(1, cdConcessaoLote);
			pstmt.setInt(2, cdConcessao);
			pstmt.setInt(3, cdNotaFiscal);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ConcessaoLoteNotaFiscal(rs.getInt("cd_concessao_lote"),
						rs.getInt("cd_concessao"),
						rs.getInt("cd_nota_fiscal"),
						rs.getInt("nr_dias_frequencia"),
						rs.getFloat("vl_mensal_lote"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_lote_nota_fiscal");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ConcessaoLoteNotaFiscal> getList() {
		return getList(null);
	}

	public static ArrayList<ConcessaoLoteNotaFiscal> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ConcessaoLoteNotaFiscal> list = new ArrayList<ConcessaoLoteNotaFiscal>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ConcessaoLoteNotaFiscal obj = ConcessaoLoteNotaFiscalDAO.get(rsm.getInt("cd_concessao_lote"), rsm.getInt("cd_concessao"), rsm.getInt("cd_nota_fiscal"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoLoteNotaFiscalDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_concessao_lote_nota_fiscal", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}