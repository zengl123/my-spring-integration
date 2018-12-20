package com.zenlong.study.domain;

import lombok.Data;

import java.util.List;

/**
 * 描述:
 * 项目名:cloud-tdp-data-interface
 *
 * @Author:ZENLIN
 * @Created 2018/10/25  14:22.
 */
@Data
public class ParkEntryRecordThird {
    /**
     * 记录id
     */
    private String Id;
    /**
     * 卡号
     */
    private String cardId;
    /**
     * 卡编号
     */
    private String cardNo;
    /**
     * 车牌号码
     */
    private String carNo;
    /**
     * 车主姓名
     */
    private String ownerName;
    /**
     * 卡类型
     */
    private String cardTypeName;
    /**
     * 入口名称
     */
    private String entryName;
    /**
     * 车辆入场时间 格式 yyyy-MM-dd HH:mm:ss
     */
    private String entryTime;

    /**
     * 入口操作员
     */
    private String entryOptName;
    /**
     * 出口名称
     */
    private String exitName;
    /**
     * 车辆出场时间 格式 yyyy-MM-dd HH:mm:ss
     */
    private String exitTime;
    /**
     * 出口操作员
     */
    private String exitOptName;
    /**
     * 费用信息
     */
    private List<PaymentInfo> paymentInfo;

    @Data
    public class PaymentInfo {
        /**
         * 应收金额 单位：元
         */
        private String accountCharge;
        /**
         * 实收金额 单位：元
         */
        private String payCharge;
        /**
         * 付款时间
         */
        private String PayDate;
        /**
         * 折扣金额 单位：元
         */
        private String disAmount;
        /**
         * 支付类型编号
         */
        private String paymentType;
        /**
         * 交易编号 唯一值
         */
        private String paymentTnx;
    }
}
