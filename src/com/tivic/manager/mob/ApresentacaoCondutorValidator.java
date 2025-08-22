package com.tivic.manager.mob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.fta.TipoVeiculoDAO;
import com.tivic.manager.grl.FormularioAtributo;
import com.tivic.manager.grl.FormularioAtributoDAO;
import com.tivic.manager.grl.FormularioAtributoValor;
import com.tivic.manager.validation.Validator;

@SuppressWarnings("unused")
public class ApresentacaoCondutorValidator implements Validator<AitMovimentoDocumentoDTO> {
	
	private AitMovimentoDocumentoDTO dto = null;

	@Override
	public Optional<String> validate(AitMovimentoDocumentoDTO obj) {
		this.dto = obj;		
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
	
//	private Optional<String> validateTpCnh() {
//		Connection conn = Conexao.conectar();
//		try {
//			
//			Ait ait = AitDAO.get(this.dto.getMovimento().getCdAit(), conn);
//			TipoVeiculo tipoVeiculo = TipoVeiculoDAO.get(ait.getCdTipoVeiculo(), conn);
//			
//			String[] array = tipoVeiculo.getTxtCnhRequerida().split(",");
//			if(array.length == 0)
//				return Optional.empty();
//			
//			List<String> tiposRequeridos = Arrays.asList(array);
//			
//			String tpCnh = "";
//			for(FormularioAtributoValor valor : dto.getCamposFormulario()) {
//				FormularioAtributo atributo = FormularioAtributoDAO.get(valor.getCdFormularioAtributo(), conn);
//				if(atributo.getNmAtributo().equals("tpCnhCondutor")) {
//					tpCnh = valor.getTxtAtributoValor();
//					break;
//				}	
//			}
//			
//			System.out.println(tpCnh);
//			for(String cnh: tiposRequeridos)
//				System.out.println(cnh);
//			
//			if(!tiposRequeridos.contains(tpCnh)) {
//				return Optional.of("Categoria da CNH não compatível com o veículo da infração.");
//			}
//			
//			return Optional.empty();
//		} catch(Exception e) {
//			e.printStackTrace(System.out);
//			return Optional.of("Erro ao validar Caretogia da CNH.");
//		} finally {
//			Conexao.desconectar(conn);
//		}
//	}
}
