package com.beefsack.percy;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RepositoryNotFoundException;

import android.app.Activity;
import android.os.Bundle;

public class PercyActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		File repoDir = getDir("repo", MODE_PRIVATE);
		try {
			Git git;
			try {
				git = Git.open(repoDir);
			} catch (RepositoryNotFoundException e) {
				git = Git.init().setDirectory(repoDir).call();
			}
			System.out.println(git.status().call().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
