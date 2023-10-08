package new6;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

import static new6.CheckEnum.BALANCE_CHECK;
import static new6.CheckEnum.DETAIL_CHECK;

@Slf4j
public class Test {
    public static void main(String[] args) throws Exception {
        //1.得到Spring上下文对象
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        //2.从Spring中获取bean对象
        CheckCompareFactory checkCompareFactory = (CheckCompareFactory) applicationContext.getBean("checkCompareFactory");
        //3.使用bean
        checkCompareFactory.checkCompare(DETAIL_CHECK, new File("A.txt").getAbsolutePath(), new File("B.txt").getAbsolutePath());
        log.info("------------------------------------------");
        checkCompareFactory.checkCompare(BALANCE_CHECK, new File("C.txt").getAbsolutePath(), new File("D.txt").getAbsolutePath());
    }
}
