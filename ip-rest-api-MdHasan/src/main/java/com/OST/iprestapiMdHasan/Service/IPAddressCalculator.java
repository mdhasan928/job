package com.OST.iprestapiMdHasan.Service;
import org.springframework.stereotype.Service;

import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.Math;

/*
CIDR: 192.168.32.1/24
IP Address(Binary):  11000000101010000010000000000001
Default Mask(Binary):  11111111111111111111111100000000
Default Mask: 255.255.255.0

32-n=32-24=8

First Address: Set last 8 bits of IP address to 0
              = 11000000101010000010000000000000
              = 192.168.32.0

Last Address: Set last 8 bits of IP address to 1
              = 11000000101010000010000011111111
              = 192.168.32.255
 */

@Service
public class IPAddressCalculator {

    // Convert IP address to the binary form
    public static int[] convertToBinary(String[] str)
    {
        int binaryArr[] = new int[32];
        int a, b, c, d, i, remainder;
        a = b = c = d = 1;
        Stack<Integer> st = new Stack<Integer>();

        // Separate each number of the IP address e.g. for 10.0.0.1 => a=10, b=0, c=0, d=1
        if (str != null)
        {
            a = Integer.parseInt(str[0]);
            b = Integer.parseInt(str[1]);
            c = Integer.parseInt(str[2]);
            d = Integer.parseInt(str[3]);
        }

        // convert first number to binary
        for (i = 0; i <= 7; i++)
        {
            remainder = a % 2;
            st.push(remainder);
            a = a / 2;
        }

        // Obtain First octet
        for (i = 0; i <= 7; i++) {
            binaryArr[i] = st.pop();
        }

        // convert second number to binary
        for (i = 8; i <= 15; i++) {
            remainder = b % 2;
            st.push(remainder);
            b = b / 2;
        }

        // Obtain Second octet
        for (i = 8; i <= 15; i++) {
            binaryArr[i] = st.pop();
        }

        // convert Third number to binary
        for (i = 16; i <= 23; i++) {
            remainder = c % 2;
            st.push(remainder);
            c = c / 2;
        }

        // Obtain Third octet
        for (i = 16; i <= 23; i++) {
            binaryArr[i] = st.pop();
        }

        // convert fourth number to binary
        for (i = 24; i <= 31; i++) {
            remainder = d % 2;
            st.push(remainder);
            d = d / 2;
        }

        // Obtain Fourth octet
        for (i = 24; i <= 31; i++) {
            binaryArr[i] = st.pop();
        }

        return (binaryArr);
    }


    // Convert IP address from binary to decimal form
    public static int[] convertToDecimal(int[] biArr)
    {

        int[] decArr = new int[4];
        int a, b, c, d, i, j;
        a = b = c = d = 0;
        j = 7;

        //Obtain the first decimal number of IP the address
        for (i = 0; i < 8; i++) {

            a = a + biArr[i]*(int)(Math.pow(2, j));
            j--;
        }

        //Obtain the second decimal number of the IP address
        j = 7;
        for (i = 8; i < 16; i++) {
            b = b + biArr[i] * (int)(Math.pow(2, j));
            j--;
        }
        //Obtain the third decimal number of the IP address
        j = 7;
        for (i = 16; i < 24; i++) {
            c = c + biArr[i] * (int)(Math.pow(2, j));
            j--;
        }
        //Obtain the fourth decimal number of the IP address
        j = 7;
        for (i = 24; i < 32; i++) {
            d = d + biArr[i] * (int)(Math.pow(2, j));
            j--;
        }

        decArr[0] = a;
        decArr[1] = b;
        decArr[2] = c;
        decArr[3] = d;
        return decArr;
    }

    public static void cidrCalculation(String cidr){

        int i;

        //hold four numbers of ip address (a.b.c.d)
        String[] strArr = new String[4];

        // Separate IP address and m (a.b.c.d/m)
        String[] str1 = cidr.split("/");

        // IP address of the form a.b.c.d
        String ipAddress = str1[0];

        //Integer value of n (a.b.c.d/n)
        int n = Integer.parseInt(str1[1]);

        // Split IP address into 4 parts a.b.c.d
        strArr = ipAddress.split("\\.");

        //hold the binary digit of ip address
        int[] binArr = new int[32];

        // Convert IP address to binary form
        binArr =convertToBinary(strArr);

        int[] ntwkAddressBinary = new int[32];
        int[] brdAddressBinary = new int[32];


        //total available host bit
        int hostBit = 32 - n;

        // Obtain network address
        for (i = 0; i <= (31 - hostBit); i++) {

            ntwkAddressBinary[i] = binArr[i];
            brdAddressBinary[i] = binArr[i];
        }

        // Set 32-n bits to 0
        for (i = 31; i > (31 - hostBit); i--) {

            ntwkAddressBinary[i] = 0;
        }

        // Obtain Broadcast address by setting 32-n bits to 1
        for (i = 31; i > (31 - hostBit); i--) {

            brdAddressBinary[i] = 1;
        }

        // Converting network address to decimal
        int[] ntAddressDecimal = convertToDecimal(ntwkAddressBinary);

        // Converting broadcast address to decimal
        int[] brAddressDecimal = convertToDecimal(brdAddressBinary);

        String networkAddress= ntAddressDecimal[0]+"."+ ntAddressDecimal[1] + "."
                + ntAddressDecimal[2] + "." + ntAddressDecimal[3];

        String broadcastAddress=brAddressDecimal[0] + "." + brAddressDecimal[1] + "."
                + brAddressDecimal[2] + "." + brAddressDecimal[3];


        //Total assignable available ip address
        int totalAddress= (int) Math.pow(2,hostBit)-2;

        int firstNum=Integer.valueOf(ntAddressDecimal[3]);
        int secondNum=Integer.valueOf(ntAddressDecimal[2]);
        int thirdNum=Integer.valueOf(ntAddressDecimal[1]);
        int fourthNum=Integer.valueOf(ntAddressDecimal[0]);

        int fiNum=firstNum;
        int seNum=secondNum;
        int thNum=thirdNum;
        int foNum=fourthNum;

        //Storage for all assignable ip address
        Map<String,String> map=new HashMap<>();

        //Calculate the assignable ip address and store in data store (Map)
        for(int k=1;k<=totalAddress;k++){
            if(fiNum<255)
                fiNum+=1;
            else if(fiNum>=255&&seNum<255){
                seNum+=1;
                fiNum=firstNum;

            }
            else if(fiNum>=255&&seNum>=255&&thNum<255){
                thNum+=1;
                fiNum=firstNum;
                seNum=secondNum;
            }
            else if(fiNum>=255&&seNum>=255&&thNum>=255&&foNum<255){
                foNum+=1;
                fiNum=firstNum;
                seNum=secondNum;
                thNum=thirdNum;
            }

            String address=String.valueOf(foNum)+"."+String.valueOf(thNum)+"."
                    +String.valueOf(seNum)+"."+String.valueOf(fiNum);
            map.put(address,"available");

        }



        System.out.println("IP address CIDR format is:" + cidr);
        System.out.println("Network address= "+networkAddress);
        System.out.println("Broadcast address= "+broadcastAddress);
        System.out.println("Total assignable available ip address= "+ totalAddress);

        for(Map.Entry<String, String> entry: map.entrySet()){
            System.out.print(entry.getKey()+" : "+entry.getValue()+" , ");
        }





    }


    public static void main(String args[])
    {
        String ipr = "192.168.60.55/30";
        cidrCalculation(ipr);

//        int i;
//        String[] str = new String[4];
//        //String ipr = "192.168.1.1/24";
//        //String ipr = "10.0.0.1/24";
//        //String ipr = "192.168.60.55/20";
//        //String ipr = "172.10.85.60/22";
//        String ipr = "172.10.60.16/29";
//
//        // You can take user input here
//        // instead of using default address
//        // Ask user to enter IP address of form(a.b.c.d/m)
//        System.out.println("IP address CIDR format is:" + ipr);
//
//        // Separate IP address and n
//        String[] str1 = ipr.split("/");
//
//        // IP address
//        String tr = str1[0];
//
//        // Split IP address into 4 parts a.b.c.d
//        str = tr.split("\\.");
//
//        int[] b = new int[32];
//
//        System.out.println();
//
//        // Convert IP address to binary form
//        b =convertToBinary(str);
//
//        int n = Integer.parseInt(str1[1]);
//        int[] ntwk = new int[32];
//        int[] brd = new int[32];
//        int t = 32 - n;
//
//        // Obtanining network address
//        for (i = 0; i <= (31 - t); i++) {
//
//            ntwk[i] = b[i];
//            brd[i] = b[i];
//        }
//
//        // Set 32-n bits to 0
//        for (i = 31; i > (31 - t); i--) {
//
//            ntwk[i] = 0;
//        }
//
//        // Obtaining Broadcast address
//        // by setting 32-n bits to 1
//        for (i = 31; i > (31 - t); i--) {
//
//            brd[i] = 1;
//        }
//
//        System.out.println();
//
////        // Obtaining class of Address
////        char c = cls(str);
////        System.out.println("Class : " + c);
//
//        // Converting network address to decimal
//        int[] nt = convertToDecimal(ntwk);
//
//        // Converting broadcast address to decimal
//        int[] br = convertToDecimal(brd);
//
//        // Printing in dotted decimal format
//        System.out.println("Network Address : " + nt[0]
//                + "." + nt[1] + "." + nt[2] + "." + nt[3]);
//
//        // Printing in dotted decimal format
//        System.out.println("Broadcast Address : "
//                + br[0] + "." + br[1] + "." + br[2] + "." + br[3]);
    }
}
