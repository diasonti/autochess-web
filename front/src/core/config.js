const baseUrl = 'http://localhost:5000/api'

const apiMap = {
    login: '/auth/login',
    logout: '/auth/logout',
    playersOnline: '/stats/online',
    fetchPlayer: '/fetch/player',
    fetchMatchHistory: '/fetch/match',
    stream: '/stream',

    registration: '/registration/register',
    emailConfirmation: '/registration/confirm',

    fetchState: '/fetch/state',
    searchStart: '/intent/menu/search/start',
    searchStop: '/intent/menu/search/stop',
    moveIntent: '/intent/game/move',
}

export {baseUrl, apiMap}
