package top.watech.backmonitor.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.test.context.junit4.SpringRunner;
import top.watech.backmonitor.entity.TotalReport;
import top.watech.backmonitor.repository.TotalReportRepository;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by wuao.tp on 2018/8/9.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class Monitest {

    @Autowired
    private TotalReportRepository totalReportRepository;

    @Test
    public void testData() throws ParseException {
        for (int i = 0; i < 5; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = null;
            try {
                time = sdf.parse(sdf.format(new Date()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            TotalReport totalReport = new TotalReport();
            Date date = new Date();
            totalReport.setStartTime(time);
            totalReport.setErrorCount(1);
            totalReport.setMonitorNum(12);
            totalReport.setSrpId((long) 66);
            totalReport.setEndTime(time);
            totalReportRepository.save(totalReport);
        }

    }

    @Test
    public void findAll() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<TotalReport> pages = totalReportRepository.findAll(pageable);
        List<TotalReport> content = pages.getContent();
        for (TotalReport totalReport : content) {
            System.out.println(totalReport);
        }
    }

    @Test
    public void findAllwhere() {
        Page<TotalReport> all = totalReportRepository.findAll(new Specification<TotalReport>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<TotalReport> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicate = new ArrayList<>();
                Path errorCount = root.get("errorCount");
                Path time = root.get("startTime");

                predicate.add(criteriaBuilder.equal(errorCount, 0));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date   starttime = sdf.parse("2018-08-09 14:19:45.0");
                    Date   endtime = sdf.parse("2018-08-09 14:25:45.0");
                    predicate.add(criteriaBuilder.between(time,starttime,endtime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Predicate[] pre = new Predicate[predicate.size()];
                return criteriaQuery.where(predicate.toArray(pre)).getRestriction();
            }
        }, PageRequest.of(0, 15));
        for (TotalReport report : all) {
            System.err.println("report = " + report);
//            String resultJson = JSONObject.toJSONString(report);

            System.err.println("report = " + report.getSrp());
        }

    }
//    @Test
//    public void findallDynamic(){
//        Monitest monitest = new Monitest();
//        Page<TotalReport> allwhere = monitest.findAllwhere(null, 15, 0);
//        for (TotalReport report : allwhere) {
//            System.out.println("report = " + report);
//        }
//    }

}

