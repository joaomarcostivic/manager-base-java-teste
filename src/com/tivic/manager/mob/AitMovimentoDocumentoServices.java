package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.ws.rs.core.NoContentException;

import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.gpn.TipoDocumento;
import com.tivic.manager.gpn.TipoDocumentoDAO;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.EstadoServices;
import com.tivic.manager.grl.FormularioAtributo;
import com.tivic.manager.grl.FormularioAtributoDAO;
import com.tivic.manager.grl.FormularioAtributoValor;
import com.tivic.manager.grl.FormularioAtributoValorDAO;
import com.tivic.manager.grl.FormularioAtributoValorServices;
import com.tivic.manager.grl.Parametro;
import com.tivic.manager.grl.ParametroOpcao;
import com.tivic.manager.grl.ParametroOpcaoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.mob.ait.relatorios.BuscarFiltrosRelatorioBuilder;
import com.tivic.manager.mob.ait.relatorios.RelatorioAitDTO;
import com.tivic.manager.mob.colaborador.ColaboradorDTOPageBuilder;
import com.tivic.sol.report.ReportServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoArquivo;
import com.tivic.manager.ptc.DocumentoArquivoDAO;
import com.tivic.manager.ptc.DocumentoArquivoServices;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.DocumentoPessoa;
import com.tivic.manager.ptc.DocumentoPessoaServices;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.ptc.Fase;
import com.tivic.manager.ptc.FaseDAO;
import com.tivic.manager.ptc.FaseServices;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioServices;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.manager.util.Util;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.tivic.manager.validation.Validators;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.ProtocoloExternoDetranObject;
import com.tivic.manager.wsdl.ServicoDetranExternoFactory;
import com.tivic.manager.wsdl.ServicoDetranFactory;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.detran.mg.recursofici.TipoParecerFici;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.manager.wsdl.interfaces.ServicoDetranRecurso;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.ConfManager;
import sol.util.Result;

public class AitMovimentoDocumentoServices {

	CidadeRepository cidadeRepository;
	EstadoRepository estadoRepository;
	ParametroServices parametroServices;
	
	public AitMovimentoDocumentoServices() throws Exception {
		cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
		estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
	}
	
	public AitMovimentoDocumentoDTO insert(AitMovimentoDocumentoDTO dto, int cdAit, Validators<?> validators) throws ValidationException, SQLException {
		return insert(dto, cdAit, validators, null);
	}
	
	public AitMovimentoDocumentoDTO insert(AitMovimentoDocumentoDTO dto, int cdAit, Validators<?> validators, Connection connection) throws ValidationException, SQLException {
		boolean isConnNull = connection == null;
		int apresCondutor = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_APRESENTACAO_CONDUTOR", 0, 0, connection);
		int jari = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_JARI", 0, 0, connection);
		int deferido = ParametroServices.getValorOfParametroAsInteger("CD_FASE_DEFERIDO", 0, 0, connection);
		int indeferido = ParametroServices.getValorOfParametroAsInteger("CD_FASE_INDEFERIDO", 0, 0, connection);
		
		try {
			if(isConnNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			// VALIDACOES =====================================================			
			Ait ait = AitDAO.get(cdAit, connection);
			if(ait == null) {
				throw new ValidationException("AIT informado não existe.");
			}

			Optional<String> error = validators.validateAll();
			if(error.isPresent())
				throw new ValidationException(error.get());
			// ================================================================
			
			// ATA ============================================================
			
			if(dto.getDocumentoSuperior().getNrDocumento() != null && dto.getDocumentoSuperior().getNrDocumento() != "") {
				Documento documentoSuperior = this.insertAta(dto.getDocumentoSuperior(), connection);
				if(documentoSuperior == null) {
					throw new ValidationException("Erro ao inserir a ata");
				}
				dto.setDocumentoSuperior(documentoSuperior);
				dto.getDocumento().setCdDocumentoSuperior(dto.getDocumentoSuperior().getCdDocumento());
			}
			
			// INSERIR DOCUMENTO ==============================================
			int cdFasePendente = FaseServices.getCdFaseByNome("Pendente", connection);
			if(dto.getDocumento().getCdFase() != cdFasePendente) {
				String nrDocumento = dto.getMovimento().getNrProcesso();
				
				if(nrDocumento != null) 
					dto.getDocumento().setNrProtocoloExterno(nrDocumento);
			}
			
			Documento _documento = this.insert(dto.getDocumento(), dto.getCamposFormulario(), connection);
			if(_documento == null) {
				throw new ValidationException("Erro ao inserir o protocolo");
			}
			dto.setDocumento(_documento);	

			if(dto.getArquivos() != null && dto.getArquivos().size() > 0)
				dto.setArquivosAnexados(this.convFiles(dto));
			
			List<DocumentoArquivo> _arquivos = this.insert(dto.getArquivosAnexados(), _documento.getCdDocumento(), connection);
			dto.setArquivosAnexados(_arquivos);
			dto.getMovimento().setNrMovimento(0);
			// ================================================================

			// MOVIMENTO ==================================================
			AitMovimento _movimento = this.generate(_documento, cdAit, dto);
			
			if(dto.getDocumento().getCdTipoDocumento() == apresCondutor) {
				Criterios criterios = new Criterios();
				criterios.add("tp_ocorrencia", "3", ItemComparator.EQUAL, Types.INTEGER);
				criterios.add("id_movimentacao", "22", ItemComparator.EQUAL, Types.INTEGER);
				
				ResultSetMap rsm = OcorrenciaServices.find(criterios);
				
				if(rsm.next()) {
					_movimento.setCdOcorrencia(rsm.getInt("cd_ocorrencia"));
				}
			}
			
			AitMovimento movimento = this.insert(_movimento, connection);
			dto.setMovimento(movimento);
			// ============================================================
			
			// AIT ========================================================
			if(apresCondutor == dto.getDocumento().getCdTipoDocumento() && dto.getDocumento().getCdFase() == FaseServices.getCdFaseByNome("Deferido", connection))
				ait = updateAitCondutor(dto, ait, connection);
			
			ait = this.updateUltimoMovimento(dto);
			int resultadoAtualizacaoAit = AitDAO.update(ait, connection);
			if(resultadoAtualizacaoAit < 0) {
				throw new ValidationException("Erro ao atualizar ait");
			}
			// ================================================================
			
			AitMovimentoDocumento _amd = new AitMovimentoDocumento();
			_amd.setCdAit(cdAit);
			_amd.setCdDocumento(_documento.getCdDocumento());
			_amd.setCdMovimento(dto.getMovimento().getNrMovimento());
			_amd = this.insert(_amd, connection);
			
			// DOCUMENTO_PESSOA ===============================================
			if(dto.getDocumento().getCdTipoDocumento() == jari && (dto.getDocumento().getCdFase() == deferido || dto.getDocumento().getCdFase() == indeferido)) {
				dto.getDocumentoPessoa().setCdDocumento(dto.getDocumento().getCdDocumento());
				dto.getDocumentoPessoa().setNmQualificacao("RELATOR JARI");
				Result result = DocumentoPessoaServices.save(dto.getDocumentoPessoa(), connection);
				if(result.getCode() < 0) {
					throw new ValidationException("Erro ao salvar relator da jari");
				}
				DocumentoPessoa _documentoPessoa =(DocumentoPessoa) result.getObjects().get("PESSOA");
				dto.setDocumentoPessoa(_documentoPessoa);
			}

			if(isConnNull)
				connection.commit();
			
			return dto;
		} catch(SQLException sqlex) {
			if(isConnNull) {
				Conexao.rollback(connection);
			}
			System.out.println("Erro! AitMovimentoDocumentoServices.insert");
			sqlex.printStackTrace(System.out);
			throw sqlex;
		} catch(ValidationException ex) {
			if(isConnNull) {
				Conexao.rollback(connection);
			}
			System.out.println("Erro! AitMovimentoDocumentoServices.insert");
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			if(isConnNull) 
				Conexao.desconectar(connection);
		}
	}
	
	public AitMovimentoDocumentoDTO insertExterno(AitMovimentoDocumentoDTO dto) throws ValidationException, SQLException {
		return insertExterno(dto, null);
	}
	
	public AitMovimentoDocumentoDTO insertExterno(AitMovimentoDocumentoDTO dto, Connection connection) throws ValidationException, SQLException {
		boolean isConnNull = connection == null;
		int apresCondutor = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_APRESENTACAO_CONDUTOR", 0, 0, connection);
		
		try {
			if(isConnNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			
			// INSERIR DOCUMENTO ==============================================			
			Documento _documento = this.insertDocumentoExterno(dto.getDocumento(), dto.getCamposFormulario(), connection);
			dto.setDocumento(_documento);	

			// MOVIMENTO ==================================================
			AitMovimento _movimento = this.generateMovimentoExterno(_documento, dto);
			dto.getMovimento().setNrMovimento(0);
			dto.setMovimento(_movimento);
			
			if(dto.getDocumento().getCdTipoDocumento() == apresCondutor) {
				Criterios criterios = new Criterios();
				criterios.add("tp_ocorrencia", "3", ItemComparator.EQUAL, Types.INTEGER);
				criterios.add("id_movimentacao", "22", ItemComparator.EQUAL, Types.INTEGER);
				
				ResultSetMap rsm = OcorrenciaServices.find(criterios);
				
				if(rsm.next()) {
					_movimento.setCdOcorrencia(rsm.getInt("cd_ocorrencia"));
				}
			}
			
			if(isConnNull)
				connection.commit();
			
			return dto;
		} catch(SQLException sqlex) {
			if(isConnNull) {
				Conexao.rollback(connection);
			}
			System.out.println("Erro! AitMovimentoDocumentoServices.insert");
			sqlex.printStackTrace(System.out);
			throw sqlex;
		} catch(ValidationException ex) {
			if(isConnNull) {
				Conexao.rollback(connection);
			}
			System.out.println("Erro! AitMovimentoDocumentoServices.insert");
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			if(isConnNull) 
				Conexao.desconectar(connection);
		}
	}
	
	public DadosRetornoMG sendDetran(Documento documento, int cdAit, Connection connection) {
		connection = Conexao.conectar();
		
		int apresCondutor = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_APRESENTACAO_CONDUTOR", 0, 0, null);
		try {
			int movimentoMg = this.gerarMovimento(documento.getCdTipoDocumento(), null);
			Ait _ait = AitDAO.get(cdAit);
			AitMovimentoDocumento _amd = AitMovimentoDocumentoDAO.get(_ait.getCdAit(), documento.getCdDocumento());
			AitMovimento _movimento = AitMovimentoDAO.get(_amd.getCdMovimento(), _ait.getCdAit());
			
			if(documento.getCdTipoDocumento() == apresCondutor) {
				String nrCnhCondutor;
				ResultSetMap rsm = FormularioAtributoValorServices.getAllByDocumentoAtributo(documento.getCdDocumento(), "nrCnhCondutor");
				
				if(rsm.next()) {
					nrCnhCondutor = rsm.getString("txt_atributo_valor");
				} else {
					return null;
				}

				_ait.setNrCnhCondutor(nrCnhCondutor);
			}
			
			if (movimentoMg > 0) {
				boolean isProducao = ManagerConf.getInstance().getAsBoolean("PRODEMGE_PRODUCAO", false);
				ServicoDetran servicoDetran = ServicoDetranFactory.gerarServico(EstadoServices.getEstadoOrgaoAutuador().getSgEstado(), movimentoMg, isProducao);
				ServicoDetranObjeto retorno = servicoDetran.executar(new AitDetranObject(_ait, _movimento));
				return (DadosRetornoMG) retorno.getDadosRetorno();
			}
			
			return null;
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	public DadosRetornoMG sendDetranExterno(AitMovimentoDocumentoDTO dto) {
		return sendDetranExterno(dto, null);
	}
	
	public DadosRetornoMG sendDetranExterno(AitMovimentoDocumentoDTO dto, Connection connection) {
		connection = Conexao.conectar();
		
		int apresCondutor = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_APRESENTACAO_CONDUTOR", 0, 0, null);
		try {
			int movimentoMg = this.gerarMovimento(dto.getDocumento().getCdTipoDocumento(), null);
			Ait _ait = dto.getAit();
			AitMovimento _movimento = dto.getMovimento();
			
			if(dto.getDocumento().getCdTipoDocumento() == apresCondutor) {
				String nrCnhCondutor;
				ResultSetMap rsm = FormularioAtributoValorServices.getAllByDocumentoAtributo(dto.getDocumento().getCdDocumento(), "nrCnhCondutor");
				
				if(rsm.next())
					nrCnhCondutor = rsm.getString("txt_atributo_valor");
				else
					return null;

				_ait.setNrCnhCondutor(nrCnhCondutor);
			}
			
			if (movimentoMg > 0) {
				ServicoDetranRecurso servicoDetran = ServicoDetranExternoFactory.gerarServico("MG", movimentoMg, true);
				ServicoDetranObjeto retorno = servicoDetran.executarExterno(new ProtocoloExternoDetranObject(_ait, _movimento, dto.getDocumento()));
				return (DadosRetornoMG) retorno.getDadosRetorno();
			}
			
			return null;
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connection);
		}
	}
	
public ArrayList<AitMovimentoDocumentoDTO> findByRelator(int cdPessoa) {
		
		Connection connect = Conexao.conectar();
		ArrayList<AitMovimentoDocumentoDTO> _dtos = new ArrayList<AitMovimentoDocumentoDTO>();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT C.*, E.*, F.*, G.*, H.* " + 
					"FROM PTC_DOCUMENTO_PESSOA A " + 
					"JOIN MOB_AIT_MOVIMENTO_DOCUMENTO B ON (A.CD_DOCUMENTO = B.CD_DOCUMENTO) " + 
					"JOIN MOB_AIT_MOVIMENTO C ON (B.CD_AIT = C.CD_AIT AND B.CD_MOVIMENTO = C.CD_MOVIMENTO) " + 
					"JOIN GRL_PESSOA D ON (D.CD_PESSOA = A.CD_PESSOA) " + 
					"JOIN PTC_DOCUMENTO E ON (E.CD_DOCUMENTO = B.CD_DOCUMENTO) " + 
					"JOIN PTC_FASE F ON (E.CD_FASE = F.CD_FASE) " + 
					"JOIN GPN_TIPO_DOCUMENTO G ON (G.CD_TIPO_DOCUMENTO = E.CD_TIPO_DOCUMENTO) " + 
					"JOIN MOB_AIT H ON (H.CD_AIT = B.CD_AIT) " + 
					"WHERE D.CD_PESSOA = ? AND E.CD_FASE = ?");
			
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, FaseServices.getCdFaseByNome("Pendente", connect));
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			
			List<AitMovimento> _movimentos = new ResultSetMapper<AitMovimento> (_rsm, AitMovimento.class).toList();
			List<Documento> _documentos = new ResultSetMapper<Documento> (_rsm, Documento.class).toList();
			List<Fase> _fases = new ResultSetMapper<Fase> (_rsm, Fase.class).toList();
			List<TipoDocumento> _tiposDocumento = new ResultSetMapper<TipoDocumento> (_rsm, TipoDocumento.class).toList();
			List<Ait> _aits = new ResultSetMapper<Ait> (_rsm, Ait.class).toList();
			
			for (int i = 0; i < _rsm.getLines().size(); i++) {
				AitMovimentoDocumentoDTO _dto = new AitMovimentoDocumentoDTO();
				
				_dto.setMovimento(_movimentos.get(i));
				_dto.setDocumento(_documentos.get(i));
				_dto.setFase(_fases.get(i));
				_dto.setTipoDocumento(_tiposDocumento.get(i));
				_dto.setAit(_aits.get(i));
				
				_dtos.add(_dto);
			}
			
			return _dtos;
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}

	public ArrayList<AitMovimentoDocumentoDTO> findSemRelator() {
	
		Connection connect = Conexao.conectar();
		ArrayList<AitMovimentoDocumentoDTO> _dtos = new ArrayList<AitMovimentoDocumentoDTO>();
		
		int _jari = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_JARI", 0);
		int _fasePendente = FaseServices.getCdFaseByNome("Pendente", connect);
		
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.*, C.*, F.*, G.*, H.* " + 
					"FROM PTC_DOCUMENTO A " + 
					"JOIN MOB_AIT_MOVIMENTO_DOCUMENTO B ON (A.CD_DOCUMENTO = B.CD_DOCUMENTO)  " + 
					"JOIN MOB_AIT_MOVIMENTO C ON (B.CD_AIT = C.CD_AIT AND B.CD_MOVIMENTO = C.CD_MOVIMENTO)  " + 
					"JOIN PTC_FASE F ON (A.CD_FASE = F.CD_FASE)  " + 
					"JOIN GPN_TIPO_DOCUMENTO G ON (G.CD_TIPO_DOCUMENTO = A.CD_TIPO_DOCUMENTO)  " + 
					"JOIN MOB_AIT H ON (H.CD_AIT = B.CD_AIT)  " + 
					"WHERE A.CD_FASE = ? AND A.CD_TIPO_DOCUMENTO = ? AND A.CD_DOCUMENTO NOT IN ( " + 
					"	SELECT E.CD_DOCUMENTO " + 
					"	FROM PTC_DOCUMENTO_PESSOA A " + 
					"	JOIN PTC_DOCUMENTO E ON (E.CD_DOCUMENTO = A.CD_DOCUMENTO)  " + 
					"	WHERE E.CD_FASE = ? " + 
					")");
			
			pstmt.setInt(1, _fasePendente);
			pstmt.setInt(2, _jari);
			pstmt.setInt(3, _fasePendente);
			
			ResultSetMap _rsm = new ResultSetMap(pstmt.executeQuery());
			
			System.out.println(pstmt);
			
			List<AitMovimento> _movimentos = new ResultSetMapper<AitMovimento> (_rsm, AitMovimento.class).toList();
			List<Documento> _documentos = new ResultSetMapper<Documento> (_rsm, Documento.class).toList();
			List<Fase> _fases = new ResultSetMapper<Fase> (_rsm, Fase.class).toList();
			List<TipoDocumento> _tiposDocumento = new ResultSetMapper<TipoDocumento> (_rsm, TipoDocumento.class).toList();
			List<Ait> _aits = new ResultSetMapper<Ait> (_rsm, Ait.class).toList();
			
			for (int i = 0; i < _rsm.getLines().size(); i++) {
				AitMovimentoDocumentoDTO _dto = new AitMovimentoDocumentoDTO();
				
				_dto.setMovimento(_movimentos.get(i));
				_dto.setDocumento(_documentos.get(i));
				_dto.setFase(_fases.get(i));
				_dto.setTipoDocumento(_tiposDocumento.get(i));
				_dto.setAit(_aits.get(i));
				
				_dtos.add(_dto);
			}
			
			return _dtos;
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	private Documento insert(Documento documento, List<FormularioAtributoValor> camposFormulario, Connection connection) throws ValidationException {
		
		int cdDocumento = DocumentoDAO.insert(documento, connection);
		if(cdDocumento <= 0)		
			throw new ValidationException("Erro ao inserir documento. ");
		
		documento.setCdDocumento(cdDocumento);
		
		if(camposFormulario != null) {
			for (FormularioAtributoValor atributo : camposFormulario) {
				atributo.setCdDocumento(cdDocumento);
				int cod = FormularioAtributoValorDAO.insert(atributo, connection);
				if (cod <= 0) {
					Conexao.rollback(connection);
					throw new ValidationException("Erro ao inserir " + atributo.getTxtAtributoValor());
				}
				
				atributo.setCdFormularioAtributoValor(cod);
			}
		}
		
		return documento;
	}
	
	private Documento insertDocumentoExterno(Documento documento, List<FormularioAtributoValor> camposFormulario, Connection connection) throws ValidationException {
		
		documento.setCdFase(FaseServices.getCdFaseByNome("Pendente", connection));
		int cdDocumento = DocumentoDAO.insert(documento, connection);
		if(cdDocumento <= 0)		
			throw new ValidationException("Erro ao inserir documento. ");
		
		documento.setCdDocumento(cdDocumento);
		
		if(camposFormulario != null) {
			for (FormularioAtributoValor atributo : camposFormulario) {
				atributo.setCdDocumento(cdDocumento);
				int cod = FormularioAtributoValorDAO.insert(atributo, connection);
				if (cod <= 0) {
					Conexao.rollback(connection);
					throw new ValidationException("Erro ao inserir " + atributo.getTxtAtributoValor());
				}
				
				atributo.setCdFormularioAtributoValor(cod);
			}
		}
		
		return documento;
	}
	
	private String setCnhAit(List<FormularioAtributoValor> camposFormulario, Connection connect) {		
		for (FormularioAtributoValor valor : camposFormulario) {
			FormularioAtributo atributo = FormularioAtributoDAO.get(valor.getCdFormularioAtributo(), connect);
			
			if(atributo.getNmAtributo().equals("nrCnhCondutor")) {
				return valor.getTxtAtributoValor();
			}
		}
		
		return null;
	}
	
	private List<DocumentoArquivo> insert(List<DocumentoArquivo> arquivos, int cdDocumento, Connection connection) throws ValidationException {
		
		if(arquivos != null) {
			for (DocumentoArquivo arquivo : arquivos) {
				arquivo.setCdDocumento(cdDocumento);
				
				Result result = DocumentoArquivoServices.save(arquivo, true, connection);
				if(result.getCode() <= 0) {
					throw new ValidationException("Erro ao inserir arquivos.");
				}
				
				arquivo.setBlbArquivo(null);
			}
		}
		
		return arquivos;
	}
	
	private AitMovimento insert(AitMovimento movimento, Connection connection) throws ValidationException {
		
		Result result = AitMovimentoServices.save(movimento, null, connection);
		if(result.getCode() <= 0) {
			throw new ValidationException(result.getMessage());
		}
		
		movimento = (AitMovimento) result.getObjects().get("AITMOVIMENTO");
		
		movimento.setNrMovimento(movimento.getCdMovimento());
		AitMovimentoDAO.update(movimento, connection);
		
		return movimento;
	}
	
	private AitMovimentoDocumento insert(AitMovimentoDocumento amd, Connection connection) throws ValidationException {
		int r = AitMovimentoDocumentoDAO.insert(amd, connection);
		
		if(r <= 0)
			throw new ValidationException("Erro ao inserir AitMovimentoDocumento.");
		
		return amd;
	}
	
	private AitMovimento generate(Documento documento, int cdAit, AitMovimentoDocumentoDTO dto) {
		TipoDocumento td = TipoDocumentoDAO.get(documento.getCdTipoDocumento());	
		AitMovimento movimento = new AitMovimento();
		movimento.setCdMovimento(0);
		movimento.setCdAit(cdAit);
		movimento.setDtMovimento(dto.getDocumento().getDtProtocolo());
		movimento.setTpStatus(this.generateTpStatus(documento, Integer.parseInt(td.getIdTipoDocumento())));
		movimento.setDsObservacao(documento.getTxtDocumento());
		movimento.setDtDigitacao(new GregorianCalendar());
		movimento.setCdUsuario(documento.getCdUsuario());
		movimento.setTpArquivo(documento.getCdFase());
		movimento.setNrProcesso(generateNrProcesso(documento, dto));
		return movimento;
	}
	
	private void inserirCdOcorrencia(AitMovimentoDocumentoDTO dto, AitMovimento movimento, int cdFase) throws Exception {
		int apresCondutor = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_APRESENTACAO_CONDUTOR", 0, 0);
		int cdFaseDeferido = ParametroServices.getValorOfParametroAsInteger("CD_FASE_DEFERIDO", 0, 0, null);
		if(cdFase == cdFaseDeferido && dto.getDocumento().getCdTipoDocumento() == apresCondutor) {
			com.tivic.sol.search.Search<Ocorrencia> ocorrencia = searchOcorrencia();
			if(ocorrencia.getRsm().next()) {
				movimento.setCdOcorrencia(ocorrencia.getRsm().getInt("cd_ocorrencia"));
			}
		}
	}
	
	private com.tivic.sol.search.Search<Ocorrencia> searchOcorrencia() throws Exception {	
		SearchCriterios searchCriterios = criteriosOcorrencia();
		com.tivic.sol.search.Search<Ocorrencia> search = new SearchBuilder<Ocorrencia>("MOB_OCORRENCIA A")
				.searchCriterios(searchCriterios)
				.build();
		return search;
	}
	
	private SearchCriterios criteriosOcorrencia() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.tp_ocorrencia", OcorrenciaServices.TIPO_FICI, true);
		searchCriterios.addCriteriosEqualInteger("A.id_movimentacao", TipoParecerFici.PEDIDO_IDENTIFICACAO_ACEITO.getTipoParecerFici(), true);
		return searchCriterios;
	}
	
	private AitMovimento generateMovimentoExterno(Documento documento, AitMovimentoDocumentoDTO dto) {
		TipoDocumento td = TipoDocumentoDAO.get(documento.getCdTipoDocumento());
				
		AitMovimento movimento = new AitMovimento();
		movimento.setCdMovimento(0);
		movimento.setDtMovimento(documento.getDtProtocolo());
		movimento.setTpStatus(this.generateTpStatus(documento, Integer.parseInt(td.getIdTipoDocumento())));
		movimento.setDsObservacao(documento.getTxtDocumento());
		movimento.setDtDigitacao(new GregorianCalendar());
		movimento.setCdUsuario(documento.getCdUsuario());
		movimento.setTpArquivo(documento.getCdFase());
		movimento.setNrProcesso(documento.getNrDocumento());
		
		return movimento;
	}
	
	private String generateNrProcesso(Documento documento, AitMovimentoDocumentoDTO dto) {	
		 if(dto.getMovimento().getNrProcesso() != null) {
			 return dto.getMovimento().getNrProcesso();
		 }else {
			 if(documento.getNrDocumento().length() <= 16)
					return documento.getNrDocumentoExterno();
				else
					return documento.getNrDocumento();
		 }
	}
	
	private int generateTpStatus(Documento documento, int tpDocumento) {
		
		if(tpDocumento == AitMovimentoServices.DEFESA_PREVIA) {
			if(documento.getCdFase() == FaseServices.getCdFaseByNome("Pendente", null))
				return AitMovimentoServices.DEFESA_PREVIA;
			
			if(documento.getCdFase() == FaseServices.getCdFaseByNome("Deferido", null))
				return AitMovimentoServices.DEFESA_DEFERIDA;
			
			if(documento.getCdFase() == FaseServices.getCdFaseByNome("Indeferido", null))
				return AitMovimentoServices.DEFESA_INDEFERIDA;			
		}
		
		if(tpDocumento == AitMovimentoServices.RECURSO_JARI) {
			if(documento.getCdFase() == FaseServices.getCdFaseByNome("Pendente", null))
				return AitMovimentoServices.RECURSO_JARI;
			
			if(documento.getCdFase() == FaseServices.getCdFaseByNome("Deferido", null))
				return AitMovimentoServices.JARI_COM_PROVIMENTO;
			
			if(documento.getCdFase() == FaseServices.getCdFaseByNome("Indeferido", null))
				return AitMovimentoServices.JARI_SEM_PROVIMENTO;
		}
		
		if(tpDocumento == AitMovimentoServices.RECURSO_CETRAN) {
			if(documento.getCdFase() == FaseServices.getCdFaseByNome("Pendente", null))
				return AitMovimentoServices.RECURSO_CETRAN;
			
			if(documento.getCdFase() == FaseServices.getCdFaseByNome("Deferido", null))
				return AitMovimentoServices.CETRAN_DEFERIDO;
			
			if(documento.getCdFase() == FaseServices.getCdFaseByNome("Indeferido", null))
				return AitMovimentoServices.CETRAN_INDEFERIDO;
		}
		
		if(tpDocumento == AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA) {
			if(documento.getCdFase() == FaseServices.getCdFaseByNome("Pendente", null))
				return AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA;
			
			if(documento.getCdFase() == FaseServices.getCdFaseByNome("Deferido", null))
				return AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA;
			
			if(documento.getCdFase() == FaseServices.getCdFaseByNome("Indeferido", null))
				return AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA;			
		}
		
		return tpDocumento;
	}
	
	private Ait setCamposAit (Ait ait, FormularioAtributo atributo, FormularioAtributoValor valor) {
		
		switch(atributo.getNmAtributo()) {
			case "nrCnhCondutor":
				ait.setNrCnhCondutor(valor.getTxtAtributoValor());
				break;
				
			case "ufCnhCondutor":
				ait.setUfCnhCondutor(valor.getTxtAtributoValor());
				break;
				
//			case "tpCnhCondutor": 
//				ait.setTpCnhCondutor(Integer.parseInt(valor.getTxtAtributoValor()));
//				break;
				
			case "nmCondutor":
				ait.setNmCondutor(valor.getTxtAtributoValor());
				break;
				
			case "dsEnderecoCondutor":
				ait.setDsEnderecoCondutor(valor.getTxtAtributoValor());
				break;
				
			case "nrEnderecoCondutor":
				ait.setDsEnderecoCondutor(ait.getDsEnderecoCondutor()+" "+valor.getTxtAtributoValor());
				break;
				
			case "dsComplementoEnderecoCondutor":
				ait.setDsComplementoCondutor(valor.getTxtAtributoValor());
				break;
			
			/*Retornar cdCidade ao invés de nmCidade em protocolo
			case "nmCidadeCondutor":
				ait.setCdCidadeCondutor(Integer.parseInt(valor.getTxtAtributoValor()));
				break;*/
				
			/*case "nrTelefone1Condutor":
				ait.setNrTelefone(valor.getTxtAtributoValor());
				break;*/
				
			case "nrCpfCondutor":
				ait.setNrCpfCondutor(valor.getTxtAtributoValor());
				break;			
			
		}
		
		//TODO: 
		//ait.setTpCnhCondutor(AitServices.TP_CNH_NOVA);
		
		return ait;
	}
	
	@Deprecated
	public List<AitMovimentoDocumentoDTO> search(Criterios criterios) throws Exception {
		return search(criterios, null);
	}

	@Deprecated
	public List<AitMovimentoDocumentoDTO> search(Criterios criterios, Connection connection) throws Exception {
		boolean isConnNull = connection == null;
		try {
			if(isConnNull)
				connection = Conexao.conectar();
			
			List<AitMovimentoDocumentoDTO> list = new ArrayList<AitMovimentoDocumentoDTO>();
			
			String sql = "SELECT A.*, B.*, C.* "
					+ " FROM ptc_documento A"
					+ " JOIN mob_ait_movimento_documento B ON (A.cd_documento = B.cd_documento)"
					+ " JOIN mob_ait_movimento C ON (B.cd_ait = C.cd_ait AND B.cd_movimento = C.cd_movimento)"
					+ " JOIN mob_ait D ON (B.cd_ait = D.cd_ait)"
					+ " WHERE 1=1";
			
			ResultSetMap rsm = Search.find(sql, criterios, connection, isConnNull);
			List<Documento> documentos = new ResultSetMapper<Documento>(rsm, Documento.class).toList();
			List<AitMovimento> movimentos = new ResultSetMapper<AitMovimento>(rsm, AitMovimento.class).toList();
			
			for(int i = 0; i < rsm.getLines().size(); i++) {
				Documento documento = documentos.get(i);
				AitMovimento movimento = movimentos.get(i);
				
				AitMovimentoDocumentoDTO dto = new AitMovimentoDocumentoDTO();
				dto.setDocumento(documento);
				dto.setMovimento(movimento);
				
				list.add(dto);
			}
			
			return list;
		} catch(Exception ex) {
			System.out.println("Erro! AitMovimentoDocumentoServices.search");
			ex.printStackTrace(System.out);
			throw ex;
		}
	}
	
	public Object getAtributoByMovimento(AitMovimento aitMovimento, String nmAtributo) throws ValidacaoException {
	
		AitMovimentoDocumento aitMovimentoDocumento = null;
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_ait", String.valueOf(aitMovimento.getCdAit()), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("cd_movimento", String.valueOf(aitMovimento.getCdMovimento()), ItemComparator.EQUAL, Types.INTEGER));
		System.out.println(criterios.toString());
		
		ResultSetMap rsm = AitMovimentoDocumentoDAO.find(criterios);
		System.out.println(rsm.toString());
		
		if(rsm.next()) {
			aitMovimentoDocumento = AitMovimentoDocumentoDAO.get(rsm.getInt("cd_ait"), rsm.getInt("cd_movimento"), rsm.getInt("cd_documento"));
		}
		
		if(aitMovimentoDocumento == null)
			throw new ValidacaoException("Movimento sem documento");
		
		ResultSetMap rsmValor = FormularioAtributoValorServices.getAllByDocumentoAtributo(aitMovimentoDocumento.getCdDocumento(), nmAtributo);
		if(rsmValor.next()) {
			return rsmValor.getObject("txt_atributo_valor");
		}
		
		return "";
	}
	
	public byte[] getProtocolo(int cdAit, int cdDocumento) {
		return getProtocolo(cdAit, cdDocumento, null);
	}
	
	public byte[] getProtocolo(int cdAit, int cdDocumento, Connection connect) {
		boolean isConnectionNull = (connect == null);
		Documento _documento = DocumentoDAO.get(cdDocumento);
		
		try {
			
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			String reportName;
			
			int tpParametro = ParametroServices.getValorOfParametroAsInteger("MOB_LG_IMPRIMIR_PROTOCOLO_GERAL", 0);
			
			if(tpParametro != 1) {
				reportName = "mob/protocolo";
			} else {
				Fase fase = FaseServices.getFaseByNome("Pendente", null);
				
				if(_documento.getCdFase() == fase.getCdFase())
					reportName = "mob/protocolo_2_vias";
				else
					reportName = "mob/protocolo_julgamento";
			}
			
			HashMap<String, Object> params = getParamsPtc(cdAit, cdDocumento, connect);
			
			ResultSetMap rsm = new ResultSetMap();
			rsm.addRegister(new HashMap<String, Object>());
			
			byte[] print = new byte[] {};
			
			if(params != null) {
				print = ReportServices.getPdfReport(reportName, params, rsm);
				System.out.println("Filled");
			}else{
				System.out.println("Continued empty");
			}
			
			return print;
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public byte[] getProtocoloExterno(int cdDocumento) {
		return getProtocoloExterno(cdDocumento, null);
	}
	
	public byte[] getProtocoloExterno(int cdDocumento, Connection connect) {
		
		boolean isConnectionNull = (connect == null);
		Documento _documento = DocumentoDAO.get(cdDocumento);
		
		try {
			
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			String reportName;
			
			int tpParametro = ParametroServices.getValorOfParametroAsInteger("MOB_LG_IMPRIMIR_PROTOCOLO_GERAL", 0);
			
			reportName = "mob/protocolo_externo";
			
			HashMap<String, Object> params = getParamsPtcExterno(cdDocumento, connect);
			
			ResultSetMap rsm = new ResultSetMap();
			rsm.addRegister(new HashMap<String, Object>());
			
			byte[] print = new byte[] {};
			
			if(params != null) {
				print = ReportServices.getPdfReport(reportName, params, rsm);
				System.out.println("Filled");
			}else{
				System.out.println("Continued empty");
			}
			
			return print;
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public HashMap<String, Object> getParamsPtcExterno(int cdDocumento, Connection connect) {
		
		boolean isConnectionNull = (connect == null);
		
		try {
			
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			HashMap<String, Object> params = new HashMap<>();
			Documento doc = DocumentoDAO.get(cdDocumento); // DocumentoServices.getDocumentoById(cdDocumento);
			Fase fase = FaseDAO.get(doc.getCdFase());
			FormularioAtributoValorServices.getAllByDocumentoAtributo(cdDocumento, "id_ait");
			
			params.put("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", "LOREM IPSUM DOLOR SIT AMET", connect));
			params.put("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", "CONSECTETUR ADIPISCING ELIT", connect));
			params.put("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", "INCIDIDUNT UT LABORE ET DOLORE", connect));
			params.put("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE", "+55 (99) 9999-9999", connect));
			params.put("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL", "email@dominio.com.br", connect));
			params.put("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
			params.put("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
			params.put("NM_REQUERENTE", FormularioAtributoValorServices.getAtributoByNmAtributo(cdDocumento, "nmRequerente", 101).getTxtAtributoValor());			
			params.put("NM_TITULO", com.tivic.manager.ptc.TipoDocumentoServices.getTxtTipoDocumento(doc.getCdTipoDocumento(), connect));
			params.put("DT_PROTOCOLO", this.convDate(doc.getDtProtocolo()));
			params.put("DS_DT_PROTOCOLO", Util.formatDate(doc.getDtProtocolo(), "dd/MM/yyyy"));
			params.put("NR_AIT", FormularioAtributoValorServices.getAtributoByNmAtributo(cdDocumento, "idAit", 101).getTxtAtributoValor());			
			params.put("NR_DOCUMENTO", FormularioAtributoValorServices.getAtributoByNmAtributo(cdDocumento, "nrDocumento", 101).getTxtAtributoValor());			
			params.put("NR_PLACA", FormularioAtributoValorServices.getAtributoByNmAtributo(cdDocumento, "nrPlaca", 101).getTxtAtributoValor());
			params.put("NM_FASE", fase.getNmFase());
			
			return params;
			
			
		} catch(Exception e) {	
			e.printStackTrace(System.out);
			return null;
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}		
		
	}
	
	public HashMap<String, Object> getParamsPtc(int cdAit, int cdDocumento, Connection connect) {
		
		boolean isConnectionNull = (connect == null);
		
		try {
			
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			HashMap<String, Object> params = new HashMap<>();
			Documento doc = DocumentoDAO.get(cdDocumento); // DocumentoServices.getDocumentoById(cdDocumento);
			Ait ait = this.getAit(cdAit);
			Fase fase = FaseDAO.get(doc.getCdFase());
			
			params.put("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", "LOREM IPSUM DOLOR SIT AMET", connect));
			params.put("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", "CONSECTETUR ADIPISCING ELIT", connect));
			params.put("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", "INCIDIDUNT UT LABORE ET DOLORE", connect));
			params.put("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE", "+55 (99) 9999-9999", connect));
			params.put("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL", "email@dominio.com.br", connect));
			params.put("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
			params.put("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
			params.put("NM_REQUERENTE", this.getNmRequerente(doc));			
			params.put("NM_TITULO", com.tivic.manager.ptc.TipoDocumentoServices.getTxtTipoDocumento(doc.getCdTipoDocumento(), connect));
			params.put("DT_PROTOCOLO", this.convDate(doc.getDtProtocolo()));
			params.put("DS_DT_PROTOCOLO", Util.formatDate(doc.getDtProtocolo(), "dd/MM/yyyy"));
			params.put("NR_AIT", ait.getIdAit());			
			params.put("NR_DOCUMENTO", doc.getNrDocumento());			
			params.put("NR_PLACA", ait.getNrPlaca());
			params.put("NM_FASE", fase.getNmFase());
			
			return params;
			
			
		} catch(Exception e) {	
			e.printStackTrace(System.out);
			return null;
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}		
		
	}
	
	public AitMovimentoDocumentoDTO getDocumentoDto(int cdDocumento) { 
		return getDocumentoDto(cdDocumento, null);
	}
	
	public AitMovimentoDocumentoDTO getDocumentoDto(int cdDocumento, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		AitMovimentoDocumentoDTO documentoDto = new AitMovimentoDocumentoDTO();
		
		if(isConnectionNull)
			connect = Conexao.conectar();

		try {
			documentoDto.setDocumento(DocumentoDAO.get(cdDocumento));
			documentoDto.setArquivosAnexados(DocumentoArquivoDAO.getArquivosDocumento(cdDocumento));
			documentoDto.setCamposFormulario(this.convCamposFormulario(FormularioAtributoValorServices.getAtributosFormulario(cdDocumento)));
			documentoDto.setMovimento(this.getMovimentoDocumento(cdDocumento));
			documentoDto.setDocumentoSuperior(this.getAtaJari(documentoDto.getDocumento()));
			documentoDto.setDocumentoPessoa(this.getRelatorJari(cdDocumento));
			
			return documentoDto;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public Result cancelDocumento(AitMovimentoDocumentoDTO _dto) {
		return cancelDocumento(_dto, null);
	}
	
	public Result cancelDocumento(AitMovimentoDocumentoDTO _dto, Connection conn) {
		
		boolean isConnNull = (conn == null);
		boolean recursoEnviado = _dto.getMovimento().getLgEnviadoDetran() == AitMovimentoServices.ENVIADO_AO_DETRAN;
		
		if(isConnNull)
			conn = Conexao.conectar();
		
		try {
			_dto.getMovimento().setLgEnviadoDetran(AitMovimentoServices.REGISTRO_CANCELADO);
			AitMovimentoDAO.update(_dto.getMovimento());
			
			_dto.getDocumento().setCdSituacaoDocumento(DocumentoServices.ST_ARQUIVO_CANCELADO);
			DocumentoDAO.update(_dto.getDocumento());
			
			AitMovimento movimentoCancelamento = (AitMovimento) _dto.getMovimento().clone();
			movimentoCancelamento.setTpStatus(this.getTpStatusCancelamento(_dto.getMovimento()));
			movimentoCancelamento.setCdUsuario(_dto.getUsuario().getCdUsuario());
			movimentoCancelamento.setDtMovimento(new GregorianCalendar());
			movimentoCancelamento.setLgEnviadoDetran(AitMovimentoServices.NAO_ENVIADO);
			AitMovimentoDAO.insert(movimentoCancelamento);
			
			if(recursoEnviado) {
				boolean isProducao = ManagerConf.getInstance().getAsBoolean("PRODEMGE_PRODUCAO", false);
				
				ServicoDetran servicoDetran = ServicoDetranFactory.gerarServico(EstadoServices.getEstadoOrgaoAutuador().getSgEstado(), movimentoCancelamento.getTpStatus(), isProducao);
				DadosRetornoMG retorno = (DadosRetornoMG )servicoDetran.executar(new AitDetranObject(_dto.getAit(), movimentoCancelamento)).getDadosRetorno();
				if(retorno.getCodigoRetorno() == 0)
					return new Result(1, "Movimento cancelado na base estadual !");
				else 
					return new Result(-1, "Erro ao cancelar auto na base estadual, reenvie na tela de remessas");
			}
			
			return new Result(1, "Movimento cancelado com sucesso !");
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnNull)
				Conexao.desconectar(conn);
		}
	}
	
	public AitMovimento getMovimentoDocumento(int cdDocumento) {
		return getMovimentoDocumento(cdDocumento, null);
	}
	
	public AitMovimento getMovimentoDocumento(int cdDocumento, Connection connect) {
		boolean isConnectionNull = (connect == null);
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			System.out.println(cdDocumento);
			AitMovimentoDocumento aitMovDocumento = AitMovimentoDocumentoDAO.getByDocumento(cdDocumento);
			AitMovimento aitMovimento = AitMovimentoDAO.get(aitMovDocumento.getCdMovimento(), aitMovDocumento.getCdAit());
			
			return aitMovimento;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public List<AitMovimentoDocumentoDTO> find(Criterios criterios) throws Exception {
		return find(criterios, null);
	}
	
	public List<AitMovimentoDocumentoDTO> find(Criterios criterios, Connection connection) throws Exception {
		boolean isConnNull = connection == null;
		try {
			
			if(isConnNull)
				connection = Conexao.conectar();
			
			List<AitMovimentoDocumentoDTO> list = new ArrayList<AitMovimentoDocumentoDTO>();
			
			String sql = "SELECT A.*, B.*, C.*, D.*, E.*, F.*"
					+ " FROM ptc_documento A"
					+ " JOIN mob_ait_movimento_documento B ON (A.cd_documento = B.cd_documento)"
					+ " JOIN mob_ait_movimento C ON (B.cd_ait = C.cd_ait AND B.cd_movimento = C.cd_movimento)"
					+ " JOIN mob_ait D ON (C.cd_ait = D.cd_ait)"
					+ " JOIN mob_agente E ON (D.cd_agente = E.cd_agente)"
					+ " JOIN seg_usuario F ON (A.cd_usuario = F.cd_usuario)"
					+ " WHERE 1=1";
			
			ResultSetMap rsm = Search.find(sql, " ORDER BY A.dt_protocolo ", criterios, connection, isConnNull);
			List<Documento> documentos = new ResultSetMapper<Documento>(rsm, Documento.class).toList();
			List<AitMovimento> movimentos = new ResultSetMapper<AitMovimento>(rsm, AitMovimento.class).toList();
			List<Ait> aits = new ResultSetMapper<Ait>(rsm, Ait.class).toList();
			List<Agente> agentes = new ResultSetMapper<Agente>(rsm, Agente.class).toList();
			List<Usuario> usuarios = new ResultSetMapper<Usuario>(rsm, Usuario.class).toList();
			
			
			for(int i = 0; i < rsm.getLines().size(); i++) {

				AitMovimentoDocumentoDTO dto = new AitMovimentoDocumentoDTO();
				
				Documento documento = documentos.get(i);
				dto.setDocumento(documento);
								
				AitMovimento movimento = movimentos.get(i);
				dto.setMovimento(movimento);
				
				Ait ait = aits.get(i);
				dto.setAit(ait);
				
				Fase fase = FaseDAO.get(documento.getCdFase());
				dto.setFase(fase);
				
				TipoDocumento tipoDocumento = TipoDocumentoDAO.get(documento.getCdTipoDocumento());
				dto.setTipoDocumento(tipoDocumento);
				
				Agente agente = agentes.get(i);
				dto.setAgente(agente);
				
				Usuario usuario = usuarios.get(i);
				dto.setUsuario(usuario);
				
				list.add(dto);
			}
			
			return list;
		} catch(Exception ex) {
			System.out.println("Erro! AitMovimentoDocumentoServices.search");
			ex.printStackTrace(System.out);
			throw ex;
		}
	}
	
	public byte[] getReport(Criterios criterios, Connection connection) throws Exception {
		boolean isConnNull = connection == null;
		try {
			
			if(isConnNull)
				connection = Conexao.conectar();
			
			String sql = " SELECT A.*, B.*, C.*, D.* "
					+ " FROM ptc_documento A"
					+ " JOIN mob_ait_movimento_documento B ON (A.cd_documento = B.cd_documento)"
					+ " JOIN mob_ait_movimento C ON (B.cd_ait = C.cd_ait AND B.cd_movimento = C.cd_movimento)"
					+ " JOIN mob_ait D ON (C.cd_ait = D.cd_ait)"
					+ " WHERE 1=1";
			ResultSetMap rsm = Search.find(sql, " ORDER BY A.dt_protocolo ", criterios, connection, false);
			
			while(rsm.next()) {
				Fase fase = FaseDAO.get(rsm.getInt("cd_fase"));
				rsm.setValueToField("NM_FASE", fase!=null?fase.getNmFase():"");
				
				rsm.setValueToField("DS_DT_INFRACAO", Util.formatDate(rsm.getGregorianCalendar("dt_infracao"), "dd/MM/yyyy 'às' HH:mm"));
				rsm.setValueToField("DS_DT_PROTOCOLO", Util.formatDate(rsm.getGregorianCalendar("dt_protocolo"), "dd/MM/yyyy"));
				
				TipoDocumento tipoDocumento = TipoDocumentoDAO.get(rsm.getInt("cd_tipo_documento"));
				rsm.setValueToField("NM_TIPO_DOCUMENTO", (tipoDocumento!=null ? tipoDocumento.getNmTipoDocumento() : ""));
				
				Infracao infracao = InfracaoDAO.get(rsm.getInt("cd_infracao"));
				if(infracao != null) {
					String dsInfracao = infracao.getNrCodDetran()+" - "+infracao.getDsInfracao();
					rsm.setValueToField("DS_INFRACAO", dsInfracao);
				}		
				
				rsm.setValueToField("NM_RELATOR", "Fulano de Tal");
			}
			rsm.beforeFirst();
			
			HashMap<String, Object> params =  new HashMap<String, Object>();			
			params.put("DS_TITULO_1", ParametroServices.getValorOfParametroAsString("DS_TITULO_1", "PREFEITURA MUNICIPAL DE TEÓFILO OTONI"));
			params.put("DS_TITULO_2", ParametroServices.getValorOfParametroAsString("DS_TITULO_2", "SECRETARIA MUNICIPAL DE PLANEJAMENTO"));
			params.put("DS_TITULO_3", ParametroServices.getValorOfParametroAsString("DS_TITULO_3", "DIVISÃO DE TRÂNSITO E TRANSPORTE"));
			params.put("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
			params.put("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
			params.put("DS_EMISSAO", "Emitido em "+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy 'às' HH:mm"));
			
			
			byte[] print = ReportServices.getPdfReport("mob/relatorio_protocolo", params, rsm);	
			
			return print;
			
		}  catch(Exception ex) {
			System.out.println("Erro! AitMovimentoDocumentoServices.getReport");
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			if(isConnNull)
				Conexao.desconectar(connection);
		}
	}
	
	public AitMovimentoDocumentoDTO updateDocumento(int cdDocumento, int cdTipoDocumento, int cdFase, AitMovimentoDocumentoDTO documento) throws ValidationException {
		try {
			Optional<String> error = checkCanceladoRegistrado(documento.getMovimento());
			if(error.isPresent())
				throw new ValidationException(error.get());
			
			Documento _documento = DocumentoDAO.get(cdDocumento);
			AitMovimentoDocumento _movimento = AitMovimentoDocumentoDAO.getByDocumento(cdDocumento);
			Ait ait = AitDAO.get(_movimento.getCdAit());
			
			this.updateFaseDocumento(cdFase, cdTipoDocumento, cdDocumento);
			this.updateFields(documento.getCamposFormulario(), cdDocumento);			
			this.updateAitFiciDeferido(this.convCamposFormulario(FormularioAtributoValorServices.getAtributosFormulario(cdDocumento)), cdFase, ait);
			this.updateRelatorJari(documento);
			documento.getDocumento().setCdDocumentoSuperior(this.updateAtaJari(documento.getDocumentoSuperior()));
			
			AitMovimento _movGerado = this.generate(_documento, _movimento.getCdAit(), documento);
			inserirCdOcorrencia(documento, _movGerado, cdFase);
			_movGerado.setCdMovimento(documento.getMovimento().getCdMovimento());
			_movGerado.setDtMovimento(documento.getDocumento().getDtProtocolo());
			AitMovimento _novoMovimento = this.insert(_movGerado, null);
			
			AitMovimentoDocumento _movDocumento = new AitMovimentoDocumento(_movimento.getCdAit(), _novoMovimento.getNrMovimento(), _documento.getCdDocumento());
			AitMovimentoDocumentoDAO.update(_movDocumento);
			
			documento.getMovimento().setCdAit(_movDocumento.getCdAit());
			documento.getMovimento().setCdMovimento(_movDocumento.getCdMovimento());
			documento.getDocumento().setCdDocumento(_movDocumento.getCdDocumento());

			DocumentoDAO.update(documento.getDocumento());
			
			return documento;
		} catch(ValidationException ve) { 
			System.out.println("Erro! AitMovimentoDocumentoServices.update");
			ve.printStackTrace(System.out);
			throw ve;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	private Optional<String> checkCanceladoRegistrado(AitMovimento _movimento) {
		if(_movimento.getLgEnviadoDetran() == AitMovimentoServices.ENVIADO_AO_DETRAN)
			return Optional.of("Não é possível editar protocolos registrados no DETRAN.");
		
		if(_movimento.getLgEnviadoDetran() == AitMovimentoServices.NAO_ENVIAR)
			return Optional.of("Não é possível editar protocolos cancelados.");
		
		return Optional.empty();
	}
	
	private void updateRelatorJari(AitMovimentoDocumentoDTO documento) {
		if(documento.getDocumentoPessoa() != null && documento.getDocumentoPessoa().getCdPessoa() > 0) {
			DocumentoPessoa relator = documento.getDocumentoPessoa();
			relator.setCdDocumento(documento.getDocumento().getCdDocumento());

			DocumentoPessoaServices.updateRelatorJari(relator);
		}
	}
	
	private int updateAtaJari(Documento ata) {
		if(ata == null || ata.getNrDocumento() == null)
			return 0;
		
		Documento docValid = DocumentoServices.getByNrDocumento(ata.getNrDocumento());
		
		if(docValid != null) {
			return docValid.getCdDocumento();
		}
		
		ata.setCdDocumento(0);
		Documento nAta = this.insertAta(ata);
		
		return nAta.getCdDocumento();
	}
	
	private void updateAitFiciDeferido(List<FormularioAtributoValor> campos, int cdFase, Ait ait) {
		int cdFaseDeferido = ParametroServices.getValorOfParametroAsInteger("CD_FASE_DEFERIDO", 0, 0, null);
		if(cdFase == cdFaseDeferido) {
			
			for(FormularioAtributoValor valor : campos) {
				FormularioAtributo atributo = FormularioAtributoDAO.get(valor.getCdFormularioAtributo(), null);
				ait = this.setCamposAit(ait, atributo, valor);
			}
			AitDAO.update(ait);
		}
	}
	
	public List<Documento> getHistoricoAit(int cdAit) {
		Connection connect = Conexao.conectar();
		
		try {			
			PreparedStatement pstmt = connect.prepareStatement(
					  "SELECT B.CD_DOCUMENTO, B.NR_DOCUMENTO, B.CD_TIPO_DOCUMENTO, B.CD_FASE "
					+ "FROM MOB_AIT_MOVIMENTO_DOCUMENTO A INNER JOIN PTC_DOCUMENTO B ON (A.CD_DOCUMENTO = B.CD_DOCUMENTO) "
					+ "WHERE A.CD_AIT = ? ORDER BY A.CD_MOVIMENTO ASC");
			
			pstmt.setInt(1, cdAit);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				ResultSetMapper<Documento> rsmConv = new ResultSetMapper<Documento>(rsm, Documento.class);
				return rsmConv.toList();
			}
			
			return null;
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	public ResultSetMap getResultadosAta(String nrAta) {
		Connection connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT C.DT_MOVIMENTO, A.NR_DOCUMENTO, D.ID_AIT, D.NR_PLACA, E.NM_FASE, F.DT_PROTOCOLO, F.CD_DOCUMENTO FROM PTC_DOCUMENTO A " +
					"JOIN MOB_AIT_MOVIMENTO_DOCUMENTO B ON (A.CD_DOCUMENTO = B.CD_DOCUMENTO) " +
					"JOIN MOB_AIT_MOVIMENTO C ON (B.CD_MOVIMENTO = C.CD_MOVIMENTO AND B.CD_AIT = C.CD_AIT) " +
					"JOIN MOB_AIT D ON (C.CD_AIT = D.CD_AIT) JOIN PTC_FASE E ON (A.CD_FASE = E.CD_FASE) " + 
					"JOIN PTC_DOCUMENTO F ON (A.CD_DOCUMENTO_SUPERIOR = F.CD_DOCUMENTO) " +
					"WHERE F.NR_DOCUMENTO = ?"
					);
			
			pstmt.setString(1, nrAta);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				return rsm;
			}
			
			return null;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
		
	}
	
	public byte[] getBoletimJari(String nrAta, Connection connect) throws Exception {
		boolean isConnNull = connect == null;
		try {
			
			if(isConnNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT C.DT_MOVIMENTO, A.NR_DOCUMENTO, D.ID_AIT, D.NR_PLACA, E.NM_FASE, F.DT_PROTOCOLO FROM PTC_DOCUMENTO A " +
					"JOIN MOB_AIT_MOVIMENTO_DOCUMENTO B ON (A.CD_DOCUMENTO = B.CD_DOCUMENTO) " +
					"JOIN MOB_AIT_MOVIMENTO C ON (B.CD_MOVIMENTO = C.CD_MOVIMENTO AND B.CD_AIT = C.CD_AIT) " +
					"JOIN MOB_AIT D ON (C.CD_AIT = D.CD_AIT) JOIN PTC_FASE E ON (A.CD_FASE = E.CD_FASE) " + 
					"JOIN PTC_DOCUMENTO F ON (A.CD_DOCUMENTO_SUPERIOR = F.CD_DOCUMENTO) " +
					"WHERE F.NR_DOCUMENTO = ?"
					);
			
			pstmt.setString(1, nrAta);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			rsm.beforeFirst();
			
			HashMap<String, Object> params =  new HashMap<String, Object>();			

			params.put("DATA_ATA", new Timestamp(new GregorianCalendar().getTimeInMillis()));
			params.put("NR_ATA", nrAta);
			params.put("QTD_REGISTROS", rsm.getLines().size());
			params.put("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", connect));
			params.put("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE", connect));
			params.put("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", connect));
			params.put("NM_DIRETOR_JARI", ParametroServices.getValorOfParametro("NM_DIRETOR_JARI", connect));
			
			byte[] print = ReportServices.getPdfReport("mob/Boletim_Jari", params, rsm);	
			
			return print;
			
		}  catch(Exception ex) {
			System.out.println("Erro! AitMovimentoDocumentoServices.getBoletimJari");
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}
	
	public ArrayList<AitMovimentoDocumentoDTO> findProcessos(Criterios crt) throws Exception {
		return findProcessos(crt, null);
	}
	
	public ArrayList<AitMovimentoDocumentoDTO> findProcessos(Criterios crt, Connection connect) throws Exception {
		boolean isConnNull = connect == null;
		ArrayList<AitMovimentoDocumentoDTO> _dtos = new ArrayList<AitMovimentoDocumentoDTO>();
		try {
			
			if(isConnNull)
				connect = Conexao.conectar();
			
			String statement = "SELECT A.*, B.*, C.*, D.*, E.*, F.* "
					+ "FROM MOB_AIT_MOVIMENTO A "
					+ "JOIN MOB_AIT B ON (A.CD_AIT = B.CD_AIT) "
					+ "JOIN MOB_AIT_MOVIMENTO_DOCUMENTO C ON (C.CD_AIT = B.CD_AIT AND A.CD_MOVIMENTO = C.CD_MOVIMENTO) "
					+ "JOIN PTC_DOCUMENTO D ON (C.CD_DOCUMENTO = D.CD_DOCUMENTO) "
					+ "JOIN GPN_TIPO_DOCUMENTO E ON (E.CD_TIPO_DOCUMENTO = D.CD_TIPO_DOCUMENTO) "
					+ "JOIN PTC_FASE F ON (F.CD_FASE = D.CD_FASE)";

						
			ResultSetMap rsm = Search.find(statement, " ORDER BY C.CD_AIT ", crt, connect, false);
			
			List<Documento> documentos = new ResultSetMapper<Documento>(rsm, Documento.class).toList();
			List<AitMovimento> movimentos = new ResultSetMapper<AitMovimento>(rsm, AitMovimento.class).toList();
			List<Ait> aits = new ResultSetMapper<Ait>(rsm, Ait.class).toList();
			List<TipoDocumento> tiposDocumento = new ResultSetMapper<TipoDocumento>(rsm, TipoDocumento.class).toList();
			List<Fase> fases = new ResultSetMapper<Fase>(rsm, Fase.class).toList();
			
			
			for(int i = 0; i < rsm.getLines().size(); i++) {

				AitMovimentoDocumentoDTO dto = new AitMovimentoDocumentoDTO();
				
				Documento documento = documentos.get(i);
				dto.setDocumento(documento);
								
				AitMovimento movimento = movimentos.get(i);
				dto.setMovimento(movimento);
				
				Ait ait = aits.get(i);
				dto.setAit(ait);
				
				Fase fase = fases.get(i);
				dto.setFase(fase);
				
				TipoDocumento tipoDocumento = tiposDocumento.get(i);
				dto.setTipoDocumento(tipoDocumento);
				
				_dtos.add(dto);
			}
			
			return _dtos;
			
		}  catch(Exception ex) {
			System.out.println("Erro! AitMovimentoDocumentoServices.getBoletimJari");
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}
	
	public byte[] printRelatorioProcessos(Criterios crt, Connection connect) throws Exception {
		boolean isConnNull = connect == null;
		try {
			
			if(isConnNull)
				connect = Conexao.conectar();
			
			String statement = "SELECT B.ID_AIT, B.NR_PLACA, D.NR_DOCUMENTO, A.DT_MOVIMENTO, D.DT_PROTOCOLO, B.DT_INFRACAO, E.NM_TIPO_DOCUMENTO, F.NM_FASE "
					+ "FROM MOB_AIT_MOVIMENTO A "
					+ "JOIN MOB_AIT B ON (A.CD_AIT = B.CD_AIT) "
					+ "JOIN MOB_AIT_MOVIMENTO_DOCUMENTO C ON (C.CD_AIT = B.CD_AIT AND A.CD_MOVIMENTO = C.CD_MOVIMENTO) "
					+ "JOIN PTC_DOCUMENTO D ON (C.CD_DOCUMENTO = D.CD_DOCUMENTO) "
					+ "JOIN GPN_TIPO_DOCUMENTO E ON (E.CD_TIPO_DOCUMENTO = D.CD_TIPO_DOCUMENTO) "
					+ "JOIN PTC_FASE F ON (F.CD_FASE = D.CD_FASE)";

						
			ResultSetMap rsm = Search.find(statement, " ORDER BY C.CD_AIT ", crt, connect, false);
			
			while(rsm.next()) {
				rsm.setValueToField("DT_PROTOCOLO", new Timestamp(rsm.getGregorianCalendar("dt_protocolo").getTimeInMillis()));
				rsm.setValueToField("DT_MOVIMENTO", new Timestamp(rsm.getGregorianCalendar("dt_movimento").getTimeInMillis()));
				rsm.setValueToField("DT_INFRACAO", new Timestamp(rsm.getGregorianCalendar("dt_infracao").getTimeInMillis()));
				
				if(rsm.getString("nm_tipo_documento").equals("Apresentação de Condutor"))
					rsm.setValueToField("NM_TIPO_DOCUMENTO", "Apres. Condutor");
			}
			
			rsm.beforeFirst();
			
			HashMap<String, Object> params =  new HashMap<String, Object>();
			
			params = this.countProcessos(params, crt, connect);
			params = this.countFases(params, crt, connect);
			params.put("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", "LOREM IPSUM DOLOR SIT AMET", connect));
			params.put("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", "CONSECTETUR ADIPISCING ELIT", connect));
			params.put("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", "INCIDIDUNT UT LABORE ET DOLORE", connect));
			params.put("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", connect));
			params.put("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", connect));
			
			byte[] print = ReportServices.getDocReport("mob/relatorio_processos", params, rsm);	
			
			ArquivoServices.salvarArquivoDePostagemDO(print, "PROCESSO", ArquivoServices.PROCESSOS_PUBLICADOS);
			
			return print;
			
		}  catch(Exception ex) {
			System.out.println("Erro! AitMovimentoDocumentoServices.getBoletimJari");
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	private static void setParamsReport(HashMap<String, Object> params) throws ValidacaoException {
		ConfManager conf = ManagerConf.getInstance();
		String reportPath = conf.getProps().getProperty("REPORT_PATH");
		String path = ContextManager.getRealPath()+"/"+reportPath;
		ArrayList<String> reportNames = new ArrayList<>();
		reportNames.add("mob//relatores_resumo_ata_subreport");
		
		params.put("SUBREPORT_DIR", path+"//mob");
		params.put("REPORT_LOCALE", new Locale("pt", "BR"));		
		params.put("SUBREPORT_NAMES", reportNames);
	}
	
	private static void getRelatores(HashMap<String, Object> params) {
		String nmRelatores = "";
		List<HashMap<String, Object>> relatores = new ArrayList<HashMap<String, Object>>();
		ResultSetMap rsm = ParametroServices.getValoresOfParametro("LISTA_RELATORES", null);
		rsm.beforeFirst();
		while(rsm.next()) {
			if(rsm.getString("VL_APRESENTACAO") != null) {
				nmRelatores += rsm.getString("VL_APRESENTACAO") + "; ";
				
				HashMap<String, Object> relator = new HashMap<String, Object>();
				relator.put("NM_RELATOR", rsm.getString("VL_APRESENTACAO"));
				relatores.add(relator);
			}		
		}
		rsm.beforeFirst();
		params.put("LISTA_RELATORES", nmRelatores);	
		params.put("DATASET_LISTA_RELATORES", new JRBeanCollectionDataSource(relatores));
	}
	
	private String getSecretarioGeral() {
		String nmSecretario = "";
		int cdSecretarioGeral = ParametroServices.getValorOfParametroAsInteger("CD_SECRETARIO_GERAL", 0);
		if(cdSecretarioGeral > 0) {
			Pessoa secretario = PessoaDAO.get(cdSecretarioGeral);
			return secretario.getNmPessoa();
		}
		return nmSecretario;
	}

	public String getEstado() throws Exception {
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Cidade cidade = cidadeRepository.get(orgao.getCdCidade());
		return estadoRepository.get(cidade.getCdEstado()).getSgEstado();
	}
	
	private int getRegistrosAta(String nrAta, String nmFase, HashMap<String, Object> params, Connection connect) {
		int cdFase = FaseServices.getCdFaseByNome(nmFase, connect);
		String registros = null;
		int c = 0;
		
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT D.NM_FASE, A.NR_DOCUMENTO, C.ID_AIT " + 
					"FROM PTC_DOCUMENTO A JOIN MOB_AIT_MOVIMENTO_DOCUMENTO B ON (A.CD_DOCUMENTO = B.CD_DOCUMENTO) " + 
					"JOIN MOB_AIT C ON (C.CD_AIT = B.CD_AIT) " + 
					"JOIN PTC_FASE D ON (D.CD_FASE = A.CD_FASE) " + 
					"JOIN PTC_DOCUMENTO ATA ON (A.CD_DOCUMENTO_SUPERIOR = ATA.CD_DOCUMENTO) " + 
					"WHERE ATA.NR_DOCUMENTO = ? AND A.CD_FASE = ?"
					);
			
			pstmt.setString(1, nrAta);
			pstmt.setInt(2, cdFase);
			
			ResultSetMap result = new ResultSetMap(pstmt.executeQuery());
			
			result.beforeFirst();
			
			if(result.next()) {
				registros = "";
				result.beforeFirst();
				while(result.next()) {
					registros += result.getString("id_ait") + "; ";
					c++;
				}
			}
			
			params.put(nmFase, registros);
			
			return c;
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
	}
	
	public String validateAta(String nrAta) {
		Connection connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					  "SELECT COUNT(A.*) AS REG FROM PTC_DOCUMENTO A "
					+ "JOIN MOB_AIT_MOVIMENTO_DOCUMENTO B ON (A.CD_DOCUMENTO = B.CD_DOCUMENTO) "
					+ "JOIN MOB_AIT_MOVIMENTO C ON (B.CD_MOVIMENTO = C.CD_MOVIMENTO AND B.CD_AIT = C.CD_AIT) "
					+ "JOIN MOB_AIT D ON (C.CD_AIT = D.CD_AIT) JOIN PTC_FASE E ON (A.CD_FASE = E.CD_FASE) "
					+ "WHERE A.CD_DOCUMENTO_SUPERIOR = (SELECT CD_DOCUMENTO FROM PTC_DOCUMENTO WHERE NR_DOCUMENTO = ? AND CD_TIPO_DOCUMENTO = ?)");
			
			pstmt.setString(1, nrAta);
			pstmt.setInt(2, ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_ATA", -1));
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next())
				return String.valueOf(rsm.getInt("reg"));
			else
				return "INS";
			
		} catch(Exception e) {
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
		
	}
	
	public List<AitMovimento> getMovimentosFromDTO(Criterios crt) {
		return getMovimentosFromDTO(crt, null);
	}
	
	public List<AitMovimento> getMovimentosFromDTO(Criterios crt, Connection connect) {
		try {
			List<AitMovimentoDocumentoDTO> _dtos = findProcessos(crt, connect);
			List<AitMovimento> _movimentos = new ArrayList<>();
			
			for(AitMovimentoDocumentoDTO _dto: _dtos)
				_movimentos.add(_dto.getMovimento());
			
			return _movimentos;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	private HashMap<String, Object> countProcessos(HashMap<String, Object> params, Criterios crt, Connection connect) {
		try {
			String statement = "SELECT E.NM_TIPO_DOCUMENTO, COUNT(A.*) AS REGISTROS "
					+ "FROM MOB_AIT_MOVIMENTO A "
					+ "JOIN MOB_AIT B ON (A.CD_AIT = B.CD_AIT) "
					+ "JOIN MOB_AIT_MOVIMENTO_DOCUMENTO C ON (C.CD_AIT = B.CD_AIT AND A.CD_MOVIMENTO = C.CD_MOVIMENTO) "
					+ "JOIN PTC_DOCUMENTO D ON (C.CD_DOCUMENTO = D.CD_DOCUMENTO) "
					+ "JOIN GPN_TIPO_DOCUMENTO E ON (E.CD_TIPO_DOCUMENTO = D.CD_TIPO_DOCUMENTO) "
					+ "JOIN PTC_FASE F ON (F.CD_FASE = D.CD_FASE) ";
			
			ResultSetMap _rsm = Search.find(statement, "GROUP BY E.CD_TIPO_DOCUMENTO ", crt, connect, false);
			
			while(_rsm.next())
				params.put(_rsm.getString("nm_tipo_documento"), _rsm.getInt("registros"));
			
			return params;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return params;
		}
	}
	
	private HashMap<String, Object> countFases(HashMap<String, Object> params, Criterios crt, Connection connect) {
		try {
			String statement = "SELECT F.NM_FASE, COUNT(A.*) AS REGISTROS "
					+ "FROM MOB_AIT_MOVIMENTO A "
					+ "JOIN MOB_AIT B ON (A.CD_AIT = B.CD_AIT) "
					+ "JOIN MOB_AIT_MOVIMENTO_DOCUMENTO C ON (C.CD_AIT = B.CD_AIT AND A.CD_MOVIMENTO = C.CD_MOVIMENTO) "
					+ "JOIN PTC_DOCUMENTO D ON (C.CD_DOCUMENTO = D.CD_DOCUMENTO) "
					+ "JOIN GPN_TIPO_DOCUMENTO E ON (E.CD_TIPO_DOCUMENTO = D.CD_TIPO_DOCUMENTO) "
					+ "JOIN PTC_FASE F ON (F.CD_FASE = D.CD_FASE) ";
			
			ResultSetMap _rsm = Search.find(statement," GROUP BY F.CD_FASE ", crt, connect, false);
			
			while(_rsm.next())
				params.put(_rsm.getString("nm_fase"), _rsm.getInt("registros"));
			
			return params;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return params;
		}
	}
	
	private Documento insertAta(Documento ata) {
		return insertAta(ata, null);
	}
	
	private Documento insertAta(Documento ata, Connection connect) {
		int tpDocumentoAta = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_ATA", 0);
		ata.setCdTipoDocumento(tpDocumentoAta);
		ata.setDtProtocolo(new GregorianCalendar());
		
		boolean isConnNull = (connect == null);
		
		if(isConnNull)
			connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM PTC_DOCUMENTO WHERE NR_DOCUMENTO = ? AND CD_TIPO_DOCUMENTO = ?");
			
			pstmt.setString(1, ata.getNrDocumento());
			pstmt.setInt(2, tpDocumentoAta);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next())
				return DocumentoDAO.get(rsm.getInt("cd_documento"));
			else
				return DocumentoDAO.get(DocumentoDAO.insert(ata));
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}
	
	private void updateFields(List<FormularioAtributoValor> valores, int cdDocumento) {
		Connection connect = Conexao.conectar();
		
		try {
			for(FormularioAtributoValor atributo: valores) {
				PreparedStatement pstmt = connect.prepareStatement("UPDATE GRL_FORMULARIO_ATRIBUTO_VALOR SET TXT_ATRIBUTO_VALOR = ? WHERE CD_FORMULARIO_ATRIBUTO = ? AND CD_DOCUMENTO = ?");
				
				pstmt.setString(1, atributo.getTxtAtributoValor());
				pstmt.setInt(2, atributo.getCdFormularioAtributo());
				pstmt.setInt(3, cdDocumento);
				
				int res = pstmt.executeUpdate();
				
				if(res <= 0) {
					createField(atributo, cdDocumento, connect);
				}
			}
		} catch(SQLException ex) {
			System.out.println(ex);
			ex.printStackTrace(System.out);
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	private void createField(FormularioAtributoValor atributoValor, int cdDocumento, Connection connect) {
		atributoValor.setCdDocumento(cdDocumento);
		FormularioAtributoValorDAO.insert(atributoValor, connect);
	}
	
	private int updateFaseDocumento(int cdFase, int cdTipoDocumento, int cdDocumento) {
		Connection connect = Conexao.conectar();
		
		try {
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PTC_DOCUMENTO SET CD_FASE = ?, CD_TIPO_DOCUMENTO = ? WHERE CD_DOCUMENTO = ?");
			pstmt.setInt(1, cdFase);
			pstmt.setInt(2, cdTipoDocumento);
			pstmt.setInt(3, cdDocumento);
			
			int res = pstmt.executeUpdate();
			
			return res;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
	}
	
	private List<FormularioAtributoValor> convCamposFormulario(ResultSetMap rsm) {
		try {
			return new ResultSetMapper<FormularioAtributoValor>(rsm, FormularioAtributoValor.class).toList();
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	private Timestamp convDate(GregorianCalendar date) {
		Timestamp ts = new Timestamp(date.getTimeInMillis());
		return ts;
	}
	
	private void Hash2String(HashMap<String, Object> hsmp) {
		String res = "";
		
		res = hsmp.toString();
		System.out.println(res);
	}
	
	private Ait getAit(int cdAit) {
		Criterios crt = new Criterios();
		try {
		ResultSetMap rsm = AitServices.find(crt.add((Util.isStrBaseAntiga() ? "codigo_ait" : "A.cd_ait"), Integer.toString(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		
		if(rsm.next()) {
			ResultSetMapper<Ait> rsmConv = new ResultSetMapper<Ait>(rsm, Ait.class);
			return rsmConv.getFirst();
		}
		
		return null;
		}catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	private String getNmRequerente(Documento doc) {		

		FormularioAtributoValor atributoValor = FormularioAtributoValorServices.getAtributoByDocAtributo(doc.getCdDocumento(), 3);
		
		if(atributoValor != null && atributoValor.getTxtAtributoValor() != null)
			return atributoValor.getTxtAtributoValor();
			
		else if (doc.getNmRequerente() != null) 
			return doc.getNmRequerente();
		
		return "Não informado";
	}
	
	private List<DocumentoArquivo> convFiles(AitMovimentoDocumentoDTO dto) {

		List<DocumentoArquivo> arquivos = dto.getArquivosAnexados();
		List<DocumentoArquivo> arqRet = new ArrayList<DocumentoArquivo>();

		for (DocumentoArquivo arquivo : arquivos) {
			if(arquivo.getTxtOcr() != null)
				arquivo.setBlbArquivo(Util.convertBase64(arquivo.getTxtOcr()));
			arquivo.setTxtOcr("");
			arqRet.add(arquivo);
		}
		return arqRet;
	}
	
	
	private Ait updateAitCondutor(AitMovimentoDocumentoDTO dto, Ait ait, Connection connection) throws ValidationException {
		for(FormularioAtributoValor valor : dto.getCamposFormulario()) {
			FormularioAtributo atributo = FormularioAtributoDAO.get(valor.getCdFormularioAtributo(), connection);
			ait = this.setCamposAit(ait, atributo, valor);
		}
		
		int result = AitDAO.update(ait);
		
		if(result <= 0)
			throw new ValidationException("Erro ao incluir dados de condutor.");
		
		return ait;		
	}
	
	private int gerarMovimento(int tpDocumento, Connection connect) {
		boolean isConnNull = (connect == null);
		
		try {
			
			if(isConnNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM GPN_TIPO_DOCUMENTO WHERE CD_TIPO_DOCUMENTO = ?");
			pstmt.setInt(1, tpDocumento);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next())
				return Integer.parseInt(rsm.getString("ID_TIPO_DOCUMENTO"));
			
			return 0;
			
		}catch (Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}finally {
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}
	
	private Documento getAtaJari(Documento documento) {
		if (documento.getCdDocumentoSuperior() <= 0)
			return null;
		
		Documento _ata = DocumentoDAO.get(documento.getCdDocumentoSuperior());
		
		return _ata;
	}
	
	private DocumentoPessoa getRelatorJari(int cdDocumento) {
		if(cdDocumento <= 0)
			return null;
		
		try {
			ResultSetMap _rsm = DocumentoPessoaServices.getAllByDocumento(cdDocumento);
			
			if(_rsm.next()) {
				ResultSetMapper<DocumentoPessoa> _list = new ResultSetMapper<DocumentoPessoa>(_rsm, DocumentoPessoa.class);
				return _list.get(0);
			}
			return null;
		} catch(SQLException ex) {
			ex.printStackTrace(System.out);
			return null;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	private Ait updateUltimoMovimento(AitMovimentoDocumentoDTO _dto) {
		if(_dto.getDocumento().getCdTipoDocumento() == ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_APRESENTACAO_CONDUTOR", 0))
			if(_dto.getDocumento().getCdFase() == FaseServices.getCdFaseByNome("Indeferido"))
				return _dto.getAit();
			
		Ait _ait = _dto.getAit();
		_ait.setCdMovimentoAtual(_dto.getMovimento().getCdMovimento());
		
		return _ait;
	}
	
	private int getTpStatusCancelamento(AitMovimento _movimento) {
		TabelasAuxiliaresMG tabelasMg = new TabelasAuxiliaresMG();
		return tabelasMg.getStatusCancelamento(_movimento.getTpStatus());
	}
	
	private String checkResultado(AitMovimentoDocumentoDTO _dto) {
		int cdFasePendente = FaseServices.getCdFaseByNome("Pendente", null);
		try {
			Connection connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT C.NR_DOCUMENTO FROM MOB_AIT A" + 
					"INNER JOIN MOB_AIT_MOVIMENTO_DOCUMENTO B ON (A.CD_AIT = B.CD_AIT)" + 
					"INNER JOIN PTC_DOCUMENTO C ON (B.CD_DOCUMENTO = C.CD_DOCUMENTO)" + 
					"WHERE A.CD_AIT = ? AND C.CD_TIPO_DOCUMENTO = ? AND C.CD_FASE = '20'" + 
					"ORDER BY C.CD_TIPO_DOCUMENTO");
			
			pstmt.setInt(1, _dto.getMovimento().getCdAit());
			pstmt.setInt(2, _dto.getDocumento().getCdTipoDocumento());
			pstmt.setInt(3, cdFasePendente);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			Conexao.desconectar(connect);
			
			if(rsm.next())
				return rsm.getString("nr_documento");
			
			return null;
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
		
		return null;
	}
}
