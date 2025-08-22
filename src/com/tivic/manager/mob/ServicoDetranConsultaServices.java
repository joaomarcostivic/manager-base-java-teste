package com.tivic.manager.mob;

import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.ServicoDetranConsultaFactory;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class ServicoDetranConsultaServices {
	
	public static final String CODIGO_RETORNO_SUCESSO = "0";
	
	public ServicoDetranObjeto consultarPlaca(String nrPlaca){
		return consultarPlaca(nrPlaca, null);
	}
	
	public ServicoDetranObjeto consultarPlaca(String nrPlaca, String uf){
		ServicoDetran servicoDetran = ServicoDetranConsultaFactory.gerarServico((uf != null ? uf : getSgEstadoOrgao()), ServicoDetranConsultaFactory.PLACA);
		return servicoDetran.executar(new AitDetranObject(nrPlaca));
	}
	
	public ServicoDetranObjeto consultarAutoBaseNacional(String idAit){
		return consultarAutoBaseNacional(idAit, null);
	}
	
	public ServicoDetranObjeto consultarAutoBaseNacional(String idAit, String uf){
		ServicoDetran servicoDetran = ServicoDetranConsultaFactory.gerarServico((uf != null ? uf : getSgEstadoOrgao()), ServicoDetranConsultaFactory.AUTO_BASE_NACIONAL);
		Ait ait = AitServices.getById(idAit);
		return servicoDetran.executar(new AitDetranObject(ait));
	}

	public ServicoDetranObjeto consultarAutoBaseEstadual(String idAit){
		return consultarAutoBaseEstadual(idAit, null);
	}
	
	public ServicoDetranObjeto consultarAutoBaseEstadual(String idAit, String uf){
		ServicoDetran servicoDetran = ServicoDetranConsultaFactory.gerarServico((uf != null ? uf : getSgEstadoOrgao()), ServicoDetranConsultaFactory.AUTO_BASE_ESTADUAL);
		Ait ait = AitServices.getById(idAit);
		return servicoDetran.executar(new AitDetranObject(ait));
	}

	public ServicoDetranObjeto consultarMovimentacoes(String nrPlaca, String idAit){
		return consultarMovimentacoes(nrPlaca, idAit, null);
	}
	
	public ServicoDetranObjeto consultarMovimentacoes(String nrPlaca, String idAit, String uf){
		ServicoDetran servicoDetran = ServicoDetranConsultaFactory.gerarServico((uf != null ? uf : getSgEstadoOrgao()), ServicoDetranConsultaFactory.MOVIMENTACOES);
		Ait ait = AitServices.getById(idAit);
		return servicoDetran.executar(new AitDetranObject(ait));
	}

	public ServicoDetranObjeto consultarRecursos(String nrPlaca, String idAit){
		return consultarRecursos(nrPlaca, idAit, null);
	}
	
	public ServicoDetranObjeto consultarRecursos(String nrPlaca, String idAit, String uf){
		ServicoDetran servicoDetran = ServicoDetranConsultaFactory.gerarServico((uf != null ? uf : getSgEstadoOrgao()), ServicoDetranConsultaFactory.RECURSOS);
		Ait ait = AitServices.getById(idAit);
		return servicoDetran.executar(new AitDetranObject(ait));
	}
	
	public ServicoDetranObjeto consultarPossuidorPlaca(String nrPlaca){
		return consultarPossuidorPlaca(nrPlaca, null);
	}
	
	public ServicoDetranObjeto consultarPossuidorPlaca(String nrPlaca, String uf){
		ServicoDetran servicoDetran = ServicoDetranConsultaFactory.gerarServico((uf != null ? uf : getSgEstadoOrgao()), ServicoDetranConsultaFactory.POSSUIDOR_PLACA);
		return servicoDetran.executar(new AitDetranObject(nrPlaca));
	}
	
	public ServicoDetranObjeto syncAutoBaseEstadual(String idAit, String nrPlaca){
        return syncAutoBaseEstadual(idAit, nrPlaca, null);
    }

    public ServicoDetranObjeto syncAutoBaseEstadual(String idAit, String nrPlaca, String uf){
        ServicoDetran servicoDetran = ServicoDetranConsultaFactory.gerarServico((uf != null ? uf : getSgEstadoOrgao()), ServicoDetranConsultaFactory.AUTO_BASE_ESTADUAL);
        Ait ait = new Ait();
        ait.setNrPlaca(nrPlaca);
        ait.setIdAit(idAit);
        return servicoDetran.executar(new AitDetranObject(ait));
    }
	
	
	private String getSgEstadoOrgao(){
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Cidade cidadeOrgao = CidadeDAO.get(orgao.getCdCidade());
		Estado estadoOrgao = EstadoDAO.get(cidadeOrgao.getCdEstado());
		return estadoOrgao.getSgEstado();
	}	
	
}
