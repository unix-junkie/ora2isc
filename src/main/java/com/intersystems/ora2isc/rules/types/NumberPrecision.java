/*-
 * $Id$
 */
package com.intersystems.ora2isc.rules.types;

import com.intersystems.ora2isc.rules.Rule;

/**
 * Replaces the asterisk (*) in types like NUMBER(*, 0) and NUMBER(*)
 * with the default precision value (38).
 *
 * @author Andrey Shcheglov <mailto:andrey.shcheglov@intersystems.com>
 */
public final class NumberPrecision implements Rule {
	private static final int DEFAULT_PRECISION = 38;

	/**
	 * @see Rule#transform(String)
	 */
	@Override
	public String transform(final String input) {
		return input.replaceAll("(?is)\\bnumber\\b\\(\\s*\\*\\s*(\\,\\s*\\-?\\d+\\s*)?\\)",
				"NUMBER(" + DEFAULT_PRECISION + "$1)");
	}
}
