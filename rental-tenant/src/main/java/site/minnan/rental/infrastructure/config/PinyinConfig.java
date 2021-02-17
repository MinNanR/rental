package site.minnan.rental.infrastructure.config;

import cn.hutool.extra.pinyin.PinyinEngine;
import cn.hutool.extra.pinyin.engine.PinyinFactory;
import cn.hutool.extra.pinyin.engine.pinyin4j.Pinyin4jEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class PinyinConfig {

    @Bean
    public PinyinEngine pinyinEngine(){
        return new Pinyin4jEngine();
    }
}
