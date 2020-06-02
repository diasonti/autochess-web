import Vue from 'vue'

const ensureTwoDigits = (value) => value < 10 ? '0'.concat(value) : value

Vue.filter('time', (value) => {
    if (!value || value <= 0)
        return '00:00'
    const seconds = parseInt(value)
    let sec = seconds % 60
    if (sec < 10)
        sec = '0' + sec

    let min = Math.floor(seconds / 60) % 60
    if (min < 10)
        min = '0' + min

    let hr = Math.floor(seconds / 3600)
    if (hr > 0 && hr < 10)
        hr = '0' + hr

    let time = min + ':' + sec
    if (hr > 0)
        time = hr + ':' + time
    return time
})

Vue.filter('localDateTime', (array) => {
    const year = array[2]
    const month = ensureTwoDigits(array[1])
    const day = ensureTwoDigits(array[0])
    const hour = ensureTwoDigits(array[3])
    const minute = ensureTwoDigits(array[4])
    const second = ensureTwoDigits(array[5])
    const date = year + '.' + month + '.' + day
    const time = hour + ':' + minute + ':' + second
    return date + ' ' + time
})
