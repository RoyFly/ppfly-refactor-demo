package new6;

public interface ICheckStrategy {

    /**
     * 校验
     *
     * @param pathOfA
     * @param pathOfB
     */
    void check(String pathOfA, String pathOfB) throws Exception;

    /**
     * 校验的类型：明细/余额
     *
     * @return
     */
    CheckEnum getCheckEnum();
}
