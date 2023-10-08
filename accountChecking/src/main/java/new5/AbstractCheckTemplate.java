package new5;

import com.sun.tools.javac.util.Pair;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import new4.BaseDTO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/**
 * 抽象模板方法
 *
 * @param <T>
 */
@Slf4j
public abstract class AbstractCheckTemplate<T extends BaseDTO> {

    @SneakyThrows
    public void check(String pathOfA, String pathOfB) {
        //读取A、B端文件到内存的两个list
        final Pair<List<T>, List<T>> listPair = this.readDataFromFile(pathOfA, pathOfB, this::convertLineToDTO);
        //两个list通过某个唯一key转化为map
        final Pair<Map<String, T>, Map<String, T>> mapPair = this.convertListToMap(listPair);
        //两个map字段逐个对比
        //余额逐个对比
        compareDiff(mapPair.fst, mapPair.snd);
    }

    protected abstract T convertLineToDTO(String line);


    /**
     * @param pathOfA
     * @param pathOfB
     * @param function
     * @param <T>
     * @return
     * @throws IOException
     */
    private <T> Pair<List<T>, List<T>> readDataFromFile(String pathOfA, String pathOfB, Function<String, T> function) throws IOException {
        final List<T> resultListOfA = this.readDataFromFile(pathOfA, function);
        final List<T> resultListOfB = this.readDataFromFile(pathOfB, function);
        return new Pair<>(resultListOfA, resultListOfB);
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
    private <T> List<T> readDataFromFile(String detailPathOfA, Function<String, T> function) throws IOException {
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

    private <T extends BaseDTO> Pair<Map<String, T>, Map<String, T>> convertListToMap(Pair<List<T>, List<T>> listPair) {
        final Map<String, T> mapOfA = this.convertListToMap(listPair.fst);
        final Map<String, T> mapOfB = this.convertListToMap(listPair.snd);
        Pair pair = new Pair(mapOfA, mapOfB);
        return pair;
    }

    /**
     * 列表转化为Map
     *
     * @param resultList
     * @return
     */
    private <T extends BaseDTO> Map<String, T> convertListToMap(List<T> resultList) {
        Map<String, T> resultMapOf = new HashMap<>();
        for (T baseDTO : resultList) {
            resultMapOf.put(baseDTO.getKey(), baseDTO);
        }
        return resultMapOf;
    }


    private <T extends BaseDTO> void compareDiff(Map<String, T> resultMapOfA, Map<String, T> resultMapOfB) throws IOException {
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

}
