package com.tivic.manager.wsdl.detran.ba;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.wsdl.detran.mg.AitSyncDTO;
import com.tivic.manager.wsdl.detran.mg.consultainfracoes.ConsultarInfracoesDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.PontuacaoDadosCondutorDTO;
import com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso.AlteraPrazoRecursoDTO;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ServicoDetranServicesBA implements ServicoDetranServices {

	@Override
	public List<ServicoDetranDTO> remessa(List<AitMovimento> aisMovimento) throws Exception {
		return new ArrayList<ServicoDetranDTO>();
	}

	@Override
	public List<ServicoDetranDTO> renainf(List<AitMovimento> aisMovimento) throws Exception {
		return new ArrayList<ServicoDetranDTO>();
	}

	@Override
	public Ait incluirAitSync(String idAit, String nrPlaca, int cdUsuario) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void searchDetran(Ait ait) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AitSyncDTO verificarAitSync(int cdAit) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ait atualizarAitSync(int cdAit) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PontuacaoDadosCondutorDTO consultarPontuacaoDadosCondutor(String documento, int tipoDocumento)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ait alterarDataLimiteRecurso(int cdAit, GregorianCalendar dataLimiteRecurso) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ait alterarPrazoRecurso(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void envioAutomativo(SearchCriterios searchCriterios, boolean lgNaoEnviado) throws Exception {

	}

	@Override
	public ConsultarInfracoesDadosRetorno consultarInfracoes(String documento, int tpDocumento, String dtInicial,
			String dtFinal) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ait alterarPrazoRecurso(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO, CustomConnection customConnection)
			throws Exception {
		// TODO Auto-generated method stub
		throw new Exception("Method not implemented!");
	}

	@Override
	public void alterarPrazoRecursoLoteImpressao(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) throws Exception {
		throw new Exception("Method not implemented!");
	}

	public ConsultarInfracoesDadosRetorno consultarInfrator(String documento, int tpDocumento, String dtInicial,
			String dtFinal) throws Exception {
		throw new Exception("Method not implemented!");
	}

	@Override
	public void alterarPrazoRecursoLoteAits(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) throws Exception {
		throw new Exception("Method not implemented!");		
	}

}
