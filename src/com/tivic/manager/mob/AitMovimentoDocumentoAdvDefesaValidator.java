package com.tivic.manager.mob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.FaseServices;
import com.tivic.manager.validation.Validator;

import sol.dao.ResultSetMap;

@SuppressWarnings("unused")
public class AitMovimentoDocumentoAdvDefesaValidator implements Validator<AitMovimentoDocumentoDTO> {
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
	
	private Optional<String> validateNAI() {	
		try {
			Connection connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement(
					  "SELECT * FROM MOB_AIT_MOVIMENTO WHERE TP_STATUS = ? AND CD_AIT = ? AND LG_ENVIADO_DETRAN = ?"
					);
			
			pstmt.setInt(1, AitMovimentoServices.NAI_ENVIADO);
			pstmt.setInt(2, this._dto.getMovimento().getCdAit());
			pstmt.setInt(3, AitMovimentoServices.ENVIADO_AO_DETRAN);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			Conexao.desconectar(connect);
			
			if(rsm.next())
				return Optional.empty();
			
			return Optional.of("Protocolo não possui NAI lançada.");

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return Optional.of("NAI: " + e.getMessage());
		}
	}
	
	private Optional<String> validateNIP() {	
		try {
			Connection connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement(
					  "SELECT * FROM MOB_AIT_MOVIMENTO WHERE TP_STATUS = ? AND CD_AIT = ? AND LG_ENVIADO_DETRAN = ?"
					);
			
			pstmt.setInt(1, AitMovimentoServices.NIP_ENVIADA);
			pstmt.setInt(2, this._dto.getMovimento().getCdAit());
			pstmt.setInt(3, AitMovimentoServices.ENVIADO_AO_DETRAN);	
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			Conexao.desconectar(connect);
			
			if(rsm.next())
				return Optional.of("Protocolo já possui NIP lançada");
			
			return Optional.empty();

		} catch (Exception e) {
			e.printStackTrace(System.out);
			return Optional.of("NIP: " + e.getMessage());
		}
	}
	
	private Optional<String> validateDefesa() {
		int tpDefesaPrevia = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_ADVERTENCIA_DEFESA", 0);
		
		if(tpDefesaPrevia == 0)
			return Optional.of("Não foi possível validar, parâmetro de \"Defesa com Advertência\" não preenchido.");
		
		if(this._dto.getDocumento().getCdFase() != FaseServices.getCdFaseByNome("Pendente", null))
			return Optional.empty();
		
		try {
			Connection connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement(
					  "SELECT * FROM MOB_AIT_MOVIMENTO WHERE TP_STATUS = ? AND CD_AIT = ?"
					);
			
			pstmt.setInt(1, AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA);
			pstmt.setInt(2, this._dto.getMovimento().getCdAit());
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next())
				return Optional.of("Já existe entrade de Defesa com Advertência dessa infração");
			
			return Optional.empty();
		} catch(Exception e) {
			return Optional.of("Defesa: " + e.getMessage());
		}
	}
	
	private Optional<String> validateResultado() {
		int cdFasePendente = FaseServices.getCdFaseByNome("Pendente", null);
		
		if(this._dto.getDocumento().getCdFase() == cdFasePendente)
			return Optional.empty();
		
		try {
			Connection connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement(
					  "SELECT * FROM MOB_AIT_MOVIMENTO WHERE TP_STATUS = ? AND CD_AIT = ? AND LG_ENVIADO_DETRAN = ?"
					);
			
			pstmt.setInt(1, AitMovimentoServices.ADVERTENCIA_DEFESA_ENTRADA);
			pstmt.setInt(2, this._dto.getMovimento().getCdAit());
			pstmt.setInt(3, AitMovimentoServices.ENVIADO_AO_DETRAN);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) 
				this._dto.getMovimento().setNrProcesso(rsm.getString("nr_processo"));
			else
				return Optional.of("Não existe entrada de Defesa com Advertência para lançamento de resultado.");
			
			pstmt = connect.prepareStatement("SELECT * FROM MOB_AIT_MOVIMENTO WHERE CD_AIT = ? AND TP_STATUS IN (?, ?)");
			
			pstmt.setInt(1, this._dto.getMovimento().getCdAit());
			pstmt.setInt(2, AitMovimentoServices.ADVERTENCIA_DEFESA_DEFERIDA);
			pstmt.setInt(3, AitMovimentoServices.ADVERTENCIA_DEFESA_INDEFERIDA);
			
			ResultSetMap _res = new ResultSetMap(pstmt.executeQuery());
			
			if(_res.next())
				return Optional.of("Já existe resultado de defesa com advertência pra essa infração.");
			
			return Optional.empty();
			
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return Optional.of("Resultado: " + e.getMessage());
		}
	}
	
	private Optional<String> validateCancelamento() {
		
		try {
			Connection connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM MOB_AIT_MOVIMENTO WHERE TP_STATUS IN ('17', '20', '25', '26') AND CD_AIT = ?");
			
			pstmt.setInt(1, this._dto.getMovimento().getCdAit());
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next())
				return Optional.of("Já existe movimento de cancelamento para essa infração");
			
			return Optional.empty();
		} catch(Exception e) {
			return Optional.of("Cancelamento: " + e.getMessage());
		}
	}

}