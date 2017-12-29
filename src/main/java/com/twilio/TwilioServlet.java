package com.twilio;

import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;

import com.twilio.type.PhoneNumber;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TwilioServlet extends HttpServlet {

    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID = "ACdabe19f914a1c8467b401a72437ab5df";
    public static final String AUTH_TOKEN = "0706d0c260eb051e7083dc2e5749b852";
    public static final String TWILIO_NUMBER = "3478366163";
    public static final String PUBLIC_IP = "http://7b440ea3.ngrok.io";   // Place a public accessible IP here
    public static HashMap <String,String> timeVersusSID = new HashMap<String,String>();
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Timer timer = new Timer();
        int timeDelay = 0;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        
        TwilioRestClient client = new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN).build();

        if (!request.getParameter("delay").equals("")) {
            timeDelay = Integer.valueOf(request.getParameter("delay"));
        }

        PhoneNumber to = new PhoneNumber(request.getParameter("phone").replaceAll("-", ""));
        PhoneNumber from = new PhoneNumber(TWILIO_NUMBER);
        URI uri = URI.create( PUBLIC_IP + "/phone/fizzBuzz");

        TimerTask delayedThreadStartTask = new TimerTask() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                                           
                        Call call = Call.creator(to, from, uri).create(client);
                        timeVersusSID.put(dateFormat.format(date), call.getSid());
                        
                    }
                }).start();
            }
        };
        response.getWriter().print(dateFormat.format(date));
        timer.schedule(delayedThreadStartTask, TimeUnit.MINUTES.toMillis(timeDelay));

    }

}
