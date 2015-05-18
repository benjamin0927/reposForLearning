package org.benjamin.ci.modules.git;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * https://github.com/rimerosolutions/ant-git-tasks
 */
public class GIT {
	String command;
	String url;
	String userName;
	String password;
	File directory;
	String branchName;
	
	public UsernamePasswordCredentialsProvider getUsernamePasswordCredentialsProvider(){
		return new UsernamePasswordCredentialsProvider(userName, password);
	}
	
	public void gitClone(){
		try {
			Git.cloneRepository().setURI(this.getUrl()).setDirectory(this.getDirectory())
				.setCredentialsProvider(this.getUsernamePasswordCredentialsProvider()).call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void gitCheckout(){
		try {
			Git.open(this.getDirectory()).checkout().setName(this.getBranchName()).call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void resetHard(){
		try {
			Git.open(this.getDirectory()).reset().setMode(ResetType.HARD).call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void gitClean(){
		try {
			Git.open(this.getDirectory()).clean().setCleanDirectories(true).call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void gitPullRebase(){
		try {
			Git.open(this.getDirectory()).pull().setRemoteBranchName(this.getBranchName()).setRebase(true)
				.setCredentialsProvider(this.getUsernamePasswordCredentialsProvider()).call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public File getDirectory() {
		return directory;
	}

	public void setDirectory(File directory) {
		this.directory = directory;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
}
