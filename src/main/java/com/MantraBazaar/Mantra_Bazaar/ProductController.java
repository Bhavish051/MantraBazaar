package com.MantraBazaar.Mantra_Bazaar;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = { "", "product" })
public class ProductController implements ServletContextAware {

    private ServletContext servletContext;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return "product/index";
    }

    @RequestMapping(value = "process", method = RequestMethod.POST)
    public String process(@RequestParam("file") MultipartFile file) throws Exception {
        String fileName = uploadExcelFile(file);
        System.out.println("File Name: " + fileName);
        String excelPath = servletContext.getRealPath("/resources/excels/" + fileName);
        System.out.println("Excel Path: " + excelPath);
        ExcelHelper excelHelper = new ExcelHelper(excelPath);
        List<Product> products = excelHelper.readData(Product.class.getName());
        System.out.println("Product List");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Product product : products) {
            System.out.println("Id: " + product.getProductId());
            System.out.println("Name: " + product.getName());
            System.out.println("Price: " + product.getSalePriceIncTax());
            System.out.println("=========================");
        }
        return "product/index";
    }

    private String uploadExcelFile(MultipartFile multipartFile) {
        try {
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths
                    .get(servletContext.getRealPath("/resources/excels/" + multipartFile.getOriginalFilename()));
            Files.write(path, bytes);
            return multipartFile.getOriginalFilename();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}