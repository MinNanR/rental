package site.minnan.rental.infrastructure.utils;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.font.FontProvider;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import site.minnan.rental.application.service.BillService;
import site.minnan.rental.domain.entity.BillDetails;
import site.minnan.rental.domain.vo.UtilityPrice;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class ReceiptUtils {

    @Autowired
    private BillService billService;

    @Value("${aliyun.bucketName}")
    private String bucketName;

    @Autowired
    private OSS oss;

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
        return templateEngine.process("thymeleaf/receipt", context);
    }

    /**
     * 将html转换成pdf
     *
     * @param html
     * @param outputStream
     * @throws IOException
     */
    private File transferHtmlToPdf(String html, OutputStream outputStream) throws IOException {
        ConverterProperties converterProperties = new ConverterProperties();
        FontProvider fontProvider = new FontProvider();
        fontProvider.addFont(fontProvider.getClass().getClassLoader().getResource("font/simhei.ttf").getPath());
        fontProvider.addStandardPdfFonts();
        converterProperties.setFontProvider(fontProvider);
        converterProperties.setCharset("utf-8");
        File temp = File.createTempFile("temp", ".pdf");
        Document document = new Document(new PdfDocument(new PdfWriter(temp)), PageSize.A4);
        document.setMargins(10, 0, 10, 0);
        List<IElement> iElements = HtmlConverter.convertToElements(html, converterProperties);
        iElements.forEach(iElement -> document.add((IBlockElement) iElement));
        document.close();
        return temp;
//        RenderingProperties
    }

    /**
     * 将pdf转换成图片
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
        File file = transferHtmlToPdf(html, pipedOutputStream);
        savePdfAsImageToOss(new FileInputStream(file), bill.getId());
    }


}
