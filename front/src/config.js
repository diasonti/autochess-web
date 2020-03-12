const baseUrl = 'http://localhost:5000'

const apiMap = {
    login: '/api/auth/login',
    logout: '/api/auth/logout',
    fetch: '/api/auth/fetch',
    gameSearchGetToken: '/api/matchmaking/search/token',
    gameSearchSseStream: '/api/matchmaking/search/stream',
}

export {baseUrl, apiMap}
