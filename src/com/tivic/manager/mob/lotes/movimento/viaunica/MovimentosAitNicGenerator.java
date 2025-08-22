package com.tivic.manager.mob.lotes.movimento.viaunica;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.util.DateUtil;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class MovimentosAitNicGenerator {
	private AitMovimentoRepository aitMovimentoRepository;
	private ManagerLog managerLog;
	
	public MovimentosAitNicGenerator() throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}
	
	public void gerarMovimentos(Ait ait, int cdUsuario, CustomConnection customConnection) throws Exception {
		gerarMovimentoNIC(ait, cdUsuario, customConnection);		
		gerarMovimentoNai(ait, cdUsuario, customConnection);
		customConnection.finishConnection();
	}
	
	private void gerarMovimentoNIC(Ait ait, int cdUsuario, CustomConnection customConnection) throws Exception {
		this.managerLog.info("Gerando Movimento NIC AIT: ", ait.getIdAit());
		AitMovimento aitMovimento = new AitMovimentoBuilder()
				.setCdAit(ait.getCdAit())
				.setCdOcorrencia(ait.getCdOcorrencia())
				.setCdUsuario(cdUsuario)
				.setDsObservacao(ait.getDsObservacao())
				.setDtDigitacao(DateUtil.today())
				.setTpStatus(TipoStatusEnum.NIC_ENVIADA.getKey())
				.setDtMovimento(DateUtil.today())
				.build();
			this.aitMovimentoRepository.insert(aitMovimento, customConnection);
	}	
	
	private void gerarMovimentoNai(Ait ait, int cdUsuario, CustomConnection customConnection) throws Exception {
		this.managerLog.info("Gerando Movimento NAI da NIC: ", ait.getIdAit());
		AitMovimento aitMovimento = new AitMovimentoBuilder()
				.setCdAit(ait.getCdAit())
				.setCdOcorrencia(ait.getCdOcorrencia())
				.setCdUsuario(cdUsuario)
				.setDsObservacao(ait.getDsObservacao())
				.setDtDigitacao(DateUtil.today())
				.setDtMovimento()
				.setTpStatus(TipoStatusEnum.NAI_ENVIADO.getKey())
				.setDtMovimento(DateUtil.today())
				.build();
			this.aitMovimentoRepository.insert(aitMovimento, customConnection);
	}
	
	public void gerarMovimentoRegistro(Ait ait, AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		this.managerLog.info("Gerando Movimento Registro de Infração da NIC AIT: ", ait.getIdAit());
		GregorianCalendar dtMovimentoNovo = (GregorianCalendar) aitMovimento.getDtMovimento().clone();
		dtMovimentoNovo.add(Calendar.MINUTE, -1);		
		AitMovimento aitMovimentoNovo = new AitMovimentoBuilder()
				.setCdAit(ait.getCdAit())
				.setCdOcorrencia(ait.getCdOcorrencia())
				.setCdUsuario(aitMovimento.getCdUsuario())
				.setDsObservacao(ait.getDsObservacao())
				.setDtDigitacao(aitMovimento.getDtDigitacao())
				.setDtMovimento(dtMovimentoNovo)
				.setTpStatus(TipoStatusEnum.REGISTRO_INFRACAO.getKey())
				.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.REGISTRADO.getKey())
				.setDtRegistroDetran(aitMovimento.getDtRegistroDetran())
				.build();
		this.aitMovimentoRepository.insert(aitMovimentoNovo, customConnection);
	}
}