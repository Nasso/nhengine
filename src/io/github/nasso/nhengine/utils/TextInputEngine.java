package io.github.nasso.nhengine.utils;

import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.core.Nhengine;

/**
 * 
 * @author nasso (<a href="https://github.com/Nasso">GitHub</a>)
 */
public class TextInputEngine {
	private boolean multiline = false;
	
	private boolean shift = false;
	private boolean ctrl = false;
	
	private char[] _chars = new char[2];
	private int caretPosition = 0;
	private int selectionPoint = 0;
	
	private StringBuffer text = new StringBuffer();
	
	public TextInputEngine(StringBuffer textBuffer, boolean multiline) {
		this.text = textBuffer;
	}
	
	public StringBuffer getBuffer() {
		return this.text;
	}
	
	public void setSelectionPoint(int point) {
		this.selectionPoint = point;
	}
	
	public int getSelectionPoint() {
		return this.selectionPoint;
	}
	
	private void clampCaret() {
		this.caretPosition = MathUtils.clamp(this.caretPosition, 0, this.text.length());
	}
	
	public void incrementCaretPosition() {
		this.caretPosition++;
		this.clampCaret();
		
		if(!this.shift) {
			this.selectionPoint = this.caretPosition;
		}
	}
	
	public void decrementCaretPosition() {
		this.caretPosition--;
		this.clampCaret();
		
		if(!this.shift) {
			this.selectionPoint = this.caretPosition;
		}
	}
	
	public void setCaretPosition(int pos) {
		this.caretPosition = pos;
		this.clampCaret();
		
		if(!this.shift) {
			this.selectionPoint = this.caretPosition;
		}
	}
	
	public int getCaretPosition() {
		return this.caretPosition;
	}
	
	public void deleteSelection() {
		if(this.caretPosition != this.selectionPoint) {
			int min = Math.min(this.caretPosition, this.selectionPoint);
			int max = Math.max(this.caretPosition, this.selectionPoint);
			
			this.text.delete(min, max);
			
			this.caretPosition = min;
			this.selectionPoint = min;
		}
	}
	
	public void type(CharSequence txt) {
		if(txt == null) return;
		
		this.deleteSelection();
		
		this.text.insert(this.caretPosition, txt);
		
		this.caretPosition += txt.length();
		this.clampCaret();
		
		this.selectionPoint = this.caretPosition;
	}
	
	public void textInput(int codepoint) {
		this.deleteSelection();
		
		int l = Character.toChars(codepoint, this._chars, 0);
		this.text.insert(this.caretPosition, this._chars, 0, l);
		
		this.caretPosition += l;
		this.clampCaret();
		
		this.selectionPoint = this.caretPosition;
	}
	
	public void keyTyped(int key, int scancode) {
		switch(key) {
			case Nhengine.KEY_LEFT_CONTROL:
			case Nhengine.KEY_RIGHT_CONTROL:
				this.ctrl = true;
				break;
			case Nhengine.KEY_RIGHT_SHIFT:
			case Nhengine.KEY_LEFT_SHIFT:
				this.shift = true;
				break;
			case Nhengine.KEY_KP_ENTER:
			case Nhengine.KEY_ENTER:
				if(this.multiline) this.textInput('\n');
				break;
			case Nhengine.KEY_RIGHT:
				do {
					if(!this.shift && this.caretPosition != this.selectionPoint) this.setCaretPosition(Math.max(this.caretPosition, this.selectionPoint));
					else this.setCaretPosition(this.caretPosition + 1);
				} while(this.ctrl && this.caretPosition < this.text.length() && this.text.charAt(this.caretPosition - 1) != ' ');
				break;
			case Nhengine.KEY_LEFT:
				do {
					if(!this.shift && this.caretPosition != this.selectionPoint) this.setCaretPosition(Math.min(this.caretPosition, this.selectionPoint));
					else this.setCaretPosition(this.caretPosition - 1);
				} while(this.ctrl && this.caretPosition > 0 && this.text.charAt(this.caretPosition - 1) != ' ');
				break;
			case Nhengine.KEY_HOME:
				this.setCaretPosition(0);
				break;
			case Nhengine.KEY_END:
				this.setCaretPosition(this.text.length());
				break;
			case Nhengine.KEY_BACKSPACE:
				if(this.caretPosition != this.selectionPoint) {
					this.deleteSelection();
				} else if(this.caretPosition > 0) {
					this.text.deleteCharAt(this.caretPosition - 1);
					
					this.caretPosition--;
					this.clampCaret();
					
					this.selectionPoint = this.caretPosition;
				}
				break;
			case Nhengine.KEY_DELETE:
				if(this.caretPosition != this.selectionPoint) {
					this.deleteSelection();
				} else if(this.caretPosition < this.text.length()) {
					this.text.deleteCharAt(this.caretPosition);
					
					this.caretPosition = Math.min(this.caretPosition, this.selectionPoint);
					this.clampCaret();
					
					this.selectionPoint = this.caretPosition;
				}
				break;
			default:
				if(this.ctrl) {
					String str = Nhengine.getKeyName(key, scancode);
					if(str == null || str.length() <= 0) break;
					
					char c = str.charAt(0);
					
					GameWindow win = Game.instance().window();
					
					switch(c) {
						case 'A':
							this.caretPosition = 0;
							this.selectionPoint = this.text.length();
							break;
						case 'C':
							if(this.caretPosition != this.selectionPoint) win.setClipboard(this.text.subSequence(Math.min(this.caretPosition, this.selectionPoint), Math.max(this.caretPosition, this.selectionPoint)));
							break;
						case 'X':
							if(this.caretPosition != this.selectionPoint) win.setClipboard(this.text.subSequence(Math.min(this.caretPosition, this.selectionPoint), Math.max(this.caretPosition, this.selectionPoint)));
							this.deleteSelection();
							break;
						case 'V':
							this.type(win.getClipboard());
							break;
					}
				}
				break;
		}
	}
	
	public void keyReleased(int key, int scancode) {
		switch(key) {
			case Nhengine.KEY_LEFT_CONTROL:
			case Nhengine.KEY_RIGHT_CONTROL:
				this.ctrl = false;
				break;
			case Nhengine.KEY_RIGHT_SHIFT:
			case Nhengine.KEY_LEFT_SHIFT:
				this.shift = false;
				break;
		}
	}
	
	public boolean hasSelection() {
		return this.caretPosition != this.selectionPoint;
	}
	
	public boolean isMultiline() {
		return this.multiline;
	}
	
	public void setMultiline(boolean multiline) {
		this.multiline = multiline;
	}
}
