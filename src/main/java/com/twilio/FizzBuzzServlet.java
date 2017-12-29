package com.twilio;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.Gather;
import com.twilio.twiml.Redirect;
import com.twilio.twiml.Say;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.type.PhoneNumber;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author MC
 */
public class FizzBuzzServlet extends HttpServlet {



    public static HashMap<String, Integer> SidVersusChoice = new HashMap<String, Integer>();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        VoiceResponse.Builder builder = new VoiceResponse.Builder();

        String digits = request.getParameter("Digits");
        if (digits != null) {

            builder.say(new Say.Builder(fizzBuzzGame(Integer.valueOf(digits))).build());

            String sid = request.getParameter("CallSid");

            SidVersusChoice.put(sid, Integer.valueOf(digits));
        } else {

            appendGather(builder);
        }

        response.setContentType("application/xml");
        try {
            response.getWriter().print(builder.build().toXml());
        } catch (TwiMLException e) {
            throw new RuntimeException(e);
        }
    }


    private static void appendGather(VoiceResponse.Builder builder) {
        builder.gather(new Gather.Builder()
                .say(new Say.Builder("Please enter a number").build())
                .build()
        )
                .redirect(new Redirect.Builder().url("/voice").build());
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
