package top.watech.backmonitor.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import top.watech.backmonitor.entity.PageEntity;
import top.watech.backmonitor.entity.SRP;
import top.watech.backmonitor.entity.TotalReport;
import top.watech.backmonitor.repository.SrpRepository;
import top.watech.backmonitor.repository.TotalReportRepository;
import top.watech.backmonitor.repository.UserRepository;
import top.watech.backmonitor.service.SRPService;
import top.watech.backmonitor.service.TotalReportService;

import javax.persistence.criteria.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * TotalReportService接口实现类
 * Created by fhm on 2018/7/27.
 */

@Service
public class TotalReportServiceImpl implements TotalReportService {
    @Autowired
    TotalReportRepository totalReportRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SrpRepository srpRepository;
    @Autowired
    SRPService srpService;

    //根据时间取所有SRP的监控结果
    @Override
    public List<TotalReport> getTotalReportList(Time createTime) {
        return totalReportRepository.findByStartTimeOrderByStartTimeDesc(createTime);
    }

    //总监控列表（按所属用户显示）
    @Override
    public PageEntity getTOList(int pageNo, int role, TotalReport totalReport, Long userId) {
        Sort sort = new Sort(Sort.Direction.DESC, "startTime");
        if (role == 1) {
            Page<TotalReport> pages = totalReportRepository.findAll(new Specification<TotalReport>() {
                @Nullable
                @Override
                public Predicate toPredicate(Root<TotalReport> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicate = new ArrayList<>();
                    Path srpId = root.get("srpId");
                    Path errorCount = root.get("errorCount");
                    Path time = root.get("startTime");

                    if (totalReport.getSrpId() != null) {
                        predicate.add(criteriaBuilder.equal(srpId, totalReport.getSrpId()));

                    }
                    if (totalReport.getErrorCount() != null && totalReport.getErrorCount() == 1) {
                        predicate.add(criteriaBuilder.equal(errorCount, 0));
                    }
                    if (totalReport.getErrorCount() != null && totalReport.getErrorCount() == 2) {
                        predicate.add(criteriaBuilder.gt(errorCount, 0));
                    }

                    if (totalReport.getStartTime() != null && totalReport.getEndTime() != null) {

                        predicate.add(criteriaBuilder.between(time, totalReport.getStartTime(), totalReport.getEndTime()));
                    }

                    Predicate[] pre = new Predicate[predicate.size()];
                    return criteriaQuery.where(predicate.toArray(pre)).getRestriction();
                }
            }, PageRequest.of(pageNo - 1, 15, sort));
            for (TotalReport page : pages) {
                if (page.getSrp().getSrpName() != null) {
                    page.setSrpName(page.getSrp().getSrpName());
                }
            }
            PageEntity pageEntity = new PageEntity();
            pageEntity.setTotal((int) pages.getTotalElements());
            pageEntity.setData(pages.getContent());
            return pageEntity;
        } else {
            List<SRP> srpList = srpService.findByUserId(userId);//62L
            if (srpList.size() != 0) {
                Page<TotalReport> pages = totalReportRepository.findAll(new Specification<TotalReport>() {
                    @Nullable
                    @Override
                    public Predicate toPredicate(Root<TotalReport> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                        List<Predicate> predicate = new ArrayList<>();

                        Path srpId = root.get("srpId");
                        Path errorCount = root.get("errorCount");
                        Path time = root.get("startTime");

                        if (totalReport.getSrpId() != null) {
                            predicate.add(criteriaBuilder.equal(srpId, totalReport.getSrpId()));

                        } else {


                            List<Long> srpIds = new ArrayList<>();
                            for (SRP srp : srpList) {
                                Long srpId1 = srp.getSrpId();
                                srpIds.add(srpId1);
                            }
                            System.err.println(srpIds);

                            predicate.add(root.get("srpId").in(srpIds));

                        }
                        if (totalReport.getErrorCount() != null && totalReport.getErrorCount() == 1) {
                            predicate.add(criteriaBuilder.equal(errorCount, 0));
                        }
                        if (totalReport.getErrorCount() != null && totalReport.getErrorCount() == 2) {
                            predicate.add(criteriaBuilder.gt(errorCount, 0));
                        }

                        if (totalReport.getStartTime() != null && totalReport.getEndTime() != null) {

                            predicate.add(criteriaBuilder.between(time, totalReport.getStartTime(), totalReport.getEndTime()));
                        }

                        Predicate[] pre = new Predicate[predicate.size()];
                        return criteriaQuery.where(predicate.toArray(pre)).getRestriction();
                    }
                }, PageRequest.of(pageNo - 1, 15, sort));

                if (pages == null) {
                    return null;
                }
                for (TotalReport page : pages) {
                    if (page.getSrp().getSrpName() != null) {
                        page.setSrpName(page.getSrp().getSrpName());
                    }
                }
                PageEntity pageEntity = new PageEntity();
                pageEntity.setTotal((int) pages.getTotalElements());
                pageEntity.setData(pages.getContent());
                return pageEntity;
            }
            else {
                PageEntity pageEntity = new PageEntity();

                return pageEntity;
            }

        }
    }


//    }
//    @Transactional
//    public void updateUsers(Long userId,String username){
//        userRepository.updateUserUsername(userId,username);
//    }
}
