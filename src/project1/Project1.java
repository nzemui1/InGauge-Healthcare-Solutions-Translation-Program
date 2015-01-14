package project1;
import java.text.DecimalFormat;
import java.io.*;
import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileNotFoundException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
 


public class Project1 {      
    static FileWriter writer1;
    public static void main (String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {       
        
        Scanner sc= new Scanner (System.in); //Reads in the file.
        System.out.print("Enter the name of the file: "); 
        String wordfile=sc.nextLine();
        Scanner s=new Scanner(new File(wordfile));                    
        s.useDelimiter("\t+"); //Removes tabs.            
        
        Scanner sc1= new Scanner (System.in); //Reads a set pathname
        System.out.print("Enter a pathname: "); 
        String path=sc1.nextLine();
        
        while (s.hasNext() ){ 
            String word =  s.next().replaceAll("[\\*\\:\\~\\s]+"," "); //Removes characters and spaces.             
            String account = word.substring(word.indexOf("DA")+2, word.indexOf("DA")+6); //Reads the account number.
            String payer = word.substring(word.indexOf("N1 PR"), word.indexOf("N3")).replaceAll("N1 PR\\s", ""); //Name of Insurance Company.              
            String pstreet = word.substring(word.indexOf("N3"), word.indexOf("N4")).replaceAll("N3\\s", ""); //Address of Insurance Company.              
            String pcity = word.substring(word.indexOf("N4"), word.indexOf("N1 PE")).replaceAll("N4\\s", ""); //Street, state, and zip code of Insurance Company.
            String name = word.substring(word.indexOf("N1 PE"),word.indexOf("FI")).replaceAll("N1 PE\\s", ""); //Name of payee.                
            String street = word.substring(word.lastIndexOf("N3"), word.lastIndexOf("N4")).replaceAll("N3\\s", ""); //Address of payee.               
            String city = word.substring(word.lastIndexOf("N4"), word.indexOf("REF PQ")).replaceAll("N4\\s", ""); //Street, state, and zip code of payee.
            String AddId = word.substring(word.indexOf("REF PQ"), word.indexOf("REF PQ")+20).replace("REF PQ ", ""); //Payee additional identification.
            String AddId1 = AddId.substring(0, 3);
            String TRN = word.substring(word.indexOf("TRN 1"), word.indexOf("TRN 1")+18).replace("TRN 1", ""); //Reassociation trace number.
            String year = word.substring(word.lastIndexOf("DA")+12, word.lastIndexOf("DA")+21); //Date.                                   
            
            File dir = new File(path+AddId1);
            dir.mkdir();
            File file = new File(dir,payer+"_"+TRN+"_"+year+".txt");  //Print to file in folder in directory set by path.                           
            writer1 = new FileWriter(file);            
            
            writer1.write(payer + " []");
            writer1.write(System.getProperty("line.separator"));
            writer1.write(pstreet);
            writer1.write(System.getProperty("line.separator"));
            writer1.write(pcity);                               
            writer1.write(System.getProperty("line.separator"));
            writer1.write(name +  " ["+ AddId + "]");
            writer1.write(System.getProperty("line.separator"));
            writer1.write(street);
            writer1.write(System.getProperty("line.separator"));
            writer1.write(city);                         
            writer1.write(System.getProperty("line.separator"));
            writer1.write("NPI #:");                            
            writer1.write(System.getProperty("line.separator"));
            writer1.write("Date:" + year);
            writer1.write(System.getProperty("line.separator"));
            writer1.write("CHECK/EFT #:"+TRN);                  
            writer1.write(System.getProperty("line.separator"));
            writer1.write("REND PROV "+" SERV DATE "+" POS NOS "+" PROC "+" MODS "+" BILLED "+" ALLOWED DEDUCT "+" COINS "+" GRP/RC-AMT "+" PROV PD");
            writer1.write(System.getProperty("line.separator"));
            
            int index1 = word.indexOf("CLP"); //Indexes of Claim Payment Information.                                                                                   
            int a = 0;                
            while (index1 >= 0) {               
                index1 = word.indexOf("CLP", index1 + 1);  
                a++; 
            }
                
            int [] in= new int[a];
            int index2 = word.indexOf("CLP");                                                            
            int b = 0;
            while (index2 >= 0) {   
                in[b]=index2;  
                index2 = word.indexOf("CLP", index2 + 1);  
                b++; 
            }
            
           for(int i = 0; i <= in.length; i++){  //Divide Claim Payment Information into string and send to method.
              if(i < in.length-1){
                String patient = word.substring(in[i], in[i+1]);            
                output(patient);           
              }
               else if(i == in.length-1){
                String patient = word.substring(in[i], word.lastIndexOf("LQ HE"));  
                output(patient);
              }
           }
           writer1.write(System.getProperty("line.separator"));
           writer1.write(System.getProperty("line.separator"));
           writer1.write("GLOSSARY: GROUP, REASON, MOA, REMARK AND REASON CODES");
           writer1.write(System.getProperty("line.separator"));
           writer1.write("CO-45 Charges exceed your contracted/ legislated fee arrangement. This change to be effective 6/1/07: Charge \n" +
                              "exceeds fee schedule/maximum allowable or contracted/legislated fee arrangement. (Use Group Codes PR or \n" +
                              "CO depending upon liability).");
           writer1.write(System.getProperty("line.separator"));
           writer1.write("N130 Consult plan benefit documents/guidelines for information about restrictions for this service.");
           writer1.write(System.getProperty("line.separator"));
           writer1.write("PR-2 Coinsurance Amount");
           writer1.write(System.getProperty("line.separator"));
           writer1.write("PR-50 These are non-covered services because this is not deemed a 'medical necessity' by the payer");           
           writer1.write(System.getProperty("line.separator"));
        } 
        writer1.close();
    }
    public static FileWriter output(String patient){
        try {
        String patientname = patient.substring(patient.indexOf("NM1 QC 1"), patient.indexOf(" MI ")).replace("NM1 QC 1", "NAME:"); //Patient name.     
        String HIC;        
        if(patient.contains("NM1 IL")){ //Member Indentification Number.
            HIC = patient.substring(patient.lastIndexOf(" MI "), patient.lastIndexOf(" MI ")+13).replace(" MI ", " HIC ");            
        }
        else{
            HIC = patient.substring(patient.indexOf(" MI "), patient.indexOf(" MI ")+13).replace(" MI ", " HIC ");
        }        
        String acnt = patient.substring(patient.indexOf("CLP"), patient.indexOf("CLP")+14).replace("CLP", " ACNT").replace(" 1 ",""); //Claim Submit Identifier.                
        String acnt1 = patient.substring(patient.lastIndexOf("11 1")-14, patient.lastIndexOf("11 1")).replace("11 1",""); //Reference Identification//                
                
        writer1.write(System.getProperty("line.separator"));
        writer1.write(System.getProperty("line.separator"));
        writer1.write(patientname+HIC+acnt+" ICN "+acnt1);
        writer1.write(System.getProperty("line.separator"));
        
        DecimalFormat dformat = new DecimalFormat("##0.00");
        
        String HC = null; //Composite Medical Procedure Indentifier.
        String HC1 = null; //Claim Adjustment Group Code.
        String HC2 = null; //Claim Adjustment Group Code.
        String HC3;
        double bill = 0; //Monestary Amount.
        double bill1 = 0; //Sum of amounts under bill.
        double amt = 0; //Monetary Amount.
        double amt1 = 0; //Sum of amounts under allowed.
        double prov = 0; //Monetary Amount.
        double prov1 = 0; //Sum of amounts under prov.
        double coin1 = 0; //Monetary Amount.
        double coin2 = 0; //Monetary Amount.
        double coin3 = 0; //Sum of amounts under coims.
        double coin4 = 0; //Sum of amounts after Claim Adjustment Group Code..
        double deduct = 0; //Deduction.
        String date = null;
        String date1 = null;    
        
        int a = 0;                
        int index1 = patient.indexOf("SVC"); //Indexes of each occurance of Service Payment Information.                                                                    
        while (index1 >= 0) {               
            index1 = patient.indexOf("SVC", index1 + 1);  
            a++; 
        }        
        int [] in1= new int[a];
        int index2 = patient.indexOf("SVC");                                                             
        int b = 0;
        while (index2 >= 0) {   
            in1[b]=index2;  
            index2 = patient.indexOf("SVC", index2 + 1);  
            b++; 
        }
        
        int c = 0;                
        int index3 = patient.indexOf("AMT B6"); //Indexes of each occurance of Service Supplemental Amount.                                                                   
        while (index3 >= 0) {               
            index3 = patient.indexOf("AMT B6", index3 + 1);  
            c++; 
        }        
        int [] in2= new int[c];
        int index4 = patient.indexOf("AMT B6");                                                            
        int d = 0;
        while (index4 >= 0) {   
            in2[d]=index4;  
            index4 = patient.indexOf("AMT B6", index4 + 1);  
            d++; 
        }
        for (int e = 0; e < in1.length; e++){            
            String SVC = patient.substring(in1[e], in2[e]+10); //Divide Service Payment Information into strings.
            if(SVC.contains("PR 50")){
                HC = SVC.substring(SVC.indexOf("HC"), SVC.indexOf("HC")+8).replace("HC", " 0 ");
                HC1 = SVC.substring(SVC.indexOf("PR 50"), SVC.indexOf("PR 50")+5).replace(" ", "-");    
                coin1 = Double.parseDouble(SVC.substring(SVC.indexOf("PR 50")+5, SVC.indexOf("PR 50")+8));        
                bill = Double.parseDouble(SVC.substring(SVC.indexOf("HC")+9, SVC.indexOf("HC")+12));        
                amt = Double.parseDouble(SVC.substring(SVC.indexOf("AMT B6"), SVC.indexOf("AMT B6")+9).replace("AMT B6" ,""));        
                prov = Double.parseDouble(SVC.substring(SVC.indexOf("HC")+12, SVC.indexOf("HC")+14));                                
                deduct = bill-coin1;
                date = SVC.substring(SVC.indexOf("DTM 472"), SVC.indexOf("DTM 472")+16).replace("DTM 472", "");
                date1 = date.substring(5,9);     
                try{
                writer1.write(date1+date+HC+"\t\t"+dformat.format(bill)+"\t"+dformat.format(amt)+"\t"+dformat.format(deduct)+"\t"+dformat.format(prov)+"\t"+HC1+"\t"+dformat.format(coin1)+"\t"+dformat.format(prov));                        
                writer1.write(System.getProperty("line.separator"));
                writer1.write("\t\t\tREM: N130");
                writer1.write(System.getProperty("line.separator"));
                } catch (Exception e2){}
                bill1 += coin1;
                amt1 += amt;
                coin3 += prov;
                coin4 += coin1;
                prov1 += prov;
            }
            if(SVC.contains("CO 45")){
                HC = SVC.substring(SVC.indexOf("HC"), SVC.indexOf("HC")+8).replace("HC", " 1 ");
                HC1 = SVC.substring(SVC.indexOf("CO 45"), SVC.indexOf("CO 45")+5).replace(" ", "-");        
                HC2 = SVC.substring(SVC.indexOf("PR 2"), SVC.indexOf("PR 2")+4).replace(" ", "-");   
                coin1 = Double.parseDouble(SVC.substring(SVC.indexOf("CO 45")+5, SVC.indexOf("CO 45")+8));  
                coin2 = Double.parseDouble(SVC.substring(SVC.indexOf("PR 2")+5, SVC.indexOf("PR 2")+9).replace(" R",""));                             
                if(SVC.contains(" 51 ") || SVC.contains(" 59 ")){
                    HC3 = SVC.substring(SVC.indexOf("HC")+8, SVC.indexOf("HC")+11);                    
                    bill = Double.parseDouble(SVC.substring(SVC.indexOf("HC")+12, SVC.indexOf("HC")+14));        
                    amt = Double.parseDouble(SVC.substring(SVC.indexOf("AMT B6"), SVC.indexOf("AMT B6")+10).replace("AMT B6",""));        
                    prov = Double.parseDouble(SVC.substring(SVC.indexOf("HC")+15, SVC.indexOf("HC")+19).replace(" 1",""));                
                    deduct = amt-(prov+coin2); 
                    date = SVC.substring(SVC.indexOf("DTM 472"), SVC.indexOf("DTM 472")+16).replace("DTM 472", "");
                    date1 = date.substring(5,9); 
                    writer1.write(date1+date+HC+HC3+"\t"+dformat.format(bill)+"\t"+dformat.format(amt)+"\t"+dformat.format(deduct)+"\t"+dformat.format(coin2)+"\t"+HC1+"\t"+dformat.format(coin1)+"\t"+dformat.format(prov));                        
                    writer1.write(System.getProperty("line.separator"));
                    writer1.write("\t\t\t\t\t\t\t\t"+HC2+"\t"+dformat.format(coin2));  
                    writer1.write(System.getProperty("line.separator"));
                    bill1 += coin2;
                    amt1 += amt;
                    coin3 += coin2;
                    coin4 += coin1;
                    prov1 += prov;
                }
                else{
                    bill = Double.parseDouble(SVC.substring(SVC.indexOf("HC")+9, SVC.indexOf("HC")+12));        
                    amt = Double.parseDouble(SVC.substring(SVC.indexOf("AMT B6"), SVC.indexOf("AMT B6")+9).replace("AMT B6" ,""));        
                    prov = Double.parseDouble(SVC.substring(SVC.indexOf("HC")+12, SVC.indexOf("HC")+16).replace(" 1",""));                
                    deduct = amt-(prov+coin2); 
                    date = SVC.substring(SVC.indexOf("DTM 472"), SVC.indexOf("DTM 472")+16).replace("DTM 472", "");
                    date1 = date.substring(5,9);     
                    writer1.write(date1+date+HC+"\t\t"+dformat.format(bill)+"\t"+dformat.format(amt)+"\t"+dformat.format(deduct)+"\t"+dformat.format(coin2)+"\t"+HC1+"\t"+dformat.format(coin1)+"\t"+dformat.format(prov));                        
                    writer1.write(System.getProperty("line.separator"));
                    writer1.write("\t\t\t\t\t\t\t\t"+HC2+"\t"+dformat.format(coin2));
                    writer1.write(System.getProperty("line.separator"));
                    bill1 += coin2;
                    amt1 += amt;
                    coin3 += coin2;
                    coin4 += coin1;
                    prov1 += prov;
                }
            } 
            else if(!SVC.contains("CO 45") && SVC.contains("PR 2")){
                HC = SVC.substring(SVC.indexOf("HC"), SVC.indexOf("HC")+9).replace("HC", " 1 ");
                HC1 = SVC.substring(SVC.indexOf("PR 2"), SVC.indexOf("PR 2")+4).replace(" ", "-");    
                coin1 = Double.parseDouble(SVC.substring(SVC.indexOf("PR 2")+4, SVC.indexOf("PR 2")+7));        
                bill = Double.parseDouble(SVC.substring(SVC.indexOf("HC")+9, SVC.indexOf("HC")+12));        
                amt = Double.parseDouble(SVC.substring(SVC.indexOf("AMT B6"), SVC.indexOf("AMT B6")+9).replace("AMT B6" ,""));        
                prov = Double.parseDouble(SVC.substring(SVC.indexOf("HC")+12, SVC.indexOf("HC")+15));                
                deduct = amt-(prov+coin1); 
                date = SVC.substring(SVC.indexOf("DTM 472"), SVC.indexOf("DTM 472")+16).replace("DTM 472", "");
                date1 = date.substring(5,9);     
                writer1.write(date1+date+HC+"\t\t"+dformat.format(bill)+"\t"+dformat.format(amt)+"\t"+dformat.format(deduct)+"\t"+dformat.format(coin1)+"\t"+HC1+"\t"+dformat.format(coin1)+"\t"+dformat.format(prov));                        
                writer1.write(System.getProperty("line.separator"));
                bill1 += coin1;
                amt1 += amt;
                coin3 += coin1;
                coin4 += coin1;
                prov1 += prov;
            }        
        }
        String plan = patient.substring(patient.indexOf("REF CE"), patient.indexOf("DTM 050")).replace("REF CE", ""); //Reference Identification Qualifier of Healthcare Policy Identification.                                                            
        writer1.write("PT RESP "+dformat.format(bill1)+" CLAIMS TOTALS "+dformat.format(amt1)+" "+dformat.format(deduct)+" "+dformat.format(coin3)+" "+dformat.format(coin4)+" "+dformat.format(prov1));
        writer1.write(System.getProperty("line.separator"));
        writer1.write("ADJ TO TOTAL: PREV PD INTEREST "+dformat.format(deduct)+" LATE FILING CHARGE "+dformat.format(deduct)+" NET "+dformat.format(prov1));
        writer1.write(System.getProperty("line.separator"));
        writer1.write("PLAN TYPE: "+plan);
        writer1.write(System.getProperty("line.separator"));
        writer1.write("STATUS CODE 1: PROCESSED AS PRIMARY");
        } catch (Exception e ){
            e.printStackTrace();
          } 
        return writer1;       
    }    
}