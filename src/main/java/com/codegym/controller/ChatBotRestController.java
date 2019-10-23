package com.codegym.controller;

import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
public class ChatBotRestController {
    @GetMapping("/")
    public ResponseEntity<String> homePage(HttpServletRequest request, HttpServletResponse response){
        String str = "Home page. Server running okay.";
        return new ResponseEntity<String>(str, HttpStatus.OK);
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> webhookGet(HttpServletRequest request, HttpServletResponse response){
        String message = "";
        if (request.getHeader("hub.verify_token") == "ma_xac_minh_cua_ban") {
           message = request.getHeader("hub.challenge");
            return new ResponseEntity<String>(message, HttpStatus.OK);
        }
        message = "Error, wrong validation token";
        return new ResponseEntity<String>(message,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhookPost(@RequestBody HashMap<String, Object> entryObject, HttpServletRequest request, HttpServletResponse response) throws ParseException {
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("",HttpStatus.OK);

        ArrayList<HashMap<String,Object>> arrayList = (ArrayList<HashMap<String, Object>>) entryObject.get("entry");
        HashMap<String,Object> entry = arrayList.get(0);

        arrayList = (ArrayList<HashMap<String, Object>>) entry.get("messaging");
        HashMap<String,Object> messaging = arrayList.get(0);

        HashMap<String,Object> sender = (HashMap<String, Object>) messaging.get("sender");

        String senderId = (String) sender.get("id");

        HashMap<String, Object> message = (HashMap<String, Object>) messaging.get("message");

        String text = (String) message.get("text");

        System.out.println(text);
        if (text!= null){
            responseEntity = new ResponseEntity<String>(sendMessage(senderId,"Tui la bot day " + text),HttpStatus.OK);
        }
        return responseEntity;
    }

    private String sendMessage(String senderId, String message) {
        String str = " {\n" +
                "   url: 'https://graph.facebook.com/v2.6/me/messages',\n" +
                "   qs: {\n" +
                "     access_token: \"token\",\n" +
                "   },\n" +
                "   method: 'POST',\n" +
                "   json: {\n" +
                "     recipient: {\n" +
                "       id:"+ senderId +"\n" +
                "     },\n" +
                "     message: {\n" +
                "       text:"+ message+"\n" +
                "     },\n" +
                "   }\n" +
                " }";
        return str;
    }
}
