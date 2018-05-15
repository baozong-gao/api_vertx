package com.shenxin.core.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.shenxin.core.api.util.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.io.IOException;
import java.util.List;

/**
 * @Author: gaobaozong
 * @Description: 对象工具类
 * @Date: Created in 2017/9/29 - 14:05
 * @Version: V1.0
 */
@Slf4j
public class BeanUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static <T> T json2Object(String json, Class<T> c) {
        try {
            return mapper.readValue(json, c);
        } catch (IOException e) {
           log.error("json 转换 {} 异常",c.getName(), e);
        }
        return null;
    }

    public static String object2Json(Object o){
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("object:[{}] 转换 string 异常",o, e);
        }
        return null;
    }

    public static void validateJSR303(Validator validator, Object o) throws ParamException{
        BindingResult result = new BeanPropertyBindingResult(o, o.getClass().getName());
        validator.validate(o, result);
        List<ObjectError> allErrors = result.getAllErrors();
        if (allErrors != null && allErrors.size() > 0) {
            String errorMessage = "";
            for (ObjectError error : allErrors) {
                errorMessage += "【" + error.getDefaultMessage() + "】";
            }
            throw new ParamException(errorMessage);
        }
    }


}
