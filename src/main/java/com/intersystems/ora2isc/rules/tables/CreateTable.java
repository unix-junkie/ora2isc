/*-
 * $Id$
 */
package com.intersystems.ora2isc.rules.tables;

import com.intersystems.ora2isc.rules.Rule;

/**
 * Removes the following patterns from {@code CREATE TABLE statement}:
 * <ul>
 * 	<li>ORGANIZATION INDEX</li>
 * 	<li>PCTTHRESHOLD 50</li>
 * 	<li><pre>
 *      LOB (NAME_A) STORE AS SYS_LOB0000074853C00007$$
 *      (
 *        ENABLE STORAGE IN ROW
 *        CHUNK 8192
 *        PCTVERSION 10
 *        NOCACHE
 *        LOGGING
 *      )
 *	</pre></li>
 * </ul>
 *
 * @author Andrey Shcheglov <mailto:andrey.shcheglov@intersystems.com>
 */
public final class CreateTable implements Rule {
	private static final String PATTERNS[] = {
		"\\borganization\\s+index\\b",
		"\\bpctthreshold\\s+\\d+",
		"\\blob\\s*\\([^()]+\\)[^()]*\\([^()]+\\)"
	};

	/**
	 * @see Rule#transform(String)
	 */
	@Override
	public String transform(final String input) {
		/*
		 * Since the pattern starts with CREATE TABLE,
		 * and can contain sub-patterns in any order,
		 * it can't be processed in a single run.
		 */
		String s = input;
		for (final String pattern : PATTERNS) {
			s = s.replaceAll("(?is)(\\bcreate\\s+table[^;]+)" + pattern + "([^;]*;)", "$1 $2");
		}
		return s;
	}
}
