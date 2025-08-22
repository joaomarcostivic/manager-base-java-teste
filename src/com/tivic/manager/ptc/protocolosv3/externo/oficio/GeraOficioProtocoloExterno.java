package com.tivic.manager.ptc.protocolosv3.externo.oficio;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.gpn.TipoDocumento;
import com.tivic.manager.gpn.tipodocumento.TipoDocumentoRepository;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.arquivo.IArquivoService;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.colaborador.IColaboradorService;
import com.tivic.manager.mob.lotes.builders.LoteBuilder;
import com.tivic.manager.mob.lotes.builders.impressao.LoteImpressaoBuilder;
import com.tivic.manager.mob.lotes.builders.protocolo.LoteDocumentoExternoBuilder;
import com.tivic.manager.mob.lotes.enums.documentoexterno.TipoLoteDocumentoExternoEnum;
import com.tivic.manager.mob.lotes.enums.impressao.StatusLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.documentoexterno.LoteDocumentoExterno;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.repository.documentoexterno.LoteDocumentoExternoRepository;
import com.tivic.manager.mob.lotes.repository.impressao.LoteImpressaoRepository;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoDTO;
import com.tivic.manager.ptc.protocolosv3.externo.builders.ArquivoProtocoloExternoBuilder;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.util.date.DateUtil;

public class GeraOficioProtocoloExterno implements IGeraOficioProtocoloExterno{

	private CidadeRepository cidadeRepository;
	private EstadoRepository estadoRepository;
	private IArquivoService arquivoService;
	private LoteDocumentoExternoRepository loteDocumentoExternoRepository;
	private TipoDocumentoRepository tipoDocumentoRepository;
	private IParametroRepository parametroRepository; 
	private int cdLoteImpressao;
	private IColaboradorService colaboradorService;
	private LoteRepository loteRepository;
	private LoteImpressaoRepository loteImpressaoRepository;
	
	public GeraOficioProtocoloExterno() throws Exception {
		cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
		estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
		this.arquivoService = (IArquivoService) BeansFactory.get(IArquivoService.class);
		this.loteDocumentoExternoRepository = (LoteDocumentoExternoRepository) BeansFactory.get(LoteDocumentoExternoRepository.class);
		this.tipoDocumentoRepository = (TipoDocumentoRepository) BeansFactory.get(TipoDocumentoRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.colaboradorService = (IColaboradorService) BeansFactory.get(IColaboradorService.class);
		this.loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
		this.loteImpressaoRepository = (LoteImpressaoRepository) BeansFactory.get(LoteImpressaoRepository.class);
	}
	
	@Override
	public LoteImpressao gerarOficio(List<ProtocoloExternoDTO> protocoloExternoDTO) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		Lote lote = criarLote(protocoloExternoDTO, customConnection);
		LoteImpressao loteImpressao = criarLoteImpressao(protocoloExternoDTO, lote.getCdLote(), customConnection);
    	byte[] printDocumentoExterno = this.gerar(protocoloExternoDTO);
    	Arquivo arquivo = salvarArquivo(printDocumentoExterno, protocoloExternoDTO, customConnection);
    	salvarLoteDocumentoExterno(loteImpressao.getCdLoteImpressao(), protocoloExternoDTO, customConnection);
    	lote.setCdArquivo(arquivo.getCdArquivo());
		loteRepository.update(lote, customConnection);
		return loteImpressao;
	}

	public byte[] gerar(List<ProtocoloExternoDTO> protocoloExternoDTO) throws Exception {
		ReportCriterios reportCriterios = montarReportCriterios();
		Report report = new ReportBuilder()
				.list(protocoloExternoDTO)
				.reportCriterios(reportCriterios)
				.build();
		return report.getReportPdf("ptc/oficio_protocolo_externo");
	}
	
	private Lote criarLote(List<ProtocoloExternoDTO> protocoloExternoDTO, CustomConnection customConnection) throws Exception {
		Lote lote = new LoteBuilder()
				.setDtCriacao(DateUtil.getDataAtual())
				.setCdCriador(protocoloExternoDTO.get(0).getCdUsuario())
				.setIdLote(Util.generateRandomString(5))
				.build();
		loteRepository.insert(lote, customConnection);
		return lote;
	}

	private LoteImpressao criarLoteImpressao(List<ProtocoloExternoDTO> protocoloExternoDTO, int cdLote, CustomConnection customConnection) throws Exception {
		LoteImpressao loteImpressao = new LoteImpressaoBuilder()
				.setCdLote(cdLote)
				.setStLote(StatusLoteImpressaoEnum.AGUARDANDO_IMPRESSAO.getKey())
				.setTpImpressao(TipoLoteDocumentoExternoEnum.LOTE_OFICIO_PROTOCOLO_EXTERNO.getKey())
				.build();
		loteImpressaoRepository.insert(loteImpressao, customConnection);
		return loteImpressao;
	}

	private Arquivo salvarArquivo(byte[] bytePdfDocumentoExterno, List<ProtocoloExternoDTO> protocoloExternoDTO, CustomConnection customConnection) throws Exception {
		Arquivo arquivoProtocoloExterno = new ArquivoProtocoloExternoBuilder(
				bytePdfDocumentoExterno, 
				protocoloExternoDTO.get(0), 
				this.cdLoteImpressao).build();
		this.arquivoService.save(arquivoProtocoloExterno, customConnection);
		return arquivoProtocoloExterno;
	}
	
	private void salvarLoteDocumentoExterno(int cdloteImpressao, List<ProtocoloExternoDTO> protocoloExternoDTO, CustomConnection customConnection) throws Exception {
		if(protocoloExternoDTO.size() > 0 && protocoloExternoDTO != null) {
			for(ProtocoloExternoDTO protocoloExterno : protocoloExternoDTO) {
				LoteDocumentoExterno loteDocumentoExterno = new LoteDocumentoExternoBuilder()
						.setCdLoteImpressao(cdloteImpressao)
						.setCdDocumento(protocoloExterno.getCdDocumento())
						.setCdDocumentoExterno(protocoloExterno.getCdDocumentoExterno())
						.build();
				this.loteDocumentoExternoRepository.insert(loteDocumentoExterno, customConnection);
			}
		}else {
			throw new ValidacaoException("Erro ao salvar. Lista de protocolo externo está vazia");
		}
	}
	
	private ReportCriterios montarReportCriterios() throws Exception {
		ReportCriterios reportCriterios = new ReportCriterios();
		reportCriterios.addParametros("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1"));
		reportCriterios.addParametros("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2"));
		reportCriterios.addParametros("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3"));
		reportCriterios.addParametros("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
		reportCriterios.addParametros("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
		reportCriterios.addParametros("MOB_PUBLICAR_AITS_NAO_ENTREGUES", ParametroServices.getValorOfParametro("MOB_PUBLICAR_AITS_NAO_ENTREGUES"));
		reportCriterios.addParametros("DT_GERACAO", new SimpleDateFormat("dd/MM/yyyy").format(new GregorianCalendar().getTime()));
		reportCriterios.addParametros("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE"));
		reportCriterios.addParametros("SG_UF", getEstado());
	    reportCriterios.addParametros("NUMERO_OFICIO", gerarNumeroOficio());
		reportCriterios.addParametros("NM_AUTORIDADE_TRANSITO", colaboradorService.buscaNomeAutoridadeTransito());
		reportCriterios.addParametros("SG_DEPARTAMENTO_TRANSITO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO"));
		reportCriterios.addParametros("DS_ENDERECO", ParametroServices.getValorOfParametro("DS_ENDERECO"));
        reportCriterios.addParametros("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE"));
        reportCriterios.addParametros("NR_TELEFONE2", ParametroServices.getValorOfParametro("NR_TELEFONE2"));
        reportCriterios.addParametros("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL"));
		return reportCriterios;
	}	
	
	public String getEstado() throws Exception {
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Cidade cidade = cidadeRepository.get(orgao.getCdCidade());
		return estadoRepository.get(cidade.getCdEstado()).getSgEstado();
	}
	
	private String gerarNumeroOficio() throws Exception {
		int cdTipoDocumento = parametroRepository.getValorOfParametroAsInt("MOB_CD_TIPO_DOCUMENTO_OFICIO_EXTERNO");
		if (cdTipoDocumento <= 0)
			throw new Exception("Parâmetro MOB_CD_TIPO_DOCUMENTO_OFICIO_EXTERNO não configurado.");
		TipoDocumento tipoDocumento = tipoDocumentoRepository.get(cdTipoDocumento);
	    Calendar calendario = new GregorianCalendar();
	    int anoAtual = calendario.get(Calendar.YEAR);
	    Integer sequencial = tipoDocumento.getNrUltimaNumeracao() + 1;
	    tipoDocumento.setNrUltimaNumeracao(sequencial);
	    updateTipoDocumento(tipoDocumento);
	    String numeroOficio = String.format("%04d/%04d", sequencial, anoAtual);
	    return numeroOficio;
	}
	
	private void updateTipoDocumento(TipoDocumento tipoDocumento) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			tipoDocumentoRepository.update(tipoDocumento, customConnection);
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
}
