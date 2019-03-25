package com.able.springboothelloworld.com.able.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jipeng
 * @date 2019-03-15 10:03
 * @description
 */
@Data
public class UserScore {
    //主键
    private Long id;
    //用户id
    private Long userId;
    //课程id
    private Long courseId;
    //视频观看得分
    private BigDecimal videoAccessScore;
    //随堂测验得分
    private BigDecimal testAccessScore;
    //考试得分
    private BigDecimal examAccessScore;
    //课程总得分
    private BigDecimal totalAccessScore;
}

