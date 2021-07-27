package com.devwoo.projectstudy;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.layout.font.FontProvider;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.util.StringUtils;

/**
 * @author 우두홍(2021 - 07 - 27)
 */
public class HtmlToPdfSummaryTest {

    private static final String SAMPLE_RESOURCES_PATH = "src/main/resources/sample";
    private static final String RESULT_RESOURCES_PATH = "src/main/resources/result";
    private static final String CHROME_DRIVER_PATH = "src/test/java/chromedriver.exe";

    @BeforeAll
    static void setUp() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
    }

    @Test
    @DisplayName("html to pdf 테스트 - html 소스[url 요청 결과값]")
    void test1() throws IOException {
        convertHtmlToPdf(
            preprocessHtml(
                getHtml("http://localhost:8080/html/sample")
            )
        );
    }

    @Test
    @DisplayName("html to pdf 테스트 - html 소스[html 파일]")
    void test1_1() throws IOException {
        convertHtmlToPdf(
            preprocessHtml(
                getHtml(Paths.get(SAMPLE_RESOURCES_PATH + "/convert/sample_division.html"))
            )
            , Paths.get(RESULT_RESOURCES_PATH + "/pdf/sample_from_file_font_tway.pdf")
            , "src/main/resources/static/font/tway_air.ttf"
        );
    }

    /**
     * PDF 변환 전 Html 전처리 - pdf 변환 할 경우 xml 형태가 필요해서 syntax.xml 설정 - 해당 설정을 할 경우 닫혀 있지 않은 태그도 전부 닫아 준다. ex)<br> -> <br/> - script tag 삭제(스크립트가 수행된 결과물을 html 받아왔기 때문에 script 내용은 필요없다)
     *
     * @param html
     * @return
     */
    private String preprocessHtml(String html) {
        Document jsoup = Jsoup.parse(html, StandardCharsets.UTF_8.name());
        jsoup.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        jsoup.getElementsByTag("script").remove();
        return jsoup.outerHtml();
    }

    /**
     * html을 가지고 온다. - chrome driver를 사용했고 로컬 환경은 윈도우기 때문에 .exe 파일을 수행하도록 셋팅 - 서버에 배포 할 경우는 리눅스용 실행 파일을 셋팅해주어야 한다. - System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
     *
     * @param url
     * @return
     */
    private String getHtml(String url) {
        ChromeDriver webDriver = null;
        try {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("headless");
            webDriver = new ChromeDriver(chromeOptions);

            webDriver.get(url);

            return webDriver.findElement(By.tagName("html")).getAttribute("outerHTML");
        } finally {
            if (webDriver != null) {
                webDriver.close();
            }
        }
    }

    private String getHtml(Path path) {
        return getHtml(path.toUri().toString());
    }

    private void convertHtmlToPdf(String html) throws IOException {
        convertHtmlToPdf(html, Paths.get(RESULT_RESOURCES_PATH + "/pdf/sample.pdf"), "");
    }

    /**
     * html을 pdf로 변환한다.
     *
     * @param html
     * @param pdfPath
     * @param fontPath
     * @throws IOException
     */
    private void convertHtmlToPdf(String html, Path pdfPath, String fontPath) throws IOException {
        HtmlConverter.convertToPdf(new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8)), new FileOutputStream(pdfPath.toFile()), getConverterProperties(fontPath));
    }

    private ConverterProperties getConverterProperties(String fontPath) throws IOException {
        FontProvider fontProvider;

        if (StringUtils.hasText(fontPath)) {
            fontProvider = new DefaultFontProvider();
            fontProvider.addFont(FontProgramFactory.createFont(fontPath));
        } else {
            fontProvider = new DefaultFontProvider(true, true, true);
        }

        return new ConverterProperties().setFontProvider(fontProvider);
    }
}
