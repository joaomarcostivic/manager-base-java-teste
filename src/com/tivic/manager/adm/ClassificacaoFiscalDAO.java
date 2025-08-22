package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ClassificacaoFiscalDAO{

	public static int insert(ClassificacaoFiscal objeto) {
		return insert(objeto, null);
	}

	public static int insert(ClassificacaoFiscal objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_classificacao_fiscal", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdClassificacaoFiscal(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_classificacao_fiscal (cd_classificacao_fiscal,"+
			                                  "cd_classificacao_fiscal_superior,"+
			                                  "nm_classificacao_fiscal,"+
			                                  "id_classificacao_fiscal,"+
			                                  "nr_classe,"+
			                                  "nr_enquadramento,"+
			                                  "tp_origem) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdClassificacaoFiscalSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdClassificacaoFiscalSuperior());
			pstmt.setString(3,objeto.getNmClassificacaoFiscal());
			pstmt.setString(4,objeto.getIdClassificacaoFiscal());
			pstmt.setString(5,objeto.getNrClasse());
			pstmt.setString(6,objeto.getNrEnquadramento());
			pstmt.setInt(7,objeto.getTpOrigem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ClassificacaoFiscal objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ClassificacaoFiscal objeto, int cdClassificacaoFiscalOld) {
		return update(objeto, cdClassificacaoFiscalOld, null);
	}

	public static int update(ClassificacaoFiscal objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ClassificacaoFiscal objeto, int cdClassificacaoFiscalOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_classificacao_fiscal SET cd_classificacao_fiscal=?,"+
												      		   "cd_classificacao_fiscal_superior=?,"+
												      		   "nm_classificacao_fiscal=?,"+
												      		   "id_classificacao_fiscal=?,"+
												      		   "nr_classe=?,"+
												      		   "nr_enquadramento=?,"+
												      		   "tp_origem=? WHERE cd_classificacao_fiscal=?");
			pstmt.setInt(1,objeto.getCdClassificacaoFiscal());
			if(objeto.getCdClassificacaoFiscalSuperior()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdClassificacaoFiscalSuperior());
			pstmt.setString(3,objeto.getNmClassificacaoFiscal());
			pstmt.setString(4,objeto.getIdClassificacaoFiscal());
			pstmt.setString(5,objeto.getNrClasse());
			pstmt.setString(6,objeto.getNrEnquadramento());
			pstmt.setInt(7,objeto.getTpOrigem());
			pstmt.setInt(8, cdClassificacaoFiscalOld!=0 ? cdClassificacaoFiscalOld : objeto.getCdClassificacaoFiscal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdClassificacaoFiscal) {
		return delete(cdClassificacaoFiscal, null);
	}

	public static int delete(int cdClassificacaoFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_classificacao_fiscal WHERE cd_classificacao_fiscal=?");
			pstmt.setInt(1, cdClassificacaoFiscal);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ClassificacaoFiscal get(int cdClassificacaoFiscal) {
		return get(cdClassificacaoFiscal, null);
	}

	public static ClassificacaoFiscal get(int cdClassificacaoFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_classificacao_fiscal WHERE cd_classificacao_fiscal=?");
			pstmt.setInt(1, cdClassificacaoFiscal);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ClassificacaoFiscal(rs.getInt("cd_classificacao_fiscal"),
						rs.getInt("cd_classificacao_fiscal_superior"),
						rs.getString("nm_classificacao_fiscal"),
						rs.getString("id_classificacao_fiscal"),
						rs.getString("nr_classe"),
						rs.getString("nr_enquadramento"),
						rs.getInt("tp_origem"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_classificacao_fiscal");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ClassificacaoFiscal> getList() {
		return getList(null);
	}

	public static ArrayList<ClassificacaoFiscal> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ClassificacaoFiscal> list = new ArrayList<ClassificacaoFiscal>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ClassificacaoFiscal obj = ClassificacaoFiscalDAO.get(rsm.getInt("cd_classificacao_fiscal"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoFiscalDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_classificacao_fiscal", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
