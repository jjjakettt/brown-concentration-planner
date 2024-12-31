import type { ClerkSetupOptions } from '../common';
type ClerkSetupParams = {
    config: Cypress.PluginConfigOptions;
    options?: ClerkSetupOptions;
};
/**
 * Sets up Clerk for testing by fetching the testing token from the Clerk Backend API.
 *
 * @param config - The Cypress config object.
 * @param options - The Clerk setup options.
 * @param options.publishableKey - The publishable key for your Clerk dev instance.
 * @param options.frontendApiUrl - The frontend API URL for your Clerk dev instance, without the protocol. It overrides the Frontend API URL parsed from the publishable key.
 * @param options.debug - Enable debug logs.
 * @returns The Cypress config object with the Clerk environment variables set.
 *
 * @throws An error if the publishable key or the secret key is not provided.
 * @throws An error if the secret key is from a production instance.
 * @throws An error if the testing token cannot be fetched from the Clerk Backend API.
 * @throws An error if the Cypress config object is not provided.
 */
export declare const clerkSetup: ({ config, options }: ClerkSetupParams) => Promise<Cypress.PluginConfigOptions>;
export {};
//# sourceMappingURL=setup.d.ts.map