package com.tivic.manager.mob.lotes.impressao.codigobarras;

import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.lotes.impressao.codigobarras.MG.GeraCodigoBarras;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class GerarCodigoBarrasFactory {

	public static final String MG = "MG";
	public static final String BA = "BA";
	public static final String SP = "SP";

	public IGerarCodigoBarras getStrategy() throws ValidacaoException {
		String sgEstado = EstadoDAO.get(CidadeDAO.get(OrgaoServices.getOrgaoUnico().getCdCidade()).getCdEstado())
				.getSgEstado();
		switch (sgEstado) {
		case MG:
		case BA:
			return new GeraCodigoBarras();
		default:
			throw new ValidacaoException("Código de barras não implementado para este estado.");
		}
	}
}
