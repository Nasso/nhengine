package io.github.nasso.nhengine.tools.nhstudio.dialogs;

import io.github.nasso.nhengine.graphics.FontFamily.FontStyle;
import io.github.nasso.nhengine.tools.nhstudio.NhStudioApp;
import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.ui.control.UILabel;
import io.github.nasso.nhengine.ui.control.UITextField;
import io.github.nasso.nhengine.ui.layout.UIBorderLayout;
import io.github.nasso.nhengine.ui.layout.UIBoxLayout;

public class NewRoomDialog extends NewItemDialog {
	public NewRoomDialog(NhStudioApp nhStudioApp) {
		super("New Room", nhStudioApp);
		
		this.createUI();
	}
	
	private UITextField formTextField(UIContainer dest, String label) {
		UIContainer field = new UIContainer(new UIBorderLayout(8));
		field.add(new UILabel(label, UILabel.ANCHOR_LEFT, FontStyle.BOLD), UIBorderLayout.WEST);
		
		UITextField textField = new UITextField();
		field.add(textField, UIBorderLayout.EAST);
		
		dest.add(field);
		
		return textField;
	}
	
	private void createUI() {
		UIContainer formContent = new UIContainer(new UIBoxLayout(UIBoxLayout.VERTICAL, 4));
		this.formTextField(formContent, "Name:");
		
		UIContainer content = this.getContent();
		content.setLayout(new UIBorderLayout());
		content.add(formContent, UIBorderLayout.NORTH);
	}
}
