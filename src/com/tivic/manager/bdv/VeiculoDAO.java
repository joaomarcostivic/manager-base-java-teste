package com.tivic.manager.bdv;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class VeiculoDAO{

	public static int insert(Veiculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Veiculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("bdv_veiculo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdVeiculo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO bdv_veiculo (cd_veiculo,"+
			                                  "nr_placa,"+
			                                  "nr_renavan,"+
			                                  "tp_veiculo,"+
			                                  "tp_carroceria,"+
			                                  "nr_ano_modelo,"+
			                                  "nr_ano_fabricacao,"+
			                                  "nr_codigo_municipio,"+
			                                  "nm_municipio,"+
			                                  "sg_estado,"+
			                                  "nr_codigo_marca,"+
			                                  "nm_marca_modelo,"+
			                                  "nr_codigo_cor,"+
			                                  "nm_cor,"+
			                                  "nr_codigo_especie,"+
			                                  "nm_especie,"+
			                                  "nm_tipo,"+
			                                  "nm_categoria,"+
			                                  "dt_informacao,"+
			                                  "nr_chassi) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNrPlaca());
			pstmt.setString(3,objeto.getNrRenavan());
			pstmt.setInt(4,objeto.getTpVeiculo());
			pstmt.setInt(5,objeto.getTpCarroceria());
			pstmt.setInt(6,objeto.getNrAnoModelo());
			pstmt.setInt(7,objeto.getNrAnoFabricacao());
			pstmt.setInt(8,objeto.getNrCodigoMunicipio());
			pstmt.setString(9,objeto.getNmMunicipio());
			pstmt.setString(10,objeto.getSgEstado());
			pstmt.setInt(11,objeto.getNrCodigoMarca());
			pstmt.setString(12,objeto.getNmMarcaModelo());
			pstmt.setInt(13,objeto.getNrCodigoCor());
			pstmt.setString(14,objeto.getNmCor());
			pstmt.setInt(15,objeto.getNrCodigoEspecie());
			pstmt.setString(16,objeto.getNmEspecie());
			pstmt.setString(17,objeto.getNmTipo());
			pstmt.setString(18,objeto.getNmCategoria());
			if(objeto.getDtInformacao()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtInformacao().getTimeInMillis()));
			pstmt.setString(20,objeto.getNrChassi());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Veiculo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Veiculo objeto, int cdVeiculoOld) {
		return update(objeto, cdVeiculoOld, null);
	}

	public static int update(Veiculo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Veiculo objeto, int cdVeiculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE bdv_veiculo SET cd_veiculo=?,"+
												      		   "nr_placa=?,"+
												      		   "nr_renavan=?,"+
												      		   "tp_veiculo=?,"+
												      		   "tp_carroceria=?,"+
												      		   "nr_ano_modelo=?,"+
												      		   "nr_ano_fabricacao=?,"+
												      		   "nr_codigo_municipio=?,"+
												      		   "nm_municipio=?,"+
												      		   "sg_estado=?,"+
												      		   "nr_codigo_marca=?,"+
												      		   "nm_marca_modelo=?,"+
												      		   "nr_codigo_cor=?,"+
												      		   "nm_cor=?,"+
												      		   "nr_codigo_especie=?,"+
												      		   "nm_especie=?,"+
												      		   "nm_tipo=?,"+
												      		   "nm_categoria=?,"+
												      		   "dt_informacao=?,"+
												      		   "nr_chassi=? WHERE cd_veiculo=?");
			pstmt.setInt(1,objeto.getCdVeiculo());
			pstmt.setString(2,objeto.getNrPlaca());
			pstmt.setString(3,objeto.getNrRenavan());
			pstmt.setInt(4,objeto.getTpVeiculo());
			pstmt.setInt(5,objeto.getTpCarroceria());
			pstmt.setInt(6,objeto.getNrAnoModelo());
			pstmt.setInt(7,objeto.getNrAnoFabricacao());
			pstmt.setInt(8,objeto.getNrCodigoMunicipio());
			pstmt.setString(9,objeto.getNmMunicipio());
			pstmt.setString(10,objeto.getSgEstado());
			pstmt.setInt(11,objeto.getNrCodigoMarca());
			pstmt.setString(12,objeto.getNmMarcaModelo());
			pstmt.setInt(13,objeto.getNrCodigoCor());
			pstmt.setString(14,objeto.getNmCor());
			pstmt.setInt(15,objeto.getNrCodigoEspecie());
			pstmt.setString(16,objeto.getNmEspecie());
			pstmt.setString(17,objeto.getNmTipo());
			pstmt.setString(18,objeto.getNmCategoria());
			if(objeto.getDtInformacao()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtInformacao().getTimeInMillis()));
			pstmt.setString(20,objeto.getNrChassi());
			pstmt.setInt(21, cdVeiculoOld!=0 ? cdVeiculoOld : objeto.getCdVeiculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVeiculo) {
		return delete(cdVeiculo, null);
	}

	public static int delete(int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bdv_veiculo WHERE cd_veiculo=?");
			pstmt.setInt(1, cdVeiculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Veiculo get(int cdVeiculo) {
		return get(cdVeiculo, null);
	}

	public static Veiculo get(int cdVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bdv_veiculo WHERE cd_veiculo=?");
			pstmt.setInt(1, cdVeiculo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Veiculo(rs.getInt("cd_veiculo"),
						rs.getString("nr_placa"),
						rs.getString("nr_renavan"),
						rs.getInt("tp_veiculo"),
						rs.getInt("tp_carroceria"),
						rs.getInt("nr_ano_modelo"),
						rs.getInt("nr_ano_fabricacao"),
						rs.getInt("nr_codigo_municipio"),
						rs.getString("nm_municipio"),
						rs.getString("sg_estado"),
						rs.getInt("nr_codigo_marca"),
						rs.getString("nm_marca_modelo"),
						rs.getInt("nr_codigo_cor"),
						rs.getString("nm_cor"),
						rs.getInt("nr_codigo_especie"),
						rs.getString("nm_especie"),
						rs.getString("nm_tipo"),
						rs.getString("nm_categoria"),
						(rs.getTimestamp("dt_informacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_informacao").getTime()),
						rs.getString("nr_chassi"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM bdv_veiculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Veiculo> getList() {
		return getList(null);
	}

	public static ArrayList<Veiculo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Veiculo> list = new ArrayList<Veiculo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Veiculo obj = VeiculoDAO.get(rsm.getInt("cd_veiculo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM bdv_veiculo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
