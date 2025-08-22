package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ProdutoTerceiroDAO{

	public static int insert(ProdutoTerceiro objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProdutoTerceiro objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_produto_terceiro", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProdutoTerceiro(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_produto_terceiro (cd_produto_terceiro,"+
			                                  "cd_marca,"+
			                                  "cd_modelo,"+
			                                  "nm_produto_terceiro,"+
			                                  "nr_serie,"+
			                                  "id_produto_terceiro) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdMarca()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMarca());
			if(objeto.getCdModelo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdModelo());
			pstmt.setString(4,objeto.getNmProdutoTerceiro());
			pstmt.setString(5,objeto.getNrSerie());
			pstmt.setString(6,objeto.getIdProdutoTerceiro());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoTerceiroDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoTerceiroDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoTerceiro objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ProdutoTerceiro objeto, int cdProdutoTerceiroOld) {
		return update(objeto, cdProdutoTerceiroOld, null);
	}

	public static int update(ProdutoTerceiro objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ProdutoTerceiro objeto, int cdProdutoTerceiroOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_produto_terceiro SET cd_produto_terceiro=?,"+
												      		   "cd_marca=?,"+
												      		   "cd_modelo=?,"+
												      		   "nm_produto_terceiro=?,"+
												      		   "nr_serie=?,"+
												      		   "id_produto_terceiro=? WHERE cd_produto_terceiro=?");
			pstmt.setInt(1,objeto.getCdProdutoTerceiro());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMarca());
			if(objeto.getCdModelo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdModelo());
			pstmt.setString(4,objeto.getNmProdutoTerceiro());
			pstmt.setString(5,objeto.getNrSerie());
			pstmt.setString(6,objeto.getIdProdutoTerceiro());
			pstmt.setInt(7, cdProdutoTerceiroOld!=0 ? cdProdutoTerceiroOld : objeto.getCdProdutoTerceiro());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoTerceiroDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoTerceiroDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProdutoTerceiro) {
		return delete(cdProdutoTerceiro, null);
	}

	public static int delete(int cdProdutoTerceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_produto_terceiro WHERE cd_produto_terceiro=?");
			pstmt.setInt(1, cdProdutoTerceiro);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoTerceiroDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoTerceiroDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoTerceiro get(int cdProdutoTerceiro) {
		return get(cdProdutoTerceiro, null);
	}

	public static ProdutoTerceiro get(int cdProdutoTerceiro, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_terceiro WHERE cd_produto_terceiro=?");
			pstmt.setInt(1, cdProdutoTerceiro);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProdutoTerceiro(rs.getInt("cd_produto_terceiro"),
						rs.getInt("cd_marca"),
						rs.getInt("cd_modelo"),
						rs.getString("nm_produto_terceiro"),
						rs.getString("nr_serie"),
						rs.getString("id_produto_terceiro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoTerceiroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoTerceiroDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_terceiro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoTerceiroDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoTerceiroDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_produto_terceiro", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
