package com.beefsack.percy;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class AccountActivity extends Activity {
	private Account account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		try {
			setAccount(Account.load(new File(bundle.getString("path"))));
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(),
					"Unable to open account.", Toast.LENGTH_LONG).show();
			finish();
		}
		setContentView(R.layout.account);
		Toast.makeText(getApplicationContext(),
				"Opened " + getAccount().getName() + ".", Toast.LENGTH_LONG).show();
	}

	/**
	 * @return the account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 * @return this activity
	 */
	public AccountActivity setAccount(Account account) {
		this.account = account;
		return this;
	}

}
