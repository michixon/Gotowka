package com.company;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Main extends com.company.URLConnectionReader {

    public static void main(String[] args) throws Exception {
        String EAN = "";
        int sposPlatnosci;
        double reszta = 0;
        int a = 0;
        int b;
        double zaplata = 0;
        int zaplata1 = 0;
        float suma = 0;
        ArrayList<Integer> banknoty = new ArrayList<Integer>();
        Scanner myObj = new Scanner(System.in);
        Random rnd = new Random();
        int x =1;
        String productName = "";
        System.out.println("Zeskanuj swoje produkty");
        do{
            System.out.print("-> ");
            EAN = myObj.nextLine();
            if (!Objects.equals(EAN, "x")) {
                String response = printApiData("http://localhost:8080/ean/" + EAN).replace('"', ' ');
                String[] first = response.split(",");
                String[] second = first[0].split(":");
                String[] third = first[1].split(":");
                String[] czwarty = third[1].split("}");
                productName = czwarty[0];
//            System.out.println(third[1]+ " <-- third 1     " + third[0] + " <-- third 0");
//            System.out.println(second[1]+ " <-- second 1     " + second[0] + " <-- second 0");
                float price = Float.parseFloat(second[1]);
                suma += price;
                System.out.println(x + ". " + productName + " " + price + " zl");
                System.out.println("lacznie: " + suma + " zl");
                x++;

                System.out.println("wpisz 'x' jeśli zeskanujesz wszystkie produkty");
            }
        }while(!Objects.equals(EAN, "x"));
        //double am = rnd.nextInt(10000) / 100.0;
        //double amount = Math.round(Math.random() * 100000) / 100.0; //0,984187687253;      879.82
        boolean doRepeat = false;

        do {
            System.out.print("Do zaplaty: " + suma + " Wybierz sposob platnosci:\n1. Karta\n2. BLIK\n3. Gotowka\nWpisz liczbe odpowiadajaca Twojemu wyborowi: ");
            String input1 = myObj.nextLine();

            try {
                sposPlatnosci = Integer.parseInt(input1);//licba - OK; litery - EXCEPTION!!!!
            } catch (NumberFormatException e) {
                sposPlatnosci = 0;
            }


            if (sposPlatnosci == 1) {
                System.out.println("wybrales sposob platnosci: Karta");
                doRepeat = true;
            } else if (sposPlatnosci == 2) {
                System.out.println("wybrales sposob platnosci: Blik");
                doRepeat = true;
            } else if (sposPlatnosci == 3) {
                System.out.println("wybrales sposob platnosci: Gotowka");
                doRepeat = true;
            } else {
                System.out.println("wybierz sposob platnosci od 1 do 3: ");
                try {
                    sposPlatnosci = Integer.parseInt(input1);//licba - OK; litery - EXCEPTION!!!!
                } catch (NumberFormatException e) {
                    sposPlatnosci = 0;
                }
                doRepeat = false;
            }
            System.out.println("Wybrales: " + sposPlatnosci);
        } while (doRepeat == false);

        if (sposPlatnosci == 3) {
            System.out.println("kwota do zplaty wysosi: " + suma);
            while(zaplata1 <= suma) {
                while (a < 1) {
                    System.out.println("Podawaj banknoty pojeynczo");
                    System.out.println("Jesli podales wszystkie banknoty wpisz numer 0");
                    b = Integer.parseInt(myObj.nextLine());
                    if (b == 0) {
                        if(zaplata1 >= suma){
                            a++;
                        }else{
                            System.out.println("Podano za malo banknotow !!!");
                        }

                    } else if (b == 10 || b == 20 || b == 50 || b == 100 || b == 200 || b == 500) {
                        banknoty.add(b);
                        zaplata1 += b;
                        //System.out.println("Podawaj tylko istniejace banknoty :)");
                    } else {
                        System.out.println("Podawaj tylko istniejace banknoty :)");
                        //banknoty.add(b);
                        //zaplata1 += b;
                    }

                }
            }
            for (int i : banknoty) {
                zaplata+=i;
            }
            System.out.println("zaplaciles: " + zaplata1);
            System.out.println("koszt wynosil: " + suma);
            double c = suma;
            reszta = zaplata-c;
            DecimalFormat df = new DecimalFormat("###.##");
            System.out.println("Reszta wynosi: " + df.format(reszta) );
        }
        if(sposPlatnosci==2) {
            System.out.println("Do zapłaty " + suma);
            System.out.println("Wprowadz kod blik");
            String blikCode = myObj.nextLine();

            printApiData("http://localhost:8080/blik/"+blikCode+"/"+suma);

            String token = printApiData("http://localhost:8080/blik/token/"+blikCode);
            PushNotifictionHelper.sendPushNotification(token);

//            String response = printApiData("http://192.168.1.109:8080/blik/" + blikCode);
//            System.out.println(response);


//            if(response == ){
//
//            }
//
//            System.out.println("http://192.168.1.111:8080/blik/" + blikCode + amount);


        }
    }
}



