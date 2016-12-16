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
         * ��֤��ͼƬ�Ŀ��
         */
        private int width = 60;
        /**
         * ��֤��ͼƬ�ĸ߶�
         */
        private int height = 20;
        /**
         * ��֤���ַ�����
         */
        private int codeCount = 4;
        /**
         * xx
         */
        private int xx = 0;
        /**
         * ����߶�
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
         * ��ʼ����֤ͼƬ����
         */
        public void init(int codeCount)
        {
            // ��web.xml�л�ȡ��ʼ��Ϣ

            // �ַ�����
            this.codeCount = codeCount;
            PathUtil pu = new PathUtil();
            fileLocation = pu.getVerifyCodePath();
            xx = width / (this.codeCount + 2);
            fontHeight = height - 2;
            codeY = height - 4;
        }

        /**
         * ����Image
         */
        public BufferedImage createImage()
        {
            // ����ͼ��buffer
            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D gd = buffImg.createGraphics();

            // ����һ���������������
            Random random = new Random();

            // ��ͼ�����Ϊ��ɫ
            gd.setColor(Color.WHITE);
            gd.fillRect(0, 0, width, height);

            // �������壬����Ĵ�СӦ�ø���ͼƬ�ĸ߶�������
            Font font = new Font("Fixedsys", Font.BOLD | Font.ITALIC, fontHeight);
            // �������塣
            gd.setFont(font);

            // ���߿�
            gd.setColor(Color.BLACK);
            gd.drawRect(0, 0, width - 1, height - 1);

            // �������60�������ߣ�ʹͼ���е���֤�벻�ױ���������̽�⵽��
            gd.setColor(Color.BLACK);
            for (int i = 0; i < 60; i++)
            {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                gd.drawLine(x, y, x + xl, y + yl);
            }

            // randomCode���ڱ��������������֤�룬�Ա��û���¼�������֤��
            StringBuilder randomCode = new StringBuilder();
            int red = 0, green = 0, blue = 0;

            // �������codeCount���ֵ���֤�롣
            for (int i = 0; i < codeCount; i++)
            {
                // �õ������������֤�����֡�
                String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
                // �����������ɫ������������ɫֵ�����������ÿλ���ֵ���ɫֵ������ͬ��
                red = random.nextInt(255);
                green = random.nextInt(255);
                blue = random.nextInt(255);

                // �������������ɫ����֤����Ƶ�ͼ���С�
                gd.setColor(new Color(red, green, blue));
                gd.drawString(strRand, (i + 1) * xx, codeY);

                // ���������ĸ�����������һ��
                randomCode.append(strRand);
            }
            // ����λ���ֵ���֤�뱣�档
            code = randomCode.toString().trim();

            return buffImg;
        }
    }
}
