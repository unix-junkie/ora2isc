/*-
 * $Id$
 */
package com.intersystems.ora2isc.rules.types;

import com.intersystems.ora2isc.rules.Rule;

/**
 * @author Andrey Shcheglov <mailto:andrey.shcheglov@intersystems.com>
 */
public final class Timestamp implements Rule {
	/**
	 * @see Rule#transform(String)
	 */
	@Override
	public String transform(final String input) {
		return input.replaceAll("(?is)\\btimestamp(\\s*\\(\\s*\\d+\\s*\\))?", "%TimeStamp");
	}
}
