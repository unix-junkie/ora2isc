/*-
 * $Id$
 */
package com.intersystems.ora2isc;

import static java.lang.System.exit;
import static java.lang.System.getProperty;
import static java.util.Arrays.asList;
import static java.util.prefs.Preferences.userNodeForPackage;
import static javax.swing.Box.createHorizontalGlue;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.FILES_ONLY;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.filechooser.FileFilter;

/**
 * @author Andrey Shcheglov <mailto:andrey.shcheglov@intersystems.com>
 */
final class MainFrame extends JFrame {
	private static final long serialVersionUID = 5977560036150369545L;

	private JPanel contentPane;

	JLabel statusBar;

	File selectedFile;

	JProgressBar progressBar;

	JButton button;

	JMenuItem mntmOpen;
	private JMenu mnHelp;
	private JMenuItem mntmAbout;

	public MainFrame() {
		this.setTitle("Cach\u00e9 Data Migration Wizard");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);

		final JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		final JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);

		this.mntmOpen = new JMenuItem("Open");
		this.mntmOpen.setMnemonic('O');
		this.mntmOpen.addActionListener(new ActionListener() {
			/**
			 * @see ActionListener#actionPerformed(ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(MainFrame.this.getLastSelectedDirectory());
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileSelectionMode(FILES_ONLY);
				fileChooser.setDialogTitle("Open Oracle DDL file");
				fileChooser.setFileFilter(new FileFilter() {
					/**
					 * @see FileFilter#getDescription()
					 */
					@Override
					public String getDescription() {
						return "SQL and DDL files";
					}

					/**
					 * @see FileFilter#accept(File)
					 */
					@Override
					public boolean accept(final File f) {
						final String basename = f.getName();
						final int i = basename.indexOf('.');
						final boolean hasExtension = i != -1;
						final String extension = hasExtension ? basename.substring(i + 1).toLowerCase() : "";
						final List<String> extensions = asList("sql", "ddl");
						return f.isDirectory() || hasExtension && extensions.contains(extension);
					}
				});
				final int option = fileChooser.showOpenDialog(MainFrame.this);
				if (option == APPROVE_OPTION) {
					MainFrame.this.statusBar.setText((MainFrame.this.selectedFile = fileChooser.getSelectedFile()).getAbsolutePath());
					MainFrame.this.setLastSelectedDirectory(MainFrame.this.selectedFile.getParentFile());
					MainFrame.this.button.setEnabled(true);
				}
			}
		});
		this.mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(this.mntmOpen);

		final JSeparator separator = new JSeparator();
		mnFile.add(separator);

		final JMenuItem mntmExit = new JMenuItem("Exit to DOS");
		mntmExit.setMnemonic('X');
		mntmExit.addActionListener(new ActionListener() {
			/**
			 * @see ActionListener#actionPerformed(ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				exit(0);
			}
		});
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK));
		mnFile.add(mntmExit);

		menuBar.add(createHorizontalGlue());

		this.mnHelp = new JMenu("Help");
		this.mnHelp.setMnemonic('H');
		menuBar.add(this.mnHelp);

		this.mntmAbout = new JMenuItem("About Cach\u00e9 Data Migration Wizard");
		this.mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final AboutBox aboutBox = new AboutBox(MainFrame.this);
				aboutBox.pack();
				final Point location = MainFrame.this.getLocation();
				final Dimension parentSize = MainFrame.this.getSize();
				final Dimension size = aboutBox.getSize();
				location.x += (parentSize.width - size.width) / 2;
				location.y += (parentSize.height - size.height) / 2;
				aboutBox.setLocation(location);
				aboutBox.setVisible(true);
			}
		});
		this.mntmAbout.setMnemonic('A');
		this.mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		this.mnHelp.add(this.mntmAbout);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(5, 5));
		this.setContentPane(this.contentPane);

		this.statusBar = new JLabel("<No file selected>");
		this.statusBar.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		this.contentPane.add(this.statusBar, BorderLayout.SOUTH);

		this.progressBar = new JProgressBar();
		this.contentPane.add(this.progressBar, BorderLayout.NORTH);

		this.button = new JButton("Do the magic!");
		this.button.setEnabled(false);
		this.button.addActionListener(new ActionListener() {
			/**
			 * @see ActionListener#actionPerformed(ActionEvent)
			 */
			@Override
			public void actionPerformed(final ActionEvent e) {
				MainFrame.this.mntmOpen.setEnabled(false);
				MainFrame.this.button.setEnabled(false);
				MainFrame.this.progressBar.setIndeterminate(true);
				Main.enqueueFile(MainFrame.this.selectedFile, new Runnable() {
					/**
					 * @see Runnable#run()
					 */
					@Override
					public void run() {
						MainFrame.this.mntmOpen.setEnabled(true);
						MainFrame.this.button.setEnabled(true);
						MainFrame.this.progressBar.setIndeterminate(false);
					}
				});
			}
		});
		this.button.setDefaultCapable(false);
		this.contentPane.add(this.button, BorderLayout.CENTER);
	}

	File getLastSelectedDirectory() {
		final Preferences preferences = userNodeForPackage(this.getClass());
		final String lastSelectedDirectory = preferences.get("last.selected.directory", getProperty("user.home"));
		return new File(lastSelectedDirectory);
	}

	/**
	 * @param lastSelectedDirectory
	 */
	void setLastSelectedDirectory(final File lastSelectedDirectory) {
		final Preferences preferences = userNodeForPackage(this.getClass());
		preferences.put("last.selected.directory", lastSelectedDirectory.getAbsolutePath());
	}
}
