package com.able.springboothelloworld.com.able.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jipeng
 * @date 2019-03-15 10:30
 * @description
 */
@Data
public class ScoreRule {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 课程id
     */
    private Long courseId;
    /**
     * 课程总分
     */
    private Integer TotalScore;
    /**
     * 过关分数
     */
    private BigDecimal passScore;
    /**
     * 视频观看进度占比
     */
    private BigDecimal videoViewProPrecent;
    /**
     * 随堂测试占比
     */
    private BigDecimal classRoomTestPrecent;
    /**
     *期末考试成绩占比
     */
    private BigDecimal FinalExamPrecent;

}

