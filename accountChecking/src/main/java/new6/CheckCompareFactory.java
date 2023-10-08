package new6;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CheckCompareFactory implements ApplicationContextAware {

    private Map<CheckEnum, ICheckStrategy> checkStrategyMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        final Map<String, ICheckStrategy> beansOfType = applicationContext.getBeansOfType(ICheckStrategy.class);
        for (ICheckStrategy checkStrategy : beansOfType.values()) {
            checkStrategyMap.put(checkStrategy.getCheckEnum(), checkStrategy);
        }
    }

    public void checkCompare(CheckEnum checkEnum, String pathOfA, String pathOfB) throws Exception {
        checkStrategyMap.get(checkEnum).check(pathOfA, pathOfB);
    }
}
