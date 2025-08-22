package com.tivic.manager.mob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.GregorianCalendar;
import java.util.Optional;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.gpn.TipoDocumentoServices;
import com.tivic.manager.grl.FormularioAtributoValor;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.FaseServices;
import com.tivic.manager.validation.Validator;

import sol.dao.ResultSetMap;
import sol.dao.Util;

import com.tivic.manager.validation.Validator;

@SuppressWarnings("unused")
public class AitMovimentoDocumentoApresValidator implements Validator<AitMovimentoDocumentoDTO> {
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
	
	private Optional<String> validateApres() {
		int tpApresCondutor = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_DOCUMENTO_APRESENTACAO_CONDUTOR", 0);
		
		if(tpApresCondutor == 0)
			return Optional.of("Não foi possível validar, parâmetro de \"Apresentação de Condutor\" não preenchido.");
		
		try {
			Connection connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM MOB_AIT_MOVIMENTO WHERE CD_AIT = ? AND TP_STATUS = ? AND LG_ENVIADO_DETRAN NOT IN (?, ?)");
			
			pstmt.setInt(1, this._dto.getMovimento().getCdAit());
			pstmt.setInt(2, AitMovimentoServices.TRANSFERENCIA_PONTUACAO);
			pstmt.setInt(3, AitMovimentoServices.NAO_ENVIAR);
			pstmt.setInt(4, AitMovimentoServices.REGISTRO_CANCELADO);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			Conexao.desconectar(connect);
			
			if(rsm.next())
				return Optional.of("Já existe apresentação de condutor dessa infração");
			
			return Optional.empty();
		} catch(Exception e) {
			return Optional.of(e.getMessage());
		}
	}
	
	private Optional<String> validateNic() {
		Ait _ait = AitDAO.get(this._dto.getMovimento().getCdAit());
		Infracao _infracao = InfracaoDAO.get(_ait.getCdInfracao());
		
		if(_infracao.getNrCodDetran() == 50002 || _infracao.getNrCodDetran() == 50020)
			return Optional.of("Infração não pode ser NIC");
		
		return Optional.empty();		
	}
	
	private Optional<String> validateNai() {
		try {
			Connection connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement(
					  "SELECT CD_AIT FROM MOB_AIT_MOVIMENTO WHERE TP_STATUS = ? AND CD_AIT = ? AND LG_ENVIADO_DETRAN = ?"
					);
			
			pstmt.setInt(1, AitMovimentoServices.NAI_ENVIADO);
			pstmt.setInt(2, this._dto.getMovimento().getCdAit());
			pstmt.setInt(3, AitMovimentoServices.ENVIADO_AO_DETRAN);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			Conexao.desconectar(connect);
			
			if(rsm.next())
				return Optional.empty();
			
			return Optional.of("Infração não possui NAI lançada.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return Optional.of(e.getMessage());
		}
		
	}
	
	private Optional<String> validateResponsabilidade() {
		try {
			Connection connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT C.* FROM MOB_AIT_MOVIMENTO_DOCUMENTO A " + 
					"INNER JOIN MOB_AIT B ON (A.CD_AIT = B.CD_AIT) " + 
					"INNER JOIN MOB_INFRACAO C ON (B.CD_INFRACAO = C.CD_INFRACAO) " + 
					"WHERE A.CD_AIT = ?");
			
			pstmt.setInt(1, this._dto.getMovimento().getCdAit());
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				int tpResponsabilidade = rsm.getInt("TP_RESPONSABILIDADE");
				if (tpResponsabilidade != 0)
					return Optional.of("Não é possível lançar Apresentação de Condutor para infrações do tipo \"Condutor\".");
			}
			
			return Optional.empty();
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return Optional.of(e.getMessage());
		}
	}
	
	private Optional<String> validateData() {	
		try {
			Connection connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement(
					  "SELECT * FROM MOB_AIT_MOVIMENTO WHERE TP_STATUS = '3' AND CD_AIT = ?"
					);
			
			pstmt.setInt(1, this._dto.getMovimento().getCdAit());
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			Conexao.desconectar(connect);
			
			if(rsm.next()) {
				GregorianCalendar dtMovimento = (rsm.getTimestamp("dt_movimento")==null) ? null : Util.longToCalendar(rsm.getTimestamp("dt_movimento").getTime());
				
				if(dtMovimento == null)
					return Optional.of("Data do movimento inválida");
				
				if(this._dto.getDocumento().getDtProtocolo().compareTo(dtMovimento) < 0) 
					return Optional.of("Data do documento é anterior a data do lançamento da NAI");
				
				return Optional.empty();
			}
			
			return Optional.of("Infração não possui NAI lançada.");
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return Optional.of(e.getMessage());
		}
	}	
	
	private Optional<String> validateUfCnh() {
		Connection connect = Conexao.conectar();
		
		int tpIndeferido = FaseServices.getCdFaseByNome("indeferido", connect);
		if(this._dto.getDocumento().getCdFase() == tpIndeferido)
			return Optional.empty();
		
		try {
			
			PreparedStatement modelStmt = connect.prepareStatement("SELECT * FROM GRL_FORMULARIO_ATRIBUTO WHERE NM_ATRIBUTO = 'modelCnhCondutor'");
			ResultSetMap modelRsm = new ResultSetMap(modelStmt.executeQuery());
			
			if (modelRsm.next()) {
				String modelCnhCondutor = _dto.getCamposFormulario().get(modelRsm.getInt("cd_formulario_atributo") - 1).getTxtAtributoValor();
				
				if(modelCnhCondutor == "")
					return Optional.of("É necessário selecionar o modelo da CNH");
				
				if(Integer.parseInt(modelCnhCondutor) == 3)
					return Optional.empty();
			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM GRL_FORMULARIO_ATRIBUTO WHERE NM_ATRIBUTO = 'ufCnhCondutor'");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			
			if(rsm.next()) {
				String ufCnhCondutor = _dto.getCamposFormulario().get(rsm.getInt("cd_formulario_atributo") - 1).getTxtAtributoValor();
				
				if(ufCnhCondutor == null || ufCnhCondutor == "")
					return Optional.of("É necessário informar a UF da CNH");
			}
			
			return Optional.empty();
		} catch (Exception e) {
			return Optional.of(e.getMessage());
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	@SuppressWarnings("resource")
	private Optional<String> validateTpCnh() {
		
		int tpIndeferido = FaseServices.getCdFaseByNome("indeferido", null);
		if(this._dto.getDocumento().getCdFase() == tpIndeferido)
			return Optional.empty();
		
		try {
			Connection connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM GRL_FORMULARIO_ATRIBUTO WHERE NM_ATRIBUTO = 'modelCnhCondutor'");
			ResultSetMap model = new ResultSetMap(pstmt.executeQuery());
			
			if(model.next()) {
				String modelCnhCondutor = _dto.getCamposFormulario().get(model.getInt("cd_formulario_atributo") - 1).getTxtAtributoValor();
				
				if(modelCnhCondutor == "")
					return Optional.of("É necessário selecionar o modelo da CNH");
				
				if(Integer.parseInt(modelCnhCondutor) == 3) {
					pstmt = connect.prepareStatement("SELECT * FROM GRL_FORMULARIO_ATRIBUTO WHERE NM_ATRIBUTO = 'paisCnhCondutor'");
					
					ResultSetMap pais = new ResultSetMap(pstmt.executeQuery());
					
					if(pais.next()) {
						String paisCnhCondutor = _dto.getCamposFormulario().get((pais.getInt("cd_formulario_atributo") - 1)).getTxtAtributoValor();
						
						if(paisCnhCondutor != null && paisCnhCondutor != "")
							return Optional.empty();
					}
					
					return Optional.of("É necessário informar o país da CNH em casos de CNH Estranheira");					
				}
			}
			
			pstmt = connect.prepareStatement("SELECT * FROM MOB_AIT WHERE CD_AIT = ?");
			
			pstmt.setInt(1, this._dto.getMovimento().getCdAit());
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			String tpCnhCondutor = this._dto.getCamposFormulario().get(3).getTxtAtributoValor();
			int tpVeiculo = -1;
			
			if(rsm.next()) {
				tpVeiculo = rsm.getInt("CD_TIPO_VEICULO");

				if(tpVeiculo == 0)
					return Optional.empty();
			}
			
			pstmt = connect.prepareStatement("SELECT * FROM FTA_TIPO_VEICULO WHERE CD_TIPO_VEICULO = ?");
			pstmt.setInt(1, rsm.getInt("CD_TIPO_VEICULO"));
			
			rsm = new ResultSetMap(pstmt.executeQuery());
			
			Conexao.desconectar(connect);
			TipoVeiculo _tipoVeiculo;
			
			if(rsm.next()) {
				_tipoVeiculo = new TipoVeiculo(
						rsm.getInt("cd_tipo_veiculo"),
						rsm.getString("nm_tipo_veiculo"),
						rsm.getString("txt_cnh_requerida")
						);
			} else {
				_tipoVeiculo = new TipoVeiculo();
			}
			
			String[] cnhsAceitas = _tipoVeiculo.getTxtCnhRequerida().split(",");

			
			for(String tpCnh : cnhsAceitas) {
				if(tpCnh.equals(tpCnhCondutor)) {
					return Optional.empty();
				}
			}	
			
			return Optional.of("CNH não é compatível com o tipo de veículo.");
			
		} catch(Exception e) {
			return Optional.of(e.getMessage());
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
			return Optional.of(e.getMessage());
		}
	}
	
}
