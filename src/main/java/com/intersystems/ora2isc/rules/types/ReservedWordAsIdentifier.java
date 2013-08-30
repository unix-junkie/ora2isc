/*-
 * $Id$
 */
package com.intersystems.ora2isc.rules.types;

import com.intersystems.ora2isc.rules.Rule;

/**
 * @author Andrey Shcheglov <mailto:andrey.shcheglov@intersystems.com>
 */
public final class ReservedWordAsIdentifier implements Rule {
	/**
	 * @see Rule#transform(String)
	 */
	@Override
	public String transform(final String input) {
		/*
		 * Replaces COUNT used as an identifier with "COUNT".
		 * Doesn't replace "COUNT" and COUNT() occurrences.
		 */
		return input.replaceAll("(?is)([^\"])\\b(count)\\b(\\s*[^()\\s]+)", "$1\"$2\"$3");
	}
}
