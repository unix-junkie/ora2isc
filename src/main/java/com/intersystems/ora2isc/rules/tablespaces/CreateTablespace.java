/*-
 * $Id$
 */
package com.intersystems.ora2isc.rules.tablespaces;

import com.intersystems.ora2isc.rules.Rule;
import com.intersystems.ora2isc.rules.SqlPlus;

/**
 * <p>Should run after {@link SqlPlus}.</p>
 *
 * @author Andrey Shcheglov <mailto:andrey.shcheglov@intersystems.com>
 */
public final class CreateTablespace implements Rule {
	/**
	 * @see com.intersystems.ora2isc.rules.Rule#transform(java.lang.String)
	 */
	@Override
	public String transform(final String input) {
		return input.replaceAll("(?is)\\bcreate\\s+tablespace\\b[^;]+;", "");
	}
}
