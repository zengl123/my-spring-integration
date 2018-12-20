package com.zenlong.study.domain.po;

import com.zenlong.study.domain.BaseModel;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

/**
 * 描述:
 * 项目名:my-spring-integration
 *
 * @Author:ZENLIN
 * @Created 2018/10/23  17:46.
 */
@Data
public class ParkExportRecord extends BaseModel {
    public final static transient String INDEX = "index_park_export_record";
    public final static transient String TYPE = "type_park_export_record";
    @Id
    private String id;
    /**
     * 停车场编号
     */
    private String parkNo;
    /**
     * 车牌号码
     */
    private String plateNumber;
    /**
     * 卡编号
     */
    private String cardNo;
    /**
     * 卡类型
     */
    private String cardTypeName;
    /**
     * 车主姓名
     */
    private String ownerName;
    /**
     * 入场时间
     */
    private String entryTime;
    /**
     * 入口名称
     */
    private String entryName;
    /**
     * 入口操作员
     */
    private String entryOptName;
    /**
     * 出场时间
     */
    private String exportTime;
    /**
     * 出口名称
     */
    private String exportName;
    /**
     * 出口操作员
     */
    private String exportOptName;
    /**
     * 应收金额
     */
    private BigDecimal receivableAmount;
    /**
     * 实收金额
     */
    private BigDecimal paidAmount;
    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;
    /**
     * 支付类型(0-现金支付,1-微信支付,2-支付宝支付,3-银联闪付,4-交通卡支付,5-自助缴费机,6-其它)
     */
    private Integer paymentType;
    private transient String paymentTypeName;
}
