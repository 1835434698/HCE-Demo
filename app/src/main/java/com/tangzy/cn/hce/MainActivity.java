package com.tangzy.cn.hce;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

import com.tangzy.cn.hce.control.CardControl;
import com.tangzy.cn.hce.db.DatabaseDao;
import com.tangzy.cn.hce.util.FileControl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	
	private Button button1;
	private Button button2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(this);
		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(this);
		
//		try {
//			DatabaseDao dao = new DatabaseDao(this);
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("timeMillis", "1440570184333");
//			jsonObject.put("codeNum", "0C4C6FEC");
//			jsonObject.put("atc", "0004");
//			dao.add(jsonObject );
//			jsonObject = new JSONObject();
//			jsonObject.put("timeMillis", "1440570184334");
//			jsonObject.put("codeNum", "0C4C6FE4");
//			jsonObject.put("atc", "0014");
//			dao.add(jsonObject );
//			jsonObject = new JSONObject();
//			jsonObject.put("timeMillis", "1440570184314");
//			jsonObject.put("codeNum", "0C4C61E4");
//			jsonObject.put("atc", "0024");
//			dao.add(jsonObject );
//			jsonObject = new JSONObject();
//			jsonObject.put("timeMillis", "1420570184334");
//			jsonObject.put("codeNum", "024C6FE4");
//			jsonObject.put("atc", "0034");
//			dao.add(jsonObject );
//			
//			dao.findAll();
////			dao.qure();
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

//		CardControl cardControl = new CardControl(this);
//
//		try {
//			cardControl.cardInit();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


	}

	@Override
	public void onClick(View v) {
//		CardControl cardControl = new CardControl(this);
//		switch (v.getId()) {
//		case R.id.button1:
//			try {
//				cardControl.cardInit();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			
//			break;
//		case R.id.button2:
////			cardControl.cardUpdate();
////			FileControl fileControl = new FileControl();
////			try {
////				fileControl.CreateText();
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//			
//			break;
//
//		default:
//			break;
//		}
		
	}

}
