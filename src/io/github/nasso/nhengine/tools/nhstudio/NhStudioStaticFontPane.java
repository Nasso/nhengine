package io.github.nasso.nhengine.tools.nhstudio;

import org.joml.Vector2f;

import io.github.nasso.nhengine.data.StaticLatin1Font;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.Font.FontPackedGlyph;
import io.github.nasso.nhengine.graphics.FontFamily.FontStyle;
import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.ui.control.UIImageView;
import io.github.nasso.nhengine.ui.control.UILabel;
import io.github.nasso.nhengine.ui.control.UISeparator;
import io.github.nasso.nhengine.ui.layout.UIBorderLayout;
import io.github.nasso.nhengine.ui.layout.UIBoxLayout;
import io.github.nasso.nhengine.ui.layout.UIGridLayout;
import io.github.nasso.nhengine.utils.MathUtils;

public class NhStudioStaticFontPane extends UIContainer {
	private StaticLatin1Font currentFont = null;
	
	private UIImageView textureView, letterView;
	private UILabel labelSize;
	private UILabel labelRes;
	private UILabel labelAscent;
	private UILabel labelDescent;
	private UILabel labelHeight;
	
	private UILabel labelGlyph;
	private UILabel labelGlyphName;
	private UILabel labelGlyphX;
	private UILabel labelGlyphY;
	private UILabel labelGlyphW;
	private UILabel labelGlyphH;
	
	public NhStudioStaticFontPane(StaticLatin1Font fnt) {
		this.currentFont = fnt;
		
		this.setLayout(new UIGridLayout(1, 0, 8, 8));
		this.setPadding(16);
		this.add(this.textureView = new UIImageView(fnt == null ? null : fnt.getTexture()) {
			private Vector2f _vec2 = new Vector2f();
			private char[] _c2 = new char[2];
			
			private FontPackedGlyph searchCharAt(float x, float y, FontPackedGlyph[] chars) {
				FontPackedGlyph pchar = null;
				for(int i = 0; i < chars.length; i++) {
					pchar = chars[i];
					if(MathUtils.boxContains(x, y, pchar.x0(), pchar.y0(), pchar.x1() - pchar.x0(), pchar.y1() - pchar.y0())) return pchar;
				}
				
				return null;
			}
			
			public boolean mouseButtonPressed(float x, float y, int btn) {
				if(NhStudioStaticFontPane.this.currentFont != null) {
					this._vec2.set(x, y);
					this.transformPositionRelativeToImage(this._vec2);
					if(this._vec2.x < 0 || this._vec2.y < 0 || this._vec2.x >= this.getImage().getWidth() || this._vec2.y >= this.getImage().getHeight()) return true;
					
					StaticLatin1Font f = NhStudioStaticFontPane.this.currentFont;
					
					FontPackedGlyph pchar = null;
					
					if(f.getBasicLatinTable() != null) {
						pchar = this.searchCharAt(this._vec2.x, this._vec2.y, f.getBasicLatinTable());
					}
					
					if(pchar == null && f.getLatin1Table() != null) {
						pchar = this.searchCharAt(this._vec2.x, this._vec2.y, f.getLatin1Table());
					}
					
					if(pchar == null) return true;
					
					int charW = pchar.x1() - pchar.x0();
					int charH = pchar.y1() - pchar.y0();
					
					Character.toChars(pchar.codepoint(), this._c2, 0);
					
					NhStudioStaticFontPane.this.letterView.setImage(NhStudioStaticFontPane.this.currentFont.getTexture(), pchar.x0(), pchar.y0(), charW, charH);
					NhStudioStaticFontPane.this.labelGlyph.setText(new String(this._c2));
					NhStudioStaticFontPane.this.labelGlyphName.setText(Character.getName(pchar.codepoint()));
					NhStudioStaticFontPane.this.labelGlyphX.setText(pchar.x0() + "px");
					NhStudioStaticFontPane.this.labelGlyphY.setText(pchar.y0() + "px");
					NhStudioStaticFontPane.this.labelGlyphW.setText(charW + "px");
					NhStudioStaticFontPane.this.labelGlyphH.setText(charH + "px");
				}
				return true;
			}
		});
		this.textureView.setOpaque(true);
		this.textureView.setBackground(Color.BLACK);
		
		UIContainer infoPane = new UIContainer();
		infoPane.setLayout(new UIBoxLayout(UIBoxLayout.VERTICAL, 8));
		infoPane.setPadding(16);
		infoPane.add(new UILabel("Font info", FontStyle.BOLD));
		infoPane.add(new UISeparator());
		this.labelSize = this.createField(infoPane, "Font size:", fnt == null ? "N/A" : (fnt.getSize() + "px"));
		infoPane.add(new UISeparator());
		this.labelRes = this.createField(infoPane, "Font resolution:", fnt == null ? "N/A" : (fnt.getResolution() + "x" + fnt.getResolution()));
		infoPane.add(new UISeparator());
		this.labelAscent = this.createField(infoPane, "Font ascent:", fnt == null ? "N/A" : (fnt.getAscent() + "px"));
		infoPane.add(new UISeparator());
		this.labelDescent = this.createField(infoPane, "Font descent:", fnt == null ? "N/A" : (fnt.getDescent() + "px"));
		infoPane.add(new UISeparator());
		this.labelHeight = this.createField(infoPane, "Font height:", fnt == null ? "N/A" : (fnt.getHeight() + "px"));
		infoPane.add(new UISeparator());
		
		UIContainer letterPane = new UIContainer();
		letterPane.setLayout(new UIGridLayout(1, 0));
		letterPane.add(this.letterView = new UIImageView());
		this.letterView.setOpaque(true);
		this.letterView.setBackground(Color.BLACK);
		
		UIContainer letterInfoPane = new UIContainer();
		letterInfoPane.setLayout(new UIBoxLayout(UIBoxLayout.VERTICAL, 8));
		letterInfoPane.add(new UILabel("Glyph info", FontStyle.BOLD));
		letterInfoPane.add(new UISeparator());
		this.labelGlyph = this.createField(letterInfoPane, "Character:", "N/A");
		this.labelGlyphName = this.createField(letterInfoPane, null, "N/A");
		this.labelGlyphX = this.createField(letterInfoPane, "Glyph offset X:", "N/A");
		this.labelGlyphY = this.createField(letterInfoPane, "Glyph offset Y:", "N/A");
		this.labelGlyphW = this.createField(letterInfoPane, "Glyph width:", "N/A");
		this.labelGlyphH = this.createField(letterInfoPane, "Glyph height:", "N/A");
		letterInfoPane.add(new UISeparator());
		
		letterPane.add(letterInfoPane);
		
		UIContainer eastPane = new UIContainer();
		eastPane.setLayout(new UIBorderLayout());
		eastPane.add(infoPane, UIBorderLayout.NORTH);
		eastPane.add(letterPane, UIBorderLayout.SOUTH);
		
		this.add(eastPane);
	}
	
	private UILabel createField(UIContainer cont, CharSequence name, CharSequence defaultValue) {
		UIContainer fieldContainer = new UIContainer();
		fieldContainer.setLayout(new UIBorderLayout());
		if(name != null) fieldContainer.add(new UILabel(name, UILabel.ANCHOR_LEFT), UIBorderLayout.CENTER);
		
		UILabel lab;
		fieldContainer.add(lab = new UILabel(defaultValue, UILabel.ANCHOR_RIGHT), UIBorderLayout.EAST);
		
		fieldContainer.setPadding(0, 16);
		cont.add(fieldContainer);
		
		return lab;
	}
	
	public void setCurrentFont(StaticLatin1Font fnt) {
		if(this.currentFont == fnt) return;
		this.currentFont = fnt;
		
		this.textureView.setImage(fnt.getTexture());
		this.labelSize.setText(fnt.getSize() + "px");
		this.labelRes.setText(fnt.getResolution() + "x" + fnt.getResolution());
		this.labelAscent.setText(fnt.getAscent() + "px");
		this.labelDescent.setText(fnt.getDescent() + "px");
		this.labelHeight.setText(fnt.getHeight() + "px");
	}
	
	public StaticLatin1Font getCurrentFont() {
		return this.currentFont;
	}
}
