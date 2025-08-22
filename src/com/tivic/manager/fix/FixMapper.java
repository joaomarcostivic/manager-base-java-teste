package com.tivic.manager.fix;

import com.tivic.manager.fix.mob.ait.FixAitServices;
import com.tivic.manager.fix.mob.ait.FixLoteImpressaoAit;
import com.tivic.manager.fix.ptc.protocolo.FixProtocolos;
import com.tivic.manager.fix.ptc.protocoloexterno.FixProtocoloExterno;
import com.tivic.manager.fix.mob.aitMovimento.FixAitMovimento;
import com.tivic.manager.util.FixServices;
import com.tivic.sol.fix.FixBuilder;
import com.tivic.sol.fix.FixMap;

public class FixMapper extends com.tivic.sol.fix.FixMapper {
	public FixMapper() throws Exception {
		super();
		this.fixes.add(new FixMap(new FixBuilder()
			.id("cod_movimentacao")
			.nmClass("com.tivic.manager.fix.mob.ait.FixAitServices")
			.nmMethod("fixIdMovimentacao")
			.apenasUmaVez()
		.build(), FixAitServices::fixIdMovimentacao));
		
		this.fixes.add(new FixMap(new FixBuilder()
			.id("cod_lote_impressao_ait")
			.nmClass("com.tivic.manager.fix.mob.ait.FixLoteImpressaoAit")
			.nmMethod("fixLoteImpressaoAit")
			.apenasUmaVez()
		.build(), FixLoteImpressaoAit::fixLoteImpressaoAit));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("correcao_data_prazo_defesa")
				.nmClass("com.tivic.manager.fix.mob.ait.FixAitServices")
				.nmMethod("fixCorrecaoDataPrazoDefesa")
				.apenasUmaVez()
			.build(), FixAitServices::fixCorrecaoDataPrazoDefesa));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("correcao_data_vencimento")
				.nmClass("com.tivic.manager.fix.mob.ait.FixAitServices")
				.nmMethod("fixCorrecaoDataVencimento")
				.apenasUmaVez()
			.build(), FixAitServices::fixCorrecaoDataVencimento));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("cidade_proprietario_ait")
				.nmClass("com.tivic.manager.fix.mob.ait.FixAitServices")
				.nmMethod("fixCdCidadeProprietarioAits")
				.apenasUmaVez()
			.build(), FixAitServices::fixCdCidadeProprietarioAits));
			
		this.fixes.add(new FixMap(new FixBuilder()	
				.id("limpeza_registro_situacao_documento")
				.nmClass("com.tivic.manager.fix.mob.ait.FixServices")
				.nmMethod("limpezaRegistroSituacaoDocumento")
				.apenasUmaVez()
			.build(), FixServices::limpezaRegistroSituacaoDocumento));
		
		this.fixes.add(new FixMap(new FixBuilder()	
				.id("limpeza_registro_fase")
				.nmClass("com.tivic.manager.fix.mob.ait.FixServices")
				.nmMethod("limpezaRegistroFase")
				.apenasUmaVez()
			.build(), FixServices::limpezaRegistroFase));
		
		this.fixes.add(new FixMap(new FixBuilder()	
				.id("alteracao_tabela")
				.nmClass("com.tivic.manager.fix.ptc.FixProtocoloExterno")
				.nmMethod("fixProtocoloExterno")
				.apenasUmaVez()
			.build(), FixProtocoloExterno::fixProtocoloExterno));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("movimento_cancelamento")
				.nmClass("com.tivic.manager.fix.mob.aitMovimento.FixAitMovimento")
				.nmMethod("fixMovimentoCancelamento")
				.apenasUmaVez()
			.build(), FixAitMovimento::fixMovimentoCancelamento));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("tp_cnh_condutor_erro")
				.nmClass("com.tivic.manager.fix.mob.ait.FixAitServices")
				.nmMethod("fixTpCnhCondutorComErro")
				.apenasUmaVez()
			.build(), FixAitServices::fixTpCnhCondutorComErro));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("tp_cnh_condutor_registro")
				.nmClass("com.tivic.manager.fix.mob.ait.FixAitServices")
				.nmMethod("fixTpCnhCondutorRegistro")
				.apenasUmaVez()
			.build(), FixAitServices::fixTpCnhCondutorRegistro));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("fixPessoa")
				.nmClass("com.tivic.manager.fix.mob.ait.FixAitServices")
				.nmMethod("fixPessoa")
				.apenasUmaVez()
			.build(), FixAitServices::fixPessoa));

		this.fixes.add(new FixMap(new FixBuilder()	
				.id("correcao_nr_ait_radar")
				.nmClass("com.tivic.manager.fix.mob.ait.FixServices")
				.nmMethod("fixNrAitRadar")
				.apenasUmaVez()
			.build(), FixServices::fixNrAitRadar));
		
		this.fixes.add(new FixMap(new FixBuilder()	
				.id("protocolo")
				.nmClass("com.tivic.manager.fix.ptc.FixProtocolos")
				.nmMethod("fixCancelaProtocolo")
				.apenasUmaVez()
			.build(), FixProtocolos::fixCancelaProtocolo));
		this.fixes.add(new FixMap(new FixBuilder()
				.id("alteracao_prazo_vencimento")
				.nmClass("com.tivic.manager.fix.mob.ait.FixLoteImpressaoAit")
				.nmMethod("alterarPrazoVencimentoAitLote")
				.apenasUmaVez()
			.build(), FixLoteImpressaoAit::alterarPrazoVencimentoAitLote));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("alteracao_vencimento_ait")
				.nmClass("com.tivic.manager.fix.mob.ait.FixAitServices")
				.nmMethod("alterarPrazoVencimentoAit")
				.apenasUmaVez()
			.build(), FixAitServices::alterarPrazoVencimentoAit));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("alteracao_tipo_lote_base_antiga")
				.nmClass("com.tivic.manager.fix.mob.ait.FixLoteImpressaoAit")
				.nmMethod("alterarTipoLoteImpressaoBaseAntiga")
				.apenasUmaVez()
			.build(), FixLoteImpressaoAit::alterarTipoLoteImpressaoBaseAntiga));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("movimento_sne")
				.nmClass("com.tivic.manager.fix.mob.aitMovimento.FixAitMovimento")
				.nmMethod("fixUpdateMovimentosSne")
				.apenasUmaVez()
				.build(), FixAitMovimento::fixUpdateMovimentosSne));
				
		this.fixes.add(new FixMap(new FixBuilder()
				.id("alteracao_registro_infracao_enviado")
				.nmClass("com.tivic.manager.fix.mob.ait.FixAitServices")
				.nmMethod("fixRegistroInfracaoArquivoMovimento")
				.apenasUmaVez()
			.build(), FixAitServices::fixRegistroInfracaoArquivoMovimento));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("alteracao_endereco_boat")
				.nmClass("com.tivic.manager.fix.mob.ait.FixAitServices")
				.nmMethod("enderecoBoat")
				.apenasUmaVez()
				.build(), FixAitServices::enderecoBoat));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("importar_nr_imovel_arquivo_csv")
				.nmClass("com.tivic.manager.fix.mob.ait.FixAitServices")
				.nmMethod("importarNrImovelArquivoCsv")
				.apenasUmaVez()
				.build(), FixAitServices::importarNrImovelArquivoCsv));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("importar_criar_nic_arquivo_csv")
				.nmClass("com.tivic.manager.fix.mob.ait.FixAitServices")
				.nmMethod("importarECriarNicrquivoCsv")
				.apenasUmaVez()
				.build(), FixAitServices::importarECriarNicrquivoCsv));
		
		this.fixes.add(new FixMap(new FixBuilder()
				.id("corrigir_placas_minusculas")
				.nmClass("com.tivic.manager.fix.mob.ait.FixAitServices")
				.nmMethod("corrigirPlacasMinusculas")
				.build(), FixAitServices::corrigirPlacasMinusculas));
		
	}
}
