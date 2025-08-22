package com.tivic.manager.mob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Types;
import java.util.Optional;

import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.validation.Validator;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class TalonarioTransporteValidator implements Validator<Talonario> {

	private Talonario talonario;

	@Override
	public Optional<String> validate(Talonario object) {
		this.talonario = object;

		try {
			Method[] methods = this.getClass().getDeclaredMethods();

			for (Method method : methods) {
				if (method.getName().endsWith("validate"))
					continue;

				@SuppressWarnings("unchecked")
				Optional<String> op = ((Optional<String>) method.invoke(this));
				if (op.isPresent())
					return op;
			}

			return Optional.empty();
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			ex.printStackTrace(System.out);
			return Optional.of(ex.getMessage());
		}
	}

	public Optional<String> validateTipoTalao() {

		if (this.talonario.getCdTalao() == 0) {
			ResultSetMap rsm = TalonarioServices.find(new Criterios()
					.add("A.nr_talao", Integer.toString(this.talonario.getNrTalao()), ItemComparator.EQUAL, Types.INTEGER)
					.add("A.tp_talao", Integer.toString(this.talonario.getTpTalao()), ItemComparator.EQUAL, Types.INTEGER));

			if (rsm.next()) {
				return Optional.of("Já existe um talão com esse número e desse tipo.");
			}
		}
		return Optional.empty();
	}

	public Optional<String> validateSituacaoTalao() {
		if (this.talonario.getStTalao() == TalonarioServices.ST_TALAO_ATIVO) {
			if (this.talonario.getCdAgente() <= 0)
				return Optional.of("Para ativar o talonário, é necessário a vinculação de um agente.");

			if (this.talonario.getDtEntrega() == null)
				return Optional.of("Não é permitido ativar um talão sem preencher a data de entrega.");

		} else {
			if (this.talonario.getCdAgente() > 0 && this.talonario.getStTalao() == TalonarioServices.ST_TALAO_INATIVO)
				return Optional.of("Talonário inativo não pode ter agente vinculado.");

			if (this.talonario.getDtEntrega() != null)
				return Optional.of("Talonário inativo não pode ter uma data de entrega.");
		}
		return Optional.empty();
	}
}
