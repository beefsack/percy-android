package com.beefsack.percy;

import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig.Host;
import org.eclipse.jgit.util.FS;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class PercyJschConfigSessionFactory extends JschConfigSessionFactory {
	private PercyActivity activity;

	@Override
	protected void configure(Host arg0, Session arg1) {
	}

	@Override
	protected JSch getJSch(Host arg0, FS arg1) throws JSchException {
		JSch jsch = super.getJSch(arg0, arg1);
		// Clear identities and load the relevant one
		jsch.removeAllIdentity();
		jsch.addIdentity(getActivity().getKeyForAccount(
				getActivity().getActiveAccount()));
		return jsch;
	}

	/**
	 * @return the activity
	 */
	public PercyActivity getActivity() {
		return activity;
	}

	/**
	 * @param activity
	 *            the activity to set
	 */
	public void setActivity(PercyActivity activity) {
		this.activity = activity;
	}

}
