package com.beefsack.percy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RepositoryNotFoundException;

public class Account {
	private Git repo;
	private String name;

	/**
	 * Find all stored accounts in the storage locations.
	 * 
	 * @return stored accounts
	 */
	public static ArrayList<Account> findStoredAccounts(
			ArrayList<File> locations) {
		ArrayList<Account> accounts = new ArrayList<Account>();
		for (File location : locations) {
			for (File repoDir : location.listFiles()) {
				try {
					accounts.add(load(repoDir));
				} catch (IOException e) {
				}
			}
		}
		return accounts;
	}

	/**
	 * Load an account.
	 * 
	 * @param directory
	 *            the account directory
	 * @return loaded account
	 * @throws IOException
	 */
	public static Account load(File directory) throws IOException {
		Git repo;
		try {
			repo = Git.open(directory);
		} catch (RepositoryNotFoundException e) {
			repo = Git.init().setDirectory(directory).call();
		}
		return new Account().setRepo(repo);
	}

	/**
	 * Push changes to the remote.
	 * 
	 * @return this
	 */
	public Account push() {
		return this;
	}

	/**
	 * Pull changes from the remote.
	 * 
	 * @return this
	 */
	public Account pull() {
		return this;
	}

	/**
	 * @return the repo
	 */
	public Git getRepo() {
		return repo;
	}

	/**
	 * @param repo
	 *            the repo to set
	 * @return this
	 */
	public Account setRepo(Git repo) {
		this.repo = repo;
		File parent = repo.getRepository().getDirectory().getParentFile();
		setName(parent == null ? "" : parent.getName());
		return this;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 * @return account
	 */
	public Account setName(String name) {
		this.name = name;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

}
