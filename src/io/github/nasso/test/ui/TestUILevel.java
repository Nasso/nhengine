package io.github.nasso.test.ui;

import java.io.IOException;
import java.util.function.Consumer;

import org.joml.Vector2f;

import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.data.TextureIO;
import io.github.nasso.nhengine.data.TrueTypeFont;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.FontFamily;
import io.github.nasso.nhengine.graphics.FontFamily.FontStyle;
import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.level.Level;
import io.github.nasso.nhengine.level.Scene;
import io.github.nasso.nhengine.ui.UICanvas;
import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.ui.control.UIButton;
import io.github.nasso.nhengine.ui.control.UIDialog;
import io.github.nasso.nhengine.ui.control.UIImageView;
import io.github.nasso.nhengine.ui.control.UILabel;
import io.github.nasso.nhengine.ui.control.UIMenu;
import io.github.nasso.nhengine.ui.control.UIMenuBar;
import io.github.nasso.nhengine.ui.control.UIMenuItem;
import io.github.nasso.nhengine.ui.control.UIMenuSeparator;
import io.github.nasso.nhengine.ui.control.UIMessageBox;
import io.github.nasso.nhengine.ui.control.UIMessageBox.MessageType;
import io.github.nasso.nhengine.ui.control.UIPopupMenu;
import io.github.nasso.nhengine.ui.control.UIProgressBar;
import io.github.nasso.nhengine.ui.control.UIScrollPane;
import io.github.nasso.nhengine.ui.control.UISeparator;
import io.github.nasso.nhengine.ui.control.UISlider;
import io.github.nasso.nhengine.ui.control.UISplitPane;
import io.github.nasso.nhengine.ui.control.UITabbedPane;
import io.github.nasso.nhengine.ui.control.UITextField;
import io.github.nasso.nhengine.ui.control.UIToggleButton;
import io.github.nasso.nhengine.ui.control.UIToggleButtonGroup;
import io.github.nasso.nhengine.ui.layout.UIBorderLayout;
import io.github.nasso.nhengine.ui.layout.UIBoxLayout;
import io.github.nasso.nhengine.ui.layout.UIGridLayout;
import io.github.nasso.nhengine.ui.theme.UIDefaultDarkTheme;
import io.github.nasso.nhengine.ui.theme.UIDefaultLightTheme;
import io.github.nasso.nhengine.ui.theme.UITheme;
import io.github.nasso.nhengine.utils.FloatTransition;
import penner.easing.Bounce;

public class TestUILevel extends Level {
	private UICanvas cvs = null;
	
	private UITheme darkTheme = new UIDefaultDarkTheme();
	private UITheme lightTheme = new UIDefaultLightTheme();
	
	private Scene sce = new Scene();
	
	private Vector2f frameSize = new Vector2f();
	
	private UIContainer leftPane, demoContainer;
	private UIToggleButton toggleShowBounds;
	
	private FloatTransition leftPaneTrans = new FloatTransition(1, this::leftPaneTransitionStep, -300, Bounce::easeOut);
	
	private FontFamily arialFamily = new FontFamily();
	
	public TestUILevel(float w, float h) {
		try {
			this.arialFamily.setRegular(new TrueTypeFont("res/fonts/arial.ttf", 18, true, true));
			this.arialFamily.setRegularItalic(new TrueTypeFont("res/fonts/ariali.ttf", 18, true, true));
			this.arialFamily.setBold(new TrueTypeFont("res/fonts/arialbd.ttf", 18, true, true));
			this.arialFamily.setBoldItalic(new TrueTypeFont("res/fonts/arialbi.ttf", 18, true, true));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.cvs = new UICanvas(this.darkTheme, (int) w, (int) h);
		
		this.leftPane = new UIContainer();
		this.leftPane.setBackground(Color.get(0, 0, 0, 0.1f));
		this.leftPane.setLayout(new UIGridLayout(0, 1, 8, 8));
		this.leftPane.setBounds(-300, 0, 300, h);
		this.leftPane.setPadding(16);
		
		this.toggleShowBounds = new UIToggleButton("Show component bounds", (btn) -> {
			this.cvs.set_showComponentBounds(btn.isSelected());
		});
		
		StringBuffer themeToggleText = new StringBuffer("Switch to light theme");
		UIToggleButton themeToggle = new UIToggleButton("Light theme", (btn) -> {
			if(btn.isSelected()) {
				themeToggleText.replace(0, themeToggleText.length(), "Switch to dark theme");
				this.cvs.setTheme(this.lightTheme);
			} else {
				themeToggleText.replace(0, themeToggleText.length(), "Switch to light theme");
				this.cvs.setTheme(this.darkTheme);
			}
		});
		
		UIButton themeToggleButton = new UIButton(themeToggleText, (btn) -> {
			themeToggle.doClick();
		});
		
		UIButton hideButton = new UIButton("Hide", (btn) -> {
			this.leftPaneTrans.setTargetValue(-this.leftPane.getWidth());
		});
		
		UIToggleButtonGroup group = new UIToggleButtonGroup(0, 2);
		UIToggleButton toggleGood = new UIToggleButton("Good", null, group);
		UIToggleButton toggleCheap = new UIToggleButton("Cheap", null, group);
		UIToggleButton toggleFast = new UIToggleButton("Fast", null, group);
		
		this.toggleShowBounds.setOpaque(false);
		themeToggle.setOpaque(false);
		themeToggleButton.setOpaque(false);
		hideButton.setOpaque(false);
		toggleGood.setOpaque(false);
		toggleCheap.setOpaque(false);
		toggleFast.setOpaque(false);
		
		this.leftPane.addAll(this.toggleShowBounds, themeToggle, themeToggleButton, hideButton, toggleGood, toggleCheap, toggleFast);
		
		this.demoContainer = this.createDemoContainer();
		
		UIContainer contentPane = this.cvs.getRootPane().getContentPane();
		contentPane.setLayout(new UIBorderLayout());
		contentPane.add(this.demoContainer, UIBorderLayout.CENTER);
		contentPane.add(this.leftPane);
		
		// @format:off
		UIMenuBar bar = new UIMenuBar(new UIMenuItem[] {
				new UIMenu("File", new UIMenuItem[] {
						new UIMenu("New", new UIMenuItem[] {
								new UIMenuItem("Java project"),
								new UIMenuItem("Project")
						}),
						new UIMenuItem("Save"),
						new UIMenuItem("Save as"),
						new UIMenuItem(this.loadImage("folder.png"), "Open")
				}),
				new UIMenu("Edit", new UIMenuItem[] {
						new UIMenuItem("Undo"),
						new UIMenuItem("Redo"),
						new UIMenuSeparator(),
						new UIMenuItem("Copy"),
						new UIMenuItem("Cut"),
						new UIMenuItem("Paste")
				}),
				new UIMenu("Refactor", new UIMenuItem[] {
						new UIMenuItem("Rename"),
						new UIMenuItem("Move")
				}),
				new UIMenu("Source", new UIMenuItem[] {
						new UIMenuItem("Toggle comment"),
						new UIMenuItem("Indent"),
						new UIMenuItem("Organize imports")
				}),
				new UIMenu("Help", new UIMenuItem[] {
						new UIMenuItem("Check for updates"),
						new UIMenuItem("About")
				}),
		});
		// @format:on
		
		this.cvs.getRootPane().setMenuBar(bar);
		
		this.sce.setRoot(this.cvs);
		
		this.addOverlayScene(this.sce);
	}
	
	private Texture2D loadImage(CharSequence name) {
		try {
			return TextureIO.loadTexture2D("res/demo/images/" + name, 4, false, true, true, true);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private UIContainer createDemoContainer() {
		// - BUTTONS -
		UIContainer iconButtonsPane = new UIContainer();
		iconButtonsPane.setLayout(new UIBoxLayout(UIBoxLayout.HORIZONTAL));
		iconButtonsPane.setPadding(8, 0);
		iconButtonsPane.add(new UIButton(this.loadImage("play.png")));
		iconButtonsPane.add(new UIButton(this.loadImage("settings.png")));
		iconButtonsPane.add(new UIButton(this.loadImage("cloud.png")));
		iconButtonsPane.add(new UIButton(this.loadImage("pen.png")));
		
		UIContainer pushButtonsPane = new UIContainer();
		pushButtonsPane.setLayout(new UIBoxLayout(UIBoxLayout.VERTICAL));
		pushButtonsPane.setPadding(8, 0, 8, 64);
		pushButtonsPane.add(new UIButton("Push me"));
		pushButtonsPane.add(new UIButton("Styled", null, this.loadImage("rocket.png")));
		
		final String[] randomTexts = { "Oh yeah!", "Again!", "Harder!", "Better!", "Faster!", "Stronger!", "Thanks", "Mmhh..", "Boom", "Good job", "Keep pressing!", "Nice aim" };
		
		UIContainer repeaterPane = new UIContainer();
		repeaterPane.setLayout(new UIBoxLayout(UIBoxLayout.VERTICAL));
		repeaterPane.setPadding(8, 0, 8, 64);
		repeaterPane.add(new UIButton("Press me", (btn) -> {
			btn.setText(randomTexts[(int) (Math.random() * randomTexts.length)]);
		}));
		
		UIContainer togglePane = new UIContainer();
		togglePane.setLayout(new UIBoxLayout(UIBoxLayout.VERTICAL, 0));
		togglePane.setPadding(8, 0, 8, 64);
		togglePane.add(new UIToggleButton("Toggle"));
		togglePane.add(new UIToggleButton("Toggle"));
		togglePane.add(new UIToggleButton("Toggle"));
		
		UIContainer radioPane = new UIContainer();
		radioPane.setLayout(new UIBoxLayout(UIBoxLayout.VERTICAL, 0));
		radioPane.setPadding(8, 0, 8, 64);
		
		UIToggleButtonGroup radioGroup = new UIToggleButtonGroup();
		radioPane.add(new UIToggleButton("Select", radioGroup));
		radioPane.add(new UIToggleButton("Select", radioGroup));
		radioPane.add(new UIToggleButton("Select", radioGroup));
		
		UIContainer buttonsPane = new UIContainer();
		buttonsPane.setLayout(new UIBoxLayout(UIBoxLayout.VERTICAL));
		buttonsPane.setPadding(8);
		
		buttonsPane.add(new UILabel("Button Demo", UILabel.ANCHOR_LEFT, FontStyle.BOLD));
		buttonsPane.add(new UISeparator(8, 0));
		buttonsPane.add(iconButtonsPane);
		
		buttonsPane.add(new UILabel("Push buttons", UILabel.ANCHOR_LEFT, FontStyle.BOLD));
		buttonsPane.add(pushButtonsPane);
		
		buttonsPane.add(new UILabel("Repeater", UILabel.ANCHOR_LEFT, FontStyle.BOLD));
		buttonsPane.add(repeaterPane);
		
		buttonsPane.add(new UILabel("Toggle buttons", UILabel.ANCHOR_LEFT, FontStyle.BOLD));
		buttonsPane.add(togglePane);
		
		buttonsPane.add(new UILabel("Radio buttons", UILabel.ANCHOR_LEFT, FontStyle.BOLD));
		buttonsPane.add(radioPane);
		
		Consumer<UIMenuItem> itemAction = (i) -> {
			System.out.println("You clicked on '" + i.getText() + "'");
		};
		
		// @format:off
		UIPopupMenu popup = new UIPopupMenu(new UIMenuItem[] {
				new UIMenuItem(this.loadImage("copy.png"), "Copy", itemAction),
				new UIMenuItem(this.loadImage("delete.png"), "Delete", itemAction),
				new UIMenu(this.loadImage("convert.png"), "Convert", new UIMenuItem[] {
					new UIMenuItem("Sub menu 1", itemAction),
					new UIMenuItem("Sub menu 2", itemAction),
					new UIMenuItem("Sub menu 3", itemAction),
				}),
				new UIMenuItem(this.loadImage("edit.png"), "Edit", itemAction),
				new UIMenuSeparator(),
				new UIMenu("More...", new UIMenuItem[] {
					new UIMenuItem("Do this", itemAction),
					new UIMenu("Do that", new UIMenuItem[] {
							new UIMenuItem("Option A", itemAction),
							new UIMenuItem("Option B", itemAction),
							new UIMenuItem("Option C", itemAction),
					}),
					new UIMenuItem("Do it", itemAction),
					new UIMenuItem("Do those", itemAction),
					new UIMenuItem("Do these", itemAction),
					new UIMenuItem("Do them", itemAction),
					new UIMenuItem("Call of", itemAction),
					new UIMenuItem("Do tea", itemAction),
					new UIMenuSeparator(),
					new UIMenuItem("Doom", itemAction),
					new UIMenuItem("Fancy", itemAction),
					new UIMenuItem("Transitions", itemAction),
					new UIMenuItem("Stuff", itemAction),
				}),
		});
		// @format:on
		
		buttonsPane.setPopupMenu(popup);
		
		// - BASICS -
		UIContainer progressBarPane = new UIContainer();
		progressBarPane.setLayout(new UIBoxLayout(UIBoxLayout.VERTICAL));
		progressBarPane.setPadding(8, 0, 8, 64);
		progressBarPane.add(new UIProgressBar(0.5f) {
			public boolean mouseButtonPressed(float x, float y, int btn) {
				if(btn == Nhengine.MOUSE_BUTTON_LEFT) this.setValue(x / this.getWidth());
				return true;
			}
		});
		
		UIContainer sliderPane = new UIContainer();
		sliderPane.setLayout(new UIBoxLayout(UIBoxLayout.VERTICAL));
		sliderPane.setPadding(8, 0, 8, 64);
		sliderPane.add(new UISlider());
		
		// @format:off
		final Texture2D[] images = {
				this.loadImage("image1.jpg"),
				this.loadImage("image2.jpg"),
				this.loadImage("image3.jpg"),
				this.loadImage("image4.jpg"),
				this.loadImage("image5.jpg"),
				this.loadImage("image6.jpg"),
				this.loadImage("image7.jpg"),
				this.loadImage("image8.jpg"),
				this.loadImage("image9.jpg"),
				this.loadImage("image10.jpg"),
				this.loadImage("image11.jpg"),
				this.loadImage("image12.jpg"),
		};
		// @format:on
		
		UIContainer imagePane = new UIContainer();
		imagePane.setLayout(new UIBoxLayout(UIBoxLayout.VERTICAL));
		imagePane.setPadding(8, 0, 8, 64);
		imagePane.add(new UIImageView(images[0]) {
			private int imageIndex = 0;
			
			public boolean mouseClicked(int btn) {
				this.imageIndex++;
				
				if(this.imageIndex >= images.length) this.imageIndex = 0;
				
				this.setImage(images[this.imageIndex]);
				return true;
			}
		});
		
		UILabel dialogLabel = new UILabel();
		
		UIDialog dialog = new UIDialog("Dialog");
		dialog.getContent().setPadding(16);
		dialog.getContent().setLayout(new UIBorderLayout());
		dialog.getContent().add(dialogLabel, UIBorderLayout.CENTER);
		
		UITextField textField = new UITextField();
		textField.setFontFamily(this.arialFamily);
		textField.setFontStyle(FontStyle.REGULAR);
		
		UIContainer textFieldPane = new UIContainer();
		textFieldPane.setLayout(new UIBorderLayout(8));
		textFieldPane.setPadding(8, 0, 8, 64);
		textFieldPane.add(textField, UIBorderLayout.CENTER);
		textFieldPane.add(new UIButton("Submit", (btn) -> {
			dialogLabel.setText(textField.getText());
			dialog.open(TestUILevel.this.cvs.getRootPane());
		}), UIBorderLayout.EAST);
		
		UIContainer basicsPane = new UIContainer();
		basicsPane.setLayout(new UIBoxLayout(UIBoxLayout.VERTICAL));
		basicsPane.setPadding(8);
		
		basicsPane.add(new UILabel("Progressbar", UILabel.ANCHOR_LEFT, FontStyle.BOLD));
		basicsPane.add(progressBarPane);
		
		basicsPane.add(new UILabel("Slider", UILabel.ANCHOR_LEFT, FontStyle.BOLD));
		basicsPane.add(sliderPane);
		
		basicsPane.add(new UILabel("Image view", UILabel.ANCHOR_LEFT, FontStyle.BOLD));
		basicsPane.add(imagePane);
		
		basicsPane.add(new UILabel("Text field", UILabel.ANCHOR_LEFT, FontStyle.BOLD));
		basicsPane.add(textFieldPane);
		
		// - DIALOGS -
		// information, warning, error
		UIContainer messageBoxPane = new UIContainer(new UIGridLayout(1, 3, 8, 8));
		
		// info
		UIMessageBox infoBox = new UIMessageBox("This is an information.", MessageType.INFORMATION, true);
		messageBoxPane.add(new UIButton("Information", (btn) -> {
			infoBox.open(this.cvs.getRootPane());
		}));
		
		// warning
		UIMessageBox warnBox = new UIMessageBox("Beware of the man who speaks in hands...", MessageType.WARNING, true);
		messageBoxPane.add(new UIButton("Warning", (btn) -> {
			warnBox.open(this.cvs.getRootPane());
		}));
		
		// error
		UIMessageBox errBox = new UIMessageBox("The program failed to do something... too bad!", MessageType.ERROR, true);
		messageBoxPane.add(new UIButton("Error", (btn) -> {
			errBox.open(this.cvs.getRootPane());
		}));
		
		UIContainer dialogsPane = new UIContainer(new UIBoxLayout(UIBoxLayout.VERTICAL, 8));
		dialogsPane.setPadding(8);
		dialogsPane.add(messageBoxPane);
		
		// - SPLIT -
		UISplitPane splitPane = new UISplitPane();
		UIButton splitA = new UIButton("A"), splitB = new UIButton("B");
		splitPane.setPadding(4);
		splitPane.setA(splitA);
		splitPane.setB(splitB);
		
		// - GLOBAL -
		UITabbedPane demoPane = new UITabbedPane();
		demoPane.setPadding(8);
		demoPane.insertTab("Buttons", new UIScrollPane(buttonsPane));
		demoPane.insertTab("Basics", basicsPane);
		demoPane.insertTab("Dialogs", dialogsPane);
		demoPane.insertTab("Split", splitPane);
		
		return demoPane;
	}
	
	private void leftPaneTransitionStep(float f) {
		this.leftPane.setPosition(f, 0);
	}
	
	public void update(float delta) {
		GameWindow win = Game.instance().window();
		
		if(win.getFrameWidth() != this.frameSize.x || win.getFrameHeight() != this.frameSize.y) {
			this.frameSize.set(win.getFrameWidth(), win.getFrameHeight());
			this.sce.getCamera().setViewport2D(this.frameSize.x, this.frameSize.y);
			
			this.leftPane.setPreferredSize(this.leftPane.getWidth(), this.frameSize.y);
			this.cvs.setSize((int) this.frameSize.x, (int) this.frameSize.y);
			
			this.cvs.getRootPane().repack();
		}
		
		if(!(this.cvs.getFocusComponent() instanceof UITextField)) {
			if(win.isKeyPressed(Nhengine.KEY_ESCAPE)) {
				this.leftPaneTrans.setTargetValue(0);
			}
			
			if(win.isKeyPressed(Nhengine.KEY_R)) {
				this.cvs.repaint();
			}
			
			if(win.isKeyPressed(Nhengine.KEY_P)) {
				this.cvs.getRootPane().repack();
			}
			
			if(win.isKeyPressed(Nhengine.KEY_B)) {
				this.toggleShowBounds.doClick();
			}
		}
		
		this.cvs.repaint();
		this.cvs.update();
	}
}
