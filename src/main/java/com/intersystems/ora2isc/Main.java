/*-
 * $Id$
 */
package com.intersystems.ora2isc;

import static java.awt.EventQueue.invokeLater;
import static java.util.Arrays.asList;
import static java.util.concurrent.Executors.newSingleThreadExecutor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.intersystems.ora2isc.rules.Rule;
import com.intersystems.ora2isc.rules.SqlPlus;
import com.intersystems.ora2isc.rules.constraints.EnableConstraint;
import com.intersystems.ora2isc.rules.constraints.Index;
import com.intersystems.ora2isc.rules.tables.CreateTable;
import com.intersystems.ora2isc.rules.tablespaces.CreateTablespace;
import com.intersystems.ora2isc.rules.types.CharLength;
import com.intersystems.ora2isc.rules.types.NumberPrecision;
import com.intersystems.ora2isc.rules.types.ReservedWordAsIdentifier;
import com.intersystems.ora2isc.rules.types.Timestamp;

/**
 * @author Andrey Shcheglov <mailto:andrey.shcheglov@intersystems.com>
 */
abstract class Main {
	private static ExecutorService background = newSingleThreadExecutor();

	private static final List<? extends Rule> PROCESSORS = asList(
			new SqlPlus(),
			new NumberPrecision(),
			new EnableConstraint(),
			new CreateTablespace(),
			new CreateTable(),
			new Index(),
			new CharLength(),
			new ReservedWordAsIdentifier(),
			new Timestamp()
	);

	private Main() {
		assert false;
	}

	/**
	 * @param file
	 * @param onCompletion
	 */
	static void enqueueFile(final File file, final Runnable onCompletion) {
		background.submit(new Runnable() {
			@Override
			public void run() {
				processFile(file);
				invokeLater(onCompletion);
			}
		});
	}

	/**
	 * @param file
	 */
	static void processFile(final File file) {
		try {
			final BufferedReader in = new BufferedReader(new FileReader(file));
			try {
				processReader(in, file.getPath());
			} finally {
				in.close();
			}
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * @param in
	 * @param path
	 * @throws IOException
	 */
	private static void processReader(final BufferedReader in, final String path) throws IOException {
		final StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = in.readLine()) != null) {
			stringBuilder.append(line).append('\n');
		}
		String contents = stringBuilder.toString();
		for (final Rule rule : PROCESSORS) {
			contents = rule.transform(contents);
		}
		final int i = path.lastIndexOf('.');
		final String newPath = path.substring(0, i) + "_Cach\u00e9" + path.substring(i);

		final PrintWriter out = new PrintWriter(newPath);
		try {
			out.println("--");
			out.println("-- Converted with Cach\u00e9 Data Migration Wizard.");
			out.println("-- Please visit us at <https://github.com/unix-junkie/ora2isc>.");
			out.println("--");
			out.print(contents);
			out.flush();
		} finally {
			out.close();
		}
		System.out.println(newPath + " written.");
		System.out.println("You can now run");
		System.out.println("d $system.SQL.DDLImport(\"Oracle\", $username, \"" + newPath + "\", , 1, , , 0)");
		System.out.println("from the Cach\u00e9 prompt.");
	}

	/**
	 * @param args
	 */
	public static void main(final String args[]) {
		invokeLater(new Runnable() {
			/**
			 * @see Runnable#run()
			 */
			@Override
			public void run() {
				try {
					final MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
