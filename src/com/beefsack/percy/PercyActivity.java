package com.beefsack.percy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.util.Base64;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class PercyActivity extends Activity {
	public static final String ACCOUNTS_DIR_NAME = "accounts";
	public static final String KEYS_DIR_NAME = "keys";
	public static final String MAIN_KEY_NAME = "main_key";
	public static final Integer MAIN_KEY_SIZE = 2048;
	public static final String MAIN_KEY_ALGORITHM = "RSA";
	private ArrayList<Account> accounts;
	private Account activeAccount;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Override session factory to allow custom keys
		PercyJschConfigSessionFactory jsch = new PercyJschConfigSessionFactory();
		jsch.setActivity(this);
		SshSessionFactory.setInstance(jsch);
		// Load accounts
		setAccounts(Account.findStoredAccounts(getAccountLocations()));
		// Display
		setContentView(R.layout.main);
		// Make beautiful
		EditText keyElement = (EditText) findViewById(R.id.EditTextPrivateKey); 
		keyElement.setText(getMainKey());
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
	 * @return percy activity
	 */
	public PercyActivity setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
		return this;
	}

	/**
	 * @param account
	 *            the account to get a key for
	 * @return key for account
	 */
	public String getKeyForAccount(Account account) {
		for (File keyDir : getKeyLocations()) {
			File accountKey = new File(keyDir.getAbsolutePath()
					+ File.separator + account.getName());
			if (!accountKey.exists())
				continue;
			return accountKey.getName();
		}
		// Fall back to main key
		return getMainKey();
	}

	/**
	 * @return main key for percy
	 */
	public String getMainKey() {
		File mainKey = getMainKeyFile();
		try {
			return readFileToString(mainKey);
		} catch (IOException e) {
		}
		String key = generateMainKey();
		try {
			writeStringToFile(key, mainKey);
		} catch (IOException e) {
			throw new RuntimeException("Unable to create new main key.");
		}
		return key;
	}
	
	/**
	 * @return main key file
	 */
	public File getMainKeyFile() {
		return new File(getInternalKeyLocation().getAbsolutePath()
				+ File.separator + MAIN_KEY_NAME);
	}
	
	/**
	 * @return new main key
	 */
	public String generateMainKey() {
		// Generate a new key
		KeyPairGenerator keyGen;
		try {
			keyGen = KeyPairGenerator.getInstance(MAIN_KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e1) {
			throw new RuntimeException("Incorrect algorithm.");
		}
		keyGen.initialize(MAIN_KEY_SIZE);
		KeyPair mainKeyPair = keyGen.genKeyPair();
		StringBuffer keyBuffer = new StringBuffer();
		String lineSeparator = System.getProperty("line.separator");
		keyBuffer.append("-----BEGIN " + MAIN_KEY_ALGORITHM
				+ " PRIVATE KEY-----" + lineSeparator);
		keyBuffer.append(Base64.encodeBytes(mainKeyPair.getPrivate()
				.getEncoded()));
		keyBuffer.append(lineSeparator + "-----END " + MAIN_KEY_ALGORITHM
				+ " PRIVATE KEY-----" + lineSeparator);
		return keyBuffer.toString();		
	}

	/**
	 * @return directory locations for accounts
	 */
	public ArrayList<File> getAccountLocations() {
		ArrayList<File> accountLocations = new ArrayList<File>();
		accountLocations.add(getInternalAccountLocation()); // Internal
		return accountLocations;
	}

	/**
	 * @return internal account directory
	 */
	public File getInternalAccountLocation() {
		return getDir(ACCOUNTS_DIR_NAME, MODE_PRIVATE);
	}

	/**
	 * @return directory locations for key
	 */
	public ArrayList<File> getKeyLocations() {
		ArrayList<File> keyLocations = new ArrayList<File>();
		keyLocations.add(getInternalKeyLocation()); // Internal
		return keyLocations;
	}

	/**
	 * @return internal key directory
	 */
	public File getInternalKeyLocation() {
		return getDir(KEYS_DIR_NAME, MODE_PRIVATE);
	}

	/**
	 * @param file
	 * @return contents of file
	 * @throws IOException
	 */
	public static String readFileToString(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuffer input = new StringBuffer();
		String line;
		String lineSeparator = System.getProperty("line.separator");
		while ((line = reader.readLine()) != null) {
			input.append(line).append(lineSeparator);
		}
		reader.close();
		return input.toString();
	}

	/**
	 * @param string
	 * @param file
	 * @throws IOException
	 */
	public static void writeStringToFile(String string, File file)
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		writer.write(string);
		writer.close();
	}

	/**
	 * @return the activeAccount
	 */
	public Account getActiveAccount() {
		return activeAccount;
	}

	/**
	 * @param activeAccount
	 *            the activeAccount to set
	 * @return percy activity
	 */
	public PercyActivity setActiveAccount(Account activeAccount) {
		this.activeAccount = activeAccount;
		return this;
	}

}
