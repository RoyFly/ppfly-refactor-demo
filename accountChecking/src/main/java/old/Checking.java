package old;

import common.DetailDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Checking {
    //对比明细
    private static void checkDetail(String detailPathOfA, String detailPathOfB) throws IOException {

        //读取A端的文件
        List<DetailDTO> resultListOfA = new ArrayList<>();
        try (BufferedReader reader1 = new BufferedReader(new FileReader(detailPathOfA))) {
            String line;
            while ((line = reader1.readLine()) != null) {
                resultListOfA.add(DetailDTO.convert(line));
            }
        }

        //读取B端的文件
        List<DetailDTO> resultListOfB = new ArrayList<>();
        try (BufferedReader reader1 = new BufferedReader(new FileReader(detailPathOfB))) {
            String line;
            while ((line = reader1.readLine()) != null) {
                resultListOfB.add(DetailDTO.convert(line));
            }
        }

        //A列表转化为Map
        Map<String, DetailDTO> resultMapOfA = new HashMap<>();
        for (DetailDTO detail : resultListOfA) {
            resultMapOfA.put(detail.getBizSeq(), detail);
        }

        //B列表转化为Map
        Map<String, DetailDTO> resultMapOfB = new HashMap<>();
        for (DetailDTO detail : resultListOfB) {
            resultMapOfB.put(detail.getBizSeq(), detail);
        }

        //明细逐个对比
        for (Map.Entry<String, DetailDTO> temp : resultMapOfA.entrySet()) {
            if (resultMapOfB.containsKey(temp.getKey())) {
                DetailDTO detailOfA = temp.getValue();
                DetailDTO detailOfB = resultMapOfB.get(temp.getKey());

                if (!detailOfA.getAmt().equals(detailOfB.getAmt())) {
                    log.warn("amt is different,key:{}", temp.getKey());
                }
                if (!detailOfA.getDate().equals(detailOfB.getDate())) {
                    log.warn("date is different,key:{}", temp.getKey());
                }
                if (!detailOfA.getStatus().equals(detailOfB.getStatus())) {
                    log.warn("status is different,key:{}", temp.getKey());
                }
            }
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        checkDetail(new File("A.txt").getAbsolutePath(),new File("B.txt").getAbsolutePath());
    }
}
