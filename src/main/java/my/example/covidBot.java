package my.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class covidBot extends TelegramLongPollingBot{

    String message_text;
    long chat_id;

    private static List userinput = new ArrayList();
    private static List country = new ArrayList();
    private static List updates = new ArrayList();
    private static ArrayList<Double> highestnewcases = new ArrayList<Double>();
    private static ArrayList<Double> highestdeaths  = new ArrayList<Double>();
    private static ArrayList<Double> highestnewdeaths  = new ArrayList<Double>();
    private static ArrayList<Double> highestcases  = new ArrayList<Double>();


    public void onUpdateReceived(Update update){

        message_text = update.getMessage().getText().replace(" ", "-");;
        chat_id = update.getMessage().getChatId();

        new Thread((Runnable)()->{
            try {
                userinput.clear();
                updates.clear();
                country.clear();
                highestnewcases.clear();
                highestdeaths.clear();
                highestnewdeaths.clear();
                highestcases.clear();

                collectuserinput(message_text);
                collectCountry(message_text);
                collectHighestCases(message_text);
                collectHighestNewCases(message_text);
                collectHighestDeaths(message_text);
                collectHighestNewDeaths(message_text);
                Thread.sleep(500);

            } catch (InterruptedException e){
                e.printStackTrace();
            }

            String user=" ";
            for (int i=0; i<userinput.size(); i++){
                user += "\n" + userinput.get(i);

            }
            String updated=" ";
            for (int i=0; i<updates.size(); i++){
                updated += "" + updates.get(i);
            }

            double max = highestnewcases.get(0);
            int maxIndex = 0;

            for (int r=9; r<220; r++) {
                if( highestnewcases.get(r) > max ){
                    max = highestnewcases.get(r);
                    maxIndex = r;
                }
            }

            double max1 = highestdeaths.get(0);
            int maxIndex1 = 0;

            for (int s=9; s<220; s++) {
                if( highestdeaths.get(s) > max1 ){
                    max1 = highestdeaths.get(s);
                    maxIndex1 = s;
                }
            }

            double max2 = highestnewdeaths.get(0);
            int maxIndex2 = 0;

            for (int t=9; t<220; t++) {
                if( highestnewdeaths.get(t) > max2 ){
                    max2 = highestnewdeaths.get(t);
                    maxIndex2 = t;
                }
            }

            double max3 = highestcases.get(0);
            int maxIndex3 = 0;

            for (int u=9; u<220; u++) {
                if( highestcases.get(u) > max3 ){
                    max3 = highestcases.get(u);
                    maxIndex3 = u;
                }
            }

            SendMessage message = new SendMessage()
                    .setChatId(chat_id)
                    .setText("Country Name : " + message_text.replace("-", " ")+
                            user.replace(":"," : ").split("Recovered")[0]
                            + "New Cases & Death :" + updated.replace("[source]", "")
                            + "\n------------------------------------------\n"
                            + "Highest Cases : " + country.get(maxIndex3-2) + ", " + highestcases.get(maxIndex3).toString().replace(".0", "")
                            + "\nHighest New Cases : " + country.get(maxIndex-2) + ", " + highestnewcases.get(maxIndex).toString().replace(".0", "")
                            + "\nHighest Deaths : " + country.get(maxIndex1-2) + ", " + highestdeaths.get(maxIndex1).toString().replace(".0", "")
                            + "\nHighest New Deaths : " + country.get(maxIndex2-2) + ", " + highestnewdeaths.get(maxIndex2).toString().replace(".0", ""));

            SendMessage welcome = new SendMessage()
                    .setChatId(chat_id).setText("Please type any country");
            try {
                if (message_text.contains("/start"))
                    execute(welcome);
                else
                    execute(message);

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public String getBotUsername() {
        return "STIW3054_Covid19_bot";
    }

    public String getBotToken() {
        return "1312090455:AAGzEZCYLJWWnXIHJfrknnrloMUHjlsmvHU";
    }

    public static void collectHighestNewDeaths(String message_text){

        final String url = "https://www.worldometers.info/coronavirus/";

        try {
            final Document source = Jsoup.connect(url).timeout(50000).get();
            for (Element row : source.select("table#main_table_countries_today tr")){

                if (row.select("td:nth-of-type(6)").text().equals("")){
                    final String totaldeath1= row.select("td:nth-of-type(6)").text()+"0";
                    final String totaldeath12=totaldeath1.replace("+","").replace(",","").replace(" ","0");
                    final double totaldeath123=Double.parseDouble(totaldeath12);
                    highestnewdeaths.add(totaldeath123);
                    continue;
                }else{
                    final String totaldeath11= row.select("td:nth-of-type(6)").text();
                    final String totaldeath22=totaldeath11.replace("+","").replace(",","").replace(" ","0");
                    final double totaldeath33=Double.parseDouble(totaldeath22);
                    highestnewdeaths.add(totaldeath33);
                }
            }

        } catch (IOException e) {
            System.out.println("ERROR : Failed to access total new death" + "https://www.worldometers.info/coronavirus/");
        }
    }

    public static void collectHighestDeaths(String message_text){

        final String url = "https://www.worldometers.info/coronavirus/";

        try {
            final Document source = Jsoup.connect(url).timeout(50000).get();
            for (Element row : source.select("table#main_table_countries_today tr")){

                if (row.select("td:nth-of-type(5)").text().equals("")){
                    final String totalnew1= row.select("td:nth-of-type(5)").text()+"0";
                    final String totalnew12=totalnew1.replace("+","").replace(",","");
                    final double totalnew123=Double.parseDouble(totalnew12);
                    highestdeaths.add(totalnew123);
                    continue;
                }else{
                    final String totalnew11= row.select("td:nth-of-type(5)").text();
                    final String totalnew22=totalnew11.replace("+","").replace(",","");
                    final double totalnew33=Double.parseDouble(totalnew22);
                    highestdeaths.add(totalnew33);
                }
            }

        } catch (IOException e) {
            System.out.println("ERROR : Failed to access total death" + "https://www.worldometers.info/coronavirus/");
        }


    }
    public static void collectHighestNewCases(String message_text){

        final String url = "https://www.worldometers.info/coronavirus/";

        try {
            final Document source = Jsoup.connect(url).timeout(50000).get();
            for (Element row : source.select("table#main_table_countries_today tr")){

                if (row.select("td:nth-of-type(4)").text().equals("")){
                    final String totaldeath1= row.select("td:nth-of-type(4)").text()+"0";
                    final String totaldeath12=totaldeath1.replace("+","").replace(",","").replace(" ","0");
                    final double totaldeath123=Double.parseDouble(totaldeath12);
                    highestnewcases.add(totaldeath123);
                    continue;
                }else{
                    final String totaldeath11= row.select("td:nth-of-type(4)").text();
                    final String totaldeath22=totaldeath11.replace("+","").replace(",","").replace(" ","0");
                    final double totaldeath33=Double.parseDouble(totaldeath22);
                    highestnewcases.add(totaldeath33);
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR : Failed to access total death" + "https://www.worldometers.info/coronavirus/");
        }


    }


    public static void collectHighestCases(String message_text){

        final String url = "https://www.worldometers.info/coronavirus/";

        try {
            final Document source = Jsoup.connect(url).timeout(60000).get();
            for (Element row : source.select("table#main_table_countries_today tr")){

                if (row.select("td:nth-of-type(3)").text().equals("")){
                    final String totaldeath1= row.select("td:nth-of-type(3)").text()+"0";
                    final String totaldeath12=totaldeath1.replace("+","").replace(",","").replace(" ","0");
                    final double totaldeath123=Double.parseDouble(totaldeath12);
                    highestcases.add(totaldeath123);
                    continue;
                }else{
                    final String totaldeath11= row.select("td:nth-of-type(3)").text();
                    final String totaldeath22=totaldeath11.replace("+","").replace(",","").replace(" ","0");
                    final double totaldeath33=Double.parseDouble(totaldeath22);
                    highestcases.add(totaldeath33);
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR : Failed to access " + "https://www.worldometers.info/coronavirus/");
        }

    }

    public static void collectCountry(String message_text){

        final String url = "https://www.worldometers.info/coronavirus/";

        try {
            final Document source = Jsoup.connect(url).timeout(60000).get();
            for (Element row : source.select("table#main_table_countries_today tr")){
                if (row.select("td:nth-of-type(2)").text().equals("")){
                    continue;
                }else{
                    final String country1= row.select("td:nth-of-type(2)").text();
                    country.add(country1);
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR : Failed to access Country" + "https://www.worldometers.info/coronavirus/");
        }
    }

    public static void collectuserinput(String message_text){

        Document doc=null;

        try {
            doc = Jsoup.connect("https://www.worldometers.info/coronavirus/country/"+message_text+"/").timeout(30000).get();
            Elements Totaldeathcases = doc.select("#maincounter-wrap");
            Totaldeathcases.forEach((item)->{
                String a =item.select("h1").text();
                String b = item.select(".maincounter-number>span").text();
                userinput.add(a+b);
            });

            Elements newscase1 = doc.select("#newsdate2020-07-15");
            newscase1.forEach((news)-> {
                String a = news.select(".news_li>strong").text();
                userinput.add(a);
            });

            Elements newcase2 = doc.select("#newsdate2020-07-15");
            newcase2.forEach((news)-> {
                String a = news.select(".news_li").text();
                updates.add(a);
            });

        } catch (IOException e) {
            System.out.println("Error collected ");
            e.printStackTrace();
        }
    }
}