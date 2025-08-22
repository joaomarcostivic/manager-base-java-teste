package com.tivic.manager.cae;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class RecomendacaoNutricionalDAO{

	public static int insert(RecomendacaoNutricional objeto) {
		return insert(objeto, null);
	}

	public static int insert(RecomendacaoNutricional objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("cae_recomendacao_nutricional", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRecomendacaoNutricional(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO cae_recomendacao_nutricional (cd_recomendacao_nutricional,"+
			                                  "idade_inicio,"+
			                                  "idade_fim,"+
			                                  "vl_kcal_min,"+
			                                  "vl_kcal_max,"+
			                                  "vl_cho_min,"+
			                                  "vl_cho_max,"+
			                                  "vl_ptn_min,"+
			                                  "vl_ptn_max,"+
			                                  "vl_lip_min,"+
			                                  "vl_lip_max,"+
			                                  "vl_fibras_min,"+
			                                  "vl_fibras_max,"+
			                                  "vl_vit_a_min,"+
			                                  "vl_vit_a_max,"+
			                                  "vl_vit_c_min,"+
			                                  "vl_vit_c_max,"+
			                                  "vl_ca_min,"+
			                                  "vl_ca_max,"+
			                                  "vl_fe_min,"+
			                                  "vl_fe_max,"+
			                                  "vl_mg_min,"+
			                                  "vl_mg_max,"+
			                                  "vl_zn_min,"+
			                                  "vl_zn_max,"+
											  "nm_recomendacao_nutricional) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getIdadeInicio());
			pstmt.setInt(3,objeto.getIdadeFim());
			pstmt.setDouble(4,objeto.getVlKcalMin());
			pstmt.setDouble(5,objeto.getVlKcalMax());
			pstmt.setDouble(6,objeto.getVlChoMin());
			pstmt.setDouble(7,objeto.getVlChoMax());
			pstmt.setDouble(8,objeto.getVlPtnMin());
			pstmt.setDouble(9,objeto.getVlPtnMax());
			pstmt.setDouble(10,objeto.getVlLipMin());
			pstmt.setDouble(11,objeto.getVlLipMax());
			pstmt.setDouble(12,objeto.getVlFibrasMin());
			pstmt.setDouble(13,objeto.getVlFibrasMax());
			pstmt.setDouble(14,objeto.getVlVitAMin());
			pstmt.setDouble(15,objeto.getVlVitAMax());
			pstmt.setDouble(16,objeto.getVlVitCMin());
			pstmt.setDouble(17,objeto.getVlVitCMax());
			pstmt.setDouble(18,objeto.getVlCaMin());
			pstmt.setDouble(19,objeto.getVlCaMax());
			pstmt.setDouble(20,objeto.getVlFeMin());
			pstmt.setDouble(21,objeto.getVlFeMax());
			pstmt.setDouble(22,objeto.getVlMgMin());
			pstmt.setDouble(23,objeto.getVlMgMax());
			pstmt.setDouble(24,objeto.getVlZnMin());
			pstmt.setDouble(25,objeto.getVlZnMax());
			pstmt.setString(26,objeto.getNmRecomendacaoNutricional());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecomendacaoNutricionalDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecomendacaoNutricionalDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RecomendacaoNutricional objeto) {
		return update(objeto, 0, null);
	}

	public static int update(RecomendacaoNutricional objeto, int cdRecomendacaoNutricionalOld) {
		return update(objeto, cdRecomendacaoNutricionalOld, null);
	}

	public static int update(RecomendacaoNutricional objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(RecomendacaoNutricional objeto, int cdRecomendacaoNutricionalOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE cae_recomendacao_nutricional SET cd_recomendacao_nutricional=?,"+
												      		   "idade_inicio=?,"+
												      		   "idade_fim=?,"+
												      		   "vl_kcal_min=?,"+
												      		   "vl_kcal_max=?,"+
												      		   "vl_cho_min=?,"+
												      		   "vl_cho_max=?,"+
												      		   "vl_ptn_min=?,"+
												      		   "vl_ptn_max=?,"+
												      		   "vl_lip_min=?,"+
												      		   "vl_lip_max=?,"+
												      		   "vl_fibras_min=?,"+
												      		   "vl_fibras_max=?,"+
												      		   "vl_vit_a_min=?,"+
												      		   "vl_vit_a_max=?,"+
												      		   "vl_vit_c_min=?,"+
												      		   "vl_vit_c_max=?,"+
												      		   "vl_ca_min=?,"+
												      		   "vl_ca_max=?,"+
												      		   "vl_fe_min=?,"+
												      		   "vl_fe_max=?,"+
												      		   "vl_mg_min=?,"+
												      		   "vl_mg_max=?,"+
												      		   "vl_zn_min=?,"+
												      		   "vl_zn_max=?,"+
															   "nm_recomendacao_nutricional=? WHERE cd_recomendacao_nutricional=?");
			pstmt.setInt(1,objeto.getCdRecomendacaoNutricional());
			pstmt.setInt(2,objeto.getIdadeInicio());
			pstmt.setInt(3,objeto.getIdadeFim());
			pstmt.setDouble(4,objeto.getVlKcalMin());
			pstmt.setDouble(5,objeto.getVlKcalMax());
			pstmt.setDouble(6,objeto.getVlChoMin());
			pstmt.setDouble(7,objeto.getVlChoMax());
			pstmt.setDouble(8,objeto.getVlPtnMin());
			pstmt.setDouble(9,objeto.getVlPtnMax());
			pstmt.setDouble(10,objeto.getVlLipMin());
			pstmt.setDouble(11,objeto.getVlLipMax());
			pstmt.setDouble(12,objeto.getVlFibrasMin());
			pstmt.setDouble(13,objeto.getVlFibrasMax());
			pstmt.setDouble(14,objeto.getVlVitAMin());
			pstmt.setDouble(15,objeto.getVlVitAMax());
			pstmt.setDouble(16,objeto.getVlVitCMin());
			pstmt.setDouble(17,objeto.getVlVitCMax());
			pstmt.setDouble(18,objeto.getVlCaMin());
			pstmt.setDouble(19,objeto.getVlCaMax());
			pstmt.setDouble(20,objeto.getVlFeMin());
			pstmt.setDouble(21,objeto.getVlFeMax());
			pstmt.setDouble(22,objeto.getVlMgMin());
			pstmt.setDouble(23,objeto.getVlMgMax());
			pstmt.setDouble(24,objeto.getVlZnMin());
			pstmt.setDouble(25,objeto.getVlZnMax());
			pstmt.setString(26,objeto.getNmRecomendacaoNutricional());
			pstmt.setInt(27, cdRecomendacaoNutricionalOld!=0 ? cdRecomendacaoNutricionalOld : objeto.getCdRecomendacaoNutricional());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecomendacaoNutricionalDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecomendacaoNutricionalDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRecomendacaoNutricional) {
		return delete(cdRecomendacaoNutricional, null);
	}

	public static int delete(int cdRecomendacaoNutricional, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM cae_recomendacao_nutricional WHERE cd_recomendacao_nutricional=?");
			pstmt.setInt(1, cdRecomendacaoNutricional);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecomendacaoNutricionalDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecomendacaoNutricionalDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RecomendacaoNutricional get(int cdRecomendacaoNutricional) {
		return get(cdRecomendacaoNutricional, null);
	}

	public static RecomendacaoNutricional get(int cdRecomendacaoNutricional, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM cae_recomendacao_nutricional WHERE cd_recomendacao_nutricional=?");
			pstmt.setInt(1, cdRecomendacaoNutricional);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RecomendacaoNutricional(rs.getInt("cd_recomendacao_nutricional"),
						rs.getInt("idade_inicio"),
						rs.getInt("idade_fim"),
						rs.getDouble("vl_kcal_min"),
						rs.getDouble("vl_kcal_max"),
						rs.getDouble("vl_cho_min"),
						rs.getDouble("vl_cho_max"),
						rs.getDouble("vl_ptn_min"),
						rs.getDouble("vl_ptn_max"),
						rs.getDouble("vl_lip_min"),
						rs.getDouble("vl_lip_max"),
						rs.getDouble("vl_fibras_min"),
						rs.getDouble("vl_fibras_max"),
						rs.getDouble("vl_vit_a_min"),
						rs.getDouble("vl_vit_a_max"),
						rs.getDouble("vl_vit_c_min"),
						rs.getDouble("vl_vit_c_max"),
						rs.getDouble("vl_ca_min"),
						rs.getDouble("vl_ca_max"),
						rs.getDouble("vl_fe_min"),
						rs.getDouble("vl_fe_max"),
						rs.getDouble("vl_mg_min"),
						rs.getDouble("vl_mg_max"),
						rs.getDouble("vl_zn_min"),
						rs.getDouble("vl_zn_max"),
						rs.getString("nm_recomendacao_nutricional"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecomendacaoNutricionalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecomendacaoNutricionalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM cae_recomendacao_nutricional");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecomendacaoNutricionalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecomendacaoNutricionalDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RecomendacaoNutricional> getList() {
		return getList(null);
	}

	public static ArrayList<RecomendacaoNutricional> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RecomendacaoNutricional> list = new ArrayList<RecomendacaoNutricional>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RecomendacaoNutricional obj = RecomendacaoNutricionalDAO.get(rsm.getInt("cd_recomendacao_nutricional"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecomendacaoNutricionalDAO.getList: " + e);
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
		return Search.find("SELECT * FROM cae_recomendacao_nutricional", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
