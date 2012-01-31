package com.beefsack.percy;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jgit.transport.SshSessionFactory;

import android.app.Activity;
import android.os.Bundle;

public class PercyActivity extends Activity {
	public static final String REPOS_DIR_NAME = "repos";
	public static final String KEYS_DIR_NAME = "keys";
	private ArrayList<Account> accounts;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Override session factory to allow custom keys
		SshSessionFactory.setInstance(new PercyJschConfigSessionFactory());
		// Load accounts
		ArrayList<File> locations = new ArrayList<File>();
		locations.add(getDir(REPOS_DIR_NAME, MODE_PRIVATE)); // Internal
		accounts = Account.findStoredAccounts(locations);
		// Display
		setContentView(R.layout.main);
	}

	/**
	 * @return the accounts
	 */
	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts
	 *            the accounts to set
	 */
	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}

}
