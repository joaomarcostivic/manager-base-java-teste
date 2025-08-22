package com.tivic.manager.srh;

import java.sql.*;
import sol.dao.*;

import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class VinculoEmpregaticioDAO{

	public static int insert(VinculoEmpregaticio objeto) {
		return insert(objeto, null);
	}

	public static int insert(VinculoEmpregaticio objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("srh_vinculo_empregaticio", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdVinculoEmpregaticio(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_vinculo_empregaticio (cd_vinculo_empregaticio,"+
			                                  "nm_vinculo_empregaticio,"+
			                                  "id_vinculo_empregaticio,"+
			                                  "tp_regime_juridico,"+
			                                  "nr_sefip,"+
			                                  "nr_rais,"+
			                                  "lg_decimo_mensal,"+
			                                  "lg_ferias_mensal,"+
			                                  "lg_terco_ferias_mensal,"+
			                                  "lg_terco_ferias,"+
			                                  "lg_ferias_menor_ano,"+
			                                  "lg_caged,"+
			                                  "lg_rais,"+
			                                  "lg_sefip,"+
			                                  "lg_nao_gerar_vencimento,"+
			                                  "cd_tipo_desligamento,"+
			                                  "cd_empresa,"+
			                                  "cd_evento_financeiro) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmVinculoEmpregaticio());
			pstmt.setString(3,objeto.getIdVinculoEmpregaticio());
			pstmt.setInt(4,objeto.getTpRegimeJuridico());
			pstmt.setString(5,objeto.getNrSefip());
			pstmt.setString(6,objeto.getNrRais());
			pstmt.setInt(7,objeto.getLgDecimoMensal());
			pstmt.setInt(8,objeto.getLgFeriasMensal());
			pstmt.setInt(9,objeto.getLgTercoFeriasMensal());
			pstmt.setInt(10,objeto.getLgTercoFerias());
			pstmt.setInt(11,objeto.getLgFeriasMenorAno());
			pstmt.setInt(12,objeto.getLgCaged());
			pstmt.setInt(13,objeto.getLgRais());
			pstmt.setInt(14,objeto.getLgSefip());
			pstmt.setInt(15,objeto.getLgNaoGerarVencimento());
			if(objeto.getCdTipoDesligamento()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTipoDesligamento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdEmpresa());
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdEventoFinanceiro());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VinculoEmpregaticioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoEmpregaticioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(VinculoEmpregaticio objeto) {
		return update(objeto, 0, null);
	}

	public static int update(VinculoEmpregaticio objeto, int cdVinculoEmpregaticioOld) {
		return update(objeto, cdVinculoEmpregaticioOld, null);
	}

	public static int update(VinculoEmpregaticio objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(VinculoEmpregaticio objeto, int cdVinculoEmpregaticioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_vinculo_empregaticio SET cd_vinculo_empregaticio=?,"+
												      		   "nm_vinculo_empregaticio=?,"+
												      		   "id_vinculo_empregaticio=?,"+
												      		   "tp_regime_juridico=?,"+
												      		   "nr_sefip=?,"+
												      		   "nr_rais=?,"+
												      		   "lg_decimo_mensal=?,"+
												      		   "lg_ferias_mensal=?,"+
												      		   "lg_terco_ferias_mensal=?,"+
												      		   "lg_terco_ferias=?,"+
												      		   "lg_ferias_menor_ano=?,"+
												      		   "lg_caged=?,"+
												      		   "lg_rais=?,"+
												      		   "lg_sefip=?,"+
												      		   "lg_nao_gerar_vencimento=?,"+
												      		   "cd_tipo_desligamento=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_evento_financeiro=? WHERE cd_vinculo_empregaticio=?");
			pstmt.setInt(1,objeto.getCdVinculoEmpregaticio());
			pstmt.setString(2,objeto.getNmVinculoEmpregaticio());
			pstmt.setString(3,objeto.getIdVinculoEmpregaticio());
			pstmt.setInt(4,objeto.getTpRegimeJuridico());
			pstmt.setString(5,objeto.getNrSefip());
			pstmt.setString(6,objeto.getNrRais());
			pstmt.setInt(7,objeto.getLgDecimoMensal());
			pstmt.setInt(8,objeto.getLgFeriasMensal());
			pstmt.setInt(9,objeto.getLgTercoFeriasMensal());
			pstmt.setInt(10,objeto.getLgTercoFerias());
			pstmt.setInt(11,objeto.getLgFeriasMenorAno());
			pstmt.setInt(12,objeto.getLgCaged());
			pstmt.setInt(13,objeto.getLgRais());
			pstmt.setInt(14,objeto.getLgSefip());
			pstmt.setInt(15,objeto.getLgNaoGerarVencimento());
			if(objeto.getCdTipoDesligamento()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTipoDesligamento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdEmpresa());
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdEventoFinanceiro());
			pstmt.setInt(19, cdVinculoEmpregaticioOld!=0 ? cdVinculoEmpregaticioOld : objeto.getCdVinculoEmpregaticio());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VinculoEmpregaticioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoEmpregaticioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVinculoEmpregaticio) {
		return delete(cdVinculoEmpregaticio, null);
	}

	public static int delete(int cdVinculoEmpregaticio, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_vinculo_empregaticio WHERE cd_vinculo_empregaticio=?");
			pstmt.setInt(1, cdVinculoEmpregaticio);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VinculoEmpregaticioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoEmpregaticioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static VinculoEmpregaticio get(int cdVinculoEmpregaticio) {
		return get(cdVinculoEmpregaticio, null);
	}

	public static VinculoEmpregaticio get(int cdVinculoEmpregaticio, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_vinculo_empregaticio WHERE cd_vinculo_empregaticio=?");
			pstmt.setInt(1, cdVinculoEmpregaticio);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new VinculoEmpregaticio(rs.getInt("cd_vinculo_empregaticio"),
						rs.getString("nm_vinculo_empregaticio"),
						rs.getString("id_vinculo_empregaticio"),
						rs.getInt("tp_regime_juridico"),
						rs.getString("nr_sefip"),
						rs.getString("nr_rais"),
						rs.getInt("lg_decimo_mensal"),
						rs.getInt("lg_ferias_mensal"),
						rs.getInt("lg_terco_ferias_mensal"),
						rs.getInt("lg_terco_ferias"),
						rs.getInt("lg_ferias_menor_ano"),
						rs.getInt("lg_caged"),
						rs.getInt("lg_rais"),
						rs.getInt("lg_sefip"),
						rs.getInt("lg_nao_gerar_vencimento"),
						rs.getInt("cd_tipo_desligamento"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_evento_financeiro"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VinculoEmpregaticioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoEmpregaticioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_vinculo_empregaticio");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VinculoEmpregaticioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VinculoEmpregaticioDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_vinculo_empregaticio", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
