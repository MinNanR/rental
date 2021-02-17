package site.minnan.rental.infrastructure.config;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.TrueTypeFont;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class ReceiptConfig {

    @Bean(name = "simhei")
    public FontProgram  fontProgram(){
        try {
            InputStream fontStream = this.getClass().getClassLoader().getResourceAsStream("font/simhei.ttf");
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = fontStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            fontStream.close();
            return new TrueTypeFont(outSteam.toByteArray());
        } catch (IOException e) {
            log.error("加载字体失败");
            return null;
        }
    }
}
