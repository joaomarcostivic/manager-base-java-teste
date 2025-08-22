package com.tivic.manager.mob.lote.impressao.viaunica.nic;

import java.util.Arrays;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.detran.mg.recursonic.IncluirNIC;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class GeradorNic {
	private AitRepository aitRepository; 
	private AitMovimentoRepository aitMovimentoRepository;
	private Ait ait;
	private ServicoDetranServices servicoDetranServices;
	private ManagerLog managerLog;

	public GeradorNic(CustomConnection customConnection) throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	public void generate(Ait ait, int cdUsuario, CustomConnection customConnection) throws Exception {
		managerLog.info("Gerando Movimento NIC AIT gerador: ", ait.getIdAit());
		this.ait = new AitNicBuilder(ait, cdUsuario, customConnection).build();
		aitRepository.insert(this.ait, customConnection);
		new ListMovimentosNicBuilder().gerarMovimentos(this.ait, cdUsuario, customConnection);
		this.managerLog.info("Movimento NIC gerado para o AIT: ", this.ait.getIdAit());
		enviosNic(customConnection);
	}
	
	private void enviosNic(CustomConnection customConnection) throws Exception {
		AitDetranObject aitDetranObject = new AitDetranObject(this.ait, getMovimentoEnvio(TipoStatusEnum.NIC_ENVIADA.getKey(), customConnection));
		ServicoDetranObjeto servicoDetranObjeto = new IncluirNIC().executar(aitDetranObject);
		if(servicoDetranObjeto.getAit().getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.REGISTRADO.getKey())
			envioNai(customConnection);
	}
	
	private void envioNai(CustomConnection customConnection) throws Exception {
		List<AitMovimento> aitMovimentoList = Arrays.asList(getMovimentoEnvio(TipoStatusEnum.NAI_ENVIADO.getKey(), customConnection));
		this.servicoDetranServices.remessa(aitMovimentoList);
	}
	
	private AitMovimento getMovimentoEnvio(int tpStatus, CustomConnection customConnection) throws Exception {
		AitMovimento aitMovimento = aitMovimentoRepository.getByStatus(this.ait.getCdAit(), tpStatus, customConnection);
		return aitMovimento;
	}

}
