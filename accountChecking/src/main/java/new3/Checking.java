package new3;

import common.BalanceDTO;
import common.DetailDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

@Slf4j
public class Checking {
    //对比余额
    private static void checkBalance(String balancePathOfA, String balancePathOfB) throws IOException {
        //读取A端的文件
        List<BalanceDTO> resultListOfA = readDataFromFile(balancePathOfA, BalanceDTO::convert);

        //读取B端的文件
        List<BalanceDTO> resultListOfB = readDataFromFile(balancePathOfB, BalanceDTO::convert);

        //A列表转化为Map
        Map<String, BalanceDTO> resultMapOfA = convertBalanceListToMap(resultListOfA);

        //B列表转化为Map
        Map<String, BalanceDTO> resultMapOfB = convertBalanceListToMap(resultListOfB);

        //余额逐个对比
        for (Map.Entry<String, BalanceDTO> temp : resultMapOfA.entrySet()) {
            if (resultMapOfB.containsKey(temp.getKey())) {
                BalanceDTO balanceOfA = temp.getValue();
                BalanceDTO balanceOfB = resultMapOfB.get(temp.getKey());

                final List<String> list = compareObject(balanceOfA, balanceOfB);
                for (String str : list) {
                    log.warn("{} is different,key:{}", str, temp.getKey());
                }
            }
        }
    }

    //对比明细
    private static void checkDetail(String detailPathOfA, String detailPathOfB) throws IOException {
        //读取A端的文件
        List<DetailDTO> resultListOfA = readDataFromFile(detailPathOfA, DetailDTO::convert);

        //读取B端的文件
        List<DetailDTO> resultListOfB = readDataFromFile(detailPathOfB, DetailDTO::convert);

        //A列表转化为Map
        Map<String, DetailDTO> resultMapOfA = convertListToMap(resultListOfA);

        //B列表转化为Map
        Map<String, DetailDTO> resultMapOfB = convertListToMap(resultListOfB);

        //明细逐个对比
        for (Map.Entry<String, DetailDTO> temp : resultMapOfA.entrySet()) {
            if (resultMapOfB.containsKey(temp.getKey())) {
                DetailDTO detailOfA = temp.getValue();
                DetailDTO detailOfB = resultMapOfB.get(temp.getKey());

                final List<String> list = compareObject(detailOfA, detailOfB);
                for (String str : list) {
                    log.warn("{} is different,key:{}", str, temp.getKey());
                }
            }
        }
    }

    /**
     * 列表转化为Map
     *
     * @param resultList
     * @return
     */
    private static Map<String, DetailDTO> convertListToMap(List<DetailDTO> resultList) {
        Map<String, DetailDTO> resultMapOf = new HashMap<>();
        for (DetailDTO detail : resultList) {
            resultMapOf.put(detail.getBizSeq(), detail);
        }
        return resultMapOf;
    }


    /**
     * 账户余额列表转化为Map
     *
     * @param resultList
     * @return
     */
    private static Map<String, BalanceDTO> convertBalanceListToMap(List<BalanceDTO> resultList) {
        Map<String, BalanceDTO> resultMapOf = new HashMap<>();
        for (BalanceDTO balanceDTO : resultList) {
            resultMapOf.put(balanceDTO.getAccountNo().concat("-").concat(balanceDTO.getType()), balanceDTO);
        }
        return resultMapOf;
    }

    /**
     * 读取文件存储至集合
     *
     * @param detailPathOfA
     * @param function      lambda表达式函数型接口
     * @param <T>
     * @return
     * @throws IOException
     */
    private static <T> List<T> readDataFromFile(String detailPathOfA, Function<String, T> function) throws IOException {
        //读取端的文件
        List<T> resultList = new ArrayList<>();
        try (BufferedReader reader1 = new BufferedReader(new FileReader(detailPathOfA))) {
            String line;
            while ((line = reader1.readLine()) != null) {
                resultList.add(function.apply(line));
            }
        }
        return resultList;
    }

    @SneakyThrows
    private static List<String> compareObject(Object obj1, Object obj2) throws IOException {
        List<String> list = new ArrayList<>();
        final Field[] fields = obj1.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            final Object value1 = field.get(obj1);
            final Object value2 = field.get(obj2);
            if (!Objects.equals(value1, value2)) {
                list.add(field.getName());
            }
        }
        return list;
    }


    @SneakyThrows
    public static void main(String[] args) {
        checkDetail(new File("A.txt").getAbsolutePath(), new File("B.txt").getAbsolutePath());
        log.info("------------------------------------------");
        checkBalance(new File("C.txt").getAbsolutePath(), new File("D.txt").getAbsolutePath());
    }
}
