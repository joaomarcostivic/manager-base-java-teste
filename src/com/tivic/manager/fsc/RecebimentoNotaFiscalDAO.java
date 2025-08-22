package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class RecebimentoNotaFiscalDAO{

	public static int insert(RecebimentoNotaFiscal objeto) {
		return insert(objeto, null);
	}

	public static int insert(RecebimentoNotaFiscal objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			int code = Conexao.getSequenceCode("fsc_recebimento_nota_fiscal", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRecebimentoNotaFiscal(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_recebimento_nota_fiscal (cd_recebimento_nota_fiscal,"+
			                                  "cd_pessoa,"+
			                                  "nr_mes,"+
			                                  "dt_recebimento,"+
			                                  "st_recebimento,"+
			                                  "nr_ano,"+
			                                  "cd_nota_fiscal,"+
			                                  "cd_concessao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getNrMes());
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStRecebimento());
			pstmt.setInt(6,objeto.getNrAno());
			if(objeto.getCdNotaFiscal()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdNotaFiscal());
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConcessao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RecebimentoNotaFiscal objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(RecebimentoNotaFiscal objeto, int cdRecebimentoNotaFiscalOld, int cdNotaFiscalOld) {
		return update(objeto, cdRecebimentoNotaFiscalOld, cdNotaFiscalOld, null);
	}

	public static int update(RecebimentoNotaFiscal objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(RecebimentoNotaFiscal objeto, int cdRecebimentoNotaFiscalOld, int cdNotaFiscalOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_recebimento_nota_fiscal SET cd_recebimento_nota_fiscal=?,"+
												      		   "cd_pessoa=?,"+
												      		   "nr_mes=?,"+
												      		   "dt_recebimento=?,"+
												      		   "st_recebimento=?,"+
												      		   "nr_ano=?,"+
												      		   "cd_nota_fiscal=?,"+
												      		   "cd_concessao=? WHERE cd_recebimento_nota_fiscal=?");
			pstmt.setInt(1,objeto.getCdRecebimentoNotaFiscal());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getNrMes());
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStRecebimento());
			pstmt.setInt(6,objeto.getNrAno());
			if(objeto.getCdNotaFiscal()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdNotaFiscal());
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConcessao());
			pstmt.setInt(9, cdRecebimentoNotaFiscalOld!=0 ? cdRecebimentoNotaFiscalOld : objeto.getCdRecebimentoNotaFiscal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRecebimentoNotaFiscal) {
		return delete(cdRecebimentoNotaFiscal, null);
	}

	public static int delete(int cdRecebimentoNotaFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_recebimento_nota_fiscal WHERE cd_recebimento_nota_fiscal=?");
			pstmt.setInt(1, cdRecebimentoNotaFiscal);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RecebimentoNotaFiscal get(int cdRecebimentoNotaFiscal) {
		return get(cdRecebimentoNotaFiscal, null);
	}

	public static RecebimentoNotaFiscal get(int cdRecebimentoNotaFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_recebimento_nota_fiscal WHERE cd_recebimento_nota_fiscal=?");
			pstmt.setInt(1, cdRecebimentoNotaFiscal);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RecebimentoNotaFiscal(rs.getInt("cd_recebimento_nota_fiscal"),
						rs.getInt("cd_pessoa"),
						rs.getInt("nr_mes"),
						(rs.getTimestamp("dt_recebimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_recebimento").getTime()),
						rs.getInt("st_recebimento"),
						rs.getInt("nr_ano"),
						rs.getInt("cd_nota_fiscal"),
						rs.getInt("cd_concessao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_recebimento_nota_fiscal");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RecebimentoNotaFiscal> getList() {
		return getList(null);
	}

	public static ArrayList<RecebimentoNotaFiscal> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RecebimentoNotaFiscal> list = new ArrayList<RecebimentoNotaFiscal>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RecebimentoNotaFiscal obj = RecebimentoNotaFiscalDAO.get(rsm.getInt("cd_recebimento_nota_fiscal"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecebimentoNotaFiscalDAO.getList: " + e);
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
		return Search.find("SELECT * FROM fsc_recebimento_nota_fiscal", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}