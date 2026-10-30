package edu.gdlgxy.pctrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.gdlgxy.pctrade.common.Result;
import edu.gdlgxy.pctrade.dto.UserLoginDTO;
import edu.gdlgxy.pctrade.dto.UserRegisterDTO;
import edu.gdlgxy.pctrade.entity.User;
import edu.gdlgxy.pctrade.mapper.UserMapper;
import edu.gdlgxy.pctrade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service//这是业务层组件
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    //创建 BCrypt 加密器
    private final BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    @Override
    public Result<String> register(UserRegisterDTO dto){
        //1. 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,dto.getUsername());
        if(userMapper.selectCount(wrapper)>0){
            return Result.error("用户名已存在");
        }

        // 2. 构建 User 对象并加密密码
        User user=new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname()==null? "装机爱好者":dto.getNickname());
        user.setCredit(100);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        //3. 存入数据库
        userMapper.insert(user);
        return Result.success("注册成功");
    }

    public Result<User> login(UserLoginDTO dto){
        //1. 检查用户名是否存在
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,dto.getUsername());
        User user=userMapper.selectOne(wrapper);

        //2. 检查用户是否存在
        if(user==null){
            return Result.error("用户不存在");
        }

        //3.校验密码（BCrypt 的 matches 方法，把明文和密文比对）
        if(!passwordEncoder.matches(dto.getPassword(),user.getPassword())){
            return Result.error("用户名或密码错误");
        }

        //4.判断账号是否被禁用
        if(user.getStatus()==0){
            return Result.error("账号已被禁用");
        }

        //5.登录成功，返回用户信息（注意：别忘了把密码置空，保护隐私！）
        user.setPassword(null);
        return Result.success(user);

    }
    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

}

