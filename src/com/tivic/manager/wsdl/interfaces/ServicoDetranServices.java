package com.tivic.manager.wsdl.interfaces;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.wsdl.detran.mg.AitSyncDTO;
import com.tivic.manager.wsdl.detran.mg.consultainfracoes.ConsultarInfracoesDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.PontuacaoDadosCondutorDTO;
import com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso.AlteraPrazoRecursoDTO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public interface ServicoDetranServices {
	public List<ServicoDetranDTO> remessa(List<AitMovimento> aisMovimento) throws Exception;
	public List<ServicoDetranDTO> renainf(List<AitMovimento> aisMovimento) throws Exception;
	public Ait incluirAitSync(String idAit, String nrPlaca, int cdUsuario) throws Exception;
	public void searchDetran(Ait ait) throws Exception;
	public AitSyncDTO verificarAitSync(int cdAit) throws Exception;
	public Ait atualizarAitSync(int cdAit) throws Exception;
	public PontuacaoDadosCondutorDTO consultarPontuacaoDadosCondutor(String documento, int tipoDocumento) throws Exception;
	public ConsultarInfracoesDadosRetorno consultarInfracoes(String documento, int tpDocumento, String dtInicial, String dtFinal) throws Exception;
	public ConsultarInfracoesDadosRetorno consultarInfrator(String documento, int tpDocumento, String dtInicial, String dtFinal) throws Exception;
	public Ait alterarDataLimiteRecurso(int cdAit, GregorianCalendar dataLimiteRecurso) throws Exception;
	public Ait alterarPrazoRecurso(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) throws Exception;
	public void envioAutomativo(SearchCriterios searchCriterios, boolean lgNaoEnviado) throws Exception;
	public Ait alterarPrazoRecurso(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO, CustomConnection customConnection) throws Exception;
	public void alterarPrazoRecursoLoteImpressao(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) throws Exception;
	public void alterarPrazoRecursoLoteAits(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) throws Exception;
}
