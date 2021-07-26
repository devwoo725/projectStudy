package com.devwoo.projectstudy;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlCanvas;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLImageElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPhraseElement;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.layout.font.FontProvider;
import com.openhtmltopdf.bidi.support.ICUBidiReorderer;
import com.openhtmltopdf.bidi.support.ICUBidiSplitter;
import com.openhtmltopdf.extend.SVGDrawer;
import com.openhtmltopdf.latexsupport.LaTeXDOMMutator;
import com.openhtmltopdf.mathmlsupport.MathMLDrawer;
import com.openhtmltopdf.objects.StandardObjectDrawerFactory;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder.PdfAConformance;
import com.openhtmltopdf.render.DefaultObjectDrawerFactory;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.pdfbox.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** @author 우두홍(2021 - 07 - 16) */
public class HtmlToPdfTest {
  private String sampleResourcesPath = "src/main/resources/sample";
  private String resultResourcesPath = "src/main/resources/result";

  @Test
  void test1() throws IOException {
    Document jsoup =
        Jsoup.parse(new File(sampleResourcesPath + "/htmltopdf_simple_jquery_ready.html"), "UTF-8");
    Files.write(
        Paths.get(resultResourcesPath + "/changeTest_jsoup.html"),
        jsoup.outerHtml().getBytes("UTF-8"));
  }

  @Test
  void test2() throws IOException {
    try (final WebClient webClient = new WebClient()) {
      final HtmlPage page =
          webClient.getPage(
              new File(sampleResourcesPath + "/htmltopdf_simple_jquery_ready.html")
                  .toURI()
                  .toURL());
      String pageAsXml = page.asXml();
      Files.write(
          Paths.get(resultResourcesPath + "/changeTest_htmlunit.html"),
          pageAsXml.getBytes("UTF-8"));
    }
  }

  @Test
  void test1_1() throws IOException {
    Document jsoup =
        Jsoup.parse(new File(sampleResourcesPath + "/convert/sample_division.html"), "UTF-8");
    Files.write(
        Paths.get(resultResourcesPath + "/convert_sampler_jsoup.html"),
        jsoup.outerHtml().getBytes("UTF-8"));
  }

  @Test
  void test2_1() throws IOException {
    try (final WebClient webClient = new WebClient()) {
      final HtmlPage page =
          webClient.getPage(
              new File(sampleResourcesPath + "/convert/sample_division.html").toURI().toURL());
      final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
      jobManager.waitForJobs(50_000);
      String pageAsXml = page.asXml();
      Files.write(
          Paths.get(resultResourcesPath + "/convert_sampler_htmlunit_division.html"),
          pageAsXml.getBytes("UTF-8"));
    }
  }

  @Test
  void test2_2() throws IOException, InterruptedException {
    try (final WebClient webClient = new WebClient()) {
      final HtmlPage page = webClient.getPage(new URL("http://localhost:8080/pdf/sampleHtml"));
      final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
      jobManager.waitForJobs(50_000);
      //      page.executeJavaScript("convertCanvasToImg(\"irregularPulseBurdenChart\", \"\",
      // $(\"#irregularPulseBurdenChart\").attr(\"style\"))");

      String pageAsXml = page.asXml();
      //      HtmlImage imageElement = (HtmlImage)
      // page.getByXPath("//*[@id=\"irregularPulseBurdenChartImg\"]").get(0);
      //      HtmlCanvas htmlCanvas =(HtmlCanvas)
      // page.getByXPath("//*[@id=\"irregularPulseBurdenChart\"]/div/canvas[1]").get(0);

      //      html/body/div[8]/div[1]/div[4]/img
      //      System.out.println(imageElement.getSrc());
      //      System.out.println(htmlCanvas.toDataURL("image/png"));
      Files.write(
          Paths.get(resultResourcesPath + "/convert_sampler_htmlunit_division_test2.html"),
          pageAsXml.getBytes("UTF-8"));
    }
  }

  @Test
  void test2_3() throws IOException, InterruptedException {
    List<String> alerts = new ArrayList<>();

    try (final WebClient webClient = new WebClient()) {
      final HtmlPage page = webClient.getPage(new URL("http://localhost:8080/pdf/sampleHtml"));
      webClient.setAlertHandler(new CollectingAlertHandler(alerts));
      final JavaScriptJobManager jobManager = page.getEnclosingWindow().getJobManager();
      jobManager.waitForJobs(50_000);
      //      page.executeJavaScript("convertCanvasToImg(\"irregularPulseBurdenChart\", \"\",
      // $(\"#irregularPulseBurdenChart\").attr(\"style\"))");

      System.out.println(alerts.get(0));
      String pageAsXml = page.asXml();
      //      HtmlImage imageElement = (HtmlImage)
      // page.getByXPath("//*[@id=\"irregularPulseBurdenChartImg\"]").get(0);
      //      HtmlCanvas htmlCanvas =(HtmlCanvas)
      // page.getByXPath("//*[@id=\"irregularPulseBurdenChart\"]/div/canvas[1]").get(0);

      //      html/body/div[8]/div[1]/div[4]/img
      //      System.out.println(imageElement.getSrc());
      //      System.out.println(htmlCanvas.toDataURL("image/png"));
      Files.write(
          Paths.get(resultResourcesPath + "/convert_sampler_htmlunit_division_test2.html"),
          pageAsXml.getBytes("UTF-8"));
    }
  }

  @Test
  void test4() throws IOException {
    System.setProperty("webdriver.chrome.driver", "src/test/java/chromedriver.exe");
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.addArguments("headless");
    ChromeDriver webDriver = new ChromeDriver(chromeOptions);

    WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(10).getSeconds());

    webDriver.get("http://localhost:8080/pdf/sampleHtml");

    String html = "<html>" + webDriver.findElement(By.tagName("html")).getAttribute("innerHTML") + "</html>";

    Document jsoup = Jsoup.parse(html, "UTF-8");
    jsoup.outputSettings().syntax( Document.OutputSettings.Syntax.xml);

    jsoup.getElementsByTag("script").remove();

    Files.write(Paths.get(resultResourcesPath + "/convert_sampler_htmlunit_selenium.html"), jsoup.outerHtml().getBytes("UTF-8"));
    webDriver.close();
  }

  @Test
  void test4_1() throws IOException {
    System.setProperty("webdriver.chrome.driver", "src/test/java/chromedriver.exe");
    HtmlUnitDriver webDriver = new HtmlUnitDriver(true);
//    WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(10).getSeconds());

    webDriver.get("http://localhost:8080/pdf/sampleHtml");

    String html = "<html>" + webDriver.findElement(By.tagName("html")).getAttribute("innerHTML") + "</html>";

    Document jsoup = Jsoup.parse(html, "UTF-8");
    jsoup.outputSettings().syntax( Document.OutputSettings.Syntax.xml);

    jsoup.getElementsByTag("script").remove();

    Files.write(Paths.get(resultResourcesPath + "/convert_sampler_htmlunit_selenium.html"), jsoup.outerHtml().getBytes("UTF-8"));
    webDriver.close();
  }

  @Test
  void test4_2() throws IOException {
    System.setProperty("phantomjs.binary.path", "src/test/java/phantomjs.exe");
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setJavascriptEnabled(true);
    PhantomJSDriver webDriver = new PhantomJSDriver(caps);
//    WebDriverWait webDriverWait = new WebDriverWait(webDriver, Duration.ofSeconds(10).getSeconds());

    webDriver.get("http://localhost:8080/pdf/sampleHtml");

    String html = "<html>" + webDriver.findElement(By.tagName("html")).getAttribute("innerHTML") + "</html>";

    Document jsoup = Jsoup.parse(html, "UTF-8");
    jsoup.outputSettings().syntax( Document.OutputSettings.Syntax.xml);

    jsoup.getElementsByTag("script").remove();

    Files.write(Paths.get(resultResourcesPath + "/convert_sampler_htmlunit_selenium.html"), jsoup.outerHtml().getBytes("UTF-8"));
    webDriver.quit();
  }

  @Test
  void test3() throws IOException, ParserConfigurationException, SAXException {
    try (InputStream is =
        new FileInputStream(resultResourcesPath + "/convert_sampler_htmlunit_selenium.html")) {
      byte[] htmlBytes = IOUtils.toByteArray(is);
      String html = new String(htmlBytes, StandardCharsets.UTF_8);
      String testCaseOutputFile = resultResourcesPath + "/pdf/convert_sampler_htmlunit_selenium.pdf";

      try (FileOutputStream outputStream = new FileOutputStream(testCaseOutputFile)) {
        renderPDF(html, PdfAConformance.NONE, outputStream);
//        renderPDF_2(html, outputStream);
      }
    }
  }

  private static void renderPDF(String html, PdfAConformance pdfaConformance, OutputStream outputStream) throws IOException, ParserConfigurationException, SAXException {
    PdfRendererBuilder builder = new PdfRendererBuilder();

    builder.useUnicodeBidiSplitter(new ICUBidiSplitter.ICUBidiSplitterFactory());
    builder.useUnicodeBidiReorderer(new ICUBidiReorderer());
    builder.defaultTextDirection(BaseRendererBuilder.TextDirection.LTR);
    builder.addDOMMutator(LaTeXDOMMutator.INSTANCE);
    builder.usePdfAConformance(pdfaConformance);
    builder.withHtmlContent(html, "");
    builder.useFont(new File("resources/font/tway_air.ttf"), "Noto");
    builder.toStream(outputStream);
    builder.run();
  }

  private void renderPDF_2(String html, OutputStream outputStream) throws IOException, ParserConfigurationException, SAXException {
    try (FileOutputStream os = new FileOutputStream(resultResourcesPath + "/pdf/convert_sampler_htmlunit_selenium.pdf")) {
      PdfRendererBuilder builder = new PdfRendererBuilder();
      builder.useFastMode();
      builder.testMode(true);
      builder.usePdfUaAccessbility(true);
      builder.useFont(new File("resources/font/tway_air.ttf"), "TestFont");
      builder.withHtmlContent(html, "");
      builder.toStream(os);
      builder.run();
    }
  }

  @Test
  void test5() throws IOException {
    File htmlSource = new File(resultResourcesPath + "/convert_sampler_htmlunit_selenium.html");
    File pdfDest = new File(resultResourcesPath + "/pdf/convert_sampler_htmlunit_selenium.pdf");
    ConverterProperties properties = new ConverterProperties();
    properties.setFontProvider( new DefaultFontProvider(true, true, true));
    HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), properties);
  }
  @Test
  void test5_1() throws IOException {
    File htmlSource = new File(resultResourcesPath + "/convert_sampler_htmlunit_selenium.html");
    File pdfDest = new File(resultResourcesPath + "/pdf/convert_sampler_htmlunit_selenium_1.pdf");
    ConverterProperties properties = new ConverterProperties();
    FontProvider fontProvider = new DefaultFontProvider();
    FontProgram fontProgram = FontProgramFactory.createFont("src/main/resources/static/font/NanumBarunGothic.ttf");
    fontProvider.addFont(fontProgram);
    properties.setFontProvider(fontProvider);
    HtmlConverter.convertToPdf(new FileInputStream(htmlSource), new FileOutputStream(pdfDest), properties);
  }
}
