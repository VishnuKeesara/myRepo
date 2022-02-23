
package schemagen;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Random;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;

/**
 *
 * @author SKatta
 */
public class Dbschem {

    static String str = "jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.BATCH_ID\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.EMAIL_NOTICE_TMS\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.ENGINE_POSITN_CD\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.ENGINE_TYPE_TXT\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.HR_SINCE_LAST_OIL_ADD_NBR\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.JOB_ID\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.MATL_ID\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.MOSAIC_ROW_UPDT_TMS\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.OIL_ADD_DETL_ACL_PINT_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.OIL_ADD_DETL_NDCL_PINT_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.OIL_ADD_ID\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.OIL_ADD_PREDCT_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.OIL_AVG_RUN_ALERT_CD\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.OIL_PINTS_ADD_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.OIL_SPOT_RATE_ALERT_CD\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.OIL_SUDDEN_INCREASE_ALERT\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.OIL_SUDDEN_INCREASE_METHOD_TXT\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_AVG_RATE_CONSMPTN_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_MINUTE_SINCE_INSTAL_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_RUN_AVG_HIGH_RANGE_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_RUN_AVG_NORMAL_RANGE_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_SERIAL_ID\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_SPOT_CONSMPTN_RATE_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_SPOT_OIL_HIGH_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_SPOT_OIL_NORMAL_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_SRVC_OIL_ADD_DETL_ID\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_SRVC_PRSNEL_ID\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_SUDDEN_INCRS_CONSMPTN_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_TTL_AVG_FLT_TM_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_TTL_AVG_OIL_PINT_USED_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_TTL_SPOT_FLT_TM_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.PART_TTL_SPOT_OIL_PINT_USD_QTY\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.RED_ALERT_IND\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.RUN_ID\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.SRC_ROW_CREATE_TMS\n" +
"jpINPUT_SOURCE_DB.PART_SRVC_OIL_ADD_DETL.YELLOW_NOTICE_IND";

    static List<String> eleList = new ArrayList<>();
    static List<String> eList = new ArrayList<>();
    static Set<String> intset = new HashSet<>();

    static String s = "";
    static int m;

    public static Timestamp getRandomTime() {

        Random r = new Random();
        int Low = 100;
        int High = 1500;
        int Result = r.nextInt(High - Low) + Low;
        int ResultSec = r.nextInt(High - Low) + Low;

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -Result);
        calendar.add(Calendar.SECOND, -ResultSec);

        java.sql.Timestamp ts = new java.sql.Timestamp(calendar.getTimeInMillis());
        return ts;
    }

    public static void main(String[] args) {
        if (str.contains(".dbo")) {
            str = str.replaceAll(".dbo", "");
        }
        if (str.contains(".dematicemsp")) {
            str = str.replaceAll(".dematicemsp", "");
        }

        String p = str.replaceAll("\n", ",");

        Set<String> eLists = new HashSet<>();
        Map<String, Set<String>> hmap
                = new HashMap();
        eleList = Arrays.asList(p.split(","));

        for (int j = 0; j < eleList.size(); j++) {
            if (j != eLists.size()) {

            }
            int length = eleList.get(j).split("\\.").length;
            String key = eleList.get(j).split("\\.")[length - 2];
            String value = eleList.get(j).split("\\.")[length - 1];
            if (value.toLowerCase().endsWith("_dt") || value.toLowerCase().endsWith("date")) {
                eLists.add(value.toLowerCase() + "  Date");
            } else if (value.toLowerCase().endsWith("_tms")) {

                eLists.add(value.toLowerCase() + "  timestamp");
            } else if (value.toLowerCase().endsWith("_hrs") || value.toLowerCase().endsWith("time") || value.toLowerCase().endsWith("_tm")) {
                eLists.add(value.toLowerCase() + "  Time");
            } else if (value.toLowerCase().endsWith("_id") || value.toLowerCase().endsWith("id") || value.toLowerCase().endsWith("_unit") || value.toLowerCase().endsWith("_qty") || value.toLowerCase().endsWith("_nbr")||value.toLowerCase().endsWith("_number")||value.toLowerCase().endsWith("_num")) {
                eLists.add(value.toLowerCase() + "  int");
            } else {
                eLists.add(value.toLowerCase() + "  Varchar(20)");
            }
            if (hmap.get(key) == null) {
                hmap.put(key, eLists);
                eLists = new HashSet();
            } else {
                Set<String> temp = hmap.get(key);
                temp.addAll(eLists);
                eLists = new HashSet();
            }

        }
//        System.out.println("hm" + eLists
//        );
//        List<String> stringsList = new ArrayList<>(eLists);
//         int pos =stringsList.indexOf("_int");
//         System.out.println("pos  " +pos);
//stringsList.get(0); // "string1";
//stringsList.get(1);
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        for (Map.Entry<String, Set<String>> entry : hmap.entrySet()) {
            sb.append("create table DXC." + entry.getKey() + "(" + "\n");

            Set<String> value = entry.getValue();
            String lastEle = "";
            for (String x : value) {
                lastEle = x;
            }
            for (String st : value) {
                if (lastEle.equalsIgnoreCase(st)) {
                    sb.append(st + "\n");

                } else {
                    sb.append(st + "," + "\n");

                }
            }
            sb.append(");" + "\n");
            int k = 1;
            int count = 0;
            for (k = 1; k < 3; k++) {

                sb.append("Insert into DXC." + entry.getKey() + " values" + "(");
                sb2.append("(");
                for (String insertvalue : value) {

                    if (insertvalue.endsWith("int")) {
//                        if (count == 0) {
//                            intset.add(k);
                            sb.append(k);
                            sb.append(",");
                            sb2.append(k);
//                             intset.add(k);
                            sb2.append(",");
                            count++;
//                        } else {
//
//                            String numbers = "1234567890";
//                            Random random = new Random();
//                            char[] otp = new char[4];
//
//                            for (int j = 0; j < 4; j++) {
//                                otp[j] = numbers.charAt(random.nextInt(numbers.length()));
//                            }
//                            int a = Integer.parseInt(String.valueOf(otp));
////                        intset.add(a);
//                            sb.append(otp);
//                            sb.append(",");
//                            sb2.append(otp);
//                            sb2.append(",");
//                            intset.add(otp.toString());
//                        }

                    } else if (insertvalue.endsWith("Time")) {
                        final Random random = new Random();
                       // final int millisInDay = 24  60  60 * 1000;
                    //    Time time = new Time((long) random.nextInt(millisInDay));
                        sb.append("'" + "03:46:41" + "'" + ",");
                        sb2.append("'" + "03:46:41" + "'" + ",");
//                        intset.add(time.toString());
                    } else if (insertvalue.endsWith("Date")) {
                        LocalDate createRandomDate = RandomDate.createRandomDate(1900, 2000);
                        sb.append("'" + "1981-04-15" + "'" + ",");
                        sb2.append("'" + "1981-04-15" + "'" + ",");

                        intset.add(createRandomDate.toString());
                    } else if (insertvalue.endsWith("timestamp")) {
                        Timestamp randomTime = getRandomTime();
                        sb.append("'" + "1943-03-25 05:33:30" + "'" + ",");
                        sb2.append("'" + "1943-03-25 05:33:30" + "'" + ",");
                        intset.add(randomTime.toString());

                    } else {

                        // create a string of all characters
                        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

                        // create random string builder
                        StringBuilder sb1 = new StringBuilder();

                        // create an object of Random class
                        Random random = new Random();

                        // specify length of random string
                        int length = 7;

                        for (int i = 0; i < length; i++) {

                            // generate random index number
                            int index = random.nextInt(alphabet.length());

                            // get character specified by index
                            // from the string
                            char randomChar = alphabet.charAt(index);

                            // append the character to string builder
                            sb1.append(randomChar);
                            Character.toString(randomChar);
                            intset.add(Character.toString(randomChar));
                        }
//                    System.out.println("str  " +sb1 );
                        sb.append("'" + "abab" + "'" + ",");
                        sb2.append("'" + "abab" + "'" + ",");

                    }

                }

                sb.append(");" + "\n");
                sb2.append(");" + "\n");
                count = 0;
            }

        }

//        String replace = sb.toString().replace(",)", ")");
//        sb.append(replace);
        System.out.println("" + sb.toString().replace(",)", ")"));
        intset.add(sb2.toString());
//        System.out.println("" + sb2.toString());
//        System.out.println("" + intset);

    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

