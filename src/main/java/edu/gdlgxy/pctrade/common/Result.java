package edu.gdlgxy.pctrade.common;

//import jdk.jfr.DataAmount;
import lombok.Data;

@Data
public class Result<T> {
    private Integer code;//200成功 400失败
    private String msg;
    private T data;

    public static <T> Result<T> success(T data){
        Result<T> r=new Result<>();
        r.setCode(200);
        r.setMsg("操作成功");
        r.setData(data);
        return r;
    }

    public static <T> Result<T> error(String msg){
        Result<T> r=new Result<>();
        r.setCode(400);
        r.setMsg(msg);
        return r;
    }
}
