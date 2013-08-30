/*-
 * $Id$
 */
package com.intersystems.ora2isc.rules.constraints;

import com.intersystems.ora2isc.rules.Rule;

/**
 * @author Andrey Shcheglov <mailto:andrey.shcheglov@intersystems.com>
 */
public final class EnableConstraint implements Rule {
	/**
	 * @see Rule#transform(String)
	 */
	@Override
	public String transform(final String input) {
		/*
		 * Case insensitive + dotall mode.
		 */
		return input.replaceAll("(?is)(\\bconstraint[^;]+)(enable|disable)\\b", "$1");
	}

}
