package com.devwoo.projectstudy.pdf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 우두홍(2021 - 07 - 20)
 */
@Controller
public class PdfController {
    @GetMapping("/pdf/sampleHtml")
    public String loadSampleHtml() {
        return "pdf/sample";
    }
}
