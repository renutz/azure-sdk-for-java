/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.datafactory.v2018_06_01;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Get GitHub access token request definition.
 */
public class GitHubAccessTokenRequest {
    /**
     * GitHub access code.
     */
    @JsonProperty(value = "gitHubAccessCode", required = true)
    private String gitHubAccessCode;

    /**
     * GitHub application client ID.
     */
    @JsonProperty(value = "gitHubClientId")
    private String gitHubClientId;

    /**
     * GitHub access token base URL.
     */
    @JsonProperty(value = "gitHubAccessTokenBaseUrl", required = true)
    private String gitHubAccessTokenBaseUrl;

    /**
     * Get gitHub access code.
     *
     * @return the gitHubAccessCode value
     */
    public String gitHubAccessCode() {
        return this.gitHubAccessCode;
    }

    /**
     * Set gitHub access code.
     *
     * @param gitHubAccessCode the gitHubAccessCode value to set
     * @return the GitHubAccessTokenRequest object itself.
     */
    public GitHubAccessTokenRequest withGitHubAccessCode(String gitHubAccessCode) {
        this.gitHubAccessCode = gitHubAccessCode;
        return this;
    }

    /**
     * Get gitHub application client ID.
     *
     * @return the gitHubClientId value
     */
    public String gitHubClientId() {
        return this.gitHubClientId;
    }

    /**
     * Set gitHub application client ID.
     *
     * @param gitHubClientId the gitHubClientId value to set
     * @return the GitHubAccessTokenRequest object itself.
     */
    public GitHubAccessTokenRequest withGitHubClientId(String gitHubClientId) {
        this.gitHubClientId = gitHubClientId;
        return this;
    }

    /**
     * Get gitHub access token base URL.
     *
     * @return the gitHubAccessTokenBaseUrl value
     */
    public String gitHubAccessTokenBaseUrl() {
        return this.gitHubAccessTokenBaseUrl;
    }

    /**
     * Set gitHub access token base URL.
     *
     * @param gitHubAccessTokenBaseUrl the gitHubAccessTokenBaseUrl value to set
     * @return the GitHubAccessTokenRequest object itself.
     */
    public GitHubAccessTokenRequest withGitHubAccessTokenBaseUrl(String gitHubAccessTokenBaseUrl) {
        this.gitHubAccessTokenBaseUrl = gitHubAccessTokenBaseUrl;
        return this;
    }

}
