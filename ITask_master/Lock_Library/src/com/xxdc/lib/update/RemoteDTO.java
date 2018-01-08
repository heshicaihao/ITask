package com.xxdc.lib.update;

import java.io.Serializable;

public class RemoteDTO implements Serializable{
	private static final long serialVersionUID = 1778516506980177889L;

	private String download;
	
	private String system;
	
	private String terminal;
	
	private int version;

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
