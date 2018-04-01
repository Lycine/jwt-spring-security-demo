package org.zerhusen.security.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerhusen.security.JwtTokenUtil;
import org.zerhusen.security.JwtUser;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

@RestController
public class UserRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private Producer captchaProducer;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        return user;
    }

    @RequestMapping(value = "/persons/getKaptchaImage", method = RequestMethod.GET)
    public String getKaptchaImage(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String capText = captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

        System.out.println("capText: " + capText);
        BufferedImage bi = captchaProducer.createImage(capText);
        String imageInBase64 = getImageBinary(bi);
        System.out.println("imageInBase64: " + imageInBase64);
        String imgTag = "<img src=\"data:image/png;base64," + imageInBase64 + "\"/>";
        return imgTag;
    }

    @RequestMapping(value = "/persons/verifyKaptchaImage/{incomeCapText}", method = RequestMethod.GET)
    public String verifyKaptcha(HttpServletRequest request, @PathVariable("incomeCapText")String incomeCapText) throws Exception {
        HttpSession session = request.getSession();
        String realCapText = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        System.out.println("realCapText: " + realCapText);
        System.out.println("incomeCapText: " + incomeCapText);
        if (StringUtils.equalsIgnoreCase(realCapText,incomeCapText)){
            return "true";
        } else {
            return "false";
        }
    }


    static String getImageBinary(BufferedImage bi) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", baos);
            byte[] bytes = baos.toByteArray();
            byte[] enbytes = Base64.encodeBase64Chunked(bytes);
            return new String(enbytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public DefaultKaptcha captchaProducer(){
        DefaultKaptcha captchaProducer =new DefaultKaptcha();
        Properties properties =new Properties();
        properties.setProperty("kaptcha.border","no");
        properties.setProperty("kaptcha.border.color","105,179,90");

        properties.setProperty("kaptcha.textproducer.font.color","46,146,247");
        properties.setProperty("kaptcha.image.width","180");
        properties.setProperty("kaptcha.image.height","50");
        properties.setProperty("kaptcha.textproducer.char.space","3");

//        properties.setProperty("kaptcha.noise.color","51,102,255");
        properties.setProperty("kaptcha.noise.color","46,146,247");
        properties.setProperty("kaptcha.textproducer.font.size","40");
        properties.setProperty("kaptcha.session.key","code");
        properties.setProperty("kaptcha.textproducer.char.length","4");
//        properties.setProperty("kaptcha.textproducer.font.names","宋体,楷体,微软雅黑");
        properties.setProperty("kaptcha.textproducer.font.names","Tahoma");
        properties.setProperty("kaptcha.obscurificator.impl","com.google.code.kaptcha.impl.WaterRipple");
        properties.setProperty("kaptcha.textproducer.char.string","abcdefghkmnprstuvwxyABCEFGHKMNPRSTUVWXY1345678");
        properties.setProperty("kaptcha.background.clear.from","white");
        properties.setProperty("kaptcha.background.clear.to","white");



        Config config=new Config(properties);
        captchaProducer.setConfig(config);
        return  captchaProducer;
    }
}
