package com.tangzy.cn.hce.control;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

import java.sql.Timestamp;
import java.util.List;

import org.json.JSONObject;

import com.tangzy.cn.hce.db.DatabaseDao;

/**
 * Created by tang on 2015/8/26.
 */
public class CardControl {

	private Context context;
	private static String FILECITIC = "citic_hce";

	public CardControl(Context context) {
		this.context = context;
	}

	public void cardInit() throws Exception {

			SharedPreferences sp = context.getSharedPreferences(FILECITIC, Activity.MODE_ENABLE_WRITE_AHEAD_LOGGING);
			if (!sp.getBoolean("isOpen", false)) {

				return;
			}
			String packageName = sp.getString("packageName", "");
			Context c = context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY);

			// int SequenceCounter = 0;
			SharedPreferences.Editor editor = c.getSharedPreferences(FILECITIC, Activity.MODE_ENABLE_WRITE_AHEAD_LOGGING).edit();
			// TODO 卡片图样：可视化的卡片外观，用于持卡人对账户的识别；

			// 账户ID：账户别称或账户的后4位，用于持卡人对账户的识别； 。
			editor.putString("CardId", "6651");
			// 账户状态：如暂停、活动（激活状态）等；
			editor.putString("CardState", "ACTIVATING");
			// TODO 账户配置信息：参见6.3.2.
			// 该账户所支持的CVM方法及其优先顺序；
			editor.putString("CVM", "");// TODO
			// 发卡方在验证密文时所使用的发卡方主密钥分散索引

			// 5） 闪付交易流程参数：绑定在某一账户下面，仅对绑定账户有效。参见6.3.3；

			// 6） 账户动态参数：当阀值限制超过时，该参数集需申请更新。参见6.3.4；

			long timeMillis = System.currentTimeMillis();
			String timeNow = new Timestamp(timeMillis).toString();
			String YY = timeNow.substring(2, 4);
			String MM = timeNow.substring(5, 7);
			String DD = timeNow.substring(8, 10);
			String HH = timeNow.substring(11, 13);
			// 账户动态参数索引：CCCCYYMMDDHHNN，在第8章中定义

			editor.putString("DynamicIndex", "0000" + YY + MM + DD + HH + "00");
			// 二磁道等效数据:在qUICS交易流程中的GPO命令响应中返回
			editor.putString("TrackTwo", "8888010010000146651D22126210403323");
			// 限制密钥：用于生成应用密文。在第8.1章节中定义
			editor.putString("KeyLimit", "25365268945236540129985012365725");

			// 参数使用时间
			editor.putString("now_timeMillis", timeMillis+"");
//			editor.putLong("now_timeMillis", timeMillis);
			// 闪付交易次数 某账户动态参数集（可由CCCCYYMMDDHHNN索引）成功进行闪付交易的的次数
			editor.putString("now_QuickTimes", "0000");
			// 累计金额 特定账户动态参数集 （可由CCCCYYMMDDHHNN索引） 发生qUICS交易的累计交易金额
			editor.putString("now_ThresholTotal", "0000");

			// 参数更新计数器 某账户成功进行账户动态参数集更新的次数
			editor.putString("SequenceCounter", "0000");
			// 云账户设备阀值参数：移动设备所绑定的特定账户的风险控制参数，包括：
			// 阀值限制数
			editor.putString("Thresholdlimits", "100");
			// 阀值相关的交易次数计数器和交易金额累计器
			editor.putString("ThresholATC", "0");
			editor.putString("ThresholTotal", "0");

			editor.commit();
		
	}

	public String cardUpdate() throws Exception {
		// 移动应用向云端支付平台所提供参数更新请求数据至少应包括以下内容：
		// 1） 上一次接收到的账户动态参数索引（CCCCYYMMDDHHNN） ；
		// 2） 参数更新计数器（Squence Number） ：截止当前时间前，已成功完成的账户参数更新的次数；
		// 3） 闪付交易列表：包括对应账户每笔闪付交易的时间戳、交易不可预知数以及发行者要求的其
		// 他交易相关数据；
		// 4） 使用限制密钥对以上数据进行加密得到的MAC值；
		SharedPreferences sp0 = context.getSharedPreferences(FILECITIC, Activity.MODE_ENABLE_WRITE_AHEAD_LOGGING);
		if (!sp0.getBoolean("isOpen", false)) {

			return "";
		}
		String packageName = sp0.getString("packageName", "");
		Context c = context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY);

		SharedPreferences sp = c.getSharedPreferences(FILECITIC,
				Activity.MODE_ENABLE_WRITE_AHEAD_LOGGING);
		// 上一次接收到的账户动态参数索引
		String dynamicIndex = sp.getString("DynamicIndex", "");
		dynamicIndex = "0" + dynamicIndex;
		// 参数更新计数器
		String squenceNumber = sp.getString("SequenceCounter", "");
		int parseInt = Integer.parseInt(squenceNumber, 16);
		if (parseInt > 0xffff) {
			// TODO 账号更新次数最大需要重置
			return "1";
		}
		// TODO 交易验证日志（每笔闪付交易） ：
		// 闪付交易列表：包括对应账户每笔闪付交易的时间戳、交易不可预知数以及发行者要求的其他交易相关数据；
		DatabaseDao dao = new DatabaseDao(context);
		List<JSONObject> logList = dao.findAll();

		// TODO
		// 使用限制密钥对以上数据进行加密得到的MAC值

		// 参数更新计数器
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("SequenceCounter", (parseInt + 1) + "");

		// SharedPreferences.Editor editor =
		// context.getSharedPreferences("citic_hce",
		// Activity.MODE_PRIVATE).edit();
		// CCCC：参数更新计数器，当前值为参数更新的累计次数，由移动应用维护并在参数更新请求
		// 中上送， （参见6.4.3.1） ，取值为范围:0x0000~0xFFFF；
		//  YY：年份，范围为00~99；
		//  MM: 月份，范围为01~12；
		//  DD：日期，范围为01~31；
		//  HH：小时，范围为00~23；
		//  NN：当前小时内每次密钥更新加一,当前值为一小时内参数更新的次数，范围：0x00~0xFF
		//  80：固定填充位
		long timeMillis = System.currentTimeMillis();
		String timeNow = new Timestamp(timeMillis).toString();
		String YY = timeNow.substring(2, 4);
		String MM = timeNow.substring(5, 7);
		String DD = timeNow.substring(8, 10);
		String HH = timeNow.substring(11, 13);
		int NN;
		String stringShold = sp.getString("Threshold", "");
		if (stringShold.length() > 0) {
			if (Long.parseLong(stringShold.substring(0, 13)) + 3600000l > timeMillis) {
				NN = sp.getInt("NN", 0);
				if (NN < 0xFF) {
					NN = NN + 1;
				} else {
					// TODO 达到最大值
					NN = 0x0;
				}
			} else {
				NN = 0x0;
			}
		} else {
			NN = 0x0;
		}
		editor.putString("NN", NN+"");
		editor.putString("DynamicIndex", "0001" + YY + MM + DD + HH + NN + "80");

		editor.putString("TrackTwo", "8888010010000146651D22126210403323");

		editor.putString("KeyLimit", "25365268945236540129985012365725");

		// Timestamp tsTemp = new Timestamp(timeMillis);
		// String ts = tsTemp.toString();

		// 1） 参数使用时间：由时间戳标识的当前账户动态参数的有效存在时间，见第6.3.5.2中定义；
		// 2） 闪付交易次数：某账户动态参数集（可由CCCCYYMMDDHHNN索引）成功进行闪付交易的的次数；
		// 3） 累计金额： 特定账户动态参数集 （可由CCCCYYMMDDHHNN索引） 发生qUICS交易的累计交易金额；
		// 4） 特定条件下的交易次数，或特定条件下的累计金额：如限制在qUICS国际交易或国内交易的场
		// 景下发生闪付交易的累计次数或金额。
		// 5） 其他由发卡方或云端支付平台定义的参数。
		// 参数使用时间和闪付交易次数 timeMillis=1440570184333 累计金额=000200000000 特定条件下的交易次数 =
		// 0005
		editor.putString("Threshold", timeMillis + "0008" + "000200000000"
				+ "0005");
		editor.commit();
		return "0";
	}

	public void applyCard() {

		ApplyCard applyCard = new ApplyCard();
		applyCard.init();
		// TODO 网络请求
		applyCard.getResult();

	}

}
