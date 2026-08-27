package com.example;
 
import org.junit.jupiter.api.Test;
 
import com.microsoft.playwright.Browser;

import com.microsoft.playwright.BrowserContext;

import com.microsoft.playwright.BrowserType;

import com.microsoft.playwright.Page;

import com.microsoft.playwright.Playwright;

import com.microsoft.playwright.options.AriaRole;
 
public class QuizTesting {
 
    @Test

    void quizapp() {
 
        try (Playwright playwright = Playwright.create()) {
 
            Browser browser = playwright.chromium().launch(

                    new BrowserType.LaunchOptions()

                            .setHeadless(false)

            );
 
            BrowserContext context = browser.newContext();

            Page page = context.newPage();
 
            // Open application

            page.navigate("http://localhost:9090/quizapp/");
 
            browser.close();

        }

    }

}
 