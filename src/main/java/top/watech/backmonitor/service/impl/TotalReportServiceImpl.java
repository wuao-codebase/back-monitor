package top.watech.backmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import top.watech.backmonitor.entity.PageEntity;
import top.watech.backmonitor.entity.TotalReport;
import top.watech.backmonitor.repository.TotalReportRepository;
import top.watech.backmonitor.service.TotalReportService;

import javax.persistence.criteria.*;
import java.sql.Time;
import java.util.List;

/**
 * Created by fhm on 2018/7/27.
 */

@Service
public class TotalReportServiceImpl implements TotalReportService {
    @Autowired
    TotalReportRepository totalReportRepository;

    //根据时间取所有SRP的监控结果
    @Override
    public List<TotalReport> getTotalReportList(Time createTime) {
        return totalReportRepository.findByStartTime(createTime);
    }
    @Override
    public PageEntity getTOList(int pageNo) {
        Page<TotalReport> pages = totalReportRepository.findAll(new Specification<TotalReport>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<TotalReport> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path errorCount = root.get("errorCount");
                Predicate predicate1 = criteriaBuilder.equal(errorCount, 0);

                Path startTime = root.get("startTime");
                Predicate predicate2 = criteriaBuilder.equal(errorCount, 0);
                return null;
            }
        }, PageRequest.of(pageNo, 15));
        PageEntity pageEntity = new PageEntity();
        pageEntity.setTotal(pages.getTotalPages());
        pageEntity.setData(pages.getContent());
        return pageEntity;
    }
//    @Transactional
//    public void updateUsers(Long userId,String username){
//        userRepository.updateUserUsername(userId,username);
//    }
}
