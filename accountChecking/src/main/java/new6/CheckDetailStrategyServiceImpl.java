package new6;

import new4.DetailDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CheckDetailStrategyServiceImpl extends AbstractCheckTemplate<DetailDTO> {

    @Override
    protected DetailDTO convertLineToDTO(String line) {
        return DetailDTO.convert(line);
    }

    //对比校验类型为：明细
    @Override
    public CheckEnum getCheckEnum() {
        return CheckEnum.DETAIL_CHECK;
    }
}