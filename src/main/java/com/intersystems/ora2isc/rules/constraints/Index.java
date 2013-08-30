/*-
 * $Id$
 */
package com.intersystems.ora2isc.rules.constraints;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intersystems.ora2isc.rules.Rule;

/**
 * <p>Removes the following patterns from {@code CONSTRAINT} specifications:
 * <ul>
 * 	<li>USING INDEX T_CLOB_KL1 (unfortunately, existing indices can&apos;t be used)</li>
 * 	<li><pre>
 *	  USING INDEX
 *	  (
 *	      CREATE UNIQUE INDEX F_TARIF_SETKA_K1 ON F_TARIF_SETKA (ID ASC)
 *	      LOGGING
 *	      TABLESPACE "MEDBAZA"
 *	      PCTFREE 10
 *	      INITRANS 2
 *	      STORAGE
 *	      (
 *	        INITIAL 65536
 *	        NEXT 1048576
 *	        MINEXTENTS 1
 *	        MAXEXTENTS UNLIMITED
 *	        BUFFER_POOL DEFAULT
 *	      )
 *	  )
 * 	</pre></li>
 * </ul></p>
 *
 * <p>Additionally, any {@code CREATE (UNIQUE)? INDEX} statement
 * should be immediately terminated with a semicolon &mdash;
 * no subsequent syntactic noise, no whitespace.</p>
 *
 * @author Andrey Shcheglov <mailto:andrey.shcheglov@intersystems.com>
 */
public final class Index implements Rule {
	public static final String CREATE_INDEX_PATTERN = "(\\bcreate\\s+(unique\\s+)?index\\s+(\\S+)\\s+on\\s+\\S+\\s+\\([^()]+\\))[^()]*(\\bstorage\\s*\\([^()]+\\))?";

	private static final String USING_INDEX_PATTERNS[] = {
		"\\s+([^\\s()]+)", // Named indices
		"\\s*\\(\\s*" + CREATE_INDEX_PATTERN +  "\\s*\\)" // Anonymous indices
	};

	private static CharSequence listUsingIndexPatterns() {
		final StringBuilder builder = new StringBuilder();
		for (final String usingIndexPattern : USING_INDEX_PATTERNS) {
			builder.append(usingIndexPattern).append('|');
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
		final List<String> unusedIndices = new ArrayList<String>();

		/*
		 * Process USING INDEX clauses and collect the names of unused indices.
		 */
		final Pattern usingIndexPattern = Pattern.compile("(?is)\\busing\\s+index(" + listUsingIndexPatterns() + ')');
		final Matcher usingIndexMatcher = usingIndexPattern.matcher(input);
		while (usingIndexMatcher.find()) {
			final String indexName = usingIndexMatcher.group(2);
			if (indexName == null) {
				continue;
			}
			final int i = indexName.indexOf('.');
			final String shortIndexName = i == -1 ? indexName : indexName.substring(i + 1);
			unusedIndices.add(shortIndexName);
		}
		final String s = usingIndexMatcher.replaceAll("");

		/*
		 * Process CREATE INDEX statements.
		 */
		final Pattern createIndexPattern = Pattern.compile("(?is)" + CREATE_INDEX_PATTERN + "\\s*(;)");
		final Matcher createIndexMatcher = createIndexPattern.matcher(s);
		if (createIndexMatcher.find()) {
			final StringBuffer builder = new StringBuffer();
			do {
				final String indexName = createIndexMatcher.group(3);
				final int i = indexName.indexOf('.');
				final String shortIndexName = i == -1 ? indexName : indexName.substring(i + 1);
				if (unusedIndices.contains(shortIndexName)) {
					System.out.println("Unused index will be removed: " + shortIndexName);
				}
				/*
				 * If the index is not in the unused list,
				 * then apply the regular filter.
				 *
				 * Otherwise, remove the subsequence.
				 */
				createIndexMatcher.appendReplacement(builder, unusedIndices.contains(shortIndexName) ? "" : "$1$5");
			} while (createIndexMatcher.find());
			createIndexMatcher.appendTail(builder);
			return builder.toString();
		}

		/*
		 * If threre're no CREATE INDEX statements,
		 * just return the original string.
		 */
		return s;
	}
}
