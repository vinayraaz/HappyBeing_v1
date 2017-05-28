package com.azuyo.happybeing.Utils;

import com.citrus.sdk.Environment;

/**
 * Created by kavya on 14/7/16.
 */
public enum AppEnvironment {
    SANDBOX {
        @Override
        public String getBillUrl() {
            return "http://mentalhealthcure.com/citrus/bill";
        }

        @Override
        public String getVanity() {
            return "nativeSDK";
        }

        @Override
        public String getSignUpId() {
            return "9hh5re3r5q-signup";
        }

        @Override
        public String getSignUpSecret() {
            return "3be4d7bf59c109e76a3619a33c1da9a8";
        }

        @Override
        public String getSignInId() {
            return "9hh5re3r5q-signin";
        }

        @Override
        public String getSignInSecret() {
            return "ffcfaaf6e6e78c2f654791d9d6cb7f09";
        }

        @Override
        public Environment getEnvironment() {
            return Environment.SANDBOX;
        }

    },
    PRODUCTION {
        @Override
        public String getBillUrl() {
            return "http://mentalhealthcure.com/citrus/bill";
        }

        @Override
        public String getVanity() {
            return "nsmiles";
        }

        @Override
        public String getSignUpId() {
            return "ll7zwppa4n-sdk-merchant";
        }

        @Override
        public String getSignUpSecret() {
            return "02201b812996f7692ffe97b0671c838b";
        }

        @Override
        public String getSignInId() {
            return "ll7zwppa4n-sdk-consumer";
        }

        @Override
        public String getSignInSecret() {
            return "741cb81d4f23fe0fd2de8afb57a60192";
        }

        @Override
        public Environment getEnvironment() {
            return Environment.PRODUCTION;
        }
    };

    public abstract String getBillUrl();

    public abstract String getVanity();

    public abstract String getSignUpId();

    public abstract String getSignUpSecret();

    public abstract String getSignInId();

    public abstract String getSignInSecret();

    public abstract Environment getEnvironment();
}
