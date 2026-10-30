package edu.gdlgxy.pctrade.service;

import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.entity.Product;
import java.util.List;

public interface ProductService {
    Result<String> publish(Product product,Long sellerId);
//    Result<List<Product>> getList(int page,int size);

    //根据 ID 查询商品详情
    Product getById(Long id);
    // 获取商品列表（支持分类和关键字搜索）
    Result<List<Product>> getList(int page, int size, String category, String keyword);
}
