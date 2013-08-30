/*-
 * $Id$
 */
package com.intersystems.ora2isc.rules;

/**
 * <p>Filters out <em>SQLPlus</em>-specific extensions, such as
 * {@code PROMPT} or {@SPOOL}.</p>
 *
 * <p>Should be the first rule to run.</p>
 *
 * @author Andrey Shcheglov <mailto:andrey.shcheglov@intersystems.com>
 */
public final class SqlPlus implements Rule {
	/**
	 * @see Rule#transform(String)
	 */
	@Override
	public String transform(final String input) {
		/*
		 * We need multi-line mode here in order to match
		 * the beginning and end of each line.
		 */
		return input.replaceAll("(?im)^\\s*(prompt|spool)\\s+\\S+.*$", "");
	}
}
