import {
  clerkSetup,
  setupClerkTestingToken,
  clerk,
} from "@clerk/testing/playwright";
import { test as setup } from "@playwright/test";
import { expect, test } from "@playwright/test";

setup("global setup", async ({}) => {
  await clerkSetup();
});

test("login/logout", async ({ page }) => {
  // Notice: http, not https! Our front-end is not set up for HTTPs.
  await clerkSetup();
  setupClerkTestingToken({ page });
  await page.goto("http://localhost:8000/");
  await clerk.loaded({ page });
  const loginButton = page.getByRole("button", { name: "Sign in" });
  await expect(loginButton).toBeVisible();
  // await loginButton.click();

  // This logs in/out via _Clerk_, not via actual component interaction. But that's OK.
  // (Clerk's Playwright guide has an example of filling the login form itself.)
  await clerk.signIn({
    page,
    signInParams: {
      strategy: "password",
      password: process.env.E2E_CLERK_USER_PASSWORD!,
      identifier: process.env.E2E_CLERK_USER_USERNAME!,
    },
  });
  await expect(page.getByRole("button", { name: "Sign out" })).toBeVisible();
  await clerk.signOut({ page });
  // checks successful sign out
  await expect(loginButton).toBeVisible();
});

