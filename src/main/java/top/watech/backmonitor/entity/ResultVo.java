package top.watech.backmonitor.entity;

import top.watech.backmonitor.enums.ResultEnum;

/**
 * http请求统一的返回类
 */
public class ResultVo {


    /**返回状态码*/
    private String code;
    /**返回的信息*/
    private String msg;
    /**返回的数据*/
    private Object data;

    public ResultVo() {
    }

    public ResultVo(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVo(ResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
