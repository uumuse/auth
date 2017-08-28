package com.kuke.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageCut {
	
	public static int divWidth = 200;

	public static File scale(File srcImgFile, String destImgFilePath, String name, double standardWidth, double standardHeight) {
		File destFile = null;
		try {
			BufferedImage src = ImageIO.read(srcImgFile); // 读入文件
			Image image = src.getScaledInstance((int) standardWidth, (int) standardHeight, Image.SCALE_DEFAULT);

			BufferedImage tag = new BufferedImage((int) standardWidth, (int) standardHeight, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();

			destFile = new File(destImgFilePath + File.separator + (int) standardWidth + File.separator + name);
			if(!destFile.getParentFile().exists()){
				destFile.getParentFile().mkdirs();
			}
			ImageIO.write(tag, "JPEG", destFile);// 输出到文件流

			tag.flush();
			tag = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return destFile;
	}
	/**
	 * @param srcImageFile
	 *            源图像地址
	 * @param x
	 *            目标切片起点x坐标
	 * @param y
	 *            目标切片起点y坐标
	 * @param destWidth
	 *            目标切片宽度
	 * @param destHeight
	 *            目标切片高度
	 */
	public static boolean abscut(String srcImageFile, String tarImageFile, int x,
			int y, int destWidth, int destHeight) {
		boolean flag = false;
		try {
			Image img;
			ImageFilter cropFilter;
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getWidth(); // 源图宽度
			int srcHeight = bi.getHeight(); // 源图高度
			Image image = bi.getScaledInstance(srcWidth, srcHeight,Image.SCALE_DEFAULT);
			bi.flush();
			bi = null;
			if (srcWidth >= destWidth && srcHeight >= destHeight) {
				/* ****************************************
				 * 判断原图的宽高和DIV宽高的大小 根据图片外层DIV的宽度，选择的起始点则有相对变化
				 * **************************************
				 */
				int[] CSize = CSize(srcImageFile, divWidth, divWidth);
				int t = divWidth;
				if (CSize[0] >= CSize[1]) {
					t = CSize[0];
				} else {
					t = CSize[0];
				}
				int x1 = x * srcWidth / t;
				int y1 = y * srcWidth / t;
				int w = destWidth * srcWidth / t;
				int h = destHeight * srcWidth / t;
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				cropFilter = new CropImageFilter(x1, y1, w, h);
				img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				// 输出为文件
				ImageIO.write(tag, "JPEG", new File(tarImageFile));
				tag.flush();
				tag = null;
				flag = true;
			}else if(srcWidth >= destWidth && srcHeight<destHeight){
				/* ****************************************
				 * 判断原图的宽高和DIV宽高的大小 根据图片外层DIV的宽度，选择的起始点则有相对变化
				 * **************************************
				 */
				int[] CSize = CSize(srcImageFile, divWidth, divWidth);
				int t = divWidth;
				if (CSize[0] >= CSize[1]) {
					t = CSize[0];
				} else {
					t = CSize[0];
				}
				int x1 = x * srcWidth / t;
				int y1 = y * srcWidth / t;
				int w = destWidth * srcWidth / t;
				int h = srcHeight * srcWidth / t;
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				cropFilter = new CropImageFilter(x1, y1, w, h);
				img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				// 输出为文件
				ImageIO.write(tag, "JPEG", new File(tarImageFile));
				tag.flush();
				tag = null;
				flag = true;
			}else if(srcWidth<destWidth && srcHeight>=destHeight){
				/* ****************************************
				 * 判断原图的宽高和DIV宽高的大小 根据图片外层DIV的宽度，选择的起始点则有相对变化
				 * **************************************
				 */
				int[] CSize = CSize(srcImageFile, divWidth, divWidth);
				int t = divWidth;
				if (CSize[0] >= CSize[1]) {
					t = CSize[0];
				} else {
					t = CSize[0];
				}
				int x1 = x * srcWidth / t;
				int y1 = y * srcWidth / t;
				int w = srcWidth * srcWidth / t;
				int h = destHeight * srcWidth / t;
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				cropFilter = new CropImageFilter(x1, y1, w, h);
				img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				// 输出为文件
				ImageIO.write(tag, "JPEG", new File(tarImageFile));
				tag.flush();
				tag = null;
				flag = true;
			}else{
				/* ****************************************
				 * 判断原图的宽高和DIV宽高的大小 根据图片外层DIV的宽度，选择的起始点则有相对变化
				 * **************************************
				 */
//				int[] CSize = CSize(srcImageFile, divWidth, divWidth);
				int[] CSize = CSize(srcImageFile, srcWidth, srcHeight);
				int t = divWidth;
				if (CSize[0] >= CSize[1]) {
					t = CSize[0];
				} else {
					t = CSize[0];
				}
				int x1 = x * srcWidth / t;
				int y1 = y * srcWidth / t;
				int w = srcWidth * srcWidth / t;
				int h = srcHeight * srcWidth / t;
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				cropFilter = new CropImageFilter(x1, y1, w, h);
				img = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				// 输出为文件
				ImageIO.write(tag, "JPEG", new File(tarImageFile));
				tag.flush();
				tag = null;
				flag = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  flag;
	}
	
	

    
    
    public static int[] CSize(String srcImageFile, int tarWidths, int tarHeights) {  
		int[] CSize = new int[2];
        try {  
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getWidth(); // 源图宽度
			int srcHeight = bi.getHeight(); // 源图高度
			bi.flush();
			bi = null;
			float percent = 0L;
			float percentW = ((float) (float) tarWidths / (float) srcWidth);
			float percentH = ((float) (float) tarHeights / (float) srcHeight);
			
			if (percentH < percentW) {
				percent = percentH;
			} else {
				percent = percentW;
			}
			int destWidth = (int) ((int) srcWidth * percent);
			int destHeight = (int) ((int) srcHeight * percent);
			CSize[0] = destWidth;
			CSize[1] = destHeight;
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
		return CSize;
    } 
}
