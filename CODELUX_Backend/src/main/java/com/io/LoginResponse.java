package com.io;

import java.time.LocalDateTime;

public record LoginResponse(String token, LocalDateTime expiresAt, AdminResponse admin) {
}
