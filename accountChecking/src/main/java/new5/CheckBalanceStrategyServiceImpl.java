package new5;

import new4.BalanceDTO;

public class CheckBalanceStrategyServiceImpl extends AbstractCheckTemplate<BalanceDTO> {
    @Override
    protected BalanceDTO convertLineToDTO(String line) {
        return BalanceDTO.convert(line);
    }
}
