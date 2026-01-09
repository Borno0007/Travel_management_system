package com.tms.util;

import java.security.SecureRandom;

public class OTPUtils {
    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_LENGTH = 6;
    
    /**
     * Generate a random 6-digit OTP
     */
    public static String generateOTP() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    
    /**
     * Check if OTP is expired (valid for 5 minutes)
     */
    public static boolean isOTPExpired(java.sql.Timestamp otpTimestamp) {
        if (otpTimestamp == null) {
            return true;
        }
        
        long currentTime = System.currentTimeMillis();
        long otpTime = otpTimestamp.getTime();
        long differenceInMinutes = (currentTime - otpTime) / (1000 * 60);
        
        return differenceInMinutes > 5; // OTP expires after 5 minutes
    }
}
