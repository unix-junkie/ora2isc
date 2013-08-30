/*-
 * $Id$
 */
package com.intersystems.ora2isc;

import static java.awt.GridBagConstraints.RELATIVE;
import static java.awt.GridBagConstraints.REMAINDER;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * @author Andrey Shcheglov <mailto:andrey.shcheglov@intersystems.com>
 */
public final class AboutBox extends JDialog {
	private static final long serialVersionUID = 7027088988966908146L;

	public AboutBox(final Frame parent) {
		super(parent);

		this.setResizable(false);
		this.setTitle("About Cach\u00e9 Data Migration Wizard");
		this.setModal(true);
		final GridBagLayout gridBagLayout = new GridBagLayout();
		this.getContentPane().setLayout(gridBagLayout);

		final JLabel lblAboutCachueData = new JLabel("Cach\u00e9 Data Migration Wizard");
		final GridBagConstraints gbc_lblAboutCachueData = new GridBagConstraints();
		gbc_lblAboutCachueData.weightx = 1.0;
		gbc_lblAboutCachueData.gridwidth = REMAINDER;
		gbc_lblAboutCachueData.insets = new Insets(10, 10, 5, 10);
		this.getContentPane().add(lblAboutCachueData, gbc_lblAboutCachueData);

		final JLabel lblVersion = new JLabel("Version $Id$");
		final GridBagConstraints gbc_lblVersion = new GridBagConstraints();
		gbc_lblVersion.weightx = 1.0;
		gbc_lblVersion.gridwidth = REMAINDER;
		gbc_lblVersion.insets = new Insets(0, 10, 5, 10);
		this.getContentPane().add(lblVersion, gbc_lblVersion);

		final JLabel lblPleaseVisitUs = new JLabel("Please visit us at");
		final GridBagConstraints gbc_lblPleaseVisitUs = new GridBagConstraints();
		gbc_lblPleaseVisitUs.weightx = 1.0;
		gbc_lblPleaseVisitUs.gridwidth = REMAINDER;
		gbc_lblPleaseVisitUs.insets = new Insets(0, 10, 5, 10);
		this.getContentPane().add(lblPleaseVisitUs, gbc_lblPleaseVisitUs);

		final JTextField txtUrl = new JTextField();
		txtUrl.setBorder(new EmptyBorder(0, 0, 0, 0));
		txtUrl.setEditable(false);
		txtUrl.setHorizontalAlignment(SwingConstants.CENTER);
		txtUrl.setText("https://github.com/unix-junkie/ora2isc");
		final GridBagConstraints gbc_txtUrl = new GridBagConstraints();
		gbc_txtUrl.weightx = 1.0;
		gbc_txtUrl.gridwidth = REMAINDER;
		gbc_txtUrl.insets = new Insets(0, 10, 5, 10);
		gbc_txtUrl.fill = GridBagConstraints.HORIZONTAL;
		this.getContentPane().add(txtUrl, gbc_txtUrl);
		txtUrl.setColumns(10);

		final JSeparator separator = new JSeparator();
		final GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.anchor = GridBagConstraints.SOUTH;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.weighty = 1.0;
		gbc_separator.weightx = 1.0;
		gbc_separator.gridwidth = REMAINDER;
		gbc_separator.gridheight = RELATIVE;
		gbc_separator.insets = new Insets(0, 10, 5, 10);
		this.getContentPane().add(separator, gbc_separator);

		final JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				AboutBox.this.setVisible(false);
			}
		});
		final GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.insets = new Insets(0, 10, 10, 10);
		gbc_btnOk.weightx = 1.0;
		gbc_btnOk.gridwidth = REMAINDER;
		gbc_btnOk.gridheight = REMAINDER;
		gbc_btnOk.anchor = GridBagConstraints.EAST;
		this.getContentPane().add(btnOk, gbc_btnOk);
	}
}
