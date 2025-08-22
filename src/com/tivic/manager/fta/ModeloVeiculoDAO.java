package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ModeloVeiculoDAO{

	public static int insert(ModeloVeiculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(ModeloVeiculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			//Usado para que não dê erro ao tentar cadastrar novo produto sem nome
			objeto.setNmProdutoServico(objeto.getNmModelo());
			int code = com.tivic.manager.bpm.BemDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_modelo_veiculo (cd_modelo,"+
			                                  "cd_marca,"+
			                                  "nr_portas,"+
			                                  "tp_combustivel,"+
			                                  "nr_capacidade,"+
			                                  "tp_reboque,"+
			                                  "tp_carga,"+
			                                  "nr_potencia,"+
			                                  "nr_cilindrada,"+
			                                  "qt_capacidade_tanque,"+
			                                  "qt_consumo_urbano,"+
			                                  "qt_consumo_rodoviario,"+
			                                  "tp_eixo_dianteiro,"+
			                                  "tp_eixo_traseiro,"+
			                                  "qt_eixos_dianteiros,"+
			                                  "qt_eixos_traseiros,"+
			                                  "nm_modelo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProdutoServico());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMarca());
			pstmt.setInt(3,objeto.getNrPortas());
			pstmt.setInt(4,objeto.getTpCombustivel());
			pstmt.setString(5,objeto.getNrCapacidade());
			pstmt.setInt(6,objeto.getTpReboque());
			pstmt.setInt(7,objeto.getTpCarga());
			pstmt.setInt(8,objeto.getNrPotencia());
			pstmt.setInt(9,objeto.getNrCilindrada());
			pstmt.setInt(10,objeto.getQtCapacidadeTanque());
			pstmt.setFloat(11,objeto.getQtConsumoUrbano());
			pstmt.setFloat(12,objeto.getQtConsumoRodoviario());
			pstmt.setInt(13,objeto.getTpEixoDianteiro());
			pstmt.setInt(14,objeto.getTpEixoTraseiro());
			pstmt.setInt(15,objeto.getQtEixosDianteiros());
			pstmt.setInt(16,objeto.getQtEixosTraseiros());
			pstmt.setString(17,objeto.getNmModelo());
			
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.out.println("Erro! ModeloVeiculoDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.out.println("Erro! ModeloVeiculoDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ModeloVeiculo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ModeloVeiculo objeto, int cdModeloOld) {
		return update(objeto, cdModeloOld, null);
	}

	public static int update(ModeloVeiculo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ModeloVeiculo objeto, int cdModeloOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			ModeloVeiculo objetoTemp = get(objeto.getCdModelo(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO fta_modelo_veiculo (cd_modelo,"+
			                                  "cd_marca,"+
			                                  "nr_portas,"+
			                                  "tp_combustivel,"+
			                                  "nr_capacidade,"+
			                                  "tp_reboque,"+
			                                  "tp_carga,"+
			                                  "nr_potencia,"+
			                                  "nr_cilindrada,"+
			                                  "qt_capacidade_tanque,"+
			                                  "qt_consumo_urbano,"+
			                                  "qt_consumo_rodoviario,"+
			                                  "tp_eixo_dianteiro,"+
			                                  "tp_eixo_traseiro,"+
			                                  "qt_eixos_dianteiros,"+
			                                  "qt_eixos_traseiros,"+
			                                  "nm_modelo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE fta_modelo_veiculo SET cd_modelo=?,"+
												      		   "cd_marca=?,"+
												      		   "nr_portas=?,"+
												      		   "tp_combustivel=?,"+
												      		   "nr_capacidade=?,"+
												      		   "tp_reboque=?,"+
												      		   "tp_carga=?,"+
												      		   "nr_potencia=?,"+
												      		   "nr_cilindrada=?,"+
												      		   "qt_capacidade_tanque=?,"+
												      		   "qt_consumo_urbano=?,"+
												      		   "qt_consumo_rodoviario=?,"+
												      		   "tp_eixo_dianteiro=?,"+
												      		   "tp_eixo_traseiro=?,"+
												      		   "qt_eixos_dianteiros=?,"+
												      		   "qt_eixos_traseiros=?,"+
												      		   "nm_modelo=? WHERE cd_modelo=?");
			System.out.println("update cd_modelo="+objeto.getCdModelo());
			
			pstmt.setInt(1,objeto.getCdModelo());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMarca());
			pstmt.setInt(3,objeto.getNrPortas());
			pstmt.setInt(4,objeto.getTpCombustivel());
			pstmt.setString(5,objeto.getNrCapacidade());
			pstmt.setInt(6,objeto.getTpReboque());
			pstmt.setInt(7,objeto.getTpCarga());
			pstmt.setInt(8,objeto.getNrPotencia());
			pstmt.setInt(9,objeto.getNrCilindrada());
			pstmt.setInt(10,objeto.getQtCapacidadeTanque());
			pstmt.setFloat(11,objeto.getQtConsumoUrbano());
			pstmt.setFloat(12,objeto.getQtConsumoRodoviario());
			pstmt.setInt(13,objeto.getTpEixoDianteiro());
			pstmt.setInt(14,objeto.getTpEixoTraseiro());
			pstmt.setInt(15,objeto.getQtEixosDianteiros());
			pstmt.setInt(16,objeto.getQtEixosTraseiros());
			pstmt.setString(17,objeto.getNmModelo());
			if (objetoTemp != null) {
				pstmt.setInt(18, cdModeloOld!=0 ? cdModeloOld : objeto.getCdModelo());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.bpm.BemDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloVeiculoDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloVeiculoDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdModelo) {
		return delete(cdModelo, null);
	}

	public static int delete(int cdModelo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_modelo_veiculo WHERE cd_modelo=?");
			pstmt.setInt(1, cdModelo);
			pstmt.executeUpdate();
			if (com.tivic.manager.bpm.BemDAO.delete(cdModelo, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloVeiculoDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloVeiculoDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ModeloVeiculo get(int cdModelo) {
		return get(cdModelo, null);
	}

	public static ModeloVeiculo get(int cdModelo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT *, C.cd_marca AS cd_marca_produto_servico, C.nm_modelo AS nm_modelo_produto_servico FROM fta_modelo_veiculo A, bpm_bem B, grl_produto_servico C WHERE A.cd_modelo=B.cd_bem AND B.cd_bem=C.cd_produto_servico AND A.cd_modelo=?");
			pstmt.setInt(1, cdModelo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ModeloVeiculo(rs.getInt("cd_produto_servico"),
						rs.getInt("cd_categoria_economica"),
						rs.getString("nm_produto_servico"),
						rs.getString("txt_produto_servico"),
						rs.getString("txt_especificacao"),
						rs.getString("txt_dado_tecnico"),
						rs.getString("txt_prazo_entrega"),
						rs.getInt("tp_produto_servico"),
						rs.getString("id_produto_servico"),
						rs.getString("sg_produto_servico"),
						rs.getInt("cd_classificacao_fiscal"),
						rs.getInt("cd_fabricante"),
						rs.getInt("cd_marca_produto_servico"),
						rs.getString("nm_modelo_produto_servico"),
						rs.getInt("cd_ncm"),
						rs.getString("nr_referencia"),
						rs.getInt("cd_classificacao"),
						rs.getFloat("pr_depreciacao"),
						rs.getInt("cd_modelo"),
						rs.getInt("cd_marca"),
						rs.getInt("nr_portas"),
						rs.getInt("tp_combustivel"),
						rs.getString("nr_capacidade"),
						rs.getInt("tp_reboque"),
						rs.getInt("tp_carga"),
						rs.getInt("nr_potencia"),
						rs.getInt("nr_cilindrada"),
						rs.getInt("qt_capacidade_tanque"),
						rs.getFloat("qt_consumo_urbano"),
						rs.getFloat("qt_consumo_rodoviario"),
						rs.getInt("tp_eixo_dianteiro"),
						rs.getInt("tp_eixo_traseiro"),
						rs.getInt("qt_eixos_dianteiros"),
						rs.getInt("qt_eixos_traseiros"),
						rs.getString("nm_modelo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloVeiculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloVeiculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_modelo_veiculo A, bpm_bem B, grl_produto_servico C WHERE A.cd_modelo=B.cd_bem AND B.cd_bem=C.cd_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ModeloVeiculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ModeloVeiculoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_modelo_veiculo A, bpm_bem B, grl_produto_servico C WHERE A.cd_modelo=B.cd_bem AND B.cd_bem=C.cd_produto_servico ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
