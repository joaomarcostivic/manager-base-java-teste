package com.tivic.manager.wsdl.detran.mg.incluirautoinfracao;

import java.util.HashSet;
import java.util.Set;

import com.tivic.manager.fta.Cor;
import com.tivic.manager.fta.CorDAO;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.grl.equipamento.repository.EquipamentoDAO;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.AgenteDAO;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoServices;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistenciaServices;
import com.tivic.manager.mob.AitInconsistenciaBuilder.AitInconsistenciaBuilder;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.mob.ait.TpDocumentoCondutorNaoHabilitadoEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaService;
import com.tivic.manager.mob.inconsistencias.TipoInconsistenciaEnum;
import com.tivic.manager.mob.inconsistencias.TipoSituacaoInconsistencia;
import com.tivic.manager.mob.infracao.InfracaoRepository;
import com.tivic.manager.mob.infracao.TipoCompetenciaEnum;
import com.tivic.manager.ptc.protocolosv3.enums.CodigoPaisCNHEnum;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.detran.mg.validators.IncluirCodigoInfracaoValidators;
import com.tivic.manager.wsdl.detran.mg.validators.InfracaoCondutorValidatorBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.TabelasAuxiliares;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class IncluirAutoInfracaoDadosEntradaFactory {

	private static TabelasAuxiliares tabelasAuxiliares = new TabelasAuxiliaresMG();
	private static IAitService aitService;
	private static InfracaoRepository infracaoRepository;
	private static IAitMovimentoService aitMovimentoService;
	
	public static IncluirAutoInfracaoDadosEntrada fazerDadoEntrada(Ait ait, boolean lgHomologacao) throws ValidacaoException, Exception{
		aitService = (IAitService) BeansFactory.get(IAitService.class);
		infracaoRepository = (InfracaoRepository) BeansFactory.get(InfracaoRepository.class);	
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		Infracao infracao = infracaoRepository.get(ait.getCdInfracao());
		if(infracao == null)
			throw new ValidacaoException("Movimento sem ocorrência");
		validarCompetenciaInfracao(infracao, ait);
		Agente agente = AgenteDAO.get(ait.getCdAgente());
		Cor cor = CorDAO.get(ait.getCdCorVeiculo());
		Equipamento equipamento = EquipamentoDAO.get(ait.getCdEquipamento());
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Cidade cidade = CidadeDAO.get(orgao.getCdCidade());
		Estado estado = EstadoDAO.get(cidade.getCdEstado());
		IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada = new IncluirAutoInfracaoDadosEntrada();
		incluirAutoInfracaoDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		incluirAutoInfracaoDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		incluirAutoInfracaoDadosEntrada.setPlaca(ait.getNrPlaca());
		incluirAutoInfracaoDadosEntrada.setUf(ait.getSgUfVeiculo());
		incluirAutoInfracaoDadosEntrada.setAit(ait.getIdAit());
		validarCodigoInfracao(infracao);
		incluirAutoInfracaoDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		incluirAutoInfracaoDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		incluirAutoInfracaoDadosEntrada.setFiscalizacaoEletronica(isFiscalizacaoEletronica(equipamento));
		incluirInfoAgente(agente, equipamento, incluirAutoInfracaoDadosEntrada);
		incluirAutoInfracaoDadosEntrada.setDataAit(ait.getDtInfracao());
		incluirAutoInfracaoDadosEntrada.setHoraAit(ait.getDtInfracao());
		incluirAutoInfracaoDadosEntrada.setLocalAit(ait.getDsLocalInfracao());
		incluirAutoInfracaoDadosEntrada.setCodigoMunicipio(cidade.getIdCidade());
		incluirAutoInfracaoDadosEntrada.setUfMunicipio(estado.getSgEstado());
		incluirAutoInfracaoDadosEntrada.setAssinatura(tabelasAuxiliares.getAssinatura(ait.getLgAutoAssinado()));
		validarCondutor(ait);
		incluirCondutor(ait, incluirAutoInfracaoDadosEntrada);
		incluirDadosVeiculo(ait, incluirAutoInfracaoDadosEntrada);		
		incluirCor(cor, incluirAutoInfracaoDadosEntrada);
		incluirAutoInfracaoDadosEntrada.setValorAit(ait.getVlMulta());
		incluirVelocidade(ait, incluirAutoInfracaoDadosEntrada);
		incluirAutoInfracaoDadosEntrada.setNomeEmbarcador("");//:TODO Esperando a informaï¿½ï¿½o ser adicionada a tabela de AIT
		incluirAutoInfracaoDadosEntrada.setDocumentoEmbarcador("");//:TODO Esperando a informaï¿½ï¿½o ser adicionada a tabela de AIT
		incluirAutoInfracaoDadosEntrada.setNomeTransportador("");//:TODO Esperando a informaï¿½ï¿½o ser adicionada a tabela de AIT
		incluirAutoInfracaoDadosEntrada.setDocumentoTransportador("");//:TODO Esperando a informaï¿½ï¿½o ser adicionada a tabela de AIT
		incluirAutoInfracaoDadosEntrada.setEspecieVeiculo("");//Nï¿½o utilizado para Minas (Apenas pelo Sistema SIDAM)
		incluirObservacao(ait, incluirAutoInfracaoDadosEntrada);
		incluirAutoInfracaoDadosEntrada.setCampoReservado("");//Nï¿½o utilizado
		incluirAutoInfracaoDadosEntrada.setNmServico("INCLUIR AUTO INFRACAO");
	
		return incluirAutoInfracaoDadosEntrada;
	}
	
	private static void validarCompetenciaInfracao(Infracao infracao, Ait ait) throws Exception {
		if(infracao.getTpCompetencia() == TipoCompetenciaEnum.ESTADUAL.getKey()) {
			atualizarMovimento(ait.getCdAit());
			criarInconsistencia(ait);			
		}
	}
	
	private static void atualizarMovimento(int cdAit) throws Exception {
		AitMovimento aitMovimento = aitMovimentoService.getStatusMovimento(cdAit, TipoStatusEnum.REGISTRO_INFRACAO.getKey());
		aitMovimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey());
		aitMovimentoService.update(aitMovimento);
	}
	
	private static void criarInconsistencia(Ait ait) throws Exception {
		int tpErroInfracaoCompetenciaEstadual = ParametroServices.getValorOfParametroAsInteger("MOB_INCONSISTENCIA_INFRACAO_DE_COMPETENCIA_ESTADUAL", 0);
		AitInconsistencia aitInconsistencia = new AitInconsistenciaBuilder().build(ait, tpErroInfracaoCompetenciaEstadual, TipoStatusEnum.REGISTRO_INFRACAO.getKey());
		aitInconsistencia.setStInconsistencia(TipoSituacaoInconsistencia.RESOLVIDO.getKey());
		new AitInconsistenciaService().salvarInconsistencia(aitInconsistencia);
	}
	
	private static void validarCodigoInfracao(Infracao infracao) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			new IncluirCodigoInfracaoValidators().validate(infracao, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private static String isFiscalizacaoEletronica(Equipamento equipamento){
		return equipamento != null && equipamento.getTpEquipamento() == EquipamentoServices.RADAR_FIXO ? "S" : "";
	}
	
	private static void incluirInfoAgente(Agente agente, Equipamento equipamento, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada){
		if(agente != null){
			incluirAutoInfracaoDadosEntrada.setCodigoAgente(agente.getNrMatricula());//Homologacao, usar 12345
			incluirAutoInfracaoDadosEntrada.setTipoAgente(equipamento != null && equipamento.getTpEquipamento() == EquipamentoServices.RADAR_FIXO ? 0 : 1);//:TODO Homologacao, usar 1 por padrao pois a nossa tabela de Tipo de Agente e referente a Transito/Transporte
		}
	}
	
	private static void incluirDocNaoHabilitado(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada){
		try{
			if(ait.getTpCnhCondutor() == AitServices.TP_CNH_NAO_HABILITADO){
				incluirAutoInfracaoDadosEntrada.setTipoDocumento(tabelasAuxiliares.getTipoDocumento(ait.getTpDocumento()));
				incluirAutoInfracaoDadosEntrada.setNumeroDocumento(ait.getNrDocumento());
			}
		}
		catch(NullPointerException npe){}
	}
	
	private static void incluirRg(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada){
		if(ait.getTpDocumento() == AitServices.TP_DOCUMENTO_RG){
			incluirAutoInfracaoDadosEntrada.setRgCondutor(ait.getNrDocumento());
		}
		else{
			incluirAutoInfracaoDadosEntrada.setRgCondutor("");
		}
		incluirAutoInfracaoDadosEntrada.setOrgaoRg("");
		incluirAutoInfracaoDadosEntrada.setUfRg("");
	}
	
	private static void validarCondutor(Ait ait) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(false);
			new InfracaoCondutorValidatorBuilder().validate(ait, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private static void incluirCondutor(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada) {
		incluirCpfCondutor(ait, incluirAutoInfracaoDadosEntrada);
		incluirTpCnh(ait, incluirAutoInfracaoDadosEntrada);
		incluirNrCnh(ait, incluirAutoInfracaoDadosEntrada);
		incluirUFCnh(ait, incluirAutoInfracaoDadosEntrada);
		incluirDocumentoCondutorNaoHabilitado(ait, incluirAutoInfracaoDadosEntrada);
		incluirCdPaisCnh(ait, incluirAutoInfracaoDadosEntrada);
		incluirNmCondutor(ait, incluirAutoInfracaoDadosEntrada);
		incluirRg(ait, incluirAutoInfracaoDadosEntrada);  
	}
	
	private static void incluirTpDocumento(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada) {
		if(ait.getTpDocumento() > 0 && ait.getTpDocumento() != TpDocumentoCondutorNaoHabilitadoEnum.NAO_APRESENTADO.getKey()) {
			incluirAutoInfracaoDadosEntrada.setTipoDocumento(ait.getTpDocumento());
		}
	}
	
	private static void incluirDocumentoCondutorNaoHabilitado(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada) {
		if (ait.getTpCnhCondutor() == TipoCnhEnum.TP_CNH_NAO_HABILITADO.getKey()) {
			incluirTpDocumento(ait, incluirAutoInfracaoDadosEntrada);
			Set<Integer> documentosAceitos = new HashSet<>();
			documentosAceitos.add(TpDocumentoCondutorNaoHabilitadoEnum.CPF.getKey());
			documentosAceitos.add(TpDocumentoCondutorNaoHabilitadoEnum.CNPJ.getKey());
			documentosAceitos.add(TpDocumentoCondutorNaoHabilitadoEnum.RG.getKey());
			documentosAceitos.add(TpDocumentoCondutorNaoHabilitadoEnum.RNE.getKey());
			documentosAceitos.add(TpDocumentoCondutorNaoHabilitadoEnum.OUTROS.getKey());
			if (documentosAceitos.contains(ait.getTpDocumento())) {
				incluirAutoInfracaoDadosEntrada.setNumeroDocumento(ait.getNrDocumentoAutuacao());
			}
		}
	}
	
	private static void incluirNrCnh(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada) {
		if(ait.getNrCnhCondutor() != null && !ait.getNrCnhCondutor().trim().equals(""))
			incluirAutoInfracaoDadosEntrada.setNumeroCnh(ait.getNrCnhCondutor());
	}
	
	private static void incluirTpCnh(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada) {
		if(ait.getTpCnhCondutor() > 0)
			incluirAutoInfracaoDadosEntrada.setModeloCnh(ait.getTpCnhCondutor());			
	}
	
	private static void incluirUFCnh(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada) {
		if(ait.getUfCnhAutuacao() != null && !ait.getUfCnhAutuacao().trim().equals(""))
			incluirAutoInfracaoDadosEntrada.setUfCnh(ait.getUfCnhAutuacao());
	}
	
	private static void incluirCdPaisCnh(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada) {
		if(ait.getTpCnhCondutor() == TipoCnhEnum.TP_CNH_HABILITACAO_ESTRANGEIRA.getKey()) 
			incluirAutoInfracaoDadosEntrada.setCodigoPaisCnh(CodigoPaisCNHEnum.OUTROS.getKey());
	}
	
	private static void incluirNmCondutor(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada) {
		if(ait.getNmCondutor() != null && !ait.getNmCondutor().trim().equals(""))
			incluirAutoInfracaoDadosEntrada.setNomeCondutor(ait.getNmCondutor());
	}
	
	private static void incluirCpfCondutor(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada) {
		if(ait.getNrCpfCondutor()!=null && !ait.getNrCpfCondutor().trim().equals(""))
			incluirAutoInfracaoDadosEntrada.setCpfCondutor(ait.getNrCpfCondutor());
	}

	
	private static void incluirCnh(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada){
		if(ait.getNrCnhAutuacao()!= null && !ait.getNrCnhAutuacao().trim().equals("")){
			incluirAutoInfracaoDadosEntrada.setNumeroCnh(ait.getNrCnhAutuacao());
			
			if(ait.getUfCnhAutuacao()!= null && !ait.getUfCnhAutuacao().trim().equals("")) {
				incluirAutoInfracaoDadosEntrada.setUfCnh(ait.getUfCnhAutuacao());
			}
						
			incluirAutoInfracaoDadosEntrada.setCodigoPaisCnh(0);
			
			//tpCnh
			try {
				if(ait.getTpCnhCondutor() == -1)
					System.out.println("Não informada");
				if(ait.getTpCnhCondutor()!=0 || ait.getTpCnhCondutor()!=-1)
					incluirAutoInfracaoDadosEntrada.setModeloCnh(tabelasAuxiliares.getTipoCnh(ait.getTpCnhCondutor()));
				else
					incluirAutoInfracaoDadosEntrada.setModeloCnh(tabelasAuxiliares.getTipoCnh(AitServices.TP_CNH_NOVA));
				
			} catch(Exception e) {
				System.out.println("\t\t> IncluirAutoInfracao: Erro ao preencher MODELO CNH");
			}
		}
	}
	
	private static void incluirCor(Cor cor, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada){
		if(cor != null)
			incluirAutoInfracaoDadosEntrada.setCor(cor.getNmCor());
	}
	
	private static void incluirVelocidade(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada){
		if(ait.getVlVelocidadeAferida() != null && ait.getVlVelocidadeAferida() > 0)
			incluirAutoInfracaoDadosEntrada.setMedicaoReal(ait.getVlVelocidadeAferida());
		if(ait.getVlVelocidadePermitida() != null && ait.getVlVelocidadePermitida() > 0)
			incluirAutoInfracaoDadosEntrada.setLimite(ait.getVlVelocidadePermitida());
		if(ait.getVlVelocidadePenalidade() != null && ait.getVlVelocidadePenalidade() > 0)
			incluirAutoInfracaoDadosEntrada.setMedicaoConsiderada(ait.getVlVelocidadePenalidade());
		if(ait.getVlVelocidadeAferida() != null && ait.getVlVelocidadePermitida() != null && ait.getVlVelocidadePenalidade() != null && (ait.getVlVelocidadeAferida() > 0 || ait.getVlVelocidadePermitida() > 0 || ait.getVlVelocidadePenalidade() > 0))
			incluirAutoInfracaoDadosEntrada.setUnidadeMedida(1);//:TODO Fixo, saber qual tabela usada
		
	}
	
	private static void incluirObservacao(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada){
		if(ait.getDsObservacao() != null)
			incluirAutoInfracaoDadosEntrada.setObservacao(ait.getDsObservacao().replaceAll("\"", ""));
	}
	
	private static void setCamposCnhNaoInformada(IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada) {
		incluirAutoInfracaoDadosEntrada.setCpfCondutor(null);
		incluirAutoInfracaoDadosEntrada.setNumeroDocumento(null);
		incluirAutoInfracaoDadosEntrada.setNomeCondutor(null);
		incluirAutoInfracaoDadosEntrada.setNumeroCnh(null);
		incluirAutoInfracaoDadosEntrada.setUfCnh(null);
	}
	
	private static boolean checkCnh(Ait ait) {
		boolean isNull = false;
		
		if(ait.getNrCnhCondutor() != null && ait.getUfCnhCondutor() != null && ait.getNmCondutor() != null) {
			isNull = ait.getNrCnhCondutor().equals("");
			isNull = ait.getUfCnhCondutor().equals("");
			isNull = ait.getNmCondutor().equals("");
		} else {
			isNull = true;
		}
		
		return isNull;
	}
	
	private static void incluirDadosVeiculo(Ait ait, IncluirAutoInfracaoDadosEntrada incluirAutoInfracaoDadosEntrada) {
		try {
			aitService.updateDetran(ait);
			MarcaModelo marcaModelo = MarcaModeloServices.get(ait.getCdMarcaVeiculo());
			if(marcaModelo != null) {
				incluirAutoInfracaoDadosEntrada.setDescricaoMarcaModelo(marcaModelo.getNmModelo());
				incluirAutoInfracaoDadosEntrada.setCodigoMarcaModelo(marcaModelo.getNrMarca());
			}
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
	
}
