package site.minnan.rental.infrastructure.utils;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapBuilder;
import io.lettuce.core.RedisURI;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import site.minnan.rental.application.service.BillService;
import site.minnan.rental.domain.aggregate.Bill;
import site.minnan.rental.domain.entity.BillDetails;
import site.minnan.rental.domain.mapper.BillMapper;
import site.minnan.rental.domain.vo.UtilityPrice;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReceiptUtils {

    @Autowired
    private BillService billService;

    private final static Map<Integer, String> numberChinesMap;

    static {
        numberChinesMap = new HashMap<>();
        numberChinesMap.put(0, "零");
        numberChinesMap.put(1, "壹");
        numberChinesMap.put(2, "贰");
        numberChinesMap.put(3, "叁");
        numberChinesMap.put(4, "肆");
        numberChinesMap.put(5, "伍");
        numberChinesMap.put(6, "陆");
        numberChinesMap.put(7, "柒");
        numberChinesMap.put(8, "捌");
        numberChinesMap.put(9, "玖");
    }

    public String parse(BillDetails bill) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        UtilityPrice price = billService.getUtilityPrice();

        Context context = new Context();
        context.setVariable("roomNumber", bill.getRoomNumber());
        context.setVariable("startDate", DateUtil.format(bill.getCreateTime(), "yyyy年M月d日"));
        context.setVariable("endDate", DateUtil.format(bill.getCompletedDate(), "yyyy年M月d日"));

        context.setVariable("waterStart", bill.getWaterStart().intValue());
        context.setVariable("waterEnd", bill.getWaterEnd().intValue());
        context.setVariable("waterUsage", bill.getWaterUsage().intValue());
        context.setVariable("waterPrice", price.getWaterPrice().intValue());
        String[] waterCharge = splitNumber(bill.getWaterCharge().intValue());
        context.setVariable("waterCharge", waterCharge);

        context.setVariable("electricityStart", bill.getElectricityStart().intValue());
        context.setVariable("electricityEnd", bill.getElectricityEnd().intValue());
        context.setVariable("electricityUsage", bill.getElectricityUsage().intValue());
        context.setVariable("electricityPrice", price.getElectricityPrice().intValue());
        String[] electricityCharge = splitNumber(bill.getElectricityCharge().intValue());
        context.setVariable("electricityCharge", electricityCharge);

        String[] rent = splitNumber(bill.getRent());
        context.setVariable("rent", rent);
        BigDecimal totalCharge = bill.totalCharge();
        String[] total = parseNumberToChinese(totalCharge.intValue());
        context.setVariable("total", total);
        context.setVariable("totalCharge", totalCharge);
//        context.setVariable("to", "123");
        return templateEngine.process("thymeleaf/test", context);
    }

    /**
     * 将数字转换成大写的中文
     *
     * @param number 要转换的数字
     * @return 结果数组，第一个用人民币符号，前面用空格补充
     */
    private static String[] parseNumberToChinese(int number) {
        String[] numberChars = new String[5];
        Iterator<Integer> iterator = CollectionUtil.newArrayList(0, 1, 2, 3, 4).iterator();
        while (iterator.hasNext() && number > 0) {
            int index = iterator.next();
            int n = number % 10;
            String chineseChar = numberChinesMap.get(n);
            numberChars[index] = chineseChar;
            number = number / 10;
        }
        if (iterator.hasNext()) {
            Integer index = iterator.next();
            numberChars[index] = "￥";
            iterator.forEachRemaining(i -> numberChars[i] = "");
        }
        return numberChars;
    }

    private static String[] splitNumber(int number) {
        String[] numberChars = new String[5];
        Iterator<Integer> iterator = CollectionUtil.newArrayList(0, 1, 2, 3, 4).iterator();
        while (iterator.hasNext() && number > 0) {
            int index = iterator.next();
            int n = number % 10;
            numberChars[index] = String.valueOf(n);
            number = number / 10;
        }
        if (iterator.hasNext()) {
            int index = iterator.next();
            numberChars[index] = "￥";
            iterator.forEachRemaining(i -> numberChars[i] = "");
        }
        return numberChars;
    }

    public static void main(String[] args) {
        String[] chars = splitNumber(1234);
        Console.log(chars);
    }
}
