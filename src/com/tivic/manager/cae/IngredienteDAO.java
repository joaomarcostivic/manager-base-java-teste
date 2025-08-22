package com.tivic.manager.cae;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class IngredienteDAO{

	public static int insert(Ingrediente objeto) {
		return insert(objeto, null);
	}

	public static int insert(Ingrediente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.grl.ProdutoDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO cae_ingrediente (cd_ingrediente,"+
			                                  "nm_ingrediente,"+
			                                  "vl_per_capta,"+
			                                  "vl_kcal,"+
			                                  "vl_cho,"+
			                                  "vl_ptn,"+
			                                  "vl_lip,"+
			                                  "vl_fibras,"+
			                                  "vl_vit_a,"+
			                                  "vl_vit_c,"+
			                                  "vl_ca,"+
			                                  "vl_fe,"+
			                                  "vl_mg,"+
			                                  "vl_zn,"+
			                                  "cd_grupo,"+
			                                  "cd_unidade_medida) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmIngrediente());
			pstmt.setDouble(3,objeto.getVlPerCapta());
			pstmt.setDouble(4,objeto.getVlKcal());
			pstmt.setDouble(5,objeto.getVlCho());
			pstmt.setDouble(6,objeto.getVlPtn());
			pstmt.setDouble(7,objeto.getVlLip());
			pstmt.setDouble(8,objeto.getVlFibras());
			pstmt.setDouble(9,objeto.getVlVitA());
			pstmt.setDouble(10,objeto.getVlVitC());
			pstmt.setDouble(11,objeto.getVlCa());
			pstmt.setDouble(12,objeto.getVlFe());
			pstmt.setDouble(13,objeto.getVlMg());
			pstmt.setDouble(14,objeto.getVlZn());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdGrupo());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdUnidadeMedida());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IngredienteDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Ingrediente objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Ingrediente objeto, int cdIngredienteOld) {
		return update(objeto, cdIngredienteOld, null);
	}

	public static int update(Ingrediente objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Ingrediente objeto, int cdIngredienteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Ingrediente objetoTemp = get(objeto.getCdProdutoServico(), connect);
			if (objetoTemp == null) 
				pstmt = connect.prepareStatement("INSERT INTO cae_ingrediente (cd_ingrediente,"+
			                                  "nm_ingrediente,"+
			                                  "vl_per_capta,"+
			                                  "vl_kcal,"+
			                                  "vl_cho,"+
			                                  "vl_ptn,"+
			                                  "vl_lip,"+
			                                  "vl_fibras,"+
			                                  "vl_vit_a,"+
			                                  "vl_vit_c,"+
			                                  "vl_ca,"+
			                                  "vl_fe,"+
			                                  "vl_mg,"+
			                                  "vl_zn,"+
			                                  "cd_grupo,"+
			                                  "cd_unidade_medida) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE cae_ingrediente SET cd_ingrediente=?,"+
												      		   "nm_ingrediente=?,"+
												      		   "vl_per_capta=?,"+
												      		   "vl_kcal=?,"+
												      		   "vl_cho=?,"+
												      		   "vl_ptn=?,"+
												      		   "vl_lip=?,"+
												      		   "vl_fibras=?,"+
												      		   "vl_vit_a=?,"+
												      		   "vl_vit_c=?,"+
												      		   "vl_ca=?,"+
												      		   "vl_fe=?,"+
												      		   "vl_mg=?,"+
												      		   "vl_zn=?,"+
												      		   "cd_grupo=?,"+
												      		   "cd_unidade_medida=? WHERE cd_ingrediente=?");
			pstmt.setInt(1,objeto.getCdIngrediente());
			pstmt.setString(2,objeto.getNmIngrediente());
			pstmt.setDouble(3,objeto.getVlPerCapta());
			pstmt.setDouble(4,objeto.getVlKcal());
			pstmt.setDouble(5,objeto.getVlCho());
			pstmt.setDouble(6,objeto.getVlPtn());
			pstmt.setDouble(7,objeto.getVlLip());
			pstmt.setDouble(8,objeto.getVlFibras());
			pstmt.setDouble(9,objeto.getVlVitA());
			pstmt.setDouble(10,objeto.getVlVitC());
			pstmt.setDouble(11,objeto.getVlCa());
			pstmt.setDouble(12,objeto.getVlFe());
			pstmt.setDouble(13,objeto.getVlMg());
			pstmt.setDouble(14,objeto.getVlZn());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdGrupo());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdUnidadeMedida());
			if (objetoTemp != null) {
				pstmt.setInt(17, cdIngredienteOld!=0 ? cdIngredienteOld : objeto.getCdProdutoServico());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.ProdutoDAO.update(objeto, connect)<=0) {
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
			System.err.println("Erro! IngredienteDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdIngrediente) {
		return delete(cdIngrediente, null);
	}

	public static int delete(int cdIngrediente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM cae_ingrediente WHERE cd_ingrediente=?");
			pstmt.setInt(1, cdIngrediente);
			pstmt.executeUpdate();
			if (com.tivic.manager.grl.ProdutoDAO.delete(cdIngrediente, connect)<=0) {
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
			System.err.println("Erro! IngredienteDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Ingrediente get(int cdIngrediente) {
		return get(cdIngrediente, null);
	}

	public static Ingrediente get(int cdIngrediente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM cae_ingrediente A, grl_produto B, grl_produto_servico C WHERE A.cd_ingrediente=B.cd_produto_servico AND B.cd_produto_servico = C.cd_produto_servico AND A.cd_ingrediente=?");
			pstmt.setInt(1, cdIngrediente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Ingrediente(rs.getInt("cd_produto_servico"),
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
						rs.getInt("cd_marca"),
						rs.getString("nm_modelo"),
						rs.getInt("cd_ncm"),
						rs.getString("nr_referencia"),
						rs.getFloat("vl_peso_unitario"),
						rs.getFloat("vl_peso_unitario_embalagem"),
						rs.getFloat("vl_comprimento"),
						rs.getFloat("vl_largura"),
						rs.getFloat("vl_altura"),
						rs.getFloat("vl_comprimento_embalagem"),
						rs.getFloat("vl_largura_embalagem"),
						rs.getFloat("vl_altura_embalagem"),
						rs.getInt("qt_embalagem"),
						rs.getString("nm_ingrediente"),
						rs.getDouble("vl_per_capta"),
						rs.getDouble("vl_kcal"),
						rs.getDouble("vl_cho"),
						rs.getDouble("vl_ptn"),
						rs.getDouble("vl_lip"),
						rs.getDouble("vl_fibras"),
						rs.getDouble("vl_vit_a"),
						rs.getDouble("vl_vit_c"),
						rs.getDouble("vl_ca"),
						rs.getDouble("vl_fe"),
						rs.getDouble("vl_mg"),
						rs.getDouble("vl_zn"),
						rs.getInt("cd_grupo"),
						rs.getInt("cd_unidade_medida"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IngredienteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_ingrediente A, grl_produto B, grl_produto_servico C WHERE A.cd_ingrediente=B.cd_produto_servico AND B.cd_produto_servico = C.cd_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! IngredienteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Ingrediente> getList() {
		return getList(null);
	}

	public static ArrayList<Ingrediente> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Ingrediente> list = new ArrayList<Ingrediente>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Ingrediente obj = IngredienteDAO.get(rsm.getInt("cd_ingrediente"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! IngredienteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM cae_ingrediente A, grl_produto B, grl_produto_servico C WHERE A.cd_ingrediente=B.cd_produto_servico AND B.cd_produto_servico = C.cd_produto_servico", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
