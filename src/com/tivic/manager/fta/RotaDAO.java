package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class RotaDAO{

	public static int insert(Rota objeto) {
		return insert(objeto, null);
	}

	public static int insert(Rota objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fta_rota", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRota(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_rota (cd_rota,"+
			                                  "cd_tipo_rota,"+
			                                  "cd_cidade_origem,"+
			                                  "cd_cidade_destino,"+
			                                  "cd_logradouro_origem,"+
			                                  "cd_logradouro_destino,"+
			                                  "nm_local_origem,"+
			                                  "nm_local_destino,"+
			                                  "qt_distancia,"+
			                                  "vl_frete,"+
			                                  "vl_frete_unidade,"+
			                                  "lg_pagamento_km,"+
			                                  "nm_rota," +
			                                  "cd_vendedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTipoRota()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoRota());
			if(objeto.getCdCidadeOrigem()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCidadeOrigem());
			if(objeto.getCdCidadeDestino()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCidadeDestino());
			if(objeto.getCdLogradouroOrigem()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdLogradouroOrigem());
			if(objeto.getCdLogradouroDestino()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdLogradouroDestino());
			pstmt.setString(7,objeto.getNmLocalOrigem());
			pstmt.setString(8,objeto.getNmLocalDestino());
			pstmt.setFloat(9,objeto.getQtDistancia());
			pstmt.setFloat(10,objeto.getVlFrete());
			pstmt.setFloat(11,objeto.getVlFreteUnidade());
			pstmt.setInt(12,objeto.getLgPagamentoKm());
			pstmt.setString(13,objeto.getNmRota());
			if(objeto.getCdVendedor()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdVendedor());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RotaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RotaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Rota objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Rota objeto, int cdRotaOld) {
		return update(objeto, cdRotaOld, null);
	}

	public static int update(Rota objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Rota objeto, int cdRotaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_rota SET cd_rota=?,"+
												      		   "cd_tipo_rota=?,"+
												      		   "cd_cidade_origem=?,"+
												      		   "cd_cidade_destino=?,"+
												      		   "cd_logradouro_origem=?,"+
												      		   "cd_logradouro_destino=?,"+
												      		   "nm_local_origem=?,"+
												      		   "nm_local_destino=?,"+
												      		   "qt_distancia=?,"+
												      		   "vl_frete=?,"+
												      		   "vl_frete_unidade=?,"+
												      		   "lg_pagamento_km=?,"+
												      		   "nm_rota=?," +
												      		   "cd_vendedor=? WHERE cd_rota=?");
			pstmt.setInt(1,objeto.getCdRota());
			if(objeto.getCdTipoRota()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoRota());
			if(objeto.getCdCidadeOrigem()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCidadeOrigem());
			if(objeto.getCdCidadeDestino()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCidadeDestino());
			if(objeto.getCdLogradouroOrigem()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdLogradouroOrigem());
			if(objeto.getCdLogradouroDestino()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdLogradouroDestino());
			pstmt.setString(7,objeto.getNmLocalOrigem());
			pstmt.setString(8,objeto.getNmLocalDestino());
			pstmt.setFloat(9,objeto.getQtDistancia());
			pstmt.setFloat(10,objeto.getVlFrete());
			pstmt.setFloat(11,objeto.getVlFreteUnidade());
			pstmt.setInt(12,objeto.getLgPagamentoKm());
			pstmt.setString(13,objeto.getNmRota());
			if(objeto.getCdVendedor()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdVendedor());
			pstmt.setInt(15, cdRotaOld!=0 ? cdRotaOld : objeto.getCdRota());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RotaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RotaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRota) {
		return delete(cdRota, null);
	}

	public static int delete(int cdRota, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_rota WHERE cd_rota=?");
			pstmt.setInt(1, cdRota);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RotaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RotaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Rota get(int cdRota) {
		return get(cdRota, null);
	}

	public static Rota get(int cdRota, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_rota WHERE cd_rota=?");
			pstmt.setInt(1, cdRota);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Rota(rs.getInt("cd_rota"),
						rs.getInt("cd_tipo_rota"),
						rs.getInt("cd_cidade_origem"),
						rs.getInt("cd_cidade_destino"),
						rs.getInt("cd_logradouro_origem"),
						rs.getInt("cd_logradouro_destino"),
						rs.getString("nm_local_origem"),
						rs.getString("nm_local_destino"),
						rs.getFloat("qt_distancia"),
						rs.getFloat("vl_frete"),
						rs.getFloat("vl_frete_unidade"),
						rs.getInt("lg_pagamento_km"),
						rs.getString("nm_rota"),
						rs.getInt("cd_vendedor"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RotaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RotaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_rota");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RotaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RotaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_rota", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
