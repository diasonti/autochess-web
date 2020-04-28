const baseUrl = 'http://localhost:5000'

const apiMap = {
    login: '/api/auth/login',
    logout: '/api/auth/logout',
    playersOnline: '/api/stats/online',
    fetchPlayer: '/api/fetch/player',
    fetchMatchHistory: '/api/fetch/match',
    stream: '/api/stream',

    fetchState: '/api/fetch/state',
    searchStart: '/api/intent/menu/search/start',
    searchStop: '/api/intent/menu/search/stop',
    moveIntent: '/api/intent/game/move',
}

export {baseUrl, apiMap}
