package com.tdin360.zjw.marathon.model;

/**
 * Created by jeffery on 2017/12/14.
 */

public class PayInfoBean {


    /**
     * model :
     * {"AppWeiXinPayModel":{"appId":null,"partnerId":null,"prepayId":null,"packageValue":null,"nonceStr":null,"timeStamp":null,"sign":null},
     * "AppAliPayData":{"state":null,"Body":"app_id=2016070701592488&biz_content=%7b%22body%22%3a%22%e9%85%92%e5%ba%97%e9%a2%84%e8%ae%a2%22%2c%22out_trade_no%22%3a%22201712161021092440010%22%2c%22product_code%22%3a%22QUICK_MSECURITY_PAY%22%2c%22subject%22%3a%22%e5%9b%bd%e9%99%85%e9%85%92%e5%ba%97-%e8%b1%aa%e5%8d%8e%e5%8d%95%e4%ba%ba%e9%97%b41%e9%97%b4-%e6%88%bf%e9%97%b4%e9%a2%84%e8%ae%a2%e8%b4%b9%e7%94%a8%22%2c%22timeout_express%22%3a%2230m%22%2c%22total_amount%22%3a%220.01%22%7d&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3a%2f%2fwww.baijar.com%2fEventAppApi%2fZFB_Notify_url&sign_type=RSA2&timestamp=2017-12-16+10%3a21%3a15&version=1.0&sign=ZjyGdWpTCapsQeCV57VGjrp41y%2f5jsWTggyu%2fDsih8dI9%2fQyg62G7baCr00zrlyupHS28A2tXDcHQDEXu2D6wiHYYqf6o8MfcIaJQbhhmjBL3%2b9WwD3MPiJtpXWvz9TsKZZW583IT9x00DKk7zUeBSd2GQp9BIoymY0riEZ6Pmy2iqnse4%2fr1wIZViP4LRL%2fCxnBuwsAM1JEQ5W831w6Xz8FmVRqSs4Sf0jRgsCPnbFGqD3ShGL%2bSDeSqXVi0%2fPCxAfSOfH%2fL9%2fpo6hwNF7AOqUDxeW%2fvGTQ2vsuNgEH%2bPO5p6HbF4a5Ak%2f6NBFSqY%2fqtIxaOS2jlpdK0Ys83te3mg%3d%3d"},
     * "AppWxRefundModel":{"result_code":null,"appid":null,"mch_id":null,"nonce_str":null,"sign":null,"transaction_id":null,"out_trade_no":null,"out_refund_no":null,"refund_id":null,"refund_fee":null}}
     */

    private ModelBean model;

    public ModelBean getModel() {
        return model;
    }

    public void setModel(ModelBean model) {
        this.model = model;
    }

    public static class ModelBean {
        /**
         * AppWeiXinPayModel : {"appId":null,"partnerId":null,"prepayId":null,"packageValue":null,"nonceStr":null,"timeStamp":null,"sign":null}
         * AppAliPayData : {"state":null,"Body":"app_id=2016070701592488&biz_content=%7b%22body%22%3a%22%e9%85%92%e5%ba%97%e9%a2%84%e8%ae%a2%22%2c%22out_trade_no%22%3a%22201712161021092440010%22%2c%22product_code%22%3a%22QUICK_MSECURITY_PAY%22%2c%22subject%22%3a%22%e5%9b%bd%e9%99%85%e9%85%92%e5%ba%97-%e8%b1%aa%e5%8d%8e%e5%8d%95%e4%ba%ba%e9%97%b41%e9%97%b4-%e6%88%bf%e9%97%b4%e9%a2%84%e8%ae%a2%e8%b4%b9%e7%94%a8%22%2c%22timeout_express%22%3a%2230m%22%2c%22total_amount%22%3a%220.01%22%7d&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3a%2f%2fwww.baijar.com%2fEventAppApi%2fZFB_Notify_url&sign_type=RSA2&timestamp=2017-12-16+10%3a21%3a15&version=1.0&sign=ZjyGdWpTCapsQeCV57VGjrp41y%2f5jsWTggyu%2fDsih8dI9%2fQyg62G7baCr00zrlyupHS28A2tXDcHQDEXu2D6wiHYYqf6o8MfcIaJQbhhmjBL3%2b9WwD3MPiJtpXWvz9TsKZZW583IT9x00DKk7zUeBSd2GQp9BIoymY0riEZ6Pmy2iqnse4%2fr1wIZViP4LRL%2fCxnBuwsAM1JEQ5W831w6Xz8FmVRqSs4Sf0jRgsCPnbFGqD3ShGL%2bSDeSqXVi0%2fPCxAfSOfH%2fL9%2fpo6hwNF7AOqUDxeW%2fvGTQ2vsuNgEH%2bPO5p6HbF4a5Ak%2f6NBFSqY%2fqtIxaOS2jlpdK0Ys83te3mg%3d%3d"}
         * AppWxRefundModel : {"result_code":null,"appid":null,"mch_id":null,"nonce_str":null,"sign":null,"transaction_id":null,"out_trade_no":null,"out_refund_no":null,"refund_id":null,"refund_fee":null}
         */

        private AppWeiXinPayModelBean AppWeiXinPayModel;
        private AppAliPayDataBean AppAliPayData;
        private AppWxRefundModelBean AppWxRefundModel;

        public AppWeiXinPayModelBean getAppWeiXinPayModel() {
            return AppWeiXinPayModel;
        }

        public void setAppWeiXinPayModel(AppWeiXinPayModelBean AppWeiXinPayModel) {
            this.AppWeiXinPayModel = AppWeiXinPayModel;
        }

        public AppAliPayDataBean getAppAliPayData() {
            return AppAliPayData;
        }

        public void setAppAliPayData(AppAliPayDataBean AppAliPayData) {
            this.AppAliPayData = AppAliPayData;
        }

        public AppWxRefundModelBean getAppWxRefundModel() {
            return AppWxRefundModel;
        }

        public void setAppWxRefundModel(AppWxRefundModelBean AppWxRefundModel) {
            this.AppWxRefundModel = AppWxRefundModel;
        }

        public static class AppWeiXinPayModelBean {
            /**
             * appId : null
             * partnerId : null
             * prepayId : null
             * packageValue : null
             * nonceStr : null
             * timeStamp : null
             * sign : null
             */

            private String appId;
            private String partnerId;
            private String prepayId;
            private String packageValue;
            private String nonceStr;
            private String timeStamp;
            private String sign;

            public String getAppId() {
                return appId;
            }

            public void setAppId(String appId) {
                this.appId = appId;
            }

            public String getPartnerId() {
                return partnerId;
            }

            public void setPartnerId(String partnerId) {
                this.partnerId = partnerId;
            }

            public String getPrepayId() {
                return prepayId;
            }

            public void setPrepayId(String prepayId) {
                this.prepayId = prepayId;
            }

            public String getPackageValue() {
                return packageValue;
            }

            public void setPackageValue(String packageValue) {
                this.packageValue = packageValue;
            }

            public String getNonceStr() {
                return nonceStr;
            }

            public void setNonceStr(String nonceStr) {
                this.nonceStr = nonceStr;
            }

            public String getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }
        }

        public static class AppAliPayDataBean {
            /**
             * state : null
             * Body : app_id=2016070701592488&biz_content=%7b%22body%22%3a%22%e9%85%92%e5%ba%97%e9%a2%84%e8%ae%a2%22%2c%22out_trade_no%22%3a%22201712161021092440010%22%2c%22product_code%22%3a%22QUICK_MSECURITY_PAY%22%2c%22subject%22%3a%22%e5%9b%bd%e9%99%85%e9%85%92%e5%ba%97-%e8%b1%aa%e5%8d%8e%e5%8d%95%e4%ba%ba%e9%97%b41%e9%97%b4-%e6%88%bf%e9%97%b4%e9%a2%84%e8%ae%a2%e8%b4%b9%e7%94%a8%22%2c%22timeout_express%22%3a%2230m%22%2c%22total_amount%22%3a%220.01%22%7d&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3a%2f%2fwww.baijar.com%2fEventAppApi%2fZFB_Notify_url&sign_type=RSA2&timestamp=2017-12-16+10%3a21%3a15&version=1.0&sign=ZjyGdWpTCapsQeCV57VGjrp41y%2f5jsWTggyu%2fDsih8dI9%2fQyg62G7baCr00zrlyupHS28A2tXDcHQDEXu2D6wiHYYqf6o8MfcIaJQbhhmjBL3%2b9WwD3MPiJtpXWvz9TsKZZW583IT9x00DKk7zUeBSd2GQp9BIoymY0riEZ6Pmy2iqnse4%2fr1wIZViP4LRL%2fCxnBuwsAM1JEQ5W831w6Xz8FmVRqSs4Sf0jRgsCPnbFGqD3ShGL%2bSDeSqXVi0%2fPCxAfSOfH%2fL9%2fpo6hwNF7AOqUDxeW%2fvGTQ2vsuNgEH%2bPO5p6HbF4a5Ak%2f6NBFSqY%2fqtIxaOS2jlpdK0Ys83te3mg%3d%3d
             */

            private Object state;
            private String Body;

            public Object getState() {
                return state;
            }

            public void setState(Object state) {
                this.state = state;
            }

            public String getBody() {
                return Body;
            }

            public void setBody(String Body) {
                this.Body = Body;
            }
        }

        public static class AppWxRefundModelBean {
            /**
             * result_code : null
             * appid : null
             * mch_id : null
             * nonce_str : null
             * sign : null
             * transaction_id : null
             * out_trade_no : null
             * out_refund_no : null
             * refund_id : null
             * refund_fee : null
             */

            private Object result_code;
            private Object appid;
            private Object mch_id;
            private Object nonce_str;
            private Object sign;
            private Object transaction_id;
            private Object out_trade_no;
            private Object out_refund_no;
            private Object refund_id;
            private Object refund_fee;

            public Object getResult_code() {
                return result_code;
            }

            public void setResult_code(Object result_code) {
                this.result_code = result_code;
            }

            public Object getAppid() {
                return appid;
            }

            public void setAppid(Object appid) {
                this.appid = appid;
            }

            public Object getMch_id() {
                return mch_id;
            }

            public void setMch_id(Object mch_id) {
                this.mch_id = mch_id;
            }

            public Object getNonce_str() {
                return nonce_str;
            }

            public void setNonce_str(Object nonce_str) {
                this.nonce_str = nonce_str;
            }

            public Object getSign() {
                return sign;
            }

            public void setSign(Object sign) {
                this.sign = sign;
            }

            public Object getTransaction_id() {
                return transaction_id;
            }

            public void setTransaction_id(Object transaction_id) {
                this.transaction_id = transaction_id;
            }

            public Object getOut_trade_no() {
                return out_trade_no;
            }

            public void setOut_trade_no(Object out_trade_no) {
                this.out_trade_no = out_trade_no;
            }

            public Object getOut_refund_no() {
                return out_refund_no;
            }

            public void setOut_refund_no(Object out_refund_no) {
                this.out_refund_no = out_refund_no;
            }

            public Object getRefund_id() {
                return refund_id;
            }

            public void setRefund_id(Object refund_id) {
                this.refund_id = refund_id;
            }

            public Object getRefund_fee() {
                return refund_fee;
            }

            public void setRefund_fee(Object refund_fee) {
                this.refund_fee = refund_fee;
            }
        }
    }
}
