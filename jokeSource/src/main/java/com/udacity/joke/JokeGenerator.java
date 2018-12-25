package com.udacity.joke;

public class JokeGenerator {
    public String getJoke() {
        String joke1="Q. What is the biggest lie in the entire universe? \nA.I have read and agree to the Terms & Conditions.";
        String joke2="Why do Java developers wear glasses? Because they can't C#";
        String joke3=" An SEO expert walks into a bar, bars, pub, tavern, public house, Irish pub, drinks, beer, alcohol";
        String joke4="\"Knock, knock. Who's there?\"\n" +
                "very long pause...\n" +
                "\"Java.\"";
        String joke5="There's a band called 1023MB. They haven't had any gigs yet.";
        String[] jokes={joke1,joke2,joke3,joke4,joke5};
        int index=(int)(Math.random()*5);
        return jokes[index];
    }
}
