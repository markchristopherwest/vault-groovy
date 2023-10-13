#!groovy
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

node {
    loginVaultAppRole()
}

def loginVaultAppRole() {
    parameters {
        string(name: 'authToken', defaultValue: '')
        string(name: 'mySecret', defaultValue: '')
    }
    stage('Login to Vault via AppRole') {
        env.authToken = loginVault()
    }
    stage('Lookup Vault Secret') {
        env.mySecret = lookupSecret()
    }
}

def loginVault() {
    def payload = payloadLogin()
    println ("Login Payload Equals" + payload)
    def response = httpRequest(
        customHeaders: [
                // [ name: "X-Vault-Namespace", value: "my-namespace"], 
                [ name: "Content-Type", value: "application/json" ]
            ],
        httpMode: 'POST',
        requestBody: "${payload}",
        url: "http://host.docker.internal:8200/v1/auth/approle/login"
    )
    def data = new JsonSlurper().parseText(response.content)
    println ("Session Token: " + data.auth.client_token)
    return data.auth.client_token
}

def payloadLogin() {
    withCredentials([string(credentialsId: 'vault_app_role_id', variable: 'VAULT_ROLE_ID'), string(credentialsId: 'vault_app_role_secret_id', variable: 'VAULT_SECRET_ID')]) {
        def payload = """
{
  "role_id": "$VAULT_ROLE_ID",
  "secret_id": "$VAULT_SECRET_ID"
}
    """
        return payload
    }
}

def lookupSecret() {
    println ("Using Token: " + "$authToken")
    def response_secret = httpRequest(
        customHeaders: [
                // [ name: "X-Vault-Namespace", value: "my-namespace"], 
                [ name: "X-Vault-Token", value: "$authToken" ]
            ],
        httpMode: 'GET',
        url: "http://host.docker.internal:8200/v1/secrets/creds/dev"
    )
    def mySecret = new JsonSlurper().parseText(response_secret.content)
    println ("Neat Secret: " + mySecret)
    return mySecret
}