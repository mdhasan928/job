package com.OST.iprestapiMdHasan.Controller;


import com.OST.iprestapiMdHasan.Service.SubnetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Map;

@RestController
public class IpAddressController {

    @Autowired
    SubnetUtils snetutils;

    @Autowired
    SubnetUtils.SubnetInfo ssnetutils;


   // Create IP addresses - take in a CIDR block (e.g. 10.0.0.1/24) and add all IP addresses within that block
   // to the data store with status “available”
    @RequestMapping(value = "/Create IP address")
     public String createIpAddress(){
            ssnetutils.storeAddress();
            return "IP addresses are stroed in data store";
            }

    @RequestMapping(value = "/Create IP address/{sidr})")
    public String createIpAddress(@PathVariable("sidr") String sid){
        String s= String.valueOf(sid);
        ssnetutils.storeAddress(s);
        return "IP addresses are stroed in data store";
    }

    //Return all IP addresses in the system with their current status in pairs
    @RequestMapping(value="/List IP addresses")
    public Map<String,String> listIPaddresses(){
       return ssnetutils.listAddress();
    }


    //Set the status of a certain IP to “acquired”
    @RequestMapping(value="/Acquire an IP")
        public String acquireAnIP(){
       return ssnetutils.acquireAddress();

    }

   // Release an IP - set the status of a certain IP "acquired" to “available”
   @RequestMapping(value="/Release an IP")
   public String releaseAnIP(){
      return ssnetutils.releaseAddress();
   }


}
