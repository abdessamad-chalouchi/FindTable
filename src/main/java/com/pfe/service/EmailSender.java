package com.pfe.service;

public interface EmailSender {
    void send(String to, String email,String subject);
}
