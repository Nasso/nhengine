package io.github.nasso.nhengine.tools.nhstudio.dialogs;

import io.github.nasso.nhengine.tools.nhstudio.NhStudioApp;
import io.github.nasso.nhengine.ui.control.UIDialog;

public class NewItemDialog extends UIDialog {
	private NhStudioApp app;
	
	public NewItemDialog(String title, NhStudioApp nhStudioApp) {
		super(title, true);
		this.app = nhStudioApp;
	}
	
	public NhStudioApp getApp() {
		return this.app;
	}
}
