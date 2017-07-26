package io.github.nasso.nhengine.core;

import java.lang.reflect.Field;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;

public class Nhengine {
	// TODO REMOVE DEBUG WHEN DONE
	public static final boolean DEBUG = true;
	
	// ------------ GLFW ------------
	
	public static final int OPENGL_API = GLFW.GLFW_OPENGL_API;
	public static final int OPENGL_ES_API = GLFW.GLFW_OPENGL_ES_API;
	
	/** The key or button was released. */
	public static final int RELEASE = GLFW.GLFW_RELEASE;
	
	/** The key or button was pressed. */
	public static final int PRESS = GLFW.GLFW_PRESS;
	
	/** The key was held down until it repeated. */
	public static final int REPEAT = GLFW.GLFW_REPEAT;
	
	// Unknown
	public static final int KEY_UNKNOWN = GLFW.GLFW_KEY_UNKNOWN;
	
	// Printable keys
	public static final int KEY_SPACE = GLFW.GLFW_KEY_SPACE;
	public static final int KEY_APOSTROPHE = GLFW.GLFW_KEY_APOSTROPHE;
	public static final int KEY_COMMA = GLFW.GLFW_KEY_COMMA;
	public static final int KEY_MINUS = GLFW.GLFW_KEY_MINUS;
	public static final int KEY_PERIOD = GLFW.GLFW_KEY_PERIOD;
	public static final int KEY_SLASH = GLFW.GLFW_KEY_SLASH;
	public static final int KEY_0 = GLFW.GLFW_KEY_0;
	public static final int KEY_1 = GLFW.GLFW_KEY_1;
	public static final int KEY_2 = GLFW.GLFW_KEY_2;
	public static final int KEY_3 = GLFW.GLFW_KEY_3;
	public static final int KEY_4 = GLFW.GLFW_KEY_4;
	public static final int KEY_5 = GLFW.GLFW_KEY_5;
	public static final int KEY_6 = GLFW.GLFW_KEY_6;
	public static final int KEY_7 = GLFW.GLFW_KEY_7;
	public static final int KEY_8 = GLFW.GLFW_KEY_8;
	public static final int KEY_9 = GLFW.GLFW_KEY_9;
	public static final int KEY_SEMICOLON = GLFW.GLFW_KEY_SEMICOLON;
	public static final int KEY_EQUAL = GLFW.GLFW_KEY_EQUAL;
	public static final int KEY_A = GLFW.GLFW_KEY_A;
	public static final int KEY_B = GLFW.GLFW_KEY_B;
	public static final int KEY_C = GLFW.GLFW_KEY_C;
	public static final int KEY_D = GLFW.GLFW_KEY_D;
	public static final int KEY_E = GLFW.GLFW_KEY_E;
	public static final int KEY_F = GLFW.GLFW_KEY_F;
	public static final int KEY_G = GLFW.GLFW_KEY_G;
	public static final int KEY_H = GLFW.GLFW_KEY_H;
	public static final int KEY_I = GLFW.GLFW_KEY_I;
	public static final int KEY_J = GLFW.GLFW_KEY_J;
	public static final int KEY_K = GLFW.GLFW_KEY_K;
	public static final int KEY_L = GLFW.GLFW_KEY_L;
	public static final int KEY_M = GLFW.GLFW_KEY_M;
	public static final int KEY_N = GLFW.GLFW_KEY_N;
	public static final int KEY_O = GLFW.GLFW_KEY_O;
	public static final int KEY_P = GLFW.GLFW_KEY_P;
	public static final int KEY_Q = GLFW.GLFW_KEY_Q;
	public static final int KEY_R = GLFW.GLFW_KEY_R;
	public static final int KEY_S = GLFW.GLFW_KEY_S;
	public static final int KEY_T = GLFW.GLFW_KEY_T;
	public static final int KEY_U = GLFW.GLFW_KEY_U;
	public static final int KEY_V = GLFW.GLFW_KEY_V;
	public static final int KEY_W = GLFW.GLFW_KEY_W;
	public static final int KEY_X = GLFW.GLFW_KEY_X;
	public static final int KEY_Y = GLFW.GLFW_KEY_Y;
	public static final int KEY_Z = GLFW.GLFW_KEY_Z;
	public static final int KEY_LEFT_BRACKET = GLFW.GLFW_KEY_LEFT_BRACKET;
	public static final int KEY_BACKSLASH = GLFW.GLFW_KEY_BACKSLASH;
	public static final int KEY_RIGHT_BRACKET = GLFW.GLFW_KEY_RIGHT_BRACKET;
	public static final int KEY_GRAVE_ACCENT = GLFW.GLFW_KEY_GRAVE_ACCENT;
	public static final int KEY_WORLD_1 = GLFW.GLFW_KEY_WORLD_1;
	public static final int KEY_WORLD_2 = GLFW.GLFW_KEY_WORLD_2;
	
	// Functions keys
	public static final int KEY_ESCAPE = GLFW.GLFW_KEY_ESCAPE;
	public static final int KEY_ENTER = GLFW.GLFW_KEY_ENTER;
	public static final int KEY_TAB = GLFW.GLFW_KEY_TAB;
	public static final int KEY_BACKSPACE = GLFW.GLFW_KEY_BACKSPACE;
	public static final int KEY_INSERT = GLFW.GLFW_KEY_INSERT;
	public static final int KEY_DELETE = GLFW.GLFW_KEY_DELETE;
	public static final int KEY_RIGHT = GLFW.GLFW_KEY_RIGHT;
	public static final int KEY_LEFT = GLFW.GLFW_KEY_LEFT;
	public static final int KEY_DOWN = GLFW.GLFW_KEY_DOWN;
	public static final int KEY_UP = GLFW.GLFW_KEY_UP;
	public static final int KEY_PAGE_UP = GLFW.GLFW_KEY_PAGE_UP;
	public static final int KEY_PAGE_DOWN = GLFW.GLFW_KEY_PAGE_DOWN;
	public static final int KEY_HOME = GLFW.GLFW_KEY_HOME;
	public static final int KEY_END = GLFW.GLFW_KEY_END;
	public static final int KEY_CAPS_LOCK = GLFW.GLFW_KEY_CAPS_LOCK;
	public static final int KEY_SCROLL_LOCK = GLFW.GLFW_KEY_SCROLL_LOCK;
	public static final int KEY_NUM_LOCK = GLFW.GLFW_KEY_NUM_LOCK;
	public static final int KEY_PRINT_SCREEN = GLFW.GLFW_KEY_PRINT_SCREEN;
	public static final int KEY_PAUSE = GLFW.GLFW_KEY_PAUSE;
	public static final int KEY_F1 = GLFW.GLFW_KEY_F1;
	public static final int KEY_F2 = GLFW.GLFW_KEY_F2;
	public static final int KEY_F3 = GLFW.GLFW_KEY_F3;
	public static final int KEY_F4 = GLFW.GLFW_KEY_F4;
	public static final int KEY_F5 = GLFW.GLFW_KEY_F5;
	public static final int KEY_F6 = GLFW.GLFW_KEY_F6;
	public static final int KEY_F7 = GLFW.GLFW_KEY_F7;
	public static final int KEY_F8 = GLFW.GLFW_KEY_F8;
	public static final int KEY_F9 = GLFW.GLFW_KEY_F9;
	public static final int KEY_F10 = GLFW.GLFW_KEY_F10;
	public static final int KEY_F11 = GLFW.GLFW_KEY_F11;
	public static final int KEY_F12 = GLFW.GLFW_KEY_F12;
	public static final int KEY_F13 = GLFW.GLFW_KEY_F13;
	public static final int KEY_F14 = GLFW.GLFW_KEY_F14;
	public static final int KEY_F15 = GLFW.GLFW_KEY_F15;
	public static final int KEY_F16 = GLFW.GLFW_KEY_F16;
	public static final int KEY_F17 = GLFW.GLFW_KEY_F17;
	public static final int KEY_F18 = GLFW.GLFW_KEY_F18;
	public static final int KEY_F19 = GLFW.GLFW_KEY_F19;
	public static final int KEY_F20 = GLFW.GLFW_KEY_F20;
	public static final int KEY_F21 = GLFW.GLFW_KEY_F21;
	public static final int KEY_F22 = GLFW.GLFW_KEY_F22;
	public static final int KEY_F23 = GLFW.GLFW_KEY_F23;
	public static final int KEY_F24 = GLFW.GLFW_KEY_F24;
	public static final int KEY_F25 = GLFW.GLFW_KEY_F25;
	public static final int KEY_KP_0 = GLFW.GLFW_KEY_KP_0;
	public static final int KEY_KP_1 = GLFW.GLFW_KEY_KP_1;
	public static final int KEY_KP_2 = GLFW.GLFW_KEY_KP_2;
	public static final int KEY_KP_3 = GLFW.GLFW_KEY_KP_3;
	public static final int KEY_KP_4 = GLFW.GLFW_KEY_KP_4;
	public static final int KEY_KP_5 = GLFW.GLFW_KEY_KP_5;
	public static final int KEY_KP_6 = GLFW.GLFW_KEY_KP_6;
	public static final int KEY_KP_7 = GLFW.GLFW_KEY_KP_7;
	public static final int KEY_KP_8 = GLFW.GLFW_KEY_KP_8;
	public static final int KEY_KP_9 = GLFW.GLFW_KEY_KP_9;
	public static final int KEY_KP_DECIMAL = GLFW.GLFW_KEY_KP_DECIMAL;
	public static final int KEY_KP_DIVIDE = GLFW.GLFW_KEY_KP_DIVIDE;
	public static final int KEY_KP_MULTIPLY = GLFW.GLFW_KEY_KP_MULTIPLY;
	public static final int KEY_KP_SUBTRACT = GLFW.GLFW_KEY_KP_SUBTRACT;
	public static final int KEY_KP_ADD = GLFW.GLFW_KEY_KP_ADD;
	public static final int KEY_KP_ENTER = GLFW.GLFW_KEY_KP_ENTER;
	public static final int KEY_KP_EQUAL = GLFW.GLFW_KEY_KP_EQUAL;
	public static final int KEY_LEFT_SHIFT = GLFW.GLFW_KEY_LEFT_SHIFT;
	public static final int KEY_LEFT_CONTROL = GLFW.GLFW_KEY_LEFT_CONTROL;
	public static final int KEY_LEFT_ALT = GLFW.GLFW_KEY_LEFT_ALT;
	public static final int KEY_LEFT_SUPER = GLFW.GLFW_KEY_LEFT_SUPER;
	public static final int KEY_RIGHT_SHIFT = GLFW.GLFW_KEY_RIGHT_SHIFT;
	public static final int KEY_RIGHT_CONTROL = GLFW.GLFW_KEY_RIGHT_CONTROL;
	public static final int KEY_RIGHT_ALT = GLFW.GLFW_KEY_RIGHT_ALT;
	public static final int KEY_RIGHT_SUPER = GLFW.GLFW_KEY_RIGHT_SUPER;
	public static final int KEY_MENU = GLFW.GLFW_KEY_MENU;
	
	// Scancodes...
	public static final int SCANCODE_SPACE = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_SPACE);
	public static final int SCANCODE_APOSTROPHE = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_APOSTROPHE);
	public static final int SCANCODE_COMMA = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_COMMA);
	public static final int SCANCODE_MINUS = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_MINUS);
	public static final int SCANCODE_PERIOD = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_PERIOD);
	public static final int SCANCODE_SLASH = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_SLASH);
	public static final int SCANCODE_0 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_0);
	public static final int SCANCODE_1 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_1);
	public static final int SCANCODE_2 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_2);
	public static final int SCANCODE_3 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_3);
	public static final int SCANCODE_4 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_4);
	public static final int SCANCODE_5 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_5);
	public static final int SCANCODE_6 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_6);
	public static final int SCANCODE_7 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_7);
	public static final int SCANCODE_8 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_8);
	public static final int SCANCODE_9 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_9);
	public static final int SCANCODE_SEMICOLON = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_SEMICOLON);
	public static final int SCANCODE_EQUAL = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_EQUAL);
	public static final int SCANCODE_A = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_A);
	public static final int SCANCODE_B = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_B);
	public static final int SCANCODE_C = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_C);
	public static final int SCANCODE_D = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_D);
	public static final int SCANCODE_E = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_E);
	public static final int SCANCODE_F = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F);
	public static final int SCANCODE_G = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_G);
	public static final int SCANCODE_H = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_H);
	public static final int SCANCODE_I = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_I);
	public static final int SCANCODE_J = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_J);
	public static final int SCANCODE_K = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_K);
	public static final int SCANCODE_L = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_L);
	public static final int SCANCODE_M = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_M);
	public static final int SCANCODE_N = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_N);
	public static final int SCANCODE_O = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_O);
	public static final int SCANCODE_P = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_P);
	public static final int SCANCODE_Q = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_Q);
	public static final int SCANCODE_R = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_R);
	public static final int SCANCODE_S = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_S);
	public static final int SCANCODE_T = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_T);
	public static final int SCANCODE_U = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_U);
	public static final int SCANCODE_V = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_V);
	public static final int SCANCODE_W = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_W);
	public static final int SCANCODE_X = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_X);
	public static final int SCANCODE_Y = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_Y);
	public static final int SCANCODE_Z = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_Z);
	public static final int SCANCODE_LEFT_BRACKET = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_LEFT_BRACKET);
	public static final int SCANCODE_BACKSLASH = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_BACKSLASH);
	public static final int SCANCODE_RIGHT_BRACKET = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_RIGHT_BRACKET);
	public static final int SCANCODE_GRAVE_ACCENT = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_GRAVE_ACCENT);
	public static final int SCANCODE_WORLD_1 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_WORLD_1);
	public static final int SCANCODE_WORLD_2 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_WORLD_2);
	
	public static final int SCANCODE_ESCAPE = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_ESCAPE);
	public static final int SCANCODE_ENTER = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_ENTER);
	public static final int SCANCODE_TAB = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_TAB);
	public static final int SCANCODE_BACKSPACE = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_BACKSPACE);
	public static final int SCANCODE_INSERT = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_INSERT);
	public static final int SCANCODE_DELETE = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_DELETE);
	public static final int SCANCODE_RIGHT = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_RIGHT);
	public static final int SCANCODE_LEFT = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_LEFT);
	public static final int SCANCODE_DOWN = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_DOWN);
	public static final int SCANCODE_UP = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_UP);
	public static final int SCANCODE_PAGE_UP = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_PAGE_UP);
	public static final int SCANCODE_PAGE_DOWN = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_PAGE_DOWN);
	public static final int SCANCODE_HOME = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_HOME);
	public static final int SCANCODE_END = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_END);
	public static final int SCANCODE_CAPS_LOCK = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_CAPS_LOCK);
	public static final int SCANCODE_SCROLL_LOCK = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_SCROLL_LOCK);
	public static final int SCANCODE_NUM_LOCK = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_NUM_LOCK);
	public static final int SCANCODE_PRINT_SCREEN = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_PRINT_SCREEN);
	public static final int SCANCODE_PAUSE = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_PAUSE);
	public static final int SCANCODE_F1 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F1);
	public static final int SCANCODE_F2 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F2);
	public static final int SCANCODE_F3 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F3);
	public static final int SCANCODE_F4 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F4);
	public static final int SCANCODE_F5 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F5);
	public static final int SCANCODE_F6 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F6);
	public static final int SCANCODE_F7 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F7);
	public static final int SCANCODE_F8 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F8);
	public static final int SCANCODE_F9 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F9);
	public static final int SCANCODE_F10 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F10);
	public static final int SCANCODE_F11 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F11);
	public static final int SCANCODE_F12 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F12);
	public static final int SCANCODE_F13 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F13);
	public static final int SCANCODE_F14 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F14);
	public static final int SCANCODE_F15 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F15);
	public static final int SCANCODE_F16 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F16);
	public static final int SCANCODE_F17 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F17);
	public static final int SCANCODE_F18 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F18);
	public static final int SCANCODE_F19 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F19);
	public static final int SCANCODE_F20 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F20);
	public static final int SCANCODE_F21 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F21);
	public static final int SCANCODE_F22 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F22);
	public static final int SCANCODE_F23 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F23);
	public static final int SCANCODE_F24 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F24);
	public static final int SCANCODE_F25 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_F25);
	public static final int SCANCODE_KP_0 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_0);
	public static final int SCANCODE_KP_1 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_1);
	public static final int SCANCODE_KP_2 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_2);
	public static final int SCANCODE_KP_3 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_3);
	public static final int SCANCODE_KP_4 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_4);
	public static final int SCANCODE_KP_5 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_5);
	public static final int SCANCODE_KP_6 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_6);
	public static final int SCANCODE_KP_7 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_7);
	public static final int SCANCODE_KP_8 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_8);
	public static final int SCANCODE_KP_9 = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_9);
	public static final int SCANCODE_KP_DECIMAL = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_DECIMAL);
	public static final int SCANCODE_KP_DIVIDE = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_DIVIDE);
	public static final int SCANCODE_KP_MULTIPLY = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_MULTIPLY);
	public static final int SCANCODE_KP_SUBTRACT = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_SUBTRACT);
	public static final int SCANCODE_KP_ADD = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_ADD);
	public static final int SCANCODE_KP_ENTER = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_ENTER);
	public static final int SCANCODE_KP_EQUAL = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_KP_EQUAL);
	public static final int SCANCODE_LEFT_SHIFT = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_LEFT_SHIFT);
	public static final int SCANCODE_LEFT_CONTROL = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_LEFT_CONTROL);
	public static final int SCANCODE_LEFT_ALT = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_LEFT_ALT);
	public static final int SCANCODE_LEFT_SUPER = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_LEFT_SUPER);
	public static final int SCANCODE_RIGHT_SHIFT = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_RIGHT_SHIFT);
	public static final int SCANCODE_RIGHT_CONTROL = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_RIGHT_CONTROL);
	public static final int SCANCODE_RIGHT_ALT = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_RIGHT_ALT);
	public static final int SCANCODE_RIGHT_SUPER = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_RIGHT_SUPER);
	public static final int SCANCODE_MENU = GLFW.glfwGetKeyScancode(GLFW.GLFW_KEY_MENU);
	
	// Mouse
	public static final int MOUSE_BUTTON_1 = GLFW.GLFW_MOUSE_BUTTON_1;
	public static final int MOUSE_BUTTON_2 = GLFW.GLFW_MOUSE_BUTTON_2;
	public static final int MOUSE_BUTTON_3 = GLFW.GLFW_MOUSE_BUTTON_3;
	public static final int MOUSE_BUTTON_4 = GLFW.GLFW_MOUSE_BUTTON_4;
	public static final int MOUSE_BUTTON_5 = GLFW.GLFW_MOUSE_BUTTON_5;
	public static final int MOUSE_BUTTON_6 = GLFW.GLFW_MOUSE_BUTTON_6;
	public static final int MOUSE_BUTTON_7 = GLFW.GLFW_MOUSE_BUTTON_7;
	public static final int MOUSE_BUTTON_8 = GLFW.GLFW_MOUSE_BUTTON_8;
	public static final int MOUSE_BUTTON_LAST = GLFW.GLFW_MOUSE_BUTTON_LAST;
	public static final int MOUSE_BUTTON_LEFT = GLFW.GLFW_MOUSE_BUTTON_LEFT;
	public static final int MOUSE_BUTTON_MIDDLE = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
	public static final int MOUSE_BUTTON_RIGHT = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
	
	// Joystick
	public static final int JOYSTICK_1 = GLFW.GLFW_JOYSTICK_1;
	public static final int JOYSTICK_2 = GLFW.GLFW_JOYSTICK_2;
	public static final int JOYSTICK_3 = GLFW.GLFW_JOYSTICK_3;
	public static final int JOYSTICK_4 = GLFW.GLFW_JOYSTICK_4;
	public static final int JOYSTICK_5 = GLFW.GLFW_JOYSTICK_5;
	public static final int JOYSTICK_6 = GLFW.GLFW_JOYSTICK_6;
	public static final int JOYSTICK_7 = GLFW.GLFW_JOYSTICK_7;
	public static final int JOYSTICK_8 = GLFW.GLFW_JOYSTICK_8;
	public static final int JOYSTICK_9 = GLFW.GLFW_JOYSTICK_9;
	public static final int JOYSTICK_10 = GLFW.GLFW_JOYSTICK_10;
	public static final int JOYSTICK_11 = GLFW.GLFW_JOYSTICK_11;
	public static final int JOYSTICK_12 = GLFW.GLFW_JOYSTICK_12;
	public static final int JOYSTICK_13 = GLFW.GLFW_JOYSTICK_13;
	public static final int JOYSTICK_14 = GLFW.GLFW_JOYSTICK_14;
	public static final int JOYSTICK_15 = GLFW.GLFW_JOYSTICK_15;
	public static final int JOYSTICK_16 = GLFW.GLFW_JOYSTICK_16;
	
	// Cursor
	public static final int CURSOR_DISABLED = GLFW.GLFW_CURSOR_DISABLED;
	public static final int CURSOR_HIDDEN = GLFW.GLFW_CURSOR_HIDDEN;
	public static final int CURSOR_NORMAL = GLFW.GLFW_CURSOR_NORMAL;
	
	public static final int ARROW_CURSOR = GLFW.GLFW_ARROW_CURSOR;
	public static final int IBEAM_CURSOR = GLFW.GLFW_IBEAM_CURSOR;
	public static final int CROSSHAIR_CURSOR = GLFW.GLFW_CROSSHAIR_CURSOR;
	public static final int HAND_CURSOR = GLFW.GLFW_HAND_CURSOR;
	public static final int HRESIZE_CURSOR = GLFW.GLFW_HRESIZE_CURSOR;
	public static final int VRESIZE_CURSOR = GLFW.GLFW_VRESIZE_CURSOR;
	
	public static final int getScancode(int key) {
		return GLFW.glfwGetKeyScancode(key);
	}
	
	/**
	 * <b>From GLFW's documentation:</b><br>
	 * <br>
	 * Returns the layout-specific name of the specified printable key.
	 * 
	 * <p>
	 * This function returns the name of the specified printable key, encoded as UTF-8. This is typically the character that key would produce without any modifier keys, intended for displaying key bindings to the user. For dead keys, it is typically the diacritic it would add to a character.
	 * </p>
	 * 
	 * <p>
	 * <b>Do not use this function</b> for text input. You will break text input for many languages even if it happens to work for yours.
	 * </p>
	 * 
	 * <p>
	 * If the key is {@link #KEY_UNKNOWN KEY_UNKNOWN}, the scancode is used to identify the key, otherwise the scancode is ignored. If you specify a non-printable key, or {@link #KEY_UNKNOWN KEY_UNKNOWN} and a scancode that maps to a non-printable key, this function returns {@code NULL} but does not emit an error.
	 * </p>
	 * 
	 * <p>
	 * This behavior allows you to always pass in the arguments in the key callback without modification.
	 * </p>
	 * 
	 * <p>
	 * The printable keys are:
	 * </p>
	 * 
	 * <ul>
	 * <li>{@link #KEY_APOSTROPHE KEY_APOSTROPHE}</li>
	 * <li>{@link #KEY_COMMA KEY_COMMA}</li>
	 * <li>{@link #KEY_MINUS KEY_MINUS}</li>
	 * <li>{@link #KEY_PERIOD KEY_PERIOD}</li>
	 * <li>{@link #KEY_SLASH KEY_SLASH}</li>
	 * <li>{@link #KEY_SEMICOLON KEY_SEMICOLON}</li>
	 * <li>{@link #KEY_EQUAL KEY_EQUAL}</li>
	 * <li>{@link #KEY_LEFT_BRACKET KEY_LEFT_BRACKET}</li>
	 * <li>{@link #KEY_RIGHT_BRACKET KEY_RIGHT_BRACKET}</li>
	 * <li>{@link #KEY_BACKSLASH KEY_BACKSLASH}</li>
	 * <li>{@link #KEY_WORLD_1 KEY_WORLD_1}</li>
	 * <li>{@link #KEY_WORLD_2 KEY_WORLD_2}</li>
	 * <li>{@link #KEY_0 KEY_0} to {@link #KEY_9 KEY_9}</li>
	 * <li>{@link #KEY_A KEY_A} to {@link #KEY_Z KEY_Z}</li>
	 * <li>{@link #KEY_KP_0 KEY_KP_0} to {@link #KEY_KP_9 KEY_KP_9}</li>
	 * <li>{@link #KEY_KP_DECIMAL KEY_KP_DECIMAL}</li>
	 * <li>{@link #KEY_KP_DIVIDE KEY_KP_DIVIDE}</li>
	 * <li>{@link #KEY_KP_MULTIPLY KEY_KP_MULTIPLY}</li>
	 * <li>{@link #KEY_KP_SUBTRACT KEY_KP_SUBTRACT}</li>
	 * <li>{@link #KEY_KP_ADD KEY_KP_ADD}</li>
	 * <li>{@link #KEY_KP_EQUAL KEY_KP_EQUAL}</li>
	 * </ul>
	 * 
	 * <p>
	 * Names for printable keys depend on keyboard layout, while names for non-printable keys are the same across layouts but depend on the application language and should be localized along with other user interface text.
	 * </p>
	 * 
	 * <p>
	 * This function must only be called from the main thread.
	 * </p>
	 *
	 * @param key
	 *            the key to query, or {@link #GLFW_KEY_UNKNOWN KEY_UNKNOWN}
	 * @param scancode
	 *            the scancode of the key to query
	 *
	 * @return the UTF-8 encoded, layout-specific name of the key, or {@code NULL}
	 */
	public static final String getKeyName(int key) {
		return GLFW.glfwGetKeyName(key, 0);
	}
	
	// ------------ RENDERING ------------
	public static final int getOpenGLConst(int nhen) {
		int opengl = 0;
		
		if(nhen == Nhengine.REPEAT) opengl = GL11.GL_REPEAT;
		else opengl = nhen; // Nhengine's constants are the OpenGL ones
		
		return opengl;
	}
	
	public static final int NEAREST = GL11.GL_NEAREST;
	public static final int LINEAR = GL11.GL_LINEAR;
	
	public static final int NEAREST_MIPMAP_NEAREST = GL11.GL_NEAREST_MIPMAP_NEAREST;
	public static final int LINEAR_MIPMAP_NEAREST = GL11.GL_LINEAR_MIPMAP_NEAREST;
	public static final int NEAREST_MIPMAP_LINEAR = GL11.GL_NEAREST_MIPMAP_LINEAR;
	public static final int LINEAR_MIPMAP_LINEAR = GL11.GL_LINEAR_MIPMAP_LINEAR;
	
	public static final int CLAMP = GL11.GL_CLAMP;
	// public static final int REPEAT = GL_REPEAT; Already defined for GLFW
	public static final int CLAMP_TO_EDGE = GL12.GL_CLAMP_TO_EDGE;
	
	public static final int COLOR_INDEX = GL11.GL_COLOR_INDEX;
	public static final int STENCIL_INDEX = GL11.GL_STENCIL_INDEX;
	public static final int DEPTH_COMPONENT = GL11.GL_DEPTH_COMPONENT;
	public static final int RED = GL11.GL_RED;
	public static final int GREEN = GL11.GL_GREEN;
	public static final int BLUE = GL11.GL_BLUE;
	public static final int ALPHA = GL11.GL_ALPHA;
	public static final int RG = GL30.GL_RG;
	public static final int RGB = GL11.GL_RGB;
	public static final int RGBA = GL11.GL_RGBA;
	public static final int LUMINANCE = GL11.GL_LUMINANCE;
	public static final int LUMINANCE_ALPHA = GL11.GL_LUMINANCE_ALPHA;
	public static final int SRGB = GL21.GL_SRGB;
	public static final int SRGB_ALPHA = GL21.GL_SRGB_ALPHA;
	
	public static final int POINT = GL11.GL_POINT;
	public static final int LINE = GL11.GL_LINE;
	public static final int FILL = GL11.GL_FILL;
	
	public static final int POINTS = GL11.GL_POINTS;
	public static final int LINES = GL11.GL_LINES;
	public static final int LINE_LOOP = GL11.GL_LINE_LOOP;
	public static final int TRIANGLES = GL11.GL_TRIANGLES;
	public static final int TRIANGLES_STRIP = GL11.GL_TRIANGLE_STRIP;
	public static final int TRIANGLES_FAN = GL11.GL_TRIANGLE_FAN;
	
	public static final int BYTE = GL11.GL_BYTE;
	public static final int UNSIGNED_BYTE = GL11.GL_UNSIGNED_BYTE;
	public static final int SHORT = GL11.GL_SHORT;
	public static final int UNSIGNED_SHORT = GL11.GL_UNSIGNED_SHORT;
	public static final int INT = GL11.GL_INT;
	public static final int UNSIGNED_INT = GL11.GL_UNSIGNED_INT;
	public static final int FLOAT = GL11.GL_FLOAT;
	public static final int BYTES_2 = GL11.GL_2_BYTES;
	public static final int BYTES_3 = GL11.GL_3_BYTES;
	public static final int BYTES_4 = GL11.GL_4_BYTES;
	public static final int DOUBLE = GL11.GL_DOUBLE;
	
	public static final int NONE = GL11.GL_NONE;
	public static final int FRONT = GL11.GL_FRONT;
	public static final int BACK = GL11.GL_BACK;
	public static final int FRONT_AND_BACK = GL11.GL_FRONT_AND_BACK;
	
	public static int valueOf(String what) {
		try {
			Field f = Nhengine.class.getField(what);
			if(f != null) return f.getInt(null);
		} catch(IllegalArgumentException e) {
			e.printStackTrace();
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		} catch(NoSuchFieldException e) {
			e.printStackTrace();
		} catch(SecurityException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
}
