package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ProgramaFaturaDAO{

	public static int insert(ProgramaFatura objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProgramaFatura objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_programa_fatura", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProgramaFatura(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_programa_fatura (cd_programa_fatura,"+
			                                  "id_programa_fatura,"+
			                                  "st_programa_fatura,"+
			                                  "lg_padrao,"+
			                                  "nm_programa_fatura,"+
			                                  "txt_descricao,"+
			                                  "lg_apos_entrega,"+
			                                  "lg_proximo_dia_util,"+
			                                  "nr_dia_fixo,"+
			                                  "qt_dias_carencia,"+
			                                  "pr_desconto,"+
			                                  "qt_suspender_apos,"+
			                                  "lg_periodo,"+
			                                  "lg_cobrar_juros,"+
			                                  "pr_cobrar_juros,"+
			                                  "lg_protestar,"+
			                                  "qt_dias_protesto,"+
			                                  "txt_nota,"+
			                                  "nr_inicio_periodo,"+
			                                  "nr_final_periodo,"+
			                                  "nr_vencimento_periodo,"+
			                                  "lg_na_venda,"+
			                                  "lg_vencimento_fixo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdProgramaFatura());
			pstmt.setInt(3,objeto.getStProgramaFatura());
			pstmt.setInt(4,objeto.getLgPadrao());
			pstmt.setString(5,objeto.getNmProgramaFatura());
			pstmt.setString(6,objeto.getTxtDescricao());
			pstmt.setInt(7,objeto.getLgAposEntrega());
			pstmt.setInt(8,objeto.getLgProximoDiaUtil());
			pstmt.setInt(9,objeto.getNrDiaFixo());
			pstmt.setInt(10,objeto.getQtDiasCarencia());
			pstmt.setFloat(11,objeto.getPrDesconto());
			pstmt.setInt(12,objeto.getQtSuspenderApos());
			pstmt.setInt(13,objeto.getLgPeriodo());
			pstmt.setInt(14,objeto.getLgCobrarJuros());
			pstmt.setFloat(15,objeto.getPrCobrarJuros());
			pstmt.setInt(16,objeto.getLgProtestar());
			pstmt.setInt(17,objeto.getQtDiasProtesto());
			pstmt.setString(18,objeto.getTxtNota());
			pstmt.setInt(19,objeto.getNrInicioPeriodo());
			pstmt.setInt(20,objeto.getNrFinalPeriodo());
			pstmt.setInt(21,objeto.getNrVencimentoPeriodo());
			pstmt.setInt(22,objeto.getLgNaVenda());
			pstmt.setInt(23,objeto.getLgVencimentoFixo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProgramaFatura objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ProgramaFatura objeto, int cdProgramaFaturaOld) {
		return update(objeto, cdProgramaFaturaOld, null);
	}

	public static int update(ProgramaFatura objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ProgramaFatura objeto, int cdProgramaFaturaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_programa_fatura SET cd_programa_fatura=?,"+
												      		   "id_programa_fatura=?,"+
												      		   "st_programa_fatura=?,"+
												      		   "lg_padrao=?,"+
												      		   "nm_programa_fatura=?,"+
												      		   "txt_descricao=?,"+
												      		   "lg_apos_entrega=?,"+
												      		   "lg_proximo_dia_util=?,"+
												      		   "nr_dia_fixo=?,"+
												      		   "qt_dias_carencia=?,"+
												      		   "pr_desconto=?,"+
												      		   "qt_suspender_apos=?,"+
												      		   "lg_periodo=?,"+
												      		   "lg_cobrar_juros=?,"+
												      		   "pr_cobrar_juros=?,"+
												      		   "lg_protestar=?,"+
												      		   "qt_dias_protesto=?,"+
												      		   "txt_nota=?,"+
												      		   "nr_inicio_periodo=?,"+
												      		   "nr_final_periodo=?,"+
												      		   "nr_vencimento_periodo=?,"+
												      		   "lg_na_venda=?,"+
												      		   "lg_vencimento_fixo=? WHERE cd_programa_fatura=?");
			pstmt.setInt(1,objeto.getCdProgramaFatura());
			pstmt.setString(2,objeto.getIdProgramaFatura());
			pstmt.setInt(3,objeto.getStProgramaFatura());
			pstmt.setInt(4,objeto.getLgPadrao());
			pstmt.setString(5,objeto.getNmProgramaFatura());
			pstmt.setString(6,objeto.getTxtDescricao());
			pstmt.setInt(7,objeto.getLgAposEntrega());
			pstmt.setInt(8,objeto.getLgProximoDiaUtil());
			pstmt.setInt(9,objeto.getNrDiaFixo());
			pstmt.setInt(10,objeto.getQtDiasCarencia());
			pstmt.setFloat(11,objeto.getPrDesconto());
			pstmt.setInt(12,objeto.getQtSuspenderApos());
			pstmt.setInt(13,objeto.getLgPeriodo());
			pstmt.setInt(14,objeto.getLgCobrarJuros());
			pstmt.setFloat(15,objeto.getPrCobrarJuros());
			pstmt.setInt(16,objeto.getLgProtestar());
			pstmt.setInt(17,objeto.getQtDiasProtesto());
			pstmt.setString(18,objeto.getTxtNota());
			pstmt.setInt(19,objeto.getNrInicioPeriodo());
			pstmt.setInt(20,objeto.getNrFinalPeriodo());
			pstmt.setInt(21,objeto.getNrVencimentoPeriodo());
			pstmt.setInt(22,objeto.getLgNaVenda());
			pstmt.setInt(23,objeto.getLgVencimentoFixo());
			pstmt.setInt(24, cdProgramaFaturaOld!=0 ? cdProgramaFaturaOld : objeto.getCdProgramaFatura());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProgramaFatura) {
		return delete(cdProgramaFatura, null);
	}

	public static int delete(int cdProgramaFatura, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_programa_fatura WHERE cd_programa_fatura=?");
			pstmt.setInt(1, cdProgramaFatura);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProgramaFatura get(int cdProgramaFatura) {
		return get(cdProgramaFatura, null);
	}

	public static ProgramaFatura get(int cdProgramaFatura, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_programa_fatura WHERE cd_programa_fatura=?");
			pstmt.setInt(1, cdProgramaFatura);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProgramaFatura(rs.getInt("cd_programa_fatura"),
						rs.getString("id_programa_fatura"),
						rs.getInt("st_programa_fatura"),
						rs.getInt("lg_padrao"),
						rs.getString("nm_programa_fatura"),
						rs.getString("txt_descricao"),
						rs.getInt("lg_apos_entrega"),
						rs.getInt("lg_proximo_dia_util"),
						rs.getInt("nr_dia_fixo"),
						rs.getInt("qt_dias_carencia"),
						rs.getFloat("pr_desconto"),
						rs.getInt("qt_suspender_apos"),
						rs.getInt("lg_periodo"),
						rs.getInt("lg_cobrar_juros"),
						rs.getFloat("pr_cobrar_juros"),
						rs.getInt("lg_protestar"),
						rs.getInt("qt_dias_protesto"),
						rs.getString("txt_nota"),
						rs.getInt("nr_inicio_periodo"),
						rs.getInt("nr_final_periodo"),
						rs.getInt("nr_vencimento_periodo"),
						rs.getInt("lg_na_venda"),
						rs.getInt("lg_vencimento_fixo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_programa_fatura");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProgramaFaturaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_programa_fatura", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
