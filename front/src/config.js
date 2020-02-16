const baseUrl = 'http://localhost:5000'

const apiMap = {
    login: '/api/auth/login',
    refreshToken: '/api/auth/refresh',
    gameSearchGetToken: '/api/matchmaking/search/token',
    gameSearchSseStream: '/api/matchmaking/search/stream',
}

export {baseUrl, apiMap}
