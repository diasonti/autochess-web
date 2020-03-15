const baseUrl = 'http://localhost:5000'

const apiMap = {
    login: '/api/auth/login',
    logout: '/api/auth/logout',
    playersOnline: '/api/stats/online',
    fetchPlayer: '/api/fetch/player',
    fetchMatchHistory: '/api/fetch/match',
    stream: '/api/stream',
    searchStart: '/api/matchmaking/search/start',
    searchStop: '/api/matchmaking/search/stop',
}

export {baseUrl, apiMap}
