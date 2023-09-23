package com.unfbx.chatgptsteamoutput.until.RequestTypeUntil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestTypeFactory implements InitializingBean, ApplicationContextAware {
    private static Map<RequestTypeEnum, RequestType> requestStrategies = new HashMap<>();
    private ApplicationContext appContext;

    public static RequestType getRequest(RequestTypeEnum requestType) {
        if (requestType == null) {
            throw new IllegalArgumentException("Type is null!");
        }
        if (!requestStrategies.containsKey(requestType)) {
            throw new IllegalArgumentException("Type not supported");
        }
        return requestStrategies.get(requestType);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        appContext.getBeansOfType(RequestType.class)
                .values()
                .forEach(requestType -> requestStrategies.put(requestType.getRequestType(), requestType));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }
}
