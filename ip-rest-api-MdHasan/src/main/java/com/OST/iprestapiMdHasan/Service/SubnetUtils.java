package com.OST.iprestapiMdHasan.Service;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SubnetUtils extends Object{

    private String cidrNotation, networkAddress, broadcastAddress;
    public static Map<String,String> map=new HashMap<>();

    public SubnetUtils(){};

  /*
  Constructor that takes a CIDR-notation string, e.g. "192.168.0.1/16"
  Parameters:
  cidrNotation - A CIDR-notation string, e.g. "192.168.0.1/16"
  Throws:
   IllegalArgumentException - if the parameter is invalid, i.e. does not match n.n.n.n/m where n=1-3 decimal digits, m = 1-3 decimal digits in range 1-32
   */
    public SubnetUtils(String cidrNotation){
        //hold four numbers of ip address (a.b.c.d)
        String[] strArr = new String[4];

        // Separate IP address and m (a.b.c.d/m)
        String[] str1 = cidrNotation.split("/");

        // IP address of the form a.b.c.d
        String ipAddress = str1[0];

        //Integer value of n (a.b.c.d/n)
        int n = Integer.parseInt(str1[1]);
        if(n<0||n>=31) throw new IllegalArgumentException("Invalid cidr Notation ");

        // Split IP address into 4 parts a.b.c.d
        strArr = ipAddress.split("\\.");

        int n1=Integer.valueOf(strArr[0]);
        int n2=Integer.valueOf(strArr[1]);
        int n3=Integer.valueOf(strArr[2]);
        int n4=Integer.valueOf(strArr[3]);
        if(n1>255||n1<0||n2>255||n2<0||n3>255||n3<0||n4>255||n4<0) throw new IllegalArgumentException("Ivalid cidr notation");

        this.cidrNotation = cidrNotation;
    }

//    public boolean validCidrNotation(String cidrNotation){
//        String cidrRegex="[0-9]+.[0-9]+.[0-9]+.[0-9]+/[1-9]";
//        Pattern cirdPat=Pattern.compile(cidrRegex,Pattern.CASE_INSENSITIVE);
//        Matcher matcher=cirdPat.matcher(cidrNotation);
//        return  matcher.find();
//    }
    /*
        Constructor that takes a dotted decimal address and a dotted decimal mask.
        Parameters:
        address - An IP address, e.g. "192.168.0.1"
        mask - A dotted decimal netmask e.g. "255.255.0.0"
        Throws:
        IllegalArgumentException - if the address or mask is invalid, i.e. does not match n.n.n.n where n=1-3 decimal digits and the mask is not all zeros
      */
//    public SubnetUtils(String address, String mask) throws IllegalArgumentException{
//        this.address = address;
//        this.mask = mask;
//    }

    //Return a SubnetUtils.SubnetInfo instance that contains subnet-specific statistics
    public final SubnetUtils.SubnetInfo getInfo(){

        SubnetUtils.SubnetInfo instance=new SubnetUtils.SubnetInfo();
        return instance;
    }

    //Set to true if you want the return value of SubnetUtils.SubnetInfo.getAddressCount() to include the network and broadcast addresses.
    public boolean isInclusiveHostCount(){
        return false;
    }

    public void setInclusiveHostCount(boolean inclusiveHostCount){
        //Set to true if you want the return value of SubnetUtils.SubnetInfo.getAddressCount() to include the network and broadcast addresses.
    }




    @Component
    public static final class SubnetInfo extends Object{

//       SubnetUtils suNotation= new SubnetUtils("10.0.0.1/24");
//       SubnetUtils suAddressMask= new SubnetUtils("110.0.0.1","255.255.255.0");
//       public static String networkAddress="10.0.0.0";
//       public static String broadcastAddress="10.0.0.255";
//        public static String lowAddress="10.0.0.1";
//        public static String highAddress="10.0.0.254";
//
//        //for this particular example
//        public void storeAddress(){
//            map.put("10.0.0.0", "network"); //set the network address status to "network"
//            map.put("10.0.0.255", "broadcast"); // set the broadcast address status to "broadcast"
//
//            //set all assignable IP addresses status to "available"
//            for(int i=1;i<=254;i++){
//                String key="10.0.0."+String.valueOf(i);
//                String value="available";
//                map.put(key,value);
//            }
//
//        }


        public String storeAddress(){

            SubnetUtils suNotation= new SubnetUtils("10.0.0.1/24");

            cidrCalculation(suNotation.cidrNotation);

            return "All assignable ip addresses are stored with status available";
        }

        public String storeAddress(String sidr){

            SubnetUtils suNotation= new SubnetUtils(sidr);

            cidrCalculation(sidr);

            return "All assignable ip addresses are stored with status available";
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






        //return all ip addresses including network address and broadcast addresses
        public Map<String, String> listAddress(){
            return map;
        }

        //acquire a specific address and change the status to "acquired"
        public String acquireAddress(){
            String ipAddress="10.0.0.84";
            if(map.containsKey(ipAddress)&& map.get(ipAddress)=="available"){
                map.replace(ipAddress, "acquired");
                return ipAddress+" is acquired";
            }
            return ipAddress +" is not inRange";
        }

        //release the specific ip address which was acquired and change the status to "available"
        public String releaseAddress(){
            String ipAddress="10.0.0.84";
            if(map.containsKey(ipAddress)&& map.get(ipAddress)=="acquired"){
                map.replace(ipAddress, "available");
                return ipAddress+" is released and now the status is available ";
            }
            else if(map.containsKey(ipAddress)&& map.get(ipAddress)=="available"){
                return ipAddress+" was no acquired ";
            }
            return ipAddress +" is not inRange";
        }




        //        Returns true if the parameter address is in the range of usable endpoint addresses for this subnet.
        //        This excludes the network and broadcast adresses.
        //                Parameters:
        //        address - A dot-delimited IPv4 address, e.g. "192.168.0.1"
       //        Returns:
       //        True if in range, false otherwise
        public boolean isInRange(String address){
            if(map.containsKey(address)){
                return true;
            }
            return false;
        }


        public boolean isInRange(int address){
            return false;
        }


//        public String getBroadcastAddress(){
//            return broadcastAddress;
//        }
//
//        public String getNetworkAddress(){
//            return networkAddress;
//        }
//
//        public String getNetmask(){
//            return suAddressMask.mask;
//        }
//
//        public String getAddress(){
//            return suAddressMask.address;
//        }

//        Return the low address as a dotted IP address. Will be zero for CIDR/31 and CIDR/32 if the inclusive flag is false.
//        Returns:
//        the IP address in dotted format, may be "0.0.0.0" if there is no valid address

//        public String getLowAddress(){
//            return lowAddress;
//        }

//        Return the high address as a dotted IP address. Will be zero for CIDR/31 and CIDR/32 if the inclusive flag is false.
//        Returns:
//        the IP address in dotted format, may be "0.0.0.0" if there is no valid address

//        public String getHighAddress(){
//            return highAddress;
//        }

//        Deprecated. (3.4) use getAddressCountLong() instead
//        Get the count of available addresses. Will be zero for CIDR/31 and CIDR/32 if the inclusive flag is false.
//        Returns:
//        the count of addresses, may be zero.
//        Throws:
//        RuntimeException - if the correct count is greater than Integer.MAX_VALUE

        @Deprecated
        public int getAddressCount(){
            return 0;
        }


//        Get the count of available addresses. Will be zero for CIDR/31 and CIDR/32 if the inclusive flag is false.
//        Returns:
//        the count of addresses, may be zero.
//        Since:
//                3.4

        public long getAddressCountLong(){
            return 0;
        }

        public int asInteger(String address){
            return 0;
        }

//        public String getCidrSignature(){
//            return suNotation.cidrNotation;
//        }

        public String[] getAllAddresses(){
            List<String> list=new ArrayList<String>();



            String[] arr = new String[list.size()];
            list.toArray(arr);
            return arr;
        }


        @Override
        public String toString() {
            return "SubnetInfo{}";
        }
    }

}
