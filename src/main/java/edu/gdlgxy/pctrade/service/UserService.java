package edu.gdlgxy.pctrade.service;
import edu.gdlgxy.pctrade.common.Result;

import edu.gdlgxy.pctrade.dto.UserLoginDTO;

import edu.gdlgxy.pctrade.dto.UserRegisterDTO;
import edu.gdlgxy.pctrade.entity.User;


public interface UserService {
    Result<String> register(UserRegisterDTO userRegisterDTO);
    Result<User> login(UserLoginDTO dto);//登录成功返回用户信息
    User getById(Long id);
}
