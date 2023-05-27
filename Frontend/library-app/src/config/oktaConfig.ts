export const oktaConfig = {
    clientId: '0oa9ah0u3oAy2POQO5d7',
    issuer: 'https://dev-42074220.okta.com/oauth2/default',
    redirectUri: 'http://localhost:3000/login/callback',
    scopes: ['openid', 'profile', 'email'],
    pkce: true,
    disableHttpsCheck: true
}