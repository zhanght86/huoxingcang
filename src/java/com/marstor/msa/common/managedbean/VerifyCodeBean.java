/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marstor.msa.common.managedbean;

import com.marstor.msa.common.util.PathUtil;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Administrator
 */
@ManagedBean(name = "verifyCode")
@SessionScoped
public final class VerifyCodeBean
{

    private VerifyCodeImage image = null;
    private StreamedContent code;
    private int verifyCodeCount = 4;
    private String input = "";

    public VerifyCodeBean()
    {
        image = new VerifyCodeImage();
        image.init(verifyCodeCount);
    }

    public void close()
    {
        input = "";
    }

    public void updateImage()
    {
        System.out.println("updateImage");
        try
        {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image.createImage(), "png", os);
            System.out.println("updateImage = " + image.code);
            code = new DefaultStreamedContent(new ByteArrayInputStream(os.toByteArray()), "image/png");
        }
        catch (IOException ex)
        {
            Logger.getLogger(VerifyCodeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void resetDialog()
    {
        updateImage();
    }

    public void refreshCode()
    {
    }

    public StreamedContent getCode()
    {
        try
        {
            
            if (code ==null || code.getStream().available() == 0)
            {
                updateImage();
            }
System.out.println(" getCode =   " + code);
            System.out.println(" getCode =   " + code.getStream());
            System.out.println(" getCode =   " + code.getStream().available());
        }
        catch (IOException ex)
        {
            Logger.getLogger(VerifyCodeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return code;
    }

    public void setCode(StreamedContent code)
    {
        this.code = code;
    }

    public String getImageCode()
    {
        System.out.println("getImageCode = " + image.code);
        System.out.println("getImageCode code = " + code);
        return image.code;
    }

    public VerifyCodeImage getImage()
    {
        return image;
    }

    public void setImage(VerifyCodeImage image)
    {
        this.image = image;
    }

    public int getVerifyCodeCount()
    {
        return verifyCodeCount;
    }

    public void setVerifyCodeCount(int verifyCodeCount)
    {
        this.verifyCodeCount = verifyCodeCount;
    }

    public String getInput()
    {
        return input;
    }

    public void setInput(String input)
    {
        this.input = input;
    }

    class VerifyCodeImage
    {

        protected String code = "";
        /**
         * 验证码图片的宽度
         */
        private int width = 60;
        /**
         * 验证码图片的高度
         */
        private int height = 20;
        /**
         * 验证码字符个数
         */
        private int codeCount = 4;
        /**
         * xx
         */
        private int xx = 0;
        /**
         * 字体高度
         */
        private int fontHeight;
        /**
         * codeY
         */
        private int codeY;
        /**
         * codeSequence
         */
        char[] codeSequence =
        {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
            'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'
        };
        private String fileLocation = "";

        /**
         * 初始化验证图片属性
         */
        public void init(int codeCount)
        {
            // 从web.xml中获取初始信息

            // 字符个数
            this.codeCount = codeCount;
            PathUtil pu = new PathUtil();
            fileLocation = pu.getVerifyCodePath();
            xx = width / (this.codeCount + 2);
            fontHeight = height - 2;
            codeY = height - 4;
        }

        /**
         * 生成Image
         */
        public BufferedImage createImage()
        {
            // 定义图像buffer
            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D gd = buffImg.createGraphics();

            // 创建一个随机数生成器类
            Random random = new Random();

            // 将图像填充为白色
            gd.setColor(Color.WHITE);
            gd.fillRect(0, 0, width, height);

            // 创建字体，字体的大小应该根据图片的高度来定。
            Font font = new Font("Fixedsys", Font.BOLD | Font.ITALIC, fontHeight);
            // 设置字体。
            gd.setFont(font);

            // 画边框。
            gd.setColor(Color.BLACK);
            gd.drawRect(0, 0, width - 1, height - 1);

            // 随机产生60条干扰线，使图象中的认证码不易被其它程序探测到。
            gd.setColor(Color.BLACK);
            for (int i = 0; i < 60; i++)
            {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                gd.drawLine(x, y, x + xl, y + yl);
            }

            // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
            StringBuilder randomCode = new StringBuilder();
            int red = 0, green = 0, blue = 0;

            // 随机产生codeCount数字的验证码。
            for (int i = 0; i < codeCount; i++)
            {
                // 得到随机产生的验证码数字。
                String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
                // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
                red = random.nextInt(255);
                green = random.nextInt(255);
                blue = random.nextInt(255);

                // 用随机产生的颜色将验证码绘制到图像中。
                gd.setColor(new Color(red, green, blue));
                gd.drawString(strRand, (i + 1) * xx, codeY);

                // 将产生的四个随机数组合在一起。
                randomCode.append(strRand);
            }
            // 将四位数字的验证码保存。
            code = randomCode.toString().trim();

            return buffImg;
        }
    }
}
