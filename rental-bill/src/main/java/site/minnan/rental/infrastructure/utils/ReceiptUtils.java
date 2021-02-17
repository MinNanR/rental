package site.minnan.rental.infrastructure.utils;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.TrueTypeFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.font.FontProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import site.minnan.rental.application.service.BillService;
import site.minnan.rental.domain.entity.BillDetails;
import site.minnan.rental.domain.vo.UtilityPrice;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

@Component
@Slf4j
public class ReceiptUtils {

    @Autowired
    private BillService billService;

    @Value("${aliyun.baseUrl}")
    private String baseUrl;

    @Value("${aliyun.bucketName}")
    private String bucketName;

    @Autowired
    @Qualifier("simhei")
    private FontProgram fontProgram;

    @Autowired
    private OSS oss;

    private final static Map<Integer, String> numberChinesMap;

    private final static String[] emptyNumber = new String[]{"", "", "", "", "", "", ""};

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

    /**
     * 将数组拆分成数组
     *
     * @param number
     * @return
     */
    private static String[] splitNumber(int number) {
        if (number <= 0) {
            return emptyNumber;
        }
        String[] numberChars = new String[7];
        Iterator<Integer> iterator = CollectionUtil.newArrayList(2, 3, 4, 5, 6).iterator();
        numberChars[0] = "0";
        numberChars[1] = "0";
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

    /**
     * 解析成模板
     *
     * @param bill
     * @return
     */
    private String parseToHTML(BillDetails bill) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        switch (bill.getType()) {
            case MONTHLY:
                monthly(context, bill);
                break;
            case CHECK_IN:
                checkIn(context, bill);
                break;
        }
        return templateEngine.process("thymeleaf/receipt", context);
    }

    /**
     * 将html转换成pdf
     *
     * @param html
     * @throws IOException
     */
    private File transferHtmlToPdf(String html) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        FontProvider fontProvider = new FontProvider();
        fontProvider.addStandardPdfFonts();
        fontProvider.addFont(fontProgram);
        converterProperties.setFontProvider(fontProvider);
        converterProperties.setCharset("utf-8");
        File temp = File.createTempFile("temp", ".pdf");
        Document document = new Document(new PdfDocument(new PdfWriter(temp)), PageSize.A4);
        document.setMargins(10, 0, 10, 0);
        List<IElement> iElements = HtmlConverter.convertToElements(html, converterProperties);
        iElements.forEach(iElement -> document.add((IBlockElement) iElement));
        document.close();
        return temp;
    }


    /**
     * 将pdf转换成图片，并保存到阿里云oss
     *
     * @param documentStream pdf输入流流
     * @throws IOException
     */
    private void savePdfAsImageToOss(InputStream documentStream, Integer id) throws IOException {
        PDDocument document = PDDocument.load(documentStream);
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage bufferedImage = renderer.renderImageWithDPI(0, 200, ImageType.RGB);
        String ossKey = StrUtil.format("receipt/{}.png", id);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", os);
        InputStream imageStream = new ByteArrayInputStream(os.toByteArray());
        oss.putObject(bucketName, ossKey, imageStream);
        document.close();
    }

    /**
     * 生成收据图片
     *
     * @param bill 账单详情
     */
    public void generateReceipt(BillDetails bill) throws IOException {
        String html = parseToHTML(bill);
        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream pipedOutputStream = new PipedOutputStream();
        pipedInputStream.connect(pipedOutputStream);
        File file = transferHtmlToPdf(html);
        savePdfAsImageToOss(new FileInputStream(file), bill.getId());
        bill.setReceiptUrl(StrUtil.format("{}/receipt/{}.png", baseUrl, bill.getId()));
        log.info("生成账单成功，id={}", bill.getId());
    }

    private void checkIn(Context context, BillDetails bill) {
        UtilityPrice price = billService.getUtilityPrice();
        //抬头
        context.setVariable("no", StrUtil.fillBefore(bill.getId().toString(), '0', 7));
        context.setVariable("houseName", bill.getHouseName());
        context.setVariable("roomNumber", bill.getRoomNumber());
        context.setVariable("startDate", DateUtil.format(bill.getCreateTime(), "yyyy年M月d日"));
        //水费
        context.setVariable("waterStart", bill.getWaterStart().intValue());
        context.setVariable("waterEnd", "");
        context.setVariable("waterUsage", "");
        context.setVariable("waterPrice", "");
        context.setVariable("waterCharge", emptyNumber);
        //电费
        context.setVariable("electricityStart", bill.getElectricityStart().intValue());
        context.setVariable("electricityEnd", "");
        context.setVariable("electricityUsage", "");
        context.setVariable("electricityPrice", "");
        context.setVariable("electricityCharge", emptyNumber);
        //门禁卡
        Integer accessCardPrice = price.getAccessCardPrice(bill.getHouseName());
        context.setVariable("accessCardQuantity", bill.getAccessCardQuantity());
        context.setVariable("accessCardPrice", accessCardPrice);
        String[] accessCardCharge = splitNumber(bill.getAccessCardCharge());
        context.setVariable("accessCardCharge", accessCardCharge);
        //押金
        String[] deposit = splitNumber(bill.getDeposit());
        context.setVariable("deposit", deposit);
        //房租
        String[] rent = splitNumber(bill.getRent());
        context.setVariable("rent", rent);
        //总额
        BigDecimal totalCharge = bill.totalCharge();
        String[] total = parseNumberToChinese(totalCharge.intValue());
        context.setVariable("total", total);
        context.setVariable("totalCharge", totalCharge);
        //备注
        context.setVariable("remark", "备注：" + Optional.ofNullable(bill.getRemark()).orElse(""));
    }

    private void monthly(Context context, BillDetails bill) {
        UtilityPrice price = billService.getUtilityPrice();
        //抬头
        context.setVariable("no", StrUtil.fillBefore(bill.getId().toString(), '0', 7));
        context.setVariable("houseName", bill.getHouseName());
        context.setVariable("roomNumber", bill.getRoomNumber());
        context.setVariable("startDate", DateUtil.format(bill.getCreateTime(), "yyyy年M月d日"));
        context.setVariable("endDate", "至" + DateUtil.format(bill.getCompletedDate(), "yyyy年M月d日"));
        //水费
        context.setVariable("waterStart", bill.getWaterStart().intValue());
        context.setVariable("waterEnd", bill.getWaterEnd().intValue());
        context.setVariable("waterUsage", bill.getWaterUsage());
        context.setVariable("waterPrice", price.getWaterPrice());
        String[] waterCharge = splitNumber(bill.getWaterCharge().intValue());
        context.setVariable("waterCharge", waterCharge);
        //电费
        context.setVariable("electricityStart", bill.getElectricityStart().intValue());
        context.setVariable("electricityEnd", bill.getElectricityEnd().intValue());
        context.setVariable("electricityUsage", bill.getElectricityUsage());
        context.setVariable("electricityPrice", price.getElectricityPrice());
        String[] electricityCharge = splitNumber(bill.getElectricityCharge().intValue());
        context.setVariable("electricityCharge", electricityCharge);
        //门禁卡
        context.setVariable("accessCardQuantity", "");
        context.setVariable("accessCardPrice", "");
        context.setVariable("accessCardCharge", emptyNumber);
        //押金
        context.setVariable("deposit", emptyNumber);
        //房租
        String[] rent = splitNumber(bill.getRent());
        context.setVariable("rent", rent);
        //总额
        BigDecimal totalCharge = bill.totalCharge();
        String[] total = parseNumberToChinese(totalCharge.intValue());
        context.setVariable("total", total);
        context.setVariable("totalCharge", totalCharge);
        //备注
        context.setVariable("remark", "备注：" + Optional.ofNullable(bill.getRemark()).orElse(""));
    }
}
