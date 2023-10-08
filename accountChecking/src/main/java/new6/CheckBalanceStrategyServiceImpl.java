package new6;

import new4.BalanceDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 余额对比策略
 */
@Service
public class CheckBalanceStrategyServiceImpl extends AbstractCheckTemplate<BalanceDTO> {

    @Override
    protected BalanceDTO convertLineToDTO(String line) {
        return BalanceDTO.convert(line);
    }

    //对比校验类型为：余额
    @Override
    public CheckEnum getCheckEnum() {
        return CheckEnum.BALANCE_CHECK;
    }
}