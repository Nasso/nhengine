package io.github.nasso.test.demo;

public class DemoTestDialog extends DemoDialogBuilder {
	public DemoTestDialog() {
		// @format:off
		this.appendPage(
				"* Use the box?",
				-1, DemoAssets.FNT_DETERMINATION_MONO, DemoAssets.SND_REGULAR_VOICE);
		this.appendPage(
				"SO, AS I" + MACRO_LINE_SEPARATOR +
				"WAS SAYING" + MACRO_LINE_SEPARATOR +
				"ABOUT UNDYNE,",
				DemoAssets.SPR_FACE_PAPYRUS_0, DemoAssets.FNT_PAPY, DemoAssets.SND_PAPY_VOICE);
		this.appendPage(
				"HEY YOU!!" + MACRO_PAUSE_16 + MACRO_LINE_SEPARATOR +
				"STOP MOVING!!!",
				DemoAssets.SPR_FACE_PAPYRUSMAD_0, DemoAssets.FNT_PAPY, DemoAssets.SND_PAPY_VOICE);
		this.appendPage(
				"HOW DID YOU" + MACRO_LINE_SEPARATOR +
				"GET HERE?",
				DemoAssets.SPR_FACE_PAPYRUSMAD_0, DemoAssets.FNT_PAPY, DemoAssets.SND_PAPY_VOICE);
		this.appendPage(
				"THIS AREA IS" + MACRO_LINE_SEPARATOR +
				"VERY HIGHLY" + MACRO_LINE_SEPARATOR +
				"PROTECTED!",
				DemoAssets.SPR_FACE_PAPYRUSMAD_0, DemoAssets.FNT_PAPY, DemoAssets.SND_PAPY_VOICE);
		this.appendPage(
				"BUT I, " + MACRO_PAUSE_16 + "THE GREAT" + MACRO_LINE_SEPARATOR +
				"PAPYRUS..." + MACRO_PAUSE_16 + MACRO_LINE_SEPARATOR +
				"." + MACRO_PAUSE_16 + "." + MACRO_PAUSE_16 + ".",
				DemoAssets.SPR_FACE_PAPYRUSEVIL_0, DemoAssets.FNT_PAPY, DemoAssets.SND_PAPY_VOICE);
		this.appendPage(
				"EHHMMM..." + MACRO_PAUSE_16,
				DemoAssets.SPR_FACE_PAPYRUSSIDE_0, DemoAssets.FNT_PAPY, DemoAssets.SND_PAPY_VOICE);
		this.appendPage(
				"WELL..." + MACRO_PAUSE_16 + MACRO_LINE_SEPARATOR +
				"HOW PATHETIC!",
				DemoAssets.SPR_FACE_PAPYRUSLAUGH_0, DemoAssets.FNT_PAPY, DemoAssets.SND_PAPY_VOICE);
		this.appendPage(
				"I FORGOT MY" + MACRO_LINE_SEPARATOR +
				"DIALOGUE!",
				DemoAssets.SPR_FACE_PAPYRUSSWEAT_0, DemoAssets.FNT_PAPY, DemoAssets.SND_PAPY_VOICE, DemoDialog.TEXT_EFFECT_TENSION);
		this.appendPage(
				"BUT YOU SHALL" + MACRO_LINE_SEPARATOR +
				"NOT ESCAPE!",
				DemoAssets.SPR_FACE_PAPYRUSEVIL_0, DemoAssets.FNT_PAPY, DemoAssets.SND_PAPY_VOICE);
		this.appendPage(
				"* hey bro who are you" + MACRO_LINE_SEPARATOR +
				"  talking to?",
				DemoAssets.SPR_FACE_SANSCHUCKLE, DemoAssets.FNT_SANS, DemoAssets.SND_SANS_VOICE);
		this.appendPage(
				"* oh, " + MACRO_PAUSE_16 + "you're there",
				DemoAssets.SPR_FACE_SANSCHUCKLE, DemoAssets.FNT_SANS, DemoAssets.SND_SANS_VOICE);
		this.appendPage(
				"* i knew you could switch" + MACRO_LINE_SEPARATOR +
				"  between " + MACRO_COLOR_BLUE + "timelines" + MACRO_COLOR_DEFAULT + ", " + MACRO_PAUSE_16 + "but" + MACRO_LINE_SEPARATOR +
				"  games...?",
				DemoAssets.SPR_FACE_SANSCHUCKLE, DemoAssets.FNT_SANS, DemoAssets.SND_SANS_VOICE);
		this.appendPage(
				"* you really should be" + MACRO_LINE_SEPARATOR +
				"  careful there..." + MACRO_PAUSE_8 + MACRO_LINE_SEPARATOR +
				"  you're not alone...",
				DemoAssets.SPR_FACE_SANSWINK, DemoAssets.FNT_SANS, DemoAssets.SND_SANS_VOICE);
		this.appendPage(
				"* well anyway, " + MACRO_PAUSE_16 + "have fun!" + MACRO_LINE_SEPARATOR +
				"  and the most important...",
				DemoAssets.SPR_FACE_SANSBLINK, DemoAssets.FNT_SANS, DemoAssets.SND_SANS_VOICE);
		this.appendPage(
				"* ...stay nice!",
				DemoAssets.SPR_FACE_SANSBLINK, DemoAssets.FNT_SANS, DemoAssets.SND_SANS_VOICE);
		this.appendPage(
				"...unless you wanna have" + MACRO_LINE_SEPARATOR +
				"a " + MACRO_COLOR_RED + "really bad time" + MACRO_COLOR_DEFAULT + "..." + MACRO_PAUSE_16 + MACRO_LINE_SEPARATOR +
				"...remember?",
				DemoAssets.SPR_FACE_SANSNOEYES, DemoAssets.FNT_DETERMINATION_MONO, -1, 0, 1);
		// @format:on
		
		this.applyPages();
	}
}
