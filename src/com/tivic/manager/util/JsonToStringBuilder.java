package com.tivic.manager.util;

import java.util.Arrays;
import java.util.Collection;

/**
 * Encontrei em:
 * https://stackoverflow.com/a/42656488/12312780
 */

/**
 * Builds a toString() method for Eclipse IDE, that generates the toString
 * result in JSON format.</br>.
 */
public class JsonToStringBuilder {
	private StringBuilder sb = new StringBuilder();

	/**
	 * Constructor with Object parameter. Required by the Eclipse ToString()
	 * builder.
	 * 
	 * @param o
	 */
	public JsonToStringBuilder(Object o) {
		super();
	}

	/**
	 * @param fieldName
	 * @param fieldValue
	 * @return this
	 */
	public JsonToStringBuilder append(final String fieldName,
			final Object fieldValue) {
		sb.append(sb.length() == 0 ? "" : ",").append("\"").append(fieldName)
				.append("\" : ");

		if (!(fieldValue instanceof Collection<?> || fieldValue instanceof Object[])) {
			sb.append("\"");
		}

		{
			if (fieldValue instanceof Collection<?>
					|| fieldValue instanceof Object[]) {
				sb.append("[");
				new ForEach(fieldValue) {
					@Override
					public void e(Object o, int i, int max) {
						sb.append("\"" + String.valueOf(o) + "\"");
						if (!(i == max - 1))
							sb.append(",");
					}
				}.x();
				sb.append("]");
			} else {
				sb.append(String.valueOf(fieldValue));
			}
		}

		if (!(fieldValue instanceof Collection<?> || fieldValue instanceof Object[])) {
			sb.append("\"");
		}
		return this;
	}

	/**
	 * Builds the String representation of the object
	 */
	public String build() {
		return "{" + sb.toString() + "}";
	}
	
	public String toString() {
		return build();
	}
}

abstract class ForEach {
	private Collection<?> c;

	public ForEach(Object c) {
		if (c instanceof Object[])
			c = (Collection<?>) Arrays.asList((Object[]) c);

		this.c = (Collection<?>) c;
	}

	void x() {
		int i = 0;
		for (Object o : c) {
			e(o, i, c.size());
			i++;
		}
	}

	public abstract void e(Object o, int i, int n);
}
