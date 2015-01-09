package org.visitor.appportal.web.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageUtils {
	protected static final Logger log = LoggerFactory.getLogger(ImageUtils.class);
    /**图片格式：JPG*/
    private static final String PICTRUE_FORMATE_JPG = "jpg";
    private static final String PICTRUE_FORMATE_PNG = "png";
    
    private ImageUtils(){}
    
    /**
     * 添加图片水印
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg
     * @param waterImg  水印图片路径，如：C://myPictrue//logo.png
     * @param x 水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y 水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     */
    public final static void pressImage(String targetImg, String waterImg, int x, int y, float alpha) {
    	if(log.isInfoEnabled()) {
    		log.info("pressImage: targetImg: " + targetImg + "  waterImg:" + waterImg);
    	}
    	try {
    		File file = new File(targetImg);
    		BufferedImage image = ImageIO.read(file);
    		//BufferedImage b_Image = ImageIO.read(file);
    		int width_1 = image.getWidth(null);
    		int height_1 = image.getHeight(null);
                
            BufferedImage waterImage = ImageIO.read(new File(waterImg));    // 水印文件
            int width_2 = waterImage.getWidth(null);
            int height_2 = waterImage.getHeight(null);
                
            BufferedImage bufferedImage = new BufferedImage(width_2, height_2, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = bufferedImage.createGraphics();

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, alpha));
                
            int widthDiff = width_2-width_1;
            int heightDiff = height_2 - height_1;
                
            if(x < 0){
            	x = widthDiff / 2;
            }else if(x > widthDiff){
            	x = widthDiff;
            }
            if(y < 0){
            	y = heightDiff / 2;
            }else if(y > heightDiff){
            	y = heightDiff;
            }

            g.drawImage(waterImage, 0, 0, width_2, height_2, null); // 水印文件结束
            g.drawImage(image, x, y, width_1, height_1, null);
            g.dispose();
            ImageIO.write(bufferedImage, PICTRUE_FORMATE_PNG, file);
            
            //清理一下流信息，可能会避免PermGen OutOfMemrory
            waterImage.flush();
            bufferedImage.flush();
            image.flush();
    	} catch (IOException e) {
    		log.error(e.getMessage(), e);
    	}
    }

    /**
     * 添加文字水印
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg
     * @param pressText 水印文字， 如：中国证券网
     * @param fontName 字体名称，    如：宋体
     * @param fontStyle 字体样式，如：粗体和斜体(Font.BOLD|Font.ITALIC)
     * @param fontSize 字体大小，单位为像素
     * @param color 字体颜色
     * @param x 水印文字距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y 水印文字距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     */
    public static void pressText(String targetImg, String pressText, String fontName, int fontStyle, int fontSize, Color color, int x, int y, float alpha) {
    	if(log.isInfoEnabled()) {
    		log.info("pressText: targetImg: " + targetImg + "  pressText:" + pressText + " fontName: " + fontName);
    	}    	
        try {
            File file = new File(targetImg);
            
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setColor(color);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            
            int width_1 = fontSize * getLength(pressText);
            int height_1 = fontSize;
            int widthDiff = width - width_1;
            int heightDiff = height - height_1;
            if(x < 0){
                x = widthDiff / 2;
            }else if(x > widthDiff){
                x = widthDiff;
            }
            if(y < 0){
                y = heightDiff / 2;
            }else if(y > heightDiff){
                y = heightDiff;
            }
            
            g.drawString(pressText, x, y + height_1);
            g.dispose();
            ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
            
            //清理一下流信息，可能会避免PermGen OutOfMemrory
            bufferedImage.flush();
            image.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符
     * @param text
     * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
*/
    public static int getLength(String text) {
        int textLength = text.length();
        int length = textLength;
        for (int i = 0; i < textLength; i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
                length++;
            }
        }
        return (length % 2 == 0) ? length / 2 : length / 2 + 1;
    }

    /**
     * 图片缩放
     * @param filePath 图片路径
     * @param newfilePath 另存图片路径
     * @param height 高度
     * @param width 宽度
     * @param bb 比例不对时是否需要补白
     */
    public static void resize(String filePath, String newfilePath, int height, int width, boolean bb) {
    	if(log.isInfoEnabled()) {
    		log.info("resize: filePath: " + filePath + "  newfilePath:" + newfilePath);
    	}      	
        try {
            double ratio = 0; //缩放比例    
            File f = new File(filePath);
            File new_f = new File(newfilePath);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
            //计算比例   
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {   
                if (bi.getHeight() > bi.getWidth()) {   
                    ratio = (new Integer(height)).doubleValue() / bi.getHeight();   
                } else {   
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();   
                }   
                AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);   
                itemp = op.filter(bi, null);   
            }
            
            BufferedImage image = new BufferedImage(width, height, bi.getType());   
            Graphics2D g = image.createGraphics();
            
            if (bb) {
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
            }
            
            if (width == itemp.getWidth(null))
                g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), null);
            else
                g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), null);
            
            g.dispose();
            ImageIO.write(image, "png", new_f);
            
            //清理一下流信息，可能会避免PermGen OutOfMemrory
            itemp.flush();
            image.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
    	for(int i=0;i<1000;i++) {
    		resize("/Users/ying_7839/Documents/remotefiles/616150.png","/Users/ying_7839/Documents/remotefiles/616150"+ i + ".png", 72, 72, false);
    	}
        //pressImage("D://work//tmp//273308.png", "D://work//tmp//water//gray.png", -1, -1, 1.0f);
//    	pressImage("D://work//tmp//445169.png", "D://work//tmp//water//activity.png", -1, -1, 1.0f);

        //pressText("C://pic//jpg", "旺仔之印", "宋体", Font.BOLD|Font.ITALIC, 20, Color.BLACK, 0, 0, 8f);
////        resize("D://work//tmp//gray.png", 72, 72, false);
//        resize("D://work//tmp//273510.png", 72, 72, false);
//        resize("D://work//tmp//277247.JPG", 72, 72, false);
//        resize("D://work//tmp//443370.png", 72, 72, false);
//        resize("D://work//tmp//444083.png", 72, 72, false);
//        resize("D://work//tmp//445169.png", 72, 72, false);
//        resize("D://work//tmp//445421.png", 72, 72, false);
    }
}