package com.tivic.manager.mob.ait.aitpessoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;

import com.tivic.sol.connection.Conexao;

public class AitPessoaDAO {
	
	public static int insert(AitPessoa objeto, Connection connect) {
		try {
			int code = Conexao.getSequenceCode("mob_ait_pessoa", connect);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_ait_pessoa (cd_ait_pessoa, " +
															   "cd_agente, " +
															   "cd_ocorrencia, " +
															   "cd_talao, " +
															   "cd_usuario, " +
															   "cd_equipamento," +
															   "st_ait," +
															   "tp_pessoa," +
															   "vl_latitude," +
															   "vl_longitude," +
															   "nm_pessoa," +
															   "id_ait," +
															   "nr_ait," +
															   "nr_cpf_cnpj_pessoa," +
															   "txt_observacao," +
															   "ds_observacao," +
															   "ds_local_infracao," +
															   "ds_ponto_referencia," +
															   "dt_infracao," +
															   "dt_digitacao) " +
															   "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pstmt.setInt(1, code);
			if(objeto.getCdAgente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAgente());
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOcorrencia());
			if(objeto.getCdTalao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTalao());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdUsuario());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEquipamento());
			pstmt.setInt(7, objeto.getStAit());
			pstmt.setInt(8, objeto.getTpPessoa());
			pstmt.setDouble(9, objeto.getVlLatitude());
			pstmt.setDouble(10, objeto.getVlLongitude());
			pstmt.setString(11, objeto.getNmPessoa());
			pstmt.setString(12, objeto.getIdAit());
			pstmt.setInt(13, objeto.getNrAit());
			pstmt.setString(14, objeto.getNrCpfCnpjPessoa());
			pstmt.setString(15, objeto.getTxtObservacao());
			pstmt.setString(16, objeto.getDsObservacao());
			pstmt.setString(17, objeto.getDsLocalInfracao());
			pstmt.setString(18, objeto.getDsPontoReferencia());
			if(objeto.getDtInfracao() == 0)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19, new Timestamp(objeto.getDtInfracao()));
			if(objeto.getDtDigitacao() == 0)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20, new Timestamp(objeto.getDtDigitacao()));
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPessoaDAO.insert: " + e);
			return -1;
		} 
	}

	public static int update(AitPessoa objeto, Connection connect) {
		try {
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_ait_pessoa SET " +
															   "cd_ait_pessoa=?," +
															   "cd_agente=?, " +
															   "cd_ocorrencia=?, " +
															   "cd_talao=?, " +
															   "cd_usuario=?, " +
															   "cd_equipamento=?," +
															   "st_ait=?," +
															   "tp_pessoa=?," +
															   "vl_latitude=?," +
															   "vl_longitude=?," +
															   "nm_pessoa=?," +
															   "id_ait=?," +
															   "nr_ait=?," +
															   "nr_cpf_cnpj_pessoa=?," +
															   "txt_observacao=?," +
															   "ds_observacao=?," +
															   "ds_local_infracao=?," +
															   "ds_ponto_referencia=?," +
															   "dt_infracao=?," +
															   "dt_digitacao=? " +
															   "WHERE cd_ait_pessoa=?");
			pstmt.setInt(1, objeto.getCdAitPessoa());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAgente());
			if(objeto.getCdOcorrencia()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOcorrencia());
			if(objeto.getCdTalao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTalao());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdUsuario());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEquipamento());
			pstmt.setInt(7, objeto.getStAit());
			pstmt.setInt(8, objeto.getTpPessoa());
			pstmt.setDouble(9, objeto.getVlLatitude());
			pstmt.setDouble(10, objeto.getVlLongitude());
			pstmt.setString(11, objeto.getNmPessoa());
			pstmt.setString(12, objeto.getIdAit());
			pstmt.setInt(13, objeto.getNrAit());
			pstmt.setString(14, objeto.getNrCpfCnpjPessoa());
			pstmt.setString(15, objeto.getTxtObservacao());
			pstmt.setString(16, objeto.getDsObservacao());
			pstmt.setString(17, objeto.getDsLocalInfracao());
			pstmt.setString(18, objeto.getDsPontoReferencia());
			if(objeto.getDtInfracao() == 0)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19, new Timestamp(objeto.getDtInfracao()));
			if(objeto.getDtDigitacao() == 0)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20, new Timestamp(objeto.getDtDigitacao()));
			pstmt.setInt(21, objeto.getCdAitPessoa());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPessoaDAO.update: " + e);
			return -1;
		} 
	}
	
	public static AitPessoa get(int cdAitPessoa, Connection connect) {
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_pessoa WHERE " +
											 "cd_ait_pessoa=?");
			pstmt.setInt(1, cdAitPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				AitPessoa aitPessoa = new AitPessoa();
				aitPessoa.setCdAitPessoa(rs.getInt("cd_ait_pessoa"));
				aitPessoa.setCdAgente(rs.getInt("cd_agente"));
				aitPessoa.setCdOcorrencia(rs.getInt("cd_ocorrencia"));
				aitPessoa.setCdTalao(rs.getInt("cd_talao"));
				aitPessoa.setCdUsuario(rs.getInt("cd_usuario"));
				aitPessoa.setCdEquipamento(rs.getInt("cd_equipamento"));
				aitPessoa.setStAit(rs.getInt("st_ait"));
				aitPessoa.setTpPessoa(rs.getInt("tp_pessoa"));
				aitPessoa.setVlLatitude(rs.getInt("vl_latitude"));
				aitPessoa.setVlLongitude(rs.getInt("vl_longitude"));
				aitPessoa.setNmPessoa(rs.getString("nm_pessoa"));
				aitPessoa.setIdAit(rs.getString("id_ait"));
				aitPessoa.setNrAit(rs.getInt("nr_ait"));
				aitPessoa.setNrCpfCnpjPessoa(rs.getString("nr_cpf_cnpj_pessoa"));
				aitPessoa.setTxtObservacao(rs.getString("txt_observacao"));
				aitPessoa.setDsObservacao(rs.getString("ds_observacao"));
				aitPessoa.setDsLocalInfracao(rs.getString("ds_local_infracao"));
				aitPessoa.setDsPontoReferencia(rs.getString("ds_ponto_referencia"));
				aitPessoa.setDtInfracao(rs.getTimestamp("dt_infracao")==null? null : rs.getTimestamp("dt_infracao").getTime());
				aitPessoa.setDtDigitacao(rs.getTimestamp("dt_digitacao")==null? null : rs.getTimestamp("dt_digitacao").getTime());
				return aitPessoa;
			} else {
				return null;
			} 
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitPessoaDAO.get: " + e);
			return null;
		} 
	}
}
