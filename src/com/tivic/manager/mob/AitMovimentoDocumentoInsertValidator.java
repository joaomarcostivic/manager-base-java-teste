package com.tivic.manager.mob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.GregorianCalendar;
import java.util.Optional;

import com.tivic.manager.gpn.TipoDocumentoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.util.Util;
import com.tivic.manager.validation.Validator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.manager.validation.Validator;

/**
 * Valida {@link Documento} para inserção
 * 
 * @author Maurício <mauricio@tivic.com.br>
 *
 */
@SuppressWarnings("unused")
public class AitMovimentoDocumentoInsertValidator implements Validator<AitMovimentoDocumentoDTO> {	
	
	private Documento documento = null;
	private AitMovimentoDocumentoDTO _dto = null;
	
	@Override
	public Optional<String> validate(AitMovimentoDocumentoDTO object) {	
		this.documento = object.getDocumento();
		this._dto = object;
		try {
			Method[] methods = this.getClass().getDeclaredMethods();
			for (Method method : methods) {
				if(method.getName().endsWith("validate"))
					continue;
				
				@SuppressWarnings("unchecked")
				Optional<String> op = ((Optional<String>) method.invoke(this));
				if(op.isPresent()) {
					return op;
				}
			}

			return Optional.empty();
		} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			ex.printStackTrace(System.out);
			return Optional.of(ex.getMessage());
		}
	}
	
	private Optional<String> validateNrDocumento(CustomConnection customConnection) throws Exception {
		NumeroProtocoloGeneratorFactory _factory = new NumeroProtocoloGeneratorFactory();
		
		IGerarNumeroProtocoloGenerator _generator = _factory.gerarNumero(OrgaoServices.getCidadeOrgaoAutuador());
		this.documento = _generator.generate(this.documento, this._dto.getAit().getCdAit(), customConnection);
		
		if(this.documento.getNrDocumento() == null || this.documento.getNrDocumento().trim().equals(""))
			Optional.of("Número de documento inválido.");
		
		return Optional.empty();
	}
	
	private Optional<String> validateDtProtocolo() {
		GregorianCalendar now = new GregorianCalendar();
		
		if(documento.getDtProtocolo() == null) 
			documento.setDtProtocolo(now);
		
		return documento.getDtProtocolo().after(now) ? Optional.of("Dt. Protocolo é posterior a hoje.") : Optional.empty();
	}
	
	private Optional<String> validateCdSituacao() {
		int cdSituacao = ParametroServices.getValorOfParametroAsInteger("CD_SIT_DOC_EM_ABERTO", 0, 0, null);
		System.out.println(cdSituacao);
		
		if(cdSituacao <= 0) {
			System.out.println("VALIDATE_CD_SITUACAO ERROR");
			return Optional.of("Não há parâmetro para documento em \"Em aberto\".");
		}
				
		documento.setCdSituacaoDocumento(cdSituacao);
		
		return Optional.empty();
	}
	
	private Optional<String> validateCdSetor() {
		int cdSetor = ParametroServices.getValorOfParametroAsInteger("CD_SETOR_PROTOCOLO", 0, 0, null);
		
		if(cdSetor <= 0)
			return Optional.of("Não há parâmetro para setor \"Protocolo\".");
		
		documento.setCdSetor(cdSetor);
		documento.setCdSetorAtual(cdSetor);
		
		return Optional.empty();
	}
	
	private Optional<String> validateCdFase() {
		if(documento.getCdFase() > 0)
			return Optional.empty();
		
		int cdFase = ParametroServices.getValorOfParametroAsInteger("CD_FASE_INICIAL", 0, 0, null);

		if(cdFase <= 0)
			return Optional.of("Não há parâmetro para fase inicial.");
		
		if(documento.getCdFase() <= 0 ) {
			documento.setCdFase(cdFase);
		}
		
		return Optional.empty();
	}
	
	private Optional<String> validateCdUsuario() {
		if(documento.getCdUsuario() <= 0)
			return Optional.of("É necessário indicar o usuário que registra o documento.");
				
		return Optional.empty();
	}
	
	private Optional<String> validateNrControleAit() {
		Ait _ait = AitDAO.get(_dto.getMovimento().getCdAit());
		
		if(_ait == null)
			return Optional.of("AIT Inválido");
		
		if(_ait.getNrControle() == null || _ait.getNrControle().equals("") || _ait.getNrControle().equals("0"))
			return Optional.of("AIT não registrado no DETRAN");
		
		return Optional.empty();
	}
}
