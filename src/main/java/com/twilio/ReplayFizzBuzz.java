package com.twilio;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.Say;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.type.PhoneNumber;
import java.io.IOException;
import java.net.URI;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author MC
 */


@WebServlet(
        name = "ReplayFizzBuzz",
        urlPatterns = {"/replayFizzBuz"}
)
public class ReplayFizzBuzz extends HttpServlet {

    public static final String ACCOUNT_SID = "ACdabe19f914a1c8467b401a72437ab5df";
    public static final String AUTH_TOKEN = "0706d0c260eb051e7083dc2e5749b852";
    public static final String TWILIO_NUMBER = "3478366163";
    public static final String PUBLIC_IP = "http://7b440ea3.ngrok.io";   // Place a public accessible IP here
    public static int previousSelection = 1;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        VoiceResponse.Builder builder = new VoiceResponse.Builder();

        builder.say(new Say.Builder(fizzBuzzGame(previousSelection)).build());

        response.setContentType("application/xml");
        try {
            response.getWriter().print(builder.build().toXml());
        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("asdasd " + request.getParameter("id") + request.getParameter("phone"));

        TwilioRestClient client = new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN).build();
        PhoneNumber to = new PhoneNumber(request.getParameter("phone").replaceAll("-", ""));
        PhoneNumber from = new PhoneNumber(TWILIO_NUMBER);
        URI uri = URI.create(PUBLIC_IP + "/phone/replayFizzBuzz");
        Call call = Call.creator(to, from, uri).create(client);
        
        String sid = TwilioServlet.timeVersusSID.get(request.getParameter("id")); // This returns the sid of previous call
        if(FizzBuzzServlet.SidVersusChoice.get(sid) != null){
            previousSelection = FizzBuzzServlet.SidVersusChoice.get(sid); // returns old selection
            response.getWriter().print(previousSelection);
        }else
            response.getWriter().print("None");
        
    }

    private static String fizzBuzzGame(int endNumber) {

        String output = "";
        for (int i = 1; i <= endNumber; i++) {
            if (i % 15 == 0) {
                output += "Fizz Buzz, ";
            } else if (i % 3 == 0) {
                output += "Fizz, ";
            } else if (i % 5 == 0) {
                output += "Buzz, ";
            } else {
                output += String.valueOf(i) + ", ";
            }
        }

        return output;
    }

}
