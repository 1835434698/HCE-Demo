package com.tangzy.cn.hce.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class FileControl {
	
	String filenameTemp ="/data/data/com.tangzy.cn.hce/log" ;
    
    //创建文件夹及文件  
    public void CreateText() throws IOException {  
    	
//    	另外提供了新的函数： 
//    	Environment.getExternalStoragePublicDirectory()，通过此函数获取保存目录后，保存在此目录的文件，程序卸载时，不会被自动删除。 
    	
        File file = new File(filenameTemp);  
        if (!file.exists()) {  
            try {  
                //按照指定的路径创建文件夹  
                file.mkdirs();  
            } catch (Exception e) {  
                // TODO: handle exception  
            }  
        }  
        File dir = new File(filenameTemp+"/log.txt");  
        if (!dir.exists()) {  
              try {  
                  //在指定的文件夹中创建文件  
                  dir.createNewFile();  
            } catch (Exception e) {  
            }  
        }  
  
    }  
      
    //向已创建的文件中写入数据  
    public void print(String str) {  
        FileWriter fw = null;  
        BufferedWriter bw = null;  
        String datetime = "";  
        try {  
            SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " "  
                    + "hh:mm:ss");  
            datetime = tempDate.format(new java.util.Date()).toString();  
            fw = new FileWriter(filenameTemp, true);//  
            // 创建FileWriter对象，用来写入字符流  
            bw = new BufferedWriter(fw); // 将缓冲对文件的输出  
            String myreadline = datetime + "[]" + str;  
              
            bw.write(myreadline + "\n"); // 写入文件  
            bw.newLine();  
            bw.flush(); // 刷新该流的缓冲  
            bw.close();  
            fw.close();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            try {  
                bw.close();  
                fw.close();  
            } catch (IOException e1) {  
                // TODO Auto-generated catch block  
            }  
        }  
    }  
	
	
	
	

}
