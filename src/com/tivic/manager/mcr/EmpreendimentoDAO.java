package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class EmpreendimentoDAO{

	public static int insert(Empreendimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Empreendimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("mcr_empreendimento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_empreendimento (cd_empreendimento,"+
			                                  "cd_pessoa,"+
			                                  "tp_negocio,"+
			                                  "tp_local,"+
			                                  "tp_ponto_comercial,"+
			                                  "dt_contrato,"+
			                                  "nm_outro_ponto,"+
			                                  "nr_dias_atividade,"+
			                                  "nm_local_atividade,"+
			                                  "tp_atividade,"+
			                                  "tp_estruturacao,"+
			                                  "lg_empresa_familiar,"+
			                                  "nr_tempo_experiencia,"+
			                                  "nr_tempo_independencia,"+
			                                  "txt_aprendizado,"+
			                                  "txt_historico,"+
			                                  "vl_muito_bom,"+
			                                  "vl_bom,"+
			                                  "vl_regular,"+
			                                  "vl_fraco,"+
			                                  "txt_concorrencia,"+
			                                  "txt_fidelidade,"+
			                                  "txt_capacidade_instalada,"+
			                                  "dt_informacao_bem,"+
			                                  "nr_dia_prestacao,"+
			                                  "txt_observacao,"+
			                                  "id_empreendimento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getTpNegocio());
			pstmt.setInt(4,objeto.getTpLocal());
			pstmt.setInt(5,objeto.getTpPontoComercial());
			if(objeto.getDtContrato()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtContrato().getTimeInMillis()));
			pstmt.setString(7,objeto.getNmOutroPonto());
			pstmt.setInt(8,objeto.getNrDiasAtividade());
			pstmt.setString(9,objeto.getNmLocalAtividade());
			pstmt.setInt(10,objeto.getTpAtividade());
			pstmt.setInt(11,objeto.getTpEstruturacao());
			pstmt.setInt(12,objeto.getLgEmpresaFamiliar());
			pstmt.setInt(13,objeto.getNrTempoExperiencia());
			pstmt.setInt(14,objeto.getNrTempoIndependencia());
			pstmt.setString(15,objeto.getTxtAprendizado());
			pstmt.setString(16,objeto.getTxtHistorico());
			pstmt.setInt(17,objeto.getVlMuitoBom());
			pstmt.setInt(18,objeto.getVlBom());
			pstmt.setInt(19,objeto.getVlRegular());
			pstmt.setInt(20,objeto.getVlFraco());
			pstmt.setString(21,objeto.getTxtConcorrencia());
			pstmt.setString(22,objeto.getTxtFidelidade());
			pstmt.setString(23,objeto.getTxtCapacidadeInstalada());
			if(objeto.getDtInformacaoBem()==null)
				pstmt.setNull(24, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(24,new Timestamp(objeto.getDtInformacaoBem().getTimeInMillis()));
			pstmt.setInt(25,objeto.getNrDiaPrestacao());
			pstmt.setString(26,objeto.getTxtObservacao());
			pstmt.setString(27,objeto.getIdEmpreendimento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Empreendimento objeto) {
		return update(objeto, null);
	}

	public static int update(Empreendimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_empreendimento SET cd_pessoa=?,"+
			                                  "tp_negocio=?,"+
			                                  "tp_local=?,"+
			                                  "tp_ponto_comercial=?,"+
			                                  "dt_contrato=?,"+
			                                  "nm_outro_ponto=?,"+
			                                  "nr_dias_atividade=?,"+
			                                  "nm_local_atividade=?,"+
			                                  "tp_atividade=?,"+
			                                  "tp_estruturacao=?,"+
			                                  "lg_empresa_familiar=?,"+
			                                  "nr_tempo_experiencia=?,"+
			                                  "nr_tempo_independencia=?,"+
			                                  "txt_aprendizado=?,"+
			                                  "txt_historico=?,"+
			                                  "vl_muito_bom=?,"+
			                                  "vl_bom=?,"+
			                                  "vl_regular=?,"+
			                                  "vl_fraco=?,"+
			                                  "txt_concorrencia=?,"+
			                                  "txt_fidelidade=?,"+
			                                  "txt_capacidade_instalada=?,"+
			                                  "dt_informacao_bem=?,"+
			                                  "nr_dia_prestacao=?,"+
			                                  "txt_observacao=?,"+
			                                  "id_empreendimento=? WHERE cd_empreendimento=?");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getTpNegocio());
			pstmt.setInt(3,objeto.getTpLocal());
			pstmt.setInt(4,objeto.getTpPontoComercial());
			if(objeto.getDtContrato()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtContrato().getTimeInMillis()));
			pstmt.setString(6,objeto.getNmOutroPonto());
			pstmt.setInt(7,objeto.getNrDiasAtividade());
			pstmt.setString(8,objeto.getNmLocalAtividade());
			pstmt.setInt(9,objeto.getTpAtividade());
			pstmt.setInt(10,objeto.getTpEstruturacao());
			pstmt.setInt(11,objeto.getLgEmpresaFamiliar());
			pstmt.setInt(12,objeto.getNrTempoExperiencia());
			pstmt.setInt(13,objeto.getNrTempoIndependencia());
			pstmt.setString(14,objeto.getTxtAprendizado());
			pstmt.setString(15,objeto.getTxtHistorico());
			pstmt.setInt(16,objeto.getVlMuitoBom());
			pstmt.setInt(17,objeto.getVlBom());
			pstmt.setInt(18,objeto.getVlRegular());
			pstmt.setInt(19,objeto.getVlFraco());
			pstmt.setString(20,objeto.getTxtConcorrencia());
			pstmt.setString(21,objeto.getTxtFidelidade());
			pstmt.setString(22,objeto.getTxtCapacidadeInstalada());
			if(objeto.getDtInformacaoBem()==null)
				pstmt.setNull(23, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(23,new Timestamp(objeto.getDtInformacaoBem().getTimeInMillis()));
			pstmt.setInt(24,objeto.getNrDiaPrestacao());
			pstmt.setString(25,objeto.getTxtObservacao());
			pstmt.setString(26,objeto.getIdEmpreendimento());
			pstmt.setInt(27,objeto.getCdEmpreendimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpreendimento) {
		return delete(cdEmpreendimento, null);
	}

	public static int delete(int cdEmpreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_empreendimento WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Empreendimento get(int cdEmpreendimento) {
		return get(cdEmpreendimento, null);
	}

	public static Empreendimento get(int cdEmpreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Empreendimento(rs.getInt("cd_empreendimento"),
						rs.getInt("cd_pessoa"),
						rs.getInt("tp_negocio"),
						rs.getInt("tp_local"),
						rs.getInt("tp_ponto_comercial"),
						(rs.getTimestamp("dt_contrato")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_contrato").getTime()),
						rs.getString("nm_outro_ponto"),
						rs.getInt("nr_dias_atividade"),
						rs.getString("nm_local_atividade"),
						rs.getInt("tp_atividade"),
						rs.getInt("tp_estruturacao"),
						rs.getInt("lg_empresa_familiar"),
						rs.getInt("nr_tempo_experiencia"),
						rs.getInt("nr_tempo_independencia"),
						rs.getString("txt_aprendizado"),
						rs.getString("txt_historico"),
						rs.getInt("vl_muito_bom"),
						rs.getInt("vl_bom"),
						rs.getInt("vl_regular"),
						rs.getInt("vl_fraco"),
						rs.getString("txt_concorrencia"),
						rs.getString("txt_fidelidade"),
						rs.getString("txt_capacidade_instalada"),
						(rs.getTimestamp("dt_informacao_bem")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_informacao_bem").getTime()),
						rs.getInt("nr_dia_prestacao"),
						rs.getString("txt_observacao"),
						rs.getString("id_empreendimento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_empreendimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EmpreendimentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM mcr_empreendimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
