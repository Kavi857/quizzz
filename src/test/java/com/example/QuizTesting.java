package com.example;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

public class QuizTesting {

    @Test
    void quizAppFlow() {

        System.out.println("===== PLAYWRIGHT TEST STARTED =====");

        try (Playwright playwright = Playwright.create()) {

            System.out.println("Playwright created");

            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            .setHeadless(false)
            );

            System.out.println("Browser opened");

            Page page = browser.newPage();

            // Open Quiz Application
            page.navigate("http://localhost:9090/quizapp/");

            System.out.println("Page opened");
            System.out.println("Current URL: " + page.url());
            System.out.println("Page title: " + page.title());

            // =========================================
            // YOUR RECORDED PLAYWRIGHT ACTIONS
            // =========================================

            // Select Category
            page.locator("#quizap__Categories__el_btn_3_0").click();

            // Question 1
            page.locator("#quizap__Questions__el_inp_1_0").click();
            page.locator("#quizap__Questions__el_inp_1_0").fill("A");

            // Question 2
            page.locator("#quizap__Questions__el_inp_1_1").click();
            page.locator("#quizap__Questions__el_inp_1_1").fill("AA");

            // Question 3
            page.locator("#quizap__Questions__el_inp_1_2").click();

            // Question 2 again
            page.locator("#quizap__Questions__el_inp_1_1").click();
            page.locator("#quizap__Questions__el_inp_1_1").fill("A");

            // Question 3
            page.locator("#quizap__Questions__el_inp_1_2").click();
            page.locator("#quizap__Questions__el_inp_1_2").fill("A");

            // Question 4
            page.locator("#quizap__Questions__el_inp_1_3").click();
            page.locator("#quizap__Questions__el_inp_1_3").fill("A");

            // Question 5
            page.locator("#quizap__Questions__el_inp_1_4").click();
            page.locator("#quizap__Questions__el_inp_1_4").fill("A");

            // Submit
            page.getByRole(
                    AriaRole.BUTTON,
                    new Page.GetByRoleOptions()
                            .setName("Submit")
            ).click();

            // OK
            page.getByRole(
                    AriaRole.BUTTON,
                    new Page.GetByRoleOptions()
                            .setName("Ok")
            ).click();

            // View Results
            page.getByRole(
                    AriaRole.BUTTON,
                    new Page.GetByRoleOptions()
                            .setName("view results")
            ).click();

            System.out.println("===== QUIZ COMPLETED =====");

            // Open Playwright Inspector
            

            browser.close();

            System.out.println("Browser closed");
        }

        System.out.println("===== PLAYWRIGHT TEST FINISHED =====");
    }
}