package io.github.nasso.nhengine.ui.control;

import java.util.function.Consumer;

import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.ui.layout.UIBorderLayout;
import io.github.nasso.nhengine.ui.layout.UIFlowLayout;

public class UIMessageBox extends UIDialog {
	public static enum MessageType {
		INFORMATION, WARNING, ERROR
	}
	
	private Consumer<UIMessageBox> onOkay = null;
	
	public UIMessageBox(CharSequence message) {
		this(message, MessageType.INFORMATION);
	}
	
	public UIMessageBox(CharSequence message, MessageType messageType) {
		this(message, messageType, true);
	}
	
	public UIMessageBox(CharSequence message, MessageType messageType, boolean modal) {
		super(messageType == MessageType.INFORMATION ? "Information" : messageType == MessageType.WARNING ? "Warning!" : messageType == MessageType.ERROR ? "Error" : "Message");
		
		UIButton okayBtn = new UIButton("Okay");
		okayBtn.setOnAction((btn) -> {
			if(this.onOkay != null) this.onOkay.accept(this);
			this.close();
		});
		
		UIContainer bottomContainer = new UIContainer(new UIFlowLayout(UIFlowLayout.RIGHT, 8, 0));
		bottomContainer.add(okayBtn);
		
		UIContainer c = this.getContent();
		c.setPadding(16);
		c.setLayout(new UIBorderLayout(16));
		c.add(new UILabel(message), UIBorderLayout.CENTER);
		c.add(bottomContainer, UIBorderLayout.SOUTH);
	}
	
	public Consumer<UIMessageBox> getOnOkay() {
		return this.onOkay;
	}
	
	public void setOnOkay(Consumer<UIMessageBox> onOkay) {
		this.onOkay = onOkay;
	}
}
