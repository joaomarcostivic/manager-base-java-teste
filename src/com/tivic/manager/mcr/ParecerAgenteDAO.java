package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ParecerAgenteDAO{

	public static int insert(ParecerAgente objeto) {
		return insert(objeto, null);
	}

	public static int insert(ParecerAgente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mcr_parecer_agente", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdParecer(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mcr_parecer_agente (cd_parecer,"+
			                                  "cd_pessoa,"+
			                                  "txt_carater,"+
			                                  "txt_capital,"+
			                                  "txt_condicao_negocio,"+
			                                  "txt_capacidade_pagamento,"+
			                                  "txt_collateral,"+
			                                  "txt_conclusao,"+
			                                  "vl_sugerido,"+
			                                  "nr_parcelas_sugerido) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getTxtCarater());
			pstmt.setString(4,objeto.getTxtCapital());
			pstmt.setString(5,objeto.getTxtCondicaoNegocio());
			pstmt.setString(6,objeto.getTxtCapacidadePagamento());
			pstmt.setString(7,objeto.getTxtCollateral());
			pstmt.setString(8,objeto.getTxtConclusao());
			pstmt.setFloat(9,objeto.getVlSugerido());
			pstmt.setInt(10,objeto.getNrParcelasSugerido());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParecerAgenteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParecerAgenteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ParecerAgente objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ParecerAgente objeto, int cdParecerOld) {
		return update(objeto, cdParecerOld, null);
	}

	public static int update(ParecerAgente objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ParecerAgente objeto, int cdParecerOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mcr_parecer_agente SET cd_parecer=?,"+
												      		   "cd_pessoa=?,"+
												      		   "txt_carater=?,"+
												      		   "txt_capital=?,"+
												      		   "txt_condicao_negocio=?,"+
												      		   "txt_capacidade_pagamento=?,"+
												      		   "txt_collateral=?,"+
												      		   "txt_conclusao=?,"+
												      		   "vl_sugerido=?,"+
												      		   "nr_parcelas_sugerido=? WHERE cd_parecer=?");
			pstmt.setInt(1,objeto.getCdParecer());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getTxtCarater());
			pstmt.setString(4,objeto.getTxtCapital());
			pstmt.setString(5,objeto.getTxtCondicaoNegocio());
			pstmt.setString(6,objeto.getTxtCapacidadePagamento());
			pstmt.setString(7,objeto.getTxtCollateral());
			pstmt.setString(8,objeto.getTxtConclusao());
			pstmt.setFloat(9,objeto.getVlSugerido());
			pstmt.setInt(10,objeto.getNrParcelasSugerido());
			pstmt.setInt(11, cdParecerOld!=0 ? cdParecerOld : objeto.getCdParecer());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParecerAgenteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParecerAgenteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdParecer) {
		return delete(cdParecer, null);
	}

	public static int delete(int cdParecer, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mcr_parecer_agente WHERE cd_parecer=?");
			pstmt.setInt(1, cdParecer);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParecerAgenteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParecerAgenteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ParecerAgente get(int cdParecer) {
		return get(cdParecer, null);
	}

	public static ParecerAgente get(int cdParecer, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_parecer_agente WHERE cd_parecer=?");
			pstmt.setInt(1, cdParecer);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ParecerAgente(rs.getInt("cd_parecer"),
						rs.getInt("cd_pessoa"),
						rs.getString("txt_carater"),
						rs.getString("txt_capital"),
						rs.getString("txt_condicao_negocio"),
						rs.getString("txt_capacidade_pagamento"),
						rs.getString("txt_collateral"),
						rs.getString("txt_conclusao"),
						rs.getFloat("vl_sugerido"),
						rs.getInt("nr_parcelas_sugerido"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParecerAgenteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParecerAgenteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_parecer_agente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParecerAgenteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParecerAgenteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_parecer_agente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
