package io.github.nasso.nhengine.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBIWriteCallback;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;

import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.graphics.TextureData;
import io.github.nasso.nhengine.utils.Nhutils;

public class TextureIO {
	private static int[] int_buffer_a = new int[1];
	private static int[] int_buffer_b = new int[1];
	private static int[] int_buffer_c = new int[1];
	
	public static TextureData loadTextureData(InputStream in, int size, int bpp) throws IOException {
		ByteBuffer readData = BufferUtils.createByteBuffer(size);
		
		byte[] buffer = new byte[2048];
		int numByteToRead = 0;
		int numByteRead = 0;
		while((numByteToRead = Math.min(buffer.length, size - numByteRead)) != 0) {
			in.read(buffer, 0, numByteToRead);
			readData.put(buffer, 0, numByteToRead);
			numByteRead += numByteToRead;
		}
		readData.flip();
		
		ByteBuffer data = STBImage.stbi_load_from_memory(readData, TextureIO.int_buffer_a, TextureIO.int_buffer_b, TextureIO.int_buffer_c, bpp);
		
		TextureData texData = new TextureData();
		texData.setWidth(TextureIO.int_buffer_a[0]);
		texData.setHeight(TextureIO.int_buffer_b[0]);
		texData.setData(data);
		texData.setBytesPerPixel(TextureIO.int_buffer_c[0]);
		
		return texData;
	}
	
	public static Texture2D loadTexture2D(InputStream in, int size, int bpp) throws IOException {
		return TextureIO.loadTexture2D(in, size, bpp, false, false, false);
	}
	
	public static Texture2D loadTexture2D(InputStream in, int size, int bpp, boolean gammaCorrect) throws IOException {
		return TextureIO.loadTexture2D(in, size, bpp, gammaCorrect, false, false);
	}
	
	public static Texture2D loadTexture2D(InputStream in, int size, int bpp, boolean gammaCorrect, boolean mipmapping, boolean smooth) throws IOException {
		TextureData data = TextureIO.loadTextureData(in, size, bpp);
		if(data == null || data.getBytesPerPixel() < 1 || data.getBytesPerPixel() > 4) return null;
		
		Texture2D.Builder b = new Texture2D.Builder().width(data.getWidth()).height(data.getHeight()).data(data.getData()).type(Nhengine.UNSIGNED_BYTE);
		
		if(gammaCorrect) b.internalFormat(data.getBytesPerPixel() == 4 ? Nhengine.SRGB_ALPHA : Nhengine.SRGB);
		else {
			switch(data.getBytesPerPixel()) {
				case 4:
					b.internalFormat(Nhengine.RGBA);
					b.format(Nhengine.RGBA);
					break;
				case 3:
					b.internalFormat(Nhengine.RGB);
					b.format(Nhengine.RGB);
					break;
				case 2:
					b.internalFormat(Nhengine.RG);
					b.format(Nhengine.RG);
					break;
				case 1:
					b.internalFormat(Nhengine.RED);
					b.format(Nhengine.RED);
					break;
			}
		}
		
		if(mipmapping) b.minFilter(Nhengine.LINEAR_MIPMAP_LINEAR);
		else b.minFilter(Nhengine.NEAREST_MIPMAP_NEAREST);
		
		if(smooth) b.magFilter(Nhengine.LINEAR);
		else b.magFilter(Nhengine.NEAREST);
		
		return b.build();
	}
	
	public static ByteBuffer convertDataBPP(ByteBuffer source, int sourceBpp, ByteBuffer dest, int destBpp) {
		if(sourceBpp == destBpp) return source;
		if(sourceBpp == 0 || destBpp == 0) return null;
		
		int pixelCount = source.limit() / sourceBpp;
		
		if(dest == null) dest = BufferUtils.createByteBuffer(pixelCount * destBpp);
		
		if(sourceBpp < destBpp) {
			for(int i = 0; i < pixelCount; i++) {
				for(int j = 0; j < sourceBpp; j++)
					dest.put(source.get());
				
				for(int j = 0; j < (destBpp - sourceBpp); j++)
					dest.put((byte) 255);
			}
		} else {
			for(int i = 0; i < pixelCount; i++) {
				for(int j = 0; j < destBpp; j++)
					dest.put(source.get());
				
				for(int j = 0; j < (sourceBpp - destBpp); j++)
					source.get();
			}
		}
		
		dest.flip();
		return dest;
	}
	
	public static TextureData loadTextureData(String filePath, int bpp, boolean inJar) throws IOException {
		ByteBuffer data = null;
		
		byte[] fileDataArray = Nhutils.readFileBytes(filePath, inJar);
		ByteBuffer fileData = BufferUtils.createByteBuffer(fileDataArray.length);
		fileData.put(fileDataArray);
		fileData.flip();
		data = STBImage.stbi_load_from_memory(fileData, TextureIO.int_buffer_a, TextureIO.int_buffer_b, TextureIO.int_buffer_c, 0);
		fileData.clear();
		fileData = null;
		
		ByteBuffer convertedData = convertDataBPP(data, TextureIO.int_buffer_c[0], null, bpp);
		
		TextureData texData = new TextureData();
		texData.setWidth(TextureIO.int_buffer_a[0]);
		texData.setHeight(TextureIO.int_buffer_b[0]);
		texData.setBytesPerPixel(bpp);
		texData.setData(convertedData);
		
		return texData;
	}
	
	public static Texture2D loadTexture2D(String filePath) throws IOException {
		return loadTexture2D(filePath, 4);
	}
	
	public static Texture2D loadTexture2D(String filePath, int bpp) throws IOException {
		return TextureIO.loadTexture2D(filePath, bpp, false, false, false);
	}
	
	public static Texture2D loadTexture2D(String filePath, int bpp, boolean gammaCorrect) throws IOException {
		return TextureIO.loadTexture2D(filePath, bpp, gammaCorrect, false, false);
	}
	
	public static Texture2D loadTexture2D(String filePath, int bpp, boolean gammaCorrect, boolean mipmapping, boolean smooth) throws IOException {
		return loadTexture2D(filePath, bpp, gammaCorrect, mipmapping, smooth, false);
	}
	
	public static Texture2D loadJarTexture2D(String filePath) throws IOException {
		return loadJarTexture2D(filePath, 4);
	}
	
	public static Texture2D loadJarTexture2D(String filePath, int bpp) throws IOException {
		return TextureIO.loadJarTexture2D(filePath, bpp, false, false, false);
	}
	
	public static Texture2D loadJarTexture2D(String filePath, int bpp, boolean gammaCorrect) throws IOException {
		return TextureIO.loadJarTexture2D(filePath, bpp, gammaCorrect, false, false);
	}
	
	public static Texture2D loadJarTexture2D(String filePath, int bpp, boolean gammaCorrect, boolean mipmapping, boolean smooth) throws IOException {
		return loadTexture2D(filePath, bpp, gammaCorrect, mipmapping, smooth, true);
	}
	
	public static Texture2D loadTexture2D(String filePath, int bpp, boolean gammaCorrect, boolean mipmapping, boolean smooth, boolean inJar) throws IOException {
		bpp = 4; // TODO: Fix bpp
		
		TextureData data = TextureIO.loadTextureData(filePath, bpp, inJar);
		if(data == null || data.getBytesPerPixel() < 1 || data.getBytesPerPixel() > 4) return null;
		
		Texture2D.Builder b = new Texture2D.Builder().width(data.getWidth()).height(data.getHeight()).data(data.getData());
		
		if(gammaCorrect && (data.getBytesPerPixel() == 4 || data.getBytesPerPixel() == 3)) b.internalFormat(data.getBytesPerPixel() == 4 ? Nhengine.SRGB_ALPHA : Nhengine.SRGB);
		else {
			switch(data.getBytesPerPixel()) {
				case 4:
					b.internalFormat(Nhengine.RGBA);
					b.format(Nhengine.RGBA);
					break;
				case 3:
					b.internalFormat(Nhengine.RGB);
					b.format(Nhengine.RGB);
					break;
				case 2:
					b.internalFormat(Nhengine.RG);
					b.format(Nhengine.RG);
					break;
				case 1:
					b.internalFormat(Nhengine.RED);
					b.format(Nhengine.RED);
					break;
			}
		}
		
		if(mipmapping) b.minFilter(Nhengine.LINEAR_MIPMAP_LINEAR);
		else b.minFilter(Nhengine.NEAREST_MIPMAP_NEAREST);
		
		if(smooth) b.magFilter(Nhengine.LINEAR);
		else b.magFilter(Nhengine.NEAREST);
		
		return b.build();
	}
	
	public static void writeTexture(OutputStream out, String format, TextureData texData) {
		int width = texData.getWidth();
		int height = texData.getHeight();
		int bpp = texData.getBytesPerPixel();
		ByteBuffer data = texData.getData();
		format = format.toLowerCase();
		if(!format.matches("png|tga|bmp")) return;
		
		STBIWriteCallback writer = new STBIWriteCallback() {
			public void invoke(long context, long data, int size) {
				try {
					ByteBuffer buf = getData(data, size);
					
					for(int i = 0; i < size; i++) {
						out.write(buf.get());
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		if(format.matches("png")) STBImageWrite.stbi_write_png_to_func(writer, 0, width, height, bpp, data, 0);
		else if(format.matches("tga")) STBImageWrite.stbi_write_tga_to_func(writer, 0, width, height, bpp, data);
		else if(format.matches("bmp")) STBImageWrite.stbi_write_bmp_to_func(writer, 0, width, height, bpp, data);
	}
	
	public static void writeTexture(String path, String format, TextureData texData) {
		int width = texData.getWidth();
		int height = texData.getHeight();
		int bpp = texData.getBytesPerPixel();
		ByteBuffer data = texData.getData();
		format = format.toLowerCase();
		if(!format.matches("png|tga|bmp")) return;
		
		File dest = new File(path);
		File parent = dest.getParentFile();
		
		if(!parent.exists() && !parent.mkdirs()) {
			System.err.println("Error while creating the directories " + dest);
			return;
		}
		
		if(format.matches("png")) STBImageWrite.stbi_write_png(path, width, height, bpp, data, 0);
		else if(format.matches("tga")) STBImageWrite.stbi_write_tga(path, width, height, bpp, data);
		else if(format.matches("bmp")) STBImageWrite.stbi_write_bmp(path, width, height, bpp, data);
	}
}
