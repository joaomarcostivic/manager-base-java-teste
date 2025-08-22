package com.tivic.manager.flp;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class GrupoPagamentoDAO{

	public static int insert(GrupoPagamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(GrupoPagamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("flp_grupo_pagamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdGrupoPagamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO flp_grupo_pagamento (cd_grupo_pagamento,"+
			                                  "nm_grupo_pagamento,"+
			                                  "cd_empresa,"+
			                                  "id_grupo_pagamento) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmGrupoPagamento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setString(4,objeto.getIdGrupoPagamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoPagamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoPagamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(GrupoPagamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(GrupoPagamento objeto, int cdGrupoPagamentoOld) {
		return update(objeto, cdGrupoPagamentoOld, null);
	}

	public static int update(GrupoPagamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(GrupoPagamento objeto, int cdGrupoPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE flp_grupo_pagamento SET cd_grupo_pagamento=?,"+
												      		   "nm_grupo_pagamento=?,"+
												      		   "cd_empresa=?,"+
												      		   "id_grupo_pagamento=? WHERE cd_grupo_pagamento=?");
			pstmt.setInt(1,objeto.getCdGrupoPagamento());
			pstmt.setString(2,objeto.getNmGrupoPagamento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setString(4,objeto.getIdGrupoPagamento());
			pstmt.setInt(5, cdGrupoPagamentoOld!=0 ? cdGrupoPagamentoOld : objeto.getCdGrupoPagamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoPagamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoPagamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupoPagamento) {
		return delete(cdGrupoPagamento, null);
	}

	public static int delete(int cdGrupoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM flp_grupo_pagamento WHERE cd_grupo_pagamento=?");
			pstmt.setInt(1, cdGrupoPagamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoPagamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoPagamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static GrupoPagamento get(int cdGrupoPagamento) {
		return get(cdGrupoPagamento, null);
	}

	public static GrupoPagamento get(int cdGrupoPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM flp_grupo_pagamento WHERE cd_grupo_pagamento=?");
			pstmt.setInt(1, cdGrupoPagamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new GrupoPagamento(rs.getInt("cd_grupo_pagamento"),
						rs.getString("nm_grupo_pagamento"),
						rs.getInt("cd_empresa"),
						rs.getString("id_grupo_pagamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoPagamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoPagamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM flp_grupo_pagamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoPagamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoPagamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<GrupoPagamento> getList() {
		return getList(null);
	}

	public static ArrayList<GrupoPagamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<GrupoPagamento> list = new ArrayList<GrupoPagamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				GrupoPagamento obj = GrupoPagamentoDAO.get(rsm.getInt("cd_grupo_pagamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoPagamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM flp_grupo_pagamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
