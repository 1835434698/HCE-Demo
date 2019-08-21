package com.tangzy.cn.hce.control;

import org.json.JSONException;
import org.json.JSONObject;

public class ApplyCard {
	
//	1	productCode		32/String	Y	
//	2	cardHolderName	用户姓名	50/String	Y	
//	3	cardHolderIDType	证件类型	3/String	Y	
//	4	cardHolderID	证件号码	32/String	Y	
//	5	phoneNo	手机号	11/String	Y	
//	6	mailbox	邮箱,格式：xxxx@xxx.xx	50/String	N	
//	7	pan	  主账号，15-19位数字，不能以0开始	19/String	Y	
//	8	expiryDate	 主卡有效期	12/String	N	
//	9	verificationNumber		12/String	N	
	private String productCode;
	private String cardHolderName;
	private String cardHolderIDType;
	private String cardHolderID;
	private String phoneNo;
	private String mailbox;
	private String pan;
	private String expiryDate;
	private String verificationNumber;
	
	public void init(){
		productCode = "";
		cardHolderName = "张三";
		cardHolderIDType = "001";
		cardHolderID = "111122223333444455";
		phoneNo = "1345670955";
		mailbox = "123456789@qq.com";
		pan = "123456789012345678";
		expiryDate = "250830271230";
		verificationNumber = "";
		
	}
	
	
	
//	  1	status	状态码	2/String	“-1”: 表示超时
//	  “-2”:表示重复操作,请求被拒绝
//	  “0”:表示操作失败
//	  “1”:表示操作成功
//	    2	errorMsg	   错误信息	32/String	云支付安全插件或者云支付平台返回的错误信息
//	    3	responseContent	响应JsonObject	255/String	以JSON 字符串的形式返回（包含所需要的云卡信息）        
//
//	  	pan	主账号，15-19位数字，不能以0开始	19/String	
//	  	state	云卡状态	32/String	“READY”:卡片有效
//	  “INACTIVATED”:卡片不可用,需激“ACTIVATING”:卡片激活中“LOCKING”：卡片正在锁定“LOCKED”：卡片已锁定
//	  	keyTokensCount	可用LUK数量	2/String	
//	  	cardId	   云卡卡号	32/String	
//	  	cardExpiryDate	  卡过期日期	12/String	
//	  	imageId 	   卡面图样	32/String	
//	  	holderName 	  持卡人姓名	50/String	
//	  	paymentReadinessState	 云卡支付状态	32/String	“READY”：可支付状态“NOT_READY_PAYMENT_DISABLED”：支付已被禁用
//	  “NOT_READY_CARD_EXPIRED”:卡过期
//	  “NOT_READY_NO_KEYTOKENS”:没有可用LUK
	
	private JSONObject responseContent;
	
	public JSONObject getResult(){
		try {
			responseContent.put("pan", pan);
			responseContent.put("state", "INACTIVATED");
			responseContent.put("keyTokensCount", 10+"");
			responseContent.put("cardId", "123456789098765432");
			responseContent.put("cardExpiryDate", "270830");
			responseContent.put("imageId ", "imageId0001");
			responseContent.put("holderName  ", "张三");
			responseContent.put("paymentReadinessState  ", "NOT_READY_NO_KEYTOKENS");
			
			return responseContent;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseContent;
	}
	
	
	public void Activation(){
		String tokenPan = "123456789098765432";
		
	}
	
	public JSONObject getActivationResult(){
		try {
			responseContent.put("pan", pan);
			responseContent.put("state", "ACTIVATING");
			responseContent.put("keyTokensCount", 10+"");
			responseContent.put("cardId", "123456789098765432");
			responseContent.put("cardExpiryDate", "270830");
			responseContent.put("imageId ", "imageId0001");
			responseContent.put("holderName  ", "张三");
			responseContent.put("paymentReadinessState  ", "READY");
			
			return responseContent;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseContent;
	}
	
	

}
