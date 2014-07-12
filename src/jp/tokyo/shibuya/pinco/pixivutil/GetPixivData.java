package jp.tokyo.shibuya.pinco.pixivutil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

import jp.tokyo.shibuya.pinco.entity.MediaEntity;
import jp.tokyo.shibuya.pinco.util.Constants;

public class GetPixivData {
    public static void main (String[] args){
        HashSet<MediaEntity> meHS = GetPixivData.getRankingDaily();
        System.out.println(meHS.toString());
    }

    public static HashSet<MediaEntity> getRankingDaily(){
        return getRanking("day");
    }

    public static HashSet<MediaEntity> getRankingWeekly(){
        return getRanking("week");
    }

    public static HashSet<MediaEntity> getRankingMonthly(){
        return getRanking("month");
    }

    public static HashSet<MediaEntity> getSearch(String key){
        return getSearchKey(key, 1);
    }

    public static HashSet<MediaEntity> getSearch(String key, int page){
        return getSearchKey(key, page);
    }

    private static HashSet<MediaEntity> getRanking(String key){
        HashSet<MediaEntity> meHS = new HashSet<MediaEntity>();
        try {
            URL url = new URL(
                    Constants.PIXIV_RANKING_API + "mode=" + key);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            String line = "";
            StringBuffer str = new StringBuffer();
            ArrayList<String> details = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                str.append(line);
            }
            parse(details, str.toString().split(",", 2));
            for (int i = 0; i < details.size() / 30; i++) {
                if((replace(details.get(i*30+2)).toLowerCase(Locale.JAPANESE).matches("jpg|gif|png"))){
                    MediaEntity me = new MediaEntity ("pixiv", replace(details.get(i*30)) , replace(details.get(i*30+24)), replace(details.get(i*30+9)), replace(details.get(i*30+18)));
                    meHS.add(me);
                }
            }

        } catch (Exception e) {
            return null;
        }
        return meHS;
    }

    private static HashSet<MediaEntity> getSearchKey(String key, int page){
        HashSet<MediaEntity> meHS = new HashSet<MediaEntity>();
        try {
            URL url = new URL(
                    Constants.PIXIV_SEARCH_API + "&s_mode=s_tag&word=" + URLEncoder.encode(key,"utf-8") + "&order=date&PHPSESSID=0&p=" + page);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "utf-8"));
            String line;
            StringBuffer str = new StringBuffer();
            ArrayList<String> details = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                str.append(line);
            }
            parse(details, str.toString().split(",", 2));
            for (int i = 0; i < details.size() / 30; i++) {
                if((replace(details.get(i*30+2)).toLowerCase(Locale.JAPANESE).matches("jpg|gif|png"))){
                    MediaEntity me = new MediaEntity ("pixiv", replace(details.get(i*30)) , replace(details.get(i*30+24)), replace(details.get(i*30+9)), replace(details.get(i*30+18)));
                    meHS.add(me);
                }
            }

        } catch (Exception e) {
            return null;
        }
        return meHS;
    }

    private static String parse(ArrayList<String> details, String[] strarr) {
        if (strarr.length != 2) {
            return null;
        } else if (strarr[0].isEmpty() || strarr[0].startsWith("\"")
                && strarr[0].endsWith("\"")) {
            details.add(strarr[0]);
            strarr = strarr[1].split(",", 2);
            return parse(details, strarr);
        } else if (strarr[1].isEmpty() || strarr[1].startsWith("\"")
                && strarr[1].endsWith("\"")) {
            return strarr[1];
        } else {
                String[] strarr2 = strarr[1].split(",", 2);
                strarr[0] = strarr[0].concat(strarr2[0]);
                strarr[1] = strarr2[1];
            return parse(details, strarr);
        }
    }

    private static String replace (String str) {
        return str.replaceAll("^\"", "").replaceAll("\"$", "");
    }
}
