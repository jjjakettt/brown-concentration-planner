import {
  clerkSetup,
  setupClerkTestingToken,
  clerk,
} from "@clerk/testing/playwright";
import { expect, test } from "@playwright/test";

import { test as setup } from "@playwright/test";

/**
  The general shapes of tests in Playwright Test are:
    1. Navigate to a URL
    2. Interact with the page
    3. Assert something about the page against your expectations
  Look for this pattern in the tests below!
 */

setup("global setup", async ({}) => {
  await clerkSetup();
});

test("checking front end cells (log in, load in, table screen)", async ({
  page,
}) => {
  await clerkSetup();
  setupClerkTestingToken({ page });
  await page.goto("http://localhost:8000/");
  await clerk.loaded({ page });
  await expect(
    page.getByRole("heading", { name: "Welcome to Brown" })
  ).toBeVisible();
  await expect(page.getByRole("heading")).toContainText(
    "Welcome to Brown Concentration Planner"
  );
  await expect(page.getByText("Please sign in with your")).toBeVisible();
  await expect(page.getByRole("paragraph")).toContainText(
    "Please sign in with your Brown Account to start planning your academic journey"
  );
  await expect(
    page.getByRole("button", { name: "Sign In with Brown Account" })
  ).toBeVisible();
  await expect(page.getByRole("button")).toContainText(
    "Sign In with Brown Account"
  );

  const loginButton = page.getByRole("button", { name: "Sign in" });
  await expect(loginButton).toBeVisible();

  await clerk.signIn({
    page,
    signInParams: {
      strategy: "password",
      password: process.env.E2E_CLERK_USER_PASSWORD!,
      identifier: process.env.E2E_CLERK_USER_USERNAME!,
    },
  });

  await expect(page.getByText("BROWN UNIVERSITY")).toBeVisible();
  await expect(page.locator("#root")).toContainText("BROWN UNIVERSITY");

  await expect(page.getByRole("heading", { name: "Semester 1" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 1");
  await expect(page.getByRole("heading", { name: "Semester 3" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 3");
  await expect(page.getByRole("heading", { name: "Semester 6" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 6");

  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 1No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");

  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 8No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");

  await expect(page.getByRole("button", { name: "Course Bank" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Course Bank");

  await expect(page.getByRole("button", { name: "Sign Out" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Sign Out");
  await page.getByRole("button", { name: "Sign Out" }).click();
  await expect(
    page.getByRole("heading", { name: "Welcome to Brown" })
  ).toBeVisible();
  await expect(page.getByRole("heading")).toContainText(
    "Welcome to Brown Concentration Planner"
  );
});

test("sign in / sign out, then try a bad email)", async ({ page }) => {
  await clerkSetup();
  setupClerkTestingToken({ page });
  await page.goto("http://localhost:8000/");
  await clerk.loaded({ page });
  await expect(
    page.getByRole("heading", { name: "Welcome to Brown" })
  ).toBeVisible();
  await expect(page.getByRole("heading")).toContainText(
    "Welcome to Brown Concentration Planner"
  );
  await expect(page.getByText("Please sign in with your")).toBeVisible();
  await expect(page.getByRole("paragraph")).toContainText(
    "Please sign in with your Brown Account to start planning your academic journey"
  );
  await expect(
    page.getByRole("button", { name: "Sign In with Brown Account" })
  ).toBeVisible();
  await expect(page.getByRole("button")).toContainText(
    "Sign In with Brown Account"
  );

  const loginButton = page.getByRole("button", { name: "Sign in" });
  await expect(loginButton).toBeVisible();

  await clerk.signIn({
    page,
    signInParams: {
      strategy: "password",
      password: process.env.E2E_CLERK_USER_PASSWORD!,
      identifier: process.env.E2E_CLERK_USER_USERNAME!,
    },
  });

  await expect(page.getByText("BROWN UNIVERSITY")).toBeVisible();
  await expect(page.locator("#root")).toContainText("BROWN UNIVERSITY");

  await expect(page.getByRole("heading", { name: "Semester 1" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 1");
  await expect(page.getByRole("heading", { name: "Semester 3" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 3");
  await expect(page.getByRole("heading", { name: "Semester 6" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 6");

  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 1No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");

  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 8No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");

  await expect(page.getByRole("button", { name: "Course Bank" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Course Bank");

  await expect(page.getByRole("button", { name: "Sign Out" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Sign Out");
  await page.getByRole("button", { name: "Sign Out" }).click();
  await expect(
    page.getByRole("heading", { name: "Welcome to Brown" })
  ).toBeVisible();
  await expect(page.getByRole("heading")).toContainText(
    "Welcome to Brown Concentration Planner"
  );

  await page
    .getByRole("button", { name: "Sign In with Brown Account" })
    .click();
  await page.getByPlaceholder("Enter your email address").click();
  await page
    .getByPlaceholder("Enter your email address")
    .fill("will@gmail.com");
  await page.getByRole("button", { name: "Continue", exact: true }).click();
  await expect(page.getByText("No account found with this")).toBeVisible();
  await expect(page.locator("#error-identifier")).toContainText(
    "No account found with this identifier. Please check and try again."
  );
});

test("table checking", async ({ page }) => {
  await clerkSetup();
  setupClerkTestingToken({ page });
  await page.goto("http://localhost:8000/");
  await clerk.loaded({ page });
  await expect(
    page.getByRole("heading", { name: "Welcome to Brown" })
  ).toBeVisible();
  await expect(page.getByRole("heading")).toContainText(
    "Welcome to Brown Concentration Planner"
  );
  await expect(page.getByText("Please sign in with your")).toBeVisible();
  await expect(page.getByRole("paragraph")).toContainText(
    "Please sign in with your Brown Account to start planning your academic journey"
  );
  await expect(
    page.getByRole("button", { name: "Sign In with Brown Account" })
  ).toBeVisible();
  await expect(page.getByRole("button")).toContainText(
    "Sign In with Brown Account"
  );

  const loginButton = page.getByRole("button", { name: "Sign in" });
  await expect(loginButton).toBeVisible();

  await clerk.signIn({
    page,
    signInParams: {
      strategy: "password",
      password: process.env.E2E_CLERK_USER_PASSWORD!,
      identifier: process.env.E2E_CLERK_USER_USERNAME!,
    },
  });

  await expect(page.getByText("BROWN UNIVERSITY")).toBeVisible();
  await expect(page.locator("#root")).toContainText("BROWN UNIVERSITY");
  await expect(page.getByRole("button", { name: "Sign Out" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Sign Out");

  await expect(page.getByLabel("Submit the selected dataset")).toBeVisible();
  await expect(page.getByLabel("Submit the selected dataset")).toContainText(
    "View Computer Science Concentration Requirements"
  );
  await page.getByLabel("Submit the selected dataset").click();

  await expect(
    page.getByRole("cell", {
      name: "Computing Foundations: Program Organization",
    })
  ).toBeVisible();
  await expect(page.getByRole("rowgroup")).toContainText(
    "Computing Foundations: Program Organization"
  );
  await expect(
    page.getByRole("cell", { name: "an additional CS course not" })
  ).toBeVisible();
  await expect(page.getByRole("rowgroup")).toContainText(
    "an additional CS course not otherwise used to satisfy a concentration requirement; this course may be CSCI 0200, a Foundations course, or a 1000-level course."
  );
  await expect(page.getByText("Total Credits")).toBeVisible();
  await expect(page.getByRole("rowgroup")).toContainText("Total Credits");
});

test("adding and removing 1 course from planner", async ({ page }) => {
  await clerkSetup();
  setupClerkTestingToken({ page });
  await page.goto("http://localhost:8000/");
  await clerk.loaded({ page });
  await expect(
    page.getByRole("heading", { name: "Welcome to Brown" })
  ).toBeVisible();
  await expect(page.getByRole("heading")).toContainText(
    "Welcome to Brown Concentration Planner"
  );
  await expect(page.getByText("Please sign in with your")).toBeVisible();
  await expect(page.getByRole("paragraph")).toContainText(
    "Please sign in with your Brown Account to start planning your academic journey"
  );
  await expect(
    page.getByRole("button", { name: "Sign In with Brown Account" })
  ).toBeVisible();
  await expect(page.getByRole("button")).toContainText(
    "Sign In with Brown Account"
  );

  const loginButton = page.getByRole("button", { name: "Sign in" });
  await expect(loginButton).toBeVisible();

  await clerk.signIn({
    page,
    signInParams: {
      strategy: "password",
      password: process.env.E2E_CLERK_USER_PASSWORD!,
      identifier: process.env.E2E_CLERK_USER_USERNAME!,
    },
  });

  await expect(page.getByText("BROWN UNIVERSITY")).toBeVisible();
  await expect(page.locator("#root")).toContainText("BROWN UNIVERSITY");
  await expect(page.getByRole("button", { name: "Sign Out" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Sign Out");
  await expect(page.getByRole("heading", { name: "Semester 1" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 1");
  await expect(page.getByRole("heading", { name: "Semester 3" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 3");
  await expect(page.getByRole("heading", { name: "Semester 6" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 6");

  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 1No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");

  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 8No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");

  await expect(page.getByRole("button", { name: "Course Bank" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Course Bank");
  await page.getByRole("button", { name: "Course Bank" }).click();

  await expect(
    page.getByRole("cell", { name: "The Digital World" })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("The Digital World");
  await expect(
    page.getByRole("cell", { name: "John Hughes" }).first()
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("John Hughes");

  await page
    .getByRole("row", { name: "The Digital World CSCI 0020" })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 1" }).click();

  await expect(
    page.getByRole("button", { name: "Back to Planner" })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("Back to Planner");
  await page.getByRole("button", { name: "Back to Planner" }).click();

  await expect(page.getByText("CSCI 0020")).toBeVisible();
  await expect(page.locator("#root")).toContainText("CSCI 0020");
  await expect(page.getByText("The Digital World")).toBeVisible();
  await expect(page.locator("#root")).toContainText("The Digital World");

  await page.getByRole("button", { name: "x" }).click();
  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 1No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();

  await expect(page.locator("#root")).toContainText("No courses added");
  await page.getByRole("button", { name: "Course Bank" }).click();
});

test("searching for classes", async ({ page }) => {
  await clerkSetup();
  setupClerkTestingToken({ page });
  await page.goto("http://localhost:8000/");
  await clerk.loaded({ page });
  await expect(
    page.getByRole("heading", { name: "Welcome to Brown" })
  ).toBeVisible();
  await expect(page.getByRole("heading")).toContainText(
    "Welcome to Brown Concentration Planner"
  );
  await expect(page.getByText("Please sign in with your")).toBeVisible();
  await expect(page.getByRole("paragraph")).toContainText(
    "Please sign in with your Brown Account to start planning your academic journey"
  );
  await expect(
    page.getByRole("button", { name: "Sign In with Brown Account" })
  ).toBeVisible();
  await expect(page.getByRole("button")).toContainText(
    "Sign In with Brown Account"
  );

  const loginButton = page.getByRole("button", { name: "Sign in" });
  await expect(loginButton).toBeVisible();

  await clerk.signIn({
    page,
    signInParams: {
      strategy: "password",
      password: process.env.E2E_CLERK_USER_PASSWORD!,
      identifier: process.env.E2E_CLERK_USER_USERNAME!,
    },
  });

  await expect(page.getByText("BROWN UNIVERSITY")).toBeVisible();
  await expect(page.locator("#root")).toContainText("BROWN UNIVERSITY");
  await expect(page.getByRole("button", { name: "Sign Out" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Sign Out");
  await expect(page.getByRole("heading", { name: "Semester 1" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 1");
  await expect(page.getByRole("heading", { name: "Semester 3" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 3");
  await expect(page.getByRole("heading", { name: "Semester 6" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 6");

  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 1No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");

  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 8No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");

  await expect(page.getByRole("button", { name: "Course Bank" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Course Bank");
  await page.getByRole("button", { name: "Course Bank" }).click();

  await expect(
    page.getByRole("button", { name: "Back to Planner" })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("Back to Planner");

  await page.getByPlaceholder("Search courses...").click();
  await page.getByPlaceholder("Search courses...").fill("software");

  await expect(
    page.getByRole("cell", { name: "Software Engineering", exact: true })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("Software Engineering");
  await expect(page.getByRole("cell", { name: "Tim Nelson" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Tim Nelson");
  await page
    .getByRole("row", { name: "Software Engineering" })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 1" }).click();

  await page.getByRole("button", { name: "Back to Planner" }).click();
  await expect(page.getByText("CSCI 0320")).toBeVisible();
  await expect(page.locator("#root")).toContainText("CSCI 0320");
  await expect(page.getByText("Software Engineering")).toBeVisible();
  await expect(page.locator("#root")).toContainText("Software Engineering");
});

test("adding and deleting multiple classes", async ({ page }) => {
  await clerkSetup();
  setupClerkTestingToken({ page });
  await page.goto("http://localhost:8000/");
  await clerk.loaded({ page });
  await expect(
    page.getByRole("heading", { name: "Welcome to Brown" })
  ).toBeVisible();
  await expect(page.getByRole("heading")).toContainText(
    "Welcome to Brown Concentration Planner"
  );
  await expect(page.getByText("Please sign in with your")).toBeVisible();
  await expect(page.getByRole("paragraph")).toContainText(
    "Please sign in with your Brown Account to start planning your academic journey"
  );
  await expect(
    page.getByRole("button", { name: "Sign In with Brown Account" })
  ).toBeVisible();
  await expect(page.getByRole("button")).toContainText(
    "Sign In with Brown Account"
  );

  const loginButton = page.getByRole("button", { name: "Sign in" });
  await expect(loginButton).toBeVisible();

  await clerk.signIn({
    page,
    signInParams: {
      strategy: "password",
      password: process.env.E2E_CLERK_USER_PASSWORD!,
      identifier: process.env.E2E_CLERK_USER_USERNAME!,
    },
  });

  await expect(page.getByText("BROWN UNIVERSITY")).toBeVisible();
  await expect(page.locator("#root")).toContainText("BROWN UNIVERSITY");
  await expect(page.getByRole("button", { name: "Sign Out" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Sign Out");
  await expect(page.getByRole("heading", { name: "Semester 1" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 1");
  await expect(page.getByRole("heading", { name: "Semester 3" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 3");
  await expect(page.getByRole("heading", { name: "Semester 6" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 6");

  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 1No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");

  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 8No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");

  await expect(page.getByRole("button", { name: "Course Bank" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Course Bank");
  await page.getByRole("button", { name: "Course Bank" }).click();

  await page
    .getByRole("row", {
      name: "Computing Foundations: Data CSCI 0111 Barbara Lerner Fall +",
    })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 1" }).click();

  await page
    .getByRole("row", {
      name: "Computing Foundations: Program Organization CSCI 0112 Kathi Fisler Spring +",
    })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 2" }).click();

  await page
    .getByRole("row", {
      name: "Introduction to Object-Oriented Programming CSCI 0150 Andy van Dam Fall +",
    })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 1" }).click();

  await page
    .getByRole("row", { name: "CS: An Integrated" })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 1" }).click();

  await page
    .getByRole("row", {
      name: "Program Design with Data Structures CSCI 0200 John Jannotti Both +",
    })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 1" }).click();

  await page
    .getByRole("row", {
      name: "Introduction to Discrete Structures CSCI 0220 Philip Klein Both +",
    })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 1" }).click();

  await page.getByRole("button", { name: "Back to Planner" }).click();
  await expect(
    page.locator("div").filter({ hasText: /^CSCI 0111$/ })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("CSCI 0111");
  await expect(
    page.locator("div").filter({ hasText: /^CSCI 0150$/ })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("CSCI 0150");
  await expect(
    page.locator("div").filter({ hasText: /^CSCI 0170$/ })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("CSCI 0170");
  await expect(
    page.locator("div").filter({ hasText: /^CSCI 0200$/ })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("CSCI 0200");
  await expect(
    page.locator("div").filter({ hasText: /^CSCI 0220$/ })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("CSCI 0220");

  await page
    .locator("div")
    .filter({ hasText: /^CSCI 0111Computing Foundations: Datax$/ })
    .getByRole("button")
    .click();

  await page
    .locator("div")
    .filter({
      hasText: /^CSCI 0150Introduction to Object-Oriented Programmingx$/,
    })
    .getByRole("button")
    .click();

  await page
    .locator("div")
    .filter({ hasText: /^CSCI 0170CS: An Integrated Introductionx$/ })
    .getByRole("button")
    .click();

  await page
    .locator("div")
    .filter({ hasText: /^CSCI 0200Program Design with Data Structuresx$/ })
    .getByRole("button")
    .click();

  await page
    .locator("div")
    .filter({
      hasText: /^Semester 1CSCI 0220Introduction to Discrete Structuresx$/,
    })
    .getByRole("button")
    .click();

  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 1No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");
});

test("all", async ({ page }) => {
  await clerkSetup();
  setupClerkTestingToken({ page });
  await page.goto("http://localhost:8000/");
  await clerk.loaded({ page });
  await expect(
    page.getByRole("heading", { name: "Welcome to Brown" })
  ).toBeVisible();
  await expect(page.getByRole("heading")).toContainText(
    "Welcome to Brown Concentration Planner"
  );
  await expect(page.getByText("Please sign in with your")).toBeVisible();
  await expect(page.getByRole("paragraph")).toContainText(
    "Please sign in with your Brown Account to start planning your academic journey"
  );
  await expect(
    page.getByRole("button", { name: "Sign In with Brown Account" })
  ).toBeVisible();
  await expect(page.getByRole("button")).toContainText(
    "Sign In with Brown Account"
  );

  const loginButton = page.getByRole("button", { name: "Sign in" });
  await expect(loginButton).toBeVisible();

  await clerk.signIn({
    page,
    signInParams: {
      strategy: "password",
      password: process.env.E2E_CLERK_USER_PASSWORD!,
      identifier: process.env.E2E_CLERK_USER_USERNAME!,
    },
  });

  await expect(page.getByText("BROWN UNIVERSITY")).toBeVisible();
  await expect(page.locator("#root")).toContainText("BROWN UNIVERSITY");
  await expect(page.getByRole("button", { name: "Sign Out" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Sign Out");
  await expect(page.getByRole("heading", { name: "Semester 1" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 1");
  await expect(page.getByRole("heading", { name: "Semester 3" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 3");
  await expect(page.getByRole("heading", { name: "Semester 6" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Semester 6");

  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 1No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");

  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 8No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");

  await expect(page.getByLabel("Submit the selected dataset")).toBeVisible();
  await expect(page.getByLabel("Submit the selected dataset")).toContainText(
    "View Computer Science Concentration Requirements"
  );
  await page.getByLabel("Submit the selected dataset").click();

  await expect(page.getByRole("button", { name: "Course Bank" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Course Bank");
  await page.getByRole("button", { name: "Course Bank" }).click();

  await expect(
    page.getByRole("button", { name: "Back to Planner" })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("Back to Planner");

  await expect(
    page.getByRole("cell", { name: "The Digital World" })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("The Digital World");
  await expect(
    page.getByRole("cell", { name: "John Hughes" }).first()
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("John Hughes");
  await page.getByPlaceholder("Search courses...").click();
  await page.getByPlaceholder("Search courses...").fill("software");
  await expect(
    page.getByRole("cell", { name: "Software Engineering", exact: true })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("Software Engineering");
  await expect(page.getByRole("cell", { name: "Tim Nelson" })).toBeVisible();
  await expect(page.locator("#root")).toContainText("Tim Nelson");
  await expect(
    page.getByRole("button", { name: "Show All Courses" })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("Show All Courses");
  await page.getByRole("button", { name: "Show All Courses" }).click();
  await page
    .getByRole("row", { name: "The Digital World CSCI 0020" })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 1" }).click();
  await page.getByRole("button", { name: "Back to Planner" }).click();
  await expect(page.getByText("CSCI 0020")).toBeVisible();
  await expect(page.locator("#root")).toContainText("CSCI 0020");
  await expect(page.getByText("The Digital World")).toBeVisible();
  await expect(page.locator("#root")).toContainText("The Digital World");
  await page.getByRole("button", { name: "x" }).click();
  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 1No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");
  await page.getByRole("button", { name: "Course Bank" }).click();
  await page
    .getByRole("row", {
      name: "Computing Foundations: Data CSCI 0111 Barbara Lerner Fall +",
    })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 1" }).click();
  await page
    .getByRole("row", {
      name: "Computing Foundations: Program Organization CSCI 0112 Kathi Fisler Spring +",
    })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 2" }).click();
  await page
    .getByRole("row", {
      name: "Introduction to Object-Oriented Programming CSCI 0150 Andy van Dam Fall +",
    })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 1" }).click();
  await page
    .getByRole("row", { name: "CS: An Integrated" })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 1" }).click();
  await page
    .getByRole("row", {
      name: "Program Design with Data Structures CSCI 0200 John Jannotti Both +",
    })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 1" }).click();
  await page
    .getByRole("row", {
      name: "Introduction to Discrete Structures CSCI 0220 Philip Klein Both +",
    })
    .getByRole("button")
    .click();
  await page.getByRole("button", { name: "Semester 1" }).click();
  await page
    .getByRole("row", { name: "Software Engineering CSCI" })
    .getByRole("button")
    .click();
  await expect(page.locator("#root")).toContainText("Cancel");
  await page.getByRole("button", { name: "Cancel" }).click();
  await page.getByRole("button", { name: "Back to Planner" }).click();
  await expect(
    page.locator("div").filter({ hasText: /^CSCI 0111$/ })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("CSCI 0111");
  await expect(
    page.locator("div").filter({ hasText: /^CSCI 0150$/ })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("CSCI 0150");
  await expect(
    page.locator("div").filter({ hasText: /^CSCI 0170$/ })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("CSCI 0170");
  await expect(
    page.locator("div").filter({ hasText: /^CSCI 0200$/ })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("CSCI 0200");
  await expect(
    page.locator("div").filter({ hasText: /^CSCI 0220$/ })
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("CSCI 0220");
  await page
    .locator("div")
    .filter({ hasText: /^CSCI 0111Computing Foundations: Datax$/ })
    .getByRole("button")
    .click();
  await page
    .locator("div")
    .filter({
      hasText: /^CSCI 0150Introduction to Object-Oriented Programmingx$/,
    })
    .getByRole("button")
    .click();
  await page
    .locator("div")
    .filter({ hasText: /^CSCI 0170CS: An Integrated Introductionx$/ })
    .getByRole("button")
    .click();
  await page
    .locator("div")
    .filter({ hasText: /^CSCI 0200Program Design with Data Structuresx$/ })
    .getByRole("button")
    .click();
  await page
    .locator("div")
    .filter({
      hasText: /^Semester 1CSCI 0220Introduction to Discrete Structuresx$/,
    })
    .getByRole("button")
    .click();
  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 1No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(
    page
      .locator("div")
      .filter({ hasText: /^Semester 1No courses added$/ })
      .locator("div")
      .nth(1)
  ).toBeVisible();
  await expect(page.locator("#root")).toContainText("No courses added");
  await page.getByRole("button", { name: "Sign Out" }).click();
  await expect(
    page.getByRole("heading", { name: "Welcome to Brown" })
  ).toBeVisible();
  await expect(page.getByRole("heading")).toContainText(
    "Welcome to Brown Concentration Planner"
  );
  await page
    .getByRole("button", { name: "Sign In with Brown Account" })
    .click();
  await page.getByPlaceholder("Enter your email address").click();
  await page
    .getByPlaceholder("Enter your email address")
    .fill("will@gmail.com");
  await page.getByRole("button", { name: "Continue", exact: true }).click();
  await expect(page.getByText("No account found with this")).toBeVisible();
  await expect(page.locator("#error-identifier")).toContainText(
    "No account found with this identifier. Please check and try again."
  );

  await expect(
    page.getByRole("cell", {
      name: "Computing Foundations: Program Organization",
    })
  ).toBeVisible();
  await expect(page.getByRole("rowgroup")).toContainText(
    "Computing Foundations: Program Organization"
  );
  await expect(
    page.getByRole("cell", { name: "an additional CS course not" })
  ).toBeVisible();
  await expect(page.getByRole("rowgroup")).toContainText(
    "an additional CS course not otherwise used to satisfy a concentration requirement; this course may be CSCI 0200, a Foundations course, or a 1000-level course."
  );
  await expect(page.getByText("Total Credits")).toBeVisible();
  await expect(page.getByRole("rowgroup")).toContainText("Total Credits");
});


