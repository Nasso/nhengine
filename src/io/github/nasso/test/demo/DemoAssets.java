package io.github.nasso.test.demo;

import java.io.IOException;

import io.github.nasso.nhengine.audio.Sound;
import io.github.nasso.nhengine.data.TextureIO;
import io.github.nasso.nhengine.data.TrueTypeFont;
import io.github.nasso.nhengine.graphics.Font;
import io.github.nasso.nhengine.graphics.Texture2D;

public class DemoAssets {
	private static int SND_COUNTER = 0;
	
	public static final int SND_SANS_VOICE = SND_COUNTER++;
	public static final int SND_PAPY_VOICE = SND_COUNTER++;
	public static final int SND_REGULAR_VOICE = SND_COUNTER++;
	public static final int SND_MENU_CLICK = SND_COUNTER++;
	public static final int SND_ENCOUNTER = SND_COUNTER++;
	public static final int SND_PHONE_CALL = SND_COUNTER++;
	public static final int SND_SAVE = SND_COUNTER++;
	public static final int SND_DAMAGE = SND_COUNTER++;
	public static final int SND_HEART_SHATTERS = SND_COUNTER++;
	public static final int SND_HEART_BREAK = SND_COUNTER++;
	
	public static final Sound[] SOUNDS = new Sound[SND_COUNTER];
	
	// @format:off
	public static final String[] SOUND_PATHS = {
			"000029e6.wav",
			"000029e5.wav",
			"000029ed.wav",
			"00002a00.wav",
			"000029ab.wav",
			"000029f6.wav",
			"000029a7.wav",
			"00002a1c.wav",
			"000029ac.wav",
			"000029ad.wav",
	};
	// @format:on
	
	private static int SPR_COUNTER = 0;
	
	public static final int SPR_FACE_SANS = SPR_COUNTER++;
	public static final int SPR_FACE_SANSWINK = SPR_COUNTER++;
	public static final int SPR_FACE_SANSBLINK = SPR_COUNTER++;
	public static final int SPR_FACE_SANSNOEYES = SPR_COUNTER++;
	public static final int SPR_FACE_SANSCHUCKLE = SPR_COUNTER++;
	public static final int SPR_FACE_SANSCHUCKLE2 = SPR_COUNTER++;
	
	public static final int SPR_FACE_PAPYRUS_0 = SPR_COUNTER++;
	public static final int SPR_FACE_PAPYRUS_1 = SPR_COUNTER++;
	public static final int SPR_FACE_PAPYRUSMAD_0 = SPR_COUNTER++;
	public static final int SPR_FACE_PAPYRUSMAD_1 = SPR_COUNTER++;
	public static final int SPR_FACE_PAPYRUSSIDE_0 = SPR_COUNTER++;
	public static final int SPR_FACE_PAPYRUSSIDE_1 = SPR_COUNTER++;
	public static final int SPR_FACE_PAPYRUSEVIL_0 = SPR_COUNTER++;
	public static final int SPR_FACE_PAPYRUSEVIL_1 = SPR_COUNTER++;
	public static final int SPR_FACE_PAPYRUSSWEAT_0 = SPR_COUNTER++;
	public static final int SPR_FACE_PAPYRUSSWEAT_1 = SPR_COUNTER++;
	public static final int SPR_FACE_PAPYRUSLAUGH_0 = SPR_COUNTER++;
	public static final int SPR_FACE_PAPYRUSLAUGH_1 = SPR_COUNTER++;
	
	public static final Texture2D[] SPRITES = new Texture2D[SPR_COUNTER];
	
	// @format:off
	public static final String[] SPRITE_PATHS = {
			"spr_face_sans_0.png",
			"spr_face_sanswink_0.png",
			"spr_face_sansblink_0.png",
			"spr_face_sansnoeyes_0.png",
			"spr_face_sanschuckle_0.png",
			"spr_face_sanschuckle2_0.png",
			
			"spr_face_papyrus_0.png",
			"spr_face_papyrus_1.png",
			"spr_face_papyrusmad_0.png",
			"spr_face_papyrusmad_1.png",
			"spr_face_papyrusside_0.png",
			"spr_face_papyrusside_1.png",
			"spr_face_papyrusevil_0.png",
			"spr_face_papyrusevil_1.png",
			"spr_face_papyrussweat_0.png",
			"spr_face_papyrussweat_1.png",
			"spr_face_papyruslaugh_0.png",
			"spr_face_papyruslaugh_1.png",
	};
	// @format:on
	
	private static final int FONT_SIZE_S = 8;
	private static final int FONT_SIZE_L = 16;
	
	private static int FNT_COUNTER = 0;
	
	public static final int FNT_DETERMINATION_S = FNT_COUNTER++;
	public static final int FNT_DETERMINATION_MONO = FNT_COUNTER++;
	public static final int FNT_SANS = FNT_COUNTER++;
	public static final int FNT_PAPY = FNT_COUNTER++;
	
	public static final Font[] FONTS = new Font[SPR_COUNTER];
	
	// @format:off
	public static final String[] FONT_PATHS = {
			"DeterminationSans.ttf",
			"DeterminationMono.ttf",
			"Comic-Sans-UT.ttf",
			"Papyrus-UT.ttf",
	};
	
	public static final int[] FONT_SIZES = {
			FONT_SIZE_S,
			FONT_SIZE_L,
			FONT_SIZE_L,
			13
	};
	// @format:on
	
	public static void initAll() throws IOException {
		for(int i = 0; i < SOUND_PATHS.length; i++) {
			SOUNDS[i] = Sound.load("res/demo/audio/sounds/" + SOUND_PATHS[i], true);
		}
		
		for(int i = 0; i < SPRITE_PATHS.length; i++) {
			SPRITES[i] = TextureIO.loadTexture2D("res/demo/sprites/" + SPRITE_PATHS[i], 4, false, false, false, true);
		}
		
		for(int i = 0; i < FONT_PATHS.length; i++) {
			FONTS[i] = new TrueTypeFont("res/demo/fonts/" + FONT_PATHS[i], FONT_SIZES[i], false, true);
		}
	}
	
	public static Sound getSound(int id) {
		return (id >= 0 && id < SOUNDS.length) ? SOUNDS[id] : null;
	}
	
	public static Texture2D getSprite(int id) {
		return (id >= 0 && id < SPRITES.length) ? SPRITES[id] : null;
	}
	
	public static Font getFont(int id) {
		return (id >= 0 && id < FONTS.length) ? FONTS[id] : null;
	}
	
	public static void disposeAll() {
		for(int i = 0; i < SOUNDS.length; i++) {
			if(SOUNDS[i] != null) SOUNDS[i].dispose();
		}
		
		for(int i = 0; i < SPRITES.length; i++) {
			if(SPRITES[i] != null) SPRITES[i].dispose();
		}
		
		for(int i = 0; i < FONTS.length; i++) {
			if(FONTS[i] != null) FONTS[i].dispose();
		}
	}
}
