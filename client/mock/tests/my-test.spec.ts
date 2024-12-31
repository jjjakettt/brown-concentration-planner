// import { expect, test } from "@playwright/test";
// import { setupClerkTestingToken, clerk } from "@clerk/testing/playwright";

// const url = "http://localhost:8000";

// // If you needed to do something before every test case...
// test.beforeEach(() => {
// });


// // Verifies that the login button is visible on page load.
// test("Testing login/logout", async ({ page }) => {
//   // Notice: http, not https! Our front-end is not set up for HTTPs.
//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();

//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });


//   //await expect(page.getByLabel("Sign Out")).toBeVisible();
//   await expect(page.getByLabel("Retrieve Table")).toBeVisible();
//   await clerk.signOut({ page });

// });


// // Ensures the input box and sign-out button are hidden before login and appear after logging in.
// test("On page load, I don't see the input boxes and submit buttons until after login", async ({ page }) => {
//   // Notice: http, not https! Our front-end is not set up for HTTPs.
//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();

//   await expect(page.getByLabel("Sign Out")).not.toBeVisible();
//   await expect(page.getByLabel("table-dropdown")).not.toBeVisible(); //Switching tables
//   await expect(page.getByLabel("format-dropdown")).not.toBeVisible(); // Switching formats
//   await expect(page.getByLabel("table-name-input")).not.toBeVisible(); //space to enter table name
//   await expect(page.getByLabel("Add Table Name")).not.toBeVisible(); //Add Table Name button
//   await expect(page.getByLabel("Retrieve Table")).not.toBeVisible();

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });
//   await expect(page.getByLabel("table-dropdown")).toBeVisible();
//   await expect(page.getByLabel("format-dropdown")).toBeVisible();
//   await expect(page.getByLabel("table-name-input")).toBeVisible();
//   await expect(page.getByLabel("Add Table Name")).toBeVisible();
//   await expect(page.getByLabel("Retrieve Table")).toBeVisible();
// });

// // Confirms that the submit button ("Retrieve Table") is visible after logging in.
// test("On page load, I see a submit button", async ({ page }) => {
//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();
//   await expect(page.getByText("Retrieve Table")).not.toBeVisible();

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });

//   await expect(page.getByText("Retrieve Table")).toBeVisible();
// });

// // Checks if the default message asking to select a table is visible after login.
// test("On login, I see the message 'Please select a table to view`", async ({
//   page,
// }) => {
//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });

//   await expect(page.getByLabel("Default Message")).toBeVisible();
// });

// // Checks if an invalid table is entered it shows an error message
// test("On submit of an invalid (non-existing) table, I see the message 'No data available for the selected table`.", async ({
//   page,
// }) => {
//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });
  
//   await page.getByLabel("table-name-input").fill("empty")
//   await page.getByLabel("Add Table Name").click();
//   await page.getByLabel("table-dropdown").selectOption("empty");
//   await page.getByLabel("Retrieve Table").click();
//   await expect(page.getByLabel("Empty Table")).toBeVisible();
// });

// // // Tests entering table (postsecondary_education.csv), selecting table, and displaying table
// test("On table retrieval (postsecondary_education.csv), I see the table", async ({
//   page,
// }) => {
  
//   // await page.goto("http://localhost:8000/");
//   // await expect(page.getByLabel("Sign Out")).not.toBeVisible();
//   // await expect(page.getByLabel("table-dropdown")).not.toBeVisible();

//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });


//   await page.getByLabel("table-name-input").fill("postsecondary_education.csv")
//   await page.getByLabel("Add Table Name").click();
//   await page.getByLabel("table-dropdown").selectOption("postsecondary_education.csv");
//   await page.getByLabel("format-dropdown").selectOption("Tables")
//   await page.getByLabel("Retrieve Table").click();
//   await expect(page.getByRole('table')).toBeVisible();
//   await expect(page.getByLabel('postsecondary_education')).toBeVisible();

// });

// // Tests entering table, selecting table, and displaying table, mulitple times
// test("Entering two tables, selecting one, displaying it, switching table (without changing format) and displaying again", async ({
//   page,
// }) => {
//   // await page.goto("http://localhost:8000/");
//   // await expect(page.getByLabel("Sign Out")).not.toBeVisible();
//   // await expect(page.getByLabel("table-dropdown")).not.toBeVisible();

//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });
//   await page.getByLabel("table-name-input").fill("postsecondary_education.csv")
//   await page.getByLabel("Add Table Name").click();
//   await page.getByLabel("table-dropdown").selectOption("postsecondary_education.csv");
//   await page.getByLabel("format-dropdown").selectOption("Tables")
//   await page.getByLabel("Retrieve Table").click();
//   await expect(page.getByRole('table')).toBeVisible();
//   await expect(page.getByLabel('postsecondary_education')).toBeVisible();

//   await page.getByLabel("table-name-input").fill("ten-star.csv")
//   await page.getByLabel("Add Table Name").click();
//   await page.getByLabel("table-dropdown").selectOption("ten-star.csv");
//   await page.getByLabel("format-dropdown").selectOption("Tables")
//   await page.getByLabel("Retrieve Table").click();
//   await expect(page.getByRole('table')).toBeVisible();
//   await expect(page.getByLabel('ten-star')).toBeVisible();
// });


// // // tests mulitple entries, format switches and table switches
// test("Entering multiple entries, selecting one, displaying it in tabular form, switching format, displaying it in Bar Chart, and switching to another table, and displaying it", async ({
//   page,
// }) => {
//   // await page.goto("http://localhost:8000/");
//   // await expect(page.getByLabel("Sign Out")).not.toBeVisible();
//   // await expect(page.getByLabel("table-dropdown")).not.toBeVisible();

//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });
//   // adds the first two entries
//   await page.getByLabel("table-name-input").fill("postsecondary_education.csv")
//   await page.getByLabel("Add Table Name").click();
//   await page.getByLabel("table-name-input").fill("ten-star.csv")
//   await page.getByLabel("Add Table Name").click();


//   //checking if one displays 
//   await page.getByLabel("table-dropdown").selectOption("postsecondary_education.csv");
//   await page.getByLabel("format-dropdown").selectOption("Tables")
//   await page.getByLabel("Retrieve Table").click();
//   await expect(page.getByRole('table')).toBeVisible();
//   await expect(page.getByLabel('postsecondary_education')).toBeVisible();

//   // Switching formats
//   await page.getByLabel('format-dropdown').selectOption("Bar Chart");
//   await page.getByLabel("Retrieve Table").click();
//   await expect(page.getByLabel('Bar Chart')).toBeVisible();
//   await expect(page.getByText('Skipped Attributes: postsecondary_education.csv')).toBeVisible();

//   // Adds third entry
//   await page.getByLabel("table-name-input").fill("malformed_signs.csv")
//   await page.getByLabel("Add Table Name").click();

//   await page.getByLabel("table-dropdown").selectOption("malformed_signs.csv");
//   await page.getByLabel("format-dropdown").selectOption("Tables")
//   await page.getByLabel("Retrieve Table").click();
//   await expect(page.getByRole('table')).toBeVisible();
//   await expect(page.getByLabel('malformed_signs')).toBeVisible();
// });

// // // tests switching formats multiple times
// test("When I switch to different formats, it should display the selected format (table, bar chart, stacked bar chart", async ({
//   page,
// }) => {
//   // await page.goto("http://localhost:8000/");
//   // await expect(page.getByLabel("Sign Out")).not.toBeVisible();
//   // await expect(page.getByLabel("table-dropdown")).not.toBeVisible();

//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });


//   await page.getByLabel("table-name-input").fill("postsecondary_education.csv")
//   await page.getByLabel("Add Table Name").click();


//   // Displays data in tabular form
//   await page.getByLabel("table-dropdown").selectOption("postsecondary_education.csv");
//   await page.getByLabel("format-dropdown").selectOption("Tables")
//   await page.getByLabel("Retrieve Table").click();
//   await expect(page.getByRole('table')).toBeVisible();
//   await expect(page.getByLabel('postsecondary_education')).toBeVisible();

//   // Switching to bar chart
//   await page.getByLabel('format-dropdown').selectOption("Bar Chart");
//   await page.getByLabel("Retrieve Table").click();
//   await expect(page.getByLabel('Bar Chart')).toBeVisible();
//   await expect(page.getByText('Skipped Attributes: postsecondary_education.csv')).toBeVisible();
//   await expect(page.getByText('University     Slug University     Sex     ')).toBeVisible();

//   // Switching stacked bar chart
//   await page.getByLabel('format-dropdown').selectOption("Stacked Bar Chart");
//   await page.getByLabel("Retrieve Table").click();
//   await expect(page.getByLabel('Stacked Bar Chart')).toBeVisible();
//   await expect(page.getByText('Skipped Attributes:')).toBeVisible();
//   await expect(page.getByText('University     Slug University     Sex     ')).toBeVisible({ timeout: 50000 });
// });

// // // S_DIST 4.1

// // testing empty search
// test("When I load a file and input an empty search input, it displays 'Please enter a search term.'", async ({
//   page,
// }) => {
//   // await page.goto("http://localhost:8000/");
//   // await expect(page.getByLabel("Sign Out")).not.toBeVisible();
//   // await expect(page.getByLabel("table-dropdown")).not.toBeVisible();

//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });


//   await page.getByLabel("table-name-input").fill("RIT_Income/RIT_Data.csv")
//   await page.getByLabel("Add Table Name").click();


//   // Displays data in tabular form
//   await page.getByLabel("table-dropdown").selectOption("RIT_Income/RIT_Data.csv");
//   await page.getByLabel("format-dropdown").selectOption("Tables")
//   await page.getByLabel("Retrieve Table").click();

//   await expect(page.getByRole('table')).toBeVisible();
//   await expect(page.getByLabel('RIT_Income/RIT_Data.csv')).toBeVisible();

//   // Entering empty search value
//   await page.getByLabel("csv-search-input").fill("");
//   await page.getByLabel("Search Button").click();
//   await expect(page.getByText('Please enter a search term.')).toBeVisible({ timeout: 50000 });
// });

// // testing invalid search
// test("When I load a file and input an invalid search input, it displays 'No data found for the given search term.'", async ({
//   page,
// }) => {
//   await page.goto("http://localhost:8000/");

//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });


//   await page.getByLabel("table-name-input").fill("RIT_Income/RIT_Data.csv")
//   await page.getByLabel("Add Table Name").click();


//   // Displays data in tabular form
//   await page.getByLabel("table-dropdown").selectOption("RIT_Income/RIT_Data.csv");
//   await page.getByLabel("format-dropdown").selectOption("Tables")
//   await page.getByLabel("Retrieve Table").click();

//   await expect(page.getByRole('table')).toBeVisible();
//   await expect(page.getByLabel('RIT_Income/RIT_Data.csv')).toBeVisible();

//   // Entering empty search value
//   await page.getByLabel("csv-search-input").fill("ac");
//   await page.getByLabel("Search Button").click();
//   await expect(page.getByText('No data found for the given search term.')).toBeVisible({ timeout: 50000 });
// });

// // // testing valid search
// test("When I load a file and input an vaild search input, it displays the search results", async ({
//   page,
// }) => {
//   // await page.goto("http://localhost:8000/");

//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });


//   await page.getByLabel("table-name-input").fill("RIT_Income/RIT_Data.csv")
//   await page.getByLabel("Add Table Name").click();


//   // Displays data in tabular form
//   await page.getByLabel("table-dropdown").selectOption("RIT_Income/RIT_Data.csv");
//   await page.getByLabel("format-dropdown").selectOption("Tables")
//   await page.getByLabel("Retrieve Table").click();

//   await expect(page.getByRole('table')).toBeVisible();
//   await expect(page.getByLabel('RIT_Income/RIT_Data.csv')).toBeVisible();

//   // Entering valid search value
//   await page.getByLabel("csv-search-input").fill("Rhode Island");
//   await page.getByLabel("Search Button").click();
//   await expect(page.getByText('Please enter a search term.')).not.toBeVisible({ timeout: 50000 });
//   await expect(page.getByText('No data found for the given search term.')).not.toBeVisible({ timeout: 50000 });

//   await expect(page.getByText('Matches:')).toBeVisible({ timeout: 50000 });
//   await expect(page.getByText('[Rhode Island, "74, 489.00", "95, 198.00", "39, 603.00"]')).toBeVisible({ timeout: 50000 });
// });

// // testing mulitple search results
// test("When I load a file and input an vaild search input with multiple potential results, it displays all the search results", async ({
//   page,
// }) => {
//   // await page.goto("http://localhost:8000/");

//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });


//   await page.getByLabel("table-name-input").fill("RIT_Income/RIT_Data.csv")
//   await page.getByLabel("Add Table Name").click();


//   // Displays data in tabular form
//   await page.getByLabel("table-dropdown").selectOption("RIT_Income/RIT_Data.csv");
//   await page.getByLabel("format-dropdown").selectOption("Tables")
//   await page.getByLabel("Retrieve Table").click();

//   await expect(page.getByRole('table')).toBeVisible();
//   await expect(page.getByLabel('RIT_Income/RIT_Data.csv')).toBeVisible();

//   // Entering valid search value
//   await page.getByLabel("csv-search-input").fill("B");
//   await page.getByLabel("Search Button").click();
//   await expect(page.getByText('Please enter a search term.')).not.toBeVisible({ timeout: 50000 });
//   await expect(page.getByText('No data found for the given search term.')).not.toBeVisible({ timeout: 50000 });

//   await expect(page.getByText('Matches:')).toBeVisible({ timeout: 50000 });
//   await expect(page.getByText('[Barrington, "130, 455.00", "154, 441.00", "69, 917.00"]')).toBeVisible({ timeout: 50000 });
//   await expect(page.getByText('[Bristol, "80, 727.00", "115, 740.00", "42, 658.00"]')).toBeVisible({ timeout: 50000 });
//   await expect(page.getByText('[Burrillville, "96, 824.00", "109, 340.00", "39, 470.00"]')).toBeVisible({ timeout: 50000 });
// });

// // testing mulitple searches for different values
// test("When I load a file and input an vaild search input, it displays all the search results, and if I search another value, it displays that search result", async ({
//   page,
// }) => {
//   // await page.goto("http://localhost:8000/");

//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });


//   await page.getByLabel("table-name-input").fill("RIT_Income/RIT_Data.csv")
//   await page.getByLabel("Add Table Name").click();


//   // Displays data in tabular form
//   await page.getByLabel("table-dropdown").selectOption("RIT_Income/RIT_Data.csv");
//   await page.getByLabel("format-dropdown").selectOption("Tables")
//   await page.getByLabel("Retrieve Table").click();

//   await expect(page.getByRole('table')).toBeVisible();
//   await expect(page.getByLabel('RIT_Income/RIT_Data.csv')).toBeVisible();

//   // Entering first search value
//   await page.getByLabel("csv-search-input").fill("Rhode Island");
//   await page.getByLabel("Search Button").click();
//   await expect(page.getByText('Please enter a search term.')).not.toBeVisible({ timeout: 50000 });
//   await expect(page.getByText('No data found for the given search term.')).not.toBeVisible({ timeout: 50000 });

//   await expect(page.getByText('Matches:')).toBeVisible({ timeout: 50000 });
//   await expect(page.getByText('[Rhode Island, "74, 489.00", "95, 198.00", "39, 603.00"]')).toBeVisible({ timeout: 50000 });

//   // Entering second search value
//   await page.getByLabel("csv-search-input").fill("Bristol");
//   await page.getByLabel("Search Button").click();
//   await expect(page.getByText('Please enter a search term.')).not.toBeVisible({ timeout: 50000 });
//   await expect(page.getByText('No data found for the given search term.')).not.toBeVisible({ timeout: 50000 });

//   await expect(page.getByText('Matches:')).toBeVisible({ timeout: 50000 });
//   await expect(page.getByText('[Bristol, "80, 727.00", "115, 740.00", "42, 658.00"]')).toBeVisible({ timeout: 50000 });
// });

// // SPRINT 4.2

// // testing broadband searches
// test("Broadband calls return expected result", async ({
//   page,
// }) => {
//   // await page.goto("http://localhost:8000/");

//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });

//   // Entering a broadband value
//   await page.getByLabel("variable-name-input").fill("S2802_C03_022E");
//   await page.getByLabel("state-input").fill("Texas");
//   await page.getByLabel("county-input").fill("Orange County");
//   await page.getByLabel("Submit Variable").click();

//   await expect(page.getByText('90.4')).toBeVisible({ timeout: 50000 });

//   // Entering second broadband value + checking broadband button works
//   await page.getByLabel("variable-name-input").fill("notavariable");
//   await page.getByLabel("Broadband").click();
//   await page.getByLabel("state-input").fill("Texas");
//   await page.getByLabel("county-input").fill("Bexar County");
//   await page.getByLabel("Submit Variable").click();

//   await expect(page.getByText('87.6')).toBeVisible({ timeout: 50000 });
// });



// // testing other variable searches
// test("Variables other than broadband return expected result", async ({
//   page,
// }) => {
//   // await page.goto("http://localhost:8000/");

//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });

//   // Entering a variable
//   await page.getByLabel("variable-name-input").fill("S2801_C02_019E");
//   await page.getByLabel("state-input").fill("Texas");
//   await page.getByLabel("county-input").fill("McLennan County");
//   await page.getByLabel("Submit Variable").click();

//   await expect(page.getByText('10.8')).toBeVisible({ timeout: 50000 });

//   // Entering second variable
//   await page.getByLabel("variable-name-input").fill("S2801_C02_019E");
//   await page.getByLabel("state-input").fill("Texas");
//   await page.getByLabel("county-input").fill("Midland County");
//   await page.getByLabel("Submit Variable").click();

//   await expect(page.getByText('7.1')).toBeVisible({ timeout: 50000 });
// });

// // testing invalid variable searches
// test("Invalid broadband calls return correct error message", async ({
//   page,
// }) => {
//   // await page.goto("http://localhost:8000/");

//   setupClerkTestingToken({ page });
//   await page.goto(url);
//   await clerk.loaded({ page });
//   const loginButton = page.getByRole("button", { name: "Sign In" });
//   await expect(loginButton).toBeVisible();;

//   // signing in
//   await clerk.signIn({
//     page,
//     signInParams: {
//       strategy: "password",
//       password: process.env.E2E_CLERK_USER_PASSWORD!,
//       identifier: process.env.E2E_CLERK_USER_USERNAME!,
//     },
//   });

//   // Entering an invalid variable - invalid variable error message should show
//   await page.getByLabel("variable-name-input").fill("Snotavariable");
//   await page.getByLabel("state-input").fill("Texas");
//   await page.getByLabel("county-input").fill("McLennan County");
//   await page.getByLabel("Submit Variable").click();

//   await expect(page.getByText('An unexpected error occurred: Error: Input variable is not valid')).toBeVisible({ timeout: 50000 });

//   // Entering invalid state - invalid state error message should show
//   await page.getByLabel("variable-name-input").fill("S2801_C02_019E");
//   await page.getByLabel("state-input").fill("notastate");
//   await page.getByLabel("county-input").fill("Midland County");
//   await page.getByLabel("Submit Variable").click();

//   await expect(page.getByText('An unexpected error occurred: Error: Input variable is not valid')).not.toBeVisible({ timeout: 50000 });
//   await expect(page.getByText('An unexpected error occurred: Invalid state name')).toBeVisible({ timeout: 50000 });
// });
