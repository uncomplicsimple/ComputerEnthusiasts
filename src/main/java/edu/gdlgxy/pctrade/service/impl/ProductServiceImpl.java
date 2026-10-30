package edu.gdlgxy.pctrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Product;
import edu.gdlgxy.pctrade.mapper.ProductMapper;
import edu.gdlgxy.pctrade.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Result<String> publish(Product product,Long sellerId){
        product.setSellerId(sellerId);//设置卖家ID
        product.setStatus(1);//默认在售
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        productMapper.insert(product);
        return Result.success("发布成功");
    }

    @Override
    public Result<List<Product>> getList(int page, int size, String category, String keyword) {
        Page<Product> pageParams = new Page<>(page, size);

        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        //必须是“在售”的商品
        wrapper.eq(Product::getStatus, 1);

        //如果传了分类，就查该分类
        if (category != null && !category.isEmpty()) {
            wrapper.eq(Product::getCategory, category);
        }

        //如果传了关键字，就在标题里模糊查询
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Product::getTitle, keyword);
        }

        //按时间倒序
        wrapper.orderByDesc(Product::getCreateTime);

        Page<Product> resultPage = productMapper.selectPage(pageParams, wrapper);
        return Result.success(resultPage.getRecords());
    }

//    @Override
//    public Result<List<Product>> getList(int page,int size){
//        //创建分页对象，第 page 页，每页 size 条
//        Page<Product> pageParams = new Page<>(page,size);
//
//        //查询条件：只查在售的 (status = 1)，按时间倒序排列
//        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Product::getStatus,1)
//                .orderByDesc(Product::getCreateTime);
//
//        // 执行分页查询
//        Page<Product> resultPage = productMapper.selectPage(pageParams,wrapper);
//
//        //返回数据列表
//        return Result.success(resultPage.getRecords());
//    }

    @Override
    public Product getById(Long id){
        return productMapper.selectById(id);
    }


}
