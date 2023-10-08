package new2;

import common.DetailDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class Checking {
    //对比明细
    private static void checkDetail(String detailPathOfA, String detailPathOfB) throws IOException {
        //读取A端的文件
        List<DetailDTO> resultListOfA = readFile(detailPathOfA);

        //读取B端的文件
        List<DetailDTO> resultListOfB = readFile(detailPathOfB);

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
                    log.warn("{} is different,key:{}",str, temp.getKey());
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
     * 读取文件存储至集合
     *
     * @param detailPathOfA
     * @return
     * @throws IOException
     */
    private static List<DetailDTO> readFile(String detailPathOfA) throws IOException {
        //读取端的文件
        List<DetailDTO> resultList = new ArrayList<>();
        try (BufferedReader reader1 = new BufferedReader(new FileReader(detailPathOfA))) {
            String line;
            while ((line = reader1.readLine()) != null) {
                resultList.add(DetailDTO.convert(line));
            }
        }
        return resultList;
    }

    @SneakyThrows
    private static List<String> compareObject(DetailDTO detailDTO1, DetailDTO detailDTO2) throws IOException {
        List<String> list = new ArrayList<>();
        final Field[] fields = detailDTO1.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            final Object value1 = field.get(detailDTO1);
            final Object value2 = field.get(detailDTO2);
            if(!Objects.equals(value1,value2)){
                list.add(field.getName());
            }
        }
        return list;
    }


    @SneakyThrows
    public static void main(String[] args) {
        checkDetail(new File("A.txt").getAbsolutePath(), new File("B.txt").getAbsolutePath());
    }
}
