package edu.gdlgxy.pctrade.controller;

import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Product;
import edu.gdlgxy.pctrade.entity.User;
import edu.gdlgxy.pctrade.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    // ================= 页面跳转 =================

    // 跳转到发布页面
    @GetMapping("/publish-page")
    public String publishPage() {
        return "publish";
    }

    // ================= 接口测试 (返回 JSON) =================

    @PostMapping("/publish")
    @ResponseBody // 加上这个，接口才返回 JSON
    public Result<String> publish(@RequestBody Product product, HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return Result.error("请先登录");
        }
        return productService.publish(product, loginUser.getId());
    }

    @GetMapping("/list")
    @ResponseBody // 加上这个，接口才返回 JSON
    public Result<List<Product>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword ) {
        return productService.getList(page, size, category, keyword);
    }

    // ================= 表单提交 (返回 HTML 视图) =================

    // 处理 HTML 发布表单（包含图片上传）
    @PostMapping("/publish-html")
    public String publishHtml(Product product,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              HttpSession session) throws IOException {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/user/login-page";
        }

        // 1. 处理图片上传
        if (imageFile != null && !imageFile.isEmpty()) {
            // 获取原始文件名
            String originalFilename = imageFile.getOriginalFilename();
            // 为了防止文件名冲突，用 UUID 重新生成一个名字
            String newFileName = UUID.randomUUID().toString() + "_" + originalFilename;

            // 指定保存的文件夹（你可以建在 D 盘或者项目根目录）
            String uploadDir = "D:/pc_trade_uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs(); // 文件夹不存在就创建
            }

            // 把文件保存到硬盘
            File destFile = new File(uploadDir + newFileName);
            imageFile.transferTo(destFile);

            // 将相对路径存入数据库
            product.setImageUrl("/uploads/" + newFileName);
        }

        // 2. 调用业务方法存库
        productService.publish(product, loginUser.getId());

        return "redirect:/";
    }
}