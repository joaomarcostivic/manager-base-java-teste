package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;

import sol.dao.ResultSetMap;

public class AitReportValidators {
	
	public static void validar(int cdAit, int tpStatusPretendido, Connection connect) throws AitReportErrorException {
		Ait ait = AitDAO.get(cdAit, connect);
		verificarNomeProprietario(ait, tpStatusPretendido);
		verificarSituacaoAit(ait, tpStatusPretendido);
		verificarNrControle(ait, tpStatusPretendido);
		verificarCpfCnpjProprietario(ait, tpStatusPretendido);
		verificarModeloVeiculo(ait, tpStatusPretendido, connect);
		verificarEspecieVeiculo(ait, tpStatusPretendido, connect);
		verificarNrRenavan(ait, tpStatusPretendido);
	}
	
	private static void verificarNomeProprietario(Ait ait, int tpStatusPretendido) throws AitReportErrorException{
		if (ait.getNmProprietario() == null || ait.getNmProprietario().trim().equals("")){
			AitReportInconsistencia.salvarInconsistencia(InconsistenciaServices.TP_SEM_NOME_PROPRIETARIO, tpStatusPretendido, ait);
			throw new AitReportErrorException("O AIT não possui o nome do proprietário.");
		}
	}
	
	private static void verificarSituacaoAit(Ait ait, int tpStatusPretendido) throws AitReportErrorException {
		if (ait.getStAit() != AitServices.ST_CONFIRMADO){
			AitReportInconsistencia.salvarInconsistencia(InconsistenciaServices.TP_SITUACAO_AIT_NAO_CONFIRMADA, tpStatusPretendido, ait);
			throw new  AitReportErrorException("O AIT não esta com a situação confirmada.");
		}
	}
	
	private static void verificarNrControle(Ait ait, int tpStatusPretendido) throws AitReportErrorException{
		if (ait.getNrControle() == null || ait.getNrControle().equals("0") || ait.getNrControle().trim().equals("")){
			AitReportInconsistencia.salvarInconsistencia(InconsistenciaServices.TP_SEM_NUMERO_CONTROLE, tpStatusPretendido, ait);
			throw new  AitReportErrorException("O AIT não possui número de controle.");
		}
	}
	
	private static void verificarCpfCnpjProprietario(Ait ait, int tpStatusPretendido) throws AitReportErrorException{
		if (ait.getNrCpfCnpjProprietario() == null ||  ait.getNrCpfCnpjProprietario().trim().equals("")){
			AitReportInconsistencia.salvarInconsistencia(InconsistenciaServices.TP_SEM_CPF_CNPJ_PROPRIETARIO, tpStatusPretendido, ait);
			throw new  AitReportErrorException("O AIT não possui o CPF/CNPJ do proprietário.");
		}
	}
	
	private static void verificarModeloVeiculo(Ait ait, int tpStatusPretendido, Connection connect) throws AitReportErrorException{
		String nmModelo = getNmModelo(ait.getCdAit(), connect);
		if (nmModelo == null){
			AitReportInconsistencia.salvarInconsistencia(InconsistenciaServices.TP_SEM_MODELO_VEICULO, tpStatusPretendido, ait);
			throw new  AitReportErrorException("Modelo do veiculo não encontrado.");
		}
	}
	
	private static void verificarEspecieVeiculo(Ait ait, int tpStatusPretendido, Connection connect) throws AitReportErrorException{
		String dsEspecie = getDsEspecie(ait.getCdAit(), connect);
		if (dsEspecie == null){
			AitReportInconsistencia.salvarInconsistencia(InconsistenciaServices.TP_SEM_ESPECIE_VEICULO, tpStatusPretendido, ait);
			throw new  AitReportErrorException("A espécie do veículo não foi encontrada.");
		}
	}
	
	private static void verificarNrRenavan(Ait ait, int tpStatusPretendido) throws AitReportErrorException{
		if (ait.getNrRenavan() == null || ait.getNrRenavan().trim().equals("")){
			AitReportInconsistencia.salvarInconsistencia(InconsistenciaServices.TP_SEM_NR_RENAVAN, tpStatusPretendido, ait);
			throw new  AitReportErrorException("Numero de renavan não encontrado.");
		}
	}
	
	private static String getNmModelo (int cdAit, Connection connect){
		String nmModelo = null;
		try {
			String sql =  "SELECT A.cd_ait, A.cd_marca_veiculo, B.cd_marca, B.nm_modelo " 
						+ "FROM mob_ait A "
						+ "INNER JOIN fta_marca_modelo B on (A.cd_marca_veiculo = B.cd_marca) "
						+ "WHERE cd_ait = ? ";
			PreparedStatement ps;
			ps = connect.prepareStatement(sql);
			ps.setInt(1, cdAit);
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			if (rsm.next())
				nmModelo = rsm.getString("nm_modelo");
			return nmModelo;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String getDsEspecie (int cdAit, Connection connect){
		String nmDsEspecie = null;
		try {
			String sql =  "SELECT A.cd_ait, A.cd_especie_veiculo, B.cd_especie, B.DS_ESPECIE " 
						+ "FROM mob_ait A "
						+ "INNER JOIN fta_especie_veiculo B on (A.cd_especie_veiculo = B.cd_especie) "
						+ "WHERE cd_ait = ? ";
			PreparedStatement ps;
			ps = connect.prepareStatement(sql);
			ps.setInt(1, cdAit);
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			if (rsm.next())
				nmDsEspecie = rsm.getString("DS_ESPECIE");
			return nmDsEspecie;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}