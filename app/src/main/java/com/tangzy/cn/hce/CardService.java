package com.tangzy.cn.hce;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.tangzy.cn.hce.util.Util;

/**
 * This is a sample APDU Service which demonstrates how to interface with the
 * card emulation support added in Android 4.4, KitKat.
 * <p/>
 * <p/>
 * This sample replies to any requests sent with the string "Hello World". In
 * real-world situations, you would need to modify this code to implement your
 * desired communication protocol.
 * <p/>
 * <p/>
 * This sample will be invoked for any terminals selecting AIDs of 0xF11111111,
 * 0xF22222222, or 0xF33333333. See src/main/res/xml/aid_list.xml for more
 * details.
 * <p/>
 * <p class="note">
 * Note: This is a low-level interface. Unlike the NdefMessage many developers
 * are familiar with for implementing Android Beam in apps, card emulation only
 * provides a byte-array based communication channel. It is left to developers
 * to implement higher level protocol support as needed.
 */
public class CardService extends HostApduService {
    private static final String TAG = "CardService";

    private static final String SELECT_APDU_HEADER = "00A40400";
    // "OK" status word sent in response to SELECT AID command (0x9000)
    private static final byte[] SELECT_OK_SW = HexStringToByteArray("9000");
    // "UNKNOWN" status word sent in response to invalid APDU command (0x0000)
    private static final byte[] UNKNOWN_CMD_SW = HexStringToByteArray("0000");

    private static final byte[] SELECT_Error_SW = HexStringToByteArray("");

    // DFN_PPSE获取AID
    // protected final byte[] DFN_PPSE = { 50, 80, 65, 89, 46, 83, 89, 83,
    // 46, 68, 68, 70, 48, 49 };
    protected final static byte[] DFN_PPSE = {(byte) '2', (byte) 'P',
            (byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Y',
            (byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F',
            (byte) '0', (byte) '1',};// 50, 80, 65, 89, 46, 83, 89, 83, 46, 68,
    // 68, 70, 48, 49
    private static String AID = "D156000999010101";
    MyCount myCount = new MyCount(30000, 1000);
    private boolean isPPSE = false;
    private boolean isAID = false;
    private static String FILECITIC = "citic_hce";

    protected final static byte[] DFI_MF = {(byte) 0x3F, (byte) 0x00};

    private static String Balance = "9F7906000000000000";
    private static String LimitSingle = "9F7806000000100000";
    private static String LimitAmount = "9F7706000000200000";
    private static String ATCOnLine = "9F1302000F";
    private static String ATCOnLine1 = "9F1302000F";
    private static String ATC = "9F3602000F";
    private static String ATC1 = "9F3602000F";
    private static String CodeCurrency = "9F51020156";
    private static String FormatJournal = "9F4F199A039F21039F02069F03069F1A025F2A029F4E149C019F3602";
    // 发卡行国家代码
    private static String String5F28 = "0156";
    // 失效日期
    private static String String5F24 = "200131";
    // 生效日期
    private static String String5F25 = "150101";
    // 应用 PAN
    private static String String5F34 = "01";
    // 应用 应用版本号
    private static String String9F08 = "0030";
    // 服务码
    private static String String5F30 = "0201";
    // 允许脱机金额
    private static String String9F5D = "000000000000";

    // 卡号
    // private static String CARDNO = RelatedCardAc.cardNo;

    private boolean cardsign = false;
    private String CARDNOlenth = "08";
    private String CARDNO = "6225768744046633";


    /*“6F”  FCI 模板  变长  M     6F 21
        “84”  “2PAY.SYS.DDF01”  0E  M    84 0E
        “A5”  FCI 专用模板  变长  M        A5 0F
            “BF0C”  FCI 发卡方自定义数据  变长  M  BF0C 0C
                “61”  目录入口  变长  M    61 0A
                “4F”  DF 名（AID）  07-08  M   4F 08 D156000999010101*/
    private static final String FCI = "6F21840E325041592E5359532E4444463031A50FBF0C0C610A4F08"
            + AID;

//    private static String AIDFile = "6F548408"
//            + AID
//            + "A548500A50424F432044454249548701019F38189F66049F02069F03069F1A0295055F2A029A039C019F37045F2D027A689F1101019F120A50424F43204445424954BF0C059F4D020B0A";



    /**
     * Called if the connection to the NFC card is lost, in order to let the
     * application know the cause for the disconnection (either a lost link, or
     * another AID being selected by the reader).
     *
     * @param reason Either DEACTIVATION_LINK_LOSS or DEACTIVATION_DESELECTED
     */
    @Override
    public void onDeactivated(int reason) {
        Log.i(TAG, "--onDeactivated--" + reason);

    }

    String stringApdu;
    String stringApdu0_4;
    String stringApdu0_8;
    String stringApdu0_8lenth;
    String selectByName = "00A40400";
    String getProcessingOptions = "80A80000";
    String getData = "80CA";
    static String DFN_ppse = "325041592E5359532E4444463031";
    String DFN_pse = "315041592E5359532E4444463031";
    String GPO1;
    String fileInfo = "00B2";

    /**
     * This method will be called when a command APDU has been received from a
     * remote device. A response APDU can be provided directly by returning a
     * byte-array in this method. In general response APDUs must be sent as
     * quickly as possible, given the fact that the user is likely holding his
     * device over an NFC reader when this method is called.
     * <p/>
     * <p class="note">
     * If there are multiple services that have registered for the same AIDs in
     * their meta-data entry, you will only get called if the user has
     * explicitly selected your service, either as a default or just for the
     * next tap.
     * <p/>
     * <p class="note">
     * This method is running on the main thread of your application. If you
     * cannot return a response APDU immediately, return null and use the
     * {@link #sendResponseApdu(byte[])} method later.
     *
     * @param commandApdu The APDU that received from the remote device
     * @param extras      A bundle containing extra data. May be null.
     * @return a byte-array containing the response APDU, or null if no response
     * APDU can be sent at this point.
     */
    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
    	Log.d(TAG, "commandApdu=" + ByteArrayToHexString(commandApdu));
    	
    	SharedPreferences sp = this.getSharedPreferences(FILECITIC, Activity.MODE_PRIVATE);
    	if (!sp.getBoolean("isOpen", false)) {
    		//开关未打开
    		return ConcatArrays(HexStringToByteArray("6984"), SELECT_Error_SW); 
		}
    	
        try {
            stringApdu = ByteArrayToHexString(commandApdu);
            stringApdu0_4 = stringApdu.substring(0, 4);
            stringApdu0_8 = stringApdu.substring(0, 8);
            stringApdu0_8lenth = stringApdu.substring(8, 10);
            
            if (stringApdu0_8.equals(selectByName)) {
            	Log.d(TAG, "selectByName");
                if (stringApdu0_8lenth.equals("0E")){
                    if (stringApdu.contains(DFN_ppse)) {
                    	Log.d(TAG, "DFN_ppse");
                        isPPSE = true;
                        myCount.start();

                        Log.d(TAG, "FCI="+FCI);
                        return ConcatArrays(HexStringToByteArray(FCI), SELECT_OK_SW);
                    }else {
                    	Log.d(TAG, "FCI="+"6D00");
                        return ConcatArrays(HexStringToByteArray("6D00"),SELECT_Error_SW);
                    }
                }else if(stringApdu0_8lenth.equals("08")){
                    if (isPPSE){
                        if (stringApdu.contains(AID)) {
                        	Log.d(TAG, "AID");
                            isAID = true;
                    /*
                    * ‘6F’  FCI 模板  M      6F 54
                       ‘84’  DF 名  M        84 08 D156000999010101
                       ‘A5’  FCI 数据专用模板  M      A5 48
                       ‘50’  应用标签  M         50 0A 50424F43204445424954
                       ‘87’  应用优先指示器  O         87 01 01
                       ‘9F38’  PDOL  O          9F38 18 9F66 04  9F02 06  9F03 06  9F1A 02  95 05  5F2A 02  9A 03  9C 01  9F37 04
                       ‘5F2D’  首选语言  O      5F2D 02 7A68
                       ‘9F11’  发卡方代码表索引  O      9F11 01 01
                       ‘9F12’  应用优先名称  O        9F12 0A 50424F43204445424954
                       ‘BF0C’  发卡方自定义数据（FCI）        BF0C 05
                        '9F4D'   交易日志入口     9F4D  02  0B0A
                    *
                    *
                    * PDOL
                    *  9F38 18
                    *  9F66 04  终端交易属性
                     * 9F02 06  授权金额 9F03 06  其它金额
                     * 9F1A 02  终端国家代码
                     * 95 05    终端验证结果（TVR）
                     * 5F2A 02  交易货币代码
                     * 9A 03    交易日期
                      * 9C 01   交易类型
                      * 9F37 04  不可预知数
                    *
                    * */
                            String AIDFile = "6F548408"
                                    + AID
                                    + "A548500A50424F432044454249548701019F38189F66049F02069F03069F1A0295055F2A029A039C019F37045F2D027A689F1101019F120A50424F43204445424954BF0C059F4D020B0A";

                            Log.d(TAG, "AIDFile="+AIDFile);
                            return ConcatArrays(HexStringToByteArray(AIDFile), SELECT_OK_SW);
                        }

                    }else {
                        return ConcatArrays(HexStringToByteArray("6985"), SELECT_Error_SW);
                    }
                }else {
                    return ConcatArrays(HexStringToByteArray("6283"), SELECT_Error_SW);
                }
            }else if (stringApdu0_8.equals(getProcessingOptions)) {
                if (isPPSE && isAID) {
                	Log.d(TAG, "DFN_ppse");

                	String provideUri = sp.getString("provideUri", "");
                	
                	
                	Uri uri = Uri.parse("content://"+provideUri+"/card/19");
        			ContentResolver resolver = this.getContentResolver();  
        			Cursor cursor = resolver.query(uri, null, null, null, null);
        			String stringATC = null;
        			String trackTwo = null;
        			String DynamicIndex = null;
        			while (cursor.moveToNext()) {
        				 stringATC = cursor.getString(0);
        				 trackTwo = cursor.getString(1);
        				 DynamicIndex = cursor.getString(2);
        				 
        			}
        			cursor.close();
        			
                    int ATC = Integer.parseInt(stringATC, 16);
                    if (ATC < 0xFFFF) {
                        ATC = ATC + 1;
                        //80A8000023 832136000080000000000001000000000000015600000000000156150731000C4C6FEC 00
                        //8321  36000080  000000000001  000000000000  0156  0000000000  0156  150731  00  0C4C6FEC
                        String GPO = stringApdu.substring(10, stringApdu.length() - 2);
                        if (GPO.length() == 70) {
                            String string9F66 = GPO.substring(4, 12);
                            String binary9F66 = Util.hexString2binaryString(string9F66);
                            if (binary9F66.substring(1, 2).equals("0") && binary9F66.substring(2, 3).equals("1") && binary9F66.substring(4, 5).equals("0")) {
                                //下面的都用于卡片密文计算
                                String string9F02 = GPO.substring(12, 24);
                                String string9F03 = GPO.substring(24, 36);
                                String string9F1A = GPO.substring(36, 40);
                                String string95 = GPO.substring(40, 50);
                                String string5F2A = GPO.substring(50, 54);
                                String string9A = GPO.substring(54, 60);
                                String string9C = GPO.substring(60, 62);
                                String string9F37 = GPO.substring(62, 70);
    //
                                String string9F6C = Util.binaryString2hexString("0000000000000000");
                                String CVR = Util.binaryString2hexString("0000000000000000");
                                if (binary9F66.substring(9, 10).equals("1")) {
//                                if (binary9F66.substring(9, 10).equals("0")) {
                                    //检查并比较6.3.2中所定义的账户配置参数中的CVM数据
                                    if (binary9F66.substring(5, 6).equals("1")) {
                                        //string9F6C = 10000000
                                        string9F6C = Util.binaryString2hexString("0000000010000000");
                                        //卡片验证结果 CVR = 01101110
                                        CVR = Util.binaryString2hexString("0110111000000000");
                                        //TODO 7.4.4.1
                                        String gpo2 = getGPO(trackTwo, DynamicIndex, ATC+"", CVR, string9F6C);
                                        Log.d(TAG, "GPO="+gpo2);
                                        return ConcatArrays(HexStringToByteArray(gpo2), SELECT_OK_SW);

                                    } else if (binary9F66.substring(17, 18).equals("1")){
                                        string9F6C = Util.binaryString2hexString("1000000000000000");
                                        // 移动应用按照表10.2的定义设置卡片验证结果 （CVR） 第1字节第8~5位为CD-CVM的验证实体， ；
                                        //   移动应用按照表10.2的定义设置卡片验证结果 （CVR） 第1字节第4~1位为CD-CVM的输入类型，
                                        CVR = Util.binaryString2hexString("011011100000000000000000");
                                        //TODO 7.4.4.1
                                        //TODO 用户输入界面；
                                        return ConcatArrays(HexStringToByteArray("6984"), SELECT_Error_SW);
                                    }else if (binary9F66.substring(6, 7).equals("1")){
                                        string9F6C = Util.binaryString2hexString("0100000000000000");
                                        CVR = Util.binaryString2hexString("0110110100000000");
                                        //TODO 7.4.4.1
                                        String gpo2 = getGPO(trackTwo, DynamicIndex, ATC+"", CVR, string9F6C);
                                        Log.d(TAG, "GPO="+gpo2);
                                        return ConcatArrays(HexStringToByteArray(gpo2), SELECT_OK_SW);
                                    }else{
                                        string9F6C = Util.binaryString2hexString("0000000010000000");
                                        CVR = Util.binaryString2hexString("0000000000000000");
                                        //TODO 7.4.4.1
                                        String gpo2 = getGPO(trackTwo, DynamicIndex, ATC+"", CVR, string9F6C);
                                        Log.d(TAG, "GPO="+gpo2);
                                        return ConcatArrays(HexStringToByteArray(gpo2), SELECT_OK_SW);
                                    }
                                } else {
//                                	如果移动应用和终端无共同支持的CVM，或终端未请求CVM（终端交易属性第2字节第7位为‘0b’ ） ，
//                                	则移动应用应
                                	string9F6C = Util.binaryString2hexString("0000000010000000");
                                    //卡片验证结果 CVR = 00000000
                                    CVR = Util.binaryString2hexString("0000000000000000");
                                    //TODO 7.4.4.1
                                    String gpo2 = getGPO(trackTwo, DynamicIndex, ATC+"", CVR, string9F6C);
                                    Log.d(TAG, "GPO="+gpo2);
                                    return ConcatArrays(HexStringToByteArray(gpo2), SELECT_OK_SW);
                                }
                            } else {
                                //TODO 9F66 不正确
                                return ConcatArrays(HexStringToByteArray("6985"), SELECT_Error_SW);
                            }
                        } else {
                            //todo GPO错误
                            return ConcatArrays(HexStringToByteArray("6985"), SELECT_Error_SW);
                        }
                    }else {
                        //TODO ATC最大值
                        return ConcatArrays(HexStringToByteArray("6985"), SELECT_Error_SW);
                    }
                    //校验失败返回
                }else {
                    return ConcatArrays(HexStringToByteArray("6985"), SELECT_Error_SW);
                }
            } else {
                return ConcatArrays(HexStringToByteArray("6D00"),SELECT_Error_SW);
            }
        }catch (Exception e){
//            任何原因导致的无法成功执行命令，如无特殊说明，则可统一返回一个错误的状态码"6984"
            return ConcatArrays(HexStringToByteArray("6984"), SELECT_Error_SW);
        }
        return ConcatArrays(HexStringToByteArray("6D00"), SELECT_Error_SW);
    }

    private String getGPO(String trackTwo, String dynamicIndex,String ATC,String CVR, String string9F6C){
        String GPO="";
        String AIP = "0040";
        while (ATC.length() < 4) {
			ATC = "0"+ATC;
			
		}								
//        String trackTwo = sp.getString("TrackTwo", "");
        String trackTwoLenth = Integer.toHexString(trackTwo.length()/2);
//        见6.3.2账户配置参数  分散密钥索引，用于索引密文计 算使用的密钥
        //TODO 限制秘钥版本
        String DKI = "01";
//        高4位：‘A’ ：云端支付密文标识
//        低4位：‘1’ ：限制密钥生成算法1 参见8.2.1
//           ‘2’ ：限制密钥生成算法2 参见8.2.2
        String CVN = "A1";
//        6-5   00：GPO中返回AAC；  01：GPO中返回TC；  10：GPO中返回ARQC； 11：应用无效；
        String CVR1 = "03"+Util.binaryString2hexString("00100000")+CVR+Util.binaryString2hexString("00000000");
        String IDYinLian = "00000000";
        String string9F10 = "07"+DKI+CVN+CVR1+"010DA1"+IDYinLian+dynamicIndex;
//        字节 1－字节 8： 银行标识码
//        字节 9：行业应用类型
//        字节 10：移动支付规范保留；
//        字节 11：发卡行保留；FE238000
//        字节 12~14：本规范保留；
//        字节 15~16：发卡行保留
        String string9F63 = Util.binaryString2hexString("00000000000000000000000000000000000000000000000000000000000000000000000000000000")+"FE238000"+Util.binaryString2hexString("0000000000000000");
        String length = Integer.toHexString(83+trackTwo.length()/2);
        GPO = "77"+length+"8202"+AIP+"9F3602"+ATC+"57"+trackTwoLenth + trackTwo+"9F1016"+string9F10+"9F270180"+"9F26"+"09123654895522135225"+"9F630F"+string9F63+"5F34"+"0401010101"+"9F6C02"+string9F6C;
        						
//      GPO = "77"+"length"+"8202"+AIP+"9F3602"+ATC+"57"+trackTwoLenth + trackTwo+"9F1016"+string9F10+"9F270180"+"9F26"+"lenth"+"9F6316"+string9F63+"5F34"+"0101"+"9F6C02"+string9F6C;


        //todo    交易验证日志
        return GPO;
    }



    /**
     * Build APDU for SELECT AID command. This command indicates which service a
     * reader is interested in communicating with. See ISO 7816-4.
     *
     * @param aid Application ID (AID) to select
     * @return APDU for SELECT AID command
     */
    public static byte[] BuildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH |
        // DATA]
        return HexStringToByteArray(SELECT_APDU_HEADER
                + String.format("%02X", aid.length() / 2) + aid);
    }

    /**
     * Utility method to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex
        // characters (nibbles)
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned
            // value
            hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from
            // upper nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character
            // from lower nibble
        }
        return new String(hexChars);
    }

    /**
     * Utility method to convert a hexadecimal string to a byte string.
     * <p/>
     * <p/>
     * Behavior with input strings containing non-hexadecimal characters is
     * undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     * @throws IllegalArgumentException if input length is incorrect
     */
    public static byte[] HexStringToByteArray(String s)
            throws IllegalArgumentException {
        int len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException(
                    "Hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2]; // Allocate 1 byte per 2 hex characters
        for (int i = 0; i < len; i += 2) {
            // Convert each character into a integer (base-16), then bit-shift
            // into place
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Utility method to concatenate two byte arrays.
     *
     * @param first First array
     * @param rest  Any remaining arrays
     * @return Concatenated copy of input arrays
     */
    public static byte[] ConcatArrays(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    //
    // @Override
    // public boolean onUnbind(Intent intent) {
    // return super.onUnbind(intent);
    // }
    //
    //
    // public class MyBinder extends Binder{
    // public void MyMethod(){
    //
    // Log.i(TAG, "--MyMethod--");
    // }
    // }

    public static byte[] selectByName(byte... name) throws IOException {
        ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
        buff.put((byte) 0x00) // CLA Class
                .put((byte) 0xA4) // INS Instruction
                .put((byte) 0x04) // P1 Parameter 1
                .put((byte) 0x00) // P2 Parameter 2
                .put((byte) name.length) // Lc
                .put(name).put((byte) 0x00); // Le
        return buff.array();
    }

    public byte[] selectByID(byte... id) throws IOException {
        ByteBuffer buff = ByteBuffer.allocate(id.length + 6);
        buff.put((byte) 0x00) // CLA Class
                .put((byte) 0xA4) // INS Instruction
                .put((byte) 0x00) // P1 Parameter 1
                .put((byte) 0x00) // P2 Parameter 2
                .put((byte) id.length) // Lc
                .put(id).put((byte) 0x00); // Le

        return buff.array();
    }

    private static byte[] selectBalancePboc20() {
        final byte[] cmd = {(byte) 0x80, // CLA Class
                (byte) 0xCA, // INS Instruction
                (byte) 0x9F, // P1 Parameter 1
                (byte) 0x79, // P2 Parameter 2
                (byte) 0x00, // Le
        };
        return cmd;
    }

    public static byte[] readRecord(int index) {
        final byte[] cmd = {(byte) 0x00, // CLA Class
                (byte) 0xB2, // INS Instruction
                (byte) index, // P1 Parameter 1
                (byte) 0x0C, // P2 Parameter 2
                (byte) 0x00, // Le
        };

        return cmd;
    }

    public static byte[] getData(short tag) throws IOException {
        final byte[] cmd = {(byte) 0x80, // CLA Class
                (byte) 0xCA, // INS Instruction
                (byte) ((tag >> 8) & 0xFF), (byte) (tag & 0xFF), (byte) 0x00, // Le
        };

        return cmd;
    }

    public byte[] readRecord(int sfi, int index) throws IOException {
        final byte[] cmd = {(byte) 0x00, // CLA Class
                (byte) 0xB2, // INS Instruction
                (byte) index, // P1 Parameter 1
                (byte) ((sfi << 3) | 0x04), // P2 Parameter 2
                (byte) 0x00, // Le
        };
        byte Y = (byte) ((sfi << 3) | 0x04);

        return cmd;
    }

    public byte[] readBinary(int sfi) throws IOException {
        final byte[] cmd = {(byte) 0x00, // CLA Class
                (byte) 0xB0, // INS Instruction
                (byte) (0x00000080 | (sfi & 0x1F)), // P1 Parameter 1
                (byte) 0x00, // P2 Parameter 2
                (byte) 0x00, // Le
        };

        return cmd;
    }


    public class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            isPPSE = false;
            isAID = false;
//            bt_timer.setText("重新发送");
        }

        @Override
        public void onTick(long millisUntilFinished) {
//            bt_timer.setText(millisUntilFinished / 1000 + "秒后重发");
        }

    }


}

