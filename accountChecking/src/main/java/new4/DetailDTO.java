package new4;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账单详情
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailDTO extends BaseDTO {
    /**
     * 业务流水号
     */
    private String bizSeq;
    /**
     * 金额
     */
    private String amt;
    /**
     * 日期
     */
    private String date;
    /**
     * 状态
     */
    private String status;

    public static DetailDTO convert(String line) {
        final String[] split = line.split(" ");
        DetailDTO detailDTO = new DetailDTO(split[0], split[1], split[2], split[3]);
        return detailDTO;
    }

    @Override
    public String getKey() {
        return bizSeq;
    }
}
