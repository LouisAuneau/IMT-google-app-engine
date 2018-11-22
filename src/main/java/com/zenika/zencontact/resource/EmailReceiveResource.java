package com.zenika.zencontact.resource;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.zenika.zencontact.domain.Email;
import com.zenika.zencontact.email.EmailService;

@WebServlet(name = "EmailReceiveResource", value = "/_ah/mail/*")
public class EmailReceiveResource extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        EmailService.getInstance().logEmail(request);
    }
    
}

