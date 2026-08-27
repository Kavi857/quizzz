package com.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

public class QuizTesting {

    @Test
    void quizAppFlow() {

        System.out.println("===== PLAYWRIGHT TEST STARTED =====");

        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                    .setHeadless(false)
            );

            Page page = browser.newPage();

            System.out.println("Browser opened");

            page.navigate("http://localhost:9090/quizapp/");

            System.out.println("Page opened");
            System.out.println("Current URL: " + page.url());
            System.out.println("Page title: " + page.title());

            // Categories
            page.locator("#quizap__Categories__el_btn_3_0").click();

            // Question 1
            page.locator("#quizap__Questions__el_inp_1_0").fill("A");

            // Question 2
            page.locator("#quizap__Questions__el_inp_1_1").fill("A");

            // Question 3
            page.locator("#quizap__Questions__el_inp_1_2").fill("A");

            // Question 4
            page.locator("#quizap__Questions__el_inp_1_3").fill("A");

            // Question 5
            page.locator("#quizap__Questions__el_inp_1_4").fill("A");

            // Submit
            page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Submit")
            ).click();

            // OK
            page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Ok")
            ).click();

            // View Results
            page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("view results")
            ).click();

            System.out.println("===== QUIZ COMPLETED =====");

            page.pause();

            browser.close();
        }

        System.out.println("===== PLAYWRIGHT TEST FINISHED =====");
    }
}