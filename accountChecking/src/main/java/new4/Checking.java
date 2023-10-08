package new4;

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
        Map<String, BalanceDTO> resultMapOfA = convertListToMap(resultListOfA);

        //B列表转化为Map
        Map<String, BalanceDTO> resultMapOfB = convertListToMap(resultListOfB);

        //余额逐个对比
        compareDiff(resultMapOfA, resultMapOfB);
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

        compareDiff(resultMapOfA, resultMapOfB);
    }

    private static <T extends BaseDTO> void compareDiff(Map<String, T> resultMapOfA, Map<String, T> resultMapOfB) throws IOException {
        //明细逐个对比
        for (Map.Entry<String, T> temp : resultMapOfA.entrySet()) {
            if (resultMapOfB.containsKey(temp.getKey())) {
                T detailOfA = temp.getValue();
                T detailOfB = resultMapOfB.get(temp.getKey());

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
    private static <T extends BaseDTO> Map<String, T> convertListToMap(List<T> resultList) {
        Map<String, T> resultMapOf = new HashMap<>();
        for (T baseDTO : resultList) {
            resultMapOf.put(baseDTO.getKey(), baseDTO);
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
