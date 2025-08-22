package com.tivic.manager.mob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.GregorianCalendar;
import java.util.Optional;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.gpn.TipoDocumentoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.DocumentoDAO;
import com.tivic.manager.ptc.FaseServices;
import com.tivic.manager.validation.Validator;

import sol.dao.ResultSetMap;

import com.tivic.manager.validation.Validator;

@SuppressWarnings("unused")
public class AitMovimentoDocumentoJariValidator implements Validator<AitMovimentoDocumentoDTO> {
	
	private AitMovimentoDocumentoDTO _dto = null;
	
	@Override
	public Optional<String> validate(AitMovimentoDocumentoDTO object) {
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
	
	private Optional<String> validateJari() {
		int tpJari = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_JARI", 0);
		int cdFasePendente = FaseServices.getCdFaseByNome("Pendente", null);
		
		if(tpJari == 0)
			return Optional.of("Não foi possível validar, parâmetro de \"JARI\" não preenchido.");
		
		if(this._dto.getDocumento().getCdFase() != cdFasePendente)
			return Optional.empty();
		
		Connection connect = Conexao.conectar();
		try {
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM MOB_AIT_MOVIMENTO WHERE CD_AIT = ? AND TP_STATUS = ? AND LG_ENVIADO_DETRAN NOT IN (?, ?)");
			
			pstmt.setInt(1, this._dto.getMovimento().getCdAit());
			pstmt.setInt(2, AitMovimentoServices.RECURSO_JARI);
			pstmt.setInt(3, AitMovimentoServices.NAO_ENVIAR);
			pstmt.setInt(4, AitMovimentoServices.REGISTRO_CANCELADO);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next())
				return Optional.of("Já existe outro processo de JARI dessa infração");
			
			return Optional.empty();
		} catch(Exception e) {
			return Optional.of(e.getMessage());
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	private Optional<String> validateNip() {
		Connection connect = Conexao.conectar();
		try {
			PreparedStatement pstmt = connect.prepareStatement(
					  "SELECT * FROM MOB_AIT_MOVIMENTO WHERE TP_STATUS = ? AND CD_AIT = ? AND LG_ENVIADO_DETRAN NOT IN (?, ?)"
					);
			
			pstmt.setInt(1, AitMovimentoServices.NIP_ENVIADA);
			pstmt.setInt(2, this._dto.getMovimento().getCdAit());
			pstmt.setInt(3, AitMovimentoServices.NAO_ENVIAR);
			pstmt.setInt(4, AitMovimentoServices.REGISTRO_CANCELADO);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next())
				return Optional.empty();
			
			return Optional.of("Infração não pussui NIP Lançada.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return Optional.of(e.getMessage());
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	private Optional<String> validateCetran() {
		int tpCetran = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_CETRAN", 0);
		
		if(tpCetran == 0)
			return Optional.of("Não foi possível validar, parâmetro de \"CETRAN\" não preenchido.");
		
		Connection connect = Conexao.conectar();
		try {
			
			PreparedStatement pstmt = connect.prepareStatement(
					  "SELECT * FROM MOB_AIT_MOVIMENTO WHERE TP_STATUS IN (?, ?, ?) AND CD_AIT = ?"
					);
			
			pstmt.setInt(1, AitMovimentoServices.RECURSO_CETRAN);
			pstmt.setInt(2, AitMovimentoServices.CETRAN_DEFERIDO);
			pstmt.setInt(3, AitMovimentoServices.CETRAN_INDEFERIDO);
			pstmt.setInt(4, this._dto.getMovimento().getCdAit());
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next())
				return Optional.of("Já existe processo de CETRAN dessa infração");
			
			return Optional.empty();
		} catch(Exception e) {
			return Optional.of(e.getMessage());
		}  finally {
			Conexao.desconectar(connect);
		}
	}
	
	private Optional<String> validateResultado() {
		int tpJari = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_JARI", 0);
		int cdFasePendente = FaseServices.getCdFaseByNome("Pendente", null);
		
		if(tpJari == 0)
			return Optional.of("Não foi possível validar, parâmetro de \"JARI\" não preenchido.");
		
		if(this._dto.getDocumento().getCdFase() == cdFasePendente)
			return Optional.empty();
		
		Connection connect = Conexao.conectar();
		try {
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM MOB_AIT_MOVIMENTO WHERE CD_AIT = ? AND TP_STATUS = ? AND LG_ENVIADO_DETRAN = ?");
			
			pstmt.setInt(1, this._dto.getMovimento().getCdAit());
			pstmt.setInt(2, AitMovimentoServices.RECURSO_JARI);
			pstmt.setInt(3, AitMovimentoServices.ENVIADO_AO_DETRAN);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) 
				this._dto.getMovimento().setNrProcesso(rsm.getString("nr_processo"));
			else
				return Optional.of("Não existe processo de JARI pendente para lançamento de resultado.");
			
			pstmt = connect.prepareStatement("SELECT * FROM MOB_AIT_MOVIMENTO WHERE CD_AIT = ? AND TP_STATUS IN (?, ?) AND LG_ENVIADO_DETRAN NOT IN (?, ?)");
			
			pstmt.setInt(1, this._dto.getMovimento().getCdAit());
			pstmt.setInt(2, AitMovimentoServices.JARI_COM_PROVIMENTO);
			pstmt.setInt(3, AitMovimentoServices.JARI_SEM_PROVIMENTO);
			pstmt.setInt(4, AitMovimentoServices.NAO_ENVIAR);
			pstmt.setInt(5, AitMovimentoServices.REGISTRO_CANCELADO);
			
			ResultSetMap _res = new ResultSetMap(pstmt.executeQuery());
			
			if(_res.next())
				return Optional.of("Já existe resultado de JARI pra essa infração.");
			
			return Optional.empty();
			
		} catch(Exception e) {
			return Optional.of(e.getMessage());
		}  finally {
			Conexao.desconectar(connect);
		}
	}
	
	private Optional<String> validateCancelamento() {
		
		Connection connect = Conexao.conectar();
		try {
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM MOB_AIT_MOVIMENTO WHERE TP_STATUS IN ('17', '20', '25', '26') AND CD_AIT = ?");
			
			pstmt.setInt(1, this._dto.getMovimento().getCdAit());
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next())
				return Optional.of("Já existe movimento de cancelamento para essa infração");
			
			return Optional.empty();
		} catch(Exception e) {
			return Optional.of(e.getMessage());
		}  finally {
			Conexao.desconectar(connect);
		}
	}
	
	private Optional<String> validateAta() {
		int cdFasePendente = FaseServices.getCdFaseByNome("Pendente", null);
		
		if(this._dto.getDocumento().getCdFase() != cdFasePendente) {
			if(this._dto.getDocumentoSuperior().getNrDocumento() != null && this._dto.getDocumentoSuperior().getNrDocumento() != "")
				return Optional.empty();
			else
				return Optional.of("É necessário informar um número de ATA para lançamento de JARI");
		}
		
		return Optional.empty();
	}

}
