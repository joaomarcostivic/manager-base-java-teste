package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.NoContentException;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.IParametroService;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorRepository;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloService;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.ApresentacaoCondutorUpdateValidatorBuilder;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.ApresentacaoCondutorValidatorBuilder;
import com.tivic.manager.ptc.protocolosv3.builders.ApresentacaoCondutorDTOSearchBuilder;
import com.tivic.manager.ptc.protocolosv3.resultado.ResultadoDTO;
import com.tivic.manager.ptc.protocolosv3.search.DadosApresentacaoCondutorDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class ApresentacaoCondutorService extends ProtocoloService 
{
	private ApresentacaoCondutorRepository apresentacaoCondutorRepository;
	private DocumentoRepository documentoRepository;
	private IParametroService parametroService;
	
	public ApresentacaoCondutorService() throws Exception {
		super();
		apresentacaoCondutorRepository = (ApresentacaoCondutorRepository) BeansFactory.get(ApresentacaoCondutorRepository.class);
		documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		this.parametroService = (IParametroService) BeansFactory.get(IParametroService.class);
	}
	
	public ApresentacaoCondutorDTO insert(ApresentacaoCondutorInsertDTO dadosProtocolo) throws Exception {
		return insert(dadosProtocolo, new CustomConnection());
	}
	
	public ApresentacaoCondutorDTO insert(ApresentacaoCondutorInsertDTO dadosProtocolo, CustomConnection customConnection) throws ValidacaoException, Exception {
		try {		
			customConnection.initConnection(true);
			getArquivoValido(dadosProtocolo);
			ProtocoloDTO protocolo = super.insert(dadosProtocolo, customConnection);
			ApresentacaoCondutorDTO apresentacaoCondutorDTO = 
					new ApresentacaoCondutorDTOBuilder()
					.protocolo(protocolo)
					.apresentacaoCondutor(dadosProtocolo)
					.build();
			new ApresentacaoCondutorValidatorBuilder().validate(apresentacaoCondutorDTO, customConnection);
			apresentacaoCondutorDTO.setArquivos(protocolo.getArquivos());
			apresentacaoCondutorRepository.insert(apresentacaoCondutorDTO.getApresentacaoCondutor(), customConnection);
			customConnection.finishConnection();
			if(super.isEnvioAutomatico) 
				super.enviarDetran(protocolo.getAitMovimento());
			return apresentacaoCondutorDTO;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	private void getArquivoValido(ApresentacaoCondutorInsertDTO apresentacaoCondutorInsertDTO) {
		List<Arquivo> arquivos = apresentacaoCondutorInsertDTO.getArquivos();
		arquivos = arquivos.stream()
                .filter(arquivo -> arquivo != null)
                .collect(Collectors.toList());
		apresentacaoCondutorInsertDTO.setArquivos(arquivos);
	}
	
	public ApresentacaoCondutorDTO update(ApresentacaoCondutorDTO protocolo) throws Exception {
		return update(protocolo, new CustomConnection());
	}

	public ApresentacaoCondutorDTO update(ApresentacaoCondutorDTO protocolo, CustomConnection customConnection) throws Exception {
		try {
			if(protocolo==null)
				throw new ValidacaoException("Erro ao salvar. Os dados do Documento são nulos.");
			
			ProtocoloDTO documento = super.update(protocolo);
			protocolo = new ApresentacaoCondutorUpdateBuilder(protocolo).apresentacaoCondutor(customConnection).build();
			protocolo.setDocumento(documento.getDocumento());
			new ApresentacaoCondutorUpdateValidatorBuilder().validate(protocolo, customConnection);
			apresentacaoCondutorRepository.update(protocolo.getApresentacaoCondutor());
			customConnection.closeConnection();	
			return protocolo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public ApresentacaoCondutorDTO get(Integer cdDocumento, Integer cdApresentacaoCondutor) throws Exception {
		if(cdDocumento == null || cdDocumento == 0)
			throw new ValidacaoException("Erro! Código do Documento inválido.");
		
		if(cdApresentacaoCondutor == null || cdApresentacaoCondutor == 0)
			throw new ValidacaoException("Erro! Código da Apresentação de Condutor inválido.");
		
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("D.cd_documento", cdDocumento, cdDocumento > 0);
		searchCriterios.addCriteriosEqualInteger("G.cd_apresentacao_condutor", cdApresentacaoCondutor, cdApresentacaoCondutor > 0);
	
		Search<DadosApresentacaoCondutorDTO> dadosProtocoloSearch = dadosApresentacaoCondutorBuilder(searchCriterios);
		DadosApresentacaoCondutorDTO dadosApresentacaoCondutorDTO = dadosProtocoloSearch.getList(DadosApresentacaoCondutorDTO.class).get(0);
		Integer tpCategoria = verificaTpCategoriaNull(null, cdApresentacaoCondutor);
		dadosApresentacaoCondutorDTO.setTpCategoriaCnh(String.valueOf(tpCategoria));
		
		List<DadosApresentacaoCondutorDTO> dadosProtocolo = new ArrayList<DadosApresentacaoCondutorDTO>();
		dadosProtocolo.add(dadosApresentacaoCondutorDTO);
		List<ApresentacaoCondutorDTO> protocolo = new ApresentacaoCondutorDTOSearchBuilder(dadosProtocoloSearch, dadosProtocolo).build();
		if(protocolo.size() < 1)
			throw new NoContentException("Nenhum protocolo encontrado");
		
		return protocolo.get(0);
	}
	
	private Integer verificaTpCategoriaNull(Connection connect, int cdApresentacaoCondutor) throws SQLException {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_apresentacao_condutor WHERE cd_apresentacao_condutor=?");
			pstmt.setInt(1, cdApresentacaoCondutor);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return rs.getObject("tp_categoria_cnh") != null ? rs.getObject("tp_categoria_cnh", Integer.class) : null;
			}
			else{
				return null;
			}
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private Search<DadosApresentacaoCondutorDTO> dadosApresentacaoCondutorBuilder(SearchCriterios searchCriterios) throws Exception{
		Search<DadosApresentacaoCondutorDTO> search = new SearchBuilder<DadosApresentacaoCondutorDTO>("mob_ait_movimento A")
			.fields(" A.*, B.ID_AIT, B.NR_PLACA, D.NR_DOCUMENTO, D.NM_REQUERENTE, "
				  + "D.CD_DOCUMENTO, D.DT_PROTOCOLO, D.TXT_OBSERVACAO, D.TP_DOCUMENTO, B.DT_INFRACAO, E.NM_TIPO_DOCUMENTO,"
				  + "E.ID_TIPO_DOCUMENTO, F.CD_FASE, F.NM_FASE, H.CD_SITUACAO_DOCUMENTO, H.NM_SITUACAO_DOCUMENTO, G.*")
			.addJoinTable(" JOIN mob_ait B ON (A.CD_AIT = B.CD_AIT) ")
			.addJoinTable(" JOIN mob_ait_movimento_documento C ON (C.CD_AIT = B.cd_ait AND A.cd_movimento = C.cd_movimento) ")
			.addJoinTable(" JOIN ptc_documento D ON (C.cd_documento = D.cd_documento) ")
			.addJoinTable(" JOIN gpn_tipo_documento E ON (E.cd_tipo_documento = D.cd_tipo_documento) ")
			.addJoinTable(" JOIN ptc_fase F ON (F.cd_fase = D.cd_fase) ")
			.addJoinTable(" JOIN ptc_situacao_documento H ON (D.cd_situacao_documento = H.cd_situacao_documento)" )
			.addJoinTable(" JOIN ptc_apresentacao_condutor G ON (G.cd_documento = D.cd_documento)" )
			.searchCriterios(searchCriterios)
			.build();
		
		return search;

	}
	
	public ProtocoloDTO cancelar(ProtocoloDTO protocolo) throws Exception {
		return cancelar(protocolo, new CustomConnection());
	}

	public ProtocoloDTO cancelar(ProtocoloDTO dadosProtocolo, CustomConnection customConnection) throws ValidacaoException, Exception {
		try {
			if(dadosProtocolo==null)
				throw new ValidacaoException("Erro ao cancelar. Os dados do Documento são nulos.");
			
			customConnection.initConnection(true);
			super.cancelar(dadosProtocolo, customConnection);
			customConnection.finishConnection();
			return dadosProtocolo;
		} finally {
			customConnection.closeConnection();
		}
	}
	
	public ResultadoDTO deferir(ResultadoDTO resultado) throws Exception, ValidacaoException {
		resultado.setCdSituacaoDocumento(getCdParametro("CD_SITUACAO_DOCUMENTO_DEFERIDO"));
		resultado.setCdTipoOcorrencia(getCdParametro("CD_TIPO_OCORRENCIA_DEFERIDA"));
		return saveResultado(resultado, new CustomConnection());
	}
	
	public ResultadoDTO indeferir(ResultadoDTO resultado) throws Exception, ValidacaoException {
		resultado.setCdSituacaoDocumento(getCdParametro("CD_SITUACAO_DOCUMENTO_INDEFERIDO"));
		resultado.setCdTipoOcorrencia(getCdParametro("CD_TIPO_OCORRENCIA_INDEFERIDA"));
		return saveResultado(resultado, new CustomConnection());
	}


	private ResultadoDTO saveResultado(ResultadoDTO resultado, CustomConnection customConnection) throws Exception, ValidacaoException {
		customConnection.initConnection(true);
		try {
			if(resultado==null)
				throw new ValidacaoException("Erro ao salvar. Documento é nulo.");
			Documento documento = getDocumento(resultado.getCdDocumento());
			documentoRepository.update(setDadosDocumento(documento, resultado, customConnection), customConnection);
			
			AitMovimento movimento = getMovimento(resultado.getCdMovimento(), resultado.getCdAit());
			this.aitMovimentoRepository.update(setDadosMovimento(movimento, resultado, customConnection), customConnection);
			customConnection.finishConnection();
			return resultado;
		} finally {
			customConnection.closeConnection();
		}
	}

	public Documento getDocumento(int cdDocumento) throws Exception {
		Documento documento = documentoRepository.get(cdDocumento);
		if(documento == null)
			throw new Exception("Documento não encontrado.");
		
		return documento;
	} 
	
	private Documento setDadosDocumento(Documento documentoAnterior, ResultadoDTO resultado, CustomConnection customConnection) throws Exception {
		int cdFaseJulgado = getCdParametro("CD_FASE_JULGADO"); 
		documentoAnterior.setCdFase(cdFaseJulgado);
		documentoAnterior.setCdSituacaoDocumento(resultado.getCdSituacaoDocumento());
		return documentoAnterior;
	}
	
	private AitMovimento getMovimento(int cdMovimento, int cdAit) throws Exception {
		AitMovimento aitMovimento = aitMovimentoRepository.get(cdMovimento, cdAit);
		if(aitMovimento == null)
			throw new Exception("Movimento não encontrado.");
		
		return aitMovimento;
	} 
	
	private AitMovimento setDadosMovimento(AitMovimento movimento, ResultadoDTO resultado, CustomConnection customConnection) throws Exception {
		movimento.setLgEnviadoDetran(0);
		movimento.setCdOcorrencia(resultado.getCdOcorrencia());
		return movimento;
	}
	
	private int getCdParametro(String nmParametro) {
		int cdParametro = ParametroServices.getValorOfParametroAsInteger(nmParametro, 0);
		if(cdParametro == 0)
			throw new BadRequestException("O parâmetro "+ nmParametro +" não foi configurado.");
		
		return cdParametro;
	}

}
