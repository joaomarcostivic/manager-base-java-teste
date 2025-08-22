package com.tivic.manager.mob.mobile;

import java.awt.image.BufferedImage;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.ValidationException;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.repository.EquipamentoRepository;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.exceptions.UsuarioException;
import com.tivic.manager.mob.mobile.builders.GeradorImagemQrCode;
import com.tivic.manager.mob.mobile.builders.ParametrosQrCodeBuilder;
import com.tivic.manager.mob.mobile.dto.GeradorQrCodeDTO;
import com.tivic.manager.mob.mobile.dto.QrCodeResponseDTO;
import com.tivic.manager.mob.mobile.dto.VinculacaoEquipamentoDTO;
import com.tivic.manager.mob.mobile.utils.QrCodeImagemUtils;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.mob.talonario.SituacaoTalaoEnum;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

import sol.dao.ItemComparator;

public class EquipamentoTalonarioService implements IEquipamentoTalonarioService {
	
	private EquipamentoRepository equipamentoRepository;
    public String nmEquipamentoPrefixo = null;
    public GeradorQrCodeDTO geradorQrCodeDTO;
    private ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;

	public EquipamentoTalonarioService() throws Exception {
		equipamentoRepository = (EquipamentoRepository) BeansFactory.get(EquipamentoRepository.class);
		conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);;
	}
	
	@Override
	public VinculacaoEquipamentoDTO insert(Equipamento equipamento) throws Exception{
		return insert(equipamento, new CustomConnection());
	}
	
	@Override
	public VinculacaoEquipamentoDTO insert(Equipamento equipamento, CustomConnection customConnection) throws Exception{
		try {
			customConnection.initConnection(true);
			Equipamento equipamentoExistente = getEquipamentoExistente(equipamento, customConnection);
	        Usuario usuarioSuporte = getUsuarioMaster();
			if(equipamentoExistente != null) {
				return new VinculacaoEquipamentoDTO(equipamentoExistente, usuarioSuporte);
			}
			equipamento.setStEquipamento(SituacaoTalaoEnum.ST_TALAO_INATIVO.getKey());
			Equipamento equipamentoInserido = equipamentoRepository.insert(equipamento, customConnection);
			customConnection.finishConnection();
	        return new VinculacaoEquipamentoDTO(equipamentoInserido, usuarioSuporte);
		} finally {
			customConnection.closeConnection();
		}
	}
	
	@Override
	public QrCodeResponseDTO gerarQrCodeComParametros(int cdEquipamento) throws Exception, ValidacaoException {
	    Map<String, String> parametros = gerarParametros(cdEquipamento);
	    byte[] qrCodeBytes = gerarQrCodeAutenticacao();  
	    QrCodeResponseDTO responseDTO = new QrCodeResponseDTO();
	    responseDTO.setQrCodeImage(qrCodeBytes);
	    responseDTO.setParametros(parametros);  
	    return responseDTO;
	}

    private byte[] gerarQrCodeAutenticacao() throws Exception, ValidacaoException {
        GeradorQrCodeDTO geradorQrCodeDTO = new GeradorImagemQrCode().gerarQrCode(this.geradorQrCodeDTO);
        BufferedImage qrCodeImage = geradorQrCodeDTO.getQrCodeImage();
        return QrCodeImagemUtils.converterImagemParaByteArray(qrCodeImage);
    }
	
	private Map<String, String> gerarParametros(int cdEquipamento) throws Exception, ValidacaoException {
	    this.geradorQrCodeDTO = adicionarParametros(cdEquipamento);
	    Map<String, String> parametros = new HashMap<>();
	    parametros.put("Protocolo SSL", (this.geradorQrCodeDTO.getLgApiSsl() ? "Sim" : "Não"));
	    parametros.put("Endereço", this.geradorQrCodeDTO.getNmApiHost());
	    parametros.put("Porta", this.geradorQrCodeDTO.getNmApiPort());
	    parametros.put("Contexto", this.geradorQrCodeDTO.getNmApiContext());
	    parametros.put("Caminho", this.geradorQrCodeDTO.getNmApiRoot());
	    parametros.put("Id. do equipamento", this.geradorQrCodeDTO.getDsDeviceUuid());
	    parametros.put("Nome do equipamento", this.geradorQrCodeDTO.getNmEquipamento());
	    StringBuilder parametrosFormatados = new StringBuilder();
	    for (Map.Entry<String, String> entry : parametros.entrySet()) {
	        parametrosFormatados.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
	    }
	    return parametros;
	}

	private GeradorQrCodeDTO adicionarParametros(int cdEquipamento) throws Exception {
	    Map<String, String> parametros = obterParametros();
	    String nmEquipamentoPrefixo = parametros.get("NM_EQUIPAMENTO_PREFIXO");
	    Equipamento equipamento = null;
	    if (cdEquipamento > 0) {
	        equipamento = getEquipamento(cdEquipamento);
	    }
	    String idDivice = UUID.randomUUID().toString();
	    return new ParametrosQrCodeBuilder()
	        .addLgApiSsl(parametros.get("LG_API_SSL"))
	        .addNmApiHost(parametros.get("NM_API_HOST"))
	        .addNmApiPort(parametros.get("NM_API_PORT"))
	        .addNmApiContext(parametros.get("NM_API_CONTEXT"))
	        .addNmApiRoot(parametros.get("NM_API_APIROOT"))
	        .addCdEquipamento(cdEquipamento)
	        .addDsDeviceUuid(equipamento != null && equipamento.getIdEquipamento() != null ? equipamento.getIdEquipamento() : idDivice.toUpperCase())
	        .addNmEquipamento(equipamento != null && equipamento.getNmEquipamento() != null ? equipamento.getNmEquipamento() : gerarNomeEquipamento(nmEquipamentoPrefixo))
	        .build();
	}
	
	private Map<String, String> obterParametros() throws Exception, ValidacaoException {
	    String[] nomesParametros = {"LG_API_SSL", "NM_API_HOST", "NM_API_PORT", "NM_API_CONTEXT", "NM_API_APIROOT", "NM_EQUIPAMENTO_PREFIXO" };
	    Map<String, String> parametros = new HashMap<>();

	    for (String nome : nomesParametros) {
	        String valor = this.conversorBaseAntigaNovaFactory.getParametroRepository().getValorOfParametroAsString(nome);
	        if (valor == null || valor.isEmpty() || (valor.equals("0") && !nome.equals("LG_API_SSL"))) {
	            throw new ValidacaoException("O parâmetro " + nome + " não está definido ou está vazio.");
	        }
	        if (nome.equals("NM_EQUIPAMENTO_PREFIXO")) {
	            this.nmEquipamentoPrefixo = valor;
	        }
	        parametros.put(nome, valor);
	    }
	    return parametros;
	}

	private String gerarNomeEquipamento(String nmEquipamentoPrefixo) throws Exception {
	    int quantidadeTalonarios = getQuantidadeTalonariosCriados();
	    int novoNumeroSequencial = quantidadeTalonarios + 1;
	    String nrSequencial = String.format("%03d", novoNumeroSequencial);
	    String novoNmEquipamento = nmEquipamentoPrefixo + nrSequencial;
	    return novoNmEquipamento;
	}

	private int getQuantidadeTalonariosCriados() throws Exception {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("tp_equipamento", String.valueOf(EquipamentoEnum.TALONARIO_ELETRONICO.getKey()), ItemComparator.EQUAL, Types.VARCHAR));
		List<Equipamento> equipamentos = this.equipamentoRepository.find(criterios, new CustomConnection());
		return equipamentos.size();
	}
	
	private Equipamento getEquipamento(int cdEquipamento) throws Exception {
		Equipamento equipamento = this.equipamentoRepository.get(cdEquipamento, new CustomConnection());
		return equipamento != null ? equipamento : null;
	}
	
	private Equipamento getEquipamentoExistente(Equipamento equipamento, CustomConnection customConnection) throws Exception, ValidationException {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("id_equipamento", equipamento.getIdEquipamento(), ItemComparator.EQUAL, Types.VARCHAR));
		criterios.add(new ItemComparator("cd_equipamento", String.valueOf(equipamento.getCdEquipamento()), ItemComparator.DIFFERENT, Types.INTEGER));
		List<Equipamento> equipamentos = this.equipamentoRepository.find(criterios, customConnection);
		return equipamentos != null && !equipamentos.isEmpty() ? equipamentos.get(0) : null;
	}
	
	private Usuario getUsuarioMaster() throws Exception, UsuarioException {
		int cdUsuarioMaster = this.conversorBaseAntigaNovaFactory.getParametroRepository().getValorOfParametroAsInt("CD_USUARIO_MASTER");
        if (cdUsuarioMaster <= 0) {
            throw new Exception("O parâmetro CD_USUARIO_MASTER não está definido ou está vazio.");
        }
		Usuario usuario = this.conversorBaseAntigaNovaFactory.getUsuarioRepository().get(cdUsuarioMaster);
		if (usuario == null) {
			throw new UsuarioException("Usuário Master não localizado.");
		}
		return usuario;	
	}
	
}
