/*-
 * $Id$
 */
package com.intersystems.ora2isc.rules.types;

import com.intersystems.ora2isc.rules.Rule;

/**
 * @author Andrey Shcheglov <mailto:andrey.shcheglov@intersystems.com>
 */
public final class CharLength implements Rule {
	private static final String CHAR_TYPES[] = {
		"char",
		"varchar2",
	};

	private static CharSequence listTypes() {
		final StringBuilder builder = new StringBuilder();
		for (final String type : CHAR_TYPES) {
			builder.append(type).append('|');
		}
		if (builder.length() != 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder;
	}

	/**
	 * @see Rule#transform(String)
	 */
	@Override
	public String transform(final String input) {
		return input.replaceAll("(?is)(\\b(" + listTypes() + ")\\s*\\(\\d+)\\s+(byte|char)\\s*(\\))", "$1$4");
	}
}
