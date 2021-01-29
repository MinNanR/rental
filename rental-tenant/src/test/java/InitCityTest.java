import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.minnan.rental.TenantApplication;
import site.minnan.rental.infrastructure.utils.RedisUtil;

@SpringBootTest(classes = TenantApplication.class)
public class InitCityTest {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void initCity() {
//        CsvReader reader = CsvUtil.getReader();
//        CsvData data = reader.read(FileUtil.file("result.csv"));
//        Iterator<CsvRow> iterator = data.iterator();
//        iterator.next();
//        while (iterator.hasNext()) {
//            CsvRow row = iterator.next();
//            String code = row.get(0);
//            String province = row.get(1);
//            String sub = row.get(2);
//            if("".equals(sub)){
//                sub = row.get(3);
//            }
//            ArrayList<String> value = CollectionUtil.newArrayList(province, sub);
//            redisUtil.hashPut("region", code, value);
//        }
    }


}
