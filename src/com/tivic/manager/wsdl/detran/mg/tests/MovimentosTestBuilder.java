package com.tivic.manager.wsdl.detran.mg.tests;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.builders.AitMovimentoCancelarAutoInfracaoBuilder;
import com.tivic.manager.wsdl.detran.mg.builders.AitMovimentoEnviadoAoDetranBuilder;
import com.tivic.manager.wsdl.detran.mg.builders.AitMovimentoRegistroBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class MovimentosTestBuilder {

	private AitRepository aitRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	
	private List<AitMovimento> movimentos;
	
	public MovimentosTestBuilder() throws Exception {
		this.movimentos = new ArrayList<AitMovimento>();
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}
	
	public MovimentosTestBuilder addNovoAit(String idAit, String nrPlaca) throws Exception {
		Ait ait = buildAit(idAit, nrPlaca);
		AitMovimento aitMovimento = new AitMovimentoEnviadoAoDetranBuilder(ait).build();
		this.aitMovimentoRepository.insert(aitMovimento, new CustomConnection());
		aitMovimento = new AitMovimentoRegistroBuilder(ait).build();
		this.aitMovimentoRepository.insert(aitMovimento, new CustomConnection());
		this.movimentos.add(aitMovimento);
		return this;
	}

	public MovimentosTestBuilder addCancelamentoAit(int cdAit) throws Exception {
		Ait ait = this.aitRepository.get(cdAit);
		AitMovimento aitMovimento = new AitMovimentoCancelarAutoInfracaoBuilder(ait).build();
		this.aitMovimentoRepository.insert(aitMovimento, new CustomConnection());
		this.movimentos.add(aitMovimento);
		return this;
	}
	
	private Ait buildAit(String idAit, String nrPlaca) throws Exception {
		Ait ait = new Ait();
		ait.setIdAit(idAit);
		ait.setNrControle("000001955");
		ait.setNrRenainf("02435001215");
		ait.setNrPlaca(nrPlaca);
		ait.setDtInfracao(Util.getDataAtual());
		ait.setCdAgente(233);
		ait.setCdMarcaVeiculo(149553);
		ait.setDsLocalInfracao("AVENIDA LUIZ BOALI, 230, CENTRO,  TEOFILO OTONI.");
		ait.setCdInfracao(1073);
		ait.setVlMulta(293.47);
//		ait.setVlVelocidadeAferida(80.0);
//		ait.setVlVelocidadePermitida(60.0);
//		ait.setVlVelocidadePenalidade(70.0);
		ait.setDsObservacao("EXEMPLO DE DESCRICAO");
		this.aitRepository.insert(ait, new CustomConnection());
		return ait;
	}
	
	public List<AitMovimento> build(){
		return this.movimentos;
	}
}
