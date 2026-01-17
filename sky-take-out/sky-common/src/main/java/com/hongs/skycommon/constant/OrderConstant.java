package com.hongs.skycommon.constant;

/**
 * 订单常量
 */
public class OrderConstant {

    // --- 1. 订单状态 (status) ---
    /** 待付款 */
    public static final Integer PENDING_PAYMENT = 1;
    /** 待接单 */
    public static final Integer TO_BE_CONFIRMED = 2;
    /** 已接单 */
    public static final Integer CONFIRMED = 3;
    /** 派送中 */
    public static final Integer DELIVERY_IN_PROGRESS = 4;
    /** 已完成 */
    public static final Integer COMPLETED = 5;
    /** 已取消 */
    public static final Integer CANCELLED = 6;

    // --- 2. 支付方式 (pay_method) ---
    /** 微信支付 */
    public static final Integer WECHAT = 1;
    /** 支付宝支付 */
    public static final Integer ALIPAY = 2;

    // --- 3. 支付状态 (pay_status) ---
    /** 未支付 */
    public static final Integer UN_PAID = 0;
    /** 已支付 */
    public static final Integer PAID = 1;
    /** 退款 */
    public static final Integer REFUND = 2;

    // --- 4. 餐具数量状态 (tableware_status) ---
    /** 按餐量提供 */
    public static final Integer TABLEWARE_BY_NUMBER_OF_PEOPLE = 1;
    /** 选择具体数量 */
    public static final Integer TABLEWARE_SPECIFIED_QUANTITY = 0;

    // --- 5. 配送状态/时间要求 (delivery_status) ---
    /** 立即送出 */
    public static final Integer DELIVERY_IMMEDIATELY = 1;
    /** 选择具体时间 */
    public static final Integer DELIVERY_SPECIFIED_TIME = 0;

}