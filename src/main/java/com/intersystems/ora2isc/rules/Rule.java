/*-
 * $Id$
 */
package com.intersystems.ora2isc.rules;

/**
 * @author Andrey Shcheglov <mailto:andrey.shcheglov@intersystems.com>
 */
public interface Rule {
	/**
	 * @param input
	 */
	String transform(String input);
}
