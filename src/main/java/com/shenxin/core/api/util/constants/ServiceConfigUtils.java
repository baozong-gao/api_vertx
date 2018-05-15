package com.shenxin.core.api.util.constants;

import com.shenxin.core.api.pojo.ServiceConfig;
import com.shenxin.core.api.util.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import javax.inject.Named;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: gaobaozong
 * @Description: 服务配置工具类
 * @Date: Created in 2017/9/19 - 16:05
 * @Version: V1.0
 */
@Named
@Slf4j
public class ServiceConfigUtils {

    private Map<String, Map<String, String>> config;

    private String configFilePath = Class.class.getClass().getResource("/service.yaml").getPath();

    public ServiceConfigUtils() {
        loadYaml();
    }

    public void loadYaml() {
        try {
            Yaml yaml = new Yaml();
            FileInputStream stream =  new FileInputStream(new File(configFilePath));
            config = yaml.loadAs(stream, Map.class);
            stream.close();
        } catch (Exception e) {
            log.error("加载服务配置文件失败;[{}]", configFilePath, e);
        }
    }

    public ServiceConfig getConfig(String code) throws SystemException{
        if(code == null){
            return null;
        }
        Optional.ofNullable(config).orElseThrow(() -> new SystemException("服务配置异常"));

        ServiceConfig serviceConfig = null;
        Map<String, String> codeMap = null;
        String name = null;
        for(String type :config.keySet()){
            codeMap = config.get(type);
            name = codeMap.get(code);
            if(name != null){
                serviceConfig = new ServiceConfig();
                serviceConfig.setType(type);
                serviceConfig.setName(name);
                break;
            }
        }
     return serviceConfig;
    }
}
