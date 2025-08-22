package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class AitVeiculoDAO{

	public static int insert(AitVeiculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(AitVeiculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_ait_veiculo");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_ait");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdAit()));
			int code = Conexao.getSequenceCode("mob_ait_veiculo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAitVeiculo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_veiculo (cd_ait_veiculo,"+
			                                  "cd_ait,"+
			                                  "cd_especie,"+
			                                  "cd_cor,"+
			                                  "cd_tipo,"+
			                                  "cd_categoria,"+
			                                  "cd_marca,"+
			                                  "cd_marca_autuacao,"+
			                                  "uf_veiculo,"+
			                                  "nr_renavam,"+
			                                  "ds_ano_fabricacao,"+
			                                  "ds_ano_modelo,"+
			                                  "cd_veiculo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdAit()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAit());
			if(objeto.getCdEspecie()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEspecie());
			if(objeto.getCdCor()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCor());
			if(objeto.getCdTipo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipo());
			if(objeto.getCdCategoria()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCategoria());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdMarca());
			if(objeto.getCdMarcaAutuacao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdMarcaAutuacao());
			pstmt.setString(9,objeto.getUfVeiculo());
			pstmt.setString(10,objeto.getNrRenavam());
			pstmt.setString(11,objeto.getDsAnoFabricacao());
			pstmt.setString(12,objeto.getDsAnoModelo());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdVeiculo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitVeiculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitVeiculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AitVeiculo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AitVeiculo objeto, int cdAitVeiculoOld, int cdAitOld) {
		return update(objeto, cdAitVeiculoOld, cdAitOld, null);
	}

	public static int update(AitVeiculo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AitVeiculo objeto, int cdAitVeiculoOld, int cdAitOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_veiculo SET cd_ait_veiculo=?,"+
												      		   "cd_ait=?,"+
												      		   "cd_especie=?,"+
												      		   "cd_cor=?,"+
												      		   "cd_tipo=?,"+
												      		   "cd_categoria=?,"+
												      		   "cd_marca=?,"+
												      		   "cd_marca_autuacao=?,"+
												      		   "uf_veiculo=?,"+
												      		   "nr_renavam=?,"+
												      		   "ds_ano_fabricacao=?,"+
												      		   "ds_ano_modelo=?,"+
												      		   "cd_veiculo=?  WHERE cd_ait_veiculo=? AND cd_ait=?");
			pstmt.setInt(1,objeto.getCdAitVeiculo());
			pstmt.setInt(2,objeto.getCdAit());
			if(objeto.getCdEspecie()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEspecie());
			if(objeto.getCdCor()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCor());
			if(objeto.getCdTipo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTipo());
			if(objeto.getCdCategoria()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCategoria());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdMarca());
			if(objeto.getCdMarcaAutuacao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdMarcaAutuacao());
			pstmt.setString(9,objeto.getUfVeiculo());
			pstmt.setString(10,objeto.getNrRenavam());
			pstmt.setString(11,objeto.getDsAnoFabricacao());
			pstmt.setString(12,objeto.getDsAnoModelo());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdVeiculo());
			pstmt.setInt(15, cdAitVeiculoOld!=0 ? cdAitVeiculoOld : objeto.getCdAitVeiculo());
			pstmt.setInt(16, cdAitOld!=0 ? cdAitOld : objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitVeiculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitVeiculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAitVeiculo, int cdAit) {
		return delete(cdAitVeiculo, cdAit, null);
	}

	public static int delete(int cdAitVeiculo, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_ait_veiculo WHERE cd_ait_veiculo=? AND cd_ait=?");
			pstmt.setInt(1, cdAitVeiculo);
			pstmt.setInt(2, cdAit);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitVeiculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AitVeiculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AitVeiculo get(int cdAitVeiculo, int cdAit) {
		return get(cdAitVeiculo, cdAit, null);
	}

	public static AitVeiculo get(int cdAitVeiculo, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_veiculo WHERE cd_ait_veiculo=? AND cd_ait=?");
			pstmt.setInt(1, cdAitVeiculo);
			pstmt.setInt(2, cdAit);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AitVeiculo(rs.getInt("cd_ait_veiculo"),
						rs.getInt("cd_ait"),
						rs.getInt("cd_especie"),
						rs.getInt("cd_cor"),
						rs.getInt("cd_tipo"),
						rs.getInt("cd_categoria"),
						rs.getInt("cd_marca"),
						rs.getInt("cd_marca_autuacao"),
						rs.getString("uf_veiculo"),
						rs.getString("nr_renavam"),
						rs.getString("ds_ano_fabricacao"),
						rs.getString("ds_ano_modelo"),
						rs.getInt("cd_veiculo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitVeiculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitVeiculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_veiculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitVeiculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitVeiculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AitVeiculo> getList() {
		return getList(null);
	}

	public static ArrayList<AitVeiculo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AitVeiculo> list = new ArrayList<AitVeiculo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AitVeiculo obj = AitVeiculoDAO.get(rsm.getInt("cd_ait_veiculo"), rsm.getInt("cd_ait"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitVeiculoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_ait_veiculo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
