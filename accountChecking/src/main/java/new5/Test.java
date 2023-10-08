package new5;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class Test {
    public static void main(String[] args) {
        AbstractCheckTemplate detailCheck = new CheckDetailStrategyServiceImpl();
        detailCheck.check(new File("A.txt").getAbsolutePath(), new File("B.txt").getAbsolutePath());
        log.info("------------------------------------------");
        AbstractCheckTemplate balanceCheck = new CheckBalanceStrategyServiceImpl();
        balanceCheck.check(new File("C.txt").getAbsolutePath(), new File("D.txt").getAbsolutePath());
    }
}
