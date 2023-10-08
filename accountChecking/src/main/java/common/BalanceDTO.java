package common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 余额
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceDTO {
    /**
     * 户号
     */
    private String accountNo;
    /**
     * 余额
     */
    private String balance;

    /**
     * 类别
     */
    private String type;

    public static BalanceDTO convert(String line) {
        final String[] split = line.split(" ");
        BalanceDTO detailDTO = new BalanceDTO(split[0], split[1], split[2]);
        return detailDTO;
    }
}
