package new5;

import new4.DetailDTO;

public class CheckDetailStrategyServiceImpl extends AbstractCheckTemplate<DetailDTO> {
    @Override
    protected DetailDTO convertLineToDTO(String line) {
        return DetailDTO.convert(line);
    }
}
