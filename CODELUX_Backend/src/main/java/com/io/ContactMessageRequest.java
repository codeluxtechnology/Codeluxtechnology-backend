package com.io;

public record ContactMessageRequest(String fullName, String email, String phone, String subject, String message) {
}
