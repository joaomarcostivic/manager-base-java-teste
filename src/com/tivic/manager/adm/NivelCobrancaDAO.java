package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class NivelCobrancaDAO{

	public static int insert(NivelCobranca objeto) {
		return insert(objeto, null);
	}

	public static int insert(NivelCobranca objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			System.out.println("objeto = " + objeto);
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_nivel_cobranca");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_cobranca");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdCobranca()));
			int code = Conexao.getSequenceCode("adm_nivel_cobranca", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNivelCobranca(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_nivel_cobranca (cd_nivel_cobranca,"+
			                                  "cd_cobranca,"+
			                                  "id_nivel_cobranca,"+
			                                  "nm_nivel_cobranca,"+
			                                  "nm_descricao,"+
			                                  "qt_dias_apos_vencimento,"+
			                                  "qt_dias_entre_aviso_cobranca,"+
			                                  "lg_cobrar_juros,"+
			                                  "pr_taxa_juros,"+
			                                  "lg_encargos_mora,"+
			                                  "pr_encargos_mora,"+
			                                  "st_nivel_cobranca,"+
			                                  "lg_suspender_credito,"+
			                                  "lg_cancelar_credito,"+
			                                  "txt_nota) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCobranca()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCobranca());
			pstmt.setString(3,objeto.getIdNivelCobranca());
			pstmt.setString(4,objeto.getNmNivelCobranca());
			pstmt.setString(5,objeto.getNmDescricao());
			pstmt.setInt(6,objeto.getQtDiasAposVencimento());
			pstmt.setInt(7,objeto.getQtDiasEntreAvisoCobranca());
			pstmt.setInt(8,objeto.getLgCobrarJuros());
			pstmt.setFloat(9,objeto.getPrTaxaJuros());
			pstmt.setInt(10,objeto.getLgEncargosMora());
			pstmt.setFloat(11,objeto.getPrEncargosMora());
			pstmt.setInt(12,objeto.getStNivelCobranca());
			pstmt.setInt(13,objeto.getLgSuspenderCredito());
			pstmt.setInt(14,objeto.getLgCancelarCredito());
			pstmt.setString(15,objeto.getTxtNota());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelCobrancaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelCobrancaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NivelCobranca objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(NivelCobranca objeto, int cdNivelCobrancaOld, int cdCobrancaOld) {
		return update(objeto, cdNivelCobrancaOld, cdCobrancaOld, null);
	}

	public static int update(NivelCobranca objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(NivelCobranca objeto, int cdNivelCobrancaOld, int cdCobrancaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_nivel_cobranca SET cd_nivel_cobranca=?,"+
												      		   "cd_cobranca=?,"+
												      		   "id_nivel_cobranca=?,"+
												      		   "nm_nivel_cobranca=?,"+
												      		   "nm_descricao=?,"+
												      		   "qt_dias_apos_vencimento=?,"+
												      		   "qt_dias_entre_aviso_cobranca=?,"+
												      		   "lg_cobrar_juros=?,"+
												      		   "pr_taxa_juros=?,"+
												      		   "lg_encargos_mora=?,"+
												      		   "pr_encargos_mora=?,"+
												      		   "st_nivel_cobranca=?,"+
												      		   "lg_suspender_credito=?,"+
												      		   "lg_cancelar_credito=?,"+
												      		   "txt_nota=? WHERE cd_nivel_cobranca=? AND cd_cobranca=?");
			pstmt.setInt(1,objeto.getCdNivelCobranca());
			pstmt.setInt(2,objeto.getCdCobranca());
			pstmt.setString(3,objeto.getIdNivelCobranca());
			pstmt.setString(4,objeto.getNmNivelCobranca());
			pstmt.setString(5,objeto.getNmDescricao());
			pstmt.setInt(6,objeto.getQtDiasAposVencimento());
			pstmt.setInt(7,objeto.getQtDiasEntreAvisoCobranca());
			pstmt.setInt(8,objeto.getLgCobrarJuros());
			pstmt.setFloat(9,objeto.getPrTaxaJuros());
			pstmt.setInt(10,objeto.getLgEncargosMora());
			pstmt.setFloat(11,objeto.getPrEncargosMora());
			pstmt.setInt(12,objeto.getStNivelCobranca());
			pstmt.setInt(13,objeto.getLgSuspenderCredito());
			pstmt.setInt(14,objeto.getLgCancelarCredito());
			pstmt.setString(15,objeto.getTxtNota());
			pstmt.setInt(16, cdNivelCobrancaOld!=0 ? cdNivelCobrancaOld : objeto.getCdNivelCobranca());
			pstmt.setInt(17, cdCobrancaOld!=0 ? cdCobrancaOld : objeto.getCdCobranca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelCobrancaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelCobrancaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNivelCobranca, int cdCobranca) {
		return delete(cdNivelCobranca, cdCobranca, null);
	}

	public static int delete(int cdNivelCobranca, int cdCobranca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_nivel_cobranca WHERE cd_nivel_cobranca=? AND cd_cobranca=?");
			pstmt.setInt(1, cdNivelCobranca);
			pstmt.setInt(2, cdCobranca);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelCobrancaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelCobrancaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NivelCobranca get(int cdNivelCobranca, int cdCobranca) {
		return get(cdNivelCobranca, cdCobranca, null);
	}

	public static NivelCobranca get(int cdNivelCobranca, int cdCobranca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_nivel_cobranca WHERE cd_nivel_cobranca=? AND cd_cobranca=?");
			pstmt.setInt(1, cdNivelCobranca);
			pstmt.setInt(2, cdCobranca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NivelCobranca(rs.getInt("cd_nivel_cobranca"),
						rs.getInt("cd_cobranca"),
						rs.getString("id_nivel_cobranca"),
						rs.getString("nm_nivel_cobranca"),
						rs.getString("nm_descricao"),
						rs.getInt("qt_dias_apos_vencimento"),
						rs.getInt("qt_dias_entre_aviso_cobranca"),
						rs.getInt("lg_cobrar_juros"),
						rs.getFloat("pr_taxa_juros"),
						rs.getInt("lg_encargos_mora"),
						rs.getFloat("pr_encargos_mora"),
						rs.getInt("st_nivel_cobranca"),
						rs.getInt("lg_suspender_credito"),
						rs.getInt("lg_cancelar_credito"),
						rs.getString("txt_nota"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelCobrancaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelCobrancaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_nivel_cobranca");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelCobrancaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelCobrancaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_nivel_cobranca", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
