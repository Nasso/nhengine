package io.github.nasso.nhengine.tools.nhstudio;

import static org.lwjgl.util.tinyfd.TinyFileDialogs.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryUtil;

import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.data.StaticLatin1Font;
import io.github.nasso.nhengine.graphics.FontFamily.FontStyle;
import io.github.nasso.nhengine.tools.nhstudio.dialogs.NewFontDialog;
import io.github.nasso.nhengine.tools.nhstudio.dialogs.NewObjectDialog;
import io.github.nasso.nhengine.tools.nhstudio.dialogs.NewRoomDialog;
import io.github.nasso.nhengine.tools.nhstudio.dialogs.NewScriptDialog;
import io.github.nasso.nhengine.tools.nhstudio.dialogs.NewSoundDialog;
import io.github.nasso.nhengine.tools.nhstudio.dialogs.NewSpriteDialog;
import io.github.nasso.nhengine.tools.nhstudio.game.Room;
import io.github.nasso.nhengine.ui.UICanvas;
import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.ui.UIRootPane;
import io.github.nasso.nhengine.ui.control.UILabel;
import io.github.nasso.nhengine.ui.control.UIMenu;
import io.github.nasso.nhengine.ui.control.UIMenuBar;
import io.github.nasso.nhengine.ui.control.UIMenuItem;
import io.github.nasso.nhengine.ui.control.UIMenuSeparator;
import io.github.nasso.nhengine.ui.control.UIPopupMenu;
import io.github.nasso.nhengine.ui.control.UIScrollPane;
import io.github.nasso.nhengine.ui.control.UISeparator;
import io.github.nasso.nhengine.ui.control.UITabbedPane;
import io.github.nasso.nhengine.ui.control.UIScrollPane.BarPolicy;
import io.github.nasso.nhengine.ui.layout.UIBorderLayout;
import io.github.nasso.nhengine.ui.layout.UIBoxLayout;
import io.github.nasso.nhengine.utils.TimeManager;

public class NhStudioApp {
	private UICanvas cvs;
	
	private UITabbedPane tabs = new UITabbedPane();
	
	private NewRoomDialog newRoomDialog;
	private NewObjectDialog newObjectDialog;
	private NewSpriteDialog newSpriteDialog;
	private NewSoundDialog newSoundDialog;
	private NewFontDialog newFontDialog;
	private NewScriptDialog newScriptDialog;
	
	private UIContainer roomListContentPane, objectListContentPane,
			spriteListContentPane, soundListContentPane, fontListContentPane,
			scriptListContentPane;
	
	public void start(UICanvas cvs) {
		this.newRoomDialog = new NewRoomDialog(this);
		this.newObjectDialog = new NewObjectDialog(this);
		this.newSpriteDialog = new NewSpriteDialog(this);
		this.newSoundDialog = new NewSoundDialog(this);
		this.newFontDialog = new NewFontDialog(this);
		this.newScriptDialog = new NewScriptDialog(this);
		this.cvs = cvs;
		
		this.createUI(this.cvs.getRootPane());
	}
	
	private void openFileSync(String file) {
		String fileName = Paths.get(file).getFileName().toString();
		
		if(file.endsWith(".nhfont")) {
			try {
				final StaticLatin1Font fnt = StaticLatin1Font.loadStaticFont(file);
				
				this.tabs.insertTab(fileName, new NhStudioStaticFontPane(fnt)).setOnClosed((tab) -> {
					fnt.dispose();
				});
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else if(file.endsWith(".ttf")) {
			int size = -1;
			
			do {
				try {
					String str = tinyfd_inputBox("Import size", "Type the input size", "18");
					if(str == null) return;
					size = Integer.parseInt(str.replaceAll("\\n|\\r", ""));
				} catch(NumberFormatException e) {
					size = -1;
				}
			} while(size == -1);
			
			int res = -1;
			
			do {
				try {
					String str = tinyfd_inputBox("Import resolution", "Type the desired font resolution", "256");
					if(str == null) return;
					res = Integer.parseInt(str.replaceAll("\\n|\\r", ""));
				} catch(NumberFormatException e) {
					res = -1;
				}
			} while(res == -1);
			
			try {
				StaticLatin1Font fnt = StaticLatin1Font.loadStaticTTF(file, size, res);
				
				this.tabs.insertTab(fileName, new NhStudioStaticFontPane(fnt)).setOnClosed((tab) -> {
					fnt.dispose();
				});
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void openAction(UIMenuItem item) {
		Thread t = new Thread(() -> {
			PointerBuffer ext = BufferUtils.createPointerBuffer(2);
			ext.put(MemoryUtil.memUTF8("*.nhfont"));
			ext.put(MemoryUtil.memUTF8("*.ttf"));
			ext.flip();
			
			String select = tinyfd_openFileDialog(null, null, ext, "Supported Font Files", false);
			
			if(select != null) {
				TimeManager.runLater(() -> {
					this.openFileSync(select);
				});
			}
		});
		t.setDaemon(true);
		t.start();
	}
	
	private void exportAction(UIMenuItem item) {
		UIComponent tab = this.tabs.getCurrentTab().getComponent();
		
		if(tab instanceof NhStudioStaticFontPane) {
			NhStudioStaticFontPane fontPane = (NhStudioStaticFontPane) tab;
			final StaticLatin1Font fnt = fontPane.getCurrentFont();
			
			if(fnt != null) {
				Thread t = new Thread(() -> {
					PointerBuffer ext = BufferUtils.createPointerBuffer(2);
					ext.put(MemoryUtil.memUTF8("*.nhfont"));
					ext.flip();
					
					String dest = tinyfd_saveFileDialog(null, null, ext, "Nhengine Font File");
					if(dest != null) {
						try {
							StaticLatin1Font.writeFont(fnt, dest);
						} catch(IOException e) {
							e.printStackTrace();
						}
					}
				});
				t.setDaemon(true);
				t.start();
			}
		}
	}
	
	private UIContainer createListContentPane() {
		UIContainer c = new UIContainer(new UIBoxLayout());
		c.setPadding(0, 0, 0, 16);
		return c;
	}
	
	private UIContainer buildItemList(String name, UIContainer contentPane) {
		UIContainer c = new UIContainer(new UIBoxLayout());
		c.add(new UILabel(name, UILabel.ANCHOR_LEFT, FontStyle.MEDIUM_ITALIC));
		c.add(contentPane);
		c.add(new UISeparator(4));
		
		return c;
	}
	
	private void createUI(UIRootPane root) {
		// @format:off
		root.setMenuBar(new UIMenuBar(new UIMenuItem[] {
				new UIMenu("File", new UIMenuItem[] {
						new UIMenuItem("New Project"),	
						new UIMenuItem("Open", this::openAction),
						new UIMenuItem("Export", this::exportAction),
						new UIMenuSeparator(),
						new UIMenuItem("Exit", (item) -> {
							Game.instance().quit();
						})
				}),
		}));
		// @format:on
		
		UIContainer content = root.getContentPane();
		content.setLayout(new UIBorderLayout());
		
		UIContainer dataBrowser = new UIContainer(new UIBoxLayout(UIBoxLayout.VERTICAL, 16));
		dataBrowser.setPadding(16);
		
		// @format:off
		// Rooms
		this.roomListContentPane = this.createListContentPane();
		UIContainer roomsSection = this.buildItemList("Rooms", this.roomListContentPane);
		
		roomsSection.setPopupMenu(new UIPopupMenu(new UIMenuItem[] {
				new UIMenuItem("Add new", (item) -> {
					this.newRoomDialog.open(root);
				})
		}));

		// Objects
		this.objectListContentPane = this.createListContentPane();
		UIContainer objectsSection = this.buildItemList("Objects", this.objectListContentPane);
		
		objectsSection.setPopupMenu(new UIPopupMenu(new UIMenuItem[] {
				new UIMenuItem("Add new", (item) -> {
					this.newObjectDialog.open(root);
				})
		}));

		// Sprites
		this.spriteListContentPane = this.createListContentPane();
		UIContainer spritesSection = this.buildItemList("Sprites", this.spriteListContentPane);
		
		spritesSection.setPopupMenu(new UIPopupMenu(new UIMenuItem[] {
				new UIMenuItem("Add new", (item) -> {
					this.newSpriteDialog.open(root);
				})
		}));

		// Sounds
		this.soundListContentPane = this.createListContentPane();
		UIContainer soundsSection = this.buildItemList("Sounds", this.soundListContentPane);
		
		soundsSection.setPopupMenu(new UIPopupMenu(new UIMenuItem[] {
				new UIMenuItem("Add new", (item) -> {
					this.newSoundDialog.open(root);
				})
		}));

		// Fonts
		this.fontListContentPane = this.createListContentPane();
		UIContainer fontsSection = this.buildItemList("Fonts", this.fontListContentPane);
		
		fontsSection.setPopupMenu(new UIPopupMenu(new UIMenuItem[] {
				new UIMenuItem("Add new", (item) -> {
					this.newFontDialog.open(root);
				})
		}));

		// Scripts
		this.scriptListContentPane = this.createListContentPane();
		UIContainer scriptsSection = this.buildItemList("Scripts", this.scriptListContentPane);
		
		scriptsSection.setPopupMenu(new UIPopupMenu(new UIMenuItem[] {
				new UIMenuItem("Add new", (item) -> {
					this.newScriptDialog.open(root);
				})
		}));
		// @format:on
		
		dataBrowser.add(roomsSection);
		dataBrowser.add(objectsSection);
		dataBrowser.add(spritesSection);
		dataBrowser.add(soundsSection);
		dataBrowser.add(fontsSection);
		dataBrowser.add(scriptsSection);
		
		content.add(new UIScrollPane(dataBrowser, BarPolicy.DISABLED, BarPolicy.ALWAYS_VISIBLE), UIBorderLayout.WEST);
		
		this.tabs.setPadding(8);
		content.add(this.tabs, UIBorderLayout.CENTER);
		
		this.tabs.insertTab("Welcome", new UILabel("Welcome to Nhengine Studio 1.0", UILabel.ANCHOR_CENTER, FontStyle.BOLD));
	}
	
	public void addRoom(Room room) {
		// content.add(new UILabel("New thing", UILabel.ANCHOR_LEFT));
	}
}
